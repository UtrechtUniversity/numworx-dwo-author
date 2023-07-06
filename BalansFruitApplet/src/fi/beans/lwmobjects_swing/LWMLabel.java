/*
 * Created on Feb 8, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fi.beans.lwmobjects_swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;

import fi.beans.lwmobjects_swing.LWMObject;

public class LWMLabel extends LWMObject
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String text;
   
    private int paintWith(Graphics g) {
        FontMetrics fm = g.getFontMetrics();
        char[] chars = text.toCharArray();
        int width = getSize().width;
        int x = 0;
        int xOrg = 0;
        if(hasBorder())
        {
        	xOrg = fm.charWidth(' ') + getBorderWidth();
        	x = xOrg;
        	width -= xOrg*2;
        }
        int y = fm.getAscent();
        for(int i = 0; i < chars.length; )
        {
            int len = 1;
            while(chars.length > (i+len) && chars[i+len-1] != ' ' && chars[i+len-1]!='\n')
            {
                len++;
            }
            if(len > 1) len --;
//          System.err.println("i=" + i + ",len="+len);
            int w = fm.charsWidth(chars, i, len);
            if(chars[i] == '\n' || ((x+w) > width && chars[i] != ' '&& x!=0))
            {
                x = xOrg;
                y += fm.getHeight();
            }
            if(chars[i] == '\n') 
            {
                len --; i++; w = fm.charsWidth(chars, i, len);
            }
            if(g!= null)
                g.drawChars(chars, i, len, x, y);
            x += w;
            i += len;
        }
        if(x==xOrg) y -= fm.getHeight();
        return y+fm.getDescent();       
    }
   
    /* (non-Javadoc)
     * @see fi.beans.lwmobjects.LWMObject#paint(java.awt.Graphics)
     */
    public void paint(Graphics g)
    {
        super.paint(g);
        if(text==null)
            return;        
        g.setColor(getForeground());
        paintWith(g);
    }

    public LWMLabel(int w, int h)
    {
        super(w, h);
    }

    public LWMLabel(Color c, int w, int h)
    {
        super(c, w, h);
    }

    public LWMLabel(Image i)
    {
        super(i);
     }

    public LWMLabel(Image i, int w, int h)
    {
        super(i, w, h);
   }

    /**
     * @return Returns the text.
     */
    public String getText()
    {
        return text;
    }

    /**
     * @param text The text to set.
     */
    public void setText(String text)
    {
        this.text = text;
        repaint();
    }

    /* (non-Javadoc)
     * @see fi.beans.lwmobjects.LWMComponent#getPreferredSize()
     */
    public Dimension getPreferredSize()
    {
        Dimension f =  super.getPreferredSize();
        Graphics  g = getGraphics();
        if (g != null)
        {
            int h = paintWith(g);
            f.height = h;
        }
        return f;
    }


}
