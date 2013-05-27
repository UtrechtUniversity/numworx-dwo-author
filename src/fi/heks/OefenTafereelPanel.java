package fi.heks;

import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import java.util.*;
import fi.heks.scobjects.*;
import fi.beans.appletutil.*;
import fi.heks.vectortek.*;

public class OefenTafereelPanel extends ScPanel implements MouseListener, MouseMotionListener, ActionListener {
	private AppletUtil au;
	private ActionListener actionListener;
	private AchtergrondContainer achtergrond;

	private Tekening blokjePlus, blokjeMin, blokjeSleep, blokjeSleepMin;
	private Tekening pot, potinhoud, erinpijl, eruitpijl;
	private ZinkAnimatie za;
	private GetalComponent tc;
	private ScPanel sleeppanel;
	private Polygon[] p;
	// private AudioClip plons, bubbel;

	private int laatstex, laatstey;
	private boolean instelbaar;
	private boolean raakSleep, raakSleepMin;
	private boolean plusEruit, minEruit, pasEruit;
	private boolean[] kleurBlokjes = { true, false, true, true, true, true, false, false, false, false, true };

	private boolean actief, erinMogelijk, eruitMogelijk;
	private int aantalKerenGebruikt;

	public OefenTafereelPanel(int x, int y, int b, int h, Applet applet) {
		super(x, y, b, h);
		// setBackground(new Color(255,255,220));
		setBackground(getBackground());

		au = new AppletUtil(applet);
		// plons = au.getAudioClip("resources/watersplash.au");
		// bubbel = au.getAudioClip("resources/bubble.au");

		// raakPlusBuiten = false;
		// raakPlusBinnen = false;
		// raakMinBuiten = false;
		// raakMinBinnen = false;
		raakSleep = false;
		plusEruit = false;
		minEruit = false;
		pasEruit = false;
		erinMogelijk = true;
		eruitMogelijk = true;
		instelbaar = false;

		actief = false;
		aantalKerenGebruikt = 0;

		Color color_01 = new Color(240, 240, 240);
		String kleurcode = applet.getParameter("color_01");
		if (kleurcode != null)
			color_01 = new Color(Integer.parseInt(kleurcode.substring(1), 16));

		sleeppanel = new ScPanel(0, 0, b, h - 5);
		// sleeppanel.setBackground(color_01);
		sleeppanel.addMouseListener(this);
		sleeppanel.addMouseMotionListener(this);
		achtergrond = new AchtergrondContainer(0, 0, b, h - 5);

		pot = new Tekening(40, 80, 175, 150, au, "potnieuw.gif");
		achtergrond.add(pot);

		erinpijl = new Tekening(20, 10, 100, 75, au, "gebogenpijl.gif");
		erinpijl.setVisible(false);
		achtergrond.add(erinpijl);

		eruitpijl = new Tekening(120, 10, 100, 75, au, "gebogenpijl.gif");
		eruitpijl.setVisible(false);
		achtergrond.add(eruitpijl);

		blokjePlus = new Tekening(200, 5, 30, 30, au, "blokjePlus.gif");
		achtergrond.add(blokjePlus);

		blokjeMin = new Tekening(200, 40, 30, 30, au, "blokjeMin.gif");
		achtergrond.add(blokjeMin);

		za = new ZinkAnimatie(75, 90, 105, 100, applet);
		za.zetBellenAan(false);
		sleeppanel.add(za, 0);

		blokjeSleep = new Tekening(-100, -100, 30, 30, au, "blokjePlus.gif");
		sleeppanel.add(blokjeSleep, 0);

		blokjeSleepMin = new Tekening(-100, -100, 30, 30, au, "blokjeMin.gif");
		sleeppanel.add(blokjeSleepMin, 0);

		potinhoud = new Tekening(40, 80, 175, 150, au, "inhoudnieuw.gif");
		sleeppanel.add(potinhoud, 0);

		tc = new GetalComponent(100, 125, 80, 40);
		tc.zetAlsTemp(true);
		tc.zetBekend(false);
		sleeppanel.add(tc, 0);
		sleeppanel.add(achtergrond);
		add(sleeppanel);
	}

	public void zetErinMogelijk(boolean b) {
		erinMogelijk = b;
		if (!b) {
			eruitpijl.setVisible(true);
			achtergrond.remove(blokjePlus);
			achtergrond.remove(blokjeMin);
			// blokjeSleep.setLocation(-100,-100);
			// blokjeSleepMin.setLocation(-100,-100);
			blokjeSleep.repaint();
		} else {
			eruitpijl.setVisible(false);
		}
	}

	public void zetEruitMogelijk(boolean b) {
		eruitMogelijk = b;
		if (!b) {
			erinpijl.setVisible(true);
		} else {
			erinpijl.setVisible(false);
		}
	}

	public void zetBeginTemp(int temp) {
		tc.zetWaarde(temp);
	}

