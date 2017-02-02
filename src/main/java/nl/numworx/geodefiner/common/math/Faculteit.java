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
		double antw = 1;
		for(long i=w ; i>0 ; i--) antw = antw*i;
		value = Numbers.createDouble(antw);
		setStringValue(l,value);
	}

}
