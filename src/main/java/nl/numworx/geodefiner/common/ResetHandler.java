package nl.numworx.geodefiner.common;

import fi.euclides.event.EventHandler;

public class ResetHandler extends EventHandler {

	public Instance instance;
	
	public ResetHandler(String string) {
		super(string);
	}

	@Override
	public void command() {
		setStatus("reset");
		if(instance != null) 
			instance.reset();
	}

	
	
}
