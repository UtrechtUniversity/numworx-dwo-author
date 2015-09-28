package fi.wiskopdr.symbolen;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class Lijn extends Symbool {

	public Lijn(int richting)
	{
		super(richting);
	}
	
	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = createGraphics2D(g);
		
		if(richting == RICHTING_LINKS || richting == RICHTING_RECHTS)
			g2.drawLine(0, this.getHeight()/2, this.getWidth(), this.getHeight()/2);
		else if(richting == RICHTING_BOVEN || richting == RICHTING_BENEDEN)
			g2.drawLine(this.getWidth()/2, 0, this.getWidth()/2, this.getHeight());
		else if(richting == RICHTING_LINKSBOVEN || richting == RICHTING_RECHTSONDER)
			g2.drawLine(0, 0, this.getWidth(), this.getHeight());
		else
			g2.drawLine(0, this.getHeight(), this.getWidth(), 0);
			
	}
	
	public int geefType()
	{
		return Symbool.LIJN;
	}
}
