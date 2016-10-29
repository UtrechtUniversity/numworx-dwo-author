package nl.numworx.geodefiner.ui;

import java.util.Map;

import fi.euclides.model.Destroyable;

public interface UIModel<T extends Destroyable> {
	
	UIModel<T> init(T item);
	void install();
	Map<String, Object> toMap();
	void fromMap(Map<String,Object> map);
	UIEditor editor();
	void setVisible(boolean visible);
}
