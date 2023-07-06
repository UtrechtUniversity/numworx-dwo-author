package fi.wiskopdr.tekstobjects;

import fi.wiskopdr.WiskOpdr;

public enum LinkType {
  FALSE("_blank"),
  TRUE("true"),
  PLAYER("_self"),
  CLIENT("_parent"),
  TOP("_top");
  
  private final String display;

  LinkType(String display) {
    this.display = display;
  }

  public String display() {
    return display;
  }
  
  public String toString() {
    try {
      return WiskOpdr.rb.getString("LinkType_" + name());
    } catch (Exception e) {
      return name();
    }
  }

  public static LinkType parse(String display) {
    for(LinkType item: values()) {
      if (item.display.equals(display)) return item;
    }
    return FALSE;
  }
}
