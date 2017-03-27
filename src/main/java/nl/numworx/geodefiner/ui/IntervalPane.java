package nl.numworx.geodefiner.ui;

import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;

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
		remove(alwaysF.getParent());
		
		preview.removeAll();
		ComboBoxModel<Align> boxmodel = new DefaultComboBoxModel<>(new Align[] { Align.TOP, Align.BOTTOM, Align.NONE} );
		alignBox.setModel(boxmodel);
		alignBox.setSelectedItem(model.align);
		animateBox = new JComboBox<Animate>(Animate.values());
		animateBox.setSelectedItem(model.animate);
		intervalField = new JFormattedTextField(NumberFormat.getInstance(Locale.US));
		intervalField.setValue(model.interval/1000.0);
		intervalField.setColumns(5);
		intervalField.setMaximumSize(intervalField.getPreferredSize());
		lengthField = new JFormattedTextField(NumberFormat.getInstance(Locale.US));
		lengthField.setValue(model.length);
		lengthField.setColumns(5);
		lengthField.setMaximumSize(lengthField.getPreferredSize());
		stepField = new JFormattedTextField(NumberFormat.getInstance(Locale.US)); // sets DoubleFormat
		stepField.setFocusLostBehavior(JFormattedTextField.PERSIST);
		stepField.setColumns(10);
		if(model.step!= null)
			stepField.setValue(model.step.doubleValue());
		stepField.setMaximumSize(stepField.getPreferredSize());
		widthField = new JFormattedTextField(NumberFormat.getInstance(Locale.US));
		widthField.setFocusLostBehavior(JFormattedTextField.PERSIST);
		widthField.setColumns(5);
		if(model.width != null) widthField.setValue(model.width+0.0);
		widthField.setMaximumSize(widthField.getPreferredSize());
		Box hbox;
		hbox = Box.createHorizontalBox();
		hbox.add(new JLabel("stapgrootte")); hbox.add(stepField);hbox.add(Box.createGlue()); add(hbox);
		hbox = Box.createHorizontalBox();
		hbox.add(new JLabel("lengte"));hbox.add(lengthField);hbox.add(new JLabel("px"));hbox.add(Box.createGlue()); add(hbox); 
		hbox = Box.createHorizontalBox();
		hbox.add(new JLabel("dikte"));hbox.add(widthField);hbox.add(new JLabel("px"));hbox.add(Box.createGlue()); add(hbox); 

		hbox = Box.createHorizontalBox();
		hbox.add(new JLabel("animatietype"));hbox.add(animateBox);hbox.add(Box.createGlue()); add(hbox); 
		hbox = Box.createHorizontalBox();
		hbox.add(new JLabel("interval"));hbox.add(intervalField);hbox.add(new JLabel("s"));hbox.add(Box.createGlue()); add(hbox); 
	}

	@Override
	public void commit() {
		commitFields(lengthField, intervalField, stepField, widthField);
		
		model.animate = (Animate) animateBox.getSelectedItem();
		model.length = ((Number) lengthField.getValue()).doubleValue();
		model.interval = Math.round( ((Number) intervalField.getValue()).floatValue()*1000.0f);
		model.step = (Number) stepField.getValue();
		Number number = (Number) widthField.getValue();
		if (number != null) model.width = number.floatValue();
		else model.width = null;
		super.commit();
	}

}
