package fi.wiskopdr.domainmodel;

public class NodeLeaf implements Node {

  private boolean value;
  private StudentObjective obj; 
  
  public NodeLeaf(StudentObjective obj) {
    this.obj = obj;
  }  
  
  public String toString() {
    return obj.objective;
  }

  @Override
  public String getDescription() {
    return obj.description;
  }

  public boolean isValue() {
    return value;
  }

  public void setValue(boolean value) {
    this.value = value;
  }

  public String getId() {
    return obj.id;
  }

}
