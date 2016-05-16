package fi.wiskopdr;

import java.awt.AWTEventMulticaster;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import fi.beans.wiskopdrbeans.*;
import fi.wiskopdr.formuleobjects.*;
import fi.wiskopdr.opdrnav.*;
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
	private JCheckBox eerderGeenCorrCB;
	private JCheckBox significantieCB;
	private JCheckBox objectivesCB;
	private JCheckBox misconceptionsCB;
	private JCheckBox fontOverervingCB;
	private JCheckBox fontOverervingFormCB;
	private JCheckBox scoresZichtbaarCB;
	
	
	private JLabel wiskundeLabel, navigatieLabel, layoutLabel, nakijkenLabel;
	
	private JLabel fontLabel, fontSizeLabel;
	private JComboBox fontNameCO;
	private JTextField fontSizeTF;
	private String fontName = "SansSerif";
	private int fontSize = 12;
	
	private int condPerc = 100;
	private JTextField condPercTF;
	
	public VoorwaardelijkeNavigatieButton condButton;
	
	private JLabel margesLabel;
	private JLabel margeLinksLabel;
	private JLabel margeRechtsLabel;
	private JLabel margeOnderLabel;
	private JLabel margeBovenLabel;
	private JTextField margeLinksTF;
	private JTextField margeRechtsTF;
	private JTextField margeBovenTF;
	private JTextField margeOnderTF;
	private int margeLinks = 18;
	private int margeRechts = 15;
	private int margeBoven = "GR".equals(WiskOpdr.deployVariant)?10:15;
	private int margeOnder = 15;
	
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
	
	
	private Font font = new Font("SansSerif",Font.PLAIN,12);
	private Font boldFont = new Font("SansSerif", Font.BOLD, 14);
	private OpdrNavStructEdit opdrNavStruct;
	
	public InstellingenPanel(DialogFacade dialog, OpdrNavStructEdit opdrNavStruct)
	{	
		setLayout(new BorderLayout());
		setBackground(WiskOpdr.bgcolorEditor);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(2,2));
		mainPanel.setOpaque(false);
		JPanel bottomPanel = new JPanel();
		
		
		
		
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
		keyboardLabel = new JLabel("Tablet keyboard"+" ");
		keyboardLabel.setFont(font);
		boxh.add(keyboardLabel);
		boxh.add(Box.createHorizontalStrut(10));
		
		keyboardCombobox = new JComboBox();
		keyboardCombobox.setFont(font);
		keyboardCombobox.addItem("Onderbouw-keyboard");
		keyboardCombobox.addItem("Algebra-keyboard");
		keyboardCombobox.addItem("Gonio-keyboard");
		keyboardCombobox.addItem("Statistiek-keyboard");
		keyboardCombobox.addItem("Meetkunde-keyboard");
		boxh.add(keyboardCombobox);
		boxh.add(Box.createHorizontalStrut(80));
		boxv1.add(boxh);
		boxv1.add(Box.createVerticalStrut(5));
		
		boxh = Box.createHorizontalBox();
		writeMathLabel = new JLabel("Tablet handschriftset"+" ");
		writeMathLabel.setFont(font);
		boxh.add(writeMathLabel);
		boxh.add(Box.createHorizontalStrut(10));
		
		writeMathCombobox = new JComboBox();
		writeMathCombobox.setFont(font);
		writeMathCombobox.addItem("Basis");
		writeMathCombobox.addItem("Uitgebreid");
		boxh.add(writeMathCombobox);
		boxh.add(Box.createHorizontalStrut(80));
		boxv1.add(boxh);
		
		//Navigatie-opties
		Box boxv2 = Box.createVerticalBox();
		navigatieLabel = maakLabel(WiskOpdr.rb.getString("OPT_navigatieLabel"), boxv2);
		bolletjesCB = maakCheckBox(WiskOpdr.rb.getString("OPT_opdrachtBolletjes"), boxv2, true);//"Opdrachtbolletjes"
		volgendeKnopCB = maakCheckBox(WiskOpdr.rb.getString("OPT_volgendeKnop"), boxv2, false);//"volgende-knop zichtbaar"
		vorigeKnopCB = maakCheckBox(WiskOpdr.rb.getString("OPT_VorigeKnop"), boxv2, false);//"vorige-knop zichtbaar"
		voortgangCB = maakCheckBox(WiskOpdr.rb.getString("OPT_voortgangKnop"), boxv2, false);
		condNavCB = maakCheckBox(WiskOpdr.rb.getString("OPT_conditionalNav"), boxv2, false);
		condNavCB.addActionListener(this);
		
		
		//Box boxh;
		boxh = Box.createHorizontalBox();
		//condNavCB = new JCheckBox(WiskOpdr.rb.getString("OPT_conditionalNav"));
		//condNavCB.setOpaque(false);
		//condNavCB.setFont(font);
		//condNavCB.setSelected(false);
		//boxh.add(condNavCB);
		
		boxh.add(Box.createHorizontalStrut(20));
		
		condNavPercentageRB = new JRadioButton(WiskOpdr.rb.getString("OPT_conditionalPercLabel"));//"Tijdslimiet(sec)"
		condNavPercentageRB.addActionListener(this);
		condNavPercentageRB.setFont(font);
		condNavPercentageRB.setOpaque(false);
		condNavPercentageRB.setVisible(false);
		condNavPercentageRB.setSelected(true);
		boxh.add(condNavPercentageRB);
		boxh.add(Box.createHorizontalStrut(10));
		
		condPercTF = new JTextField(""+condPerc);
		condPercTF.setFont(font);
		condPercTF.setPreferredSize(new Dimension(50,24));
		condPercTF.setMaximumSize(new Dimension(50,24));
		condPercTF.setVisible(false);
		boxh.add(condPercTF);
		boxh.add(Box.createGlue());
		boxh.setAlignmentY(Component.LEFT_ALIGNMENT);
		boxv2.add(boxh);
		
		boxh = Box.createHorizontalBox();
		boxh.add(Box.createHorizontalStrut(20));
		
		condNavVoorwaardenRB = new JRadioButton(WiskOpdr.rb.getString("OPT_voorwaarden"));
		condNavVoorwaardenRB.addActionListener(this);
		condNavVoorwaardenRB.setOpaque(false);
		condNavVoorwaardenRB.setFont(font);
		condNavVoorwaardenRB.setSelected(false);
		condNavVoorwaardenRB.setVisible(false);
		boxh.add(condNavVoorwaardenRB);
		
		boxh.add(Box.createHorizontalStrut(10));
		
		condButton = new VoorwaardelijkeNavigatieButton();
		condButton.setFont(font);
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
		
		allesCorrectCB = new JCheckBox(WiskOpdr.rb.getString("OPT_allesCorrect"));
		allesCorrectCB.setOpaque(false);
		allesCorrectCB.setFont(font);
		allesCorrectCB.setSelected(false);
		allesCorrectCB.setVisible(false);
		boxh.add(allesCorrectCB);
		boxh.add(Box.createHorizontalGlue());
		boxv2.add(boxh);
	    
	    boxv2.add(Box.createVerticalStrut(70));
		
		
		//Layout-opties
		Box boxv3 = Box.createVerticalBox();
		layoutLabel = maakLabel(WiskOpdr.rb.getString("OPT_layoutLabel"), boxv3);
		boxh = Box.createHorizontalBox();
		
		margesLabel = new JLabel(WiskOpdr.rb.getString("OPT_margesLabel"));
		margesLabel.setFont(font);
		boxh.add(margesLabel);
		boxh.add(Box.createHorizontalGlue());
		
		boxv3.add(boxh);
		boxv3.add(Box.createVerticalStrut(5));
		
		boxh = Box.createHorizontalBox();
		margeLinksLabel = new JLabel(WiskOpdr.rb.getString("OPT_margeLinksLabel")+" ");
		margeLinksLabel.setFont(font);
		boxh.add(margeLinksLabel);
		
		margeLinksTF = new JTextField(""+margeLinks);
		margeLinksTF.setFont(font);
		//margeLinksTF.setSize(new Dimension(50,24));
		boxh.add(margeLinksTF);
		boxh.add(Box.createHorizontalStrut(10));
		
		margeRechtsLabel = new JLabel(WiskOpdr.rb.getString("OPT_margeRechtsLabel")+" ");
		margeRechtsLabel.setFont(font);
		boxh.add(margeRechtsLabel);
		
		margeRechtsTF = new JTextField(""+margeRechts);
		margeRechtsTF.setFont(font);
		//margeRechtsTF.setSize(new Dimension(50,24));
		boxh.add(margeRechtsTF);
		boxh.add(Box.createHorizontalStrut(10));
		
		margeBovenLabel = new JLabel(WiskOpdr.rb.getString("OPT_margeBovenLabel")+" ");
		margeBovenLabel.setFont(font);
		boxh.add(margeBovenLabel);
		 
		margeBovenTF = new JTextField(""+margeBoven);
		margeBovenTF.setFont(font);
		//margeBovenTF.setSize(new Dimension(50,24));
		boxh.add(margeBovenTF);
		boxh.add(Box.createHorizontalStrut(10));
		
		margeOnderLabel = new JLabel(WiskOpdr.rb.getString("OPT_margeOnderLabel")+" ");
		margeOnderLabel.setFont(font);
		boxh.add(margeOnderLabel);
		
		margeOnderTF = new JTextField(""+margeOnder);
		margeOnderTF.setFont(font);
		//margeOnderTF.setSize(new Dimension(50,24));
		boxh.add(margeOnderTF);
		boxh.add(Box.createHorizontalGlue());
		
		
		boxv3.add(boxh);
		boxv3.add(Box.createVerticalStrut(5));
		
		boxh = Box.createHorizontalBox();
		
		fontLabel = new JLabel(WiskOpdr.rb.getString("OPT_fontNaam"));//"Tijdslimiet(sec)"
		fontLabel.setFont(font);
		boxh.add(fontLabel);
		boxh.add(Box.createHorizontalStrut(10));
		
		fontNameCO = new JComboBox();
		fontNameCO.setFont(font);
		for(int i=0 ; i<fontNames.length ; i++)
		{	fontNameCO.addItem(fontNames[i]);
		}
		fontNameCO.setPreferredSize(new Dimension(100,24));
		boxh.add(fontNameCO);
		boxh.add(Box.createGlue());
		boxv3.add(boxh);
		boxh.add(Box.createHorizontalStrut(10));
		
		fontSizeLabel = new JLabel(WiskOpdr.rb.getString("OPT_fontFormaat"));//"Tijdslimiet(sec)"
		fontSizeLabel.setFont(font);
		boxh.add(fontSizeLabel);
		boxh.add(Box.createHorizontalStrut(10));
				
		fontSizeTF = new JTextField(""+fontSize);
		fontSizeTF.setFont(font);
		fontSizeTF.setSize(new Dimension(50,24));
		boxh.add(fontSizeTF);
		boxh.add(Box.createGlue());
		boxv3.add(boxh);
		boxv3.add(Box.createVerticalStrut(5));
		
		boxh = Box.createHorizontalBox();
		
		navigatieSizeLabel = new JLabel(WiskOpdr.rb.getString("OPT_navigatieFormaat"));//"Tijdslimiet(sec)"
		navigatieSizeLabel.setFont(font);
		boxh.add(navigatieSizeLabel);
		boxh.add(Box.createHorizontalStrut(10));
		
		navigatieSizeTF = new JTextField(""+navigatieSize);
		navigatieSizeTF.setFont(font);
		navigatieSizeTF.setSize(new Dimension(50,24));
		boxh.add(navigatieSizeTF);
		boxh.add(Box.createGlue());
		boxv3.add(boxh);
		
		formTimesCB = maakCheckBox(WiskOpdr.rb.getString("OPT_formTimes"), boxv3, true);//"formules in Times Roman"
		paginaCB = maakCheckBox(WiskOpdr.rb.getString("OPT_paginaIpvOpdracht"), boxv3, false);//"pagina ipv opdracht"
		abcDeelOpdrCB = maakCheckBox(WiskOpdr.rb.getString("OPT_deelOpdr"), boxv3, false);//"F-toetsen gebruiken of niet"
		fontOverervingCB = maakCheckBox(WiskOpdr.rb.getString("OPT_fontOvererving"), boxv3, false);//"Font-overerving tekstvakken"
		fontOverervingFormCB = maakCheckBox(WiskOpdr.rb.getString("OPT_fontOverervingForm"), boxv3, false);
		
		boxv3.add(Box.createVerticalStrut(70));
		
		//Nakijk-opties
		Box boxv4 = Box.createVerticalBox();
		nakijkenLabel = maakLabel(WiskOpdr.rb.getString("OPT_nakijkenLabel"), boxv4);
		scoresZichtbaarCB = maakCheckBox(WiskOpdr.rb.getString("OPT_scoreZichtbaar"), boxv4, true);//"formules in Times Roman"
		opnieuwCB = maakCheckBox(WiskOpdr.rb.getString("OPT_opnieuwKnop"),boxv4, false);//"'Opnieuw' mogelijk"
		itemOpnieuwCB = maakCheckBox(WiskOpdr.rb.getString("OPT_itemOpnieuwKnop"),boxv4, false);//"'Opnieuw' mogelijk"
		checkPerOpdrachtCB = maakCheckBox(WiskOpdr.rb.getString("OPT_checkPerOpdracht"), boxv4, false);//"Check-knop per opdracht"
		zelftoetsGeenCorrCB = maakCheckBox(WiskOpdr.rb.getString("OPT_zelftoetsGeenCorr"), boxv4, false);//"F-toetsen gebruiken of niet"
		eerderGeenCorrCB = maakCheckBox(WiskOpdr.rb.getString("OPT_eerderGeenCorr"), boxv4, false);
		
		boxh = Box.createHorizontalBox();
		timerCB = new JCheckBox(WiskOpdr.rb.getString("OPT_tempoToets"));
		timerCB.addActionListener(this);
		timerCB.setOpaque(false);
		timerCB.setFont(font);
		timerCB.setSelected(false);
		boxh.add(timerCB);
		
		boxh.add(Box.createHorizontalStrut(10));
		
		timerLabel = new JLabel(WiskOpdr.rb.getString("OPT_tijdsLimiet"));//"Tijdslimiet(sec)"
		timerLabel.setFont(font);
		timerLabel.setVisible(false);
		boxh.add(timerLabel);
		boxh.add(Box.createHorizontalStrut(10));
		
		timerTF = new JTextField(""+timeLimit);
		timerTF.setFont(font);
		timerTF.setPreferredSize(new Dimension(50,24));
		timerTF.setVisible(false);
		boxh.add(timerTF);
		boxh.add(Box.createGlue());
		
		boxv4.add(boxh);
		
		boxh = Box.createHorizontalBox();
		boxh.setAlignmentY(Component.LEFT_ALIGNMENT);
		
		objectivesCB = new JCheckBox(WiskOpdr.rb.getString("OPT_objectives"));
		objectivesCB.addActionListener(this);
		objectivesCB.setOpaque(false);
		objectivesCB.setFont(font);
		objectivesCB.setSelected(false);
		boxh.add(objectivesCB);
		boxh.setAlignmentY(Component.LEFT_ALIGNMENT);
		boxh.add(Box.createGlue());
		
		objectivesButton = new ObjectiveSettingsButton();
		objectivesButton.setVisible(false);
		boxh.add(objectivesButton);
		boxh.add(Box.createHorizontalStrut(70));
		boxv4.add(boxh);
		
		boxh = Box.createHorizontalBox();
		boxh.setAlignmentY(Component.LEFT_ALIGNMENT);
		
		misconceptionsCB = new JCheckBox(WiskOpdr.rb.getString("OPT_misconceptions"));
		misconceptionsCB.addActionListener(this);
		misconceptionsCB.setOpaque(false);
		misconceptionsCB.setFont(font);
		misconceptionsCB.setSelected(false);
		boxh.add(misconceptionsCB);
		boxh.setAlignmentY(Component.LEFT_ALIGNMENT);
		boxh.add(Box.createGlue());
		
		misconceptionsButton = new ObjectiveSettingsButton(WiskOpdr.rb.getString("OPT_misconceptions"), WiskOpdr.rb.getString("MCC_misconception"), WiskOpdr.rb.getString("MCC_categorie"));
		misconceptionsButton.setVisible(false);
		boxh.add(misconceptionsButton);
		boxh.add(Box.createHorizontalStrut(70));
		boxv4.add(boxh);
		boxv4.add(Box.createVerticalStrut(70));
		
		
		
		//MainPanel en BottomPanel in elkaar zetten
		mainPanel.add(boxv1);
		mainPanel.add(boxv3);
		mainPanel.add(boxv2);
		mainPanel.add(boxv4);
		okButton = new JButton("Ok");//
		okButton.setFont(font);
		okButton.addActionListener(this);
		bottomPanel.add(okButton);
		
		cancelButton = new JButton("Cancel");//
		cancelButton.setFont(font);
		cancelButton.addActionListener(this);
		bottomPanel.add(cancelButton);
		
	}
	
	private JCheckBox maakCheckBox(String s, Container c, boolean selected)
	{	
		Box boxh = Box.createHorizontalBox();
		//boxh.setPreferredSize(new Dimension(300,24));
		JCheckBox checkbox = new JCheckBox(s);
		checkbox.setOpaque(false);
		checkbox.setFont(font);
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
		label.setOpaque(false);
		label.setFont(boldFont);
		boxh.add(label);
		boxh.add(Box.createHorizontalGlue());
		
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
		int margeLinks = 18;
		int margeRechts = 15;
		int margeBoven = "GR".equals(WiskOpdr.deployVariant)?10:15;
		int margeOnder = 15;
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
		boolean eerderGeenCorr = false;
		boolean significantie = false;
		boolean hasObjectives = false;
		String[][] objectives = null;
		String[] categorieString = null;
		boolean hasMisconceptions = false;
		String[][] misconceptions = null;
		String[] mccCategorieString = null;
		boolean scoresZichtbaar = true;
		
		try
		{	fontSize = Integer.parseInt(fontSizeTF.getText());
			navigatieSize = Integer.parseInt(navigatieSizeTF.getText());
			timeLimit = Integer.parseInt(timerTF.getText());
			margeLinks = Integer.parseInt(margeLinksTF.getText());
			margeRechts = Integer.parseInt(margeRechtsTF.getText());
			margeBoven = Integer.parseInt(margeBovenTF.getText());
			margeOnder = Integer.parseInt(margeOnderTF.getText());
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
		pagina = paginaCB.isSelected();
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
		abcDeelOpdr = abcDeelOpdrCB.isSelected();
		zelftoetsGeenCorr = zelftoetsGeenCorrCB.isSelected();
		eerderGeenCorr = eerderGeenCorrCB.isSelected();
		significantie = significantieCB.isSelected();
		hasObjectives = objectivesCB.isSelected();
		hasMisconceptions = misconceptionsCB.isSelected();
		objectives = objectivesButton.getObjectives();
		misconceptions = misconceptionsButton.getObjectives();
		categorieString = objectivesButton.getCategories();
		mccCategorieString = misconceptionsButton.getCategories();
		scoresZichtbaar = scoresZichtbaarCB.isSelected();
		
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
		h.put("margeRechts", new Integer(margeRechts));
		h.put("margeBoven", new Integer(margeBoven));
		h.put("margeOnder", new Integer(margeOnder));
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
		h.put("eerderGeenCorr", new Boolean(eerderGeenCorr));
		h.put("significantie", new Boolean(significantie));
		h.put("hasObjectives", new Boolean(hasObjectives));
		if(hasObjectives && objectives!=null)
		{	h.put("objectives", objectives);
			h.put("categorieString", categorieString);
		}
		h.put("hasMisconceptions", new Boolean(hasMisconceptions));
		if(hasMisconceptions && misconceptions!=null)
		{	h.put("misconceptions", misconceptions);
			h.put("mccCategorieString", mccCategorieString);
		}
		h.put("scoresZichtbaar", new Boolean(scoresZichtbaar));
		
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
		int margeLinks = 18;
		int margeRechts = 15;
		int margeBoven = "GR".equals(WiskOpdr.deployVariant)?10:15;
		int margeOnder = 15;
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
		boolean eerderGeenCorr = false;
		boolean significantie = false;
		boolean hasObjectives = false;
		String[][] objectives = null;
		String[] categorieString = null;
		boolean hasMisconceptions = false;
		String[][] misconceptions = null;
		String[] mccCategorieString = null;
		boolean scoresZichtbaar = true;
		
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
		if(h.containsKey("eerderGeenCorr")) eerderGeenCorr = ((Boolean)h.get("eerderGeenCorr")).booleanValue();
		if(h.containsKey("significantie")) significantie = ((Boolean)h.get("significantie")).booleanValue();
		if(h.containsKey("hasObjectives")) hasObjectives = ((Boolean)h.get("hasObjectives")).booleanValue();
		if(h.containsKey("objectives")) 
			try	{	
				objectives = (String[][]) h.get("objectives");
			} catch(Exception ex){
				
			}
		if(h.containsKey("categorieString")) categorieString = (String[])h.get("categorieString");
		if(h.containsKey("hasMisconceptions")) hasMisconceptions = ((Boolean)h.get("hasMisconceptions")).booleanValue();
		if(h.containsKey("misconceptions")) 
			try	{	
				misconceptions = (String[][]) h.get("misconceptions");
			} catch(Exception ex){
				
			}
		if(h.containsKey("mccCategorieString")) mccCategorieString = (String[])h.get("mccCategorieString");
		if(h.containsKey("scoresZichtbaar")) scoresZichtbaar = ((Boolean)h.get("scoresZichtbaar")).booleanValue();
		
		fontSizeTF.setText(""+fontSize);
		navigatieSizeTF.setText(""+navigatieSize);
		maalTekenCB.setSelected(maalTeken);
		woordFormuleCB.setSelected(woordFormule);
		tweeHLVarCB.setSelected(tweeHoofdletterVar);
		timerCB.setSelected(timer);
		timerTF.setText(""+timeLimit);
		opnieuwCB.setSelected(opnieuw);
		itemOpnieuwCB.setSelected(itemOpnieuw);
		checkPerOpdrachtCB.setSelected(checkPerOpdracht);
		hoekGradenCB.setSelected(hoekGraden);
		bolletjesCB.setSelected(bolletjesZichtbaar);
		volgendeKnopCB.setSelected(volgendeKnopZichtbaar);
		vorigeKnopCB.setSelected(vorigeKnopZichtbaar);
		paginaCB.setSelected(pagina);
		formTimesCB.setSelected(formTimes);
		fontNameCO.setSelectedItem(fontName);
		fontOverervingCB.setSelected(fontOvererving);
		fontOverervingFormCB.setSelected(fontOverervingForm);
		margeLinksTF.setText(""+margeLinks);
		margeRechtsTF.setText(""+margeRechts);
		margeBovenTF.setText(""+margeBoven);
		margeOnderTF.setText(""+margeOnder);
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
		abcDeelOpdrCB.setSelected(abcDeelOpdr);
		condPercTF.setText(""+condPerc);
		zelftoetsGeenCorrCB.setSelected(zelftoetsGeenCorr);
		eerderGeenCorrCB.setSelected(eerderGeenCorr);
		significantieCB.setSelected(significantie);
		objectivesCB.setSelected(hasObjectives);
		objectivesButton.setVisible(hasObjectives);
		if(hasObjectives)
		{	objectivesButton.setObjectives(objectives);
			objectivesButton.setCategories(categorieString);
		}
		misconceptionsCB.setSelected(hasMisconceptions);
		misconceptionsButton.setVisible(hasMisconceptions);
		if(hasMisconceptions)
		{	misconceptionsButton.setObjectives(misconceptions);
			misconceptionsButton.setCategories(mccCategorieString);
		}
		scoresZichtbaarCB.setSelected(scoresZichtbaar);
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
			margeRechts = Integer.parseInt(margeRechtsTF.getText());
			margeBoven = Integer.parseInt(margeBovenTF.getText());
			margeOnder = Integer.parseInt(margeOnderTF.getText());
			condPerc = Integer.parseInt(condPercTF.getText());
		}
		catch(Exception e){}
		fontName = (String)fontNameCO.getSelectedItem();
		WiskOpdr.zetFont(fontName,fontSize);
		WiskOpdr.setFormTimes(formTimesCB.isSelected());
		TekstVakPanel.zetFontOvererving(fontOverervingCB.isSelected());
		AntwoordFormuleVak.zetFontOverervingForm(fontOverervingFormCB.isSelected());
		SimpelAntwoordFormuleVak.zetFontOverervingForm(fontOverervingFormCB.isSelected());
		AntwoordVergelijkingVak.zetFontOverervingForm(fontOverervingFormCB.isSelected());
		SimpelAntwoordVergelijkingVak.zetFontOverervingForm(fontOverervingFormCB.isSelected());
		AntwoordTekstVak.zetFontOverervingForm(fontOverervingFormCB.isSelected());
		
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
		opdrNavStruct.setAbcDeelOpdr(abcDeelOpdrCB.isSelected());
		//opdrNavStruct.setZelftoetsGeenCorr(zelftoetsGeenCorrCB.isSelected());
		FormuleParser.zetSignificantie(significantieCB.isSelected());
		AntwoordFormuleVakEditPanel.zetSignificantieAan(significantieCB.isSelected());
		AntwoordVergelijkingVakEditPanel.zetSignificantieAan(significantieCB.isSelected());
		if(objectivesCB.isSelected())
		{	WiskOpdr.setObjectives(objectivesButton.getObjectives());
			WiskOpdr.setCategories(objectivesButton.getCategories());
		}
		if(misconceptionsCB.isSelected())
		{	WiskOpdr.setMisconceptions(misconceptionsButton.getObjectives());
			WiskOpdr.setMccCategories(misconceptionsButton.getCategories());
		}
		//opdrNavStruct.zetScoresZichtbaar(scoresZichtbaarCB.isSelected());
		
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
		}
		if(e.getSource()==misconceptionsCB)
		{
			misconceptionsButton.setVisible(misconceptionsCB.isSelected());
		}


		
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
