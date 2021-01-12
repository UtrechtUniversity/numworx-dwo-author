package nl.numworx.geodefiner.ui;

import nl.numworx.geodefiner.common.Grid;
import nl.numworx.geodefiner.common.Integral;
import nl.numworx.geodefiner.common.Interval;
import nl.numworx.geodefiner.common.UIModel;
import javax.inject.Inject;
import javax.inject.Singleton;

import fi.euclides.event.Tracker;
import fi.euclides.model.Boog;
import fi.euclides.model.Cirkel;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Groep;
import fi.euclides.model.Kegelsnede2;
import fi.euclides.model.Label;
import fi.euclides.model.Lijn;
import fi.euclides.model.Locus;
import fi.euclides.model.Punt;
import fi.euclides.model.Ray;
import fi.euclides.model.Segment;
import fi.euclides.model.Triangle;
import fi.euclides.model.Visitor;

@Singleton
public class UIModelFactory extends nl.numworx.geodefiner.common.UIModelFactory implements Visitor {

	private UIModel<? extends Destroyable, UIEditor> model;
	private Tracker tracker;
	private Models models;
	
	@Inject public UIModelFactory(Tracker viewer, Models.Builder builder) {
	    this.models = builder.build();
		this.tracker = viewer;
	}
		
	public UIModel<? extends Destroyable, UIEditor> build(Destroyable d) {
		model = null;
		if(d instanceof Groep) {
			Groep g = (Groep)d;
			build(g.prototype()); // what if 0 elements?
			return model.init2(d);
		}
		d.visit(this);
		return model.set(tracker);
	}
	
	public void visitPunt(Punt p) {
		if(p == tracker.getModel().getO())
			model = models.omodel();
		else if (p == tracker.getModel().getU())
			model = models.umodel();
		else
			model = models.pointmodel().init(p);
	}

	public void visitLijn(Lijn l) {
		if (l instanceof Ray) {
			model = models.raymodel().init(l);
			return;
		}
		String name = tracker.getMapper().toString(l);
		if("x".equals(name) || "y".equals(name))
			model = models.axesmodel().init(l);
		else
			model = models.linemodel().init(l);
	}

	public void visitCirkel(Cirkel c) {
		model = models.circlemodel().init(c);
	}

	public void visitSegment(Segment s) {
		model = models.segmentmodel().init(s);
	}

	public void visitLabel(Label label) {
		//model = new ColorModel<Label>().init(label);
		if(label.getRegistered() instanceof Interval) {
			model = models.intervalmodel().init(label);
			return;
		}

		model = models.textmodel().init(label);
	}

	public void visitTriangle(Triangle t) {
		model = models.circlemodel().init(t);
	}

	public void visitKegelsnede(Kegelsnede2 k) {
		model = models.linemodel().init(k);
	}

	public void visitLocus(Locus l) {		
		if (l instanceof Grid) {
			model = models.gridmodel().init(l);
		} else		
		if (l instanceof Integral) {
			if (isVgl(l))
				model = models.inequalitymodel().init(l);
			else
				model = models.integralmodel().init(l);
		} else if (isVgl(l)) 
			model = models.vglmodel().init(l);
		else
			model = models.linemodel().init(l);
	}

	private boolean isVgl(Locus l) {
		return tracker.getMapper().toString(l).startsWith("$");
	}

	public void visitBoog(Boog b) {
		model = models.circlemodel().init(b);
	}

	@Override
	public UIModel<?, ?> lightBuild(Destroyable d) {
		if (d instanceof Triangle) return models.triangleModel().init(d);
		return super.lightBuild(d);
	}

}
