package nl.numworx.geodefiner.ui;

import java.awt.Font;
import java.text.ParseException;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fi.euclides.model.Punt;
import fi.euclides.model.algo.FreePoint;
import nl.numworx.geodefiner.ui.PointModel.Type;

@SuppressWarnings("serial")
public class PointPane extends ColorPane<PointModel> {

	private JComboBox<PointModel.Type> type;
	private JFormattedTextField sizeField;
	private JCheckBox  rigid;
	
	PointPane(PointModel model) {
		super(model);
		type = new JComboBox<PointModel.Type>(Type.values());
		type.setSelectedItem(model.type);
		JPanel panel = new JPanel();
		sizeField = new JFormattedTextField(Integer.valueOf(model.size));
		rigid = new JCheckBox("rigid");
		rigid.setEnabled(model.item instanceof FreePoint);
		rigid.setSelected(model.rigid);			
		add(chooser);
		panel.add( new JLabel("•"));
		panel.add( new JLabel("markertype" )); panel.add(type);
		panel.add( new JLabel("size")); panel.add(sizeField);
		panel.add( rigid );
		panel.validate();
		panel.setSize(panel.getPreferredSize());
		chooser.setPreviewPanel(panel); // disable preview panel
		}

	public void commit() {
		model.type = (Type) type.getSelectedItem();
		model.rigid = rigid.isSelected();

		try {
			sizeField.commitEdit();
		} catch (ParseException e) {
		}
		model.size  = ((Number) sizeField.getValue()).intValue();
		super.commit();
	}
}
