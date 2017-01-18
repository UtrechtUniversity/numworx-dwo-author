package fi.euclides.model.algo;

import fi.euclides.model.math.Numbers;
import fi.euclides.util.Adaptee;

public interface FreePoint extends Adaptee {
	void setFree(boolean free);
	boolean isFree();
	
	Numbers getX();
	Numbers getY();
	void setXY(Numbers x, Numbers y);
	
}
