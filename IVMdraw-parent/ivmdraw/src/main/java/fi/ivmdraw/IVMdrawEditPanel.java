package fi.ivmdraw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;

import fi.beans.numworxlf.Constants;
import fi.beans.numworxlf.JCheckBox;
import fi.beans.numworxlf.JComboBox;
import fi.beans.numworxlf.JRadioButton;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import fi.beans.numworxlf.JTextField;
import javax.swing.SwingUtilities;

import fi.beans.wiskopdrbeans.InteractieEditPanel;
//import fi.wiskopdr.tekstobjects.EditInteractiePanelDialog;

public class IVMdrawEditPanel extends JPanel implements InteractieEditPanel, ActionListener {

	private IVMdrawPanel ivmDrawPanel;
	
	private JComboBox vaasKeuze;
	private JLabel opdrachtLabel;
	private JLabel vaasOpdrachtLabel;
	private JLabel vaas1Label, vaas2Label, vaas3Label, vaas4Label, vaas5Label;
	private JCheckBox jarFeedbackVisibleCB;
	private JCheckBox feedbackVisibleCB;
	private JCheckBox goedFoutVisibleCB;
	private JCheckBox checkCB;
	private JCheckBox historyVisibleCB;
	private JLabel scoreLabel;
	private JLabel scoreLabel0;
	private JLabel scoreLabel1;
	private JLabel scoreLabel2;
	private JTextField scoreTF;
	private JTextField scoreTF1;
	private JTextField scoreTF2;
	private JLabel feedbackLabel;
	private Box hbasis;
	private Box feedbackBox;
	private JTextArea feedbackTA1,feedbackTA2a,feedbackTA2b,feedbackTA3a,feedbackTA3b,feedbackTA4a,feedbackTA4b,feedbackTA5,feedbackTA6,feedbackTA7;
	private JLabel graphCase1Label, graphCase2aLabel, graphCase2bLabel, graphCase3aLabel, graphCase3bLabel, graphCase4aLabel, graphCase4bLabel,graphCase5Label, graphCase6Label;
	private JRadioButton rbCase1,rbCase2a,rbCase2b,rbCase3a,rbCase3b,rbCase4a,rbCase4b,rbCase5,rbCase6,rbCase7;
	private JLabel goedImageLabel, foutImageLabel;
	
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
		
		graphCase1Label = new JLabel(maakImageIcon("resources/graphCase1.png"));
		graphCase2aLabel = new JLabel(maakImageIcon("resources/graphCase2a.png"));
		graphCase2bLabel = new JLabel(maakImageIcon("resources/graphCase2b.png"));
		graphCase3aLabel = new JLabel(maakImageIcon("resources/graphCase3a.png"));
		graphCase3bLabel = new JLabel(maakImageIcon("resources/graphCase3b.png"));
		graphCase4aLabel = new JLabel(maakImageIcon("resources/graphCase4a.png"));
		graphCase4bLabel = new JLabel(maakImageIcon("resources/graphCase4b.png"));
		graphCase5Label = new JLabel(maakImageIcon("resources/graphCase5.png"));
		graphCase6Label = new JLabel(maakImageIcon("resources/graphCase6.png"));
		
		feedbackTA1 =  makeTextArea(IVMdraw.rb.getString("defaultFeedbackCase1"));
		feedbackTA2a = makeTextArea(IVMdraw.rb.getString("defaultFeedbackCase2a"));
		feedbackTA2b = makeTextArea(IVMdraw.rb.getString("defaultFeedbackCase2b"));
		feedbackTA3a = makeTextArea(IVMdraw.rb.getString("defaultFeedbackCase3a"));
		feedbackTA3b = makeTextArea(IVMdraw.rb.getString("defaultFeedbackCase3b"));
		feedbackTA4a = makeTextArea(IVMdraw.rb.getString("defaultFeedbackCase4a"));
		feedbackTA4b = makeTextArea(IVMdraw.rb.getString("defaultFeedbackCase4b"));
		feedbackTA5 = makeTextArea(IVMdraw.rb.getString("defaultFeedbackCase5"));
		feedbackTA6 = makeTextArea(IVMdraw.rb.getString("defaultFeedbackCase6"));
		feedbackTA7 = makeTextArea(IVMdraw.rb.getString("defaultFeedbackCase7"));
		
		
		rbCase1 = new JRadioButton();
		rbCase2a = new JRadioButton();
		rbCase2b = new JRadioButton();
		rbCase3a = new JRadioButton();
		rbCase3b = new JRadioButton();
		rbCase4a = new JRadioButton();
		rbCase4b = new JRadioButton();
		rbCase5 = new JRadioButton();
		rbCase6 = new JRadioButton();
		rbCase7 = new JRadioButton();
		
