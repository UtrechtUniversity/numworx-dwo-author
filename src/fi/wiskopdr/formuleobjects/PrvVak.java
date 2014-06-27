package fi.wiskopdr.formuleobjects;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

import fi.wiskopdr.expressies.Algebra;
import fi.wiskopdr.expressies.BasisExpressie;


public class PrvVak extends RegelVak
{	
	public PrvVak(FormuleVak  fv)
	{	formuleVak = fv;
		
		setLayout(null);
		super.setFont(fv.getFont());
		fm = getFontMetrics(getFont());
		
		Font font2 = new Font(fv.getFont().getName(),fv.getFont().getStyle(),2*fv.getFont().getSize()/3);
		fm2 = getFontMetrics(font2);
			
		setSize(4*fm.getAscent()/3,3*fm.getAscent() + fm.getDescent());
		ashoogte = 3*fm.getAscent()/4;
		
		kind1 = new FormuleRegel(formuleVak);
		kind1.setLocation(5*fm.getAscent()/7+5,fm.getAscent());
		add(kind1);
		
		kind2 = new FormuleRegel(formuleVak);
		kind2.setLocation(fm.getAscent(),0);
		add(kind2);
		
		kind3 = new FormuleRegel(formuleVak);
		kind3.setLocation(fm.getAscent(),0);
		add(kind3);
		
		kind4 = new FormuleRegel(formuleVak);
		kind4.insert("x");
		kind4.setLocation(fm.getAscent(),0);
		//add(kind4);
		setOpaque(false);
	}
	
	public void setFont(Font f)
	{	super.setFont(f);
		fm = getFontMetrics(getFont());
		if(kind1==null)return;
		
        Font font2 = new Font(formuleVak.getFont().getName(),formuleVak.getFont().getStyle(),2*formuleVak.getFont().getSize()/3);
        for(int i=0 ; i<kind2.getComponentCount()  ; i++)
        {   kind2.getComponent(i).setFont(font2);
        }
        for(int i=0 ; i<kind3.getComponentCount()  ; i++)
        {   kind3.getComponent(i).setFont(font2);
        }
        kind2.setFont(font2);
        kind3.setFont(font2);
	}
	
	public void paint(Graphics g)
	{	if(selected)
		{	g.setColor(Color.black);
			g.fillRect(0,0,getSize().width,getSize().height);
		}
		if(selected)g.setColor(Color.white);
		else g.setColor(fgColor);
		
		int a = fm.getAscent();
		int kh = kind1.getSize().height;
		int kb = kind1.getSize().width;
		int k2b = kind2.getSize().width;
		int k2h = kind2.getSize().height;
		int k3b = kind3.getSize().width;
		int k3h = kind3.getSize().height;
		
		int t1x = 0;
		int t1y = 0;
		int t1b = a/3;
		int t1h = 2*a/3+Math.max(kh+2*t1b,k2h+k3h);
		
		int t2x = t1x+t1b+kb;
		int t2y = 0;
		int t2b = a/3;
		int t2h = t1h;
		
		int k3x = t2x+t2b+2;
		int k3y = 0;
		
		int kx = t1x+t1b;
		int ky = t1y+t1h/2-kind1.ashoogte;
		
		ashoogte = ky + kind1.ashoogte ;
		
		//int k4x = kx+kb+tb;
		//int k4y = ashoogte-kind4.ashoogte;
		
		g.drawLine(t1x,t1y+a/3,t1x+t1b,t1y+a/3);
		g.drawLine(t1x,t1y+a/3,t1x,t1y+t1h-a/3);
		g.drawLine(t1x,t1y+t1h-a/3,t1x+t1b,t1y+t1h-a/3);
        
		g.drawLine(t2x,t2y+a/3,t2x+t2b,t2y+a/3);
		g.drawLine(t2x+t2b,t2y+a/3,t2x+t2b,t2y+t2h-a/3);
		g.drawLine(t2x,t2y+t2h-a/3,t2x+t2b,t2y+t2h-a/3);
		
        
		//g.drawLine(5,2*getSize().height/3,fm.getAscent()/3+5,getSize().height);
		//g.drawLine(6,2*getSize().height/3,fm.getAscent()/3+6,getSize().height);
		//g.drawLine(fm.getAscent()/3+5,getSize().height,2*fm.getAscent()/3+4,fm.getAscent()/8);
		//g.drawLine(2*fm.getAscent()/3+5,fm.getAscent()/8,getSize().width+5,fm.getAscent()/8);
		super.paint(g);
	}
		
	public void zetMaat()
	{	int a = fm.getAscent();
		int kb = kind1.getSize().width;
		int kh = kind1.getSize().height;
		int k2b = kind2.getSize().width;
		int k2h = kind2.getSize().height;
		int k3b = kind3.getSize().width;
		int k3h = kind3.getSize().height;
		//int k4b = kind4.getSize().width;
		//int k4h = kind4.getSize().height;
		
		int t1x = 0;
		int t1y = 0;
		int t1b = a/3;
		int t1h = 2*a/3+Math.max(kh+2*t1b, k2h+k3h);
		
		int t2x = t1x+t1b+kb;
		int t2y = 0;
		int t2b = a/3;
		int t2h = t1h;
		
		int k3x = t2x+t2b+2;
		int k3y = 0;
		
		int kx = t1x+t1b;
		int ky = t1y+t1h/2-kind1.ashoogte;
		
		ashoogte = ky + kind1.ashoogte ;
		
		//int k4x = kx+kb+tb;
		//int k4y = ashoogte-kind4.ashoogte;
		
		int k2x = t2x+t2b+2;
		int k2y = t2h-k2h;
		
		int b = t2x+t2b+Math.max(k2b, k3b)+4;
		int h = t2h+1;
		
		setSize(b,h);
		ashoogte = ky + kind1.ashoogte ;
		kind1.setLocation(kx,ky);
		kind2.setLocation(k2x,k2y);
		kind3.setLocation(k3x,k3y);
		//kind4.setLocation(k4x,k4y);
		
        /*
        Font font2 = new Font(formuleVak.getFont().getName(),formuleVak.getFont().getStyle(),2*formuleVak.getFont().getSize()/3);
		for(int i=0 ; i<kind2.getComponentCount()  ; i++)
		{	kind2.getComponent(i).setFont(font2);
		}
		for(int i=0 ; i<kind3.getComponentCount()  ; i++)
		{	kind3.getComponent(i).setFont(font2);
		}
		kind2.setFont(font2);
		kind3.setFont(font2);
		*/
        
		if(getParent()instanceof FormuleElement)((FormuleElement)getParent()).zetMaat();
			
	}
	
	public String toString()
	{	
	    return "$q" + kind1.toString() + "$n" + kind2.toString() + "$k" + kind3.toString() + "$l" + kind4.toString() + "@@@@";//"$n" + kind3.toString() + 
	}
}

