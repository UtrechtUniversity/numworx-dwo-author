package fi.algebrapijlenopdr.formuleobjects;

import java.awt.*;

public class BufferedPanel extends Container
{	
	private Image im;
	private Graphics gIm;
	private boolean resized = false;
	
	public void paint(Graphics g)
	{	{ 	if(im==null || resized)
			{	if(resized && gIm!=null)
				{	gIm.dispose();
				}
				im = createImage(getSize().width,getSize().height);
  				gIm = im.getGraphics();
				resized = false;
			}
			gIm.setColor(getBackground());
			gIm.fillRect(0,0,getSize().width,getSize().height);
			super.paint(gIm);
			g.drawImage(im, 0, 0, null);
  		}
	}		public void setSize(int b, int h)	{	resized = true;		super.setSize(b,h);	}
	
	public void destroy()
	{	if(gIm!=null)
		{	gIm.dispose();
			gIm = null;
		}
	}
	public void update(Graphics g)
	{	paint(g);
	}
}
