package fi.statistiek;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ToolTipManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.JTableHeader;
import fi.statistiek.addcolumndialog.AddColumnDialogController;
import fi.statistiek.addcolumndialog.AddColumnDialogModel;
import fi.statistiek.addcolumndialog.AddColumnDialogView;
import fi.statistiek.types.AllowedTypes;
import fi.statistiek.types.ColumnType;


/**
 * A Table StatistiekView
 * 
 * @author Manu Drijvers, Sylvia van Borkulo
 * 
 */
public class StatTable extends JPanel implements StatistiekView,
	TableModelListener, ActionListener, ListSelectionListener,
	SelectionListener
{
	private static final String RESET_ICON_PATH = "resources/reseticon.gif";
	private static final String DELIMITER = ";";
	private StatTableModel statTableModel;

	// Include field statInteractiePanel to process the reset actions
	private StatInteractiePanel statInteractiePanel;

	private JTable table;
	private String viewName;
	private JPopupMenu popup;
	private int popUpColumnIndex;
	private JTable rowTable;
	private JScrollPane scrollPane;

	private JPanel editDataPanel;
	private JButton addRowButton;
	private JButton addColumnButton;
	private JButton pasteButton;
	private JButton deleteRowsButton;
	private JButton resetButton;
	// test syl
	private JButton importButton;
	private JFileChooser fileChooser;

	/**
	 * Constructor without viewname
	 * 
	 * @param statTableModel
	 *            The datamodel
	 * @param statInteractiePanel
	 *            The StatInteractiePanel
	 */
	public StatTable(StatTableModel statTableModel,
		StatInteractiePanel statInteractiePanel)
	{
		super(new BorderLayout());
		this.statTableModel = statTableModel;
		this.statInteractiePanel = statInteractiePanel;
		this.viewName = "";
		this.setUp();
	}

	/**
	 * Constructor with viewname
	 * 
	 * @param statTableModel
	 *            The datamodel
	 * @param statInteractiePanel
	 *            The StatInteractiePanel
	 * @param viewName
	 *            The name of this view
	 */
	public StatTable(StatTableModel statTableModel,
		StatInteractiePanel statInteractiePanel, String viewName)
	{
		super(new BorderLayout());
		this.statTableModel = statTableModel;
		this.statInteractiePanel = statInteractiePanel;
		this.viewName = viewName;
		this.setUp();
	}

	/**
	 * Update field popup. If the data is not editable, the options edit column and
	 * delete column are not available.
	 */
	private void updatePopUp()
	{
		if (!this.statTableModel.isDataEditable())
		{
			if (this.popup.getSubElements().length == 3)
			{
				this.popup.remove(2);			
				this.popup.remove(1);
			}
		}
		else
		{
			if (this.popup.getSubElements().length == 1)
			{
				// add menu items
				JMenuItem editItem = new JMenuItem(Statistiek.rb.getString("editcolumnItem"));
				editItem.setActionCommand("editItem");
				editItem.addActionListener(this);
				JMenuItem deleteItem = new JMenuItem(Statistiek.rb.getString("deletecolumnItem"));
				deleteItem.setActionCommand("deleteItem");
				deleteItem.addActionListener(this);
				this.popup.add(editItem);
				this.popup.add(deleteItem);
			}
		}
	}

	/**
	 * Initialize
	 */
	private void setUp()
	{
		this.scrollPane = new JScrollPane();
		this.table = new JTable(this.statTableModel)
		{
			protected JTableHeader createDefaultTableHeader()
			{
				return new JTableHeader(columnModel)
				{
					public String getToolTipText(MouseEvent e)
					{
						String tip = null;
						java.awt.Point p = e.getPoint();
						int index = columnModel.getColumnIndexAtX(p.x);
						int realIndex = columnModel.getColumn(index)
							.getModelIndex();
						return StatTable.this.statTableModel.getColumnTypes()
							.get(realIndex).getUitleg();
					}
				};
			}
		};
		this.table.getSelectionModel().addListSelectionListener(this);
		this.statTableModel.addTableModelListener(this);
		this.statTableModel.addSelectionListener(this);
		this.scrollPane.setViewportView(this.table);

		this.rowTable = new RowNumberTable(this.table);
		this.scrollPane.setRowHeaderView(this.rowTable);
		this.scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER,
			this.rowTable.getTableHeader());

		this.popup = new JPopupMenu();
		JMenuItem sortItem = new JMenuItem(Statistiek.rb.getString("sortItem"));
		sortItem.setActionCommand("sortItem");
		sortItem.addActionListener(this);
		JMenuItem editItem = new JMenuItem(Statistiek.rb.getString("editcolumnItem"));
		editItem.setActionCommand("editItem");
		editItem.addActionListener(this);
		JMenuItem deleteItem = new JMenuItem(Statistiek.rb.getString("deletecolumnItem"));
		deleteItem.setActionCommand("deleteItem");
		deleteItem.addActionListener(this);
		this.popup.add(sortItem);
		if (this.statTableModel.isDataEditable())
		{
			this.popup.add(editItem);
			this.popup.add(deleteItem);
		}
		MouseListener popupListener = new PopupListener();
		this.table.getTableHeader().addMouseListener(popupListener);

		super.add(this.scrollPane, BorderLayout.CENTER);
		
		ToolTipManager.sharedInstance().setInitialDelay(0);
		ToolTipManager.sharedInstance().setReshowDelay(0);

		// maak editDataPanel
		GridLayout gl = new GridLayout();
		gl.setHgap(5);
		gl.setVgap(5);
		this.editDataPanel = new JPanel(gl);
		
		this.importButton = new JButton(Statistiek.rb.getString("importButton"));
		this.importButton.setToolTipText(Statistiek.rb.getString("importButton"));
		this.importButton.addActionListener(this);
		this.editDataPanel.add(this.importButton);

		this.addRowButton = new JButton(Statistiek.rb.getString("addrowButton"));
		this.addRowButton.setToolTipText(Statistiek.rb.getString("addrowButton"));
		this.addRowButton.addActionListener(this);
		this.editDataPanel.add(this.addRowButton);
		
		this.addColumnButton = new JButton(Statistiek.rb.getString("addcolumnButton"));
		this.addColumnButton.setToolTipText(Statistiek.rb.getString("addcolumnButton"));
		this.addColumnButton.addActionListener(this);
		this.editDataPanel.add(this.addColumnButton);
		
		this.deleteRowsButton = new JButton(Statistiek.rb.getString("deleteselectedrowsButton"));
		this.deleteRowsButton.setToolTipText(Statistiek.rb.getString("deleteselectedrowsButton"));
		this.deleteRowsButton.addActionListener(this);
		this.editDataPanel.add(this.deleteRowsButton);
		
		this.pasteButton = new JButton(Statistiek.rb.getString("pasteclipboardButton"));
		this.pasteButton.setToolTipText(Statistiek.rb.getString("pasteclipboardButton"));
		this.pasteButton.addActionListener(this);
		this.editDataPanel.add(this.pasteButton);
		
		this.resetButton = new JButton();
		try
		{
			Image img = ImageIO.read(getClass().getResource(RESET_ICON_PATH));
			resetButton.setIcon(new ImageIcon(img));
		}
		catch (IOException ex)
		{
		}
		this.resetButton.setToolTipText(Statistiek.rb.getString("resetButton"));
		this.resetButton.addActionListener(this);
		this.editDataPanel.add(this.resetButton);
		
		this.editDataPanel.setVisible(this.statTableModel.isDataEditable());

		// set the right selection
		this.selectionChanged();

		super.add(this.editDataPanel, BorderLayout.SOUTH);
		
		// set up the file chooser to open a data file
		setUpFileChooser();
	}

	private void setUpFileChooser()
	{
		this.fileChooser = new JFileChooser();
		try
		{
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"CSV files (*csv)", "csv");
		    fileChooser.setFileFilter(filter);
		    fileChooser.setAcceptAllFileFilterUsed(false);
		}
		catch (Exception e)
		{
			System.out.println("Creating FileNameExtensionFilter failed. " + e.toString());
		}
	}

	class PopupListener extends MouseAdapter
	{
		public void mousePressed(MouseEvent e)
		{
		}

		public void mouseReleased(MouseEvent e)
		{
			if (e.isPopupTrigger())
			{
				Point p = e.getPoint();
				int column = StatTable.this.table.columnAtPoint(p);

				// convert view index to model index
				StatTable.this.popUpColumnIndex = StatTable.this.table
					.convertColumnIndexToModel(column);
				StatTable.this.popup.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}

	/**
	 * Implementation of TableModelListener
	 * 
	 * This is necessary because HEADER_ROW_CHANGED events reset the
	 * cellRenderers. On such an event this method will restore the
	 * cellRenderers
	 */
	public void tableChanged(TableModelEvent e)
	{
		// System.out.println("Table changed");

		if (e.getFirstRow() == TableModelEvent.HEADER_ROW
			&& this.statTableModel != null)
		{
			// a HEADER_ROW CHANGED TableModelEvent resets the cellRenderers, so
			// set them again.
			this.setCellRenderers();
		}

		this.editDataPanel.setVisible(this.statTableModel.isDataEditable());
		this.updatePopUp();
	}

	/**
	 * Set the right cellRenderers
	 */
	private void setCellRenderers()
	{
		// System.out.println("Setting cell renderers!");
		int i = 0;
		for (ColumnType type : this.statTableModel.getColumnTypes())
		{
			if (type.getType().equals(AllowedTypes.ENUM))
			{
				// Enums get a dropdownbox as editor
				JComboBox box = new JComboBox();
				for (String s : type.getEnumOptions())
				{
					box.addItem(s);
				}
				this.table.getColumnModel().getColumn(i)
					.setCellEditor(new DefaultCellEditor(box));
			}
			i++;
		}
	}

	/**
	 * Change this view's model
	 * 
	 * @param model
	 *            the new StatTableModel
	 */
	public void setModel(StatTableModel model)
	{
		this.statTableModel = model;
		this.statTableModel.addTableModelListener(this);
		this.statTableModel.addSelectionListener(this);
		this.table = new JTable(this.statTableModel)
		{
			protected JTableHeader createDefaultTableHeader()
			{
				return new JTableHeader(columnModel)
				{
					public String getToolTipText(MouseEvent e)
					{
						String tip = null;
						java.awt.Point p = e.getPoint();
						int index = columnModel.getColumnIndexAtX(p.x);
						int realIndex = columnModel.getColumn(index)
							.getModelIndex();
						return StatTable.this.statTableModel.getColumnTypes()
							.get(realIndex).getUitleg();
					}
				};
			}
		};
		this.table.getSelectionModel().addListSelectionListener(this);
		this.rowTable.setModel(this.statTableModel);

		this.scrollPane.setViewportView(this.table);

		this.rowTable = new RowNumberTable(this.table);
		this.scrollPane.setRowHeaderView(this.rowTable);
		this.scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER,
			this.rowTable.getTableHeader());

		MouseListener popupListener = new PopupListener();
		this.table.getTableHeader().addMouseListener(popupListener);

		this.editDataPanel.setVisible(this.statTableModel.isDataEditable());

		this.setCellRenderers();
	}

	/**
	 * StatistiekView implementation
	 */
	public JComponent getComponent()
	{
		return this;
	}

	/**
	 * StatistiekView implementation
	 * 
	 * @return this view's name
	 */
	public String getViewName()
	{
		return this.viewName;
	}

	public void setViewName(String s)
	{
		this.viewName = s;

	}

	/**
	 * Override setBounds
	 */
	public void setBounds(int x, int y, int b, int h)
	{
		super.setBounds(x, y, b, h);
	}

	/**
	 * Determine which rows are currently selected
	 * 
	 * @return an array containing indices of selected rows
	 */
	public int[] getSelectedRows()
	{
		return this.table.getSelectedRows();
	}

	/**
	 * Determine which column is currently selected
	 * 
	 * @return the selected column's index
	 */
	public int[] getSelectedColumns()
	{
		return this.table.getSelectedColumns();
	}

	/**
	 * StatistiekView implementation
	 */
	public void setUp(Frame owner)
	{
		// nothing to set up here
	}

	/**
	 * StatistiekView implementation
	 */
	public void setUp(Dialog owner)
	{
		// nothing to set up here
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
			clipboardString = (String) clipboardData
				.getTransferData(DataFlavor.stringFlavor);
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
			if (s.split("\t").length != this.statTableModel.getColumnCount())
			{
				// row has incorrect amount of cells
				return;
			}
		}

		int currentRow = this.statTableModel.getRowCount();
		int currentColumn;
		for (String s : rowStrings)
		{
			this.statTableModel.addRowWithoutEvent();
			currentColumn = 0;
			for (String cellString : s.split("\t"))
			{
				this.statTableModel.setValueAtWithoutEvent(cellString,
					currentRow, currentColumn);
				currentColumn++;
			}
			currentRow++;
		}

		this.statTableModel.fireTableModelEvent();
	}

	public void actionPerformed(ActionEvent e)
	{
		String actionCommand = e.getActionCommand();
		if (e.getSource() == this.addRowButton)
		{
			this.statTableModel.addRow();
		}
		else if (e.getSource() == this.addColumnButton)
		{
			AddColumnDialogModel dialogModel = new AddColumnDialogModel(
				this.statTableModel);
			AddColumnDialogView dialogView;

			// Try to find the top level ancestor (Dialog or Frame)
			Container c = Statistiek.getTopLevelAcestor(this);
			if (c instanceof Frame)
			{
				dialogView = new AddColumnDialogView((Frame) c, dialogModel);
			}
			else if (c instanceof Dialog)
			{
				dialogView = new AddColumnDialogView((Dialog) c, dialogModel);
			}
			else
			{
				System.out.println("Error finding top level frame/dialog");
				return;
			}
			AddColumnDialogController dialogController = new AddColumnDialogController(
				dialogModel, dialogView);

			dialogView.setVisible(true);

			if (dialogModel.getDonePressed())
			{
				this.statTableModel.addColumn(dialogModel.getName(),
					new ColumnType(dialogModel));
			}
		}
		else if (e.getSource() == this.deleteRowsButton)
		{
			int[] toRemove = this.getSelectedRows(); // TODO ordered?
			if (toRemove.length > 0)
			{
				for (int i = toRemove.length - 1; i >= 0; i--)
				{
					this.statTableModel.removeRow(toRemove[i]);
				}
			}
		}
		else if (e.getSource() == this.pasteButton)
		{
			this.pasteClipboardData();
		}
		else if (actionCommand.equals("sortItem"))
		{
			this.statTableModel.sort(this.popUpColumnIndex);
		}
		else if (actionCommand.equals("deleteItem"))
		{
			this.statTableModel.removeColumn(this.popUpColumnIndex);
		}
		else if (actionCommand.equals("editItem"))
		{
			AddColumnDialogModel m = new AddColumnDialogModel(
				this.statTableModel,
				this.statTableModel.getColumnName(this.popUpColumnIndex),
				this.statTableModel.getColumnTypes().get(popUpColumnIndex),
				this.popUpColumnIndex);
			AddColumnDialogView v;

			Container c = Statistiek.getTopLevelAcestor(this);
			if (c instanceof Frame)
			{
				v = new AddColumnDialogView((Frame) c, m);
			}
			else if (c instanceof Dialog)
			{
				v = new AddColumnDialogView((Dialog) c, m);
			}
			else
			{
				System.out.println("Error finding top level frame/dialog.");
				return;
			}
			AddColumnDialogController c2 = new AddColumnDialogController(m, v);

			v.setVisible(true);

			if (m.getDonePressed())
			{
				this.statTableModel.editColumn(this.popUpColumnIndex,
					m.getName(), new ColumnType(m));
			}
		}
		else if (e.getSource() == this.resetButton)
		{
			if (this.statInteractiePanel != null)
			{
    			Hashtable resetHashtable = this.statInteractiePanel.getModel()
    				.getResetHashtable();
    
    			// clear stringFrequencies
    			this.statTableModel.clearStringFrequencies();

    			// clear selectionList and listeners
    			this.statTableModel.clearSelectionList();
    			this.statTableModel.clearListeners();
    
    			// System.out.println("reset clicked! this.statInteractiePanel.getModel().getResetHashtable()="
    			// + resetHashtable);
    
    			// Complete reset met zetOpdracht()
    			this.statInteractiePanel.getView().getController()
    				.zetOpdracht(resetHashtable, null, null);
			} // else the button is clicked in edit-mode: do nothing
		}
		// test syl
		else if (e.getSource() == this.importButton)
		{
			if (this.statTableModel.getRowCount() > 0)
			{
				int reply = JOptionPane.showConfirmDialog(null, 
					Statistiek.rb.getString("importWarning"),
					"Waarschuwing", JOptionPane.OK_CANCEL_OPTION);
				if (reply == JOptionPane.OK_OPTION)
				{
					openFileChooserDialog();
				}
			}
			else
			{
				openFileChooserDialog();
			}
		}
	}

	/*
	 * Opent de dialoog voor het openen van een data bestand.
	 */
	private void openFileChooserDialog()
	{
		int returnVal;
		
		returnVal = this.fileChooser.showOpenDialog(this);
		
		if (returnVal == JFileChooser.APPROVE_OPTION) 
		{
//			System.out.println("You chose to open this file: " +
//				fileChooser.getSelectedFile().getName());
			
			// Remove old views
			this.removeViews();
			
			processCSVDataFile(fileChooser.getSelectedFile());
		}
	}
	
	/**
	 * Remove views except Table.
	 */
	private void removeViews()
	{
		ArrayList<StatistiekView> views = new ArrayList<StatistiekView>();
		
		// In edit-mode is statInteractiePanel null
		if (this.statInteractiePanel != null)
			views = this.statInteractiePanel.getModel().getViews();
		
        Iterator<StatistiekView> iterator = views.iterator();
        while (iterator.hasNext()) 
        {
        	StatistiekView view = iterator.next();
        	if (!view.getViewName().equals(this.viewName))
        	{
        		iterator.remove();
        		this.statInteractiePanel.getModel().removeView(view.getViewName());
        	}
        }
	}

	
	/*
	 * Create columns based on the names. 
	 */
	private void createColumns(String[] names)
	{
		//System.out.println("StatTable.createColumns(): " + names.toString());
		for (int i = 0; i < names.length; i++)
		{
    		this.statTableModel.addColumn(names[i],
    			new ColumnType(AllowedTypes.STRING));
		}
	}

	/*
	 * Remove all columns. 
	 */
	private void removeColumns()
	{
		//System.out.println("StatTable.removeColumns()");
		for (int i = this.statTableModel.getColumnCount() - 1; i >= 0; i--)
		{
    		this.statTableModel.removeColumn(i);
		}
	}

	/*
	 * Clear the statTableModel.
	 */
	private void clearStatTableModel()
	{
		// statTable rij voor rij, kolom voor kolom leegmaken
		int numberOfRows = this.statTableModel.getRowCount();
		for (int i = numberOfRows - 1; i >= 0; i--)
		{
			//System.out.println("StatTable.clearStatTableModel(): remove row " + i);
			this.statTableModel.removeRow(i);
		}

		int numberOfColumns = this.statTableModel.getColumnCount(); 
		for (int i = numberOfColumns - 1; i >=0; i--)
		{
			//System.out.println("StatTable.clearStatTableModel(): remove column " + i);
			this.statTableModel.removeColumn(i);
		}
	}

	/*
	 * Process the CSV data file.
	 */
	private void processCSVDataFile(File file)
	{
		BufferedReader br = null;
		String line = "";
		ArrayList<String> dataRows = null;
		String[] headers = null;
	 
		try 
		{
			br = new BufferedReader(new FileReader(file));
			dataRows = new ArrayList<String>();
			
			// read the header line
			if ((line = br.readLine()) != null)
			{
				headers = line.split(StatTable.DELIMITER);
			}

			// read the data
			while ((line = br.readLine()) != null) 
			{
				dataRows.add(line);
			}
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			if (br != null)
			{
				try 
				{
					br.close();
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
	 
		// clear the old data
		this.clearStatTableModel();
		
		// create string columns from headers
		this.createColumns(headers);
		
		// add row data
		this.addDataRows(dataRows);
		
		this.statTableModel.updateNumericalColumnTypes();
		
		// update the view
		if (this.statInteractiePanel != null)
			this.statInteractiePanel.getView().update(null, null);
	}

	/**
	 * Add the row data to the table.
	 * @param dataRows
	 */
	private void addDataRows(ArrayList<String> dataRows)
	{
        Iterator<String> rowIterator = dataRows.iterator();
        int rowIndex = 0;
        while (rowIterator.hasNext()) 
        {
        	String dataRow = rowIterator.next();
        	this.statTableModel.addRow();
        	
        	// add the data
        	String[] values = dataRow.split(";");
        	int columnIndex = 0;
            for (int i = 0; i < values.length; i++) 
            {
            	String value = values[i];
//                System.out.println("value = " + value);
                this.statTableModel.setValueAt(value, rowIndex, columnIndex);
                
                columnIndex++;
            }
        	
        	rowIndex++;
        }
	}

	public Object getState()
	{
		return this.viewName;
	}

	public void setState(Object state)
	{
		if (state instanceof String)
		{
			this.viewName = (String) state;
		}
		this.setCellRenderers();
	}

	public String getViewType()
	{
		return "Table";
	}

	public void valueChanged(ListSelectionEvent arg0)
	{
		if (!arg0.getValueIsAdjusting())
		{
			ArrayList<Boolean> newSelection = new ArrayList<Boolean>(
				this.statTableModel.getRowCount());
			for (int row = 0; row < this.statTableModel.getRowCount(); row++)
			{
				newSelection.add(this.table.isRowSelected(row));
				// System.out.println(this.table.isRowSelected(row));
			}
			System.out.println();
			this.statTableModel.setSelectionList(newSelection);
		}
	}

	public void selectionChanged()
	{
		// test syl
		//System.out.println("selection Changed called");
		ListSelectionModel selectionModel = this.table.getSelectionModel();
		selectionModel.removeListSelectionListener(this);

		for (int row = 0; row < this.statTableModel.getRowCount(); row++)
		{
			if (this.statTableModel.isRowSelected(row)
				&& !selectionModel.isSelectedIndex(row))
			{
				// System.out.println("Selecting row " + row);
				selectionModel.addSelectionInterval(row, row);
			}
			else if (!this.statTableModel.isRowSelected(row)
				&& selectionModel.isSelectedIndex(row))
			{
				// System.out.println("Deselecting row " + row);
				selectionModel.removeSelectionInterval(row, row);
			}
		}
		selectionModel.addListSelectionListener(this);
	}

	public String toString()
	{
		return this.getViewName();
	}

	public StatInteractiePanel getStatInteractiePanel()
	{
		return statInteractiePanel;
	}

	public void setStatInteractiePanel(StatInteractiePanel statInteractiePanel)
	{
		this.statInteractiePanel = statInteractiePanel;
	}
}
