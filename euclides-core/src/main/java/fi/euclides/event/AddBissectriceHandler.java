package fi.euclides.event;

import java.util.List;
import java.util.Vector;

import fi.euclides.util.Messages;
import fi.euclides.model.Bissectrice;
import fi.euclides.model.Destroyable;
import fi.euclides.model.LijnTrack;
import fi.euclides.model.Model;
import fi.euclides.model.Punt;
import fi.euclides.model.Track;
import fi.euclides.model.math.Numbers;

public class AddBissectriceHandler extends EventHandler {

	private int state;
	Punt p1, p2;
	
	public AddBissectriceHandler() {
		super(Messages.getString("AddBissectriceHandler.0")); //$NON-NLS-1$
		testLijn = true;
		testPunt = true;
	}
	
	
	AddBissectriceHandler(String string) {
		super(string);
		testLijn = true;
		testPunt = true;
	}


	/* (non-Javadoc)
	 * @see euclides.event.EventHandler#command()
	 */
	public void command() {
		final Vector<?> select = getModel().getSelect();
		state = select.size();
		switch(state) {
		case 3: 
			build();
			state = 0;
			break;		
		case 2: Object o2 = select.lastElement();
				if(o2 instanceof Punt)
				{
					p2 = (Punt) o2;
					if(select.firstElement() instanceof Punt) {
						p1 = (Punt) select.firstElement();
						createTrack(); // uses p1 and p2
					}
				} else {
					state = 0;
				}
		case 1: Object object = select.firstElement();
			if(object instanceof Punt)
			{
				p1 = (Punt) object;
			} else {
				state = 0;
			}
		case 0:	super.command();
				break;
		} 
		
	}

	protected void createTrack() {
		Bissectrice bs = new Bissectrice();
		bs.setP1(p1);
		bs.setP2(p2);
		track = (new LijnTrack(p1.getX(), p1.getY(), bs));
	}

	protected void build() {
		getModel().buildBissectrice();
		state = 0;
	}

	public void pointerPressed(Numbers x, Numbers y, TrackerContext context) {
		if(state == 2)
		{
			context.setTrack(track);
			pointerDragged(x,y,context);
		} else if(state == 0 || state == 1)
		{	
			track=new Track(x, y);
			context.setTrack(track);
			pointerDragged(x,y,context);
		}		
	}

	public void pointerReleased(Numbers x, Numbers y, TrackerContext context) {
		pointerDragged(x,y,context);
		context.setTrack(null);
		Model m = getModel();
		Vector<Destroyable> select = context.selection();
		switch(state) {
		case 0:
			if(select.size() == 1 && select.firstElement() instanceof Punt) 
				; // okay
			else {
				context.toggle(m.buildPunt(x, y));
			}
			break;
		case 1:
			if(select.size()==1 && select.firstElement() instanceof Punt)
				select.insertElementAt(p1, 0);
			else {
				Punt p = m.buildPunt(x, y);
				context.toggle(p1);
				context.toggle(p);
			}
			break;
		case 2:
			if(select.size()==1 && select.firstElement() instanceof Punt)
			{	
				select.insertElementAt(p1, 0);
				select.insertElementAt(p2, 1);
			} else 
			{ 	
				Punt p = m.buildPunt(x, y);
				context.toggle(p1);
				context.toggle(p2);
				context.toggle(p);
			}
			break;
		}
		command();
	}

	/* (non-Javadoc)
	 * @see fi.euclides.event.EventHandler#allowSelection(java.util.Vector)
	 */
	public boolean allowSelection(Vector selection) {
		int size = selection.size();
		if(size > 3)
			return false;
		for(int i = 0; i < size; i++)
			if(!(selection.elementAt(i) instanceof Punt))
				return false;
		return super.allowSelection(selection);
	}

}
