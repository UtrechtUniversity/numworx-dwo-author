package fi.euclides.model.algo;

import fi.euclides.model.math.Numbers;

public interface PointOnAlgorithm<T> extends PointAlgorithm {
	public void recalc(T on, FreePoint punt, double x, double y);
	public void recalc(T on, FreePoint punt, Numbers x, Numbers y);
	public void update(T on, FreePoint punt);
}
