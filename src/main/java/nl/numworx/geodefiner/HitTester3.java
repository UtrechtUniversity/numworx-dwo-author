package nl.numworx.geodefiner;

import java.awt.FontMetrics;

import fi.euclides.event.HitTester;
import fi.euclides.model.Punt;
import fi.euclides.swing.HitTester2;

class HitTester3 extends HitTester2 {

	HitTester3(FontMetrics fm) {
		super(fm);
	}

	@Override
	public HitTester copy() {
		return new HitTester3(fm);
	}

	@Override
	public void visitPunt(Punt p) {
		double old = marge;
		try {
			float pointSize;
			Float ps = p.adapt(Float.class);
			if(ps != null) {
				pointSize = ps.floatValue();
				marge = Math.max(marge, pointSize/2);
			} 
			super.visitPunt(p);
		} finally {
			marge = old;
		}
	}

}
