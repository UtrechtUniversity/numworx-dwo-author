package fi.wiskopdr.tekstobjects;

import java.applet.AppletContext;
//no references at mayscript.jar until absolutely neccessary?
//import netscape.javascript.JSObject;

public interface LinkIF {

	boolean gotoScoNr(String rest);

	Object getJSObject();

	AppletContext getAppletContext();

	void setJSObject(Object window);

}
