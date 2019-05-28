package fi.ivmdraw;

import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JPanel;

import fi.beans.wiskopdrbeans.InteractieEditPanel;

public class IVMdrawEditPanel extends JPanel implements InteractieEditPanel {

	IVMdrawPanel ivmDrawPanel;
	
	public IVMdrawEditPanel(IVMdrawPanel ivmDrawPanel) {
		this.ivmDrawPanel = ivmDrawPanel;
		 JLabel label = new JLabel("Dit is het interactieEditpanel");
		    add(label);
	}

	public Hashtable getEditState() {
		return ivmDrawPanel.getEditState();
	}

	public void setEditState(Hashtable arg0) {
		// TODO Auto-generated method stub

	}

	public void start() {
		// TODO Auto-generated method stub

	}

	public void stop() {
		// TODO Auto-generated method stub

	}

	public void zetBreedte(int arg0) {
		// TODO Auto-generated method stub

	}

	public void zetHoogte(int arg0) {
		// TODO Auto-generated method stub

	}

}
