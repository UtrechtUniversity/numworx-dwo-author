package nl.numworx.geodefiner.merge;

import java.awt.Container;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.inject.Inject;
import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JTextField;

import dagger.Lazy;
import dagger.Reusable;
import fi.euclides.model.Destroyable;
import fi.euclides.openmath.OMConstants;
import nl.numworx.geodefiner.Definitions;
import nl.numworx.geodefiner.Editor;
import nl.numworx.geodefiner.common.CELL;
import nl.numworx.geodefiner.common.Instance;
import nl.numworx.geodefiner.common.NamingModel;
import nl.numworx.geodefiner.common.UIModel;
import nl.numworx.geodefiner.ui.ColorModel;
import nl.numworx.geodefiner.ui.ColorPane;
import nl.tue.win.riaca.openmath.lang.OMBinding;
import nl.tue.win.riaca.openmath.lang.OMObject;
import nl.tue.win.riaca.openmath.lang.OMVariable;
import nl.uu.fi.dwo.interaction.client.JSONUtilities;

@SuppressWarnings("serial")
public abstract class RenameAction extends AbstractAction {
	
	
	public interface RenamePane {
		void setName(String actionCommand);
		Destroyable getItem();		
	}
	
  public RenameAction() {
    super("rename");
  }
  
  @Inject Lazy<Editor> editor;
  @Inject Lazy<Instance> instance;
  @Inject NamingModel mapper;
  @Inject Definitions definitions;
  
  private RenamePane pane;
  
  @Override
  public void actionPerformed(ActionEvent e) {
	Object source = e.getSource();
	if (source instanceof JTextField && ((JTextField) source).getInputVerifier() != null) {
		JTextField f = (JTextField) source;
		if (! f.getInputVerifier().verify(f)) return;
		String newname = f.getText();
		Destroyable pp = mapper.fromString(newname);
		if (pp != null && pp != pane.getItem())
			return;
			
	}
    pane.setName(e.getActionCommand());
    putValue(NAME,e.getActionCommand());
    Container parent = (Container) pane;
    do { parent = parent.getParent(); }
    while (!(parent instanceof JDialog));
    ((Dialog) parent).setTitle(e.getActionCommand());
  }


  public void doRename() {
    Destroyable p = pane.getItem();
    String oldName = mapper.toString(p);
    String newName = getName();
    Destroyable pp = mapper.fromString(newName);
    if (!oldName .equals( newName) && (pp == null || pp == p))
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
//        OMObject obj = cell.item.adapt(OMObject.class);
//        if (obj != null) {
//        	OMObject obj1 = rename(obj, oldName, newName);
//        	if (obj != obj1) 
//        		DefaultAdapter.getDefault(cell.item).put(OMObject.class, obj1);
//        }
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
      
      List checkObjects = editor.get().getCheckObjects().toList();
      int size = checkObjects.size();
      for(int i = 0; i < size; i++) {
    	  Map obj = (Map) checkObjects.get(i);
    	  String formule = (String) obj.get("value");
    	  String formule2 = rename(formule, map, true);
    	  if (formule != formule2)
    		  obj.put("value",formule2);
      }
      editor.get().getCheckObjects().fromList(JSONUtilities.wrapList(checkObjects));
      editor.get().getCheckObjects().fireTableDataChanged();

      Map<String, ?> data = editor.get().getLaunchData();
      editor.get().setLaunchData(data);      
      editor.get().repaint();         
    }
  }
  
  private String rename(String text, Map<String, String> map, boolean b) {
	return MergeAction.rename(text, map, b);
  }


	private OMObject rename(OMObject obj, String oldname, String newname) {
		OMVariable oldvar = new OMVariable(oldname);
		OMVariable newvar = new OMVariable(newname);
		Vector vars = new Vector();
		vars.add(oldvar);
		OMBinding bind = new OMBinding(OMConstants.FNS1_LAMBDA, vars, obj);
		bind.alphaConvert(oldvar, newvar);
		return bind.getBody();
	}


public RenamePane getPane() {
    return pane;
  }

  public void setPane(RenamePane pane) {
    this.pane = pane;
    String name = mapper.toString(pane.getItem());
    putValue(NAME, name);
    pane.setName(name);
  }

  public String getName() {
    return getValue(NAME).toString();
  }

}
