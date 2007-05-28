package fi.heks;

import java.awt.*;
import java.util.*;
import java.applet.*;
import java.awt.event.*;
import fi.heks.scobjects.*;
import fi.beans.appletutil.*;
import fi.heks.vectortek.*;

public class BlokjesErbijPanel extends ScPanel implements ActionListener
{	
	AppletUtil au;

	Tekening  potErin, potEruit, beginPot, eindPot, pijl, schrijfheks;
	GetalComponent beginTemp, eindTemp, term1, term2, uitkomst;
	BlokjesContainer erinContainer, erinOefenContainer;
	ScLWButton volgendeKnop, opnieuwKnop, helemaalOpnieuwKnop;
	ScLabel  plusMinLabel,  isLabel, goedFoutLabel, erinLabel,erinOefenLabel, wordtLabel, titelLabel;
	ScTekstContainer beginLabel, eindLabel, aantalBlLabel, uitleg;
	OefenTafereelPanel oefenTafereelPanel;
	ScContainer tabelContainer;
	KnipperComponent vraagteken;
	TabelComponent tabel;
	Som huidigeSom;
	ScLabel scoreLabel;
	int score;
	int aantalPunten;
	
	
	public BlokjesErbijPanel(int x, int y, int b, int h, Applet applet)
	{	super(x,y,b,h);
		//setBackground(new Color(255,255,220));
		setBackground(getBackground());
		zetVastePlaats(true);
		
		
		au = new AppletUtil(applet);
		
		volgendeKnop = new ScLWButton(80,480,160,25,Heks.rb.getString("volgendeKnopLabel"));
		volgendeKnop.addActionListener(this);
		volgendeKnop.setVisible(false);
		add(volgendeKnop);
		
		opnieuwKnop = new ScLWButton(110,400,100,25,Heks.rb.getString("opnieuwKnopLabel"));
		opnieuwKnop.addActionListener(this);
		add(opnieuwKnop);
		opnieuwKnop.setVisible(false);
		
		helemaalOpnieuwKnop = new ScLWButton(80,480,160,25,Heks.rb.getString("helemaalOpnieuwKnopLabel"));
		helemaalOpnieuwKnop.addActionListener(this);
		add(helemaalOpnieuwKnop);
		helemaalOpnieuwKnop.setVisible(false);
		
		erinOefenContainer = new BlokjesContainer(430,350,110,85,applet);
		erinOefenContainer.toonAlsGetal(true);
		add(erinOefenContainer);
		
		schrijfheks = new Tekening(30,70,160,160,au,"schrijfheks.gif");
		add(schrijfheks);
		
		goedFoutLabel = new ScLabel(110,430,100,40, "");
		add(goedFoutLabel);
		
		titelLabel = new ScLabel(20,20,200,40,Heks.rb.getString("blokjesErinTitelLabel"));
		//add(titelLabel);	
		
		tabelContainer = new ScContainer(230,5,550,300);
		
		vraagteken = new KnipperComponent(470,130,40,40,"?");
		vraagteken.start();
		tabelContainer.add(vraagteken);
		
		beginTemp = new GetalComponent(30,130,60,30);
		beginTemp.zetAlsTemp(true);
		tabelContainer.add(beginTemp);
		
		beginLabel = new ScTekstContainer(2,30,110,25,2,Heks.rb.getString("beginTempLabel"));
		tabelContainer.add(beginLabel);
		
		beginPot = new Tekening(10,110,90,80,au,"potzwart.gif");
		tabelContainer.add(beginPot);
		
		term1 = new GetalComponent(30,210,60,40);
		tabelContainer.add(term1);
				
		erinLabel = new ScLabel(135,50,60,25,Heks.rb.getString("erinLabel"));
		tabelContainer.add(erinLabel);
		
		erinOefenLabel = new ScLabel(415,310,100,35,Heks.rb.getString("erinOefenLabel"));
		erinOefenLabel.setVisible(false);
		add(erinOefenLabel);
		
		aantalBlLabel = new ScTekstContainer(220,20,110,25,3,Heks.rb.getString("aantalWBlokjesLabel"));
		tabelContainer.add(aantalBlLabel);
		
		//wordtLabel = new ScLabel(355,50,60,25,"wordt");
		//tabelContainer.add(wordtLabel);
		
		potErin = new Tekening(115,105,100,90,au,"potErin.gif");
		tabelContainer.add(potErin);
		
		//pijl = new Tekening(335,105,100,90,au,"pijl.gif");
		//tabelContainer.add(pijl);
		
		plusMinLabel = new ScLabel(135,210,60,40,Heks.rb.getString("plusTeken"));
		tabelContainer.add(plusMinLabel);
		
		eindTemp = new GetalComponent(460,130,60,30);
		eindTemp.zetBekend(false);
		eindTemp.zetInstelbaar(true);
		eindTemp.zetLeeg(true);
		eindTemp.zetAlsTemp(true);
		eindTemp.addActionListener(this);
		tabelContainer.add(eindTemp);
		
		eindLabel = new ScTekstContainer(440,30,110,25,2,Heks.rb.getString("eindTempLabel"));
		tabelContainer.add(eindLabel);
		
		eindPot = new Tekening(445,110,90,80,au,"potzwart.gif");
		tabelContainer.add(eindPot);
		
		uitkomst = new GetalComponent(465,210,60,40);
		uitkomst.zetBekend(false);
		uitkomst.zetInstelbaar(true);
		uitkomst.addActionListener(this);
		tabelContainer.add(uitkomst);
		
		erinContainer = new BlokjesContainer(225,107,100,85,applet);
		tabelContainer.add(erinContainer);
				
		term2 = new GetalComponent(245,210,60,40);
		tabelContainer.add(term2);
		
		isLabel = new ScLabel(355,210,60,40, Heks.rb.getString("isTeken"));
		tabelContainer.add(isLabel);
		
		tabel = new TabelComponent(3,5,0,0,550,300, false);
		tabelContainer.add(tabel);
		
		add(tabelContainer);		
		
		uitleg = new ScTekstContainer(45,310,400,20,4,Heks.rb.getString("blokjesErbijUitleg"));
		//"Bereken het antwoord van de opdracht hierboven./Vul je antwoord in bij het vraagteken./Je mag de ketel gebruiken om het antwoord te vinden./Zorg voor een score van 10 punten.");
		



		uitleg.lijnUit(ScLabel.LINKS);
		add(uitleg);
		
		oefenTafereelPanel = new OefenTafereelPanel(540,285,240,240,applet);
		oefenTafereelPanel.addActionListener(this);
		oefenTafereelPanel.zetEruitMogelijk(false);
		add(oefenTafereelPanel);
		
		huidigeSom = new Som(Som.PLUS);
		zetSom(huidigeSom);
		oefenTafereelPanel.zetBeginTemp(huidigeSom.geefTerm1());
		
		scoreLabel = new ScLabel(30,220,150,40,"test");
		scoreLabel.setLabel(Heks.rb.getString("scoreLabel") + Integer.toString(score));
		add(scoreLabel);
		
	}
	
