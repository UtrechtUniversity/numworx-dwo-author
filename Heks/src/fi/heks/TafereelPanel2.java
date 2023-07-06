package fi.heks;

import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import java.util.*;
import fi.heks.scobjects.*;
import fi.beans.appletutil.*;
import fi.heks.vectortek.*;

public class TafereelPanel2 extends ScPanel implements MouseListener, MouseMotionListener, ActionListener {
	// private Heks eigenaar;
	private AppletUtil au;

	private AchtergrondContainer achtergrond;

	Tekening blokjePlus, blokjeMin, blokjeSleep, blokjeSleepMin;
	Tekening pot, potEruit, potErin, beginPot, eindPot, potinhoud, vloer, schrijfheks, werkheks;
	ZinkAnimatie za;
	GetalComponent tc, beginTemp, eindTemp;
	Thermometer tm;
	BlokjesContainer eruitContainer, erinContainer;
	ScLWButton opnieuwKnop, opdrachtKnop, werkKnop;
	ScLabel beginLabel, eindLabel, erinLabel, eruitLabel, titel, opdrachtTitel;
	ScTekstContainer uitleg, opdracht;
	ScPanel sleeppanel;
	ScTextArea textArea;
	Polygon[] p;
	// AudioClip plons, bubbel;

	int laatstex, laatstey;
	boolean raakPlusBuiten, raakPlusBinnen, raakMinBuiten, raakMinBinnen, raakSleep, raakSleepMin;
	boolean plusEruit, minEruit, pasEruit;
	boolean[] kleurBlokjes = { true, false, true, true, true, true, false, false, false, false, true };

