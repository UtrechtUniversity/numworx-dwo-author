package fi.wiskopdr.domainmodel.filter;

import java.awt.Component;

import javax.swing.JTree;

import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.domainmodel.StudentMethod;

public class AnyMethodAction extends MethodeAction {
  
  
  AnyMethodAction() {
    super("");
  }
  AnyMethodAction(boolean b) {
    this();
    readonly = b;
  }   

  AnyMethodAction(Component owner, JTree tree) {
    this(); setOwner(owner); setTree(tree);
  }

  AnyMethodAction(boolean b, Component owner, JTree tree) {
    this(b); setOwner(owner); setTree(tree);
  }


  void setMethode(StudentMethod methode) {
    putValue(NAME, methode.getMethod());
    putValue(KEY,  methode.key());
    KOPPELING_LEERDOEL = "Koppeling leerdoel aan " + methode.getMethod();
    grJaarlagen       = methode.getBooks().toArray(new String[methode.getBooks().size()]);
    aantalHoofdstukken= new int[grJaarlagen.length];
    for (int i = 0; i < aantalHoofdstukken.length; i++) {
      aantalHoofdstukken[i] = methode.getChapters().get(i).size();
    }
  }

  void setMethode(String active) {
    setEnabled(active != null);
    setMethode(WiskOpdr.applet.getStudentMethod(active));
  }
  
//  // hulpje voor constructor;
//  AnyMethodAction init(int i) {
//    setEnabled(i != 0);
//    setMethode(MethodsProperties.instance().get(i));
//    return this;
//  }
  @Override
  public void setEnabled(boolean newValue) {
    // TODO Auto-generated method stub
    super.setEnabled(newValue);
  }

  
}
