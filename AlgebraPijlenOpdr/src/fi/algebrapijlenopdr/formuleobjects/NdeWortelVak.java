package fi.algebrapijlenopdr.formuleobjects;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;


public class NdeWortelVak extends RegelVak
{	
	public NdeWortelVak(FormuleVak  fv)
	{	formuleVak = fv;
		
		setLayout(null);
		super.setFont(fv.getFont());
		fm = getFontMetrics(getFont());
		
		Font font2 = new Font(fv.getFont().getName(),fv.getFont().getStyle(),2*fv.getFont().getSize()/3);
		fm2 = getFontMetrics(font2);
			
		setSize(4*fm.getAscent()/3,5*fm.getAscent()/4 + fm.getDescent());
		ashoogte = 3*fm.getAscent()/4;
		
		kind1 = new FormuleRegel(formuleVak);
		kind1.setLocation(5*fm.getAscent()/7+5,fm.getAscent()/4);
		add(kind1);
		
		kind2 = new FormuleRegel(formuleVak);
		kind2.setLocation(fm.getAscent()/4,0);
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
		kind1.setLocation(5*fm.getAscent()/7+5,fm.getAscent()/4);
		
		kind2.setFont(font2);
		kind2.setLocation(5,0);
	}
	
	public void paint(Graphics g)
	{	if(selected)
		{	g.setColor(Color.black);
			g.fillRect(0,0,getSize().width,getSize().height);
		}
		if(selected)g.setColor(Color.white);
		else g.setColor(Color.black);
		g.drawLine(5,2*getSize().height/3,fm.getAscent()/3+5,getSize().height);
		g.drawLine(6,2*getSize().height/3,fm.getAscent()/3+6,getSize().height);
		g.drawLine(fm.getAscent()/3+5,getSize().height,2*fm.getAscent()/3+4,fm.getAscent()/8);
		g.drawLine(2*fm.getAscent()/3+5,fm.getAscent()/8,getSize().width+5,fm.getAscent()/8);
		super.paint(g);
	}
		
	public void zetMaat()
	{	setSize(5*fm.getAscent()/6 + kind1.getSize().width+5, fm.getAscent()/4 + kind1.getSize().height);
		ashoogte = kind1.ashoogte + fm.getAscent()/4 ;
		Font font2 = new Font(formuleVak.getFont().getName(),formuleVak.getFont().getStyle(),2*formuleVak.getFont().getSize()/3);
		for(int i=0 ; i<kind2.getComponentCount()  ; i++)
		{	kind2.getComponent(i).setFont(font2);
		}
		kind2.setFont(font2);
		
		if(getParent()instanceof FormuleElement)((FormuleElement)getParent()).zetMaat();
			
	}
	
	public String toString()
	{	return "$W" + kind1.toString() + "$n" + kind2.toString() + "@@";
	}
}

