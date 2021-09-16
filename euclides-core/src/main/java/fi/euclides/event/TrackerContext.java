package fi.euclides.event;

import java.util.Vector;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Track;
import fi.euclides.util.Adaptee;

public interface TrackerContext extends Adaptee {
  void setTrack(Track track);
  Track getTrack();
  
  HitTester getHitTester();
  
  void clearSelection();
  void toggle(Destroyable d);
  Vector<Destroyable> selection();
  default boolean isTracked(Destroyable p) {
    Track t = getTrack();
    return t != null && t.isTracked(p);
  }

}
