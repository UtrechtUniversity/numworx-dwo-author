package fi.algebraexpressies.expressies;

import java.awt.*;

public class HaakjeRechts 
{	
	int breedte;
	int hoogte;
		
	public HaakjeRechts(int h)
	{	
		hoogte = h-2;
		
	}
	
	public void teken(Graphics g, int x, int y)
  	{ 	int h =g.getFontMetrics().getHeight();
		int hh = h/2;
		int b = h/4;
		int bb = b/2;
		breedte = b;
		y++;
		x--;
		g.drawLine(x, y, x+bb, y+bb);
		g.drawLine(x+bb, y+bb, x+b, y+hh-b);
		g.drawLine(x+b, y+hh-b, x+b, y+hoogte-hh+b);		
		g.drawLine(x+bb, y+hoogte-bb, x+b, y+hoogte-hh+b);
		g.drawLine(x, y+hoogte, x+bb, y+hoogte-bb);
		
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
