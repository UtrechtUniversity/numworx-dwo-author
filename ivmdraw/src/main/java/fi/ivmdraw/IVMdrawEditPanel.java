package fi.ivmdraw;

import java.util.HashMap;
import java.util.Hashtable;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fi.beans.wiskopdrbeans.InteractieEditPanel;

public class IVMdrawEditPanel extends JPanel implements InteractieEditPanel {

	private IVMdrawPanel ivmDrawPanel;
	
	private JComboBox vaasKeuze;
	private JCheckBox feedbackVisibleCB;
	
	public IVMdrawEditPanel(IVMdrawPanel ivmDrawPanel) {
		this.ivmDrawPanel = ivmDrawPanel;
//		JLabel label = new JLabel("Dit is het interactieEditpanel");
//		add(label);
		    
		vaasKeuze = new JComboBox();
		vaasKeuze.addItem(IVMdraw.rb.getString("vaas") + " 1");
		vaasKeuze.addItem(IVMdraw.rb.getString("vaas") + " 2");
		vaasKeuze.addItem(IVMdraw.rb.getString("vaas") + " 3");
		vaasKeuze.addItem(IVMdraw.rb.getString("vaas") + " 4");
		vaasKeuze.addItem(IVMdraw.rb.getString("vaas") + " 5");
		add(vaasKeuze);
		
		feedbackVisibleCB = new JCheckBox(IVMdraw.rb.getString("feedbackVisibleCBLabel"));
		add(feedbackVisibleCB);
	}

	public Hashtable getEditState() {
		Hashtable h = ivmDrawPanel.getEditState();
		h.put("vaasNummer", new Integer(vaasKeuze.getSelectedIndex()+1));
		h.put("feedbackVisible", new Boolean(feedbackVisibleCB.isSelected()));
		return h;
		
	}

	public void setEditState(Hashtable h) {
		int vaasNummer = 1;
		boolean feedbackVisible = false;
		if(h.containsKey("vaasNummer")) vaasNummer = ((Integer)h.get("vaasNummer")).intValue();
		if(h.containsKey("feedbackVisible")) feedbackVisible = ((Boolean)h.get("feedbackVisible")).booleanValue();
	
		vaasKeuze.setSelectedIndex(vaasNummer-1);
		feedbackVisibleCB.setSelected(feedbackVisible);
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
