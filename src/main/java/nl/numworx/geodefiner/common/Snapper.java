package nl.numworx.geodefiner.common;

import fi.euclides.model.algo.FreePoint;

public abstract class Snapper {

	protected boolean gravity;
	
	public void setGravity(boolean gravity) {
		this.gravity = gravity;
	}

	public boolean isGravity() {
		return gravity;
	}
	
	public void snap(FreePoint fp) {};
}
