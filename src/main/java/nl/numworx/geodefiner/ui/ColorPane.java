package nl.numworx.geodefiner.ui;

import java.awt.Dimension;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.Objects;
import java.util.function.Supplier;

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
import nl.numworx.geodefiner.common.Randomizer;
import nl.numworx.geodefiner.common.math.Expression;
import nl.numworx.geodefiner.merge.RenameAction;
import nl.numworx.geodefiner.merge.RenameAction.RenamePane;
import nl.numworx.geodefiner.ui.color.ColorChooser;
import nl.tue.win.riaca.openmath.lang.OMObject;
import fi.euclides.event.NameMapper;
import fi.euclides.event.Tracker;
import fi.euclides.expr.InterpretException;
import fi.euclides.formuleobjects.FormuleParser;
import fi.euclides.formuleobjects.ParseException;
import fi.euclides.formuleobjects.Token;
import fi.euclides.formuleobjects.TokenMgrError;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.util.Messages;
import fi.wiskopdr.formuleobjects.FormuleEditor;

public class ColorPane<T extends ColorModel<?>> extends UIEditor implements RenamePane {

	static final class RenameVerifier extends InputVerifier {
		@Override
		public boolean verify(JComponent input) {
			JTextField field = (JTextField) input;
			String text = field.getText();
			return verify(text);
		}

		boolean verify(String text) {
			if (text.isEmpty()) return true;
			FormuleParser parser = new FormuleParser(text);
			try {
				Token t = parser.variableAt();
				return true;
			} catch(Exception e) {}
			return false;
		}
	}

	static final class RenameFormat extends Format {
		
		@Override
		public Object parseObject(String source, ParsePosition pos) {
			if (!verifier.verify(source)) {
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
	}

	public T model;
	ColorChooser  chooser;
	FormuleEditor  visibilityEditor;
	JCheckBox trails, log;
	JTextField name;
	RenameFormat  formatter;
	
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

	static RenameVerifier verifier = new RenameVerifier();
	
	void addComponents() {
		Box hbox = Box.createHorizontalBox();

		if (model.getRename().isPresent()) {
		  RenameAction action = model.getRename().get();
		  action.setPane(this);
		  hbox.add(new JLabel(Messages.getString("Euclides.103")));
		  JFormattedTextField n;
		  formatter = new RenameFormat(); 
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
		model.visibility.setString ( getVisibility()) ;
		model.trail = trails.isSelected();
		model.log = log.isSelected();
		model.install();
        model.getRename().ifPresent(RenameAction::doRename);
	}

	public String getVisibility() {
		return visibilityEditor.formuleVak.toString();
	}

	JTextField getNameField() {
		return name;
	}

	@Override
	public Destroyable getItem() {
		return model.item;
	}

	@Override
	public boolean verify(Tracker t) {
		String v = getVisibility();
		if ("$f@".equals(v))
			return super.verify(t); // empty
		Randomizer r = t.adapt(Randomizer.class);
		if (r != null) v = r.randomize(v);
		FormuleParser parser = new FormuleParser(v.substring(2));
		try {
			OMObject object = parser.logic();
			Expression expr = t.adapt(Expression.class);
			Label l = new Label();
			expr.interpret(object, l, t.getMapper());
			l.destroy();
		} catch (TokenMgrError e) { // TODO zet feedback tekst
			return false;
		} catch (ParseException e) {
			return false;
		} catch (InterpretException e) {
			return false;
		} catch (Exception e) {
			return false;
		}

		return super.verify(t);
	}

}
