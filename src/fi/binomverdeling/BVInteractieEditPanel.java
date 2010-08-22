package fi.binomverdeling;

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
	private JCheckBox nSliderWeergaveBox;
	private JCheckBox pSliderWeergaveBox;
	private JCheckBox successenSliderWeergaveBox;
	
	/**
	 * Constructor
	 */
	public BVInteractieEditPanel() {		
		this.setLayout(new GridLayout(0,2));
		this.interactiePanel = new BVInteractiePanel();
		this.add(this.interactiePanel);
		
		this.settingsPanel = new JPanel();
		this.settingsPanel.setLayout(new GridLayout(10,2));
		this.veranderbaarLabel = new JLabel("aanpasbaarheid instellingen");
		this.settingsPanel.add(this.veranderbaarLabel);
		
		this.nVeranderbaarBox = new JCheckBox("n veranderbaar", true);
		this.nVeranderbaarBox.addActionListener(this);
		this.settingsPanel.add(this.nVeranderbaarBox);
		
		this.pVeranderbaarBox = new JCheckBox("p veranderbaar", true);
		this.pVeranderbaarBox.addActionListener(this);
		this.settingsPanel.add(this.pVeranderbaarBox);
				
		this.successenVeranderbaarBox = new JCheckBox("successen veranderbaar", true);
		this.successenVeranderbaarBox.addActionListener(this);
		this.settingsPanel.add(this.successenVeranderbaarBox);
		
		this.weergaveLabel = new JLabel("weergave instellingen");
		this.settingsPanel.add(this.weergaveLabel);
		
		this.xAsWeergaveBox = new JCheckBox("geef X-as weer", true);
		this.xAsWeergaveBox.addActionListener(this);
		this.settingsPanel.add(this.xAsWeergaveBox);
		
		this.yAsWeergaveBox = new JCheckBox("Geef Y-as weer", true);
		this.yAsWeergaveBox.addActionListener(this);
		this.settingsPanel.add(this.yAsWeergaveBox);
		
		this.nSliderWeergaveBox = new JCheckBox("Geef slider voor n weer", true);
		this.nSliderWeergaveBox.addActionListener(this);
		this.settingsPanel.add(this.nSliderWeergaveBox);
		
		this.pSliderWeergaveBox = new JCheckBox("Geef slider voor p weer", true);
		this.pSliderWeergaveBox.addActionListener(this);
		this.settingsPanel.add(this.pSliderWeergaveBox);
		
		this.successenSliderWeergaveBox = new JCheckBox("Geef slider voor successen weer", true);
		this.successenSliderWeergaveBox.addActionListener(this);
		this.settingsPanel.add(this.successenSliderWeergaveBox);
		
		Slider slider = new Slider(100,10);
		this.settingsPanel.add(slider);
		this.add(this.settingsPanel);
	}

	/**
	 * Zet de al gemaakte instellingen om deze te kunnen bewerken.
	 */
	public void setEditState(Hashtable h) {
		this.interactiePanel.setEditState(h);
		
		if (h.containsKey("nVeranderbaar")) {
			this.nVeranderbaarBox.setSelected(((Boolean)h.get("nVeranderbaar")).booleanValue());
		}
		if (h.containsKey("pVeranderbaar")) {
			this.pVeranderbaarBox.setSelected(((Boolean)h.get("pVeranderbaar")).booleanValue());
		}
		if (h.containsKey("successenVeranderbaar")) {
			this.successenVeranderbaarBox.setSelected(((Boolean)h.get("successenVeranderbaar")).booleanValue());
		}
		if (h.containsKey("showXAs")) {
			this.xAsWeergaveBox.setSelected(((Boolean)h.get("showXAs")).booleanValue());
		}
		if (h.containsKey("showYAs")) {
			this.yAsWeergaveBox.setSelected(((Boolean)h.get("showYAs")).booleanValue());
		}
		if (h.containsKey("showNSlider")) {
			this.nSliderWeergaveBox.setSelected(((Boolean)h.get("showNSlider")).booleanValue());
		}
		if (h.containsKey("showPSlider")) {
			this.pSliderWeergaveBox.setSelected(((Boolean)h.get("showPSlider")).booleanValue());
		}
		if (h.containsKey("showSuccessenSlider")) {
			this.successenSliderWeergaveBox.setSelected(((Boolean)h.get("showSuccessenSlider")).booleanValue());
		}
	}

	/**
	 * Geef alle instellingen in de vorm van een hashtable
	 */
	public Hashtable getEditState() {
		
		//haal de edit gegevens uit het InteractiePanel
		Hashtable h = this.interactiePanel.getEditState();
		
		//en voeg de editgegevens uit het InteractieEditPanel er aan toe.
		h.put("nVeranderbaar", new Boolean(this.nVeranderbaarBox.isSelected()));
		h.put("pVeranderbaar", new Boolean(this.pVeranderbaarBox.isSelected()));
		h.put("successenVeranderbaar", new Boolean(this.successenVeranderbaarBox.isSelected()));
		h.put("showXAs", new Boolean(this.xAsWeergaveBox.isSelected()));
		h.put("showYAs", new Boolean(this.yAsWeergaveBox.isSelected()));
		h.put("showNSlider", new Boolean(this.nSliderWeergaveBox.isSelected()));
		h.put("showPSlider", new Boolean(this.pSliderWeergaveBox.isSelected()));
		h.put("showSuccessenSlider", new Boolean(this.successenSliderWeergaveBox.isSelected()));
		
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
		if(e.getSource() == this.nVeranderbaarBox) {
			this.interactiePanel.setNVeranderbaar(this.nVeranderbaarBox.isSelected()); //zet instelling in interactiePanel om de preview aan te passen
		}
		if(e.getSource() == this.pVeranderbaarBox) {
			this.interactiePanel.setPVeranderbaar(this.pVeranderbaarBox.isSelected()); //zet instelling in interactiePanel om de preview aan te passen
		}
		if(e.getSource() == this.successenVeranderbaarBox) {
			this.interactiePanel.setSuccessenVeranderbaar(this.successenVeranderbaarBox.isSelected()); //zet instelling in interactiePanel om de preview aan te passen
		}
		if(e.getSource() == this.xAsWeergaveBox) {
			this.interactiePanel.setShowXAs(this.xAsWeergaveBox.isSelected());
		}
		if(e.getSource() == this.yAsWeergaveBox) {
			this.interactiePanel.setShowYAs(this.yAsWeergaveBox.isSelected());
		}
		if(e.getSource() == this.nSliderWeergaveBox) {
			this.interactiePanel.setShowNSlider(this.nSliderWeergaveBox.isSelected());
		}
		if(e.getSource() == this.pSliderWeergaveBox) {
			this.interactiePanel.setShowPSlider(this.pSliderWeergaveBox.isSelected());
		}
		if(e.getSource() == this.successenSliderWeergaveBox) {
			this.interactiePanel.setShowSuccessenSlider(this.successenSliderWeergaveBox.isSelected());
		}
	}
}
