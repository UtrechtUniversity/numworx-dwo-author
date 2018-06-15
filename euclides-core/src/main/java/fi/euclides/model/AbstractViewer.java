package fi.euclides.model;

import java.util.Arrays;
import java.util.Collections;

import fi.euclides.event.DescriptionBuilder;
import fi.euclides.event.EventHandler;
import fi.euclides.event.HitTester;
import fi.euclides.event.NameMapper;
import fi.euclides.event.Tracker;
import fi.euclides.event.TrackerContext;
import fi.euclides.model.Kegelsnede2;
import fi.euclides.model.Locus;
import fi.euclides.model.MP;
import fi.euclides.model.SegmentVisitor;
import fi.euclides.model.Triangle;
import fi.euclides.proof.LabelDelegate;
import fi.euclides.util.Hashtable;
import fi.euclides.util.JMath;
import fi.euclides.util.Observable;

public abstract class AbstractViewer extends Observable implements Visitor, Tracker, SegmentVisitor {

	public static final double R_TO_D = 180.0 / Math.PI;
	// five standard colors:
	protected static final int BLACK = 0;
	protected static final int RED = 1;
	protected static final int blue = 2;
	protected static final int magenta = 3;
	protected static final int LIGHT_GRAY = 4;
	
	protected Visitor LABELER = new Labeler();
	protected HitTester hitTester = new HitTester();
	private Hashtable<String, LabelDelegate> map = new Hashtable<String, LabelDelegate>();

	public HitTester getHitTester() {
		return hitTester;
	}

	protected boolean trail;
	protected boolean tracking;
	private Model model;
	private boolean polar, axes;
	protected float pointSize = 5f;

	protected AbstractViewer(Model model) {
		this.model = model;
	}
	protected AbstractViewer() {
		this(new Model());
	}
	
	/**
	 * @return the polar
	 */
	public boolean isPolar() {
		return polar;
	}
	/**
	 * @param polar the polar to set
	 */
	public void setPolar(boolean polar) {
		this.polar = polar;
	}
	/**
	 * @return the axes
	 */
	public boolean isAxes() {
		return axes;
	}
	/**
	 * @param axes the axes to set
	 */
	public void setAxes(boolean axes) {
		this.axes = axes;
	}

	protected class Labeler implements Visitor {

		public void visitCirkel(Cirkel c) {
			double x = (c.getX()+c.getD());
			double y = (c.getY()+c.getR());
			drawString(AbstractViewer.this.toString(c), x+1, y);
		}

		public void visitBoog(Boog b) {
			Cirkel c = b.base;
			double half = b.getStart() + b.length()/3;
			double x = c.getCenter().getXd()+ c.getR() * Math.cos(half);
			double y = c.getCenter().getYd()- c.getR()* Math.sin(half);
			drawString(AbstractViewer.this.toString(b), x+1, y);
		}
		
		public void visitLijn(Lijn l) {
			double x = (l.getX1()+l.getDX()/3);
			double y = (l.getY1()+l.getDY()/3);
			drawString(AbstractViewer.this.toString(l), x+1, y);
		}

		public void visitPunt(Punt p) {
			double x = (p.getXd());
			double y = (p.getYd());
			drawString(AbstractViewer.this.toString(p), x+1, y-1);
		}

		public void visitSegment(Segment s) {
			visitLijn(s);
		}

		public void visitLabel(Label label) {
		}

		public void visitTriangle(Triangle t) {
			double x = t.getA().getXd()+t.getB().getXd()+t.getC().getXd();
			double y = t.getA().getYd()+t.getB().getYd()+t.getC().getYd();
			drawString(AbstractViewer.this.toString(t), x/3, y/3);
		}

		public void visitKegelsnede(Kegelsnede2 k) {
		}

		public void visitLocus(Locus l) {
		}
		
	}
	public void visitBoog(Boog b) {
		selectColor(b);
		double d = b.base.getD();
		double s = b.getStart();
		double l = b.length();
		drawArc(b.base.getX(), b.base.getY(), d, s, l);
	}
	
	public void visitCirkel(Cirkel c) {
		selectColor(c);
		double d = c.getD();
		drawCircle(c.getX(), c.getY(),d);
	}

	protected ExtendedLijn ll = new ExtendedLijn();
	protected ExtendRay rr = new ExtendRay();

	protected Iterable<TrackerContext> track = Collections.emptySet();
	private boolean labels;

	public void visitLijn(Lijn l) {
		selectColor(l);
		ExtendedLijn uu = ll;
		if(l instanceof Ray) 
			uu = rr;
		uu.setLijn(l);
			drawLine(uu.getX1(), uu.getY1() , uu.getX2(), uu.getY2());		
	}

