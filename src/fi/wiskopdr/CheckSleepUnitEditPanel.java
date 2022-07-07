package fi.wiskopdr;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Hashtable;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Point;

import javax.swing.*;

import fi.wiskopdr.tekstobjects.*;
import fi.wiskopdr.domainmodel.Constants;
import fi.wiskopdr.formuleobjects.*;
import fi.wiskopdr.opdrnav.ActKeuzePanel;
import fi.wiskopdr.opdrnav.OpdrNavStructEdit;
import fi.wiskopdr.opdrnav.OpdrachtNrRij;
import fi.wiskopdr.opdrnav.PlusMinKnop;
import fi.beans.iconan.Iconan;
import fi.beans.wiskopdrbeans.*;

public class CheckSleepUnitEditPanel extends JPanel implements InteractieEditPanel, ActionListener, FocusListener, HelpButtonPanelIF
{
	// Algemene attributen 
    private Font font = new Font("SansSerif",Font.PLAIN,12);//WiskOpdr.tekstFont;
    private int scoreMax;
    private int aantalSleepObjects;
	private int aantalDoelObjects;
    private Tablet tablet;
    private boolean tabletAdded;
    private FormuleVakHouder tabletUser;
   	
    // Basis GUI
    private JPanel mainPanel;
    
    //mainPanel  //juiste antwoord
  	private JLabel titleAntwoordLabel;
  	private FormuleEditor formuleEditor;
  	private JPanel antwoordEditorPanel;
  	
  	 private OpdrachtNrRij tabbladTab;
     private PlusMinKnop aantalTabsKnop;
     private PlusMinKnop tabPositieKnop;
     
     private int aantalAnswerModels = 1;
     private Hashtable[] answerModels;
     private Hashtable resAnswerModel = new Hashtable();
     private int answerModelNr = 0;
     
     //Feedback editor
     private TekstEditor feedbackEditor;
     private JLabel titleFeedbackLabel;
     private JLabel titleFeedbackTekstLabel;
     private ActKeuzePanel goedFoutIP;
     private Box feedbackBox;
     private JLabel titleScoreLabel;
     private JTextField  feedbackPV;
   	
  	//mainPanel  // instellingen
  	private JLabel titleSettingsLabel;
  	private JLabel aantalSleepObjectsLabel;
  	private JTextField aantalSleepObjectsTF;
  	private JCheckBox snapToTargetCB;
  	private JLabel acceptedMargeLabel;
  	private JTextField acceptedMargeTF;
  	private JCheckBox randomizePositionsCB;
  	private JCheckBox relocateCB;
  	private JLabel aantalDoelObjectsLabel;
	private JTextField aantalDoelObjectsTF;
	private JCheckBox checkVastCB;
	private JCheckBox checkFormuleCB;
	private JCheckBox verzamelDoelCB;
  	
  	private JLabel imageKnopLabel;
  	private FormuleButton knopImageButton;
  	private Image knopImage;
  	private Dialog imageDialog;
  	private Iconan iconman;
  	private String knopImageString = "";
  	
  	// logging /nakijken
 	private JLabel titleLoggingLabel;
 	private JLabel maxScoreLabel;
 	private JTextField maxScoreTF;
 	private JCheckBox checkCB;
 	private JCheckBox teltMeeCB;
 	private JCheckBox logCB;
 	private JTextField logIDField;
 	private JTextField logIDLabelField;
 	private JLabel logIDLabelLabel;
 	private ObjectiveChoiceButton logObjectivesButton;	
 	private JCheckBox feedbackCB;
  	
 	// Hulp
 	private JLabel titleHulpLabel;
 	private JCheckBox viewCB;
 	
 	// Helpbuttons
    private static String HELP_16_URL = WiskOpdr.rb.getString("HELP_16_URL");
    private static String HELP_16_URL_CHECK = WiskOpdr.rb.getString("HELP_16_URL_CHECK");
    private static String HELP_16_URL_TELTMEE = WiskOpdr.rb.getString("HELP_16_URL_TELTMEE");
    private static String HELP_16_URL_LOGID = WiskOpdr.rb.getString("HELP_16_URL_LOGID");
    private static String HELP_16_URL_RANDOM = WiskOpdr.rb.getString("HELP_16_URL_RANDOM");
    private static String HELP_16_URL_SNAP = WiskOpdr.rb.getString("HELP_16_URL_SNAP");
    private static String HELP_16_URL_RELOCATE = WiskOpdr.rb.getString("HELP_16_URL_RELOCATE");
    private static String HELP_16_URL_VAST = WiskOpdr.rb.getString("HELP_16_URL_VAST");
    private static String HELP_16_URL_CHECKFORMULE = WiskOpdr.rb.getString("HELP_16_URL_CHECKFORMULE");
    private static String HELP_16_URL_ANTWOORD = WiskOpdr.rb.getString("HELP_16_URL_ANTWOORD");
    private static String HELP_16_URL_VERZAMEL = WiskOpdr.rb.getString("HELP_16_URL_VERZAMEL");
    private static String HELP_16_URL_KNOPIMAGE = WiskOpdr.rb.getString("HELP_16_URL_KNOPIMAGE");
    private static String HELP_16_URL_VIEW = WiskOpdr.rb.getString("HELP_16_URL_VIEW");
    
