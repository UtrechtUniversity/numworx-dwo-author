package nl.numworx.geodefiner;

import java.awt.Component;

import javax.swing.JOptionPane;

import fi.euclides.model.Label;
import fi.euclides.model.Punt;
import fi.euclides.model.math.Numbers;
import nl.numworx.geodefiner.common.AbstractTextHandler;
import nl.numworx.geodefiner.common.Volgpunt;

public class TextHandler extends AbstractTextHandler {

	TextHandler(String string) {
		super(string);
	}

	protected void attachLabel(Punt p) {
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
			Volgpunt vp;
			p = vp = new Volgpunt(p);
			vp.setDxy(Numbers.createInteger(4), Numbers.ZERO);
			vp.setFree(false);
			l.setP(p);
			getModel().add(l);
		}
	}

}
