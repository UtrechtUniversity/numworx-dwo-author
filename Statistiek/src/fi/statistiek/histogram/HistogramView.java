package fi.statistiek.histogram;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import javax.swing.*;

import fi.statistiek.ColorGenerator;
import fi.statistiek.ColorLegend;
import fi.statistiek.ColorPreviewer;
import fi.statistiek.Statistiek;
import fi.statistiek.histogram.HistogramModel.FrequencyTuple;
import fi.statistiek.types.AllowedTypes;
import fi.statistiek.types.ColumnType;

/**
 * MVC View for StatistiekView Histogram
 * 
 * @author ManuDrijvers, Sylvia van Borkulo
 * 
 */
/**
 * @author borku102
 *
 */
public class HistogramView extends JPanel implements Observer
{
	private HistogramModel model;
	private HistogramController controller;
	private HistogramUserOptionsPanel userOptionsPanel;
	private JButton dialogButton;

	public static final int KEUZEBALK_HOOGTE = 50;
	public static final int X_AS_OFFSET = 50;
	public static final int Y_AS_OFFSET = 50;
	public static final double MAX_SCREEN_FRACTION_FOR_BARS = 0.8;

	private double verticalBarWidth;
	private double horizontalBarWidth;

	/**
	 * List of the bar rectangles. For frequency polygons the rectangles correspond to the dots.
	 */
	private ArrayList<Rectangle> barRectangles;
	private JPanel mainPanel;
	private JScrollPane scrollPane;
	private ColorLegend colorLegend;

	private Random random;

	public static final Color BAR_COLOR = ColorGenerator.DEFAULT_VIEW_ELEMENT_COLOR;
	public static final Color SELECTED_BAR_COLOR = ColorGenerator.SELECTION_COLOR;

	private int xAxisOffset;
	private int yAxisOffset;
	private Point lastPolygonPoint;
	
	/**
	 * The number of the highlighted bar, or for frequency polygons the
	 * number of the highlighted dot. 
	 */
	private int highlightedBar = -1;
	private int highlightInSplit = -1;

	/**
	 * Constructor
	 * 
	 * @param model
	 *            MVC model
	 * @param controller
	 *            MVC controller
	 */
	public HistogramView(HistogramModel model, HistogramController controller)
	{
		super(new BorderLayout());

		this.model = model;
		this.model.addObserver(this);
		this.controller = controller;

		// create GUI
		userOptionsPanel = new HistogramUserOptionsPanel(this, controller, model);
		dialogButton = userOptionsPanel.getDialogButton();
		super.add(dialogButton, BorderLayout.SOUTH);

		this.mainPanel = new HistogramBarPanel();
		this.mainPanel.addMouseMotionListener((MouseMotionListener) this.mainPanel);
		// test syl: onderstaande werkt niet...
//		this.mainPanel.setBackground(Statistiek.backgroundColor);
//		this.setBackground(Statistiek.backgroundColor);
		this.mainPanel.setBackground(Color.WHITE);
		
		this.scrollPane = new JScrollPane(this.mainPanel);
		this.scrollPane.getVerticalScrollBar().setUnitIncrement(Statistiek.scrollSpeedUnit);
		
		super.add(this.scrollPane, BorderLayout.CENTER);
		this.mainPanel.addMouseListener(new BarClickListener());

		this.colorLegend = new ColorLegend("", null, null);
		super.add(this.colorLegend, BorderLayout.EAST);
		this.colorLegend.setVisible(false);

		this.random = new Random();
	}

	/**
	 * Get color for displaying multiple split groups in a single view
	 * 
	 * @param number
	 *            the number of the split group
	 * @return the color in which this split group will be displayed
	 */
	private Color getColor(int number)
	{
//		if (number < this.colorList.size())
//		{
//			return this.colorList.get(number);
//		}
//		else
//		{
//			Color c = new Color(this.random.nextInt(256),
//				this.random.nextInt(256), this.random.nextInt(256));
//			this.colorList.add(c);
//			return c;
//		}

		return ColorGenerator.getColor(number);
	}

	public void setModel(HistogramModel model)
	{
		this.model = model;
		this.model.addObserver(this);
		userOptionsPanel.setModel(model);
		this.update(null, null);
	}

	/**
	 * Gets the currently selected item of the combobox allowing you to choose
	 * the column that this StatistiekView will display
	 * 
	 * @return index of currently selected item
	 */
	public int getVarBoxSelectedIndex()
	{
		return userOptionsPanel.getVarBoxSelectedIndex();
	}

	public int getSplitVarBoxSelectedIndex()
	{
		return userOptionsPanel.getSplitVarBoxSelectedIndex();
	}

	public boolean isCumulativeBoxSelected()
	{
		return userOptionsPanel.isCumulativeBoxSelected();
	}

	public boolean isSplitSingleViewSelected()
	{
		return userOptionsPanel.isSplitSingleViewSelected();
	}

	public boolean isNextToEachOtherSelected()
	{
		if (this.model.isFrequencyPolygonMode())
			return true; // is natuurljk een beetje gek
		return userOptionsPanel.isNextToEachOtherSelected();
	}

	public boolean isStackModeBoxSelected()
	{
		return userOptionsPanel.isStackModeBoxSelected();
	}
	
	public boolean isOptimizeScale()
	{
		return userOptionsPanel.isOptimizeScale();
	}

	/**
	 * Gets currently selected item of the combobox allowing you to choose the
	 * number of bins
	 * 
	 * @return currently selected number of bins
	 */
	public int getBinsBoxSelectedInt()
	{
		return userOptionsPanel.getBinsBoxSelectedInt();
	}

	public int getSplitBinsBoxSelectedInt()
	{
		return userOptionsPanel.getSplitBinsBoxSelectedInt();
	}

	public double getMinBoundary()
	{
		return this.userOptionsPanel.getMinBoundary();
	}
	
	/**
	 * Set min boundary with value d
	 * @param d
	 */
	public void setMinBoundary(double d)
	{
		this.userOptionsPanel.setMinBoundary(d);
	}

	/**
	 * Set max boundary with value d
	 * @param d
	 */
	public void setMaxBoundary(double d)
	{
		this.userOptionsPanel.setMaxOnScale(d);
	}

	public double getSplitMinBoundary()
	{
		return userOptionsPanel.getSplitMinBoundary();
	}

	/**
	 * Set min boundary with value min
	 * @param min
	 */
	public void setSplitMinBoundary(double min)
	{
		this.userOptionsPanel.setSplitMinBoundary(min);
	}

	public double getBinWidth()
	{
		return this.userOptionsPanel.getBinWidth();
	}

	public void setBinWidth(double d)
	{
		this.userOptionsPanel.setBinWidth(d);
	}

	public void setBinWidth()
	{
		this.userOptionsPanel.setBinWidth();
	}

	public double getSplitBinWidth()
	{
		return userOptionsPanel.getSplitBinWidth();
	}

	public void setSplitBinWidth(double d)
	{
		this.userOptionsPanel.setSplitBinWidth(d);
	}

	public void setSplitBinWidth()
	{
		this.userOptionsPanel.setSplitBinWidth();
	}

	/**
	 * Get the chosen axis from this.axisBox
	 * 
	 * @return true if x-axis selected
	 */
	public boolean xAxisSelected()
	{
		return userOptionsPanel.xAxisSelected();
	}

	/**
	 * Gets which item the user selected from the radiogroup
	 * choosing between percentage or amount.
	 * 
	 * @return true if percentage is chosen
	 */
	public boolean percentageItemSelected()
	{
		return userOptionsPanel.percentageItemSelected();
	}

	/**
	 * Gets which item the user selected from the radiogroup
	 * choosing between percentage in split category relative
	 * to split total or end total.
	 * 
	 * @return true if percentage in split is relative to split total
	 */
	public boolean percentageSplitTotalSelected()
	{
		return userOptionsPanel.percentageSplitTotalSelected();
	}

	/**
	 * Gets which item the user selected from the radiogroup
	 * choosing between labels under bin or labels between bins.
	 * 
	 * @return true if label under bin is chosen
	 */
	public boolean labelUnderBinItemSelected()
	{
		return userOptionsPanel.labelUnderBinSelected();
	}

	/**
	 * Calculate the width of each bar
	 * 
	 * @param numberOfBars
	 *            The amount of bars
	 */
	private void setBarWidth(int numberOfBars)
	{
		this.verticalBarWidth = ((double) (this.barAreaWidth() - numberOfBars - 1) / ((double) numberOfBars + 0.5));
		this.horizontalBarWidth = ((double) (this.barAreaHeight()
			- numberOfBars - 1) / ((double) numberOfBars + 0.5));
	}

	private AlphaComposite makeComposite(int splitClasses)
	{
//		System.out.println("Making composite for " + splitClasses
//			+ " splitClasses.");
		int type = AlphaComposite.SRC_ATOP;
		return (AlphaComposite.getInstance(type, (float) (1.0 / splitClasses)));
	}

	/**
	 * Get the location of the point representing the upper middle of the bar
	 * for given bar number and height.
	 * 
	 * @param barHeight
	 *            The height of the bar
	 * @param barNumber
	 *            The number of the bar
	 * @return the point where given bar would be painted
	 */
	private Point barLocation(int barHeight, int barNumber)
	{
		if (this.model.hasVerticalBars())
		{
			int x1 = this.yAxisOffset + barNumber + 1
				+ (int) (barNumber * this.verticalBarWidth);
			int x2 = this.yAxisOffset + barNumber + 1
				+ (int) ((barNumber + 1) * this.verticalBarWidth);
			int y = this.barAreaHeight() - barHeight;
			int xPoint;
			if (this.model.isFrequencyPolygonCumulativeMode())
			{
				xPoint = x2;
			}
			else
			{
				xPoint = (x1 + x2) / 2;
			}

			return new Point(xPoint, y);
		}
		else
		{
			int y1 = barNumber + 1
				+ (int) ((barNumber + 0.5) * this.horizontalBarWidth);
			int y2 = barNumber + 1
				+ (int) ((barNumber + 1.5) * this.horizontalBarWidth);
			int xPoint = this.yAxisOffset + barHeight;
			int yPoint;
			if (this.model.isFrequencyPolygonCumulativeMode())
			{
				yPoint = y2;
			}
			else
			{
				yPoint = (y1 + y2) / 2;
			}
			return new Point(xPoint, yPoint);
		}
	}

	/**
	 * Paint a single bar. In case of frequencypolygon the dot and connecting
	 * line is painted. The bar color will be the standard bar color.
	 * 
	 * @param g
	 *            The graphics in which this will be painted
	 * @param barLength
	 *            The length of the bar
	 * @param selectedLength
	 *            The length of the selected part of the bar
	 * @param barNumber
	 *            The number of the bar
	 * @param ySplitOffset
	 * @param xSplitOffset
	 * @param splitClass
	 * 		The number of the split class
	 */
	private void paintBar(Graphics g, int barLength, int selectedLength,
		int barNumber, int ySplitOffset, int xSplitOffset, int splitClass)
	{
		this.paintBar(g, barLength, selectedLength, barNumber, ySplitOffset,
			xSplitOffset, HistogramView.BAR_COLOR, 0, 0, false, splitClass);
	}

