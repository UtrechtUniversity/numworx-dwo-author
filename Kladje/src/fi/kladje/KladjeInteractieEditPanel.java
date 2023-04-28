package fi.kladje;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.*;

import fi.beans.numworxlf.JCheckBox;
import fi.beans.numworxlf.JRadioButton;
import fi.beans.numworxlf.JTextField;
import fi.beans.wiskopdrbeans.*;

import fi.wiskopdr.tekstobjects.EditInteractiePanelDialog;

import fi.wiskopdr.ObjectiveChoiceButton;



public class KladjeInteractieEditPanel extends JPanel implements InteractieEditPanel,	ActionListener {	
	
	// Algemene attributen 
    private Font font = new Font("SansSerif",Font.PLAIN,12);//WiskOpdr.tekstFont;
    private Font theFont = new Font("Dialog", Font.PLAIN, 12);
    private FontMetrics theFM = getFontMetrics(theFont);
    private Font theBoldFont = new Font("Dialog", Font.BOLD, 12);
    private FontMetrics theBoldFM = getFontMetrics(theBoldFont);
    
	// Basis GUI
	private JPanel mainPanel;
	private JPanel leftPanel;
	private JPanel instellingenPanel;
	private KladjeInteractiePanel klip;
	
	// Logging/Nakijken
  	private Box settingsBox;
 	private JLabel titleLoggingLabel;
    private JCheckBox checkCB;
    private Component checkCBRA = ra(0,5);
    private Component objectiveCBRA = ra(0,5);
    private JLabel maxScoreLabel;
    private JTextField maxScoreField;
    private JCheckBox logCB;
 	private JTextField logIDField;
	 	
 	// Settings
 	private JLabel titleSettingsLabel;
 	private JCheckBox kleurkeuzeBox;
 	private ButtonGroup achtergrondGroep;
 	private JCheckBox lijnTekenenBox, rechthoekTekenenBox, cirkelTekenenBox, tekstTekenenBox;
 	private JCheckBox formuleOptieBox;
 	private JCheckBox ivmOptieBox;
 	private JCheckBox roterenBox, schalenBox;
 	private FormuleInstellingenButton fiButton;
 	
 	private JLabel translationLabel;
 	private JTextField translationXTF, translationYTF;
 	private JLabel scaleLabel;
 	private JTextField scaleTF;
 	
 	// Layout
   	private JLabel titleLayoutLabel;
   	private JRadioButton blancoButton, lijnenButton, ruitjes20Button, ruitjes40Button, ruitjes80Button; 
   	private JCheckBox toolbarOnTopCB;
	

	int editWidth = 250;
	int editHeight = 500; 
	int klipBreedte = 500; // startbreedte spip
	int klipHoogte = 450; // starthoogte spip
	
	
	
	int offset = 10;
	boolean componentsCreated = false;
	boolean noSetBounds = false;	
	int scoreMax = 0;
	

	private JLabel scoreLabel;
	private JTextField scoreTF;
	private ObjectiveChoiceButton objectiveBtn;
	

	private boolean check;
	
	public KladjeInteractieEditPanel(KladjeInteractiePanel klip)	{
		this.klip = klip;
		klip.setPreferredSize(new Dimension(500,450));
		makeGUI();
	}
	
