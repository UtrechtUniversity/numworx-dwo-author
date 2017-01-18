package fi.euclides.expr;

import fi.euclides.model.Label;
import fi.euclides.model.math.Numbers;

public class Complex extends Som {


	private Complex() {
		super("|");
	}
	public static final Complex INSTANCE = new Complex();

	Numbers eval(Label[] ll) {
		return Numbers.createComplex( ll[0].value, ll[1].value);
	}

	protected void recalc(Label l, Label[] labels) {
		Numbers value;
		value = eval(labels);
		setStringValue(l,value);
	}
	
	
}
