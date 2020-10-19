package fi.wiskopdr;

import java.awt.AWTEventMulticaster;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import fi.beans.stringutils.StringUtils;
import fi.beans.wiskopdrbeans.*;
import fi.wiskopdr.formuleobjects.*;
import fi.wiskopdr.opdrnav.*;
import fi.wiskopdr.tekstobjects.EditInteractiePanelDialog;
import fi.wiskopdr.tekstobjects.ShareAction;
//import fi.wiskopdr.tekstobjects.VoorwaardelijkeLinkButton;
import fi.wiskopdr.expressies.*;
import fi.beans.base64code.StringCodeObject;
import fi.beans.numworxlf.JScrollPane;


public class InstellingenPanel extends JPanel implements ActionListener
{
	private JCheckBox maalTekenCB;
	private JCheckBox woordFormuleCB;
	private JCheckBox tweeHLVarCB;
	private JCheckBox timerCB;
	private JCheckBox opnieuwCB;
	private JCheckBox itemOpnieuwCB;
	private JCheckBox checkPerOpdrachtCB;
	private JCheckBox hoekGradenCB;
	private JCheckBox bolletjesCB;
	private JCheckBox volgendeKnopCB;
	private JCheckBox vorigeKnopCB;
	private JCheckBox paginaCB;
	private JCheckBox formTimesCB;
	private JCheckBox fToetsCB;
	private JCheckBox globalParamCB;
	private JCheckBox diffOperatorenCB;
	private JCheckBox voortgangCB;
	private JCheckBox condNavCB;
	private JRadioButton condNavPercentageRB;
	private JRadioButton condNavVoorwaardenRB;
	private JCheckBox allesCorrectCB;
	private JCheckBox abcDeelOpdrCB;
	private JCheckBox zelftoetsGeenCorrCB;
	private JCheckBox aftrekCorrectieZelftoetsCB;
	private JCheckBox zelftoetsGeschiedenisCB;
	private JCheckBox zelftoetsHighScoreCB;
	private JCheckBox eerderGeenCorrCB;
	private JCheckBox significantieCB;
	private JCheckBox objectivesCB;
	private JCheckBox pilotObjectivesCB;
	private JCheckBox misconceptionsCB;
	private JCheckBox fontOverervingCB;
	private JCheckBox fontOverervingFormCB;
	private JCheckBox scoresZichtbaarCB;
	private JCheckBox templateEditCB;
	private JCheckBox stylesCB;
	private JButton importStylesButton;
	private JButton importStylesNowButton;
	private JButton exportStylesButton;
	private JCheckBox layersCB;
	private JCheckBox combinedComponentsCB;
	private JLabel styleInteractionsLabel;
	private JComboBox styleInteractionsComboBox;
	private String[] templateNames = {"TemplateBasis","TemplateBasis", "TemplateNumworx", "TemplateUUTest"};
	
	private JLabel wiskundeLabel, navigatieLabel, layoutLabel, nakijkenLabel;
	
	private JLabel fontLabel, fontSizeLabel;
	private JComboBox fontNameCO;
	private JTextField fontSizeTF;
	private String fontName = "SansSerif";
	private int fontSize = 12;
	
	private int condPerc = 100;
	private JTextField condPercTF;
	
	private int aftrekCorrectieZelftoets = 5;
	private JTextField aftrekCorrectieZelftoetsTF;
	
	public VoorwaardelijkeNavigatieButton condButton;
	public LayersButton layersButton;
	
	private JLabel pageLabel;
	private JLabel docWidthLabel;
	private JLabel docHeightLabel;
	private JTextField docWidthTF;
    private JTextField docHeightTF;
    private int docWidth = 1024;
    private int docHeight = 450;
    
	private JLabel margesLabel;
	private JLabel margeLinksLabel;
	private JLabel margeRechtsLabel;
	private JLabel margeOnderLabel;
	private JLabel margeBovenLabel;
	private JTextField margeLinksTF;
	private JTextField margeRechtsTF;
	private JTextField margeBovenTF;
	private JTextField margeOnderTF;
	private int margeLinks = 10;
	private int margeRechts = 10;
	private int margeBoven = 10;
	private int margeOnder = 10;
	
	private JComboBox keyboardCombobox;
	private JLabel keyboardLabel;
	private JComboBox keyboardVersionCombobox;
	private JLabel keyboardVersionLabel;
	private JLabel writeMathLabel;
	private JComboBox writeMathCombobox;
	
	HelpButton helpButton;
	private JPanel helpPanel;
    private Box helpBox;
    private Box helpTitelBox;
    private JButton hideHelpButton;
	
	private String[] fontNames = {
			"SansSerif",
			"Verdana",
			"Arial",
			"TimesRoman",
	};
	
	
	private JLabel navigatieSizeLabel;
	private JTextField navigatieSizeTF;
	private int navigatieSize = 12;
	
	private JLabel timerLabel;
	private JTextField timerTF;
	private int timeLimit = 60;
	
	private ObjectiveSettingsButton objectivesButton;
	private ObjectiveSettingsButton misconceptionsButton;
	
	private JButton okButton, cancelButton;
	
	private DialogFacade dialog;
	private DialogFacade stylesExportDialog;
	private JTextArea stylesExportTekstArea;
	private DialogFacade stylesImportDialog;
	private JTextArea stylesImportTekstArea;

	
	
	private Font font = new Font("SansSerif",Font.PLAIN,12);
	private Font boldFont = new Font("SansSerif", Font.BOLD, 16);
	private OpdrNavStructEdit opdrNavStruct;
	
	private static String HELP_INST_URL = WiskOpdr.rb.getString("HELP_INST_URL"); 
	private static String HELP_INST_URL_MAALTEKEN = WiskOpdr.rb.getString("HELP_INST_URL_MAALTEKEN");
	private static String HELP_INST_URL_WOORDFORMULE = WiskOpdr.rb.getString("HELP_INST_URL_WOORDFORMULE");
	private static String HELP_INST_URL_TWEEHLVAR = WiskOpdr.rb.getString("HELP_INST_URL_TWEEHLVAR");
	private static String HELP_INST_URL_HOEKGRADEN = WiskOpdr.rb.getString("HELP_INST_URL_HOEKGRADEN");
	private static String HELP_INST_URL_SIGNIFICANTIE = WiskOpdr.rb.getString("HELP_INST_URL_SIGNIFICANTIE");
	private static String HELP_INST_URL_GLOBAALPARAM = WiskOpdr.rb.getString("HELP_INST_URL_GLOBAALPARAM");
	private static String HELP_INST_URL_DIFFOPERATOREN = WiskOpdr.rb.getString("HELP_INST_URL_DIFFOPERATOREN");
	private static String HELP_INST_URL_BOLLETJES = WiskOpdr.rb.getString("HELP_INST_URL_BOLLETJES");
	private static String HELP_INST_URL_VOLGENDEKNOP = WiskOpdr.rb.getString("HELP_INST_URL_VOLGENDEKNOP");
	private static String HELP_INST_URL_VORIGEKNOP = WiskOpdr.rb.getString("HELP_INST_URL_VORIGEKNOP");
	private static String HELP_INST_URL_VOORTGANG = WiskOpdr.rb.getString("HELP_INST_URL_VOORTGANG");
	private static String HELP_INST_URL_CONDNAV = WiskOpdr.rb.getString("HELP_INST_URL_CONDNAV");
	private static String HELP_INST_URL_COMBINEDCOMPONENTS = WiskOpdr.rb.getString("HELP_INST_URL_COMBINEDCOMPONENTS");
	private static String HELP_INST_URL_FORMTIMES = WiskOpdr.rb.getString("HELP_INST_URL_FORMTIMES");
	private static String HELP_INST_URL_FONTOVERERVING = WiskOpdr.rb.getString("HELP_INST_URL_FONTOVERERVING");
	private static String HELP_INST_URL_FONTOVERERVINGFORM = WiskOpdr.rb.getString("HELP_INST_URL_FONTOVERERVINGFORM");
	private static String HELP_INST_URL_TEMPLATEEDIT = WiskOpdr.rb.getString("HELP_INST_URL_TEMPLATEEDIT");
	private static String HELP_INST_URL_STYLES = WiskOpdr.rb.getString("HELP_INST_URL_STYLES");
	private static String HELP_INST_URL_LAYERS = WiskOpdr.rb.getString("HELP_INST_URL_LAYERS");
	private static String HELP_INST_URL_SCORESZICHTBAAR = WiskOpdr.rb.getString("HELP_INST_URL_SCORESZICHTBAAR");
	private static String HELP_INST_URL_OPNIEUW = WiskOpdr.rb.getString("HELP_INST_URL_OPNIEUW");
	private static String HELP_INST_URL_ITEMOPNIEUW = WiskOpdr.rb.getString("HELP_INST_URL_ITEMOPNIEUW");
	private static String HELP_INST_URL_ZELFTOETSGEENCORR = WiskOpdr.rb.getString("HELP_INST_URL_ZELFTOETSGEENCORR");
	private static String HELP_INST_URL_ZELFTOETSGESCHIEDENIS = WiskOpdr.rb.getString("HELP_INST_URL_ZELFTOETSGESCHIEDENIS");
	private static String HELP_INST_URL_AFTREKCORRZELFTOETS = WiskOpdr.rb.getString("HELP_INST_URL_AFTREKCORRZELFTOETS");
	private static String HELP_INST_URL_EERDERGEENCORR = WiskOpdr.rb.getString("HELP_INST_URL_EERDERGEENCORR");
	private static String HELP_INST_URL_TIMER = WiskOpdr.rb.getString("HELP_INST_URL_TIMER");
	private static String HELP_INST_URL_OBJECTIVES = WiskOpdr.rb.getString("HELP_INST_URL_OBJECTIVES");
	
