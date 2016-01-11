package fi.graphtool;

import java.awt.*;
import fi.wiskopdr.formuleobjects.*;
import fi.wiskopdr.formuleobjects.FormuleVak;
import fi.wiskopdr.WiskOpdr;


public class VergelijkingVak extends FormuleElement
{	
	private Font font = WiskOpdr.formuleFont0;
	private FontMetrics fm;
	private Color fgColor = Color.black;
	FormuleVak formuleVak;
	FormuleVak functieBeginVak;
	private boolean functieBeginAanpasbaar;
	
	public VergelijkingVak(boolean functieBeginAanpasbaar)
	{	setLayout(null);
		this.functieBeginAanpasbaar = functieBeginAanpasbaar;
	
		super.setFont(font);
		fm = getFontMetrics(getFont());
					
		setSize(fm.getAscent()/2 + fm.stringWidth(" = "),fm.getAscent() + fm.getDescent());
		ashoogte = fm.getAscent()/2;
		
		functieBeginVak = new FormuleVak();
		functieBeginVak.setLocation(0, 0);
		functieBeginVak.setEditable(false);
		if(!functieBeginAanpasbaar)
			add(functieBeginVak);
		
		formuleVak = new FormuleVak();
		if(functieBeginAanpasbaar)
			formuleVak.setLocation(0, 0);
		else
			formuleVak.setLocation(functieBeginVak.getWidth(), 0);
		add(formuleVak);
	}
	
	public void setFont(Font f)
	{	super.setFont(f);
		fm = getFontMetrics(getFont());
		if(formuleVak==null)return;
		formuleVak.setFont(f);
		functieBeginVak.setFont(f);
	}
	
	public void paint(Graphics g)
	{	g.setColor(fgColor);
		g.setFont(getFont());
		super.paint(g);
	}
	
	public void setFGColor(Color c){
		fgColor = c;
		formuleVak.setFGColor(c);
		functieBeginVak.setFGColor(c);
	}
	
	public void zetMaat()
	{	int b = formuleVak.getSize().width;
		
		if(!functieBeginAanpasbaar)
		{	b = formuleVak.getSize().width + functieBeginVak.getSize().width;
		}
		
		int h1 = formuleVak.ashoogte;
		if(functieBeginVak.ashoogte > h1) h1 = functieBeginVak.ashoogte;
		int h2 = formuleVak.getSize().height - formuleVak.ashoogte;
		if(functieBeginVak.getSize().height - functieBeginVak.ashoogte > h2) h2 = functieBeginVak.getSize().height - functieBeginVak.ashoogte;
		
		setSize(b, h1+h2);
		ashoogte = h1;
		
		if(functieBeginAanpasbaar)
			formuleVak.setLocation(0,ashoogte-formuleVak.ashoogte);
		else
		{	functieBeginVak.setLocation(0, ashoogte - functieBeginVak.ashoogte);
			formuleVak.setLocation(functieBeginVak.getSize().width, ashoogte - formuleVak.ashoogte);
			
		}
		
	}
	
}

