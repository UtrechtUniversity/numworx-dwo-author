package fi.heks;

import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import java.util.*;
import fi.heks.scobjects.*;
import fi.beans.appletutil.*;
import fi.heks.vectortek.*;

public class OefentoetsPanelExtra extends ScPanel implements ActionListener {
	AppletUtil au;

	Tekening goedkrul, foutkruis, schrijfheks;
	ScLWButton kijkNaKnop, opnieuwKnop;
	ScLabel titelLabel, scoreLabel, nakijkLabel;
	GetalComponent kerenNagekeken;
	ScTekstContainer uitleg;
	int aantalSommen;
	SomContainer[] sommen;
	int aantalPunten;
	double score;
	int soort;
	int aantalKerenNagekeken;

	public OefentoetsPanelExtra(int x, int y, int b, int h, Applet applet, int srt, boolean rechtsInv) {
		super(x, y, b, h);
		// setBackground(new Color(255,255,220));
		setBackground(getBackground());
		zetVastePlaats(true);
		soort = srt;

		au = new AppletUtil(applet);

		kijkNaKnop = new ScLWButton(570, 380, 140, 25, Heks.rb.getString("kijkNaKnopLabel"));
		kijkNaKnop.addActionListener(this);
		add(kijkNaKnop);

		opnieuwKnop = new ScLWButton(570, 430, 140, 25, Heks.rb.getString("nieuwToetsKnopLabel"));
		opnieuwKnop.addActionListener(this);
		add(opnieuwKnop);

		schrijfheks = new Tekening(510, 60, 250, 220, au, "schrijfheks.gif");
		add(schrijfheks);

		if (soort == Som.PLUSMIN)
			titelLabel = new ScLabel(20, 20, 200, 40, "Oefentoets 4");
		else if (soort == Som.MAAL && rechtsInv)
			titelLabel = new ScLabel(20, 20, 200, 40, "Oefentoets 5");
		else if (soort == Som.MAAL && !rechtsInv)
			titelLabel = new ScLabel(20, 20, 200, 40, "Oefentoets 6");
		// add(titelLabel);

		nakijkLabel = new ScLabel(530, 300, 170, 20, Heks.rb.getString("aantalNakijkLabel"));
		nakijkLabel.setVisible(false);
		add(nakijkLabel);

		kerenNagekeken = new GetalComponent(700, 300, 20, 20);
		if (Heks.rb.getLocale().toString().equals("en"))
			kerenNagekeken = new GetalComponent(600, 300, 20, 20);
		kerenNagekeken.zetWaarde(0);
		kerenNagekeken.setVisible(false);
		add(kerenNagekeken);

		scoreLabel = new ScLabel(530, 330, 220, 40, "");
		scoreLabel.setVisible(false);
		add(scoreLabel);

		uitleg = new ScTekstContainer(50, 40, 400, 20, 6, Heks.rb.getString("OefenToetsPanelExtraUitleg"));
		uitleg.lijnUit(ScLabel.LINKS);
		add(uitleg);

		aantalSommen = 20;
		sommen = new SomContainer[aantalSommen];
		for (int i = 0; i < 10; i++) {
			sommen[i] = new SomContainer(50, 230 + 25 * i, 200, 25, applet, soort, rechtsInv);
			sommen[i].addActionListener(this);
			add(sommen[i]);
		}
		for (int i = 10; i < aantalSommen; i++) {
			sommen[i] = new SomContainer(280, 230 + 25 * (i - 10), 200, 25, applet, soort, rechtsInv);
			sommen[i].addActionListener(this);
			add(sommen[i]);
		}

	}

