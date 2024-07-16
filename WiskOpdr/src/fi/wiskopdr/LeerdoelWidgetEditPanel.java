package fi.wiskopdr;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.json.fimple.JSONObject;

import fi.beans.numworxlf.JCheckBox;
import fi.beans.numworxlf.JComboBox;
import fi.beans.numworxlf.JLabel;
import fi.beans.numworxlf.JRadioButton;
import fi.beans.numworxlf.JTabbedPane;
import fi.beans.numworxlf.JTextField;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.wiskopdr.domainmodel.StudentModel;
import fi.wiskopdr.domainmodel.StudentModelChoicePanel;
import fi.wiskopdr.domainmodel.filter.FilterPanel;
import fi.wiskopdr.tekstobjects.EditInteractiePanelDialog;

public class LeerdoelWidgetEditPanel extends JPanel implements InteractieEditPanel, ActionListener {

  //Algemene attributen 
  private Font font = WiskOpdr.tekstFont;

  //Basis GUI
  private JPanel mainPanel;
  private JPanel instellingenPanel;
  
  //Filter
  private JLabel titleSettingsLabel;
  private JComboBox<StudentModel> leerdomeinCombobox;
  private JLabel filterSettingsLabel;
  private JTabbedPane filterPanelContainer;
  private FilterPanel filterPanel;
  private StudentModelChoicePanel modelPanel;
  
  // type
  private JLabel typeLabel;
  
  //settings
  private JLabel settingsLabel;
  private JCheckBox leerdoelPopupCB;
  private JCheckBox voorkennisKnopCB;
  private JCheckBox voorkennisMenuCB;
  private JCheckBox zoomKnoppenCB;
  private JCheckBox filterHeaderCB;
  private JComboBox<String> typeCB;
  
  private JLabel scoreLabel;
  private JCheckBox leerdoelScoreCB;
  
  private StudentModel[] studentModels;
  private StudentModel studentModel;
  
  
  public LeerdoelWidgetEditPanel() {
    studentModels = WiskOpdr.applet.getStudentModels();
    makeGUI();
  }
  
  private void makeGUI() {
    mainPanel = new JPanel(new BorderLayout());
    mainPanel.setBackground(WiskOpdr.colorGray3);
    
    instellingenPanel = new JPanel(null);
    instellingenPanel.setPreferredSize(new Dimension(300,451));
    instellingenPanel.setMaximumSize(new Dimension(300,451));
    instellingenPanel.setLayout(new BorderLayout());
    
    // Leerdomein, filter
    titleSettingsLabel = new JLabel(WiskOpdr.rb.getString("LWEP_titleSettingsLabel"));
    titleSettingsLabel.setForeground(WiskOpdr.colorBlue1);
    titleSettingsLabel.setFont(font.deriveFont(Font.BOLD, 16));
    
    leerdomeinCombobox = new JComboBox<>(/*WiskOpdr.applet.getStudentModels()*/);
    leerdomeinCombobox.setModel(new DefaultComboBoxModel<>(studentModels));
    leerdomeinCombobox.setSelectedItem(studentModel);
    
    leerdomeinCombobox.setForeground(WiskOpdr.colorBlue1);
    leerdomeinCombobox.setPreferredSize(new Dimension(450,22));
    leerdomeinCombobox.setMaximumSize(new Dimension(450,22));
    leerdomeinCombobox.setMinimumSize(new Dimension(450,22));
    leerdomeinCombobox.addActionListener(this);
    
//    filterSettingsLabel = new JLabel(WiskOpdr.rb.getString("LWEP_filterSettingsLabel"));
//    filterSettingsLabel.setForeground(WiskOpdr.colorBlue1);
//    filterSettingsLabel.setFont(font.deriveFont(Font.BOLD, 16));
//    filterSettingsLabel.setVisible(false);
    
    filterPanelContainer = new JTabbedPane();
    filterPanelContainer.setPreferredSize(new Dimension(450,400));
//    filterPanelContainer.add(filterSettingsLabel, BorderLayout.NORTH);
//    filterPanelContainer.addTab(WiskOpdr.rb.getString("LWEP_filterSettingsLabel"), filterHeaderCB);
    
    // Instellingen
    settingsLabel = new JLabel(WiskOpdr.rb.getString("LWEP_settingsLabel"));
    settingsLabel.setForeground(WiskOpdr.colorBlue1);
    settingsLabel.setFont(font.deriveFont(Font.BOLD, 16));
    
    leerdoelPopupCB = new JCheckBox(WiskOpdr.rb.getString("LWEP_leerdoelPopupCB"));
    leerdoelPopupCB.setSelected(true);
    
    voorkennisKnopCB = new JCheckBox(WiskOpdr.rb.getString("LWEP_voorkennisKnopCB"));
    voorkennisKnopCB.setSelected(false);
    
    voorkennisMenuCB = new JCheckBox(WiskOpdr.rb.getString("LWEP_voorkennisMenuCB"));
    voorkennisMenuCB.setSelected(false);
    
    zoomKnoppenCB = new JCheckBox(WiskOpdr.rb.getString("LWEP_zoomKnoppenCB"));
    zoomKnoppenCB.setSelected(false);
    
    filterHeaderCB = new JCheckBox(WiskOpdr.rb.getString("LWEP_filterHeaderCB"));
    filterHeaderCB.setSelected(false);
    
    scoreLabel = new JLabel(WiskOpdr.rb.getString("LWEP_scoreLabel"));
    scoreLabel.setForeground(WiskOpdr.colorBlue1);
    scoreLabel.setFont(font.deriveFont(Font.BOLD, 16));
    
    leerdoelScoreCB = new JCheckBox(WiskOpdr.rb.getString("LWEP_leerdoelScoreCB"));
    leerdoelScoreCB.setSelected(false);
    
    typeLabel = new JLabel("Type");
    typeLabel.setForeground(WiskOpdr.colorBlue1);
    typeLabel.setFont(font.deriveFont(Font.BOLD, 16));
    String[] items = new String[] { "Graph", "Lijst", "Recommender" }; // FIXME i18n
    typeCB = new JComboBox<String>(items);
    //typeCB.setMaximumSize(typeCB.getPreferredSize());
    typeCB.setForeground(WiskOpdr.colorBlue1);
    typeCB.setPreferredSize(new Dimension(150,22));
    typeCB.setMaximumSize(new Dimension(450,22));
    typeCB.setMinimumSize(new Dimension(150,22));
  
    plaatsGUI();
  }
  
