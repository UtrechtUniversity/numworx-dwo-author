package fi.statistiek.histogram;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
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

/**
 * @author borku102
 *
 */
public class HistogramUserOptionsPanel extends JPanel implements ActionListener
{

	private HistogramView view;
	private HistogramController controller;
	private HistogramModel model;

	private DialogButton dialogButton;

	private JPanel panel;
	private Box vb0;

	// variable settings
	private JLabel varLabel;
	private JComboBox varBox;
	private JLabel axisLabel;
	private JComboBox axisBox;

	// bin settings
	private JCheckBox optimizeScaleBox;
	private JLabel binsLabel;
	/**
	 * Box for choosing the number of bins.
	 */
	private JComboBox binsBox;
	/**
	 * Separator between bin boundaries settings and number of bins setting 
	 */
	private JSeparator separator1;
	private JLabel minBoundaryLabel;
	private JTextField minBoundaryField;
	private JLabel binWidthLabel;
	private JTextField binWidthField;
	private JLabel maxOnScaleLabel;
	/**
	 * Max boundary input field. To be used when the
	 * scale is not automatically optimized.
	 */
	private JTextField maxOnScaleField;
	private JLabel noObjectsLabel;
	private JLabel minValueLabel;
	private JLabel maxValueLabel;

	// display settings
	private JLabel absRelLabel;
	private JRadioButton amountRadioItem;
	private JRadioButton percentageRadioItem;
	private JCheckBox cumulativeBox;
	/**
	 * Separator between amount/percentage settings and label positioning settings
	 */
	private JSeparator separatorAmountLabel;
	private JRadioButton labelUnderBinRadioItem; // labels midden onder staven
	private JRadioButton labelBetweenBinsRadioItem; // labels tussen staven
	/**
	 * Separator between label positioning settings and split view setting
	 */
	private JSeparator separatorLabelSplitView;
	private JRadioButton nextToEachOtherRadioItem;
	private JRadioButton aboveEachOtherRadioItem;
	private JRadioButton separateRadioItem;
	private JRadioButton singleViewRadioItem;
	/**
	 * Separator between split view setting and split percentage
	 */
	private JSeparator separatorSplitViewPercentage;
	/**
	 * Indicates that the percentage in the split is
	 * relative to the end total.
	 */
	private JRadioButton percentage_endTotal;
	/**
	 * Indicates that the percentage in the split is
	 * relative to the split total.
	 */
	private JRadioButton percentage_splitTotal;

	// split settings
	private JButton splitButton;
	private JLabel splitVarLabel;
	private JComboBox splitVarBox;
	private JLabel splitBinsLabel;
	/**
	 * Box for choosing the number of split bins.
	 */
	private JComboBox splitBinsBox;
	private JButton splitChooseBoundariesButton;
	/**
	 * Separator between number of split bins settings and split bin boundaries settings
	 */
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

	private JCheckBox stackModeBox;

	private JButton okButton;

	private boolean splitBoundariesVisible;
	private boolean splitOptionsVisible;
	private boolean enumClasses;
	private boolean splitEnumClasses;

	public HistogramUserOptionsPanel(HistogramView view,
		HistogramController controller, HistogramModel model)
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

		this.splitVarLabel = new JLabel(
			Statistiek.rb.getString("splitvariableLabel"));
		this.splitVarLabel.setFont(Statistiek.font);

		// VARIABLE SETTINGS
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
//		this.varBox.setMaximumSize(new Dimension(100, 25));
		this.varBox.setActionCommand("varBox");
		this.varBox.addActionListener(this.controller);

		this.axisLabel = new JLabel(Statistiek.rb.getString("axisLabel"));
		this.axisLabel.setFont(Statistiek.font);

		String[] options2 = new String[2];
		options2[0] = "X";
		options2[1] = "Y";
		this.axisBox = new JComboBox(options2);
		this.axisBox.setFont(Statistiek.font);
		this.axisBox.setPreferredSize(new Dimension(100, 25));
		this.axisBox.setMaximumSize(new Dimension(100, 25));
		this.axisBox.setActionCommand("axisBox");
		this.axisBox.addActionListener(this.controller);

		// BIN SETTINGS
		this.optimizeScaleBox = new JCheckBox(
			Statistiek.rb.getString("optimizeScaleBox"), false);
		this.optimizeScaleBox.setFont(Statistiek.font);
		this.optimizeScaleBox.setOpaque(false);
		this.optimizeScaleBox.setActionCommand("optimizeScaleBox");
		this.optimizeScaleBox.addActionListener(controller);
		
		this.binsLabel = new JLabel(Statistiek.rb.getString("noClassesLabel"));
		this.binsLabel.setFont(Statistiek.font);

		Integer[] options1 = new Integer[50];
		for (int i = 0; i < 50; i++)
		{
			options1[i] = i + 1;
		}
		this.binsBox = new JComboBox(options1);
		this.binsBox.setFont(Statistiek.font);
		this.binsBox.setMaximumSize(new Dimension(100, 25));
		this.binsBox.setPreferredSize(new Dimension(100, 25));
		this.binsBox.setActionCommand("binsBox");
		this.binsBox.addActionListener(this.controller);
		
