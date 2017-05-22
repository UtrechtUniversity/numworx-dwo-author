package nl.numworx.geodefiner.common.index;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Kegelsnede2;
import fi.euclides.model.SegmentVisitor;

class KegelsnedeIndex extends Kegelsnede2 implements Indexed<Kegelsnede2> {

	private ListSelector selector;
	private Kegelsnede2 delegate;
	private boolean defined;
	
	public KegelsnedeIndex(ListSelector listSelector) {
		selector = listSelector;
	}

	public Kegelsnede2 getDelegate() {
		return delegate;
	}

	public void setDelegate(Kegelsnede2 delegate) {
		this.delegate = delegate;
	}

	public boolean isDefined() {
		return defined;
	}

	public void setDefined(boolean defined) {
		this.defined = defined;
	}

	@Override
	public Kegelsnede2 asDestroyable() {
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

	public void visitSegments(SegmentVisitor v) {
		delegate.visitSegments(v);
	}

}
