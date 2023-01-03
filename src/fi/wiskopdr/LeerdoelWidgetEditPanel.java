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
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import fi.beans.numworxlf.JCheckBox;
import fi.beans.numworxlf.JComboBox;
import fi.beans.numworxlf.JLabel;
import fi.beans.numworxlf.JRadioButton;
import fi.beans.numworxlf.JTextField;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.wiskopdr.domainmodel.StudentModel;
import fi.wiskopdr.domainmodel.filter.FilterPanel;
import fi.wiskopdr.tekstobjects.EditInteractiePanelDialog;

public class LeerdoelWidgetEditPanel extends JPanel implements InteractieEditPanel, ActionListener {

  //Algemene attributen 
  private Font font = new Font("SansSerif",Font.PLAIN,12);//WiskOpdr.tekstFont;

  //Basis GUI
  private JPanel mainPanel;
  private JPanel instellingenPanel;
  
  //Settings
  private JLabel titleSettingsLabel;
  private JComboBox<StudentModel> leerdomeinCombobox;
  private JLabel filterSettingsLabel;
  private JPanel filterPanelContainer;
  private FilterPanel filterPanel;
  
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
    
    // Instellingen
    titleSettingsLabel = new JLabel(WiskOpdr.rb.getString("LWEP_titleSettingsLabel"));
    titleSettingsLabel.setForeground(WiskOpdr.colorBlue1);
    titleSettingsLabel.setFont(font.deriveFont(Font.BOLD, 16));
    
    leerdomeinCombobox = new JComboBox<>(/*WiskOpdr.applet.getStudentModels()*/);
    leerdomeinCombobox.setModel(new DefaultComboBoxModel<>(studentModels));
    leerdomeinCombobox.setSelectedItem(studentModel);
    
    leerdomeinCombobox.setForeground(WiskOpdr.colorBlue1);
    leerdomeinCombobox.setPreferredSize(new Dimension(360,22));
    leerdomeinCombobox.setMaximumSize(new Dimension(360,22));
    leerdomeinCombobox.setMinimumSize(new Dimension(360,22));
    leerdomeinCombobox.addActionListener(this);
    
    filterSettingsLabel = new JLabel(WiskOpdr.rb.getString("LWEP_filterSettingsLabel"));
    filterSettingsLabel.setForeground(WiskOpdr.colorBlue1);
    filterSettingsLabel.setFont(font.deriveFont(Font.BOLD, 16));
    
    filterPanelContainer = new JPanel();
    filterPanelContainer.setLayout(new BorderLayout());
    filterPanelContainer.setPreferredSize(new Dimension(450,300));
    filterPanelContainer.add(filterSettingsLabel, BorderLayout.NORTH);
    
    plaatsGUI();
  }
  
  public void plaatsGUI() {
    
    Component[] r11 = {titleSettingsLabel, hgl()};
    Component[] r12 = {leerdomeinCombobox, hgl()};
    Component[] r13 = {filterPanelContainer, hgl()};
   
    
    Component[] k1 = {hb(r11), vst(10), hb(r12), vst(10), hb(r13), vgl()};
    
    
    
 
    Component[] k2 = {vgl()};
    
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
    String studentModelID = null;
    Map<String,Map<String,Collection<Number>>> filter = null;
    String activeMethod = null;
    
    if(h.containsKey("activeMethod"))
      activeMethod = (String)h.get("activeMethod");
    if(h.containsKey("studentModelID"))
      studentModelID = (String)h.get("studentModelID");
    if(h.containsKey("filter"))
      filter = (Map<String,Map<String,Collection<Number>>>)h.get("filter");
    
    for(StudentModel s: studentModels) {
      if (s != null && s.id .equals(studentModelID)) { studentModel = s; break; }
    }
    leerdomeinCombobox.setSelectedItem(studentModel);
    if(filter!=null)
      if(studentModel!=null && studentModel.activeMethod == activeMethod) {
        filterPanel = new FilterPanel(studentModel.activeMethod);
        //filterPanel.setFilter(filter); // werkt niet. Even aan Wim vragen 
        filterPanelContainer.add(filterPanel);
      }
   
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
   
    if(studentModel!=null) {
      activeMethod = studentModel.activeMethod;
      studentModelID = studentModel.id;
    }
    if(filterPanel!=null)
      filter = filterPanel.getFilter();
        
    Hashtable h = new Hashtable();
    if(studentModelID!=null)
      h.put("studentModelID", studentModelID);
    if(studentModelID!=null)
      h.put("filter", filter);
    if(activeMethod!=null)
      h.put("activeMethod", activeMethod);
   
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
    if(e.getSource().equals(leerdomeinCombobox)) {
      studentModel = (StudentModel) leerdomeinCombobox.getSelectedItem();
      if(studentModel!=null) {
        if(filterPanel!=null) 
          filterPanelContainer.remove(filterPanel);
        filterPanel = new FilterPanel(studentModel.activeMethod);
        filterPanelContainer.add(filterPanel);
      }
    }
    
    ((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(EditInteractiePanelDialog.class,(Component)mainPanel)).packWidth();
    
  }

}
