package fi.statistiek.boxplot;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;
import javax.swing.ToolTipManager;

import fi.statistiek.ColorGenerator;
import fi.statistiek.Statistiek;

/**
 * Draws a single dotplot
 * 
 * @author Manu Drijvers, Sylvia van Borkulo
 * 
 */
public class SingleBoxplotView extends JPanel implements MouseMotionListener
{
	private boolean drawable;

	private double minValue; // the minimum value for the data of this boxplot
	private double lowerQuartile;
	private double median;
	private double upperQuartile;
	private double maxValue;

	private double dataMinValue; // the minimum value in all the data
	private double dataMaxValue; // the maximum value in all the data
	
	private double firstMarker;
	private int step;
	private double max;

	private boolean verticalBoxplots;
	
	private boolean highlightMinValue = false;
	private boolean highlightLowerQuartile = false;
	private boolean highlightMedian = false;
	private boolean highlightUpperQuartile = false;
	private boolean highlightMaxValue = false;

	public static double WIDTH_FILL_FRACTION = 0.8;
	public static double HEIGHT_FILL_FRACTION = 0.8;
	public static final Color BOX_COLOR = ColorGenerator.DEFAULT_VIEW_ELEMENT_COLOR;
	public static final int MAX_BOX_HEIGHT = 40;

	public SingleBoxplotView(Double minValue, Double lowerQuartile,
		Double median, Double upperQuartile, Double maxValue,
		Double dataMinValue, Double dataMaxValue, boolean verticalBoxplots)
	{
		if (minValue == null || lowerQuartile == null || median == null
			|| upperQuartile == null || maxValue == null
			|| dataMinValue == null || dataMaxValue == null)
		{
			drawable = false;
			//System.out.println("SingleBoxplotView(): Niet drawable!");
			return;
		}
		else
		{
			drawable = true;
		}
		this.minValue = minValue;
		this.lowerQuartile = lowerQuartile;
		this.median = median;
		this.upperQuartile = upperQuartile;
		this.maxValue = maxValue;

		this.dataMinValue = dataMinValue;
		this.dataMaxValue = dataMaxValue;

		this.verticalBoxplots = verticalBoxplots;
		this.determineScale();
		
		this.addMouseMotionListener(this);
	}

	/**
	 * Determines the y-coordinate on the screen in this component of value d
	 * 
	 * @return the y-coordinate on the screen in this component of value d
	 */
	private int valueToScreenLocation(double d)
	{
		double a = (this.dataMaxValue - d)
			/ (this.dataMaxValue - this.dataMinValue);
		double y = (a * HEIGHT_FILL_FRACTION)
			* (this.verticalBoxplots ? super.getHeight() : super.getWidth());
		y += (1.0 - HEIGHT_FILL_FRACTION) * 0.5
			* (this.verticalBoxplots ? super.getHeight() : super.getWidth());

		if (!this.verticalBoxplots)
		{
			y = super.getWidth() - y;
		}
		return (int) Math.round(y);
	}

	/**
	 * Draws a dotted vertical line
	 * 
	 * @param x
	 * @param y1
	 * @param y2
	 * @param g
	 */
	private void drawDottedVerticalLine(int x, int y1, int y2, Graphics g)
	{
		for (int i = y1; i < y2; i += 4)
		{
			if ((i + 1) < y2)
			{
				g.drawLine(x, i, x, i + 2);
			}
			else
			{
				g.drawLine(x, i, x, i + 1);
			}
		}
	}

	/**
	 * Draws a dotted horizontal line
	 * 
	 * @param x1
	 * @param x2
	 * @param y
	 * @param g
	 */
	private void drawDottedHorizontalLine(int x1, int x2, int y, Graphics g)
	{
		for (int i = x1; i < x2; i += 4)
		{
			if ((i + 1) < x2)
			{
				g.drawLine(i, y, i + 2, y);
			}
			else
			{
				g.drawLine(i, y, i + 1, y);
			}
		}
	}

