package nl.numworx.geodefiner.ui;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import fi.euclides.model.Punt;
import fi.euclides.model.algo.FreePoint;
import fi.euclides.util.DefaultAdapter;

public class PointModel extends ColorModel<Punt> implements UIModel<Punt> {
	enum Type { DISK }
	
	int   size = 5;
	Type  type = Type.DISK;
	boolean rigid = true;

	public void install(Punt item) {
		if(item instanceof FreePoint) {
			FreePoint r = (FreePoint) item;
			r.setFree(!rigid);
		}
		super.install(item);
	}

	public Map<String, Object> toMap() {
		Map<String, Object> map = super.toMap();
		map.put("size", size);
		map.put("type", type.name());
		map.put("rigid", rigid);
		return map;
	}
	
	public void fromMap(Map<String, Object> map) {
		super.fromMap(map);
		size  = ((Number) map.get("size")).intValue();
		type  = Type.valueOf((String) map.get("type"));
		rigid   = !Boolean.FALSE.equals( map.get("rigid"));
	}
	
	public UIEditor editor() {
		return new PointPane(this);
	}

	public UIModel<Punt> init(Punt item) {
		this.rigid = !item.isFree();
		return super.init(item);
	}

}
