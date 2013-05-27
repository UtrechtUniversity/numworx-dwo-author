package fi.heks;

import java.awt.*;
import fi.heks.scobjects.*;
import fi.beans.appletutil.*;
import fi.heks.vectortek.*;
import java.applet.*;

public class ZinkAnimatieEmmer extends ScContainer implements Runnable {
	private Applet eigenaar;
	private AppletUtil au;
	private Emmer emmer;
	private int blx, bly;
	private Thread zinkAnimatie;
	private BellenDraad bellenAnimatie;
	private boolean klaar = true;
	private int[] belx, bely;
	private double[] beld;
	private int aantalBellen;
	private boolean bellenAan;

	public ZinkAnimatieEmmer(int x, int y, int b, int h, Applet hk) {
		super(x, y, b, h);
		eigenaar = hk;
		au = new AppletUtil(eigenaar);

		emmer = new Emmer(0, 200, 110 * h / 200, 125 * h / 200, eigenaar);
		emmer.zetInstelbaar(false);
		add(emmer);

		aantalBellen = 50;
		belx = new int[aantalBellen];
		bely = new int[aantalBellen];
		beld = new double[aantalBellen];
		for (int i = 0; i < aantalBellen; i++) {
			belx[i] = (int) (schaal * (10 + (relb - 20) * Math.random()));
			bely[i] = (int) (schaal * (20 + (relh - 20) * Math.random()));
			beld[i] = schaal * 2;
		}
		bellenAan = true;
		// bellenAnimatie = new BellenDraad();
		// bellenAnimatie.start();
	}

	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(new Color(255, 0, 255));
		for (int i = 0; i < aantalBellen; i++) {
			g.drawOval(belx[i], bely[i], (int) beld[i], (int) beld[i]);
		}
	}

	public void start() {
		bellenAan = true;
		startBellen();
	}

	public void stop() {
		bellenAan = false;
	}

	public void zetInhoud(int aantal) {
		emmer.zetInhoud(aantal);
	}

	public void zetBellenAan(boolean b) {
		bellenAan = b;
	}

	public void run() {
		int breedte = getSize().width;
		int hoogte = getSize().height;
		while (bly < hoogte) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) // geen ;
			{
			}
			;
			emmer.setLocation(blx, bly);
			bly += 2;
			repaint();
		}
		klaar = true;
	}

	public void pauze(int millisec) {
		try {
			Thread.sleep(millisec);
		} catch (InterruptedException e) // geen ;
		{
		}
	}

	public void start(boolean plus, int startx) {
		if (startx < 0)
			blx = 0;
		else if (startx > getSize().width - emmer.getSize().width)
			blx = getSize().width - emmer.getSize().width;
		else
			blx = startx;
		bly = 0;
		emmer.setLocation(blx, bly);
		if (klaar) {
			klaar = false;
			zinkAnimatie = new Thread(this);
			zinkAnimatie.start();
		}
	}

	public void startBellen() {
		bellenAnimatie = new BellenDraad();
		bellenAnimatie.start();
	}

	class BellenDraad extends Thread {
		public void run() {
			while (bellenAan) {
				for (int i = 0; i < aantalBellen; i++) {
					bely[i] -= 2;
					beld[i] += 0.1;
					if (bely[i] < 0) {
						belx[i] = (int) (schaal * relb * Math.random());
						bely[i] = (int) (schaal * relh * Math.random());
						beld[i] = 2;
					}
				}
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) // geen ;
				{
				}
				;
				repaint();
			}
		}
	}

}
