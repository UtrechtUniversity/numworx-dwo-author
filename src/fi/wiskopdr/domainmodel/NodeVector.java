package fi.wiskopdr.domainmodel;

import java.util.Enumeration;
import java.util.Vector;

class NodeVector extends Vector<Node> implements Node {

  private String title, description;

  public NodeVector(StudentModel model) {
    this.title = model.title;
    this.description = model.description;
    for (StudentCategory cat : model.categories) {
      addElement(new NodeVector(cat));
    }
  }

  NodeVector(StudentCategory cat) {
    this.title = cat.category;
    this.description = cat.description;
    StudentObjective[] objectives = cat.objectives;
    addChildren(objectives);
  }

  private void addChildren(StudentObjective[] objectives) {
    for (StudentObjective obj: objectives) {
      if (obj.objectives == null) {
        addElement(new NodeLeaf(obj));
      } else {
        addElement(new NodeVector(obj));
      }
    }
  }

  public NodeVector(StudentObjective obj) {
    this.title = obj.objective;
    this.description = obj.description;
    addChildren(obj.objectives);
  }

  public String toString() {
    return title;
  }
  
  public String getDescription() {
    return description;
  }

  @Override
  public boolean isValue() {
    Enumeration<Node> e = elements();
    while (e.hasMoreElements()) {
      Node node = (Node) e.nextElement();
      if (node.isValue()) return true;      
    }
    return false;
  }
}
