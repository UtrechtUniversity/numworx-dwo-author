package fi.euclides.model;

import java.io.IOException;
import java.util.Enumeration;

import fi.euclides.event.NameMapper;
import fi.euclides.model.Boog;
import fi.euclides.model.Cirkel;
import fi.euclides.model.Codec;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Lijn;
import fi.euclides.model.Model;
import fi.euclides.model.Pair;
import fi.euclides.model.Punt;
import fi.euclides.model.PuntOp;
import fi.euclides.model.Ray;
import fi.euclides.model.Segment;
import fi.euclides.model.Visitor;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.JMath;
import fi.euclides.util.Observable;
import fi.euclides.util.Observer;

public class Locus extends MP {

	public interface LocusModel {
		public interface Builder {
			LocusModel build(PuntOp<?> sourceorg2, Punt destorg2,
					NameMapper toString);
			LocusModel build(Label f);
			LocusModel readModel(Codec codec) throws IOException;
		}
		
		Punt getDest();
		PuntOp<?> getSource();
		void addObserver(Observer obs);
		void destroy();
		void writeModel(Codec codec) throws IOException;
		Destroyable[] getDepend();

	}

	public class RayStrategy extends LijnStrategy {

		public RayStrategy(Lijn l) {
			super(l);
		}

		Punt interpolate(Numbers x) {
			return super.interpolate(x);
		}

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
			for(int i = N/2; i < N; i++)
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

	}

	class NullStrategy extends Strategy {
		/**
		 * 
		 */
		public NullStrategy() {
			super();
			punten.removeAllElements();
			punten.addElement(new Pair(Numbers.ZERO, dest));
		}

		boolean isDefined()
		{
			return dest.isDefined();
		}
		
		void recalc() {
		}

		/* (non-Javadoc)
		 * @see fi.euclides.locus.MP.Strategy#interpolate(fi.euclides.model.math.Numbers)
		 */
		Punt interpolate(Numbers x) {
			return dest;
		}

	}

	class BoogStrategy extends Strategy {
		private static final double PI2 = Math.PI*2.0;
		private static final int N = 100;
		Boog c;
		/**
		 * @param c
		 */
		public BoogStrategy(Boog c) {
			this.c = c;			
		}

		void recalc() {
			punten.removeAllElements();
			if(!c.isDefined())
				return;
			Punt m = c.getCenter();
			double mx = m.getXd();
			double my = m.getYd();
			double r = c.getR();
			double dh = c.length()/N;
			double h = c.getStart();
			for(int i=0; i <= N; i++, h += dh)
			{			
				source.setXY(mx+r*Math.cos(h), my-r*Math.sin(h));
				addDest(Numbers.createDouble(h/PI2));
			}
		}

		/* (non-Javadoc)
		 * @see fi.euclides.locus.MP.Strategy#interpolate(fi.euclides.model.math.Numbers)
		 */
		Punt interpolate(Numbers x) {
			Punt m = c.getCenter();
			double mx = m.getXd();
			double my = m.getYd();
			double r = c.getR();
			double h = x.doubleValue();
			h *= PI2;
			source.setXY(mx+r*Math.cos(h), my-r*Math.sin(h));
			return copy(dest);
		}
	}

	class CirkelStrategy extends Strategy {

		private static final double PI2 = Math.PI*2.0;
		private static final int N = 100;
		Cirkel c;
		/**
		 * @param c
		 */
		public CirkelStrategy(Cirkel c) {
			this.c = c;			
		}

		void recalc() {
			punten.removeAllElements();
			if(!c.isDefined())
				return;
			Punt m = c.getCenter();
			double mx = m.getXd();
			double my = m.getYd();
			double r = c.getR();
			double dh = PI2/N;
			double h = 0;
			for(int i=0; i <= N; i++, h += dh)
			{			
				source.setXY(mx+r*Math.cos(h), my+r*Math.sin(h));
				addDest(Numbers.createDouble(h/PI2));
			}
		}

		/* (non-Javadoc)
		 * @see fi.euclides.locus.MP.Strategy#interpolate(fi.euclides.model.math.Numbers)
		 */
		Punt interpolate(Numbers x) {
			Punt m = c.getCenter();
			double mx = m.getXd();
			double my = m.getYd();
			double r = c.getR();
			double h = x.doubleValue();
			h *= PI2;
			source.setXY(mx+r*Math.cos(h), my+r*Math.sin(h));
			return copy(dest);
		}

	}

	public static final String TYPE="MP";
	
	private PuntOp<?> sourceorg;
	private Punt   destorg;
	
	private LocusModel model;
	
	class MPStrategy extends Strategy {

