/**
 * 
 */
package nl.numworx.geodefiner.common;

import java.util.Collection;

import fi.euclides.event.DestroyHandler;
import fi.euclides.model.Destroyable;

/**
 * @author wim
 *
 */
public class FilteredDestroyHandler extends DestroyHandler {

	private final Instance instance;
	/**
	 * @param instance 
	 * 
	 */
	public FilteredDestroyHandler(nl.numworx.geodefiner.common.Instance instance) {
		this.instance = instance;
	}
	@Override
	protected boolean filterSelection(Collection<? extends Destroyable> set) {	
		set.removeAll(instance.resetItems);
		return super.filterSelection(set);
	}

}
