package nl.numworx.geodefiner.common.math;

import fi.euclides.expr.Som;
import fi.euclides.model.Label;
import fi.euclides.model.math.DoubleFormat;
import fi.euclides.model.math.Numbers;

public class Rnq extends Som {

	public Rnq() {
		super("rnq");
	}

	@Override
	public String getSymbolicValue(Label l) {
		return "rnq("+s(l.getDepend()[0]) +"," + s(l.getDepend()[1]) + ")";
	}

	@Override
	protected void recalc(Label l, Label[] labels) {
		double x = labels[0].value.doubleValue();
		Numbers value;
		if(labels[1] != null)
			value = labels[1].value;
		else
			value = Numbers.ZERO;
		int y = Numbers.round(value).intValue();
		x = Rnd.roundToSignificantFigures(x, y);
		Numbers n;
		if(y > 0 || Math.abs(x)>= Integer.MAX_VALUE)
			n = Numbers.createDouble(x);
		else
			n = Numbers.createRational(Math.round(x),1);
		if(l.isDefined())
			l.setString(toString(x, y));
		else
			l.setString("");
		l.setValue(n);
		
	}
	
	private static String toString(double x, int y) {
		DoubleFormat.setMaximumFractionDigits(Math.max(0,y));
		String s = DoubleFormat.toString(Numbers.createDouble(x));
		DoubleFormat.setMaximumFractionDigits(DoubleFormat.DEFAULT); // DEFAULT
		return s;
	}


}
