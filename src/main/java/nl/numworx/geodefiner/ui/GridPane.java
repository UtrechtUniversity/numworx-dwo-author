package nl.numworx.geodefiner.ui;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Box;
import fi.beans.numworxlf.JCheckBox;
import fi.beans.numworxlf.JFormattedTextField;

import javax.swing.JLabel;

import fi.euclides.util.Messages;

@SuppressWarnings("serial")
public class GridPane extends LinePane<GridModel> implements ItemListener {

	JCheckBox gravity;
	JFormattedTextField snapField;
	
	public GridPane(GridModel model) {
		super(model);
		remove(rigid.getParent()); // ons kent ons.
		gravity = new JCheckBox(Messages.getString("gravity"));
		gravity.setSelected(model.gravity);
		Box hbox = Box.createHorizontalBox();
		hbox.add(new JLabel(Messages.getString("GridPane.1")));
		hbox.add(gravity);
		hbox.add(new JLabel(Messages.getString("GridPane.2")));
		snapField = new JFormattedTextField();
		snapField.setValue(Double.valueOf(model.snap));
		snapField.setColumns(4);
		snapField.setMaximumSize(snapField.getPreferredSize());
		
		snapField.setEnabled(model.gravity);
		hbox.add(snapField);
		hbox.add(new JLabel("px"));		
		hbox.add(Box.createGlue());
		content.add(hbox);
		gravity.addItemListener(this);
		setSizes();
	}

	@Override
	public void commit() {
		model.gravity = gravity.isSelected();
		model.snap = ((Number) snapField.getValue()).intValue();
		super.commit();
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		snapField.setEnabled(e.getStateChange() == ItemEvent.SELECTED);		
	}

}
