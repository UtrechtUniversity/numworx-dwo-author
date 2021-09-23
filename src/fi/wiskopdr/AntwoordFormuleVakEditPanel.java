package fi.wiskopdr;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import org.cbook.cbookif.CBookWidgetEditIF;

import fi.beans.base64code.*;
import fi.beans.ideas.Exercise;
import fi.beans.ideas.IdeasIF;
import fi.beans.ideas.RuleIF;
import fi.wiskopdr.tekstobjects.*;
import fi.wiskopdr.formuleobjects.*;
import fi.wiskopdr.AntwoordVergelijkingVakEditPanel.EditorComponentListener;
import fi.wiskopdr.domainmodel.Constants;
import fi.wiskopdr.expressies.*;
import fi.wiskopdr.opdrnav.*;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;
import fi.beans.stringutils.StringUtils;


public class AntwoordFormuleVakEditPanel extends JLayeredPane implements InteractieEditPanel, ActionListener,  MouseListener, MouseMotionListener, HelpButtonPanelIF
{
	// Algemene attributen 
    private Font font = new Font("SansSerif",Font.PLAIN,12);//WiskOpdr.tekstFont;
	
    private int soort;
	private boolean stappen = true;
    private boolean stappenDefault = true;
     
    private Tablet tablet;
    private boolean tabletAdded;
    private FormuleVakHouder tabletUser;
   
    // Basis GUI
    private JPanel mainPanel;
    
    //Start editor
  	private FormuleEditor startEditor;
  	private JPanel startEditorPanel;
  	private JLabel titleStartLabel;
  	private Box startBox;
  	private DialogFacade startEditorPopupFrame;
  	
  	// Antwoord editor
 	private FormuleEditor antwoordvak;
 	private JPanel antwoordEditorPanel;
 	private JLabel titleAntwoordLabel;
 	private Box antwoordBox;
 	
 	private OpdrachtNrRij tabbladTab;
 	private PlusMinKnop aantalTabsKnop;
    private PlusMinKnop tabPositieKnop;
     
    private int aantalAnswerModels = 1;
    private Hashtable[] answerModels;
    private Hashtable resAnswerModel = new Hashtable();
    private int answerModelNr = 0;
    
    // VormEditor
 	private FormuleEditor vormEditor;
 	private JPanel vormEditorPanel;
 	private JLabel titleVormLabel;
 	private Box vormBox;
 	private DialogFacade vormEditorPopupFrame;
 	
 	//Feedback editor
 	private TekstEditor feedbackEditor;
 	private JCheckBox feedbackSizeCB;
 	private JLabel titleFeedbackLabel;
 	private JLabel titleFeedbackTekstLabel;
 	private ActKeuzePanel goedFoutIP;
 	private Box feedbackBox;
 	private DialogFacade feedbackEditorPopupFrame;
 	
 	// Verificatie
 	private JLabel titleVerificatieLabel;
 	private JLabel titleVerificatieScoreLabel;
 	private Box verificatieBox;
 	private JCheckBox  gelijkwaardigCB, herleidingCB, exactCB, significantCB;
 	private JTextField gelijkwaardigPV, herleidingPV, exactPV, significantPV;
 	
 	private boolean gelijkwaardig = true;
    private boolean herleiding;
    private boolean exact;
    private boolean	significant;
    private double eqTestValueMin = 0;
    private double eqTestValueMax = 5;
     
    private int puntenGelijkwaardig = 0;
    private int puntenHerleiding = 0;
    private int puntenExact = 0;
    private int puntenSignificant = 0;
    private int puntenFeedback = 0;
     
    static boolean	significantieAan=false;
     
    // Score
    private JLabel titleScoreLabel;
    private JLabel ScoringLabel; // overbodig?
    private Box scoringBox;
    private JTextField  feedbackPV;
    private JCheckBox scoreCumulatiefCB;
    
    // Logging/Nakijken
 	private JLabel titleLoggingLabel;
    private JCheckBox checkCB;
    private JCheckBox teltMeeCB;
     
    private JCheckBox logCB;
 	private JTextField logIDField;
 	private JTextField logIDLabelField;
 	private JLabel logIDLabelLabel;
 	private ObjectiveChoiceButton logObjectivesButton;
 	private ObjectiveChoiceButton logMisconceptionsButton;
	 
 	// Hulp setting
 	private JLabel titleHulpLabel;
 	private Box settingsBox;
 	private JCheckBox  subKnopCB, stappenCB, subKnopExtraCB;
    private JCheckBox tipsCB;
    private JCheckBox formuleToolBijFocusCB;
    private JCheckBox feedbackCB;
    private IdeasInstellingenButton ideasButton;
    private JCheckBox uitwCB;
    private JCheckBox  rmKnopCB;
    private JTextField aantalDecRmField;
	private JLabel aantalDecRmLabel;
	private JCheckBox eigenOpdrCB;
    
    private boolean subKnop;
    private boolean subKnopExtra;
    private boolean formuleToolBijFocus;
    private boolean hasFeedback;
    private boolean tips; // ideas
    
    // Opmaak
 	private JLabel titleOpmaakLabel;
 	private JCheckBox boxMetRandCB;
 	
    // contextVars
 	private JCheckBox contextVarCB;
    private JLabel titleContextLabel;
 	private Box contextBox;
 	private JButton substitutiesButton;
    private FormuleEditor antwoordSubstitutiesVak;
    private JButton functiesButton;
    private FormuleEditor antwoordFunctiesVak;
     
 	// Helpbuttons
    private static String HELP_0_URL_2 = WiskOpdr.rb.getString("HELP_0_URL_2");
    private static String HELP_0_URL_0 = WiskOpdr.rb.getString("HELP_0_URL_0");
    private static String HELP_0_URL_CHECK = WiskOpdr.rb.getString("HELP_0_URL_CHECK");
    private static String HELP_0_URL_TELTMEE = WiskOpdr.rb.getString("HELP_0_URL_TELTMEE");
    private static String HELP_0_URL_LOGID = WiskOpdr.rb.getString("HELP_0_URL_LOGID");
    
    private static String HELP_0_URL_FEEDBACK = WiskOpdr.rb.getString("HELP_0_URL_FEEDBACK");
    private static String HELP_0_URL_REKENMACHINE = WiskOpdr.rb.getString("HELP_0_URL_REKENMACHINE");
    private static String HELP_0_URL_SUBSTITUTIES = WiskOpdr.rb.getString("HELP_0_URL_SUBSTITUTIES");
    private static String HELP_0_URL_CONTEXTVAR = WiskOpdr.rb.getString("HELP_0_URL_CONTEXTVAR");
    private static String HELP_0_URL_EIGENOPDR = WiskOpdr.rb.getString("HELP_0_URL_EIGENOPDR");
    private static String HELP_0_URL_FORMINVOER = WiskOpdr.rb.getString("HELP_0_URL_FORMINVOER");
    private static String HELP_0_URL_UITWERKING = WiskOpdr.rb.getString("HELP_0_URL_UITWERKING");
    private static String HELP_0_URL_RAND_0 = WiskOpdr.rb.getString("HELP_0_URL_RAND_0");
    private static String HELP_0_URL_RAND_2 = WiskOpdr.rb.getString("HELP_0_URL_RAND_2");
    private static String HELP_0_URL_VERIFICATIE = WiskOpdr.rb.getString("HELP_0_URL_VERIFICATIE");
    private static String HELP_0_URL_ANTWOORD_0 = WiskOpdr.rb.getString("HELP_0_URL_ANTWOORD_0");
    private static String HELP_0_URL_ANTWOORD_2 = WiskOpdr.rb.getString("HELP_0_URL_ANTWOORD_2");
    private static String HELP_0_URL_STARTEXPRESSIE = WiskOpdr.rb.getString("HELP_0_URL_STARTEXPRESSIE");
    private static String HELP_0_URL_SCORE = WiskOpdr.rb.getString("HELP_0_URL_SCORE");
    private static String HELP_0_URL_FEEDBACKTITLE = WiskOpdr.rb.getString("HELP_0_URL_FEEDBACKTITLE");
    
   
    
    private HelpButton hbCheck;
    private HelpButton hbTeltMee;
    private HelpButton hbLogID;
    private HelpButton hbFeedback;
    private HelpButton hbRekenmach;
    private HelpButton hbSubstituties;
    private HelpButton hbContextvar;
    private HelpButton hbEigenOpdr;
    private HelpButton hbFormInvoer;
    private HelpButton hbUitwerking;
    private HelpButton hbRand_0;
    private HelpButton hbRand_2;
    private HelpButton hbVerificatie;
    private HelpButton hbAntwoord_0;
    private HelpButton hbAntwoord_2;
    private HelpButton hbStartExpressie;
    private HelpButton hbScore;
    private HelpButton hbFeedbackTitel;
    
    
    
	
	
    
    // Overige attributen (wellicht overbodig geworden
	//private JLabel feedbackLabel;
	//private JLabel antwoordLabel, startLabel;
	private JCheckBox     antwoordCheckCB;
	private JLabel  puntenLabel, checkTotaalLabel;
	private JComboBox herleidingsKeuze;
	private JCheckBox rekenVakCB;
    
	private boolean	antwoordCheck = true;
	private int soortHerleiding = 0;
	private String[] herleidingItems;
	
	public static void zetSignificantieAan(boolean b)
    {	significantieAan = b;
    }
	
	public String geefHelpURL() {
		if(soort==2)
			return HELP_0_URL_2;
		else
			return HELP_0_URL_0;
	}
    
	public AntwoordFormuleVakEditPanel(int soort)
	{	this.soort = soort;
		setLayout(new BorderLayout());
		super.setSize(770,520); //voor dwo
		setBackground(Color.white);	
		setOpaque(true);
		addMouseListener(this);
		addMouseMotionListener(this);
		makeGUI();
		
		answerModels = new Hashtable[aantalAnswerModels];
		setFeedbackOption(false);
		
		if(soort==0)
		{	stappen = true;	
			stappenDefault = true;
			tipsCB.setVisible(WiskOpdr.isExperimental());
			
			
		}
		
		else if(soort==2)
		{	stappen = false;
			stappenDefault = false;
			//startLabel.setVisible(false);
			startEditor.setVisible(false);
			rmKnopCB.setVisible(false);
			subKnopCB.setVisible(false);
			tipsCB.setVisible(false);
			formuleToolBijFocusCB.setVisible(true);
			uitwCB.setVisible(true);
			boxMetRandCB.setVisible(true);
			//feedbackCB.setVisible(false);
			
			hbRekenmach.setVisible(false);
	    	hbSubstituties.setVisible(false);
	    	hbEigenOpdr.setVisible(false);
		}
	}
	
