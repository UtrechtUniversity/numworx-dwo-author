package fi.euclides.model;

import java.io.IOException;

import fi.euclides.model.Cirkel;
import fi.euclides.model.Codec;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Dpunt;
import fi.euclides.model.ExtendedLijn;
import fi.euclides.model.Pair;
import fi.euclides.model.Punt;
import fi.euclides.model.Visitor;
import fi.euclides.model.algo.PointOnAlgorithm;
import fi.euclides.model.algo.PointOnConic;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.Observable;

public class Kegelsnede2 extends MP {

	Punt a,b,c,d,e;
	public static final int ELIPSE = 1, PARABOLA = 2, HYPERBOLA = 3, LINES = 4;
	
	// ellips/parabool/hyperbool
	public int type() {
		return strategy.type();
	}
	
	public Destroyable[] getDepend() {
		return new Punt[] { a,b,c,d,e };
	}

	boolean defined;

	public static final String TYPE ="K"; // kegelsnede
	static final int N = 51;
	static final double SMALL = 1.0E-15;
	
	class ParabolaStrategy extends Strategy {
		int type() { return PARABOLA; }
		double a,b,c,d,e,f;
		double aebd;
		boolean swap;
		
		Punt interpolate(Numbers n) {
			double p = n.doubleValue();
// -oo < t < oo en 0<=p<=1
			double t = (p - 0.5) * 2;
			if(t >= 0) t = Math.pow(t, 1./81.) / 2.;
			else t = -Math.pow(-t, 1./81.) / 2.;
			t = t * Math.PI / (1 + SMALL);
			t = Math.tan(t);
			
			double x = (a*b*t*t+2*a*e*t+b*f)/aebd;
			double y = -(2*a*a*t*t+2*a*d*t+2*a*f)/aebd;
			if(swap)
				return new VrijPunt(y,x);
			return new VrijPunt(x,y);
		}

		/**
		 * @param a
		 * @param b
		 * @param c
		 * @param d
		 * @param e
		 * @param f
		 */
		public ParabolaStrategy(double a, double b, double c, double d,
				double e, double f) {
			if(!(swap = !(Math.abs(a)> Math.abs(c))))
			{
				this.a = a;
				this.c = c;
				this.d = d;
				this.e = e;
			} else {
				this.a = c;
				this.c = a;
				this.d = e;
				this.e = d;
			}
			this.b = b;
			this.f = f;
			aebd = 2*this.a*this.e-this.b*this.d;
		}

		void recalc() {
			punten.removeAllElements();
			for(int i = 0; i < N; i++)
			{
				Numbers p = Numbers.createRational(i, N-1);
				add(p, interpolate(p));
			}
		
		}
		
	}
	
	
	public class DegeneratedStrategy extends Strategy
	{
		int type() { return LINES; }
		public double ax;
		public double ay;
		public double bx=1;
		public double by=1;
		public double cx, cy, dx=1, dy=-1;
		
		
		/**
		 * @param ax
		 * @param ay
		 * @param bx
		 * @param by
		 * @param cx
		 * @param cy
		 * @param dx
		 * @param dy
		 */
		DegeneratedStrategy(double ax, double ay, double bx, double by,
				double cx, double cy, double dx, double dy) {
			this.ax = ax;
			this.ay = ay;
			this.bx = bx;
			this.by = by;
			this.cx = cx;
			this.cy = cy;
			this.dx = dx;
			this.dy = dy;
		}
		public DegeneratedStrategy(Punt a, Punt c, Punt b, Punt d) {
			ax = a.getXd(); ay = a.getYd();
			bx = c.getXd()-ax; by = c.getYd()-ay;
			cx = b.getXd(); cy = b.getYd();
			dx = d.getXd()-cx; dy = d.getYd()-cy;
		}
		public DegeneratedStrategy(double ox, double oy, double bx, double by, double dx, double dy)
		{
			ax = cx = ox;
			ay = cy = oy;
			this.bx = bx; this.by = by;
			this.dx = dx; this.dy = dy;
		}


