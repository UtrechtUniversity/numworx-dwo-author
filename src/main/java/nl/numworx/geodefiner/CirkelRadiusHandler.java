package nl.numworx.geodefiner;

import java.awt.Component;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.JOptionPane;

import fi.euclides.model.Punt;
import fi.euclides.model.math.Numbers;
import nl.numworx.geodefiner.common.AbstractCirkelLabelHandler;

public class CirkelRadiusHandler extends AbstractCirkelLabelHandler {

	public CirkelRadiusHandler(String string) {
		super(string);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void attachLabel(Punt p) {
		CommandPanel message = new CommandPanel(null);
		Component parent = getTracker().adapt(Component.class);
		int r = JOptionPane.showConfirmDialog(parent, message, Messages.getString("CirkelRadiusHandler.0"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if(r == JOptionPane.OK_OPTION) {
			String formule = message.toString();
			formule = formule.substring(2, formule.length()-1);
			try {
				Number d = NumberFormat.getNumberInstance().parse(formule);
				Numbers dd = Numbers.createDouble(d.doubleValue());
				build(p,dd);
			} catch (ParseException e) {
				setStatus(e.toString());
			}
		}
	}

}
