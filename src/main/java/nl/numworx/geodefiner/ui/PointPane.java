package nl.numworx.geodefiner.ui;

import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.Box;
import fi.beans.numworxlf.JCheckBox;
import fi.beans.numworxlf.JComboBox;
import fi.beans.numworxlf.JFormattedTextField;

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
		sizeField.setValue(Integer.valueOf(model.size == null ? 5 : model.size.intValue()));
		sizeField.setColumns(5);
		sizeField.setMaximumSize(sizeField.getPreferredSize());
		rigid = new JCheckBox(Messages.getString("rigid"));
		Destroyable item = model.item;
		if (item instanceof Groep) {
		  item = ((Groep)item).prototype();
		}
        rigid.setEnabled(item instanceof FreePoint||item == null);
        rigid.setVisible(item != null);
		rigid.setSelected(!model.rigid);			
		Box panel = Box.createHorizontalBox();
		panel.add( new JLabel(Messages.getString("PointPane.1"))); panel.add(sizeField);panel.add(new JLabel("px"));panel.add(Box.createGlue());
		content.add(Box.createVerticalStrut(10));
		content.add(panel);
		panel = Box.createHorizontalBox();
		if (item != null)
			panel.add(new JLabel(Messages.getString("PointPane.2")));
		panel.add(rigid); panel.add(Box.createGlue());
		content.add(Box.createVerticalStrut(10));
		content.add(panel );

		panel.validate();
		panel.setSize(panel.getPreferredSize());
		
		setSizes();
		}

	public void commit() {
		commitFields(sizeField);
		model.type = (PointType) type.getSelectedItem();
		model.rigid = !rigid.isSelected();
		model.size  = ((Number) sizeField.getValue()).floatValue();
		super.commit();
	}
}
