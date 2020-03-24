package nl.numworx.geodefiner.merge;

import java.awt.Container;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import javax.inject.Inject;
import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import dagger.Lazy;
import dagger.Reusable;
import fi.euclides.event.NameMapper;
import fi.euclides.model.Destroyable;
import nl.numworx.geodefiner.Definitions;
import nl.numworx.geodefiner.Editor;
import nl.numworx.geodefiner.common.CELL;
import nl.numworx.geodefiner.common.NamingModel;
import nl.numworx.geodefiner.ui.ColorModel;
import nl.numworx.geodefiner.ui.ColorPane;

@Reusable
public class RenameAction extends AbstractAction {

  @Inject public RenameAction() {
    super("rename");
  }
  
  @Inject Lazy<Editor> editor;
  @Inject NamingModel mapper;
  @Inject Definitions definitions;
  
  private ColorPane<?> pane;
  
  @Override
  public void actionPerformed(ActionEvent e) {
    System.err.println(e.getActionCommand());
    pane.setName(e.getActionCommand());
    putValue(NAME,e.getActionCommand());
    Container parent = (Container) pane;
    do { parent = parent.getParent(); }
    while (!(parent instanceof JDialog));
    ((Dialog) parent).setTitle(e.getActionCommand());
  }


  public void doRename() {
    Destroyable p = pane.model.item;
    String oldName = mapper.toString(p);
    String newName = getName();
    if (!oldName .equals( newName) )
    {
      mapper.rename(p, newName);
      Iterator<CELL> iter = definitions.elements();
      Map<String,String> map = Collections.singletonMap(oldName, newName);
      while (iter.hasNext()) {
        CELL cell = iter.next();
        if (cell.item == p) {
          System.err.println(cell.text);
          cell.text = MergeAction.rename(cell.text, map);
          cell.var = newName;
          definitions.update(cell);
        } else {
          cell.text = MergeAction.rename(cell.text, map);
          definitions.update(cell);        
        }       
      }
      editor.get().repaint();         
    }
  }
  
  public ColorPane<?> getPane() {
    return pane;
  }

  public void setPane(ColorPane<?> pane) {
    this.pane = pane;
    ColorModel<? extends Destroyable> model = pane.model;
    String name = mapper.toString(model.item);
    putValue(NAME, name);
    pane.setName(name);
  }

  public String getName() {
    return getValue(NAME).toString();
  }

}
