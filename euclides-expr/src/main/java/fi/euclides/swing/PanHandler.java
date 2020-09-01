package fi.euclides.swing;

import fi.euclides.event.EventHandler;
import fi.euclides.event.TrackerContext;
import fi.euclides.model.math.Numbers;

public class PanHandler extends EventHandler {

	
	double lastx;
	double lasty;
	private AWTViewer viewer;
	public PanHandler(String string, AWTViewer viewer) {
		super(string);
		this.viewer = viewer;
	}

	/* (non-Javadoc)
	 * @see euclides.event.EventHandler#pointerDragged(double, double)
	 */
	@Override
	public void pointerDragged(Numbers x, Numbers y, TrackerContext context) {
		viewer.offX += x.doubleValue()-lastx;
		viewer.offY += y.doubleValue()-lasty;
		viewer.getModel().getO().forceChanged();
	}
	
	/* (non-Javadoc)
	 * @see euclides.event.EventHandler#pointerPressed(double, double)
	 */
	@Override
	public void pointerPressed(Numbers x, Numbers y, TrackerContext context) {
		lastx=x.doubleValue();
		lasty=y.doubleValue();
	}
	
}