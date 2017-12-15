package nl.numworx.geodefiner.common;

import java.util.Vector;

import fi.euclides.event.EventHandler;
import fi.euclides.model.Destroyable;
import fi.euclides.model.LijnTrack;
import fi.euclides.model.Model;
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
		final Model model = getModel();
		Vector<Destroyable> select = model.getSelect();
		switch(state) {
		case 0:
			if(select.isEmpty()) {
				Punt p = model.buildPunt(x, y);
				points.addElement(p);
				state = 1;
				setTrack(new LijnTrack(p.getX(), p.getY(), new Segment()));
			}
			break;
		case 1: 
			if(select.isEmpty()) {
				Punt p = model.buildPunt(x, y);
				points.addElement(p);
				state = 2;
				Triangle t = new Triangle(3);
				t.setA(points.firstElement());
				t.setB(p);
				setTrack(new LijnTrack(p.getX(), p.getY(),t));
			}
			break;
		case 2:
			if(select.isEmpty()) {
				Punt p = model.buildPunt(x, y);
				points.addElement(p);
				state = 3;
				setTrack(new PolygonTrack(x,y, points));			
			}
			break;
		case 3:
			if(select.isEmpty()) {
				Punt p = model.buildPunt(x, y);
				points.addElement(p);
				setTrack(new PolygonTrack(x,y, points));			
			} else if (select.firstElement() == points.firstElement()) {
					Punt[] array = points.toArray(new Punt[points.size()]);
					Triangle p;
					if(array.length > 3)
						p = new Polygon(array);
					else
						p = new Triangle(array);
					model.add(p);
					points.clear();
					setTrack(new Track(x,y));
					state = 0;
			}
			break;
		}
		getTracker().setTrack(null);
		getTracker().paint();
	}

	@Override
	public void pointerPressed(Numbers x, Numbers y) {	
		final Track t = getTrack();
		t.setXY(x, y);
		getTracker().setTrack(t);
	}

	
}
