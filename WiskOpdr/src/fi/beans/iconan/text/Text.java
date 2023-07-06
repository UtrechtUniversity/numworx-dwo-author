package fi.beans.iconan.text;

import java.util.Enumeration;
import java.util.ResourceBundle;

public class Text extends ResourceBundle {

	public static final String TITEL = "title";
	public static final String ANNULEER = "cancel";
	public static final String NIEUW = "add";
	public static final String REMOVE = "remove";
	public static final String OK = "ok";
	public static final String CLOSE = "close";
	public static final String FILE = "file";
	public static final String URL = "URL";
	public static final String EDIT = "edit";
	public static final String WIJZIG = "change";
	public static final String EDIT_URL ="URL of picture";

	public Enumeration getKeys() {
		return null;
	}

	protected Object handleGetObject(String key) {
		return key;
	}

}
