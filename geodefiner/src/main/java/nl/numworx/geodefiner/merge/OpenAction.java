package nl.numworx.geodefiner.merge;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.inject.Inject;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

import dagger.Lazy;
import nl.numworx.geodefiner.Editor;

@SuppressWarnings("serial")
public class OpenAction extends AbstractAction implements Constants {
  private final Logger LOG = Logger.getLogger(getClass().getName());

  @Inject OpenAction() {
    super("Open...");
  }

  @Inject JFileChooser chooser;
  @Inject Lazy<Editor> editor;
  
  @SuppressWarnings("unchecked")
  @Override
  public void actionPerformed(ActionEvent e) {
    Component parent = (Component) e.getSource();
    if (JFileChooser.APPROVE_OPTION == chooser.showOpenDialog(parent)) {
      File f = chooser.getSelectedFile();
      try {
        ZipFile in = new ZipFile(f);
        ZipEntry entry = in.getEntry(CONTENTS);
        ObjectInputStream dis = new ObjectInputStream(in.getInputStream(entry));
        Object o = dis.readObject();
        dis.close();
        editor.get().setLaunchData((Map<String, ?>) o);
        in.close();
      } catch (IOException | ClassNotFoundException e1) {
        LOG.log(Level.SEVERE, "open selected file " + f, e1);
      }
    }
  }

}
