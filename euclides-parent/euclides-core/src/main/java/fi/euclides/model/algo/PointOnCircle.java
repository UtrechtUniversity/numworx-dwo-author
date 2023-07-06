package fi.euclides.model.algo;

import fi.euclides.model.Cirkel;
import fi.euclides.model.math.Numbers;

public class PointOnCircle implements PointOnAlgorithm<Cirkel> {

	PointOnCircle() {
	}
	
	public static final PointOnCircle INSTANCE = new PointOnCircle();
	
	public String key() {
		return Cirkel.PUNTOP;
	}

	public void recalc(Cirkel cirkel, FreePoint punt, double dx, double dy) {
		Numbers x = Numbers.createRational(Math.round(dx), 1);
		Numbers y = Numbers.createRational(Math.round(dy), 1);
		Numbers cx= cirkel.getCenter().getX();
		Numbers cy= cirkel.getCenter().getY();
		x = Numbers.sub(x, cx);
		y = Numbers.sub(y, cy);
		Numbers ln = Numbers.sqrt(Numbers.div(Numbers.add(Numbers.sqr(x), Numbers.sqr(y)),cirkel.getR2n()));
		punt.setXY(Numbers.add(cx, Numbers.div(x, ln)), Numbers.add(cy,Numbers.div(y, ln)));
	}
	
	public void update(Cirkel cirkel, FreePoint punt) {
		recalc(cirkel, punt, punt.getX(), punt.getY());
	}

	public void recalc(Cirkel cirkel, FreePoint punt, Numbers x, Numbers y) {
		recalc(cirkel, punt, x.doubleValue(), y.doubleValue());
	}
}
