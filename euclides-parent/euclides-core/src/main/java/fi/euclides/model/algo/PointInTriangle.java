package fi.euclides.model.algo;

import fi.euclides.model.MP;
import fi.euclides.model.Punt;
import fi.euclides.model.PuntOp;
import fi.euclides.model.Triangle;
import fi.euclides.model.algo.PointOnAlgorithm;
import fi.euclides.model.math.Numbers;

public class PointInTriangle implements PointOnAlgorithm<MP> {

	private PointInTriangle() {
	}
	
	public static final PointInTriangle INSTANCE = new PointInTriangle();
	
	public String key() {
		return MP.PUNTOP;
	}
	private double out(double x1, double y1, double x2, double y2) {
		return x1*y2 - x2 * y1;
	}
	private double out(double x1, double y1, double x2, double y2, double x3, double y3 )
	{
		return out(x1-x3, y1-y3, x2-x3, y2-y3);
	}
	
	
	public void recalc(MP mp, FreePoint punt, double x, double y) {
		Triangle t = (Triangle) mp;
		double ax = t.getA().getXd();
		double ay = t.getA().getYd();
		double bx = t.getB().getXd();
		double by = t.getB().getYd();
		double cx = t.getC().getXd();
		double cy = t.getC().getYd();
		double minx = Math.min(ax, Math.min(bx, cx));
		x = Math.max(x, minx);
		
		punt.setXY(Numbers.createDouble(x), Numbers.createDouble(y));
	}
	@Override
	public void update(MP on, FreePoint punt) {
		recalc(on, punt, punt.getX(), punt.getY());
	}
	public void recalc(MP on, FreePoint punt, Numbers x, Numbers y) {
		recalc(on, punt, x.doubleValue(), y.doubleValue());
	}

}
