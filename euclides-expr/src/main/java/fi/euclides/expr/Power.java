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
		double r = root.doubleValue();
		double p    = pow.doubleValue();
		if(isRoot) p = 1.0/p;
		double result = Math.pow(r, p);
		return Numbers.createDouble(result);
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
