package nl.numworx.geodefiner.common;

import java.util.Vector;

import fi.euclides.event.EventHandler;
import fi.euclides.model.Destroyable;
import fi.euclides.model.LijnTrack;
import fi.euclides.model.Model;
import fi.euclides.model.OpObject;
import fi.euclides.model.Punt;
import fi.euclides.model.Segment;
import fi.euclides.model.Track;
import fi.euclides.model.Triangle;
import fi.euclides.model.Visitor;
import fi.euclides.model.math.Numbers;

public class AddPolygonHandler extends EventHandler {

	class PolygonTrack extends Track {

		private Polygon poly;

		public PolygonTrack(Numbers d, Numbers e, Vector<Punt> l) {
			super(d, e);
			int n = l.size()+1;
			Punt[] p = new Punt[n];
			l.copyInto(p);
			p[n-1] = super.p;
			poly = new Polygon(p);
		}
		public void destroy() {
			poly.destroy();
		}
		public void visit(Visitor v) {
			poly.visit(v);
		}
	}
	
	
	int state = 0;
	public AddPolygonHandler(String string) {
		super(string);
		testPunt = true;
		testLijn = true;
	}
	Vector<Punt> points;
	
	@Override
	public void command() {
		points = new Vector<>();
		super.command();
		setTrack(new Track(Numbers.NaN, Numbers.NaN));
	}

	@Override
	public void pointerReleased(Numbers x, Numbers y) {
		pointerDragged(x,y);
		Model model = getModel();
		Vector<Destroyable> select = model.getSelect();
		Punt p;
		switch(state) {
		case 0:
			p = selectedPoint(x, y, model, select);
			if (p == null) break;
			points.addElement(p);
			state = 1;
			setTrack(new LijnTrack(p.getX(), p.getY(), new Segment()));
			break;
		case 1:
			p = selectedPoint(x, y, model, select);
			if (p == null) break;
			if( points.contains(p)) {
				break;
			}
			points.addElement(p);
			state = 2;
			Triangle t = new Triangle(3);
			t.setA(points.firstElement());
			t.setB(p);
			setTrack(new LijnTrack(p.getX(), p.getY(),t));
			break;
		case 2:
			p = selectedPoint(x, y, model, select);
			if (p == null) break;
			if (points.contains(p)) {
				break;
			}
			points.addElement(p);
			state = 3;
			setTrack(new PolygonTrack(x,y, points));			
			break;
		case 3:
			p = selectedPoint(x,y, model, select);
			if (p==null) break;
			if (points.contains(p)) {
				if (p == points.firstElement()) {
					Punt[] array = points.toArray(new Punt[points.size()]);
					if(array.length > 3)
						t = new Polygon(array);
					else
						t = new Triangle(array);
					model.add(t);
					points.clear();
					setTrack(new Track(x,y));
					state = 0;
				}
				break;
			}
			points.addElement(p);
			setTrack(new PolygonTrack(x,y, points));
			break;
		}
		getTracker().setTrack(null);
		getTracker().paint();
	}

	private Punt selectedPoint(Numbers x, Numbers y, Model model, Vector<Destroyable> select) {
		Punt p = null;
		if(select.isEmpty()) {
			p = model.buildPunt(x, y);
		} else if(select.size() == 1 && select.firstElement() instanceof Punt) {
			p = (Punt) select.firstElement();
		} else if (select.firstElement() instanceof OpObject) {
			p = model.buildPunt(x,y);
		}
		model.clearSelection();
		return p;
	}

	@Override
	public void pointerPressed(Numbers x, Numbers y) {	
		final Track t = getTrack();
		t.setXY(x, y);
		getTracker().setTrack(t);
	}

	
}
