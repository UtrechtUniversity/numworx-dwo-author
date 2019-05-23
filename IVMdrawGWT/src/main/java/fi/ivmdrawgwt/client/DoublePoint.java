package fi.ivmdrawgwt.client;

import java.awt.Point;

/**
 * punt in het vlak met double coordinaten 
 * @author huub
 */
public class DoublePoint 
{
	/**
	 * x- en y-coordinaat of double punt
	 */
	double x; double y;
	
	/**
	 * constructor
	 * @param x x-coordinaat
	 * @param y y-coordinaat
	 */
	public DoublePoint(double x, double y) 
	{
		this.x = x; this.y = y;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public void translate(double dx, double dy) {
		x+=dx;
		y+=dy;
	}
	
	public void scale(double cx, double cy, double factor) {
		x = cx+(x-cx)*factor;
		y = cy+(y-cy)*factor;
	}
	
}
