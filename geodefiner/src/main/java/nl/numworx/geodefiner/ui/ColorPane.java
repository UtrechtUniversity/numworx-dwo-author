package nl.numworx.geodefiner.ui;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;

import fi.beans.numworxlf.JCheckBox;
import fi.beans.numworxlf.JFormattedTextField;

import javax.swing.JLabel;
import javax.swing.JTextField;

import nl.numworx.geodefiner.merge.RenameAction;
import nl.numworx.geodefiner.merge.RenameAction.RenamePane;
import nl.numworx.geodefiner.ui.color.ColorChooser;
import fi.euclides.model.Destroyable;
import fi.euclides.util.Messages;
import fi.wiskopdr.formuleobjects.FormuleEditor;

public class ColorPane<T extends ColorModel<?>> extends UIEditor implements RenamePane {

	public T model;
	ColorChooser  chooser;
	JCheckBox trails, log;
	RenameFormat  formatter;
	public String toString() {
		return "*";
	}
	
	public ColorPane(T model) {
        this.model = model;
		visibilityEditor = new FormuleEditor(false);
		BoxLayout layout = new BoxLayout(content, BoxLayout.PAGE_AXIS);
		content.setLayout(layout);
		chooser = new ColorChooser(model.color);
		if(model.getVisibility() != null)
			visibilityEditor.formuleVak.vulVak(model.getVisibility());
		visibilityEditor.setHeader(false);
		visibilityEditor.setPreferredSize(new Dimension(240,40));
		trails = new JCheckBox(Messages.getString("Euclides.44"), model.trail);
		log = new JCheckBox("log", model.log);
		addComponents();		
		setSizes();
	}


	void addComponents() {
		Box hbox = Box.createHorizontalBox();

		if (model.getRename().isPresent()) {
		  RenameAction action = model.getRename().get();
		  action.setPane(this);
		  hbox.add(new JLabel(Messages.getString("Euclides.103")));
		  JFormattedTextField n;
		  formatter = new RenameFormat(); 
		  name = n = new JFormattedTextField(formatter);
		  name.setInputVerifier(verifier);
		  name.addActionListener(action);
		  hbox.add(name);
		  hbox.add(Box.createGlue());
		  content.add(hbox);
		  hbox = Box.createHorizontalBox();
		}
		if (model.item != null) { 
			hbox.add(new JLabel(Messages.getString("ColorPane.1")));
			hbox.add(Box.createGlue());
			hbox.add(log);
			hbox.add(trails);
			content.add(hbox);
			content.add(visibilityEditor);
			content.add(Box.createVerticalStrut(10));
		}
		hbox = Box.createHorizontalBox();
		hbox.add(new JLabel(Messages.getString("ColorPane.2")));hbox.add(Box.createGlue());
		content.add(hbox);
		content.add(chooser);
	}
		
	
	
	
	@Override
	public void commit() {
		model.color = chooser.getColor();
		model.visibility.setString(getVisibility()) ;
		model.trail = trails.isSelected();
		model.log = log.isSelected();
		model.install();
        model.getRename().ifPresent(RenameAction::doRename);
	}

	JTextField getNameField() {
		return name;
	}

	@Override
	public Destroyable getItem() {
		return model.item;
	}

}
