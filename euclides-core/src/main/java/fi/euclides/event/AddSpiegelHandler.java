package fi.euclides.event;

import java.util.Vector;

import fi.euclides.util.Messages;
import fi.euclides.model.Destroyable;
import fi.euclides.model.LijnTrack;
import fi.euclides.model.Punt;
import fi.euclides.model.SpiegelPunt;
import fi.euclides.model.math.Numbers;

public class AddSpiegelHandler extends EventHandler {

	
	private Destroyable m;
	private int state;
	
	public AddSpiegelHandler() {
		super(Messages.getString("AddSpiegelHandler.0")); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see fi.euclides.event.EventHandler#command()
	 */
	public void command() {
		Vector select = getModel().getSelect();
		state = select.size();
		if(state==2)
		{ 	Destroyable d = (Destroyable) select.firstElement();
			getModel().toggle(d); // swap 1st and 2nd
			getModel().toggle(d);
			build();
			return;
		}
		if(state==1)
		{
			testLijn = false;
			testPunt = true;
			SpiegelPunt ll = new SpiegelPunt();
			ll.setMirror(m = (Destroyable) select.firstElement());
			track = new LijnTrack(Numbers.ZERO,Numbers.ZERO, ll);
			getTracker().setPointerHandler(this);
			setStatus(Messages.getString("AddSpiegelHandler.1")); //$NON-NLS-1$
			return;
		}
		getModel().clearSelection();
		state = 0;
		
		
		if(state==0)
		{
			testPunt = true;
			testLijn = true;
			super.command();
			return;
		}
	}

	/**
	 * 
	 */
	private void build() {
		getModel().buildSpiegel();
	}
	/* (non-Javadoc)
	 * @see fi.euclides.model.event.EventHandler#pointerPressed(double, double)
	 */
	public void pointerPressed(Numbers x, Numbers y, TrackerContext context) {
		if(state == 1)
		{
			context.setTrack(track);
			pointerDragged(x,y,context);
		} else if(state == 0)
		{	
			pointerDragged(x,y,context);
		}		
	}

	/* (non-Javadoc)
	 * @see fi.euclides.model.event.EventHandler#pointerReleased(double, double)
	 */

	public void pointerReleased(Numbers x, Numbers y, TrackerContext context) {
		pointerDragged(x,y,context);
		context.setTrack(null);
		Vector<Destroyable> select = context.selection();
		if(state == 1)
		{	Destroyable p1;
			if(select.size()==1 && select.firstElement() instanceof Punt)
				p1 = (Destroyable) select.elementAt(0);
			else {
				p1 = getModel().buildPunt(x, y);
				getModel().toggle(p1);
			}
			getModel().toggle(m);
			build();
		} else if(state == 0)
		{
			command();
		}
	}

}