	private void makeGUI() {  
    	// Main
    	mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBackground(WiskOpdr.colorGray3);
		//mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
	
		// GUI startBox  
    	titleStartLabel = new JLabel(WiskOpdr.rb.getString("FEF_titleStartLabel"));
    	titleStartLabel.setForeground(WiskOpdr.colorBlue1);
    	titleStartLabel.setFont(font.deriveFont(Font.BOLD, 16));
    	
    	
        startEditor = new FormuleEditor(false);
        startEditor.setHeader(true);
		startEditor.setBounds(0,0,470,110);
		startEditor.setFont(font);
        startEditor.addActionListener(this);
        
        startEditorPanel = new JPanel();
        startEditorPanel.setLayout(null);
        startEditorPanel.add(startEditor);
        startEditorPanel.addComponentListener(new EditorComponentListener());
        startEditorPanel.setPreferredSize(new Dimension(450,110));
        startEditorPanel.setMaximumSize(new Dimension(2870,150));
        
        // GUI antwoordBox
        titleAntwoordLabel = new JLabel(WiskOpdr.rb.getString("FEV_titleAntwoordLabel"));
    	titleAntwoordLabel.setForeground(WiskOpdr.colorBlue1);
    	titleAntwoordLabel.setFont(font.deriveFont(Font.BOLD, 16));
    	titleAntwoordLabel.setBounds(0,-3,140,20);
    	
        antwoordvak = new FormuleEditor(true);
        antwoordvak.setBounds(0,20,435,150);
        antwoordvak.setFont(font);
        antwoordvak.addActionListener(this);
        
        antwoordEditorPanel = new JPanel();
        antwoordEditorPanel.setLayout(null);
        antwoordEditorPanel.add(titleAntwoordLabel);
        antwoordEditorPanel.add(antwoordvak);
        antwoordEditorPanel.addComponentListener(new EditorComponentListener());
        antwoordEditorPanel.setPreferredSize(new Dimension(450,140));
        antwoordEditorPanel.setMaximumSize(new Dimension(2835,170));
        
        tabbladTab = new OpdrachtNrRij(aantalAnswerModels, 250,20);
        tabbladTab.setSize(tabbladTab.getSize().width, 23);
        tabbladTab.setTab(true);
        tabbladTab.setScoresVisible(false);
        tabbladTab.addActionListener(this);
        tabbladTab.setBackground(new Color(210,210,210));
        tabbladTab.setSelected(1);
        antwoordEditorPanel.add(tabbladTab,0);
        
        aantalTabsKnop = new PlusMinKnop(250+25*aantalAnswerModels+5 ,24,20,16,PlusMinKnop.HORIZONTAAL);
        aantalTabsKnop.setBackground(new Color(210,210,210));
        aantalTabsKnop.addActionListener(this);
        antwoordEditorPanel.add(aantalTabsKnop,0);
        
        tabPositieKnop = new PlusMinKnop(246+25*answerModelNr+5 ,0,20,16,PlusMinKnop.HORIZONTAAL);
        tabPositieKnop.addActionListener(this);
        antwoordEditorPanel.add(tabPositieKnop,0);
        
     // GUI Vormbox
        titleVormLabel = new JLabel(WiskOpdr.rb.getString("FEV_titleVormLabel"));
    	titleVormLabel.setForeground(WiskOpdr.colorBlue1);
    	titleVormLabel.setFont(font.deriveFont(Font.BOLD, 16));
    	
        vormEditor = new FormuleEditor(true);
        vormEditor.setResizable(true);
        vormEditor.setMultiLine(true);
        vormEditor.setScrollHorizontal(true);
        vormEditor.setBounds(0,0,200,110);
        vormEditor.setFont(font);
        vormEditor.addActionListener(this);
        vormEditor.setVisible(false);
        
        vormEditorPanel = new JPanel();
        vormEditorPanel.setLayout(null);
        vormEditorPanel.setPreferredSize(new Dimension(200,120));
        vormEditorPanel.setMaximumSize(new Dimension(2860,160));
        vormEditorPanel.add(vormEditor);
        vormEditorPanel.addComponentListener(new EditorComponentListener());
        
        //GUI Feedback editor
        titleFeedbackTekstLabel = new JLabel(WiskOpdr.rb.getString("FEV_titleFeedbackLabel"));
        titleFeedbackTekstLabel.setForeground(WiskOpdr.colorBlue1);
        titleFeedbackTekstLabel.setFont(font.deriveFont(Font.BOLD, 16));
        
        titleFeedbackLabel = new JLabel(WiskOpdr.rb.getString("feedbackLabel"));
        titleFeedbackLabel.setForeground(WiskOpdr.colorBlue1);
        titleFeedbackLabel.setFont(font.deriveFont(Font.BOLD, 16));
        
        String[] items = {WiskOpdr.rb.getString("goedLabel"),WiskOpdr.rb.getString("halfLabel"),WiskOpdr.rb.getString("foutLabel")};
		goedFoutIP = new ActKeuzePanel(items,440,420,70,80);
		goedFoutIP.setPreferredSize(new Dimension(100,80));
    	
        feedbackEditor = new TekstEditor(false,true,true);
        feedbackEditor.setPreferredSize(new Dimension(200,120));
        feedbackEditor.setMaximumSize(new Dimension(2280,160));
        feedbackEditor.setBounds(5,350,280,110);
        feedbackEditor.setFont(font);
        feedbackEditor.addActionListener(this);
        feedbackEditor.setBackground(new Color(255,255,200));
        
        feedbackSizeCB = makeCheckBox(80,330,180,20,WiskOpdr.rb.getString("feedbackSizeCBLabel"),false,true);
	
        // GUI Verificatie box
        titleVerificatieLabel = new JLabel(WiskOpdr.rb.getString("FEV_titleVerificatieLabel"));
      	titleVerificatieLabel.setForeground(WiskOpdr.colorBlue1);
      	titleVerificatieLabel.setFont(font.deriveFont(Font.BOLD, 16));
      	
      	titleVerificatieScoreLabel = new JLabel(WiskOpdr.rb.getString("FEV_titleScoringLabel"));
      	titleVerificatieScoreLabel.setForeground(WiskOpdr.colorBlue1);
      	titleVerificatieScoreLabel.setFont(font.deriveFont(Font.BOLD, 16));
      	
      	gelijkwaardigCB = makeCheckBox(320,410,120,20,WiskOpdr.rb.getString("gelijkwaardigCBLabel"),true,true);
  		gelijkwaardigCB.addMouseListener(this);
		herleidingCB = makeCheckBox(320,435,120,20,WiskOpdr.rb.getString("vormCBLabel"),false,true);
		significantCB = makeCheckBox(320,460,120,20,WiskOpdr.rb.getString("significantCBLabel"),false,significantieAan?true:false);
		exactCB = makeCheckBox(320,significantieAan?485:460,120,20,WiskOpdr.rb.getString("exactCBLabel"),false,true);
		gelijkwaardigPV = makeTextField(460,410,30,20,"10",true);
		herleidingPV = makeTextField(460,435,30,20,"0",false);
		exactPV = makeTextField(460,significantieAan?485:460,30,20,"0",false);
		significantPV = makeTextField(460,460,30,20,"0",false);
		
		// GUI Score
		titleScoreLabel = new JLabel(WiskOpdr.rb.getString("FEV_titleScoringLabel"));
    	    titleScoreLabel.setForeground(WiskOpdr.colorBlue1);
    	    titleScoreLabel.setFont(font.deriveFont(Font.BOLD, 16));
    	
    	    feedbackPV = makeTextField(460,385,30,20,"0",false);
    	    scoreCumulatiefCB =  makeCheckBox(0,0,120,20,WiskOpdr.rb.getString("scoreCumulatiefCBLabel"),false,true);
        	
		
    	
		// Logging/Nakijken
		titleLoggingLabel = new JLabel(WiskOpdr.rb.getString("FEV_titleLoggingLabel"));
    	    titleLoggingLabel.setForeground(WiskOpdr.colorBlue1);
    	    titleLoggingLabel.setFont(font.deriveFont(Font.BOLD, 16));
    	
    	    checkCB = makeCheckBox(5,5,200,20,WiskOpdr.rb.getString("checkCBLabel"),true,true);
        teltMeeCB = makeCheckBox(225,5,200,20,WiskOpdr.rb.getString("teltMeeCBLabel"),true,true);
        logCB = makeCheckBox(450,5,70,20,WiskOpdr.rb.getString("logCBLabel"),false,true);
        logIDField = makeTextField(520,5,60,20,"0",false);
        logIDField.setPreferredSize(new Dimension(50,22));
        logIDLabelField = makeTextField(520,25,60,20,"",false);
        logIDLabelField.setPreferredSize(new Dimension(50,22));
        ScoringLabel = makeLabel(320,385,160,20,WiskOpdr.rb.getString("score"),true);
        logIDLabelLabel = makeLabel(470,25,50,20,WiskOpdr.rb.getString("TVEP_logIDLabelLabel"),false);
		
        logObjectivesButton = new ObjectiveChoiceButton();
        logObjectivesButton.setVisible(ObjectiveChoiceButton.hasObjectiveChoices());
        logObjectivesButton.setBounds(600,5,120,20);
        logObjectivesButton.setPreferredSize(new Dimension(120,22));
        logObjectivesButton.setMaximumSize(new Dimension(120,22));
        
        logMisconceptionsButton = new ObjectiveChoiceButton(WiskOpdr.rb.getString("OPT_misconceptions"), WiskOpdr.misconceptions, WiskOpdr.mccCategorieString);
        logMisconceptionsButton.setVisible(WiskOpdr.misconceptions!=null);
        logMisconceptionsButton.setBounds(350,350,120,20);
        logMisconceptionsButton.setPreferredSize(new Dimension(200,22));
        
        // Hulp setting
        titleHulpLabel = new JLabel(WiskOpdr.rb.getString("FEV_titleHulpLabel"));
        titleHulpLabel.setForeground(WiskOpdr.colorBlue1);
        titleHulpLabel.setFont(font.deriveFont(Font.BOLD, 16));
    	
        feedbackCB = makeCheckBox(140,-2,80,20,WiskOpdr.rb.getString("feedbackCBLabel"),false,true);
		
        rmKnopCB = makeCheckBox(650,105,150,20,WiskOpdr.rb.getString("rmCBLabel"),false,true);
        aantalDecRmField = makeTextField(670,135,100,20,"10",false);
        aantalDecRmLabel = makeLabel(670,135,100,20,WiskOpdr.rb.getString("rmAantalDecLabel"),false);
        
        stappenCB = makeCheckBox(630,50,200,20,WiskOpdr.rb.getString("stappenCBLabel"),true,false);
		rekenVakCB = makeCheckBox(630,50,200,20,"Formulevak als calculator",false,false);
		formuleToolBijFocusCB = makeCheckBox(530,50,270,20,WiskOpdr.rb.getString("formuleToolCBLabel"),false,false);
		subKnopCB = makeCheckBox(650,55,100,20,WiskOpdr.rb.getString("subKnopCBLabel"),false,true);
		subKnopExtraCB = makeCheckBox(650,80,100,20,WiskOpdr.rb.getString("subKnopExtraCBLabel"),false,false);
		eigenOpdrCB = makeCheckBox(690,160,270,20,WiskOpdr.rb.getString("eigenOpdrCBLabel"),false,true);
		uitwCB = makeCheckBox(530,75,270,20,WiskOpdr.rb.getString("uitwCBLabel"),false,false);
        
		tipsCB = makeCheckBox(500,55,100,20,"Ideas [test]",false,true);
        
		ideasButton = new IdeasInstellingenButton();
	    ideasButton.setPreferredSize(new Dimension(80,22));
	    ideasButton.setMaximumSize(new Dimension(80,22));
	    ideasButton.setFont(font);
	    ideasButton.setBounds(500,80,80,20);
	    ideasButton.setVisible(false);
	       
	    // GUI contextVar box
	    titleContextLabel = new JLabel(WiskOpdr.rb.getString("FEV_titleContextLabel"));
	    titleContextLabel.setForeground(WiskOpdr.colorBlue1);
	    titleContextLabel.setFont(font.deriveFont(Font.BOLD, 16));
    	
		contextVarCB = makeCheckBox(690,115,100,20,WiskOpdr.rb.getString("contextVarCBLabel"),false,true);
		
		antwoordSubstitutiesVak = new FormuleEditor(true);
        antwoordSubstitutiesVak.setBounds(15,270,570,150);
        antwoordSubstitutiesVak.setFont(font);
        antwoordSubstitutiesVak.addActionListener(this);
        antwoordSubstitutiesVak.setMultiLine(true);
        antwoordSubstitutiesVak.setResizable(true);
        
        substitutiesButton = new WiskOpdrButton(WiskOpdr.rb.getString("substitutiesButtonLabel"));
        substitutiesButton.setBounds(10,305,150,20);
        substitutiesButton.setPreferredSize(new Dimension(150,22));
        substitutiesButton.setMaximumSize(new Dimension(150,22));
        substitutiesButton.setMargin(new Insets(3,2,3,2));
        substitutiesButton.addActionListener(this);
        
        antwoordFunctiesVak = new FormuleEditor(true);
        antwoordFunctiesVak.setBounds(125,270,570,150);
        antwoordFunctiesVak.setFont(font);
        antwoordFunctiesVak.addActionListener(this);
        antwoordFunctiesVak.setMultiLine(true);
        antwoordFunctiesVak.setResizable(true);
        
        functiesButton = new WiskOpdrButton(WiskOpdr.rb.getString("functiesButtonLabel"));
        functiesButton.setMaximumSize(new Dimension(150,22));
        functiesButton.setBounds(180,305,150,20);
        functiesButton.setPreferredSize(new Dimension(150,22));
        functiesButton.setMargin(new Insets(3,2,3,2));
        functiesButton.addActionListener(this);
        
        // GUI Opmaak box
        titleOpmaakLabel = new JLabel(WiskOpdr.rb.getString("FEV_titleOpmaakLabel"));
    	titleOpmaakLabel.setForeground(WiskOpdr.colorBlue1);
    	titleOpmaakLabel.setFont(font.deriveFont(Font.BOLD, 16));
    	
    	boxMetRandCB = makeCheckBox(690,95,80,20,WiskOpdr.rb.getString("boxMetRand"),true,true);
    	
    	// HelpKnoppen
    	hbCheck = makeHelpButton(HELP_0_URL_CHECK);
    	hbTeltMee = makeHelpButton(HELP_0_URL_TELTMEE);
    	hbLogID = makeHelpButton(HELP_0_URL_LOGID);
    	hbFeedback = makeHelpButton(HELP_0_URL_FEEDBACK);
    	hbRekenmach = makeHelpButton(HELP_0_URL_REKENMACHINE);
    	hbSubstituties = makeHelpButton(HELP_0_URL_SUBSTITUTIES);
    	hbContextvar = makeHelpButton(HELP_0_URL_CONTEXTVAR);
    	hbEigenOpdr = makeHelpButton(HELP_0_URL_EIGENOPDR);
    	hbFormInvoer = makeHelpButton(HELP_0_URL_FORMINVOER);
    	hbUitwerking = makeHelpButton(HELP_0_URL_UITWERKING);
    	hbRand_0 = makeHelpButton(HELP_0_URL_RAND_0);
    	hbRand_2 = makeHelpButton(HELP_0_URL_RAND_2);
    	hbVerificatie = makeHelpButton(HELP_0_URL_VERIFICATIE);
    	hbAntwoord_0 = makeHelpButton(HELP_0_URL_ANTWOORD_0);
    	hbAntwoord_2 = makeHelpButton(HELP_0_URL_ANTWOORD_2);
    	hbStartExpressie = makeHelpButton(HELP_0_URL_STARTEXPRESSIE);
    	hbScore = makeHelpButton(HELP_0_URL_SCORE);
    	hbFeedbackTitel = makeHelpButton(HELP_0_URL_FEEDBACKTITLE);
    	
    	hbAntwoord_0.setBounds(140,-2,18,18);
    	hbAntwoord_2.setBounds(140,-2,18,18);
    	if(soort==0)antwoordEditorPanel.add(hbAntwoord_0,0);
    	if(soort==2)antwoordEditorPanel.add(hbAntwoord_2,0);
    	
    	hbFormInvoer.setVisible(false);
    	hbUitwerking.setVisible(false);
    	
		//Overige zaken, wellicht overbodig
		puntenLabel = makeLabel(460,385,40,20,WiskOpdr.rb.getString("puntenLabel"),true);
		checkTotaalLabel = makeLabel(620,385,160,20,WiskOpdr.rb.getString("checkTotaalLabel"),false);
		checkTotaalLabel.setForeground(Color.red);
		
		herleidingsKeuze = new JComboBox();
		herleidingsKeuze.setBounds(570,435,170,20);
		herleidingsKeuze.setFont(font);
		herleidingsKeuze.addActionListener(this);
		herleidingsKeuze.setVisible(false);
		
		herleidingItems = new String [7];
		herleidingItems[0] = WiskOpdr.rb.getString("herleidingKeuze_0");
		herleidingItems[1] = WiskOpdr.rb.getString("herleidingKeuze_1");
		herleidingItems[2] = WiskOpdr.rb.getString("herleidingKeuze_2");
		herleidingItems[3] = WiskOpdr.rb.getString("herleidingKeuze_3");
		herleidingItems[4] = WiskOpdr.rb.getString("herleidingKeuze_4");
		herleidingItems[5] = WiskOpdr.rb.getString("herleidingKeuze_5");
		herleidingItems[6] = WiskOpdr.rb.getString("herleidingKeuze_6");
		for (int i = 0; i<herleidingItems.length; i++) 
		{	herleidingsKeuze.addItem(herleidingItems[i]);
	    }
       
		removeAll();
	    plaatsGUI();
	    add(mainPanel);
    }
	
