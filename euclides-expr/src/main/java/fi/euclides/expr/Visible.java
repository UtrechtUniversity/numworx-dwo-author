package fi.euclides.expr;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.math.Numbers;
import fi.euclides.proof.LabelDelegate;
import fi.euclides.proof.LabelTester;

public class Visible extends LabelTester {

	public static final String TYPE = "visible";

	public Visible() {
		super(TYPE);
	}

	@Override
	public Destroyable[] createDepend() {
		return new Destroyable[2];
	}

	@Override
	protected boolean test(Label l) {
		Label v = (Label) l.getDepend()[1];
		boolean visible = v.getState() != Label.FALSE;
		l.getDepend()[0].setVisible(visible);
		Numbers value = visible ? Numbers.ZERO : Numbers.ONE;
		l.setValue(value);
		l.setState(visible ? Label.EXACT : Label.FALSE);
		l.notifyObservers();
		return true;
	}

	@Override
	public boolean define(Label l) {
		l.register(this);
		l.getDepend()[1].addObserver(l);
		return test(l);
	}

}
