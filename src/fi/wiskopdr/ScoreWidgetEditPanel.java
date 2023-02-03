package fi.wiskopdr;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import fi.beans.numworxlf.JCheckBox;
import fi.beans.numworxlf.JLabel;
import fi.beans.numworxlf.JRadioButton;
import fi.beans.numworxlf.JTextField;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.wiskopdr.tekstobjects.EditInteractiePanelDialog;

public class ScoreWidgetEditPanel extends JPanel implements InteractieEditPanel, ActionListener {

  public static int THIS_ACTIVITY = 0;
  public static int THIS_MODULE = 1;
  public static int ACTIVITY_ID = 2;
  public static int MODULE_ID = 3;
  //Algemene attributen 
  private Font font = new Font("SansSerif",Font.PLAIN,12);//WiskOpdr.tekstFont;

  //Basis GUI
  private JPanel mainPanel;
  private JPanel instellingenPanel;
  
  //Settings
  private JLabel titleSettingsLabel;
  private JRadioButton thisActiviteitRB;
  private JRadioButton thisModuleRB;
  private JRadioButton activiteitIdRB;
  private JRadioButton moduleIdRB;
  private ButtonGroup buttonGroup1;
  private JLabel activiteitNrLabel;
  private JLabel paginaNrLabel;
  private JLabel paginaTitelLabel;
  private JTextField activiteitNrTF;
  private JTextField paginaNrTF;
  private JTextField paginaTitelTF;
  private JTextField activiteitIdTF;
  private JTextField moduleIdTF;
  
  private JLabel titleKeuzeLabel;
  private JCheckBox scoreRB;
  private JCheckBox goedFoutRB;
  private JCheckBox bezochtRB;
  private JCheckBox toonTitelCB;
  
  private JLabel cesuurLabel;
  private JTextField cesuurTF;

  
  private JLabel titleLinkLabel;
  private JCheckBox linkActiveCB;
  
  public ScoreWidgetEditPanel() {
    makeGUI();
  }
  
