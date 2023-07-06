package fi.wiskopdr.formuleobjects;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;


public class HaakjesVak extends RegelVak
{	
	public HaakjesVak(FormuleVak  fv)
	{	formuleVak = fv;
		
		setLayout(null);
		
		super.setFont(fv.getFont());
		fm = getFontMetrics(getFont());
			
		setSize(5*fm.getAscent()/5,4*fm.getAscent()/3 + fm.getDescent());
		ashoogte = fm.getAscent()/2;
		
		kind1 = new FormuleRegel(formuleVak);
		kind1.setLocation(5*fm.getAscent()/12,fm.getAscent()/12);
		add(kind1);
		setOpaque(false);
	}
	
	public void setFont(Font f)
	{	super.setFont(f);
		fm = getFontMetrics(getFont());
		if(kind1==null)return;
		setSize(5*fm.getAscent()/6 + kind1.getSize().width, fm.getAscent()/6 + kind1.getSize().height);
		ashoogte = kind1.ashoogte + fm.getAscent()/12;
		kind1.setLocation(5*fm.getAscent()/12,fm.getAscent()/12);
		kind1.setFont(f);
	}
	
	public void paint(Graphics gr)
	{	Graphics g ;
		//if(!WiskOpdr.formTimes)
		{ 	g = (Graphics2D)gr;
			((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			//((Graphics2D)g).setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_PURE);
			((Graphics2D)g).setStroke(new BasicStroke(0.7f));
	    }
	    //else 
	    g=gr;
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
		
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		
		super.paint(g);
	}
	
	public void zetMaat()
	{	setSize(5*fm.getAscent()/6 + kind1.getSize().width, fm.getAscent()/6 + kind1.getSize().height);
		ashoogte = kind1.ashoogte + fm.getAscent()/12;
		if(getParent()instanceof FormuleElement)((FormuleElement)getParent()).zetMaat();
	}
	
	
	public String toString()
	{	return "$h" + kind1.toString() + "@";
	}
	
	public String toMathML() 
	{
		return "<mfenced>" + kind1.toMathML() + "</mfenced>";
	}
}

