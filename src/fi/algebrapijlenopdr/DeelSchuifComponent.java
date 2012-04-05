package fi.algebrapijlenopdr;

import java.awt.Polygon;
import java.awt.*;
import java.awt.event.*;
import fi.algebrapijlenopdr.expressies_ap.*;

public class DeelSchuifComponent extends BewerkingSchuifComponent 
{	
	public DeelSchuifComponent(AlgebraSchuifVeld asv,int x, int y, int b, int h)
	{	super(asv,x,y,b,h);
		
		tf.setText("3");
	}
	
	public void paint(Graphics g)
  	{ 	super.paint(g);
		
		g.setColor(Color.black);
		String s = "/ " + Expressie.df.format(beginw.geefWaarde());
		
		Font f = new Font("SansSerrif",Font.PLAIN,14);
		g.setFont(f);
		FontMetrics fm = g.getFontMetrics();
		int w = fm.stringWidth(s); 
		int sccrollCorr = 0;
		if(scrollable)sccrollCorr = 10;
		
		if (!tf.isVisible())
		{	
			if(!links)g.drawString(s,5+(getSize().width-w-sccrollCorr)/2,getSize().height-4);
			else g.drawString(s,-5+(getSize().width-w-sccrollCorr)/2,getSize().height-4);
		}
		else
		{	
			if(!links)g.drawString("/ ",5+(getSize().width-w-sccrollCorr)/2,getSize().height-4);
			else g.drawString("/ ",-5+(getSize().width-w-sccrollCorr)/2,getSize().height-4);
		}
			
	}
	
	
	public Expressie geefUitvoer(int max)
	{	if (AlgebraPijlenOpdr.simplify)
		{	Expressie uitv = new Expressie();
			if (pijlIn1 == null)
				return null;
			Expressie e1 = pijlIn1.zender.geefUitvoer(max-1);
			Expressie e2 = beginw;
			if (e1 == null)
				return null;
			
			double d = 1;
			if (e1 instanceof Vermenigvuldiging)
			{	d = e1.kind1.geefWaarde().doubleValue() / e2.geefWaarde().doubleValue();
				double dn = e2.geefWaarde().doubleValue() / e1.kind1.geefWaarde().doubleValue();
				//if(d==0)uitv = new BasisExpressie("0");
				if (d == 1)
					uitv = e1.kind2;
				else if (d == -1)
					uitv = new Aftrekking(new BasisExpressie("0"), e1.kind2);
				else if (d > 0 && Expressie.isInteger(d))
					uitv = new Vermenigvuldiging(new BasisExpressie(Expressie.df.format(d)), e1.kind2);
				else if (d > 0 && Expressie.isInteger(dn))
					uitv = new Deling(e1.kind2, new BasisExpressie(Expressie.df.format(dn)));
				else if (d <0 && Expressie.isInteger(d))
					uitv = new Aftrekking(new BasisExpressie("0"),
							              new Vermenigvuldiging(new BasisExpressie(Expressie.df.format(-d)), e1.kind2));
				else if (d < 0 && Expressie.isInteger(dn))
					uitv = new Deling(e1.kind2,new BasisExpressie(Expressie.df.format(dn)));
			
				//else if(d<0)uitv = new Aftrekking(new BasisExpressie("0"),new Vermenigvuldiging(new BasisExpressie(Expressie.df.format(-d)),e1.kind2));
				else uitv = new Deling(e1,e2);
				return uitv;
			}
			else if(e1 instanceof Aftrekking && !Double.isNaN(e1.kind1.geefWaarde().doubleValue()) && 
					e1.kind1.geefWaarde().doubleValue()==0 && e1.kind2 instanceof Vermenigvuldiging)
			{	d = -e1.kind2.kind1.geefWaarde().doubleValue() / e2.geefWaarde().doubleValue();
				double dn = -e2.geefWaarde().doubleValue() / e1.kind2.kind1.geefWaarde().doubleValue();
				//if(d==0)uitv = new BasisExpressie("0");
				if(d==1)uitv = e1.kind2.kind2;
				else if(d==-1)uitv = new Aftrekking(new BasisExpressie("0"),e1.kind2.kind2);
				else if(d>0 && Expressie.isInteger(d))uitv = new Vermenigvuldiging(new BasisExpressie(Expressie.df.format(d)),e1.kind2.kind2);
				else if(d>0 && Expressie.isInteger(dn))uitv = new Deling(e1.kind2.kind2,new BasisExpressie(Expressie.df.format(dn)));
				else if(d<0 && Expressie.isInteger(d))uitv = new Aftrekking(new BasisExpressie("0"),new Vermenigvuldiging(new BasisExpressie(Expressie.df.format(-d)),e1.kind2.kind2));
				else if(d<0 && Expressie.isInteger(dn))uitv = new Deling(e1.kind2.kind2,new BasisExpressie(Expressie.df.format(dn)));
			
				//else if(d<0)uitv = new Aftrekking(new BasisExpressie("0"),new Vermenigvuldiging(new BasisExpressie(Expressie.df.format(-d)),e1.kind2.kind2));
				//else uitv = new Vermenigvuldiging(new BasisExpressie(Expressie.df.format(d)),e1.kind2.kind2);
				else uitv = new Deling(e1,e2);
				return uitv;
			}
			else if(e1 instanceof Deling && !Double.isNaN(e1.kind2.geefWaarde().doubleValue()))
			{	d = e2.geefWaarde().doubleValue() * e1.kind2.geefWaarde().doubleValue();
				//if(d==0)uitv = new BasisExpressie("0");
				if(d==1)uitv = e1.kind1;
				else if(d==-1)uitv = new Aftrekking(new BasisExpressie("0"),e1.kind1);
				//else if(d<0)uitv = new Aftrekking(new BasisExpressie("0"),new Vermenigvuldiging(new BasisExpressie(Expressie.df.format(-d)),e1.kind1));
				else uitv = new Deling(e1.kind1,new BasisExpressie(Expressie.df.format(d)));
				return uitv;
			}
			else if(e1 instanceof Deling && !Double.isNaN(e1.kind2.geefWaarde().doubleValue()))
			{	d = e1.kind1.geefWaarde().doubleValue() / e2.geefWaarde().doubleValue();
				double dn = e2.geefWaarde().doubleValue() / e1.kind1.geefWaarde().doubleValue();
				//if(d==0)uitv = new BasisExpressie("0");
				if(Expressie.isInteger(d))uitv = new Deling(new BasisExpressie(Expressie.df.format(d)),e1.kind2);
				else if(Expressie.isInteger(dn))uitv = new Deling(new BasisExpressie("1"),new Vermenigvuldiging(new BasisExpressie(Expressie.df.format(dn)),e1.kind2));
				//else if(d<0)uitv = new Aftrekking(new BasisExpressie("0"),new Vermenigvuldiging(new BasisExpressie(Expressie.df.format(-d)),e1.kind1));
				//else uitv = new Vermenigvuldiging(new BasisExpressie(Expressie.df.format(d)),e1.kind1);
				else uitv = new Deling(e1.kind1,new Vermenigvuldiging(e2,e1.kind2));
				return uitv;
			}
			/*else if(e1 instanceof Deling && e1.kind2.geefWaarde()==null)
			{	d = e1.kind1.geefWaarde().doubleValue() /e2.geefWaarde().doubleValue();
				if(d==0)uitv = new BasisExpressie("0");
				else uitv = new Deling(new BasisExpressie(Expressie.df.format(d)),e1.kind2);
				//else if(d==1)
				//else if(d==-1)uitv = new Aftrekking(new BasisExpressie("0"),e1.kind1);
				//else if(d<0)uitv = new Aftrekking(new BasisExpressie("0"),new Vermenigvuldiging(new BasisExpressie(Expressie.df.format(-d)),e1.kind1));
				//else uitv = new Vermenigvuldiging(new BasisExpressie(Expressie.df.format(d)),e1.kind1);
				return uitv;
			}*/
			else
			{	if(e2.geefWaarde().doubleValue()==1)uitv = e1;
				else if(e2.geefWaarde().doubleValue()==-1)uitv = new Aftrekking(new BasisExpressie("0"),e1);
				//else if(e2.geefWaarde().doubleValue()<0)uitv = new Aftrekking(new BasisExpressie("0"),new Vermenigvuldiging(new BasisExpressie(Expressie.df.format(-e2.geefWaarde().doubleValue())),e1));
				else uitv = new Deling(e1,e2);
				return uitv;
			}
			
			//if(e2.geefWaarde().doubleValue()==1)uitv = e1;
			//else uitv = new Deling(e1,e2);
			//return uitv;
		}
		else
		{	Expressie uitv = new Expressie();
			if(pijlIn1==null)return null;
			Expressie e1 = pijlIn1.zender.geefUitvoer(max-1);
			Expressie e2 = beginw;
			if(e1==null)return null;
			if(e2.geefWaarde().doubleValue()==1)uitv = e1;
			else uitv = new Deling(e1,e2);
			return uitv;
		}
	}
	
	public Expressie geefVerborgenUitvoer(int max)
	{	Expressie uitv = new Expressie();
		if(pijlIn1==null)return null;
		Expressie e1 = pijlIn1.zender.geefVerborgenUitvoer(max-1);
		Expressie e2 = beginw;
		if(e1==null)return null;
		if(e2.geefWaarde().doubleValue()==1)uitv = e1;
		else uitv = new Deling(e1,e2);
		return uitv;
	}
}
