package fi.wiskopdr;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JPanel;

import fi.beans.base64code.StringCodeObject;
import fi.beans.numworxlf.JButton;
import fi.beans.numworxlf.JOptionPane;
import fi.wiskopdr.domainmodel.Constants;
import fi.wiskopdr.domainmodel.StudentCategory;
import fi.wiskopdr.domainmodel.StudentModel;
import fi.wiskopdr.domainmodel.StudentObjective;
import fi.wiskopdr.domainmodel.graph.Graph;
import fi.wiskopdr.domainmodel.graph.GraphNode;
import fi.wiskopdr.opdrnav.OpdrNavStructEdit;

@SuppressWarnings("serial")
class ObjectivesViewAction extends AbstractAction {

  private ObjectiveSettingsButton objectivesButton;
  private boolean[][] choices;
  private Set<String> objectives;
  private OpdrNavStructEdit editor;
  private Set<String> voorkennis;


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
      btn.strategy.setObjectives(toList(objectives)); // show exclusief voorkennis, maximum factor
      btn.strategy.setTitle(getValue(Action.NAME).toString());
      Component t = btn.strategy.makeGUI();
      t.setEnabled(false);
//       JButton b = new JButton("Graph");
//     b.addActionListener(ev -> {
//        Graph gr = new Graph();
//        gr.setModel(btn.strategy.getTreeModel(), null);
//        gr.setSize(1000,650);
//        gr.setPreferredSize(new Dimension(1000, 650));
//        ArrayList<GraphNode> nodes = gr.getGraphNodes();
//        Set strip = strip(objectives);
//        for(GraphNode node: nodes) {
//          String id = node.getID();
//          if (strip.contains(id)) node.setSuccesFailScore(100.0);
//          else if (voorkennis.contains(id)) node.setSuccesFailScore(60.0);
//        }
//        
//        JOptionPane.showMessageDialog(b, gr, "Graph", JOptionPane.PLAIN_MESSAGE);
//      });
//      JPanel  p = new JPanel(new BorderLayout());
//      p.add(t, BorderLayout.CENTER);
//      if (btn.strategy.getTreeModel() != null) {
//        Box vb = Box.createHorizontalBox(); vb.add(Box.createGlue()); vb.add(b);
//        p.add(vb, BorderLayout.NORTH);
//      }
      Map<String, Double> scoreMap = new HashMap<>();
      voorkennis.forEach(s -> scoreMap.put(s, 60.0));
      strip(objectives).forEach(s -> scoreMap.put(s, 100.0));
      btn.strategy.setScore(scoreMap);
      
      DialogFacade dialog = DialogFacade.newInstance((Component) e.getSource(), "", true);
      dialog.setContentPane((Container) t);
      dialog.pack();
// center
      Dimension screenSize = WiskOpdr.applet.getToolkit().getScreenSize();
      int x = (screenSize.width-dialog.getSize().width)/2;
      int y = (screenSize.height-dialog.getSize().height)/2;
      dialog.setLocation(x , y);
      dialog.setVisible(true);
      dialog.dispose();
      btn.strategy.close();
    }

  }

  private List<String> toList(Set<String> set) {
    return set.stream().map(o -> o.contains("/")? o: (o+"/1.0")).sorted().distinct().collect(Collectors.toList());
  }


  void buildChoices() {
    String[][] objectives = objectivesButton.getObjectives();
    if (objectives != null) {
      choices = new boolean[objectives.length][];
      for (int i = 0; i < objectives.length; i++) {
        choices[i] = new boolean[objectives[i].length];
    }
    } else {
      choices = new boolean[0][0];
    }
    this.objectives = new TreeSet<>();
    
    Map state = editor.getEditState();
    int aantal = editor.geefAantalOpdrachten(0);
    for (int i = 0; i < aantal; i++) {
      String statei = (String) state.get("opdracht_1_" + (i+1));
      Map mapi = (Map)StringCodeObject.decodeStringToObject(statei);
      int[][] max = (int[][])mapi.get("scoreMaxObjectives");
      if (max != null) updatechoices(max);
      Map[] data = (Map[])mapi.get("interactiePanelLaunchData");
      updatechoices(data);
    }
    this.voorkennis = metVoorkennis(this.objectives, objectivesButton.getStudentModel());
  }

  private Set<String> strip(Collection<String> ids) {
    return ids.stream().map(s -> s.split("/")[0]).collect(Collectors.toSet());
  }

  Set<String> metVoorkennis(Set<String> ids, StudentModel model) {
      if(model == null) return ids;
      ids = strip(ids);
      Map<String,StudentObjective> infos = new TreeMap<>();
      for(StudentCategory item: model.categories) {
        add(item.objectives, infos);
      }
      Set<String> all = new TreeSet<String>(ids);
      Set<String> extra = new TreeSet<>();
      Set<String> work = new TreeSet<>(all);
      while( ! work.isEmpty()) {
        // extra is empty, work is nonempty, work all in "all"
        for (String id : work) {
          StudentObjective info = infos.get(id);
          if (info == null) continue;
          String[] voorkennis = info.voorkennis;
          if (voorkennis == null) continue;
          extra.addAll(strip(Arrays.asList(voorkennis)));
        }
        extra.removeAll(all);
        work.clear();
        work.addAll(extra);
        all.addAll(extra);
        extra.clear();
    }
    return all;
  }

  private void add(StudentObjective[] objs, Map<String, StudentObjective> infos) {
    if (objs == null) return;
    for(StudentObjective obj: objs) {
      add(obj.objectives, infos);
      if (obj.id != null) {
        infos.put(obj.id, obj);
      }    
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
        if (maxi[j]) choicei[j] = true;
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
