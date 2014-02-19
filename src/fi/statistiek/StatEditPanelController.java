package fi.statistiek;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JPanel;

import fi.beans.wiskopdrbeans.InteractieEditPanel;

/**
 * Statistiek InteractieEditPanel MVC Controller
 * 
 * @author Manu Drijvers, Sylvia van Borkulo
 * 
 */
public class StatEditPanelController extends JPanel implements
	InteractieEditPanel, ActionListener
{
	private StatModel model;
	private StatEditPanelView view;
	public static final String TABLE_VIEW_NAME = "Table";

	// public static String TABLE_VIEW_NAME;// = "Table";

	/**
	 * Constructor
	 */
	public StatEditPanelController()
	{
		super(new BorderLayout());
		this.model = new StatModel();
		this.view = new StatEditPanelView(this.model, this);
		super.add(this.view, BorderLayout.CENTER);
	}

	/**
	 * Try to paste clipboard data into the tablemodel Splits data over cells
	 * with tabs ('\t') and over rows with newlines ('\n') Will only paste the
	 * data into the model if amount of cells at every line corresponds with the
	 * amount of columns
	 */
	public void pasteClipboardData()
	{
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable clipboardData = clipboard.getContents(this);
		String clipboardString;
		try
		{
			clipboardString = (String) clipboardData.getTransferData(DataFlavor.stringFlavor);
		}
		catch (UnsupportedFlavorException e)
		{
			System.out.println("UnsupportedFlavorException in AddColmnDialogController.pasteClipboardData");
			e.printStackTrace();
			return;
		}
		catch (IOException e)
		{
			System.out.println("IOException in AddColmnDialogController.pasteClipboardData");
			e.printStackTrace();
			return;
		}

		String[] rowStrings = clipboardString.split("\n");

		// check the amount of cells in each row
		for (String s : rowStrings)
		{
			if (s.split("\t").length != this.model.getData().getColumnCount())
			{
				// row has incorrect amount of cells
				return;
			}
		}

		int currentRow = this.model.getData().getRowCount();
		int currentColumn;
		for (String s : rowStrings)
		{
			this.model.getData().addRowWithoutEvent();
			currentColumn = 0;
			for (String cellString : s.split("\t"))
			{
				this.model.getData().setValueAtWithoutEvent(cellString,
					currentRow, currentColumn);
				currentColumn++;
			}
			currentRow++;
		}

		this.model.getData().fireTableModelEvent();
	}

	/**
	 * deeply clones a Hashtable by serializing and deserializing.
	 */
	private Hashtable<String, Object> deepCopy(Hashtable<String, Object> original)
	{
		Object copy = null;
		try
		{
			// Write the object out to a byte array
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bos);
			out.writeObject(original);
			out.flush();
			out.close();

			// Make an input stream from the byte array and read
			// a copy of the object back in.
			ObjectInputStream in = new ObjectInputStream(
				new ByteArrayInputStream(bos.toByteArray()));
			copy = in.readObject();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (ClassNotFoundException cnfe)
		{
			cnfe.printStackTrace();
		}
		return (Hashtable) copy;
	}

	/**
	 * Restores the state
	 * 
	 * @param b
	 *            Hashtable containing a state
	 */
	public void setEditState(Hashtable hashtable)
	{
		Hashtable b = deepCopy(hashtable);

		this.model.removeViewsWithoutEvent();

		if (b.containsKey("tableModel"))
		{
			this.model.getData().setState((Hashtable) b.get("tableModel"));
		}
		if (b.containsKey("selectionList"))
		{
			this.model.getData().setSelectionList(
				(ArrayList<Boolean>) b.get("selectionList"));
		}
		else
		{
			ArrayList<Boolean> selectionList = new ArrayList<Boolean>(
				this.model.getData().getRowCount());
			for (int i = 0; i < this.model.getData().getRowCount(); i++)
			{
				selectionList.add(false);
			}
			this.model.getData().setSelectionList(selectionList);
		}

		if (b.containsKey("statistiekViewTypes")
			&& b.containsKey("statistiekViewStates"))
		{
			String[] statistiekViewTypes = (String[]) b
				.get("statistiekViewTypes");
			Object[] statistiekViewStates = (Object[]) b
				.get("statistiekViewStates");

			for (int i = 0; i < statistiekViewTypes.length; i++)
			{
				StatistiekView statistiekView = Statistiek.createView(
					statistiekViewTypes[i], "", this.model.getData(), 0, null);
				if (statistiekView != null)
				{
					statistiekView.setState(statistiekViewStates[i]);
					this.model.addView(statistiekView);
				}
			}
		}

		if (b.containsKey("selectedView"))
		{
			this.view.setInteractiePanelSelectedView(((Integer) b
				.get("selectedView")).intValue());
		}
	}

	/**
	 * Returns the current state in a hashtable
	 * 
	 * @return A hashtable containing the current state
	 */
	public Hashtable getEditState()
	{
		System.out.println("EditPanel.getEditState()");
		Hashtable h = new Hashtable();
		h.put("tableModel", this.model.getData().getState());
		h.put("selectionList", this.model.getData().getSelectionList());

		int noViews = this.model.getViews().size();
		String[] statistiekViewTypes = new String[noViews];
		Object[] statistiekViewStates = new Object[noViews];
		for (int i = 0; i < noViews; i++)
		{
			statistiekViewTypes[i] = this.model.getViews().get(i).getViewType();
			statistiekViewStates[i] = this.model.getViews().get(i).getState();
		}

		h.put("statistiekViewTypes", statistiekViewTypes);
		h.put("statistiekViewStates", statistiekViewStates);

		h.put(
			"selectedView",
			new Integer(this.model.mainWindowIndexToGeneralIndex(this.view
				.getInteractiePanelSelectedView())));

		return h;
	}

	/**
	 * Override setBounds
	 */
	public void setBounds(int x, int y, int b, int h)
	{
		if (!super.getBounds().equals(new Rectangle(x, y, b, h)))
		{
			super.setBounds(x, y, b, h);
		}
	}

	public void zetBreedte(int b)
	{
		// TODO Auto-generated method stub
	}

	public void zetHoogte(int h)
	{
		// TODO Auto-generated method stub
	}

	public void wis()
	{
		// TODO Auto-generated method stub
	}

	public void zetMode(int mode)
	{
		// TODO Auto-generated method stub
	}

	public void stop()
	{
		this.view.getInteractiePanel().stop();
	}

	public void start()
	{
		// TODO Auto-generated method stub
	}

	public void addActionListener(ActionListener al)
	{
		// TODO Auto-generated method stub
	}

	/**
	 * ActionListener implementation
	 */
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals("dataEditableBox"))
		{
			int selectedView = this.view.getInteractiePanelSelectedView();
			this.model.getData().setDataEditable(
				this.view.isDataEditableBoxSelected());

			this.view.setInteractiePanelSelectedView(selectedView);
		}
		else if (e.getActionCommand().equals("viewsEditableBox"))
		{
			int selectedView = this.view.getInteractiePanelSelectedView();
			this.model.getData().setViewsEditable(
				this.view.isViewsEditableBoxSelected());

			if (!this.model.getData().isViewsEditable())
			{
				// addable without editable is useless, so disable addable
				this.model.getData().setViewsAddable(false);
			}

			this.view.setInteractiePanelSelectedView(selectedView);
		}
		else if (e.getActionCommand().equals("viewsAddableBox"))
		{
			int selectedView = this.view.getInteractiePanelSelectedView();
			this.model.getData().setViewsAddable(
				this.view.isViewsAddableBoxSelected());
			this.view.setInteractiePanelSelectedView(selectedView);
		}
	}
}
