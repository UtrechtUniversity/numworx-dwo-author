package fi.javalogoweb;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import logotekenap.Rekenblad;
import logotekenap.Tekenblad;

import fi.javalogoweb.schuifobjects.SchuifVeld;

public class BlokCommandComponent extends CommandContainer  implements ActionListener
{
	protected boolean caretIn;
	
	public BlokCommandComponent(int x, int y, int b, int h)
	{	super(x,y,b,h,null);
		commandString = "";
		kommaString = null;
		haakjeString = "";
		
			
		zetMaat();
	}

	public Component add(Component c)
	{	if(caretPos==-1 || isStapel)
		{	return getParent().add(c);
		}
		if(c instanceof CommandComponent) c.setBounds(0,0+getComponentCount()*23, getSize().width, c.getSize().height);
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
			if(c instanceof CommandComponent) c.setSize(b,c.getSize().height);
		}
		super.setSize(b,h);
	}
	
	public void setBounds(int x, int y, int b, int h)
	{	for(int i=0 ; i<getComponentCount() ; i++)
		{	Component c = getComponent(i);
			if(c instanceof CommandComponent) c.setSize(b,c.getSize().height);
		}
		super.setBounds(x,y,b,h);
	}
	
	public void reArange()
	{	int hoogte = 25;
		for(int i=0 ; i<getComponentCount()  ; i++)
		{	Component c = getComponent(i);
			if(c instanceof CommandComponent) 
			{	c.setLocation(0,hoogte);
				hoogte += c.getSize().height-2;
			}
		}
		setSize(getSize().width, Math.max(25,hoogte+2));
		if(getParent() instanceof CommandContainer)((CommandContainer)getParent()).reArange();
	}
	
	public void paint(Graphics g)
	{	
		g.setColor(Color.red);
		g.fillRect(0,0,getSize().width-1,getSize().height-1);
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
			g.drawLine(27,27,getSize().width-3,27);
			g.drawLine(27,28,getSize().width-3,28);
			
		}
		if(caretDown)
		{	g.drawLine(2,getSize().height-3,getSize().width-3,getSize().height-3);
			g.drawLine(2,getSize().height-4,getSize().width-3,getSize().height-4);
		}
		if(label!=null)g.drawString(label,20,18);
		//g.drawRect(25,25,getSize().width-26,getSize().height-26);
		//g.drawRect(26,26,getSize().width-28,getSize().height-28);
		super.paint(g);
	}
	
	public boolean teken(Tekenblad tb, VarSet varSet)
	{	for(int j=0 ; j<getComponentCount() ; j++)
		{	Component c = getComponent(j);
			if(c instanceof CommandComponent)
			{	boolean tracekleur = ((CommandComponent)c).teken(tb, varSet);
				if(tracekleur) return true;
				//if(!(c instanceof CommandContainer) && ((CommandComponent)c).traceKleur) 
				//{	cc = (CommandComponent)c;
				//	break;
				//}
			}
		}
		return false;
	}
	
	public boolean reken(Rekenblad rb, VarSet varSet)
	{	for(int j=0 ; j<getComponentCount() ; j++)
		{	Component c = getComponent(j);
			if(c instanceof CommandComponent)
			{	boolean tracekleur = ((CommandComponent)c).reken(rb, varSet);
				if(tracekleur) return true;
				//if(!(c instanceof CommandContainer) && ((CommandComponent)c).traceKleur) 
				//{	cc = (CommandComponent)c;
				//	break;
				//}
			}
		}
		return false;
	}
	
	public String getCode(String tab)
	{	String s = tab +"{";
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
