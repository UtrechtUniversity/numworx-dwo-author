package fi.ivmdraw;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;

import javax.swing.Box;
import javax.swing.ImageIcon;

import fi.beans.numworxlf.Constants;
import fi.beans.numworxlf.JCheckBox;
import fi.beans.numworxlf.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import fi.beans.numworxlf.JTextField;
import javax.swing.SwingUtilities;

import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.wiskopdr.tekstobjects.EditInteractiePanelDialog;

public class IVMdrawEditPanel extends JPanel implements InteractieEditPanel, ActionListener {

	private IVMdrawPanel ivmDrawPanel;
	
	private JComboBox vaasKeuze;
	private JLabel opdrachtLabel;
	private JLabel vaasOpdrachtLabel;
	private JLabel vaas1Label, vaas2Label, vaas3Label, vaas4Label, vaas5Label;
	private JCheckBox jarFeedbackVisibleCB;
	private JCheckBox feedbackVisibleCB;
	private JCheckBox checkCB;
	private JCheckBox historyVisibleCB;
	private JLabel scoreLabel;
	private JLabel scoreLabel0;
	private JLabel scoreLabel1;
	private JLabel scoreLabel2;
	private JTextField scoreTF;
	private JTextField scoreTF1;
	private JTextField scoreTF2;
	
	private Font titelFont = new Font("SansSerif", Font.BOLD, 14);
	
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
		vaasKeuze.setSelectedIndex(1);
		vaasKeuze.addActionListener(this);
		
		opdrachtLabel = new JLabel(IVMdraw.rb.getString("opdrachtLabel"));
		opdrachtLabel.setFont(titelFont);
		opdrachtLabel.setForeground(Constants.COLOR15);
		
		vaasOpdrachtLabel = new JLabel(IVMdraw.rb.getString("vaasOpdrachtLabel"));
		vaasOpdrachtLabel.setForeground(Constants.COLOR15);
		
		vaas1Label = new JLabel(maakImageIcon("resources/vaas1.jpg"));
		vaas2Label = new JLabel(maakImageIcon("resources/vaas2.jpg"));
		vaas3Label = new JLabel(maakImageIcon("resources/vaas3.jpg"));
		vaas4Label = new JLabel(maakImageIcon("resources/vaas4.jpg"));
		vaas5Label = new JLabel(maakImageIcon("resources/vaas5.jpg"));
		
		vaasKeuze.setPreferredSize(new Dimension(300,22));
		vaasKeuze.setMaximumSize(new Dimension(520,26));
		vaasKeuze.setForeground(Constants.COLOR15);
		
		jarFeedbackVisibleCB = new JCheckBox(IVMdraw.rb.getString("jarFeedbackVisibleCBLabel"));
		jarFeedbackVisibleCB.setSelected(true);
		historyVisibleCB = new JCheckBox(IVMdraw.rb.getString("historyVisibleCBLabel"));
		checkCB = new JCheckBox(IVMdraw.rb.getString("checkCBLabel"));
		checkCB.addActionListener(this);
		feedbackVisibleCB = new JCheckBox(IVMdraw.rb.getString("feedbackVisibleCBLabel"));
		feedbackVisibleCB.setVisible(false);
		
		scoreLabel = new JLabel(IVMdraw.rb.getString("scoreLabel"));
		scoreLabel.setFont(titelFont);
		scoreLabel.setForeground(Constants.COLOR15);
		scoreLabel.setVisible(false);
		
		scoreLabel0 = new JLabel(IVMdraw.rb.getString("scoreLabel0"));
		scoreLabel0.setForeground(Constants.COLOR15);
		scoreLabel0.setVisible(false);
		
		scoreLabel1 = new JLabel(IVMdraw.rb.getString("scoreLabel1"));
		scoreLabel1.setForeground(Constants.COLOR15);
		scoreLabel1.setVisible(false);
		
		scoreLabel2 = new JLabel(IVMdraw.rb.getString("scoreLabel2"));
		scoreLabel2.setForeground(Constants.COLOR15);
		scoreLabel2.setVisible(false);
		
		scoreTF = new JTextField("3");
		scoreTF.setPreferredSize(new Dimension(50,20));
		scoreTF.setMaximumSize(new Dimension(50,20));
		scoreTF.setVisible(false);
		
		scoreTF1 = new JTextField("2");
		scoreTF1.setPreferredSize(new Dimension(50,20));
		scoreTF1.setMaximumSize(new Dimension(50,20));
		scoreTF1.setVisible(false);
		
