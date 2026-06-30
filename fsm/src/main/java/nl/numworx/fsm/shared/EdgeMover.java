package nl.numworx.fsm.shared;

import java.util.Vector;

import fi.euclides.event.EventHandler;
import fi.euclides.event.NameMapper;
import fi.euclides.event.TrackerContext;
import fi.euclides.model.Boog;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Punt;
import fi.euclides.model.Segment;
import fi.euclides.model.VrijPunt;
import fi.euclides.model.math.Numbers;

public class EdgeMover extends EventHandler {

	public EdgeMover() {
		super("EdgeMover");
		testLijn = true;
	}

	Destroyable track;
	Boog boog;

	@Override
	public void pointerDragged(Numbers x, Numbers y, TrackerContext context) {
		// TODO Auto-generated method stub
		super.pointerDragged(x, y, context);
	}

	@Override
	public void pointerPressed(Numbers x, Numbers y, TrackerContext context) {
		this.testHits(x.doubleValue(), y.doubleValue(), context);
		Vector<Destroyable> v = context.selection();
		if (!v.isEmpty()) {
			Destroyable edge = v.firstElement();
			if (edge instanceof Segment) {
				Segment s = (Segment) edge;
				VrijPunt mid = new MidBoogPunt(x, y, s.getP1(), s.getP2());
				Boog b = new Boog(s.getP1(), mid, s.getP2());
				track = s;
				boog = b;
				context.setTrack(new BoogTrack(x, y, b));
			} else if (edge instanceof Boog) {
				boog = (Boog) edge;track = null;
				Punt start = Boog.startOf(boog);
				if (start.getIndex() > 0) {
					context.setTrack(new BoogTrack(x, y, boog));
				}
			}
		}
	}

	@Override
	public void command() {
		// TODO Auto-generated method stub
		super.command();
	}



	@Override
	public void pointerReleased(Numbers x, Numbers y, TrackerContext context) {
		context.setTrack(null);
		if (track != null) {
			NameMapper mapper = getTracker().getMapper();
			String name = mapper.toString(track);
			track.destroy(); track = null;
			mapper.rename(boog, name);
			getModel().add(boog);
		}
	}

}
