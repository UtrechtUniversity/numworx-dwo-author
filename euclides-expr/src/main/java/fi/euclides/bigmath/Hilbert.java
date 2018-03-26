package fi.euclides.bigmath;

import java.io.IOException;

import fi.euclides.model.math.Exact;
import fi.euclides.model.math.NumberCodec;
import fi.euclides.model.math.Numbers;


public class Hilbert extends Exact {

	private static String wortel = "\u221a";
	
	Rational value0;
	Exact    value1;
	boolean sign;
	
	/**
	 * Constructor voor \u221a a
	 * @param a
	 */
	public Hilbert(Exact a) {
		value1 = a;
		value0 = (Rational) Numbers.ZERO;
	}

	public String getValue() {
		char s = sign ? '-':'+';
		Rational r = (Rational)value0;
		if(value1 instanceof Rational)
		{
			Rational w = (Rational)value1;
			return r.getValue() + s + wortel + w.getValue();
		} else
		{
			Hilbert w = (Hilbert)value1;
			return r.getValue() + s + wortel + w.getValue();
		}
	}
	
	public void setValue(String v)
	{
		int r = v.indexOf(wortel);
		String substring = v.substring(0, r-1);
		if("0/1".equals(substring))
		{
			value0 = (Rational) ZERO;
		} else {
			Rational x = new Rational();
			x.setValue(substring);
			value0 = x;
		}
		sign = v.charAt(r-1) == '-';
		v = v.substring(r+1);
		if(v.indexOf(wortel)>=0)
		{
			Hilbert w = new Hilbert(); w.setValue(v);
			value1 = w;
		} else {
			Rational w = new Rational(); w.setValue(v);
			value1 = w;
		}
	}
	
	
	
	/* FIXME length altijd 2
	 * (non-Javadoc)
	 * @see fi.euclides.model.math.Numbers#sqr()
	 */
	protected Numbers sqr() {
		if(value0==ZERO)
			return value1;
		Numbers sum = mul(mul(value0, TWO), new Hilbert(value1));
		if(sign) sum = neg(sum);
		return add(sum,add(Numbers.sqr(value0), value1));
	}

	/**
	 * 
	 */
	public Hilbert() {
		value1 = value0 = (Rational) ZERO;
	}

	public double doubleValue() {
		double sqrt = Math.sqrt(value1.doubleValue());
		if(sign) sqrt = -sqrt;
		sqrt += value0.doubleValue();
		return sqrt;
	}

	public String toString() {
		String string = wortel + value1;
		if(sign) string = "-"+string;
		if(value0!=ZERO)
		{
			if(!sign) string = "+"+string;
			string = value0 + string;
		}
		return string;
	}

	public Numbers mul(Rational b) {
		Hilbert r = new Hilbert((Exact) mul(value1, Numbers.sqr(b)));
		if(b.teller.signum()<0 != sign)
			r.sign = true;
		r.value0 = (Rational) mul(value0,b);
		return r;
	}

	public Numbers add(Rational b) {
		Hilbert r = new Hilbert(value1);
		r.sign = sign;
		r.value0=(Rational) add(value0, b);
		return r;
	}
	public Numbers sub(Rational b) {
		Hilbert r = new Hilbert(value1);
		r.sign = sign;
		r.value0=(Rational) sub(value0, b);
		return r;
	}
	public Numbers add(Hilbert b)
	{
		if(!(value1 instanceof Rational))
			return Numbers.add(createDouble(doubleValue()), b);
		if(!(b.value1 instanceof Rational))
			return Numbers.add(createDouble(b.doubleValue()), this);
			
			
		Numbers r = add(value0, b.value0);
		Numbers wr;
		if(sign == b.sign)
		{
			wr = add(value1, b.value1);
			Numbers wrr = mul(value1, b.value1);
			wrr = sqrt(wrr);
			wrr = mul(TWO, wrr);
			wr = add(wr, wrr);
			wr = sqrt(wr);
			if(sign) wr = neg(wr);
			r = add(r, wr);
			return r;
		}
		wr = sub(value1, b.value1);
		if(ZERO==wr)
			return r;
		boolean s = Numbers.signum(wr)<0;
		wr = add(value1, b.value1);
		Numbers wrr = mul(value1, b.value1);
		wrr = sqrt(wrr);
		wrr = mul(TWO, wrr);
		wr = sub(wr, wrr);
		r = s==sign ? add(r, sqrt(wr)) : sub(r, sqrt(wr));
		return r;
			
	}
	
	
	/* (non-Javadoc)
	 * @see fi.euclides.model.math.Numbers#neg()
	 */
	protected Numbers neg() {
		Hilbert h = new Hilbert(value1);
		h.sign = !sign;
		h.value0 = (Rational) neg(value0);
		return h;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (sign ? 1231 : 1237);
		result = prime * result + ((value0 == null) ? 0 : value0.hashCode());
		result = prime * result + ((value1 == null) ? 0 : value1.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Hilbert other = (Hilbert) obj;
		if (sign != other.sign)
			return false;
		if (value0 == null) {
			if (other.value0 != null)
				return false;
		} else if (!value0.equals(other.value0))
			return false;
		if (value1 == null) {
			if (other.value1 != null)
				return false;
		} else if (!value1.equals(other.value1))
			return false;
		return true;
	}

	public Numbers mul(Hilbert b) {
		if(value0!=ZERO || b.value0!=ZERO)
			return Numbers.createDouble(doubleValue()*b.doubleValue());		
//		(a + wb) (x + wy) = ax + awy + xwb + wbwy = ax + w(a2y) + w(x2b) + w(by)
		
		Numbers r = mul(value1, b.value1);
		r = sqrt(r);
		if(b.sign != sign) r = neg(r);
		return r;
	}

	public Numbers invers() {
		if(value0!=ZERO || !(value1 instanceof Rational))
			return Numbers.createDouble(1/doubleValue());
		Rational r1 = (Rational) value1;
		Hilbert r= new Hilbert(r1.invers());
		r.sign = sign;
		// TODO Auto-generated method stub
		return r;
	}

	protected int signum() {
		int s = value0.signum();
		if(sign && s < 0 )
			return -1;
		if(!sign && s >=0)
			return +1;
		Numbers r = sub(Numbers.sqr(value0), value1);
		s = Numbers.signum(r);
		if(sign)
			return s;
		return -s;
	}

	protected Numbers sqrt() {
		return new Hilbert(this);
	}

	public void writeNumber(NumberCodec memento) throws IOException {
		memento.writeHilbert(value0, sign, value1);
	}

}
