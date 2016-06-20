package fi.wiskopdr;

import java.awt.AWTEventMulticaster;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import fi.beans.iconan.Iconan;
import fi.beans.wiskopdrbeans.*;
import fi.wiskopdr.formuleobjects.*;
import fi.wiskopdr.tekstobjects.*;
import fi.wiskopdr.opdrnav.*;

public class TekstVakEditPanel extends JPanel implements InteractieEditPanel , ActionListener, FocusListener, TabletOwner
{
	private TekstEditor tekstEditor;
	private TekstVakPanel tekstVakPanel;
	
	private JCheckBox randZichtbaarCB, bgColorZichtbaarCB, zwevendCB, anderFontCB, buttonCB, tableBordersCB;
	private JCheckBox centerHCB, centerVCB;
	private JCheckBox pasAanHCB, pasAanBCB;
	//private JLabel varNaamLabel;
	
	private JLabel aantalRijenLabel;
	private JLabel aantalKolommenLabel;
	private JLabel cellMargeLabel;
	private JLabel bovenMargeLabel;
	private JLabel rondingLabel;
	private JLabel hoekLabel;
	private JLabel kopLayoutLabel;
	private JLabel kopFunctieLabel;
	private JLabel interlinieLabel;
	private JLabel cellSpaceColumnLabel;
	private JLabel cellSpaceRowLabel;
	
	private JTextField aantalRijenTF;
	private JTextField aantalKolommenTF;
	private PlusMinKnop rijenPlusMin;
	private PlusMinKnop kolommenPlusMin;
	private JTextField cellMargeTF;
	private JTextField bovenMargeTF;
	private JTextField rondingTF;
	private PlusMinKnop rondingPlusMin;
	private JTextField hoekTF;
	private JTextField interlinieTF;
	private JTextField cellSpaceColumnTF;
	private JTextField cellSpaceRowTF;
	private JTextField randDikteTF;
	
	private boolean randZichtbaar, bgColorZichtbaar, zwevend, anderFont, buttonOptie, tableBorders;
	
	private Tablet tablet;
	private boolean tabletAdded;
	private FormuleVakHouder tabletUser;
	
	private Font ifFont = new Font("SansSerif",Font.PLAIN,12);
	private Font titelFont = new Font("SansSerif", Font.BOLD, 14);
	
	private boolean tableMode = true;
	
	private int cellMarge; 
	private int bovenMarge; 
	private int cellSpaceColumn = 2; 
	private int cellSpaceRow = 2; 
	private int ronding; 
	private Font font;
	private int hoek;
	private boolean centerH;
	private boolean centerV;
	private boolean pasAanH;
	private boolean pasAanB;
	private int interlinie;
	private int randDikte=1;
		
	private JButton fontButton, randColorButton,  bgColorButton, fgColorButton, linkButton;
	private Color randColor = Color.gray;
	private Color bgColor = new Color(255,255,180);
	private Color fgColor = new Color(0,0,0);
	
	private JCheckBox selectableCB;
	private JCheckBox colorSelectionCB;
	private JCheckBox sleepbaarCB;
	private JCheckBox sleepdoelCB;
	private JCheckBox sleepHandleCB;
	private FormuleVak checkExpressieFormuleVak;
	private JLabel selectieWaardeLabel;
	private JTextField scoreMaxTF;
	private boolean selectable;
	private boolean colorSelection;
	private boolean sleepbaar;
	private boolean sleepdoel;
	private boolean sleepHandle;
	private String checkExpressieString;
	private JCheckBox linkCB;
	private boolean isLink;
	
	private JLabel interactiePanelIdLabel;
	private JTextField interactiePanelIdTF;
	private int ipId;
	
	private JPanel cp;
	
	private JCheckBox defaultBijNullCB;
	private boolean defaultBijNull;
	
	private JCheckBox zichtbaarNaNakijkenCB;
	private boolean zichtbaarNaNakijken;
	
	private JCheckBox balansVergComCB;
	private boolean balansVergCom;
	
	private JCheckBox aftrekPopupCB;
	private boolean aftrekPopup;
	private int puntenAftrekPopup = 5;
	private JTextField aftrekPopupTF;
	private JLabel aftrekPopupLabel;
	private JCheckBox vulHoogteCB;
	private boolean vulHoogte;
	private JCheckBox callOutCB;
	private boolean callOut;
	private JCheckBox inklapbaarCB;
	private boolean inklapbaar;
	private boolean checkUitklapVak;
	
	private JCheckBox stylesCB;
	private boolean styles;
	private JComboBox kiesStyleChoice;
	private StyleManager styleManager;
	private JLabel styleSettingsLabel;
	private JButton editStylesButton;
	
	
	
	String[][][] randomteksten = null;
	Hashtable[][] randomIpLaunchdata = null;
	private boolean random = false;
	private JCheckBox randomCB;
	private JTextField randomTF;
	private OpdrachtNrRij randomTab;
	private int aantalRandom = 1;
	private int randomNr = 0;
	private PlusMinKnop aantalTabsKnop;
	private PlusMinKnop tabPositieKnop;
	private String randomVar = "a";
	
	//private Link link = new Link("link","http://",400,400);
	String[] httpString = new String[] {"http://", "http://", "http://", "http://",
			"http://", "http://", "http://", "http://", "http://", "http://"};
	
	private Link link = new Link("link", httpString, 400, 400, false, null);
	//private Link link = new Link("link", null, 400, 400, null);
	// kijken of nu de http's wel tevoorschijn komen
	
	
	private JDialog imageDialog1;
	private JDialog imageDialog2;
	private Iconan iconman1;
	private Iconan iconman2;
	private FormuleButton knopImageButton1;
	private FormuleButton knopImageButton2;
	private String knopImageString1 = "";
	private String knopImageString2 = "";
	private Image knopImage1;
	private Image knopImage2;
	private JRadioButton posBeginRB, posEindRB, posNaTekstRB;
	private JCheckBox checkUitklapVakCB;
	
	private JPanel optionsPanel;
	private JPanel layoutOptionsPanel, interactionOptionsPanel; 
	private JTabbedPane tabbedPane;
	
	private int defaultWidth = 1000;
	private int defaultIpHeight = 450;
	private int defaultIpWidth = 680; //hier stond 270
	private int defaultOpWidth = 280;
	private int defaultOpHeight= 580;
	
	private JCheckBox logCB;
	private JTextField logIDField;
	private JTextField logIDLabelField;
	private JLabel logIDLabelLabel;
	
	private boolean visible = true;
	private JCheckBox visibleCB;
	
