package fi.wiskopdr.domainmodel;

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
  
  public String objective;
  public String description;
  public StudentObjective[] objectives; // and so on....
  public String[] voorkennis;
}
