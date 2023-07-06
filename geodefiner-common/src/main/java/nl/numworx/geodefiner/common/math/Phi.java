package nl.numworx.geodefiner.common.math;

import fi.euclides.expr.Som;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.math.Numbers;
import fi.euclides.proof.LabelValue;

class Phi extends Som {

	Phi() {
		super("ɸ");
	}

	@Override
	public String getSymbolicValue(Label l) {
		return "ɸ("+s(l.getDepend()[0]) +"," + s(l.getDepend()[1]) + ")";
	}

	@Override
	protected void recalc(Label l, Label[] labels) {
		double x = labels[0].value.doubleValue();
		double y = labels[1].value.doubleValue();
		double phi = Math.atan2(y,x);
		setStringValue(l,Numbers.createDouble(phi));
	}


}