    private HelpButton hbCheck = makeHelpButton(HELP_16_URL_CHECK);
    private HelpButton hbTeltMee = makeHelpButton(HELP_16_URL_TELTMEE);
    private HelpButton hbLogID = makeHelpButton(HELP_16_URL_LOGID);
    private HelpButton hbRandom = makeHelpButton(HELP_16_URL_RANDOM);
    private HelpButton hbSnap = makeHelpButton(HELP_16_URL_SNAP);
    private HelpButton hbRelocate = makeHelpButton(HELP_16_URL_RELOCATE);
    private HelpButton hbVast = makeHelpButton(HELP_16_URL_VAST);
    private HelpButton hbCheckFormule = makeHelpButton(HELP_16_URL_CHECKFORMULE);
    private HelpButton hbAntwoord = makeHelpButton(HELP_16_URL_ANTWOORD);
    private HelpButton hbVerzamel = makeHelpButton(HELP_16_URL_VERZAMEL);
    private HelpButton hbKnopImage = makeHelpButton(HELP_16_URL_KNOPIMAGE);
    private HelpButton hbView = makeHelpButton(HELP_16_URL_VIEW);
  	
    private boolean helpVisible = false;
	
	public CheckSleepUnitEditPanel()
	{
		setLayout(new BorderLayout());
		setBounds(0,0,780,480);
		
		makeGUI();
		answerModels = new Hashtable[aantalAnswerModels];
	}
	
	private void makeGUI() {
		// Main
    	mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBackground(WiskOpdr.colorGray3);
		//mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
		
		//mainPanel  //juiste antwoord
		titleAntwoordLabel = makeLabel(WiskOpdr.rb.getString("FEV_titleAntwoordLabel"), font.deriveFont(Font.BOLD, 16));
		titleAntwoordLabel.setBounds(0,-3,140,20);
		titleAntwoordLabel.setVisible(false);
		
		formuleEditor = new FormuleEditor(true);
		formuleEditor.setBounds(0,20,450,300);
		formuleEditor.setMultiLine(true);
		//formuleEditor.setPreferredSize(new Dimension(340,300));
		//formuleEditor.setVisible(false);
		
		antwoordEditorPanel = new JPanel();
        antwoordEditorPanel.setLayout(null);
        antwoordEditorPanel.add(titleAntwoordLabel);
        antwoordEditorPanel.add(formuleEditor);
        antwoordEditorPanel.setBorder(BorderFactory.createMatteBorder(0,0,1,0,WiskOpdr.colorBlue3));
        //antwoordEditorPanel.add(antwoordvak);
        antwoordEditorPanel.addComponentListener(new EditorComponentListener());
        antwoordEditorPanel.setPreferredSize(new Dimension(450,220));
        antwoordEditorPanel.setMaximumSize(new Dimension(2835,420));
        antwoordEditorPanel.setVisible(false);
        
        tabbladTab = new OpdrachtNrRij(aantalAnswerModels, 160,20);
        tabbladTab.setSize(tabbladTab.getSize().width, 23);
        tabbladTab.setTab(true);
        tabbladTab.setScoresVisible(false);
        tabbladTab.addActionListener(this);
        tabbladTab.setBackground(new Color(210,210,210));
        tabbladTab.setSelected(1);
        tabbladTab.setVisible(false);
        antwoordEditorPanel.add(tabbladTab,0);
        
        aantalTabsKnop = new PlusMinKnop(160+25*aantalAnswerModels+5 ,24,20,16,PlusMinKnop.HORIZONTAAL);
        aantalTabsKnop.setBackground(new Color(210,210,210));
        aantalTabsKnop.addActionListener(this);
        aantalTabsKnop.setVisible(false);
        antwoordEditorPanel.add(aantalTabsKnop,0);
        
        tabPositieKnop = new PlusMinKnop(156+25*answerModelNr+5 ,0,20,16,PlusMinKnop.HORIZONTAAL);
        tabPositieKnop.addActionListener(this);
        tabPositieKnop.setVisible(false);
        antwoordEditorPanel.add(tabPositieKnop,0);
        
        
        //mainPanel //GUI Feedback editor
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
        
        feedbackPV = makeTextField("0",50,24,this);
        feedbackPV.addFocusListener(goedFoutIP);
        feedbackPV.addFocusListener(this);
        feedbackPV.setVisible(false);
        
        titleScoreLabel = new JLabel(WiskOpdr.rb.getString("FEV_titleScoringLabel"));
        titleScoreLabel.setForeground(WiskOpdr.colorBlue1);
        titleScoreLabel.setFont(font.deriveFont(Font.BOLD, 16));
        titleScoreLabel.setVisible(false);
        
		
		//mainPanel  //settings
		titleSettingsLabel = makeLabel(WiskOpdr.rb.getString("settingsLabel"), font.deriveFont(Font.BOLD, 16));
		aantalSleepObjectsLabel = makeLabel(WiskOpdr.rb.getString("aantalSleepObjLabel"), font);
		aantalSleepObjectsTF = makeTextField("0", 30, 22, this);
		aantalDoelObjectsLabel = makeLabel(WiskOpdr.rb.getString("aantalDoelObjLabel"), font);
		aantalDoelObjectsTF = makeTextField("0", 30, 22, this);
		
		randomizePositionsCB = makeCheckBox(WiskOpdr.rb.getString("randomPosLabel"),false,this);
		snapToTargetCB = makeCheckBox(WiskOpdr.rb.getString("snapToTargetLabel"),true,this);//("Snap to target");
		acceptedMargeLabel = makeLabel(WiskOpdr.rb.getString("snapMargeLabel"),font);
		acceptedMargeTF = makeTextField("10",40,20,this);
		relocateCB = makeCheckBox(WiskOpdr.rb.getString("relocateCBLabel"), false, this);//("Springt terug");
		checkVastCB = makeCheckBox(WiskOpdr.rb.getString("checkVasteDoelenLabel"), true, this);
		checkFormuleCB = makeCheckBox(WiskOpdr.rb.getString("checkWaardeOpDoelLabel"), false, this);
		verzamelDoelCB = makeCheckBox(WiskOpdr.rb.getString("verzamelDoelCBLabel"), false, this);
		
		imageKnopLabel = makeLabel(WiskOpdr.rb.getString("editImageKnopLabel"), font);
		
		knopImageButton = new FormuleButton(WiskOpdr.rb.getString("klaarKnopLabel"));
		knopImageButton.setPreferredSize(new Dimension(100,22));
		knopImageButton.addActionListener(this);
				
		//mainPanel  // logging / nakijken
		titleLoggingLabel = makeLabel(WiskOpdr.rb.getString("FEV_titleLoggingLabel"),font.deriveFont(Font.BOLD, 16));
		maxScoreLabel = makeLabel(WiskOpdr.rb.getString("score"), font);
		maxScoreTF = makeTextField("0",50,22,this);
		checkCB = makeCheckBox(WiskOpdr.rb.getString("checkCBLabel"),true, null);
		teltMeeCB = makeCheckBox(WiskOpdr.rb.getString("teltMeeCBLabel"), true, null);
		logCB = makeCheckBox(WiskOpdr.rb.getString("logCBLabel"), false, this);
		logIDField = makeTextField("",50,22,this);
		logIDField.setVisible(false);
		logIDLabelLabel = makeLabel(WiskOpdr.rb.getString("TVEP_logIDLabelLabel"),font);
		logIDLabelLabel.setVisible(false);
		logIDLabelField = makeTextField("",50,22,this);
		logIDLabelField.setVisible(false);
		logObjectivesButton = new ObjectiveChoiceButton();
        logObjectivesButton.setPreferredSize(new Dimension(120,22));
        logObjectivesButton.setMaximumSize(new Dimension(120,22));
        logObjectivesButton.setVisible(ObjectiveChoiceButton.hasObjectiveChoices());
        feedbackCB = makeCheckBox(WiskOpdr.rb.getString("feedbackCBLabel"),false,this);
        feedbackCB.setVisible(false);
        
        
        // mainPanel    //Hulp
        titleHulpLabel = makeLabel(WiskOpdr.rb.getString("FEV_titleHulpLabel"), font.deriveFont(Font.BOLD, 16));
        viewCB = makeCheckBox(WiskOpdr.rb.getString("viewCBLabel"), false, this);
		
        removeAll();
	    plaatsGUI();
	    add(mainPanel);
	}
	
