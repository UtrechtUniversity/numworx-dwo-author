package fi.algebrapijlenopdr;

import java.awt.*;
import java.awt.event.*;

class InvulPanel extends Panel implements ItemListener
{	
	private AlgebraSchuifVeld eigenaar;
	private CheckboxGroup g;
	private boolean expr;
	private Checkbox[] checkboxes;
	private int aantalCheckboxes;
	
	public InvulPanel(AlgebraSchuifVeld ae, int x, int y, int b, int h)
	{	setLayout(null);
		eigenaar = ae;
		setBackground(Color.white);
		setBounds(x,y,b,h);
		g = new CheckboxGroup();
		checkboxes = new Checkbox[2];
		addCheckbox(AlgebraPijlenOpdr.rb.getString("ipRegel1Label"),0,0,b,20, false);
		addCheckbox(AlgebraPijlenOpdr.rb.getString("ipRegel2Label"),0,20,b,20,true);
		expr = false;
	}
		
	private void addCheckbox(String naam, int x, int y, int b, int h, boolean bl)
	{	Checkbox c = new Checkbox(naam, g, bl);
		c.setBounds(x,y,b,h);
		c.setFont(new Font("SansSerif",Font.PLAIN,12));
		c.addItemListener(this);
		add(c);
		checkboxes[aantalCheckboxes] = c;
		aantalCheckboxes++;
	}
	
	public void itemStateChanged(ItemEvent e)
	{	Checkbox cSelect = g.getSelectedCheckbox();
		if (cSelect.getLabel().equals(AlgebraPijlenOpdr.rb.getString("ipRegel1Label")))
			expr = true;
		else expr = false;
		eigenaar.zetVeranderd();
	}
	
	public boolean isExpr()
	{	return expr;
	}
	
	public void zetExpressie(boolean b)
	{	if(b) 
		{	checkboxes[0].setState(true);
			checkboxes[1].setState(false);
		}
		else
		{	checkboxes[1].setState(true);
			checkboxes[0].setState(false);
		}
	}
	
	
}
