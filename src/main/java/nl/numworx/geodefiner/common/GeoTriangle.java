package nl.numworx.geodefiner.common;

import java.io.IOException;

import fi.euclides.event.Tracker;
import fi.euclides.model.Codec;
import fi.euclides.model.Coordinaten;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Locus;
import fi.euclides.model.Model;
import fi.euclides.model.Punt;
import fi.euclides.model.PuntOp;
import fi.euclides.model.Segment;
import fi.euclides.model.SegmentVisitor;
import fi.euclides.model.Triangle;
import fi.euclides.model.Visitor;
import fi.euclides.model.VrijPunt;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.Observable;

public class GeoTriangle extends Triangle {
	
	private final Tracker viewer;

	public GeoTriangle(Tracker viewer) {
		super(3);
		this.viewer = viewer;
		
		Punt o = viewer.getModel().getO();
		Punt u = viewer.getModel().getU();
		a = new Coordinaten(newLabel(), newLabel(), o, u);
		a.setCy(Numbers.ZERO);
		a.setCx(Numbers.createInteger(-size));
		b = new Coordinaten(newLabel(), newLabel(), o, u);
		b.setCy(Numbers.ZERO);
		b.setCx(Numbers.createInteger(size));
		c = new Coordinaten(newLabel(), newLabel(), o, u);
		c.setCx(Numbers.ZERO);
		c.setCy(Numbers.createInteger(size));
		
		ab = new Segment(a, b);
		bc = new Segment(b, c);
		ca = new Segment(c, a);
		setA(a);
		setB(b);
		setC(c);
	}

	private Label newLabel() {
		Label l = new Label();
		l.register(viewer.getRegistered("1"));
		return l;
	}

	// model 
	
	int size = 7;
	
	Coordinaten a, b, c;
	Segment  ab, bc, ca;
	
	@Override
	public boolean isDefined() {
		return true;
	}

	@Override
	public void visitSegments(SegmentVisitor v) {
		v.visitSegment(ab);
		v.visitSegment(bc);
		v.visitSegment(ca);
	}

	public void update(Observable observable, Object arg) {
	}

	@Override
	public String key() {
		return "geo";
	}

	@Override
	public void write(Codec codec) throws IOException {
	}

	@Override
	public void read(Codec codec) throws IOException {
	}


}