	public void plaatsGUI() {
		//plaats componenten mainPanel
//		Component[] r11 = {titleAntwoordLabel, 	ra(10,0),	hbAntwoord, 	hgl()};
//		Component[] r12 = {formuleEditor, 	hgl()};
//     Component[] k1 = {hb(r11), vst(15), hb(r12),  vst(15), vgl()};
      
	   
	    
	 // plaats componenten feedback box
        Component[] r51 = {titleFeedbackLabel,  ra(5,10),   hgl()};
        Component[] r52 = {goedFoutIP,              hgl()};
        Component[] r52a = { titleScoreLabel, ra(5,10), feedbackPV, hgl()};
        
        Component[] r53 = {titleFeedbackTekstLabel,         hgl()};
        Component[] r54 = {ra(0,110),           feedbackEditor};
        
        Component[] k51 = {hb(r51),ra(0,5),hb(r52),ra(0,5),hb(r52a), vgl()};
        Component[] k52 = {hb(r53),ra(0,5),hb(r54), vgl()};
        
        Component[] h5 = {ra(20,0),vb(k51),ra(10,0), vb(k52)};
        feedbackBox = hb(h5);
        feedbackBox.setVisible(false);
        
        Component[] r11 = {antwoordEditorPanel};
        Component[] k1 = {hb(r11), ra(0,15), feedbackBox, vgl()};
		
		Component[] r21 = {titleSettingsLabel, 		hgl()};
		Component[] r22 = {aantalSleepObjectsLabel, ra(10,10), 	hgl(), aantalSleepObjectsTF	};
		Component[] r23 = {aantalDoelObjectsLabel, 	ra(10,10), 	hgl(), aantalDoelObjectsTF	};
		Component[] r24 = {randomizePositionsCB, 	ra(5,0),	hgl(),	hbRandom};
		Component[] r25 = {snapToTargetCB, 			ra(5,0),	hgl(),	hbSnap};
		Component[] r26 = {acceptedMargeLabel, 		ra(10,10), 	hgl(), acceptedMargeTF	};
		Component[] r27 = {relocateCB, 				ra(5,0),	hgl(),	hbRelocate};
		Component[] r28 = {checkVastCB, 			ra(5,0),	hgl(),	hbVast};
		Component[] r29 = {checkFormuleCB, 			ra(5,0),	hgl(),	hbCheckFormule};
		Component[] r210 = {verzamelDoelCB, 		ra(5,0),	hgl(),	hbVerzamel};
		Component[] r211 = {imageKnopLabel, 		ra(5,5), 	knopImageButton,ra(5,0),	hgl(),	hbKnopImage};
		
		Component[] k2 = {hb(r21), vst(15), hb(r22), vst(5), hb(r23), vst(5), hb(r24), vst(5), 
				hb(r25), vst(5), hb(r26), vst(5), hb(r27), vst(5), hb(r28), vst(5), hb(r29), vst(5), hb(r210), vst(5), hb(r211), vst(5), vgl()};
		
		Component[] r31 = {titleLoggingLabel, 	hgl()};
		Component[] r32 = {maxScoreLabel, 		ra(5,10), maxScoreTF, hgl()};
		Component[] r33 = {checkCB, 			ra(5,0),	hgl(),	hbCheck};
		Component[] r34 = {teltMeeCB, 			ra(5,0),	hgl(),	hbTeltMee};
		Component[] r35 = {logCB, 				ra(5,10), logIDField, ra(5,10), logIDLabelLabel, ra(5,10), logIDLabelField, ra(5,0),	hgl(),	hbLogID};
		Component[] r35a = {feedbackCB,                 ra(5,10),    hgl()};
        Component[] r36 = {ra(6,0),			logObjectivesButton, hgl()};
		Component[] r37 = {titleHulpLabel, 		hgl()};
		Component[] r38 = {viewCB, 				ra(5,0),	hgl(),	hbView};
		
		Component[] k3 = {hb(r31), vst(15), hb(r32), vst(5), hb(r33), vst(5), hb(r34), vst(5), hb(r35), vst(5), hb(r35a), vst(10),hb(r36), vst(30), hb(r37), vst(15), hb(r38),vgl()};
		
		Component[] main = {vb(k2), ra(50,0), vb(k1),ra(50,0), vb(k3)};
		mainPanel.add(hb(main));
	}
	
