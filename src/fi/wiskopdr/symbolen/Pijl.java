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
		{	g2.drawLine(dikte, this.getHeight()/2, this.getWidth(), this.getHeight()/2);
			g2.drawLine(dikte, this.getHeight()/2, 5 + dikte, this.getHeight()/2 - 5);
			g2.drawLine(dikte, this.getHeight()/2, 5 + dikte, this.getHeight()/2 + 5);
		}
		else if(richting == RICHTING_RECHTS)
		{	g2.drawLine(0, this.getHeight()/2, this.getWidth() - dikte, this.getHeight()/2);
			g2.drawLine(this.getWidth() - dikte, this.getHeight()/2, this.getWidth() - 5 - dikte, this.getHeight()/2 - 5);
			g2.drawLine(this.getWidth() - dikte, this.getHeight()/2, this.getWidth() - 5 - dikte, this.getHeight()/2 + 5);
		}
		else if(richting == RICHTING_BOVEN)
		{	g2.drawLine(this.getWidth()/2, dikte, this.getWidth()/2, this.getHeight());
			g2.drawLine(this.getWidth()/2, dikte, this.getWidth()/2 - 5, 5 + dikte);
			g2.drawLine(this.getWidth()/2, dikte, this.getWidth()/2 + 5, 5 + dikte);
		
		}
		else if(richting == RICHTING_BENEDEN)
		{	g2.drawLine(this.getWidth()/2, 0, this.getWidth()/2, this.getHeight() - dikte);
			g2.drawLine(this.getWidth()/2, this.getHeight() - dikte , this.getWidth()/2 - 5, this.getHeight() - 5 - dikte);
			g2.drawLine(this.getWidth()/2, this.getHeight() - dikte, this.getWidth()/2 + 5, this.getHeight() - 5 - dikte);
		}
		else if(richting == RICHTING_LINKSBOVEN)
		{	g2.drawLine(0, 0, this.getWidth(), this.getHeight());
			g2.drawLine(0, dikte/2, 5 + dikte, dikte/2);
			g2.drawLine(dikte/2, 0, dikte/2, 5 + dikte);
		
		}
		else if(richting == RICHTING_RECHTSONDER)
		{	g2.drawLine(0, 0, this.getWidth(), this.getHeight());
			g2.drawLine(this.getWidth() - 1, this.getHeight() - 1 - dikte/2, this.getWidth() - 6 - dikte, this.getHeight() - 1 - dikte/2);
			g2.drawLine(this.getWidth() - 1 - dikte/2, this.getHeight() - 1, this.getWidth() - 1 - dikte/2, this.getHeight() - 6 - dikte);
		
		}
		else if(richting == RICHTING_LINKSONDER)
		{	g2.drawLine(0, this.getHeight(), this.getWidth(), 0);
			g2.drawLine(0, this.getHeight() - 1 - dikte/2, 5 + dikte, this.getHeight() - 1 - dikte/2);
			g2.drawLine(dikte/2, this.getHeight() - 1, dikte/2, this.getHeight() - 6 - dikte);
		
		}
		else
		{	g2.drawLine(0, this.getHeight(), this.getWidth() - 1, 0);
			g2.drawLine(this.getWidth(), dikte/2, this.getWidth() - 6 - dikte, dikte/2);
			g2.drawLine(this.getWidth() - dikte/2 - 1, 0, this.getWidth() - dikte/2 - 1, 5 + dikte);
		
		}
			
	}
	
	public int geefType()
	{
		return Symbool.PIJL;
	}
}
