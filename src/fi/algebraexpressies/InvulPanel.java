package fi.algebraexpressies;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

class InvulPanel extends JPanel implements ActionListener //ItemListener
{	
	private AlgebraSchuifVeld eigenaar;
	//private CheckboxGroup g;
	private ButtonGroup g;
	JRadioButton expressieButton, waardeButton;
	private boolean expr;
	
	public InvulPanel(AlgebraSchuifVeld ae, int x, int y, int b, int h)
	{	setLayout(null);
		eigenaar = ae;
		setBackground(Color.white);
		setBounds(x, y, b, h);
		//g = new CheckboxGroup();
		g = new ButtonGroup();
		expressieButton = addCheckbox(AlgebraExpressies.rb.getString("ipRegel1Label"), 0, 0, b, 20, true);
		waardeButton = addCheckbox(AlgebraExpressies.rb.getString("ipRegel2Label"), 0, 20, b, 20, false);
		expr = true;
	}
		
	private JRadioButton addCheckbox(String naam, int x, int y, int b, int h, boolean bl)
	{	//Checkbox c = new Checkbox(naam, g, bl);
		JRadioButton c = new JRadioButton(naam);
		g.add(c);
		c.setSelected(bl);
		c.setBounds(x, y, b, h);
		c.setFont(new Font("SansSerif",Font.PLAIN, 12));
		c.setOpaque(false);
		//c.addItemListener(this);
		c.addActionListener(this);
		add(c);
		
		return c;
	}
/*	
	public void itemStateChanged(ItemEvent e)
	{	Checkbox cSelect = g.getSelectedCheckbox();
		if (cSelect.getLabel().equals(AlgebraExpressies.rb.getString("ipRegel1Label")))
			expr = true;
		else 
			expr = false;
		eigenaar.zetVeranderd();
	}
*/	
	public void actionPerformed(ActionEvent e)
	{
		if (expressieButton.isSelected())
			expr = true;
		else 
			expr = false;
		eigenaar.zetVeranderd();
		
//System.out.println("expr = " + expr);		
			
	}
	
	public boolean isExpr()
	{	return expr;
	}
	
	public void zetExpressie(boolean b)
	{	if(b) 
		{	//checkboxes[0].setState(true);
			//checkboxes[1].setState(false);
			expressieButton.setSelected(true);
		}
		else
		{	//checkboxes[1].setState(true);
			//checkboxes[0].setState(false);
			waardeButton.setSelected(true);
		}
	}
	
}
