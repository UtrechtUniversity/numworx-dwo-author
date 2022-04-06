package fi.wiskopdr;

import java.awt.AWTEventMulticaster;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Hashtable;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import fi.beans.numworxlf.JOptionPane;
import fi.beans.numworxlf.JRadioButton;
import fi.beans.stringutils.StringUtils;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.wiskopdr.AntwoordFormuleVakEditPanel.EditorComponentListener;
import fi.wiskopdr.expressies.Expressie;
import fi.wiskopdr.formuleobjects.FormuleEditor;
import fi.wiskopdr.formuleobjects.FormuleVakHouder;
import fi.wiskopdr.formuleobjects.Tablet;
import fi.wiskopdr.opdrnav.ActKeuzePanel;
import fi.wiskopdr.opdrnav.OpdrNavStructEdit;
import fi.wiskopdr.opdrnav.OpdrachtNrRij;
import fi.wiskopdr.opdrnav.PlusMinKnop;
import fi.wiskopdr.strategievak.FormuleAntwoordManager;
import fi.wiskopdr.strategievak.VergelijkingAntwoordManager;
import fi.wiskopdr.tekstobjects.EditInteractiePanelDialog;
import fi.wiskopdr.tekstobjects.TekstEditor;

public class BerekeningVakEditPanel extends JLayeredPane implements InteractieEditPanel, ActionListener,  MouseListener, MouseMotionListener, HelpButtonPanelIF {
	// Algemene attributen 
    private Font font = new Font("SansSerif",Font.PLAIN,12);//WiskOpdr.tekstFont;
	
    private Tablet tablet;
    private boolean tabletAdded;
    private FormuleVakHouder tabletUser;
   
    // Basis GUI
    private JPanel mainPanel;
    
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
    private Box scoringBox;
    private JTextField  feedbackPV;
    private JCheckBox scoreCumulatiefCB;
    
    // Logging/Nakijken
 	private JLabel titleLoggingLabel;
    private JCheckBox checkCB;
    private JRadioButton checkAutomatischRB;
    private JRadioButton checkDocentRB;
    private JCheckBox teltMeeCB;
    private JTextField scoreTF;
    private JLabel scoreLabel;
    private int scoreMax = 10;
     
    private JCheckBox logCB;
 	private JTextField logIDField;
 	private JTextField logIDLabelField;
 	private JLabel logIDLabelLabel;
 	private ObjectiveChoiceButton logObjectivesButton;
 	private ObjectiveChoiceButton logMisconceptionsButton;
	 
 	// Hulp setting
 	private JLabel titleHulpLabel;
 	private JCheckBox formuleToolBijFocusCB;
    private JCheckBox feedbackCB;
    private JCheckBox  rmKnopCB;
    private JTextField aantalDecRmField;
	private JLabel aantalDecRmLabel;
	 
    private boolean formuleToolBijFocus = true;
    private boolean hasFeedback;
   
    // Opmaak
 	private JLabel titleOpmaakLabel;
 	private JCheckBox boxMetRandCB;
 	private JCheckBox meerregeligCB;
 	private JCheckBox pastHoogteAanCB;
 	
    // contextVars
 	private JCheckBox contextVarCB;
    private JLabel titleContextLabel;
 	private Box contextBox;
 	private JButton substitutiesButton;
    private FormuleEditor antwoordSubstitutiesVak;
    private JButton functiesButton;
    private FormuleEditor antwoordFunctiesVak;
    
   
    private Box antwoordModelBox;
//    private JLabel titleAntwoordLabel;
//    private Box antwoordBox;
//    private JTabbedPane tabbedPane;
//    private JCheckBox formuleAntwoordModelCB;
//    private JCheckBox vergelijkingAntwoordModelCB;
//    private JPanel formuleAntwoordTabblad;
//    private JPanel vergelijkingAntwoordTabblad;
    
    // AntwoordManagers
    private FormuleAntwoordManager formuleAntwoordManager;
    private VergelijkingAntwoordManager vergelijkingAntwoordManager;
     
