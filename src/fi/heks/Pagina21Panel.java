package fi.heks;

import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import java.util.*;
import fi.heks.scobjects.*;
import fi.beans.appletutil.*;
import fi.heks.vectortek.*;

public class Pagina21Panel extends ScPanel implements MouseListener, MouseMotionListener, ActionListener 
{
	// private Heks eigenaar;
	//private AppletUtil au;
	
	HeksInteractiePanel heip;

	private AchtergrondContainer achtergrond;

	Tekening blokjePlus, blokjeMin, blokjeSleep, blokjeSleepMin;
	Tekening pot, potEruit, potErin, beginPot, eindPot, potinhoud, vloer, schrijfheks, werkheks;
	ZinkAnimatie za;
	GetalComponent tc, beginTemp, eindTemp;
	Thermometer tm;
	BlokjesContainer eruitContainer, erinContainer;
	//ScLWButton opdrachtKnop, werkKnop;
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

	ImageButton opnieuwKnop;

	public Pagina21Panel(int x, int y, int b, int h, HeksInteractiePanel heip) 
	{
		super(x, y, b, h);
		// eigenaar = applet;
		// setBackground(new Color(255,255,220));
		setBackground(heip.bgColor);
//System.out.println("p21 bg = " + getBackground().toString());		
		setOpaque(true);
		
		zetVastePlaats(true);

		this.heip = heip; 
		
		//au = new AppletUtil(applet);

		//Image heksnieuw = au.getImage("resources/heksnieuw.jpg");
		Image opnieuwknop = null;
		if (Heks.rb.getLocale().getLanguage().equals("nl")) 
		{
			opnieuwknop = heip.opnieuwNLImage; //au.getImage("resources/opnieuwknop.gif");
		} 
		else 
		{
			opnieuwknop = heip.opnieuwENImage; //au.getImage("resources/againKnop.gif");
		}
		//MediaTracker tr = new MediaTracker(this);
		//tr.addImage(heksnieuw, 0);
		//tr.addImage(opnieuwknop, 0);
		//try 
		//{
		//	tr.waitForAll();
		//} 
		//catch (Exception e) 
		//{
		//}

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

		//Color color_01 = new Color(240, 240, 240);
		//String kleurcode = applet.getParameter("color_01");
		//if (kleurcode != null)
		//	color_01 = new Color(Integer.parseInt(kleurcode.substring(1), 16));

		sleeppanel = new ScPanel(350, 30, b - 320, h - 5);
		//sleeppanel.setBackground(color_01);
		sleeppanel.setBackground(Color.white);
		sleeppanel.addMouseListener(this);
		sleeppanel.addMouseMotionListener(this);
		
		achtergrond = new AchtergrondContainer(0, 0, b - 320, h - 5);

		opnieuwKnop = new ImageButton(opnieuwknop);
		opnieuwKnop.setBounds(20, 600, 95, 35);
		opnieuwKnop.addActionListener(this);
		add(opnieuwKnop);

		pot = new Tekening(20, 280, 380, 330, heip, "potnieuw.gif", true);
		achtergrond.add(pot);

		vloer = new Tekening(-10, 450, 430, 175, heip, "vloer.gif", true);
		achtergrond.add(vloer);

		beginTemp = new GetalComponent(170, 100, 80, 40);
		beginTemp.zetInstelbaar(true);
		beginTemp.zetAlsTemp(true);
		beginTemp.addActionListener(this);
		add(beginTemp);

		beginLabel = new ScLabel(50, 105, 60, 30, Heks.rb.getString("beginLabel"));
		beginLabel.addMouseListener(this);
		add(beginLabel);

		beginPot = new Tekening(35, 85, 90, 65, heip, "potzwart.gif", true);
		add(beginPot);


		erinLabel = new ScLabel(65, 215, 60, 30, Heks.rb.getString("erinLabel"));
		add(erinLabel);

		potErin = new Tekening(30, 170, 100, 90, heip, "potErin.gif", true);
		add(potErin);

		eruitLabel = new ScLabel(35, 365, 60, 30, Heks.rb.getString("eruitLabel"));
		// add(eruitLabel);

		potEruit = new Tekening(30, 260, 100, 90, heip, "potEruit.gif");
		// add(potEruit);

		eindTemp = new GetalComponent(170, 290, 80, 40);
		eindTemp.zetAlsTemp(true);
		add(eindTemp);

		eindLabel = new ScLabel(50, 305, 60, 30, Heks.rb.getString("eindLabel"));
		add(eindLabel);

		eindPot = new Tekening(35, 285, 90, 65, heip, "potzwart.gif");
		add(eindPot);

		erinContainer = new BlokjesContainer(180, 190, 165, 127, heip);
		erinContainer.zetMaxRijen(2);
		add(erinContainer);

		eruitContainer = new BlokjesContainer(150, 380, 110, 85, heip);
		// add(eruitContainer);

		blokjePlus = new Tekening(350, 80, 65, 65, heip, "blokjePlus.gif", true);
		achtergrond.add(blokjePlus);

		blokjeMin = new Tekening(350, 150, 65, 65, heip, "blokjeMin.gif");
		achtergrond.add(blokjeMin);

		//uitleg = new ScTekstContainer(5, 5, 285, 20, 5,
		//		"Gooi blokjes in de ketel./Wat gebeurt er met de temperatuur?/Wil je een andere begintemperatuur, /klik dat op de begintemperatuur en/vul een ander getal in.");
		//uitleg.lijnUit(ScLabel.LINKS);
		// add(uitleg);

		// opdracht = new
		// ScTekstContainer(5,405,285,20,4,"Beschrijf hieronder nauwkeurig hoe je de/temperatuur kunt regelen met blokjes.");
		// opdracht.lijnUit(ScLabel.LINKS);
		// add(opdracht);

		//titel = new ScLabel(20, 10, 240, 40, "De toverdrank 1");
		// add(titel);

		za = new ZinkAnimatie(110, 290, 210, 200, heip);
		sleeppanel.add(za, 0);

		blokjeSleep = new Tekening(350, 80, 65, 65, heip, "blokjePlus.gif");
		sleeppanel.add(blokjeSleep, 0);

		blokjeSleepMin = new Tekening(350, 150, 65, 65, heip, "blokjeMin.gif");
		sleeppanel.add(blokjeSleepMin, 0);

		potinhoud = new Tekening(20, 305, 375, 300, heip, "inhoudnieuw.gif", true);
		sleeppanel.add(potinhoud, 0);

		tc = new GetalComponent(320, 360, 120, 40);
		tc.zetAlsTemp(true);
		sleeppanel.add(tc, 0);

		tm = new Thermometer(100, 0, 55, 400);
		sleeppanel.add(tm, 0);

		sleeppanel.add(achtergrond);
		add(sleeppanel);

		// textArea = new
		// ScTextArea(5,450,270,110,0,0,TextArea.SCROLLBARS_VERTICAL_ONLY,"");
		// add(textArea);

		//opdrachtTitel = new ScLabel(20, 200, 200, 40, "Opdracht");
		//opdrachtTitel.setVisible(false);
		//add(opdrachtTitel);

		//opdracht = new ScTekstContainer(5, 250, 285, 20, 4, "Beschrijf hieronder nauwkeurig hoe je de/temperatuur kunt regelen met blokjes.");
		//opdracht.lijnUit(ScLabel.LINKS);
		//opdracht.setVisible(false);
		//add(opdracht);

		textArea = new ScTextArea(5, 340, 270, 150, 0, 0, TextArea.SCROLLBARS_VERTICAL_ONLY, "");
		textArea.setVisible(false);
		add(textArea);

		//opdrachtKnop = new ScLWButton(5, 510, 270, 35, "Maak de opdracht");
		//opdrachtKnop.addActionListener(this);
		// add(opdrachtKnop);

		//werkKnop = new ScLWButton(5, 510, 270, 35, "Werk met de blokjes");
		//werkKnop.addActionListener(this);
		//textArea.setVisible(false);
		// add(werkKnop);

		//ImageComponent heksNieuw = new ImageComponent(heksnieuw);
		//heksNieuw.setLocation(15, 0);
		// add(heksNieuw,0);

	}

