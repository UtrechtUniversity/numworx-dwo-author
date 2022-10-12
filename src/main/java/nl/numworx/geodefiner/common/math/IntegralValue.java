package nl.numworx.geodefiner.common.math;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.proof.LabelValue;
import fi.euclides.util.Observable;
import nl.numworx.geodefiner.common.Integral;

public class IntegralValue extends LabelValue {

	private static final String TYPE = "∫";


	public IntegralValue() {
		super(TYPE);
	}

	@Override
	public Destroyable[] createDepend() {
		return new Integral[1];
	}

	@Override
	public String getSymbolicValue(Label l) {		
		return getSubKey() + "(" + s(l.getDepend()[0]) + ")";
	}

	public void update(Observable observable, Object arg) {
		if(arg == Destroyable.DESTROY 
		|| arg == Destroyable.VISIBLE
		|| arg == Label.STATE
		|| arg == Destroyable.RENAME)
			return;
		
		Label l = (Label)observable;
		recalc(l, (Integral) l.getDepend()[0]);
	}


	private void recalc(Label l, Integral integral) {
		IntegralSum summer = new IntegralSum(getModel());
		integral.visitSegments(summer);
		setStringValue(l, summer.getSum());		
	}

}
