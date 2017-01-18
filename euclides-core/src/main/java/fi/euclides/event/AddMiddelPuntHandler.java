package fi.euclides.event;

import java.util.Vector;

import fi.euclides.util.Messages;
import fi.euclides.model.Kegelsnede2;
import fi.euclides.model.Triangle;
import fi.euclides.model.Cirkel;
import fi.euclides.model.Destroyable;
import fi.euclides.model.LijnTrack;
import fi.euclides.model.MiddelPunt;
import fi.euclides.model.Punt;
import fi.euclides.model.Segment;
import fi.euclides.model.Track;
import fi.euclides.model.math.Numbers;

public class AddMiddelPuntHandler extends EventHandler {

	private int state;
	Punt p;
	
	public AddMiddelPuntHandler() {
		super(Messages.getString("AddMiddelPuntHandler.0")); //$NON-NLS-1$
		testPunt = true;
	}

	/* (non-Javadoc)
	 * @see fi.euclides.model.event.EventHandler#command()
	 */
	public void command() {
		final Vector select = getModel().getSelect();
		state = select.size();
		if(state == 0)
		{	testLijn = true;
			super.command();
			return;
		}
		if(state == 1)
		{
			final Object object = select.firstElement();
			if(object instanceof Punt)
			{		
				testLijn = false;
				p = (Punt) object;
				setTrack(
					new LijnTrack(p.getX(), p.getY(), new MiddelPunt())
					);
				getTracker().setPointerHandler(this);
				setStatus(Messages.getString("AddMiddelPuntHandler.1")); //$NON-NLS-1$
				return;
			} else if (object instanceof Segment|| object instanceof Cirkel)
			{
				
			} else {
				setStatus(Messages.getString("AddMiddelPuntHandler.2")); //$NON-NLS-1$
			}
		}

		getModel().buildMiddelPunt();
		state = 0;
	}

	/* (non-Javadoc)
	 * @see euclides.event.EventHandler#pointerPressed(double, double)
	 */
	public void pointerPressed(Numbers x, Numbers y) {
		if(state == 1)
		{
			tracker.setTrack(track);
			pointerDragged(x,y);
		} else if(state == 0)
		{	
			track=new Track(x, y);
			tracker.setTrack(track);
			pointerDragged(x,y);
		}		
	}
	/* (non-Javadoc)
	 * @see euclides.event.EventHandler#pointerReleased(double, double)
	 */

	public void pointerReleased(Numbers x, Numbers y) {
		pointerDragged(x,y);
		tracker.setTrack(null);
		Vector select = getModel().getSelect();
		if(state == 1)
		{	Destroyable p1;
			if(select.size()==1 && select.firstElement() instanceof Punt)
				p1 = (Destroyable) select.firstElement();
			else {
				p1 = getModel().buildPunt(x, y);
				getModel().toggle(p1);
			}
			getModel().toggle(p);
			getModel().buildMiddelPunt();
			command();
		} else if(state == 0)
		{
			command();
		}
	}

	/* (non-Javadoc)
	 * @see fi.euclides.event.EventHandler#allowSelection(java.util.Vector)
	 */
	public boolean allowSelection(Vector selection) {
		Object o;
		switch(selection.size())
		{
		default:
			return false;
		case 0: return true;
		case 1: o = selection.firstElement();
				return o instanceof Punt || 
				       o instanceof Segment || 
				       o instanceof Cirkel ||
				       o instanceof Kegelsnede2 ||
				       o instanceof Triangle;
		case 2:
				return selection.firstElement() instanceof Punt &&
					   selection.lastElement() instanceof Punt;
		}
	}

	
	
}
