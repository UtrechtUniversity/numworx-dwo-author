package nl.numworx.geodefiner.common;

import fi.euclides.model.PuntenLijn;
import fi.euclides.model.Segment;
import fi.euclides.util.Adapter;

public class ShortSegment extends Segment {
	double dx, dy;
	PuntenLijn org;
	Tips tip;
	@Override
	public double getX1() {
		double x1 = org.getX1();
		if (tip != Tips.ATEND) x1 += dx;
		return x1;
	}
	@Override
	public double getX2() {
		double x2 = org.getX2();
		if (tip != Tips.ATSTART) x2 -= dx;
		return x2;
	}
	@Override
	public double getY1() {
		double y1 = org.getY1();
		if (tip != Tips.ATEND) y1 += dy;
		return y1;
	}
	@Override
	public double getY2() {
		double y2 = org.getY2();
		if (tip != Tips.ATSTART) y2 -= dy;
		return y2;
	}
	@Override
	public <T> T adapt(Class<T> clz) {
		return org.adapt(clz);
	}
	
	public Adapter getAdapter() {
		return org.getAdapter();
	}
	
	public ShortSegment(PuntenLijn s, double dx, double dy, Tips tip) {
		this.org = s;
		this.dx = dx;
		this.dy = dy;
		this.tip = tip;
	}
	
	@Override
	public boolean isDefined() {
		return true;
	}
}