	/**
	 * Paint a single bar. In case of frequencypolygon, the dot and connecting
	 * line is painted.
	 * 
	 * @param g
	 *            The graphics in which this will be painted
	 * @param barLength
	 *            The length of the bar
	 * @param selectedLength
	 *            The length of the selected part of the bar
	 * @param barNumber
	 *            The number of the bar
	 * @param ySplitOffset
	 * @param xSplitOffset
	 * @param c
	 * 		The color of the bar
	 * @param numberOfBars
	 * 		Must be 'numberOfBarInBin'? If not split in single view, then 
	 * 		numberOfBars is 0. If split in single view, then numberOfBars 
	 * 		indicates the splitClass.
	 * @param totalBars
	 * @param drawNextToEachOther
	 * @param splitClass
	 * 		The number of the split class
	 */
	private void paintBar(Graphics g, int barLength, int selectedLength,
		int barNumber, int ySplitOffset, int xSplitOffset, Color c,
		int numberOfBars, int totalBars, boolean drawNextToEachOther, int splitClass)
	{
//		System.out.println("HistogramView.paintBar(): barLength = " + barLength 
//			+ ", barNumber = " + barNumber
//			+ ", numberOfBars = " + numberOfBars
//			+ ", splitClass = " + splitClass);

		if (this.model.isFrequencyPolygonMode())
		{
			g.setColor(c);
			Point p = this.barLocation(barLength, barNumber);
			int size = 4;
			
			if ((highlightedBar == barNumber) && (highlightInSplit == splitClass))
			{
//				System.out.println("HIGHLIGHT! HistogramView.paintBar(): barNumber = " + barNumber
//					+ ", splitClass = " + splitClass + ", numberOfBars = "
//					+ numberOfBars + ", totalBars = " + totalBars);

				Graphics2D g2 = (Graphics2D) g;
		        g2.setStroke(new BasicStroke(3));
				g.drawOval(p.x - size, p.y - size + ySplitOffset, 2 * size,
					2 * size);
		        g2.setStroke(new BasicStroke(1));
			}

			g.fillOval(p.x - size, p.y - size + ySplitOffset, 2 * size,
				2 * size);
			
			// Add bar to barRectangles 
			this.barRectangles.add(new Rectangle(
				p.x - size, p.y - size + ySplitOffset, 2 * size,
				2 * size));

			if (this.model.isFrequencyPolygonCumulativeMode()
				&& this.lastPolygonPoint == null)
			{
				this.lastPolygonPoint = this.barLocation(0, -1);
			}
			if (this.lastPolygonPoint != null)
			{
				g.drawLine(this.lastPolygonPoint.x, this.lastPolygonPoint.y
					+ ySplitOffset, p.x, p.y + ySplitOffset);
			}

			this.lastPolygonPoint = p;
		} // frequency polygon
		else
		{
			int columnIndex = this.model.getColumnIndex();
			AllowedTypes type = this.model.getStatTableModel().getColumnTypes().get(columnIndex).getType();
			
			int spacing = 0;
					
			if (type.equals(AllowedTypes.ENUM))
			{
				spacing = 4;
			}
//			if (labelUnderBinItemSelected() && type.equals(AllowedTypes.INTEGER) && getBinWidth() == 1)
//			{
////				System.out.println("HistogramView.paintBar(): INT & 1, spacing!");
//				spacing = 4;
//			}
			
//			System.out.println("HistogramView.paintBar(): type = " + type + ", binWidth = " 
//				+ getBinWidth() + ", spacing = " + spacing);

			double colorMixSymm = 0.5; // t.b.v. shading
			double colorMix = 0.7;

			if (this.model.hasVerticalBars())
			{
				int x1 = this.yAxisOffset + barNumber + 1
					+ (int) (barNumber * this.verticalBarWidth);
				int x2 = this.yAxisOffset + barNumber + 1
					+ (int) ((barNumber + 1) * this.verticalBarWidth);
				int y = this.barAreaHeight() - barLength;

				g.setColor(c);
				int barWidth = x2 - x1;
				int barOffset = 0;
				if (drawNextToEachOther)
				{
					barWidth = (barWidth - 6) / totalBars;
					barOffset = numberOfBars * barWidth + 3;
					barWidth = barWidth - 1;
				}
				
				if (type.equals(AllowedTypes.ENUM) 
					|| (type.equals(AllowedTypes.INTEGER)) && model.getBinWidth() == 1 && labelUnderBinItemSelected())
				{
					// Symmetrical shading
					
    				Color shadingColor = ColorPreviewer.mixColors(c, Color.WHITE,
						colorMixSymm);
    				
    				int x_coordinate = x1 + barOffset + spacing;
    				int y_coordinate = y + ySplitOffset;
    				int width = barWidth - spacing;
    				int height = barLength - selectedLength;
    				
    				fillRectWithSymmShade(x_coordinate, y_coordinate, 
    					width, height, g, c, shadingColor, true);
    				
    				// draw selected bar
    				y_coordinate = y + barLength - selectedLength + ySplitOffset;
    				height = selectedLength;

    				// draw the selected bar darker in its original color c 
    				shadingColor = ColorPreviewer.mixColors(c.darker().darker(), Color.WHITE,
						colorMixSymm);
    				fillRectWithSymmShade(x_coordinate, y_coordinate, 
    					width, height, g, c.darker().darker(), shadingColor, true);
				}
				else
				{
					// shading to the right 
					// in order to support visually that the upper bin boundary is not included
					
    				Color shadingColor = ColorPreviewer.mixColors(c, Color.WHITE,
						colorMix);

    				// paint bar
    				int x_coordinate = x1 + barOffset + spacing;
    				int y_coordinate = y + ySplitOffset;
    				int width = barWidth - spacing;
    				int height = barLength - selectedLength;
    				fillRectWithShadeToUpperBinSide(x_coordinate, y_coordinate, 
    					width, height, g, c, shadingColor, true);

    				// paint selected bar
    				y_coordinate = y + barLength - selectedLength + ySplitOffset;
    				height = selectedLength;

    				// draw the selected bar darker in its original color c 
    				shadingColor = ColorPreviewer.mixColors(c.darker().darker(), Color.WHITE, colorMix);
    				fillRectWithShadeToUpperBinSide(x_coordinate, y_coordinate, 
    					width, height, g, c.darker().darker(), shadingColor,
    					true);
				}
				
				g.setColor(Color.BLACK);
				
				if ((highlightedBar == barNumber) && (highlightInSplit == splitClass))
				{
//					System.out.println("HIGHLIGHT! HistogramView.paintBar(): barNumber = " + barNumber
//						+ ", splitClass = " + splitClass + ", numberOfBars = "
//						+ numberOfBars + ", totalBars = " + totalBars);

    				Graphics2D g2 = (Graphics2D) g;
    		        g2.setStroke(new BasicStroke(3));
    				g.drawLine(x1 - 1 + barOffset + spacing, y + ySplitOffset,
    					x1 + barOffset + barWidth, y + ySplitOffset);
    		        g2.setStroke(new BasicStroke(1));
				}
				
   				g.drawRect(x1 - 1 + barOffset + spacing, y + ySplitOffset,
   					barWidth + 1 - spacing, barLength);

				this.barRectangles.add(new Rectangle(x1 - 1 + barOffset + spacing, y
					+ ySplitOffset, barWidth + 1 - spacing, barLength));
			} // vertical bars
			else // horizontal bars
			{
				int x1 = this.yAxisOffset;
				int y1 = barNumber + 1
					+ (int) ((barNumber + 0.5) * this.horizontalBarWidth);
				int y2 = barNumber + 1
					+ (int) ((barNumber + 1.5) * this.horizontalBarWidth);

				int barWidth = y2 - y1;
				int barOffset = 0;
				if (drawNextToEachOther)
				{
					barWidth = barWidth / totalBars;
					barOffset = numberOfBars * barWidth;
					barWidth = barWidth - 1;
				}

				if (type.equals(AllowedTypes.ENUM) 
					|| (model.getBinWidth() == 1 && type.equals(AllowedTypes.INTEGER) && labelUnderBinItemSelected()))
				{
 					Color shadingColor = ColorPreviewer.mixColors(c, Color.WHITE,
						colorMixSymm);
    				
    				int x_coordinate = x1 + xSplitOffset + selectedLength;
    				int y_coordinate = y1 + ySplitOffset + barOffset + spacing;
    				int width = barLength - selectedLength;
    				int height = barWidth - spacing;
    				
    				fillRectWithSymmShade(x_coordinate, y_coordinate, 
    					width, height, g, c, shadingColor, false);
    				
    				// draw selected bar
    				x_coordinate = x1 + xSplitOffset;
    				width = selectedLength;
    				shadingColor = ColorPreviewer.mixColors(c.darker().darker(), Color.WHITE,
						colorMixSymm);
    				fillRectWithSymmShade(x_coordinate, y_coordinate, 
    					width, height, g, c.darker().darker(), shadingColor, false);

				}	
				else
				{
					// shading to the down side 
					// in order to support visually that the upper bin boundary is not included

					Graphics2D g2d = (Graphics2D) g;
    				Color shadingColor = ColorPreviewer.mixColors(c, Color.WHITE,
						colorMix);
    				
    				// paint bar
    				int x_coordinate = x1 + xSplitOffset + selectedLength;
    				int y_coordinate = y1 + ySplitOffset + barOffset + spacing;
    				int width = barLength - selectedLength;
    				int height = barWidth - spacing;
    				fillRectWithShadeToUpperBinSide(x_coordinate, y_coordinate, 
    					width, height, g, c, shadingColor, false);

					// paint selected bar
					shadingColor = ColorPreviewer.mixColors(c.darker().darker(), Color.WHITE,
						colorMix);
					x_coordinate = x1 + xSplitOffset;
					width = selectedLength;
					fillRectWithShadeToUpperBinSide(x_coordinate, y_coordinate, 
						width, height, g, c.darker().darker(), shadingColor, false);
				}

				g.setColor(Color.BLACK);

				if ((highlightedBar == barNumber) && (highlightInSplit == splitClass))
				{
//					System.out.println("HIGHLIGHT! HistogramView.paintBar(): barNumber = " + barNumber
//						+ ", splitClass = " + splitClass + ", numberOfBars = "
//						+ numberOfBars + ", totalBars = " + totalBars);

    				Graphics2D g2 = (Graphics2D) g;
    		        g2.setStroke(new BasicStroke(3));
    				g.drawLine(x1 + xSplitOffset - 1 + barLength, 
    					y1 - 1 + ySplitOffset + barOffset + spacing,
    					x1 + xSplitOffset - 1 + barLength,
    					y1 + ySplitOffset + barOffset + barWidth);
    		        g2.setStroke(new BasicStroke(1));
				}

				g.drawRect(x1 + xSplitOffset - 1, y1 - 1 + ySplitOffset
					+ barOffset + spacing, barLength, barWidth + 1 - spacing);

				this.barRectangles.add(new Rectangle(x1 + xSplitOffset - 1, y1
					- 1 + ySplitOffset + barOffset + spacing, barLength,
					barWidth + 1 - spacing));
			}
		} // no frequency polygon
		this.repaint();
	}

	/**
	 * Fills the rectangle starting at (x, y) with given width and height,
	 * and adds shade to both the right and left side, using color c for the body
	 * and shadingColor for the shading parts.
	 *  
	 * @param x
	 * 	x coordinate of the rectangle starting point
	 * @param y
	 * 	y coordinate of the rectangle starting point
	 * @param width
	 * 	width of the rectangle
	 * @param height
	 * 	height of the rectangle
	 * @param g
	 * 	the graphics to be used
	 * @param c
	 * 	the color of the body
	 * @param shadingColor
	 * 	the color of the end of the shade
	 * @param isVerticalBar
	 * 	true if the rectangle is a vertical bar,
	 * 	false if the rectangle is a horizontal bar
	 */
	private void fillRectWithSymmShade(int x, int y,
		int width, int height, Graphics g, Color c, Color shadingColor,
		boolean isVerticalBar)
	{
//		double symmShadingFraction = (double) 1/4; // number indicating the part of the outside of the bar that is shaded
		double symmShadingFraction = (double) 1/3; // number indicating the part of the outside of the bar that is shaded

		Graphics2D g2d = (Graphics2D)g;

		// orig
		GradientPaint gradient;
		
		if (isVerticalBar)
		{
    		gradient = new GradientPaint(
    			x, y, shadingColor,
    			x + (int) (width * symmShadingFraction), y, c, 
    			false); 
		}
		else // horizontal bar
		{
    		gradient = new GradientPaint(
    			x, y, shadingColor,
    			x, y + (int)(height * symmShadingFraction), c, 
    			false);
		}
		
		g2d.setPaint(gradient);

		if (isVerticalBar)
		{
			g2d.fillRect(x, y, (int) (width * symmShadingFraction), height);
		}
		else
		{
			g2d.fillRect(x, y, width, (int) (height * symmShadingFraction));
		}
		
		g2d.setColor(c);
		if (isVerticalBar)
		{
			g2d.fillRect(x + (int) (width * symmShadingFraction), 
				y, (int) (width * ((1/symmShadingFraction) - 1) * symmShadingFraction), height);
		}
		else
		{
			g2d.fillRect(x, y  + (int) (height * symmShadingFraction), 
				width, (int) (height * ((1/symmShadingFraction) - 1) * symmShadingFraction));
		}

		if (isVerticalBar)
		{
    		gradient = new GradientPaint(
    			x + (int) (width * ((1/symmShadingFraction) - 1) * symmShadingFraction), 
    			y, 
    			c,
    			x + width, 
    			y, shadingColor, false);
		}
		else // horizontal bar
		{
    		gradient = new GradientPaint(
    			x, 
    			y + (int) (height * ((1/symmShadingFraction) - 1) * symmShadingFraction), 
    			c,
    			x, 
    			y + height, shadingColor, false);
		}
		g2d.setPaint(gradient);
		
		if (isVerticalBar)
		{
    		g2d.fillRect(
    			x + (int) (width * ((1/symmShadingFraction) - 1) * symmShadingFraction) - 1,
    			y, 
    			(int) (width * symmShadingFraction) + 2,
    			height); // door int afrondingen wat extra marge nemen
		}
		else
		{
			g2d.fillRect(
				x,
    			y  + (int) (height * ((1/symmShadingFraction) - 1) * symmShadingFraction) - 1, 
    			width, 
    			(int) (height * symmShadingFraction) + 2); // door int afrondingen wat extra marge nemen
		}
	}
	
	/**
	 * Fills the rectangle starting at (x, y) with given width and height,
	 * and adds shade to the upper bin boundary side, using color c for the body
	 * and shadingColor for the shading part.
	 *  
	 * @param x
	 * 	x coordinate of the rectangle starting point
	 * @param y
	 * 	y coordinate of the rectangle starting point
	 * @param width
	 * 	width of the rectangle
	 * @param height
	 * 	height of the rectangle
	 * @param g
	 * 	the graphics to be used
	 * @param c
	 * 	the color of the body
	 * @param shadingColor
	 * 	the color of the end of the shade
	 * @param isVerticalBar
	 * 	true if the rectangle is a vertical bar,
	 * 	false if the rectangle is a horizontal bar
	 */
	private void fillRectWithShadeToUpperBinSide(int x, int y,
		int width, int height, Graphics g, Color c, Color shadingColor, 
		boolean isVerticalBar)
	{
		Graphics2D g2d = (Graphics2D)g;

		GradientPaint gradient;
		
		if (isVerticalBar)
		{
    		gradient = new GradientPaint(
    			x, 
    			y, c,
    			x + width, 
    			y, shadingColor, false);
		}
		else // horizontal bar
		{
    		gradient = new GradientPaint(
    			x, 
    			y, c,
    			x, 
    			y + height, shadingColor, false);
		}
		g2d.setPaint(gradient);

		// paint bar
		g2d.fillRect(x, y, width, height);
	}

	private void fillCumulativeFreqPolygonSegment(Graphics g, int dotHeight,
		int stackHeight, int prevDotHeight, int prevStackHeight, int dotNumber,
		Color color)
	{
		g.setColor(color);
		Point p1 = this.barLocation(prevStackHeight, dotNumber - 1);
		Point p2 = this.barLocation(prevDotHeight, dotNumber - 1);
		Point p3 = this.barLocation(dotHeight, dotNumber);
		Point p4 = this.barLocation(stackHeight, dotNumber);
		int[] xPoints =
			{ p1.x, p2.x, p3.x, p4.x };
		int[] yPoints =
			{ p1.y, p2.y, p3.y, p4.y };
		g.fillPolygon(xPoints, yPoints, 4);
		this.lastPolygonPoint = p3;

		if (!this.model.isFrequencyPolygonStackMode())
		{
			// paint area above this segment white to get the correct color
			// mixing when using alpha values
			if (this.model.hasVerticalBars())
			{
				yPoints[0] = 0;
				yPoints[3] = 0;
			}
			else
			{
				xPoints[0] = this.barAreaWidth();
				xPoints[3] = this.barAreaWidth();
			}
			g.setColor(Color.WHITE);
			g.fillPolygon(xPoints, yPoints, 4);
		}
	}

	/**
	 * Get the maximum value in the array.
	 * 
	 * @param array
	 * @return
	 */
	private int arrayMax(int[] array)
	{
		if (array.length == 0)
		{
			return -1;
		}
		int max = array[0];
		for (int i = 1; i < array.length; i++)
		{
			if (array[i] > max)
			{
				max = array[i];
			}
		}
		return max;
	}

	/**
	 * Get the maximum frequency value in the array.
	 * 
	 * @param array
	 * @return
	 */
	private int arrayMax(FrequencyTuple[] array)
	{
		if (array.length == 0)
		{
			return -1;
		}
		int max = array[0].frequency;
		for (int i = 1; i < array.length; i++)
		{
			if (array[i].frequency > max)
			{
				max = array[i].frequency;
			}
		}
		return max;
	}

	private int tupleArrayMax(FrequencyTuple[] array)
	{
		if (array.length == 0)
		{
			return -1;
		}
		int max = array[0].frequency;
		for (int i = 1; i < array.length; i++)
		{
			if (array[i].frequency > max)
			{
				max = array[i].frequency;
			}
		}
		return max;
	}

	/**
	 * Get the sum of all frequencies in the array.
	 * 
	 * @param array
	 * @return
	 */
	private int tupleArraySum(FrequencyTuple[] array)
	{
		int sum = 0;
		for (FrequencyTuple ft : array)
		{
			sum += ft.frequency;
		}
		return sum;
	}

	/**
	 * @return The sum of all elements on even indices
	 */
	private int arrayEvenSum(int[] array)
	{
		int sum = 0;
		for (int i = 0; i < array.length; i += 2)
		{
			sum += array[i];
		}
		return sum;
	}

	private int barAreaWidth()
	{
		// breedte verticale scrollbar aftrekken
		return this.getWidth() - this.yAxisOffset
			- (this.colorLegend.isVisible() ? this.colorLegend.getWidth() : 0)
			- this.scrollPane.getVerticalScrollBar().getWidth();
	}

