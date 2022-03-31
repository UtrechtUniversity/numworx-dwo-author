package fi.wiskopdr.strategievak;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
//import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

import fi.beans.numworxlf.JScrollPane;
import fi.beans.numworxlf.JTextField;
import fi.beans.numworxlf.JCheckBox;
import fi.beans.numworxlf.JLabel;
import fi.beans.numworxlf.JRadioButton;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.wiskopdr.HelpButton;
import fi.wiskopdr.ObjectiveChoiceButton;
import fi.wiskopdr.TekstVakPanel;
import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.WiskOpdrCheckbox;
import fi.wiskopdr.WiskOpdrTextField;
import fi.wiskopdr.AntwoordKeuzeVakEditPanel.EditorComponentListener;
import fi.wiskopdr.formuleobjects.FormuleEditor;
import fi.wiskopdr.opdrnav.OpdrachtNrRij;
import fi.wiskopdr.opdrnav.PlusMinKnop;
import fi.wiskopdr.opdrnav.XWidgetManager;
import fi.wiskopdr.tekstobjects.BasisTekstVak;
import fi.wiskopdr.tekstobjects.EditInteractiePanelDialog;
import fi.wiskopdr.tekstobjects.TekstEditor;
import fi.wiskopdr.tekstobjects.TekstVak;

public class StrategieVakEditPanel extends JPanel implements InteractieEditPanel, ActionListener, FocusListener, MouseListener{

	// Algemene attributen 
    private Font font = new Font("SansSerif",Font.PLAIN,12);//WiskOpdr.tekstFont;
    private int keuzeNr = 0;
   
    
    // Basis GUI
    private JPanel mainPanel;
    
    // Knop-editors
   	private JLabel titleKeuzeTekstenLabel;
   	private Box keuzeTekstenBox;
   	private TekstEditor[] keuzeVelden;
	private JPanel keuzeVeldenPanel;
	private JScrollPane scrollPaneKeuzeVelden;
	private JPanel basisKeuzeVeldenPanel;
	private JLabel aantalKeuzesLabel;
    private JTextField aantalKeuzesTF;
    
    private int aantalKeuzes = 3;
    private int maxAantalKeuzes = 20;
    
    // Component editor
    private TekstEditor componentEditor;
	private JPanel componentEditorPanel;
   	private JLabel titleComponentLabel;
   	private Box componentBox;
   	
    private OpdrachtNrRij knopTab;
    private ArrayList<String>[] stepContents;
    
    // Logging/Nakijken
  	private JLabel titleLoggingLabel;
  	private Box loggingBox;
    private JCheckBox checkCB;
    private JCheckBox teltMeeCB;
    private JRadioButton checkAutomatischRB;
    private JRadioButton checkDocentRB;
    private JTextField scoreTF;
    private JLabel scoreLabel;
      
    private JCheckBox logCB;
  	private JTextField logIDField;
  	private JTextField logIDLabelField;
  	private JLabel logIDLabelLabel;
  	private ObjectiveChoiceButton logObjectivesButton;
  	
  	private JLabel titleAntwoordLabel;
  	private Box antwoordBox;
  	private JTabbedPane tabbedPane;
  	private JCheckBox formuleAntwoordModelCB;
  	private JCheckBox vergelijkingAntwoordModelCB;
  	private JPanel formuleAntwoordTabblad;
  	private JPanel vergelijkingAntwoordTabblad;
    
  	
     
 // Helpbuttons
    private static String HELP_14_URL_CHECK = WiskOpdr.rb.getString("HELP_14_URL_CHECK");
    private static String HELP_14_URL_TELTMEE = WiskOpdr.rb.getString("HELP_14_URL_TELTMEE");
    private static String HELP_14_URL_LOGID = WiskOpdr.rb.getString("HELP_14_URL_LOGID");
	
    private HelpButton hbCheck = makeHelpButton(HELP_14_URL_CHECK);
	private HelpButton hbTeltMee = makeHelpButton(HELP_14_URL_TELTMEE);
	private HelpButton hbLogID = makeHelpButton(HELP_14_URL_LOGID);
    
	// AntwoordManagers
	private FormuleAntwoordManager formuleAntwoordManager;
	private VergelijkingAntwoordManager vergelijkingAntwoordManager;
	
    // Overige attributen (wellicht overbodig geworden
	int stappenBreedte = 0;
    int stappenHoogte = 400;
    
	int editHeight = 400;
    
    Font theFont;
    FontMetrics theFM;
    
    int width = 400;
    int height = 300;
    
    int cbHeight = 20;
    int cbWidth = 200;
    
    int offset = 10;
    
	private JLabel[] nrLabels;
	//private JCheckBox[] vereistCB;
	private PlusMinKnop stapPositieKnop;
    
   
    private int scoreMax = 10;
    //private JLabel scoreLabel;
   
    
   
