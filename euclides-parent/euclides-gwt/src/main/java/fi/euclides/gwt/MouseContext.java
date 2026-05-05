package fi.euclides.gwt;

import fi.euclides.event.HumanContext;

public interface MouseContext extends HumanContext {
  int getID();
  int getX();
  int getY();
  
  int getScreenX();
  int getScreenY();
  
  int getClientX();
  int getClientY();
  
  long getTimestamp();
}
