package nl.numworx.geodefiner.ui;

import java.text.ParseException;

import javax.swing.JFormattedTextField;
import javax.swing.JPanel;

import fi.euclides.event.Tracker;

@SuppressWarnings("serial")
public abstract class UIEditor extends JPanel {

	public abstract void commit();
	public boolean verify(Tracker t) { 
		return true;
	}

	protected void commitFields(JFormattedTextField... fields) {
		for( JFormattedTextField field: fields) {
			try {
				field.commitEdit();
			} catch(ParseException pe) {
				field.setValue(null);
			}
		}
	}
}
