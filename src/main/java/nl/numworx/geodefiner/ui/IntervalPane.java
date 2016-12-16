package nl.numworx.geodefiner.ui;

import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;

import nl.numworx.geodefiner.common.Align;
import nl.numworx.geodefiner.common.Animate;

public class IntervalPane extends TextPane<IntervalModel> {

	JComboBox<Animate> animateBox;
	JFormattedTextField intervalField;
	JFormattedTextField lengthField;
	JFormattedTextField stepField;
	
	public IntervalPane(IntervalModel model) {
		super(model);
		preview.removeAll();
		alignBox = new JComboBox<Align>(new Align[] { Align.TOP, Align.BOTTOM });
		alignBox.setSelectedItem(model.align);
		animateBox = new JComboBox<Animate>(Animate.values());
		animateBox.setSelectedItem(model.animate);
		intervalField = new JFormattedTextField(model.interval);
		lengthField = new JFormattedTextField(model.length);
		stepField = new JFormattedTextField(0.01); // sets DoubleFormat
		stepField.setValue(model.step);
		preview.add(alignBox);
		preview.add(animateBox);
		preview.add(intervalField);
		preview.add(lengthField);
		preview.add(stepField);
	}

	@Override
	public void commit() {
		model.animate = (Animate) animateBox.getSelectedItem();
		model.length = ((Number) lengthField.getValue()).doubleValue();
		model.interval = ((Number) intervalField.getValue()).intValue();
		model.step = (Double) stepField.getValue();
		super.commit();
	}

}