	public void setState(Hashtable h) {
		int aantalSommen = ((Integer) h.get("aantalSommen")).intValue();
		boolean scoreVisible = ((Boolean) h.get("scoreVisible")).booleanValue();
		int aantalPunten = ((Integer) h.get("aantalPunten")).intValue();
		// int aantalKerenHulp = ((Integer)h.get("aantalKerenHulp")).intValue();
		int aantalKerenNagekeken = ((Integer) h.get("aantalKerenNagekeken")).intValue();
		Hashtable[] somStates = (Hashtable[]) h.get("somStates");

		this.aantalSommen = aantalSommen;
		scoreLabel.setVisible(scoreVisible);
		// oefenTafereelPanel.zetGebruikt(aantalKerenHulp);
		this.aantalPunten = aantalPunten;
		this.aantalKerenNagekeken = aantalKerenNagekeken;
		// kerenHulp.zetWaarde(oefenTafereelPanel.geefGebruikt());
		kerenNagekeken.zetWaarde(aantalKerenNagekeken);
		score = Math.max(0, 1.0 * (aantalPunten + 1 - aantalKerenNagekeken) / aantalSommen * 5);
		scoreLabel.setLabel("Score: " + Double.toString(score));
		scoreLabel.setVisible(scoreVisible);
		for (int i = 0; i < aantalSommen; i++) {
			sommen[i].setState(somStates[i]);
		}
		if (aantalKerenNagekeken > 0) {
			nakijkLabel.setVisible(true);
			kerenNagekeken.setVisible(true);
		}
	}

	public Hashtable getState() {
		int aantalSommen = 0;
		boolean scoreVisible = false;
		int aantalPunten = 0;
		// int aantalKerenHulp = 0;
		int aantalKerenNagekeken = 0;
		Hashtable[] somStates = null;

		aantalSommen = this.aantalSommen;
		aantalPunten = this.aantalPunten;
		// aantalKerenHulp = oefenTafereelPanel.geefGebruikt();
		somStates = new Hashtable[aantalSommen];
		aantalKerenNagekeken = this.aantalKerenNagekeken;
		scoreVisible = aantalKerenNagekeken > 0;
		for (int i = 0; i < aantalSommen; i++) {
			somStates[i] = sommen[i].getState();
		}

		Hashtable h = new Hashtable();
		h.put("aantalSommen", new Integer(aantalSommen));
		h.put("scoreVisible", new Boolean(scoreVisible));
		h.put("aantalPunten", new Integer(aantalPunten));
		// h.put("aantalKerenHulp", new Integer(aantalKerenHulp));
		h.put("aantalKerenNagekeken", new Integer(aantalKerenNagekeken));
		h.put("somStates", somStates);
		return h;
	}

	public double getScore() {
		return score * 10;
	}

	public void start() {
		int vorigeTerm1 = 0;
		for (int i = 0; i < aantalSommen; i++) {
			Som s = new Som(soort);
			while (s.geefTerm1() == vorigeTerm1) {
				s = new Som(soort);
			}
			vorigeTerm1 = s.geefTerm1();
			sommen[i].zetSom(s);
		}
		scoreLabel.setVisible(false);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == kijkNaKnop) {
			aantalKerenNagekeken++;
			requestFocus();
			aantalPunten = 0;
			for (int i = 0; i < aantalSommen; i++) {
				if (sommen[i].evalueer())
					aantalPunten = aantalPunten + 2;
			}
			score = Math.max(0, 1.0 * (aantalPunten + 1 - aantalKerenNagekeken) / aantalSommen * 5);
			scoreLabel.setLabel("Score: " + Double.toString(score));
			scoreLabel.setVisible(true);
		} else if (e.getSource() == opnieuwKnop) {
			aantalKerenNagekeken = 0;
			requestFocus();
			int vorigeTerm1 = 0;
			for (int i = 0; i < aantalSommen; i++) {
				Som s = new Som(soort);
				while (s.geefTerm1() == vorigeTerm1) {
					s = new Som(soort);
				}
				vorigeTerm1 = s.geefTerm1();
				sommen[i].zetSom(s);
			}
			scoreLabel.setVisible(false);
		}
		kerenNagekeken.zetWaarde(aantalKerenNagekeken);
		if (aantalKerenNagekeken > 0) {
			nakijkLabel.setVisible(true);
			kerenNagekeken.setVisible(true);
		} else {
			nakijkLabel.setVisible(false);
			kerenNagekeken.setVisible(false);
		}
	}
}
