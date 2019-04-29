package nl.numworx.geodefiner.ui;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;

import nl.numworx.geodefiner.common.PointType;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Groep;
import fi.euclides.model.algo.FreePoint;
import fi.euclides.util.Messages;

@SuppressWarnings("serial")
class PointPane<T extends PointModel> extends ColorPane<T> {

	private JComboBox<PointType> type;
	private JFormattedTextField sizeField;
	private JCheckBox  rigid;
	
	PointPane(T model) {
		super(model);
		type = new JComboBox<PointType>(PointType.values());
		type.setSelectedItem(model.type);
		NumberFormat format = NumberFormat.getIntegerInstance(Locale.US);
		sizeField = new JFormattedTextField(format);
		sizeField.setValue(Integer.valueOf(model.size));
		sizeField.setColumns(5);
		sizeField.setMaximumSize(sizeField.getPreferredSize());
		rigid = new JCheckBox(Messages.getString("rigid"));
		Destroyable item = model.item;
		if (item instanceof Groep) {
		  item = ((Groep)item).prototype();
		}
        rigid.setEnabled(item instanceof FreePoint);
		rigid.setSelected(!model.rigid);			
		Box panel = Box.createHorizontalBox();
		panel.add( new JLabel(Messages.getString("PointPane.1"))); panel.add(sizeField);panel.add(new JLabel("px"));panel.add(Box.createGlue());
		add(Box.createVerticalStrut(10));
		add(panel);
		panel = Box.createHorizontalBox();
		panel.add(new JLabel(Messages.getString("PointPane.2"))); panel.add(rigid); panel.add(Box.createGlue());
		add(Box.createVerticalStrut(10));
		add(panel );

		panel.validate();
		panel.setSize(panel.getPreferredSize());
		}

	public void commit() {
		commitFields(sizeField);
		model.type = (PointType) type.getSelectedItem();
		model.rigid = !rigid.isSelected();

		model.size  = ((Number) sizeField.getValue()).intValue();
		super.commit();
	}
}
