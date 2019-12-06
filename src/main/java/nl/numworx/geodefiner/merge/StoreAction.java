package nl.numworx.geodefiner.merge;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

import dagger.Lazy;
import nl.numworx.geodefiner.Editor;

import javax.inject.*;

@SuppressWarnings("serial")
public class StoreAction extends AbstractAction implements Constants {
  private final Logger LOG = Logger.getLogger(getClass().getName());

  @Inject StoreAction() {
    super("Safe...");
  }

  @Inject JFileChooser chooser;
  @Inject Lazy<Editor> editor;

  @Override
  public void actionPerformed(ActionEvent e) {    
    Component parent = (Component) e.getSource();
    if (JFileChooser.APPROVE_OPTION == chooser.showSaveDialog(parent)) {
      File f = chooser.getSelectedFile();
      try {
        FileOutputStream out = new FileOutputStream(f);
        ZipOutputStream  zip = new ZipOutputStream(out);
        ZipEntry entry = new ZipEntry(CONTENTS);
        zip.putNextEntry(entry);
        ObjectOutputStream dos = new ObjectOutputStream(zip);
        dos.writeObject(editor.get().getLaunchData());        
        dos.flush();
        zip.closeEntry();
        zip.close();
      } catch (IOException e1) {
        LOG.log(Level.SEVERE, "safe selected file " + f, e1);
      }      
    }

  }

}
