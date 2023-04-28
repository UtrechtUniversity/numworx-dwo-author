package fi.heks;

import java.awt.*;
import java.applet.*;
import java.util.*;
import java.awt.event.*;
import fi.heks.scobjects.*;
import fi.beans.appletutil.*;
import fi.heks.vectortek.*;

public class Pagina23Panel extends ScPanel implements ActionListener 
{
	//private AppletUtil au;
	HeksInteractiePanel heip;
	
	private ScLWButton opdrachtKnop, werkKnop;
	private ScLabel titelLabel, maalLabel, opdrachtTitel;
	//private OefenTafereelPanelEmmer_WN oefenTafereelPanel;
	private Pagina23OefenPanel oefenTafereelPanel;
	EmmerPanel emmer;
	private GetalComponent emmerTeller;
	private ScTekstContainer uitleg, opdracht;
	ScTextArea textArea;

	ImageButton opnieuwKnop;

	public Pagina23Panel(int x, int y, int b, int h, HeksInteractiePanel heip) 
	{
		super(x, y, b, h);
		this.heip = heip;
		// setBackground(new Color(255,255,220));
		setBackground(heip.bgColor);
		zetVastePlaats(true);

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
//		MediaTracker tr = new MediaTracker(this);
//		tr.addImage(heksnieuw, 0);
//		tr.addImage(opnieuwknop, 0);
//		try {
//			tr.waitForAll();
//		} catch (Exception e) {
//		}

//		ImageComponent heksNieuw = new ImageComponent(heksnieuw);
//		heksNieuw.setLocation(5, 0);
		// add(heksNieuw,0);

		//titelLabel = new ScLabel(10, 20, 290, 40, "Emmers met blokjes");
		// add(titelLabel);

		//uitleg = new ScTekstContainer(
		//		5,
		//		10,
		//		400,
		//		20,
		//		9,
		//		"Met een emmer kun je meer blokjes /tegelijk in de ketel doen of er uit halen. /De emmer in de ketel gebruik je om /blokjes uit de ketel te halen./De emmer boven de ketel gebruik je om /blokjes in de ketel te doen./Klik op de emmer en vul het aantal /blokjes in. Voor koude blokjes vul je/een negatief getal in.");

		// "Met een emmer kun je meer blokjes /tegelijk in de ketel doen of
		// eruit halen./ /
		// Klik op de emmer boven de ketel. Vul er een aantal warme of koude
		// blokjes in. Gooi een aantal emmers in de ketel en kijk wat er
		// gebeurt.
		// Klik op de emmer in de ketel. Vul er een aantal warme of koude
		// blokjes in. Haal een aantal emmers uit de ketel en kijk wat er
		// gebeurt.

		//uitleg.lijnUit(ScLabel.LINKS);
		// add(uitleg);

		oefenTafereelPanel = new Pagina23OefenPanel(180, 0, b - 200, h - 5, heip);
		oefenTafereelPanel.addActionListener(this);
		add(oefenTafereelPanel);

		//emmer = new EmmerPanel(150, 10, 110, 125, heip);
		emmer = new EmmerPanel(70, 10, 90, 105, heip);
		emmer.zetInstelbaar(false);
		add(emmer, 0);
		emmer.setVisible(false);

		//maalLabel = new ScLabel(100, 60, 50, 50, "X");
		maalLabel = new ScLabel(40, 58, 35, 35, "X");
		add(maalLabel);
		maalLabel.setVisible(false);

		//emmerTeller = new GetalComponent(50, 60, 50, 50);
		emmerTeller = new GetalComponent(0, 55, 55, 40);
		emmerTeller.zetWaarde(0);
		add(emmerTeller);
		emmerTeller.setVisible(false);

		//opdrachtTitel = new ScLabel(20, 200, 200, 40, "Opdracht");
		//opdrachtTitel.setVisible(false);
		// add(opdrachtTitel);

		//opdracht = new ScTekstContainer(5, 250, 285, 20, 4,
		//		"Als met de emmers werkt, dan kun je/vier soorten vermenigvuldigingen krijgen. /Leg uit welke vier en hoe je die krijgt.");
		//opdracht.lijnUit(ScLabel.LINKS);
		//opdracht.setVisible(false);
		// add(opdracht);

		//textArea = new ScTextArea(5, 340, 270, 150, 0, 0, TextArea.SCROLLBARS_VERTICAL_ONLY, "");
		//textArea.setVisible(false);
		// add(textArea);

		//opdrachtKnop = new ScLWButton(5, 510, 270, 35, "Maak de opdracht");
		//opdrachtKnop.addActionListener(this);
		// add(opdrachtKnop);

		//werkKnop = new ScLWButton(5, 510, 270, 35, "Werk met de emmers");
		//werkKnop.addActionListener(this);
		//textArea.setVisible(false);
		// add(werkKnop);

		opnieuwKnop = new ImageButton(opnieuwknop);
		opnieuwKnop.setBounds(20, 300, 90, 24);
		opnieuwKnop.addActionListener(this);
		add(opnieuwKnop, 0);

	}

