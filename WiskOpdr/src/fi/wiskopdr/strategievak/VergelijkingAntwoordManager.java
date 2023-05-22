package fi.wiskopdr.strategievak;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import fi.beans.stringutils.StringUtils;
import fi.wiskopdr.BerekeningVakEditPanel;
import fi.wiskopdr.DialogFacade;
import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.WiskOpdrCheckbox;
import fi.wiskopdr.WiskOpdrTextField;
import fi.wiskopdr.formuleobjects.FormuleEditor;
import fi.wiskopdr.opdrnav.ActKeuzePanel;
import fi.wiskopdr.opdrnav.OpdrachtNrRij;
import fi.wiskopdr.opdrnav.PlusMinKnop;
import fi.wiskopdr.strategievak.FormuleAntwoordManager.EditorComponentListener;
import fi.wiskopdr.tekstobjects.EditInteractiePanelDialog;
import fi.wiskopdr.tekstobjects.TekstEditor;

public class VergelijkingAntwoordManager implements ActionListener {

  private Font font = new Font("SansSerif",Font.PLAIN,12);//WiskOpdr.tekstFont;
  private StrategieVakEditPanel strategieVakEditPanel;
  private BerekeningVakEditPanel berekeningVakEditPanel;
  
  // Basis GUI
  private JPanel mainPanel;
  
  //Antwoord editor
   private FormuleEditor antwoordvak;
   private JPanel antwoordEditorPanel;
   private JLabel titleAntwoordLabel;
   private Box antwoordBox;
  
   private OpdrachtNrRij tabbladTab;
   private PlusMinKnop aantalTabsKnop;
   private PlusMinKnop tabPositieKnop;
  
   private int aantalAnswerModels = 1;
   private Hashtable[] answerModels;
   private Hashtable resAnswerModel = new Hashtable();
   private int answerModelNr = 0;
   
   private String[] antwoordSubStrings = null;
   private String[] antwoordFuncStrings = null;
  
   // VormEditor
   private FormuleEditor vormEditor;
   private JPanel vormEditorPanel;
   private JLabel titleVormLabel;
   private Box vormBox;
   private DialogFacade vormEditorPopupFrame;
  
   // Feedback editor
   private JCheckBox feedbackCB;
   private TekstEditor feedbackEditor;
   private JCheckBox feedbackSizeCB;
   private JLabel titleFeedbackLabel;
   private JLabel titleFeedbackTekstLabel;
   private ActKeuzePanel goedFoutIP;
   private Box feedbackBox;
   private DialogFacade feedbackEditorPopupFrame;
   
   // Verificatie
   private JLabel titleVerificatieLabel;
   private JLabel titleVerificatieScoreLabel;
   private Box verificatieBox;
   private JCheckBox  gelijkwaardigCB, vormCB, exactCB, significantCB, eindOplossingCB;
   private JTextField gelijkwaardigPV, vormPV, exactPV, significantPV, eindOplossingPV;
   
   private boolean gelijkwaardig = true;
   private boolean vorm;
   private boolean eindOplossingNodig = true;
   private boolean exact;
   private boolean significant;
   private double eqTestValueMin = 0;
   private double eqTestValueMax = 5;
   
   private int puntenGelijkwaardig = 0;
   private int puntenVorm = 0;
   private int puntenEindOplossing = 10;
   private int puntenExact = 0;
   private int puntenSignificant = 0;
   private int puntenFeedback = 0;
   private boolean hasFeedback;
   
   static boolean  significantieAan=false;
  
   // Score
   private JLabel titleScoreLabel;
   private JLabel ScoringLabel; // overbodig?
   private Box scoringBox;
   private JTextField  feedbackPV;
   private JCheckBox scoreCumulatiefCB;
   
   public static void zetSignificantieAan(boolean b)
   {   significantieAan = b;
   }
   
   public VergelijkingAntwoordManager(StrategieVakEditPanel strategieVakEditPanel) {
     this.strategieVakEditPanel = strategieVakEditPanel;
     makeGUI();
     
     answerModels = new Hashtable[aantalAnswerModels];
   }
   
   public VergelijkingAntwoordManager(BerekeningVakEditPanel berekeningVakEditPanel) {
     this.berekeningVakEditPanel = berekeningVakEditPanel;
     makeGUI();
     
     answerModels = new Hashtable[aantalAnswerModels];
   }
   
