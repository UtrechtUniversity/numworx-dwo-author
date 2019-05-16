package nl.numworx.geodefiner.common.index;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Lijn;
import fi.euclides.model.PuntenLijn;
import fi.euclides.model.VrijPunt;
import fi.euclides.model.math.Numbers;

class LijnIndex extends PuntenLijn implements Indexed<Lijn> {

	private Lijn delegate;
	private boolean defined;
	private Selector selector;
	
	LijnIndex(Selector s) {
		super(new VrijPunt(), new VrijPunt());
		selector = s;
	}
	
	@Override
	public Lijn asDestroyable() {
		return this;
	}

	@Override
	public Numbers getX1n() {
		return delegate.getX1n();
	}

	@Override
	public Numbers getX2n() {
		return delegate.getX2n();
	}

	@Override
	public Numbers getY1n() {
		return delegate.getY1n();
	}

	@Override
	public Numbers getY2n() {
		return delegate.getY2n();
	}

	@Override
	public boolean isDefined() {
		return defined;
	}

	@Override
	public void setDelegate(Lijn d) {
		delegate = d;		
	}

	@Override
	public Lijn getDelegate() {
		return delegate;
	}

	@Override
	public void changed() {
		getP1().setXY(delegate.getX1n(), delegate.getY1n());
		getP2().setXY(delegate.getX2n(), delegate.getY2n());
		setChanged();
	}

	@Override
	public void setDefined(boolean defined) {
		this.defined = defined;
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
