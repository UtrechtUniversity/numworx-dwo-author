package fi.algebrapijlenopdr;

import java.awt.*;
import java.awt.event.*;

import fi.algebrapijlenopdr.expressies_ap.*;
import fi.algebrapijlenopdr.schuifobjects.*;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.lang.reflect.Constructor;

import javax.swing.*;

import fi.beans.appletutil.AppletUtil;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;

public class AlgebraPijlenOpdrInteractiePanel extends JPanel
	implements InteractiePanel, InteractieEditPanel, ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static int OEFENEN = 0;
	public static int OEFENEN_STRAFPUNTEN = 1;
	public static int ZELFTOETS = 2;
	public static int EINDTOETS = 3;

	AlgebraPijlenOpdr applet;
	ImageIcon goedkrulIcon, foutkruisIcon, halfkrulIcon;
	Image goedkrul, foutkruis, halfkrul;

	private AlgebraSchuifVeld algebraSchuifVeld;

	boolean kijkNaActief = false;
	boolean checkExternal = false;

	int score = 0;
	int scoreMax = 10;

	boolean ingevuld;
	private boolean nagekeken;
	private int mode;
	boolean correct = false;
	boolean fout = false;

	Vector docentExpressieStrings = new Vector();
	Vector docentExpressies = new Vector();

	Vector listeners = new Vector();

	public AlgebraPijlenOpdrInteractiePanel()
	{
		setLayout(null);
		// echte initiatie vind pas plaats na setBounds

		java.net.URL imageURL = AlgebraPijlenOpdr.class.getResource("resources/goedkrul.gif");
		if (imageURL != null)
		{
			goedkrulIcon = new ImageIcon(imageURL);
		}
		else
		{
			System.out.println("Error reading goedkrul.gif.");
		}
		imageURL = AlgebraPijlenOpdr.class.getResource("resources/foutkruis.gif");
		if (imageURL != null)
		{
			foutkruisIcon = new ImageIcon(imageURL);
		}
		else
		{
			System.out.println("Error reading foutkruis.");
		}
		imageURL = AlgebraPijlenOpdr.class.getResource("resources/goedkrulhalf.gif");
		if (imageURL != null)
		{
			halfkrulIcon = new ImageIcon(imageURL);
		}
		else
		{
			System.out.println("Error reading goedkrulhalf.");
		}

		goedkrul = goedkrulIcon.getImage();
		foutkruis = foutkruisIcon.getImage();
		halfkrul = halfkrulIcon.getImage();
	}

	public void zetOpdracht(Hashtable h, String[] randomVars, Hashtable randomValues)
	{
		if (h.containsKey("docentExpressieStrings"))
			docentExpressieStrings = (Vector) h.get("docentExpressieStrings");

		if (h.containsKey("kijkNaActief"))
			kijkNaActief = ((Boolean) h.get("kijkNaActief")).booleanValue();
		zetKijkNaActief(kijkNaActief);

		if (h.containsKey("checkExternal"))
			checkExternal = ((Boolean) h.get("checkExternal")).booleanValue();
		zetCheckExternal(checkExternal);

		if (h.containsKey("scoreMax"))
			scoreMax = ((Integer) h.get("scoreMax")).intValue();

		algebraSchuifVeld.setEditModeState(h);

		algebraSchuifVeld.changed = false;
		ingevuld = false;
	}

	public void setState(Hashtable h)
	{
		algebraSchuifVeld.setState(h);

		ingevuld = false;

		if (h.containsKey("nagekeken"))
			nagekeken = ((Boolean) h.get("nagekeken")).booleanValue();

		if (h.containsKey("ingevuld"))
			ingevuld = ((Boolean) h.get("ingevuld")).booleanValue();

		if (!ingevuld)
		{
			algebraSchuifVeld.changed = false;
		}

		if (ingevuld && (mode == OEFENEN || nagekeken))
		{
			kijkNa();
		}
	}

	public void setEditState(Hashtable h)
	{
		if (algebraSchuifVeld != null)
		{
			if (h.containsKey("docentExpressiesString"))
				docentExpressieStrings = (Vector) h.get("docentExpressieStrings");

			if (h.containsKey("kijkNaActief"))
				kijkNaActief = ((Boolean) h.get("kijkNaActief")).booleanValue();
			zetKijkNaActief(kijkNaActief);

			if (h.containsKey("checkExternal"))
				checkExternal = ((Boolean) h.get("checkExternal")).booleanValue();
			zetCheckExternal(checkExternal);

			if (h.containsKey("scoreMax"))
				scoreMax = ((Integer) h.get("scoreMax")).intValue();

			algebraSchuifVeld.setEditModeState(h);
		}
	}

	public Hashtable getState()
	{
		boolean nagekeken = false;
		boolean ingevuld = false;

		nagekeken = this.nagekeken;
		ingevuld = this.ingevuld;

		Hashtable h = algebraSchuifVeld.getState();
		h.put("nagekeken", new Boolean(nagekeken));
		h.put("ingevuld", new Boolean(ingevuld));

		return h;
	}

	public Hashtable getEditState()
	{
		Hashtable h = algebraSchuifVeld.getState();

		h.put("kijkNaActief", new Boolean(kijkNaActief));
		h.put("checkExternal", new Boolean(checkExternal));
		// h.put("scoreMax", new Integer(scoreMax));

		return h;
	}

	public void zetToolkit(boolean b)
	{
		algebraSchuifVeld.zetToolkit(b);
	}

	public void zetAlleenInvullen(boolean b)
	{
		algebraSchuifVeld.zetAlleenInvullen(b);
	}

	public void zetIsDemo(boolean b)
	{
		algebraSchuifVeld.zetIsDemo(b);
	}

	public void zetBrugklas(boolean b)
	{
		algebraSchuifVeld.zetBrugklas(b);
	}

	public void zetTerugHeen(boolean b)
	{
		algebraSchuifVeld.zetTerugHeen(b);
	}

	public void zetTabelOptie(boolean b)
	{
		algebraSchuifVeld.zetTabelOptie(b);
	}

	public void zetGrafiekOptie(boolean b)
	{
		algebraSchuifVeld.zetGrafiekOptie(b);
	}

	public void zetScrollOptie(boolean b)
	{
		algebraSchuifVeld.zetScrollOptie(b);
	}

	public void zetZoomOptie(boolean b)
	{
		algebraSchuifVeld.zetZoomOptie(b);
	}

	public void zetBeginExpressie(Expressie exp)
	{
		if (algebraSchuifVeld != null)
			algebraSchuifVeld.zetBeginExpressie(exp);
	}

	public void zetKijkNaActief(boolean b)
	{
		kijkNaActief = b;

		if (algebraSchuifVeld != null)
			algebraSchuifVeld.zetKijkNaActief(kijkNaActief);
	}

	public void zetCheckExternal(boolean b)
	{
		checkExternal = b;

		if (algebraSchuifVeld != null)
			algebraSchuifVeld.zetCheckExternal(checkExternal);
	}

	public InteractieEditPanel getEditPanel()
	{
		return new AlgebraPijlenOpdrInteractieEditPanel();
	}

	public void setBounds(int x, int y, int b, int h)
	{
		if (h == 1)
			return;

		super.setBounds(x, y, b, h);
		
		if (algebraSchuifVeld == null)
		{
			algebraSchuifVeld = new AlgebraSchuifVeld(0, 0, b, h, this);
			add(algebraSchuifVeld, 0);
			algebraSchuifVeld.zetPlaatjes(goedkrul, foutkruis, halfkrul);
			algebraSchuifVeld.kijkNaKnop.addActionListener(this);
		}
		else
		{
			algebraSchuifVeld.setSize(b, h);
			algebraSchuifVeld.kijkNaKnop.setLocation(110 + (getSize().width - 110 - 90) / 2, getSize().height - 30);
		}
		algebraSchuifVeld.start();

		algebraSchuifVeld.tekenOpnieuw();
	}

	public void zetBreedte(int b)
	{
		algebraSchuifVeld.setSize(b, algebraSchuifVeld.getSize().height);

		algebraSchuifVeld.tekenOpnieuw();
	}

	public void zetHoogte(int h)
	{
		algebraSchuifVeld.setSize(algebraSchuifVeld.getSize().width, h);

		algebraSchuifVeld.tekenOpnieuw();
	}

	public void disableElements(boolean b)
	{
		algebraSchuifVeld.disableElements(b);
	}

	public void wis()
	{
	}

	public void zetMaat()
	{
	}

	public int geefAsHoogte()
	{
		return 0;
	}

	public int getIpId()
	{
		return 0;
	}

	public String getIpExpString()
	{
		return null;
	}

	/**
	 * Retourneert true als in nakijk-modus en
	 * er nagekeken moet worden. 
	 * 
	 * @return
	 */
	boolean isNakijkModus()
	{
		return kijkNaActief || checkExternal;
	}
	
	public int getScore()
	{
		if (isNakijkModus())
			return score;
		else
			return 0;
	}

	public int getScoreMax()
	{
		if (isNakijkModus())
			return scoreMax;
		else
			return 0;
	}

	public boolean isCorrect()
	{
		if (isNakijkModus())
			return correct;
		else
			return true;
	}

	public boolean isFout()
	{
		if (isNakijkModus())
			return fout;
		else
			return false;
	}

	public void zetMode(int mode)
	{
		this.mode = mode;
		if (kijkNaActief) // zet het uit als mode niet oefenen of oefenen met strafpunten is
			zetKijkNaActief(mode == OEFENEN || mode == OEFENEN_STRAFPUNTEN);
		if (checkExternal) // zet het uit als mode niet oefenen of oefenen met strafpunten is
			zetCheckExternal(mode == OEFENEN || mode == OEFENEN_STRAFPUNTEN);
	}

	public void zetNagekeken(boolean b)
	{
		if (ingevuld)
			nagekeken = b;
	}

	public void stop()
	{
		// kijkNa();
	}

	public void start()
	{
		algebraSchuifVeld.tekenOpnieuw();
	}

	public void destroy()
	{
	}

	public void opnieuw()
	{
	}

	public void maakDocentExpressies()
	{
		docentExpressies.removeAllElements();
		for (int i = 0; i < docentExpressieStrings.size(); i++)
		{
			String text = (String) docentExpressieStrings.elementAt(i);
			String formuleText = "$f" + text + "@";
			Expressie exp = FormuleParser_ap.geefExpressie(formuleText);
			docentExpressies.addElement(exp);
		}

	}

	public void kijkNa()
	{
		if (!isNakijkModus())
			return;

		ingevuld = true;

		maakDocentExpressies();

		// geen opdracht, alles goed
		if (docentExpressies.size() == 0)
		{
			score = scoreMax;
			fireChangeEvent();
			return;
		}

		Vector leerlingExpressieUVS = algebraSchuifVeld.vindExpressieUVS();

		int hits = 0;

		// hier zijn er docent expressies
		for (int lCnt = 0; lCnt < leerlingExpressieUVS.size(); lCnt++)
		{
			UitvoerSchuifComponent uvs = (UitvoerSchuifComponent) leerlingExpressieUVS.elementAt(lCnt);
			Expressie llgExp = null;
			if (uvs.geefUitvoer(0) != null)
				llgExp = uvs.geefUitvoer(0);
			else if (uvs.geefVerborgenUitvoer(0) != null)
				llgExp = uvs.geefVerborgenUitvoer(0);

			String llgExpStr = llgExp.toString();
			String llgExpStrC = llgExpStr.replaceAll(uvs.geefBronDefaultVarnaam(), "x");
			llgExp = FormuleParser_ap.geefExpressie("$f" + llgExpStrC + "@");

			boolean correct = false;
			if (llgExp != null)
			{
				for (int dCnt = 0; dCnt < docentExpressies.size(); dCnt++)
				{
					Expressie docExp = (Expressie) docentExpressies.elementAt(dCnt);
					if (Algebra.isGelijkwaardig(docExp, llgExp))
					{
						hits++;
						correct = true;
					}
				}

				if (correct)
				{
					uvs.pijlUit[0].im = goedkrul;
				}
				else
				{
					uvs.pijlUit[0].im = foutkruis;
				}
			}
		}

		int scorePerExpressie = scoreMax / docentExpressies.size();
		if (hits == 0)
		{
			score = 0;
			correct = false;
			fout = true;
		}
		else if (hits == docentExpressies.size())
		{
			score = scoreMax;
			correct = true;
			fout = false;
		}
		else
		{
			score = hits * scorePerExpressie;
			correct = true;
			fout = false;
		}

		nagekeken = true;
		/*
		 * // leerlingExpressieUVS.size() - hits is aantal foute expressies if
		 * (leerlingExpressieUVS.size() >= docentExpressies.size()) score =
		 * Math.max(0, scoreMax - (leerlingExpressieUVS.size() - hits)); else
		 * score = Math.max(0, scoreMax - (docentExpressies.size() -
		 * leerlingExpressieUVS.size() - hits));
		 */

		algebraSchuifVeld.tekenOpnieuw();

		fireChangeEvent();
	}

	public void answerChanged()
	{
		if (isNakijkModus())
		{
			algebraSchuifVeld.changed = true;
			correct = false;
			fout = false;
			score = 0;
			nagekeken = false;
			ingevuld = true;
			fireChangeEvent();
		}
	}

	public void fireChangeEvent()
	{
		ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "changed");
		for (int lCnt = 0; lCnt < listeners.size(); lCnt++)
		{
			((ActionListener) listeners.elementAt(lCnt)).actionPerformed(event);
		}

	}

	public void kijkNa(int stapNr)
	{
		kijkNa();
	}

	public void addActionListener(ActionListener al)
	{
		listeners.addElement(al);
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == algebraSchuifVeld.kijkNaKnop)
		{
			// nagekeken = true;
			kijkNa();
		}
	}
}
