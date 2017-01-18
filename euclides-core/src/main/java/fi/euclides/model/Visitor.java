package fi.euclides.model;

import fi.euclides.model.Kegelsnede2;
import fi.euclides.model.Locus;
import fi.euclides.model.Triangle;

public interface Visitor {
	void visitPunt(Punt p);
	void visitLijn(Lijn l);
	void visitCirkel(Cirkel c);
	void visitSegment(Segment s);
	void visitLabel(Label label);
	void visitTriangle(Triangle t);
	void visitKegelsnede(Kegelsnede2 k);
	void visitLocus(Locus l);
	void visitBoog(Boog b);
}
