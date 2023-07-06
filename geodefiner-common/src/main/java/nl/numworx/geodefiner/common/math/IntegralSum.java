package nl.numworx.geodefiner.common.math;

import fi.euclides.event.Tracker;
import fi.euclides.model.HorizontalPunt;
import fi.euclides.model.Model;
import fi.euclides.model.Segment;
import fi.euclides.model.SegmentVisitor;
import fi.euclides.model.math.Numbers;

public class IntegralSum implements SegmentVisitor {

	private static final Numbers POSITIVE_INFINITY = Numbers.createDouble(Double.POSITIVE_INFINITY);
	private static final Numbers NEGATIVE_INFINITY = Numbers.createDouble(Double.NEGATIVE_INFINITY);
	private double y0, dx, dx2;
	private double sum;

	public IntegralSum(Model model) {
		this.y0 = model.getO().getYd();
		this.dx = ((HorizontalPunt)model.getU()).getDistance().doubleValue();
		this.dx2 = dx * dx;
	}

	@Override
	public void visitSegment(Segment s) {
		double x1 = s.getX1();
		double x2 = s.getX2();
		double y1 = s.getY1();
		double y2 = s.getY2();
		double h = y0-(y1+y2)/2;
		double dx = Math.abs(x1-x2);
		sum += dx * h;
	}
	
	public Numbers getSum() {
		return Numbers.createDouble(sum / dx2);
	}
	

	@Override
	public Numbers clipTop() {
		return NEGATIVE_INFINITY;
	}

	@Override
	public Numbers clipBottom() {
		return POSITIVE_INFINITY;
	}

	@Override
	public Numbers clipLeft() {
		return NEGATIVE_INFINITY;
	}

	@Override
	public Numbers clipRight() {
		return POSITIVE_INFINITY;
	}

}
