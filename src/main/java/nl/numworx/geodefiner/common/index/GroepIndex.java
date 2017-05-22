package nl.numworx.geodefiner.common.index;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Groep;

class GroepIndex extends Groep implements Indexed<Groep> {
		private ListSelector selector;
		private Groep delegate;
		private boolean defined;
		
		GroepIndex(ListSelector listSelector) {
			selector = listSelector;
		}

		public Groep getDelegate() {
			return delegate;
		}
		public void setDelegate(Groep delegate) {
			this.delegate = delegate;
		}
		public boolean isDefined() {
			return defined;
		}
		public void setDefined(boolean defined) {
			this.defined = defined;
		}
		@Override
		public Groep asDestroyable() {
			return this;
		}
		@Override
		public void changed() {
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
