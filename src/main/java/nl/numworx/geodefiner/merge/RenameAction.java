package nl.numworx.geodefiner.merge;

import java.awt.Container;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import dagger.Lazy;
import dagger.Reusable;
import fi.euclides.event.NameMapper;
import fi.euclides.formuleobjects.Token;
import fi.euclides.model.Destroyable;
import nl.numworx.geodefiner.Definitions;
import nl.numworx.geodefiner.Editor;
import nl.numworx.geodefiner.common.CELL;
import nl.numworx.geodefiner.common.CheckObject;
import nl.numworx.geodefiner.common.CheckObjectList;
import nl.numworx.geodefiner.common.Instance;
import nl.numworx.geodefiner.common.NamingModel;
import nl.numworx.geodefiner.common.UIModel;
import nl.numworx.geodefiner.ui.ColorModel;
import nl.numworx.geodefiner.ui.ColorPane;
import nl.uu.fi.dwo.interaction.client.JSONUtilities;

import static nl.numworx.geodefiner.merge.MergeAction.rename;

@SuppressWarnings("serial")
@Reusable
public class RenameAction extends AbstractAction {

  @Inject public RenameAction() {
    super("rename");
  }
  
  @Inject Lazy<Editor> editor;
  @Inject Lazy<Instance> instance;
  @Inject NamingModel mapper;
  @Inject Definitions definitions;
  
  private ColorPane<?> pane;
  
  @Override
  public void actionPerformed(ActionEvent e) {
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

        if (cell.config != null) {
        	UIModel<?, ?> config = cell.config;
        	Map<String, Object> cfg = config.toMap();
        	if (cfg.containsKey(ColorModel.VISIBILITY)) {
        		String visibility = (String) cfg.get(ColorModel.VISIBILITY);
        		String visibility2 = rename(visibility, map, true);
        		if (visibility != visibility2) {
        			cfg.put(ColorModel.VISIBILITY, visibility2);
        			config.fromMap(JSONUtilities.wrapMap(cfg));
        		}        		
        	}
        	
        }
        if (cell.item == p) {
          cell.text = rename(cell.text, map, false);
          cell.var = newName;
          definitions.update(cell);
        } else {
          cell.text = rename(cell.text, map, false);
          definitions.update(cell);        
        }       
      }
      
      Map<String, Object> checkDWO = editor.get().getCheckDWO().toMap();
      checkDWO.put("formule", rename((String) checkDWO.get("formule"), map, true));
      editor.get().getCheckDWO().fromMap(JSONUtilities.wrapMap(checkDWO));
      
      CheckObjectList checkObjects = instance.get().checkObjects;
      int size = checkObjects.getSize();
      for(int i = 0; i < size; i++) {
    	  CheckObject obj = checkObjects.getElementAt(i);
    	  String formule = obj.getFormule();
    	  String formule2 = rename(formule, map, true);
    	  if (formule != formule2)
    		  obj.setFormule(formule2);
      }
      editor.get().getCheckObjects().fireTableDataChanged();
      
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
