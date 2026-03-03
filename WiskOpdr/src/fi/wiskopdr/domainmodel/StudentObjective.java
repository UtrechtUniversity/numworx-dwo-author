package fi.wiskopdr.domainmodel;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StudentObjective {

  public StudentObjective(String objective, String description, String id, Object objectives, String[] voorkennis) {
    this.objective = objective;
    this.description = description;
    this.id = id;
    this.voorkennis = voorkennis;
    if (objectives != null) {
      this.objectives = StudentModel.readObjectives(objectives);
    }
  }
  public String id;
  
  public final String objective;
  public String description;
  public StudentObjective[] objectives; // and so on....
  public String[] voorkennis;
  public Variant[] variants;

  public Integer x,y;

  public Map<String, Map<String, Collection<Number>>> methode;

  public List<DomStudentModelMethodInfo> methodInfo;
}
