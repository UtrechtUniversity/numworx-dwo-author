package fi.wiskopdr.formuleobjects;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import fi.wiskopdr.WiskOpdr;


public class NdeLogVak extends RegelVak
{	
	private boolean isEN;
	
	public NdeLogVak(FormuleVak  fv)
	{	formuleVak = fv;
	
		isEN = WiskOpdr.language.getLanguage().equals("en");
		
		setLayout(null);
		super.setFont(fv.getFont());
		fm = getFontMetrics(getFont());
		
		Font font2 = new Font(fv.getFont().getName(),fv.getFont().getStyle(),2*fv.getFont().getSize()/3);
		fm2 = getFontMetrics(font2);
			
		setSize(4*fm.getAscent()/3,5*fm.getAscent()/4 + fm.getDescent());
		ashoogte = 3*fm.getAscent()/4;
		
		kind1 = new FormuleRegel(formuleVak);
		add(kind1);
		
		kind2 = new FormuleRegel(formuleVak);
		kind2.setFont(font2);
		add(kind2);
        
        maakMaat();
		setOpaque(false);
	}
	
	public void setFont(Font f)
	{	super.setFont(f);
		fm = getFontMetrics(getFont());
		if(kind1==null)return;
		
		
		Font font2 = new Font(formuleVak.getFont().getName(),formuleVak.getFont().getStyle(),2*formuleVak.getFont().getSize()/3);
		kind2.setFont(font2);
		for(int i=0 ; i<kind2.getComponentCount()  ; i++)
		{	if(kind2.getComponent(i) instanceof FormuleTeken)kind2.getComponent(i).setFont(font2);
		}
		
        maakMaat();
		/*Font font2 = new Font(formuleVak.getFont().getName(),formuleVak.getFont().getStyle(),2*formuleVak.getFont().getSize()/3);
		fm2 = getFontMetrics(font2);
		
		//setSize(5*fm.getAscent()/6 + kind1.getSize().width+5, fm.getAscent()/4 + kind1.getSize().height);
		setSize(12+kind2.getSize().width + fm.stringWidth("log") + kind1.getSize().width + fm.getAscent()/2, kind2.getSize().height/2 + kind1.getSize().height);
		ashoogte = kind1.ashoogte + fm.getAscent()/4 ;
		
		kind1.setFont(f);
		kind1.setLocation(12+fm.stringWidth("log("),fm.getAscent()/12);
		
		kind2.setFont(font2);
		kind2.setLocation(5,0);*/
	}
	
	public void paint(Graphics g)
	{	super.zetMaat();
		if(selected)
		{	g.setColor(Color.black);
			g.fillRect(0,0,getSize().width,getSize().height);
		}
		if(selected)g.setColor(Color.white);
		else g.setColor(fgColor);
		
		if(isEN)g.drawString("log", 3 ,ashoogte + asc/2 + asc/12);
		else g.drawString("log", 5+k2w ,ashoogte + asc/2 + asc/12);
		
		//Font font2 = new Font(formuleVak.getFont().getName(),formuleVak.getFont().getStyle(),2*formuleVak.getFont().getSize()/3);
		
		
		int hoogte = k1h;
		int breedte = getSize().width;
		int h =3*asc/2;
		int hh = h/2;
		int b = h/6;
		int bb = b/2;
		
		int c = asc/6;
		int d = asc/8;
		
		int locx = asc/3+k2w + fm.stringWidth("log");
		int locy = k2h/2;
		if(WiskOpdr.language.toString().equals("en"))locy = 0;
		
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


		super.paint(g);
	}
	
    private void maakMaat()
    {   super.zetMaat();
        
        int fStr = fm.stringWidth("log");
        setSize(2*asc/3+k2w + fStr + k1w + asc/2, k2h/2 + k1h);
        ashoogte = kind1.ashoogte+ k2h/2;
        if(isEN)ashoogte = kind1.ashoogte;
        
        kind1.setLocation(k2w + fStr + 3*asc/4, k2h/2);
        kind2.setLocation(asc/3, ashoogte-(k2h/2 + asc/2));
        
        if(WiskOpdr.language.toString().equals("en"))
        {
        	kind1.setLocation(k2w + fStr + 3*asc/4, 0);
        	kind2.setLocation(5+fStr, ashoogte + (-kind2.ashoogte + 2*asc/3));
        }
    }
    
	public void zetMaat()
	{	maakMaat();
		if(getParent()instanceof FormuleElement)((FormuleElement)getParent()).zetMaat();
	}
	
	
	
	public String toString()
	{	return "$L" + kind1.toString() + "$n" + kind2.toString() + "@@";
	}
	
	public String toMathML() 
	{
// if language is 'en' grondtal als subscript
		if(isEN)
		{
			return "<mrow><msub><mi>log</mi>" + kind2.toMathML() + "</msub><mfenced>" + kind1.toMathML() + "</mfenced></mrow>";
		}
		return "<mrow><mmultiscripts><mi>log</mi><mprescripts /><none />" + kind2.toMathML() + "</mmultiscripts><mfenced>" + kind1.toMathML() + "</mfenced></mrow>";
	}
}

