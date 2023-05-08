package fi.euclides.gwt;

import fi.euclides.event.HitTester;
import fi.euclides.model.Label;
import fi.euclides.model.Triangle;
import gwt.awt.Shape;

public class HitTester2 extends HitTester {

	public HitTester2() {
	}

	@Override
	public void visitLabel(Label label) {
		Shape s = label.adapt(Shape.class);
		if (s != null && s.contains(lastx, lasty))
		{
			call(label);
			return;
		}
		super.visitLabel(label);
	}

	@Override
	public void visitTriangle(Triangle t) {
		// TODO Auto-generated method stub
		super.visitTriangle(t);
	}

	@Override
	public HitTester copy() {
		return new HitTester2();
	}

}
