package fi.euclides.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import fi.euclides.util.Messages;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Model;
import fi.euclides.model.Punt;
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

	protected boolean filterSelection(Collection<? extends Destroyable> set) {
		return !set.isEmpty();
	}
	
	/* (non-Javadoc)
	 * @see euclides.event.EventHandler#command()
	 */
	public void command() {
		//super.command();
		
		final Model model = getModel();
		if(model.getSelect().isEmpty())
		{	Collection<Destroyable> d = new ArrayList<>(2);
			Vector<Destroyable> lijnen = model.getLijnen();
			Vector<Punt> punten = model.getPunten();
			if(!lijnen.isEmpty())
			{
				d.add(lijnen.lastElement());
			}
			if( !punten.isEmpty())
			{
				d.add(punten.lastElement());
			}
			if(filterSelection(d))
				getTracker().setPointerHandler(this);
		} else {
			if(filterSelection(model.getSelect()))
			{
				setStatus(string);
				model.destroy();
			}
		}
	}

	public void pointerClicked(Numbers x, Numbers y) {
		testHits(x.doubleValue(), y.doubleValue());
		if(filterSelection(getModel().getSelect()))
			getModel().destroy();
	}	
}
