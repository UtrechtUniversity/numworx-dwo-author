package fi.beans.wiskopdrbeans;

import java.applet.AppletStub;

public interface WiskOpdrApplet {
	
	public InteractiePanel getInteractiePanel();
	@Deprecated
	public void setStub(AppletStub stub);
	
	public default void setStub(fi.beans.mainframe.AppletStub s) {
	  setStub ( (AppletStub) s);
	}

}
