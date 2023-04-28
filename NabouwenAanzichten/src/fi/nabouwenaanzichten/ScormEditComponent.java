package fi.nabouwenaanzichten;

import java.applet.Applet;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import fi.nabouwenaanzichten.text.*;
import fi.beans.scorm.*;
import fi.beans.base64code.*;

public class ScormEditComponent extends Panel implements ActionListener, ScormEditComponentIF, NabouwenAanzichtenIF
{
	
	private Image im;
  	private Graphics gIm;
  	private boolean resized = false;
  	

	private Button volLeegKnop;
	Viewer3d   vWerk;
	private Viewer3d  vVoorbeeld0;
	private VaktekPanel  vVoorbeeld;
	private KubusRooster[][] kubusRoosters, kubusTekenRoosters;
	private KubusRooster kubusRoosterActief, kubusTekenRoosterActief;
	private Label aantalKLabel, aantalKLabelVb;
	InvulKeuzePanel ip;
	private KeuzeLijst keuzelijst;
	private int selectieNummer;
	
	private int aantalActiviteiten;
	private int activiteitNr;
	private OpdrachtNrRij[] or;
	private OpdrachtNrRij orActief;
	private Label scoreLabel;	
	
	private String[][] opdrachten;
	private int[] aantalOpdrachten;
	private int maxAantalOpdrachten; 
	private int opdrachtNr;
	private Label[] scores;
	private InvulPanel invulPanel;
	private TekstPanel tp0, tp1, tp2;
	
