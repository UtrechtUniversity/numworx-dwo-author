package fi.wiskopdr;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.function.Supplier;

import javax.swing.*;

import fi.wiskopdr.tekstobjects.*;
import fi.wiskopdr.formuleobjects.*;
import fi.wiskopdr.opdrnav.*;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.numworxlf.JScrollPane;


public class AntwoordKeuzeVakEditPanel extends JLayeredPane implements InteractieEditPanel, FocusListener, ActionListener,  MouseListener, MouseMotionListener, TabletOwner, HelpButtonPanelIF, Supplier<Number>
{
	// Algemene attributen 
    private Font font = new Font("SansSerif",Font.PLAIN,12);//WiskOpdr.tekstFont;
	 
    private Tablet tablet;
    private boolean tabletAdded;
    //private FormuleVakHouder tabletUser;
    
    // Basis GUI
    private JPanel mainPanel;
    
    // Antwoord editor
   	private TekstEditor antwoordvak;
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
   	private JLabel titleFeedbackLabel;
   	private Box feedbackBox;
   	//private DialogFacade feedbackEditorPopupFrame;
   	
   	// Verificatie
   	private JLabel titleVerificatieLabel;
   	private Box verificatieBox;
   	     
    //private int puntenGelijkwaardig = 10;
    private int puntenFeedback = 0;
     
    // Score
    private JLabel titleScoreLabel;
    //private JLabel ScoringLabel; // overbodig?
    private Box scoringBox;
    private JTextField  feedbackPV;
    private JTextField maxScorePV;
    private ActKeuzePanel goedFoutIP; 	
   	
    // Logging/Nakijken
  	private JLabel titleLoggingLabel;
  	//private Box loggingBox;
    private JCheckBox checkCB;
    private JCheckBox teltMeeCB;
      
    private JCheckBox logCB;
  	private JTextField logIDField;
  	private JTextField logIDLabelField;
  	private JLabel logIDLabelLabel;
  	private ObjectiveChoiceButton logObjectivesButton;
  	 	
  	// Settings
   	private JLabel titleSettingsLabel;
   	private Box settingsBox;
   	private JLabel aantalKeuzesLabel;
    private JTextField aantalKeuzesTF;
   	private JCheckBox feedbackCB;
   	private JCheckBox checkExternalCB;
    private JCheckBox randomizePositionsCB;
	
   	private boolean hasFeedback;
     
   	// keuzeTeksten
   	private JLabel titleKeuzeTekstenLabel;
   	private Box keuzeTekstenBox;
   	private TekstEditor[] keuzeVelden;
	private int maxKeuzeVelden = 50;
	private JPanel keuzeVeldenPanel;
	private JScrollPane scrollPaneKeuzeVelden;
	private JPanel basisKeuzeVeldenPanel;
	
	private int aantalKeuzes = 4;
	
	// Helpbuttons
    private static String HELP_14_URL = WiskOpdr.rb.getString("HELP_14_URL");
    private static String HELP_14_URL_CHECK = WiskOpdr.rb.getString("HELP_14_URL_CHECK");
    private static String HELP_14_URL_TELTMEE = WiskOpdr.rb.getString("HELP_14_URL_TELTMEE");
    private static String HELP_14_URL_LOGID = WiskOpdr.rb.getString("HELP_14_URL_LOGID");
    private static String HELP_14_URL_FEEDBACK = WiskOpdr.rb.getString("HELP_14_URL_FEEDBACK");
    private static String HELP_14_URL_ANTWOORD = WiskOpdr.rb.getString("HELP_14_URL_ANTWOORD");
    private static String HELP_14_URL_SCORE = WiskOpdr.rb.getString("HELP_14_URL_SCORE");
    private static String HELP_14_URL_KEUZE = WiskOpdr.rb.getString("HELP_14_URL_KEUZE");
    private static String HELP_14_URL_EXTERN = WiskOpdr.rb.getString("HELP_14_URL_EXTERN");
    private static String HELP_14_URL_FEEDBACKTITLE = WiskOpdr.rb.getString("HELP_14_URL_FEEDBACKTITLE");
    
