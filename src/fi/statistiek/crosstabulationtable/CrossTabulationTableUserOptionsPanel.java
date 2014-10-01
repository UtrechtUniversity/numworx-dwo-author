package fi.statistiek.crosstabulationtable;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fi.statistiek.DialogButton;
import fi.statistiek.Statistiek;
import fi.statistiek.types.AllowedTypes;
import fi.statistiek.types.ColumnType;

/**
 * User options panel for StatistiekView CrossTabulationTable
 * 
 * @author Sylvia van Borkulo
 * 
 */
public class CrossTabulationTableUserOptionsPanel extends JPanel implements
	ActionListener
{

	private CrossTabulationTableView view;
	private CrossTabulationTableController controller;
	private CrossTabulationTableModel model;

	private DialogButton dialogButton;

	private JPanel panel;
	private Box vb0;
	private Color backgroundColor = new Color(230, 230, 230);

	// ROWS variable settings
	/**
	 * The box for choosing the variable for the rows in the crosstab
	 */
	private JLabel rowsLabel;
	private JComboBox<String> rowIndexBox;
	
	// rows bin settings
	private JLabel minBoundaryRowsLabel;
	private JTextField minBoundaryRowsField;
	private JLabel binWidthRowsLabel;
	private JTextField binWidthRowsField;
	private JLabel noObjectsRowsLabel;
	private JLabel minValueRowsLabel;
	private JLabel maxValueRowsLabel;
	
	// COLUMNS variable settings
	/**
	 * The box for choosing the variable for the columns in the crosstab
	 */
	private JLabel columnsLabel;
	private JComboBox<String> columnIndexBox;

	// columns bin settings
	private JLabel minBoundaryColumnsLabel;
	private JTextField minBoundaryColumnsField;
	private JLabel binWidthColumnsLabel;
	private JTextField binWidthColumnsField;
	private JLabel noObjectsColumnsLabel;
	private JLabel minValueColumnsLabel;
	private JLabel maxValueColumnsLabel;

	// display settings
	/**
	 * Indicates that amounts are shown in the crosstab table
	 */
	private JRadioButton amountRadioItem;
	/**
	 * Indicates that percentages are shown in the crosstab table
	 */
	private JRadioButton percentageRadioItem;
	/**
	 * Separates the percentage radio button and the 
	 * percentage settings.
	 */
	private JSeparator separatorPercentageSettings;
	/**
	 * Indicates that the end total adds up to 100%
	 */
	private JRadioButton percentage_endTotal;
	/**
	 * Indicates that the row total adds up to 100%
	 */
	private JRadioButton percentage_rowTotal;
	/**
	 * Indicates that the column total adds up to 100%
	 */
	private JRadioButton percentage_columnTotal;

	private boolean enumClassesRows;
	private boolean enumClassesColumns;

	private static final String SWAP_ICON_PATH = "../resources/reseticon.gif";

	/**
	 * Button to swap row and column variable
	 */
	private JButton swapButton;
	private JButton okButton;

	private ArrayList<Double> binBoundariesRows;
	private ArrayList<Double> binBoundariesColumns;

	public CrossTabulationTableUserOptionsPanel(CrossTabulationTableView view,
		CrossTabulationTableController controller, CrossTabulationTableModel model)
	{
//		System.out.println("CrossTabulationTableUserOptionsPanel() met createGUI en layoutGUI");
		
		this.view = view;
		this.controller = controller;
		this.model = model;

		createGuiComponents();
		layoutGuiComponents();

		dialogButton = new DialogButton(
			Statistiek.rb.getString("settingsButton"), this.panel);
		dialogButton.addActionListener(this);

		panel.addComponentListener(dialogButton);

	}

	private void createGuiComponents()
	{
		this.panel = new JPanel(new FlowLayout());
		this.panel.setBackground(backgroundColor);

		// ROWS variable settings
		this.rowsLabel = new JLabel(
			Statistiek.rb.getString("rowsLabel"));
		this.rowsLabel.setFont(Statistiek.font);
		this.rowIndexBox = new JComboBox<String>();
		this.rowIndexBox.setFont(Statistiek.font);
		this.rowIndexBox.setPreferredSize(new Dimension(100, 25));
		this.rowIndexBox.setMaximumSize(new Dimension(100, 25));
		this.rowIndexBox.setActionCommand("rowIndexBox");
		this.rowIndexBox.addActionListener(this.controller);

		// rows bin settings
		this.minBoundaryRowsLabel = new JLabel(
			Statistiek.rb.getString("startvalueLabel"));
		this.minBoundaryRowsLabel.setFont(Statistiek.font);

		this.minBoundaryRowsField = new JTextField();
		this.minBoundaryRowsField.setMaximumSize(new Dimension(40, 25));
		this.minBoundaryRowsField.setMinimumSize(new Dimension(40, 25));
		this.minBoundaryRowsField.setPreferredSize(new Dimension(40, 25));
		this.minBoundaryRowsField.setActionCommand("minBoundaryRows");
		this.minBoundaryRowsField.addActionListener(controller);
		this.minBoundaryRowsField.addFocusListener(controller);

		this.binWidthRowsLabel = new JLabel(
			Statistiek.rb.getString("classwidthLabel"));
		this.binWidthRowsLabel.setFont(Statistiek.font);

		this.binWidthRowsField = new JTextField();
		this.binWidthRowsField.setMaximumSize(new Dimension(40, 25));
		this.binWidthRowsField.setMinimumSize(new Dimension(40, 25));
		this.binWidthRowsField.setPreferredSize(new Dimension(40, 25));
		this.binWidthRowsField.setActionCommand("binWidthRows");
		this.binWidthRowsField.addActionListener(controller);
		this.binWidthRowsField.addFocusListener(controller);

		this.noObjectsRowsLabel = new JLabel("noObjectsLabel");
		this.noObjectsRowsLabel.setFont(Statistiek.font);

		this.minValueRowsLabel = new JLabel("minValueLabel");
		this.minValueRowsLabel.setFont(Statistiek.font);

		this.maxValueRowsLabel = new JLabel("maxValueLabel");
		this.maxValueRowsLabel.setFont(Statistiek.font);

		// COLUMNS variable settings
		this.columnsLabel = new JLabel(
			Statistiek.rb.getString("columnsLabel"));
		this.columnsLabel.setFont(Statistiek.font);
		this.columnIndexBox = new JComboBox<String>();
		this.columnIndexBox.setFont(Statistiek.font);
		this.columnIndexBox.setPreferredSize(new Dimension(100, 25));
		this.columnIndexBox.setMaximumSize(new Dimension(100, 25));
		this.columnIndexBox.setActionCommand("columnIndexBox");
		this.columnIndexBox.addActionListener(this.controller);

		// columns bin settings
		this.minBoundaryColumnsLabel = new JLabel(
			Statistiek.rb.getString("startvalueLabel"));
		this.minBoundaryColumnsLabel.setFont(Statistiek.font);

		this.minBoundaryColumnsField = new JTextField();
		this.minBoundaryColumnsField.setMaximumSize(new Dimension(40, 25));
		this.minBoundaryColumnsField.setMinimumSize(new Dimension(40, 25));
		this.minBoundaryColumnsField.setPreferredSize(new Dimension(40, 25));
		this.minBoundaryColumnsField.setActionCommand("minBoundaryColumns");
		this.minBoundaryColumnsField.addActionListener(controller);
		this.minBoundaryColumnsField.addFocusListener(controller);

		this.binWidthColumnsLabel = new JLabel(
			Statistiek.rb.getString("classwidthLabel"));
		this.binWidthColumnsLabel.setFont(Statistiek.font);

		this.binWidthColumnsField = new JTextField();
		this.binWidthColumnsField.setMaximumSize(new Dimension(40, 25));
		this.binWidthColumnsField.setMinimumSize(new Dimension(40, 25));
		this.binWidthColumnsField.setPreferredSize(new Dimension(40, 25));
		this.binWidthColumnsField.setActionCommand("binWidthColumns");
		this.binWidthColumnsField.addActionListener(controller);
		this.binWidthColumnsField.addFocusListener(controller);

		this.noObjectsColumnsLabel = new JLabel("noObjectsLabel");
		this.noObjectsColumnsLabel.setFont(Statistiek.font);

		this.minValueColumnsLabel = new JLabel("minValueLabel");
		this.minValueColumnsLabel.setFont(Statistiek.font);

		this.maxValueColumnsLabel = new JLabel("maxValueLabel");
		this.maxValueColumnsLabel.setFont(Statistiek.font);

		// display settings
		this.amountRadioItem = new JRadioButton(
			Statistiek.rb.getString("amountLabel"));
		this.amountRadioItem.setFont(Statistiek.font);
		this.amountRadioItem.setOpaque(false);
		this.amountRadioItem.setActionCommand("amountRadioItem");
		this.amountRadioItem.addActionListener(this);

		this.percentageRadioItem = new JRadioButton(
			Statistiek.rb.getString("percentageRadio"));
		this.percentageRadioItem.setFont(Statistiek.font);
		this.percentageRadioItem.setActionCommand("percentageRadioItem");
		this.percentageRadioItem.addActionListener(this);
		this.percentageRadioItem.setOpaque(false);

		ButtonGroup buttonGroup1 = new ButtonGroup();
		buttonGroup1.add(this.percentageRadioItem);
		buttonGroup1.add(this.amountRadioItem);

		this.separatorPercentageSettings = new JSeparator();
		this.separatorPercentageSettings.setBorder(BorderFactory
			.createEtchedBorder(EtchedBorder.LOWERED));
		this.separatorPercentageSettings.setMaximumSize(new Dimension(140, 3));

		this.percentage_endTotal = new JRadioButton(
			Statistiek.rb.getString("percentage_endTotal"));
		this.percentage_endTotal.setFont(Statistiek.font);
		this.percentage_endTotal.setActionCommand("percentage_endTotal");
		this.percentage_endTotal.addActionListener(this);
		this.percentage_endTotal.setOpaque(false);
		this.percentage_endTotal.setSelected(true);// by default true

		this.percentage_rowTotal = new JRadioButton(
			Statistiek.rb.getString("percentage_rowTotal"));
		this.percentage_rowTotal.setFont(Statistiek.font);
		this.percentage_rowTotal.setActionCommand("percentage_rowTotal");
		this.percentage_rowTotal.addActionListener(this);
		this.percentage_rowTotal.setOpaque(false);

		this.percentage_columnTotal = new JRadioButton(
			Statistiek.rb.getString("percentage_columnTotal"));
		this.percentage_columnTotal.setFont(Statistiek.font);
		this.percentage_columnTotal.setActionCommand("percentage_columnTotal");
		this.percentage_columnTotal.addActionListener(this);
		this.percentage_columnTotal.setOpaque(false);

		ButtonGroup buttonGroupPercentageSettings = new ButtonGroup();
		buttonGroupPercentageSettings.add(this.percentage_endTotal);
		buttonGroupPercentageSettings.add(this.percentage_rowTotal);
		buttonGroupPercentageSettings.add(this.percentage_columnTotal);

		// button to swap row and column variable
		this.swapButton = new JButton();
		java.net.URL imageURL = Statistiek.class.getResource("resources/reseticon.gif");
		if (imageURL != null) 
		{
		   swapButton.setIcon(new ImageIcon(imageURL));
		}

		this.swapButton.setToolTipText(Statistiek.rb.getString("swapTooltip"));
		this.swapButton.addActionListener(this);

		// ok-cancel
		this.okButton = new JButton("OK");
		this.okButton.addActionListener(this);
	}

	private void layoutGuiComponents()
	{
		Box hb1, hb2, hb3, hb4, hb5, hb6, hb7, 
			hb8, 
			hb9, hb10, hb11, hb12, hb13, hb14, hb15,
			hb16, hb17, hb18, hb19, hb20, hb21;
		Box vb1, vb2, vb3, vb4, vb5, vb6;

		// ROWS variable settings
		
		hb1 = Box.createHorizontalBox();
		hb1.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		hb1.add(rowsLabel);

		hb2 = Box.createHorizontalBox();
		hb2.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		hb2.add(rowIndexBox);

		// Rows bins settings
		
		hb3 = Box.createHorizontalBox();
		hb3.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		hb3.add(this.minBoundaryRowsLabel);
		hb3.add(Box.createHorizontalStrut(5));
		hb3.add(Box.createHorizontalGlue());
		hb3.add(this.minBoundaryRowsField);

		hb4 = Box.createHorizontalBox();
		hb4.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		hb4.add(this.binWidthRowsLabel);
		hb4.add(Box.createHorizontalGlue());
		hb4.add(Box.createHorizontalStrut(5));
		hb4.add(this.binWidthRowsField);

		hb5 = Box.createHorizontalBox();
		hb5.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		hb5.add(this.noObjectsRowsLabel);
		hb5.add(Box.createHorizontalGlue());

		hb6 = Box.createHorizontalBox();
		hb6.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		hb6.add(this.minValueRowsLabel);
		hb6.add(Box.createHorizontalGlue());

		hb7 = Box.createHorizontalBox();
		hb7.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		hb7.add(this.maxValueRowsLabel);
		hb7.add(Box.createHorizontalGlue());

		vb2 = Box.createVerticalBox();
		Border border = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		vb2.setBorder(BorderFactory.createTitledBorder(border,
			Statistiek.rb.getString("classDivisionLabel"), TitledBorder.CENTER,
			TitledBorder.TOP, Statistiek.font));
		vb2.add(hb3);
		vb2.add(hb4);
		vb2.add(hb5);
		vb2.add(hb6);
		vb2.add(hb7);
		vb2.add(Box.createVerticalGlue());
		
		vb1 = Box.createVerticalBox();
		vb1.setBorder(BorderFactory.createTitledBorder(border,
			Statistiek.rb.getString("variableLabel"), TitledBorder.CENTER,
			TitledBorder.TOP, Statistiek.font));
		vb1.add(hb1);
		vb1.add(hb2);
		vb1.add(vb2);
		vb1.add(Box.createVerticalGlue());

		// Swap button
		hb8 = Box.createHorizontalBox();
		hb8.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		hb8.add(swapButton);

		vb6 = Box.createVerticalBox();
		vb6.setBorder(BorderFactory.createTitledBorder(border,
			Statistiek.rb.getString("swapLabel"), TitledBorder.CENTER,
			TitledBorder.TOP, Statistiek.font));
		vb6.add(hb8);
		vb6.add(Box.createVerticalGlue());

		// COLUMNS variable settings
		
		hb9 = Box.createHorizontalBox();
		hb9.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		hb9.add(columnsLabel);

		hb10 = Box.createHorizontalBox();
		hb10.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		hb10.add(columnIndexBox);

		// Columns bins settings
		
		hb11 = Box.createHorizontalBox();
		hb11.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		hb11.add(this.minBoundaryColumnsLabel);
		hb11.add(Box.createHorizontalStrut(5));
		hb11.add(Box.createHorizontalGlue());
		hb11.add(this.minBoundaryColumnsField);

		hb12 = Box.createHorizontalBox();
		hb12.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		hb12.add(this.binWidthColumnsLabel);
		hb12.add(Box.createHorizontalGlue());
		hb12.add(Box.createHorizontalStrut(5));
		hb12.add(this.binWidthColumnsField);

		hb13 = Box.createHorizontalBox();
		hb13.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		hb13.add(this.noObjectsColumnsLabel);
		hb13.add(Box.createHorizontalGlue());

		hb14 = Box.createHorizontalBox();
		hb14.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		hb14.add(this.minValueColumnsLabel);
		hb14.add(Box.createHorizontalGlue());

		hb15 = Box.createHorizontalBox();
		hb15.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		hb15.add(this.maxValueColumnsLabel);
		hb15.add(Box.createHorizontalGlue());

		vb4 = Box.createVerticalBox();
		vb4.setBorder(BorderFactory.createTitledBorder(border,
			Statistiek.rb.getString("classDivisionLabel"), TitledBorder.CENTER,
			TitledBorder.TOP, Statistiek.font));
		vb4.add(hb11);
		vb4.add(hb12);
		vb4.add(hb13);
		vb4.add(hb14);
		vb4.add(hb15);
		vb4.add(Box.createVerticalGlue());
		
		vb3 = Box.createVerticalBox();
		border = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		vb3.setBorder(BorderFactory.createTitledBorder(border,
			Statistiek.rb.getString("variableLabel"), TitledBorder.CENTER,
			TitledBorder.TOP, Statistiek.font));
		vb3.add(hb9);
		vb3.add(hb10);
		vb3.add(vb4);
		vb3.add(Box.createVerticalGlue());

		// Display
		hb16 = Box.createHorizontalBox();
		hb16.add(amountRadioItem);
		hb16.add(Box.createHorizontalGlue());

		hb17 = Box.createHorizontalBox();
		hb17.add(percentageRadioItem);
		hb17.add(Box.createHorizontalGlue());

		hb18 = Box.createHorizontalBox();
		hb18.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		hb18.add(separatorPercentageSettings);

		hb19 = Box.createHorizontalBox();
		hb19.add(percentage_endTotal);
		hb19.add(Box.createHorizontalGlue());

		hb20 = Box.createHorizontalBox();
		hb20.add(percentage_rowTotal);
		hb20.add(Box.createHorizontalGlue());

		hb21 = Box.createHorizontalBox();
		hb21.add(percentage_columnTotal);
		hb21.add(Box.createHorizontalGlue());

		vb5 = Box.createVerticalBox();
		vb5.setBorder(BorderFactory.createTitledBorder(border,
			Statistiek.rb.getString("absRelLabel"), TitledBorder.CENTER,
			TitledBorder.TOP, Statistiek.font));
		vb5.add(hb16);
		vb5.add(hb17);
		vb5.add(hb18);
		vb5.add(hb19);
		vb5.add(hb20);
		vb5.add(hb21);
		vb5.add(Box.createVerticalGlue());
		
		Box hb0 = Box.createHorizontalBox();
		hb0.add(Box.createHorizontalStrut(10));
		hb0.add(vb1);
		hb0.add(Box.createHorizontalStrut(10));
		hb0.add(vb6);
		hb0.add(Box.createHorizontalStrut(10));
		hb0.add(vb3);
		hb0.add(Box.createHorizontalStrut(10));
		hb0.add(vb5);
		hb0.add(Box.createHorizontalStrut(10));

		vb0 = Box.createVerticalBox();
		vb0.add(hb0);
		vb0.add(Box.createVerticalStrut(10));
		vb0.add(okButton);
		vb0.add(Box.createVerticalStrut(20));

		panel.add(vb0);
	}

	public DialogButton getDialogButton()
	{
		return dialogButton;
	}

	public int getVarRowsBoxSelectedIndex()
	{
		return this.rowIndexBox.getSelectedIndex();
	}

	public int getVarColumnsBoxSelectedIndex()
	{
		return this.columnIndexBox.getSelectedIndex();
	}

	public void setModel(CrossTabulationTableModel model)
	{
		this.model = model;
	}
	
	public void updateRowIndexBox()
	{
		this.rowIndexBox.removeActionListener(this.controller);
		this.rowIndexBox.removeAllItems();

		for (String varName : this.model.getTableModel().getColumnNames())
		{
			this.rowIndexBox.addItem(varName);
		}

		if (this.model.columnIndexValid())
		{
			this.rowIndexBox.setSelectedIndex(this.model.getColumnIndex());
		}
		else
		{
			// set no item selected
			//System.out.println("CrossTabulationTableUserOptionsPanel.update(): no column index selected!");
			this.rowIndexBox.setSelectedIndex(-1);
		}
		this.rowIndexBox.addActionListener(this.controller);
	}
	
	public void updateColumnIndexBox()
	{
		this.columnIndexBox.removeActionListener(this.controller);
		this.columnIndexBox.removeAllItems();

		for (String varName : this.model.getTableModel().getColumnNames())
		{
			this.columnIndexBox.addItem(varName);
		}
		
		if (this.model.getTableModel().isColumnIndexValid(this.model.getColumnSplitIndex()))
		{
			this.columnIndexBox.setSelectedIndex(this.model.getColumnSplitIndex());
		}
		else
		{
			// set no item selected
			//System.out.println("CrossTabulationTableUserOptionsPanel.update(): no column index selected!");
			this.columnIndexBox.setSelectedIndex(-1);
		}
		this.columnIndexBox.addActionListener(this.controller);
	}
	
	public void updateRowBinSettings()
	{
		if (this.model.columnIndexValid())
		{
			ColumnType cType = this.model.getTableModel().getColumnTypes()
				.get(this.model.getColumnIndex());
			AllowedTypes type = cType.getType();
			if (type.equals(AllowedTypes.DOUBLE)
				|| type.equals(AllowedTypes.INTEGER))
			{
				this.minBoundaryRowsField.setText(Statistiek.df.format(this.model
					.getBinBoundaries().get(0)));
				Double d = this.model.getBinBoundaries().get(1)
					- this.model.getBinBoundaries().get(0);
				this.binWidthRowsField.setText(Statistiek.df.format(d));
				this.noObjectsRowsLabel.setText(Statistiek.rb
					.getString("numberLabel")
					+ this.model.getTableModel().getRowCount());
				this.minValueRowsLabel.setText(Statistiek.rb.getString("minLabel")
					+ this.model.getTableModel().getColumnMin(
						this.model.getColumnIndex()));
				this.maxValueRowsLabel.setText(Statistiek.rb.getString("maxLabel")
					+ this.model.getTableModel().getColumnMax(
						this.model.getColumnIndex()));
				setEnumClassesRows(false);
			}
			else if (type.equals(AllowedTypes.ENUM))
			{
				setEnumClassesRows(true);
			}
			else if (type.equals(AllowedTypes.STRING))
			{
				setEnumClassesRows(true);
			}
		}
	}
	
	public void updateColumnBinSettings()
	{
		if (this.model.getSplitOptions().getColumnSplitIndex() > -1)
		{
			AllowedTypes type = this.model.getTableModel().getColumnTypes().get(this.model.getSplitOptions().getColumnSplitIndex()).getType();
			
			if (type.equals(AllowedTypes.DOUBLE)
				|| type.equals(AllowedTypes.INTEGER))
			{
				this.minBoundaryColumnsField.setText(Statistiek.df.format(this.model
					.getSplitOptions().getBinBoundaries().get(0)));
				Double d = this.model.getSplitOptions().getBinBoundaries().get(1)
					- this.model.getSplitOptions().getBinBoundaries().get(0);
				this.binWidthColumnsField.setText(Statistiek.df.format(d));
				this.noObjectsColumnsLabel.setText(Statistiek.rb
					.getString("numberLabel")
					+ this.model.getTableModel().getRowCount());
				this.minValueColumnsLabel.setText(Statistiek.rb.getString("minLabel")
					+ this.model.getTableModel().getColumnMin(
						this.model.getSplitOptions().getColumnSplitIndex()));
				this.maxValueColumnsLabel.setText(Statistiek.rb.getString("maxLabel")
					+ this.model.getTableModel().getColumnMax(
						this.model.getSplitOptions().getColumnSplitIndex()));
				setEnumClassesColumns(false);
			}
			else if (type.equals(AllowedTypes.ENUM))
			{
				setEnumClassesColumns(true);
			}
			else if (type.equals(AllowedTypes.STRING))
			{
				setEnumClassesColumns(true);
			}
		}
	}

	public void update()
	{
//		System.out.println("CrossTabulationTableUserOptionsPanel.update()");
		
		updateRowIndexBox();
		
		updateRowBinSettings();
		
		updateColumnIndexBox();

		updateColumnBinSettings();
		
		if (this.model.isShowPercentage())
		{
			this.percentageRadioItem.setSelected(true);
			
			this.setPercentageOptionsVisible(true);
			
			this.percentage_endTotal.setSelected(this.model.isShowPercentage_endTotal());
			this.percentage_rowTotal.setSelected(this.model.isShowPercentage_rowTotal());
			this.percentage_columnTotal.setSelected(this.model.isShowPercentage_columnTotal());
		}
		else
		{
			this.amountRadioItem.setSelected(true);
			this.setPercentageOptionsVisible(false);
		}
		
		if (SwingUtilities.getWindowAncestor(this.panel) != null)
			SwingUtilities.getWindowAncestor(this.panel).pack();
		repaint();
	}

	public double getBinWidthRows()
	{
		String s = this.binWidthRowsField.getText();
		s = s.replace(',', '.');
		return Double.parseDouble(s);
	}

	public void setBinWidthRows(double d)
	{
		this.binWidthRowsField.setText(String.valueOf(d));
	}
	
	public double getMinBoundaryRows()
	{
		String s = this.minBoundaryRowsField.getText();
		s = s.replace(',', '.');
		return Double.parseDouble(s);
	}
	
	public void setMinBoundaryRows(double d)
	{
		this.minBoundaryRowsField.setText(String.valueOf(d));
	}

	public JTextField getBinWidthRowsField()
	{
		return binWidthRowsField;
	}

	public JTextField getMinBoundaryRowsField()
	{
		return minBoundaryRowsField;
	}

	public JTextField getBinWidthColumnsField()
	{
		return binWidthColumnsField;
	}

	public JTextField getMinBoundaryColumnsField()
	{
		return minBoundaryColumnsField;
	}

	public double getBinWidthColumns()
	{
		String s = this.binWidthColumnsField.getText();
		s = s.replace(',', '.');
		return Double.parseDouble(s);
	}

	public void setBinWidthColumns(double d)
	{
		this.binWidthColumnsField.setText(String.valueOf(d));
	}
	
	public double getMinBoundaryColumns()
	{
		String s = this.minBoundaryColumnsField.getText();
		s = s.replace(',', '.');
		return Double.parseDouble(s);
	}

	public void setMinBoundaryColumns(double d)
	{
		this.minBoundaryColumnsField.setText(String.valueOf(d));
	}

	private void setEnumClassesRows(boolean b)
	{
		//System.out.println("CrossTabulationTableUserOptionsPanel.setEnumClassesRows(" + b + ")");
		
		enumClassesRows = b;
		
		minBoundaryRowsLabel.getParent().setVisible(!b);
		minBoundaryRowsField.getParent().setVisible(!b);
		binWidthRowsLabel.getParent().setVisible(!b);
		binWidthRowsField.getParent().setVisible(!b);
		noObjectsRowsLabel.getParent().setVisible(!b);
		minValueRowsLabel.getParent().setVisible(!b);
		maxValueRowsLabel.getParent().setVisible(!b);

		// box waarin componenten zitten ook visible zetten
		maxValueRowsLabel.getParent().getParent().setVisible(!b);
	}

	private void setEnumClassesColumns(boolean b)
	{
		//System.out.println("CrossTabulationTableUserOptionsPanel.setEnumClassesRows(" + b + ")");
		
		enumClassesColumns = b;
		
		minBoundaryColumnsLabel.getParent().setVisible(!b);
		minBoundaryColumnsField.getParent().setVisible(!b);
		binWidthColumnsLabel.getParent().setVisible(!b);
		binWidthColumnsField.getParent().setVisible(!b);
		noObjectsColumnsLabel.getParent().setVisible(!b);
		minValueColumnsLabel.getParent().setVisible(!b);
		maxValueColumnsLabel.getParent().setVisible(!b);

		// box waarin componenten zitten ook visible zetten
		maxValueColumnsLabel.getParent().getParent().setVisible(!b);
	}

	public void actionPerformed(ActionEvent e)
	{

		if (e.getSource() == okButton)
		{
			dialogButton.closeDialog();
		}
		else if ((e.getSource() == percentageRadioItem)
			|| (e.getSource() == amountRadioItem))
		{
			setPercentageOptionsVisible(this.view.percentageItemSelected());
			if (!this.view.percentage_endTotalSelected()
				&& !this.view.percentage_rowTotalSelected()
				&& !this.view.percentage_columnTotalSelected())
			{
				// set the default end totals add up to 100%
				this.model.setShowPercentage_endTotal(true);
				this.percentage_endTotal.setSelected(true);
			}			
			this.model.setShowPercentage(this.view.percentageItemSelected());
		}
		else if (e.getSource() == percentage_endTotal)
		{
			boolean b = this.percentage_endTotal.isSelected();
			
			// Only update if there is a real change
			if (b != this.model.isShowPercentage_endTotal())
			{
				if (b)
				{
					// zet de andere 2 opties op false
					this.model.setShowPercentage_rowTotal(false);
					this.model.setShowPercentage_columnTotal(false);
				}

				this.model.setShowPercentage_endTotal(b);
			}
		}
		else if (e.getSource() == percentage_rowTotal)
		{
			boolean b = this.percentage_rowTotal.isSelected();
			
			// Only update if there is a real change
			if (b != this.model.isShowPercentage_rowTotal())
			{
				if (b)
				{
					// zet de andere 2 opties op false
					this.model.setShowPercentage_endTotal(false);
					this.model.setShowPercentage_columnTotal(false);
				}

				this.model.setShowPercentage_rowTotal(b);
			}
		}
		else if (e.getSource() == percentage_columnTotal)
		{
			boolean b = this.percentage_columnTotal.isSelected();
			
			// Only update if there is a real change
			if (b != this.model.isShowPercentage_columnTotal())
			{
				if (b)
				{
					// zet de andere 2 opties op false
					this.model.setShowPercentage_endTotal(false);
					this.model.setShowPercentage_rowTotal(false);
				}

				this.model.setShowPercentage_columnTotal(b);
			}
		}
		else if (e.getSource() == swapButton)
		{
			this.model.swapVariables();
		}
		else if (e.getSource() == dialogButton)
		{
			Thread startDraad = new Thread()
			{
				public void run()
				{
					try
					{
						sleep(2);
					}
					catch (InterruptedException e)
					{
					}
					//init();
					repaint();
				}
			};
			startDraad.start();
		}
	}

	private void setPercentageOptionsVisible(boolean b)
	{
		this.separatorPercentageSettings.getParent().setVisible(b);
		this.percentage_endTotal.getParent().setVisible(b);
		this.percentage_rowTotal.getParent().setVisible(b);
		this.percentage_columnTotal.getParent().setVisible(b);
	}

	public boolean percentageItemSelected()
	{
		return this.percentageRadioItem.isSelected();
	}

	public boolean percentage_endTotalSelected()
	{
		return this.percentage_endTotal.isSelected();
	}
	
	public boolean percentage_rowTotalSelected()
	{
		return this.percentage_rowTotal.isSelected();
	}
	
	public boolean percentage_columnTotalSelected()
	{
		return this.percentage_columnTotal.isSelected();
	}
	
}
