package fi.ivmdraw;

import java.util.HashMap;
import java.util.Hashtable;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fi.beans.wiskopdrbeans.InteractieEditPanel;

public class IVMdrawEditPanel extends JPanel implements InteractieEditPanel {

	IVMdrawPanel ivmDrawPanel;
	JComboBox vaasKeuze;
	
	public IVMdrawEditPanel(IVMdrawPanel ivmDrawPanel) {
		this.ivmDrawPanel = ivmDrawPanel;
		JLabel label = new JLabel("Dit is het interactieEditpanel");
		add(label);
		    
		vaasKeuze = new JComboBox();
		vaasKeuze.addItem("Vaas 1");
		vaasKeuze.addItem("Vaas 2");
		vaasKeuze.addItem("Vaas 3");
		vaasKeuze.addItem("Vaas 4");
		vaasKeuze.addItem("Vaas 5");
		add(vaasKeuze);
	}

	public Hashtable getEditState() {
		Hashtable h = ivmDrawPanel.getEditState();
		h.put("vaasNummer", new Integer(vaasKeuze.getSelectedIndex()));
		return h;
		
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
