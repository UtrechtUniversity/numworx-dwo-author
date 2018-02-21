package fi.euclides.model;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import fi.euclides.util.Hashtable;

import java.util.Vector;

import fi.euclides.event.NameMapper;
import fi.euclides.model.FocusPunt;
import fi.euclides.model.Kegelsnede2;
import fi.euclides.model.Locus;
import fi.euclides.model.MP;
import fi.euclides.model.RaakLijnConic;
import fi.euclides.model.RaaklijnLocus;
import fi.euclides.model.SnijpuntLijn;
import fi.euclides.model.Triangle;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.Arrays;
import fi.euclides.util.DComparator;
import fi.euclides.util.Observable;
import fi.euclides.util.Observer;

public class Model extends Observable implements Observer, NameMapper {
	public static final String SELECT = "SELECT";
	public static final String DELAY  = "DELAY";
	private Vector<Destroyable> lijnen = new Vector<Destroyable>();
	private Vector<Punt> punten = new Vector<Punt>();
	private Vector<Destroyable> select = new Vector<Destroyable>();
	
	private Hashtable<Destroyable, Vector<Destroyable>> trail = new Hashtable<Destroyable, Vector<Destroyable>>();
	private Vector<Pair<Observable, Observer>> delay = new Vector<Pair<Observable, Observer>>();
	
	public synchronized void executeDelay() {
		while (!delay.isEmpty()) {
			@SuppressWarnings({ "unchecked" })
			Pair<Observable, Observer> p[] = new Pair[delay.size()];
			delay.copyInto(p);
			delay.removeAllElements();
			for (int i = 0; i < p.length; i++) {
				Observable observable = p[i].getA();
				Observer   observer   = p[i].getB();
				observer.update(observable, DELAY);
			}
		}
	}
	
	public synchronized void addDelay(Observable a, Observer b)
	{
		Pair<Observable, Observer> p = new Pair<Observable, Observer>(a,b)
				{
					@Override
					public boolean equals(Object other) {
						if(other == this) return true;
						if(other == null) return false;
						if(getClass() != other.getClass()) return false;
						Pair<?,?> pair = (Pair<?,?>) other;
						return getA() == pair.getA() && getB() == pair.getB();
					}
				};
		if(!delay.contains(p))
				delay.addElement(p);
	}
	
	
	private int index;
	
	public void reconnect()
	{
		addThis(lijnen);
		addThis(punten);
	}
	
	public void setLijnen(Vector lijnen) {
		this.lijnen = lijnen;
	}

	public Vector<Destroyable> getLijnen() {
		return lijnen;
	}
	public void setPunten(Vector punten) {
		this.punten = punten;
	}
	
	public Vector<Punt> getPunten() {
		return punten;
	}
	public void setSelect(Vector<Destroyable> select) {
		this.select = select;
	}
	public Vector<Destroyable> getSelect() {
		return select;
	}
	
	/**
	 * Maak instance van MiddelPunt.
	 * De selectie moet twee Punten bevatten of één lijnstuk
	 * @return een Punt in het midden
	 */
	public Punt buildMiddelPunt() {
		if(select.size() == 2)
		{
			Object o1 = select.firstElement();
			Object o2 = select.lastElement();
			if(o1 instanceof Punt && o2 instanceof Punt) 
			{
				Punt punt  = new MiddelPunt((Punt)o1,(Punt)o2);
				return addPunt(punt);
			} 
		} else if(select.size() == 1) {
			Object o = select.firstElement();
//			if(o instanceof Segment)
//			{
//				Segment s = (Segment) o;
//				Punt p = new MiddelPunt(s.getP1(), s.getP2());
//				return addPunt(p);	
//			} else
			if(o instanceof Cirkel) 
			{
				Cirkel c = (Cirkel) o;
				Punt p = c.getCenter();
				if(p.getIndex()==0)
				{
					return addPunt(new ZwaartePunt(c));
				}
			} else
			if(o instanceof Triangle|| o instanceof Kegelsnede2 || o instanceof Segment)
			{
				return addPunt(new ZwaartePunt((Destroyable) o));
			} 
		}
		return null;
	}
	
