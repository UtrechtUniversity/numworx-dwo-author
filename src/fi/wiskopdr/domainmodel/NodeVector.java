package fi.wiskopdr.domainmodel;

import java.util.Vector;

class NodeVector extends Vector {

  private String title;

  public NodeVector(StudentModel model) {
    this.title = model.title;
  }

  public String toString() {
    return title;
  }
}