	private HelpButton hbMaalTeken = makeHelpButton(HELP_INST_URL_MAALTEKEN);
	private HelpButton hbWoordFormule = makeHelpButton(HELP_INST_URL_WOORDFORMULE);
	private HelpButton hbTweeHLVar = makeHelpButton(HELP_INST_URL_TWEEHLVAR);
	private HelpButton hbHoekGraden = makeHelpButton(HELP_INST_URL_HOEKGRADEN);
	private HelpButton hbSignifacantie = makeHelpButton(HELP_INST_URL_SIGNIFICANTIE);
	private HelpButton hbGlobaalParam = makeHelpButton(HELP_INST_URL_GLOBAALPARAM);
	private HelpButton hbDiffOperatoren = makeHelpButton(HELP_INST_URL_DIFFOPERATOREN);
	private HelpButton hbBolletjes = makeHelpButton(HELP_INST_URL_BOLLETJES);
	private HelpButton hbVolgendeKnop = makeHelpButton(HELP_INST_URL_VOLGENDEKNOP);
	private HelpButton hbVorigeKnop = makeHelpButton(HELP_INST_URL_VORIGEKNOP);
	private HelpButton hbVoortgang = makeHelpButton(HELP_INST_URL_VOORTGANG);
	private HelpButton hbCondNav = makeHelpButton(HELP_INST_URL_CONDNAV);
	private HelpButton hbCombinedComponents = makeHelpButton(HELP_INST_URL_COMBINEDCOMPONENTS);
	private HelpButton hbFormTimes = makeHelpButton(HELP_INST_URL_FORMTIMES);
	private HelpButton hbFontOvererving = makeHelpButton(HELP_INST_URL_FONTOVERERVING);
	private HelpButton hbFontOverervingForm = makeHelpButton(HELP_INST_URL_FONTOVERERVINGFORM);
	private HelpButton hbTemplateEdit = makeHelpButton(HELP_INST_URL_TEMPLATEEDIT);
	private HelpButton hbStyles = makeHelpButton(HELP_INST_URL_STYLES);
	private HelpButton hbLayers = makeHelpButton(HELP_INST_URL_LAYERS);
	private HelpButton hbScoresZichtbaar = makeHelpButton(HELP_INST_URL_SCORESZICHTBAAR);
	private HelpButton hbOpnieuw = makeHelpButton(HELP_INST_URL_OPNIEUW);
	private HelpButton hbItemOpnieuw = makeHelpButton(HELP_INST_URL_ITEMOPNIEUW);
	private HelpButton hbZelftoetsGeenCorr = makeHelpButton(HELP_INST_URL_ZELFTOETSGEENCORR);
	private HelpButton hbZelftoetsGeschiedenis = makeHelpButton(HELP_INST_URL_ZELFTOETSGESCHIEDENIS);
	private HelpButton hbAftrekCorrZelftoets = makeHelpButton(HELP_INST_URL_AFTREKCORRZELFTOETS);
	private HelpButton hEerderGeenCorr = makeHelpButton(HELP_INST_URL_EERDERGEENCORR);
	private HelpButton hbTimer = makeHelpButton(HELP_INST_URL_TIMER);
	private HelpButton hbObjectives = makeHelpButton(HELP_INST_URL_OBJECTIVES);
	
	
	public InstellingenPanel(DialogFacade dialog, OpdrNavStructEdit opdrNavStruct)
	{	
		setLayout(new BorderLayout());
		setBackground(WiskOpdr.bgcolorEditor);
		
		 JLabel headerTitle = new JLabel(WiskOpdr.rb.getString("OPT_instellingenActiviteitTitel"));
	        headerTitle.setForeground(WiskOpdr.colorGray3);
	        headerTitle.setFont(new Font("SansSerif",Font.PLAIN, 24));
	        //headerPanel.add(headerTitle);
	        
	        String HELP_URL1 = "https://app.dwo.nl/public/?header=less&hash=#s:670047";
			 helpButton = new HelpButton(HELP_URL1);
			//helpButton.setFont(new Font("SansSerif",Font.BOLD,14));
			helpButton.setPreferredSize(new Dimension(22,22));
			helpButton.setMinimumSize(new Dimension(22,22));
			helpButton.setMaximumSize(new Dimension(22,22));
			helpButton.addActionListener(this);
			//headerPanel.add(helpButton);
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
			helpBox.setVisible(false);
			helpBox.setMaximumSize(new Dimension(300,800));
			
			JLabel helpTitleLabel = new JLabel(WiskOpdr.rb.getString("helpTitelLabel"));
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
			//helpheader.setMaximumSize(new Dimension(300,0));
			helpTitelBox.setPreferredSize(new Dimension(300,30));
			helpTitelBox.add(helpheader);
			helpTitelBox.setVisible(false);
			
			Box headerbox = Box.createHorizontalBox();
			headerbox.add(Box.createHorizontalGlue());
			headerbox.add(headerTitle);
			headerbox.add(Box.createHorizontalGlue());
			
			headerbox.add(helpButton);
			headerbox.add(helpTitelBox);
			
			
	        
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
		topPanel.setOpaque(true);
		topPanel.setBackground(new Color(49,71,112));
//		JLabel topLabel = new JLabel("Instellingen Activiteit");
//		topLabel.setFont(new Font("SansSerif",Font.BOLD, 28));
//		topLabel.setForeground(new Color(237,239,241));
//		topPanel.add(topLabel);
		
		topPanel.add(headerbox);
		
		add(BorderLayout.NORTH,topPanel);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(2,2));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		mainPanel.setOpaque(false);
		
		JScrollPane scrollPane = new JScrollPane(mainPanel);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.setMinimumSize(new Dimension(780,400));
		scrollPane.getComponent(0).setBackground(WiskOpdr.colorGray3);
		
		Box hb = Box.createHorizontalBox();
        hb.add(scrollPane);
        hb.add(helpBox);
        
        add(hb);
		//add(bottomPanel,BorderLayout.SOUTH);
		add(topPanel,BorderLayout.NORTH);
        
		JPanel bottomPanel = new JPanel();
		
		stylesExportTekstArea = new JTextArea();
		stylesExportTekstArea.setBounds(0, 0, 600, 400);
		
		stylesExportDialog = DialogFacade.newInstance(this, "styles", true);
		stylesExportDialog.setSize(stylesExportTekstArea.getSize());
		JScrollPane scrollpaneExport = new JScrollPane(stylesExportTekstArea);
		stylesExportDialog.getContentPane().add(scrollpaneExport, BorderLayout.CENTER);
		Dimension screenSize = WiskOpdr.applet.getToolkit().getScreenSize();
	    int x = (screenSize.width-stylesExportDialog.getSize().width)/2;
	    int y = (screenSize.height-stylesExportDialog.getSize().height)/2;
	    stylesExportDialog.setLocation(x , y);
		
		stylesImportTekstArea = new JTextArea();
		stylesImportTekstArea.setBounds(0, 0, 600, 400);
		
		stylesImportDialog = DialogFacade.newInstance(this, "styles", true);
		stylesImportDialog.setSize(stylesImportTekstArea.getSize());
		JScrollPane scrollpaneImport = new JScrollPane(stylesImportTekstArea);
		stylesImportDialog.getContentPane().add(scrollpaneImport, BorderLayout.CENTER);
		screenSize = WiskOpdr.applet.getToolkit().getScreenSize();
	    x = (screenSize.width-stylesImportDialog.getSize().width)/2;
	    y = (screenSize.height-stylesImportDialog.getSize().height)/2;
	    stylesImportDialog.setLocation(x , y);
		
		importStylesNowButton = new WiskOpdrButton(WiskOpdr.rb.getString("OPT_importStyles"));
		importStylesNowButton.addActionListener(this);
		importStylesNowButton.setFont(font);
		stylesImportDialog.getContentPane().add(importStylesNowButton, BorderLayout.SOUTH);
		
		
		//add(mainPanel);
		add(bottomPanel,BorderLayout.SOUTH);
		add(hb);
		
		this.dialog = dialog;
		this.opdrNavStruct = opdrNavStruct;
		
		
		
