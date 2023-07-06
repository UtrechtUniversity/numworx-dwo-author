package fi.euclides.gwt.canvas;

import fi.euclides.event.EventHandler;
import fi.euclides.event.TrackerContext;
import fi.euclides.model.Pair;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.DefaultAdapter;

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
	@Override
	public void pointerDragged(Numbers x, Numbers y, TrackerContext context) {
		@SuppressWarnings("unchecked")
		Pair<Numbers,Numbers> pair = context.getAdapter().adapt(Pair.class);
		if (pair != null) {
			lastx = pair.getA().doubleValue();
			lasty = pair.getB().doubleValue();
		}
		
		viewer.offX += x.doubleValue()-lastx;
		viewer.offY += y.doubleValue()-lasty;
		viewer.getModel().getO().forceChanged();
	}
	
	/* (non-Javadoc)
	 * @see euclides.event.EventHandler#pointerPressed(double, double)
	 */
	@Override
	public void pointerPressed(Numbers x, Numbers y, TrackerContext context) {
		Pair<Numbers, Numbers> pair;
		pair = new Pair<>(x, y);
		DefaultAdapter.getDefault(context).put(Pair.class, pair);
		lastx=x.doubleValue();
		lasty=y.doubleValue();
	}
	
}