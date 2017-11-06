package fi.euclides.model;

import java.io.IOException;

import fi.euclides.model.math.Numbers;
import fi.euclides.util.Observable;

public abstract class LijnPuntCombi<T extends Destroyable> extends Lijn {

	protected T lijn;
	protected Punt punt;

	/**
	 * @param lijn
	 * @param punt
	 */
	protected LijnPuntCombi(T lijn, Punt punt) {
		setDestroyable(lijn);
		setPunt(punt);
	}

	protected LijnPuntCombi() {
	}

	public boolean isDefined() {
		return lijn.isDefined() && punt.isDefined();
	}

	/* (non-Javadoc)
	 * @see euclides.Destroyable#destroy()
	 */
	public void destroy() {
		punt.deleteObserver(this);
		lijn.deleteObserver(this);
		super.destroy();
	}

	public void update(Observable o, Object arg) {
		if(arg == DESTROY) {
			destroy();
		}
		else if(o == lijn || o == punt) {
			setChanged();
			notifyObservers(arg);
		}
	}

	public void read(Codec codec) throws IOException {
			Destroyable l = codec.readDestroyable();
			if(l!=null)setDestroyable((T) l);
			Punt p = codec.readPunt();
			if(p!=null)setPunt(p);
	}

	public void write(Codec codec) throws IOException {
			codec.writeDestroyable(getDestroyable());
			codec.writePunt(getPunt());
		
	}

	public T getDestroyable() {
		return lijn;
	}

	/**
	 * @param lijn the lijn to set
	 */
	public void setDestroyable(T lijn) {
		this.lijn = lijn;
		lijn.addObserver(this);
	}

	/**
	 * @return the punt
	 */
	public Punt getPunt() {
		return punt;
	}

	/**
	 * @param punt the punt to set
	 */
	public void setPunt(Punt punt) {
		this.punt = punt;
		punt.addObserver(this);
	}
	
	/**
	 * @return
	 * @see fi.euclides.model.Punt#getX()
	 */
	public Numbers getX1n() {
		return punt.getX();
	}

	/**
	 * @return
	 * @see fi.euclides.model.Punt#getY()
	 */
	public Numbers getY1n() {
		return punt.getY();
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
		final LijnPuntCombi other = (LijnPuntCombi) obj;
		if (lijn == null) {
			if (other.lijn != null)
				return false;
		} else if (!lijn.equals(other.lijn))
			return false;
		if (punt == null) {
			if (other.punt != null)
				return false;
		} else if (!punt.equals(other.punt))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see fi.euclides.model.Destroyable#incident(fi.euclides.model.Destroyable)
	 */
	public boolean incident(Destroyable other) {
		return super.incident(other)||other == punt;
	}

	public Destroyable[] getImage(Destroyable mirror)
	{
		return getImage(mirror, punt, new Punt2(this));
	}
}
