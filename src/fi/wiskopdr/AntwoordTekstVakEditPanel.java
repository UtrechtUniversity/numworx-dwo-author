package fi.wiskopdr;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import fi.beans.base64code.*;
import fi.wiskopdr.tekstobjects.*;
import fi.wiskopdr.formuleobjects.*;
import fi.wiskopdr.AntwoordFormuleVakEditPanel.EditorComponentListener;
import fi.wiskopdr.domainmodel.Constants;
import fi.wiskopdr.expressies.*;
import fi.wiskopdr.opdrnav.*;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;


public class AntwoordTekstVakEditPanel extends JLayeredPane implements InteractieEditPanel, ActionListener,  MouseListener, MouseMotionListener, HelpButtonPanelIF
{	
	// Algemene attributen 
    private Font font = new Font("SansSerif",Font.PLAIN,12);//WiskOpdr.tekstFont;
	 
    private Tablet tablet;
    private boolean tabletAdded;
    private FormuleVakHouder tabletUser;
	
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
 	private JLabel titleFeedbackTekstLabel;
 	private ActKeuzePanel goedFoutIP;
 	private Box feedbackBox;
   	private DialogFacade feedbackEditorPopupFrame;
   	
   	// Verificatie
  	private JLabel titleVerificatieLabel;
  	private JLabel titleVerificatieScoreLabel;
  	private Box verificatieBox;
  	    
    private int puntenGelijkwaardig = 0;
    private int puntenFeedback = 0;
      
    // Score
    private JLabel titleScoreLabel;
    private JLabel ScoringLabel; // overbodig?
    private Box scoringBox;
    private JTextField  feedbackPV;
    private JTextField scoreMaxPV;
    
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
  	private JCheckBox formuleModeCB;
	
  	private JCheckBox formuleToolBijFocusCB;
    private JCheckBox feedbackCB;
    private boolean formuleToolBijFocus;
    private boolean hasFeedback;
    
    // Opmaak
  	private JLabel titleOpmaakLabel;
  	private JCheckBox boxMetRandCB;
    
  	// Helpbuttons
    private static String HELP_13_URL = WiskOpdr.rb.getString("HELP_13_URL");
    private static String HELP_13_URL_CHECK = WiskOpdr.rb.getString("HELP_13_URL_CHECK");
    private static String HELP_13_URL_TELTMEE = WiskOpdr.rb.getString("HELP_13_URL_TELTMEE");
    private static String HELP_13_URL_LOGID = WiskOpdr.rb.getString("HELP_13_URL_LOGID");
    private static String HELP_13_URL_FEEDBACK = WiskOpdr.rb.getString("HELP_13_URL_FEEDBACK");
    private static String HELP_13_URL_FORMINVOER = WiskOpdr.rb.getString("HELP_13_URL_FORMINVOER");
    private static String HELP_13_URL_RAND = WiskOpdr.rb.getString("HELP_13_URL_RAND");
    private static String HELP_13_URL_ANTWOORD = WiskOpdr.rb.getString("HELP_13_URL_ANTWOORD");
    private static String HELP_13_URL_FEEDBACKTITLE = WiskOpdr.rb.getString("HELP_13_URL_FEEDBACKTITLE");
    
    private HelpButton hbCheck;
    private HelpButton hbTeltMee;
    private HelpButton hbLogID;
    private HelpButton hbFeedback;
    private HelpButton hbFormInvoer;
    private HelpButton hbRand;
    private HelpButton hbAntwoord;
    private HelpButton hbFeedbackTitel; 
    
