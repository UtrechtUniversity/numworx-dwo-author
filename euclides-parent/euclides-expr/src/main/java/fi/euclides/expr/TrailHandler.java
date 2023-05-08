package fi.euclides.expr;

import fi.euclides.event.EventHandler;

public class TrailHandler extends EventHandler {

	public TrailHandler(String string) {
		super(string);
	}

	@Override
	public void command() {
		getTracker().getModel().toggleTrail();
	}

}
