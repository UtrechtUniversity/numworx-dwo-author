package fi.euclides.event;

import java.util.Vector;

import fi.euclides.model.Kegelsnede2;
import fi.euclides.model.Cirkel;
import fi.euclides.model.Punt;

public class AddPoollijnHandler extends EventHandler {

	public AddPoollijnHandler(String string) {
		super(string);
	}
	
	public AddPoollijnHandler() {
		super("Poollijn");
	}

	public void command() {
		getModel().buildPoollijn();
	}

	public boolean allowSelection(Vector selection) {
		boolean test = selection.size() == 2;
		if(test)
		{
			Object f = selection.firstElement();
			test = f instanceof Punt || f instanceof Cirkel || f instanceof Kegelsnede2;
		}
		if(test)
		{
			Object f = selection.lastElement();
			test = f instanceof Punt || f instanceof Cirkel || f instanceof Kegelsnede2;
		}
		
		return test;
	}

}