  	//Overige (wellicht overbodig geworden)
	private JLabel antwoordLabel,  feedbackLabel;
	private JLabel  checkTotaalLabel;
		
	
	public AntwoordTekstVakEditPanel()
	{	setLayout(new BorderLayout());
		super.setSize(770,520); //voor dwo
		setBackground(Color.white);		
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
		
		// GUI antwoordBox
        titleAntwoordLabel = new JLabel(WiskOpdr.rb.getString("FEV_titleAntwoordLabel"));
    	titleAntwoordLabel.setForeground(WiskOpdr.colorBlue1);
    	titleAntwoordLabel.setFont(font.deriveFont(Font.BOLD, 16));
    	titleAntwoordLabel.setBounds(0,-3,140,20);
    	
        antwoordvak = new TekstEditor();
        antwoordvak.setBounds(0,20,435,150);
        antwoordvak.setFont(font);
        antwoordvak.addActionListener(this);
        antwoordvak.setResizable(true);
        
        antwoordEditorPanel = new JPanel();
        antwoordEditorPanel.setLayout(null);
        antwoordEditorPanel.add(titleAntwoordLabel);
        antwoordEditorPanel.add(antwoordvak);
        antwoordEditorPanel.addComponentListener(new EditorComponentListener());
        antwoordEditorPanel.setPreferredSize(new Dimension(600,170));
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
        	
        //GUI Feedback editor
        titleFeedbackTekstLabel = new JLabel(WiskOpdr.rb.getString("FEV_titleFeedbackLabel"));
        titleFeedbackTekstLabel.setForeground(WiskOpdr.colorBlue1);
        titleFeedbackTekstLabel.setFont(font.deriveFont(Font.BOLD, 16));
    	
        feedbackEditor = new TekstEditor(false,true,true);
        feedbackEditor.setPreferredSize(new Dimension(400,120));
        feedbackEditor.setMaximumSize(new Dimension(2280,120));
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
    	scoreMaxPV = makeTextField(460,385,30,20,"0",false);
		
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
        ScoringLabel = makeLabel(320,385,160,20,WiskOpdr.rb.getString("score"),true);
        logIDLabelLabel = makeLabel(470,25,50,20,WiskOpdr.rb.getString("TVEP_logIDLabelLabel"),false);
		
        logObjectivesButton = new ObjectiveChoiceButton(WiskOpdr.objectives, WiskOpdr.categorieString, WiskOpdr.studentModel);
        logObjectivesButton.setVisible(WiskOpdr.objectives!=null || WiskOpdr.studentModel!=null);
        logObjectivesButton.setBounds(600,5,120,20);
        logObjectivesButton.setPreferredSize(new Dimension(120,22));
        logObjectivesButton.setMaximumSize(new Dimension(120,22));
		        
        // Hulp setting
        titleHulpLabel = new JLabel(WiskOpdr.rb.getString("FEV_titleHulpLabel"));
    	titleHulpLabel.setForeground(WiskOpdr.colorBlue1);
    	titleHulpLabel.setFont(font.deriveFont(Font.BOLD, 16));
    	
    	feedbackCB = makeCheckBox(140,-2,80,20,WiskOpdr.rb.getString("feedbackCBLabel"),false,true);
    	formuleModeCB = makeCheckBox(500,50,200,20,WiskOpdr.rb.getString("formuleInvoerModeCBLabel"),false,true);
        formuleToolBijFocusCB = makeCheckBox(530,50,270,20,WiskOpdr.rb.getString("formuleToolCBLabel"),false,false);
		         
        // GUI Opmaak box
        titleOpmaakLabel = new JLabel(WiskOpdr.rb.getString("FEV_titleOpmaakLabel"));
    	titleOpmaakLabel.setForeground(WiskOpdr.colorBlue1);
    	titleOpmaakLabel.setFont(font.deriveFont(Font.BOLD, 16));
    	
    	boxMetRandCB = makeCheckBox(690,95,80,20,WiskOpdr.rb.getString("boxMetRand"),true,true);
    	        
		        
    	//Overige zaken, wellicht overbodig
		antwoordLabel = makeLabel(5,160,770,20,WiskOpdr.rb.getString("antwoordLabel"),true);
		feedbackLabel = makeLabel(20,330,320,20,WiskOpdr.rb.getString("feedbackLabel"),true);
		ScoringLabel = makeLabel(320,385,160,20,WiskOpdr.rb.getString("score"),true);
		checkTotaalLabel = makeLabel(620,385,160,20,WiskOpdr.rb.getString("checkTotaalLabel"),false);
		checkTotaalLabel.setForeground(Color.red);
		
		hbCheck = makeHelpButton(HELP_13_URL_CHECK);
	    hbTeltMee = makeHelpButton(HELP_13_URL_TELTMEE);
	    hbLogID = makeHelpButton(HELP_13_URL_LOGID);
	    hbFeedback = makeHelpButton(HELP_13_URL_FEEDBACK);
	    hbFormInvoer = makeHelpButton(HELP_13_URL_FORMINVOER);
	    hbRand = makeHelpButton(HELP_13_URL_RAND);
	    hbAntwoord = makeHelpButton(HELP_13_URL_ANTWOORD);
	    hbFeedbackTitel = makeHelpButton(HELP_13_URL_FEEDBACKTITLE); 
		
	    hbAntwoord.setBounds(140,-2,18,18);
    	antwoordEditorPanel.add(hbAntwoord,0);
    	
		removeAll();
	    plaatsGUI();
	    add(mainPanel);		
	}
	