	public void setState(Hashtable h)
	{	int aantalPunten = ((Integer)h.get("aantalPunten")).intValue();
				
		this.aantalPunten = aantalPunten;
		score = 2*aantalPunten;
		scoreLabel.setLabel("Score: " + Integer.toString(score));
		if(score==10)helemaalOpnieuwKnop.setVisible(true);
	}
	
	public Hashtable getState()
	{	int aantalPunten = 0;
			
		aantalPunten = this.aantalPunten;
		
		Hashtable h = new Hashtable();
	    h.put("aantalPunten", new Integer(aantalPunten));
	    
	    return h;
	}
	
	public double getScore()
	{	return (double)score*10;
	}
	
	public void start()
	{	erinContainer.removeAll();
		huidigeSom = new Som(Som.PLUS);
		zetSom(huidigeSom);
		oefenTafereelPanel.zetBeginTemp(huidigeSom.geefTerm1());
		vraagteken.setVisible(true);
		eindTemp.zetInstelbaar(true);
		erinOefenContainer.removeAll();
		erinOefenLabel.setVisible(false);
		volgendeKnop.setVisible(false);
		opnieuwKnop.setVisible(false);
	}
	
	public void zetSom(Som som)
	{	goedFoutLabel.setLabel("");
		beginTemp.zetWaarde(som.geefTerm1());
		erinContainer.removeAll();
		for(int i=0 ; i<Math.abs(som.geefTerm2()) ; i++)
		{	erinContainer.voegBlokjeToe(som.geefTerm2()>0);
		}
		eindTemp.zetBekend(false);
		eindTemp.repaint();
		term1.zetWaarde(som.geefTerm1());
		term2.zetWaarde(som.geefTerm2());
		String op = som.geefOperator();
		plusMinLabel.setLabel(op);
		if(term2.geefWaarde()>0) aantalBlLabel.setText(Heks.rb.getString("aantalWBlokjesLabel"));
		else aantalBlLabel.setText(Heks.rb.getString("aantalKBlokjesLabel"));
		uitkomst.zetBekend(false);
		vraagteken.setVisible(true);
		uitkomst.repaint();
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getSource()==volgendeKnop)
		{	huidigeSom = new Som(Som.PLUS);
			zetSom(huidigeSom);
			oefenTafereelPanel.zetBeginTemp(huidigeSom.geefTerm1());
			erinOefenContainer.removeAll();
			erinOefenLabel.setVisible(false);
			volgendeKnop.setVisible(false);
			eindTemp.zetInstelbaar(true);
			uitkomst.zetInstelbaar(true);
		}
		else if(e.getSource()==helemaalOpnieuwKnop)
		{	huidigeSom = new Som(Som.PLUS);
			zetSom(huidigeSom);
			oefenTafereelPanel.zetBeginTemp(huidigeSom.geefTerm1());
			erinOefenContainer.removeAll();
			erinOefenLabel.setVisible(false);
			volgendeKnop.setVisible(false);
			eindTemp.zetInstelbaar(true);
			uitkomst.zetInstelbaar(true);
			aantalPunten = 0;
			score = 2*aantalPunten;
			scoreLabel.setLabel(Heks.rb.getString("scoreLabel") + Integer.toString(score));
			helemaalOpnieuwKnop.setVisible(false);
		}
		else if(e.getSource()==opnieuwKnop)
		{	zetSom(huidigeSom);
			oefenTafereelPanel.zetBeginTemp(huidigeSom.geefTerm1());
			erinOefenContainer.removeAll();
			erinOefenLabel.setVisible(false);
			opnieuwKnop.setVisible(false);
			eindTemp.zetInstelbaar(true);
			uitkomst.zetInstelbaar(true);
			
			
		}
		else if(e.getSource()==eindTemp || e.getSource()==uitkomst)
		{	if(e.getSource()==eindTemp)
			{	int w = eindTemp.geefWaarde();
				if(w!=-999)
				{	uitkomst.zetWaarde(eindTemp.geefWaarde());
				}
			}
			else if(e.getSource()==uitkomst)
			{	int w = uitkomst.geefWaarde();
				if(w!=-999)
				{	eindTemp.zetWaarde(uitkomst.geefWaarde());
				}
			}
			else return;
				
			if(e.getActionCommand().equals("focuslost")
				//|| e.getActionCommand().equals("action") 
				//&& System.getProperties().getProperty("java.vendor").equals("Microsoft Corp.")
				)
			{
				boolean b = huidigeSom.evalueer(uitkomst.geefWaarde());
				if(b)
				{	goedFoutLabel.setForeground(new Color(0,150,0));
					goedFoutLabel.setLabel(Heks.rb.getString("goedLabel"));
					vraagteken.setVisible(false);
					opnieuwKnop.setVisible(false);
					
					if(score<10)aantalPunten++;
					score = 2*aantalPunten;
					scoreLabel.setLabel(Heks.rb.getString("scoreLabel") + Integer.toString(score));
					
					if(score<10)volgendeKnop.setVisible(true);
					if(score==10)helemaalOpnieuwKnop.setVisible(true);
				}
				else 
				{	goedFoutLabel.setForeground(new Color(255,0,0));
					goedFoutLabel.setLabel(Heks.rb.getString("foutLabel"));
					vraagteken.setVisible(false);
					opnieuwKnop.setVisible(true);
					
					if(aantalPunten>0)aantalPunten--;
					score = 2*aantalPunten;
					scoreLabel.setLabel(Heks.rb.getString("scoreLabel") + Integer.toString(score));
				}
				eindTemp.zetInstelbaar(false);
				uitkomst.zetInstelbaar(false);
			
			}
		}
		else if(e.getSource()==oefenTafereelPanel)
		{	erinOefenLabel.setVisible(true);
			opnieuwKnop.setVisible(true);
			if(e.getActionCommand().equals("plusErin"))
			{	erinOefenContainer.voegBlokjeToe(true);
			}
			if(e.getActionCommand().equals("minErin"))
			{	erinOefenContainer.voegBlokjeToe(false);
			}
		}
	}
	
}
