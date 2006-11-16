package fi.algebraexpressies.expressies;

import java.awt.*;

public class BasisExpressie extends Expressie  
{	String 	basisString;
	double w;
	boolean isWaarde;
	
	public BasisExpressie(String s)
	{	super();
		basisString = s;
		isVeelterm = false;
		isProdukt = false;
		isBasis = true;
		isAsym = false;
		if(geefWaarde()!=null)
		{	isWaarde = true;
			w = geefWaarde().doubleValue();
		}
	}
	
	public void teken(Graphics g, int x, int y)
  	{ 	g.drawString(basisString, x, y+fm.getAscent()-1);
	}
	
	public void zetMaat(FontMetrics fm)
  	{	this.fm = fm;
		hoogte = fm.getHeight()-2;
		breedte = fm.stringWidth(basisString);
		ashoogte = hoogte/2;
	}
	
	public Double geefWaarde()
	{	Double waarde = null;
		try
		{	waarde = Double.valueOf(basisString);
		}
		catch(NumberFormatException e)
		{	
		}
		return waarde;
	}
	
	public Double geefWaarde(double subst)
	{	if(geefWaarde()==null)return new Double(subst);
		else return geefWaarde();
	}
	
	public double geefW(double subst)
	{	if(!isWaarde)return subst;
		else return w;
	}
	
	public String geefVarNaam()
	{	if(geefWaarde()==null)return basisString;
		return null;
	}

}