  private void makeGUI() {
    mainPanel = new JPanel(new BorderLayout());
    mainPanel.setBackground(WiskOpdr.colorGray3);
    
    instellingenPanel = new JPanel(null);
    instellingenPanel.setPreferredSize(new Dimension(300,451));
    instellingenPanel.setMaximumSize(new Dimension(300,451));
    instellingenPanel.setLayout(new BorderLayout());
    
    // Instellingen
    titleSettingsLabel = new JLabel(WiskOpdr.rb.getString("SWEP_titleSettingsLabel"));
    titleSettingsLabel.setForeground(WiskOpdr.colorBlue1);
    titleSettingsLabel.setFont(font.deriveFont(Font.BOLD, 16));
    
    thisActiviteitRB = new JRadioButton(WiskOpdr.rb.getString("SWEP_thisActiviteitButton"));
    thisActiviteitRB.addActionListener(this);
    thisActiviteitRB.setSelected(true);
    
    thisModuleRB = new JRadioButton(WiskOpdr.rb.getString("SWEP_thisModuleButton"));
    thisModuleRB.addActionListener(this);
    thisModuleRB.setSelected(false);
    
    activiteitIdRB = new JRadioButton(WiskOpdr.rb.getString("SWEP_activiteitIdButton"));
    activiteitIdRB.addActionListener(this);
    activiteitIdRB.setSelected(true);
    
    moduleIdRB = new JRadioButton(WiskOpdr.rb.getString("SWEP_moduleIdButton"));
    moduleIdRB.addActionListener(this);
    moduleIdRB.setSelected(true);
    
    buttonGroup1 = new ButtonGroup();
    buttonGroup1.add(thisActiviteitRB);
    buttonGroup1.add(thisModuleRB);
    buttonGroup1.add(activiteitIdRB);
    buttonGroup1.add(moduleIdRB);
    
    activiteitIdTF = new JTextField("");
    activiteitIdTF.setFont(font);
    activiteitIdTF.setPreferredSize(new Dimension(60,22));
    activiteitIdTF.setMaximumSize(new Dimension(60,22));
    
    moduleIdTF = new JTextField("");
    moduleIdTF.setFont(font);
    moduleIdTF.setPreferredSize(new Dimension(60,22));
    moduleIdTF.setMaximumSize(new Dimension(60,22));
    
    activiteitNrLabel = new JLabel(WiskOpdr.rb.getString("SWEP_activiteitNrLabel"));
    activiteitNrLabel.setForeground(WiskOpdr.colorBlue1);
    activiteitNrLabel.setFont(font);
    activiteitNrLabel.setVisible(false);
    
    activiteitNrTF = new JTextField("");
    activiteitNrTF.setFont(font);
    activiteitNrTF.setPreferredSize(new Dimension(50,22));
    activiteitNrTF.setMaximumSize(new Dimension(50,22));
    activiteitNrTF.setVisible(false);
    
    paginaNrLabel = new JLabel(WiskOpdr.rb.getString("SWEP_paginaNrLabel"));
    paginaNrLabel.setForeground(WiskOpdr.colorBlue1);
    paginaNrLabel.setFont(font);
    
    paginaTitelLabel = new JLabel(WiskOpdr.rb.getString("SWEP_paginaTitelLabel"));
    paginaTitelLabel.setForeground(WiskOpdr.colorBlue1);
    paginaTitelLabel.setFont(font);
    
    paginaNrTF = new JTextField("");
    paginaNrTF.setFont(font);
    paginaNrTF.setPreferredSize(new Dimension(50,22));
    paginaNrTF.setMaximumSize(new Dimension(50,22));
    
    paginaTitelTF = new JTextField("");
    paginaTitelTF.setFont(font);
    paginaTitelTF.setPreferredSize(new Dimension(150,22));
    paginaTitelTF.setMaximumSize(new Dimension(150,22));
    
    titleKeuzeLabel = new JLabel(WiskOpdr.rb.getString("SWEP_titleKeuzeLabel"));
    titleKeuzeLabel.setForeground(WiskOpdr.colorBlue1);
    titleKeuzeLabel.setFont(font.deriveFont(Font.BOLD, 16));
    
    goedFoutRB = new JCheckBox(WiskOpdr.rb.getString("SWEP_goedFoutButton"));
    goedFoutRB.addActionListener(this);
    goedFoutRB.setSelected(true);
    
    cesuurLabel = new JLabel(WiskOpdr.rb.getString("SWEP_cesuurLabel"));
    cesuurLabel.setForeground(WiskOpdr.colorBlue1);
    cesuurLabel.setFont(font);
   
    cesuurTF = new JTextField("");
    cesuurTF.setFont(font);
    cesuurTF.setPreferredSize(new Dimension(50,22));
    cesuurTF.setMaximumSize(new Dimension(50,22));
     
    scoreRB = new JCheckBox(WiskOpdr.rb.getString("SWEP_scoreButton"));
    scoreRB.setSelected(false);
    
    toonTitelCB = new JCheckBox(WiskOpdr.rb.getString("SWEP_toonTitelCB"));
    toonTitelCB.setSelected(true);
    
    bezochtRB = new JCheckBox(WiskOpdr.rb.getString("SWEP_bezochtButton"));
    bezochtRB.setSelected(false);
    
    titleLinkLabel = new JLabel(WiskOpdr.rb.getString("SWEP_titleLinkLabel"));
    titleLinkLabel.setForeground(WiskOpdr.colorBlue1);
    titleLinkLabel.setFont(font.deriveFont(Font.BOLD, 16));
    
    linkActiveCB = new JCheckBox(WiskOpdr.rb.getString("SWEP_linkActiveCB"));
    linkActiveCB.setSelected(true);
    
    scoreRB.setSelected(false);
    
    plaatsGUI();
  }
  
