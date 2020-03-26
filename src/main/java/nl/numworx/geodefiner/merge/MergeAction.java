package nl.numworx.geodefiner.merge;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import dagger.Lazy;
import fi.beans.numworxlf.JRadioButton;
import fi.beans.numworxlf.JTextField;
import fi.euclides.formuleobjects.FormuleParser;
import fi.euclides.formuleobjects.ParseException;
import fi.euclides.formuleobjects.Token;
import fi.euclides.model.Model;
import fi.euclides.openmath.OMConstants;
import nl.numworx.geodefiner.Editor;
import nl.tue.win.riaca.openmath.lang.OMApplication;
import nl.tue.win.riaca.openmath.lang.OMObject;
import nl.tue.win.riaca.openmath.lang.OMVariable;

import javax.inject.*;

@SuppressWarnings("serial")
public class MergeAction extends AbstractAction implements Constants {

  enum QueryType { REPLACE, KEEP, RENAME };
    
  static interface Query {
    String name();
    QueryType ask(String name);
  }
  
  
  static class Ask implements Query {

    QueryType type;
    Component parent;
    JTextField name;
    JRadioButton replace, keep, rename;
    ButtonGroup group;
    
    public Ask(Component parent) {
      type = QueryType.KEEP;
      this.parent = parent;
      group = new ButtonGroup();
      replace = new JRadioButton("replace"); group.add(replace);
      keep = new JRadioButton("keep", true); group.add(keep);
      rename = new JRadioButton("rename"); group.add(rename);      
    }

    @Override
    public String name() {
      return name.getText();
    }

    @Override
    public QueryType ask(String name) {
      this.name = new JTextField(name);
      // TODO ask user to keep/rename/replace name
      Box message = Box.createVerticalBox();
      message.add(keep); keep.setAlignmentX(Component.LEFT_ALIGNMENT);
      Box h = Box.createHorizontalBox();
      h.add(rename); h.add(this.name);
      h.setAlignmentX(Component.LEFT_ALIGNMENT);
      message.add(h);
      replace.setAlignmentX(Component.LEFT_ALIGNMENT);
      message.add(replace);
      JOptionPane.showMessageDialog(parent, message, "?", JOptionPane.PLAIN_MESSAGE);
      if (rename.isSelected()) return QueryType.RENAME;
      if (replace.isSelected()) return QueryType.REPLACE;
      return type;
    }

  }

  private static final Logger LOG = Logger.getLogger(MergeAction.class.getName());

  @Inject MergeAction() {
    super("Merge...");
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
        in.close();
        if (o instanceof Map) {
          Map<String, ?> data = editor.get().getLaunchData();
          data = merge(data, (Map<String, ?>) o, new Ask(parent));
          editor.get().setLaunchData(data);
        }
      } catch (IOException | ClassNotFoundException e1) {
        LOG.log(Level.SEVERE, "open selected file " + f, e1);
      }
    }

  }

  @SuppressWarnings("unchecked")
  Map<String, ?> merge(Map<String,?> org, Map<String, ?> merge, Query query) {
    Map<String, String> rename;
    Collection<String> orderOrg, orderMerge;
    orderOrg = new HashSet<>((List<String>) org.get("order"));
    orderMerge = (List<String>) merge.get("order");
    rename = new LinkedHashMap<>();
    for(String item: orderMerge) {
      if (orderOrg.contains(item)) {
        switch(query.ask(suggest(item, orderOrg, rename.values()))) {
          case KEEP: /*rename.put(item, null);*/ break;
          case REPLACE: rename.put(item, item); break;
          case RENAME: rename.put(item, query.name());
        }} else {
          rename.put(item, item);
        }
      }
    org = new TreeMap<>(org);
    List<String> defMerge = (List<String>) merge.get("definitions");
    List<String> defOrg = (List<String>) org.get("definitions");
    Map<String,Map<String,Object>> configOrg   = (Map<String,Map<String,Object>>) org.get("configuration");
    Map<String,Map<String,Object>> configMerge = (Map<String,Map<String,Object>>) merge.get("configuration");
 
    Map<String,Object> posOrg   = (Map<String,Object>) org.get("positions");
    Map<String,Object> posMerge = (Map<String,Object>) merge.get("positions");
    
    Map<String,String> mapMerge = new LinkedHashMap<>();
    for(String item: defMerge) {
      String key = key(item);
      if (rename.containsKey(key))
        mapMerge.put(key, rename(item, rename));
    }
    for(String item: mapMerge.keySet()) {
      String newItem = rename.get(item);
      String def = mapMerge.get(item);
      remove(defOrg, newItem);
      if(def != null) {
        defOrg.add(def);
        Map<String, Object> config = configMerge.get(item);
        if(config != null) {
          // TODO rename visibility
          configOrg.put(newItem, config);
        } else configOrg.remove(newItem);
        Object position = posMerge.get(item);
        if (position != null) {
          posOrg.put(newItem, position);
        } else posOrg.remove(newItem);        
      }
    }
    return org;
  }

  private String suggest(String item, Collection<String> orderOrg, Collection<String> values) {
	int l = item.length();
	while(l > 0 && Character.isDigit(item.charAt(l-1))) l--;
	String pre = item.substring(0, l);
	String post = item.substring(l);
	if (post.isEmpty()) post = "1";
	int n = Integer.parseInt(post);
	Set<String> items = new TreeSet<>(values); items.addAll(orderOrg);
	do { 
		n++;
		item = pre + n;
	} while(items.contains(item));
	return item;
}

private void remove(List<String> defOrg, String item) {
    Iterator<String> i = defOrg.iterator();
    while (i.hasNext()) {
      String string = (String) i.next();
      String key = key(string);
      if (key.equals(item)) i.remove();
      else {
        Set<String> vars = vars(string);
        if(vars.contains(item)) i.remove();
      }
    }
    
  }

  private Set<String> vars(String string) {
    // TODO zie LocusModelIF.varsof
    return Collections.emptySet();
  }

  static String rename(String item, Map<String, String> rename) {
    FormuleParser p = new FormuleParser(item.substring(2));
    try {
      List<Token> t = p.tokens();
      StringBuilder sb = new StringBuilder(item.length()+10);
      sb.append("$f");
      for(Token i: t) {
        String s = i.image;
        if (i.kind == FormuleParser.VARIABLE)
          s = rename.getOrDefault(s, s);
        else if(i.kind == FormuleParser.STRING)
          sb.append('"'); // prefix "
        sb.append(s);
      }
      return sb.append('@').toString();
    } catch (ParseException e) {
      LOG.warning(e.toString());
    }
    return item;
  }

  private String key(String item) {
    try {
      OMObject object = new FormuleParser(item.substring(2)).parse();
      if(object instanceof OMApplication) {
        OMApplication oma = (OMApplication) object;
        OMObject first = oma.firstElement();
        if( first.isSame(OMConstants.PROG1_ASSIGN))
        {
            OMVariable var = (OMVariable) oma.getElementAt(1);
            return var.getName();
        }
      }
    } catch (ParseException e) {
      LOG.warning(e.toString());
    }

    return item;
  }
}
