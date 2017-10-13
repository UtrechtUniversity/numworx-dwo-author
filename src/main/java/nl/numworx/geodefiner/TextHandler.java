package nl.numworx.geodefiner;

import java.awt.Component;
import java.util.Vector;

import javax.swing.JOptionPane;

import nl.numworx.geodefiner.common.Volgpunt;
import fi.euclides.event.EventHandler;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Punt;

public class TextHandler extends EventHandler {

	TextHandler(String string) {
		super(string);
	}

	@Override
	public void command() {
		Vector<Destroyable> selection = getModel().getSelect();
		if(selection.size() == 1) {
			Destroyable first = selection.firstElement();
			if(first instanceof Punt) {
				Punt p = (Punt) first;
				attachLabel(p);
			}
		}	
	}

	private void attachLabel(Punt p) {
		CommandPanel message = new CommandPanel();
		message.random = null; // no randomizer
		message.instance = null; // no NPE!
		Component parent = getTracker().adapt(Component.class);
		int r = JOptionPane.showConfirmDialog(parent, message, string, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if(r == JOptionPane.OK_OPTION) {
			String formule = message.toString();
			formule = formule.substring(2, formule.length()-1);
			Label l = new Label();
			l.setString(formule);
			p = new Volgpunt(p);
			l.setP(p);
			getModel().add(l);
		}
	}

}
