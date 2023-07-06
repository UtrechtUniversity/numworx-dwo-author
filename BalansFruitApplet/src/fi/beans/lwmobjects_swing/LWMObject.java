// $Id: LWMObject.java 329 2006-11-21 09:48:54Z wim $
package fi.beans.lwmobjects_swing;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

/**
 * Een 'LightWeight Movable (LWM) Object' 
 * Een LWMObject heeft ofwel een gekleurd vlakje of een plaatje.
 * Naamkeuze.... LWMObject is een LWMComponent die geen LWMContainer is!
 * 
 * @author Paul Bergervoet
 *
 * @version 0, 20 augustus 2000
 */

public class LWMObject extends LWMComponent
{	// constanten
	
	// variables: none extra!
	
/**
 * common part of constructors.
 */
	private void commonConstructorPart(int w, int h)
	{	setSize(w, h);
		isMovable = true;
		
//		addMouseListener(this);
//		addMouseMotionListener(this);

		initListeners();
	}

/**
 * Constructs a transparant LWMObject with the specified dimension.
 *
 * @param w Width (int)
 * @param h Height (int)
 */
	public LWMObject(int w, int h)
	{	setTransparant();
		commonConstructorPart(w, h);
	}
	
/**
 * Constructs a LWMObject with the specified dimension and background color.
 *
 * @param c The background Color
 * @param w Width (int)
 * @param h Height (int)
 */
	public LWMObject(Color c, int w, int h)
	{	setColor(c);
		commonConstructorPart(w, h);
	}
	
/**
 * Constructs a LWMObject with the specified dimension and background image.
 *
 * @param i The background Image
 * @param w Width (int)
 * @param h Height (int)
 */
	public LWMObject(Image i, int w, int h)
	{	setImage(i);
		commonConstructorPart(w, h);
	}

/**
 * Constructs a LWMObject with the specified background image and unknown size.
 * Size will be set by the image itself.
 *
 * @param i The background Image
 */
	public LWMObject(Image i)
	{	setImage(i);
		commonConstructorPart(40, 40);	// just some number until the info arrives...
		int nw = i.getWidth(this);			// get width
		if ( nw != -1 )					// if known, set it, else wait for imageUpdate
		{	width = nw;
			setSize(width, getHeight() );
		}
		int nh = i.getHeight(this);			// same for height
		if ( nh != -1 )
		{	height = nh;
			setSize(getWidth(), height);
		}
	}

/* Change the size of a LWMObject.
 * Not to be used lightly when the object is an Image!
 *
 * @param w Width (int)
 * @param h Height (int)
 */
	public void setSize(int w, int h)
	{	width = w;
		height = h;
		super.setSize(width, height);
	}
	
	/**
	 * @see java.awt.Component#setBounds(int, int, int, int)
	 */	
	public void setBounds(int x, int y, int w, int h)
	{
		width = w;
		height = h;
		super.setBounds(x, y, w, h);
	}
/**
 * Change the size of a LWMObject.
 * Not to be used lightly when the object is an Image!
 *
 * @param d The new dimensions
 */
	public void setSize(Dimension d)
	{	setSize(d.width, d.height);
	}	
		
/**
 * imageUpdate will set the size of this LWMObject, in case that it is an Image.
 * also takes care of repainting when the Image is ready.
 */
	public synchronized boolean imageUpdate(Image p, int flags, int x, int y, int w, int h)
	{	if ( (flags & ImageObserver.WIDTH) != 0 )
		{	width = w;
			setSize(width, getHeight() );
			return true;
		}
		if ( (flags & ImageObserver.HEIGHT) != 0 )
		{	height = h;
			setSize(getWidth(), height);
			return true;
		}
		if ( (flags & ImageObserver.ALLBITS) != 0 )
		{	repaint();
			return false;
		} else
		{	return true;
		}
	}
	
/**
 * Paint this LWMObject. 
 * Just paints the component itself using paintComponent from the superclass.
 *	
 * @param g The graphics context
 */
	public void paint(Graphics g)
	{	paintComponent(g);
		// super.paint(g);			// Belangrijk verschil met LWMContainer!!!!!!!!!!!
	
//g.setColor(Color.black);
//g.drawRect(0,0, getWidth() - 1, getHeight() - 1) ;	
	
	}
	
}
