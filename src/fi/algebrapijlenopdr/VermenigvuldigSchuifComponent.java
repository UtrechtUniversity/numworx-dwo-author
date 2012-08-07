package fi.algebrapijlenopdr;

import java.awt.Polygon;
import java.awt.*;
import java.awt.event.*;
import fi.algebrapijlenopdr.expressies_ap.*;

public class VermenigvuldigSchuifComponent extends BewerkingSchuifComponent 
{	
	public VermenigvuldigSchuifComponent(AlgebraSchuifVeld asv,int x, int y, int b, int h)
	{	super(asv,x,y,b,h);
	
		tf.setText("3");
	}
	
	public void paint(Graphics g)
  	{ 	super.paint(g);
		
		g.setColor(Color.black);
		String s;
		if (beginw.geefWaarde().doubleValue() < 0)
			s = "x  " + Expressie.df.format(beginw.geefWaarde());
		else 
			s = "x " + Expressie.df.format(beginw.geefWaarde());
		Font f = new Font("SansSerrif",Font.PLAIN,14);
		g.setFont(f);
		FontMetrics fm = g.getFontMetrics();
		int w = fm.stringWidth(s); 
		int sccrollCorr = 0;
		if (scrollable) sccrollCorr = 10;
		
		if (!tf.isVisible())
		{	
			if(!links)g.drawString(s,5+(getSize().width-w-sccrollCorr)/2,getSize().height-4);
			else g.drawString(s,-5+(getSize().width-w-sccrollCorr)/2,getSize().height-4);
		}
		else
		{
			if (!links)g.drawString("x ",5+(getSize().width-w-sccrollCorr)/2,getSize().height-4);
			else g.drawString("x ",-5+(getSize().width-w-sccrollCorr)/2,getSize().height-4);
			
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
			if (e1 instanceof Vermenigvuldiging && !Double.isNaN(e1.kind1.geefWaarde().doubleValue()))
			{	
//System.out.println("vermenigvuldiging 1");

				d = e1.kind1.geefWaarde().doubleValue() * e2.geefWaarde().doubleValue();
				//if(d==0)uitv = new BasisExpressie("0");
				if (d == 1)
					uitv = e1.kind2;
				else if (d == -1)
					uitv = new Aftrekking(new BasisExpressie("0"), e1.kind2);
				else if (d < 0)
					uitv = new Aftrekking(new BasisExpressie("0"),
							new Vermenigvuldiging(new BasisExpressie(Expressie.df.format(-d)), e1.kind2));
				else 
					uitv = new Vermenigvuldiging(new BasisExpressie(Expressie.df.format(d)),e1.kind2);
				return uitv;
			}
			else if (e1 instanceof Vermenigvuldiging && !Double.isNaN(e1.kind2.geefWaarde().doubleValue()))
			{	
System.out.println("vermenigvuldiging 1A");

				d = e1.kind2.geefWaarde().doubleValue() * e2.geefWaarde().doubleValue();
				//if(d==0)uitv = new BasisExpressie("0");
				if (d == 1)
					uitv = e1.kind1;
				else if (d == -1)
					uitv = new Aftrekking(new BasisExpressie("0"), e1.kind1);
				else if (d < 0)
					uitv = new Aftrekking(new BasisExpressie("0"),
							new Vermenigvuldiging(new BasisExpressie(Expressie.df.format(-d)), e1.kind1));
				else 
					uitv = new Vermenigvuldiging(new BasisExpressie(Expressie.df.format(d)),e1.kind1);
				return uitv;
			}
			
			
			
			
			else if (e1 instanceof Aftrekking && !Double.isNaN(e1.kind1.geefWaarde().doubleValue()) && 
					 e1.kind1.geefWaarde().doubleValue() == 0 && e1.kind2 instanceof Vermenigvuldiging &&
					 !Double.isNaN(e1.kind2.kind1.geefWaarde().doubleValue()))
			{	
//System.out.println("aftrekking 1");				
				
				d = - e1.kind2.kind1.geefWaarde().doubleValue() * e2.geefWaarde().doubleValue();
				//if(d==0)uitv = new BasisExpressie("0");
				if (d == 1)
					uitv = e1.kind2.kind2;
				else if (d == -1)
					uitv = new Aftrekking(new BasisExpressie("0"), e1.kind2.kind2);
				else if (d < 0)
					uitv = new Aftrekking(new BasisExpressie("0"),
							              new Vermenigvuldiging(new BasisExpressie(Expressie.df.format(- d)), e1.kind2.kind2));
				else 
					uitv = new Vermenigvuldiging(new BasisExpressie(Expressie.df.format(d)), e1.kind2.kind2);
				return uitv;
			}
			else if (e1 instanceof Aftrekking && !Double.isNaN(e1.kind1.geefWaarde().doubleValue()) && 
					 e1.kind1.geefWaarde().doubleValue() == 0 && e1.kind2 instanceof Vermenigvuldiging &&
					 !Double.isNaN(e1.kind2.kind2.geefWaarde().doubleValue()))
			{	
//System.out.println("aftrekking 1AA");				
				
				d = - e1.kind2.kind2.geefWaarde().doubleValue() * e2.geefWaarde().doubleValue();
				//if(d==0)uitv = new BasisExpressie("0");
				if (d == 1)
					uitv = e1.kind2.kind1;
				else if (d == -1)
					uitv = new Aftrekking(new BasisExpressie("0"), e1.kind2.kind1);
				else if (d < 0)
					uitv = new Aftrekking(new BasisExpressie("0"),
							              new Vermenigvuldiging(new BasisExpressie(Expressie.df.format(- d)), e1.kind2.kind1));
				else 
					uitv = new Vermenigvuldiging(new BasisExpressie(Expressie.df.format(d)), e1.kind2.kind1);
				return uitv;
			}
			else if (e1 instanceof Aftrekking && !Double.isNaN(e1.kind1.geefWaarde().doubleValue()) && 
					 e1.kind1.geefWaarde().doubleValue() == 0 && e1.kind2 instanceof Deling &&
					 e2.geefWaarde().doubleValue() == -1)
			{	
//System.out.println("aftrekking 1A");

				uitv = e1.kind2;
				
				return uitv;
			}
			else if (e1 instanceof Aftrekking && !Double.isNaN(e1.kind1.geefWaarde().doubleValue()) && 
					 e1.kind1.geefWaarde().doubleValue() == 0 && e1.kind2 instanceof Wortel &&
					 e2.geefWaarde().doubleValue() == -1)
			{	
//System.out.println("aftrekking 1B");

				uitv = e1.kind2;
				
				return uitv;
			}
			else if (e1 instanceof Aftrekking && !Double.isNaN(e1.kind1.geefWaarde().doubleValue()) && 
					 e1.kind1.geefWaarde().doubleValue() == 0 && e1.kind2 instanceof Macht &&
					 e2.geefWaarde().doubleValue() == -1)
			{	
//System.out.println("aftrekking 1C");

				uitv = e1.kind2;
				
				return uitv;
			}
			
			else if (e1 instanceof Aftrekking && !Double.isNaN(e1.kind1.geefWaarde().doubleValue()) && 
					 e1.kind1.geefWaarde().doubleValue() == 0)
			{	
//System.out.println("aftrekking 2");
//System.out.println("e2 = " + e2.toString());
//System.out.println("" + e2.geefWaarde().doubleValue());

				d = -1.0 * e2.geefWaarde().doubleValue();
				//if(d==0)uitv = new BasisExpressie("0");
				if (d == 1)
					uitv = e1.kind2.kind2;
				else if (d == -1)
					uitv = new Aftrekking(new BasisExpressie("0"), e1.kind2);
				else if (d < 0)
					uitv = new Aftrekking(new BasisExpressie("0"),
							              new Vermenigvuldiging(new BasisExpressie(Expressie.df.format(-d)), e1.kind2));
				else 
					uitv = new Vermenigvuldiging(new BasisExpressie(Expressie.df.format(d)), e1.kind2);
				return uitv;
			}
			else if(e1 instanceof Deling && !Double.isNaN(e1.kind2.geefWaarde().doubleValue()))
			{	
//System.out.println("deling 1");				
				d = e2.geefWaarde().doubleValue() / e1.kind2.geefWaarde().doubleValue();
				double dn = e1.kind2.geefWaarde().doubleValue() / e2.geefWaarde().doubleValue();
				//if(d==0)uitv = new BasisExpressie("0");
				if(d==1)uitv = e1.kind1;
				else if(d==-1)uitv = new Aftrekking(new BasisExpressie("0"),e1.kind1);
				else if(d>0 && Expressie.isInteger(d))uitv = new Vermenigvuldiging(new BasisExpressie(Expressie.df.format(d)),e1.kind1);
				else if(d>0 && Expressie.isInteger(dn))uitv = new Deling(e1.kind1,new BasisExpressie(Expressie.df.format(dn)));
				else if(d<0 && Expressie.isInteger(d))uitv = new Vermenigvuldiging(new BasisExpressie(Expressie.df.format(d)),e1.kind1);
				else if(d<0 && Expressie.isInteger(dn))uitv = new Deling(e1.kind1,new BasisExpressie(Expressie.df.format(dn)));
				//else if(d<0)uitv = new Aftrekking(new BasisExpressie("0"),new Vermenigvuldiging(new BasisExpressie(Expressie.df.format(-d)),e1.kind1));
				//else uitv = new Vermenigvuldiging(new BasisExpressie(Expressie.df.format(d)),e1.kind1);
				else uitv = new Deling(new Vermenigvuldiging(e2,e1.kind1),e1.kind2);
				return uitv;
			}
			else if(e1 instanceof Deling && !Double.isNaN(e1.kind2.geefWaarde().doubleValue()))
			{	
//System.out.println("deling 2");				
				d = e2.geefWaarde().doubleValue() * e1.kind1.geefWaarde().doubleValue();
				//if(d==0)uitv = new BasisExpressie("0");
				uitv = new Deling(new BasisExpressie(Expressie.df.format(d)),e1.kind2);
				//else if(d==1)
				//else if(d==-1)uitv = new Aftrekking(new BasisExpressie("0"),e1.kind1);
				//else if(d<0)uitv = new Aftrekking(new BasisExpressie("0"),new Vermenigvuldiging(new BasisExpressie(Expressie.df.format(-d)),e1.kind1));
				//else uitv = new Vermenigvuldiging(new BasisExpressie(Expressie.df.format(d)),e1.kind1);
				return uitv;
			}
			else
			{	
//System.out.println("else");				
				if (e2.geefWaarde().doubleValue() == 1)
					uitv = e1;
				else if (e2.geefWaarde().doubleValue() == -1)
					uitv = new Aftrekking(new BasisExpressie("0"), e1);
				else if (e2.geefWaarde().doubleValue() < 0)
					uitv = new Aftrekking(new BasisExpressie("0"),
							              new Vermenigvuldiging(
							            		  new BasisExpressie(Expressie.df.format(-e2.geefWaarde().doubleValue())),e1));
				else 
					uitv = new Vermenigvuldiging(e2, e1);
				return uitv;
			}
			/*if(d!=1)
			{	e2 = new BasisExpressie(Expressie.df.format(d));
				uitv = new Optelling(e1.kind1,e2);
			}
			else if(d<0)
			{	e2 = new BasisExpressie(Expressie.df.format(-d));
				uitv = new Aftrekking(e1.kind1,e2);
			}
			else uitv = e1.kind1;
			//return uitv;
			
			
			
			if(e2.geefWaarde().doubleValue()==0)uitv = new BasisExpressie("0");
			else if(e2.geefWaarde().doubleValue()==1)uitv = e1;
			else if(e2.geefWaarde().doubleValue()==-1)uitv = new Aftrekking(new BasisExpressie("0"),e1);
			else if(e2.geefWaarde().doubleValue()<0)uitv = new Aftrekking(new BasisExpressie("0"),new Vermenigvuldiging(new BasisExpressie(Expressie.df.format(-e2.geefWaarde().doubleValue())),e1));
			else uitv = new Vermenigvuldiging(e2,e1);
			return uitv;*/
		}
		else
		{	Expressie uitv = new Expressie();
			if(pijlIn1==null)return null;
			Expressie e1 = pijlIn1.zender.geefUitvoer(max-1);
			Expressie e2 = beginw;
			if(e1==null)return null;
			//if(e2.geefWaarde().doubleValue()==0)uitv = new BasisExpressie("0");
			else if(e2.geefWaarde().doubleValue()==1)uitv = e1;
			else if(e2.geefWaarde().doubleValue()==-1)uitv = new Aftrekking(new BasisExpressie("0"),e1);
			else if(e2.geefWaarde().doubleValue()<0)uitv = new Aftrekking(new BasisExpressie("0"),
					new Vermenigvuldiging(new BasisExpressie(Expressie.df.format(-e2.geefWaarde().doubleValue())),e1));
			else uitv = new Vermenigvuldiging(e2,e1);
			return uitv;
		}
	}
	
