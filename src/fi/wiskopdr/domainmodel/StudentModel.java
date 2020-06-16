package fi.wiskopdr.domainmodel;

import java.util.Map;

import org.json.fimple.JSONArray;
import org.json.fimple.JSONObject;

import fi.wiskopdr.WiskOpdr;

public class StudentModel {
  public String title;
  public String description;
  public String id;
  public StudentCategory[] categories;
  public String toString() {
      return String.valueOf(title);
  }

  public int getMaxObjectives() {
      int max = 0;
      for(StudentCategory c: categories)
          max = Math.max(max, c.objectives.length);
      return max;
  }
  
  public static StudentModel readModel(Object object) {
    StudentModel result = new StudentModel();
    JSONObject map = (JSONObject) object;
    JSONObject model = (JSONObject) map.get("modelStructure");
    result.title = getTitle(model);
    result.description = getDescription(model);
    result.id = getId(map); // FIXME 
    result.categories = readCategories(model.get("categories"));        
    return result;
}
  private static String getId(JSONObject map) {
    Object id = map.get("id");
    if(id instanceof Map) {
        return (String) ((Map) id).get("idString");
    }
    return null;
}
  private static StudentCategory[] readCategories(Object object) {
    if(object == null) return new StudentCategory[0];
    JSONArray array = (JSONArray) object;
    int size = array.size();
    StudentCategory[] categories = new StudentCategory[size];
    for(int i = 0; i < size; i++)  {
        categories[i] = readStudentCategory(array.get(i));
    }
    return categories;
}
  private static StudentCategory readStudentCategory(Object object) {
    JSONObject map = (JSONObject) object;
    StudentCategory result = new StudentCategory();
    result.category = getTitle(map);
    result.description = getDescription(map);
    result.objectives = readObjectives(map.get("objectives"));
    return result;
}

protected static String getTitle(JSONObject map) {
    return (String) ((Map) ((Map) map.get("info")).get("title")).get(WiskOpdr.language.toString());
}
protected static String getDescription0(JSONObject map) {
  return (String) ((Map) ((Map) map.get("info")).get("description")).get(WiskOpdr.language.toString());
}
protected static String getJSON(JSONObject map) {
  return (String) ((Map) ((Map) map.get("info")).get("description")).get(WiskOpdr.language.toString() + "@JSON");
}

protected static String getDescription(JSONObject map) {
  String test = getDescription0(map);
  if (test == null) return null;
  if (test.startsWith(StudentModelChoicePanel.WISKOPDR_SIG))
    test = getJSON(map);
  else if (test.startsWith("{"))
    test = '\uFEFF' + test; // prefix with BOM
  return test;
}


static StudentObjective[] readObjectives(Object object) {
    if (object == null) return new StudentObjective[0];
    JSONArray array = (JSONArray) object;
    int size = array.size();
    StudentObjective[] result = new StudentObjective[size];
    for (int i = 0; i < size; i++) {
        result[i] = readObjective(array.get(i));
    }
    return result;
}

static StudentObjective readObjective(Object object) {
    JSONObject map = (JSONObject) object;
    return new StudentObjective ( getTitle(map), getDescription(map), getUUID(map), map.get("objectives") );
}

static String getUUID(JSONObject map) {   
  return (String) ((Map) map.get("info")).get("id");
}

}
