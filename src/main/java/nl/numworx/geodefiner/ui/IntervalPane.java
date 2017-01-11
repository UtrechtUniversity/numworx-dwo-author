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
	JFormattedTextField widthField;
	
	public IntervalPane(IntervalModel model) {
		super(model);
		preview.removeAll();
		alignBox = new JComboBox<Align>(new Align[] { Align.TOP, Align.BOTTOM });
		alignBox.setSelectedItem(model.align);
		animateBox = new JComboBox<Animate>(Animate.values());
		animateBox.setSelectedItem(model.animate);
		intervalField = new JFormattedTextField(model.interval/1000.0);
		intervalField.setColumns(10);
		lengthField = new JFormattedTextField(model.length);
		lengthField.setColumns(10);
		stepField = new JFormattedTextField(0.01); // sets DoubleFormat
		stepField.setColumns(10);
		stepField.setValue(model.step);
		widthField = new JFormattedTextField(1.0f);
		widthField.setColumns(10);
		widthField.setValue(model.width);
		
		preview.add(alignBox);
		preview.add(animateBox);
		preview.add(intervalField);
		preview.add(lengthField);
		preview.add(stepField);
		preview.add(widthField);
	}

	@Override
	public void commit() {
		model.animate = (Animate) animateBox.getSelectedItem();
		model.length = ((Number) lengthField.getValue()).doubleValue();
		model.interval = Math.round( ((Number) intervalField.getValue()).floatValue()*1000.0f);
		model.step = (Double) stepField.getValue();
		model.width = (Float) widthField.getValue();
		super.commit();
	}

}
