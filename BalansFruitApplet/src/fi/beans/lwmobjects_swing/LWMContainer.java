package fi.beans.lwmobjects_swing;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

/**
 * A 'LightWeight Movable (LWM) Container' 
 +
 * A LWMContainer contains LWMComponents and is  a LWMComponent itself.
 * LWMContainer inherits from java.awt.Container through (abstract) LWMComponent.
 * Special LWMContainer features like gravity, ordering of components are
 * implemented in this class.
 *
 * @author Paul Bergervoet
 *
 * @version 1, 20 september 2000, first fully functional version.
 */

public class LWMContainer extends LWMComponent
{	
// ----------- Constants ----------

/**
 * The constant specifying that no gravity must be applied to subcomponents 
 */
	public static final int GONOWHERE = 0;		// Directions for gravity

/**
 * The constant specifying that subcomponents must be placed at the bottom/south edge.
 */
	public static final int GOSOUTH = 1;

/**
 * The constant specifying that subcomponents must be placed at the left/west edge.
 */
	public static final int GOWEST = 2;

/**
 * The constant specifying that subcomponents must be placed at the top/north edge.
 */
	public static final int GONORTH = 3;

/**
 * The constant specifying that subcomponents must be placed at the right/east edge.
 */
	public static final int GOEAST = 4;
	
// ----------- Variables ----------
/**
 * Boolean indicating if subcomponents must be moved entirely inside the container.
 */
	protected boolean keepInside;			// keep LWMComponents completely inside Container?

/**
 * The current value 
 */
	protected int gravity;					// zero or one of the constants above.

// ----------- Constructors ----------
/**
 * common part of constructors.
 */
	private void commonConstructorPart(int w, int h)
	{	width = w;
		height = h;
		super.setSize(width, height);
		keepInside = false;
		gravity = GONOWHERE;
		isMovable = true;
		
		//addMouseListener(this);
		//addMouseMotionListener(this);
		
		initListeners();
	}

/**
 * Constructs a LWMContainer with the specified dimension and background color.
 *
 * @param c The background Color
 * @param w Width (int)
 * @param h Height (int)
 */
	public LWMContainer(Color c, int w, int h)
	{	setColor(c);
		commonConstructorPart(w, h);
	}
	
/**
 * Constructs a LWMContainer with the specified dimension and background image.
 *
 * Note: because of addLWMComponent, LWMContainers always need to know their 
 * size at startup, unlike LWMObjects.
 *
 * @param i The background Image
 * @param w Width (int)
 * @param h Height (int)
 */
	public LWMContainer(Image i, int w, int h)
	{	setImage(i);
		commonConstructorPart(w, h);
	}

/**
 * Constructs a transparant LWMContainer with the specified dimension.
 *
 * @param w Width (int)
 * @param h Height (int)
 */
	public LWMContainer(int w, int h)
	{	setTransparant();
		commonConstructorPart(w, h);
	}

// ----------- Set and Get ----------
/**
 * Sets the KeepInside property. 
 * When this is turned on, components dropped on the container will be moved
 * slightly if needed, so they will fit entirely within the borders of the container.
 * <br>
 * Note: normally used at initialisation. When used while running, this method will not
 * affect the Location of the components in the LWMContainer, it will only act on new additions.
 * Use rearrangeComponents() to make the new keepinside work on the current components.
 *
 * @param b true or false for KeepInside
 */
	public void setKeepInside(boolean b)
	{	keepInside = b;
	}

/**
 * Find out if keepInside is on
 *
 * @return boolean, true == on
 */
	public boolean hasKeepInside()
	{	return keepInside;
	}
	
/**
 * Sets the Gravity property.
 * When gravity is on, components will be placed against one of the container's edges.
 * <br>
 * Note: normally used at initialisation. When used while running, this method will not
 * affect the Location of the components in the LWMContainer, it will only act on new additions.
 * Use rearrangeComponents() to make the new gravity work on the current components.
 *
 * @param g One of the five constants GOSOUTH, GOWEST etc.
 */
	public void setGravity(int g)
	{	gravity = g;
	}

/**
 * Gets the setting of the gravity-property
 *
 * @return int, see the constants for meaning of values.
 */
	public int getGravity()
	{	return gravity;
	}
	
/**
 * Change Size of a LWMContainer.
 * Locations of componenten inside the Container may need to be adjusted:
 * <ul>
 * <li>
 * When 'keepInside' is on, components are moved inside when needed.
 * <li>
 * When gravity is GOSOUTH or GOEAST components will be moved against the
 * edges that have moved as a result of the change in size.
 * When gravity is GONORTH or GOWEST the y resp. x are already zero, so there is no
 * need for change.
 * <ul>
 *
 * @param w Width (int)
 * @param h Height (int)
 */
	public void setSize(int w, int h)
	{	int teller;
		int maat;
		Point p;
		LWMComponent c;
		if ( w != width )					// breedte veranderd
		{	for ( teller = 0; teller < getComponentCount(); teller++ )
			{	c = (LWMComponent)getComponent(teller);
				p = c.getLocation();
				if ( gravity == GOEAST )	// verplaats alles met verschil oude en nieuwe breedte
				{	c.setLocation(w-c.getWidth()-borderWidth, p.y);
				} else					// gravity garandeert keepInside in deze richting
				{	if ( keepInside )
					{	maat = Math.min(p.x, w-c.getWidth());
						maat = Math.max(maat, 0);
						c.setLocation(maat, p.y);
					}
				}
			}
		}
		if ( h != height )					// hoogte veranderd
		{	for ( teller = 0; teller < getComponentCount(); teller++ )
			{	c = (LWMComponent)getComponent(teller);
				p = c.getLocation();
				if ( gravity == GOSOUTH )	// verplaats alles met verschil oude en nieuwe hoogte
				{	c.setLocation(p.x, h-c.getHeight()-borderWidth );
				} else
				{	if ( keepInside )
					{	maat = Math.min(p.y, h-c.getHeight());
						maat = Math.max(maat, 0);
						c.setLocation(p.x, maat);
					}
				}
			}
		}
		width = w;
		height = h;
		super.setSize(w, h);				// het ding zelf natuurlijk ook!
	}

/**
 * Change size of a LWMContainer.
 *
 * @param d The new dimensions
 */
	public void setSize(Dimension d)
	{	setSize(d.width, d.height);
	}

// ----------- Adding and removing components ----------
/**
 * Find rank of component with the specified width or height.
 * Gravity orders components by size (horizontale gravity: width, vertical gravity: height).
 *
 * @param horizontal Boolean horizontal/vertical gravity
 * @param size The width/height of the component to be added
 *
 * @return int The rank in the list of components
 */
	private int findRank(boolean horizontal, int size)
	{	int n = getComponentCount();
		int i = 0;
		boolean gadoor = true;
		int csize;
		LWMComponent c;
		while ( gadoor )
		{	if ( i < n )				// er zijn nog componenten
			{	c = (LWMComponent)getComponent(i);
				if ( horizontal )
				{	csize = c.getWidth();
				} else
				{	csize = c.getHeight();
				}
				if ( size <= csize )	// deze >= nieuwe component, dan ervoor
				{	gadoor = false;
				} else
				{	i++;			// go to next component
				}
			} else
			{	gadoor = false;		// geen componenten meer.
				i = -1;			// achteraan
			}
		} // while gadoor
		return i;
	}
	
/**
 * Add a LWMComponent to this Container at the specified Location and rank.
 * (rank in list of Components: 0 is on top, i is index, -1 at the back)
 * <br>
 * If keepInside is on, the Component may be moved slightly, so it will fit completely
 * within (the border of) the Container.
 * <br>
 * If gravity is on (SOUTH, WEST, etc) the Component will be placed against the 
 * specified edge. Rank of Components will be decided by size (smallest on top),
 * it is then useless to specify rank, since it will be overruled.
 *
 * @param c The component to be added
 * @param x The x location
 * @param y The y location
 * @param index The rank in the order of subcomponents of this container
 */
	protected void addLWMComponent(LWMComponent c, int x, int y, int index)
	{	int locx = x;
		int locy = y;
		int locindex = index;
		if ( keepInside )
		{	locx = Math.min(locx, width-c.getWidth()-borderWidth);
			locx = Math.max(locx, borderWidth);
			locy = Math.min(locy, height-c.getHeight()-borderWidth);
			locy = Math.max(locy, borderWidth);
		}
		if ( gravity > 0 )
		{	switch ( gravity )
			{ case GOSOUTH:
				locy = height-c.getHeight()-borderWidth;
				locindex = findRank(false, c.getHeight());
				break;
			  case GOWEST:
				locx = borderWidth;
				locindex = findRank(true, c.getWidth());
				break;
			  case GONORTH:
				locy = borderWidth;
				locindex = findRank(false, c.getHeight());
				break;
			  case GOEAST:
				locx = width-c.getWidth()-borderWidth;
				locindex = findRank(true, c.getWidth());
			}
		}
		c.setLocation(locx, locy);
		c.setBoxes(this, allbox);
		add(c, locindex);
	}

/**
 * Add a component at front.
 *
 * @param c The component to be added
 * @param x The x location
 * @param y The y location
 */ 
	public void addLWMComponent(LWMComponent c, int x, int y)
	{	addLWMComponent(c, x, y, 0);
	}

/**
 * Remove the specified component from this container.
 *
 * @param c The component to be removed
 */
	public void removeLWMComponent(LWMComponent c)
	{	remove(c);
		c.clearBoxes();
	}

/**
 * Rearranges the components in a container, especially after a change of gravity, etc.
 *
 * Note: this method just removes all components and adds them again at their previous
 * locations. This will cause gravity and keepinside to take effect!
 */
	public void rearrangeComponents()
	{	Point p;
		int teller;
		int aantal = getComponentCount();
		LWMComponent[] comps = new LWMComponent[aantal];
		for ( teller=0; teller<aantal; teller++ )
		{	// NB: steeds component met index 0 weghalen. Na remove van de eerste component
			// uit 0..n blijft er een reeks 0..n-1 over!
			comps[teller] = (LWMComponent)( getComponent(0) );
			comps[teller].setPreviousPosition();
			removeLWMComponent(comps[teller]);
		}
		for ( teller=aantal-1; teller>=0; teller-- )
		{	p = comps[teller].getPreviousLocation();
			addLWMComponent(comps[teller], p.x, p.y);
		}
		repaint();
	}

// ----------- Painting ----------
/**
 * Paint this container. paints the component itself and calls java.awt.Container.paint.
 *	
 * @param g The graphics context
 */
	public void paint(Graphics g)
	{	paintComponent(g);
		super.paint(g);			// Belangrijk verschil met LWMObject!!!!!!!!!!!
	}

}
