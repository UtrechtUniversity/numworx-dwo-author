package fi.euclides.event;

import java.util.Enumeration;

import fi.euclides.model.Destroyable;

public class ShowHideHandler extends EventHandler {

	public ShowHideHandler() {
		super("");
	}

	public void command() {
		Enumeration select = getModel().getSelect().elements();
		while (select.hasMoreElements()) {
			Destroyable object = (Destroyable) select.nextElement();
			object.setVisible(!object.isVisible());
		}
		getModel().clearSelection();
	}

}
