package fi.beans.iconan;

import javax.swing.JComponent;

interface Strategy {

  int getWidth(String name);

  int getHeight(String name);

  JComponent getPreviewPanel(String name);
}