	private int mode;
	private Hashtable launchData;
	private Color bgcolor;
	
	
	
	
	public ScormEditComponent(Hashtable launchData)
	{	setSize(770,480);
		this.launchData = launchData;
		
		setLayout(null);
		bgcolor = new Color(230,240,255);
				
		tp0 = new TekstPanel(15,30,35,320,100);
		tp0.setBackground(bgcolor);
		{	tp0.setText(0,NabouwenAanzichten.rb.getString("textPanelRegel0_0"));
			tp0.setText(1,NabouwenAanzichten.rb.getString("textPanelRegel0_1"));
			tp0.setText(2,NabouwenAanzichten.rb.getString("textPanelRegel0_2"));
			tp0.setText(3,NabouwenAanzichten.rb.getString("textPanelRegel0_3"));
			tp0.setText(4,NabouwenAanzichten.rb.getString("textPanelRegel0_4"));
		}
		
		tp1 = new TekstPanel(15,30,35,320,140);
		tp1.setBackground(bgcolor);
		{	tp1.setText(0,NabouwenAanzichten.rb.getString("textPanelRegel1_0"));
			tp1.setText(1,NabouwenAanzichten.rb.getString("textPanelRegel1_1"));
			tp1.setText(2,NabouwenAanzichten.rb.getString("textPanelRegel1_2"));
			tp1.setText(3,NabouwenAanzichten.rb.getString("textPanelRegel1_3"));
			tp1.setText(4,NabouwenAanzichten.rb.getString("textPanelRegel1_4"));
			tp1.setText(5,NabouwenAanzichten.rb.getString("textPanelRegel1_5"));
			tp1.setText(6,NabouwenAanzichten.rb.getString("textPanelRegel1_6"));
		}
		tp2 = new TekstPanel(15,30,35,285,120);
		tp2.setBackground(bgcolor);
		{	tp2.setText(0,NabouwenAanzichten.rb.getString("textPanelRegel2_0"));
			tp2.setText(1,NabouwenAanzichten.rb.getString("textPanelRegel2_1"));
			tp2.setText(2,NabouwenAanzichten.rb.getString("textPanelRegel2_2"));
			tp2.setText(3,NabouwenAanzichten.rb.getString("textPanelRegel2_3"));
			tp2.setText(4,NabouwenAanzichten.rb.getString("textPanelRegel2_4"));
			tp2.setText(5,NabouwenAanzichten.rb.getString("textPanelRegel2_5"));
		}
		
		selectieNummer = 0;
		
		ip = new InvulKeuzePanel(390,400,100,50);
		ip.setBackground(bgcolor);
		add(ip);
		
		KubusRooster kr = new KubusRooster(4,1);
		
		volLeegKnop = new Button(NabouwenAanzichten.rb.getString("volLeegKnopLabel1"));
		volLeegKnop.addActionListener(this);
		volLeegKnop.setBounds(490,420,80,24);
		add(volLeegKnop);
		
		String[] activiteitNamen = null;
		
		String modeString = getParameter("mode");
		if ( modeString == null) modeString = "1";
		mode = Integer.parseInt(modeString);
		
		if(mode==0)
		{	add(tp0);
			vWerk = new Viewer3d(kr, 351, -30, 450, 450, this);
			vWerk.zetAchtergrond(bgcolor);
			vWerk.zetAfstand(10000000);
			vWerk.zetSchaduw(false);
			vWerk.zetBeginHoeken(90,0);
			vWerk.zetMuisAan(false);
			vWerk.zetGetalRooster(true);
			add(vWerk);
			
			vVoorbeeld0 = new Viewer3d(new KubusRooster(4,1), 40, 120, 300, 290, this);
			vVoorbeeld0.zetAchtergrond(bgcolor);
			vVoorbeeld0.zetKlikAan(false);
			vVoorbeeld0.zetSchaduw(false);
			add(vVoorbeeld0);
			
			
			
			
			maxAantalOpdrachten = 10;
			activiteitNr = 0;
	
			{	aantalActiviteiten = 1;
				activiteitNr = 0;
				aantalOpdrachten = new int[aantalActiviteiten];
				aantalOpdrachten[0] = 10;
				//aantalOpdrachten[1] = 10;
				//aantalOpdrachten[2] = 10;
				
				
				maxAantalOpdrachten = 10;
				
				activiteitNamen = new String[aantalActiviteiten];
				activiteitNamen[0] = NabouwenAanzichten.rb.getString("niveau")+ "1";
				//activiteitNamen[1] = NabouwenAanzichten.rb.getString("niveau")+ "2";
				//activiteitNamen[2] = NabouwenAanzichten.rb.getString("niveau")+ "3";
				
				
				/*opdrachten = new String[aantalActiviteiten][maxAantalOpdrachten];
				
				String[] opdr0 = {
							"figuur_0_1_0",
						  	"figuur_0_1_1",
						  	"figuur_0_1_2",
						  	"figuur_0_1_3",
						  	"figuur_0_1_4",
						  	"figuur_0_1_5",
						  	"figuur_0_1_6",
						  	"figuur_0_1_7",
						  	"figuur_0_1_8",
						  	"figuur_0_1_9"
						};			   
				
				opdrachten[0] = opdr0;*/
			}
			invulPanel = new InvulPanel(activiteitNamen,100,140,180,80);
			invulPanel.addActionListener(this);
			invulPanel.setBackground(bgcolor);
			if(activiteitNamen.length>1)add(invulPanel);
			
			scores = new Label[aantalActiviteiten];
			for(int i=0 ; i<aantalActiviteiten; i++)
		    {	scores[i] = new Label("Score: "+0);
		    	scores[i].setBounds(210,140 + i*20,80,20);
		    	scores[i].setFont(new Font("SansSerif",Font.PLAIN,14));
		    	add(scores[i],0);
		    }
		}
		else if(mode==1)
		{	add(tp1);
			vWerk = new Viewer3d(kr, 351, -30, 450, 450, this);
			vWerk.zetAchtergrond(bgcolor);
			vWerk.zetAfstand(10000000);
			vWerk.zetSchaduw(false);
			vWerk.zetBeginHoeken(90,0);
			vWerk.zetMuisAan(false);
			vWerk.zetGetalRooster(true);
			add(vWerk);
			
			vVoorbeeld = new VaktekPanel(new KubusRooster(4,1), 40, 250, 300, 170,2, this);
			vVoorbeeld.zetAchtergrond(Color.white);
			vVoorbeeld.zetKlikAan(false);
			vVoorbeeld.ra.zetPijlAan(false);
			add(vVoorbeeld);
			
			
			maxAantalOpdrachten = 10;
			activiteitNr = 0;
	
			{	aantalActiviteiten = 1;
				activiteitNr = 0;
				aantalOpdrachten = new int[aantalActiviteiten];
				aantalOpdrachten[0] = 10;
				//aantalOpdrachten[1] = 10;
				//aantalOpdrachten[2] = 10;
				
				
				maxAantalOpdrachten = 10;
				
				activiteitNamen = new String[aantalActiviteiten];
				activiteitNamen[0] = NabouwenAanzichten.rb.getString("niveau")+ "1";
				//activiteitNamen[1] = NabouwenAanzichten.rb.getString("niveau")+ "2";
				//activiteitNamen[2] = NabouwenAanzichten.rb.getString("niveau")+ "3";
				
				
				/*opdrachten = new String[aantalActiviteiten][maxAantalOpdrachten];
				
				String[] opdr0 = {
							"figuur_1_1_0",
						  	"figuur_1_1_1",
						  	"figuur_1_1_2",
						  	"figuur_1_1_3",
						  	"figuur_1_1_4",
						  	"figuur_1_1_5",
						  	"figuur_1_1_6",
						  	"figuur_1_1_7",
						  	"figuur_1_1_8",
						  	"figuur_1_1_9"
						};			   
				
				opdrachten[0] = opdr0;*/
			}
			invulPanel = new InvulPanel(activiteitNamen,100,180,180,80);
			invulPanel.addActionListener(this);
			invulPanel.setBackground(bgcolor);
			if(activiteitNamen.length>1)add(invulPanel);
			
			scores = new Label[aantalActiviteiten];
			for(int i=0 ; i<aantalActiviteiten; i++)
		    {	scores[i] = new Label("Score: "+0);
		    	scores[i].setBounds(210,180 + i*20,80,20);
		    	scores[i].setFont(new Font("SansSerif",Font.PLAIN,14));
		    	add(scores[i],0);
		    }
		}
		else if(mode==2)
		{	add(tp2);
			vWerk = new Viewer3d(kr, 351, -30, 450, 450, this);
			vWerk.zetAchtergrond(bgcolor);
			vWerk.zetBeginHoeken(30,-30);
			add(vWerk);
			
			vVoorbeeld = new VaktekPanel(new KubusRooster(4,1), 40, 150, 250, 250,3, this);
			vVoorbeeld.zetAchtergrond(Color.white);
			vVoorbeeld.zetKlikAan(false);
			vVoorbeeld.ra.zetPijlAan(false);
			add(vVoorbeeld);
			
			maxAantalOpdrachten = 10;
			activiteitNr = 0;
	
			{	aantalActiviteiten = 2;
				activiteitNr = 0;
				aantalOpdrachten = new int[aantalActiviteiten];
				aantalOpdrachten[0] = 10;
				aantalOpdrachten[1] = 10;
				//aantalOpdrachten[2] = 10;
				
				
				maxAantalOpdrachten = 10;
				
				activiteitNamen = new String[aantalActiviteiten];
				activiteitNamen[0] = NabouwenAanzichten.rb.getString("niveau")+ "1";
				activiteitNamen[1] = NabouwenAanzichten.rb.getString("niveau")+ "2";
				//activiteitNamen[2] = NabouwenAanzichten.rb.getString("niveau")+ "3";
				
				
				/*opdrachten = new String[aantalActiviteiten][maxAantalOpdrachten];
				
				String[] opdr0 = {
							"figuur_2_1_0",
						  	"figuur_2_1_1",
						  	"figuur_2_1_2",
						  	"figuur_2_1_3",
						  	"figuur_2_1_4",
						  	"figuur_2_1_5",
						  	"figuur_2_1_6",
						  	"figuur_2_1_7",
						  	"figuur_2_1_8",
						  	"figuur_2_1_9"
						};			   
				
				opdrachten[0] = opdr0;
				
				String[] opdr1 = {
						  	"figuur_2_2_0",
						  	"figuur_2_2_1",
						  	"figuur_2_2_2",
						  	"figuur_2_2_3",
						  	"figuur_2_2_4",
						  	"figuur_2_2_5",
						  	"figuur_2_2_6",
						  	"figuur_2_2_7",
						  	"figuur_2_2_8",
						  	"figuur_2_2_9"
						};
							   			  			
				opdrachten[1] = opdr1;*/
			}
			invulPanel = new InvulPanel(activiteitNamen,200,180,180,80);
			invulPanel.addActionListener(this);
			invulPanel.setBackground(bgcolor);
			if(activiteitNamen.length>1)add(invulPanel);
			
			scores = new Label[aantalActiviteiten];
			for(int i=0 ; i<aantalActiviteiten; i++)
		    {	scores[i] = new Label("Score: "+0);
		    	scores[i].setBounds(310,180 + i*20,80,20);
		    	scores[i].setFont(new Font("SansSerif",Font.PLAIN,14));
		    	add(scores[i],0);
		    }
		}
		
		
		
	    
	    opdrachtNr = 0;
	    kubusRoosters = new KubusRooster[aantalActiviteiten][maxAantalOpdrachten];
	    kubusTekenRoosters = new KubusRooster[aantalActiviteiten][maxAantalOpdrachten];
		String editModeStateString = getParameter("editModeState");
		setEditModeState(editModeStateString);
		
		
		for(int i=0 ; i<aantalActiviteiten ; i++)
		{	for(int j=0 ; j<aantalOpdrachten[i] ; j++)
			{	//kubusRoosters[i][j] = leesFile("resources/" + opdrachten[i][j] + ".gif");
				if(mode==0)kubusRoosters[i][j].zetVulkleur("zwart");
				//kubusTekenRoosters[i][j] = new KubusRooster(kubusRoosters[i][j].maxAantal,1);
			}
		}
		
		
		kubusRoosterActief = kubusRoosters[activiteitNr][opdrachtNr];
		kubusTekenRoosterActief = kubusTekenRoosters[activiteitNr][opdrachtNr];
			
		aantalKLabel = new Label(NabouwenAanzichten.rb.getString("aantalKLabel")+ 0);
		aantalKLabel.setBounds(600,420,180,20);
		aantalKLabel.setFont(new Font("SansSerif",Font.PLAIN,14));
		add(aantalKLabel);
		
		aantalKLabelVb = new Label(NabouwenAanzichten.rb.getString("aantalKLabel")+ 0);
		aantalKLabelVb.setBounds(110,310,120,15);
		aantalKLabelVb.setBackground(new Color(255,255,200));
		aantalKLabelVb.setFont(new Font("SansSerif",Font.PLAIN,12));
		//add(aantalKLabelVb);
		
		vWerk.zetKubusRooster(kubusTekenRoosters[activiteitNr][opdrachtNr]);
		
	    if(mode==1 || mode==2)vVoorbeeld.zetKubusRooster(kubusRoosters[activiteitNr][opdrachtNr]);
	    else if(mode==0)vVoorbeeld0.zetKubusRooster(kubusRoosters[activiteitNr][opdrachtNr]);
	    
	    String str;
		if(kubusRoosterActief!=null)str = NabouwenAanzichten.rb.getString("aantalKLabel")+ kubusRoosterActief.geefAantalK();
		else str = NabouwenAanzichten.rb.getString("aantalKLabel")+ 0;
		aantalKLabelVb.setText(str);
		
		or = new OpdrachtNrRij[aantalActiviteiten];
		for(int i=0 ; i<aantalActiviteiten ; i++)
		{	or[i] = new OpdrachtNrRij(aantalOpdrachten[i], 70,420);
			or[i].addActionListener(this);
			or[i].setBackground(bgcolor);
			or[i].setSelected(1);
			add(or[i]);
			or[i].setVisible(false);
		}
		orActief = or[activiteitNr];
		orActief.setVisible(true);
		
		scoreLabel = new Label(NabouwenAanzichten.rb.getString("scoreLabel"));
		scoreLabel.setBounds(20,450,50,24);
		add(scoreLabel);
		
		
	}
	
		
	public String getParameter(String name)
	{	String value = (String)launchData.get(name);
		return value;
	}
	
	
	
	
	public void setState(String s)
	{	if(s==null || s.equals(""))return;
		Object o = StringCodeObject.decodeStringToObject(s);
		boolean[][][][][] booleanKRs = (boolean[][][][][])o;
		
		orActief.setVisible(false);
		for(int i=0 ; i<aantalActiviteiten ; i++)
		{	orActief = or[i];
			for(int j=0 ; j<aantalOpdrachten[i] ; j++)
			{	kubusTekenRoosters[i][j] = new KubusRooster(booleanKRs[i][j],1);
				s = NabouwenAanzichten.rb.getString("aantalKLabel")+ kubusTekenRoosterActief.geefAantalK();
				kubusRoosterActief = kubusRoosters[i][j];
				kubusTekenRoosterActief = kubusTekenRoosters[i][j];
				if(kubusTekenRoosterActief.isGelijkAanzichtenVB(kubusRoosterActief) && kubusTekenRoosterActief.aantalKubussen <= kubusRoosterActief.aantalKubussen)
				{	orActief.zetGemaakt(j+1,true);
					orActief.zetScore(j+1,10);
				}
				else if(kubusTekenRoosterActief.isGelijkAanzichtenVB(kubusRoosterActief))
				{	orActief.zetGemaaktHalf(j+1);
					orActief.zetScore(j+1,Math.max(5,10-(kubusTekenRoosterActief.aantalKubussen-kubusRoosterActief.aantalKubussen)));
				}
				else
				{	orActief.zetGemaakt(j+1,false);
					orActief.zetScore(j+1,0);
				}
			}
			scores[i].setText(NabouwenAanzichten.rb.getString("scoreLabel")+orActief.geefScoreTotaal());
		}
		activiteitNr=0;
		opdrachtNr=0;
		kubusRoosterActief = kubusRoosters[activiteitNr][opdrachtNr];
		kubusTekenRoosterActief = kubusTekenRoosters[activiteitNr][opdrachtNr];
		orActief = or[activiteitNr];
		orActief.setVisible(true);	
		
	    vWerk.zetKubusRooster(kubusTekenRoosterActief);
	    
	    if(mode==1 || mode==2)vVoorbeeld.zetKubusRooster(kubusRoosterActief);
	    else if(mode==0)vVoorbeeld0.zetKubusRooster(kubusRoosterActief);
	    
		if(mode==2)vWerk.zetBeginHoeken(30,-30);
		String str;
		str = NabouwenAanzichten.rb.getString("aantalKLabel")+ kubusRoosterActief.geefAantalK();
		aantalKLabelVb.setText(str);
	}
	
