package fi.euclides.event;

import java.util.Enumeration;
import java.util.Vector;

import fi.euclides.util.Messages;
import fi.euclides.model.Punt;

public class AddKegelsnedeHandler extends EventHandler {

	public AddKegelsnedeHandler(String string) {
		super(string);
	}
	public AddKegelsnedeHandler() {
		super(Messages.getString("Kegelsnede"));
	}
	public void command() {
		getTracker().getModel().buildKegelsnede();
	}
	/* (non-Javadoc)
	 * @see fi.euclides.event.EventHandler#allowSelection(java.util.Vector)
	 */
	public boolean allowSelection(Vector selection) {
		if(selection.size() != 5)
			return false;
		Enumeration elements = selection.elements();
		while (elements.hasMoreElements()) {
			if(!(elements.nextElement() instanceof Punt))
				return false;
		}
		return true;
	}

}
