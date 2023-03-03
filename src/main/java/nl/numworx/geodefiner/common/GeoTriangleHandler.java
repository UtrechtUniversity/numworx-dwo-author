package nl.numworx.geodefiner.common;

import fi.euclides.event.EventHandler;
import fi.euclides.event.Tracker;
import fi.euclides.model.Destroyable;

public class GeoTriangleHandler extends EventHandler {

	@Override
	public void command() {
		Tracker viewer = getTracker();
		Destroyable d = viewer.getMapper().fromString("geo");
		if (d != null) 
		{	d.setVisible(!d.isVisible());
		}
	}

	public GeoTriangleHandler(String string) {
		super(string);

	}

}
