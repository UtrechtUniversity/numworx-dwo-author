package nl.numworx.geodefiner.ui;

import javax.swing.JColorChooser;
import javax.swing.JPanel;

public class ColorPane<T extends ColorModel<?>> extends UIEditor {

	T model;
	JColorChooser  chooser;

	public ColorPane(T model) {
		this.model = model;
		chooser = new JColorChooser(model.color);
		chooser.setPreviewPanel(new JPanel());
		add(chooser);
	}

	@Override
	public void commit() {
		model.color = chooser.getColor();
		model.install();
	}

}
