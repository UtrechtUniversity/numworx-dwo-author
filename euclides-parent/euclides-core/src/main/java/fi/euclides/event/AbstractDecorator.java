package fi.euclides.event;

import fi.euclides.model.Boog;
import fi.euclides.model.Cirkel;
import fi.euclides.model.Kegelsnede2;
import fi.euclides.model.Label;
import fi.euclides.model.Lijn;
import fi.euclides.model.Locus;
import fi.euclides.model.Punt;
import fi.euclides.model.Segment;
import fi.euclides.model.Triangle;
import fi.euclides.model.Visitor;

public class AbstractDecorator implements Visitor {

	public static final AbstractDecorator NULL = new AbstractDecorator();
	
	protected AbstractDecorator() { 
		
	}
	
	@Override
	public void visitPunt(Punt p) {
	}

	@Override
	public void visitLijn(Lijn l) {
	}

	@Override
	public void visitCirkel(Cirkel c) {
	}

	@Override
	public void visitSegment(Segment s) {
	}

	@Override
	public void visitLabel(Label label) {
	}

	@Override
	public void visitTriangle(Triangle t) {
	}

	@Override
	public void visitKegelsnede(Kegelsnede2 k) {
	}

	@Override
	public void visitLocus(Locus l) {
	}

	@Override
	public void visitBoog(Boog b) {
	}


}
