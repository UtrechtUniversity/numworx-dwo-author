package fi.euclides.expr;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.math.Numbers;

public class Power extends Som {

	final Numbers HALF = Numbers.createRational(1, 2);
	private boolean isRoot;
	
	public static final Power POWER = new Power(false);
	public static final Power ROOT = new Power(true);
	
	private Power(boolean b)
	{
		super(b?"root":"^");
		isRoot = b;
	}
	
	private Numbers s(Numbers root, boolean isRoot)
	{
		return isRoot? Numbers.sqrt(root) : Numbers.sqr(root);
	}
	
	
	Numbers eval(Label[] ll) {
// super simpel, uitzondering sqr en sqrt en 1/sqr en 1/sqrt
		Numbers pow;
		if(ll[1] == null)
			pow = Numbers.TWO;
		else 
			pow= ll[1].value;
		Numbers root = ll[0].value;
		if(Numbers.ZERO.equals(Numbers.sub(pow, Numbers.TWO)))
		{
			return s(root, isRoot);
		}
		if(Numbers.ZERO.equals(Numbers.sub(pow, HALF)))
		{
			return s(root, !isRoot);
		}
		if(Numbers.ZERO.equals(Numbers.add(pow, Numbers.TWO)))
		{
			return Numbers.div(Numbers.ONE, s(root, isRoot));
		}
		if(Numbers.ZERO.equals(Numbers.add(pow, HALF)))
		{
			return Numbers.div(Numbers.ONE, s(root, !isRoot));
		}		
// TODO complex^complex
		if(root instanceof fi.euclides.model.math.Complex) {
			double abs = Numbers.abs(root).doubleValue();
			double arg = Math.atan2(Numbers.imag(root).doubleValue(), Numbers.real(root).doubleValue());
			if(isRoot) pow = Numbers.div(Numbers.ONE, pow);
			if(pow instanceof fi.euclides.model.math.Complex) 
			{
				double re = Numbers.real(pow).doubleValue();
				double im = Numbers.imag(pow).doubleValue();

				double r = Math.pow(abs, re) * Math.exp(-im*arg);
				double ang = Math.log(abs)*im + arg * re;
				Numbers Re = Numbers.createDouble(r*Math.cos(ang));
				Numbers Im = Numbers.createDouble(r*Math.sin(ang));
				return Numbers.createComplex(Re, Im);
				
// complex ^ complex = (r*e^(i*phi))^(re+i*im) =
// r^re * r^(i*im) * e^(i*phi*(re+i*im))
// r^re * r^(i*im) * e^(i*phi*re)* e^(-im*phi)
// r^re * e^(lnr*i*im) * e^(i*phi*re) * e^(-im*phi)
// r^re * e^(-im*phi) * e^(i* (im*lnr + phi*re))

			} else {
// complex ^ real
				double re = (pow).doubleValue();
				double im = 0.0;

				double r = Math.pow(abs, re) /* * Math.exp(-im*arg)*/;
				double ang = /* Math.log(abs)*im + */ arg * re;
				Numbers Re = Numbers.createDouble(r*Math.cos(ang));
				Numbers Im = Numbers.createDouble(r*Math.sin(ang));
				return Numbers.createComplex(Re, Im);

			
			}
		} else if (pow instanceof fi.euclides.model.math.Complex)
		{
// real ^ complex
			double abs = Numbers.abs(root).doubleValue();
			double arg = Math.atan2(Numbers.imag(root).doubleValue(), Numbers.real(root).doubleValue());
			if(isRoot) pow = Numbers.div(Numbers.ONE, pow);
			double re = Numbers.real(pow).doubleValue();
			double im = Numbers.imag(pow).doubleValue();

			double r = Math.pow(abs, re) * Math.exp(-im*arg);
			double ang = Math.log(abs)*im + arg * re;
			Numbers Re = Numbers.createDouble(r*Math.cos(ang));
			Numbers Im = Numbers.createDouble(r*Math.sin(ang));
			return Numbers.createComplex(Re, Im);
		} else {		
			double r = root.doubleValue();
			double p    = pow.doubleValue();
			if(isRoot) p = 1.0/p;
			double result = Math.pow(r, p);
			return Numbers.createDouble(result);
		}
	}

	public String getSymbolicValue(Label l) {
		if(isRoot)
		{
			Destroyable[] ll = l.getDepend();
			if(ll[1]== null)
			{
				return "\u221a(" + s(ll[0]) + ")";
			}
			return "root(" + s(ll[0])  + "," + s(ll[1])+")";
			
		}
		return super.getSymbolicValue(l);
	}


}
