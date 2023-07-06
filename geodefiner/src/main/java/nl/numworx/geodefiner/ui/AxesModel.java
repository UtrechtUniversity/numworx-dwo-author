package nl.numworx.geodefiner.ui;

import java.util.Map;

import javax.inject.Inject;

import nl.uu.fi.dwo.interaction.client.json.ObjectMap;
import fi.euclides.event.Tracker;
import fi.euclides.model.Destroyable;

public class AxesModel extends LineModel {

	public boolean numbers;

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> map = super.toMap();
		if(numbers) map.put("numbers", Boolean.TRUE);
		return map;
	}

	@Override
	public void fromMap(ObjectMap map) {
		numbers = map.getBoolean("numbers", false);
		super.fromMap(map);
	}

	@Override
	public UIEditor editor() {
		return new AxesPane(this);
	}

	@Override
	public void install(Destroyable item) {
		super.install(item);
	}

	@Inject AxesModel(Tracker t) {
		set(t);
	}

}