  public void plaatsGUI() {
    
    Component[] r11 = {titleSettingsLabel, hgl()};
    Component[] r12 = {thisActiviteitRB, hgl()};
    Component[] r13 = {thisModuleRB, hgl()};
    Component[] r14 = {activiteitIdRB, ra(10,0), activiteitIdTF, hgl()};
    Component[] r15 = {moduleIdRB, ra(10,0), moduleIdTF, hgl()};
    Component[] r16 = {activiteitNrLabel, ra(10,0), activiteitNrTF, hgl()};
    Component[] r17 = {paginaNrLabel, ra(10,0), paginaNrTF, hgl()};
    Component[] r18 = {paginaTitelLabel, ra(10,0), paginaTitelTF, hgl()};
    
    Component[] k1 = {hb(r11), vst(10), hb(r12), vst(5), hb(r13), vst(5), hb(r14), vst(5), hb(r15), 
                      vst(10), hb(r16), vst(5), hb(r17), vst(5), hb(r18), vgl()};
    
    Component[] r21 = {titleKeuzeLabel, hgl()};
    Component[] r22 = {goedFoutRB, hgl()};
    Component[] r22a = {cesuurLabel, ra(10,0), cesuurTF, hgl()};
    Component[] r22b= {bezochtRB, hgl()};
    Component[] r23 = {scoreRB, hgl()};
    Component[] r24 = {toonTitelCB, hgl()};
    Component[] r25 = {titleLinkLabel, hgl()};
    Component[] r26 = {linkActiveCB, hgl()};
 
    Component[] k2 = {hb(r21), vst(10), hb(r22), vst(5), hb(r22a), vst(5), hb(r22b), vst(5),hb(r23), vst(5), hb(r24),vst(20), hb(r25), vst(10), hb(r26),vgl()};
    
    Component[] rr = {vb(k1), hgl(), ra(100,0), vb(k2), hgl()};
    
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
  
  @Override
  public void setEditState(Hashtable h) {
    int choicePageMode = 0;
    int activiteitNr = 0;
    int paginaNr = 0;
    String paginaTitel = "";
    int activiteitID = 0;
    int moduleID = 0;
    boolean score = false;
    boolean goedFout = true;
    boolean bezocht = false;
    boolean toonTitel = true;
    boolean linkActive = true;
    int cesuur = -1;
    
    if(h.containsKey("choicePageMode"))
      choicePageMode = (int)h.get("choicePageMode");
    if(h.containsKey("activiteitNr"))
      activiteitNr = (int)h.get("activiteitNr");
    if(h.containsKey("paginaTitel"))
      paginaTitel = (String)h.get("paginaTitel");
    if(h.containsKey("paginaNr"))
      paginaNr = (int)h.get("paginaNr");
    if(h.containsKey("activiteitID"))
      activiteitID = (int)h.get("activiteitID");
    if(h.containsKey("moduleID"))
      moduleID = (int)h.get("moduleID");
    if(h.containsKey("score"))
      score = (boolean)h.get("score");
    if(h.containsKey("goedFout"))
      goedFout = (boolean)h.get("goedFout");
    if(h.containsKey("bezocht"))
      bezocht = (boolean)h.get("bezocht");
    if(h.containsKey("toonTitel"))
      toonTitel = (boolean)h.get("toonTitel");
    if(h.containsKey("linkActive"))
      linkActive = (boolean)h.get("linkActive");
    if(h.containsKey("cesuur"))
      cesuur = (int)h.get("cesuur");
    
    thisActiviteitRB.setSelected(choicePageMode==0);
    thisModuleRB.setSelected(choicePageMode==1);
    activiteitIdRB.setSelected(choicePageMode==2);
    moduleIdRB.setSelected(choicePageMode==3);
    
    scoreRB.setSelected(score);
    goedFoutRB.setSelected(goedFout);
    toonTitelCB.setSelected(toonTitel);
    bezochtRB.setSelected(bezocht);
    
    linkActiveCB.setSelected(linkActive);
    
    paginaNrTF.setText(""+paginaNr);
    paginaTitelTF.setText(""+paginaTitel);
    activiteitNrTF.setText(""+activiteitNr);
    activiteitIdTF.setText(""+activiteitID);
    moduleIdTF.setText(""+moduleID);
    
    if(thisActiviteitRB.isSelected() || activiteitIdRB.isSelected()) {
      activiteitNrLabel.setVisible(false);
      activiteitNrTF.setVisible(false);
    }
    else {
      activiteitNrLabel.setVisible(true);
      activiteitNrTF.setVisible(true);
    }
    
    cesuurLabel.setVisible(goedFout);
    if(cesuur > -1)
      cesuurTF.setText("" + cesuur);
    cesuurTF.setVisible(goedFout);
    
    ((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)mainPanel)).packWidth();
    
  }

