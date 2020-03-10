package fi.wiskopdr.tekstobjects;

import java.applet.AppletContext;

public interface LinkIF {

	boolean gotoScoNr(String rest);

	Object getJSObject();

	AppletContext getAppletContext();

	void setJSObject(Object window);

}
