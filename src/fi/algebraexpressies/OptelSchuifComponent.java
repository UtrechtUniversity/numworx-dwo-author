package fi.algebraexpressies;

import java.awt.Polygon;
import java.awt.*;
import java.awt.event.*;
import fi.algebraexpressies.expressies.*;

public class OptelSchuifComponent extends AlgebraSchuifComponent 
{	
	Expressie waarde;
	Font f;
	FontMetrics fm;
	
	public OptelSchuifComponent(AlgebraSchuifVeld asv,int x, int y, int b, int h)
	{	super(2,asv,x,y,b,h);
		
		f = new Font("SansSerrif",Font.PLAIN,14);
		fm = getFontMetrics(f);
	}
	
	public void paint(Graphics g)
  	{ 	super.paint(g);
		
		g.setColor(Color.orange);
		g.fillRect(0,10,getSize().width-1,getSize().height-11);
		g.setColor(Color.black);
		g.drawRect(0,10,getSize().width-1,getSize().height-11);
		
		g.setFont(f);
		String s = "...+...";
		int w = fm.stringWidth(s); 
		g.drawString(s,(getSize().width-w)/2,getSize().height-4);
	}
	
	
	public Expressie geefUitvoer(int max)
	{	Expressie uitv = new Expressie();
		if(pijlIn1==null || pijlIn2==null || max<0)return null;
		Expressie e1 = pijlIn1.zender.geefUitvoer(max-1);
		Expressie e2 = pijlIn2.zender.geefUitvoer(max-1);
		if(e1==null || e2==null)return null;
		uitv = new Optelling(e1,e2);
		return uitv;
	}
}