		scoreTF2 = new JTextField("1");
		scoreTF2.setPreferredSize(new Dimension(50,20));
		scoreTF2.setMaximumSize(new Dimension(50,20));
		scoreTF2.setVisible(false);
		
		vaas1Label.setVisible(true);
		vaas2Label.setVisible(false);
		vaas3Label.setVisible(false);
		vaas4Label.setVisible(false);
		vaas5Label.setVisible(false);
		
		Box hbasis = Box.createHorizontalBox();
		
		Box vb = Box.createVerticalBox();
		
		Box hb = Box.createHorizontalBox();
		hb.add(opdrachtLabel);
		hb.add(Box.createHorizontalGlue());
		vb.add(hb);
		vb.add(Box.createRigidArea(new Dimension(0,10)));
		
		hb = Box.createHorizontalBox();
		hb.add(vaasKeuze);
		vb.add(hb);
		vb.add(Box.createRigidArea(new Dimension(0,15)));
		
		hb = Box.createHorizontalBox();
		hb.add(jarFeedbackVisibleCB);
		hb.add(Box.createHorizontalGlue());
		vb.add(hb);
		vb.add(Box.createRigidArea(new Dimension(0,5)));
		
		
		
		hb = Box.createHorizontalBox();
		hb.add(historyVisibleCB);
		hb.add(Box.createHorizontalGlue());
		vb.add(hb);
		vb.add(Box.createRigidArea(new Dimension(0,5)));
		
		hb = Box.createHorizontalBox();
		hb.add(checkCB);
		hb.add(Box.createHorizontalGlue());
		vb.add(hb);
		vb.add(Box.createRigidArea(new Dimension(0,5)));
		
		hb = Box.createHorizontalBox();
		hb.add(feedbackVisibleCB);
		hb.add(Box.createHorizontalGlue());
		vb.add(hb);
		vb.add(Box.createRigidArea(new Dimension(0,15)));
		
		hb = Box.createHorizontalBox();
		hb.add(scoreLabel);
		hb.add(Box.createHorizontalGlue());
		vb.add(hb);
		vb.add(Box.createRigidArea(new Dimension(0,10)));
		
		hb = Box.createHorizontalBox();
		hb.add(scoreLabel0);
		hb.add(Box.createRigidArea(new Dimension(10,0)));
		hb.add(Box.createHorizontalGlue());
		hb.add(scoreTF);
		vb.add(hb);
		vb.add(Box.createRigidArea(new Dimension(0,5)));
		
		hb = Box.createHorizontalBox();
		hb.add(scoreLabel1);
		hb.add(Box.createRigidArea(new Dimension(10,0)));
		hb.add(Box.createHorizontalGlue());
		hb.add(scoreTF1);
		vb.add(hb);
		vb.add(Box.createRigidArea(new Dimension(0,5)));
		
		hb = Box.createHorizontalBox();
		hb.add(scoreLabel2);
		hb.add(Box.createRigidArea(new Dimension(10,0)));
		hb.add(Box.createHorizontalGlue());
		hb.add(scoreTF2);
		vb.add(hb);
		vb.add(Box.createRigidArea(new Dimension(0,5)));
		vb.add(Box.createVerticalGlue());
		vb.setMaximumSize(new Dimension(300,500));
		
		hbasis.add(vb);
		hbasis.add(Box.createRigidArea(new Dimension(40,0)));
		
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
		h.put("jarFeedbackVisible", new Boolean(jarFeedbackVisibleCB.isSelected()));
		h.put("feedbackVisible", new Boolean(feedbackVisibleCB.isSelected()));
		h.put("historyVisible", new Boolean(historyVisibleCB.isSelected()));
		h.put("check", new Boolean(checkCB.isSelected() || feedbackVisibleCB.isSelected()));//"feedbackVisibleCB.isSelected()" voor backwards compatibiliteit
		int scoreMax = 0;
		int deelScore1 = 0;
		int deelScore2 = 0;
		if(checkCB.isSelected() && vaasKeuze.getSelectedIndex()!=0) {
			try {
				scoreMax = Integer.parseInt(scoreTF.getText());
			}catch(Exception e) {}
			try {
				deelScore1 = Integer.parseInt(scoreTF1.getText());
			}catch(Exception e) {}
			try {
				deelScore2 = Integer.parseInt(scoreTF2.getText());
			}catch(Exception e) {}
		}
		h.put("scoreMax", new Integer(scoreMax));
		h.put("deelScore1", new Integer(deelScore1));
		h.put("deelScore2", new Integer(deelScore2));
		
