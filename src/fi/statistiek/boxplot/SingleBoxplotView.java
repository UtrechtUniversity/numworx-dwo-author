package fi.statistiek.boxplot;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * Draws a single dotplot
 * 
 * @author Manu Drijvers
 *
 */
public class SingleBoxplotView extends JPanel {
	private boolean drawable;
	
	private double minValue;		//the minimum value for the data of this boxplot
	private double lowerQuartile;
	private double median;
	private double upperQuartile;
	private double maxValue;

	private double dataMinValue;	//the minimum value in all the data
	private double dataMaxValue;	//the maximum value in all the data
	
	private boolean verticalBoxplots;
	
	public static double WIDTH_FILL_FRACTION = 0.8;
	public static double HEIGHT_FILL_FRACTION = 0.8;
	public static final Color BOX_COLOR = new Color(220,160,0);
	public static final int MAX_BOX_HEIGHT = 40;
	
	public SingleBoxplotView(Double minValue, Double lowerQuartile, Double median, Double upperQuartile, Double maxValue, Double dataMinValue, Double dataMaxValue, boolean verticalBoxplots) {
		if(minValue == null || lowerQuartile == null || median == null || upperQuartile == null || maxValue == null || dataMinValue == null || dataMaxValue == null) {
			drawable = false;
			System.out.println("Niet drawable!");
			return;
		}
		else {
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
	}
	
	/**
	 * Determines the y-coordinate on the screen in this component of value d
	 * @return the y-coordinate on the screen in this component of value d
	 */
	private int valueToScreenLocation(double d) {
		double a = (this.dataMaxValue - d)/(this.dataMaxValue-this.dataMinValue);
		double y = (a*HEIGHT_FILL_FRACTION)* (this.verticalBoxplots ? super.getHeight() : super.getWidth());
		y += (1.0-HEIGHT_FILL_FRACTION)*0.5*(this.verticalBoxplots ? super.getHeight() : super.getWidth());
		
		if(!this.verticalBoxplots) {
			y = super.getWidth() - y;
		}
		return (int)Math.round(y);
	}
	
	/**
	 * Draws a dotted vertical line
	 * @param x
	 * @param y1
	 * @param y2
	 * @param g
	 */
	private void drawDottedVerticalLine(int x, int y1, int y2, Graphics g) {
		for(int i = y1; i < y2; i+=4) {
			if((i+1)<y2) {
				g.drawLine(x, i, x, i+2);
			}
			else {
				g.drawLine(x, i, x, i+1);
			}
		}
	}
	
	/**
	 * Draws a dotted horizontal line
	 * @param x1 
	 * @param x2
	 * @param y
	 * @param g
	 */
	private void drawDottedHorizontalLine(int x1, int x2, int y, Graphics g) {
		for(int i = x1; i < x2; i+=4) {
			if((i+1)<x2) {
				g.drawLine(i, y, i+2, y);
			}
			else {
				g.drawLine(i, y, i+1, y);
			}
		}
	}
	
	public void paintComponent(Graphics g) {
		if(!this.drawable) {
			return;
		}
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		//g.clearRect(0, 0, super.getWidth(), super.getHeight());
		g.setColor(Color.BLACK);
		
		
		int locationMinValue = this.valueToScreenLocation(this.minValue);
		int locationLowerQuartile = this.valueToScreenLocation(this.lowerQuartile);
		int locationMedian = this.valueToScreenLocation(this.median);
		int locationUpperQuartile = this.valueToScreenLocation(this.upperQuartile);
		int locationMaxValue = this.valueToScreenLocation(this.maxValue);
		
		
		if(this.verticalBoxplots) {	
			int x = (int)Math.round((1.0-WIDTH_FILL_FRACTION)*0.5*super.getWidth());
			int width = Math.min(SingleBoxplotView.MAX_BOX_HEIGHT, (int)Math.round(WIDTH_FILL_FRACTION*super.getWidth()));

			//draw min value
			g.drawLine(x, locationMinValue, x+width, locationMinValue);
			
			//draw dotted line from min value to lower quartile
			this.drawDottedVerticalLine(x+width/2, locationLowerQuartile, locationMinValue, g);
			
			//draw median and quartiles box around it
			g.setColor(BOX_COLOR);
			g.fillRect(x, locationLowerQuartile, width, locationUpperQuartile - locationLowerQuartile);
			g.setColor(Color.BLACK);
			g.drawLine(x, locationLowerQuartile, x+width, locationLowerQuartile);
			g.drawLine(x, locationMedian, x+width, locationMedian);
			g.drawLine(x, locationUpperQuartile, x+width, locationUpperQuartile);
			g.drawLine(x, locationLowerQuartile, x, locationUpperQuartile);
			g.drawLine(x+width, locationLowerQuartile, x+width, locationUpperQuartile);
			
			//draw dotted line from upper quartile to max value
			this.drawDottedVerticalLine(x+width/2, locationMaxValue, locationUpperQuartile, g);
			
			//draw max value
			g.drawLine(x, locationMaxValue, x+width, locationMaxValue);
		}
		else {
			int y = (int)Math.round((WIDTH_FILL_FRACTION)*super.getHeight());
			int height = Math.min(SingleBoxplotView.MAX_BOX_HEIGHT, (int)Math.round(WIDTH_FILL_FRACTION*super.getHeight()));
			
			//draw min value
			g.drawLine(locationMinValue, y, locationMinValue, y-height);
			
			//draw dotted line from min value to lower quartile
			this.drawDottedHorizontalLine(locationMinValue, locationLowerQuartile, y-height/2, g);
			
			//draw median and quartiles box around it
			g.setColor(BOX_COLOR);
			g.fillRect(locationLowerQuartile, y-height, locationUpperQuartile - locationLowerQuartile , height);
			g.setColor(Color.BLACK);
			g.drawLine(locationLowerQuartile, y, locationUpperQuartile, y);
			g.drawLine(locationLowerQuartile, y-height, locationUpperQuartile, y-height);
			g.drawLine(locationMedian, y, locationMedian,  y-height);
			g.drawLine(locationLowerQuartile, y, locationLowerQuartile, y-height);
			g.drawLine(locationUpperQuartile, y, locationUpperQuartile, y-height);
			
			//draw dotted line from upper quartile to max value
			this.drawDottedHorizontalLine(locationUpperQuartile, locationMaxValue, y-height/2, g);
			
			//draw max value
			g.drawLine(locationMaxValue, y, locationMaxValue, y-height);
		}
	}
	
	public String toString() {
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
}