	public void makeGUI() {
		// Main
   		mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBackground(Kladje.colorGray3);
		
		// Panels links/rechts
		leftPanel = new JPanel(new BorderLayout());
		leftPanel.add(klip);
		
		instellingenPanel = new JPanel(null);
		instellingenPanel.setPreferredSize(new Dimension(200,451));
		instellingenPanel.setMaximumSize(new Dimension(200,451));
		instellingenPanel.setLayout(new BorderLayout());
   		
		// Logging/Nakijken
 		titleLoggingLabel = new JLabel(Kladje.rb.getString("titleLoggingLabel"));
     	titleLoggingLabel.setForeground(Kladje.colorBlue1);
     	titleLoggingLabel.setFont(font.deriveFont(Font.BOLD, 16));
     	
     	checkCB = new JCheckBox(Kladje.rb.getString("checkCB"));
     	checkCB.addActionListener(this);
     	checkCBRA.setVisible(false);
     	
     	maxScoreLabel = new JLabel(Kladje.rb.getString("maxScoreLabel"));
     	maxScoreLabel.setForeground(Kladje.colorBlue1);
     	maxScoreLabel.setFont(font);
     	maxScoreLabel.setVisible(false);
     	
     	maxScoreField = new JTextField("0");
     	maxScoreField.setFont(font);
     	maxScoreField.setPreferredSize(new Dimension(50,22));
     	maxScoreField.setMaximumSize(new Dimension(50,22));
     	maxScoreField.setVisible(false);
     	
     	objectiveBtn = new ObjectiveChoiceButton();
		objectiveBtn.setVisible(false);
		objectiveCBRA.setVisible(false);
		
     	logCB = new JCheckBox(Kladje.rb.getString("logCB"));
     	logCB.addActionListener(this);
     	
        logIDField = new JTextField("0");
        logIDField.setPreferredSize(new Dimension(50,22));
        logIDField.setMaximumSize(new Dimension(50,22));
        logIDField.setFont(font);
        
        logIDField.setVisible(false);
        
        // Instellingen
        titleSettingsLabel = new JLabel(Kladje.rb.getString("titleSettingsLabel"));
        titleSettingsLabel.setForeground(Kladje.colorBlue1);
        titleSettingsLabel.setFont(font.deriveFont(Font.BOLD, 16));
        
        kleurkeuzeBox = new JCheckBox(Kladje.rb.getString("kleurkeuzeTekst"), true);
        kleurkeuzeBox.setSelected(true);
		kleurkeuzeBox.addActionListener(this);
		

		lijnTekenenBox = new JCheckBox(Kladje.rb.getString("lijnTekenenTekst"), true);
		lijnTekenenBox.addActionListener(this);
		
		rechthoekTekenenBox = new JCheckBox(Kladje.rb.getString("rechthoekTekenenTekst"), true);
		rechthoekTekenenBox.addActionListener(this);
		
		cirkelTekenenBox = new JCheckBox(Kladje.rb.getString("cirkelTekenenTekst"), true);
		cirkelTekenenBox.addActionListener(this);
		
		tekstTekenenBox = new JCheckBox(Kladje.rb.getString("tekstTekenenTekst"), true);
		tekstTekenenBox.addActionListener(this);
		
		// Deprecated instellingen
		formuleOptieBox = new JCheckBox(Kladje.rb.getString("formuleOptieTekst"), false);
		formuleOptieBox.setVisible(Kladje.isExperimental);
		formuleOptieBox.addActionListener(this);
		
		fiButton = new FormuleInstellingenButton("settings");
		fiButton.addActionListener(this);
		fiButton.setVisible(false);
		
		roterenBox = new JCheckBox(Kladje.rb.getString("roterenTekst"), true);
		roterenBox.addActionListener(this);
		
		schalenBox = new JCheckBox(Kladje.rb.getString("schalenTekst"), true);
		schalenBox.addActionListener(this);
		
		translationLabel = new JLabel("translation");
		translationLabel.setFont(theFont);
		
		translationXTF = new JTextField("0");
		translationXTF.setFont(theFont);
		translationXTF.addActionListener(this);
		
		translationYTF = new JTextField("0");
		translationYTF.setFont(theFont);
		translationYTF.addActionListener(this);
		
		scaleLabel = new JLabel("scale");
		scaleLabel.setFont(theFont);
		
		scaleTF = new JTextField("1.0");
		scaleTF.setFont(theFont);
		scaleTF.addActionListener(this);
		
		ivmOptieBox = new JCheckBox(Kladje.rb.getString("ivmOptieTekst"), false);
		ivmOptieBox.addActionListener(this);
		ivmOptieBox.setVisible(Kladje.isExperimental);
		
		// Opmaak
		titleLayoutLabel = new JLabel(Kladje.rb.getString("titleLayoutLabel"));
		titleLayoutLabel.setForeground(Kladje.colorBlue1);
		titleLayoutLabel.setFont(font.deriveFont(Font.BOLD, 16));
		
		
		achtergrondGroep = new ButtonGroup();
		
		blancoButton = new JRadioButton(Kladje.rb.getString("blancoTekst"), true);
		blancoButton.setFont(theFont);
		blancoButton.addActionListener(this);
		achtergrondGroep.add(blancoButton);
		
		lijnenButton = new JRadioButton(Kladje.rb.getString("lijnenTekst"), false);
		lijnenButton.setFont(theFont);
		lijnenButton.addActionListener(this);
		achtergrondGroep.add(lijnenButton);
		
		ruitjes20Button = new JRadioButton(Kladje.rb.getString("ruitjesTekst"), false);
		ruitjes20Button.setFont(theFont);
		ruitjes20Button.addActionListener(this);
		achtergrondGroep.add(ruitjes20Button);
		
		ruitjes40Button = new JRadioButton(Kladje.rb.getString("ruitjes40Tekst"), false);
		ruitjes40Button.setFont(theFont);
		ruitjes40Button.addActionListener(this);
		achtergrondGroep.add(ruitjes40Button);
		
		ruitjes80Button = new JRadioButton(Kladje.rb.getString("ruitjes80Tekst"), false);
		ruitjes80Button.setFont(theFont);
		ruitjes80Button.addActionListener(this);
		achtergrondGroep.add(ruitjes80Button);
		
		toolbarOnTopCB = new JCheckBox(Kladje.rb.getString("toolBarOnTopTekst"), false);
		toolbarOnTopCB.addActionListener(this);
		
		plaatsGUI();
	}
	

