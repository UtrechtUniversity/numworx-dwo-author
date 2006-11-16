package fi.algebraexpressies.expressies;

import java.awt.*;

public class Kwadraat extends Expressie  
{	Expressie operatorExpr;
	
	public Kwadraat(Expressie e1 )
	{	kind1 = e1;
		operatorString = "2";
		operatorExpr = new BasisExpressie(operatorString);
		isVeelterm = false;
		isProdukt = true;
		isBasis = false;
		isAsym = false;
	}
	
	public void teken(Graphics g, int x, int y)
  	{ 	HaakjeLinks hl= new HaakjeLinks(kind1.hoogte+2);
		HaakjeRechts hr= new HaakjeRechts(kind1.hoogte+2);
		int g1;
		if(!(kind1.isBasis && (kind1.geefWaarde()==null || (kind1.geefWaarde()!=null && kind1.geefWaarde().doubleValue()>0))))
		{	hl.teken(g, x, y+ashoogte-kind1.ashoogte-1);
			g1 = HaakjeLinks.geefHBreedte(fm);
		}
		else g1 = 0;	
		kind1.teken(g, x+g1, y + ashoogte-kind1.ashoogte);
		int g2 = g1 + kind1.breedte;
		int g3;
		if(!(kind1.isBasis && (kind1.geefWaarde()==null || (kind1.geefWaarde()!=null && kind1.geefWaarde().doubleValue()>0))))
		{	hr.teken(g, x+g2, y+ashoogte-kind1.ashoogte-1);
			g3 = g2 + HaakjeLinks.geefHBreedte(fm);
		}
		else g3 = g2;
		operatorExpr.teken(g, x+g3, y);
	}
	
	public void zetMaat(FontMetrics fm)
  	{	this.fm = fm;
		kind1.zetMaat(fm);
		operatorExpr.zetMaat(fm);
		if(!(kind1.isBasis && (kind1.geefWaarde()==null || (kind1.geefWaarde()!=null && kind1.geefWaarde().doubleValue()>0))))
		{	int hb = HaakjeLinks.geefHBreedte(fm);
			breedte = kind1.breedte + fm.stringWidth(operatorString) + 2*hb;
			hoogte = kind1.hoogte + 4 + operatorExpr.hoogte-10;
		}
		else
		{	breedte = kind1.breedte + operatorExpr.breedte;
			hoogte =  kind1.hoogte + 2 + operatorExpr.hoogte-10;
		}
		
		{	ashoogte =  kind1.ashoogte + 2 + operatorExpr.hoogte-10;
			isAsym = true;
		}
	}
	
	public Double geefWaarde()
	{	if(kind1.geefWaarde()!=null)
		{	double d1 = kind1.geefWaarde().doubleValue();
			return new Double(d1*d1);
		}
		else return null;
	}
	public double geefW(double subst)
	{	return kind1.geefW(subst)*kind1.geefW(subst);
	}
	
	public boolean isWaarde(double subst)
	{	return kind1.isWaarde(subst);
	}
	
	public String geefVarNaam()
	{	String s1 = kind1.geefVarNaam();
		if(s1!=null)return s1;
		return null;
	}
}
