package fi.heks;

import java.awt.*;
import java.util.*;
import java.applet.*;
import java.awt.event.*;
import fi.heks.scobjects.*;
import fi.beans.appletutil.*;
import fi.heks.vectortek.*;

public class BlokjesMaalPanelExtra extends ScPanel implements ActionListener
{	
	AppletUtil au;

	Tekening  potErin, potEruit, beginPot, eindPot, pijl, schrijfheks;
	GetalComponent beginTemp, eindTemp, term1, term2, uitkomst;
	BlokjesContainer erinContainer, erinOefenContainer;
	ScLWButton volgendeKnop, opnieuwKnop, helemaalOpnieuwKnop;
	ScLabel  maalLabel,  isLabel, goedFoutLabel, erinLabel,erinOefenLabel, wordtLabel, titelLabel;
	ScTekstContainer beginLabel, eindLabel, aantalBlLabel, uitleg, beginKeerLabel;
	OefenTafereelPanelEmmer oefenTafereelPanelEmmer;
	ScContainer tabelContainer;
	KnipperComponent vraagteken;
	TabelComponent tabel;
	Som huidigeSom;
	ScLabel scoreLabel;
	int score;
	int aantalPunten;
	String[] getalWoorden = {Heks.rb.getString("int0Label"),
			Heks.rb.getString("int1Label"),
			Heks.rb.getString("int2Label"),
			Heks.rb.getString("int3Label"),
			Heks.rb.getString("int4Label"),
			Heks.rb.getString("int5Label"),
			Heks.rb.getString("int6Label"),
			Heks.rb.getString("int7Label"),
			Heks.rb.getString("int8Label"),
			Heks.rb.getString("int9Label")};
	
	Emmer emmer;
	
	ScPanel afdekking;
	
	
	
	public BlokjesMaalPanelExtra(int x, int y, int b, int h, Applet applet)
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
		
		afdekking = new ScPanel(200,0,600,179);
		add(afdekking,0);
		
		schrijfheks = new Tekening(30,70,160,160,au,"schrijfheks.gif");
		add(schrijfheks);
		
		goedFoutLabel = new ScLabel(110,430,800,40, "");
		goedFoutLabel.lijnUit(ScLabel.LINKS);
		add(goedFoutLabel);
		
		titelLabel = new ScLabel(20,20,200,40,"Werken met emmers");
		//add(titelLabel);	
		
		tabelContainer = new ScContainer(230,55,550,250);
		
		aantalBlLabel = new ScTekstContainer(220,15,110,25,3,Heks.rb.getString("aantalWBlokjesLabel"));
		tabelContainer.add(aantalBlLabel);
		
		beginKeerLabel = new ScTekstContainer(2,30,110,25,2,"");
		tabelContainer.add(beginKeerLabel);
		
		term1 = new GetalComponent(30,150,60,40);
		tabelContainer.add(term1);
				
		maalLabel = new ScLabel(135,150,60,40,"x");
		tabelContainer.add(maalLabel);
		
		
		eindLabel = new ScTekstContainer(440,30,110,25,2,Heks.rb.getString("tempVeranderingLabel"));
		tabelContainer.add(eindLabel);
		
		uitkomst = new GetalComponent(465,150,60,40);
		uitkomst.zetBekend(false);
		uitkomst.zetInstelbaar(true);
		uitkomst.addActionListener(this);
		tabelContainer.add(uitkomst);
		
		emmer = new Emmer(250,70,60,50,applet);
		emmer.zetInstelbaar(false);
		tabelContainer.add(emmer);
				
		term2 = new GetalComponent(245,150,60,40);
		tabelContainer.add(term2);
		
		isLabel = new ScLabel(355,150,60,40, "=");
		tabelContainer.add(isLabel);
		
		tabel = new TabelComponent(2,5,0,0,550,250, true);
		tabelContainer.add(tabel);
		
		add(tabelContainer);		
		
		uitleg = new ScTekstContainer(45,310,400,20,3,Heks.rb.getString("BlokjesMaalPanelUitleg"));
		



		uitleg.lijnUit(ScLabel.LINKS);
		add(uitleg);
		
		
		huidigeSom = new Som(Som.MAAL);
		zetSom(huidigeSom);
		
