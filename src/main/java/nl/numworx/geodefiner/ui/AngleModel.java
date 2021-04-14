package nl.numworx.geodefiner.ui;

import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import fi.euclides.event.Tracker;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.util.DefaultAdapter;
import nl.numworx.geodefiner.common.AngleType;
import nl.numworx.geodefiner.merge.RenameAction;
import nl.uu.fi.dwo.interaction.client.json.ObjectMap;

public class AngleModel extends TextModel {
	
	boolean rad = true;

	@Inject AngleModel(Tracker t, Optional<RenameAction> ra) {
		super(t, ra);
	}

	public boolean isRad() {
		return rad;
	}

	public void setRad(boolean rad) {
		this.rad = rad;
	}

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> map = super.toMap();
		map.put("rad", rad);
		return map;
	}

	@Override
	public void fromMap(ObjectMap map) {
		map.getBoolean("rad", true);
		super.fromMap(map);
	}

	@Override
	public void install0(Destroyable item) {
		DefaultAdapter.getDefault(item).put(rad ? AngleType.RAD : AngleType.DEGREE);
		super.install0(item);
	}

	@Override
	public UIEditor editor() {
		setdxy();
		return new AnglePane(this);
	}

	@Override
	public void install(Label item) {
		super.install(item);
		item.getRegistered().update(item, null);
	}



}
