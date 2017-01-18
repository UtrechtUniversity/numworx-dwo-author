package fi.euclides.util;

public class JMath {

	public static double hypot(double x, double y) {
		return java.lang.Math.sqrt(x*x+y*y);
	}

	public static long round(double d) {
		return Math.round(d);
	}
	
	/**
	 *	Returns the inverse (arc) tangent of its argument.
	 *	@param	x	The argument, a double.
	 *	@return	Returns the angle, in radians, whose tangent is x.
	 *			It is in the range [-pi/2,pi/2].
	 */
	static public double atan(double x)
	{
		return Math.atan(x);
	}
	/**
	 *	Returns angle corresponding to a Cartesian point.
	 *	@param	x	The first argument, a double.
	 *	@param	y	The second argument, a double.
	 *	@return	Returns the angle, in radians, the the line
	 *			from (0,0) to (x,y) makes with the x-axis.
	 *			It is in the range [-pi,pi].
	 */
	static public double atan2(double y, double x)
	{  
		return Math.atan2(y, x);
	}

	public static double pow(double a, double b) {
		return Math.pow(a, b);
	}

}