		void recalc() {
			punten.removeAllElements();
			Numbers x;
			
			x = Numbers.ZERO;
			
			add(x, new VrijPunt(ax+bx, ay+by));
			x = Numbers.createDouble(0.4);
			add(x, new VrijPunt(ax-bx, ay-by));
			x = Numbers.createRational(1, 2);
			Punt p = new VrijPunt(); p.setDefined(false);
			add(x, p);
			x = Numbers.createDouble(0.6);
			add(x, new VrijPunt(cx+dx, cy+dy));
			x = Numbers.ONE;
			add(x, new VrijPunt(cx-dx, cy-dy));
			
		}
		Punt interpolate(Numbers x) {
			return null;
		}

		public void focus(Punt focus1, Punt focus2) {
			focus1.setDefined(false);
			focus2.setDefined(false);
		}
		
	}
	
	
	
	
	/* (non-Javadoc)
	 * @see fi.euclides.locus.MP#visitSegments(fi.euclides.locus.SegmentVisitor)
	 */
	public void visitSegments(SegmentVisitor v) {
		if(strategy instanceof DegeneratedStrategy)
		{
			ExtendedLijn ll = new ExtendedLijn();
			ll.setClip(v.clipLeft(), v.clipTop(), v.clipRight(), v.clipBottom());
			Pair p; 
			p = (Pair) punten.firstElement();
			copyTo((Punt) p.getB(), start);
			p = (Pair) punten.elementAt(1);
			copyTo((Punt) p.getB(), stop);
			ll.setLijn(s);
			//copyTo(ll.getP1(), start);
			start.setXY(ll.getX1(), ll.getY1());
			//copyTo(ll.getP2(), stop);
			stop.setXY(ll.getX2(), ll.getY2());
			v.visitSegment(s);
			p = (Pair) punten.lastElement();
			copyTo((Punt) p.getB(), start);
			p = (Pair) punten.elementAt(3);
			copyTo((Punt) p.getB(), stop);
			ll.setLijn(s);
			start.setXY(ll.getX1(), ll.getY1());
			stop.setXY(ll.getX2(), ll.getY2());
			
			v.visitSegment(s);
			
			return;
		}
		super.visitSegments(v);
	}
	static final double SQRT_8 = Math.sqrt(8);

	class EllipseStrategy extends Strategy
	{

		int type() { return ELIPSE; }
		double a = 200;
		double b = 200;
		
		double tx, ty;
		double cx = 0.0, cy = 0.0;
		
		public EllipseStrategy(double a, double b, double p, double q,
				double s, double r) {
			this.a = 1/Math.sqrt(a/2);
			this.b = 1/Math.sqrt(b/16);
			tx = p; ty = q;
			cx = s; cy = r;
		}

		/* (non-Javadoc)
		 * @see fi.euclides.locus.MP.Strategy#interpolate(fi.euclides.model.math.Numbers)
		 */
		Punt interpolate(Numbers cc) {
			double t = cc.doubleValue();
			t = 2.0 * t - 1.0;	// -1 < t < +1 ;
			double t2 = t * t;
			double t3 = t * t2;
			double t4 = t2 * t2;
			double t5 = t4 + 1.0;
			double x = a * ( t4 - 4 * t2 + 1.0 ) / t5;
			double y = b * ( t - t3 ) / t5;
			dest.setXY(x+cx*y + tx, y + cy*x + ty);
			return copy(dest);
		}

		void recalc() {
			punten.removeAllElements();
			dest.deleteObserver(Kegelsnede2.this);
			for(int i = 0; i < N; i++)
			{
				Numbers cc = Numbers.createRational(i, N-1);
				add(cc, interpolate(cc));
			}
		}

