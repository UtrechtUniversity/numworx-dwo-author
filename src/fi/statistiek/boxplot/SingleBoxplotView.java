package fi.statistiek.boxplot;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.ToolTipManager;

import fi.statistiek.ColorGenerator;
import fi.statistiek.Statistiek;

/**
 * Draws a single dotplot, possibly for a split class.
 * 
 * @author Manu Drijvers, Sylvia van Borkulo
 * 
 */
/**
 * @author borku102
 *
 */
public class SingleBoxplotView extends JPanel implements MouseMotionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean drawable;

	/**
	 * The value below which a value is a strong outlier in the Tukey boxplot, i.e.,  
	 * more than 3 * IQR below Q1.
	 */
	private Double outlierStrongMinValue;
	/**
	 * The value below which a value is a weak outlier in the Tukey boxplot, i.e., 
	 * between 1.5 * IQR and 3 * IQR below Q1.
	 */
	private Double outlierWeakMinValue;
	/**
	 * An arraylist of outlier values on the
	 * lower side of the Tukey boxplot.
	 */
	private ArrayList<Double> outlierMinValues;
	/**
	 * The minimum value for the data of this (split class) boxplot.
	 */
	private double minValue;
	private double lowerQuartile;
	private double median;
	private double upperQuartile;
	private double maxValue;
	/**
	 * An arraylist of outlier values on the
	 * upper side of the Tukey boxplot for each split class.
	 */
	private ArrayList<Double> outlierMaxValues;
	/**
	 * The value above which a value is a weak outlier in the Tukey boxplot, i.e., 
	 * between 1.5 * IQR and 3 * IQR above Q3.
	 */
	private Double outlierWeakMaxValue;
	/**
	 * The value above which a value is a strong outlier in the Tukey boxplot, i.e.,  
	 * more than 3 * IQR above Q3.
	 */
	private Double outlierStrongMaxValue;

	/**
	 * The minimum value in all the data.
	 */
	private double dataMinValue;
	/**
	 * The maximum value in all the data.
	 */
	private double dataMaxValue; 
	
	private double firstMarker;
	private int step;
	private double max;

	private boolean isTukeyBox;
	private boolean isEmptyBoxplot;
	private boolean verticalBoxplots;
	
	/**
	 * An array of booleans 'is highlighted y/n' for the lower outlier values.
	 */
	private ArrayList<Boolean> highlightOutlierMinValues;
	private boolean highlightMinValue = false;
	private boolean highlightLowerQuartile = false;
	private boolean highlightMedian = false;
	private boolean highlightUpperQuartile = false;
	private boolean highlightMaxValue = false;
	/**
	 * An array of booleans 'is highlighted y/n' for the upper outlier values.
	 */
	private ArrayList<Boolean> highlightOutlierMaxValues;

	public static double WIDTH_FILL_FRACTION = 0.8;
	public static double HEIGHT_FILL_FRACTION = 0.8;
	public static final Color BOX_COLOR = ColorGenerator.DEFAULT_VIEW_ELEMENT_COLOR;
	public static final int MAX_BOX_HEIGHT = 40;
	private static final Font OUTLIER_FONT_STRING = new Font("SansSerif", Font.BOLD, 18);
	private static final Font SELECTED_OUTLIER_FONT_STRING = new Font("SansSerif", Font.BOLD, 22);
