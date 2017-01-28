package nl.numworx.geodefiner.common;

import fi.euclides.model.Boog;
import fi.euclides.model.Cirkel;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Kegelsnede2;
import fi.euclides.model.Label;
import fi.euclides.model.Lijn;
import fi.euclides.model.Locus;
import fi.euclides.model.Punt;
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
				Numbers dy1 = Numbers.sub(p.getY(), pb.getY());
				Numbers dx1 = Numbers.sub(p.getX(), pb.getX());
				test = Numbers.hypot(dx1, dy1);
			}
			
		}

		@Override
		public void visitLijn(Lijn a) {
			test = a .equals(b) ? Numbers.ZERO : Numbers.ONE;			
		}

		@Override
		public void visitCirkel(Cirkel a) {
			test = a .equals(b) ? Numbers.ZERO : Numbers.ONE;
		}

		@Override
		public void visitSegment(Segment a) {
			test = a .equals(b) ? Numbers.ZERO : Numbers.ONE;		}

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
