package fi.javalogoweb;

import java.awt.*;
import java.awt.event.*;

import fi.javalogoweb.schuifobjects.*;


public class CommandContainer extends CommandComponent 
{
	protected CommandComponent[] commands;
	protected int caretPos;
	
	public CommandContainer(int x, int y, int b, int h, SchuifVeld sv)
	{	super(x,y,b,h,sv);
		commands = new CommandComponent[200];
	}
	
	public Component add(Component c)
	{	if(isStapel)return null;
		if(caretPos==-1)
		{	return getParent().add(c);
		}
		if(c instanceof CommandComponent)c.setBounds(0,getComponentCount()*23, getSize().width, c.getSize().height);
		Component comp = super.add(c, caretPos);
		reArange();
		caretPos = getComponentCount();
		return comp;
	}
	
	public void setSize(int b, int h)
	{	for(int i=0 ; i<getComponentCount() ; i++)
		{	Component c = getComponent(i);
			if(c instanceof CommandComponent)c.setSize(b,c.getSize().height);
		}
		super.setSize(b,h);
	}
	
	public void showCaret(int x, int y, boolean b)
	{	super.showCaret(x,y,b);
		if(getParent() instanceof CommandContainer && caretUp) setCaretPos(-1);
	}
	
	public CommandContainer getCommandContainerAt(int x, int y)
	{	CommandContainer cc = null;
		Component c = getComponentAt(x,y);
		if(c!=this && c!=null  && c instanceof CommandContainer) 
		{	cc = (CommandContainer)c;
			return cc.getCommandContainerAt(x - cc.getLocation().x,y - cc.getLocation().y);
		}
		
		return this;
	}
	public void remove(Component c)
	{	super.remove(c);
		reArange();
		caretPos = getComponentCount();
	}
	
	public void setCaretPos(int pos)
	{	caretPos = pos;
		
	}
	
	public void setCaret(CommandComponent cc, boolean up)
	{	for(int i=0 ; i<getComponentCount() ; i++)
		{	if(cc == getComponent(i)) 
			{	if(up)caretPos = i;
				else caretPos = i+1;
			}
		}
	}
	
	public void reArange()
	{	int hoogte = 0;
		for(int i=0 ; i<getComponentCount()  ; i++)
		{	Component c = getComponent(i);
			if(c instanceof CommandComponent)
			{	getComponent(i).setLocation(0,hoogte);
				hoogte += getComponent(i).getSize().height-2;
			}
		}
	}
	
	public void zetVast(boolean b)
	{	vast = b;
		for(int i=0 ; i<200 ; i++)
		{	if(commands[i] != null) commands[i].zetVast(b);
		}
	}
	
	
	
	
}
