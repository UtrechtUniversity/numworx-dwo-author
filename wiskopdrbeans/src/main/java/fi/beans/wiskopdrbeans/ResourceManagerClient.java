package fi.beans.wiskopdrbeans;

import org.cbook.cbookif.rm.ResourceManager;

/**
 * Marker interface. Dit InteractiePanel heeft een id nodig
 * The id is voor de ResourceManager.
 * @author wim
 * @see InteractiePanel
 * @see org.cbook.cbookif.rm.ResourceManager
 */
public interface ResourceManagerClient {
	interface ResourceManagerFactory {
		ResourceManager getResourceManager();
	}
	void setInstanceId(String id);
	void setFactory(ResourceManagerFactory factory);
	String getInstanceId();
	String getClassName();
}