		this.separator1 = new JSeparator();
		this.separator1.setBorder(BorderFactory
			.createEtchedBorder(EtchedBorder.LOWERED));
		this.separator1.setMaximumSize(new Dimension(140, 3));

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

		this.maxOnScaleLabel = new JLabel(
			Statistiek.rb.getString("maxValueLabel"));
		this.maxOnScaleLabel.setFont(Statistiek.font);

		this.maxOnScaleField = new JTextField();
		this.maxOnScaleField.setMaximumSize(new Dimension(40, 25));
		this.maxOnScaleField.setMinimumSize(new Dimension(40, 25));
		this.maxOnScaleField.setPreferredSize(new Dimension(40, 25));
		this.maxOnScaleField.setActionCommand("maxOnScale");
		this.maxOnScaleField.addActionListener(controller);
		this.maxOnScaleField.addFocusListener(controller);

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

		this.noObjectsLabel = new JLabel("");
		this.noObjectsLabel.setFont(Statistiek.font);

		this.minValueLabel = new JLabel("");
		this.minValueLabel.setFont(Statistiek.font);

		this.maxValueLabel = new JLabel("");
		this.maxValueLabel.setFont(Statistiek.font);

		// DISPLAY SETTINGS
		this.absRelLabel = new JLabel(Statistiek.rb.getString("absRelLabel"));
		this.absRelLabel.setFont(Statistiek.font);

		this.amountRadioItem = new JRadioButton(
			Statistiek.rb.getString("amountLabel"));
		this.amountRadioItem.setFont(Statistiek.font);
		this.amountRadioItem.setOpaque(false);
		this.amountRadioItem.setActionCommand("amountRadioItem");
		this.amountRadioItem.addActionListener(this.controller);

		this.percentageRadioItem = new JRadioButton(
			Statistiek.rb.getString("percentageRadio"));
		this.percentageRadioItem.setFont(Statistiek.font);
		this.percentageRadioItem.setActionCommand("percentageRadioItem");
		this.percentageRadioItem.addActionListener(this.controller);
		this.percentageRadioItem.setOpaque(false);

		ButtonGroup buttonGroup1 = new ButtonGroup();
		buttonGroup1.add(this.percentageRadioItem);
		buttonGroup1.add(this.amountRadioItem);

		this.cumulativeBox = new JCheckBox(
			Statistiek.rb.getString("cumulativeCheckbox"), false);
		this.cumulativeBox.setFont(Statistiek.font);
		this.cumulativeBox.setOpaque(false);
		this.cumulativeBox.setActionCommand("cumulativeBox");
		this.cumulativeBox.addActionListener(controller);

		this.separatorAmountLabel = new JSeparator();
		this.separatorAmountLabel.setBorder(BorderFactory
			.createEtchedBorder(EtchedBorder.LOWERED));
		this.separatorAmountLabel.setMaximumSize(new Dimension(140, 3));

		// radiobuttons for position labels
		this.labelBetweenBinsRadioItem = new JRadioButton(
			Statistiek.rb.getString("labelBetweenBinsRadio"));
		this.labelBetweenBinsRadioItem.setFont(Statistiek.font);
		this.labelBetweenBinsRadioItem.setOpaque(false);
		this.labelBetweenBinsRadioItem.setActionCommand("labelsBetweenBinsRadioItem");
		this.labelBetweenBinsRadioItem.addActionListener(this.controller);

		this.labelUnderBinRadioItem = new JRadioButton(
			Statistiek.rb.getString("labelUnderBinRadio"));
		this.labelUnderBinRadioItem.setFont(Statistiek.font);
		this.labelUnderBinRadioItem.setActionCommand("labelsUnderBinRadioItem");
		this.labelUnderBinRadioItem.addActionListener(this.controller);
		this.labelUnderBinRadioItem.setOpaque(false);

		ButtonGroup buttonGroup3 = new ButtonGroup();
		buttonGroup3.add(this.labelBetweenBinsRadioItem);
		buttonGroup3.add(this.labelUnderBinRadioItem);

		this.separatorLabelSplitView = new JSeparator();
		this.separatorLabelSplitView.setBorder(BorderFactory
			.createEtchedBorder(EtchedBorder.LOWERED));
		this.separatorLabelSplitView.setMaximumSize(new Dimension(140, 3));

		this.nextToEachOtherRadioItem = new JRadioButton(
			Statistiek.rb.getString("nextToEachOtherRadioItem"), false);
		this.nextToEachOtherRadioItem.setFont(Statistiek.font);
		this.nextToEachOtherRadioItem.setOpaque(false);
		this.nextToEachOtherRadioItem.setActionCommand("nextToEachOther");
		this.nextToEachOtherRadioItem.addActionListener(this.controller);

		this.aboveEachOtherRadioItem = new JRadioButton(
			Statistiek.rb.getString("aboveEachOtherRadioItem"), false);
		this.aboveEachOtherRadioItem.setFont(Statistiek.font);
		this.aboveEachOtherRadioItem.setOpaque(false);
		this.aboveEachOtherRadioItem.setActionCommand("aboveEachOther");
		this.aboveEachOtherRadioItem.addActionListener(this.controller);

