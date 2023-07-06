package fi.euclides.model;

import java.io.IOException;

import fi.euclides.model.algo.PointOnAlgorithm;
import fi.euclides.model.algo.PointOnArc;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.Observable;

public class Boog extends Rondje implements OpObject<Boog> {

// Trapdoor voor SVG
	public static Punt startOf(Boog b) {
		return b.getA();
	}
	public static Punt endOf(Boog b) {
		return b.getC();
	}
	
	
	private static final double PI2 = 2.0 * Math.PI;
	Cirkel base;
	private Punt[] depend;
	
	private void createBase(Punt... punts) {
		depend = punts;
		Cirkel3 base3 = new Cirkel3();
		base3.depend = punts;
		base3.setRadius(punts[0]);
		punts[1].addObserver(this);
		punts[2].addObserver(this);
		base3.l1.p1 = punts[0]; base3.l1.p2 = punts[1];
		base3.l2.p1 = punts[0]; base3.l2.p2 = punts[2];
		base3.recalc();

		base = base3;
		base.addObserver(this);
	}
	
	public double getStart() {
		double mx = base.getCenter().getXd();
		double my = base.getCenter().getYd();
		double ax = getA().getXd();
		double ay = getA().getYd();
		double s = Math.atan2(my-ay, ax-mx); //range -pi .. pi
		return s;
	}
	
	public double length() {
		double s = getStart();
		Punt c = getC();
		double mx = base.getCenter().getXd();
		double my = base.getCenter().getYd();
		
		double cx = c.getXd();
		double cy = c.getYd();
		double e = Math.atan2(my-cy, cx-mx);
		double bx = getB().getXd();
		double by = getB().getYd();
		double m = Math.atan2(my-by, bx-mx);
		while ( m < s) m += PI2;
// Er moet gelden e > m > s  dan e-s > 0
//		          e < m < s  dan e-s < 0
		
		double l = e - s;
		while (l >= PI2) l -= PI2; // not to big
		while (l < 0) l += PI2; // make positive
		// 0 <= l < 360
		if ( m > s + l) 
			l -= PI2;
		return l;
	}

	public void update(Observable observable, Object arg) {
		if(arg == DESTROY) {
			destroy();
			return;
		}
		if(observable == base) {
			setChanged();
			notifyObservers(arg);
			return;
		}
		base.update(observable, arg);
	}

	public void read(Codec codec) throws IOException {
		Cirkel3 base3 = new Cirkel3();
		base3.read(codec);
		base = base3;
		base.addObserver(this);
		depend = base3.depend;
	}

	public void write(Codec codec) throws IOException {
		base.write(codec);
	}

	public void visit(Visitor v) {
		v.visitBoog(this);
	}

	Punt getC() {
		return depend[2];
	}
	Punt getB() {
		return depend[1];
	}
	
	public static String TYPE = "bt";

	public Boog() {
	}

	public Boog(Punt a, Punt b, Punt c) {
		createBase(a,b,c);
	}

	public Boog(Punt[] punts) {
		createBase(punts);
	}

	@Override
	public String key() {
		return TYPE;
	}

	@Override
	public Destroyable[] getDepend() {
		return depend;
	}

	public double getR() {
		return base.getR();
	}

	public Punt getCenter() {
		return base.getCenter();
	}

	public PointOnAlgorithm<Boog> getAlgo() {
		return PointOnArc.INSTANCE;
	}

	public PuntOp<Boog> pointOn(Numbers x, Numbers y) {
		return new PuntOp<Boog>(x, y, this, getAlgo());
	}

	@Override
	public void destroy() {
		base.deleteObserver(this);
		base.destroy();
		super.destroy();
	}

	@Override
	public Destroyable trail() {
		Punt a = (Punt) getA().trail();
		Punt b = (Punt) getB().trail();
		Punt c = (Punt) getC().trail();
		return new Boog(a,b,c);
	}

	protected Punt getA() {
		return base.getRadius();
	}

	@Override
	public boolean isDefined() {
		return base.isDefined();
	}

	@Override
	Punt getRadius() {
		return base.getRadius();
	}
	@Override
	Numbers getR2n() {
		return base.getR2n();
	}

	@Override
	boolean isr2c() {
		return base.isr2c();
	}

	@Override
	public double getD() {
		return base.getD();
	}
	@Override
	boolean contains(Punt p) {
		double lastx = p.getXd();
		double lasty = p.getYd();
		double r = getR();
		double x = getCenter().getXd()-lastx;
		double y = getCenter().getYd()-lasty;
		double r2 = Math.hypot(x,y);
		double marge = 0.01;
		boolean done = Math.abs(r2-r) < marge;
		if(!done) return false;
		double h = Math.atan2(y, -x);
		double s = getStart();
		double l = length();
		if(l >= 0) {
			if(h < s) h += Math.PI * 2.0;
			done = s <= h && h <= s+l;
		} else {
			if(h > s) h -= Math.PI * 2.0;
			done = s+l <= h && h <= s;
		}
		return done;
	}

  public double getX() {
    return base.getX();
  }
  public double getY() {
    return base.getY();
  }

}
