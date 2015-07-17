package fi.wiskopdr.symbolen;

import java.awt.Graphics;

public class Pijl extends Symbool{

	public Pijl(int richting)
	{
		super(richting);
	}
	
	public void paintComponent(Graphics g)
	{
		g.setColor(kleur);
		//uitzoeken hoe ik dikte goed kan verwerken. Meerdere lijnen naast elkaar? Of kan ik één dikke stroke maken?
		//iets met rendering?
		
		if(richting == RICHTING_LINKS)
		{	g.drawLine(0, this.getHeight()/2, this.getWidth(), this.getHeight()/2);
			g.drawLine(0, this.getHeight()/2, 5, this.getHeight()/2 - 5);
			g.drawLine(0, this.getHeight()/2, 5, this.getHeight()/2 + 5);
		}
		else if(richting == RICHTING_RECHTS)
		{	g.drawLine(0, this.getHeight()/2, this.getWidth(), this.getHeight()/2);
			g.drawLine(this.getWidth(), this.getHeight()/2, this.getWidth() - 5, this.getHeight()/2 - 5);
			g.drawLine(this.getWidth(), this.getHeight()/2, this.getWidth() - 5, this.getHeight()/2 + 5);
		}
		else if(richting == RICHTING_BOVEN)
		{	g.drawLine(this.getWidth()/2, 0, this.getWidth()/2, this.getHeight());
			g.drawLine(this.getWidth()/2, 0, this.getWidth()/2 - 5, 5);
			g.drawLine(this.getWidth()/2, 0, this.getWidth()/2 + 5, 5);
		
		}
		else if(richting == RICHTING_BENEDEN)
		{	g.drawLine(this.getWidth()/2, 0, this.getWidth()/2, this.getHeight());
			g.drawLine(this.getWidth()/2, this.getHeight(), this.getWidth()/2 - 5, this.getHeight() - 5);
			g.drawLine(this.getWidth()/2, this.getHeight(), this.getWidth()/2 + 5, this.getHeight() - 5);
		}
		else if(richting == RICHTING_LINKSBOVEN)
		{	g.drawLine(0, 0, this.getWidth(), this.getHeight());
			g.drawLine(0, 0, 5, 0);
			g.drawLine(0, 0, 0, 5);
		
		}
		else if(richting == RICHTING_RECHTSONDER)
		{	g.drawLine(0, 0, this.getWidth(), this.getHeight());
			g.drawLine(this.getWidth() - 1, this.getHeight() - 1, this.getWidth() - 6, this.getHeight() - 1);
			g.drawLine(this.getWidth() - 1, this.getHeight() - 1, this.getWidth() - 1, this.getHeight() - 6);
		
		}
		else if(richting == RICHTING_LINKSONDER)
		{	g.drawLine(0, this.getHeight(), this.getWidth(), 0);
			g.drawLine(0, this.getHeight() - 1, 5, this.getHeight() - 1);
			g.drawLine(0, this.getHeight() - 1, 0, this.getHeight() - 6);
		
		}
		else
		{	g.drawLine(0, this.getHeight(), this.getWidth(), 0);
			g.drawLine(this.getWidth() - 1, 0, this.getWidth() - 6, 0);
			g.drawLine(this.getWidth() - 1, 0, this.getWidth() - 1, 5);
		
		}
			
	}
	
	public int geefType()
	{
		return Symbool.PIJL;
	}
}
