package fi.euclides.event;

import java.util.Vector;

import fi.euclides.util.Messages;
import fi.euclides.model.Bissectrice;
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
		testLijn = false;
		testPunt = true;
	}
	
	/* (non-Javadoc)
	 * @see euclides.event.EventHandler#command()
	 */
	public void command() {
		final Vector select = getModel().getSelect();
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
					createTrack();
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

	public void pointerPressed(Numbers x, Numbers y) {
		if(state == 2)
		{
			tracker.setTrack(track);
			pointerDragged(x,y);
		} else if(state == 0 || state == 1)
		{	
			track=new Track(x, y);
			tracker.setTrack(track);
			pointerDragged(x,y);
		}		
	}

	public void pointerReleased(Numbers x, Numbers y) {
		pointerDragged(x,y);
		tracker.setTrack(null);
		Vector select = getModel().getSelect();
		if(state == 1)
		{
			if(select.size()==1 && select.firstElement() instanceof Punt)
				select.insertElementAt(p1, 0);
		} else if(state == 2)
		{
			if(select.size()==1 && select.firstElement() instanceof Punt)
			{	
				select.insertElementAt(p1, 0);
				select.insertElementAt(p2, 1);
			} else if(select.isEmpty())
			{ 	Model m = getModel();
				m.toggle(m.buildPunt(x, y));
				select.insertElementAt(p1, 0);
				select.insertElementAt(p2, 1);
			}
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
