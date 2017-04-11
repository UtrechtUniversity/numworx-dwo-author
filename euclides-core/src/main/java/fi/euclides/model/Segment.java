package fi.euclides.model;

import fi.euclides.model.algo.PointOnAlgorithm;
import fi.euclides.model.algo.PointOnLine;
import fi.euclides.model.algo.PointOnSegment;
import fi.euclides.util.Observable;

public class Segment extends PuntenLijn {

	public static final String TYPE = "s";

	/* (non-Javadoc)
	 * @see euclides.PuntenLijn#key()
	 */
	public String key() {
		return TYPE;
	}

	/* (non-Javadoc)
	 * @see euclides.Lijn#visit(euclides.Visitor)
	 */
	public void visit(Visitor v) {
		v.visitSegment(this);
	}

	/**
	 * 
	 */
	public Segment() {
	}

	/**
	 * @param p1
	 * @param p2
	 */
	public Segment(Punt p1, Punt p2) {
		setP1(p1); // NO SWAP, Voor de pijlpunten!
		setP2(p2);
	}

	public Observable newInstance() {
		return new Segment();
	}

	/* (non-Javadoc)
	 * @see fi.euclides.model.Destroyable#trail()
	 */
	public Destroyable trail() {
		return new Segment((Punt) getP1().trail(), (Punt) getP2().trail());
	}

	public PointOnAlgorithm<Lijn> getAlgo() {
		return PointOnSegment.INSTANCE;
	}

	
//	public void recalc(PuntOp punt, double x, double y) {
//		double dx = this.getDX();
//		double dy = this.getDY();
//		double x1 = this.getX1();
//		double y1 = this.getY1();
//		double inp = (dx*(x-x1)+dy*(y-y1))/(dx*dx+dy*dy);
//// endpoints
//		double xx = x1 + inp*dx;
//			if(dx<0)
//			{
//				if(xx > this.getX1())
//				{
//					punt.setXY(this.getX1n(), this.getY1n());
//					return;
//				} 
//				if(xx < this.getX2())
//				{
//					punt.setXY(this.getX2n(), this.getY2n());
//					return;	
//				}
//			} else if(dx>0)
//			{
//				if(xx < this.getX1())
//				{
//					punt.setXY(this.getX1n(), this.getY1n());
//					return;
//				} 
//				if(xx > this.getX2())
//				{
//					punt.setXY(this.getX2n(), this.getY2n());
//					return;	
//				}
//			} else if(dy>0)
//			{
//				xx = y1+inp*dy;
//				if(xx < this.getY1())
//				{
//					punt.setXY(this.getX1n(), this.getY1n());
//					return;
//				}
//				if(xx > this.getY2())
//				{
//					punt.setXY(this.getX2n(), this.getY2n());
//					return;
//				}
//			} else if(dy < 0) 
//			{
//				xx = y1+inp*dy;
//				if(xx > this.getY1())
//				{
//					punt.setXY(this.getX1n(), this.getY1n());
//					return;
//				}
//				if(xx < this.getY2())
//				{
//					punt.setXY(this.getX2n(), this.getY2n());
//					return;
//				}
//				
//			}
//		online(punt, x1, y1, dx, dy, inp);
//	}

	/* (non-Javadoc)
	 * @see fi.euclides.model.Lijn#contains(fi.euclides.model.Punt)
	 */
	final static private double EPS = 0.00001; // rekenfouten
	public boolean contains(Punt p) {
		double x = p.getXd();
		double y = p.getYd();
		double x1 = getX1();
		double x2 = getX2();
		double y1 = getY1();
		double y2 = getY2();
		boolean result;
		if(x1<x2)
			result = x1-EPS<=x && x<=x2+EPS;
		else 
			result = x2-EPS<=x && x<=x1+EPS;
		if(y1<y2)
			result &= y1-EPS<=y && y<=y2+EPS;
		else
			result &= y2-EPS<=y && y<=y1+EPS;
		
		return result;
	}

	
}
