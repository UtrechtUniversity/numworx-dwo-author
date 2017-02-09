package nl.numworx.geodefiner.common.math;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.proof.LabelValue;

public class Sigma extends LabelValue {

	public Sigma() {
		super("Σ");
	}

	@Override
	public String getSymbolicValue(Label l) {
		return string;
	}

	@Override
	public Destroyable[] createDepend() {
		return new Label[2];
	}

}