	public void zetInstelbaar(boolean b) {
		instelbaar = b;
		tc.zetInstelbaar(b);
		if (b) {
			tc.addActionListener(this);
		} else {
			tc.removeActionListener(this);
		}
	}

	public void zetActief(boolean b) {
		actief = b;
		if (!b)
			tc.zetBekend(false);
		tc.repaint();
	}

	public void zetGebruikt(int aantal) {
		aantalKerenGebruikt = aantal;
		tc.zetBekend(false);
		tc.repaint();
	}

	public int geefGebruikt() {
		return aantalKerenGebruikt;
	}

	public int geefTemp() {
		return tc.geefWaarde();
	}

	public void addActionListener(ActionListener listener) {
		actionListener = AWTEventMulticaster.add(actionListener, listener);
	}

	public void removeActionListener(ActionListener listener) {
		actionListener = AWTEventMulticaster.remove(actionListener, listener);
	}

	public void mousePressed(MouseEvent e) {
		laatstex = e.getX();
		laatstey = e.getY();
		if (instelbaar)
			tc.zetInstelbaar(false);

		p = new Polygon[11];
		for (int i = 0; i < 11; i++) {
			p[i] = ((VeelhoekTek) (potinhoud.to[i])).basisPolygon;
		}

		if ((blokjePlus.contains(e.getX(), e.getY()) || blokjeMin.contains(e.getX(), e.getY())) && erinMogelijk) {
			blokjeSleep.setLocation((int) (schaal * 200), (int) (schaal * 5));
			blokjeSleepMin.setLocation((int) (schaal * 200), (int) (schaal * 40));
			// raakPlusBuiten = true;
		}

		if (blokjeSleep.contains(e.getX(), e.getY())) {
			raakSleep = true;
		}

		if (blokjeSleepMin.contains(e.getX(), e.getY())) {
			raakSleepMin = true;
		}
		int x = e.getX() - potinhoud.getLocation().x;
		int y = e.getY() - potinhoud.getLocation().y;

		for (int i = 10; i > -1; i--) {
			if (p[i].contains(x, y)) {
				if (kleurBlokjes[i] && eruitMogelijk) {
					blokjeSleep.setLocation(e.getX() - blokjeSleep.getSize().width / 2, e.getY() - blokjeSleep.getSize().height / 2);
					blokjeSleep.repaint();
					plusEruit = true;
				} else if (eruitMogelijk) {
					blokjeSleepMin.setLocation(e.getX() - blokjeSleep.getSize().width / 2, e.getY() - blokjeSleep.getSize().height / 2);
					blokjeSleepMin.repaint();
					minEruit = true;
				}

				return;
			}
		}

	}

