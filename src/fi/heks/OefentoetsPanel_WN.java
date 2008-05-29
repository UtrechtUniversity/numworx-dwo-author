package fi.heks;

import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

import fi.heks.scobjects.*;
import fi.beans.appletutil.*;
import fi.heks.vectortek.*;

public class OefentoetsPanel_WN extends ScPanel implements ActionListener
{	
	AppletUtil au;

	Tekening schrijfheks;
	//ScLWButton  ;
	ImageButton kijkNaKnop,opnieuwKnop;
	ScLabel titelLabel, hulpLabel, nakijkLabel;//, scoreLabel;
	GetalComponent kerenHulp, kerenNagekeken;
	ScTekstContainer uitleg;
	OefenTafereelPanel oefenTafereelPanel;
	int aantalSommen;
	SomContainer[] sommen;
	int aantalPunten;
	double score;
	int soort;
	int aantalKerenNagekeken;
	
	private int aantalGoed, aantalFout;
	static DecimalFormatSymbols dfs;
	public static DecimalFormat df;
	public static FontMetrics fm;
	
	private Label  scoreLabel, aantalGoedLabel, aantalFoutLabel;
		
	public OefentoetsPanel_WN(int x, int y, int b, int h, Applet applet, int srt)
	{	super(x,y,b,h);
		//setBackground(new Color(255,255,220));
		setBackground(getBackground());
		zetVastePlaats(true);
		soort = srt;
		
		au = new AppletUtil(applet);
		
		Image controleerknop = null;
		Image volgendeknop = null;
		controleerknop = au.getImage("resources/controleerknop.gif");
		volgendeknop = au.getImage("resources/volgendeknop.gif");
		MediaTracker tr = new MediaTracker(this);
		tr.addImage(controleerknop,0);
		tr.addImage(volgendeknop,0);
		try{tr.waitForAll();} catch(Exception e) {}
		
		
		kijkNaKnop = new ImageButton(controleerknop);
		kijkNaKnop.setBounds(20,270,96,24);
		kijkNaKnop.addActionListener(this);
		add(kijkNaKnop);
		
		//kijkNaKnop = new ScLWButton(40,400,140,25,Heks.rb.getString("kijkNaKnopLabel"));
		//kijkNaKnop.addActionListener(this);
		//add(kijkNaKnop);
		
		opnieuwKnop = new ImageButton(volgendeknop);
		opnieuwKnop.setBounds(20,300,96,24);
		opnieuwKnop.addActionListener(this);
		add(opnieuwKnop);
		
		schrijfheks = new Tekening(30,70,160,160,au,"schrijfheks.gif");
		//add(schrijfheks);
		
		if(soort == Som.MIN)titelLabel = new ScLabel(20,20,200,40,"Oefentoets 2");
		else if(soort == Som.PLUSMIN)titelLabel = new ScLabel(20,20,200,40,"Oefentoets 3");
		else if(soort == Som.MAAL)titelLabel = new ScLabel(20,20,200,40,"Oefentoets 5");
		else titelLabel = new ScLabel(20,20,300,46,"Typ je antwoord in");
		//add(titelLabel);
		
		hulpLabel = new ScLabel(550,260,170,20,Heks.rb.getString("aantalHulpLabel"));
		if(Heks.rb.getLocale().toString().equals("en")) hulpLabel = new ScLabel(530,260,160,20,Heks.rb.getString("aantalHulpLabel"));
		//add(hulpLabel);
		
		kerenHulp = new GetalComponent(720,260,20,20);
		if(Heks.rb.getLocale().toString().equals("en")) kerenHulp = new GetalComponent(610,260,20,20);
		kerenHulp.zetWaarde(0);
		//add(kerenHulp);
		
		nakijkLabel = new ScLabel(550,300,170,20,Heks.rb.getString("aantalNakijkLabel"));
		nakijkLabel.setVisible(false);
		//add(nakijkLabel);
		
		kerenNagekeken = new GetalComponent(720,300,20,20);
		kerenNagekeken.zetWaarde(0);
		kerenNagekeken.setVisible(false);
		//add(kerenNagekeken);
		
		
			
		
		//scoreLabel = new ScLabel(550,350,220,40,"");
		//scoreLabel.setVisible(false);
		//add(scoreLabel);
		
		
		uitleg = new ScTekstContainer(250,70,270,20,9,
									  Heks.rb.getString("OefenToetsPanelUitleg"));
		
 		uitleg.lijnUit(ScLabel.LINKS);
		//add(uitleg);
		
		oefenTafereelPanel = new OefenTafereelPanel(530,10,240,240,applet);
		oefenTafereelPanel.zetInstelbaar(true);
		//add(oefenTafereelPanel);
		
		aantalSommen = 20;
		sommen = new SomContainer[aantalSommen];
		for(int i=0 ; i<10 ; i++)
		{	sommen[i] = new SomContainer(300,130+50*i,350,46, applet, soort, true);
			sommen[i].addActionListener(this);
			add(sommen[i]);
		}
		for(int i=10 ; i<aantalSommen ; i++)
		{	sommen[i] = new SomContainer(650,130+50*(i-10),350,46, applet, soort, true);
			sommen[i].addActionListener(this);
			add(sommen[i]);
		}
		
		dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator(',');
        df = new DecimalFormat("0.#", dfs);
        
        String s1 = "aantal opdrachten: 20" + "   |   " + "goed:";
        String s2 = "             |   "  + "fout:";
        scoreLabel = new Label(s1 + s2);
		scoreLabel.setBounds(240,15,300,24);
		scoreLabel.setFont(new Font("Verdana",Font.PLAIN,12));
		add(scoreLabel);
		
		
		//scoreLabel.setVisible(false);
		FontMetrics fm = getFontMetrics(new Font("Verdana",Font.PLAIN,12));
        int scoreLength1 = 210 + fm.stringWidth(s1);
        int scoreLength2 = 185 + fm.stringWidth(s1+s2);
        
        aantalGoedLabel = new Label(""+df.format(aantalGoed));
        aantalGoedLabel.setBounds(scoreLength1,15,36,24);
        aantalGoedLabel.setFont(new Font("Verdana",Font.BOLD,12));
        aantalGoedLabel.setForeground(new Color(0,150,0));
        add(aantalGoedLabel,0);
        
        aantalFoutLabel = new Label(""+df.format(aantalFout));
        aantalFoutLabel.setBounds(scoreLength2,15,36,24);
        aantalFoutLabel.setFont(new Font("Verdana",Font.BOLD,12));
        aantalFoutLabel.setForeground(new Color(255,0,0));
        add(aantalFoutLabel,0);
				
	}
	
	public void paint(Graphics gr)
	{	Graphics g = (Graphics2D)gr;
    	((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
    	super.paint(g);
    	
    	g.setFont(new Font("SansSerif", Font.BOLD, 16));
    	g.drawString("Typ je antwoord in", 10, 30);
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
		//scoreLabel.setLabel("Score: " + Double.toString(score));
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
			score = Math.max(0 , 0.5*(aantalPunten));
			//scoreLabel.setLabel("Score: " + Double.toString(score));
			//scoreLabel.setVisible(true);
			if(score<0)score=0;
			aantalGoedLabel.setText(""+df.format((double)score));
			aantalFoutLabel.setText(""+df.format(20-(double)score));
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
			//scoreLabel.setVisible(false);
			aantalGoedLabel.setText("0");
			aantalFoutLabel.setText("0");
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