		MP l;
		/**
		 * @param l
		 */
		public MPStrategy(MP l) {
			this.l = l;
		}
		void recalc() {
			punten.removeAllElements();
			Enumeration e = l.punten.elements();
			while (e.hasMoreElements()) {
				Pair pair = (Pair) e.nextElement();
				Punt p = (Punt) pair.getB();
				if(p.isDefined())
				{
					source.setXY(p.getX(), p.getY());
					addDest((Numbers) pair.getA());
				}			
			}		
		}
		/* (non-Javadoc)
		 * @see fi.euclides.locus.MP.Strategy#interpolate(fi.euclides.model.math.Numbers)
		 */
		Punt interpolate(Numbers x) {
			Punt p = l.strategy.interpolate(x);
			source.setXY(p.getX(), p.getY());
			return copy(dest);
		}	
		
	}
	
	
	class TriangleStrategy extends Strategy {

		TriangleStrategy(Triangle t) {
			this.t = t;
		}

		Triangle t;
		static final int n = 100;
		static final double N = n * 3;
		final Numbers nn = Numbers.createDouble(n);
		void step(Punt a, Punt b, int start, int stop)
		{
			Numbers dx = Numbers.sub(b.getX(), a.getX());
			dx = Numbers.div(dx, nn);
			Numbers dy = Numbers.sub(b.getY(), a.getY());
			dy = Numbers.div(dy, nn);
			source.setXY(a.getX(), a.getY());			
			addDest(Numbers.createDouble(start/N));
			for(int i = 1+start; i <= stop; i++)
			{
				source.setXY(Numbers.add(source.getX(),dx), Numbers.add(source.getY(), dy));			
				addDest(Numbers.createDouble(i/(double)N));
			}			
			
		}
		
		void recalc() {
			punten.removeAllElements();
			step(t.getA(), t.getB(), 0, 99);
			step(t.getB(), t.getC(), 100, 199);
			step(t.getC(), t.getA(), 200, 300);
		}

		Punt interpolate(Numbers x) {
			double xx = x.doubleValue() * 3;
			if(xx <= 1.0)
				return interpolate(t.getA(), t.getB(), xx);
			if(xx <= 2.0)
				return interpolate(t.getB(), t.getC(), xx-1);
			
			return interpolate(t.getC(), t.getA(), xx-2);
		}

		private Punt interpolate(Punt a, Punt b, double step) {
			Numbers dx = Numbers.sub(b.getX(), a.getX());
			Numbers dy = Numbers.sub(b.getY(), a.getY());
			dx = Numbers.mul(dx, Numbers.createDouble(step));
			dy = Numbers.mul(dy, Numbers.createDouble(step));
			source.setXY(Numbers.add(a.getX(), dx), Numbers.add(a.getY(), dy));
			return copy(dest);
		}
	}
	
	class SegmentStrategy extends Strategy {
		Lijn l;

		/**
		 * @param l
		 */
		public SegmentStrategy(Lijn l) {
			this.l = l;
		}
		final int N = 100;
		void recalc() {
			Numbers dx, dy;
			dx = Numbers.div(l.getDXn(), Numbers.createDouble(N));
			dy = Numbers.div(l.getDYn(), Numbers.createDouble(N));			
			punten.removeAllElements();
			source.setXY(l.getX1n(), l.getY1n());			
			addDest(Numbers.ZERO);
			for(int i = 1; i <= N; i++)
			{
				source.setXY(Numbers.add(source.getX(),dx), Numbers.add(source.getY(), dy));			
				addDest(Numbers.createDouble(i/(double)N));
			}			
		}
		/* (non-Javadoc)
		 * @see fi.euclides.locus.MP.Strategy#interpolate(fi.euclides.model.math.Numbers)
		 */
		Punt interpolate(Numbers x) {
			Numbers dx = Numbers.mul(l.getDXn(), x);
			Numbers dy = Numbers.mul(l.getDYn(), x);
			dx  = Numbers.add(dx, l.getX1n());
			dy  = Numbers.add(dy, l.getY1n());
			source.setXY(dx, dy);
			return copy(dest);
		}
		
		
	}

	/**
	 * @param source
	 * @param dest
	 */
	public Locus(PuntOp source, Punt dest, Model parent) {
		this.sourceorg = source;
		this.destorg = dest;
		initStrategy(parent);
	}

	class InitStrategy implements Visitor
	{
		public void visitCirkel(Cirkel c) {
			strategy = new CirkelStrategy(c);		
		}
		
		public void visitBoog(Boog b) {
			strategy = new BoogStrategy(b);
		}
		public void visitLabel(Label label) {
			strategy = new NullStrategy();
		}
		public void visitLijn(Lijn l) {
			if(l instanceof Ray)
				strategy = new RayStrategy((Ray)l);
			else
				strategy = new LijnStrategy(l);
		}