    private HelpButton hbCheck = makeHelpButton(HELP_14_URL_CHECK);
    private HelpButton hbTeltMee = makeHelpButton(HELP_14_URL_TELTMEE);
    private HelpButton hbLogID = makeHelpButton(HELP_14_URL_LOGID);
    private HelpButton hbFeedback = makeHelpButton(HELP_14_URL_FEEDBACK);
    private HelpButton hbAntwoord = makeHelpButton(HELP_14_URL_ANTWOORD);
    private HelpButton hbScore = makeHelpButton(HELP_14_URL_SCORE);
    private HelpButton hbKeuze = makeHelpButton(HELP_14_URL_KEUZE);
    private HelpButton hbExtern = makeHelpButton(HELP_14_URL_EXTERN);
    private HelpButton hbFeedbackTitel = makeHelpButton(HELP_14_URL_FEEDBACKTITLE); 
   
	
	//Overige (wellicht overbodig geworden)
	private JLabel antwoordLabel,  feedbackLabel;
	private JLabel checkTotaalLabel;
	
	public AntwoordKeuzeVakEditPanel()
	{	setLayout(new BorderLayout());
		super.setSize(770,520); //voor dwo
		setBackground(Color.white);		
		addMouseListener(this);
		addMouseMotionListener(this);
		makeGUI();
		
		setFeedbackOption(false);
		answerModels = new Hashtable[aantalAnswerModels];
	}
	