	public void plaatsGUI() {
		//plaats componenten startbox
        Component[] r11 = {titleStartLabel, ra(5,5), hbStartExpressie, 	hgl()};
		Component[] r12 = {ra(0,110), 			startEditorPanel};
		
		Component[] k1 = {hb(r11), ra(5,5), hb(r12)};
		startBox = vb(k1);
		
		//plaats componenten contextbox
		Component[] r21 = {titleContextLabel};
		Component[] r22 = {substitutiesButton};
		Component[] r23 = {functiesButton};
		
		Component[] k2 = {hb(r21),vst(15),hb(r22),vst(10),hb(r23), ra(10,20),vgl()};
		if(soort==0) {
			Component[] h1 = {ra(20,20),vb(k2)};
			contextBox = hb(h1);
		}
		else {
			Component[] h1 = {vb(k2), hgl()};
			contextBox = hb(h1);
		}
		
		// plaats compoenenten antwoordbox
				Component[] r31 = {ra(0,130), 	antwoordEditorPanel};
				Component[] k3 = {hb(r31)};
				antwoordBox = vb(k3);
				
					
		// plaatsComponenten settingBox
		Component[] r41 = {titleLoggingLabel, 	hgl()};
		Component[] r42 = {checkCB, 			ra(5,0),	hgl(),	hbCheck};
		Component[] r43 = {teltMeeCB, 			ra(5,0),	hgl(),	hbTeltMee};
		Component[] r44 = {logCB, 				ra(5,10), 	logIDField, ra(5,10), logIDLabelLabel, ra(5,10), logIDLabelField, 	ra(5,0),	hgl(),	hbLogID};
		Component[] r45 = {ra(6,0),			logObjectivesButton, hgl()};
		Component[] r46 = {titleHulpLabel, 		hgl()};
		Component[] r47 = {feedbackCB, 			ra(5,0),	hgl(),	hbFeedback};
		Component[] r48 = {formuleToolBijFocusCB, 			ra(5,0),	hgl(),	hbFormInvoer};
		Component[] r49 = {uitwCB, 				ra(5,0),	hgl(),	hbUitwerking};
		Component[] r410 = {rmKnopCB, 			ra(5,0),	hgl(),	hbRekenmach};
		Component[] r411 = {ra(20,0),			aantalDecRmLabel, 	ra(4,0),	aantalDecRmField,hgl()};
		Component[] r416 = {subKnopCB, 			ra(5,0),	hgl(),	hbSubstituties};
		Component[] r417 = {subKnopExtraCB, 	hgl()};
		Component[] r418 = {contextVarCB, 		ra(5,0),	hgl(),	hbContextvar};
		Component[] r419 = {eigenOpdrCB, 		ra(5,0),	hgl(),	hbEigenOpdr};
		Component[] r420 = {tipsCB, 			ideasButton,	hgl()};
		Component[] r421 = {contextBox, 		hgl()};
		Component[] r422 = {titleOpmaakLabel, 	hgl()};
		
		Box settingsBox;
		if(soort==0) {
			Component[] r424 = {boxMetRandCB, 		ra(5,0),	hgl(),	hbRand_0};
			Component[] k4 = {hb(r41),vst(5),hb(r42),hb(r43),hb(r44),vst(3),hb(r45),vst(20),hb(r46),vst(5),hb(r47),hb(r48),hb(r49),
					hb(r410),hb(r411),hb(r416),hb(r417),hb(r418),hb(r419),hb(r420), vst(20), hb(r422),vst(5),hb(r424), vgl()};
			settingsBox = vb(k4);
		}
		else {
			Component[] r424 = {boxMetRandCB, 		ra(5,0),	hgl(),	hbRand_2};
			Component[] k4 = {hb(r41),vst(5),hb(r42),hb(r43),hb(r44),vst(3),hb(r45),vst(20),hb(r46),vst(5),hb(r47),hb(r48),hb(r49),
				hb(r410),hb(r411),hb(r416),hb(r417),hb(r418),hb(r420), vst(20),hb(r421), hb(r422),vst(5),hb(r424), vgl()};
			settingsBox = vb(k4);
		}		
		settingsBox.setMaximumSize(new Dimension(500,800));
		
    	Box regelBox = Box.createHorizontalBox();
    	Box kolomBox = Box.createHorizontalBox();
    	
    	// plaats componenten feedback box
		Component[] r51 = {titleFeedbackLabel, 	ra(5,10),	hgl(), hbFeedbackTitel};
		Component[] r52 = {goedFoutIP,				hgl()};
		
		Component[] r53 = {titleFeedbackTekstLabel, 		hgl()};
		Component[] r54 = {ra(0,110),	 		feedbackEditor};
		
		Component[] k51 = {hb(r51),vst(5),hb(r52), vgl()};
		Component[] k52 = {hb(r53),vst(5),hb(r54), vgl()};
		
		Component[] h5 = {ra(20,0),vb(k51),hst(10), vb(k52)};
		feedbackBox = hb(h5);
		
		//plaats componenten scoringbox 
        Component[] r70 = {titleScoreLabel, 	ra(10,10), 		feedbackPV, ra(5,10), hbScore, hgl()};
        Component[] r71 = {scoreCumulatiefCB,  hgl()};
        //Component[] r71 = {ra(10,10), 		feedbackPV, 	hgl()};
		//Component[] r72 = {goedFoutIP, 		hgl()};
		
        if(soort==0) {
        	Component[] k7 = {hb(r70),vst(5),hb(r71),vgl()};
        	scoringBox = vb(k7);
        }
        else
        {
        	Component[] k7 = {hb(r70),vgl()};
        	scoringBox = vb(k7);
        }
		
		
		// plaats componenten verificatie box
		Component[] r61 = {titleVerificatieLabel, 	ra(5,10),	hbVerificatie,	hgl(),	ra(5,10),	titleVerificatieScoreLabel,ra(5,10)	};
		Component[] r62 = {gelijkwaardigCB,			ra(10,10),	hgl(),  		gelijkwaardigPV};
		Component[] r63 = {herleidingCB,			hgl(),  		herleidingPV};
		Component[] r65 = {significantCB,			hgl(),  		significantPV};
		Component[] r66 = {exactCB,					hgl(),  		exactPV};
		Component[] r67 = {scoringBox,					hgl(),  	};
		
		Component[] k6 = {hb(r61), vst(5), hb(r62), hb(r63), hb(r65), hb(r66), vst(10),vgl(),hb(r67)};
		Component[] h6 = {vb(k6)};
		verificatieBox = hb(h6);
		verificatieBox.setMaximumSize(new Dimension(240,300));
    			
		
		
		//plaats componenten vormbox
        Component[] r81 = {titleVormLabel, 		hgl()};
		Component[] r82 = {ra(0,110), 			vormEditorPanel};
		
		Component[] k8 = {hb(r81), vst(5), hb(r82), vgl()};
		vormBox = vb(k8);
		
		// boxes plaatsen
		Box boxh = Box.createHorizontalBox();
		mainPanel.add(boxh);
		
		Box boxv1 = Box.createVerticalBox();
		boxh.add(boxv1);
		boxh.add(Box.createHorizontalStrut(20));
		boxh.add(settingsBox);
		
		vormBox.setVisible(false);
		scoringBox.setVisible(false);
		feedbackBox.setVisible(false);
		contextBox.setVisible(false);
		
		Box boxh1 = Box.createHorizontalBox();
		Box boxh2 = Box.createHorizontalBox();
		Box boxh3 = Box.createHorizontalBox();
		if(soort==0) {
			boxv1.add(boxh1);
			boxv1.add(Box.createVerticalStrut(20));
		}
		
		boxv1.add(boxh2);
		boxv1.add(Box.createVerticalStrut(20));
		boxv1.add(boxh3);
		
		boxh1.add(startBox);
		if(soort==0)
			boxh1.add(contextBox);
		
		boxh2.add(antwoordBox);
		
		boxh3.add(verificatieBox);
		boxh3.add(Box.createHorizontalGlue());
		//boxh3.add(Box.createHorizontalStrut(5));
		//boxh3.add(scoringBox);
		boxh3.add(Box.createHorizontalStrut(10));
		boxh3.add(vormBox);
		//boxh3.add(Box.createHorizontalGlue());
		boxh3.add(feedbackBox);
	}
	
	private Box hb(Component[] c) {
		Box box = Box.createHorizontalBox();
		for(int i=0 ; c!=null && i<c.length ; i++) 
			box.add(c[i]);
		return box;
	}
	
	private Box vb(Component[] c) {
		Box box = Box.createVerticalBox();
		for(int i=0 ; c!=null && i<c.length ; i++) 
			box.add(c[i]);
		return box;
	}
	
	private Component hgl() {
		return Box.createHorizontalGlue();
	}
	
	private Component vgl() {
		return Box.createVerticalGlue();
	}
	
	private Component hst(int n) {
		return Box.createHorizontalStrut(n);
	}
	
	private Component vst(int n) {
		return Box.createVerticalStrut(n);
	}
	
	private Component ra(int w, int h) {
		return Box.createRigidArea(new Dimension(w,h));
	}

    static AntwoordFormuleVakEditPanel instance;
    
    public static void main(String[] args) {
    	WiskOpdr wiskOpdr = new WiskOpdr();
    	instance = new AntwoordFormuleVakEditPanel(2);
    	makeFrame(wiskOpdr);
    }
    static Frame frame;
    public static void makeFrame(Component c){
    	frame = new JFrame();
	   	frame.setLayout(new BorderLayout());
	    frame.add(instance);
	    frame.pack();
	    frame.setVisible(true);
	}
    
	public JCheckBox makeCheckBox(int x, int y, int b, int h, String text, boolean selected, boolean visible)
	{	JCheckBox checkbox = new WiskOpdrCheckbox(text);
		checkbox.setBounds(x,y,b,h);
		checkbox.setFont(font);
		checkbox.setOpaque(false);
		checkbox.addActionListener(this);
		checkbox.setSelected(selected);
		checkbox.setVisible(visible);
		add(checkbox,0);
		return checkbox;
	}
	
	public JLabel makeLabel(int x, int y, int b, int h, String text, boolean visible)
	{	JLabel label = new JLabel(text);
		label.setForeground(WiskOpdr.colorBlue1);
		label.setBounds(x,y,b,h);
		label.setFont(font);
		label.setVisible(visible);
		add(label,0);
		return label;
	}
	
	public JTextField makeTextField(int x, int y, int b, int h, String text, boolean visible)
	{	JTextField textField = new WiskOpdrTextField(text);
		textField.setBounds(x,y,b,h);
		textField.setPreferredSize(new Dimension(40,22));
        textField.setMaximumSize(new Dimension(50,22));
		textField.setFont(font);
		textField.addActionListener(this);
		textField.setVisible(visible);
		add(textField,0);
		return textField;
	}
	
	public HelpButton makeHelpButton(String url) {
		HelpButton helpButton = new HelpButton(url);
		helpButton.addActionListener(this);
 		helpButton.setFont(new Font("SansSerif",Font.BOLD,12));
 		helpButton.setPreferredSize(new Dimension(18,18));
 		helpButton.setMinimumSize(new Dimension(18,18));
 		helpButton.setMaximumSize(new Dimension(18,18));
 		helpButton.setVisible(false);
 		return helpButton;
	}
	
	public void zetBreedte(int b)
	{	//grafiekPanel.setSize(b,grafiekPanel.getSize().height);
	}
	public void zetHoogte(int h)
	{	//grafiekPanel.setSize(grafiekPanel.getSize().width, h);
	}
	
