package nl.numworx.geodefiner.common.math;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Model;
import fi.euclides.model.Punt;
import fi.euclides.model.Segment;
import fi.euclides.model.math.Numbers;
import fi.euclides.proof.LabelValue;
import fi.euclides.util.Observable;

public class ToComplex extends LabelValue {

	public ToComplex() {
		super("complex");
	}

	@Override
	public String getSymbolicValue(Label l) {
		return s(l.getDepend()[0]);
	}

	@Override
	public Destroyable[] createDepend() {
		return new Destroyable[1];
	}

	@Override
	public void update(Observable observable, Object arg) {
		Label l = (Label) observable;
		Destroyable d = l.getDepend()[0];
		if(arg == null) recalc(l, d);
	
	}

	void recalc(Label l, Destroyable d) {
		Numbers x,y;
		Model m = getTracker().getModel();
		Punt O = m.getO();
		Punt U = m.getU();
		if (d == O) 
		{
			setStringValue(l, Numbers.ZERO);
			return;
		}
		else if (d == U)
		{
			setStringValue(l, Numbers.ONE);
			return;
		}
		else
		if(d instanceof Punt) {
			x = ((Punt) d).getX();
			y = ((Punt) d).getY();
		} else if (d instanceof Segment) {
			x = ((Segment) d).getDXn();
			y = ((Segment) d).getDYn();
		} else {
			x = Numbers.NaN;
			y = Numbers.NaN;
		}
		Numbers Ox = O.getX();
		Numbers Oy = O.getY();
		Numbers Ux = U.getX();
		Numbers scale = Numbers.abs(Numbers.sub(Ux,Ox));
		x = Numbers.div(Numbers.sub(x, Ox), scale);
		y = Numbers.div(Numbers.sub(Oy, y), scale);
		setStringValue(l, Numbers.createComplex(x, y));
	}


}
