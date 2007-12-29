package fi.spot_problems_dwo.wiskopdr.formuleobjects;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;


public class BreukVak extends RegelVak
{	
	private boolean klein = false;
	
	public BreukVak(FormuleVak  fv)
	{	formuleVak = fv;
				
		setLayout(null);
		super.setFont(fv.getFont());
		fm = getFontMetrics(getFont());
			
		setSize(3*fm.getAscent()/4,5*fm.getAscent()/2 + 2*fm.getDescent());
		
		ashoogte = 5*fm.getAscent()/4 + fm.getDescent() ;
		kind1 = new FormuleRegel(formuleVak);
		kind1.setLocation(fm.getAscent()/8,0);
		add(kind1);
		
		kind2 = new FormuleRegel(formuleVak);
		kind2.setLocation(fm.getAscent()/8,6*fm.getAscent()/4 + fm.getDescent());
		add(kind2);
	}
	
	public void setFont(Font f)
	{	super.setFont(f);
		fm = getFontMetrics(f);
		int b = kind1.getSize().width;
		if(b < kind2.getSize().width)b = kind2.getSize().width;
		setSize(b + fm.getAscent()/4,kind1.getSize().height + kind2.getSize().height + fm.getAscent()/4);
		ashoogte = kind1.getSize().height - fm.getAscent()/8;
		kind1.setLocation((getSize().width-kind1.getSize().width)/2, 0);
		kind2.setLocation((getSize().width-kind2.getSize().width)/2, kind1.getSize().height + fm.getAscent()/4);
		kind1.setFont(f);
		kind2.setFont(f);
		kind1.setLocation(fm.getAscent()/8,0);
		kind2.setLocation(fm.getAscent()/8,6*fm.getAscent()/4 + fm.getDescent());
	}
	
	public void paint(Graphics g)
	{	zetMaat();
		if(selected)
		{	g.setColor(Color.black);
			g.fillRect(0,0,getSize().width,getSize().height);
		}
		if(selected)g.setColor(Color.white);
		else g.setColor(Color.black);
		g.drawLine(fm.getAscent()/8,kind1.getSize().height + fm.getAscent()/8,getSize().width - fm.getAscent()/8,kind1.getSize().height + fm.getAscent()/8);
		
		super.paint(g);
	}
	
	private boolean onlyDigits(String s)
	{	for(int i=0 ; i<s.length(); i++)
		{	if(!Character.isDigit(s.charAt(i))) return false;
		}
		if(s.length()==0) return false;
		return true;
	}
	
	public void zetMaat()
	{	//int teller,noemer;
		Font f;	
		boolean integerBreuk = true;
			try
			{	//teller = Integer.parseInt(kind1.toString());
				//noemer = Integer.parseInt(kind2.toString());
				if(onlyDigits(kind1.toString()) && onlyDigits(kind2.toString()))integerBreuk = true;
				else integerBreuk = false;
			}
			catch(NumberFormatException e)
			{	integerBreuk = false;
			}
			if(integerBreuk && !klein)
			{	klein = true;
				f = new Font(formuleVak.getFont().getName(),formuleVak.getFont().getStyle(),2*formuleVak.getFont().getSize()/3);
			
				fm = getFontMetrics(f);
				
				for(int i=0 ; i<kind1.getComponentCount()  ; i++)
				{	kind1.getComponent(i).setFont(f);
				}
				for(int i=0 ; i<kind2.getComponentCount()  ; i++)
				{	kind2.getComponent(i).setFont(f);
				}
				kind1.setFont(f);
				kind2.setFont(f);
			}
			else if(!integerBreuk && klein)
			{	klein = false;
				//f = new Font(formuleVak.getFont().getName(),formuleVak.getFont().getStyle(),3*formuleVak.getFont().getSize()/2);
				f = formuleVak.getDefaultFont();
				fm = getFontMetrics(f);
				
				for(int i=0 ; i<kind1.getComponentCount()  ; i++)
				{	kind1.getComponent(i).setFont(f);
				}
				for(int i=0 ; i<kind2.getComponentCount()  ; i++)
				{	kind2.getComponent(i).setFont(f);
				}
				kind1.setFont(f);
				kind2.setFont(f);
			}
		int b = kind1.getSize().width;
		if(b < kind2.getSize().width)b = kind2.getSize().width;
		setSize(b + fm.getAscent()/4,kind1.getSize().height + kind2.getSize().height + fm.getAscent()/4);
		ashoogte = kind1.getSize().height - fm.getAscent()/8;
		kind1.setLocation((getSize().width-kind1.getSize().width)/2, 0);
		kind2.setLocation((getSize().width-kind2.getSize().width)/2, kind1.getSize().height + fm.getAscent()/4);
		//kind2.setCaretPosition(kind2.getSize().width);
		if(getParent()instanceof FormuleElement)((FormuleElement)getParent()).zetMaat();
	}
	
	public String toString()
	{	return "$b" + kind1.toString() + "$n" + kind2.toString() + "@@";
	}
}

