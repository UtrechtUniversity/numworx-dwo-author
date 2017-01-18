package fi.euclides.event;

import fi.euclides.model.Triangle;
import fi.euclides.model.LijnTrack;

public class AddTriangleHandler2 extends AddBissectriceHandler {

	public AddTriangleHandler2() {
	}
	protected void createTrack() {
		Triangle bs = new Triangle();
		bs.setA(p1);
		bs.setB(p2);
		track = new LijnTrack(p1.getX(), p1.getY(), bs);
	}

	protected void build() {
		getModel().buildTriangle();
	}

}
