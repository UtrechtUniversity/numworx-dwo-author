package nl.numworx.geodefiner.common.math;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.math.Numbers;
import fi.euclides.proof.LabelTester;

public class Eq extends LabelTester {

	public Eq() {
		super("=");
	}

	@Override
	public Destroyable[] createDepend() {
		return new Destroyable[2];
	}

	@Override
	protected boolean test(Label l) {
		Destroyable[] depend = l.getDepend();
		double marge = 0.0000000000000001;
		Destroyable a = depend[0];
		Destroyable b = depend[1];
		EqualsVisitor eq = new EqualsVisitor(b, getTracker());
		if(a != null) a.visit(eq);
		Numbers test = eq.test();
		setState(l, test, marge);
		return true;
	}

	@Override
	public boolean define(Label l) {
		test(l);
		Destroyable[] depend = l.getDepend();
		l.setString( s(depend[0]) + string + s(depend[1]));
		return true;
	}

}
