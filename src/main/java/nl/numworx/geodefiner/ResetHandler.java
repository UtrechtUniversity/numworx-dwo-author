package nl.numworx.geodefiner;

import fi.euclides.event.EventHandler;

public class ResetHandler extends EventHandler {

	Instance instance;
	
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
