package nl.numworx.geodefiner.ui;

import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import fi.euclides.event.Tracker;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Lijn;
import fi.euclides.util.DefaultAdapter;
import nl.numworx.geodefiner.common.Tips;
import nl.numworx.geodefiner.common.UIModel;
import nl.numworx.geodefiner.merge.RenameAction;
import nl.uu.fi.dwo.interaction.client.json.ObjectMap;

public class SegmentModel extends LineModel {

	Tips tip = Tips.NOTIP;

	public String toString() {
		return "segment";
	}

	@Override
	public UIModel<Destroyable, UIEditor> init(Lijn item) {
		return super.init(item);
	}

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> map = super.toMap();
		if(tip != Tips.NOTIP) map.put("tip", tip.name());
		return map;
	}

	@Override
	public void fromMap(ObjectMap map) {
		try { 
			tip = Tips.valueOf(map.getString("tip"));
		} catch (Exception e) {
			tip = Tips.NOTIP;
		}
		super.fromMap(map);
	}

	@Override
	public UIEditor editor() {
		return new SegmentPane(this);
	}

	@Override
	public void install(Destroyable item) {
		super.install(item);
		DefaultAdapter adapter = DefaultAdapter.getDefault(item);
		if(tip == Tips.NOTIP) {
			adapter.put(Tips.class, null);
			adapter.put(Float.class, null);
		} else {
			adapter.put(tip);
			adapter.put(width);
		}
	}

	@Inject SegmentModel(Tracker t, Optional<RenameAction> ra) {
		set(t);
		setRename(ra);
	}

	@Override
	public void installLight() {
		DefaultAdapter adapter = DefaultAdapter.getDefault(item);
		if(tip == Tips.NOTIP) {
			adapter.put(Tips.class, null);
			adapter.put(Float.class, null);
		} else {
			adapter.put(tip);
			adapter.put(width);
		}
		super.installLight();
	}

	@Override
	public void fromLightMap(ObjectMap map) {
		try { 
			tip = Tips.valueOf(map.getString("tip"));
		} catch (Exception e) {
			tip = Tips.NOTIP;
		}
		super.fromLightMap(map);
	}

	@Override
	public Map<String, Object> toLightMap() {
		Map<String, Object> map = super.toLightMap();
		if(tip != Tips.NOTIP) map.put("tip", tip.name());
		return map;
	}
	
}
