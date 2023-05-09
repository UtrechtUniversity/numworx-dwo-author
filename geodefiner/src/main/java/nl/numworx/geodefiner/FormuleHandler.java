package nl.numworx.geodefiner;

import java.awt.Component;

import nl.tue.win.riaca.openmath.lang.OMObject;
import fi.beans.numworxlf.JOptionPane;
import fi.euclides.event.EventHandler;
import fi.euclides.formuleobjects.FormuleParser;
import fi.euclides.formuleobjects.ParseException;

public class FormuleHandler extends EventHandler {

	public FormuleHandler(String string, Definitions d) {
		super(string);
		definitions = d;
	}
	private Definitions definitions;

	@Override
	public void command() {

		CommandPanel message = new CommandPanel(null);
		Component parent = getTracker().adapt(Component.class);
		int r = JOptionPane.showConfirmDialog(parent, message, "Definitie", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if(r == JOptionPane.OK_OPTION) {
			String formule = message.toString();
			OMObject object;
			try {
				object = new FormuleParser(formule.substring(2)).parse();
				definitions.define(formule, object);
			} catch (ParseException e) {
				e.printStackTrace();
				setStatus(e.toString());
			}
		}
	
	}

}
