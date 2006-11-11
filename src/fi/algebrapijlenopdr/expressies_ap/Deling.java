package fi.algebrapijlenopdr.expressies_ap;

import java.awt.*;

public class Deling extends Expressie  
{	
	
	public Deling(Expressie e1,Expressie e2 )
	{	kind1 = e1;
		kind2 = e2;
		isVeelterm = false;
		isProdukt = true;
		isBasis = false;
	}
	
	public void teken(Graphics g, int x, int y)
  	{ 		
		kind1.teken(g, x + (breedte-kind1.breedte)/2, y);
		int y1 = kind1.hoogte+1;
		g.drawLine(x,y+y1,x+breedte-1,y+y1);
		int y2 = y1+2;
		kind2.teken(g ,x + (breedte-kind2.breedte)/2, y+y2);
		
	}
	
	public void zetMaat(FontMetrics fm)
  	{	this.fm = fm;
		kind1.zetMaat(fm);
		kind2.zetMaat(fm);
		
		if(kind1.hoogte != kind2.hoogte)isAsym = true;
		
		breedte = Math.max(kind1.breedte, kind2.breedte)+4;
		hoogte = kind1.hoogte + kind2.hoogte + 3;
		ashoogte = kind1.hoogte+1;
	}
	
	public Double geefWaarde()
	{	if(kind1.geefWaarde()!=null && kind2.geefWaarde()!=null)
		{	double d1 = kind1.geefWaarde().doubleValue();
			double d2 = kind2.geefWaarde().doubleValue();
			if(d2!=0)return new Double(d1/d2);
			else return null;
		}
		else return null;
	}
	
	public double geefW(double subst)
	{	return kind1.geefW(subst)/kind2.geefW(subst);
	}
	
	public boolean isWaarde(double subst)
	{	return kind1.isWaarde(subst) && kind2.isWaarde(subst) && kind2.geefW(subst)!=0;
	}
	
	public String geefVarNaam()
	{	String s1 = kind1.geefVarNaam();
		String s2 = kind2.geefVarNaam();
		if(s1!=null && s2!=null && (s1.equals("") || s2.equals("")))return "";
		else if(s1!=null && s2!=null && !s1.equals(s2))return "";
		else if(s1!=null && s2!=null && s1.equals(s2))return s1;
		else if(s1!=null && s2==null)return s1;
		else if(s1==null && s2!=null)return s2;
		else return null;
	}
}