	private int barAreaHeight()
	{
		// return this.getHeight() - HistogramView.X_AS_OFFSET -
		// (this.model.getTableModel().isViewsEditable() ?
		// HistogramView.KEUZEBALK_HOOGTE : 0);
		// return this.getHeight() - this.xAxisOffset-
		// (this.model.getTableModel().isViewsEditable() ?
		// HistogramView.KEUZEBALK_HOOGTE : 0);
		return this.scrollPane.getHeight() - this.xAxisOffset;
	}

	/**
	 * Paint the axis text labels
	 * 
	 * @param g
	 *            The graphics in which the labels will be painted
	 */
	private void paintAxisLabels(Graphics g, int yOffset, int splitClass)
	{
		Font font = super.getFont().deriveFont(super.getFont().getStyle());
		FontMetrics fm = super.getFontMetrics(font);
		AffineTransform at = new AffineTransform();
		at.rotate(Math.PI * 1.5);
		Font rotateFont = font.deriveFont(at);

		String s1 = this.model.hasVerticalBars() ? (this.model.getPercentage() ? Statistiek.rb
			.getString("percentageLabel") : Statistiek.rb
			.getString("frequentieLabel"))
			: this.model.getStatTableModel().getColumnName(
				this.model.getColumnIndex());
		String s2 = !this.model.hasVerticalBars() ? (this.model.getPercentage() ? Statistiek.rb
			.getString("percentageLabel") : Statistiek.rb
			.getString("frequentieLabel"))
			: this.model.getStatTableModel().getColumnName(
				this.model.getColumnIndex());
			
		g.setColor(Color.BLACK);
		g.setFont(rotateFont);
		g.drawString(s1, fm.getHeight(),
			this.barAreaHeight() / 2 + fm.stringWidth(s1) / 2 + yOffset);
		g.setFont(font);
		g.drawString(s2,
			(this.barAreaWidth() - this.yAxisOffset - fm.stringWidth(s2)) / 2
				+ this.yAxisOffset, 
			this.barAreaHeight() + this.xAxisOffset - 10 + yOffset);

		if (this.model.getStatTableModel().numberOfSplitVarClasses(
			this.model.getSplitOptions()) > 1
			&& !this.model.isSplitInSingleView())
		{
			String name = this.model.getStatTableModel().getColumnName(
				this.model.getSplitOptions().getColumnSplitIndex());
			String s = name
				+ ": "
				+ this.model.getSplitOptions().getSplitClassLabel(splitClass,
					this.model.getStatTableModel());
			g.drawString(s, 10, this.barAreaHeight() + this.xAxisOffset - 10
				+ yOffset);
		}

		g.fillRect(0, this.barAreaHeight() + this.xAxisOffset - 2 + yOffset,
			super.getWidth(), 1);
	}

	/**
	 * Get the max frequency in the given array.
	 * 
	 * @param frequencies
	 * @return
	 */
	private int maxFrequency(int[] frequencies)
	{
		int max;
		if (this.model.isFrequencyPolygonMode()
			&& this.model.isFrequencyPolygonCumulativeMode())
		{
			// max frequency is total frequency, because we're in cumulative
			// mode
			max = this.arrayEvenSum(frequencies);
		}
		else
		{
			max = this.arrayMax(frequencies);
		}
		return max;
	}
	
	/**
	 * Get the max percentage in the given array.
	 * 
	 * @param frequencies
	 * @return
	 */
	private int maxPercentage(int[] frequencies)
	{
		double max;
		if (this.model.isFrequencyPolygonMode()
			&& this.model.isFrequencyPolygonCumulativeMode())
		{
			// max percentage is 100%, because we're in cumulative
			// mode
			max = 100;
		}
		else
		{
			double freq = this.arrayMax(frequencies);
			double totalFrequency = this.arrayEvenSum(frequencies);
			max = 100 * freq / totalFrequency;
		}
		return (int) max;
	}
	
	/**
	 * Get the max percentage in the given array.
	 * 
	 * @param frequencies
	 * @return
	 */
	private int maxPercentage(FrequencyTuple[] frequencies)
	{
		double max;
		if (this.model.isFrequencyPolygonMode()
			&& this.model.isFrequencyPolygonCumulativeMode())
		{
			// max percentage is 100%, because we're in cumulative
			// mode
			max = 100;
		}
		else
		{
			double freq = this.arrayMax(frequencies);
			double totalFrequency = this.tupleArraySum(frequencies);
			max = 100 * freq / totalFrequency;
		}
		return (int) max;
	}
	
	/**
	 * Get the maximum frequency over all splits.
	 * 
	 * @param frequenciesArray
	 * @return
	 */
	private int maxFrequencyOverAllSplits(int[][] frequenciesArray)
	{
		int max = 0;
		
		for (int[] splitFreq : frequenciesArray)
		{
			max = Math.max(max, this.maxFrequency(splitFreq));
		}
		
		return max;
	}
	
	/**
	 * Get the maximum percentage relative to the split (split total = 100%) over all splits.
	 * 
	 * @param frequenciesArray
	 * @return
	 */
	private int maxPercentageOverAllSplits(int[][] frequenciesArray)
	{
		int max = 0;
		
		for (int[] splitFreq : frequenciesArray)
		{
			max = Math.max(max, this.maxPercentage(splitFreq));
		}
		
		return max;
	}
	
	/**
	 * Get the maximum percentage relative to the split (split total = 100%) over all splits.
	 * 
	 * @param frequenciesArray
	 * @return
	 */
	private int maxPercentageOverAllSplits(FrequencyTuple[][] frequenciesArray)
	{
		int max = 0;
		
		for (int i = 0; i < frequenciesArray.length; i++)
		{
			max = Math.max(max, this.maxPercentage(frequenciesArray[i]));
		}
		
		return max;
	}
	
	private int getSplitWithMaxFrequency(int[][] frequenciesArray)
	{
		int split = 0;
		int maxPerfrequency;
		
		int max = this.maxFrequencyOverAllSplits(frequenciesArray);
		for (int[] splitFreq : frequenciesArray)
		{
			maxPerfrequency = Math.min(max, this.maxFrequency(splitFreq));
			if (maxPerfrequency == max)
				break;
			else
				split++;
		}
		
		return split;
	}
	
	/**
	 * Get the maximum frequency over all splits.
	 * 
	 * @param frequenciesArray
	 * @return
	 */
	private int maxFrequencyOverAllSplits(FrequencyTuple[][] frequenciesArray)
	{
		int max = 0;
		
		for (FrequencyTuple[] splitFreqTuple : frequenciesArray)
		{
			max = Math.max(max, this.maxFrequency(splitFreqTuple));
		}
		
		return max;
	}

	private int maxFrequency(FrequencyTuple[] frequencies)
	{
		int max;
		if (this.model.isFrequencyPolygonMode()
			&& this.model.isFrequencyPolygonCumulativeMode())
		{
			// max frequency is total frequency, because we're in cumulative
			// mode
			max = this.tupleArraySum(frequencies);
		}
		else
		{
			max = this.tupleArrayMax(frequencies);
		}
		return max;
	}

	/**
	 * Bepaalt de max frequency voor de gegeven split class.
	 * 
	 * @param frequencies
	 * @param splitClass
	 * @return
	 */
	private int maxFrequency(int[][] frequencies, int splitClass)
	{
		int max = 0;
		if (this.model.isSplitInSingleView())
		{
			if (!isNextToEachOtherSelected())
			{ // gestapeld
				for (int[] splitFreq : frequencies)
				{
					max += this.maxFrequency(splitFreq);
				}
				return max;
			}
			else
			{ // staafjes naast elkaar
				return this.maxFrequency(frequencies[splitClass]);
			}
		} // split in single view
		else
		{ // split in multiple views
			return this.maxFrequency(frequencies[splitClass]);
		}
	}

	private int maxFrequency(FrequencyTuple[][] frequencies, int splitClass)
	{
		int max = 0;
		if (this.model.isSplitInSingleView())
		{
			if (!isNextToEachOtherSelected())
			{
				for (FrequencyTuple[] splitFreq : frequencies)
				{
					max += this.maxFrequency(splitFreq);
				}
				return max;
			}
			else
			{
				for (FrequencyTuple[] splitFreq : frequencies)
				{
					max = Math.max(max, this.maxFrequency(splitFreq));
				}
				return max;
			}
		}
		else
		{
			return this.maxFrequency(frequencies[splitClass]);
		}
	}

	/**
	 * Get the sum of the frequencies in the given split class.
	 * 
	 * @param frequencies
	 * @param splitClass
	 * @return the sum of the frequencies in the given split class
	 */
	private int getFrequenciesSum(int[][] frequencies, int splitClass)
	{
		int sum = 0;
		if (this.model.isSplitInSingleView()
			&& this.model.isFrequencyPolygonMode()
			&& this.model.isFrequencyPolygonCumulativeMode()
			&& this.model.isFrequencyPolygonStackMode())
		{
			for (int[] splitFreq : frequencies)
			{
				sum += this.arrayEvenSum(splitFreq);
			}
			return sum;
		}
		else
		{
			return this.arrayEvenSum(frequencies[splitClass]);
		}
	}
	
	/**
	 * Get the sum of the frequencies over all split classes.
	 * 
	 * @param frequencies
	 * @return the sum of the frequencies over all split classes
	 */
	private int getFrequenciesSum(int[][] frequencies)
	{
		int sum = 0;
		for (int[] splitFreq : frequencies)
		{
			sum += this.arrayEvenSum(splitFreq);
		}
		return sum;
	}
	
	/**
	 * Get the maximum of the summed frequencies of all split classes.
	 * 
	 * @param frequencies
	 * @param splitClass
	 * @return the maximum of the summed frequencies of all split classes
	 */
	private int getMaxFrequenciesSumOverSplitClasses(int[][] frequencies)
	{
		int sum = 0;

		// for split in single view, the return value is the max of summed frequencies per splitClass
		for (int[] splitFreq : frequencies)
		{
			sum = Math.max(sum, this.arrayEvenSum(splitFreq));
		}
		return sum;
	}
	
	/**
	 * 
	 * @param frequencies
	 * @param splitClass
	 * @return
	 */
	private int getFrequenciesSum(FrequencyTuple[][] frequencies, int splitClass)
	{
		int sum = 0;
		if (this.model.isSplitInSingleView()
			&& this.model.isFrequencyPolygonMode()
			&& this.model.isFrequencyPolygonCumulativeMode()
			&& this.model.isFrequencyPolygonStackMode())
		{
			for (FrequencyTuple[] splitFreq : frequencies)
			{
				sum += this.tupleArraySum(splitFreq);
			}
			return sum;
		}
		else
		{
			return this.tupleArraySum(frequencies[splitClass]);
		}
	}

	/**
	 * Returns the sum of the frequencies over all split classes.
	 * 
	 * @param frequencies
	 * @return
	 */
	private int getFrequenciesSum(FrequencyTuple[][] frequencies)
	{
		int sum = 0;
		for (FrequencyTuple[] splitFreq : frequencies)
		{
			sum += this.tupleArraySum(splitFreq);
		}
		return sum;
	}

