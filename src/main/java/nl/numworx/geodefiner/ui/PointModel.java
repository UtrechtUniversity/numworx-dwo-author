package nl.numworx.geodefiner.ui;

import java.util.Map;

import nl.numworx.geodefiner.common.PointType;
import nl.numworx.geodefiner.common.UIModel;
import nl.uu.fi.dwo.interaction.client.json.ObjectMap;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Groep;
import fi.euclides.model.Punt;
import fi.euclides.model.algo.FreePoint;
import fi.euclides.util.DefaultAdapter;

public class PointModel extends ColorModel<Destroyable> implements UIModel<Destroyable, UIEditor> {
	int   size = 5;
	PointType  type = PointType.DISK;
	boolean rigid = true;

	public void install(Destroyable item) {
		if(item instanceof FreePoint) {
			FreePoint r = (FreePoint) item;
			r.setFree(!rigid);
		} else if (item instanceof Groep) {
		  Groep g = (Groep) item;
		  if (g.prototype() instanceof FreePoint)
		    g.setFree(!rigid);
		}
		DefaultAdapter adapter = DefaultAdapter.getDefault(item);
		adapter.put(Float.valueOf(size));
		adapter.put(type);
		super.install(item);
	}

	public Map<String, Object> toMap() {
		Map<String, Object> map = super.toMap();
		map.put("size", size);
		map.put("type", type.name());
		map.put("rigid", rigid);
		return map;
	}
	
	public void fromMap(ObjectMap map) {
		super.fromMap(map);
		if(map.containsKey("size")) size  = map.getInt("size");
		if(map.containsKey("type")) type  = PointType.valueOf( map.getString("type"));
		rigid = map.getBoolean("rigid", true);
	}
	
	public UIEditor editor() {
		return new PointPane(this);
	}

	public UIModel<Destroyable, UIEditor> init(Punt item) {
		if (item != null) this.rigid = !item.isFree();
		return super.init(item);
	}

	public UIModel<Destroyable, UIEditor> init(Destroyable item) {
		return super.init(item);
	}
	
	public String toString() {
		return "point";
	}
}
