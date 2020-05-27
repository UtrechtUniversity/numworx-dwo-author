package nl.numworx.geodefiner.common;

import java.util.Map;

import nl.uu.fi.dwo.interaction.client.json.ObjectMap;
import fi.euclides.event.Tracker;
import fi.euclides.model.Destroyable;

public interface UIModel<T extends Destroyable, U> {
	
	UIModel<T, U> init(T item);
	UIModel<T, U> init2(Destroyable d);
	void install();
	Map<String, Object> toMap();
	void fromMap(ObjectMap value);
	U editor();
	void setVisible(boolean visible);
	UIModel<T, U> set(Tracker tracker);
	default void installLight() { }
	void install(Destroyable buildPunt);
}
