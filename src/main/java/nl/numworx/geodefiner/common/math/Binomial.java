package nl.numworx.geodefiner.common.math;

import fi.euclides.expr.Som;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.math.Numbers;
import fi.wiskopdr.expressies.Bin;

public class Binomial extends Som {

	public Binomial() {
		super("binomial");
	}

	@Override
	public String getSymbolicValue(Label l) {
		Destroyable d[] = l.getDepend();
		return string + "(" + s(d[0]) + "," + s(d[1]) + ")";
	}

	@Override
	protected void recalc(Label l, Label[] labels) {
		int a = (int)Math.round(labels[0].value.doubleValue());
		int b = (int)Math.round(labels[1].value.doubleValue());
		Numbers value = Numbers.createDouble(Bin.binom(a,b));
		setStringValue(l, value);
	}


}
