package nl.numworx.geodefiner.common;

import java.util.Vector;

import fi.euclides.event.EventHandler;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Punt;
import fi.euclides.model.Track;
import fi.euclides.model.math.Numbers;

public abstract class AbstractTextHandler extends EventHandler {

	protected AbstractTextHandler(String string) {
		super(string);
		testPunt = true;
		setTrack(new Track(Numbers.ZERO, Numbers.ZERO));
	}

	@Override
	public void command() {
		if (!attachSelection())		
			super.command();
	}

	private boolean attachSelection() {
		Vector<Destroyable> selection = getModel().getSelect();
		if(selection.size() == 1) {
			Destroyable first = selection.firstElement();
			if(first instanceof Punt) {
				Punt p = (Punt) first;
				attachLabel(p);
				return true;
			}
		}
		selection.clear();
		return false;
	}

	
	
	
	@Override
	public void pointerPressed(Numbers x, Numbers y) {
		getTracker().setTrack(getTrack());
		pointerDragged(x,y);
	}

	@Override
	public void pointerReleased(Numbers x, Numbers y) {
		pointerDragged(x,y);
		attachSelection();
		getTracker().setTrack(null);
	}

	abstract protected void attachLabel(Punt p);
	
}
