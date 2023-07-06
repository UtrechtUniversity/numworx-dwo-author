package nl.numworx.geodefiner;

import fi.euclides.expr.TrailHandler;
import fi.euclides.swing.AWTViewer;
import fi.euclides.swing.XXXAction;

public class TrailAction extends XXXAction {

	public TrailAction(String name, AWTViewer viewer) {
		super(name, "/thickness2.png", new TrailHandler(name), viewer);
	}
}