 	// Helpbuttons
    private static String HELP_0_URL_2 = WiskOpdr.rb.getString("HELP_0_URL_2");
    private static String HELP_0_URL_CHECK = WiskOpdr.rb.getString("HELP_0_URL_CHECK");
    private static String HELP_0_URL_TELTMEE = WiskOpdr.rb.getString("HELP_0_URL_TELTMEE");
    private static String HELP_0_URL_LOGID = WiskOpdr.rb.getString("HELP_0_URL_LOGID");
    
    private static String HELP_0_URL_FEEDBACK = WiskOpdr.rb.getString("HELP_0_URL_FEEDBACK");
    private static String HELP_0_URL_REKENMACHINE = WiskOpdr.rb.getString("HELP_0_URL_REKENMACHINE");
    private static String HELP_0_URL_SUBSTITUTIES = WiskOpdr.rb.getString("HELP_0_URL_SUBSTITUTIES");
    private static String HELP_0_URL_CONTEXTVAR = WiskOpdr.rb.getString("HELP_0_URL_CONTEXTVAR");
    private static String HELP_0_URL_FORMINVOER = WiskOpdr.rb.getString("HELP_0_URL_FORMINVOER");
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
    private HelpButton hbFormInvoer;
    private HelpButton hbRand_0;
    private HelpButton hbRand_2;
    private HelpButton hbVerificatie;
    private HelpButton hbAntwoord_0;
    private HelpButton hbAntwoord_2;
    private HelpButton hbStartExpressie;
    private HelpButton hbScore;
    private HelpButton hbFeedbackTitel;
    
    
	public static void zetSignificantieAan(boolean b)
    {	significantieAan = b;
    }
	
	public String geefHelpURL() {
		return HELP_0_URL_2;
	}
    
	public BerekeningVakEditPanel()
	{	setLayout(new BorderLayout());
		super.setSize(770,520); //voor dwo
		setBackground(Color.white);	
		setOpaque(true);
		addMouseListener(this);
		addMouseMotionListener(this);
		makeGUI();
		
		answerModels = new Hashtable[aantalAnswerModels];
		setFeedbackOption(false);
		
		rmKnopCB.setVisible(true);
		formuleToolBijFocusCB.setVisible(true);
		boxMetRandCB.setVisible(true);
		pastHoogteAanCB.setVisible(false);
			
		hbRekenmach.setVisible(false);
	    	hbSubstituties.setVisible(false);
	    
	}
	