	public TekstVakEditPanel(XWidgetManager manager)
	{	
		setLayout(null);
		setBackground(WiskOpdr.bgcolor);
		tekstEditor = new TekstEditor(true, true, false, new BasisTekstVak(manager));
		tekstEditor.setBackground(getBackground());
		tekstEditor.remove(tekstEditor.crosswidgetKnop);
		tekstEditor.setBounds(10,20,680,24);
		//if(!tableMode)
			add(tekstEditor);
		
		tekstVakPanel = new TekstVakPanel(1,1);
		tekstVakPanel.setBackground(getBackground());
		tekstVakPanel.setBounds(10,50,300,250);
		tekstVakPanel.setEditable(true);
		if(tableMode)add(tekstVakPanel);
		
		tekstVakPanel.addActionListener(tekstEditor);
		
		
		
		randZichtbaar = false;
		bgColorZichtbaar = false;
		zwevend = false;
		anderFont = false;
		ronding = 0;
		hoek = 0;
		centerH = false;
		centerV = false;
		pasAanH = true;
		pasAanB = false;
		selectable = false;
		sleepbaar = false;
		
		optionsPanel = new JPanel();
		optionsPanel.setLayout(null);
		optionsPanel.setBounds(defaultIpWidth+20,20,defaultOpWidth,defaultOpHeight);
		add(optionsPanel);
		
		tabbedPane = new JTabbedPane();
		tabbedPane.setBackground(getBackground());				
		tabbedPane.setBounds(0, 0, defaultOpWidth, defaultOpHeight);	
		tabbedPane.setOpaque(false);
		optionsPanel.add(tabbedPane);
		
		layoutOptionsPanel = new JPanel();
		layoutOptionsPanel.setLayout(null);
		layoutOptionsPanel.setBackground(new Color(240,240,240));		
		layoutOptionsPanel.setPreferredSize(new Dimension(defaultOpWidth, defaultOpHeight - 30));
		tabbedPane.add(WiskOpdr.rb.getString("TVEP_layoutLabel"), layoutOptionsPanel);
		
		interactionOptionsPanel = new JPanel();
		interactionOptionsPanel.setLayout(null);	
		interactionOptionsPanel.setBackground(new Color(240,240,240));		
		interactionOptionsPanel.setPreferredSize(new Dimension(defaultOpWidth, defaultOpHeight - 30));
		tabbedPane.add(WiskOpdr.rb.getString("TVEP_gebruikersInteractieLabel"), interactionOptionsPanel);
		
		cp = new JPanel();
		cp.setLayout(null);
		cp.setOpaque(false);
		cp.setBounds(0,0,1000,740);
		//add(cp);
		
		randZichtbaarCB = maakCheckBox(WiskOpdr.rb.getString("TVEP_randZichtbaar"), 10,28,110,20, randZichtbaar, layoutOptionsPanel);
		bgColorZichtbaarCB = maakCheckBox(WiskOpdr.rb.getString("TVEP_achtergrondKleurAanwezig"), 10,52,140,20, bgColorZichtbaar, layoutOptionsPanel);
		zwevendCB = maakCheckBox(WiskOpdr.rb.getString("TVEP_zwevend"), 10,76,160,20, zwevend, layoutOptionsPanel);
		anderFontCB = maakCheckBox(WiskOpdr.rb.getString("TVEP_anderFont"), 10,100,100,20, anderFont, layoutOptionsPanel);
		//buttonCB = maakCheckBox("Weergave via pop-up", 10,180,160,20, buttonOptie, layoutOptionsPanel);
		centerHCB = maakCheckBox(WiskOpdr.rb.getString("TVEP_centreerHor"), 10,175,160,20, centerH, layoutOptionsPanel);
		centerVCB = maakCheckBox(WiskOpdr.rb.getString("TVEP_centreerVert"), 10,200,160,20, centerV, layoutOptionsPanel);
		pasAanHCB = maakCheckBox(WiskOpdr.rb.getString("TVEP_pasAanH"), 10,225,120,20, pasAanH, layoutOptionsPanel);
		pasAanBCB = maakCheckBox(WiskOpdr.rb.getString("TVEP_pasAanB"), 150,225,120,20, pasAanB, layoutOptionsPanel);
		selectableCB = maakCheckBox(WiskOpdr.rb.getString("TVEP_selectieObject"), 10,70,150,20, selectable, interactionOptionsPanel);
		colorSelectionCB = maakCheckBox(WiskOpdr.rb.getString("TVEP_selectieKleur"), 160,70,100,20, selectable, interactionOptionsPanel);
		sleepbaarCB = maakCheckBox(WiskOpdr.rb.getString("TVEP_sleepObject"), 10,95,140,20, sleepbaar, interactionOptionsPanel);
		sleepdoelCB = maakCheckBox(WiskOpdr.rb.getString("TVEP_sleepDoel"), 10,120,160,20, sleepdoel, interactionOptionsPanel);
		linkCB = maakCheckBox(WiskOpdr.rb.getString("TVEP_linkObject"), 10,145,140,20, isLink, interactionOptionsPanel);
		sleepHandleCB = maakCheckBox(WiskOpdr.rb.getString("TVEP_sleepHandle"), 150,95,160,20, sleepHandle, interactionOptionsPanel);
		zichtbaarNaNakijkenCB = maakCheckBox(WiskOpdr.rb.getString("TVEP_zichtbaarNaNakijken"), 10,195,240,20, zichtbaarNaNakijken, interactionOptionsPanel);
		balansVergComCB = maakCheckBox(WiskOpdr.rb.getString("TVEP_balansVergCom"), 10,673,240,20, balansVergCom, interactionOptionsPanel);
		aftrekPopupCB = maakCheckBox(WiskOpdr.rb.getString("TVEP_aftrekPopup"), 10,220,225,20, aftrekPopup, interactionOptionsPanel);
		//stylesCB = maakCheckBox(WiskOpdr.rb.getString("TVEP_manageStyles"), 200,4,70,20, styles, layoutOptionsPanel);
		
		vulHoogteCB = maakCheckBox(WiskOpdr.rb.getString("TVEP_vulHoogte"), 10,390,225,20, vulHoogte, layoutOptionsPanel);
		callOutCB = maakCheckBox(WiskOpdr.rb.getString("TVEP_callOut"), 10,505,225,20, callOut, layoutOptionsPanel);
		inklapbaarCB = maakCheckBox(WiskOpdr.rb.getString("TVEP_inklapbaar"), 10,415,120,20, inklapbaar, layoutOptionsPanel);
		checkUitklapVakCB = maakCheckBox(WiskOpdr.rb.getString("TVEP_checkUitklapVak"), 210,415,120,20, checkUitklapVak, layoutOptionsPanel);
		randomCB = maakCheckBox(WiskOpdr.rb.getString("TVEP_random"), 240,20,70,24, random, this);
		logCB = maakCheckBox(WiskOpdr.rb.getString("logCBLabel"),10,250,70,20,false,interactionOptionsPanel);
		visibleCB = maakCheckBox(WiskOpdr.rb.getString("TVEP_visible"), 10,530,225,20, visible, layoutOptionsPanel);
        
		logIDField = new JTextField("0");
		logIDField.setBounds(100,250,70,20);
		logIDField.setFont(ifFont);
		logIDField.setVisible(false);
		logIDField.addActionListener(this);
		logIDField.addFocusListener(this);
		interactionOptionsPanel.add(logIDField);
		
		logIDLabelField = new JTextField("");
		logIDLabelField.setBounds(100,275,70,20);
		logIDLabelField.setFont(ifFont);
		logIDLabelField.setVisible(false);
		logIDLabelField.addActionListener(this);
		logIDLabelField.addFocusListener(this);
		interactionOptionsPanel.add(logIDLabelField);
		
		logIDLabelLabel = new JLabel(WiskOpdr.rb.getString("TVEP_logIDLabelLabel"));//"Gebruikersinteractie");
		logIDLabelLabel.setBounds(30,275,70,20);
		logIDLabelLabel.setFont(ifFont);
		logIDLabelLabel.setVisible(false);
		interactionOptionsPanel.add(logIDLabelLabel);
        
		
		sleepHandleCB.setVisible(false);
		colorSelectionCB.setVisible(false);
		checkUitklapVakCB.setVisible(false);
		sleepbaarCB.setEnabled(false);
		sleepdoelCB.setEnabled(false);
		
		kopLayoutLabel = new JLabel(WiskOpdr.rb.getString("TVEP_layoutLabel"));//"Layout tekstvak");
		kopLayoutLabel.setBounds(10,10,150,20);
		kopLayoutLabel.setFont(titelFont);
		//layoutOptionsPanel.add(kopLayoutLabel);
		
		kopFunctieLabel = new JLabel(WiskOpdr.rb.getString("TVEP_gebruikersInteractieLabel"));//"Gebruikersinteractie");
		kopFunctieLabel.setBounds(10,420,150,20);
		kopFunctieLabel.setFont(titelFont);
		cp.add(kopFunctieLabel);
		
		rondingLabel = new JLabel(WiskOpdr.rb.getString("TVEP_rondingHoeken"));
		rondingLabel.setBounds(10,125,150,20);
		rondingLabel.setFont(ifFont);
		layoutOptionsPanel.add(rondingLabel);
		
		rondingTF = new JTextField("0");
		rondingTF.setBounds(170,125,30,20);
		rondingTF.setFont(ifFont);
		rondingTF.addActionListener(this);
		rondingTF.addFocusListener(this);
		layoutOptionsPanel.add(rondingTF);
		
		hoekLabel = new JLabel(WiskOpdr.rb.getString("TVEP_rotatieHoek"));
		hoekLabel.setBounds(10,150,150,20);
		hoekLabel.setFont(ifFont);
		layoutOptionsPanel.add(hoekLabel);
		
		hoekTF = new JTextField("0");
		hoekTF.setBounds(170,150,30,20);
		hoekTF.setFont(ifFont);
		hoekTF.addActionListener(this);
		hoekTF.addFocusListener(this);
		layoutOptionsPanel.add(hoekTF);
		
		selectieWaardeLabel = new JLabel(WiskOpdr.rb.getString("TVEP_selectieSleepWaarde"));
		selectieWaardeLabel.setBounds(10,40,150,20);
		selectieWaardeLabel.setFont(ifFont);
		//selectieWaardeLabel.setEnabled(false);
		interactionOptionsPanel.add(selectieWaardeLabel);
		
		defaultBijNullCB = maakCheckBox(WiskOpdr.rb.getString("TVEP_defaultBijNull"), 65,40,80,20, defaultBijNull, interactionOptionsPanel);
		
		//scoreMaxTF = new JTextField("");//+scoreMax);
		//scoreMaxTF.setBounds(880,510,30,20);
		//scoreMaxTF.setFont(ifFont);
		//scoreMaxTF.addActionListener(this);
		//scoreMaxTF.setVisible(false);
		//add(scoreMaxTF);
		
		checkExpressieFormuleVak = new FormuleVak();
		checkExpressieFormuleVak.setLocation(170,40);
		checkExpressieFormuleVak.vulVak("$f0@");
		//checkExpressieFormuleVak.setEnabled(false);
		interactionOptionsPanel.add(checkExpressieFormuleVak);
		
		rondingPlusMin = new PlusMinKnop(2,140,16,20,PlusMinKnop.VERTIKAAL);
		rondingPlusMin.addActionListener(this);
		//add(rondingPlusMin);
		
		tableBordersCB = maakCheckBox(WiskOpdr.rb.getString("TVEP_tabelRanden"), 10,310,160,20, tableBorders, layoutOptionsPanel);
		
		// nog even niet
		//cp.remove(buttonCB);
		
		aantalRijenLabel = new JLabel(WiskOpdr.rb.getString("TVEP_aantalRijen"));
		aantalRijenLabel.setBounds(10,255,120,20);
		aantalRijenLabel.setFont(ifFont);
		layoutOptionsPanel.add(aantalRijenLabel);
		
		aantalRijenTF = new JTextField("1");
		aantalRijenTF.setBounds(130,255,30,20);
		aantalRijenTF.setFont(ifFont);
		//aantalRijenTF.addActionListener(this);
		aantalRijenTF.setEditable(false);
		layoutOptionsPanel.add(aantalRijenTF);
		
		rijenPlusMin = new PlusMinKnop(162,255,16,20,PlusMinKnop.VERTIKAAL);
		rijenPlusMin.addActionListener(this);
		layoutOptionsPanel.add(rijenPlusMin);
		
		cellSpaceRowLabel = new JLabel(WiskOpdr.rb.getString("TVEP_cellSpaceRow"));
		cellSpaceRowLabel.setBounds(185,255,50,20);
		cellSpaceRowLabel.setFont(ifFont);
		layoutOptionsPanel.add(cellSpaceRowLabel);
		
		cellSpaceRowTF = new JTextField("2");
		cellSpaceRowTF.setBounds(235,255,30,20);
		cellSpaceRowTF.setFont(ifFont);
		cellSpaceRowTF.addActionListener(this);
		cellSpaceRowTF.addFocusListener(this);
		layoutOptionsPanel.add(cellSpaceRowTF);
				
		aantalKolommenLabel = new JLabel(WiskOpdr.rb.getString("TVEP_aantalKolommen"));
		aantalKolommenLabel.setBounds(10,280,120,20);
		aantalKolommenLabel.setFont(ifFont);
		layoutOptionsPanel.add(aantalKolommenLabel);
		
		aantalKolommenTF = new JTextField("1");
		aantalKolommenTF.setBounds(130,280,30,20);
		aantalKolommenTF.setFont(ifFont);
		//aantalKolommenTF.addActionListener(this);
		aantalKolommenTF.setEditable(false);
		layoutOptionsPanel.add(aantalKolommenTF);
		
		kolommenPlusMin = new PlusMinKnop(162,280,16,20,PlusMinKnop.VERTIKAAL);
		kolommenPlusMin.addActionListener(this);
		layoutOptionsPanel.add(kolommenPlusMin);
		
		cellSpaceColumnLabel = new JLabel(WiskOpdr.rb.getString("TVEP_cellSpaceColumn"));
		cellSpaceColumnLabel.setBounds(185,280,50,20);
		cellSpaceColumnLabel.setFont(ifFont);
		layoutOptionsPanel.add(cellSpaceColumnLabel);
		
		cellSpaceColumnTF = new JTextField("2");
		cellSpaceColumnTF.setBounds(235,280,30,20);
		cellSpaceColumnTF.setFont(ifFont);
		cellSpaceColumnTF.addActionListener(this);
		cellSpaceColumnTF.addFocusListener(this);
		layoutOptionsPanel.add(cellSpaceColumnTF);
		
		cellMargeLabel = new JLabel(WiskOpdr.rb.getString("TVEP_celMarge"));
		cellMargeLabel.setBounds(10,335,70,20);
		cellMargeLabel.setFont(ifFont);
		layoutOptionsPanel.add(cellMargeLabel);
		
		cellMargeTF = new JTextField("0");
		cellMargeTF.setBounds(80,335,50,20);
		cellMargeTF.setFont(ifFont);
		cellMargeTF.addActionListener(this);
		cellMargeTF.addFocusListener(this);
		layoutOptionsPanel.add(cellMargeTF);
		
		bovenMargeLabel = new JLabel(WiskOpdr.rb.getString("TVEP_bovenMarge"));
		bovenMargeLabel.setBounds(140,335,70,20);
		bovenMargeLabel.setFont(ifFont);
		layoutOptionsPanel.add(bovenMargeLabel);
        
        bovenMargeTF = new JTextField("0");
        bovenMargeTF.setBounds(210,335,50,20);
        bovenMargeTF.setFont(ifFont);
        bovenMargeTF.addActionListener(this);
        bovenMargeTF.addFocusListener(this);
        layoutOptionsPanel.add(bovenMargeTF);
       
        interlinieLabel = new JLabel(WiskOpdr.rb.getString("TVEP_interlinie"));
        interlinieLabel.setBounds(10,360,70,20);
        interlinieLabel.setFont(ifFont);
        layoutOptionsPanel.add(interlinieLabel);
		
		interlinieTF = new JTextField("0");
		interlinieTF.setBounds(80,360,50,20);
		interlinieTF.setFont(ifFont);
		interlinieTF.addActionListener(this);
		interlinieTF.addFocusListener(this);
		layoutOptionsPanel.add(interlinieTF);
		
		randDikteTF = new JTextField("1");
		randDikteTF.setBounds(120,28,30,20);
		randDikteTF.setFont(ifFont);
		randDikteTF.addActionListener(this);
		randDikteTF.addFocusListener(this);
		randDikteTF.setVisible(false);
		layoutOptionsPanel.add(randDikteTF);
		
		fontButton = new JButton(WiskOpdr.rb.getString("TVEP_fontType"));
		fontButton.addActionListener(this);
		fontButton.setFont(ifFont);
		fontButton.setBounds(180,100,80,20);
		fontButton.setVisible(false);
		layoutOptionsPanel.add(fontButton);
		
		randColorButton = new JButton(WiskOpdr.rb.getString("TVEP_achtergrondKleur"));
		randColorButton.addActionListener(this);
		randColorButton.setFont(ifFont);
		randColorButton.setBounds(160,28,100,20);
		randColorButton.setMargin(new Insets(4,5,4,5));
		randColorButton.setVisible(false);
		layoutOptionsPanel.add(randColorButton);
		
		bgColorButton = new JButton(WiskOpdr.rb.getString("TVEP_achtergrondKleur"));
		bgColorButton.addActionListener(this);
		bgColorButton.setFont(ifFont);
		bgColorButton.setBounds(160,52,100,20);
		bgColorButton.setMargin(new Insets(4,5,4,5));
		bgColorButton.setVisible(false);
		layoutOptionsPanel.add(bgColorButton);
		
		fgColorButton = new JButton(WiskOpdr.rb.getString("TVEP_fgKleurKnopLabel"));
		fgColorButton.addActionListener(this);
		fgColorButton.setFont(ifFont);
		fgColorButton.setBounds(110,100,60,20);
		fgColorButton.setMargin(new Insets(4,5,4,5));
		fgColorButton.setVisible(false);
		layoutOptionsPanel.add(fgColorButton);
		
		linkButton = new JButton(WiskOpdr.rb.getString("TVEP_editLink"));
		linkButton.addActionListener(this);
		linkButton.setFont(ifFont);
		linkButton.setBounds(150,145,120,20);
		linkButton.setVisible(false);
		interactionOptionsPanel.add(linkButton);

		
		interactiePanelIdLabel = new JLabel("ID = ");
		interactiePanelIdLabel.setBounds(10,10,40,20);
		interactiePanelIdLabel.setFont(ifFont);
		interactionOptionsPanel.add(interactiePanelIdLabel);
		
		interactiePanelIdTF = new JTextField("0");
		interactiePanelIdTF.addActionListener(this);
		interactiePanelIdTF.addFocusListener(this);
		interactiePanelIdTF.setBounds(50,10,50,20);
		interactiePanelIdTF.setFont(ifFont);
		interactionOptionsPanel.add(interactiePanelIdTF);
		
		aftrekPopupTF = new JTextField(""+puntenAftrekPopup);
		aftrekPopupTF.setBounds(240,220,30,20);
		aftrekPopupTF.setFont(ifFont);
		aftrekPopupTF.addActionListener(this);
		aftrekPopupTF.addFocusListener(this);
		aftrekPopupTF.setVisible(false);
		interactionOptionsPanel.add(aftrekPopupTF);
		
		aftrekPopupLabel = new JLabel(WiskOpdr.rb.getString("TVEP_puntenaftrek"));
		aftrekPopupLabel.setBounds(165,220,70,20);
		aftrekPopupLabel.setFont(ifFont);
		aftrekPopupLabel.setVisible(false);
		interactionOptionsPanel.add(aftrekPopupLabel);
		
		randomTF = new JTextField(randomVar);
		randomTF.setBounds(320,22,30,20);
		randomTF.setFont(ifFont);
		randomTF.addActionListener(this);
		randomTF.addFocusListener(this);
		randomTF.setVisible(false);
		add(randomTF,0);
		add(randomCB,0);
		
		randomTab = new OpdrachtNrRij(aantalRandom, 360,22);
		randomTab.setSize(randomTab.getSize().width, 23);
		randomTab.setTab(true);
		randomTab.setScoresVisible(false);
		randomTab.addActionListener(this);
		randomTab.setBackground(new Color(210,210,210));
		randomTab.setSelected(1);
		randomTab.setVisible(false);
		add(randomTab,0);
		
		aantalTabsKnop = new PlusMinKnop(360+25*aantalRandom+5 ,24,20,16,PlusMinKnop.HORIZONTAAL);
		aantalTabsKnop.setBackground(new Color(210,210,210));
		aantalTabsKnop.addActionListener(this);
		aantalTabsKnop.setVisible(false);
    	add(aantalTabsKnop,0);
    	
    	tabPositieKnop = new PlusMinKnop(356+25*randomNr+5 ,2,20,16,PlusMinKnop.HORIZONTAAL);
    	tabPositieKnop.addActionListener(this);
    	tabPositieKnop.setVisible(false);
    	add(tabPositieKnop,0);
    	
    	randomteksten = new String[1][][];
    	randomIpLaunchdata = new Hashtable[1][];
    	
    	knopImageButton1 = new FormuleButton("\u25b8");
		knopImageButton1.setBounds(130,415,20,20);
		knopImageButton1.addActionListener(this);
		knopImageButton1.setVisible(false);
		layoutOptionsPanel.add(knopImageButton1);
		
		knopImageButton2 = new FormuleButton("\u25be");
		knopImageButton2.setBounds(170,415,20,20);
		knopImageButton2.addActionListener(this);
		knopImageButton2.setVisible(false);
		layoutOptionsPanel.add(knopImageButton2);
		
		posBeginRB = new JRadioButton(WiskOpdr.rb.getString("TVEP_knopLinks"));
		posBeginRB.setBounds(20,440,200,20);
		posBeginRB.addActionListener(this);
		posBeginRB.setFont(ifFont);
		posBeginRB.setVisible(false);
		layoutOptionsPanel.add(posBeginRB);
		
		posEindRB = new JRadioButton(WiskOpdr.rb.getString("TVEP_knopRechts"));
		posEindRB.setBounds(20,460,200,20);
		posEindRB.addActionListener(this);
		posEindRB.setFont(ifFont);
		posEindRB.setSelected(true);
		posEindRB.setVisible(false);
		layoutOptionsPanel.add(posEindRB);
		
		posNaTekstRB = new JRadioButton(WiskOpdr.rb.getString("TVEP_knopAchterTekst"));
		posNaTekstRB.setBounds(20,480,200,20);
		posNaTekstRB.addActionListener(this);
		posNaTekstRB.setFont(ifFont);
		posNaTekstRB.setVisible(false);
		layoutOptionsPanel.add(posNaTekstRB);
		
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(posBeginRB);
		buttonGroup.add(posEindRB);
		buttonGroup.add(posNaTekstRB);
		
		kiesStyleChoice = new JComboBox();
		kiesStyleChoice.setFont(ifFont);
		kiesStyleChoice.addItem("No style");
		for (String key : TekstVakPanel.styles.keySet()) 
		{	kiesStyleChoice.addItem(key);
		}
		//kiesStyleChoice.addItem("Style 1");
		kiesStyleChoice.addActionListener(this);
		kiesStyleChoice.setBounds(10,4,190,20);
		//kiesStyleChoice.setVisible(false);
		layoutOptionsPanel.add(kiesStyleChoice);
		
		styleManager = new StyleManager(this, kiesStyleChoice);
		styleManager.setBounds(0, 390, 260, 155);
		styleManager.addActionListener(this);
		styleManager.setVisible(false);
		layoutOptionsPanel.add(styleManager,0);
		
		kiesStyleChoice.addActionListener(styleManager);
		
		styleSettingsLabel = new JLabel("Style settings");
		styleSettingsLabel.setBounds(0,2,250,25);
		styleSettingsLabel.setHorizontalAlignment(JLabel.CENTER);
		styleSettingsLabel.setFont(titelFont);
		styleSettingsLabel.setVisible(false);
		layoutOptionsPanel.add(styleSettingsLabel);
		
		editStylesButton = new JButton("edit");
		editStylesButton.setBounds(210,4,50,20);
		editStylesButton.setMargin(new Insets(0, 0, 0, 0));
		editStylesButton.addActionListener(this);
		editStylesButton.setFont(ifFont);
		layoutOptionsPanel.add(editStylesButton);
		
	}
	
