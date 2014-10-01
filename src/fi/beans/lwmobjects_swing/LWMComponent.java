package fi.beans.lwmobjects_swing;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.Vector;

import javax.swing.*;

/**
 * The abstract superclass for all 'LightWeight Movable Component's  (LWMComponent).
 * <p>
 * This class extends Container, because the subclass LWMObject needs the Container functionality.
 * The other subclass LWMObject must not use any of the Container methods. Normally, further
 * subclasses will inherit from either LWMObject or LWMObject.
 * <br>
 * (This really is a multiple inheritance problem!)
 * <p>
 * All LWMComponents can be moved by mouse drags, they may move from one LWMContainer to
 * another. 
 * 
 * @author Paul Bergervoet
 *
 * @version 1, 20 september 2000 fully documented
 */

public abstract class LWMComponent extends Container
	implements MouseListener, MouseMotionListener
{	
// ----------- Constants ----------
/**
 * Default width for borders.
 */
	public static int DEFAULTBORDERWIDTH = 2;

/**
 * Default size of the arcs when using drawRoundRect.
 */
	public static int DEFAULTARCSIZE = 6;

/**
 * Default color for borders.
 */
	public static Color DEFAULTBORDERCOLOR = Color.black;
	
// ----------- Variables ----------
/**
 * Indicates if this component shows an image.
 */
	boolean hasImage;

/**
 * The image of this component
 */
	Image myImage;

/**
 * Indicates if this component shows a background color.
 */
	boolean hasColor;

/**
 * The background color of this component
 */
	Color myColor;

/**
 * Indicates if this component shows a border.
 */
	boolean hasBorder = false;

/**
 * The current width of the border.
 */
	int borderWidth = 0;

/**
 * The current size of the arcs when using drawRoundRect.
 */
	int arcSize = 0;

/**
 * The current color for borders.
 */
	Color borderColor = DEFAULTBORDERCOLOR;

/**
 * Width of the component. Included because typing getSize().width is too much trouble.
 */
	int width;

/**
 * Height of the component. Included because typing getSize().height is too much trouble.
 */
	int height;
	
/**
 * Indicates if this component is movable by mouse drags.
 */
	boolean isMovable;

/**
 * Indicates that the component should stay inside the root container
 */
	boolean stayInsideRoot;

/**
 * Link to parent container. Included expicitly so we don't need to cast getParent all the time.
 */
	LWMContainer mybox;

/**
 * Link to the overall container. Needed to allow drags outside the current parent-container.
 */
	LWMRootContainer allbox;

/**
 * Boolean indicating if mouse has been pressed and drag is being done (press: true, release: false).
 * Explicitly needed because cloning will need to clear it (PileObject). Clone is not being dragged,
 * but the cloning operation will copy the field and leave it on. Switch it off explicitly.
 */
	boolean dragging;

/**
 * Coordinates of mouse press on component, needed to determine exact location.
 */
	private Point startpunt;

/**
 * Current permitted containers for this component to be dragged to.
 */
	MovePermissions myPermissions;

/**
 * Remember previous container in case of a drag that is not permitted: go back!
 */
	LWMContainer previousContainer;

/**
 * Remember previous location in case of a drag that is not permitted: go back!
 */
	Point previousLocation;

/**
 * Vector met LWMListeners
 */
	private Vector listeners;

// ------- LWMListeners, to be notified after a move --------------

	protected void initListeners()
	{	listeners = new Vector();
	}

 /**
  * Add a LWMListener to the component's listener-list.
  *
  * @param l NumberListener to be added.
  */
	public void addLWMListener(LWMListener l)
	{	listeners.addElement(l);
	}
	
 /**
  * Remove a LWMListener to the component's listener-list.
  *
  * @param l NumberListener to be removed.
  */
	public void RemoveLWMListener(LWMListener l)
	{	listeners.removeElement(l);				// don't care if it is there (returns boolean)
	}
	
/**
 * Notify listener of move.
 */
	private void tellListeners(LWMContainer from, LWMContainer to)
	{	for ( int i=0; i<listeners.size(); i++ )
		{	( (LWMListener)listeners.elementAt(i) ).componentMoved(this, from, to);
		}
	}
	
// ------- overrides of standard methods involving Size --------------

/**
 * Returns the width of the component.
 */	
	public int getWidth()
	{	return width;
	}
	
/**
 * Returns the height of the component.
 */	
	public int getHeight()
	{	return height;
	}
	
/**
 * Minimum size always is the specified size in the constructor!
 */	
	public Dimension getMinimumSize()
	{	return new Dimension(width, height);
	}
 
/**
 * Preferred size always is the specified size in the constructor!
 */	
	public Dimension getPreferredSize()
	{	return new Dimension(width, height);
	}

// ------- Set and get   --------------

/**
 * Switch movability of a component on/off.
 *
 * @param b Boolean, true=on. 
 */	
	public void setMovable(boolean b)
	{	isMovable = b;
	}
	
/**
 * Find out if a component is movable.
 *
 * @return Boolean, true=movable. 
 */	
	public boolean isMovable()
	{	return isMovable;
	}
	
/**
 * Set if a component should stay inside the root container.
 *
 * @param b Boolean, true=stay inside root. 
 */	
	public void setStayInsideRoot(boolean b)
	{	stayInsideRoot = b;
	}
	
/**
 * Find out if a component should stay inside the root container.
 *
 * @return Boolean, true=stay inside root. 
 */	
	public boolean getStayInsideRoot()
	{	return stayInsideRoot;
	}
	
/**
 * Set the move permissions of a component.
 *
 * @param mp a MovePermissions object that lists the permitted containers. 
 */	
	public void setPermissions(MovePermissions mp)
	{	myPermissions = mp;
	}

/**
 * Switches border on/off.
 * Borderproperties are set to the default values when border is switched on.
 * They are cleared when border is switched off.
 *
 * @param b Boolean, true=on. 
 */
	public void setBorder(boolean b)
	{	hasBorder = b;
		if ( b )
		{	borderWidth = DEFAULTBORDERWIDTH;
			arcSize = DEFAULTARCSIZE;
			borderColor = DEFAULTBORDERCOLOR;
		} else
		{	borderWidth = 0;
			arcSize = 0;
		}
		repaint();
	}
	
/**
 * Switches border on and sets borderproperties with the specified values.
 *
 * @param w Width of the border in pixels
 * @param a The arc size, for rounded rectangles
 * @param c The border color
 */
	public void setBorder(int w, int a, Color c)
	{	hasBorder = true;
		borderWidth = w;
		arcSize = a;
		borderColor = c;
		repaint();
	}

/**
 * Find out if a component has a border.
 *
 * @return Boolean, true=has a border. 
 */	
	public boolean hasBorder()
	{	return hasBorder;
	}

/**
 * Gets the size of the arc, used in drawing the border.
 *
 * @return int
 */
	public int getArcSize()
	{	return arcSize;
	}
	
/**
 * Gets the width of the border.
 *
 * @return int
 */
	public int getBorderWidth()
	{	return borderWidth;
	}
	
/**
 * Gets the color of the border.
 *
 * @return int
 */
	public Color getBorderColor()
	{	return borderColor;
	}

/**
 * Returns the background color. May return null if there is none!
 */
	public Color getColor()
	{	return myColor;
	}
	
/**
 * Sets the background color. 
 * Can also be used to switch from an image to a colored rectangle.
 * <br>
 * Note: the original image is not wiped. You can revert to the previous looks
 * using getImage and setImage.
 */
	public void setColor(Color c)
	{	hasImage = false;
		hasColor = true;
		myColor = c;
		repaint();
	}

/**
 * Find out if a component has a background color.
 *
 * @return Boolean, true=has a background color. 
 */	
	public boolean hasColor()
	{	return hasColor;
	}

/**
 * Returns the background image. May return null if there is none!
 */
	public Image getImage()
	{	return myImage;
	}

/**
 * Sets the background image. 
 * Can also be used to switch from  to a colored rectangle to an image.
 * <br>
 * Note: the original color is not wiped. You can revert to the previous looks
 * using getColor and setColor.
 */
	public void setImage(Image i)
	{	hasColor = false;
		hasImage = true;
		myImage = i;
		repaint();
	}
	
/**
 * Find out if a component has an image.
 *
 * @return Boolean, true=has an image. 
 */	
	public boolean hasImage()
	{	return hasImage;
	}

/**
 * Makes the component transparant. 
 * <br>
 * Note: the original color/image is not wiped. You can revert to the previous looks
 * using getColor/setColor or getImage/setImage.
 */
	public void setTransparant()
	{	hasColor = false;
		hasImage = false;
		repaint();
	}
	
// ------- Painting components:   --------------

/**
 * Paints just this component.
 * <br>
 * Technical note: No override of paint(g). It will stay equal to java.awt.Container.paint(g).
 * LWMContainer will override paint by calling paintComponent and super.paint, which will
 * take care of painting the subcomponents. LWMObject will override paint by calling
 * paintComponent only!
 */
	public void paintComponent(Graphics g)
	{	if ( hasImage )
		{	g.drawImage(myImage, 0, 0, this);
		} else if ( hasColor )					// It's image OR background
		{	g.setColor(myColor);
			g.fillRoundRect(0, 0, width, height, arcSize, arcSize);
		}
		if ( hasBorder )						// Border may occur with images AND colors
		{	g.setColor(borderColor);
			for ( int i=0; i<borderWidth; i++ )
			{	g.drawRoundRect(i, i, width-(2*i+1), height-(2*i+1), arcSize, arcSize);
			}
		}
	}

/**
 * The override of update(Graphics) just calles paint.
 */
	public void update(Graphics g)
	{	paint(g);
	}
	
/**
 * imageUpdate makes sure that LWMComponents with an image are painted correctly.
 */
	public synchronized boolean imageUpdate(Image p, int flags, int x, int y, int w, int h)
	{	if ( (flags & ImageObserver.ALLBITS) != 0 )			// non-animated!!!!
		{	repaint();
			return false;
		} else
		{	return true;
		}
	}

// ------- Moving components: essential methods  --------------

/**
 * Determine the absolute location of this LWMComponent within the LWMRootContainer.
 * Subclasses should not override!
 *
 * @return Point: the location.
 */
	public Point getAbsLocation()
	{	Point pl = mybox.getAbsLocation();		// parent Location, watch out for null!! xxx
		Point ml = getLocation();
		return new Point(pl.x+ml.x, pl.y+ml.y);
	}

/**
 * Remembers current position (Container and Location). 
 * Used before starting a move. You can get the old position with the methods
 * getPreviousContainer and getPreviousLocation.
 */
	protected void setPreviousPosition()
	{	previousContainer = mybox;
		previousLocation = getLocation();
	}

/**
 * Gets the previous container of a LWMComponent.
 * The old position (Container and Location) is set when initiating a drag.
 *
 * @return LWMContainer
 */
	protected LWMContainer getPreviousContainer()
	{	return previousContainer;
	}

/**
 * Gets the previous location of a LWMComponent.
 * The old position (Container and Location) is set when initiating a drag.
 *
 * @return Point Location relative to previous container.
 */
	protected Point getPreviousLocation()
	{	return previousLocation;
	}
	
/**
 * Gets the LWMContainer that is the parent of this LWMComponent
 *
 * @return LWMContainer
 */
	public LWMContainer getBox()
	{	return mybox;
	}

/**
 * Gets the LWMRootContainer that is the root of the LWMComponent tree
 *
 * @return LWMRootContainer
 */
	public LWMRootContainer getAllbox()
	{	return allbox;
	}
	
/**
 * Sets both the LWMContainer and the LWMRootContainer a component is in.
 * <br>
 * Called by LWMContainer.addLWMComponent(...). Don't mess with it!
 *
 * @param c The component's container.
 * @param a The program's rootcontainer (fixed in any program)
 */
	void setBoxes(LWMContainer c, LWMRootContainer a)
	{	mybox = c;
		allbox = a;
	}

/**
 * Clears the Container of this component.
 * <br>
 * Called by LWMContainer.removeLWMComponent(...). Don't mess with it!
 * Note: this method doesn't actually clear the reference to the rootcontainer.
 * This reference is always needed.
 */
	void clearBoxes()
	{	mybox = null;
		// allbox = null;				// niet doen! nu nodig bij terugzetten....
	}
	
// ------- Moving components: methods to override  --------------

/**
 * pickupLWMComponent does the actual 'pick up' of the LWMComponent. 
 * <br>
 * The default implementation removes the LWMComponent from its container
 * and adds it to the LWMRootContainer at the specifies absolute location ap.
 * <p>
 * Subclasses may override this method to implement special pick up operations, i.e. PileObject.
 * The absolute location is passed to the method as an argument, all other necessary
 * data are provided through the variables mybox, allbox, getLocation(), etc.
 * The variables previousContainer and previousLocation have already been set.
 *
 * @param ap The absolute location of the LWMComponent in the LWMRootContainer.
 */
	protected void pickupLWMComponent(Point ap)
	{	mybox.removeLWMComponent(this);
		allbox.addLWMComponent(this, ap.x, ap.y, 0);
	}
	
/**
 * dropLWMComponent does the actual 'drop' of the LWMComponent into a LWMContainer. 
 * <br>
 * The default implementation will calculate the relative location of the LWMComponent 
 * from the component's absolute location ap and the absolute location of the 
 * destination LWMContainer and add the component.
 * <p>
 * Subclasses may override this method to implement special drop operations.
 * The absolute location and destination are passed to the method as an argument, 
 * all other necessary data are provided through the variables mybox, allbox, getLocation(), etc.
 * The variables previousContainer and previousLocation have already been set.
 *
 * @param dest The destination LWMContainer.
 * @param ap The absolute location of the LWMComponent in the LWMRootContainer.
 */
	protected void dropLWMComponent(LWMContainer dest, Point ap)
	{	// The Location of the LWMComponent in the destination Container equals
 		// the current (absolute) Location minus the absolute Location of dest.
 		Point dp = dest.getAbsLocation();
 		dest.addLWMComponent(this, ap.x-dp.x, ap.y-dp.y, 0);
 	}

// ------- Event handling: MouseListener --------------

/**
 * action on mouse pressed: pick up component for moving.
 * <br>
 * The original position is remembered so we can put the component back in case the move is not permitted.
 * The actual 'pick up' is done by pickupLWMComponent(Point 'absolute location'), this method can be
 * overridden.
 *
 * @see LWMComponent#pickupLWMComponent(Point ap)
 */
	public void mousePressed(MouseEvent e)
	{	if ( isMovable )
		{	// remember old location
			setPreviousPosition();
			// initiate move
			dragging = true;
			startpunt = new Point(e.getX(), e.getY());
			Point ap = getAbsLocation();
			pickupLWMComponent(ap);
		}
	}
	
/**
 * action on mouse released: drop component into new position.
 * <br>
 * Drop a LWMComponent into a new position (LWMContainer and Location) or put the component back 
 * in the old position in case the move is not permitted.
 * The actual 'drop' is done by dropLWMComponent(LWMContainer, Point 'absolute location'), 
 * this method can be overridden (i.e. PileObject)
 *
 * @see LWMComponent#dropLWMComponent(LWMContainer dest, Point ap)
 */
	public void mouseReleased(MouseEvent e) 
 	{	if ( dragging )			// check nodig? niet de meest elegante manier.....
		{	dragging = false;
			startpunt = null;
			// 1. Vind absolute positie
 			Point ap = getAbsLocation();
 			// 2. Verwijder Component uit boom (anders vindt Container.getComponentAt
 			// 	de Component zelf!
			mybox.removeLWMComponent(this);
			// 3. Zoek de nieuwe LWMContainer
 			LWMContainer dest = allbox.getLWMContainerAt(ap.x+e.getX(), ap.y+e.getY() );
 			// 4. Check permission
 			if ( dest != null && ( myPermissions == null || myPermissions.isPermitted(dest) ) )
 			{	// 5a. dropLWMComponent will by default calculate the Component's location
 				// 	and add it to dest.
 				dropLWMComponent(dest, ap);
 				tellListeners(previousContainer, dest);
 			} else
 			{	// 5b. Terug, tis niet goed!
 				//	NB: ook index in componentlist terugzetten?
 				Point p1 = previousContainer.getAbsLocation();
 				Point p2 = new Point(p1.x + previousLocation.x, p1.y + previousLocation.y);
 				dropLWMComponent(previousContainer, p2);
 				// Note: we must use dropLWMComponent(..) to ensure that copies of a pile are
 				// returned to their pile, all specials are done by this method....
 			}
 			allbox.repaint();
 		}
 	}  

/**
 * Does nothing.
 */
	public void mouseEntered(MouseEvent e)
	{}
	
/**
 * Does nothing.
 */
	public void mouseExited(MouseEvent e)
	{}
	
/**
 * Does nothing.
 */
	public void mouseClicked(MouseEvent e)
	{}
	
// ------- Event handling: MouseMotionListener --------------
	
/**
 * Does nothing.
 */
	public void mouseMoved(MouseEvent e)
	{}

/**
 * Drag the LWMComponent around. Just changes the components location.
 */
	public void mouseDragged(MouseEvent e) 
 	{	if ( dragging )
		{	int dx = e.getX() - startpunt.x;
			int dy = e.getY() - startpunt.y;
			int x = getLocation().x + dx;		// NB drag is steeds relatief tov de huidige positie!
			int y = getLocation().y + dy;
			if (stayInsideRoot)
			{	if ( x < 0 )
					x = 0;
				else if ( x + width > allbox.getBounds().width )
					x = allbox.getBounds().width - width;

				if ( y < 0 )
					y = 0;
				else if ( y + height > allbox.getBounds().height )
					y = allbox.getBounds().height - height;
			}

			setLocation(x, y);
			repaint();
		}
 	}  
	
}