//	private static final int COMPENSATION_POSITION_ASTERIX = 12;//10;
	private static final int SELECTED_LINE_WIDTH = 3;
	private static final int DEFAULT_LINE_WIDTH = 1;

	/**
	 * 
	 * @param outlierMinValues
	 * @param minValue
	 * @param lowerQuartile
	 * @param median
	 * @param upperQuartile
	 * @param maxValue
	 * @param outlierMaxValues
	 * @param dataMinValue
	 * @param dataMaxValue
	 * @param verticalBoxplots
	 * @param isTukey
	 */
	public SingleBoxplotView(Double outlierStrongMinValue, Double outlierWeakMinValue, ArrayList<Double> outlierMinValues, Double minValue, Double lowerQuartile,
		Double median, Double upperQuartile, Double maxValue, ArrayList<Double> outlierMaxValues,
		Double outlierWeakMaxValue, Double outlierStrongMaxValue, Double dataMinValue, Double dataMaxValue, 
		boolean verticalBoxplots, boolean isTukey, boolean isEmptyBoxplot)
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

		this.outlierStrongMinValue = outlierStrongMinValue;
		this.outlierWeakMinValue = outlierWeakMinValue;
		this.outlierMinValues = outlierMinValues;
		this.minValue = minValue;
		this.lowerQuartile = lowerQuartile;
		this.median = median;
		this.upperQuartile = upperQuartile;
		this.maxValue = maxValue;
		this.outlierMaxValues = outlierMaxValues;
		this.outlierWeakMaxValue = outlierWeakMaxValue;
		this.outlierStrongMaxValue = outlierStrongMaxValue;

		this.dataMinValue = dataMinValue;
		this.dataMaxValue = dataMaxValue;

		this.initializeHighlightValues();

		this.verticalBoxplots = verticalBoxplots;
		this.isTukeyBox = isTukey;
		this.isEmptyBoxplot = isEmptyBoxplot;
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
	 * Determines for an array of values the y coordinate (vertical boxplot) or the
	 * x coordinate (horizontal boxplot) on the screen in the boxplot.
	 * 
	 * @return an array of coordinates on the screen in the boxplot of 
	 * 		the array of values
	 */
	private int[] valueToScreenLocation(ArrayList<Double> values)
	{
		int[] coordinates = new int[values.size()];
		
		for (int i = 0; i < values.size(); i++)
		{
			int coordinate;
			
			if (values.get(i) != null)
			{
				coordinate = this.valueToScreenLocation(values.get(i));
			}
			else
			{
				coordinate = -1;
			}

			coordinates[i] = coordinate; 
		}
		
		return coordinates;
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

	/**
	 * Teken het hele 'assenstelsel-veld'. Bij meerdere boxplots worden 
	 * meerdere singleboxplotviews naast elkaar getoond.
	 */
	public void paintComponent(Graphics g)
	{
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		// g.clearRect(0, 0, super.getWidth(), super.getHeight());

		g.setColor(Color.BLACK);

		if (!this.drawable)
		{
			return;
		}

		int locationMinValue = this.valueToScreenLocation(this.minValue);
		int locationLowerQuartile = this
			.valueToScreenLocation(this.lowerQuartile);
		int locationMedian = this.valueToScreenLocation(this.median);
		int locationUpperQuartile = this
			.valueToScreenLocation(this.upperQuartile);
		int locationMaxValue = this.valueToScreenLocation(this.maxValue);
		int[] locationOutlierMinValues = null;
		int[] locationOutlierMaxValues = null;

		if (this.isTukeyBox && !this.isEmptyBoxplot)
		{
			locationOutlierMinValues = valueToScreenLocation(outlierMinValues);
			locationOutlierMaxValues = valueToScreenLocation(outlierMaxValues);
		}
		
		// the radius for the outlier dots
		double dotRadius = 5;

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

			// paint lower and upper outliers for Tukey boxplot
			if (this.isTukeyBox)
			{
				// paint the lower outliers
				this.paintLowerOutliers(g, x + (int) (0.5 * width), dotRadius, locationOutlierMinValues);
				// paint the upper outliers
				this.paintUpperOutliers(g, x + (int) (0.5 * width), dotRadius, locationOutlierMaxValues);
			}

			// paint min value
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

			// paint lower and upper outliers for Tukey boxplot
			if (this.isTukeyBox)
			{
				// paint the lower outliers
				this.paintLowerOutliers(g, y - (int) (0.5 * height), dotRadius, locationOutlierMinValues);
				// paint the upper outliers
				this.paintUpperOutliers(g, y - (int) (0.5 * height), dotRadius, locationOutlierMaxValues);
			}

			// paint min value
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
	 * Paint the lower outliers of the boxplot with the given fixed coordinate.
	 * 
	 * @param g
	 * @param fixedCoordinate
	 * @param dotRadius
	 * @param locationLowerOutliers
	 */
	private void paintLowerOutliers(Graphics g, int fixedCoordinate, double dotRadius, int[] locationLowerOutliers)
	{
		String asterix = "*";
		Font font = super.getFont().deriveFont(super.getFont().getStyle());
		FontMetrics fm = super.getFontMetrics(font);
		
		Graphics2D g2 = (Graphics2D) g;

		Font originalFontString = g.getFont();
		
		for (int i = 0; i < locationLowerOutliers.length; i++)
		{
			if (verticalBoxplots)
			{
				if (outlierMinValues.get(i) < this.outlierStrongMinValue)
				{
					// draw asterix

					if (highlightOutlierMinValues.get(i))
					{
						g.setFont(SELECTED_OUTLIER_FONT_STRING);
					}
					else
					{
						g.setFont(OUTLIER_FONT_STRING);
					}
					g.drawString(asterix,
						fixedCoordinate - fm.stringWidth(asterix)/2,
//						locationLowerOutliers[i] + COMPENSATION_POSITION_ASTERIX);
						locationLowerOutliers[i] + fm.getHeight()/2);
					// reset to original font
					g.setFont(originalFontString);
				}
				else
				{
					// draw circle
					
					if (highlightOutlierMinValues.get(i))
					{
				        g2.setStroke(new BasicStroke(SELECTED_LINE_WIDTH));
					}

//					g2.fillOval((int) (fixedCoordinate - dotRadius - 2), 
//						(int) (locationLowerOutliers[i] - dotRadius - 2),
//						(int) (2 * dotRadius + 4),
//						(int) (2 * dotRadius + 4));
					g2.drawOval((int) (fixedCoordinate - dotRadius/2), 
						(int) (locationLowerOutliers[i] - dotRadius/2),
						(int) dotRadius,
						(int) dotRadius);

			        g2.setStroke(new BasicStroke(DEFAULT_LINE_WIDTH));
				}
			} // vertical box
			else
			{
				// horizontal box
				
				if (outlierMinValues.get(i) < outlierStrongMinValue)
				{
					// strong outlier
					// draw asterix

					if (highlightOutlierMinValues.get(i))
					{
						g.setFont(SELECTED_OUTLIER_FONT_STRING);
					}
					else
					{
						g.setFont(OUTLIER_FONT_STRING);
					}
					
					g.drawString(asterix,
						locationLowerOutliers[i] - fm.stringWidth(asterix)/2,
//						fixedCoordinate + COMPENSATION_POSITION_ASTERIX);
						fixedCoordinate + fm.getHeight()/2);
					// reset to original font
					g.setFont(originalFontString);

				}
				else
				{
					// weak outlier
					// draw circle
					
					if (highlightOutlierMinValues.get(i))
					{
				        g2.setStroke(new BasicStroke(SELECTED_LINE_WIDTH));
					}

//					g2.drawOval(locationLowerOutliers[i], 
//						fixedCoordinate,
//						(int) dotRadius,
//						(int) dotRadius);
					g2.drawOval((int) (locationLowerOutliers[i] - dotRadius/2), 
						(int) (fixedCoordinate - dotRadius/2),
						(int) dotRadius,
						(int) dotRadius);

					g2.setStroke(new BasicStroke(DEFAULT_LINE_WIDTH));
				}
			}
		}
	}

	/**
	 * Paint the upper outliers of the boxplot with the given fixed coordinate.
	 * 
	 * @param g
	 * @param fixedCoordinate
	 * @param dotRadius
	 * @param locationUpperOutliers
	 */
	private void paintUpperOutliers(Graphics g, int fixedCoordinate, double dotRadius, int[] locationUpperOutliers)
	{
		String asterix = "*";
		Font font = super.getFont().deriveFont(super.getFont().getStyle());
		FontMetrics fm = super.getFontMetrics(font);
		
		Graphics2D g2 = (Graphics2D) g;

		Font originalFontString = g.getFont();
		
		for (int i = 0; i < locationUpperOutliers.length; i++)
		{
			if (verticalBoxplots)
			{
				if (outlierMaxValues.get(i) > outlierStrongMaxValue)
				{
					// draw asterix

					if (highlightOutlierMaxValues.get(i))
					{
						g.setFont(SELECTED_OUTLIER_FONT_STRING);
					}
					else
					{
						g.setFont(OUTLIER_FONT_STRING);
					}
					
					g.drawString(asterix,
						fixedCoordinate - fm.stringWidth(asterix)/2,
//						locationUpperOutliers[i] + COMPENSATION_POSITION_ASTERIX);
						locationUpperOutliers[i] + fm.getHeight()/2);
					// reset to original font
					g.setFont(originalFontString);
				}
				else
				{
					// draw circle

					if (highlightOutlierMaxValues.get(i))
					{
				        g2.setStroke(new BasicStroke(SELECTED_LINE_WIDTH));
					}

//					g2.drawOval(fixedCoordinate,
//						locationUpperOutliers[i], 
//						(int) dotRadius,
//						(int) dotRadius);
					g2.drawOval((int) (fixedCoordinate - dotRadius/2),
						(int) (locationUpperOutliers[i] - dotRadius/2), 
						(int) dotRadius,
						(int) dotRadius);

					g2.setStroke(new BasicStroke(DEFAULT_LINE_WIDTH));
				}
			} // vertical box
			else
			{
				// horizontal box
				
				if (outlierMaxValues.get(i) > outlierStrongMaxValue)
				{
					// draw asterix

					if (highlightOutlierMaxValues.get(i))
					{
						g.setFont(SELECTED_OUTLIER_FONT_STRING);
					}
					else
					{
						g.setFont(OUTLIER_FONT_STRING);
					}
					
					g.drawString(asterix,
						locationUpperOutliers[i] - fm.stringWidth(asterix)/2,
						fixedCoordinate + fm.getHeight()/2);
//						fixedCoordinate + COMPENSATION_POSITION_ASTERIX);
					// reset to original font
					g.setFont(originalFontString);
				}
				else
				{
					// draw circle

					if (highlightOutlierMaxValues != null && highlightOutlierMaxValues.get(i))
					{
				        g2.setStroke(new BasicStroke(SELECTED_LINE_WIDTH));
					}

//					g2.drawOval(locationUpperOutliers[i], 
//						fixedCoordinate,
//						(int) dotRadius,
//						(int) dotRadius);
					g2.drawOval((int) (locationUpperOutliers[i] - dotRadius/2), 
						(int) (fixedCoordinate - dotRadius/2),
						(int) dotRadius,
						(int) dotRadius);

					g2.setStroke(new BasicStroke(DEFAULT_LINE_WIDTH));
				}
			} // horizontal boxplot
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
			
    		if (p.x > lower_x && p.x < upper_x) // x in the box area -> TODO eruit halen, zit nu in elke check
    		{
    			if (p.y > (locationMedian - 5) && p.y < (locationMedian + 5))
	    		{
	    			ToolTipManager.sharedInstance().setEnabled(true);
	    			this.setToolTipText(Statistiek.rb.getString("medianIs") + Statistiek.getStringValue(this.median));			
	    			setHighlightValues(false, false, true, false, false);
	    		}
	    		else if ((p.y > (locationMinValue - 5)) && p.y < (locationMinValue + 5))
	    		{
	    			ToolTipManager.sharedInstance().setEnabled(true);
					String text;
					if (isTukeyBox && hasLowerOutliers())
					{
						text = Statistiek.rb.getString("tukeyMinimumIs");
					}
					else
					{
						text = Statistiek.rb.getString("minimumIs");
					}
	    			this.setToolTipText(text + Statistiek.getStringValue(this.minValue));
	    			setHighlightValues(true, false, false, false, false);
	    		}
	    		// TODO: when e.g. minValue and lowerQuartile are close together or the same
	    		// show multiple tooltips
	    		else if ((p.y > (locationLowerQuartile - 5)) && p.y < (locationLowerQuartile + 5))
	    		{
	    			ToolTipManager.sharedInstance().setEnabled(true);
	    			this.setToolTipText(Statistiek.rb.getString("firstQuartileIs") + Statistiek.getStringValue(this.lowerQuartile));
	    			setHighlightValues(false, true, false, false, false);
	    		}
	    		else if ((p.y > (locationUpperQuartile - 5)) && p.y < (locationUpperQuartile + 5))
	    		{
	    			ToolTipManager.sharedInstance().setEnabled(true);
	    			this.setToolTipText(Statistiek.rb.getString("thirdQuartileIs") + Statistiek.getStringValue(this.upperQuartile));			
	    			setHighlightValues(false, false, false, true, false);
	    		}
	    		else if ((p.y > (locationMaxValue - 5)) && p.y < (locationMaxValue + 5))
	    		{
	    			ToolTipManager.sharedInstance().setEnabled(true);
					String text;
					if (isTukeyBox && hasUpperOutliers())
					{
						text = Statistiek.rb.getString("tukeyMaximumIs");
					}
					else
					{
						text = Statistiek.rb.getString("maximumIs");
					}
	    			this.setToolTipText(text + Statistiek.getStringValue(this.maxValue));			
	    			setHighlightValues(false, false, false, false, true);
	    		}
	    		else if ((p.x > lower_x + (upper_x - lower_x)/2 - 3 && p.x < upper_x - (upper_x - lower_x)/2 + 3) // x in a more restricted middle part of box width 
	    			&& isTukeyBox)
	    		{
	    			processOutliers(p.y);
	    		}
	    		else
	    		{
	    			if (isTukeyBox)
	    			{
	    				resetOutlierHighlightValues();
	    			}
	    			
	    			ToolTipManager.sharedInstance().setEnabled(false);
	    			setHighlightValues(false, false, false, false, false);
	    		}
    		}
    		else
    		{
    			if (isTukeyBox)
    			{
    				resetOutlierHighlightValues();
    			}
    			
    			ToolTipManager.sharedInstance().setEnabled(false);
    			setHighlightValues(false, false, false, false, false);
    		}
		} // vertical boxplots
		else // horizontal boxplots
		{
			// lower y coordinate of the horizontal boxplot
			int upper_y = (int) Math.round((WIDTH_FILL_FRACTION) * super.getHeight());
			int lower_y = upper_y - widthBoxplot;
			
    		if (p.y > lower_y && p.y < upper_y) // y in the box area
    		{
    			if (p.x > (locationMedian - 5) && p.x < (locationMedian + 5))
	    		{
	    			ToolTipManager.sharedInstance().setEnabled(true);
	    			this.setToolTipText(Statistiek.rb.getString("medianIs") + Statistiek.getStringValue(this.median));			
	    			setHighlightValues(false, false, true, false, false);
	    		}
	    		else if ((p.x > (locationMinValue - 5)) && p.x < (locationMinValue + 5))
	    		{
	    			ToolTipManager.sharedInstance().setEnabled(true);
					String text;
					if (isTukeyBox && hasLowerOutliers())
					{
						text = Statistiek.rb.getString("tukeyMinimumIs");
					}
					else
					{
						text = Statistiek.rb.getString("minimumIs");
					}
	    			this.setToolTipText(text + Statistiek.getStringValue(this.minValue));
	    			setHighlightValues(true, false, false, false, false);
	    		}
	    		else if ((p.x > (locationLowerQuartile - 5)) && p.x < (locationLowerQuartile + 5))
	    		{
	    			ToolTipManager.sharedInstance().setEnabled(true);
	    			this.setToolTipText(Statistiek.rb.getString("firstQuartileIs") + Statistiek.getStringValue(this.lowerQuartile));
	    			setHighlightValues(false, true, false, false, false);
	    		}
	    		else if ((p.x > (locationUpperQuartile - 5)) && p.x < (locationUpperQuartile + 5))
	    		{
	    			ToolTipManager.sharedInstance().setEnabled(true);
	    			this.setToolTipText(Statistiek.rb.getString("thirdQuartileIs") + Statistiek.getStringValue(this.upperQuartile));			
	    			setHighlightValues(false, false, false, true, false);
	    		}
	    		else if ((p.x > (locationMaxValue - 5)) && p.x < (locationMaxValue + 5))
	    		{
	    			ToolTipManager.sharedInstance().setEnabled(true);
					String text;
					if (isTukeyBox && hasUpperOutliers())
					{
						text = Statistiek.rb.getString("tukeyMaximumIs");
					}
					else
					{
						text = Statistiek.rb.getString("maximumIs");
					}
	    			this.setToolTipText(text + Statistiek.getStringValue(this.maxValue));			
	    			setHighlightValues(false, false, false, false, true);
	    		}
	    		else if ((p.y > lower_y + (upper_y - lower_y)/2 - 3 && p.y < upper_y - (upper_y - lower_y)/2 + 3) // y in a more restricted middle part of box width 
	    			&& isTukeyBox)
	    		{
	    			processOutliers(p.x);
	    		}
	    		else
	    		{
	    			if (isTukeyBox)
	    			{
	    				resetOutlierHighlightValues();
	    			}
	    			
	    			ToolTipManager.sharedInstance().setEnabled(false);
	    			setHighlightValues(false, false, false, false, false);
	    		}
    		}
    		else
    		{
    			if (isTukeyBox)
    			{
    				resetOutlierHighlightValues();
    			}
    			
    			ToolTipManager.sharedInstance().setEnabled(false);
				setHighlightValues(false, false, false, false, false);
    		}
		} // horizontal boxplot
		
		this.repaint();
	}
	
	/**
	 * Returns whether or not the boxplot has lower outliers.
	 * 
	 * @return
	 */
	private boolean hasLowerOutliers()
	{
		boolean b = false;
		
		if (this.isTukeyBox && this.outlierMinValues != null && this.outlierMinValues.size() > 0)
		{
			b = true;
		}
		
		return b;
	}

	/**
	 * Returns whether or not the boxplot has upper outliers.
	 * 
	 * @return
	 */
	private boolean hasUpperOutliers()
	{
		boolean b = false;
		
		if (this.isTukeyBox && this.outlierMaxValues != null && this.outlierMaxValues.size() > 0)
		{
			b = true;
		}
		
		return b;
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
	
	/**
	 * Reset the outlier highlight values to false.
	 */
	public void resetOutlierHighlightValues()
	{
		for (int i = 0; i < outlierMinValues.size(); i++)
		{
			this.setArrayListValue(this.highlightOutlierMinValues, i, false);		
		}
		
		for (int i = 0; i < outlierMaxValues.size(); i++)
		{
			this.setArrayListValue(this.highlightOutlierMaxValues, i, false);
		}
	}

	/**
	 * Set the boolean value at the given index or add the boolean value at the given index if the index
	 * exceeds the size of the list.
	 *  
	 * @param valueList
	 * @param index
	 * @param value
	 */
	private void setArrayListValue(ArrayList<Boolean> valueList,
		int index, boolean value)
	{
		if (valueList.size() < index + 1)
		{
			valueList.add(index, value);
		}
		else
		{
			valueList.set(index, value);
		}
	}

	/**
	 * Show popup for outliers and set highlight values.
	 * 
	 * @param coordinate
	 */
	private void processOutliers(int coordinate)
	{
		int[] locationOutlierMinValues = valueToScreenLocation(outlierMinValues);
		int[] locationOutlierMaxValues = valueToScreenLocation(outlierMaxValues);
		
		// process lower outliers
		for (int i = 0; i < locationOutlierMinValues.length; i++)
		{
			if (coordinate > locationOutlierMinValues[i] - 2 && coordinate < locationOutlierMinValues[i] + 2)
			{
				// set tooltip
    			ToolTipManager.sharedInstance().setEnabled(true);
    			this.setToolTipText(Statistiek.rb.getString("valueIs") 
					+ Statistiek.getStringValue(outlierMinValues.get(i)));			

    			setArrayListValue(highlightOutlierMinValues, i, true);
    			//showPopup = true;
    			
				// if found, still continue to set the rest of the highlight values
			}
			else
			{
				setArrayListValue(highlightOutlierMinValues, i, false);
			}
		}
		
		// process upper outliers
		for (int i = 0; i < locationOutlierMaxValues.length; i++)
		{
			if (coordinate > locationOutlierMaxValues[i] - 2 && coordinate < locationOutlierMaxValues[i] + 2)
			{
				// set tooltip
    			ToolTipManager.sharedInstance().setEnabled(true);
    			this.setToolTipText(Statistiek.rb.getString("valueIs") 
					+ Statistiek.getStringValue(outlierMaxValues.get(i)));
    			setArrayListValue(highlightOutlierMaxValues, i, true);
    			//showPopup = true;
				
				// if found, still continue to set the rest of the highlight values
			}
			else
			{
				setArrayListValue(highlightOutlierMaxValues, i, false);
			}
		}				
		
		// process upper outliers
	}
	
	/**
	 * Initialize the highlight arraylists and the independent axis width. 
	 * Lists are created and a default false value is added. If there is
	 * no split, the independent axis width is set 0.
	 *  
	 */
	public void initializeHighlightValues()
	{
		this.highlightOutlierMinValues = new ArrayList<Boolean>();
		this.highlightMinValue = false;
		this.highlightLowerQuartile = false;
		this.highlightMedian = false;
		this.highlightUpperQuartile = false;
		this.highlightMaxValue = false;
		this.highlightOutlierMaxValues = new ArrayList<Boolean>();
	}
	
	/**
	 * Initialize booleans for highlighting outliers in a Tukey boxplot.
	 * 
	 */
	void initializeOutlierHighlightValues()
	{
		for (int i = 0; i < outlierMinValues.size(); i++)
		{
			this.setArrayListValue(this.highlightOutlierMinValues, i, false);		
		}
		
		for (int i = 0; i < outlierMaxValues.size(); i++)
		{
			this.setArrayListValue(this.highlightOutlierMaxValues, i, false);
		}
	}
	
	/**
	 * Returns whether the single boxplot is drawable.
	 * 
	 * @return
	 */
	public boolean isDrawable()
	{
		return this.drawable;
	}
}
