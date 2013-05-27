package fi.heks;

import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import fi.heks.scobjects.*;
import fi.beans.appletutil.*;
import fi.heks.vectortek.*;

public class BlokjesEruitPanel extends BlokjesErbijPanel {
	public BlokjesEruitPanel(int x, int y, int b, int h, Applet applet) {
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
		eindTemp.zetInstelbaar(true);
		uitkomst.zetInstelbaar(true);
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
			eindTemp.zetInstelbaar(true);
			uitkomst.zetInstelbaar(true);
		} else if (e.getSource() == helemaalOpnieuwKnop) {
			huidigeSom = new Som(Som.MIN);
			zetSom(huidigeSom);
			oefenTafereelPanel.zetBeginTemp(huidigeSom.geefTerm1());
			erinOefenContainer.removeAll();
			erinOefenLabel.setVisible(false);
			volgendeKnop.setVisible(false);
			eindTemp.zetInstelbaar(true);
			uitkomst.zetInstelbaar(true);
			aantalPunten = 0;
			score = 2 * aantalPunten;
			scoreLabel.setLabel("Score: " + Integer.toString(score));
			helemaalOpnieuwKnop.setVisible(false);
		} else if (e.getSource() == opnieuwKnop) {
			zetSom(huidigeSom);
			oefenTafereelPanel.zetBeginTemp(huidigeSom.geefTerm1());
			erinOefenContainer.removeAll();
			erinOefenLabel.setVisible(false);
			opnieuwKnop.setVisible(false);
			eindTemp.zetInstelbaar(true);
			uitkomst.zetInstelbaar(true);
		} else if (e.getSource() == eindTemp || e.getSource() == uitkomst) {
			if (e.getSource() == eindTemp) {
				int w = eindTemp.geefWaarde();
				if (w != -999) {
					uitkomst.zetWaarde(eindTemp.geefWaarde());
				}
			} else if (e.getSource() == uitkomst) {
				int w = uitkomst.geefWaarde();
				if (w != -999) {
					eindTemp.zetWaarde(uitkomst.geefWaarde());
				}
			} else
				return;

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

					if (score < 10)
						aantalPunten++;
					score = 2 * aantalPunten;
					scoreLabel.setLabel(Heks.rb.getString("scoreLabel") + Integer.toString(score));

					if (score < 10)
						volgendeKnop.setVisible(true);
					if (score == 10)
						helemaalOpnieuwKnop.setVisible(true);
				} else {
					goedFoutLabel.setForeground(new Color(255, 0, 0));
					goedFoutLabel.setLabel(Heks.rb.getString("foutLabel"));
					vraagteken.setVisible(false);
					opnieuwKnop.setVisible(true);

					if (aantalPunten > 0)
						aantalPunten--;
					score = 2 * aantalPunten;
					scoreLabel.setLabel(Heks.rb.getString("scoreLabel") + Integer.toString(score));
				}
				eindTemp.zetInstelbaar(false);
				uitkomst.zetInstelbaar(false);

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
