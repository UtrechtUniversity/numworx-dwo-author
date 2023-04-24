package fi.algebrapijlenopdr.expressies_ap;

import java.awt.*;

public class HaakjeLinks 
{	
	int breedte;
	int hoogte;
		
	public HaakjeLinks(int h)
	{	hoogte = h-2;
	}
	
	public void teken(Graphics g, int x, int y)
  	{ 	int h = g.getFontMetrics().getHeight();
		int hh = h/2;
		int b = h/4;
		int bb = b/2;
		breedte = b;
		y++;
		g.drawLine(x+b, y, x+b-bb, y+bb);
		g.drawLine(x+b-bb, y+bb, x, y+hh-b);
		g.drawLine(x, y+hh-b, x, y+hoogte-hh+b);		
		g.drawLine(x+b-bb, y+hoogte-bb, x, y+hoogte-hh+b);
		g.drawLine(x+b, y+hoogte, x+b-bb, y+hoogte-bb);
	}
	
	public void zetMaat(FontMetrics fm)
  	{	int h =fm.getHeight();
		int b = h/4;
		breedte = b;
	}
	public static int geefHBreedte(FontMetrics fm)
	{	int h =fm.getHeight();
		int b = h/4;
		return b;
	}

}
