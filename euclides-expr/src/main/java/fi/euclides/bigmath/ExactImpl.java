package fi.euclides.bigmath;

import fi.euclides.model.math.DefaultFactory;
import fi.euclides.model.math.DoubleFormat;
import fi.euclides.model.math.Exact;
import fi.euclides.model.math.ExactFactory;
import fi.euclides.model.math.Numbers;

public class ExactImpl implements ExactFactory {

	private static final Rational ONE = new Rational(1,1);
	private static final Rational TWO = new Rational(2,1);
	private static final Rational ZERO = new Rational(0,1);

	public Numbers getOne() {
		return ONE;
	}

	public Numbers getTwo() {
		return TWO;
	}

	public Exact getZero() {
		return ZERO;
	}

	public Numbers getPi() {
		return DefaultFactory.INSTANCE.getPi();
	}
	
	public Numbers createInteger(int value) {
		return new Rational(value,1);
	}

	public Numbers createRational(long teller, long noemer) {
		return new Rational(teller, noemer);
	}

	public Numbers add(Exact a, Exact b) {
		if(a instanceof Rational && b instanceof Rational)
			return ((Rational)a).add((Rational) b);
		if(a instanceof Hilbert && b instanceof Rational)
			return ((Hilbert)a).add((Rational) b);
		if(a instanceof Rational && b instanceof Hilbert)
			return ((Hilbert)b).add((Rational) a);
		if(a instanceof Hilbert && b instanceof Hilbert)
			return ((Hilbert)a).add((Hilbert) b);
		return Numbers.createDouble(a.doubleValue()+b.doubleValue());
	}

	public Numbers sub(Exact a, Exact b) {
		if(a instanceof Rational && b instanceof Rational)
			return ((Rational)a).sub((Rational) b);
		if(a instanceof Hilbert && b instanceof Rational)
			return ((Hilbert)a).sub((Rational) b);
		return Numbers.add(a, Numbers.neg(b));
	}

	public Numbers div(Exact a, Exact b) {
		if(a instanceof Rational && b instanceof Rational)
			return ((Rational)a).div((Rational) b);
		if(a instanceof Hilbert && b instanceof Rational)
			return ((Hilbert)a).mul(((Rational) b).invers());
		if(b instanceof Hilbert)
			return Numbers.mul(a,((Hilbert)b).invers());
		return Numbers.createDouble(a.doubleValue()/b.doubleValue());
	}

	public Numbers mul(Exact a, Exact b) {
		if(a instanceof Rational && b instanceof Rational)
			return ((Rational)a).mul((Rational) b);
		if(a instanceof Hilbert && b instanceof Rational)
			return ((Hilbert)a).mul((Rational) b);
		if(a instanceof Rational && b instanceof Hilbert)
			return ((Hilbert)b).mul((Rational) a);
		if(a instanceof Hilbert && b instanceof Hilbert)
			return ((Hilbert)a).mul((Hilbert) b);
		return Numbers.createDouble(a.doubleValue()*b.doubleValue());
	}
	
	public String getValue(Exact value) {
		if(value instanceof Rational)
			return ((Rational)value).getValue(); 
		if(value instanceof Hilbert)
				return ((Hilbert)value).getValue(); 
		return value.toString();
	}
	
	public String toString(Exact value) {
		return value.toString();
	}

	public Numbers valueOf(String string) {
		try {
			Rational r;
			r = new Rational();
			r.setValue(string);
			return r;
		} catch (RuntimeException e) {
			Numbers r = DoubleFormat.valueOf(string);
			if(r == null)
				r = Numbers.createDouble(Double.parseDouble(string));
			return r;
		}
		
	}

}