	private void makeGUI() {  
    		// Main
		mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBackground(WiskOpdr.colorGray3);
		//mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
	
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
  	    
    	checkAutomatischRB = new JRadioButton("Automatisch nakijken");
        checkAutomatischRB.addActionListener(this);
        checkDocentRB = new JRadioButton("Nakijken door docent");
        checkDocentRB.addActionListener(this);
        checkAutomatischRB.setSelected(true);
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(checkAutomatischRB);
        buttonGroup.add(checkDocentRB);
        
        scoreLabel = new JLabel("Score");
        scoreLabel.setForeground(WiskOpdr.colorBlue1);
        scoreLabel.setFont(font);
        scoreLabel.setVisible(false);
        
        scoreTF = makeTextField(520,25,60,20,"" + scoreMax,false);
        
  	    checkCB = makeCheckBox(5,5,200,20,WiskOpdr.rb.getString("checkCBLabel"),true,true);
        teltMeeCB = makeCheckBox(225,5,200,20,WiskOpdr.rb.getString("teltMeeCBLabel"),true,true);
        logCB = makeCheckBox(450,5,70,20,WiskOpdr.rb.getString("logCBLabel"),false,true);
        logIDField = makeTextField(520,5,60,20,"0",false);
        logIDField.setPreferredSize(new Dimension(50,22));
        logIDLabelField = makeTextField(520,25,60,20,"",false);
        logIDLabelField.setPreferredSize(new Dimension(50,22));
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
		
        rmKnopCB = makeCheckBox(650,105,150,20,WiskOpdr.rb.getString("rmCBLabel"),true,true);
        aantalDecRmField = makeTextField(670,135,100,20,"10",true);
        aantalDecRmLabel = makeLabel(670,135,100,20,WiskOpdr.rb.getString("rmAantalDecLabel"),true);
        
        formuleToolBijFocusCB = makeCheckBox(530,50,270,20,WiskOpdr.rb.getString("formuleToolCBLabel"),true,true);
		
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
	    	meerregeligCB = makeCheckBox(690,95,80,20,WiskOpdr.rb.getString("meerregelig"),false,true);
	    	pastHoogteAanCB = makeCheckBox(690,120,80,20,WiskOpdr.rb.getString("TVEP_pasAanH"),false,true);
	    	
	    	// HelpKnoppen
	    	hbCheck = makeHelpButton(HELP_0_URL_CHECK);
	    	hbTeltMee = makeHelpButton(HELP_0_URL_TELTMEE);
	    	hbLogID = makeHelpButton(HELP_0_URL_LOGID);
	    	hbFeedback = makeHelpButton(HELP_0_URL_FEEDBACK);
	    	hbRekenmach = makeHelpButton(HELP_0_URL_REKENMACHINE);
	    	hbSubstituties = makeHelpButton(HELP_0_URL_SUBSTITUTIES);
	    	hbContextvar = makeHelpButton(HELP_0_URL_CONTEXTVAR);
	    hbFormInvoer = makeHelpButton(HELP_0_URL_FORMINVOER);
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
	    antwoordEditorPanel.add(hbAntwoord_2,0);
	    	
	    	hbFormInvoer.setVisible(false);
	    
	    	removeAll();
	    plaatsGUI();
	    add(mainPanel);
    }
	
