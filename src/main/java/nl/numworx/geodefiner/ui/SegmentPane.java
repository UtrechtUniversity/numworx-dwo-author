package nl.numworx.geodefiner.ui;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import nl.numworx.geodefiner.common.Tips;

class SegmentPane extends LinePane<SegmentModel> {

	JComboBox<Tips> tips;
	
	public SegmentPane(SegmentModel model) {
		super(model);
		tips = new JComboBox<Tips>(Tips.values());
		tips.setSelectedItem(model.tip);
		Box panel = Box.createHorizontalBox();
		panel.add(new JLabel("pijlpunten"));
		panel.add(tips);
		panel.add(Box.createGlue());
		add(panel);
	}

	@Override
	public void commit() {
		model.tip = (Tips) tips.getSelectedItem();
		super.commit();
	}

}