	public Destroyable buildSpiegel() { 
		if(select.size()==2)
		{
			Destroyable source = select.firstElement();
			Destroyable mirror = select.lastElement();
			Destroyable[] image = source.getImage(mirror);
			Destroyable d = null;
			if(image != null)
			{
				clearSelection();
				for (int i = 0; i < image.length; i++) {
					d = image[i];
					if(d instanceof Punt)
					{
						d = addAlways((Punt) d, punten);
					} else
						d = addAlways(d, lijnen);
				}
			}
			return d;
		}
		return null;
	}
	
	public Lijn buildRaaklijn(final Numbers x, final Numbers y) { 
		if(select.size() == 2)
		{
			Destroyable first = select.firstElement();
			Destroyable last = select.lastElement();
			final Lijn[] ll = new Lijn[1]; 
			
			if(last instanceof Punt) {
				Destroyable tmp = first;
				first = last;
				last  = tmp;
			}
			if ( !(first instanceof Punt)) return null;
			final Punt p = (Punt) first;
			
			last.visit(new Visitor() {

				public void visitPunt(Punt p) {
				}

				public void visitLijn(Lijn last) {
					ll[0] = last;
				}

				public void visitCirkel(Cirkel c) {
					if(c.incident(p) || p.incident(c))
						ll[0] = new Poollijn(p, c);
					else
						ll[0] = new RaakLijnCirkel(c, p, x, y);
				}

				public void visitSegment(Segment s) {
					ll[0] = new CarryingLine(s);
				}

				public void visitLabel(Label label) {
				}

				public void visitTriangle(Triangle t) {
				}

				public void visitKegelsnede(Kegelsnede2 k) {
					if(p != null && k != null && (k.incident((Destroyable)p) || p.incident(k)))
						ll[0] = new Poollijn(p, k);
					else {
						ll[0] = new RaakLijnConic(k, p, x, y);
					}					
				}

				public void visitLocus(Locus l) {
					ll[0] = new RaaklijnLocus(l, p);
				}

				public void visitBoog(Boog b) {
					visitCirkel(b.base);
				}});
			
			Lijn l = ll[0];
			if(l != null)
				add(l, lijnen);
			return l;
		}
		return null;
	}
	
	public Coordinaten buildCoordinaten(Label cx, Label cy)
	{
		Coordinaten p = new Coordinaten(cx, cy, getO(), getU());
		addPunt(p);
		return p;
	}
	
	public void toggleTrail() {
		if(select.size() == 1) {
			Destroyable o = (Destroyable) select.firstElement();
			if(!trail.containsKey(o))
			{
				Destroyable copy = o.trail();
				if(copy != null && o.isDefined())
				{
					Vector<Destroyable> v = new Vector<Destroyable>();
					v.addElement(copy);
					trail.put(o, v);
				}
			}
			else 
				trail.remove(o);
			
		}
	}

	public Punt buildDPunt(Numbers x, Numbers y)
	{
		Punt p = null;
		if(select.size() == 1 && select.firstElement() instanceof Punt)
		{
			p = new Dpunt(x, y, (Punt) select.firstElement());
			addPunt(p);
		} else
			p = buildPunt(x,y);
		return p;
	}
	
