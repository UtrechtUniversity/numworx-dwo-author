package fi.wiskopdr.domainmodel;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

  public List<String> getVoorkennis() {
    return Arrays.asList(obj.voorkennis);
  }

  public void setX(Integer x) {
    obj.x = x;
    
  }

  public Map<String, Map<String, Collection<Number>>> getMethode() {
    return obj.methode == null ? Collections.emptyMap() : obj.methode;  }

  public List<DomStudentModelMethodInfo> getMethodeInfos() {
    return obj.methodInfo;
  }

  public Integer getX() {
    return obj.x;
  }

  public Integer getY() {
    return obj.y;
  }

  public void setY(Integer y) {
    obj.y = y;    
  }

  public void setVoorkennis(List<String> voorkennis) {
    obj.voorkennis = voorkennis.toArray(new String[voorkennis.size()]);
    
  }

  public void setMethodeInfos(Collection<DomStudentModelMethodInfo> methodeInfos) {
  }

}
