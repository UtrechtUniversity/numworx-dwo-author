package fi.euclides.expr;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.math.Numbers;
import fi.euclides.proof.LabelTester;

public class Not extends LabelTester {

	public  Not() {
		super("not");
	}
	
	public Destroyable[] createDepend() {
		return new Label[1];
	}

	public boolean define(Label l) {
		l.setString("not " + s(l.getDepend()[0]));
		return test(l);
	}

	protected boolean test(Label l) {
		Label org = (Label) l.getDepend()[0];
		Numbers value = Numbers.ONE;
		if(org.getState()==Label.FALSE)
			value = Numbers.ZERO;
		return setState(l, value, 0.0);
	}

}
