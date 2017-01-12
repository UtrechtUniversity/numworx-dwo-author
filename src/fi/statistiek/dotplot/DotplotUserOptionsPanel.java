package fi.statistiek.dotplot;

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

import fi.statistiek.ColorPreviewer;
import fi.statistiek.DialogButton;
import fi.statistiek.Statistiek;
import fi.statistiek.types.AllowedTypes;
import fi.statistiek.types.ColumnType;

public class DotplotUserOptionsPanel extends JPanel implements ActionListener, FocusListener
{

	private DotplotView view;
	private DotplotController controller;
	private DotplotModel model;

	private DialogButton dialogButton;

	private JPanel panel;
	private Box vb0;

	// variable settings
	private JLabel varXLabel;
	private JComboBox varXBox;
	private JLabel varYLabel;
	private JComboBox varYBox;

	// display settings
	private JLabel absRelLabel;
	/**
	 * Fields for setting the scale of the X variable
	 */
	private JCheckBox optimizeScaleXBox;
	private JLabel minXOnScaleLabel;
	private JTextField minXOnScaleField;
	private JLabel maxXOnScaleLabel;
	private JTextField maxXOnScaleField;
//	private JLabel noObjectsLabel;
	private JLabel minValueXLabel;
	private JLabel maxValueXLabel;
	/**
	 * Fields for setting the scale of the Y variable
	 */
//	private JCheckBox optimizeScaleYBox;
//	private JLabel minYOnScaleLabel;
//	private JTextField minYOnScaleField;
//	private JLabel maxYOnScaleLabel;
//	private JTextField maxYOnScaleField;
//	private JLabel minValueYLabel;
//	private JLabel maxValueYLabel;
	private JCheckBox useColorScaleBox;
	private JCheckBox showCorrelationBox;
	private JLabel varColorLabel;
	private JComboBox varColorBox;
	private ColorPreviewer colorPreviewPanel;
	private JSeparator separatorColorScale_splitOptions;
	private JSeparator separatorSplitOptions_correlation;
	private JRadioButton singleViewRadioItem;
	private JRadioButton separateRadioItem;

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

	// private JCheckBox splitSingleViewBox;

	private JButton okButton;
	private JButton cancelButton;

	private boolean BoundariesVisible;
	private boolean splitBoundariesVisible;
	private boolean splitOptionsVisible;
	private boolean enumClasses;
	private boolean splitEnumClasses;

	private ArrayList<Double> Boundaries;

	public DotplotUserOptionsPanel(DotplotView view,
		DotplotController controller, DotplotModel model)
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

		// var settings
		this.varXLabel = new JLabel(Statistiek.rb.getString("variableXLabel"));
		this.varXLabel.setFont(Statistiek.font);