	private JCheckBox maakCheckBox(String s, int x, int y, int b, int h, boolean selected, JPanel parent)
	{	JCheckBox checkbox = new JCheckBox(s);
		checkbox.setBounds(x,y,b,h);
		checkbox.setFont(ifFont);
		checkbox.setBackground(getBackground());
		checkbox.setOpaque(false);
		checkbox.setSelected(selected);
		checkbox.addActionListener(this);
		parent.add(checkbox);
		
		return checkbox;
	}
	
	public void setTableMode(boolean b)
	{	tableMode = b;
	}
	
	public Vector geefInteractiePanels()
	{	Vector v = null; 
		if(tableMode) v = tekstVakPanel.geefInteractiePanels();
		else v = tekstEditor.geefInteractiePanels();
		return v;
	}
	
	public Hashtable getEditState()
	{	
		String styleString = null;
		if(kiesStyleChoice.getSelectedIndex()>0)
			styleString = (String)kiesStyleChoice.getSelectedItem();
		
		boolean randZichtbaar = false;
		boolean bgColorZichtbaar = false;
		Color bgColor = new Color(255,255,180);
		Color fgColor = new Color(0,0,0);
		Color randColor = Color.gray;
		boolean zwevend = true;
		boolean anderFont = false;
		boolean formuleToolPopup = true;
		boolean buttonOptie = false;
		boolean tableBorders = false;
		Hashtable[] interactiePanelLaunchData = null;
		int scoreMax = 0;
		int cellMarge = 5;
		int bovenMarge = 0;
		int ronding = 0;
		int hoek = 0;
		boolean centerH = false;
		boolean centerV = false;
		boolean pasAanH = false;
		boolean pasAanB = false;
		boolean selectable = false;
		boolean colorSelection = false;
		boolean sleepbaar = false;
		boolean sleepdoel = false;
		boolean sleepHandle = false;
		String checkExpressieString = "$f@";
		int ipId = 0;
		int interlinie = 0;
		int cellSpaceColumn = 2; 
		int cellSpaceRow = 2; 
		int randDikte = 1;
		boolean zichtbaarNaNakijken = false;
		boolean balansVergCom = false;
		boolean aftrekPopup = false;
		int puntenAftrekPopup = 5;
		boolean callOut = false;
		boolean vulHoogte = false;
		boolean inklapbaar = false;
		boolean checkUitklapVak = false;
		int inklapKnopPos = 1;
		String knopImageString1 = "";
		String knopImageString2 = "";
		boolean isLink = false;
		boolean defaultBijNull = false;
		//String linkUrl = "";
		String[] linkUrls = null;
		int[] grensScores = null;
		int linkWidth = 400;
		int linkHeight = 400;
		
		String[][][] randomteksten = new String[1][][];
		Hashtable[][] randomIpLaunchdata = new Hashtable[1][];
		int aantalRandom = 1;
		boolean random = false;
		String randomVar = "a";
		
		String[][] teksten = null;
		double[] breedtes = null;
		double[] hoogtes = null;
		
		boolean logOption = false;
		String logID = "";
		String logIDLabel = "";
		boolean visible = true;
		
		
		
		this.ronding = Integer.parseInt(rondingTF.getText());
		this.hoek = Integer.parseInt(hoekTF.getText());
		this.ipId = Integer.parseInt(interactiePanelIdTF.getText());
		
		inklapKnopPos = posEindRB.isSelected() ? 1 : (posNaTekstRB.isSelected() ? 2 : 0);
		
		randZichtbaar = this.randZichtbaar;
		bgColorZichtbaar = this.bgColorZichtbaar;
		bgColor = this.bgColor;
		fgColor = this.fgColor;
		randColor = this.randColor;
		zwevend = this.zwevend;
		anderFont = this.anderFont;
		buttonOptie = this.buttonOptie;
		tableBorders = this.tableBorders;
		cellMarge = this.cellMarge;
		bovenMarge = this.bovenMarge;
		ronding = this.ronding;
		hoek = this.hoek;
		centerH = this.centerH;
		centerV = this.centerV;
		pasAanH = this.pasAanH;
		pasAanB = this.pasAanB;
		selectable = this.selectable;
		colorSelection = this.colorSelection;
		sleepbaar = this.sleepbaar;
		sleepdoel = this.sleepdoel;
		sleepHandle = this.sleepHandle;
		checkExpressieString = checkExpressieFormuleVak.toString();
		ipId =  this.ipId;
		interlinie =  this.interlinie;
		cellSpaceColumn =  this.cellSpaceColumn;
		cellSpaceRow =  this.cellSpaceRow;
		randDikte = this.randDikte;
		zichtbaarNaNakijken = this.zichtbaarNaNakijken;
		balansVergCom = this.balansVergCom;
		aftrekPopup = this.aftrekPopup;
		puntenAftrekPopup = this.puntenAftrekPopup;
		callOut = this.callOut;
		vulHoogte = this.vulHoogte;
		inklapbaar = this.inklapbaar;
		checkUitklapVak = this.checkUitklapVak;	
		knopImageString1 = this.knopImageString1;
		knopImageString2 = this.knopImageString2;
		randomteksten = this.randomteksten;
		randomIpLaunchdata = this.randomIpLaunchdata;
		aantalRandom = this.aantalRandom;
		random = this.random;
		randomVar = this.randomVar;
		isLink = this.isLink;
		defaultBijNull = this.defaultBijNull;
		linkUrls = link.getUrlString();
		grensScores = link.getGrensScores();
		linkWidth = link.getWidth();
		linkWidth = link.getHeight();
		
		logOption = logCB.isSelected();
		logID = logIDField.getText();
		logIDLabel = logIDLabelField.getText();
		visible = this.visible;
			
		Hashtable h = null;
		 
		if(tableMode) h = tekstVakPanel.getEditState();
		else h = tekstEditor.getEditState();
		
		if(random) 
		{	randomteksten[randomNr] = (String[][])tekstVakPanel.getEditState().get("teksten");
			randomIpLaunchdata[randomNr] = (Hashtable[])tekstVakPanel.getEditState().get("interactiePanelLaunchData");
			h.put("teksten",randomteksten[0]);
			h.put("interactiePanelLaunchData",randomIpLaunchdata[0]);
		}
		
		if(styleString!=null)
			h.put("styleString", styleString);
		
		{
			h.put("randZichtbaar", new Boolean(randZichtbaar));
			h.put("bgColorZichtbaar", new Boolean(bgColorZichtbaar));
			if(bgColor!=null)h.put("bgColor", bgColor);
			h.put("fgColor", fgColor);
			h.put("randColor", randColor);
			h.put("anderFont", new Boolean(anderFont));
			h.put("tableBorders", new Boolean(tableBorders));
			h.put("cellMarge",new Integer(cellMarge));
			h.put("bovenMarge",new Integer(bovenMarge));
			h.put("ronding",new Integer(ronding));
			h.put("hoek",new Integer(hoek));
			h.put("centerH", new Boolean(centerH));
			h.put("centerV", new Boolean(centerV));
			h.put("pasAanH", new Boolean(pasAanH));
			h.put("pasAanB", new Boolean(pasAanB));
			h.put("interlinie",new Integer(interlinie));
			h.put("cellSpaceColumn",new Integer(cellSpaceColumn));
			h.put("cellSpaceRow",new Integer(cellSpaceRow));
			h.put("randDikte",new Integer(randDikte));
		}
		
		h.put("zwevend", new Boolean(zwevend));
		h.put("buttonOptie", new Boolean(buttonOptie));
		if(breedtes!=null)h.put("breedtes", breedtes);
		if(hoogtes!=null)h.put("hoogtes", hoogtes);
		h.put("selectable", new Boolean(selectable));
		h.put("colorSelection", new Boolean(colorSelection));
		h.put("sleepbaar", new Boolean(sleepbaar));
		h.put("sleepdoel", new Boolean(sleepdoel));
		h.put("sleepHandle", new Boolean(sleepHandle));
		h.put("checkExpressieString", checkExpressieString);
		h.put("ipId",new Integer(ipId));
		h.put("zichtbaarNaNakijken", new Boolean(zichtbaarNaNakijken));
		h.put("balansVergCom", new Boolean(balansVergCom));
		h.put("aftrekPopup", new Boolean(aftrekPopup));
		h.put("puntenAftrekPopup",new Integer(puntenAftrekPopup));
		h.put("callOut", new Boolean(callOut));
		h.put("vulHoogte", new Boolean(vulHoogte));
		h.put("inklapbaar", new Boolean(inklapbaar));
		if(inklapbaar)
		{	h.put("knopImageString1", knopImageString1);
			h.put("knopImageString2", knopImageString2);
			h.put("inklapKnopPos", inklapKnopPos);
			h.put("checkUitklapVak", new Boolean(checkUitklapVak));
		}
		h.put("random", new Boolean(random));
		h.put("isLink", new Boolean(isLink));
		h.put("defaultBijNull", new Boolean(defaultBijNull));
		
		if(random) 
		{	h.put("randomteksten", randomteksten);
			h.put("randomIpLaunchdata", randomIpLaunchdata);
			h.put("aantalRandom",new Integer(aantalRandom));
			h.put("randomVar",randomVar);
		}
		if(isLink) 
		{	h.put("linkUrls", linkUrls);
			h.put("linkWidth", new Integer(linkWidth));
			h.put("linkHeight", new Integer(linkHeight));
		}
		h.put("logOption",new Boolean(logOption));
		h.put("logID",logID);
		h.put("logIDLabel",logIDLabel);
		h.put("visible", new Boolean(visible));
		
		return h;
	}
	
