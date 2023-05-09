package nl.numworx.geodefiner;

import java.awt.Image;
import java.net.URL;

import fi.euclides.event.Tracker;
import fi.euclides.model.Punt;
import fi.euclides.model.Visitor;
import fi.euclides.model.VrijPunt;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.DefaultAdapter;
import nl.numworx.geodefiner.common.GeoTriangle;

public class AWTTriangle extends GeoTriangle {
	
	String resource = "/fi/euclides/resources/geodriehoek.png";
	int width = 450;
	int height = 450;
	VrijPunt imageCenter;

	public AWTTriangle(Tracker viewer) {
		super(viewer, (450.0/28.0)/2.0 );
		URL url = getClass().getResource(resource);
		Image image = java.awt.Toolkit.getDefaultToolkit().createImage(url);
		DefaultAdapter.getDefault(this).put(Image.class, image);
		imageCenter = new VrijPunt(width/2.0, height/2.0);
		imageCenter.setFree(false);
	}

	@Override
	public void visit(Visitor v) {
		v.visitImage(this);
	}

	@Override
	public Punt imageCenter() {
		return imageCenter;
	}

	@Override
	public Numbers rotation() {
		return Numbers.div(super.rotation(), Numbers.createInteger(width));
	}

	
}
