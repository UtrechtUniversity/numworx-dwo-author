package fi.euclides.event;

import java.util.Vector;

import fi.euclides.model.Cirkel;
import fi.euclides.model.Kegelsnede2;

public class AddFocusHandler extends EventHandler {

	public AddFocusHandler() {
		super("Brandpunten van kegelsnede of cirkel");
	}

	public void command() {
		getModel().buildFocus();
	}

	/* (non-Javadoc)
	 * @see fi.euclides.event.EventHandler#allowSelection(java.util.Vector)
	 */
	public boolean allowSelection(Vector selection) {
		return selection.size() == 1 && isConic(selection.firstElement());
	}

	private boolean isConic(Object o) {
		return o instanceof Cirkel || o instanceof Kegelsnede2;
	}
	
}
