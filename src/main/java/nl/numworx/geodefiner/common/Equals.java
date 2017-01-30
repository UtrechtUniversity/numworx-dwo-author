package nl.numworx.geodefiner.common;

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
import fi.euclides.model.Segment;
import fi.euclides.model.Triangle;
import fi.euclides.model.math.Numbers;
import fi.euclides.proof.LabelTester;
import fi.euclides.model.Visitor;

class Equals extends LabelTester {

	public Equals() {
		super("\u2248");
	}

	@Override
	public Destroyable[] createDepend() {
		return new Destroyable[3];
	}

	private Numbers getScale() {
		Model model = getTracker().getModel();
		return Numbers.abs(Numbers.sub(model.getO().getX(), model.getU().getX()));
	}
	
	
	class EqualsVisitor implements Visitor {

		Numbers test = Numbers.ONE;
		Destroyable b;
		
		EqualsVisitor(Destroyable b) {
			this.b = b;
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
			test = a .equals(b) ? Numbers.ZERO : Numbers.ONE;			
		}

		@Override
		public void visitCirkel(Cirkel a) {
			test = a .equals(b) ? Numbers.ZERO : Numbers.ONE;
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
				test = Numbers.sub(label.value, lb.value);
			}
		}

		@Override
		public void visitTriangle(Triangle a) {
			test = a .equals(b) ? Numbers.ZERO : Numbers.ONE;			
		}

		@Override
		public void visitKegelsnede(Kegelsnede2 a) {
			test = a .equals(b) ? Numbers.ZERO : Numbers.ONE;			
		}

		@Override
		public void visitLocus(Locus a) {
			test = a .equals(b) ? Numbers.ZERO : Numbers.ONE;			
		}

		@Override
		public void visitBoog(Boog a) {
			test = a .equals(b) ? Numbers.ZERO : Numbers.ONE;			
		}
		
	}
	
	@Override
	protected boolean test(Label l) {
		Destroyable[] depend = l.getDepend();
		double marge = 0.001;
		if(depend.length > 2 && depend[2] instanceof Label) 
			marge = ((Label)depend[2]).value.doubleValue();
		Destroyable a = depend[0];
		Destroyable b = depend[1];
		EqualsVisitor eq = new EqualsVisitor(b);
		if(a != null) a.visit(eq);
		Numbers test = eq.test;
		setState(l, test, marge);
		return true;
	}

	@Override
	public boolean define(Label l) {
		test(l);
		Destroyable[] depend = l.getDepend();
		l.setString( s(depend[0]) + string + s(depend[1]));
		return true;
	}

}
