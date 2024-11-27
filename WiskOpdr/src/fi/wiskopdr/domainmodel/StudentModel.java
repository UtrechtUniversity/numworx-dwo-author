package fi.wiskopdr.domainmodel;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.fimple.JSONArray;
import org.json.fimple.JSONObject;

import fi.wiskopdr.WiskOpdr;

public class StudentModel {
  public String title;
  public String description;
  public String id;
  public StudentCategory[] categories;
  public String activeMethod;
  
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
    JSONObject info = (JSONObject) model.get("info");
    result.title = getTitle(info);
    result.description = getDescription(info);
    result.id = getId(map); // FIXME 
    result.categories = readCategories(model.get("categories")); 
    result.activeMethod = getMethod(model);
    return result;
}
  private static String getMethod(JSONObject map) {
    Object object = map.get("activeMethod");
    if (object instanceof Map) object = ((Map)object).get("idString");
    if (object instanceof String) return (String) object;
    return null;
  }

  private static String getId(JSONObject map) {
    Object id = map.get("id");
    if(id instanceof Map) {
        return (String) ((Map) id).get("idString");
    }
    return null;
}
  private static StudentCategory[] readCategories(Object object) {
    if(object == null) return null;
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
    JSONObject info = (JSONObject) map.get("info"); 
    StudentCategory result = new StudentCategory();
    result.category = getTitle(info);
    result.description = getDescription(info);
    result.objectives = readObjectives(map.get("objectives"));
    return result;
}

@SuppressWarnings("unchecked")
protected static String getTitle(JSONObject info) {
    try {
      Map<String,String> title = (Map<String,String>) info.get("title");
      String locale = WiskOpdr.language.toString();
      String language = title.getOrDefault(locale,"");
// fallback to en/nl
      if (language.isEmpty())
        language = title.getOrDefault("en", "");
      if (language.isEmpty() || "Untitled".equals(language))
        language = title.getOrDefault("nl", "Untitled");
      return language;
    } catch (Exception e) {
      return "Untitled";
    }
}
protected static String getDescription0(JSONObject info) {
  try {
    return (String) ((Map) info.get("description")).get(WiskOpdr.language.toString());
  } catch (Exception e) {
    return null;
  }
}

protected static String getJSON(JSONObject info) {
  try {
    return (String) ((Map) info.get("description")).get(WiskOpdr.language.toString() + "@JSON");
  } catch (Exception e) {
    return null;
  }
}

protected static String getDescription(JSONObject info) {
  String test = getDescription0(info);
  if (test == null) return null;
  if (test.startsWith(StudentModelChoicePanel.WISKOPDR_SIG))
    test = getJSON(info);
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
    JSONObject info = (JSONObject) map.get("info");
    StudentObjective obj = new StudentObjective ( getTitle(info), getDescription(info), getUUID(info), map.get("objectives"), getVoorkennis(info) );
    obj.x = getX(info);
    obj.y = getY(info);
    obj.methode = (Map<String, Map<String, Collection<Number>>>) info.get("methods");
    obj.methodInfo = getMethodInfo(info);
    obj.variants = getVariants(info);
    return obj;
}

@SuppressWarnings("rawtypes")
private static Variant[] getVariants(JSONObject info) {
  Object variants = info.get("variants");
  if (variants instanceof List) {
    List l = (List) variants;
    Variant[] result = new Variant[l.size()];
    for (int i = 0; i < result.length; i++) {
      Map v = (Map) l.get(i);
      Variant resulti = new Variant();
      resulti.name = (String) v.get("name");
      resulti.layers = v.get("layers");
      resulti.deselections = v.get("deselections");
      result[i] = resulti;
    }
    return result;
  }
  return null;
}

private static List<DomStudentModelMethodInfo> getMethodInfo(JSONObject map) {
  Object info = map.get("methodInfo");
  if (info instanceof List) {
    return ((List<Map>)info).stream().map(item -> {
      DomStudentModelMethodInfo result = new DomStudentModelMethodInfo((String)item.get("method"), (String)item.get("book"), (Number) item.get("chapter"));
      result.setX((Number) item.get("x"));
      result.setY((Number) item.get("y"));
      result.setVariant(item.get("variant"));
      return result;
    }).collect(Collectors.toList());
  }

  return null;
}

private static Integer getX(JSONObject info) {
  return integerValue(info.get("x"));
}

private static Integer getY(JSONObject info) {
  return integerValue(info.get("y"));
}

private static Integer integerValue(Object obj) {
  if (obj instanceof Integer) return (Integer) obj;
  if (obj instanceof Number) return ((Number) obj).intValue();
  return null;
}

private static String[] getVoorkennis(JSONObject map) {
  List<String> strings = (List<String>) map.get("voorkennis");
  if (strings == null) return new String[0];
  return strings.toArray(new String[strings.size()]);
}

static String getUUID(JSONObject map) {   
  return (String) map.get("id");
}

}
