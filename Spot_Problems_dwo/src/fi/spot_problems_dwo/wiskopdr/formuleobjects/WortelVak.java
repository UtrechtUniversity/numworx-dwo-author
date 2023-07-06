package fi.spot_problems_dwo.wiskopdr.formuleobjects;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;


public class WortelVak extends RegelVak
{	
	public WortelVak(FormuleVak  fv)
	{	formuleVak = fv;
		
		setLayout(null);
		super.setFont(fv.getFont());
		fm = getFontMetrics(getFont());
			
		setSize(4*fm.getAscent()/3,5*fm.getAscent()/4 + fm.getDescent());
		ashoogte = 3*fm.getAscent()/4;
		kind1 = new FormuleRegel(formuleVak);
		kind1.setLocation(5*fm.getAscent()/7,fm.getAscent()/4);
		add(kind1);
	}
	
	public void setFont(Font f)
	{	super.setFont(f);
		fm = getFontMetrics(getFont());
		setSize(5*fm.getAscent()/6 + kind1.getSize().width, fm.getAscent()/4 + kind1.getSize().height);
		ashoogte = kind1.ashoogte + fm.getAscent()/4 ;
		kind1.setFont(f);
		kind1.setLocation(5*fm.getAscent()/7,fm.getAscent()/4);
	}
	
	public void paint(Graphics g)
	{	if(selected)
		{	g.setColor(Color.black);
			g.fillRect(0,0,getSize().width,getSize().height);
		}
		if(selected)g.setColor(Color.white);
		else g.setColor(Color.black);
		g.drawLine(0,2*getSize().height/3,fm.getAscent()/3,getSize().height);
		g.drawLine(1,2*getSize().height/3,fm.getAscent()/3+1,getSize().height);
		g.drawLine(fm.getAscent()/3,getSize().height,2*fm.getAscent()/3-1,fm.getAscent()/8);
		g.drawLine(2*fm.getAscent()/3,fm.getAscent()/8,getSize().width,fm.getAscent()/8);
		super.paint(g);
	}
		
	public void zetMaat()
	{	setSize(5*fm.getAscent()/6 + kind1.getSize().width, fm.getAscent()/4 + kind1.getSize().height);
		ashoogte = kind1.ashoogte + fm.getAscent()/4 ;
		if(getParent()instanceof FormuleElement)((FormuleElement)getParent()).zetMaat();
	}
	
	public String toString()
	{	return "$w" + kind1.toString() + "@";
	}
}

