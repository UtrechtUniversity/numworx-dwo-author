package nl.numworx.geodefiner.ui;

import java.util.Map;

import nl.numworx.geodefiner.Instance;
import nl.numworx.geodefiner.Instance.Snapper;
import nl.numworx.geodefiner.common.UIModel;
import nl.uu.fi.dwo.interaction.client.json.ObjectMap;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Lijn;
import fi.euclides.model.Locus;

public class GridModel extends LineModel {
	boolean gravity;

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> map = super.toMap();
		map.put("gravity", gravity);
		return map;
	}

	@Override
	public void fromMap(ObjectMap map) {
		gravity = map.getBoolean("gravity", false);
		super.fromMap(map);
	}

	@Override
	public UIEditor editor() {
		return new GridPane(this);
	}

	@Override
	public void install(Destroyable item) {
		super.install(item);
		Snapper snapper = tracker.adapt(Snapper.class);
		snapper.setGravity(gravity);
	}
	
	public UIModel<Destroyable, UIEditor> init(Locus item) {
		return super.init(item);
	}
}
