package nl.numworx.geodefiner.ui;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

import nl.numworx.geodefiner.ui.color.ColorChooser;
import fi.wiskopdr.formuleobjects.FormuleEditor;

public class ColorPane<T extends ColorModel<?>> extends UIEditor {

	T model;
	ColorChooser  chooser;
	FormuleEditor  visibilityEditor;

	public ColorPane(T model) {
		visibilityEditor = new FormuleEditor(false);
		BoxLayout layout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
		setLayout(layout);
		this.model = model;
		chooser = new ColorChooser(model.color);
		if(model.getVisibility() != null)
			visibilityEditor.formuleVak.vulVak(model.getVisibility());
		visibilityEditor.setHeader(false);
		visibilityEditor.setPreferredSize(new Dimension(240,40));
		addComponents();
	}

	void addComponents() {
		Box hbox = Box.createHorizontalBox();
		hbox.add(new JLabel("Zichtbaarheid"));hbox.add(Box.createGlue());
		add(hbox);
		add(visibilityEditor);
		add(Box.createVerticalStrut(10));
		hbox = Box.createHorizontalBox();
		hbox.add(new JLabel("Kleur"));hbox.add(Box.createGlue());
		add(hbox);
		add(chooser);
	}

	@Override
	public void commit() {
		model.color = chooser.getColor();
		model.visibility.setString ( visibilityEditor.formuleVak.toString()) ;
		model.install();
	}

}
