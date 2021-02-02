package fi.wiskopdr;

import java.awt.AWTEventMulticaster;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.cbook.cbookif.CBookEvent;
import org.cbook.cbookif.CBookEventHandler;
import org.cbook.cbookif.CBookEventListener;

import fi.beans.ideas.RuleIF;
import fi.beans.stringutils.StringUtils;
import fi.beans.wiskopdrbeans.CBookAware;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;
import fi.beans.wnwidgets.NWButtonUI;
import fi.wiskopdr.expressies.Algebra;
import fi.wiskopdr.expressies.BasisExpressie;
import fi.wiskopdr.expressies.DecRound;
import fi.wiskopdr.expressies.Expressie;
import fi.wiskopdr.expressies.FunctieMV;
import fi.wiskopdr.expressies.Vergelijking;
import fi.wiskopdr.expressies.VergelijkingMeerv;
import fi.wiskopdr.expressies.Matrix.BerekendeMatrix;
import fi.wiskopdr.expressies.VectorExpr.BerekendeVectorExpr;
import fi.wiskopdr.expressies.FunctieMVDefSet;
import fi.wiskopdr.expressies.repr.MPReduce;
import fi.wiskopdr.expressies.repr.MPReduceConverter;
import fi.wiskopdr.formuleobjects.FormuleButton;
import fi.wiskopdr.formuleobjects.FormuleEditor;
import fi.wiskopdr.formuleobjects.FormuleParser;
import fi.wiskopdr.formuleobjects.FormuleVak;
import fi.wiskopdr.formuleobjects.TabletOwner;
import fi.wiskopdr.opdrnav.MyOpdrContainer;
import fi.wiskopdr.opdrnav.OpdrNavStruct;
import fi.wiskopdr.tekstobjects.FeedbackTekstArea;
import fi.wiskopdr.tekstobjects.TekstArea;
import fi.wiskopdr.tekstobjects.TekstInteractiePanelVak;

public class AntwoordFormuleVak extends AntwoordVak implements InteractiePanel, CBookAware
{
	private static int GOED = 1;
	private static int FOUT = 0;
	private static int HALF = 2;
	private static int GEEN = 3;
	
	private FormuleButton gelijkwaardigKnop,terugKnop, subKnop, rekenKnop;
	private boolean	herleiding;
	private boolean	exact;
	private boolean	significant;
	private int soortHerleiding = 0;
	private boolean ingevuld = false;
	boolean nagekeken = false;
	
	private boolean isGelijkwaardig = false;
	private boolean pastGelijkwaardig = false; // t.b.v. nakijken eerste stap 
	private boolean isHerleid = false;
	private boolean isExact = false;
	private boolean pastExact = false; // t.b.v. nakijken eerste stap
	private boolean isSignificant = false;
	private int puntenGelijkwaardig = 10;
	private int puntenHerleiding = 0;
	private int puntenExact = 0;
	private int puntenSignificant = 0;
	private boolean startString = false;
	
	private int score;
	private int scoreMax;
	private int errorCount;
	private int attemptsCount;
	private Vector attempts;
	private boolean correct;
	private boolean fout;
	private boolean stapOk;
	
	private ImageComponent goedIC, foutIC, halfIC, huidigIC;
	private ImageComponent feedbackIC;
	private Expressie[] juisteAntwoorden;
	private double[] absPrecisions;
	private Expressie[] juisteVormen;
	
	private PijlVak pijlVak;
	private PijlVak[] pijlVakken;
	private int pijlX = 140;
	private FormuleVak[] formuleVakken;
	private int stapNr;
	
	private boolean scoreCumulatief = false;
	private int[] scoreContainer;
	private int currentAnswerModel = 0;
	
	private boolean stappen;
	private boolean hasPrefix;
	private String prefix;
	private FormuleVak[] prefixVakken;
	
	private ImageComponent[] imageComponenten;
	private ImageComponent[] imageComponentenStap;
	
	private int mode = 0;
	
	private int formuleVakX = 30;
	private int formuleVakY = 10;
	private int pijlVakH = 20;
	private int stapH = 3*WiskOpdr.formuleFont0.getSize()/4;
	private int imageCompX = 5;
	private Font formuleVakFont = (!WiskOpdr.formTimes || WiskOpdr.mac) ? WiskOpdr.formuleFont0Mac : WiskOpdr.formuleFont0; //new Font("TimesRoman",Font.PLAIN,16);
	
	private Expressie substitutie;
	private Vergelijking[] gebruikersSubstituties;
	private FormuleEditor gebruikersSubstitutiesVak;
	
	private TekstArea feedbackTekst;
    
    //private String gekozenAntwoordString, gekozenStartString;
    private String formuleVakString;
    
    private Hashtable[] answerModels;
    private String[] randomVarNamen;
	private Hashtable randomVarWaarden;
	
	private String feedback;
	private boolean feedbackSize;
	private int feedbackWidth = 195;
	private int feedbackHeight = 20;
	private boolean exactP;
	private boolean significantP;
	private boolean herleidingP;
	private boolean gelijkwaardigP;
	private boolean hasFeedback;
	private int puntenFeedback;
	private int goedHalfFout;
	
	private boolean syntaxFout;
	
	private boolean hasSubKnop;
	private boolean hasSubKnopExtra;
	private boolean rmKnop;
	
	private boolean check;
	private boolean teltMee;
	
	private boolean logOption;
	private String logID;
	private String logIDLabel;
	
	private boolean[][] logObjectives;
	private int[][] possibleMisconceptions;
	private int[][] measuredMisconceptions;
	
	private double eqTestValueMin = 0;
	private double eqTestValueMax = 5;
	
	private int aantalDecRm = 10;
	
	private Image feedbackBallonImage;
	private JPanel mwFeedbackPanel;
	private JButton feedbackCloseButton;
	
	private FormuleVak formuleVakSimpel;
	private boolean uitw = false;
	
	private boolean casCheck = false;
	private String casString = "";
	private boolean casResult = false;
	
	//ideas instellingen
	
	private boolean tips; //ideas aan
	private String strategieDomein;
	private int foutenTeller;
	private int feedbackModus;
	private boolean tipGebruikt;
	private boolean hulpGebruikt;
	private int aftrekTipHulp;
	
	private boolean tipOpBalk = true;
    private boolean hulpOpBalk = false;
    private boolean stapOpBalk = false;
    private boolean solveOpBalk = false;
    private boolean meerTips = false;
    private boolean tipBijFout = false;
    private boolean feedbackBijFout = false;
    private boolean hulpBijTip = false;
    
    private int aftrekTip = 0;
	private int aftrekHulp = 0;
	private int aftrekStap = 0;
	private int aftrekSolve = 0;
	
	private boolean diagnose = false;
	
	private int ideasPuntenAftrek;
    
    private FormuleButton tip1Knop, tip2Knop, hulpKnop, solveKnop;
	
    
    private Hashtable changedTexts = new Hashtable();
    
	
	private boolean hidePrefix = (WiskOpdr.language.equals(new Locale("en"))) ? true : false;
	
	private boolean eigenOpdr = false;
	private FormuleButton wisKnop;
	
	static boolean fontOvererving;
	
	private FunctieMVDefSet functieMVDefSet = new FunctieMVDefSet();
	private Vergelijking[] antwoordSubstituties;
	
	private CBookEventHandler cbookEventHandler = new CBookEventHandler(this);
	
	
	
	
	public static void zetFontOverervingForm(boolean b)
	{	fontOvererving = b;
	}
	
	public AntwoordFormuleVak()
	{	super(true);
		remove(formuleVak);
		formuleVak.removeActionListener(this);
		
		goedIC = new ImageComponent(WiskOpdr.GOEDKRUL);
		goedIC.setLocation(0,0);
		goedIC.setVisible(false);
		add(goedIC);
		
		foutIC = new ImageComponent(WiskOpdr.FOUTKRUIS);
		foutIC.setLocation(0,0);
		foutIC.setVisible(false);
		add(foutIC);
		
		halfIC = new ImageComponent(WiskOpdr.HALFKRUL);
		halfIC.setLocation(0,0);
		halfIC.setVisible(false);
		add(halfIC);
		
		feedbackIC = new ImageComponent(WiskOpdr.loadImage("resources/feedback.gif"));
		feedbackIC.setLocation(0,0);
		feedbackIC.setVisible(false);
		add(feedbackIC);
		
		
		
		huidigIC = foutIC;
		
		gelijkwaardigKnop = new FormuleButton("gelijkwaardig", FormuleButton.NAVIGATIEKNOP);
		gelijkwaardigKnop.setBounds(280,2,20,20);
		gelijkwaardigKnop.addActionListener(this);
		zetOpBalk(gelijkwaardigKnop);
		
		terugKnop = new FormuleButton("terug", FormuleButton.NAVIGATIEKNOP);
		terugKnop.setBounds(302,2,20,20);
		terugKnop.addActionListener(this);
		zetOpBalk(terugKnop);
		
		subKnop = new FormuleButton("sub", FormuleButton.BEWERKINGSKNOP);
		subKnop.setBounds(250,2,20,20);
		subKnop.addActionListener(this);
		zetOpBalk(subKnop);
		subKnop.setVisible(false);
		
		rekenKnop = new FormuleButton("rmvak");
		rekenKnop.setBounds(250,2,20,20);
		rekenKnop.addActionListener(this);
		zetOpBalk(rekenKnop);
		rekenKnop.setVisible(false);
		
		wisKnop = new FormuleButton("wis");
		wisKnop.setBounds(250, 2, 40, 20);
		wisKnop.addActionListener(this);
		wisKnop.setVisible(false);
		zetOpBalk(wisKnop);
		
		int knoppenStartX = 20;
	    tip1Knop = new FormuleButton(WiskOpdr.rb.getString("ideasTip"));
		tip1Knop.setBounds(knoppenStartX+300,2,30,20);
		tip1Knop.addActionListener(this);
		tip1Knop.setVisible(false);
		zetOpBalk(tip1Knop);
		
		tip2Knop = new FormuleButton(WiskOpdr.rb.getString("ideasHelp"));
		tip2Knop.setBounds(knoppenStartX+335,2,30,20);
		tip2Knop.addActionListener(this);
		tip2Knop.setVisible(false);
		zetOpBalk(tip2Knop);
			
		hulpKnop = new FormuleButton(WiskOpdr.rb.getString("ideasStap"));
		//hulpKnop.setFont(new Font("SansSerif", Font.BOLD,16));
		hulpKnop.setBounds(knoppenStartX+370,2,30,20);
		hulpKnop.addActionListener(this);
		hulpKnop.setVisible(false);
		zetOpBalk(hulpKnop);
		
		solveKnop = new FormuleButton("solve");
		//hulpKnop.setFont(new Font("SansSerif", Font.BOLD,16));
		solveKnop.setBounds(knoppenStartX+405,2,40,20);
		solveKnop.addActionListener(this);
		solveKnop.setVisible(false);
		zetOpBalk(solveKnop);
		
		if("MW".equals(WiskOpdr.deployVariant))
		{	gelijkwaardigKnop.setBounds(280,0,21,23);
			terugKnop.setBounds(302,0,21,23);
			subKnop.setBounds(250,0,22,23);
			
			tip1Knop.setBounds(377, 0, 22, 23);
			tip2Knop.setBounds(403, 0, 22, 23);
			solveKnop.setBounds(403, 0, 22, 23);
			
			formuleVakX=50;
						
		}
		if("GR".equals(WiskOpdr.deployVariant))
		{	gelijkwaardigKnop.setBounds(280,0,15,23);
			terugKnop.setBounds(302,0,16,23);
			subKnop.setBounds(250,0,16,23);
			
			tip1Knop.setBounds(377, 0, 22, 23);
			tip2Knop.setBounds(403, 0, 22, 23);
			solveKnop.setBounds(403, 0, 22, 23);
			
			formuleVakX=30;
			imageCompX=4;
		}
		
		
        
        //if(WiskOpdr.deployVariant!=null && WiskOpdr.deployVariant.equals("GR")) {
        //    gelijkwaardigKnop.setBounds(290,2,90,20);
        //    terugKnop.setBounds(390,2,80,20);
        //}
		
		pijlVakken = new PijlVak[100];
		formuleVakken = new FormuleVak[100];
		prefixVakken = new FormuleVak[100];
		imageComponenten = new ImageComponent[100];
		imageComponentenStap = new ImageComponent[100];
		scoreContainer = new int[100];
		for(int i=0 ; i<scoreContainer.length ; i++) {
			scoreContainer[i] = -1;
		}
		
		formuleVakken[0] = new FormuleVak();
		formuleVakken[0].setFont(formuleVakFont);
		formuleVakken[0].addActionListener(this);
		formuleVakken[0].setLocation(formuleVakX,formuleVakY);
		add(formuleVakken[0]);
		
		
		formuleVak = formuleVakken[0];
		formuleVakSimpel = formuleVakken[0];
		
		attempts = new Vector();
		
		feedbackTekst = new FeedbackTekstArea(); // FIXME xwidgetmanager
		feedbackTekst.setSize(195,20);
		feedbackTekst.setBackground(new Color(255,255,200));
		if("MW".equals(WiskOpdr.deployVariant))feedbackTekst.setBackground(new Color(250,255,220));
		if("GR".equals(WiskOpdr.deployVariant))feedbackTekst.setBackground(new Color(255,255,255));
		feedbackTekst.setBorders(true);
		feedbackTekst.setCloseable(true);
		feedbackTekst.addActionListener(this);
		
		if("MW".equals(WiskOpdr.deployVariant))
		{
			feedbackBallonImage = NWButtonUI.loadImage("DWO-tekstballon.png", this);
			mwFeedbackPanel = new JPanel()
			{	public void paintComponent(Graphics g)
				{	int H = feedbackTekst.getHeight()+20;
					int w = getWidth();
					g.drawImage(feedbackBallonImage, 0, 0, w, 20, 0, 0, w, 20, this);
					g.drawImage(feedbackBallonImage, 0, 20, w, H-10, 0, 20, w, 25, this);
					g.drawImage(feedbackBallonImage, 0, H-10, w, H,0, 49, w, 59,  this);
				}
			};
			mwFeedbackPanel.setLayout(null);
			mwFeedbackPanel.setSize(283,59);
			feedbackCloseButton = new FormuleButton("maal",FormuleButton.MEERKNOP);
			feedbackCloseButton.addActionListener(this);
			feedbackCloseButton.setBackground(new Color(255,255,200));
			feedbackCloseButton.setBounds(mwFeedbackPanel.getSize().width-17, 5, 12,12);
			mwFeedbackPanel.add(feedbackCloseButton);
			feedbackTekst.setBorders(false);
		}
		if("GR".equals(WiskOpdr.deployVariant))
		{
			if("GR".equals(WiskOpdr.deployVariant))feedbackBallonImage = NWButtonUI.loadImage("DWO-tekstballon-gr.png", this);
			mwFeedbackPanel = new JPanel()
			{	public void paintComponent(Graphics g)
				{	int H = feedbackTekst.getHeight()+40;
					int w = getWidth();
					g.drawImage(feedbackBallonImage, 0, 0, w, 40, 0, 0, w, 40, this);
					g.drawImage(feedbackBallonImage, 0, 40, w, H-10, 0, 40, w, 44, this);
					g.drawImage(feedbackBallonImage, 0, H-10, w, H,0, 44, w, 54,  this);
				}
			};
			mwFeedbackPanel.setLayout(null);
			mwFeedbackPanel.setSize(206,54);
			feedbackCloseButton = new FormuleButton("maal",FormuleButton.MEERKNOP);
			feedbackCloseButton.addActionListener(this);
			feedbackCloseButton.setBackground(new Color(255,255,255));
			feedbackCloseButton.setBounds(mwFeedbackPanel.getSize().width-18, 18, 12,12);
			mwFeedbackPanel.add(feedbackCloseButton);
			feedbackTekst.setBorders(false);
		}
		
		gebruikersSubstitutiesVak = new FormuleEditor(true);
		 gebruikersSubstitutiesVak.setBounds(0,30,220,150);
		 //gebruikersSubstitutiesVak.setFont(font);
		 gebruikersSubstitutiesVak.addActionListener(this);
		 gebruikersSubstitutiesVak.setMultiLine(true);
		 gebruikersSubstitutiesVak.setResizable(true);
		 gebruikersSubstitutiesVak.setBackgroundContentPane(new Color(255,255,230));
	     setLayer((Component)gebruikersSubstitutiesVak, JLayeredPane.POPUP_LAYER.intValue());
	     //zetOpRoot(gebruikersSubstitutiesVak);
	     
	     if(WiskOpdr.misconceptions != null && WiskOpdr.misconceptions.length>0)
	     {	 possibleMisconceptions = new int[WiskOpdr.misconceptions.length][];
	     	 measuredMisconceptions = new int[WiskOpdr.misconceptions.length][];
		     for(int i=0 ; i<WiskOpdr.misconceptions.length ; i++)
		     {	 possibleMisconceptions[i] = new int[WiskOpdr.misconceptions[i].length];
		     	 measuredMisconceptions[i] = new int[WiskOpdr.misconceptions[i].length];
		    	 for(int j=0 ; j<WiskOpdr.misconceptions[i].length ; j++)
			     {  possibleMisconceptions[i][j] = 0;
			     	measuredMisconceptions[i][j] = 0;
			     }
		     }
	     }
	}
	