		public void focus(Punt focus1, Punt focus2) {
			double v = b / SQRT_8;  // vertical 
			double h = a;			// horizontal
			
			v = v*v;
			h = h*h;
			double d = h - v;
			double dd = Math.sqrt(Math.abs(d)); 
			double dx = 0, dy = 0;
			if (d > 0) {
				dx = dd; dy = 0;
			} else {
				dx = 0; dy = dd;
			}
			// NIET GOED: GEEN rotate:
			double ddx = dx + cx * dy;
			double ddy = dy + cy * dx;
			
			
// rotatie c,s hoe met cx en cy
			// cos = 1, sin = 0
			// x' = x + cx*y, y' = y // cy = 0;
			// tan = 1/(1+cx);
			System.out.println(cy); // extentricity?
			focus1.setDefined(isDefined());
			focus2.setDefined(isDefined());
// TODO bepaal dx en dy uit a en b en cx,cy
			focus1.setXY(tx+ddx, ty+ddy);
			focus2.setXY(tx-ddx, ty-ddy);
		}

		public void center(Punt center) {
			center.setDefined(isDefined());
			center.setXY(tx, ty);
		}
		
	}

	public class HyperboolStrategy extends Strategy
	{
		int type() { return HYPERBOLA; }
		final Numbers HALF = Numbers.createRational(1, 2);
		public double a = 2, b = 2;	// parameters hyperbool
		public double rx = 0, ry = 0;  // rotate
		public double tx = 0;  // translate
		public double ty = 0;
		public boolean swap;
		public HyperboolStrategy(double a, double b, double tx, double ty,
				double rx, double ry) {
			swap = a > 0;
			if(swap)
			{
				double t = a;
				a = 1/Math.sqrt(-b/2);
				b = 1/Math.sqrt(t/2);
			} else {
				a = 1/Math.sqrt(-a/2);
				b = 1/Math.sqrt(b/2);
			}
			this.a = a/2;
			this.b = b/2;
			this.tx = tx;
			this.ty = ty;
			this.rx = rx;
			this.ry = ry;
		}


		Punt interpolate(Numbers cc)
		{
			double t = cc.doubleValue(); // 0 <= t <= 1		
			double c = (t - 0.5) * 4;	// -2 <= c <= +2
			if(c >= 1) c = 1/(2.0-c);
			else if ( c < -1)
				c = 1/(-2.0-c);
			t = c;
//System.out.print(cc.doubleValue() + " -> " + t + " -> ");
			double x = a * ( t - 1/t);
			double y = b * ( t + 1/t);
			if(swap)
			{
				double tt = x;
				x = y; y = tt;
			}
//System.out.println(x + "|" + y);
			dest.setXY(x+y*rx+tx, y+x*ry+ty);
			dest.setDefined(c != 0.0);
			return copy(dest);
			
		}
		
		
		void recalc() {
			punten.removeAllElements();
			dest.deleteObserver(Kegelsnede2.this);
			double d;
//System.out.println("recalc start");
			for(int i = 0; i < N; i++)
			{
// P = 0.5
				if(i == (N-1)/2)
				{
					Numbers cc = Numbers.createDouble(0.5-SMALL);
					add(cc, interpolate(cc));
					dest.setXY(Double.NaN, Double.NaN);
					dest.setDefined(false);
					addDest(HALF);
					cc = Numbers.createDouble(0.5+SMALL);
					add(cc, interpolate(cc));
					continue;
				}
// P > 0 , P < 1				
				
				double p = i/(double)(N-1);
				if(i == 0) p += SMALL;
				else if(i == N-1) p -= SMALL;
				Numbers cc = Numbers.createDouble(p);
				Punt pp = interpolate(cc);
				add(cc, pp);
			}
//System.out.println("recalc end");
		}

		public void center(Punt center) {
			center.setDefined(isDefined());
			center.setXY(tx, ty);
		}

	}
	
	
	
	/**
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @param e
	 */
	public Kegelsnede2(Punt a, Punt b, Punt c, Punt d, Punt e) {
		this();
		setA(a);
		setB(b);
		setC(c);
		setD(d);
		setE(e);
		initPunten();
	}
	
