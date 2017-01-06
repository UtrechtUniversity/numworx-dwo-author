package nl.numworx.geodefiner.common;

import fi.euclides.model.Locus;
import fi.euclides.model.Model;
import fi.euclides.model.Punt;
import fi.euclides.model.PuntOp;

public class Integral extends Locus {

	public Integral() {
	}

	public Integral(LocusModel lm) {
		super(lm);
	}

	@Override
	public String key() {
		return "MI";
	}

}
