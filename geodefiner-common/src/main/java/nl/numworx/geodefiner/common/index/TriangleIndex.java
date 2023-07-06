package nl.numworx.geodefiner.common.index;

import fi.euclides.model.Destroyable;
import fi.euclides.model.SegmentVisitor;
import fi.euclides.model.Triangle;

class TriangleIndex extends Triangle implements Indexed<Triangle> {

	private Triangle delegate;
	private Selector selector;
	private boolean defined;
	
	public boolean isDefined() {
		return defined;
	}

	public void setDefined(boolean defined) {
		this.defined = defined;
	}

	TriangleIndex(Selector listSelector) {
		selector = listSelector;
	}

	public Triangle getDelegate() {
		return delegate;
	}

	public void setDelegate(Triangle delegate) {
		this.delegate = delegate;
	}

	@Override
	public Triangle asDestroyable() {
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
