package fi.euclides.gwt;

public interface MouseContext {
  int getID();
  int getX();
  int getY();
  
  int getScreenX();
  int getScreenY();
  
  int getClientX();
  int getClientY();
  
  long getTimestamp();
}
