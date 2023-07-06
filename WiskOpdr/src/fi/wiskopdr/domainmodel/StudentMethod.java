package fi.wiskopdr.domainmodel;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentMethod {

  private final Map<String,Object> contents; // JSON representation of DomMethod

  private static final Map<String,Object> NULL = new HashMap<>();
  static {
    NULL.put("id", Collections.EMPTY_MAP);
  }

  @SuppressWarnings("unchecked")
  public StudentMethod(Object contents) {
    if (contents instanceof Map)
      this.contents = (Map<String,Object>) contents;
    else {
      this.contents = NULL;
      NULL.put("method", "Alle leerdoelen"); // FIXME I18n
    }
  }
  
  public StudentMethod() {
    this(null);
  }

  public String getMethod() {
    return (String) contents.get("method");
  }
  
  @SuppressWarnings("unchecked")
  public List<String> getBooks() {
    return (List<String>) contents.getOrDefault("books", Collections.emptyList());
  }
  
  @SuppressWarnings("unchecked")
  public List<List<String>> getChapters() {
    return (List<List<String>>) contents.getOrDefault("chapters", Collections.emptyList());    
  }
  
  @SuppressWarnings("unchecked")
  public List<List<Number>> getEdges() {
    return (List<List<Number>>) contents.getOrDefault("edges", Collections.emptyList());
  }
  
  public String key() {
    String id = getId();
    if (id == null) return null;
    String[] split = id.split(";", 3);
    return split[2];
  }

  @SuppressWarnings("unchecked")
  public String getId() {
    return (String) ((Map<String, Object>) contents.get("id")).get("idString");
  }
}
