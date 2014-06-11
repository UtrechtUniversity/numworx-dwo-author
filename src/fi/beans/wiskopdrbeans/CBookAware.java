package fi.beans.wiskopdrbeans;

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

}
