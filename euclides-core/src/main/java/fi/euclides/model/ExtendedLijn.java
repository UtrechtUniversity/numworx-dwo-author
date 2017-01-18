package fi.euclides.model;

import java.io.IOException;

import fi.euclides.model.math.Numbers;

public class ExtendedLijn extends PuntenLijn {

	Lijn lijn;
	
	double x1, x2, y1, y2;
	
	double bx, by, ex, ey;
	
	public void setClip(int x, int y, int w, int h)
	{
		bx = x; 
		by = y;
		ex = w+x;
		ey = h+y;
	}
	
	
	void recalc() {
		if(Math.abs(lijn.getDX())>Math.abs(lijn.getDY()))
		{
			extendH();
		} else {
			extendV();
		}		
	}


	void extendV() {
		// vertical line.
		if( y1 > y2 ) {
			double tmp;
			tmp = x1; x1 = x2; x2 = tmp;
			tmp = y1; y1 = y2; y2 = tmp;
		}
		double ry = by;
		if(y1 > ry)
		{
			x1 = x1 - lijn.getDX()/lijn.getDY() * (y1-ry);
			y1 = ry;
		}
		ry = ey;
		if (y2 < ry)
		{
			x2 = x2 + lijn.getDX()/lijn.getDY() * (ry-y2);
			y2 = ry;
		}
	}


	void extendH() {
		// horizontal line.
		if( x1 > x2 ) {
			double tmp;
			tmp = x1; x1 = x2; x2 = tmp;
			tmp = y1; y1 = y2; y2 = tmp;
		}
		double rx = bx;
		if(x1 > rx)
		{
			y1 = y1 - lijn.getDY()/lijn.getDX() * (x1-rx);
			x1 = rx;
		}
		rx = ex;
		if (x2 < rx)
		{
			y2 = y2 + lijn.getDY()/lijn.getDX() * (rx-x2);
			x2 = rx;
		}
	}


	public boolean isDefined() {
		return false;
	}

	public String key() {
		return lijn.key();
	}

	public void read(Codec codec) throws IOException {
		throw new IOException();
	}

	public void write(Codec codec) throws IOException {
		lijn.write(codec);
	}

	/**
	 * @return the lijn
	 */
	public Lijn getLijn() {
		return lijn;
	}

	/**
	 * @param lijn the lijn to set
	 */
	public void setLijn(Lijn lijn) {
		this.lijn = lijn;
		x1 = lijn.getX1();
		y1 = lijn.getY1();
		x2 = lijn.getX2();
		y2 = lijn.getY2();
		recalc();
	}

	/**
	 * @return the x1
	 */
	public double getX1() {
		return x1;
	}

	/**
	 * @param x1 the x1 to set
	 */
	public void setX1(double x1) {
		this.x1 = x1;
	}

	/**
	 * @return the x2
	 */
	public double getX2() {
		return x2;
	}

	/**
	 * @param x2 the x2 to set
	 */
	public void setX2(double x2) {
		this.x2 = x2;
	}

	/**
	 * @return the y1
	 */
	public double getY1() {
		return y1;
	}

	/**
	 * @param y1 the y1 to set
	 */
	public void setY1(double y1) {
		this.y1 = y1;
	}

	/**
	 * @return the y2
	 */
	public double getY2() {
		return y2;
	}

	/**
	 * @param y2 the y2 to set
	 */
	public void setY2(double y2) {
		this.y2 = y2;
	}


	public void setClip(Numbers right, Numbers top, Numbers left,
			Numbers bottom) {
		bx = (int) Math.floor(right.doubleValue());
		ex = (int) Math.ceil(left.doubleValue());
		by = (int) Math.floor(top.doubleValue());
		ey = (int) Math.ceil(bottom.doubleValue());
	}

}
