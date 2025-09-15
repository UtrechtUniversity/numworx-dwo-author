package fi.wiskopdr.tekstobjects;

public interface LinkIF {

	boolean gotoScoNr(String rest);

	/**
	 * 
	 * @return null
	 * @deprecated always null
	 */
	Object getJSObject();

	fi.beans.mainframe.AppletContext getAppletContext();

	@Deprecated
	void setJSObject(Object window);

}
