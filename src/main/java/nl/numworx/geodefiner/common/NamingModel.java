package nl.numworx.geodefiner.common;

import java.util.ArrayList;
import java.util.Map;

import fi.euclides.event.NameMapper;
import fi.euclides.event.Tracker;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Model;
import fi.euclides.model.Punt;
import fi.euclides.util.DefaultAdapter;

public class NamingModel implements NameMapper {

	private Tracker viewer;
	private Map<String,Destroyable> cache;
	
	public NamingModel(Tracker viewer, Map<String,Destroyable> map) {
		this.viewer = viewer;
		this.cache = map;
	}

	public Destroyable fromString(String name) {
		Destroyable item = cache.get(name);
		if(item != null && toString(item).equals(name) && item.getIndex() > 0) return item; // cache hit		
		
		ArrayList<Destroyable> v = new ArrayList<Destroyable>(getModel().getPunten());
		v.addAll(getModel().getLijnen());
		for (Destroyable p : v) {
			if (name .equals( toString(p)) ) {
				cache.put(name, p);
				return p;
			}
		}
		return null;
	}

	private Model getModel() {
		return viewer.getModel();
	}

	public Punt getO() {
		return getModel().getO();
	}

	public Punt getU() {
		return getModel().getU();
	}

	public void rename(Destroyable p, String name) {
		DefaultAdapter.getDefault(p).put(name);
		cache.put(name,p);
	}

	@Override
	public String toString(Destroyable d) {
		String s = d.adapt(String.class);
		if(s == null)
			return getModel().toString(d);
		return s;
	}

}