	public void zetAlleenErin()
	{
//System.out.println("p23 zetAlleenErin()");

		oefenTafereelPanel.alleenErin = false;
		oefenTafereelPanel.alleenEruit = false;
		oefenTafereelPanel.zetOpnieuw();
		oefenTafereelPanel.emmerbinnen.setVisible(false);
		oefenTafereelPanel.emmerbuiten.setVisible(true);
		oefenTafereelPanel.alleenErin = true;
	}

	public void zetAlleenEruit()
	{
		
//System.out.println("p23 zetAlleenEruit");

		oefenTafereelPanel.alleenErin = false;
		oefenTafereelPanel.alleenEruit = false;
		oefenTafereelPanel.zetOpnieuw();
		oefenTafereelPanel.emmerbinnen.setVisible(true);
		oefenTafereelPanel.emmerbuiten.setVisible(false);
		oefenTafereelPanel.alleenEruit = true;
	}
	
	public void zetKeuzeErinEruit()
	{
		oefenTafereelPanel.alleenErin = false;
		oefenTafereelPanel.alleenEruit = false;
		oefenTafereelPanel.zetOpnieuw();
	}

	
	public void setState(Hashtable h) 
	{
//System.out.println("p23 setState");		
		boolean erinMogelijk = false;
		if (h.containsKey("erinmogelijk"))
			erinMogelijk = ((Boolean) h.get("erinmogelijk")).booleanValue();
		boolean eruitMogelijk = false;
		if (h.containsKey("eruitmogelijk"))
			eruitMogelijk = ((Boolean) h.get("eruitmogelijk")).booleanValue();
		int emmerInhoud = 0; 
		if (h.containsKey("emmerinhoud"))
			emmerInhoud = ((Integer) h.get("emmerinhoud")).intValue();
		oefenTafereelPanel.emmerInhoud = emmerInhoud;
		int aantalEmmers = 0; 
		if (h.containsKey("aantalemmers"))
			aantalEmmers = ((Integer) h.get("aantalemmers")).intValue();
		if (erinMogelijk)
		{	
//System.out.println("p23 setState erinMogelijk");			
			oefenTafereelPanel.zetErinMogelijk();
			oefenTafereelPanel.emmerbuiten.zetInhoud(emmerInhoud);
			oefenTafereelPanel.emmerbinnen.setVisible(false);
			oefenTafereelPanel.emmerSleep.zetInhoud(emmerInhoud);
//System.out.println("set emmerinhoud " + emmer.geefInhoud());
//System.out.println("emmerSleep " + oefenTafereelPanel.emmerSleep.etiket.isInstelbaar());
			oefenTafereelPanel.emmerbuiten.etiket.zetInstelbaar(false);
			oefenTafereelPanel.za.zetInhoud(emmerInhoud);
			oefenTafereelPanel.za.start();
			emmer.zetInhoud(emmerInhoud);
			emmerTeller.zetWaarde(aantalEmmers);
			if (aantalEmmers > 0)
			{	emmer.setVisible(true);
				maalLabel.setVisible(true);
				emmerTeller.setVisible(true);
			}	
			
		}
		else if (eruitMogelijk)
		{	
//System.out.println("p23 setState eruitMogelijk");			
			oefenTafereelPanel.zetEruitMogelijk();
			oefenTafereelPanel.emmerbinnen.zetInhoud(emmerInhoud);
			oefenTafereelPanel.emmerbuiten.setVisible(false);
			oefenTafereelPanel.emmerSleep.zetInhoud(emmerInhoud);
//System.out.println("set emmerinhoud " + emmer.geefInhoud());			
//System.out.println("emmerSleep " + oefenTafereelPanel.emmerSleep.etiket.isInstelbaar());			
			oefenTafereelPanel.emmerbinnen.etiket.zetInstelbaar(false);
			oefenTafereelPanel.za.zetInhoud(emmerInhoud);
			oefenTafereelPanel.za.start();
			emmer.zetInhoud(emmerInhoud);
			emmerTeller.zetWaarde(aantalEmmers);
			if (aantalEmmers < 0)
			{	emmer.setVisible(true);
				maalLabel.setVisible(true);
				emmerTeller.setVisible(true);
			}	

		}
		else
		{
			
		}
		int eindtemp = 0; 
		if (h.containsKey("eindtemp"))
			eindtemp = ((Integer) h.get("eindtemp")).intValue();
		oefenTafereelPanel.tc.zetWaarde(eindtemp);


	}

