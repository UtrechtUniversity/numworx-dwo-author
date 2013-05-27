package fi.heks;

import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import fi.heks.scobjects.*;
import fi.beans.appletutil.*;
import fi.heks.vectortek.*;

public class BlokjesEruitPanelExtra extends BlokjesErbijPanelExtra {
	public BlokjesEruitPanelExtra(int x, int y, int b, int h, Applet applet) {
		super(x, y, b, h, applet);

		titelLabel.setLabel("Blokjes er uit");

		erinLabel.setLabel(Heks.rb.getString("eruitLabel"));
		erinOefenLabel.setLabel(Heks.rb.getString("eruitOefenLabel"));

		tabelContainer.remove(potErin);
		potEruit = new Tekening(115, 105, 100, 90, au, "potEruit.gif");
		tabelContainer.add(potEruit, 0);

		tabelContainer.remove(plusMinLabel);
		plusMinLabel = new ScLabel(135, 210, 60, 40, "-");
		tabelContainer.add(plusMinLabel, 0);

		erinContainer.removeAll();
		huidigeSom = new Som(Som.MIN);
		zetSom(huidigeSom);
		oefenTafereelPanel.zetBeginTemp(huidigeSom.geefTerm1());
		oefenTafereelPanel.zetEruitMogelijk(true);
		oefenTafereelPanel.zetErinMogelijk(false);
	}

	public void start() {
		erinContainer.removeAll();
		huidigeSom = new Som(Som.MIN);
		zetSom(huidigeSom);
		oefenTafereelPanel.zetBeginTemp(huidigeSom.geefTerm1());
		opnieuwKnop.setVisible(true);
		vraagteken.setVisible(true);
		eindTemp.zetInstelbaar(false);
		erinOefenContainer.removeAll();
		erinOefenLabel.setVisible(false);
		volgendeKnop.setVisible(false);
		opnieuwKnop.setVisible(false);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == volgendeKnop) {
			huidigeSom = new Som(Som.MIN);
			zetSom(huidigeSom);
			oefenTafereelPanel.zetBeginTemp(huidigeSom.geefTerm1());
			erinOefenContainer.removeAll();
			erinOefenLabel.setVisible(false);
			volgendeKnop.setVisible(false);
			afdekking.setVisible(true);
			uitkomst.zetInstelbaar(true);
			// eindTemp.zetInstelbaar(true);
		} else if (e.getSource() == helemaalOpnieuwKnop) {
			huidigeSom = new Som(Som.MIN);
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
		} else if (somNr % 2 == 0 && e.getSource() == uitkomst && uitkomst.isBekend()) {
			int w = uitkomst.geefWaarde();
			// if(w!=-999)
			// { uitkomst.zetWaarde(eindTemp.geefWaarde());
			// }
			// else return;

			if (e.getActionCommand().equals("focuslost")
			// || e.getActionCommand().equals("action")
			// &&
			// System.getProperties().getProperty("java.vendor").equals("Microsoft Corp.")
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
				eindTemp.zetInstelbaar(false);

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
			if (e.getActionCommand().equals("plusEruit")) {
				erinOefenContainer.voegBlokjeToe(true);
			} else if (e.getActionCommand().equals("minEruit")) {
				erinOefenContainer.voegBlokjeToe(false);
			} else if (e.getActionCommand().equals("plusErin")) {
				erinOefenContainer.verwijderBlokje();
			} else if (e.getActionCommand().equals("minErin")) {
				erinOefenContainer.verwijderBlokje();
			}
		}
	}

}
