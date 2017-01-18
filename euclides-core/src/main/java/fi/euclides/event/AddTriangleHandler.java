package fi.euclides.event;

import java.util.Vector;

import fi.euclides.model.Punt;


// TODO looks like addBissectriceHandler
public class AddTriangleHandler extends EventHandler {

	private int state;
	
	public AddTriangleHandler() {
		super("Driehoek met 3 punten"); //$NON-NLS-1$
		testLijn = false;
		testPunt = true;
	}
	
	/* (non-Javadoc)
	 * @see euclides.event.EventHandler#command()
	 */
	public void command() {
		final Vector select = getModel().getSelect();
		state = select.size();
		switch(state) {
		case 3: 
			getModel().buildTriangle();
			state = 0;
			break;		
		case 0:	super.command();
				break;
		} 
		
	}

}
