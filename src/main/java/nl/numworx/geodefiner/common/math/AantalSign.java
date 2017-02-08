package nl.numworx.geodefiner.common.math;

import fi.euclides.expr.Som;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.math.Numbers;

public class AantalSign extends Som {

	AantalSign() {
		super("aantalSign");
	}

	@Override
	public Destroyable[] createDepend() {
		return new Label[1];
	}

	@Override
	protected void recalc(Label l, Label[] labels) {
		double d = labels[0].value.doubleValue();
		Numbers value = Numbers.createInteger(eval(d));
		setStringValue(l, value);
	}

	int eval(double d) {
		d = Math.abs(d);
		String test =  Double.toString(d);
		int E = test.indexOf('E');
		if(E > 0) test = test.substring(0,E);
		test = test.replace(".", "");
		while(test.length()>1 && test.startsWith("0")) test = test.substring(1);
		while(test.length()>1 && test.endsWith("0")) test = test.substring(0, test.length()-1);
		return (test.length());
	}

}