		//this.varXBox = new JComboBox();
		// error in swing: JComboBox is misbehaving (the same as JTextField) in reporting an unbounded max height
		// see also: http://stackoverflow.com/questions/7581846/swing-boxlayout-problem-with-jcombobox-without-using-setxxxsize/7582033#7582033
		// Solution: subclass and return a reasonable height
		this.varXBox = new JComboBox<String>() {
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
        
		this.varXBox.setFont(Statistiek.font);
		this.varXBox.setPreferredSize(new Dimension(100, 25));
		this.varXBox.setActionCommand("varXBox");
		this.varXBox.addActionListener(this.controller);

		if (this.model.isScatterplotMode())
		{
			this.varYLabel = new JLabel(Statistiek.rb.getString("variableYLabel"));
			this.varYLabel.setFont(Statistiek.font);
	
			//this.varYBox = new JComboBox();
			// error in swing: JComboBox is misbehaving (the same as JTextField) in reporting an unbounded max height
			// see also: http://stackoverflow.com/questions/7581846/swing-boxlayout-problem-with-jcombobox-without-using-setxxxsize/7582033#7582033
			// Solution: subclass and return a reasonable height
			this.varYBox = new JComboBox<String>() {
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

			this.varYBox.setFont(Statistiek.font);
			this.varYBox.setPreferredSize(new Dimension(100, 25));
			this.varYBox.setActionCommand("varYBox");
			this.varYBox.addActionListener(this.controller);
		}

		// display settings
		this.absRelLabel = new JLabel(Statistiek.rb.getString("absRelLabel"));
		this.absRelLabel.setFont(Statistiek.font);

		// fields for setting the scale of the X variable
		this.optimizeScaleXBox = new JCheckBox(
			Statistiek.rb.getString("optimizeScaleBox"), true);
		this.optimizeScaleXBox.setFont(Statistiek.font);
		this.optimizeScaleXBox.setOpaque(false);
		this.optimizeScaleXBox.setActionCommand("optimizeScaleXBox");
		this.optimizeScaleXBox.addActionListener(this);
		
		this.minXOnScaleLabel = new JLabel(
			Statistiek.rb.getString("minValueLabel"));
		this.minXOnScaleLabel.setFont(Statistiek.font);

		this.minXOnScaleField = new JTextField();
		this.minXOnScaleField.setMaximumSize(new Dimension(40, 25));
		this.minXOnScaleField.setMinimumSize(new Dimension(40, 25));
		this.minXOnScaleField.setPreferredSize(new Dimension(40, 25));
		this.minXOnScaleField.setActionCommand("minOnScale");
		this.minXOnScaleField.addActionListener(this);
		this.minXOnScaleField.addFocusListener(this);

		this.maxXOnScaleLabel = new JLabel(
			Statistiek.rb.getString("maxValueLabel"));
		this.maxXOnScaleLabel.setFont(Statistiek.font);

		this.maxXOnScaleField = new JTextField();
		this.maxXOnScaleField.setMaximumSize(new Dimension(40, 25));
		this.maxXOnScaleField.setMinimumSize(new Dimension(40, 25));
		this.maxXOnScaleField.setPreferredSize(new Dimension(40, 25));
		this.maxXOnScaleField.setActionCommand("maxOnScale");
		this.maxXOnScaleField.addActionListener(this);
		this.maxXOnScaleField.addFocusListener(this);

		this.minValueXLabel = new JLabel("");
		this.minValueXLabel.setFont(Statistiek.font);

		this.maxValueXLabel = new JLabel("");
		this.maxValueXLabel.setFont(Statistiek.font);

		// fields for setting the scale of the Y variable
//		this.optimizeScaleYBox = new JCheckBox(
//			Statistiek.rb.getString("optimizeScaleYBox"), true);
//		this.optimizeScaleYBox.setFont(Statistiek.font);
//		this.optimizeScaleYBox.setOpaque(false);
//		this.optimizeScaleYBox.setActionCommand("optimizeScaleYBox");
//		this.optimizeScaleYBox.addActionListener(this);
//		
//		this.minYOnScaleLabel = new JLabel(
//			Statistiek.rb.getString("minValueLabel"));
//		this.minYOnScaleLabel.setFont(Statistiek.font);
//
//		this.minYOnScaleField = new JTextField();
//		this.minYOnScaleField.setMaximumSize(new Dimension(40, 25));
//		this.minYOnScaleField.setMinimumSize(new Dimension(40, 25));
//		this.minYOnScaleField.setPreferredSize(new Dimension(40, 25));
//		this.minYOnScaleField.setActionCommand("minOnScale");
//		this.minYOnScaleField.addActionListener(this);
//		this.minYOnScaleField.addFocusListener(this);
//
//		this.maxYOnScaleLabel = new JLabel(
//			Statistiek.rb.getString("maxValueLabel"));
//		this.maxYOnScaleLabel.setFont(Statistiek.font);
//
//		this.maxYOnScaleField = new JTextField();
//		this.maxYOnScaleField.setMaximumSize(new Dimension(40, 25));
//		this.maxYOnScaleField.setMinimumSize(new Dimension(40, 25));
//		this.maxYOnScaleField.setPreferredSize(new Dimension(40, 25));
//		this.maxYOnScaleField.setActionCommand("maxOnScale");
//		this.maxYOnScaleField.addActionListener(this);
//		this.maxYOnScaleField.addFocusListener(this);
//
//		this.minValueYLabel = new JLabel("");
//		this.minValueYLabel.setFont(Statistiek.font);
//
//		this.maxValueYLabel = new JLabel("");
//		this.maxValueYLabel.setFont(Statistiek.font);

		this.useColorScaleBox = new JCheckBox(
			Statistiek.rb.getString("usecolorscaleCheckbox"));
		this.useColorScaleBox.setFont(Statistiek.font);
		this.useColorScaleBox.setOpaque(false);
		this.useColorScaleBox.setActionCommand("useColorScaleBox");
		this.useColorScaleBox.addActionListener(this);

		this.varColorLabel = new JLabel(
			Statistiek.rb.getString("variablecolorscaleLabel"));
		this.varColorLabel.setFont(Statistiek.font);

		this.varColorBox = new JComboBox();
		this.varColorBox.setFont(Statistiek.font);
		this.varColorBox.setPreferredSize(new Dimension(100, 25));
		this.varColorBox.setMaximumSize(new Dimension(100, 25));
		this.varColorBox.setActionCommand("varColorBox");
		this.varColorBox.addActionListener(this.controller);

		this.showCorrelationBox = new JCheckBox(
			Statistiek.rb.getString("showcorrelationCheckbox"), false);
		this.showCorrelationBox.setOpaque(false);
		this.showCorrelationBox.setFont(Statistiek.font);
		this.showCorrelationBox.setActionCommand("showCorrelationBox");
		this.showCorrelationBox.addActionListener(this.controller);

		this.colorPreviewPanel = new ColorPreviewer(this.model.getColorA(),
			this.model.getColorB());
		this.colorPreviewPanel.addMouseListener(this.controller);
		this.colorPreviewPanel.setPreferredSize(new Dimension(100, 25));
		this.colorPreviewPanel.setMaximumSize(new Dimension(100, 25));

		this.separatorColorScale_splitOptions = new JSeparator();
		this.separatorColorScale_splitOptions.setBorder(BorderFactory
			.createEtchedBorder(EtchedBorder.LOWERED));
		this.separatorColorScale_splitOptions.setMaximumSize(new Dimension(140, 3));

		this.singleViewRadioItem = new JRadioButton(
			Statistiek.rb.getString("splitsingleviewCheckBox"), false);
		this.singleViewRadioItem.setFont(Statistiek.font);
		this.singleViewRadioItem.setOpaque(false);
		this.singleViewRadioItem.setActionCommand("splitSingleView");
		this.singleViewRadioItem.addActionListener(this.controller);

		this.separateRadioItem = new JRadioButton(
			Statistiek.rb.getString("separateFromEachOtherRadioItem"), false);
		this.separateRadioItem.setFont(Statistiek.font);
		this.separateRadioItem.setOpaque(false);
		this.separateRadioItem.setActionCommand("separateFromEachOther");
		this.separateRadioItem.addActionListener(this.controller);

		ButtonGroup buttonGroup2 = new ButtonGroup();
		buttonGroup2.add(this.singleViewRadioItem);
		buttonGroup2.add(this.separateRadioItem);

		this.separatorSplitOptions_correlation = new JSeparator();
		this.separatorSplitOptions_correlation.setBorder(BorderFactory
			.createEtchedBorder(EtchedBorder.LOWERED));
		this.separatorSplitOptions_correlation.setMaximumSize(new Dimension(140, 3));

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
				DotplotUserOptionsPanel.this.controller.processSplitMinBoundaryChanged();
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
				DotplotUserOptionsPanel.this.controller.processSplitBinWidthChanged();
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

		// ok-cancel
		this.okButton = new JButton("OK");
		this.okButton.addActionListener(this);
	}

	private void layoutGuiComponents()
	{
		// Variable
		Box hb1, hb2, hb3 = null, hb4 = null, hb5, hb6, hb7, hb8, hb9, hb10, hb11, 
			hb12, hb13, hb14, hb15, hb16, hb17, hb18, hb19;
		Box vb1, vb2, vb3, vb4, vb5, vb6, vb7;

		hb1 = Box.createHorizontalBox();
		hb1.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		hb1.add(varXLabel);

		hb2 = Box.createHorizontalBox();
		hb2.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));
		hb2.add(varXBox);

