package nl.numworx.geodefiner.common.math;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.math.Exact;
import fi.euclides.model.math.Numbers;
import fi.euclides.proof.LabelTester;

class Equals extends LabelTester {

	public Equals() {
		super("\u2248");
	}

	@Override
	public Destroyable[] createDepend() {
		return new Destroyable[3];
	}

	@Override
	protected boolean test(Label l) {
		Destroyable[] depend = l.getDepend();
		double marge = 0.001;
		if(depend.length > 2 && depend[2] instanceof Label) 
			marge = ((Label)depend[2]).value.doubleValue();
		Destroyable a = depend[0];
		Destroyable b = depend[1];
		EqualsVisitor eq = new EqualsVisitor(b, getTracker());
		if(a != null) a.visit(eq);
		Numbers test = eq.test;
		setState(l, test, marge);
		return true;
	}

	@Override
	protected boolean setState(Label l, Numbers d, double eps) {
		if(l!=null)
			l.value=d;
		else 
			return ((Numbers.abs(d).doubleValue())<=eps);

		if(l.getState() > Label.EXACT)  // keep state if: NAGEKEKEN, BEWEZEN, OPGAVE, VOLDOENDE
			return true;
		if(d.equals(Numbers.ZERO))
		{
			l.setState(Label.EXACT);
			return true;
		}		
		boolean test = ((Numbers.abs(d).doubleValue())<=eps);
		if(!test)
			l.setState(Label.FALSE);
		else
			l.setState(Label.INEXACT);
		return test;
	}

	@Override
	public boolean define(Label l) {
		test(l);
		Destroyable[] depend = l.getDepend();
		l.setString( s(depend[0]) + string + s(depend[1]));
		return true;
	}

}
