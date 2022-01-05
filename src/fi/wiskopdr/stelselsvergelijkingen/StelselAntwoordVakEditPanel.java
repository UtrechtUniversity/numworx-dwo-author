package fi.wiskopdr.stelselsvergelijkingen;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.regex.Pattern;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import fi.wiskopdr.DialogFacade;
import fi.wiskopdr.HelpButton;
import fi.wiskopdr.HelpButtonPanelIF;
import fi.wiskopdr.ObjectiveChoiceButton;
import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.WiskOpdrButton;
import fi.wiskopdr.WiskOpdrCheckbox;
import fi.wiskopdr.WiskOpdrTextField;
import fi.wiskopdr.tekstobjects.*;
import fi.wiskopdr.formuleobjects.*;
import fi.wiskopdr.expressies.*;
import fi.wiskopdr.opdrnav.*;
import fi.beans.numworxlf.JLabel;
import fi.beans.numworxlf.JOptionPane;
import fi.beans.stringutils.StringUtils;
import fi.beans.wiskopdrbeans.InteractieEditPanel;


public class StelselAntwoordVakEditPanel extends JLayeredPane implements InteractieEditPanel, ActionListener,  MouseListener, MouseMotionListener, HelpButtonPanelIF
{
	// Algemene attributen 
    private Font font = new Font("SansSerif",Font.PLAIN,12);//WiskOpdr.tekstFont;
    
    private boolean stappen = true;
    private boolean stappenDefault = true;
     
    private Tablet tablet;
    private boolean tabletAdded;
    private FormuleVakHouder tabletUser;
    
    // Basis GUI
    private JPanel mainPanel;
    
	//Start editor
	private FormuleEditor variabelenEditor;
	private JPanel variabelenEditorPanel;
	private JLabel titleVariabelenLabel;
	private Box variabelenBox;
	
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
	   
    //Feedback editor
  	private TekstEditor feedbackEditor;
  	private JCheckBox feedbackSizeCB;
  	private JLabel titleFeedbackLabel;
  	private Box feedbackBox;
  	private DialogFacade feedbackEditorPopupFrame;
    
  	// Verificatie
 	private JLabel titleVerificatieLabel;
 	private Box verificatieBox;
 	private JCheckBox  gelijkwaardigCB, exactCB, significantCB, onafhankelijkCB, eindOplossingCB;
 	private JTextField gelijkwaardigPV, exactPV, significantPV, onafhankelijkPV,eindOplossingPV;
 	
 	private boolean gelijkwaardig = true;
	private boolean eindOplossingNodig = true;
	private boolean onafhankelijkNodig = false;
    private boolean exact;
	private boolean	significant;
	private double eqTestValueMin = 0;
	private double eqTestValueMax = 5;
	 
	private int puntenGelijkwaardig = 0;
	private int puntenEindOplossing = 10;
	private int puntenExact = 0;
	private int puntenSignificant = 0;
	private int puntenFeedback = 0;
	private int puntenOnafhankelijk = 0;
     
	static boolean	significantieAan=false; 
    
	// Score
    private JLabel titleScoreLabel;
    private JLabel scoringLabel; // overbodig?
    private Box scoringBox;
    private JTextField  feedbackPV;
    private ActKeuzePanel goedFoutIP;
    private JLabel puntenLabel, checkTotaalLabel; // overbodig?
    
    // Logging/Nakijken
 	private JLabel titleLoggingLabel;
    private JCheckBox checkCB;
    private JCheckBox teltMeeCB;
     
    private JCheckBox logCB;
 	private JTextField logIDField;
 	private JTextField logIDLabelField;
 	private JLabel logIDLabelLabel;
 	private ObjectiveChoiceButton logObjectivesButton;
     
 	// Hulp setting
 	private JLabel titleHulpLabel;
 	private Box settingsBox;
 	private JCheckBox  stappenCB, contextVarCB;
 	private JCheckBox formuleToolBijFocusCB;
    private JCheckBox feedbackCB;
    
    private boolean formuleToolBijFocus;
    private boolean hasFeedback;
     
    
    // Opmaak
 	private JLabel titleOpmaakLabel;
 	private JCheckBox boxMetRandCB;
 	
 	// contextVars
	private JLabel titleContextLabel;
	private Box contextBox;
	private JButton substitutiesButton;
    private FormuleEditor antwoordSubstitutiesVak;
    private JButton functiesButton;
    private FormuleEditor antwoordFunctiesVak;
    
 // Helpbuttons
    private static String HELP_53_URL = WiskOpdr.rb.getString("HELP_53_URL");
    private static String HELP_53_URL_CHECK = WiskOpdr.rb.getString("HELP_53_URL_CHECK");
    private static String HELP_53_URL_TELTMEE = WiskOpdr.rb.getString("HELP_53_URL_TELTMEE");
    private static String HELP_53_URL_LOGID = WiskOpdr.rb.getString("HELP_53_URL_LOGID");
    private static String HELP_53_URL_REKENVAKZICHTBAAR = WiskOpdr.rb.getString("HELP_53_URL_REKENVAKZICHTBAAR");
    private static String HELP_53_URL_OPLOSSINGREGEL = WiskOpdr.rb.getString("HELP_53_URL_OPLOSSINGREGEL");
    private static String HELP_53_URL_CONTEXTVAR = WiskOpdr.rb.getString("HELP_53_URL_CONTEXTVAR");
    private static String HELP_53_URL_FORMINVOER = WiskOpdr.rb.getString("HELP_53_URL_FORMINVOER");
    private static String HELP_53_URL_RAND = WiskOpdr.rb.getString("HELP_53_URL_RAND");
    private static String HELP_53_URL_VERIFICATIE = WiskOpdr.rb.getString("HELP_53_URL_VERIFICATIE");
    private static String HELP_53_URL_VARIABELEN = WiskOpdr.rb.getString("HELP_53_URL_VARIABELEN");
    private static String HELP_53_URL_ANTWOORD = WiskOpdr.rb.getString("HELP_53_URL_ANTWOORD");
    
    private HelpButton hbCheck;
    private HelpButton hbTeltMee;
    private HelpButton hbLogID;
    private HelpButton hbRekenVakZichtbaar;
    private HelpButton hbOplossingRegel;
    private HelpButton hbContextvar;
    private HelpButton hbFormInvoer;
    private HelpButton hbRand;
    private HelpButton hbVerificatie;
    private HelpButton hbVariabelen;
    private HelpButton hbAntwoord;
    
    private JLabel antwoordLabel, variabelenLabel, feedbackLabel;
	private JCheckBox oplossingenRegelZichtbaarCB, rekenVakZichtbaarCB;
    //private JTextField solveTF;
    
	//private JCheckBox casAntwCB;
	
	
	public static void zetSignificantieAan(boolean b)
	{	significantieAan = b;
	}
    
    public StelselAntwoordVakEditPanel()
    {   setLayout(new BorderLayout());
        super.setSize(770,520); //voor dwo
        setBackground(Color.white); 
        setOpaque(true);
        addMouseListener(this);
        addMouseMotionListener(this);
        
        makeGUI();
        
        answerModels = new Hashtable[aantalAnswerModels];
        
        setFeedbackOption(false);
    }
    
