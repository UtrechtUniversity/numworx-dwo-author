package fi.euclides.model.math;

import java.io.IOException;

public class Complex extends Numbers {

	private Numbers re, im;
	
	public double doubleValue() {
		return Double.NaN;
	}

	public String toString() {
		return "(" + toString(re) + "|" + toString(im) + ")";
	}

	@Override
	protected Numbers sqrt() {
		int s = signum(im);
		if( s == 0)
		{
			s = signum(re);
			if(s >= 0)
				return sqrt(re);
			return createComplex(ZERO, sqrt(neg(re)));
		}
		Numbers r = abs();
		Numbers real = sqrt(div(add(r, re), TWO));
		Numbers imag = sqrt(div(sub(r, re), TWO));
		if(s < 0) imag = neg(imag);
		return createComplex(real,imag);
		
	}

	protected Numbers abs() {
		return hypot(re, im);
	}
	
	protected int signum() {
		return 0;
	}

	Complex(Numbers re, Numbers im) {
		this.re = re;
		this.im = im;
	}

	public Complex() {
	}
	
	Numbers add(Complex b)
	{
		return createComplex(Numbers.add(re, b.re), Numbers.add(im, b.im));
	}
	
	Numbers mul(Complex b)
	{
		return createComplex(
				sub(mul(re, b.re), mul(im, b.im))
			,	add(mul(im, b.re), mul(re, b.im)) 
			);
	}
	
	Numbers mul(Numbers b)
	{
		if(b instanceof Complex)
			return mul((Complex)b);
		return createComplex(
				mul(re, b),
				mul(im, b)
		);
	}
	
	Numbers div(Complex b)
	{
		Numbers r = add(sqr(b.im), sqr(b.re));
		
		return div(createComplex(
				add(mul(re, b.re), mul(im, b.im))
				,
				sub(mul(b.re, im), mul(b.im, re))
		
		), r);
	}
	
	Numbers div(Numbers b)
	{
		if( b instanceof Complex)
			return div((Complex)b);
		return createComplex(div(re,b), div(im, b));
	}
	
	
	Numbers  add(Numbers b)
	{
		if(b instanceof Complex)
			return add((Complex)b);
		return createComplex(Numbers.add(re,b), im);
	}
	
	protected Numbers neg() {
		return createComplex(neg(re), neg(im));
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((im == null) ? 0 : im.hashCode());
		result = prime * result + ((re == null) ? 0 : re.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Complex other = (Complex) obj;
		if (im == null) {
			if (other.im != null)
				return false;
		} else if (!im.equals(other.im))
			return false;
		if (re == null) {
			if (other.re != null)
				return false;
		} else if (!re.equals(other.re))
			return false;
		return true;
	}

	public void writeNumber(NumberCodec dos) throws IOException {
		dos.writeComplex(re, im);
	}

	protected Numbers real() {
		return re;
	}

	protected Numbers imag() {
		return im;
	}

	protected Numbers conj() {
		return createComplex(real(this), neg(imag(this)));
	}

	
	
}
