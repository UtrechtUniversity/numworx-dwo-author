package fi.euclides.model;

import fi.euclides.model.algo.PointOnAlgorithm;
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