	public Expressie geefVerborgenUitvoer(int max)
	{	Expressie uitv = new Expressie();
		if(pijlIn1==null)return null;
		Expressie e1 = pijlIn1.zender.geefVerborgenUitvoer(max-1);
		Expressie e2 = beginw;
		if(e1==null)return null;
		
		double d = 1;
		if(e1 instanceof Vermenigvuldiging)
		{	d = e1.kind1.geefWaarde().doubleValue() * e2.geefWaarde().doubleValue();
			//if(d==0)uitv = new BasisExpressie("0");
			if(d==1)uitv = e1.kind2;
			else if(d==-1)uitv = new Aftrekking(new BasisExpressie("0"),e1.kind2);
			else if(d<0)uitv = new Aftrekking(new BasisExpressie("0"),new Vermenigvuldiging(new BasisExpressie(Expressie.df.format(-d)),e1.kind2));
			else uitv = new Vermenigvuldiging(new BasisExpressie(Expressie.df.format(d)),e1.kind2);
			return uitv;
		}
		else if(e1 instanceof Aftrekking && !Double.isNaN(e1.kind1.geefWaarde().doubleValue()) && 
				e1.kind1.geefWaarde().doubleValue()==0 && e1.kind2 instanceof Vermenigvuldiging)
		{	d = -e1.kind2.kind1.geefWaarde().doubleValue() * e2.geefWaarde().doubleValue();
			//if(d==0)uitv = new BasisExpressie("0");
			if(d==1)uitv = e1.kind2.kind2;
			else if(d==-1)uitv = new Aftrekking(new BasisExpressie("0"),e1.kind2.kind2);
			else if(d<0)uitv = new Aftrekking(new BasisExpressie("0"),new Vermenigvuldiging(new BasisExpressie(Expressie.df.format(-d)),e1.kind2.kind2));
			else uitv = new Vermenigvuldiging(new BasisExpressie(Expressie.df.format(d)),e1.kind2.kind2);
			return uitv;
		}
		else if(e1 instanceof Aftrekking && !Double.isNaN(e1.kind1.geefWaarde().doubleValue()) && 
				e1.kind1.geefWaarde().doubleValue()==0)
		{	d = -1.0 * e2.geefWaarde().doubleValue();
			//if(d==0)uitv = new BasisExpressie("0");
			if(d==1)uitv = e1.kind2.kind2;
			else if(d==-1)uitv = new Aftrekking(new BasisExpressie("0"),e1.kind2);
			else if(d<0)uitv = new Aftrekking(new BasisExpressie("0"),new Vermenigvuldiging(new BasisExpressie(Expressie.df.format(-d)),e1.kind2));
			else uitv = new Vermenigvuldiging(new BasisExpressie(Expressie.df.format(d)),e1.kind2);
			return uitv;
		}
		else if(e1 instanceof Deling && !Double.isNaN(e1.kind2.geefWaarde().doubleValue()))
		{	d = e2.geefWaarde().doubleValue() / e1.kind2.geefWaarde().doubleValue();
			double dn = e1.kind2.geefWaarde().doubleValue() / e2.geefWaarde().doubleValue();
			//if(d==0)uitv = new BasisExpressie("0");
			if(d==1)uitv = e1.kind1;
			else if(d==-1)uitv = new Aftrekking(new BasisExpressie("0"),e1.kind1);
			else if(d>0 && Expressie.isInteger(d))uitv = new Vermenigvuldiging(new BasisExpressie(Expressie.df.format(d)),e1.kind1);
			else if(d>0 && Expressie.isInteger(dn))uitv = new Deling(e1.kind1,new BasisExpressie(Expressie.df.format(dn)));
			else if(d<0 && Expressie.isInteger(d))uitv = new Vermenigvuldiging(new BasisExpressie(Expressie.df.format(d)),e1.kind1);
			else if(d<0 && Expressie.isInteger(dn))uitv = new Deling(e1.kind1,new BasisExpressie(Expressie.df.format(dn)));
			//else if(d<0)uitv = new Aftrekking(new BasisExpressie("0"),new Vermenigvuldiging(new BasisExpressie(Expressie.df.format(-d)),e1.kind1));
			//else uitv = new Vermenigvuldiging(new BasisExpressie(Expressie.df.format(d)),e1.kind1);
			else uitv = new Deling(new Vermenigvuldiging(e2,e1.kind1),e1.kind2);
			return uitv;
		}
		else if(e1 instanceof Deling && !Double.isNaN(e1.kind2.geefWaarde().doubleValue()))
		{	d = e2.geefWaarde().doubleValue() * e1.kind1.geefWaarde().doubleValue();
			//if(d==0)uitv = new BasisExpressie("0");
			uitv = new Deling(new BasisExpressie(Expressie.df.format(d)),e1.kind2);
			//else if(d==1)
			//else if(d==-1)uitv = new Aftrekking(new BasisExpressie("0"),e1.kind1);
			//else if(d<0)uitv = new Aftrekking(new BasisExpressie("0"),new Vermenigvuldiging(new BasisExpressie(Expressie.df.format(-d)),e1.kind1));
			//else uitv = new Vermenigvuldiging(new BasisExpressie(Expressie.df.format(d)),e1.kind1);
			return uitv;
		}
		else
		{	if(e2.geefWaarde().doubleValue()==1)uitv = e1;
			else if(e2.geefWaarde().doubleValue()==-1)uitv = new Aftrekking(new BasisExpressie("0"),e1);
			else if(e2.geefWaarde().doubleValue()<0)uitv = new Aftrekking(new BasisExpressie("0"),new Vermenigvuldiging(new BasisExpressie(Expressie.df.format(-e2.geefWaarde().doubleValue())),e1));
			else uitv = new Vermenigvuldiging(e2,e1);
			return uitv;
		}
		/*if(d!=1)
		{	e2 = new BasisExpressie(Expressie.df.format(d));
			uitv = new Optelling(e1.kind1,e2);
		}
		else if(d<0)
		{	e2 = new BasisExpressie(Expressie.df.format(-d));
			uitv = new Aftrekking(e1.kind1,e2);
		}
		else uitv = e1.kind1;
		//return uitv;
		
		
		
		if(e2.geefWaarde().doubleValue()==0)uitv = new BasisExpressie("0");
		else if(e2.geefWaarde().doubleValue()==1)uitv = e1;
		else if(e2.geefWaarde().doubleValue()==-1)uitv = new Aftrekking(new BasisExpressie("0"),e1);
		else if(e2.geefWaarde().doubleValue()<0)uitv = new Aftrekking(new BasisExpressie("0"),new Vermenigvuldiging(new BasisExpressie(Expressie.df.format(-e2.geefWaarde().doubleValue())),e1));
		else uitv = new Vermenigvuldiging(e2,e1);
		return uitv;*/
	}
}
