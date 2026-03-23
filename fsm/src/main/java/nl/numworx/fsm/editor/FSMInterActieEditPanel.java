package nl.numworx.fsm.editor;

import java.util.Hashtable;

import javax.swing.JPanel;

import fi.beans.wiskopdrbeans.InteractieEditPanel;

public class FSMInterActieEditPanel extends JPanel implements InteractieEditPanel {

	Hashtable launchdata = new Hashtable();
	
	@Override
	public void setEditState(Hashtable b) {
		if (b == null) b = new Hashtable();
		launchdata = b;

	}

	@Override
	public Hashtable getEditState() {		
		return launchdata;
	}

	@Override
	public void zetBreedte(int b) {
		// TODO Auto-generated method stub

	}

	@Override
	public void zetHoogte(int h) {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop() {
	}

	@Override
	public void start() {
	}

}