		goedImageLabel = new JLabel(maakImageIcon("resources/goedvink-new.png"));
		foutImageLabel = new JLabel(maakImageIcon("resources/foutkruis-new.png"));
		
		
		
		vaasKeuze.setPreferredSize(new Dimension(300,22));
		vaasKeuze.setMaximumSize(new Dimension(520,26));
		vaasKeuze.setForeground(Constants.COLOR15);
		
		jarFeedbackVisibleCB = new JCheckBox(IVMdraw.rb.getString("jarFeedbackVisibleCBLabel"));
		jarFeedbackVisibleCB.setSelected(true);
		historyVisibleCB = new JCheckBox(IVMdraw.rb.getString("historyVisibleCBLabel"));
		checkCB = new JCheckBox(IVMdraw.rb.getString("checkCBLabel"));
		checkCB.addActionListener(this);
		feedbackVisibleCB = new JCheckBox(IVMdraw.rb.getString("feedbackVisibleCBLabel"));
		feedbackVisibleCB.addActionListener(this);
		feedbackVisibleCB.setVisible(false);
		goedFoutVisibleCB = new JCheckBox(IVMdraw.rb.getString("goedFoutVisibleCBLabel"));
		goedFoutVisibleCB.setSelected(true);
		goedFoutVisibleCB.addActionListener(this);
		goedFoutVisibleCB.setVisible(false);
		
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
		
		feedbackLabel = new JLabel(IVMdraw.rb.getString("feedbackLabel"));
		feedbackLabel.setFont(titelFont);
		feedbackLabel.setForeground(Constants.COLOR15);
		
		vaas1Label.setVisible(true);
		vaas2Label.setVisible(false);
		vaas3Label.setVisible(false);
		vaas4Label.setVisible(false);
		vaas5Label.setVisible(false);
		
		hbasis = Box.createHorizontalBox();
		
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
		hb.add(goedFoutVisibleCB);
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
		vb.add(Box.createRigidArea(new Dimension(0,40)));
		vb.add(vaas1Label);
		vb.add(vaas2Label);
		vb.add(vaas3Label);
		vb.add(vaas4Label);
		vb.add(vaas5Label);
		vb.add(Box.createVerticalGlue());
		
		hbasis.add(vb);
		hbasis.add(Box.createRigidArea(new Dimension(40,0)));
		
		feedbackBox = Box.createVerticalBox();
		fillFeedbackBox(feedbackBox);
		feedbackBox.setVisible(false);
		hbasis.add(feedbackBox);
		
