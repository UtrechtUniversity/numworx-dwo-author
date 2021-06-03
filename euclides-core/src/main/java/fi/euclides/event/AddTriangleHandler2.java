package fi.euclides.event;

import fi.euclides.model.Triangle;
import fi.euclides.model.LijnTrack;
import fi.euclides.model.Segment;

public class AddTriangleHandler2 extends AddBissectriceHandler {

	public AddTriangleHandler2() {
	}
	protected void createTrack() {
		if (p2 == null) {
			track = new LijnTrack(p1.getX(), p1.getY(), new Segment());
		} else {
			Triangle bs = new Triangle(3);
			bs.setA(p1);
			bs.setB(p2);
			track = new LijnTrack(p1.getX(), p1.getY(), bs);
		}
	}

	protected void build() {
		visit(getModel().buildTriangle());
	}

}