		this.separateRadioItem = new JRadioButton(
			Statistiek.rb.getString("separateFromEachOtherRadioItem"), false);
		this.separateRadioItem.setFont(Statistiek.font);
		this.separateRadioItem.setOpaque(false);
		this.separateRadioItem.setActionCommand("separateFromEachOther");
		this.separateRadioItem.addActionListener(this.controller);

		this.singleViewRadioItem = new JRadioButton(
			Statistiek.rb.getString("splitsingleviewCheckBox"), false);
		this.singleViewRadioItem.setFont(Statistiek.font);
		this.singleViewRadioItem.setOpaque(false);
		this.singleViewRadioItem.setActionCommand("splitSingleView");
		this.singleViewRadioItem.addActionListener(this.controller);

		ButtonGroup buttonGroup2 = new ButtonGroup();
		if (this.model.isFrequencyPolygonMode())
			buttonGroup2.add(this.singleViewRadioItem);
		else
		{
			buttonGroup2.add(this.nextToEachOtherRadioItem);
			buttonGroup2.add(this.aboveEachOtherRadioItem);
		}
		buttonGroup2.add(this.separateRadioItem);
		
		this.separatorSplitViewPercentage = new JSeparator();
		this.separatorSplitViewPercentage.setBorder(BorderFactory
			.createEtchedBorder(EtchedBorder.LOWERED));
		this.separatorSplitViewPercentage.setMaximumSize(new Dimension(140, 3));

		this.percentage_splitTotal = new JRadioButton(
			Statistiek.rb.getString("percentage_splitTotal"), false);
		this.percentage_splitTotal.setFont(Statistiek.font);
		this.percentage_splitTotal.setOpaque(false);
		this.percentage_splitTotal.setActionCommand("percentage_splitTotal");
		this.percentage_splitTotal.addActionListener(this.controller);

		this.percentage_endTotal = new JRadioButton(
			Statistiek.rb.getString("percentage_endTotal"), false);
		this.percentage_endTotal.setFont(Statistiek.font);
		this.percentage_endTotal.setOpaque(false);
		this.percentage_endTotal.setActionCommand("percentage_endTotal");
		this.percentage_endTotal.addActionListener(this.controller);

		ButtonGroup buttonGroup4 = new ButtonGroup();
		buttonGroup4.add(this.percentage_splitTotal);
		buttonGroup4.add(this.percentage_endTotal);

		// SPLIT SETTINGS
		this.splitButton = new JButton(
			Statistiek.rb.getString("splitoptionsButton"));
		this.splitButton.setPreferredSize(new Dimension(140, 25));
		this.splitButton.setFont(Statistiek.font);
		this.splitButton.setActionCommand("splitButton");
		this.splitButton.addActionListener(this);

		this.splitVarLabel = new JLabel(
			Statistiek.rb.getString("splitvariableLabel"));
		this.splitVarLabel.setFont(Statistiek.font);

		this.splitVarBox = new JComboBox();
		this.splitVarBox.setFont(Statistiek.font);
		this.splitVarBox.setPreferredSize(new Dimension(100, 25));
		this.splitVarBox.setMaximumSize(new Dimension(100, 25));
		this.splitVarBox.setActionCommand("splitVarBox");
		// changes in split variable have GUI consequences,
		// so action is performed by the userOptionsPanel
		this.splitVarBox.addActionListener(this);

		this.splitBinsLabel = new JLabel(
			Statistiek.rb.getString("noClassesLabel"));
		this.splitBinsLabel.setFont(Statistiek.font);

		this.splitBinsBox = new JComboBox(options1);
		this.splitBinsBox.setFont(Statistiek.font);
		this.splitBinsBox.setMaximumSize(new Dimension(100, 25));
		this.splitBinsBox.setPreferredSize(new Dimension(100, 25));
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
				HistogramUserOptionsPanel.this.controller.updateSplitBoundariesFromBinSettings();
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
				HistogramUserOptionsPanel.this.controller.updateSplitBoundariesFromBinSettings();
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
		this.splitBoundariesAreaScrollPane.getVerticalScrollBar().setUnitIncrement(Statistiek.scrollSpeedUnit);
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

		this.stackModeBox = new JCheckBox(
			Statistiek.rb.getString("stackfrequencypolygonsCheckbox"), true);
		this.stackModeBox.setFont(Statistiek.font);
		this.stackModeBox.setOpaque(false);
		this.stackModeBox.setActionCommand("stackMode");
		this.stackModeBox.addActionListener(this.controller);
		this.stackModeBox.setVisible(this.model.isFrequencyPolygonMode());

