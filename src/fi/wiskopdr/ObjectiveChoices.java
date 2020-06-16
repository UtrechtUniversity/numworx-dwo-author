package fi.wiskopdr;

import java.awt.Component;
import java.util.List;

public interface ObjectiveChoices {
  boolean[][] getChoices();
  List<String> getObjectives();
  
  void setChoices(boolean[][] choices);
  void setObjectives(List<String> objectives);
  void makeChoices();
  Component makeGUI();
  default void close() {}
}
