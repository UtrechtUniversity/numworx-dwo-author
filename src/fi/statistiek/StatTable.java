package fi.statistiek;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ToolTipManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import org.cbook.cbookif.CBookEventHandler;

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
	private JPopupMenu headerPopup;
	/**
	 * A popup with options for marking the cell and the row as outlier.
	 */
	private JPopupMenu outlierPopup;
	/**
	 * Item in the outlierPopup.
	 */
	private JMenuItem outlierCellItem;
	/**
	 * Item in the outlierPopup.
	 */
	private JMenuItem outlierRowItem;
	/**
	 * A popup with only the option for marking the row as outlier.
	 * Used for right clicking the row numbers in the table.
	 */
	private JPopupMenu rowOutlierPopup;
	/**
	 * Item in the rowOutlierPopup.
	 */
	private JMenuItem rowOutlierRowItem;
	private int popUpColumnIndex;
	private int outlierColumnIndex;
	private int outlierRowIndex;
	private JTable rowTable;
	private JScrollPane scrollPane;

	private JPanel editDataPanel;
	private JButton addRowButton;
	private JButton addColumnButton;
	private JButton pasteButton, copyButton;
	private JButton deleteRowsButton;
	private JButton resetButton;
	// test syl
	private JButton importButton;
	private JFileChooser fileChooser;
	private CBookEventHandler handler;
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
	 * Update the outlier popup.
	 */
	private void updateOutlierPopup(int rowIndex, int columnIndex)
	{
		if (this.statTableModel.isOutlier(rowIndex, columnIndex))
			this.outlierCellItem.setText(Statistiek.rb.getString("demarkOutlierCell"));
		else
			this.outlierCellItem.setText(Statistiek.rb.getString("markOutlierCell"));
		
		if (this.statTableModel.isOutlier(rowIndex))
			this.outlierRowItem.setText(Statistiek.rb.getString("demarkOutlierRow"));
		else
			this.outlierRowItem.setText(Statistiek.rb.getString("markOutlierRow"));
	}

	/**
	 * Update the row outlier popup.
	 */
	private void updateRowOutlierPopup(int rowIndex)
	{
		if (this.statTableModel.isOutlier(rowIndex))
			this.rowOutlierRowItem.setText(Statistiek.rb.getString("demarkOutlierRow"));
		else
			this.rowOutlierRowItem.setText(Statistiek.rb.getString("markOutlierRow"));
	}

	/**
	 * Update the header popup. If the data is not editable, the options edit column and
	 * delete column are not available.
	 */
	private void updateHeaderPopUp()
	{
		if (!this.statTableModel.isDataEditable())
		{
			if (this.headerPopup.getSubElements().length == 4)
			{
				// remove options edit and delete
				int indexEditItem = 1;
				int indexDeleteItem = 2;
				this.headerPopup.remove(indexDeleteItem);
				this.headerPopup.remove(indexEditItem);
			}
		}
		else
		{
			if (this.headerPopup.getSubElements().length == 2)
			{
				// add menu items edit and delete
				JMenuItem editItem = new JMenuItem(Statistiek.rb.getString("editcolumnItem"));
				editItem.setActionCommand("editItem");
				editItem.addActionListener(this);
				JMenuItem deleteItem = new JMenuItem(Statistiek.rb.getString("deletecolumnItem"));
				deleteItem.setActionCommand("deleteItem");
				deleteItem.addActionListener(this);
				this.headerPopup.add(editItem, 1);
				this.headerPopup.add(deleteItem, 2);
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
						String text = null;
						java.awt.Point p = e.getPoint();
						int index = columnModel.getColumnIndexAtX(p.x);
						int realIndex = columnModel.getColumn(index)
							.getModelIndex();
						text = StatTable.this.statTableModel.getColumnTypes()
							.get(realIndex).getUitleg();
						
						return text;
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

		this.headerPopup = new JPopupMenu();
		JMenuItem sortAscendingItem = new JMenuItem(Statistiek.rb.getString("sortAscendingItem"));
		sortAscendingItem.setActionCommand("sortAscendingItem");
		sortAscendingItem.addActionListener(this);
		
		JMenuItem sortDescendingItem = new JMenuItem(Statistiek.rb.getString("sortDescendingItem"));
		sortDescendingItem.setActionCommand("sortDescendingItem");
		sortDescendingItem.addActionListener(this);
		
		JMenuItem editItem = new JMenuItem(Statistiek.rb.getString("editcolumnItem"));
		editItem.setActionCommand("editItem");
		editItem.addActionListener(this);
		
		JMenuItem deleteItem = new JMenuItem(Statistiek.rb.getString("deletecolumnItem"));
		deleteItem.setActionCommand("deleteItem");
		deleteItem.addActionListener(this);
		
		JMenuItem columnInfoItem = new JMenuItem(Statistiek.rb.getString("infocolumnItem"));
		columnInfoItem.setActionCommand("infocolumnItem");
		columnInfoItem.addActionListener(this);
		
		this.headerPopup.add(sortAscendingItem);
		this.headerPopup.add(sortDescendingItem);
		if (this.statTableModel.isDataEditable())
		{
			this.headerPopup.add(editItem);
			this.headerPopup.add(deleteItem);
		}
		this.headerPopup.add(columnInfoItem);
		
		MouseListener headerPopupListener = new HeaderPopupListener();
		this.table.getTableHeader().addMouseListener(headerPopupListener);
		
		// voor outliers
		this.outlierPopup = new JPopupMenu();
		this.outlierCellItem = new JMenuItem(Statistiek.rb.getString("markOutlierCell"));
		this.outlierCellItem.setActionCommand("outlierCell");
		this.outlierCellItem.addActionListener(this);
		
		this.outlierRowItem = new JMenuItem(Statistiek.rb.getString("markOutlierRow"));
		this.outlierRowItem.setActionCommand("outlierRow");
		this.outlierRowItem.addActionListener(this);
		
		this.outlierPopup.add(outlierCellItem);
		this.outlierPopup.add(outlierRowItem);
		
		this.rowOutlierPopup = new JPopupMenu();
		this.rowOutlierRowItem = new JMenuItem(Statistiek.rb.getString("markOutlierRow"));
		this.rowOutlierRowItem.setActionCommand("outlierRow");
		this.rowOutlierRowItem.addActionListener(this);
		
		this.rowOutlierPopup.add(rowOutlierRowItem);
		
		MouseListener outlierPopupListener = new OutlierPopupListener();
		this.table.addMouseListener(outlierPopupListener);
		// outlier rechtermuisknopopties op rijnummers
		MouseListener rowOutlierPopupListener = new RowOutlierPopupListener();
		this.rowTable.addMouseListener(rowOutlierPopupListener);

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

		this.copyButton = new JButton(Statistiek.rb.getString("copyclipboardButton"));
		this.copyButton.setToolTipText(Statistiek.rb.getString("copyclipboardButton"));
		this.copyButton.addActionListener(this);
		this.editDataPanel.add(this.copyButton);
		
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

	class HeaderPopupListener extends MouseAdapter
	{
		public void mousePressed(MouseEvent e)
		{
		}

		public void mouseReleased(MouseEvent e)
		{
			if (e.isPopupTrigger() 
				|| e.getButton() == MouseEvent.BUTTON3 || e.isControlDown())  // voor mac
			{
				Point p = e.getPoint();
				int column = StatTable.this.table.columnAtPoint(p);

				// convert view index to model index
				StatTable.this.popUpColumnIndex = StatTable.this.table
					.convertColumnIndexToModel(column);
				StatTable.this.headerPopup.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	} // class PopupListener

	/**
	 * Class for handling right mouse click on a cell in the table.
	 * @author borku102
	 *
	 */
	class OutlierPopupListener extends MouseAdapter
	{
		public void mousePressed(MouseEvent e)
		{
		}

		public void mouseReleased(MouseEvent e)
		{
			if (e.isPopupTrigger() 
				|| e.getButton() == MouseEvent.BUTTON3 || e.isControlDown())  // voor mac
			{
				Point p = e.getPoint();
				int column = StatTable.this.table.columnAtPoint(p);
				int row = StatTable.this.table.rowAtPoint(p);

				// convert view index to model index
				StatTable.this.outlierColumnIndex = StatTable.this.table
					.convertColumnIndexToModel(column);
				StatTable.this.outlierRowIndex = StatTable.this.table
					.convertRowIndexToModel(row);
				
				// Is dit de goede plek?
				StatTable.this.updateOutlierPopup(row, column);
				StatTable.this.outlierPopup.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	} // class OutlierPopupListener
	
	/**
	 * Class for handling right mouse click on a cell in the row number column in the table.
	 * @author borku102
	 *
	 */
	class RowOutlierPopupListener extends MouseAdapter
	{
		public void mousePressed(MouseEvent e)
		{
		}

		public void mouseReleased(MouseEvent e)
		{
			if (e.isPopupTrigger() 
				|| e.getButton() == MouseEvent.BUTTON3 || e.isControlDown())  // voor mac
			{
				Point p = e.getPoint();
				int row = StatTable.this.table.rowAtPoint(p);

				// convert view index to model index
				StatTable.this.outlierRowIndex = StatTable.this.table
					.convertRowIndexToModel(row);
				
				// Is dit de goede plek?
				StatTable.this.updateRowOutlierPopup(row);
				StatTable.this.rowOutlierPopup.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	} // class PopupListener
	
	/**
	 * Class to render outliers.
	 * 
	 * @author Sylvia van Borkulo
	 *
	 */
	static class OutlierRenderer extends DefaultTableCellRenderer
	{
		Color backgroundColor = getBackground();
		Color selectedOutlierColor = ColorGenerator.getBackgroundSelectedTableOutlier();
		Color outlierColor = ColorGenerator.getBackgroundTableOutlier();

        @Override
		public Component getTableCellRendererComponent(JTable table,
			Object value, boolean isSelected, boolean hasFocus, int row,
			int column)
		{
			Component c = super.getTableCellRendererComponent(table, value,
				isSelected, hasFocus, row, column);
			StatTableModel model = (StatTableModel) table.getModel();
			if (model.isOutlier(row, column))
			{
				if (isSelected)
					c.setBackground(selectedOutlierColor);
				else
					c.setBackground(outlierColor);
			}
			else
			{
				if (!isSelected)
				{
					c.setBackground(backgroundColor);
				}
			}
			return c;
		}

	}// class OutlierRenderer
	
	/**
	 * Class to render decimal format correctly depending on language settings.
	 * 
	 * @author Sylvia van Borkulo
	 *
	 */
	static class DecimalRenderer extends DefaultTableCellRenderer
	{
		DecimalFormat df;
		Color backgroundColor = getBackground();
		Color selectedOutlierColor = ColorGenerator.getBackgroundSelectedTableOutlier();
		Color outlierColor = ColorGenerator.getBackgroundTableOutlier();

		public DecimalRenderer()
		{
			super();
		}

		public void setValue(Object value)
		{
			if (value==null || ((String)value).equals(ColumnType.WILDCARD))
			{
				// a wildcard remains a wildcard
				setText((String)value);
			}
			else
			{
				// set the text with value in the correct format
				Double doubleValue = Double.parseDouble((String) value);
				df = Statistiek.getDecimalFormat(doubleValue);
				setText((value == null) ? "" : df.format(doubleValue));
			}
		}
		
        @Override
		public Component getTableCellRendererComponent(JTable table,
			Object value, boolean isSelected, boolean hasFocus, int row,
			int column)
		{
			Component c = super.getTableCellRendererComponent(table, value,
				isSelected, hasFocus, row, column);
			StatTableModel model = (StatTableModel) table.getModel();
			if (model.isOutlier(row, column))
			{
				if (isSelected)
					c.setBackground(selectedOutlierColor);
				else
					c.setBackground(outlierColor);
			}
			else
			{
				if (!isSelected)
				{
					c.setBackground(backgroundColor);
				}
			}
			
			return c;
		}
	} // class DecimalRenderer

	/**
	 * Class to edit decimal format correctly depending on language settings.
	 * 
	 * @author Sylvia van Borkulo
	 *
	 */
	static class DecimalEditor extends DefaultCellEditor
	{
//		DecimalFormat df;
		JTextField textField;

		public DecimalEditor(JTextField textField)
		{
			super(textField);
//			this.df = Statistiek.getDefaultDecimalFormat();
			this.textField = textField;
		}

		public void setValue(Object value)
		{
			Double doubleValue = Double.parseDouble((String) value);
			DecimalFormat df = Statistiek.getDecimalFormat(doubleValue);
			setValue((value == null) ? "" : df.format(doubleValue));
		}
		
//		@Override
//		public Object getCellEditorValue()
//		{
//			Object value = (String) super.getCellEditorValue();
//			return (value == null) ? "" : value;
//		}
		
		@Override
		public Component getTableCellEditorComponent(
			JTable table, Object value, boolean isSelected,
			int row, int column)
		{
			if (((String)value).equals(ColumnType.WILDCARD))
			{
				// a wildcard remains a wildcard
				textField.setText((String)value);
			}
			else
			{
				Double doubleValue = Double.valueOf(value.toString());
				DecimalFormat df = Statistiek.getDecimalFormat(doubleValue);
				textField.setText(df.format(doubleValue));
			}
			
			return textField;
		}
	} // class DecimalEditor

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
		this.updateHeaderPopUp();
	}

	/**
	 * Set the right cellRenderers
	 */
	private void setCellRenderers()
	{
		ArrayList<ColumnType> types = this.statTableModel.getColumnTypes();
		
		// loop over the columns
		for (int i = 0; i < this.statTableModel.getColumnCount(); i++)
		{
			// set cell renderer for different background for outliers
			
			
			ColumnType type = types.get(i);
			if (type.getType().equals(AllowedTypes.ENUM))
			{
				// Enums get a dropdownbox as editor
				JComboBox box = new JComboBox();
				for (String s : type.getEnumOptions())
				{
					box.addItem(s);
				}
				this.table.getColumnModel().getColumn(i)
					.setCellRenderer(new OutlierRenderer());
				this.table.getColumnModel().getColumn(i)
					.setCellEditor(new DefaultCellEditor(box));
			}
			else if (type.getType().equals(AllowedTypes.DOUBLE))
			{
				this.table.getColumnModel().getColumn(i)
					.setCellRenderer(new DecimalRenderer());
				this.table.getColumnModel().getColumn(i)
					.setCellEditor(new DecimalEditor(new JTextField()));// welk jtextfield?
			}
			else
			{
				this.table.getColumnModel().getColumn(i)
					.setCellRenderer(new OutlierRenderer());
			}
		}
	}

//	/**
//	 * Change this view's model
//	 * 
//	 * @param model
//	 *            the new StatTableModel
//	 */
//	public void setModel(StatTableModel model)
//	{
//		this.statTableModel = model;
//		this.statTableModel.addTableModelListener(this);
//		this.statTableModel.addSelectionListener(this);
//		this.table = new JTable(this.statTableModel)
//		{
//			protected JTableHeader createDefaultTableHeader()
//			{
//				return new JTableHeader(columnModel)
//				{
//					public String getToolTipText(MouseEvent e)
//					{
//						String tip = null;
//						java.awt.Point p = e.getPoint();
//						int index = columnModel.getColumnIndexAtX(p.x);
//						int realIndex = columnModel.getColumn(index)
//							.getModelIndex();
//						return StatTable.this.statTableModel.getColumnTypes()
//							.get(realIndex).getUitleg();
//					}
//				};
//			}
//		};
//		this.table.getSelectionModel().addListSelectionListener(this);
//		this.rowTable.setModel(this.statTableModel);
//
//		this.scrollPane.setViewportView(this.table);
//
//		this.rowTable = new RowNumberTable(this.table);
//		this.scrollPane.setRowHeaderView(this.rowTable);
//		this.scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER,
//			this.rowTable.getTableHeader());
//
//		MouseListener headerPopupListener = new HeaderPopupListener();
//		this.table.getTableHeader().addMouseListener(headerPopupListener);
//		
//		// mouselistener for marking outliers
//		MouseListener outlierPopupListener = new OutlierPopupListener();
//		this.table.addMouseListener(outlierPopupListener);
//
//		this.editDataPanel.setVisible(this.statTableModel.isDataEditable());
//
//		this.setCellRenderers();
//	}

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

	private void copyClipboardData() {
		int rows = statTableModel.getRowCount();
		int cols = statTableModel.getColumnCount();
		StringBuilder sb = new StringBuilder();
// separators: ; en lf
		final char eol = '\n';
		final char eod = ';'; // XXX SPECIFICATIE
		for(int i = 0; i < rows; i++) {
			char sep = eol;
			for(int j = 0; j < cols; j++) {
				sb.append(sep);
				sb.append(statTableModel.getValueAt(i, j));
				sep = eod;
			}		
		}
		{
			sb.append(eol);
			final String data = sb.substring(1);
System.err.println(sb);
            Clipboard clipboard = null;
			try {
				clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

				Transferable transferable = new Transferable() {
	
					@Override
					public DataFlavor[] getTransferDataFlavors() {
						return new DataFlavor[] { DataFlavor.stringFlavor };
					}
	
					@Override
					public boolean isDataFlavorSupported(DataFlavor flavor) {
						return DataFlavor.stringFlavor .equals (flavor);
					}
	
					@Override
					public Object getTransferData(DataFlavor flavor)
							throws UnsupportedFlavorException, IOException {
						return data;
					}};
				ClipboardOwner clipboardowner = new ClipboardOwner() {
	
					@Override
					public void lostOwnership(Clipboard clipboard,
							Transferable contents) {
					}};
				clipboard.setContents(transferable, clipboardowner);
			} catch (Exception se) {
			}
 			
			if(statInteractiePanel != null)
				statInteractiePanel.fire("text.csv", "content", data);
			
		}
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
			Container c = Statistiek.getTopLevelAncestor(this);
			if (c instanceof Frame)
			{
				dialogView = new AddColumnDialogView((Frame) c, dialogModel, Statistiek.rb.getString("addacolumn"));
			}
			else if (c instanceof Dialog)
			{
				dialogView = new AddColumnDialogView((Dialog) c, dialogModel, Statistiek.rb.getString("addacolumn"));
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
					new ColumnType(dialogModel), dialogView.getComputeVariableFormula());
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
		else if (e.getSource() == this.copyButton)
		{
			this.copyClipboardData();
		}
		else if (actionCommand.equals("sortAscendingItem"))
		{
			this.statTableModel.sort(this.popUpColumnIndex, Statistiek.ASCENDING);
		}
		else if (actionCommand.equals("sortDescendingItem"))
		{
			this.statTableModel.sort(this.popUpColumnIndex, Statistiek.DESCENDING);
		}
		else if (actionCommand.equals("deleteItem"))
		{
			this.statTableModel.removeColumn(this.popUpColumnIndex);
		}
		else if (actionCommand.equals("editItem"))
		{
			AddColumnDialogModel dialogModel = new AddColumnDialogModel(
				this.statTableModel,
				this.statTableModel.getColumnName(this.popUpColumnIndex),
				this.statTableModel.getColumnTypes().get(popUpColumnIndex),
				this.popUpColumnIndex);
			AddColumnDialogView dialogView;

			Container c = Statistiek.getTopLevelAncestor(this);
			if (c instanceof Frame)
			{
				dialogView = new AddColumnDialogView((Frame) c, dialogModel, Statistiek.rb.getString("editacolumn"));
			}
			else if (c instanceof Dialog)
			{
				dialogView = new AddColumnDialogView((Dialog) c, dialogModel, Statistiek.rb.getString("editacolumn"));
			}
			else
			{
				System.out.println("Error finding top level frame/dialog.");
				return;
			}
			AddColumnDialogController c2 = new AddColumnDialogController(dialogModel, dialogView);

			dialogView.setVisible(true);

			if (dialogModel.getDonePressed())
			{
				this.statTableModel.editColumn(this.popUpColumnIndex,
					dialogModel.getName(), new ColumnType(dialogModel), dialogView.getComputeVariableFormula());
			}
		}
		else if (actionCommand.equals("infocolumnItem"))
		{
			AddColumnDialogModel m = new AddColumnDialogModel(
				this.statTableModel,
				this.statTableModel.getColumnName(this.popUpColumnIndex),
				this.statTableModel.getColumnTypes().get(popUpColumnIndex),
				this.popUpColumnIndex);
			AddColumnDialogView v;

			Container c = Statistiek.getTopLevelAncestor(this);
			if (c instanceof Frame)
			{
				v = new AddColumnDialogView((Frame) c, m, Statistiek.rb.getString("columninfo"));
			}
			else if (c instanceof Dialog)
			{
				v = new AddColumnDialogView((Dialog) c, m, Statistiek.rb.getString("columninfo"));
			}
			else
			{
				System.out.println("Error finding top level frame/dialog.");
				return;
			}
			AddColumnDialogController c2 = new AddColumnDialogController(m, v);

			v.setVisible(true);

//			if (m.getDonePressed())
//			{
//				this.statTableModel.editColumn(this.popUpColumnIndex,
//					m.getName(), new ColumnType(m));
//			}
		}
		else if (actionCommand.equals("outlierCell"))
		{
			boolean b = this.statTableModel.getCellOutlierList().get(outlierColumnIndex).get(outlierRowIndex);
			this.statTableModel.markCellAsOutlier(this.outlierRowIndex, this.outlierColumnIndex, !b); // toggle the value
			this.table.repaint(); // nodig anders wordt alleen het deel achter de outlierPopup rood getekend
		}
		else if (actionCommand.equals("outlierRow"))
		{
			boolean b = this.statTableModel.getRowOutlierList().get(outlierRowIndex);
			this.statTableModel.markRowAsOutlier(this.outlierRowIndex, !b); // toggle the value
			this.table.repaint(); // nodig anders wordt alleen het deel achter de outlierPopup rood getekend
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
    			this.statTableModel.clearOutlierLists();
    
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
					Statistiek.rb.getString("warning"), JOptionPane.OK_CANCEL_OPTION);
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
			
			// remove listeners related to views
			this.removeViewListeners();
			
			processCSVDataFile(fileChooser.getSelectedFile());
		}
	}

	/**
	 * Remove listeners related to views, except the table view.
	 */
	private void removeViewListeners()
	{
		// remove table model listeners
		ArrayList<TableModelListener> listeners = this.statTableModel.getTableModelListeners();
		
		for (int i = listeners.size() - 1; i >= 0; i--)
		{
			TableModelListener l = listeners.get(i);
			
			// check for listeners other than listeners related to the table view
			if (!(l instanceof StatModel) 
				&& !(l instanceof StatTable)
				&& !(l.getClass().getName().equals("fi.statistiek.StatTable$1"))
				&& !(l instanceof RowNumberTable))
			{
				// remove listener related to a view other than table
				this.statTableModel.removeTableModelListener(l);
			}
		}
		
		// remove selection listeners
		ArrayList<SelectionListener> selectionListeners = this.statTableModel.getSelectionListeners();
		
		for (int i = selectionListeners.size() - 1; i >= 0; i--)
		{
			SelectionListener l = selectionListeners.get(i);
			
			// check for listeners other than listeners related to the table view
			if (!(l instanceof StatTable))
			{
				// remove selection listener related to a view other than table
				this.statTableModel.removeSelectionListener(l);
			}
		}
	}

	/**
	 * Remove views except Table.
	 */
	private void removeViews()
	{
		// test syl
		//System.out.println("StatTable.removeViews()");
		
		ArrayList<StatistiekView> views = new ArrayList<StatistiekView>();
		
		// In edit-mode en standalone is statInteractiePanel null... De tabs blijven daar gewoon staan
		if (this.statInteractiePanel != null)
		{
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
	}

	
	/*
	 * Create columns based on the names. 
	 */
	private void createColumns(String[] names)
	{
		//System.out.println("StatTable.createColumns(): " + names.toString());
		for (int i = 0; i < names.length; i++)
		{
    		this.statTableModel.addColumnWithoutEvent(names[i],
    			new ColumnType(AllowedTypes.STRING), "");
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
			this.statTableModel.removeRowWithoutEvent(i);
		}

		int numberOfColumns = this.statTableModel.getColumnCount(); 
		for (int i = numberOfColumns - 1; i >=0; i--)
		{
			//System.out.println("StatTable.clearStatTableModel(): remove column " + i);
			this.statTableModel.removeColumnWithoutEvent(i);
		}
	}

	/*
	 * Process the CSV data file.
	 */
	private void processCSVDataFile(File file)
	{
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
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
				
				if (hasDuplicates(headers))
				{
					int reply = JOptionPane.showConfirmDialog(null, 
						Statistiek.rb.getString("importCSVformatWarning"),
						Statistiek.rb.getString("error"), JOptionPane.OK_OPTION);
					if (reply == JOptionPane.OK_OPTION)
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
						this.setCursor(Cursor.getDefaultCursor());
						return;
					}
				}
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
		this.addDataRowsWithoutEvent(dataRows);
		
		this.statTableModel.updateNumericalColumnTypes();
		
		// update the view
		if (this.statInteractiePanel != null)
			this.statInteractiePanel.getView().update(null, null);
		
		this.setCursor(Cursor.getDefaultCursor());
	}

	/**
	 * Checkt in de string array of er duplicaten voorkomen.
	 * 
	 * @param items
	 * @return
	 */
	private boolean hasDuplicates(String[] items)
	{
		boolean duplicates = false;
		
		for (int i = 0; i < items.length; i++)
		{
			for (int j = i + 1; j < items.length; j++)
			{
			    if ((j != i) && items[j].equals(items[i]))
			    {
			    	duplicates=true;
			    	break;
			    }
			}
		}
		return duplicates;
	}

	/**
	 * Add the row data to the table.
	 * @param dataRows
	 */
	private void addDataRowsWithoutEvent(ArrayList<String> dataRows)
	{
        Iterator<String> rowIterator = dataRows.iterator();
        int rowIndex = 0;
        while (rowIterator.hasNext()) 
        {
        	String dataRow = rowIterator.next();
        	
         	String[] values = dataRow.split(";", -1);
         	this.replaceMissingValues(values);
        	ArrayList<Object> valuesList= new ArrayList<Object>(Arrays.asList(values));
        	
        	this.statTableModel.addRowWithoutEvent(valuesList);
        }
	}

	/**
	 * Replace empty values with missing value wildcard.
	 * @param values
	 */
	private void replaceMissingValues(String[] values)
	{
		for (int i = 0; i < values.length; i++)
		{
			if (values[i].equals(""))
			{
				values[i] = ColumnType.WILDCARD;
			}
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
//			System.out.println();
			this.statTableModel.setSelectionList(newSelection);
		}
	}

	public void selectionChanged()
	{
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
	
	/**
	 * Methode die hoort bij SelectionListener.
	 * Outliers worden altijd door StatTable zelf gewijzigd
	 * en dus is geen verdere actie vereist.
	 */
	public void outliersChanged()
	{
		//System.out.println("StatTable.outliersChanged()");
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
