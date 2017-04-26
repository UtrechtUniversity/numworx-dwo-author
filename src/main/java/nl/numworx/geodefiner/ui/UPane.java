package nl.numworx.geodefiner.ui;

import javax.swing.Box;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;

import fi.euclides.model.HorizontalPunt;
import fi.euclides.model.math.Numbers;

class UPane extends PointPane<UModel> {

	JFormattedTextField dField;
	private HorizontalPunt u;
	
	public UPane(UModel model) {
		super(model);
		dField = new JFormattedTextField();
		u = (HorizontalPunt) model.item;
		dField.setValue(u.getDistance().doubleValue());
		Box panel = Box.createHorizontalBox();
		panel.add(new JLabel("roostermaat"));
		panel.add(dField);
		panel.add(new JLabel("px"));
		add(panel);
	}

	@Override
	public void commit() {
		commitFields(dField);
		u.setDistance(Numbers.createInteger(((Number) dField.getValue()).intValue()));
		super.commit();
	}

}
