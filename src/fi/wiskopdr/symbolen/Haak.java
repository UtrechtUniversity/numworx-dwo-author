package fi.wiskopdr.symbolen;

import java.awt.Graphics;
import java.awt.Graphics2D;

public class Haak extends Symbool {

	
	public Haak(int richting)
	{
		super(richting);
	}
	
	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = createGraphics2D(g);
		
		if(richting == Symbool.RICHTING_LINKS)
		{	int x = this.getWidth()/2;
			g2.drawArc(x, 1, 10, 10, 90, 90);
			g2.drawLine(x, 6, x, getSize().height - 6);
			g2.drawArc(x, getSize().height - 12, 10, 10, 180, 90);
		}
		else if(richting == Symbool.RICHTING_RECHTS)
		{
			int x = this.getWidth()/2;
			g2.drawArc(x - 10, 1, 10, 10, 0, 90);
			g2.drawLine(x, 6, x, getSize().height  - 6);
			g2.drawArc(x - 10, getSize().height - 12, 10, 10, 270, 90);
		}
		
	}
	
	public int geefType()
	{
		return Symbool.HAAK;
	}
}
