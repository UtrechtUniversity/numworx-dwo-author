package nl.numworx.fsm.shared;

import fi.euclides.event.HitTester;
import fi.euclides.model.Punt;
import fi.euclides.model.Segment;

public class Hits extends HitTester {
	
	final static double ORG = 5;
	final static double margePunt = 75/2;
	final static double margeSegment = 10;

	@Override
	public void visitPunt(Punt p) {
	 try {
		marge = margePunt;
		super.visitPunt(p);
	 } finally {
		 marge = ORG; 
	 }
	}

	@Override
	public void visitSegment(Segment s) {
		try { marge = margeSegment;
			super.visitSegment(s);
		} finally {
			marge = ORG;
		}
	}

}