	private Hashtable fillAnswerModel(Hashtable h)
	{
		String antwoordString = "$f@";
		boolean gelijkwaardig = true;
		boolean herleiding = false;
		boolean exact = false;
		boolean significant = false;
		int puntenFeedback = 0;
		int soortHerleiding = 0;
		String feedback = "";
		int feedbackWidth = 195;
		int feedbackHeight = 20;
		String vormString = "$f@";
		int goedHalfFout = 2;
		boolean[][] logMisconceptions = null;
		
		antwoordString = antwoordvak.geefFormuleVak().toString();
		gelijkwaardig = this.gelijkwaardig;
		herleiding = this.herleiding;
		exact = this.exact;
		significant = this.significant;
		soortHerleiding = this.soortHerleiding;
				
		puntenFeedback = (Integer.parseInt(feedbackPV.getText()));
		feedback  = feedbackEditor.getText();
		if(feedbackEditorPopupFrame!=null)
		{	feedbackWidth = feedbackEditorPopupFrame.getSize().width - feedbackEditorPopupFrame.getInsets().left - feedbackEditorPopupFrame.getInsets().right;
			feedbackHeight = feedbackEditorPopupFrame.getSize().height - feedbackEditorPopupFrame.getInsets().top - feedbackEditorPopupFrame.getInsets().bottom;
		}
		
		//vormString = vormEditor.geefFormuleVak().toString();
		String[] vormStrings = vormEditor.geefRegels();
        if(vormStrings.length==1) vormString = vormEditor.geefFormuleVak().toString();
        else
        {	vormString = "$f";
        	for(int i=0 ; i<vormStrings.length ; i++)
        	{	vormString = vormString + vormStrings[i].substring(2,vormStrings[i].length()-1) + "::";
        	}
        	vormString = vormString.substring(0,vormString.length()-2) + "@";
        }
		goedHalfFout = goedFoutIP.geefKeuze()-1;
		
		logMisconceptions = logMisconceptionsButton.getChoices();
		
		h.put("antwoordString",antwoordString);
		h.put("gelijkwaardig",new Boolean(gelijkwaardig));
		h.put("herleiding",new Boolean(herleiding));
		h.put("exact",new Boolean(exact));
		h.put("significant",new Boolean(significant));
		h.put("puntenFeedback",new Integer(puntenFeedback));
		h.put("soortHerleiding",new Integer(soortHerleiding));
		h.put("feedback",feedback);
		h.put("feedbackWidth",new Integer(feedbackWidth));
		h.put("feedbackHeight",new Integer(feedbackHeight));
		h.put("vormString",vormString);
		h.put("goedHalfFout",new Integer(goedHalfFout));
		if(logMisconceptions!=null)
			h.put("logMisconceptions",logMisconceptions);
		return h;
	}
	
	private void updateFeedbackTitelLabel()
    {	String feedbackNrString = "";
    	if(hasFeedback && answerModelNr>0) {
    		feedbackNrString += (answerModelNr+1);
    		titleFeedbackTekstLabel.setText(WiskOpdr.rb.getString("FEV_titleFeedbackLabel") + " " + feedbackNrString);
    		titleFeedbackLabel.setText(WiskOpdr.rb.getString("feedbackLabel") + " " + feedbackNrString);
	    	titleVerificatieLabel.setText(WiskOpdr.rb.getString("FEV_titleVerificatieLabel") + " " + feedbackNrString);
    		titleAntwoordLabel.setText(WiskOpdr.rb.getString("FEV_titleAntwoordNrLabel") + " " + feedbackNrString);
	    	titleScoreLabel.setText(WiskOpdr.rb.getString("FEV_titleScoringLabel") + " " + feedbackNrString);
	    	scoringBox.validate();
	   }
    	else {
    		titleFeedbackTekstLabel.setText(WiskOpdr.rb.getString("FEV_titleFeedbackLabel"));
    		titleFeedbackLabel.setText(WiskOpdr.rb.getString("feedbackLabel"));
    		titleVerificatieLabel.setText(WiskOpdr.rb.getString("FEV_titleVerificatieLabel"));
    		titleAntwoordLabel.setText(WiskOpdr.rb.getString("FEV_titleAntwoordLabel"));
	    	titleScoreLabel.setText(WiskOpdr.rb.getString("FEV_titleScoringLabel"));
	    	titleScoreLabel.setText(WiskOpdr.rb.getString("FEV_titleScoringLabel") + (hasFeedback && !scoreCumulatiefCB.isSelected() ? " max" : " 1"));
	    	scoringBox.validate();
	   }
    }
	
	private void setAnswerModel(Hashtable h)
	{	String antwoordString = "$f@";
		boolean gelijkwaardig = true;
		boolean herleiding = false;
		boolean exact = false;
		boolean significant = false;
		int puntenFeedback = 0;
		int soortHerleiding = 0;
		String feedback = "";
		int feedbackWidth = 0;
        int feedbackHeight = 0;
		String vormString = "$f@";
		int goedHalfFout = 2;
		boolean[][] logMisconceptions = null;
		
		if(h!=null) 
		{	if(h.containsKey("antwoordString")) antwoordString = (String)h.get("antwoordString");
			if(h.containsKey("gelijkwaardig")) gelijkwaardig = ((Boolean)h.get("gelijkwaardig")).booleanValue();
			if(h.containsKey("herleiding")) herleiding = ((Boolean)h.get("herleiding")).booleanValue();
			if(h.containsKey("exact")) exact = ((Boolean)h.get("exact")).booleanValue();
			if(h.containsKey("significant")) significant = ((Boolean)h.get("significant")).booleanValue();
			if(h.containsKey("stappen")) stappen = ((Boolean)h.get("stappen")).booleanValue();
			if(h.containsKey("soortHerleiding")) soortHerleiding = ((Integer)h.get("soortHerleiding")).intValue();
			if(h.containsKey("puntenFeedback")) puntenFeedback = ((Integer)h.get("puntenFeedback")).intValue();
			if(h.containsKey("feedback")) feedback = (String)h.get("feedback");
			if(h.containsKey("feedbackWidth")) feedbackWidth = ((Integer)h.get("feedbackWidth")).intValue();
			if(h.containsKey("feedbackHeight")) feedbackHeight = ((Integer)h.get("feedbackHeight")).intValue();
            if(h.containsKey("vormString")) vormString = (String)h.get("vormString");
			if(h.containsKey("goedHalfFout")) goedHalfFout = ((Integer)h.get("goedHalfFout")).intValue();
			if(h.containsKey("logMisconceptions")) logMisconceptions = (boolean[][])h.get("logMisconceptions");
			
		}
		this.herleiding = herleiding;
		this.gelijkwaardig = gelijkwaardig;
		this.exact = exact;
		this.significant = significant;
		this.soortHerleiding = soortHerleiding;
		this.puntenFeedback = puntenFeedback;
		
		antwoordvak.geefFormuleVak().vulVak(antwoordString);
		//vormEditor.geefFormuleVak().vulVak(vormString);
		String[] vormStrings = StringUtils.split(vormString, "::");
        for(int i=0 ; i<vormStrings.length ; i++)
    	{	if(i==0) vormStrings[i] = vormStrings[i] + "@";
    		else if(i==vormStrings.length-1) vormStrings[i] = "$f" + vormStrings[i];
    		else  vormStrings[i] = "$f" + vormStrings[i] + "@";
    	}
        vormEditor.verwijderRegels();
        vormEditor.zetRegels(vormStrings);
			
		herleidingCB.setVisible(true);
		exactCB.setVisible(true);
		if(significantieAan) significantCB.setVisible(true);
		herleidingsKeuze.setVisible(true);
				
		gelijkwaardigCB.setSelected(gelijkwaardig);
		herleidingCB.setSelected(herleiding);
		exactCB.setSelected(exact);
		significantCB.setSelected(significant);
		
		vormEditor.setVisible(herleiding);
		vormBox.setVisible(herleiding);
		
		feedbackPV.setVisible(hasFeedback);
		feedbackPV.setText(""+puntenFeedback);
		
		feedbackEditor.zetTekst(feedback);
		feedbackEditor.layoutTekst();
		feedbackEditor.setEnlargedWidth(feedbackWidth);
		feedbackEditor.setEnlargedHeight(feedbackHeight);
		if(feedbackEditorPopupFrame!=null)
		{	feedbackEditor.setEnlargedSize();
			int width = feedbackWidth + feedbackEditorPopupFrame.getInsets().left + feedbackEditorPopupFrame.getInsets().right;
        	int height = feedbackHeight + feedbackEditorPopupFrame.getInsets().top + feedbackEditorPopupFrame.getInsets().bottom;
        	feedbackEditorPopupFrame.setSize(width,height);
		}
		feedbackEditor.repaint();
		
		goedFoutIP.setItem(goedHalfFout);
		
		logMisconceptionsButton.setChoices(logMisconceptions);
		
		updateFeedbackTitelLabel();
	}
	
	private void getAnswerModel()
	{	if(answerModels==null)return;
		answerModels[answerModelNr] = fillAnswerModel(new Hashtable());
	}
	
	private void setAnswerModel()
	{	if(answerModels==null)return;
		logMisconceptionsButton.setVisible(WiskOpdr.misconceptions!=null && answerModelNr>-1);
		setAnswerModel(answerModels[answerModelNr]);	
	}
	
	public Hashtable changeToCompatibleEditState(Hashtable interactiePanelLaunchState)
    {	Hashtable compatibleLaunchSate = new Hashtable();
    	compatibleLaunchSate.putAll(interactiePanelLaunchState);
    	compatibleLaunchSate.remove("startString");
    	compatibleLaunchSate.remove("stappen");
    	compatibleLaunchSate.remove("subKnop");
    	compatibleLaunchSate.remove("subKnopExtra");
    	compatibleLaunchSate.remove("formuleToolBijFocus");
    	compatibleLaunchSate.remove("rmKnop");
    	compatibleLaunchSate.remove("linStrategieVersie");
    	compatibleLaunchSate.remove("linOefenVersie");
    	compatibleLaunchSate.remove("bordjesMethode");
    	compatibleLaunchSate.remove("tips");
    	compatibleLaunchSate.remove("ideasInstellingen");
    	
    	compatibleLaunchSate.remove("uitw");
    	compatibleLaunchSate.remove("aantalDecRm");
    	compatibleLaunchSate.remove("eigenOpdr");
    	return compatibleLaunchSate;
    }
	
