package fi.ivmdraw;

import java.util.HashMap;
import java.util.Hashtable;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fi.beans.wiskopdrbeans.InteractieEditPanel;

public class IVMdrawEditPanel extends JPanel implements InteractieEditPanel {

	private IVMdrawPanel ivmDrawPanel;
	
	private JComboBox vaasKeuze;
	private JCheckBox feedbackVisibleCB;
	private JCheckBox historyVisibleCB;
	private JTextField scoreTF;
	private JLabel scoreLabel;
	
	public IVMdrawEditPanel(IVMdrawPanel ivmDrawPanel) {
		this.ivmDrawPanel = ivmDrawPanel;
//		JLabel label = new JLabel("Dit is het interactieEditpanel");
//		add(label);
		    
		vaasKeuze = new JComboBox();
		vaasKeuze.addItem(IVMdraw.rb.getString("geenVaas"));
		vaasKeuze.addItem(IVMdraw.rb.getString("vaas") + " 1");
		vaasKeuze.addItem(IVMdraw.rb.getString("vaas") + " 2");
		vaasKeuze.addItem(IVMdraw.rb.getString("vaas") + " 3");
		vaasKeuze.addItem(IVMdraw.rb.getString("vaas") + " 4");
		vaasKeuze.addItem(IVMdraw.rb.getString("vaas") + " 5");
		add(vaasKeuze);
		
		feedbackVisibleCB = new JCheckBox(IVMdraw.rb.getString("feedbackVisibleCBLabel"));
		add(feedbackVisibleCB);
		
		historyVisibleCB = new JCheckBox(IVMdraw.rb.getString("historyVisibleCBLabel"));
		add(historyVisibleCB);
		
		scoreLabel = new JLabel("Score");
		add(scoreLabel);
		
		scoreTF = new JTextField("0");
		add(scoreTF);
		
		
	}

	public Hashtable getEditState() {
		Hashtable h = ivmDrawPanel.getEditState();
		h.put("vaasNummer", new Integer(vaasKeuze.getSelectedIndex()));
		h.put("feedbackVisible", new Boolean(feedbackVisibleCB.isSelected()));
		h.put("historyVisible", new Boolean(historyVisibleCB.isSelected()));
		return h;
		
	}

	public void setEditState(Hashtable h) {
		int vaasNummer = 1;
		boolean feedbackVisible = false;
		boolean historyVisible = false;
		if(h.containsKey("vaasNummer")) vaasNummer = ((Integer)h.get("vaasNummer")).intValue();
		if(h.containsKey("feedbackVisible")) feedbackVisible = ((Boolean)h.get("feedbackVisible")).booleanValue();
		if(h.containsKey("historyVisible")) historyVisible = ((Boolean)h.get("historyVisible")).booleanValue();
		
		vaasKeuze.setSelectedIndex(vaasNummer);
		feedbackVisibleCB.setSelected(feedbackVisible);
		historyVisibleCB.setSelected(historyVisible);
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
