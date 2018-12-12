package fi.euclides.model.algo;

import java.util.Enumeration;

import fi.euclides.model.MP;
import fi.euclides.model.Pair;
import fi.euclides.model.Punt;
import fi.euclides.model.algo.PointOnAlgorithm;
import fi.euclides.model.math.Numbers;

public class PointOnLocus implements PointOnAlgorithm<MP> {

	protected PointOnLocus() {
	}
	
	public static final PointOnLocus INSTANCE = new PointOnLocus();
	private static final double TOL = 0.8;
	
	public String key() {
		return MP.PUNTOP;
	}

	public void recalc(MP mp, FreePoint punt, double x, double y) {
		double dist = Double.MAX_VALUE;
		Enumeration<Pair<Numbers, Punt>> e = mp.punten.elements();
		int min = 1;
		for(int i = 0; e.hasMoreElements() ; i++) {
			Pair<Numbers, Punt> pair = e.nextElement();
			Punt p = pair.getB();
			if(p.isDefined())
			{
				double d = Math.hypot(x-p.getXd(), y-p.getYd());
				if(d < dist)
				{
					punt.setXY(p.getX(),p.getY());
					dist = d;
					min = i;
				}
			}
		}
		if (true && dist > TOL) {
          //System.out.println("dist was = " + dist);
		  GoldenSectionSearch.Function F;		    
		  double a = mp.punten.elementAt(Math.max(0,min-1)).getA().doubleValue();
		  double b = mp.punten.elementAt(Math.min(mp.punten.size()-1, min+1)).getA().doubleValue();
		  F = (n) -> {
		    Punt pn= mp.interpolate(Numbers.createDouble(n));
		    return Math.hypot(x-pn.getXd(), y-pn.getYd());
		  };
		  double[] ab = GoldenSectionSearch.gss(F, a, b, (b-a)/16);
		  Punt pn = mp.interpolate(Numbers.createDouble((ab[0]+ab[1])/2));
		  //System.out.println("dist is = " + F.of((ab[0]+ab[1])/2));
		  punt.setXY(pn.getX(), pn.getY());
		}
	}
	public void update(MP mp, FreePoint punt) {
		recalc(mp, punt, punt.getX(), punt.getY());
	}

	public void recalc(MP mp, FreePoint punt, Numbers x, Numbers y) {
		recalc(mp, punt, x.doubleValue(), y.doubleValue());
	}

}