		if (this.model.isScatterplotMode())
		{
			hb3 = Box.createHorizontalBox();
			hb3.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
			hb3.add(varYLabel);
	
			hb4 = Box.createHorizontalBox();
			hb4.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			hb4.add(varYBox);
		}

		vb1 = Box.createVerticalBox();
		Border border = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		vb1.setBorder(BorderFactory.createTitledBorder(border,
			Statistiek.rb.getString("variableLabel"), TitledBorder.CENTER,
			TitledBorder.TOP, Statistiek.font));
		vb1.add(hb1);
		vb1.add(hb2);
		if (this.model.isScatterplotMode())
		{
			vb1.add(hb3);
			vb1.add(hb4);
		}
		vb1.add(Box.createVerticalGlue());

		// Bins

		// vb2 = Box.createVerticalBox();
		// vb2.setBorder(BorderFactory.createTitledBorder(border,
		// Statistiek.rb.getString("classDivisionLabel"), TitledBorder.CENTER,
		// TitledBorder.TOP, Statistiek.font));
		// vb2.add(Box.createVerticalGlue());

		// Display

		hb1 = Box.createHorizontalBox();
		hb1.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		hb1.add(optimizeScaleXBox);

		hb2 = Box.createHorizontalBox();
		hb2.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		hb2.add(this.minXOnScaleLabel);
		hb2.add(Box.createHorizontalStrut(5));
		hb2.add(Box.createHorizontalGlue());
		hb2.add(this.minXOnScaleField);
		// set maximum size prevents hb filling up vertical space
		// and makes hb adjust its height to the content
		hb2.setMaximumSize(new Dimension(250, 200));

