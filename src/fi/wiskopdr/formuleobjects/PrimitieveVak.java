package fi.wiskopdr.formuleobjects;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;


public class PrimitieveVak extends RegelVak
{	
	public PrimitieveVak(FormuleVak  fv)
	{	formuleVak = fv;
		
		setLayout(null);
		super.setFont(fv.getFont());
		fm = getFontMetrics(getFont());
		
		Font font2 = new Font(fv.getFont().getName(),fv.getFont().getStyle(),2*fv.getFont().getSize()/3);
		fm2 = getFontMetrics(font2);
			
		setSize(4*fm.getAscent()/3,3*fm.getAscent() + fm.getDescent());
		ashoogte = 3*fm.getAscent()/4;
		
		kind1 = new FormuleRegel(formuleVak);
		
		kind2 = new FormuleRegel(formuleVak);
		kind2.insert("x");
		
		add(kind1);
		add(kind2);
		
		
		maakMaat();
		
		setOpaque(false);
	}
	
	public void setFont(Font f)
	{	super.setFont(f);
		fm = getFontMetrics(getFont());
		if(kind1==null)return;
        maakMaat();
        
	}
	
	public void paint(Graphics g)
	{	if(selected)
		{	g.setColor(Color.black);
			g.fillRect(0,0,getSize().width,getSize().height);
		}
		if(selected)g.setColor(Color.white);
		else g.setColor(fgColor);
		
		maakMaat();
		
		int tx = 1;
		int ty = 0;
		int tb = 2*asc/3;
		int th = k1h+tb;
		
		g.drawArc(tx+asc/3,ty,asc/3,asc/3,0,180);
        g.drawArc(tx,ty+th-asc/3,asc/3,asc/3,180,180);
        g.drawLine(tx+asc/3,ty+asc/6,tx+asc/3,ty+th-asc/6);
        g.drawString("d",tx+asc+k1w+asc/5-2,ashoogte+(asc-1)/2+1);
        //g.drawString("d",tx+asc+k1w+asc/5,ashoogte+4*asc/7);
        
		//g.drawLine(5,2*getSize().height/3,fm.getAscent()/3+5,getSize().height);
		//g.drawLine(6,2*getSize().height/3,fm.getAscent()/3+6,getSize().height);
		//g.drawLine(fm.getAscent()/3+5,getSize().height,2*fm.getAscent()/3+4,fm.getAscent()/8);
		//g.drawLine(2*fm.getAscent()/3+5,fm.getAscent()/8,getSize().width+5,fm.getAscent()/8);
		super.paint(g);
	}
		
	public void maakMaat()
	{
		super.zetMaat();
		int tx = 1;
		int ty = 0;
		int tb = 2*asc/3;
		int th = k1h+tb;
		
		int k1x = tx+asc-2;
		int k1y = asc/3;
		
		ashoogte = k1y + k1a ;
		
		int k2x = k1x+k1w+tb-2;
		int k2y = ashoogte-k2a;
		
		width = 1+tx+k1w+asc+k2w+tb;
		height = th+1;
		
		setSize(width,height);
		ashoogte = k1y + k1a ;
		kind1.setLocation(k1x,k1y);
		kind2.setLocation(k2x,k2y);
		
	}
	
	public void zetMaat()
	{	maakMaat();
		if(getParent()instanceof FormuleElement)((FormuleElement)getParent()).zetMaat();
			
	}
	
	public String toString()
	{	return "$P" + kind1.toString() + "$n" + kind2.toString() + "@@";//"$n" + kind3.toString() + 
	}
}

