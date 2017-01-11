package nl.numworx.geodefiner.ui;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import nl.numworx.geodefiner.common.Tips;

class SegmentPane extends LinePane<SegmentModel> {

	JComboBox<Tips> tips;
	
	public SegmentPane(SegmentModel model) {
		super(model);
		tips = new JComboBox<Tips>(Tips.values());
		tips.setSelectedItem(model.tip);
		JComponent panel = chooser.getPreviewPanel();
		panel.add(tips);
	}

	@Override
	public void commit() {
		model.tip = (Tips) tips.getSelectedItem();
		super.commit();
	}

}
