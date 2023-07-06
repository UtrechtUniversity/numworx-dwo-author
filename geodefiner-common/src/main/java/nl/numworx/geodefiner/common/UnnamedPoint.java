package nl.numworx.geodefiner.common;

import fi.euclides.event.Tracker;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Punt;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.Observable;

public class UnnamedPoint extends Punt {

  private static int max = 5; // singleton is evil
  private final Tracker tracker;
  private Punt p;
  
  
  public static void setMaxIndex(int index) {
    UnnamedPoint.max = index;
  }

  UnnamedPoint(Tracker tracker) {
    super(Numbers.NaN, Numbers.NaN);
    this.tracker = tracker;
  }

  @Override
  public void update(Observable observable, Object arg) {
    if (arg == DESTROY && observable == p && p != null) {
      observable.deleteObserver(this);
      p = null;
      return;
    }
    super.update(observable, arg);
  }

  public boolean similar(Punt p) {
      if ( p == this.p) 
        return true;
      if ( this.p == null) {
        int index = p.getIndex();
        if( index >= max) {
          this.p = p;
          p.addObserver(this);
          return true;
        }
      }
      return false;
  }

  public Punt getP() {
    return p;
  }


}
