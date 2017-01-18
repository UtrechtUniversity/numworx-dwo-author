package fi.euclides.model;

import java.io.IOException;

import fi.euclides.model.math.Numbers;
import fi.euclides.util.Observable;

public class PuntenLijn extends Lijn {

	public static final String TYPE = "l";
	/**
	 * 
	 */
	public PuntenLijn() {
	}
	
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

	public void update(Observable o, Object arg) {
		setChanged();
		if(arg == DESTROY)
			destroy();
		super.update(o, arg);
	}

	private Punt p1, p2;

	public Numbers getX1n() {
		return p1.getX();
	}

	public Numbers getX2n() {
		return p2.getX();
	}


	public Numbers getY1n() {
		return p1.getY();
	}

	public Numbers getY2n() {
		return p2.getY();
	}

	public boolean isDefined() {
		return p1.isDefined() && p2.isDefined() && (nonzero(getDXn()) || nonzero(getDYn()));
	}

	private boolean nonzero(Numbers yn) {
		return Numbers.signum(yn)!=0;
	}

	/**
	 * @param p1
	 * @param p2
	 */
	public PuntenLijn(Punt p1, Punt p2) {
		if(p1.getIndex()<p2.getIndex())
		{ setP1(p1);
		  setP2(p2);
		} else {
			setP1(p2);
			setP2(p1);
		}
	}

	public Destroyable[] getDepend() {
		return new Punt[] { p1, p2 };
	}
	
	public String key() {
		return TYPE;
	}

	public void read(Codec codec) throws IOException {
		Punt p = codec.readPunt();
		if(p != null) setP1(p);
		p = (codec.readPunt());
		if(p != null) setP2(p);
	}

	public void write(Codec codec) throws IOException {
		codec.write(getDepend());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((p1 == null) ? 0 : p1.hashCode());
		result = prime * result + ((p2 == null) ? 0 : p2.hashCode());
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
		final PuntenLijn other = (PuntenLijn) obj;
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
		return true;
	}

	/* (non-Javadoc)
	 * @see fi.euclides.model.Destroyable#incident(fi.euclides.model.Destroyable)
	 */
	public boolean incident(Destroyable other) {
		return super.incident(other) || other == p1 || other == p2;
	}

	public Observable newInstance() { return new PuntenLijn(); }
	
	public Destroyable[] getImage(Destroyable mirror)
	{
		if(mirror instanceof Cirkel)
		{
			if(getClass() == PuntenLijn.class)
				return getImage(mirror, p1, p2);
			return super.getImage(mirror);
		} 
		PuntenLijn clone;
		clone = (PuntenLijn) newInstance();
		Punt p1 = getP1().getImage(mirror, this); p1.setVisible(false);
		Punt p2 = getP2().getImage(mirror, this); p2.setVisible(false);
		clone.setP1(p1);
		clone.setP2(p2);
		return new Destroyable[] { p1, p2, clone };
	}
	
}
