package nl.numworx.geodefiner.common.index;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Groep;
import fi.euclides.model.Label;
import fi.euclides.model.Punt;
import fi.euclides.model.Ray;

class RayIndex extends Ray implements Indexed<Ray> {

	private Ray delegate;
	private boolean defined;
	private ListSelector selector;
	
	
	public Ray getDelegate() {
		return delegate;
	}

	public void setDelegate(Ray delegate) {
		this.delegate = delegate;
	}

	public boolean isDefined() {
		return defined;
	}

	public void setDefined(boolean defined) {
		this.defined = defined;
	}

	RayIndex(ListSelector listSelector) {
		selector = listSelector;
	}

	@Override
	public Ray asDestroyable() {
		return this;
	}

	@Override
	public void changed() {
		getP1().setXY(delegate.getX1n(), delegate.getY1n());
		getP2().setXY(delegate.getX2n(), delegate.getY2n());
		setChanged();
	}

	@Override
	public void destroy() {
		delegate.deleteObserver(selector);
		super.destroy();
	}

	public Destroyable[] getDepend() {
		return selector.getDepend();
	}


}
