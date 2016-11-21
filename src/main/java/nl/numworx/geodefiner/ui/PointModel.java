package nl.numworx.geodefiner.ui;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import nl.numworx.geodefiner.common.PointType;
import nl.numworx.geodefiner.common.UIModel;
import nl.uu.fi.dwo.interaction.client.json.ObjectMap;
import fi.euclides.model.Punt;
import fi.euclides.model.algo.FreePoint;
import fi.euclides.util.DefaultAdapter;

public class PointModel extends ColorModel<Punt> implements UIModel<Punt, UIEditor> {
	int   size = 5;
	PointType  type = PointType.DISK;
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
	
	public void fromMap(ObjectMap map) {
		super.fromMap(map);
		size  = map.getInt("size");
		type  = PointType.valueOf( map.getString("type"));
		rigid = map.getBoolean("rigid", true);
	}
	
	public UIEditor editor() {
		return new PointPane(this);
	}

	public UIModel<Punt, UIEditor> init(Punt item) {
		this.rigid = !item.isFree();
		return super.init(item);
	}

}
