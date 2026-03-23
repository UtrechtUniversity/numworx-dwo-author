package nl.numworx.fsm.shared;

import java.util.Map;
import java.util.TreeMap;

import fi.euclides.event.NameMapper;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Punt;
import fi.euclides.util.DefaultAdapter;

public class FSMMapper implements NameMapper {

	
	Map<String, Destroyable> points;
	Map<String, Destroyable> edges;
	
	
	public FSMMapper() {
		super();
		points = new TreeMap<>();
		edges = new TreeMap<>();
	}

	@Override
	public Destroyable fromString(String name) {
		return edges.getOrDefault(name, points.get(name));
	}

	@Override
	public Punt getO() {
		return null;
	}

	@Override
	public Punt getU() {
		return null;
	}

	@Override
	public String toString(Destroyable destroyable) {
		return destroyable.adapt(String.class);
	}

	@Override
	public void rename(Destroyable p, String name) {
		if (name != null && name.isEmpty()) name = null; // only null, not empty
		Map<String, Destroyable> m = edges;
		if (p instanceof Punt) {
			m = points;
		}
		String old = toString(p);
		Destroyable d = name != null ? m.get(name) : null;
		
			if (p == d) {
				// same? old == name?
			} else if (d != null) {
				// don't
			} else {
				if (old != null) m.remove(old, p);
				if (name != null) m.put(name, p);
				DefaultAdapter.getDefault(p).put(String.class, name);
			}
		

	}

}
