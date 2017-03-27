package nl.numworx.geodefiner.ui;

import java.text.ParseException;

import javax.swing.JFormattedTextField;
import javax.swing.JPanel;

public abstract class UIEditor extends JPanel {

	public abstract void commit();

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
