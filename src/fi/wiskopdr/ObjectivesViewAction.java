package fi.wiskopdr;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.AbstractAction;

import fi.beans.base64code.StringCodeObject;
import fi.beans.numworxlf.JOptionPane;
import fi.wiskopdr.domainmodel.Constants;
import fi.wiskopdr.opdrnav.OpdrNavStructEdit;

@SuppressWarnings("serial")
class ObjectivesViewAction extends AbstractAction {

  private ObjectiveSettingsButton objectivesButton;
  private boolean[][] choices;
  private Set<String> objectives;
  private OpdrNavStructEdit editor;


  public ObjectivesViewAction(String name, ObjectiveSettingsButton objectivesButton, OpdrNavStructEdit editor) {
    super(name);
    this.objectivesButton = objectivesButton;
    this.editor = editor;
  }


  public boolean hasObjectiveChoices() {
    return objectivesButton.getObjectives() !=  null || objectivesButton.getStudentModelID() != null; 
  }


  @Override
  public void actionPerformed(ActionEvent e) {
    if (hasObjectiveChoices()) {
      @SuppressWarnings("deprecation")
      ObjectiveChoiceButton btn = new ObjectiveChoiceButton(objectivesButton.getObjectives(), objectivesButton.getCategories(), objectivesButton.getStudentModel());
      buildChoices();
      btn.strategy.setChoices(choices);
      btn.strategy.setObjectives(new ArrayList<>(objectives));
      Component t = btn.strategy.makeGUI();
      JOptionPane.showMessageDialog((Component) e.getSource(), t);
    }

  }

  void buildChoices() {
    String[][] objectives = objectivesButton.getObjectives();
    choices = new boolean[objectives.length][];
    for (int i = 0; i < objectives.length; i++) {
      choices[i] = new boolean[objectives[i].length];
    }
    this.objectives = new TreeSet<>();
    
    Map state = editor.getEditState();
    int aantal = editor.geefAantalOpdrachten(0);
    for (int i = 0; i < aantal; i++) {
      String statei = (String) state.get("opdracht_1_" + (i+1));
      Map mapi = (Map)StringCodeObject.decodeStringToObject(statei);
      int[][] max = (int[][])mapi.get("scoreMaxObjectives");
      updatechoices(max);
      Map[] data = (Map[])mapi.get("interactiePanelLaunchData");
      updatechoices(data);
    }
    
  }


  private void updatechoices(Map[] data) {
    for (int i = 0; i < data.length; i++) {
      Map map = data[i];
      if(map != null) updatechoices(map);    
    }
  }


  private void updatechoices(Map map) {
    Map state = (Map) map.get("interactiePanelLaunchState");
    Object data = state.get("interactiePanelLaunchData");
    if (data instanceof Map[]) updatechoices( (Map[])data);
    boolean[][] choices = (boolean[][]) state.get("logObjectives");
    if (choices != null) updatechoices(choices);
    String[] list = (String[]) state.get(Constants.OBJECTIVES);
    if (list != null) {
      for (int i = 0; i < list.length; i++) {
        String string = list[i];
        objectives.add(string);       
      }
    }
  }


  private void updatechoices(boolean[][] max) {
    for(int i = 0; i < Math.min(choices.length, max.length); i++) {
      boolean choicei[] = choices[i];
      boolean[] maxi = max[i];
      int limit = Math.min(choicei.length, maxi.length);
      for(int j = 0; j < limit; j++) {
        if (maxi[j]!= false) choicei[j] = true;
      }
    }
    
  }


  private void updatechoices(int[][] max) {
    for(int i = 0; i < Math.min(choices.length, max.length); i++) {
      boolean choicei[] = choices[i];
      int[] maxi = max[i];
      int limit = Math.min(choicei.length, maxi.length);
      for(int j = 0; j < limit; j++) {
        if (maxi[j]!= 0) choicei[j] = true;
      }
    }
    
  }

}
