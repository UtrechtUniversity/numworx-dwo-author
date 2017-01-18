package fi.euclides.event;

import java.util.Vector;

import fi.euclides.util.Messages;
import fi.euclides.model.Destroyable;
import fi.euclides.model.math.Numbers;

public class DestroyHandler extends EventHandler {

	public DestroyHandler() {
		super(Messages.getString("DestroyHandler.0")); //$NON-NLS-1$
		testPunt = true;
		testLijn = true;
		testLabel = true;
	}

	/* (non-Javadoc)
	 * @see fi.euclides.event.EventHandler#pointerDragged(fi.euclides.model.math.Numbers, fi.euclides.model.math.Numbers)
	 */
	public void pointerDragged(Numbers x, Numbers y) {
	}

	/* (non-Javadoc)
	 * @see euclides.event.EventHandler#command()
	 */
	public void command() {
		super.command();
		
		if(getModel().getSelect().isEmpty())
		{	Destroyable d = null;
			Vector lijnen = getModel().getLijnen();
			Vector punten = getModel().getPunten();
			if(!lijnen.isEmpty())
			{
				d = (Destroyable) lijnen.lastElement();
			}
			if( !punten.isEmpty())
			{
				Destroyable d1 = (Destroyable) punten.lastElement();
				if(d == null || d.getIndex()< d1.getIndex())
					d = d1;
			}
			if(d == null)
				return;
//			getModel().toggle(d);
			getTracker().setPointerHandler(this);
//			setStatus(s(d) + Messages.getString("DestroyHandler.1")); //$NON-NLS-1$
		} else {
			setStatus(string);
			getModel().destroy();
		}
	}

	public void pointerClicked(Numbers x, Numbers y) {
		testHits(x.doubleValue(), y.doubleValue());
		if(!getModel().getSelect().isEmpty())getModel().destroy();
	}	
}
