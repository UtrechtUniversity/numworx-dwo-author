package nl.numworx.geodefiner.common;

import java.io.IOException;

import fi.euclides.event.Tracker;
import fi.euclides.model.Codec;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Punt;
import fi.euclides.model.Segment;
import fi.euclides.model.SegmentVisitor;
import fi.euclides.model.Visitor;
import fi.euclides.model.VrijPunt;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.Observable;

public class Grid extends fi.euclides.model.Locus {

	private Tracker viewer;

	public Grid(Tracker viewer) {
		this.viewer = viewer;
		setVisible(true);
	}

	@Override
	public boolean isDefined() {
		return false;
	}

	@Override
	public Destroyable[] getDepend() {
		return Label.EMPTY;
	}

	@Override
	public void visitSegments(SegmentVisitor v) {
		VrijPunt a = new VrijPunt();
		VrijPunt b = new VrijPunt();
		Segment s = new Segment(a,b);
		double bottom = v.clipBottom().doubleValue();
		double top = v.clipTop().doubleValue();
		double left = v.clipLeft().doubleValue();
		a.setX(left);
		double right = v.clipRight().doubleValue();
		b.setX(right);
		Punt o = viewer.getModel().getO();
		Punt u = viewer.getModel().getU();
		double y = o.getYd();
		double x = o.getXd();
		double d = Numbers.sub(o.getX(), u.getX()).doubleValue(); // hypot
		d = Math.abs(d);
		if ( d <= 2) return;
		for( double i = y+d; i < bottom; i += d) {
			a.setY(i);
			b.setY(i);
			v.visitSegment(s);
		}
		for( double i = y-d; i > top; i -= d) {
			a.setY(i);
			b.setY(i);
			v.visitSegment(s);
		}
		a.setY(top);
		b.setY(bottom);

		for( double i = x+d; i < right; i += d) {
			a.setX(i);
			b.setX(i);
			v.visitSegment(s);
		}
		for( double i = x-d; i > left; i -= d) {
			a.setX(i);
			b.setX(i);
			v.visitSegment(s);
		}	
	}

	public void update(Observable observable, Object arg) {
	}

	@Override
	public void visit(Visitor v) {
		v.visitLocus(this);
	}

	@Override
	public String key() {
		return "GRID";
	}

	@Override
	public void write(Codec codec) throws IOException {
	}

	@Override
	public void read(Codec codec) throws IOException {
	}

}
