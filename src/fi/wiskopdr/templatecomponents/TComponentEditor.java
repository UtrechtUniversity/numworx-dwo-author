package fi.wiskopdr.templatecomponents;

import java.awt.event.ActionListener;
import java.util.Hashtable;

import fi.wiskopdr.tekstobjects.TekstInteractiePanelVak;

public interface TComponentEditor {

	public Hashtable getPreferences();
	
	public void setPreferences(Hashtable<String,Object> preferences);
	
	public void addActionListener(ActionListener actionListener);
	
}
