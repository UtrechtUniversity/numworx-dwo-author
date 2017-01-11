package nl.numworx.geodefiner.ui;

import java.text.ParseException;

import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import nl.numworx.geodefiner.common.LineType;

public class LinePane<M extends LineModel> extends ColorPane<M> {

	JComboBox<LineType> type;
	JFormattedTextField widthField;
	JLabel sample;
	public LinePane(M model) {
		super(model);
		JPanel panel = new JPanel();
		sample = new JLabel("⎯⎯⎯⎯⎯⎯⎯⎯⎯"); sample.setBackground(null);
		type   = new JComboBox<LineType>(LineType.values());
		type.setSelectedItem(model.type);
		widthField = new JFormattedTextField(model.width);
		panel.add(sample);
		panel.add(type);
		panel.add(widthField);
		chooser.setPreviewPanel(panel);

	}
	public void commit() {
		model.type = (LineType) type.getSelectedItem();
		try {
			widthField.commitEdit();
		} catch (ParseException e) {
		}
		model.width  = ((Number) widthField.getValue()).floatValue();
		super.commit();
	}
}
