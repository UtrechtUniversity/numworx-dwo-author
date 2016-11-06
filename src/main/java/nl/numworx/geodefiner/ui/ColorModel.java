package nl.numworx.geodefiner.ui;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import nl.numworx.geodefiner.common.UIModel;
import nl.numworx.geodefiner.ui.PointModel.Type;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Punt;
import fi.euclides.model.algo.FreePoint;
import fi.euclides.util.DefaultAdapter;

public class ColorModel<T extends Destroyable> implements UIModel<T, UIEditor> {

	T item;
	Color color = Color.black;
	boolean visible;
	public  void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void install(T item) {
		DefaultAdapter adapter = DefaultAdapter.getDefault(item);
		adapter.put(color);
		item.setVisible(visible);
	}

	public void install() {
		install(item);
	}

	public UIModel<T, UIEditor> init(T item) {
		this.item = item;
		this.color = item.getAdapter().adapt(Color.class);
		if(color == null) color = Color.black;
		this.visible = item.isVisible();
		return this;
	}

	public Map<String, Object> toMap() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("color", color.getRGB());
		map.put("visible", visible);
		return map;
	}

	public void fromMap(Map<String, Object> map) {
		color = new Color( ((Number) map.get("color")).intValue(), true);
		visible = !Boolean.FALSE.equals( map.get("visible"));
	}

	public UIEditor editor() {
		return new ColorPane(this);
	}

}