	public TafereelPanel2(int x, int y, int b, int h, Applet applet) {
		super(x, y, b, h);
		// eigenaar = applet;
		// setBackground(new Color(255,255,220));
		setBackground(getBackground());
		zetVastePlaats(true);

		au = new AppletUtil(applet);
		// plons = au.getAudioClip("resources/watersplash.au");
		// bubbel = au.getAudioClip("resources/bubble.au");

		raakPlusBuiten = false;
		raakPlusBinnen = false;
		raakMinBuiten = false;
		raakMinBinnen = false;
		raakSleep = false;
		plusEruit = false;
		minEruit = false;
		pasEruit = false;

		Color color_01 = new Color(240, 240, 240);
		String kleurcode = applet.getParameter("color_01");
		if (kleurcode != null)
			color_01 = new Color(Integer.parseInt(kleurcode.substring(1), 16));

		sleeppanel = new ScPanel(300, 0, b - 300, h - 5);
		// sleeppanel.setBackground(color_01);
		sleeppanel.addMouseListener(this);
		sleeppanel.addMouseMotionListener(this);
		achtergrond = new AchtergrondContainer(0, 0, b - 300, h - 5);

		opnieuwKnop = new ScLWButton(150, 145, 100, 25, Heks.rb.getString("opnieuwKnopLabel"));
		opnieuwKnop.addActionListener(this);
		add(opnieuwKnop);

		pot = new Tekening(40, 210, 350, 300, au, "potnieuw.gif");
		achtergrond.add(pot);

		vloer = new Tekening(0, 370, 490, 160, au, "vloer.gif");
		// achtergrond.add(vloer);

		beginTemp = new GetalComponent(170, 200, 80, 40);
		beginTemp.zetInstelbaar(true);
		beginTemp.zetAlsTemp(true);
		beginTemp.addActionListener(this);
		add(beginTemp);

		beginLabel = new ScLabel(50, 205, 60, 30, Heks.rb.getString("beginLabel"));
		beginLabel.addMouseListener(this);
		add(beginLabel);

		beginPot = new Tekening(35, 185, 90, 65, au, "potzwart.gif");
		add(beginPot);

		// schrijfheks = new Tekening(145,0,140,140,au,"schrijfheks.gif");
		// add(schrijfheks);

		werkheks = new Tekening(180, 0, 150, 150, au, "werkheks.gif");
		achtergrond.add(werkheks);

		erinLabel = new ScLabel(65, 315, 60, 30, Heks.rb.getString("erinLabel"));
		add(erinLabel);

		potErin = new Tekening(30, 270, 100, 90, au, "potErin.gif");
		add(potErin);

		eruitLabel = new ScLabel(35, 465, 60, 30, Heks.rb.getString("eruitLabel"));
		// add(eruitLabel);

		potEruit = new Tekening(30, 360, 100, 90, au, "potEruit.gif");
		// add(potEruit);

		eindTemp = new GetalComponent(170, 390, 80, 40);
		eindTemp.zetAlsTemp(true);
		add(eindTemp);

		eindLabel = new ScLabel(50, 405, 60, 30, Heks.rb.getString("eindLabel"));
		add(eindLabel);

		eindPot = new Tekening(35, 385, 90, 65, au, "potzwart.gif");
		add(eindPot);

		erinContainer = new BlokjesContainer(150, 280, 110, 85, applet);
		add(erinContainer);

		eruitContainer = new BlokjesContainer(150, 430, 110, 85, applet);
		// add(eruitContainer);

		blokjePlus = new Tekening(400, 30, 60, 60, au, "blokjePlus.gif");
		achtergrond.add(blokjePlus);

		blokjeMin = new Tekening(400, 100, 60, 60, au, "blokjeMin.gif");
		achtergrond.add(blokjeMin);

		uitleg = new ScTekstContainer(5, 5, 285, 20, 5, Heks.rb.getString("TafereelPanel2Uitleg"));
		uitleg.lijnUit(ScLabel.LINKS);
		add(uitleg);

		// opdracht = new
		// ScTekstContainer(5,405,285,20,4,"Beschrijf hieronder nauwkeurig hoe je de/temperatuur kunt regelen met blokjes.");
		// opdracht.lijnUit(ScLabel.LINKS);
		// add(opdracht);

		titel = new ScLabel(20, 10, 240, 40, "De toverdrank 1");
		// add(titel);

		za = new ZinkAnimatie(110, 240, 210, 200, applet);
		sleeppanel.add(za, 0);

		blokjeSleep = new Tekening(400, 30, 60, 60, au, "blokjePlus.gif");
		sleeppanel.add(blokjeSleep, 0);

		blokjeSleepMin = new Tekening(400, 100, 60, 60, au, "blokjeMin.gif");
		sleeppanel.add(blokjeSleepMin, 0);

		potinhoud = new Tekening(40, 210, 350, 300, au, "inhoudnieuw.gif");
		sleeppanel.add(potinhoud, 0);

		tc = new GetalComponent(390, 280, 120, 40);
		tc.zetAlsTemp(true);
		sleeppanel.add(tc, 0);

		tm = new Thermometer(100, 10, 40, 300);
		sleeppanel.add(tm, 0);

		sleeppanel.add(achtergrond);
		add(sleeppanel);

		// textArea = new
		// ScTextArea(5,450,270,110,0,0,TextArea.SCROLLBARS_VERTICAL_ONLY,"");
		// add(textArea);

		opdrachtTitel = new ScLabel(20, 200, 200, 40, Heks.rb.getString("opdrachtTitelLabel"));
		opdrachtTitel.setVisible(false);
		add(opdrachtTitel);

		opdracht = new ScTekstContainer(5, 250, 285, 20, 4, Heks.rb.getString("TafereelPanel2Opdracht"));
		opdracht.lijnUit(ScLabel.LINKS);
		opdracht.setVisible(false);
		add(opdracht);

		textArea = new ScTextArea(5, 340, 270, 150, 0, 0, TextArea.SCROLLBARS_VERTICAL_ONLY, "");
		textArea.setVisible(false);
		add(textArea);

		opdrachtKnop = new ScLWButton(5, 510, 270, 35, Heks.rb.getString("opdrachtKnopLabel"));
		opdrachtKnop.addActionListener(this);
		add(opdrachtKnop);

		werkKnop = new ScLWButton(5, 510, 270, 35, Heks.rb.getString("werkKnopLabel"));
		werkKnop.addActionListener(this);
		textArea.setVisible(false);
		add(werkKnop);
	}

	public void setState(Hashtable h) {
		String tekst = (String) h.get("tekst");

		textArea.setText(tekst);
	}

