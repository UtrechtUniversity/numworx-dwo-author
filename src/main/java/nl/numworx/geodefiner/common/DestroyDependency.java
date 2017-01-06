package nl.numworx.geodefiner.common;

import fi.euclides.model.Destroyable;
import fi.euclides.util.Observable;
import fi.euclides.util.Observer;

public final class DestroyDependency implements Observer {
	private final Destroyable destroyable;

	public DestroyDependency(Destroyable destroyable) {
		this.destroyable = destroyable;
	}

	@Override
	public void update(Observable locus, Object arg1) {
		if(arg1 == Destroyable.DESTROY)
		{
			locus.deleteObserver(this);
			destroyable.destroy();
		}
		
	}
}