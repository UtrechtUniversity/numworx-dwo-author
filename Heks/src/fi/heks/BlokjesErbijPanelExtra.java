package fi.heks;

import java.awt.*;
import java.util.*;
import java.applet.*;
import java.awt.event.*;
import fi.heks.scobjects.*;
import fi.beans.appletutil.*;
import fi.heks.vectortek.*;

public class BlokjesErbijPanelExtra extends ScPanel implements ActionListener {
	AppletUtil au;

	Tekening potErin, potEruit, beginPot, eindPot, pijl, schrijfheks;
	GetalComponent beginTemp, eindTemp, term1, term2, uitkomst;
	BlokjesContainer erinContainer, erinOefenContainer;
	ScLWButton volgendeKnop, opnieuwKnop, helemaalOpnieuwKnop;
	ScLabel plusMinLabel, isLabel, goedFoutLabel, erinLabel, erinOefenLabel, wordtLabel, titelLabel;
	ScTekstContainer beginLabel, eindLabel, aantalBlLabel, uitleg;
	OefenTafereelPanel oefenTafereelPanel;
	ScContainer tabelContainer;
	KnipperComponent vraagteken;
	TabelComponent tabel;
	Som huidigeSom;
	ScLabel scoreLabel;
	int score;
	int aantalPunten;
	int somNr = 0;

	ScPanel afdekking;

