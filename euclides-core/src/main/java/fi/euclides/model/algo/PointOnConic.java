package fi.euclides.model.algo;

import fi.euclides.model.Kegelsnede2.DegeneratedStrategy;
import fi.euclides.model.Kegelsnede2.HyperboolStrategy;
import fi.euclides.model.MP;
import fi.euclides.model.PuntOp;
import fi.euclides.model.algo.PointOnAlgorithm;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.JMath;

public class PointOnConic extends PointOnLocus implements PointOnAlgorithm<MP> {

	PointOnConic() {
	}
	public static final PointOnConic INSTANCE = new PointOnConic();

	public void recalc(MP ks, FreePoint punt, double x, double y) {
		if(ks.strategy instanceof DegeneratedStrategy)
		{
			DegeneratedStrategy ds = (DegeneratedStrategy)ks.strategy;
// Zie punt op lijn: 
			double inp; // (dx*(x-x1)+dy*(y-y1))/(dx*dx+dy*dy);
			inp = (ds.bx*(x-ds.ax)+ds.by*(y-ds.ay))/(ds.bx*ds.bx + ds.by*ds.by);
			double x1 = ds.ax + inp * ds.bx;
			double y1 = ds.ay + inp * ds.by;

			inp = (ds.dx*(x-ds.cx)+ds.dy*(y-ds.cy))/(ds.dx*ds.dx + ds.dy*ds.dy);
			double x2 = ds.cx + inp * ds.dx;
			double y2 = ds.cy + inp * ds.dy;
			double alfa = JMath.hypot(x-x1, y-y1);
			double beta = JMath.hypot(x-x2, y-y2);
			if(alfa < beta)	// kan overwippen!
			{
				punt.setXY(Numbers.createDouble(x1), Numbers.createDouble(y1));
			} else {
				punt.setXY(Numbers.createDouble(x2), Numbers.createDouble(y2));
			}
			
			return;
		}
		if(ks.strategy instanceof HyperboolStrategy)
		{
			HyperboolStrategy hs = (HyperboolStrategy)ks.strategy;
			x = x - hs.tx;
			y = y - hs.ty;
			x = x - hs.rx*y;
			y = y - hs.ry*x;
			double a2 = hs.a*2;
			double b2 = hs.b*2;
			boolean b = y*x>=0;
			if(b)x=-x;
			if(hs.swap)
			{
				double t = a2*b2 / ( a2*x - b2*y);
				x = b2 * ( t + 1/t)/2;
				y = a2 * ( t - 1/t)/2;

			} else {
				double t = a2*b2 / ( a2*y - b2*x); // is instabiel als t>0
				x = a2 * ( t - 1/t)/2;
				y = b2 * ( t + 1/t)/2;
			}
			if(b)x=-x;
			punt.setXY(Numbers.createDouble(x+y*hs.rx+hs.tx), Numbers.createDouble(y+x*hs.ry+hs.ty));
			return;
		}
		super.recalc(ks, punt, x, y);
	}

}
