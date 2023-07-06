package fi.statistiek.piechart;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fi.statistiek.DialogButton;
import fi.statistiek.DisabledItemsComboBox;
import fi.statistiek.Statistiek;
import fi.statistiek.types.AllowedTypes;
import fi.statistiek.types.ColumnType;

/**
 * User options panel for StatistiekView PieChart
 * 
 * @author Sylvia van Borkulo
 * 
 */
public class PieChartUserOptionsPanel extends JPanel implements
	ActionListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private PieChartView view;
	private PieChartController controller;
	private PieChartModel model;

	private DialogButton dialogButton;

	private JPanel panel;
	private Box vb0;
	private Color backgroundColor = new Color(230, 230, 230);

	/**
	 * The box for choosing the variable in the piechart table
	 */
//	private JComboBox<String> columnIndexBox;
	private DisabledItemsComboBox columnIndexBox;
	
	private JButton okButton;

	public PieChartUserOptionsPanel(PieChartView view,
		PieChartController controller, PieChartModel model)
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

		// selected variable settings
		// error in swing: JComboBox is misbehaving (the same as JTextField) in reporting an unbounded max height
		// see also: http://stackoverflow.com/questions/7581846/swing-boxlayout-problem-with-jcombobox-without-using-setxxxsize/7582033#7582033
		// Solution: subclass and return a reasonable height
//		this.columnIndexBox = new JComboBox<String>() {
//            /**
//			 * 
//			 */
//			private static final long serialVersionUID = 1L;
//
//			/** 
//             * @inherited <p>
//             */
//            @Override
//            public Dimension getMaximumSize() {
//                Dimension max = super.getMaximumSize();
//                max.height = getPreferredSize().height;
//                return max;
//            }
//        };
		this.columnIndexBox = new DisabledItemsComboBox();
		this.columnIndexBox.setFont(Statistiek.font);
		this.columnIndexBox.setPreferredSize(new Dimension(100, 25));
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
		hb1.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		hb1.add(columnIndexBox);

		vb1 = Box.createVerticalBox();
		Border border = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		vb1.setBorder(BorderFactory.createTitledBorder(border,
			Statistiek.rb.getString("variableLabel"), TitledBorder.CENTER,
			TitledBorder.TOP, Statistiek.font));
		vb1.add(hb1);
		vb1.add(Box.createRigidArea(new Dimension(5,0)));
		vb1.add(Box.createVerticalGlue());
		vb1.add(Box.createHorizontalGlue());

		Box hb0 = Box.createHorizontalBox();
		hb0.add(Box.createRigidArea(new Dimension(10,0)));
		hb0.add(vb1);
		hb0.add(Box.createRigidArea(new Dimension(10,0)));
		hb0.add(Box.createHorizontalGlue());

		vb0 = Box.createVerticalBox();
		vb0.add(hb0);
		vb0.add(Box.createRigidArea(new Dimension(0,10)));
		vb0.add(okButton);
		vb0.add(Box.createRigidArea(new Dimension(0,20)));

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

	public void setModel(PieChartModel model)
	{
		this.model = model;
	}
	
	public void updateColumnIndexBox()
	{
		this.columnIndexBox.removeActionListener(this.controller);
		this.columnIndexBox.removeAllItems();

		ArrayList<String> names = model.getStatTableModel().getColumnNames();
		ArrayList<ColumnType> types = model.getStatTableModel().getColumnTypes();
		
//		for (String varName : this.model.getStatTableModel().getColumnNames())
		for (int i = 0; i < types.size(); i++)
		{
			if (AllowedTypes.ENUM.toString().equals(types.get(i).getType().toString()))
				this.columnIndexBox.addItem(names.get(i), false);
			else
				this.columnIndexBox.addItem(names.get(i), true); // disabled
		}

		if (this.model.getStatTableModel().isColumnIndexValid(this.model.getColumnIndex()))
		{
			this.columnIndexBox.setSelectedIndex(this.model.getColumnIndex());
		}
		else
		{
			// set no item selected
			//System.out.println("PieChartUserOptionsPanel.update(): no column index selected!");
			this.columnIndexBox.setSelectedIndex(-1);
		}
		this.columnIndexBox.addActionListener(this.controller);
	}
	
	public void update()
	{
		//System.out.println("PieChartUserOptionsPanel.update()");
		
		updateColumnIndexBox();
		
		this.resize(vb0);
	}
	
	public void resize(JComponent c)
	{
		Dimension d = c.getPreferredSize();
		
		panel.setSize(new Dimension(d.width + 10, d.height));
		panel.setPreferredSize(new Dimension(d.width + 10, d.height));
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
