package nl.numworx.geodefiner.ui;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JLabel;

import fi.euclides.util.Messages;

@SuppressWarnings("serial")
public class GridPane extends LinePane<GridModel> {

	JCheckBox gravity;
	
	public GridPane(GridModel model) {
		super(model);
		remove(rigid.getParent()); // ons kent ons.
		gravity = new JCheckBox(Messages.getString("gravity"));
		gravity.setSelected(model.gravity);
		Box hbox = Box.createHorizontalBox();
		hbox.add(new JLabel(Messages.getString("GridPane.1")));
		hbox.add(gravity);
		hbox.add(Box.createGlue());
		add(hbox);
	}

	@Override
	public void commit() {
		model.gravity = gravity.isSelected();
		super.commit();
	}

}