	private void makeGUI() {
		// Main
    	mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBackground(WiskOpdr.colorGray3);
		
		// GUI Keuzeteksten
		titleKeuzeTekstenLabel = new JLabel(WiskOpdr.rb.getString("AKV_titleTekstenLabel"));
		titleKeuzeTekstenLabel.setForeground(WiskOpdr.colorBlue1);
		titleKeuzeTekstenLabel.setFont(font.deriveFont(Font.BOLD, 16));
		
		basisKeuzeVeldenPanel = new JPanel(new BorderLayout());
		basisKeuzeVeldenPanel.setPreferredSize(new Dimension(210,340));
		basisKeuzeVeldenPanel.setMinimumSize(new Dimension(210,340));
		basisKeuzeVeldenPanel.setMaximumSize(new Dimension(300,740));
		//basisKeuzeVeldenPanel.setOpaque(false);
		basisKeuzeVeldenPanel.setBackground(WiskOpdr.colorGray3);
        
        keuzeVeldenPanel = new JPanel();
		keuzeVeldenPanel.setLayout(null);
		//keuzeVeldenPanel.setPreferredSize(new Dimension(200,aantalKeuzes*85));
		keuzeVeldenPanel.setBackground(WiskOpdr.colorGray3);
      	
      	scrollPaneKeuzeVelden = new JScrollPane(keuzeVeldenPanel,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      	scrollPaneKeuzeVelden.setBorder(BorderFactory.createEmptyBorder());
      	basisKeuzeVeldenPanel.add(scrollPaneKeuzeVelden);
      	scrollPaneKeuzeVelden.setBackground(WiskOpdr.colorGray3);
    	
        keuzeVelden = new TekstEditor[maxKeuzeVelden];
        maakKeuzeVelden();
        
        // GUI antwoordBox
        titleAntwoordLabel = new JLabel(WiskOpdr.rb.getString("FEV_titleAntwoordLabel"));
      	titleAntwoordLabel.setForeground(WiskOpdr.colorBlue1);
      	titleAntwoordLabel.setFont(font.deriveFont(Font.BOLD, 16));
      	titleAntwoordLabel.setBounds(0,-3,140,20);
    	
        antwoordvak = new TekstEditor();
        antwoordvak.setBounds(0,20,500,150);
        antwoordvak.setFont(font);
        antwoordvak.addActionListener(this);
        antwoordvak.setResizable(true);
        
        antwoordEditorPanel = new JPanel();
        antwoordEditorPanel.setLayout(null);
        antwoordEditorPanel.add(titleAntwoordLabel);
        antwoordEditorPanel.add(antwoordvak);
        antwoordEditorPanel.addComponentListener(new EditorComponentListener());
        antwoordEditorPanel.setPreferredSize(new Dimension(400,120));
        antwoordEditorPanel.setMaximumSize(new Dimension(2835,120));
        
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
        feedbackEditor.setPreferredSize(new Dimension(250,100));
        feedbackEditor.setMaximumSize(new Dimension(2280,100));
        feedbackEditor.setBounds(5,350,280,110);
        feedbackEditor.setFont(font);
        feedbackEditor.addActionListener(this);
        feedbackEditor.setBackground(new Color(255,255,200));
          
        // GUI Verificatie box
        titleVerificatieLabel = new JLabel(WiskOpdr.rb.getString("FEV_titleVerificatieLabel"));
    	titleVerificatieLabel.setForeground(WiskOpdr.colorBlue1);
    	titleVerificatieLabel.setFont(font.deriveFont(Font.BOLD, 16));
    	
    	// GUI Score
		titleScoreLabel = new JLabel(WiskOpdr.rb.getString("FEV_titleScoringLabel"));
    	titleScoreLabel.setForeground(WiskOpdr.colorBlue1);
    	titleScoreLabel.setFont(font.deriveFont(Font.BOLD, 16));
    	
    	feedbackPV = makeTextField(460,385,30,20,"0",false);
    	maxScorePV = makeTextField(460,385,30,20,"0",false);
		
    	String[] items = {WiskOpdr.rb.getString("goedLabel"),WiskOpdr.rb.getString("halfLabel"),WiskOpdr.rb.getString("foutLabel")};
		goedFoutIP = new ActKeuzePanel(items,440,420,70,80);
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
        //ScoringLabel = makeLabel(320,385,160,20,WiskOpdr.rb.getString("score"),true);
        logIDLabelLabel = makeLabel(470,25,50,20,WiskOpdr.rb.getString("TVEP_logIDLabelLabel"),false);
		
        logObjectivesButton = new ObjectiveChoiceButton();
        logObjectivesButton.setVisible(ObjectiveChoiceButton.hasObjectiveChoices());
        logObjectivesButton.setBounds(600,5,120,20);
        logObjectivesButton.setPreferredSize(new Dimension(120,22));
        logObjectivesButton.setMaximumSize(new Dimension(120,22));
        logObjectivesButton.setDefaultGuess(this);
		        
        // Settings
        titleSettingsLabel = new JLabel(WiskOpdr.rb.getString("settingsLabel"));
        titleSettingsLabel.setForeground(WiskOpdr.colorBlue1);
        titleSettingsLabel.setFont(font.deriveFont(Font.BOLD, 16));
    	
    	aantalKeuzesLabel = makeLabel(10,50,180,20,WiskOpdr.rb.getString("aantalKeuzesLabel"),true);
		 
        aantalKeuzesTF = makeTextField(190,50,40,20,""+aantalKeuzes,true);
        aantalKeuzesTF.addActionListener(this);
        aantalKeuzesTF.addFocusListener(this);
        
    	feedbackCB = makeCheckBox(140,-2,80,20,WiskOpdr.rb.getString("feedbackCBLabel"),false,true);
    	checkExternalCB = makeCheckBox(250,120,200,20,WiskOpdr.rb.getString("checkExternalCBLabel"),false,true);
        randomizePositionsCB = makeCheckBox(0, 0, 200, 20, WiskOpdr.rb.getString("randomPosLabel"),false,true);
       
    	
        
    	//Overige zaken, wellicht overbodig
        antwoordLabel = makeLabel(250,160,520,20,WiskOpdr.rb.getString("antwoordLabel"),true);
        feedbackLabel = makeLabel(250,330,320,20,WiskOpdr.rb.getString("feedbackLabel"),true);
        //ScoringLabel = makeLabel(570,385,160,20,WiskOpdr.rb.getString("score"),true);
		checkTotaalLabel = makeLabel(620,385,160,20,WiskOpdr.rb.getString("checkTotaalLabel"),false);
		checkTotaalLabel.setForeground(Color.red);
				
		removeAll();
	    plaatsGUI();
	    add(mainPanel);	
		
	}
	
	private void plaatsGUI() {
		
		hbAntwoord.setBounds(140,-2,18,18);
    	antwoordEditorPanel.add(hbAntwoord,0);
    	
		// plaats compoenenten keuzeTekstenBox
		Component[] r11 = {titleKeuzeTekstenLabel, 	ra(5,5), hbKeuze, 	hgl()};
		Component[] r12 = {basisKeuzeVeldenPanel, 	hgl()};
		Component[] k1 = {hb(r11), vst(10),hb(r12)};
		keuzeTekstenBox = vb(k1);
		
		// plaatsComponenten settingBox
		Component[] r21 = {titleSettingsLabel, 	hgl()};
		Component[] r22 = {aantalKeuzesLabel, 	ra(10,10),	aantalKeuzesTF,		hgl()};
		Component[] r23 = {feedbackCB, 			ra(5,0),	hgl(),	hbFeedback};
		Component[] r24 = {checkExternalCB, 	ra(5,0),	hgl(),	hbExtern};
		Component[] r25 = {randomizePositionsCB, hgl()};
		Component[] k2 = {hb(r21), vst(20),hb(r22),vst(5), hb(r23),hb(r24), hb(r25), vgl()};	
		settingsBox = vb(k2);
		
		// plaatsComponenten loggingBox
		Component[] r41 = {titleLoggingLabel, 	hgl()};
		Component[] r42 = {checkCB, 			ra(5,0),	hgl(),	hbCheck};
		Component[] r43 = {teltMeeCB, 			ra(5,0),	hgl(),	hbTeltMee};
		Component[] r44 = {logCB, 				ra(5,10), logIDField, ra(5,10), logIDLabelLabel, ra(5,10), logIDLabelField,  ra(5,0),	hgl(),	hbLogID};
		Component[] r45 = {ra(6,0),			logObjectivesButton, hgl()};
		Component[] k4 = {hb(r41),vst(20),hb(r42),hb(r43),hb(r44),vst(3),hb(r45), vgl()};
		Box loggingBox = vb(k4);	
		
		// plaats componenten antwoordbox
		Component[] r31 = {ra(0,130), 	antwoordEditorPanel};
		Component[] k3 = {hb(r31)};
		antwoordBox = vb(k3);
		
		// plaats componenten feedback box
		Component[] r51 = {titleFeedbackLabel, 		hgl()};
		Component[] r52 = {ra(0,110),				hgl(),  		feedbackEditor};
		
		Component[] k5 = {hb(r51),vst(5),hb(r52), vgl()};
		Component[] h5 = {ra(20,10),vb(k5)};
		feedbackBox = hb(h5);
				
		// plaats componenten verificatie box
		Component[] r61 = {titleScoreLabel, 	ra(10,10),		maxScorePV,hgl()}; //titleVerificatieLabel, 	ra(10,10),		hgl(),		
		
		Component[] k6 = {hb(r61), vst(25), vgl()};
		Component[] h6 = {vb(k6), hgl(), hgl()};
		verificatieBox = hb(h6);
		    			
		//plaats componenten scoringbox
        Component[] r71 = {ra(10,10), 		feedbackPV, 	ra(5,10), hbScore, hgl()};
        Component[] r73 = {goedFoutIP, 		hgl()};
		
		Component[] k7 = {hb(r71),  vst(20), hb(r73), vgl()};
				scoringBox = vb(k7);
				
		// boxes plaatsen
		Box boxh = Box.createHorizontalBox();
		mainPanel.add(boxh);
		
		Box boxv1 = Box.createVerticalBox();
		boxh.add(keuzeTekstenBox);
		boxh.add(Box.createHorizontalStrut(50));
		boxh.add(boxv1);
		
		scoringBox.setVisible(false);
		feedbackBox.setVisible(false);
		
		Box boxh1 = Box.createHorizontalBox();
		Box boxh2 = Box.createHorizontalBox();
		Box boxh3 = Box.createHorizontalBox();
		
		boxv1.add(boxh1);
		boxv1.add(Box.createVerticalStrut(20));
		boxv1.add(boxh2);
		boxv1.add(Box.createVerticalStrut(20));
		boxv1.add(boxh3);
		
		boxh1.add(settingsBox);
		boxh1.add(Box.createHorizontalGlue());
		boxh1.add(loggingBox);
		
		boxh2.add(antwoordBox);
		
		boxh3.add(verificatieBox);
		boxh3.add(Box.createHorizontalStrut(5));
		boxh3.add(scoringBox);
		boxh3.add(Box.createHorizontalStrut(10));
		boxh3.add(Box.createHorizontalGlue());
		boxh3.add(feedbackBox);
	}
	
	private void updateFeedbackTitelLabel()
    {	String feedbackNrString = "";
    	if(hasFeedback && answerModelNr>0) {
    		feedbackNrString += (answerModelNr+1);
	    	titleFeedbackLabel.setText(WiskOpdr.rb.getString("FEV_titleFeedbackLabel") + " " + feedbackNrString);
	    	titleAntwoordLabel.setText(WiskOpdr.rb.getString("FEV_titleAntwoordNrLabel") + " " + feedbackNrString);
	    	titleScoreLabel.setText(WiskOpdr.rb.getString("FEV_titleScoringLabel") + " " + feedbackNrString);
	    	scoringBox.validate();
	   }
    	else {
    		titleFeedbackLabel.setText(WiskOpdr.rb.getString("FEV_titleFeedbackLabel"));
	    	titleAntwoordLabel.setText(WiskOpdr.rb.getString("FEV_titleAntwoordLabel"));
	    	titleScoreLabel.setText(WiskOpdr.rb.getString("FEV_titleScoringLabel"));
	    	scoringBox.validate();
	   }
    }
	
	public void maakKeuzeVelden()
    {
       
            /*if(keuzeLabels[i]==null)
            {   keuzeLabels[i] = new JLabel("Nr "+(i+1));
                keuzeLabels[i].setBounds(10,80+i*85,40,80);
                add(keuzeLabels[i]);
            }
        	if(selectableCheckboxes[i]==null)
            {   selectableCheckboxes[i] = new JCheckBox();//("Nr "+(i+1));
                selectableCheckboxes[i].setOpaque(false);
                selectableCheckboxes[i].setBounds(10,80+i*85,20,80);
                add(selectableCheckboxes[i],0);
            }*/
			//keuzeVeldenPanel.setBounds(0,80,200,aantalKeuzes*85);
			keuzeVeldenPanel.removeAll();
			for(int i=0 ; i<aantalKeuzes ; i++)
            {
	        	if(keuzeVelden[i]==null)
	            {   keuzeVelden[i] = new TekstEditor();
	            	keuzeVelden[i].setBounds(0,i*85,190,80);
	                
	            }
	        	keuzeVeldenPanel.add(keuzeVelden[i],0);
            }
			keuzeVeldenPanel.setPreferredSize(new Dimension(210,aantalKeuzes*85));
			keuzeVeldenPanel.scrollRectToVisible(new Rectangle(0,0, 10, 100));
			keuzeVeldenPanel.revalidate();
			keuzeVeldenPanel.doLayout();
        	
        	
        	
//            if(keuzeVelden[i]==null)
//            {   keuzeVelden[i] = new TekstEditor();
//            	
//                keuzeVelden[i].setBounds(30,80+i*85,190,80);
//                add(keuzeVelden[i],0);
//            }
//        }   
	    if (logObjectivesButton!=null)
			logObjectivesButton.setDefaultGuess(this);
        repaint();
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
	{	//grafiekPanel.setSize(b,grafiekPanel.getSize().height);
	}
	public void zetHoogte(int h)
	{	//grafiekPanel.setSize(grafiekPanel.getSize().width, h);
	}
	
	private Hashtable fillAnswerModel(Hashtable h)
	{
		String antwoordString = "$f@";
		int puntenFeedback = 0;
		String feedback = "";
		int goedHalfFout = 2;
		
		antwoordString = antwoordvak.getText().trim();
		puntenFeedback = (Integer.parseInt(feedbackPV.getText()));
		feedback  = feedbackEditor.getText();
		goedHalfFout = goedFoutIP.geefKeuze()-1;
		
		h.put("antwoordString",antwoordString);
		h.put("puntenFeedback",(puntenFeedback));
		h.put("feedback",feedback);
		h.put("goedHalfFout",(goedHalfFout));
		
		return h;
	}
	
	private void setAnswerModel(Hashtable h)
	{	String antwoordString = "";
		int puntenFeedback = 0;
		String feedback = "";
		int goedHalfFout = 2;
		
		if(h!=null) 
		{	if(h.containsKey("antwoordString")) antwoordString = (String)h.get("antwoordString");
			if(h.containsKey("puntenFeedback")) puntenFeedback = ((Integer)h.get("puntenFeedback")).intValue();
			if(h.containsKey("feedback")) feedback = (String)h.get("feedback");
			if(h.containsKey("goedHalfFout")) goedHalfFout = ((Integer)h.get("goedHalfFout")).intValue();
			
		}
		this.puntenFeedback = puntenFeedback;
		antwoordvak.zetTekst(antwoordString);
		antwoordvak.layoutTekst();
			
		
		feedbackPV.setVisible(hasFeedback);
		feedbackPV.setText(""+puntenFeedback);
		
		feedbackEditor.zetTekst(feedback);
		feedbackEditor.layoutTekst();
		feedbackEditor.repaint();
		
		goedFoutIP.setItem(goedHalfFout);
		
		updateFeedbackTitelLabel();
	}
	
	private void getAnswerModel()
	{	if(answerModels==null)return;
		answerModels[answerModelNr] = fillAnswerModel(new Hashtable());
	}
	
	private void setAnswerModel()
	{	if(answerModels==null)return;
		setAnswerModel(answerModels[answerModelNr]);	
	}
	
			
	public void setEditState(Hashtable interactiePanelLaunchState)
	{			
				String[] keuzeMogelijkheden = null;	
				String antwoordString = "$f@";
				String startString = "$f@";
				int scoreMax = 10;
				Hashtable[] answerModels = null;
				boolean hasFeedback = false;
				boolean check = true;
				boolean teltMee = true;
				boolean logOption = false;
				String logID = "";
				String logIDLabel = "";
				boolean checkExternal = false;
				boolean randomizePositions = false;
				
				
				if(interactiePanelLaunchState.containsKey("keuzeMogelijkheden")) keuzeMogelijkheden = (String[])interactiePanelLaunchState.get("keuzeMogelijkheden");
                if(interactiePanelLaunchState.containsKey("antwoordString")) antwoordString = (String)interactiePanelLaunchState.get("antwoordString");
				if(interactiePanelLaunchState.containsKey("startString")) startString = (String)interactiePanelLaunchState.get("startString");
				if(interactiePanelLaunchState.containsKey("scoreMax")) scoreMax = ((Integer)interactiePanelLaunchState.get("scoreMax")).intValue();
				if(interactiePanelLaunchState.containsKey("answerModels")) answerModels = (Hashtable[])interactiePanelLaunchState.get("answerModels");
				if(interactiePanelLaunchState.containsKey("hasFeedback")) hasFeedback = ((Boolean)interactiePanelLaunchState.get("hasFeedback")).booleanValue();
				if(interactiePanelLaunchState.containsKey("check")) check = ((Boolean)interactiePanelLaunchState.get("check")).booleanValue();
				if(interactiePanelLaunchState.containsKey("teltMee")) teltMee = ((Boolean)interactiePanelLaunchState.get("teltMee")).booleanValue();
				if(interactiePanelLaunchState.containsKey("logOption")) logOption = ((Boolean)interactiePanelLaunchState.get("logOption")).booleanValue();
				if(interactiePanelLaunchState.containsKey("logID")) logID = (String)interactiePanelLaunchState.get("logID");
				if(interactiePanelLaunchState.containsKey("logIDLabel")) logIDLabel = (String)interactiePanelLaunchState.get("logIDLabel");
				if(interactiePanelLaunchState.containsKey("checkExternal")) checkExternal = ((Boolean)interactiePanelLaunchState.get("checkExternal")).booleanValue();
		        if(interactiePanelLaunchState.containsKey("randomizePositions")) randomizePositions = ((Boolean)interactiePanelLaunchState.get("randomizePositions")).booleanValue();
				
				aantalKeuzes = keuzeMogelijkheden.length;
			    aantalKeuzesTF.setText(""+aantalKeuzes);
			    maxScorePV.setText(""+scoreMax);
			     
			    maakKeuzeVelden();
			    
			    for(int i=0 ; i<aantalKeuzes ; i++)
			    {   
//			    	/*selectableCheckboxes[i] = new JCheckBox();//("Nr "+(i+1));
//		            selectableCheckboxes[i].setOpaque(false);
//		            selectableCheckboxes[i].setBounds(10,80+i*85,20,80);
//		            add(selectableCheckboxes[i],0);*/
//		           
//			    	
//			    	keuzeVelden[i] = new TekstEditor();
			        keuzeVelden[i].zetTekst(keuzeMogelijkheden[i]);
			        keuzeVelden[i].layoutTekst();
//			        keuzeVelden[i].setBounds(30,80+i*85,170,80);
//			        add(keuzeVelden[i],0);
				}
			    
			   
				
			    this.answerModels = new Hashtable[answerModels.length];
				for(int i=0 ; i<answerModels.length ; i++)
				{	this.answerModels[i] = answerModels[i];
				}
					
					//this.answerModels = answerModels;
				
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
				}
				
				antwoordvak.zetTekst(antwoordString);
				antwoordvak.layoutTekst();
				maxScorePV.setText(""+scoreMax);
				
				checkCB.setSelected(check);
                teltMeeCB.setSelected(teltMee);
                logCB.setSelected(logOption);
                    
                logIDField.setVisible(logOption);
                logIDLabelField.setVisible(logOption);
	            logIDLabelLabel.setVisible(logOption);
                //logObjectivesButton.setVisible(logOption);
                logIDField.setText(logID);
                logIDLabelField.setText(logIDLabel);
                logObjectivesButton.setEditState(interactiePanelLaunchState);
                
				setFeedbackOption(hasFeedback);
				feedbackCB.setSelected(hasFeedback);
				//if(hasFeedback)return;
				
				checkExternalCB.setSelected(checkExternal);
				randomizePositionsCB.setSelected(randomizePositions);
				
				((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)mainPanel)).packWidth();

		
	}
	
