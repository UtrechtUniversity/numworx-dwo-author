package fi.wiskopdr;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.JLayeredPane;

import fi.beans.wiskopdrbeans.InteractieEditPanel;

public class BerekeningVakEditPanel extends JLayeredPane implements InteractieEditPanel, ActionListener, HelpButtonPanelIF {

	@Override
	public void showHelpButtons(boolean b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String geefHelpURL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEditState(Hashtable b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Hashtable getEditState() {
		Hashtable interactiePanelLaunchState = new Hashtable();
		return interactiePanelLaunchState;
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

}
