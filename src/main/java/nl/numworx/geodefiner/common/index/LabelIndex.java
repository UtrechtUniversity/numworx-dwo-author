package nl.numworx.geodefiner.common.index;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.math.Numbers;

class LabelIndex extends Label implements Indexed<Label> {
	
	private Selector selector;
	private Label delegate;
	
	public Label getDelegate() {
		return delegate;
	}

	public void setDelegate(Label delegate) {
		this.delegate = delegate;
		changed();
	}

	LabelIndex(Selector selector) {
		super();
		this.selector = selector;
	}

	@Override
	public Label asDestroyable() {
		return this;
	}

	@Override
	public void changed() {
		if(delegate == null) {
			setString("");
			setValue(Numbers.NaN);
			setState(Label.FALSE);
			return;
		}
		setString(delegate.getString());
		setValue(delegate.value);
		setState(delegate.getState());
		getP().setXY(delegate.getX(), delegate.getY());
	}

	@Override
	public void destroy() {
		if(delegate != null)
			delegate.deleteObserver(selector);
		super.destroy();
	}

	public Destroyable[] getDepend() {
		return selector.getDepend();
	}

}
