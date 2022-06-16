package nl.numworx.geodefiner.common.math;

import java.util.Objects;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.math.Numbers;
import fi.euclides.proof.LabelTester;
import nl.numworx.geodefiner.common.LineType;

public abstract class IsLineType extends LabelTester {

	public IsLineType() {
		super("linetype");
	}

	@Override
	public Destroyable[] createDepend() {
		return new Destroyable[2];
	}

	abstract protected LineType getLineTypeA(Destroyable a);
	abstract protected LineType getLineTypeB(Destroyable b);
	
	@Override
	protected boolean test(Label l) {
		Destroyable depend[] = l.getDepend();
		LineType a = getLineTypeA(depend[0]);
		LineType b = getLineTypeB(depend[1]);
		boolean is  = Objects.equals(a, b);
		return setState(l, is?Numbers.ZERO:Numbers.ONE, 0.0);
	}

	@Override
	public boolean define(Label l) {
		test(l);
		Destroyable[] depend = l.getDepend();
		l.setString( string + "(" + s(depend[0]) + "," + s(depend[1]) + ")");		
		return true;
	}

}
