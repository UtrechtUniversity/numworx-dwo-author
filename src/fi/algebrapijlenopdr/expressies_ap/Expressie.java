package fi.algebrapijlenopdr.expressies_ap;

import java.awt.*;
import java.text.*;

public class Expressie 
{	
	public int breedte;
	public int hoogte;
	public int ashoogte;
	public Expressie kind1, kind2;
	public String operatorString;
	boolean isVeelterm;
	boolean isProdukt;
	boolean isBasis;
	boolean isAsym;
	//boolean isLeeg;

	static DecimalFormatSymbols dfs;
	public static DecimalFormat df;
	public static FontMetrics fm;
	
	public static boolean isInteger(double d)
	{	return Math.abs(Math.rint(d)-d)<0.000000001;
	}
	
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
	public Double geefWaarde()
	{	return null;
	}
	//public Double geefWaarde(double subst)
	//{	return null;
	//}
	public double geefW(double subst)
	{	return 0;
	}
	public boolean isWaarde(double subst)
	{	return true;
	}
	public String geefVarNaam()
	{	return null;
	}
}
