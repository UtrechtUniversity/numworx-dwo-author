package fi.euclides.event;

import java.util.Vector;

import fi.euclides.model.Kegelsnede2;
import fi.euclides.model.Locus;
import fi.euclides.model.MP;
import fi.euclides.model.Triangle;
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

public abstract class EventHandler implements Visitor {

	/* (non-Javadoc)
	 * @see euclides.Visitor#visitLabel(euclides.Label)
	 */
	public void visitLabel(Label label) {
		if(testLabel) getModel().toggle(label);
	}

	Tracker tracker;
	Track track;
	protected boolean testPunt;
	protected boolean testLijn;
	protected boolean testLabel;
	protected boolean done;
	protected String string;
		
	public void command() {
		getTracker().setPointerHandler(this);
		setStatus(this.string);
	}
	
	protected void testHits(double x, double y) {
		clear();
		HitTester hitTester = getTracker().getHitTester();
		hitTester.setVisitor(this);
		hitTester.setXY(x, y);
		done = false;
		if(testPunt)
			getModel().visitPunten(hitTester);
		if(!done && (testLijn||testLabel))
			getModel().visitLijnen(hitTester);
		hitTester.done();
	}

	void clear() {
		getModel().clearSelection();
	}
	
	
	public void pointerDragged(Numbers x, Numbers y)
	{
		testHits(x.doubleValue(),y.doubleValue());
		if(track!= null) 
			track.setXY(x, y);
	}
	
	public void pointerPressed(Numbers x, Numbers y)
	{
		
	}
	
	public void pointerReleased(Numbers x, Numbers y)
	{
		
	}
	
	public void visitCirkel(Cirkel c) {
		if(testLijn) getModel().toggle(c);
	}
	
	public void visitBoog(Boog b) {
		if(testLijn) getModel().toggle(b);
	}

	public void visitLijn(Lijn l) {
		if(testLijn) getModel().toggle(l);
	}
	public void visitMP(MP l) {
		if(testLijn) getModel().toggle(l);
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
		getModel().toggle(p);
		done = true;
	}

	final public Model getModel() {
		return tracker.getModel();
	}

	public void setTrack(Track track) {
		this.track = track;
	}

	public Track getTrack() {
		return track;
	}

	public void setTracker(Tracker tracker) {
		this.tracker = tracker;
	}

	final public Tracker getTracker() {
		return tracker;
	}

	public void pointerClicked(Numbers x, Numbers y) {
	}
	final public void pointerClicked(int x, int y) {
		pointerClicked(Numbers.createInteger(x),Numbers.createInteger(y));
	}
	final public void pointerClicked(double x, double y) {
		pointerClicked(Numbers.createDouble(x), Numbers.createDouble(y));
	}

	/**
	 * @param string
	 */
	public EventHandler(String string) {
		this.string = string;
	}

	/* (non-Javadoc)
	 * @see euclides.Visitor#visitSegment(euclides.Segment)
	 */
	public void visitSegment(Segment s) {
		visitLijn(s);
	}

	protected void setStatus(String string) {
		getTracker().setStatus(string);
	}

	final public void pointerDragged(int i, int j) {
		pointerDragged(Numbers.createInteger(i), Numbers.createInteger(j));	
	}
	final public void pointerDragged(double i, double j) {
		pointerDragged(Numbers.createDouble(i), Numbers.createDouble(j));	
	}

	final public void pointerPressed(int i, int j) {
		pointerPressed(Numbers.createInteger(i), Numbers.createInteger(j));	
	}
	final public void pointerPressed(double i, double j) {
		pointerPressed(Numbers.createDouble(i), Numbers.createDouble(j));	
	}

	public final void pointerReleased(int i, int j) {
		pointerReleased(Numbers.createInteger(i), Numbers.createInteger(j));	
	}
	public final void pointerReleased(double i, double j) {
		pointerReleased(Numbers.createDouble(i), Numbers.createDouble(j));	
	}

	protected String s(Destroyable d) {
		return getTracker().getMapper().toString(d);
	}

	public boolean allowSelection(Vector selection) {
		return true;
	}

}
