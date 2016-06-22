package fi.statistiek.boxplot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
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

import fi.statistiek.DialogButton;
import fi.statistiek.Statistiek;
import fi.statistiek.types.AllowedTypes;
import fi.statistiek.types.ColumnType;

public class BoxplotUserOptionsPanel extends JPanel implements ActionListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BoxplotView view;
	private BoxplotController controller;
	private BoxplotModel model;

	private DialogButton dialogButton;

	private JPanel panel;
	private Box vb0;

	// variable settings
	private JLabel varLabel;
	private JComboBox varBox;

	// display settings
	private JLabel absRelLabel;
	private JCheckBox tukeyBox;
	private JRadioButton verticalBoxesRadioItem;
	private JRadioButton horizontalBoxesRadioItem;
	private JSeparator separator2;

	// split settings
	private JLabel splitsLabel;
	private JButton splitButton;
	private JLabel splitsVarLabel;
	private JComboBox splitVarBox;
	private JLabel splitBinsLabel;
	private JComboBox splitBinsBox;
	private JButton splitChooseBoundariesButton;
	private JSeparator separator3;
	private JLabel splitMinBoundaryLabel;
	private JTextField splitMinBoundaryField;
	private JLabel splitBinWidthLabel;
	private JTextField splitBinWidthField;
	private JLabel splitBoundariesLabel;
	private JTextArea splitBoundariesArea;
	private JScrollPane splitBoundariesAreaScrollPane;
	private JLabel splitNoObjectsLabel;
	private JLabel splitMinValueLabel;
	private JLabel splitMaxValueLabel;

	private JButton okButton;
	private JButton cancelButton;

	private boolean BoundariesVisible;
	private boolean splitBoundariesVisible;
	private boolean splitOptionsVisible;
	private boolean enumClasses;
	private boolean splitEnumClasses;

	private ArrayList<Double> Boundaries;

	public BoxplotUserOptionsPanel(BoxplotView view,
		BoxplotController controller, BoxplotModel model)
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
		this.panel.setBackground(new Color(230, 230, 230));

		this.splitsVarLabel = new JLabel(
			Statistiek.rb.getString("splitvariableLabel"));
		this.splitsVarLabel.setFont(Statistiek.font);

		// var settings
		this.varLabel = new JLabel(Statistiek.rb.getString("variableLabel"));
		this.varLabel.setFont(Statistiek.font);

		//this.varBox = new JComboBox();
		// error in swing: JComboBox is misbehaving (the same as JTextField) in reporting an unbounded max height
		// see also: http://stackoverflow.com/questions/7581846/swing-boxlayout-problem-with-jcombobox-without-using-setxxxsize/7582033#7582033
		// Solution: subclass and return a reasonable height
		this.varBox = new JComboBox<String>() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			/** 
             * @inherited <p>
             */
            @Override
            public Dimension getMaximumSize() {
                Dimension max = super.getMaximumSize();
                max.height = getPreferredSize().height;
                return max;
            }
        };

		this.varBox.setFont(Statistiek.font);
		this.varBox.setPreferredSize(new Dimension(100, 25));
		//this.varBox.setMaximumSize(new Dimension(100, 25));
		this.varBox.setActionCommand("varBox");
		this.varBox.addActionListener(this.controller);

		// display settings
		this.absRelLabel = new JLabel(Statistiek.rb.getString("absRelLabel"));
		this.absRelLabel.setFont(Statistiek.font);

		this.tukeyBox = new JCheckBox(
			Statistiek.rb.getString("tukeyCheckbox"), true);
		this.tukeyBox.setFont(Statistiek.font);
		this.tukeyBox.setOpaque(false);
		this.tukeyBox.setActionCommand("tukeyBox");
		this.tukeyBox.addActionListener(this.controller);

		this.verticalBoxesRadioItem = new JRadioButton(
			Statistiek.rb.getString("verticalboxplotsRadio"));
		this.verticalBoxesRadioItem.setFont(Statistiek.font);
		this.verticalBoxesRadioItem.setOpaque(false);
		this.verticalBoxesRadioItem.setActionCommand("verticalboxplotsRadio");
		this.verticalBoxesRadioItem.addActionListener(this.controller);

		this.horizontalBoxesRadioItem = new JRadioButton(
			Statistiek.rb.getString("horizontalboxplotsRadio"));
		this.horizontalBoxesRadioItem.setFont(Statistiek.font);
		this.horizontalBoxesRadioItem
			.setActionCommand("horizontalboxplotsRadio");
		this.horizontalBoxesRadioItem.setSelected(true);
		this.horizontalBoxesRadioItem.addActionListener(this.controller);
		this.horizontalBoxesRadioItem.setOpaque(false);

		ButtonGroup buttonGroup1 = new ButtonGroup();
		buttonGroup1.add(this.verticalBoxesRadioItem);
		buttonGroup1.add(this.horizontalBoxesRadioItem);

		this.separator2 = new JSeparator();
		this.separator2.setBorder(BorderFactory
			.createEtchedBorder(EtchedBorder.LOWERED));
		this.separator2.setMaximumSize(new Dimension(140, 3));

		// split settings
		this.splitButton = new JButton(
			Statistiek.rb.getString("splitoptionsButton"));
		this.splitButton.setPreferredSize(new Dimension(140, 25));
		this.splitButton.setFont(Statistiek.font);
		this.splitButton.setActionCommand("splitButton");
		this.splitButton.addActionListener(this);

		this.splitsVarLabel = new JLabel(
			Statistiek.rb.getString("splitvariableLabel"));
		this.splitsVarLabel.setFont(Statistiek.font);

		this.splitVarBox = new JComboBox();
		this.splitVarBox.setFont(Statistiek.font);
		this.splitVarBox.setPreferredSize(new Dimension(100, 25));
		this.splitVarBox.setMaximumSize(new Dimension(100, 25));
		this.splitVarBox.setActionCommand("splitVarBox");
		this.splitVarBox.addActionListener(this.controller);

		this.splitBinsLabel = new JLabel(
			Statistiek.rb.getString("noClassesLabel"));
		this.splitBinsLabel.setFont(Statistiek.font);

		Integer[] options1 = new Integer[20];
		for (int i = 0; i < 20; i++)
		{
			options1[i] = i + 1;
		}
		this.splitBinsBox = new JComboBox(options1);
		this.splitBinsBox.setFont(Statistiek.font);
		this.splitBinsBox.setMaximumSize(new Dimension(100, 25));
		this.splitBinsBox.setPreferredSize(new Dimension(100, 25));
		this.splitBinsBox.setSelectedIndex(1); //  by default select 1
		this.splitBinsBox.setActionCommand("splitBinsBox");
		this.splitBinsBox.addActionListener(this.controller);

		this.splitChooseBoundariesButton = new JButton(
			Statistiek.rb.getString("binsButton"));
		this.splitChooseBoundariesButton.setFont(Statistiek.font);
		this.splitChooseBoundariesButton
			.setPreferredSize(new Dimension(100, 25));
		this.splitChooseBoundariesButton
			.setActionCommand("splitChooseBinsButton");
		this.splitChooseBoundariesButton.addActionListener(this);

		this.separator3 = new JSeparator();
		this.separator3.setBorder(BorderFactory
			.createEtchedBorder(EtchedBorder.LOWERED));
		this.separator3.setMaximumSize(new Dimension(140, 3));

		this.splitMinBoundaryLabel = new JLabel(
			Statistiek.rb.getString("startvalueLabel"));
		this.splitMinBoundaryLabel.setFont(Statistiek.font);

		this.splitMinBoundaryField = new JTextField();
		this.splitMinBoundaryField.setMaximumSize(new Dimension(40, 25));
		this.splitMinBoundaryField.setMinimumSize(new Dimension(40, 25));
		this.splitMinBoundaryField.setPreferredSize(new Dimension(40, 25));
		this.splitMinBoundaryField.setActionCommand("splitMinBoundary");
		this.splitMinBoundaryField.addActionListener(controller);
		this.splitMinBoundaryField.addFocusListener(new FocusListener()
		{
			@Override
			public void focusLost(FocusEvent e)
			{
				BoxplotUserOptionsPanel.this.controller.processSplitMinBoundaryChanged();
			}
			
			@Override
			public void focusGained(FocusEvent e)
			{
			}
		});

		this.splitBinWidthLabel = new JLabel(
			Statistiek.rb.getString("classwidthLabel"));
		this.splitBinWidthLabel.setFont(Statistiek.font);

		this.splitBinWidthField = new JTextField();
		this.splitBinWidthField.setMaximumSize(new Dimension(40, 25));
		this.splitBinWidthField.setMinimumSize(new Dimension(40, 25));
		this.splitBinWidthField.setPreferredSize(new Dimension(40, 25));
		this.splitBinWidthField.setActionCommand("splitBinWidth");
		this.splitBinWidthField.addActionListener(controller);
		this.splitBinWidthField.addFocusListener(new FocusListener()
		{
			@Override
			public void focusLost(FocusEvent e)
			{
				BoxplotUserOptionsPanel.this.controller.processSplitBinWidthChanged();;
			}
			
			@Override
			public void focusGained(FocusEvent e)
			{
			}
		});

		this.splitBoundariesLabel = new JLabel(
			Statistiek.rb.getString("binsButton"));
		this.splitBoundariesLabel.setFont(Statistiek.font);

		this.splitBoundariesArea = new JTextArea();
		this.splitBoundariesArea.setEditable(false);
		this.splitBoundariesAreaScrollPane = new JScrollPane(
			this.splitBoundariesArea);
		this.splitBoundariesArea.setBorder(BorderFactory
			.createLoweredBevelBorder());
		this.splitBoundariesAreaScrollPane.setMaximumSize(new Dimension(120, 140));
		this.splitBoundariesAreaScrollPane.setMinimumSize(new Dimension(120, 140));
		this.splitBoundariesAreaScrollPane.setPreferredSize(new Dimension(130, 140));

		this.splitNoObjectsLabel = new JLabel("");
		this.splitNoObjectsLabel.setFont(Statistiek.font);

		this.splitMinValueLabel = new JLabel("");
		this.splitMinValueLabel.setFont(Statistiek.font);

		this.splitMaxValueLabel = new JLabel("");
		this.splitMaxValueLabel.setFont(Statistiek.font);

		// ok-cancel
		this.okButton = new JButton("OK");
		this.okButton.addActionListener(this);
	}

	private void layoutGuiComponents()
	{
		// Variable
		Box hb1, hb2, hb3, hb4, hb5, hb6, hb7, hb8, hb9, hb10, hb11, hb12, hb13, hb14;
		Box vb1, vb2, vb3, vb4, vb5, vb6, vb7;

		hb1 = Box.createHorizontalBox();
		hb1.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		hb1.add(varLabel);

		hb2 = Box.createHorizontalBox();
		hb2.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
		hb2.add(varBox);

		vb1 = Box.createVerticalBox();
		Border border = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		vb1.setBorder(BorderFactory.createTitledBorder(border,
			Statistiek.rb.getString("variableLabel"), TitledBorder.CENTER,
			TitledBorder.TOP, Statistiek.font));
		vb1.add(hb2);
		vb1.add(Box.createVerticalGlue());

		// Display
		hb1 = Box.createHorizontalBox();
		hb1.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));
		hb1.add(absRelLabel);
		hb1.add(Box.createHorizontalGlue());

		hb2 = Box.createHorizontalBox();
		hb2.add(tukeyBox);
		hb2.add(Box.createHorizontalGlue());

		hb3 = Box.createHorizontalBox();
		hb3.add(horizontalBoxesRadioItem);
		hb3.add(Box.createHorizontalGlue());

		hb4 = Box.createHorizontalBox();
		hb4.add(verticalBoxesRadioItem);
		hb4.add(Box.createHorizontalGlue());

		hb5 = Box.createHorizontalBox();
		hb5.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		hb5.add(separator2);

		vb3 = Box.createVerticalBox();
		vb3.setBorder(BorderFactory.createTitledBorder(border,
			Statistiek.rb.getString("absRelLabel"), TitledBorder.CENTER,
			TitledBorder.TOP, Statistiek.font));
		vb3.add(hb2);
		vb3.add(hb3);
		vb3.add(hb4);
		vb3.add(hb5);
		vb3.add(Box.createVerticalGlue());

		// splitOptions
		hb1 = Box.createHorizontalBox();
		hb1.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));
		hb1.add(splitButton);

		hb2 = Box.createHorizontalBox();
		hb2.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		hb2.add(splitsVarLabel);

		hb3 = Box.createHorizontalBox();
		hb3.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		hb3.add(splitVarBox);

		hb4 = Box.createHorizontalBox();
		hb4.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		hb4.add(splitBinsLabel);

		hb5 = Box.createHorizontalBox();
		hb5.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		hb5.add(splitBinsBox);

		hb6 = Box.createHorizontalBox();
		hb6.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		hb6.add(separator3);

		hb7 = Box.createHorizontalBox();
		hb7.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		hb7.add(splitChooseBoundariesButton);

		hb8 = Box.createHorizontalBox();
		hb8.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		hb8.add(this.splitMinBoundaryLabel);
		hb8.add(Box.createHorizontalStrut(5));
		hb8.add(Box.createHorizontalGlue());
		hb8.add(this.splitMinBoundaryField);

		hb9 = Box.createHorizontalBox();
		hb9.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		hb9.add(this.splitBinWidthLabel);
		hb9.add(Box.createHorizontalStrut(5));
		hb9.add(Box.createHorizontalGlue());
		hb9.add(this.splitBinWidthField);

		hb10 = Box.createHorizontalBox();
		hb10.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		hb10.add(this.splitBoundariesLabel);

		hb11 = Box.createHorizontalBox();
		hb11.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		hb11.add(this.splitBoundariesAreaScrollPane);

		hb12 = Box.createHorizontalBox();
		hb12.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		hb12.add(this.splitNoObjectsLabel);
		hb12.add(Box.createHorizontalGlue());

		hb13 = Box.createHorizontalBox();
		hb13.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		hb13.add(this.splitMinValueLabel);
		hb13.add(Box.createHorizontalGlue());

		hb14 = Box.createHorizontalBox();
		hb14.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		hb14.add(this.splitMaxValueLabel);
		hb14.add(Box.createHorizontalGlue());

		vb4 = Box.createVerticalBox();
		vb4.setBorder(BorderFactory.createTitledBorder(border,
			Statistiek.rb.getString("splitsLabel"), TitledBorder.CENTER,
			TitledBorder.TOP, Statistiek.font));
		vb4.add(hb1);
		vb4.add(hb2);
		vb4.add(hb3);
		vb4.add(hb4);
		vb4.add(hb5);
		vb4.add(hb6);
		vb4.add(hb7);
		vb4.add(hb8);
		vb4.add(hb9);
		vb4.add(hb10);
		vb4.add(hb11);
		vb4.add(hb12);
		vb4.add(hb13);
		vb4.add(hb14);
		vb4.add(Box.createVerticalGlue());

		Box hb0 = Box.createHorizontalBox();
		// hb0.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		hb0.add(Box.createHorizontalStrut(10));
		hb0.add(vb1);
		hb0.add(Box.createHorizontalStrut(10));
		hb0.add(vb3);
		hb0.add(Box.createHorizontalStrut(10));
		hb0.add(vb4);
		hb0.add(Box.createHorizontalStrut(10));

		vb0 = Box.createVerticalBox();
		// vb0.add(settingsLabel);
		// vb0.add(Box.createVerticalStrut(10));
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

	public String getColumnBoxSelectedString()
	{
		return (String) this.varBox.getSelectedItem();
	}

	public boolean isTukeyBoxSelected()
	{
		return (this.tukeyBox != null && this.tukeyBox.isSelected());
	}

	public boolean isVerticalBoxesButtonSelected()
	{
		return verticalBoxesRadioItem.isSelected();
	}

	public int getVarBoxSelectedIndex()
	{
		return this.varBox.getSelectedIndex();
	}

	public int getSplitVarBoxSelectedIndex()
	{
		return this.splitVarBox.getSelectedIndex();
	}

	public int getSplitBinsBoxSelectedInt()
	{
		return ((Integer) this.splitBinsBox.getSelectedItem()).intValue();
	}

	public double getSplitMinBoundary()
	{
		String s = this.splitMinBoundaryField.getText();
		s = s.replace(',', '.');
		return Double.parseDouble(s);
	}

	public void setSplitMinBoundary(double d)
	{
		this.splitMinBoundaryField.setText(String.valueOf(d));
	}

	public double getSplitBinWidth()
	{
		String s = this.splitBinWidthField.getText();
		s = s.replace(',', '.');
		return Double.parseDouble(s);
	}

	public void setSplitBinWidth(double d)
	{
		this.splitBinWidthField.setText(String.valueOf(d));
	}
	
	/**
	 *Set the split bin width based on the model's split bin boundaries. 
	 */
	public void setSplitBinWidth()
	{
		this.splitBinWidthField.setText(Statistiek.getFormattedBinWidth(this.model.getSplitBinBoundaries()));
	}
	
	public void setModel(BoxplotModel model)
	{
		this.model = model;
	}

	public void update()
	{
		this.varBox.removeActionListener(this.controller);
		this.varBox.removeAllItems();
		for (String varName : this.model.getStatTableModel().getColumnNames())
		{
//			System.out.println("BoxplotUserOptionsPanel.update(): varBox.addItem("
//				+ varName + ")");
			this.varBox.addItem(varName);
		}
		if (this.model.getColumnIndex() > -1)
		{
			this.varBox.setSelectedIndex(this.model.getColumnIndex());
		}
		else
		{
			// set no item selected
			this.varBox.setSelectedIndex(-1);
		}
		this.varBox.addActionListener(this.controller);

		this.splitVarBox.removeActionListener(this.controller);
		this.splitVarBox.removeAllItems();
		this.splitVarBox.addItem(Statistiek.rb.getString("chooseItem"));
		for (int column = 0; column < this.model.getStatTableModel()
			.getColumnCount(); column++)
		{
			splitVarBox.addItem(this.model.getStatTableModel()
				.getColumnName(column));
		}
		this.splitVarBox.setSelectedIndex(this.model.getSplitOptions()
			.getColumnSplitIndex() + 1);
		this.splitVarBox.addActionListener(this.controller);

		if (this.model.getColumnIndex() > -1)
		{

		}

		this.splitBinsBox.removeActionListener(this.controller);
		this.splitBinsBox.setSelectedItem(new Integer(this.model
			.getSplitOptions().getBinBoundaries().size() - 1));
		this.splitBinsBox.addActionListener(this.controller);

		if (this.model.getSplitOptions().getColumnSplitIndex() > -1)
		{
			ColumnType cSplitType = this.model.getStatTableModel().getColumnTypes()
				.get(this.model.getSplitOptions().getColumnSplitIndex());
			AllowedTypes splitType = cSplitType.getType();
			if (splitType.equals(AllowedTypes.DOUBLE)
				|| splitType.equals(AllowedTypes.INTEGER))
			{
				this.splitMinBoundaryField.setText(
					Statistiek.getStringValue(this.model.getSplitOptions().getBinBoundaries().get(0)));
				this.splitBinWidthField.setText(Statistiek.getFormattedBinWidth(this.model.getSplitBinBoundaries()));
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < this.model.getSplitOptions()
					.getBinBoundaries().size() - 1; i++)
				{
					sb.append(
						Statistiek.getStringValue(this.model.getSplitOptions().getBinBoundaries().get(i)));
					sb.append(" -< ");
					sb.append(
						Statistiek.getStringValue(this.model.getSplitOptions().getBinBoundaries().get(i + 1)));
					sb.append("\n");
				}
				//sb.delete(sb.length() - 3, sb.length());
				this.splitBoundariesArea.setText(sb.toString());
				this.splitNoObjectsLabel.setText(Statistiek.rb
					.getString("numberLabel")
					+ this.model.getStatTableModel().getRowCount());
				String splitMinValue = Statistiek.getStringValue(
					this.model.getStatTableModel().getColumnMin(this.model.getSplitOptions().getColumnSplitIndex()));
				this.splitMinValueLabel.setText(Statistiek.rb.getString("minLabel") + splitMinValue);
				String splitMaxValue = Statistiek.getStringValue(
					this.model.getStatTableModel().getColumnMax(this.model.getSplitOptions().getColumnSplitIndex()));
				this.splitMaxValueLabel.setText(Statistiek.rb.getString("maxLabel") + splitMaxValue);
				this.splitBinsBox.getParent().setVisible(true);
				this.splitBinsLabel.getParent().setVisible(true);
				setSplitEnumClasses(false);
			}
			else if (splitType.equals(AllowedTypes.ENUM))
			{
				StringBuilder sb = new StringBuilder();
				for (String s : cSplitType.getEnumOptions())
				{
					sb.append(s);
					sb.append("\n");
				}
				sb.substring(0, sb.length() - 1);
				this.splitBoundariesArea.setText(sb.toString());
				this.splitBinsBox.getParent().setVisible(false);
				this.splitBinsLabel.getParent().setVisible(false);
				setSplitEnumClasses(true);
			}
		}

		this.tukeyBox.setSelected(this.model.isTukeyBox());

		if (this.model.isVerticalBoxplots())
		{
			verticalBoxesRadioItem.setSelected(true);
		}
		else
		{
			horizontalBoxesRadioItem.setSelected(true);
		}

		boolean split = this.model.getSplitOptions().getColumnSplitIndex() > -1;
		this.setVisibleSplitOptions(split);

	}

	public void init()
	{
		setVisibleSplitBoundaryOptions(false);
		if (vb0 != null)
			resize(vb0);
	}

	private void setSplitEnumClasses(boolean b)
	{
		splitEnumClasses = b;
		// if(b) {
		splitMinBoundaryLabel.getParent().setVisible(
			splitBoundariesVisible && !b);
		splitMinBoundaryField.getParent().setVisible(
			splitBoundariesVisible && !b);
		splitBinWidthLabel.getParent().setVisible(splitBoundariesVisible && !b);
		splitBinWidthField.getParent().setVisible(splitBoundariesVisible && !b);
		splitNoObjectsLabel.getParent()
			.setVisible(splitBoundariesVisible && !b);
		splitMinValueLabel.getParent().setVisible(splitBoundariesVisible && !b);
		splitMaxValueLabel.getParent().setVisible(splitBoundariesVisible && !b);
		// }
		resize(vb0);
	}

	private void setVisibleSplitBoundaryOptions(boolean b)
	{
		splitBoundariesVisible = b;
		separator3.getParent().setVisible(b);
		splitMinBoundaryLabel.getParent().setVisible(b && !splitEnumClasses);
		splitMinBoundaryField.getParent().setVisible(b && !splitEnumClasses);
		splitBinWidthLabel.getParent().setVisible(b && !splitEnumClasses);
		splitBinWidthField.getParent().setVisible(b && !splitEnumClasses);
		splitBoundariesLabel.getParent().setVisible(b);
		splitBoundariesArea.getParent().setVisible(b);
		splitBoundariesAreaScrollPane.getParent().setVisible(b);
		splitNoObjectsLabel.getParent().setVisible(b && !splitEnumClasses);
		splitMinValueLabel.getParent().setVisible(b && !splitEnumClasses);
		splitMaxValueLabel.getParent().setVisible(b && !splitEnumClasses);
		if (!b)
		{
			splitChooseBoundariesButton.setText(Statistiek.rb
				.getString("binsButton"));
		}
		else
		{
			splitChooseBoundariesButton.setText(Statistiek.rb
				.getString("hideButtonLabel"));
		}
	}

	private void setVisibleSplitOptions(boolean b)
	{
		splitOptionsVisible = b;
		separator2.getParent().setVisible(b);
		// splitSingleViewBox.getParent().setVisible(b);
		splitsVarLabel.getParent().setVisible(b);
		splitVarBox.getParent().setVisible(b);
		if (!b)
			splitBinsLabel.getParent().setVisible(b);
		if (!b)
			splitBinsBox.getParent().setVisible(b);
		splitChooseBoundariesButton.getParent().setVisible(b);
		if (!b)
		{
			splitButton.setText(Statistiek.rb.getString("splitoptionsButton"));
			setVisibleSplitBoundaryOptions(false);
		}
		else
		{
			splitButton.setText(Statistiek.rb
				.getString("removeSplitoptionsButton"));
		}
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == splitChooseBoundariesButton)
		{
			if (splitBoundariesVisible)
			{
				setVisibleSplitBoundaryOptions(false);
			}
			else
			{
				setVisibleSplitBoundaryOptions(true);
			}

			resize(vb0);
		}
		else if (e.getSource() == splitButton)
		{
			if (splitOptionsVisible)
			{
				setVisibleSplitOptions(false);
				this.splitVarBox.setSelectedIndex(0);
			}
			else
			{
				setVisibleSplitOptions(true);
			}
			resize(vb0);
		}
		else if (e.getSource() == okButton)
		{
			setVisibleSplitBoundaryOptions(false);
			if (splitVarBox.getSelectedIndex() == 0)
				setVisibleSplitOptions(false);
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
}
