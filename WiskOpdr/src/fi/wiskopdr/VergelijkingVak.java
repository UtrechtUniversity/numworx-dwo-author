package fi.wiskopdr;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import fi.wiskopdr.formuleobjects.*;
import fi.wiskopdr.WiskOpdr;


public class VergelijkingVak extends FormuleElement
{	
	private Font font = WiskOpdr.formuleFont0;
	private FontMetrics fm;
	private Color fgColor = Color.black;
	FormuleVak formuleVak1;
	FormuleVak formuleVak2;
	
	public VergelijkingVak()
	{	setLayout(null);
		
		super.setFont(font);
		fm = getFontMetrics(getFont());
					
		setSize(fm.getAscent()/2 + fm.stringWidth(" = "),fm.getAscent() + fm.getDescent());
		ashoogte = fm.getAscent()/2;
		
		formuleVak1 = new FormuleVak();
		formuleVak1.setLocation(0,0);
		add(formuleVak1);
		
		formuleVak2 = new FormuleVak();
		formuleVak2.setLocation(fm.getAscent()/2 + fm.stringWidth(" = "),0);
		add(formuleVak2);
	}
	
	public void setFont(Font f)
	{	super.setFont(f);
		fm = getFontMetrics(getFont());
		if(formuleVak1==null || formuleVak2==null)return;
		formuleVak1.setFont(f);
		formuleVak2.setFont(f);
	}
	
	public void paint(Graphics g)
	{	g.setColor(fgColor);
		g.setFont(getFont());
		g.drawString(" = ",formuleVak1.getSize().width,ashoogte+fm.getAscent()/2+ fm.getDescent()/2);
		super.paint(g);
	}
	
	public void setFGColor(Color c){
		fgColor = c;
		formuleVak1.setFGColor(c);
		formuleVak2.setFGColor(c);
		
	}
	
	public void zetMaat()
	{	int b = fm.stringWidth(" = ") + formuleVak1.getSize().width + formuleVak2.getSize().width;
		
		int h1 = formuleVak1.ashoogte;
		if(formuleVak2.ashoogte>h1)h1=formuleVak2.ashoogte;
		int h2 = formuleVak1.getSize().height - formuleVak1.ashoogte;
		if(formuleVak2.getSize().height - formuleVak2.ashoogte>h2)h2=formuleVak2.getSize().height - formuleVak2.ashoogte;
		
		setSize(b, h1+h2);
		ashoogte = h1;
		
		formuleVak1.setLocation(0,ashoogte-formuleVak1.ashoogte);
		formuleVak2.setLocation(formuleVak1.getSize().width + fm.stringWidth(" = "),ashoogte-formuleVak2.ashoogte);
	}
	
}

