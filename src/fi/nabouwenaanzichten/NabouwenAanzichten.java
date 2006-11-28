package fi.nabouwenaanzichten;

import java.applet.Applet;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import fi.nabouwenaanzichten.text.*;
import fi.beans.scorm.*;
import fi.beans.base64code.*;

/**
 * @author Peter Boon
 */

public class NabouwenAanzichten extends Applet implements ScormAppletIF, ActionListener, NabouwenAanzichtenIF 
{	
	private Image im;
  	private Graphics gIm;
  	private boolean resized = false;
  	
	protected SCORM12APIInterface api;
	private long sessionStartTime;
	
	private FIButton fiButton;
	private AppletUtil au;
	protected static ResourceBundle rb;
	private String langArg;
	private String bgColorArg;
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
	private Color bgcolor = Color.white;
	
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
	
	private int mode = 2;
	private Hashtable defaultParamValues;
	
	public static void main(String[] args)    
	{	int width = 800;
        int height = 600;
        ScormMainFrame mf = new ScormMainFrame(new NabouwenAanzichten(),width, height);
		mf.setTitle("Nabouwen Aanzichten");
		mf.pack();
		mf.show();
		mf.setSize(width, height);
	}
	
	public NabouwenAanzichten()
	{	Locale language = new Locale ("nl", "");
		rb = ResourceBundle.getBundle("fi.nabouwenaanzichten.text.Text",language);
	}
	
	
	public void init()
	{	
		String variantString = super.getParameter("variant");
		int variant = 0;
		if(variantString!=null) variant = Integer.parseInt(variantString);
		
		defaultParamValues = makeDefaultParamValues(variant);
		
		try
		{	api = Scorm.findAPI(this);
		}
		catch(Exception e){}
		
		setLayout(null);
		
		
		
		
		au = new AppletUtil(this);
		
		langArg = getParameter("language");
		if ( langArg == null) langArg = "nl";
		Locale language = new Locale (langArg, "");
		rb = ResourceBundle.getBundle("fi.nabouwenaanzichten.text.Text",language);
		
		Color bgcolor = new Color(230,240,255);
		bgColorArg = getParameter("bgcolor");
		if(bgColorArg!=null)bgcolor = new Color(Integer.parseInt(bgColorArg.substring(1),16));
		setBackground(bgcolor);
		
		fiButton = new FIButton("Nabouwen Aanzichten",new String[]{"","versie-info: 20060615",
											"auteur: Peter Boon",
											"programmeur: Peter Boon",
											"Freudenthal Instituut",
																																	"www.fi.uu.nl",""});
		fiButton.setBounds(2,2,20,30);
		add(fiButton);
		
		String modeString = getParameter("mode");
		if ( modeString == null) modeString = "1";
		mode = Integer.parseInt(modeString);
		
				
		tp0 = new TekstPanel(15,30,35,380,100);
		tp0.setBackground(getBackground());
		{	tp0.setText(0,NabouwenAanzichten.rb.getString("textPanelRegel0_0"));
			tp0.setText(1,NabouwenAanzichten.rb.getString("textPanelRegel0_1"));
			tp0.setText(2,NabouwenAanzichten.rb.getString("textPanelRegel0_2"));
			tp0.setText(3,NabouwenAanzichten.rb.getString("textPanelRegel0_3"));
			tp0.setText(4,NabouwenAanzichten.rb.getString("textPanelRegel0_4"));
		}
		
		tp1 = new TekstPanel(15,30,35,380,150);
		tp1.setBackground(getBackground());
		{	tp1.setText(0,NabouwenAanzichten.rb.getString("textPanelRegel1_0"));
			tp1.setText(1,NabouwenAanzichten.rb.getString("textPanelRegel1_1"));
			tp1.setText(2,NabouwenAanzichten.rb.getString("textPanelRegel1_2"));
			tp1.setText(3,NabouwenAanzichten.rb.getString("textPanelRegel1_3"));
			tp1.setText(4,NabouwenAanzichten.rb.getString("textPanelRegel1_4"));
			tp1.setText(5,NabouwenAanzichten.rb.getString("textPanelRegel1_5"));
			tp1.setText(6,NabouwenAanzichten.rb.getString("textPanelRegel1_6"));
			tp1.setText(7,NabouwenAanzichten.rb.getString("textPanelRegel1_7"));
		}
		tp2 = new TekstPanel(15,30,35,380,120);
		tp2.setBackground(getBackground());
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
		volLeegKnop.setBounds(510,420,80,24);
		add(volLeegKnop);
		
		String[] activiteitNamen = null;
		
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
	
			aantalActiviteiten = 1;
			activiteitNr = 0;
			aantalOpdrachten = new int[aantalActiviteiten];
			aantalOpdrachten[0] = 10;
						
			activiteitNamen = new String[aantalActiviteiten];
			activiteitNamen[0] = NabouwenAanzichten.rb.getString("niveau")+ "1";
			
			invulPanel = new InvulPanel(activiteitNamen,100,140,180,80);
			invulPanel.addActionListener(this);
			invulPanel.setBackground(getBackground());
			if(activiteitNamen.length>1)add(invulPanel);
			
			scores = new Label[aantalActiviteiten];
			for(int i=0 ; i<aantalActiviteiten; i++)
		    {	scores[i] = new Label(NabouwenAanzichten.rb.getString("scoreLabel")+0);
		    	scores[i].setBounds(180,140 + i*20,140,20);
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
			
			vVoorbeeld = new VaktekPanel(new KubusRooster(4,1), 60, 250, 300, 170,2, this);
			vVoorbeeld.zetAchtergrond(Color.white);
			vVoorbeeld.zetKlikAan(false);
			vVoorbeeld.ra.zetPijlAan(false);
			add(vVoorbeeld);
			
			
			maxAantalOpdrachten = 10;
			activiteitNr = 0;
	
			aantalActiviteiten = 1;
			activiteitNr = 0;
			aantalOpdrachten = new int[aantalActiviteiten];
			aantalOpdrachten[0] = 10;
				
			activiteitNamen = new String[aantalActiviteiten];
			activiteitNamen[0] = NabouwenAanzichten.rb.getString("niveau")+ "1";
			
			invulPanel = new InvulPanel(activiteitNamen,100,180,180,80);
			invulPanel.addActionListener(this);
			invulPanel.setBackground(getBackground());
			if(activiteitNamen.length>1)add(invulPanel);
			
			scores = new Label[aantalActiviteiten];
			for(int i=0 ; i<aantalActiviteiten; i++)
		    {	scores[i] = new Label(NabouwenAanzichten.rb.getString("scoreLabel")+0);
		    	scores[i].setBounds(180,160 + i*20,140,20);
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
	
			aantalActiviteiten = 2;
			activiteitNr = 0;
			aantalOpdrachten = new int[aantalActiviteiten];
			aantalOpdrachten[0] = 10;
			aantalOpdrachten[1] = 10;
			
			activiteitNamen = new String[aantalActiviteiten];
			activiteitNamen[0] = NabouwenAanzichten.rb.getString("niveau")+ "1";
			activiteitNamen[1] = NabouwenAanzichten.rb.getString("niveau")+ "2";

			invulPanel = new InvulPanel(activiteitNamen,200,180,180,80);
			invulPanel.addActionListener(this);
			invulPanel.setBackground(getBackground());
			if(activiteitNamen.length>1)add(invulPanel);
			
			scores = new Label[aantalActiviteiten];
			for(int i=0 ; i<aantalActiviteiten; i++)
		    {	scores[i] = new Label(NabouwenAanzichten.rb.getString("scoreLabel")+0);
		    	scores[i].setBounds(280,180 + i*20,140,20);
		    	scores[i].setFont(new Font("SansSerif",Font.PLAIN,14));
		    	add(scores[i],0);
		    }
		}
		
		
		
	    
	    opdrachtNr = 0;
	    kubusRoosters = new KubusRooster[aantalActiviteiten][maxAantalOpdrachten];
		String editModeStateString = getParameter("editModeState");
		setEditModeState(editModeStateString);
		
		kubusTekenRoosters = new KubusRooster[aantalActiviteiten][maxAantalOpdrachten];
		for(int i=0 ; i<aantalActiviteiten ; i++)
		{	for(int j=0 ; j<aantalOpdrachten[i] ; j++)
			{	if(mode==0)kubusRoosters[i][j].zetVulkleur("zwart");
				kubusTekenRoosters[i][j] = new KubusRooster(kubusRoosters[i][j].maxAantal,1);
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
		{	or[i] = new OpdrachtNrRij(aantalOpdrachten[i], 90,420);
			or[i].addActionListener(this);
			or[i].setBackground(getBackground());
			or[i].setSelected(1);
			add(or[i]);
			or[i].setVisible(false);
		}
		orActief = or[activiteitNr];
		orActief.setVisible(true);
		
		scoreLabel = new Label(NabouwenAanzichten.rb.getString("scoreLabel"));
		scoreLabel.setBounds(0,450,90,24);
		scoreLabel.setAlignment(Label.RIGHT);
		add(scoreLabel);
		
		
	}
	
	public Hashtable getDefaultParamValues(int variant)
	{	return makeDefaultParamValues(variant);
	}
	
	public String getParameter(String name)
	{	String value = super.getParameter(name);
		if(value==null)value = (String)defaultParamValues.get(name);
		return value;
	}
	
	private Hashtable makeDefaultParamValues(int variant)
	{	Hashtable h = new Hashtable();
		h.put("language","nl");
		h.put("bgcolor","#DDEEFF");
							
		if(variant==0)
		{	h.put("mode","2");
			h.put("editModeState","H4sIAAAAAAAAAM1XvUoEQQxO1ptDrhTFRlBRH8InEe7QxhdQQbASxAew1gew8gFs7MTGztJS0Ma/JxCcPbw9MyaZzMzeoXDgt9l8m2Qy+bl6B3d4AN1B/dc/fT2p5s4/7iqAoz0AqLzIDSVrl+uPn19vDyNJz0s6taC6fb7Z2ei9jAQdL5jxz3eXF1fvL5YWfj+vBv2ts5XN+evtp+ax/8HhPhzDbI2QIOBRd0gnvBUwICYzUB3BIp3B4lOnYdD94T1QrTHpZPsj2JcSEd6++j/XcLnmDYdE3UEKHOsGpKEa2lgCaYQ0YPF+WwzXSSPRkPyYSEzFJKZJryF/3bCMoUGtMzS22RnyIoIUWSICOpoOA1Id5BiMEVGtESJC31KzTDrhcgaSI5oX8RwxaSdERDjThFtDdQT2hFsjMMg5ErYKjKKSnLdFJKedxu2m/mq3xphvxpy35G9ac+f48geMeB1pYdyhDATljJAUYWlMIegPP8gP0X4Q1ysKBrFUbhM/yJX3jD/5Hq9xkfNh6xj1V6uxqT094g/KJ2VjULpoZpXmUGaOZFmjdXLbCSv1xWQDXeL4CFtXsJDZUN/+x62R7U5dStW5j0cJWVY+jQvxieVIPMLiUgokZYcrEoow3LZIEjuVBaxQ3HRLVr2xQdMgBbogB99gPonceWiDQqujE2iNyMRgGrYnODoJX0wpMuoKyLbG1hkyiq19KZUbq9TKwnEvY4FqmUHhQ8CiiKSfcM4QahqyktqpzvcNGaPoOhcXAAA=");
		}
		if(variant==1)
		{	h.put("mode","1");
			h.put("editModeState","H4sIAAAAAAAAAFvzloG1tIiBLRoEoppfNzIJzXx3jImBoaKAgYGBESjFCpZRXqZy6/3fN5dgMlxAGRaQBNORp3vjVLmewSRYgBLMQPFkeXHF07NkRJHFmaKjwvsVLEV2xD6CCwMxQ2khQx0DB4jHiMJjwM5jAxtHSBXEPEZGkk3AoYd8N2CXA7FY4WaxwlWwMqBoJ43LiOJAcg1lZMQhS5KhoHAkxkr8huJ0C0mGMhKpDT+XBR7ppCVBRpokIPyJGI95RJqA3d2YiRhriDDicQ2a22D2kG0CIw7/UG4CA0oYEGUCWWkER8jTvKAjPY0Qla7ISiNEmUV1/1A7TImKV/LKEeLih5jKi9qlAPkhQjCNUD3NkxEiRIXB4AiRwecfosoyomKFfmmEyuUIDr9SFCsA2uxjlbgLAAA=");
		}
		if(variant==2)
		{	h.put("mode","0");
			h.put("editModeState","H4sIAAAAAAAAAFvzloG1tIiBLRoEoppfNzIJzXx3jImBoaKAgYGBESjFCpZRXqZy6/3fN5dgMlxAGRaQBNORp3vjVLmewSRYgBLMQPFkeXHF07NkRJHFmaKjwvsVLEV2xD6CCwMxQ2khQx0DB4jHiIcHV8kGNg6XHkZUHiM28wiYwIjbPBwmkOELEIsVbhYrXAUrI4phrAykcBF60QxFV8eIQxs2WQZiDUU3hZEahlLFpdQJUxZ4pOONfrTEAAQUJmLsyQm/CcSYhz8RY3c3ZiLGGiJE+QDVnkGYrVHDgKgQxh0ixPuHrBDBah75aYSRjFSGnUdsiJCRRhjw6yGi8kBP87h5ROZcYnxBjRDBnWIpLAVwuBmfedRJI9grQxQHYNYp+LgMKFHGikcbAwqXAR+XaENxaaPIUGq5lAphSnSU4XUkTi7+CpyAHbhkCRhKEhelTCLeUCIDhybeB0UZO9x4drgSdrASJCOIEWBEtYZEM6jgAMoESHcAGFCoghQHkGTawEQBRhqgKAQoS1G0CAGaRAEEAAD+oKT2eQ8AAA==");
		}
		return h;
	}
	
	public void start()
	{	sessionStartTime = System.currentTimeMillis();
		if(api!=null)
		{	String s = api.LMSGetValue("cmi.suspend_data");
			if(s!=null && !s.equals(""))setState(s);
			//
			//api.LMSSetValue("cmi.launch_data",StringCodeObject.encodeObjectToString(defaultParamValues));
			//
		}
	
	}
	
	public void stopSco()
	{	if(api!=null)
		{	stop();
			api = null;
		}
	}
	
	public void stop()
	{	if(api!=null)
		{	String s = getState();
			String d = new Double(getScore()).toString();
			String t = getSessionTime();
			api.LMSSetValue("cmi.core.session_time",t);
			api.LMSSetValue("cmi.core.score.raw",d);
			api.LMSSetValue("cmi.suspend_data",s);
		}
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
	
	public double getScore()
	{	double score = 0;
		for(int i=0 ; i<aantalActiviteiten ; i++)
		{	score += or[i].geefScoreTotaal();
		}
		score = score/aantalActiviteiten;
		score = Math.rint(score*10)/10;
		return score;
	}
	
	public String getSessionTime()
	{	long sessionTime = System.currentTimeMillis() - sessionStartTime;
		String s = "";
		int hours = (int)sessionTime/3600000;
		int minutes = (int)sessionTime/60000 - hours*60;
		int seconds = (int)sessionTime/1000 - hours*3600 - minutes*60;
		if(hours<10)s += "0";
		s += hours;
		s += ":";
		if(minutes<10)s += "0";
		s += minutes;
		s += ":";
		if(seconds<10)s += "0";
		s += seconds;
		return s;
	}
	
	public boolean hasEditMode()
	{	return true;
	}
	
	public ScormEditComponentIF getEditComponent(Hashtable launchData)
	{	return new ScormEditComponent(launchData);
	}
	
	public Parameter[] getEditableParameters()
	{	Parameter[] parameters = new Parameter[1]; 
		ScormTree treeType = new ScormTree();
		treeType.setMaxItems(5);
		
		TreeParameter treeParam = new TreeParameter("niveaus", "Niveaus", treeType);
		treeParam.setItemCountName("aantalActiviteiten");
		treeParam.addSubParameter(new Parameter("activiteit", "Niveau", new ScormString()));
		treeParam.setItemLabel("Niveau");
		
		DataType type = null;
		Parameter param = null;	
		
		ScormTree treeType2 = new ScormTree();
		treeType2.setMaxItems(15);
		TreeParameter subtree = new TreeParameter("opdrachten", "Opdrachten", treeType2);
		subtree.setItemCountName("aantalOpdrachten");
		subtree.setItemLabel("Opdracht");
		
		type = new ScormString();
		param = new Parameter("opdracht", "Opdracht", type);
		subtree.addSubParameter(param);
		
		treeParam.addSubParameter(subtree);
		parameters[0] = treeParam;
		return parameters;
	}
	
	public Parameter[] getAllParameters()
	{	Parameter[] parameters = new Parameter[4]; 
		DataType type = null;
		Parameter param = null;	

		type = new ScormString();
		param = new Parameter("language", "Taal", type);
		parameters[0] = param;
		
		type = new ScormInteger();
		type.setSize(8);
		param = new Parameter("bgcolor", "Achtergrondkleur", type);
		parameters[1] = param;
		
		type = new ScormInteger();
		type.setSize(4);
		param = new Parameter("mode", "Mode", type);
		parameters[2] = param;
		
		type = new ScormString();
		param = new Parameter("editModeData", "Data begintoestand", type);
		parameters[3] = param;
		
		return parameters;

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
		gIm.setColor(getBackground());
		gIm.fillRect(0,0,getSize().width,getSize().height);
		super.paint(gIm);
		g.drawImage(im,0,0,null);
	}
	
	public void zetVeranderd()
	{	vWerk.tekenOpnieuw();
		//v.tekenOpnieuw();
		String s = "";
		if(kubusRoosterActief!=null)
		{	if(mode==0)
			{	if(kubusTekenRoosterActief.isGelijk(kubusRoosterActief))
				{	orActief.zetGemaakt(opdrachtNr+1,true);
					orActief.zetScore(opdrachtNr+1,10);
				}
				else
				{	orActief.zetGemaakt(opdrachtNr+1,false);
					orActief.zetScore(opdrachtNr+1,0);
				}
			}
			else if(mode==1)
			{	s = NabouwenAanzichten.rb.getString("aantalKLabel")+ kubusTekenRoosterActief.geefAantalK();
				if(kubusTekenRoosterActief.isGelijkAanzichtenVB(kubusRoosterActief) && kubusTekenRoosterActief.aantalKubussen <= kubusRoosterActief.aantalKubussen)
				{	orActief.zetGemaakt(opdrachtNr+1,true);
					orActief.zetScore(opdrachtNr+1,10);
				}
				else if(kubusTekenRoosterActief.isGelijkAanzichtenVB(kubusRoosterActief))
				{	orActief.zetGemaaktHalf(opdrachtNr+1);
					orActief.zetScore(opdrachtNr+1,Math.max(5,10-(kubusTekenRoosterActief.aantalKubussen-kubusRoosterActief.aantalKubussen)));
				}
				else
				{	orActief.zetGemaakt(opdrachtNr+1,false);
					orActief.zetScore(opdrachtNr+1,0);
				}
			}
			else if(mode==2)
			{	s = NabouwenAanzichten.rb.getString("aantalKLabel")+ kubusTekenRoosterActief.geefAantalK();
				if(kubusTekenRoosterActief.isGelijkAanzichten(kubusRoosterActief) && kubusTekenRoosterActief.aantalKubussen <= kubusRoosterActief.aantalKubussen)
				{	orActief.zetGemaakt(opdrachtNr+1,true);
					orActief.zetScore(opdrachtNr+1,10);
				}
				else if(kubusTekenRoosterActief.isGelijkAanzichten(kubusRoosterActief))
				{	orActief.zetGemaaktHalf(opdrachtNr+1);
					orActief.zetScore(opdrachtNr+1,Math.max(5,10-(kubusTekenRoosterActief.aantalKubussen-kubusRoosterActief.aantalKubussen)));
				}
				else
				{	orActief.zetGemaakt(opdrachtNr+1,false);
					orActief.zetScore(opdrachtNr+1,0);
				}
			}
		
			scores[activiteitNr].setText(NabouwenAanzichten.rb.getString("scoreLabel")+orActief.geefScoreTotaal());
		}
		else s = NabouwenAanzichten.rb.getString("aantalKLabel")+ vWerk.kr.geefAantalK();
		aantalKLabel.setText(s);
	}
	
	public KubusRooster geefKubusRooster()
	{	return kubusTekenRoosterActief;
	}
	
	KubusRooster leesFile(String naam)//
	{	KubusRooster kr = null;
		int aantalKubussen = 0;
		int maxAantal = 0;
		DataInputStream invoer;
		try
		{	invoer = new DataInputStream(au.getStream(naam));
			//invoer = new DataInputStream(new FileInputStream(new File(naam)));
			aantalKubussen = invoer.readShort();
			maxAantal = invoer.readByte();
			kr = new KubusRooster(maxAantal,1);
			for(int i=0 ; i<aantalKubussen ; i++)
			{	int x = invoer.readByte();
				int y = invoer.readByte();
				int z = invoer.readByte();
				kr.voegKubusToe(x,y,z);
			}
		}
		catch(IOException io){}
		return kr;
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
	
	public boolean isBouwen()
	{	return ip.isBouwen();
	}
}