	public void setEditState(Hashtable interactiePanelLaunchState)
	{			
				String antwoordString = "$f@";
				String startString = "$f@";
				boolean herleiding = false;
				boolean exact = false;
				boolean significant = false;
				boolean stappen = stappenDefault;
				int soortHerleiding = 0;
				int puntenGelijkwaardig = 10;
				int puntenHerleiding = 0;
				int puntenExact = 0;
				int puntenSignificant = 0;
				boolean formuleToolBijFocus = false;
				Hashtable[] answerModels = null;
				boolean hasFeedback = false;
				boolean feedbackSize = false;
				String vormString = "$f@";
				boolean subKnop = false;
				boolean subKnopExtra = false;
				boolean rmKnop = false;
				boolean check = true;
				boolean teltMee = true;
				boolean logOption = false;
				String logID = "";
				String logIDLabel = "";
				boolean[][] logObjectives = null;
				String[] smObjectives = null;
				String[] smDeselections = null;
				boolean hasObjectives = false;
				double eqTestValueMin = 0;
				double eqTestValueMax = 5;
				int aantalDecRm = 10;
				boolean uitw = false;
				boolean tips = false;
                Hashtable ideasInstellingen = new Hashtable();
                boolean eigenOpdr = false;
                boolean boxMetRand = true;
                String[] antwoordSubStrings = null;
                String[] antwoordFuncStrings = null;
                boolean scoreCumulatief = false;
				
				if(interactiePanelLaunchState.containsKey("antwoordString")) antwoordString = (String)interactiePanelLaunchState.get("antwoordString");
				if(interactiePanelLaunchState.containsKey("startString")) startString = (String)interactiePanelLaunchState.get("startString");
				if(interactiePanelLaunchState.containsKey("herleiding")) herleiding = ((Boolean)interactiePanelLaunchState.get("herleiding")).booleanValue();
				if(interactiePanelLaunchState.containsKey("exact")) exact = ((Boolean)interactiePanelLaunchState.get("exact")).booleanValue();
				if(interactiePanelLaunchState.containsKey("significant")) significant = ((Boolean)interactiePanelLaunchState.get("significant")).booleanValue();
				if(interactiePanelLaunchState.containsKey("stappen")) stappen = ((Boolean)interactiePanelLaunchState.get("stappen")).booleanValue();
				if(interactiePanelLaunchState.containsKey("soortHerleiding")) soortHerleiding = ((Integer)interactiePanelLaunchState.get("soortHerleiding")).intValue();
				if(interactiePanelLaunchState.containsKey("puntenGelijkwaardig")) puntenGelijkwaardig = ((Integer)interactiePanelLaunchState.get("puntenGelijkwaardig")).intValue();
				if(interactiePanelLaunchState.containsKey("puntenHerleiding")) puntenHerleiding = ((Integer)interactiePanelLaunchState.get("puntenHerleiding")).intValue();
				if(interactiePanelLaunchState.containsKey("puntenExact")) puntenExact = ((Integer)interactiePanelLaunchState.get("puntenExact")).intValue();
				if(interactiePanelLaunchState.containsKey("puntenSignificant")) puntenSignificant = ((Integer)interactiePanelLaunchState.get("puntenSignificant")).intValue();
				if(interactiePanelLaunchState.containsKey("formuleToolBijFocus")) formuleToolBijFocus = ((Boolean)interactiePanelLaunchState.get("formuleToolBijFocus")).booleanValue();
				if(interactiePanelLaunchState.containsKey("answerModels")) answerModels = (Hashtable[])interactiePanelLaunchState.get("answerModels");
				if(interactiePanelLaunchState.containsKey("hasFeedback")) hasFeedback = ((Boolean)interactiePanelLaunchState.get("hasFeedback")).booleanValue();
				if(interactiePanelLaunchState.containsKey("feedbackSize")) feedbackSize = ((Boolean)interactiePanelLaunchState.get("feedbackSize")).booleanValue();
				if(interactiePanelLaunchState.containsKey("vormString")) vormString = (String)interactiePanelLaunchState.get("vormString");
				if(interactiePanelLaunchState.containsKey("subKnop")) subKnop = ((Boolean)interactiePanelLaunchState.get("subKnop")).booleanValue();
				if(interactiePanelLaunchState.containsKey("subKnopExtra")) subKnopExtra = ((Boolean)interactiePanelLaunchState.get("subKnopExtra")).booleanValue();
				if(interactiePanelLaunchState.containsKey("rmKnop")) rmKnop = ((Boolean)interactiePanelLaunchState.get("rmKnop")).booleanValue();
				if(interactiePanelLaunchState.containsKey("check")) check = ((Boolean)interactiePanelLaunchState.get("check")).booleanValue();
				if(interactiePanelLaunchState.containsKey("teltMee")) teltMee = ((Boolean)interactiePanelLaunchState.get("teltMee")).booleanValue();
				if(interactiePanelLaunchState.containsKey("logOption")) logOption = ((Boolean)interactiePanelLaunchState.get("logOption")).booleanValue();
				if(interactiePanelLaunchState.containsKey("logID")) logID = (String)interactiePanelLaunchState.get("logID");
				if(interactiePanelLaunchState.containsKey("logIDLabel")) logIDLabel = (String)interactiePanelLaunchState.get("logIDLabel");
				if(interactiePanelLaunchState.containsKey("eqTestValueMin")) eqTestValueMin = ((Double)interactiePanelLaunchState.get("eqTestValueMin")).doubleValue();
				if(interactiePanelLaunchState.containsKey("eqTestValueMax")) eqTestValueMax = ((Double)interactiePanelLaunchState.get("eqTestValueMax")).doubleValue();
				if(interactiePanelLaunchState.containsKey("aantalDecRm")) aantalDecRm = ((Integer)interactiePanelLaunchState.get("aantalDecRm")).intValue();
				if(interactiePanelLaunchState.containsKey("uitw")) uitw = ((Boolean)interactiePanelLaunchState.get("uitw")).booleanValue();
				if(interactiePanelLaunchState.containsKey("eigenOpdr")) eigenOpdr = ((Boolean)interactiePanelLaunchState.get("eigenOpdr")).booleanValue();
				if(interactiePanelLaunchState.containsKey("boxMetRand")) boxMetRand = ((Boolean)interactiePanelLaunchState.get("boxMetRand")).booleanValue();
				if(interactiePanelLaunchState.containsKey("scoreCumulatief")) scoreCumulatief = ((Boolean)interactiePanelLaunchState.get("scoreCumulatief")).booleanValue();
				if(interactiePanelLaunchState.containsKey("tips")) tips = ((Boolean)interactiePanelLaunchState.get("tips")).booleanValue();
                if(tips){
                	if(interactiePanelLaunchState.containsKey("ideasInstellingen")) ideasInstellingen = (Hashtable)interactiePanelLaunchState.get("ideasInstellingen");
                }
                if(interactiePanelLaunchState.containsKey("logObjectives")) 
                	try	{	
        				logObjectives = (boolean[][])interactiePanelLaunchState.get("logObjectives");
        			} catch(Exception ex){
        			}
                    try {
                        smObjectives = (String[]) interactiePanelLaunchState.get(Constants.OBJECTIVES);
                        smDeselections = (String[]) interactiePanelLaunchState.get(Constants.DESELECTIONS);
                    } catch(Exception ex) {}
                
        		if(interactiePanelLaunchState.containsKey("hasObjectives"))
        			hasObjectives = ((Boolean)interactiePanelLaunchState.get("hasObjectives")).booleanValue();
        		else
        			hasObjectives = logObjectives != null;
        		if(interactiePanelLaunchState.containsKey("antwoordSubStrings")) antwoordSubStrings = (String[])interactiePanelLaunchState.get("antwoordSubStrings");
                if(interactiePanelLaunchState.containsKey("antwoordFuncStrings")) antwoordFuncStrings = (String[])interactiePanelLaunchState.get("antwoordFuncStrings");
                
				this.herleiding = herleiding;
				this.exact = exact;
				this.significant = significant;
				this.soortHerleiding = soortHerleiding;
				this.puntenGelijkwaardig = puntenGelijkwaardig;
				this.puntenHerleiding = puntenHerleiding;
				this.puntenExact = puntenExact;
				this.puntenSignificant = puntenSignificant;
				this.stappen = stappen;
				this.formuleToolBijFocus = formuleToolBijFocus;
				
				if(answerModels != null)
				{	this.answerModels = new Hashtable[answerModels.length];
					for(int i=0 ; i<answerModels.length ; i++)
					{	this.answerModels[i] = answerModels[i];
					}
				}
					//this.answerModels = answerModels;
				
				this.subKnop = subKnop;
				this.subKnopExtra = subKnopExtra;
				
				this.eqTestValueMin = eqTestValueMin;
				this.eqTestValueMax = eqTestValueMax;
				
				this.tips = tips;
				scoreCumulatiefCB.setSelected(scoreCumulatief);
				ideasButton.setVisible(tips);
				if(tips)
                {  	ideasButton.zetInstellingen(ideasInstellingen);
                	tipsCB.setSelected(tips);
                    
                }
				
				if(hasFeedback)
				{	aantalAnswerModels = answerModels.length;
					antwoordEditorPanel.remove(tabbladTab);
					tabbladTab = new OpdrachtNrRij(aantalAnswerModels, 250,20);
					tabbladTab.setTab(true);
					tabbladTab.setScoresVisible(false);
					tabbladTab.setSize(tabbladTab.getSize().width, 23);
					tabbladTab.addActionListener(this);
					tabbladTab.setBackground(new Color(210,210,210));
					tabbladTab.setSelected(answerModelNr+1);
					antwoordEditorPanel.add(tabbladTab,0);
					aantalTabsKnop.setLocation(250+25*aantalAnswerModels+5 ,24);
					
					answerModelNr = 0;
					setAnswerModel();
					antwoordEditorPanel.setPreferredSize(new Dimension(Math.max(250+25*aantalAnswerModels+40,450),140));
	            }
				
				antwoordvak.geefFormuleVak().vulVak(antwoordString);
				startEditor.geefFormuleVak().vulVak(startString);
				//vormEditor.geefFormuleVak().vulVak(vormString);
				String[] vormStrings = StringUtils.split(vormString, "::");
                for(int i=0 ; i<vormStrings.length ; i++)
            	{	if(i==0) vormStrings[i] = vormStrings[i] + "@";
            		else if(i==vormStrings.length-1) vormStrings[i] = "$f" + vormStrings[i];
            		else  vormStrings[i] = "$f" + vormStrings[i] + "@";
            	}
                vormEditor.zetRegels(vormStrings);
                
                antwoordSubstitutiesVak.zetRegels(antwoordSubStrings);
                antwoordFunctiesVak.zetRegels(antwoordFuncStrings);
                boolean hasSub = antwoordSubStrings!=null && (antwoordSubStrings.length>0 && !antwoordSubStrings[0].equals("$f@"));
                boolean hasFunc = antwoordFuncStrings!=null && (antwoordFuncStrings.length>0 && !antwoordFuncStrings[0].equals("$f@"));
                contextBox.setVisible(hasSub || hasFunc);
                this.contextVarCB.setSelected(hasSub || hasFunc);
                               
				stappenCB.setSelected(stappen);
				formuleToolBijFocusCB.setSelected(formuleToolBijFocus);
				
				subKnopCB.setSelected(subKnop);
				subKnopExtraCB.setVisible(subKnop);
				subKnopExtraCB.setSelected(subKnopExtra);
				
	           
	                
	            checkCB.setSelected(check);
	            teltMeeCB.setSelected(teltMee);
	            logCB.setSelected(logOption);
	            
	            uitwCB.setSelected(uitw);
	            boxMetRandCB.setSelected(boxMetRand);
	            eigenOpdrCB.setSelected(eigenOpdr);
	            rmKnopCB.setVisible(uitw||stappen);
	            rmKnopCB.setSelected(rmKnop);
	            aantalDecRmField.setVisible(uitw && rmKnop);
	            aantalDecRmLabel.setVisible(uitw && rmKnop);
		    	
	            //startLabel.setVisible(uitw);
				//startEditor.setVisible(uitw);
	                
	            logIDField.setVisible(logOption);
	            logIDLabelField.setVisible(logOption);
	            logIDLabelLabel.setVisible(logOption);
	            //logObjectivesButton.setVisible(hasObjectives);
	            logIDField.setText(logID);
	            logIDLabelField.setText(logIDLabel);
	            
	            logObjectivesButton.setChoices(logObjectives); // this order!
	            logObjectivesButton.setObjectives(smObjectives);
	            logObjectivesButton.setDeselections(smDeselections);
	           	            
	            aantalDecRmField.setVisible(rmKnop);
	            aantalDecRmLabel.setVisible(rmKnop);
	            aantalDecRmField.setText(""+aantalDecRm);
				
                gelijkwaardigPV.setText(""+puntenGelijkwaardig);
                
				setFeedbackOption(hasFeedback);
				feedbackCB.setSelected(hasFeedback);
				feedbackSizeCB.setSelected(feedbackSize);
				feedbackEditor.setResizable(feedbackSize);
				if(hasFeedback) {
					 ((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)mainPanel)).pack();
					return;
				}
				
                herleidingCB.setSelected(herleiding);
                herleidingPV.setVisible(herleiding);
                herleidingPV.setText(""+puntenHerleiding);
                
                vormEditor.setVisible(herleiding);
                vormBox.setVisible(herleiding);
                
                exactCB.setSelected(exact);
                exactPV.setVisible(exact);
                exactPV.setText(""+puntenExact);
                
                significantCB.setSelected(significant);
                significantPV.setVisible(significant && significantieAan);
                significantPV.setText(""+puntenSignificant);
                
