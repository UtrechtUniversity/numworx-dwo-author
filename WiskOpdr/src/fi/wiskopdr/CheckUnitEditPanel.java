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
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;

import javax.swing.*;

import fi.wiskopdr.domainmodel.Constants;
import fi.wiskopdr.formuleobjects.*;
import fi.wiskopdr.opdrnav.ActKeuzePanel;
import fi.wiskopdr.opdrnav.OpdrNavStructEdit;
import fi.wiskopdr.opdrnav.OpdrachtNrRij;
import fi.wiskopdr.opdrnav.PlusMinKnop;
import fi.wiskopdr.tekstobjects.EditInteractiePanelDialog;
import fi.wiskopdr.tekstobjects.TekstEditor;
import fi.wiskopdr.tekstobjects.TekstImageVak;
import fi.wiskopdr.tekstobjects.TekstVak;
import fi.wiskopdr.templatecomponents.MultipleChoiceGenerator;
import fi.beans.iconan.Iconan;
import fi.beans.wiskopdrbeans.*;

public class CheckUnitEditPanel extends JPanel implements InteractieEditPanel, ActionListener, FocusListener, HelpButtonPanelIF
{
	// Algemene attributen 
    private Font font = new Font("SansSerif",Font.PLAIN,12);//WiskOpdr.tekstFont;
    private int scoreMax;
   	private boolean multiselections;
   	private int aantalSelectables = 4;
   	private int aantalSelectablesMax = 30;
   	private boolean[][][] logMisconceptions;
	
    // Basis GUI
    private JPanel mainPanel;
    
    //mainPanel  //juiste antwoord
  	private JLabel titleAntwoordLabel;
  	private JPanel antwoordEditorPanel;
  	private Component antwoordPanelBox;
  	private JCheckBox[] selectableCheckboxes;
  	private ButtonGroup buttonGroup = new ButtonGroup();
  	private Box selectableCBBox;
  	private FormuleEditor formuleEditor;
  	private ObjectiveChoiceButton[] logMisconceptionsButtons;
  	
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
  	
  	// instellingen
 	private JLabel titleSettingsLabel;
 	private JLabel itemCountLabel;
 	private JTextField itemCountTF;
 	private JCheckBox multiSelectionsCB;
 	private JCheckBox randomizePositionsCB;
	private JCheckBox checkFormuleCB;
	private JLabel imageKnopLabel;
 	private FormuleButton knopImageButton;
 	private Image knopImage;
 	private Dialog imageDialog;
 	private Iconan iconman;
 	private String knopImageString = "";
 	private JCheckBox feedbackCB;
    private boolean hasFeedback;
    private JTextField  feedbackPV;
 	
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
 	
 	// Helpbuttons
    private static String HELP_12_URL = WiskOpdr.rb.getString("HELP_12_URL");
    private static String HELP_12_URL_CHECK = WiskOpdr.rb.getString("HELP_12_URL_CHECK");
    private static String HELP_12_URL_TELTMEE = WiskOpdr.rb.getString("HELP_12_URL_TELTMEE");
    private static String HELP_12_URL_LOGID = WiskOpdr.rb.getString("HELP_12_URL_LOGID");
    private static String HELP_12_URL_ANTWOORD = WiskOpdr.rb.getString("HELP_12_URL_ANTWOORD");
    private static String HELP_12_URL_MEERVOUDIG = WiskOpdr.rb.getString("HELP_12_URL_MEERVOUDIG");
    private static String HELP_12_URL_RANDOM = WiskOpdr.rb.getString("HELP_12_URL_RANDOM");
    private static String HELP_12_URL_CHECKFORMULE = WiskOpdr.rb.getString("HELP_12_URL_CHECKFORMULE");
    private static String HELP_12_URL_KNOPIMAGE = WiskOpdr.rb.getString("HELP_12_URL_KNOPIMAGE");
    
