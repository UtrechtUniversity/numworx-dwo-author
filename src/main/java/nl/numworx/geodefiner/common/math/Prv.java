package nl.numworx.geodefiner.common.math;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.proof.LabelValue;

public class Prv extends LabelValue {

	Prv() {
		super("prv");
	}

	@Override
	public String getSymbolicValue(Label l) {
		Destroyable[] depend = l.getDepend();
		return "prv(" + s(depend[0]) +"," + s(depend[1]) + "," + s(depend[2]) + ")";
	}

	@Override
	public Destroyable[] createDepend() {
		return new Label[3];
	}

}
