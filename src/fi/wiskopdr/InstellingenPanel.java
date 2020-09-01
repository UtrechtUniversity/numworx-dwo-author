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
	private String[] templateNames = {"TemplateBasis","TemplateBasis", "TemplateNumworx"};
	
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
			  WiskOpdr.setStudentModel(objectivesButton.getStudentModel());
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
				"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",
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
