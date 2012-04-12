package fi.algebraexpressies.expressies_ap;

import java.awt.*;

import fi.beans.stringutils.StringUtils;
import fi.algebraexpressies.AlgebraExpressies;
//import fi.algebrapijlenopdr.expressies.Algebra;

public class BasisExpressie extends Expressie  
{	public String basisString;
	double w;
	boolean isWaarde;
	
	public BasisExpressie(String s)
	{	super();
		basisString = s;
		isVeelterm = false;
		isProdukt = false;
		isBasis = true;
		isAsym = false;
		//if (geefWaarde() != null)
		if (!Double.isNaN(geefWaarde().doubleValue()))
		{	isWaarde = true;
			w = geefWaarde().doubleValue();
		}
	}
	
	public void teken(Graphics g, int x, int y)
  	{ 	//g.setFont(new Font("TimesRoman", Font.PLAIN,13));
		//if(geefWaarde()==null)g.setFont(new Font(g.getFont().getName(),Font.ITALIC,g.getFont().getSize()));
		g.drawString(basisString, x, y+fm.getAscent()-1);
		//if(geefWaarde()==null)g.setFont(new Font(g.getFont().getName(),Font.PLAIN,g.getFont().getSize()));
	}
	
	public void zetMaat(FontMetrics fm)
  	{	this.fm = fm;
		hoogte = fm.getHeight()-2;
		breedte = fm.stringWidth(basisString);
		ashoogte = hoogte/2;
	}
	
	public Double geefWaarde()
	{	Double waarde = new Double(Double.NaN); //null;
		try
		{	waarde = Double.valueOf(basisString);
		}
		catch(NumberFormatException e)
		{	
		}
		return waarde;
	}
	
	public Double geefWaarde(double subst)
	{	if (Double.isNaN(geefWaarde().doubleValue()))
			return new Double(subst);
		else 
			return geefWaarde();
	}
	
	public double geefW(double subst)
	{	if (!isWaarde)
			return subst;
		else 
			return w;
	}
	
	public Expressie substitueer(double subst, String var)
	{	if (basisString.equals(var))
		{	return new BasisExpressie("" + subst);
		}
		else return new BasisExpressie(basisString);
	}
	
	public String geefVarNaam()
	{	if (Double.isNaN(geefWaarde().doubleValue()))
			return basisString;
		return null;
	}

	public String toString()
	{	
		String basisStringUit = StringUtils.replaceStr(basisString,"?(","$s");
		basisStringUit = StringUtils.replaceStr(basisStringUit,")","@");
		
		if(!Double.isNaN(w) && (!Algebra.withinLongRange((long)w) || basisString.indexOf('E')>-1))basisStringUit = StringUtils.replaceStr(basisString,"E","*$p10$n") + "@@";
		//if(!Double.isNaN(waarde) && (Math.abs(1.0/waarde)>10000000000.0))basisStringUit = StringUtils.replaceStr(basisString,"E","*$p10$n") + "@@";
        
        if(AlgebraExpressies.language.toString().equals("nl"))basisStringUit = basisStringUit.replace('.',',');
        
		 
        return basisStringUit;
	}
	
	public String toStringStrikt()
	{	
		String basisStringUit = StringUtils.replaceStr(basisString,"?(","$s");
        basisStringUit = StringUtils.replaceStr(basisStringUit,")","@");
        
        if(!Double.isNaN(w) && (!Algebra.withinLongRange((long)w) || basisString.indexOf('E')>-1))basisStringUit = StringUtils.replaceStr(basisString,"E","*$p10$n") + "@@";
		//if(!Double.isNaN(waarde) && (Math.abs(1.0/waarde)>10000000000.0))basisStringUit = StringUtils.replaceStr(basisString,"E","*$p10$n") + "@@";
        
        if(AlgebraExpressies.language.toString().equals("nl"))basisStringUit = basisStringUit.replace('.',',');
        return basisStringUit;
		//basisString = basisString.replace('.',',');
		//if(isWaarde())
	    //{
	    //}
		//return basisString;
	}
	
}
