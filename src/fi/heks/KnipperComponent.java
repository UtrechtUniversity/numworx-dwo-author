package fi.heks;

import fi.heks.scobjects.*;
import java.awt.*;

public class KnipperComponent extends ScComponent
{
	private KnipperDraad knipperAnimatie;
	private boolean aan;
	private String tekst;
	
	public KnipperComponent(int x, int y, int b, int h, String s)
	{	super(x,y,b,h);
		tekst = s;
	}
	
	public void paint(Graphics g)
	{	if(aan)
		{	g.setColor(Color.red); 
			Font f = new Font("SansSerif", Font.PLAIN, (int)(3*schaal*relh/4));
			g.setFont(f);
			FontMetrics fm = g.getFontMetrics();
			int woordbreedte = fm.stringWidth(tekst);
			g.drawString(tekst,(getSize().width - woordbreedte)/2, (getSize().height +fm.getHeight())/2 - fm.getDescent());
		}
	}
	
	public void start()
	{	knipperAnimatie = new KnipperDraad();
		knipperAnimatie.start();
	}
				
	class KnipperDraad extends Thread 
	{	
		public void run()
		{	while(true)
			{	if(aan)aan = false;
				else aan = true;
				repaint();
				try
    			{   knipperAnimatie.sleep(500);
				}
    			catch(InterruptedException e)    // geen ;
				{   };
			}
		}
	}
}
