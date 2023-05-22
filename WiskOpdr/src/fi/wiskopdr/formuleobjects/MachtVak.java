package fi.wiskopdr.formuleobjects;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;


public class MachtVak extends RegelVak
{	
	public MachtVak(FormuleVak  fv)
	{	formuleVak = fv;
		
		setLayout(null);
		
		int size = fv.getFont().getSize();	
		if(!fv.getFont().getName().equals("TimesRoman"))size += 2;
		Font f = new Font(fv.getFont().getName(),fv.getFont().getStyle(),(int)(2.0*size/3));
		setFont(f);
		fm = getFontMetrics(getFont());
		
		setSize(fm.getAscent()/2,3*fm.getAscent()/2 + fm.getDescent());
		ashoogte = fm.getAscent();
		
		kind1 = new FormuleRegel(formuleVak);
		kind1.setLocation(0,0);
		add(kind1);
		setOpaque(false);
	}
	
	public void paintComponent(Graphics g)
	{	//zetMaat();
		if(selected)
		{	g.setColor(Color.black);
			g.fillRect(0,0,getSize().width,getSize().height);
		}
		//super.paint(g);
	}
	
	public void zetMaat()
	{	int vgh = 0;
		int vgah = 0;
		if(getParent() != null)
		{	vgh = ((FormuleRegel)getParent()).geefVoorgangerHoogte(this);
			vgah = ((FormuleRegel)getParent()).geefVoorgangerAsHoogte(this);
		}
		setSize(kind1.getSize().width, kind1.getSize().height - 2*fm.getAscent()/3 -  fm.getDescent() + vgh);
		ashoogte = kind1.getSize().height - 2*fm.getAscent()/3 -  fm.getDescent() + vgah;
		if(getParent() != null && getParent()instanceof FormuleElement)((FormuleElement)getParent()).zetMaat();
	}
	
	public String toString()
	{	return "$m" + kind1.toString() + "@";
	}
	
	public String toMathML() {
		return kind1.toMathML();
	}
}