	public Hashtable getEditState()
	{	
		Hashtable interactiePanelLaunchState = new Hashtable();
        
		    // boolean randomizePositions = false;
		    String[] keuzeMogelijkheden = null;
	        String antwoordString = null;
			int scoreMax = 0;
			Hashtable[] answerModels;
			boolean hasFeedback;
			boolean check = true;
			boolean teltMee = true;
			boolean logOption = false;
			String logID = "";
			String logIDLabel = "";
			boolean checkExternal = false;
			boolean randomizePositions = false;
			
			keuzeMogelijkheden = new String[aantalKeuzes];
			for(int i=0 ; i<aantalKeuzes ; i++)
	        {   keuzeMogelijkheden[i] = keuzeVelden[i].getCompleteText();
	        }
			getAnswerModel();
			answerModels = this.answerModels;
			if(answerModels!=null)setAnswerModel(answerModels[0]);
			
			antwoordString = antwoordvak.getCompleteText();
			scoreMax = Integer.parseInt(maxScorePV.getText());
			hasFeedback = this.hasFeedback;
			if(hasFeedback)scoreMax = puntenFeedback;
			
			check = checkCB.isSelected();
			teltMee = teltMeeCB.isSelected();
			logOption = logCB.isSelected();
			logID = logIDField.getText();	
			logIDLabel = logIDLabelField.getText();
			
			checkExternal = checkExternalCB.isSelected();
			randomizePositions = randomizePositionsCB.isSelected();
			
			interactiePanelLaunchState.put("keuzeMogelijkheden",keuzeMogelijkheden);
			interactiePanelLaunchState.put("antwoordString",antwoordString);
			interactiePanelLaunchState.put("scoreMax",(scoreMax));
			if(answerModels!=null)interactiePanelLaunchState.put("answerModels",answerModels);
			interactiePanelLaunchState.put("hasFeedback",(hasFeedback));
			interactiePanelLaunchState.put("check",(check));
			interactiePanelLaunchState.put("teltMee",(teltMee));
			interactiePanelLaunchState.put("logOption",(logOption));
			interactiePanelLaunchState.put("logID",logID);
			interactiePanelLaunchState.put("logIDLabel",logIDLabel);
			interactiePanelLaunchState.put("checkExternal",checkExternal);
			if (randomizePositions)
			  interactiePanelLaunchState.put("randomizePositions", randomizePositions);
			
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
    {	antwoordvak.setNewScrollSize();
	}
    
	public void focusLost(FocusEvent e)
	{
		if(e.getSource()==aantalKeuzesTF)
        {
            aantalKeuzes = Math.min(20, Integer.parseInt(aantalKeuzesTF.getText()));
            maakKeuzeVelden();
        }
	}
	
	public void focusGained(FocusEvent e)
	{
		
	}
	
	public void actionPerformed(ActionEvent e)
	{	
		if(e.getSource() instanceof HelpButton)
		{
			OpdrNavStructEdit.helpBrowser.loadURL(((HelpButton)e.getSource()).getURL());
		}
		else if(e.getSource()==aantalKeuzesTF)
        {
            aantalKeuzes = Math.min(20, Integer.parseInt(aantalKeuzesTF.getText()));
            maakKeuzeVelden();
            ((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)mainPanel)).packWidth();
    		
        }
	    if(e.getSource() == tabbladTab)
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
		}
		else if(e.getSource()==feedbackCB)
		{	setFeedbackOption(feedbackCB.isSelected());
			((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)mainPanel)).packWidth();
		}
		else if(e.getSource()==logCB)
	    {   logIDField.setVisible(logCB.isSelected());
	    	logIDLabelField.setVisible(logCB.isSelected());	
	    	logIDLabelLabel.setVisible(logCB.isSelected());
	    	//logObjectivesButton.setVisible(logCB.isSelected());
	    }
		
		//else if(e.getSource()==gelijkwaardigCB)
		//{	gelijkwaardig = gelijkwaardigCB.isSelected();
		//	if(!hasFeedback && answerModelNr==0)gelijkwaardigPV.setVisible(gelijkwaardig);
			
		//}
		
		else
		{
			int puntenGelijkwaardig = 0;
			int puntenHerleiding = 0;
			int puntenExact = 0;
			int puntenEindOplossing = 0;
			
			/*if(e.getSource()==gelijkwaardigPV)
			{	try
				{	puntenGelijkwaardig = Integer.parseInt(gelijkwaardigPV.getText());
					this.puntenGelijkwaardig = puntenGelijkwaardig;
				}	
				catch(Exception ex)
				{	}
			}*/
			
			
			
			
			 
		}
	}
	
	
	public void setFeedbackOption(boolean b)
	{
		hasFeedback = b;
		tabbladTab.setVisible(b);
		feedbackEditor.setVisible(b);
		if(feedbackBox!=null)
			feedbackBox.setVisible(b);
		feedbackLabel.setVisible(b);
		aantalTabsKnop.setVisible(b);
		tabPositieKnop.setVisible(b);
		feedbackPV.setVisible(b);
		goedFoutIP.setVisible(b);
		maxScorePV.setVisible(!b);
		if(scoringBox!=null)
			scoringBox.setVisible(b);
		answerModelNr = 0;
		tabbladTab.setSelected(answerModelNr+1);
		if(b)setAnswerModel();
	}
	
	public void zetTabletUser(FormuleVakHouder formuleVakHouder)
	{	if(tablet==null) return;
		tablet.zetFormuleVakHouder(formuleVakHouder);
		//tabletUser = formuleVakHouder;
		
	}
	
	public void zetTablet(FormuleVakHouder formuleVakHouder, int x, int y)
	{	if(tablet==null) 
		{	tablet = new Tablet(formuleVakHouder);
			tablet.setLocation(x,y);
			
		}
		tablet.zetFormuleVakHouder(formuleVakHouder);
		//tabletUser = formuleVakHouder;
		
		
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
	{	
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
 	
 	public class EditorComponentListener extends ComponentAdapter implements ComponentListener {

	      @Override
	      public void componentResized(ComponentEvent e) {
	    	  if(e.getSource()==antwoordEditorPanel) {
	    		  int w = antwoordEditorPanel.getWidth();
	    		  int h = antwoordEditorPanel.getHeight();
	    		  antwoordvak.setBounds(0,20,w,h-20);
	    	  }
	      }
	}
 	@Override
	public void showHelpButtons(boolean b) {
		hbCheck.setVisible(b);
    	hbTeltMee.setVisible(b);
    	hbLogID.setVisible(b);
    	hbFeedback.setVisible(b);
    	hbKeuze.setVisible(b);
    	hbExtern.setVisible(b);
    	hbAntwoord.setVisible(b);
    	hbScore.setVisible(b);
    	hbFeedbackTitel.setVisible(b);
        
	}

	@Override
	public String geefHelpURL() {
		return HELP_14_URL;
	}

  @Override
  public Number get() {
    return Float.valueOf(1.0f / aantalKeuzes);
  }
}

