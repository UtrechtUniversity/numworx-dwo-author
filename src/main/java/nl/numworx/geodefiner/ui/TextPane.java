package nl.numworx.geodefiner.ui;

import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.AffineTransform;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fi.euclides.util.Messages;
import nl.numworx.geodefiner.common.Align;

public class TextPane<T extends TextModel> extends ColorPane<T> implements Icon, ItemListener {

	JComboBox<Align> alignBox;
	JLabel sampleLabel;
	JPanel preview = new JPanel();
	JFormattedTextField fontSize;
	JCheckBox alwaysF;
	
	public TextPane(T model) {
		super(model);
		alignBox = new JComboBox<Align>(Align.values());
		alignBox.addItemListener(this);
		sampleLabel = new JLabel(model.sample());
		sampleLabel.setForeground(null);
		sampleLabel.setIcon(this);
		sampleLabel.setFont(model.font);
		alignBox.setSelectedItem(model.align);
		preview.add(sampleLabel);
		fontSize = new JFormattedTextField(NumberFormat.getInstance(Locale.US));
		fontSize.setValue(model.font.getSize2D()+0.0);
		fontSize.setColumns(5);
		fontSize.setMaximumSize(fontSize.getPreferredSize());
		fontSize.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Font f = sampleLabel.getFont().deriveFont(((Number) fontSize.getValue()).floatValue());
				sampleLabel.setFont(f);
			}
			
		});
		alwaysF = new JCheckBox(Messages.getString("TextPane.1"));
		alwaysF.setSelected(Boolean.TRUE.equals(model.alwaysF));
		chooser.setPreviewPanel(preview);
		add(Box.createVerticalStrut(10));
		Box hbox = Box.createHorizontalBox();
		hbox.add(new JLabel(Messages.getString("TextPane.2")));
		hbox.add(alignBox);
		hbox.add(Box.createGlue());
		add(hbox);
		hbox = Box.createHorizontalBox();
		hbox.add(new JLabel(Messages.getString("TextPane.3")));
		hbox.add(fontSize);
		hbox.add(new JLabel("px"));
		hbox.add(Box.createGlue());
		add(hbox);
		hbox = Box.createHorizontalBox();
		hbox.add(new JLabel(Messages.getString("TextPane.4")));
		hbox.add(alwaysF);
		hbox.add(Box.createGlue());
		add(hbox);
	}
	@Override
	public void commit() {
		commitFields(fontSize);
		model.align = alignBox.getItemAt(alignBox.getSelectedIndex());
		Object value = fontSize.getValue();
		float size = TextModel.DEFAULT_SIZE;
		if(value != null)
			size =	((Number) value).floatValue();
		model.font = model.font.deriveFont(size);
		model.alwaysF = Boolean.valueOf(alwaysF.isSelected());
		super.commit();
	}
	public void paintIcon(Component c, Graphics g, int x, int y) {
		g.setColor(getForeground());
		g.fillOval(x, y, getIconWidth(), getIconHeight());
	}

	
	
	
	public int getIconWidth() {
		return 5;
	}
	public int getIconHeight() {
		return 5;
	}
	public void itemStateChanged(ItemEvent e) {
		if (ItemEvent.SELECTED == e.getStateChange())
		{
			Align a = (Align) e.getItem(); // ?
			switch(a) {
			case BASE: 
				sampleLabel.setHorizontalTextPosition(JLabel.RIGHT);
				sampleLabel.setVerticalTextPosition(JLabel.BOTTOM);
				break;
			case RIGHT:
					sampleLabel.setHorizontalTextPosition(JLabel.RIGHT);
					sampleLabel.setVerticalTextPosition(JLabel.CENTER);
					break;
			case BOTTOM:
				sampleLabel.setHorizontalTextPosition(JLabel.CENTER);
				sampleLabel.setVerticalTextPosition(JLabel.BOTTOM);
				break;
			case TOP:
				sampleLabel.setHorizontalTextPosition(JLabel.CENTER);
				sampleLabel.setVerticalTextPosition(JLabel.TOP);
				break;
			case LEFT:
				sampleLabel.setHorizontalTextPosition(JLabel.LEFT);
				sampleLabel.setVerticalTextPosition(JLabel.CENTER);
				break;
			}
			sampleLabel.invalidate();
			preview.validate();
			preview.repaint();
		}
	}

}