		hb3 = Box.createHorizontalBox();
		hb3.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		hb3.add(this.maxXOnScaleLabel);
		hb3.add(Box.createHorizontalStrut(5));
		hb3.add(Box.createHorizontalGlue());
		hb3.add(this.maxXOnScaleField);
		// set maximum size prevents hb filling up vertical space
		// and makes hb adjust its height to the content
		hb3.setMaximumSize(new Dimension(250, 200));

		hb4 = Box.createHorizontalBox();
		hb4.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		hb4.add(this.minValueXLabel);
		hb4.add(Box.createHorizontalGlue());

		hb5 = Box.createHorizontalBox();
		hb5.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		hb5.add(this.maxValueXLabel);
		hb5.add(Box.createHorizontalGlue());
		
//		hb6 = Box.createHorizontalBox();
//		hb6.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
//		hb6.add(optimizeScaleYBox);
//
//		hb7 = Box.createHorizontalBox();
//		hb7.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
//		hb7.add(this.minYOnScaleLabel);
//		hb7.add(Box.createHorizontalStrut(5));
//		hb7.add(Box.createHorizontalGlue());
//		hb7.add(this.minYOnScaleField);
//		// set maximum size prevents hb filling up vertical space
//		// and makes hb adjust its height to the content
//		hb7.setMaximumSize(new Dimension(250, 200));
//
//		hb8 = Box.createHorizontalBox();
//		hb8.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
//		hb8.add(this.maxYOnScaleLabel);
//		hb8.add(Box.createHorizontalStrut(5));
//		hb8.add(Box.createHorizontalGlue());
//		hb8.add(this.maxYOnScaleField);
//		// set maximum size prevents hb filling up vertical space
//		// and makes hb adjust its height to the content
//		hb8.setMaximumSize(new Dimension(250, 200));
//
//		hb9 = Box.createHorizontalBox();
//		hb9.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
//		hb9.add(this.minValueYLabel);
//		hb9.add(Box.createHorizontalGlue());
//
//		hb10 = Box.createHorizontalBox();
//		hb10.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
//		hb10.add(this.maxValueYLabel);
//		hb10.add(Box.createHorizontalGlue());
		
		hb11 = Box.createHorizontalBox();
		hb11.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		hb11.add(useColorScaleBox);
		hb11.add(Box.createHorizontalGlue());

		hb12 = Box.createHorizontalBox();
		hb12.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		hb12.add(varColorLabel);

		hb13 = Box.createHorizontalBox();
		hb13.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		hb13.add(varColorBox);

		hb14 = Box.createHorizontalBox();
		hb14.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		hb14.add(colorPreviewPanel);

		hb15 = Box.createHorizontalBox();
		hb15.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		hb15.add(separatorColorScale_splitOptions);

		hb16 = Box.createHorizontalBox();
		hb16.add(separateRadioItem);
		hb16.add(Box.createHorizontalGlue());

		hb17 = Box.createHorizontalBox();
		hb17.add(singleViewRadioItem);
		hb17.add(Box.createHorizontalGlue());

		hb18 = Box.createHorizontalBox();
		hb18.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
		hb18.add(separatorSplitOptions_correlation);

		hb19 = Box.createHorizontalBox();
		hb19.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		hb19.add(showCorrelationBox);
		hb19.add(Box.createHorizontalGlue());

		vb3 = Box.createVerticalBox();
		vb3.setBorder(BorderFactory.createTitledBorder(border,
			Statistiek.rb.getString("absRelLabel"), TitledBorder.CENTER,
			TitledBorder.TOP, Statistiek.font));
		vb3.add(hb1);
		vb3.add(hb2);
		vb3.add(hb3);
		vb3.add(hb4);
		vb3.add(hb5);
//		vb3.add(hb6);
//		vb3.add(hb7);
//		vb3.add(hb8);
//		vb3.add(hb9);
//		vb3.add(hb10);
		vb3.add(hb11);
		vb3.add(hb12);
		vb3.add(hb13);
		vb3.add(hb14);
		vb3.add(hb15);
		vb3.add(hb16);
		vb3.add(hb17);
		vb3.add(hb18);
		vb3.add(hb19);
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

		Box hb5a = Box.createHorizontalBox();
		hb5a.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		hb5a.add(separator3);

		hb6 = Box.createHorizontalBox();
		hb6.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		hb6.add(splitChooseBoundariesButton);

		hb7 = Box.createHorizontalBox();
		hb7.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		hb7.add(this.splitMinBoundaryLabel);
		hb7.add(Box.createHorizontalStrut(5));
		hb7.add(Box.createHorizontalGlue());
		hb7.add(this.splitMinBoundaryField);

