package fi.euclides.proof;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.math.Numbers;

final public class FlipFlop extends LabelTester {

	public static final String TYPE = "?";

	public FlipFlop() {
		super(TYPE);
	}

	@Override
	public Destroyable[] createDepend() {
		return Label.EMPTY;
	}

	@Override
	public boolean test(Label l) {
		boolean r = setState(l, l.value, 0.0);
		return true;
	}

	@Override
	public boolean define(Label l) {
		l.register(this);
		return setState(l, l.value, 0.0);
	}

	@Override
	public boolean equals(Label label, Label other) {
		return label == other;
	}

}
