package nl.numworx.geodefiner.common.index;

import fi.euclides.model.Boog;
import fi.euclides.model.Destroyable;

class BoogIndex extends Boog implements Indexed<Boog> {

	private Boog delegate;
	private Selector selector;
	private boolean defined;
	
	BoogIndex(Selector listSelector) {
		selector = listSelector;
	}

	public boolean isDefined() {
		return defined;
	}

	public void setDefined(boolean defined) {
		this.defined = defined;
	}

	public Boog getDelegate() {
		return delegate;
	}

	public void setDelegate(Boog delegate) {
		this.delegate = delegate;
	}

	@Override
	public Boog asDestroyable() {
		return this;
	}

	@Override
	public void changed() {
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
