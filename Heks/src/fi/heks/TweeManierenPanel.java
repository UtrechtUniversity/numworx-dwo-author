package fi.heks;

import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import java.util.*;
import fi.heks.scobjects.*;
import fi.beans.appletutil.*;
import fi.heks.vectortek.*;

public class TweeManierenPanel extends ScPanel implements ActionListener {
	AppletUtil au;

	ScLWButton volgendeKnop, opnieuwKnop, helemaalOpnieuwKnop;
	ScLabel titelLabel, opdrachtTitel, gewensteTempLabel, goedFoutLabel, erinOefenLabel, eruitOefenLabel;
	ScTekstContainer uitleg;
	BlokjesContainer erinOefenContainer, eruitOefenContainer;
	GetalComponent gewensteTemp;
	OefenTafereelPanel oefenTafereelPanel1, oefenTafereelPanel2;
	Tekening werkheks;
	// AudioClip klappen;

	int beginTemp, eindTemp;

	ScLabel scoreLabel;
	int score;
	int aantalPunten;

	public TweeManierenPanel(int x, int y, int b, int h, Applet applet) {
		super(x, y, b, h);
		// setBackground(new Color(255,255,220));
		setBackground(getBackground());
		zetVastePlaats(true);

		au = new AppletUtil(applet);
		// klappen = au.getAudioClip("resources/klappen.au");

		opnieuwKnop = new ScLWButton(360, 470, 120, 25, Heks.rb.getString("opnieuwKnopLabel"));
		opnieuwKnop.addActionListener(this);
		add(opnieuwKnop);

		helemaalOpnieuwKnop = new ScLWButton(360, 470, 160, 25, Heks.rb.getString("helemaalOpnieuwKnopLabel"));
		helemaalOpnieuwKnop.addActionListener(this);
		add(helemaalOpnieuwKnop);
		helemaalOpnieuwKnop.setVisible(false);

		volgendeKnop = new ScLWButton(340, 470, 160, 25, Heks.rb.getString("volgendeKnopLabel"));
		volgendeKnop.addActionListener(this);
		volgendeKnop.setVisible(false);
		add(volgendeKnop);

		goedFoutLabel = new ScLabel(350, 225, 100, 40, "");
		goedFoutLabel.setForeground(new Color(0, 150, 0));
		add(goedFoutLabel);

		werkheks = new Tekening(40, 250, 250, 250, au, "werkheks.gif");
		add(werkheks);

		titelLabel = new ScLabel(20, 20, 250, 40, "Twee manieren");
		// add(titelLabel);

		// gewensteTempLabel = new
		// ScLabel(60,230,300,30,"Gewenste temperatuur:");
		// add(gewensteTempLabel);

		gewensteTemp = new GetalComponent(160, 170, 80, 40);
		gewensteTemp.zetAlsTemp(true);
		add(gewensteTemp);

		uitleg = new ScTekstContainer(30, 60, 400, 20, 4, Heks.rb.getString("TweeManierenPanelUitleg"));

		uitleg.lijnUit(ScLabel.LINKS);
		add(uitleg);

		erinOefenLabel = new ScLabel(440, 85, 100, 35, Heks.rb.getString("erinGedaanLabel"));
		erinOefenLabel.setVisible(false);
		add(erinOefenLabel);

		eruitOefenLabel = new ScLabel(440, 345, 100, 35, Heks.rb.getString("eruitGehaaldLabel"));
		if (Heks.rb.getLocale().toString().equals("en"))
			eruitOefenLabel = new ScLabel(420, 345, 120, 35, Heks.rb.getString("eruitGehaaldLabel"));
		eruitOefenLabel.setVisible(false);
		add(eruitOefenLabel);

		erinOefenContainer = new BlokjesContainer(440, 120, 110, 85, applet);
		erinOefenContainer.toonAlsGetal(true);
		add(erinOefenContainer);

		eruitOefenContainer = new BlokjesContainer(440, 380, 110, 85, applet);
		eruitOefenContainer.toonAlsGetal(true);
		add(eruitOefenContainer);

		oefenTafereelPanel1 = new OefenTafereelPanel(540, 20, 240, 240, applet);
		oefenTafereelPanel1.zetEruitMogelijk(false);
		oefenTafereelPanel1.addActionListener(this);
		add(oefenTafereelPanel1);

		oefenTafereelPanel2 = new OefenTafereelPanel(540, 280, 240, 240, applet);
		oefenTafereelPanel2.zetErinMogelijk(false);
		oefenTafereelPanel2.addActionListener(this);
		add(oefenTafereelPanel2);
		// oefenTafereelPanel2.repaint();

		scoreLabel = new ScLabel(130, 470, 150, 40, "");
		scoreLabel.setLabel(Heks.rb.getString("scoreLabel") + Integer.toString(score));
		add(scoreLabel);

		zetTemps();

		opdrachtTitel = new ScLabel(20, 0, 200, 40, Heks.rb.getString("opdrachtTitelLabel"));
		add(opdrachtTitel);

	}