	public void setEditState(Hashtable h)
	{
		int aantalSleepObjects = 0;
		int aantalDoelObjects = 0;
	    int scoreMax = 0;
		boolean randomizePositions = false;
		boolean snapToTarget = false;
		int acceptedMarge = 10;
		boolean checkFormule = false;
		String formuleString = "$f@";
		String[] formuleStrings = null;
		boolean logOption = false;
		String logID = "";
		boolean check = true;
		boolean teltMee = true;
		boolean relocate = false;
		boolean view = false;
		boolean verzamelDoel = false;
		String knopImageString = "";
		Hashtable[] answerModels = null;
        boolean hasFeedback = false;
		
	    if(h.containsKey("aantalSleepObjects")) aantalSleepObjects = ((Integer)h.get("aantalSleepObjects")).intValue();
	    if(h.containsKey("aantalDoelObjects")) aantalDoelObjects = ((Integer)h.get("aantalDoelObjects")).intValue();
	    if(h.containsKey("scoreMax")) scoreMax = ((Integer)h.get("scoreMax")).intValue();
	    if(h.containsKey("randomizePositions")) randomizePositions = ((Boolean)h.get("randomizePositions")).booleanValue();
	    if(h.containsKey("snapToTarget")) snapToTarget = ((Boolean)h.get("snapToTarget")).booleanValue();
	    if(h.containsKey("acceptedMarge")) acceptedMarge = ((Integer)h.get("acceptedMarge")).intValue();
	    if(h.containsKey("checkFormule")) checkFormule = ((Boolean)h.get("checkFormule")).booleanValue();
	    if(h.containsKey("formuleString")) formuleString = (String)h.get("formuleString");
	    if(h.containsKey("formuleStrings")) formuleStrings = (String[])h.get("formuleStrings");
	    if(h.containsKey("logOption")) logOption = ((Boolean)h.get("logOption")).booleanValue();
		if(h.containsKey("logID")) logID = (String)h.get("logID");
		if(h.containsKey("check")) check = ((Boolean)h.get("check")).booleanValue();
		if(h.containsKey("teltMee")) teltMee = ((Boolean)h.get("teltMee")).booleanValue();
		if(h.containsKey("relocate")) relocate = ((Boolean)h.get("relocate")).booleanValue();
		if(h.containsKey("view")) view = ((Boolean)h.get("view")).booleanValue();
		if(h.containsKey("verzamelDoel")) verzamelDoel = ((Boolean)h.get("verzamelDoel")).booleanValue();
		if(h.containsKey("knopImageString")) knopImageString = (String)h.get("knopImageString");
		if(h.containsKey("answerModels")) answerModels = (Hashtable[])h.get("answerModels");
        if(h.containsKey("hasFeedback")) hasFeedback = ((Boolean)h.get("hasFeedback")).booleanValue();
        
        if(answerModels != null)
        {   this.answerModels = new Hashtable[answerModels.length];
            for(int i=0 ; i<answerModels.length ; i++)
            {   this.answerModels[i] = answerModels[i];
            }
        } 
        feedbackCB.setSelected(hasFeedback);
        if(hasFeedback)
        {   aantalAnswerModels = answerModels.length;
            antwoordEditorPanel.remove(tabbladTab);
            tabbladTab = new OpdrachtNrRij(aantalAnswerModels, 160,20);
            tabbladTab.setTab(true);
            tabbladTab.setScoresVisible(false);
            tabbladTab.setSize(tabbladTab.getSize().width, 23);
            tabbladTab.addActionListener(this);
            tabbladTab.setBackground(new Color(210,210,210));
            tabbladTab.setSelected(answerModelNr+1);
            antwoordEditorPanel.add(tabbladTab,0);
            aantalTabsKnop.setLocation(160+25*aantalAnswerModels+5 ,24);
            
            answerModelNr = 0;
            setAnswerModel();
            antwoordEditorPanel.setPreferredSize(new Dimension(Math.max(160+25*aantalAnswerModels+40,400),320));
        }
        
		this.knopImageString = knopImageString;
	    this.aantalSleepObjects = aantalSleepObjects;
	    this.aantalDoelObjects = aantalDoelObjects;
	    aantalSleepObjectsTF.setText(""+aantalSleepObjects);
	    aantalDoelObjectsTF.setText(""+aantalDoelObjects);
	    maxScoreTF.setText(""+scoreMax);
	    randomizePositionsCB.setSelected(randomizePositions);
	    snapToTargetCB.setSelected(snapToTarget);
	    acceptedMargeTF.setText(""+acceptedMarge);
	    checkVastCB.setSelected(!checkFormule);
	    checkFormuleCB.setSelected(checkFormule);
	    formuleEditor.verwijderRegels();
	    formuleEditor.geefFormuleVak().vulVak(formuleString);
	    if(formuleStrings!=null)formuleEditor.zetRegels(formuleStrings);
	    antwoordEditorPanel.setVisible(checkFormule);
	    feedbackCB.setVisible(checkFormule);
	    
	    logCB.setSelected(logOption);
        logIDField.setVisible(logOption);
        //logObjectivesButton.setVisible(logOption);
        logIDField.setText(logID);
        logObjectivesButton.setEditState(h);
        checkCB.setSelected(check);
        teltMeeCB.setSelected(teltMee);
        relocateCB.setVisible(snapToTarget);
        relocateCB.setSelected(relocate && snapToTarget);
        viewCB.setVisible(!checkFormule);
        viewCB.setSelected(view && !checkFormule);
        verzamelDoelCB.setSelected(verzamelDoel);
        
        checkVastCB.setEnabled(!verzamelDoel);
		
		knopImageButton.setPopupButtonImage(knopImage);
    	iconman = new Iconan(WiskOpdr.applet, (Component)this, (Hashtable)TekstImageVak.getImageMap());
    	if(knopImageString!=null && !"".equals(knopImageString)) {
    		knopImage = iconman.getImage(knopImageString);
    		knopImageButton.setPopupButtonImage(knopImage);
    	}
    	else {
    		knopImageButton.setCode(WiskOpdr.rb.getString("klaarKnopLabel"));
    	}
    	
    	setFeedbackOption(hasFeedback);
        
    	((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)mainPanel)).pack();

    }
	
	private JCheckBox makeCheckBox (String text, boolean selected, ActionListener al) {
		JCheckBox cb = new WiskOpdrCheckbox(text);
		if(al!=null) cb.addActionListener(al);
		cb.setSelected(selected);
		return cb;
	}
	
	private JButton makeButton (String text,  ActionListener al) {
		JButton bt = new WiskOpdrButton(text);
		if(al!=null) bt.addActionListener(al);
		return bt;
	}
	
	private JLabel makeLabel (String text, Font f) {
		JLabel lb = new JLabel(text);
		lb.setFont(f);
		lb.setForeground(WiskOpdr.colorBlue1);
		return lb;
	}
	
	private JTextField makeTextField (String text, int prefWidth, int prefHeight, ActionListener al) {
		JTextField tf = new WiskOpdrTextField(text);
		tf.setPreferredSize(new Dimension(prefWidth,prefHeight));
		if(al!=null) {
			tf.addActionListener(al);
			tf.addFocusListener(this);
		}
		return tf;
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
	
	private int intFromText(int defaultInt, String text) {
		int i = defaultInt;
		try {
			i = Integer.parseInt(text);
		}
		catch(NumberFormatException e) {}
		return i;
	}
	
  @SuppressWarnings("unchecked")
  public Hashtable getEditState()
	{
	    int aantalSleepObjects;
	    int aantalDoelObjects;
	    int scoreMax = 0;
	    boolean randomizePositions = false;
		boolean snapToTarget = false;
		int acceptedMarge = 0;
		boolean checkFormule = false;
		String formuleString = "$f@";
		String[] formuleStrings = null;
		boolean logOption = false;
		String logID = "";
		boolean check = true;
		boolean teltMee = true;
		boolean relocate = false;
		boolean view = false;
		boolean verzamelDoel = false;
		String knopImageString = "";
		Hashtable[] answerModels = null;
        boolean hasFeedback = false;
        
        getAnswerModel();
        if(this.answerModels!=null)
            setAnswerModel(this.answerModels[0]);
        
		
		knopImageString = this.knopImageString;
		aantalSleepObjects = Integer.parseInt(aantalSleepObjectsTF.getText());
		aantalDoelObjects = Integer.parseInt(aantalDoelObjectsTF.getText());
		scoreMax = intFromText(scoreMax, maxScoreTF.getText());
	        if(hasFeedback)
	          scoreMax = intFromText(scoreMax, feedbackPV.getText());
	    randomizePositions = randomizePositionsCB.isSelected();
	    snapToTarget = snapToTargetCB.isSelected();
	    acceptedMarge = Integer.parseInt(acceptedMargeTF.getText());
	    checkFormule = checkFormuleCB.isSelected();
	    formuleString = formuleEditor.geefFormuleVak().toString();
	    formuleStrings = formuleEditor.geefRegels();
	    logOption = logCB.isSelected();
		logID = logIDField.getText();
		check = checkCB.isSelected();
		teltMee = teltMeeCB.isSelected();
		relocate = relocateCB.isSelected();
		view = viewCB.isSelected();
		verzamelDoel = verzamelDoelCB.isSelected();
		hasFeedback = feedbackCB.isSelected();
		answerModels = this.answerModels;
		
		Hashtable h = new Hashtable();
		h.put("aantalSleepObjects", new Integer(aantalSleepObjects));
		h.put("aantalDoelObjects", new Integer(aantalDoelObjects));
		h.put("scoreMax", new Integer(scoreMax));
		h.put("randomizePositions", new Boolean(randomizePositions));
		h.put("snapToTarget", new Boolean(snapToTarget));
		h.put("acceptedMarge", new Integer(acceptedMarge));
		h.put("checkFormule", new Boolean(checkFormule));
		h.put("formuleString", formuleString);
		h.put("formuleStrings", formuleStrings);
		h.put("logOption",new Boolean(logOption));
		h.put("logID",logID);
		h.put("check",new Boolean(check));
		h.put("teltMee",new Boolean(teltMee));
		h.put("relocate",new Boolean(relocate));
		h.put("view",new Boolean(view));
		h.put("verzamelDoel",new Boolean(verzamelDoel));
		
		h.put("hasFeedback",new Boolean(hasFeedback));
        if(answerModels!=null)h.put("answerModels",answerModels);
        
        
        h.putAll(logObjectivesButton.getEditState(scoreMax));
            
		h.put("knopImageString", knopImageString);
		
		return h;
	}
		
	public void setBounds(int x, int y, int b, int h)
	{
		super.setBounds(x,y,b,h);
	}
	
	public void zetBreedte(int b){}
	
	public void zetHoogte(int h){}
	
	public void wis(){}
    
	public void zetMode(int mode){}
	
    public void stop(){}
    
    public void start(){}
    
    public void addActionListener(ActionListener al){}
    
    public void editImage() {
    	if(iconman==null)
			iconman = new Iconan(WiskOpdr.applet, this, (Hashtable)TekstImageVak.getImageMap());
		iconman.editImage(knopImageString, this, this);
		
//        if(imageDialog == null)
//        {
//        	
//        	Frame f = JOptionPane.getFrameForComponent(this);
//			imageDialog = new Dialog(f,"title", true);
//			imageDialog.setLayout(new BorderLayout());
//			iconman = new Iconan(WiskOpdr.applet, (Component)this, (Hashtable)TekstImageVak.getImageMap());
//            imageDialog.add(iconman);
//            imageDialog.pack();
//            iconman.addActionListener(this);
//        }
//        iconman.select(knopImageString);
//        imageDialog.setVisible(true);
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
                tabPositieKnop.setLocation(156+25*answerModelNr+5 ,0);
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
                tabPositieKnop.setLocation(156+25*answerModelNr+5 ,0);
            }
            if(e.getActionCommand().equals("min") && answerModelNr>0) 
            {   resAnswerModel = new Hashtable();
                fillAnswerModel(resAnswerModel);
                answerModels[answerModelNr] = answerModels[answerModelNr-1];
                answerModels[answerModelNr-1] = resAnswerModel;
                answerModelNr--;
                tabbladTab.setSelected(answerModelNr+1);
                tabPositieKnop.setLocation(156+25*answerModelNr+5 ,0);
            }
            
        }
        else if(e.getSource() == aantalTabsKnop)
        {   if(e.getActionCommand().equals("min") && aantalAnswerModels>1)
            {   //remove(opdrContainers[activiteitNr][aantalOpdrachten[activiteitNr]-1]);
                //opdrContainers[activiteitNr][aantalOpdrachten[activiteitNr]-1] = null;
                aantalAnswerModels--;
                if(answerModelNr>aantalAnswerModels-1) answerModelNr--;
                setAnswerModel();
                aantalTabsKnop.setLocation(160+25*aantalAnswerModels+5 ,24);
                antwoordEditorPanel.remove(tabbladTab);
                tabbladTab = new OpdrachtNrRij(aantalAnswerModels, 160,20);
                tabbladTab.setTab(true);
                tabbladTab.setScoresVisible(false);
                tabbladTab.setSize(tabbladTab.getSize().width, 23);
                tabbladTab.addActionListener(this);
                tabbladTab.setBackground(new Color(210,210,210));
                tabbladTab.setSelected(answerModelNr+1);
                antwoordEditorPanel.add(tabbladTab,0);
                Hashtable[] answerModelsNew = new Hashtable[aantalAnswerModels];
                for(int i=0 ; i<aantalAnswerModels ; i++)
                {   answerModelsNew[i] = answerModels[i];
                }
                answerModels = answerModelsNew;
                antwoordEditorPanel.repaint();
                
            }
            if(e.getActionCommand().equals("plus") && aantalAnswerModels<20)
            {   aantalAnswerModels++;
                aantalTabsKnop.setLocation(160+25*aantalAnswerModels+5 ,24);
                antwoordEditorPanel.remove(tabbladTab);
                tabbladTab = new OpdrachtNrRij(aantalAnswerModels, 160,20);
                tabbladTab.setTab(true);
                tabbladTab.setScoresVisible(false);
                tabbladTab.setSize(tabbladTab.getSize().width, 23);
                tabbladTab.addActionListener(this);
                tabbladTab.setBackground(new Color(210,210,210));
                tabbladTab.setSelected(answerModelNr+1);
                antwoordEditorPanel.add(tabbladTab,0);
                Hashtable[] answerModelsNew = new Hashtable[aantalAnswerModels];
                for(int i=0 ; i<aantalAnswerModels-1 ; i++)
                {   answerModelsNew[i] = answerModels[i];
                }
                answerModels = answerModelsNew;
                answerModels[aantalAnswerModels-1] = fillAnswerModel(new Hashtable());
                antwoordEditorPanel.repaint();
            }
            antwoordEditorPanel.setPreferredSize(new Dimension(Math.max(200+25*aantalAnswerModels+40,450),220));
            ((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)mainPanel)).pack();
        }
        else if(e.getSource()==feedbackCB)
        {   setFeedbackOption(feedbackCB.isSelected());
            ((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)mainPanel)).pack();
        }
		else if(e.getSource()==aantalSleepObjectsTF)
		{	aantalSleepObjects = Integer.parseInt(aantalSleepObjectsTF.getText());
		}
		else if(e.getSource()==aantalDoelObjectsTF)
		{	aantalDoelObjects = Integer.parseInt(aantalDoelObjectsTF.getText());
		}
		else if(e.getSource()==checkVastCB)
		{	checkFormuleCB.setSelected(!checkVastCB.isSelected());
			titleAntwoordLabel.setVisible(!checkVastCB.isSelected());
			//formuleEditor.setVisible(!checkVastCB.isSelected());
			viewCB.setVisible(checkVastCB.isSelected());
			titleHulpLabel.setVisible(checkVastCB.isSelected());
			if(!checkVastCB.isSelected())viewCB.setSelected(false);
			hbView.setVisible(helpVisible && !checkFormuleCB.isSelected());
			hbAntwoord.setVisible(helpVisible && checkFormuleCB.isSelected());
			feedbackCB.setVisible(checkFormuleCB.isSelected());
			antwoordEditorPanel.setVisible(checkFormuleCB.isSelected());
			((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)mainPanel)).pack();

		}
		else if(e.getSource()==checkFormuleCB)
		{	checkVastCB.setSelected(!checkFormuleCB.isSelected());
			//formuleEditor.setVisible(!checkVastCB.isSelected());
			hbAntwoord.setVisible(helpVisible && checkFormuleCB.isSelected());
			hbView.setVisible(helpVisible && !checkFormuleCB.isSelected());
			titleAntwoordLabel.setVisible(!checkVastCB.isSelected());
			viewCB.setVisible(checkVastCB.isSelected());
			titleHulpLabel.setVisible(checkVastCB.isSelected());
			if(!checkVastCB.isSelected())viewCB.setSelected(false);
			feedbackCB.setVisible(checkFormuleCB.isSelected());
			antwoordEditorPanel.setVisible(checkFormuleCB.isSelected());
			((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)mainPanel)).pack();

		}
		else if(e.getSource()==logCB)
	    {   logIDField.setVisible(logCB.isSelected()); 
	    	//logObjectivesButton.setVisible(logCB.isSelected());
	    	((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)mainPanel)).pack();

	    }
		else if(e.getSource()==snapToTargetCB)
		{	relocateCB.setVisible(snapToTargetCB.isSelected());
			if(!snapToTargetCB.isSelected())relocateCB.setSelected(false);
		}
		else if(e.getSource()==verzamelDoelCB)
		{
			boolean b = verzamelDoelCB.isSelected();
			checkVastCB.setEnabled(!b);
			checkFormuleCB.setEnabled(!b);
			if (b)
				checkVastCB.setSelected(!b);
			if (b)
				checkFormuleCB.setSelected(b);
			antwoordEditorPanel.setVisible(checkFormuleCB.isSelected());
			feedbackCB.setVisible(checkFormuleCB.isSelected());
            viewCB.setVisible(checkVastCB.isSelected());
			titleHulpLabel.setVisible(checkVastCB.isSelected());
			if(!checkVastCB.isSelected())viewCB.setSelected(false);
			((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)mainPanel)).pack();

		}
		else if(e.getSource()==knopImageButton) {   
			iconman = new Iconan(WiskOpdr.applet, mainPanel, (Hashtable)TekstImageVak.getImageMap());
			iconman.editImage(knopImageString, mainPanel, this);
	    }
	    else if(e.getSource()==iconman) {
	    	String name = e.getActionCommand();
	        if(!"".equals(name)) {
	        	knopImageString = name;
	            this.knopImage = iconman.getImage(name);
	            knopImageButton.setPopupButtonImage(knopImage);
	            int imWidth = iconman.getWidth(knopImageString);
				int imHeight = iconman.getHeight(knopImageString);
				if(imWidth == -1) imWidth = 20;
				if(imHeight == -1) imHeight = 20;
				knopImageButton.setPreferredSize(new Dimension(Math.max(imWidth,80),Math.max(imHeight,22)));
	         }
	        else {
	    		knopImageButton.setPopupButtonImage(null);
	    		knopImageButton.setCode(WiskOpdr.rb.getString("klaarKnopLabel"));
	    		knopImageButton.setPreferredSize(new Dimension(80,22));
	    	}
	    }

	    if(imageDialog!=null)
	        imageDialog.setVisible(false);
	}
    
    private Hashtable fillAnswerModel(Hashtable h)
    {
        String formuleString = "$f@";
        String[] formuleStrings = null;    
        
        formuleString = formuleEditor.geefFormuleVak().toString();
        formuleStrings = formuleEditor.geefRegels();
        String feedback = feedbackEditor.getText();
        int puntenFeedback = (Integer.parseInt(feedbackPV.getText()));
        
        int goedHalfFout = goedFoutIP.geefKeuze()-1;
  
        
        h.put("formuleString", formuleString);
        h.put("formuleStrings", formuleStrings);
        h.put("feedback", feedback);
        h.put("puntenFeedback", new Integer(puntenFeedback));
        h.put("goedHalfFout", new Integer(goedHalfFout));
      
        return h;
    }
    
    private void setAnswerModel(Hashtable h)
    {   
        String formuleString = "$f@";
        if(h.containsKey("formuleString")) 
            formuleString = (String)h.get("formuleString");
        
        String[] formuleStrings = null;
        if(h.containsKey("formuleStrings")) formuleStrings = (String[])h.get("formuleStrings");
       
        String feedback = "";
        if(h.containsKey("feedback")) feedback = (String)h.get("feedback");
        
        int puntenFeedback = 0;
        if(h.containsKey("puntenFeedback")) puntenFeedback = ((Integer)h.get("puntenFeedback")).intValue();
        
        int goedHalfFout = 2;
        if(h.containsKey("goedHalfFout")) goedHalfFout = ((Integer)h.get("goedHalfFout")).intValue();
        
        formuleEditor.verwijderRegels();
        formuleEditor.geefFormuleVak().vulVak(formuleString);
        if(formuleStrings!=null)formuleEditor.zetRegels(formuleStrings);
        feedbackEditor.zetTekst(feedback);
        feedbackEditor.layoutTekst();
        feedbackPV.setText(""+puntenFeedback);
        goedFoutIP.setItem(goedHalfFout);
       
        updateFeedbackTitelLabel();
    }
    
    private void getAnswerModel()
    {   if(answerModels==null)return;
        answerModels[answerModelNr] = fillAnswerModel(new Hashtable());
        if(answerModelNr==0) 
          maxScoreTF.setText(feedbackPV.getText());
    }
    
    private void setAnswerModel()
    {   if(answerModels==null)return;
         setAnswerModel(answerModels[answerModelNr]);
         
         
         
    }
    
    private void updateFeedbackTitelLabel()
    {   String feedbackNrString = "";
        boolean hasFeedback = feedbackCB.isSelected();
        if(hasFeedback && answerModelNr>0) {
            feedbackNrString += (answerModelNr+1);
            titleFeedbackTekstLabel.setText(WiskOpdr.rb.getString("FEV_titleFeedbackLabel") + " " + feedbackNrString);
            titleFeedbackLabel.setText(WiskOpdr.rb.getString("feedbackLabel") + " " + feedbackNrString);
            titleAntwoordLabel.setText(WiskOpdr.rb.getString("FEV_titleAntwoordNrLabel") + " " + feedbackNrString);
            titleScoreLabel.setText(WiskOpdr.rb.getString("FEV_titleScoringLabel") + " " + feedbackNrString);
            feedbackBox.validate();
       }
        else {
            titleFeedbackTekstLabel.setText(WiskOpdr.rb.getString("FEV_titleFeedbackLabel"));
            titleFeedbackLabel.setText(WiskOpdr.rb.getString("feedbackLabel"));
            titleAntwoordLabel.setText(WiskOpdr.rb.getString("FEV_titleAntwoordLabel"));
            titleScoreLabel.setText(WiskOpdr.rb.getString("FEV_titleScoringLabel"));
            titleScoreLabel.setText(WiskOpdr.rb.getString("FEV_titleScoringLabel") + (hasFeedback  ? " max" : " 1"));
            feedbackBox.validate();
       }
    }
    
    public void setFeedbackOption(boolean b)
    {
      tabbladTab.setVisible(b);
      aantalTabsKnop.setVisible(b);
      tabPositieKnop.setVisible(b);
      goedFoutIP.setVisible(b);
      feedbackPV.setVisible(b);
      goedFoutIP.setVisible(b);
      titleScoreLabel.setVisible(b);
      
      maxScoreLabel.setVisible(!b);
      maxScoreTF.setVisible(!b);
      
      if(!b) {
        antwoordEditorPanel.setPreferredSize(new Dimension(450,220));
        antwoordEditorPanel.setBorder(BorderFactory.createEmptyBorder());
      }
      else {
        antwoordEditorPanel.setPreferredSize(new Dimension(450,220));
        antwoordEditorPanel.setBorder(BorderFactory.createMatteBorder(0,0,1,0,WiskOpdr.colorBlue3));
      }
      if(feedbackBox!=null)
        feedbackBox.setVisible(b);
      
      answerModelNr = 0;
      tabbladTab.setSelected(answerModelNr+1);
      
      if(b) {
        getAnswerModel();
        setAnswerModel();
      }
           
//        hasFeedback = b;
//        if(!b) logMisconceptionsButton.setVisible(false);
//        tabbladTab.setVisible(b);
//        feedbackEditor.setVisible(b);
//        if(feedbackBox!=null)
//              feedbackBox.setVisible(b);
//        feedbackSizeCB.setVisible(b);
//        //feedbackLabel.setVisible(b);
//        aantalTabsKnop.setVisible(b);
//        tabPositieKnop.setVisible(b);
//        feedbackPV.setVisible(b);
//        goedFoutIP.setVisible(b);
//        if(scoringBox!=null)
//            scoringBox.setVisible(b);
//        titleVerificatieScoreLabel.setVisible(!b);
//        puntenLabel.setVisible(!b);
//        gelijkwaardigPV.setVisible(!b);
//        if(b || herleiding) herleidingPV.setVisible(!b);
//        if(b || exact) exactPV.setVisible(!b);
//        if(b || significant && significantieAan) significantPV.setVisible(!b);
//        
//        if(!b)
//                scoreCumulatiefCB.setSelected(false);
//        
//        answerModelNr = 0;
//        tabbladTab.setSelected(answerModelNr+1);
//        if(b)setAnswerModel();
    }

