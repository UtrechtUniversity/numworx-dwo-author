package fi.wiskopdr.formuleobjects;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;


public class StelselVak extends RegelVak
{	
	Vector<FormuleRegel> kinderen;
	
	public StelselVak(FormuleVak  fv)
	{	formuleVak = fv;
		kinderen = new Vector<FormuleRegel>();
		setLayout(null);
		
		super.setFont(fv.getFont());
		fm = getFontMetrics(getFont());
		
		kind1 = new FormuleRegel(formuleVak);
		kind1.setLocation(10, 5);//iets met fm.getAscent en fm.getDescent
		kinderen.add(kind1);
		add(kind1);
		maakMaat();
		
		setOpaque(false);
	}
	
	public void setFont(Font f)
	{	super.setFont(f);
		fm = getFontMetrics(getFont());
		if(kinderen == null || kinderen.get(0) == null)
			return;
		for(int i = 0; i < kinderen.size(); i++)
			kinderen.get(i).setFont(f);
		maakMaat();
				
		//kind1.setLocation(10, 5);// van wortel: 5*fm.getAscent()/7-1,fm.getAscent()/4);
	}
	
	public void paint(Graphics g)
	{	maakMaat();
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		((Graphics2D)g).setStroke(new BasicStroke(1.2f));
	    		
		if(selected)
		{	g.setColor(Color.black);
			g.fillRect(0,0,getSize().width,getSize().height);
		}
		if(selected)g.setColor(Color.white);
		else g.setColor(fgColor);
		
		g.drawArc(5, 1, 10, 10, 90, 90);
		g.drawLine(5, 6, 5, getSize().height / 2 - 3);
		g.drawArc(0, getSize().height / 2 - 6, 5, 5, 270, 90);
		g.drawArc(0, getSize().height / 2, 5, 5, 0, 90);
		g.drawLine(5, getSize().height / 2 + 3, 5, getSize().height - 6);
		g.drawArc(5, getSize().height - 12, 10, 10, 180, 90);
		
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		((Graphics2D)g).setStroke(new BasicStroke(0.7f));
		
		super.paint(g);
	}
		
	
	private void maakMaat()
	{	super.zetMaat();
		
		height = 5;
		width = 10;
		for(int i = 0; i < kinderen.size(); i++)
		{
			height += kinderen.get(i).getSize().height + 5;
			width = Math.max(width, 10 + kinderen.get(i).getSize().width);
		}
		setSize(width, height);
		ashoogte = height / 2 - fm.getDescent();
		
		int kindHoogte = 5;
		for(int i = 0; i < kinderen.size(); i++)
		{	kinderen.get(i).setLocation(10, kindHoogte);
			kindHoogte += kinderen.get(i).getSize().height + 5;
		}
	
	}
	
	public int bepaalKindMetFocus()
	{
		int kindMetFocus = 0;
		for(int i = 0; i < kinderen.size(); i++)
		{
			if(kinderen.get(i).hasFocus())
			{	kindMetFocus = i;
				break;
			}
		}
		return kindMetFocus;
	}
	
	public void focusKindOmhoog()
	{
		int kindMetFocus = bepaalKindMetFocus();
		if(kindMetFocus > 0)
			kinderen.get(kindMetFocus - 1).neemFocus("rechts");
	}
	
	public void focusKindOmlaag()
	{
		int kindMetFocus = bepaalKindMetFocus();
		if(kindMetFocus == kinderen.size() - 1)
		{	maakNieuwKind();
		}
		kinderen.get(kindMetFocus + 1).neemFocus("rechts");
		zetMaat();
		repaint();
	}
	
	public void maakNieuwKind()
	{
		FormuleRegel kind = new FormuleRegel(formuleVak);
		kind.setFont(getFont());
		kinderen.add(kind);
		add(kind);
	}
	
	public void deleteKind()
	{
		int kindMetFocus = bepaalKindMetFocus();
		FormuleRegel kind = kinderen.get(kindMetFocus);
		//testen of kind leeg is.
		//Als het kind leeg is: kind verwijderen. 
		if(kind.toString().length() > 0)
			return;
		remove(kind);
		kinderen.remove(kindMetFocus);
		if(kindMetFocus < kinderen.size())
			kinderen.get(kindMetFocus).neemFocus("rechts");
		else
			kinderen.get(kindMetFocus - 1).neemFocus("rechts");
		zetMaat();
		repaint();
	}
	
	public void zetMaat()
	{	maakMaat();
		if(getParent()instanceof FormuleElement)((FormuleElement)getParent()).zetMaat();	
	}
	
	public void vulVak(String s)
	{
		//hier komt altijd een string in waarin de elementen zijn gescheiden door $n. 
		kinderen = new Vector<FormuleRegel>();
		kind1.removeAll();
		remove(kind1);
		
		while(s.length()>0)
		{	char ch0 = s.charAt(0);
			if(ch0=='@')
			{	break;
			}
			else if(ch0=='$')
			{	int niv = 1;
				int eind = 0;
				String sz = s.substring(2);
				while(niv>0 )
				{	int eindB = sz.indexOf("$");
					int eindE = sz.indexOf("@");
					if(eindB < eindE && eindB!=-1)
					{	eind = eindB;
						niv++;
					}
					else
					{	eind = eindE;
						niv--;
					}
					sz = sz.substring(eind+1);
				}
				eind = s.length()-sz.length();				
				char ch1 = s.charAt(1);
				if(ch1 == 'n')
				{	maakNieuwKind();
					kinderen.get(kinderen.size() - 1).insert(s.substring(2, eind));
					s = s.substring(eind);
				}
			}
		}
		for(int i = 0; i < kinderen.size(); i++)
			kinderen.get(i).zetMaat();	
	}
	
	public String toString()
	{	String string = "$Q";
		if(kinderen.size() > 0)
		{	for(int i = 0; i < kinderen.size(); i++)
				string = string + "$n" +  kinderen.get(i).toString() + "@";
		}
		return string + "@";
		
	}
}

