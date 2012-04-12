package fi.algebraexpressies;

import java.awt.Polygon;
import java.awt.*;
import java.awt.event.*;
import fi.algebraexpressies.expressies_ap.*;

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
		
		g.setColor(Color.orange);
		g.fillRect(0,10,getSize().width-1,getSize().height-11);
		g.setColor(Color.black);
		g.drawRect(0,10,getSize().width-1,getSize().height-11);
		
		String s1 = "...";
		Font f1 = new Font("SansSerrif",Font.PLAIN,14);
		g.setFont(f1);
		g.drawString(s1,14,getSize().height-4);
			
		(new Wortelteken(20,12)).paint(g, 7,getSize().height-15);
	}
	
	
	public Expressie geefUitvoer(int max)
	{	Expressie uitv = new Expressie();
		if(pijlIn1==null  || max<0)return null;
		Expressie e1 = pijlIn1.zender.geefUitvoer(max-1);
		if(e1==null)return null;
		uitv = new Wortel(e1);
		return uitv;
	}
	
	public Expressie geefVerborgenUitvoer(int max)
	{	Expressie uitv = new Expressie();
		if(pijlIn1==null  || max<0)return null;
		Expressie e1 = pijlIn1.zender.geefVerborgenUitvoer(max-1);
		if(e1==null)return null;
		uitv = new Wortel(e1);
		return uitv;
	}	
}
