package fi.euclides.event;

import java.util.Vector;

import fi.euclides.util.Messages;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Punt;
import fi.euclides.model.PuntOp;

public class AddLocusHandler extends EventHandler {

	public AddLocusHandler(String string) {
		super(string);
	}

	/* (non-Javadoc)
	 * @see fi.euclides.event.EventHandler#command()
	 */
	public void command() {
		final Vector select = getModel().getSelect();
		if(	select.size() == 2 &&
			!(select.firstElement() instanceof PuntOp) &&
			select.lastElement() instanceof PuntOp
		  )
		{
			Destroyable first = (Destroyable) select.firstElement();
			getModel().toggle(first);
			getModel().toggle(first);
		}
		
		if(select.size()!= 2 ||
				!(select.firstElement() instanceof PuntOp) ||
				!(select.lastElement() instanceof Punt)
		  )
			return;
		
		getTracker().getModel().buildLocus();
	}

	public AddLocusHandler() {
		super(Messages.getString("Meetkundige plaats"));
	}
}
