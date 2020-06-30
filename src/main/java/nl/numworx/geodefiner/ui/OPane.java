package nl.numworx.geodefiner.ui;

import javax.swing.Box;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;

import fi.beans.numworxlf.NumworxTextFieldUI;
import fi.euclides.model.math.Numbers;
import nl.numworx.geodefiner.Messages;

public class OPane extends PointPane<OModel> {

	JFormattedTextField xField, yField;
	
	public OPane(OModel model) {
		super(model);
		xField = new JFormattedTextField();xField.setUI(NumworxTextFieldUI.createUI(xField));
		yField = new JFormattedTextField();yField.setUI(NumworxTextFieldUI.createUI(yField));
		xField.setValue(model.item.getXd());xField.setColumns(5);
		xField.setMaximumSize(xField.getPreferredSize());
		yField.setValue(model.item.getYd());yField.setColumns(5);
		yField.setMaximumSize(yField.getPreferredSize());
		Box panel = Box.createHorizontalBox();
		panel.add(new JLabel(Messages.getString("OPane.0"))); //$NON-NLS-1$
		panel.add(xField);
		panel.add(new JLabel("px"));panel.add(Box.createGlue()); //$NON-NLS-1$
		add(panel);
		panel = Box.createHorizontalBox();
		panel.add(new JLabel(Messages.getString("OPane.2"))); //$NON-NLS-1$
		panel.add(yField);
		panel.add(new JLabel("px"));panel.add(Box.createGlue()); //$NON-NLS-1$
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
