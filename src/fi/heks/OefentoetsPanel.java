package fi.heks;

import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import java.util.*;
import fi.heks.scobjects.*;
import fi.beans.appletutil.*;
import fi.heks.vectortek.*;

public class OefentoetsPanel extends ScPanel implements ActionListener
{	
	AppletUtil au;

	Tekening schrijfheks;
	ScLWButton kijkNaKnop, opnieuwKnop;
	ScLabel titelLabel, hulpLabel, nakijkLabel, scoreLabel;
	GetalComponent kerenHulp, kerenNagekeken;
	ScTekstContainer uitleg;
	OefenTafereelPanel oefenTafereelPanel;
	int aantalSommen;
	SomContainer[] sommen;
	int aantalPunten;
	double score;
	int soort;
	int aantalKerenNagekeken;
		
	public OefentoetsPanel(int x, int y, int b, int h, Applet applet, int srt)
	{	super(x,y,b,h);
		//setBackground(new Color(255,255,220));
		setBackground(getBackground());
		zetVastePlaats(true);
		soort = srt;
		
		au = new AppletUtil(applet);
		
		kijkNaKnop = new ScLWButton(590,400,140,25,Heks.rb.getString("kijkNaKnopLabel"));
		kijkNaKnop.addActionListener(this);
		add(kijkNaKnop);
		
		opnieuwKnop = new ScLWButton(590,450,140,25,Heks.rb.getString("nieuwToetsKnopLabel"));
		opnieuwKnop.addActionListener(this);
		add(opnieuwKnop);
		
		schrijfheks = new Tekening(30,70,160,160,au,"schrijfheks.gif");
		add(schrijfheks);
		
		if(soort == Som.MIN)titelLabel = new ScLabel(20,20,200,40,"Oefentoets 2");
		else if(soort == Som.PLUSMIN)titelLabel = new ScLabel(20,20,200,40,"Oefentoets 3");
		else if(soort == Som.MAAL)titelLabel = new ScLabel(20,20,200,40,"Oefentoets 5");
		else titelLabel = new ScLabel(20,20,200,40,"Oefentoets 1");
		//add(titelLabel);
		
		hulpLabel = new ScLabel(550,260,170,20,Heks.rb.getString("aantalHulpLabel"));
		if(Heks.rb.getLocale().toString().equals("en")) hulpLabel = new ScLabel(530,260,160,20,Heks.rb.getString("aantalHulpLabel"));
		add(hulpLabel);
		
		kerenHulp = new GetalComponent(720,260,20,20);
		if(Heks.rb.getLocale().toString().equals("en")) kerenHulp = new GetalComponent(610,260,20,20);
		kerenHulp.zetWaarde(0);
		add(kerenHulp);
		
		nakijkLabel = new ScLabel(550,300,170,20,Heks.rb.getString("aantalNakijkLabel"));
		nakijkLabel.setVisible(false);
		add(nakijkLabel);
		
		kerenNagekeken = new GetalComponent(720,300,20,20);
		kerenNagekeken.zetWaarde(0);
		kerenNagekeken.setVisible(false);
		add(kerenNagekeken);
		
		
			
		
		scoreLabel = new ScLabel(550,350,220,40,"");
		scoreLabel.setVisible(false);
		add(scoreLabel);
		
		
		uitleg = new ScTekstContainer(250,70,270,20,9,
									  Heks.rb.getString("OefenToetsPanelUitleg"));
		
 		uitleg.lijnUit(ScLabel.LINKS);
		add(uitleg);
		
		oefenTafereelPanel = new OefenTafereelPanel(530,10,240,240,applet);
		oefenTafereelPanel.zetInstelbaar(true);
		add(oefenTafereelPanel);
		
		aantalSommen = 20;
		sommen = new SomContainer[aantalSommen];
		for(int i=0 ; i<10 ; i++)
		{	sommen[i] = new SomContainer(50,270+25*i,200,25, applet, soort, true);
			sommen[i].addActionListener(this);
			add(sommen[i]);
		}
		for(int i=10 ; i<aantalSommen ; i++)
		{	sommen[i] = new SomContainer(280,270+25*(i-10),200,25, applet, soort, true);
			sommen[i].addActionListener(this);
			add(sommen[i]);
		}
				
	}
	
