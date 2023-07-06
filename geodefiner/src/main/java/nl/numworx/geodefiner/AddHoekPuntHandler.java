package nl.numworx.geodefiner;

import java.awt.Component;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.inject.Inject;

import fi.beans.numworxlf.JOptionPane;
import fi.euclides.model.math.Numbers;
import nl.numworx.geodefiner.common.AbstractAddHoekPuntHandler;

public class AddHoekPuntHandler extends AbstractAddHoekPuntHandler {
	@Inject AddHoekPuntHandler() {
		super("Punt onder hoek");
	}

	@Override
	public void createTrack() {
		CommandPanel message = new CommandPanel(null);
		Component parent = getTracker().adapt(Component.class);
		int r = JOptionPane.showConfirmDialog(parent, message, "Hoek in graden", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if(r == JOptionPane.OK_OPTION) {
			String formule = message.toString();
			formule = formule.substring(2, formule.length()-1);
			try {
				Number d = NumberFormat.getNumberInstance().parse(formule);
				Numbers dd = Numbers.createDouble(d.doubleValue());
				build(p1,p2,dd);
			} catch (ParseException e) {
				setStatus(e.toString());
			}
		}
		getModel().clearSelection();		
	}

}