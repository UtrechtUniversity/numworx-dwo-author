package fi.euclides.model;

import java.io.IOException;

import fi.euclides.model.math.Numbers;
import fi.euclides.util.Observable;

public class Bissectrice extends Lijn {

	public static final String TYPE = "lb";
	/* (non-Javadoc)
	 * @see euclides.Lijn#update(euclides.util.Observable, java.lang.Object)
	 */
	public void update(Observable o, Object arg) {
		if(arg!=DESTROY)
			recalc();
		super.update(o, arg);
	}

	private Punt p1, p2, p3;
	private Numbers x2;
	private Numbers y2;
	/**
	 * @return the p1
	 */
	public Punt getP1() {
		return p1;
	}

	/**
	 * @param p1 the p1 to set
	 */
	public void setP1(Punt p1) {
		this.p1 = p1;
		p1.addObserver(this);
	}

	/**
	 * @return the p2
	 */
	public Punt getP2() {
		return p2;
	}

	/**
	 * @param p2 the p2 to set
	 */
	public void setP2(Punt p2) {
		this.p2 = p2;
		p2.addObserver(this);
	}

	/**
	 * @return the p3
	 */
	public Punt getP3() {
		return p3;
	}

	/**
	 * @param p3 the p3 to set
	 */
	public void setP3(Punt p3) {
		this.p3 = p3;
		p3.addObserver(this);
	}


	public Bissectrice() {
	}

	public Bissectrice(Punt p1, Punt p2, Punt p3)
	{
		setP2(p2);
		if(p1.getIndex()<p3.getIndex())
		{ 	setP1(p1);
			setP3(p3);
		} else { 
			setP1(p3);
			setP3(p1);
		}
		recalc();
	}
	private void recalc() {
		if(p1 != null && p3 != null)
		{
			final Numbers x = p2.getX();
			final Numbers y = p2.getY();
			Numbers dx1 = Numbers.sub(p1.getX(),x);
			Numbers dy1 = Numbers.sub(p1.getY(),y);
			Numbers dx3 = Numbers.sub(p3.getX(),x);
			Numbers dy3 = Numbers.sub(p3.getY(),y);
			Numbers n1 = Numbers.hypot(dx1,dy1);
			Numbers n3 = Numbers.hypot(dx3,dy3);
			setX2(Numbers.add(x , Numbers.add(Numbers.div(dx1,n1),Numbers.div(dx3,n3))));
			setY2(Numbers.add(y , Numbers.add(Numbers.div(dy1,n1),Numbers.div(dy3,n3))));
		}	
	}

	public Numbers getX2n() {
		return x2;
	}

	public Numbers getY1n() {
		return p2.getY();
	}

	public Numbers getY2n() {
		return y2;
	}

	public boolean isDefined() {
		return p2.isDefined() &&
		  (
			(p1==null || p1.isDefined())  &&
			(p3==null || p3.isDefined()) 
		  );
	}

	public String key() {
		return TYPE;
	}

	public void read(Codec codec) throws IOException {
		Punt p = codec.readPunt();
		if(p != null) setP1(p);
		p = codec.readPunt();
		if(p != null) setP2(p);
		p = codec.readPunt();
		if(p != null) setP3(p);
		recalc();
	}

	public void write(Codec codec) throws IOException {
		codec.writePunt(p1);
		codec.writePunt(p2);
		codec.writePunt(p3);
	}

	/**
	 * @param x2 the x2 to set
	 */
	public void setX2(Numbers x2) {
		if(x2 != this.x2)
			setChanged();
		this.x2 = x2;
	}

	/**
	 * @param y2 the y2 to set
	 */
	public void setY2(Numbers y2) {
		if(y2 != this.y2)
			setChanged();
		this.y2 = y2;
	}

	public void setX2n(Numbers x)
	{
		setX2(x);
	}

	public void setY2n(Numbers y)
	{
		setY2(y);
	}
	
	
	public Numbers getX1n() {
		return p2.getX();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((p1 == null) ? 0 : p1.hashCode());
		result = prime * result + ((p2 == null) ? 0 : p2.hashCode());
		result = prime * result + ((p3 == null) ? 0 : p3.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Bissectrice other = (Bissectrice) obj;
		if (p1 == null) {
			if (other.p1 != null)
				return false;
		} else if (!p1.equals(other.p1))
			return false;
		if (p2 == null) {
			if (other.p2 != null)
				return false;
		} else if (!p2.equals(other.p2))
			return false;
		if (p3 == null) {
			if (other.p3 != null)
				return false;
		} else if (!p3.equals(other.p3))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see fi.euclides.model.Destroyable#incident(fi.euclides.model.Destroyable)
	 */
	public boolean incident(Destroyable other) {
		return super.incident(other) || other == p2;
	}

	public Destroyable[] getImage(Destroyable mirror) {
		return getImage(mirror, getP2(), new Punt2(this)); // FIXME, addpoint Punt2?
	}
	
}
