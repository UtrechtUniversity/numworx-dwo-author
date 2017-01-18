package fi.euclides.event;

import java.util.Vector;

import fi.euclides.util.Messages;
import fi.euclides.model.Cirkel;
import fi.euclides.model.CirkelTrack;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.LijnTrack;
import fi.euclides.model.Punt;
import fi.euclides.model.Segment;
import fi.euclides.model.Track;
import fi.euclides.model.math.Numbers;

public class AddCirkelHandler extends EventHandler {

	private int state;
	private Destroyable p;


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
			{	p1 = (Destroyable) select.firstElement();
				getModel().toggle(p1);
			} else {
				if(select.isEmpty() && p instanceof Punt) 
					getModel().toggle(p);
				p1 = getModel().buildDPunt(x, y);
			}
			getModel().toggle(p);
			getModel().toggle(p1);
			getModel().buildCirkel();
			command();
		} else if(state == 0)
		{
			command();
		}
	}

	public AddCirkelHandler() {
		super(Messages.getString("AddCirkelHandler.0")); //$NON-NLS-1$
		this.testPunt = true;
		this.testLijn = true;
	}

	public void command() {
		Vector select = getModel().getSelect();
		state = select.size();
		if(state == 1 && select.firstElement() instanceof Punt)
		{   Punt p0;
			p = p0 = (Punt) select.firstElement();
			setTrack(new CirkelTrack(p0.getX(), p0.getY()));
			getTracker().setPointerHandler(this);
			setStatus(Messages.getString("AddCirkelHandler.1")); //$NON-NLS-1$
			return;
		} else if(state == 1 && select.firstElement() instanceof Segment)
		{
			Segment s = (Segment) select.firstElement();
			Cirkel c = new Cirkel();
			c.setRadius(s.getP1());
			c.setRadius2(s.getP2());
			Punt p0 = c.getRadius();
			p = s;
			setTrack(new LijnTrack(p0.getX(), p0.getY(), c));
			getTracker().setPointerHandler(this);
			setStatus(Messages.getString("AddCirkelHandler.2")); //$NON-NLS-1$
			return;
		} else if (state == 1) {
			state = 0;
			getModel().clearSelection();
		}
		if(state == 0)
		{
			super.command();
			return;				
		}
		getModel().buildCirkel();
	}

	/* (non-Javadoc)
	 * @see fi.euclides.event.EventHandler#allowSelection(java.util.Vector)
	 */
	public boolean allowSelection(Vector selection) {
		if(selection.isEmpty())
			return true;
		Object b,c;
		Object a = selection.firstElement();
		switch(selection.size())
		{
		default: return false;
		case 1:  return a instanceof Punt || a instanceof Segment;
		case 2:  c = selection.lastElement();
				 return a instanceof Punt && (c instanceof Punt || c instanceof Segment || c instanceof Label) ||
				 		((a instanceof Segment||a instanceof Label) && c instanceof Punt);
		case 3:  c = selection.lastElement();
				 b = selection.elementAt(1);
				 return a instanceof Punt && b instanceof Punt && c instanceof Punt;		 
		}
	}

}
