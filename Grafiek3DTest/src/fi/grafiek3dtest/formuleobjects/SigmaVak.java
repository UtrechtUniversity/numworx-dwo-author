package fi.grafiek3dtest.formuleobjects;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;


public class SigmaVak extends RegelVak
{	
	int richting = 0;
	Font font, font2;
	
	
	public SigmaVak(FormuleVak  fv)
	{	formuleVak = fv;
		
		setLayout(null);
		
		super.setFont(fv.getFont());
		fm = getFontMetrics(getFont());
		
		Font font2 = new Font(fv.getFont().getName(),fv.getFont().getStyle(),2*fv.getFont().getSize()/3);
		fm2 = getFontMetrics(font2);
		kind1 = new FormuleRegel(formuleVak);
		
		kind2 = new FormuleRegel(formuleVak);
		kind2.insert("i");
		
		kind3 = new FormuleRegel(formuleVak);
		
		kind4 = new FormuleRegel(formuleVak);
		kind4.insert("0");
				
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
		
		Font font2 = new Font(formuleVak.getFont().getName(),formuleVak.getFont().getStyle(),4*formuleVak.getFont().getSize()/5);
		fm2 = getFontMetrics(font2);
		
		kind1.setFont(f);
		kind2.setFont(font2);
		kind3.setFont(font2);
		kind4.setFont(font2);
		
		maakMaat();
	}
	
	public void paint(Graphics g)
	{	maakMaat();
		if(selected)
		{	g.setColor(Color.black);
			g.fillRect(0,0,getSize().width,getSize().height);
		}
		if(selected)g.setColor(Color.white);
		else g.setColor(fgColor);
		
		
		System.out.println(kind4.toString());
		Font f = fm.getFont();
		Font font0 = new Font(f.getName(), f.getStyle(), f.getSize()*3/2);
		g.setFont(font0);
		FontMetrics fm0 = getFontMetrics(font0);
		int sigmaW = fm0.stringWidth("\u03A3");
		g.drawString("\u03A3", k4x+(k4w-sigmaW)/2,Math.min(k2y, k3y));
		
		f = fm2.getFont();
		g.setFont(f);
		g.drawString("=", k2x+k2w,k3y+2*k3a);
		
		f = fm.getFont();
		g.setFont(f);
		
		

		super.paint(g);
	}
		
	
	private void maakMaat()
	{	super.zetMaat();
		
		k4y = Math.max(0,k1a+1-k4h-asc/2);
		k2x = 0;
		k2y = k4y+k4h+asc;
		k3y = k4y+k4h+asc;
		k3x = k2w+asc/2;
		k1x = k3x+k3w;
		k1y = Math.max(0, k4y+k4h+asc/2-k1a-1);
		int w2plus3 = k2w+asc/2+k3w;
		k4x = (w2plus3-k2w)/2;
		width = w2plus3 + k1w;
		height = Math.max(k4h+asc/2, k1a+1) + Math.max(asc/2+k2h, k1h-k1a-1);
		setSize(width, height);
		ashoogte = k1y+k1a;
		
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
	{	return "$S" + kind1.toString() + "$n"+ kind2.toString() + "$k" + kind3.toString() + "$l" + kind4.toString() + "@@@@";
	}
}

