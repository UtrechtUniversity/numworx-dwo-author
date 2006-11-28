package fi.nabouwenaanzichten;

import java.awt.*;
import java.awt.event.*;

class InvulKeuzePanel extends Panel implements ItemListener
{
	private CheckboxGroup g;
	private boolean bouwen;
	
	public InvulKeuzePanel(int x, int y, int b, int h)
	{	setLayout(null);
		setBackground(Color.white);
		setBounds(x,y,b,h);
		g = new CheckboxGroup();
		addCheckbox(NabouwenAanzichten.rb.getString("ipRegel1Label"),0,0,b,24, true);
		addCheckbox(NabouwenAanzichten.rb.getString("ipRegel2Label"),0,24,b,24,false);
		bouwen = true;
	}
		
	private void addCheckbox(String naam, int x, int y, int b, int h, boolean bl)
	{	Checkbox c = new Checkbox(naam, g, bl);
		c.setBounds(x,y,b,h);
		c.setFont(new Font("SansSerif",Font.PLAIN,14));
		c.addItemListener(this);
		add(c);
	}
	
	public void itemStateChanged(ItemEvent e)
	{	Checkbox cSelect = g.getSelectedCheckbox();
		if (cSelect.getLabel().equals(NabouwenAanzichten.rb.getString("ipRegel1Label")))
			bouwen = true;
		else bouwen = false;
	}
	
	public boolean isBouwen()
	{	return bouwen;
	}
	
	
}