  public void plaatsGUI() {
    
    Component[] r11 = {titleSettingsLabel, hgl()};
    Component[] r12 = {leerdomeinCombobox, hgl()};
    Component[] r13 = {filterPanelContainer, hgl()};
   
    
    Component[] k1 = {hb(r11), vst(10), hb(r12), vst(20), hb(r13), vgl()};
    Component[] r31 = { typeLabel, hgl() };
    Component[] r32 = { typeCB };
    Component[] r21 = {settingsLabel, hgl()};
    Component[] r22 = {leerdoelPopupCB, hgl()};
    Component[] r23 = {voorkennisKnopCB, hgl()};
    Component[] r24 = {voorkennisMenuCB, hgl()};
    Component[] r25 = {zoomKnoppenCB, hgl()};
    Component[] r26 = {filterHeaderCB, hgl()};
    
    Component[] r27 = {scoreLabel, hgl()};
    Component[] r28 = {leerdoelScoreCB, hgl()};
 
    Component[] k2 = {hb(r31), vst(10), hb(r32), vst(20),
                      hb(r21), vst(10), hb(r22), vst(5), hb(r23), vst(5), hb(r24), vst(5), hb(r25), vst(5), hb(r26), vst(20), hb(r27), vst(10), hb(r28), vgl()};
    
    Component[] rr = {vb(k1), hgl(), ra(50,0), vb(k2), hgl()};
    
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
    String studentModelID = null;
    Map<String,Map<String,Collection<Number>>> filter = null;
    String activeMethod = null;
    boolean leerdoelPopup = true;
    boolean voorkennisKnop = false;
    boolean voorkennisMenu = false;
    boolean zoomKnoppen = false;
    boolean filterHeader = false;
    boolean leerdoelScore = false;
    List<String> objectives = Collections.emptyList();
    int type = 0;
    
    if(h.containsKey("activeMethod"))
      activeMethod = (String)h.get("activeMethod");
    if(h.containsKey("studentModelID"))
      studentModelID = (String)h.get("studentModelID");
    if(h.containsKey("filter"))
      filter = (Map<String,Map<String,Collection<Number>>>)h.get("filter");
    if(h.containsKey("leerdoelPopup"))
      leerdoelPopup = ((Boolean)h.get("leerdoelPopup")).booleanValue(); // description popup
    if(h.containsKey("voorkennisKnop"))
      voorkennisKnop = ((Boolean)h.get("voorkennisKnop")).booleanValue();
    if(h.containsKey("voorkennisMenu"))
      voorkennisMenu = ((Boolean)h.get("voorkennisMenu")).booleanValue();
    if(h.containsKey("zoomKnoppen"))
      zoomKnoppen = ((Boolean)h.get("zoomKnoppen")).booleanValue();
    if(h.containsKey("filterHeader"))
      filterHeader = ((Boolean)h.get("filterHeader")).booleanValue(); // header met de filterselectie
    if(h.containsKey("leerdoelScore"))
      leerdoelScore = ((Boolean)h.get("leerdoelScore")).booleanValue();
    if (h.containsKey("type"))
      type = ((Number)h.get("type")).intValue();
    if (h.containsKey("objectives")) 
      objectives = (List<String>) h.get("objectives");
    for(StudentModel s: studentModels) {
      if (s != null && s.id .equals(studentModelID)) { studentModel = s; break; }
    }
    leerdomeinCombobox.setSelectedItem(studentModel);
    if(filter!=null)
      if(studentModel!=null && Objects.equals(studentModel.activeMethod, activeMethod)) {
        if (filterPanel != null) {
          int index = filterPanelContainer.indexOfComponent(filterPanel);
          filterPanelContainer.remove(index);
        }
        
        filterPanel = new FilterPanel(studentModel.activeMethod);
        filterPanel.setFilter(filter); // werkt niet. Even aan Wim vragen 
        filterPanelContainer.addTab(WiskOpdr.rb.getString("LWEP_filterSettingsLabel"), filterPanel);
      }
    if (studentModel != null) {
      if (modelPanel != null) {
        int index = filterPanelContainer.indexOfComponent(modelPanel);
        filterPanelContainer.remove(index);
      }
      modelPanel = new StudentModelChoicePanel(this::supplyModel, false);
      modelPanel.setObjectives(objectives);
      modelPanel.makeGUI();
      filterPanelContainer.addTab("Kies leerdoel", modelPanel);
    }
    leerdoelPopupCB.setSelected(leerdoelPopup);
    voorkennisKnopCB.setSelected(voorkennisKnop);
    voorkennisMenuCB.setSelected(voorkennisMenu);
    zoomKnoppenCB.setSelected(zoomKnoppen);
    filterHeaderCB.setSelected(filterHeader);
    leerdoelScoreCB.setSelected(leerdoelScore);
    typeCB.setSelectedIndex(type);
   
    ((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)mainPanel)).packWidth();
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
    String studentModelID = null;
    Map<String,Map<String,Collection<Number>>> filter = null;
    String activeMethod = null;
    boolean leerdoelPopup = true;
    boolean voorkennisKnop = false;
    boolean voorkennisMenu = false;
    boolean zoomKnoppen = false;
    boolean filterHeader = false;
    boolean leerdoelScore = false;
    List<String> objectives = null;
    int type = 0;
   
