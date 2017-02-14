package nl.numworx.geodefiner.ui;

import java.awt.Dimension;

import javax.swing.JColorChooser;
import javax.swing.JPanel;

import nl.numworx.geodefiner.ui.color.ColorChooser;
import fi.wiskopdr.formuleobjects.FormuleEditor;

public class ColorPane<T extends ColorModel<?>> extends UIEditor {

	T model;
	ColorChooser  chooser;
	FormuleEditor  visibilityEditor;

	public ColorPane(T model) {
		visibilityEditor = new FormuleEditor(false);
		this.model = model;
		chooser = new ColorChooser(model.color);
		//chooser.setPreviewPanel(new JPanel());
		add(chooser);
		add(visibilityEditor);
		if(model.getVisibility() != null)
			visibilityEditor.formuleVak.vulVak(model.getVisibility());
		visibilityEditor.setHeader(false);
		visibilityEditor.setPreferredSize(new Dimension(240,40));
	}

	@Override
	public void commit() {
		model.color = chooser.getColor();
		model.visibility.setString ( visibilityEditor.formuleVak.toString()) ;
		model.install();
	}

}