    private HelpButton hbCheck = makeHelpButton(HELP_12_URL_CHECK);
    private HelpButton hbTeltMee = makeHelpButton(HELP_12_URL_TELTMEE);
    private HelpButton hbLogID = makeHelpButton(HELP_12_URL_LOGID);
    private HelpButton hbAntwoord = makeHelpButton(HELP_12_URL_ANTWOORD);
    private HelpButton hbMeervoudig = makeHelpButton(HELP_12_URL_MEERVOUDIG);
    private HelpButton hbRandom = makeHelpButton(HELP_12_URL_RANDOM);
    private HelpButton hbCheckFormule = makeHelpButton(HELP_12_URL_CHECKFORMULE);
    private HelpButton hbKnopImage = makeHelpButton(HELP_12_URL_KNOPIMAGE); 
   
 	
	//private InteractiePanel[] selectables;
	//private JLabel[] selectableLabels;
	//private boolean randomizePositions;
	
	public CheckUnitEditPanel()
	{
		setLayout(new BorderLayout());
		//setBounds(0,0,780,480);
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
		
		selectableCheckboxes = new JCheckBox[aantalSelectablesMax];
		logMisconceptionsButtons = new ObjectiveChoiceButton[aantalSelectablesMax];
		selectableCBBox = Box.createVerticalBox();
		maakCheckboxes();
		
		formuleEditor = new FormuleEditor(true);
		formuleEditor.setBounds(0,20,350,200);
		formuleEditor.setMultiLine(true);
		//formuleEditor.setPreferredSize(new Dimension(340,200));
		formuleEditor.setVisible(false);
		
        
		antwoordEditorPanel = new JPanel();
        antwoordEditorPanel.setLayout(null);
        antwoordEditorPanel.add(titleAntwoordLabel);
        antwoordEditorPanel.add(formuleEditor);
        antwoordEditorPanel.setBorder(BorderFactory.createMatteBorder(0,0,1,0,WiskOpdr.colorBlue3));
        //antwoordEditorPanel.add(antwoordvak);
        antwoordEditorPanel.addComponentListener(new EditorComponentListener());
        antwoordEditorPanel.setPreferredSize(new Dimension(150,24));
        antwoordEditorPanel.setMaximumSize(new Dimension(2835,220));
        
        antwoordPanelBox = ra(0,200);
        antwoordPanelBox.setVisible(false);
        
        tabbladTab = new OpdrachtNrRij(aantalAnswerModels, 150,20);
        tabbladTab.setSize(tabbladTab.getSize().width, 23);
        tabbladTab.setTab(true);
        tabbladTab.setScoresVisible(false);
        tabbladTab.addActionListener(this);
        tabbladTab.setBackground(new Color(210,210,210));
        tabbladTab.setSelected(1);
        tabbladTab.setVisible(false);
        antwoordEditorPanel.add(tabbladTab,0);
        
        aantalTabsKnop = new PlusMinKnop(150+25*aantalAnswerModels+5 ,24,20,16,PlusMinKnop.HORIZONTAAL);
        aantalTabsKnop.setBackground(new Color(210,210,210));
        aantalTabsKnop.addActionListener(this);
        aantalTabsKnop.setVisible(false);
        antwoordEditorPanel.add(aantalTabsKnop,0);
        
        tabPositieKnop = new PlusMinKnop(146+25*answerModelNr+5 ,0,20,16,PlusMinKnop.HORIZONTAAL);
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
		itemCountLabel = makeLabel(WiskOpdr.rb.getString("aantalSelectieObjLabel"), font);
		itemCountTF = makeTextField(""+aantalSelectables, 30, 22, this);
		multiSelectionsCB = makeCheckBox(WiskOpdr.rb.getString("meervSelectiesLabel"), false, this);//("Meervoudige selecties mogelijk");
		randomizePositionsCB = makeCheckBox(WiskOpdr.rb.getString("randomPosLabel"), false, this);
		checkFormuleCB = makeCheckBox(WiskOpdr.rb.getString("checkViaFormuleLabel"), false, this);
		imageKnopLabel = makeLabel(WiskOpdr.rb.getString("editImageKnopLabel"), font);
		
		knopImageButton = new FormuleButton(WiskOpdr.rb.getString("klaarKnopLabel"));
		knopImageButton.setPreferredSize(new Dimension(80,22));
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
        
        setFeedbackOption(false);
        
        removeAll();
	    plaatsGUI();
	    add(mainPanel);
	}
        
