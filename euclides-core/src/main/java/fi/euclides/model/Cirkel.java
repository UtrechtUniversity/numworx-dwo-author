package fi.euclides.model;

import java.io.IOException;

import fi.euclides.model.algo.PointOnAlgorithm;
import fi.euclides.model.algo.PointOnCircle;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.JMath;
import fi.euclides.util.Observable;
import fi.euclides.util.Observer;

public class Cirkel extends Rondje implements Observer, OpObject<Cirkel> {

	public static final String TYPE = "c";
	private double x, y, d;
	
	/* (non-Javadoc)
	 * @see fi.euclides.util.Observable#setChanged()
	 */
	public void setChanged() {
		super.setChanged();
		r2 = null;
	}

	/**
	 * 
	 */
	public Cirkel() {
	}

	private Punt center, radius, radius2;
	
	/**
	 * @param center
	 * @param radius
	 */
	public Cirkel(Punt center, Punt radius) {
		this(center, radius, center);
	}

	public Cirkel(Punt center, Punt radius, Punt radius2)
	{
		setCenter(center);
		// We willen center == radius2
		if(center==radius)
		{
			setRadius(radius2);
			setRadius2(radius);
		} else {
			if(center == radius2 || radius.getIndex()< radius2.getIndex())
			{ 	setRadius(radius);
				setRadius2(radius2);
			} else {
				setRadius(radius2);
				setRadius2(radius);
			}
		}
		recalc();
	}
	
	public Cirkel(Punt center, Segment s) {
		this(center, s.getP1(), s.getP2());
	}
	

	/* (non-Javadoc)
	 * @see fi.euclides.model.Destroyable#isDefined()
	 */
	public boolean isDefined() {
		return center.isDefined() && radius.isDefined() && radius2.isDefined();
	}

	public void update(Observable observable, Object arg) {
		if(arg == DESTROY)
			destroy();
		else
		{	recalc();
			notifyObservers();
		}
	}

	public boolean isr2c()
	{
		return center == radius2;
	}
	
	void recalc() {
		double rx = radius2.getXd()-getRadius().getXd();
		double ry = radius2.getYd()-getRadius().getYd();
		setD( 2 * JMath.hypot(rx,ry));
		setX(getCenter().getXd()-getR());
		setY(getCenter().getYd()-getR());
		setChanged();
	}

	/**
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	private void setX(double x) {
		if(this.x!= x)
			setChanged();
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	private void setY(double y) {
		if(this.y != y)
			setChanged();
		this.y = y;
	}

	/**
	 * @return the d
	 */
	public double getD() {
		return d;
	}
	public double getR() {
		return d/2.0;
	}

	private Numbers r2;
	public static final String PUNTOP = "Pc";
	public Numbers getR2n() {
		if(r2 != null)
			return r2;
		return r2 = getR2n_nocache();
	}
	
	public Numbers getR2n_nocache() {
		Numbers x = Numbers.sub(getRadius().getX(), getRadius2().getX());
		Numbers y = Numbers.sub(getRadius().getY(), getRadius2().getY());
		x = Numbers.sqr(x);
		y = Numbers.sqr(y);
		return Numbers.add(x,y);
	}
	
	/**
	 * @param d the d to set
	 */
	private void setD(double d) {
		if(d!=this.d)
		{
			setChanged();
		}
		this.d = d;
	}

	
	/* (non-Javadoc)
	 * @see fi.euclides.model.Destroyable#getDepend()
	 */
	public Destroyable[] getDepend() {
		return new Destroyable[] { getCenter(), getRadius(), getRadius2() };
	}

	/* (non-Javadoc)
	 * @see euclides.Destroyable#visit(euclides.Visitor)
	 */
	public void visit(Visitor v) {
		v.visitCirkel(this);
	}

	public String key() {
		return TYPE;
	}

	/**
	 * @return the center
	 */
	public Punt getCenter() {
		return center;
	}

	/**
	 * @param center the center to set
	 */
	public void setCenter(Punt center) {
		this.center = center;
		center.addObserver(this);
	}

	/**
	 * @return the radius
	 */
	public Punt getRadius() {
		return radius;
	}

	/**
	 * @param radius the radius to set
	 */
	public void setRadius(Punt radius) {
		this.radius = radius;
		radius.addObserver(this);
	}

	public void read(Codec codec) throws IOException {
		Punt p = codec.readPunt();
		if(p!=null) setCenter(p);
		p = codec.readPunt();
		if(p!=null) setRadius(p);
		p = codec.readPunt();
		if(p!=null) setRadius2(p);
		if(center != null && radius != null && radius2 != null) 
			recalc();
	}

	public void write(Codec codec) throws IOException {
		codec.writePunt(getCenter());
		codec.writePunt(getRadius());
		codec.writePunt(getRadius2());
	}

	/**
	 * @return the radius2
	 */
	public Punt getRadius2() {
		return radius2;
	}

	/**
	 * @param radius2 the radius2 to set
	 */
	public void setRadius2(Punt radius2) {
		this.radius2 = radius2;
		radius2.addObserver(this);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((center == null) ? 0 : center.hashCode());
		result = prime * result + ((radius == null) ? 0 : radius.hashCode());
		result = prime * result + ((radius2 == null) ? 0 : radius2.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean same(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Cirkel other = (Cirkel) obj;
		if (center == null) {
			if (other.center != null)
				return false;
		} else if (!center.equals(other.center))
			return false;
		if (radius == null) {
			if (other.radius != null)
				return false;
		} else if (!radius.equals(other.radius))
			return false;
		if (radius2 == null) {
			if (other.radius2 != null)
				return false;
		} else if (!radius2.equals(other.radius2))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see fi.euclides.model.Destroyable#trail()
	 */
	public Destroyable trail() {
		Punt c = (Punt)getCenter().trail();
		Punt r = (Punt)getRadius().trail();
		if(isr2c())
			return new Cirkel(c,r);
		else
			return new Cirkel(c, r, (Punt)getRadius2().trail());
	}

	public PointOnAlgorithm<Cirkel> getAlgo() {
		return PointOnCircle.INSTANCE;
	}
	
	public PuntOp<Cirkel> pointOn(Numbers x, Numbers y) {
		return new PuntOp<Cirkel>(x, y, this, getAlgo());
	}
	
	/* (non-Javadoc)
	 * @see fi.euclides.model.Destroyable#incident(fi.euclides.model.Destroyable)
	 */
	public boolean incident(Destroyable other) {
		return super.incident(other) || ( radius==other && isr2c() );
	}
	
	public Destroyable[] getImage(Destroyable mirror) {
		if(mirror instanceof Cirkel)
		{
			// FIXME, kan beter!
			return super.getImage(mirror);
		}
		Punt p1 = getCenter().getImage(mirror, this); p1.setVisible(false);
		return new Destroyable[] { p1, new Cirkel(p1, getRadius(), getRadius2())};
	}

	@Override
	boolean contains(Punt p) {
		return true;
	}
	

}
