package fi.kladje;

public class AffineTransform 
{
	double m00 = 1, m01 = 0, m10 = 0, m11 = 1, b0 = 0, b1 = 0;
	
	public AffineTransform()
	{
		
	}
	
	public AffineTransform(double m00, double m01, double m10, double m11, double b0, double b1)
	{
		this.m00 = m00;
		this.m01 = m01;
		this.m10 = m10;
		this.m11 = m11;
		this.b0 = b0;
		this.b1 = b1;
	}
	
	public AffineTransform leftMultiplyBy(AffineTransform at)
	{
		AffineTransform result = new AffineTransform(
				at.m00*m00+at.m01*m10,
				at.m00*m01+at.m01*m11,
				at.m10*m00+at.m11*m10,
				at.m10*m01+at.m11*m11,
				at.m00*b0+at.m01*b1+at.b0,
				at.m10*b0+at.m11*b1+at.b1);
				
		return result;
	}

}