   private void makeGUI() {
     // Main
     mainPanel = new JPanel(new BorderLayout());
     mainPanel.setBackground(WiskOpdr.colorGray4);
     
  // GUI antwoordBox
     titleAntwoordLabel = new JLabel(WiskOpdr.rb.getString("FEV_titleAntwoordLabel"));
     titleAntwoordLabel.setForeground(WiskOpdr.colorBlue1);
     titleAntwoordLabel.setFont(font.deriveFont(Font.BOLD, 16));
     titleAntwoordLabel.setBounds(0,-3,140,20);
    
     antwoordvak = new FormuleEditor(true);
     antwoordvak.setBounds(0,20,435,150);
     antwoordvak.setFont(font);
     antwoordvak.addActionListener(this);
     
     antwoordEditorPanel = new JPanel();
     antwoordEditorPanel.setBackground(WiskOpdr.colorGray4);
     antwoordEditorPanel.setLayout(null);
     //antwoordEditorPanel.add(titleAntwoordLabel);
     antwoordEditorPanel.add(antwoordvak);
     antwoordEditorPanel.addComponentListener(new EditorComponentListener());
     antwoordEditorPanel.setPreferredSize(new Dimension(450,140));
     antwoordEditorPanel.setMaximumSize(new Dimension(2835,170));
     antwoordEditorPanel.setMinimumSize(new Dimension(450,170));
     
     tabbladTab = new OpdrachtNrRij(aantalAnswerModels, 250,20);
     tabbladTab.setSize(tabbladTab.getSize().width, 23);
     tabbladTab.setTab(true);
     tabbladTab.setScoresVisible(false);
     tabbladTab.addActionListener(this);
     tabbladTab.setBackground(new Color(210,210,210));
     tabbladTab.setSelected(1);
     antwoordEditorPanel.add(tabbladTab,0);
     
     aantalTabsKnop = new PlusMinKnop(250+25*aantalAnswerModels+5 ,24,20,16,PlusMinKnop.HORIZONTAAL);
     aantalTabsKnop.setBackground(new Color(210,210,210));
     aantalTabsKnop.addActionListener(this);
     antwoordEditorPanel.add(aantalTabsKnop,0);
     
     tabPositieKnop = new PlusMinKnop(246+25*answerModelNr+5 ,0,20,16,PlusMinKnop.HORIZONTAAL);
     tabPositieKnop.addActionListener(this);
     antwoordEditorPanel.add(tabPositieKnop,0);
     
     // GUI Vormbox
     titleVormLabel = new JLabel(WiskOpdr.rb.getString("FEV_titleVormLabel"));
     titleVormLabel.setForeground(WiskOpdr.colorBlue1);
     titleVormLabel.setFont(font.deriveFont(Font.BOLD, 16));
     
     vormEditor = new FormuleEditor(true);
     vormEditor.setResizable(true);
     vormEditor.setMultiLine(true);
     vormEditor.setScrollHorizontal(true);
     vormEditor.setBounds(0,0,200,110);
     vormEditor.setFont(font);
     vormEditor.addActionListener(this);
     vormEditor.setVisible(false);
     
     vormEditorPanel = new JPanel();
     vormEditorPanel.setLayout(null);
     vormEditorPanel.setPreferredSize(new Dimension(200,120));
     vormEditorPanel.setMaximumSize(new Dimension(2860,160));
     vormEditorPanel.add(vormEditor);
     vormEditorPanel.addComponentListener(new EditorComponentListener());

   //GUI Feedback editor
     
     feedbackCB = makeCheckBox(WiskOpdr.rb.getString("feedbackCBLabel"),false,true);
     feedbackCB.setBounds(0,-2,240,20);
     antwoordEditorPanel.add(feedbackCB);
     
     titleFeedbackTekstLabel = new JLabel(WiskOpdr.rb.getString("FEV_titleFeedbackLabel"));
     titleFeedbackTekstLabel.setForeground(WiskOpdr.colorBlue1);
     titleFeedbackTekstLabel.setFont(font.deriveFont(Font.BOLD, 16));
     
     titleFeedbackLabel = new JLabel(WiskOpdr.rb.getString("feedbackLabel"));
     titleFeedbackLabel.setForeground(WiskOpdr.colorBlue1);
     titleFeedbackLabel.setFont(font.deriveFont(Font.BOLD, 16));
     
//     String[] items = {WiskOpdr.rb.getString("goedLabel"),WiskOpdr.rb.getString("halfLabel"),WiskOpdr.rb.getString("foutLabel")};
//     goedFoutIP = new ActKeuzePanel(items,440,420,70,80);
//     goedFoutIP.setPreferredSize(new Dimension(100,80));
     
     feedbackEditor = new TekstEditor(false,true,true);
     feedbackEditor.setPreferredSize(new Dimension(200,120));
     feedbackEditor.setMaximumSize(new Dimension(2280,160));
     feedbackEditor.setBounds(5,350,280,110);
     feedbackEditor.setFont(font);
     feedbackEditor.addActionListener(this);
     feedbackEditor.setBackground(new Color(255,255,200));
     
  // GUI Verificatie box
     titleVerificatieLabel = new JLabel(WiskOpdr.rb.getString("FEV_titleVerificatieLabel"));
     titleVerificatieLabel.setForeground(WiskOpdr.colorBlue1);
     titleVerificatieLabel.setFont(font.deriveFont(Font.BOLD, 16));
     
     titleVerificatieScoreLabel = new JLabel(WiskOpdr.rb.getString("FEV_titleScoringLabel"));
     titleVerificatieScoreLabel.setForeground(WiskOpdr.colorBlue1);
     titleVerificatieScoreLabel.setFont(font.deriveFont(Font.BOLD, 16));
     
     gelijkwaardigCB = makeCheckBox(WiskOpdr.rb.getString("gelijkwaardigCBLabel"),true,true);
    // gelijkwaardigCB.addMouseListener(this);
     vormCB = makeCheckBox(WiskOpdr.rb.getString("vormCBLabel"),false,true);
     exactCB = makeCheckBox(WiskOpdr.rb.getString("exactCBLabel"),false,true);
     significantCB = makeCheckBox(WiskOpdr.rb.getString("significantCBLabel"),false,significantieAan?true:false);
     eindOplossingCB = makeCheckBox(WiskOpdr.rb.getString("eindOplossingCBLabel"),true,true);
     gelijkwaardigPV = makeTextField(""+puntenGelijkwaardig,true);
     vormPV = makeTextField(""+puntenVorm,false);
     eindOplossingPV = makeTextField(""+puntenEindOplossing,true);
     exactPV = makeTextField(""+puntenExact,false);
     significantPV = makeTextField("0",false);
     
     // GUI Score
     titleScoreLabel = new JLabel(WiskOpdr.rb.getString("FEV_titleScoringLabel"));
     titleScoreLabel.setForeground(WiskOpdr.colorBlue1);
     titleScoreLabel.setFont(font.deriveFont(Font.BOLD, 16));
     
     feedbackPV = makeTextField(""+puntenFeedback,false);
     scoreCumulatiefCB =  makeCheckBox(WiskOpdr.rb.getString("scoreCumulatiefCBLabel"),false,true);
     
     String[] items = {WiskOpdr.rb.getString("goedLabel"),WiskOpdr.rb.getString("doorLabel"),WiskOpdr.rb.getString("halfLabel"),WiskOpdr.rb.getString("foutLabel")};
     goedFoutIP = new ActKeuzePanel(items,440,420,80,80);
     goedFoutIP.setPreferredSize(new Dimension(100,80));
     
     plaatsGUI();
   }
   
