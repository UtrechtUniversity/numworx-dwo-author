package fi.algebrapijlenopdr;

import java.awt.Polygon;
import java.awt.*;
import java.awt.event.*;
import fi.algebrapijlenopdr.expressies_ap.*;

public class WortelSchuifComponent extends AlgebraSchuifComponent 
{	
	Expressie waarde;
	Font f;
	FontMetrics fm;
	
	public WortelSchuifComponent(AlgebraSchuifVeld asv,int x, int y, int b, int h)
	{	super(1,asv,x,y,b,h);
		
		f = new Font("SansSerrif",Font.PLAIN,14);
		fm = getFontMetrics(f);
	}
	
	public void paint(Graphics g)
  	{ 	super.paint(g);
		
		if(!links)
		{	g.setColor(Color.orange);
			g.fillRoundRect(10,0,getSize().width-11,getSize().height-1,8,8);
			g.setColor(Color.black);
			g.drawRoundRect(10,0,getSize().width-11,getSize().height-1,8,8);
		}
		else
		{	g.setColor(Color.orange);
			g.fillRoundRect(0,0,getSize().width-11,getSize().height-1,8,8);
			g.setColor(Color.black);
			g.drawRoundRect(0,0,getSize().width-11,getSize().height-1,8,8);
		}	
		
		String s1 = "...";
		Font f1 = new Font("SansSerrif",Font.PLAIN,14);
		g.setFont(f1);
		if(!links)
		{	g.drawString(s1,24,getSize().height-4);
			(new Wortelteken(20,12)).paint(g, 17,getSize().height-15);
		}
		else 
		{	g.drawString(s1,14,getSize().height-4);
			(new Wortelteken(20,12)).paint(g, 7,getSize().height-15);
		}
	}
	
	
	public Expressie geefUitvoer(int max)
	{	if (AlgebraPijlenOpdr.simplify)
		{	Expressie uitv = new Expressie();
			if (pijlIn1==null  || max < 0)
				return null;
			Expressie e1 = pijlIn1.zender.geefUitvoer(max - 1);
			if (e1 == null)
				return null;
			if ((e1 instanceof Macht) && (e1.kind2 instanceof BasisExpressie) && 
				!Double.isNaN(e1.kind2.geefWaarde().doubleValue()) && (e1.kind2.geefWaarde().doubleValue() == 2))
				uitv = e1.kind1;
			else	
				uitv = new Wortel(e1);
			return uitv;
		}
		else
		{	Expressie uitv = new Expressie();
			if(pijlIn1==null  || max<0)return null;
			Expressie e1 = pijlIn1.zender.geefUitvoer(max-1);
			if(e1==null)return null;
			uitv = new Wortel(e1);
			return uitv;
		}
	}
	
	public Expressie geefVerborgenUitvoer(int max)
	{	Expressie uitv = new Expressie();
		if (pijlIn1 == null  || max < 0)
			return null;
		Expressie e1 = pijlIn1.zender.geefVerborgenUitvoer(max - 1);
		if (e1 == null)
			return null;
		uitv = new Wortel(e1);
		return uitv;
	}
}
