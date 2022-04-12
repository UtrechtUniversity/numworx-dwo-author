package fi.wiskopdr;

import java.awt.AWTEventMulticaster;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
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

import javax.swing.BorderFactory;
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
import javax.swing.plaf.basic.BasicTabbedPaneUI;

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
    //private JCheckBox feedbackCB;
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
    
    // antwoord
    private JLabel titleAntwoordLabel;
    private Box antwoordBox;
    
    private JTabbedPane tabbedPane;
    private JCheckBox formuleAntwoordModelCB;
    private JCheckBox vergelijkingAntwoordModelCB;
    private JPanel formuleAntwoordTabblad;
    private JPanel vergelijkingAntwoordTabblad;
    
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
    
    
//	public static void zetSignificantieAan(boolean b)
//    {	significantieAan = b;
//    }
	
	public String geefHelpURL() {
		return HELP_0_URL_2;
	}
    
	public BerekeningVakEditPanel()
	{	setLayout(new BorderLayout());
		super.setSize(770,540); //voor dwo
		setBackground(Color.white);	
		setOpaque(true);
		addMouseListener(this);
		addMouseMotionListener(this);
		
		formuleAntwoordManager = new FormuleAntwoordManager(this);
        vergelijkingAntwoordManager = new VergelijkingAntwoordManager(this);
        
		makeGUI();
		
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
    	
        //feedbackCB = makeCheckBox(140,-2,80,20,WiskOpdr.rb.getString("feedbackCBLabel"),false,true);
		
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
	    	
	    	
	    formuleAntwoordModelCB = makeCheckBox("Formule-antwoordmodel",true, true);
	    vergelijkingAntwoordModelCB = makeCheckBox("Vergelijking-antwoordmodel",false, true);
	         
	    tabbedPane = new JTabbedPane();
        tabbedPane.setUI(new BasicTabbedPaneUI() {
            @Override
            protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected)  {
                if(isSelected)g.setColor(WiskOpdr.colorBlue1);
                else g.setColor(Color.white);
                g.fillRect(x, y, w,h);
            }
            @Override
            protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
            }
            @Override
            protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
                g.setColor(WiskOpdr.colorBlue4);
                g.drawRect(x, y, w, h);
            }
            @Override
            protected void paintText(Graphics gr, int tabPlacement, Font font, FontMetrics metrics, int tabIndex, String title, Rectangle textRect, boolean isSelected) {
              Graphics2D g = (Graphics2D)gr;
              ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
              ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
            
              if(isSelected)g.setColor(Color.white);
                else g.setColor(WiskOpdr.colorBlue1);
                g.setFont(font);
                g.drawString(title, textRect.x, textRect.y+textRect.height-2);
            }
        });
        tabbedPane.setForeground(WiskOpdr.colorBlue1);
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(0, -10, 0, 0));
        //tabbedPane.setBounds(0, 0, 1, 1);
        tabbedPane.setMaximumSize(new Dimension(1200,1200));
        tabbedPane.setOpaque(false);
        
        formuleAntwoordTabblad = formuleAntwoordManager.getPanel();
        formuleAntwoordTabblad.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        tabbedPane.add("Formule-antwoordmodel", formuleAntwoordTabblad);
        
        vergelijkingAntwoordTabblad = vergelijkingAntwoordManager.getPanel();
        vergelijkingAntwoordTabblad.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
       
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
	    //antwoordEditorPanel.add(hbAntwoord_2,0);
	    	
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
		//Component[] r47 = {feedbackCB, 			ra(5,0),	hgl(),	hbFeedback};
		Component[] r48 = {formuleToolBijFocusCB, 			ra(5,0),	hgl(),	hbFormInvoer};
		Component[] r410 = {rmKnopCB, 			ra(5,0),	hgl(),	hbRekenmach};
		Component[] r411 = {ra(20,0),			aantalDecRmLabel, 	ra(4,0),	aantalDecRmField,hgl()};
		Component[] r418 = {contextVarCB, 		ra(5,0),	hgl(),	hbContextvar};
		Component[] r421 = {contextBox, 		hgl()};
		Component[] r422 = {titleOpmaakLabel, 	hgl()};
		
		Box settingsBox;
		
		Component[] r424 = {boxMetRandCB, 		ra(5,0),	hgl(),	hbRand_2};
		Component[] r425 = {meerregeligCB, 		ra(5,0),	hgl()};
		Component[] k4 = {hb(r41),vst(5),hb(r42),vst(3),hb(r42a),vst(3),hb(r42b),vst(3),hb(r42c),vst(3),hb(r43),hb(r44),vst(3),hb(r45),vst(20),hb(r46),hb(r48),
				hb(r410),hb(r411),hb(r418), vst(20),hb(r421), hb(r422),vst(5),hb(r424),hb(r425), vgl()};
		settingsBox = vb(k4);
				
		settingsBox.setMaximumSize(new Dimension(500,800));
		
		// boxes plaatsen
		Box boxh = Box.createHorizontalBox();
		mainPanel.add(boxh);
		
		// plaats compoenenten antwoordbox
		Component[] r30 = {ra(450,0), hgl() };
        Component[] r30a = {titleAntwoordLabel, hgl() };
        Component[] r30b = {formuleAntwoordModelCB, hgl()};
        Component[] r30c = {vergelijkingAntwoordModelCB, hgl()};
        Component[] r31 = {tabbedPane, hgl()};
        Component[] k3 = {hb(r30a), vst(5),hb(r30b), hb(r30c),vst(5),hb(r31)};
        antwoordBox = vb(k3);
        //antwoordBox.setVisible(false);
        
      Box boxv1 = Box.createVerticalBox();
      boxv1.add(ra(450,0));
      boxv1.add(antwoordBox);
      boxh.add(boxv1);
        
        boxh.add(Box.createHorizontalStrut(20));
        boxh.add(settingsBox);
        
		contextBox.setVisible(false);
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
	
	public JCheckBox makeCheckBox(String text, boolean selected, boolean visible)
    {   JCheckBox checkbox = new WiskOpdrCheckbox(text);
        checkbox.setFont(font);
        checkbox.setOpaque(false);
        checkbox.addActionListener(this);
        checkbox.setSelected(selected);
        checkbox.setVisible(visible);
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
		boolean formuleToolBijFocus = true;
		boolean rmKnop = false;
		boolean check = true;
		boolean checkDocent = true;
		boolean teltMee = true;
		boolean logOption = false;
		String logID = "";
		String logIDLabel = "";
		int aantalDecRm = 10;
		boolean boxMetRand = true;
		boolean meerregelig = false;
        boolean pastHoogteAan = false;
        String[] antwoordSubStrings = null;
        String[] antwoordFuncStrings = null;
        boolean scoreCumulatief = false;
        boolean hasFormuleAnswerModel = false;
        boolean hasVergelijkingAnswerModel = false;
        Hashtable formuleAntwoordModel = null;
        Hashtable vergelijkingAntwoordModel = null;
		
		if(interactiePanelLaunchState.containsKey("hasFeedback")) hasFeedback = ((Boolean)interactiePanelLaunchState.get("hasFeedback")).booleanValue();
		if(interactiePanelLaunchState.containsKey("rmKnop")) rmKnop = ((Boolean)interactiePanelLaunchState.get("rmKnop")).booleanValue();
		if(interactiePanelLaunchState.containsKey("check")) check = ((Boolean)interactiePanelLaunchState.get("check")).booleanValue();
		if(interactiePanelLaunchState.containsKey("checkDocent")) checkDocent = ((Boolean)interactiePanelLaunchState.get("checkDocent")).booleanValue();
        if(interactiePanelLaunchState.containsKey("teltMee")) teltMee = ((Boolean)interactiePanelLaunchState.get("teltMee")).booleanValue();
		if(interactiePanelLaunchState.containsKey("logOption")) logOption = ((Boolean)interactiePanelLaunchState.get("logOption")).booleanValue();
		if(interactiePanelLaunchState.containsKey("logID")) logID = (String)interactiePanelLaunchState.get("logID");
		if(interactiePanelLaunchState.containsKey("logIDLabel")) logIDLabel = (String)interactiePanelLaunchState.get("logIDLabel");
		if(interactiePanelLaunchState.containsKey("aantalDecRm")) aantalDecRm = ((Integer)interactiePanelLaunchState.get("aantalDecRm")).intValue();
		if(interactiePanelLaunchState.containsKey("boxMetRand")) boxMetRand = ((Boolean)interactiePanelLaunchState.get("boxMetRand")).booleanValue();
		if(interactiePanelLaunchState.containsKey("meerregelig")) meerregelig = ((Boolean)interactiePanelLaunchState.get("meerregelig")).booleanValue();
		if(interactiePanelLaunchState.containsKey("pasAanH")) pastHoogteAan = ((Boolean)interactiePanelLaunchState.get("pasAanH")).booleanValue();
		
		if(interactiePanelLaunchState.containsKey("antwoordSubStrings")) antwoordSubStrings = (String[])interactiePanelLaunchState.get("antwoordSubStrings");
        if(interactiePanelLaunchState.containsKey("antwoordFuncStrings")) antwoordFuncStrings = (String[])interactiePanelLaunchState.get("antwoordFuncStrings");
        if(interactiePanelLaunchState.containsKey("hasFormuleAnswerModel")) hasFormuleAnswerModel = ((Boolean)interactiePanelLaunchState.get("hasFormuleAnswerModel")).booleanValue();
        if(interactiePanelLaunchState.containsKey("hasVergelijkingAnswerModel")) hasVergelijkingAnswerModel = ((Boolean)interactiePanelLaunchState.get("hasVergelijkingAnswerModel")).booleanValue();
        
        if(check && !checkDocent) {
          if(interactiePanelLaunchState.containsKey("formuleAntwoordModel")) formuleAntwoordModel = (Hashtable)interactiePanelLaunchState.get("formuleAntwoordModel");
          formuleAntwoordManager.setEditState(formuleAntwoordModel);
          if(interactiePanelLaunchState.containsKey("vergelijkingAntwoordModel")) vergelijkingAntwoordModel = (Hashtable)interactiePanelLaunchState.get("vergelijkingAntwoordModel");
          vergelijkingAntwoordManager.setEditState(vergelijkingAntwoordModel);
            
        }
        
		this.formuleToolBijFocus = formuleToolBijFocus;
		       
        antwoordSubstitutiesVak.zetRegels(antwoordSubStrings);
        antwoordFunctiesVak.zetRegels(antwoordFuncStrings);
        boolean hasSub = antwoordSubStrings!=null && (antwoordSubStrings.length>0 && !antwoordSubStrings[0].equals("$f@"));
        boolean hasFunc = antwoordFuncStrings!=null && (antwoordFuncStrings.length>0 && !antwoordFuncStrings[0].equals("$f@"));
        contextBox.setVisible(hasSub || hasFunc);
        this.contextVarCB.setSelected(hasSub || hasFunc);
                       
		formuleToolBijFocusCB.setSelected(formuleToolBijFocus);
		    
		teltMeeCB.setSelected(teltMee);
        checkCB.setSelected(check);
        checkDocentRB.setSelected(checkDocent);
        checkAutomatischRB.setSelected(check && !checkDocent);
        formuleAntwoordModelCB.setVisible(check && !checkDocent);
        vergelijkingAntwoordModelCB.setVisible(check && !checkDocent);
        formuleAntwoordModelCB.setSelected(hasFormuleAnswerModel);
        vergelijkingAntwoordModelCB.setSelected(hasVergelijkingAnswerModel);
        scoreLabel.setVisible(checkCB.isSelected() && checkDocentRB.isSelected());
        scoreTF.setVisible(checkCB.isSelected() && checkDocentRB.isSelected());
        checkAutomatischRB.setVisible(checkCB.isSelected());
        checkDocentRB.setVisible(checkCB.isSelected());
            logObjectivesButton.setVisible(ObjectiveChoiceButton.hasObjectiveChoices() && checkCB.isSelected());
            if(!checkCB.isSelected())
                scoreTF.setText("0");
        antwoordBox.setVisible(check && !checkDocent);
        
        if(formuleAntwoordModelCB.isSelected()) 
             tabbedPane.add("Formule-antwoordmodel", formuleAntwoordTabblad);
          
        if(vergelijkingAntwoordModelCB.isSelected()) 
            tabbedPane.add("Vergelijking-antwoordmodel", vergelijkingAntwoordTabblad);
          
        
        
        
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
		        
        pack();
	}
	
	public Hashtable getEditState()
	{	
		Hashtable interactiePanelLaunchState = new Hashtable();
        
        
			int scoreMax = 0;
			int[][] scoreMaxObjectives = null;
			boolean formuleToolBijFocus = true;
			String vormString = "$f@";
			boolean rmKnop = false;
			boolean check = true;
			boolean checkDocent = true;
			boolean teltMee = true;
			boolean logOption = false;
			String logID = "";
			String logIDLabel = "";
			boolean[][] logObjectives = null;
			int aantalDecRm = 10;
			boolean boxMetRand = true;
			boolean meerregelig = false;
			boolean pastHoogteAan = false;
			String[] antwoordSubStrings = null;
            String[] antwoordFuncStrings = null;
            boolean scoreCumulatief = false;
            boolean hasFormuleAnswerModel = false;
            boolean hasVergelijkingAnswerModel = false;
            Hashtable formuleAntwoordModel = null;
            Hashtable vergelijkingAntwoordModel = null;
			
			try
			{	aantalDecRm = Integer.parseInt(aantalDecRmField.getText());
			}	
			catch(Exception ex)
			{	}
			
			antwoordSubStrings = antwoordSubstitutiesVak.geefRegels();
            antwoordFuncStrings = antwoordFunctiesVak.geefRegels();
           	formuleAntwoordManager.setContextVars(antwoordSubStrings, antwoordFuncStrings);
           	vergelijkingAntwoordManager.setContextVars(antwoordSubStrings, antwoordFuncStrings);
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
			hasFormuleAnswerModel = formuleAntwoordModelCB.isSelected();
	        hasVergelijkingAnswerModel = vergelijkingAntwoordModelCB.isSelected();
	        
			formuleToolBijFocus = this.formuleToolBijFocus;

			if(!teltMee)scoreMax = 0;
			
			rmKnop = rmKnopCB.isSelected();
						
			boxMetRand = boxMetRandCB.isSelected();
			meerregelig = meerregeligCB.isSelected();
			pastHoogteAan = pastHoogteAanCB.isSelected();
			
			interactiePanelLaunchState.put("formuleToolBijFocus",new Boolean(formuleToolBijFocus));
			interactiePanelLaunchState.put("scoreMax",new Integer(scoreMax));
			interactiePanelLaunchState.put("rmKnop",new Boolean(rmKnop));
			interactiePanelLaunchState.put("check",new Boolean(check));
			interactiePanelLaunchState.put("checkDocent",new Boolean(checkDocent));
		    interactiePanelLaunchState.put("teltMee",new Boolean(teltMee));
			interactiePanelLaunchState.put("logOption",new Boolean(logOption));
			interactiePanelLaunchState.put("logID",logID);
			interactiePanelLaunchState.put("logIDLabel",logIDLabel);
			interactiePanelLaunchState.put("aantalDecRm",new Integer(aantalDecRm));
			interactiePanelLaunchState.put("boxMetRand",new Boolean(boxMetRand));
			interactiePanelLaunchState.put("meerregelig",new Boolean(meerregelig));
			interactiePanelLaunchState.put("pasAanH",new Boolean(pastHoogteAan));
			
            interactiePanelLaunchState.putAll(logObjectivesButton.getEditState(scoreMax));
                
	        interactiePanelLaunchState.put("antwoordSubStrings",antwoordSubStrings);
            interactiePanelLaunchState.put("antwoordFuncStrings",antwoordFuncStrings);
            interactiePanelLaunchState.put("hasFormuleAnswerModel",new Boolean(hasFormuleAnswerModel));
            interactiePanelLaunchState.put("hasVergelijkingAnswerModel",new Boolean(hasVergelijkingAnswerModel));
            
            if(check && !checkDocent) {
              formuleAntwoordModel = formuleAntwoordManager.getEditState();
              scoreMax = formuleAntwoordManager.getScoreMax();
              interactiePanelLaunchState.put("formuleAntwoordModel",formuleAntwoordModel);
              vergelijkingAntwoordModel = vergelijkingAntwoordManager.getEditState();
              scoreMax += vergelijkingAntwoordManager.getScoreMax();
              interactiePanelLaunchState.put("vergelijkingAntwoordModel",vergelijkingAntwoordModel);
            }
            interactiePanelLaunchState.put("scoreMax", new Integer(scoreMax));
			
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
    {	//antwoordvak.setNewScrollSize();
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
    
  
	public void actionPerformed(ActionEvent e)
	{	
		if(e.getSource() instanceof HelpButton)
		{
			OpdrNavStructEdit.helpBrowser.loadURL(((HelpButton)e.getSource()).getURL());
		}
		
		else if(e.getSource()==contextVarCB)
        {   contextBox.setVisible(contextVarCB.isSelected());
        		((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)mainPanel)).packWidth();
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
              formuleAntwoordModelCB.setVisible(checkCB.isSelected() && checkAutomatischRB.isSelected());
              vergelijkingAntwoordModelCB.setVisible(checkCB.isSelected() && checkAutomatischRB.isSelected());
              logObjectivesButton.setVisible(ObjectiveChoiceButton.hasObjectiveChoices() && checkCB.isSelected());
              if(!checkCB.isSelected())
                  scoreTF.setText("0");
              antwoordBox.setVisible(checkCB.isSelected() && checkAutomatischRB.isSelected());
              //feedbackCB.setVisible(checkCB.isSelected() && checkAutomatischRB.isSelected());
              contextVarCB.setVisible(checkCB.isSelected() && checkAutomatischRB.isSelected());
              pack(); 
        }
        else if(e.getSource()==checkAutomatischRB) { 
            scoreLabel.setVisible(!checkAutomatischRB.isSelected());
            scoreTF.setVisible(!checkAutomatischRB.isSelected());
            antwoordBox.setVisible(checkAutomatischRB.isSelected());
            //feedbackCB.setVisible(checkAutomatischRB.isSelected());
            contextVarCB.setVisible(checkAutomatischRB.isSelected());
            formuleAntwoordModelCB.setVisible(checkAutomatischRB.isSelected());
            vergelijkingAntwoordModelCB.setVisible(checkAutomatischRB.isSelected());
            pack();
        }
        else if(e.getSource()==checkDocentRB) { 
            scoreLabel.setVisible(checkDocentRB.isSelected());
            scoreTF.setVisible(checkDocentRB.isSelected());
            antwoordBox.setVisible(checkAutomatischRB.isSelected());
            //feedbackCB.setVisible(checkAutomatischRB.isSelected());
            contextVarCB.setVisible(checkAutomatischRB.isSelected());
            formuleAntwoordModelCB.setVisible(checkAutomatischRB.isSelected());
            vergelijkingAntwoordModelCB.setVisible(checkAutomatischRB.isSelected());
            pack();
        }
        else if(e.getSource()==formuleAntwoordModelCB) { 
          if(formuleAntwoordModelCB.isSelected()) {
             tabbedPane.add("Formule-antwoordmodel", formuleAntwoordTabblad);
          }
          else
            tabbedPane.remove(formuleAntwoordTabblad);
          pack();
        }
        else if(e.getSource()==vergelijkingAntwoordModelCB) { 
          if(vergelijkingAntwoordModelCB.isSelected()) {
            tabbedPane.add("Vergelijking-antwoordmodel", vergelijkingAntwoordTabblad);
          }
          else 
            tabbedPane.remove(vergelijkingAntwoordTabblad);
          pack();
        }
		else if(e.getSource()==rmKnopCB)
	    {   aantalDecRmField.setVisible(rmKnopCB.isSelected());
	    	aantalDecRmLabel.setVisible(rmKnopCB.isSelected());
	    	((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)mainPanel)).packWidth();

 	    }
		

	}
	public void pack() {
      ((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)mainPanel)).packWidth();
    }
	
	public void setFeedbackOption(boolean b)
	{
		hasFeedback = b;

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
