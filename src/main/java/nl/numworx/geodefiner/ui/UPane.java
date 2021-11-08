package nl.numworx.geodefiner.ui;

import javax.swing.Box;
import javax.swing.JLabel;

import fi.beans.numworxlf.JFormattedTextField;
import fi.euclides.model.HorizontalPunt;
import fi.euclides.model.math.Numbers;
import nl.numworx.geodefiner.Messages;

class UPane extends PointPane<UModel> {

	JFormattedTextField dField;
	private HorizontalPunt u;
	
	public UPane(UModel model) {
		super(model);
		dField = new JFormattedTextField();
		u = (HorizontalPunt) model.item;
		dField.setValue(u.getDistance().doubleValue());dField.setColumns(5);
		dField.setMaximumSize(dField.getPreferredSize());
		Box panel = Box.createHorizontalBox();
		panel.add(new JLabel(Messages.getString("UPane.0"))); //$NON-NLS-1$
		panel.add(dField);
		panel.add(new JLabel("px"));panel.add(Box.createGlue()); //$NON-NLS-1$
		content.add(panel);
		setSizes();
	}

	@Override
	public void commit() {
		commitFields(dField);
		u.setDistance(Numbers.createInteger(((Number) dField.getValue()).intValue()));
		super.commit();
	}

}