		public void visitPunt(Punt p) {
			strategy = new NullStrategy();	
		}
		public void visitSegment(Segment s) {
			strategy = new SegmentStrategy(s);
		}
		public void visitTriangle(Triangle t) {
			strategy = new TriangleStrategy(t);
			
		}
		public void visitKegelsnede(Kegelsnede2 k) {
			strategy = new MPStrategy(k);			
		}
		public void visitLocus(Locus l) {
			strategy = new MPStrategy(l);
		}
	}
	
	final InitStrategy INIT_STRATEGY = new InitStrategy();
	/**
	 * @param toString 
	 * 
	 */
	private void initStrategy(NameMapper toString) {		
		model = createLocusModel(sourceorg, destorg, toString);
		destorg.addObserver(this);
		sourceorg.addObserver(this);
		initStrategy(model);
	}

	private void initStrategy(LocusModel lm) {
		model = lm;
		model.addObserver(this);
		dest = model.getDest();
		source = model.getSource();
		Destroyable op = source.getOp();
		op.visit(INIT_STRATEGY);			
		recalc();
	}

	public static LocusModel.Builder BUILDER = null;
	
	protected LocusModel createLocusModel(PuntOp<?> sourceorg2, Punt destorg2,
			NameMapper toString) {
		return BUILDER.build(sourceorg2, destorg2, toString);
	}

	private void recalc() {
		strategy.recalc();
		setChanged();
		notifyObservers();
	}

	public Locus() {
	}

	public Locus(LocusModel lm) {
		model = lm;
		initStrategy(lm);
	}

	public String key() {
		return TYPE;
	}

	public void read(Codec codec) throws IOException {
//		Punt op = codec.readPunt();
//		if(op != null) setSource((PuntOp) op);
//		op = codec.readPunt();
//		if(op != null) setDest(op);
//		initStrategy(codec.getModel());
		LocusModel model = BUILDER.readModel(codec);
		initStrategy(model);
	}

	/* (non-Javadoc)
	 * @see fi.euclides.model.Destroyable#isDefined()
	 */
	public boolean isDefined() {
	//	return sourceorg.getOp().isDefined();
		return true;
	}

	public void write(Codec codec) throws IOException {
		//super.write(codec);
		//codec.writeDestroyable(sourceorg);
		//codec.writeDestroyable(destorg);
		model.writeModel(codec);
	}

	public void update(Observable observable, Object arg) {
//		System.out.println(observable + "->update " + arg);
		if(arg == Destroyable.VISIBLE)
			return;
		if(arg == Destroyable.DESTROY)
		{
			destroy();
			return;
		}
		if(observable == model)
			recalc();
		else {
//			System.out.println(observable + "->update " + arg);
		}
	}

	/* (non-Javadoc)
	 * @see fi.euclides.model.Destroyable#destroy()
	 */
	public void destroy() {
		if(sourceorg != null)
			sourceorg.deleteObserver(this);
		if(dest != null)
			dest.deleteObserver(this);
		if(destorg != null)
			destorg.deleteObserver(this);
		if(model != null)
			model.destroy();
		super.destroy();
	}

	public Destroyable[] getDepend() {
		return model.getDepend();
	}
	
	/**
	 * @return the source
	 * @deprecated gebruik getDepend()[0];
	 */
	public PuntOp<?> getSource() {
		return sourceorg;
	}

//	/**
//	 * @param source the source to set
//	 */
//	private void setSource(PuntOp<?> source) {
//		this.sourceorg = source;
//	}

	/**
	 * @return the dest
	 * @deprecated gebruik getDepend()[1]
	 */
	public Punt getDest() {
		Destroyable[] depend = getDepend();
		if(depend.length > 1 && depend[1] instanceof Punt)
			return (Punt) depend[1];
		return destorg;
	}

//	/**
//	 * @param dest the dest to set
//	 */
//	private void setDest(Punt dest) {
//		this.destorg = dest;
//	}
	boolean fixme = true;
	/* FIXME
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		if(fixme) return System.identityHashCode(this);
		final int prime = 31;
		int result = 1;
		result = prime * result + ((destorg == null) ? 0 : destorg.hashCode());
		result = prime * result
				+ ((sourceorg == null) ? 0 : sourceorg.hashCode());
		return result;
	}

	/* FIXME
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean same(Object obj) {
		if(fixme) return this == obj;
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Locus other = (Locus) obj;
		if (destorg == null) {
			if (other.destorg != null)
				return false;
		} else if (!destorg.equals(other.destorg))
			return false;
		if (sourceorg == null) {
			if (other.sourceorg != null)
				return false;
		} else if (!sourceorg.equals(other.sourceorg))
			return false;
		return true;
	}

	public void visit(Visitor v) {
		v.visitLocus(this);		
	}

}
