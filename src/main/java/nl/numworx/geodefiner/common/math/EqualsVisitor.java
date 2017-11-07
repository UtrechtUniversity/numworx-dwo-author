package nl.numworx.geodefiner.common.math;

import fi.euclides.event.NameMapper;
import fi.euclides.event.Tracker;
import fi.euclides.model.Boog;
import fi.euclides.model.Cirkel;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Kegelsnede2;
import fi.euclides.model.Label;
import fi.euclides.model.Lijn;
import fi.euclides.model.Locus;
import fi.euclides.model.Model;
import fi.euclides.model.Punt;
import fi.euclides.model.PuntenLijn;
import fi.euclides.model.Ray;
import fi.euclides.model.Segment;
import fi.euclides.model.Triangle;
import fi.euclides.model.Visitor;
import fi.euclides.model.math.Numbers;
import nl.numworx.geodefiner.common.Volgpunt;

public class EqualsVisitor implements Visitor {

	private static Numbers bracket(Numbers x1, Numbers y1, Lijn l) {
		Numbers x2 = l.getX1n();
		Numbers x3 = l.getX2n();
		Numbers y2 = l.getY1n();
		Numbers y3 = l.getY2n();
	
	Numbers d = 
		Numbers.add(Numbers.sub(Numbers.mul(x1, Numbers.sub(y2,y3)) ,Numbers.mul(y1,Numbers.sub(x2,x3))) , 
		Numbers.sub(Numbers.mul(x2,y3),Numbers.mul(y2,x3)));
		return Numbers.abs(d);
	}

	
	private NameMapper model;

	private Numbers getScale() {
		return Numbers.abs(Numbers.sub(model.getO().getX(), model.getU().getX()));
	}

	Numbers test = Numbers.ONE;
	Destroyable b;
	
	public void reset() {
		test = Numbers.ONE;
	}

	public Numbers test() {
		return test;
	}
	
	public EqualsVisitor(Destroyable b, Tracker t) {
		this.b = b;
		model = t.getMapper();
	}
	public EqualsVisitor(Destroyable b, NameMapper m) {
		this.b = b;
		this.model = m;
	}

	@Override
	public void visitPunt(Punt p) {
		if (b instanceof Punt) {
			Punt pb = (Punt) b;
			test = puntenTest(p, pb);
		}
	}

	private Numbers puntenTest(Punt p, Punt pb) {
		Numbers dy1 = Numbers.sub(p.getY(), pb.getY());
		Numbers dx1 = Numbers.sub(p.getX(), pb.getX());
		Numbers test = Numbers.hypot(dx1, dy1);
		test = Numbers.div(test, getScale()); // Overleg nodig?
		return test;
	}

	@Override
	public void visitLijn(Lijn a) {
		if (a instanceof Ray ^ b instanceof Ray) {
			test = Numbers.ONE;
			return;
		}
		test = a .equals(b) ? Numbers.ZERO : Numbers.ONE;
		if (test == Numbers.ONE && b instanceof Lijn && !(b instanceof Segment))
		{
			Lijn lb = (Lijn) b;
			test = Numbers.add( bracket(a.getX1n(),a.getY1n(), lb), 
							    bracket(a.getX2n(),a.getY2n(), lb)
					);
		}
	}

	@Override
	public void visitCirkel(Cirkel a) {
		test = a .same(b) ? Numbers.ZERO : Numbers.ONE;
		if (test == Numbers.ONE && b instanceof Cirkel) {
			Cirkel cb = (Cirkel) b;
			test = puntenTest(cb.getCenter(), a.getCenter());
			Numbers rtest = Numbers.abs(Numbers.sub(a.getR2n(), cb.getR2n()));
			rtest = Numbers.div(rtest, getScale());
			test = Numbers.add(test, rtest);
		}
	}

	@Override
	public void visitSegment(Segment a) {
		test = a .equals(b) ? Numbers.ZERO : Numbers.ONE;
		if ( test == Numbers.ONE && b instanceof Segment) {
			Punt a1 = a.getP1(); Punt a2 = a.getP2();
			Punt b1 = ((PuntenLijn) b).getP1();
			Punt b2 = ((PuntenLijn) b).getP2();
			Numbers t1 = Numbers.add(puntenTest(a1,b1),puntenTest(a2,b2));
			Numbers t2 = Numbers.add(puntenTest(a1,b2),puntenTest(a2,b1));
			// minimum nemen
			int s = Numbers.signum(Numbers.sub(t1, t2));
			if(s < 0) test = t1; else  test = t2;
		}
	}

	@Override
	public void visitLabel(Label label) {
		if(b instanceof Label) {
			Label lb = (Label)b;
// text("A",a) equals text("b",b) if "a" == "b" and a near b
			if(lb.same(label))
			{
				Punt pa = label.getP();
				Punt pb = lb.getP();
				if(pa instanceof Volgpunt)
					pa = ((Volgpunt) pa).getP();
				if(pb instanceof Volgpunt)
					pb = ((Volgpunt) pb).getP();
				test = puntenTest(pa,pb);	
			}
			else
				test = Numbers.abs(Numbers.sub(label.value, lb.value));
		}
	}

	@Override
	public void visitTriangle(Triangle a) {
		test = a .same(b) ? Numbers.ZERO : Numbers.ONE;			
	}

	@Override
	public void visitKegelsnede(Kegelsnede2 a) {
		test = a .equals(b) ? Numbers.ZERO : Numbers.ONE;			
	}

	@Override
	public void visitLocus(Locus a) {
		test = a .same(b) ? Numbers.ZERO : Numbers.ONE;			
	}

	@Override
	public void visitBoog(Boog a) {
		test = a .equals(b) ? Numbers.ZERO : Numbers.ONE;			
	}
	
}