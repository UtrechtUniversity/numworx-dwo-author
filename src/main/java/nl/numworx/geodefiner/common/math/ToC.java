package nl.numworx.geodefiner.common.math;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Model;
import fi.euclides.model.Punt;
import fi.euclides.model.math.Numbers;
import fi.euclides.proof.LabelValue;
import fi.euclides.util.Observable;

public class ToC extends LabelValue {

	public static final String TYPE = "toc";
	
	public ToC() {
		super(TYPE);
	}

	@Override
	public String getSymbolicValue(Label l) {
		return l.getString();
	}

	@Override
	public Destroyable[] createDepend() {
		return new Punt[1];
	}

	@Override
	public void update(Observable observable, Object arg) {
		Label l = (Label) observable;
		Destroyable d = l.getDepend()[0];
		if(arg == null) recalc(l, (Punt) d);	
	}

	void recalc(Label l, Punt d) {
		if(!l.isDefined())
		{
			setValue(l,Numbers.NaN);
		}
		Numbers x,y;
		Model m = getTracker().getModel();
		Punt O = m.getO();
		Punt U = m.getU();
		if (d == O) 
		{
			setValue(l, Numbers.ZERO);
			return;
		}
		else if (d == U)
		{
			setValue(l, Numbers.ONE);
			return;
		}
		else
		{
			x = (d).getX();
			y = (d).getY();
		}
		Numbers Ox = O.getX();
		Numbers Oy = O.getY();
		Numbers Ux = U.getX();
		Numbers scale = Numbers.abs(Numbers.sub(Ux,Ox));
		x = Numbers.div(Numbers.sub(x, Ox), scale);
		y = Numbers.div(Numbers.sub(Oy, y), scale);
		setValue(l, Numbers.createComplex(x, y));
	}

	protected void setValue(Label l, Numbers value) {
		l.setValue(value);
	}


}
