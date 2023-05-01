package fi.spot_problems_dwo.wiskopdr;

import java.awt.*;
import java.awt.event.*;
import fi.spot_problems_dwo.wiskopdr.formuleobjects.*;
import fi.beans.tooltip.*;

public class BewerkingButton extends FormuleButton implements ToolTipIF, MouseListener	
{	
	private String toolTip;
	
	public BewerkingButton(String s)
	{	super(s);
	}

	public void paintBuffer(Graphics g)
	{	//focus = true;
		bgColor = new Color(255,150,150);
		g.setColor(fgUseColor);
		{	g.setColor(bgColor);
			g.fillRect(0,0,getSize().width,getSize().height);
			//if(focus)
			{	if(actief)g.setColor(bgColor.darker());
			else g.setColor(bgColor.brighter());
				g.drawLine(0,0,getSize().width-1,0);
				g.drawLine(0,0,0,getSize().height-1);
				if(focus)
				{	g.drawLine(1,1,getSize().width-2,1);
					g.drawLine(1,1,1,getSize().height-2);
				}
				if(actief)g.setColor(bgColor.brighter());
				else g.setColor(bgColor.darker());
				g.drawLine(getSize().width-1,0,getSize().width-1,getSize().height-1);
				g.drawLine(0,getSize().height-1,getSize().width-1,getSize().height-1);
				if(focus)
				{	g.drawLine(getSize().width-2,1,getSize().width-2,getSize().height-2);
					g.drawLine(1,getSize().height-2,getSize().width-2,getSize().height-2);
				}
			}
		}
		int b = getSize().width;
		int h = getSize().height;
		g.setColor(Color.black);
		if(code.equals("plus"))
		{	g.drawLine(b/4,h/2,3*b/4,h/2);
			g.drawLine(b/2,h/4,b/2,3*h/4);
		}
		else if(code.equals("min"))
		{	g.drawLine(b/4,h/2,3*b/4,h/2);
		}
		else if(code.equals("maal"))
		{	g.drawLine(b/4,h/4,3*b/4,3*h/4);
			g.drawLine(b/4,3*h/4,3*b/4,h/4);
		}
		else if(code.equals("deel"))
			{	g.fillRect(b/2,h/4,2,2);
			g.fillRect(b/2,3*h/4,2,2);
		}
		else if(code.equals("haakjes"))
		{	g.drawString("(",5,15);
			g.drawString(")",11,15);
			g.drawLine(b/5,h/5,4*b/5,4*h/5);
		}
		else if(code.equals("ontbind"))
		{	g.drawString("(",11,15);
			g.drawString(")",5,15);
		}
		else if(code.equals("wortel"))
		{	g.drawLine(3,2*h/4,h/4,h-3);
			g.drawLine(4,2*h/4,h/4+1,h-3);
			g.drawLine(h/4+1,h-3,h/2,3);
			g.drawLine(h/2,3,b-3,3);
		}
		else if(code.equals("splits"))
		{	g.drawLine(10,10,16,16);
			g.drawLine(10,10,4,16);
			g.drawLine(10,4,10,10);
			g.drawLine(16,16,16,12);
			g.drawLine(16,16,12,16);
			g.drawLine(4,16,8,16);
			g.drawLine(4,16,4,12);
		}
		else if(code.equals("herleid"))
		{	g.drawRect(4,3,4,10);
			g.drawRect(11,3,4,10);
			g.drawLine(4,16,15,16);
			g.drawLine(4,16,3,15);
			g.drawLine(15,16,16,15);
		}
		else if(code.equals("gelijkwaardig"))
		{	g.setColor(Color.black);
			g.drawLine(10,3,10,17);
			g.drawLine(10,17,13,14);
			g.drawLine(10,17,7,14);
		}
		else if(code.equals("terug"))
		{	g.setColor(Color.black);
			g.drawLine(10,3,10,17);
			g.drawLine(10,3,13,6);
			g.drawLine(10,3,7,6);
		}
		/*if(code.equals("terug"))
		 *{	g.setFont(new Font("SansSerif", Font.PLAIN, 12));
			g.drawString("terug",5,15);
		}
		if(code.equals("hulp"))
		{	g.setFont(new Font("SansSerif", Font.PLAIN, 12));
			g.drawString("hulp",5,15);
		}*/
		else
		{	g.setFont(new Font("SansSerif", Font.PLAIN, 12));
			g.drawString(code,5,15);
		}
	}
	
	public void setToolTip(String toolTip)
	{	this.toolTip = toolTip;
       	ToolTipManager.registerComponent(this);
	}
	
	public String getToolTip() 
	{	return toolTip;
    }
    
    public Component getComponent() 
	{	return this;
    }
	
}