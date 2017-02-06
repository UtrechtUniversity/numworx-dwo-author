package nl.numworx.geodefiner.common.math;

import fi.euclides.expr.Som;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.math.Numbers;

public class Faculteit extends Som {

	public Faculteit() {
		super("!");
	}

	@Override
	public Destroyable[] createDepend() {
		return new Label[1];
	}

	@Override
	public String getSymbolicValue(Label l) {
		return "(" + s(l.getDepend()[0]) + ")!";
	}

	@Override
	protected void recalc(Label l, Label[] labels) {
		Numbers value = labels[0].value;
		long w = Math.round(value.doubleValue());
		if(w >= 0 && w < 100) {
			double antw = 1;
			for(long i=w ; i>0 ; i--) antw = antw*i;
			value = Numbers.createDouble(antw);
		} else // tot 171 daarna infinity
			value = Numbers.createDouble(gamma(w+1));
		setStringValue(l,value);
	}

	public static double logGamma(double x) {
		double tmp = (x - 0.5) * Math.log(x + 4.5) - (x + 4.5);
		double ser = 1.0 + 76.18009173 / (x + 0) - 86.50532033 / (x + 1)
				+ 24.01409822 / (x + 2) - 1.231739516 / (x + 3) + 0.00120858003
				/ (x + 4) - 0.00000536382 / (x + 5);
		return tmp + Math.log(ser * Math.sqrt(2 * Math.PI));
	}

	public static double gamma(double x) {
		return Math.exp(logGamma(x));
	}
}
