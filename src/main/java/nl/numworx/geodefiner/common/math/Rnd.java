package nl.numworx.geodefiner.common.math;


import fi.euclides.expr.Som;
import fi.euclides.model.Label;
import fi.euclides.model.math.DoubleFormat;
import fi.euclides.model.math.Numbers;

public class Rnd extends Som {

	public Rnd() {
		super("rnd");
	}

	@Override
	public String getSymbolicValue(Label l) {
		return "rnd("+s(l.getDepend()[0]) +"," + s(l.getDepend()[1]) + ")";
	}

	@Override
	protected void recalc(Label l, Label[] labels) {
		double x = labels[0].value.doubleValue();
		int y = (int) Numbers.round(labels[1].value).longValue();
		x = roundToSignificantFigures(x, Math.abs(y));
		Numbers n = Numbers.createDouble(x);
		if(l.isDefined())
			l.setString(toString(x, y));
		else
			l.setString("");
		l.setValue(n);
		
	}
	
	private String toString(double x, int y) {
		DoubleFormat.setMaximumFractionDigits(y);
		String s = DoubleFormat.toString(Numbers.createDouble(x));
		DoubleFormat.setMaximumFractionDigits(DoubleFormat.DEFAULT); // DEFAULT
		return s;
	}

	private static double roundToSignificantFigures(double num, int n) {
	    if(num == 0) {
	        return 0;
	    }
	    //final double d = Math.ceil(Math.log10(num < 0 ? -num: num));
	    final int power = n ; // - (int) d;

	    final double magnitude = Math.pow(10, power);
	    final long shifted = Math.round(num*magnitude);
	    return shifted/magnitude;
	}


}
