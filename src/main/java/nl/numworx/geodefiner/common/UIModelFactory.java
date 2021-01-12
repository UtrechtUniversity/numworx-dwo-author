package nl.numworx.geodefiner.common;

import fi.euclides.model.Destroyable;

public abstract class UIModelFactory {

	public UIModel<?, ?> build(Destroyable d) {
		return null;
	}

	public UIModel<?, ?> lightBuild(Destroyable d) {
		return build(d);
	}

}
