package fi.euclides.model;

import java.util.Collection;
import java.util.Vector;

public interface TrailBuilder {
  Destroyable trail(Destroyable d);

  default Vector<Destroyable> fromTrail(Collection<Destroyable> set) {
    return new Vector<Destroyable> (set);
  }
  default void toTrail(Destroyable key, Vector<Destroyable> values) {}
}
