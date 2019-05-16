package nl.numworx.geodefiner.common.index;

import java.io.IOException;

import fi.euclides.model.Codec;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Visitor;
import fi.euclides.util.Observable;
import nl.numworx.geodefiner.common.CheckObject;

class GeneralDestroyable extends Destroyable implements Indexed<Destroyable> {

	Destroyable delegate;
	Selector selector;
	boolean defined;

	GeneralDestroyable(Selector selector) {
		this.selector = selector;
	}

	@Override
	public Destroyable asDestroyable() {
		return this;
	}

	@Override
	public void setDelegate(Destroyable d) {
		Object o = delegate;
		delegate = d;
		if(o != delegate) 
			setChanged();

	}

	@Override
	public void destroy() {
		if(delegate != null) {
			delegate.deleteObserver(selector);
		}
		super.destroy();
	}

	@Override
	public Destroyable getDelegate() {
		return delegate;
	}

	@Override
	public void changed() {
		setChanged();
	}

	@Override
	public void notifyObservers() {
		if(delegate != null)
			delegate.notifyObservers();
		super.notifyObservers();
	}

	@Override
	public void setDefined(boolean defined) {
		boolean old = isDefined();
		this.defined = defined;
		if(old != isDefined())
			setChanged();
	}

	@Override
	public boolean isDefined() {
		return defined && delegate != null && delegate.isDefined() && isTrueLabel();
	}


	private boolean isTrueLabel() {
		if(! CheckObject.isTest(delegate) ) return true;
		int state = ((Label) delegate).getState();
		return state > 0;
	}

	@Override
	public void update(Observable observable, Object arg) {
		if(delegate != null)
			delegate.update(observable, arg);
		
	}

	@Override
	public void visit(Visitor v) {
		if(delegate != null)
			delegate.visit(v);
			
	}

	@Override
	public String key() {
		return "X";
	}

	@Override
	public void write(Codec codec) throws IOException {
	}

	@Override
	public void read(Codec codec) throws IOException {
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T adapt(Class<T> clz) {
//		if ( clz.isInstance(delegate))
//			return (T) delegate;
		return super.adapt(clz);
	}

	@Override
	public Destroyable[] getDepend() {
		return selector.getDepend();
	}

}