// methoden TabletOwner
    
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
    {   return tablet;
    }

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void focusLost(FocusEvent e) {
	  if(e.getSource()==feedbackPV && answerModelNr==0) {
        maxScoreTF.setText(feedbackPV.getText());
      }
		
	}

	@Override
	public void showHelpButtons(boolean b) {
		helpVisible = b;
		hbCheck.setVisible(b);
    	hbTeltMee.setVisible(b);
    	hbLogID.setVisible(b);
    	hbRandom.setVisible(b);
    	hbSnap.setVisible(b);
    	hbRelocate.setVisible(b);
    	hbVast.setVisible(b);
    	hbCheckFormule.setVisible(b);
    	if(checkFormuleCB.isSelected())hbAntwoord.setVisible(b);
    	hbVerzamel.setVisible(b);
    	hbKnopImage.setVisible(b);
    	hbView.setVisible(b);
    	((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)mainPanel)).pack();

   }

	@Override
	public String geefHelpURL() {
		return HELP_16_URL;
	}
    
    // einde methode TabletOwner
	
	public class EditorComponentListener implements ComponentListener {

      @Override
      public void componentResized(ComponentEvent e) {
          if(e.getSource()==antwoordEditorPanel) {
              int w = antwoordEditorPanel.getWidth();
              int h = antwoordEditorPanel.getHeight();
              formuleEditor.setBounds(0,20,w,h-20);
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
}
