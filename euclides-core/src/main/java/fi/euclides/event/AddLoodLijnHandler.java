package fi.euclides.event;

import java.util.Vector;

import fi.euclides.util.Messages;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Lijn;
import fi.euclides.model.LijnPuntCombi;
import fi.euclides.model.LijnTrack;
import fi.euclides.model.LoodLijn;
import fi.euclides.model.Punt;
import fi.euclides.model.math.Numbers;

public class AddLoodLijnHandler extends EventHandler {

	/* (non-Javadoc)
	 * @see fi.euclides.event.EventHandler#allowSelection(java.util.Vector)
	 */
	public boolean allowSelection(Vector selection) {
		if(selection.size()>2)
			return false;
		if(selection.isEmpty())
			return true;
		Object o = selection.firstElement();
		if( !(o instanceof Punt) && !(o instanceof Lijn) )
			return false;
		o = selection.lastElement();
		if( !(o instanceof Punt) && !(o instanceof Lijn) )
			return false;
// if size == 2 punt/lijn of lijn/punt maar geen lijn/lijn of punt/punt		
		// TODO Auto-generated method stub
		return super.allowSelection(selection);
	}

	private int state;
	private Destroyable o1;
	public AddLoodLijnHandler() {
		super(Messages.getString("AddLoodLijnHandler.0")); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see fi.euclides.model.event.EventHandler#command()
	 */
	public void command() {
		Vector select = getModel().getSelect();
		state = select.size();
		if(state == 0)
		{
			testPunt = false;
			testLijn = true;
			super.command();
			return;
		}
		if(state == 1 && select.firstElement() instanceof Lijn)
		{
			testLijn = false;
			testPunt = true;
			LijnPuntCombi ll = newLijnCombi();
			ll.setDestroyable((Lijn) (o1 = (Destroyable) select.firstElement()));
			track = new LijnTrack(Numbers.ZERO,Numbers.ZERO, ll);
			getTracker().setPointerHandler(this);
			setStatus(Messages.getString("AddLoodLijnHandler.1")); //$NON-NLS-1$
			return;
		} else if (state == 1)
		{
			clear();
			command();
			return;
		}
		build();
	}

	Lijn build() {
		return getModel().buildLoodlijn();
	}

	LijnPuntCombi newLijnCombi() {
		return new LoodLijn();
	}

	/* (non-Javadoc)
	 * @see fi.euclides.model.event.EventHandler#pointerPressed(double, double)
	 */
	public void pointerPressed(Numbers x, Numbers y) {
		if(state == 1)
		{
			tracker.setTrack(track);
			pointerDragged(x,y);
		} else if(state == 0)
		{	
			pointerDragged(x,y);
		}		
	}

	/* (non-Javadoc)
	 * @see fi.euclides.model.event.EventHandler#pointerReleased(double, double)
	 */

	public void pointerReleased(Numbers x, Numbers y) {
		pointerDragged(x,y);
		tracker.setTrack(null);
		Vector select = getModel().getSelect();
		if(state == 1)
		{	Destroyable p1;
			if(select.size()==1 && select.firstElement() instanceof Punt)
				p1 = (Destroyable) select.elementAt(0);
			else {
				p1 = getModel().buildPunt(x, y);
				getModel().toggle(p1);
			}
			getModel().toggle(o1);
			build();
			command();
		} else if(state == 0)
		{
			command();
		}
	}


}
