package nl.numworx.geodefiner.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fi.wiskopdr.formuleobjects.FormuleEditor;
import nl.numworx.geodefiner.common.LineType;
import nl.numworx.geodefiner.ui.color.ColorChooser;

public class CirclePane extends UIEditor {

	private CircleModel model;
	private JComboBox<LineType> type;
	private JFormattedTextField widthField;
	private ColorChooser stroke, fill;
	private FormuleEditor  visibilityEditor;

	public CirclePane(CircleModel model) {
		this.model = model;
		type = new JComboBox<LineType>(LineType.values());
		type.setSelectedItem(model.type);
		widthField = new JFormattedTextField(NumberFormat.getInstance(Locale.US));
		widthField.setValue(Double.valueOf(model.width));
		widthField.setColumns(5);
		widthField.setMaximumSize(widthField.getPreferredSize());
		stroke = new ColorChooser(model.color);
		stroke.setPreviewPanel(new JPanel());
		fill = new ColorChooser((Color) model.fill); // FIXME ons kent ons
		fill.setPreviewPanel(new JPanel());
		visibilityEditor = new FormuleEditor(false);
		if(model.getVisibility() != null)
			visibilityEditor.formuleVak.vulVak(model.getVisibility());
		visibilityEditor.setHeader(false);
		visibilityEditor.setPreferredSize(new Dimension(240,40));
// Wat nu:
		BoxLayout layout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
		setLayout(layout);
// TODO mooie layout, tabbladen?
		Box hbox = Box.createHorizontalBox();
		hbox.add(new JLabel("Zichtbaarheid"));hbox.add(Box.createGlue());
		add(hbox);
		add(visibilityEditor);
		add(Box.createVerticalStrut(10));
		hbox = Box.createHorizontalBox();
		hbox.add(new JLabel("Lijnkleur"));hbox.add(Box.createGlue());
		add(hbox);
		add(stroke);
		add(Box.createVerticalStrut(10));
		hbox = Box.createHorizontalBox();
		hbox.add(new JLabel("lijntype"));hbox.add(type);hbox.add(Box.createGlue());
		add(hbox);
		hbox = Box.createHorizontalBox();
		hbox.add(new JLabel("lijndikte"));
		hbox.add(widthField);hbox.add(new JLabel("px"));hbox.add(Box.createGlue());
		add(hbox);
		add(Box.createVerticalStrut(10));
		hbox = Box.createHorizontalBox();
		hbox.add(new JLabel("Vulkleur"));hbox.add(Box.createGlue());
		add(hbox);
		add (fill);
	}

	@Override
	public void commit() {
		model.color = stroke.getColor();
		model.fill  = fill.getColor();
		model.width = ((Number) widthField.getValue()).floatValue();
		model.type = (LineType) type.getSelectedItem();
		model.visibility.setString(visibilityEditor.formuleVak.toString());

		model.install();
	}

}
