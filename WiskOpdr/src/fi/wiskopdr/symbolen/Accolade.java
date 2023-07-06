package fi.wiskopdr.symbolen;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class Accolade extends Symbool{

	public Accolade(int richting)
	{
		super(richting);
	}
	
	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = createGraphics2D(g);
		
		if(richting == Symbool.RICHTING_LINKS)
		{	int x = this.getWidth()/2;
			g2.drawArc(x, 1, 10, 10, 90, 90);
			g2.drawLine(x, 6, x, getSize().height / 2 - 3);
			g2.drawArc(x - 5, getSize().height / 2 - 6, 5, 5, 270, 90);
			g2.drawArc(x - 5, getSize().height / 2, 5, 5, 0, 90);
			g2.drawLine(x, getSize().height / 2 + 3, x, getSize().height - 6);
			g2.drawArc(x, getSize().height - 12, 10, 10, 180, 90);
		}
		else if(richting == Symbool.RICHTING_RECHTS)
		{
			int x = this.getWidth()/2;
			g2.drawArc(x - 10, 1, 10, 10, 0, 90);
			g2.drawLine(x, 6, x, getSize().height / 2 - 3);
			g2.drawArc(x, getSize().height / 2 - 6, 5, 5, 180, 90);
			g2.drawArc(x, getSize().height / 2, 5, 5, 90, 90);
			g2.drawLine(x, getSize().height / 2 + 3, x, getSize().height - 6);
			g2.drawArc(x - 10, getSize().height - 12, 10, 10, 270, 90);
		}
		else if(richting == Symbool.RICHTING_BENEDEN)
		{
			int y = this.getHeight()/2;
			g2.drawArc(1,  y - 10, 10, 10, 180, 90);
			g2.drawLine(6, y, getSize().width / 2 - 3, y);
			g2.drawArc(getSize().width / 2 - 6, y, 5, 5, 0, 90);
			g2.drawArc(getSize().width / 2, y, 5, 5, 90, 90);
			g2.drawLine(getSize().width / 2 + 3, y, getSize().width - 6, y);
			g2.drawArc(getSize().width - 12, y - 10, 10, 10, 270, 90);
		}
		else if(richting == Symbool.RICHTING_BOVEN)
		{
			int y = this.getHeight()/2;
			g2.drawArc(1,  y, 10, 10, 90, 90);
			g2.drawLine(6, y, getSize().width / 2 - 3, y);
			g2.drawArc(getSize().width / 2 - 6, y - 5, 5, 5, 270, 90);
			g2.drawArc(getSize().width / 2, y - 5, 5, 5, 180, 90);
			g2.drawLine(getSize().width / 2 + 3, y, getSize().width - 6, y);
			g2.drawArc(getSize().width - 12, y, 10, 10, 0, 90);
		}
		
	}
	
	public int geefType()
	{
		return Symbool.ACCOLADE;
	}
}
