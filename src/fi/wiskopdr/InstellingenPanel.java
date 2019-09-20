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
import fi.wiskopdr.tekstobjects.ShareAction;
//import fi.wiskopdr.tekstobjects.VoorwaardelijkeLinkButton;
import fi.wiskopdr.expressies.*;


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
	
	private JLabel keyboardLabel;
	private JComboBox keyboardCombobox;
	private JLabel writeMathLabel;
	private JComboBox writeMathCombobox;
	
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
	
	public InstellingenPanel(DialogFacade dialog, OpdrNavStructEdit opdrNavStruct)
	{	
		setLayout(new BorderLayout());
		setBackground(WiskOpdr.bgcolorEditor);
		
		JPanel topPanel = new JPanel();
		topPanel.setOpaque(true);
		topPanel.setBackground(new Color(49,71,112));
		JLabel topLabel = new JLabel("Instellingen Activiteit");
		topLabel.setFont(new Font("SansSerif",Font.BOLD, 28));
		topLabel.setForeground(new Color(237,239,241));
		topPanel.add(topLabel);
		
		add(BorderLayout.NORTH,topPanel);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(2,2));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		mainPanel.setOpaque(false);
		JPanel bottomPanel = new JPanel();
		
		stylesExportTekstArea = new JTextArea();
		stylesExportTekstArea.setBounds(0, 0, 600, 400);
		
		stylesExportDialog = DialogFacade.newInstance(this, "styles", true);
		stylesExportDialog.setSize(stylesExportTekstArea.getSize());
		JScrollPane scrollpaneExport = new JScrollPane(stylesExportTekstArea);
		stylesExportDialog.getContentPane().add(scrollpaneExport, BorderLayout.CENTER);
		
		stylesImportTekstArea = new JTextArea();
		stylesImportTekstArea.setBounds(0, 0, 600, 400);
		
		stylesImportDialog = DialogFacade.newInstance(this, "styles", true);
		stylesImportDialog.setSize(stylesImportTekstArea.getSize());
		JScrollPane scrollpaneImport = new JScrollPane(stylesImportTekstArea);
		stylesImportDialog.getContentPane().add(scrollpaneImport, BorderLayout.CENTER);
		
		importStylesNowButton = new WiskOpdrButton(WiskOpdr.rb.getString("OPT_importStyles"));
		importStylesNowButton.addActionListener(this);
		importStylesNowButton.setFont(font);
		stylesImportDialog.getContentPane().add(importStylesNowButton, BorderLayout.SOUTH);
		
		
		//add(mainPanel);
		add(bottomPanel,BorderLayout.SOUTH);
		add(mainPanel);
		
		this.dialog = dialog;
		this.opdrNavStruct = opdrNavStruct;
		
		
		
		//Wiskunde-opties
		Box boxv1 = Box.createVerticalBox();
		//boxv1.add(Box.createHorizontalStrut(10));
		//boxv1.add(Box.createVerticalStrut(20));
		wiskundeLabel = maakLabel(WiskOpdr.rb.getString("OPT_wiskundeLabel"), boxv1);
		maalTekenCB = maakCheckBox(WiskOpdr.rb.getString("OPT_vermenigvTeken"), boxv1, false);//"Vermenigvuldigingsteken X"
		woordFormuleCB = maakCheckBox(WiskOpdr.rb.getString("OPT_woordformules"), boxv1, false);//"Woordformules"
		tweeHLVarCB = maakCheckBox(WiskOpdr.rb.getString("OPT_tweeHoofdletterVars"), boxv1, false);//"Twee-hoofdletter variabelen "
		hoekGradenCB = maakCheckBox(WiskOpdr.rb.getString("OPT_hoekInGraden"),boxv1, false);//"Hoekberekeningen in graden"
		fToetsCB = maakCheckBox(WiskOpdr.rb.getString("OPT_fToets"), boxv1, true);//"F-toetsen gebruiken of niet"
		significantieCB = maakCheckBox(WiskOpdr.rb.getString("OPT_significantie"), boxv1, false); //"Checkmogelijkheid significante getallen"
		globalParamCB = maakCheckBox(WiskOpdr.rb.getString("OPT_globalParam"), boxv1, false);//"Globale parameters"
		diffOperatorenCB = maakCheckBox(WiskOpdr.rb.getString("OPT_diffOperatoren"), boxv1, false);
		//diffOperatorenCB.setVisible(false);
		
		Box boxh = Box.createHorizontalBox();
		keyboardLabel = new JLabel(WiskOpdr.rb.getString("Tablet keyboard")+" ");
		keyboardLabel.setFont(font);
		keyboardLabel.setForeground(WiskOpdr.fgcolorEditor);
		boxh.add(keyboardLabel);
		boxh.add(Box.createHorizontalStrut(10));
		
		keyboardCombobox = new WiskOpdrComboBox();
		keyboardCombobox.setFont(font);
		
		keyboardCombobox.setForeground(WiskOpdr.fgcolorEditor);
		keyboardCombobox.addItem(WiskOpdr.rb.getString("Onderbouw-keyboard"));
		keyboardCombobox.addItem(WiskOpdr.rb.getString("Algebra-keyboard"));
		keyboardCombobox.addItem(WiskOpdr.rb.getString("Gonio-keyboard"));
		keyboardCombobox.addItem(WiskOpdr.rb.getString("Statistiek-keyboard"));
		keyboardCombobox.addItem(WiskOpdr.rb.getString("Meetkunde-keyboard"));
		boxh.add(keyboardCombobox);
		boxh.add(Box.createHorizontalStrut(80));
		boxv1.add(boxh);
		boxv1.add(Box.createVerticalStrut(5));
		
		boxh = Box.createHorizontalBox();
		writeMathLabel = new JLabel(WiskOpdr.rb.getString("Tablet handschriftset")+" ");
		writeMathLabel.setFont(font);
		writeMathLabel.setForeground(WiskOpdr.fgcolorEditor);
		boxh.add(writeMathLabel);
		boxh.add(Box.createHorizontalStrut(10));
		
		writeMathCombobox = new WiskOpdrComboBox();
		writeMathCombobox.setFont(font);
		writeMathCombobox.setForeground(WiskOpdr.fgcolorEditor);
		writeMathCombobox.addItem(WiskOpdr.rb.getString("Basis"));
		writeMathCombobox.addItem(WiskOpdr.rb.getString("Uitgebreid"));
		boxh.add(writeMathCombobox);
		boxh.add(Box.createHorizontalStrut(80));
		boxv1.add(boxh);
		boxv1.add(Box.createVerticalStrut(90));
		
		//Navigatie-opties
		Box boxv2 = Box.createVerticalBox();
		navigatieLabel = maakLabel(WiskOpdr.rb.getString("OPT_navigatieLabel"), boxv2);
		bolletjesCB = maakCheckBox(WiskOpdr.rb.getString("OPT_opdrachtBolletjes"), boxv2, true);//"Opdrachtbolletjes"
		volgendeKnopCB = maakCheckBox(WiskOpdr.rb.getString("OPT_volgendeKnop"), boxv2, false);//"volgende-knop zichtbaar"
		vorigeKnopCB = maakCheckBox(WiskOpdr.rb.getString("OPT_VorigeKnop"), boxv2, false);//"vorige-knop zichtbaar"
		voortgangCB = maakCheckBox(WiskOpdr.rb.getString("OPT_voortgangKnop"), boxv2, false);
		condNavCB = maakCheckBox(WiskOpdr.rb.getString("OPT_conditionalNav"), boxv2, false);
		condNavCB.addActionListener(this);
		combinedComponentsCB = maakCheckBox(WiskOpdr.rb.getString("OPT_combCompNav"), boxv2, false);
		
		
		//Box boxh;
		boxh = Box.createHorizontalBox();
		//condNavCB = new JCheckBox(WiskOpdr.rb.getString("OPT_conditionalNav"));
		//condNavCB.setOpaque(false);
		//condNavCB.setFont(font);
		//condNavCB.setSelected(false);
		//boxh.add(condNavCB);
		
		boxh.add(Box.createHorizontalStrut(20));
		
		condNavPercentageRB = new WiskOpdrRadioButton(WiskOpdr.rb.getString("OPT_conditionalPercLabel"));
		condNavPercentageRB.addActionListener(this);
		condNavPercentageRB.setFont(font);
		condNavPercentageRB.setForeground(WiskOpdr.fgcolorEditor);
		condNavPercentageRB.setOpaque(false);
		condNavPercentageRB.setVisible(false);
		condNavPercentageRB.setSelected(true);
		boxh.add(condNavPercentageRB);
		boxh.add(Box.createHorizontalStrut(10));
		
		condPercTF = new WiskOpdrTextField(""+condPerc);
		condPercTF.setFont(font);
		//condPercTF.setBorder(BorderFactory.createLineBorder(new Color(120,150,202)));
		condPercTF.setForeground(WiskOpdr.fgcolorEditor);
		condPercTF.setPreferredSize(new Dimension(50,24));
		condPercTF.setMaximumSize(new Dimension(50,24));
		condPercTF.setVisible(false);
		boxh.add(condPercTF);
		boxh.add(Box.createGlue());
		boxh.setAlignmentY(Component.LEFT_ALIGNMENT);
		boxv2.add(boxh);
		
		boxh = Box.createHorizontalBox();
		boxh.add(Box.createHorizontalStrut(20));
		
		condNavVoorwaardenRB = new WiskOpdrRadioButton(WiskOpdr.rb.getString("OPT_voorwaarden"));
		condNavVoorwaardenRB.addActionListener(this);
		condNavVoorwaardenRB.setOpaque(false);
		condNavVoorwaardenRB.setFont(font);
		condNavVoorwaardenRB.setForeground(WiskOpdr.fgcolorEditor);
		condNavVoorwaardenRB.setSelected(false);
		condNavVoorwaardenRB.setVisible(false);
		boxh.add(condNavVoorwaardenRB);
		
		boxh.add(Box.createHorizontalStrut(10));
		
		condButton = new VoorwaardelijkeNavigatieButton();
		//condButton.setFont(font);
		condButton.setPreferredSize(new Dimension(100,24));
		condButton.setMaximumSize(new Dimension(100,24));
		condButton.setVisible(false);
		boxh.add(condButton);
		boxh.add(Box.createGlue());
		boxh.setAlignmentY(Component.LEFT_ALIGNMENT);
		boxv2.add(boxh);
		
		ButtonGroup group = new ButtonGroup();
	    group.add(condNavPercentageRB);
	    group.add(condNavVoorwaardenRB);
		
	    boxh = Box.createHorizontalBox();
		boxh.add(Box.createHorizontalStrut(20));
		
		allesCorrectCB = new WiskOpdrCheckbox(WiskOpdr.rb.getString("OPT_allesCorrect"));
		allesCorrectCB.setOpaque(false);
		allesCorrectCB.setFont(font);
		allesCorrectCB.setForeground(WiskOpdr.fgcolorEditor);
		allesCorrectCB.setSelected(false);
		allesCorrectCB.setVisible(false);
		boxh.add(allesCorrectCB);
		boxh.add(Box.createHorizontalGlue());
		boxv2.add(boxh);
	    
	    boxv2.add(Box.createVerticalStrut(130));
	    
		
		
		//Layout-opties
		Box boxv3 = Box.createVerticalBox();
		layoutLabel = maakLabel(WiskOpdr.rb.getString("OPT_layoutLabel"), boxv3);
		
		boxh = Box.createHorizontalBox();
		
		pageLabel = new JLabel(WiskOpdr.rb.getString("OPT_pageLabel"));
		pageLabel.setFont(font);
		pageLabel.setForeground(WiskOpdr.fgcolorEditor);
        boxh.add(pageLabel);
        boxh.add(Box.createHorizontalStrut(10));
        
        docWidthLabel = new JLabel(WiskOpdr.rb.getString("OPT_docWidthLabel"));
        docWidthLabel.setFont(font);
        docWidthLabel.setForeground(WiskOpdr.fgcolorEditor);
        boxh.add(docWidthLabel);
        boxh.add(Box.createHorizontalStrut(10));
        
        docWidthTF = new WiskOpdrTextField(""+docWidth);
        docWidthTF.setFont(font);
        docWidthTF.setForeground(WiskOpdr.fgcolorEditor);
        boxh.add(docWidthTF);
        boxh.add(Box.createHorizontalStrut(10));
        
        docHeightLabel = new JLabel(WiskOpdr.rb.getString("OPT_docHeightLabel"));
        docHeightLabel.setFont(font);
        docHeightLabel.setForeground(WiskOpdr.fgcolorEditor);
        boxh.add(docHeightLabel);
        boxh.add(Box.createHorizontalStrut(10));
        
        docHeightTF = new WiskOpdrTextField(""+docHeight);
        docHeightTF.setFont(font);
        docHeightTF.setForeground(WiskOpdr.fgcolorEditor);
        boxh.add(docHeightTF);
        
        
        boxv3.add(boxh);
        boxv3.add(Box.createVerticalStrut(5));

		boxh = Box.createHorizontalBox();
		margesLabel = new JLabel(WiskOpdr.rb.getString("OPT_margesLabel"));
		margesLabel.setFont(font);
		margesLabel.setForeground(WiskOpdr.fgcolorEditor);
		boxh.add(margesLabel);
		boxh.add(Box.createHorizontalStrut(10));
		
		
		//boxv3.add(boxh);
		//boxv3.add(Box.createVerticalStrut(5));
		
		//boxh = Box.createHorizontalBox();
		margeLinksLabel = new JLabel(WiskOpdr.rb.getString("OPT_margeLinksLabel")+" ");
		margeLinksLabel.setFont(font);
		margeLinksLabel.setForeground(WiskOpdr.fgcolorEditor);
		boxh.add(margeLinksLabel);
		boxh.add(Box.createHorizontalStrut(10));
		
		margeLinksTF = new WiskOpdrTextField(""+margeLinks);
		margeLinksTF.setFont(font);
		margeLinksTF.setForeground(WiskOpdr.fgcolorEditor);
		//margeLinksTF.setSize(new Dimension(50,24));
		boxh.add(margeLinksTF);
		boxh.add(Box.createHorizontalStrut(10));
		
		margeRechtsLabel = new JLabel(WiskOpdr.rb.getString("OPT_margeRechtsLabel")+" ");
		margeRechtsLabel.setFont(font);
		margeRechtsLabel.setForeground(WiskOpdr.fgcolorEditor);
		//boxh.add(margeRechtsLabel);
		
		margeRechtsTF = new WiskOpdrTextField(""+margeRechts);
		margeRechtsTF.setFont(font);
		margeRechtsTF.setForeground(WiskOpdr.fgcolorEditor);
		//margeRechtsTF.setSize(new Dimension(50,24));
		//boxh.add(margeRechtsTF);
		//boxh.add(Box.createHorizontalStrut(10));
		
		margeBovenLabel = new JLabel(WiskOpdr.rb.getString("OPT_margeBovenLabel")+" ");
		margeBovenLabel.setFont(font);
		margeBovenLabel.setForeground(WiskOpdr.fgcolorEditor);
		boxh.add(margeBovenLabel);
		boxh.add(Box.createHorizontalStrut(10));
		 
		margeBovenTF = new WiskOpdrTextField(""+margeBoven);
		margeBovenTF.setFont(font);
		margeBovenTF.setForeground(WiskOpdr.fgcolorEditor);
		//margeBovenTF.setSize(new Dimension(50,24));
		boxh.add(margeBovenTF);
		
		
		margeOnderLabel = new JLabel(WiskOpdr.rb.getString("OPT_margeOnderLabel")+" ");
		margeOnderLabel.setFont(font);
		margeOnderLabel.setForeground(WiskOpdr.fgcolorEditor);
		//boxh.add(margeOnderLabel);
		
		margeOnderTF = new WiskOpdrTextField(""+margeOnder);
		margeOnderTF.setFont(font);
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
		boxh.add(Box.createHorizontalStrut(10));
		
		fontNameCO = new WiskOpdrComboBox();
		fontNameCO.setFont(font);
		fontNameCO.setForeground(WiskOpdr.fgcolorEditor);
		for(int i=0 ; i<fontNames.length ; i++)
		{	fontNameCO.addItem(fontNames[i]);
		}
		fontNameCO.setPreferredSize(new Dimension(100,24));
		boxh.add(fontNameCO);
		
		boxv3.add(boxh);
		boxh.add(Box.createHorizontalStrut(10));
		
		fontSizeLabel = new JLabel(WiskOpdr.rb.getString("OPT_fontFormaat"));
		fontSizeLabel.setFont(font);
		fontSizeLabel.setForeground(WiskOpdr.fgcolorEditor);
		boxh.add(fontSizeLabel);
		boxh.add(Box.createHorizontalStrut(10));
				
		fontSizeTF = new WiskOpdrTextField(""+fontSize);
		fontSizeTF.setFont(font);
		fontSizeTF.setForeground(WiskOpdr.fgcolorEditor);
		fontSizeTF.setSize(new Dimension(50,24));
		boxh.add(fontSizeTF);
		
		boxv3.add(boxh);
		boxv3.add(Box.createVerticalStrut(5));
		
		boxh = Box.createHorizontalBox();
		
		navigatieSizeLabel = new JLabel(WiskOpdr.rb.getString("OPT_navigatieFormaat"));
		navigatieSizeLabel.setFont(font);
		navigatieSizeLabel.setForeground(WiskOpdr.fgcolorEditor);
		navigatieSizeLabel.setForeground(WiskOpdr.fgcolorEditor);
		//boxh.add(navigatieSizeLabel);
		//boxh.add(Box.createHorizontalStrut(10));
		
		navigatieSizeTF = new WiskOpdrTextField(""+navigatieSize);
		navigatieSizeTF.setFont(font);
		navigatieSizeTF.setSize(new Dimension(50,24));
		//boxh.add(navigatieSizeTF);
		//boxh.add(Box.createGlue());
		//boxv3.add(boxh);
		
		formTimesCB = maakCheckBox(WiskOpdr.rb.getString("OPT_formTimes"), boxv3, true);//"formules in Times Roman"
		//paginaCB = maakCheckBox(WiskOpdr.rb.getString("OPT_paginaIpvOpdracht"), boxv3, false);//"pagina ipv opdracht"
		//abcDeelOpdrCB = maakCheckBox(WiskOpdr.rb.getString("OPT_deelOpdr"), boxv3, false);//"F-toetsen gebruiken of niet"
		fontOverervingCB = maakCheckBox(WiskOpdr.rb.getString("OPT_fontOvererving"), boxv3, false);//"Font-overerving tekstvakken"
		fontOverervingFormCB = maakCheckBox(WiskOpdr.rb.getString("OPT_fontOverervingForm"), boxv3, false);
		templateEditCB = maakCheckBox(WiskOpdr.rb.getString("OPT_templateEditor"), boxv3, false);
		
		boxh = Box.createHorizontalBox();
		boolean manageStyles = TekstVakPanel.styles!=null;
		stylesCB = maakCheckBox(WiskOpdr.rb.getString("OPT_styles"), boxh, manageStyles);
		stylesCB.addActionListener(this);
		boxh.add(Box.createHorizontalStrut(10));
		importStylesButton = new WiskOpdrButton(WiskOpdr.rb.getString("OPT_importStyles"));
		importStylesButton.addActionListener(this);
		//importStylesButton.setFont(font);
		importStylesButton.setPreferredSize(new Dimension(100,24));
		importStylesButton.setMaximumSize(new Dimension(100,24));
		boxh.add(importStylesButton);
		boxh.add(Box.createHorizontalStrut(10));
		exportStylesButton = new WiskOpdrButton(WiskOpdr.rb.getString("OPT_exportStyles"));
		exportStylesButton.addActionListener(this);
		//exportStylesButton.setFont(font);
		exportStylesButton.setPreferredSize(new Dimension(100,24));
		exportStylesButton.setMaximumSize(new Dimension(100,24));
		boxh.add(exportStylesButton);
		boxh.add(Box.createHorizontalStrut(10));
		boxv3.add(boxh);
		boxv3.add(Box.createVerticalStrut(10));
		
		
		boxh = Box.createHorizontalBox();
		layersCB = maakCheckBox(WiskOpdr.rb.getString("OPT_layers"), boxh, false);
		layersCB.addActionListener(this);
		boxh.add(Box.createHorizontalStrut(10));
		
		layersButton = new LayersButton();
		//layersButton.setFont(font);
		layersButton.setVisible(false);
		layersButton.setPreferredSize(new Dimension(100,24));
		layersButton.setMaximumSize(new Dimension(100,24));
		boxh.add(layersButton);
		boxh.add(Box.createGlue());
		boxh.setAlignmentY(Component.LEFT_ALIGNMENT);
		boxv3.add(boxh);
		
		boxv3.add(Box.createVerticalStrut(70));
		
		//Nakijk-opties
		Box boxv4 = Box.createVerticalBox();
		nakijkenLabel = maakLabel(WiskOpdr.rb.getString("OPT_nakijkenLabel"), boxv4);
		scoresZichtbaarCB = maakCheckBox(WiskOpdr.rb.getString("OPT_scoreZichtbaar"), boxv4, true);//"formules in Times Roman"
		opnieuwCB = maakCheckBox(WiskOpdr.rb.getString("OPT_opnieuwKnop"),boxv4, false);//"'Opnieuw' mogelijk"
		itemOpnieuwCB = maakCheckBox(WiskOpdr.rb.getString("OPT_itemOpnieuwKnop"),boxv4, false);//"'Opnieuw' mogelijk"
		checkPerOpdrachtCB = maakCheckBox(WiskOpdr.rb.getString("OPT_checkPerOpdracht"), boxv4, false);//"Check-knop per opdracht"
		checkPerOpdrachtCB.setVisible(false);
		zelftoetsGeenCorrCB = maakCheckBox(WiskOpdr.rb.getString("OPT_zelftoetsGeenCorr"), boxv4, false);//"F-toetsen gebruiken of niet"
		
		boxh = Box.createHorizontalBox();
		zelftoetsGeschiedenisCB = maakCheckBox(WiskOpdr.rb.getString("OPT_zelftoetsGeschiedenis"), boxh, false);//zelftoets geschiedenis tonen of niet"
		zelftoetsGeschiedenisCB.addActionListener(this);
		boxh.add(Box.createHorizontalStrut(10));
		zelftoetsHighScoreCB = maakCheckBox(WiskOpdr.rb.getString("OPT_zelftoetsHighScore"), boxh, false);
		zelftoetsHighScoreCB.setVisible(false);
		boxv4.add(boxh);
		
		boxh = Box.createHorizontalBox();
		aftrekCorrectieZelftoetsCB = maakCheckBox(WiskOpdr.rb.getString("OPT_zelftoetsCorrAftrek"), boxh, true);
		aftrekCorrectieZelftoetsCB.addActionListener(this);
		boxh.add(Box.createHorizontalStrut(10));
		aftrekCorrectieZelftoetsTF = new WiskOpdrTextField(""+aftrekCorrectieZelftoets);
		aftrekCorrectieZelftoetsTF.setPreferredSize(new Dimension(50,24));
		aftrekCorrectieZelftoetsTF.setMaximumSize(new Dimension(50,24));
		aftrekCorrectieZelftoetsTF.setForeground(WiskOpdr.fgcolorEditor);
		boxh.add(aftrekCorrectieZelftoetsTF);
		
		boxv4.add(boxh);
		
		eerderGeenCorrCB = maakCheckBox(WiskOpdr.rb.getString("OPT_eerderGeenCorr"), boxv4, false);
		
		boxh = Box.createHorizontalBox();
		timerCB = new WiskOpdrCheckbox(WiskOpdr.rb.getString("OPT_tempoToets"));
		timerCB.addActionListener(this);
		timerCB.setOpaque(false);
		timerCB.setFont(font);
		timerCB.setForeground(WiskOpdr.fgcolorEditor);
		timerCB.setSelected(false);
		boxh.add(timerCB);
		
		boxh.add(Box.createHorizontalStrut(10));
		
		timerLabel = new JLabel(WiskOpdr.rb.getString("OPT_tijdsLimiet"));//"Tijdslimiet(sec)"
		timerLabel.setFont(font);
		timerLabel.setForeground(WiskOpdr.fgcolorEditor);
		timerLabel.setVisible(false);
		boxh.add(timerLabel);
		boxh.add(Box.createHorizontalStrut(10));
		
		timerTF = new WiskOpdrTextField("" + timeLimit);
		timerTF.setFont(font);
		timerTF.setForeground(WiskOpdr.fgcolorEditor);
		timerTF.setPreferredSize(new Dimension(50,24));
		timerTF.setVisible(false);
		boxh.add(timerTF);
		boxh.add(Box.createGlue());
		
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
		boxh.add(Box.createGlue());
		
		pilotObjectivesCB = new WiskOpdrCheckbox(WiskOpdr.rb.getString("OPT_pilotObjectives"));
		pilotObjectivesCB.setVisible(false);
		pilotObjectivesCB.addActionListener(this);
		pilotObjectivesCB.setOpaque(false);
		pilotObjectivesCB.setFont(font);
		pilotObjectivesCB.setForeground(WiskOpdr.fgcolorEditor);
		pilotObjectivesCB.setSelected(false);
		boxh.add(pilotObjectivesCB);
		boxh.add(Box.createHorizontalStrut(20));
		
		objectivesButton = new ObjectiveSettingsButton();
		objectivesButton.setPreferredSize(new Dimension(100,24));
		objectivesButton.setMaximumSize(new Dimension(100,24));
		objectivesButton.setVisible(false);
		boxh.add(objectivesButton);
		boxh.add(Box.createHorizontalStrut(10));
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
		boxh.add(Box.createGlue());
		
		misconceptionsButton = new ObjectiveSettingsButton(WiskOpdr.rb.getString("OPT_misconceptions"), WiskOpdr.rb.getString("MCC_misconception"), WiskOpdr.rb.getString("MCC_categorie"));
		misconceptionsButton.setVisible(false);
		misconceptionsButton.setPreferredSize(new Dimension(140,24));
		misconceptionsButton.setMaximumSize(new Dimension(140,24));
		boxh.add(misconceptionsButton);
		boxh.add(Box.createHorizontalStrut(70));
		boxv4.add(boxh);
		boxv4.add(Box.createVerticalStrut(60));
		
		
		
		//MainPanel en BottomPanel in elkaar zetten
		mainPanel.add(boxv1);
		mainPanel.add(boxv3);
		mainPanel.add(boxv2);
		mainPanel.add(boxv4);
		okButton = new WiskOpdrButton("Ok");//
		//okButton.setFont(font);
		//okButton.setBackground(new Color(49,71,112));
		okButton.addActionListener(this);
		bottomPanel.add(okButton);
		
		cancelButton = new WiskOpdrButton("Cancel");//
		//cancelButton.setFont(font);
		//cancelButton.setBackground(new Color(49,71,112));
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
			if(studentModelId != null) {
				h.put("studentModelId", studentModelId);
			}
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
		keyboardCombobox.setSelectedIndex(keyboardNr);
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
		if(e.getSource().equals(okButton))
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
				int b = JOptionPane.showConfirmDialog(null,"Alle ingestelde styles gaan verloren. Akkoord?", "", JOptionPane.YES_NO_OPTION);
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
	
	
	
	
}
