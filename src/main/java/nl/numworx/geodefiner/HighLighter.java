package nl.numworx.geodefiner;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import fi.euclides.event.HitTester;
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
import fi.euclides.swing.AWTViewer;
import nl.numworx.geodefiner.Instance.InstanceViewer;

class HighLighter implements MouseMotionListener, MouseListener, Visitor {

	final HitTester hits;
	final InstanceViewer viewer;
	final ThreadLocal<Graphics2D> g = new ThreadLocal<>();
	boolean hilight, out;
	int x, y;
	
	HighLighter(HitTester h, InstanceViewer v) {
		hits = h;
		h.setVisitor(this);
		viewer = v;
		hilight = true;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		//hilight = false;
		//viewer.paint();
		mouseMoved(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		//hilight = true;
		//viewer.paint();
		mouseMoved(e);

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		out = false;
		mouseMoved(e);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		out = true;
		viewer.paint();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouseMoved(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		x = e.getX()-viewer.offX;
		y = e.getY()-viewer.offY;
		hits.setXY(x, y);
		viewer.paint();
	}

	public void hilight(Destroyable d, Graphics2D g) {
		if(hilight && !out) {
			this.g.set(g);
			d.visit(hits);
			hits.done();
		}
	}

	@Override
	public void visitPunt(Punt p) {
		viewer.setPointSize(viewer.getPointSize()+ 2f);
	}

	@Override
	public void visitLijn(Lijn l) {
		thickerStroke();
	}

	private void thickerStroke() {
		Stroke s = g.get().getStroke();
		if (s instanceof BasicStroke) {
			BasicStroke bs = (BasicStroke) s;
			bs = new BasicStroke(bs.getLineWidth()+1,bs.getEndCap(), bs.getLineJoin(), bs.getMiterLimit(), bs.getDashArray(), bs.getDashPhase());
			g.get().setStroke(bs);
		}
	}

	@Override
	public void visitCirkel(Cirkel c) { // pas op: breedte 0 of color transparant
		thickerStroke();
	}

	@Override
	public void visitSegment(Segment s) {
		thickerStroke();
	}

	@Override
	public void visitLabel(Label label) {
		// bigger font?
	}

	@Override
	public void visitTriangle(Triangle t) {
		thickerStroke();
	}

	@Override
	public void visitKegelsnede(Kegelsnede2 k) {
		thickerStroke();
	}

	@Override
	public void visitLocus(Locus l) {
		thickerStroke();
	}

	@Override
	public void visitBoog(Boog b) {
		thickerStroke();
	}
	
}
