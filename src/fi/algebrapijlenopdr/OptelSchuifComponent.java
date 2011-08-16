package fi.algebrapijlenopdr;

import java.awt.Polygon;
import java.awt.*;
import java.awt.event.*;
import fi.algebrapijlenopdr.expressies_ap.*;

public class OptelSchuifComponent extends BewerkingSchuifComponent 
{	
	
	public OptelSchuifComponent(AlgebraSchuifVeld asv,int x, int y, int b, int h)
	{	super(asv,x,y,b,h);
		tf.setText("3");
	}
	
	public void paint(Graphics g)
  	{ 	super.paint(g);
				
		g.setColor(Color.black);
		String s = "+ " + Expressie.df.format(beginw.geefWaarde());
		
		Font f = new Font("SansSerrif",Font.PLAIN,14);
		g.setFont(f);
		FontMetrics fm = g.getFontMetrics();
		int w = fm.stringWidth(s); 
		int sccrollCorr = 0;
		if (scrollable)
			sccrollCorr = 10;
		
		if (!tf.isVisible())
		{	
			if (!links)
				g.drawString(s,5+(getSize().width-w-sccrollCorr)/2,getSize().height-4);
			else 
				g.drawString(s,-5+(getSize().width-w-sccrollCorr)/2,getSize().height-4);
		}
		else
		{	
			if (!links)
				g.drawString("+ ",5+(getSize().width-w-sccrollCorr)/2,getSize().height-4);
			else 
				g.drawString("+ ",-5+(getSize().width-w-sccrollCorr)/2,getSize().height-4);
		}
			
	}
	
	
	public Expressie geefUitvoer(int max)
	{	if(AlgebraPijlenOpdr.simplify)
		{	Expressie uitv = new Expressie();
			if(pijlIn1==null)return null;
			Expressie e1 = pijlIn1.zender.geefUitvoer(max-1);
			Expressie e2 = beginw;
			if(e1==null)return null;
			//if(e2.geefWaarde().doubleValue()==0)uitv = e1;
			double d = 0;
			if(e1 instanceof Optelling)
			{	d = 	e1.kind2.geefWaarde().doubleValue() + e2.geefWaarde().doubleValue();
			}
			else if(e1 instanceof Aftrekking && e1.kind1.geefWaarde()==null)
			{	d = 	-e1.kind2.geefWaarde().doubleValue() + e2.geefWaarde().doubleValue();
			}
			else
			{	if(e2.geefWaarde().doubleValue()==0)uitv = e1;
				else uitv = new Optelling(e1,e2);
				return uitv;
			}
			if(d>0)
			{	e2 = new BasisExpressie(Expressie.df.format(d));
				uitv = new Optelling(e1.kind1,e2);
			}
			else if(d<0)
			{	e2 = new BasisExpressie(Expressie.df.format(-d));
				uitv = new Aftrekking(e1.kind1,e2);
			}
			else uitv = e1.kind1;
			return uitv;
		}
		else
		{	Expressie uitv = new Expressie();
			if(pijlIn1==null)return null;
			Expressie e1 = pijlIn1.zender.geefUitvoer(max-1);
			Expressie e2 = beginw;
			if(e1==null)return null;
			if(e2.geefWaarde().doubleValue()==0)uitv = e1;
			else uitv = new Optelling(e1,e2);
			return uitv;
		}
	}
	
	public Expressie geefVerborgenUitvoer(int max)
	{	Expressie uitv = new Expressie();
		if(pijlIn1==null)return null;
		Expressie e1 = pijlIn1.zender.geefVerborgenUitvoer(max-1);
		Expressie e2 = beginw;
		if(e1==null)return null;
		if(e2.geefWaarde().doubleValue()==0)uitv = e1;
		else uitv = new Optelling(e1,e2);
		return uitv;
	}
}
