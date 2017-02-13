package fi.euclides.math;

import fi.euclides.model.math.DefaultFactory;
import fi.euclides.model.math.Exact;
import fi.euclides.model.math.ExactFactory;
import fi.euclides.model.math.Numbers;

public class IntegerFactory implements ExactFactory {

	final IntegerValue ZERO = new IntegerValue(0);
	final IntegerValue ONE = new IntegerValue(1);
	final IntegerValue TWO = new IntegerValue(2);

	public static long getGCD(long m, long n)
	{
		long hlp;
		if (m < 0)
			m = -m;
		if (n < 0)
			n = -n;
		if (m < n)
		{
			hlp = n;
			n = m;
			m = hlp;
		}
		if (n == 0)
			return m;
		hlp = m % n;
		if (hlp == 0)
			return n;
		else
			return getGCD(n, hlp);

	}
	
	public IntegerFactory() {
	}

	@Override
	public Exact getZero() {
		return ZERO;
	}

	@Override
	public Numbers getOne() {
		return ONE;
	}

	@Override
	public Numbers getTwo() {
		return TWO;
	}

	@Override
	public Numbers getPi() {
		return DefaultFactory.INSTANCE.getPi();
	}

	@Override
	public Numbers createInteger(int value) {
		switch(value) {
		case 0: return ZERO;
		case 1: return ONE;
		case 2: return TWO;
		default:
			return new IntegerValue(value);
		}
	}

	@Override
	public Numbers createRational(long teller, long noemer) {	
		if(noemer != 1L) {
			long gcd = getGCD(teller, noemer);
			teller /= gcd;
			noemer /= gcd;
			if(noemer <= -1) { teller = -teller; noemer = -noemer; }
		}
		if(noemer == 1 && teller <= Integer.MAX_VALUE && teller >= Integer.MIN_VALUE)
			return createInteger( (int) teller);
		return DefaultFactory.INSTANCE.createRational(teller, noemer);
	}
	
	private Numbers add(IntegerValue a, IntegerValue b) {
		return createRational((long)a.value + (long)b.value, 1L);
	}
	private Numbers sub(IntegerValue a, IntegerValue b) {
		return createRational((long)a.value - (long)b.value, 1L);
	}
	private Numbers mul(IntegerValue a, IntegerValue b) {
		return createRational((long)a.value * (long)b.value, 1L);
	}
	private Numbers div(IntegerValue a, IntegerValue b) {
		return createRational((long)a.value , (long)b.value);
	}
	
	@Override
	public Numbers add(Exact a, Exact b) {
		if(a instanceof IntegerValue && b instanceof IntegerValue)
			return add( (IntegerValue)a, (IntegerValue)b);
		return DefaultFactory.INSTANCE.add(a, b);
	}

	@Override
	public Numbers sub(Exact a, Exact b) {
		if(a instanceof IntegerValue && b instanceof IntegerValue)
			return sub( (IntegerValue)a, (IntegerValue)b);
		return DefaultFactory.INSTANCE.sub(a, b);
	}

	@Override
	public Numbers mul(Exact a, Exact b) {
		if(a instanceof IntegerValue && b instanceof IntegerValue)
			return mul( (IntegerValue)a, (IntegerValue)b);
		return DefaultFactory.INSTANCE.mul(a, b);
	}

	@Override
	public Numbers div(Exact a, Exact b) {
		if(a instanceof IntegerValue && b instanceof IntegerValue)
			return div( (IntegerValue)a, (IntegerValue)b);
		return DefaultFactory.INSTANCE.div(a, b);
	}

	@Override
	public Numbers valueOf(String string) {
		return DefaultFactory.INSTANCE.valueOf(string);
	}

	@Override
	public String toString(Exact value) {
		return DefaultFactory.INSTANCE.toString(value);
	}

}