	private void setEditStyle()
	{
		Hashtable style = null;
		String styleString = null;
		if(kiesStyleChoice.getSelectedIndex()>0)
			styleString = (String)kiesStyleChoice.getSelectedItem();
		if(styleString!=null)
			if(TekstVakPanel.styles.containsKey(styleString));
				style = (Hashtable)TekstVakPanel.styles.get(styleString);
			
		if(style!=null)	
		{	if(style.containsKey("randZichtbaar")) randZichtbaar = ((Boolean)style.get("randZichtbaar")).booleanValue();
			if(style.containsKey("bgColorZichtbaar")) bgColorZichtbaar = ((Boolean)style.get("bgColorZichtbaar")).booleanValue();
				if(bgColorZichtbaar) bgColor = new Color(255,255,180);
			if(style.containsKey("bgColor")) bgColor = (Color)style.get("bgColor");
			if(style.containsKey("fgColor")) fgColor = (Color)style.get("fgColor");
			if(style.containsKey("randColor")) randColor = (Color)style.get("randColor");
			if(style.containsKey("tableBorders")) tableBorders = ((Boolean)style.get("tableBorders")).booleanValue();
			if(style.containsKey("cellMarge")) cellMarge = ((Integer)style.get("cellMarge")).intValue();
			if(style.containsKey("bovenMarge")) bovenMarge = ((Integer)style.get("bovenMarge")).intValue();
		    if(style.containsKey("ronding")) ronding = ((Integer)style.get("ronding")).intValue();
				if(anderFont) font = new Font("SansSerif", Font.BOLD, 14);
			if(style.containsKey("font")) font = (Font)style.get("font");
			if(style.containsKey("hoek")) hoek = ((Integer)style.get("hoek")).intValue();
			if(style.containsKey("centerH")) centerH = ((Boolean)style.get("centerH")).booleanValue();
			if(style.containsKey("centerV")) centerV = ((Boolean)style.get("centerV")).booleanValue();
			if(style.containsKey("pasAanH")) pasAanH = ((Boolean)style.get("pasAanH")).booleanValue();
			if(style.containsKey("pasAanB")) pasAanB = ((Boolean)style.get("pasAanB")).booleanValue();
			if(style.containsKey("interlinie")) interlinie = ((Integer)style.get("interlinie")).intValue();
			if(style.containsKey("cellSpaceColumn")) cellSpaceColumn = ((Integer)style.get("cellSpaceColumn")).intValue();
			if(style.containsKey("cellSpaceRow")) cellSpaceRow = ((Integer)style.get("cellSpaceRow")).intValue();
			if(style.containsKey("randDikte")) randDikte = ((Integer)style.get("randDikte")).intValue();
			
			randZichtbaarCB.setSelected(randZichtbaar);
			randDikteTF.setVisible(randZichtbaar);
			randColorButton.setVisible(randZichtbaar);
			bgColorZichtbaarCB.setSelected(bgColorZichtbaar);
			anderFontCB.setSelected(anderFont);
			//buttonCB.setSelected(buttonOptie);
			tableBordersCB.setSelected(tableBorders);
			fontButton.setVisible(anderFont);
			fgColorButton.setVisible(anderFont);
			centerHCB.setSelected(centerH);
			centerVCB.setSelected(centerV);
			pasAanHCB.setSelected(pasAanH);
			pasAanBCB.setSelected(pasAanB);
			
			randDikteTF.setText(Integer.toString(randDikte));
			cellMargeTF.setText(Integer.toString(cellMarge));
			bovenMargeTF.setText(Integer.toString(bovenMarge));
	        rondingTF.setText(Integer.toString(ronding));
			hoekTF.setText(Integer.toString(hoek));
			interlinieTF.setText(Integer.toString(interlinie));
			cellSpaceColumnTF.setText(Integer.toString(cellSpaceColumn));
			cellSpaceRowTF.setText(Integer.toString(cellSpaceRow));
			
			
		}
	}
	public void setEditState(Hashtable h)
	{
		String styleString = null;
		
		String[][] teksten = null;
		boolean randZichtbaar = false;
		boolean bgColorZichtbaar = true;
		Color bgColor = null;
		Color fgColor = Color.black;
		Color randColor = Color.gray;
		boolean zwevend = true;
		boolean anderFont = false;
		boolean buttonOptie = false;
		boolean tableBorders = false;
		int cellMarge = 5;
		int bovenMarge = 0;
		int ronding = 0;
		Font font = WiskOpdr.tekstFont;
		int hoek = 0;
		boolean centerH = false;
		boolean centerV = false;
		boolean pasAanH = false;
		boolean pasAanB = false;
		boolean selectable = false;
		boolean colorSelection = false;
		boolean sleepbaar = false;
		boolean sleepdoel = false;
		boolean sleepHandle = false;
		String checkExpressieString = "$f@";
		int scoreMax = 0;
		int ipId = 0;
		int interlinie = 0;
		int cellSpaceColumn = 2; 
		int cellSpaceRow = 2; 
		int randDikte = 1;
		boolean zichtbaarNaNakijken = false;
		boolean balansVergCom = false;
		boolean aftrekPopup = false;
		int puntenAftrekPopup = 5;
		boolean callOut = false;
		boolean vulHoogte = false;
		boolean inklapbaar = false;
		boolean checkUitklapVak = false;
		int inklapKnopPos = 1;
		String knopImageString1 = "";
		String knopImageString2 = "";
		boolean isLink = false;
		boolean defaultBijNull = false;
		//String linkUrl = "";
		String[] linkUrls = null;
		int[] grensScores = null;
		int linkWidth = 400;
		int linkHeight = 400;
		boolean logOption = false;
		String logID = "";
		String logIDLabel = "";
		boolean visible = true;
		
		String[][][] randomteksten = new String[1][][];
		Hashtable[][] randomIpLaunchdata = new Hashtable[1][];
		int aantalRandom = 1;
		boolean random = false;
		String randomVar = "a";
		
		Hashtable style = null;
		if(h.containsKey("styleString")) styleString = (String)h.get("styleString");
		if(styleString!=null)
			if(TekstVakPanel.styles.containsKey(styleString)) 
				style = (Hashtable)TekstVakPanel.styles.get(styleString);
			
		if(style!=null)	
		{	if(style.containsKey("randZichtbaar")) randZichtbaar = ((Boolean)style.get("randZichtbaar")).booleanValue();
			if(style.containsKey("bgColorZichtbaar")) bgColorZichtbaar = ((Boolean)style.get("bgColorZichtbaar")).booleanValue();
				if(bgColorZichtbaar) bgColor = new Color(255,255,180);
			if(style.containsKey("bgColor")) bgColor = (Color)style.get("bgColor");
			if(style.containsKey("fgColor")) fgColor = (Color)style.get("fgColor");
			if(style.containsKey("randColor")) randColor = (Color)style.get("randColor");
			if(style.containsKey("tableBorders")) tableBorders = ((Boolean)style.get("tableBorders")).booleanValue();
			if(style.containsKey("cellMarge")) cellMarge = ((Integer)style.get("cellMarge")).intValue();
			if(style.containsKey("bovenMarge")) bovenMarge = ((Integer)style.get("bovenMarge")).intValue();
		    if(style.containsKey("ronding")) ronding = ((Integer)style.get("ronding")).intValue();
				if(anderFont) font = new Font("SansSerif", Font.BOLD, 14);
			if(style.containsKey("font")) font = (Font)style.get("font");
			if(style.containsKey("hoek")) hoek = ((Integer)style.get("hoek")).intValue();
			if(style.containsKey("centerH")) centerH = ((Boolean)style.get("centerH")).booleanValue();
			if(style.containsKey("centerV")) centerV = ((Boolean)style.get("centerV")).booleanValue();
			if(style.containsKey("pasAanH")) pasAanH = ((Boolean)style.get("pasAanH")).booleanValue();
			if(style.containsKey("pasAanB")) pasAanB = ((Boolean)style.get("pasAanB")).booleanValue();
			if(style.containsKey("interlinie")) interlinie = ((Integer)style.get("interlinie")).intValue();
			if(style.containsKey("cellSpaceColumn")) cellSpaceColumn = ((Integer)style.get("cellSpaceColumn")).intValue();
			if(style.containsKey("cellSpaceRow")) cellSpaceRow = ((Integer)style.get("cellSpaceRow")).intValue();
			if(style.containsKey("randDikte")) randDikte = ((Integer)style.get("randDikte")).intValue();
		}
		else
		{
			if(h.containsKey("randZichtbaar")) randZichtbaar = ((Boolean)h.get("randZichtbaar")).booleanValue();
			if(h.containsKey("bgColorZichtbaar")) bgColorZichtbaar = ((Boolean)h.get("bgColorZichtbaar")).booleanValue();
				if(bgColorZichtbaar) bgColor = new Color(255,255,180);
			if(h.containsKey("bgColor")) bgColor = (Color)h.get("bgColor");
			if(h.containsKey("fgColor")) fgColor = (Color)h.get("fgColor");
			if(h.containsKey("randColor")) randColor = (Color)h.get("randColor");
			if(h.containsKey("tableBorders")) tableBorders = ((Boolean)h.get("tableBorders")).booleanValue();
			if(h.containsKey("cellMarge")) cellMarge = ((Integer)h.get("cellMarge")).intValue();
			if(h.containsKey("bovenMarge")) bovenMarge = ((Integer)h.get("bovenMarge")).intValue();
	        if(h.containsKey("ronding")) ronding = ((Integer)h.get("ronding")).intValue();
				if(anderFont) font = new Font("SansSerif", Font.BOLD, 14);
			if(h.containsKey("font")) font = (Font)h.get("font");
			if(h.containsKey("hoek")) hoek = ((Integer)h.get("hoek")).intValue();
			if(h.containsKey("centerH")) centerH = ((Boolean)h.get("centerH")).booleanValue();
			if(h.containsKey("centerV")) centerV = ((Boolean)h.get("centerV")).booleanValue();
			if(h.containsKey("pasAanH")) pasAanH = ((Boolean)h.get("pasAanH")).booleanValue();
			if(h.containsKey("pasAanB")) pasAanB = ((Boolean)h.get("pasAanB")).booleanValue();
			if(h.containsKey("interlinie")) interlinie = ((Integer)h.get("interlinie")).intValue();
			if(h.containsKey("cellSpaceColumn")) cellSpaceColumn = ((Integer)h.get("cellSpaceColumn")).intValue();
			if(h.containsKey("cellSpaceRow")) cellSpaceRow = ((Integer)h.get("cellSpaceRow")).intValue();
			if(h.containsKey("randDikte")) randDikte = ((Integer)h.get("randDikte")).intValue();
		}
		
		if(h.containsKey("teksten")) teksten = (String[][])h.get("teksten");
		if(h.containsKey("zwevend")) zwevend = ((Boolean)h.get("zwevend")).booleanValue();
		if(h.containsKey("anderFont")) anderFont = ((Boolean)h.get("anderFont")).booleanValue();
		if(h.containsKey("buttonOptie")) buttonOptie = ((Boolean)h.get("buttonOptie")).booleanValue();
		if(h.containsKey("selectable")) selectable = ((Boolean)h.get("selectable")).booleanValue();
		if(h.containsKey("colorSelection")) colorSelection = ((Boolean)h.get("colorSelection")).booleanValue();
		if(h.containsKey("sleepbaar")) sleepbaar = ((Boolean)h.get("sleepbaar")).booleanValue();
		if(h.containsKey("sleepdoel")) sleepdoel = ((Boolean)h.get("sleepdoel")).booleanValue();
		if(h.containsKey("sleepHandle")) sleepHandle = ((Boolean)h.get("sleepHandle")).booleanValue();
		if(h.containsKey("checkExpressieString")) checkExpressieString = (String)h.get("checkExpressieString");
		if(h.containsKey("ipId")) ipId = ((Integer)h.get("ipId")).intValue();
		if(h.containsKey("zichtbaarNaNakijken")) zichtbaarNaNakijken = ((Boolean)h.get("zichtbaarNaNakijken")).booleanValue();
		if(h.containsKey("balansVergCom")) balansVergCom = ((Boolean)h.get("balansVergCom")).booleanValue();
		if(h.containsKey("aftrekPopup")) aftrekPopup = ((Boolean)h.get("aftrekPopup")).booleanValue();
		if(h.containsKey("puntenAftrekPopup")) puntenAftrekPopup = ((Integer)h.get("puntenAftrekPopup")).intValue();
		if(h.containsKey("callOut")) callOut = ((Boolean)h.get("callOut")).booleanValue();
		if(h.containsKey("vulHoogte")) vulHoogte = ((Boolean)h.get("vulHoogte")).booleanValue();
		if(h.containsKey("inklapbaar")) inklapbaar = ((Boolean)h.get("inklapbaar")).booleanValue();
		if(h.containsKey("checkUitklapVak")) checkUitklapVak = ((Boolean)h.get("checkUitklapVak")).booleanValue();
		if(h.containsKey("inklapKnopPos")) inklapKnopPos = ((Integer)h.get("inklapKnopPos")).intValue();
		if(h.containsKey("knopImageString1")) knopImageString1 = (String)h.get("knopImageString1");
		if(h.containsKey("knopImageString2")) knopImageString2 = (String)h.get("knopImageString2");
		if(h.containsKey("random")) random = ((Boolean)h.get("random")).booleanValue();
		if(h.containsKey("aantalRandom")) aantalRandom = ((Integer)h.get("aantalRandom")).intValue();
		if(h.containsKey("randomteksten")) randomteksten = (String[][][]) h.get("randomteksten");
		if(h.containsKey("randomIpLaunchdata")) randomIpLaunchdata = (Hashtable[][]) h.get("randomIpLaunchdata");
		if(h.containsKey("randomVar")) randomVar = (String) h.get("randomVar");
		if(h.containsKey("isLink")) isLink = ((Boolean) h.get("isLink")).booleanValue();
		if(h.containsKey("defaultBijNull")) defaultBijNull = ((Boolean) h.get("defaultBijNull")).booleanValue();
		//if(h.containsKey("linkUrl")) linkUrl = (String) h.get("linkUrl");
		if(h.containsKey("linkUrls")) linkUrls = (String[]) h.get("linkUrls");
		if(h.containsKey("grensScores")) grensScores = (int[]) h.get("grensScores");
		if(h.containsKey("linkWidth")) linkWidth = ((Integer)h.get("linkWidth")).intValue();
		if(h.containsKey("linkHeight")) linkHeight = ((Integer)h.get("linkHeight")).intValue();
		if(h.containsKey("logOption")) logOption = ((Boolean)h.get("logOption")).booleanValue();
        if(h.containsKey("logID")) logID = (String)h.get("logID");
        if(h.containsKey("logIDLabel")) logIDLabel = (String)h.get("logIDLabel");
        if(h.containsKey("visible")) visible = ((Boolean) h.get("visible")).booleanValue();
		
        System.out.println("logOption: "+logOption);

		this.randZichtbaar = randZichtbaar;
		this.randDikte = randDikte;
		this.bgColorZichtbaar = bgColorZichtbaar;
		this.bgColor = bgColor;
		this.fgColor = fgColor;
		this.randColor = randColor;
		this.zwevend = zwevend;
		this.anderFont = anderFont;
		this.buttonOptie = buttonOptie;
		this.tableBorders = tableBorders;
		this.cellMarge = cellMarge;
		this.bovenMarge = bovenMarge;
        this.ronding = ronding;
		this.hoek = hoek;
		Font ff = new Font(font.getName(), font.getStyle(), font.getSize());
		this.font = ff;
		this.centerH = centerH;
		this.centerV = centerV;
		this.pasAanH = pasAanH;
		this.pasAanB = pasAanB;
		this.selectable = selectable;
		this.colorSelection = colorSelection;
		this.sleepbaar = sleepbaar;
		this.sleepdoel = sleepdoel;
		this.sleepHandle = sleepHandle;
		this.ipId = ipId;
		this.interlinie = interlinie;
		this.cellSpaceColumn = cellSpaceColumn;
		this.cellSpaceRow = cellSpaceRow;
		this.zichtbaarNaNakijken = zichtbaarNaNakijken;
		this.balansVergCom = balansVergCom;
		this.aftrekPopup = aftrekPopup;
		this.puntenAftrekPopup = puntenAftrekPopup;
		this.callOut = callOut;
		this.vulHoogte = vulHoogte;
		this.inklapbaar = inklapbaar;
		this.checkUitklapVak = checkUitklapVak;	
		this.knopImageString1 = knopImageString1;
		this.knopImageString2 = knopImageString2;
		this.random = random;
		this.aantalRandom = aantalRandom;
		this.randomteksten = randomteksten;
		this.randomIpLaunchdata = randomIpLaunchdata;
		this.randomVar = randomVar;
		this.isLink = isLink;
		this.defaultBijNull = defaultBijNull;
		this.visible = visible;
		
		if(isLink)
			//link = new Link("", linkUrl, linkWidth, linkHeight);
			link = new Link("", linkUrls, linkWidth, linkHeight, false, grensScores);
		
		if(styleString!=null && TekstVakPanel.styles.containsKey(styleString))
			kiesStyleChoice.setSelectedItem(styleString);
		
		randZichtbaarCB.setSelected(randZichtbaar);
		randDikteTF.setVisible(randZichtbaar);
		randColorButton.setVisible(randZichtbaar);
		bgColorZichtbaarCB.setSelected(bgColorZichtbaar);
		zwevendCB.setSelected(zwevend);
		anderFontCB.setSelected(anderFont);
		//buttonCB.setSelected(buttonOptie);
		tableBordersCB.setSelected(tableBorders);
		fontButton.setVisible(anderFont);
		fgColorButton.setVisible(anderFont);
		centerHCB.setSelected(centerH);
		centerVCB.setSelected(centerV);
		pasAanHCB.setSelected(pasAanH);
		pasAanBCB.setSelected(pasAanB);
		selectableCB.setSelected(selectable);
		colorSelectionCB.setVisible(selectable);
		colorSelectionCB.setSelected(colorSelection);
		sleepbaarCB.setSelected(sleepbaar);
		sleepdoelCB.setSelected(sleepdoel);
		sleepHandleCB.setSelected(sleepHandle);
		zichtbaarNaNakijkenCB.setSelected(zichtbaarNaNakijken);
		balansVergComCB.setSelected(balansVergCom);
		aftrekPopupCB.setSelected(aftrekPopup);
		aftrekPopupTF.setVisible(aftrekPopup);
		aftrekPopupLabel.setVisible(aftrekPopup);
		callOutCB.setSelected(callOut);
		vulHoogteCB.setSelected(vulHoogte);
		inklapbaarCB.setSelected(inklapbaar);
		checkUitklapVakCB.setSelected(checkUitklapVak);
		posBeginRB.setSelected(inklapKnopPos==0);
		posBeginRB.setVisible(inklapbaar);
		posEindRB.setSelected(inklapKnopPos==1);
		posEindRB.setVisible(inklapbaar);
		posNaTekstRB.setSelected(inklapKnopPos==2);
		posNaTekstRB.setVisible(inklapbaar);
		knopImageButton1.setVisible(inklapbaar);
		knopImageButton2.setVisible(inklapbaar);
		checkUitklapVakCB.setVisible(inklapbaar);
		linkCB.setSelected(isLink);
		defaultBijNullCB.setSelected(defaultBijNull);
		linkButton.setVisible(isLink);
		
		logCB.setSelected(logOption);
        logIDField.setVisible(logOption);
        logIDLabelField.setVisible(logOption); 
    	logIDLabelLabel.setVisible(logOption); 
        //logObjectivesButton.setVisible(logOption);
        logIDField.setText(logID);
        logIDLabelField.setText(logIDLabel);
		
		randomCB.setSelected(random);
		randomTF.setVisible(random);
		randomTF.setText(randomVar);
		randomTab.setVisible(random);
		aantalTabsKnop.setVisible(random);
		tabPositieKnop.setVisible(random);
		
		if(random)
		{	remove(randomTab);
			randomTab = new OpdrachtNrRij(aantalRandom, 360,22);
			randomTab.setTab(true);
			randomTab.setScoresVisible(false);
			randomTab.setSize(randomTab.getSize().width, 23);
			randomTab.addActionListener(this);
			randomTab.setBackground(new Color(210,210,210));
			randomTab.setSelected(1);
			add(randomTab,0);
			aantalTabsKnop.setLocation(360+25*aantalRandom+5 ,22);
			
			randomNr = 0;
		}
		
		randDikteTF.setText(Integer.toString(randDikte));
		interactiePanelIdTF.setText(Integer.toString(ipId));
		cellMargeTF.setText(Integer.toString(cellMarge));
		bovenMargeTF.setText(Integer.toString(bovenMarge));
        rondingTF.setText(Integer.toString(ronding));
		hoekTF.setText(Integer.toString(hoek));
		interlinieTF.setText(Integer.toString(interlinie));
		cellSpaceColumnTF.setText(Integer.toString(cellSpaceColumn));
		cellSpaceRowTF.setText(Integer.toString(cellSpaceRow));
		aftrekPopupTF.setText(Integer.toString(puntenAftrekPopup));
		//scoreMaxTF.setText(Integer.toString(scoreMax));
		//scoreMaxTF.setVisible(selectable);
		//selectieWaardeLabel.setEnabled(selectable);
		checkExpressieFormuleVak.vulVak(checkExpressieString);
		//checkExpressieFormuleVak.setEnabled(selectable);
		sleepbaarCB.setEnabled(zwevend);
		sleepdoelCB.setEnabled(zwevend);
		sleepHandleCB.setVisible(sleepbaar);
		visibleCB.setSelected(visible);
		
		if(teksten!=null)
		{	aantalRijenTF.setText(Integer.toString(teksten.length));
			aantalKolommenTF.setText(Integer.toString(teksten[0].length));
			if(tableMode)tekstVakPanel.setEditState(h);
		}
		
		
		
		
		if(!tableMode)tekstEditor.setEditState(h);
		
		if(randZichtbaar)tekstVakPanel.setBorder(Color.gray);
		else tekstVakPanel.setBorder(Color.gray,0);
		if(bgColorZichtbaar) 
		{	tekstVakPanel.setBackground(bgColor);
			bgColorButton.setVisible(true);
		}
		tekstVakPanel.setForeground(fgColor);
		tekstVakPanel.setInterlinie(interlinie);
		
		knopImageButton1.setPopupButtonImage(knopImage1);
    	iconman1 = new Iconan(WiskOpdr.applet, (Component)this, (Hashtable)TekstImageVak.getImageMap());
    	if(knopImageString1!=null && !"".equals(knopImageString1)) {
    		knopImage1 = iconman1.getImage(knopImageString1);
    		knopImageButton1.setPopupButtonImage(knopImage1);
    	}
    	else {
    		knopImageButton1.setCode("\u25b8");
    	}
    	
    	knopImageButton2.setPopupButtonImage(knopImage2);
    	iconman2 = new Iconan(WiskOpdr.applet, (Component)this, (Hashtable)TekstImageVak.getImageMap());
    	if(knopImageString2!=null && !"".equals(knopImageString2)) {
    		knopImage2 = iconman2.getImage(knopImageString2);
    		knopImageButton2.setPopupButtonImage(knopImage2);
    	}
    	else {
    		knopImageButton2.setCode("\u25be");
    	}
	}
	
	
	
