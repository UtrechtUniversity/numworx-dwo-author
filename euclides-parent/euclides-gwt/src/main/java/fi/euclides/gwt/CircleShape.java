package fi.euclides.gwt;

import gwt.awt.Shape;
import gwt.awt.geom.Ellipse2D;

public class CircleShape extends Ellipse2D.Double implements Shape {

	public CircleShape(double x, double y, double d) {
		super(x,y, d, d);
	}

}