	public Punt buildPunt(Numbers x, Numbers y) {
		Punt p = null;
		if(select.size() >= 2)
		{
			Object o1 = select.firstElement();
			Object o2 = select.lastElement();
			if(o1 instanceof Lijn && o2 instanceof Lijn) 
			{
				p = new SnijPunt((Lijn)o1,(Lijn)o2);
			} else if(o1 instanceof Rondje && o2 instanceof Rondje)
			{
				p = new CirkelSnijpunt( (Rondje)o1, (Rondje) o2, x, y);
			} else if(o1 instanceof Lijn && o2 instanceof Rondje)
			{
				p = new CirkelLijnSnijpunt( (Lijn)o1, (Rondje)o2, x, y);
			} else if(o2 instanceof Lijn && o1 instanceof Rondje)
			{
				p = new CirkelLijnSnijpunt( (Lijn)o2, (Rondje)o1, x, y);
			} else if(o1 instanceof Kegelsnede2 && o2 instanceof Lijn)
			{
				p = new SnijpuntLijn((Kegelsnede2)o1, (Lijn)o2, x, y);
			} else if(o2 instanceof Kegelsnede2 && o1 instanceof Lijn)
			{
				p = new SnijpuntLijn((Kegelsnede2)o2, (Lijn)o1, x, y);
			}
		} else if(select.size() == 1)
		{
			Object o = select.firstElement();
			if(o instanceof Segment)
			{
				Segment s = (Segment)o;
				p = s.getP2();
				if(Numbers.hypot(Numbers.sub(x, p.getX()), Numbers.sub(y, p.getY())).doubleValue()<4)
				{
					p.setVisible(true);						
				} else 								
				{	p = s.getP1();
					if(Numbers.hypot(Numbers.sub(x, p.getX()), Numbers.sub(y, p.getY())).doubleValue()<4)
					{
						p.setVisible(true);
					} else
						p = new PuntOp<Lijn>(x, y, s, s.getAlgo());
				}
			} else
			if(o instanceof Ray)
			{
				Ray s = (Ray)o;
				p = s.getP1();
				if(Numbers.hypot(Numbers.sub(x, p.getX()), Numbers.sub(y, p.getY())).doubleValue()<4)
				{
						p.setVisible(true);
				} else
					p = new PuntOp<Lijn>(x, y, s, s.getAlgo());
			} else
			
			if(o instanceof OpObject)
			{	p = ((OpObject<?>) o).pointOn(x, y);
			}
		}
		if(p == null) 
			p = new VrijPunt(x, y);
		return addPunt(p);
	}
	
	private Punt addPunt(Destroyable p) {
		return (Punt) add(p, punten);
	}

	public void destroy() {
		final int size = select.size();
		if(size == 0)
			return;
		Destroyable[] elements = new Destroyable[size];
		select.copyInto(elements);
		clearSelection();
		for (int i = 0; i < size; i++) {
			elements[i].destroy();
		}
		int li = lijnen.isEmpty()? 0 :((Destroyable) lijnen.lastElement()).getIndex();
		int pi = punten.isEmpty()? 0 :((Destroyable) punten.lastElement()).getIndex();
		setIndex( Math.max(pi, li));
	}
	
	public void destroyAll() {
		delay.clear();
		clearSelection();
		while(!lijnen.isEmpty()) { lijnen.lastElement().destroy(); }
		while(!punten.isEmpty()) { punten.lastElement().destroy(); }
		setIndex(0);
	}
	
	public void update(Observable observable, Object arg) {
		if (arg == Destroyable.DESTROY)
		{
// destroy observables in the delay list
			synchronized(delay) {
			if (!delay.isEmpty()) {
				Iterator<Pair<Observable, Observer>> iter = delay.iterator();
				while (iter.hasNext()) {
					Pair<Observable, Observer> pair = (Pair<Observable, Observer>) iter
							.next();
					if(pair.getA() == observable) iter.remove();
			}}}
			select.removeElement(observable);
			punten.removeElement(observable);
			lijnen.removeElement(observable);
			trail.remove(observable);
		} else if(trail.containsKey(observable))
		{
			if(arg == DELAY) {
				Destroyable p = (Destroyable)observable;
				if(p.isDefined())
					trail.get(p).addElement(p.trail());
			} else
				addDelay(observable, this);
		}
		setChanged();
		notifyObservers(arg);
	}

	public Lijn buildLijn() {
		if(select.size() == 1)
		{
			Object first = select.firstElement();
			if(first instanceof Segment)
			{
				return (Lijn) add(new CarryingLine((Segment) first), lijnen);
			}
			if(first instanceof Ray)
			{
				return (Lijn) add(new CarryingLine((Ray) first), lijnen);
			}
		}
		return twoPuntBuilder(new PuntenLijn(), true);
	}
	

	private <T extends PuntenLijn> T twoPuntBuilder(T proto, boolean sort) {
		Destroyable s[] = new Destroyable[select.size()];
		select.copyInto(s);
		return twoPuntBuilder(proto, sort, s);
	}
	
