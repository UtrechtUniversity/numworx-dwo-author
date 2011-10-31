package fi.javalogoweb;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import logotekenap.Tekenblad;

import fi.javalogoweb.schuifobjects.SchuifVeld;

public class KeuzeCommandComponent extends CommandContainer  implements ActionListener
{
	protected boolean caretIn;
	String jaString = "ja";
	String neeString = "nee";
	
	public KeuzeCommandComponent(int x, int y, int b, int h, SchuifVeld sv)
	{	super(x,y,b,h,sv);
		commandString = "Keuze ";
		kommaString = null;
		haakjeString = "  ";
		
		
		
		bc = new BooleanComponent(locationGc1,2,fm.stringWidth("0"),21);
		bc.zetInstelbaar(true);
		bc.zetTekst("0=0");
		bc.addActionListener(this);
		add(bc,0);
				
		zetMaat();
	}
	
	

	public Component add(Component c)
	{	if(caretPos==-1 || isStapel)
		{	return getParent().add(c);
		}
		if(c instanceof CommandComponent) {
		    c.setBounds(c.getX()-getLocationOpSchuifveld().x,25+getComponentCount()*23, getSize().width/2+1, c.getSize().height);
		}
		Component comp = super.add(c, caretPos);
		reArange();
		caretPos = getComponentCount();
		return comp;
	}
	
	public void showCaret(int x, int y, boolean b)
	{	Rectangle upRect = new Rectangle(0,0,getSize().width,10);
		Rectangle downRect = new Rectangle(0,getSize().height-10,getSize().width,10);
		boolean up = upRect.contains(x-getAbsLocation().x, y-getAbsLocation().y);
		boolean down = downRect.contains(x-getAbsLocation().x, y-getAbsLocation().y);
		if(b)
		{	caretUp = up;
			caretDown = down;
			caretIn = !up && !down;
		}
		else 
		{	caretUp = false;
			caretDown = false;
			caretIn = false;
		}
		if(b) ((CommandContainer)getParent()).setCaret(this,up);
		if(getParent() instanceof CommandContainer && (caretUp || caretDown)) setCaretPos(-1);
		else setCaretPos(0);
	}
	
	public void setSize(int b, int h)
	{	for(int i=0 ; i<getComponentCount() ; i++)
		{	Component c = getComponent(i);
			if(c instanceof CommandComponent) c.setSize(b-25,c.getSize().height);
		}
		super.setSize(b,h);
	}
	
	public void setBounds(int x, int y, int b, int h)
	{	for(int i=0 ; i<getComponentCount() ; i++)
		{	Component c = getComponent(i);
			if(c instanceof CommandComponent) c.setSize(b/2+1,c.getSize().height);
		}
		super.setBounds(x,y,b,h);
	}
	
	public void reArange()
	{	int hoogteLinks = 25;
		for(int i=0 ; i<getComponentCount(); i++)
		{	Component c = getComponent(i);
			if(c instanceof CommandComponent && c.getX()<getWidth()/2-3) 
			{	c.setLocation(0,hoogteLinks);
			   	hoogteLinks += c.getSize().height-2;
			}
		}
		int hoogteRechts = 25;
		for(int i=0 ; i<getComponentCount(); i++)
        {   Component c = getComponent(i);
            if(c instanceof CommandComponent && c.getX()>getWidth()/2-4) 
            {   c.setLocation(getWidth()/2-1,hoogteRechts);
            	hoogteRechts += c.getSize().height-2;
            }
        }
		setSize(getSize().width, Math.max(48,Math.max(hoogteLinks,hoogteRechts)+2));
		if(getParent() instanceof CommandContainer)((CommandContainer)getParent()).reArange();
		locationGc1 = getSize().width/2-10;
		bc.setLocation(locationGc1, 2);
	}
	
	public void paint(Graphics g)
	{	g.setColor(Color.orange);
		if(traceKleur)g.setColor(traceActiveColor);
		g.fillRect(0,0,getSize().width-1,getSize().height-1);
		g.setColor(Color.white);
		g.fillRect(0,25,getSize().width,getSize().height-26);
		g.setColor(Color.black);
		g.drawRect(0,0,getSize().width-1,getSize().height-1);
		g.drawRect(1,1,getSize().width-3,getSize().height-3);
		//if(caretUp)g.drawLine(2,2,getSize().width-3,2);
		//if(caretIn)g.drawLine(27,27,getSize().width-3,27);
		//if(caretDown)g.drawLine(2,getSize().height-3,getSize().width-3,getSize().height-3);
		if(caretUp)
		{	g.drawLine(2,2,getSize().width-3,2);
			g.drawLine(2,3,getSize().width-3,3);
		}
		if(caretIn)
		{
			g.drawLine(0,27,getSize().width-3,27);
			g.drawLine(0,28,getSize().width-3,28);
			
		}
		if(caretDown)
		{	g.drawLine(2,getSize().height-3,getSize().width-3,getSize().height-3);
			g.drawLine(2,getSize().height-4,getSize().width-3,getSize().height-4);
		}
		if(label!=null)g.drawString(label,20,18);
		g.drawLine(0,0,getSize().width/2,25);
		g.drawLine(0,1,getSize().width/2,26);
		g.drawLine(getSize().width,0,getSize().width/2,25);
        g.drawLine(getSize().width,1,getSize().width/2,26);
        g.drawLine(0,25,getSize().width,25);
        g.drawRect(0,26,getSize().width/2-1,getSize().height-28);
		g.drawRect(getSize().width/2,26,getSize().width/2,getSize().height-28);
		
		g.drawString(jaString, 10, 20);
		g.drawString(neeString, getSize().width-30, 20);
		super.paint(g);
	}
	
	public boolean teken(Tekenblad tb, VarSet varSet)
	{	boolean value = bc.geefWaarde(varSet);
		traceKleur = tb.checkKeuze(bc.geefTekst() + "? " + (value?"ja":"nee"));
		if(traceKleur)schuifveld.tekenOpnieuw();
		
		CommandComponent cc = null;
		if(value)
		{	for(int j=0 ; j<getComponentCount() ; j++)
			{	Component c = getComponent(j);
				if(c instanceof CommandComponent && c.getX()==0)
				{	boolean tracekleur = ((CommandComponent)c).teken(tb, varSet);
					if(tracekleur) return true;
					//if(!(c instanceof CommandContainer) && ((CommandComponent)c).traceKleur) 
					//{	cc = (CommandComponent)c;
					//	break;
					//}
				}
			}
		}
		else
		{	for(int j=0 ; j<getComponentCount() ; j++)
			{	Component c = getComponent(j);
				if(c instanceof CommandComponent && c.getX()>0)
				{	boolean tracekleur = ((CommandComponent)c).teken(tb, varSet);
					if(tracekleur) return true;
					//if(!(c instanceof CommandContainer) && ((CommandComponent)c).traceKleur) 
					//{	cc = (CommandComponent)c;
					//	break;
					//}
				}
			}
		}
		
		
		
		return false;
	}
	
	public String getCode(String tab)
	{	String s = tab + "Keuze " + bc.geefTekst()  + "\n" + tab +"{";
		String tabExtra = "      ";
		String tabNieuw = tab + tabExtra;
		for(int i=0 ; i<getComponentCount() ; i++)
		{	Component c = getComponent(i);
			if(c instanceof CommandComponent)
			{	if(i==0) s = s +((CommandComponent)c).getCode(tabExtra.substring(2));
				else s = s +((CommandComponent)c).getCode(tabNieuw);
				
			}
		}
		s = s + tab + "}\n";
		return s;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		schuifveld.tekenOpnieuw();
	}
}
