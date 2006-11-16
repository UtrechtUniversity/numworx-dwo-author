package fi.algebraexpressies;

import java.awt.*;
import java.awt.event.*;

class InvulPanel extends Panel implements ItemListener
{	
	private AlgebraSchuifVeld eigenaar;
	private CheckboxGroup g;
	private boolean expr;
	
	public InvulPanel(AlgebraSchuifVeld ae, int x, int y, int b, int h)
	{	setLayout(null);
		eigenaar = ae;
		setBackground(Color.white);
		setBounds(x,y,b,h);
		g = new CheckboxGroup();
		addCheckbox(AlgebraExpressies.rb.getString("ipRegel1Label"),0,0,b,20, true);
		addCheckbox(AlgebraExpressies.rb.getString("ipRegel2Label"),0,20,b,20,false);
		expr = true;
	}
		
	private void addCheckbox(String naam, int x, int y, int b, int h, boolean bl)
	{	Checkbox c = new Checkbox(naam, g, bl);
		c.setBounds(x,y,b,h);
		c.setFont(new Font("SansSerif",Font.PLAIN,12));
		c.addItemListener(this);
		add(c);
	}
	
	public void itemStateChanged(ItemEvent e)
	{	Checkbox cSelect = g.getSelectedCheckbox();
		if (cSelect.getLabel().equals(AlgebraExpressies.rb.getString("ipRegel1Label")))
			expr = true;
		else expr = false;
		eigenaar.zetVeranderd();
	}
	
	public boolean isExpr()
	{	return expr;
	}
	
	
}