	public void mouseDragged(MouseEvent e) {
		int dx = e.getX() - laatstex;
		int dy = e.getY() - laatstey;

		if (raakSleep) {
			blokjeSleep.setLocation(blokjeSleep.getLocation().x + dx, blokjeSleep.getLocation().y + dy);
			Polygon p = ((VulKrommeTek) (pot.to[2])).buigPolygon;
			int lx = pot.getLocation().x;
			int ly = pot.getLocation().y;
			for (int i = 0; i < p.npoints; i++) {
				if (blokjeSleep.contains(p.xpoints[i] + lx, p.ypoints[i] + ly)) {
					blokjeSleep.setLocation(blokjeSleep.getLocation().x - dx, blokjeSleep.getLocation().y - dy);
					raakSleep = false;
				}
			}
			blokjeSleep.repaint();
		}
		if (raakSleepMin) {
			blokjeSleepMin.setLocation(blokjeSleepMin.getLocation().x + dx, blokjeSleepMin.getLocation().y + dy);
			Polygon p = ((VulKrommeTek) (pot.to[2])).buigPolygon;
			int lx = pot.getLocation().x;
			int ly = pot.getLocation().y;
			for (int i = 0; i < p.npoints; i++) {
				if (blokjeSleepMin.contains(p.xpoints[i] + lx, p.ypoints[i] + ly)) {
					blokjeSleepMin.setLocation(blokjeSleepMin.getLocation().x - dx, blokjeSleepMin.getLocation().y - dy);
					raakSleepMin = false;
				}
			}
			blokjeSleepMin.repaint();
		} else if (blokjeSleep.contains(e.getX(), e.getY())) {
			raakSleep = true;
		} else if (blokjeSleepMin.contains(e.getX(), e.getY())) {
			raakSleepMin = true;
		}

		if (!plusEruit && blokjeSleep.getLocation().x + blokjeSleep.getSize().width < za.getLocation().x + za.getSize().width
				&& blokjeSleep.getLocation().x > za.getLocation().x && blokjeSleep.getLocation().y > za.getLocation().y
				&& blokjeSleep.getLocation().y + blokjeSleep.getSize().height < za.getLocation().y + za.getSize().height) { // plons.play();
			za.start(true, blokjeSleep.getLocation().x - za.getLocation().x);
			tc.verhoog();
			if (actionListener != null) {
				actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "plusErin"));
			}
			if (!erinMogelijk)
				blokjeSleep.setLocation(-100, -100);
			else
				blokjeSleep.setLocation((int) (blokjePlus.getLocation().x), (int) (blokjePlus.getLocation().y));
			raakSleep = false;
			pasEruit = false;
		}

		if (!minEruit && blokjeSleepMin.getLocation().x + blokjeSleepMin.getSize().width < za.getLocation().x + za.getSize().width
				&& blokjeSleepMin.getLocation().x > za.getLocation().x && blokjeSleepMin.getLocation().y > za.getLocation().y
				&& blokjeSleepMin.getLocation().y + blokjeSleepMin.getSize().height < za.getLocation().y + za.getSize().height) { // plons.play();
			za.start(false, blokjeSleepMin.getLocation().x - za.getLocation().x);
			tc.verlaag();
			if (actionListener != null) {
				actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "minErin"));
			}
			if (!erinMogelijk)
				blokjeSleepMin.setLocation(-100, -100);
			else
				blokjeSleepMin.setLocation((int) (blokjeMin.getLocation().x), (int) (blokjeMin.getLocation().y));
			raakSleepMin = false;
			pasEruit = false;
		}

		if (plusEruit && blokjeSleep.getLocation().y < za.getLocation().y) {
			plusEruit = false;
			pasEruit = true;
			// bubbel.play();
			tc.verlaag();
			if (actionListener != null) {
				actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "plusEruit"));
			}
		}
		if (minEruit && blokjeSleepMin.getLocation().y < za.getLocation().y) {
			minEruit = false;
			pasEruit = true;
			// bubbel.play();
			tc.verhoog();
			if (actionListener != null) {
				actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "minEruit"));
			}
		}
		laatstex = e.getX();
		laatstey = e.getY();
	}

	public void mouseReleased(MouseEvent e) {
		raakSleep = false;
		raakSleepMin = false;
		if (instelbaar)
			tc.zetInstelbaar(true);

		if (!plusEruit && blokjeSleep.getLocation().x + blokjeSleep.getSize().width < pot.getLocation().x + pot.getSize().width
				&& blokjeSleep.getLocation().x > pot.getLocation().x
				&& blokjeSleep.getLocation().y + blokjeSleep.getSize().height < za.getLocation().y + za.getSize().height) {
			int x = blokjeSleep.getLocation().x;
			if (!erinMogelijk)
				blokjeSleep.setLocation(-100, -100);
			else
				blokjeSleep.setLocation((int) (blokjePlus.getLocation().x), (int) (blokjePlus.getLocation().y));
			// plons.play();
			za.start(true, x - za.getLocation().x);
			tc.verhoog();
			if (actionListener != null) {
				actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "plusErin"));
			}
		} else {
			if (!erinMogelijk)
				blokjeSleep.setLocation(-100, -100);
			else
				blokjeSleep.setLocation((int) (blokjePlus.getLocation().x), (int) (blokjePlus.getLocation().y));
		}

		if (!minEruit && blokjeSleepMin.getLocation().x + blokjeSleepMin.getSize().width < pot.getLocation().x + pot.getSize().width
				&& blokjeSleepMin.getLocation().x > pot.getLocation().x
				&& blokjeSleepMin.getLocation().y + blokjeSleepMin.getSize().height < za.getLocation().y + za.getSize().height) {
			int x = blokjeSleepMin.getLocation().x;
			if (!erinMogelijk)
				blokjeSleepMin.setLocation(-100, -100);
			else
				blokjeSleepMin.setLocation((int) (blokjeMin.getLocation().x), (int) (blokjeMin.getLocation().y));
			// plons.play();
			za.start(false, x - za.getLocation().x);
			tc.verlaag();
			if (actionListener != null) {
				actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "minErin"));
			}
		} else {
			if (!erinMogelijk)
				blokjeSleepMin.setLocation(-100, -100);
			else
				blokjeSleepMin.setLocation((int) (blokjeMin.getLocation().x), (int) (blokjeMin.getLocation().y));
		}

		plusEruit = false;
		minEruit = false;
		pasEruit = false;
		// ((TweeManierenPanel)(getParent())).controleer();
	}

	public void mouseMoved(MouseEvent e) {
		;
	}

	public void mouseExited(MouseEvent e) {
		;
	}

	public void mouseClicked(MouseEvent e) {
		;
	}

	public void mouseEntered(MouseEvent e) {
		;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == tc && tc.isBekend()) {
			if (e.getActionCommand().equals("focuslost")
			// || e.getActionCommand().equals("action")
			// &&
			// System.getProperties().getProperty("java.vendor").equals("Microsoft Corp.")
			) {
				aantalKerenGebruikt++;
				actief = true;
			}
		} else
			actief = false;
	}
}
