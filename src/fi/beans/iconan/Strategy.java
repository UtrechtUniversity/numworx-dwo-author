package fi.beans.iconan;

import javax.swing.Icon;
import javax.swing.JComponent;

interface Strategy {

  int getWidth(String name);

  int getHeight(String name);

  JComponent getPreviewPanel(String name);

  JComponent getComponent(String name);

  Icon getIcon(String name);
  
  default void dispose() { }
}
