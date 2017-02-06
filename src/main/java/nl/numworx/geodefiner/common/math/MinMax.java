package nl.numworx.geodefiner.common.math;

import fi.euclides.expr.Som;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.math.Numbers;

public class MinMax extends Som {

	final int een;
	
	MinMax(int een) {
		super(een == 1?"max":"min");
		this.een = een;
	}

	public static final MinMax MAX = new MinMax(1);
	public static final MinMax MIN = new MinMax(-1);
	
	@Override
	public Destroyable[] createDepend() {
		return new Label[2];
	}

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
