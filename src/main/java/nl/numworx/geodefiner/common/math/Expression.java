package nl.numworx.geodefiner.common.math;

import nl.numworx.geodefiner.common.Definitions;
import nl.numworx.geodefiner.common.GroupOf;
import nl.numworx.geodefiner.common.Polygon;
import nl.tue.win.riaca.openmath.lang.OMApplication;
import nl.tue.win.riaca.openmath.lang.OMObject;
import fi.euclides.event.NameMapper;
import fi.euclides.event.Tracker;
import fi.euclides.expr.InterpretException;
import fi.euclides.model.Coordinaten;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Model;
import fi.euclides.model.Punt;
import fi.euclides.model.PuntenLijn;
import fi.euclides.model.Ray;
import fi.euclides.model.Segment;
import fi.euclides.model.Triangle;
import fi.euclides.openmath.OMConstants;
import fi.euclides.proof.HoekHandler;
import fi.euclides.proof.LabelDelegate;
import fi.euclides.proof.LabelValue;
import fi.euclides.util.DComparator;

public class Expression extends fi.euclides.openmath.Expression {

	public Expression(Tracker tracker) {
		super(tracker);
		LabelDelegate value = new HoekHandler();
		value.setTracker(tracker);
		symbolmap.put("geodefiner.angle", value);
		value = new Phi();
		value.setTracker(tracker);
		symbolmap.put("geodefiner.phi", value);
		install(new Rnd(), tracker);
		install(new Rnq(), tracker);
		value = new Equals();
		value.setTracker(tracker);
		symbolmap.put("geodefiner.equals", value);
		symbolmap.put("relation1.approx", value);
		value = new Eq();
		value.setTracker(tracker);
		symbolmap.put("relation1.eq", value);
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
		Binomial bin = new Binomial(); bin.setTracker(tracker);
		symbolmap.put("combinat1.binomial", bin);
		install(new BinomCDF(), tracker);
		install(new BinomPDF(), tracker);
		install(new InvNorm(), tracker);
		install(new NormalCDF(), tracker);
		install(new PoissonCDF(), tracker);
		install(new PoissonPDF(), tracker);
		install(new AantalSign(), tracker);
		GCD gcd = new GCD(); gcd.setTracker(tracker);
		symbolmap.put("arith1.gcd", gcd);
		
	}

	private void install(LabelValue value, Tracker tracker) {
		value.setTracker(tracker);
		symbolmap.put("geodefiner."+value.getSubKey(), value);		
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

	@Override
	public Destroyable copyOMA(Label l, OMObject o, NameMapper mapper) {
		OMApplication oma = (OMApplication)o;
		OMObject func = oma.firstElement();
		Model model = toc.getTracker().getModel();
		if(Definitions.POINT.isSame(func)) {
			Label depend[] = new Label[2];
			copy(oma, mapper, depend);
			return new Coordinaten(depend[0], depend[1], mapper.getO(), mapper.getU());
		}
		if(Definitions.LINE.isSame(func)) {
			Punt depend[] = new Punt[2];
			copy(oma, mapper, depend);
			return new PuntenLijn(depend[0], depend[1]);
		}
		if(Definitions.SEGMENT.isSame(func)) {
			Punt depend[] = new Punt[2];
			copy(oma, mapper, depend);
			return new Segment(depend[0], depend[1]);
		}
		if(Definitions.HALFLINE.isSame(func)) {
			Punt depend[] = new Punt[2];
			copy(oma, mapper, depend);
			return new Ray(depend[0], depend[1]);
		}
		if (Definitions.CIRCLE.isSame(func)) {
			Destroyable depend[] = new Destroyable[oma.getLength()-1];
			copy(oma, mapper, depend);
			return model.createCirkel(depend);
		}
		if (Definitions.ARC.isSame(func)) {
			Destroyable depend[] = new Destroyable[oma.getLength()-1];
			copy(oma, mapper, depend);
			return model.createBoog(depend);
		}
		if (Definitions.POLYGON.isSame(func)) {
			Punt depend[] = new Punt[oma.getLength()-1];
			copy(oma, mapper, depend);
			if(depend.length == 3) {
				DComparator.sort(depend);			
				return new Triangle(depend);
			}
			return new Polygon(depend);
		}
		if (OMConstants.LIST1_MAP.isSame(func)) {
			Destroyable depend[] = new Destroyable[oma.getLength()-1];
			copy(oma, mapper, depend);
			return new GroupOf(depend, this, mapper);
		}
		if (OMConstants.LIST2_LIST_SELECTOR.isSame(func)) {
			// map(f,1..n)_i === f(i)
			// [a1,a2,a3,a4]_i === a_i
			// f_i == f(i)
			
		}
		
		return super.copyOMA(l, o, mapper);
	}

}
