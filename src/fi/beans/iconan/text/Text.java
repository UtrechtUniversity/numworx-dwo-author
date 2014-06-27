package fi.beans.iconan.text;

import java.util.Enumeration;
import java.util.ResourceBundle;

import javax.swing.Icon;

public class Text extends ResourceBundle {

	public static final String TITEL = "title";
	public static final String ANNULEER = "cancel";
	public static final String NIEUW = "add";
	public static final String REMOVE = "remove";
	public static final String OK = "ok";
	public static final String FILE = "file";
	public static final String URL = "URL";
	public static final String EDIT = "edit";
	public static final String WIJZIG = "change";

	public Enumeration getKeys() {
		// TODO Auto-generated method stub
		return null;
	}

	protected Object handleGetObject(String key) {
		return key;
	}

}
