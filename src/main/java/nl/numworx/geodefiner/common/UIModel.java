package nl.numworx.geodefiner.common;

import java.util.Map;

import nl.uu.fi.dwo.interaction.client.json.ObjectMap;
import fi.euclides.model.Destroyable;

public interface UIModel<T extends Destroyable, U> {
	
	UIModel<T, U> init(T item);
	void install();
	Map<String, Object> toMap();
	void fromMap(ObjectMap value);
	U editor();
	void setVisible(boolean visible);
}