    private void makeGUI() {
    	// Main
    	mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBackground(WiskOpdr.colorGray3);
		//mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
		
		// GUI startBox  
		titleVariabelenLabel = new JLabel(WiskOpdr.rb.getString("variabelenLabel"));
		titleVariabelenLabel.setForeground(WiskOpdr.colorBlue1);
		titleVariabelenLabel.setFont(font.deriveFont(Font.BOLD, 16));
		variabelenLabel = makeLabel(5,30,770,20,WiskOpdr.rb.getString("variabelenLabel"),true);
    	
		variabelenEditor = new FormuleEditor(false);
		variabelenEditor.setHeader(true);
		variabelenEditor.setBounds(0,0,470,110);
		variabelenEditor.setFont(font);
		variabelenEditor.addActionListener(this);
        
		variabelenEditorPanel = new JPanel();
		variabelenEditorPanel.setLayout(null);
		variabelenEditorPanel.add(variabelenEditor);
		variabelenEditorPanel.addComponentListener(new EditorComponentListener());
		variabelenEditorPanel.setPreferredSize(new Dimension(600,110));
		variabelenEditorPanel.setMaximumSize(new Dimension(2870,110));
        
		// GUI antwoordBox
        titleAntwoordLabel = new JLabel(WiskOpdr.rb.getString("FEV_titleAntwoordLabel")+"  "+WiskOpdr.rb.getString("antwoordLabelStelsel"));
    	titleAntwoordLabel.setForeground(WiskOpdr.colorBlue1);
    	titleAntwoordLabel.setFont(font.deriveFont(Font.BOLD, 16));
    	titleAntwoordLabel.setBounds(0,-3,140,20);
    	antwoordLabel = makeLabel(5,160,770,20,WiskOpdr.rb.getString("antwoordLabelStelsel"),true);
    	
        antwoordvak = new FormuleEditor(true);
        antwoordvak.setBounds(0,0,435,150);
        antwoordvak.setFont(font);
        antwoordvak.addActionListener(this);
        
        antwoordEditorPanel = new JPanel();
        antwoordEditorPanel.setLayout(null);
        //antwoordEditorPanel.add(titleAntwoordLabel);
        antwoordEditorPanel.add(antwoordvak);
        antwoordEditorPanel.addComponentListener(new EditorComponentListener());
        antwoordEditorPanel.setPreferredSize(new Dimension(600,150));
        antwoordEditorPanel.setMaximumSize(new Dimension(2835,150));
        
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
		
        //GUI Feedback editor
        titleFeedbackLabel = new JLabel(WiskOpdr.rb.getString("FEV_titleFeedbackLabel"));
    	titleFeedbackLabel.setForeground(WiskOpdr.colorBlue1);
    	titleFeedbackLabel.setFont(font.deriveFont(Font.BOLD, 16));
    	
        feedbackEditor = new TekstEditor(false,true,true);
        feedbackEditor.setPreferredSize(new Dimension(400,160));
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
    	
        gelijkwaardigCB = makeCheckBox(320,410,120,20,WiskOpdr.rb.getString("gelijkwaardigCBLabel"),true,true);
        gelijkwaardigCB.addMouseListener(this);
        exactCB = makeCheckBox(320,significantieAan?510:485,120,20,WiskOpdr.rb.getString("exactCBLabel"),false,true);
        significantCB = makeCheckBox(320,485,120,20,WiskOpdr.rb.getString("significantCBLabel"),false,significantieAan?true:false);
        onafhankelijkCB = makeCheckBox(320,460,120,20,WiskOpdr.rb.getString("onafhankelijkCBLabel"), false, true);
        eindOplossingCB = makeCheckBox(320,460,120,20,WiskOpdr.rb.getString("eindOplossingCBLabel"),true,true);
        gelijkwaardigPV = makeTextField(460,410,30,20,""+puntenGelijkwaardig,true);
        eindOplossingPV = makeTextField(460,460,30,20,""+puntenEindOplossing,true);
        exactPV = makeTextField(460,significantieAan?510:485,30,20,""+puntenExact,false);
        significantPV = makeTextField(460,485,30,20,"0",false);
        onafhankelijkPV = makeTextField(460, 460, 30, 20, "" + puntenOnafhankelijk, true);
                
        // GUI Score
        titleScoreLabel = new JLabel(WiskOpdr.rb.getString("FEV_titleScoringLabel"));
    	titleScoreLabel.setForeground(WiskOpdr.colorBlue1);
    	titleScoreLabel.setFont(font.deriveFont(Font.BOLD, 16));
    	
    	feedbackPV = makeTextField(200,0,30,20,""+puntenFeedback,false);
    	
    	String[] items = {WiskOpdr.rb.getString("goedLabel"),WiskOpdr.rb.getString("doorLabel"),WiskOpdr.rb.getString("halfLabel"),WiskOpdr.rb.getString("foutLabel")};
        goedFoutIP = new ActKeuzePanel(items,440,420,80,80);
        goedFoutIP.setPreferredSize(new Dimension(100,80));
        
        // Logging/Nakijken
        titleLoggingLabel = new JLabel(WiskOpdr.rb.getString("FEV_titleLoggingLabel"));
    	titleLoggingLabel.setForeground(WiskOpdr.colorBlue1);
    	titleLoggingLabel.setFont(font.deriveFont(Font.BOLD, 16));
    	
    	checkCB = makeCheckBox(5,5,200,20,WiskOpdr.rb.getString("checkCBLabel"),true,true);
        teltMeeCB = makeCheckBox(225,5,200,20,WiskOpdr.rb.getString("teltMeeCBLabel"),true,true);
        logCB = makeCheckBox(450,5,70,20,WiskOpdr.rb.getString("logCBLabel"),false,true);
        logIDField = makeTextField(520,5,60,20,"0",false);
        logIDLabelField = makeTextField(520,25,60,20,"",false);
        scoringLabel = makeLabel(320,385,160,20,WiskOpdr.rb.getString("score"),true);
        logIDLabelLabel = makeLabel(470,25,50,20,WiskOpdr.rb.getString("TVEP_logIDLabelLabel"),false);
       
        logObjectivesButton = new ObjectiveChoiceButton();
        logObjectivesButton.setPreferredSize(new Dimension(120,22));
        logObjectivesButton.setMaximumSize(new Dimension(120,22));
        logObjectivesButton.setVisible(ObjectiveChoiceButton.hasObjectiveChoices());
        logObjectivesButton.setBounds(600,5,120,20);
        if(ObjectiveChoiceButton.hasObjectiveChoices())add(logObjectivesButton);
        
        // Hulp setting
        titleHulpLabel = new JLabel(WiskOpdr.rb.getString("FEV_titleHulpLabel"));
    	titleHulpLabel.setForeground(WiskOpdr.colorBlue1);
    	titleHulpLabel.setFont(font.deriveFont(Font.BOLD, 16));
    	
    	feedbackCB = makeCheckBox(140,-2,80,20,WiskOpdr.rb.getString("feedbackCBLabel"),false,true);
    	stappenCB = makeCheckBox(630,50,200,20,WiskOpdr.rb.getString("stappenCBLabel"),true,false);
        formuleToolBijFocusCB = makeCheckBox(600,40,200,20,WiskOpdr.rb.getString("formuleToolCBLabel"),false,true);
        rekenVakZichtbaarCB = makeCheckBox(500,80,200,20,WiskOpdr.rb.getString("rekenVakZichtbaar"), true, true);
        oplossingenRegelZichtbaarCB = makeCheckBox(500,100,200,20,WiskOpdr.rb.getString("oplossingenRegelZichtbaar"), true, true);
        
        // GUI contextVar box
        titleContextLabel = new JLabel(WiskOpdr.rb.getString("FEV_titleContextLabel"));
    	titleContextLabel.setForeground(WiskOpdr.colorBlue1);
    	titleContextLabel.setFont(font.deriveFont(Font.BOLD, 16));
    	
        contextVarCB = makeCheckBox(690,115,100,20,WiskOpdr.rb.getString("contextVarCBLabel"),false,true);
        
        antwoordSubstitutiesVak = new FormuleEditor(true);
        antwoordSubstitutiesVak.setBounds(502,120,350,150);
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
        antwoordFunctiesVak.setBounds(502,150,350,150);
        
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
    	
    	//GUI HelpButtons
	    hbCheck = makeHelpButton(HELP_53_URL_CHECK);
    	hbTeltMee = makeHelpButton(HELP_53_URL_TELTMEE);
    	hbLogID = makeHelpButton(HELP_53_URL_LOGID);
    	hbRekenVakZichtbaar = makeHelpButton(HELP_53_URL_REKENVAKZICHTBAAR);
    	hbOplossingRegel = makeHelpButton(HELP_53_URL_OPLOSSINGREGEL);
    	hbContextvar = makeHelpButton(HELP_53_URL_CONTEXTVAR);
    	hbFormInvoer = makeHelpButton(HELP_53_URL_FORMINVOER);
    	hbRand = makeHelpButton(HELP_53_URL_RAND);	
    	hbVerificatie = makeHelpButton(HELP_53_URL_VERIFICATIE);
    	hbVariabelen = makeHelpButton(HELP_53_URL_VARIABELEN);
    	hbAntwoord = makeHelpButton(HELP_53_URL_ANTWOORD);
        
        // Overige
    	feedbackLabel = makeLabel(20,330,320,20,"feedback",true);        
        puntenLabel = makeLabel(460,385,40,20,WiskOpdr.rb.getString("puntenLabel"),true);
        checkTotaalLabel = makeLabel(620,385,160,20,WiskOpdr.rb.getString("checkTotaalLabel"),false);
        checkTotaalLabel.setForeground(Color.red);
		
        removeAll();
        plaatsGUI();
        add(mainPanel); 
    }
    
