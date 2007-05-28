package fi.heks;

import java.awt.*;
import fi.heks.scobjects.*;


public class AchtergrondContainer extends ScContainer
{
	private Image bufferimage ;
  	private Graphics gIm ;
	
	
	
	public AchtergrondContainer(int x, int y, int b, int h)
	{	super(x,y,b,h);
	}
	
	public void paint(Graphics g)
	{	Dimension dd = getSize();				
		if (bufferimage == null || resized)
		{	bufferimage = createImage(dd.width, dd.height);
			gIm = bufferimage.getGraphics();			gIm.setColor(getBackground());			gIm.fillRect(0,0,dd.width,dd.height);
			gIm.setColor(Color.black);			gIm.drawRect(0,0,dd.width-1,dd.height-1);
			gIm.drawRect(1,1,dd.width-3,dd.height-3);			super.paint(gIm);
			gIm.dispose();			resized = false;
		}
		g.drawImage(bufferimage, 0, 0, null);
	}
	
}
