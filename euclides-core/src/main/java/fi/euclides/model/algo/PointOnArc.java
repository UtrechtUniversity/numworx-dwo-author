package fi.euclides.model.algo;

import fi.euclides.model.Boog;
import fi.euclides.model.Punt;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.JMath;

public class PointOnArc implements PointOnAlgorithm<Boog> {

	private PointOnArc() {
	}

	public static final PointOnArc INSTANCE = new PointOnArc();
	
	public String key() {
		return "Pb";
	}

	public void recalc(Boog on, FreePoint punt, double dx, double dy) {
		Numbers x = Numbers.createRational(JMath.round(dx), 1);
		Numbers y = Numbers.createRational(JMath.round(dy), 1);
		Numbers cx= on.getCenter().getX();
		Numbers cy= on.getCenter().getY();
		x = Numbers.sub(x, cx);
		y = Numbers.sub(y, cy);
		
		Numbers r2n = Numbers.createDouble(on.getR() * on.getR());
		Numbers ln = Numbers.sqrt(Numbers.div(Numbers.add(Numbers.sqr(x), Numbers.sqr(y)),r2n));
		punt.setXY(Numbers.add(cx, Numbers.div(x, ln)), Numbers.add(cy,Numbers.div(y, ln)));
		
		double rx = punt.getX().doubleValue() - cx.doubleValue();
		double ry = punt.getY().doubleValue() - cy.doubleValue();
		double hoek = JMath.atan2(-ry, rx);
		double l = on.length();
		double s = on.getStart();
		if( l >= 0) {
			if(hoek < s) hoek += Math.PI*2.0;
			if(hoek > s + l) {
				// outside
				if(hoek < s + l + (Math.PI*2-l)/2) {
					// at end
					Punt start = Boog.endOf(on);
					punt.setXY(start.getX(), start.getY());
				} else {
					Punt start = Boog.startOf(on);
					punt.setXY(start.getX(), start.getY());
				}
			}
		} else {
			if (hoek > s) hoek -= Math.PI*2.0;
			if (hoek < s+l) {
				if(hoek > s + l - (Math.PI*2+l)/2) {
					// at end
					Punt start = Boog.endOf(on);
					punt.setXY(start.getX(), start.getY());
				} else {
					Punt start = Boog.startOf(on);
					punt.setXY(start.getX(), start.getY());
				}
				
			}
		}
	}

	@Override
	public void update(Boog on, FreePoint punt) {
		recalc(on, punt, punt.getX(), punt.getY());
	}

	public void recalc(Boog on, FreePoint punt, Numbers x, Numbers y) {
		recalc(on,punt, x.doubleValue(), y.doubleValue());
	}

}