	public void zetBreedte(int b)
	{	
		if(tableMode)
		{	
			tekstVakPanel.zetBreedte(b);//setSize(b,tekstVakPanel.getSize().height);
			tekstVakPanel.setEditState(getEditState());
			
		}
		else tekstEditor.setSize(b,tekstEditor.getSize().height);
	}
	
	public int geefTekstVakPanelHoogte()
	{	return tekstVakPanel.getHeight();
	}
	
	public int geefTekstVakPanelBreedte()
	{	return tekstVakPanel.getWidth();
	}
	
	public boolean hoogteDwingend()
	{	return pasAanH;
	}
	
	public boolean breedteDwingend()
	{	return pasAanB;
	}
	
	public void zetHoogte(int h)
	{	
		if(tableMode)
		{	tekstVakPanel.zetHoogte(h);//setSize(tekstVakPanel.getSize().width, h);
			
		}
		else tekstEditor.setSize(tekstEditor.getSize().width, h+24);
	}
	
	public void setBounds(int x, int y, int b, int h)
	{	//cp.setLocation(b - cp.getWidth(),0);
		optionsPanel.setBounds(getWidth()-defaultOpWidth-10,20,defaultOpWidth, getHeight() - 30);
		tabbedPane.setSize(defaultOpWidth, getHeight() - 30);
		super.setBounds(x,y,b,h);
	}
	
