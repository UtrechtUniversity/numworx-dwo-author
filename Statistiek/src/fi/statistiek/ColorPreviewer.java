package fi.statistiek;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * 
 * Extended JPanel that shows a color gradient
 * @author Manu Drijvers
 *
 */
public class ColorPreviewer extends JPanel {
	private Color c1;
	private Color c2;
	
	/**
	 * Constructor
	 * @param c1 The color of the left-hand side
	 * @param c2 The color of the right-hand side
	 */
	public ColorPreviewer(Color c1, Color c2) {
		this.c1 = c1;
		this.c2 = c2;
	}
	
	public void paintComponent(Graphics g) {
		g.clearRect(0, 0, this.getWidth(), this.getHeight());
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, this.getWidth()-1, this.getHeight()-1);
		int width = this.getWidth()-2;

		for(int x = 0; x < width; x++) {
			Color c = ColorPreviewer.mixColors(c1, c2, (double)x/(double)width);
			g.setColor(c);
			g.drawLine(x+1, 1, x+1, this.getHeight()-2);
		}
	}
	
	/**
	 * Mix two colors 
	 * @param c1 Color 1
	 * @param c2 Color 2
	 * @param d How to mix; 0 is just color 1, 1 is just color 2
	 * @return The mixed color
	 */
	public static Color mixColors(Color c1, Color c2, double d) {
		int red = (int)(d*c2.getRed() + (1-d)*c1.getRed());
		int green = (int)(d*c2.getGreen() + (1-d)*c1.getGreen());
		int blue = (int)(d*c2.getBlue() + (1-d)*c1.getBlue());
		return new Color(red, green, blue);
	}
	
	/**
	 * Change the color of the left-hand side
	 * @param c1 the new color of the left-hand side
	 */
	public void setColorA(Color c1) {
		this.c1 = c1;
		this.repaint();
	}
	
	/**
	 * Change the color of the right-hand side
	 * @param c2 the new color of the right-hand side
	 */
	public void setColorB(Color c2) {
		this.c2 = c2;

		this.repaint();
	}
}
