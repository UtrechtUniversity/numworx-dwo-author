package fi.euclides.expr;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Punt;
import fi.euclides.model.math.Numbers;
import fi.euclides.proof.LabelValue;
import fi.euclides.util.Observable;

public class Ratio extends LabelValue {

	public static final Ratio INSTANCE = new Ratio();

	private Ratio() {
		super("R2");
	}

	public String getSymbolicValue(Label l) {
		Destroyable[] depend = l.getDepend();
		
		return s(depend[0]) + s(depend[1]) + ":" + s(depend[2]) + s(depend[3]);
	}

	public Destroyable[] createDepend() {
		return new Punt[4];
	}

	public void update(Observable observable, Object arg) {
		Label l = (Label) observable;
		Punt[] p = (Punt[]) l.getDepend();
		Numbers x0, y0, x1, y1, x2, y2, x3, y3;
		x0 = p[0].getX();
		y0 = p[0].getY();
		x1 = p[1].getX();
		y1 = p[1].getY();
		x2 = p[2].getX();
		y2 = p[2].getY();
		x3 = p[3].getX();
		y3 = p[3].getY();
		// parallel noodzakelijk
		Numbers dx0 = Numbers.sub(x0, x1);
		Numbers dx2 = Numbers.sub(x2, x3);
		Numbers dy0 = Numbers.sub(y0, y1);
		Numbers dy2 = Numbers.sub(y2, y3);
		Numbers cros = Numbers.sub(Numbers.mul(dx0, dy2), Numbers.mul(dx2, dy0));
		l.setDefined(Numbers.abs(cros).doubleValue() < 0.000001);
		if(l.isDefined())
		{
			Numbers value = Numbers.add(Numbers.mul(dx0, dx2), Numbers.mul(dy0, dy2));
			Numbers div   = Numbers.add(Numbers.sqr(dx2), Numbers.sqr(dy2));
			if(!Numbers.ZERO .equals(div))
			{	setStringValue(l,Numbers.div(value, div));
			} else {
				l.setDefined(false);
				setStringValue(l,Numbers.div(Numbers.ONE, div)); // value is ook 0
			}
		} else {
			setStringValue(l,Numbers.createDouble(Double.NaN));
		}
		
	
	}

}