	/**
	 * Paint the axes with number labels and bars for numerical data.
	 * 
	 * @param g
	 *	The graphics in which the bars will be painted
	 * @param allFrequencies
	 *	Array of frequencies, one for each splitClass, containing for each bin
	 *	the frequency for the bin and the number of selected items within the bin. 
	 * @param splitClass
	 * 	The number of the splitClass, starting with 0. Is 0 when there is no split.
	 */
	private void paintNumberClass(Graphics2D g, int[][] allFrequencies,
		int splitClass)
	{
		boolean normalFit;
		double availableSpace;
		double maxValueOnAxis;
		double scale;
		double amountScale;
		
		int[] frequencies = allFrequencies[splitClass];
		g.setFont(super.getFont());
		FontMetrics fm = g.getFontMetrics();
		AffineTransform at = new AffineTransform();
		double theta = Math.PI * 1.75;
		at.rotate(theta); // 315 graden met de klok mee; 45 graden tegen de klok in
		Font rotateFont = super.getFont().deriveFont(at);
		int ySplitOffset = splitClass * (this.scrollPane.getHeight() - 5);

		availableSpace = HistogramView.MAX_SCREEN_FRACTION_FOR_BARS *
        	(this.model.hasVerticalBars() ? 
        		this.barAreaHeight() : 
        		this.barAreaWidth()); // if vertical then availableSpace represents height, if horizontal then width
		
		// determine max value on axis
		double max = this.maxFrequencyOverAllSplits(allFrequencies);
		if (this.model.getPercentage()
			&& this.model.getPercentageSplitTotal()) // split total is 100%
		{
			maxValueOnAxis = this.maxPercentageOverAllSplits(allFrequencies);
		}
		else if (this.model.getPercentage()) // end total is 100%
		{
			maxValueOnAxis = 100.0 * max / (this.getFrequenciesSum(allFrequencies)); 
		}
		else
		{
			maxValueOnAxis = max;
		}
			
		// determine scale for the axis with percentages or amounts; should be the same for splits in multiple views
		if (maxValueOnAxis == 0)
		{
			scale = 0;
		}
		else
		{
			scale =  availableSpace/maxValueOnAxis;
		}

		// set bar width
		int numberOfBars = frequencies.length / 2;
		if (isOptimizeScale())
		{
			this.setBarWidth(numberOfBars);
		}
		else
		{
			int numberOfBinsOnScale = this.getNumberOfBinsfromBinsSettings(); 
			this.setBarWidth(numberOfBinsOnScale);
		}
		
		// bepaal de bins
		ArrayList<Number> binsOnScale = this.getBinsOnScale();

		if (this.model.hasVerticalBars())
		{
			this.yAxisOffset = this.determineDependentAxisWidth(scale) + 15 + fm.getHeight();

			// check if the bin boundary strings will fit and determine the longest
			normalFit = true;
			int longest = 0;
			int width;
			
			// bepaal normalFit (of labels bij as onder staven passen of gekanteld moeten worden)
			if (this.model.getLabelUnderBin())
			{
				int marge = 5;
				int columnIndex = this.model.getColumnIndex();
				AllowedTypes type = this.model.getStatTableModel().getColumnTypes().get(columnIndex).getType();
				
				for (int i = 0; i < binsOnScale.size(); i++)
				{
					String s = Statistiek.getStringValue(binsOnScale.get(i).doubleValue());
					if (i < binsOnScale.size() - 1)
					{
						String s_labelUnderBin;
						if (type.equals(AllowedTypes.INTEGER) && ((int) model.getBinWidth()) == 1)// klopt binWidth als geen data?
						{
							// Voor gehele getallen met 1 waarde per klasse, 1 getal tonen onder de staaf
							s_labelUnderBin = s;
						}
						else
						{
    						s_labelUnderBin = s + "-<" +
								Statistiek.getStringValue(binsOnScale.get(i + 1).doubleValue());
						}
						
						width = fm.stringWidth(s_labelUnderBin) + marge;
						if (width > this.verticalBarWidth)
						{
							normalFit = false;
						}
						if (width > longest)
						{
							longest = width;
						}
					}
				}
			} // label under bin
			else // label between bins
			{
				for (Number d : binsOnScale)
				{
					width = fm.stringWidth(Statistiek.getStringValue(d.doubleValue()));
					if (width > this.verticalBarWidth)
					{
						normalFit = false;
					}
					if (width > longest)
					{
						longest = width;
					}
				}
			}

			if (normalFit)
			{
				this.xAxisOffset = 50;
			}
			else
			{
				this.xAxisOffset = longest + 12 + fm.getHeight();
			}
		} // vertical bars
		else // horizontal bars
		{
			this.xAxisOffset = this.determineDependentAxisWidth(scale);

			int longest = 0;
			int width;

			// bepaal normalFit (of labels bij as onder staven passen of gekanteld moeten worden)
			if (this.model.getLabelUnderBin())
			{
				int columnIndex = this.model.getColumnIndex();
				AllowedTypes type = this.model.getStatTableModel().getColumnTypes().get(columnIndex).getType();
				
				for (int i = 0; i < binsOnScale.size(); i++)
				{
					String s = Statistiek.getStringValue(binsOnScale.get(i).doubleValue());
					if (i < binsOnScale.size() - 1)
					{
						String s_labelUnderBin;
						if (type.equals(AllowedTypes.INTEGER) && ((int) model.getBinWidth()) == 1) // klopt binWidth bij geen data?
						{
							// Voor gehele getallen met 1 waarde per klasse, 1 getal tonen bij de staaf
							s_labelUnderBin = s;
						}
						else
						{
    						s_labelUnderBin = s + "-<" +
								Statistiek.getStringValue(binsOnScale.get(i + 1).doubleValue());
						}
						
						width = fm.stringWidth(s_labelUnderBin);
						if (width > longest)
						{
							longest = width;
						}
					}
				}
			}
			else // labels between bins
			{
				// find longest binboundary label
				for (Number d : binsOnScale)
				{
					width = fm.stringWidth(d.toString());
					if (width > longest)
					{
						longest = width;
					}
				}
			}

			this.yAxisOffset = longest + 15 + fm.getHeight();
		} // horizontal bars

		// ---- correct scales ---- 
		// bepaal de amountscale om de bar-lengtes per split te kunnen berekenen
		if (this.model.getPercentage())
		{
			if ((this.model.getStatTableModel().numberOfSplitVarClasses(this.model.getSplitOptions()) > 1)
			&& (this.model.isSplitInSingleView()))
			{
				if (this.model.isFrequencyPolygonMode()
					&& this.model.isFrequencyPolygonCumulativeMode())
				{
					maxValueOnAxis = 100.0;
				}
				else
				{
					int maxInSplit = 0;
					double maxFraction = 0;
					for (int split = 0; split < allFrequencies.length; split++)
					{
						maxInSplit = this.maxFrequency(allFrequencies, split);
						max = Math.max(max, maxInSplit);
						if (this.getFrequenciesSum(allFrequencies) == 0)
						{
							maxFraction = 1;
						}
						else
						{
							maxFraction = Math.max(
								(double) maxInSplit
									/ this.getFrequenciesSum(allFrequencies),
								maxFraction);
						}
					}
	
					maxValueOnAxis = 100.0 * maxFraction;
				}
	
				if (maxValueOnAxis == 0)
				{
					scale = 0;
				}
				else
				{
					scale = availableSpace/maxValueOnAxis;
				}
			} // split in single view
			else
			{ // split in multiple views
				if (this.model.getPercentageSplitTotal())
				{
					// amountScale = de beschikbare ruimte (die toont maxValueOnScale) gedeeld door de frequentie (in de huidige splitClass) die deze maxValueOnScale-waarde geeft.
					// Let op: voor split total = 100% is max dus de frequentie (in de huidige splitClass) die deze maxValueOnScale-waarde geeft
					// maar dat is niet perse een frequentie die voorkomt in de huidige splitClass. Het is daarom een decimale waarde.
					max = maxValueOnAxis * this.getFrequenciesSum(allFrequencies, splitClass) / 100;
				}
			}

		} // percentage
		else
		{ // aantallen
			if (this.model.getStatTableModel().numberOfSplitVarClasses(
				this.model.getSplitOptions()) > 1)
			{
				// er is een split
				if (this.model.isSplitInSingleView())
				{
					int maxInSplit = 0;
					for (int split = 0; split < allFrequencies.length; split++)
					{
						maxInSplit = Math.max(maxInSplit,
							this.maxFrequency(allFrequencies, split));
						max = Math.max(max, maxInSplit);
					}

					maxValueOnAxis = maxInSplit;
					max = maxInSplit;

					if (maxValueOnAxis == 0)
					{
						scale = 0;
					}
					else
					{
						scale = availableSpace / maxValueOnAxis;
					}
				} // split in single view
			}
		}
		
		// PAINT SCALE
		this.paintScale(g, scale, ySplitOffset);

		// PAINT BARS
		// use amountScale to paint the correct bar length for each split
		if (max == 0)
		{
			amountScale = availableSpace;
		}
		else
		{
			amountScale = availableSpace/max;
		}

		ArrayList<Color> splitColors = new ArrayList<Color>();
		ArrayList<String> splitLabels = new ArrayList<String>();

		if (this.model.isSplitInSingleView()
			&& this.model.getStatTableModel().numberOfSplitVarClasses(
				this.model.getSplitOptions()) > 1)
		{
			if (this.model.isFrequencyPolygonMode()
				&& this.model.isFrequencyPolygonCumulativeMode()
				&& this.model.isFrequencyPolygonStackMode())
			{
				int[] totalFrequencies = new int[frequencies.length / 2];
				int[] frequencySum = new int[frequencies.length / 2];
				for (int split = 0; split < allFrequencies.length; split++)
				{
					int splitFreq[] = allFrequencies[split];
					this.lastPolygonPoint = null;
					Color c = this.getColor(split);
					splitColors.add(c);
					splitLabels.add(this.model.getSplitOptions()
						.getSplitClassLabel(splitClass,
							this.model.getStatTableModel()));

					for (int i = 0; i < splitFreq.length / 2; i++)
					{
						if (i > 0)
						{
							frequencySum[i] = frequencySum[i - 1]
								+ splitFreq[2 * i];
						}
						else
						{
							frequencySum[i] = splitFreq[2 * i];
						}

						this.fillCumulativeFreqPolygonSegment(
							g,
							(int) Math.round(amountScale
								* (totalFrequencies[i] + frequencySum[i])),
							(int) Math.round(amountScale * totalFrequencies[i]),
							(int) Math.round(amountScale
								* (i > 0 ? frequencySum[i - 1]
									+ totalFrequencies[i - 1] : 0)),
							(int) Math.round(amountScale
								* (i > 0 ? totalFrequencies[i - 1] : 0)), i, c);
					}
					for (int i = 0; i < frequencySum.length; i++)
					{
						totalFrequencies[i] += frequencySum[i];
					}
				}
			} // frequentiepolygoon, cumulatief, stapelen
			else
			{
				// frequentiepolygoon, cumulatief, split in single view
				if (this.model.isFrequencyPolygonMode()
					&& this.model.isFrequencyPolygonCumulativeMode())
				{
					// Paint the frequency polygon as dots and lines 
					for (int split = 0; split < allFrequencies.length; split++)
					{
						if (this.model.getPercentage())
						{
							amountScale = availableSpace/this.maxFrequency(allFrequencies[split]);
						}
						else
						{ // aantal
							// bij aantal moet hij de max frequency over alle splits nemen
							amountScale = availableSpace/this.maxFrequencyOverAllSplits(allFrequencies);
						}
						
						int[] splitFreq = allFrequencies[split];
						Color c = this.getColor(split);
						splitColors.add(c);
						splitLabels.add(this.model.getSplitOptions()
							.getSplitClassLabel(splitClass,
								this.model.getStatTableModel()));

						int frequencySum = 0;
						int frequencySelectedSum = 0;
						
						for (int i = 0; i < frequencies.length / 2; i++)
						{
							frequencySum += splitFreq[2 * i];
							frequencySelectedSum = splitFreq[2 * i + 1];
							this.paintBar(
								g,
								(int) (frequencySum * amountScale),
								(int) (frequencySelectedSum * amountScale),
								i, 0, 0, c, split,
								allFrequencies.length, true, split);
						}
						this.lastPolygonPoint = null;
					}

					g.setComposite(this.makeComposite(1));

				} // cumulatief frequentiepolygoon
				else
				{
					// niet-cumulatief frequentiepolygoon
					// en histogram met split in single view
					
					// Hier worden de samengestelde staafjes getekend.
					int[] cumHeight = new int[frequencies.length / 2];
					for (int split = 0; split < allFrequencies.length; split++)
					{
						int[] splitFreq = allFrequencies[split];
						Color c = this.getColor(split);
						splitColors.add(c);
						splitLabels.add(this.model.getSplitOptions()
							.getSplitClassLabel(splitClass,
								this.model.getStatTableModel()));

						for (int i = 0; i < frequencies.length / 2; i++)
						{
							if (this.model.hasVerticalBars())
							{
								if (this.isNextToEachOtherSelected())
								{
									this.paintBar(
										g,
										(int) (splitFreq[2 * i] * amountScale),
										(int) (splitFreq[2 * i + 1] * amountScale),
										i, 0, 0, c, split,
										allFrequencies.length, true, split);
								}
								else
								{
									this.paintBar(
										g,
										(int) (splitFreq[2 * i] * amountScale),
										(int) (splitFreq[2 * i + 1] * amountScale),
										i, -cumHeight[i], 0, c, 0, 0, false, split);
								}
							}
							else
							{
								if (this.isNextToEachOtherSelected())
								{
									this.paintBar(
										g,
										(int) (splitFreq[2 * i] * amountScale),
										(int) (splitFreq[2 * i + 1] * amountScale),
										i, 0, 0, c, split,
										allFrequencies.length, true, split);
								}
								else
								{
									this.paintBar(
										g,
										(int) (splitFreq[2 * i] * amountScale),
										(int) (splitFreq[2 * i + 1] * amountScale),
										i, 0, cumHeight[i], c, 0, 0, false, split);
								}
							}
							cumHeight[i] += (int) (splitFreq[2 * i] * amountScale);
						}
						this.lastPolygonPoint = null;
					}
					g.setComposite(this.makeComposite(1));
				}
			}
		} // split van meer dan 1 klasse
		else
		{ // no split or split in multiple views

			if (this.model.isFrequencyPolygonMode()
				&& this.model.isFrequencyPolygonCumulativeMode())
			{
				if (this.model.getStatTableModel().numberOfSplitVarClasses(
				this.model.getSplitOptions()) > 1)
				{
					// split in multiple views
					Color c = this.getColor(splitClass);
					splitColors.add(c);
					splitLabels.add(this.model.getSplitOptions()
						.getSplitClassLabel(splitClass,
							this.model.getStatTableModel()));

					int frequencySum = 0;
					int frequencySelectedSum = 0;
					for (int i = 0; i < frequencies.length / 2; i++)
					{
						frequencySum += frequencies[2 * i];
						frequencySelectedSum = frequencies[2 * i + 1];
						this.paintBar(
							g,
							(int) (frequencySum * amountScale),
							(int) (frequencySelectedSum * amountScale),
							i, ySplitOffset, 0, c, splitClass,
							allFrequencies.length, true, splitClass);
					}
				}
				else // no split
				{
					int frequencySum = 0;
					int frequencySelectedSum = 0;
					for (int i = 0; i < frequencies.length / 2; i++)
					{
						frequencySum += frequencies[2 * i];
						frequencySelectedSum = frequencies[2 * i + 1];
						this.paintBar(
							g, 
							(int) (frequencySum * amountScale),
							(int) (frequencySelectedSum * amountScale), 
							i, ySplitOffset, 0, splitClass);
					}
				}
			} // frequentiepolygoon cumulatief
			else
			{ // frequentiepolygoon niet-cumulatief of split in multiple views
				for (int i = 0; i < frequencies.length / 2; i++)
				{
					if (allFrequencies.length > 1)
						this.paintBar(
							g,
							(int) (frequencies[2 * i] * amountScale),
							(int) (frequencies[2 * i + 1] * amountScale), 
							i, ySplitOffset, 0, this.getColor(splitClass), 0, 0,
							false, splitClass);
					else
						this.paintBar(
							g,
							(int) (frequencies[2 * i] * amountScale),
							(int) (frequencies[2 * i + 1] * amountScale), 
							i, ySplitOffset, 0, splitClass);
				}
			}
		}

		g.setColor(Color.BLACK);

		int columnIndex = this.model.getColumnIndex();
		AllowedTypes type = this.model.getStatTableModel().getColumnTypes().get(columnIndex).getType();

		// PAINT BIN BOUNDARY LABELS
		if (this.model.hasVerticalBars())
		{
			// check if the bin boundary strings will fit
			normalFit = determineNormalFitForVerticalBars(fm, type);

			paintVerticalBinBoundaryLabels(g, normalFit, fm, theta, rotateFont,
				ySplitOffset, type, binsOnScale);
			
		} // vertical bars
		else
		{ // horizontal bars
			paintHorizontalBinBoundaryLabels(g, fm, ySplitOffset, type,
				binsOnScale);
		} // horizontal bars
	}

	/**
	 * Paint the bin boundary labels on the scale for a vertical histogram.
	 * 
	 * @param g
	 * @param fm
	 * @param ySplitOffset
	 * @param type
	 * @param binsOnScale
	 */
	private void paintHorizontalBinBoundaryLabels(Graphics2D g,
		FontMetrics fm, int ySplitOffset, AllowedTypes type, ArrayList<Number> binsOnScale)
	{
		// paint bin boundaries
		for (int i = 0; i < binsOnScale.size(); i++)
		{
			int y = (int) (i + (i + 0.5) * this.horizontalBarWidth);
			int x = this.yAxisOffset;
			// Get the string value (integer or double)
			String s = Statistiek.getStringValue(binsOnScale.get(i).doubleValue());
			
			if (this.model.getLabelUnderBin())
			{
				// put label under bin
				if (i < binsOnScale.size() - 1)
				{
					String s_labelUnderBin;
//					if (type.equals(AllowedTypes.INTEGER) && ((int) getBinWidth()) == 1)
					if (type.equals(AllowedTypes.INTEGER) && ((int) model.getBinWidth()) == 1)
					{
						if (!HistogramView.this.model.isFrequencyPolygonMode())
							s_labelUnderBin = s;
						else
						{
							s_labelUnderBin = s;
							// draw marker
							g.drawLine(x - 7, y + ySplitOffset, x - 2, y + ySplitOffset);
						}
					}
					else
					{
						s_labelUnderBin = s + "-<" +
							Statistiek.getStringValue(binsOnScale.get(i + 1).doubleValue());
						// draw marker
						g.drawLine(x - 7, y + ySplitOffset, x - 2, y + ySplitOffset);
					}
										
					int offset_labelUnderBin = fm.stringWidth(s_labelUnderBin);
					if (isWorkableInterval(i))
					{
						g.drawString(s_labelUnderBin, x - offset_labelUnderBin - 7, y
							+ (int) (fm.getHeight() / 2.0) - 2 + ySplitOffset + (int) this.horizontalBarWidth/2);
					}
				}
				else
				{
					// draw last marker
					g.drawLine(x - 7, y + ySplitOffset, x - 2, y + ySplitOffset);
				}
			}
			else
			{
				// draw marker
				g.drawLine(x - 7, y + ySplitOffset, x - 2, y + ySplitOffset);
				// put label between bins
				int offset = fm.stringWidth(s);
				if (isWorkableInterval(i))
				{
					g.drawString(s, x - offset - 7, 
						y + (int) (fm.getHeight() / 2.0) - 2 + ySplitOffset);
				}
			}
		}
	}
	
