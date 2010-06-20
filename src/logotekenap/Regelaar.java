package logotekenap;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Panel;

import javax.swing.JPanel;

public class Regelaar extends JPanel
{	
	private TekenApplet eigenaar;
	private GridBagLayout gridbag;
	private GridBagConstraints c;
	private int aantalComponenten;
	private int maxAantalComponenten;
	private Component[] componenten;
	
	public Regelaar(TekenApplet ap)
	{	eigenaar = ap;
		maxAantalComponenten = 10;
		componenten = new Component[maxAantalComponenten];
		aantalComponenten = 0;
		gridbag = new GridBagLayout();
		c = new GridBagConstraints();
		setLayout(gridbag);
		c.insets = new Insets(10, 10, 10, 10); 			
		c.anchor = GridBagConstraints.NORTHWEST;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.weighty = 0.0;
		c.weightx = 0.0;
	}	
	//-----------------------------------------------------------------------------------------
	// nieuwe InvoerVariabelen worden hier op het panel geplaatst 
	//-----------------------------------------------------------------------------------------
	public void maakZichtbaar(Component com)
	{	if(com instanceof InvoerVar)
		{	((InvoerVar)com).zetBaas(eigenaar);
		}
		componenten[aantalComponenten] = com;
		aantalComponenten++;
		gridbag.setConstraints(com, c);
		add(com);
	}
	//-----------------------------------------------------------------------------------------
	// wordt gebruikt door de tracebeheerder om de InvoerVariabelen tijdens de trace uit te zetten
	//-----------------------------------------------------------------------------------------
	public void setEnableAll(boolean b)
	{	for(int i=0 ; i<aantalComponenten ; i++)
		{	componenten[i].setEnabled(b);
		}
	}
}