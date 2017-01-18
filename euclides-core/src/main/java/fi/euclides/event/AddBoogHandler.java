package fi.euclides.event;

import java.util.Vector;

import fi.euclides.util.Messages;

public class AddBoogHandler extends EventHandler {

	public AddBoogHandler(String string) {
		super(string);
	}

	public AddBoogHandler() {
		this(Messages.getString("AddBoogHandler.0"));
	}

	@Override
	public void command() {
		int size = getModel().getSelect().size();
		if(size == 3||size == 4) {
			getModel().buildBoog();
		}
	}

	@Override
	public boolean allowSelection(Vector selection) {
		return 3 == selection.size() || 4 == selection.size();
	}
	
}
