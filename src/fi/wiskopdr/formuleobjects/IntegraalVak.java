package fi.wiskopdr.formuleobjects;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;


public class IntegraalVak extends RegelVak
{	
	public IntegraalVak(FormuleVak  fv)
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
		
		kind3 = new FormuleRegel(formuleVak);
		
		kind4 = new FormuleRegel(formuleVak);
        //kind4.setEditable(false);
		kind4.insert("x");
		
		add(kind1);
		add(kind2);
		add(kind3);
		add(kind4);
		
		maakMaat();
		
		setOpaque(false);
	}
	
	public void setFont(Font f)
	{	super.setFont(f);
		fm = getFontMetrics(getFont());
		if(kind1==null)return;
		kind1.setFont(f);
		kind4.setFont(f);
		int size = formuleVak.getFont().getSize();	
		if(!formuleVak.getFont().getName().equals("TimesRoman"))size += 2;
		Font font2 = new Font(formuleVak.getFont().getName(),formuleVak.getFont().getStyle(),(int)(2.0*size/3));
		//Font font2 = new Font(formuleVak.getFont().getName(),formuleVak.getFont().getStyle(),2*formuleVak.getFont().getSize()/3);
		kind2.setFont(font2);
		kind3.setFont(font2);
		/*for(int i=0 ; i<kind2.getComponentCount()  ; i++)
		{	kind2.getComponent(i).setFont(font2);
		}
		for(int i=0 ; i<kind3.getComponentCount()  ; i++)
		{	kind3.getComponent(i).setFont(font2);
		}*/
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
		
		int tx = Math.max(1, k2w-asc/2);
		int ty = k3h+1;
		int tb = 2*asc/3;
		int th = k1h+tb;
		
		g.drawArc(tx+asc/3,ty,asc/3,asc/3,0,180);
        g.drawArc(tx,ty+th-asc/3,asc/3,asc/3,180,180);
        g.drawLine(tx+asc/3,ty+asc/6,tx+asc/3,ty+th-asc/6);
        g.drawString("d",tx+asc+k1w+asc/5-2,ashoogte+(asc-1)/2+1);//(int)Math.rint(4.0*asc/8));
        
		//g.drawLine(5,2*getSize().height/3,fm.getAscent()/3+5,getSize().height);
		//g.drawLine(6,2*getSize().height/3,fm.getAscent()/3+6,getSize().height);
		//g.drawLine(fm.getAscent()/3+5,getSize().height,2*fm.getAscent()/3+4,fm.getAscent()/8);
		//g.drawLine(2*fm.getAscent()/3+5,fm.getAscent()/8,getSize().width+5,fm.getAscent()/8);
		super.paint(g);
	}
		
	public void maakMaat()
	{
		super.zetMaat();
		int tx = Math.max(1, k2w-asc/2);
		int ty = k3h+1;
		int tb = 2*asc/3;
		int th = k1h+tb;
		
		int k3x = tx+asc/2;
		int k3y = 0;
		
		int k1x = tx+asc-2;
		int k1y = ty+tb/2;
		
		ashoogte = k1y + k1a ;
		
		int k4x = k1x+k1w+tb-2;
		int k4y = ashoogte-k4a;
		
		int k2x = 1;
		int k2y = k3h+th+2;
		
		width = 1+tx+k1w+asc+k4w+tb;
		height = 2+k2h+k3h+th;
		
		setSize(width,height);
		ashoogte = k1y + k1a ;
		kind1.setLocation(k1x,k1y);
		kind2.setLocation(k2x,k2y);
		kind3.setLocation(k3x,k3y);
		kind4.setLocation(k4x,k4y);
	}
	
	public void zetMaat()
	{	maakMaat();
		if(getParent()instanceof FormuleElement)((FormuleElement)getParent()).zetMaat();
			
	}
	
	public String toString()
	{	return "$i" + kind1.toString() + "$n" + kind2.toString() + "$k" + kind3.toString() + "$l" + kind4.toString() + "@@@@";//"$n" + kind3.toString() + 
	}
}