	public void setState(Hashtable h) 
	{
System.out.println("p21 setState");		
		

//if (erinContainer.isVisible())
//System.out.println("erinC visible");
//else
//System.out.println("erinC not visible");

		int begintemp = 0; 
		if (h.containsKey("begintemp"))
			begintemp = ((Integer) h.get("begintemp")).intValue();
		beginTemp.zetWaarde(begintemp);
		
		erinContainer.removeAll();
		
		int blokjespluserin = 0;
		if (h.containsKey("blokjespluserin"))
			blokjespluserin = ((Integer) h.get("blokjespluserin")).intValue();
		for (int plusInCnt = 0; plusInCnt < blokjespluserin; plusInCnt++)
			erinContainer.voegBlokjeToe(true);
		int blokjesminerin = 0;
		if (h.containsKey("blokjesminerin"))
			blokjesminerin = ((Integer) h.get("blokjesminerin")).intValue();
		for (int minInCnt = 0; minInCnt < blokjesminerin; minInCnt++)
			erinContainer.voegBlokjeToe(false);
		
		int eindtemp = 0; 
		if (h.containsKey("eindtemp"))
			eindtemp = ((Integer) h.get("eindtemp")).intValue();
		eindTemp.zetWaarde(eindtemp);
		tc.zetWaarde(eindtemp);
		tm.zetTemp(eindtemp);
		
		repaint();
		
	}

