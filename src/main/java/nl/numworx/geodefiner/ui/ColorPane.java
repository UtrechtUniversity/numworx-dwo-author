package nl.numworx.geodefiner.ui;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;

import fi.beans.numworxlf.JButton;
import fi.beans.numworxlf.JCheckBox;
import fi.beans.numworxlf.JTextField;

import javax.swing.JLabel;

import nl.numworx.geodefiner.GeoDefiner;
import nl.numworx.geodefiner.merge.RenameAction;
import nl.numworx.geodefiner.ui.color.ColorChooser;
import fi.euclides.event.NameMapper;
import fi.euclides.util.Messages;
import fi.wiskopdr.formuleobjects.FormuleEditor;

public class ColorPane<T extends ColorModel<?>> extends UIEditor {

	public T model;
	ColorChooser  chooser;
	FormuleEditor  visibilityEditor;
	JCheckBox trails, log;
	JTextField name;
	
	public String toString() {
		return "*";
	}
	
	public ColorPane(T model) {
        this.model = model;
		visibilityEditor = new FormuleEditor(false);
		BoxLayout layout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
		setLayout(layout);
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

		if (model.rename.isPresent()) {
		  RenameAction action = model.rename.get();
		  action.setPane(this);
		  hbox.add(new JLabel("Name:"));
		  name = new JTextField(action.getName());
		  name.addActionListener(action);
		  hbox.add(name);
		  hbox.add(Box.createGlue());
		  add(hbox);
		  hbox = Box.createHorizontalBox();
		}
		hbox.add(new JLabel(Messages.getString("ColorPane.1")));
		hbox.add(Box.createGlue());
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
        model.rename.ifPresent(RenameAction::doRename);
	}

}
