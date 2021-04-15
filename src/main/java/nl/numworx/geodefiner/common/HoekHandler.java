package nl.numworx.geodefiner.common;

import fi.euclides.model.CarryingLine;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Lijn;
import fi.euclides.model.Punt;
import fi.euclides.model.Ray;
import fi.euclides.model.Segment;
import fi.euclides.model.SnijPunt;
import fi.euclides.model.math.Numbers;

public class HoekHandler extends fi.euclides.proof.HoekHandler {

	public HoekHandler(String string) {
		super(string);
	}

	public HoekHandler() {
		super();
	}

	@Override
	public <T extends Destroyable> T visit(T t) {
		
		volg( (Label) t);
		return super.visit(t);
	}

	private void volg(Label l) {
		Destroyable depend[] = l.getDepend();
		if (depend[1] instanceof Punt)
		{
			Punt p = (Punt) depend[1];
			Volgpunt vp = new Volgpunt(p);
			vp.setDxy(Numbers.createInteger(10), Numbers.createInteger(-10));
			l.setP(vp);
		} else if (depend[1] instanceof Lijn) {
			SnijPunt p = new SnijPunt(lijn(depend[0]), lijn( depend[1]));
			Volgpunt vp = new Volgpunt(p);
			vp.setDxy(Numbers.createInteger(10), Numbers.createInteger(-10));
			l.setP(vp);
		}
	}

	private Lijn lijn(Destroyable d) {
		if (d instanceof Segment) {
			return new CarryingLine((Segment)d);
		}
		if (d instanceof Ray) {
			return new CarryingLine((Ray)d);

		}
		return (Lijn) d;
	}

}