	public boolean hasPrefix()
	{	return hasPrefix;
	}
	
	public boolean isPrefix(FormuleVak fv)
	{	if(!hasPrefix)return false;
		for(int i=0 ; i<stapNr+1 ; i++)
		{	if(prefixVakken[i]==fv) return true;
		}
		return false;
	}
	
	public Component getComponentSimpel()
	{	return formuleVakSimpel;
	}
	
	public void zetSimpelFormuleVak(FormuleVak f)
	{	
		
		int x = formuleVakX + (hasPrefix ? prefixVakken[0].getSize().width : 0);
		//formuleVakken[0].setLocation(x,formuleVakY);
		f.setLocation(x,formuleVakY);
		//f.setLocation(formuleVakX,10);
		f.setBorder(true);
		
		if(stapNr>0)//(!startString && stapNr>0 || stapNr>1)
		{	boolean nk = nagekeken;
			stapTerug();
			formuleVakSimpel = null;
			maakStap();
			nagekeken = nk;
		}
		int fsx = formuleVakSimpel.getX();
		int fsy = formuleVakSimpel.getY();
		
		remove(formuleVakSimpel);
		formuleVakSimpel = f;
		formuleVakSimpel.setLocation(fsx,fsy);
		
		formuleVakken[stapNr] = formuleVakSimpel;
		formuleVak = formuleVakSimpel;
		add(formuleVakSimpel);
		
		goedIC.setVisible(false);
		goedIC.zetKlein(false);
		add(goedIC);
		halfIC.setVisible(false);
		halfIC.zetKlein(false);
		add(halfIC);
		foutIC.setVisible(false);
		foutIC.zetKlein(false);
		add(foutIC);
		
		if((mode==0 || mode==1 || nagekeken)  && (stapNr>0 || !startString))kijkNa();
		else if(stapNr==0 && startString) maakStap();
	}
	
	public Component getGoedIC()
	{	return goedIC;
	}
	
	public Component getFoutIC()
	{	return foutIC;
	}
	
	public Component getHalfIC()
	{	return halfIC;
	}
	
	public Component getFeedbackIC()
	{
		return feedbackIC;
	}
	
	public Component getToetsIC()
	{	return imageComponenten[0];
	}
	
	public void setFont(Font f)
	{	//formuleVakFont = f;
	}
	
	public InteractieEditPanel getEditPanel()
	{	return new AntwoordFormuleVakEditPanel(0);
	}
	
	public void zetMode(int mode)
	{	this.mode = mode;
	}
	
	public void setBounds(int x, int y, int b, int h)
	{	//if(WiskOpdr.deployVariant!=null && WiskOpdr.deployVariant.equals("GR")) 
		//{	gelijkwaardigKnop.setBounds(290,2,90,20);
        //	terugKnop.setBounds(390,2,80,20);
		//}
		//
		if("MW".equals(WiskOpdr.deployVariant))
		{
			gelijkwaardigKnop.setLocation(b-48,0);
        	terugKnop.setLocation(b-25,0);
        	subKnop.setLocation(b-85,0);
        	rekenKnop.setLocation(b-110,0);
        	tip1Knop.setLocation(b - 240, 0);
			tip2Knop.setLocation(b - 210, 0);
			hulpKnop.setLocation(b - 180, 0);
			solveKnop.setLocation(b - 150, 0);
		}
		else if("GR".equals(WiskOpdr.deployVariant))
		{
			gelijkwaardigKnop.setLocation(b-36,0);
        	terugKnop.setLocation(b-18,0);
        	subKnop.setLocation(b-85,0);
        	rekenKnop.setLocation(b-110,0);
        	tip1Knop.setLocation(b - 200, 0);
			tip2Knop.setLocation(b - 175, 0);
			hulpKnop.setLocation(b - 180, 0);
			solveKnop.setLocation(b - 150, 0);
		}
		else	
		{	gelijkwaardigKnop.setLocation(b-47,2);
        	terugKnop.setLocation(b-25,2);
        	subKnop.setLocation(b-85,2);
        	rekenKnop.setLocation(b-110,2);
        	tip1Knop.setLocation(b - 240, 2);
			tip2Knop.setLocation(b - 210, 2);
			hulpKnop.setLocation(b - 180, 2);
			solveKnop.setLocation(b - 150, 2);
        	wisKnop.setLocation(b - 120, 2);
    		
		}
        super.setBounds(x,y,b,h);
	}
	