	public void setState(Hashtable h) {
		int aantalPunten = ((Integer) h.get("aantalPunten")).intValue();

		this.aantalPunten = aantalPunten;
		score = 2 * aantalPunten;
		scoreLabel.setLabel(Heks.rb.getString("scoreLabel") + Integer.toString(score));
		if (score == 10)
			helemaalOpnieuwKnop.setVisible(true);
	}

	public Hashtable getState() {
		int aantalPunten = 0;

		aantalPunten = this.aantalPunten;

		Hashtable h = new Hashtable();
		h.put("aantalPunten", new Integer(aantalPunten));

		return h;
	}

	public double getScore() {
		return (double) score * 10;
	}

	public void start() {
		zetTemps();
		goedFoutLabel.setLabel("");
		erinOefenLabel.setVisible(false);
		eruitOefenLabel.setVisible(false);
		erinOefenContainer.removeAll();
		eruitOefenContainer.removeAll();
	}

	public void controleer() {
		if (gewensteTemp.geefWaarde() == oefenTafereelPanel1.geefTemp() && gewensteTemp.geefWaarde() == oefenTafereelPanel2.geefTemp()) {
			goedFoutLabel.setLabel(Heks.rb.getString("goedLabel"));
			// klappen.play();
			opnieuwKnop.setVisible(false);
			aantalPunten++;
			score = 2 * aantalPunten;
			scoreLabel.setLabel(Heks.rb.getString("scoreLabel") + Integer.toString(score));
			if (score < 10) {
				volgendeKnop.setVisible(true);
			} else if (score == 10) {
				helemaalOpnieuwKnop.setVisible(true);
				goedFoutLabel.setLabel(Heks.rb.getString("klaarLabel"));
				erinOefenContainer.removeAll();
				eruitOefenContainer.removeAll();
				erinOefenLabel.setVisible(false);
				eruitOefenLabel.setVisible(false);
			}
		} else {
			goedFoutLabel.setLabel("");
		}
	}

	public void zetTemps() {
		beginTemp = (int) (-10 + 20 * Math.random());
		int teken;
		if (Math.random() > 0.5)
			teken = -1;
		else
			teken = 1;
		int stap = teken * (int) (2 + 5 * Math.random());
		eindTemp = beginTemp + stap;

		gewensteTemp.zetWaarde(eindTemp);
		oefenTafereelPanel1.zetBeginTemp(beginTemp);
		oefenTafereelPanel2.zetBeginTemp(beginTemp);

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == opnieuwKnop) { // zetTemps();
			goedFoutLabel.setLabel("");
			erinOefenLabel.setVisible(false);
			eruitOefenLabel.setVisible(false);
			erinOefenContainer.removeAll();
			eruitOefenContainer.removeAll();
			oefenTafereelPanel1.zetBeginTemp(beginTemp);
			oefenTafereelPanel2.zetBeginTemp(beginTemp);
		} else if (e.getSource() == helemaalOpnieuwKnop) {
			goedFoutLabel.setLabel("");
			zetTemps();
			erinOefenLabel.setVisible(false);
			eruitOefenLabel.setVisible(false);
			erinOefenContainer.removeAll();
			eruitOefenContainer.removeAll();
			oefenTafereelPanel1.zetBeginTemp(beginTemp);
			oefenTafereelPanel2.zetBeginTemp(beginTemp);
			aantalPunten = 0;
			score = 2 * aantalPunten;
			scoreLabel.setLabel(Heks.rb.getString("scoreLabel") + Integer.toString(score));
			helemaalOpnieuwKnop.setVisible(false);
		} else if (e.getSource() == volgendeKnop) {
			zetTemps();
			goedFoutLabel.setLabel("");
			erinOefenLabel.setVisible(false);
			eruitOefenLabel.setVisible(false);
			erinOefenContainer.removeAll();
			eruitOefenContainer.removeAll();
			volgendeKnop.setVisible(false);
			opnieuwKnop.setVisible(true);
		} else if (e.getSource() == oefenTafereelPanel1) {
			erinOefenLabel.setVisible(true);
			if (e.getActionCommand().equals("plusErin")) {
				erinOefenContainer.voegBlokjeToe(true);
			}
			if (e.getActionCommand().equals("minErin")) {
				erinOefenContainer.voegBlokjeToe(false);
			}
			controleer();
		} else if (e.getSource() == oefenTafereelPanel2) {
			eruitOefenLabel.setVisible(true);
			if (e.getActionCommand().equals("plusEruit")) {
				eruitOefenContainer.voegBlokjeToe(true);
			} else if (e.getActionCommand().equals("minEruit")) {
				eruitOefenContainer.voegBlokjeToe(false);
			} else if (e.getActionCommand().equals("plusErin")) {
				eruitOefenContainer.verwijderBlokje();
			} else if (e.getActionCommand().equals("minErin")) {
				eruitOefenContainer.verwijderBlokje();
			}
			controleer();
		}
	}
}