	public Kegelsnede2(Punt[] punten) {
		this(punten[0],punten[1],punten[2],punten[3],punten[4]);
	}

	public Kegelsnede2() {
		dest = new VrijPunt();
		Dpunt dp;
		source = dp = new Dpunt();
		dp.setOp(dest);
		dest.deleteObserver(source);
	}

	public String key() {
		return TYPE;
	}

	public void read(Codec codec) throws IOException {
		setA(codec.readPunt());
		setB(codec.readPunt());
		setC(codec.readPunt());
		setD(codec.readPunt());
		setE(codec.readPunt());
		initPunten();
	}

	public void write(Codec codec) throws IOException {
		codec.write(getDepend());
	}
	/* (non-Javadoc)
	 * @see fi.euclides.model.Destroyable#destroy()
	 */
	public void destroy() {
		dest.deleteObserver(this);
		super.destroy();
	}

	public void update(Observable observable, Object arg) {
		if(arg == DESTROY)
		{
			destroy();
		} else if(arg != VISIBLE)
		{
			initPunten();
			setChanged();
			notifyObservers();
		}
	}
	
	final Numbers[][] A = new Numbers[3][3], B = new Numbers[3][3], C = new Numbers[3][3];
	final Numbers[] [] FA = new Numbers[3][3];
	final Numbers[] v = new Numbers[3], w = new Numbers[3];
	
