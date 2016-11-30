package nl.numworx.geodefiner.ui;

import nl.numworx.geodefiner.common.UIModel;
import fi.euclides.event.Tracker;
import fi.euclides.model.Boog;
import fi.euclides.model.Cirkel;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Kegelsnede2;
import fi.euclides.model.Label;
import fi.euclides.model.Lijn;
import fi.euclides.model.Locus;
import fi.euclides.model.Punt;
import fi.euclides.model.Segment;
import fi.euclides.model.Triangle;
import fi.euclides.model.Visitor;

public class UIModelFactory extends nl.numworx.geodefiner.common.UIModelFactory implements Visitor {

	private UIModel<?, UIEditor> model;
	private Tracker tracker;
	public UIModelFactory(Tracker viewer) {
		this.tracker = viewer;
	}

	public UIModel<?, UIEditor> build(Destroyable d) {
		model = null;
		d.visit(this);
		return model;
	}
	
	public void visitPunt(Punt p) {
		model = new PointModel().init(p).set(tracker);
	}

	public void visitLijn(Lijn l) {
		model = new LineModel().init(l);
	}

	public void visitCirkel(Cirkel c) {
		model = new ColorModel<Cirkel>().init(c);
	}

	public void visitSegment(Segment s) {
		model = new LineModel().init(s);
	}

	public void visitLabel(Label label) {
		//model = new ColorModel<Label>().init(label);
		model = new TextModel().init(label);
	}

	public void visitTriangle(Triangle t) {
		model = new ColorModel<Triangle>().init(t);
	}

	public void visitKegelsnede(Kegelsnede2 k) {
		model = new LineModel().init(k);
	}

	public void visitLocus(Locus l) {
		model = new LineModel().init(l);
	}

	public void visitBoog(Boog b) {
		model = new ColorModel<Boog>().init(b);
	}

}
