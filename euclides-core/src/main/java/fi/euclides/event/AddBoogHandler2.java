package fi.euclides.event;

import fi.euclides.model.VrijPunt;
import fi.euclides.model.Boog;
import fi.euclides.model.LijnTrack;

public class AddBoogHandler2 extends AddBissectriceHandler {

	public AddBoogHandler2(String string) {
		super(string);
	}

	protected void createTrack() {
		VrijPunt p3;
		Boog bs = new Boog(p1,p2,p3 = new VrijPunt(p2.getX(), p2.getY()));
		track = new LijnTrack(p3, bs);
	}

	protected void build() {
		visit(getModel().buildBoog());
	}

}