	public String getState()
	{	int aantalActiviteiten = 0;
		int[] aantalOpdrachten = null;
		boolean[][][][][] booleanKRs = null;
		
		aantalActiviteiten = this.aantalActiviteiten;
		aantalOpdrachten = this.aantalOpdrachten;
		booleanKRs = new boolean[aantalActiviteiten][][][][];
		for(int i=0 ; i<aantalActiviteiten ; i++)
		{	booleanKRs[i] = new boolean[aantalOpdrachten[i]][][][];
			for(int j=0 ; j<aantalOpdrachten[i] ; j++)
			{	booleanKRs[i][j] = kubusTekenRoosters[i][j].geefBooleanRooster();
			}
		}
		
		
		String s = StringCodeObject.encodeObjectToString(booleanKRs);
	    return s;
	}
	
	public void setEditModeState(String s)
	{	if(s==null || s.equals(""))return;
		Object o = StringCodeObject.decodeStringToObject(s);
		boolean[][][][][] booleanKRs = (boolean[][][][][])o;
		
		for(int i=0 ; i<aantalActiviteiten ; i++)
		{	for(int j=0 ; j<aantalOpdrachten[i] ; j++)
			{	kubusRoosters[i][j] = new KubusRooster(booleanKRs[i][j],1);
				kubusTekenRoosters[i][j] = new KubusRooster(booleanKRs[i][j],1);
			}
		}
	}
	    
