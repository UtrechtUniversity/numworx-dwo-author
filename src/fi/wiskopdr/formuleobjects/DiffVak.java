package fi.wiskopdr.formuleobjects;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;


public class DiffVak extends RegelVak
{	
	private boolean diffBreuk;
	
	public DiffVak(FormuleVak  fv)
	{	formuleVak = fv;
		
		setLayout(null);
		super.setFont(fv.getFont());
		fm = getFontMetrics(getFont());
			
		kind1 = new FormuleRegel(formuleVak);
		add(kind1);
		
		kind2 = new FormuleRegel(formuleVak);
		kind2.insert("x");
		add(kind2);
        
        maakMaat();
		
		setOpaque(false);
	}
		
	public void setFont(Font f)
	{	super.setFont(f);
		fm = getFontMetrics(getFont());
		if(kind1==null)return;
		kind1.setFont(f);
		kind2.setFont(f);
		
		maakMaat();
	}
	
	public void paint(Graphics g)
	{	super.zetMaat();
		diffBreuk = kind1.toString().length()==1 && Character.isLetter(kind1.toString().charAt(0));
        
		if(selected)
		{	g.setColor(Color.black);
			g.fillRect(0,0,getSize().width,getSize().height);
		}
		if(selected)g.setColor(Color.white);
		else g.setColor(fgColor);
		
		String dString = "d";
		
		g.drawLine(asc/8,(height-(2*asc+2*desc+asc/4))/2+asc+desc,asc/8+asc,(height-(2*asc+2*desc+asc/4))/2+asc+desc);
		g.drawString(dString, asc/8+ (diffBreuk?0:asc/4),(height-(2*asc+2*desc+asc/4))/2+asc);
		g.drawString(dString, asc/8, height-desc-(height-(2*asc+2*desc+asc/4))/2-asc/6);
		
		
		int hoogte = kind1.getSize().height;
		int breedte = getSize().width;
		int h =3*fm.getAscent()/2;
		int hh = h/2;
		int b = h/6;
		int bb = b/2;
		
		int c = fm.getAscent()/6;
		int d = fm.getAscent()/8;
		
		int locx = asc/8+asc;
		int locy = (height-k1h)/2;
		
		if(!diffBreuk)
		{	g.drawLine(locx+c+b, locy+d, locx+c+b-bb, locy+d+bb);
			g.drawLine(locx+c+b-bb, locy+d+bb, locx+c, locy+d+hh-b);
			g.drawLine(locx+c, locy+d+hh-b, locx+c, locy+hoogte-hh+b-d);		
			g.drawLine(locx+c+b-bb, locy+hoogte-bb-d, locx+c, locy+hoogte-hh+b-d);
			g.drawLine(locx+c+b, locy+hoogte-d, locx+c+b-bb, locy+hoogte-bb-d);
			
			g.drawLine(breedte-b-1-c, locy+d, breedte-b+bb-1-c, locy+d+bb);
			g.drawLine(breedte-b+bb-1-c, locy+d+bb, breedte-1-c, locy+d+hh-b);
			g.drawLine(breedte-1-c, locy+d+hh-b, breedte-1-c, locy+hoogte-hh+b-d);		
			g.drawLine(breedte-b+bb-1-c, locy+hoogte-bb-d, breedte-1-c, locy+hoogte-hh+b-d);
			g.drawLine(breedte-b-1-c, locy+hoogte-d, breedte-b+bb-1-c, locy+hoogte-bb-d);
		}

		super.paint(g);
	}
    
    private void maakMaat()
    {   super.zetMaat();
     	diffBreuk = kind1.toString().length()==1 && Character.isLetter(kind1.toString().charAt(0));
        width = asc/8+asc+asc/3+k1w+asc/3+asc/4;
        height = Math.max(k1h, 2*(asc+desc)+asc/4);
        if(diffBreuk) width = asc/8+k1w+asc/3+asc/4;
        k1x = asc/8+asc+asc/3+1;
        k1y = (height-k1h)/2-1;
        if(diffBreuk) 
        {	k1x = asc/2-1;
            k1y = 0;
        }
        k2x = asc/8+asc/2-2;
        k2y = (height-(2*(asc+desc)+asc/4))/2+asc+desc+asc/8;
        
        ashoogte = k2y- fm.getAscent()/8-2;//k1a + k1y;
        if(diffBreuk) ashoogte = kind1.getSize().height - fm.getAscent()/8-1;
    	
        
        setSize(width, height);
        kind1.setLocation(k1x,k1y);
        kind2.setLocation(k2x,k2y);
    }
	public void zetMaat()
	{	maakMaat();
		if(getParent()instanceof FormuleElement)((FormuleElement)getParent()).zetMaat();
			
	}
	
	public String toString()
	{	return "$d" + kind1.toString() + "$n" + kind2.toString() + "@@";
	}
}

