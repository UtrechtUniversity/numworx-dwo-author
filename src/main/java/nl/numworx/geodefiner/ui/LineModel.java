package nl.numworx.geodefiner.ui;

import java.awt.BasicStroke;
import java.awt.Stroke;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import nl.numworx.geodefiner.IsLineType;
import nl.numworx.geodefiner.common.LineType;
import nl.numworx.geodefiner.common.UIModel;
import nl.numworx.geodefiner.merge.RenameAction;
import nl.uu.fi.dwo.interaction.client.json.ObjectMap;
import fi.euclides.event.Tracker;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Lijn;
import fi.euclides.util.DefaultAdapter;

public class LineModel extends ColorModel<Destroyable> {

	LineType type = LineType.SOLID;
	float width = 1.0f;
	boolean rigid; // default beweeglijk

	public UIModel<Destroyable, UIEditor> init(Lijn item) {
		return super.init(item);
	}

	public String toString() {
		return "line";
	}

	public Map<String, Object> toMap() {
		Map<String, Object> map = super.toMap();
		map.put("width", Double.valueOf(width)); // float not supported?
		map.put("type", type.name());
		map.put("rigid", rigid);
		return map;
	}

	public void fromMap(ObjectMap map) {
		super.fromMap(map);
		rigid = map.getBoolean("rigid", false);
		
		try {
			width = (float) map.getDouble("width");
			if(Float.isNaN(width)) width = 1.0f;
		} catch (Exception e) {
			width = 1.0f;
		}
		try {
			type  = LineType.valueOf(map.getString("type"));
		} catch (Exception e) {
			type = LineType.SOLID;
		}
	}

	public UIEditor editor() {
		return new LinePane(this);
	}

	static final float[][]  dashes = { null, { 1f, 3f }, { 5f, 3f } , { 5f, 3f, 1f, 3f } };
	
	@Override
	public void install(Destroyable item) {
		Stroke stroke = getStroke(type, width);
		DefaultAdapter adapter = DefaultAdapter.getDefault(item);
		adapter.put(Stroke.class, stroke);
		adapter.put(Boolean.valueOf(rigid));
		super.install(item);
	}

	public static Stroke getStroke(LineType type, float width) {
		float[] dash = dashes[type.ordinal()];
		if (dash != null) {
			dash = new float[dash.length];
			for(int i = 0; i < dash.length; i++) {
				dash[i] = width * dashes[type.ordinal()][i];
			}
		}
		BasicStroke stroke = new BasicStroke(width,BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
		return stroke;
	}

	@Deprecated
	public static Stroke getStroke(LineType type) {
		return getStroke(type, 1.0f);
	}
	
	LineModel() {}

	@Inject LineModel(Tracker tracker, Optional<RenameAction> ra) {
		this();
		set(tracker);
		setRename(ra);
	}

	@Override
	public void installLight() {
		Stroke stroke = getStroke(type, width);
		DefaultAdapter adapter = DefaultAdapter.getDefault(item);
		adapter.put(Stroke.class, stroke);
		
		super.installLight();
	}
	
	@Override
	public void fromLightMap(ObjectMap map) {
		super.fromLightMap(map);
		if (map.containsKey("type")) {
			type = LineType.valueOf(map.getString("type"));
		} else {
			type = new IsLineType().getLineTypeA(item);
		}
		if (map.containsKey("width")) {
			width = (float) map.getDouble("width");
		} else
			width = getLineWidth(item);
	}
	
	public static float getLineWidth(Destroyable item) {
		float width;
		Stroke s = item.adapt(Stroke.class);
		if (s instanceof BasicStroke) {
			width = ((BasicStroke) s).getLineWidth();
		} else
			width = 1.0f;
		return width;
	}

	@Override
	public Map<String,Object> toLightMap() {
		Map<String,Object> m = super.toLightMap();
		m.put("type", type.name());
		return m;
	}
}
