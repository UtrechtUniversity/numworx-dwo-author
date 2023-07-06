package fi.euclides.model;

import java.io.IOException;

import fi.euclides.model.Kegelsnede2;
import fi.euclides.model.Locus;
import fi.euclides.model.MP;
import fi.euclides.model.Triangle;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.Observable;
import fi.euclides.util.Observer;

public class ZwaartePunt extends Punt implements Visitor {
	
	class SegmentStrategy extends Strategy {

		Punt a,b;
		public SegmentStrategy(Segment s) {
			a = s.getP1();
			b = s.getP2();
		}

		void recalc() {
			setXY(Numbers.div(Numbers.add(a.getX(),b.getX()), Numbers.TWO),
				  Numbers.div(Numbers.add(a.getY(), b.getY()), Numbers.TWO)
			);
		}

	}

	public static final String TYPE = "Pz";

	public void visitPunt(Punt p) {
	}
	public void visitLijn(Lijn l) {			
	}
	public void visitCirkel(Cirkel c) {
		strategy = new CirkelStrategy(c);
	}
	
	public void visitBoog(Boog b) {
		visitCirkel(b.base);
	}
	public void visitSegment(Segment s) {
		strategy = new SegmentStrategy(s);
	}
	public void visitLabel(Label label) {			
	}
	
	abstract class Strategy {
		abstract void recalc();
		boolean isDefined() {
			return depend[0].isDefined();
		}		
	}

	class CirkelStrategy extends Strategy {
		Punt c;

		void recalc() {
			setXY(c.getX(), c.getY());	
		}
		
		CirkelStrategy(Cirkel c) {
			this.c = c.getCenter();
		}
	}
	
	class TriangleStrategy extends Strategy {
		Punt a,b,c;

		void recalc() {
			Numbers three = Numbers.createInteger(3);
			setXY(
				Numbers.div(Numbers.add(Numbers.add(a.getX(), b.getX()),c.getX()), three),
				Numbers.div(Numbers.add(Numbers.add(a.getY(), b.getY()),c.getY()), three)
			);
		}
		
		TriangleStrategy(Triangle t)
		{
			a = t.getA();
			b = t.getB();
			c = t.getC();
		}
		
	}
	
	class KegelsnedeStrategy extends Strategy {
		KegelsnedeStrategy(Kegelsnede2 k) {
			super();
			this.k = k;
		}

		Kegelsnede2 k;
		
		void recalc() {
			k.getCenter(ZwaartePunt.this);
		}

		boolean isDefined() {
			return ZwaartePunt.this.defined && k.isDefined();
		}
		
	}
	
	Strategy strategy;
	Destroyable depend[] = new Destroyable[1];

	public ZwaartePunt() {
	}

	
	public ZwaartePunt(Destroyable t) {
		super(Numbers.ZERO, Numbers.ZERO);
		t.visit(this);
		depend[0] = t;
		t.addObserver(this);
		strategy.recalc();
	}
	
	public boolean isDefined() {
		return strategy != null && strategy.isDefined();
	}

	public void moveTo(Numbers x, Numbers y) {
	}

	public String key() {
		return TYPE;
	}

	public void read(Codec codec) throws IOException {
		codec.read(depend);
		if(depend[0]!=null)
		{
			depend[0].addObserver(this);
			depend[0].visit(this);
			strategy.recalc();
		}
	}

	public void write(Codec codec) throws IOException {
		codec.write(depend);
	}

	public void update(Observable observable, Object arg) {
		if(arg == null)
		{
			strategy.recalc();
			notifyObservers(arg);
			return;
		}
		if(arg == DESTROY)
			destroy();
	}

	public Destroyable[] getDepend() {
		return depend;
	}

	public void visitTriangle(Triangle t) {
		strategy = new TriangleStrategy(t);
		
	}
	public void visitKegelsnede(Kegelsnede2 k) {
		strategy = new KegelsnedeStrategy(k);
	}
	public void visitLocus(Locus l) {
	}

}
