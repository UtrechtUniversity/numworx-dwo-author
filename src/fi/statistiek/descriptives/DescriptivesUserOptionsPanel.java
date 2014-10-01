package fi.statistiek.descriptives;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
 * User options panel for StatistiekView Descriptives
 * 
 * @author Sylvia van Borkulo
 * 
 */
public class DescriptivesUserOptionsPanel extends JPanel implements
	ActionListener
{

	private DescriptivesView view;
	private DescriptivesController controller;
	private DescriptivesModel model;

	private DialogButton dialogButton;

	private JPanel panel;
	private Box vb0;
	private Color backgroundColor = new Color(230, 230, 230);

	/**
	 * The box for choosing the variable in the descriptives table
	 */
	private JComboBox<String> columnIndexBox;
	
	// split settings
	private JLabel splitsLabel;
	private JButton splitButton;
	private JLabel splitVarLabel;
	private JComboBox splitVarBox;
	private JLabel splitNoBinsLabel;
	private JComboBox splitNoBinsBox;
	private JButton splitChooseBoundariesButton;
	/**
	 * Separator between number of split bins settings and split bin boundaries settings
	 */
	private JSeparator separatorSplitBoundaries;
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
	private boolean splitOptionsVisible;
	private boolean splitBoundariesVisible;
	private boolean splitEnumClasses;

	private JButton okButton;

	public DescriptivesUserOptionsPanel(DescriptivesView view,
		DescriptivesController controller, DescriptivesModel model)
	{
//		System.out.println("DescriptivesUserOptionsPanel() met createGUI en layoutGUI");
		
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

		// selected variable settings
		this.columnIndexBox = new JComboBox<String>();
		this.columnIndexBox.setFont(Statistiek.font);
		this.columnIndexBox.setPreferredSize(new Dimension(100, 25));
		this.columnIndexBox.setMaximumSize(new Dimension(100, 25));
		this.columnIndexBox.setActionCommand("columnIndexBox");
		this.columnIndexBox.addActionListener(this.controller);

		// split settings
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
		this.splitVarBox.addActionListener(this.controller);

		this.splitNoBinsLabel = new JLabel(
			Statistiek.rb.getString("noClassesLabel"));
		this.splitNoBinsLabel.setFont(Statistiek.font);

		Integer[] options1 = new Integer[50];
		for (int i = 0; i < 50; i++)
		{
			options1[i] = i + 1;
		}
		this.splitNoBinsBox = new JComboBox(options1);
		this.splitNoBinsBox.setFont(Statistiek.font);
		this.splitNoBinsBox.setMaximumSize(new Dimension(100, 25));
		this.splitNoBinsBox.setPreferredSize(new Dimension(100, 25));
		this.splitNoBinsBox.setActionCommand("splitNoBinsBox");
		this.splitNoBinsBox.addActionListener(this.controller);

		this.splitChooseBoundariesButton = new JButton(
			Statistiek.rb.getString("binsButton"));
		this.splitChooseBoundariesButton.setFont(Statistiek.font);
		this.splitChooseBoundariesButton
			.setPreferredSize(new Dimension(100, 25));
		this.splitChooseBoundariesButton
			.setActionCommand("splitChooseBinsButton");
		this.splitChooseBoundariesButton.addActionListener(this);

		this.separatorSplitBoundaries = new JSeparator();
		this.separatorSplitBoundaries.setBorder(BorderFactory
			.createEtchedBorder(EtchedBorder.LOWERED));
		this.separatorSplitBoundaries.setMaximumSize(new Dimension(140, 3));

		this.splitMinBoundaryLabel = new JLabel(
			Statistiek.rb.getString("startvalueLabel"));
		this.splitMinBoundaryLabel.setFont(Statistiek.font);

		this.splitMinBoundaryField = new JTextField();
		this.splitMinBoundaryField.setMaximumSize(new Dimension(40, 25));
		this.splitMinBoundaryField.setMinimumSize(new Dimension(40, 25));
		this.splitMinBoundaryField.setPreferredSize(new Dimension(40, 25));
		this.splitMinBoundaryField.setActionCommand("splitMinBoundary");
		this.splitMinBoundaryField.addActionListener(controller);
		this.splitMinBoundaryField.addFocusListener(controller);

		this.splitBinWidthLabel = new JLabel(
			Statistiek.rb.getString("classwidthLabel"));
		this.splitBinWidthLabel.setFont(Statistiek.font);

		this.splitBinWidthField = new JTextField();
		this.splitBinWidthField.setMaximumSize(new Dimension(40, 25));
		this.splitBinWidthField.setMinimumSize(new Dimension(40, 25));
		this.splitBinWidthField.setPreferredSize(new Dimension(40, 25));
		this.splitBinWidthField.setActionCommand("splitBinWidth");
		this.splitBinWidthField.addActionListener(controller);
		this.splitBinWidthField.addFocusListener(controller);

		this.splitBoundariesLabel = new JLabel(
			Statistiek.rb.getString("binsButton"));
		this.splitBoundariesLabel.setFont(Statistiek.font);

		this.splitBoundariesArea = new JTextArea();
		this.splitBoundariesArea.setEditable(false);
		this.splitBoundariesAreaScrollPane = new JScrollPane(
			this.splitBoundariesArea);
		this.splitBoundariesArea.setBorder(BorderFactory
			.createLoweredBevelBorder());
		this.splitBoundariesArea.setMaximumSize(new Dimension(120, 140));
		this.splitBoundariesArea.setMinimumSize(new Dimension(120, 140));
		this.splitBoundariesArea.setPreferredSize(new Dimension(130, 140));

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
		Box hb1, hb2, hb3, hb4, hb5, hb6, hb7, hb8, hb9, hb10, 
			hb11, hb12, hb13, hb14;
		Box vb1, vb2;

		// Selected variable settings
		
		hb1 = Box.createHorizontalBox();
		hb1.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		hb1.add(columnIndexBox);
		// create some extra space after columnIndexBox
		hb1.add(Box.createRigidArea(new Dimension(25, 25)));

		vb1 = Box.createVerticalBox();
		Border border = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		vb1.setBorder(BorderFactory.createTitledBorder(border,
			Statistiek.rb.getString("variableLabel"), TitledBorder.CENTER,
			TitledBorder.TOP, Statistiek.font));
		vb1.add(hb1);
		vb1.add(Box.createVerticalGlue());

		// splitOptions
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
		hb4.add(splitNoBinsLabel);

		hb5 = Box.createHorizontalBox();
		hb5.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		hb5.add(splitNoBinsBox);

		hb6 = Box.createHorizontalBox();
		hb6.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		hb6.add(separatorSplitBoundaries);

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

		vb2 = Box.createVerticalBox();
		vb2.setBorder(BorderFactory.createTitledBorder(border,
			Statistiek.rb.getString("splitsLabel"), TitledBorder.CENTER,
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
		vb2.add(hb11);
		vb2.add(hb12);
		vb2.add(hb13);
		vb2.add(hb14);
		vb2.add(Box.createVerticalGlue());
		
		Box hb0 = Box.createHorizontalBox();
		hb0.add(Box.createHorizontalStrut(10));
		hb0.add(vb1);
		hb0.add(Box.createHorizontalStrut(10));
		hb0.add(vb2);
		hb0.add(Box.createHorizontalStrut(50));

		vb0 = Box.createVerticalBox();
		vb0.add(hb0);
		vb0.add(Box.createVerticalStrut(10));
		vb0.add(okButton);
		vb0.add(Box.createVerticalStrut(20));

		panel.add(vb0);
	}

	public DialogButton getDialogButton()
	{
		return this.dialogButton;
	}

	public int getVarColumnsBoxSelectedIndex()
	{
		return this.columnIndexBox.getSelectedIndex();
	}

	public int getSplitVarBoxSelectedIndex()
	{
		return this.splitVarBox.getSelectedIndex();
	}

	public void setModel(DescriptivesModel model)
	{
		this.model = model;
	}
	
	public void updateColumnIndexBox()
	{
		this.columnIndexBox.removeActionListener(this.controller);
		this.columnIndexBox.removeAllItems();

		for (String varName : this.model.getTableModel().getColumnNames())
		{
			this.columnIndexBox.addItem(varName);
		}
		
		if (this.model.getTableModel().isColumnIndexValid(this.model.getColumnIndex()))
		{
			this.columnIndexBox.setSelectedIndex(this.model.getColumnIndex());
		}
		else
		{
			// set no item selected
			//System.out.println("DescriptivesUserOptionsPanel.update(): no column index selected!");
			this.columnIndexBox.setSelectedIndex(-1);
		}
		this.columnIndexBox.addActionListener(this.controller);
	}
	
	public void update()
	{
//		System.out.println("DescriptivesUserOptionsPanel.update()");
		
		updateColumnIndexBox();
		updateSplitSettings();

		this.resize(vb0);
	}
	
	public void resize(JComponent c)
	{
		//System.out.println("DescriptivesUserOptionsPanel.resize(): c = " + c.toString());
		Dimension d = c.getPreferredSize();
		panel.setSize(new Dimension(d.width + 10, d.height));
		panel.setPreferredSize(new Dimension(d.width + 10, d.height));
	}
	
	public void init()
	{
		setVisibleSplitBoundaryOptions(false);
		if (vb0 != null)
			resize(vb0);

	}

	private void updateSplitSettings()
	{
		this.splitVarBox.removeActionListener(this.controller);
		this.splitVarBox.removeAllItems();
		this.splitVarBox.addItem(Statistiek.rb.getString("chooseItem"));
		for (int column = 0; column < this.model.getTableModel()
			.getColumnCount(); column++)
		{
			splitVarBox.addItem(this.model.getTableModel()
				.getColumnName(column));
		}
		
		// check of columnindex valid
		if (this.model.columnIndexValid())
		{
			//System.out.println("HistogramUserOptionsPanel.update(): COLUMN INDEX VALID!");
			this.splitVarBox.setSelectedIndex(this.model.getSplitOptions()
				.getColumnSplitIndex() + 1);
		}
		else
		{
			// set no item selected
			this.splitVarBox.setSelectedIndex(-1);
		}
		
		this.splitVarBox.addActionListener(this.controller);
		
		this.splitNoBinsBox.removeActionListener(this.controller);
		this.splitNoBinsBox.setSelectedItem(new Integer(this.model
			.getSplitOptions().getBinBoundaries().size() - 1));
		this.splitNoBinsBox.addActionListener(this.controller);
		
		// check of column index valid
		if (this.model.columnIndexValid())
		{
    		if (this.model.getSplitOptions().getColumnSplitIndex() > -1)
    		{
    			ColumnType cSplitType = this.model.getTableModel().getColumnTypes()
    				.get(this.model.getSplitOptions().getColumnSplitIndex());
    			AllowedTypes splitType = cSplitType.getType();
    			if (splitType.equals(AllowedTypes.DOUBLE)
    				|| splitType.equals(AllowedTypes.INTEGER))
    			{
    				this.splitMinBoundaryField.setText(Statistiek.df
    					.format(this.model.getSplitOptions().getBinBoundaries()
    						.get(0)));
    				Double d = this.model.getSplitOptions().getBinBoundaries()
    					.get(1)
    					- this.model.getSplitOptions().getBinBoundaries().get(0);
    				this.splitBinWidthField.setText(Statistiek.df.format(d));
    				StringBuilder sb = new StringBuilder();
    				for (int i = 0; i < this.model.getSplitOptions()
    					.getBinBoundaries().size() - 1; i++)
    				{
    					sb.append(Statistiek.df.format(this.model.getSplitOptions()
    						.getBinBoundaries().get(i)));
    					sb.append(" - ");
    					sb.append(Statistiek.df.format(this.model.getSplitOptions()
    						.getBinBoundaries().get(i + 1)));
    					sb.append("\n");
    				}
    				this.splitBoundariesArea.setText(sb.toString());
    				this.splitNoObjectsLabel.setText(Statistiek.rb
    					.getString("numberLabel")
    					+ this.model.getTableModel().getRowCount());
    				this.splitMinValueLabel.setText(Statistiek.rb
    					.getString("minLabel")
    					+ this.model.getTableModel().getColumnMin(
    						this.model.getSplitOptions().getColumnSplitIndex()));
    				this.splitMaxValueLabel.setText(Statistiek.rb
    					.getString("maxLabel")
    					+ this.model.getTableModel().getColumnMax(
    						this.model.getSplitOptions().getColumnSplitIndex()));
    				setVisibleSplitEnumClasses(false);
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
    				setVisibleSplitEnumClasses(true);
    			}
    			
    			// set button for setting split boundaries visible
    			this.splitChooseBoundariesButton.getParent().setVisible(true);    			
    		} // er is een split variabele ingesteld
    		else
    		{
    			// set combobox for number of bins invisible
    			this.splitNoBinsBox.getParent().setVisible(false);
    			this.splitNoBinsLabel.getParent().setVisible(false);
    			// set button for setting split boundaries invisible
    			this.splitChooseBoundariesButton.getParent().setVisible(false);
    			
    		}
		}
		
		boolean split = this.model.getSplitOptions().getColumnSplitIndex() > -1;
		this.setVisibleSplitOptions(split);
	}

	private void setVisibleSplitEnumClasses(boolean b)
	{
		splitEnumClasses = b;
		
		this.splitNoBinsBox.getParent().setVisible(!b);
		this.splitNoBinsLabel.getParent().setVisible(!b);

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

	private void setVisibleSplitOptions(boolean b)
	{
		splitOptionsVisible = b;

		splitVarLabel.getParent().setVisible(b);
		splitVarBox.getParent().setVisible(b);
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

	private void setVisibleSplitBoundaryOptions(boolean b)
	{
		splitBoundariesVisible = b;
		separatorSplitBoundaries.getParent().setVisible(b);
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

	public double getSplitminBoundary()
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
	
	public int getSplitBinsBoxSelectedInt()
	{
		return ((Integer) this.splitNoBinsBox.getSelectedItem()).intValue();
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

			this.updateSplitSettings();
			this.resize(vb0);
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

			this.resize(vb0);
		}
		else if (e.getSource() == okButton)
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
					
					if (vb0 != null)
						resize(vb0);

					init();
					update();
					repaint();
				}
			};
			startDraad.start();
		}
	}
}