  private int getChoicePageMode() {
    int choicePageMode = 0;
    if(thisModuleRB.isSelected())
      choicePageMode = 1;
    if(activiteitIdRB.isSelected())
      choicePageMode = 2;
    if(moduleIdRB.isSelected())
      choicePageMode = 3;
    return choicePageMode;
  }
  
  private int intFromText(int defaultInt, String text) {
    int i = defaultInt;
    try {
      i = Integer.parseInt(text);
    }
    catch(NumberFormatException e) {
    }
    return i;
  }
  
  @Override
  public Hashtable getEditState() {
   int choicePageMode = 0;
   int activiteitNr = 1;
   int paginaNr = 1;
   String paginaTitel = "";
   int activiteitID = 0;
   int moduleID = 0;
   boolean score = false;
   boolean goedFout = true;
   boolean bezocht = false;
   boolean toonTitel = true;
   boolean linkActive = true;
   int cesuur = -1;
   
   choicePageMode = getChoicePageMode();
   paginaNr = intFromText(paginaNr, paginaNrTF.getText());
   paginaTitel = paginaTitelTF.getText();
   toonTitel = toonTitelCB.isSelected();
   if(choicePageMode!=0 && choicePageMode!=2)
     activiteitNr = intFromText(activiteitNr, activiteitNrTF.getText());
   if(choicePageMode==2)
     activiteitID = intFromText(activiteitID, activiteitIdTF.getText());
   if(choicePageMode==3)
     moduleID = intFromText(moduleID, moduleIdTF.getText());
    
    score = scoreRB.isSelected();
    goedFout = goedFoutRB.isSelected();
    linkActive = linkActiveCB.isSelected();
    bezocht = bezochtRB.isSelected();
    cesuur = intFromText(-1, cesuurTF.getText());
    
    Hashtable h = new Hashtable();
    h.put("choicePageMode", new Integer(choicePageMode));
    h.put("activiteitNr", new Integer(activiteitNr));
    h.put("paginaNr", new Integer(paginaNr));
    h.put("paginaTitel", paginaTitel);
    h.put("activiteitID", new Integer(activiteitID));
    h.put("moduleID", new Integer(moduleID));
    h.put("score", new Boolean(score));
    h.put("goedFout", new Boolean(goedFout));
    h.put("bezocht", Boolean.valueOf(bezocht));
    h.put("toonTitel", new Boolean(toonTitel));
    h.put("linkActive", new Boolean(linkActive));
    if(cesuur>-1)
      h.put("cesuur", new Integer(cesuur));
    
    return h;
    
  }

  @Override
  public void zetBreedte(int b) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void zetHoogte(int h) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void stop() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void start() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if(thisActiviteitRB.isSelected() || activiteitIdRB.isSelected()) {
      activiteitNrLabel.setVisible(false);
      activiteitNrTF.setVisible(false);
    }
    else {
      activiteitNrLabel.setVisible(true);
      activiteitNrTF.setVisible(true);
    }
    if(e.getSource()==goedFoutRB) {
      cesuurLabel.setVisible(goedFoutRB.isSelected());
      cesuurTF.setVisible(goedFoutRB.isSelected());
    }
    ((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)mainPanel)).packWidth();
    
  }

}
