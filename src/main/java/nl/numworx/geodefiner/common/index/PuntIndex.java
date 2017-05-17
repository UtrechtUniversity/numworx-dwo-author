package nl.numworx.geodefiner.common.index;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Groep;
import fi.euclides.model.Label;
import fi.euclides.model.Punt;
import fi.euclides.model.Visitor;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.Observable;

class PuntIndex extends Punt implements Indexed {

	private Punt delegate;
	private Groep grp;
	private Label index;
	
	public PuntIndex() {
	}

	@Override
	public Destroyable asDestroyable() {
		return this;
	}

	/* (non-Javadoc)
	 * @see fi.euclides.model.Destroyable#getDepend()
	 */
	@Override
	public Destroyable[] getDepend() {
		return new Destroyable[] { grp, index };
	}

	/**
	 * @param grp the grp to set
	 */
	public void setGrp(Groep grp) {
		this.grp = grp;
		if(grp != null) grp.addObserver(this);
	}

	/**
	 * @param index the index to set
	 */
	public void setIdx(Label index) {
		this.index = index;
		if(index != null) index.addObserver(this);
	}

	/* (non-Javadoc)
	 * @see fi.euclides.model.Punt#update(fi.euclides.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable observable, Object arg) {
		if(arg == DESTROY)
			destroy();
		else if(arg == null ) {
			if(observable == grp || observable == index)
				recalc();
			else if(observable == delegate) {
				setDefined(delegate.isDefined());
			}
			setXY(delegate.getX(), delegate.getY());
			notifyObservers();
		}
	}

	public void recalc() {
		if( grp != null && index != null) {
			int i = (int) Numbers.round(index.value).longValue();
			if(i >= 1 && i <= grp.size()) {
				if(delegate != null) delegate.deleteObserver(this);
				delegate = (Punt) grp.elementAt(i-1);
				delegate.addObserver(this);
				setDefined(delegate.isDefined());
				return;
			}
		} 
		setDefined(false);		
	}

	/**
	 * @param clz
	 * @return
	 * @see fi.euclides.util.Observable#adapt(java.lang.Class)
	 */
	public <T> T adapt(Class<T> clz) {
		return delegate.adapt(clz);
	}

	/**
	 * @return
	 * @see fi.euclides.model.Punt#getY()
	 */
	public Numbers getY() {
		return delegate.getY();
	}

	/**
	 * @return
	 * @see fi.euclides.model.Punt#getX()
	 */
	public Numbers getX() {
		return delegate.getX();
	}

	/* (non-Javadoc)
	 * @see fi.euclides.model.Destroyable#destroy()
	 */
	@Override
	public void destroy() {
		if(delegate != null) {
			delegate.deleteObserver(this);
		}
		super.destroy();
	}

	
	
}