                ((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)mainPanel)).pack();

		
			//// EIND //// Deze code zal moeten worden aangepast als de interface meerdere antwoordvakken ondersteunt
			
			
		
	}
	
	public Hashtable getEditState()
	{	
		Hashtable interactiePanelLaunchState = new Hashtable();
        
        
			String antwoordString = null;
			String startString = null;
			boolean herleiding = false;
			boolean exact = false;
			boolean significant = false;
			boolean stappen = false;
			int soortHerleiding = 0;
			int puntenGelijkwaardig = 10;
			int puntenHerleiding = 0;
			int puntenExact = 0; 
			int puntenSignificant = 0; 
			int scoreMax = 0;
			int[][] scoreMaxObjectives = null;
			boolean formuleToolBijFocus = false;
			Hashtable[] answerModels;
			boolean hasFeedback;
			boolean feedbackSize;
			String vormString = "$f@";
			boolean subKnop = false;
			boolean subKnopExtra = false;
			boolean rmKnop = false;
			boolean check = true;
			boolean teltMee = true;
			boolean logOption = false;
			String logID = "";
			String logIDLabel = "";
			boolean[][] logObjectives = null;
			double eqTestValueMin = 0;
			double eqTestValueMax = 5;
			int aantalDecRm = 10;
			boolean uitw = false;
			boolean tips;
			Hashtable ideasInstellingen = new Hashtable();
			boolean eigenOpdr = false;
			boolean boxMetRand = true;
			String[] antwoordSubStrings = null;
            String[] antwoordFuncStrings = null;
            boolean scoreCumulatief = false;
			
			getAnswerModel();
			answerModels = this.answerModels;
			if(answerModels!=null)setAnswerModel(answerModels[0]);
			
			antwoordString = antwoordvak.geefFormuleVak().toString();
			startString = startEditor.geefFormuleVak().toString();
			//vormString = vormEditor.geefFormuleVak().toString();
			String[] vormStrings = vormEditor.geefRegels();
            if(vormStrings.length==1) vormString = vormEditor.geefFormuleVak().toString();
            else
            {	vormString = "$f";
            	for(int i=0 ; i<vormStrings.length ; i++)
            	{	vormString = vormString + vormStrings[i].substring(2,vormStrings[i].length()-1) + "::";
            	}
            	vormString = vormString.substring(0,vormString.length()-2) + "@";
            }
			herleiding = this.herleiding;
			exact = this.exact;
			significant = this.significant;
			stappen = this.stappen;
			soortHerleiding = this.soortHerleiding;
			try
			{	puntenGelijkwaardig = Integer.parseInt(gelijkwaardigPV.getText());
				this.puntenGelijkwaardig = puntenGelijkwaardig;
			}	
			catch(Exception ex)
			{	}
			try
			{	puntenHerleiding = Integer.parseInt(herleidingPV.getText());
				this.puntenHerleiding = puntenHerleiding;
			}	
			catch(Exception ex)
			{	}
			try
			{	puntenExact = Integer.parseInt(exactPV.getText());
				this.puntenExact = puntenExact;
			}	
			catch(Exception ex)
			{	}
			try
			{	puntenSignificant = Integer.parseInt(significantPV.getText());
				this.puntenSignificant = puntenSignificant;
			}	
			catch(Exception ex)
			{	}
			try
			{	aantalDecRm = Integer.parseInt(aantalDecRmField.getText());
			}	
			catch(Exception ex)
			{	}
			
			antwoordSubStrings = antwoordSubstitutiesVak.geefRegels();
            antwoordFuncStrings = antwoordFunctiesVak.geefRegels();
           	
    		for (int i = 0; i < antwoordFuncStrings.length; i++)
    		{
    			if(antwoordFuncStrings[i]==null || antwoordFuncStrings[i].equals("$f@"))
    				break;
    			String[] functieDelen = antwoordFuncStrings[i].split("=");
    			if(functieDelen.length!=2) {
    				JOptionPane.showMessageDialog(this, "Syntax van functiedefinitie klopt niet");
    				break;
    			}
    			//String functieExpressieString = "$f"+functieDelen[1];
    			//Expressie functieExpressie = FormuleParser.geefExpressie(functieExpressieString);
    			//if(functieExpressie==null) {
    			//	JOptionPane.showMessageDialog(this, "Syntax van functie-expressie klopt niet");
    			//	break;
    			//}
    			System.out.println(functieDelen[0].substring(2));
    			/*String pattern = "[a-zA-Z]+[']?[(][a-zA-Z][)]";
    	        boolean matches = Pattern.matches(pattern, functieDelen[0].substring(2));
    	        if(!matches)
    	        {	JOptionPane.showMessageDialog(this, "Syntax klopt niet. Gebruik bv:\n f(x)=expressie \n of \n func(x)=expressie");
    	        	break;
    	        }*/
    		}
			
			check = checkCB.isSelected();
			teltMee = teltMeeCB.isSelected();
			logOption = logCB.isSelected();
			logID = logIDField.getText();
			logIDLabel = logIDLabelField.getText();
			logObjectives = logObjectivesButton.getChoices();
			String[] smObjectives = logObjectivesButton.getObjectives();
			String[] smDeselections = logObjectivesButton.getDeselections();
			
			puntenGelijkwaardig = this.puntenGelijkwaardig;
			puntenHerleiding = this.puntenHerleiding;
			puntenExact = this.puntenExact;
			puntenSignificant = this.puntenSignificant;
			formuleToolBijFocus = this.formuleToolBijFocus;
			scoreMax = puntenGelijkwaardig + puntenHerleiding + puntenExact;
			hasFeedback = this.hasFeedback;
			feedbackSize = feedbackSizeCB.isSelected();
            scoreCumulatief = scoreCumulatiefCB.isSelected();
            if(hasFeedback) { 
            		scoreMax = puntenFeedback;
            		if(scoreCumulatief) {
            			scoreMax = 0;
            			for(int i=0 ; i<answerModels.length ; i++) {
            				scoreMax += (Integer)answerModels[i].get("puntenFeedback");
            			}
            		}
            }
			if(!teltMee)scoreMax = 0;
			
			if(logObjectives!=null)
			{	scoreMaxObjectives = new int[logObjectives.length][];
				for(int j=0 ; j<scoreMaxObjectives.length; j++)
				{	scoreMaxObjectives[j] = new int[logObjectives[j].length];
					for(int i=0 ; i<scoreMaxObjectives[j].length ; i++)
					{	if(logObjectives[j][i]) scoreMaxObjectives[j][i] = scoreMax;
					}
				}
			}
			subKnop = this.subKnop;
			subKnopExtra = this.subKnopExtra;
			rmKnop = rmKnopCB.isSelected();
						
			uitw = uitwCB.isSelected();
			eigenOpdr = eigenOpdrCB.isSelected();
			boxMetRand = boxMetRandCB.isSelected();
			
			eqTestValueMin = this.eqTestValueMin;
			eqTestValueMax = this.eqTestValueMax;
			
			tips = this.tips;
			if(tips) ideasInstellingen = ideasButton.geefInstellingen();
            
			interactiePanelLaunchState.put("antwoordString",antwoordString);
			interactiePanelLaunchState.put("startString",startString);
			interactiePanelLaunchState.put("herleiding",new Boolean(herleiding));
			interactiePanelLaunchState.put("exact",new Boolean(exact));
			interactiePanelLaunchState.put("significant",new Boolean(significant));
			interactiePanelLaunchState.put("stappen",new Boolean(stappen));
			interactiePanelLaunchState.put("soortHerleiding",new Integer(soortHerleiding));
			interactiePanelLaunchState.put("puntenGelijkwaardig",new Integer(puntenGelijkwaardig));
			interactiePanelLaunchState.put("puntenHerleiding",new Integer(puntenHerleiding));
			interactiePanelLaunchState.put("puntenExact",new Integer(puntenExact));
			interactiePanelLaunchState.put("puntenSignificant",new Integer(puntenSignificant));
			interactiePanelLaunchState.put("formuleToolBijFocus",new Boolean(formuleToolBijFocus));
			interactiePanelLaunchState.put("scoreMax",new Integer(scoreMax));
			if(answerModels!=null)interactiePanelLaunchState.put("answerModels",answerModels);
			interactiePanelLaunchState.put("hasFeedback",new Boolean(hasFeedback));
			interactiePanelLaunchState.put("feedbackSize",new Boolean(feedbackSize));
			interactiePanelLaunchState.put("vormString",vormString);
			interactiePanelLaunchState.put("subKnop",new Boolean(subKnop));
			interactiePanelLaunchState.put("subKnopExtra",new Boolean(subKnopExtra));
			interactiePanelLaunchState.put("rmKnop",new Boolean(rmKnop));
			interactiePanelLaunchState.put("check",new Boolean(check));
			interactiePanelLaunchState.put("teltMee",new Boolean(teltMee));
			interactiePanelLaunchState.put("logOption",new Boolean(logOption));
			interactiePanelLaunchState.put("logID",logID);
			interactiePanelLaunchState.put("logIDLabel",logIDLabel);
			interactiePanelLaunchState.put("eqTestValueMin",new Double(eqTestValueMin));
			interactiePanelLaunchState.put("eqTestValueMax",new Double(eqTestValueMax));
			interactiePanelLaunchState.put("aantalDecRm",new Integer(aantalDecRm));
			interactiePanelLaunchState.put("uitw",new Boolean(uitw));
			interactiePanelLaunchState.put("eigenOpdr",new Boolean(eigenOpdr));
			interactiePanelLaunchState.put("boxMetRand",new Boolean(boxMetRand));
			interactiePanelLaunchState.put("scoreCumulatief",new Boolean(scoreCumulatief));
			interactiePanelLaunchState.put("tips",new Boolean(tips));
			if(tips){
				interactiePanelLaunchState.put("ideasInstellingen",ideasInstellingen);
	        }
	        if(logObjectives!=null)
	        {	interactiePanelLaunchState.put("logObjectives",logObjectives);
	        	interactiePanelLaunchState.put("scoreMaxObjectives",scoreMaxObjectives);
	        	try {
	        	  interactiePanelLaunchState.put(Constants.OBJECTIVES, smObjectives);
	        	  interactiePanelLaunchState.put(Constants.DESELECTIONS, smDeselections);
	        	} catch(Exception e) {}
	        }
	        interactiePanelLaunchState.put("antwoordSubStrings",antwoordSubStrings);
            interactiePanelLaunchState.put("antwoordFuncStrings",antwoordFuncStrings);
            
			
		return interactiePanelLaunchState;
	}
	public void destroy()
	{	
		
	}
		
	public void wis()
	{
	}
	public void zetMode(int mode)
	{
	}
    public void stop()
    {
	}
    public void start()
    {	antwoordvak.setNewScrollSize();
	}
    
	
	
	/*public void textValueChanged(TextEvent e)
	{	int puntenGelijkwaardig = 0;
		int puntenHerleiding = 0;
		int puntenExact = 0;
		int puntenEindOplossing = 0;
		
		if(e.getSource()==gelijkwaardigPV)
		{	try
			{	puntenGelijkwaardig = Integer.parseInt(gelijkwaardigPV.getText());
				this.puntenGelijkwaardig = puntenGelijkwaardig;
			}	
			catch(Exception ex)
			{	}
		}
		if(e.getSource()==herleidingPV)
		{	try
			{	puntenHerleiding = Integer.parseInt(herleidingPV.getText());
				this.puntenHerleiding = puntenHerleiding;
			}	
			catch(Exception ex)
			{	}
		}
		if(e.getSource()==exactPV)
		{	try
			{	puntenExact = Integer.parseInt(exactPV.getText());
				this.puntenExact = puntenExact;
			}	
			catch(Exception ex)
			{	}
		}
		if(e.getSource()==eindOplossingPV)
		{	try
			{	puntenEindOplossing = Integer.parseInt(eindOplossingPV.getText());
				this.puntenEindOplossing = puntenEindOplossing;
			}	
			catch(Exception ex)
			{	}
		}
		boolean b = false;
		if(vergelijking)b = this.puntenGelijkwaardig + this.puntenEindOplossing + this.puntenExact == 10;
		else b = this.puntenGelijkwaardig + this.puntenHerleiding + this.puntenExact == 10;
		 
		//checkTotaalLabel.setVisible(!b);
	}*/
	
    public void maakStartPopupFrame()
	{	
		startEditorPopupFrame = DialogFacade.newInstance(this, "");
		
		
		startEditorPopupFrame.getContentPane().setLayout(null);
		startEditorPopupFrame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e)
			{   startEditor.produceAction("verklein");
				startEditor.setEnlarged(false);
			}
		});
		startEditorPopupFrame.addComponentListener(new ComponentAdapter(){
			public void componentResized(ComponentEvent e)
			{   int x = 0;
				int y = 0;
				int b = startEditorPopupFrame.getSize().width - startEditorPopupFrame.getInsets().left - startEditorPopupFrame.getInsets().right;
				int h = startEditorPopupFrame.getSize().height - startEditorPopupFrame.getInsets().top - startEditorPopupFrame.getInsets().bottom;
				startEditor.setBounds(x,y,b,h);
			}
		});
	}
    
    public void maakVormPopupFrame()
	{
		vormEditorPopupFrame = DialogFacade.newInstance(this, "");
		
		
		vormEditorPopupFrame.getContentPane().setLayout(null);
		vormEditorPopupFrame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e)
			{   vormEditor.produceAction("verklein");
				vormEditor.setEnlarged(false);
			}
		});
		vormEditorPopupFrame.addComponentListener(new ComponentAdapter(){
			public void componentResized(ComponentEvent e)
			{   int x = 0;
				int y = 0;
				int b = vormEditorPopupFrame.getSize().width - vormEditorPopupFrame.getInsets().left - vormEditorPopupFrame.getInsets().right;
				int h = vormEditorPopupFrame.getSize().height - vormEditorPopupFrame.getInsets().top - vormEditorPopupFrame.getInsets().bottom;
				vormEditor.setBounds(x,y,b,h);
			}
		});
	}
    
    public void maakantwoordSubstitutiesFrame()
   	{
    	DialogFacade substitutiesVakPopupFrame = DialogFacade.newInstance(this,  WiskOpdr.rb.getString("titelDefVariabelen"));
    	substitutiesVakPopupFrame.getContentPane().setLayout(null);
    	substitutiesVakPopupFrame.getContentPane().add(this.antwoordSubstitutiesVak);
    	substitutiesVakPopupFrame.addWindowListener(new WindowAdapter(){
   			public void windowClosing(WindowEvent e)
   			{   substitutiesVakPopupFrame.dispose();
   			}
   		});
    	substitutiesVakPopupFrame.addComponentListener(new ComponentAdapter(){
			public void componentResized(ComponentEvent e)
			{   int x = 0;
				int y = 0;
				int b = substitutiesVakPopupFrame.getSize().width - substitutiesVakPopupFrame.getInsets().left - substitutiesVakPopupFrame.getInsets().right;
				int h = substitutiesVakPopupFrame.getSize().height - substitutiesVakPopupFrame.getInsets().top - substitutiesVakPopupFrame.getInsets().bottom;
				antwoordSubstitutiesVak.setBounds(x,y,b,h);
			}
		});
    	substitutiesVakPopupFrame.setVisible(true); 
    	Dimension screenSize = WiskOpdr.applet.getToolkit().getScreenSize();
		int x = substitutiesButton.getLocationOnScreen().x + Math.min(0,screenSize.width - (getLocationOnScreen().x + 350));
		int y = substitutiesButton.getLocationOnScreen().y + Math.min(0,screenSize.height - (getLocationOnScreen().y + 430));
		substitutiesVakPopupFrame.setVisible(true);
		substitutiesVakPopupFrame.pack();
		substitutiesVakPopupFrame.setSize(400,400);
		substitutiesVakPopupFrame.setLocation(x,y);
   	}
    
    public void maakantwoordFunctiesFrame()
   	{
    	DialogFacade functiesVakPopupFrame = DialogFacade.newInstance(this, WiskOpdr.rb.getString("titelDefFuncties"));
    	functiesVakPopupFrame.getContentPane().setLayout(null);
    	functiesVakPopupFrame.getContentPane().add(this.antwoordFunctiesVak);
    	functiesVakPopupFrame.addWindowListener(new WindowAdapter(){
   			public void windowClosing(WindowEvent e)
   			{   functiesVakPopupFrame.dispose();
   			}
   		});
    	functiesVakPopupFrame.addComponentListener(new ComponentAdapter(){
			public void componentResized(ComponentEvent e)
			{   int x = 0;
				int y = 0;
				int b = functiesVakPopupFrame.getSize().width - functiesVakPopupFrame.getInsets().left - functiesVakPopupFrame.getInsets().right;
				int h = functiesVakPopupFrame.getSize().height - functiesVakPopupFrame.getInsets().top - functiesVakPopupFrame.getInsets().bottom;
				antwoordFunctiesVak.setBounds(x,y,b,h);
			}
		});
    	functiesVakPopupFrame.setVisible(true); 
    	Dimension screenSize = WiskOpdr.applet.getToolkit().getScreenSize();
		int x = functiesButton.getLocationOnScreen().x + Math.min(0,screenSize.width - (getLocationOnScreen().x + 350));
		int y = functiesButton.getLocationOnScreen().y + Math.min(0,screenSize.height - (getLocationOnScreen().y + 430));
		functiesVakPopupFrame.setVisible(true);
		functiesVakPopupFrame.pack();
		functiesVakPopupFrame.setSize(400,400);
		functiesVakPopupFrame.setLocation(x,y);
   	}
    
    public void maakFeedbackEditorPopupFrame()
    {
        feedbackEditorPopupFrame = DialogFacade.newInstance(this, "");
        
        
        feedbackEditorPopupFrame.getContentPane().setLayout(null);
        feedbackEditorPopupFrame.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e)
            {   feedbackEditor.produceAction("verklein");
                feedbackEditor.setEnlarged(false);
            }
        });
        feedbackEditorPopupFrame.addComponentListener(new ComponentAdapter(){
            public void componentResized(ComponentEvent e)
            {   int x = 0;
                int y = 0;
                //int b = feedbackEditor.getEnlargedWidth() + feedbackEditorPopupFrame.getInsets().left + feedbackEditorPopupFrame.getInsets().right;
                //int h = feedbackEditor.getEnlargedHeight() + feedbackEditorPopupFrame.getInsets().top + feedbackEditorPopupFrame.getInsets().bottom;
                //feedbackEditor.setLocation(0,0);
                //feedbackEditor.setEnlargedSize();
                //feedbackEditorPopupFrame.setSize(b,h);
                
                int b = feedbackEditorPopupFrame.getSize().width - feedbackEditorPopupFrame.getInsets().left - feedbackEditorPopupFrame.getInsets().right;
                int h = feedbackEditorPopupFrame.getSize().height - feedbackEditorPopupFrame.getInsets().top - feedbackEditorPopupFrame.getInsets().bottom;
                feedbackEditor.setEnlargedWidth(b);
                feedbackEditor.setEnlargedHeight(h);
                if(feedbackEditor.isEnlarged())feedbackEditor.setBounds(x,y,b,h);
                
            }
        });
    }
    
    
	public void actionPerformed(ActionEvent e)
	{	
		if(e.getSource() instanceof HelpButton)
		{
			OpdrNavStructEdit.helpBrowser.loadURL(((HelpButton)e.getSource()).getURL());
		}
		else if(e.getSource() == tabbladTab)
		{	int nr = Integer.parseInt(e.getActionCommand())-1;
			if(answerModelNr != nr) 
			{
				getAnswerModel();
				answerModelNr = nr;
				setAnswerModel();
				tabPositieKnop.setLocation(246+25*answerModelNr+5 ,0);
			}
			
		}
		else if(e.getSource() == tabPositieKnop)
		{	if(e.getActionCommand().equals("plus") && answerModelNr<aantalAnswerModels-1) 
			{	resAnswerModel = new Hashtable();
				fillAnswerModel(resAnswerModel);
				answerModels[answerModelNr] = answerModels[answerModelNr+1];
				answerModels[answerModelNr+1] = resAnswerModel;
				answerModelNr++;
				tabbladTab.setSelected(answerModelNr+1);
				tabPositieKnop.setLocation(246+25*answerModelNr+5 ,0);
			}
			if(e.getActionCommand().equals("min") && answerModelNr>0) 
			{	resAnswerModel = new Hashtable();
				fillAnswerModel(resAnswerModel);
				answerModels[answerModelNr] = answerModels[answerModelNr-1];
				answerModels[answerModelNr-1] = resAnswerModel;
				answerModelNr--;
				tabbladTab.setSelected(answerModelNr+1);
				tabPositieKnop.setLocation(246+25*answerModelNr+5 ,0);
			}
			
		}
		else if(e.getSource() == aantalTabsKnop)
		{	if(e.getActionCommand().equals("min") && aantalAnswerModels>1)
			{	//remove(opdrContainers[activiteitNr][aantalOpdrachten[activiteitNr]-1]);
				//opdrContainers[activiteitNr][aantalOpdrachten[activiteitNr]-1] = null;
				aantalAnswerModels--;
				if(answerModelNr>aantalAnswerModels-1) answerModelNr--;
				setAnswerModel();
				aantalTabsKnop.setLocation(250+25*aantalAnswerModels+5 ,24);
				antwoordEditorPanel.remove(tabbladTab);
				tabbladTab = new OpdrachtNrRij(aantalAnswerModels, 250,20);
				tabbladTab.setTab(true);
				tabbladTab.setScoresVisible(false);
				tabbladTab.setSize(tabbladTab.getSize().width, 23);
				tabbladTab.addActionListener(this);
				tabbladTab.setBackground(new Color(210,210,210));
				tabbladTab.setSelected(answerModelNr+1);
				antwoordEditorPanel.add(tabbladTab,0);
				Hashtable[] answerModelsNew = new Hashtable[aantalAnswerModels];
				for(int i=0 ; i<aantalAnswerModels ; i++)
				{	answerModelsNew[i] = answerModels[i];
				}
				answerModels = answerModelsNew;
				repaint();
				
			}
			if(e.getActionCommand().equals("plus") && aantalAnswerModels<20)
			{	aantalAnswerModels++;
				aantalTabsKnop.setLocation(250+25*aantalAnswerModels+5 ,24);
				antwoordEditorPanel.remove(tabbladTab);
				tabbladTab = new OpdrachtNrRij(aantalAnswerModels, 250,20);
				tabbladTab.setTab(true);
				tabbladTab.setScoresVisible(false);
				tabbladTab.setSize(tabbladTab.getSize().width, 23);
				tabbladTab.addActionListener(this);
				tabbladTab.setBackground(new Color(210,210,210));
				tabbladTab.setSelected(answerModelNr+1);
				antwoordEditorPanel.add(tabbladTab,0);
				Hashtable[] answerModelsNew = new Hashtable[aantalAnswerModels];
				for(int i=0 ; i<aantalAnswerModels-1 ; i++)
				{	answerModelsNew[i] = answerModels[i];
				}
				answerModels = answerModelsNew;
				repaint();
			}
			antwoordEditorPanel.setPreferredSize(new Dimension(Math.max(250+25*aantalAnswerModels+40,450),140));
			((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)mainPanel)).pack();
		}
		else if(e.getSource()==feedbackCB)
		{	setFeedbackOption(feedbackCB.isSelected());
	 		((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)mainPanel)).pack();
		}
		else if(e.getSource()==feedbackSizeCB)
		{	feedbackEditor.setResizable(feedbackSizeCB.isSelected());
			
		}
		else if(e.getSource()==contextVarCB)
        {   contextBox.setVisible(contextVarCB.isSelected());
        	((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)mainPanel)).packWidth();
        }
		else if(e.getSource()==antwoordCheckCB)
		{	antwoordCheck = antwoordCheckCB.isSelected();
			
		}
		else if(e.getSource()==subKnopCB)
	    {   subKnop = subKnopCB.isSelected();
	    	subKnopExtraCB.setVisible(subKnop);
	    	if(!subKnop)subKnopExtraCB.setSelected(subKnop);
	    	((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)mainPanel)).packWidth();

	    }
		else if(e.getSource()==subKnopExtraCB)
	    {   subKnopExtra = subKnopExtraCB.isSelected();
	    }
		else if(e.getSource()==gelijkwaardigCB)
		{	gelijkwaardig = gelijkwaardigCB.isSelected();
			if(!hasFeedback && answerModelNr==0)
			if(!hasFeedback && answerModelNr==0) {
            	gelijkwaardigPV.setVisible(gelijkwaardig);
            	verificatieBox.validate();
            }
			
		}
		else if(e.getSource()==herleidingCB)
		{	boolean b = herleidingCB.isSelected();
			herleiding = b; //herleiding wordt gebruikt voor vormen en moet op false blijven staan
			if(!hasFeedback && answerModelNr==0)herleidingPV.setVisible(b);
			herleidingsKeuze.setVisible(b);
			vormEditor.setVisible(b);
			vormBox.setVisible(b);
			if(!b)
			{	herleidingPV.setText("0");
				puntenHerleiding = 0;
			}
			((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)mainPanel)).pack();
			
		}
		else if(e.getSource()==exactCB)
		{	boolean b = exactCB.isSelected();
			exact = b;
			if(!hasFeedback && answerModelNr==0) {
            	exactPV.setVisible(b);
            	verificatieBox.validate();
            }
			if(b)
			{	gelijkwaardigPV.setText("0");
				exactPV.setText("10");
				
				puntenGelijkwaardig = 0;
				puntenExact = 10;
			}
			else
			{	
				gelijkwaardigPV.setText("0");
				gelijkwaardigPV.setText("10");
				exactPV.setText("0");
			
				puntenGelijkwaardig = 10;
				puntenExact = 0;
			}	
		}
		else if(e.getSource()==significantCB)
		{
			boolean b = significantCB.isSelected();
			significant = b;
			if(!hasFeedback && answerModelNr==0)
			if(!hasFeedback && answerModelNr==0) {
				significantPV.setVisible(b);
				significantPV.validate();
            }
		}
		
		else if(e.getSource()==stappenCB)
		{	boolean b = stappenCB.isSelected();
			stappen = b;
		}
		else if(e.getSource()==formuleToolBijFocusCB)
		{	boolean b = formuleToolBijFocusCB.isSelected();
			formuleToolBijFocus = b;
		}
		else if(e.getSource() == antwoordSubstitutiesVak)
        {  	remove(antwoordSubstitutiesVak);
        	repaint();
        }
        else if(e.getSource() == substitutiesButton)
        {  	//add(antwoordSubstitutiesVak,0);
        	//repaint();
        	maakantwoordSubstitutiesFrame();
        }
        else if(e.getSource() == antwoordFunctiesVak)
        {  	remove(antwoordFunctiesVak);
        	repaint();
        }
        else if(e.getSource() == functiesButton)
        {  	//add(antwoordFunctiesVak,0);
        	//repaint();
        	maakantwoordFunctiesFrame();
        }
		else if(e.getSource()==herleidingsKeuze)
		{	soortHerleiding = herleidingsKeuze.getSelectedIndex();
		}
		else if(e.getSource()==logCB)
	    {   logIDField.setVisible(logCB.isSelected());
	    	logIDLabelField.setVisible(logCB.isSelected());
	    	logIDLabelLabel.setVisible(logCB.isSelected());
	    	//logObjectivesButton.setVisible(logCB.isSelected());
	    }
		else if(e.getSource()==uitwCB)
	    {   rmKnopCB.setVisible(uitwCB.isSelected()); 
	    	aantalDecRmField.setVisible(uitwCB.isSelected() && rmKnopCB.isSelected());
	    	aantalDecRmLabel.setVisible(uitwCB.isSelected() && rmKnopCB.isSelected());
		    //startLabel.setVisible(uitwCB.isSelected());
			//startEditor.setVisible(uitwCB.isSelected());
	    	((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)mainPanel)).packWidth();

	    }
		else if(e.getSource()==rmKnopCB)
	    {   aantalDecRmField.setVisible(rmKnopCB.isSelected());
	    	aantalDecRmLabel.setVisible(rmKnopCB.isSelected());
	    	((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)mainPanel)).packWidth();

 	    }
		else if(e.getSource()==startEditor)
		{	if(e.getActionCommand().equals("vergroot"))
			{	if(startEditorPopupFrame==null)	maakStartPopupFrame();
				Dimension screenSize = WiskOpdr.applet.getToolkit().getScreenSize();
				int x = startEditor.getLocationOnScreen().x + Math.min(0,screenSize.width - (getLocationOnScreen().x + 500));
				int y = startEditor.getLocationOnScreen().y + Math.min(0,screenSize.height - (getLocationOnScreen().y + 400));
				startEditorPopupFrame.setVisible(true);
				//startLabel.setVisible(false);
				startEditorPopupFrame.getContentPane().add(startEditor);
				startEditorPopupFrame.pack();
				startEditorPopupFrame.setSize(500,400);
				startEditorPopupFrame.setLocation(x,y);
			}
			if(e.getActionCommand().equals("verklein"))
			{	startEditorPopupFrame.setVisible(false);
				//startLabel.setVisible(true);
				startEditor.setBounds(startEditorPanel.getBounds());
		        //startLabel.setBounds(5,30,470,20);
				startEditorPanel.add(startEditor);
				startEditorPopupFrame.dispose();
			}
			revalidate();
            repaint();
		}
        else if(e.getSource()==vormEditor)
		{	if(e.getActionCommand().equals("vergroot"))
			{	if(vormEditorPopupFrame==null)	maakVormPopupFrame();
				Dimension screenSize = WiskOpdr.applet.getToolkit().getScreenSize();
				int x = vormEditor.getLocationOnScreen().x + Math.min(0,screenSize.width - (getLocationOnScreen().x + 500));
				int y = vormEditor.getLocationOnScreen().y + Math.min(0,screenSize.height - (getLocationOnScreen().y + 400));
				vormEditorPopupFrame.setVisible(true);
				vormEditorPopupFrame.getContentPane().add(vormEditor);
				vormEditorPopupFrame.pack();
				vormEditorPopupFrame.setSize(500,400);
				vormEditorPopupFrame.setLocation(x,y);
			}
			if(e.getActionCommand().equals("verklein"))
			{	vormEditorPopupFrame.setVisible(false);
				vormEditor.setBounds(vormEditorPanel.getBounds());
		        vormEditorPanel.add(vormEditor);
				vormEditorPopupFrame.dispose();
			}
			revalidate();
            repaint();
		}
        else if(e.getSource()==feedbackEditor)
        {   if(e.getActionCommand().equals("vergroot"))
            {   if(feedbackEditorPopupFrame==null)  maakFeedbackEditorPopupFrame();
                Dimension screenSize = WiskOpdr.applet.getToolkit().getScreenSize();
                int x = feedbackEditor.getLocationOnScreen().x + Math.min(0,screenSize.width - (getLocationOnScreen().x + 500));
                int y = feedbackEditor.getLocationOnScreen().y + Math.min(0,screenSize.height - (getLocationOnScreen().y + 400));
                int eWidth = feedbackEditor.getEnlargedWidth();
                int eHeight = feedbackEditor.getEnlargedHeight();
                eWidth = eWidth==TekstEditor.defaultEnlargedWidth ? 195 : eWidth;
                eHeight = eHeight==TekstEditor.defaultEnlargedHeigth ? 20 : eHeight;
                int b = eWidth + feedbackEditorPopupFrame.getInsets().left + feedbackEditorPopupFrame.getInsets().right;
                int h = eHeight + feedbackEditorPopupFrame.getInsets().top + feedbackEditorPopupFrame.getInsets().bottom;
                
                feedbackEditorPopupFrame.setVisible(true);
                feedbackEditorPopupFrame.getContentPane().add(feedbackEditor);
                feedbackEditorPopupFrame.pack();
                feedbackEditorPopupFrame.setSize(b,h);
                feedbackEditorPopupFrame.setLocation(x,y);
            }
            if(e.getActionCommand().equals("verklein"))
            {	int b = feedbackEditorPopupFrame.getSize().width;// - feedbackEditorPopupFrame.getInsets().left - feedbackEditorPopupFrame.getInsets().right;
                int h = feedbackEditorPopupFrame.getSize().height;// - feedbackEditorPopupFrame.getInsets().top - feedbackEditorPopupFrame.getInsets().bottom;
                feedbackEditor.setEnlargedWidth(b);
                feedbackEditor.setEnlargedHeight(h);
            	feedbackEditorPopupFrame.setVisible(false);
                feedbackEditor.setBounds(5,350,300,160);
                add(feedbackEditor);
                feedbackEditorPopupFrame.dispose();
            }
            revalidate();
            repaint();
        }
        else if(e.getSource()==tipsCB)
		{	tips = tipsCB.isSelected();
			ideasButton.setVisible(tips);
		}
        else if(e.getSource()==scoreCumulatiefCB)
        {	updateFeedbackTitelLabel();
        }
        else
		{
			int puntenGelijkwaardig = 0;
			int puntenHerleiding = 0;
			int puntenExact = 0;
			int puntenSignificant = 0;
			int puntenEindOplossing = 0;
			
			if(e.getSource()==gelijkwaardigPV)
			{	try
				{	puntenGelijkwaardig = Integer.parseInt(gelijkwaardigPV.getText());
					this.puntenGelijkwaardig = puntenGelijkwaardig;
				}	
				catch(Exception ex)
				{	}
			}
			if(e.getSource()==herleidingPV)
			{	try
				{	puntenHerleiding = Integer.parseInt(herleidingPV.getText());
					this.puntenHerleiding = puntenHerleiding;
				}	
				catch(Exception ex)
				{	}
			}
			if(e.getSource()==exactPV)
			{	try
				{	puntenExact = Integer.parseInt(exactPV.getText());
					this.puntenExact = puntenExact;
				}	
				catch(Exception ex)
				{	}
			}
			if(e.getSource()==significantPV)
			{	try
				{	puntenSignificant = Integer.parseInt(significantPV.getText());
					this.puntenSignificant = puntenSignificant;
				}	
				catch(Exception ex)
				{	}
			}
			
			boolean b = false;
			b = this.puntenGelijkwaardig + this.puntenHerleiding + this.puntenExact == 10;
			 
		}
	}
	
	
	public void zetTekstVak(boolean b)
	{	rekenVakCB.setVisible(b);
	}
	
	public void setFeedbackOption(boolean b)
	{
		hasFeedback = b;
		if(!b) logMisconceptionsButton.setVisible(false);
		tabbladTab.setVisible(b);
		feedbackEditor.setVisible(b);
		if(feedbackBox!=null)
	          feedbackBox.setVisible(b);
		feedbackSizeCB.setVisible(b);
		//feedbackLabel.setVisible(b);
		aantalTabsKnop.setVisible(b);
		tabPositieKnop.setVisible(b);
		feedbackPV.setVisible(b);
		goedFoutIP.setVisible(b);
		if(scoringBox!=null)
			scoringBox.setVisible(b);
		titleVerificatieScoreLabel.setVisible(!b);
		puntenLabel.setVisible(!b);
		gelijkwaardigPV.setVisible(!b);
		if(b || herleiding) herleidingPV.setVisible(!b);
		if(b || exact) exactPV.setVisible(!b);
		if(b || significant && significantieAan) significantPV.setVisible(!b);
		
		if(!b)
        		scoreCumulatiefCB.setSelected(false);
		
		answerModelNr = 0;
		tabbladTab.setSelected(answerModelNr+1);
		if(b)setAnswerModel();
	}
	
	public void setVisibleAntwoordModel(boolean b)
	{
		
	}
	
	/*public void itemStateChanged(ItemEvent e)
	{	if(e.getSource()==feedbackCB)
		{	setFeedbackOption(feedbackCB.isSelected());
			
		}
		if(e.getSource()==vergelijkingCB)
		{	boolean b = vergelijkingCB.isSelected();
			zetVergelijkingsMode(b);
		}
	
		if(e.getSource()==gelijkwaardigCB)
		{	gelijkwaardig = gelijkwaardigCB.isSelected();
			if(!hasFeedback && answerModelNr==0)gelijkwaardigPV.setVisible(gelijkwaardig);
			
		}
		if(e.getSource()==herleidingCB)
		{	boolean b = herleidingCB.isSelected();
			herleiding = b; //herleiding wordt gebruikt voor vormen en moet op false blijven staan
			if(!hasFeedback && answerModelNr==0)herleidingPV.setVisible(b);
			herleidingsKeuze.setVisible(b);
			vormEditor.setVisible(b);
			if(!b)
			{	herleidingPV.setText("0");
				puntenHerleiding = 0;
			}
			
		}
		if(e.getSource()==exactCB)
		{	boolean b = exactCB.isSelected();
			exact = b;
			if(!hasFeedback && answerModelNr==0)exactPV.setVisible(b);
			
			if(b)
			{	eindOplossingNodig = true;
				eindOplossingCB.setSelected(true);
				if(vergelijking)eindOplossingPV.setVisible(true);
				eindOplossingCB.setSelected(true);
				
				
				gelijkwaardigPV.setText("0");
				eindOplossingPV.setText("0");
				exactPV.setText("10");
				
				puntenEindOplossing = 0;
				puntenGelijkwaardig = 0;
				puntenExact = 10;
			}
			else
			{	
				gelijkwaardigPV.setText("0");
				if(vergelijking)eindOplossingPV.setText("10");
				else gelijkwaardigPV.setText("10");
				exactPV.setText("0");
			
				puntenGelijkwaardig = 0;
				puntenEindOplossing = 10;
				puntenExact = 0;
			}	
		}
		if(e.getSource()==eindOplossingCB)
		{	boolean b = eindOplossingCB.isSelected();
			eindOplossingNodig = b;
			eindOplossingPV.setVisible(b);
			if(b)
			{	gelijkwaardigPV.setText("0");
				eindOplossingPV.setText("10");
				exactPV.setText("0");
				
				puntenGelijkwaardig = 0;
				puntenEindOplossing = 10;
				puntenExact = 0;
			}
			else
			{	exactCB.setSelected(false);
				exactPV.setVisible(false);
				
				gelijkwaardigPV.setText("10");
				eindOplossingPV.setText("0");
				exactPV.setText("0");
				
				puntenGelijkwaardig = 10;
				puntenEindOplossing = 0;
				puntenExact = 0;
			}
		}
		if(e.getSource()==stappenCB)
		{	boolean b = stappenCB.isSelected();
			stappen = b;
		}
		if(e.getSource()==bewerkingKnoppenCB)
		{	boolean b = bewerkingKnoppenCB.isSelected();
			bewerkingKnoppen = b;
		}
		if(e.getSource()==abcKnopCB)
		{	boolean b = abcKnopCB.isSelected();
			abcKnop = b;
		}
		if(e.getSource()==subKnopCB)
		{	boolean b = subKnopCB.isSelected();
			subKnop = b;
		}
		if(e.getSource()==formuleToolBijFocusCB)
		{	boolean b = formuleToolBijFocusCB.isSelected();
			formuleToolBijFocus = b;
		}
		if(e.getSource()==herleidingsKeuze)
		{	soortHerleiding = herleidingsKeuze.getSelectedIndex();
		}
		
	}*/
	
	
	public void zetTabletUser(FormuleVakHouder formuleVakHouder)
	{	if(tablet==null) return;
		tablet.zetFormuleVakHouder(formuleVakHouder);
		tabletUser = formuleVakHouder;
		
	}
	
	public void zetTablet(FormuleVakHouder formuleVakHouder, int x, int y)
	{	if(tablet==null) 
		{	tablet = new Tablet(formuleVakHouder);
			tablet.setLocation(x,y);
			
		}
		tablet.zetFormuleVakHouder(formuleVakHouder);
		tabletUser = formuleVakHouder;
		
		
	}
	
	public void addTablet(FormuleVakHouder formuleVakHouder, int x, int y)
	{	if(tablet==null) 
		{	tablet = new Tablet(formuleVakHouder);
			this.setLayer((Component)tablet, JLayeredPane.PALETTE_LAYER.intValue());
			
		}
		if(!tabletAdded)
		{	add(tablet,0);
			tablet.setLocation(x,y);
			tabletAdded = true;
			//resize();
            repaint();
		}
		tablet.zetFormuleVakHouder(formuleVakHouder);
	}
	
	public void removeTablet()
	{	if(tablet==null)return;
        remove(tablet);
        //resize();
        repaint();
		tabletAdded = false;
	}
	
	public Tablet getTablet()
	{	return tablet;
	}
	
	public void mousePressed(MouseEvent e)
	{	if(e.getSource()==gelijkwaardigCB && e.getModifiers()== InputEvent.BUTTON3_MASK || e.isControlDown())
		{	try{
			new Expressie();
			String intervalString = JOptionPane.showInputDialog(this, WiskOpdr.rb.getString("tekstKeuzeTestwaarden") + "[" + Expressie.df.format(eqTestValueMin) + ";" + Expressie.df.format(eqTestValueMax) +"]", WiskOpdr.rb.getString("titelKeuzeTestwaarden"), JOptionPane.QUESTION_MESSAGE);
			intervalString = StringUtils.replaceStr(intervalString, "[", "");
			intervalString = StringUtils.replaceStr(intervalString, "]", "");
			String[] parts = StringUtils.split(intervalString, ";");
			eqTestValueMin = Double.parseDouble(parts[0]);
			eqTestValueMax = Double.parseDouble(parts[1]);
			} catch(Exception ex){}
			
		}
	}
	
	public void mouseClicked(MouseEvent e){;}
	public void mouseReleased(MouseEvent e)
	{	
	}
	public void mouseEntered(MouseEvent e)
	{	
	}
	public void mouseExited(MouseEvent e)
	{	
	}
	
	public void mouseDragged(MouseEvent e)
	{	
	}
	public void mouseMoved(MouseEvent e)
	{	
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
 	//end ActionProducer

 	public class EditorComponentListener implements ComponentListener {

 	      @Override
 	      public void componentResized(ComponentEvent e) {
 	    	  if(e.getSource()==antwoordEditorPanel) {
 	    		  int w = antwoordEditorPanel.getWidth();
 	    		  int h = antwoordEditorPanel.getHeight();
 	    		  antwoordvak.setBounds(0,20,w,h-20);
 	    	  }
 	    	  if(e.getSource()==vormEditorPanel) {
 	    		  int w = vormEditorPanel.getWidth();
 	    		  int h = vormEditorPanel.getHeight();
 	    		  vormEditor.setBounds(0,0,w,h);
 	    	  }
 	    	  if(e.getSource()==startEditorPanel) {
 	    		  int w = startEditorPanel.getWidth();
 	    		  int h = startEditorPanel.getHeight();
 	    		  startEditor.setBounds(0,0,w,h);
 	    	  }
 	        
 	      }
 	     @Override
 	      public void componentMoved(ComponentEvent e) {
 	        // TODO Auto-generated method stub
 	        
 	      }

 	      @Override
 	      public void componentShown(ComponentEvent e) {
 	        // TODO Auto-generated method stub
 	        
 	      }

 	      @Override
 	      public void componentHidden(ComponentEvent e) {
 	        // TODO Auto-generated method stub
 	        
 	      }
 	}
	@Override
	public void showHelpButtons(boolean b) {
		hbCheck.setVisible(b);
    	hbTeltMee.setVisible(b);
    	hbLogID.setVisible(b);
    	hbFeedback.setVisible(b);
    	if(soort==0) hbRekenmach.setVisible(b);
    	if(soort==0)hbSubstituties.setVisible(b);
    	hbContextvar.setVisible(b);
    	if(soort==0)hbEigenOpdr.setVisible(b);
    	if(soort==2)hbFormInvoer.setVisible(b);
    	if(soort==2)hbUitwerking.setVisible(b);
    	hbRand_0.setVisible(b);	
    	hbRand_2.setVisible(b);
    	hbVerificatie.setVisible(b);
    	hbAntwoord_0.setVisible(b);
    	hbAntwoord_2.setVisible(b);
    	hbStartExpressie.setVisible(b);
    	hbScore.setVisible(b);
    	hbFeedbackTitel.setVisible(b);
    	//validate();
     	//((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)mainPanel)).packWidth();
        
	}
	
//	public class HelpButtonAction extends AbstractAction {
//	    public HelpButtonAction(String text, String url) {
//	        super(text, null);
//	        putValue("url", url);
//	    }
//	    public void actionPerformed(ActionEvent e) {
//	    	OpdrNavStructEdit.helpBrowser.loadURL((String)getValue("url"));
//	    }
//	}
}