    private boolean[] stepRequired;
    
    
	public StrategieVakEditPanel()
	{
		setLayout(new BorderLayout());
		formuleAntwoordManager = new FormuleAntwoordManager(this);
		vergelijkingAntwoordManager = new VergelijkingAntwoordManager(this);
        
		makeGUI();
		
		
		
        stepContents = new ArrayList[aantalKeuzes];
        for(int i = 0; i < aantalKeuzes; i++)
        { stepContents[i] = new ArrayList<String>();
        }
        stepRequired = new boolean[maxAantalKeuzes];
        for(int i = 0; i < maxAantalKeuzes; i++)
          stepRequired[i] = false;
    }
	
	private void makeGUI() {
		// Main
		mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBackground(WiskOpdr.colorGray3);
		
		// GUI Keuzeteksten
		aantalKeuzesLabel = new JLabel(WiskOpdr.rb.getString("Strategy_nrOfSteps"));
		aantalKeuzesLabel.setForeground(WiskOpdr.colorBlue1);
		aantalKeuzesLabel.setFont(font);
		
		aantalKeuzesTF = new JTextField("3");
		aantalKeuzesTF.setPreferredSize(new Dimension(40,22));
		aantalKeuzesTF.setMaximumSize(new Dimension(50,22));
		aantalKeuzesTF.setFont(font);
		aantalKeuzesTF.addActionListener(this);
		aantalKeuzesTF.addFocusListener(this);
		
		titleKeuzeTekstenLabel = new JLabel(WiskOpdr.rb.getString("Strategy_name"));
		titleKeuzeTekstenLabel.setForeground(WiskOpdr.colorBlue1);
		titleKeuzeTekstenLabel.setFont(font.deriveFont(Font.BOLD, 16));
		
		basisKeuzeVeldenPanel = new JPanel(new BorderLayout());
		basisKeuzeVeldenPanel.setPreferredSize(new Dimension(180,340));
		basisKeuzeVeldenPanel.setMinimumSize(new Dimension(180,340));
		basisKeuzeVeldenPanel.setMaximumSize(new Dimension(180,740));
		basisKeuzeVeldenPanel.setBackground(WiskOpdr.colorGray3);
        
        keuzeVeldenPanel = new JPanel();
		keuzeVeldenPanel.setLayout(null);
		keuzeVeldenPanel.setBackground(WiskOpdr.colorGray3);
      	
      	scrollPaneKeuzeVelden = new JScrollPane(keuzeVeldenPanel,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      	scrollPaneKeuzeVelden.setBorder(BorderFactory.createEmptyBorder());
      	basisKeuzeVeldenPanel.add(scrollPaneKeuzeVelden);
      	scrollPaneKeuzeVelden.setBackground(WiskOpdr.colorGray3);
    	
      	stapPositieKnop = new PlusMinKnop(0,12,16,20,PlusMinKnop.VERTIKAAL);
      	stapPositieKnop.setBackground(new Color(210,210,210));
      	stapPositieKnop.addActionListener(this);
      	keuzeVeldenPanel.add(stapPositieKnop,0);
         
      	nrLabels = new JLabel[maxAantalKeuzes];
      	Border border = BorderFactory.createMatteBorder(1, 1, 1, 0, new Color(128, 128, 128));
      	int veldHeight = 50;
      	int veldOffset = 5;
      	int veldWidth = 320;
      	for(int i = 0; i < maxAantalKeuzes; i++) {
			nrLabels[i] = new JLabel("" + (i+1), SwingConstants.CENTER);
			nrLabels[i].setBounds(20,i * (veldHeight + veldOffset), 20, 50);
			nrLabels[i].setOpaque(true);
			nrLabels[i].setBackground(new Color(230,230,230));
			nrLabels[i].setBorder(border);
			nrLabels[i].setFont(theFont);
			nrLabels[i].addMouseListener(this);
		}
      	maakKeuzeVelden();
      	
      	// GUI Component editor
      	titleComponentLabel = new JLabel(WiskOpdr.rb.getString("Strategy_content"));
      	titleComponentLabel.setForeground(WiskOpdr.colorBlue1);
      	titleComponentLabel.setFont(font.deriveFont(Font.BOLD, 16));
      	
      	componentEditor = new TekstEditor(true, true);
        componentEditor.setBounds(0,20,450,150);
        componentEditor.addActionListener(this);
        componentEditor.setResizable(true);
        
        componentEditorPanel = new JPanel();
        componentEditorPanel.setLayout(null);
        componentEditorPanel.add(componentEditor);
        componentEditorPanel.addComponentListener(new EditorComponentListener());
        componentEditorPanel.setPreferredSize(new Dimension(450,140));
        componentEditorPanel.setMaximumSize(new Dimension(2835,173));
        componentEditorPanel.setMinimumSize(new Dimension(450,140));
        
      	knopTab = new OpdrachtNrRij(aantalKeuzes, 0, 0);
        knopTab.setSize(knopTab.getSize().width, 23);
        knopTab.setTab(true);
        knopTab.setScoresVisible(false);
        knopTab.addActionListener(this);
        knopTab.setBackground(new Color(210,210,210));
        knopTab.setSelected(1);
        componentEditorPanel.add(knopTab,0);
        
        // Logging/Nakijken
 		titleLoggingLabel = new JLabel(WiskOpdr.rb.getString("FEV_titleLoggingLabel"));
     	titleLoggingLabel.setForeground(WiskOpdr.colorBlue1);
     	titleLoggingLabel.setFont(font.deriveFont(Font.BOLD, 16));
     	
     	checkCB = makeCheckBox(WiskOpdr.rb.getString("checkCBLabel"),true,true);
         teltMeeCB = makeCheckBox(WiskOpdr.rb.getString("teltMeeCBLabel"),true,true);
         logCB = makeCheckBox(WiskOpdr.rb.getString("logCBLabel"),false,true);
         logIDField = makeTextField(520,5,60,20,"0",false);
         logIDLabelField = makeTextField(520,25,60,20,"",false);
         //ScoringLabel = makeLabel(320,385,160,20,WiskOpdr.rb.getString("score"),true);
         logIDLabelLabel = makeLabel(470,25,50,20,WiskOpdr.rb.getString("TVEP_logIDLabelLabel"),false);
 		
         checkAutomatischRB = new JRadioButton("Automatisch nakijken");
         checkAutomatischRB.addActionListener(this);
         checkDocentRB = new JRadioButton("Nakijken door docent");
         checkDocentRB.addActionListener(this);
         checkDocentRB.setSelected(true);
         ButtonGroup buttonGroup = new ButtonGroup();
         buttonGroup.add(checkAutomatischRB);
         buttonGroup.add(checkDocentRB);
         
         scoreLabel = new JLabel("Score");
         scoreLabel.setForeground(WiskOpdr.colorBlue1);
         scoreLabel.setFont(font);
         
         scoreTF = new JTextField("" + scoreMax);
         scoreTF.setPreferredSize(new Dimension(40,22));
         scoreTF.setMaximumSize(new Dimension(50,22));
         scoreTF.setFont(font);
         scoreTF.addActionListener(this);
         scoreTF.addFocusListener(this);
         
         logObjectivesButton = new ObjectiveChoiceButton();
         logObjectivesButton.setVisible(ObjectiveChoiceButton.hasObjectiveChoices());
         logObjectivesButton.setBounds(600,5,120,20);
         logObjectivesButton.setPreferredSize(new Dimension(120,22));
         logObjectivesButton.setMaximumSize(new Dimension(120,22));
     	
         formuleAntwoordModelCB = makeCheckBox("Formule-antwoordmodel",false, false);
         vergelijkingAntwoordModelCB = makeCheckBox("Vergelijking-antwoordmodel",false, false);
         
         titleAntwoordLabel = new JLabel(WiskOpdr.rb.getString("FEV_titleAntwoordenLabel"));
         titleAntwoordLabel.setForeground(WiskOpdr.colorBlue1);
         titleAntwoordLabel.setFont(font.deriveFont(Font.BOLD, 16));
         
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
         tabbedPane.setMaximumSize(new Dimension(400,1200));
         tabbedPane.setOpaque(false);
         
         formuleAntwoordTabblad = formuleAntwoordManager.getPanel();
         formuleAntwoordTabblad.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
         vergelijkingAntwoordTabblad = vergelijkingAntwoordManager.getPanel();
         vergelijkingAntwoordTabblad.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        
         
       	removeAll();
 	    plaatsGUI();
 	    add(mainPanel);	
	}

	private void plaatsGUI() {
		// plaats componenten keuzeTekstenBox
		Component[] r11 = {titleKeuzeTekstenLabel, 	hgl()};
		Component[] r12 = {aantalKeuzesLabel, ra(10,0), aantalKeuzesTF, 	hgl()};
		Component[] r13 = {basisKeuzeVeldenPanel, 	hgl()};
		Component[] k1 = {hb(r11), vst(20),hb(r12), vst(10),hb(r13)};
		keuzeTekstenBox = vb(k1);
		
		// plaats compoenenten antwoordbox
		Component[] r30 = {titleAntwoordLabel, hgl() };
		Component[] r31 = {tabbedPane, hgl()};
		Component[] k3 = {hb(r30), vst(5),hb(r31)};
		antwoordBox = vb(k3);
		antwoordBox.setVisible(false);
		
		Component[] r21 = {titleComponentLabel, 	hgl()};
		Component[] r22 = {componentEditorPanel};
		Component[] k2 = {hb(r21), vst(20),hb(r22), vst(20),antwoordBox, vgl()};
		componentBox = vb(k2);
		
		// plaatsComponenten loggingBox
		Component[] r41 = {titleLoggingLabel, 	hgl()};
		Component[] r42 = {checkCB, 			ra(5,0),	hgl(),	hbCheck};
		Component[] r42a = {ra(20,0), checkAutomatischRB, 	hgl()			};
		Component[] r42b = {ra(20,0), checkDocentRB, 	hgl()			};
		Component[] r42c = {ra(50,0), scoreLabel, ra(5,0), scoreTF,	hgl()			};
		Component[] r43 = {teltMeeCB, 			ra(5,0),	hgl(),	hbTeltMee};
		Component[] r44 = {logCB, 				ra(5,10), logIDField, ra(5,10), logIDLabelLabel, ra(5,10), logIDLabelField,  ra(5,0),	hgl(),	hbLogID};
		Component[] r45 = {ra(6,0),			logObjectivesButton, hgl()};
		Component[] r46 = {formuleAntwoordModelCB, hgl()};
		Component[] r47 = {vergelijkingAntwoordModelCB, hgl()};
		
		Component[] k4 = {hb(r41),vst(20),hb(r42),vst(3),hb(r42a),vst(3),hb(r42b),vst(3),hb(r42c),vst(3),hb(r43),vst(3),hb(r44),vst(3),hb(r45),vst(65),hb(r46),vst(3),hb(r47), vgl()};
		Box loggingBox = vb(k4);	
		
		// boxes plaatsen
		Box boxh = Box.createHorizontalBox();
		mainPanel.add(boxh);
		
		boxh.add(keuzeTekstenBox);
		boxh.add(Box.createHorizontalStrut(50));
		
		//Box boxv1 = Box.createVerticalBox();
		//boxv1.add(componentEditorPanel);
		boxh.add(componentBox);
		boxh.add(Box.createHorizontalStrut(50));
		boxh.add(loggingBox);
		
		
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
	
	public JLabel makeLabel(int x, int y, int b, int h, String text, boolean visible)
    {   JLabel label = new JLabel(text);
        label.setBounds(x,y,b,h);
        label.setVisible(visible);
        label.setFont(theFont);
        add(label,0);
        return label;
    }
	
	public JTextField makeTextField(int x, int y, int b, int h, String text, boolean visible)
	{	JTextField textField = new JTextField(text);
		textField.setBounds(x,y,b,h);
		textField.setPreferredSize(new Dimension(40,22));
        textField.setMaximumSize(new Dimension(50,22));
		textField.setFont(font);
		textField.addActionListener(this);
		textField.setVisible(visible);
		add(textField,0);
		return textField;
	}
	
	public JCheckBox makeCheckBox(String text, boolean selected, boolean visible)
	{	JCheckBox checkbox = new JCheckBox(text);
		checkbox.setFont(font);
		checkbox.setOpaque(false);
		checkbox.addActionListener(this);
		checkbox.setSelected(selected);
		checkbox.setVisible(visible);
		return checkbox;
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
	
	private void setStepContent(ArrayList<String> content)
	{
	    String contentString = "";
	    for(int i = 0; i < content.size(); i++)
	      contentString = contentString + content.get(i);
        componentEditor.zetTekst("");  
        componentEditor.geefTekstVak().setCaret(0);
        componentEditor.geefTekstVak().insert(contentString);
        componentEditor.geefTekstVak().setCaret(contentString.length());
        componentEditor.layoutTekst();
            
    }
    
    private void getStepContent()
    {   if(stepContents==null)
          return;
        else if(keuzeNr >= stepContents.length)
          return;
        //stepContents[keuzeNr] = componentEditor.getCompleteText().trim();
        String content = componentEditor.getCompleteText().trim();
        //hier de grote kniptruc
        
        stepContents[keuzeNr] = getArrayListContentsFromString(content);
    }
    
    private ArrayList<String> getArrayListContentsFromString(String s)
    {
      System.out.println("input: " + s);
      
      ArrayList<String> list = new ArrayList<String>();
      
      int startingPoint = 0;
      for(int i = 1; i < s.length(); i++)
      { if(s.charAt(i) == '$' && i < s.length() && s.charAt(i + 1) == 'V')
        {  list.add(s.substring(startingPoint, i));
           startingPoint = i;
        }
        if(s.charAt(i) == '@')
        {
            if(s.charAt(startingPoint) == '$' && s.charAt(startingPoint + 1) == 'V')
            {
              list.add(s.substring(startingPoint, i + 1));
              startingPoint = i + 1;
            }
        }
      }
      if(startingPoint < s.length())
        list.add(s.substring(startingPoint, s.length()));
      return list;
    }
	
    private void setStepContent()
    {   if(stepContents==null)
          return;
        else if(keuzeNr >= stepContents.length)
          return;
        setStepContent(stepContents[keuzeNr]);    
    }
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == aantalKeuzesTF)
        {
		  aantalKeuzeVeldenAction();
          
        }
		else if(e.getSource() == knopTab)
        {   int nr = Integer.parseInt(e.getActionCommand())-1;
            moveStepFocus(nr);
            componentEditor.geefTekstVak().requestFocus();
        }
		else if(e.getSource() == stapPositieKnop)
        {   int posX = (int) stapPositieKnop.getLocation().getX();
            int posY = (int) stapPositieKnop.getLocation().getY();
            String resKeuze = "";
            if(e.getActionCommand().equals("min") && keuzeNr<aantalKeuzes-1) 
            {   
                resKeuze = keuzeVelden[keuzeNr].getCompleteText();
                //String resStepContent = componentEditor.getCompleteText().trim();
                ArrayList<String> resStepContent = getArrayListContentsFromString(componentEditor.getCompleteText().trim());
                boolean resRequired = stepRequired[keuzeNr];
                
                keuzeVelden[keuzeNr].zetTekst(keuzeVelden[keuzeNr + 1].getCompleteText());
                keuzeVelden[keuzeNr + 1].zetTekst(resKeuze);
                
                stepContents[keuzeNr] = stepContents[keuzeNr + 1];
                stepContents[keuzeNr + 1] = resStepContent;
                
                stepRequired[keuzeNr] = stepRequired[keuzeNr + 1];
                stepRequired[keuzeNr + 1] = resRequired;
                                
                keuzeVelden[keuzeNr].layoutTekst();
                keuzeNr++;//adjust keuzeNr here to avoid that moveStepFocus (fired from layoutTekst) changes too much 
                keuzeVelden[keuzeNr].layoutTekst();
                
                knopTab.setSelected(keuzeNr+1);
                stapPositieKnop.setLocation(posX, posY + 55);
            }
            if(e.getActionCommand().equals("plus") && keuzeNr>0) 
            {   resKeuze = keuzeVelden[keuzeNr].getCompleteText();
                //String resStepContent = componentEditor.getCompleteText().trim();
                ArrayList<String> resStepContent = getArrayListContentsFromString(componentEditor.getCompleteText().trim());
                boolean resRequired = stepRequired[keuzeNr];
            
                keuzeVelden[keuzeNr].zetTekst(keuzeVelden[keuzeNr - 1].getCompleteText());
                keuzeVelden[keuzeNr - 1].zetTekst(resKeuze);
                
                stepContents[keuzeNr] = stepContents[keuzeNr-1];
                stepContents[keuzeNr-1] = resStepContent;
                
                stepRequired[keuzeNr] = stepRequired[keuzeNr - 1];
                stepRequired[keuzeNr - 1] = resRequired;
                
                keuzeVelden[keuzeNr].layoutTekst();
                keuzeNr--;
                keuzeVelden[keuzeNr].layoutTekst();
                knopTab.setSelected(keuzeNr+1);
                stapPositieKnop.setLocation(posX, posY - 55);
            }
            
        }
		else if(e.getSource()==logCB) {   
			logIDField.setVisible(logCB.isSelected());
			logIDLabelField.setVisible(logCB.isSelected());	
	    		logIDLabelLabel.setVisible(logCB.isSelected());
	    		repaint();
	    		pack();
	    }
		else if(e.getSource()==checkCB) {   
			scoreLabel.setVisible(checkCB.isSelected() && checkDocentRB.isSelected());
		    	scoreTF.setVisible(checkCB.isSelected() && checkDocentRB.isSelected());
		    	checkAutomatischRB.setVisible(checkCB.isSelected());
		    	checkDocentRB.setVisible(checkCB.isSelected());
		    	logObjectivesButton.setVisible(ObjectiveChoiceButton.hasObjectiveChoices() && checkCB.isSelected());
		    	if(!checkCB.isSelected())
		    		scoreTF.setText("0");
		    	antwoordBox.setVisible(checkCB.isSelected() && checkAutomatischRB.isSelected());
		    	pack(); 
	    }
		else if(e.getSource()==checkAutomatischRB) { 
			scoreLabel.setVisible(!checkAutomatischRB.isSelected());
			scoreTF.setVisible(!checkAutomatischRB.isSelected());
			antwoordBox.setVisible(checkAutomatischRB.isSelected());
			formuleAntwoordModelCB.setVisible(checkAutomatischRB.isSelected());
			vergelijkingAntwoordModelCB.setVisible(checkAutomatischRB.isSelected());
			pack();
		}
		else if(e.getSource()==checkDocentRB) { 
			scoreLabel.setVisible(checkDocentRB.isSelected());
			scoreTF.setVisible(checkDocentRB.isSelected());
			antwoordBox.setVisible(checkAutomatischRB.isSelected());
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
	}
	
	public void pack() {
	  ((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)mainPanel)).packWidth();
    }
	
	
	public void aantalKeuzeVeldenAction()
	{
	  int posX = (int) stapPositieKnop.getLocation().getX();
      int posY = (int) stapPositieKnop.getLocation().getY();
      int oldKeuzeNr = keuzeNr;
      try {
        aantalKeuzes = Math.min(maxAantalKeuzes, Integer.parseInt(aantalKeuzesTF.getText()));  
      }
      catch(Exception e) {
        return;
      }
      if(aantalKeuzes < keuzeNr + 1)
      {  int verschil = keuzeNr + 1 - aantalKeuzes; 
         keuzeNr = aantalKeuzes - 1;
         setStepContent();
         stapPositieKnop.setLocation(posX, posY - verschil * 55);
      }
      maakKeuzeVelden();
      maakInhoudVelden();
      if(aantalKeuzes > oldKeuzeNr + 1)
      {  keuzeNr = oldKeuzeNr;
         stapPositieKnop.setLocation(posX, posY);
         knopTab.setSelected(keuzeNr+1);
      }
      for(int i = aantalKeuzes; i < maxAantalKeuzes; i++)
        stepRequired[i] = false;
	}

	@Override
	public void focusGained(FocusEvent e) {
	  
	}

	@Override
	public void focusLost(FocusEvent e) {
	    if(e.getSource() == aantalKeuzesTF)
	    {
	      aantalKeuzeVeldenAction();
	    }
	}

	public void maakKeuzeVelden()
    {
          int veldHeight = 50;
          int veldWidth = 160;
          int offset = 5;
          TekstEditor[] oldKeuzeVelden = null;
          keuzeVeldenPanel.setBounds(0, 0,veldWidth,aantalKeuzes*(veldHeight + offset));
          keuzeVeldenPanel.removeAll();
          if(keuzeVelden != null)
          { 
            oldKeuzeVelden = new TekstEditor[keuzeVelden.length];
            for(int i = 0; i < keuzeVelden.length; i++)
            { keuzeVelden[i].removeMouseListener(this);  
              oldKeuzeVelden[i] = keuzeVelden[i];
            
            }
          }
          
          keuzeVelden = new TekstEditorStrategieKeuzeVeld[aantalKeuzes];
          for(int i=0 ; i<aantalKeuzes ; i++)
          {
              
              keuzeVeldenPanel.add(nrLabels[i]);
              
              
              if(oldKeuzeVelden != null && i < oldKeuzeVelden.length)
              {
                keuzeVelden[i] = oldKeuzeVelden[i];
              }
              else
              {
                keuzeVelden[i] = new TekstEditorStrategieKeuzeVeld(this, i);
                keuzeVelden[i].setBounds(40,i*(veldHeight + offset),veldWidth - 30,veldHeight);
              }
              keuzeVeldenPanel.add(keuzeVelden[i],0);
              keuzeVelden[i].addMouseListener(this);
              //keuzeVeldenPanel.add(vereistCB[i], 0);
          }
          keuzeVeldenPanel.add(stapPositieKnop);
          keuzeVeldenPanel.setPreferredSize(new Dimension(veldWidth,aantalKeuzes*(veldHeight + offset)));
          keuzeVeldenPanel.scrollRectToVisible(new Rectangle(0,0, 10, 100));
          keuzeVeldenPanel.revalidate();
          keuzeVeldenPanel.doLayout();
          
        repaint();
    }
	
	public void maakInhoudVelden()
	{
	  int tabbladX = (int) knopTab.getLocation().getX();
	  int tabbladY = (int) knopTab.getLocation().getY();
	  componentEditorPanel.remove(knopTab);
	  knopTab = new OpdrachtNrRij(aantalKeuzes, tabbladX, tabbladY);
      knopTab.setTab(true);
      knopTab.setScoresVisible(false);
      knopTab.setSize(knopTab.getSize().width, 23);
      knopTab.addActionListener(this);
      knopTab.setBackground(new Color(210,210,210));
      knopTab.setSelected(keuzeNr+1);
      componentEditorPanel.add(knopTab,0);
      ArrayList<String>[] stepContentsNew = new ArrayList[aantalKeuzes];
      for(int i = 0; i < stepContentsNew.length; i++)
        stepContentsNew[i] = new ArrayList<String>();
      for(int i = 0; i < Math.min(aantalKeuzes, stepContents.length); i++)
      {
        stepContentsNew[i] = stepContents[i];
      }
      stepContents = stepContentsNew;
      repaint();
	}
	
	
	@Override
	public void setEditState(Hashtable h) {
		Hashtable[] steps = null;
		int scoreMax = 10;
		boolean[] stepRequired = null;
		boolean check = true;
		boolean checkDocent = true;
		boolean teltMee = true;
		boolean logOption = false;
		String logID = "";
		String logIDLabel = "";
		boolean checkExternal = false;
		boolean hasFormuleAnswerModel = false;
        boolean hasVergelijkingAnswerModel = false;
		Hashtable formuleAntwoordModel = null;
		Hashtable vergelijkingAntwoordModel = null;
		
		if(h.containsKey("scoreMax")) scoreMax = ((Integer) h.get("scoreMax")).intValue();
		if(h.containsKey("stepRequired")) stepRequired = (boolean[]) h.get("stepRequired");
		if(h.containsKey("check")) check = ((Boolean)h.get("check")).booleanValue();
		if(h.containsKey("teltMee")) teltMee = ((Boolean)h.get("teltMee")).booleanValue();
		if(h.containsKey("checkDocent")) checkDocent = ((Boolean)h.get("checkDocent")).booleanValue();
		if(h.containsKey("hasFormuleAnswerModel")) hasFormuleAnswerModel = ((Boolean)h.get("hasFormuleAnswerModel")).booleanValue();
		if(h.containsKey("hasVergelijkingAnswerModel")) hasVergelijkingAnswerModel = ((Boolean)h.get("hasVergelijkingAnswerModel")).booleanValue();
        if(h.containsKey("teltMee")) teltMee = ((Boolean)h.get("teltMee")).booleanValue();
		if(h.containsKey("logOption")) logOption = ((Boolean)h.get("logOption")).booleanValue();
		if(h.containsKey("logID")) logID = (String)h.get("logID");
		if(h.containsKey("logIDLabel")) logIDLabel = (String)h.get("logIDLabel");
		if(check && !checkDocent) {
		  if(h.containsKey("formuleAntwoordModel")) formuleAntwoordModel = (Hashtable)h.get("formuleAntwoordModel");
		  formuleAntwoordManager.setEditState(formuleAntwoordModel);
		  if(h.containsKey("vergelijkingAntwoordModel")) vergelijkingAntwoordModel = (Hashtable)h.get("vergelijkingAntwoordModel");
          vergelijkingAntwoordManager.setEditState(vergelijkingAntwoordModel);
	        
		}
		
		if(stepRequired != null && stepRequired.length > 0) {   
		  System.arraycopy(stepRequired, 0, this.stepRequired, 0, stepRequired.length);
		}
		this.scoreMax = scoreMax;
		scoreTF.setText("" + scoreMax);
		if(h.containsKey("steps"))
		    steps = (Hashtable[]) h.get("steps");
		aantalKeuzes = steps.length;
        aantalKeuzesTF.setText(""+aantalKeuzes);
        maakKeuzeVelden();
        for(int i=0 ; i<aantalKeuzes ; i++)
        {   
            String keuze = ((String) steps[i].get("keuze"));
            keuzeVelden[i].zetTekst(keuze);
            keuzeVelden[i].layoutTekst();
        }
        this.stepContents = new ArrayList[steps.length];
        
        for(int i = 0; i < aantalKeuzes; i++)
        {
          ArrayList<String> content = new ArrayList<String>();
          try{
           content.addAll ( (ArrayList<String>) steps[i].get("stepContent"));
          }
          catch(Exception e)
          {
            String contentString = (String) steps[i].get("stepContent");
            content = getArrayListContentsFromString(contentString);
          }
          for(int j = 0; j < content.size(); j++)
          {
            if(content.get(j).startsWith("H4sIAAAAAAAAA"))
            {   
              String toReplace = content.get(j);
              content.set(j, "$V" + toReplace + "@");
              //content = "$V"+content+"@";
            }
            
          }
          this.stepContents[i] = content;
        }
        
        maakInhoudVelden();
        keuzeNr = 0;
        setStepContent();
        knopTab.setSelected(keuzeNr+1);
        int posX = (int) stapPositieKnop.getLocation().getX();
        int posY = (int) stapPositieKnop.getLocation().getY();
        stapPositieKnop.setLocation(posX, posY - (aantalKeuzes - 1) * 55);
        
        logCB.setSelected(logOption);
        logIDField.setVisible(logOption);
        logIDLabelField.setVisible(logOption);
        logIDLabelLabel.setVisible(logOption);
        //logObjectivesButton.setVisible(logOption);
        logIDField.setText(logID);
        logIDLabelField.setText(logIDLabel);
        logObjectivesButton.setEditState(h);
        
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
          
    	pack();
	}

	@Override
	public Hashtable getEditState() {
		
	    ArrayList<String>[] stepContents = null;
	    int scoreMax = 10;
	    boolean check = true;
		boolean teltMee = true;
		boolean logOption = false;
		String logID = "";
		String logIDLabel = "";
		boolean checkDocent = true;
		boolean hasFormuleAnswerModel = false;
		boolean hasVergelijkingAnswerModel = false;
		Hashtable formuleAntwoordModel = null;
		Hashtable vergelijkingAntwoordModel = null;
        
	    getStepContent();
        stepContents = this.stepContents;
        if(stepContents!=null)setStepContent(stepContents[0]);
        
        for(int i = 0; i < stepContents.length; i++)
        {
          //poging over andere boeg
          setStepContent(stepContents[i]);
          TekstVakPanel tvp = new TekstVakPanel();
          tvp.zetTekst(componentEditor.geefTekstVak().toString());
          String tvpString = tvp.toString();
          System.out.println("tvpString: " + tvpString); // dat is niet de string die ik wil hebben. Ik heb geen idee hoe ik die wel krijg.. ($V ..)
          
          
          //einde poging over andere boeg
          
          
          
          for(int j = 0; j < stepContents[i].size(); j++)
          {
            if(stepContents[i].get(j).startsWith("$V"))
            {
              String toReplace = stepContents[i].get(j);
              stepContents[i].set(j, toReplace.substring(2, toReplace.length() - 1));
            }
              //stepContents[i] = stepContents[i].substring(2, stepContents[i].length() - 1);
          }
        }
        Hashtable[] steps = new Hashtable[aantalKeuzes];
        for(int i = 0; i < aantalKeuzes; i++)
        {
          steps[i] = new Hashtable();
          steps[i].put("keuze", keuzeVelden[i].getCompleteText());
          
          steps[i].put("stepContent", stepContents[i]);
        }
        
        try
        {   scoreMax = Integer.parseInt(scoreTF.getText());
        }   
        catch(Exception ex) {}
        
        check = checkCB.isSelected();
		teltMee = teltMeeCB.isSelected();
		logOption = logCB.isSelected();
		logID = logIDField.getText();	
		logIDLabel = logIDLabelField.getText();
		checkDocent = checkDocentRB.isSelected();
		hasFormuleAnswerModel = formuleAntwoordModelCB.isSelected();
		hasVergelijkingAnswerModel = vergelijkingAntwoordModelCB.isSelected();
		
        
        Hashtable h = new Hashtable();
        h.put("steps", steps);
        h.put("stepRequired", stepRequired);
        h.put("check",new Boolean(check));
        h.put("checkDocent",new Boolean(checkDocent));
        h.put("hasFormuleAnswerModel",new Boolean(hasFormuleAnswerModel));
        h.put("hasVergelijkingAnswerModel",new Boolean(hasVergelijkingAnswerModel));
		h.put("teltMee",new Boolean(teltMee));
		h.put("logOption",new Boolean(logOption));
		h.put("logID",logID);
		h.put("logIDLabel",logIDLabel);
		h.putAll(logObjectivesButton.getEditState(scoreMax));
		if(check && !checkDocent) {
		  formuleAntwoordModel = formuleAntwoordManager.getEditState();
		  scoreMax = formuleAntwoordManager.getScoreMax();
	      h.put("formuleAntwoordModel",formuleAntwoordModel);
	      vergelijkingAntwoordModel = vergelijkingAntwoordManager.getEditState();
          scoreMax += vergelijkingAntwoordManager.getScoreMax();
          h.put("vergelijkingAntwoordModel",vergelijkingAntwoordModel);
		}
		h.put("scoreMax", new Integer(scoreMax));
        
        
        return h;
	}

	
	
	@Override
	public void zetBreedte(int b) {
		stappenBreedte = b;
		setBounds(getLocation().x, getLocation().y, stappenBreedte + width + 3 * offset, Math.max(stappenHoogte, editHeight));		
		//plaatsComponenten();
		
	}

	@Override
	public void zetHoogte(int h) {
		stappenHoogte = h;
		setBounds(getLocation().x, getLocation().y, stappenBreedte + width + 3 * offset, Math.max(stappenHoogte, editHeight));		
		
	}

	@Override
	public void stop() {
		
	}

	@Override
	public void start() {
		
	}

  @Override
  public void mouseClicked(MouseEvent e) {
    
  }

  @Override
  public void mousePressed(MouseEvent e) {
    if(keuzeVelden != null)
    {
      for(int i = 0; i < keuzeVelden.length; i++)
      {   if(e.getSource() == keuzeVelden[i] || e.getSource() == nrLabels[i])
          {
            moveStepFocus(i);
          }
      }
    }
    
  }

  public void moveStepFocus(int i)
  {
    if(keuzeNr != i && knopTab != null) 
    {
        int verschil = keuzeNr - i;
        getStepContent();
        keuzeNr = i;
        setStepContent();
        knopTab.setSelected(keuzeNr+1);
        
        int posX = (int) stapPositieKnop.getLocation().getX();
        int posY = (int) stapPositieKnop.getLocation().getY();
        stapPositieKnop.setLocation(posX, posY - verschil * 55);
    }
  }
  
  @Override
  public void mouseReleased(MouseEvent e) {
    
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    
  }

  @Override
  public void mouseExited(MouseEvent e) {
    
  }
  
  public class EditorComponentListener implements ComponentListener {

	      @Override
	      public void componentResized(ComponentEvent e) {
		    	  if(e.getSource()==componentEditorPanel) {
		    		  int w = componentEditorPanel.getWidth();
		    		  int h = componentEditorPanel.getHeight();
		    		  componentEditor.setBounds(0,20,w,h-20);
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
