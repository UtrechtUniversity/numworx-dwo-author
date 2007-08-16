package logotekenap;

import java.awt.*;

/**
 * class Slider: draws bar & slider
 */

class Slider extends Canvas
 {	double huidigePos;					// position, value from 0 to 1
	private Color enabledSliderColor = Color.red;
	private Color disabledSliderColor = Color.gray;
	Color sliderColor = enabledSliderColor;
	Dimension dd;

	public Slider(double initp)				// initial position
	{	setSize(100,20);				// for now fixed size (not needed for functionality)
		dd = getSize();
		huidigePos = initp;
	}

	public void paint(Graphics g)
	{	dd = getSize();
		g.drawRect(1, 7, dd.width-2, 6);		// bar
		g.setColor(sliderColor);
		g.fillOval((int)Math.round( huidigePos*(dd.width-7) ), 1, 7, 20);		
									// -7 because of width oval (=7)
	}
	
	protected double getPos( int mousepos )		// working width of bar for scaling MouseEvents
	{	return ((double)(mousepos-3)) / (dd.width-7);
	}

	protected void setSlide(double p)	// set position to p, 0<=p<=1
	{	huidigePos = p;
		repaint();
	}

	public void setEnabled(boolean ena)
	{	if ( ena )						// Color only, NumberSlider handles events
		{	sliderColor = enabledSliderColor;
		} else
		{	sliderColor = disabledSliderColor;
		}
		repaint();						// eigenlijk onnodig
	}

	protected void setDisabledSliderColor(Color c)
	{	disabledSliderColor = c;				// call to repaint() in NumberSlider
	}

	protected void setEnabledSliderColor(Color c)
	{	enabledSliderColor = c;
	}

 }	// end class Slider