	public String getEditModeState()
	{	int aantalActiviteiten = 0;
		int[] aantalOpdrachten = null;
		boolean[][][][][] booleanKRs = null;
		
		aantalActiviteiten = this.aantalActiviteiten;
		aantalOpdrachten = this.aantalOpdrachten;
		booleanKRs = new boolean[aantalActiviteiten][][][][];
		for(int i=0 ; i<aantalActiviteiten ; i++)
		{	booleanKRs[i] = new boolean[aantalOpdrachten[i]][][][];
			for(int j=0 ; j<aantalOpdrachten[i] ; j++)
			{	booleanKRs[i][j] = kubusRoosters[i][j].geefBooleanRooster();
			}
		}
		String s = StringCodeObject.encodeObjectToString(booleanKRs);
	    return s;
	}
	
	
	
	public void update(Graphics g)
	{	paint(g);
	}
	
	public void paint(Graphics g)
	{	Dimension dd = getSize();				
		if (resized || im == null)
		{	if(gIm!=null)gIm.dispose();
			im = createImage(dd.width, dd.height);
			gIm = im.getGraphics();
			resized = false;
		}
		gIm.setColor(bgcolor);
		gIm.fillRect(0,0,getSize().width,getSize().height);
		super.paint(gIm);
		g.drawImage(im,0,0,null);
	}
	
