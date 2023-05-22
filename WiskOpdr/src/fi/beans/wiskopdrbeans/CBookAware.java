package fi.beans.wiskopdrbeans;

import org.cbook.cbookif.CBookContext;
import org.cbook.cbookif.CBookEventListener;

/**
 * Optional extension of InteractiePanel
 * @author wim
 *
 */
public interface CBookAware extends CBookEventListener {

	void addCBookEventListener(CBookEventListener listener, String command);

	void removeCBookEventListener(CBookEventListener listener, String command);

	String[] getSendCmds();

	String[] getAcceptedCmds();
	
	String getLocalizedCmd(String cmd);
	
	String CBA_PREFIX = "CBA_";

	default void setCBookContext(CBookContext context) {} 
	
}
