package fi.wiskopdr.formuleobjects;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

import fi.wiskopdr.WiskOpdr;


public class VermenigvuldigingVak extends RegelVak
{	
	private String operator;
	
	public VermenigvuldigingVak(FormuleVak  fv)
	{	formuleVak = fv;
		operator = "MW".equals(WiskOpdr.deployVariant) ? "" : "\u22c5";
		//operator = "";	
		setLayout(null);
		super.setFont(fv.getFont());
		fm = getFontMetrics(getFont());
			
		setSize(fm.getAscent()/2 + fm.stringWidth(operator),fm.getAscent() + fm.getDescent());
		ashoogte = fm.getAscent()/2;
				
		kind1 = new FormuleRegel(formuleVak);
		kind1.setLocation(0,0);
		add(kind1);
		
		kind2 = new FormuleRegel(formuleVak);
		kind2.setLocation(fm.getAscent()/2 + fm.stringWidth(operator),0);
		add(kind2);
		setOpaque(false);
	}
	
	public void setFont(Font f)
	{	super.setFont(f);
		fm = getFontMetrics(getFont());
		if(kind1==null)return;
		
		int b = fm.stringWidth(operator) + kind1.getSize().width + kind2.getSize().width;
		int h1 = kind1.ashoogte;
		if(kind2.ashoogte>h1)h1=kind2.ashoogte;
		int h2 = kind1.getSize().height - kind1.ashoogte;
		if(kind2.getSize().height - kind2.ashoogte>h2)h2=kind2.getSize().height - kind2.ashoogte;
		
		setSize(b, h1+h2);
		ashoogte = h1;
		
		kind1.setLocation(0,ashoogte-kind1.ashoogte);
		kind2.setLocation(kind1.getSize().width + fm.stringWidth(operator),ashoogte-kind2.ashoogte);
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
		
		g.drawString(operator,kind1.getSize().width+1,ashoogte+fm.getAscent()/2+ fm.getDescent()/2);
		super.paint(g);
		super.paint(g);
	}
	
	public void zetMaat()
	{	int b = fm.stringWidth(operator) + kind1.getSize().width + kind2.getSize().width;
		
		int h1 = kind1.ashoogte;
		if(kind2.ashoogte>h1)h1=kind2.ashoogte;
		int h2 = kind1.getSize().height - kind1.ashoogte;
		if(kind2.getSize().height - kind2.ashoogte>h2)h2=kind2.getSize().height - kind2.ashoogte;
		
		setSize(b, h1+h2);
		ashoogte = h1;
		
		kind1.setLocation(0,ashoogte-kind1.ashoogte);
		kind2.setLocation(kind1.getSize().width + fm.stringWidth(operator),ashoogte-kind2.ashoogte);
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
	{	return "$v" + kind1.toString() + "$n" + kind2.toString() + "@@";
	}
}