	public void wis()
	{	for(int i=0 ; i<stapNr+1; i++)
	    {	if(i>0)remove(formuleVakken[i]);
	    	if(prefixVakken[i]!=null)remove(prefixVakken[i]);
	    	if(imageComponenten[i]!=null) remove(imageComponenten[i]);
	    	if(imageComponentenStap[i]!=null) remove(imageComponentenStap[i]);
	    	if(i<stapNr)remove(pijlVakken[i]);
	    }
		
	    formuleVak = formuleVakken[0];
	    formuleVakSimpel = formuleVakken[0];
	    if(huidigIC!=null)
	    {	huidigIC.setVisible(false);
	    	huidigIC.setLocation(0,0);
	    }
	    stapNr = 0;
	    //if(startString)stapNr = 1;
	    formuleVakken[stapNr].setEditable(true);
	    //stapNr = 0;
	    correct = false;
	    fout = false;
	    score = 0;
	    errorCount = 0;
	    attemptsCount = 0;
	    nagekeken = false;
	    ingevuld = false;
	    if(feedbackTekst!=null)
	    {	if("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant))	remove(mwFeedbackPanel);
	    	else remove(feedbackTekst);
	    }
	    startString = false;
	    hasPrefix = false;
		prefix = null;
		formuleVakken[0].setLocation(formuleVakX,formuleVakY);
		zetGeenAntwoord(false);
		zetRandomFout(false);
		
	    substitutie = null;
	    pijlVak = null;
	    
	    attempts = new Vector();
		
	}
	
	public Hashtable getEditState()
	{	return null;
	}
	
	public void setEditState(Hashtable h)
	{	
		if(fontOvererving && getParent() instanceof TekstInteractiePanelVak)
		{	Font geerftFont = ((TekstInteractiePanelVak)getParent()).getTekstVak().getFont();
			if (!geerftFont.getName().equals("TimesRoman") && WiskOpdr.formTimes && !WiskOpdr.mac) {
				geerftFont = new Font("TimesRoman", geerftFont.getStyle(), geerftFont.getSize() * 6 / 5);
			}
			formuleVakFont = geerftFont;
			formuleVakken[0].setFont(formuleVakFont);
			formuleVakSimpel.setFont(formuleVakFont);
		}
		if(h.containsKey("hasFeedback"))
			hasFeedback = ((Boolean)h.get("hasFeedback")).booleanValue();
		feedbackIC.setVisible(hasFeedback);
	}
	
	public void setAnswerModel(int nr)
	{	currentAnswerModel = nr;
		Hashtable h = answerModels[nr];
		if(h==null) return;
	
		String antwoordString = "$f@";
		boolean gelijkwaardig = true;
		boolean herleiding = false;
		boolean exact = false;
		boolean significant = false;
		int soortHerleiding = 0;
		int puntenGelijkwaardig = 10;
		int puntenHerleiding = 0;
		int puntenExact = 0;
		int puntenFeedback = 0;
		String feedback = "";
		int feedbackWidth = 195;
		int feedbackHeight = 20;
		String vormString = "$f@";
		int goedHalfFout = 0;
		boolean[][] logMisconceptions;
		
		
		
		if(h!=null) 
		{	if(h.containsKey("antwoordString")) antwoordString = (String)h.get("antwoordString");
			if(h.containsKey("gelijkwaardig")) gelijkwaardig = ((Boolean)h.get("gelijkwaardig")).booleanValue();
			if(h.containsKey("herleiding")) herleiding = ((Boolean)h.get("herleiding")).booleanValue();
			if(h.containsKey("exact")) exact = ((Boolean)h.get("exact")).booleanValue();
			if(h.containsKey("significant")) significant = ((Boolean)h.get("significant")).booleanValue();
			if(h.containsKey("stappen")) stappen = ((Boolean)h.get("stappen")).booleanValue();
			if(h.containsKey("soortHerleiding")) soortHerleiding = ((Integer)h.get("soortHerleiding")).intValue();
			if(h.containsKey("puntenGelijkwaardig")) puntenGelijkwaardig = ((Integer)h.get("puntenGelijkwaardig")).intValue();
			if(h.containsKey("puntenHerleiding")) puntenHerleiding = ((Integer)h.get("puntenHerleiding")).intValue();
			if(h.containsKey("puntenExact")) puntenExact = ((Integer)h.get("puntenExact")).intValue();
			if(h.containsKey("puntenFeedback")) puntenFeedback = ((Integer)h.get("puntenFeedback")).intValue();
			if(h.containsKey("feedback")) feedback = (String)h.get("feedback");
			if(h.containsKey("feedbackWidth")) feedbackWidth = ((Integer)h.get("feedbackWidth")).intValue();
			if(h.containsKey("feedbackHeight")) feedbackHeight = ((Integer)h.get("feedbackHeight")).intValue();
			if(h.containsKey("vormString")) vormString = (String)h.get("vormString");
			if(h.containsKey("goedHalfFout")) goedHalfFout = ((Integer)h.get("goedHalfFout")).intValue();
			if(h.containsKey("logMisconceptions")) logMisconceptions = (boolean[][])h.get("logMisconceptions");
			
		}	
		exactP = exact;
		significantP = significant;
		herleidingP = herleiding;
		gelijkwaardigP = gelijkwaardig;
		this.goedHalfFout = goedHalfFout;
		this.puntenFeedback = puntenFeedback;
		
		FunctieMV.setFunctieMVDefSet(functieMVDefSet);
        try         
        {   antwoordString = FormuleParser.randomizeString(antwoordString,randomVarNamen,randomVarWaarden);
        }
        catch(Exception e)
        {   antwoordString = "$f???@";
            zetGeenAntwoord(true);
            //antwoordSyntaxFout = true;
        }
        
        try         
        {   vormString = FormuleParser.randomizeString(vormString,randomVarNamen,randomVarWaarden);
        }
        catch(Exception e)
        {   vormString = "$f???@";
            zetGeenAntwoord(true);
        }
        
        try         
        {   feedback = modifyFeedback(feedback);
        	feedback = FormuleParser.randomizeTekstVakString(feedback, randomVarNamen, randomVarWaarden);
        }
        catch(Exception e)
        {   feedback = "$f???@";
        }
        FunctieMV.setFunctieMVDefSet(null);
        
        zetJuisteAntwoord(antwoordString, false);
        zetJuisteVorm(vormString);
        
       
        //this.gekozenAntwoordString = antwoordString;
        this.feedback = feedback;
        if(feedbackSize)
        {	this.feedbackWidth = feedbackWidth;
        	this.feedbackHeight = feedbackHeight;
        }
       
	}
	
	public void zetOpdracht(Hashtable h, String[] randomVars, Hashtable randomValues)
	{
		if(fontOvererving && getParent() instanceof TekstInteractiePanelVak)
		{	Font geerftFont = ((TekstInteractiePanelVak)getParent()).getTekstVak().getFont();
			if (!geerftFont.getName().equals("TimesRoman") && WiskOpdr.formTimes && !WiskOpdr.mac) {
				geerftFont = new Font("TimesRoman", geerftFont.getStyle(), geerftFont.getSize() * 6 / 5);
			}
			formuleVakFont = geerftFont;
			formuleVakken[0].setFont(formuleVakFont);
			formuleVakSimpel.setFont(formuleVakFont);
		}
		
		randomVarNamen = randomVars;
        randomVarWaarden = randomValues;
        
		String antwoordString = "$f@";
		String startString = "$f@";
		boolean herleiding = false;
		boolean exact = false;
		boolean significant = false;
		boolean stappen = true;
		int soortHerleiding = 0;
		int puntenGelijkwaardig = 10;
		int puntenHerleiding = 0;
		int puntenExact = 0;
		int puntenSignificant = 0;
		Hashtable[] answerModels = null;
		boolean hasFeedback = false;
		boolean feedbackSize = false;
		String vormString = "$f@";
		boolean hasSubKnop = false;
		boolean hasSubKnopExtra = false;
		boolean rmKnop = false;
		boolean check = true;
		boolean teltMee = true;
		boolean logOption = false;
		String logID = "";
		String logIDLabel = "";
		double eqTestValueMin = 0;
		double eqTestValueMax = 5;
		int aantalDecRm = 10;
		int scoreMax = 0;
		boolean uitw = false;
		boolean tips = false;
		String strategieDomein = "";
		boolean tipOpBalk = true;
        boolean hulpOpBalk = false;
        boolean stapOpBalk = false;
        boolean solveOpBalk = false;
        boolean meerTips = false;
        boolean tipBijFout = false;
        boolean feedbackBijFout = false;
        boolean hulpBijTip = false;
        Hashtable changedTexts = new Hashtable();
        Hashtable ideasInstellingen = null;
        boolean eigenOpdr = false;
        boolean boxMetRand = true;
        boolean[][] logObjectives = null;
        String[] antwoordSubStrings = null;
		String[] antwoordFuncStrings = null;
		boolean scoreCumulatief = false;
		
        		
		if(h.containsKey("antwoordString")) antwoordString = (String)h.get("antwoordString");
		if(h.containsKey("startString")) startString = (String)h.get("startString");
		if(h.containsKey("herleiding")) herleiding = ((Boolean)h.get("herleiding")).booleanValue();
		if(h.containsKey("exact")) exact = ((Boolean)h.get("exact")).booleanValue();
		if(h.containsKey("significant")) significant = ((Boolean)h.get("significant")).booleanValue();
		if(h.containsKey("stappen")) stappen = ((Boolean)h.get("stappen")).booleanValue();
		if(h.containsKey("soortHerleiding")) soortHerleiding = ((Integer)h.get("soortHerleiding")).intValue();
		if(h.containsKey("puntenGelijkwaardig")) puntenGelijkwaardig = ((Integer)h.get("puntenGelijkwaardig")).intValue();
		if(h.containsKey("puntenHerleiding")) puntenHerleiding = ((Integer)h.get("puntenHerleiding")).intValue();
		if(h.containsKey("puntenExact")) puntenExact = ((Integer)h.get("puntenExact")).intValue();
		if(h.containsKey("puntenSignificant")) puntenSignificant = ((Integer)h.get("puntenSignificant")).intValue();
		if(h.containsKey("answerModels")) answerModels = (Hashtable[])h.get("answerModels");
		if(h.containsKey("hasFeedback")) hasFeedback = ((Boolean)h.get("hasFeedback")).booleanValue();
		if(h.containsKey("feedbackSize")) feedbackSize = ((Boolean)h.get("feedbackSize")).booleanValue();
		if(h.containsKey("vormString")) vormString = (String)h.get("vormString");
		if(h.containsKey("subKnop")) hasSubKnop = ((Boolean)h.get("subKnop")).booleanValue();
		if(h.containsKey("subKnopExtra")) hasSubKnopExtra = ((Boolean)h.get("subKnopExtra")).booleanValue();
		if(h.containsKey("rmKnop")) rmKnop = ((Boolean)h.get("rmKnop")).booleanValue();
		if(h.containsKey("check")) check = ((Boolean)h.get("check")).booleanValue();
		if(h.containsKey("teltMee")) teltMee = ((Boolean)h.get("teltMee")).booleanValue();
		if(h.containsKey("logOption")) logOption = ((Boolean)h.get("logOption")).booleanValue();
		if(h.containsKey("logID")) logID = (String)h.get("logID");
		if(h.containsKey("logIDLabel")) logIDLabel = (String)h.get("logIDLabel");
		if(h.containsKey("eqTestValueMin")) eqTestValueMin = ((Double)h.get("eqTestValueMin")).doubleValue();
		if(h.containsKey("eqTestValueMax")) eqTestValueMax = ((Double)h.get("eqTestValueMax")).doubleValue();
		if(h.containsKey("aantalDecRm")) aantalDecRm = ((Integer)h.get("aantalDecRm")).intValue();
		if(h.containsKey("scoreMax")) scoreMax = ((Integer)h.get("scoreMax")).intValue();
		if(h.containsKey("uitw")) uitw = ((Boolean)h.get("uitw")).booleanValue();
		if(h.containsKey("eigenOpdr")) eigenOpdr = ((Boolean)h.get("eigenOpdr")).booleanValue();
		if(h.containsKey("boxMetRand")) boxMetRand = ((Boolean)h.get("boxMetRand")).booleanValue();
		if(h.containsKey("tips")) tips = ((Boolean)h.get("tips")).booleanValue();
		if(tips){
			if(h.containsKey("ideasInstellingen")) ideasInstellingen = (Hashtable)h.get("ideasInstellingen");
	    }
		if(h.containsKey("logObjectives")) logObjectives = (boolean[][])h.get("logObjectives");
		if (h.containsKey("antwoordSubStrings")) antwoordSubStrings = (String[]) h.get("antwoordSubStrings");
		if (h.containsKey("antwoordFuncStrings")) antwoordFuncStrings = (String[]) h.get("antwoordFuncStrings");
		if (h.containsKey("scoreCumulatief")) scoreCumulatief = ((Boolean) h.get("scoreCumulatief")).booleanValue();
		
		//setScoreDataFormule(herleiding,exact,soortHerleiding, puntenGelijkwaardig, puntenHerleiding, puntenExact);
		
		this.herleiding = herleiding;
		this.exact = exact;
		this.significant = significant;
		this.soortHerleiding = soortHerleiding;
		this.puntenGelijkwaardig = puntenGelijkwaardig;
		this.puntenHerleiding = puntenHerleiding;
		this.puntenExact = puntenExact;
		this.puntenSignificant = puntenSignificant;
		this.logObjectives = logObjectives;
		this.scoreCumulatief = scoreCumulatief;
		
		zetStappen(stappen);
		
		if (antwoordFuncStrings != null) {
			
			for (int i = 0; i < antwoordFuncStrings.length; i++)
			{
				String[] functieDelen = antwoordFuncStrings[i].split("=");
				if(functieDelen.length<2) break;
				String functieExpressieString = "$f"+functieDelen[1];
				String functieNaam = functieDelen[0].substring(2, functieDelen[0].indexOf('('));
				String varString = functieDelen[0].substring(functieDelen[0].indexOf('(')+1, functieDelen[0].indexOf(')'));
				String[] functieMVVariabelen = varString.split(",");
				//String functieVariabele = functieDelen[0].substring(functieDelen[0].indexOf('(')+1, functieDelen[0].indexOf('(')+2);
				//System.out.println("varString:"+varString);
				//System.out.println("functieExpressieString:"+functieExpressieString);
				//System.out.println("functieMVVariabelen:"+functieMVVariabelen[0]);
				try
				{
					functieExpressieString = FormuleParser.randomizeString(functieExpressieString, randomVars, randomValues);
					
				} catch (Exception e) {
					
				}
				Expressie functieExpressie = FormuleParser.geefExpressie(functieExpressieString);
				//functieDefSet.addFunctieExpressie(functieNaam, functieVariabele, functieExpressie);
				functieMVDefSet.addFunctieMVExpressie(functieNaam, functieMVVariabelen, functieExpressie);
			}
		}
		FunctieMV.setFunctieMVDefSet(functieMVDefSet);
        try         
        {   antwoordString = FormuleParser.randomizeString(antwoordString,randomVars,randomValues);
        }
        catch(Exception e)
        {   antwoordString = "$f???@";
            zetGeenAntwoord(true);
        }
        try         
        {   vormString = FormuleParser.randomizeString(vormString,randomVars,randomValues);
        }
        catch(Exception e)
        {   vormString = "$f???@";
            zetGeenAntwoord(true);
        }
        zetJuisteAntwoord(antwoordString);
        zetJuisteVorm(vormString);
         
        try         
        {   startString = FormuleParser.randomizeString(startString,randomVars,randomValues);
        }
        catch(Exception e)
        {   startString = "$f???@";
        }
		zetStartString(startString);
		
		if (antwoordSubStrings != null) {
			boolean subCorrect = true;
			antwoordSubstituties = new Vergelijking[antwoordSubStrings.length];
			for (int i = 0; i < antwoordSubStrings.length; i++)
			{
				try
				{
					antwoordSubStrings[i] = FormuleParser.randomizeString(antwoordSubStrings[i], randomVars, randomValues);
					//antwoordSubstituties[i] = (FormuleParser.parseVergelijking(antwoordSubStrings[i], functieDefSet)).geefVergelijking(0);
					antwoordSubstituties[i] = (FormuleParser.parseVergelijking(antwoordSubStrings[i], functieMVDefSet)).geefVergelijking(0);
					if (!antwoordSubstituties[i].geefExpLinks().isVar())
						subCorrect = false;
				} catch (Exception e) {
					subCorrect = false;
				}
			}
			if (!subCorrect)
				antwoordSubstituties = null;
			
		}
		else
			antwoordSubstituties = null;
		
		FunctieMV.setFunctieMVDefSet(null);
		
		
        //this.gekozenAntwoordString = antwoordString;
        //this.gekozenStartString = startString;
        this.answerModels = answerModels;
        
        if (answerModels!=null)
        {
        	boolean[][] logMisconceptions = null;
        	for (int i = 0; i < answerModels.length; i++)
        	{	
        		if (answerModels[i] != null && answerModels[i].containsKey("logMisconceptions"))
        		{	
        			logMisconceptions = (boolean[][])answerModels[i].get("logMisconceptions");
        			for (int j = 0; j < logMisconceptions.length && j < possibleMisconceptions.length; j++)
        			{	
        				for (int k = 0; k < logMisconceptions[j].length && k < possibleMisconceptions[j].length; k++)
            			{	
        					if (logMisconceptions[j][k])
        						possibleMisconceptions[j][k] = 1;
            			}
        			}
        		}
        	}
        }
        
        this.hasFeedback = hasFeedback;
        this.feedbackSize = feedbackSize;
        this.hasSubKnop = hasSubKnop;
        this.hasSubKnopExtra = hasSubKnopExtra;
        this.rmKnop = rmKnop;
        this.check = check;
        this.teltMee = teltMee;
        this.logOption = logOption;
        this.logID = logID;
        this.logIDLabel = logIDLabel;
        this.eqTestValueMin = eqTestValueMin;
        this.eqTestValueMax = eqTestValueMax;
        this.aantalDecRm = aantalDecRm;
        this.scoreMax = scoreMax;
        this.uitw = uitw;
        this.tips = tips;
        this.eigenOpdr = eigenOpdr;
                
		if(tips && ideasInstellingen!=null)
		{	setIdeas(ideasInstellingen);
			
		}
       
        subKnop.setVisible(hasSubKnop);
        rekenKnop.setVisible(rmKnop);
        wisKnop.setVisible(eigenOpdr && !this.startString);
        zetMetRand(boxMetRand);
        feedbackIC.setVisible(false);
        
        
		
	}
	
	public void setIdeas(Hashtable h)
	{
		if(h.containsKey("strategieDomein")) strategieDomein = (String)h.get("strategieDomein");
		if(h.containsKey("tipOpBalk")) tipOpBalk = ((Boolean)h.get("tipOpBalk")).booleanValue();
        if(h.containsKey("hulpOpBalk")) hulpOpBalk = ((Boolean)h.get("hulpOpBalk")).booleanValue();
        if(h.containsKey("stapOpBalk")) stapOpBalk = ((Boolean)h.get("stapOpBalk")).booleanValue();
        if(h.containsKey("solveOpBalk")) solveOpBalk = ((Boolean)h.get("solveOpBalk")).booleanValue();
        if(h.containsKey("meerTips")) meerTips = ((Boolean)h.get("meerTips")).booleanValue();
        if(h.containsKey("tipBijFout")) tipBijFout = ((Boolean)h.get("tipBijFout")).booleanValue();
        if(h.containsKey("feedbackBijFout")) feedbackBijFout = ((Boolean)h.get("feedbackBijFout")).booleanValue();
        if(h.containsKey("hulpBijTip")) hulpBijTip = ((Boolean)h.get("hulpBijTip")).booleanValue();
        if(h.containsKey("changedTexts")) changedTexts = (Hashtable)h.get("changedTexts");
        if (h.containsKey("diagnose"))
			diagnose = ((Boolean) h.get("diagnose")).booleanValue();
		if (h.containsKey("aftrekTip"))
			aftrekTip = ((Integer) h.get("aftrekTip")).intValue();
		if (h.containsKey("aftrekHulp"))
			aftrekHulp = ((Integer) h.get("aftrekHulp")).intValue();
		if (h.containsKey("aftrekStap"))
			aftrekStap = ((Integer) h.get("aftrekStap")).intValue();
		if (h.containsKey("aftrekSolve"))
			aftrekSolve = ((Integer) h.get("aftrekSolve")).intValue();
        
        tip1Knop.setVisible(tipOpBalk);
		tip2Knop.setVisible(hulpOpBalk);
		hulpKnop.setVisible(stapOpBalk);
		solveKnop.setVisible(solveOpBalk);
		
	}
	
	
	public Hashtable getState()
	{	int stapNr=0;
		String[] formuleVakInhouden=null;
		int[] vvY = null;
		String[] pijlVakInhouden=null;
		String[] pijlVakOperatoren=null;
		int[] pvY = null;
		boolean ingevuld = true;
		boolean nagekeken = false;
		//String gewensteAntwoordString; 
		//String startString; 
		String antwoordString;
		String substitutieString = "";
		Vector attempts = new Vector();
		int attemptsCount = 0;
		int errorCount = 0;
		String[] gebruikersSubStrings;
		int ideasPuntenAftrek = 0;
		
		if(!("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant))) kijkNa(-1,false);
		
		stapNr = this.stapNr;
		nagekeken = this.nagekeken;
		ingevuld = this.ingevuld;
		if(!ingevuld && mode!=1 && mode!=2 && mode!=3)
		{	try
			{	formuleVak.finish();
				if(formuleVak!=formuleVakSimpel)formuleVakSimpel.finish();
			}
			catch(Exception e){}
		}	
	    
		formuleVakInhouden = new String[stapNr+1];
		vvY = new int[stapNr+1];
		for(int i=0 ; i<stapNr+1; i++)
	    {	if(formuleVakken[i]==null) formuleVakInhouden[i] = "$f@";
			else formuleVakInhouden[i] = formuleVakken[i].toString();
	    	vvY[i] = formuleVakken[i].getLocation().y;
	    }
	    
	    
	    pijlVakInhouden = new String[stapNr];
		pijlVakOperatoren = new String[stapNr];
	    pvY = new int[stapNr];
		for(int i=0 ; i<stapNr; i++)
	    {	pijlVakInhouden[i] = pijlVakken[i].geefExpressieString();
	    	pijlVakOperatoren[i] = pijlVakken[i].geefOperator();
			pvY[i] = pijlVakken[i].getLocation().y;
	    }
	    
	    ingevuld = this.ingevuld;
	    nagekeken = this.nagekeken;
	    //gewensteAntwoordString = this.gekozenAntwoordString;
		//startString = this.gekozenStartString;
		antwoordString = toString();
		attempts = this.attempts;
		attemptsCount = this.attemptsCount;
		errorCount = this.errorCount;
		ideasPuntenAftrek = this.ideasPuntenAftrek;
		
		if(substitutie!=null) substitutieString = "$f" + substitutie.toString() + "@";
		
		gebruikersSubStrings = gebruikersSubstitutiesVak.geefRegels();
	    		
		if(logOption)
		{	
			Hashtable logMap = new Hashtable();
			
			String logString = "";
			String string = formuleVakken[this.stapNr].toString();
			
			if("$f@".equals(string))logString = "";
			else
			{	//string = FormuleParser.schoon(FormuleParser.formuleString(string));
				//string = StringUtils.replaceStr(string, "(0-", "(-");
				//logString = FormuleParser.pel(string);
				
				logString = formuleVakken[stapNr].toMathML();
			}
			
			logMap.put("logIDLabel", logIDLabel);
			logMap.put("logAnswer", logString);
			logMap.put("logScore", new Integer(score));
			logMap.put("logMaxScore", new Integer(scoreMax));
			logMap.put("logErrorCount", new Integer(errorCount));
			logMap.put("logAttemptsCount", new Integer(attemptsCount));
			logMap.put("logAttempts", attempts);
			
			WiskOpdr.setLog(logID, logMap);
		}
	    			    
	    Hashtable h = new Hashtable();
	    h.put("stapNr", new Integer(stapNr));
	    h.put("formuleVakInhouden", formuleVakInhouden);
	    h.put("vvY", vvY);
	    h.put("pijlVakInhouden", pijlVakInhouden);
	    h.put("pijlVakOperatoren", pijlVakOperatoren);
	    h.put("pvY", pvY);
	    h.put("ingevuld", new Boolean(ingevuld));
	    h.put("nagekeken", new Boolean(nagekeken));
	    //h.put("gewensteAntwoordString", gewensteAntwoordString);
	    //h.put("startString", startString);
	    h.put("antwoordString", antwoordString);
	    h.put("substitutieString", substitutieString);
	    h.put("attempts", attempts);
	    h.put("attemptsCount", new Integer(attemptsCount));
	    h.put("errorCount", new Integer(errorCount));
	    h.put("gebruikersSubStrings", gebruikersSubStrings);
	    h.put("ideasPuntenAftrek", new Integer(ideasPuntenAftrek));
	    return h;
	}
	
	public void zetMaat()
	{
		
	}
	
	public int geefAsHoogte()
	{
		return (getFontMetrics(WiskOpdr.tekstFont)).getAscent();
	}
	
	public void zetNagekeken(boolean b)
	{	if(ingevuld || syntaxFout)
		nagekeken = b;
	}
	
	public void zetStappen(boolean b)
	{	stappen = b;
		gelijkwaardigKnop.setVisible(b);
		terugKnop.setVisible(b);
	}
	
	/*public void setScoreDataFormule(boolean herleiding, boolean exact, int soortHerleiding,int puntenGelijkwaardig, int puntenHerleiding, int puntenExact)
	{	this.herleiding = herleiding;
		this.exact = exact;
		this.soortHerleiding = soortHerleiding;
		this.puntenGelijkwaardig = puntenGelijkwaardig;
		this.puntenHerleiding = puntenHerleiding;
		this.puntenExact = puntenExact;
	}*/
	
	public Expressie geefExpressie()
	{	return formuleVak.geefExpressie();
	}
	
	public void zetStartString(String s)
	{	formuleVak.vulVak(s);
		if(s.length()>3)
		{	formuleVak.setEditable(false);
			stapOk = true;
			maakStap();
			startString = true;
			setAttempt(true);
		}
		else
		{ 	startString = false;
		}
	}
	
	public void vulVak(String s)
	{	formuleVak.vulVak(s);
	}
	
	public String toString()
	{	return formuleVak.toString();
	}
	
	public void zetGoedFout(int uitslag)
	{
		boolean checkWas = check;
		check = true;
		zetGoedFout(uitslag,-1);
		check = checkWas;
		
	}
	
	public void zetGoedFout(int uitslag, int formuleVakNr)
	{	
	    if(!check && uitslag!=GEEN) return;
	    if(formuleVakNr==-1)
		{	if(huidigIC!=null)
            {   huidigIC.setVisible(false);
                huidigIC.setLocation(0,0);
            }
			if(uitslag==GEEN)return;
            if(uitslag==GOED)huidigIC = goedIC;
			else if(uitslag==FOUT)huidigIC = foutIC;
			else if(uitslag==HALF)huidigIC = halfIC;
			else if(uitslag==GEEN)huidigIC = halfIC;
			int y = formuleVakken[stapNr].getLocation().y + formuleVakken[stapNr].getSize().height-5;
			if("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant)) y = formuleVakken[stapNr].getLocation().y + formuleVakken[stapNr].getSize().height/2-15;
			huidigIC.setLocation(imageCompX,y);
			huidigIC.setVisible(true);
		}
		else
		{	if(imageComponenten[formuleVakNr]!=null) remove(imageComponenten[formuleVakNr]);
			if(uitslag==GOED)imageComponenten[formuleVakNr] = new ImageComponent(WiskOpdr.GOEDKRUL);
			else if(uitslag==FOUT) imageComponenten[formuleVakNr] = new ImageComponent(WiskOpdr.FOUTKRUIS);
			else if(uitslag==HALF) imageComponenten[formuleVakNr] = new ImageComponent(WiskOpdr.HALFKRUL);
			else if(uitslag==GEEN) imageComponenten[formuleVakNr] = new ImageComponent(null);
			int y = formuleVakken[formuleVakNr].getLocation().y + formuleVakken[formuleVakNr].getSize().height-20;
			if("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant)) y = formuleVakken[stapNr].getLocation().y + formuleVakken[stapNr].getSize().height/2-15;
			imageComponenten[formuleVakNr].setLocation(imageCompX,y);
			add(imageComponenten[formuleVakNr]);
		}
	}
	
	private void zetGoedFoutStap(int uitslag, int pijlVakNr)
	{
		if (!check)
			return;
		if (imageComponentenStap[pijlVakNr] != null)
			remove(imageComponentenStap[pijlVakNr]);
		if (uitslag == GOED)
			imageComponentenStap[pijlVakNr] = new ImageComponent(WiskOpdr.GOEDKRUL);
		else if (uitslag == FOUT)
			imageComponentenStap[pijlVakNr] = new ImageComponent(WiskOpdr.FOUTKRUIS);
		else if (uitslag == HALF)
			imageComponentenStap[pijlVakNr] = new ImageComponent(WiskOpdr.HALFKRUL);
		else if (uitslag == GEEN)
			imageComponentenStap[pijlVakNr] = new ImageComponent(null);
		int x = pijlVakken[pijlVakNr].getLocation().x + 20;
		int y = pijlVakken[pijlVakNr].getLocation().y + pijlVakken[pijlVakNr].getSize().height / 2 - 15;
		imageComponentenStap[pijlVakNr].setLocation(x, y);
		add(pijlVakken[pijlVakNr]);
		add(imageComponentenStap[pijlVakNr], 0);
	}
	
	public void zetJuisteAntwoord(String s)
	{	zetJuisteAntwoord(s,true);
	}
	
	public void zetJuisteAntwoord(String s, boolean preparePrefix)
	{	
		if(s.length()>5 && s.substring(0,6).equals("$fCAS{"))
		{	casCheck = true;
			casString = s.substring(6,s.length()-2);
			juisteAntwoorden = new Expressie[1]; // NOT NULL
			return;
		}
		else casCheck = false;
		
		int index = s.indexOf("=");
        if(index==-1) index = s.indexOf("\u2248");
		if(index>-1)
		{	prefix = s.substring(0,index+1)+"@";
			hasPrefix = true;
			s = "$f"+ s.substring(index+1);
			if(preparePrefix)
			{	if(prefixVakken[0] != null) remove(prefixVakken[0]);
				prefixVakken[0] = new FormuleVak();
				prefixVakken[0].setFont(formuleVakFont);
				prefixVakken[0].setLocation(formuleVakX,formuleVakY);
				prefixVakken[0].setEditable(false);
				prefixVakken[0].setSelectable(false);
				prefixVakken[0].vulVak(prefix);
				add(prefixVakken[0]);
				int x = formuleVakX + prefixVakken[0].getSize().width;
				formuleVakken[0].setLocation(x,formuleVakY);
			}
		}
	
		s = s.substring(2,s.length()-1);
		String[] antwoordStrings = StringUtils.split(s,";");
		
		if(antwoordStrings.length==1)antwoordStrings = StringUtils.split(s,"::");
		
		juisteAntwoorden = new Expressie[antwoordStrings.length];
		absPrecisions = new double[antwoordStrings.length];
		
		FormuleParser p = new FormuleParser();
		for(int i=0 ; i<antwoordStrings.length; i++) 
		{	if(antwoordStrings[i]!=null)
			{	String[] antwoordDelen = StringUtils.split(antwoordStrings[i],"\u00b1");//"�");
				if(antwoordDelen.length>1)
				{
					String antwoordStr = "$f" + antwoordDelen[0] + "@";
					//juisteAntwoorden[i] = p.parse(p.schoon(p.formuleString(antwoordStr)));
					juisteAntwoorden[i] = FormuleParser.geefExpressie(antwoordStr, functieMVDefSet);
					Expressie e = FormuleParser.geefExpressie("$f" + antwoordDelen[1] + "@", functieMVDefSet);
					if (e !=null && !Double.isNaN(e.geefWaarde())) absPrecisions[i] = e.geefWaarde();
					
				}
				else
				{
					String antwoordStr = "$f" + antwoordStrings[i] + "@";
					//juisteAntwoorden[i] = p.parse(p.schoon(p.formuleString(antwoordStr)));
					juisteAntwoorden[i] = FormuleParser.geefExpressie(antwoordStr, functieMVDefSet);
				}
			}
			else
			{
				String antwoordStr = "$f" + antwoordStrings[i] + "@";
				//juisteAntwoorden[i] = p.parse(p.schoon(p.formuleString(antwoordStr)));
				juisteAntwoorden[i] = FormuleParser.geefExpressie(antwoordStr, functieMVDefSet);
			}
		}
		
	}
	
	public void zetJuisteVorm(String s)
	{	s = s.substring(2,s.length()-1);
		String[] antwoordStrings = StringUtils.split(s,";");
		
		if(antwoordStrings.length==1)antwoordStrings = StringUtils.split(s,"::");
		
		juisteVormen = new Expressie[antwoordStrings.length];
		
		FormuleParser p = new FormuleParser();
		for(int i=0 ; i<antwoordStrings.length; i++) 
		{	String antwoordStr = "$f" + antwoordStrings[i] + "@";
			//juisteVormen[i] = p.parse(p.schoon(p.formuleString(antwoordStr)));
			juisteVormen[i] = FormuleParser.geefExpressie(antwoordStr, functieMVDefSet);
		}
		
	}
	
	public void stop()
	{	if(mode!=1)kijkNa();
		if(ingevuld) produceAction("changed");
		if(getParent()==null && feedbackTekst.getParent()!=null)
		{	produceAction("feedbackWeg");
		}
			
	}
	
//	public void start()
//	{	formuleVak.requestFocus();
//		if(getParent()==null)return;
//		else if(getParent().getParent() instanceof TabletOwner)
//			((TabletOwner)getParent().getParent()).zetTablet(this,getLocation().x, getLocation().y+getSize().height-2);
//		else if(getParent().getParent().getParent() instanceof TabletOwner)
//			((TabletOwner)getParent().getParent().getParent()).zetTablet(this,getLocation().x, getLocation().y+getSize().height-2);
//		else if(getParent().getParent().getParent().getParent() instanceof TabletOwner)
//			((TabletOwner)getParent().getParent().getParent().getParent()).zetTablet(this,getLocation().x, getLocation().y+getSize().height-2);
//		else if(getParent().getParent().getParent().getParent().getParent() instanceof TabletOwner)
//			((TabletOwner)getParent().getParent().getParent().getParent().getParent()).zetTablet(this,getLocation().x, getLocation().y+getSize().height-2);
//		else if(getParent().getParent().getParent().getParent().getParent().getParent() instanceof TabletOwner)
//			((TabletOwner)getParent().getParent().getParent().getParent().getParent().getParent()).zetTablet(this,getLocation().x, getLocation().y+getSize().height-2);
//	}
	public void start()
	{
		formuleVak.requestFocus();
		Container parent = getParent();
		if (parent == null)
			return;
		int x = getLocation().x + parent.getLocation().x;
		int y = getLocation().y + parent.getLocation().y;
		int h = parent.getSize().height;
		for (int i = 0; parent != null && i < 40; i++)
		{
			if (parent instanceof TabletOwner)
			{
				((TabletOwner) parent).zetTablet(this, x + 20, y + 20);

				break;
			}
			else
			{
				parent = parent.getParent();
				if (parent == null)
					return;
				x += parent.getLocation().x;
				y += parent.getLocation().y;
			}
		}

	}
	
	public void opnieuw()
	{	score = 0;
		correct = false;
		for(int i=0 ; i<100; i++)
	    {	if(formuleVakken[i]!=null)remove(formuleVakken[i]);
	    	if(prefixVakken[i]!=null)remove(prefixVakken[i]);
	    	if(pijlVakken[i]!=null)remove(pijlVakken[i]);
	    	if(imageComponenten[i]!=null)remove(imageComponenten[i]);
	    	if(imageComponentenStap[i]!=null)remove(imageComponentenStap[i]);
	    }
	    stapNr = 0;
	    formuleVakken[0] = new FormuleVak();
		formuleVakken[0].setFont(formuleVakFont);
		formuleVakken[0].addActionListener(this);
		formuleVakken[0].setLocation(formuleVakX,formuleVakY);
		add(formuleVakken[0]);
		formuleVak = formuleVakken[0];
		formuleVakSimpel = formuleVakken[0];
	}
	
	public void setAttempt()
	{	setAttempt(false);
	}
	
	public void setAttempt(boolean start)
	{
		if (formuleVak.toString().equals("$f@"))
			return;

		String goedFout = "";
		if (huidigIC == goedIC && huidigIC.isVisible())
			goedFout = "goed";
		if (huidigIC == halfIC && huidigIC.isVisible())
			goedFout = "half";
		if (huidigIC == foutIC && huidigIC.isVisible())
			goedFout = "fout";

		String formule = "";
		String string = formuleVak.toString();
		if (start)
			string = formuleVakken[0].toString();
		String attemptFormuleString = FormuleParser.schoon(FormuleParser.formuleString(string));
		attemptFormuleString = StringUtils.replaceStr(attemptFormuleString, "(0-", "(-");
		formule = FormuleParser.pel(attemptFormuleString);

		String fbTekst = "";
		if (feedbackTekst.isVisible() && feedbackTekst.getParent() != null)
			fbTekst = feedbackTekst.getText();

		String s = formule;
		s = s + "   ;   ";
		if (start)
			s = s + "start";
		else
			s = s + goedFout;
		s = s + "   ;   ";
		s = s + "score = " + score;
		s = s + "   ;   ";
		s = s + new Date().toString();
		s = s + "   ;   ";
		if (start)
			s = s + "Regelnummer = " + 0;
		else
			s = s + "Regelnummer = " + stapNr;
		s = s + "   ;   ";
		s = s + fbTekst;

		attempts.addElement(s);
	}
	
	public JPanel getMWFeedbackComponent()
	{	return mwFeedbackPanel;
	}
	
	public TekstArea getFeedbackComponent()
	{	return feedbackTekst;
	}
	
	public String modifyFeedback(String tekst)
	{
		for(int i=tekst.length()-1 ; i>-1; i--)
		{	if(tekst.charAt(i)=='@')
			{	int index = tekst.substring(0,i).lastIndexOf("$f");
				
				int indexF = tekst.substring(0,i).lastIndexOf("$f");
				int indexA = tekst.substring(0,i).lastIndexOf("$A");
				int indexV = tekst.substring(0,i).lastIndexOf("$V");
				int indexH = tekst.substring(0,i).lastIndexOf("$H");
				int indexI = tekst.lastIndexOf("$I", i);
				index = Math.max(indexF, indexA);
				index = Math.max(index, indexV);
				index = Math.max(index, indexH);
				index = Math.max(index, indexI);
				
				String formString = tekst.substring(index,i+1);
				for(int j=formString.length()-1 ; j>-1; j--)
				{	if(formString.charAt(j)=='}')
					{	int index1 = formString.substring(0,j).lastIndexOf("{");
						String parseString = formString.substring(index1+1,j);
						if(parseString.equals("ANS"))
						{	parseString = formuleVak.toString();
							parseString = parseString.substring(2,parseString.length()-1);
						}
						formString = ""+formString.substring(0,index1)+parseString+formString.substring(j+1);
						j=index1;
					}	
				}	
				
				tekst = ""+tekst.substring(0,index)+formString+tekst.substring(i+1);
				
				i=index;
			}
		}
		return tekst;
	}
	
	public void setFeedback(String tekst, boolean closeable)
	{	feedbackTekst.setText("");
		feedbackTekst.setCloseable(closeable);
		feedbackTekst.setSize(feedbackWidth,feedbackHeight);
		feedbackTekst.setLocation(getSize().width-250,formuleVakken[stapNr].getLocation().y + formuleVakken[stapNr].getSize().height + 10);
		feedbackTekst.setText(tekst);
		feedbackTekst.resize();
		if("MW".equals(WiskOpdr.deployVariant))
		{	
			int h = feedbackTekst.getHeight()+20;
			mwFeedbackPanel.setSize(mwFeedbackPanel.getWidth(),h);
			mwFeedbackPanel.setLocation(getSize().width-310,formuleVakken[stapNr].getLocation().y + formuleVakken[stapNr].getSize().height + 5);
			feedbackTekst.setLocation(50,10);
			mwFeedbackPanel.add(feedbackTekst,0);
			add(mwFeedbackPanel,0);
		}
		else if("GR".equals(WiskOpdr.deployVariant))
		{	
		    feedbackTekst.setOpaque(false);
            int h = feedbackTekst.getHeight()+40;
			mwFeedbackPanel.setSize(mwFeedbackPanel.getWidth(),h);
			int fbx = Math.min(getSize().width-220, formuleVakken[stapNr].getLocation().x + formuleVakken[stapNr].getSize().width + 5);
			int fby = formuleVakken[stapNr].getLocation().y + formuleVakken[stapNr].getSize().height + 5;
			mwFeedbackPanel.setLocation(fbx, fby);
			feedbackTekst.setLocation(5,25);
			mwFeedbackPanel.add(feedbackTekst,0);
			add(mwFeedbackPanel,0);
		}
		else add(feedbackTekst);
		produceAction("feedback");
	}
	
	public void checkStap(int pijlVakNr, FormuleVak fv1, FormuleVak fv2)
	{
		Expressie e1 = fv1.geefExpressie();
		if(e1==null) 
		{	String antwoordString = fv1.toString();
			if(antwoordString.charAt(antwoordString.length()-2)=='=' || antwoordString.charAt(antwoordString.length()-2)=='\u2248')
			{	int isIndex = antwoordString.length()-2;
				antwoordString = antwoordString.substring(0,isIndex)+"@";
				//e1 = FormuleParser.geefExpressie(antwoordString);
				e1 = FormuleParser.geefExpressie(antwoordString, functieMVDefSet);
			}
		}
		
		// check of substitutie in het spel
		if (substitutie != null)
		{
			e1 = e1.substitueer(substitutie, "u");
		}

		Expressie e2 = fv2.geefExpressie();
		if(e2==null) 
		{	String antwoordString = fv2.toString();
			if(antwoordString.charAt(antwoordString.length()-2)=='=' || antwoordString.charAt(antwoordString.length()-2)=='\u2248')
			{	int isIndex = antwoordString.length()-2;
				antwoordString = antwoordString.substring(0,isIndex)+"@";
				//e2 = FormuleParser.geefExpressie(antwoordString);
				e2 = FormuleParser.geefExpressie(antwoordString, functieMVDefSet);
			}
		}
		
		// check of substitutie in het spel
		if (substitutie != null)
		{
			e2 = e2.substitueer(substitutie, "u");
		}

		if(e1==null || e2==null)
		{	zetGoedFoutStap(FOUT,pijlVakNr);
			return;
		}
		
		boolean casNodig = e1.toString().indexOf("$i")>-1 || e1.toString().indexOf("$d")>-1 || e1.toString().indexOf("$T")>-1  || e1.toString().indexOf("$S")>-1  || e1.toString().indexOf("$P")>-1;
        if(casNodig)
            e1 = Expressie.evalWithCAS(e1);
        casNodig = e2.toString().indexOf("$i")>-1 || e2.toString().indexOf("$d")>-1 || e2.toString().indexOf("$T")>-1  || e2.toString().indexOf("$S")>-1  || e2.toString().indexOf("$P")>-1;
        if(casNodig)
            e2 = Expressie.evalWithCAS(e2);
        
		boolean gelijkw = Algebra.isGelijkwaardig(e1, e2);
		zetGoedFoutStap(gelijkw?GOED:FOUT,pijlVakNr);
		
	}
	
	public void kijkNaIdeas()
	{	if(WiskOpdr.ideas==null)
		{	JOptionPane.showMessageDialog(this,"Feedbackservice not available");
			return;
		}
		score = 0; 
		if(stapNr<1)return;
		FormuleVak fvVorig = formuleVakken[stapNr-1];
		FormuleVak fvHuidig = formuleVakken[stapNr];
		if(formuleVak.geefExpressie()!=null && isGelijkwaardig) fvHuidig = formuleVak;
		
		boolean geenOplossing = false;
		Expressie antwoordGeen = null;
		
		if(fvHuidig.toString().equals("$f@"))return;
		else if(fvHuidig.geefExpressie()==null)
		{
			setFeedback(WiskOpdr.rb.getString("feedbackTekst14"),false);
			correct = false;
			fout = false;
			repaint();
			return;
		}
		
		ingevuld = true;
		if(feedbackTekst!=null)
		{	if("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant))removeSoft(mwFeedbackPanel);
			else removeSoft(feedbackTekst);
		}
		String eStringVorig = fvVorig.toString();
		eStringVorig = verwijderIsTeken(eStringVorig);
		//String vglStriktVorig = FormuleParser.geefExpressie(eStringVorig).toStringStrikt();
		String vglStriktVorig = FormuleParser.geefExpressie(eStringVorig, functieMVDefSet).toStringStrikt();
		vglStriktVorig = vertaalNaarIdeasExpressie(vglStriktVorig);
		
		String eStringHuidig = fvHuidig.toString();
		eStringHuidig = verwijderIsTeken(eStringHuidig);
		//String vglStriktHuidig = FormuleParser.geefExpressie(eStringHuidig).toStringStrikt();
		String vglStriktHuidig = FormuleParser.geefExpressie(eStringHuidig, functieMVDefSet).toStringStrikt();
		//System.out.println(vglStriktHuidig);
		vglStriktHuidig = vertaalNaarIdeasExpressie(vglStriktHuidig);
		
		//System.out.println("diagnose("+vglStriktVorig+","+vglStriktHuidig+","+strategieDomein+")");
		
		RuleIF rule = WiskOpdr.ideas.diagnose(vglStriktVorig,vglStriktHuidig,strategieDomein);
		//System.out.println(rule.getName());
		
		String feedback = translateRule(rule.getName());
		if(rule.getName().equals("notequiv")) 
		{	feedback = translateRule(rule.getName());
			zetGoedFout(FOUT,-1);
			stapOk = false;
			correct = false;
			fout = true;
		}
		else if(rule.getName().equals("buggy")) 
		{	if(feedbackBijFout) feedback = translateRule(rule.getId());
			zetGoedFout(FOUT,-1);
			stapOk = false;
			correct = false;
			fout = true;
		}
		else if(rule.isReady()) 
		{	feedback = translateRule("ready");
			zetGoedFout(GOED,-1);
			stapOk = false;
			if(!hasFeedback)correct = true;
			fout = false;
			score = puntenExact+puntenHerleiding+puntenGelijkwaardig;
		}
		else if(rule.getName().equals("similar")) 
		{	feedback = translateRule(rule.getName());
			zetGoedFout(GEEN,-1);
			stapOk = false;
			correct = false;
			fout = false;
		}
		
		else if(rule.getName().equals("expected")) 
		{	feedback = translateRule(rule.getName());
			zetGoedFout(HALF,-1);
			stapOk=true;
			correct = false;
			fout = false;
		}
		else if(rule.getName().equals("detour")) 
		{	feedback = translateRule(rule.getName());
			zetGoedFout(HALF,-1);
			stapOk=true;
			correct = false;
			fout = false;
		}
		else if(rule.getName().equals("correct")) 
		{	feedback = translateRule(rule.getName());
			zetGoedFout(HALF,-1);
			stapOk=true;
			correct = false;
			fout = false;
		}
		
		if(fout && tipBijFout)
		{	RuleIF rules = WiskOpdr.ideas.getOneFirst(vglStriktVorig,strategieDomein);
			if(hulpBijTip); //setFeedback(feedback + "\n\nTip: \n" 
					//+ translateRule(rules.getId()) + "\n"
					//,true,3);
			else setFeedback("Tip: \n" 
					+ translateRule(rules.getId()) + "\n"
					,true);
			repaint();
		}
		else
		{	setFeedback(feedback, true);
			repaint();
		}
		
		if(hasFeedback)
		{	kijkNa();
			if(rule.getName().equals("similar")) stapOk = false;
		}
		else 
		{
			if(ingevuld)produceAction("changed");
		}
	}
	
	public void kijkNa()
	{
		if (mode == 0 || mode == 1)
		{
			kijkNa(-1);
			if (ingevuld)
				produceAction("changed");
		}
		if (mode == 2 || mode == 3)
		{
			int start = 0;
			if (startString)
				start = 1;
			if (formuleVakken[stapNr] != null && formuleVakken[stapNr].toString().equals("$f@"))
			{
				boolean nk = nagekeken;
				if (stapNr > 1)
					stapTerug();
				else if (!startString && stapNr > 0)
					stapTerug();
				nagekeken = nk;
			}
			int voortgangsScore = 0;
			for (int i = start; i < stapNr + 1; i++)
			{
				formuleVak = formuleVakken[i];
				checkAntwoord();
				if (i > 0) // geeft een kruis als start = 1
				{
					if (start == 1 && i == start)
					{
						// bij startstring niet stap 1 met de startstring vergelijken, maar stap 1 evalueren
						zetGoedFoutStap(isExact || isGelijkwaardig || pastExact || pastGelijkwaardig ? GOED : FOUT, i - 1); // GOED ook bij DOOR en HALFGOED
					}
					else
					{
						checkStap(i - 1, formuleVakken[i - 1], formuleVakken[i]);
					}
					
					if (i == stapNr)
						kijkNa(i);
				}
				else if (stapNr == 0)
					kijkNa(i);
				if (hasFeedback)
					voortgangsScore = Math.max(voortgangsScore, puntenFeedback);
				// System.out.println("voortgangsScore: "+voortgangsScore);
			}
			if (hasFeedback)
				score = voortgangsScore;
			// System.out.println("Score: "+score);
			if (ingevuld)
				produceAction("changed");
		}
	}
	
	public void kijkNaKlaarKnop()
	{
		int start = 0;
		if(startString) start = 1;
		for(int i=start ; i<stapNr+1 ; i++)
		{	formuleVak = formuleVakken[i];
			Algebra.setTestValues(eqTestValueMin, eqTestValueMax);
			checkAntwoord();
			Algebra.setDefaultTestValues();
			kijkNa(i);
		}
		if(ingevuld)produceAction("checked");
	}
	
	public void kijkNa(int stapNr)
	{
		kijkNa(stapNr, true);
		if(correct && cbookEventHandler.hasListeners("action.correct"))
			cbookEventHandler.fire("action.correct");
		if(fout && cbookEventHandler.hasListeners("action.false"))
			cbookEventHandler.fire("action.false");
		if(fout && getErrorCount()>1 && cbookEventHandler.hasListeners("action.false_2"))
			cbookEventHandler.fire("action.false_2");
	}
	
	public void kijkNa(int stapNr, boolean show)
	{	if(stapNr==-2)
		{
			kijkNaKlaarKnop();
			return;
		}
		if((mode==2 || mode==3) && !show && stapNr==-1)
		{	
			if(formuleVakken[this.stapNr]!=null && formuleVakken[this.stapNr].toString().equals("$f@"))
			{	boolean nk = nagekeken;
				if(this.stapNr>1)stapTerug();
				else if(!startString && this.stapNr>0)stapTerug();
				nagekeken = nk;
			}
		}
		Algebra.setTestValues(eqTestValueMin, eqTestValueMax);
		checkAntwoord(show);
		Algebra.setDefaultTestValues();
		
		if(syntaxFout)
		{	if(show)zetGoedFout(FOUT,stapNr);
			if(show)setFeedback(WiskOpdr.rb.getString("feedbackTekst14"),true);
			return;
		}
		else if(!ingevuld)
		{	if(show)zetGoedFout(GEEN,stapNr);
			return;
		}
		
		else if(hasFeedback)
		{	if(goedHalfFout==0)
			{	if(show)zetGoedFout(GOED,stapNr);
				score = puntenFeedback;
				correct = true;
				fout = false;
				if(show)stapOk = true;
			}
			else if(goedHalfFout==1)
			{	if(show)zetGoedFout(HALF,stapNr);
				score = puntenFeedback;
				correct = false;
				fout = false;
				if(show)stapOk = true;
			}
			else if(goedHalfFout==2)
			{	if(show)zetGoedFout(FOUT,stapNr);
				score = puntenFeedback;
				correct = false;
				fout = true;
				if(show)stapOk = false;
			}
		
		}
		else if(casCheck)
		{	if(casResult)
			{	if(show)zetGoedFout(GOED,stapNr);
				score = scoreMax;
				correct = true;
				fout = false;
				if(show)stapOk = true;
			}
			else 
			{	if(show)zetGoedFout(FOUT,stapNr);
				score = 0;
				correct = false;
				fout = true;
				if(show)stapOk = false;
			}
		}
		else
		{	
			if(significant && !exact)
			{
				if(isGelijkwaardig && isSignificant)
				{	if(show)zetGoedFout(GOED,stapNr);
					score = puntenGelijkwaardig + puntenSignificant;
					correct = true;
					fout = false;
					if(show)stapOk = true;
				}
				else if(isGelijkwaardig && !isSignificant)
				{	if(show)zetGoedFout(HALF,stapNr);
					score = puntenGelijkwaardig;
					correct = false;
					fout = false;
					if(show)stapOk = true;
				}
				else 
				{	if(show)zetGoedFout(FOUT,stapNr);
					score = 0;
					correct = false;
					fout = true;
					if(show)stapOk = false;
				}
			}
			else
			{
				if(!herleiding && !exact) 
				{	if(isGelijkwaardig)
					{	if(show)zetGoedFout(GOED,stapNr);
						score = puntenGelijkwaardig;
						correct = true;
						fout = false;
						if(show)stapOk = true;
					}
					else
					{	if(show)zetGoedFout(FOUT,stapNr);
						score = 0;
						correct = false;
						fout = true;
						if(show)stapOk = false;
					}
				}
				else if(herleiding && !exact)
				{	if(isGelijkwaardig && isHerleid)
					{	if(show)zetGoedFout(GOED,stapNr);
						score = puntenGelijkwaardig + puntenHerleiding;
						correct = true;
						fout = false;
						if(show)stapOk = true;
					}
					else if(isGelijkwaardig && !isHerleid)
					{	if(show)zetGoedFout(HALF,stapNr);
						score = puntenGelijkwaardig;
						correct = false;
						fout = false;
						if(show)stapOk = true;
					}
					else 
					{	if(show)zetGoedFout(FOUT,stapNr);
						score = 0;
						correct = false;
						fout = true;
						if(show)stapOk = false;
					}
				}
				else if(exact && !herleiding)
				{	if(isGelijkwaardig && isExact)
					{	if(show)zetGoedFout(GOED,stapNr);
						score = puntenGelijkwaardig + puntenExact;
						correct = true;
						fout = false;
						if(show)stapOk = true;
					}
					else if(isGelijkwaardig && !isExact)
					{	if(show)zetGoedFout(HALF,stapNr);
						score = puntenGelijkwaardig;
						correct = false;
						fout = false;
						if(show)stapOk = true;
					}
					else 
					{	if(show)zetGoedFout(FOUT,stapNr);
						score = 0;
						correct = false;
						fout = true;
						if(show)stapOk = false;
					}
				}
				else if(exact && herleiding)
				{	if(isGelijkwaardig && isExact)
					{	if(show)zetGoedFout(GOED,stapNr);
						score = puntenGelijkwaardig + puntenHerleiding + puntenExact;
						correct = true;
						fout = false;
						if(show)stapOk = true;
					}
					else if(isGelijkwaardig && isHerleid)
					{	if(show)zetGoedFout(HALF,stapNr);
						score = puntenGelijkwaardig + puntenHerleiding;
						correct = false;
						fout = false;
						if(show)stapOk = true;
					}
					else if(isGelijkwaardig)
					{	if(show)zetGoedFout(HALF,stapNr);
						score = puntenGelijkwaardig;
						correct = false;
						fout = false;
						if(show)stapOk = true;
					}
					else 
					{	if(show)zetGoedFout(FOUT,stapNr);
						score = 0;
						correct = false;
						fout = true;
						if(show)stapOk = false;
					}
				}
			}
		}
	}
	
	public void updateGebruikersSubstituties()
	{
		String [] gebruikersSubstitutieStrings = gebruikersSubstitutiesVak.geefRegels();
		if(gebruikersSubstitutieStrings!=null)
        {   boolean subCorrect = true;
        	gebruikersSubstituties = new Vergelijking[gebruikersSubstitutieStrings.length];
        	for(int i=0 ; i<gebruikersSubstitutieStrings.length ; i++)
			{	try         
		        {   gebruikersSubstituties[i] = (FormuleParser.parseVergelijking(gebruikersSubstitutieStrings[i], functieMVDefSet)).geefVergelijking(0);
			        //gebruikersSubstituties[i] = (FormuleParser.parseVergelijking(gebruikersSubstitutieStrings[i])).geefVergelijking(0);
		        	if(!gebruikersSubstituties[i].geefExpLinks().isVar()) subCorrect = false;
		        }
		        catch(Exception e)
		        {   subCorrect = false;
		        }
		    }
        	if(!subCorrect)gebruikersSubstituties = null;
        }
	}

///**
// * TODO Alternatief met doCas switch Mathematica, Reduce of Ideas.
// * @deprecated Mathematica
// */
//	private void checkCasStatement()	
//	{
//		String checkString = casString;
//		for(int j=checkString.length()-1 ; j>-1; j--)
//		{	if(checkString.charAt(j)=='}')
//			{	int index1 = checkString.substring(0,j).lastIndexOf("{");
//				String parseString = checkString.substring(index1+1,j);
//				if(parseString.equals("ANS"))
//				{	parseString = (formuleVak.geefExpressie()).toStringCAS();
//				}
//				checkString = checkString.substring(0,index1)+parseString+checkString.substring(j+1);
//				j=index1;
//			}	
//		}
//		Expressie e = Expressie.evalWithCAS(checkString);
//		String casResultString = "False";
//		if(e!=null) casResultString = e.toString();
//		casResult = "True".equals(casResultString);	
//	}
	
	private void checkReduceStatement() {

		String ans = formuleVak.geefExpressie().visit(MPReduceConverter.getInstance()).toString();
		String command = StringUtils.replaceStr(casString, "{ANS}", ans);
		String result = MPReduce.evaluate("testbool(" +  command + ")");
		casResult = "1$".equals(result);
	}
	
	
	private void checkStatementViaCas() {
		
		if (Expressie.isCasLocal())
		{
// CAS string is een VergelijkingMeerv
			casResult = checkStatementVergelijkingMeerv();
// String is Reduce invoer (met {ANS} als substitutie)
			//checkReduceStatement();
		} else {
// String is Mathematica invoer
			//checkCasStatement();
// Converteer VergelijkingMeerv naar Mathematica
			casResult = false;//checkStatementVergelijkingMeervCAS();
		}
	}

//	private boolean checkStatementVergelijkingMeervCAS() {
//		VergelijkingMeerv check = getVergelijkingMeerv();
//		if(check == null) {
//			return false;
//		}
//		String checkString = check.visit(MathematicaConverter.getInstance()).toString();
//		Expressie e = Expressie.evalWithCAS(checkString);
//		String casResultString = "False";
//		if(e!=null) casResultString = e.toString();
//		return "True".equals(casResultString);	
//	}

	private VergelijkingMeerv getVergelijkingMeerv() {
		String ans = "$h" + toString().substring(2); // $h ans @
		String vgl = StringUtils.replaceStr(casString, "{ANS}", ans);
		VergelijkingMeerv check = FormuleParser.parseVergelijking("$f" + vgl + "@", functieMVDefSet);
		return check;
	}

	private boolean checkStatementVergelijkingMeerv() {
		VergelijkingMeerv check = getVergelijkingMeerv();
		if(check == null)
		{
			return false;
		}
		String command =  check.visit(MPReduceConverter.getInstance()).toString();
		String result = MPReduce.evaluate("testbool(" +  command + ")");
		return "1$".equals(result);
	}

	public String verwijderIsTeken(String inputStr){
		if(inputStr.charAt(inputStr.length()-2)=='=' || inputStr.charAt(inputStr.length()-2)=='\u2248')
		{	int isIndex = inputStr.length()-2;
			inputStr = inputStr.substring(0,isIndex)+"@";
		}
		return inputStr;
	}
	
	public void checkAntwoord()	
	{
		checkAntwoord(true);
	}
	
	public void checkAntwoord(boolean show)	
	{	
		ingevuld = false;
		syntaxFout = false;
		if ("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant))
			removeSoft(mwFeedbackPanel);
		else
			removeSoft(feedbackTekst);
		Expressie antwoord = formuleVak.geefExpressie(functieMVDefSet);
//		System.out.println("AntwoordFormuleVak.checkAntwoord(): antwoord = " + antwoord);

		boolean exactOnaf = false;
		String[] exactOnafStrings = { "+-", "--", "*-", "/-", "(0-" };
		
		for (int i = 0; i < exactOnafStrings.length; i++)
		{
			exactOnaf = exactOnaf || formuleVak.toString().contains(exactOnafStrings[i]);
		}

		Expressie antwoordNonSub = antwoord;

		updateGebruikersSubstituties();
		if (gebruikersSubstituties != null && antwoord != null)
		{
			for (int i = 0; i < gebruikersSubstituties.length; i++)
			{
				antwoord = antwoord.substitueer(gebruikersSubstituties[i].geefExpRechts(),
					gebruikersSubstituties[i].geefExpLinks().geefVarNaam());
			}
		}
		if (antwoordSubstituties != null && antwoord != null)
		{
			for (int i = 0; i < antwoordSubstituties.length; i++)
			{
				antwoord = antwoord.substitueer(antwoordSubstituties[i].geefExpRechts(),
					antwoordSubstituties[i].geefExpLinks().geefVarNaam());
			}
		}

		if (antwoord == null)
		{
			String antwoordString = formuleVak.toString();
			if (antwoordString.charAt(antwoordString.length() - 2) == '='
				|| antwoordString.charAt(antwoordString.length() - 2) == '\u2248')
			{
				int isIndex = antwoordString.length() - 2;
				antwoordString = antwoordString.substring(0, isIndex) + "@";
				// antwoord = FormuleParser.geefExpressie(antwoordString);
				antwoord = FormuleParser.geefExpressie(antwoordString, functieMVDefSet);
			}
		}
		if (antwoord != null)
		{
			ingevuld = true;

		}
		else if (formuleVak.toString().length() > 3)
		{
			syntaxFout = true;
			return;
		}
		else
			return;

		if (substitutie != null && antwoord != null)
		{
			antwoord = antwoord.substitueer(substitutie, "u");
		}

		String diffVar = "x";
		String diffVar2 = antwoord.geefVarNaam();
		if (diffVar2 != null && !diffVar2.equals(""))
		{
			diffVar = diffVar2;
		}
		antwoord = antwoord.vervangDifferentialen(diffVar);

		Expressie antwoordEvalCAS = null;
		boolean casNodig = false;
		if (antwoord != null)
			casNodig = antwoord.toString().indexOf("$i") > -1 || antwoord.toString().indexOf("$d") > -1
				|| antwoord.toString().indexOf("$T") > -1 || antwoord.toString().indexOf("$S") > -1
				|| antwoord.toString().indexOf("$P") > -1;
		//
		if (casNodig)
		{
			antwoordEvalCAS = Expressie.evalWithCAS(antwoord);

		}
		isGelijkwaardig = false;
		isSignificant = false;

		if (hasFeedback)
		{
			int aantalAnswerModels = answerModels.length;
			for (int h = 0; h < aantalAnswerModels; h++)
			{
				setAnswerModel(h);
				pastGelijkwaardig = false;
				boolean pastHerleid = false;
				pastExact = false;
				boolean pastSignificant = false;

				if (casCheck)
				{
					checkStatementViaCas();
					if (casResult)
					{
						pastGelijkwaardig = true;
						pastHerleid = true;
						pastExact = true;
						pastSignificant = true;
					}
				}
				else
				{
					for (int i = 0; i < juisteAntwoorden.length; i++)
					{
						if (casNodig)
							pastGelijkwaardig = pastGelijkwaardig || AntwoordChecker.checkGelijkwaardig(antwoordEvalCAS,
								juisteAntwoorden[i], absPrecisions[i]);
						else
							pastGelijkwaardig = pastGelijkwaardig
								|| AntwoordChecker.checkGelijkwaardig(antwoord, juisteAntwoorden[i], absPrecisions[i]);

						pastSignificant = pastSignificant
							|| AntwoordChecker.checkSignificant(antwoord, juisteAntwoorden[i]);

						if (Algebra.isBreukPlusGetal(juisteAntwoorden[i]))
							pastExact = pastExact
								|| AntwoordChecker.checkExactBreukPlusGetal(formuleVak.toString(), juisteAntwoorden[i]);
						else if (hasSubKnop)
							pastExact = pastExact || AntwoordChecker.checkExact(antwoordNonSub, juisteAntwoorden[i]);
						else
							pastExact = pastExact || AntwoordChecker.checkExact(antwoord, juisteAntwoorden[i]);
					}
					for (int i = 0; juisteVormen != null && i < juisteVormen.length; i++)
					{
						if (Algebra.isScalarMaalVector(juisteVormen[i])) // als de gewenste vorm een veelvoud van een vector is (t.b.v. normaalvector)
						{
							pastHerleid = pastHerleid || Algebra.isJuisteScalarMaalVector(antwoord, juisteVormen[i]);
							if (pastHerleid) // de gegeven vector is een goede normaalvector
							{
								isGelijkwaardig = true;
								pastGelijkwaardig = true;
								pastExact = true;
								pastSignificant = true;
							}
						}
						else 
							pastHerleid = pastHerleid
								|| AntwoordChecker.checkHerleiding(antwoord, juisteVormen[i], soortHerleiding);
					}
					if (!gelijkwaardigP)
						pastGelijkwaardig = true;
					if (!herleidingP)
						pastHerleid = true;
					if (!exactP)
						pastExact = true;
					if (!significantP)
						pastSignificant = true;
				}

				boolean answerModelFits = pastGelijkwaardig && pastHerleid && pastExact && pastSignificant;
// FIXME bij casCheck GEEN juisteAntwoorden[] 
				if (juisteAntwoorden[0] != null && juisteAntwoorden[0].toString().equals("else"))
					answerModelFits = true;
				if (answerModelFits)
				{
					scoreContainer[stapNr] = currentAnswerModel;
					
					if (!feedback.trim().equals("") && show)
						setFeedback(feedback, true);
					else if (getParent() == null && feedbackTekst.getParent() != null)
					{
						produceAction("feedbackWeg");
					}
					if (answerModels != null)
					{
						boolean[][] logMisconceptions = null;
						if (answerModels[h].containsKey("logMisconceptions"))
						{
							logMisconceptions = (boolean[][]) answerModels[h].get("logMisconceptions");
							for (int j = 0; j < logMisconceptions.length && j < measuredMisconceptions.length; j++)
							{
								for (int k = 0; k < logMisconceptions[j].length
									&& k < measuredMisconceptions[j].length; k++)
								{
									if (logMisconceptions[j][k])
										measuredMisconceptions[j][k] = 1;
								}
							}
						}
					}
					break;
				}
			}
		}
		else if (casCheck)
		{
			checkStatementViaCas();
		}
		else
		{
			// System.out.println(formuleVak.toString());
			// System.out.println(FormuleParser.formuleString(formuleVak.toString()));
			// System.out.println(FormuleParser.schoon(FormuleParser.formuleString(formuleVak.toString())));
			// System.out.println(FormuleParser.pel(FormuleParser.schoon(FormuleParser.formuleString(formuleVak.toString()))));

			isHerleid = false;
			for (int i = 0; i < juisteAntwoorden.length; i++)
			{
				if (casNodig)
					isGelijkwaardig = isGelijkwaardig
						|| AntwoordChecker.checkGelijkwaardig(antwoordEvalCAS, juisteAntwoorden[i], absPrecisions[i]);
				else
					isGelijkwaardig = isGelijkwaardig
						|| AntwoordChecker.checkGelijkwaardig(antwoord, juisteAntwoorden[i], absPrecisions[i]);
				if (soortHerleiding != 0)
					isHerleid = AntwoordChecker.checkHerleiding(antwoord, juisteAntwoorden[0], soortHerleiding);

				if (significant)
					isSignificant = isSignificant || AntwoordChecker.checkSignificant(antwoord, juisteAntwoorden[i]);

				if (Algebra.isBreukPlusGetal(juisteAntwoorden[i]))
					isExact = AntwoordChecker.checkExactBreukPlusGetal(formuleVak.toString(), juisteAntwoorden[i]);
				else if (hasSubKnopExtra)
					isExact = AntwoordChecker.checkExact(antwoordNonSub, juisteAntwoorden[i]);
				else
					isExact = !exactOnaf && AntwoordChecker.checkExact(antwoord, juisteAntwoorden[i]);

				if (isExact)
					break;
			}
			for (int i = 0; soortHerleiding == 0 && i < juisteVormen.length; i++)
			{
				if (Algebra.isScalarMaalVector(juisteVormen[i])) // als de gewenste vorm een veelvoud van een vector is (normaalvector)
				{
					isHerleid = isHerleid || Algebra.isJuisteScalarMaalVector(antwoord, juisteVormen[i]);
					if (isHerleid) // de gegeven vector is een goede normaalvector
					{
						isGelijkwaardig = true;
						pastGelijkwaardig = true;
						pastExact = true;
					}
				}
				else 
					isHerleid = isHerleid || AntwoordChecker.checkHerleiding(antwoord, juisteVormen[i], soortHerleiding);
				
				if (isHerleid)
					break;
			}
		}
		repaint();
	}	
	
	public int getIpId(){return 0;}
	
	public String getIpExpString(){return null;}
	
	public int getScore()
	{	if(!teltMee) return 0;
		if (tips)
			return Math.max(0, score - ideasPuntenAftrek);
		if(mode==1)
			return Math.max(0, score-errorCount*2);
		if(scoreCumulatief) {
			int scoreCum = 0;
			int[] scoreContainerTemp = new int[scoreContainer.length];
			for(int i=0 ; i<scoreContainer.length ; i++) {
				scoreContainerTemp[i] = scoreContainer[i];
			}
			for(int i=0 ; i<scoreContainerTemp.length ; i++) {
				int amNr = scoreContainerTemp[i];
				if(amNr>-1) {
					scoreCum = scoreCum + (Integer)answerModels[amNr].get("puntenFeedback");
					for(int j=i ; j<scoreContainerTemp.length ; j++) {
						if(scoreContainerTemp[j] == amNr) 
							scoreContainerTemp[j] = -1;
					}
				}
			}
			if(mode==1)
				return Math.max(0, scoreCum-errorCount*2);
			return scoreCum;
		}
	    return score;
	}
	
	public int[][] getScoreObjectives()
	{	if(logObjectives==null)return null;
		int[][] scoreObjectives = new int[logObjectives.length][];
		for(int i =0; i<logObjectives.length; i++)
			scoreObjectives[i] = new int[logObjectives[i].length];
		for(int i=0 ; i<logObjectives.length ; i++)
			for(int j = 0; j<logObjectives[i].length; j++)
			{	if(logObjectives[i][j] && mode==1)
					scoreObjectives[i][j] = Math.max(0, score - errorCount*2);
				else if(logObjectives[i][j] && tips) 
					scoreObjectives[i][j] = Math.max(0, score - ideasPuntenAftrek);
				else if(logObjectives[i][j]) 
					scoreObjectives[i][j] = score;
			}
		return scoreObjectives;
	}
	
	public int[][] getMeasuredMisconceptions()
	{	return measuredMisconceptions;
	}
	
	public int[][] getPossibleMisconceptions()
	{	return possibleMisconceptions;
	}
	
	public int getScoreMax()
	{	if(!teltMee) return 0;
	    return scoreMax;
	}

	public boolean isCorrect()
	{	if(!teltMee)return true;
	    return correct;
	}
	
	public boolean isCorrectStrikt()
	{	return correct;
	}
	
	public boolean isFout()
	{	if(!teltMee)return false;
		return fout;
	}
	
	public int getErrorCount()
	{	return errorCount;
	}
	
	public boolean hasCheck()
    {   return check;
    }
	
	private void maakStap()
	{	maakStap("");
	}
	
	private void maakStap(String s)
	{	if(!stappen)return;
		
		if(stapOk  || mode==2 || mode==3)
		{	nagekeken = false;
			stapOk = false;
			pijlVakken[stapNr] = new PijlVak(s);
			int y = formuleVakken[stapNr].getLocation().y + formuleVakken[stapNr].getSize().height/2;
			pijlVakken[stapNr].setLocation(getSize().width-pijlX,y);
			if(s.equals("sub"))
			{	add(pijlVakken[stapNr]);
				pijlVakken[stapNr].setSubVar("u");
				pijlVakken[stapNr].addActionListener(this);
				pijlVak = pijlVakken[stapNr];
				pijlVak.requestFocus();
			}
			else if(tips)
			{
				add(pijlVakken[stapNr]);
				pijlVak = pijlVakken[stapNr];
			}
			
			String string = formuleVakken[stapNr].toString();
			if(prefix==null && (string.charAt(string.length()-2))!='=')formuleVakken[stapNr].vulVak(string.substring(0,string.length()-1) + "=@");
			if(prefix==null && (string.charAt(string.length()-2))=='\u2248')formuleVakken[stapNr].vulVak(string.substring(0,string.length()-2) + "=@");
			if((mode!=2 && mode!=3) || (startString && stapNr==0))formuleVakken[stapNr].setEditable(false);
			
			formuleVakken[stapNr+1] = new FormuleVak();
			formuleVakken[stapNr+1].setFont(formuleVakFont);
			y = formuleVakken[stapNr].getLocation().y + formuleVakken[stapNr].getSize().height + stapH;
			if(hasPrefix) y = Math.min(formuleVakken[stapNr].getLocation().y,prefixVakken[stapNr].getLocation().y) + Math.max(formuleVakken[stapNr].getSize().height,prefixVakken[stapNr].getSize().height)+stapH ;
			
			int x = formuleVakX;
			if(prefix!=null) 
	    	{	prefixVakken[stapNr+1] = new FormuleVak();
	    		prefixVakken[stapNr+1].setFont(formuleVakFont);
				
				if(hidePrefix) prefixVakken[stapNr+1].vulVak("$f=@");
				else prefixVakken[stapNr+1].vulVak(prefix);
					
				prefixVakken[stapNr+1].setEditable(false);
				prefixVakken[stapNr+1].setSelectable(false);
				add(prefixVakken[stapNr+1]);
				x = formuleVakX + prefixVakken[0].getSize().width;
	    	}
			int dh = 0;
			if(prefix!=null)dh = -formuleVakken[stapNr+1].ashoogte+prefixVakken[stapNr+1].ashoogte;
			formuleVakken[stapNr+1].setLocation(x,Math.max(0,y+dh));
			if(prefix!=null) 
	    	{	if(hidePrefix)prefixVakken[stapNr+1].setLocation(formuleVakken[stapNr+1].getX() - prefixVakken[stapNr+1].getWidth(),y);
				else prefixVakken[stapNr+1].setLocation(formuleVakX,y);
	    	}
			formuleVakken[stapNr+1].addActionListener(this);
			add(formuleVakken[stapNr+1]);
			
			
			
			formuleVak = formuleVakken[stapNr+1];
			formuleVakSimpel = formuleVakken[stapNr+1];
				
			stapNr++;
			formuleVak.requestFocus();
			if("MW".equals(WiskOpdr.deployVariant))	mwFeedbackPanel.setLocation(getSize().width-310,formuleVakken[stapNr].getLocation().y + formuleVakken[stapNr].getSize().height + 5);
			else if("GR".equals(WiskOpdr.deployVariant))
			{	int fbx = Math.min(getSize().width-220, formuleVakken[stapNr].getLocation().x + formuleVakken[stapNr].getSize().width + 5);
				int fby = formuleVakken[stapNr].getLocation().y + formuleVakken[stapNr].getSize().height + 5;
				mwFeedbackPanel.setLocation(fbx, fby);
			}
			else feedbackTekst.setLocation(getSize().width-250,formuleVakken[stapNr].getLocation().y + formuleVakken[stapNr].getSize().height + 10);
			repaint();
		}
	}
	
			
	public void stapLeegTerug()
	{
		if(formuleVakken[stapNr]!=null && formuleVakken[stapNr].toString().equals("$f@"))
		{	if(stapNr>1)stapTerug();
			else if(!startString && stapNr>0)stapTerug();
		}
	}
	
	public void setState(Hashtable h)
	{	if(h==null)return;
	    for(int i=0 ; i<stapNr+1; i++)
	    {	remove(formuleVakken[i]);
	    }
	    for(int i=0 ; i<stapNr; i++)
	    {	remove(pijlVakken[i]);
	    }
	    
	    int stapNr = 0;
	    String[] formuleVakInhouden = null;
	    //int[] vvY = null;
	    //int[] pvY = null;
	    String[] pijlVakInhouden = null;
	    String[] pijlVakOperatoren = null;
	    boolean ingevuld = false;
	    boolean nagekeken = false;
	    String substitutieString = "";
		Vector attempts = new Vector();
		int attemptsCount = 0;
		int errorCount = 0;
		String[] gebruikersSubStrings = null;
		int ideasPuntenAftrek = 0;
	    
	    if(h.containsKey("stapNr")) stapNr = ((Number)h.get("stapNr")).intValue();
	    if(h.containsKey("formuleVakInhouden")) formuleVakInhouden = OpdrNavStruct.toStringArray(h.get("formuleVakInhouden"));
	    //if(h.containsKey("vvY")) vvY = (int[])h.get("vvY");
		//if(h.containsKey("pvY")) pvY = (int[])h.get("pvY");
	    if(h.containsKey("ingevuld")) ingevuld = ((Boolean)h.get("ingevuld")).booleanValue();
		if(h.containsKey("nagekeken")) nagekeken = ((Boolean)h.get("nagekeken")).booleanValue();
		//if(h.containsKey("gewensteAntwoordString")) gekozenAntwoordString = (String)h.get("gewensteAntwoordString");
		if(h.containsKey("antwoordString")) formuleVakString = (String)h.get("antwoordString");
		//if(h.containsKey("startString")) gekozenStartString = (String)h.get("startString");
		if(h.containsKey("substitutieString")) substitutieString = (String)h.get("substitutieString");
		if(h.containsKey("pijlVakInhouden"))pijlVakInhouden = OpdrNavStruct.toStringArray(h.get("pijlVakInhouden"));
	    if(h.containsKey("pijlVakOperatoren"))pijlVakOperatoren =  OpdrNavStruct.toStringArray(h.get("pijlVakOperatoren"));
	    if(h.containsKey("attempts"))attempts = OpdrNavStruct.toVector(h.get("attempts"));
	    if(h.containsKey("attemptsCount")) attemptsCount = ((Number)h.get("attemptsCount")).intValue();
	    if(h.containsKey("errorCount")) errorCount = ((Number)h.get("errorCount")).intValue();
	    if(h.containsKey("gebruikersSubStrings"))gebruikersSubStrings = OpdrNavStruct.toStringArray(h.get("gebruikersSubStrings"));
	    if(h.containsKey("ideasPuntenAftrek")) ideasPuntenAftrek = ((Number)h.get("ideasPuntenAftrek")).intValue();
	    
	    //patch voor reviewMode eindtoets html5
	  		if(mode==3 && uitw==true && stapNr>0 && formuleVakInhouden[stapNr].equals("$f@")) {
	  			stapNr--;
	  			//formuleVakInhouden[stapNr] = formuleVakString;
	  		}
	  		
		if(ingevuld && formuleVakString.length()>2 )vulVak(formuleVakString) ;
		//zetJuisteAntwoord(gekozenAntwoordString);
		//FormuleParser p = new FormuleParser();
		//if(!substitutieString.equals(""))substitutie = p.parse(p.schoon(p.formuleString(substitutieString)));
		if(!substitutieString.equals(""))substitutie = FormuleParser.geefExpressie(substitutieString,functieMVDefSet);
		
		
		
		this.stapNr = stapNr;
		
		formuleVakken = new FormuleVak[100];
		int y = formuleVakY;
		for(int i=0 ; i<stapNr+1; i++)
	    {	formuleVakken[i] = new FormuleVak();
	    	formuleVakken[i].setFont(formuleVakFont);
	    	if(i<stapNr && mode!=2) formuleVakken[i].setEditable(false);
	    	if(startString && i==0 && mode==2)formuleVakken[i].setEditable(false);
	    	int x = formuleVakX;
	    	
	    	if(prefix!=null) 
	    	{	int start = 0;
	    		if(startString) start = 1;
	    		if(i>start)	
	    		{	prefixVakken[i] = new FormuleVak();
	    			prefixVakken[i].setFont(formuleVakFont);
	    			prefixVakken[i].setEditable(false);
	    			prefixVakken[i].setSelectable(false);
	    			prefixVakken[i].setLocation(formuleVakX,y);
	    			if(hidePrefix)prefixVakken[i].vulVak("$f=@");
	    			else prefixVakken[i].vulVak(prefix);
	    			add(prefixVakken[i]);
	    		}
				x = formuleVakX + prefixVakken[0].getSize().width;
	    	}
	    	formuleVakken[i].setLocation(x,y);
	    	
	    	
			
	    	if(prefix!=null && hidePrefix) prefixVakken[i].setLocation(formuleVakken[i].getX() - prefixVakken[i].getWidth(),y);
	    	
	    	formuleVakken[i].addActionListener(this);
	    	add(formuleVakken[i]);
	    	formuleVakken[i].vulVak(formuleVakInhouden[i]);
	    	
	    	if(prefix!=null) 
	    	{	int dh = formuleVakken[i].ashoogte-prefixVakken[i].ashoogte;
		    	prefixVakken[i].setLocation(prefixVakken[i].getX(),y+dh);
	    	}
	    	y = formuleVakken[i].getLocation().y + formuleVakken[i].getSize().height + stapH;
			if(hasPrefix) y = Math.min(formuleVakken[i].getLocation().y,prefixVakken[i].getLocation().y) + Math.max(formuleVakken[i].getSize().height,prefixVakken[i].getSize().height)+stapH ;
			
	    }
		if(prefix!=null) 
		{	int h2 = formuleVakken[0].getLocation().y;		
			int dh = formuleVakken[0].ashoogte-prefixVakken[0].ashoogte;
			prefixVakken[0].setLocation(formuleVakX,h2+dh);
		}
		
		
		
		pijlVakken = new PijlVak[100];
		for (int i = 0; i < stapNr; i++)
		{
			y = formuleVakken[i].getLocation().y + formuleVakken[i].getSize().height / 2;
			if (pijlVakOperatoren != null && pijlVakOperatoren[i] != null)
				pijlVakken[i] = new PijlVak(pijlVakOperatoren[i]);
			else
				pijlVakken[i] = new PijlVak("gelijkwaardig");
			
			// het prefixvak heeft niet de goede variabele bij een substitutie; de constructor van PijlVak doet default p bij substitutie
			if (pijlVakOperatoren != null && pijlVakOperatoren[i] != null && pijlVakOperatoren[i].equals("sub"))
			{
				pijlVakken[i].setSubVar("u");
			}
			
			if (pijlVakInhouden != null && pijlVakInhouden[i] != null)
				pijlVakken[i].zetExpressie(pijlVakInhouden[i]);
			pijlVakken[i].setLocation(getSize().width - pijlX, y);
			if (pijlVakOperatoren != null && pijlVakOperatoren[i] != null && pijlVakOperatoren[i].equals("sub"))
			{
				pijlVakken[i].setLocation(getSize().width - pijlX - 30, y);
				add(pijlVakken[i]);
			}
		}
	    
	    this.ingevuld = ingevuld;
	    this.nagekeken = nagekeken;
	    this.attempts = attempts;
	    this.attemptsCount = attemptsCount;
	    this.errorCount = errorCount;
	    this.ideasPuntenAftrek = ideasPuntenAftrek;
	    
	    
	    if(hasSubKnopExtra)
	    {   gebruikersSubstitutiesVak.zetRegels(gebruikersSubStrings);
		    updateGebruikersSubstituties();
		    if(gebruikersSubstituties!=null)
		    {	gebruikersSubstitutiesVak.setLocation(getWidth()-221,27);
				zetOpRoot(gebruikersSubstitutiesVak);
		    }
	    }
	    
	    if (eigenOpdr && !startString) {
			String eString = formuleVakken[0].toString();
			//Expressie exp = FormuleParser.geefExpressie(eString);
			Expressie exp = FormuleParser.geefExpressie(eString,functieMVDefSet);
			if(exp==null) 
			{	if(eString.charAt(eString.length()-2)=='=' || eString.charAt(eString.length()-2)=='\u2248')
				{	int isIndex = eString.length()-2;
					eString = eString.substring(0,isIndex)+"@";
					//exp = FormuleParser.geefExpressie(eString);
					exp = FormuleParser.geefExpressie(eString,functieMVDefSet);
				}
			}
        	
        	Expressie expAntw = null;
        	if(exp!=null)
        	{	
        		expAntw = Expressie.evalWithCAS(exp);
        		String def = "";
        		if(expAntw!=null) def = expAntw.toString();
        		zetJuisteAntwoord("$f"+def+"@");
        		
        		startString = true;
			}
        	
		}
	    
	    formuleVak = formuleVakken[stapNr];
	    formuleVakSimpel = formuleVakken[stapNr];
	   	if(mode==0 || nagekeken)	kijkNa();
	}

	private void stapTerug()
	{	nagekeken = false;
		if(stapNr>0)
		{	remove(formuleVakken[stapNr]);
			remove(pijlVakken[stapNr-1]);
			scoreContainer[stapNr] = -1;
			if("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant))removeSoft(mwFeedbackPanel);
			else removeSoft(feedbackTekst);
			
			if(prefixVakken[stapNr]!=null)remove(prefixVakken[stapNr]);
			if(stapNr!=1 || !startString) 
			{	String string = formuleVakken[stapNr-1].toString();
				if(prefix==null && string.charAt(string.length()-2)=='=') formuleVakken[stapNr-1].vulVak(string.substring(0,string.length()-2) + "@");
				if(prefix==null && string.charAt(string.length()-2)=='\u2248') formuleVakken[stapNr-1].vulVak(string.substring(0,string.length()-2) + "@");
				if(mode!=2 && mode!=3)formuleVakken[stapNr-1].setEditable(false);
				if(!check)formuleVakken[stapNr-1].setEditable(true);
			}
			formuleVak = formuleVakken[stapNr-1];
			formuleVakSimpel = formuleVakken[stapNr-1];
			stapNr--;
			stapOk = false;
						
			if((mode==0 || mode==1) )
			{	if(stapNr>0 || !startString)kijkNa();
				else
				{	zetGoedFout(GEEN,-1);
					stapOk = true;
				}
				
			}
			else 
			{	stapOk = true;
				zetGoedFout(GEEN,stapNr+1);
				if(imageComponentenStap[stapNr]!=null) remove(imageComponentenStap[stapNr]);
			}
			produceAction("changed");
			
			
		}
		else if(stapNr==0 && !startString)
		{
			formuleVakken[stapNr].vulVak("$f@");
			formuleVakken[stapNr].setEditable(true);
			if("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant))remove(mwFeedbackPanel);
			else remove(feedbackTekst);
			score = 0;
			correct = false;
			stapOk = false;
			zetGoedFout(GEEN,-1);
			produceAction("changed");
		}
	}
	
	public void bereken()
	{
		maakStap();
		
		if (stapNr==0)
			return;
		
		Expressie exp = formuleVakken[stapNr-1].geefExpressie();
		
		if (exp==null) 
		{
			String expString = formuleVakken[stapNr-1].toString();
			if (expString.charAt(expString.length()-2)=='=' || expString.charAt(expString.length()-2)=='\u2248')
			{
				int isIndex = expString.length()-2;
				expString = expString.substring(0,isIndex)+"@";
				//exp = FormuleParser.geefExpressie(expString);
				exp = FormuleParser.geefExpressie(expString,functieMVDefSet);
			}
		}
		if (exp == null 
			|| (Double.isNaN(exp.geefWaarde()) && !(Algebra.isVector(exp) || Algebra.isMatrix(exp)))
			|| exp instanceof BasisExpressie)
		{
			stapTerug();
			zetGoedFout(GEEN,-1);
		}

		if (exp != null && !(exp instanceof BasisExpressie))
		{
			String formule;
			
			if (Algebra.isVector(exp))
			{
                BerekendeVectorExpr berekendeVector = exp.geefVector().berekenVector(aantalDecRm);
                formule = berekendeVector.toString();
                
                if (berekendeVector.isAfgerond())
                {
                    if (!hasPrefix)
                    {
                        String vorige = formuleVakken[stapNr - 1].toString();
                        if (vorige.charAt(vorige.length() - 2) == '=')
                            formuleVakken[stapNr - 1].vulVak(vorige.substring(0, vorige.length() - 2) + "\u2248@");
                  }
                }

				formuleVakken[stapNr].vulVak("$f" + formule + "@");
			}
			else if (Algebra.isMatrix(exp))
			{
			    BerekendeMatrix berekendeMatrix = exp.geefMatrix().berekenMatrix(aantalDecRm);
                formule = berekendeMatrix.toString();

                if (berekendeMatrix.isAfgerond())
                {
                    if (!hasPrefix)
                    {
                        String vorige = formuleVakken[stapNr - 1].toString();
                        if (vorige.charAt(vorige.length() - 2) == '=')
                            formuleVakken[stapNr - 1].vulVak(vorige.substring(0, vorige.length() - 2) + "\u2248@");
                    }
                }
                
				formuleVakken[stapNr].vulVak("$f" + formule + "@");
			}
			else if (!Double.isNaN(exp.geefWaarde()))
			{
				double d = exp.geefWaarde();
				Expressie expAfgerond = new DecRound(exp, new BasisExpressie(aantalDecRm));
				double dAfgerond = expAfgerond.geefWaarde();
				boolean isAfronding = !Algebra.isGelijkDouble(d, dAfgerond, 0.00000000000000001);
//				s1 = formuleVakken[stapNr - 1].toString(); // met s1 gebeurt niks...?
//				s1 = s1.substring(2, s1.length() - 1);
				formule = Expressie.df3.format(dAfgerond);

				String s = Double.toString(dAfgerond);
				String[] delen = StringUtils.split(s, "E");
				if (delen.length > 1)
					formule = delen[0] + "*10$m" + delen[1] + "@";
				else
					formule = delen[0];

				formuleVakken[stapNr].vulVak("$f" + formule + "@");
				String string = formuleVakken[stapNr - 1].toString();
				if (isAfronding && prefix == null && (string.charAt(string.length() - 2)) == '=')
					formuleVakken[stapNr - 1].vulVak(string.substring(0, string.length() - 2) + "\u2248@");
			}
		}
	}
	
	public void actionPerformed(ActionEvent e)
	{	super.actionPerformed(e);
		if(e.getSource()==formuleVak && e.getActionCommand().equals("ingevuld"))
		{	
			
			if (stapNr==0 && eigenOpdr && !startString) {
				String eString = formuleVak.toString();
				//Expressie exp = FormuleParser.geefExpressie(eString);
				Expressie exp = FormuleParser.geefExpressie(eString,functieMVDefSet);
	        	//String eStringCas = "$f@";
	        	
	        	Expressie expAntw = null;
	        	if(exp!=null)
	        	{	
	        		//eStringCas = exp.toStringCAS();
	        		expAntw = Expressie.evalWithCAS(exp);
	        		String def = "";
	        		if(expAntw!=null) def = expAntw.toString();
	        		zetJuisteAntwoord("$f"+def+"@");
	        		
	        		startString = true;
				    stapOk = true;
				    setAttempt();
	                maakStap();
	                return;
	        	}
	        	
			}
			
			if(mode==0 || mode==1) 
			{
				kijkNa();
				if (!formuleVak.toString().equals("$f@"))
					zetNagekeken(true);
				if (fout)
					errorCount++;
				attemptsCount++;
				setAttempt();
			}
			else try
			{	Expressie antwoord = formuleVak.geefExpressie();
				if(antwoord!=null)
				{	//formuleVak.vulVak("$f" + antwoord.toString() + "@"); // of is deze herschrijving echt nodig?
					requestFocus();
				}
				setAttempt();
			}
			
			catch(Exception exc){}
			if(!check && !rmKnop)
			{	stapOk = true;
				maakStap();
				return;
			}
			if(!check && rmKnop)
			{	stapOk = true;
				bereken();
				return;
			}
			if((mode==0 || mode==1)&& ingevuld)produceAction("checked");
			if((mode==0 || mode==1) && !hasFeedback && herleiding && !isHerleid) maakStap();
			else if((mode==0 || mode==1) && !hasFeedback  && exact && !isExact) maakStap();
			else if((mode==0 || mode==1) && hasFeedback  && goedHalfFout==1) maakStap();
			
			if((mode==2 || mode==3) && !formuleVak.toString().equals("$f@")) {
				Expressie antwoordIngevuld = formuleVak.geefExpressie();
				if (antwoordIngevuld == null) { 
					setFeedback(WiskOpdr.rb.getString("feedbackTekst14"), true);
				}
				else {
					maakStap();
					remove(feedbackTekst);
				}
			}
			
			
		}
		else if(e.getSource()==formuleVak && e.getActionCommand().equals("formChanged"))
		{	if(feedbackTekst!=null && getParent()==null && feedbackTekst.getParent()!=null)
			{	remove(feedbackTekst);
				produceAction("feedbackWeg");
			}
			zetGoedFout(GEEN,-1);
			if(mode==2 || mode==3)
			{	for(int i=0 ; i<stapNr+1 ; i++)
				{	zetGoedFout(GEEN,i);
				}
				produceAction("feedbackWeg");
			}
		}
		else if(e.getSource() instanceof FormuleVak && e.getActionCommand().equals("focus"))
		{	FormuleVak fv = (FormuleVak)e.getSource();
			if(fv != formuleVak) formuleVak = fv;
		}
		else if(e.getSource()==gelijkwaardigKnop)
		{	maakStap();
			if(stapOk )
			{	zetGoedFout(GEEN,-1);
			}
			else formuleVak.requestFocus();
		}
		else if(e.getSource()==terugKnop)
		{	stapTerug();
			setAttempt();
			formuleVak.requestFocus();
		}
		
		else if(e.getSource() == gebruikersSubstitutiesVak)
        {  	removeFromRoot(gebruikersSubstitutiesVak);
        	repaint();
        }
		else if(e.getSource()==subKnop)
		{	
			if(hasSubKnopExtra)
			{
				gebruikersSubstitutiesVak.setLocation(getWidth()-221,27);
				zetOpRoot(gebruikersSubstitutiesVak);
			}
			else
			{	if(!stapOk && stapNr>0) stapTerug();
				maakStap("sub");
				if(pijlVak!=null) pijlVak.requestFocus();
			}
		}
		else if(e.getSource()==pijlVak)
		{	maakStap();
			if(e.getActionCommand().equals("substitutie"))
			{	substitutie = ((PijlVak)e.getSource()).geefSubstitutie();
				formuleVak.requestFocus();
			}
			
		}
		else if(e.getSource()==formuleVak && e.getActionCommand().equals("zetMaat") && prefix!=null)
		{	int hf = formuleVakken[stapNr].getLocation().y;
			int dh = formuleVakken[stapNr].ashoogte-prefixVakken[stapNr].ashoogte;
			prefixVakken[stapNr].setLocation(prefixVakken[stapNr].getX(),hf+dh);
			
			
		}
		else if(e.getSource()==feedbackTekst)
		{	if(getParent()!=null)removeSoft(feedbackTekst);
			if((uitw || getParent()==null) && feedbackTekst.getParent()!=null)
			{	produceAction("closeFeedback");
			}		
			formuleVak.requestFocus();
		}
		else if(e.getSource()==feedbackCloseButton)
		{	removeSoft(mwFeedbackPanel);
			if((uitw || getParent()==null) && feedbackTekst.getParent()!=null)
			{	produceAction("closeFeedback");
			}
			formuleVak.requestFocus();
		}
		else if(e.getSource()==rekenKnop)
		{	formuleVak.finish();
			if(!fout)zetGoedFout(GEEN, -1);
			bereken();
		}
		else if (e.getSource()==tip1Knop)
		{	ideasPuntenAftrek += aftrekTip;
			FormuleVak fv = formuleVakken[this.stapNr<1?0:this.stapNr-1];
			if(formuleVak.geefExpressie()!=null && isGelijkwaardig) fv = formuleVak;
			
			String eString = fv.toString();
			eString = verwijderIsTeken(eString);
			//String vglStrikt = FormuleParser.geefExpressie(eString).toStringStrikt();
			String vglStrikt = FormuleParser.geefExpressie(eString,functieMVDefSet).toStringStrikt();
			vglStrikt = vertaalNaarIdeasExpressie(vglStrikt);
			//String vgl = fv.geefExpressie().toString();
			if(meerTips)
			{	RuleIF[] rules = WiskOpdr.ideas.getAllFirsts(vglStrikt,strategieDomein);
				if(rules==null)
				{	JOptionPane.showMessageDialog(this,"Feedbackservice not available");
					return;
				}
				String feedback = "";
				for(int i=0 ; i<rules.length ; i++)
				{	if(i>0) feedback = feedback + "\n";
					if(rules.length>1)feedback = feedback + "Tip "+(i+1)+": \n";
					else feedback = feedback + "Tip: \n";
					feedback = feedback + translateRule(rules[i].getId()) + "\n";
				}
				if(rules.length>1)setFeedback(feedback,true);
				//else if(hulpBijTip)setFeedback(feedback,true,3);
			}
			else 
			{	RuleIF rule = WiskOpdr.ideas.getOneFirst(vglStrikt,strategieDomein);
				if(rule==null)
				{	JOptionPane.showMessageDialog(this,"Feedbackservice not available");
					return;
				}
				String feedback = "";
				feedback = feedback + "Tip: \n" +  translateRule(rule.getId()) + "\n";
				if(feedbackModus==1 || hulpBijTip);//setFeedback(feedback,true,3);
				else setFeedback(feedback,true);
			}
			if(feedbackModus==1 && !tipGebruikt)aftrekTipHulp = aftrekTipHulp + 1;
			tipGebruikt = true;
			repaint();
				
		}
		else if (e.getSource()==tip2Knop)
		{	ideasPuntenAftrek += aftrekHulp;
			FormuleVak fv = formuleVakken[this.stapNr<1?0:this.stapNr-1];
			if(formuleVak.geefExpressie()!=null && isGelijkwaardig) fv = formuleVak;
			String eString = fv.toString();
			eString = verwijderIsTeken(eString);
			
			//String vglStrikt = FormuleParser.geefExpressie(eString).toStringStrikt();
			String vglStrikt = FormuleParser.geefExpressie(eString, functieMVDefSet).toStringStrikt();
			vglStrikt = vertaalNaarIdeasExpressie(vglStrikt);

			RuleIF rule = WiskOpdr.ideas.getOneFirst(vglStrikt,strategieDomein);
			if(rule==null)
			{	JOptionPane.showMessageDialog(this,"Feedbackservice not available");
				return;
			}
			String exprString = vertaalIdeasExpressie(rule.getExpr());
			//Expressie v = FormuleParser.geefExpressie("$f" + exprString + "@");
			Expressie v = FormuleParser.geefExpressie("$f" + exprString + "@", functieMVDefSet);
			setFeedback("Tip: \n" 
					+ translateRule(rule.getId()) + "\n"
					+ "\n"
					+ eString + "\n"
					+ "      " + WiskOpdr.rb.getString("ideasWordtDan") + ":\n"
					+ "$f" + v.toString() + "@\n"
					
					,true);
			if(feedbackModus==1 && !hulpGebruikt)aftrekTipHulp = aftrekTipHulp + 2;
			hulpGebruikt = true;
			repaint();
			
			
		}
		else if (e.getSource()==hulpKnop)
		{	ideasPuntenAftrek += aftrekStap;
			FormuleVak fv = formuleVakken[this.stapNr<1?0:this.stapNr-1];
			if(formuleVak.geefExpressie()!=null && isGelijkwaardig) fv = formuleVak;
			String eString = fv.toString();
			eString = verwijderIsTeken(eString);
			//String vglStrikt = FormuleParser.geefExpressie(eString).toStringStrikt();
			String vglStrikt = FormuleParser.geefExpressie(eString, functieMVDefSet).toStringStrikt();
			vglStrikt = vertaalNaarIdeasExpressie(vglStrikt);
			//System.out.println(vglStrikt);
			RuleIF rule = WiskOpdr.ideas.getOneFirst(vglStrikt,strategieDomein);
			if(rule==null)
			{	JOptionPane.showMessageDialog(this,"Feedbackservice not available");
				return;
			}
			//RuleIF[] rules = WiskOpdr.ideas.getDerivation(vglStrikt,strategieDomein);
			String exprString = vertaalIdeasExpressie(rule.getExpr());
			
			//System.out.println(exprString);
		    
			
			//Expressie v = FormuleParser.geefExpressie("$f" + exprString + "@");
			Expressie v = FormuleParser.geefExpressie("$f" + exprString + "@", functieMVDefSet);
			if(stapNr>0)pijlVakken[stapNr-1].zetPijlTekst("", false);
			//pijlVakken[stapNr-1].setLocation(getSize().width-pijlX,pijlVakken[stapNr-1].getLocation().y);
			maakStap();
			pijlVakken[stapNr-1].setLocation(getSize().width-pijlX-150,pijlVakken[stapNr-1].getLocation().y);
			pijlVakken[stapNr-1].setSize(250,pijlVakken[stapNr-1].getSize().height);
			pijlVakken[stapNr-1].zetPijlTekst(translateRule(rule.getId()), false);
			
			formuleVakken[stapNr].vulVak("$f" + v.toString() + "@");
			int y = formuleVakken[stapNr-1].getLocation().y + formuleVakken[stapNr-1].getSize().height + pijlVakken[stapNr-1].getHeight()-10;
			int x = formuleVakX;
			formuleVakken[stapNr].setLocation(x,y);
			kijkNaIdeas();
			maakStap();
			//		stapTerug();
			
			//System.out.println("$f" + rules[0].getExpr() + "@");
			repaint();
		}
		else if (e.getSource()==solveKnop)
		{	ideasPuntenAftrek += aftrekSolve;
			String eString = formuleVakken[0].toString();
			eString = verwijderIsTeken(eString);
			//String vglStrikt = FormuleParser.geefExpressie(eString).toStringStrikt();
			String vglStrikt = FormuleParser.geefExpressie(eString, functieMVDefSet).toStringStrikt();
			System.out.println(vglStrikt.toString());
		    RuleIF[] rules = WiskOpdr.ideas.getDerivation(vglStrikt,strategieDomein);
		    if(rules==null)
			{	JOptionPane.showMessageDialog(this,"Feedbackservice not available");
				return;
			}
		    
			while(stapNr>0)stapTerug();
			for(int i=0 ; i<rules.length ; i++)
			{	stapOk=true;
				maakStap();
				//System.out.println(rules[i].getExpr());
				String exprString = vertaalIdeasExpressie(rules[i].getExpr());
				
                
				//Expressie v = FormuleParser.geefExpressie("$f" + exprString + "@");
				Expressie v = FormuleParser.geefExpressie("$f" + exprString + "@", functieMVDefSet);
				pijlVakken[stapNr-1].zetPijlTekst(translateRule(rules[i].getId()), false);
				//pijlVakken[stapNr-1].zetPijlTekst(rules[i].getId(), false);
                pijlVakken[stapNr-1].setLocation(getSize().width-pijlX-150,pijlVakken[stapNr-1].getLocation().y);
				pijlVakken[stapNr-1].setSize(250,pijlVakken[stapNr-1].getSize().height);
				formuleVakken[stapNr].vulVak("$f" + v.toString() + "@");
				int y = formuleVakken[stapNr-1].getLocation().y + formuleVakken[stapNr-1].getSize().height + pijlVakken[stapNr-1].getHeight()-10;
				int x = formuleVakX;
				formuleVakken[stapNr].setLocation(x,y);
			}
			kijkNaIdeas();
			
			repaint();
		}
		else if(e.getSource()==wisKnop) {
			startString = false;
			wis();
			formuleVak.vulVak("$f@");
			stapOk=false;
		}
	
	}
	
	public String vertaalIdeasExpressie(String s)
	{
		//System.out.println(s);
		//s = StringUtils.replaceStr(s,"?",WiskOpdr.rb.getString("ofLabel"));
		s = StringUtils.replaceStr(s,"\u2228",WiskOpdr.rb.getString("ofLabel"));
        if(s.equals("false"))s = "x=geen";
        return s;
	}
	
	public String vertaalNaarIdeasExpressie(String s)
	{
		s = StringUtils.replaceStr(s,WiskOpdr.rb.getString("ofLabel"),"\u2228");
		s = StringUtils.replaceStr(s, " ", "");
		if(s.equals("geenoplossingen"))s = "false";
        return s;
	}
	
	public String translateRule(String s)
	{
		if(s != null && changedTexts.containsKey(s))return((String)changedTexts.get(s));
		else if(s != null) return AntwoordVergelijkingVak.translateRuleToStandard(s);
		else return("null");
	}
	
	
	
	public void verplaatsFocus()
	{	Container parent = getParent();
		for(int i=0 ; parent!=null && i<40 ; i++)
		{	if(parent instanceof TekstVakPanel) 
			{	((TekstVakPanel)parent).verplaatsFocus();
				break;
			}
			else if(parent instanceof MyOpdrContainer) 
			{	((MyOpdrContainer)parent).verplaatsFocus();
				break;
			}
			else 
			{	parent = parent.getParent();
			}
		}
	}
	
	//ActionProducer
	private ActionListener actionListener = null;
	
	public void addActionListener(ActionListener l) 
 	{	actionListener = AWTEventMulticaster.add(actionListener,l);
 	}
 	
 	public void removeActionListener(ActionListener l)
 	{	actionListener = AWTEventMulticaster.remove(actionListener, l);
 	}	
 	
 	public void produceAction(String command)
 	{	if (actionListener != null)
 		{	actionListener.actionPerformed( new ActionEvent(this, 0, command) );
 		}
 	}
 	//

	@Override
	public void acceptCBookEvent(CBookEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addCBookEventListener(CBookEventListener listener,
			String command) {
		cbookEventHandler.addCBookEventListener(listener, command);
		
	}

	@Override
	public void removeCBookEventListener(CBookEventListener listener,
			String command) {
		cbookEventHandler.removeCBookEventListener(listener, command);
		
	}

	@Override
	public String[] getSendCmds() {
		String[] commands = {"action.correct",
				"action.false",
				"action.false_2"};
		return commands;
	}

	@Override
	public String[] getAcceptedCmds() {
		return new String[] { "action.setNotEditable" };
	}

	@Override
	public String getLocalizedCmd(String cmd) {
		String localizedCmd = WiskOpdr.rb.getString(CBA_PREFIX + cmd);
		if(localizedCmd==null)
			return cmd;
		return localizedCmd;
	}
}
