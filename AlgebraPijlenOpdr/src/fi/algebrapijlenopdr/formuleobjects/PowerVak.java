package fi.algebrapijlenopdr.formuleobjects;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;


public class PowerVak extends RegelVak
{	
	public PowerVak(FormuleVak  fv)
	{	formuleVak = fv;
				
		setLayout(null);
		super.setFont(fv.getFont());
		fm = getFontMetrics(getFont());
		
		Font font2 = new Font(fv.getFont().getName(),fv.getFont().getStyle(),2*fv.getFont().getSize()/3);
		fm2 = getFontMetrics(font2);
		
		setSize(fm.getAscent()/2, fm2.getAscent()/2 + fm.getAscent()/2 + fm.getDescent());
		ashoogte = fm2.getAscent()/2 + fm.getAscent()/2;
		
		kind1 = new FormuleRegel(formuleVak);
		kind1.setLocation(0,fm2.getAscent()/2);
		add(kind1);
		
		kind2 = new FormuleRegel(formuleVak);
		kind2.setLocation(fm.getAscent()/4,0);
		add(kind2);
	}
	
	
	public void setFont(Font f)
	{	super.setFont(f);
		fm = getFontMetrics(f);
		
		Font font2 = new Font(formuleVak.getFont().getName(),formuleVak.getFont().getStyle(),2*formuleVak.getFont().getSize()/3);
		fm2 = getFontMetrics(font2);
		
		setSize(kind1.getSize().width + kind2.getSize().width, kind1.getSize().height + kind2.getSize().height/2);
		ashoogte = kind1.ashoogte;

		kind1.setLocation(0,kind2.getSize().height/2);
		kind2.setLocation(kind1.getSize().width,0);
		kind1.setFont(f);
		kind2.setFont(font2);
		
	}
	
	public void paint(Graphics g)
	{	zetMaat();
		if(selected)
		{	g.setColor(Color.black);
			g.fillRect(0,0,getSize().width,getSize().height);
		}
		if(selected)g.setColor(Color.white);
		else g.setColor(Color.black);
		super.paint(g);
		super.paint(g);
	}
	
	public void zetMaat()
	{	
		setSize(kind1.getSize().width + kind2.getSize().width, kind1.getSize().height  - fm.getAscent()/2 -  fm.getDescent() +  kind2.getSize().height);
		ashoogte = kind1.ashoogte - fm.getAscent()/2 -  fm.getDescent() +  kind2.getSize().height;
		kind1.setLocation(0, - fm.getAscent()/2 -  fm.getDescent() +  kind2.getSize().height);
		kind2.setLocation(kind1.getSize().width,0);
		if(getParent()instanceof FormuleElement)((FormuleElement)getParent()).zetMaat();
	}
	
	public void neemFocus(String richting,FormuleElement fe)
	{	if(getParent() instanceof FormuleElement)
		{	((FormuleElement)getParent()).neemFocus(richting,this);
		}
	}
	
	public void neemFocus(String richting)
	{	kind1.neemFocus(richting);
	}
	
	public String toString()
	{	return "$p" + kind1.toString() + "$n" + kind2.toString() + "@@";
	}
}