	/**
	 * Get the upper value of the upper bin on the scale.
	 * @return
	 */
	public double getMaxBinOnScale()
	{
		double max;
		
		ArrayList<Number> binsOnScale = getBinsOnScale();
		max = binsOnScale.get(binsOnScale.size() - 1).doubleValue();
		
		return max;
	}

	/**
	 * Get the bin boundaries on the histogram's scale.
	 * 
	 * @return
	 */
	ArrayList<Number> getBinsOnScale()
	{
		ArrayList<Number> bins;
		
		if (model.isOptimizeScale())
		{
			bins = new ArrayList<Number>(model.getBinBoundaries());
		}
		else
		{
			bins = this.getBinsFromBinsSettings();
		}
		
		return bins;
	}

	/**
	 * Get the bin boundaries based on the settings in the user options panel.
	 * If scale is not optimized, the settings may result in bins that exclude some of the data.
	 * 
	 * @return
	 */
	private ArrayList<Number> getBinsFromBinsSettings()
	{
		ArrayList<Number> bins = new ArrayList<Number>();
		double maxOnScale = model.getMaxOnScale();
		double binWidth = model.getBinWidth();
		
		if (this.model.getStatTableModel().isEmptyColumn(this.model.getColumnIndex()))
		{
			// use the settings without check for valid values
			double minOnScale = this.getMinBoundary();//model.getMinOnScale(); // voor lege kolom is minOnScale mogelijk op 0 gezet
			double binValue = minOnScale;
			
			if ((minOnScale == maxOnScale) || (binWidth == 0))
			{
				bins.add(minOnScale);
				bins.add(maxOnScale);
			}
			else
			{
				for (int i = 0; binValue < maxOnScale; i++)
				{
					binValue = minOnScale + i * binWidth;
					binValue = Statistiek.round(binValue, 8);
					bins.add(binValue);
				}
			}
			
			if (bins.isEmpty())
			{
				bins.add(0.0);
				bins.add(0.0);
			}
		}
		else
		{
			// use the settings, check for valid values
			double binValue;
			double startValue = model.getMinOnScale();
			binValue = startValue;

			if ((startValue == maxOnScale) || (binWidth == 0))
			{
				bins.add(startValue);
				bins.add(maxOnScale);
			}
			else
			{
				for (int i = 0; binValue < maxOnScale; i++)
				{
					binValue = startValue + i * binWidth;
					binValue = Statistiek.round(binValue, 8);
					bins.add(binValue);
				}
			}
		}

		return bins;
	}

	/**
	 * Get the number of bin boundaries based on the settings in the user options panel.
	 * 
	 * @return
	 */
	private int getNumberOfBinsfromBinsSettings()
	{
		ArrayList<Number> binBoundaries = getBinsFromBinsSettings();
		int number = binBoundaries.size() - 1;

		return number;
	}

	private double getMaxOnScale()
	{
		return this.userOptionsPanel.getMaxOnScale();
	}

	/**
	 * Retourneert of het waardelabel aan de as van het interval met index i
	 * getekend moet worden. Hoe smaller de staafbreedte, hoe meer waarden worden weggelaten. 
	 *   
	 * @param i
	 * @return
	 */
	private boolean isWorkableInterval(int i)
	{
		double width = verticalBarWidth+1;
		return (width<1 && i%40==0 
				|| width>=1 && width<2 && i%20==0 
				|| width>=2 && width<4 && i%10==0 
				|| width>=4 && width<8 && i%5==0 
				|| width>=8 && width<16 && i%2==0
				|| width>=16 );
	}
	
	private double getMultipliedBarwidth()
	{
		double width = verticalBarWidth+1;
		if (width<1) return verticalBarWidth*40;
		if (width>=1 && width<2) return verticalBarWidth*20;
		if (width>=2 && width<4) return verticalBarWidth*10;
		if (width>=4 && width<8) return verticalBarWidth*5; 
		if (width>=8 && width<16) return verticalBarWidth*2; 
		else  return verticalBarWidth;
	}

	/**
	 * Paint the bin boundary labels on the scale for a vertical histogram.
	 * 
	 * @param g
	 * @param normalFit
	 * @param fm
	 * @param theta
	 * @param rotateFont
	 * @param ySplitOffset
	 * @param type
	 * @param binsOnScale
	 */
	private void paintVerticalBinBoundaryLabels(Graphics2D g,
		boolean normalFit, FontMetrics fm, double theta, Font rotateFont,
		int ySplitOffset, AllowedTypes type, ArrayList<Number> binsOnScale)
	{
		if (normalFit)
		{
			int y = this.barAreaHeight();

			for (int i = 0; i < binsOnScale.size(); i++)
			{
				int x = (int) (this.yAxisOffset + i + i * this.verticalBarWidth);
				
				// Get the string value (integer or double)
				String s = Statistiek.getStringValue(binsOnScale.get(i).doubleValue());

				int offset = fm.stringWidth(s) / 2;
				if (this.model.getLabelUnderBin())
				{
					// put label under bin
					if (i < binsOnScale.size() - 1)
					{
						String s_labelUnderBin;
						if (type.equals(AllowedTypes.INTEGER) && ((int) model.getBinWidth()) == 1)
						{
							s_labelUnderBin = s;
							// for frequency polygon always
							// draw marker
							g.drawLine(x, y + ySplitOffset, x, y + 5 + ySplitOffset);
						}
						else
						{
							s_labelUnderBin = s + "-<" +
								Statistiek.getStringValue(binsOnScale.get(i + 1).doubleValue());
							// draw marker
							g.drawLine(x, y + ySplitOffset, x, y + 5 + ySplitOffset);
						}
						
						int x2 = (int) (this.yAxisOffset + (i + 1) + (i + 1)
							* this.verticalBarWidth);
						int offset_labelUnderBin = fm.stringWidth(s_labelUnderBin) / 2;
						if (isWorkableInterval(i))
							g.drawString(s_labelUnderBin, ((x + x2)/2) - offset_labelUnderBin, y + 20 + ySplitOffset);
					}
					else
					{
						g.drawLine(x, y + ySplitOffset, x, y + 5 + ySplitOffset);
					}
				}
				else
				{
					// draw marker
					g.drawLine(x, y + ySplitOffset, x, y + 5 + ySplitOffset);
					// put label between bins
					if (isWorkableInterval(i))
						g.drawString(s, x - offset, y + 20 + ySplitOffset);
				}
			} // for loop over bins
		} // normal fit
		else
		{
			// the boundary labels won't fit the normal way, use rotated
			// font
			g.setFont(rotateFont);
			int y = this.barAreaHeight();
			for (int i = 0; i < binsOnScale.size(); i++)
			{
				int x = (int) (this.yAxisOffset + i + i
					* this.verticalBarWidth);
				g.drawLine(x, y + ySplitOffset, x, y + 5 + ySplitOffset);
				
				// Get the string value (integer or double)
				String s = Statistiek.getStringValue(binsOnScale.get(i).doubleValue());
				int offset = fm.stringWidth(s);
				
				if (this.model.getLabelUnderBin())
				{
					// put label under bin
					if (i < binsOnScale.size() - 1)
					{
						String s_labelUnderBin;
						if (type.equals(AllowedTypes.INTEGER) && ((int) model.getBinWidth()) == 1)
						{
							// Voor gehele getallen met 1 waarde per klasse, 1 getal tonen onder de staaf
							s_labelUnderBin = s;
						}
						else
						{
							s_labelUnderBin = s + "-<" +
								Statistiek.getStringValue(binsOnScale.get(i + 1).doubleValue());
						}
						int offset_labelUnderBin = fm.stringWidth(s_labelUnderBin);
						int widthRotatedLabel = (int) (offset_labelUnderBin * Math.cos(theta));
						int heightRotatedLabel = (int) (offset_labelUnderBin * -Math.sin(theta));
						if (isWorkableInterval(i))
							g.drawString(s_labelUnderBin, 
								(int) (x + 5 + (this.verticalBarWidth/2) - widthRotatedLabel), 
								y + 7 + heightRotatedLabel + 5 + ySplitOffset);
					}
				}
				else
				{
					// put label between bins
					int offset_labelBetweenBins = fm.stringWidth(s);
					int widthRotatedLabel = (int) (offset_labelBetweenBins * Math.cos(theta));
					if (isWorkableInterval(i))
						g.drawString(s, x + 5 - widthRotatedLabel, y + 7 + offset + ySplitOffset);
				}
			}
		} // non-normal fit
	}

	private boolean determineNormalFitForVerticalBars(FontMetrics fm, AllowedTypes type)
	{
		boolean normalFit = true;
		
		ArrayList<Number> binsOnScale = this.getBinsOnScale();

		if (this.model.getLabelUnderBin())
		{
			int marge = 5;
			for (int i = 0; i < binsOnScale.size(); i++)
			{
				String s = Statistiek.getStringValue(binsOnScale.get(i).doubleValue());
				if (i < binsOnScale.size() - 1)
				{
					String s_labelUnderBin;
					
					if (type.equals(AllowedTypes.INTEGER) && ((int) model.getBinWidth()) == 1)
					{
						s_labelUnderBin = s;
					}
					else
					{
						s_labelUnderBin = s + "-<" +
							Statistiek.getStringValue(binsOnScale.get(i + 1).doubleValue());
					}
					
					if (fm.stringWidth(s_labelUnderBin) + marge > getMultipliedBarwidth())//this.verticalBarWidth)
					{
						normalFit = false;
						break;
					}
				}
			}
		}
		else // label between bins
		{
			for (Number d : binsOnScale)
			{
				if (fm.stringWidth(d.toString()) > getMultipliedBarwidth())//this.verticalBarWidth)
				{
					normalFit = false;
					break;
				}
			}
		}
		
		return normalFit;
	}

	private int determineDependentAxisWidth(double scale)
	{
		int width;
		
		if (this.model.hasVerticalBars())
		{
			if (scale == 0)
			{
				width = 7;//14; 14 voor breedte van 2-cijferige y-aswaarden
			}
			else
			{
				width = 5;
				FontMetrics fm = this.getFontMetrics(this.getFont());
	
				int panelHeight = (int) ((this.model.hasVerticalBars() ? this
					.barAreaHeight() : this.barAreaWidth()));
				int base = 1;
				int exp = 0;
				int step = (int) (base * Math.pow(10, exp));
				while (step * 6 * scale < panelHeight)
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
					step = (int) (base * Math.pow(10, exp));
				}
	
				int majorSteps = (int) Math.floor((panelHeight - 0.5 * fm
					.getHeight()) / (step * scale));
	
				// determine the width of the axis labels
	
				for (int i = 0; i < majorSteps + 1; i++)
				{
					String s = new Integer(i * step).toString();
					if (this.model.getPercentage())
					{
						s = s + "%";
					}
					int stringWidth = fm.stringWidth(s);
					if (stringWidth > width)
					{
						width = stringWidth;
					}
				}
			}
		}
		else
		{
			width = 40;
		}

