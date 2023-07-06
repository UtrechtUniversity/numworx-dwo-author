package nl.numworx.geodefiner.common;

import fi.euclides.model.Locus;
import fi.euclides.model.Model;
import fi.euclides.model.Punt;
import fi.euclides.model.PuntOp;

public class Integral extends Locus {

	public static double LT = Double.NEGATIVE_INFINITY;
	public static double GT = Double.POSITIVE_INFINITY;
	
	final public double base;
	
	public Integral() {
		base = 0;
	}

	public Integral(LocusModel lm, double base) {
		super(lm);
		this.base = base;
	}

	public Integral(LocusModel lm) {
		super(lm);
		base = 0;
	}

	@Override
	public String key() {
		return "MI";
	}

}
