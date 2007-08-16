package logotekenap;

import java.awt.*;

/**
  * class ArrowCanvas
  * Helper klasse voor InvoerVariabele, toont plaatje met pijlen.
  *
  * InvoerVariabele is zelf de MouseListener voor ArrowCanvas
  */

class ArrowCanvas extends Canvas
 {	private Color disabledArrowColor = Color.gray;
	private Color enabledArrowColor = Color.red;
	private Color activeArrowColor = Color.green;
	private Dimension dd;
	
	private Polygon up;
	private boolean upActive = false;
	private Polygon down;
	private boolean downActive = false;
	private boolean enabled = true;

 /**
  * Constructor for ArrowCanvas
  * Creates Canvas with arrows drawn on it.
  */
	public ArrowCanvas()						// initial position
	{	dd = new Dimension(19, 23);			// fixed size.... ok?
		setSize(dd);	
		// triangles for up and down arrows
		up = new Polygon();
		up.addPoint(10, 2);
		up.addPoint(3, 10);
		up.addPoint(17, 10);
		down = new Polygon();
		down.addPoint(3, 13);
		down.addPoint(17, 13);
		down.addPoint(10, 21);
	}

// painting the arrows

	private void paintPolygon(Graphics g, Polygon p, boolean active)
	{	if ( enabled )
		{	if ( active )
			{	g.setColor(activeArrowColor);
			} else
			{	g.setColor(enabledArrowColor);
			}
		} else
		{	g.setColor(disabledArrowColor);
		}
		g.drawPolygon(p);				// draw and fill, will they look the same
		g.fillPolygon(p);				// using fill only, the up-arrow will be smaller!
	}
			
	public void paint(Graphics g)
	{	g.setColor(Color.black);
		g.drawRect(0, 0, dd.width-1, dd.height-1);
		g.setColor(Color.white);
		g.fillRect(1, 1, dd.width-2, dd.height-2);		// background
		paintPolygon(g, up, upActive);
		paintPolygon(g, down, downActive);
	}

// Set and get by NumberArrow

	protected int getArrow(int clicky)			// threshold down arrow
	{	int dir = 0;						// no direction (yet)
		if ( clicky > 12 )
		{	downActive = true;
			dir = -1;
			repaint();
		} else if ( clicky < 11 )
		{	upActive = true;
			dir = 1;
			repaint();
		}
		return dir;
	}
	
	protected void setInactive()
	{	upActive = false;
		downActive = false;
		repaint();
	}

/**
  * Enable or disable the NumberArrow depending on the value of b.
  * An enabled NumberArrow can respond to user input and generates events.
  * NumberArrows are enabled initially by default.
  *
  * @param b If true this NumberArrow is enabled, otherwise the arrow is disabled.
  */
	public void setEnabled(boolean b)
	{	enabled = b;		repaint();
	}
	
 }	// class ArrowCanvas
 