	public void plaatsGUI() {
		//plaats componenten contextbox
		Component[] r21 = {titleContextLabel};
		Component[] r22 = {substitutiesButton};
		Component[] r23 = {functiesButton};
		
		Component[] k2 = {hb(r21),vst(15),hb(r22),vst(10),hb(r23), ra(10,20),vgl()};
		
		Component[] h1 = {vb(k2), hgl()};
		contextBox = hb(h1);
		
		// plaats compoenenten antwoordbox
				Component[] r31 = {ra(0,130), 	antwoordEditorPanel};
				Component[] k3 = {hb(r31)};
				antwoordBox = vb(k3);
				
					
		// plaatsComponenten settingBox
		Component[] r41 = {titleLoggingLabel, 	hgl()};
		Component[] r42 = {checkCB, 			ra(5,0),	hgl(),	hbCheck};
		Component[] r42a = {ra(20,0), checkAutomatischRB,         ra(5,0),    hgl(),  hbCheck};
		Component[] r42b = {ra(20,0), checkDocentRB,           ra(5,0),    hgl(),  hbCheck};
		Component[] r42c = {ra(50,0), scoreLabel, ra(5,0), scoreTF,   hgl()           };
        Component[] r43 = {teltMeeCB, 			ra(5,0),	hgl(),	hbTeltMee};
		Component[] r44 = {logCB, 				ra(5,10), 	logIDField, ra(5,10), logIDLabelLabel, ra(5,10), logIDLabelField, 	ra(5,0),	hgl(),	hbLogID};
		Component[] r45 = {ra(6,0),			logObjectivesButton, hgl()};
		Component[] r46 = {titleHulpLabel, 		hgl()};
		Component[] r47 = {feedbackCB, 			ra(5,0),	hgl(),	hbFeedback};
		Component[] r48 = {formuleToolBijFocusCB, 			ra(5,0),	hgl(),	hbFormInvoer};
		Component[] r410 = {rmKnopCB, 			ra(5,0),	hgl(),	hbRekenmach};
		Component[] r411 = {ra(20,0),			aantalDecRmLabel, 	ra(4,0),	aantalDecRmField,hgl()};
		Component[] r418 = {contextVarCB, 		ra(5,0),	hgl(),	hbContextvar};
		Component[] r421 = {contextBox, 		hgl()};
		Component[] r422 = {titleOpmaakLabel, 	hgl()};
		
		Box settingsBox;
		
		Component[] r424 = {boxMetRandCB, 		ra(5,0),	hgl(),	hbRand_2};
		Component[] r425 = {meerregeligCB, 		ra(5,0),	hgl()};
		Component[] k4 = {hb(r41),vst(5),hb(r42),vst(3),hb(r42a),vst(3),hb(r42b),vst(3),hb(r42c),vst(3),hb(r43),hb(r44),vst(3),hb(r45),vst(20),hb(r46),vst(5),hb(r47),hb(r48),
				hb(r410),hb(r411),hb(r418), vst(20),hb(r421), hb(r422),vst(5),hb(r424),hb(r425), vgl()};
		settingsBox = vb(k4);
				
		settingsBox.setMaximumSize(new Dimension(500,800));
		
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
        
       	Component[] k7 = {hb(r70),vst(5),hb(r71),vgl()};
        	scoringBox = vb(k7);
        
		
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
		
		antwoordModelBox = Box.createVerticalBox();
		Box boxv1 = Box.createVerticalBox();
		boxv1.add(ra(450,0));
		boxv1.add(antwoordModelBox);
		boxh.add(boxv1);
		boxh.add(Box.createHorizontalStrut(20));
		boxh.add(settingsBox);
		
		vormBox.setVisible(false);
		scoringBox.setVisible(false);
		feedbackBox.setVisible(false);
		contextBox.setVisible(false);
		
		Box boxh2 = Box.createHorizontalBox();
		Box boxh3 = Box.createHorizontalBox();
		antwoordModelBox.add(boxh2);
		antwoordModelBox.add(Box.createVerticalStrut(20));
		antwoordModelBox.add(boxh3);
		
		boxh2.add(antwoordBox);
		
		boxh3.add(verificatieBox);
		boxh3.add(Box.createHorizontalGlue());
		boxh3.add(Box.createHorizontalStrut(10));
		boxh3.add(vormBox);
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
		this.puntenFeedback = puntenFeedback;
		
		antwoordvak.geefFormuleVak().vulVak(antwoordString);
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
		if(significantieAan) 
			significantCB.setVisible(true);
		
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
		boolean herleiding = false;
		boolean exact = false;
		boolean significant = false;
		int puntenGelijkwaardig = 10;
		int puntenHerleiding = 0;
		int puntenExact = 0;
		int puntenSignificant = 0;
		boolean formuleToolBijFocus = true;
		Hashtable[] answerModels = null;
		boolean hasFeedback = false;
		boolean feedbackSize = false;
		String vormString = "$f@";
		boolean rmKnop = false;
		boolean check = true;
		boolean checkDocent = true;
		boolean teltMee = true;
		boolean logOption = false;
		String logID = "";
		String logIDLabel = "";
		double eqTestValueMin = 0;
		double eqTestValueMax = 5;
		int aantalDecRm = 10;
		boolean boxMetRand = true;
		boolean meerregelig = false;
        boolean pastHoogteAan = false;
        String[] antwoordSubStrings = null;
        String[] antwoordFuncStrings = null;
        boolean scoreCumulatief = false;
		
		if(interactiePanelLaunchState.containsKey("antwoordString")) antwoordString = (String)interactiePanelLaunchState.get("antwoordString");
		if(interactiePanelLaunchState.containsKey("herleiding")) herleiding = ((Boolean)interactiePanelLaunchState.get("herleiding")).booleanValue();
		if(interactiePanelLaunchState.containsKey("exact")) exact = ((Boolean)interactiePanelLaunchState.get("exact")).booleanValue();
		if(interactiePanelLaunchState.containsKey("significant")) significant = ((Boolean)interactiePanelLaunchState.get("significant")).booleanValue();
		if(interactiePanelLaunchState.containsKey("puntenGelijkwaardig")) puntenGelijkwaardig = ((Integer)interactiePanelLaunchState.get("puntenGelijkwaardig")).intValue();
		if(interactiePanelLaunchState.containsKey("puntenHerleiding")) puntenHerleiding = ((Integer)interactiePanelLaunchState.get("puntenHerleiding")).intValue();
		if(interactiePanelLaunchState.containsKey("puntenExact")) puntenExact = ((Integer)interactiePanelLaunchState.get("puntenExact")).intValue();
		if(interactiePanelLaunchState.containsKey("puntenSignificant")) puntenSignificant = ((Integer)interactiePanelLaunchState.get("puntenSignificant")).intValue();
		if(interactiePanelLaunchState.containsKey("formuleToolBijFocus")) formuleToolBijFocus = ((Boolean)interactiePanelLaunchState.get("formuleToolBijFocus")).booleanValue();
		if(interactiePanelLaunchState.containsKey("answerModels")) answerModels = (Hashtable[])interactiePanelLaunchState.get("answerModels");
		if(interactiePanelLaunchState.containsKey("hasFeedback")) hasFeedback = ((Boolean)interactiePanelLaunchState.get("hasFeedback")).booleanValue();
		if(interactiePanelLaunchState.containsKey("feedbackSize")) feedbackSize = ((Boolean)interactiePanelLaunchState.get("feedbackSize")).booleanValue();
		if(interactiePanelLaunchState.containsKey("vormString")) vormString = (String)interactiePanelLaunchState.get("vormString");
		if(interactiePanelLaunchState.containsKey("rmKnop")) rmKnop = ((Boolean)interactiePanelLaunchState.get("rmKnop")).booleanValue();
		if(interactiePanelLaunchState.containsKey("check")) check = ((Boolean)interactiePanelLaunchState.get("check")).booleanValue();
		if(interactiePanelLaunchState.containsKey("checkDocent")) checkDocent = ((Boolean)interactiePanelLaunchState.get("checkDocent")).booleanValue();
        if(interactiePanelLaunchState.containsKey("teltMee")) teltMee = ((Boolean)interactiePanelLaunchState.get("teltMee")).booleanValue();
		if(interactiePanelLaunchState.containsKey("logOption")) logOption = ((Boolean)interactiePanelLaunchState.get("logOption")).booleanValue();
		if(interactiePanelLaunchState.containsKey("logID")) logID = (String)interactiePanelLaunchState.get("logID");
		if(interactiePanelLaunchState.containsKey("logIDLabel")) logIDLabel = (String)interactiePanelLaunchState.get("logIDLabel");
		if(interactiePanelLaunchState.containsKey("eqTestValueMin")) eqTestValueMin = ((Double)interactiePanelLaunchState.get("eqTestValueMin")).doubleValue();
		if(interactiePanelLaunchState.containsKey("eqTestValueMax")) eqTestValueMax = ((Double)interactiePanelLaunchState.get("eqTestValueMax")).doubleValue();
		if(interactiePanelLaunchState.containsKey("aantalDecRm")) aantalDecRm = ((Integer)interactiePanelLaunchState.get("aantalDecRm")).intValue();
		if(interactiePanelLaunchState.containsKey("boxMetRand")) boxMetRand = ((Boolean)interactiePanelLaunchState.get("boxMetRand")).booleanValue();
		if(interactiePanelLaunchState.containsKey("meerregelig")) meerregelig = ((Boolean)interactiePanelLaunchState.get("meerregelig")).booleanValue();
		if(interactiePanelLaunchState.containsKey("pasAanH")) pastHoogteAan = ((Boolean)interactiePanelLaunchState.get("pasAanH")).booleanValue();
		if(interactiePanelLaunchState.containsKey("scoreCumulatief")) scoreCumulatief = ((Boolean)interactiePanelLaunchState.get("scoreCumulatief")).booleanValue();
		
		if(interactiePanelLaunchState.containsKey("antwoordSubStrings")) antwoordSubStrings = (String[])interactiePanelLaunchState.get("antwoordSubStrings");
        if(interactiePanelLaunchState.containsKey("antwoordFuncStrings")) antwoordFuncStrings = (String[])interactiePanelLaunchState.get("antwoordFuncStrings");
        
		this.herleiding = herleiding;
		this.exact = exact;
		this.significant = significant;
		this.puntenGelijkwaardig = puntenGelijkwaardig;
		this.puntenHerleiding = puntenHerleiding;
		this.puntenExact = puntenExact;
		this.puntenSignificant = puntenSignificant;
		this.formuleToolBijFocus = formuleToolBijFocus;
		
		if(answerModels != null)
		{	this.answerModels = new Hashtable[answerModels.length];
			for(int i=0 ; i<answerModels.length ; i++)
			{	this.answerModels[i] = answerModels[i];
			}
		}
		
		this.eqTestValueMin = eqTestValueMin;
		this.eqTestValueMax = eqTestValueMax;
		
		scoreCumulatiefCB.setSelected(scoreCumulatief);
		
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
                       
		formuleToolBijFocusCB.setSelected(formuleToolBijFocus);
		    
        checkCB.setSelected(check);
        checkDocentRB.setSelected(checkDocent);
        checkAutomatischRB.setSelected(check && !checkDocent);
//        formuleAntwoordModelCB.setVisible(check && !checkDocent);
//        vergelijkingAntwoordModelCB.setVisible(check && !checkDocent);
//        formuleAntwoordModelCB.setSelected(hasFormuleAnswerModel);
//        vergelijkingAntwoordModelCB.setSelected(hasVergelijkingAnswerModel);
        scoreLabel.setVisible(checkCB.isSelected() && checkDocentRB.isSelected());
        scoreTF.setVisible(checkCB.isSelected() && checkDocentRB.isSelected());
        checkAutomatischRB.setVisible(checkCB.isSelected());
        checkDocentRB.setVisible(checkCB.isSelected());
            logObjectivesButton.setVisible(ObjectiveChoiceButton.hasObjectiveChoices() && checkCB.isSelected());
            if(!checkCB.isSelected())
                scoreTF.setText("0");
        antwoordModelBox.setVisible(check && !checkDocent);
        feedbackCB.setVisible(check && !checkDocent);
        contextVarCB.setVisible(check && !checkDocent);
        
        
        teltMeeCB.setSelected(teltMee);
        logCB.setSelected(logOption);
        
        boxMetRandCB.setSelected(boxMetRand);
        meerregeligCB.setSelected(meerregelig);
        pastHoogteAanCB.setSelected(pastHoogteAan);
        rmKnopCB.setVisible(true);
        rmKnopCB.setSelected(rmKnop);
        aantalDecRmField.setVisible(rmKnop);
        aantalDecRmLabel.setVisible(rmKnop);
    
        logIDField.setVisible(logOption);
        logIDLabelField.setVisible(logOption);
        logIDLabelLabel.setVisible(logOption);
        logIDField.setText(logID);
        logIDLabelField.setText(logIDLabel);
        
        logObjectivesButton.setEditState(interactiePanelLaunchState);
       	            
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
		
	}
	
