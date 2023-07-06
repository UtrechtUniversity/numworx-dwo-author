package nl.numworx.geodefiner;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.border.Border;

class CellBorder implements Border {
	private final ImageIcon updownImage = new ImageIcon(getClass().getResource("resources/updown.png"));

	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		updownImage.paintIcon(c, g, x+width-18, y+(height-updownImage.getIconHeight())/2);			
	}

	@Override
	public Insets getBorderInsets(Component c) {
		return new Insets(1,1,1,18);
	}

	@Override
	public boolean isBorderOpaque() {
		return false;
	}
	
}