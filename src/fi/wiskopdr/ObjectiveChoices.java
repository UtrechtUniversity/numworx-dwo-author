package fi.wiskopdr;

import java.awt.Component;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.swing.tree.TreeModel;

public interface ObjectiveChoices {
  boolean[][] getChoices();
  List<String> getObjectives();
  
  void setChoices(boolean[][] choices);
  void setObjectives(List<String> objectives);
  void makeChoices();
  Component makeGUI();
  default void close() {}
  TreeModel getTreeModel();
  void setTitle(String title);
  default void setScore(Map<String, Double> scoreMap) {}
  default void setSelection(Map<String, Boolean> selectionMap) {}
  default List<String> getDeselections() { return Collections.emptyList(); }
  default void setDeselections(List<String> deselections) { }
}