	public Numbers[][] getMatrix() {
		return A;
	}
	
	
	private void initPunten() {
		
		setDefined(a.isDefined() && b.isDefined() && c.isDefined() && d.isDefined() && e.isDefined());
		if(!defined)
		{
			punten.removeAllElements();
			return;
		}
		
		// v = a x b, w = c x d
		uit(v, a, b);
		uit(w, c, d);
		cross(A, v, w);
		uit(v, a, c);
		uit(w, b, d);
		cross(B, v, w);
		Numbers lambda = mul(e, B);
//printC(B); System.out.println("lamba = " + lambda);
		
		if(Math.abs(lambda.doubleValue())< 1)
		{
			strategy = new DegeneratedStrategy(a,c,b,d);
			strategy.recalc();
			return;
		}
		Numbers mu = Numbers.neg( mul(e, A) );
//printC(A); System.out.println("mu = " + mu);
		if(Math.abs(mu.doubleValue())< 1)
		{
			strategy = new DegeneratedStrategy(a,b,c,d);
			strategy.recalc();
			return;
		}
// todo degenerated(a,d,b,c)? or a,e,(b,c,d)
		
		mul(lambda, A);
		mul(mu, B);
		addm(C, A, B);
// symmetrisch
		C[X][Y] = C[Y][X] = Numbers.div(Numbers.add(C[X][Y] , C[Y][X]),Numbers.TWO);
		C[Z][Y] = C[Y][Z] = Numbers.div(Numbers.add(C[Z][Y] , C[Y][Z]),Numbers.TWO);
		C[X][Z] = C[Z][X] = Numbers.div(Numbers.add(C[X][Z] , C[Z][X]),Numbers.TWO);
		
		for(int i = X; i <= Z; i++)
		{
			System.arraycopy(C[i], 0, A[i], 0, 3);
			System.arraycopy(C[i], 0, FA[i], 0, 3);
			
		}
//printC(FA);
		uit(v, C[X], C[Y]);
		Numbers[] vv = C[Z];
		double det = Numbers.add(Numbers.add(Numbers.mul(v[X],vv[X]),Numbers.mul(v[Y] ,vv[Y])) , Numbers.mul(v[Z],vv[Z])).doubleValue();
//System.out.println("determinant   " + det);
//System.out.println("Basis");
//		testMul(a, C, 0.0, 0.0, 0.0, 0.0);
//		testMul(b, C, 0.0, 0.0, 0.0, 0.0);
//		testMul(c, C, 0.0, 0.0, 0.0, 0.0);
//		testMul(d, C, 0.0, 0.0, 0.0, 0.0);
//		testMul(e, C, 0.0, 0.0, 0.0, 0.0);
		
		double delta;
		{
			double a = C[X][X].doubleValue()/2;
			double b = C[X][Y].doubleValue();
			double c = C[Y][Y].doubleValue()/2;
			delta = Numbers.sub(Numbers.sqr(C[X][Y]),Numbers.mul(C[X][X],C[Y][Y])).doubleValue();
			if(Math.abs(delta) < 0.0001 && Math.abs(det)>1)
			{
				double d = C[X][Z].doubleValue();
				double e = C[Y][Z].doubleValue();
				double f = C[Z][Z].doubleValue()/2;
				strategy = new ParabolaStrategy(a,b,c,d,e,f);
				strategy.recalc();
				return;
			}
		}
		
		norm(C);
		
		
		//printC(C);
		
// find translation s.t. C[Z][.] en C[.][Z] is 0!
		double p;
		double q;
		{
			double a = C[X][X].doubleValue()/2;
			double b = C[X][Y].doubleValue();
			double c = C[Y][Y].doubleValue()/2;
			double d = C[X][Z].doubleValue();
			double e = C[Y][Z].doubleValue();
			double f = C[Z][Z].doubleValue()/2;

			if(delta == 0.0)
			{	double qb = Double.NaN;
				
				if ( b != 0 )
				{
					
					q = (c-a)/b;
					q = Math.sqrt(1 + q*q) + q;
					qb = q;
					// x -> qx+y y -> qy-x
					// a x^2 + bxy + cy^2 +dx +ey + f
					// a (qx+y)^2 + b(qx+y)(qy-x) + c(qy-x)^2 +d(qx+y) +e(qy-x) + f
					// aq^2x^2 + 2aqxy + ay^2 + bq^2xy + bqy^2 -bqx^2 -bxy + cq^2y^2-2cqyx + cx^2 + dqx +dy +eqy -ex +f
					// (aq^2-bq+c)x^2 + (2aq+bq^2-b-2cq)xy + (a+cq^2+bq)y^2 + (dq-e)x +(d+eq)y + f
					double na = a*q*q-b*q+c;
					double nb = 2*a*q+b*q*q-b-2*c*q;
					double nc = a  + b*q + c*q*q;
					double nd = d*q-e;
					double ne = d+e*q;
					a = na; b = 0; c = nc; d=nd; e =ne;
			
				}
				
				
				if( b == 0 && Math.abs(c) < 1.0e-18 && a != 0)
				{
					p = -0.5*d/a;
					f +=a*p*p+d*p;
					q = Math.sqrt(-f/a);			
					if(Double.isNaN(qb))
						strategy = new DegeneratedStrategy( p+q, 0,0,1,  p-q, 0,0,1);
					else 
						strategy = new DegeneratedStrategy(qb*( p+q ),-(p+q),1,qb,  qb*(p-q), -(p-q),1,qb);
				} else
				if(Math.abs(a) < 1.0e-18 && b == 0)
				{
// a = 0, d = 0, b = 0, e,c,f<>0 --> horizontale parallelle lijnen				
				p = -0.5*e/c; // to eliminate e
				f += c*p*p + e*p;
				q = Math.sqrt((-f)/c);

//System.out.println("p = " + p);
//System.out.println("expect " + (getA().getYd() + getD().getYd())/2.0) ;
//System.out.println("q = " + q);
//// no center: degenerated parallel lines.
//System.out.println("expect " + (getA().getYd() - getD().getYd())/2.0);
//System.out.println("qb = " + qb);
				if(Double.isNaN(qb))
					strategy = new DegeneratedStrategy(0, p+q, 1, 0,    0, p-q, 1, 0);
				else
					strategy = new DegeneratedStrategy(p+q, qb*(p+q), qb, -1, p-q, qb*(p-q), qb, -1);
				}
				
				
				strategy.recalc();
				return;
				
			} else
			{ 	q = -(d*b-2*a*e)/(b*b-4*a*c); //System.out.println(q);
				p = -(e*b-2*c*d)/(b*b-4*a*c); //System.out.println(p);
			}
			
			
		f += a*p*p + d*p + c*q*q + e*q + b*q*p;
			//System.out.println(d + " " + (b*q + 2*a*p));
			//d += b*q + 2*a*p;
			//e += b*p + 2*c*q;
// no parabola, ellips or hyperbola			
		if(Math.abs(f) < 1.0e-14)
		{
			double qb = Double.NaN;
			//a x^2 + b xy + c y^2 = 0
			// find q s.t. x > x+qy y->y-qx;
			if(b != 0.0)
			{
				qb = (c-a)/b;
				qb = Math.sqrt(1 + qb*qb)+qb;
				double na = a*qb*qb-b*qb+c;
				//double nb = 2*a*qb+b*qb*qb-b-2*c*qb;
				double nc = a  + b*qb + c*qb*qb;
				a = na;
				//b = 0;
				c = nc;
			}
			// ax^2 - cy^2 = 0;  
			a = Math.sqrt(Math.abs(a)); //y 
			c = Math.sqrt(Math.abs(c)); //x
			if(Double.isNaN(qb))
			{
				strategy = new DegeneratedStrategy(p, q, c,a,c,-a); // crossing				
			} else
				strategy = new DegeneratedStrategy(p, q, qb*c+a, qb*a-c, qb*c-a, -a*qb-c);
			// degenerated lines
			strategy.recalc();
			return;
			
		}
		
		
			C[X][Z] = C[Z][X] = Numbers.ZERO;  d = 0; //System.out.println(d);
			C[Y][Z] = C[Z][Y] = Numbers.ZERO;  e = 0; //System.out.println(e);
			C[Z][Z] = Numbers.createDouble(f*2);			   //System.out.println(f);
		}
		
	
		
		
		norm(C);
//System.out.println("na translatie");		
//testMul(a, C, p, q, 0.0, 0.0);
//testMul(b, C, p, q, 0.0, 0.0);
//testMul(c, C, p, q, 0.0, 0.0);
//testMul(d, C, p, q, 0.0, 0.0);
//testMul(e, C, p, q, 0.0, 0.0);

		
//		for(int i = X ; i <= Z; i++) {
//			for(int j = X; j<=Z; j++)
//				System.out.print(" | " + C[i][j]);
//			System.out.println();	
//		}
		double s = 0.0;
		double r = 0.0;
// nu shearing		
		{
			double a = C[X][X].doubleValue()/2;
			double b = C[X][Y].doubleValue();
			double c = C[Y][Y].doubleValue()/2;
			if(c == 0.0 && a == 0.0)
			{
				//b*x*y = 1; -> b*(y^2-x^2) = 1;
				strategy = new HyperboolStrategy(-2*b, b*2, p, q, 1, -1);
				strategy.recalc();
				return;
			}
			
			if(Math.abs(a)>Math.abs(c))
			{
				s = -b/(2*a);
				c += s*b + a*s*s;
				b += s*a*2 ;
			} else {
				r = -b/(2*c);
				a += r*b + c*r*r;
				b += r*c*2;
			}
			C[X][Y] = C[Y][X] = Numbers.createDouble(b);
			C[Y][Y] = Numbers.createDouble(c*2);
			C[X][X] = Numbers.createDouble(a*2);
 		}
//		System.out.println("na shear");		
//		testMul(a, C, p, q, s, r);
//		testMul(b, C, p, q, s, r);
//		testMul(c, C, p, q, s, r);
//		testMul(d, C, p, q, s, r);
//		testMul(e, C, p, q, s, r);
//
//		
//		for(int i = X ; i <= Z; i++) {
//			for(int j = X; j<=Z; j++)
//				System.out.print(" | " + C[i][j]);
//			System.out.println();	
//		}
		// = s = 0;
		
		//System.exit(0);
		if( C[X][X].doubleValue()*C[Y][Y].doubleValue() < 0)
			strategy = new HyperboolStrategy(C[X][X].doubleValue(),C[Y][Y].doubleValue(), p, q, s, r);
		else
			strategy = new EllipseStrategy(C[X][X].doubleValue(),C[Y][Y].doubleValue(), p, q, s, r);

		strategy.recalc();
		
	}