	public void setState(Hashtable h)
	{	int aantalSommen = ((Integer)h.get("aantalSommen")).intValue();
		boolean scoreVisible = ((Boolean)h.get("scoreVisible")).booleanValue();
		int aantalPunten = ((Integer)h.get("aantalPunten")).intValue();
		int aantalKerenHulp = ((Integer)h.get("aantalKerenHulp")).intValue();
		int aantalKerenNagekeken = ((Integer)h.get("aantalKerenNagekeken")).intValue();
		Hashtable[] somStates = (Hashtable[])h.get("somStates");
		
		this.aantalSommen = aantalSommen;
		scoreLabel.setVisible(scoreVisible);
		oefenTafereelPanel.zetGebruikt(aantalKerenHulp);
		this.aantalPunten = aantalPunten;
		this.aantalKerenNagekeken = aantalKerenNagekeken;
		kerenHulp.zetWaarde(oefenTafereelPanel.geefGebruikt());
		kerenNagekeken.zetWaarde(aantalKerenNagekeken);
		score = Math.max(0 , 1.0*(aantalPunten - oefenTafereelPanel.geefGebruikt())/aantalSommen*5);
		scoreLabel.setLabel("Score: " + Double.toString(score));
		scoreLabel.setVisible(scoreVisible);
		for(int i=0 ; i<aantalSommen; i++)
	    {	sommen[i].setState(somStates[i]);
	    }
	    if(aantalKerenNagekeken > 0)
		{	//nakijkLabel.setVisible(true);
			//kerenNagekeken.setVisible(true);
		}
	}
	
	public Hashtable getState()
	{	int aantalSommen = 0;
		boolean scoreVisible = false;
		int aantalPunten = 0;
		int aantalKerenHulp = 0;
		int aantalKerenNagekeken = 0;
		Hashtable[] somStates = null;
		
		aantalSommen = this.aantalSommen;
		aantalPunten = this.aantalPunten;
		aantalKerenHulp = oefenTafereelPanel.geefGebruikt();
		somStates = new Hashtable[aantalSommen];
		aantalKerenNagekeken = this.aantalKerenNagekeken;
		scoreVisible = aantalKerenNagekeken>0 ;
		for(int i=0 ; i<aantalSommen; i++)
	    {	somStates[i] = sommen[i].getState();
	    }
	    
	    Hashtable h = new Hashtable();
	    h.put("aantalSommen", new Integer(aantalSommen));
	    h.put("scoreVisible", new Boolean(scoreVisible));
	    h.put("aantalPunten", new Integer(aantalPunten));
	    h.put("aantalKerenHulp", new Integer(aantalKerenHulp));
	    h.put("aantalKerenNagekeken", new Integer(aantalKerenNagekeken));
	    h.put("somStates", somStates);
	    return h;
	}
	
	public double getScore()
	{	return score*10;
	}
	
	/*public void start()
	{	int vorigeTerm1 = 0;
		for(int i=0 ; i<aantalSommen ; i++)
		{	sommen[i].vernieuw();
			sommen[i].geefTerm1();
			while(sommen[i].geefTerm1()==vorigeTerm1)
			{sommen[i].vernieuw();
			}
			vorigeTerm1 = sommen[i].geefTerm1();
		}
		oefenTafereelPanel.zetGebruikt(0);
		scoreLabel.setVisible(false);
		oefenTafereelPanel.zetActief(false);
		kerenHulp.zetWaarde(0);
	}*/
	
	public void start()
	{	int vorigeTerm1 = 0;
		for(int i=0 ; i<aantalSommen ; i++)
		{	Som s = new Som(soort);
			while(s.geefTerm1()==vorigeTerm1)
			{	s = new Som(soort);
			}
			vorigeTerm1 = s.geefTerm1();
			sommen[i].zetSom(s);
		}
		oefenTafereelPanel.zetGebruikt(0);
		//scoreLabel.setVisible(false);
		oefenTafereelPanel.zetActief(false);
		kerenHulp.zetWaarde(0);
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getSource()==kijkNaKnop)
		{	requestFocus();
			aantalKerenNagekeken++;
			aantalPunten = 0;
			for(int i=0 ; i<aantalSommen ; i++)
			{	if(sommen[i].evalueer())aantalPunten = aantalPunten+2;
			}
			score = Math.max(0 , 1.0*(aantalPunten - oefenTafereelPanel.geefGebruikt())/aantalSommen*5);
			scoreLabel.setLabel("Score: " + Double.toString(score));
			scoreLabel.setVisible(true);
		}
		else if(e.getSource()==opnieuwKnop)
		{	requestFocus();
			aantalKerenNagekeken =0;
			int vorigeTerm1 = 0;
			for(int i=0 ; i<aantalSommen ; i++)
			{	Som s = new Som(soort);
				while(s.geefTerm1()==vorigeTerm1)
				{	s = new Som(soort);
				}
				vorigeTerm1 = s.geefTerm1();
				sommen[i].zetSom(s);
			}
			oefenTafereelPanel.zetGebruikt(0);
			scoreLabel.setVisible(false);
		}
		oefenTafereelPanel.zetActief(false);
		kerenHulp.zetWaarde(oefenTafereelPanel.geefGebruikt());
		kerenNagekeken.zetWaarde(aantalKerenNagekeken);
		if(aantalKerenNagekeken > 0)
		{	//nakijkLabel.setVisible(true);
			//kerenNagekeken.setVisible(true);
		}
		else
		{	nakijkLabel.setVisible(false);
			kerenNagekeken.setVisible(false);
		}
		
	}
	
}
