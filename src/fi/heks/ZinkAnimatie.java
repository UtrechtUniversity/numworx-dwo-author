package fi.heks;

import java.awt.*;
import fi.heks.scobjects.*;
import fi.beans.appletutil.*;
import fi.heks.vectortek.*;
import java.applet.*;

public class ZinkAnimatie extends ScContainer implements Runnable 
{
	//private Applet eigenaar;
	//private AppletUtil au;
	Object owner;
		
	private Tekening plusBlokje, minBlokje, blokje;
	private int blx, bly;
	private Thread zinkAnimatie;
	private BellenDraad bellenAnimatie;
	private boolean klaar = true;
	private int[] belx, bely;
	private double[] beld;
	private int aantalBellen;
	private boolean bellenAan;

	public ZinkAnimatie(int x, int y, int b, int h, Object owner) 
	{
		super(x, y, b, h);
		//eigenaar = hk;
		//au = new AppletUtil(eigenaar);
		this.owner = owner;
		

		plusBlokje = new Tekening(0, 200, 60 * h / 200, 60 * h / 200, owner, "blokjePlus.gif");
		add(plusBlokje);

		minBlokje = new Tekening(0, 200, 60 * h / 200, 60 * h / 200, owner, "blokjeMin.gif");
		add(minBlokje);
		aantalBellen = 50;
		belx = new int[aantalBellen];
		bely = new int[aantalBellen];
		beld = new double[aantalBellen];
		for (int i = 0; i < aantalBellen; i++) 
		{
			belx[i] = (int) (schaal * (10 + (relb - 20) * Math.random()));
			bely[i] = (int) (schaal * (20 + (relh - 20) * Math.random()));
			beld[i] = schaal * 2;
		}
		bellenAan = true;
		// bellenAnimatie = new BellenDraad();
		// bellenAnimatie.start();
	}

	public void start() 
	{
		bellenAan = true;
		startBellen();
	}

	public void stop() 
	{
		bellenAan = false;
	}

	public void paint(Graphics g) 
	{
		super.paint(g);
		g.setColor(new Color(255, 0, 255));
		for (int i = 0; i < aantalBellen; i++) {
			g.drawOval(belx[i], bely[i], (int) beld[i], (int) beld[i]);
		}
	}

	public void zetBellenAan(boolean b) 
	{
		bellenAan = b;
	}

	public void run() 
	{
		int breedte = getSize().width;
		int hoogte = getSize().height;
		while (bly < hoogte) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) // geen ;
			{
			}
			;
			blokje.setLocation(blx, bly);
			bly += 2;
			repaint();
		}
		klaar = true;
	}

	public void pauze(int millisec) 
	{
		try 
		{
			Thread.sleep(millisec);
		} 
		catch (InterruptedException e) // geen ;
		{}
	}

	public void start(boolean plus, int startx) 
	{
		if (startx < 0)
			blx = 0;
		else if (startx > getSize().width - plusBlokje.getSize().width)
			blx = getSize().width - plusBlokje.getSize().width;
		else
			blx = startx;
		bly = 0;
		if (plus) 
		{
			blokje = plusBlokje;
			minBlokje.setLocation(0, 200);
		} 
		else 
		{
			blokje = minBlokje;
			plusBlokje.setLocation(0, 200);
		}
		blokje.setLocation(blx, bly);
		if (klaar) 
		{
			klaar = false;
			zinkAnimatie = new Thread(this);
			zinkAnimatie.start();
		}
	}

	public void startBellen() 
	{
		bellenAnimatie = new BellenDraad();
		bellenAnimatie.start();
	}

	class BellenDraad extends Thread 
	{
		public void run() 
		{
			while (bellenAan) 
			{
				for (int i = 0; i < aantalBellen; i++) 
				{
					bely[i] -= 2;
					beld[i] += 0.1;
					if (bely[i] < 0) {
						belx[i] = (int) (schaal * relb * Math.random());
						bely[i] = (int) (schaal * relh * Math.random());
						beld[i] = 2;
					}
				}
				try 
				{
					Thread.sleep(10);
				} catch (InterruptedException e) // geen ;
				{}
				;
				repaint();
			}
		}
	}

}
