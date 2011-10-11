package fi.javalogoweb;

import java.awt.*;
import java.awt.event.*;

import fi.beans.stringutils.StringUtils;
import fi.javalogoweb.schuifobjects.*;


public class CommandContainer extends CommandComponent 
{
	protected CommandComponent[] commands;
	protected int caretPos;
	
	public CommandContainer(int x, int y, int b, int h, SchuifVeld sv)
	{	super(x,y,b,h,sv);
		commands = new CommandComponent[200];
	}
	
	public Point getLocationOpSchuifveld()
    {   int x = getLocation().x;
        int y = getLocation().y;
        if(getParent()instanceof SchuifVeld)
        {   return new Point(x,y);
        }
        else
        {   Point p = ((CommandContainer)getParent()).getLocationOpSchuifveld();
            return new Point(x + p.x, y + p.y);
        }
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
	
	public void removeAll()
	{
		super.removeAll();
		reArange();
		caretPos = getComponentCount();
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
	
	public void setCode(String code)
	{	code = code.trim();
		String[] codeCommandStrings = StringUtils.split(code, "\n");
		int indexStartBody = -1;
		int indexEindBody = -1;
		int level=0;
		String s = "";
		for(int i=0 ; i<codeCommandStrings.length ; i++)
		{	if(codeCommandStrings[i] != null)
			{	codeCommandStrings[i] = codeCommandStrings[i].trim();
				if(codeCommandStrings[i].charAt(0)=='{')
				{	level++;
					if(level==1)indexStartBody = i;
				}
				
				if(level==0) s = codeCommandStrings[i];
				else s = s + "\n" + codeCommandStrings[i];
				
				if(codeCommandStrings[i].charAt(0)=='}')
				{	level--;
					if(level==0)indexEindBody = i;
				}
				if(level==0 && !(s.equals(codeCommandStrings[i]) && s.length()>7 && s.substring(0,7).equals("Herhaal")))
				{	makeCommandComponent(s);
					System.out.println(s);
				}
			}
		}
		schuifveld.tekenOpnieuw();
	}
	
	public void makeCommandComponent(String commandCode)
	{	CommandComponent cc = null;
		String param1;
		String param2;
		int index = -1;
		if(commandCode.length()>8 && commandCode.substring(0,8).equals("vooruit("))
		{	cc = new VooruitCComponent(-100,-100,25,25, schuifveld);
			index = 8;
		}
		else if(commandCode.length()>7 && commandCode.substring(0,7).equals("rechts("))
		{	cc = new RechtsCComponent(-100,-100,25,25, schuifveld);
			index = 7;
		}
		else if(commandCode.length()>6 && commandCode.substring(0,6).equals("links("))
		{	cc = new LinksCComponent(-100,-100,25,25, schuifveld);
			index = 6;
		}
		else if(commandCode.length()>5 && commandCode.substring(0,5).equals("stap("))
		{	cc = new StapCComponent(-100,-100,25,25, schuifveld);
			index = 5;
		}
		else if(commandCode.length()>7 && commandCode.substring(0,7).equals("penAan("))
		{	cc = new PenAanCComponent(-100,-100,25,25, schuifveld);
			index = 7;
		}
		else if(commandCode.length()>7 && commandCode.substring(0,7).equals("penUit("))
		{	cc = new PenUitCComponent(-100,-100,25,25, schuifveld);
			index = 7;
		}
		else if(commandCode.length()>8 && commandCode.substring(0,8).equals("vulAan(\""))
		{	cc = new VulAanCComponent(-100,-100,25,25, schuifveld);
			index = 8;
		}
		else if(commandCode.length()>7 && commandCode.substring(0,7).equals("vulUit("))
		{	cc = new VulUitCComponent(-100,-100,25,25, schuifveld);
			index = 7;
		}
		else if(commandCode.length()>7 && commandCode.substring(0,7).equals("Herhaal"))
		{	cc = new HerhaalCommandComponent(-100,-100,25,50, schuifveld);
			index = 7;
		}
		else if(commandCode.indexOf("=")>-1) 
		{	cc = new VarCComponent(-100,-100,25,25, schuifveld);
			index = commandCode.indexOf("=");
		}
		
		else
		{	DeeltaakCComponent[] dtcs = ((JavaLogoSchuifVeld)schuifveld).geefDtcCommands();
			String command0 = dtcs[0].getCommandName();
			String command1 = dtcs[1].getCommandName();
			String command2 = dtcs[2].getCommandName();
			String command3 = dtcs[3].getCommandName();
			String command4 = dtcs[4].getCommandName();
			String dtNaam = commandCode.trim();
			int nr = -1;
			if(dtNaam.equals(command0)) nr = 0;
			else if(dtNaam.equals(command1)) nr = 1;
			else if(dtNaam.equals(command2)) nr = 2;
			else if(dtNaam.equals(command3)) nr = 3;
			else if(dtNaam.equals(command4)) nr = 4;
			if(nr!=-1)
			{	cc = new DeeltaakCComponent(-100,-100,25,25, schuifveld);
				((DeeltaakCComponent)cc).setCommandName(dtNaam);
				((DeeltaakCComponent)cc).zetDeeltaakContainer(dtcs[nr].geefProgrammaComponent());
			}
		}
	
		
		if(cc!=null && cc instanceof HerhaalCommandComponent)
		{	((JavaLogoSchuifVeld)schuifveld).voegToe(cc);
			cc.isStapel = false;
			String[] params = StringUtils.split(commandCode," ");
			if(params.length>2)
			{	param1 = params[1];
				cc.setParam1(param1);
			}
			add(cc);
			
			int indexStartBody = -1;
			int indexEindBody = -1;
			int level=0;
			String s = "";
			for(int i=0 ; i<commandCode.length() ; i++)
			{	if(commandCode.charAt(i)=='{')
				{	level++;
					if(level==1)indexStartBody = i;
				}
				if(commandCode.charAt(i)=='}')
				{	level--;
					if(level==0)indexEindBody = i;
				}
				if(indexStartBody<indexEindBody && level==0)
				{	((CommandContainer)cc).setCode(commandCode.substring(indexStartBody+1,indexEindBody));
					System.out.println(commandCode.substring(indexStartBody+1,indexEindBody));
					break;
				}
			}	
		}
		else if(cc!=null && cc instanceof VarCComponent)
		{	((JavaLogoSchuifVeld)schuifveld).voegToe(cc);
			cc.isStapel = false;
			String[] params = StringUtils.split(commandCode,"=");
			if(params.length>0)
			{	param1 = params[0];
				cc.setParam1(param1);
			}
			if(params.length>1)
			{	param2 = params[1];
				cc.setParam2(param2);
			}
			add(cc);
		}
		else if(cc!=null && cc instanceof DeeltaakCComponent)
		{	((JavaLogoSchuifVeld)schuifveld).voegToe(cc);
			cc.isStapel = false;
			((DeeltaakCComponent)cc).setCommandName(commandCode);
			add(cc);
		}
		else if(cc!=null)
		{
			((JavaLogoSchuifVeld)schuifveld).voegToe(cc);
			cc.isStapel = false;
			int indexEind = commandCode.lastIndexOf(")");
			//int indexEind = commandCode.indexOf(")",index);
			if(cc instanceof VulAanCComponent) indexEind = commandCode.indexOf("\"",index);
			if(indexEind>-1) 
			{	String[] params = new String[1];
				params[0] = commandCode.substring(index,indexEind);
				if(cc instanceof StapCComponent)  params = StringUtils.split(commandCode.substring(index,indexEind),",");
				if(params.length>0)
				{	param1 = params[0];
					if(param1!=null && !param1.equals("")) cc.setParam1(param1);
				}
				if(params.length>1)
				{	param2 = params[1];
					if(!param2.equals("")) cc.setParam2(param2);
				}	
			}
			add(cc);
		}
	}
	
	
}
