package nl.numworx.geodefiner.common.index;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Locus;
import fi.euclides.model.SegmentVisitor;

class LocusIndex extends Locus implements Indexed<Locus> {

	private ListSelector selector;
	private Locus delegate;
	private boolean defined;
	
	LocusIndex(ListSelector listSelector) {
		selector = listSelector;
	}

	public Locus getDelegate() {
		return delegate;
	}

	public void setDelegate(Locus delegate) {
		this.delegate = delegate;
	}

	@Override
	public Locus asDestroyable() {
		return this;
	}

	@Override
	public void changed() {
		setChanged();
	}

	@Override
	public void setDefined(boolean defined) {
		this.defined = defined;		
	}

	public boolean isDefined() {
		return defined;
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
