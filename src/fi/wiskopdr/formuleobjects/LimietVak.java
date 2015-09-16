package fi.wiskopdr.formuleobjects;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;


public class LimietVak extends RegelVak
{	
	int richting = 0;
	
	public LimietVak(FormuleVak  fv)
	{	formuleVak = fv;
		
		setLayout(null);
		super.setFont(fv.getFont());
		fm = getFontMetrics(getFont());
		
		Font font2 = new Font(fv.getFont().getName(),fv.getFont().getStyle(),2*fv.getFont().getSize()/3);
		fm2 = getFontMetrics(font2);
		
		kind1 = new FormuleRegel(formuleVak);
		
		kind2 = new FormuleRegel(formuleVak);
		kind2.insert("x");
		
		kind3 = new FormuleRegel(formuleVak);
		
		kind4 = new FormuleRegel(formuleVak);
		kind4.insert("0");
				
		add(kind1);
		add(kind2);
		add(kind3);
		
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
		
		g.drawString("lim", k2x+k2w/2,k3y);//(height-(2*asc+2*desc+asc/4))/2+3*asc/2+desc);
		if(kind4!=null && kind4.toString().equals("0"))
		{	
			int pijlX = k2x+k2w+fm2.getAscent()/4;
			int pijlY = k3y+k3a+fm2.getAscent()/4;
			int pijlW = 3*asc/8;
			g.drawLine(pijlX,pijlY,pijlX+pijlW,pijlY);
			g.drawLine(pijlX+pijlW-1,pijlY-1,pijlX+pijlW-1,pijlY);
			g.drawLine(pijlX+pijlW-1,pijlY+1,pijlX+pijlW,pijlY);
		}
		if(kind4!=null && kind4.toString().equals("1"))
		{	
			int pijlX = 6*asc/8;
			int pijlY = k3y+k3a+fm2.getAscent()/4-asc/3;
			int pijlH = 4*asc/8;
			g.drawLine(pijlX,pijlY,pijlX,pijlY+pijlH);
			g.drawLine(pijlX-1,pijlY+pijlH-1,pijlX,pijlY+pijlH);
			g.drawLine(pijlX+1,pijlY+pijlH-1,pijlX,pijlY+pijlH);
		}
		if(kind4!=null && kind4.toString().equals("2"))
		{	int pijlX = 6*asc/8;
			int pijlY = k3y+k3a+fm2.getAscent()/4-asc/3;
			int pijlH = 4*asc/8;
			g.drawLine(pijlX,pijlY,pijlX,pijlY+pijlH);
			g.drawLine(pijlX-1,pijlY+1,pijlX,pijlY);
			g.drawLine(pijlX+1,pijlY+1,pijlX,pijlY);
		}
		/*
		int hoogte = kind1.getSize().height;
		int breedte = getSize().width;
		int h =3*fm.getAscent()/2;
		int hh = h/2;
		int b = h/6;
		int bb = b/2;
		
		int c = fm.getAscent()/6;
		int d = fm.getAscent()/8;
		
		int locx = asc/8+2*asc;
		int locy = (height-k1h)/2;
		
		g.drawLine(locx+c+b, locy+d, locx+c+b-bb, locy+d+bb);
		g.drawLine(locx+c+b-bb, locy+d+bb, locx+c, locy+d+hh-b);
		g.drawLine(locx+c, locy+d+hh-b, locx+c, locy+hoogte-hh+b-d);		
		g.drawLine(locx+c+b-bb, locy+hoogte-bb-d, locx+c, locy+hoogte-hh+b-d);
		g.drawLine(locx+c+b, locy+hoogte-d, locx+c+b-bb, locy+hoogte-bb-d);
		
		g.drawLine(breedte-b-1-c, locy+d, breedte-b+bb-1-c, locy+d+bb);
		g.drawLine(breedte-b+bb-1-c, locy+d+bb, breedte-1-c, locy+d+hh-b);
		g.drawLine(breedte-1-c, locy+d+hh-b, breedte-1-c, locy+hoogte-hh+b-d);		
		g.drawLine(breedte-b+bb-1-c, locy+hoogte-bb-d, breedte-1-c, locy+hoogte-hh+b-d);
		g.drawLine(breedte-b-1-c, locy+hoogte-d, breedte-b+bb-1-c, locy+hoogte-bb-d);
		*/

		super.paint(g);
	}
		
	public void zetRichting(int richting)
	{	kind4.removeAll();
		kind4.insert(""+richting);
	}
	private void maakMaat()
	{	super.zetMaat();
		ashoogte =  kind1.ashoogte; 
		
		boolean breuk = kind1!=null && kind1.getComponentCount()>0 && (kind1.getComponent(0) instanceof BreukVak);
		int corr = breuk ? asc/3 : 0;
		
		k3x = k2w+3*asc/4;
		k3y = ashoogte+(asc+desc)/2-1 - corr;
		k2x = asc/8;
		k2y = k3y+k3a-k2a;//(height-(2*(asc+desc)+asc/4))/2+asc+3*desc;
		k1x = k3x+k3w+asc/4;
		k1y = 0;
		
		width = k3x+k3w+asc/4+k1w+asc/4;
		height = Math.max(k3y+k3h, k1h);
		
		setSize(width, height);
		kind1.setLocation(k1x,k1y);
		kind2.setLocation(k2x,k2y);
		kind3.setLocation(k3x,k3y);
	}
	
	public void zetMaat()
	{	maakMaat();
		if(getParent()instanceof FormuleElement)((FormuleElement)getParent()).zetMaat();	
	}
	/*
	public void paint(Graphics g)
	{	zetMaat();
		if(selected)
		{	g.setColor(Color.black);
			g.fillRect(0,0,getSize().width,getSize().height);
		}
		if(selected)g.setColor(Color.white);
		else g.setColor(Color.black);
		
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
		
		super.paint(g);
	}
	
	public void zetMaat()
	{	setSize(5*fm.getAscent()/6 + kind1.getSize().width, fm.getAscent()/6 + kind1.getSize().height);
		ashoogte = kind1.ashoogte + fm.getAscent()/12;
		if(getParent()instanceof FormuleElement)((FormuleElement)getParent()).zetMaat();
	}
	*/
	
	public String toString()
	{	return "$T" + kind1.toString() + "$n"+ kind2.toString() + "$k" + kind3.toString() + "$l" + kind4.toString() + "@@@@";
	}
	
	public String toMathML() {
		return "<mrow><munder><mi>lim</mi><mrow>" + kind2.toMathML() + arrow(kind4) + kind3.toMathML() + "</mrow></munder>" + kind1.toMathML() + "</mrow>";
	}

	private String arrow(FormuleRegel k) {
		char x = '\u2192';
		if("2".equals(k.toString()))
			x = '\u2191';
		else if("1".equals(k.toString()))
			x = '\u2193';
		return "<mo>"+ x + "</mo>";
	}
}

