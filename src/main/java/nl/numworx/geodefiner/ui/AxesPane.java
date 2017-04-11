package nl.numworx.geodefiner.ui;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JLabel;

public class AxesPane extends LinePane<AxesModel> {

	JCheckBox numbers;
	
	public AxesPane(AxesModel model) {
		super(model);
		Box hbox = Box.createHorizontalBox();
		numbers = new JCheckBox("getallen");
		numbers.setSelected(model.numbers);
		hbox.add(new JLabel("Bij de as"));
		hbox.add(numbers);
		hbox.add(Box.createGlue());
		add(hbox);
	}

	@Override
	public void commit() {
		model.numbers = numbers.isSelected();
		super.commit();
	}

}
