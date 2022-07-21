package nl.numworx.geodefiner.common;

import java.util.HashMap;
import java.util.Map;

import fi.euclides.event.Tracker;
import fi.euclides.model.Destroyable;
import nl.uu.fi.dwo.interaction.client.json.ObjectMap;

public abstract class AbstractDashModel<U> implements UIModel<Destroyable,U> {

	protected final boolean[] selected;

	public AbstractDashModel() {
		selected = new boolean[LineType.values().length];
		allTrue();
	}

	public boolean isSelected(LineType t) {
		return selected[t.ordinal()];
	}

	public boolean isSelected(int i) {
		return selected[i];
	}

	protected void allTrue() {
		for (int i = 0; i < selected.length; i++) {
			selected[i] = true;
		}
	}

	@Override
	public Map<String, Object> toMap() {
		Map<String,Object> map = new HashMap<>();
		for(LineType t: LineType.values()) {
			if (! selected[t.ordinal()])
				map.put(t.name(), selected[t.ordinal()]);
		}
		if (map.isEmpty()) return null;
		return map;
	}

	@Override
	public void fromMap(ObjectMap value) {
		if (value == null) {
			allTrue();
		} else {
			for (int i = 0; i < selected.length; i++) {
				selected[i] = value.getBoolean(LineType.values()[i].name(), true);
			}
		}
	}

	@Override
	public void setVisible(boolean visible) {
	}

	@Override
	public UIModel<Destroyable, U> set(Tracker tracker) {
		return this;
	}

	@Override
	public UIModel<Destroyable, U> init(Destroyable item) {
		return this;
	}

	@Override
	public UIModel<Destroyable, U> init2(Destroyable d) {
		return this;
	}

	@Override
	public void install() {
	}

	@Override
	public void install(Destroyable buildPunt) {
	}

}
