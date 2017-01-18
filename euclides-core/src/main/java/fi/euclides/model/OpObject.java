package fi.euclides.model;

import fi.euclides.model.algo.PointOnAlgorithm;
import fi.euclides.model.math.Numbers;



// TODO refactor PuntOpXXXX mbv deze interface
public interface OpObject<T extends Destroyable> {
	public PointOnAlgorithm<T> getAlgo();
	public PuntOp<T> pointOn(Numbers x, Numbers y);
}