	public Numbers[] getPolarLine(Punt p)
	{
		if(FA[0][0]==null)
			initPunten();
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 3; j++ )
				if(FA[i][j] == null)
					FA[i][j] = Numbers.ZERO;
		}
		Numbers x = p.getX();
		Numbers y = p.getY();
		Numbers[] result = new Numbers[3];
		for (int i = 0; i < result.length; i++) {
			Numbers[] a = FA[i];
			result[i] = Numbers.add(Numbers.mul(x,a[X]), Numbers.mul(y,a[Y]));
			result[i] = Numbers.add(result[i], a[Z]);
		}
		
		return result;
	}
	
	
	/**
	 * @param fs TODO
	 * 
	 */
	public void printC(Numbers[][] fs) {
		for(int i = X ; i <= Z; i++) {
			for(int j = X; j<=Z; j++)
				System.out.print(" | " + fs[i][j].doubleValue());
			System.out.println();	
		}
	}

	private void uit(double[] v, double[] a, double[] b) {
		v[Z] = a[X] * b[Y] - a[Y] * b[X];
		v[X] = a[Y] * b[Z] - a[Z] * b[Y];
		v[Y] = a[Z] * b[X] - a[X] * b[Z];
	}
	private void uit(Numbers[] v, Numbers[] a, Numbers[] b) {
		v[Z] = Numbers.sub(Numbers.mul(a[X],b[Y]) , Numbers.mul(a[Y], b[X]));
		v[X] = Numbers.sub(Numbers.mul(a[Y],b[Z]) , Numbers.mul(a[Z], b[Y]));
		v[Y] = Numbers.sub(Numbers.mul(a[Z],b[X]) , Numbers.mul(a[X], b[Z]));
	}

	/**
	 * 
	 */
	private void norm() {
		norm(C);
	}

	/**
	 * @param numbers TODO
	 * 
	 */
	private void norm(Numbers[][] C) {
		if(C[Z][Z].doubleValue() == 0)
			return;
		for(int i = X; i <= Z; i++)
			for(int j = X; j <= Z; j++)
				C[i][j] = Numbers.div(C[i][j], Numbers.div(C[Z][Z], Numbers.createInteger(-2)));
	}

	private void addm(double[][] c, double[][] a, double[][] b) {
		for(int i = X; i <= Z; i++)
			for(int j = X; j <= Z; j++)
				c[i][j] = a[i][j] + b[i][j];
	}

	private void addm(Numbers[][] c, Numbers[][] a, Numbers[][] b) {
		for(int i = X; i <= Z; i++)
			for(int j = X; j <= Z; j++)
				c[i][j] = Numbers.add(a[i][j],b[i][j]);
	}

	private void mul(double f, double[][] a) {
		for(int i = X; i <= Z; i++)
			for(int j = X; j <= Z; j++)
				a[i][j] *= f;
	}
	private void mul(Numbers f, Numbers[][] a) {
		for(int i = X; i <= Z; i++)
			for(int j = X; j <= Z; j++)
				a[i][j] = Numbers.mul(a[i][j],f);
	}

	final Numbers[] mh = new Numbers[3];
	final Numbers[] mt = new Numbers[3];
	public Numbers mul(Punt p, Numbers[][] a) {
		mt[X] = p.getX();
		mt[Y] = p.getY();
		mt[Z] = Numbers.ONE;
		mh[X] = mh[Y] = mh[Z] = Numbers.ZERO;
		for(int i = X; i <= Z; i++)
			for(int j = X; j <= Z; j++)
				mh[i] = Numbers.add(mh[i],Numbers.mul(a[i][j] , mt[j]));
		return Numbers.add(Numbers.add(Numbers.mul(mt[X],mh[X]), Numbers.mul(mt[Y],mh[Y])),Numbers.mul(mt[Z],mh[Z]));
	}
	
	public Numbers incident(Punt p)
	{
		return mul(p, A);
	}
	
	private void testMul(Punt punt, Numbers[][] a, double p, double q, double s, double r)
	{
//		punt = new Punt(punt.getXd()-s*(punt.getYd()-q) - p, 
//						punt.getYd()-r*(punt.getXd()-p) - q);
		
		System.out.println(mul(punt, a));
	}

	private void cross(double[][] a, double[] v, double[] w) {
		for(int i = X; i <= Z; i++)
			for(int j = X; j <= Z; j++)
				a[i][j] = v[i] * w[j];
		
	}
	private void cross(Numbers[][] a, Numbers[] v, Numbers[] w) {
		for(int i = X; i <= Z; i++)
			for(int j = X; j <= Z; j++)
				a[i][j] = Numbers.mul(v[i],w[j]);
		
	}

	static final int X = 0, Y = 1, Z = 2;
	private void uit(double[] v, Punt a, Punt b) {
		v[Z] = a.getXd() * b.getYd() - a.getYd() * b.getXd();
		v[X] = a.getYd()             -             b.getYd();
		v[Y] =             b.getXd() - a.getXd();
	}

	private void uit(Numbers[] v, Punt a, Punt b) {
		v[Z] = Numbers.sub(Numbers.mul(a.getX(),b.getY()), Numbers.mul(a.getY(),b.getX()));
		v[X] = Numbers.sub( a.getY(),b.getY());
		v[Y] = Numbers.sub( b.getX(),a.getX());
	}
	/**
	 * @return the a
	 */
	public Punt getA() {
		return a;
	}
	/**
	 * @param a the a to set
	 */
	public void setA(Punt a) {
		this.a = a;
		a.addObserver(this);
	}
	/**
	 * @return the b
	 */
	public Punt getB() {
		return b;
	}
	/**
	 * @param b the b to set
	 */
	public void setB(Punt b) {
		this.b = b;
		b.addObserver(this);
	}
	/**
	 * @return the c
	 */
	public Punt getC() {
		return c;
	}
	/**
	 * @param c the c to set
	 */
	public void setC(Punt c) {
		this.c = c;
		c.addObserver(this);
	}
	/**
	 * @return the d
	 */
	public Punt getD() {
		return d;
	}
	/**
	 * @param d the d to set
	 */
	public void setD(Punt d) {
		this.d = d;
		d.addObserver(this);
	}
	/**
	 * @return the e
	 */
	public Punt getE() {
		return e;
	}
	/**
	 * @param e the e to set
	 */
	public void setE(Punt e) {
		this.e = e;
		e.addObserver(this);
	}

	/**
	 * @return the defined
	 */
	public boolean isDefined() {
		return defined;
	}

	/**
	 * @param defined the defined to set
	 */
	public void setDefined(boolean defined) {
		this.defined = defined;
	}

	@Override
	public PointOnAlgorithm<MP> getAlgo() {
		return PointOnConic.INSTANCE;
	}
	
	public Destroyable[] getImage(Destroyable mirror)
	{
		if(mirror instanceof Cirkel)
		{
			return super.getImage(mirror);
		}
		
		Punt[]depend = (Punt[]) getDepend();
		for (int i = 0; i < depend.length; i++) {
			depend[i] = depend[i].getImage(mirror,this);			
		}
		return new Destroyable[] { depend[0], depend[1], depend[2], depend[3], depend[4], new Kegelsnede2(depend)};
	}

	public boolean incident(Destroyable other) {
		Punt[]depend = (Punt[]) getDepend();
		for (int i = 0; i < depend.length; i++) {
			if(depend[i] == other)
				return true;			
		}	
		return super.incident(other);
	}
	
	public void getCenter(Punt center)
	{
		strategy.center(center);
	}

	public void visit(Visitor v) {
		v.visitKegelsnede(this);		
	}
	
}
