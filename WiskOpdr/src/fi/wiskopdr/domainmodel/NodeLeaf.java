package fi.wiskopdr.domainmodel;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class NodeLeaf implements Node {

  private boolean value;
  private String variant;
  void setVariant(String variant) {
    this.variant = variant;
  }

  private StudentObjective obj; 
  private NodeLeaf delegate = this;
  
  public NodeLeaf(StudentObjective obj) {
    this.obj = obj;
  }  

  public NodeLeaf(StudentObjective obj, String variant) {
    this.obj = obj;
    this.variant = variant;
  }  
  
  public String toString() {
    return obj.objective;
  }
  
  public NodeLeaf(NodeLeaf org, String variant) {
    this.obj = org.obj;
    this.variant = variant;
    this.value = org.value;
    delegate = org;
    value = delegate.value && Objects.equals( delegate.variant, this.variant);
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
    if (value) delegate.variant = variant;
    else if (Objects.equals(delegate.variant, this.variant))
      delegate.value = false;
  }

  public String getId() {
    return obj.id;
  }

  public List<String> getVoorkennis() { // FIXME houd rekening met variant!
    List<String> result = Arrays.asList(obj.voorkennis);
    if (variant != null) {
      Optional<Variant> v = Arrays.stream(obj.variants).filter(t -> variant.equals(t.name)).findAny();
      v=v.filter (x -> x.deselections != null);
      v.ifPresent(x-> result.removeAll((Collection<?>) x.deselections));
    }
    return result;
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

  public String getVariant() {
    return variant;
  }

  
  
}
