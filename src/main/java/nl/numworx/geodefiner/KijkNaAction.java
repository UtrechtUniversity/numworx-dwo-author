package nl.numworx.geodefiner;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;

import javax.inject.Inject;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.cbook.cbookif.CBookEventHandler;
import org.cbook.cbookif.Constants;

import fi.euclides.util.Observable;
import fi.euclides.util.Observer;

public class KijkNaAction extends AbstractAction implements Icon, Observer {

	/**
	 * 
	 */
	private final nl.numworx.geodefiner.common.Instance instance;
	ImageIcon goed, half, fout, current;
	private CBookEventHandler handler;
	
	@Inject public KijkNaAction(nl.numworx.geodefiner.common.Instance instance, CBookEventHandler handler) {
		super(Messages.getString("kijkNa"));
		this.instance = instance;
		this.handler = handler;
		putValue(LARGE_ICON_KEY, this);
		fout = new ImageIcon(getClass().getResource("resources/foutkruis.gif"));
		half = new ImageIcon(getClass().getResource("resources/goedkrulhalf.gif"));
		goed = new ImageIcon(getClass().getResource("resources/goedkrul.gif"));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.instance.fetchScore();
		this.instance.setNagekeken(true);
		feedback();
		handler.fire(Constants.CHECKED); // Score changed
	}

	void feedback() {
		Boolean status = this.instance.getStatus();
		if(status == null) putValue(LARGE_ICON_KEY, current = half);
		else if(status.booleanValue())
			putValue(LARGE_ICON_KEY, current = goed);
		else putValue(LARGE_ICON_KEY, current = fout);
		this.instance.checkObjects.feedback();
	}


	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		if(current != null) current.paintIcon(c, g, x, y);
	}

	@Override
	public int getIconWidth() {
		return goed.getIconWidth();
	}

	@Override
	public int getIconHeight() {
		return goed.getIconHeight();
	}


	@Override
	public void update(Observable observable, Object arg) {
		nofeedback();
		this.instance.setNagekeken(false); // remove "feedback" is nagekeken.
	}

	void nofeedback() {
		current = null;
		putValue(LARGE_ICON_KEY, this);
	}

}