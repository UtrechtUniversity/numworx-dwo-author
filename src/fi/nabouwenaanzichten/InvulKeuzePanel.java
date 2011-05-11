package fi.nabouwenaanzichten;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

class InvulKeuzePanel extends JPanel implements ActionListener //ItemListener
{
	//private CheckboxGroup g;
	private ButtonGroup g;
	private boolean bouwen;
	JRadioButton bouwButton, sloopButton;
	
	public InvulKeuzePanel(int x, int y, int b, int h)
	{	setLayout(null);
		//setBackground(Color.white);
	

	
		setBounds(x,y,b,h);
		//g = new CheckboxGroup();
		g = new ButtonGroup();
		bouwButton = addCheckbox(NabouwenAanzichten.rb.getString("ipRegel1Label"),0,0,b,24, true);
		sloopButton = addCheckbox(NabouwenAanzichten.rb.getString("ipRegel2Label"),0,24,b,24,false);
		bouwen = true;
	}

	public void setBackground(Color c)
	{
		super.setBackground(c);
		if (bouwButton != null)
		{
			bouwButton.setBackground(c);
			sloopButton.setBackground(c);
		}	
	}

	public void setOpaque(boolean b)
	{
		super.setOpaque(b);
		if (bouwButton != null)
		{
			bouwButton.setOpaque(b);
			sloopButton.setOpaque(b);
		}	
		
	}
	private JRadioButton addCheckbox(String naam, int x, int y, int b, int h, boolean bl)
	{	//Checkbox c = new Checkbox(naam, g, bl);
		JRadioButton c = new JRadioButton(naam, bl);
		g.add(c);
		c.setBounds(x,y,b,h);
		c.setFont(new Font("SansSerif",Font.PLAIN,14));
		//c.addItemListener(this);
		c.addActionListener(this);
		add(c);
		return c;
	}
	
	//public void itemStateChanged(ItemEvent e)
	public void actionPerformed(ActionEvent e)
	{	
/*		
		Checkbox cSelect = g.getSelectedCheckbox();
		if (cSelect.getLabel().equals(NabouwenAanzichten.rb.getString("ipRegel1Label")))
			bouwen = true;
		else 
			bouwen = false;
*/		
		bouwen = bouwButton.isSelected();
	}
	
	public boolean isBouwen()
	{	return bouwen;
	}
	
	
}
