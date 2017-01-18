package fi.euclides.model.math;

import java.io.IOException;

public abstract class Numbers {
	
	static ExactFactory factory = DefaultFactory.INSTANCE;

	public static  Exact ZERO = factory.getZero();
	public static  Numbers ONE = factory.getOne();
	public static  Numbers TWO = factory.getTwo();
	public static  Numbers PI = factory.getPi();
	public static void setFactory(ExactFactory factory)
	{
		Numbers.factory = factory;
		ZERO = factory.getZero();
		ONE  = factory.getOne();
		TWO  = factory.getTwo();
		PI   = factory.getPi();
	}
	
	public static ExactFactory getFactory() { 
		return factory;
	}
	
	public abstract double doubleValue();
	public abstract String toString();
	
	public static Numbers createDouble(double value)
	{
		return new FloatingPoint(value);
	}
		
	public static Numbers createInteger(int value)
	{	
		switch (value) {
			case 0: return ZERO;
			case 1: return ONE;
			case 2: return TWO;
			default: return factory.createInteger(value);
		}
	}
	
	public static Numbers createRational(long teller, long noemer)
	{
		if(noemer != 0)
		{
			if(teller == 0)
				return ZERO;
			if(teller == noemer)
				return ONE;
		}
		return factory.createRational(teller, noemer);
	}
		
	public static Numbers add(Numbers a, Numbers b)
	{
		if(a == ZERO)
			return b;
		if(b == ZERO)
			return a;
		if(a instanceof Exact && b instanceof Exact)
			return factory.add((Exact)a,(Exact)b);
		if(a instanceof Complex)
			return ((Complex)a).add(b);
		if(b instanceof Complex)
			return ((Complex)b).add(a);
		return new FloatingPoint(a.doubleValue()+b.doubleValue());
	}

	public static Numbers sub(Numbers a, Numbers b)
	{
		if(a == ZERO)
			return neg(b);
		if(b == ZERO)
			return a;
		if(a instanceof Complex || b instanceof Complex)
			return add(a, b.neg());
		if(a instanceof Exact && b instanceof Exact)
			return factory.sub((Exact)a,(Exact)b);
		return new FloatingPoint(a.doubleValue()-b.doubleValue());
	}
	
	public static Numbers neg(Numbers b) {
		if(b == ZERO) return b;
		return b.neg();
	}
	
	protected Numbers neg() {
		return new FloatingPoint(-doubleValue());
	}

	public static Numbers mul(Numbers a, Numbers b)
	{
		if(a == ZERO  || b == ZERO)
			return ZERO;
		if(b == ONE)
			return a;
		if(a == ONE)
			return b;
		if(a instanceof Complex)
			return ((Complex)a).mul(b);
		if(b instanceof Complex)
			return ((Complex)b).mul(a);
		if(a instanceof Exact && b instanceof Exact)
			return factory.mul((Exact)a, (Exact)b);
		
		return new FloatingPoint(a.doubleValue()*b.doubleValue());
	}

	public static Numbers div(Numbers a, Numbers b)
	{
		if(a == ZERO)
			return ZERO;
		if(b == ONE)
			return a;
		if(a instanceof Complex)
			return ((Complex)a).div(b);
		if(b instanceof Complex)
			return new Complex(a, ZERO).div(b);
		if(a instanceof Exact && b instanceof Exact)
			return factory.div((Exact)a, (Exact)b);
		return new FloatingPoint(a.doubleValue()/b.doubleValue());
	}

	public static Numbers sqrt(Numbers a)
	{
		if(a == ZERO)
			return ZERO;
		if(a == ONE)
			return ONE;
		return a.sqrt();
	}
	
	abstract Numbers sqrt();

	/**
	 * Net echt OO, maar is het in overeenkomst met readNumber.
	 * @param memento NumberCodec
	 * @throws IOException
	 */
	
	public void writeNumber(NumberCodec memento) throws IOException {
			memento.writeDouble(this);
		}
	
	/** 
	 * Square.
	 * @param y
	 * @return y*y
	 */
	public static Numbers sqr(Numbers y) {
		return y.sqr();
	}

	protected Numbers sqr() {
		return Numbers.mul(this, this);
	}
	
	public static Numbers hypot(Numbers dx1, Numbers dy1) {
		return Numbers.sqrt(Numbers.add(dx1.sqr(), dy1.sqr()));
	}
	
	abstract protected int signum();
	
	public static int signum(Numbers x)
	{
		return x.signum();
	}
	
	protected Numbers abs()
	{
		if(signum()<0)
			return neg();
		else
			return this;
	}
	
	
	public static Numbers abs(Numbers x)
	{
		return x.abs();
	}

	public static Numbers valueOf(String string) {
		return factory.valueOf(string);
	}

	public static String toString(Exact value) {
		return factory.toString(value);
	}
		
	public static String toString(Numbers value) {
		if(value instanceof Complex)
			return value.toString();
		if(value instanceof Exact)
			return toString( (Exact)value);
		return DoubleFormat.toString(value);	
	}

	public static Numbers createComplex(Numbers re, Numbers im) {
		if(ZERO == (im))
			return re;
		if(re instanceof Complex || im instanceof Complex)
		{
			return add(re, mul(createComplex(Numbers.ZERO, Numbers.ONE), im));
		}
		return new Complex(re, im);
	}
	
	// Complex functions
	protected Numbers real()
	{
		return this;
	}
	protected Numbers imag()
	{
		return Numbers.ZERO;
	}
	protected Numbers conj()
	{
		return this;
	}
	// public Complex functions
	
	public static Numbers real(Numbers f)
	{
		return f.real();
	}
	
	public static Numbers imag(Numbers f)
	{
		return f.imag();
	}
	
	public static Numbers conj(Numbers f)
	{
		return f.conj();
	}

	public static Numbers round(Numbers value) {
		return value.round();
	}

	protected Numbers round() {
		return createDouble(Math.round(doubleValue()));
	}

	public static Numbers floor(Numbers value) {
		return value.floor();
	}

	protected Numbers floor() {
		return createDouble(Math.floor(doubleValue()));
	}

	public static Numbers ceiling(Numbers value) {
		return value.ceiling();
	}

	protected Numbers ceiling() {
		return createDouble(Math.ceil(doubleValue()));
	}
	
}
