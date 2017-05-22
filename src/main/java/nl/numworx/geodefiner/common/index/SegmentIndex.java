package nl.numworx.geodefiner.common.index;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Groep;
import fi.euclides.model.Label;
import fi.euclides.model.Segment;
import fi.euclides.model.VrijPunt;

class SegmentIndex extends Segment implements Indexed<Segment> {

	@Override
	public Segment asDestroyable() {
		return this;
	}

	private ListSelector selector;
	private Segment delegate;
	private boolean defined;
	

	SegmentIndex(ListSelector selector) {
		super(new VrijPunt(), new VrijPunt());
		this.selector = selector;
	}
	
	public Segment getDelegate() {
		return delegate;
	}
	public void setDelegate(Segment delegate) {
		this.delegate = delegate;
	}
	public boolean isDefined() {
		return defined;
	}
	public void setDefined(boolean defined) {
		this.defined = defined;
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