	public void visitMP(MP l) {
		selectColor(l);
		trail = true;
		l.visitSegments(this);
		trail = false;
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
		
	public void visitPunt(Punt punt) {
		if (trail) {
			drawPoint(punt.getXd(),punt.getYd());
		} else if (tracking)
		{	float p2 = pointSize/2f;
			drawCircle(punt.getXd()-p2, punt.getYd()-p2 , pointSize);
		} else
		{	selectColor(punt);
			float p2 = pointSize/2f;
			fillCircle(punt.getXd()-p2, punt.getYd()-p2 , pointSize);
		}
	}

	protected abstract void drawLine(double x1, double y1, double x2, double y2);

	protected abstract void fillCircle(double x, double y, double w);
	
	/**
	 * Draw a open circle
	 * @param x
	 * @param y
	 * @param w
	 */
//	@Deprecated
//	protected void drawCircle(int x, int y, int w) {
//	}
	/**
	 * Draw a open circle
	 * @param x
	 * @param y
	 * @param w
	 */
	protected abstract void drawCircle(double x, double y, double w);
	
	protected void drawArc(double x, double y, double w, double start, double length) {
		drawCircle(x,y,w);
	}
	
	protected int rint(double x) {
		return (int)Math.round(x);
	}
	
	protected abstract void setColor(int magenta);
	protected abstract void drawPoint(double x, double y);
	protected abstract void drawString(String string, double x, double y);
	
	/**
	 * @param object
	 */
	public void selectColor(Destroyable object) {
		if(tracking || trail)
			return;
		if(getModel().getSelect().contains(object))
			setColor(RED);
		else 
			setColor(BLACK);
	}

	public void update()  {
		
		getModel().executeDelay();
		drawAxes();
		
		tracking = false;			
		trail = true;
		setColor(LIGHT_GRAY);
		getModel().visitTrail(this);
		trail = false;
		getModel().visitLijnen(this);
		getModel().visitPunten(this);
		if(labels)
		{ 
			setColor(blue);
			getModel().visitPunten(LABELER);
			getModel().visitLijnen(LABELER);
		}
		if(track!=null) {
			setColor(magenta);
			tracking = true;
			for(TrackerContext t: track) {
              Track track2 = t.getTrack();
              if (track2 != null) track2.visit(this);
            }
			tracking = false;
		}

	}
	
	public void setTrack(TrackerContext track) {
	  this.track = Collections.singleton(track);
	}
		
	public void setTrack(Iterable<TrackerContext> track) {
		this.track = track;
	}

	public void visitSegment(Segment s) {
		selectColor(s);
		drawLine(s.getX1(), s.getY1() , s.getX2(), s.getY2());
	}

	public void setLabels(boolean selected) {
		labels = selected;
		paint();
	}

	public void setModel(Model model) {
		this.model = model;
		setChanged();
		notifyObservers(model);
	}

	public Model getModel() {
		return model;
	}

	
	/**
	 * @return the labels
	 */
	public boolean isLabels() {
		return labels;
	}

	/* (non-Javadoc)
	 * @see euclides.Visitor#visitLabel(euclides.Label)
	 */
	public void visitLabel(Label label) {
		selectColor(label);
		drawString(label.getString(), label.getXd(), label.getYd());
	}

	/* (non-Javadoc)
	 * @see euclides.event.Tracker#contains(double, double)
	 */
	public boolean contains(double x, double y) {
		return true;
	}

	/** Deze paint() is absoluut essentieel voor een SPV M700 telefoon.
	 * Anders een {@link java.lang.Error} bij {@link #setLabels(boolean)}.
	 */
	abstract public void paint();
	
	/* (non-Javadoc)
	 * @see euclides.event.Tracker#setPointerHandler(euclides.event.EventHandler)
	 */
	public void setPointerHandler(EventHandler eventHandler) {
	}

	/* (non-Javadoc)
	 * @see euclides.event.Tracker#setStatus(java.lang.String)
	 */
	public void setStatus(String string) {
	}
	
	public String toString(Destroyable d )
	{
		return getMapper().toString(d);
	}
	
	public NameMapper getMapper() { 
		return getModel();
	}
	
	public void drawAxes() {
		Punt O = getModel().getO();
		Punt U = getModel().getU();
		if(isAxes() && O != null && U != null)
		{
			setColor(LIGHT_GRAY);
			PuntenLijn xas = new PuntenLijn(O,U);
			if(isPolar())
			{	double x, y, w;
				rr.setLijn(xas);
				drawLine(rr.getX1(), rr.getY1(), rr.getX2(), rr.getY2());		
				Cirkel c = new Cirkel(O, U);
				w = c.getD();
				x = c.getX();
				y = c.getY();
				drawCircle(x, y, w);
			} else {
				ll.setLijn(xas);
				drawLine(ll.getX1(), ll.getY1(), ll.getX2(), ll.getY2());		
				LoodLijn yas = new LoodLijn(xas, O);
				ll.setLijn(yas);
				drawLine(ll.getX1(), ll.getY1(), ll.getX2(), ll.getY2());		
			}
		}
	}
	
	public String describe(Destroyable d) {
		DescriptionBuilder builder = new DescriptionBuilder(getMapper());
		d.visit(builder);
		return builder.toString();
	}

	public LabelDelegate getRegistered(String key) {
		return (LabelDelegate) map.get(key);
	}

	public void register(String key, LabelDelegate delegate) {
		map.put(key, delegate);
	}

}
