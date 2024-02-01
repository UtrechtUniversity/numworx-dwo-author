package fi.wiskopdr.tekstobjects;

import java.applet.AppletContext;

public interface LinkIF {

	boolean gotoScoNr(String rest);

	/**
	 * 
	 * @return null
	 * @deprecated always null
	 */
	Object getJSObject();

	AppletContext getAppletContext();

	@Deprecated
	void setJSObject(Object window);

}
