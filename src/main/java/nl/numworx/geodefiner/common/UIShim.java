package nl.numworx.geodefiner.common;

import java.util.Map;

import fi.euclides.event.Tracker;
import fi.euclides.model.Destroyable;
import nl.uu.fi.dwo.interaction.client.json.ObjectMap;

public class  UIShim<D extends Destroyable, T> implements UIModel<D, T> {
	final UIModel<D, T> delegate;
	final private Map<String, Map<String, Object>> state;
	final private Tracker tracker;
	
	public UIShim(UIModel<D, T> delegate, Map<String, Map<String, Object>> state, Tracker tracker) {
		this.delegate = delegate;
		this.state = state;
		this.tracker = tracker;
	}


	public boolean set;

	public UIModel<D, T> init(D item) {
		return delegate.init(item);
	}

	public UIModel<D, T> init2(Destroyable d) {
		return delegate.init2(d);
	}

	public void install() {
	}

	public Map<String, Object> toMap() {
		if (set)
			return delegate.toMap();
		return null;
	}

	public void fromMap(ObjectMap value) {
		delegate.fromMap(value);
		set = true;
	}

	public T editor() {
		return delegate.editor();
	}

	public void setVisible(boolean visible) {
		delegate.setVisible(visible);
	}

	public UIModel<D, T> set(Tracker tracker) {
		return delegate.set(tracker);
	}

	public void installLight() {
	}

	public void install(Destroyable buildPunt) {
		
		if(set)
		{
			delegate.install(buildPunt);
			String name = tracker.getMapper().toString(buildPunt);
			state.put(name, delegate.toMap());
		}
	}
	
}