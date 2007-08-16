package fi.javalogoweb.formuleobjects;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;


public class NdeLogVak extends RegelVak
{	
	public NdeLogVak(FormuleVak  fv)
	{	formuleVak = fv;
		
		setLayout(null);
		super.setFont(fv.getFont());
		fm = getFontMetrics(getFont());
		
		Font font2 = new Font(fv.getFont().getName(),fv.getFont().getStyle(),2*fv.getFont().getSize()/3);
		fm2 = getFontMetrics(font2);
			
		setSize(4*fm.getAscent()/3,5*fm.getAscent()/4 + fm.getDescent());
		ashoogte = 3*fm.getAscent()/4;
		
		kind1 = new FormuleRegel(formuleVak);
		kind1.setLocation(12+fm.stringWidth("log("),fm.getAscent()/12);
		add(kind1);
		
		kind2 = new FormuleRegel(formuleVak);
		kind2.setFont(font2);
		kind2.setLocation(5,0);
		add(kind2);
	}
	
	public void setFont(Font f)
	{	super.setFont(f);
		fm = getFontMetrics(getFont());
		
		Font font2 = new Font(formuleVak.getFont().getName(),formuleVak.getFont().getStyle(),2*formuleVak.getFont().getSize()/3);
		fm2 = getFontMetrics(font2);
		
		setSize(5*fm.getAscent()/6 + kind1.getSize().width+5, fm.getAscent()/4 + kind1.getSize().height);
		ashoogte = kind1.ashoogte + fm.getAscent()/4 ;
		
		kind1.setFont(f);
		kind1.setLocation(12+fm.stringWidth("log("),fm.getAscent()/12);
		
		kind2.setFont(font2);
		kind2.setLocation(5,0);
	}
	
	public void paint(Graphics g)
	{	zetMaat();
		if(selected)
		{	g.setColor(Color.black);
			g.fillRect(0,0,getSize().width,getSize().height);
		}
		if(selected)g.setColor(Color.white);
		else g.setColor(Color.black);
		g.drawString("log", 5+kind2.getSize().width ,ashoogte + fm.getAscent()/2 + fm.getAscent()/12);
		
		Font font2 = new Font(formuleVak.getFont().getName(),formuleVak.getFont().getStyle(),2*formuleVak.getFont().getSize()/3);
		
		
		int hoogte = kind1.getSize().height;
		int breedte = getSize().width;
		int h =3*fm.getAscent()/2;
		int hh = h/2;
		int b = h/6;
		int bb = b/2;
		
		int c = fm.getAscent()/6;
		int d = fm.getAscent()/8;
		
		int locx = 5+kind2.getSize().width + fm.stringWidth("log");
		int locy = kind2.getSize().height/2;
		
		g.drawLine(locx+c+b, locy+d, locx+c+b-bb, locy+d+bb);
		g.drawLine(locx+c+b-bb, locy+d+bb, locx+c, locy+d+hh-b);
		g.drawLine(locx+c, locy+d+hh-b, locx+c, locy+hoogte-hh+b-d);		
		g.drawLine(locx+c+b-bb, locy+hoogte-bb-d, locx+c, locy+hoogte-hh+b-d);
		g.drawLine(locx+c+b, locy+hoogte-d, locx+c+b-bb, locy+hoogte-bb-d);
		
		g.drawLine(breedte-b-1-c, locy+d, breedte-b+bb-1-c, locy+d+bb);
		g.drawLine(breedte-b+bb-1-c, locy+d+bb, breedte-1-c, locy+d+hh-b);
		g.drawLine(breedte-1-c, locy+d+hh-b, breedte-1-c, locy+hoogte-hh+b-d);		
		g.drawLine(breedte-b+bb-1-c, locy+hoogte-bb-d, breedte-1-c, locy+hoogte-hh+b-d);
		g.drawLine(breedte-b-1-c, locy+hoogte-d, breedte-b+bb-1-c, locy+hoogte-bb-d);


		super.paint(g);
	}
		
	public void zetMaat()
	{	setSize(12+kind2.getSize().width + fm.stringWidth("log") + kind1.getSize().width + fm.getAscent()/2, kind2.getSize().height/2 + kind1.getSize().height);
		kind1.setLocation(8+kind2.getSize().width + fm.stringWidth("log") + fm.getAscent()/4, kind2.getSize().height/2);
		//kind2.setLocation(5, kind2.getSize().height/2);
		ashoogte = kind1.ashoogte+ kind2.getSize().height/2;
		kind2.setLocation(5, ashoogte-(kind2.getSize().height/2 + fm.getAscent()/2));
		
		Font font2 = new Font(formuleVak.getFont().getName(),formuleVak.getFont().getStyle(),2*formuleVak.getFont().getSize()/3);
		kind2.setFont(font2);
		for(int i=0 ; i<kind2.getComponentCount()  ; i++)
		{	if(kind2.getComponent(i) instanceof FormuleTeken)kind2.getComponent(i).setFont(font2);
		}
		
		
		if(getParent()instanceof FormuleElement)((FormuleElement)getParent()).zetMaat();
			
	}
	/*
	public void paint(Graphics g)
	{	zetMaat();
		if(selected)
		{	g.setColor(Color.black);
			g.fillRect(0,0,getSize().width,getSize().height);
		}
		if(selected)g.setColor(Color.white);
		else g.setColor(Color.black);
		
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
	{	setSize(5*fm.getAscent()/6 + kind1.getSize().width, fm.getAscent()/6 + kind1.getSize().height);
		ashoogte = kind1.ashoogte + fm.getAscent()/12;
		if(getParent()instanceof FormuleElement)((FormuleElement)getParent()).zetMaat();
	}
	*/
	
	public String toString()
	{	return "$L" + kind1.toString() + "$n" + kind2.toString() + "@@";
	}
}

