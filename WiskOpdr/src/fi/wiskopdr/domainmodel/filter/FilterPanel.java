package fi.wiskopdr.domainmodel.filter;

import java.awt.Font;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import fi.beans.numworxlf.JCheckBox;
import fi.wiskopdr.WiskOpdr;

public class FilterPanel extends JPanel {

 // MethodeAction mw = new AnyMethodAction().init(2);
 // MethodeAction genr = new AnyMethodAction().init(1);
  
  AnyMethodAction m;
  JCheckBox rest = new JCheckBox("Niet geclassificeerde leerdoelen");
  

//  KoppelingGRPanel genrtab = genr.getTab();
//  KoppelingGRPanel mwtab = mw.getTab();
  KoppelingGRPanel mtab;

  
  
  public FilterPanel(String activeMethod) {
    super(null);
    BoxLayout layout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
    setLayout(layout);
    Border margin = BorderFactory.createEmptyBorder(20, 20, 5, 0);
    JLabel l;
    if (activeMethod != null) {
      m = new AnyMethodAction();
      m.setMethode(WiskOpdr.applet.getStudentMethod(activeMethod));
      l = new JLabel("Actieve methode: " + m.getName());
      l.setForeground(WiskOpdr.colorBlue1);
      l.setFont(new Font("SansSerif", Font.PLAIN, 14));
      l.setBorder(margin);
      add(l);
      add((mtab = m.getTab()).getMainPanel());
    } else {
//      l = new JLabel(genr.getName());
//      l.setBorder(margin);
//      add(l);
 //     add(genrtab);
 //     l = new JLabel(mw.getName());
 //     l.setBorder(margin);
//      add(l);
//      add( mwtab);
    }
    l = new JLabel("Alle leerdoelen");
    l.setForeground(WiskOpdr.colorBlue1);
    l.setFont(new Font("SansSerif", Font.PLAIN, 14));
    l.setBorder(margin);
    add(l);
    rest.setBorder(margin);
    add(rest);
    
  }

   public Map<String,Map<String,Collection<Number>>> getFilter() {
//      Map<String, Set<Integer>> mwmap = mw.getMethodMap(mwtab);
//      Map<String, Set<Integer>> genrmap = genr.getMethodMap(genrtab);
      Map<String,Map<String,Collection<Number>>> filter = new HashMap<>();
      Map<String, Collection<Number>> mwmap;
      if (m != null) {
        mwmap = m.getMethodMap(mtab);
        if (!mwmap.isEmpty()) filter.put(m.getKey(), mwmap);
      } else {
//        if (!mwmap.isEmpty())   filter.put(mw.getKey(), mwmap);
//        if (!genrmap.isEmpty()) filter.put(genr.getKey(), genrmap);
      }
      if (rest.isSelected()) filter.put(null,null);
      return filter;
    }
  
   public  void setFilter(Map<String, Map<String, Collection<Number>>> filter) {
      rest.setSelected(filter.containsKey(null));
      if (m != null) {
        Map<String, Collection<Number>> mwmap = filter.getOrDefault(m.getKey(), Collections.emptyMap());
        m.setMethodMap(mtab, mwmap);
      } else {
//        Map<String, Set<Integer>> mwmap = filter.getOrDefault(mw.getKey(), Collections.emptyMap());
//        mw.setMethodMap(mwtab, mwmap);
//        Map<String, Set<Integer>> genrmap = filter.getOrDefault(genr.getKey(), Collections.emptyMap());
//        genr.setMethodMap(genrtab, genrmap);
      }
    }
}
