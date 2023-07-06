package nl.numworx.geodefiner.common;

import java.util.function.Predicate;

import fi.euclides.event.EventHandler;
import fi.euclides.event.Tracker;
import fi.euclides.model.Destroyable;

public class GeoTriangleHandler extends EventHandler {

	private final EventHandler selector;
	private Predicate<Destroyable> test = d -> !d.isVisible();
 	
	@Override
	public void command() {
		Tracker viewer = getTracker();
		Destroyable d = viewer.getMapper().fromString("geo");
		if (d != null) 
		{	d.setVisible(test.test(d));
		    if (d.isVisible())
		    	selector.command();
		}
	}

	public GeoTriangleHandler(String string, EventHandler selector) {
		super(string);
		this.selector = selector;
	}
	public GeoTriangleHandler(String string, EventHandler selector, Predicate<Destroyable> test) {
		this(string, selector);
		this.test = test;
	}

}
