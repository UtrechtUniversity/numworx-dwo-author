package fi.euclides.event;

import java.util.Vector;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Track;

public interface TrackerContext {
  void setTrack(Track track);
  Track getTrack();
  
  HitTester getHitTester();
  
  void clearSelection();
  void toggle(Destroyable d);
  Vector<Destroyable> selection();

  <T> T adapt(Class<T> cls); // extension point

}