	public void wis(){}
    
	public void zetMode(int mode){}
	
    public void stop(){}
    
    public void start(){}
	
    public void focusGained(FocusEvent e)
    {
    }
    
    public void focusLost(FocusEvent e)
    {
        if(e.getSource().equals(cellMargeTF))
        {   
            cellMarge = Integer.parseInt(cellMargeTF.getText());
            tekstVakPanel.setEditState(getEditState());
            
        }
        if(e.getSource().equals(bovenMargeTF))
        {   
            bovenMarge = Integer.parseInt(bovenMargeTF.getText());
            tekstVakPanel.setEditState(getEditState());
            
        }
        if(e.getSource().equals(cellSpaceColumnTF))
		{	
			cellSpaceColumn = Integer.parseInt(cellSpaceColumnTF.getText());
			tekstVakPanel.setEditState(getEditState());
			
		}
		if(e.getSource().equals(cellSpaceRowTF))
		{	
			cellSpaceRow = Integer.parseInt(cellSpaceRowTF.getText());
			tekstVakPanel.setEditState(getEditState());
			
		}
        if(e.getSource().equals(rondingTF))
        {   
            ronding = Integer.parseInt(rondingTF.getText());
            tekstVakPanel.setEditState(getEditState());
            
        }
        if(e.getSource().equals(hoekTF))
        {   
            hoek = Integer.parseInt(hoekTF.getText());
            tekstVakPanel.setEditState(getEditState());
            
        }
        if(e.getSource().equals(interactiePanelIdTF))
        {   
            ipId = Integer.parseInt(interactiePanelIdTF.getText());
            tekstVakPanel.setEditState(getEditState());
            
        }
        if(e.getSource().equals(randDikteTF))
        {   
        	randDikte = Integer.parseInt(randDikteTF.getText());
            tekstVakPanel.setEditState(getEditState());
            
        }
        if(e.getSource().equals(aftrekPopupTF))
        {   
        	puntenAftrekPopup = Integer.parseInt(aftrekPopupTF.getText());
            tekstVakPanel.setEditState(getEditState());
            
        }
        if(e.getSource().equals(randomTF))
		{	randomVar = randomTF.getText();
			tekstVakPanel.setEditState(getEditState());
		}
    }
    
