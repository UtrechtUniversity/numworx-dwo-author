package fi.binomverdeling;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fi.beans.wiskopdrbeans.InteractieEditPanel;

public class BVInteractieEditPanel extends JPanel implements InteractieEditPanel, ActionListener {
	private JPanel settingsPanel;
	private BVInteractiePanel interactiePanel;
	private JCheckBox nVeranderbaarBox;
	private JCheckBox pVeranderbaarBox;
	private JCheckBox successenVeranderbaarBox;
	private JLabel veranderbaarLabel;
	private JLabel weergaveLabel;
	private JCheckBox xAsWeergaveBox;
	private JCheckBox yAsWeergaveBox;
	
	
	public BVInteractieEditPanel() {		
		this.setLayout(new GridLayout(0,2));
		this.interactiePanel = new BVInteractiePanel();
		this.add(this.interactiePanel);
		
		this.settingsPanel = new JPanel();
		this.settingsPanel.setLayout(new GridLayout(10,2));
		this.veranderbaarLabel = new JLabel("aanpasbaarheid instellingen");
		this.settingsPanel.add(this.veranderbaarLabel);
		
		this.nVeranderbaarBox = new JCheckBox("n veranderbaar", true);
		this.nVeranderbaarBox.setActionCommand("nboxupdate");
		this.nVeranderbaarBox.addActionListener(this);
		this.settingsPanel.add(this.nVeranderbaarBox);
		
		this.pVeranderbaarBox = new JCheckBox("p veranderbaar", true);
		this.pVeranderbaarBox.setActionCommand("pboxupdate");
		this.pVeranderbaarBox.addActionListener(this);
		this.settingsPanel.add(this.pVeranderbaarBox);
				
		this.successenVeranderbaarBox = new JCheckBox("successen veranderbaar", true);
		this.successenVeranderbaarBox.setActionCommand("successenboxupdate");
		this.successenVeranderbaarBox.addActionListener(this);
		this.settingsPanel.add(this.successenVeranderbaarBox);
		
		this.weergaveLabel = new JLabel("weergave instellingen");
		this.settingsPanel.add(this.weergaveLabel);
		
		this.xAsWeergaveBox = new JCheckBox("geef X-as weer", true);
		this.xAsWeergaveBox.setActionCommand("xasweergaveboxupdate");
		this.xAsWeergaveBox.addActionListener(this);
		this.settingsPanel.add(this.xAsWeergaveBox);
		
		this.yAsWeergaveBox = new JCheckBox("Geef Y-as weer", true);
		this.yAsWeergaveBox.setActionCommand("yasweergaveboxupdate");
		this.yAsWeergaveBox.addActionListener(this);
		this.settingsPanel.add(this.yAsWeergaveBox);
				
		this.add(this.settingsPanel);
	}

	public void setEditState(Hashtable h) {
		if(h.contains("nVeranderbaar")) {
			this.nVeranderbaarBox.setSelected(((Boolean)h.get("nVeranderbaar")).booleanValue());
		}
		if(h.contains("pVeranderbaar")) {
			this.pVeranderbaarBox.setSelected(((Boolean)h.get("pVeranderbaar")).booleanValue());
		}
		if(h.contains("successenVeranderbaar")) {
			this.successenVeranderbaarBox.setSelected(((Boolean)h.get("successenVeranderbaar")).booleanValue());
		}
		if(h.contains("showXAs")) {
			this.xAsWeergaveBox.setSelected(((Boolean)h.get("showXAs")).booleanValue());
		}
		if(h.contains("showYAs")) {
			this.yAsWeergaveBox.setSelected(((Boolean)h.get("showYAs")).booleanValue());
		}
	}

	public Hashtable getEditState() {
		//haal de edit gegevens uit het InteractiePanel
		Hashtable h = this.interactiePanel.getEditState();
		
		//en voeg de editgegevens uit het InteractieEditPanel er aan toe.
		h.put("nVeranderbaar", new Boolean(this.nVeranderbaarBox.isSelected()));
		h.put("pVeranderbaar", new Boolean(this.pVeranderbaarBox.isSelected()));
		h.put("successenVeranderbaar", new Boolean(this.successenVeranderbaarBox.isSelected()));
		h.put("showXAs", new Boolean(this.xAsWeergaveBox.isSelected()));
		h.put("showYAs", new Boolean(this.yAsWeergaveBox.isSelected()));
		
		return h;
	}

	public void setBounds(int x, int y, int b, int h) {
		super.setBounds(x, y, b, h);
	}

	public void zetBreedte(int b) {
	}

	public void zetHoogte(int h) {
	}

	public void wis() {

	}

	public void zetMode(int mode) {

	}

	public void stop() {

	}

	public void start() {

	}

	public void addActionListener(ActionListener al) {

	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("nboxupdate")) {
			this.interactiePanel.setNVeranderbaar(this.nVeranderbaarBox.isSelected()); //zet instelling in interactiePanel om de preview aan te passen
		}
		if(e.getActionCommand().equals("pboxupdate")) {
			this.interactiePanel.setPVeranderbaar(this.pVeranderbaarBox.isSelected()); //zet instelling in interactiePanel om de preview aan te passen
		}
		if(e.getActionCommand().equals("successenboxupdate")) {
			this.interactiePanel.setSuccessenVeranderbaar(this.successenVeranderbaarBox.isSelected()); //zet instelling in interactiePanel om de preview aan te passen
		}
		if(e.getActionCommand().equals("xasweergaveboxupdate")) {
			this.interactiePanel.setShowXAs(this.xAsWeergaveBox.isSelected());
		}
		if(e.getActionCommand().equals("yasweergaveboxupdate")) {
			this.interactiePanel.setShowYAs(this.yAsWeergaveBox.isSelected());
		}
	}
}
