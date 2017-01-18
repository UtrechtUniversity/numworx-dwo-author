package fi.euclides.model;

import fi.euclides.model.algo.FreePoint;
import fi.euclides.model.math.Numbers;

public class VrijPunt extends Punt implements FreePoint {

	private boolean free = true;

	public VrijPunt() {
	}

	public boolean isFree() {
		return free;
	}

	public VrijPunt(double x, double y) {
		super(x, y);
	}

	public VrijPunt(Numbers x, Numbers y) {
		super(x, y);
	}

	public void setFree(boolean free) {
		this.free = free;
		
	}

	public <T> T adapt(Class<T> clz) {
		if(clz == FreePoint.class) return (T) this;
		return super.adapt(clz);
	}

}