		add(hbasis);
		
	}
	
	private JTextArea makeTextArea(String s) {
		JTextArea ta = new JTextArea(s);
		ta.setPreferredSize(new Dimension(250,50));
		ta.setBorder(BorderFactory.createLineBorder(new Color(120,150,202)));
		ta.setForeground(new Color(49,71,112));
		ta.setLineWrap(true);
		ta.setWrapStyleWord(true);
		return ta;
	}
	
	public ImageIcon maakImageIcon(String s)	{
		URL imageURL = IVMdraw.class.getResource(s);
		ImageIcon imageIcon = new ImageIcon();
		if (imageURL != null) 
			imageIcon = new ImageIcon(imageURL);
		else
			System.out.println("Error reading " + s);
		return imageIcon;
	}
	
	public JLabel geefGoedFoutImage(String feedbackCase) {
		if(!goedFoutVisibleCB.isSelected())
			return new JLabel("");
		if("1".equals(feedbackCase) && vaasKeuze.getSelectedIndex()==3) 
			return new JLabel(maakImageIcon("resources/goedvink-new.png"));
		if("2a".equals(feedbackCase) && vaasKeuze.getSelectedIndex()==5) 
			return new JLabel(maakImageIcon("resources/goedvink-new.png"));
		if("2b".equals(feedbackCase) && vaasKeuze.getSelectedIndex()==2) 
			return new JLabel(maakImageIcon("resources/goedvink-new.png"));
		if("3a".equals(feedbackCase) && vaasKeuze.getSelectedIndex()==4) 
			return new JLabel(maakImageIcon("resources/goedvink-new.png"));
		if("3b".equals(feedbackCase) && vaasKeuze.getSelectedIndex()==1) 
			return new JLabel(maakImageIcon("resources/goedvink-new.png"));
		return new JLabel(maakImageIcon("resources/foutkruis-new.png"));
	}
	
	public boolean geefGoedFout(String feedbackCase) {
		
		if("1".equals(feedbackCase) && vaasKeuze.getSelectedIndex()==3) 
			return true;
		if("2a".equals(feedbackCase) && vaasKeuze.getSelectedIndex()==5) 
			return true;
		if("2b".equals(feedbackCase) && vaasKeuze.getSelectedIndex()==2) 
			return true;
		if("3a".equals(feedbackCase) && vaasKeuze.getSelectedIndex()==4) 
			return true;
		if("3b".equals(feedbackCase) && vaasKeuze.getSelectedIndex()==1) 
			return true;
		return false;
	}

	public Hashtable getEditState() {
		Hashtable h = ivmDrawPanel.getEditState();
		h.put("vaasNummer", new Integer(vaasKeuze.getSelectedIndex()));
		h.put("jarFeedbackVisible", new Boolean(jarFeedbackVisibleCB.isSelected()));
		h.put("feedbackVisible", new Boolean(feedbackVisibleCB.isSelected()));
		h.put("goedFoutVisible", new Boolean(goedFoutVisibleCB.isSelected()));
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
		h.put("feedbackCase1", feedbackTA1.getText());
		h.put("feedbackCase2a", feedbackTA2a.getText());
		h.put("feedbackCase2b", feedbackTA2b.getText());
		h.put("feedbackCase3a", feedbackTA3a.getText());
		h.put("feedbackCase3b", feedbackTA3b.getText());
		h.put("feedbackCase4a", feedbackTA4a.getText());
		h.put("feedbackCase4b", feedbackTA4b.getText());
		h.put("feedbackCase5", feedbackTA5.getText());
		h.put("feedbackCase6", feedbackTA6.getText());
		h.put("feedbackCase7", feedbackTA7.getText());
		
		return h;
	}
	
	public void fillFeedbackBox(Box feedbackBox) {
		feedbackBox.removeAll();
		
		Box hb = Box.createHorizontalBox();
		hb.add(feedbackLabel);
		hb.add(Box.createHorizontalGlue());
		feedbackBox.add(hb);
		feedbackBox.add(Box.createRigidArea(new Dimension(0,10)));
		
		hb = Box.createHorizontalBox();
		hb.add(geefGoedFoutImage("1"));
		hb.add(Box.createRigidArea(new Dimension(10,0)));
		if(geefGoedFout("1"))graphCase1Label.setBorder(BorderFactory.createLineBorder(new Color(120,150,202)));
		else graphCase1Label.setBorder(BorderFactory.createEmptyBorder());
		hb.add(graphCase1Label);
		hb.add(Box.createRigidArea(new Dimension(10,0)));
		hb.add(feedbackTA1);
		feedbackBox.add(hb);
		feedbackBox.add(Box.createRigidArea(new Dimension(0,5)));
		
		hb = Box.createHorizontalBox();
		hb.add(geefGoedFoutImage("2a"));
		hb.add(Box.createRigidArea(new Dimension(10,0)));
		if(geefGoedFout("2a"))graphCase2aLabel.setBorder(BorderFactory.createLineBorder(new Color(120,150,202)));
		else graphCase2aLabel.setBorder(BorderFactory.createEmptyBorder());
		hb.add(graphCase2aLabel);
		hb.add(Box.createRigidArea(new Dimension(10,0)));
		hb.add(feedbackTA2a);
		feedbackBox.add(hb);
		feedbackBox.add(Box.createRigidArea(new Dimension(0,5)));
		
		hb = Box.createHorizontalBox();
		hb.add(geefGoedFoutImage("2b"));
		hb.add(Box.createRigidArea(new Dimension(10,0)));
		if(geefGoedFout("2b"))graphCase2bLabel.setBorder(BorderFactory.createLineBorder(new Color(120,150,202)));
		else graphCase2bLabel.setBorder(BorderFactory.createEmptyBorder());
		hb.add(graphCase2bLabel);
		hb.add(Box.createRigidArea(new Dimension(10,0)));
		hb.add(feedbackTA2b);
		feedbackBox.add(hb);
		feedbackBox.add(Box.createRigidArea(new Dimension(0,5)));
		
		hb = Box.createHorizontalBox();
		hb.add(geefGoedFoutImage("3a"));
		hb.add(Box.createRigidArea(new Dimension(10,0)));
		if(geefGoedFout("3a"))graphCase3aLabel.setBorder(BorderFactory.createLineBorder(new Color(120,150,202)));
		else graphCase3aLabel.setBorder(BorderFactory.createEmptyBorder());
		hb.add(graphCase3aLabel);
		hb.add(Box.createRigidArea(new Dimension(10,0)));
		hb.add(feedbackTA3a);
		feedbackBox.add(hb);
		feedbackBox.add(Box.createRigidArea(new Dimension(0,5)));
		
		hb = Box.createHorizontalBox();
		hb.add(geefGoedFoutImage("3b"));
		hb.add(Box.createRigidArea(new Dimension(10,0)));
		if(geefGoedFout("3b"))graphCase3bLabel.setBorder(BorderFactory.createLineBorder(new Color(120,150,202)));
		else graphCase3bLabel.setBorder(BorderFactory.createEmptyBorder());
		hb.add(graphCase3bLabel);
		hb.add(Box.createRigidArea(new Dimension(10,0)));
		hb.add(feedbackTA3b);
		feedbackBox.add(hb);
		feedbackBox.add(Box.createRigidArea(new Dimension(0,5)));
		
		hb = Box.createHorizontalBox();
		hb.add(geefGoedFoutImage("4a"));
		hb.add(Box.createRigidArea(new Dimension(10,0)));
		if(geefGoedFout("4a"))graphCase4aLabel.setBorder(BorderFactory.createLineBorder(new Color(120,150,202)));
		else graphCase4aLabel.setBorder(BorderFactory.createEmptyBorder());
		hb.add(graphCase4aLabel);
		hb.add(Box.createRigidArea(new Dimension(10,0)));
		hb.add(feedbackTA4a);
		feedbackBox.add(hb);
		feedbackBox.add(Box.createRigidArea(new Dimension(0,5)));
		
		hb = Box.createHorizontalBox();
		hb.add(geefGoedFoutImage("4b"));
		hb.add(Box.createRigidArea(new Dimension(10,0)));
		if(geefGoedFout("4b"))graphCase4bLabel.setBorder(BorderFactory.createLineBorder(new Color(120,150,202)));
		else graphCase4bLabel.setBorder(BorderFactory.createEmptyBorder());
		hb.add(graphCase4bLabel);
		hb.add(Box.createRigidArea(new Dimension(10,0)));
		hb.add(feedbackTA4b);
		feedbackBox.add(hb);
		feedbackBox.add(Box.createRigidArea(new Dimension(0,5)));
		
		hb = Box.createHorizontalBox();
		hb.add(geefGoedFoutImage("5"));
		hb.add(Box.createRigidArea(new Dimension(10,0)));
		hb.add(graphCase5Label);
		hb.add(Box.createRigidArea(new Dimension(10,0)));
		hb.add(feedbackTA5);
		feedbackBox.add(hb);
		feedbackBox.add(Box.createRigidArea(new Dimension(0,5)));
		
		hb = Box.createHorizontalBox();
		hb.add(geefGoedFoutImage("6"));
		hb.add(Box.createRigidArea(new Dimension(10,0)));
		hb.add(graphCase6Label);
		hb.add(Box.createRigidArea(new Dimension(10,0)));
		hb.add(feedbackTA6);
		feedbackBox.add(hb);
		feedbackBox.add(Box.createRigidArea(new Dimension(0,5)));
		
//		hb = Box.createHorizontalBox();
//		hb.add(geefGoedFoutImage("7"));
//		hb.add(Box.createRigidArea(new Dimension(60,0)));
//		hb.add(feedbackTA7);
//		feedbackBox.add(hb);
//		feedbackBox.add(Box.createRigidArea(new Dimension(0,5)));
		
		
	}

	public void setEditState(Hashtable h) {
		int vaasNummer = 1;
		boolean jarFeedbackVisible = true;
		boolean feedbackVisible = false;
		boolean goedFoutVisible = true;
		boolean historyVisible = false;
		boolean check = false;
		String feedbackCase1 = IVMdraw.rb.getString("defaultFeedbackCase1");
		String feedbackCase2a = IVMdraw.rb.getString("defaultFeedbackCase2a");
		String feedbackCase2b = IVMdraw.rb.getString("defaultFeedbackCase2b");
		String feedbackCase3a = IVMdraw.rb.getString("defaultFeedbackCase3a");
		String feedbackCase3b = IVMdraw.rb.getString("defaultFeedbackCase3b");
		String feedbackCase4a = IVMdraw.rb.getString("defaultFeedbackCase4a");
		String feedbackCase4b = IVMdraw.rb.getString("defaultFeedbackCase4b");
		String feedbackCase5 = IVMdraw.rb.getString("defaultFeedbackCase5");
		String feedbackCase6 = IVMdraw.rb.getString("defaultFeedbackCase6");
		String feedbackCase7 = IVMdraw.rb.getString("defaultFeedbackCase7");
		
		int scoreMax = 0;
		int deelScore1 = 0;
		int deelScore2 = 0;
		if(h.containsKey("vaasNummer")) vaasNummer = ((Integer)h.get("vaasNummer")).intValue();
		if(h.containsKey("jarFeedbackVisible")) jarFeedbackVisible = ((Boolean)h.get("jarFeedbackVisible")).booleanValue();
		if(h.containsKey("feedbackVisible")) feedbackVisible = ((Boolean)h.get("feedbackVisible")).booleanValue();
		if(h.containsKey("goedFoutVisible")) goedFoutVisible = ((Boolean)h.get("goedFoutVisible")).booleanValue();
		if(h.containsKey("historyVisible")) historyVisible = ((Boolean)h.get("historyVisible")).booleanValue();
		if(h.containsKey("scoreMax")) scoreMax = ((Integer)h.get("scoreMax")).intValue();
		if(h.containsKey("deelScore1")) deelScore1 = ((Integer)h.get("deelScore1")).intValue();
		if(h.containsKey("deelScore2")) deelScore2 = ((Integer)h.get("deelScore2")).intValue();
		if(h.containsKey("check")) check = ((Boolean)h.get("check")).booleanValue();
		if(h.containsKey("feedbackCase1")) feedbackCase1 = (String)h.get("feedbackCase1");
		if(h.containsKey("feedbackCase2a")) feedbackCase2a = (String)h.get("feedbackCase2a");
		if(h.containsKey("feedbackCase2b")) feedbackCase2b = (String)h.get("feedbackCase2b");
		if(h.containsKey("feedbackCase3a")) feedbackCase3a = (String)h.get("feedbackCase3a");
		if(h.containsKey("feedbackCase3b")) feedbackCase3b = (String)h.get("feedbackCase3b");
		if(h.containsKey("feedbackCase4a")) feedbackCase4a = (String)h.get("feedbackCase4a");
		if(h.containsKey("feedbackCase4b")) feedbackCase4b = (String)h.get("feedbackCase4b");
		if(h.containsKey("feedbackCase5")) feedbackCase5 = (String)h.get("feedbackCase5");
		if(h.containsKey("feedbackCase6")) feedbackCase6 = (String)h.get("feedbackCase6");
		if(h.containsKey("feedbackCase7")) feedbackCase7 = (String)h.get("feedbackCase7");
		
		check = check || feedbackVisible; // Voor backwards Compatibiliteit
		
		vaasKeuze.setSelectedIndex(vaasNummer);
		jarFeedbackVisibleCB.setSelected(jarFeedbackVisible);
		feedbackVisibleCB.setSelected(feedbackVisible);
		goedFoutVisibleCB.setVisible(feedbackVisible);
		goedFoutVisibleCB.setSelected(goedFoutVisible);
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
		
		fillFeedbackBox(feedbackBox);
		feedbackBox.setVisible(feedbackVisibleCB.isSelected());
		
		feedbackTA1.setText(feedbackCase1);
		feedbackTA2a.setText(feedbackCase2a);
		feedbackTA2b.setText(feedbackCase2b);
		feedbackTA3a.setText(feedbackCase3a);
		feedbackTA3b.setText(feedbackCase3b);
		feedbackTA4a.setText(feedbackCase4a);
		feedbackTA4b.setText(feedbackCase4b);
		feedbackTA5.setText(feedbackCase5);
		feedbackTA6.setText(feedbackCase6);
		feedbackTA7.setText(feedbackCase7);
		
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
			fillFeedbackBox(feedbackBox);
			feedbackBox.setVisible(feedbackVisibleCB.isSelected());
			//hbasis.validate();
			
			((JDialog)SwingUtilities.getAncestorOfClass(JDialog.class,(Component)this)).pack();
		}
		if(e.getSource()==feedbackVisibleCB) {
			goedFoutVisibleCB.setVisible(feedbackVisibleCB.isSelected());
			feedbackBox.setVisible(feedbackVisibleCB.isSelected());
			//hbasis.validate();
			((JDialog)SwingUtilities.getAncestorOfClass(JDialog.class,(Component)this)).pack();
		}
		if(e.getSource()==goedFoutVisibleCB) {
			fillFeedbackBox(feedbackBox);
			feedbackBox.setVisible(feedbackVisibleCB.isSelected());
			//hbasis.validate();
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
