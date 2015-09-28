package fi.wiskopdr.symbolen;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

public class Ellips extends Symbool{
	
	public Ellips(int richting)
	{
		super(richting);
	}

	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = createGraphics2D(g);
		
		g2.draw(new Ellipse2D.Double(dikte, dikte, this.getWidth() - 2 * dikte, this.getHeight() - 2 * dikte));
                
	}
	
	public int geefType()
	{
		return Symbool.ELLIPS;
	}
}
