package nl.numworx.geodefiner.ui;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;

import nl.numworx.geodefiner.common.LineType;

public class LinePane<M extends LineModel> extends ColorPane<M> {

	JComboBox<LineType> type;
	JFormattedTextField widthField;
	JLabel sample;
	public LinePane(M model) {
		super(model);
		Box panel; 
		panel = Box.createHorizontalBox();
		sample = new JLabel("lijntype"); 
		type   = new JComboBox<LineType>(LineType.values());
		type.setSelectedItem(model.type);
		NumberFormat format = NumberFormat.getInstance(Locale.US);
		widthField = new JFormattedTextField(format);
		widthField.setValue(model.width);
		widthField.setColumns(5);
		widthField.setMaximumSize(widthField.getPreferredSize());
		panel.add(sample); panel.add(type);panel.add(Box.createGlue());
		add(panel);
		panel = Box.createHorizontalBox();
		panel.add(new JLabel("lijndikte"));panel.add(widthField);panel.add(new JLabel("px"));
		panel.add(Box.createGlue());
		add(panel);
	}
	public void commit() {
		commitFields(widthField);
		model.type = (LineType) type.getSelectedItem();
		try {
			widthField.commitEdit();
		} catch (ParseException e) {
		}
		model.width  = ((Number) widthField.getValue()).floatValue();
		super.commit();
	}
}