	public Hashtable getEditState()
	{	
		Hashtable interactiePanelLaunchState = new Hashtable();
        
        
			String antwoordString = null;
			boolean herleiding = false;
			boolean exact = false;
			boolean significant = false;
			int puntenGelijkwaardig = 10;
			int puntenHerleiding = 0;
			int puntenExact = 0; 
			int puntenSignificant = 0; 
			int scoreMax = 0;
			int[][] scoreMaxObjectives = null;
			boolean formuleToolBijFocus = true;
			Hashtable[] answerModels;
			boolean hasFeedback;
			boolean feedbackSize;
			String vormString = "$f@";
			boolean subKnop = false;
			boolean subKnopExtra = false;
			boolean rmKnop = false;
			boolean check = true;
			boolean checkDocent = true;
			boolean teltMee = true;
			boolean logOption = false;
			String logID = "";
			String logIDLabel = "";
			boolean[][] logObjectives = null;
			double eqTestValueMin = 0;
			double eqTestValueMax = 5;
			int aantalDecRm = 10;
			boolean boxMetRand = true;
			boolean meerregelig = false;
			boolean pastHoogteAan = false;
			String[] antwoordSubStrings = null;
            String[] antwoordFuncStrings = null;
            boolean scoreCumulatief = false;
			
			getAnswerModel();
			answerModels = this.answerModels;
			if(answerModels!=null)setAnswerModel(answerModels[0]);
			
			antwoordString = antwoordvak.geefFormuleVak().toString();
			String[] vormStrings = vormEditor.geefRegels();
            if(vormStrings.length==1) 
            		vormString = vormEditor.geefFormuleVak().toString();
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
    			System.out.println(functieDelen[0].substring(2));
    		}
			
