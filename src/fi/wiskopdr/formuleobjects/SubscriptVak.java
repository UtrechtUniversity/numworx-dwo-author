package fi.wiskopdr.formuleobjects;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;


public class SubscriptVak extends RegelVak
{	
	public SubscriptVak(FormuleVak  fv)
	{	formuleVak = fv;
		
		setLayout(null);
		
		super.setFont(new Font(fv.getFont().getName(),fv.getFont().getStyle(),2*fv.getFont().getSize()/3));
		fm = getFontMetrics(getFont());
		
		setSize(fm.getAscent()/2,3*fm.getAscent()/2 + fm.getDescent());
		ashoogte = fm.getAscent();
		
		kind1 = new FormuleRegel(formuleVak);
		kind1.setLocation(0,0);
		add(kind1);
		setOpaque(false);
	}
	
	public void setFont(Font f)
	{
		super.setFont(new Font(f.getName(),f.getStyle(),2*f.getSize()/3));
		fm = getFontMetrics(getFont());
		if(kind1==null)return;
		kind1.setFont(getFont());
		maakMaat();
	}
	
	public void paintComponent(Graphics g)
	{	//zetMaat();
		if(selected)
		{	g.setColor(Color.black);
			g.fillRect(0,0,getSize().width,getSize().height);
		}
		//super.paint(g);
	}
	
	public void maakMaat()
	{
		int vgh = ((FormuleRegel)getParent()).geefVoorgangerHoogte(this);
		int vgah = ((FormuleRegel)getParent()).geefVoorgangerAsHoogte(this);
		setSize(kind1.getSize().width, kind1.getSize().height + vgh - 2*fm.getAscent()/3);
        kind1.setLocation(0,vgh - 2*fm.getAscent()/3);
		ashoogte = vgah;
	}
	
	public void zetMaat()
	{	maakMaat();
		if(getParent()instanceof FormuleElement)((FormuleElement)getParent()).zetMaat();
	}
	
	public String toString()
	{	return "$s" + kind1.toString() + "@";
	}
	
	public String toMathML()
	{	return kind1.toMathML();
	}
}

