package fi.euclides.model.algo;

import java.util.Enumeration;

import fi.euclides.model.Lijn;
import fi.euclides.model.MP;
import fi.euclides.model.Pair;
import fi.euclides.model.Punt;
import fi.euclides.model.PuntOp;
import fi.euclides.model.algo.PointOnAlgorithm;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.JMath;

public class PointOnLocus implements PointOnAlgorithm<MP> {

	protected PointOnLocus() {
	}
	
	public static final PointOnLocus INSTANCE = new PointOnLocus();
	
	public String key() {
		return MP.PUNTOP;
	}

	public void recalc(MP mp, FreePoint punt, double x, double y) {
		double dist = Double.MAX_VALUE;
		Enumeration<?> e = mp.punten.elements();
		for(int i = 0; e.hasMoreElements() ; i++) {
			Pair pair = (Pair) e.nextElement();
			Punt p = (Punt) pair.getB();
			if(p.isDefined())
			{
				double d = JMath.hypot(x-p.getXd(), y-p.getYd());
				if(d < dist)
				{
					punt.setXY(p.getX(),p.getY());
					dist = d;
					pair.getA();
				}
			}
		}
	}
	public void update(MP mp, FreePoint punt) {
		recalc(mp, punt, punt.getX(), punt.getY());
	}

	public void recalc(MP mp, FreePoint punt, Numbers x, Numbers y) {
		recalc(mp, punt, x.doubleValue(), y.doubleValue());
	}

}
