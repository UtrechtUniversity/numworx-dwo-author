package fi.algebraexpressies.expressies_ap;

import java.awt.*;

public class Wortelteken 
{	
	int breedte;
	int hoogte;
			
	public Wortelteken(int b, int h)
	{	breedte = b;
		hoogte = h;
	}
	
	public void paint(Graphics gIm, int x, int y)
  	{ 	gIm.setColor(Color.black);
		gIm.drawLine(x+2,y+hoogte/2,x+hoogte/4,y+hoogte-1);
		gIm.drawLine(x+hoogte/2,y+0,x+hoogte/4,y+hoogte-1);
		gIm.drawLine(x+breedte-1,y+0,x+hoogte/2,y+0);
	}
}
