package fi.beans.lwmobjects_swing;

import java.awt.*;
import java.awt.event.*;

/**
 * LWMRootContainer is an LWMContainer that contains all: the root of the LWMComponent tree.
 * The LWMRootContainer should be added to e Panel that does double buffering.
 * Main functions:
 * - while dragging: An LWMComponent that is being dragged, will leave its previous LWMContainer.
 * During dragging it will be added to the LWMRootContainer.
 * - at drop: The LWMRootContainer will search the Component tree for the destination LWMContainer,
 * that is the LWMContainer at the mouseReleased location. This cannot be done by ordinary 
 * event handling; the mouseReleased event arrives at the dragged Component.
 * 
 * @author Paul Bergervoet
 *
 * @version 1, 1 september 2000
 */

public class LWMRootContainer extends LWMContainer
{	// no additional variables needed

/**
 * Constructs a LWMRootContainer with the specified dimension and background color.
 *
 * @param c The background Color
 * @param w Width (int)
 * @param h Height (int)
 */
	public LWMRootContainer(Color c, int w, int h)
	{	super(c, w, h);
		setMovable(false);
	}
	
/**
 * Constructs a LWMRootContainer with the specified dimension and background image.
 *
 * @param i The background Image
 * @param w Width (int)
 * @param h Height (int)
 */
	public LWMRootContainer(Image i, int w, int h)
	{	super(i, w, h);
		setMovable(false);
	}

/**
 * Constructs a transparant LWMRootContainer with the specified dimension.
 *
 * @param w Width (int)
 * @param h Height (int)
 */
	public LWMRootContainer(int w, int h)
	{	super(w, h);
		setMovable(false);
	}

	public void initRoot()
	{	setBoxes(this, this);
	}

/**
 * Determine the absolute location of this LWMComponent within the LWMRootContainer.
 * Obviously returns (0,0)! This method is here only to stop recursion!
 *
 * @return Point (0,0)
 */
	public Point getAbsLocation()
	{	return new Point(0, 0);
	}

/** Find the LWMContainer at the specified x, y.
 * May return null, if the specified x and y are outside the LWMRootContainer itself!
 * May return itself, when the x,y are inside, but no subContainer is there.
 *
 * @param ix the x-coordinate
 * @param iy the x-coordinate
 *
 * @return the LWMContainer at the specified location (or null)
 */
	LWMContainer getLWMContainerAt(int ix, int iy)
	{	int x = ix;
		int y = iy;
		Point p;
		if ( contains(ix, iy) )						// Point inside RootContainer?
		{	Component dest = this;					// start search at LWMRootContainer itself
			while ( dest != dest.getComponentAt(x, y) ) 	// will search just one level deep!
			{	dest = dest.getComponentAt(x, y);
				p = dest.getLocation();				// Translate coordinates.
				x = x - p.x;
				y = y - p.y;
			}								// Lowest Component in tree found...
			if ( dest instanceof LWMContainer )		// ... if it's a LWMContainer: return it!
			{	return (LWMContainer)dest;		
			} else								// ... if not: return its parent LWMContainer!
			{	return ((LWMComponent)dest).getBox();				
			}
		} else
		{	return null;
		}
	}
}
