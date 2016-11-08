package nl.numworx.geodefiner.ui;

import java.awt.BasicStroke;
import java.awt.Stroke;
import java.util.Map;

import javax.swing.JLabel;

import nl.numworx.geodefiner.common.UIModel;
import nl.uu.fi.dwo.interaction.client.json.ObjectMap;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Lijn;
import fi.euclides.util.DefaultAdapter;

public class LineModel extends ColorModel<Destroyable> {

	LineType type = LineType.SOLID;
	float width = 1.0f;

	public UIModel<Destroyable, UIEditor> init(Lijn item) {
		return super.init(item);
	}

	public Map<String, Object> toMap() {
		Map<String, Object> map = super.toMap();
		map.put("width", width);
		map.put("type", type.name());
		return map;
	}

	public void fromMap(ObjectMap map) {
		super.fromMap(map);
		try {
			width = (float) map.getDouble("width");
		} catch (Exception e) {
		}
		try {
			type  = LineType.valueOf(map.getString("type"));
		} catch (Exception e) {
		}
	}

	public UIEditor editor() {
		return new LinePane(this);
	}

	float[][]  dashes = { null, { 1f, 3f }, { 5f, 3f } , { 5f, 3f, 1f, 3f } };
	
	@Override
	public void install(Destroyable item) {
		float[] dash = dashes[type.ordinal()];
		BasicStroke stroke = new BasicStroke(width,BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
		DefaultAdapter.getDefault(item).put(Stroke.class, stroke);
		super.install(item);
	}

}