    public void plaatsGUI() {
    	//plaats componenten startbox
        Component[] r11 = {titleVariabelenLabel, 	ra(5,0), hbVariabelen, hgl()};
		Component[] r12 = {ra(0,110), 			variabelenEditorPanel};
		
		Component[] k1 = {hb(r11), ra(5,5), hb(r12)};
		variabelenBox = vb(k1);
		
		//plaats componenten contextbox
		Component[] r21 = {titleContextLabel, hgl()};
		Component[] r22 = {substitutiesButton, hgl()};
		Component[] r23 = {functiesButton, hgl()};
		
		Component[] k2 = {hb(r21),vst(15),hb(r22),vst(10),hb(r23), ra(10,20)};
		Component[] h1 = {vb(k2), hgl()};
		contextBox = hb(h1);
		
		// plaats compoenenten antwoordbox
		Component[] r31 = {titleAntwoordLabel,	ra(5,0),	 hbAntwoord, 	hgl()};
		Component[] r32 = {ra(0,110), 	antwoordEditorPanel};
		Component[] k3 = {hb(r31), ra(5,5), hb(r32)};
		antwoordBox = vb(k3);
				
		// plaatsComponenten settingBox
		Component[] r41 = {titleLoggingLabel, 	hgl()};
		Component[] r42 = {checkCB, 			ra(5,0),	hgl(),	hbCheck};
		Component[] r43 = {teltMeeCB, 			ra(5,0),	hgl(),	hbTeltMee};
		Component[] r44 = {logCB, 				ra(5,10), logIDField, ra(5,10), logIDLabelLabel, ra(5,10), logIDLabelField,ra(5,0),	hgl(),	hbLogID};
		Component[] r45 = {ra(6,0),			logObjectivesButton, hgl()};
		Component[] r46 = {titleHulpLabel, 		hgl()};
		Component[] r47 = {rekenVakZichtbaarCB, ra(5,0),		hgl(), hbRekenVakZichtbaar};
		Component[] r48 = {oplossingenRegelZichtbaarCB, ra(5,0),		hgl(), hbOplossingRegel};
		Component[] r49 = {formuleToolBijFocusCB, ra(5,0),	hgl(),	hbFormInvoer};
		Component[] r418 = {contextVarCB, 		ra(5,0),	hgl(),	hbContextvar};
		Component[] r421 = {contextBox, 		hgl()};
		Component[] r422 = {titleOpmaakLabel, 	hgl()};
		Component[] r424 = {boxMetRandCB, 		ra(5,0),	hgl(),	hbRand};
		Box settingsBox;
		
		Component[] k4 = {hb(r41),vst(5),hb(r42),hb(r43),hb(r44),vst(3),hb(r45),vst(20),hb(r46),vst(5),hb(r47),hb(r48),hb(r49),
				hb(r418),vst(20),hb(r421), hb(r422),vst(5),hb(r424), vgl()};
		settingsBox = vb(k4);
				
		// plaats componenten verificatie box
		Component[] r61 = {titleVerificatieLabel, 	ra(5,10),	hbVerificatie,	hgl(),	ra(5,10),				titleScoreLabel};
		Component[] r62 = {gelijkwaardigCB,			hgl(),  		gelijkwaardigPV};
		Component[] r64 = {eindOplossingCB,			hgl(),  		eindOplossingPV};
		Component[] r65 = {onafhankelijkCB,			hgl(),  		onafhankelijkPV};
		Component[] r66 = {exactCB,					hgl(),  		exactPV};
		
		Component[] k6 = {hb(r61), vst(15), hb(r62), hb(r64), hb(r65), hb(r66), vgl()};
		Component[] h6 = {vb(k6), hgl(), hgl()};
		verificatieBox = hb(h6);
		
		
		//plaats componenten scoringbox
        Component[] r71 = {ra(10,10), 		feedbackPV, 	hgl()};
		Component[] r72 = {goedFoutIP, 		hgl()};
		
		Component[] k7 = {hb(r71), vst(20), hb(r72), vgl()};
		scoringBox = vb(k7);
						
		contextBox.setVisible(false);
		
		// box plaatsen
		Box boxh = Box.createHorizontalBox();
		mainPanel.add(boxh);
		
		Box boxv1 = Box.createVerticalBox();
		boxh.add(boxv1);
		boxh.add(Box.createHorizontalStrut(20));
		boxh.add(settingsBox);
		
		Box boxh1 = Box.createHorizontalBox();
		Box boxh2 = Box.createHorizontalBox();
		Box boxh3 = Box.createHorizontalBox();
		
			boxv1.add(boxh1);
			boxv1.add(Box.createVerticalStrut(20));
		
		boxv1.add(boxh2);
		boxv1.add(Box.createVerticalStrut(20));
		boxv1.add(boxh3);
		
		boxh1.add(variabelenBox);
//		if(soort==1)
//			boxh1.add(contextBox);
		
		boxh2.add(antwoordBox);
		
		boxh3.add(verificatieBox);
		boxh3.add(Box.createHorizontalStrut(5));
		boxh3.add(scoringBox);
		boxh3.add(Box.createHorizontalGlue());
		//boxh3.add(feedbackBox);
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
    
    public JCheckBox makeCheckBox(int x, int y, int b, int h, String text, boolean selected, boolean visible)
    {   JCheckBox checkbox = new WiskOpdrCheckbox(text);
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
    {   JLabel label = new JLabel(text);
        label.setBounds(x,y,b,h);
        label.setFont(font);
        label.setVisible(visible);
        add(label,0);
        return label;
    }
    
    public JTextField makeTextField(int x, int y, int b, int h, String text, boolean visible)
    {   JTextField textField = new WiskOpdrTextField(text);
        textField.setBounds(x,y,b,h);
        textField.setPreferredSize(new Dimension(50,22));
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
    {   //grafiekPanel.setSize(b,grafiekPanel.getSize().height);
    }
    public void zetHoogte(int h)
    {   //grafiekPanel.setSize(grafiekPanel.getSize().width, h);
    }
    
    private Hashtable fillAnswerModel(Hashtable h)
    {
        String antwoordString = "$f@";
        boolean gelijkwaardig = true;
        boolean eindOplossingNodig = true;
        boolean exact = false;
        boolean significant = false;
        int puntenFeedback = 0;
        String feedback = "";
        int feedbackWidth = 200;
		int feedbackHeight = 20;
        int goedHalfFout = 3;
        
        antwoordString = antwoordvak.geefFormuleVak().toString();
        gelijkwaardig = this.gelijkwaardig;
        eindOplossingNodig = this.eindOplossingNodig;
        exact = this.exact;
        significant = this.significant;
		
                
        puntenFeedback = (Integer.parseInt(feedbackPV.getText()));
        feedback  = feedbackEditor.getText();
        if(feedbackEditorPopupFrame!=null)
		{	feedbackWidth = feedbackEditorPopupFrame.getSize().width - feedbackEditorPopupFrame.getInsets().left - feedbackEditorPopupFrame.getInsets().right;
			feedbackHeight = feedbackEditorPopupFrame.getSize().height - feedbackEditorPopupFrame.getInsets().top - feedbackEditorPopupFrame.getInsets().bottom;
		}
        //vormString = vormEditor.geefFormuleVak().toString();
        goedHalfFout = goedFoutIP.geefKeuze()-1;
        
        h.put("antwoordString",antwoordString);
        h.put("gelijkwaardig",new Boolean(gelijkwaardig));
        h.put("eindOplossingNodig",new Boolean(eindOplossingNodig));
        h.put("exact",new Boolean(exact));
        h.put("significant",new Boolean(significant));
        h.put("puntenFeedback",new Integer(puntenFeedback));
        h.put("feedback",feedback);
        h.put("feedbackWidth",new Integer(feedbackWidth));
		h.put("feedbackHeight",new Integer(feedbackHeight));
		h.put("goedHalfFout",new Integer(goedHalfFout));
        
        return h;
    }
    
    private void setAnswerModel(Hashtable h)
    {   String antwoordString = "$f@";
        boolean gelijkwaardig = true;
        boolean eindOplossingNodig = true;
        boolean exact = false;
        boolean significant = false;
		int puntenFeedback = 0;
        String feedback = "";
        int feedbackWidth = 0;
        int feedbackHeight = 0;
		int goedHalfFout = 3;
        
        if(h!=null) 
        {   if(h.containsKey("antwoordString")) antwoordString = (String)h.get("antwoordString");
            if(h.containsKey("gelijkwaardig")) gelijkwaardig = ((Boolean)h.get("gelijkwaardig")).booleanValue();
            if(h.containsKey("eindOplossingNodig")) eindOplossingNodig = ((Boolean)h.get("eindOplossingNodig")).booleanValue();
            if(h.containsKey("exact")) exact = ((Boolean)h.get("exact")).booleanValue();
            if(h.containsKey("significant")) significant = ((Boolean)h.get("significant")).booleanValue();
			if(h.containsKey("stappen")) stappen = ((Boolean)h.get("stappen")).booleanValue();
            if(h.containsKey("puntenFeedback")) puntenFeedback = ((Integer)h.get("puntenFeedback")).intValue();
            if(h.containsKey("feedback")) feedback = (String)h.get("feedback");
            if(h.containsKey("feedbackWidth")) feedbackWidth = ((Integer)h.get("feedbackWidth")).intValue();
			if(h.containsKey("feedbackHeight")) feedbackHeight = ((Integer)h.get("feedbackHeight")).intValue();
            if(h.containsKey("goedHalfFout")) goedHalfFout = ((Integer)h.get("goedHalfFout")).intValue();
            
        }
        this.gelijkwaardig = gelijkwaardig;
        this.eindOplossingNodig = eindOplossingNodig;
        this.exact = exact;
        this.significant = significant;
		this.puntenFeedback = puntenFeedback;
        
        antwoordvak.geefFormuleVak().vulVak(antwoordString);
        
        exactCB.setVisible(true);
        if(significantieAan) significantCB.setVisible(true);
		
                
        gelijkwaardigCB.setSelected(gelijkwaardig);
        eindOplossingCB.setSelected(eindOplossingNodig);
        exactCB.setSelected(exact);
        significantCB.setSelected(significant);
		
        feedbackPV.setVisible(hasFeedback);
        feedbackPV.setText(""+puntenFeedback);
        
        feedbackEditor.zetTekst(feedback);
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
        
        
    }
    
    private void getAnswerModel()
    {   if(answerModels==null)return;
       	answerModels[answerModelNr] = fillAnswerModel(new Hashtable());
    }
    
    private void setAnswerModel()
    {   if(answerModels==null)return;
        setAnswerModel(answerModels[answerModelNr]);    
    }
    
    public void setEditState(Hashtable interactiePanelLaunchState)
    {           String antwoordString = "$f@";
                String variabelenString = "$f@";
                boolean exact = false;
                boolean significant = false;
				boolean stappen = stappenDefault;
                int puntenGelijkwaardig = 10;
                int puntenExact = 0;
                int puntenOnafhankelijk = 0;
                int puntenSignificant = 0;
                boolean onafhankelijkNodig = false;
				boolean eindOplossingNodig = true;
                int puntenEindOplossing = 10;
                Hashtable[] answerModels = null;
                boolean hasFeedback = false;
                boolean feedbackSize = false;
                int feedbackModus = 0;
                String[] antwoordSubStrings = null;
                String[] antwoordFuncStrings = null;
                boolean check = true;
                boolean teltMee = true;
                boolean logOption = false;
				String logID = "";
				double eqTestValueMin = 0;
				double eqTestValueMax = 5;
				boolean casAntw = false;
				boolean boxMetRand = true;
				boolean rekenVakZichtbaar = true;
				boolean oplossingenRegelZichtbaar = true;
				boolean formuleToolBijFocus = true;
				
                if(interactiePanelLaunchState.containsKey("antwoordString")) antwoordString = (String)interactiePanelLaunchState.get("antwoordString");
                if(interactiePanelLaunchState.containsKey("variabelenString")) variabelenString = (String)interactiePanelLaunchState.get("variabelenString");
                if(interactiePanelLaunchState.containsKey("exact")) exact = ((Boolean)interactiePanelLaunchState.get("exact")).booleanValue();
                if(interactiePanelLaunchState.containsKey("significant")) significant = ((Boolean)interactiePanelLaunchState.get("significant")).booleanValue();
				if(interactiePanelLaunchState.containsKey("stappen")) stappen = ((Boolean)interactiePanelLaunchState.get("stappen")).booleanValue();
                if(interactiePanelLaunchState.containsKey("puntenGelijkwaardig")) puntenGelijkwaardig = ((Integer)interactiePanelLaunchState.get("puntenGelijkwaardig")).intValue();
                if(interactiePanelLaunchState.containsKey("puntenOnafhankelijk")) puntenOnafhankelijk = ((Integer)interactiePanelLaunchState.get("puntenOnafhankelijk")).intValue();
                if(interactiePanelLaunchState.containsKey("puntenExact")) puntenExact = ((Integer)interactiePanelLaunchState.get("puntenExact")).intValue();
                if(interactiePanelLaunchState.containsKey("puntenSignificant")) puntenSignificant = ((Integer)interactiePanelLaunchState.get("puntenSignificant")).intValue();
				if(interactiePanelLaunchState.containsKey("eindOplossingNodig")) eindOplossingNodig = ((Boolean)interactiePanelLaunchState.get("eindOplossingNodig")).booleanValue();
				if(interactiePanelLaunchState.containsKey("onafhankelijkNodig")) onafhankelijkNodig = ((Boolean)interactiePanelLaunchState.get("onafhankelijkNodig")).booleanValue();
                if(interactiePanelLaunchState.containsKey("puntenEindOplossing")) puntenEindOplossing = ((Integer)interactiePanelLaunchState.get("puntenEindOplossing")).intValue();
                if(interactiePanelLaunchState.containsKey("answerModels")) answerModels = (Hashtable[])interactiePanelLaunchState.get("answerModels");
                if(interactiePanelLaunchState.containsKey("hasFeedback")) hasFeedback = ((Boolean)interactiePanelLaunchState.get("hasFeedback")).booleanValue();
                if(interactiePanelLaunchState.containsKey("feedbackSize")) feedbackSize = ((Boolean)interactiePanelLaunchState.get("feedbackSize")).booleanValue();
				if(interactiePanelLaunchState.containsKey("antwoordSubStrings")) antwoordSubStrings = (String[])interactiePanelLaunchState.get("antwoordSubStrings");
                if(interactiePanelLaunchState.containsKey("antwoordFuncStrings")) antwoordFuncStrings = (String[])interactiePanelLaunchState.get("antwoordFuncStrings");
                if(interactiePanelLaunchState.containsKey("check")) check = ((Boolean)interactiePanelLaunchState.get("check")).booleanValue();
                if(interactiePanelLaunchState.containsKey("teltMee")) teltMee = ((Boolean)interactiePanelLaunchState.get("teltMee")).booleanValue();
                
                if(interactiePanelLaunchState.containsKey("logOption")) logOption = ((Boolean)interactiePanelLaunchState.get("logOption")).booleanValue();
                if(interactiePanelLaunchState.containsKey("logID")) logID = (String)interactiePanelLaunchState.get("logID");
				if(interactiePanelLaunchState.containsKey("eqTestValueMin")) eqTestValueMin = ((Double)interactiePanelLaunchState.get("eqTestValueMin")).doubleValue();
				if(interactiePanelLaunchState.containsKey("eqTestValueMax")) eqTestValueMax = ((Double)interactiePanelLaunchState.get("eqTestValueMax")).doubleValue();
				if(interactiePanelLaunchState.containsKey("casAntw")) casAntw = ((Boolean)interactiePanelLaunchState.get("casAntw")).booleanValue();
				if(interactiePanelLaunchState.containsKey("boxMetRand")) boxMetRand = ((Boolean)interactiePanelLaunchState.get("boxMetRand")).booleanValue();
				if(interactiePanelLaunchState.containsKey("rekenVakZichtbaar")) rekenVakZichtbaar = ((Boolean)interactiePanelLaunchState.get("rekenVakZichtbaar")).booleanValue();
				if(interactiePanelLaunchState.containsKey("oplossingenRegelZichtbaar")) oplossingenRegelZichtbaar = ((Boolean)interactiePanelLaunchState.get("oplossingenRegelZichtbaar")).booleanValue();
				if(interactiePanelLaunchState.containsKey("formuleToolBijFocus")) formuleToolBijFocus = ((Boolean)interactiePanelLaunchState.get("formuleToolBijFocus")).booleanValue();
				
                this.exact = exact;
                this.significant = significant;
				this.puntenGelijkwaardig = puntenGelijkwaardig;
                this.puntenExact = puntenExact;
                this.puntenSignificant = puntenSignificant;
                this.puntenOnafhankelijk = puntenOnafhankelijk;
				this.stappen = stappen;
                this.eindOplossingNodig = eindOplossingNodig;
                this.onafhankelijkNodig = onafhankelijkNodig;
                this.puntenEindOplossing = puntenEindOplossing;
                
                this.answerModels = new Hashtable[answerModels.length];
				for(int i=0 ; i<answerModels.length ; i++)
				{	this.answerModels[i] = answerModels[i];
				}
					
				this.eqTestValueMin = eqTestValueMin;
				this.eqTestValueMax = eqTestValueMax;
				this.hasFeedback = hasFeedback;
                
                if(hasFeedback)
                {   aantalAnswerModels = answerModels.length;
                    remove(tabbladTab);
                    tabbladTab = new OpdrachtNrRij(aantalAnswerModels, 250,184);
                    tabbladTab.setTab(true);
                    tabbladTab.setScoresVisible(false);
                    tabbladTab.setSize(tabbladTab.getSize().width, 23);
                    tabbladTab.addActionListener(this);
                    tabbladTab.setBackground(new Color(210,210,210));
                    tabbladTab.setSelected(answerModelNr+1);
                    add(tabbladTab,0);
                    aantalTabsKnop.setLocation(250+25*aantalAnswerModels+5 ,184);
                    
                    answerModelNr = 0;
                    setAnswerModel();
                }
                
                antwoordvak.geefFormuleVak().vulVak(antwoordString);
                variabelenEditor.geefFormuleVak().vulVak(variabelenString);
                
                antwoordSubstitutiesVak.zetRegels(antwoordSubStrings);
                antwoordFunctiesVak.zetRegels(antwoordFuncStrings);
                boolean hasSub = antwoordSubStrings!=null && (antwoordSubStrings.length>0 && !antwoordSubStrings[0].equals("$f@"));
                boolean hasFunc = antwoordFuncStrings!=null && (antwoordFuncStrings.length>0 && !antwoordFuncStrings[0].equals("$f@"));
                contextBox.setVisible(hasSub || hasFunc);
                this.contextVarCB.setSelected(hasSub || hasFunc);
                
                stappenCB.setSelected(stappen);
                
                checkCB.setSelected(check);
                teltMeeCB.setSelected(teltMee);
                
                logCB.setSelected(logOption);
                logIDField.setVisible(logOption);
	            //logObjectivesButton.setVisible(logOption);
	            logIDField.setText(logID);
                logObjectivesButton.setEditState(interactiePanelLaunchState);
                
              //  casAntwCB.setSelected(casAntw);
                boxMetRandCB.setSelected(boxMetRand);
                rekenVakZichtbaarCB.setSelected(rekenVakZichtbaar);
                oplossingenRegelZichtbaarCB.setSelected(oplossingenRegelZichtbaar);
                formuleToolBijFocusCB.setSelected(formuleToolBijFocus);
	            //startLabel.setVisible(uitw);
				//startEditor.setVisible(uitw);
                
                setFeedbackOption(hasFeedback);
               // feedbackCB.setSelected(hasFeedback);
                feedbackSizeCB.setSelected(feedbackSize);
				feedbackEditor.setResizable(feedbackSize);
				
                if(hasFeedback)return;
                
                eindOplossingCB.setSelected(eindOplossingNodig);
                eindOplossingPV.setVisible(eindOplossingNodig);
                eindOplossingPV.setText(""+puntenEindOplossing);
                    
                gelijkwaardigPV.setText(""+puntenGelijkwaardig);
                                
                exactCB.setSelected(exact);
                exactPV.setVisible(exact);
                exactPV.setText(""+puntenExact);
                
                significantCB.setSelected(significant);
                significantPV.setVisible(significant && significantieAan);
                significantPV.setText(""+puntenSignificant);
                
                onafhankelijkCB.setSelected(onafhankelijkNodig);
                onafhankelijkPV.setVisible(onafhankelijkNodig);
                onafhankelijkPV.setText(""+puntenOnafhankelijk);
    }
    
    @SuppressWarnings("unchecked")
    public Hashtable getEditState()
    {   
        Hashtable interactiePanelLaunchState = new Hashtable();
        
        
            String antwoordString = null;
            String variabelenString = null;
            boolean vorm = false;
            boolean exact = false;
            boolean significant = false;
			boolean stappen = false;
            int puntenGelijkwaardig = 10;
            int puntenExact = 0; 
            int puntenSignificant = 0; 
            int puntenOnafhankelijk = 0;
			boolean eindOplossingNodig = true;
			boolean onafhankelijkNodig = false;
            int puntenEindOplossing = 10;
            int scoreMax = 0;
            Hashtable[] answerModels;
            boolean hasFeedback;
            boolean feedbackSize;
            int feedbackModus = 0;
            String[] antwoordSubStrings = null;
            String[] antwoordFuncStrings = null;
             boolean check = true;
            boolean teltMee = true;
            boolean logOption = false;
			String logID = "";
			double eqTestValueMin = 0;
			double eqTestValueMax = 5;
			boolean boxMetRand = true;
			boolean rekenVakZichtbaar = true;
			boolean oplossingenRegelZichtbaar = true;
			boolean formuleToolBijFocus = true;
			
            getAnswerModel();
            answerModels = this.answerModels;
            if(answerModels!=null)setAnswerModel(answerModels[0]);
            
            antwoordString = antwoordvak.geefFormuleVak().toString();
            variabelenString = variabelenEditor.geefFormuleVak().toString();
            
            exact = this.exact;
            significant = this.significant;
			stappen = this.stappen;
			
            try
            {   puntenGelijkwaardig = Integer.parseInt(gelijkwaardigPV.getText());
                this.puntenGelijkwaardig = puntenGelijkwaardig;
            }   
            catch(Exception ex)
            {   }
            try
            {   puntenExact = Integer.parseInt(exactPV.getText());
                this.puntenExact = puntenExact;
            }   
            catch(Exception ex)
            {   }
            try
			{	puntenSignificant = Integer.parseInt(significantPV.getText());
				this.puntenSignificant = puntenSignificant;
			}	
			catch(Exception ex)
			{	}
            try
            {   puntenEindOplossing = Integer.parseInt(eindOplossingPV.getText());
                this.puntenEindOplossing = puntenEindOplossing;
            }   
            catch(Exception ex)
            {   }
            try
            {   puntenOnafhankelijk = Integer.parseInt(onafhankelijkPV.getText());
                this.puntenOnafhankelijk = puntenOnafhankelijk;
            }   
            catch(Exception ex)
            {   }
            puntenGelijkwaardig = this.puntenGelijkwaardig;
            puntenExact = this.puntenExact;
            puntenSignificant = this.puntenSignificant;
            puntenOnafhankelijk = this.puntenOnafhankelijk;
			eindOplossingNodig = this.eindOplossingNodig;
			onafhankelijkNodig = this.onafhankelijkNodig;
            puntenEindOplossing = this.puntenEindOplossing;
            scoreMax = puntenGelijkwaardig + puntenOnafhankelijk + puntenEindOplossing + puntenExact;
            hasFeedback = this.hasFeedback;
            if(hasFeedback)scoreMax = puntenFeedback;
            feedbackSize = feedbackSizeCB.isSelected();
            
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
    			String pattern = "[a-zA-Z]+[']?[(][a-zA-Z][)]";
    	        boolean matches = Pattern.matches(pattern, functieDelen[0].substring(2));
    	        if(!matches)
    	        {	JOptionPane.showMessageDialog(this, "Syntax klopt niet. Gebruik bv:\n f(x)=expressie \n of \n func(x)=expressie");
    	        	break;
    	        }
    		}
    		
            check = checkCB.isSelected();
            teltMee = teltMeeCB.isSelected();
            logOption = logCB.isSelected();
			logID = logIDField.getText();
			//casAntw = casAntwCB.isSelected();
			boxMetRand = boxMetRandCB.isSelected();
			rekenVakZichtbaar = rekenVakZichtbaarCB.isSelected();
			oplossingenRegelZichtbaar = oplossingenRegelZichtbaarCB.isSelected();
			formuleToolBijFocus = formuleToolBijFocusCB.isSelected();
			
			if(!teltMee)scoreMax = 0;
			            
            eqTestValueMin = this.eqTestValueMin;
			eqTestValueMax = this.eqTestValueMax;
            
            interactiePanelLaunchState.put("antwoordString",antwoordString);
            interactiePanelLaunchState.put("variabelenString",variabelenString);
            interactiePanelLaunchState.put("vorm",new Boolean(vorm));
            interactiePanelLaunchState.put("exact",new Boolean(exact));
            interactiePanelLaunchState.put("significant",new Boolean(significant));
			interactiePanelLaunchState.put("stappen",new Boolean(stappen));
            interactiePanelLaunchState.put("puntenGelijkwaardig",new Integer(puntenGelijkwaardig));
            interactiePanelLaunchState.put("puntenOnafhankelijk", new Integer(puntenOnafhankelijk));
            interactiePanelLaunchState.put("puntenExact",new Integer(puntenExact));
            interactiePanelLaunchState.put("puntenSignificant",new Integer(puntenSignificant));
			interactiePanelLaunchState.put("eindOplossingNodig",new Boolean(eindOplossingNodig));
			interactiePanelLaunchState.put("onafhankelijkNodig", new Boolean(onafhankelijkNodig));
            interactiePanelLaunchState.put("puntenEindOplossing",new Integer(puntenEindOplossing));
            interactiePanelLaunchState.put("scoreMax",new Integer(scoreMax));
            if(answerModels!=null)interactiePanelLaunchState.put("answerModels",answerModels);
            interactiePanelLaunchState.put("hasFeedback",new Boolean(hasFeedback));
            interactiePanelLaunchState.put("feedbackSize",new Boolean(feedbackSize));
			interactiePanelLaunchState.put("antwoordSubStrings",antwoordSubStrings);
            interactiePanelLaunchState.put("antwoordFuncStrings",antwoordFuncStrings);
            interactiePanelLaunchState.put("check",new Boolean(check));
            interactiePanelLaunchState.put("teltMee",new Boolean(teltMee));
            interactiePanelLaunchState.put("logOption",new Boolean(logOption));
			interactiePanelLaunchState.put("logID",logID);     
			interactiePanelLaunchState.put("eqTestValueMin",new Double(eqTestValueMin));
			interactiePanelLaunchState.put("eqTestValueMax",new Double(eqTestValueMax));
			interactiePanelLaunchState.put("boxMetRand",new Boolean(boxMetRand));
			interactiePanelLaunchState.put("rekenVakZichtbaar", new Boolean(rekenVakZichtbaar));
			interactiePanelLaunchState.put("oplossingenRegelZichtbaar", new Boolean(oplossingenRegelZichtbaar));
			interactiePanelLaunchState.put("formuleToolBijFocus", new Boolean(formuleToolBijFocus));
            
            interactiePanelLaunchState.putAll(logObjectivesButton.getEditState(scoreMax));
                
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
    {   antwoordvak.setNewScrollSize();
    }
    
    public void maakantwoordSubstitutiesFrame()
   	{
    	DialogFacade substitutiesVakPopupFrame = DialogFacade.newInstance(this, "Definities variabelen");
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
    	DialogFacade functiesVakPopupFrame = DialogFacade.newInstance(this, "Definities functies");
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
    
    public void actionPerformed(ActionEvent e)
    {   
    	if(e.getSource() instanceof HelpButton)
		{
			OpdrNavStructEdit.helpBrowser.loadURL(((HelpButton)e.getSource()).getURL());
		}
		else if(e.getSource() == tabbladTab)
        {   int nr = Integer.parseInt(e.getActionCommand())-1;
            if(answerModelNr != nr) 
            {
                getAnswerModel();
                answerModelNr = nr;
                setAnswerModel();
                tabPositieKnop.setLocation(246+25*answerModelNr+5 ,162);
            }
            
        }
        else if(e.getSource() == tabPositieKnop)
        {   if(e.getActionCommand().equals("plus") && answerModelNr<aantalAnswerModels-1) 
            {   resAnswerModel = new Hashtable();
                fillAnswerModel(resAnswerModel);
                answerModels[answerModelNr] = answerModels[answerModelNr+1];
                answerModels[answerModelNr+1] = resAnswerModel;
                answerModelNr++;
                tabbladTab.setSelected(answerModelNr+1);
                tabPositieKnop.setLocation(246+25*answerModelNr+5 ,162);
            }
            if(e.getActionCommand().equals("min") && answerModelNr>0) 
            {   resAnswerModel = new Hashtable();
                fillAnswerModel(resAnswerModel);
                answerModels[answerModelNr] = answerModels[answerModelNr-1];
                answerModels[answerModelNr-1] = resAnswerModel;
                answerModelNr--;
                tabbladTab.setSelected(answerModelNr+1);
                tabPositieKnop.setLocation(246+25*answerModelNr+5 ,162);
            }
            
        }
        else if(e.getSource() == aantalTabsKnop)
        {   if(e.getActionCommand().equals("min") && aantalAnswerModels>1)
            {   aantalAnswerModels--;
                if(answerModelNr>aantalAnswerModels-1) answerModelNr--;
                setAnswerModel();
                aantalTabsKnop.setLocation(250+25*aantalAnswerModels+5 ,184);
                remove(tabbladTab);
                tabbladTab = new OpdrachtNrRij(aantalAnswerModels, 250,184);
                tabbladTab.setTab(true);
                tabbladTab.setScoresVisible(false);
                tabbladTab.setSize(tabbladTab.getSize().width, 23);
                tabbladTab.addActionListener(this);
                tabbladTab.setBackground(new Color(210,210,210));
                tabbladTab.setSelected(answerModelNr+1);
                add(tabbladTab,0);
                Hashtable[] answerModelsNew = new Hashtable[aantalAnswerModels];
                for(int i=0 ; i<aantalAnswerModels ; i++)
                {   answerModelsNew[i] = answerModels[i];
                }
                answerModels = answerModelsNew;
                repaint();
                
            }
            if(e.getActionCommand().equals("plus") && aantalAnswerModels<20)
            {   aantalAnswerModels++;
                aantalTabsKnop.setLocation(250+25*aantalAnswerModels+5 ,184);
                remove(tabbladTab);
                tabbladTab = new OpdrachtNrRij(aantalAnswerModels, 250,184);
                tabbladTab.setTab(true);
                tabbladTab.setScoresVisible(false);
                tabbladTab.setSize(tabbladTab.getSize().width, 23);
                tabbladTab.addActionListener(this);
                tabbladTab.setBackground(new Color(210,210,210));
                tabbladTab.setSelected(answerModelNr+1);
                add(tabbladTab,0);
                Hashtable[] answerModelsNew = new Hashtable[aantalAnswerModels];
                for(int i=0 ; i<aantalAnswerModels-1 ; i++)
                {   answerModelsNew[i] = answerModels[i];
                }
                answerModels = answerModelsNew;
                repaint();
            }
        }
        //else if(e.getSource()==feedbackCB)
       // {   setFeedbackOption(feedbackCB.isSelected());
       //     
        //}
        
        else if(e.getSource()==contextVarCB)
        {   contextBox.setVisible(contextVarCB.isSelected());
    		((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)mainPanel)).packWidth();
        }
        else if(e.getSource()==feedbackSizeCB)
		{	feedbackEditor.setResizable(feedbackSizeCB.isSelected());
			
		}
        else if(e.getSource()==gelijkwaardigCB)
        {   gelijkwaardig = gelijkwaardigCB.isSelected();
            if(!hasFeedback && answerModelNr==0)gelijkwaardigPV.setVisible(gelijkwaardig);
            
        }
        else if(e.getSource()==exactCB)
        {   boolean b = exactCB.isSelected();
            exact = b;
            if(!hasFeedback && answerModelNr==0) {
            	exactPV.setVisible(b);
            	verificatieBox.validate();
            }
            
            if(b)
            {   eindOplossingNodig = true;
                eindOplossingCB.setSelected(true);
                if(!hasFeedback)eindOplossingPV.setVisible(true);
                
                gelijkwaardigPV.setText("0");
                onafhankelijkPV.setText("0");
                eindOplossingPV.setText("0");
                exactPV.setText("10");
                
                puntenEindOplossing = 0;
                puntenGelijkwaardig = 0;
                puntenOnafhankelijk = 0;
                puntenExact = 10;
            }
            else
            {   
                gelijkwaardigPV.setText("0");
                onafhankelijkPV.setText("0");
                eindOplossingPV.setText("10");
                exactPV.setText("0");
            
                puntenGelijkwaardig = 0;
                puntenOnafhankelijk = 0;
                puntenEindOplossing = 10;
                puntenExact = 0;
            }   
        }
        else if(e.getSource()==significantCB)
		{
			boolean b = significantCB.isSelected();
			significant = b;
			if(!hasFeedback && answerModelNr==0)significantPV.setVisible(b);
			if(b)
	        {   eindOplossingNodig = true;
	            eindOplossingCB.setSelected(true);
	            if(!hasFeedback)eindOplossingPV.setVisible(true);
	            eindOplossingPV.setText("10");
	            puntenEindOplossing = 10;
	        }
		}
        else if(e.getSource()==eindOplossingCB)
        {   boolean b = eindOplossingCB.isSelected();
            eindOplossingNodig = b;
            if(!hasFeedback && answerModelNr==0) {
            	eindOplossingPV.setVisible(b);
            	verificatieBox.validate();
            }
            if(b)
            {   gelijkwaardigPV.setText("0");
                onafhankelijkPV.setText("0");
                eindOplossingPV.setText("10");
                exactPV.setText("0");
                
                puntenGelijkwaardig = 0;
                puntenOnafhankelijk = 0;
                puntenEindOplossing = 10;
                puntenExact = 0;
            }
            else
            {   exactCB.setSelected(false);
                exactPV.setVisible(false);
                
                significantCB.setSelected(false);
              	significantPV.setVisible(false);
                
                gelijkwaardigPV.setText("10");
                gelijkwaardigPV.setText("10");
                puntenGelijkwaardig = 10;
                eindOplossingPV.setText("0");
                onafhankelijkPV.setText("0");
                exactPV.setText("0");
                significantPV.setText("0");
                
                puntenEindOplossing = 0;
                puntenOnafhankelijk = 0;
                puntenExact = 0;
                puntenSignificant = 0;
            }
        }
        else if(e.getSource()==stappenCB)
        {   boolean b = stappenCB.isSelected();
            stappen = b;
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
//        else if(e.getSource() == antwoordSolveVak)
//        {  	remove(antwoordSolveVak);
//        	repaint();
//        }
        
        else if(e.getSource()==logCB)
	    {   logIDField.setVisible(logCB.isSelected());
	    	validate();
	    }
        else if(e.getSource()==variabelenEditor)
		{	revalidate();
            repaint();
		}
       else if(e.getSource()==feedbackEditor)
        {  revalidate();
            repaint();
        }
        else
        {
            int puntenGelijkwaardig = 0;
            int puntenOnafhankelijk = 0;
            int puntenExact = 0;
            int puntenSignificant = 0;
			int puntenEindOplossing = 0;
            
            if(e.getSource()==gelijkwaardigPV)
            {   try
                {   puntenGelijkwaardig = Integer.parseInt(gelijkwaardigPV.getText());
                    this.puntenGelijkwaardig = puntenGelijkwaardig;
                }   
                catch(Exception ex)
                {   }
            }
            if(e.getSource()==onafhankelijkPV)
            {   try
                {   puntenOnafhankelijk = Integer.parseInt(onafhankelijkPV.getText());
                    this.puntenOnafhankelijk = puntenOnafhankelijk;
                }   
                catch(Exception ex)
                {   }
            }
            if(e.getSource()==exactPV)
            {   try
                {   puntenExact = Integer.parseInt(exactPV.getText());
                    this.puntenExact = puntenExact;
                }   
                catch(Exception ex)
                {   }
            }
            if(e.getSource()==significantPV)
			{	try
				{	puntenSignificant = Integer.parseInt(significantPV.getText());
					this.puntenSignificant = puntenSignificant;
				}	
				catch(Exception ex)
				{	}
			}
            if(e.getSource()==eindOplossingPV)
            {   try
                {   puntenEindOplossing = Integer.parseInt(eindOplossingPV.getText());
                    this.puntenEindOplossing = puntenEindOplossing;
                }   
                catch(Exception ex)
                {   }
            }
            
        }
    }
    
    public void zetTekstVak(boolean b)
    {   //tipsCB.setVisible(b);
    }
    
    public void setFeedbackOption(boolean b)
    {
        hasFeedback = b;
        tabbladTab.setVisible(b);
        feedbackEditor.setVisible(b);
        feedbackSizeCB.setVisible(b);
        feedbackLabel.setVisible(b);
        aantalTabsKnop.setVisible(b);
        tabPositieKnop.setVisible(b);
        feedbackPV.setVisible(b);
        goedFoutIP.setVisible(b);
        puntenLabel.setVisible(!b);
        
        gelijkwaardigPV.setVisible(!b);
        if(b || eindOplossingNodig) eindOplossingPV.setVisible(!b);
        if(b || exact)exactPV.setVisible(!b);
        if(b || significant && significantieAan) significantPV.setVisible(!b);
		
        
        answerModelNr = 0;
        tabbladTab.setSelected(answerModelNr+1);
        if(b)setAnswerModel();
    }
    
    
    public void zetTabletUser(FormuleVakHouder formuleVakHouder)
    {   if(tablet==null) return;
        tablet.zetFormuleVakHouder(formuleVakHouder);
        tabletUser = formuleVakHouder;
        
    }
    
    public void zetTablet(FormuleVakHouder formuleVakHouder, int x, int y)
    {   if(tablet==null) 
        {   tablet = new Tablet(formuleVakHouder);
            tablet.setLocation(x,y);
            
        }
        tablet.zetFormuleVakHouder(formuleVakHouder);
        tabletUser = formuleVakHouder;
        
        
    }
    
    public void addTablet(FormuleVakHouder formuleVakHouder, int x, int y)
    {   if(tablet==null) 
        {   tablet = new Tablet(formuleVakHouder);
            
            
        }
        if(!tabletAdded)
        {   add(tablet,0);
            tablet.setLocation(x,y);
            tabletAdded = true;
            //resize();
            repaint();
        }
        tablet.zetFormuleVakHouder(formuleVakHouder);
    }
    
    public void removeTablet()
    {   if(tablet==null)return;
        remove(tablet);
        //resize();
        repaint();
        tabletAdded = false;
    }
    
    public Tablet getTablet()
	{	return tablet;
	}
    
    public void mousePressed(MouseEvent e)
    {   if(e.getSource()==gelijkwaardigCB && e.getModifiers()== InputEvent.BUTTON3_MASK || e.isControlDown())
		{	try{
			new Expressie();
			String intervalString = JOptionPane.showInputDialog(this, "testwaarden interval is nu [" + Expressie.df.format(eqTestValueMin) + ";" + Expressie.df.format(eqTestValueMax) +"]", "Keuze testWaarden", JOptionPane.QUESTION_MESSAGE);
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
    {   actionListener = AWTEventMulticaster.add(actionListener,l);
    }
    
    public void removeActionListener(ActionListener l)
    {   actionListener = AWTEventMulticaster.remove(actionListener, l);
    }   
    
    public void produceAction(String command)
    {   if (actionListener != null)
        {   actionListener.actionPerformed( new ActionEvent(this, 0, command) );
        }
    }
    //end ActionProducer
    
    public class EditorComponentListener implements ComponentListener {

        @Override
        public void componentResized(ComponentEvent e) {
      	  if(e.getSource()==antwoordEditorPanel) {
      		  int w = antwoordEditorPanel.getWidth();
      		  int h = antwoordEditorPanel.getHeight();
      		  antwoordvak.setBounds(0,0,w,h);
      	  }
      	  if(e.getSource()==variabelenEditorPanel) {
      		  int w = variabelenEditorPanel.getWidth();
      		  int h = variabelenEditorPanel.getHeight();
      		  variabelenEditor.setBounds(0,0,w,h);
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
    	hbRekenVakZichtbaar.setVisible(b);
    	hbOplossingRegel.setVisible(b);
    	hbContextvar.setVisible(b);
    	hbFormInvoer.setVisible(b);
    	hbRand.setVisible(b);
    	hbVerificatie.setVisible(b);
    	hbVariabelen.setVisible(b);
    	hbAntwoord.setVisible(b);
	}

	@Override
	public String geefHelpURL() {
		return HELP_53_URL;
	}
}
