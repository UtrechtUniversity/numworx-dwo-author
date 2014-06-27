package fi.wiskopdr.formuleobjects;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;


public class BinVak extends RegelVak
{	
	public BinVak(FormuleVak  fv)
	{	formuleVak = fv;
		
		setLayout(null);
		
		super.setFont(fv.getFont());
		fm = getFontMetrics(getFont());
			
		setSize(5*fm.getAscent()/5,4*fm.getAscent()/3 + fm.getDescent());
		ashoogte = fm.getAscent()/2;
		
		kind1 = new FormuleRegel(formuleVak);
		kind1.setLocation(5*fm.getAscent()/12,fm.getAscent()/12);
		add(kind1);
		
		kind2 = new FormuleRegel(formuleVak);
		kind2.setLocation(5*fm.getAscent()/12, fm.getAscent()/6 + kind1.getSize().height);
		add(kind2);
		
		setOpaque(false);
	}
	
	public void setFont(Font f)
	{	super.setFont(f);
		fm = getFontMetrics(getFont());
		if(kind1==null)return;
		int maxWidth = Math.max(kind1.getSize().width, kind2.getSize().width);
		setSize(5*fm.getAscent()/6 + maxWidth, 3*fm.getAscent()/12 + kind1.getSize().height + kind2.getSize().height);
		ashoogte = -fm.getAscent()/8 + kind1.getSize().height;
		kind1.setLocation(5*fm.getAscent()/12 + (maxWidth-kind1.getSize().width)/2, fm.getAscent()/12);
		kind2.setLocation(5*fm.getAscent()/12 + (maxWidth-kind2.getSize().width)/2, fm.getAscent()/6 + kind1.getSize().height);
		kind1.setFont(f);
		kind2.setFont(f);
	}
	
	public void paint(Graphics g)
	{	//zetMaat();
		if(selected)
		{	g.setColor(Color.black);
			g.fillRect(0,0,getSize().width,getSize().height);
		}
		if(selected)g.setColor(Color.white);
		else g.setColor(fgColor);
		
		int hoogte = getSize().height;
		int breedte = getSize().width;
		int h =3*fm.getAscent()/2;
		int hh = h/2;
		int b = h/6;
		int bb = b/2;
		
		int c = fm.getAscent()/6;
		int d = fm.getAscent()/8;
		
		g.drawLine(c+b, d, c+b-bb, d+bb);
		g.drawLine(c+b-bb, d+bb, c, d+hh-b);
		g.drawLine(c, d+hh-b, c, hoogte-hh+b-d);		
		g.drawLine(c+b-bb, hoogte-bb-d, c, hoogte-hh+b-d);
		g.drawLine(c+b, hoogte-d, c+b-bb, hoogte-bb-d);
		
		g.drawLine(breedte-b-1-c, d, breedte-b+bb-1-c, d+bb);
		g.drawLine(breedte-b+bb-1-c, d+bb, breedte-1-c, d+hh-b);
		g.drawLine(breedte-1-c, d+hh-b, breedte-1-c, hoogte-hh+b-d);		
		g.drawLine(breedte-b+bb-1-c, hoogte-bb-d, breedte-1-c, hoogte-hh+b-d);
		g.drawLine(breedte-b-1-c, hoogte-d, breedte-b+bb-1-c, hoogte-bb-d);
		
		super.paint(g);
	}
	
	public void zetMaat()
	{	
		int maxWidth = Math.max(kind1.getSize().width, kind2.getSize().width);
		kind1.setLocation(5*fm.getAscent()/12 + (maxWidth-kind1.getSize().width)/2, fm.getAscent()/12);
		kind2.setLocation(5*fm.getAscent()/12 + (maxWidth-kind2.getSize().width)/2, fm.getAscent()/6 + kind1.getSize().height);
		setSize(5*fm.getAscent()/6 + maxWidth, 3*fm.getAscent()/12 + kind1.getSize().height + kind2.getSize().height);
		ashoogte = -fm.getAscent()/8 + kind1.getSize().height;
		
		
		if(getParent()instanceof FormuleElement)((FormuleElement)getParent()).zetMaat();
	}
	
	
	public String toString()
	{	return "$y" + kind1.toString() + "$n"+ kind2.toString() + "@@";
	}
}

