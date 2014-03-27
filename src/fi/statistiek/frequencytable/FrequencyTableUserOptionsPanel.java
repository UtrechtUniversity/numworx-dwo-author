package fi.statistiek.frequencytable;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fi.statistiek.ColorPreviewer;
import fi.statistiek.DialogButton;
import fi.statistiek.Statistiek;
import fi.statistiek.types.AllowedTypes;
import fi.statistiek.types.ColumnType;

public class FrequencyTableUserOptionsPanel extends JPanel implements
	ActionListener
{

	private FrequencyTableView view;
	private FrequencyTableController controller;
	private FrequencyTableModel model;

	private DialogButton dialogButton;

	private JPanel panel;
	private Box vb0;
	private Color backgroundColor = new Color(230, 230, 230);

	// variable settings
	private JLabel columnIndexLabel;
	private JComboBox columnIndexBox;
	private JLabel noBinsLabel;
	private JTextField noBinsField;
	private JButton chooseBinsButton;
	
	// bin settings
	private JLabel binsLabel;
	private JComboBox binsBox;
	private JButton chooseBoundariesButton;
	private JLabel minBoundaryLabel;
	private JTextField minBoundaryField;
	private JLabel binWidthLabel;
	private JTextField binWidthField;
	private JLabel boundariesLabel;
	private JTextArea boundariesArea;
	private JScrollPane boundariesAreaScrollPane;
	private JLabel noObjectsLabel;
	private JLabel minValueLabel;
	private JLabel maxValueLabel;

	// display settings
	private JCheckBox showPercBox;
	private JCheckBox showFreqBox;
	private JCheckBox showCumulativeBox;

	private JButton okButton;

	private boolean boundariesVisible;
	private boolean enumClasses;

	private ArrayList<Double> binBoundaries;

	public FrequencyTableUserOptionsPanel(FrequencyTableView view,
		FrequencyTableController controller, FrequencyTableModel model)
	{
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

		// var settings
		this.columnIndexLabel = new JLabel(Statistiek.rb.getString("variableXLabel"));
		this.columnIndexLabel.setFont(Statistiek.font);

		this.columnIndexBox = new JComboBox();
		this.columnIndexBox.setFont(Statistiek.font);
		this.columnIndexBox.setPreferredSize(new Dimension(100, 25));
		this.columnIndexBox.setMaximumSize(new Dimension(100, 25));
		this.columnIndexBox.setActionCommand("columnIndexBox");
		this.columnIndexBox.addActionListener(this.controller);

		// bin settings
		this.minBoundaryLabel = new JLabel(
			Statistiek.rb.getString("startvalueLabel"));
		this.minBoundaryLabel.setFont(Statistiek.font);

		this.minBoundaryField = new JTextField();
		this.minBoundaryField.setMaximumSize(new Dimension(40, 25));
		this.minBoundaryField.setMinimumSize(new Dimension(40, 25));
		this.minBoundaryField.setPreferredSize(new Dimension(40, 25));
		this.minBoundaryField.setActionCommand("minBoundary");
		this.minBoundaryField.addActionListener(controller);
		this.minBoundaryField.addFocusListener(controller);

		this.binWidthLabel = new JLabel(
			Statistiek.rb.getString("classwidthLabel"));
		this.binWidthLabel.setFont(Statistiek.font);

		this.binWidthField = new JTextField();
		this.binWidthField.setMaximumSize(new Dimension(40, 25));
		this.binWidthField.setMinimumSize(new Dimension(40, 25));
		this.binWidthField.setPreferredSize(new Dimension(40, 25));
		this.binWidthField.setActionCommand("binWidth");
		this.binWidthField.addActionListener(controller);
		this.binWidthField.addFocusListener(controller);

		this.boundariesLabel = new JLabel(Statistiek.rb.getString("binsButton"));
		this.boundariesLabel.setFont(Statistiek.font);

		this.boundariesArea = new JTextArea();
		this.boundariesArea.setEditable(false);
		this.boundariesAreaScrollPane = new JScrollPane(this.boundariesArea);
		this.boundariesAreaScrollPane.setBorder(BorderFactory
			.createLoweredBevelBorder());
		this.boundariesAreaScrollPane.setMaximumSize(new Dimension(120, 140));
		this.boundariesAreaScrollPane.setMinimumSize(new Dimension(120, 140));
		this.boundariesAreaScrollPane.setPreferredSize(new Dimension(130, 140));

		this.noObjectsLabel = new JLabel("noObjectsLabel");
		this.noObjectsLabel.setFont(Statistiek.font);

		this.minValueLabel = new JLabel("minValueLabel");
		this.minValueLabel.setFont(Statistiek.font);

		this.maxValueLabel = new JLabel("maxValueLabel");
		this.maxValueLabel.setFont(Statistiek.font);

		// display settings
		this.showPercBox = new JCheckBox(
			Statistiek.rb.getString("showpercentageCheckbox"), true);
		this.showPercBox.setFont(Statistiek.font);
		this.showPercBox.setBackground(backgroundColor);
		this.showPercBox.setActionCommand("showPercBox");
		this.showPercBox.addActionListener(this.controller);

		this.showFreqBox = new JCheckBox(
			Statistiek.rb.getString("showfrequencyCheckbox"), true);
		this.showFreqBox.setFont(Statistiek.font);
		this.showFreqBox.setBackground(backgroundColor);
		this.showFreqBox.setActionCommand("showFreqBox");
		this.showFreqBox.addActionListener(this.controller);

		this.showCumulativeBox = new JCheckBox(
			Statistiek.rb.getString("showcumulativefrequencyCheckbox"), true);
		this.showCumulativeBox.setFont(Statistiek.font);
		this.showCumulativeBox.setBackground(backgroundColor);
		this.showCumulativeBox.setActionCommand("showCumulativeBox");
		this.showCumulativeBox.addActionListener(this.controller);

		// ok-cancel
		this.okButton = new JButton("OK");
		this.okButton.addActionListener(this);
	}

	private void layoutGuiComponents()
	{
		// Variable
		Box hb1, hb2, hb3, hb4, hb5, hb6, hb7, hb8, hb9, hb10, hb11, hb12, hb13;
		Box vb1, vb2, vb3, vb4, vb5, vb6, vb7;

		hb1 = Box.createHorizontalBox();
		hb1.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		hb1.add(columnIndexLabel);

		hb2 = Box.createHorizontalBox();
		hb2.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
		hb2.add(columnIndexBox);

		vb1 = Box.createVerticalBox();
		Border border = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		vb1.setBorder(BorderFactory.createTitledBorder(border,
			Statistiek.rb.getString("variableLabel"), TitledBorder.CENTER,
			TitledBorder.TOP, Statistiek.font));
		vb1.add(hb1);
		vb1.add(hb2);
		vb1.add(Box.createVerticalGlue());

		// Bins
		hb1 = Box.createHorizontalBox();
		hb1.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		hb1.add(this.minBoundaryLabel);
		hb1.add(Box.createHorizontalStrut(5));
		hb1.add(Box.createHorizontalGlue());
		hb1.add(this.minBoundaryField);

		hb2 = Box.createHorizontalBox();
		hb2.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		hb2.add(this.binWidthLabel);
		hb2.add(Box.createHorizontalGlue());
		hb2.add(this.binWidthField);

		hb3 = Box.createHorizontalBox();
		hb3.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		hb3.add(this.boundariesLabel);

		hb4 = Box.createHorizontalBox();
		hb4.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		hb4.add(this.boundariesAreaScrollPane);

		hb5 = Box.createHorizontalBox();
		hb5.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		hb5.add(this.noObjectsLabel);
		hb5.add(Box.createHorizontalGlue());

		hb6 = Box.createHorizontalBox();
		hb6.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		hb6.add(this.minValueLabel);
		hb6.add(Box.createHorizontalGlue());

		hb7 = Box.createHorizontalBox();
		hb7.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		hb7.add(this.maxValueLabel);
		hb7.add(Box.createHorizontalGlue());

		vb2 = Box.createVerticalBox();
		vb2.setBorder(BorderFactory.createTitledBorder(border,
			Statistiek.rb.getString("classDivisionLabel"), TitledBorder.CENTER,
			TitledBorder.TOP, Statistiek.font));
		vb2.add(hb1);
		vb2.add(hb2);
		vb2.add(hb3);
		vb2.add(hb4);
		vb2.add(hb5);
		vb2.add(hb6);
		vb2.add(hb7);
		vb2.add(Box.createVerticalGlue());
		
		// Display

		hb1 = Box.createHorizontalBox();
		hb1.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		hb1.add(showPercBox);
		hb1.add(Box.createHorizontalGlue());

		hb2 = Box.createHorizontalBox();
		hb2.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));
		hb2.add(showFreqBox);
		hb2.add(Box.createHorizontalGlue());

		hb3 = Box.createHorizontalBox();
		hb3.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		hb3.add(showCumulativeBox);

		vb3 = Box.createVerticalBox();
		vb3.setBorder(BorderFactory.createTitledBorder(border,
			Statistiek.rb.getString("absRelLabel"), TitledBorder.CENTER,
			TitledBorder.TOP, Statistiek.font));
		vb3.add(hb1);
		vb3.add(hb2);
		vb3.add(hb3);
		vb3.add(Box.createVerticalGlue());

		Box hb0 = Box.createHorizontalBox();
		// hb0.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		hb0.add(Box.createHorizontalStrut(10));
		hb0.add(vb1);
		hb0.add(Box.createHorizontalStrut(10));
		hb0.add(vb2);
		hb0.add(Box.createHorizontalStrut(10));
		hb0.add(vb3);
		hb0.add(Box.createHorizontalStrut(10));

		vb0 = Box.createVerticalBox();
		vb0.add(hb0);
		vb0.add(Box.createVerticalStrut(10));
		vb0.add(okButton);
		vb0.add(Box.createVerticalStrut(20));

		panel.add(vb0);
		init();
		resize(vb0);
	}

	public void resize(JComponent c)
	{
		Dimension d = c.getPreferredSize();
		panel.setSize(new Dimension(d.width + 10, d.height));
		panel.setPreferredSize(new Dimension(d.width + 10, d.height));
	}

	public DialogButton getDialogButton()
	{
		return dialogButton;
	}

	public int getVarXBoxSelectedIndex()
	{
		return this.columnIndexBox.getSelectedIndex();
	}

	public void setModel(FrequencyTableModel model)
	{
		this.model = model;
	}

	public void update()
	{
		this.columnIndexBox.removeActionListener(this.controller);
		this.columnIndexBox.removeAllItems();

		for (String varName : this.model.getTableModel().getColumnNames())
		{
			this.columnIndexBox.addItem(varName);
		}

		if (this.model.columnIndexValid())
		{
			this.columnIndexBox.setSelectedIndex(this.model.getColumnIndex());
		}
		else
		{
			// set no item selected
			//System.out.println("FrequencyTableUserOptionsPanel.update(): no column index selected!");
			this.columnIndexBox.setSelectedIndex(-1);
		}
		this.columnIndexBox.addActionListener(this.controller);
		
		if (this.model.columnIndexValid())
		{
			ColumnType cType = this.model.getTableModel().getColumnTypes()
				.get(this.model.getColumnIndex());
			AllowedTypes type = cType.getType();
			if (type.equals(AllowedTypes.DOUBLE)
				|| type.equals(AllowedTypes.INTEGER))
			{
				this.minBoundaryField.setText(Statistiek.df.format(this.model
					.getBinBoundaries().get(0)));
				Double d = this.model.getBinBoundaries().get(1)
					- this.model.getBinBoundaries().get(0);
				this.binWidthField.setText(Statistiek.df.format(d));
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < this.model.getNoBins(); i++)
				{
					sb.append(Statistiek.df.format(this.model
						.getBinBoundaries().get(i)));
					sb.append(" - ");
					sb.append(Statistiek.df.format(this.model
						.getBinBoundaries().get(i + 1)));
					sb.append("\n");
				}
				sb.delete(sb.length() - 3, sb.length());
				this.boundariesArea.setText(sb.toString());
				this.noObjectsLabel.setText(Statistiek.rb
					.getString("numberLabel")
					+ this.model.getTableModel().getRowCount());
				this.minValueLabel.setText(Statistiek.rb.getString("minLabel")
					+ this.model.getTableModel().getColumnMin(
						this.model.getColumnIndex()));
				this.maxValueLabel.setText(Statistiek.rb.getString("maxLabel")
					+ this.model.getTableModel().getColumnMax(
						this.model.getColumnIndex()));
				//this.binsBox.getParent().setVisible(true);
				//this.binsLabel.getParent().setVisible(true);
				setEnumClasses(false);
			}
			else if (type.equals(AllowedTypes.ENUM))
			{
				StringBuilder sb = new StringBuilder();
				for (String s : cType.getEnumOptions())
				{
					sb.append(s);
					sb.append("\n");
				}
				sb.substring(0, sb.length() - 1);
				this.boundariesArea.setText(sb.toString());
				//this.binsBox.getParent().setVisible(false);
				//this.binsLabel.getParent().setVisible(false);
				setEnumClasses(true);
			}
		}
		
		this.showPercBox.setSelected(this.model.isShowPercentage());
		this.showFreqBox.setSelected(this.model.isShowFreq());
		this.showCumulativeBox.setSelected(this.model.isShowCumulative());
	}

	public double getBinWidth()
	{
		String s = this.binWidthField.getText();
		s = s.replace(',', '.');
		return Double.parseDouble(s);
	}

	public double getMinBoundary()
	{
		String s = this.minBoundaryField.getText();
		s = s.replace(',', '.');
		return Double.parseDouble(s);
	}

	private void setEnumClasses(boolean b)
	{
		//System.out.println("FrequencyTableUserOptionsPanel.setEnumClasses(" + b + ")");
		
		enumClasses = b;
		if (b) 
		{
    		minBoundaryLabel.getParent().setVisible(boundariesVisible && !b);
    		minBoundaryField.getParent().setVisible(boundariesVisible && !b);
    		binWidthLabel.getParent().setVisible(boundariesVisible && !b);
    		binWidthField.getParent().setVisible(boundariesVisible && !b);
    		noObjectsLabel.getParent().setVisible(boundariesVisible && !b);
    		minValueLabel.getParent().setVisible(boundariesVisible && !b);
    		maxValueLabel.getParent().setVisible(boundariesVisible && !b);
		}
		resize(vb0);
	}

	public void init()
	{
		if (vb0 != null)
			resize(vb0);
	}

	public void actionPerformed(ActionEvent e)
	{

		if (e.getSource() == okButton)
		{
			dialogButton.closeDialog();
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
					init();
					repaint();
				}
			};
			startDraad.start();
		}
	}

	public boolean isShowPercBoxSelected()
	{
		return this.showPercBox != null && this.showPercBox.isSelected();
	}

	public boolean isShowFreqBoxSelected()
	{
		return this.showFreqBox != null && this.showFreqBox.isSelected();
	}

	public boolean isShowCumulativeBoxSelected()
	{
		return this.showCumulativeBox != null && this.showCumulativeBox.isSelected();
	}
}
