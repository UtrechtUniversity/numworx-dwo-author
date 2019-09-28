package nl.numworx.geodefiner.ui;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import fi.beans.numworxlf.JCheckBox;
import javax.swing.JLabel;

import nl.numworx.geodefiner.GeoDefiner;
import nl.numworx.geodefiner.ui.color.ColorChooser;
import fi.euclides.util.Messages;
import fi.wiskopdr.formuleobjects.FormuleEditor;

public class ColorPane<T extends ColorModel<?>> extends UIEditor {

	T model;
	ColorChooser  chooser;
	FormuleEditor  visibilityEditor;
	JCheckBox trails, log;

	public String toString() {
		return "*";
	}
	
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
		trails = new JCheckBox(Messages.getString("Euclides.44"), model.trail);
		log = new JCheckBox("log", model.log);
		addComponents();
	}

	void addComponents() {
		Box hbox = Box.createHorizontalBox();
		hbox.add(new JLabel(Messages.getString("ColorPane.1")));
		hbox.add(Box.createGlue());
		if (GeoDefiner.isExperimental) 
		  hbox.add(log);
		hbox.add(trails);
		add(hbox);
		add(visibilityEditor);
		add(Box.createVerticalStrut(10));
		hbox = Box.createHorizontalBox();
		hbox.add(new JLabel(Messages.getString("ColorPane.2")));hbox.add(Box.createGlue());
		add(hbox);
		add(chooser);
	}

	@Override
	public void commit() {
		model.color = chooser.getColor();
		model.visibility.setString ( visibilityEditor.formuleVak.toString()) ;
		model.trail = trails.isSelected();
		model.log = log.isSelected();
		model.install();
	}

}