	public Hashtable getState() {
		String tekst = null;

		tekst = textArea.getText();

		Hashtable h = new Hashtable();
		h.put("tekst", tekst);

		return h;
	}

	public double getScore() {
		if (textArea.getText() != null && textArea.getText().length() > 100)
			return 100;
		if (textArea.getText() != null && textArea.getText().length() > 5)
			return 10;
		return 0;
	}

	public void start() {
		beginTemp.zetWaarde(0);
		eindTemp.zetWaarde(0);
		tc.zetWaarde(0);
		tm.zetTemp(0);
		erinContainer.removeAll();
		eruitContainer.removeAll();
		za.start();
	}

	public void stop() {
		za.stop();
	}

	public void mousePressed(MouseEvent e) {
		if (opdracht.isVisible())
			return;
		laatstex = e.getX();
		laatstey = e.getY();

		if (beginLabel.contains(e.getX(), e.getY())) {
			beginTemp.vulIn();
		}

		p = new Polygon[11];
		for (int i = 0; i < 11; i++) {
			p[i] = ((VeelhoekTek) (potinhoud.to[i])).basisPolygon;
		}

		if (blokjePlus.contains(e.getX(), e.getY())) {
			raakPlusBuiten = true;
		}

		if (blokjeSleep.contains(e.getX(), e.getY())) {
			raakSleep = true;
		}

		if (blokjeSleepMin.contains(e.getX(), e.getY())) {
			raakSleepMin = true;
		}
		int x = e.getX() - potinhoud.getLocation().x;
		int y = e.getY() - potinhoud.getLocation().y;

		/*
		 * for(int i=10 ; i>-1 ; i--) { if(p[i].contains(x,y)) {
		 * if(kleurBlokjes[i]) {
		 * blokjeSleep.setLocation(e.getX()-blokjeSleep.getSize
		 * ().width/2,e.getY()-blokjeSleep.getSize().height/2);
		 * blokjeSleep.repaint(); plusEruit = true; } else {
		 * blokjeSleepMin.setLocation
		 * (e.getX()-blokjeSleep.getSize().width/2,e.getY
		 * ()-blokjeSleep.getSize().height/2); blokjeSleepMin.repaint();
		 * minEruit = true; }
		 * 
		 * return; } }
		 */

	}

	public void mouseDragged(MouseEvent e) {
		if (opdracht.isVisible())
			return;
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
			tm.tempPlus();
			eindTemp.verhoog();
			if (!pasEruit)
				erinContainer.voegBlokjeToe(true);
			else
				eruitContainer.verwijderBlokje();
			blokjeSleep.setLocation((int) (blokjePlus.getLocation().x), (int) (blokjePlus.getLocation().y));
			raakSleep = false;
			pasEruit = false;
		}

		if (!minEruit && blokjeSleepMin.getLocation().x + blokjeSleepMin.getSize().width < za.getLocation().x + za.getSize().width
				&& blokjeSleepMin.getLocation().x > za.getLocation().x && blokjeSleepMin.getLocation().y > za.getLocation().y
				&& blokjeSleepMin.getLocation().y + blokjeSleepMin.getSize().height < za.getLocation().y + za.getSize().height) { // plons.play();
			za.start(false, blokjeSleepMin.getLocation().x - za.getLocation().x);
			tc.verlaag();
			tm.tempMin();
			eindTemp.verlaag();
			if (!pasEruit)
				erinContainer.voegBlokjeToe(false);
			else
				eruitContainer.verwijderBlokje();
			blokjeSleepMin.setLocation((int) (blokjeMin.getLocation().x), (int) (blokjeMin.getLocation().y));
			raakSleepMin = false;
			pasEruit = false;
		}

