package nl.numworx.geodefiner.common;

import fi.euclides.event.EventHandler;

public class ResetHandler extends EventHandler {

	public final Instance instance;
	
	public ResetHandler(String string, Instance instance) {
		super(string);
		this.instance = instance;
	}

	@Override
	public void command() {
		setStatus("reset");
		if(instance != null) 
			instance.reset();
	}

	
	
}
