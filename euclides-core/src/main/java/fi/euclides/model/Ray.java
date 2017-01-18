package fi.euclides.model;

import fi.euclides.model.algo.PointOnAlgorithm;
import fi.euclides.model.algo.PointOnLine;
import fi.euclides.model.algo.PointOnRay;
import fi.euclides.util.Observable;

public class Ray extends PuntenLijn {

	
	public static final String TYPE = "r";
	public Ray() {
	}

	public Ray(Punt p1, Punt p2) {
		super();
		setP1(p1);
		setP2(p2);
	}

	public String key() {
		return TYPE;
	}

	public Observable newInstance() {
		return new Ray();
	}

	public Destroyable trail() {
		return new Ray(new VrijPunt(getX1(), getY1()), new VrijPunt(getX2(), getY2()));
	}
	
	public PointOnAlgorithm<Lijn> getAlgo() {
		return PointOnRay.INSTANCE;
	}

	
//	public void recalc(PuntOp punt, double x, double y) {
//		double dx = this.getDX();
//		double dy = this.getDY();
//		double x1 = this.getX1();
//		double y1 = this.getY1();
//		double inp = (dx*(x-x1)+dy*(y-y1))/(dx*dx+dy*dy);
//// TODO 1 endpoint
//		double xx = x1 + inp*dx;
//			if(dx<0)
//			{
//				if(xx > this.getX1())
//				{
//					punt.setXY(this.getX1n(), this.getY1n());
//					return;
//				} else
//				if(xx < this.getX2())
//				{
//					//punt.setXY(this.getX2n(), this.getY2n());
//					//return;	
//				}
//			} else if(dx>0)
//			{
//				if(xx < this.getX1())
//				{
//					punt.setXY(this.getX1n(), this.getY1n());
//					return;
//				} else
//				if(xx > this.getX2())
//				{
//					//punt.setXY(this.getX2n(), this.getY2n());
//					//return;	
//				}
//			} else if(dy>0)
//			{
//				xx = y1+inp*dy;
//				if(xx < this.getY1())
//				{
//					punt.setXY(this.getX1n(), this.getY1n());
//					return;
//				} else
//				if(xx > this.getY2())
//				{
//					//punt.setXY(this.getX2n(), this.getY2n());
//					//return;
//				}
//			} else if(dy < 0) 
//			{
//				xx = y1+inp*dy;
//				if(xx > this.getY1())
//				{
//					punt.setXY(this.getX1n(), this.getY1n());
//					return;
//				} else
//				if(xx < this.getY2())
//				{
//					//punt.setXY(this.getX2n(), this.getY2n());
//					//return;
//				}
//				
//			}
//		online(punt, x1, y1, dx, dy, inp);
//	}

	/* (non-Javadoc)
	 * @see fi.euclides.model.Lijn#contains(fi.euclides.model.Punt)
	 */
	public boolean contains(Punt p) {
		double x = p.getXd();
		double y = p.getYd();
		double x1 = getX1();
		double x2 = getX2();
		double y1 = getY1();
		double y2 = getY2();
		boolean result;
		if(x1<x2)
			result = x1<=x;
		else 
			result = x<=x1;
		if(y1<y2)
			result &= y1<=y;
		else
			result &= y<=y1;
		
		return result;
	}



}
