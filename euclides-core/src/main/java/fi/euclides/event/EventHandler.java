package fi.euclides.event;

import java.util.Vector;

import fi.euclides.model.Kegelsnede2;
import fi.euclides.model.Locus;
import fi.euclides.model.MP;
import fi.euclides.model.Triangle;
import fi.euclides.model.AbstractViewer;
import fi.euclides.model.Boog;
import fi.euclides.model.Cirkel;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Lijn;
import fi.euclides.model.Model;
import fi.euclides.model.Punt;
import fi.euclides.model.Segment;
import fi.euclides.model.Track;
import fi.euclides.model.Visitor;
import fi.euclides.model.math.Numbers;

public abstract class EventHandler {

	Tracker tracker;
	//Track track;
	protected boolean testPunt;
	protected boolean testLijn;
	protected boolean testLabel;
	protected boolean done;
	protected String string;
		
	public void command() {
		getTracker().setPointerHandler(this);
		setStatus(this.string);
	}
	
  protected class InContext implements Visitor {
    protected final TrackerContext context;

    protected InContext(TrackerContext context) {
      this.context = context;
    }

    protected void toggle(Destroyable d) {
      context.toggle(d);
    }
    
    public void visitCirkel(Cirkel c) {
      if (testLijn) toggle(c);
    }

    public void visitBoog(Boog b) {
      if (testLijn) toggle(b);
    }

    /*
     * (non-Javadoc)
     * 
     * @see euclides.Visitor#visitSegment(euclides.Segment)
     */
    public void visitSegment(Segment s) {
      visitLijn(s);
    }

    public void visitLijn(Lijn l) {
      if (testLijn) toggle(l);
    }

    protected void visitMP(MP l) {
      if (testLijn) toggle(l);
    }

    public void visitTriangle(Triangle t) {
      visitMP(t);
    }

    public void visitKegelsnede(Kegelsnede2 k) {
      visitMP(k);
    }

    public void visitLocus(Locus l) {
      visitMP(l);
    }

    public void visitPunt(Punt p) {
      toggle(p);
      done = true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see euclides.Visitor#visitLabel(euclides.Label)
     */
    public void visitLabel(Label label) {
      if (testLabel) toggle(label);
    }

  }

	protected Visitor inContext(TrackerContext context) {
	  return new InContext(context);
	}
	
	protected void testHits(double x, double y, TrackerContext context) {
		clear(context);
		HitTester hitTester = context.getHitTester();
		hitTester.setVisitor(inContext(context));
		hitTester.setXY(x, y);
		done = false;
		if(testPunt)
			getModel().visitPunten(hitTester);
		if(!done && (testLijn||testLabel))
			getModel().visitLijnen(hitTester);
		hitTester.done();
	}

	void clear(TrackerContext context) {
		context.clearSelection();
	}
	
	
	public void pointerDragged(Numbers x, Numbers y, TrackerContext context)
	{
		testHits(x.doubleValue(),y.doubleValue(), context);
		Track track = context.getTrack();
		if(track!= null) 
		{	track.setXY(x, y);
		}
	}
	
	public void pointerPressed(Numbers x, Numbers y, TrackerContext context)
	{
		
	}
	
	public void pointerReleased(Numbers x, Numbers y, TrackerContext context)
	{
		
	}
	

	final public Model getModel() {
		return tracker.getModel();
	}

//	public void setTrack(Track track) {
//		this.track = track;
//	}
//
//	public Track getTrack() {
//		return track;
//	}

	public void setTracker(Tracker tracker) {
		this.tracker = tracker;
	}

	final public Tracker getTracker() {
		return tracker;
	}

	public void pointerClicked(Numbers x, Numbers y, TrackerContext context) {
	}
	final public void pointerClicked(int x, int y, TrackerContext context) {
		pointerClicked(Numbers.createInteger(x),Numbers.createInteger(y), context);
	}
	final public void pointerClicked(double x, double y, TrackerContext context) {
		pointerClicked(Numbers.createDouble(x), Numbers.createDouble(y),context);
	}

	/**
	 * @param string
	 */
	public EventHandler(String string) {
		this.string = string;
	}


	protected void setStatus(String string) {
		getTracker().setStatus(string);
	}

	final public void pointerDragged(int i, int j, TrackerContext context) {
		pointerDragged(Numbers.createInteger(i), Numbers.createInteger(j), context);	
	}
	final public void pointerDragged(double i, double j, TrackerContext context) {
		pointerDragged(Numbers.createDouble(i), Numbers.createDouble(j), context);	
	}

	final public void pointerPressed(int i, int j, TrackerContext context) {
		pointerPressed(Numbers.createInteger(i), Numbers.createInteger(j), context);	
	}
	final public void pointerPressed(double i, double j, TrackerContext context) {
		pointerPressed(Numbers.createDouble(i), Numbers.createDouble(j), context);	
	}

	public final void pointerReleased(int i, int j, TrackerContext context) {
		pointerReleased(Numbers.createInteger(i), Numbers.createInteger(j), context);	
	}
	public final void pointerReleased(double i, double j, TrackerContext context) {
		pointerReleased(Numbers.createDouble(i), Numbers.createDouble(j), context);	
	}

	protected String s(Destroyable d) {
		return getTracker().getMapper().toString(d);
	}

	public boolean allowSelection(Vector selection) {
		return true;
	}

}