	public void paintComponent(Graphics g)
	{
		//System.out.println("SingleBoxplotView.paintComponent()");

		if (!this.drawable)
		{
			return;
		}

		// Teken het hele 'assenstelsel-veld'
		g.setColor(Color.WHITE);
		// test syl: zie wat het hele veld is;
		// bij meerdere boxplots worden meerdere singleboxplotviews naast elkaar getoond
//		g.setColor(ColorGenerator.getColor());
		g.fillRect(0, 0, getWidth(), getHeight());
		// g.clearRect(0, 0, super.getWidth(), super.getHeight());

		g.setColor(Color.BLACK);

		int locationMinValue = this.valueToScreenLocation(this.minValue);
		int locationLowerQuartile = this
			.valueToScreenLocation(this.lowerQuartile);
		int locationMedian = this.valueToScreenLocation(this.median);
		int locationUpperQuartile = this
			.valueToScreenLocation(this.upperQuartile);
		int locationMaxValue = this.valueToScreenLocation(this.maxValue);

		if (this.verticalBoxplots)
		{
			double p = this.firstMarker;
			while (p < this.max)
			{
				int x = this.valueToScreenLocation(p);
				// Zet hulplijnkleur
				g.setColor(new Color(220, 220, 220));
				// Teken hulplijn
				g.drawLine(0, x, super.getHeight(), x);

				p += step;
			}
			// Zet kleur weer terug op zwart
			g.setColor(Color.BLACK);

			int x = (int) Math.round((1.0 - WIDTH_FILL_FRACTION) * 0.5
				* super.getWidth());
			int width = Math.min(SingleBoxplotView.MAX_BOX_HEIGHT,
				(int) Math.round(WIDTH_FILL_FRACTION * super.getWidth()));

			// draw min value
			if (highlightMinValue)
			{
				Graphics2D g2 = (Graphics2D) g;
		        g2.setStroke(new BasicStroke(3));
		        g2.drawLine(x, locationMinValue, x + width, locationMinValue);
		        g2.setStroke(new BasicStroke(1));
			}
			else
			{
				g.drawLine(x, locationMinValue, x + width, locationMinValue);
			}

			// draw dotted line from min value to lower quartile
			this.drawDottedVerticalLine(x + width / 2, locationLowerQuartile,
				locationMinValue, g);

			// draw median and quartiles box around it
			// test syl: hoe krijg ik per split een andere kleur?
			g.setColor(BOX_COLOR);
			g.fillRect(x, locationUpperQuartile, width, locationLowerQuartile 
				- locationUpperQuartile);
			g.setColor(Color.BLACK);
			
			// draw lower quartile
			if (highlightLowerQuartile)
			{
				Graphics2D g2 = (Graphics2D) g;
		        g2.setStroke(new BasicStroke(3));
		        g2.drawLine(x, locationLowerQuartile, x + width,
					locationLowerQuartile);
		        g2.setStroke(new BasicStroke(1));
			}
			else
			{
				g.drawLine(x, locationLowerQuartile, x + width,
					locationLowerQuartile);
			}

			// draw median
			if (highlightMedian)
			{
				Graphics2D g2 = (Graphics2D) g;
		        g2.setStroke(new BasicStroke(3));
		        g2.drawLine(x, locationMedian, x + width, locationMedian);
		        g2.setStroke(new BasicStroke(1));
			}
			else
			{
				g.drawLine(x, locationMedian, x + width, locationMedian);
			}
			
			// draw upper quartile
			if (highlightUpperQuartile)
			{
				Graphics2D g2 = (Graphics2D) g;
		        g2.setStroke(new BasicStroke(3));
				g2.drawLine(x, locationUpperQuartile, x + width,
					locationUpperQuartile);
		        g2.setStroke(new BasicStroke(1));
			}
			else
			{
				g.drawLine(x, locationUpperQuartile, x + width,
					locationUpperQuartile);
			}
			
			g.drawLine(x, locationLowerQuartile, x, locationUpperQuartile);
			g.drawLine(x + width, locationLowerQuartile, x + width,
				locationUpperQuartile);
			// draw dotted line from upper quartile to max value
			this.drawDottedVerticalLine(x + width / 2, locationMaxValue,
				locationUpperQuartile, g);

			// draw max value
			if (highlightMaxValue)
			{
				Graphics2D g2 = (Graphics2D) g;
		        g2.setStroke(new BasicStroke(3));
				g2.drawLine(x, locationMaxValue, x + width, locationMaxValue);
		        g2.setStroke(new BasicStroke(1));
			}
			else
			{
				g.drawLine(x, locationMaxValue, x + width, locationMaxValue);
			}
			
		} // vertical boxplots
		else 
		{
			// horizontal boxplots
			
			double p = this.firstMarker;
			while (p < this.max)
			{
				int x = this.valueToScreenLocation(p);
				// Zet hulplijnkleur
				g.setColor(new Color(220, 220, 220));
				// Teken hulplijn
				g.drawLine(x, 0, x, super.getHeight());

				p += step;
			}
			// Zet kleur weer terug op zwart
			g.setColor(Color.BLACK);

			int y = (int) Math.round((WIDTH_FILL_FRACTION) * super.getHeight());
			int height = Math.min(SingleBoxplotView.MAX_BOX_HEIGHT,
				(int) Math.round(WIDTH_FILL_FRACTION * super.getHeight()));

			// draw min value
			if (highlightMinValue)
			{
				Graphics2D g2 = (Graphics2D) g;
		        g2.setStroke(new BasicStroke(3));
				g2.drawLine(locationMinValue, y, locationMinValue, y - height);
		        g2.setStroke(new BasicStroke(1));
			}
			else
			{
				g.drawLine(locationMinValue, y, locationMinValue, y - height);
			}

			// draw dotted line from min value to lower quartile
			this.drawDottedHorizontalLine(locationMinValue,
				locationLowerQuartile, y - height / 2, g);

			// draw median and quartiles box around it
			g.setColor(BOX_COLOR);
			g.fillRect(locationLowerQuartile, y - height, locationUpperQuartile
				- locationLowerQuartile, height);
			g.setColor(Color.BLACK);
			g.drawLine(locationLowerQuartile, y, locationUpperQuartile, y);
			g.drawLine(locationLowerQuartile, y - height,
				locationUpperQuartile, y - height);
			
			// draw median
			if (highlightMedian)
			{
				Graphics2D g2 = (Graphics2D) g;
		        g2.setStroke(new BasicStroke(3));
				g2.drawLine(locationMedian, y, locationMedian, y - height);
		        g2.setStroke(new BasicStroke(1));
			}
			else
			{
				g.drawLine(locationMedian, y, locationMedian, y - height);
			}
			
			// draw lower quartile
			if (highlightLowerQuartile)
			{
				Graphics2D g2 = (Graphics2D) g;
		        g2.setStroke(new BasicStroke(3));
				g2.drawLine(locationLowerQuartile, y, locationLowerQuartile, y
					- height);
		        g2.setStroke(new BasicStroke(1));
			}
			else
			{
				g.drawLine(locationLowerQuartile, y, locationLowerQuartile, y
					- height);
			}
			
			// draw upper quartile
			if (highlightUpperQuartile)
			{
				Graphics2D g2 = (Graphics2D) g;
		        g2.setStroke(new BasicStroke(3));
				g2.drawLine(locationUpperQuartile, y, locationUpperQuartile, y
					- height);
		        g2.setStroke(new BasicStroke(1));
			}
			else
			{
				g.drawLine(locationUpperQuartile, y, locationUpperQuartile, y
					- height);
			}

			// draw dotted line from upper quartile to max value
			this.drawDottedHorizontalLine(locationUpperQuartile,
				locationMaxValue, y - height / 2, g);

			// draw max value
			if (highlightMaxValue)
			{
				Graphics2D g2 = (Graphics2D) g;
		        g2.setStroke(new BasicStroke(3));
				g2.drawLine(locationMaxValue, y, locationMaxValue, y - height);
		        g2.setStroke(new BasicStroke(1));
			}
			else
			{
				g.drawLine(locationMaxValue, y, locationMaxValue, y - height);
			}
		}
	}

