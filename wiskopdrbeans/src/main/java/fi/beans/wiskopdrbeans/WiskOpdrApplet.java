package fi.beans.wiskopdrbeans;

import java.applet.AppletStub;

public interface WiskOpdrApplet {
	
	public InteractiePanel getInteractiePanel();
	@Deprecated
	public default void setStub(AppletStub stub) {
	  setStub ((fi.beans.mainframe.AppletStub) stub );
	}
	
	public default void setStub(fi.beans.mainframe.AppletStub s) {
	  setStub ( (AppletStub) s);
	}

}
