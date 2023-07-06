package fi.euclides.expr;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.math.Numbers;
import fi.euclides.proof.LabelTester;

public class Logic1 extends LabelTester {

	private Logic1(String key) {
		super(key);
	}
	public static final Logic1 OR() { return new Logic1("or"); }
	public static final Logic1 AND() { 
		return new Logic1("and") {
			boolean isFalse(int a, int b)
			{
				return a == Label.FALSE || b == Label.FALSE;
			}
		};
	}
	
	public Destroyable[] createDepend() {
		return new Label[2];
	}

	public boolean define(Label l) {
		test(l);
		return true;
	}

	protected boolean test(Label l) {
		Label[] ll = (Label[]) l.getDepend();
		Label a = ll[0];
		Label b = ll[1];
		l.setString( s(a) + " " + string + " " + s(b) ) ;
		Numbers d = Numbers.ZERO;
		if ( isFalse(a.getState(), b.getState()))
			d = Numbers.ONE;
		return setState(l, d, 0.0);
	}

	boolean isFalse(int a, int b) {
		return a == Label.FALSE && b == Label.FALSE;
	}


}
