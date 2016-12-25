package nl.numworx.geodefiner.common;

import fi.euclides.model.math.Numbers;

public class StepValue {
	private final Numbers step;

	public StepValue(Numbers step) {
		if(step.doubleValue() <= 0.0)
			step = Numbers.ZERO;
		this.step = step;
	}
	
	public Numbers step(Numbers value) {
		if(step == Numbers.ZERO) {
			return value;
		}
		if(step == Numbers.ONE)
			return Numbers.round(value);
		else {
			return Numbers.mul(step, Numbers.round(Numbers.div(value, step)));
		}	
	}

	public Numbers stepUp(Numbers value) {
		if(step == Numbers.ZERO) {
			return value;
		}
		if(step == Numbers.ONE)
			return Numbers.ceiling(value);
		else {
			return Numbers.mul(step, Numbers.ceiling(Numbers.div(value, step)));
		}	
	}
	
	public Numbers stepDown(Numbers value) {
		if(step == Numbers.ZERO) {
			return value;
		}
		if(step == Numbers.ONE)
			return Numbers.floor(value);
		else {
			return Numbers.mul(step, Numbers.floor(Numbers.div(value, step)));
		}	
	}

	public Double doubleValue() {
		return step.doubleValue();
	}
}
