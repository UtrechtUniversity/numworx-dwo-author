package nl.numworx.geodefiner.ui;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import nl.numworx.geodefiner.common.Align;

public class TextPane<T extends TextModel> extends ColorPane<T> implements Icon, ItemListener {

	JComboBox<Align> alignBox;
	JLabel sampleLabel;
	JPanel preview = new JPanel();
	public TextPane(T model) {
		super(model);
		alignBox = new JComboBox<Align>(Align.values());
		alignBox.addItemListener(this);
		sampleLabel = new JLabel(model.sample());
		sampleLabel.setForeground(null);
		sampleLabel.setIcon(this);
		alignBox.setSelectedItem(model.align);
		preview.add(sampleLabel);
		preview.add(alignBox);
		chooser.setPreviewPanel(preview);
	}
	@Override
	public void commit() {
		model.align = alignBox.getItemAt(alignBox.getSelectedIndex());
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
