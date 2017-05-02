package nl.numworx.geodefiner.ui;

import javax.swing.Box;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;

import fi.euclides.model.math.Numbers;

public class OPane extends PointPane<OModel> {

	JFormattedTextField xField, yField;
	
	public OPane(OModel model) {
		super(model);
		xField = new JFormattedTextField();
		yField = new JFormattedTextField();
		xField.setValue(model.item.getXd());xField.setColumns(5);
		xField.setMaximumSize(xField.getPreferredSize());
		yField.setValue(model.item.getYd());yField.setColumns(5);
		yField.setMaximumSize(yField.getPreferredSize());
		Box panel = Box.createHorizontalBox();
		panel.add(new JLabel("x-positie"));
		panel.add(xField);
		panel.add(new JLabel("px"));panel.add(Box.createGlue());
		add(panel);
		panel = Box.createHorizontalBox();
		panel.add(new JLabel("y-positie"));
		panel.add(yField);
		panel.add(new JLabel("px"));panel.add(Box.createGlue());
		add(panel);
	}

	@Override
	public void commit() {
		commitFields(xField, yField);
		int x = ((Number) xField.getValue()).intValue();
		int y = ((Number) yField.getValue()).intValue();
		model.item.setXY(Numbers.createInteger(x), Numbers.createInteger(y));
		super.commit();
	}

}
