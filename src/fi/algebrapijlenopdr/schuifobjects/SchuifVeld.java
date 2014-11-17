package fi.algebrapijlenopdr.schuifobjects;

import java.awt.*;

import javax.swing.*;

public class SchuifVeld extends JPanel
{
	private Image im, imb ;
  	private Graphics gIm, gImb;
	protected SchuifLaag schuiflaag;
	public boolean veranderd;
	private boolean gesloten;
	public boolean resized;
	public boolean start = true;
	
	
	public SchuifVeld()
	{	setOpaque(false);
	}
	
	public SchuifVeld(int x, int y, int b, int h)
	{	setBounds(x,y,b,h);
		setLayout(null);
		schuiflaag = new SchuifLaag(0,0,b,h);
		add(schuiflaag);
		setBackground(Color.white);
		veranderd = true;
		gesloten = false;
		resized = true;
		
		setOpaque(false);
	}
	
/*	
// origineel
	public void paint(Graphics g)
	{	if (veranderd)
		{	Dimension dd = getSize();				
			if (im == null || imb == null || resized)
			{	if (im != null)
					gIm.dispose();
				if (imb != null)
					gImb.dispose();
				im = createImage(dd.width, dd.height);
				gIm = im.getGraphics();
				imb = createImage(dd.width, dd.height);
				gImb = imb.getGraphics();
				resized = false;
				//return;
			}
			tekenAchtergrond(gIm);
			super.paint(gIm);
			veranderd = false;
		}

		gImb.drawImage(im, 0, 0, null);
		schuiflaag.zetActief(true);
		schuiflaag.paint(gImb);
		schuiflaag.zetActief(false);
		g.drawImage(imb, 0, 0, null);
		
	
	}
*/	

	public void paintComponent(Graphics g)
	{
//		if (veranderd)
//		{	
			tekenAchtergrond(g);
//			veranderd = false;
//		}
	}
	
	public Image geefImage()
	{	return im;
	}
	
	public void start()
	{	if (start)
		{	setSize(getSize().width, getSize().height);
			start = false;
		}
	}
	
	public void destroy()
	{	if (im != null)
		{	gIm.dispose();
			gIm = null;;
		}
		if (imb != null)
		{	gImb.dispose();
			gImb = null;
		}
	}
	
	public void setSize(int b, int h)
	{	resized = true;
		schuiflaag.setSize(b, h);
		super.setSize(b, h);
		start = true;
	}
	
	
	public void update(Graphics g)
	{	paint(g);
	}
	
	public void tekenOpnieuw()
	{	veranderd = true;
		repaint();
	}
	
	public void tekenAchtergrond(Graphics g)
	{	Dimension dd = getSize();
		g.setColor(getBackground());
		g.fillRect(0, 0, dd.width, dd.height);
//		g.setColor(Color.black);
//		g.drawRect(0, 0, dd.width - 1, dd.height - 1);
		
	}
	
	public void zetGesloten(boolean b)
	{	gesloten = b;
	}
	
	public boolean isGesloten()
	{	return gesloten;
	}
/*	
	public void zetSchuiver(SchuifComponent sc)
	{	veranderd = true;
		schuiflaag.add(sc, 0);
	}
*/
/*	
	public void losSchuiver(SchuifComponent sc)
	{	veranderd = true;
		add(sc, 0);
	}
*/
/*	
	public void zetOpSchuifLaag(Component c)
	{	veranderd = true;
		schuiflaag.add(c, 0);
	}
*/
/*	
	public void zetTerugSchuifLaag(Component c)
	{	veranderd = true;
		add(c, 0);
	}
*/	
}
