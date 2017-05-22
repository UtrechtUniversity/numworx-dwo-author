package nl.numworx.geodefiner.common.index;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;

class LabelIndex extends Label implements Indexed<Label> {
	
	private ListSelector selector;
	private Label delegate;
	
	public Label getDelegate() {
		return delegate;
	}

	public void setDelegate(Label delegate) {
		this.delegate = delegate;
	}

	LabelIndex(ListSelector selector) {
		super();
		this.selector = selector;
	}

	@Override
	public Label asDestroyable() {
		return this;
	}

	@Override
	public void changed() {
		setString(delegate.getString());
		setValue(delegate.value);
		setState(delegate.getState());
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