	/**
	 * Determine an appropriate scale
	 */
	private void determineScale()
	{
		int base = 1;
		int exp = (int) Math.log10(this.dataMaxValue - this.dataMinValue) - 1;
		this.step = (int) (base * Math.pow(10, exp));
		while ((this.dataMaxValue - this.dataMinValue) / this.step > 8)
		{
			switch (base)
			{
				case 1:
					base = 2;
					break;
				case 2:
					base = 5;
					break;
				case 5:
					base = 1;
					exp++;
					break;
			}
			this.step = (int) (base * Math.pow(10, exp));
		}

		double min = this.dataMinValue
			- (1 - SingleBoxplotView.HEIGHT_FILL_FRACTION) * 0.5
			* (this.dataMaxValue - this.dataMinValue);
		this.max = this.dataMaxValue
			+ (1 - SingleBoxplotView.HEIGHT_FILL_FRACTION) * 0.5
			* (this.dataMaxValue - this.dataMinValue);
		this.firstMarker = Math.ceil(min / this.step) * this.step;

		// Math.ceil can give -0.0, this step turns that into 0.0
		if (firstMarker == 0)
		{
			firstMarker = 0.0;
		}
	}

	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("minValue: \t" + this.minValue + "\n");
		sb.append("lowerQuartile: \t" + this.lowerQuartile + "\n");
		sb.append("median: \t" + this.median + "\n");
		sb.append("upperQuartile: \t" + this.upperQuartile + "\n");
		sb.append("maxValue: \t" + this.maxValue + "\n");
		sb.append("Data min: \t" + this.dataMinValue + "\n");
		sb.append("Data max: \t" + this.dataMaxValue + "\n");
		return sb.toString();
	}

	@Override
	public void mouseDragged(MouseEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent me)
	{
		int locationMinValue = this.valueToScreenLocation(this.minValue);
		int locationLowerQuartile = this
			.valueToScreenLocation(this.lowerQuartile);
		int locationMedian = this.valueToScreenLocation(this.median);
		int locationUpperQuartile = this
			.valueToScreenLocation(this.upperQuartile);
		int locationMaxValue = this.valueToScreenLocation(this.maxValue);
		
		// breedte van de boxplot
		int widthBoxplot = Math.min(SingleBoxplotView.MAX_BOX_HEIGHT,
			(int) Math.round(WIDTH_FILL_FRACTION * super.getHeight()));


		Point p = me.getPoint();
//		System.out.println("SingleBoxplotView.mouseMoved(): (" + p.x 
//			+ ", " + p.y + ")");
		
		ToolTipManager.sharedInstance().setInitialDelay(0);
		ToolTipManager.sharedInstance().setReshowDelay(0);

		if (this.verticalBoxplots)
		{
			int lower_x = (int) Math.round((1.0 - WIDTH_FILL_FRACTION) * 0.5
				* super.getWidth());
			int upper_x = lower_x + widthBoxplot;
			
    		if (p.x > lower_x && p.x < upper_x
    			&& (p.y > (locationMedian - 5)) && p.y < (locationMedian + 5))
    		{
    			ToolTipManager.sharedInstance().setEnabled(true);
    			this.setToolTipText(Statistiek.rb.getString("medianIs") + Statistiek.getStringValue(this.median));			
    			setHighlightValues(false, false, true, false, false);
    		}
    		else if (p.x > lower_x && p.x < upper_x
    			&& (p.y > (locationMinValue - 5)) && p.y < (locationMinValue + 5))
    		{
    			ToolTipManager.sharedInstance().setEnabled(true);
    			this.setToolTipText(Statistiek.rb.getString("minimumIs") + Statistiek.getStringValue(this.minValue));
    			setHighlightValues(true, false, false, false, false);
    		}
    		// TODO: when e.g. minValue and lowerQuartile are close together or the same
    		// show multiple tooltips
    		else if (p.x > lower_x && p.x < upper_x
    			&& (p.y > (locationLowerQuartile - 5)) && p.y < (locationLowerQuartile + 5))
    		{
    			ToolTipManager.sharedInstance().setEnabled(true);
    			this.setToolTipText(Statistiek.rb.getString("firstQuartileIs") + Statistiek.getStringValue(this.lowerQuartile));
    			setHighlightValues(false, true, false, false, false);
    		}
    		else if (p.x > lower_x && p.x < upper_x
    			&& (p.y > (locationUpperQuartile - 5)) && p.y < (locationUpperQuartile + 5))
    		{
    			ToolTipManager.sharedInstance().setEnabled(true);
    			this.setToolTipText(Statistiek.rb.getString("thirdQuartileIs") + Statistiek.getStringValue(this.upperQuartile));			
    			setHighlightValues(false, false, false, true, false);
    		}
    		else if (p.x > lower_x && p.x < upper_x
    			&& (p.y > (locationMaxValue - 5)) && p.y < (locationMaxValue + 5))
    		{
    			ToolTipManager.sharedInstance().setEnabled(true);
    			this.setToolTipText(Statistiek.rb.getString("maximumIs") + Statistiek.getStringValue(this.maxValue));			
    			setHighlightValues(false, false, false, false, true);
    		}
    		else
    		{
    			ToolTipManager.sharedInstance().setEnabled(false);
    			setHighlightValues(false, false, false, false, false);
    		}
		} // vertical boxplots
		else // horizontal boxplots
		{
			// lower y coordinate of the horizontal boxplot
			int upper_y = (int) Math.round((WIDTH_FILL_FRACTION) * super.getHeight());
			int lower_y = upper_y - widthBoxplot;
			
    		if (p.y > lower_y && p.y < upper_y
    			&& (p.x > (locationMedian - 5)) && p.x < (locationMedian + 5))
    		{
    			ToolTipManager.sharedInstance().setEnabled(true);
    			this.setToolTipText(Statistiek.rb.getString("medianIs") + Statistiek.getStringValue(this.median));			
    			setHighlightValues(false, false, true, false, false);
    		}
    		else if (p.y > lower_y && p.y < upper_y
    			&& (p.x > (locationMinValue - 5)) && p.x < (locationMinValue + 5))
    		{
    			ToolTipManager.sharedInstance().setEnabled(true);
    			this.setToolTipText(Statistiek.rb.getString("minimumIs") + Statistiek.getStringValue(this.minValue));
    			setHighlightValues(true, false, false, false, false);
    		}
    		else if (p.y > lower_y && p.y < upper_y
    			&& (p.x > (locationLowerQuartile - 5)) && p.x < (locationLowerQuartile + 5))
    		{
    			ToolTipManager.sharedInstance().setEnabled(true);
    			this.setToolTipText(Statistiek.rb.getString("firstQuartileIs") + Statistiek.getStringValue(this.lowerQuartile));
    			setHighlightValues(false, true, false, false, false);
    		}
    		else if (p.y > lower_y && p.y < upper_y
    			&& (p.x > (locationUpperQuartile - 5)) && p.x < (locationUpperQuartile + 5))
    		{
    			ToolTipManager.sharedInstance().setEnabled(true);
    			this.setToolTipText(Statistiek.rb.getString("thirdQuartileIs") + Statistiek.getStringValue(this.upperQuartile));			
    			setHighlightValues(false, false, false, true, false);
    		}
    		else if (p.y > lower_y && p.y < upper_y
    			&& (p.x > (locationMaxValue - 5)) && p.x < (locationMaxValue + 5))
    		{
    			ToolTipManager.sharedInstance().setEnabled(true);
    			this.setToolTipText(Statistiek.rb.getString("maximumIs") + Statistiek.getStringValue(this.maxValue));			
    			setHighlightValues(false, false, false, false, true);
    		}
    		else
    		{
    			ToolTipManager.sharedInstance().setEnabled(false);
    			setHighlightValues(false, false, false, false, false);
    		}
		}
		this.repaint();
	}
	
	private void setHighlightValues(boolean minValue,
		boolean lowerQuartile, boolean median, boolean upperQuartile, 
		boolean maxValue)
	{
		this.highlightMinValue = minValue;
		this.highlightLowerQuartile = lowerQuartile;
		this.highlightMedian = median;
		this.highlightUpperQuartile = upperQuartile;
		this.highlightMaxValue = maxValue;
	}
}
