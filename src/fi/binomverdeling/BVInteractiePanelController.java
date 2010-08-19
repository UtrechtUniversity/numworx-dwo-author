package fi.binomverdeling;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fi.beans.wiskopdrbeans.InteractiePanel;

public class BVInteractiePanelController implements ActionListener{
	private BVInteractiePanelModel model;
	private BVInteractiePanelView view;

	public BVInteractiePanelController() {
		this.model = new BVInteractiePanelModel(0.3, 30, 5);
		this.view = new BVInteractiePanelView(this.model);
		//this.view.addComponentListener(this);
		this.view.addNListener(this);
		this.view.addKListener(this);
		this.view.addPListener(this);
	}

	/*
	@Override
	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentResized(ComponentEvent arg0) {
		this.view.repaint();

	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}
	*/

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getActionCommand().equals("ntextupdate")) {
			int n;
			try {
				n = Integer.parseInt(this.view.getNText());
			}
			catch (NumberFormatException e) {
				n = this.model.getN();
			}
			this.model.setN(n);
		}
		if (arg0.getActionCommand().equals("ptextupdate")) {
			double p;
			try {
				p = Double.parseDouble(this.view.getPText());
			}
			catch (NumberFormatException e) {
				p = this.model.getP();
			}
			this.model.setP(p);
		}
		if (arg0.getActionCommand().equals("ktextupdate")) {
			int k;
			try {
				k = Integer.parseInt(this.view.getKText());
			}
			catch (NumberFormatException e) {
				k = this.model.getSuccessen();
			}
			this.model.setSuccessen(k);
		}
	}
}