		hb8 = Box.createHorizontalBox();
		hb8.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		hb8.add(this.splitBinWidthLabel);
		hb8.add(Box.createHorizontalStrut(5));
		hb8.add(Box.createHorizontalGlue());
		hb8.add(this.splitBinWidthField);

		hb9 = Box.createHorizontalBox();
		hb9.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		hb9.add(this.splitBoundariesLabel);

		hb10 = Box.createHorizontalBox();
		hb10.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		hb10.add(this.splitBoundariesAreaScrollPane);

		hb11 = Box.createHorizontalBox();
		hb11.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		hb11.add(this.splitNoObjectsLabel);
		hb11.add(Box.createHorizontalGlue());

		hb12 = Box.createHorizontalBox();
		hb12.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		hb12.add(this.splitMinValueLabel);
		hb12.add(Box.createHorizontalGlue());

		hb13 = Box.createHorizontalBox();
		hb13.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		hb13.add(this.splitMaxValueLabel);
		hb13.add(Box.createHorizontalGlue());

		vb4 = Box.createVerticalBox();
		// vb4.setPreferredSize(new Dimension(140,10));
		vb4.setBorder(BorderFactory.createTitledBorder(border,
			Statistiek.rb.getString("splitsLabel"), TitledBorder.CENTER,
			TitledBorder.TOP, Statistiek.font));

		vb4.add(hb1);
		vb4.add(hb2);
		vb4.add(hb3);
		vb4.add(hb4);
		vb4.add(hb5);
		vb4.add(hb5a);
		vb4.add(hb6);
		vb4.add(hb7);
		vb4.add(hb8);
		vb4.add(hb9);
		vb4.add(hb10);
		vb4.add(hb11);
		vb4.add(hb12);
		vb4.add(hb13);
		vb4.add(Box.createVerticalGlue());

		Box hb0 = Box.createHorizontalBox();
		// hb0.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		hb0.add(Box.createHorizontalStrut(10));
		hb0.add(vb1);
		hb0.add(Box.createHorizontalStrut(10));
		// hb0.add(vb2);
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

	public int getVarXBoxSelectedIndex()
	{
		return this.varXBox.getSelectedIndex();
	}

	public int getVarYBoxSelectedIndex()
	{
		return this.varYBox.getSelectedIndex();
	}

	public int getVarColorBoxSelectedIndex()
	{
		return this.varColorBox.getSelectedIndex();
	}

	public int getSplitVarBoxSelectedIndex()
	{
		return this.splitVarBox.getSelectedIndex();
	}

	public boolean isUseColorScaleBoxSelected()
	{
		return this.useColorScaleBox.isSelected();
	}

	public boolean isShowCorrelationBoxSelected()
	{
		return this.showCorrelationBox.isSelected();
	}