		// ok-cancel
		this.okButton = new JButton("OK");
		this.okButton.addActionListener(this);
	}

	private void layoutGuiComponents()
	{
		// VARIABLE
		Box hb1, hb2, hb3, hb4, hb5, hb6, hb7, hb8, hb9, hb10, hb11, hb12, hb13, hb14;
		Box vb1, vb2, vb3, vb4, vb5, vb6, vb7;

		hb2 = Box.createHorizontalBox();
		hb2.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
		hb2.add(varBox);

		hb3 = Box.createHorizontalBox();
		hb3.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		hb3.add(axisLabel);

		hb4 = Box.createHorizontalBox();
		hb4.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		hb4.add(axisBox);

		vb1 = Box.createVerticalBox();
		Border border = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		vb1.setBorder(BorderFactory.createTitledBorder(border,
			Statistiek.rb.getString("variableLabel"), TitledBorder.CENTER,
			TitledBorder.TOP, Statistiek.font));
		vb1.add(hb2);
		vb1.add(hb3);
		vb1.add(hb4);
		vb1.add(Box.createVerticalGlue());

		// BIN SETTINGS
		hb1 = Box.createHorizontalBox();
		hb1.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		hb1.add(optimizeScaleBox);

		hb2 = Box.createHorizontalBox();
		hb2.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		hb2.add(this.minBoundaryLabel);
		hb2.add(Box.createHorizontalStrut(5));
		hb2.add(Box.createHorizontalGlue());
		hb2.add(this.minBoundaryField);
		// set maximum size prevents hb filling up vertical space
		// and makes hb adjust its height to the content
		hb2.setMaximumSize(new Dimension(250, 200));

		hb3 = Box.createHorizontalBox();
		hb3.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		hb3.add(this.maxOnScaleLabel);
		hb3.add(Box.createHorizontalStrut(5));
		hb3.add(Box.createHorizontalGlue());
		hb3.add(this.maxOnScaleField);
		// set maximum size prevents hb filling up vertical space
		// and makes hb adjust its height to the content
		hb3.setMaximumSize(new Dimension(250, 200));

		hb4 = Box.createHorizontalBox();
		hb4.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		hb4.add(this.binWidthLabel);
		hb4.add(Box.createHorizontalStrut(5));
		hb4.add(Box.createHorizontalGlue());
		hb4.add(this.binWidthField);
		// set maximum size prevents hb filling up vertical space
		// and makes hb adjust its height to the content
		hb4.setMaximumSize(new Dimension(250, 200));

		hb5 = Box.createHorizontalBox();
		hb5.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		hb5.add(separator1);

		hb6 = Box.createHorizontalBox();
		hb6.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		hb6.add(binsLabel);

		hb7 = Box.createHorizontalBox();
		hb7.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
		hb7.add(binsBox);

		hb8 = Box.createHorizontalBox();
		hb8.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		hb8.add(this.noObjectsLabel);
		hb8.add(Box.createHorizontalGlue());

		hb9 = Box.createHorizontalBox();
		hb9.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		hb9.add(this.minValueLabel);
		hb9.add(Box.createHorizontalGlue());

		hb10 = Box.createHorizontalBox();
		hb10.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		hb10.add(this.maxValueLabel);
		hb10.add(Box.createHorizontalGlue());

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
		vb2.add(hb8);
		vb2.add(hb9);
		vb2.add(hb10);
		vb2.add(Box.createVerticalGlue());

		// DISPLAY
		hb1 = Box.createHorizontalBox();
		hb1.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));
		hb1.add(absRelLabel);
		hb1.add(Box.createHorizontalGlue());

		hb2 = Box.createHorizontalBox();
		hb2.add(amountRadioItem);
		hb2.add(Box.createHorizontalGlue());

		hb3 = Box.createHorizontalBox();
		hb3.add(percentageRadioItem);
		hb3.add(Box.createHorizontalGlue());

		Box hb3a = Box.createHorizontalBox();
		hb3a.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
		hb3a.add(cumulativeBox);
		hb3a.add(Box.createHorizontalGlue());

		hb4 = Box.createHorizontalBox();
		hb4.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		hb4.add(separatorAmountLabel);

		hb5 = Box.createHorizontalBox();
		hb5.add(labelBetweenBinsRadioItem);
		hb5.add(Box.createHorizontalGlue());

		hb6 = Box.createHorizontalBox();
		hb6.add(labelUnderBinRadioItem);
		hb6.add(Box.createHorizontalGlue());

		hb7 = Box.createHorizontalBox();
		hb7.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		hb7.add(separatorLabelSplitView);

		hb8 = Box.createHorizontalBox();
		hb8.add(separateRadioItem);
		hb8.add(Box.createHorizontalGlue());

		Box hb8a = Box.createHorizontalBox();
		hb8a.add(singleViewRadioItem);
		hb8a.add(Box.createHorizontalGlue());

		hb9 = Box.createHorizontalBox();
		hb9.add(aboveEachOtherRadioItem);
		hb9.add(Box.createHorizontalGlue());

		hb10 = Box.createHorizontalBox();
		hb10.add(nextToEachOtherRadioItem);
		hb10.add(Box.createHorizontalGlue());

		hb11 = Box.createHorizontalBox();
		hb11.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		hb11.add(separatorSplitViewPercentage);

		hb12 = Box.createHorizontalBox();
		hb12.add(percentage_splitTotal);
		hb12.add(Box.createHorizontalGlue());

		hb13 = Box.createHorizontalBox();
		hb13.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
		hb13.add(percentage_endTotal);
		hb13.add(Box.createHorizontalGlue());

		vb3 = Box.createVerticalBox();
		vb3.setBorder(BorderFactory.createTitledBorder(border,
			Statistiek.rb.getString("absRelLabel"), TitledBorder.CENTER,
			TitledBorder.TOP, Statistiek.font));
		vb3.add(hb2);
		vb3.add(hb3);
		if (this.model.isFrequencyPolygonMode())
			vb3.add(hb3a);
		vb3.add(hb4);
		vb3.add(hb5);
		vb3.add(hb6);
		vb3.add(hb7);
		vb3.add(hb8);
		if (this.model.isFrequencyPolygonMode())
			vb3.add(hb8a);
		else
		{
			vb3.add(hb9);
			vb3.add(hb10);
		}
		vb3.add(hb11);
		vb3.add(hb12);
		vb3.add(hb13);
		vb3.add(Box.createVerticalGlue());

		// SPLIT OPTIONS
		hb1 = Box.createHorizontalBox();
		hb1.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));
		hb1.add(splitButton);

		hb2 = Box.createHorizontalBox();
		hb2.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		hb2.add(splitVarLabel);

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
		hb0.add(Box.createHorizontalStrut(10));
		hb0.add(vb1);
		hb0.add(Box.createHorizontalStrut(10));
		hb0.add(vb2);
		hb0.add(Box.createHorizontalStrut(10));
		hb0.add(vb3);
		hb0.add(Box.createHorizontalStrut(10));
		hb0.add(vb4);
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
		//System.out.println("HistogramUserOptionsPanel.resize(): c = " + c.toString());
		Dimension d = c.getPreferredSize();
		panel.setSize(new Dimension(d.width + 10, d.height));
		panel.setPreferredSize(new Dimension(d.width + 10, d.height));
	}

	public DialogButton getDialogButton()
	{
		return dialogButton;
	}

	public int getVarBoxSelectedIndex()
	{
		return this.varBox.getSelectedIndex();
	}

	public int getSplitVarBoxSelectedIndex()
	{
		return this.splitVarBox.getSelectedIndex();
	}

	public boolean isCumulativeBoxSelected()
	{
		return this.cumulativeBox != null && this.cumulativeBox.isSelected();
	}

	public boolean isSplitSingleViewSelected()
	{
		return this.nextToEachOtherRadioItem.isSelected()
			|| aboveEachOtherRadioItem.isSelected()
			|| singleViewRadioItem.isSelected();
	}

	public boolean isNextToEachOtherSelected()
	{
		// System.out.println("HistogramUserOptionsPanel.isNextToEachOtherSelected(): nextToEachOtherRadioItem.isSelected()="
		// + this.nextToEachOtherRadioItem.isSelected());
		return this.nextToEachOtherRadioItem.isSelected();
	}
	
	public boolean isOptimizeScale()
	{
		return this.optimizeScaleBox.isSelected();
	}

	public boolean isStackModeBoxSelected()
	{
		return this.stackModeBox.isSelected();
	}

	public int getBinsBoxSelectedInt()
	{
		return ((Integer) this.binsBox.getSelectedItem()).intValue();
	}

	public int getSplitBinsBoxSelectedInt()
	{
		return ((Integer) this.splitBinsBox.getSelectedItem()).intValue();
	}

	public double getMinBoundary()
	{
		String s = this.minBoundaryField.getText();
		s = s.replace(',', '.');
		return Double.parseDouble(s);
	}

	public JCheckBox getOptimizeScaleBox()
	{
		return this.optimizeScaleBox;
	}
	
	public JTextField getMinBoundaryTextField()
	{
		return this.minBoundaryField;
	}
	
	public double getMaxOnScale()
	{
		String s = this.maxOnScaleField.getText();
		s = s.replace(',', '.');
		return Double.parseDouble(s);
	}
	
	public JTextField getMaxOnScaleField()
	{
		return this.maxOnScaleField;
	}

	public void setMinBoundary(double d)
	{
		this.minBoundaryField.setText(String.valueOf(d));
	}

	public void setMaxOnScale(double d)
	{
		this.maxOnScaleField.setText(String.valueOf(d));
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

	public double getBinWidth()
	{
		double d = 1;
		String s = this.binWidthField.getText();
		s = s.replace(',', '.');
		try
		{
			d = Double.parseDouble(s); 
		}
		catch (NumberFormatException e)
		{
			System.out.println("Klassenbreedte heeft niet het goede formaat. Cannot parse bin width " + s);
			//e.printStackTrace();
		}
		return d;
	}
	
	public JTextField getBinWidthTextField()
	{
		return this.binWidthField;
	}
	
	public void setBinWidth(double d)
	{
		this.binWidthField.setText(String.valueOf(d));
	}

	/**
	 *Set the bin width based on the model's bin boundaries. 
	 */
	public void setBinWidth()
	{
		String w;
		if (this.model.getStatTableModel().isEmptyColumn(this.model.getColumnIndex()))
		{
			w = Statistiek.getStringValue(this.model.getBinWidth());
		}
		else
		{
			this.binWidthField.setText(Statistiek.getFormattedBinWidth(this.model.getBinBoundaries()));
		}
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
		this.splitBinWidthField.setText(Statistiek.getFormattedBinWidth(this.model.getSplitOptions().getBinBoundaries()));
	}
	
	public boolean xAxisSelected()
	{
		return this.axisBox.getSelectedIndex() == 0;
	}

	public boolean percentageItemSelected()
	{
		return this.percentageRadioItem.isSelected();
	}
	
	public boolean percentageSplitTotalSelected()
	{
		return this.percentage_splitTotal.isSelected();
	}
	
	public boolean labelUnderBinSelected()
	{
		return this.labelUnderBinRadioItem.isSelected();
	}

	public boolean labelBetweenBinsSelected()
	{
		return this.labelBetweenBinsRadioItem.isSelected();
	}

	public void setModel(HistogramModel model)
	{
		this.model = model;
	}

	public void update()
	{
		//System.out.println("HistogramUserOptionsPanel.update()");

		this.optimizeScaleBox.removeActionListener(this.controller);
		this.optimizeScaleBox.setSelected(this.model.isOptimizeScale());
		this.optimizeScaleBox.addActionListener(this.controller);

		updateVarBox();

		updateSplitVarBox();

		updateBinsBox();

		updateSplitBinsBox();

		updateBinSettings();

		updateSplitBinSettings();

		if (this.model.isFrequencyPolygonMode()
			&& this.model.isFrequencyPolygonCumulativeMode() != this
				.isCumulativeBoxSelected())
		{
			this.cumulativeBox.setSelected(this.model
				.isFrequencyPolygonCumulativeMode());
		}
		
		if (this.model.getLabelUnderBin())
		{
			this.labelUnderBinRadioItem.setSelected(true);
		}
		else
		{
			this.labelBetweenBinsRadioItem.setSelected(true);
		}

		if (this.model.getPercentage())
		{
			this.percentageRadioItem.removeActionListener(controller);
			this.percentageRadioItem.setSelected(true);
			this.percentageRadioItem.addActionListener(controller);
		}
		else
		{
			this.amountRadioItem.removeActionListener(controller);
			this.amountRadioItem.setSelected(true);
			this.amountRadioItem.addActionListener(controller);
		}

		this.axisBox.removeActionListener(controller);
		if (this.model.hasVerticalBars())
		{
			this.axisBox.setSelectedIndex(0);
		}
		else
		{
			this.axisBox.setSelectedIndex(1);
		}
		this.axisBox.addActionListener(controller);

		boolean split = this.model.getSplitOptions().getColumnSplitIndex() > -1;
		this.setVisibleSplitOptions(split);
		
		this.setVisiblePercentageSettings(this.model.getPercentage() && split && !model.isSplitInSingleView());
		
		this.singleViewRadioItem.setSelected(this.model.isSplitInSingleView()
			&& split && this.model.isFrequencyPolygonMode());
		this.separateRadioItem.setSelected(!this.model.isSplitInSingleView()
			&& split);

		this.nextToEachOtherRadioItem.setSelected(this.model
			.isSplitInSingleView() && this.model.isNextToEachOther() && split);

		this.aboveEachOtherRadioItem.setSelected(this.model.isSplitInSingleView()
			&& !this.model.isNextToEachOther() && split && !this.model.isFrequencyPolygonMode());

		this.stackModeBox.setSelected(this.model.isFrequencyPolygonStackMode());
		this.stackModeBox.setEnabled(this.model.isFrequencyPolygonMode()
			&& this.model.isFrequencyPolygonCumulativeMode());
		
		this.resize(vb0);
	}

	/**
	 * Set the visibility of components related to percentage.
	 * 
	 * @param percentage
	 */
	private void setVisiblePercentageSettings(boolean b)
	{
		separatorSplitViewPercentage.getParent().setVisible(b);
		percentage_splitTotal.getParent().setVisible(b);
		percentage_endTotal.getParent().setVisible(b);
		
		percentage_splitTotal.removeActionListener(controller);
		percentage_endTotal.removeActionListener(controller);
		percentage_splitTotal.setSelected(this.model.getPercentageSplitTotal());
		percentage_endTotal.setSelected(!this.model.getPercentageSplitTotal());
		percentage_splitTotal.addActionListener(controller);
		percentage_endTotal.addActionListener(controller);
	}

	private void updateSplitBinSettings()
	{
		// check of column index valid
		if (this.model.columnIndexValid())
		{
    		if ((this.model.getSplitOptions().getColumnSplitIndex() > -1)
    			&& (this.model.getSplitOptions().getBinBoundaries() != null)) // als binBoundaries null, dan is onderstaande van de update (nog) niet nodig
    		{
    			ColumnType cSplitType = this.model.getStatTableModel().getColumnTypes()
    				.get(this.model.getSplitOptions().getColumnSplitIndex());
    			AllowedTypes splitType = cSplitType.getType();
    			if (splitType.equals(AllowedTypes.DOUBLE)
    				|| splitType.equals(AllowedTypes.INTEGER))
    			{
    				this.splitMinBoundaryField.setText(
    					Statistiek.getStringValue(this.model.getSplitOptions().getBinBoundaries().get(0)));
    				// set the split bin width based on the split bin boundaries
    				this.splitBinWidthField.setText(Statistiek.getFormattedBinWidth(this.model.getSplitOptions().getBinBoundaries()));
    				
    				StringBuilder sb = new StringBuilder();
    				for (int i = 0; i < this.model.getSplitOptions()
    					.getBinBoundaries().size() - 1; i++)
    				{
    					sb.append(Statistiek.getStringValue(this.model.getSplitOptions()
    						.getBinBoundaries().get(i)));
    					sb.append(" -< ");
    					sb.append(Statistiek.getStringValue(this.model.getSplitOptions()
    						.getBinBoundaries().get(i + 1)));
    					sb.append("\n");
    				}
    				this.splitBoundariesArea.setText(sb.toString());
    				this.splitNoObjectsLabel.setText(Statistiek.rb.getString("numberLabel")
    					+ this.model.getStatTableModel().getRowCount());
    				String splitMinValue = Statistiek.getStringValue(this.model.getStatTableModel().getColumnMin(
						this.model.getSplitOptions().getColumnSplitIndex()));
    				this.splitMinValueLabel.setText(Statistiek.rb.getString("minLabel")
    					+ splitMinValue);
    				String splitMaxValue = Statistiek.getStringValue(this.model.getStatTableModel().getColumnMax(
						this.model.getSplitOptions().getColumnSplitIndex()));
    				this.splitMaxValueLabel.setText(Statistiek.rb.getString("maxLabel")
    					+ splitMaxValue);
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
		}
	}

	private void updateBinSettings()
	{
		if (this.model.columnIndexValid())
		{
			ColumnType cType = this.model.getStatTableModel().getColumnTypes()
				.get(this.model.getColumnIndex());
			AllowedTypes type = cType.getType();
			if (type.equals(AllowedTypes.DOUBLE)
				|| type.equals(AllowedTypes.INTEGER))
			{
				if(this.view.getBinsOnScale().size()>0) // Deze check toegevoegd ivm vastloper
				{	
					this.minBoundaryField.setText(
						Statistiek.getStringValue(this.view.getBinsOnScale().get(0)));
					int last = this.view.getBinsOnScale().size() - 1;
					this.maxOnScaleField.setText(Statistiek.getStringValue(this.view.getBinsOnScale().get(last)));
				}
				// set the bin width based on the bin boundaries
				String binWidth;
				if (this.model.isOptimizeScale())
				{
					binWidth = Statistiek.getFormattedBinWidth(this.model.getBinBoundaries());
				}
				else
				{
					binWidth = Statistiek.getStringValue(this.model.getBinWidth());
				}
				this.binWidthField.setText(binWidth);
				this.noObjectsLabel.setText(Statistiek.rb
					.getString("numberLabel")
					+ this.model.getStatTableModel().getNumberOfValidDataRows(this.model.getColumnIndex()));
				String minValueString = Statistiek.getStringValue(this.model.getStatTableModel().getColumnMin(
					this.model.getColumnIndex()));
				this.minValueLabel.setText(Statistiek.rb.getString("minLabel")
					+ minValueString);
				String maxValueString = Statistiek.getStringValue(this.model.getStatTableModel().getColumnMax(
					this.model.getColumnIndex()));
				this.maxValueLabel.setText(Statistiek.rb.getString("maxLabel")
					+ maxValueString);
				this.separator1.getParent().setVisible(true);
				this.separatorAmountLabel.getParent().setVisible(true);
				this.labelBetweenBinsRadioItem.getParent().setVisible(true);
				this.labelUnderBinRadioItem.getParent().setVisible(true);
				this.binsBox.getParent().setVisible(true);
				this.binsLabel.getParent().setVisible(true);
				this.optimizeScaleBox.getParent().setVisible(true);
				setEnumClasses(false);
			}
			else if (type.equals(AllowedTypes.ENUM) || type.equals(AllowedTypes.STRING))
			{
				this.separator1.getParent().setVisible(false);
				this.separatorAmountLabel.getParent().setVisible(false);
				this.labelBetweenBinsRadioItem.getParent().setVisible(false);
				this.labelUnderBinRadioItem.getParent().setVisible(false);
				this.binsBox.getParent().setVisible(false);
				this.binsLabel.getParent().setVisible(false);
				this.optimizeScaleBox.getParent().setVisible(false);
				setEnumClasses(true);
			}
		}
	}

	private void updateSplitBinsBox()
	{
		this.splitBinsBox.removeActionListener(this.controller);
		Integer nr = null;
		if (this.model.getSplitOptions().getBinBoundaries() != null)
		{
			nr = new Integer(this.model.getSplitOptions().getBinBoundaries().size() - 1);
		}
		else
		{
			nr = new Integer(2);
		}
		this.splitBinsBox.setSelectedItem(nr);
		this.splitBinsBox.addActionListener(this.controller);
	}

	private void updateBinsBox()
	{
		this.binsBox.removeActionListener(this.controller);
		this.binsBox.setSelectedItem(new Integer(this.model.getNoBins()));
		this.binsBox.addActionListener(this.controller);
	}

	private void updateSplitVarBox()
	{
		this.splitVarBox.removeActionListener(this);
		this.splitVarBox.removeAllItems();
		this.splitVarBox.addItem(Statistiek.rb.getString("chooseItem"));
		for (int column = 0; column < this.model.getStatTableModel()
			.getColumnCount(); column++)
		{
			splitVarBox.addItem(this.model.getStatTableModel()
				.getColumnName(column));
		}
		
		// check of columnindex valid
		if (this.model.columnIndexValid())
		{
			this.splitVarBox.setSelectedIndex(this.model.getSplitOptions()
				.getColumnSplitIndex() + 1);
		}
		else
		{
			// set no split variable selected
			this.splitVarBox.setSelectedIndex(0);
		}
	
		this.splitVarBox.addActionListener(this);
	}

	private void updateVarBox()
	{
		this.varBox.removeActionListener(this.controller);
		this.varBox.removeAllItems();
		for (String varName : this.model.getStatTableModel().getColumnNames())
		{
			this.varBox.addItem(varName);
		}
		if (this.model.columnIndexValid())
		{
			this.varBox.setSelectedIndex(this.model.getColumnIndex());
		}
		else
		{
			// set no item selected
			this.varBox.setSelectedIndex(-1);
		}
		this.varBox.addActionListener(this.controller);
	}

	public void init()
	{
		setVisibleBoundaryOptions();
		setVisibleSplitBoundaryOptions(false);
		
		if (vb0 != null)
			resize(vb0);
	}

	private void setEnumClasses(boolean b)
	{
		enumClasses = b;
		
		// vertical box containing bin settings not visible for enum variable 
		binWidthLabel.getParent().getParent().setVisible(!b);

		// set visibility of the components on the vertical box
		separatorAmountLabel.getParent().setVisible(!b);
		minBoundaryLabel.getParent().setVisible(!b);
		minBoundaryField.getParent().setVisible(!b);
		maxOnScaleLabel.getParent().setVisible(!b && !isOptimizeScale());
		maxOnScaleField.getParent().setVisible(!b && !isOptimizeScale());
		binWidthLabel.getParent().setVisible(!b);
		binWidthField.getParent().setVisible(!b);
		
		binsLabel.getParent().setVisible(!b && isOptimizeScale());
		binsBox.getParent().setVisible(!b && isOptimizeScale());
		
		noObjectsLabel.getParent().setVisible(!b);
		minValueLabel.getParent().setVisible(!b);
		maxValueLabel.getParent().setVisible(!b);
	}

	private void setSplitEnumClasses(boolean b)
	{
		splitEnumClasses = b;
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
	}

	private void setVisibleBoundaryOptions()
	{
		separator1.getParent().setVisible(!enumClasses);
		optimizeScaleBox.getParent().setVisible(!enumClasses);
		minBoundaryLabel.getParent().setVisible(!enumClasses);
		minBoundaryField.getParent().setVisible(!enumClasses);
		maxOnScaleLabel.getParent().setVisible(!enumClasses && !isOptimizeScale());
		maxOnScaleField.getParent().setVisible(!enumClasses && !isOptimizeScale());
		binWidthLabel.getParent().setVisible(!enumClasses);
		binWidthField.getParent().setVisible(!enumClasses);
		noObjectsLabel.getParent().setVisible(!enumClasses);
		minValueLabel.getParent().setVisible(!enumClasses);
		maxValueLabel.getParent().setVisible(!enumClasses);
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
		separatorLabelSplitView.getParent().setVisible(b);
		nextToEachOtherRadioItem.getParent().setVisible(b);
		aboveEachOtherRadioItem.getParent().setVisible(b);
		separateRadioItem.getParent().setVisible(b);
		singleViewRadioItem.getParent().setVisible(b);

		splitVarLabel.getParent().setVisible(b);
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
//		 System.out.println("HistogramUserOptionsPanel.actionPerformed(): e.getActionCommand()="
//			 + e.getActionCommand());

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
				setVisiblePercentageSettings(false);
				this.clearGUISplitComponents();
			}
			else
			{
				setVisibleSplitOptions(true);
				setVisiblePercentageSettings(model.getPercentage() && !model.isSplitInSingleView());
			}
			resize(vb0);
		}
		else if (e.getSource() == okButton)
		{
			setVisibleBoundaryOptions();
			setVisibleSplitBoundaryOptions(false);
			if (splitVarBox.getSelectedIndex() == 0)
				setVisibleSplitOptions(false);
			dialogButton.closeDialog();
		}
		else if (e.getSource() == splitVarBox)
		{
			//System.out.println("HistogramUserOptionsPanel.actionPerformed(): splitVarBox, SplitColumnUpdate!");
			if (this.view.getSplitVarBoxSelectedIndex() - 1 != this.model
				.getSplitOptions().getColumnSplitIndex())
			{
				if (this.view.getSplitVarBoxSelectedIndex() != -1)
					this.model.setColumnSplitIndex(this.view
						.getSplitVarBoxSelectedIndex() - 1);
				this.model.setSplitOptions(this.model.getSplitOptions());
				if (this.view.getSplitVarBoxSelectedIndex() > 0)
				{
					this.controller.setSplitType(this.model
						.getStatTableModel()
						.getColumnTypes()
						.get(this.model.getSplitOptions().getColumnSplitIndex())
						.getType());
				}
			}
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
	
	/**
	 * Clear all split GUI components, i.e. 
	 * split variable, bin settings and labels with information 
	 * about number of objects and minimum and maximum values.
	 */
	private void clearGUISplitComponents()
	{
		//System.out.println("HistogramUserOptionsPanel.clearGUISplitComponents()");
		this.splitVarBox.setSelectedIndex(0);
		this.splitBinWidthField.setText("");
		this.splitMinBoundaryField.setText("");
		this.splitBoundariesArea.setText("");
		this.splitNoObjectsLabel.setText("");
		this.splitMinValueLabel.setText("");
		this.splitMaxValueLabel.setText("");
	}
}
