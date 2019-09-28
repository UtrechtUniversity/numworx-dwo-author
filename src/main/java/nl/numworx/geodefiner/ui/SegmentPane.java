package nl.numworx.geodefiner.ui;

import javax.swing.Box;
import fi.beans.numworxlf.JComboBox;
import javax.swing.JLabel;

import fi.euclides.util.Messages;
import nl.numworx.geodefiner.common.Tips;

class SegmentPane extends LinePane<SegmentModel> {

	JComboBox<Tips> tips;
	
	public SegmentPane(SegmentModel model) {
		super(model);
		tips = new JComboBox<Tips>(tips());
		tips.setSelectedItem(model.tip);
		Box panel = Box.createHorizontalBox();
		panel.add(new JLabel(Messages.getString("SegmentPane.1")));
		panel.add(tips);
		panel.add(Box.createGlue());
		add(panel);
	}

	Tips[] tips() {
		return Tips.values();
	}

	@Override
	public void commit() {
		model.tip = (Tips) tips.getSelectedItem();
		super.commit();
	}

}
