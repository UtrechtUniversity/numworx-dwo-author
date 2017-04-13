package nl.numworx.geodefiner.ui;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;

import fi.euclides.model.algo.FreePoint;
import fi.euclides.util.Messages;
import nl.numworx.geodefiner.common.LineType;

public class LinePane<M extends LineModel> extends ColorPane<M> {

	JComboBox<LineType> type;
	JFormattedTextField widthField;
	JLabel sample;
	private JCheckBox  rigid;

	public LinePane(M model) {
		super(model);
		Box panel; 
		panel = Box.createHorizontalBox();
		sample = new JLabel(Messages.getString("LinePane.1")); 
		type   = new JComboBox<LineType>(LineType.values());
		type.setSelectedItem(model.type);
		NumberFormat format = NumberFormat.getInstance(Locale.US);
		widthField = new JFormattedTextField(format);
		widthField.setValue(model.width);
		widthField.setColumns(5);
		widthField.setMaximumSize(widthField.getPreferredSize());
		rigid = new JCheckBox(Messages.getString("rigid"));
		rigid.setSelected(!model.rigid);			
		panel.add(sample); panel.add(type);panel.add(Box.createGlue());
		add(panel);
		panel = Box.createHorizontalBox();
		panel.add(new JLabel(Messages.getString("LinePane.2")));panel.add(widthField);panel.add(new JLabel("px"));
		panel.add(Box.createGlue());
		add(panel);
		panel = Box.createHorizontalBox();
		panel.add(new JLabel(Messages.getString("PointPane.2"))); panel.add(rigid); panel.add(Box.createGlue());
		add(Box.createVerticalStrut(10));
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
		model.rigid = !rigid.isSelected();
		super.commit();
	}
}