	public BlokjesErbijPanelExtra(int x, int y, int b, int h, Applet applet) {
		super(x, y, b, h);
		// setBackground(new Color(255,255,220));
		setBackground(getBackground());
		zetVastePlaats(true);

		au = new AppletUtil(applet);

		volgendeKnop = new ScLWButton(80, 480, 160, 25, Heks.rb.getString("volgendeKnopLabel"));
		volgendeKnop.addActionListener(this);
		volgendeKnop.setVisible(false);
		add(volgendeKnop);

		opnieuwKnop = new ScLWButton(110, 400, 100, 25, Heks.rb.getString("opnieuwKnopLabel"));
		opnieuwKnop.addActionListener(this);
		add(opnieuwKnop);
		opnieuwKnop.setVisible(false);

		helemaalOpnieuwKnop = new ScLWButton(80, 480, 160, 25, Heks.rb.getString("helemaalOpnieuwKnopLabel"));
		helemaalOpnieuwKnop.addActionListener(this);
		add(helemaalOpnieuwKnop);
		helemaalOpnieuwKnop.setVisible(false);

		afdekking = new ScPanel(200, 0, 600, 203);
		add(afdekking, 0);

		erinOefenContainer = new BlokjesContainer(430, 350, 110, 85, applet);
		erinOefenContainer.toonAlsGetal(true);
		add(erinOefenContainer);

		schrijfheks = new Tekening(30, 70, 160, 160, au, "schrijfheks.gif");
		add(schrijfheks);

		goedFoutLabel = new ScLabel(110, 430, 800, 40, "");
		goedFoutLabel.lijnUit(ScLabel.LINKS);
		add(goedFoutLabel);

		titelLabel = new ScLabel(20, 20, 200, 40, "Blokjes er in");
		// add(titelLabel);

		tabelContainer = new ScContainer(230, 5, 550, 300);

		vraagteken = new KnipperComponent(470, 130, 40, 40, "?");
		// vraagteken.start();
		tabelContainer.add(vraagteken);

		beginTemp = new GetalComponent(30, 130, 60, 30);
		beginTemp.zetAlsTemp(true);
		tabelContainer.add(beginTemp);

		beginLabel = new ScTekstContainer(2, 30, 110, 25, 2, Heks.rb.getString("beginTempLabel"));
		tabelContainer.add(beginLabel);

		beginPot = new Tekening(10, 110, 90, 80, au, "potzwart.gif");
		tabelContainer.add(beginPot);

		term1 = new GetalComponent(30, 210, 60, 40);
		tabelContainer.add(term1);

		erinLabel = new ScLabel(135, 50, 60, 25, Heks.rb.getString("erinLabel"));
		tabelContainer.add(erinLabel);

		erinOefenLabel = new ScLabel(415, 310, 100, 35, Heks.rb.getString("erinOefenLabel"));
		erinOefenLabel.setVisible(false);
		add(erinOefenLabel);

		aantalBlLabel = new ScTekstContainer(220, 20, 110, 25, 3, Heks.rb.getString("aantalWBlokjesLabel"));
		tabelContainer.add(aantalBlLabel);

		// wordtLabel = new ScLabel(355,50,60,25,"wordt");
		// tabelContainer.add(wordtLabel);

		potErin = new Tekening(115, 105, 100, 90, au, "potErin.gif");
		tabelContainer.add(potErin);

		// pijl = new Tekening(335,105,100,90,au,"pijl.gif");
		// tabelContainer.add(pijl);

		plusMinLabel = new ScLabel(135, 210, 60, 40, "+");
		tabelContainer.add(plusMinLabel);

		eindTemp = new GetalComponent(460, 130, 60, 30);
		eindTemp.zetBekend(false);
		eindTemp.zetInstelbaar(false);
		eindTemp.zetLeeg(true);
		eindTemp.zetAlsTemp(true);
		eindTemp.addActionListener(this);
		tabelContainer.add(eindTemp);

		eindLabel = new ScTekstContainer(440, 30, 110, 25, 2, Heks.rb.getString("eindTempLabel"));
		tabelContainer.add(eindLabel);

		eindPot = new Tekening(445, 110, 90, 80, au, "potzwart.gif");
		tabelContainer.add(eindPot);

		uitkomst = new GetalComponent(465, 210, 60, 40);
		uitkomst.zetBekend(false);
		uitkomst.zetInstelbaar(true);
		uitkomst.addActionListener(this);
		tabelContainer.add(uitkomst);

		erinContainer = new BlokjesContainer(225, 107, 100, 85, applet);
		tabelContainer.add(erinContainer);

		term2 = new GetalComponent(245, 210, 60, 40);
		term2.addActionListener(this);
		tabelContainer.add(term2);

		isLabel = new ScLabel(355, 210, 60, 40, "=");
		tabelContainer.add(isLabel);

		tabel = new TabelComponent(3, 5, 0, 0, 550, 300, true);
		tabelContainer.add(tabel);

		add(tabelContainer);

		uitleg = new ScTekstContainer(45, 310, 400, 20, 3, Heks.rb.getString("blokjesErbijExtraUitleg"));

		uitleg.lijnUit(ScLabel.LINKS);
		add(uitleg);

		oefenTafereelPanel = new OefenTafereelPanel(540, 285, 240, 240, applet);
		oefenTafereelPanel.addActionListener(this);
		oefenTafereelPanel.zetEruitMogelijk(false);
		oefenTafereelPanel.setVisible(false);
		// add(oefenTafereelPanel);

		huidigeSom = new Som(Som.PLUS);
		zetSom(huidigeSom);
		oefenTafereelPanel.zetBeginTemp(huidigeSom.geefTerm1());

		scoreLabel = new ScLabel(30, 220, 150, 40, "test");
		scoreLabel.setLabel(Heks.rb.getString("scoreLabel") + Integer.toString(score));
		add(scoreLabel);

	}

	public void setState(Hashtable h) {
		int aantalPunten = ((Integer) h.get("aantalPunten")).intValue();

		this.aantalPunten = aantalPunten;
		score = 2 * aantalPunten;
		scoreLabel.setLabel(Heks.rb.getString("blokjesErbijExtraUitleg") + Integer.toString(score));
		if (score == 10) {
			helemaalOpnieuwKnop.setVisible(true);
			uitkomst.zetInstelbaar(false);
		}
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
		erinContainer.removeAll();
		huidigeSom = new Som(Som.PLUS);
		zetSom(huidigeSom);
		oefenTafereelPanel.zetBeginTemp(huidigeSom.geefTerm1());
		vraagteken.setVisible(true);
		eindTemp.zetInstelbaar(false);
		erinOefenContainer.removeAll();
		erinOefenLabel.setVisible(false);
		volgendeKnop.setVisible(false);
		opnieuwKnop.setVisible(false);
	}

