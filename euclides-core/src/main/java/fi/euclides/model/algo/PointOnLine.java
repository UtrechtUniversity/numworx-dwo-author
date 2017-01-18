package fi.euclides.model.algo;

import fi.euclides.model.Lijn;
import fi.euclides.model.Punt;
import fi.euclides.model.PuntOp;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.DefaultAdapter;
import fi.euclides.util.JMath;

public class PointOnLine implements PointOnAlgorithm<Lijn> {

	public static final PointOnAlgorithm<Lijn> INSTANCE = new PointOnLine();
	public static boolean polPolicy = true; // euclides/geodefiner
	
	PointOnLine() {
	}

	public String key() {
		return Lijn.PUNTOP;
	}

	public void recalc(Lijn lijn, FreePoint punt, Numbers x, Numbers y)
	{
		recalc(lijn, punt, x.doubleValue(), y.doubleValue());
	}
	
	public void recalc(Lijn lijn, FreePoint punt, double x, double y) {
		double dx = lijn.getDX();
		double dy = lijn.getDY();
		double x1 = lijn.getX1();
		double y1 = lijn.getY1();
		double inp = (dx*(x-x1)+dy*(y-y1))/(dx*dx+dy*dy);
		online(lijn, punt, x1, y1, dx, dy, inp);
	}
	
	protected void online(Lijn lijn, FreePoint punt, double x1, double y1, double dx,
			double dy, double inp) {
		if(Math.abs(dx)>Math.abs(dy))
		{
			// horizontal line, round to x pixels
			long lx = JMath.round(x1 + inp*dx);
			Numbers lxn = Numbers.createRational(lx, 1);
			Numbers rat = Numbers.sub(lxn, lijn.getX1n());
			rat = Numbers.div(rat,  lijn.getDXn());
			Numbers lyn = Numbers.add(lijn.getY1n(), Numbers.mul(rat, lijn.getDYn()));
			DefaultAdapter.getDefault(punt).put(Numbers.class, rat);
			punt.setXY(lxn, lyn);
			return;
		}
		// vertical line, round to y pixels
		long ly = JMath.round(y1 + inp*dy);
		Numbers lyn = Numbers.createRational(ly, 1);
		Numbers rat = Numbers.sub(lyn,lijn.getY1n());
		rat = Numbers.div(rat, lijn.getDYn());
		Numbers lxn = Numbers.add(lijn.getX1n(), Numbers.mul(rat, lijn.getDXn()));
		DefaultAdapter.getDefault(punt).put(Numbers.class, rat);
		punt.setXY(lxn, lyn);
	}

	public void update(Lijn lijn, FreePoint punt) {
		Numbers rat = punt.getAdapter().adapt(Numbers.class);
		Numbers x = punt.getX();
		Numbers y = punt.getY();
		if(polPolicy && rat != null) {
			x = Numbers.add(lijn.getX1n(), Numbers.mul(lijn.getDXn(), rat));
			y = Numbers.add(lijn.getY1n(), Numbers.mul(lijn.getDYn(), rat));
		}
		recalc(lijn, punt, x, y);
	}
}