		return width;
	}

	/**
	 * Paint scale on the axis with the amounts or percentages.
	 * 
	 * @param g
	 *            Graphics in which it will be painted
	 * @param scale
	 *            The multiplier used to make sure the bars fill the view
	 */
	private void paintScale(Graphics g, double scale,
		int ySplitOffset)
	{
		if (scale == 0)
		{
			return;
		}
		
		g.setFont(super.getFont());
		g.setColor(Color.BLACK);
		FontMetrics fm = g.getFontMetrics();

		// Determine the interval for markers on the axis
		int panelHeight = (int) ((this.model.hasVerticalBars() ? this
			.barAreaHeight() : this.barAreaWidth()));
		int base = 1;
		int exp = 0;
		int step = (int) (base * Math.pow(10, exp));
		while (step * 6 * scale < panelHeight) // hoezo 6?
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
			step = (int) (base * Math.pow(10, exp));
		}

		int minorStep;
		int minorStepsPerMajorStep;
		switch (base)
		{
    		case 5:
    			minorStep = (int) Math.pow(10, exp);
    			minorStepsPerMajorStep = 5;
    			break;
    		case 2:
    			minorStep = (int) (5 * Math.pow(10, exp - 1));
    			minorStepsPerMajorStep = 4;
    			break;
    		case 1:
    			minorStep = (int) (2 * Math.pow(10, exp - 1));
    			minorStepsPerMajorStep = 5;
    			break;
    		default:
    			minorStep = 1;
    			minorStepsPerMajorStep = step;
		}

		double majorSteps = (panelHeight - 0.5 * fm.getHeight())
			/ (step * scale);
		int majorStepsFloor = (int) Math.floor(majorSteps);

		if (this.model.hasVerticalBars())
		{
			// Paint the small markers
			for (int i = 0; i < majorSteps * minorStepsPerMajorStep; i++)
			{
				int y = this.barAreaHeight()
					- (int) (i * minorStep * scale);
				g.setColor(new Color(240, 240, 240));
				g.drawLine(this.yAxisOffset, y + ySplitOffset, this.getWidth(),
					y + ySplitOffset);
				g.setColor(Color.black);
				g.drawLine(this.yAxisOffset - 2, y + ySplitOffset,
					this.yAxisOffset, y + ySplitOffset);
			}

			// paint the large markers with their value
			for (int i = 0; i < majorStepsFloor + 1; i++)
			{
				int y = this.barAreaHeight() - (int) (i * step * scale);
				g.setColor(new Color(220, 220, 220));
				g.drawLine(this.yAxisOffset, y + ySplitOffset, this.getWidth(),
					y + ySplitOffset);
				g.setColor(Color.black);
				g.drawLine(this.yAxisOffset - 5, y + ySplitOffset,
					this.yAxisOffset, y + ySplitOffset);
				String s = new Integer(i * step).toString();
				if (this.model.getPercentage())
				{
					s = s + "%";
				}
				g.drawString(s, this.yAxisOffset - 7 - fm.stringWidth(s), y
					+ (int) (fm.getHeight() / 2.0) - 2 + ySplitOffset);
			}
		}
		else
		{
			int y = this.barAreaHeight();

			// paint the small markers
			for (int i = 0; i < majorStepsFloor * minorStepsPerMajorStep; i++)
			{
				int x = this.yAxisOffset + (int) (i * minorStep * scale)
					- 1;
				g.drawLine(x, y + ySplitOffset, x, y + 2 + ySplitOffset);
			}

			// paint the large markers with their value
			for (int i = 0; i < majorStepsFloor + 1; i++)
			{
				int x = this.yAxisOffset + (int) (i * step * scale) - 1;
				g.drawLine(x, y + ySplitOffset, x, y + 5 + ySplitOffset);
				String s = new Integer(i * step).toString();
				if (this.model.getPercentage())
				{
					s = s + "%";
				}
				g.drawString(s, x - (int) (fm.stringWidth(s) / 2.0),
					y + 7 + fm.getHeight() + ySplitOffset);
			}
		}
	}

	/**
	 * paint the bars for enum or string data
	 * 
	 * @param g
	 *            The graphics in which the bars will be painted
	 */
	private void paintEnumClass(Graphics2D g,
		FrequencyTuple[][] allFrequencies, int splitClass)
	{
		double availableSpace;
		double maxValueOnAxis;
		double scale;
		double amountScale;

		g.setFont(super.getFont());
		g.setColor(Color.BLACK);
		FontMetrics fm = g.getFontMetrics();
		AffineTransform at = new AffineTransform();
		double theta = Math.PI * 1.75;
		at.rotate(theta); // 315 graden met de klok mee; 45 graden tegen de klok in
		Font rotateFont = super.getFont().deriveFont(at);

		// get frequencies
		FrequencyTuple[] frequencies = allFrequencies[splitClass];
		int ySplitOffset = splitClass * (this.scrollPane.getHeight() - 5);

		availableSpace = HistogramView.MAX_SCREEN_FRACTION_FOR_BARS *
        	(this.model.hasVerticalBars() ? 
        		this.barAreaHeight() : 
        		this.barAreaWidth()); // if vertical then availableSpace represents height, if horizontal then width
		
		// determine max on scale
		double max = this.maxFrequencyOverAllSplits(allFrequencies);
		if (this.model.getPercentage()
			&& this.model.getPercentageSplitTotal()) // split total is 100%
		{
			maxValueOnAxis = this.maxPercentageOverAllSplits(allFrequencies);
		}
		else if (this.model.getPercentage()) // end total is 100%
		{
			maxValueOnAxis = 100.0 * max / (this.getFrequenciesSum(allFrequencies)); 
		}
		else
		{
			maxValueOnAxis = max;
		}
		
		// determine scale for the axis with percentages or amounts; should be the same for splits in multiple views
		if (maxValueOnAxis == 0)
		{
			scale = 0;
		}
		else
		{
			scale =  availableSpace/maxValueOnAxis;
		}

		// set bar width
		this.setBarWidth(frequencies.length);

		if (this.model.hasVerticalBars())
		{
			this.yAxisOffset = this.determineDependentAxisWidth(scale) + 10 + fm.getHeight();

			// check if the bin boundary strings will fit
			boolean normalFit = true;
			int longest = 0;
			for (FrequencyTuple ft : frequencies)
			{
				int width = fm.stringWidth(ft.label);
				if (width > this.verticalBarWidth)
				{
					normalFit = false;
				}
				if (width > longest)
				{
					longest = width;
				}
			}

			if (normalFit)
			{
				this.xAxisOffset = 40;
			}
			else
			{
				this.xAxisOffset = longest + 5 + fm.getHeight();
			}
		}
		else
		{ // horizontal bars
			this.xAxisOffset = this.determineDependentAxisWidth(scale);

			// find longest binboundary
			int longest = 0;
			for (FrequencyTuple ft : frequencies)
			{
				int width = fm.stringWidth(ft.label);
				if (width > longest)
				{
					longest = width;
				}
			}

			this.yAxisOffset = longest + 5 + fm.getHeight();
		}

		// ---- correct scales ---- 
		// bepaal de amountscale om de bar-lengtes per split te kunnen berekenen
		if (this.model.getPercentage())
		{
			if ((this.model.getStatTableModel().numberOfSplitVarClasses(this.model.getSplitOptions()) > 1)
			&& (this.model.isSplitInSingleView()))
			{
				if (this.model.isFrequencyPolygonMode()
					&& this.model.isFrequencyPolygonCumulativeMode())
				{
					maxValueOnAxis = 100.0;
				}
				else
				{
					int maxInSplit = 0;
					double maxFraction = 0;
					for (int split = 0; split < allFrequencies.length; split++)
					{
						maxInSplit = this.maxFrequency(allFrequencies, split);
						max = Math.max(max, maxInSplit);
						maxFraction = Math.max(
							(double) maxInSplit
								/ this.getFrequenciesSum(allFrequencies),
							maxFraction);
					}
	
					maxValueOnAxis = 100.0 * maxFraction;
				}
	
				scale = availableSpace/maxValueOnAxis;
			} // split in single view
			else
			{ // split in multiple views
				if (this.model.getPercentageSplitTotal())
				{
					// amountScale = de beschikbare ruimte (die toont maxValueOnScale) gedeeld door de frequentie (in de huidige splitClass) die deze maxValueOnScale-waarde geeft.
					// Let op: voor split total = 100% is max dus de frequentie (in de huidige splitClass) die deze maxValueOnScale-waarde geeft
					// maar dat is niet perse een frequentie die voorkomt in de huidige splitClass. Het is daarom een decimale waarde.
					max = maxValueOnAxis * this.getFrequenciesSum(allFrequencies, splitClass) / 100;
				}
			}
		} // percentage
		else
		{ // aantallen
			if (this.model.getStatTableModel().numberOfSplitVarClasses(
				this.model.getSplitOptions()) > 1)
			{
				// er is een split
				if (this.model.isSplitInSingleView())
				{
					int maxInSplit = 0;
					for (int split = 0; split < allFrequencies.length; split++)
					{
						maxInSplit = Math.max(maxInSplit,
							this.maxFrequency(allFrequencies, split));
						max = Math.max(max, maxInSplit);
					}

					maxValueOnAxis = maxInSplit;
					max = maxInSplit;

					if (maxValueOnAxis == 0)
					{
						scale = 0;
					}
					else
					{
						scale = availableSpace / maxValueOnAxis;
					}
				} // split in single view
			}
		}

		// PAINT AMOUNT SCALE
		this.paintScale(g, scale, ySplitOffset);

		// PAINT BARS
		// use amountScale to paint the correct bar length
		amountScale = availableSpace/max;

		ArrayList<Color> splitColors = new ArrayList<Color>();
		ArrayList<String> splitLabels = new ArrayList<String>();

		if (this.model.isSplitInSingleView()
			&& this.model.getStatTableModel().numberOfSplitVarClasses(
				this.model.getSplitOptions()) > 1)
		{
			if (this.model.isFrequencyPolygonMode()
				&& this.model.isFrequencyPolygonCumulativeMode()
				&& this.model.isFrequencyPolygonStackMode())
			{
				int[] totalFrequencies = new int[frequencies.length];
				int[] frequencySum = new int[frequencies.length];
				for (int split = 0; split < allFrequencies.length; split++)
				{
					FrequencyTuple[] splitFreq = allFrequencies[split];
					this.lastPolygonPoint = null;
					Color c = this.getColor(split);
					for (int i = 0; i < splitFreq.length; i++)
					{
						if (i > 0)
						{
							frequencySum[i] = frequencySum[i - 1]
								+ splitFreq[i].frequency;
						}
						else
						{
							frequencySum[i] = splitFreq[i].frequency;
						}

						this.fillCumulativeFreqPolygonSegment(
							g,
							(int) Math.round(amountScale
								* (totalFrequencies[i] + frequencySum[i])),
							(int) Math.round(amountScale * totalFrequencies[i]),
							(int) Math.round(amountScale
								* (i > 0 ? frequencySum[i - 1]
									+ totalFrequencies[i - 1] : 0)),
							(int) Math.round(amountScale
								* (i > 0 ? totalFrequencies[i - 1] : 0)), i, c);
					}
					for (int i = 0; i < frequencySum.length; i++)
					{
						totalFrequencies[i] += frequencySum[i];
					}
					//System.out.println(Arrays.toString(totalFrequencies));
				}
			} // frequentiepolygoon, cumulatief, stapelen
			else
			{
				// frequentiepolygoon, cumulatief, split in single view
				if (this.model.isFrequencyPolygonMode()
					&& this.model.isFrequencyPolygonCumulativeMode())
				{
					// Paint the frequency polygon as dots and lines 
					for (int split = 0; split < allFrequencies.length; split++)
					{
						if (this.model.getPercentage())
						{
							amountScale = availableSpace/this.maxFrequency(allFrequencies[split]);
						}
						else
						{ // aantal
							// bij aantal moet hij de max frequency over alle splits nemen
							amountScale = availableSpace/this.maxFrequencyOverAllSplits(allFrequencies);
						}
						
						//g.setComposite(this.makeComposite(split + 1)); // test syl: wat doet dit?
						FrequencyTuple[] splitFreq = allFrequencies[split];
						int frequencySum = 0;
						int frequencySelectedSum = 0;
						Color c = this.getColor(split);

						for (int i = 0; i < splitFreq.length; i++)
						{
							frequencySum += splitFreq[i].frequency;
							frequencySelectedSum = splitFreq[i].selectionFrequency;
							this.paintBar(
								g,
								(int) (frequencySum * amountScale),
								(int) (frequencySelectedSum * amountScale),
								i, 0, 0, c, split,
								allFrequencies.length, true, split);
						}
						this.lastPolygonPoint = null;
					}
					g.setComposite(this.makeComposite(1));
				}
				else
				{
					int[] cumHeight = new int[frequencies.length];
					for (int split = 0; split < allFrequencies.length; split++)
					{
						FrequencyTuple[] splitFreq = allFrequencies[split];
						if (!this.model.isFrequencyPolygonMode())
						{
							// g.setComposite(this.makeComposite(split+1));
						}
						Color c = this.getColor(split);
						for (int i = 0; i < frequencies.length; i++)
						{
							if (this.model.hasVerticalBars())
							{
								if (this.isNextToEachOtherSelected())
								{
									this.paintBar(
										g,
										(int) (splitFreq[i].frequency * amountScale),
										(int) (splitFreq[i].selectionFrequency * amountScale),
										i, 0, 0, c, split,
										allFrequencies.length, true, split); // splitClass is altijd 0
								}
								else
								{
									this.paintBar(
										g,
										(int) (splitFreq[i].frequency * amountScale),
										(int) (splitFreq[i].selectionFrequency * amountScale),
										i, -cumHeight[i], 0, c, 0, 0, false, split); // splitClass is altijd 0
								}
							}
							else
							{
								if (this.isNextToEachOtherSelected())
								{
									this.paintBar(
										g,
										(int) (splitFreq[i].frequency * amountScale),
										(int) (splitFreq[i].selectionFrequency * amountScale),
										i, 0, 0, c, split,
										allFrequencies.length, true, split);
								}
								else
								{
									this.paintBar(
										g,
										(int) (splitFreq[i].frequency * amountScale),
										(int) (splitFreq[i].selectionFrequency * amountScale),
										i, 0, cumHeight[i], c, 0, 0, false, split);
								}
							}
							cumHeight[i] += (int) (splitFreq[i].frequency * amountScale);

						}
						this.lastPolygonPoint = null;
					}
					g.setComposite(this.makeComposite(1));
				}
			}
		} // split in single view
		else
		{ // no split or split in multiple views
			if (this.model.isFrequencyPolygonMode()
				&& this.model.isFrequencyPolygonCumulativeMode())
			{
				if (this.model.getStatTableModel().numberOfSplitVarClasses(
					this.model.getSplitOptions()) > 1)
					{
						// split in multiple views
						Color c = this.getColor(splitClass);
						splitColors.add(c);
						splitLabels.add(this.model.getSplitOptions()
							.getSplitClassLabel(splitClass,
								this.model.getStatTableModel()));

						int frequencySum = 0;
						int frequencySelectedSum = 0;
						for (int i = 0; i < frequencies.length; i++)
						{
							frequencySum += frequencies[i].frequency;
							frequencySelectedSum = frequencies[i].selectionFrequency;
							this.paintBar(
								g,
								(int) (frequencySum * amountScale),
								(int) (frequencySelectedSum * amountScale),
								i, ySplitOffset, 0, c, splitClass,
								allFrequencies.length, true, splitClass);
						}
					}
					else // no split
					{
						int frequencySum = 0;
						int frequencySelectedSum = 0;
						for (int i = 0; i < frequencies.length; i++)
						{
							frequencySum += frequencies[i].frequency;
							frequencySelectedSum = frequencies[i].selectionFrequency;
							this.paintBar(
								g, 
								(int) (frequencySum * amountScale),
								(int) (frequencySelectedSum * amountScale), 
								i, ySplitOffset, 0, splitClass);
						}
					}

			}
			else
			{
				for (int i = 0; i < frequencies.length; i++)
				{
					if (allFrequencies.length > 1)
						this.paintBar(
							g,
							(int) (frequencies[i].frequency * amountScale),
							(int) (frequencies[i].selectionFrequency * amountScale),
							i, ySplitOffset, 0, this.getColor(splitClass), 0,
							0, false, splitClass);
					else
						this.paintBar(
							g,
							(int) (frequencies[i].frequency * amountScale),
							(int) (frequencies[i].selectionFrequency * amountScale),
							i, ySplitOffset, 0, splitClass);
				}
			}
		}

		// paint bar labels
		g.setColor(Color.BLACK);
		if (this.model.hasVerticalBars())
		{
			int y = this.barAreaHeight() - 2;
			boolean normalFit = true;
			for (FrequencyTuple ft : frequencies)
			{
				int width = fm.stringWidth(ft.label);
				if (width > this.verticalBarWidth)
				{
					normalFit = false;
				}
			}

			if (normalFit)
			{
				for (int i = 0; i < frequencies.length; i++)
				{
					int x = this.yAxisOffset
						+ (int) (((double) i + 0.5) * this.verticalBarWidth)
						+ i + 1;

					String s = frequencies[i].label;
					g.drawString(s, x - (int) (fm.stringWidth(s) / 2.0), y + 5
						+ fm.getHeight() + ySplitOffset);
				}
			} // normal fit
			else
			{
				// too wide, so rotate labels
				g.setFont(rotateFont);
				for (int i = 0; i < frequencies.length; i++)
				{
					int x = this.yAxisOffset
						+ (int) (((double) i + 0.5) * this.verticalBarWidth)
						+ i + 1;
					String s = frequencies[i].label;
					int offset_labelUnderBin = fm.stringWidth(s);
					int widthRotatedLabel = (int) (offset_labelUnderBin * Math.cos(theta));
					int heightRotatedLabel = (int) (offset_labelUnderBin * -Math.sin(theta));
					g.drawString(s, 
						x - widthRotatedLabel,
						this.barAreaHeight() + 15 + heightRotatedLabel + ySplitOffset);
				}
			}
		} // vertical bars
		else
		{ // horizontal bars
			for (int i = 0; i < frequencies.length; i++)
			{
				int y = i + (int) ((i + 1) * this.horizontalBarWidth);

				// check if the label will fit on the screen
				String s = frequencies[i].label;
				if (fm.stringWidth(s) > this.yAxisOffset - 10)
				{
					// cut off the label to make it fit
					s = s + "...";
					while (fm.stringWidth(s) > this.yAxisOffset - 5
						- fm.getHeight()
						&& s.length() > 4)
					{
						s = s.substring(0, s.length() - 4) + "...";
					}
				}

				g.drawString(s, this.yAxisOffset - fm.stringWidth(s) - 3,
					(int) (y + (0.5 * fm.getHeight())) + ySplitOffset);
			}
		}
	}
	
	private int getTotalSumOfFrequencies(FrequencyTuple[][] allFrequencies)
	{
		int sum = 0;
		
		for (FrequencyTuple[] splitFreq : allFrequencies)
		{
			sum = sum + this.tupleArraySum(splitFreq);
		}

		return sum;
	}

	private int getTotalSumOfFrequencies(int[][] allFrequencies)
	{
		int sum = 0;
		
		for (int i = 0; i < allFrequencies.length; i++)
		{
			sum = sum + arrayEvenSum(allFrequencies[i]);
		}

		return sum;
	}

	private int getMaxFrequenciesofBins(FrequencyTuple[][] allFrequencies)
	{
		int max = 0;
		
		for (int i = 0; i < allFrequencies.length; i++)
		{
			max = Math.max(max, this.maxFrequency(allFrequencies, i));
		}
		
		return max;
	}
	
	private int getMaxFrequencyofBins(int[][] allFrequencies)
	{
		int max = 0;
		
		for (int i = 0; i < allFrequencies.length; i++)
		{
			max = Math.max(max, this.maxFrequency(allFrequencies, i));
		}
		
		return max;
	}

	private void setMainPanelSize()
	{
		int splitClasses = this.model.getStatTableModel().numberOfSplitVarClasses(
			this.model.getSplitOptions());
		int colorLegendWidth = this.colorLegend.isVisible() ? this.colorLegend
			.getPreferredSize().width : 0;
		if (this.model.isSplitInSingleView())
		{
			this.mainPanel.setPreferredSize(new Dimension(this.scrollPane
				.getWidth() - colorLegendWidth - 20, this.scrollPane
				.getHeight() - 5));
		}
		else
		{
			if (this.scrollPane.getWidth() == 0)
			{
				// even hardcoded op de gebruikelijke maat... Hoe komt scrollPane 0x0?
				this.mainPanel.setPreferredSize(new Dimension(653, 677));
			}
			else
			{
    			this.mainPanel.setPreferredSize(new Dimension(this.scrollPane
    				.getWidth() - colorLegendWidth - 20, splitClasses
    				* (this.scrollPane.getHeight() - 5) + 1));
			}
		}
	}

	// Override setBound
	public void setBounds(int x, int y, int w, int h)
	{
		super.setBounds(x, y, w, h);

		this.setMainPanelSize();
	}

	// Implements Observer
	public void update(Observable arg0, Object arg1)
	{
		// first update useroptionspanel, so that binWidth fields etc can be read
		userOptionsPanel.update();

		// voor het geval de data columnIndex of splitColumnIndex
		if (this.model.columnIndexValid() 
			&& !this.model.getStatTableModel().isEmptyColumn(this.model.getColumnIndex()))
		{
			this.recalculateBinBoundaries(this.model.getColumnIndex(), false);
		}
		
		if (this.model.columnSplitIndexValid() && this.model.getSplitOptions().getBinBoundaries() != null)
		{
			this.recalculateSplitBinBoundaries(this.model.getSplitOptions().getColumnSplitIndex());
		}
		
		this.dialogButton.setVisible(this.model.getStatTableModel()
			.isViewsEditable());

		if (this.updateColorLegend() || true)
		{
			this.setMainPanelSize();
			this.scrollPane.setViewportView(mainPanel);
		}

		// Revalidate the mainPanel to reset the scrollbar
		this.mainPanel.revalidate();
		
		this.repaint();
	}

	/**
	 * Updates the color legend
	 * 
	 * @return true if the visibility of the color legend changed
	 */
	private boolean updateColorLegend()
	{
		int splitClasses = this.model.getStatTableModel().numberOfSplitVarClasses(
			this.model.getSplitOptions());
		if (splitClasses > 1 && this.model.isSplitInSingleView())
		{
			this.colorLegend.setColumnString(this.model.getStatTableModel()
				.getColumnName(
					this.model.getSplitOptions().getColumnSplitIndex()));
			ArrayList<String> splitStrings = new ArrayList<String>(splitClasses);
			ArrayList<Color> splitColors = new ArrayList<Color>(splitClasses);
			for (int i = 0; i < splitClasses; i++)
			{
				splitStrings.add(this.model.getSplitOptions()
					.getSplitClassLabel(i, this.model.getStatTableModel()));
				splitColors.add(this.getColor(i));
			}
			this.colorLegend.setColors(splitStrings, splitColors);
			if (!this.colorLegend.isVisible())
			{
				this.colorLegend.setVisible(true);
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			// bij geen split of split in 1 view geen colorlegend tonen
			if (this.colorLegend.isVisible())
			{
				//System.out.println("HistogramView.updateColorLegend(): colorLegend.isVisible() en nu FALSE");

				this.colorLegend.setVisible(false);
				return true;
			}
			else
			{
				return false;
			}
		}
	}

	/**
	 * Recalculate the bin boundaries for column with columnIndex
	 * if possible.
	 * 
	 * @param columnIndex
	 * 		The index of the column for which the bin
	 *      boundaries will be calculated.
	 * @param typeHasChanged
	 * 		The type has changed yes/no.
	 */
	public void recalculateBinBoundaries(int columnIndex, boolean typeHasChanged)
	{
			if (this.model.columnIndexValid())
			{
				ArrayList<ColumnType> list = this.model.getStatTableModel().getColumnTypes();
				ArrayList<Number> binBoundaries = null;
				if (list.get(this.model.getColumnIndex()).getType().isNumber())
				{
    				binBoundaries = Statistiek.appropriateBoundariesFromBinSettings(
    					this.model.getStatTableModel().getColumnMin(this.model.getColumnIndex()),
    					this.model.getStatTableModel().getColumnMax(this.model.getColumnIndex()), 
    					this.getBinWidth(), this.getMinBoundary()); // met invoervelden // hier is min 160!
    				
    				if ((binBoundaries == null) || binBoundaries.size() == 0) // ongeldige waarden in invoervelden
    				{
        				binBoundaries = Statistiek.appropriateBoundaries(
    						this.model.getStatTableModel().getColumnMin(this.model.getColumnIndex()),
    						this.model.getStatTableModel().getColumnMax(this.model.getColumnIndex()),
    						this.model.getNoBins());
        				
        				int bin0Decimals = Statistiek.getNumberOfDecimals(binBoundaries.get(0).toString());
        				int bin1Decimals = Statistiek.getNumberOfDecimals(binBoundaries.get(1).toString());
        				int maxNumberOfDecimals = Math.max(bin0Decimals, bin1Decimals);

        				binBoundaries = Statistiek.appropriateBoundariesFromBinSettings(
        					this.model.getStatTableModel().getColumnMin(this.model.getColumnIndex()),
        					this.model.getStatTableModel().getColumnMax(this.model.getColumnIndex()), 
        					Statistiek.round(binBoundaries.get(1).doubleValue() - binBoundaries.get(0).doubleValue(), maxNumberOfDecimals), binBoundaries.get(0).doubleValue());
    				}
    				
    				if (binBoundaries == null)
    				{
    					System.out.println("HistogramView.recalculateBinBoundaries(columnIndex = " + columnIndex + ", typeHasChanged = " + typeHasChanged + ")");
    				}
    				else
    				{
    					this.model.setBinBoundariesWithoutEvent(binBoundaries);
    					this.model.setNoBinsWithoutEvent(binBoundaries.size() - 1);
    					
    					// if optimize scale update minOnScale and maxOnScale if necessary
    					if (this.model.isOptimizeScale())
    					{
	    					if (this.model.getMinOnScale() > binBoundaries.get(0).doubleValue())
	    					{
	    						this.model.setMinOnScale(binBoundaries.get(0).doubleValue());
	    					}
	    					int last = binBoundaries.size() - 1;
	    					if (this.model.getMaxOnScale() <= binBoundaries.get(last).doubleValue())
	    					{
	    						this.model.setMaxOnScale(binBoundaries.get(last).doubleValue());
	    					}
    					}
    				}
				} // type is number

				if (typeHasChanged)
				{
					// set bin label positioning
					AllowedTypes type = list.get(this.model.getColumnIndex()).getType(); 
					if (type.equals(AllowedTypes.INTEGER))
					{
						this.model.setLabelUnderBinWithoutEvent(true);
					}
					else if (type.equals(AllowedTypes.DOUBLE))
					{
						this.model.setLabelUnderBinWithoutEvent(false);
					}
				}
			}
	}

	/**
	 * Recalculate the split bin boundaries for column with columnSplitIndex
	 * if possible.
	 * 
	 * @param columnIndex
	 * 		The index of the column for which the bin
	 *      boundaries will be calculated.
	 * @param typeHasChanged
	 * 		The type has changed yes/no.
	 */
	public void recalculateSplitBinBoundaries(int columnSplitIndex)
	{
		AllowedTypes splitType = this.model.getStatTableModel().getColumnTypes().get(columnSplitIndex).getType();
		if (splitType.isNumber())
		{
			ArrayList<Number> boundaries = new ArrayList<Number>();

			boundaries = Statistiek.appropriateBoundariesFromBinSettings(
				this.model.getStatTableModel().getColumnMin(
					this.model.getSplitOptions().getColumnSplitIndex()),
				this.model.getStatTableModel().getColumnMax(
					this.model.getSplitOptions().getColumnSplitIndex()),
				this.getSplitBinWidth(),
				this.getSplitMinBoundary());

			this.model.setSplitBoundariesWithoutEvent(boundaries);
			this.model.setSplitOptionsWithoutEvent(this.model.getSplitOptions());
		}
	}

	private static int identityHashCode(Object o)
	{
		return System.identityHashCode(o);
	}
	
	/**
	 * Return true if the view has a split, else false.
	 * 
	 * @return whether the view has a split
	 */
	public boolean hasSplit()
	{
		boolean hasSplit = false;
		
		if (this.model.getSplitOptions().getColumnSplitIndex() > -1)
			hasSplit = true;
		
		return hasSplit;
	}
	
	public HistogramUserOptionsPanel getUserOptionsPanel()
	{
		return this.userOptionsPanel;
	}
	
	private class HistogramBarPanel extends JPanel implements MouseMotionListener
	{
		public void paintComponent(Graphics g)
		{
			Graphics2D g2D = (Graphics2D) g;
			g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

			// clear panel
			g2D.clearRect(0, 0, this.getWidth(), this.getHeight());
			
//			System.out.println("HistogramBarPanel.paintComponent(): this.w = " +
//				this.getWidth() + ", h = " + this.getHeight());

			HistogramView.this.lastPolygonPoint = null;

			// clear locations of bars
			HistogramView.this.barRectangles = new ArrayList<Rectangle>(
				HistogramView.this.model.getNoBins()
					* HistogramView.this.model.getStatTableModel().numberOfSplitVarClasses(
						HistogramView.this.model.getSplitOptions()));

			if (!HistogramView.this.model.columnIndexValid())
			{
				return;
			}

			// get the data type
			AllowedTypes type = HistogramView.this.model.getStatTableModel()
				.getColumnTypes()
				.get(HistogramView.this.model.getColumnIndex()).getType();

			int numberOfSplitClasses = HistogramView.this.model.getStatTableModel()
				.numberOfSplitVarClasses(HistogramView.this.model.getSplitOptions());

			if (HistogramView.this.model.isSplitInSingleView())
			{
				if (type.isNumber())
				{
					int[][] frequencies;
					if (model.isOptimizeScale())
					{
						frequencies = HistogramView.this.model.numberClassFrequency();
					}
					else
					{
						frequencies = HistogramView.this.model.numberClassFrequencyFromScaleSettings();
					}
					if (frequencies != null)
					{
						HistogramView.this.paintNumberClass(g2D, frequencies, 0);
					}
				}
				else
				{
					FrequencyTuple[][] frequencies = HistogramView.this.model
						.enumClassFrequency();
					HistogramView.this.paintEnumClass(g2D, frequencies, 0);
				}
			} // (split in) single view
			else
			{
				// call the right paint method
				if (type.equals(AllowedTypes.ENUM)
					|| type.equals(AllowedTypes.STRING))
				{
					FrequencyTuple[][] frequencies = HistogramView.this.model
						.enumClassFrequency();
					for (int splitClass = 0; splitClass < numberOfSplitClasses; splitClass++)
					{
						HistogramView.this.lastPolygonPoint = null;
						HistogramView.this.paintEnumClass(g2D, frequencies,
							splitClass);
					}
				}
				else
				{
					int[][] frequencies;
					
					if (model.isOptimizeScale())
					{
						frequencies = HistogramView.this.model.numberClassFrequency();
					}
					else
					{
						frequencies = HistogramView.this.model.numberClassFrequencyFromScaleSettings();
					}

					for (int splitClass = 0; splitClass < numberOfSplitClasses; splitClass++)
					{
						HistogramView.this.lastPolygonPoint = null;
						HistogramView.this.paintNumberClass(g2D, frequencies,
							splitClass);
					}
				}
			}

			// draw the bottom line
			for (int i = 0; i < (HistogramView.this.isSplitSingleViewSelected() ? 1
				: numberOfSplitClasses); i++)
			{
				int ySplitOffset = i
					* (HistogramView.this.scrollPane.getHeight() - 5);

				if (HistogramView.this.model.hasVerticalBars())
				{
					g2D.drawLine(HistogramView.this.yAxisOffset,
						HistogramView.this.barAreaHeight() + ySplitOffset,
						super.getWidth(), HistogramView.this.barAreaHeight()
							+ ySplitOffset);
				}
				else
				{
					g2D.drawLine(HistogramView.this.yAxisOffset - 1,
						ySplitOffset - 1, HistogramView.this.yAxisOffset - 1,
						HistogramView.this.barAreaHeight() + ySplitOffset);
				}
				HistogramView.this.paintAxisLabels(g2D, ySplitOffset, i);
			}
		}

		@Override
		public void mouseDragged(MouseEvent e)
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseMoved(MouseEvent me)
		{
			// Method mouseMoved() implements showing tooltip & highlight
			
			Point p = me.getPoint();
			FrequencyTuple[][] frequencies_enum = HistogramView.this.model
				.enumClassFrequency();
			int[][] frequencies_number;
			if (model.isOptimizeScale())
			{
				frequencies_number = HistogramView.this.model.numberClassFrequency();
			}
			else
			{
				frequencies_number = HistogramView.this.model.numberClassFrequencyFromScaleSettings();
			}

			boolean isPercentage = HistogramView.this.model.getPercentage();
			int[] aantalPerSplit = null;
			int[] aantalPerBin = null;
			int aantal_totaal = 0;
			boolean found = false;

//    			System.out.println("HistogramBarPanel.mouseMoved(): (" + p.x
//    				+ ", " + p.y + "): frequencies=" + frequencies_number +
//    				", barRectangles=" + HistogramView.this.barRectangles);

			ToolTipManager.sharedInstance().setInitialDelay(0);
			ToolTipManager.sharedInstance().setReshowDelay(0);

			int noBins = 0;
			int numberOfSplits = HistogramView.this.model.getStatTableModel()
				.numberOfSplitVarClasses(HistogramView.this.model.getSplitOptions());

			if (frequencies_number != null)
			{
				if (model.isOptimizeScale())
				{
					noBins = HistogramView.this.model.getNoBins();
				}
				else
				{
					noBins = getNumberOfBinsfromBinsSettings();
				}
			}
			else if (frequencies_enum != null)
				noBins = frequencies_enum[0].length;

			aantalPerSplit = new int[numberOfSplits];
			aantalPerBin = new int[noBins];

			if (isPercentage)
			{
				if (frequencies_number != null) // number variable
				{
					aantal_totaal = HistogramView.this.getFrequenciesSum(frequencies_number);
				}
				else if (frequencies_enum != null) // enum variable
				{
					aantal_totaal = HistogramView.this.getFrequenciesSum(frequencies_enum);
				}
				
				// bepaal aantal per bin
				for (int j = 0; j < noBins; j++)
				{
					// tel de aantallen op voor bin j
					
					aantalPerBin[j] = 0;
					if (frequencies_number != null) // number variable
					{
    					for (int i = 0; i < numberOfSplits; i++)
    					{
    						aantalPerBin[j] = aantalPerBin[j]
    							+ frequencies_number[i][j * 2];
    					}
					}
					else if (frequencies_enum != null) // enum variable
					{
						for (int i = 0; i < numberOfSplits; i++)
						{
    						aantalPerBin[j] = aantalPerBin[j]
								+ frequencies_enum[i][j].frequency;
						}
					}
//						System.out.println("... aantalPerBin[" + j + "] = " +
//							aantalPerBin[j]);
				}
				
				// bepaal aantal per split
				for (int i = 0; i < numberOfSplits; i++)
				{
					if (frequencies_number != null) // number variable
					{
    					for (int j = 0; j < noBins; j++)
    					{
    						aantalPerSplit[i] = aantalPerSplit[i]
    							+ frequencies_number[i][j * 2];
    					}
					}
					else if (frequencies_enum != null) // enum variable
					{
						for (int j = 0; j < noBins; j++)
						{
    						aantalPerSplit[i] = aantalPerSplit[i]
								+ frequencies_enum[i][j].frequency;
						}
					}    					
//    					System.out.println("... aantalPerSplit[" + i + "] = " +
//    						aantalPerSplit[i]);
				}
			} // percentage
			
			//System.out.println("HistogramView.HistogramBarPanel.mouseMoved(): aantal_totaal = " + aantal_totaal);

			Rectangle rect;
			double value = 0;
			double[] cumulativeValuePerSplit = new double[numberOfSplits];
			for (int i = 0; i < numberOfSplits && !found; i++)
			{
				for (int j = 0; j < noBins && !found; j++) // j <
														   // HistogramView.this.barRectangles.size()
				{
					rect = HistogramView.this.barRectangles.get(j + i * noBins);
					
					if (HistogramView.this.model.isFrequencyPolygonCumulativeMode())
					{
						// calculate value and add to cumulativeValue
						if (isPercentage)
						{
							if (frequencies_number != null)
							{
								if (HistogramView.this.hasSplit())
								{
									if (HistogramView.this.model.getPercentageSplitTotal())
										value = ((double) frequencies_number[i][j * 2] / aantalPerSplit[i]) * 100;
									else
										value = ((double) frequencies_number[i][j * 2] / aantal_totaal) * 100;
								}
								else // geen split
								{
    								// percentage t.o.v. totaal
        							value = ((double) frequencies_number[i][j * 2] / aantal_totaal) * 100; 
								}
							}
							else if (frequencies_enum != null)
							{
								if (HistogramView.this.hasSplit())
								{
            						value = ((double) frequencies_enum[i][j].frequency / aantalPerSplit[i]) * 100;
								}
								else // geen split
								{
    								// percentage berekenen t.o.v. totaal
        							value = ((double) frequencies_enum[i][j].frequency / aantal_totaal) * 100;
								}
							}
							
							if (Double.isNaN(value) || Double.isInfinite(value))
								value = 0;
						} // isPercentage
						else
						{
							if (frequencies_number != null)
							{
								value = frequencies_number[i][j * 2];
							}
							else if (frequencies_enum != null)
							{
								value = frequencies_enum[i][j].frequency;
							}
						}
						cumulativeValuePerSplit[i] += value;
					} // isFrequencyPolygonCumulativeMode()

					if (isOverToolTipArea(p, rect))
					{
						if (isPercentage)
						{
							value = 0;
							
							if (frequencies_number != null)
							{
								if (HistogramView.this.hasSplit())
								{
									if (HistogramView.this.model.isSplitInSingleView())
        							{
										if (HistogramView.this.isNextToEachOtherSelected())
    									{
    										// als naast elkaar, dan percentage relatief aan totaalaantal
    										value = ((double) frequencies_number[i][j*2] / aantal_totaal) * 100;
    									}
    									else
    									{
            								// als gestapeld in 1 view, dan percentage relatief aan totaal per bin 
                							//value = ((double) frequencies_number[i][j * 2] / aantalPerBin[j]) * 100;
    										// percentage relatief aan totaalaantal
    										//value = ((double) frequencies_number[i][j*2] / aantal_totaal) * 100;
    										
    										// bij gestapeld moet de waarde relatief aan totaal 
    										// en tooltip de waarde aan de as
    										for (int k = 0; k <= i; k++)
    										{
    											value = value + ((double) frequencies_number[k][j*2] / aantal_totaal) * 100;
    										}
    									}
        							}
        							else if (HistogramView.this.model.getPercentageSplitTotal())
        							{ 
        								// split in meerdere views en percentage per split relatief aan split
            							value = ((double) frequencies_number[i][j * 2] / aantalPerSplit[i]) * 100;
        							}
        							else
        							{
        								// split met meerdere views en percentage per split relatief aan totaal
        								value = ((double) frequencies_number[i][j*2] / aantal_totaal) * 100;
        							}
								}
								else // geen split
								{
    								// percentage t.o.v. totaal
        							value = ((double) frequencies_number[i][j * 2] / aantal_totaal) * 100; 
								}
							} // number
							else if (frequencies_enum != null)
							{
								if (HistogramView.this.hasSplit())
								{
									if (HistogramView.this.model.isSplitInSingleView())
    								{
    									if (HistogramView.this.isNextToEachOtherSelected())
    									{
    										// als naast elkaar, dan percentage relatief aan totaalaantal
    										value = ((double) frequencies_enum[i][j].frequency / aantal_totaal) * 100;
    									}
    									else
    									{
            								// als gestapeld in 1 view, dan percentage relatief aan totaal per bin 
                							//value = ((double) frequencies_enum[i][j].frequency / aantalPerBin[j]) * 100;
    										
    										// bij gestapeld moet de waarde relatief aan totaal 
    										// en tooltip de waarde aan de as
    										for (int k = 0; k <= i; k++)
    										{
    											value = value + ((double) frequencies_enum[k][j].frequency / aantal_totaal) * 100;
    										}
    									}
    								}
        							else if (HistogramView.this.model.getPercentageSplitTotal())
        							{
        								// split in meerdere views en percentage per split relatief aan split
            							value = ((double) frequencies_enum[i][j].frequency / aantalPerSplit[i]) * 100;
        							}
    								else
    								{
        								// split met meerdere views en percentage per split relatief aan totaal
            							value = ((double) frequencies_enum[i][j].frequency / aantal_totaal) * 100;
    								}
								}
								else // geen split
								{
    								// percentage berekenen t.o.v. totaal
        							value = ((double) frequencies_enum[i][j].frequency / aantal_totaal) * 100;
								}
							} // enum
						} // isPercentage
						else
						{
//    							int waarde = 0;
							value = 0;
							
							if (frequencies_number != null)
							{
								if (HistogramView.this.hasSplit()
										&& HistogramView.this.model.isSplitInSingleView()
										&& !HistogramView.this.isNextToEachOtherSelected())
								{
									// voor gestapeld toon tooltip van as-waarde
									for (int k = 0; k <= i; k++)
									{
										value = value + frequencies_number[k][j*2];
									}
								}
								else
								{
									value = frequencies_number[i][j * 2];
								}
							}
							else if (frequencies_enum != null)
							{
								if (HistogramView.this.hasSplit()
										&& HistogramView.this.model.isSplitInSingleView()
										&& !HistogramView.this.isNextToEachOtherSelected())
								{
									// voor gestapeld toon tooltip van as-waarde
									for (int k = 0; k <= i; k++)
									{
										value = value + frequencies_enum[k][j].frequency;
									}
								}
								else
								{
									value = frequencies_enum[i][j].frequency;
								}
							}
						}

						//System.out.println("... valueString = " + valueString);
						
						if (HistogramView.this.model.isFrequencyPolygonCumulativeMode())
						{
							value = cumulativeValuePerSplit[i]; 
						}
						
						// round to one decimal
						if (!Double.isNaN(value) && !Double.isInfinite(value))
							value = Statistiek.round(value, 1);
						else
							value = 0;
						
						// Get valueString for showing tooltip text
						String valueString = "0";
						valueString = Statistiek.getStringValue(value);
						
						if (!valueString.equals("0") || HistogramView.this.model.isFrequencyPolygonMode())
						{
							// For frequency polygon show tooltip and highlight for every dot
							
							highlightedBar = j;
							highlightInSplit = i;

//    							System.out.println("... highlightedBar = "
//    								+ highlightedBar + ", highlightInSplit = "
//    								+ highlightInSplit);

							// show tooltip
							ToolTipManager.sharedInstance().setEnabled(true);
							if (isPercentage)
								this.setToolTipText(valueString + "%");
							else
								this.setToolTipText("aantal = " + valueString);
						}
						else
						{
							highlightedBar = -1;
							highlightInSplit = -1;
						}
						found = true; // gevonden
					}
					else
					{
						highlightedBar = -1;
						highlightInSplit = -1;
						ToolTipManager.sharedInstance().setEnabled(false);
					}
				}
			}
		} // class HistogramBarPanel

		private boolean isOverToolTipArea(Point p, Rectangle rect)
		{
			boolean isOverToolTipArea = false;
			int marge = 5;
			
			if (HistogramView.this.model.isFrequencyPolygonMode())
			{
				if ((p.x > rect.x) && (p.x < (rect.x + rect.width))
					&& (p.y > rect.y) && (p.y < (rect.y + rect.height)))
				{
					isOverToolTipArea = true;
				}
			}
			else
			{
				if (HistogramView.this.model.hasVerticalBars())
				{
	    			if ((p.x > rect.x) && (p.x < (rect.x + rect.width))
	    			&& (p.y > rect.y - marge) && (p.y < rect.y + marge))
	    			{
	    				isOverToolTipArea = true;
	    			}
				}
				else // horizontal bars
				{
	    			if ((p.x > (rect.x + rect.width - marge)) 
	    				&& (p.x < (rect.x + rect.width + marge))
	    				&& (p.y > rect.y) && (p.y < (rect.y + rect.height)))
	    			{
	    				isOverToolTipArea = true;
	    			}				
				}
			}
			return isOverToolTipArea;
		}
	}

	private class BarClickListener implements MouseListener
	{

		public void mouseClicked(MouseEvent e)
		{
			if (HistogramView.this.model.isFrequencyPolygonMode())
			{
				return;
			}
			
			int i;
			
			for (i = 0; i < HistogramView.this.barRectangles.size(); i++)
			{
				if (HistogramView.this.barRectangles.get(i) != null
					&& HistogramView.this.barRectangles.get(i).contains(
						e.getPoint()))
				{
					this.barClicked(i);
					break;
				}
			}
			
			if (i == HistogramView.this.barRectangles.size())
			{
				// no bin was clicked, deselect all
				ArrayList<Boolean> selectionList = new ArrayList<Boolean>();
				for (int row = 0; row < HistogramView.this.model
					.getStatTableModel().getRowCount(); row++)
				{
					selectionList.add(false);
				}
				HistogramView.this.model.getStatTableModel().setSelectionList(
					selectionList);
			}
		}

		private void barClicked(int bar)
		{
			if (!HistogramView.this.model.columnIndexValid())
			{
				return;
			}

			int noBins;
			
			if (model.isOptimizeScale())
			{
				noBins = HistogramView.this.model.getNoBins();
			}
			else
			{
				noBins = getNumberOfBinsfromBinsSettings();
			}
			
			int bin = bar % noBins;
			int splitClass = bar / noBins;

			ColumnType cType = HistogramView.this.model.getStatTableModel()
				.getColumnTypes()
				.get(HistogramView.this.model.getColumnIndex());
			AllowedTypes type = cType.getType();
			if (type.isNumber())
			{
				ArrayList<Boolean> selectionList = new ArrayList<Boolean>(
					HistogramView.this.model.getStatTableModel().getRowCount());
				for (int i = 0; i < HistogramView.this.model.getStatTableModel()
					.getRowCount(); i++)
				{
					Object o = HistogramView.this.model.getStatTableModel()
						.getValueAt(i,
							HistogramView.this.model.getColumnIndex());

					selectionList
						.add(!o.equals(ColumnType.WILDCARD)
							&& HistogramView.this.model.binOfNumber(Double
								.parseDouble((String) o)) == bin
							&& HistogramView.this.model.getStatTableModel()
								.classifyObject(i,
									HistogramView.this.model.getSplitOptions()) == splitClass);
				}

				HistogramView.this.model.getStatTableModel().setSelectionList(
					selectionList);
			}
			else
			{
				String clicked;
				if (type.equals(AllowedTypes.ENUM))
				{
					clicked = cType.getEnumOptions()[bin];
					int wildcardIndex = Arrays.asList(cType.getEnumOptions())
						.indexOf(ColumnType.WILDCARD);
					if (wildcardIndex <= 0 && wildcardIndex < bin)
					{
						clicked = cType.getEnumOptions()[bin + 1];
					}
				}
				else
				{
					// stringColumnOptions staan niet in alfabetische volgorde; neem enumClassFrequency
					FrequencyTuple[] freqTuple = HistogramView.this.model.enumClassFrequency()[splitClass];
					clicked = freqTuple[bin].label;
				}

				ArrayList<Boolean> selectionList = new ArrayList<Boolean>(
					HistogramView.this.model.getStatTableModel().getRowCount());
				for (int i = 0; i < HistogramView.this.model.getStatTableModel()
					.getRowCount(); i++)
				{
					Object o = HistogramView.this.model.getStatTableModel()
						.getValueAt(i,
							HistogramView.this.model.getColumnIndex());
					selectionList
						.add(!o.equals(ColumnType.WILDCARD)
							&& ((String) o).equals(clicked)
							&& HistogramView.this.model.getStatTableModel()
								.classifyObject(i,
									HistogramView.this.model.getSplitOptions()) == splitClass);
				}
				HistogramView.this.model.getStatTableModel().setSelectionList(
					selectionList);
			}
		}

		public void mouseEntered(MouseEvent arg0)
		{
			// Do nothing

		}

		public void mouseExited(MouseEvent arg0)
		{
			// Do nothing

		}

		public void mousePressed(MouseEvent arg0)
		{
			// Do nothing

		}

		public void mouseReleased(MouseEvent arg0)
		{
			// Do nothing

		}

	} // class BarClickListener
}