	private <T extends PuntenLijn> T twoPuntBuilder(T lijn, boolean sort, Destroyable[] select)
	{
		if(select.length == 2)
		{
			Object o1 = select[0];
			Object o2 = select[1];
			if(o1 instanceof Punt && o2 instanceof Punt) 
			{
					Punt p1 = (Punt) o1;
					Punt p2 = (Punt) o2;
					if(sort && p1.getIndex() > p2.getIndex()) {
						Punt tmp = p1; p1 = p2; p2 = tmp;
					}
					lijn.setP1(p1);
					lijn.setP2(p2);
					
					add(lijn, lijnen);
					return lijn;
			}
		}
		return null;
		
	}
	public Lijn buildBissectrice() {
		if(select.size() == 3)
		{
			Object o1 = select.firstElement();
			Object o2 = select.elementAt(1);
			Object o3 = select.lastElement();
			if(o1 instanceof Punt && o2 instanceof Punt && o3 instanceof Punt) 
			{
				Lijn lijn = new Bissectrice((Punt)o1, (Punt)o2, (Punt) o3);
				add(lijn, lijnen);
				return lijn;
			}
		}
		return null;
	}

	public Triangle buildTriangle()
	{
		Destroyable s[] = new Destroyable[select.size()];
		select.copyInto(s);
		return buildTriangle(s);
	}
		
	public Segment buildSegment() {
		return twoPuntBuilder(new Segment(), false);
	}

	@Deprecated
	private Destroyable add(Destroyable d, Vector vector) {
		clearSelection();
		if(contains(d, vector))
		{
			tester.getD().setVisible(true); // niet als label?
			d.destroy();
			return tester.getD(); // drop on the floor...
		}
		return addAlways(d, vector);
	}
	
	private <T extends Destroyable> T addNotIfVisible(T d, Vector<T> vector) {
		clearSelection();
		if(contains(d, vector)) {
			if(tester.getD().isVisible()) {
				d.destroy();
				return (T) tester.getD();
			}
		}
		return addAlways(d, vector);
	}

	private <T extends Destroyable> T addAlways(T d, Vector<? super T> vector) {
		vector.addElement(d);
		d.addObserver(this);
		d.setIndex(nextIndex());
		setChanged();
		notifyObservers(d);
		return d;
	}

	static class Contains implements Visitor
	{
		protected boolean found;
		protected Destroyable d;
		protected Cirkel c;
		protected Punt p;
		protected Segment s;
		protected Lijn l;

		/**
		 * @return the found
		 */
		public boolean isFound() {
			return found;
		}

		public Destroyable getD() {
			return d;
		}

		/**
		 * @param found the found to set
		 */
		public void setFound(boolean found) {
			this.found = found;
		}

		public void find(Destroyable d) {
			this.d = d;
			found = false;
			if(d instanceof Cirkel)
				c = (Cirkel)d;
			else 
				c = null;
			if(d instanceof Punt)
				p = (Punt)d;
			else 
				p = null;
// relatie: Lijn en Segment
			if(d instanceof Segment)
			{
				s = (Segment)d;
				l = null;
			}
			else
			{	s = null;
				if(d instanceof Lijn)
					l = (Lijn) d;
				else
				l = null;
			}
		}

		public void visitCirkel(Cirkel c) {
			if(this.c != null)
			{
				found = c.getCenter() == this.c.getCenter() &&
						circa(c.getR(), this.c.getR());
				if(found) d = c;
			}
		}

		public void visitBoog(Boog b) {
			visitCirkel(b.base);
		}
		
		double  EPS = 0.05; // tolerance
		private boolean circa(double r, double r2) {
			return Math.abs(r-r2) <= EPS;
		}

		public void visitLabel(Label label) {
			found = label.equals(d);	
			if(found) d=label;
		}

		
		
		
		protected double outer(double x1, double y1, double x2, double y2, double x3, double y3)
		{
			double d = 
				(((x1*(y2-y3))-(y1*(x2-x3)))+((x2*y3)-(y2*x3)));
				return Math.abs(d);

		}
		
		public void visitLijn(Lijn l) {
			if(this.l instanceof Ray)
			{
				found = l instanceof Ray && Arrays.equals(l.getDepend(), this.l.getDepend());
				if(found) d = l;
			} else
			if(this.l != null)
			{
				double d2 = outer(l.getX1(), l.getY1(), l.getX2(), l.getY2(), this.l.getX1(), this.l.getY1());
				double d1 = outer(l.getX1(), l.getY1(), l.getX2(), l.getY2(), this.l.getX2(), this.l.getY2());
				found = (d1+d2)<= EPS && !(l instanceof Ray);
				if(found ) d = l;
			}
		}

