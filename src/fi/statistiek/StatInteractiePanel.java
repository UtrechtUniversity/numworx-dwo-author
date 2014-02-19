package fi.statistiek;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JPanel;

import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;

/**
 * Statistiek InteractiePanel MVC Controller
 * 
 * @author Manu Drijvers, Sylvia van Borkulo
 * 
 */
public class StatInteractiePanel extends JPanel implements InteractiePanel,
	ActionListener
{
	private StatModel model;
	private StatInteractiePanelView view;
	public static final boolean DEBUG = false;

	/**
	 * Constructor
	 */
	public StatInteractiePanel()
	{
		super(new BorderLayout());
		this.model = new StatModel();
		this.view = new StatInteractiePanelView(this.model, this);
		super.add(this.view, BorderLayout.CENTER);
	}

	public StatInteractiePanel(StatModel model)
	{
		super(new BorderLayout());
		this.model = model;
		this.view = new StatInteractiePanelView(this.model, this);
		super.add(this.view, BorderLayout.CENTER);
	}

	private void debugPrint(String s)
	{
		if (DEBUG)
		{
			System.out.println(s);
		}
	}

	public StatInteractiePanelView getView()
	{
		return view;
	}

	public void setView(StatInteractiePanelView view)
	{
		this.view = view;
	}

	public StatModel getModel()
	{
		return model;
	}

	public void setModel(StatModel model)
	{
		this.model = model;
		this.view.setModel(this.model);
	}

	public void zetOpdracht(Hashtable hashtable, String[] randomVars, Hashtable randomValues)
	{
		// Waarom randomVars en randomValues?
		// test syl
		System.out.println("StatInteractiePanel.zetOpdracht(hashtable=" + hashtable
		 + ", randomVars=" + randomVars + ", randomValues=" + randomValues);
		
		Hashtable b = deepCopy(hashtable);
		Hashtable resetHashtable = deepCopy(hashtable);

		// Deep copy the hashtable, else references will be copied and fields
		// within resetHashtable can be changed.
		this.model.setResetHashtable(resetHashtable);

		this.model.removeViewsWithoutEvent();

		if (b.containsKey("tableModel"))
		{
			this.model.getData().setState((Hashtable) b.get("tableModel"));

			// this.view.setModel(this.model);
		}

		ArrayList<Boolean> selectionList;
		if (b.containsKey("selectionList"))
		{
			selectionList = (ArrayList<Boolean>) b.get("selectionList");
			this.model.getData().setSelectionList(selectionList);
		}
		else
		{
			selectionList = new ArrayList<Boolean>(this.model.getData().getRowCount());
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
					statistiekViewTypes[i], "", this.model.getData(), 0, this);
				if (statistiekView != null)
				{
					statistiekView.setState(statistiekViewStates[i]);
					this.model.addView(statistiekView);
				}

			}
			// this.view.setModel(this.model);
		}

		if (b.containsKey("selectedView"))
		{
			// test syl
			System.out.println("StatInteractiePanel.zetOpdracht(): selectedView in hashtable = "
			 + ((Integer)b.get("selectedView")).intValue());
			this.view.processSelectedTab(((Integer) b.get("selectedView")).intValue());
			// test syl
//			this.view.processSelectedTab(1); // ?? geeft geen tab 1??!!
		}
	}

	public int getSelectedView()
	{
		return this.view.getSelectedView();
	}

	public void setSelectedTab(int tab)
	{
		this.view.processSelectedTab(tab);
	}

	public Hashtable getState()
	{
		Hashtable h = new Hashtable();

		if (this.model.getData().isDataEditable())
		{
			h.put("tableModel", this.model.getData().getState());
		}

		h.put("selectionList", this.model.getData().getSelectionList());

		if (this.model.getData().isViewsAddable()
			|| this.model.getData().isViewsEditable())
		{
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
		}

		int tabInt = this.model.mainWindowIndexToGeneralIndex(this.view
			.getSelectedView());
		h.put("selectedView", new Integer(tabInt));
		System.out.println("StatInteractiePanel.getState(): this.model.mainWindowIndexToGeneralIndex(this.view.getselectedView()="
			+ this.view.getSelectedView() + ") = " + tabInt);

		return h;
	}

	public void setState(Hashtable hashtable)
	{
		Hashtable b = deepCopy(hashtable);
		
		this.model.removeViewsWithoutEvent();

		if (b.containsKey("tableModel"))
		{
			this.model.getData().setState((Hashtable) b.get("tableModel"));
			// this.view.setModel(this.model);
		}
		if (b.containsKey("selectionList"))
		{
			this.model.getData().setSelectionList((ArrayList<Boolean>) b.get("selectionList"));
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
			String[] statistiekViewTypes = (String[]) b.get("statistiekViewTypes");
			Object[] statistiekViewStates = (Object[]) b.get("statistiekViewStates");

			for (int i = 0; i < statistiekViewTypes.length; i++)
			{
				StatistiekView statistiekView = Statistiek.createView(
					statistiekViewTypes[i], "", this.model.getData(), 0, this);
				if (statistiekView != null)
				{
					statistiekView.setState(statistiekViewStates[i]);
					this.model.addView(statistiekView);
				}

			}
			// this.view.setModel(this.model);
		}

		if (b.containsKey("selectedView"))
		{
			int index = ((Integer) b.get("selectedView")).intValue();
			// System.out.println("StatInteractiePanel.setState(): selectedView in hashtable = "
			// + index);
			this.view.processSelectedTab(index);
		}
	}

	public Hashtable getEditState()
	{
		// test syl
		System.out.println("StatInteractiePanel.getEditState()");
		return this.getState();
		//return null;
	}

	public void setEditState(Hashtable b)
	{
		this.setState(b);
	}

	public InteractieEditPanel getEditPanel()
	{
		return new StatEditPanelController();
	}

	public void setBounds(int x, int y, int b, int h)
	{
		super.setBounds(x, y, b, h);

	}

	public void wis()
	{
		// TODO Auto-generated method stub
	}

	public void zetMaat()
	{
		// TODO Auto-generated method stub
	}

	public int geefAsHoogte()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public int getIpId()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public int getScore()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public int getScoreMax()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean isCorrect()
	{
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isFout()
	{
		// TODO Auto-generated method stub
		return false;
	}

	public void zetMode(int mode)
	{
		// TODO Auto-generated method stub
	}

	public void zetNagekeken(boolean b)
	{
		// TODO Auto-generated method stub
	}

	public void stop()
	{
		// System.out.println("StatInteractiePanel.stop()");

		// close all dialogs
		ArrayList<Boolean> viewInOwnWindow = new ArrayList<Boolean>();
		for (int i = 0; i < this.model.getViewInOwnWindow().size(); i++)
		{
			viewInOwnWindow.add(false);
		}
		this.model.setViewInOwnWindow(viewInOwnWindow);
	}

	public void start()
	{
		// TODO Auto-generated method stub
	}

	public void destroy()
	{
		// TODO Auto-generated method stub
	}

	public void opnieuw()
	{
		// TODO Auto-generated method stub
	}

	public void kijkNa()
	{
		// TODO Auto-generated method stub
	}

	public void kijkNa(int stapNr)
	{
		// TODO Auto-generated method stub
	}

	public void addActionListener(ActionListener al)
	{
		// TODO Auto-generated method stub
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals("startVarBox"))
		{
			String s = this.view.getViewsBoxString();
			String t = null;
			if (Arrays.asList(Statistiek.VIEWS_translated).contains(s))
			{
				if (s == Statistiek.rb.getString("tableOption"))
				{
					t = Statistiek.VIEWS[0];
				}
				else if (s == Statistiek.rb.getString("histogramOption"))
				{
					t = Statistiek.VIEWS[1];
				}
				else if (s == Statistiek.rb.getString("dotplotOption"))
				{
					t = Statistiek.VIEWS[2];
				}
				else if (s == Statistiek.rb.getString("frequencytableOption"))
				{
					t = Statistiek.VIEWS[3];
				}
				else if (s == Statistiek.rb.getString("frequencypolygonOption"))
				{
					t = Statistiek.VIEWS[4];
				}
				else if (s == Statistiek.rb.getString("boxplotOption"))
				{
					t = Statistiek.VIEWS[5];
				}
				StatistiekView statistiekView = Statistiek.createView(t,
					this.model.findUniqueViewName(s), model.getData(),
					this.view.getStartVarBoxSelectedIndex(), this);
				this.model.addView(statistiekView);
				this.view.selectLastTab();
				this.view.clearAddViewTab();
			}
		}
		else if (e.getActionCommand().equals("viewsBox"))
		{
			this.view.setStartVarBox(true);
		}
		// resetbutton is now implemented in StatTable
		// else if(e.getActionCommand().equals("reset"))
		// {
		// Hashtable resetHashtable = this.model.getResetHashtable();
		//
		// // clear stringFrequencies
		// this.model.getData().clearStringFrequencies();
		//
		// //
		// System.out.println("reset clicked! this.model.getResetHashtable()="
		// // + resetHashtable);
		//
		// // Complete reset met zetOpdracht()
		// this.zetOpdracht(resetHashtable, null, null);
		// }
		else if (e.getSource() instanceof ButtonTabComponent.TabButton)
		{
			int mainTabIndex = this.view
				.indexOfTabComponent(((ButtonTabComponent.TabButton) (e
					.getSource())).getButtonTabComponent());
			int tab = this.model.mainWindowIndexToGeneralIndex(mainTabIndex);
			this.model.removeView(tab);
		}
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
}