	public void zetSom(Som som) {
		goedFoutLabel.setLabel("");
		beginTemp.zetWaarde(som.geefTerm1());

		if (somNr % 2 == 1) {
			erinContainer.removeAll();
			for (int i = 0; i < Math.abs(som.geefTerm2()); i++) {
				erinContainer.voegBlokjeToe(som.geefTerm2() > 0);
			}
			eindTemp.zetBekend(false);
			eindTemp.zetInstelbaar(true);
			term2.zetInstelbaar(false);
			eindTemp.repaint();
			term1.zetWaarde(som.geefTerm1());
			term2.zetWaarde(som.geefTerm2());
			String op = som.geefOperator();
			plusMinLabel.setLabel(op);
			if (term2.geefWaarde() > 0)
				aantalBlLabel.setText(Heks.rb.getString("warmeLabel"), 1);
			else
				aantalBlLabel.setText(Heks.rb.getString("koudeLabel"), 1);
			uitkomst.zetBekend(false);
			vraagteken.setVisible(true);
			uitkomst.repaint();
		} else {
			erinContainer.removeAll();
			// for(int i=0 ; i<Math.abs(som.geefTerm2()) ; i++)
			// { erinContainer.voegBlokjeToe(som.geefTerm2()>0);
			// }
			eindTemp.zetWaarde(som.geefUitkomst());
			eindTemp.zetInstelbaar(false);

			eindTemp.repaint();
			term1.zetWaarde(som.geefTerm1());
			uitkomst.zetWaarde(som.geefUitkomst());
			String op = som.geefOperator();
			plusMinLabel.setLabel(op);
			aantalBlLabel.setText(Heks.rb.getString("warmeKoudeLabel"), 1);
			term2.zetBekend(false);
			term2.zetInstelbaar(true);
			vraagteken.setVisible(true);
			uitkomst.repaint();
		}
		somNr++;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == volgendeKnop) {
			huidigeSom = new Som(Som.PLUS);
			zetSom(huidigeSom);
			oefenTafereelPanel.zetBeginTemp(huidigeSom.geefTerm1());
			erinOefenContainer.removeAll();
			erinOefenLabel.setVisible(false);
			volgendeKnop.setVisible(false);
			afdekking.setVisible(true);
			uitkomst.zetInstelbaar(true);
			// eindTemp.zetInstelbaar(true);
		} else if (e.getSource() == helemaalOpnieuwKnop) {
			huidigeSom = new Som(Som.PLUS);
			zetSom(huidigeSom);
			oefenTafereelPanel.zetBeginTemp(huidigeSom.geefTerm1());
			erinOefenContainer.removeAll();
			erinOefenLabel.setVisible(false);
			volgendeKnop.setVisible(false);
			afdekking.setVisible(true);
			uitkomst.zetInstelbaar(true);
			// eindTemp.zetInstelbaar(true);
			aantalPunten = 0;
			score = 2 * aantalPunten;
			scoreLabel.setLabel(Heks.rb.getString("scoreLabel") + Integer.toString(score));
			helemaalOpnieuwKnop.setVisible(false);
		} else if (e.getSource() == opnieuwKnop) {
			somNr--;
			zetSom(huidigeSom);
			oefenTafereelPanel.zetBeginTemp(huidigeSom.geefTerm1());
			erinOefenContainer.removeAll();
			erinOefenLabel.setVisible(false);
			opnieuwKnop.setVisible(false);
			if (somNr % 2 == 0)
				uitkomst.zetInstelbaar(true);
			if (somNr % 2 == 1)
				term2.zetInstelbaar(true);
			// eindTemp.zetInstelbaar(true);

		} else if (somNr % 2 == 0 && e.getSource() == uitkomst && uitkomst.isBekend()) {
			int w = uitkomst.geefWaarde();
			// if(w!=-999)
			// { uitkomst.zetWaarde(eindTemp.geefWaarde());
			// }
			// else return;

			if (e.getActionCommand().equals("focuslost")
			// || e.getActionCommand().equals("action")
			// &&
			// System.getProperties().getProperty("java.vendor").equals("Microsoft Corp."))
			) {
				boolean b = huidigeSom.evalueer(uitkomst.geefWaarde());
				if (b) {
					goedFoutLabel.setForeground(new Color(0, 150, 0));
					goedFoutLabel.setLabel(Heks.rb.getString("goedLabel"));
					vraagteken.setVisible(false);
					opnieuwKnop.setVisible(false);
					afdekking.setVisible(true);

					if (score < 10)
						aantalPunten++;
					score = 2 * aantalPunten;
					scoreLabel.setLabel(Heks.rb.getString("scoreLabel") + Integer.toString(score));

					if (score < 10)
						volgendeKnop.setVisible(true);
					if (score == 10) {
						helemaalOpnieuwKnop.setVisible(true);
						goedFoutLabel.setLabel(Heks.rb.getString("klaarFeedback"));
						uitkomst.zetInstelbaar(false);
					}
				} else {
					goedFoutLabel.setForeground(new Color(255, 0, 0));
					goedFoutLabel.setLabel(Heks.rb.getString("foutFeedbackBlokjes"));
					vraagteken.setVisible(false);
					opnieuwKnop.setVisible(true);
					afdekking.setVisible(false);

					if (aantalPunten > 0)
						aantalPunten--;
					score = 2 * aantalPunten;
					scoreLabel.setLabel(Heks.rb.getString("scoreLabel") + Integer.toString(score));
				}
				uitkomst.zetInstelbaar(false);

			}
		} else if (somNr % 2 == 1 && e.getSource() == term2 && term2.isBekend()) {
			int w = term2.geefWaarde();
			// if(w!=-999)
			// { uitkomst.zetWaarde(eindTemp.geefWaarde());
			// }
			// else return;

			if (e.getActionCommand().equals("focuslost")
			// || e.getActionCommand().equals("action")
			// &&
			// System.getProperties().getProperty("java.vendor").equals("Microsoft Corp."))
			) {
				boolean b = term2.geefWaarde() == huidigeSom.geefTerm2();
				if (b) {
					goedFoutLabel.setForeground(new Color(0, 150, 0));
					goedFoutLabel.setLabel(Heks.rb.getString("goedLabel"));
					vraagteken.setVisible(false);
					opnieuwKnop.setVisible(false);
					afdekking.setVisible(true);

					if (score < 10)
						aantalPunten++;
					score = 2 * aantalPunten;
					scoreLabel.setLabel(Heks.rb.getString("scoreLabel") + Integer.toString(score));

					if (score < 10)
						volgendeKnop.setVisible(true);
					if (score == 10) {
						helemaalOpnieuwKnop.setVisible(true);
						goedFoutLabel.setLabel(Heks.rb.getString("klaarFeedback"));
						term2.zetInstelbaar(false);
					}
				} else {
					goedFoutLabel.setForeground(new Color(255, 0, 0));
					goedFoutLabel.setLabel(Heks.rb.getString("foutFeedbackBlokjes"));
					vraagteken.setVisible(false);
					opnieuwKnop.setVisible(true);
					afdekking.setVisible(false);

					if (aantalPunten > 0)
						aantalPunten--;
					score = 2 * aantalPunten;
					scoreLabel.setLabel(Heks.rb.getString("scoreLabel") + Integer.toString(score));
				}
				term2.zetInstelbaar(false);

			}
		} else if (e.getSource() == oefenTafereelPanel) {
			erinOefenLabel.setVisible(true);
			opnieuwKnop.setVisible(true);
			if (e.getActionCommand().equals("plusErin")) {
				erinOefenContainer.voegBlokjeToe(true);
			}
			if (e.getActionCommand().equals("minErin")) {
				erinOefenContainer.voegBlokjeToe(false);
			}
		}
	}

}
