package fi.euclides.swing;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import fi.euclides.event.Tracker;

public class TrailAction extends AbstractAction {
	private Tracker viewer;

	public TrailAction(String name, Tracker viewer) {
		super(name);
		this.viewer = viewer;
	}

	public void actionPerformed(ActionEvent e) {
		viewer.getModel().toggleTrail();
		viewer.paint();
	}
}
