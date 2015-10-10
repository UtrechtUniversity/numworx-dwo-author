package fi.wiskopdr.cbook;

import org.cbook.cbookif.CBookEvent;
import org.cbook.cbookif.CBookEventListener;

import fi.beans.wiskopdrbeans.CBookAware;

class CBABridge implements CBookAware {
	private CBookInteractieEditPanel editor;

	CBABridge(CBookInteractieEditPanel editor) {
		this.editor = editor;
	}
	
	@Deprecated public void acceptCBookEvent(CBookEvent event) {}
	@Deprecated public void addCBookEventListener(CBookEventListener listener, String command) {}
	@Deprecated public void removeCBookEventListener(CBookEventListener listener, String command) {}

	@Override
	public String[] getSendCmds() {
		return editor.getSendCmds();
	}

	@Override
	public String[] getAcceptedCmds() {
		return editor.getAcceptedCmds();
	}

	@Override
	public String getLocalizedCmd(String cmd) {
		return editor.getLocalizedCmd(cmd);
	}
	
	@Override
	public String toString() {
		return editor.toString();
	}
	
}