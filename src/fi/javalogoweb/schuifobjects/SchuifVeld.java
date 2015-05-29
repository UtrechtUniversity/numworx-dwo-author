package fi.javalogoweb.schuifobjects;

import java.awt.*;
import javax.swing.JPanel;

public class SchuifVeld extends JPanel
{
	private Image im, imb ;
  	private Graphics gIm, gImb;
	public SchuifLaag schuiflaag;
	private boolean veranderd;
	private boolean resized;
	private boolean gesloten;
	public boolean start = true;
	
	public SchuifVeld(int x, int y, int b, int h)
	{	setBounds(x,y,b,h);
		setLayout(null);
		schuiflaag = new SchuifLaag(0,0,b,h);
		add(schuiflaag);
		setBackground(Color.white);
		veranderd = true;
		gesloten = true;
	}
	
	public void paint(Graphics g)
	{	if(veranderd || resized)
		{	Dimension dd = getSize();				
			if (im == null || imb == null || resized)
			{	if(resized && gIm!=null)gIm.dispose();
				if(resized && gImb!=null)gImb.dispose();
				im = createImage(dd.width, dd.height);
				gIm = im.getGraphics();
				imb = createImage(dd.width, dd.height);
				gImb = imb.getGraphics();
				resized = false;
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
	
	public void update(Graphics g)
	{	paint(g);
	}
	
	public void begin()
	{	if(start)
		{	resized = true;
			start = false;
		}
	}
	
	
	
	public void tekenAchtergrond(Graphics g)
	{	Dimension dd = getSize();
		g.setColor(getBackground());
		g.fillRect(0,0,dd.width,dd.height);
	}
	
	public void tekenOpnieuw()
	{	veranderd = true;
		repaint();
	}
	
	public void zetGesloten(boolean b)
	{	gesloten = b;
	}
	
	public boolean isGesloten()
	{	return gesloten;
	}
	
	
	public void zetSchuiver(Component sc)
	{	veranderd = true;
		schuiflaag.add(sc,0);
	}
	
	public void losSchuiver(Component sc)
	{	veranderd = true;
		add(sc,0);
	}
	
	public void zetOpSchuifLaag(Component c)
	{	veranderd = true;
		schuiflaag.add(c,0);
	}
	public void zetTerugSchuifLaag(Component c)
	{	veranderd = true;
		add(c,0);
	}
	
}