			check = checkCB.isSelected();
			checkDocent = checkDocentRB.isSelected();
			teltMee = teltMeeCB.isSelected();
			logOption = logCB.isSelected();
			logID = logIDField.getText();
			logIDLabel = logIDLabelField.getText();
			logObjectives = logObjectivesButton.getChoices();
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
			
			rmKnop = rmKnopCB.isSelected();
						
			boxMetRand = boxMetRandCB.isSelected();
			meerregelig = meerregeligCB.isSelected();
			pastHoogteAan = pastHoogteAanCB.isSelected();
			
			eqTestValueMin = this.eqTestValueMin;
			eqTestValueMax = this.eqTestValueMax;
			
			interactiePanelLaunchState.put("antwoordString",antwoordString);
			interactiePanelLaunchState.put("herleiding",new Boolean(herleiding));
			interactiePanelLaunchState.put("exact",new Boolean(exact));
			interactiePanelLaunchState.put("significant",new Boolean(significant));
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
			interactiePanelLaunchState.put("rmKnop",new Boolean(rmKnop));
			interactiePanelLaunchState.put("check",new Boolean(check));
			interactiePanelLaunchState.put("checkDocent",new Boolean(checkDocent));
		    interactiePanelLaunchState.put("teltMee",new Boolean(teltMee));
			interactiePanelLaunchState.put("logOption",new Boolean(logOption));
			interactiePanelLaunchState.put("logID",logID);
			interactiePanelLaunchState.put("logIDLabel",logIDLabel);
			interactiePanelLaunchState.put("eqTestValueMin",new Double(eqTestValueMin));
			interactiePanelLaunchState.put("eqTestValueMax",new Double(eqTestValueMax));
			interactiePanelLaunchState.put("aantalDecRm",new Integer(aantalDecRm));
			interactiePanelLaunchState.put("boxMetRand",new Boolean(boxMetRand));
			interactiePanelLaunchState.put("meerregelig",new Boolean(meerregelig));
			interactiePanelLaunchState.put("pasAanH",new Boolean(pastHoogteAan));
			interactiePanelLaunchState.put("scoreCumulatief",new Boolean(scoreCumulatief));
			