		public void visitMP(MP mp) {
			found = mp.equals(d);
			if(found) d = mp;
		}

		public void visitTriangle(Triangle t)
		{
			visitMP(t);
		}
		public void visitLocus(Locus t)
		{
			visitMP(t);
		}
		public void visitKegelsnede(Kegelsnede2 t)
		{
			visitMP(t);
		}
		
		public void visitPunt(Punt p) {
			if(this.p != null)
			{
				found = circa(p.getXd(), this.p.getXd()) && 
						circa(p.getYd(), this.p.getYd());
				if(found) d = p;
			}
		}

		public void visitSegment(Segment s) {
			if(this.s != null)
			{
				found = s.getP1()==this.s.getP1() && s.getP2() == this.s.getP2();
				if(found) d = s;
			}
		}
	}
	private Contains tester = new Contains();
	public boolean addAlways = true;
 	/**
	 * @param d
	 * @param vector
	 * @return
	 */
	private boolean contains(Destroyable d, Vector vector) {
		if(addAlways) {
			boolean contains = vector.contains(d); // no add under equals, destroyall breaks!
			if(contains) System.err.println("problem with " + d);
			return contains;
		} 
		Enumeration e = vector.elements();
		tester.find(d);
		while (e.hasMoreElements()) {
			Destroyable d2 = (Destroyable) e.nextElement();
			d2.visit(tester);
			if(tester.isFound())
			{
				return true;
			}
		}
		return false;
	}

	public Destroyable add(Label label)
	{
		return add(label, lijnen);
	}
	
	public Boog buildBoog() {
		Boog result =  buildBoog(select.toArray(new Destroyable[select.size()]));
		return result;
	}
	
	public Cirkel buildCirkel() {
		Cirkel cirkel = null;
		Destroyable[] s = new Destroyable[select.size()];
		select.copyInto(s);
		return buildCirkel(s);
		
	}

	public void read(Codec codec) throws IOException
	{
		setPunten(codec.readVector());		
		setLijnen(codec.readVector());
		setSelect(codec.readVector());
		reconnect();
	}
	
	private void addThis(Vector v) {
		Enumeration e  = v.elements();
		while (e.hasMoreElements()) {
			Observable object = (Observable) e.nextElement();
			object.addObserver(this);
		}
	}
	
	public void write(Codec codec) throws IOException
	{
		codec.writeVector(getPunten());
		codec.writeVector(getLijnen());
		codec.writeVector(getSelect());
	}
	public Lijn buildLoodlijn() {
		if(select.size() == 2)
		{
			Enumeration<Destroyable> i = select.elements();
			Object o1 = i.nextElement();
			Object o2 = i.nextElement();
			if(o1 instanceof Lijn && o2 instanceof Punt) 
			{
				Lijn lijn  = new LoodLijn((Lijn)o1,(Punt)o2);
				add(lijn, lijnen);
				return lijn;
			} else if(o2 instanceof Lijn && o1 instanceof Punt) 
			{
				Lijn lijn  = new LoodLijn((Lijn)o2,(Punt)o1);
				add(lijn, lijnen);
				return lijn;
			} else if(o1 instanceof Punt && o2 instanceof Punt)
			{
				ConflictLijn l = new ConflictLijn();
				l.p1 = (Punt) o1;
				l.p2 = (Punt) o2;
				l.p1.addObserver(l);
				l.p2.addObserver(l);
				return (Lijn) add(l, lijnen);
				
			}
		}
		return null;
	}
	public Lijn buildParallelLijn() {
		if(select.size() == 2)
		{
			Enumeration i = select.elements();
			Object o1 = i.nextElement();
			Object o2 = i.nextElement();
			if(o1 instanceof Lijn && o2 instanceof Punt) 
			{
				Lijn lijn  = new ParallelLijn((Lijn)o1,(Punt)o2);
				add(lijn, lijnen);
				return lijn;
			} else if(o2 instanceof Lijn && o1 instanceof Punt) 
			{
				Lijn lijn  = new ParallelLijn((Lijn)o2,(Punt)o1);
				return (Lijn) add(lijn, lijnen);
			}
		}
		return null;
	}
	
	
	public Locus buildLocus() {
		if(select.size() == 2)
		{
			PuntOp source = (PuntOp) select.firstElement();
			Punt dest   = (Punt) select.lastElement();
			Locus l = new Locus(source,dest,this);
			add(l, lijnen);
			return l;
		}
		return null;
	}
	
