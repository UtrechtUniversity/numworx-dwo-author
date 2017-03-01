package fi.euclides.expr;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.math.Numbers;
import fi.euclides.proof.LabelValue;
import fi.euclides.util.DefaultAdapter;

public class Transc1 extends Som {

	private Transc1(String fun) {
		super(fun);
	}

	// TODO ARC- varianten, -H varianten, SCS, COT, SEC varianten.
	public static final LabelValue COS = new Transc1("cos");
	public static final LabelValue SIN = new Transc1("sin");
	public static final LabelValue TAN = new Transc1("tan");
	public static final LabelValue ARCCOS = new Transc1("arccos");
	public static final LabelValue ARCSIN = new Transc1("arcsin");
	public static final LabelValue ARCTAN = new Transc1("arctan");
	public static final LabelValue CSC = new Transc1("csc");
	public static final LabelValue SEC = new Transc1("sec");
	public static final LabelValue COT = new Transc1("cot");
	public static final LabelValue EXP = new Transc1("exp");
	public static final LabelValue LN = new Transc1("ln");
	public static final LabelValue ABS = new Transc1("abs"); // Arith1!
	public static final LabelValue CONJ = new Transc1("conjugate"); // complex1
	public static final LabelValue REAL = new Transc1("real");
	public static final LabelValue IMAG = new Transc1("imaginary");
	public static final LabelValue ARGUMENT = new Transc1("argument");
	public static final LabelValue LOG = new Transc1("log") {

		@Override
		public String getSymbolicValue(Label l) {
			Destroyable[] depend = l.getDepend();
			return  string + "("+ s(depend[0]) + "," + s(depend[1])  + ")";
		}

		@Override
		public Destroyable[] createDepend() {
			return new Label[2];
		}

		@Override
		Numbers eval(Label[] ll) {
			Numbers base; 
			Numbers exp; 
			if(ll[1] == null) { // een argument is base 10
				exp  = ll[0].value;
				return Numbers.createDouble(Math.log10(exp.doubleValue()));
			} else { // twee argumenten
				base = ll[0].value;
				exp = ll[1].value;
			}
			return Numbers.createDouble(Math.log(exp.doubleValue()) / Math.log(base.doubleValue()));
		}
		
	};
	
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
		if(this == ARCCOS)
			return Numbers.createDouble(Math.acos(v.doubleValue()));
		if(this == ARCSIN)
			return Numbers.createDouble(Math.asin(v.doubleValue()));
		if(this == ARCTAN)
			return Numbers.createDouble(Math.atan(v.doubleValue()));
		if(this == EXP)
			return Numbers.createDouble(Math.exp(v.doubleValue()));
		if(this == LN)
			return Numbers.createDouble(Math.log(v.doubleValue()));
		if(this == COT)
			return Numbers.createDouble(1.0/Math.tan(v.doubleValue()));
		if(this == CSC)
			return Numbers.createDouble(1.0/Math.sin(v.doubleValue()));
		if(this == SEC)
			return Numbers.createDouble(1.0/Math.cos(v.doubleValue()));		
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
			l.setString(hoekAsString(value));
		} else
			l.setString(Numbers.toString(value));	
		l.setValue(value);

	}

}
