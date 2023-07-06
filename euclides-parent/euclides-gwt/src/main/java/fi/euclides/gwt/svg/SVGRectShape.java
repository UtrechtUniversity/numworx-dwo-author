package fi.euclides.gwt.svg;

import org.vectomatic.dom.svg.OMSVGRect;

import gwt.awt.geom.Rectangle2D;

public class SVGRectShape extends Rectangle2D.Float {
	
	public SVGRectShape(OMSVGRect bbox) {
		super(bbox.getX(), bbox.getY(), bbox.getWidth(), bbox.getHeight() );
	}
}
