package fi.wiskopdr.symbolen;

import java.awt.Graphics;
import java.awt.Graphics2D;

public class Pijl extends Symbool{

	public Pijl(int richting)
	{
		super(richting);
	}
	
	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = createGraphics2D(g);
		
		if(richting == RICHTING_LINKS)
		{	g2.drawLine(0, this.getHeight()/2, this.getWidth(), this.getHeight()/2);
			g2.drawLine(0, this.getHeight()/2, 5, this.getHeight()/2 - 5);
			g2.drawLine(0, this.getHeight()/2, 5, this.getHeight()/2 + 5);
		}
		else if(richting == RICHTING_RECHTS)
		{	g2.drawLine(0, this.getHeight()/2, this.getWidth(), this.getHeight()/2);
			g2.drawLine(this.getWidth(), this.getHeight()/2, this.getWidth() - 5, this.getHeight()/2 - 5);
			g2.drawLine(this.getWidth(), this.getHeight()/2, this.getWidth() - 5, this.getHeight()/2 + 5);
		}
		else if(richting == RICHTING_BOVEN)
		{	g2.drawLine(this.getWidth()/2, 0, this.getWidth()/2, this.getHeight());
			g2.drawLine(this.getWidth()/2, 0, this.getWidth()/2 - 5, 5);
			g2.drawLine(this.getWidth()/2, 0, this.getWidth()/2 + 5, 5);
		
		}
		else if(richting == RICHTING_BENEDEN)
		{	g2.drawLine(this.getWidth()/2, 0, this.getWidth()/2, this.getHeight());
			g2.drawLine(this.getWidth()/2, this.getHeight(), this.getWidth()/2 - 5, this.getHeight() - 5);
			g2.drawLine(this.getWidth()/2, this.getHeight(), this.getWidth()/2 + 5, this.getHeight() - 5);
		}
		else if(richting == RICHTING_LINKSBOVEN)
		{	g2.drawLine(0, 0, this.getWidth(), this.getHeight());
			g2.drawLine(0, 0, 5, 0);
			g2.drawLine(0, 0, 0, 5);
		
		}
		else if(richting == RICHTING_RECHTSONDER)
		{	g2.drawLine(0, 0, this.getWidth(), this.getHeight());
			g2.drawLine(this.getWidth() - 1, this.getHeight() - 1, this.getWidth() - 6, this.getHeight() - 1);
			g2.drawLine(this.getWidth() - 1, this.getHeight() - 1, this.getWidth() - 1, this.getHeight() - 6);
		
		}
		else if(richting == RICHTING_LINKSONDER)
		{	g2.drawLine(0, this.getHeight(), this.getWidth(), 0);
			g2.drawLine(0, this.getHeight() - 1, 5, this.getHeight() - 1);
			g2.drawLine(0, this.getHeight() - 1, 0, this.getHeight() - 6);
		
		}
		else
		{	g2.drawLine(0, this.getHeight(), this.getWidth(), 0);
			g2.drawLine(this.getWidth() - 1, 0, this.getWidth() - 6, 0);
			g2.drawLine(this.getWidth() - 1, 0, this.getWidth() - 1, 5);
		
		}
			
	}
	
	public int geefType()
	{
		return Symbool.PIJL;
	}
}