	public Hashtable getState() 
	{
System.out.println("p21 getState");		
		Hashtable h = new Hashtable();
		h.put("begintemp", new Integer(beginTemp.geefWaarde()));
		h.put("blokjespluserin", new Integer(erinContainer.getalPlus.geefWaarde()));
//System.out.println("pluserin " + erinContainer.getalPlus.geefWaarde());		
		h.put("blokjesminerin", new Integer(erinContainer.getalMin.geefWaarde()));
//System.out.println("minerin " + erinContainer.getalPlus.geefWaarde());		
		h.put("eindtemp", new Integer(eindTemp.geefWaarde()));

		return h;
	}

	public double getScore() 
	{
		if (textArea.getText() != null && textArea.getText().length() > 100)
			return 100;
		if (textArea.getText() != null && textArea.getText().length() > 5)
			return 10;
		return 0;
	}

	public void start() 
	{
		beginTemp.zetWaarde(0);
		eindTemp.zetWaarde(0);
		tc.zetWaarde(0);
		tm.zetTemp(0);
		erinContainer.removeAll();
		eruitContainer.removeAll();
		za.start();
	}

	public void stop() 
	{
		za.stop();
	}

	public void mousePressed(MouseEvent e) 
	{
		//if (opdracht.isVisible())
		//	return;
		laatstex = e.getX();
		laatstey = e.getY();

		if (beginLabel.contains(e.getX(), e.getY())) 
		{
			beginTemp.vulIn();
		}

		p = new Polygon[11];
		for (int i = 0; i < 11; i++) 
		{
			p[i] = ((VeelhoekTek) (potinhoud.to[i])).basisPolygon;
		}

		if (blokjePlus.contains(e.getX(), e.getY())) 
		{
			raakPlusBuiten = true;
		}

		if (blokjeSleep.contains(e.getX(), e.getY())) 
		{
			raakSleep = true;
		}

		if (blokjeSleepMin.contains(e.getX(), e.getY())) 
		{
			raakSleepMin = true;
		}
		int x = e.getX() - potinhoud.getLocation().x;
		int y = e.getY() - potinhoud.getLocation().y;


	}

