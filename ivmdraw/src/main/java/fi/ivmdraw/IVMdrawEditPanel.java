package fi.ivmdraw;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.wiskopdr.tekstobjects.EditInteractiePanelDialog;

public class IVMdrawEditPanel extends JPanel implements InteractieEditPanel, ActionListener {

	private IVMdrawPanel ivmDrawPanel;
	
	private JComboBox vaasKeuze;
	private JLabel vaasOpdrachtLabel;
	private JLabel vaas1Label, vaas2Label, vaas3Label, vaas4Label, vaas5Label;
	private JCheckBox feedbackVisibleCB;
	private JCheckBox historyVisibleCB;
	private JTextField scoreTF;
	private JLabel scoreLabel;
	
	public IVMdrawEditPanel(IVMdrawPanel ivmDrawPanel) {
		this.ivmDrawPanel = ivmDrawPanel;

		setLayout(new BorderLayout());
		    
		vaasKeuze = new JComboBox();
		vaasKeuze.addItem(IVMdraw.rb.getString("geenVaas"));
		vaasKeuze.addItem(IVMdraw.rb.getString("vaas") + " 1");
		vaasKeuze.addItem(IVMdraw.rb.getString("vaas") + " 2");
		vaasKeuze.addItem(IVMdraw.rb.getString("vaas") + " 3");
		vaasKeuze.addItem(IVMdraw.rb.getString("vaas") + " 4");
		vaasKeuze.addItem(IVMdraw.rb.getString("vaas") + " 5");
		vaasKeuze.addActionListener(this);
		
		vaasOpdrachtLabel = new JLabel(IVMdraw.rb.getString("vaasOpdrachtLabel"));
		
		vaas1Label = new JLabel(maakImageIcon("resources/vaas1.jpg"));
		vaas2Label = new JLabel(maakImageIcon("resources/vaas2.jpg"));
		vaas3Label = new JLabel(maakImageIcon("resources/vaas3.jpg"));
		vaas4Label = new JLabel(maakImageIcon("resources/vaas4.jpg"));
		vaas5Label = new JLabel(maakImageIcon("resources/vaas5.jpg"));
		
		vaasKeuze.setPreferredSize(new Dimension(120,20));
		vaasKeuze.setMaximumSize(new Dimension(120,26));
		feedbackVisibleCB = new JCheckBox(IVMdraw.rb.getString("feedbackVisibleCBLabel"));
		historyVisibleCB = new JCheckBox(IVMdraw.rb.getString("historyVisibleCBLabel"));
		scoreLabel = new JLabel(IVMdraw.rb.getString("scoreLabel"));
		scoreTF = new JTextField("0");
		scoreTF.setPreferredSize(new Dimension(50,20));
		scoreTF.setMaximumSize(new Dimension(50,20));
		
		vaas1Label.setVisible(false);
		vaas2Label.setVisible(false);
		vaas3Label.setVisible(false);
		vaas4Label.setVisible(false);
		vaas5Label.setVisible(false);
		
		Box hbasis = Box.createHorizontalBox();
		
		Box vb = Box.createVerticalBox();
		
		
		
		Box hb = Box.createHorizontalBox();
		hb.add(vaasOpdrachtLabel);
		hb.add(Box.createRigidArea(new Dimension(10,0)));
		hb.add(vaasKeuze);
		hb.add(Box.createHorizontalGlue());
		vb.add(hb);
		vb.add(Box.createRigidArea(new Dimension(0,5)));
		
		hb = Box.createHorizontalBox();
		hb.add(feedbackVisibleCB);
		hb.add(Box.createHorizontalGlue());
		vb.add(hb);
		vb.add(Box.createRigidArea(new Dimension(0,5)));
		
		hb = Box.createHorizontalBox();
		hb.add(historyVisibleCB);
		hb.add(Box.createHorizontalGlue());
		vb.add(hb);
		vb.add(Box.createRigidArea(new Dimension(0,5)));
		
		hb = Box.createHorizontalBox();
		hb.add(scoreLabel);
		hb.add(Box.createRigidArea(new Dimension(10,0)));
		hb.add(scoreTF);
		hb.add(Box.createHorizontalGlue());
		vb.add(hb);
		vb.add(Box.createRigidArea(new Dimension(0,5)));
		
		vb.add(Box.createVerticalGlue());
		
		hbasis.add(vb);
		
		vb = Box.createVerticalBox();
		vb.add(vaas1Label);
		vb.add(vaas2Label);
		vb.add(vaas3Label);
		vb.add(vaas4Label);
		vb.add(vaas5Label);
		hbasis.add(vb);
		
		add(hbasis);
		
	}
	
	public ImageIcon maakImageIcon(String s)
	{
		URL imageURL = IVMdraw.class.getResource(s);
		ImageIcon imageIcon = new ImageIcon();
		if (imageURL != null) 
			imageIcon = new ImageIcon(imageURL);
		else
			System.out.println("Error reading " + s);
		return imageIcon;
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
		
		if(vaasKeuze.getSelectedIndex()==1)
			vaas1Label.setVisible(true);
		if(vaasKeuze.getSelectedIndex()==2)
			vaas2Label.setVisible(true);
		if(vaasKeuze.getSelectedIndex()==3)
			vaas3Label.setVisible(true);
		if(vaasKeuze.getSelectedIndex()==4)
			vaas4Label.setVisible(true);
		if(vaasKeuze.getSelectedIndex()==5)
			vaas5Label.setVisible(true);
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

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==vaasKeuze) {
			vaas1Label.setVisible(false);
			vaas2Label.setVisible(false);
			vaas3Label.setVisible(false);
			vaas4Label.setVisible(false);
			vaas5Label.setVisible(false);
			
			if(vaasKeuze.getSelectedIndex()==1)
				vaas1Label.setVisible(true);
			if(vaasKeuze.getSelectedIndex()==2)
				vaas2Label.setVisible(true);
			if(vaasKeuze.getSelectedIndex()==3)
				vaas3Label.setVisible(true);
			if(vaasKeuze.getSelectedIndex()==4)
				vaas4Label.setVisible(true);
			if(vaasKeuze.getSelectedIndex()==5)
				vaas5Label.setVisible(true);
			
			((EditInteractiePanelDialog)SwingUtilities.getAncestorOfClass(JDialog.class,(Component)this)).pack();
		}
		
	}

}
