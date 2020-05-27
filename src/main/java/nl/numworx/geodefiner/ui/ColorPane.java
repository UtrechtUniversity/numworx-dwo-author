package nl.numworx.geodefiner.ui;

import java.awt.Dimension;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.Objects;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;

import fi.beans.numworxlf.JButton;
import fi.beans.numworxlf.JCheckBox;

import javax.swing.JLabel;
import javax.swing.JTextField;

import nl.numworx.geodefiner.GeoDefiner;
import nl.numworx.geodefiner.merge.RenameAction;
import nl.numworx.geodefiner.ui.color.ColorChooser;
import fi.euclides.event.NameMapper;
import fi.euclides.formuleobjects.FormuleParser;
import fi.euclides.formuleobjects.Token;
import fi.euclides.util.Messages;
import fi.wiskopdr.formuleobjects.FormuleEditor;

public class ColorPane<T extends ColorModel<?>> extends UIEditor {

	public T model;
	ColorChooser  chooser;
	FormuleEditor  visibilityEditor;
	JCheckBox trails, log;
	JTextField name;
	Format  formatter = new Format() {
		
		@Override
		public Object parseObject(String source, ParsePosition pos) {
			if (!verifier.verify(name)) {
				pos.setErrorIndex(0);
				return null;
			}
			pos.setIndex(source.length());
			return source;
		}
		
		@Override
		public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
			return toAppendTo.append(Objects.toString(obj, ""));
		}
	};
	
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

	static InputVerifier verifier = new InputVerifier() {

		@Override
		public boolean verify(JComponent input) {
			JTextField field = (JTextField) input;
			String text = field.getText();
			if (text.isEmpty()) return true;
			FormuleParser parser = new FormuleParser(text);
			try {
				Token t = parser.variableAt();
				return true;
			} catch(Exception e) {}
			return false;
		} 
		
	};
	
	void addComponents() {
		Box hbox = Box.createHorizontalBox();

		if (model.getRename().isPresent()) {
		  RenameAction action = model.getRename().get();
		  action.setPane(this);
		  hbox.add(new JLabel(Messages.getString("Euclides.103")));
		  JFormattedTextField n;
		  name = n = new JFormattedTextField(formatter);
		  n.setValue(action.getName());
		  name.setInputVerifier(verifier);
		  name.addActionListener(action);
		  hbox.add(name);
		  hbox.add(Box.createGlue());
		  add(hbox);
		  hbox = Box.createHorizontalBox();
		}
		if (model.item != null) { 
			hbox.add(new JLabel(Messages.getString("ColorPane.1")));
			hbox.add(Box.createGlue());
			hbox.add(log);
			hbox.add(trails);
			add(hbox);
			add(visibilityEditor);
			add(Box.createVerticalStrut(10));
		}
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
        model.getRename().ifPresent(RenameAction::doRename);
	}

}
