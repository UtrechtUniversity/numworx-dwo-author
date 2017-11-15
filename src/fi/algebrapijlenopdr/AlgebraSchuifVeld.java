package fi.algebrapijlenopdr;

import java.awt.*;
import java.awt.event.*;

//import fi.beans.appletutil.AppletUtil;
import fi.algebrapijlenopdr.schuifobjects.*;
import fi.algebrapijlenopdr.expressies_ap.*;
import fi.beans.base64code.StringCodeObject;

import javax.swing.*;

//import java.util.Enumeration;
import java.util.*;
import java.util.List;
import java.lang.reflect.Constructor;

public class AlgebraSchuifVeld extends SchuifVeld implements // ItemListener,
	MouseListener, MouseMotionListener, ActionListener
{
	private Image GOEDKRUL, FOUTKRUIS, GOEDKRULHALF;
	private JButton wisKnop, heenKnop, terugKnop;
	InvulPanel ip;
	JCheckBox grafiekCheckbox, tabelCheckbox;
	AlgebraSchuifComponent[] schuifcomponenten;
	GrafiekComponent grafiekComponent;
	int aantalSc;
	JButton kijkNaKnop;

	private boolean selecterenMogelijk;
	private boolean selecterenBezig;
	private boolean selectieGemaakt;
	private Rectangle clip;

	private JPopupMenu popup;
	JMenuItem copyItem;
	private Frame clipFrame;
	boolean links = false;

	private Hashtable editmodeState;

	boolean fixed = false;

	boolean frozen = false;

	private Panel hidePanel;

	public ZoomStateHolder zoomStateHolder;
	private Font font;

	private boolean buttonsAdded;

	boolean toolkit = true;
	boolean alleenInvullen = false;
	boolean isDemo = false;

	boolean brugklas = false;
	boolean terugHeen = true;
	boolean tabelOptie = true;
	boolean grafiekOptie = true;

	boolean scrollOptie = true;
	boolean zoomOptie = true;

	AlgebraPijlenOpdrInteractiePanel apip;

	public AlgebraSchuifVeld(int x, int y, int b, int h, AlgebraPijlenOpdrInteractiePanel apip)
	{
		super(x, y, b, h);

		this.apip = apip;

		addMouseListener(this);
		addMouseMotionListener(this);

		font = new Font("SansSerif", Font.PLAIN, 12);
		if (AlgebraPijlenOpdr.rb.getLocale().toString().equals("en"))
			font = new Font("SansSerif", Font.PLAIN, 11);

		zoomStateHolder = new ZoomStateHolder(this);

		ip = new InvulPanel(this, 8, 399, 90, 45);
		ip.setBackground(Color.lightGray);
		ip.setVisible(false);
		add(ip);

		grafiekCheckbox = new JCheckBox(AlgebraPijlenOpdr.rb.getString("grafiekLabel"));
		grafiekCheckbox.addActionListener(this);
		grafiekCheckbox.setFont(font);
		grafiekCheckbox.setBounds(8, 320, 100, 20);
		grafiekCheckbox.setBackground(new Color(210, 210, 210));
		add(grafiekCheckbox, 0);

		tabelCheckbox = new JCheckBox(AlgebraPijlenOpdr.rb.getString("tabelLabel"));
		tabelCheckbox.addActionListener(this);
		tabelCheckbox.setFont(font);
		tabelCheckbox.setBounds(8, 300, 80, 20);
		tabelCheckbox.setBackground(new Color(210, 210, 210));
		add(tabelCheckbox, 0);

		wisKnop = new JButton(AlgebraPijlenOpdr.rb.getString("wisKnopLabel"));
		wisKnop.setFont(font);
		wisKnop.setBounds(18, 350, 70, 20);
		wisKnop.addActionListener(this);
		add(wisKnop, 0);

		kijkNaKnop = new JButton(AlgebraPijlenOpdr.rb.getString("kijkNaTekst"));
		kijkNaKnop.setFont(font);
		kijkNaKnop.setBounds(110 + (getSize().width - 110 - 90) / 2, getSize().height - 30, 90, 20);
		// kijkNaKnop.addActionListener(this);
		kijkNaKnop.setVisible(false);
		add(kijkNaKnop, 0);

		terugKnop = new JButton(AlgebraPijlenOpdr.rb.getString("terugKnopLabel"));
		terugKnop.setBounds(10, 270, 86, 20);
		terugKnop.setFont(font);
		terugKnop.addActionListener(this);
		add(terugKnop, 0);

		heenKnop = new JButton(AlgebraPijlenOpdr.rb.getString("heenKnopLabel"));
		heenKnop.setBounds(10, 320, 86, 20);
		heenKnop.setFont(font);
		heenKnop.addActionListener(this);
		// add(heenKnop, 0);

		maakStapel();

		grafiekComponent = new GrafiekComponent(this, 400, 200, 200, 130);
		grafiekComponent.isStapel = false;

		popup = new JPopupMenu();

		copyItem = new JMenuItem(AlgebraPijlenOpdr.rb.getString("kopieerTekst"));
		copyItem.addActionListener(this);
		popup.add(copyItem);
		if (veldIsLeeg())
			copyItem.setEnabled(false);

		popup.addSeparator();

		JMenuItem mi = new JMenuItem(AlgebraPijlenOpdr.rb.getString("plakTekst"));
		mi.addActionListener(this);
		popup.add(mi);
		if ((AlgebraPijlenOpdr.clipBoard == null) || AlgebraPijlenOpdr.clipBoard.equals(""))
			mi.setEnabled(false);

		add(popup);

		hidePanel = new Panel();
		hidePanel.setBackground(Color.white);
		hidePanel.setBounds(1, 1, 110, h - 2);
		hidePanel.setVisible(false);
		add(hidePanel, 0);

		layoutKnoppen();
	}

	public void layoutKnoppen()
	{
		int currentY = 270;
		if (brugklas)
			currentY = 200;
		if (terugHeen)
		{
			terugKnop.setLocation(terugKnop.getLocation().x, currentY);
			currentY += 30;
		}
		if (tabelOptie)
		{
			tabelCheckbox.setLocation(tabelCheckbox.getLocation().x, currentY);
			currentY += 25;
		}
		if (grafiekOptie)
		{
			grafiekCheckbox.setLocation(grafiekCheckbox.getLocation().x, currentY);
			currentY += 30;
		}
		wisKnop.setLocation(wisKnop.getLocation().x, currentY);
	}

	public void disableElements(boolean b)
	{
		terugKnop.setEnabled(!b);
		tabelCheckbox.setEnabled(!b);
		grafiekCheckbox.setEnabled(!b);
		wisKnop.setEnabled(!b);
		kijkNaKnop.setEnabled(!b);
		frozen = b;
	}

	public boolean isEindUVS(UitvoerSchuifComponent uvs)
	{
		if (uvs.isStapel)
			return false;
		if ((uvs.geefUitvoer(0) == null) && (uvs.geefVerborgenUitvoer(0) == null))
			return false;
		// dit gebeurt niet?
		if (uvs.pijlUit[0] == null)
			return true;
		boolean einde = true;
		for (int pCnt = 0; pCnt < uvs.pijlUit.length; pCnt++)
		{
			if ((uvs.pijlUit[pCnt] != null) && uvs.pijlUit[pCnt].vast
				&& !(uvs.pijlUit[pCnt].ontvanger instanceof GrafiekComponent))
				einde = false;

		}
		return einde;
	}

	public Vector vindExpressieUVS()
	{
		Vector result = new Vector();

		for (int sCnt = 0; sCnt < aantalSc; sCnt++)
		{
			if (schuifcomponenten[sCnt] instanceof UitvoerSchuifComponent)
			{

				UitvoerSchuifComponent uvs = (UitvoerSchuifComponent) schuifcomponenten[sCnt];
				if (// (uvs.pijlIn1 != null) &&
					// !uvs.isStapel &&
					// ((uvs.pijlUit[0] == null) ||
					// (!uvs.pijlUit[0].actief && (!uvs.pijlUit[0].vast ||
					// (uvs.pijlUit[0].vast && (uvs.pijlUit[0].ontvanger
					// instanceof GrafiekComponent)))))
				isEindUVS(uvs))
				{
					// System.out.println("pijlen OK");

					result.addElement(uvs);
				}

			}
		}

		return result;
	}

	public void zetBeginExpressie(Expressie exp)
	{
		// kijk of er al een beginExpUVS is
		UitvoerSchuifComponent beginExpUVS = null;
		for (int cnt = 0; cnt < aantalSc; cnt++)
		{
			if (schuifcomponenten[cnt] instanceof UitvoerSchuifComponent
				&& ((UitvoerSchuifComponent) schuifcomponenten[cnt]).isBeginExpressie)
			{
				beginExpUVS = (UitvoerSchuifComponent) schuifcomponenten[cnt];
			}

		}

		if (beginExpUVS != null)
		{
			beginExpUVS.zetExpressie(exp);
		}
		else
		{
			beginExpUVS = new UitvoerSchuifComponent(this, 130, 45, 50, 20);
			beginExpUVS.zetTabelAan(tabelCheckbox.isSelected());
			beginExpUVS.zetScroll(true);
			beginExpUVS.isBeginExpressie = true;
			beginExpUVS.isStapel = false;
			beginExpUVS.zetExpressie(exp);

			schuifcomponenten[aantalSc] = beginExpUVS;
			schuifcomponenten[aantalSc].zetLinks(links);
			Pijl p = new Pijl(this);
			p.zetLinks(links);
			schuifcomponenten[aantalSc].voegPijlToe(p);
			add(schuifcomponenten[aantalSc]);
			aantalSc++;
		}

		tekenOpnieuw();

	}

	public boolean veldIsLeeg()
	{
		int veldCnt = 0;
		for (int vCnt = 0; vCnt < aantalSc; vCnt++)
		{
			if (!schuifcomponenten[vCnt].isStapel && !(schuifcomponenten[vCnt] instanceof GrafiekComponent))
			{
				veldCnt++;
			}
		}

		return (veldCnt == 0);
	}

	public int getAantalVeldSc()
	{
		int veldCnt = 0;
		for (int vCnt = 0; vCnt < aantalSc; vCnt++)
		{
			if (!schuifcomponenten[vCnt].isStapel)
			{
				veldCnt++;
			}
		}

		return veldCnt;
	}

	public void maakVeldLeeg()
	{
		for (int vCnt = (aantalSc - 1); vCnt >= 0; vCnt--)
		{
			if (!schuifcomponenten[vCnt].isStapel)
				verwijder(schuifcomponenten[vCnt]);
		}
	}

	public void zetPlaatjes(Image gk, Image fk, Image kh)
	{
		GOEDKRUL = gk;
		GOEDKRULHALF = kh;
		FOUTKRUIS = fk;
	}

	public void setFixed(boolean b)
	{
		fixed = b;
		hidePanel.setVisible(b);
		terugKnop.setVisible(!b);
		heenKnop.setVisible(!b);
		wisKnop.setVisible(!b);
		tabelCheckbox.setVisible(!b);
		grafiekCheckbox.setVisible(!b);
	}

	public void zetToolkit(boolean b)
	{
		toolkit = b;
		if (toolkit)
		{
			alleenInvullen = false;
			isDemo = false;

			if (getLocation().x < 0)
			{
				// setBounds(0, 0, getSize().width - 110, getSize().height);
				setLocation(0, 0);
				setSize(getSize().width - 110, getSize().height);
			}

			/*
			 * if (!buttonsAdded) {
			 * grafiekCheckbox.setLocation(grafiekCheckbox.getLocation().x +
			 * getLocation().x , grafiekCheckbox.getLocation().y +
			 * getLocation().y);
			 * tabelCheckbox.setLocation(tabelCheckbox.getLocation().x +
			 * getLocation().x , tabelCheckbox.getLocation().y +
			 * getLocation().y); wisKnop.setLocation(wisKnop.getLocation().x +
			 * getLocation().x , wisKnop.getLocation().y + getLocation().y);
			 * terugKnop.setLocation(terugKnop.getLocation().x + getLocation().x
			 * , terugKnop.getLocation().y + getLocation().y);
			 * heenKnop.setLocation(heenKnop.getLocation().x + getLocation().x ,
			 * heenKnop.getLocation().y + getLocation().y);
			 * ip.setLocation(ip.getLocation().x + getLocation().x ,
			 * ip.getLocation().y + getLocation().y);
			 * 
			 * getParent().add(grafiekCheckbox, 0);
			 * getParent().add(tabelCheckbox, 0); getParent().add(wisKnop, 0);
			 * getParent().add(terugKnop, 0); getParent().add(heenKnop, 0);
			 * getParent().add(ip, 0); buttonsAdded = true; }
			 */
			terugKnop.setVisible(terugHeen);
			heenKnop.setVisible(terugHeen);
			grafiekCheckbox.setVisible(grafiekOptie);
			tabelCheckbox.setVisible(tabelOptie);
			wisKnop.setVisible(true);

			// zetVeranderd();
			tekenOpnieuw();
		}
	}

	public void zetAlleenInvullen(boolean b)
	{
		alleenInvullen = b;
		if (alleenInvullen)
		{
			toolkit = false;
			isDemo = false;

			if (getLocation().x == 0)
			{
				// setBounds(- 110, 0, getSize().width + 110, getSize().height);
				setLocation(-110, 0);
				setSize(getSize().width + 110, getSize().height);
			}

			terugKnop.setVisible(false);
			heenKnop.setVisible(false);
			grafiekCheckbox.setVisible(false);
			tabelCheckbox.setVisible(false);
			wisKnop.setVisible(false);

			// zetVeranderd();
			tekenOpnieuw();
		}
	}

	public void zetIsDemo(boolean b)
	{
		isDemo = b;
		if (isDemo)
		{
			toolkit = false;
			alleenInvullen = false;

			if (getLocation().x == 0)
			{
				setLocation(-110, 0);
				setSize(getSize().width + 110, getSize().height);
			}

			terugKnop.setVisible(false);
			heenKnop.setVisible(false);
			grafiekCheckbox.setVisible(false);
			tabelCheckbox.setVisible(false);
			wisKnop.setVisible(false);

			// zetVeranderd();
			tekenOpnieuw();
		}
	}

	// als b==true, wat te doen met kettingen die niet-brugklas dingen bevatten?
	// voorlopig maar even niets
	public void zetBrugklas(boolean b)
	{
		brugklas = b;
		for (int i = 0; i < aantalSc; i++)
		{
			if ((schuifcomponenten[i].isStapel) && ((schuifcomponenten[i] instanceof OmkeringSchuifComponent)
				|| (schuifcomponenten[i] instanceof WortelSchuifComponent)
				|| (schuifcomponenten[i] instanceof MachtSchuifComponent)))
			{
				schuifcomponenten[i].setVisible(!b);
				for (int pCnt = 0; pCnt < schuifcomponenten[i].aantalPu; pCnt++)
				{
					if (schuifcomponenten[i].pijlUit[pCnt] != null)
						schuifcomponenten[i].pijlUit[pCnt].setVisible(!b);
				}
			}
		}

		layoutKnoppen();
		tekenOpnieuw();
	}

	public void zetTerugHeen(boolean b)
	{
		terugHeen = b;
		terugKnop.setVisible(b);
		heenKnop.setVisible(b);
		layoutKnoppen();
		tekenOpnieuw();
	}

	public void zetTabelOptie(boolean b)
	{
		tabelOptie = b;
		tabelCheckbox.setVisible(b);
		layoutKnoppen();
		tekenOpnieuw();
	}

	public void zetGrafiekOptie(boolean b)
	{
		grafiekOptie = b;
		grafiekCheckbox.setVisible(b);
		layoutKnoppen();
		tekenOpnieuw();
	}

	public void zetScrollOptie(boolean b)
	{
		scrollOptie = b;

		for (int i = 0; i < aantalSc; i++)
		{
			if (schuifcomponenten[i] instanceof UitvoerSchuifComponent)
			{
				((UitvoerSchuifComponent) schuifcomponenten[i]).zetScroll(scrollOptie);
			}
		}
	}

	public void zetZoomOptie(boolean b)
	{
		zoomOptie = b;

		for (int i = 0; i < aantalSc; i++)
		{
			if (schuifcomponenten[i] instanceof UitvoerSchuifComponent)
			{
				((UitvoerSchuifComponent) schuifcomponenten[i]).zetZoomInTabel(zoomOptie);
			}
		}

		tekenOpnieuw();
	}

	public void zetKijkNaActief(boolean b)
	{
		kijkNaKnop.setVisible(b && !apip.checkExternal);
	}
	
	/**
	 * Als extern controleren, dan moet de nakijkknop
	 * verborgen worden.
	 * 
	 * @param checkExternal
	 */
	public void zetCheckExternal(boolean checkExternal)
	{
		if (checkExternal)
			kijkNaKnop.setVisible(false);
	}

	public Hashtable<String, Object> getState()
	{
		int aantalSc = 0;
		String[] classNames = null;
		List<String> classNamesList = new ArrayList<String>();
		int[] posX = null;
		int[] posY = null;
		List<Integer> posXList = new ArrayList<Integer>();
		List<Integer> posYList = new ArrayList<Integer>();
		Hashtable<String, Object>[] scStates = null;
		List<Map<String, Object>> scStatesList = new ArrayList<Map<String, Object>>();
		boolean[][] connections = null;
		int[] graphConnections = null;
		List<Boolean> connectionsList = new ArrayList<Boolean>();
		List<Integer> graphConnectionsList = new ArrayList<Integer>();
		boolean tabel = false;
		boolean grafiek = false;
		boolean expressie = false;
		boolean links = false;
		Hashtable zoomStateHolderState = null;

		aantalSc = this.aantalSc;
		classNames = new String[aantalSc];
		posX = new int[aantalSc];
		posY = new int[aantalSc];
		scStates = new Hashtable[aantalSc];
		for (int i = 0; i < aantalSc; i++)
		{
			classNames[i] = schuifcomponenten[i].getClass().getName();
			classNamesList.add(classNames[i]);
			posX[i] = schuifcomponenten[i].getLocation().x;
			posXList.add(new Integer(posX[i]));
			posY[i] = schuifcomponenten[i].getLocation().y;
			posYList.add(new Integer(posY[i]));
			scStates[i] = schuifcomponenten[i].getState();
			scStatesList.add(scStates[i]);
		}
		connections = new boolean[aantalSc][aantalSc];
		for (int i = 0; i < aantalSc; i++)
		{
			for (int j = 0; j < aantalSc; j++)
			{
				connections[i][j] = schuifcomponenten[j].pijlIn1 != null
					&& schuifcomponenten[j].pijlIn1.zender == schuifcomponenten[i];
				connectionsList.add(new Boolean(connections[i][j]));
			}
		}

		tabel = tabelCheckbox.isSelected();
		grafiek = grafiekCheckbox.isSelected();

		expressie = ip.isExpr();
		links = this.links;
		zoomStateHolderState = zoomStateHolder.getState();

		graphConnections = new int[10];
		for (int i = 0; i < 10; i++)
		{
			graphConnections[i] = -1;
			graphConnectionsList.add(new Integer(-1));
		}
		if (grafiek)
		{
			for (int i = 0; i < 10; i++)
			{
				Pijl p = grafiekComponent.pijlenIn[i];
				for (int j = 0; j < aantalSc; j++)
				{
					if (p != null && schuifcomponenten[j] == p.zender)
					{
						graphConnections[i] = j;
						graphConnectionsList.set(i, new Integer(j));
					}
				}
			}
		}

		Hashtable h = new Hashtable();
		h.put("aantalSc", new Integer(aantalSc));
		h.put("classNames", classNames);
		h.put("classNamesList", classNamesList);
		h.put("posX", posX);
		h.put("posXList", posXList);
		h.put("posY", posY);
		h.put("posYList", posYList);
		h.put("scStates", scStates);
		h.put("scStatesList", scStatesList);
		h.put("connections", connections);
		h.put("connectionsList", connectionsList);
		h.put("graphConnections", graphConnections);
		h.put("graphConnectionsList", graphConnectionsList);
		h.put("tabel", new Boolean(tabel));
		h.put("grafiek", new Boolean(grafiek));
		h.put("expressie", new Boolean(expressie));
		h.put("links", new Boolean(links));
		h.put("zoomStateHolderState", zoomStateHolderState);

		h.put("toolkit", new Boolean(toolkit));
		h.put("alleenInvullen", new Boolean(alleenInvullen));
		h.put("isDemo", new Boolean(isDemo));

		h.put("brugklas", new Boolean(brugklas));
		h.put("terugHeen", new Boolean(terugHeen));
		h.put("tabelOptie", new Boolean(tabelOptie));
		h.put("grafiekOptie", new Boolean(grafiekOptie));

		h.put("scrollOptie", new Boolean(scrollOptie));
		h.put("zoomOptie", new Boolean(zoomOptie));

		return h;
	}

	public void copy()
	{
		Hashtable h = getCopyTable();
		String s = StringCodeObject.encodeObjectToString(h);
		AlgebraPijlenOpdr.clipBoard = s;
	}

	public Hashtable getCopyTable()
	{
		int aantalSc = 0;
		String[] classNames = null;
		int[] posX = null;
		int[] posY = null;
		Hashtable[] scStates = null;
		boolean[][] connections = null;
		int[] graphConnections = null;
		boolean tabel = false;
		boolean grafiek = false;
		Hashtable zoomStateHolderState = null;

		aantalSc = this.aantalSc;
		classNames = new String[aantalSc];
		posX = new int[aantalSc];
		posY = new int[aantalSc];
		scStates = new Hashtable[aantalSc];
		for (int i = 0; i < aantalSc; i++)
		{
			classNames[i] = schuifcomponenten[i].getClass().getName();
			posX[i] = schuifcomponenten[i].getLocation().x;
			posY[i] = schuifcomponenten[i].getLocation().y;
			scStates[i] = schuifcomponenten[i].getState();
		}

		connections = new boolean[aantalSc][aantalSc];
		for (int i = 0; i < aantalSc; i++)
		{
			for (int j = 0; j < aantalSc; j++)
			{
				connections[i][j] = schuifcomponenten[j].pijlIn1 != null
					&& schuifcomponenten[j].pijlIn1.zender == schuifcomponenten[i];
			}

		}

		tabel = tabelCheckbox.isSelected();
		grafiek = grafiekCheckbox.isSelected();

		zoomStateHolderState = zoomStateHolder.getState();

		graphConnections = new int[10];
		for (int i = 0; i < 10; i++)
		{
			graphConnections[i] = -1;
		}
		if (grafiek)
		{
			for (int i = 0; i < 10; i++)
			{
				Pijl p = grafiekComponent.pijlenIn[i];
				for (int j = 0; j < aantalSc; j++)
				{
					if (p != null && schuifcomponenten[j] == p.zender)
						graphConnections[i] = j;
				}
			}
		}

		Hashtable h = new Hashtable();
		h.put("aantalSc", new Integer(aantalSc));
		h.put("classNames", classNames);
		h.put("posX", posX);
		h.put("posY", posY);
		h.put("scStates", scStates);
		h.put("connections", connections);
		h.put("graphConnections", graphConnections);
		h.put("tabel", new Boolean(tabel));
		h.put("grafiek", new Boolean(grafiek));
		h.put("zoomStateHolderState", zoomStateHolderState);

		return h;
	}

	public void setEditModeState(Hashtable h)
	{
		editmodeState = h;
		setState(h);
	}

	public void setState(Hashtable h)
	{
		boolean toolkit = true;
		if ((h != null) && h.containsKey("toolkit"))
			toolkit = ((Boolean) h.get("toolkit")).booleanValue();
		this.toolkit = toolkit;

		boolean alleenInvullen = false;
		if ((h != null) && h.containsKey("alleenInvullen"))
			alleenInvullen = ((Boolean) h.get("alleenInvullen")).booleanValue();
		this.alleenInvullen = alleenInvullen;

		boolean isDemo = false;
		if ((h != null) && h.containsKey("isDemo"))
			isDemo = ((Boolean) h.get("isDemo")).booleanValue();
		this.isDemo = isDemo;

		int aantalSc = 0;
		String[] classNames = null;
		int[] posX = null;
		int[] posY = null;
		Hashtable[] scStates = null;
		boolean[][] connections = null;
		int[] graphConnections = null;
		boolean tabel = false;
		boolean grafiek = false;
		boolean expressie = false;
		boolean links = false;
		Hashtable zoomStateHolderState = null;

		try
		{
			aantalSc = ((Number) h.get("aantalSc")).intValue();
			if (h.containsKey("classNamesList"))
				classNames = toStringArray(h.get("classNamesList"));
			else
				classNames = (String[]) h.get("classNames");
			if (h.containsKey("posXList"))
				posX = toIntArray(h.get("posXList"));
			else
				posX = (int[]) h.get("posX");
			if (h.containsKey("posYList"))
				posY = toIntArray(h.get("posYList"));
			else
				posY = (int[]) h.get("posY");
			if (h.containsKey("scStatesList"))
				scStates = toHashtableArray(h.get("scStatesList"));
			else
				scStates = (Hashtable[]) h.get("scStates");
			if (h.containsKey("connectionsList"))
				connections = toBooleanArrayArray(h.get("connectionsList"), aantalSc);
			else
				connections = (boolean[][]) h.get("connections");
			if (h.containsKey("graphConnectionsList"))
				graphConnections = toIntArray(h.get("graphConnections"));
			else
				graphConnections = (int[]) h.get("graphConnections");
			tabel = ((Boolean) h.get("tabel")).booleanValue();
			grafiek = ((Boolean) h.get("grafiek")).booleanValue();
			expressie = ((Boolean) h.get("expressie")).booleanValue();
			links = ((Boolean) h.get("links")).booleanValue();
			zoomStateHolderState = toMap(h.get("zoomStateHolderState"));
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return;
		}

		zoomStateHolder.setState(zoomStateHolderState);

		int n = this.aantalSc;
		for (int i = 0; i < n; i++)
		{
			verwijder(schuifcomponenten[0]);
		}

		this.aantalSc = aantalSc;
		schuifcomponenten = new AlgebraSchuifComponent[200];
		for (int i = 0; i < aantalSc; i++)
		{
			try
			{
				Class c = Class.forName(classNames[i]);
				Constructor cc = c.getDeclaredConstructor(new Class[]
				{ AlgebraSchuifVeld.class, int.class, int.class, int.class, int.class });
				int breedte = 50;
				int hoogte = 20;
				if (classNames[i].equals("fi.algebrapijlenopdr.GrafiekComponent"))
				{
					breedte = 210;
					hoogte = 220;
					Object o = cc.newInstance(new Object[]
					{ this, new Integer(posX[i]), new Integer(posY[i]), new Integer(breedte), new Integer(hoogte) });
					schuifcomponenten[i] = (AlgebraSchuifComponent) o;
					grafiekComponent = (GrafiekComponent) schuifcomponenten[i];
				}
				else
				{
					Object o = cc.newInstance(new Object[]
					{ this, new Integer(posX[i]), new Integer(posY[i]), new Integer(breedte), new Integer(hoogte) });
					schuifcomponenten[i] = (AlgebraSchuifComponent) o;
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		for (int i = 0; i < aantalSc; i++)
		{
			if (!(schuifcomponenten[i] instanceof GrafiekComponent))
				schuifcomponenten[i].setState(scStates[i]);
			// bij een GrafiekComponent lukt dit niet omdat die bij setState de
			// parent nodig heeft en die heeft ie nog niet
		}

		int max = aantalSc;
		for (int i = 0; i < max; i++)
		{
			if (!(schuifcomponenten[i] instanceof GrafiekComponent))
			{
				Pijl p = new Pijl(this);
				if (schuifcomponenten[i].isStapel)
				{
					schuifcomponenten[i].zetLinks(links);
					p.zetLinks(links);
				}
				schuifcomponenten[i].voegPijlToe(p);
			}
			add(schuifcomponenten[i]);
		}

		for (int i = 0; i < aantalSc; i++)
		{
			for (int j = 0; j < aantalSc; j++)
			{
				if (connections[i][j])
				{
					Pijl p = schuifcomponenten[i].pijlUit[schuifcomponenten[i].aantalPu - 1];
					// schuifcomponenten[j].zetLinks(links);
					schuifcomponenten[j].verbind(p);
					p.zetVerbonden(schuifcomponenten[j]);
				}
			}
		}

		if (grafiek)
		{
			for (int i = 0; i < 10; i++)
			{
				if (graphConnections[i] != -1)
				{
					Pijl p = schuifcomponenten[graphConnections[i]].pijlUit[schuifcomponenten[graphConnections[i]].aantalPu
						- 1];
					grafiekComponent.verbind(p, i);
					p.zetVerbonden(grafiekComponent);
				}
			}
		}

		for (int i = 0; i < aantalSc; i++)
		{
			schuifcomponenten[i].setState(scStates[i]);
		}

		for (int i = 0; i < aantalSc; i++)
		{
			schuifcomponenten[i].zetVeranderd(20);
			if (schuifcomponenten[i] instanceof UitvoerSchuifComponent)
			{
				((UitvoerSchuifComponent) schuifcomponenten[i]).zetToonWaarde(!expressie);
				((UitvoerSchuifComponent) schuifcomponenten[i]).zetScroll(true);
				schuifcomponenten[i].zetVeranderd(20);
			}
		}

		for (int i = 0; i < aantalSc; i++)
		{
			if (schuifcomponenten[i] instanceof GrafiekComponent)
			{
				schuifcomponenten[i].setState(scStates[i]);
				schuifcomponenten[i].zetVeranderd(20);
			}
		}

		tabelCheckbox.setSelected(tabel);
		grafiekCheckbox.setSelected(grafiek);

		// ip.zetExpressie(expressie); // deze optie niet aanwezig
		this.links = links;
		if (links)
		{
			for (int i = 0; i < aantalSc; i++)
			{
				if (schuifcomponenten[i].isStapel)
				{
					// schuifcomponenten[i].setLocation(schuifcomponenten[i].getLocation().x+10,
					// schuifcomponenten[i].getLocation().y);
					schuifcomponenten[i].zetLinks(true);
				}
			}
		}

		// nodig voor backward compatability (bij niet versie staat de
		// stapelcomponenten iets hoger
		for (int i = 0; i < aantalSc; i++)
		{
			if (schuifcomponenten[i].isStapel)
			{
				if (schuifcomponenten[i] instanceof UitvoerSchuifComponent)
					schuifcomponenten[i].zetPlaats(20, 35);
				if (schuifcomponenten[i] instanceof OptelSchuifComponent)
					schuifcomponenten[i].zetPlaats(20, 90);
				if (schuifcomponenten[i] instanceof AftrekSchuifComponent)
					schuifcomponenten[i].zetPlaats(20, 115);
				if (schuifcomponenten[i] instanceof VermenigvuldigSchuifComponent)
					schuifcomponenten[i].zetPlaats(20, 140);
				if (schuifcomponenten[i] instanceof DeelSchuifComponent)
					schuifcomponenten[i].zetPlaats(20, 165);
				if (schuifcomponenten[i] instanceof OmkeringSchuifComponent)
					schuifcomponenten[i].zetPlaats(20, 190);
				if (schuifcomponenten[i] instanceof WortelSchuifComponent)
					schuifcomponenten[i].zetPlaats(20, 215);
				if (schuifcomponenten[i] instanceof MachtSchuifComponent)
					schuifcomponenten[i].zetPlaats(20, 240);
			}
		}

		Enumeration en = zoomStateHolder.keys();
		while (en.hasMoreElements())
		{
			String key = (String) en.nextElement();
			setZoomStates(key, zoomStateHolder.getZoomState(key));
		}

		copyItem.setEnabled(!veldIsLeeg());

		zetToolkit(toolkit);

		zetAlleenInvullen(alleenInvullen);

		zetIsDemo(isDemo);

		boolean brugklas = false;
		if (h.containsKey("brugklas"))
			brugklas = ((Boolean) h.get("brugklas")).booleanValue();
		zetBrugklas(brugklas);

		boolean terugHeen = true;
		if (h.containsKey("terugHeen"))
			terugHeen = ((Boolean) h.get("terugHeen")).booleanValue();
		zetTerugHeen(terugHeen);

		boolean tabelOptie = true;
		if (h.containsKey("tabelOptie"))
			tabelOptie = ((Boolean) h.get("tabelOptie")).booleanValue();
		zetTabelOptie(tabelOptie);

		boolean grafiekOptie = true;
		if (h.containsKey("grafiekOptie"))
			grafiekOptie = ((Boolean) h.get("grafiekOptie")).booleanValue();
		zetGrafiekOptie(grafiekOptie);

		boolean scrollOptie = true;
		if (h.containsKey("scrollOptie"))
			scrollOptie = ((Boolean) h.get("scrollOptie")).booleanValue();
		zetScrollOptie(scrollOptie);

		boolean zoomOptie = true;
		if (h.containsKey("zoomOptie"))
			zoomOptie = ((Boolean) h.get("zoomOptie")).booleanValue();
		zetZoomOptie(zoomOptie);

		tekenOpnieuw();

	}

	private boolean[][] toBooleanArrayArray(Object object, int aantal)
	{
		if (object instanceof boolean[][])
			return (boolean[][]) object;
		if (object instanceof List)
		{
			Iterator it = ((List) object).iterator();
			boolean[][] result = new boolean[aantal][aantal];
			for (int i = 0; i < result.length; i++)
			{
				boolean b[] = result[i];
				for (int j = 0; j < b.length; j++)
				{
					b[j] = Boolean.TRUE.equals(it.next());
				}
			}
			return result;
		}
		return null;
	}

	private Hashtable[] toHashtableArray(Object object)
	{
		if (object instanceof Hashtable[])
			return (Hashtable[]) object;
		if (object instanceof List)
		{
			List<Object> list = (List<Object>) object;
			Hashtable[] result = new Hashtable[list.size()];
			for (int i = 0; i < result.length; i++)
			{
				Object n = list.get(i);
				if (n != null)
					result[i] = toMap(n);
			}
			return result;
		}
		return null;
	}

	private String[] toStringArray(Object object)
	{
		if (object instanceof String[])
			return (String[]) object;
		if (object instanceof List)
		{
			List<String> list = (List<String>) object;
			String[] result = new String[list.size()];
			for (int i = 0; i < result.length; i++)
			{
				String n = list.get(i);
				if (n != null)
				{
					if (n.startsWith("fi."))
						result[i] = n;
					else
						result[i] = "fi.algebrapijlenopdr." + n;
				}
			}
			return result;
		}
		return null;
	}

	private int[] toIntArray(Object object)
	{
		if (object instanceof int[])
			return (int[]) object;
		if (object instanceof List)
		{
			List<Number> list = (List<Number>) object;
			int[] result = new int[list.size()];
			for (int i = 0; i < result.length; i++)
			{
				Number n = list.get(i);
				if (n != null)
					result[i] = n.intValue();
			}
			return result;
		}
		return null;
	}

	static Hashtable toMap(Object object)
	{
		if (object instanceof Hashtable)
			return (Hashtable) object;
		if (object instanceof Map)
			return new Hashtable((Map) object);
		return null;
	}

	public void paste()
	{
		if ((AlgebraPijlenOpdr.clipBoard != null) && !AlgebraPijlenOpdr.clipBoard.equals(""))
		{
			Object o = StringCodeObject.decodeStringToObject(AlgebraPijlenOpdr.clipBoard);
			if (o == null)
				return;
			Hashtable h = (Hashtable) o;
			setPasteTable(h);
		}
	}

	public void setPasteTable(Hashtable h)
	{

		int aantalPasteSc = 0;
		String[] classNames = null;
		int[] posX = null;
		int[] posY = null;
		Hashtable[] scStates = null;
		boolean[][] connections = null;
		int[] graphConnections = null;
		boolean tabel = false;
		boolean grafiek = false;
		Hashtable zoomStateHolderState = null;

		try
		{
			aantalPasteSc = ((Integer) h.get("aantalSc")).intValue();
			classNames = (String[]) h.get("classNames");
			posX = (int[]) h.get("posX");
			posY = (int[]) h.get("posY");
			scStates = (Hashtable[]) h.get("scStates");
			connections = (boolean[][]) h.get("connections");
			graphConnections = (int[]) h.get("graphConnections");
			grafiek = ((Boolean) h.get("grafiek")).booleanValue();
			zoomStateHolderState = (Hashtable) h.get("zoomStateHolderState");
		}
		catch (Exception ex)
		{
			return;
		}

		zoomStateHolder.setState(zoomStateHolderState);
		/*
		 * int n = this.aantalSc; for (int i = 0; i < n; i++) {
		 * verwijder(schuifcomponenten[0]); }
		 */
		int aantalStapels = aantalSc;

		// schuifcomponenten = new AlgebraSchuifComponent[200];
		for (int i = 0; i < aantalPasteSc; i++)
		{
			try
			{
				Class c = Class.forName(classNames[i]);
				Constructor cc = c.getDeclaredConstructor(new Class[]
				{ AlgebraSchuifVeld.class, int.class, int.class, int.class, int.class });
				int breedte = 50;
				int hoogte = 20;
				if (classNames[i].equals("fi.algebrapijlenopdr.GrafiekComponent"))
				{
					breedte = 210;
					hoogte = 220;
					Object o = cc.newInstance(new Object[]
					{ this, new Integer(posX[i]), new Integer(posY[i]), new Integer(breedte), new Integer(hoogte) });
					schuifcomponenten[aantalStapels + i] = (AlgebraSchuifComponent) o;
					grafiekComponent = (GrafiekComponent) schuifcomponenten[aantalStapels + i];
					aantalSc++;
				}
				else
				{
					Object o = cc.newInstance(new Object[]
					{ this, new Integer(posX[i]), new Integer(posY[i]), new Integer(breedte), new Integer(hoogte) });
					schuifcomponenten[aantalStapels + i] = (AlgebraSchuifComponent) o;
					aantalSc++;
				}
			}
			catch (Exception e)
			{
			}
		}

		for (int i = 0; i < (aantalSc - aantalStapels); i++)
		{
			if (!(schuifcomponenten[aantalStapels + i] instanceof GrafiekComponent))
				schuifcomponenten[aantalStapels + i].setState(scStates[i]);
			// bij een GrafiekComponent lukt dit niet omdat die bij setState de
			// parent nodig heeft en die heeft ie nog niet
		}

		int max = aantalSc;
		for (int i = 0; i < (max - aantalStapels); i++)
		{
			if (!(schuifcomponenten[aantalStapels + i] instanceof GrafiekComponent))
			{
				Pijl p = new Pijl(this);
				if (schuifcomponenten[aantalStapels + i].isStapel)
				{
					schuifcomponenten[aantalStapels + i].zetLinks(links);
					p.zetLinks(links);
				}
				schuifcomponenten[aantalStapels + i].voegPijlToe(p);
			}
			add(schuifcomponenten[aantalStapels + i]);
		}

		for (int i = 0; i < (aantalSc - aantalStapels); i++)
		{
			for (int j = 0; j < (aantalSc - aantalStapels); j++)
			{
				if (connections[i][j])
				{
					Pijl p = schuifcomponenten[aantalStapels + i].pijlUit[schuifcomponenten[aantalStapels + i].aantalPu
						- 1];
					schuifcomponenten[aantalStapels + j].verbind(p);
					p.zetVerbonden(schuifcomponenten[aantalStapels + j]);
				}
			}
		}
		if (grafiek)
		{
			for (int i = 0; i < 10; i++)
			{
				if (graphConnections[i] != -1)
				{
					Pijl p = schuifcomponenten[aantalStapels
						+ graphConnections[i]].pijlUit[schuifcomponenten[aantalStapels + graphConnections[i]].aantalPu
							- 1];
					grafiekComponent.verbind(p, i);
					p.zetVerbonden(grafiekComponent);
				}
			}
		}

		for (int i = 0; i < (aantalSc - aantalStapels); i++)
		{
			schuifcomponenten[aantalStapels + i].setState(scStates[i]);
		}

		for (int i = 0; i < (aantalSc - aantalStapels); i++)
		{
			schuifcomponenten[aantalStapels + i].zetVeranderd(20);
			if (schuifcomponenten[aantalStapels + i] instanceof UitvoerSchuifComponent)
			{
				((UitvoerSchuifComponent) schuifcomponenten[aantalStapels + i]).zetToonWaarde(true);
				((UitvoerSchuifComponent) schuifcomponenten[aantalStapels + i]).zetScroll(true);
				schuifcomponenten[aantalStapels + i].zetVeranderd(20);
			}
		}

		for (int i = 0; i < (aantalSc - aantalStapels); i++)
		{
			if (schuifcomponenten[aantalStapels + i] instanceof GrafiekComponent)
			{
				schuifcomponenten[aantalStapels + i].setState(scStates[i]);
				schuifcomponenten[aantalStapels + i].zetVeranderd(20);
			}
		}

		tabelCheckbox.setSelected(tabel);
		grafiekCheckbox.setSelected(grafiek);

		// ip.zetExpressie(expressie); // deze optie niet aanwezig

		/*
		 * this.links = links; if (links) { for (int i = 0; i < aantalSc; i++) {
		 * if (schuifcomponenten[i].isStapel) {
		 * //schuifcomponenten[i].setLocation(schuifcomponenten[i].getLocation()
		 * .x+10, schuifcomponenten[i].getLocation().y);
		 * schuifcomponenten[i].zetLinks(true); } } }
		 */

		Enumeration en = zoomStateHolder.keys();
		while (en.hasMoreElements())
		{
			String key = (String) en.nextElement();
			setZoomStates(key, zoomStateHolder.getZoomState(key));
		}

		for (int i = (aantalSc - 1); i >= aantalStapels; i--)
		{
			if (schuifcomponenten[i].isStapel)
				verwijder(schuifcomponenten[i]);
		}

		tekenOpnieuw();
	}

	public void paint(Graphics g)
	{
		super.paint(g);
		if ((selecterenBezig || selectieGemaakt) && clip != null)
		{
			int b = clip.getSize().width;
			int h = clip.getSize().height;
			int x = clip.getLocation().x;
			int y = clip.getLocation().y;
			g.setColor(Color.red);
			g.drawRect(x, y, b, h);
		}
	}

	public void maakStapel()
	{
		aantalSc = 0;
		int b = 50;
		int h = 20;
		schuifcomponenten = new AlgebraSchuifComponent[200];
		schuifcomponenten[aantalSc] = new UitvoerSchuifComponent(this, 20, 35, b, h);
		// ((UitvoerSchuifComponent)
		// schuifcomponenten[aantalSc]).zetTabelAan(tabelCheckbox.getState());
		((UitvoerSchuifComponent) schuifcomponenten[aantalSc]).zetTabelAan(tabelCheckbox.isSelected());
		((UitvoerSchuifComponent) schuifcomponenten[aantalSc]).zetScroll(true);
		aantalSc++;
		schuifcomponenten[aantalSc] = new OptelSchuifComponent(this, 20, 90, b, h);
		aantalSc++;
		schuifcomponenten[aantalSc] = new AftrekSchuifComponent(this, 20, 115, b, h);
		aantalSc++;
		schuifcomponenten[aantalSc] = new VermenigvuldigSchuifComponent(this, 20, 140, b, h);
		aantalSc++;
		schuifcomponenten[aantalSc] = new DeelSchuifComponent(this, 20, 165, b, h);
		aantalSc++;
		schuifcomponenten[aantalSc] = new OmkeringSchuifComponent(this, 20, 190, b, h);
		aantalSc++;
		schuifcomponenten[aantalSc] = new WortelSchuifComponent(this, 20, 215, b, h);
		aantalSc++;
		schuifcomponenten[aantalSc] = new MachtSchuifComponent(this, 20, 240, b, h);
		aantalSc++;

		int max = aantalSc;
		for (int i = 0; i < max; i++)
		{
			schuifcomponenten[i].zetLinks(links);
			Pijl p = new Pijl(this);
			p.zetLinks(links);
			schuifcomponenten[i].voegPijlToe(p);
			add(schuifcomponenten[i]);
		}
	}

	public void tekenAchtergrond(Graphics g)
	{
		Dimension dd = getSize();
		g.setColor(Color.white);
		// g.setColor(Color.orange);
		g.fillRect(0, 0, dd.width, dd.height);
		if (fixed)
		{
			// System.out.println("fixed");
			g.setColor(getParent().getBackground());// (Color.white);
			g.fillRect(0, 0, dd.width, dd.height);
			hidePanel.setBackground(getParent().getParent().getBackground());
			return;
		}
		if (toolkit)
		{
			// g.setColor(Color.lightGray);
			g.setColor(new Color(210, 210, 210));
			g.fillRect(0, 0, 110, dd.height);
			// g.setColor(Color.black);
			g.setColor(Color.gray);
			g.drawLine(110, 0, 110, dd.height - 1);
			g.drawRect(0, 0, dd.width - 1, dd.height - 1);
		}

		g.setFont(font);
		FontMetrics fm = g.getFontMetrics();
		g.setColor(Color.black);
		String s = AlgebraPijlenOpdr.rb.getString("invoerVakLabel");
		int lengte = fm.stringWidth(s);
		g.drawString(s, 55 - lengte / 2, 25);

		s = AlgebraPijlenOpdr.rb.getString("bewerkingenLabel");
		lengte = fm.stringWidth(s);
		g.drawString(s, 55 - lengte / 2, 80);

	}

/*	
	public void zetSchuiver(SchuifComponent sc)
	{	add(schuiflaag, 0);
		schuiflaag.add(sc, 0);
		AlgebraSchuifComponent asc = (AlgebraSchuifComponent) sc;
		for (int i = 0; i < asc.aantalPu; i++)
		{	if (asc.pijlUit[i] != null)
				schuiflaag.add(asc.pijlUit[i]);
		}
		if (asc.pijlIn1 != null)
			schuiflaag.add(asc.pijlIn1);
		if (asc.pijlIn2 != null)
			schuiflaag.add(asc.pijlIn2);
		if (asc instanceof GrafiekComponent)
		{	GrafiekComponent gsc = (GrafiekComponent)asc;
			for (int i = 0; i < gsc.aantalPijlenIn; i++)
			{	schuiflaag.add(gsc.pijlenIn[i]);
			}
		}
		tekenOpnieuw();
	}
*/
/*	
	public void losSchuiver(SchuifComponent sc)
	{	add(sc, 0);
		AlgebraSchuifComponent asc = (AlgebraSchuifComponent) sc;
		for (int i = 0; i < asc.aantalPu; i++)
		{	if (asc.pijlUit[i] != null)
				add(asc.pijlUit[i], 0);
		}
		if (asc.pijlIn1 != null)
			add(asc.pijlIn1, 0);
		if (asc.pijlIn2 != null)
			add(asc.pijlIn2, 0);
		if (asc instanceof GrafiekComponent)
		{	GrafiekComponent gsc = (GrafiekComponent)asc;
			for (int i = 0; i < gsc.aantalPijlenIn; i++)
			{	add(gsc.pijlenIn[i], 0);
			}
		}
		tekenOpnieuw();
	}
*/	
	public void zetStapel(AlgebraSchuifComponent asc)
	{
		int x = asc.getLocation().x;
		int y = asc.getLocation().y;
		int b = asc.getSize().width;
		int h = asc.getSize().height;
		// if(asc instanceof InvoerSchuifComponent)
		// { schuifcomponenten[aantalSc] = new InvoerSchuifComponent(this
		// ,x,y,b,h);
		// }
		if (asc instanceof UitvoerSchuifComponent)
		{
			schuifcomponenten[aantalSc] = new UitvoerSchuifComponent(this, x, y, b, h);
			// ((UitvoerSchuifComponent)
			// schuifcomponenten[aantalSc]).zetTabelAan(tabelCheckbox.getState());
			((UitvoerSchuifComponent) schuifcomponenten[aantalSc]).zetTabelAan(tabelCheckbox.isSelected());
		}
		else if (asc instanceof OptelSchuifComponent)
		{
			schuifcomponenten[aantalSc] = new OptelSchuifComponent(this, x, y, b, h);
		}
		else if (asc instanceof AftrekSchuifComponent)
		{
			schuifcomponenten[aantalSc] = new AftrekSchuifComponent(this, x, y, b, h);
		}
		else if (asc instanceof VermenigvuldigSchuifComponent)
		{
			schuifcomponenten[aantalSc] = new VermenigvuldigSchuifComponent(this, x, y, b, h);
		}
		else if (asc instanceof DeelSchuifComponent)
		{
			schuifcomponenten[aantalSc] = new DeelSchuifComponent(this, x, y, b, h);
		}
		else if (asc instanceof OmkeringSchuifComponent)
		{
			schuifcomponenten[aantalSc] = new OmkeringSchuifComponent(this, x, y, b, h);
		}
		else if (asc instanceof WortelSchuifComponent)
		{
			schuifcomponenten[aantalSc] = new WortelSchuifComponent(this, x, y, b, h);
		}
		else if (asc instanceof MachtSchuifComponent)
		{
			schuifcomponenten[aantalSc] = new MachtSchuifComponent(this, x, y, b, h);
		}
		else
			return;
		schuifcomponenten[aantalSc].zetLinks(links);
		Pijl p = new Pijl(this);
		p.zetLinks(links);
		schuifcomponenten[aantalSc].voegPijlToe(p);
		add(schuifcomponenten[aantalSc]);
		aantalSc++;

		copyItem.setEnabled(!veldIsLeeg());
	}

	public void verwijder(AlgebraSchuifComponent sc)
	{
		for (int i = 0; i < aantalSc; i++)
		{
			if (schuifcomponenten[i] == sc)
			{
				if (sc instanceof GrafiekComponent)
				{
					GrafiekComponent gsc = (GrafiekComponent) sc;
					while (gsc.aantalPijlenIn > 0)
					{
						Pijl p = gsc.pijlenIn[0];
						gsc.maakLos(gsc.pijlenIn[0]);
						p.zender.verwijderPijl();
						p.pijlTerug();
					}
				}
				if (sc.pijlIn1 != null)
				{
					Pijl p = sc.pijlIn1;
					sc.maakLos(sc.pijlIn1);
					p.zender.verwijderPijl();
					p.pijlTerug();
				}
				if (sc.pijlIn2 != null)
				{
					Pijl p = sc.pijlIn2;
					sc.maakLos(sc.pijlIn2);
					p.zender.verwijderPijl();
					p.pijlTerug();

				}
				for (int k = 0; k < sc.aantalPu; k++)
				{
					if (sc.pijlUit[k].ontvanger != null)
					{
						AlgebraSchuifComponent as = sc.pijlUit[k].ontvanger;
						as.maakLos(sc.pijlUit[k]);
						as.zetVeranderd(20);
					}
					remove(sc.pijlUit[k]);
				}
				remove(sc);
				for (int j = i; j < aantalSc; j++)
				{
					schuifcomponenten[j] = schuifcomponenten[j + 1];
				}
				aantalSc--;
				tekenOpnieuw();

				copyItem.setEnabled(!veldIsLeeg());

				return;
			}
		}
	}

	public void zetVeranderd()
	{
		for (int i = 0; i < aantalSc; i++)
		{
			if (schuifcomponenten[i] instanceof UitvoerSchuifComponent)
			{
				boolean b = !ip.isExpr();
				((UitvoerSchuifComponent) schuifcomponenten[i]).zetToonWaarde(b);
			}
		}
		tekenOpnieuw();
	}

	public void setBounds(int x, int y, int b, int h)
	{
		if ((getLocation().x == x) && (getLocation().y == y) && (getSize().width == b) && (getSize().height == h))
		{
			return;
		}

		int oldX = getLocation().x;
		if ((oldX < 0) && !toolkit)
		{
			super.setBounds(x, y, b - oldX, h);

		}
		else
		{
			super.setBounds(x, y, b, h);
		}

		// zetToolkit(toolkit);
		// zetAlleenInvullen(alleenInvullen);
		// zetIsDemo(isDemo);
	}

	public void setSize(int b, int h)
	{
		if ((getSize().width == b) && (getSize().height == h))
			return;

		for (int i = 0; i < getComponentCount(); i++)
		{
			if (this.getComponent(i) instanceof Pijl)
			{
				getComponent(i).setSize(b, h);
			}
		}
		super.setSize(b, h);
	}

	public void zetTabellen(int beginwaarde, int selectnummer, String varN, double schaalFactorX)
	{
	}

	public void setZoomStates(String varnaam, ZoomState zoomState)
	{
		for (int i = 0; i < aantalSc; i++)
		{
			if (schuifcomponenten[i] instanceof UitvoerSchuifComponent)
			{
				((UitvoerSchuifComponent) schuifcomponenten[i]).setZoomState(varnaam, zoomState);

			}
			if (schuifcomponenten[i] instanceof GrafiekComponent)
			{
				((GrafiekComponent) schuifcomponenten[i]).setZoomState(varnaam, zoomState);
			}
		}
		tekenOpnieuw();
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == wisKnop)
		{
			if (editmodeState == null)
			{
				links = false;
				int n = aantalSc;
				for (int i = 0; i < n; i++)
				{
					verwijder(schuifcomponenten[0]);
				}
				maakStapel();
				if (grafiekCheckbox.isSelected())
				{
					schuifcomponenten[aantalSc] = grafiekComponent;
					aantalSc++;
					add(grafiekComponent);
				}
				zoomStateHolder = new ZoomStateHolder(this);
			}
			else
			{
				setState(editmodeState);
			}
		}
		else if (e.getSource() == terugKnop)
		{
			if (!links)
			{
				links = true;
				for (int i = 0; i < aantalSc; i++)
				{
					if (schuifcomponenten[i].isStapel)
					{
						schuifcomponenten[i].setLocation(schuifcomponenten[i].getLocation().x + 10,
							schuifcomponenten[i].getLocation().y);
						schuifcomponenten[i].zetLinks(true);
					}
				}
				terugKnop.setText(AlgebraPijlenOpdr.rb.getString("heenKnopLabel"));
				tekenOpnieuw();
			}

			else // links
			{
				links = false;
				for (int i = 0; i < aantalSc; i++)
				{
					if (schuifcomponenten[i].isStapel)
					{
						schuifcomponenten[i].setLocation(schuifcomponenten[i].getLocation().x - 10,
							schuifcomponenten[i].getLocation().y);
						schuifcomponenten[i].zetLinks(false);
					}
				}
				terugKnop.setText(AlgebraPijlenOpdr.rb.getString("terugKnopLabel"));
				tekenOpnieuw();
			}

		}
		else if ((e.getSource() instanceof JMenuItem)
			&& ((JMenuItem) e.getSource()).getText().equals(AlgebraPijlenOpdr.rb.getString("kopieerTekst")))
		{
			if (!veldIsLeeg())
			{
				copy();
			}
		}
		else if ((e.getSource() instanceof JMenuItem)
			&& ((JMenuItem) e.getSource()).getText().equals(AlgebraPijlenOpdr.rb.getString("plakTekst")))
		{
			if ((AlgebraPijlenOpdr.clipBoard != null) && !AlgebraPijlenOpdr.clipBoard.equals(""))
			{
				maakVeldLeeg();

				paste();
			}
		}
		else if (e.getSource() == grafiekCheckbox)
		{
			boolean b = grafiekCheckbox.isSelected();
			if (b)
			{
				grafiekComponent = new GrafiekComponent(this, getSize().width - 100, 200, 210, 220);
				schuifcomponenten[aantalSc] = grafiekComponent;
				aantalSc++;
				add(grafiekComponent);
			}
			else
			{
				verwijder(grafiekComponent);
			}
			tekenOpnieuw();
		}
		else if (e.getSource() == tabelCheckbox)
		{
			boolean b = tabelCheckbox.isSelected();
			for (int i = 0; i < aantalSc; i++)
			{
				if (schuifcomponenten[i] instanceof UitvoerSchuifComponent)
				{
					((UitvoerSchuifComponent) schuifcomponenten[i]).zetTabelAan(b);
				}
			}
			grafiekComponent.zetVeranderd(20);
			tekenOpnieuw();
		}
	}

	public void mousePressed(MouseEvent e)
	{
		requestFocus();
		if (e.getModifiers() == e.BUTTON3_MASK && e.getX() > 100)
		{
			if (popup.isEnabled())
				popup.show(this, e.getX(), e.getY());

		}
		else if (selecterenMogelijk)
		{
			selecterenBezig = true;
			selecterenMogelijk = false;
			clip = new Rectangle(e.getX(), e.getY(), 0, 0);
		}
		else if (selectieGemaakt && !clip.contains(e.getX(), e.getY()))
		{
			selectieGemaakt = false;
			tekenOpnieuw();
		}
	}

	public void mouseDragged(MouseEvent e)
	{
		if (selecterenBezig)
		{
			int b = clip.getSize().width;
			int h = clip.getSize().height;
			int x = clip.getLocation().x;
			int y = clip.getLocation().y;
			if (e.getX() - x > 0 && e.getY() - y > 0)
			{
				clip = new Rectangle(x, y, e.getX() - x, e.getY() - y);
				this.tekenOpnieuw();
			}
		}
	}

	public void mouseReleased(MouseEvent e)
	{
		if (selecterenBezig)
		{
			selectieGemaakt = true;
			selecterenBezig = false;
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}

	public void mouseMoved(MouseEvent e)
	{
	}

	public void mouseExited(MouseEvent e)
	{
	}

	public void mouseClicked(MouseEvent e)
	{
	}

	public void mouseEntered(MouseEvent e)
	{
	}
}