		//Wiskunde-opties
		Box boxv1 = Box.createVerticalBox();
		boxv1.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 40));
		//boxv1.add(Box.createHorizontalStrut(10));
		//boxv1.add(Box.createVerticalStrut(20));
		wiskundeLabel = maakLabel(WiskOpdr.rb.getString("OPT_wiskundeLabel"), boxv1);
		maalTekenCB = maakCheckBoxHelp(hbMaalTeken,WiskOpdr.rb.getString("OPT_vermenigvTeken"), boxv1, false);//"Vermenigvuldigingsteken X"
		woordFormuleCB = maakCheckBoxHelp(hbWoordFormule,WiskOpdr.rb.getString("OPT_woordformules"), boxv1, false);//"Woordformules"
		tweeHLVarCB = maakCheckBoxHelp(hbTweeHLVar,WiskOpdr.rb.getString("OPT_tweeHoofdletterVars"), boxv1, false);//"Twee-hoofdletter variabelen "
		hoekGradenCB = maakCheckBoxHelp(hbHoekGraden,WiskOpdr.rb.getString("OPT_hoekInGraden"),boxv1, false);//"Hoekberekeningen in graden"
		fToetsCB = maakCheckBox(WiskOpdr.rb.getString("OPT_fToets"), boxv1, true);//"F-toetsen gebruiken of niet"
		boxv1.remove(boxv1.getComponentCount()-1);
		significantieCB = maakCheckBoxHelp(hbSignifacantie,WiskOpdr.rb.getString("OPT_significantie"), boxv1, false); //"Checkmogelijkheid significante getallen"
		globalParamCB = maakCheckBoxHelp(hbGlobaalParam,WiskOpdr.rb.getString("OPT_globalParam"), boxv1, false);//"Globale parameters"
		diffOperatorenCB = maakCheckBoxHelp(hbDiffOperatoren,WiskOpdr.rb.getString("OPT_diffOperatoren"), boxv1, false);
		//diffOperatorenCB.setVisible(false);
		
		Box boxh = Box.createHorizontalBox();
		keyboardLabel = new JLabel(WiskOpdr.rb.getString("Tablet keyboard")+" ");
		keyboardLabel.setFont(font);
		keyboardLabel.setForeground(WiskOpdr.fgcolorEditor);
		boxh.add(keyboardLabel);
		boxh.add(Box.createRigidArea(new Dimension(10,0)));
		boxh.add(Box.createHorizontalGlue());
		
		keyboardCombobox = new WiskOpdrComboBox();
		keyboardCombobox.setFont(font);
		keyboardCombobox.setPreferredSize(new Dimension(180,24));
		keyboardCombobox.setForeground(WiskOpdr.fgcolorEditor);
		
		if("GR".equals(WiskOpdr.deployVariant) || "MW".equals(WiskOpdr.deployVariant)) {
			keyboardCombobox.addItem(WiskOpdr.rb.getString("Onderbouw-keyboard"));
			keyboardCombobox.addItem(WiskOpdr.rb.getString("Algebra-keyboard"));
			keyboardCombobox.addItem(WiskOpdr.rb.getString("Gonio-keyboard"));
			keyboardCombobox.addItem(WiskOpdr.rb.getString("Statistiek-keyboard"));
			keyboardCombobox.addItem(WiskOpdr.rb.getString("Meetkunde-keyboard"));
		}
		else {
			keyboardCombobox.addItem(WiskOpdr.rb.getString("Algebra-keyboard"));
			keyboardCombobox.addItem(WiskOpdr.rb.getString("Onderbouw-keyboard"));
		}
		boxh.add(keyboardCombobox);
		//boxh.add(Box.createHorizontalStrut(80));
		boxv1.add(Box.createVerticalStrut(5));
		boxv1.add(boxh);
		//boxv1.add(Box.createVerticalStrut(5));
		
		boxh = Box.createHorizontalBox();
		keyboardVersionLabel = new JLabel(WiskOpdr.rb.getString("versionKeyboard")+" ");
		keyboardVersionLabel.setFont(font);
		keyboardVersionLabel.setForeground(WiskOpdr.fgcolorEditor);
		boxh.add(keyboardVersionLabel);
		boxh.add(Box.createRigidArea(new Dimension(10,0)));
		boxh.add(Box.createHorizontalGlue());
		
		keyboardVersionCombobox = new WiskOpdrComboBox();
		keyboardVersionCombobox.setFont(font);
		keyboardVersionCombobox.setPreferredSize(new Dimension(180,24));
		keyboardVersionCombobox.setForeground(WiskOpdr.fgcolorEditor);
		
		keyboardVersionCombobox.addItem(WiskOpdr.rb.getString("keyBoardAutomatisch"));
		keyboardVersionCombobox.addItem(WiskOpdr.rb.getString("keyBoardDesktop"));
		keyboardVersionCombobox.addItem(WiskOpdr.rb.getString("keyBoardTablet"));
		
		boxh.add(keyboardVersionCombobox);
		//boxh.add(Box.createHorizontalStrut(80));
		boxv1.add(Box.createVerticalStrut(5));
		boxv1.add(boxh);
		boxv1.add(Box.createVerticalStrut(5));
		
		boxh = Box.createHorizontalBox();
		writeMathLabel = new JLabel(WiskOpdr.rb.getString("Tablet handschriftset")+" ");
		writeMathLabel.setFont(font);
		writeMathLabel.setForeground(WiskOpdr.fgcolorEditor);
		if("GR".equals(WiskOpdr.deployVariant) || "MW".equals(WiskOpdr.deployVariant)) {
			boxh.add(writeMathLabel);
			boxh.add(Box.createHorizontalStrut(10));
		}
		
		writeMathCombobox = new WiskOpdrComboBox();
		writeMathCombobox.setFont(font);
		writeMathCombobox.setForeground(WiskOpdr.fgcolorEditor);
		writeMathCombobox.addItem(WiskOpdr.rb.getString("Basis"));
		writeMathCombobox.addItem(WiskOpdr.rb.getString("Uitgebreid"));
		if("GR".equals(WiskOpdr.deployVariant) || "MW".equals(WiskOpdr.deployVariant)) {
			boxh.add(writeMathCombobox);
			boxh.add(Box.createHorizontalStrut(80));
			boxv1.add(boxh);
			boxv1.add(Box.createVerticalStrut(90));
		}
		
		//Navigatie-opties
		Box boxv2 = Box.createVerticalBox();
		boxv2.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 40));
		
		navigatieLabel = maakLabel(WiskOpdr.rb.getString("OPT_navigatieLabel"), boxv2);
		bolletjesCB = maakCheckBoxHelp(hbBolletjes,WiskOpdr.rb.getString("OPT_opdrachtBolletjes"), boxv2, true);//"Opdrachtbolletjes"
		volgendeKnopCB = maakCheckBoxHelp(hbVolgendeKnop,WiskOpdr.rb.getString("OPT_volgendeKnop"), boxv2, false);//"volgende-knop zichtbaar"
		vorigeKnopCB = maakCheckBoxHelp(hbVorigeKnop,WiskOpdr.rb.getString("OPT_VorigeKnop"), boxv2, false);//"vorige-knop zichtbaar"
		voortgangCB = maakCheckBoxHelp(hbVoortgang,WiskOpdr.rb.getString("OPT_voortgangKnop"), boxv2, false);
		condNavCB = maakCheckBoxHelp(hbCondNav,WiskOpdr.rb.getString("OPT_conditionalNav"), boxv2, false);
		condNavCB.addActionListener(this);
		
		
		
		//Box boxh;
		boxh = Box.createHorizontalBox();
		//condNavCB = new JCheckBox(WiskOpdr.rb.getString("OPT_conditionalNav"));
		//condNavCB.setOpaque(false);
		//condNavCB.setFont(font);
		//condNavCB.setSelected(false);
		//boxh.add(condNavCB);
		
		boxh.add(Box.createRigidArea(new Dimension(20,0)));
		
		condNavPercentageRB = new WiskOpdrRadioButton(WiskOpdr.rb.getString("OPT_conditionalPercLabel"));
		condNavPercentageRB.addActionListener(this);
		condNavPercentageRB.setFont(font);
		condNavPercentageRB.setForeground(WiskOpdr.fgcolorEditor);
		condNavPercentageRB.setOpaque(false);
		condNavPercentageRB.setVisible(false);
		condNavPercentageRB.setSelected(true);
		boxh.add(condNavPercentageRB);
		boxh.add(Box.createRigidArea(new Dimension(10,0)));
		
		condPercTF = new WiskOpdrTextField(""+condPerc);
		condPercTF.setFont(font);
		//condPercTF.setBorder(BorderFactory.createLineBorder(new Color(120,150,202)));
		condPercTF.setForeground(WiskOpdr.fgcolorEditor);
		condPercTF.setPreferredSize(new Dimension(50,22));
		condPercTF.setMaximumSize(new Dimension(50,22));
		condPercTF.setVisible(false);
		boxh.add(condPercTF);
		boxh.add(Box.createHorizontalGlue());
		boxh.setAlignmentY(Component.LEFT_ALIGNMENT);
		boxv2.add(boxh);
		
		boxh = Box.createHorizontalBox();
		boxh.add(Box.createRigidArea(new Dimension(20,0)));
		
		condNavVoorwaardenRB = new WiskOpdrRadioButton(WiskOpdr.rb.getString("OPT_voorwaarden"));
		condNavVoorwaardenRB.addActionListener(this);
		condNavVoorwaardenRB.setOpaque(false);
		condNavVoorwaardenRB.setFont(font);
		condNavVoorwaardenRB.setForeground(WiskOpdr.fgcolorEditor);
		condNavVoorwaardenRB.setSelected(false);
		condNavVoorwaardenRB.setVisible(false);
		boxh.add(condNavVoorwaardenRB);
		
		boxh.add(Box.createRigidArea(new Dimension(10,0)));
		
		condButton = new VoorwaardelijkeNavigatieButton();
		//condButton.setFont(font);
		condButton.setPreferredSize(new Dimension(100,24));
		condButton.setMaximumSize(new Dimension(100,24));
		condButton.setVisible(false);
		boxh.add(condButton);
		boxh.add(Box.createHorizontalGlue());
		boxh.setAlignmentY(Component.LEFT_ALIGNMENT);
		boxv2.add(boxh);
		
		ButtonGroup group = new ButtonGroup();
	    group.add(condNavPercentageRB);
	    group.add(condNavVoorwaardenRB);
		
	    boxh = Box.createHorizontalBox();
	    boxh.add(Box.createRigidArea(new Dimension(20,0)));
		
		allesCorrectCB = new WiskOpdrCheckbox(WiskOpdr.rb.getString("OPT_allesCorrect"));
		allesCorrectCB.setOpaque(false);
		allesCorrectCB.setFont(font);
		allesCorrectCB.setForeground(WiskOpdr.fgcolorEditor);
		allesCorrectCB.setSelected(false);
		allesCorrectCB.setVisible(false);
		boxh.add(allesCorrectCB);
		boxh.add(Box.createHorizontalGlue());
		boxv2.add(boxh);
		
		combinedComponentsCB = maakCheckBoxHelp(hbCombinedComponents,WiskOpdr.rb.getString("OPT_combCompNav"), boxv2, false);
	    
	    //boxv2.add(Box.createVerticalStrut(130));
	    
		
		
		//Layout-opties
		Box boxv3 = Box.createVerticalBox();
		layoutLabel = maakLabel(WiskOpdr.rb.getString("OPT_layoutLabel"), boxv3);
		
		boxh = Box.createHorizontalBox();
		
		pageLabel = new JLabel(WiskOpdr.rb.getString("OPT_pageLabel"));
		pageLabel.setFont(font);
		pageLabel.setForeground(WiskOpdr.fgcolorEditor);
        boxh.add(pageLabel);
        boxh.add(Box.createRigidArea(new Dimension(10,0)));
        
        docWidthLabel = new JLabel(WiskOpdr.rb.getString("OPT_docWidthLabel"));
        docWidthLabel.setFont(font);
        docWidthLabel.setForeground(WiskOpdr.fgcolorEditor);
        boxh.add(docWidthLabel);
        boxh.add(Box.createRigidArea(new Dimension(10,0)));
        
        docWidthTF = new WiskOpdrTextField(""+docWidth);
        docWidthTF.setFont(font);
        docWidthTF.setPreferredSize(new Dimension(50,22));
        docWidthTF.setForeground(WiskOpdr.fgcolorEditor);
        boxh.add(docWidthTF);
        boxh.add(Box.createRigidArea(new Dimension(10,0)));
        
        docHeightLabel = new JLabel(WiskOpdr.rb.getString("OPT_docHeightLabel"));
        docHeightLabel.setFont(font);
        docHeightLabel.setForeground(WiskOpdr.fgcolorEditor);
        boxh.add(docHeightLabel);
        boxh.add(Box.createRigidArea(new Dimension(10,0)));
        
        docHeightTF = new WiskOpdrTextField(""+docHeight);
        docHeightTF.setFont(font);
        docHeightTF.setPreferredSize(new Dimension(50,22));
        docHeightTF.setForeground(WiskOpdr.fgcolorEditor);
        boxh.add(docHeightTF);
        
        
        boxv3.add(boxh);
        boxv3.add(Box.createVerticalStrut(5));

		boxh = Box.createHorizontalBox();
		margesLabel = new JLabel(WiskOpdr.rb.getString("OPT_margesLabel"));
		margesLabel.setFont(font);
		margesLabel.setForeground(WiskOpdr.fgcolorEditor);
		boxh.add(margesLabel);
		boxh.add(Box.createRigidArea(new Dimension(10,0)));
		
		
		//boxv3.add(boxh);
		//boxv3.add(Box.createVerticalStrut(5));
		
		//boxh = Box.createHorizontalBox();
		margeLinksLabel = new JLabel(WiskOpdr.rb.getString("OPT_margeLinksLabel")+" ");
		margeLinksLabel.setFont(font);
		margeLinksLabel.setForeground(WiskOpdr.fgcolorEditor);
		boxh.add(margeLinksLabel);
		boxh.add(Box.createRigidArea(new Dimension(10,0)));
		
		margeLinksTF = new WiskOpdrTextField(""+margeLinks);
		margeLinksTF.setFont(font);
		margeLinksTF.setPreferredSize(new Dimension(50,22));
		margeLinksTF.setForeground(WiskOpdr.fgcolorEditor);
		//margeLinksTF.setSize(new Dimension(50,24));
		boxh.add(margeLinksTF);
		boxh.add(Box.createRigidArea(new Dimension(10,0)));
		
		margeRechtsLabel = new JLabel(WiskOpdr.rb.getString("OPT_margeRechtsLabel")+" ");
		margeRechtsLabel.setFont(font);
		margeRechtsLabel.setForeground(WiskOpdr.fgcolorEditor);
		//boxh.add(margeRechtsLabel);
		
		margeRechtsTF = new WiskOpdrTextField(""+margeRechts);
		margeRechtsTF.setFont(font);
		margeRechtsTF.setPreferredSize(new Dimension(50,22));
		margeRechtsTF.setForeground(WiskOpdr.fgcolorEditor);
		//margeRechtsTF.setSize(new Dimension(50,24));
		//boxh.add(margeRechtsTF);
		//boxh.add(Box.createHorizontalStrut(10));
		
		margeBovenLabel = new JLabel(WiskOpdr.rb.getString("OPT_margeBovenLabel")+" ");
		margeBovenLabel.setFont(font);
		margeBovenLabel.setForeground(WiskOpdr.fgcolorEditor);
		boxh.add(margeBovenLabel);
		boxh.add(Box.createRigidArea(new Dimension(10,0)));
		 
		margeBovenTF = new WiskOpdrTextField(""+margeBoven);
		margeBovenTF.setFont(font);
		margeBovenTF.setPreferredSize(new Dimension(50,22));
		margeBovenTF.setForeground(WiskOpdr.fgcolorEditor);
		//margeBovenTF.setSize(new Dimension(50,24));
		boxh.add(margeBovenTF);
		
		
		margeOnderLabel = new JLabel(WiskOpdr.rb.getString("OPT_margeOnderLabel")+" ");
		margeOnderLabel.setFont(font);
		margeOnderLabel.setForeground(WiskOpdr.fgcolorEditor);
		//boxh.add(margeOnderLabel);
		
		margeOnderTF = new WiskOpdrTextField(""+margeOnder);
		margeOnderTF.setFont(font);
		margeOnderTF.setPreferredSize(new Dimension(50,22));
		margeOnderTF.setForeground(WiskOpdr.fgcolorEditor);
		//margeOnderTF.setSize(new Dimension(50,24));
		//boxh.add(margeOnderTF);
		//boxh.add(Box.createHorizontalGlue());
		
		
		boxv3.add(boxh);
		boxv3.add(Box.createVerticalStrut(5));
		
		boxh = Box.createHorizontalBox();
		
		fontLabel = new JLabel(WiskOpdr.rb.getString("OPT_fontNaam"));
		fontLabel.setFont(font);
		fontLabel.setForeground(WiskOpdr.fgcolorEditor);
		boxh.add(fontLabel);
		boxh.add(Box.createRigidArea(new Dimension(10,0)));
		
		fontNameCO = new WiskOpdrComboBox();
		fontNameCO.setFont(font);
		fontNameCO.setForeground(WiskOpdr.fgcolorEditor);
		for(int i=0 ; i<fontNames.length ; i++)
		{	fontNameCO.addItem(fontNames[i]);
		}
		fontNameCO.setPreferredSize(new Dimension(100,22));
		boxh.add(fontNameCO);
		
		boxv3.add(boxh);
		boxh.add(Box.createRigidArea(new Dimension(10,0)));
		
		fontSizeLabel = new JLabel(WiskOpdr.rb.getString("OPT_fontFormaat"));
		fontSizeLabel.setFont(font);
		fontSizeLabel.setForeground(WiskOpdr.fgcolorEditor);
		boxh.add(fontSizeLabel);
		boxh.add(Box.createRigidArea(new Dimension(10,0)));
				
		fontSizeTF = new WiskOpdrTextField(""+fontSize);
		fontSizeTF.setFont(font);
		fontSizeTF.setPreferredSize(new Dimension(50,22));
		fontSizeTF.setForeground(WiskOpdr.fgcolorEditor);
		//fontSizeTF.setSize(new Dimension(50,24));
		boxh.add(fontSizeTF);
		
		boxv3.add(boxh);
		boxv3.add(Box.createVerticalStrut(5));
		
		boxh = Box.createHorizontalBox();
		
		navigatieSizeLabel = new JLabel(WiskOpdr.rb.getString("OPT_navigatieFormaat"));
		navigatieSizeLabel.setFont(font);
		navigatieSizeLabel.setForeground(WiskOpdr.fgcolorEditor);
		navigatieSizeLabel.setForeground(WiskOpdr.fgcolorEditor);
		//boxh.add(navigatieSizeLabel);
		boxh.add(Box.createRigidArea(new Dimension(10,0)));
		
		navigatieSizeTF = new WiskOpdrTextField(""+navigatieSize);
		navigatieSizeTF.setFont(font);
		navigatieSizeTF.setPreferredSize(new Dimension(50,22));
		//navigatieSizeTF.setSize(new Dimension(50,24));
		//boxh.add(navigatieSizeTF);
		//boxh.add(Box.createGlue());
		//boxv3.add(boxh);
		
		formTimesCB = maakCheckBoxHelp(hbFormTimes,WiskOpdr.rb.getString("OPT_formTimes"), boxv3, true);//"formules in Times Roman"
		//paginaCB = maakCheckBox(WiskOpdr.rb.getString("OPT_paginaIpvOpdracht"), boxv3, false);//"pagina ipv opdracht"
		//abcDeelOpdrCB = maakCheckBox(WiskOpdr.rb.getString("OPT_deelOpdr"), boxv3, false);//"F-toetsen gebruiken of niet"
		fontOverervingCB = maakCheckBoxHelp(hbFontOvererving,WiskOpdr.rb.getString("OPT_fontOvererving"), boxv3, false);//"Font-overerving tekstvakken"
		fontOverervingFormCB = maakCheckBoxHelp(hbFontOverervingForm,WiskOpdr.rb.getString("OPT_fontOverervingForm"), boxv3, false);
		templateEditCB = maakCheckBoxHelp(hbTemplateEdit,WiskOpdr.rb.getString("OPT_templateEditor"), boxv3, false);
		
		boxh = Box.createHorizontalBox();
		boolean manageStyles = TekstVakPanel.styles!=null;
		stylesCB = maakCheckBox(WiskOpdr.rb.getString("OPT_styles"), boxh, manageStyles);
		stylesCB.addActionListener(this);
		boxh.add(Box.createRigidArea(new Dimension(10,0)));
		importStylesButton = new WiskOpdrButton(WiskOpdr.rb.getString("OPT_importStyles"));
		importStylesButton.addActionListener(this);
		//importStylesButton.setFont(font);
		importStylesButton.setPreferredSize(new Dimension(100,22));
		importStylesButton.setMaximumSize(new Dimension(100,22));
		boxh.add(importStylesButton);
		boxh.add(Box.createRigidArea(new Dimension(10,0)));
		exportStylesButton = new WiskOpdrButton(WiskOpdr.rb.getString("OPT_exportStyles"));
		exportStylesButton.addActionListener(this);
		//exportStylesButton.setFont(font);
		exportStylesButton.setPreferredSize(new Dimension(100,22));
		exportStylesButton.setMaximumSize(new Dimension(100,22));
		boxh.add(exportStylesButton);
		boxh.add(Box.createRigidArea(new Dimension(10,0)));
		boxh.add(hbStyles);
		boxv3.add(boxh);
		
		styleInteractionsLabel = new JLabel(WiskOpdr.rb.getString("OPT_styleInteractionsLabel"));
		styleInteractionsLabel.setFont(font);
		styleInteractionsLabel.setForeground(WiskOpdr.fgcolorEditor);
		
		styleInteractionsComboBox = new WiskOpdrComboBox();
		styleInteractionsComboBox.setFont(font);
		styleInteractionsComboBox.setPreferredSize(new Dimension(150,24));
		styleInteractionsComboBox.setForeground(WiskOpdr.fgcolorEditor);
		styleInteractionsComboBox.addActionListener(this);
		
		styleInteractionsComboBox.addItem(WiskOpdr.rb.getString("OPT_TemplateGeenLabel"));
		styleInteractionsComboBox.addItem(WiskOpdr.rb.getString("OPT_TemplateBasisLabel"));
		styleInteractionsComboBox.addItem(WiskOpdr.rb.getString("OPT_TemplateNumworxLabel"));
		styleInteractionsComboBox.addItem(WiskOpdr.rb.getString("OPT_TemplateToetsUULabel"));
		
		if(WiskOpdr.isExperimental()) {
			boxv3.add(Box.createVerticalStrut(5));
			boxh = Box.createHorizontalBox();
			boxh.add(styleInteractionsLabel);
			boxh.add(Box.createRigidArea(new Dimension(10,0)));
			boxh.add(styleInteractionsComboBox);
			boxh.add(Box.createHorizontalGlue());
			boxv3.add(boxh);
			boxv3.add(Box.createVerticalStrut(5));
		}
		boxh = Box.createHorizontalBox();
		layersCB = maakCheckBox(WiskOpdr.rb.getString("OPT_layers"), boxh, false);
		layersCB.addActionListener(this);
		boxh.add(Box.createRigidArea(new Dimension(10,0)));
		
		layersButton = new LayersButton();
		//layersButton.setFont(font);
		layersButton.setVisible(false);
		layersButton.setPreferredSize(new Dimension(100,22));
		layersButton.setMaximumSize(new Dimension(100,22));
		boxh.add(layersButton);
		boxh.add(Box.createHorizontalGlue());
		boxh.add(hbLayers);
		//boxh.setAlignmentY(Component.LEFT_ALIGNMENT);
		boxv3.add(boxh);
		boxv3.add(Box.createVerticalStrut(10));
		boxv3.add(Box.createVerticalGlue());
		//boxv3.add(Box.createVerticalStrut(70));
		
		//Nakijk-opties
		Box boxv4 = Box.createVerticalBox();
		nakijkenLabel = maakLabel(WiskOpdr.rb.getString("OPT_nakijkenLabel"), boxv4);
		scoresZichtbaarCB = maakCheckBoxHelp(hbScoresZichtbaar,WiskOpdr.rb.getString("OPT_scoreZichtbaar"), boxv4, true);//"formules in Times Roman"
		opnieuwCB = maakCheckBoxHelp(hbOpnieuw,WiskOpdr.rb.getString("OPT_opnieuwKnop"),boxv4, false);//"'Opnieuw' mogelijk"
		itemOpnieuwCB = maakCheckBoxHelp(hbItemOpnieuw,WiskOpdr.rb.getString("OPT_itemOpnieuwKnop"),boxv4, false);//"'Opnieuw' mogelijk"
		checkPerOpdrachtCB = maakCheckBox(WiskOpdr.rb.getString("OPT_checkPerOpdracht"), boxv4, false);//"Check-knop per opdracht"
		checkPerOpdrachtCB.setVisible(false);
		zelftoetsGeenCorrCB = maakCheckBoxHelp(hbZelftoetsGeenCorr,WiskOpdr.rb.getString("OPT_zelftoetsGeenCorr"), boxv4, false);//"F-toetsen gebruiken of niet"
		
		boxh = Box.createHorizontalBox();
		zelftoetsGeschiedenisCB = maakCheckBox(WiskOpdr.rb.getString("OPT_zelftoetsGeschiedenis"), boxh, false);//zelftoets geschiedenis tonen of niet"
		zelftoetsGeschiedenisCB.addActionListener(this);
		boxh.add(Box.createRigidArea(new Dimension(10,0)));
		zelftoetsHighScoreCB = maakCheckBox(WiskOpdr.rb.getString("OPT_zelftoetsHighScore"), boxh, false);
		zelftoetsHighScoreCB.setVisible(false);
		boxh.add(Box.createRigidArea(new Dimension(10,0)));
		boxh.add(hbZelftoetsGeschiedenis);
		boxv4.add(boxh);
		
		boxh = Box.createHorizontalBox();
		aftrekCorrectieZelftoetsCB = maakCheckBox(WiskOpdr.rb.getString("OPT_zelftoetsCorrAftrek"), boxh, true);
		aftrekCorrectieZelftoetsCB.addActionListener(this);
		boxh.add(Box.createRigidArea(new Dimension(10,0)));
		aftrekCorrectieZelftoetsTF = new WiskOpdrTextField(""+aftrekCorrectieZelftoets);
		aftrekCorrectieZelftoetsTF.setPreferredSize(new Dimension(50,22));
		aftrekCorrectieZelftoetsTF.setMaximumSize(new Dimension(50,22));
		aftrekCorrectieZelftoetsTF.setForeground(WiskOpdr.fgcolorEditor);
		boxh.add(aftrekCorrectieZelftoetsTF);
		boxh.add(Box.createRigidArea(new Dimension(10,0)));
		boxh.add(hbAftrekCorrZelftoets);
		
		boxv4.add(boxh);
		
		eerderGeenCorrCB = maakCheckBoxHelp(hEerderGeenCorr,WiskOpdr.rb.getString("OPT_eerderGeenCorr"), boxv4, false);
		
		boxh = Box.createHorizontalBox();
		timerCB = new WiskOpdrCheckbox(WiskOpdr.rb.getString("OPT_tempoToets"));
		timerCB.addActionListener(this);
		timerCB.setOpaque(false);
		timerCB.setFont(font);
		timerCB.setForeground(WiskOpdr.fgcolorEditor);
		timerCB.setSelected(false);
		boxh.add(timerCB);
		
		
		timerLabel = new JLabel(WiskOpdr.rb.getString("OPT_tijdsLimiet"));//"Tijdslimiet(sec)"
		timerLabel.setFont(font);
		timerLabel.setForeground(WiskOpdr.fgcolorEditor);
		timerLabel.setVisible(false);
		boxh.add(timerLabel);
		boxh.add(Box.createRigidArea(new Dimension(10,0)));
		
		timerTF = new WiskOpdrTextField("" + timeLimit);
		timerTF.setFont(font);
		timerTF.setForeground(WiskOpdr.fgcolorEditor);
		timerTF.setPreferredSize(new Dimension(50,24));
		timerTF.setVisible(false);
		boxh.add(timerTF);
		boxh.add(Box.createHorizontalGlue());
		boxh.add(Box.createRigidArea(new Dimension(10,0)));
		boxh.add(hbTimer);
		boxv4.add(boxh);
		
		boxh = Box.createHorizontalBox();
		boxh.setAlignmentY(Component.LEFT_ALIGNMENT);
		
		objectivesCB = new WiskOpdrCheckbox(WiskOpdr.rb.getString("OPT_objectives"));
		objectivesCB.addActionListener(this);
		objectivesCB.setOpaque(false);
		objectivesCB.setFont(font);
		objectivesCB.setForeground(WiskOpdr.fgcolorEditor);
		objectivesCB.setSelected(false);
		boxh.add(objectivesCB);
		boxh.setAlignmentY(Component.LEFT_ALIGNMENT);
		boxh.add(Box.createHorizontalGlue());
		
		pilotObjectivesCB = new WiskOpdrCheckbox(WiskOpdr.rb.getString("OPT_pilotObjectives"));
		pilotObjectivesCB.setVisible(false);
		pilotObjectivesCB.addActionListener(this);
		pilotObjectivesCB.setOpaque(false);
		pilotObjectivesCB.setFont(font);
		pilotObjectivesCB.setForeground(WiskOpdr.fgcolorEditor);
		pilotObjectivesCB.setSelected(false);
		boxh.add(pilotObjectivesCB);
		boxh.add(Box.createRigidArea(new Dimension(20,0)));
		
		objectivesButton = new ObjectiveSettingsButton();
		objectivesButton.setPreferredSize(new Dimension(100,24));
		objectivesButton.setMaximumSize(new Dimension(100,24));
		objectivesButton.setVisible(false);
		boxh.add(objectivesButton);
		boxh.add(Box.createRigidArea(new Dimension(10,0)));
		boxh.add(hbObjectives);
		
		boxv4.add(boxh);
		
		boxh = Box.createHorizontalBox();
		boxh.setAlignmentY(Component.LEFT_ALIGNMENT);
		
		misconceptionsCB = new WiskOpdrCheckbox(WiskOpdr.rb.getString("OPT_misconceptions"));
		misconceptionsCB.addActionListener(this);
		misconceptionsCB.setOpaque(false);
		misconceptionsCB.setFont(font);
		misconceptionsCB.setForeground(WiskOpdr.fgcolorEditor);
		misconceptionsCB.setSelected(false);
		boxh.add(misconceptionsCB);
		boxh.setAlignmentY(Component.LEFT_ALIGNMENT);
		boxh.add(Box.createHorizontalGlue());
		
		misconceptionsButton = new ObjectiveSettingsButton(WiskOpdr.rb.getString("OPT_misconceptions"), WiskOpdr.rb.getString("MCC_misconception"), WiskOpdr.rb.getString("MCC_categorie"));
		misconceptionsButton.setVisible(false);
		misconceptionsButton.setPreferredSize(new Dimension(140,24));
		misconceptionsButton.setMaximumSize(new Dimension(140,24));
		boxh.add(misconceptionsButton);
		boxh.add(Box.createHorizontalGlue());
		//boxv4.add(boxh);
		//boxv4.add(Box.createVerticalStrut(60));
		
		boxv4.createVerticalGlue();
		
		
		
		//MainPanel en BottomPanel in elkaar zetten
		mainPanel.add(boxv1);
		mainPanel.add(boxv3);
		mainPanel.add(boxv2);
		mainPanel.add(boxv4);
		okButton = new WiskOpdrButton("Ok");//
		okButton.setPreferredSize(new Dimension(70,24));
		//okButton.setFont(font);
		okButton.setBackground(WiskOpdr.colorBlue1);
		okButton.addActionListener(this);
		bottomPanel.add(okButton);
		
		bottomPanel.add(Box.createHorizontalStrut(10));
		cancelButton = new WiskOpdrButton("Cancel");//
		//cancelButton.setFont(font);
		cancelButton.setPreferredSize(new Dimension(70,24));
		cancelButton.setBackground(WiskOpdr.colorBlue1);
		cancelButton.addActionListener(this);
		bottomPanel.add(cancelButton);
		bottomPanel.setBackground(new Color(219,220,221));
		bottomPanel.setBorder(BorderFactory.createLineBorder(new Color(219,221,225), 5));
		
	}
	
	
	
	private JCheckBox maakCheckBox(String s, Container c, boolean selected)
	{	
		Box boxh = Box.createHorizontalBox();
		//boxh.setPreferredSize(new Dimension(300,24));
		JCheckBox checkbox = new WiskOpdrCheckbox(s);
		checkbox.setOpaque(false);
		checkbox.setFont(font);
		checkbox.setForeground(WiskOpdr.fgcolorEditor);
		checkbox.setSelected(selected);
		
		boxh.add(checkbox);
		boxh.add(Box.createHorizontalGlue());
		
		c.add(boxh);
		
		return checkbox;
	}
	
	private JCheckBox maakCheckBoxHelp(HelpButton hb,String s, Container c, boolean selected)
	{	
		Box boxh = Box.createHorizontalBox();
		//boxh.setPreferredSize(new Dimension(300,24));
		JCheckBox checkbox = new WiskOpdrCheckbox(s);
		checkbox.setOpaque(false);
		checkbox.setFont(font);
		checkbox.setForeground(WiskOpdr.fgcolorEditor);
		checkbox.setSelected(selected);
		
		boxh.add(checkbox);
		boxh.add(Box.createHorizontalGlue());
		boxh.add(Box.createRigidArea(new Dimension(10,0)));
		boxh.add(hb);
		c.add(boxh);
		
		return checkbox;
	}

	private JLabel maakLabel(String s, Container c)
	{
		Box boxh = Box.createHorizontalBox();
		JLabel label = new JLabel(s);
		label.setOpaque(true);
		label.setFont(boldFont);
		//label.setBackground(new Color(209,210,211));
		label.setForeground(WiskOpdr.fgcolorEditor);
		//label.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
		//label.setPreferredSize(new Dimension(400,35));
//		JPanel lijnPanel = new JPanel();
//        lijnPanel.setBackground(new Color(209,210,211));
//        lijnPanel.setLayout(null);
//        lijnPanel.setPreferredSize(new Dimension(300,35));
//        lijnPanel.add(label);
		boxh.add(label);
		boxh.add(Box.createHorizontalGlue());
		
		
//		c.add(boxh);
//		JPanel lijnPanel = new JPanel();
//		lijnPanel.setBackground(new Color(209,210,211));
//		lijnPanel.sboxhetMaximumSize(new Dimension(300,2));
//		lijnPanel.setPreferredSize(new Dimension(300,2));
//		lijnPanel.setBackground(new Color(209,210,211));
		c.add(boxh);
		c.add(Box.createVerticalStrut(10));
		
		return label;
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
	
	public Hashtable geefInstellingen()
	{	
		int fontSize = 12;
		boolean maalTeken = false;
		boolean woordFormule = false;
		boolean tweeHoofdletterVar = false;
		boolean timer = false;
		int timeLimit = 60;
		boolean opnieuw = false;
		boolean itemOpnieuw = false;
		boolean checkPerOpdracht = false;
		boolean hoekGraden = false;
		boolean bolletjesZichtbaar = true;
		boolean volgendeKnopZichtbaar = false;
		boolean vorigeKnopZichtbaar = false;
		boolean pagina = false;
		boolean formTimes = true;
		int navigatieSize = 12;
		String fontName = "SansSerif";
		int margeLinks = 10;
		int margeRechts = 10;
		int margeBoven = 10;
		int margeOnder = 10;
		int docWidth = 1024;
        int docHeight = 450;
		boolean fontOvererving = false;
		boolean fontOverervingForm = false;
		boolean fToets = true;
		boolean globalParam = false;
		boolean diffOperatoren = false;
		int keyboardNr = 0;
		int soortKeyboard = 0;
		int writeMathSetNr = 0;
		boolean voortgang = false;
		boolean condNav = false;
		boolean condNavPerc = true;
		boolean condNavVoorwaarden = false;
		int condPerc = 100;
		int[][][] navVoorwaarden = null;
		boolean allesCorrectNodig = false;
		boolean abcDeelOpdr = false;
		boolean zelftoetsGeenCorr = false;
		boolean zelftoetsGeschiedenis = false;
		boolean zelftoetsHighScore = false;
		boolean eerderGeenCorr = false;
		boolean significantie = false;
		boolean hasObjectives = false;
		String[][] objectives = null;
		String[] categorieString = null;
		boolean pilotObjectives = false;
		boolean hasMisconceptions = false;
		String[][] misconceptions = null;
		String[] mccCategorieString = null;
		boolean scoresZichtbaar = true;
		int aftrekCorrectieZelftoets = 5;
		boolean templateEdit = false;
		String studentModelId = null;
		boolean hasLayers = false;
		String[] layerNames = null;
		boolean[] layerVisible = null;
		boolean combinedComponents = false;
		int styleInteractionsNr = 0;
		
		try
		{	fontSize = Integer.parseInt(fontSizeTF.getText());
			//navigatieSize = Integer.parseInt(navigatieSizeTF.getText());
			timeLimit = Integer.parseInt(timerTF.getText());
			margeLinks = Integer.parseInt(margeLinksTF.getText());
			margeRechts = Integer.parseInt(margeLinksTF.getText());
			margeBoven = Integer.parseInt(margeBovenTF.getText());
			margeOnder = Integer.parseInt(margeBovenTF.getText());
			docWidth = Integer.parseInt(docWidthTF.getText());
			docHeight = Integer.parseInt(docHeightTF.getText());
			//condPerc = Integer.parseInt(condPercTF.getText());
		}
		catch(Exception e){}
		maalTeken = maalTekenCB.isSelected();
		woordFormule = woordFormuleCB.isSelected();
		tweeHoofdletterVar = tweeHLVarCB.isSelected();
		timer = timerCB.isSelected();
		opnieuw = opnieuwCB.isSelected();
		itemOpnieuw = itemOpnieuwCB.isSelected();
		checkPerOpdracht = checkPerOpdrachtCB.isSelected();
		hoekGraden = hoekGradenCB.isSelected();
		bolletjesZichtbaar = bolletjesCB.isSelected();
		volgendeKnopZichtbaar = volgendeKnopCB.isSelected();
		vorigeKnopZichtbaar = vorigeKnopCB.isSelected();
		//pagina = paginaCB.isSelected();
		formTimes = formTimesCB.isSelected();
		fontName = (String)fontNameCO.getSelectedItem();
		fontOvererving = fontOverervingCB.isSelected();
		fontOverervingForm = fontOverervingFormCB.isSelected();
		fToets = fToetsCB.isSelected();
		globalParam = globalParamCB.isSelected();
		diffOperatoren = diffOperatorenCB.isSelected();
		keyboardNr = keyboardCombobox.getSelectedIndex();
		soortKeyboard = keyboardVersionCombobox.getSelectedIndex();
		writeMathSetNr = writeMathCombobox.getSelectedIndex();
		voortgang = voortgangCB.isSelected();
		condNav = condNavCB.isSelected();
		if(condNav)
		{	condNavPerc = condNavPercentageRB.isSelected();
			condNavVoorwaarden = condNavVoorwaardenRB.isSelected();
			allesCorrectNodig = allesCorrectCB.isSelected();
			if(condNavPerc)
				try{condPerc = Integer.parseInt(condPercTF.getText());
				}
				catch(Exception e){}
			if(condNavVoorwaarden)
				navVoorwaarden = condButton.getNavVoorwaarden();
		}
		//abcDeelOpdr = abcDeelOpdrCB.isSelected();
		zelftoetsGeenCorr = zelftoetsGeenCorrCB.isSelected();
		zelftoetsGeschiedenis = zelftoetsGeschiedenisCB.isSelected();
		zelftoetsHighScore = zelftoetsHighScoreCB.isSelected();
		eerderGeenCorr = eerderGeenCorrCB.isSelected();
		significantie = significantieCB.isSelected();
		hasObjectives = objectivesCB.isSelected();
		hasMisconceptions = misconceptionsCB.isSelected();
		objectives = objectivesButton.getObjectives();
		studentModelId = objectivesButton.getStudentModelID();
		pilotObjectives = pilotObjectivesCB.isSelected();
		misconceptions = misconceptionsButton.getObjectives();
		categorieString = objectivesButton.getCategories();
		mccCategorieString = misconceptionsButton.getCategories();
		scoresZichtbaar = scoresZichtbaarCB.isSelected();
		templateEdit = templateEditCB.isSelected();
		hasLayers = layersCB.isSelected();
		if(hasLayers)
		{	layerNames = layersButton.getLayerNames();
			layerVisible = layersButton.getLayerVisible();
		}
		
		try{aftrekCorrectieZelftoets = Integer.parseInt(aftrekCorrectieZelftoetsTF.getText());
		}
		catch(Exception e){aftrekCorrectieZelftoets = 0;}
		
		combinedComponents = combinedComponentsCB.isSelected();
		styleInteractionsNr = styleInteractionsComboBox.getSelectedIndex();
		 
		
		Hashtable h = new Hashtable();
		
		h.put("fontSize", new Integer(fontSize));
		h.put("maalTeken", new Boolean(maalTeken));
		h.put("woordFormule", new Boolean(woordFormule));
		h.put("tweeHoofdletterVar", new Boolean(tweeHoofdletterVar));
		h.put("timer", new Boolean(timer));
		h.put("timeLimit", new Integer(timeLimit));
		h.put("opnieuw", new Boolean(opnieuw));
		h.put("itemOpnieuw", new Boolean(itemOpnieuw));
		h.put("checkPerOpdracht", new Boolean(checkPerOpdracht));
		h.put("hoekGraden", new Boolean(hoekGraden));
		h.put("bolletjesZichtbaar", new Boolean(bolletjesZichtbaar));
		h.put("volgendeKnopZichtbaar", new Boolean(volgendeKnopZichtbaar));
		h.put("vorigeKnopZichtbaar", new Boolean(vorigeKnopZichtbaar));
		h.put("pagina", new Boolean(pagina));
		h.put("formTimes", new Boolean(formTimes));
		h.put("navigatieSize", new Integer(navigatieSize));
		h.put("fontName", fontName);
		h.put("fontOvererving", new Boolean(fontOvererving));
		h.put("fontOverervingForm", new Boolean(fontOverervingForm));
		h.put("margeLinks", new Integer(margeLinks));
		h.put("margeRechts", new Integer(margeLinks));
		h.put("margeBoven", new Integer(margeBoven));
		h.put("margeOnder", new Integer(margeBoven));
		h.put("docWidth", new Integer(docWidth));
		h.put("docHeight", new Integer(docHeight));
		h.put("fToets", new Boolean(fToets));
		h.put("globalParam", new Boolean(globalParam));
		h.put("diffOperatoren", new Boolean(diffOperatoren));
		h.put("keyboardNr", new Integer(keyboardNr));
		h.put("soortKeyboard", new Integer(soortKeyboard));
		h.put("writeMathSetNr", new Integer(writeMathSetNr));
		h.put("voortgang", new Boolean(voortgang));
		h.put("condNav", new Boolean(condNav));
		if(condNav)
		{	h.put("condNavPerc", new Boolean(condNavPerc));
			h.put("condNavVoorwaarden", new Boolean(condNavVoorwaarden));
			h.put("allesCorrectNodig", new Boolean(allesCorrectNodig));
			if(condNavPerc)
				h.put("condPerc", new Integer(condPerc));
			if(condNavVoorwaarden && navVoorwaarden != null)
				h.put("navVoorwaarden", navVoorwaarden);
		}
		h.put("abcDeelOpdr", new Boolean(abcDeelOpdr));
		h.put("condPerc", new Integer(condPerc));
		h.put("zelftoetsGeenCorr", new Boolean(zelftoetsGeenCorr));
		h.put("zelftoetsGeschiedenis", new Boolean(zelftoetsGeschiedenis));
		h.put("zelftoetsHighScore", new Boolean(zelftoetsHighScore));
		h.put("eerderGeenCorr", new Boolean(eerderGeenCorr));
		h.put("significantie", new Boolean(significantie));
		h.put("hasObjectives", new Boolean(hasObjectives));
		if(hasObjectives && objectives!=null)
		{	h.put("objectives", objectives);
			h.put("categorieString", categorieString);
			h.put("pilotObjectives", new Boolean(pilotObjectives));
		}
		if(hasObjectives && studentModelId!=null)
		{	h.put("studentModelId", studentModelId);
		}
		h.put("hasMisconceptions", new Boolean(hasMisconceptions));
		if(hasMisconceptions && misconceptions!=null)
		{	h.put("misconceptions", misconceptions);
			h.put("mccCategorieString", mccCategorieString);
		}
		h.put("hasLayers", new Boolean(hasLayers));
		if(hasLayers && layerNames!=null && layerVisible!=null)
		{	h.put("layerNames", layerNames);
			h.put("layerVisible", layerVisible);
		}
		h.put("scoresZichtbaar", new Boolean(scoresZichtbaar));
		h.put("templateEdit", new Boolean(templateEdit));
		h.put("aftrekCorrectieZelftoets", new Integer(aftrekCorrectieZelftoets));
		h.put("combinedComponents", new Boolean(combinedComponents));
		h.put("styleInteractionsNr", new Integer(styleInteractionsNr));
		
		return h;
	}
	
	public void zetInstellingen(Hashtable h)
	{
		if(h==null) return;
		
		int fontSize = 12;
		boolean maalTeken = false;
		boolean woordFormule = false;
		boolean tweeHoofdletterVar = false;
		boolean timer = false;
		int timeLimit = 60;
		boolean opnieuw = false;
		boolean itemOpnieuw = false;
		boolean checkPerOpdracht = false;
		boolean hoekGraden = false;
		boolean bolletjesZichtbaar = true;
		boolean volgendeKnopZichtbaar = false;
		boolean vorigeKnopZichtbaar = false;
		boolean pagina = false;
		boolean formTimes = true;
		int navigatieSize = 12;
		String fontName = "SansSerif";
		int margeLinks = 10;
		int margeRechts = 10;
		int margeBoven = 10;
		int margeOnder = 10;
		int docWidth = 1024;
		int docHeight = 450;
		boolean fToets = true;
		boolean fontOvererving = false;
		boolean fontOverervingForm = false;
		boolean globalParam = false;
		boolean diffOperatoren = false;
		int keyboardNr = 0;
		int soortKeyboard = 0;
		int writeMathSetNr = 0;
		boolean voortgang = false;
		boolean condNav = false;
		boolean condNavPerc = true;
		boolean condNavVoorwaarden = false;
		int[][][] navVoorwaarden = null;
		boolean allesCorrectNodig = false;
		int condPerc = 100;
		boolean abcDeelOpdr = false;
		boolean zelftoetsGeenCorr = false;
		boolean zelftoetsGeschiedenis = false;
		boolean zelftoetsHighScore = false;
		boolean eerderGeenCorr = false;
		boolean significantie = false;
		boolean hasObjectives = false;
		String[][] objectives = null;
		String[] categorieString = null;
		String studentModelId = null;
		boolean pilotObjectives = false;
		boolean hasMisconceptions = false;
		String[][] misconceptions = null;
		String[] mccCategorieString = null;
		boolean scoresZichtbaar = true;
		int aftrekCorrectieZelftoets = 5;
		boolean templateEdit = false;
		boolean hasLayers = false;
		String[] layerNames = null;
		boolean[] layerVisible = null;
		boolean combinedComponents = false;
		int styleInteractionsNr = 0;
		
		if(h.containsKey("fontSize")) fontSize = ((Integer)h.get("fontSize")).intValue();
		if(h.containsKey("maalTeken")) maalTeken = ((Boolean)h.get("maalTeken")).booleanValue();
		if(h.containsKey("woordFormule")) woordFormule = ((Boolean)h.get("woordFormule")).booleanValue();
		if(h.containsKey("tweeHoofdletterVar")) tweeHoofdletterVar = ((Boolean)h.get("tweeHoofdletterVar")).booleanValue();
		if(h.containsKey("timer")) timer = ((Boolean)h.get("timer")).booleanValue();
		if(h.containsKey("timeLimit")) timeLimit = ((Integer)h.get("timeLimit")).intValue();
		if(h.containsKey("opnieuw")) opnieuw = ((Boolean)h.get("opnieuw")).booleanValue();
		if(h.containsKey("itemOpnieuw")) itemOpnieuw = ((Boolean)h.get("itemOpnieuw")).booleanValue();
		if(h.containsKey("checkPerOpdracht")) checkPerOpdracht = ((Boolean)h.get("checkPerOpdracht")).booleanValue();
		if(h.containsKey("hoekGraden")) hoekGraden = ((Boolean)h.get("hoekGraden")).booleanValue();
		if(h.containsKey("bolletjesZichtbaar")) bolletjesZichtbaar = ((Boolean)h.get("bolletjesZichtbaar")).booleanValue();
		if(h.containsKey("volgendeKnopZichtbaar")) volgendeKnopZichtbaar = ((Boolean)h.get("volgendeKnopZichtbaar")).booleanValue();
		if(h.containsKey("vorigeKnopZichtbaar")) vorigeKnopZichtbaar = ((Boolean)h.get("vorigeKnopZichtbaar")).booleanValue();
		if(h.containsKey("pagina")) pagina = ((Boolean)h.get("pagina")).booleanValue();
		if(h.containsKey("formTimes")) formTimes = ((Boolean)h.get("formTimes")).booleanValue();
		if(h.containsKey("navigatieSize")) navigatieSize = ((Integer)h.get("navigatieSize")).intValue();
		if(h.containsKey("fontName")) fontName = (String)h.get("fontName");
		if(h.containsKey("fontOvererving")) fontOvererving = ((Boolean)h.get("fontOvererving")).booleanValue();
		if(h.containsKey("fontOverervingForm")) fontOverervingForm = ((Boolean)h.get("fontOverervingForm")).booleanValue();
		if(h.containsKey("margeLinks")) margeLinks = ((Integer)h.get("margeLinks")).intValue();
		if(h.containsKey("margeRechts")) margeRechts = ((Integer)h.get("margeRechts")).intValue();
		if(h.containsKey("margeBoven")) margeBoven = ((Integer)h.get("margeBoven")).intValue();
		if(h.containsKey("margeOnder")) margeOnder = ((Integer)h.get("margeOnder")).intValue();
		if(h.containsKey("docWidth")) docWidth = ((Integer)h.get("docWidth")).intValue();
		if(h.containsKey("docHeight")) docHeight = ((Integer)h.get("docHeight")).intValue();
		if(h.containsKey("fToets")) fToets = ((Boolean)h.get("fToets")).booleanValue();
		if(h.containsKey("globalParam")) globalParam = ((Boolean)h.get("globalParam")).booleanValue();
		if(h.containsKey("diffOperatoren")) diffOperatoren = ((Boolean)h.get("diffOperatoren")).booleanValue();
		if(h.containsKey("keyboardNr")) keyboardNr = ((Integer)h.get("keyboardNr")).intValue();
		if(h.containsKey("soortKeyboard")) soortKeyboard = ((Integer)h.get("soortKeyboard")).intValue();
		if(h.containsKey("writeMathSetNr")) writeMathSetNr = ((Integer)h.get("writeMathSetNr")).intValue();
		if(h.containsKey("voortgang")) voortgang = ((Boolean)h.get("voortgang")).booleanValue();
		if(h.containsKey("condNav")) condNav = ((Boolean)h.get("condNav")).booleanValue();
		if(h.containsKey("condPerc")) condPerc = ((Integer)h.get("condPerc")).intValue();
		if(h.containsKey("condNavPerc")) condNavPerc = ((Boolean)h.get("condNavPerc")).booleanValue();
		if(h.containsKey("condNavVoorwaarden")) condNavVoorwaarden = ((Boolean)h.get("condNavVoorwaarden")).booleanValue();
		if(h.containsKey("navVoorwaarden")) navVoorwaarden = (int[][][]) h.get("navVoorwaarden");
		if(h.containsKey("allesCorrectNodig")) allesCorrectNodig = ((Boolean)h.get("allesCorrectNodig")).booleanValue();
		if(h.containsKey("abcDeelOpdr")) abcDeelOpdr = ((Boolean)h.get("abcDeelOpdr")).booleanValue();
		if(h.containsKey("zelftoetsGeenCorr")) zelftoetsGeenCorr = ((Boolean)h.get("zelftoetsGeenCorr")).booleanValue();
		if(h.containsKey("zelftoetsGeschiedenis")) zelftoetsGeschiedenis = ((Boolean)h.get("zelftoetsGeschiedenis")).booleanValue();
		if(h.containsKey("zelftoetsHighScore")) zelftoetsHighScore = ((Boolean)h.get("zelftoetsHighScore")).booleanValue();
		if(h.containsKey("aftrekCorrectieZelftoets")) aftrekCorrectieZelftoets = ((Integer)h.get("aftrekCorrectieZelftoets")).intValue();
		if(h.containsKey("eerderGeenCorr")) eerderGeenCorr = ((Boolean)h.get("eerderGeenCorr")).booleanValue();
		if(h.containsKey("significantie")) significantie = ((Boolean)h.get("significantie")).booleanValue();
		if(h.containsKey("hasObjectives")) hasObjectives = ((Boolean)h.get("hasObjectives")).booleanValue();
		if(h.containsKey("objectives")) 
			try	{	
				objectives = (String[][]) h.get("objectives");
			} catch(Exception ex){
				
			}
		if(h.containsKey("categorieString")) categorieString = (String[])h.get("categorieString");
		if(h.containsKey("studentModelId"))  studentModelId = (String) h.get("studentModelId");
		if(h.containsKey("pilotObjectives")) pilotObjectives = ((Boolean)h.get("pilotObjectives")).booleanValue();
		if(h.containsKey("hasMisconceptions")) hasMisconceptions = ((Boolean)h.get("hasMisconceptions")).booleanValue();
		if(h.containsKey("misconceptions")) 
			try	{	
				misconceptions = (String[][]) h.get("misconceptions");
			} catch(Exception ex){
				
			}
		if(h.containsKey("mccCategorieString")) mccCategorieString = (String[])h.get("mccCategorieString");
		if(h.containsKey("scoresZichtbaar")) scoresZichtbaar = ((Boolean)h.get("scoresZichtbaar")).booleanValue();
		if(h.containsKey("templateEdit")) templateEdit = ((Boolean)h.get("templateEdit")).booleanValue();
		if(h.containsKey("hasLayers")) hasLayers = ((Boolean)h.get("hasLayers")).booleanValue();
		if(h.containsKey("layerNames")) layerNames = (String[])h.get("layerNames");
		if(h.containsKey("layerVisible")) layerVisible = (boolean[])h.get("layerVisible");
		if(h.containsKey("combinedComponents")) combinedComponents = ((Boolean)h.get("combinedComponents")).booleanValue();
		if(h.containsKey("styleInteractionsNr")) styleInteractionsNr = ((Integer)h.get("styleInteractionsNr")).intValue();
		
		fontSizeTF.setText(""+fontSize);
		//navigatieSizeTF.setText(""+navigatieSize);
		maalTekenCB.setSelected(maalTeken);
		woordFormuleCB.setSelected(woordFormule);
		tweeHLVarCB.setSelected(tweeHoofdletterVar);
		timerCB.setSelected(timer);
		timerTF.setText(""+timeLimit);
		timerLabel.setVisible(timerCB.isSelected());
		timerTF.setVisible(timerCB.isSelected());
		opnieuwCB.setSelected(opnieuw);
		itemOpnieuwCB.setSelected(itemOpnieuw);
		checkPerOpdrachtCB.setSelected(checkPerOpdracht);
		checkPerOpdrachtCB.setVisible(checkPerOpdracht);
		hoekGradenCB.setSelected(hoekGraden);
		bolletjesCB.setSelected(bolletjesZichtbaar);
		volgendeKnopCB.setSelected(volgendeKnopZichtbaar);
		vorigeKnopCB.setSelected(vorigeKnopZichtbaar);
		//paginaCB.setSelected(pagina);
		formTimesCB.setSelected(formTimes);
		fontNameCO.setSelectedItem(fontName);
		fontOverervingCB.setSelected(fontOvererving);
		fontOverervingFormCB.setSelected(fontOverervingForm);
		margeLinksTF.setText(""+margeLinks);
		margeRechtsTF.setText(""+margeRechts);
		margeBovenTF.setText(""+margeBoven);
		margeOnderTF.setText(""+margeOnder);
		docWidthTF.setText(""+docWidth);
		docHeightTF.setText(""+docHeight);
		fToetsCB.setSelected(fToets);
		globalParamCB.setSelected(globalParam);
		diffOperatorenCB.setSelected(diffOperatoren);
		if(keyboardNr<keyboardCombobox.getItemCount())
			keyboardCombobox.setSelectedIndex(keyboardNr);
		keyboardVersionCombobox.setSelectedIndex(soortKeyboard);
		writeMathCombobox.setSelectedIndex(writeMathSetNr);
		voortgangCB.setSelected(voortgang);
		condNavCB.setSelected(condNav);
		condNavPercentageRB.setVisible(condNav);
		condNavVoorwaardenRB.setVisible(condNav);
		condNavPercentageRB.setSelected(condNavPerc);
		condNavVoorwaardenRB.setSelected(condNavVoorwaarden);
		allesCorrectCB.setVisible(condNav);
		allesCorrectCB.setSelected(allesCorrectNodig);
		if(condNavPerc)
		{	condPercTF.setVisible(condNav);
		}
		if(condNavVoorwaarden)
		{	condButton.setVisible(condNav);
			condButton.zetNavVoorwaarden(navVoorwaarden);
		}
		//abcDeelOpdrCB.setSelected(abcDeelOpdr);
		condPercTF.setText(""+condPerc);
		
		zelftoetsGeenCorrCB.setSelected(zelftoetsGeenCorr);
		zelftoetsGeschiedenisCB.setSelected(zelftoetsGeschiedenis);
		zelftoetsHighScoreCB.setVisible(zelftoetsGeschiedenis);
		zelftoetsHighScoreCB.setSelected(zelftoetsHighScore);
		
		aftrekCorrectieZelftoetsTF.setText(""+aftrekCorrectieZelftoets);
		if(aftrekCorrectieZelftoets==0) {
			//aftrekCorrectieZelftoetsTF.setVisible(false);
			aftrekCorrectieZelftoetsCB.setSelected(false);
		}
		eerderGeenCorrCB.setSelected(eerderGeenCorr);
		significantieCB.setSelected(significantie);
		objectivesCB.setSelected(hasObjectives);
		objectivesButton.setVisible(hasObjectives);
		pilotObjectivesCB.setVisible(hasObjectives);
		if(hasObjectives)
		{	objectivesButton.setObjectives(objectives);
			objectivesButton.setCategories(categorieString);
			pilotObjectivesCB.setSelected(pilotObjectives);
			if(WiskOpdr.isExperimental() && WiskOpdr.isPremium())
			{
			  objectivesButton.setStudentModelID(studentModelId);
			  //WiskOpdr.setStudentModel(objectivesButton.getStudentModel()); // Deze regel kan weg als Graphtool/Geodefiner zijn geupdate.
			  WiskOpdr.studentModelSupplier = 
			      studentModelId != null ?
			      () -> objectivesButton.getStudentModel() : null;
			}
		}
		misconceptionsCB.setSelected(hasMisconceptions);
		misconceptionsButton.setVisible(hasMisconceptions);
		if(hasMisconceptions)
		{	misconceptionsButton.setObjectives(misconceptions);
			misconceptionsButton.setCategories(mccCategorieString);
		}
		scoresZichtbaarCB.setSelected(scoresZichtbaar);
		templateEditCB.setSelected(templateEdit);
		layersCB.setSelected(hasLayers);
		layersButton.setVisible(hasLayers);
		layersButton.zetLayerInfo(layerNames, layerVisible);
		combinedComponentsCB.setSelected(combinedComponents || ShareAction.getSharingIsUsed());
		styleInteractionsComboBox.setSelectedIndex(styleInteractionsNr);
	}
	
	public void cancel()
	{
		dialog.setVisible(false);
	}
	
	public void confirm()
	{
		FormuleTeken.zetMaalTeken(maalTekenCB.isSelected());
		FormuleParser.zetWoordFormule(woordFormuleCB.isSelected());
		FormuleParser.zetTweeHoofdletterVariabele(tweeHLVarCB.isSelected());
		try
		{	fontSize = Integer.parseInt(fontSizeTF.getText());
			navigatieSize = Integer.parseInt(navigatieSizeTF.getText());
			timeLimit = Integer.parseInt(timerTF.getText());
			margeLinks = Integer.parseInt(margeLinksTF.getText());
			margeRechts = Integer.parseInt(margeLinksTF.getText());
			margeBoven = Integer.parseInt(margeBovenTF.getText());
			margeOnder = Integer.parseInt(margeBovenTF.getText());
			docWidth = Integer.parseInt(docWidthTF.getText());
			docHeight = Integer.parseInt(docHeightTF.getText());
			condPerc = Integer.parseInt(condPercTF.getText());
		}
		catch(Exception e){}
		MyOpdrEditContainer.setDefaultDocSizes(margeLinks, margeBoven, docWidth, docHeight);
		fontName = (String)fontNameCO.getSelectedItem();
		WiskOpdr.zetFont(fontName,fontSize);
		WiskOpdr.setFormTimes(formTimesCB.isSelected());
		TekstVakPanel.zetFontOvererving(fontOverervingCB.isSelected());
		AntwoordFormuleVak.zetFontOverervingForm(fontOverervingFormCB.isSelected());
		SimpelAntwoordFormuleVak.zetFontOverervingForm(fontOverervingFormCB.isSelected());
		AntwoordVergelijkingVak.zetFontOverervingForm(fontOverervingFormCB.isSelected());
		SimpelAntwoordVergelijkingVak.zetFontOverervingForm(fontOverervingFormCB.isSelected());
		AntwoordTekstVak.zetFontOverervingForm(fontOverervingFormCB.isSelected());
		ShareAction.setSharingPossible(combinedComponentsCB.isSelected() || ShareAction.getSharingIsUsed());
		
		WiskOpdr.setTemplateConstants(templateNames[styleInteractionsComboBox.getSelectedIndex()]);
		TekstVakPanel.setTemplateName(templateNames[styleInteractionsComboBox.getSelectedIndex()]);
		
		// onderstaande bestemd om oude activiteiten naar het Numworx template te brengen. strings nu hard in de code. Liver inladen uit database
		if(WiskOpdr.isExperimental()) {
			String[] launchDataStringTemplates = {
					"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"
			};
			Object ob1 = StringCodeObject.decodeStringToObject(launchDataStringTemplates[styleInteractionsComboBox.getSelectedIndex()]);
			Hashtable launchData = (Hashtable) ob1;
			
			String instellingenString = (String) launchData.get("instellingen");
			Object ob = StringCodeObject.decodeStringToObject(instellingenString);
			Hashtable instellingen = (Hashtable) ob;
			
			Hashtable styles = null;
			Hashtable templatePages = null;
			Hashtable templateComponents = null;
			ArrayList<String> templatePagesKeys = null;
			ArrayList<String> templateComponentsKeys = null;
			
			if (instellingen != null && instellingen.containsKey("TekstVakPanelStyles"))
				styles = (Hashtable) instellingen.get("TekstVakPanelStyles");
			if (instellingen != null && instellingen.containsKey("TekstVakPanelTemplatePages"))
				templatePages = (Hashtable) instellingen.get("TekstVakPanelTemplatePages");
			if (instellingen != null && instellingen.containsKey("TekstVakPanelTemplateComponents"))
				templateComponents = (Hashtable) instellingen.get("TekstVakPanelTemplateComponents");
			if (instellingen != null && instellingen.containsKey("TekstVakPanelTemplatePagesKeys"))
				templatePagesKeys = (ArrayList<String>) instellingen.get("TekstVakPanelTemplatePagesKeys");
			if (instellingen != null && instellingen.containsKey("TekstVakPanelTemplateComponentsKeys"))
				templateComponentsKeys = (ArrayList<String>) instellingen.get("TekstVakPanelTemplateComponentsKeys");
			
			TekstVakPanel.styles = styles;
			TekstVakPanel.templatePages = templatePages;
			TekstVakPanel.templateComponents = templateComponents;
			TekstVakPanel.templatePagesKeys = templatePagesKeys;
			TekstVakPanel.templateComponentsKeys = templateComponentsKeys;
		}
		
		//opdrNavStruct.setTimer(timerCB.isSelected(), timeLimit);
		//opdrNavStruct.zetOpnieuwMogelijk(opnieuwCB.isSelected());
		//opdrNavStruct.zetItemOpnieuwMogelijk(itemOpnieuwCB.isSelected());
		//opdrNavStruct.zetCheckPerOpdracht(checkPerOpdrachtCB.isSelected());
		//opdrNavStruct.setNavigatieSize(navigatieSize);
		Expressie.zetHoekGraden(hoekGradenCB.isSelected());
		//opdrNavStruct.zetMarges(margeLinks,margeRechts,margeBoven,margeOnder);
		WiskOpdr.setFToets(fToetsCB.isSelected());
		//opdrNavStruct.setGlobalParam(globalParamCB.isSelected());
		//opdrNavStruct.setCondNav(condNavCB.isSelected(), condPerc);
//		opdrNavStruct.setAbcDeelOpdr(abcDeelOpdrCB.isSelected());
		//opdrNavStruct.setZelftoetsGeenCorr(zelftoetsGeenCorrCB.isSelected());
		FormuleParser.zetSignificantie(significantieCB.isSelected());
		
		AntwoordFormuleVakEditPanel.zetSignificantieAan(significantieCB.isSelected());
		AntwoordVergelijkingVakEditPanel.zetSignificantieAan(significantieCB.isSelected());
		if(objectivesCB.isSelected())
		{	WiskOpdr.setObjectives(objectivesButton.getObjectives());
			WiskOpdr.setCategories(objectivesButton.getCategories());
			WiskOpdr.setStudentModel(objectivesButton.getStudentModel());
		}
		if(misconceptionsCB.isSelected())
		{	WiskOpdr.setMisconceptions(misconceptionsButton.getObjectives());
			WiskOpdr.setMccCategories(misconceptionsButton.getCategories());
		}
		if(layersCB.isSelected())
		{	TekstVakPanel.layerNames = layersButton.getLayerNames();
			TekstVakPanel.layerVisible = layersButton.getLayerVisible();
		}
		//opdrNavStruct.zetScoresZichtbaar(scoresZichtbaarCB.isSelected());
		TekstVakPanel.setTemplateEditor(templateEditCB.isSelected());
		
		dialog.setVisible(false);
	}
	
	
	
	public void actionPerformed(ActionEvent e)
	{	
		if(e.getSource() instanceof HelpButton)
		{
			OpdrNavStructEdit.helpBrowser.loadURL(((HelpButton)e.getSource()).getURL());
		}
		else if(e.getSource().equals(okButton))
		{	confirm();
			WiskOpdr.setLaunchDataChanged();
			produceAction("confirmed");
		}
		if(e.getSource().equals(cancelButton))
		{	cancel();
			
		}
		if(e.getSource()==timerCB)
		{
			timerLabel.setVisible(timerCB.isSelected());
			timerTF.setVisible(timerCB.isSelected());
		}
		if(e.getSource()==condNavCB)
		{
			condNavPercentageRB.setVisible(condNavCB.isSelected());
			condPercTF.setVisible(condNavPercentageRB.isSelected() && condNavCB.isSelected());
			condNavVoorwaardenRB.setVisible(condNavCB.isSelected());
			condButton.setVisible(condNavCB.isSelected() && condNavVoorwaardenRB.isSelected());
			allesCorrectCB.setVisible(condNavCB.isSelected());
			
		}
		if(e.getSource() == condNavPercentageRB)
		{	condPercTF.setVisible(condNavPercentageRB.isSelected());
			condButton.setVisible(condNavVoorwaardenRB.isSelected());
		}
			
		if(e.getSource()==condNavVoorwaardenRB)
		{	condPercTF.setVisible(condNavPercentageRB.isSelected());
			condButton.setVisible(condNavVoorwaardenRB.isSelected());
		}
		if(e.getSource()==objectivesCB)
		{
			objectivesButton.setVisible(objectivesCB.isSelected());
			pilotObjectivesCB.setVisible(objectivesCB.isSelected());
		}
		if(e.getSource()==misconceptionsCB)
		{
			misconceptionsButton.setVisible(misconceptionsCB.isSelected());
		}
		if(e.getSource()==aftrekCorrectieZelftoetsCB)
		{
			boolean b = aftrekCorrectieZelftoetsCB.isSelected();
			//aftrekCorrectieZelftoetsTF.setVisible(b);
			if(!b) {
				aftrekCorrectieZelftoets = 0;
				aftrekCorrectieZelftoetsTF.setText(""+aftrekCorrectieZelftoets);
			}
			
		}
		if(e.getSource()==zelftoetsGeschiedenisCB)
		{
			boolean b = zelftoetsGeschiedenisCB.isSelected();
			zelftoetsHighScoreCB.setVisible(b);
			if(!b) {
				zelftoetsHighScoreCB.setSelected(false);
			}
		}
		if(e.getSource().equals(stylesCB))
		{	boolean manageStyles = stylesCB.isSelected();
			if(TekstVakPanel.styles!=null) System.out.println(TekstVakPanel.styles.toString());
			if(manageStyles){
				TekstVakPanel.styles = new Hashtable();
				
			}
			else {
				int b = JOptionPane.showConfirmDialog(null,WiskOpdr.rb.getString("OPT_stylesWarnDialog"), "", JOptionPane.YES_NO_OPTION);
				System.out.println("confirm "+b);
				if(b==0)
					TekstVakPanel.styles = null;
				else
					stylesCB.setSelected(true);
			}
		}
		if(e.getSource().equals(exportStylesButton))
		{
			String contents = "";
			if(TekstVakPanel.styles != null)
				for (String key : TekstVakPanel.styles.keySet()) 
				{	
					contents =contents+"."+key+" {\n";
					
					Map<String,Object> map = TekstVakPanel.styles.get(key);
					for (String innerkey : map.keySet()) 
					{
						Object o = map.get(innerkey);
						contents =contents+"\t"+innerkey+":"+o.toString()+";\n";
					}
					contents =contents+"}\n";
				}
			stylesExportTekstArea.setText(contents);
			stylesExportDialog.setVisible(true);
		}
		
		if(e.getSource().equals(importStylesButton))
		{	
			stylesImportDialog.setVisible(true);
		}
		
		if(e.getSource().equals(importStylesNowButton))
		{	
			if(TekstVakPanel.styles==null)
				TekstVakPanel.styles = new Hashtable<String,Map<String,Object>>();
			
			String contents = " "+stylesImportTekstArea.getText();
			contents = StringUtils.replaceStr(contents, "\n", "");
			contents = StringUtils.replaceStr(contents, "\t", "");
			contents = contents.trim();
			String[] styleStrings = StringUtils.split(contents, "}");
			String[] styleKeys = new String[styleStrings.length-1];
			String[] styleValues = new String[styleStrings.length-1];
			for(int i=0 ; i<styleStrings.length-1 ; i++)
			{
				Hashtable style = new Hashtable();
				String[] styleKeyValue = StringUtils.split(styleStrings[i], "{");
				styleKeys[i] = styleKeyValue[0].substring(1).trim();
				styleValues[i] = styleKeyValue[1].trim();
				
				String[] styleElements = StringUtils.split(styleValues[i], ";");
				String[] styleElementKeys = new String[styleElements.length-1];
				String[] styleElementValues = new String[styleElements.length-1];
				for(int j=0 ; j<styleElements.length-1 ; j++)
				{
					String[] styleElementKeyValue = StringUtils.split(styleElements[j],":");
					styleElementKeys[j] = styleElementKeyValue[0].trim();
					styleElementValues[j] = styleElementKeyValue[1].trim();
					Object valueObject = styleElementValueStringToObject(styleElementValues[j]);
					style.put(styleElementKeys[j],valueObject);
				}
				
				TekstVakPanel.styles.put(styleKeys[i],style);
			}
		}
		
		if(e.getSource().equals(layersCB))
		{
			layersButton.setVisible(layersCB.isSelected());
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
    			dialog.pack();
    		}
    		else {
    			helpBox.validate(); 
    			OpdrNavStructEdit.helpBrowser.loadURL(null);
    			showHelpButtons(false);
    			//pack();
    			dialog.pack();
    			
    		}	
			//}
            
        }
		if(e.getSource() == hideHelpButton) {
			helpBox.setVisible(false);
			helpTitelBox.setVisible(false);
			OpdrNavStructEdit.helpBrowser.loadURL(null);
			showHelpButtons(false);
			dialog.pack();
		}
		
	}
	//Eenvoudige parser voor styles van Strings naar Objects
	private Object styleElementValueStringToObject(String s)
	{
		if(s.equals("true"))
			return new Boolean(true);
		else if(s.equals("false"))
			return new Boolean(false);
		else if(s.startsWith("java.awt.Color"))
		{
			s = s.substring(15,s.length()-1);
			String[] colorKeyValues = StringUtils.split(s, ",");
			String redString = colorKeyValues[0].substring(colorKeyValues[0].indexOf('=')+1).trim();
			String greenString = colorKeyValues[1].substring(colorKeyValues[1].indexOf('=')+1).trim();
			String blueString = colorKeyValues[2].substring(colorKeyValues[2].indexOf('=')+1).trim();
			System.out.println(redString);
			System.out.println(greenString);
			System.out.println(blueString);
			try{
				int red = Integer.parseInt(redString);
				int green = Integer.parseInt(greenString);
				int blue = Integer.parseInt(blueString);
				return new Color(red, green, blue);
			} catch(Exception e){}
		}
		else if(s.startsWith("java.awt.Font"))
		{
			s = s.substring(14,s.length()-1);
			String[] fontKeyValues = StringUtils.split(s, ",");
			String nameString = fontKeyValues[0].substring(fontKeyValues[0].indexOf('=')+1).trim();
			String styleString = fontKeyValues[2].substring(fontKeyValues[2].indexOf('=')+1).trim();
			String sizeString = fontKeyValues[3].substring(fontKeyValues[3].indexOf('=')+1).trim();
			
			System.out.println(nameString);
			System.out.println(styleString);
			System.out.println(sizeString);
			
			int size = 14;
			try{
				size = Integer.parseInt(sizeString);
			} catch(Exception e){}
			
			if(styleString.equals("plain"))
				return new Font(nameString,Font.PLAIN,size);
			if(styleString.equals("bold"))
				return new Font(nameString,Font.BOLD,size);
			if(styleString.equals("italic"))
				return new Font(nameString,Font.ITALIC,size);
			if(styleString.equals("bolditalic"))
				return new Font(nameString,Font.BOLD+Font.ITALIC,size);
		}
		else try{
			int waarde = Integer.parseInt(s);
			return new Integer(waarde);
		} catch(Exception e){}
		return s;
		
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
	
 	public void showHelpButtons(boolean b) {
 		hbMaalTeken.setVisible(b);
 		hbWoordFormule.setVisible(b);
 		hbTweeHLVar.setVisible(b);
 		hbHoekGraden.setVisible(b);
 		hbSignifacantie.setVisible(b);
 		hbGlobaalParam.setVisible(b);
 		hbDiffOperatoren.setVisible(b);
 		hbBolletjes.setVisible(b);
 		hbVolgendeKnop.setVisible(b);
 		hbVorigeKnop.setVisible(b);
 		hbVoortgang.setVisible(b);
 		hbCondNav.setVisible(b);
 		hbCombinedComponents.setVisible(b);
 		hbFormTimes.setVisible(b);
 		hbFontOvererving.setVisible(b);
 		hbFontOverervingForm.setVisible(b);
 		hbTemplateEdit.setVisible(b);
 		hbStyles.setVisible(b);
 		hbLayers.setVisible(b);
 		hbScoresZichtbaar.setVisible(b);
 		hbOpnieuw.setVisible(b);
 		hbItemOpnieuw.setVisible(b);
 		hbZelftoetsGeenCorr.setVisible(b);
 		hbZelftoetsGeschiedenis.setVisible(b);
 		hbAftrekCorrZelftoets.setVisible(b);
 		hEerderGeenCorr.setVisible(b);
 		hbTimer.setVisible(b);
 		hbObjectives.setVisible(b);
 	}
	
 	public String geefHelpURL() {
 		return HELP_INST_URL;
 	}
	
}
