package fi.beans.lwmobjects_swing;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * LWMBufferPanel is een Panel met double buffering voor tekenen zonder flikkeren
 * 
 * @author Paul Bergervoet
 *
 * @version 1, 15 augustus 2000
 */

public class LWMBufferPanel extends JLayeredPane
{	
	// Variabelen
	private int width;
	private int height;
	
	// Background painting, controle
	private Image bufferimage;
	private Dimension bufferimageDim;
	private Graphics bufferimageGraphics;

/**
 * Maakt een LWMBufferPanel met de opgegeven breedte en hoogte
 *
 * Een LWMBufferPanel beschikt standaard niet over een LayoutManager. We gaan uit
 * van lightweight componenten die direct op hun plaats gezet worden
 */
	public LWMBufferPanel(int w, int h)
	{	setLayout(null);
		
		width = w;
		height = h;
		setSize(width, height);
	}
	
	public Dimension getMinimumSize()
	{	return new Dimension(width, height);
	}
 
	public Dimension getPreferredSize()
	{	return new Dimension(width, height);
	}

	/*public void paint(Graphics g)
	{	// xxx fixed size maken???
		Dimension dd = getSize();				// check eventuele resize...
		if ( 	bufferimage == null || 			// Image is er nog niet of ..
			bufferimageDim.width != dd.width ||	// breedte/lengte van Canvas
			bufferimageDim.height != dd.height	// veranderd: dan nieuwe!
		   )
		{	if ( bufferimageGraphics != null )
			{	bufferimageGraphics.dispose();	// oude opruimen
			}
			bufferimage = createImage(dd.width, dd.height);
			bufferimageDim = dd;
			bufferimageGraphics = bufferimage.getGraphics();
		}
		super.paint(bufferimageGraphics);
		g.drawImage(bufferimage, 0, 0, null);
	}
	
	public void update(Graphics g)
	{	paint(g);
	}
	*/
}
