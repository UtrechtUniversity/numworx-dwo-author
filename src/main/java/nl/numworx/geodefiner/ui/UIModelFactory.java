package nl.numworx.geodefiner.ui;

import nl.numworx.geodefiner.common.Grid;
import nl.numworx.geodefiner.common.Integral;
import nl.numworx.geodefiner.common.Interval;
import nl.numworx.geodefiner.common.UIModel;
import nl.numworx.geodefiner.merge.RenameAction;

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
	private Models.Builder builder;
	private transient Models models;
	
	@Inject public UIModelFactory(Tracker viewer, Models.Builder builder) {
	    this.builder = builder;
		this.tracker = viewer;
	}
		
	public UIModel<?, UIEditor> build(Destroyable d) {
	    models = builder.init(d).build();
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
			model = models.pointmodel();
	}

	public void visitLijn(Lijn l) {
		if (l instanceof Ray) {
			model = new RayModel().init(l);
			return;
		}
		String name = tracker.getMapper().toString(l);
		if("x".equals(name) || "y".equals(name))
			model = new AxesModel().init(l);
		else
			model = new LineModel().init(l);
	}

	public void visitCirkel(Cirkel c) {
		model = new CircleModel().init(c);
	}

	public void visitSegment(Segment s) {
		model = new SegmentModel().init(s);
	}

	public void visitLabel(Label label) {
		//model = new ColorModel<Label>().init(label);
		if(label.getRegistered() instanceof Interval) {
			model = new IntervalModel().init(label);
			return;
		}

		model = new TextModel().init(label);
	}

	public void visitTriangle(Triangle t) {
		model = new CircleModel().init(t);
	}

	public void visitKegelsnede(Kegelsnede2 k) {
		model = new LineModel().init(k);
	}

	public void visitLocus(Locus l) {
		if (l instanceof Grid) {
			model = new GridModel().init(l);
		} else
		
		if (l instanceof Integral) {
			model = new ColorModel<Locus>().init(l);
		} else
			model = new LineModel().init(l);
	}

	public void visitBoog(Boog b) {
		model = new CircleModel().init(b);
	}

}
