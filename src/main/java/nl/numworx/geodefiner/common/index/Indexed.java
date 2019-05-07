package nl.numworx.geodefiner.common.index;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Groep;
import fi.euclides.model.Label;
import fi.euclides.util.Observable;

public interface Indexed<T extends Destroyable> {
	T asDestroyable();
	void setDelegate(T d);
	void destroy();
	T getDelegate();
	void changed();
	void notifyObservers();
	void setDefined(boolean defined);
	void setVisible(boolean visible);
}
