package nl.numworx.geodefiner.common.math;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.math.Numbers;
import fi.euclides.proof.LabelTester;
import fi.euclides.util.Observable;

class Exists extends LabelTester {

	Exists() {
		super("exists");
	}

	@Override
	public Destroyable[] createDepend() {
		return new Destroyable[1];
	}

	@Override
	protected boolean test(Label l) {
		Destroyable d = l.getDepend()[0];
		Numbers test = d.isDefined() ? Numbers.ZERO : Numbers.ONE;
		setState(l, test, 0.00001);
		return true;
	}

	@Override
	public boolean define(Label l) {
		test(l);
		Destroyable[] depend = l.getDepend();
		l.setString( "\u2203(" + s(depend[0]) + ")" );
		return true;
	}

	@Override
	public void update(Observable observable, Object arg) {
		((Label) observable).setDefined(true);
		super.update(observable, arg);
	}

}