		if (plusEruit && blokjeSleep.getLocation().y < za.getLocation().y) {
			plusEruit = false;
			pasEruit = true;
			// bubbel.play();
			tc.verlaag();
			tm.tempMin();
			eindTemp.verlaag();
			eruitContainer.voegBlokjeToe(true);
		}
		if (minEruit && blokjeSleepMin.getLocation().y < za.getLocation().y) {
			minEruit = false;
			pasEruit = true;
			// bubbel.play();
			tc.verhoog();
			tm.tempPlus();
			eindTemp.verhoog();
			eruitContainer.voegBlokjeToe(false);
		}
		laatstex = e.getX();
		laatstey = e.getY();
	}

	public void mouseReleased(MouseEvent e) {
		raakSleep = false;
		raakSleepMin = false;

		if (!plusEruit && blokjeSleep.getLocation().x + blokjeSleep.getSize().width < pot.getLocation().x + pot.getSize().width
				&& blokjeSleep.getLocation().x > pot.getLocation().x
				&& blokjeSleep.getLocation().y + blokjeSleep.getSize().height < za.getLocation().y + za.getSize().height) {
			int x = blokjeSleep.getLocation().x;
			blokjeSleep.setLocation((int) (blokjePlus.getLocation().x), (int) (blokjePlus.getLocation().y));
			// plons.play();
			za.start(true, x - za.getLocation().x);
			tc.verhoog();
			tm.tempPlus();
			eindTemp.verhoog();
			if (!pasEruit)
				erinContainer.voegBlokjeToe(true);
			else
				eruitContainer.verwijderBlokje();
		} else {
			blokjeSleep.setLocation((int) (blokjePlus.getLocation().x), (int) (blokjePlus.getLocation().y));
		}

		if (!minEruit && blokjeSleepMin.getLocation().x + blokjeSleepMin.getSize().width < pot.getLocation().x + pot.getSize().width
				&& blokjeSleepMin.getLocation().x > pot.getLocation().x
				&& blokjeSleepMin.getLocation().y + blokjeSleepMin.getSize().height < za.getLocation().y + za.getSize().height) {
			int x = blokjeSleepMin.getLocation().x;
			blokjeSleepMin.setLocation((int) (blokjeMin.getLocation().x), (int) (blokjeMin.getLocation().y));
			// plons.play();
			za.start(false, x - za.getLocation().x);

			tc.verlaag();
			tm.tempMin();
			eindTemp.verlaag();
			if (!pasEruit)
				erinContainer.voegBlokjeToe(false);
			else
				eruitContainer.verwijderBlokje();
		} else {
			blokjeSleepMin.setLocation((int) (blokjeMin.getLocation().x), (int) (blokjeMin.getLocation().y));
		}

		plusEruit = false;
		minEruit = false;
		pasEruit = false;
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
		;
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
		;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == opdrachtKnop) {
			beginLabel.setVisible(false);
			eindLabel.setVisible(false);
			erinLabel.setVisible(false);
			beginTemp.setVisible(false);
			eindTemp.setVisible(false);
			beginPot.setVisible(false);
			eindPot.setVisible(false);
			potErin.setVisible(false);
			erinContainer.setVisible(false);
			opdrachtKnop.setVisible(false);

			opdracht.setVisible(true);
			textArea.setVisible(true);
			opdrachtTitel.setVisible(true);
			werkKnop.setVisible(true);
		} else if (e.getSource() == werkKnop) {
			beginLabel.setVisible(true);
			eindLabel.setVisible(true);
			erinLabel.setVisible(true);
			beginTemp.setVisible(true);
			eindTemp.setVisible(true);
			beginPot.setVisible(true);
			eindPot.setVisible(true);
			potErin.setVisible(true);
			erinContainer.setVisible(true);
			opdrachtKnop.setVisible(true);

			opdracht.setVisible(false);
			textArea.setVisible(false);
			opdrachtTitel.setVisible(false);
			werkKnop.setVisible(false);

		} else {
			if (beginTemp.isBekend()) {
				eindTemp.zetBekend(true);
				tc.zetBekend(true);
				eindTemp.zetWaarde(beginTemp.geefWaarde());
				tc.zetWaarde(beginTemp.geefWaarde());
				tm.zetTemp(beginTemp.geefWaarde());
			} else {
				eindTemp.zetBekend(false);
				eindTemp.repaint();
				tc.zetBekend(false);
				tc.repaint();
				tm.zetTemp(0);
			}
			erinContainer.removeAll();
			erinContainer.repaint();
			eruitContainer.removeAll();
			eruitContainer.repaint();
		}
	}

}
