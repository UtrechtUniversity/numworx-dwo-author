package nl.numworx.geodefiner.ui;

import java.awt.LayoutManager;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.swing.BoxLayout;

import fi.beans.numworxlf.JCheckBox;
import fi.euclides.event.Tracker;
import fi.euclides.model.Destroyable;
import nl.numworx.geodefiner.common.LineType;
import nl.numworx.geodefiner.common.UIModel;
import nl.uu.fi.dwo.interaction.client.json.ObjectMap;

public class DashModel implements UIModel<Destroyable, UIEditor> {

	private class DashEditor extends UIEditor {

		JCheckBox[] boxes;
		
		@Override
		public void commit() {
			for (int i = 0; i < selected.length; i++) {
				selected[i] = boxes[i].isSelected();
			}
		}

		DashEditor() {
			super();
			BoxLayout layout = new BoxLayout(content, BoxLayout.PAGE_AXIS);
			content.setLayout(layout);
			LineType[] values = LineType.values();
			boxes = new JCheckBox[values.length];
			for (int i = 0; i < boxes.length; i++) {
				boxes[i] = new JCheckBox(values[i].toString());
				boxes[i].setSelected(selected[i]);
				add(boxes[i]);
			}
		}

	}

	private final boolean[] selected;
	
	@Inject DashModel() {
		selected = new boolean[LineType.values().length];
		allTrue();
	}

	public boolean isSelected(LineType t) {
		return selected[t.ordinal()];
	}
	
	public boolean isSelected(int i) {
		return selected[i];
	}
	
	private void allTrue() {
		for (int i = 0; i < selected.length; i++) {
			selected[i] = true;
		}
	}

	@Override
	public UIModel<Destroyable, UIEditor> init(Destroyable item) {
		return this;
	}

	@Override
	public UIModel<Destroyable, UIEditor> init2(Destroyable d) {
		return this;
	}

	@Override
	public void install() {
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
	public UIEditor editor() {
		return new DashEditor();
	}

	@Override
	public void setVisible(boolean visible) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public UIModel<Destroyable, UIEditor> set(Tracker tracker) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void install(Destroyable buildPunt) {
		// TODO Auto-generated method stub
		
	}

}
