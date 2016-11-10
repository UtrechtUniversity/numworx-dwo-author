package nl.numworx.geodefiner.common;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.math.Numbers;
import fi.euclides.proof.LabelValue;
import fi.euclides.util.Observable;

public class Interval extends LabelValue {

	public Interval() {
		super("..");
	}

	@Override
	public String getSymbolicValue(Label l) {
		Destroyable[] labels = l.getDepend();
		String a = labels[0].toString();
		String b = labels[1].toString();
		return a + " .. " + b;
	}

	@Override
	public Destroyable[] createDepend() {
		return new Label[2];
	}

	@Override
	public void update(Observable observable, Object arg) {
		Label l = (Label) observable;
		Destroyable[] minmax = l.getDepend();
		Numbers min = ((Label)minmax[0]).value;
		Numbers max = ((Label)minmax[1]).value;
		if(l.value == null) {
			setStringValue(l, Numbers.div(Numbers.add(max, min), Numbers.TWO));
		} else if(min.doubleValue() > l.value.doubleValue()) {
			setStringValue(l, min);
		} else if(max.doubleValue() < l.value.doubleValue()) {
			setStringValue(l, max);
		}
	}

}
