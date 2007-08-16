package fi.javalogoweb;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;

import logotekenap.*;

import fi.javalogoweb.schuifobjects.SchuifVeld;

public class ProgrammaComponent extends CommandContainer
{	
	public int defaultHoogte;
	
	public ProgrammaComponent(int x, int y, int b, int h, SchuifVeld sv)
	{	super(x,y,b,h,sv);
		defaultHoogte = h;
		isStapel = false;
		
	}
	
	public Component add(Component c)
	{	if(isStapel)return null;
		if(caretPos==-1)
		{	return getParent().add(c);
		}
		if(c instanceof CommandComponent)c.setBounds(0,12+getComponentCount()*23, getSize().width, c.getSize().height);
		Component comp = super.add(c, caretPos);
		caretPos++;
		reArange();
		caretPos = getComponentCount();
		return comp;
	}
	public void paint(Graphics g)
	{	g.setColor(Color.white);
		g.fillRect(0,12,getSize().width-1,getSize().height-25);
		g.setColor(Color.black);
		g.drawRect(0,12,getSize().width-1,getSize().height-25);
		g.drawRect(1,13,getSize().width-3,getSize().height-27);
		if(caretUp)g.drawLine(0,11,getSize().width-1,11);
		if(caretDown)g.drawLine(0,getSize().height-12,getSize().width-1,getSize().height-12);
		if(label!=null)g.drawString(label,20,18);
		super.paint(g);
	}
	
	public void showCaret(int x, int y, boolean b)
	{	Rectangle upRect = new Rectangle(0,0,getSize().width,36);
		boolean up = upRect.contains(x-getAbsLocation().x, y-getAbsLocation().y);
		if(b)
		{	caretUp = up;
			caretDown = !up;
		}
		else 
		{	caretUp = false;
			caretDown = false;
		}
		if(b) setCaret(this,up);
	}
	
	public void reArange()
	{	int hoogte = 12;
		for(int i=0 ; i<getComponentCount()  ; i++)
		{	Component c = getComponent(i);
			if(c instanceof CommandComponent)
			{	getComponent(i).setLocation(0,hoogte);
				hoogte += getComponent(i).getSize().height-2;
			}
		}
		setSize(getSize().width, Math.max(defaultHoogte,hoogte+14));
		((JavaLogoSchuifVeld)schuifveld).scroll(caretPos==getComponentCount());
	}
	
	public void teken(Tekenblad tb, VarSet varSet)
	{	for(int i=0 ; i<getComponentCount() ; i++)
		{	Component c = getComponent(i);
			if(c instanceof CommandComponent)((CommandComponent)c).teken(tb, varSet);
		}
	}
}
