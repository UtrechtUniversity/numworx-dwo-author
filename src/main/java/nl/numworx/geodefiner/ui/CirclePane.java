package nl.numworx.geodefiner.ui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;

import fi.wiskopdr.formuleobjects.FormuleEditor;
import nl.numworx.geodefiner.common.LineType;

public class CirclePane extends UIEditor {

	private CircleModel model;
	private JComboBox<LineType> type;
	private JFormattedTextField widthField;
	private JColorChooser stroke, fill;
	private FormuleEditor  visibilityEditor;

	public CirclePane(CircleModel model) {
		this.model = model;
		type = new JComboBox<LineType>(LineType.values());
		type.setSelectedItem(model.type);
		widthField = new JFormattedTextField(model.width);
		stroke = new JColorChooser(model.color);
		stroke.setPreviewPanel(new JPanel());
		fill  = new JColorChooser((Color) model.fill); // FIXME ons kent ons
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
		add (visibilityEditor);
		add (stroke);
		add (type);
		add (widthField);
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
