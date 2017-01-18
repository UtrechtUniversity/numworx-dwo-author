package fi.euclides.event;

import fi.euclides.model.math.Numbers;

public class AddRaakLijnHandler extends EventHandler {

	public AddRaakLijnHandler() {
		super("Raaklijn");
		
	}

	public void pointerReleased(Numbers x, Numbers y) {
		super.pointerReleased(x, y);
		getModel().buildRaaklijn(x, y);
	}

}