    if(studentModel!=null) {
      activeMethod = studentModel.activeMethod;
      studentModelID = studentModel.id;
    }
    if(filterPanel!=null)
      filter = filterPanel.getFilter();
    if (modelPanel != null) {
      modelPanel.makeChoices();
      objectives = modelPanel.getObjectives();
    }
    
    leerdoelPopup = leerdoelPopupCB.isSelected();
    voorkennisKnop = voorkennisKnopCB.isSelected();
    voorkennisMenu = voorkennisMenuCB.isSelected();
    zoomKnoppen = zoomKnoppenCB.isSelected();
    filterHeader = filterHeaderCB.isSelected();
    leerdoelScore = leerdoelScoreCB.isSelected();
    type = typeCB.getSelectedIndex();
        
    Hashtable<String,Object> h = new Hashtable<>();
    if(studentModelID!=null)
      h.put("studentModelID", studentModelID);
    if(filter!=null)
      h.put("filter", filter);
    if(activeMethod!=null)
      h.put("activeMethod", activeMethod);
    h.put("leerdoelPopup", leerdoelPopup);
    h.put("voorkennisKnop", voorkennisKnop);
    h.put("voorkennisMenu", voorkennisMenu);
    h.put("zoomKnoppen", zoomKnoppen);
    h.put("filterHeader", filterHeader);
    h.put("leerdoelScore", leerdoelScore);
    h.put("type", type);
    if (objectives != null) {
      h.put("objectives", objectives);
    }
// extra
    JSONObject p = WiskOpdr.applet.getDwoProfile();
    p = (JSONObject) p.get("id");
    h.put("dwoProfileID", p.get("idString"));
   
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

  private StudentModel supplyModel() {
    return WiskOpdr.applet.expandStudentModel(studentModel);
  }
  
  @Override
  public void actionPerformed(ActionEvent e) {
    if(e.getSource().equals(leerdomeinCombobox)) {
      studentModel = (StudentModel) leerdomeinCombobox.getSelectedItem();
      if(studentModel!=null) {
        if(filterPanel!=null) 
        {
          int index = filterPanelContainer.indexOfComponent(filterPanel);
          filterPanelContainer.remove(index);
        }
        filterPanel = new FilterPanel(studentModel.activeMethod);
//        filterPanelContainer.add(filterPanel);
//        filterSettingsLabel.setVisible(true);
        filterPanelContainer.addTab(WiskOpdr.rb.getString("LWEP_filterSettingsLabel"), filterPanel);
        if (modelPanel != null) {
          int index = filterPanelContainer.indexOfComponent(modelPanel);
          filterPanelContainer.remove(index);
        }
        modelPanel = new StudentModelChoicePanel(this::supplyModel, false);
        modelPanel.makeGUI();
        filterPanelContainer.addTab("Kies leerdoel", modelPanel);   
        filterPanelContainer.invalidate();
        filterPanelContainer.validate();
      }
    }
    
    ((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)mainPanel)).packWidth();
    
  }

}