   private void plaatsGUI() {   
        //plaats componenten mainPanel
        Component[] r11 = {antwoordPanelBox, antwoordEditorPanel};
        // Component[] r11 = {titleAntwoordLabel, 	ra(10,0),	hbAntwoord, 	hgl()};
		Component[] r12 = {selectableCBBox, 	hgl()};
		//Component[] r13 = {formuleEditor, 	hgl()};
		
		Component[] k1 = {hb(r11), vst(15), hb(r12), vst(15)};
		
		// plaats componenten feedback box
        Component[] r51 = {titleFeedbackLabel,  ra(5,10),   hgl()};
        Component[] r52 = {goedFoutIP,              hgl()};
        
        Component[] r53 = {titleFeedbackTekstLabel,         hgl()};
        Component[] r54 = {ra(0,110),           feedbackEditor};
        
        Component[] k51 = {hb(r51),vst(5),hb(r52), vgl()};
        Component[] k52 = {hb(r53),vst(5),hb(r54), vgl()};
        
        Component[] h5 = {ra(20,0),vb(k51),hst(10), vb(k52)};
        feedbackBox = hb(h5);
        feedbackBox.setVisible(false);
        
        Component[] k7 = {vb(k1),   feedbackBox};
        Box hbox = hb(k7);
        
        Component[] r70 = {titleScoreLabel,    ra(10,10),      feedbackPV, hgl()};
        Component[] k8 = {hb(r11),vst(15),hbox,ra(10,10), hb(r70), vgl()};
        
        Component[] r21 = {titleSettingsLabel, 	hgl()};
		Component[] r22 = {itemCountLabel, 		ra(10,10), 	hgl(), 	itemCountTF};
		Component[] r23 = {multiSelectionsCB, 	ra(5,0),	hgl(),	hbMeervoudig};
		Component[] r24 = {randomizePositionsCB, 		ra(5,0),	hgl(),	hbRandom};
		Component[] r25 = {checkFormuleCB,  		ra(5,0),	hgl(),	hbCheckFormule};
		Component[] r26 = {imageKnopLabel, 		ra(5,5),  	knopImageButton,ra(5,0),	hgl(),	hbKnopImage};
		
		Component[] k2 = {hb(r21), vst(15), hb(r22), vst(5), hb(r23), vst(5), hb(r24), vst(5), 
				hb(r25), vst(5), hb(r26), vst(5), vgl()};
		
		Component[] r31 = {titleLoggingLabel, 	hgl()};
		Component[] r32 = {maxScoreLabel, 		ra(5,10), maxScoreTF, hgl()};
		Component[] r33 = {checkCB, 			ra(5,0),	hgl(),	hbCheck};
		Component[] r34 = {teltMeeCB, 			ra(5,0),	hgl(),	hbTeltMee};
		Component[] r35 = {logCB, 				ra(5,10), logIDField, ra(5,10), logIDLabelLabel, ra(5,10), logIDLabelField, ra(5,0),	hgl(),	hbLogID};
		Component[] r35a = {feedbackCB,         hgl()};
        Component[] r36 = {ra(6,0),			logObjectivesButton, hgl()};
		
		//Component[] k3 = {hb(r31), vst(15), hb(r32), vst(5), hb(r33), vst(5), hb(r34), vst(5), hb(r35), vst(10), hb(r36), vgl()};
		Component[] k3 = {hb(r31), vst(15), hb(r32), vst(5), hb(r33), vst(5), hb(r34), vst(5), hb(r35), vst(5), hb(r35a), vst(10), hb(r36), vgl()};
        
		Component[] main = {vb(k8), hst(50), vb(k2), hst(50), vb(k3)};
		mainPanel.add(hb(main));
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
	

	public void maakCheckboxes() {
		if(WiskOpdr.misconceptions!=null)
			logMisconceptions = new boolean[aantalSelectables][][];
				
		for(int i=0 ; i<aantalSelectables ; i++) {
			Box regelBox = Box.createHorizontalBox();
			if(selectableCheckboxes[i]==null) {   
				selectableCheckboxes[i] = new WiskOpdrCheckbox("Nr "+(i+1));
                regelBox.add(selectableCheckboxes[i]);
                regelBox.add(Box.createRigidArea(new Dimension(20,10)));
            }
		    if(logMisconceptionsButtons[i]==null) {
		    	logMisconceptionsButtons[i] = new ObjectiveChoiceButton(WiskOpdr.rb.getString("OPT_misconceptions"),WiskOpdr.misconceptions, WiskOpdr.mccCategorieString);
		    	logMisconceptionsButtons[i].setPreferredSize(new Dimension(120,22));
		    	regelBox.add(logMisconceptionsButtons[i]);
		    }
		    selectableCBBox.add(regelBox);
		}
		selectableCBBox.add(Box.createVerticalGlue());
		enableMisconceptions();
	}
	
	private void enableMisconceptions() {
		for(int i=0 ; i<aantalSelectables ; i++) {
			boolean visible = WiskOpdr.misconceptions!=null 
					&& !multiSelectionsCB.isSelected()
					&& checkCB.isSelected();
		   	if(logMisconceptionsButtons[i]!=null)
		   		logMisconceptionsButtons[i].setVisible(visible);
		}	
	}
	
	public void verwijderCheckboxes() {
		for(int i=0 ; i<aantalSelectablesMax ; i++) {
		    if(selectableCheckboxes[i]!=null) {	
		    	selectableCheckboxes[i] = null;
		    	if(logMisconceptionsButtons[i]!=null) 
		    		logMisconceptionsButtons[i] = null;
		    	
		    }
		}
		selectableCBBox.removeAll();
		mainPanel.validate();
		aantalSelectables = 0;
	} 
	
	public void setEditState(Hashtable h)
	{
	    boolean[] juisteSelecties = null;
	    int scoreMax = 0;
		boolean randomizePositions = false;
		boolean multiSelections = false;
		boolean logOption = false;
		String logID = "";
		boolean check = true;
		boolean teltMee = true;
		boolean checkFormule = false;
		String[] formuleStrings = null;
		String knopImageString = "";
		boolean[][][] logMisconceptions = null;
		
		Hashtable[] answerModels = null;
        boolean hasFeedback = false;
				
	    if(h.containsKey("juisteSelecties")) juisteSelecties = (boolean[])h.get("juisteSelecties");
	    if(h.containsKey("scoreMax")) scoreMax = ((Integer)h.get("scoreMax")).intValue();
	    if(h.containsKey("randomizePositions")) randomizePositions = ((Boolean)h.get("randomizePositions")).booleanValue();
	    if(h.containsKey("multiSelections")) multiSelections = ((Boolean)h.get("multiSelections")).booleanValue();
	    if(h.containsKey("logOption")) logOption = ((Boolean)h.get("logOption")).booleanValue();
		if(h.containsKey("logID")) logID = (String)h.get("logID");
		if(h.containsKey("check")) check = ((Boolean)h.get("check")).booleanValue();
		if(h.containsKey("teltMee")) teltMee = ((Boolean)h.get("teltMee")).booleanValue();
		if(h.containsKey("checkFormule")) checkFormule = ((Boolean)h.get("checkFormule")).booleanValue();
		if(h.containsKey("formuleStrings")) formuleStrings = (String[])h.get("formuleStrings");
		if(h.containsKey("knopImageString")) knopImageString = (String)h.get("knopImageString");
		if(h.containsKey("logMisconceptions")) logMisconceptions = (boolean[][][])h.get("logMisconceptions");
		
		if(h.containsKey("answerModels")) answerModels = (Hashtable[])h.get("answerModels");
        if(h.containsKey("hasFeedback")) hasFeedback = ((Boolean)h.get("hasFeedback")).booleanValue();
        
        if(answerModels != null)
        {   this.answerModels = new Hashtable[answerModels.length];
            for(int i=0 ; i<answerModels.length ; i++)
            {   this.answerModels[i] = answerModels[i];
            }
        } 
        
        if(juisteSelecties==null) return;
        verwijderCheckboxes();
        aantalSelectables = juisteSelecties.length;
        itemCountTF.setText(""+aantalSelectables);
        maxScoreTF.setText(""+scoreMax);
        randomizePositionsCB.setSelected(randomizePositions);
        multiSelectionsCB.setSelected(multiSelections);
        this.logMisconceptions = logMisconceptions;

        maakCheckboxes();
        for(int i=0 ; i<aantalSelectables ; i++) {
            selectableCheckboxes[i].setSelected(juisteSelecties[i]);
            if(WiskOpdr.misconceptions!=null && logMisconceptionsButtons[i]!=null && logMisconceptions!=null)
                logMisconceptionsButtons[i].setChoices(logMisconceptions[i]);
        }
        
        enableMisconceptions();
        
        feedbackCB.setSelected(hasFeedback);
        if(hasFeedback)
        {   aantalAnswerModels = answerModels.length;
            antwoordEditorPanel.remove(tabbladTab);
            tabbladTab = new OpdrachtNrRij(aantalAnswerModels, 150,20);
            tabbladTab.setTab(true);
            tabbladTab.setScoresVisible(false);
            tabbladTab.setSize(tabbladTab.getSize().width, 23);
            tabbladTab.addActionListener(this);
            tabbladTab.setBackground(new Color(210,210,210));
            tabbladTab.setSelected(answerModelNr+1);
            antwoordEditorPanel.add(tabbladTab,0);
            aantalTabsKnop.setLocation(150+25*aantalAnswerModels+5 ,24);
            
            answerModelNr = 0;
            setAnswerModel();
            antwoordEditorPanel.setPreferredSize(new Dimension(Math.max(250+25*aantalAnswerModels+40,400),220));
        }
        
	    
	    
	    if(!multiSelections) {
          for(int i=0 ; i<aantalSelectablesMax ; i++) {
              buttonGroup.add(selectableCheckboxes[i]) ;
          }
        }
        else {
            for(int i=0 ; i<aantalSelectablesMax ; i++) {
              buttonGroup.remove(selectableCheckboxes[i]) ;
            }
        }
	    
	    logCB.setSelected(logOption);
        logIDField.setVisible(logOption);
        logIDField.setText(logID);
        logObjectivesButton.setEditState(h);
        checkCB.setSelected(check);
        teltMeeCB.setSelected(teltMee);
        checkFormuleCB.setSelected(checkFormule);
        if(formuleStrings!=null)formuleEditor.zetRegels(formuleStrings);
        formuleEditor.setVisible(checkFormule);
        antwoordPanelBox.setVisible(checkFormule);
        selectableCBBox.setVisible(!checkFormule);
        
        knopImageButton.setPopupButtonImage(knopImage);
    	iconman = new Iconan(WiskOpdr.applet, mainPanel, TekstImageVak.getImageMap(), TekstImageVak.getImageCache());
    	if(knopImageString!=null && !"".equals(knopImageString)) {
    		knopImage = iconman.getImage(knopImageString);
    		knopImageButton.setPopupButtonImage(knopImage);
    	}
    	else {
    		knopImageButton.setPopupButtonImage(null);
    		knopImageButton.setCode(WiskOpdr.rb.getString("klaarKnopLabel"));
    		knopImageButton.setPreferredSize(new Dimension(80,22));
    	}
    	this.knopImageString = knopImageString;
    	
    	setFeedbackOption(hasFeedback);
    	
    	((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)mainPanel)).pack();
	}
	
