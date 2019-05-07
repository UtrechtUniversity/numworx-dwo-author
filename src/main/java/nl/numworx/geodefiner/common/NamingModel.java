package nl.numworx.geodefiner.common;

import java.util.ArrayList;
import java.util.Map;

import fi.euclides.event.NameMapper;
import fi.euclides.event.Tracker;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Model;
import fi.euclides.model.Punt;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.DefaultAdapter;
import nl.numworx.geodefiner.common.index.Indexed;

public class NamingModel implements NameMapper {

	private Model model;
	private Map<String,Destroyable> cache;
	
	public NamingModel(Tracker viewer, Map<String,Destroyable> map) {
		this.model = viewer.getModel();
		this.cache = map;
	}

	public NamingModel(Model viewer, Map<String,Destroyable> map) {
		this.model = viewer;
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

	public Model getModel() {
		return model;
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
		if (d == null) {
			// when?
			return "null";
		}
		if (d instanceof Indexed) {
		    Destroyable[] depend = d.getDepend();
		    return toString(depend[0]) + "_" + toValue(depend[1]);
		}
		String s = d.adapt(String.class);
		if(s == null)
			return "%" + getModel().toString(d);
		return s;
	}

	private String toValue(Destroyable destroyable) {
    if (destroyable instanceof Label) {
      return Numbers.toString(((Label) destroyable).value);
    }
    return toString(destroyable);
  }

  public void clear() {
		cache.clear();
	}
}
