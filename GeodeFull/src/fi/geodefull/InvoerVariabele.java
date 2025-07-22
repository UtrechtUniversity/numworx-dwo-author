package fi.geodefull;

import java.awt.*;
import java.awt.event.*;
import java.applet.Applet;
import fi.beans.grnuminput.*;


public class InvoerVariabele implements NumberListener
{	
	private double waarde;
	private double minw;
	private double maxw;
	private String naam;
	private TekenApplet3D eigenaar ;

	public InvoerVariabele(String n, double mn, double mx, double val)
	{	naam = n;
		minw = mn;
		maxw = mx;
		waarde = val;
	}
	
	public void numberChanged(String name, double w)
	{  waarde = w;
	   eigenaar.invoerVarActie(this);
	}
	
	public double geefWaarde()
	{	return waarde;
	}
	
	public void zetWaarde(double d)
	{ 	waarde = d;
	}
	
	public String geefNaam()
	{	return naam;
	}
	
	public double geefMin()
	{	return minw;
	}
	
	public double geefMax()
	{	return maxw;
	}
	
	public void ontvangEigenaar(TekenApplet3D ap)
	{	eigenaar = ap;
	}
}