	public Kegelsnede2 buildKegelsnede() {
		if(select.size() == 5)
		{
			Punt[] punten = new Punt[5];
			select.copyInto(punten);
			DComparator.sort(punten);
			Kegelsnede2 l = new Kegelsnede2(punten);
			add(l, lijnen);
			return l;
		}
		return null;
	}
	
	
	public Punt[] buildFocus() {
		if(select.firstElement() instanceof Cirkel)
		{
			Punt c = buildMiddelPunt();
			return new Punt[] { c };
		}
		
		Kegelsnede2 l = (Kegelsnede2) select.firstElement();
		FocusPunt f1, f2;
		f1 = new FocusPunt(l);
		f2 = new FocusPunt(l, f1);
		addPunt(f1);
		addPunt(f2);
		return new Punt[] {f1, f2};
		
	}
	
	public void visitLijnen(Visitor v) {
		visitVector(v, getLijnen());
	}

	public void visitPunten(Visitor v) {
		visitVector(v, getPunten());
	}
	
	public void visitTrail(Visitor v) {
		if(trail.isEmpty())
			return;
		Enumeration<Vector<Destroyable>> e;
		Enumeration<Destroyable> e1;
		e = trail.elements();
		while (e.hasMoreElements()) {
			Vector<Destroyable> vector = e.nextElement();
			e1 = vector.elements();
			while (e1.hasMoreElements()) {
				Destroyable object = (Destroyable) e1.nextElement();
					object.visit(v);
			}
		}
	}
	
	private void visitVector(Visitor v, Vector<? extends Destroyable> punten)
	{
		for (Enumeration<? extends Destroyable> iter = punten.elements(); iter.hasMoreElements();) {
			Destroyable punt = iter.nextElement();
			if(punt.isDefined() && punt.isVisible())
				punt.visit(v);
		}
	}
	
	public void toggle(Destroyable o) {
		if ( !select.removeElement(o))
			select.addElement(o);	
		setChanged();
		notifyObservers(SELECT);
	}

