package nl.numworx.geodefiner.ui;

import javax.inject.Inject;
import javax.swing.BoxLayout;

import fi.beans.numworxlf.JCheckBox;
import fi.euclides.model.Destroyable;
import nl.numworx.geodefiner.common.AbstractDashModel;
import nl.numworx.geodefiner.common.LineType;
import nl.numworx.geodefiner.common.UIModel;

public class DashModel extends AbstractDashModel<UIEditor> implements UIModel<Destroyable, UIEditor> {

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

	@Inject DashModel() {
	}

	@Override
	public UIEditor editor() {
		return new DashEditor();
	}

}
