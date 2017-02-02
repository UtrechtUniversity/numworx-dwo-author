package nl.numworx.geodefiner.common;

import fi.euclides.model.Label;
import fi.euclides.model.math.Numbers;

public class StepValue {
	private final Numbers step;
	private final Label min;

	private Numbers getMinValue() {
		if(min != null) return min.value;
		return Numbers.ZERO;
	}
	
	public StepValue(Numbers step, Label min) {
		this.min = min;
		if(step.doubleValue() <= 0.0)
			step = Numbers.ZERO;
		this.step = step;
	}
	
	public StepValue(Numbers step) {
		this(step, null);
	}
	
	public Numbers step(Numbers value) {
		if(step == Numbers.ZERO) {
			return value;
		}
		Numbers minValue = getMinValue();
		value = Numbers.sub(value, minValue);
		if(step == Numbers.ONE)
			value =  Numbers.round(value);
		else {
			value =  Numbers.mul(step, Numbers.round(Numbers.div(value, step)));
		}	
		return Numbers.add(value, minValue);
	}

	public Numbers stepUp(Numbers value) {
		if(step == Numbers.ZERO) {
			return value;
		}
		Numbers minValue = getMinValue();
		value = Numbers.sub(value, minValue);
		if(step == Numbers.ONE)
			value = Numbers.ceiling(value);
		else {
			value = Numbers.mul(step, Numbers.ceiling(Numbers.div(value, step)));
		}
		return Numbers.add(value, minValue);
	}
	
	public Numbers stepDown(Numbers value) {
		if(step == Numbers.ZERO) {
			return value;
		}
		Numbers minValue = getMinValue();
		value = Numbers.sub(value, minValue);
		if(step == Numbers.ONE)
			value = Numbers.floor(value);
		else {
			value = Numbers.mul(step, Numbers.floor(Numbers.div(value, step)));
		}
		return Numbers.add(minValue, value);
	}

	public Double doubleValue() {
		return step.doubleValue();
	}
}
