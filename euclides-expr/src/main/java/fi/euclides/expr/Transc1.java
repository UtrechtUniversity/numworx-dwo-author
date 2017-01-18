package fi.euclides.expr;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.DefaultAdapter;
import fi.euclides.util.JMath;

public class Transc1 extends Som {

	private Transc1(String fun) {
		super(fun);
	}

	// TODO ARC- varianten, -H varianten, SCS, COT, SEC varianten.
	public static final Transc1 COS = new Transc1("cos");
	public static final Transc1 SIN = new Transc1("sin");
	public static final Transc1 TAN = new Transc1("tan");
	public static final Transc1 EXP = new Transc1("exp");
	public static final Transc1 LN = new Transc1("ln");
	public static final Transc1 ABS = new Transc1("abs"); // Arith1!
	public static final Transc1 CONJ = new Transc1("conjugate"); // complex1
	public static final Transc1 REAL = new Transc1("real");
	public static final Transc1 IMAG = new Transc1("imaginary");
	public static final Transc1 ARGUMENT = new Transc1("argument");
	
	public String getSymbolicValue(Label l) {
		return  string + "("+ s(l.getDepend()[0])  + ")";
	}

	
	public Destroyable[] createDepend() {
		return new Label[1];
	}

	Numbers eval(Label[] ll) {
		Numbers v = ll[0].value;
		if(this == COS)
			return Numbers.createDouble(Math.cos(v.doubleValue()));
		if(this == SIN)
			return Numbers.createDouble(Math.sin(v.doubleValue()));
		if(this == TAN)
			return Numbers.createDouble(Math.tan(v.doubleValue()));
		if(this == EXP)
			return Numbers.createDouble(Math.exp(v.doubleValue()));
		if(this == LN)
			return Numbers.createDouble(Math.log(v.doubleValue()));
		if(this == ABS)
			return Numbers.abs(v);
		if(this == CONJ)
			return Numbers.conj(v);
		if(this == REAL)
			return Numbers.real(v);
		if(this == IMAG)
			return Numbers.imag(v);
		if(this == ARGUMENT)
// let op, uitkomst is een HOEK
			return Numbers.createDouble(Math.atan2(Numbers.imag(v).doubleValue(), Numbers.real(v).doubleValue()));
		throw new RuntimeException("No such method");
	}


	protected void recalc(Label l, Label[] ll) {
		Label l0 = ll[0];
		if(isHoek(l0) && (this==TAN||this==COS||this==SIN))
		{
			if(l0.getAdapter().adapt(Numbers[].class) == null)
				l0.getRegistered().define(l0); // anders h = null
			Numbers[] h = l0.getAdapter().adapt(Numbers[].class);
			Numbers c = h[0];
			Numbers s = h[1];
			Numbers value;
			if(this == TAN)
			{
				value = Numbers.div(s,c);
			} else {
				Numbers hyp = Numbers.hypot(s, c);
				if( this == COS) value = Numbers.div(c,hyp);
				else value = Numbers.div(s, hyp);
			}
			setStringValue(l,value);
			return;
		}
		
		Numbers value = eval(ll);
		if(this == ARGUMENT)
		{
			Numbers c = ll[0].value;
			DefaultAdapter.getDefault(l).put( new Numbers[] { Numbers.real(c), Numbers.imag(c) });
			l.setState(Label.HOEK);
			l.setString(JMath.round(value.doubleValue() * 180.0 / Math.PI)%360 + "°");
		} else
			l.setString(Numbers.toString(value));	
		l.setValue(value);

	}

}