	//private JRadioButton blancoButton, lijnenButton, ruitjes20Button, ruitjes40Button, ruitjes80Button; 
   	//private JCheckBox toolbarOnTopCB;
	
 	
	public void plaatsGUI() {
		Component[] k1 = {leftPanel, vgl()};
		
		Component[] r11 = {titleLoggingLabel, hgl()};
		Component[] r12 = {checkCB, hgl() };
		Component[] r13 = {ra(25,0), maxScoreLabel, ra(10,0), maxScoreField, hgl() };
		Component[] r13a = {ra(25,0), objectiveBtn, hgl() };
		Component[] r14 = {logCB, ra(10,0), logIDField, hgl() };
		Component[] r15 = {titleSettingsLabel, hgl()};
		Component[] r16 = {lijnTekenenBox, hgl() };
		Component[] r17 = {rechthoekTekenenBox, hgl() };
		Component[] r18 = {cirkelTekenenBox, hgl() };
		Component[] r19 = {tekstTekenenBox, hgl() };
		Component[] r110 = {roterenBox, hgl() };
		Component[] r111 = {schalenBox, hgl() };
		Component[] r112 = {kleurkeuzeBox, hgl() };
		Component[] r113 = {formuleOptieBox, ra(10,0), fiButton, hgl() };
		Component[] r114 = {ivmOptieBox, hgl() };
		
		
		Component[] r115 = {titleLayoutLabel, hgl() };
		Component[] r116 = {toolbarOnTopCB, hgl() };
		Component[] r117 = {blancoButton, hgl() };
		Component[] r118 = {ruitjes20Button, hgl() };
		Component[] r119 = {ruitjes40Button, hgl() };
		Component[] r120 = {ruitjes80Button, hgl() };
		
		
		
		
		Component[] k2 = {hb(r11), vst(10), hb(r12), checkCBRA, hb(r13), vst(5),hb(r13a), objectiveCBRA, hb(r14), vst(15), 
				hb(r15), vst(10), hb(r16), vst(5), hb(r17), vst(5), hb(r18), vst(5), hb(r19), vst(5), hb(r110), vst(5), hb(r111), vst(5), hb(r112), vst(5),
				hb(r113), vst(15), hb(r115), vst(10), hb(r116), vst(5), hb(r117), hb(r118), hb(r119), hb(r120), vgl()};
		
		Component[] rr = {vb(k1), ra(20,0), vb(k2)};
		
		mainPanel.add(hb(rr));
		add(mainPanel);
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
	
	private Component ln(int w, int h) {
	  Component c =  ln(h);
	  c.setPreferredSize(new Dimension(w,h));
	  c.setMinimumSize(new Dimension(w,h));
	  c.setMaximumSize(new Dimension(w,h));
	  return c;
	}
	private Component ln(int h) {
	  JPanel p = new JPanel() {
	      public void paintComponent(Graphics g) {
	        //g.setColor(WiskOpdr.colorBlue4);  
	        g.drawLine(0, getHeight()/2, getWidth(), getHeight()/2);
	        //g.drawLine(0, getHeight()/2+1, getWidth(), getHeight()/2+1);
	      }
	  };
	  p.setPreferredSize(new Dimension(1,h));
	  p.setMaximumSize(new Dimension(1000,h));
	  return p;

	}
	
	public void setEditState(Hashtable b)
	{
		
//System.out.println("kliep setEditState");
		boolean toolBarOnTop = false;
		if (b.containsKey("toolBarOnTop"))
			toolBarOnTop = ((Boolean) b.get("toolBarOnTop")).booleanValue();
		toolbarOnTopCB.setSelected(toolBarOnTop);
		boolean kleurkeuze = true;
		if (b.containsKey("kleurkeuze"))
			kleurkeuze = ((Boolean) b.get("kleurkeuze")).booleanValue();
		kleurkeuzeBox.setSelected(kleurkeuze);
		boolean lijnen = false;
		if (b.containsKey("lijnen"))
			lijnen = ((Boolean) b.get("lijnen")).booleanValue();
		lijnenButton.setSelected(lijnen);
		boolean ruitjes = false;
		if (b.containsKey("ruitjes"))
			ruitjes = ((Boolean) b.get("ruitjes")).booleanValue();
		int ruitjessize = 20;
		if (b.containsKey("ruitjessize"))
			ruitjessize = ((Integer) b.get("ruitjessize")).intValue();
		if (ruitjes && ruitjessize == 40)
			ruitjes40Button.setSelected(true);
		else if (ruitjes && ruitjessize == 80)
			ruitjes80Button.setSelected(true);
		else if(ruitjes)
			ruitjes20Button.setSelected(true);
		
		boolean lijnTekenen = true;
		if (b.containsKey("lijnTekenen"))
			lijnTekenen = ((Boolean) b.get("lijnTekenen")).booleanValue();
		lijnTekenenBox.setSelected(lijnTekenen);
		boolean rechthoekTekenen = true;
		if (b.containsKey("rechthoekTekenen"))
			rechthoekTekenen = ((Boolean) b.get("rechthoekTekenen")).booleanValue();
		rechthoekTekenenBox.setSelected(rechthoekTekenen);
		boolean cirkelTekenen = true;
		if (b.containsKey("cirkelTekenen"))
			cirkelTekenen = ((Boolean) b.get("cirkelTekenen")).booleanValue();
		cirkelTekenenBox.setSelected(cirkelTekenen);
		boolean tekstTekenen = true;
		if (b.containsKey("tekstTekenen"))
			tekstTekenen = ((Boolean) b.get("tekstTekenen")).booleanValue();
		tekstTekenenBox.setSelected(tekstTekenen);
		boolean formuleOptie = false;
		if (b.containsKey("formuleOptie"))
			formuleOptie = ((Boolean) b.get("formuleOptie")).booleanValue() && Kladje.isPremium;
		formuleOptieBox.setSelected(formuleOptie);
		fiButton.setVisible(formuleOptie );
		boolean ivmOptie = false;
		if (b.containsKey("ivmOptie"))
			ivmOptie = ((Boolean) b.get("ivmOptie")).booleanValue()&& Kladje.isPremium;
		ivmOptieBox.setSelected(ivmOptie);
		
		boolean roteren = true;
		if (b.containsKey("roteren"))
			roteren = ((Boolean) b.get("roteren")).booleanValue();
		roterenBox.setSelected(roteren);
		boolean schalen = true;
		if (b.containsKey("schalen"))
			schalen = ((Boolean) b.get("schalen")).booleanValue();
		schalenBox.setSelected(schalen);
		
		int translationx = 0;
		if(b.containsKey("translationX"))
			translationx = ((Integer) b.get("translationX")).intValue();
		translationXTF.setText(""+translationx);
		int translationy = 0;
		if(b.containsKey("translationY"))
			translationy = ((Integer) b.get("translationY")).intValue();
		translationYTF.setText(""+translationy);
		
		double scale = 1.0;
		if(b.containsKey("scale"))
			scale = ((Double) b.get("scale")).doubleValue();
		scaleTF.setText(""+scale);
		
		if (b.containsKey("klipBreedte"))
			klipBreedte = ((Integer) b.get("klipBreedte")).intValue();
		if (b.containsKey("klipHoogte"))
			klipHoogte = ((Integer) b.get("klipHoogte")).intValue();
		
		setBounds(getLocation().x, getLocation().y, klipBreedte + editWidth, Math.max(klipHoogte, editHeight));
		
		if (b.containsKey("formuleInstellingen")) {
			Hashtable formuleInstellingen = ((Hashtable) b.get("formuleInstellingen"));
			fiButton.setInstellingen(formuleInstellingen);
		}
		
		if (b.containsKey("checkDocent")) { // official keyword
			check = Boolean.TRUE.equals(b.get("checkDocent"));
			checkCB.setSelected(check);
		} else if (b.containsKey("check")) { // legacy
			check = ((Boolean) b.get("check")).booleanValue();
			checkCB.setSelected(check);
		}
		checkCBRA.setVisible(checkCB.isSelected());
		maxScoreLabel.setVisible(checkCB.isSelected());
		maxScoreField.setVisible(checkCB.isSelected());
		
		if (b.containsKey("logOption")) {
			boolean logOption = ((Boolean) b.get("logOption")).booleanValue();
			logCB.setSelected(logOption);
		}
		logIDField.setVisible(logCB.isSelected());
		
		if (b.containsKey("logID")) {
			String logID = ((String) b.get("logID"));
			logIDField.setText(logID);
		}
		
		if (b.containsKey("scoreMax")) {
			scoreMax = ((Integer) b.get("scoreMax")).intValue();
			maxScoreField.setText(""+scoreMax);
		}
		if (ObjectiveChoiceButton.hasObjectiveChoices())
		{
			objectiveBtn.setVisible(checkCB.isSelected());
			objectiveBtn.setEditState(b);
			objectiveCBRA.setVisible(checkCB.isSelected());
		} else 
			objectiveBtn.setVisible(false);
		
		// HIER !!
		klip.setEditState(b);
		
		((JDialog)SwingUtilities.getAncestorOfClass(JDialog.class,(Component)this)).pack();
		
		
	}
	
	public Hashtable getEditState()
	{
System.out.println("kliep getEditState");

		Hashtable h = klip.getEditState();
		
		try {
			scoreMax = Integer.parseInt(maxScoreField.getText());
			if (scoreMax > 0 && objectiveBtn.hasObjectiveChoices())
				h.putAll(objectiveBtn.getEditState(scoreMax));
		}
		catch(Exception e) {}
		check = checkCB.isSelected();	
		
		//h.put("check", check); // legacy
		h.put("checkDocent", check); // official name
		h.put("scoreMax", new Integer(scoreMax));
		h.put("logOption", new Boolean(logCB.isSelected()));
		h.put("logID", logIDField.getText());
		
		h.put("klipBreedte", new Integer(klipBreedte));
		h.put("klipHoogte", new Integer(klipHoogte));
		
		if(formuleOptieBox.isSelected() && fiButton.getInstellingen()!=null)
		{
			h.put("formuleInstellingen", fiButton.getInstellingen());
			h.put("premium", Boolean.TRUE); // De formule optiebox = premium.
		}
		
		return h;
		
		
	}
		
//	public void setBounds(int x, int y, int b, int h)
//	{
//		if (noSetBounds)
//		{	noSetBounds = false;
//			return;
//		}
////System.out.println("spiep setBounds raw " + x + " " + y + " " + b + " " + h);
//
//		if ((h <= 1) || (x < 0) || (b <= 1))
//			return;
//		
//		super.setBounds(x, y, klipBreedte + editWidth, Math.max(klipHoogte, editHeight));
//		
////System.out.println("spiep setBounds " + x + " " + y + " " + (spipBreedte + editWidth) + " " + 
////					Math.max(spipHoogte, editHeight));
//	
//		if (klip != null)
//			klip.setBounds(0, 0, klipBreedte, klipHoogte);
//		
//		plaatsComponenten();
//		
////System.out.println("setBounds " + x + " " + y + " " + b + " " + h);		
//		
//	}
	
//	public void zetBreedte(int b)
//	{	
//		klipBreedte = b;
//		
//		setBounds(getLocation().x, getLocation().y, klipBreedte + editWidth, Math.max(klipHoogte, editHeight));		
//		plaatsComponenten();
//	}
//	
//	public void zetHoogte(int h)
//	{	
//		klipHoogte = h;
//		
//		setBounds(getLocation().x, getLocation().y, klipBreedte + editWidth, Math.max(klipHoogte, editHeight));		
//	}
	
	public void setSize(int width, int height) {
		super.setSize(width, height);
		
	}
	
	@Override
	public void zetBreedte(int b) {
		klip.setBounds(0,0,b,klip.getHeight());
	}

	@Override
	public void zetHoogte(int h) {
		klip.setBounds(0,0,klip.getWidth(), h);
	
	}
	
	public void wis()
	{}
    
	public void zetMode(int mode)
	{}
	
    public void stop()
    {}
    
    public void start()
    {}
    
    public void addActionListener(ActionListener al)
    {}
    
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == toolbarOnTopCB)
		{
			klip.zetToolBarOnTop(toolbarOnTopCB.isSelected());
		}
		else if (e.getSource() == kleurkeuzeBox)
		{
			klip.zetKleurkeuze(kleurkeuzeBox.isSelected());
		}
		else if (e.getSource() == blancoButton)
		{
			klip.zetLijnen(false);
			klip.zetRuitjes(false,20);
		}
		else if (e.getSource() == lijnenButton)
		{
			klip.zetLijnen(lijnenButton.isSelected());
		}
		else if (e.getSource() == ruitjes20Button)
		{
			klip.zetRuitjes(ruitjes20Button.isSelected(),20);
		}
		else if (e.getSource() == ruitjes40Button)
		{
			klip.zetRuitjes(ruitjes40Button.isSelected(),40);
		}
		else if (e.getSource() == ruitjes80Button)
		{
			klip.zetRuitjes(ruitjes80Button.isSelected(),80);
		}
		else if (e.getSource() == lijnTekenenBox)
		{
			klip.zetLijnTekenen(lijnTekenenBox.isSelected());
		}
		else if (e.getSource() == rechthoekTekenenBox)
		{
			klip.zetRechthoekTekenen(rechthoekTekenenBox.isSelected());
		}
		else if (e.getSource() == cirkelTekenenBox)
		{
			klip.zetCirkelTekenen(cirkelTekenenBox.isSelected());
		}
		else if (e.getSource() == tekstTekenenBox)
		{
			klip.zetTekstTekenen(tekstTekenenBox.isSelected());
		}
		else if (e.getSource() == formuleOptieBox)
		{
			klip.zetFormuleOptie(formuleOptieBox.isSelected());
			fiButton.setVisible(formuleOptieBox.isSelected());
		}
		else if (e.getSource() == ivmOptieBox)
		{
			klip.zetIvmOptie(ivmOptieBox.isSelected());
		}
		else if (e.getSource() == roterenBox)
		{
			klip.zetRoteren(roterenBox.isSelected());
		}
		else if (e.getSource() == schalenBox)
		{
			klip.zetSchalen(schalenBox.isSelected());
		}
		else if (e.getSource() == translationXTF || e.getSource() == translationYTF)
		{
			klip.zetTranslation(Integer.parseInt(translationXTF.getText()), Integer.parseInt(translationYTF.getText()));
		}
		else if (e.getSource() == scaleTF)
		{
			klip.zetScale(Double.parseDouble(scaleTF.getText()));
		}
		else if (e.getSource() == checkCB)
		{
			maxScoreLabel.setVisible(checkCB.isSelected());
			maxScoreField.setVisible(checkCB.isSelected());
			maxScoreField.setText("0");
			checkCBRA.setVisible(checkCB.isSelected());
			objectiveBtn.setVisible(checkCB.isSelected() && objectiveBtn.hasObjectiveChoices());
			objectiveCBRA.setVisible(checkCB.isSelected() && objectiveBtn.hasObjectiveChoices());
		}
		else if (e.getSource() == logCB)
		{
			logIDField.setVisible(logCB.isSelected());
		}
		((JDialog)SwingUtilities.getAncestorOfClass(JDialog.class,(Component)this)).pack();
		
	}

}
