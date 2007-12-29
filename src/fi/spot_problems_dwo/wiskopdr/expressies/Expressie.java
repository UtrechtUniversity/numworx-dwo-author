package fi.spot_problems_dwo.wiskopdr.expressies;

import java.awt.*;
import java.text.*;
import java.util.*;

public class Expressie 
{	
	public Expressie kind1, kind2;
	public String operatorString;
	boolean isVeelterm;
	boolean isProdukt;
	boolean isBasis;

	static DecimalFormatSymbols dfs;
	public static DecimalFormat df;
	public static FontMetrics fm;
	
	public Expressie()
	{	dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		df = new DecimalFormat("0.###", dfs);
	}
	public void zetMaat(FontMetrics fm)
  	{
	}
	public void teken(Graphics g, int x, int y)
  	{ 
	}
	public double geefWaarde()
	{	return Double.NaN;
	}
	
	public double geefWaarde(double subst)
	{	return Double.NaN;
	}
	
	public double geefWaarde(double[] subst, String[] vars)
	{	return Double.NaN;
	}
	
	public Expressie substitueer(double subst, String var)
	{	return null;
	}
	
	public boolean isWaarde(double subst)
	{	return true;
	}
	public String geefVarNaam()
	{	return null;
	}
	
	
	public String toString()
	{	return null;
	}
	public String toStringStrikt()
	{	return null;
	}
	
	
}
