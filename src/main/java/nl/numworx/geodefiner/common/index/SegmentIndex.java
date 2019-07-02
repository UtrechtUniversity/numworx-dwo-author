package nl.numworx.geodefiner.common.index;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Segment;
import fi.euclides.model.VrijPunt;
import fi.euclides.model.math.Numbers;

class SegmentIndex extends Segment implements Indexed<Segment> {

	@Override
	public Segment asDestroyable() {
		return this;
	}

	private Selector selector;
	private Segment delegate;
	private boolean defined;
	

	SegmentIndex(Selector selector) {
		super(new VrijPunt(Numbers.NaN, Numbers.NaN), new VrijPunt(Numbers.NaN, Numbers.NaN));
		this.selector = selector;
	}
	
	public Segment getDelegate() {
		return delegate;
	}
	public void setDelegate(Segment delegate) {
		boolean old = this.delegate == delegate;
		this.delegate = delegate;
		if (!old && delegate != null)
			changed();
	}
	public boolean isDefined() {
		return defined;
	}
	public void setDefined(boolean defined) {
		this.defined = defined;
	}

	@Override
	public void changed() {
		if (delegate == null) return;
		getP1().setXY(delegate.getX1n(), delegate.getY1n());
		getP2().setXY(delegate.getX2n(), delegate.getY2n());
		setChanged();
	}

	@Override
	public void destroy() {
		if (delegate != null)
			delegate.deleteObserver(selector);
		super.destroy();
	}

	public Destroyable[] getDepend() {
		return selector.getDepend();
	}


}
