package fi.wiskopdr.templatecomponents;

import java.awt.AWTEventMulticaster;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import fi.beans.iconan.Iconan;
import fi.wiskopdr.DialogFacade;
import fi.wiskopdr.HelpButton;
import fi.wiskopdr.HelpButtonPanelIF;
import fi.wiskopdr.ObjectiveChoiceButton;
import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.WiskOpdrButton;
import fi.wiskopdr.WiskOpdrCheckbox;
import fi.wiskopdr.WiskOpdrComboBox;
import fi.wiskopdr.WiskOpdrTextField;
import fi.wiskopdr.domainmodel.Constants;
import fi.wiskopdr.formuleobjects.FormuleButton;
import fi.wiskopdr.opdrnav.ActKeuzePanel;
import fi.wiskopdr.opdrnav.OpdrNavStructEdit;
import fi.wiskopdr.opdrnav.OpdrachtNrRij;
import fi.wiskopdr.opdrnav.PlusMinKnop;
import fi.wiskopdr.tekstobjects.EditInteractiePanelDialog;
import fi.wiskopdr.tekstobjects.TekstEditor;
import fi.wiskopdr.tekstobjects.TekstImageVak;
import fi.wiskopdr.tekstobjects.TekstVak;

public class MultipleChoiceEditor_1 implements TComponentEditor, ActionListener, FocusListener, HelpButtonPanelIF, WindowListener {

	private TekstVak tekstVak;
	private Font font = new Font("SansSerif",Font.PLAIN,12);
	private int scoreMax;
	private boolean multiselections;
	private int aantalSelectables = 4;
	private int aantalSelectablesMax = 30;
	private boolean[][][] logMisconceptions;
	private String MCwidgetID = null;
	
	private DialogFacade frame;
	private JPanel preferencesPanel;
	private JPanel topPanel, mainPanel, bottomPanel;
	
	//topPanel
	private JLabel titleLabel;
	
	//mainPanel  //juiste antwoord
	private JLabel titleAntwoordLabel;
	private JPanel antwoordEditorPanel;
	private JCheckBox[] selectableCheckboxes;
	private ButtonGroup buttonGroup = new ButtonGroup();
	private Box selectableCBBox;
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
	private JCheckBox hasPrefixCB;
	private JLabel listNumberTypeLabel;
	private JComboBox listNumberTypeComboBox;
	private JLabel tabWidthLabel;
	private JTextField tabWidthTF;
	private JLabel rowSpaceLabel;
	private JTextField rowSpaceTF;
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
	
	// bottomPanel
	private JButton okButton, cancelButton;
	private JTextField breedteTF, hoogteTF;
    private JLabel breedteLabel, hoogteLabel;
    private JCheckBox volledigeBreedteCB;
    
    private HelpButton helpButton;
	private JPanel helpPanel;
    private Box helpBox;
    private Box helpTitelBox;
    private JButton hideHelpButton;
    
 // Helpbuttons
    private static String HELP_MULTIPLECHOICE_URL = WiskOpdr.rb.getString("HELP_MULTIPLECHOICE_URL");
    private static String HELP_MULTIPLECHOICE_URL_CHECK = WiskOpdr.rb.getString("HELP_MULTIPLECHOICE_URL_CHECK");
    private static String HELP_MULTIPLECHOICE_URL_TELTMEE = WiskOpdr.rb.getString("HELP_MULTIPLECHOICE_URL_TELTMEE");
    private static String HELP_MULTIPLECHOICE_URL_LOGID = WiskOpdr.rb.getString("HELP_MULTIPLECHOICE_URL_LOGID");
    private static String HELP_MULTIPLECHOICE_URL_ANTWOORD = WiskOpdr.rb.getString("HELP_MULTIPLECHOICE_URL_ANTWOORD");
    private static String HELP_MULTIPLECHOICE_URL_MEERVOUDIG = WiskOpdr.rb.getString("HELP_MULTIPLECHOICE_URL_MEERVOUDIG");
    private static String HELP_MULTIPLECHOICE_URL_RANDOM = WiskOpdr.rb.getString("HELP_MULTIPLECHOICE_URL_RANDOM");
    private static String HELP_MULTIPLECHOICE_URL_KNOPIMAGE = WiskOpdr.rb.getString("HELP_MULTIPLECHOICE_URL_KNOPIMAGE");
    
