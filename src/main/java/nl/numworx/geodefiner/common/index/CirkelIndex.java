package nl.numworx.geodefiner.common.index;

import fi.euclides.model.Cirkel;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Groep;
import fi.euclides.model.Label;
import fi.euclides.model.VrijPunt;

class CirkelIndex extends Cirkel implements Indexed<Cirkel> {

	private Cirkel delegate;
	private ListSelector selector;
	
	CirkelIndex(ListSelector listSelector) {
		super(new VrijPunt(), new VrijPunt());
		selector = listSelector;
	}

	@Override
	public Cirkel asDestroyable() {
		return this;
	}

	@Override
	public void setDelegate(Cirkel d) {
		delegate = d;
	}

	@Override
	public Cirkel getDelegate() {
		return delegate;
	}

	@Override
	public void changed() {
		getCenter().setXY(delegate.getCenter().getX(), delegate.getCenter().getY());
		getRadius().setXY(delegate.getRadius().getX(), delegate.getRadius().getY());
		setChanged();
	}

	private boolean defined;

	@Override
	public void setDefined(boolean defined) {
		this.defined = defined;
	}

	@Override
	public boolean isDefined() {
		return defined;
	}

	/**
	 * @param clz
	 * @return
	 * @see fi.euclides.util.Observable#adapt(java.lang.Class)
	 */
	public <T> T adapt(Class<T> clz) {
		return delegate.adapt(clz);
	}

}