	public void mouseDragged(MouseEvent e) 
	{
		//if (opdracht.isVisible())
		//	return;
		int dx = e.getX() - laatstex;
		int dy = e.getY() - laatstey;

		if (raakSleep) 
		{
			blokjeSleep.setLocation(blokjeSleep.getLocation().x + dx, blokjeSleep.getLocation().y + dy);
			Polygon p = ((VulKrommeTek) (pot.to[2])).buigPolygon;
			int lx = pot.getLocation().x;
			int ly = pot.getLocation().y;
			for (int i = 0; i < p.npoints; i++) {
				if (blokjeSleep.contains(p.xpoints[i] + lx, p.ypoints[i] + ly)) 
				{
					blokjeSleep.setLocation(blokjeSleep.getLocation().x - dx, blokjeSleep.getLocation().y - dy);
					raakSleep = false;
				}
			}
			blokjeSleep.repaint();
		}
		if (raakSleepMin) 
		{
			blokjeSleepMin.setLocation(blokjeSleepMin.getLocation().x + dx, blokjeSleepMin.getLocation().y + dy);
			Polygon p = ((VulKrommeTek) (pot.to[2])).buigPolygon;
			int lx = pot.getLocation().x;
			int ly = pot.getLocation().y;
			for (int i = 0; i < p.npoints; i++) {
				if (blokjeSleepMin.contains(p.xpoints[i] + lx, p.ypoints[i] + ly)) 
				{
					blokjeSleepMin.setLocation(blokjeSleepMin.getLocation().x - dx, blokjeSleepMin.getLocation().y - dy);
					raakSleepMin = false;
				}
			}
			blokjeSleepMin.repaint();
		} 
		else if (blokjeSleep.contains(e.getX(), e.getY())) 
		{
			raakSleep = true;
		} 
		else if (blokjeSleepMin.contains(e.getX(), e.getY())) 
		{
			raakSleepMin = true;
		}

		if (!plusEruit && blokjeSleep.getLocation().x + blokjeSleep.getSize().width < za.getLocation().x + za.getSize().width
				&& blokjeSleep.getLocation().x > za.getLocation().x && blokjeSleep.getLocation().y > za.getLocation().y
				&& blokjeSleep.getLocation().y + blokjeSleep.getSize().height < za.getLocation().y + za.getSize().height) 
		{ // plons.play();
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
				&& blokjeSleepMin.getLocation().y + blokjeSleepMin.getSize().height < za.getLocation().y + za.getSize().height) 
		{ // plons.play();
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

	public void mouseReleased(MouseEvent e) 
	{
		raakSleep = false;
		raakSleepMin = false;

		if (!plusEruit && blokjeSleep.getLocation().x + blokjeSleep.getSize().width < pot.getLocation().x + pot.getSize().width
				&& blokjeSleep.getLocation().x > pot.getLocation().x
				&& blokjeSleep.getLocation().y + blokjeSleep.getSize().height < za.getLocation().y + za.getSize().height) 
		{
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
		} 
		else 
		{
			blokjeSleep.setLocation((int) (blokjePlus.getLocation().x), (int) (blokjePlus.getLocation().y));
		}

		if (!minEruit && blokjeSleepMin.getLocation().x + blokjeSleepMin.getSize().width < pot.getLocation().x + pot.getSize().width
				&& blokjeSleepMin.getLocation().x > pot.getLocation().x
				&& blokjeSleepMin.getLocation().y + blokjeSleepMin.getSize().height < za.getLocation().y + za.getSize().height) 
		{
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
		} 
		else 
		{
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

	public void actionPerformed(ActionEvent e) 
	{
		if (beginTemp.isBekend()) 
		{
			eindTemp.zetBekend(true);
			tc.zetBekend(true);
			eindTemp.zetWaarde(beginTemp.geefWaarde());
			tc.zetWaarde(beginTemp.geefWaarde());
			tm.zetTemp(beginTemp.geefWaarde());
		} 
		else 
		{
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
