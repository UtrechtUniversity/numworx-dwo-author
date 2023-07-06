package fi.euclides.bigmath;

import java.io.IOException;
import java.math.BigInteger;

import fi.euclides.model.math.Exact;
import fi.euclides.model.math.NumberCodec;
import fi.euclides.model.math.Numbers;

public class Rational extends Exact {

	BigInteger teller, noemer;
	
	/**
	 * @param teller
	 * @param noemer
	 */
	public Rational(BigInteger teller, BigInteger noemer) {
		if(noemer.signum()<0)
		{
			teller = teller.negate();
			noemer = noemer.negate();
		}
		BigInteger ggd= teller.abs().gcd(noemer);
		this.teller = teller.divide(ggd);
		this.noemer = noemer.divide(ggd);
	}

	private long ggd(long a, long b)
	{
	  while(true) {
		if(a == 0)
			return b;
		if(b == 0)
			return a;
		if(a > b)
		{	long t = a-b*(a/b);
			a = b;
			b = t;
			continue;
		}
		if(b > a)
		{	b = b-a*(b/a);
			continue;
		}
		return a;
	  }
	  
	}
	
	public Rational(long teller, long noemer) {
		if(noemer < 0 )
		{
			teller = -teller;
			noemer = -noemer;
		}
		long g = ggd(Math.abs(teller), noemer);
			
		this.teller = BigInteger.valueOf(teller/g);
		this.noemer = BigInteger.valueOf(noemer/g);
	}

	public double doubleValue() {
		return teller.doubleValue() / noemer.doubleValue();
	}

	public String toString() {
		BigInteger t = teller;
		if(noemer.equals(BigInteger.ONE))
			return String.valueOf(t);
		
		t = t.abs();
		if(t.compareTo(noemer)<0)
			return teller + "/" + noemer;
		return (teller.divide(noemer)) + " " + (t.remainder(noemer)) +"/" + noemer;
	}

	/**
	 * 
	 */
	public Rational() {
		super();
//		teller = BigInteger.ZERO;
//		noemer = BigInteger.ONE;
	}
	
	public Rational(BigInteger val) {
		this(val, BigInteger.ONE);
	}

	public String getValue() {
		return teller + "/" + noemer;
	}
	public void setValue(String value) {
		int slash = value.indexOf('/');
		if(slash < 0)
		{
			teller = new BigInteger(value);
			noemer = BigInteger.ONE;
			return;
		}
		teller = new BigInteger(value.substring(0,slash));
		noemer = new BigInteger(value.substring(slash+1));
	}
	
	

	Numbers add(Rational b) {
		BigInteger t = teller.multiply(b.noemer).add(b.teller.multiply(noemer));
		BigInteger n = noemer.multiply(b.noemer);
		return create(t,n);
		//return Numbers.createRational(teller*b.noemer + b.teller*noemer, noemer*b.noemer);
	}

	Numbers sub(Rational b) {
		BigInteger t = teller.multiply(b.noemer).subtract(b.teller.multiply(noemer));
		BigInteger n = noemer.multiply(b.noemer);
		return create(t,n);
		//return Numbers.createRational(teller*b.noemer - b.teller*noemer, noemer*b.noemer);
	}

	Numbers mul(Rational b) {
		return create(teller.multiply(b.teller), noemer.multiply(b.noemer));
	}
	
	Numbers div(Rational b) {
		if(b.teller .equals(BigInteger.ZERO))
			return Numbers.createDouble(teller.doubleValue()/0.0);
		
		BigInteger t = teller.multiply(b.noemer);
		BigInteger n = noemer.multiply(b.teller);
		return create(t, n);
	}

	/**
	 * @param t
	 * @param n
	 * @return
	 */
	private Numbers create(BigInteger t, BigInteger n) {
		if(t.equals(n))
			return ONE;
		if(t.signum()==0)
			return ZERO;
		return new Rational(t, n);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((noemer == null) ? 0 : noemer.hashCode());
		result = prime * result + ((teller == null) ? 0 : teller.hashCode());
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
		final Rational other = (Rational) obj;
		if (noemer == null) {
			if (other.noemer != null)
				return false;
		} else if (!noemer.equals(other.noemer))
			return false;
		if (teller == null) {
			if (other.teller != null)
				return false;
		} else if (!teller.equals(other.teller))
			return false;
		return true;
	}

	public Numbers neg() {
		return new Rational(teller.negate(), noemer);
	}

	/* (non-Javadoc)
	 * @see fi.euclides.model.math.Numbers#sqrt()
	 */
	protected Numbers sqrt() {
		BigInteger wt = sqrt(teller);
		if(wt != null)
		{
			BigInteger wn = sqrt(noemer);
			if(wn != null)
				return create(wt,wn);
		}
		
		return new Hilbert(this);
	}

	/**
	 * bereken de exacte wortel van x.
	 * @param x
	 * @return null of sqrt(x)
	 */
	
	private BigInteger sqrt(BigInteger x) {
		BigInteger result = BigIntegerMath.sqrt(x);
//		double t = Math.sqrt(x.doubleValue());
//		long   l = Math.round(t);
//		BigInteger result = BigInteger.valueOf(l);
		if(x.equals(result.multiply(result)))
			return result;
		return null;
	}

	public Rational invers() {
		return new Rational(noemer, teller);
	}

	protected int signum() {
		return teller.signum();
	}

	public void writeNumber(NumberCodec dos) throws IOException {
		if(teller.equals(BigInteger.ZERO))
			dos.writeZero();
		else
		if(noemer.equals(BigInteger.ONE) && teller.bitLength()<=31)  // Fits?
		{
			dos.writeInteger(teller.intValue());
		}
		else if(teller.bitLength() <= 63 && noemer.bitLength() <= 63)
			dos.writeRational(teller.longValue(), noemer.longValue());
		else
			
		{
			dos.writeRational(teller.toString(),noemer.toString());
		}
	}
	
}