	public Hashtable getState() 
	{
		Hashtable h = new Hashtable();
		h.put("erinmogelijk", new Boolean(oefenTafereelPanel.erinMogelijk));
		h.put("eruitmogelijk", new Boolean(oefenTafereelPanel.eruitMogelijk));
		h.put("emmerinhoud", new Integer(oefenTafereelPanel.emmerInhoud));
//System.out.println("put emmerinhoud " + emmer.geefInhoud());		
		h.put("aantalemmers", new Integer(emmerTeller.geefWaarde()));
		h.put("eindtemp", new Integer(oefenTafereelPanel.geefTemp()));

		return h;
	}

	public double getScore() {
		if (textArea.getText() != null && textArea.getText().length() > 100)
			return 100;
		if (textArea.getText() != null && textArea.getText().length() > 5)
			return 10;
		return 0;
	}

	public void start() 
	{
		oefenTafereelPanel.zetOpnieuw();
		emmer.zetInhoud(0);
		emmer.setVisible(false);
		emmerTeller.zetWaarde(0);
		emmerTeller.setVisible(false);
		maalLabel.setVisible(false);
		// opnieuwKnop.setVisible(false);
	}

	public void stop() 
	{
		oefenTafereelPanel.stop();
	}

	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == opdrachtKnop) 
		{
			oefenTafereelPanel.zetOpnieuw();
			oefenTafereelPanel.zetActief(false);
			emmer.zetInhoud(0);
			emmer.setVisible(false);
			emmerTeller.zetWaarde(0);
			emmerTeller.setVisible(false);
			maalLabel.setVisible(false);
			opnieuwKnop.setVisible(false);
			opdrachtKnop.setVisible(false);

			opdracht.setVisible(true);
			textArea.setVisible(true);
			opdrachtTitel.setVisible(true);
			werkKnop.setVisible(true);
		} 
		else if (e.getSource() == werkKnop) 
		{
			opdrachtKnop.setVisible(true);
			oefenTafereelPanel.zetActief(true);

			opdracht.setVisible(false);
			textArea.setVisible(false);
			opdrachtTitel.setVisible(false);
			werkKnop.setVisible(false);
		} 
		else if (e.getSource() == opnieuwKnop) 
		{
			//if (opdracht.isVisible())
			//	return;
			oefenTafereelPanel.zetOpnieuw();
			emmer.zetInhoud(0);
			emmer.setVisible(false);
			emmerTeller.zetWaarde(0);
			emmerTeller.setVisible(false);
			maalLabel.setVisible(false);
			// opnieuwKnop.setVisible(false);
		} 
		else if (e.getSource() == oefenTafereelPanel) 
		{
			//if (opdracht.isVisible())
			//	return;
			opnieuwKnop.setVisible(true);
			if (e.getActionCommand().equals("erin")) {
				emmerTeller.verhoog();
				emmer.zetInhoud(oefenTafereelPanel.geefEmmerInhoud());
				emmer.setVisible(true);
				emmerTeller.setVisible(true);
				maalLabel.setVisible(true);
			} else if (e.getActionCommand().equals("eruit")) {
				emmerTeller.verlaag();
				emmer.zetInhoud(oefenTafereelPanel.geefEmmerInhoud());
				emmer.setVisible(true);
				emmerTeller.setVisible(true);
				maalLabel.setVisible(true);
			}

		}

	}
}
