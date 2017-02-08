package nl.numworx.geodefiner.common.math;

import fi.euclides.expr.Som;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.math.Numbers;

class MinMax extends Som {

	final int een;
	
	MinMax(int een) {
		super(een == 1?"max":"min");
		this.een = een;
	}

	static final MinMax MAX = new MinMax(1);
	static final MinMax MIN = new MinMax(-1);
	
	@Override
	protected void recalc(Label l, Label[] labels) {
		Numbers waarde = labels[0].value;
		for (int i = 1; i < labels.length; i++ ) {
			Numbers v = labels[i].value;
			int s = Numbers.signum(Numbers.sub(v, waarde));
			if(s == een) waarde = v;
		}
		setStringValue(l, waarde);
	}

	@Override
	public Destroyable[] createDepend(int args) {
		return new Label[args];
	}

}
