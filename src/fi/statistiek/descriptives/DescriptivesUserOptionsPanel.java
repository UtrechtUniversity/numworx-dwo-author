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
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fi.statistiek.DialogButton;
import fi.statistiek.Statistiek;

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

		// ok-cancel
		this.okButton = new JButton("OK");
		this.okButton.addActionListener(this);
	}

	private void layoutGuiComponents()
	{
		Box hb1;
		Box vb1;

		// Selected variable settings
		
		hb1 = Box.createHorizontalBox();
		hb1.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		hb1.add(columnIndexBox);

		vb1 = Box.createVerticalBox();
		Border border = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		vb1.setBorder(BorderFactory.createTitledBorder(border,
			Statistiek.rb.getString("variableLabel"), TitledBorder.CENTER,
			TitledBorder.TOP, Statistiek.font));
		vb1.add(hb1);
		vb1.add(Box.createVerticalGlue());

		Box hb0 = Box.createHorizontalBox();
		hb0.add(Box.createHorizontalStrut(10));
		hb0.add(vb1);

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

	public int getVarColumnsBoxSelectedIndex()
	{
		return this.columnIndexBox.getSelectedIndex();
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

		if (SwingUtilities.getWindowAncestor(this.panel) != null)
			SwingUtilities.getWindowAncestor(this.panel).pack();
		repaint();
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
					//init();
					repaint();
				}
			};
			startDraad.start();
		}
	}
}