	public void actionPerformed(ActionEvent e)
	{	
		if(e.getSource() == randomTab)
		{	int nr = Integer.parseInt(e.getActionCommand())-1;
			if(randomNr != nr) 
			{
				randomteksten[randomNr] = (String[][])tekstVakPanel.getEditState().get("teksten");
				randomIpLaunchdata[randomNr] = (Hashtable[])tekstVakPanel.getEditState().get("interactiePanelLaunchData");
				randomNr = nr;
				Hashtable h = (tekstVakPanel.getEditState());
				h.put("teksten",randomteksten[randomNr]);
				h.put("interactiePanelLaunchData",randomIpLaunchdata[randomNr]);
				tekstVakPanel.setEditState(h);
				tabPositieKnop.setLocation(356+25*randomNr+5 ,2);
			}
			
		}
		else if(e.getSource() == tabPositieKnop)
		{	if(e.getActionCommand().equals("plus") && randomNr<aantalRandom-1) 
			{	String[][] resTeksten = null;
				resTeksten = randomteksten[randomNr];
				randomteksten[randomNr] = randomteksten[randomNr+1];
				randomteksten[randomNr+1] = resTeksten;
				Hashtable[] resLaunchdata = null;
				resLaunchdata = randomIpLaunchdata[randomNr];
				randomIpLaunchdata[randomNr] = randomIpLaunchdata[randomNr+1];
				randomIpLaunchdata[randomNr+1] = resLaunchdata;
				randomNr++;
				randomTab.setSelected(randomNr+1);
				tabPositieKnop.setLocation(356+25*randomNr+5 ,2);
			}
			if(e.getActionCommand().equals("min") && randomNr>0) 
			{	String[][] resTeksten = null;
				randomteksten[randomNr] = randomteksten[randomNr-1];
				randomteksten[randomNr-1] = resTeksten;
				Hashtable[] resLaunchdata = null;
				randomIpLaunchdata[randomNr] = randomIpLaunchdata[randomNr-1];
				randomIpLaunchdata[randomNr-1] = resLaunchdata;
				randomNr--;
				randomTab.setSelected(randomNr+1);
				tabPositieKnop.setLocation(356+25*randomNr+5 ,2);
			}
			
		}
		else if(e.getSource() == aantalTabsKnop)
		{	if(e.getActionCommand().equals("min") && aantalRandom>1)
			{	//remove(opdrContainers[activiteitNr][aantalOpdrachten[activiteitNr]-1]);
				//opdrContainers[activiteitNr][aantalOpdrachten[activiteitNr]-1] = null;
				aantalRandom--;
				if(randomNr>aantalRandom-1) randomNr--;
				Hashtable h = (tekstVakPanel.getEditState());
				h.put("teksten",randomteksten[randomNr]);
				h.put("interactiePanelLaunchData",randomIpLaunchdata[randomNr]);
				tekstVakPanel.setEditState(h);
				
				aantalTabsKnop.setLocation(360+25*aantalRandom+5 ,22);
				remove(randomTab);
				randomTab = new OpdrachtNrRij(aantalRandom, 360,22);
				randomTab.setTab(true);
				randomTab.setScoresVisible(false);
				randomTab.setSize(randomTab.getSize().width, 23);
				randomTab.addActionListener(this);
				randomTab.setBackground(new Color(210,210,210));
				randomTab.setSelected(randomNr+1);
				add(randomTab,0);
				String[][][]  randomTekstenNew = new String[aantalRandom][][];
				for(int i=0 ; i<aantalRandom ; i++)
				{	randomTekstenNew[i] = randomteksten[i];
				}
				randomteksten = randomTekstenNew;
				Hashtable[][]  randomIpLaunchdataNew = new Hashtable[aantalRandom][];
				for(int i=0 ; i<aantalRandom ; i++)
				{	randomIpLaunchdataNew[i] = randomIpLaunchdata[i];
				}
				randomIpLaunchdata = randomIpLaunchdataNew;
				repaint();
				
			}
			if(e.getActionCommand().equals("plus") && aantalRandom<20)
			{	aantalRandom++;
				aantalTabsKnop.setLocation(360+25*aantalRandom+5 ,22);
				remove(randomTab);
				randomTab = new OpdrachtNrRij(aantalRandom, 360,22);
				randomTab.setTab(true);
				randomTab.setScoresVisible(false);
				randomTab.setSize(randomTab.getSize().width, 23);
				randomTab.addActionListener(this);
				randomTab.setBackground(new Color(210,210,210));
				randomTab.setSelected(randomNr+1);
				add(randomTab,0);
				String[][][] randomTekstenNew = new String[aantalRandom][][];
				for(int i=0 ; i<aantalRandom-1 ; i++)
				{	randomTekstenNew[i] = randomteksten[i];
				}
				randomTekstenNew[aantalRandom-1] = randomteksten[aantalRandom-2];
				randomteksten = randomTekstenNew;
				Hashtable[][]  randomIpLaunchdataNew = new Hashtable[aantalRandom][];
				for(int i=0 ; i<aantalRandom-1 ; i++)
				{	randomIpLaunchdataNew[i] = randomIpLaunchdata[i];
				}
				randomIpLaunchdataNew[aantalRandom-1] = randomIpLaunchdata[aantalRandom-2];
				randomIpLaunchdata = randomIpLaunchdataNew;
				repaint();
			}
		}
		if(e.getSource().equals(randomCB))
		{	random = randomCB.isSelected();
			randomTF.setVisible(random);
			randomTab.setVisible(random);
			aantalTabsKnop.setVisible(random);
			tabPositieKnop.setVisible(random);
			if(random)
			{	aantalRandom = 1;
				randomteksten[0] = (String[][])tekstVakPanel.getEditState().get("teksten");
				randomIpLaunchdata[0] = (Hashtable[])tekstVakPanel.getEditState().get("interactiePanelLaunchData");
				randomTF.setVisible(random);
			}
			else
			{	aantalRandom = 0;
				Hashtable h = (tekstVakPanel.getEditState());
				h.put("teksten",randomteksten[0]);
				h.put("interactiePanelLaunchData",randomIpLaunchdata[0]);
				tekstVakPanel.setEditState(h);
			}
			tekstVakPanel.setEditState(getEditState());
		}
		
		if(e.getSource().equals(randZichtbaarCB))
		{	randZichtbaar = randZichtbaarCB.isSelected();
			tekstVakPanel.setEditState(getEditState());
			randDikteTF.setVisible(randZichtbaar);
			randColorButton.setVisible(randZichtbaar);
		}
		if(e.getSource().equals(bgColorZichtbaarCB))
		{	bgColorZichtbaar = bgColorZichtbaarCB.isSelected();
			tekstVakPanel.setEditState(getEditState());
			bgColorButton.setVisible(bgColorZichtbaar);
		}
		if(e.getSource().equals(zwevendCB))
		{	zwevend = zwevendCB.isSelected();
			sleepbaarCB.setEnabled(zwevend);
			sleepdoelCB.setEnabled(zwevend);
			if(!zwevend) 
			{	sleepbaarCB.setSelected(false);
				sleepdoelCB.setSelected(false);
				sleepbaar = false;
				sleepdoel = false;
			}
			//checkExpressieFormuleVak.setEnabled(selectable || sleepbaar);
			//selectieWaardeLabel.setEnabled(selectable || sleepbaar);
			
			tekstVakPanel.setEditState(getEditState());
		}
		if(e.getSource().equals(anderFontCB))
		{	anderFont = anderFontCB.isSelected();
			
			//if(anderFont) font = new Font("SansSerif", Font.BOLD, 14);
			//else 
			font = WiskOpdr.tekstFont;
			tekstVakPanel.setFont(font);
			tekstVakPanel.setEditState(getEditState());
			fontButton.setVisible(anderFont);
			fgColorButton.setVisible(anderFont);
			if(!anderFont)
				fgColor = new Color(0, 0, 0);
		}
		if(e.getSource().equals(buttonCB))
		{	buttonOptie = buttonCB.isSelected();
			
		}
		if(e.getSource().equals(tableBordersCB))
		{	tableBorders = tableBordersCB.isSelected();
			tekstVakPanel.setEditState(getEditState());
			repaint();
		}
		if(e.getSource().equals(centerHCB))
		{	centerH = centerHCB.isSelected();
			tekstVakPanel.setEditState(getEditState());
			repaint();
		}
		if(e.getSource().equals(centerVCB))
		{	centerV = centerVCB.isSelected();
			tekstVakPanel.setEditState(getEditState());
			repaint();
		}
		if(e.getSource().equals(pasAanHCB))
		{	pasAanH = pasAanHCB.isSelected();
			tekstVakPanel.setEditState(getEditState());
			produceAction("pasMaatAan");
		//	repaint();
		}
		if(e.getSource().equals(pasAanBCB))
		{	pasAanB = pasAanBCB.isSelected();
			tekstVakPanel.setEditState(getEditState());
			produceAction("pasMaatAan");
		//	repaint();
		}
		if(e.getSource().equals(selectableCB))
		{	selectable = selectableCB.isSelected();
			colorSelectionCB.setVisible(selectable);
			tekstVakPanel.setEditState(getEditState());
			if(!zwevend || selectable) 
			{	sleepbaarCB.setSelected(false);
				sleepdoelCB.setSelected(false);
				sleepbaar = false;
				sleepdoel = false;
			}
			repaint();
		}
		if(e.getSource().equals(colorSelectionCB))
		{	colorSelection = colorSelectionCB.isSelected();
		}
		if(e.getSource().equals(sleepbaarCB))
		{	sleepbaar = sleepbaarCB.isSelected();
			sleepHandleCB.setVisible(sleepbaar);
			if(!sleepbaar)sleepHandleCB.setSelected(sleepbaar);
			if(sleepbaar)
			{	selectableCB.setSelected(false);
				colorSelectionCB.setVisible(false);
				sleepdoelCB.setSelected(false);
				selectable = false; 
				sleepdoel = false; 
			}
			tekstVakPanel.setEditState(getEditState());
			repaint();
		}
		if(e.getSource().equals(sleepHandleCB))
		{	sleepHandle = sleepHandleCB.isSelected();
			tekstVakPanel.setEditState(getEditState());
			repaint();
		}
		if(e.getSource().equals(linkCB))
		{	isLink = linkCB.isSelected();
			linkButton.setVisible(isLink);
			tekstVakPanel.setEditState(getEditState());
			repaint();
		}
		if(e.getSource().equals(zichtbaarNaNakijkenCB))
		{	zichtbaarNaNakijken = zichtbaarNaNakijkenCB.isSelected();
			tekstVakPanel.setEditState(getEditState());
			repaint();
		}
		if(e.getSource().equals(balansVergComCB))
		{	balansVergCom = balansVergComCB.isSelected();
			tekstVakPanel.setEditState(getEditState());
			repaint();
		}
		if(e.getSource().equals(defaultBijNullCB))
		{	defaultBijNull = defaultBijNullCB.isSelected();
			tekstVakPanel.setEditState(getEditState());
		}
		if(e.getSource().equals(aftrekPopupCB))
		{	aftrekPopup = aftrekPopupCB.isSelected();
			aftrekPopupTF.setVisible(aftrekPopup);
			aftrekPopupLabel.setVisible(aftrekPopup);
			tekstVakPanel.setEditState(getEditState());
			repaint();
		}
		if(e.getSource().equals(callOutCB))
		{	callOut = callOutCB.isSelected();
			tekstVakPanel.setEditState(getEditState());
			repaint();
		}
		if(e.getSource().equals(vulHoogteCB))
		{	vulHoogte = vulHoogteCB.isSelected();
			tekstVakPanel.setEditState(getEditState());
			repaint();
		}
		if(e.getSource().equals(inklapbaarCB))
		{	inklapbaar = inklapbaarCB.isSelected();
			tekstVakPanel.setEditState(getEditState());
			tekstVakPanel.klapUitAction();
			knopImageButton1.setVisible(inklapbaar);
			knopImageButton2.setVisible(inklapbaar);
			posBeginRB.setVisible(inklapbaar);
			posEindRB.setVisible(inklapbaar);
			posNaTekstRB.setVisible(inklapbaar);
			checkUitklapVakCB.setVisible(inklapbaar);
			
			repaint();
		}
		if(e.getSource().equals(posBeginRB) || e.getSource().equals(posEindRB) || e.getSource().equals(posNaTekstRB))
		{	tekstVakPanel.setEditState(getEditState());
		}
		if(e.getSource().equals(checkUitklapVakCB))
		{	checkUitklapVak = checkUitklapVakCB.isSelected();
			tekstVakPanel.setEditState(getEditState());
		}
		if(e.getSource().equals(sleepdoelCB))
		{	sleepdoel = sleepdoelCB.isSelected();
			if(sleepdoel) 
			{	selectableCB.setSelected(false);
				sleepbaarCB.setSelected(false);
				selectable = false; 
				sleepbaar = false;
			}
			tekstVakPanel.setEditState(getEditState());
			repaint();
		}
		if(e.getSource().equals(cellMargeTF))
		{	
			cellMarge = Integer.parseInt(cellMargeTF.getText());
			tekstVakPanel.setEditState(getEditState());
			
		}
		if(e.getSource().equals(cellSpaceColumnTF))
		{	
			cellSpaceColumn = Integer.parseInt(cellSpaceColumnTF.getText());
			tekstVakPanel.setEditState(getEditState());
			
		}
		if(e.getSource().equals(cellSpaceRowTF))
		{	
			cellSpaceRow = Integer.parseInt(cellSpaceRowTF.getText());
			tekstVakPanel.setEditState(getEditState());
			
		}
		if(e.getSource().equals(bovenMargeTF))
        {   
            bovenMarge = Integer.parseInt(bovenMargeTF.getText());
            tekstVakPanel.setEditState(getEditState());
            
        }
		if(e.getSource().equals(interlinieTF))
        {   
			interlinie = Integer.parseInt(interlinieTF.getText());
            tekstVakPanel.setEditState(getEditState());
            
        }
		if(e.getSource().equals(randDikteTF))
        {   
        	randDikte = Integer.parseInt(randDikteTF.getText());
            tekstVakPanel.setEditState(getEditState());
            
        }
		if(e.getSource().equals(kolommenPlusMin))
		{	
			int aantalKolommen = Integer.parseInt(aantalKolommenTF.getText());
			if(e.getActionCommand().equals("plus"))aantalKolommen++;
			else aantalKolommen--;
			aantalKolommenTF.setText(Integer.toString(aantalKolommen));
			changeRowColum();
			
		}
		if(e.getSource().equals(rijenPlusMin))
		{	int aantalRijen = Integer.parseInt(aantalRijenTF.getText());
			if(e.getActionCommand().equals("plus"))aantalRijen++;
			else aantalRijen--;
			aantalRijenTF.setText(Integer.toString(aantalRijen));
			changeRowColum();
		}
		if(e.getSource().equals(rondingTF))
		{	
			ronding = Integer.parseInt(rondingTF.getText());
			tekstVakPanel.setEditState(getEditState());
			
		}
		if(e.getSource().equals(hoekTF))
		{	
			hoek = Integer.parseInt(hoekTF.getText());
			tekstVakPanel.setEditState(getEditState());
			
		}
		if(e.getSource().equals(interactiePanelIdTF))
		{	
			ipId = Integer.parseInt(interactiePanelIdTF.getText());
			tekstVakPanel.setEditState(getEditState());
			
		}
		if(e.getSource().equals(aftrekPopupTF))
        {   
        	puntenAftrekPopup = Integer.parseInt(aftrekPopupTF.getText());
            tekstVakPanel.setEditState(getEditState());
            
        }
		if(e.getSource().equals(rondingPlusMin))
		{	ronding = Integer.parseInt(rondingTF.getText());
			if(e.getActionCommand().equals("plus"))ronding++;
			else ronding--;
			rondingTF.setText(Integer.toString(ronding));
			tekstVakPanel.setEditState(getEditState());
		}
		if(e.getSource().equals(fontButton))
		{	//font = tekstVakPanel.getFont();
			Font f = FontChooser.makeFont(WiskOpdr.getFrame(),font);
			if(f !=null)
			{	font = new Font(f.getName(),f.getStyle(),f.getSize());
				tekstVakPanel.setFont(font);
			}
			Hashtable map = new Hashtable();
		}
		if(e.getSource().equals(bgColorButton))
		{	bgColor = JColorChooser.showDialog(this, "Kies kleur", bgColor);//new Color(255,255,180));
			tekstVakPanel.setEditState(getEditState());
		}
		if(e.getSource().equals(fgColorButton))
		{	fgColor = JColorChooser.showDialog(this, "Kies kleur", fgColor);
			if(fgColor==null) fgColor = Color.black;
			tekstVakPanel.setEditState(getEditState());
		}
		if(e.getSource().equals(randColorButton))
		{	randColor = JColorChooser.showDialog(this, "Kies kleur", randColor);
			if(randColor==null) randColor = Color.gray;
			tekstVakPanel.setEditState(getEditState());
		}
		if(e.getSource().equals(randomTF))
		{	randomVar = randomTF.getText();
			tekstVakPanel.setEditState(getEditState());
		}
		
		if(e.getSource().equals(linkButton))
		{	Link newLink = AddLinkDialog.editLink(this,link);
	        if(newLink!=null)link = newLink;
		}
		
		if(e.getSource()==knopImageButton1)
	    {   editImage1();
	            
	    }
		
		if(e.getSource()==knopImageButton2)
	    {   editImage2();
	            
	    }
		
	    if(e.getSource()==iconman1)
	    {
	    	String name = e.getActionCommand();
	        if(!"".equals(name))
	        {
	        	knopImageString1 = name;
	            this.knopImage1 = iconman1.getImage(name);
	            knopImageButton1.setPopupButtonImage(knopImage1);
	            knopImageButton1.setCode(" ");
	            //int imWidth = iconman1.getWidth(knopImageString1);
				//int imHeight = iconman1.getHeight(knopImageString1);
				//if(imWidth == -1) imWidth = 20;
				//if(imHeight == -1) imHeight = 20;
				//knopImageButton1.setSize(imWidth,imHeight);
	        } else {
	        	knopImageString1 = "";
	        	knopImage1 = null;
	        	knopImageButton1.setPopupButtonImage(null);
	        	knopImageButton1.setCode("\u25b8");
	        }
            repaint();
            imageDialog1.setVisible(false);
            WiskOpdr.setLaunchDataChanged();
	    }
	    
	    if(e.getSource()==iconman2)
	    {
	    	String name = e.getActionCommand();
	        if(!"".equals(name))
	        {
	        	knopImageString2 = name;
	            this.knopImage2 = iconman2.getImage(name);
	            knopImageButton2.setPopupButtonImage(knopImage2);
	            knopImageButton2.setCode(" ");
	            //int imWidth = iconman2.getWidth(knopImageString2);
				//int imHeight = iconman2.getHeight(knopImageString2);
				//if(imWidth == -1) imWidth = 20;
				//if(imHeight == -1) imHeight = 20;
				//knopImageButton2.setSize(imWidth,imHeight);
	        } else {
	        	knopImageString2 = "";
	        	knopImage2 = null;
	        	knopImageButton2.setPopupButtonImage(null);
	        	knopImageButton2.setCode("\u25be");
	        }
            repaint();
            imageDialog2.setVisible(false);
            WiskOpdr.setLaunchDataChanged();
	    }
	    
	    if(e.getSource()==logCB)
	    {   logIDField.setVisible(logCB.isSelected());   
	    	logIDLabelField.setVisible(logCB.isSelected()); 
	    	logIDLabelLabel.setVisible(logCB.isSelected()); 
	    }
	    if(e.getSource().equals(visibleCB))
		{	visible = visibleCB.isSelected();
			repaint();
		}
	    
	    if(e.getSource().equals(editStylesButton))
		{	//styles = stylesCB.isSelected();
			//kiesStyleChoice.setSelectedIndex(0);
			//kiesStyleChoice.setVisible(stylesCB.isSelected()); 
			styleManager.setVisible(true);
			styleSettingsLabel.setVisible(true);
			setStyleManageMode(true);
			//addStyleButton.setVisible(stylesCB.isSelected());
			editStylesButton.setVisible(false);
			repaint();
		}
	   
	    if(e.getSource().equals(kiesStyleChoice))
		{	if(kiesStyleChoice.getSelectedIndex()==0)
			{	enableStyleSettings(true);
			}
			else
			{	
				setEditStyle();
				enableStyleSettings(styleManager.isVisible());
				tekstVakPanel.setEditState(getEditState());
			}
			
	    	repaint();
		}
	    if(e.getSource().equals(styleManager))
		{	styleManager.setVisible(false);
	    	kiesStyleChoice.setBounds(10,4,190,20);
			layoutOptionsPanel.add(kiesStyleChoice);
			editStylesButton.setVisible(true);
			styleSettingsLabel.setVisible(false);
			setStyleManageMode(false);
		}

	}
	
