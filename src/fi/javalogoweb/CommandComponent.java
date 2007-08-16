package fi.javalogoweb;

import java.awt.*;
import java.applet.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import logotekenap.Tekenblad;

import fi.javalogoweb.schuifobjects.*;

public class CommandComponent extends SchuifComponent 
{
	protected boolean vast;
	protected String label;
	protected boolean caretUp, caretDown;
	protected boolean isStapel = true;
	
	protected String commandString;
	protected String kommaString;
	protected String haakjeString;
	
	protected int locationC;
	protected int locationGc1;
	protected int locationK;
	protected int locationGc2;
	protected int locationH;
	
	protected FontMetrics fm;
	
	protected GetalComponent gc1, gc2;
	protected Font font = new Font("SansSerif", Font.PLAIN, 12);
	
	public CommandComponent(int x, int y, int b, int h, SchuifVeld sv)
	{	super(x,y,b,h,sv);
		setLayout(null);
		schuifveld = sv;
		
		fm = getFontMetrics(font);
		
		
	}
	
	public void setLabel (String s)
	{	label = s;
	}
	
	public void zetVast(boolean b)
	{	vast = b;
	}
	
	public CommandComponent getCommandComponentAt(int x, int y)
	{	CommandComponent cc = null;
		Component c = getComponentAt(x,y);
		if(c!=this && c!=null && c instanceof CommandComponent) 
		{	cc = (CommandComponent)c;
			return cc.getCommandComponentAt(x - cc.getLocation().x,y - cc.getLocation().y);
			
		}
		
		return this;
	}
	
	public void showCaret(int x, int y, boolean b)
	{	Rectangle upRect = new Rectangle(0,0,getSize().width,getSize().height/2);
		boolean up = upRect.contains(x-getAbsLocation().x, y-getAbsLocation().y);
		if(b)
		{	caretUp = up;
			caretDown = !up;
		}
		else 
		{	caretUp = false;
			caretDown = false;
		}
		if(b) ((CommandContainer)getParent()).setCaret(this,up);
	}
	public void mousePressed(MouseEvent e)
	{	requestFocus();
		if(!vast)super.mousePressed(e);
	}
	
	public void mouseDragged(MouseEvent e)
	{	if(vast)return;
		if(isStapel)
		{	((JavaLogoSchuifVeld)schuifveld).zetStapel(this);
			isStapel = false;
			schuifveld.tekenOpnieuw();
		}
		((JavaLogoSchuifVeld)schuifveld).traceComponent(this);
		super.mouseDragged(e);
		
	}
	
	public void mouseReleased(MouseEvent e)
	{	if(vast)return;
		int x=getLocation().x+getSize().width/2;
		super.mouseReleased(e);
		((JavaLogoSchuifVeld)schuifveld).traceComponent(this);
		if(x<180 && !isStapel)
		{	((JavaLogoSchuifVeld)schuifveld).verwijder(this);
		}

	}
	
	public Point getAbsLocation()
	{	int x = getLocation().x;
		int y = getLocation().y;
		if(getParent() instanceof CommandComponent) 
		{	x = getLocation().x + ((CommandComponent)getParent()).getAbsLocation().x;
			y = getLocation().y + ((CommandComponent)getParent()).getAbsLocation().y;
		
		}
		return new Point(x,y);
	}
	
	public void zetMaat()
	{	locationC = 20;
		locationGc1 = locationC + fm.stringWidth(commandString);
		if(gc2 != null)
		{	locationK = locationGc1 + gc1.getSize().width;
			locationGc2 = locationK + fm.stringWidth(kommaString);
			locationH = locationGc2 + gc2.getSize().width;
		}
		else if(gc1 !=null)
		{	locationH = locationGc1 + gc1.getSize().width;
		}
		else 
		{	locationH = locationGc1;
		}
		if(gc1!=null)gc1.setLocation(locationGc1, 2);
		if(gc2!=null)gc2.setLocation(locationGc2, 2);	
	}
	
	public void tekenOpnieuw()
	{	schuifveld.tekenOpnieuw();
	}
	
	public void paint(Graphics g)
	{	g.setColor(Color.orange);
		//g.fillRect(0,0,getSize().width-1,getSize().height-1);
		g.setColor(Color.black);
		//g.drawRect(0,0,getSize().width-1,getSize().height-1);
		//g.drawRect(1,1,getSize().width-3,getSize().height-3);
		//if(caretUp)g.drawLine(2,2,getSize().width-3,2);
		//if(caretDown)g.drawLine(2,getSize().height-3,getSize().width-3,getSize().height-3);
		//if(label!=null)g.drawString(label,20,20);
		if(commandString != null)g.drawString(commandString,locationC,18);
		if(kommaString != null) g.drawString(kommaString,locationK,18);
		if(haakjeString != null) g.drawString(haakjeString,locationH,18);
		super.paint(g);
	}
	
	public void teken(Tekenblad tb, VarSet varSet)
	{	
	}
	
	public void mouseMoved(MouseEvent e){;}
	public void mouseExited(MouseEvent e){;}
	public void mouseClicked(MouseEvent e){;}
	public void mouseEntered(MouseEvent e){;}

}
