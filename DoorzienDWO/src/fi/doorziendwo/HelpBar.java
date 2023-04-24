package fi.doorziendwo;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.*;

public class HelpBar extends JPanel//Canvas
{   // applet frame    
    DoorzienFrame owner;
    DoorzienPanel owner2;
    
    int helpHeight;
    int offSet;
    
    String text = "";
    String paintText;

    String messageText = null;
    int messageX = 0;
    
	Font theFont = new Font("Dialog", Font.PLAIN, 12);
	FontMetrics theFM = getFontMetrics(theFont);
    
    
    public HelpBar(DoorzienFrame o)
    {   owner = o;
    	helpHeight = owner.helpHeight;
    	offSet = owner.offSet;
    
    }    

    public HelpBar(DoorzienPanel o)
    {   owner2 = o;
    	helpHeight = owner2.helpHeight;
    	offSet = owner2.offSet;
    }    
    
    public void setText(String t)
    {   text = t;
        repaint();
    }    
    
    public void setMessage(String m, int mX)
    {   messageText = m;
        messageX = mX;
        repaint();
    }    
    // for BorderLayout    
    public Dimension getPreferredSize()
    {   if (owner2 == null)
    		return new Dimension(owner.getSize().width,
                             	 owner.helpHeight);
    	else
    		return new Dimension(owner2.getSize().width,
                	             owner2.helpHeight);

    }    
    
    public void paint(Graphics gr)
    {   
    	Graphics2D g = (Graphics2D) gr;
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_NORMALIZE);        
    	
        g.setColor(DoorzienFrame.helpBackground);
        g.fillRect(0, 0, getSize().width, getSize().height);
        g.setColor(Color.black);
//        g.drawRect(0, 0, getSize().width - 1, getSize().height - 1);
        g.drawLine(0, 0, getSize().width - 1, 0);
        g.drawLine(0, 0, 0, getSize().height - 1);
        g.drawLine(getSize().width - 1, 0, getSize().width - 1, getSize().height - 1);
        
        //Font fo = getFont();
        Font fo = theFont;
        if ((text.length() > 0) && text.substring(0,1).equals("#"))
        {   g.setColor(Color.red);        
            fo = new Font(fo.getName(), Font.BOLD, fo.getSize());
            paintText = text.substring(1);
        }
        else
        {    paintText = text;
        }
        FontMetrics fm = getFontMetrics(fo);
        g.setFont(fo);
        int vSpace = (getSize().height - fm.getHeight()) / 2;
        g.drawString(paintText, 2 * offSet, vSpace + fm.getAscent());
        
        if (messageText != null)
        {   Font mfo = getFont();
            g.setFont(mfo);
            FontMetrics mfm = getFontMetrics(mfo);
            int messageWidth = mfm.stringWidth(messageText + "  ");
            // try to center at messageX
            int bx = messageX - messageWidth / 2;
            if (bx < 0)
                bx = 0;
            if ((bx + messageWidth) > getSize().width)
                bx = getSize().width - messageWidth;
            g.setColor(new Color(255, 255, 198));
            g.fillRect(bx, 0, messageWidth, helpHeight -1);
            g.setColor(Color.black);
            g.drawRect(bx, 0, messageWidth - 1, helpHeight);            
            g.drawString(" " + messageText, bx, vSpace + mfm.getAscent());            
            
        }
    }    
    
    
}    
