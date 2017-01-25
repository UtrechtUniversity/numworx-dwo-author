package nl.numworx.geodefiner.common;

import fi.euclides.event.Tracker;
import fi.euclides.proof.HoekHandler;
import fi.euclides.proof.LabelDelegate;

public class Expression extends fi.euclides.openmath.Expression {

	public Expression(Tracker tracker) {
		super(tracker);
		LabelDelegate value = new HoekHandler();
		value.setTracker(tracker);
		symbolmap.put("geodefiner.angle", value);
		value = new Phi();
		value.setTracker(tracker);
		symbolmap.put("geodefiner.phi", value);
		value = new Rnd();
		value.setTracker(tracker);
		symbolmap.put("geodefiner.rnd", value);
		value = new Equals();
		value.setTracker(tracker);
		symbolmap.put("geodefiner.equals", value);
		symbolmap.put("relation1.approx", value);
	}

}
