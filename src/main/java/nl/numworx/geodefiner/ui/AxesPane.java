package nl.numworx.geodefiner.ui;

import javax.swing.Box;
import fi.beans.numworxlf.JCheckBox;
import nl.numworx.geodefiner.Messages;

import javax.swing.JLabel;

public class AxesPane extends LinePane<AxesModel> {

	JCheckBox numbers;
	
	public AxesPane(AxesModel model) {
		super(model);
		Box hbox = Box.createHorizontalBox();
		numbers = new JCheckBox(Messages.getString("AxesPane.0")); //$NON-NLS-1$
		numbers.setSelected(model.numbers);
		hbox.add(new JLabel(Messages.getString("AxesPane.1"))); //$NON-NLS-1$
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