	public void plaatsGUI() {
		// plaats compoenenten antwoordbox
		Component[] r31 = {ra(0,130), 	antwoordEditorPanel};
		Component[] k3 = {hb(r31)};
		antwoordBox = vb(k3);
		
		// plaatsComponenten settingBox
		Component[] r41 = {titleLoggingLabel, 	hgl()};
		Component[] r42 = {checkCB, 			ra(5,0),	hgl(),	hbCheck};
		Component[] r43 = {teltMeeCB, 			ra(5,0),	hgl(),	hbTeltMee};
		Component[] r44 = {logCB, 				ra(5,10), logIDField, ra(5,10), logIDLabelLabel, ra(5,10), logIDLabelField, ra(5,0),	hgl(),	hbLogID};
		Component[] r45 = {ra(6,0),			logObjectivesButton, hgl()};
		Component[] r46 = {titleHulpLabel, 		hgl()};
		Component[] r47 = {feedbackCB, 			ra(5,0),	hgl(),	hbFeedback};
		Component[] r48 = {formuleModeCB, 		ra(5,0),	hgl(),	hbFormInvoer};
		Component[] r49 = {formuleToolBijFocusCB, hgl()};
		Component[] r422 = {titleOpmaakLabel, 	hgl()};
		Component[] r424 = {boxMetRandCB, 		ra(5,0),	hgl(),	hbRand};
		
		Component[] k4 = {hb(r41),vst(5),hb(r42),hb(r43),hb(r44),vst(3),hb(r45),vst(20),hb(r46),vst(5),hb(r47),hb(r48),hb(r49),
					 vst(20), hb(r422),vst(5),hb(r424), vgl()};
		Box settingsBox = vb(k4);
		
		// plaats componenten feedback box
		Component[] r51 = {titleFeedbackTekstLabel, 		ra(5,10),	hgl(), hbFeedbackTitel};
		Component[] r52 = {ra(0,110),				hgl(),  		feedbackEditor};
		
		Component[] k5 = {hb(r51),vst(5),hb(r52), vgl()};
		Component[] h5 = {ra(20,10),vb(k5)};
		feedbackBox = hb(h5);
				
		// plaats componenten verificatie box
		Component[] r61 = {titleScoreLabel, 	ra(10,10),		scoreMaxPV,hgl()}; //titleVerificatieLabel, 	ra(10,10),		hgl(),		
		
		Component[] k6 = {hb(r61), vst(25), vgl()};
		Component[] h6 = {vb(k6), hgl(), hgl()};
		verificatieBox = hb(h6);
		    			
		//plaats componenten scoringbox
        Component[] r71 = {ra(10,10), 		feedbackPV, 	hgl()};
        Component[] r73 = {goedFoutIP, 		hgl()};
		
		Component[] k7 = {hb(r71),  vst(20), hb(r73), vgl()};
		scoringBox = vb(k7);
				
		
		// boxes plaatsen
		Box boxh = Box.createHorizontalBox();
		mainPanel.add(boxh);
		
		Box boxv1 = Box.createVerticalBox();
		boxh.add(boxv1);
		boxh.add(Box.createHorizontalStrut(20));
		boxh.add(settingsBox);
		
		//scoringBox.setVisible(false);
		feedbackBox.setVisible(false);
		
		Box boxh1 = Box.createHorizontalBox();
		Box boxh2 = Box.createHorizontalBox();
		Box boxh3 = Box.createHorizontalBox();
		
		
		boxv1.add(boxh2);
		boxv1.add(Box.createVerticalStrut(20));
		boxv1.add(boxh3);
		
		
		boxh2.add(antwoordBox);
		
		boxh3.add(verificatieBox);
		boxh3.add(Box.createHorizontalStrut(5));
		boxh3.add(scoringBox);
		boxh3.add(Box.createHorizontalStrut(10));
		boxh3.add(Box.createHorizontalGlue());
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
	
	private void updateFeedbackTitelLabel()
    {	String feedbackNrString = "";
    	if(hasFeedback && answerModelNr>0) {
    		feedbackNrString += (answerModelNr+1);
    		titleFeedbackTekstLabel.setText(WiskOpdr.rb.getString("FEV_titleFeedbackLabel") + " " + feedbackNrString);
	    	titleAntwoordLabel.setText(WiskOpdr.rb.getString("FEV_titleAntwoordNrLabel") + " " + feedbackNrString);
	    	titleScoreLabel.setText(WiskOpdr.rb.getString("FEV_titleScoringLabel") + " " + feedbackNrString);
	    	scoringBox.validate();
	   }
    	else {
    		titleFeedbackTekstLabel.setText(WiskOpdr.rb.getString("FEV_titleFeedbackLabel"));
	    	titleAntwoordLabel.setText(WiskOpdr.rb.getString("FEV_titleAntwoordLabel"));
	    	titleScoreLabel.setText(WiskOpdr.rb.getString("FEV_titleScoringLabel"));
	    	scoringBox.validate();
	   }
    }
	
	public void zetBreedte(int b)
	{	//grafiekPanel.setSize(b,grafiekPanel.getSize().height);
	}
	public void zetHoogte(int h)
	{	//grafiekPanel.setSize(grafiekPanel.getSize().width, h);
	}
	
	private void fillAnswerModel(Hashtable h)
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
		h.put("puntenFeedback",new Integer(puntenFeedback));
		h.put("feedback",feedback);
		h.put("goedHalfFout",new Integer(goedHalfFout));
		
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
		if(answerModels[answerModelNr]==null) answerModels[answerModelNr] = new Hashtable();
		fillAnswerModel(answerModels[answerModelNr]);
	}
	
