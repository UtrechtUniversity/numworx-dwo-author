package nl.numworx.geodefiner.common.math;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import fi.euclides.event.Tracker;
import fi.euclides.event.TrackerContext;
import fi.euclides.model.AbstractViewer;
import fi.euclides.model.Boog;
import fi.euclides.model.Cirkel;
import fi.euclides.model.Destroyable;
import fi.euclides.model.GeoImage;
import fi.euclides.model.Kegelsnede2;
import fi.euclides.model.Label;
import fi.euclides.model.Lijn;
import fi.euclides.model.Locus;
import fi.euclides.model.Punt;
import fi.euclides.model.Segment;
import fi.euclides.model.Track;
import fi.euclides.model.Triangle;
import fi.euclides.model.Visitor;
import fi.euclides.model.math.Numbers;
import fi.euclides.proof.LabelTester;
import fi.euclides.util.Observable;
import nl.numworx.geodefiner.common.index.Indexed;

public class Dragging extends LabelTester {

  private class Collector implements Visitor {
    Collection<Destroyable> set;
    public Collector(Collection<Destroyable> set) {
      this.set = set;
    }

    @Override
    public void visitPunt(Punt p) {
      set.add(p);
    }

    @Override
    public void visitLijn(Lijn l) {
      set.add(l);
    }

    @Override
    public void visitCirkel(Cirkel c) {
      set.add(c);
    }

    @Override
    public void visitSegment(Segment s) {
      set.add(s);
    }

    @Override
    public void visitLabel(Label label) {
      set.add(label);
    }

    @Override
    public void visitTriangle(Triangle t) {
      set.add(t);
    }

    @Override
    public void visitKegelsnede(Kegelsnede2 k) {
      set.add(k);
    }

    @Override
    public void visitLocus(Locus l) {
      set.add(l);
    }

    @Override
    public void visitBoog(Boog b) {
      set.add(b);
    }

	@Override
	public void visitImage(GeoImage image) {
	  set.add(image);
	}

  }

  private AbstractViewer viewer;
  private final Collection<Label> checks = new LinkedList<>();

  Dragging(Tracker tracker) {
    super("dragging");
    viewer = tracker.adapt(AbstractViewer.class);
    setTracker(tracker);
    viewer.addObserver(this);
  }

  @Override
  public Destroyable[] createDepend() {
    return new Destroyable[1];
  }

  @Override
  protected boolean test(Label l) {
    boolean r = setState(l, l.value, 0.0);
    return true;
  }

  @Override
  public boolean define(Label l) {
    test(l);
    checks.add(l);
    Destroyable[] depend = l.getDepend();
    l.setString("");
    return true;
  }

  public void update(Observable observable, Object arg) {
    if (observable == viewer)
    {
      if (arg instanceof Iterable) {
        Iterable<TrackerContext> iter = (Iterable<TrackerContext>)arg;
        Set<Destroyable> set = new HashSet<>();
        for (TrackerContext item : iter) {
          Track t = item.getTrack();
          if (t != null) t.visit(new Collector(set));
        }
        for( Label l: checks) {
          Destroyable d0;
          Destroyable d = d0 = l.getDepend()[0];
          while (d instanceof Indexed) {
            d = ((Indexed) d).getDelegate();
          }
          Numbers value = (set.contains(d)) ? Numbers.ZERO : Numbers.ONE;
          if (setState(l,value,0.0))
            l.setString(string + " " + s(d0)); else l.setString("");
        }
      }
      
      return;
    }
    
    if(arg == Destroyable.DESTROY) {
      checks.remove(observable);
    }
    super.update(observable, arg);
}

  

}
