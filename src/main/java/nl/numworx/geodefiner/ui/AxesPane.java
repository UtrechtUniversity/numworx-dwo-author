package nl.numworx.geodefiner.ui;

import javax.swing.JCheckBox;

public class AxesPane extends LinePane<AxesModel> {

	JCheckBox numbers;
	
	public AxesPane(AxesModel model) {
		super(model);
		numbers = new JCheckBox("numbers");
		numbers.setSelected(model.numbers);
		chooser.getPreviewPanel().add(numbers);
	}

	@Override
	public void commit() {
		model.numbers = numbers.isSelected();
		super.commit();
	}

}