    private HelpButton hbCheck = makeHelpButton(HELP_MULTIPLECHOICE_URL_CHECK);
    private HelpButton hbTeltMee = makeHelpButton(HELP_MULTIPLECHOICE_URL_TELTMEE);
    private HelpButton hbLogID = makeHelpButton(HELP_MULTIPLECHOICE_URL_LOGID);
    private HelpButton hbAntwoord = makeHelpButton(HELP_MULTIPLECHOICE_URL_ANTWOORD);
    private HelpButton hbMeervoudig = makeHelpButton(HELP_MULTIPLECHOICE_URL_MEERVOUDIG);
    private HelpButton hbRandom = makeHelpButton(HELP_MULTIPLECHOICE_URL_RANDOM);
    private HelpButton hbKnopImage = makeHelpButton(HELP_MULTIPLECHOICE_URL_KNOPIMAGE);
    
   
    
	public MultipleChoiceEditor_1(TekstVak tekstVak) {
		this.tekstVak = tekstVak;
		makeGUI();
		makeFrame();
		answerModels = new Hashtable[aantalAnswerModels];
	}
	
	public void setTekstVak(TekstVak tekstVak) {
		this.tekstVak = tekstVak;
	}
	
	private void makeGUI() {
		preferencesPanel = new JPanel(new BorderLayout());
		
		topPanel = new JPanel(new BorderLayout());
		topPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
		topPanel.setBackground(WiskOpdr.colorBlue1);
		
		mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBackground(WiskOpdr.colorGray3);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 50, 30));
		
		bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.setBackground(WiskOpdr.colorGray2);
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		
		//topPanel
		titleLabel = makeLabel(WiskOpdr.rb.getString("TCOMP_multip_settings"), new Font("SansSerif",Font.PLAIN, 24));
		titleLabel.setForeground(WiskOpdr.colorGray3);
		
		
		helpButton = new HelpButton(HELP_MULTIPLECHOICE_URL);
		helpButton.setPreferredSize(new Dimension(22,22));
		helpButton.setMinimumSize(new Dimension(22,22));
		helpButton.setMaximumSize(new Dimension(22,22));
		helpButton.addActionListener(this);
		
		helpPanel = new JPanel(new BorderLayout());
		helpPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, WiskOpdr.colorBlue4));
		helpPanel.setBackground(WiskOpdr.colorBlue5);
		helpPanel.setPreferredSize(new Dimension(300,400));
		
		JComponent bp = OpdrNavStructEdit.helpBrowser.getBrowserPanel();
    	bp.setPreferredSize(new Dimension(300,400));
    	helpPanel.add(bp);
		
		helpBox = Box.createVerticalBox();
		helpBox.add(Box.createRigidArea(new Dimension(300,0)));
		helpBox.add(helpPanel);
		helpBox.setMaximumSize(new Dimension(300,800));
		helpBox.setVisible(false);
		
		JLabel helpTitleLabel = new JLabel("Help");
		helpTitleLabel.setForeground(WiskOpdr.colorBlue5);
		helpTitleLabel.setFont(new Font("SansSerif",Font.PLAIN, 24));
	      
		hideHelpButton = new WiskOpdrButton("\u276e");
        hideHelpButton.setBorder(BorderFactory.createLineBorder(WiskOpdr.colorBlue1));
        hideHelpButton.setBackground(WiskOpdr.colorBlue1);
        hideHelpButton.setForeground(WiskOpdr.colorBlue5);
        hideHelpButton.setPreferredSize(new Dimension(20,20));
        hideHelpButton.setFont(new Font("SansSerif",Font.PLAIN, 24));
        hideHelpButton.addActionListener(this);
        
		helpTitelBox = Box.createVerticalBox();
		helpTitelBox.add(Box.createRigidArea(new Dimension(300,0)));
		Box helpheader = Box.createHorizontalBox();
        helpheader.add(Box.createRigidArea(new Dimension(140,0)));
        helpheader.add(helpTitleLabel);
        helpheader.add(Box.createRigidArea(new Dimension(80,0)));
        helpheader.add(hideHelpButton);
		helpTitelBox.setPreferredSize(new Dimension(300,30));
		helpTitelBox.add(helpheader);
		helpTitelBox.setVisible(false);
		
		Box headerbox = Box.createHorizontalBox();
		headerbox.add(Box.createHorizontalGlue());
		headerbox.add(titleLabel);
		headerbox.add(Box.createHorizontalGlue());
		
		headerbox.add(helpButton);
		headerbox.add(helpTitelBox);
		
		topPanel.add(headerbox);
		
		//mainPanel  //juiste antwoord
		titleAntwoordLabel = makeLabel(WiskOpdr.rb.getString("FEV_titleAntwoordLabel"), font.deriveFont(Font.BOLD, 16));
		titleAntwoordLabel.setBounds(0,-3,140,20);
		
		selectableCheckboxes = new JCheckBox[aantalSelectablesMax];
    	logMisconceptionsButtons = new ObjectiveChoiceButton[aantalSelectablesMax];
    	selectableCBBox = Box.createVerticalBox();
    	maakCheckboxes();
    	
    	antwoordEditorPanel = new JPanel();
        antwoordEditorPanel.setLayout(null);
        antwoordEditorPanel.add(titleAntwoordLabel);
        antwoordEditorPanel.setBorder(BorderFactory.createMatteBorder(0,0,1,0,WiskOpdr.colorBlue3));
        //antwoordEditorPanel.add(antwoordvak);
        //antwoordEditorPanel.addComponentListener(new EditorComponentListener());
        antwoordEditorPanel.setPreferredSize(new Dimension(350,80));
        antwoordEditorPanel.setMaximumSize(new Dimension(2835,80));
        
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
        feedbackPV.setVisible(false);
        
        titleScoreLabel = new JLabel(WiskOpdr.rb.getString("FEV_titleScoringLabel"));
        titleScoreLabel.setForeground(WiskOpdr.colorBlue1);
        titleScoreLabel.setFont(font.deriveFont(Font.BOLD, 16));
        titleScoreLabel.setVisible(false);
        
		//mainPanel  //settings
		titleSettingsLabel = makeLabel(WiskOpdr.rb.getString("settingsLabel"), font.deriveFont(Font.BOLD, 16));
		itemCountLabel = makeLabel(WiskOpdr.rb.getString("TCOMP_multip_rowCount"), font);
		itemCountTF = makeTextField("", 30, 22, this);
		multiSelectionsCB = makeCheckBox(WiskOpdr.rb.getString("meervSelectiesLabel"), false, this);//("Meervoudige selecties mogelijk");
		randomizePositionsCB = makeCheckBox(WiskOpdr.rb.getString("randomPosLabel"), false, this);
		hasPrefixCB = makeCheckBox(WiskOpdr.rb.getString("TCOMP_multip_hasPrefix"), true, this);
		listNumberTypeLabel = makeLabel(WiskOpdr.rb.getString("TCOMP_multip_numberType"), font);
		
		listNumberTypeComboBox = new WiskOpdrComboBox();
		listNumberTypeComboBox.setFont(font);
		listNumberTypeComboBox.setPreferredSize(new Dimension(80,22));
		listNumberTypeComboBox.addItem(WiskOpdr.rb.getString("TCOMP_multip_chooseType"));
		for(int i=0 ; i<MultipleChoiceGenerator.listNumbers.length ; i++) {
			listNumberTypeComboBox.addItem(MultipleChoiceGenerator.listNumbers[i][0]+" ,"+MultipleChoiceGenerator.listNumbers[i][1]+" ,"+MultipleChoiceGenerator.listNumbers[i][2]+" , ...");
		}
		tabWidthLabel = makeLabel(WiskOpdr.rb.getString("TCOMP_multip_tabWidth"),font);
		tabWidthTF = makeTextField("",30,22,this);
		rowSpaceLabel = makeLabel(WiskOpdr.rb.getString("TCOMP_multip_rowSpace"), font);
		rowSpaceTF = makeTextField("", 30, 22, this);
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
		logObjectivesButton = new ObjectiveChoiceButton();
        logObjectivesButton.setPreferredSize(new Dimension(120,22));
        logObjectivesButton.setMaximumSize(new Dimension(120,22));
        feedbackCB = makeCheckBox(WiskOpdr.rb.getString("feedbackCBLabel"),false,this);
        
        setFeedbackOption(false);
                
        //plaats componenten mainPanel
        Component[] r11 = {antwoordEditorPanel,      hgl()};
        
        //Component[] r11 = {titleAntwoordLabel,    ra(10,0),   hbAntwoord,     hgl()};
        Component[] r12 = {selectableCBBox,     hgl()};
        
        Component[] k1 = {hb(r12), vst(15), vgl()};
        
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
        
        Component[] k7 = {vb(k1),  feedbackBox};
        Box hbox = hb(k7);
        
        Component[] r70 = {titleScoreLabel,    ra(10,10),      feedbackPV, hgl()};
        Component[] k8 = {hb(r11),hbox,ra(10,10), hb(r70), vgl()};
        
        Component[] r21 = {titleSettingsLabel,  hgl()};
        Component[] r22 = {itemCountLabel,      ra(10,10),  hgl(),  itemCountTF};
        Component[] r23 = {multiSelectionsCB,   ra(5,0),    hgl(),  hbMeervoudig};
        Component[] r23a = {randomizePositionsCB,   ra(5,0),    hgl()};
        Component[] r24 = {hasPrefixCB,         hgl()};
        Component[] r25 = {listNumberTypeLabel, ra(10,10),  hgl(),  listNumberTypeComboBox};
        Component[] r26 = {tabWidthLabel,       ra(10,10),  hgl(),  tabWidthTF};
        Component[] r27 = {rowSpaceLabel,       ra(10,10),  hgl(),  rowSpaceTF};
        Component[] r28 = {imageKnopLabel,      ra(5,5),        knopImageButton,    ra(5,0),    hgl(),  hbKnopImage};
        
        Component[] k2 = {hb(r21), vst(15), hb(r22), vst(5), hb(r23), vst(5), hb(r23a), vst(5), hb(r24), vst(5), 
                hb(r25), vst(5), hb(r26), vst(5), hb(r27), vst(5), hb(r28), vst(5), vgl()};
        
        Component[] r31 = {titleLoggingLabel,   hgl()};
        Component[] r32 = {maxScoreLabel,       ra(5,10), maxScoreTF, hgl()};
        Component[] r33 = {checkCB,             ra(5,0),    hgl(),  hbCheck};
        Component[] r34 = {teltMeeCB,           ra(5,0),    hgl(),  hbTeltMee};
        Component[] r35 = {logCB,               ra(5,10), logIDField, ra(5,10), logIDLabelLabel, ra(5,10), logIDLabelField, ra(5,0),hgl(),  hbLogID};
        Component[] r35a = {feedbackCB,         hgl()};
        Component[] r36 = {ra(6,0),         logObjectivesButton, hgl()};
        
        Component[] k3 = {hb(r31), vst(15), hb(r32), vst(5), hb(r33), vst(5), hb(r34), vst(5), hb(r35), vst(5), hb(r35a), vst(10), hb(r36), vgl()};
        
        Component[] main = {vb(k8), hst(50), vb(k2), hst(50), vb(k3)};
        mainPanel.add(hb(main));
        
        Box hb = Box.createHorizontalBox();
        hb.add(mainPanel);
        hb.add(helpBox);
		
		// bottomPanel
		okButton = makeButton("Ok",this);//
		okButton.setPreferredSize(new Dimension(70,24));
		okButton.setBackground(WiskOpdr.colorBlue1);
		okButton.setForeground(WiskOpdr.colorGray3);
		
		cancelButton = makeButton("Cancel", this);//
		cancelButton.setPreferredSize(new Dimension(70,24));
		cancelButton.setBackground(WiskOpdr.colorBlue1);
		cancelButton.setForeground(WiskOpdr.colorGray3);
		
		breedteLabel = makeLabel(WiskOpdr.rb.getString("breedteLabel"),font);
		breedteTF = makeTextField("300", 40, 22, this);
        breedteTF.setEnabled(false);
        hoogteLabel = makeLabel(WiskOpdr.rb.getString("hoogteLabel"), font);
        hoogteTF = makeTextField("250", 40,22, this);
        hoogteTF.setEnabled(false);
        volledigeBreedteCB = makeCheckBox(WiskOpdr.rb.getString("volleBreedteLabel"),true,this);
       
        // plaats componenten bottomPanel
        Component[] comp = {okButton, hst(20), cancelButton, hst(20), breedteLabel, hst(5), breedteTF, hst(10), hoogteLabel, hst(5), hoogteTF, hst(20), volledigeBreedteCB, hgl()};
        bottomPanel.add(hb(comp));
		
		preferencesPanel.add(topPanel,BorderLayout.NORTH);
		preferencesPanel.add(bottomPanel,BorderLayout.SOUTH);
		preferencesPanel.add(hb,BorderLayout.CENTER);
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
	
	public void makeFrame(){
	   	frame = DialogFacade.newInstance(tekstVak, WiskOpdr.rb.getString("TCOMP_multip"), true);
	    frame.addWindowListener(this);
	    frame.getContentPane().setLayout(new BorderLayout());
	    frame.getContentPane().add(preferencesPanel);
	    frame.getContentPane().add(bottomPanel,BorderLayout.SOUTH);
	    frame.pack();
	    Dimension screenSize = WiskOpdr.applet.getToolkit().getScreenSize();
 	    int xD = (screenSize.width-frame.getSize().width)/2;
 	    int yD = (screenSize.height-frame.getSize().height)/2;
 	    frame.setLocation(xD, yD);
	}
	
	public void dispose( ) {
		frame.dispose();
		if (iconman != null) iconman.dispose();
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
	
	@SuppressWarnings("unchecked")
  @Override
	public Hashtable getPreferences() {
		int itemCount = MultipleChoiceGenerator.initialItemCount;
		int listNumberType = MultipleChoiceGenerator.initialListNumberType;
		int tabWidth = MultipleChoiceGenerator.initialTabWidth;
		int rowSpace = MultipleChoiceGenerator.initialRowSpace;
		boolean hasPrefix = MultipleChoiceGenerator.initialHasPrefix;
		
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
		
		boolean volledigeBreedte = true;
		int breedte = 300;
		String MCwidgetID = null;
		
		itemCount = intFromText(itemCount, itemCountTF.getText());
		listNumberType = listNumberTypeComboBox.getSelectedIndex()-1;
		tabWidth = intFromText(tabWidth, tabWidthTF.getText());
		rowSpace = intFromText(rowSpace, rowSpaceTF.getText());
		hasPrefix = hasPrefixCB.isSelected();
		
		hasFeedback = feedbackCB.isSelected();
		
		if(hasFeedback) {
          getAnswerModel();
            if(answerModels!=null)
              setAnswerModel(answerModels[0]);
       }
		knopImageString = this.knopImageString;
	    juisteSelecties = new boolean[aantalSelectables];
	    for(int i=0 ; i<aantalSelectables ; i++) {  
	    	juisteSelecties[i] = selectableCheckboxes[i].isSelected();
	    }
	    
	    if(hasFeedback)
          maxScoreTF.setText(feedbackPV.getText());
	    
	    scoreMax = intFromText(scoreMax, maxScoreTF.getText());
	    if(hasFeedback)
          scoreMax = intFromText(scoreMax, feedbackPV.getText());
	    randomizePositions = randomizePositionsCB.isSelected();
	    multiSelections = multiSelectionsCB.isSelected();
	    logOption = logCB.isSelected();
		logID = logIDField.getText();
		check = checkCB.isSelected();
		teltMee = teltMeeCB.isSelected();
		//checkFormule = checkFormuleCB.isSelected();
		//formuleStrings = formuleEditor.geefRegels();
		logMisconceptions = this.logMisconceptions;
		
		volledigeBreedte = volledigeBreedteCB.isSelected();
		breedte = intFromText(breedte, breedteTF.getText());
		
		MCwidgetID = this.MCwidgetID;
				
		if(logMisconceptions!=null)	{	
			for(int i=0 ; i<aantalSelectables ; i++)
				logMisconceptions[i] = logMisconceptionsButtons[i].getChoices();
		}
		
		
		
		Hashtable preferences = new Hashtable();
		
		preferences.put("itemCount", new Integer(itemCount));
		preferences.put("listNumberType", new Integer(listNumberType));
		preferences.put("tabWidth", new Integer(tabWidth));
		preferences.put("rowSpace", new Integer(rowSpace));
		preferences.put("hasPrefix", new Boolean(hasPrefix));
		
		preferences.put("juisteSelecties", juisteSelecties);
		preferences.put("scoreMax", new Integer(scoreMax));
		preferences.put("randomizePositions", new Boolean(randomizePositions));
		preferences.put("multiSelections", new Boolean(multiSelections));
		preferences.put("logOption",new Boolean(logOption));
		preferences.put("logID",logID);
		preferences.put("check",new Boolean(check));
		preferences.put("teltMee",new Boolean(teltMee));
		//preferences.put("checkFormule",new Boolean(checkFormule));
		//preferences.put("formuleStrings", formuleStrings);
        
        preferences.putAll(logObjectivesButton.getEditState(scoreMax));
            
        preferences.put("hasFeedback",new Boolean(hasFeedback));
        if(answerModels!=null)preferences.put("answerModels",answerModels);
        
		if(logMisconceptions!=null) {	
			preferences.put("logMisconceptions",logMisconceptions);
        }
		preferences.put("knopImageString", knopImageString);
		
		preferences.put("volledigeBreedte", new Boolean(volledigeBreedte));
		preferences.put("breedte", new Integer(breedte));
		if(MCwidgetID != null)
			preferences.put("MCwidgetID", MCwidgetID);
		
		return preferences;
	}

	@Override
	public void setPreferences(Hashtable preferences) {
		
		int itemCount = MultipleChoiceGenerator.initialItemCount;
		int listNumberType = MultipleChoiceGenerator.initialListNumberType;
		int tabWidth = MultipleChoiceGenerator.initialTabWidth;
		int rowSpace = MultipleChoiceGenerator.initialRowSpace;
		boolean hasPrefix = MultipleChoiceGenerator.initialHasPrefix;
		
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
		
		boolean volledigeBreedte = true;
		int breedte = 300;
		String MCwidgetID = null;
		
		Hashtable[] answerModels = null;
        boolean hasFeedback = false;
		
		if(preferences.containsKey("itemCount")) itemCount = ((Integer)preferences.get("itemCount")).intValue();
		if(preferences.containsKey("listNumberType")) listNumberType = ((Integer)preferences.get("listNumberType")).intValue()+1;
		if(preferences.containsKey("tabWidth")) tabWidth = ((Integer)preferences.get("tabWidth")).intValue();
		if(preferences.containsKey("rowSpace")) rowSpace = ((Integer)preferences.get("rowSpace")).intValue();
		if(preferences.containsKey("hasPrefix")) hasPrefix = ((Boolean)preferences.get("hasPrefix")).booleanValue();
		
		if(preferences.containsKey("juisteSelecties")) juisteSelecties = (boolean[])preferences.get("juisteSelecties");
	    if(preferences.containsKey("scoreMax")) scoreMax = ((Integer)preferences.get("scoreMax")).intValue();
	    if(preferences.containsKey("randomizePositions")) randomizePositions = ((Boolean)preferences.get("randomizePositions")).booleanValue();
	    if(preferences.containsKey("multiSelections")) multiSelections = ((Boolean)preferences.get("multiSelections")).booleanValue();
	    if(preferences.containsKey("logOption")) logOption = ((Boolean)preferences.get("logOption")).booleanValue();
		if(preferences.containsKey("logID")) logID = (String)preferences.get("logID");
		if(preferences.containsKey("check")) check = ((Boolean)preferences.get("check")).booleanValue();
		if(preferences.containsKey("teltMee")) teltMee = ((Boolean)preferences.get("teltMee")).booleanValue();
		if(preferences.containsKey("checkFormule")) checkFormule = ((Boolean)preferences.get("checkFormule")).booleanValue();
		if(preferences.containsKey("formuleStrings")) formuleStrings = (String[])preferences.get("formuleStrings");
		if(preferences.containsKey("knopImageString")) knopImageString = (String)preferences.get("knopImageString");
		if(preferences.containsKey("logMisconceptions")) logMisconceptions = (boolean[][][])preferences.get("logMisconceptions");
		
		if(preferences.containsKey("volledigeBreedte")) volledigeBreedte = ((Boolean)preferences.get("volledigeBreedte")).booleanValue();
		if(preferences.containsKey("breedte")) breedte = ((Integer)preferences.get("breedte")).intValue();
		if(preferences.containsKey("MCwidgetID")) MCwidgetID = (String)preferences.get("MCwidgetID");
		
		if(preferences.containsKey("answerModels")) answerModels = (Hashtable[])preferences.get("answerModels");
        if(preferences.containsKey("hasFeedback")) hasFeedback = ((Boolean)preferences.get("hasFeedback")).booleanValue();
        
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
            antwoordEditorPanel.setPreferredSize(new Dimension(Math.max(250+25*aantalAnswerModels+40,400),80));
        }
        
        
		itemCountTF.setText(""+itemCount);
		listNumberTypeComboBox.setSelectedIndex(listNumberType);
		tabWidthTF.setText(""+tabWidth);
		rowSpaceTF.setText(""+rowSpace);
		hasPrefixCB.setSelected(hasPrefix);
		
		if(juisteSelecties==null) 
			juisteSelecties = new boolean[itemCount];
	    
		verwijderCheckboxes();
	    aantalSelectables = juisteSelecties.length;
	  //randomizePositionsCB.setSelected(randomizePositions);
	    multiSelectionsCB.setSelected(multiSelections);
	    this.logMisconceptions = logMisconceptions;
	    
	    
	    maakCheckboxes();
	    for(int i=0 ; i<aantalSelectables ; i++) {
	    	selectableCheckboxes[i].setSelected(juisteSelecties[i]);
	    	if(WiskOpdr.misconceptions!=null && logMisconceptionsButtons[i]!=null && logMisconceptions!=null)
	    		logMisconceptionsButtons[i].setChoices(logMisconceptions[i]);
		}
	    enableMisconceptions();
	    
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
	    
	    maxScoreTF.setText(""+scoreMax);
	    logCB.setSelected(logOption);
        logIDField.setVisible(logOption);
        logIDLabelLabel.setVisible(logOption);
        logIDLabelField.setVisible(logOption);
        logIDField.setText(logID);
        logObjectivesButton.setEditState(preferences);
        checkCB.setSelected(check);
        teltMeeCB.setSelected(teltMee);
        logObjectivesButton.setVisible(ObjectiveChoiceButton.hasObjectiveChoices());
        
        //checkFormuleCB.setSelected(checkFormule);
        //if(formuleStrings!=null)formuleEditor.zetRegels(formuleStrings);
        //formuleEditor.setVisible(checkFormule);
        
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
    	
    	volledigeBreedteCB.setSelected(volledigeBreedte);
	    breedteTF.setText(""+breedte);
	    
	    this.MCwidgetID = MCwidgetID;
	    
	    setFeedbackOption(hasFeedback);
        
        if(hasFeedback)
          frame.setSize(950 , 450);
        
		frame.setVisible(true);
		frame.pack();
		//frame.setLocation(tekstVak.getLocationOnScreen().x, tekstVak.getLocationOnScreen().y);
		Dimension screenSize = WiskOpdr.applet.getToolkit().getScreenSize();
 	    int xD = (screenSize.width-frame.getSize().width)/2;
 	    int yD = (screenSize.height-frame.getSize().height)/2;
 	    frame.setLocation(xD, yD);
	}
	
	private Hashtable fillAnswerModel(Hashtable h)
    {
        boolean[] juisteSelecties = new boolean[aantalSelectables];
        for(int i=0 ; i<aantalSelectables ; i++) {  
            juisteSelecties[i] = selectableCheckboxes[i].isSelected();
        }
        String feedback = feedbackEditor.getText();
        int puntenFeedback = intFromText(0,feedbackPV.getText());
        
        int goedHalfFout = goedFoutIP.geefKeuze()-1;

        h.put("juisteSelecties", juisteSelecties);
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
        
        int puntenFeedback = 0;
        if(h.containsKey("puntenFeedback")) puntenFeedback = ((Integer)h.get("puntenFeedback")).intValue();
        
        int goedHalfFout = 2;
        if(h.containsKey("goedHalfFout")) goedHalfFout = ((Integer)h.get("goedHalfFout")).intValue();
            
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
        antwoordEditorPanel.setPreferredSize(new Dimension(150,24));
        antwoordEditorPanel.setBorder(BorderFactory.createEmptyBorder());
      }
      else {
        antwoordEditorPanel.setPreferredSize(new Dimension(400,80));
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
	
	public void produceThisAction(ActionEvent e)
	{	if (actionListener != null)
		{	actionListener.actionPerformed(e);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
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
          antwoordEditorPanel.setPreferredSize(new Dimension(Math.max(150+25*aantalAnswerModels+40,350),80));
          frame.pack();
      }
      else if(e.getSource()==feedbackCB)
      {   setFeedbackOption(feedbackCB.isSelected());
          frame.pack();
      }
      else if(e.getSource().equals(okButton)) {
			produceAction("ok");
			helpBox.setVisible(false);
    		helpTitelBox.setVisible(false);
    		helpBox.validate(); 
			OpdrNavStructEdit.helpBrowser.loadURL(null);
			showHelpButtons(false);
			frame.setVisible(false);
		}
		else if(e.getSource().equals(cancelButton)) {
			helpBox.setVisible(false);
    		helpTitelBox.setVisible(false);
    		helpBox.validate(); 
			OpdrNavStructEdit.helpBrowser.loadURL(null);
			showHelpButtons(false);
			frame.setVisible(false);
		}
		else if(e.getSource()==itemCountTF) {
			int aantal = Math.min(aantalSelectablesMax, intFromText(4, itemCountTF.getText()));
			if(aantal != aantalSelectables)	{	
				verwijderCheckboxes();
				aantalSelectables = aantal;
				maakCheckboxes();
			}
		}
		else if(e.getSource()==multiSelectionsCB) {   
			enableMisconceptions();
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
		else if(e.getSource()==hasPrefixCB) {   
			tabWidthLabel.setVisible(hasPrefixCB.isSelected());
			tabWidthTF.setVisible(hasPrefixCB.isSelected());
			listNumberTypeLabel.setVisible(hasPrefixCB.isSelected());
			listNumberTypeComboBox.setVisible(hasPrefixCB.isSelected());
	    }
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
				knopImageButton.setPreferredSize(new Dimension(Math.max(imWidth,80),Math.max(imHeight,22)));
	         }
	        else {
	    		knopImageButton.setPopupButtonImage(null);
	    		knopImageButton.setCode(WiskOpdr.rb.getString("klaarKnopLabel"));
	    		knopImageButton.setPreferredSize(new Dimension(80,22));
	    	}
	    }
	    else if(e.getSource()==logCB) {   
	    	logIDField.setVisible(logCB.isSelected());
	    	logIDLabelField.setVisible(logCB.isSelected());
	    	logIDLabelLabel.setVisible(logCB.isSelected());
	    	frame.pack();
	    }
	    else if(e.getSource()==hasPrefixCB) {   
	    	listNumberTypeLabel.setVisible(hasPrefixCB.isSelected());
	    	listNumberTypeComboBox.setVisible(hasPrefixCB.isSelected());
	    	frame.pack();
	    }
	    else if(e.getSource()==volledigeBreedteCB) {   
	    	breedteTF.setEnabled(!volledigeBreedteCB.isSelected());
	    }
	    else if (e.getSource() == helpButton) {
        	//if(interactieEditPanel!=null && interactieEditPanel instanceof AntwoordVergelijkingVakEditPanel) 
			//{
        		//((AntwoordVergelijkingVakEditPanel)interactieEditPanel).showHelp(true);
        		
    		helpBox.setVisible(!helpBox.isVisible());
    		helpTitelBox.setVisible(helpBox.isVisible());
    		if(helpBox.isVisible()) {
    			showHelpButtons(true) ;
    			helpBox.validate();
    			OpdrNavStructEdit.helpBrowser.loadURL(geefHelpURL());
            	
            	//packWidth(1100);
    			frame.pack();
    		}
    		else {
    			helpBox.validate(); 
    			OpdrNavStructEdit.helpBrowser.loadURL(null);
    			showHelpButtons(false);
    			//pack();
    			frame.pack();
    		}	
			//}
            
        }
	    else if(e.getSource() instanceof HelpButton)
		{
			OpdrNavStructEdit.helpBrowser.loadURL(((HelpButton)e.getSource()).getURL());
		}
		if(e.getSource() == hideHelpButton) {
          helpBox.setVisible(false);
          helpTitelBox.setVisible(false);
          OpdrNavStructEdit.helpBrowser.loadURL(null);
          showHelpButtons(false);
          frame.pack();
        }
	    //if(imageDialog!=null)
	    //    imageDialog.setVisible(false);
		
	}

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void focusLost(FocusEvent e) {
		if(e.getSource()==itemCountTF) {
			int aantal = Math.min(aantalSelectablesMax, intFromText(4,itemCountTF.getText()));
			if(aantal != aantalSelectables)	{	
				verwijderCheckboxes();
				aantalSelectables = aantal;
				maakCheckboxes();
			}
		}
		if(e.getSource()==feedbackPV && answerModelNr==0) {
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
    	hbAntwoord.setVisible(b);
    	hbKnopImage.setVisible(b);
    	frame.pack();
    }

	@Override
	public String geefHelpURL() {
		return HELP_MULTIPLECHOICE_URL;
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		helpBox.setVisible(false);
		helpTitelBox.setVisible(false);
		helpBox.validate(); 
		OpdrNavStructEdit.helpBrowser.loadURL(null);
		showHelpButtons(false);
		frame.setVisible(false);
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
}