	private void setStyleAction(Map h)
	{
		if (h.containsKey("randZichtbaar"))
			randZichtbaar = ((Boolean) h.get("randZichtbaar")).booleanValue();
		if (h.containsKey("bgColorZichtbaar"))
			bgColorZichtbaar = ((Boolean) h.get("bgColorZichtbaar")).booleanValue();
		if (h.containsKey("anderFont"))
			anderFont = ((Boolean) h.get("anderFont")).booleanValue();
		if (h.containsKey("tableBorders"))
			tableBorders = ((Boolean) h.get("tableBorders")).booleanValue();
		if (h.containsKey("cellMarge"))
			cellMarge = ((Integer) h.get("cellMarge")).intValue();
		if (h.containsKey("bovenMarge"))
			bovenMarge = ((Integer) h.get("bovenMarge")).intValue();
		if (h.containsKey("ronding"))
			ronding = ((Integer) h.get("ronding")).intValue();
		if (anderFont)
			font = new Font("SansSerif", Font.BOLD, 14);
		if (h.containsKey("font"))
			font = (Font) h.get("font");
		if (bgColorZichtbaar)
			bgColor = new Color(255, 255, 180);
		if (h.containsKey("bgColor"))
			bgColor = (Color) h.get("bgColor");
		if (h.containsKey("fgColor"))
			fgColor = (Color) h.get("fgColor");
		if (h.containsKey("randColor"))
			randColor = (Color) h.get("randColor");
		if (h.containsKey("hoek"))
			hoek = ((Integer) h.get("hoek")).intValue();
		if (h.containsKey("centerH"))
			centerH = ((Boolean) h.get("centerH")).booleanValue();
		if (h.containsKey("centerV"))
			centerV = ((Boolean) h.get("centerV")).booleanValue();
		if (h.containsKey("pasAanH"))
			pasAanH = ((Boolean) h.get("pasAanH")).booleanValue();
		if (h.containsKey("pasAanB"))
			pasAanB = ((Boolean) h.get("pasAanB")).booleanValue();
	}
	
	Hashtable getStyleSettings()
	{
		Hashtable h = new Hashtable();
		h.put("randZichtbaar", new Boolean(randZichtbaar));
		h.put("bgColorZichtbaar", new Boolean(bgColorZichtbaar));
		if(bgColor!=null)h.put("bgColor", bgColor);
		h.put("fgColor", fgColor);
		h.put("randColor", randColor);
		//h.put("zwevend", new Boolean(zwevend));
		h.put("anderFont", new Boolean(anderFont));
		h.put("tableBorders", new Boolean(tableBorders));
		h.put("cellMarge",new Integer(cellMarge));
		h.put("bovenMarge",new Integer(bovenMarge));
		h.put("ronding",new Integer(ronding));
		h.put("hoek",new Integer(hoek));
		h.put("centerH", new Boolean(centerH));
		h.put("centerV", new Boolean(centerV));
		h.put("pasAanH", new Boolean(pasAanH));
		h.put("pasAanB", new Boolean(pasAanB));
		h.put("interlinie",new Integer(interlinie));
		h.put("cellSpaceColumn",new Integer(cellSpaceColumn));
		h.put("cellSpaceRow",new Integer(cellSpaceRow));
		h.put("randDikte",new Integer(randDikte));
		
		Font f = tekstVakPanel.getFont();
		Font font = WiskOpdr.tekstFont;
		if (f != null)
			font = new Font(f.getFontName(), f.getStyle(), f.getSize());
		
		h.put("font", font);
		
		return h;
	}
	
	private void removeStyleAction()
	{
		TekstVakPanel.styles.remove((String)kiesStyleChoice.getSelectedItem());
		kiesStyleChoice.setSelectedIndex(0);
	}
	
	void setStyleManageMode(boolean b)
	{
		enableStyleSettings(b || kiesStyleChoice.getSelectedIndex()==0);
		
		zwevendCB.setVisible(!b);
		aantalRijenTF.setVisible(!b);
		aantalKolommenTF.setVisible(!b);
		rijenPlusMin.setVisible(!b);
		kolommenPlusMin.setVisible(!b);
		vulHoogteCB.setVisible(!b);
		callOutCB.setVisible(!b);
		inklapbaarCB.setVisible(!b);
		visibleCB.setVisible(!b);
		
		knopImageButton1.setVisible(!b && inklapbaarCB.isSelected());
		knopImageButton2.setVisible(!b && inklapbaarCB.isSelected());
		posBeginRB.setVisible(!b && inklapbaarCB.isSelected());
		posEindRB.setVisible(!b && inklapbaarCB.isSelected());
		posNaTekstRB.setVisible(!b && inklapbaarCB.isSelected());
		checkUitklapVakCB.setVisible(!b && inklapbaarCB.isSelected());
	}
	void enableStyleSettings(boolean b)
	{
		randZichtbaarCB.setEnabled(b);
		bgColorZichtbaarCB.setEnabled(b); 
		//zwevendCB.setEnabled(b);
		anderFontCB.setEnabled(b);
		//buttonCB.setEnabled(b);
		tableBordersCB.setEnabled(b);
		centerHCB.setEnabled(b);
		centerVCB.setEnabled(b);
		pasAanHCB.setEnabled(b);
		pasAanBCB.setEnabled(b);
		
		randColorButton.setEnabled(b);
		fontButton.setEnabled(b);
		fgColorButton.setEnabled(b);
		bgColorButton.setEnabled(b);
		
		cellMargeLabel.setEnabled(b);
		bovenMargeLabel.setEnabled(b);
		rondingLabel.setEnabled(b);
		hoekLabel.setEnabled(b);
		kopLayoutLabel.setEnabled(b);
		kopFunctieLabel.setEnabled(b);
		interlinieLabel.setEnabled(b);
		cellSpaceColumnLabel.setEnabled(b);
		cellSpaceRowLabel.setEnabled(b);
		
		//aantalRijenTF.setEnabled(b);
		//aantalKolommenTF.setEnabled(b);
		//rijenPlusMin.setEnabled(b);
		//kolommenPlusMin.setEnabled(b);
		cellMargeTF.setEnabled(b);
		bovenMargeTF.setEnabled(b);
		rondingTF.setEnabled(b);
		rondingPlusMin.setEnabled(b);
		hoekTF.setEnabled(b);
		interlinieTF.setEnabled(b);
		cellSpaceColumnTF.setEnabled(b);
		cellSpaceRowTF.setEnabled(b);
		randDikteTF.setEnabled(b);
	}
	
	public void editImage1() {
        if(imageDialog1 == null)
        {
        	
        	Frame f = JOptionPane.getFrameForComponent(this);
			imageDialog1 = new JDialog(f,"title", true);
			iconman1 = new Iconan(WiskOpdr.applet, (Component)this, (Hashtable)TekstImageVak.getImageMap());
            imageDialog1.setContentPane(iconman1);
            imageDialog1.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
            imageDialog1.pack();
            iconman1.addActionListener(this);
        }
        iconman1.select(knopImageString1);
        imageDialog1.setVisible(true);
    }
	
	public void editImage2() {
        if(imageDialog2 == null)
        {
        	
        	Frame f = JOptionPane.getFrameForComponent(this);
			imageDialog2 = new JDialog(f,"title", true);
			iconman2 = new Iconan(WiskOpdr.applet, (Component)this, (Hashtable)TekstImageVak.getImageMap());
            imageDialog2.setContentPane(iconman2);
            imageDialog2.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
            imageDialog2.pack();
            iconman2.addActionListener(this);
        }
        iconman2.select(knopImageString2);
        imageDialog2.setVisible(true);
    }
	
	public void changeRowColum()//boolean rowColum, boolean addRemove)
	{	String[][] tekstenOud = (String[][])tekstVakPanel.getEditState().get("teksten");
		Font fontOud = tekstVakPanel.getFont();
		double[] breedtesOud = (double[])tekstVakPanel.getEditState().get("breedtes");
		double[] hoogtesOud = (double[])tekstVakPanel.getEditState().get("hoogtes");
		Hashtable[] interactiePanelLaunchDataOud = (Hashtable[])tekstVakPanel.getEditState().get("interactiePanelLaunchData");
		int width = tekstVakPanel.getSize().width;
		int height = tekstVakPanel.getSize().height;
		remove(tekstVakPanel);
		tekstVakPanel.removeActionListener(tekstEditor);
		tekstVakPanel = new TekstVakPanel(Integer.parseInt(aantalRijenTF.getText()),Integer.parseInt(aantalKolommenTF.getText()));
		tekstVakPanel.setBounds(10,50,width,height);
		tekstVakPanel.setEditable(true);
		tekstVakPanel.setFont(fontOud);
		tekstVakPanel.addActionListener(tekstEditor);
		add(tekstVakPanel);
		
		String[][] tekstenNieuw = (String[][])tekstVakPanel.getEditState().get("teksten");
		int iMax = Math.min(tekstenNieuw.length, tekstenOud.length);
		int jMax = Math.min(tekstenNieuw[0].length, tekstenOud[0].length);
		for(int i=0 ; i<iMax ; i++)
		{	for(int j=0 ; j<jMax ; j++)
			{	tekstenNieuw[i][j] = tekstenOud[i][j];
			}
		}
		
		double[] breedtesNieuw = (double[])tekstVakPanel.getEditState().get("breedtes");
		double[] hoogtesNieuw = (double[])tekstVakPanel.getEditState().get("hoogtes");
		int breedtesMax = Math.min(breedtesNieuw.length, breedtesOud.length);
		int hoogtesMax = Math.min(hoogtesNieuw.length, hoogtesOud.length);
		for(int i=0 ; i<breedtesMax ; i++)
		{	if(breedtesNieuw.length <= breedtesMax)	breedtesNieuw[i] = breedtesOud[i];
		}
		if(breedtesNieuw.length > breedtesMax)
		{	
		}
		for(int i=0 ; i<hoogtesMax ; i++)
		{	if(hoogtesNieuw.length<=hoogtesMax) hoogtesNieuw[i] = hoogtesOud[i];
		}
		if(hoogtesNieuw.length > hoogtesMax)
		{
		}
		tekstVakPanel.zetBreedtes(breedtesNieuw);
		tekstVakPanel.zetHoogtes(hoogtesNieuw);
		
		//if(breedtesNieuw.length==breedtesOud.length)tekstVakPanel.zetBreedtes(breedtesNieuw);
		//if(hoogtesNieuw.length==hoogtesOud.length)tekstVakPanel.zetHoogtes(hoogtesNieuw);
		tekstVakPanel.zetTeksten(tekstenNieuw);
		Hashtable editState = getEditState();
		editState.put("interactiePanelLaunchData", interactiePanelLaunchDataOud);
		tekstVakPanel.setEditState(editState);
		tekstVakPanel.zetMaat();
		/**/
		repaint();
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
