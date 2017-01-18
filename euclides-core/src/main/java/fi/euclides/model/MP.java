package fi.euclides.model;

import java.util.Enumeration;
import java.util.Vector;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Lijn;
import fi.euclides.model.OpObject;
import fi.euclides.model.Pair;
import fi.euclides.model.Punt;
import fi.euclides.model.PuntOp;
import fi.euclides.model.Segment;
import fi.euclides.model.Visitor;
import fi.euclides.model.algo.PointOnAlgorithm;
import fi.euclides.model.algo.PointOnLocus;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.JMath;
import fi.euclides.util.Observer;

public abstract class MP extends Destroyable implements Observer, OpObject<MP> {

	abstract class Strategy {
		
		abstract void recalc();
		abstract Punt interpolate(Numbers x);

		protected void add(Numbers cc, Punt pp) {
			punten.addElement(new Pair(cc,pp));			
		}
		
		protected void addDest(Numbers x) {
			add(x, copy(dest));
		}
		
		public void focus(Punt focus1, Punt focus2) {			
		}
		
		int type() { return 0; }
		
		public void center(Punt center) {
			center.setDefined(false);
		}
		
	}
	class LijnStrategy extends Strategy {
		static final double SMALL = 1.0E-10;
		Lijn l;

		/**
		 * @param l
		 */
		public LijnStrategy(Lijn l) {
			this.l = l;
		}
		final int N = 151;
		void recalc() {
			punten.removeAllElements();
			if(!l.isDefined())
				return;
			double dx, dy, px, py;
			dx = l.getDXn().doubleValue();
			dy = l.getDYn().doubleValue();
			double h = JMath.hypot(dx, dy);
			dx /= h;
			dy /= h;
			px = l.getX1();
			py = l.getY1();
			// algorithm found in KSEG, map 0..1 to -inf..+inf
			for(int i = 0; i < N; i++)
			{
				double p = i/(double)(N-1);
				double c = (p - 0.5) * 2;
				if(c >= 0) c = JMath.pow(c, 1./81.) / 2.;
				else c = -JMath.pow(-c, 1./81.) / 2.;
				c = c * Math.PI / (1 + SMALL);
				c = Math.tan(c);
				source.setXY(px + c * dx, py + c * dy);	
				addDest(Numbers.createDouble(p));
			}			
		}
		/* (non-Javadoc)
		 * @see fi.euclides.locus.MP.Strategy#interpolate(fi.euclides.model.math.Numbers)
		 */
		Punt interpolate(Numbers x) {
			double dx, dy, px, py;
			dx = l.getDXn().doubleValue();
			dy = l.getDYn().doubleValue();
			double h = JMath.hypot(dx, dy);
			dx /= h;
			dy /= h;
			px = l.getX1();
			py = l.getY1();
			double p = x.doubleValue();
			double c = (p - 0.5) * 2;
			if(c >= 0) c = JMath.pow(c, 1./81.) / 2.;
			else c = -JMath.pow(-c, 1./81.) / 2.;
			c = c * Math.PI / (1 + SMALL);
			c = Math.tan(c);
			source.setXY(px + c * dx, py + c * dy);
			return copy(dest);
		}
		
		
	}

	private static final double MAXLEN = 1.0E3;
	private static final double MINLEN = 3.0;

	public final Vector<Pair> punten = new Vector<Pair>();
	protected Punt start = new VrijPunt();
	protected Punt stop = new VrijPunt();
	protected Segment s = new Segment(start,stop);
	protected Punt dest;
	protected PuntOp<? extends Destroyable> source;
	public Strategy strategy;
	public static final String PUNTOP = "Pmp";
	
	public MP() {
	}

	public Punt copy(Punt d) {
		Punt result = new VrijPunt(d.getX(), d.getY());
		result.setDefined(d.isDefined());
		return result;
	}

	public void visitSegments(SegmentVisitor v) {
		if(punten.isEmpty())
			return;
		Enumeration<Pair> e = new Vector(punten).elements();
		if (!e.hasMoreElements())
			return; // oops
		dest.deleteObserver(this);
		Pair pair = (Pair) e.nextElement();
		Punt p = (Punt)pair.getB();
		Numbers org = (Numbers)pair.getA();
		copyTo(p, start);
		while(e.hasMoreElements()) {
			pair = e.nextElement();
			p = (Punt)pair.getB();
			Numbers next = (Numbers)pair.getA();
			copyTo(p,stop);
			if(p.isDefined() && start.isDefined() )
			{	
				if(inside(v, start, stop))
				{	
					if(length(s) < MINLEN)
						v.visitSegment(s)
						;
					else
					{
						splitSegment(v, org, copy(start), next, copy(stop), 0);
					}
				}
			}
			copyTo(p, start);
			org = next;
		}
		dest.addObserver(this);
	}
	private static final int N = 4;
	private void splitSegment(SegmentVisitor v, Numbers org, Punt po,
			Numbers next, Punt pn, int n) {
		Segment s = new Segment(po,pn);
		double length = length(s);
		if(n >= N && length > MAXLEN)
			return;
		
		if(n >= N||length<MINLEN)
		{
			if(s.isDefined())
				v.visitSegment(s);
		} else {
			Numbers mid;
			if(next.doubleValue() < org.doubleValue())
			{
				double m = (next.doubleValue()+1.0+org.doubleValue())/2.0;
				if(m > 1.0) m -= 1.0;
				mid = Numbers.createDouble(m);
			} else {
				mid = Numbers.div(Numbers.add(org, next), Numbers.TWO);
			}
			Punt midpoint = strategy.interpolate(mid);
			splitSegment(v, org, po, mid, midpoint, n+1);
			splitSegment(v, mid, midpoint, next, pn, n+1);
		}
		
		s.destroy();
	}

	/**
	 * @param p
	 * @param punt TODO
	 */
	protected void copyTo(Punt p, Punt punt) {
		punt.setDefined(p.isDefined());
		punt.setXY(p.getX(), p.getY());
	}

	private boolean inside(SegmentVisitor v, Punt p1, Punt p2) {
		Numbers n;
		n = v.clipTop();
		if(Numbers.signum(Numbers.sub(n, p2.getY()))>0 &&
		   Numbers.signum(Numbers.sub(n, p1.getY()))>0	
		) return false;
		n = v.clipBottom();
		if(Numbers.signum(Numbers.sub(n, p2.getY()))<0 &&
				   Numbers.signum(Numbers.sub(n, p1.getY()))<0	
				) return false;
		n = v.clipLeft();
		if(Numbers.signum(Numbers.sub(n, p2.getX()))>0 &&
		   Numbers.signum(Numbers.sub(n, p1.getX()))>0	
		) return false;
		n = v.clipRight();
		if(Numbers.signum(Numbers.sub(n, p2.getX()))<0 &&
				   Numbers.signum(Numbers.sub(n, p1.getX()))<0	
				) return false;
		return true;
	}

	private double length(Segment seg) {
		return JMath.hypot(seg.getDX(), seg.getDY());
	}

	/* (non-Javadoc)
	 * @see fi.euclides.model.Destroyable#destroy()
	 */
	public void destroy() {
		if(dest != null)   dest.destroy();
		if(source != null) source.destroy();
		super.destroy();
	}
	
	public PointOnAlgorithm<MP> getAlgo() {
		return PointOnLocus.INSTANCE;
	}

	public PuntOp<MP> pointOn(Numbers x, Numbers y) {
		return new PuntOp<MP>(x, y, this, getAlgo());
	}

}