	public void clearSelection() {
		if(!select.isEmpty())
		{
			setChanged();
			select.removeAllElements();
			notifyObservers(SELECT);
		}
	}
	
	
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int index)
	{
		this.index = index;
	}
	
	int nextIndex()
	{
		return ++index;
	}
	
	public Punt getO() {
		if(punten.isEmpty())
			return null;
		return (Punt) punten.firstElement();
	}
	
	public Punt getU() {
		if(punten.size()<2)
			return null;
		return (Punt) punten.elementAt(1);
	}

	public String toString(Destroyable d) {
		return String.valueOf(d);
	}
	
	public Destroyable fromString(String name)
	{
		Enumeration e = getPunten().elements();
		while (e.hasMoreElements()) {
			Punt object = (Punt) e.nextElement();
			if(name.equals(toString(object)))
				return object;
		}
		e = getLijnen().elements();
		while (e.hasMoreElements()) {
			Destroyable object = (Destroyable) e.nextElement();
			if(name.equals(toString(object)))
				return object;
		}
		return null;
	}

	public Ray buildRay() {
		return twoPuntBuilder(new Ray(), false); 
		
	}

	public void buildPoollijn() {
		if(select.size() == 2)
		{
			Poollijn pl = null;
			Object first = select.firstElement();
			Object last = select.lastElement();
			if(first instanceof Punt && last instanceof Cirkel)
			{
				pl = new Poollijn((Punt)first, (Cirkel)last);
			} else
				if(first instanceof Punt && last instanceof Kegelsnede2)
				{
					pl = new Poollijn((Punt)first, (Kegelsnede2)last);
				} else
					if(first instanceof Cirkel && last instanceof Punt)
					{
						pl = new Poollijn((Punt)last, (Cirkel)first);
					} else
						if(first instanceof Kegelsnede2 && last instanceof Punt)
						{
							pl = new Poollijn((Punt)last, (Kegelsnede2)first);
						} 
				if(pl != null)
					add(pl,lijnen);
		}
	}

	public Destroyable add(Destroyable d) {
		if(d instanceof Punt)
			return addPunt((Punt) d);
		return add(d, lijnen);
	}

	public void rename(Destroyable l, String name) {
	}

	public Lijn buildLijn(Destroyable[] depend) {
		return twoPuntBuilder(new PuntenLijn(), true, depend);
	}
	
	public Ray buildRay(Destroyable[] depend) {
		return twoPuntBuilder(new Ray(), false, depend);
	}
	public Segment buildSegment(Destroyable[] depend) {
		return twoPuntBuilder(new Segment(), false, depend);		
	}

	public Cirkel buildCirkel(Destroyable[] depend) {
		Cirkel cirkel = createCirkel(depend);
		if(cirkel != null)
		{
			add(cirkel, lijnen);
		}
		return cirkel;
	}

	public Cirkel createCirkel(Destroyable[] depend) {
		Cirkel cirkel = null;
		if(depend.length == 3)
		{
			Object o1 = depend[0];
			Object o2 = depend[1];
			Object o3 = depend[2];
			if(o1 instanceof Punt && o2 instanceof Punt && o3 instanceof Punt) 
			{
				cirkel = new Cirkel3((Punt)o1,(Punt)o2, (Punt)o3);
			}
		} else if(depend.length == 2) {
			Object o1 = depend[0];
			Object o2 = depend[1];
			if(o1 instanceof Punt && o2 instanceof Segment) {
				cirkel = new Cirkel((Punt)o1, ((Segment)o2));
			} else if(o1 instanceof Segment && o2 instanceof Punt)
			{
				cirkel = new Cirkel((Punt)o2, ((Segment)o1));
			} else if(o1 instanceof Punt && o2 instanceof Punt) 
			{
				cirkel = new Cirkel((Punt)o1,(Punt)o2);
			} else if(o1 instanceof Punt && o2 instanceof Label)
			{
				cirkel = new CirkelRadius((Punt)o1, (Label)o2, getO(), getU());
			} else if(o2 instanceof Punt && o1 instanceof Label)
			{
				cirkel = new CirkelRadius((Punt)o2, (Label)o1, getO(), getU());
			}
		}
		return cirkel;
	}

	public Boog buildBoog(Destroyable[] depend) {
		Boog boog = createBoog(depend);
		if(boog != null) {
			add(boog, lijnen);
		}
		return boog;
	}

	public Boog createBoog(Destroyable[] depend) {
		Boog boog = null;
		
		if (depend.length == 3) {
			Object o1 = depend[0];
			Object o2 = depend[1];
			Object o3 = depend[2];
			if(o1 instanceof Punt && o2 instanceof Punt && o3 instanceof Punt) 
			{
				boog = new Boog((Punt)o1,(Punt)o2, (Punt)o3);
			} else 
				if(o1 instanceof Punt && o2 instanceof Punt && o3 instanceof Label) 
				{
					boog = new BoogHoek((Punt)o1,(Punt)o2, (Label)o3);
				}	
		}
		
		if (depend.length == 4) {
			Object o1 = depend[0];
			Object o2 = depend[1];
			Object o3 = depend[2];
			Object o4 = depend[3];
			if (o1 instanceof Punt && o2 instanceof Label && o3 instanceof Label && o4 instanceof Label) {
				boog = new BoogRadiusHoek( (Punt)o1, (Label)o2, (Label)o3, (Label)o4, getO(), getU());
			}	
		}
		return boog;
	}

	public Triangle buildTriangle(Destroyable[] depend) {		
		if(depend.length == 3)
		{
			Object o1 = depend[0];
			Object o2 = depend[1];
			Object o3 = depend[2];
			if(o1 instanceof Punt && o2 instanceof Punt && o3 instanceof Punt) 
			{
				Punt[] pp = new Punt[3];
				Object[] oo = pp;
				oo[0] = o1; oo[1] = o2; oo[2] = o3;
				DComparator.sort(pp);
				Triangle triangle = new Triangle(pp);
				if(triangle.isDefined())
				{	
					add(triangle, lijnen);
					return triangle;
				} 
			}
		}
		return null;
		
	}

	/**
	 * Helper voor AddPolygonHandler. sort points
	 * @param p
	 * @param q
	 * @return
	 */
	public Segment buildSegment(Punt p, Punt q) {
		return twoPuntBuilder(new Segment(), true, new Punt[] { p, q  });
	}
}
