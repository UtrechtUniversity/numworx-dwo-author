/*
 * @(#)BasicScrollBarUI.java	1.75 03/01/23
 *
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package fi.beans.wnwidgets;


import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;

import java.beans.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.BasicScrollBarUI;


/**
 * Implementation of ScrollBarUI for the Basic Look and Feel
 *
 * @version 1.75 01/23/03
 * @author Rich Schiavi
 * @author David Kloba
 * @author Hans Muller
 */
public class MWScrollBarUI   extends BasicScrollBarUI implements LayoutManager, SwingConstants
{
	private Image scrollButtonImage;
	private int scrollBarWidth;


    public MWScrollBarUI() {
		super();
		scrollButtonImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/scrollbutton.png"));
	}
    
    public static ComponentUI createUI(JComponent c)    {
        return new MWScrollBarUI();
    }

    public void installUI(JComponent c)   {
	
    	super.installUI(c);
		MediaTracker tr = new MediaTracker(c);
		tr.addImage(scrollButtonImage, 0);
		try {
			tr.waitForAll();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	scrollbar = (JScrollBar)c;
        thumbRect = new Rectangle(0, 0, 0, 0);
        trackRect = new Rectangle(0, 0, 0, 0);
		installDefaults();
		installComponents();
		installListeners();
		installKeyboardActions();
    }

    protected JButton createDecreaseButton(int orientation)  {
        return new MWArrowButton(orientation, 
				    UIManager.getColor("ScrollBar.thumb"),
				    UIManager.getColor("ScrollBar.thumbShadow"),
				    UIManager.getColor("ScrollBar.thumbDarkShadow"),
				    UIManager.getColor("ScrollBar.thumbHighlight"));
    }

    protected JButton createIncreaseButton(int orientation)  {
        return new MWArrowButton(orientation,
				    UIManager.getColor("ScrollBar.thumb"),
				    UIManager.getColor("ScrollBar.thumbShadow"),
				    UIManager.getColor("ScrollBar.thumbDarkShadow"),
				    UIManager.getColor("ScrollBar.thumbHighlight"));
    }
   
    public void paint(Graphics g, JComponent c) {
		paintTrack(g, c, getTrackBounds());		
		paintThumb(g, c, getThumbBounds());
    }

    public Dimension getPreferredSize(JComponent c) {
		return (scrollbar.getOrientation() == JScrollBar.VERTICAL)
		    ? new Dimension(14, 48)
		    : new Dimension(48, 14);
    }

    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds)  {
        g.setColor(new Color(200,200,200));
        if(scrollbar.getOrientation() == JScrollBar.VERTICAL)
        	g.drawRect(trackBounds.x, trackBounds.y-1, trackBounds.width, trackBounds.height);
        else
        	g.drawRect(trackBounds.x, trackBounds.y-1, trackBounds.width, trackBounds.height);

		if(trackHighlight == DECREASE_HIGHLIGHT)	{
		    paintDecreaseHighlight(g);
		} 
		else if(trackHighlight == INCREASE_HIGHLIGHT)		{
		    paintIncreaseHighlight(g);
		}
    }
	
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
		if(thumbBounds.isEmpty() || !scrollbar.isEnabled())	{
		    return;
		}

        int w = thumbBounds.width;
        int h = thumbBounds.height;		

		g.translate(thumbBounds.x, thumbBounds.y);
		g.drawImage(scrollButtonImage, 0,h/2-17, null);
		g.translate(-thumbBounds.x, -thumbBounds.y);
    }



   
   
}
            
            
