package nl.numworx.geodefiner.ui;

import javax.swing.JCheckBox;

import fi.euclides.util.Messages;

public class GridPane extends LinePane<GridModel> {

	JCheckBox gravity;
	
	public GridPane(GridModel model) {
		super(model);
		gravity = new JCheckBox(Messages.getString("gravity"));
		gravity.setSelected(model.gravity);
		chooser.getPreviewPanel().add(gravity);
	}

	@Override
	public void commit() {
		model.gravity = gravity.isSelected();
		super.commit();
	}

}