	private void setAnswerModel()
	{	if(answerModels==null)return;
		setAnswerModel(answerModels[answerModelNr]);	
	}
	
			
	public void setEditState(Hashtable interactiePanelLaunchState)
	{			
					
		String antwoordString = "$f@";
		String startString = "$f@";
		int scoreMax = 10;
		Hashtable[] answerModels = null;
		boolean hasFeedback = false;
		boolean subKnop = false;
		boolean check = true;
		boolean teltMee = true;
		boolean logOption = false;
		String logID = "";
		String logIDLabel = "";
		boolean formuleMode = false;
		boolean formuleToolBijFocus = false;
		boolean boxMetRand = true;
		boolean[][] logObjectives = null;
		String[] smObjectives = null;
        
		
		
		
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
		if(interactiePanelLaunchState.containsKey("formuleMode")) formuleMode = ((Boolean)interactiePanelLaunchState.get("formuleMode")).booleanValue();
		if(interactiePanelLaunchState.containsKey("formuleToolBijFocus")) formuleToolBijFocus = ((Boolean)interactiePanelLaunchState.get("formuleToolBijFocus")).booleanValue();
		if(interactiePanelLaunchState.containsKey("boxMetRand")) boxMetRand = ((Boolean)interactiePanelLaunchState.get("boxMetRand")).booleanValue();
		if(interactiePanelLaunchState.containsKey("logObjectives")) logObjectives = (boolean[][])interactiePanelLaunchState.get("logObjectives");
		if(interactiePanelLaunchState.containsKey(Constants.OBJECTIVES)) smObjectives = (String[]) interactiePanelLaunchState.get(Constants.OBJECTIVES);
		this.puntenGelijkwaardig = puntenGelijkwaardig;
		this.answerModels = answerModels;
		
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
		scoreMaxPV.setText(""+scoreMax);
		
		setFeedbackOption(hasFeedback);
		feedbackCB.setSelected(hasFeedback);
		//if(hasFeedback)return;
		
		checkCB.setSelected(check);
        teltMeeCB.setSelected(teltMee);
        logCB.setSelected(logOption);
        logIDField.setVisible(logOption);
        logIDLabelField.setVisible(logOption);
        logIDLabelLabel.setVisible(logOption);
        //logObjectivesButton.setVisible(logOption);
        logIDField.setText(logID);
        logIDLabelField.setText(logIDLabel);
        logObjectivesButton.setChoices(logObjectives);
        logObjectivesButton.setObjectives(smObjectives);
       
        formuleModeCB.setSelected(formuleMode);
        formuleToolBijFocusCB.setVisible(formuleMode);
        formuleToolBijFocusCB.setSelected(formuleToolBijFocus);
        boxMetRandCB.setSelected(boxMetRand);
        
        ((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)mainPanel)).packWidth();

        
	}
	
	public Hashtable getEditState()
	{	
		Hashtable interactiePanelLaunchState = new Hashtable();
        
        
		String antwoordString = null;
		int scoreMax = 0;
		int[][] scoreMaxObjectives = null;
		Hashtable[] answerModels;
		boolean hasFeedback;
		boolean check = true;
		boolean teltMee = true;
		boolean logOption = false;
		String logID = "";
		String logIDLabel = "";
		boolean[][] logObjectives = null;
		String[] smObjectives = null;
		
		boolean formuleMode = false;
		boolean formuleToolBijFocus = false;
		boolean boxMetRand = true;		
		
		getAnswerModel();
		answerModels = this.answerModels;
		if(answerModels!=null)setAnswerModel(answerModels[0]);
		
		antwoordString = antwoordvak.getText().trim();
		scoreMax = Integer.parseInt(scoreMaxPV.getText());
		hasFeedback = this.hasFeedback;
		if(hasFeedback)scoreMax = puntenFeedback;
		
		check = checkCB.isSelected();
		teltMee = teltMeeCB.isSelected();
		logOption = logCB.isSelected();
		logID = logIDField.getText();
		logIDLabel = logIDLabelField.getText();
		logObjectives = logObjectivesButton.getChoices();
		smObjectives = logObjectivesButton.getObjectives();
		formuleMode = formuleModeCB.isSelected();
		formuleToolBijFocus = formuleToolBijFocusCB.isSelected();
		boxMetRand = boxMetRandCB.isSelected();
		
		if(logObjectives!=null)
		{	scoreMaxObjectives = new int[logObjectives.length][];
			for(int j=0 ; j<scoreMaxObjectives.length; j++)
			{	scoreMaxObjectives[j] = new int[logObjectives[j].length];
				for(int i=0 ; i<scoreMaxObjectives[j].length ; i++)
				{	if(logObjectives[j][i]) scoreMaxObjectives[j][i] = scoreMax;
				}
			}
		}
		
		interactiePanelLaunchState.put("antwoordString",antwoordString);
		interactiePanelLaunchState.put("scoreMax",new Integer(scoreMax));
		if(answerModels!=null)interactiePanelLaunchState.put("answerModels",answerModels);
		interactiePanelLaunchState.put("hasFeedback",new Boolean(hasFeedback));
		interactiePanelLaunchState.put("check",new Boolean(check));
		interactiePanelLaunchState.put("teltMee",new Boolean(teltMee));
		interactiePanelLaunchState.put("logOption",new Boolean(logOption));
		interactiePanelLaunchState.put("logID",logID);
		interactiePanelLaunchState.put("logIDLabel",logIDLabel);
		interactiePanelLaunchState.put("formuleMode",new Boolean(formuleMode));
		interactiePanelLaunchState.put("formuleToolBijFocus",new Boolean(formuleToolBijFocus));
		interactiePanelLaunchState.put("boxMetRand",new Boolean(boxMetRand));
		if(logObjectives!=null)
        {	interactiePanelLaunchState.put("logObjectives",logObjectives);
        	interactiePanelLaunchState.put("scoreMaxObjectives",scoreMaxObjectives);
            try {
              interactiePanelLaunchState.put(Constants.OBJECTIVES, smObjectives);
            } catch(Exception e) {}
        }
		
		
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
		}
		else if(e.getSource()==feedbackCB)
		{	setFeedbackOption(feedbackCB.isSelected());
			((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)mainPanel)).packWidth();
		}
		
		else if(e.getSource()==formuleModeCB)
		{	formuleToolBijFocusCB.setVisible(formuleModeCB.isSelected());
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
		scoreMaxPV.setVisible(!b);
		answerModelNr = 0;
		tabbladTab.setSelected(answerModelNr+1);
		if(b)setAnswerModel();
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
 	
 	public class EditorComponentListener implements ComponentListener {

	      @Override
	      public void componentResized(ComponentEvent e) {
	    	  if(e.getSource()==antwoordEditorPanel) {
	    		  int w = antwoordEditorPanel.getWidth();
	    		  int h = antwoordEditorPanel.getHeight();
	    		  antwoordvak.setBounds(0,20,w,h-20);
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
    	hbFormInvoer.setVisible(b);
    	hbRand.setVisible(b);	
    	hbAntwoord.setVisible(b);
    	hbFeedbackTitel.setVisible(b);
  	}

	@Override
	public String geefHelpURL() {
		return HELP_13_URL;
	}
}