	public void zetVeranderd()
	{	vWerk.tekenOpnieuw();
		//v.tekenOpnieuw();
		String s = "";
		if(kubusRoosterActief!=null)
		{	kubusRoosters[activiteitNr][opdrachtNr] = kubusTekenRoosterActief;
			if(mode==1 || mode==2)vVoorbeeld.zetKubusRooster(kubusRoosters[activiteitNr][opdrachtNr]);
	    	else if(mode==0)vVoorbeeld0.zetKubusRooster(kubusRoosters[activiteitNr][opdrachtNr]);
		}
		else s = NabouwenAanzichten.rb.getString("aantalKLabel")+ vWerk.kr.geefAantalK();
		aantalKLabel.setText(s);
	}
	
	public KubusRooster geefKubusRooster()
	{	return kubusTekenRoosterActief;
	}
	
	
	public void zetKubusRooster(KubusRooster k)
	{	if(mode==1 || mode==2)
		{	vVoorbeeld.zetKubusRooster(k);
			vVoorbeeld.tekenOpnieuw();
		}
		
		else if(mode==0)
		{	vVoorbeeld0.zetKubusRooster(k);
			vVoorbeeld0.tekenOpnieuw();
		}
		volLeegKnop.setLabel("Maak vol");
		if(mode==2)vWerk.zetBeginHoeken(30,-30);
		zetVeranderd();
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getSource()==volLeegKnop)
		{	if(volLeegKnop.getLabel().equals(NabouwenAanzichten.rb.getString("volLeegKnopLabel1")))
			{	if(kubusTekenRoosterActief==null)
				{	vWerk.kr.maakVol();
					vWerk.zetKubusRooster(vWerk.kr);
				}
				else 
				{	kubusTekenRoosterActief.maakVol();
					vWerk.zetKubusRooster(kubusTekenRoosterActief);
				}
				volLeegKnop.setLabel(NabouwenAanzichten.rb.getString("volLeegKnopLabel2"));
				zetVeranderd();
			}
			else
			{	if(kubusTekenRoosterActief==null)
				{	vWerk.kr.maakLeeg();
					vWerk.zetKubusRooster(vWerk.kr);
				}
				else 
				{	kubusTekenRoosterActief.maakLeeg();
					vWerk.zetKubusRooster(kubusTekenRoosterActief);
				}
				volLeegKnop.setLabel(NabouwenAanzichten.rb.getString("volLeegKnopLabel1"));
				zetVeranderd();
			}
		}
		else if(e.getSource() == orActief)
		{	opdrachtNr = Integer.parseInt(e.getActionCommand())-1;
			kubusRoosterActief = kubusRoosters[activiteitNr][opdrachtNr];
			kubusTekenRoosterActief = kubusTekenRoosters[activiteitNr][opdrachtNr];
			if(mode==1 || mode==2)
			{	vVoorbeeld.zetKubusRooster(kubusRoosterActief);
				if(mode==2)vWerk.zetBeginHoeken(30,-30);
			}
			else if(mode==0)
			{	vVoorbeeld0.zetKubusRooster(kubusRoosterActief);
			}
			vWerk.zetKubusRooster(kubusTekenRoosterActief);
			vWerk.tekenOpnieuw();
		}
		else if(e.getSource()== invulPanel)
		{	if(activiteitNr==invulPanel.geefKeuze()-1)return;
			orActief.setVisible(false);
			activiteitNr = invulPanel.geefKeuze()-1;
			orActief = or[activiteitNr];
			orActief.setSelected(1);
			orActief.setVisible(true);
			opdrachtNr = 0;
			kubusRoosterActief = kubusRoosters[activiteitNr][opdrachtNr];
			kubusTekenRoosterActief = kubusTekenRoosters[activiteitNr][opdrachtNr];
			if(mode==1 || mode==2)
			{	vVoorbeeld.zetKubusRooster(kubusRoosterActief);
				if(mode==2)vWerk.zetBeginHoeken(30,-30);
				
			}
			else if(mode==0)
			{	vVoorbeeld0.zetKubusRooster(kubusRoosterActief);
				
			}
			vWerk.zetKubusRooster(kubusTekenRoosterActief);
			vWerk.tekenOpnieuw();
		}
	}

	
	public Component getComponent()
	{   return this;
	} 
	
	public Hashtable getLaunchData()
    {   Hashtable h = launchData;
    	h.remove("editModeState");
    	h.put("editModeState",getEditModeState());
    	return h;
	}
	
    public void end()
    {   
	}
    public void reset()
    {   
	}	
	
	public boolean isBouwen()
	{	return ip.isBouwen();
	}
}