            interactiePanelLaunchState.putAll(logObjectivesButton.getEditState(scoreMax));
                
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
		{	
			int nr = Integer.parseInt(e.getActionCommand())-1;
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
			{	aantalAnswerModels--;
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
		
		else if(e.getSource()==formuleToolBijFocusCB)
		{	boolean b = formuleToolBijFocusCB.isSelected();
			formuleToolBijFocus = b;
		}
		else if(e.getSource() == antwoordSubstitutiesVak)
        {  	remove(antwoordSubstitutiesVak);
        	repaint();
        }
        else if(e.getSource() == substitutiesButton)
        {  	
        		maakantwoordSubstitutiesFrame();
        }
        else if(e.getSource() == antwoordFunctiesVak)
        {  	remove(antwoordFunctiesVak);
        		repaint();
        }
        else if(e.getSource() == functiesButton)
	    {	maakantwoordFunctiesFrame();
        }
		else if(e.getSource()==logCB)
	    {   logIDField.setVisible(logCB.isSelected());
	    		logIDLabelField.setVisible(logCB.isSelected());
	    		logIDLabelLabel.setVisible(logCB.isSelected());
	    }
		else if(e.getSource()==checkCB) {   
          scoreLabel.setVisible(checkCB.isSelected() && checkDocentRB.isSelected());
              scoreTF.setVisible(checkCB.isSelected() && checkDocentRB.isSelected());
              checkAutomatischRB.setVisible(checkCB.isSelected());
              checkDocentRB.setVisible(checkCB.isSelected());
              logObjectivesButton.setVisible(ObjectiveChoiceButton.hasObjectiveChoices() && checkCB.isSelected());
              if(!checkCB.isSelected())
                  scoreTF.setText("0");
              antwoordModelBox.setVisible(checkCB.isSelected() && checkAutomatischRB.isSelected());
              feedbackCB.setVisible(checkCB.isSelected() && checkAutomatischRB.isSelected());
              contextVarCB.setVisible(checkCB.isSelected() && checkAutomatischRB.isSelected());
              pack(); 
        }
        else if(e.getSource()==checkAutomatischRB) { 
            scoreLabel.setVisible(!checkAutomatischRB.isSelected());
            scoreTF.setVisible(!checkAutomatischRB.isSelected());
            antwoordModelBox.setVisible(checkAutomatischRB.isSelected());
            feedbackCB.setVisible(checkAutomatischRB.isSelected());
            contextVarCB.setVisible(checkAutomatischRB.isSelected());
            //formuleAntwoordModelCB.setVisible(checkAutomatischRB.isSelected());
            //vergelijkingAntwoordModelCB.setVisible(checkAutomatischRB.isSelected());
            pack();
        }
        else if(e.getSource()==checkDocentRB) { 
            scoreLabel.setVisible(checkDocentRB.isSelected());
            scoreTF.setVisible(checkDocentRB.isSelected());
            antwoordModelBox.setVisible(checkAutomatischRB.isSelected());
            feedbackCB.setVisible(checkAutomatischRB.isSelected());
            contextVarCB.setVisible(checkAutomatischRB.isSelected());
            //formuleAntwoordModelCB.setVisible(checkAutomatischRB.isSelected());
            //vergelijkingAntwoordModelCB.setVisible(checkAutomatischRB.isSelected());
            pack();
        }
		else if(e.getSource()==rmKnopCB)
	    {   aantalDecRmField.setVisible(rmKnopCB.isSelected());
	    	aantalDecRmLabel.setVisible(rmKnopCB.isSelected());
	    	((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)mainPanel)).packWidth();

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
	public void pack() {
      ((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)mainPanel)).packWidth();
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
		aantalTabsKnop.setVisible(b);
		tabPositieKnop.setVisible(b);
		feedbackPV.setVisible(b);
		goedFoutIP.setVisible(b);
		if(scoringBox!=null)
			scoringBox.setVisible(b);
		titleVerificatieScoreLabel.setVisible(!b);
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
			repaint();
		}
		tablet.zetFormuleVakHouder(formuleVakHouder);
	}
	
	public void removeTablet()
	{	if(tablet==null)return;
        remove(tablet);
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
	    hbRekenmach.setVisible(b);
	    	hbSubstituties.setVisible(b);
	    	hbContextvar.setVisible(b);
	    	hbFormInvoer.setVisible(b);
	    	hbRand_0.setVisible(b);	
	    	hbRand_2.setVisible(b);
	    	hbVerificatie.setVisible(b);
	    	hbAntwoord_0.setVisible(b);
	    	hbAntwoord_2.setVisible(b);
	    	hbStartExpressie.setVisible(b);
	    	hbScore.setVisible(b);
	    	hbFeedbackTitel.setVisible(b);
	}

}
