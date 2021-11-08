package nl.numworx.geodefiner.ui;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;

import fi.beans.numworxlf.JRadioButton;

class AnglePane extends TextPane<AngleModel> {

	JRadioButton radField;
	JRadioButton degField;
	public AnglePane(AngleModel model) {
		super(model);
		remove(alwaysF.getParent());
		preview.removeAll();
		remove(alignBox.getParent());
		Box hbox;
		radField = new JRadioButton("radialen", model.isRad());
		degField = new JRadioButton("graden", !model.isRad());
		hbox = Box.createHorizontalBox();
		hbox.add(new JLabel("Hoeken tonen in "));
		hbox.add(radField);
		hbox.add(degField);
		ButtonGroup group = new ButtonGroup();
		group.add(degField); group.add(radField);
		hbox.add(Box.createGlue()); content.add(hbox); setSizes();
		
	}
	@Override
	public void commit() {
		model.setRad(radField.isSelected());
		super.commit();
	}

}