   private void plaatsGUI() {
  // plaats compoenenten antwoordbox
     //Component[] r30 = {titleAntwoordLabel, hgl() };
     Component[] r31 = {ra(0,130),   antwoordEditorPanel};
     Component[] k3 = {vst(10),hb(r31)};
     antwoordBox = vb(k3);
     
  // plaats componenten feedback box
     Component[] r51 = {titleFeedbackLabel,  ra(5,10),   hgl()};
     Component[] r52 = {goedFoutIP,              hgl()};
      
     Component[] k51 = {hb(r51),vst(5),hb(r52), vgl()};
     
     Component[] h5 = {ra(20,0),vb(k51)};
     feedbackBox = hb(h5);
     
     //plaats componenten scoringbox 
     Component[] r70 = {titleScoreLabel,     ra(10,10),      feedbackPV, ra(5,10), hgl()};
     Component[] r71 = {scoreCumulatiefCB,  hgl()};
     //Component[] r71 = {ra(10,10),         feedbackPV,     hgl()};
     //Component[] r72 = {goedFoutIP,        hgl()};
     
     Component[] k7 = {hb(r70),vst(5),hb(r71),vgl()};
     scoringBox = vb(k7);
     
  // plaats componenten verificatie box
     Component[] r61 = {titleVerificatieLabel,   ra(5,10),    hgl(),       ra(5,10),             titleVerificatieScoreLabel,ra(5,10)};
     Component[] r62 = {gelijkwaardigCB,         ra(10,10),      hgl(),          gelijkwaardigPV};
     Component[] r63 = {vormCB,                  hgl(),          vormPV};
     Component[] r64 = {eindOplossingCB,         hgl(),          eindOplossingPV};
     Component[] r65 = {significantCB,           hgl(),          significantPV};
     Component[] r66 = {exactCB,                 hgl(),          exactPV};
     Component[] r67 = {scoringBox,              hgl(),      };
     
     Component[] k6 = {hb(r61), vst(5), hb(r62), hb(r63), hb(r64), hb(r65), hb(r66), vst(10),vgl(),hb(r67)};
     Component[] h6 = {vb(k6)};
     verificatieBox = hb(h6);
     verificatieBox.setMaximumSize(new Dimension(240,300));
     
   //plaats componenten vormbox
     Component[] r81 = {titleVormLabel,      hgl()};
     Component[] r82 = {ra(0,110),           vormEditorPanel};
     
     Component[] k8 = {hb(r81), vst(5), hb(r82), vgl()};
     vormBox = vb(k8);
     vormBox.setVisible(false);    
        
  // boxes plaatsen
     Box boxh = Box.createHorizontalBox();
     mainPanel.add(boxh);
     
     Box boxv1 = Box.createVerticalBox();
     boxh.add(ra(20,0));
     boxh.add(boxv1);
     boxh.add(ra(20,0));
     boxh.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, WiskOpdr.colorBlue4));
     
     
     Box boxh2 = Box.createHorizontalBox();
     Box boxh3 = Box.createHorizontalBox();
     
     boxv1.add(boxh2);
     boxv1.add(Box.createVerticalStrut(20));
     boxv1.add(boxh3);
     
     boxh2.add(antwoordBox);
     
     boxh3.add(verificatieBox);
     boxh3.add(Box.createHorizontalGlue());
     //boxh3.add(Box.createHorizontalStrut(5));
     //boxh3.add(scoringBox);
     boxh3.add(Box.createHorizontalStrut(10));
     boxh3.add(vormBox);
     //boxh3.add(Box.createHorizontalGlue());
     boxh3.add(feedbackBox);
   
     setFeedbackOption(false);
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
 
   public JCheckBox makeCheckBox(String text, boolean selected, boolean visible)
   {   JCheckBox checkbox = new WiskOpdrCheckbox(text);
       checkbox.setFont(font);
       checkbox.setOpaque(false);
       checkbox.addActionListener(this);
       checkbox.setSelected(selected);
       checkbox.setVisible(visible);
       return checkbox;
   }
   
   public JLabel makeLabel( String text, boolean visible)
   {   JLabel label = new JLabel(text);
       label.setForeground(WiskOpdr.colorBlue1);
       label.setFont(font);
       label.setVisible(visible);
       return label;
   }
   
   public JTextField makeTextField(String text, boolean visible)
   {   JTextField textField = new WiskOpdrTextField(text);
       textField.setPreferredSize(new Dimension(40,22));
       textField.setMaximumSize(new Dimension(50,22));
       textField.setFont(font);
       textField.addActionListener(this);
       textField.setVisible(visible);
       return textField;
   }

   public JPanel getPanel() {
     return mainPanel;
   }
   
   public Hashtable getEditState()  {
     Hashtable interactiePanelLaunchState = new Hashtable();
     
     String antwoordString = null;
     String vormString = "$f@";
     boolean vorm = false;
     boolean exact = false;
     boolean significant = false;
     int puntenGelijkwaardig = 10;
     int puntenVorm = 0;
     int puntenExact = 0; 
     int puntenSignificant = 0; 
     boolean eindOplossingNodig = true;
     int puntenEindOplossing = 10;
     boolean hasFeedback = false;
     double eqTestValueMin = 0;
     double eqTestValueMax = 5;
     
     Hashtable[] answerModels;
     boolean scoreCumulatief = false;
     
     getAnswerModel();
     answerModels = this.answerModels;
     if(answerModels!=null)setAnswerModel(answerModels[0]);
     
     antwoordString = antwoordvak.geefFormuleVak().toString();
     String[] vormStrings = vormEditor.geefRegels();
     if(vormStrings.length==1) vormString = vormEditor.geefFormuleVak().toString();
     else
     {   vormString = "$f";
         for(int i=0 ; i<vormStrings.length ; i++)
         {   vormString = vormString + vormStrings[i].substring(2,vormStrings[i].length()-1) + "::";
         }
         vormString = vormString.substring(0,vormString.length()-2) + "@";
     }
     vorm = this.vorm;
     exact = this.exact;
     significant = this.significant;
     
     try {
         puntenGelijkwaardig = Integer.parseInt(gelijkwaardigPV.getText());
         this.puntenGelijkwaardig = puntenGelijkwaardig;
     }   
     catch(Exception ex) {}
     try {
         puntenVorm = Integer.parseInt(vormPV.getText());
         this.puntenVorm = puntenVorm;
     }   
     catch(Exception ex) {}
     try {
         puntenExact = Integer.parseInt(exactPV.getText());
         this.puntenExact = puntenExact;
     }   
     catch(Exception ex) {}
     try {
         puntenSignificant = Integer.parseInt(significantPV.getText());
         this.puntenSignificant = puntenSignificant;
     }   
     catch(Exception ex) {}
     try {
         puntenEindOplossing = Integer.parseInt(eindOplossingPV.getText());
         this.puntenEindOplossing = puntenEindOplossing;
     }   
     catch(Exception ex) {}
     
     puntenGelijkwaardig = this.puntenGelijkwaardig;
     puntenVorm = this.puntenVorm;
     puntenExact = this.puntenExact;
     puntenSignificant = this.puntenSignificant;
     eindOplossingNodig = this.eindOplossingNodig;
     puntenEindOplossing = this.puntenEindOplossing;
     hasFeedback = this.hasFeedback;
     
     scoreCumulatief = scoreCumulatiefCB.isSelected();
     interactiePanelLaunchState.put("antwoordString",antwoordString);
     interactiePanelLaunchState.put("vorm",new Boolean(vorm));
     interactiePanelLaunchState.put("exact",new Boolean(exact));
     interactiePanelLaunchState.put("significant",new Boolean(significant));
     interactiePanelLaunchState.put("puntenGelijkwaardig",new Integer(puntenGelijkwaardig));
     interactiePanelLaunchState.put("puntenVorm",new Integer(puntenVorm));
     interactiePanelLaunchState.put("puntenExact",new Integer(puntenExact));
     interactiePanelLaunchState.put("puntenSignificant",new Integer(puntenSignificant));
     interactiePanelLaunchState.put("eindOplossingNodig",new Boolean(eindOplossingNodig));
     interactiePanelLaunchState.put("puntenEindOplossing",new Integer(puntenEindOplossing));
     interactiePanelLaunchState.put("eqTestValueMin",new Double(eqTestValueMin));
     interactiePanelLaunchState.put("eqTestValueMax",new Double(eqTestValueMax));
     interactiePanelLaunchState.put("answerModels",answerModels);
     interactiePanelLaunchState.put("scoreCumulatief",new Boolean(scoreCumulatief));
     interactiePanelLaunchState.put("antwoordSubStrings",antwoordSubStrings);
     interactiePanelLaunchState.put("antwoordFuncStrings",antwoordFuncStrings);
    
     return interactiePanelLaunchState;
   }
   
   public void setEditState(Hashtable interactiePanelLaunchState) {
     String antwoordString = "$f@";
     String vormString = "$f@";
     boolean vorm = false;
     boolean exact = false;
     boolean significant = false;
     int puntenGelijkwaardig = 10;
     int puntenVorm = 0;
     int puntenExact = 0;
     int puntenSignificant = 0;
     boolean eindOplossingNodig = true;
     int puntenEindOplossing = 10;
     boolean hasFeedback = false;
     double eqTestValueMin = 0;
     double eqTestValueMax = 5;
     Hashtable[] answerModels = null;
     boolean scoreCumulatief = false;
     
     if(interactiePanelLaunchState.containsKey("antwoordString")) antwoordString = (String)interactiePanelLaunchState.get("antwoordString");
     if(interactiePanelLaunchState.containsKey("vormString")) vormString = (String)interactiePanelLaunchState.get("vormString");
     if(interactiePanelLaunchState.containsKey("vorm")) vorm = ((Boolean)interactiePanelLaunchState.get("vorm")).booleanValue();
     if(interactiePanelLaunchState.containsKey("exact")) exact = ((Boolean)interactiePanelLaunchState.get("exact")).booleanValue();
     if(interactiePanelLaunchState.containsKey("significant")) significant = ((Boolean)interactiePanelLaunchState.get("significant")).booleanValue();
     if(interactiePanelLaunchState.containsKey("puntenGelijkwaardig")) puntenGelijkwaardig = ((Integer)interactiePanelLaunchState.get("puntenGelijkwaardig")).intValue();
     if(interactiePanelLaunchState.containsKey("puntenVorm")) puntenVorm = ((Integer)interactiePanelLaunchState.get("puntenVorm")).intValue();
     if(interactiePanelLaunchState.containsKey("puntenExact")) puntenExact = ((Integer)interactiePanelLaunchState.get("puntenExact")).intValue();
     if(interactiePanelLaunchState.containsKey("puntenSignificant")) puntenSignificant = ((Integer)interactiePanelLaunchState.get("puntenSignificant")).intValue();
     if(interactiePanelLaunchState.containsKey("eindOplossingNodig")) eindOplossingNodig = ((Boolean)interactiePanelLaunchState.get("eindOplossingNodig")).booleanValue();
     if(interactiePanelLaunchState.containsKey("puntenEindOplossing")) puntenEindOplossing = ((Integer)interactiePanelLaunchState.get("puntenEindOplossing")).intValue();
     if(interactiePanelLaunchState.containsKey("hasFeedback")) hasFeedback = ((Boolean)interactiePanelLaunchState.get("hasFeedback")).booleanValue();
     if(interactiePanelLaunchState.containsKey("eqTestValueMin")) eqTestValueMin = ((Double)interactiePanelLaunchState.get("eqTestValueMin")).doubleValue();
     if(interactiePanelLaunchState.containsKey("eqTestValueMax")) eqTestValueMax = ((Double)interactiePanelLaunchState.get("eqTestValueMax")).doubleValue();
     if(interactiePanelLaunchState.containsKey("answerModels")) answerModels = (Hashtable[])interactiePanelLaunchState.get("answerModels");
     if(interactiePanelLaunchState.containsKey("scoreCumulatief")) scoreCumulatief = ((Boolean)interactiePanelLaunchState.get("scoreCumulatief")).booleanValue();
     if(interactiePanelLaunchState.containsKey("antwoordSubStrings")) antwoordSubStrings = (String[])interactiePanelLaunchState.get("antwoordSubStrings");
     if(interactiePanelLaunchState.containsKey("antwoordFuncStrings")) antwoordFuncStrings = (String[])interactiePanelLaunchState.get("antwoordFuncStrings");
     
     this.vorm = vorm;
     this.exact = exact;
     this.significant = significant;
     this.puntenGelijkwaardig = puntenGelijkwaardig;
     this.puntenVorm = puntenVorm;
     this.puntenExact = puntenExact;
     this.puntenSignificant = puntenSignificant;
     this.eindOplossingNodig = eindOplossingNodig;
     this.puntenEindOplossing = puntenEindOplossing;
     
     
     if(answerModels != null) {   
       this.answerModels = new Hashtable[answerModels.length];
         for(int i=0 ; i<answerModels.length ; i++)
         {   this.answerModels[i] = answerModels[i];
         }
     }
     
     this.eqTestValueMin = eqTestValueMin;
     this.eqTestValueMax = eqTestValueMax;
     this.hasFeedback = hasFeedback;

     scoreCumulatiefCB.setSelected(scoreCumulatief);
     
     if(hasFeedback) {
       aantalAnswerModels = answerModels.length;
       antwoordEditorPanel.remove(tabbladTab);
       tabbladTab = new OpdrachtNrRij(aantalAnswerModels, 250,20);
       tabbladTab.setTab(true);
       tabbladTab.setScoresVisible(false);
       tabbladTab.setSize(tabbladTab.getSize().width, 23);
       tabbladTab.addActionListener(this);
       tabbladTab.setBackground(new Color(210,210,210));
       tabbladTab.setSelected(answerModelNr+1);
       antwoordEditorPanel.add(tabbladTab,0);
       aantalTabsKnop.setLocation(250+25*aantalAnswerModels+5 ,24);
       
       answerModelNr = 0;
       setAnswerModel();
       antwoordEditorPanel.setPreferredSize(new Dimension(Math.max(250+25*aantalAnswerModels+40,450),140));
     }
     
     antwoordvak.geefFormuleVak().vulVak(antwoordString);
     
     String[] vormStrings = StringUtils.split(vormString, "::");
     for(int i=0 ; i<vormStrings.length ; i++)
     {   if(i==0) vormStrings[i] = vormStrings[i] + "@";
         else if(i==vormStrings.length-1) vormStrings[i] = "$f" + vormStrings[i];
         else  vormStrings[i] = "$f" + vormStrings[i] + "@";
     }
     vormEditor.zetRegels(vormStrings);
     
     setFeedbackOption(hasFeedback);
     feedbackCB.setSelected(hasFeedback);
     
     if(hasFeedback) {
       ((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)mainPanel)).pack();
       return;
     }
     
     eindOplossingCB.setSelected(eindOplossingNodig);
     eindOplossingPV.setVisible(eindOplossingNodig);
     eindOplossingPV.setText(""+puntenEindOplossing);
         
     gelijkwaardigPV.setText(""+puntenGelijkwaardig);
                     
     exactCB.setSelected(exact);
     exactPV.setVisible(exact);
     exactPV.setText(""+puntenExact);
     
     significantCB.setSelected(significant);
     significantPV.setVisible(significant && significantieAan);
     significantPV.setText(""+puntenSignificant);
     
                    
     vormCB.setSelected(vorm);
     vormPV.setVisible(vorm);
     vormPV.setText(""+puntenVorm);
         
     vormEditor.setVisible(vorm);
     vormBox.setVisible(vorm);
         
     exactCB.setSelected(exact);
     exactPV.setVisible(exact);
     exactPV.setText(""+puntenExact);
  
   }
   
   public int getScoreMax() {
     int scoreMax = puntenGelijkwaardig + puntenVorm + puntenEindOplossing + puntenExact;
     if(hasFeedback) {
       scoreMax = puntenFeedback;
       if(scoreCumulatiefCB.isSelected()) {
           scoreMax = 0;
           for(int i=0 ; i<answerModels.length ; i++) {
               scoreMax += (Integer)answerModels[i].get("puntenFeedback");
           }
       }
     }
      return scoreMax;
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
            tabPositieKnop.setLocation(246+25*answerModelNr+5 ,0);
        }
        
    }
    else if(e.getSource()==feedbackCB)
    {   setFeedbackOption(feedbackCB.isSelected());
        if(berekeningVakEditPanel!=null)
          berekeningVakEditPanel.pack();
        if(strategieVakEditPanel!=null)
          strategieVakEditPanel.pack();
    }
    else if(e.getSource() == tabPositieKnop)
    {   if(e.getActionCommand().equals("plus") && answerModelNr<aantalAnswerModels-1) 
        {   resAnswerModel = new Hashtable();
            fillAnswerModel(resAnswerModel);
            answerModels[answerModelNr] = answerModels[answerModelNr+1];
            answerModels[answerModelNr+1] = resAnswerModel;
            answerModelNr++;
            tabbladTab.setSelected(answerModelNr+1);
            tabPositieKnop.setLocation(246+25*answerModelNr+5 ,0);
        }
        if(e.getActionCommand().equals("min") && answerModelNr>0) 
        {   resAnswerModel = new Hashtable();
            fillAnswerModel(resAnswerModel);
            answerModels[answerModelNr] = answerModels[answerModelNr-1];
            answerModels[answerModelNr-1] = resAnswerModel;
            answerModelNr--;
            tabbladTab.setSelected(answerModelNr+1);
            tabPositieKnop.setLocation(246+25*answerModelNr+5 ,0);
        }
        
    }
    else if(e.getSource() == aantalTabsKnop)
    {   if(e.getActionCommand().equals("min") && aantalAnswerModels>1)
        {   //remove(opdrContainers[activiteitNr][aantalOpdrachten[activiteitNr]-1]);
            //opdrContainers[activiteitNr][aantalOpdrachten[activiteitNr]-1] = null;
            aantalAnswerModels--;
            if(answerModelNr>aantalAnswerModels-1) answerModelNr--;
            setAnswerModel();
            aantalTabsKnop.setLocation(250+25*aantalAnswerModels+5 ,24);
            antwoordEditorPanel.remove(tabbladTab);
            tabbladTab = new OpdrachtNrRij(aantalAnswerModels, 250,20);
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
            //repaint();
            
        }
        if(e.getActionCommand().equals("plus") && aantalAnswerModels<20)
        {   aantalAnswerModels++;
            aantalTabsKnop.setLocation(250+25*aantalAnswerModels+5 ,24);
            antwoordEditorPanel.remove(tabbladTab);
            tabbladTab = new OpdrachtNrRij(aantalAnswerModels, 250,20);
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
            //repaint();
        }
        antwoordEditorPanel.setPreferredSize(new Dimension(Math.max(250+25*aantalAnswerModels+40,450),140));
        //((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)mainPanel)).pack();
    }
    else if(e.getSource()==gelijkwaardigCB)
    {   gelijkwaardig = gelijkwaardigCB.isSelected();
        
    }
    else if(e.getSource()==vormCB)
    {   boolean b = vormCB.isSelected();
        vorm = b; 
        if(!hasFeedback && answerModelNr==0)vormPV.setVisible(b);
          vormEditor.setVisible(b);
        vormBox.setVisible(b);
        if(b)
        {   eindOplossingNodig = false;
            eindOplossingCB.setSelected(false);
            eindOplossingPV.setText("0");
            eindOplossingPV.setVisible(false);
            puntenEindOplossing = 0;
            
            exact = false;
            exactCB.setSelected(false);
            exactPV.setText("0");
            exactPV.setVisible(false);
            puntenExact = 0;
            
            gelijkwaardigPV.setText("0");
            puntenGelijkwaardig = 0;
            
            vormPV.setText("10");
            //vormPV.setVisible(true);
            puntenVorm = 10;
            
        }
        else
        {
            gelijkwaardigPV.setText("10");
            puntenGelijkwaardig = 10;
        }
        /*
        if(b && !eindOplossingNodig)
        {   gelijkwaardigPV.setText("0");
            puntenGelijkwaardig = 0;
            vormPV.setText("10");
            puntenVorm = 10;
        }
        if(!b && !eindOplossingNodig)
        {   gelijkwaardigPV.setText("10");
            puntenGelijkwaardig = 10;
            vormPV.setText("0");
            puntenVorm = 0;
        }
        */
        ((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)mainPanel)).pack();
        
    }
    else if(e.getSource()==exactCB)
    {   boolean b = exactCB.isSelected();
        exact = b;
            
        if(!hasFeedback && answerModelNr==0) {
          exactPV.setVisible(b);
          verificatieBox.validate();
      }
        
        if(b)
        {   eindOplossingNodig = true;
            eindOplossingCB.setSelected(true);
            if(!hasFeedback)eindOplossingPV.setVisible(true);
            
            vorm = false;
            vormCB.setSelected(false);
            vormPV.setVisible(false);
            vormEditor.setVisible(false);
            vormBox.setVisible(false);
            
            gelijkwaardigPV.setText("0");
            vormPV.setText("0");
            eindOplossingPV.setText("0");
            exactPV.setText("10");
            
            puntenEindOplossing = 0;
            puntenGelijkwaardig = 0;
            puntenExact = 10;
        }
        else
        {   
            gelijkwaardigPV.setText("0");
            vormPV.setText("0");
            eindOplossingPV.setText("10");
            exactPV.setText("0");
        
            puntenGelijkwaardig = 0;
            puntenVorm = 0;
            puntenEindOplossing = 10;
            puntenExact = 0;
        }   
    }
    else if(e.getSource()==significantCB)
    {
        boolean b = significantCB.isSelected();
        significant = b;
        if(!hasFeedback && answerModelNr==0) {
          significantPV.setVisible(b);
          verificatieBox.validate();
      }
        if(b)
        {   eindOplossingNodig = true;
            eindOplossingCB.setSelected(true);
            if(!hasFeedback)eindOplossingPV.setVisible(true);
            eindOplossingPV.setText("10");
            puntenEindOplossing = 10;
        }
    }
    else if(e.getSource()==eindOplossingCB)
    {   boolean b = eindOplossingCB.isSelected();
        eindOplossingNodig = b;
        if(!hasFeedback && answerModelNr==0) {
          eindOplossingPV.setVisible(b);
          verificatieBox.validate();
        }
        if(b)
        {   vorm = false;
            vormCB.setSelected(false);
            vormPV.setVisible(false);
            vormEditor.setVisible(false);
            vormBox.setVisible(false);
            gelijkwaardigPV.setText("0");
            vormPV.setText("0");
            eindOplossingPV.setText("10");
            exactPV.setText("0");
            
            puntenGelijkwaardig = 0;
            puntenVorm = 0;
            puntenEindOplossing = 10;
            puntenExact = 0;
        }
        else
        {   exactCB.setSelected(false);
            exactPV.setVisible(false);
            
            significantCB.setSelected(false);
            significantPV.setVisible(false);
            
            gelijkwaardigPV.setText("10");
            if(vorm)
            {   gelijkwaardigPV.setText("0");
                vormPV.setText("10");
                puntenGelijkwaardig = 0;
                puntenVorm = 10;
            }
            else
            {   gelijkwaardigPV.setText("10");
                vormPV.setText("0");
                puntenGelijkwaardig = 10;
                puntenVorm = 0;
            }
            eindOplossingPV.setText("0");
            exactPV.setText("0");
            significantPV.setText("0");
            
            puntenEindOplossing = 0;
            puntenExact = 0;
            puntenSignificant = 0;
        }
    }
    
  }
  
  public void setFeedbackOption(boolean b)
  {
      hasFeedback = b;
      tabbladTab.setVisible(b);
      feedbackEditor.setVisible(b);
      if(feedbackBox!=null)
        feedbackBox.setVisible(b);
      //feedbackSizeCB.setVisible(b);
      //feedbackLabel.setVisible(b);
      aantalTabsKnop.setVisible(b);
      tabPositieKnop.setVisible(b);
      feedbackPV.setVisible(b);
      goedFoutIP.setVisible(b);
      if(scoringBox!=null)
        scoringBox.setVisible(b);
      titleVerificatieScoreLabel.setVisible(!b);
      //puntenLabel.setVisible(!b);
      
      gelijkwaardigPV.setVisible(!b);
      if(b || vorm)vormPV.setVisible(!b);
      if(b || eindOplossingNodig) eindOplossingPV.setVisible(!b);
      if(b || exact)exactPV.setVisible(!b);
      if(b || significant && significantieAan) significantPV.setVisible(!b);
      
      if(!b)
          scoreCumulatiefCB.setSelected(false);
      
      //eindOplossingCB.setVisible(!b);
      //vormCB.setVisible(b);
      
      answerModelNr = 0;
      tabbladTab.setSelected(answerModelNr+1);
      if(b)setAnswerModel();
  }
  
  private Hashtable fillAnswerModel(Hashtable h)
  {
      String antwoordString = "$f@";
      boolean gelijkwaardig = true;
      boolean vorm = false;
      boolean eindOplossingNodig = true;
      boolean exact = false;
      boolean significant = false;
      int puntenFeedback = 0;
      String feedback = "";
      int feedbackWidth = 200;
      int feedbackHeight = 20;
      String vormString = "$f@";
      int goedHalfFout = 3;
      
      antwoordString = antwoordvak.geefFormuleVak().toString();
      gelijkwaardig = this.gelijkwaardig;
      vorm = this.vorm;
      eindOplossingNodig = this.eindOplossingNodig;
      exact = this.exact;
      significant = this.significant;
      
              
      puntenFeedback = (Integer.parseInt(feedbackPV.getText()));
      feedback  = feedbackEditor.getText();
      if(feedbackEditorPopupFrame!=null)
      {   feedbackWidth = feedbackEditorPopupFrame.getSize().width - feedbackEditorPopupFrame.getInsets().left - feedbackEditorPopupFrame.getInsets().right;
          feedbackHeight = feedbackEditorPopupFrame.getSize().height - feedbackEditorPopupFrame.getInsets().top - feedbackEditorPopupFrame.getInsets().bottom;
      }
      //vormString = vormEditor.geefFormuleVak().toString();
      String[] vormStrings = vormEditor.geefRegels();
      if(vormStrings.length==1) vormString = vormEditor.geefFormuleVak().toString();
      else
      {   vormString = "$f";
          for(int i=0 ; i<vormStrings.length ; i++)
          {   vormString = vormString + vormStrings[i].substring(2,vormStrings[i].length()-1) + "::";
          }
          vormString = vormString.substring(0,vormString.length()-2) + "@";
      }
      goedHalfFout = goedFoutIP.geefKeuze()-1;
      
      h.put("antwoordString",antwoordString);
      h.put("gelijkwaardig",new Boolean(gelijkwaardig));
      h.put("vorm",new Boolean(vorm));
      h.put("eindOplossingNodig",new Boolean(eindOplossingNodig));
      h.put("exact",new Boolean(exact));
      h.put("significant",new Boolean(significant));
      h.put("puntenFeedback",new Integer(puntenFeedback));
      h.put("feedback",feedback);
      h.put("feedbackWidth",new Integer(feedbackWidth));
      h.put("feedbackHeight",new Integer(feedbackHeight));
      h.put("vormString",vormString);
      h.put("goedHalfFout",new Integer(goedHalfFout));
      
      return h;
  }
  
  private void setAnswerModel(Hashtable h)
  {   String antwoordString = "$f@";
      boolean gelijkwaardig = true;
      boolean vorm = false;
      boolean eindOplossingNodig = true;
      boolean exact = false;
      boolean significant = false;
      int puntenFeedback = 0;
      String feedback = "";
      int feedbackWidth = 0;
      int feedbackHeight = 0;
      String vormString = "$f@";
      int goedHalfFout = 3;
      
      if(h!=null) 
      {   if(h.containsKey("antwoordString")) antwoordString = (String)h.get("antwoordString");
          if(h.containsKey("gelijkwaardig")) gelijkwaardig = ((Boolean)h.get("gelijkwaardig")).booleanValue();
          if(h.containsKey("vorm")) vorm = ((Boolean)h.get("vorm")).booleanValue();
          if(h.containsKey("eindOplossingNodig")) eindOplossingNodig = ((Boolean)h.get("eindOplossingNodig")).booleanValue();
          if(h.containsKey("exact")) exact = ((Boolean)h.get("exact")).booleanValue();
          if(h.containsKey("significant")) significant = ((Boolean)h.get("significant")).booleanValue();
          if(h.containsKey("puntenFeedback")) puntenFeedback = ((Integer)h.get("puntenFeedback")).intValue();
          if(h.containsKey("feedback")) feedback = (String)h.get("feedback");
          if(h.containsKey("feedbackWidth")) feedbackWidth = ((Integer)h.get("feedbackWidth")).intValue();
          if(h.containsKey("feedbackHeight")) feedbackHeight = ((Integer)h.get("feedbackHeight")).intValue();
          if(h.containsKey("vormString")) vormString = (String)h.get("vormString");
          if(h.containsKey("goedHalfFout")) goedHalfFout = ((Integer)h.get("goedHalfFout")).intValue();
          
      }
      this.vorm = vorm;
      this.gelijkwaardig = gelijkwaardig;
      this.eindOplossingNodig = eindOplossingNodig;
      this.exact = exact;
      this.significant = significant;
      this.puntenFeedback = puntenFeedback;
      
      antwoordvak.geefFormuleVak().vulVak(antwoordString);
      
      String[] vormStrings = StringUtils.split(vormString, "::");
      for(int i=0 ; i<vormStrings.length ; i++)
      {   if(i==0) vormStrings[i] = vormStrings[i] + "@";
          else if(i==vormStrings.length-1) vormStrings[i] = "$f" + vormStrings[i];
          else  vormStrings[i] = "$f" + vormStrings[i] + "@";
      }
      vormEditor.verwijderRegels();
      vormEditor.zetRegels(vormStrings);
      
      //vormEditor.geefFormuleVak().vulVak(vormString);
          
      vormCB.setVisible(true);
      exactCB.setVisible(true);
      if(significantieAan) significantCB.setVisible(true);
      
              
      gelijkwaardigCB.setSelected(gelijkwaardig);
      vormCB.setSelected(vorm);
      eindOplossingCB.setSelected(eindOplossingNodig);
      exactCB.setSelected(exact);
      significantCB.setSelected(significant);
      
      
      vormEditor.setVisible(vorm);
      vormBox.setVisible(vorm);
      
     // feedbackPV.setVisible(hasFeedback);
      feedbackPV.setText(""+puntenFeedback);
      
      feedbackEditor.zetTekst(feedback);
      feedbackEditor.layoutTekst();
      feedbackEditor.setEnlargedWidth(feedbackWidth);
      feedbackEditor.setEnlargedHeight(feedbackHeight);
      if(feedbackEditorPopupFrame!=null)
      {   feedbackEditor.setEnlargedSize();
          int width = feedbackWidth + feedbackEditorPopupFrame.getInsets().left + feedbackEditorPopupFrame.getInsets().right;
          int height = feedbackHeight + feedbackEditorPopupFrame.getInsets().top + feedbackEditorPopupFrame.getInsets().bottom;
          feedbackEditorPopupFrame.setSize(width,height);
      }
      feedbackEditor.repaint();
      
      goedFoutIP.setItem(goedHalfFout);
      
      updateFeedbackTitelLabel();
  }
  
  public void setContextVars(String[] subVars, String[] funcVars) {
    this.antwoordSubStrings = subVars;
    this.antwoordFuncStrings = funcVars;
  }
  
  private void getAnswerModel()
  {   if(answerModels==null)return;
      answerModels[answerModelNr] = fillAnswerModel(new Hashtable());
  }
  
  private void setAnswerModel()
  {   if(answerModels==null)return;
      setAnswerModel(answerModels[answerModelNr]);    
  }
  
  private void updateFeedbackTitelLabel()
  {   String feedbackNrString = "";
      if(answerModelNr>0) {
          feedbackNrString += (answerModelNr+1);
          titleFeedbackTekstLabel.setText(WiskOpdr.rb.getString("FEV_titleFeedbackLabel") + " " + feedbackNrString);
          titleFeedbackLabel.setText(WiskOpdr.rb.getString("feedbackLabel") + " " + feedbackNrString);
          titleVerificatieLabel.setText(WiskOpdr.rb.getString("FEV_titleVerificatieLabel") + " " + feedbackNrString);
          titleAntwoordLabel.setText(WiskOpdr.rb.getString("FEV_titleAntwoordNrLabel") + " " + feedbackNrString);
          titleScoreLabel.setText(WiskOpdr.rb.getString("FEV_titleScoringLabel") + " " + feedbackNrString);
          scoringBox.validate();
      }
      else {
          titleFeedbackTekstLabel.setText(WiskOpdr.rb.getString("FEV_titleFeedbackLabel"));
          titleFeedbackLabel.setText(WiskOpdr.rb.getString("feedbackLabel"));
          titleVerificatieLabel.setText(WiskOpdr.rb.getString("FEV_titleVerificatieLabel"));
          titleAntwoordLabel.setText(WiskOpdr.rb.getString("FEV_titleAntwoordLabel"));
          titleScoreLabel.setText(WiskOpdr.rb.getString("FEV_titleScoringLabel") + (!scoreCumulatiefCB.isSelected() ? " max" : " 1"));
          scoringBox.validate();
      }
  }
  
  public class EditorComponentListener implements ComponentListener {

    @Override
    public void componentResized(ComponentEvent e) {
        if(e.getSource()==antwoordEditorPanel) {
            int w = antwoordEditorPanel.getWidth();
            int h = antwoordEditorPanel.getHeight();
            antwoordvak.setBounds(0,20,w,h-20);
        }
        if(e.getSource()==vormEditorPanel) {
            int w = vormEditorPanel.getWidth();
            int h = vormEditorPanel.getHeight();
            vormEditor.setBounds(0,0,w,h);
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
