package fi.euclides.gwt.canvas;

import fi.euclides.event.EventHandler;
import fi.euclides.model.math.Numbers;

public class PanHandler extends EventHandler {

	double lastx;
	double lasty;
	private SpeelVeld viewer;
	public PanHandler(String string, SpeelVeld viewer) {
		super(string);
		this.viewer = viewer;
	}

	/* (non-Javadoc)
	 * @see euclides.event.EventHandler#pointerDragged(double, double)
	 */
	public void pointerDragged(Numbers x, Numbers y) {
		viewer.offX += x.doubleValue()-lastx;
		viewer.offY += y.doubleValue()-lasty;
	}
	
	/* (non-Javadoc)
	 * @see euclides.event.EventHandler#pointerPressed(double, double)
	 */
	public void pointerPressed(Numbers x, Numbers y) {
		lastx=x.doubleValue();
		lasty=y.doubleValue();
	}
	
}