	public Hashtable getEditState()
	{
	    boolean[] juisteSelecties = null;
	    int scoreMax = 0;
	    boolean randomizePositions = false;
		boolean multiSelections = false;
		boolean logOption = false;
		String logID = "";
		boolean check = true;
		boolean teltMee = true;
		boolean checkFormule = false;
		String[] formuleStrings = null;
		String knopImageString = "";
		boolean[][][] logMisconceptions = null;
		boolean hasFeedback = false;
		
		hasFeedback = feedbackCB.isSelected();
		
		if(hasFeedback) {
          getAnswerModel();
            if(answerModels!=null)
              setAnswerModel(answerModels[0]);
        }
		 
		knopImageString = this.knopImageString;
	    juisteSelecties = new boolean[aantalSelectables];
	    for(int i=0 ; i<aantalSelectables ; i++)
	    {  juisteSelecties[i] = selectableCheckboxes[i].isSelected();
	    }
	    
	    scoreMax = intFromText(scoreMax, maxScoreTF.getText());
	    if(hasFeedback)
	        scoreMax = intFromText(scoreMax, feedbackPV.getText());
	    if(!teltMee)scoreMax = 0;
	    randomizePositions = randomizePositionsCB.isSelected();
	    multiSelections = multiSelectionsCB.isSelected();
	    logOption = logCB.isSelected();
		logID = logIDField.getText();
		check = checkCB.isSelected();
		teltMee = teltMeeCB.isSelected();
		checkFormule = checkFormuleCB.isSelected();
		formuleStrings = formuleEditor.geefRegels();
		logMisconceptions = this.logMisconceptions;
		
		
		if(logMisconceptions!=null)
		{	for(int i=0 ; i<aantalSelectables ; i++)
			{	logMisconceptions[i] = logMisconceptionsButtons[i].getChoices();
			}
		}
		
		
	    	    
		Hashtable h = new Hashtable();
		h.put("juisteSelecties", juisteSelecties);
		h.put("scoreMax", new Integer(scoreMax));
		h.put("randomizePositions", new Boolean(randomizePositions));
		h.put("multiSelections", new Boolean(multiSelections));
		h.put("logOption",new Boolean(logOption));
		h.put("logID",logID);
		h.put("check",new Boolean(check));
		h.put("teltMee",new Boolean(teltMee));
		h.put("checkFormule",new Boolean(checkFormule));
		h.put("formuleStrings", formuleStrings);
        
        h.putAll(logObjectivesButton.getEditState(scoreMax));
            
        h.put("hasFeedback",new Boolean(hasFeedback));
        if(answerModels!=null)h.put("answerModels",answerModels);
        
        if(logMisconceptions!=null)
        {	h.put("logMisconceptions",logMisconceptions);
        }
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
	
    public void stop(){
      if (iconman != null) iconman.dispose(); // cleanup
    }
    
    public void start(){}
    
    public void addActionListener(ActionListener al){}
    
//    public void editImage() {
//    	if(iconman==null)
//			iconman = new Iconan(WiskOpdr.applet, this, (Hashtable)TekstImageVak.getImageMap());
//		iconman.editImage(knopImageString, this, this);
//		
////        if(imageDialog == null)
////        {
////        	
////        	Frame f = JOptionPane.getFrameForComponent(this);
////			imageDialog = new Dialog(f,"title", true);
////			imageDialog.setLayout(new BorderLayout());
////			iconman = new Iconan(WiskOpdr.applet, (Component)this, (Hashtable)TekstImageVak.getImageMap());
////            imageDialog.add(iconman);
////            imageDialog.pack();
////            iconman.addActionListener(this);
////        }
////        iconman.select(knopImageString);
////        imageDialog.setVisible(true);
//    }    
    
    private Hashtable fillAnswerModel(Hashtable h)
    {
        boolean[] juisteSelecties = new boolean[aantalSelectables];
        for(int i=0 ; i<aantalSelectables ; i++) {  
            juisteSelecties[i] = selectableCheckboxes[i].isSelected();
        }
        String feedback = feedbackEditor.getText();
        int puntenFeedback = intFromText(0,feedbackPV.getText());
        
        int goedHalfFout = goedFoutIP.geefKeuze()-1;
        
        String[] formuleStrings = formuleEditor.geefRegels();

        h.put("juisteSelecties", juisteSelecties);
        h.put("formuleStrings", formuleStrings);
        h.put("feedback", feedback);
        h.put("puntenFeedback", new Integer(puntenFeedback));
        h.put("goedHalfFout", new Integer(goedHalfFout));
        return h;
    }
    
    private void setAnswerModel(Hashtable h)
    {   
        boolean[] juisteSelecties = new boolean[aantalSelectables];
        if(h.containsKey("juisteSelecties")) juisteSelecties = (boolean[])h.get("juisteSelecties");
        for(int i=0 ; i<aantalSelectables ; i++) {  
            selectableCheckboxes[i].setSelected(juisteSelecties[i]);
        }
        
        String feedback = "";
        if(h.containsKey("feedback")) feedback = (String)h.get("feedback");
        
        String[] formuleStrings = null;
        if(h.containsKey("formuleStrings")) formuleStrings = (String[])h.get("formuleStrings");
        
        int puntenFeedback = 0;
        if(h.containsKey("puntenFeedback")) puntenFeedback = ((Integer)h.get("puntenFeedback")).intValue();
        
        int goedHalfFout = 2;
        if(h.containsKey("goedHalfFout")) goedHalfFout = ((Integer)h.get("goedHalfFout")).intValue();
       
        formuleEditor.zetRegels(formuleStrings);
        feedbackEditor.zetTekst(feedback);
        feedbackEditor.layoutTekst();
        feedbackPV.setText(""+puntenFeedback);
        goedFoutIP.setItem(goedHalfFout);
       
        updateFeedbackTitelLabel();
    }
    
    private void getAnswerModel()
    {   if(answerModels==null)return;
        answerModels[answerModelNr] = fillAnswerModel(new Hashtable());
        if(answerModelNr==0 && feedbackCB.isSelected()) 
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
      
      boolean checkFormule = checkFormuleCB.isSelected();
      if(checkFormule) {
        antwoordEditorPanel.setPreferredSize(new Dimension(350,220));
       antwoordEditorPanel.setBorder(BorderFactory.createEmptyBorder());
      }
      else if(b){
        antwoordEditorPanel.setPreferredSize(new Dimension(400,42));
        antwoordEditorPanel.setBorder(BorderFactory.createMatteBorder(0,0,1,0,WiskOpdr.colorBlue3));
      }
      else {
        antwoordEditorPanel.setPreferredSize(new Dimension(150,24));
        antwoordEditorPanel.setBorder(BorderFactory.createEmptyBorder());
      }
      if(feedbackBox!=null)
        feedbackBox.setVisible(b);
      
      answerModelNr = 0;
      tabbladTab.setSelected(answerModelNr+1);
      
      if(b) {
        getAnswerModel();
        setAnswerModel();
      }
    }
    
	public void actionPerformed(ActionEvent e)
	{
	  if(e.getSource() == tabbladTab)
      {   int nr = Integer.parseInt(e.getActionCommand())-1;
          if(answerModelNr != nr) 
          {     
              getAnswerModel();
              answerModelNr = nr;
              setAnswerModel();
              tabPositieKnop.setLocation(146+25*answerModelNr+5 ,0);
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
              tabPositieKnop.setLocation(146+25*answerModelNr+5 ,0);
          }
          if(e.getActionCommand().equals("min") && answerModelNr>0) 
          {   resAnswerModel = new Hashtable();
              fillAnswerModel(resAnswerModel);
              answerModels[answerModelNr] = answerModels[answerModelNr-1];
              answerModels[answerModelNr-1] = resAnswerModel;
              answerModelNr--;
              tabbladTab.setSelected(answerModelNr+1);
              tabPositieKnop.setLocation(146+25*answerModelNr+5 ,0);
          }
          
      }
      else if(e.getSource() == aantalTabsKnop)
      {   if(e.getActionCommand().equals("min") && aantalAnswerModels>1)
          {   //remove(opdrContainers[activiteitNr][aantalOpdrachten[activiteitNr]-1]);
              //opdrContainers[activiteitNr][aantalOpdrachten[activiteitNr]-1] = null;
              aantalAnswerModels--;
              if(answerModelNr>aantalAnswerModels-1) answerModelNr--;
              setAnswerModel();
              aantalTabsKnop.setLocation(150+25*aantalAnswerModels+5 ,24);
              antwoordEditorPanel.remove(tabbladTab);
              tabbladTab = new OpdrachtNrRij(aantalAnswerModels, 150,20);
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
              aantalTabsKnop.setLocation(150+25*aantalAnswerModels+5 ,24);
              antwoordEditorPanel.remove(tabbladTab);
              tabbladTab = new OpdrachtNrRij(aantalAnswerModels, 150,20);
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
          if(checkFormuleCB.isSelected())
            antwoordEditorPanel.setPreferredSize(new Dimension(Math.max(150+25*aantalAnswerModels+40,350),220));
          else
            antwoordEditorPanel.setPreferredSize(new Dimension(Math.max(150+25*aantalAnswerModels+40,350),42));
             
            ((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)mainPanel)).pack();

      }
      else if(e.getSource()==feedbackCB)
      {   setFeedbackOption(feedbackCB.isSelected());
          ((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)mainPanel)).pack();

      }
	  else if(e.getSource() instanceof HelpButton)
		{
			OpdrNavStructEdit.helpBrowser.loadURL(((HelpButton)e.getSource()).getURL());
		}
		else if(e.getSource()==itemCountTF)
		{
			int aantal = Math.min(aantalSelectablesMax, Integer.parseInt(itemCountTF.getText()));
			if(aantal != aantalSelectables)
			{	verwijderCheckboxes();
				aantalSelectables = aantal;
				maakCheckboxes();
			}
			
		}
		else if(e.getSource()==logCB)
	    {   logIDField.setVisible(logCB.isSelected());  
	    	mainPanel.validate();
	    	//logObjectivesButton.setVisible(logCB.isSelected());
	    }
		else if(e.getSource()==checkFormuleCB)
	    {   formuleEditor.setVisible(checkFormuleCB.isSelected()); 
	        antwoordPanelBox.setVisible(checkFormuleCB.isSelected()); 
	        formuleEditor.setBounds(0,20,350,200);
	        formuleEditor.setSize(350,200);
	    	selectableCBBox.setVisible(!checkFormuleCB.isSelected());
	    	if(checkFormuleCB.isSelected()) 
	    	  antwoordEditorPanel.setPreferredSize(new Dimension(350,220));
	    	else if(feedbackCB.isSelected())
	    	  antwoordEditorPanel.setPreferredSize(new Dimension(350,42));
	    	else
	    	  antwoordEditorPanel.setPreferredSize(new Dimension(150,24));
	        enableMisconceptions();
	    	((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)mainPanel)).pack();

	    }
		else if(e.getSource()==multiSelectionsCB)
	    {   enableMisconceptions();
    	    boolean b = multiSelectionsCB.isSelected();
            if(!b) {
                for(int i=0 ; i<aantalSelectablesMax ; i++) {
                    buttonGroup.add(selectableCheckboxes[i]) ;
                }
            }
            else {
                for(int i=0 ; i<aantalSelectablesMax ; i++) {
                  buttonGroup.remove(selectableCheckboxes[i]) ;
                }
            }
	    }
//		else if(e.getSource()==knopImageButton)
//	    {   editImage();
//	            
//	    }
//	    else if(e.getSource()==iconman)
//	    {
//	    	String name = e.getActionCommand();
//	        if(!"".equals(name))
//	        {
//	        	knopImageString = name;
//	            this.knopImage = iconman.getImage(name);
//	            knopImageButton.setPopupButtonImage(knopImage);
//	            int imWidth = iconman.getWidth(knopImageString);
//				int imHeight = iconman.getHeight(knopImageString);
//				if(imWidth == -1) imWidth = 20;
//				if(imHeight == -1) imHeight = 20;
//				knopImageButton.setSize(imWidth,imHeight);
//	            repaint();
//	        }
//	    }
		else if(e.getSource()==knopImageButton) {   
			iconman = new Iconan(WiskOpdr.applet, mainPanel, TekstImageVak.getImageMap(), TekstImageVak.getImageCache());
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
				knopImageButton.setPreferredSize(new Dimension(Math.max(imWidth,80),Math.max(imHeight,220)));
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

	@Override
	public void focusGained(FocusEvent e) {
		
	}

	@Override
	public void focusLost(FocusEvent e) {
		if(e.getSource()==itemCountTF)
		{
			int aantal = Math.min(aantalSelectablesMax, Integer.parseInt(itemCountTF.getText()));
			if(aantal != aantalSelectables)
			{	verwijderCheckboxes();
				aantalSelectables = aantal;
				maakCheckboxes();
			}
		}
		if(feedbackCB.isSelected() && e.getSource()==feedbackPV && answerModelNr==0) {
          maxScoreTF.setText(feedbackPV.getText());
      }
	}

	@Override
	public void showHelpButtons(boolean b) {
		hbCheck.setVisible(b);
    	hbTeltMee.setVisible(b);
    	hbLogID.setVisible(b);
    	hbRandom.setVisible(b);
    	hbMeervoudig.setVisible(b);
    	hbCheckFormule.setVisible(b);
    	hbAntwoord.setVisible(b);
    	hbKnopImage.setVisible(b);
    	((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)mainPanel)).pack();

    }

	@Override
	public String geefHelpURL() {
		return HELP_12_URL;
	}
	
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
