package nl.numworx.geodefiner.common.math;

import nl.tue.win.riaca.openmath.lang.OMApplication;
import nl.tue.win.riaca.openmath.lang.OMObject;
import fi.euclides.event.NameMapper;
import fi.euclides.event.Tracker;
import fi.euclides.expr.InterpretException;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Punt;
import fi.euclides.model.Segment;
import fi.euclides.openmath.OMConstants;
import fi.euclides.proof.HoekHandler;
import fi.euclides.proof.LabelDelegate;
import fi.euclides.proof.LabelValue;

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
		Abs abs = new Abs();
		abs.setTracker(tracker);
		put(OMConstants.ARITH1_ABS, abs);
		Faculteit fac = new Faculteit();
		fac.setTracker(tracker);
		symbolmap.put("integer1.factorial", fac);
		Sigma sigma = new Sigma();
		sigma.setTracker(tracker);
		symbolmap.put("arith1.sum", sigma);
		Prv prv = new Prv();
		prv.setTracker(tracker);
		symbolmap.put("wiskopdr.prv", prv);
		MinMax m = MinMax.MAX(); m.setTracker(tracker);symbolmap.put("minmax1.max", m);
		 m = MinMax.MIN(); m.setTracker(tracker);symbolmap.put("minmax1.min", m);
		toc = new ToComplex();
		toc.setTracker(tracker);
	}

	LabelValue toc;
	public void copy(OMApplication oma, NameMapper mapper, Destroyable[] depend) {
		int size = oma.getLength()-1;
		for (int i = 0; i < size; i++) {
			OMObject o = oma.getElementAt(i+1);
			Destroyable result = null;
			try {
				depend[i] = result = interpret(o, new Label(), mapper);
			} catch (ArrayStoreException e) {
				if(depend instanceof Label[]) {
					if(result instanceof Punt || result instanceof Segment) {
						Label l = toc.define(new Destroyable[] { result });
						depend[i] = l;
						continue;
					}
				}
				
				
				
				throw new InterpretException("Wrong type", e);
			}
		}
	}

}