	public boolean isSplitSingleViewSelected()
	{
		return this.singleViewRadioItem.isSelected();
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
	
	public void setModel(DotplotModel model)
	{
		this.model = model;
	}

	public void update()
	{
		updateOptimizeScaleXSettings();
		setVisibleOptimizeScale();

		updateVarXBox();

		if (this.model.isScatterplotMode())
		{
			updateVarYBox();
		} // scatterplot mode

		updateVarColorBox();

		updateSplitVarBox();

		updateSplitBinsBox();

		updateSplitBinSettings();

		boolean correlatieMogelijk = this.model.getColumnXIndex() > -1
			&& this.model.getColumnYIndex() > -1;
		this.showCorrelationBox.getParent().setVisible(correlatieMogelijk);
		this.separatorSplitOptions_correlation.getParent().setVisible(correlatieMogelijk);

		boolean colorScale;
		if (this.isSplit())
			// voor split geen colorScale tonen
			colorScale = false;
		else
			colorScale = this.model.isUseColorScale();
		
		setColorOptionsVisible(colorScale);

		// als geen splitvariabele is gekozen, verdwijnt de split dialoog...
		// onderstaande regel moet dus weg
		// this.setVisibleSplitOptions(split);

		this.separateRadioItem.setSelected(!this.model.splitInSingleView());
		this.singleViewRadioItem.setSelected(this.model.splitInSingleView());
		this.separateRadioItem.getParent().setVisible(this.isSplit());
		this.separateRadioItem.setVisible(this.isSplit());
		this.singleViewRadioItem.getParent().setVisible(this.isSplit());
		this.singleViewRadioItem.setVisible(this.isSplit());

		if (this.model.isScatterplotMode() 
			&& this.view.getXType().isNumber() && this.view.getYType().isNumber()
			&& !isSplit())
		{
			this.enableCorrelationCheckBox(true);
			this.showCorrelationBox.setSelected(this.model.isShowCorrelation());
		}
		else
		{
			// uncheck
			this.showCorrelationBox.setSelected(false);
			// and disable
			this.enableCorrelationCheckBox(false);
		}
		
		resize(vb0);
	}

	/**
	 * The optimize scale option is only avaliable for dotplots,
	 * not for scatterplots.
	 */
	private void setVisibleOptimizeScale()
	{
		if (this.model.isScatterplotMode())
		{
			// show none
			this.optimizeScaleXBox.getParent().setVisible(false);
			setVisibleOptimizeScaleXSettings(false);
		}
		else
		{
			if (this.model.columnXIndexValid())
			{
				ColumnType cType = this.model.getStatTableModel().getColumnTypes()
					.get(this.model.getColumnXIndex());
				AllowedTypes type = cType.getType();
				if (type.equals(AllowedTypes.DOUBLE)
					|| type.equals(AllowedTypes.INTEGER))
				{
					// show optimize
					this.optimizeScaleXBox.getParent().setVisible(true);
					
					if (isOptimizeScaleX())
					{
						// hide rest
						setVisibleOptimizeScaleXSettings(false);
					}
					else
					{
						// show rest
						setVisibleOptimizeScaleXSettings(true);						
					}
				}
				else
				{
					// show none
					this.optimizeScaleXBox.getParent().setVisible(false);
					setVisibleOptimizeScaleXSettings(false);
				}
			}
		}
	}

	private void setVisibleOptimizeScaleXSettings(boolean isVisible)
	{
		minXOnScaleLabel.getParent().setVisible(isVisible);
		minXOnScaleField.getParent().setVisible(isVisible);
		maxXOnScaleLabel.getParent().setVisible(isVisible);
		maxXOnScaleField.getParent().setVisible(isVisible);
		minValueXLabel.getParent().setVisible(isVisible);
		maxValueXLabel.getParent().setVisible(isVisible);
	}

	private void setVisibleOptimizeScaleYSettings(boolean isVisible)
	{
//		minYOnScaleLabel.getParent().setVisible(isVisible);
//		minYOnScaleField.getParent().setVisible(isVisible);
//		maxYOnScaleLabel.getParent().setVisible(isVisible);
//		maxYOnScaleField.getParent().setVisible(isVisible);
//		minValueYLabel.getParent().setVisible(isVisible);
//		maxValueYLabel.getParent().setVisible(isVisible);
	}

	private void updateOptimizeScaleXSettings()
	{
		this.optimizeScaleXBox.removeActionListener(this);
		this.optimizeScaleXBox.setSelected(this.model.isOptimizeScaleX());
		this.optimizeScaleXBox.addActionListener(this);
		
		if (this.model.columnXIndexValid())
		{
			ColumnType cType = this.model.getStatTableModel().getColumnTypes()
				.get(this.model.getColumnXIndex());
			AllowedTypes type = cType.getType();
			if (type.equals(AllowedTypes.DOUBLE)
				|| type.equals(AllowedTypes.INTEGER))
			{
				this.minXOnScaleField.setText(
					Statistiek.getStringValue(this.model.getMinXOnScale()));
				this.maxXOnScaleField.setText(Statistiek.getStringValue(this.model.getMaxXOnScale()));
				String minValueXString = Statistiek.getStringValue(this.model.getStatTableModel().getColumnMin(
					this.model.getColumnXIndex()));
				this.minValueXLabel.setText(Statistiek.rb.getString("minLabel")
					+ minValueXString);
				String maxValueXString = Statistiek.getStringValue(this.model.getStatTableModel().getColumnMax(
					this.model.getColumnXIndex()));
				this.maxValueXLabel.setText(Statistiek.rb.getString("maxLabel")
					+ maxValueXString);
			}
		}
	}

	private void updateOptimizeScaleYSettings()
	{
//		this.optimizeScaleYBox.removeActionListener(this);
//		this.optimizeScaleYBox.setSelected(this.model.isOptimizeScaleY());
//		this.optimizeScaleYBox.addActionListener(this);
//		
//		if (this.model.columnYIndexValid())
//		{
//			ColumnType cType = this.model.getStatTableModel().getColumnTypes()
//				.get(this.model.getColumnYIndex());
//			AllowedTypes type = cType.getType();
//			if (type.equals(AllowedTypes.DOUBLE)
//				|| type.equals(AllowedTypes.INTEGER))
//			{
//				this.minYOnScaleField.setText(
//					Statistiek.getStringValue(this.model.getMinYOnScale()));
//				this.maxYOnScaleField.setText(Statistiek.getStringValue(this.model.getMaxYOnScale()));
//				String minValueYString = Statistiek.getStringValue(this.model.getStatTableModel().getColumnMin(
//					this.model.getColumnYIndex()));
//				this.minValueXLabel.setText(Statistiek.rb.getString("minLabel")
//					+ minValueYString);
//				String maxValueYString = Statistiek.getStringValue(this.model.getStatTableModel().getColumnMax(
//					this.model.getColumnYIndex()));
//				this.maxValueXLabel.setText(Statistiek.rb.getString("maxLabel")
//					+ maxValueYString);
//			}
//		}
	}

	private void updateSplitBinSettings()
	{
		if (this.model.getSplitOptions().getColumnSplitIndex() > -1)
		{
			ColumnType cSplitType = this.model.getStatTableModel().getColumnTypes()
				.get(this.model.getSplitOptions().getColumnSplitIndex());
			AllowedTypes splitType = cSplitType.getType();
			if (splitType.equals(AllowedTypes.DOUBLE)
				|| splitType.equals(AllowedTypes.INTEGER))
			{
				this.splitMinBoundaryField.setText(
					Statistiek.getStringValue(this.model.getSplitOptions().getBinBoundaries().get(0).doubleValue()));
				this.splitBinWidthField.setText(Statistiek.getFormattedBinWidth(this.model.getSplitBinBoundaries()));
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < this.model.getSplitOptions()
					.getBinBoundaries().size() - 1; i++)
				{
					sb.append(
						Statistiek.getStringValue(this.model.getSplitOptions().getBinBoundaries().get(i).doubleValue()));
					sb.append(" -< ");
					sb.append(
						Statistiek.getStringValue(this.model.getSplitOptions().getBinBoundaries().get(i + 1).doubleValue()));
					sb.append("\n");
				}
				this.splitBoundariesArea.setText(sb.toString());
				this.splitNoObjectsLabel.setText(Statistiek.rb.getString("numberLabel")
					+ this.model.getStatTableModel().getRowCount());
				String splitMinValue = Statistiek.getStringValue(
					this.model.getStatTableModel().getColumnMin(this.model.getSplitOptions().getColumnSplitIndex()));
				this.splitMinValueLabel.setText(Statistiek.rb.getString("minLabel")
					+ splitMinValue);
				String splitMaxValue = Statistiek.getStringValue(
					this.model.getStatTableModel().getColumnMax(this.model.getSplitOptions().getColumnSplitIndex()));
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
		} // split
	}

	private void updateSplitBinsBox()
	{
		this.splitBinsBox.removeActionListener(this.controller);
		this.splitBinsBox.setSelectedItem(new Integer(this.model
			.getSplitOptions().getBinBoundaries().size() - 1));
		this.splitBinsBox.addActionListener(this.controller);
	}

	private void updateSplitVarBox()
	{
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
	}

	private void updateVarColorBox()
	{
		this.varColorBox.removeActionListener(this.controller);
		this.varColorBox.removeAllItems();
		for (String varName : this.model.getStatTableModel().getColumnNames())
		{
			this.varColorBox.addItem(varName);
		}
		if (this.model.columnColorIndexValid())
		{
			this.varColorBox.setSelectedIndex(this.model.getColumnColorIndex());
		}
		else
		{
			// set no item selected
			this.varColorBox.setSelectedIndex(-1);
		}
		this.varColorBox.addActionListener(this.controller);
	}

	private void updateVarYBox()
	{
		this.varYBox.removeActionListener(this.controller);
		this.varYBox.removeAllItems();
		for (String varName : this.model.getStatTableModel().getColumnNames())
		{
			this.varYBox.addItem(varName);
		}
		if (this.model.columnYIndexValid())
		{
			this.varYBox.setSelectedIndex(this.model.getColumnYIndex());
		}
		else
		{
			// set no item selected
			this.varYBox.setSelectedIndex(-1);
		}
		this.varYBox.addActionListener(this.controller);
	}

	private void updateVarXBox()
	{
		this.varXBox.removeActionListener(this.controller);
		this.varXBox.removeAllItems();
		for (String varName : this.model.getStatTableModel().getColumnNames())
		{
			this.varXBox.addItem(varName);
		}
		if (this.model.columnXIndexValid())
		{
			this.varXBox.setSelectedIndex(this.model.getColumnXIndex());
		}
		else
		{
			// set no item selected
			this.varXBox.setSelectedIndex(-1);
		}
		this.varXBox.addActionListener(this.controller);
	}

	public void init()
	{
		// Check if a split is set and initialize properly
		if (isSplit())
			setVisibleSplitOptions(true);
		else
			setVisibleSplitOptions(false);

		setVisibleSplitBoundaryOptions(false);
		
		if (vb0 != null)
			resize(vb0);
	}

	/**
	 * Checks whether a split is set.
	 * 
	 * @return
	 */
	protected boolean isSplit()
	{
		// return getSplitVarBoxSelectedIndex() > 0;
		return this.model.getSplitOptions().getColumnSplitIndex() > -1;
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
//		resize(vb0);
	}
	
	private void enableCorrelationCheckBox(boolean b)
	{
		this.showCorrelationBox.setEnabled(b);
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
		// System.out.println("DotplotUserOptionspanel.setVisibleSplitOptions("
		// + b + ")");

		splitOptionsVisible = b;
		singleViewRadioItem.getParent().setVisible(b);
		separateRadioItem.getParent().setVisible(b);
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

	private void setColorOptionsVisible(boolean b)
	{
		this.varColorLabel.getParent().setVisible(b);
		this.varColorBox.getParent().setVisible(b);
		this.colorPreviewPanel.getParent().setVisible(b);

		if (this.isSplit()) // dotplot of scatterplot met split
		{
			// bij split geen kleurschaal
			this.useColorScaleBox.getParent().setVisible(false);
			this.separatorColorScale_splitOptions.getParent().setVisible(false);
		}
		else // dotplot of scatterplot zonder split
		{
			this.useColorScaleBox.getParent().setVisible(true);
			this.useColorScaleBox.setSelected(b);
			this.colorPreviewPanel.setColorA(this.model.getColorA());
			this.colorPreviewPanel.setColorB(this.model.getColorB());
			this.separatorColorScale_splitOptions.getParent().setVisible(false);
		}
	}

	public void actionPerformed(ActionEvent e)
	{
//		System.out.println("DotplotUserOptionsPanel.actionPerformed(): ac="
//			+ e.getActionCommand());

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
				if (this.model.isScatterplotMode())
				{
					// for a split no correlation should be shown 
					this.model.setShowCorrelation(false);
					this.enableCorrelationCheckBox(false);
				}
			}
			resize(vb0);
		}
		else if (e.getSource() == useColorScaleBox)
		{
			this.model.setUseColorScale(this.view.getUseColorScaleBoxSelected());

			setColorOptionsVisible(useColorScaleBox.isSelected());
			varColorBox.setSelectedIndex(-1);
			resize(vb0);
		}
		else if (e.getSource() == optimizeScaleXBox)
		{
			model.setOptimizeScaleX(isOptimizeScaleX());
			view.recalculateScaleXSettings();

			resize(vb0);
		}
		else if (e.getSource().equals(this.minXOnScaleField))
		{
			controller.processMinXOnScaleChanged();
		}
		else if (e.getSource().equals(this.maxXOnScaleField))
		{
			controller.processMaxXOnScaleChanged();
		}
		else if (e.getSource() == okButton)
		{
			setVisibleSplitBoundaryOptions(false);
			if (splitVarBox.getSelectedIndex() == 0)
				setVisibleSplitOptions(false);
			if (varColorBox.getSelectedIndex() < 0)
			{
				setColorOptionsVisible(false);
				this.useColorScaleBox.setSelected(false);
			}
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

	@Override
	public void focusGained(FocusEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void focusLost(FocusEvent e)
	{
		if (e.getSource().equals(this.minXOnScaleField))
		{
			controller.processMinXOnScaleChanged();
		}
		else if (e.getSource().equals(this.maxXOnScaleField))
		{
			controller.processMaxXOnScaleChanged();
		}
	}
	
	public boolean isOptimizeScaleX()
	{
		return this.optimizeScaleXBox.isSelected();
	}

	public JCheckBox getOptimizeScaleXBox()
	{
		return this.optimizeScaleXBox;
	}
	
	/**
	 * Get the maximum value of columnX on the scale as entered by the user.
	 * 
	 * @return
	 */
	public double getMaxXOnScale()
	{
		String s = this.maxXOnScaleField.getText();
		s = s.replace(',', '.');
		return Double.parseDouble(s);
	}

	/**
	 * Get the minimum value of columnX on the scale as entered by the user.
	 * 
	 * @return
	 */
	public double getMinXOnScale()
	{
		String s = this.minXOnScaleField.getText();
		s = s.replace(',', '.');
		return Double.parseDouble(s);
	}

	public void setMaxXOnScale(double d)
	{
		this.maxXOnScaleField.setText(String.valueOf(d));
	}

	public void setMinXOnScale(double d)
	{
		this.minXOnScaleField.setText(String.valueOf(d));
	}

}
