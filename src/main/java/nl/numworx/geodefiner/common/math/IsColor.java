package nl.numworx.geodefiner.common.math;

import java.util.Objects;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.math.Numbers;
import fi.euclides.proof.LabelTester;
import fi.euclides.util.Observable;

public abstract class IsColor extends LabelTester {

	public static final String COLOR = "color";

	protected IsColor() {
		super(COLOR);
	}

	@Override
	public Destroyable[] createDepend() {
		return new Destroyable[2];
	}

	abstract protected Object getColorA(Destroyable a);
	abstract protected Object getColorB(Destroyable b);
	
	@Override
	protected boolean test(Label l) {
		Destroyable depend[] = l.getDepend();
		Object a = getColorA(depend[0]);
		Object b = getColorB(depend[1]);
		boolean is  = Objects.equals(a, b) && depend[0].isDefined() && depend[1].isDefined();
		return setState(l, is?Numbers.ZERO:Numbers.ONE, 0.0);
	}

	@Override
	public boolean define(Label l) {
		test(l);
		Destroyable[] depend = l.getDepend();
		l.setString( string + "(" + s(depend[0]) + "," + s(depend[1]) + ")");
		
		return true;
	}

	@Override
	public void update(Observable observable, Object arg) {
		if (arg == COLOR && observable instanceof Label) {
			test( (Label) observable);
			return;
		}
		// TODO Auto-generated method stub
		super.update(observable, arg);
	}

}
