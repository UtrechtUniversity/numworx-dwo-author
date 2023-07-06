package fi.doorziendwo;

import java.awt.*;
import java.awt.event.*;

//class representing a horizontal slider
public class Slider extends Component //implements Observer
{   // sizes, hard coded
    static final int vertSize = 20;
    static final int horSize = 120;
    static final int buttonWidth = 7;
    static final int offSet = 10;
    // the parameter being adjusted
    //private Parameter sliderValue;
    //double sliderValue;
    double minValue, maxValue;
    // slider position, a value from 0 to 1
    private double currentPosition;
    // slider colors
	private Color enabledColor = Color.red;
	private Color disabledColor = Color.gray;
	private Color sliderColor = enabledColor;
	// flag for being enabled
	// NOT private, must be accessable from inner class MLMML
	boolean enabled = true;
	// the owner
	DrawingPanel owner;
    // constructor
	public Slider(DrawingPanel o, double min, double max)
	{	owner = o;
	    setSize(horSize, vertSize);
	    minValue = min;
	    maxValue = max;
		MLMML listener = new MLMML();
		addMouseListener(listener);
		addMouseMotionListener(listener);
		
		setPosition(owner.sliderValue);
	}
/*	
    // for Observer
    public void update(Observable o, Object arg)
    {   double value = ((Parameter) o).getCurrentValue();
        setPosition(value);
    }
*/    

// nog tekst links en rechts?

    // paint method
	public void paintSlider(Graphics g)
	{	g.setColor(Color.lightGray);
	    // outline
		g.drawRect(0, 0, horSize - 1, vertSize - 1);
	    
	    // draw rectangle
        g.setColor(Color.black);
//		g.drawRect(buttonWidth / 2, vertSize / 4,
//		           getSize().width - buttonWidth - 1, vertSize / 2);
		g.drawRect(offSet, vertSize / 4,
		           getSize().width - 2 * offSet - 1, vertSize / 2);
		           
        // draw button
		g.setColor(sliderColor);
/*		
		// NOTE: slider extends from
		// (buttonWidth / 2) to getSize().width - (buttonWidth / 2) - 1
		// thus has length getSize().width - buttonWidth - 1
		// this corresponds to currentPosition 0.0 through 1.0
		g.fillOval((int) Math.round(
		                currentPosition * (getSize().width - buttonWidth - 1)
		                 ),
		           0, buttonWidth, getSize().height);
*/		           
		// NOTE: slider extends from
		// offSet to getSize().width - offSet - 1
		// thus has length getSize().width - 2 * offSet - 1
		// this corresponds to currentPosition 0.0 through 1.0
		g.fillOval((int) Math.round(offSet - (buttonWidth / 2) +
		                currentPosition * (getSize().width - 2 * offSet - 1)
		                 ),
		           0, buttonWidth, getSize().height);
		           
	}
    // avoid flickering
	//public void update(Graphics g)
	//{   paint(g);
	//}
	// draw offscreen
	public void paint(Graphics g)
	{   //Image offscreen = createImage(getSize().width, getSize().height);
	    //Graphics og = offscreen.getGraphics();
	    g.setClip(0, 0, getSize().width, getSize().height);
	    paintSlider(g);
	    //g.drawImage(offscreen, 0, 0, null);
	    //og.dispose();
	}
	// change parameter value, prevent button from leaving
	// rectangle
    public void setValue(int mousePosition)
    {   
//        currentPosition =
//                ((double) (mousePosition - (buttonWidth / 2))) /
//	            (getSize().width - buttonWidth);
        currentPosition =
                ((double) (mousePosition - offSet)) /
	            (getSize().width - 2 * offSet);
	            
	    if (currentPosition > 1.0d)
	        currentPosition = 1.0d;
	    else if (currentPosition < 0.0d)
	        currentPosition = 0.0d;
	    owner.processSlider(minValue +
	        currentPosition * (maxValue - minValue));
        repaint();
    }
    // find button position
	public void setPosition(double val)
	{	currentPosition = (owner.sliderValue - minValue) /
	                      (maxValue - minValue);
		repaint();
	}
	public void setEnabled(boolean e)
	{	if (e)
		{	sliderColor = enabledColor;
		    enabled = true;
		}
		else
		{	sliderColor = disabledColor;
		    enabled = false;
		}
		repaint();
	}
	// inner class for mouse events
	class MLMML extends MouseAdapter
	            implements MouseMotionListener
    {   public void mousePressed(MouseEvent e)
        {   if (enabled)
            {   requestFocus();
                int xPos = e.getX();
                setValue(xPos);
            }
        }
        public void mouseDragged(MouseEvent e)
        {   if (enabled)
            {   int xPos = e.getX();
                int yPos = e.getY();
                // limit dragg events to slider
                if (contains(xPos, yPos))
                    setValue(xPos);
            }
        }
        public void mouseMoved(MouseEvent e) {}
    }
}
