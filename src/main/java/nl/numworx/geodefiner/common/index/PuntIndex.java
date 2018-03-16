package nl.numworx.geodefiner.common.index;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Punt;
import fi.euclides.model.math.Numbers;

class PuntIndex extends Punt implements Indexed<Punt> {

	private Punt delegate;
	private ListSelector selector;
	
	public Punt getDelegate() {
		return delegate;
	}

	public void setDelegate(Punt delegate) {
		this.delegate = delegate;
	}

	public PuntIndex(ListSelector listSelector) {
		selector = listSelector;
	}

	@Override
	public Punt asDestroyable() {
		return this;
	}

// twijfelachtig, visible, toString, 
//	/**
//	 * @param clz
//	 * @return
//	 * @see fi.euclides.util.Observable#adapt(java.lang.Class)
//	 */
//	public <T> T adapt(Class<T> clz) {
//		return delegate.adapt(clz);
//	}

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
			delegate.deleteObserver(selector);
		}
		super.destroy();
	}

	@Override
	public void changed() {
		if (delegate != null)
			setXY(delegate.getX(), delegate.getY());
		else
			setXY(Numbers.NaN, Numbers.NaN);
	}

	public Destroyable[] getDepend() { return selector.getDepend(); }
	
}