		return h;
	}

	public void setEditState(Hashtable h) {
		int vaasNummer = 1;
		boolean jarFeedbackVisible = true;
		boolean feedbackVisible = false;
		boolean historyVisible = false;
		boolean check = false;
		
		int scoreMax = 0;
		int deelScore1 = 0;
		int deelScore2 = 0;
		if(h.containsKey("vaasNummer")) vaasNummer = ((Integer)h.get("vaasNummer")).intValue();
		if(h.containsKey("jarFeedbackVisible")) jarFeedbackVisible = ((Boolean)h.get("jarFeedbackVisible")).booleanValue();
		if(h.containsKey("feedbackVisible")) feedbackVisible = ((Boolean)h.get("feedbackVisible")).booleanValue();
		if(h.containsKey("historyVisible")) historyVisible = ((Boolean)h.get("historyVisible")).booleanValue();
		if(h.containsKey("scoreMax")) scoreMax = ((Integer)h.get("scoreMax")).intValue();
		if(h.containsKey("deelScore1")) deelScore1 = ((Integer)h.get("deelScore1")).intValue();
		if(h.containsKey("deelScore2")) deelScore2 = ((Integer)h.get("deelScore2")).intValue();
		if(h.containsKey("check")) check = ((Boolean)h.get("check")).booleanValue();
		
		check = check || feedbackVisible; // Voor backwards Compatibiliteit
		
		vaasKeuze.setSelectedIndex(vaasNummer);
		jarFeedbackVisibleCB.setSelected(jarFeedbackVisible);
		feedbackVisibleCB.setSelected(feedbackVisible);
		historyVisibleCB.setSelected(historyVisible);
		scoreTF.setText(""+scoreMax);
		scoreTF1.setText(""+deelScore1);
		scoreTF2.setText(""+deelScore2);
		checkCB.setSelected(check);
		
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
		
		boolean u = checkCB.isSelected();
		boolean v = vaasKeuze.getSelectedIndex()!=0;
		jarFeedbackVisibleCB.setVisible(v);
		checkCB.setVisible(v);
		feedbackVisibleCB.setVisible(u && v);
		scoreLabel.setVisible(u && v);
		scoreLabel0.setVisible(u && v);
		scoreLabel1.setVisible(u && v);
		scoreLabel2.setVisible(u && v);
		scoreTF.setVisible(u && v);
		scoreTF1.setVisible(u && v);
		scoreTF2.setVisible(u && v);
		
		((JDialog)SwingUtilities.getAncestorOfClass(JDialog.class,(Component)this)).pack();
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
			
			boolean u = checkCB.isSelected();
			boolean v = vaasKeuze.getSelectedIndex()!=0;
			jarFeedbackVisibleCB.setVisible(v);
			checkCB.setVisible(v);
			feedbackVisibleCB.setVisible(u && v);
			scoreLabel.setVisible(u && v);
			scoreLabel0.setVisible(u && v);
			scoreLabel1.setVisible(u && v);
			scoreLabel2.setVisible(u && v);
			scoreTF.setVisible(u && v);
			scoreTF1.setVisible(u && v);
			scoreTF2.setVisible(u && v);
			
			if(!v) {
				checkCB.setSelected(false);
				feedbackVisibleCB.setSelected(false);
			}
			
			((JDialog)SwingUtilities.getAncestorOfClass(JDialog.class,(Component)this)).pack();
		}
		if(e.getSource()==checkCB) {
			boolean u = checkCB.isSelected();
			boolean v = vaasKeuze.getSelectedIndex()!=0;
			feedbackVisibleCB.setVisible(u && v);
			scoreLabel.setVisible(u && v);
			scoreLabel0.setVisible(u && v);
			scoreLabel1.setVisible(u && v);
			scoreLabel2.setVisible(u && v);
			scoreTF.setVisible(u && v);
			scoreTF1.setVisible(u && v);
			scoreTF2.setVisible(u && v);
			
			if(!u)
				feedbackVisibleCB.setSelected(false);
			
			((JDialog)SwingUtilities.getAncestorOfClass(JDialog.class,(Component)this)).pack();
		}
		
	}

}
