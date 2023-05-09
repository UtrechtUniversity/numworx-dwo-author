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
		int a = (int)Numbers.round(labels[0].value).longValue();
		int b = (int)Numbers.round(labels[1].value).longValue();
		Numbers value = Numbers.createDouble(Bin.binom(a,b));
		setStringValue(l, value);
	}


}
