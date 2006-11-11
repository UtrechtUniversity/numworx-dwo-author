package fi.algebrapijlenopdr.expressies_ap;

import java.awt.*;

public class Vermenigvuldiging extends Expressie  
{	
	
	public Vermenigvuldiging(Expressie e1,Expressie e2 )
	{	kind1 = e1;
		kind2 = e2;
		operatorString = "·";
		isVeelterm = false;
		isProdukt = true;
		isBasis = false;
		isAsym = false;
	}
	
	public void teken(Graphics g, int x, int y)
  	{ 	HaakjeLinks hl1= new HaakjeLinks(kind1.hoogte+2);
		HaakjeRechts hr1= new HaakjeRechts(kind1.hoogte+2);
		HaakjeLinks hl2= new HaakjeLinks(kind2.hoogte+2);
		HaakjeRechts hr2= new HaakjeRechts(kind2.hoogte+2);
		int g1;
		if(kind1.isVeelterm)
		{	hl1.teken(g, x, y+ashoogte-kind1.ashoogte-1);
			g1 = HaakjeLinks.geefHBreedte(fm);
		}
		else g1 = 0;	
		kind1.teken(g, x+g1, y + ashoogte-kind1.ashoogte);
		int g2 = g1 + kind1.breedte;
		int g3;
		if(kind1.isVeelterm)
		{	hr1.teken(g, x+g2, y+ashoogte-kind1.ashoogte-1);
			g3 = g2 + HaakjeLinks.geefHBreedte(fm);
		}
		else g3 = g2;
		g.drawString(operatorString, x+g3, y + ashoogte-fm.getHeight()/2 + fm.getAscent());
		int g4 = g3 + fm.stringWidth(operatorString);
		int g5;
		if(kind2.isVeelterm)
		{	hl2.teken(g, x+g4, y+ashoogte-kind2.ashoogte-1);
			g5 = g4 + HaakjeLinks.geefHBreedte(fm);
		}
		else g5 = g4;
		kind2.teken(g, x+g5, y + ashoogte-kind2.ashoogte);
		int g6 = g5 + kind2.breedte;
		if(kind2.isVeelterm)
		{	hr2.teken(g, x+g6, y+ashoogte-kind2.ashoogte-1);
		}
	}
	
	public void zetMaat(FontMetrics fm)
  	{	this.fm = fm;
		kind1.zetMaat(fm);
		kind2.zetMaat(fm);
		if((kind1.isVeelterm && !kind2.isVeelterm) || (!kind1.isVeelterm && kind2.isVeelterm))
		{	int hb = HaakjeLinks.geefHBreedte(fm);
			breedte = kind1.breedte + fm.stringWidth(operatorString) + kind2.breedte + 2*hb;
			hoogte = Math.max(kind1.hoogte, kind2.hoogte) + 2;
		}
		else if(kind2.isVeelterm && kind1.isVeelterm)
		{	int hb = HaakjeLinks.geefHBreedte(fm);
			breedte = kind1.breedte + fm.stringWidth(operatorString) + kind2.breedte + 4*hb;
			hoogte = Math.max(kind1.hoogte, kind2.hoogte) + 2;
		}
		else
		{	breedte = kind1.breedte + fm.stringWidth(operatorString) + kind2.breedte;
			hoogte = Math.max(kind1.hoogte, kind2.hoogte);
		}
		if(!kind1.isAsym && !kind2.isAsym)
		{	ashoogte = hoogte/2;
		}
		if(kind1.isAsym || kind2.isAsym)
		{	ashoogte = Math.max(kind1.ashoogte, kind2.ashoogte);
			hoogte = ashoogte + Math.max(kind1.hoogte-kind1.ashoogte, kind2.hoogte-kind2.ashoogte);
			isAsym = true;
		}
	}
	
	public Double geefWaarde()
	{	if(kind1.geefWaarde()!=null && kind2.geefWaarde()!=null)
		{	double d1 = kind1.geefWaarde().doubleValue();
			double d2 = kind2.geefWaarde().doubleValue();
			return new Double(d1*d2);
		}
		else if(kind1 instanceof BasisExpressie && kind1.geefWaarde().doubleValue()==0)
		{	return new Double(0);
		}
		else return null;
	}
	
	public double geefW(double subst)
	{	return kind1.geefW(subst)*kind2.geefW(subst);
	}
	
	public boolean isWaarde(double subst)
	{	return kind1.isWaarde(subst) && kind2.isWaarde(subst);
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
