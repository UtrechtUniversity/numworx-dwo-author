package nl.numworx.geodefiner;

import java.awt.Component;

import javax.inject.Inject;
import javax.swing.JOptionPane;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Punt;
import fi.euclides.model.math.Numbers;
import fi.euclides.proof.LabelDelegate;
import nl.numworx.geodefiner.common.AbstractTextHandler;
import nl.numworx.geodefiner.common.Volgpunt;
import nl.numworx.geodefiner.common.math.ToC;

public class TextHandler extends AbstractTextHandler {

	
	public TextHandler(String string) {
		super(string);
	}

	protected void attachLabel(Punt p) {
		CommandPanel message = new CommandPanel(null);
		Component parent = getTracker().adapt(Component.class);
		int r = JOptionPane.showConfirmDialog(parent, message, string, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if(r == JOptionPane.OK_OPTION) {
			String formule = message.toString();
			formule = formule.substring(2, formule.length()-1);
			Label l;
			LabelDelegate toc = getTracker().getRegistered(ToC.TYPE);
			if(toc != null) {
				Destroyable[] depend = toc.createDepend(1);
				depend[0] = p;
				l = toc.define(depend);
			} else {
				l = new Label();
			}
			l.setString(formule);
			Volgpunt vp;
			p = vp = new Volgpunt(p);
			vp.setDxy(Numbers.createInteger(6), Numbers.createInteger(-5));
			vp.setFree(false);
			l.setP(vp);
			getModel().add(l);
		}
	}

}