		scoreLabel = new ScLabel(30,220,150,40,"test");
		scoreLabel.setLabel(Heks.rb.getString("scoreLabel") + Integer.toString(score));
		add(scoreLabel);
		
	}
	
	public void setState(Hashtable h)
	{	int aantalPunten = ((Integer)h.get("aantalPunten")).intValue();
				
		this.aantalPunten = aantalPunten;
		score = 2*aantalPunten;
		scoreLabel.setLabel(Heks.rb.getString("scoreLabel") + Integer.toString(score));
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
	{	huidigeSom = new Som(Som.MAAL);
		zetSom(huidigeSom);
		volgendeKnop.setVisible(false);
		opnieuwKnop.setVisible(false);
	}
	
	public void zetSom(Som som)
	{	goedFoutLabel.setLabel("");
		boolean teken = som.geefTerm1()>0;
		if(teken)beginKeerLabel.setText("" + getalWoorden[som.geefTerm1()] + Heks.rb.getString("keerErbijLabel"));
		else beginKeerLabel.setText("" + getalWoorden[-som.geefTerm1()] + Heks.rb.getString("keerEruitLabel"));
		teken = som.geefTerm2()>0;
		if(teken)aantalBlLabel.setText("" + getalWoorden[som.geefTerm2()] + Heks.rb.getString("warmeBlokjesLabel"));
		else aantalBlLabel.setText("" + getalWoorden[-som.geefTerm2()] + Heks.rb.getString("koudeBlokjesLabel"));
		emmer.zetInhoud(som.geefTerm2());
		term1.zetWaarde(som.geefTerm1());
		term2.zetWaarde(som.geefTerm2());
		String op = som.geefOperator();
		maalLabel.setLabel(op);
		uitkomst.zetBekend(false);
		uitkomst.repaint();
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getSource()==volgendeKnop)
		{	huidigeSom = new Som(Som.MAAL);
			zetSom(huidigeSom);
			afdekking.setVisible(true);
			volgendeKnop.setVisible(false);
		}
		else if(e.getSource()==helemaalOpnieuwKnop)
		{	huidigeSom = new Som(Som.MAAL);
			zetSom(huidigeSom);
			volgendeKnop.setVisible(false);
			aantalPunten = 0;
			score = 2*aantalPunten;
			scoreLabel.setLabel(Heks.rb.getString("scoreLabel") + Integer.toString(score));
			afdekking.setVisible(true);
			helemaalOpnieuwKnop.setVisible(false);
		}
		else if(e.getSource()==opnieuwKnop)
		{	zetSom(huidigeSom);
			opnieuwKnop.setVisible(false);
		}
		else if(e.getSource()==uitkomst)
		{	int w = uitkomst.geefWaarde();
			
				
			if(e.getActionCommand().equals("focuslost")
				//|| e.getActionCommand().equals("action") 
				//&& System.getProperties().getProperty("java.vendor").equals("Microsoft Corp.")
				)
			{
				boolean b = huidigeSom.evalueer(uitkomst.geefWaarde());
				if(b)
				{	goedFoutLabel.setForeground(new Color(0,150,0));
					goedFoutLabel.setLabel(Heks.rb.getString("goedLabel"));
					afdekking.setVisible(true);
					opnieuwKnop.setVisible(false);
					
					if(score<10)aantalPunten++;
					score = 2*aantalPunten;
					scoreLabel.setLabel(Heks.rb.getString("scoreLabel") + Integer.toString(score));
					
					if(score<10)volgendeKnop.setVisible(true);
					if(score==10)
					{	helemaalOpnieuwKnop.setVisible(true);
						goedFoutLabel.setLabel(Heks.rb.getString("klaarFeedbackLabel"));
					}
				}
				else 
				{	goedFoutLabel.setForeground(new Color(255,0,0));
					goedFoutLabel.setLabel(Heks.rb.getString("foutFeedbackMaal"));
					afdekking.setVisible(false);
					opnieuwKnop.setVisible(true);
					
					if(aantalPunten>0)aantalPunten--;
					score = 2*aantalPunten;
					scoreLabel.setLabel(Heks.rb.getString("scoreLabel") + Integer.toString(score));
				}
			
			}
		}
		
	}
	
}
