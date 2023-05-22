package fi.wiskopdr;

import java.awt.*;
import java.awt.event.*;
import fi.wiskopdr.formuleobjects.*;

public class BewerkingButton extends FormuleButton implements MouseListener	
{	
	private String toolTip;
	
	public BewerkingButton(String s)
	{	super(s);
	}

	public void paintComponent(Graphics gr)
	{	Graphics g;
	    /*if(WiskOpdr.deployVariant!=null && WiskOpdr.deployVariant.equals("GR"))
	    {     g = (Graphics2D)gr;
	          ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    }
	    else */
	    g=gr;
		//focus = true;
        if(WiskOpdr.deployVariant!=null && WiskOpdr.deployVariant.equals("GR")) {
            g.setColor(fgColor);
            {   g.setColor(bgColor);
                g.fillRect(0,0,getSize().width,getSize().height);
                if(focus)
                {   if(actief)g.setColor(bgColor.darker());
                    else g.setColor(bgColor.brighter());
                    g.drawLine(0,0,getSize().width-1,0);
                    g.drawLine(0,0,0,getSize().height-1);
                    if(actief)g.setColor(bgColor.brighter());
                    else g.setColor(bgColor.darker());
                    g.drawLine(getSize().width-1,0,getSize().width-1,getSize().height-1);
                    g.drawLine(0,getSize().height-1,getSize().width-1,getSize().height-1);
                }
            }
        }
        else {
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
        }
        if(WiskOpdr.deployVariant!=null && WiskOpdr.deployVariant.equals("MW"))
		{
			
			if(code.equals("plus") 
					|| code.equals("maal") 
					|| code.equals("deel")
					|| code.equals("min") 
					|| code.equals("herleid") 
					|| code.equals("ontbind") 
					|| code.equals("haakjes")
					|| code.equals("wortel")
					|| code.equals("splits")
					|| code.equals("abc")
					|| code.equals("sub"))
				g.drawImage(getImage("wnformbuttonrood.gif"),0,0,null);
			
		}
		int b = getSize().width;
		int h = getSize().height;
		
		
		g.setColor(Color.black);
		if(code.equals("plus"))
		{	g.drawLine(b/4+1,h/2,3*b/4-1,h/2);
			g.drawLine(b/2,h/4+1,b/2,3*h/4-1);
		}
		else if(code.equals("min"))
		{	g.drawLine(b/4+1,h/2,3*b/4-1,h/2);
		}
		else if(code.equals("maal"))
		{	g.drawLine(b/4+1,h/4+1,3*b/4-1,3*h/4-1);
			g.drawLine(b/4+1,3*h/4-1,3*b/4-1,h/4+1);
		}
		else if(code.equals("deel"))
		{	g.fillRect(b/2,h/4,2,2);
			g.fillRect(b/2,3*h/4-1,2,2);
			g.drawLine(b/4+1,b/2,3*b/4-1,b/2);
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
		{	g.drawRect(b/5,h/6,b/5,h/2);
			g.drawRect(b/2+1,h/6,b/5,h/2);
			g.drawLine(b/5,4*h/5,4*b/5-1,4*h/5);
			g.drawLine(b/5,4*h/5,b/5-1,4*h/5-1);
			g.drawLine(4*b/5-1,4*h/5,4*b/5,4*h/5-1);
		}
		else if(code.equals("abc") || code.equals("123"))
		{	g.setFont(new Font("SansSerif", Font.PLAIN, 10));
			g.drawString(WiskOpdr.rb.getString("abc"),1,15);
			g.setFont(new Font("SansSerif", Font.PLAIN, 13));
		}
		else if(code.equals("gelijkwaardig"))
		{	if(WiskOpdr.deployVariant!=null && WiskOpdr.deployVariant.equals("GR")) {
                g.setFont(new Font("SansSerif", Font.PLAIN, 12));
                g.drawString("volgende regel",5,15);
            }
            else {
                g.setColor(Color.black);
                g.drawLine(b/2,h/6,b/2,h-h/6);
                g.drawLine(b/2,h-h/6,b/2+3,h-2*h/6);
    			g.drawLine(b/2,h-h/6,b/2-3,h-2*h/6);
            }      
		}
		else if(code.equals("terug"))
		{	if(WiskOpdr.deployVariant!=null && WiskOpdr.deployVariant.equals("GR")) {
            g.setFont(new Font("SansSerif", Font.PLAIN, 12));
            g.drawString("vorige regel",5,15);
            }
            else {g.setColor(Color.black);
    			g.drawLine(b/2,h/6,b/2,h-h/6);
    			g.drawLine(b/2,h/6,b/2+3,2*h/6);
    			g.drawLine(b/2,h/6,b/2-3,2*h/6);
            }
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
		{	g.setFont(new Font("TimesRoman", Font.PLAIN, 10));
			g.drawString(code,5,15);
			g.setFont(new Font("SansSerif", Font.PLAIN, 12));
		}
	}
	
	public void setToolTip(String toolTip)
	{	this.toolTip = toolTip;
       	setToolTipText(toolTip); // swing tooltips.
	}
	
	public String getToolTip() 
	{	return toolTip;
    }
    
    public Component getComponent() 
	{	return this;
    }
	
}