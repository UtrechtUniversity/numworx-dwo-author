package nl.numworx.geodefiner.common.index;

import fi.euclides.model.Boog;
import fi.euclides.model.Cirkel;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Groep;
import fi.euclides.model.Kegelsnede2;
import fi.euclides.model.Label;
import fi.euclides.model.Lijn;
import fi.euclides.model.Locus;
import fi.euclides.model.Punt;
import fi.euclides.model.Segment;
import fi.euclides.model.Triangle;
import fi.euclides.model.Visitor;

public class ListSelector implements Visitor {
	
	private Indexed indexed;
	
	public ListSelector(Groep grp, Label index) {
		grp.elementAt(0).visit(this);
		indexed.setGrp(grp);
		indexed.setIdx(index);
		indexed.recalc();
	}
	
	public Destroyable get() {
		return indexed.asDestroyable();
	}

	@Override
	public void visitPunt(Punt p) {
		indexed =  new PuntIndex();
	}

	@Override
	public void visitLijn(Lijn l) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visitCirkel(Cirkel c) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visitSegment(Segment s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visitLabel(Label label) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visitTriangle(Triangle t) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visitKegelsnede(Kegelsnede2 k) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visitLocus(Locus l) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visitBoog(Boog b) {
		// TODO Auto-generated method stub

	}

}
