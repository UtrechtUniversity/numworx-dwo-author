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

/**
 * InteractieEditPanel van de Binomiale Verdeling
 */
public class BVInteractieEditPanel extends JPanel implements InteractieEditPanel, ActionListener {
	private JPanel settingsPanel;
	private BVInteractiePanel interactiePanel;
	private JCheckBox nVeranderbaarBox;
	private JCheckBox pVeranderbaarBox;
	private JLabel veranderbaarLabel;
	private JLabel weergaveLabel;
	private JCheckBox xAsWeergaveBox;
	private JCheckBox yAsWeergaveBox;
	private JCheckBox nSliderWeergaveBox;
	private JCheckBox pSliderWeergaveBox;
	private JCheckBox grensSliderWeergaveBox;
	private JCheckBox showToggleGrenzenBox;
	private JCheckBox showKansBalkBox;
	private JCheckBox showNPanelBox;
	private Font font;
	
	/**
	 * Constructor
	 */
	public BVInteractieEditPanel() {		
		this.setLayout(new GridLayout(0,2));
		this.interactiePanel = new BVInteractiePanel();
		this.add(this.interactiePanel);
		
		this.font = new Font("Dialog", Font.PLAIN, 12);
		
		this.settingsPanel = new JPanel();
		this.settingsPanel.setLayout(new GridLayout(10,1));
		this.veranderbaarLabel = new JLabel("aanpasbaarheid instellingen");
		this.veranderbaarLabel.setFont(this.font);
		this.settingsPanel.add(this.veranderbaarLabel);
		
		this.nVeranderbaarBox = new JCheckBox("n veranderbaar", true);
		this.nVeranderbaarBox.setFont(this.font);
		this.nVeranderbaarBox.addActionListener(this);
		this.settingsPanel.add(this.nVeranderbaarBox);
		
		this.pVeranderbaarBox = new JCheckBox("p veranderbaar", true);
		this.pVeranderbaarBox.setFont(this.font);
		this.pVeranderbaarBox.addActionListener(this);
		this.settingsPanel.add(this.pVeranderbaarBox);
		
		this.weergaveLabel = new JLabel("weergave instellingen");
		this.weergaveLabel.setFont(this.font);
		this.settingsPanel.add(this.weergaveLabel);
		
		this.xAsWeergaveBox = new JCheckBox("geef X-as weer", true);
		this.xAsWeergaveBox.setFont(this.font);
		this.xAsWeergaveBox.addActionListener(this);
		this.settingsPanel.add(this.xAsWeergaveBox);
		
		this.yAsWeergaveBox = new JCheckBox("Geef Y-as weer", true);
		this.yAsWeergaveBox.setFont(this.font);
		this.yAsWeergaveBox.addActionListener(this);
		this.settingsPanel.add(this.yAsWeergaveBox);
		
		this.nSliderWeergaveBox = new JCheckBox("Geef slider voor n weer", true);
		this.nSliderWeergaveBox.setFont(this.font);
		this.nSliderWeergaveBox.addActionListener(this);
		this.settingsPanel.add(this.nSliderWeergaveBox);
		
		this.pSliderWeergaveBox = new JCheckBox("Geef slider voor p weer", true);
		this.pSliderWeergaveBox.setFont(this.font);
		this.pSliderWeergaveBox.addActionListener(this);
		this.settingsPanel.add(this.pSliderWeergaveBox);
		
		this.grensSliderWeergaveBox = new JCheckBox("Geef slider voor successen weer", true);
		this.grensSliderWeergaveBox.setFont(this.font);
		this.grensSliderWeergaveBox.addActionListener(this);
		this.settingsPanel.add(this.grensSliderWeergaveBox);
		
		this.showToggleGrenzenBox = new JCheckBox("Geef keuze tussen één en twee grenzen",true);
		this.showToggleGrenzenBox.setFont(this.font);
		this.showToggleGrenzenBox.addActionListener(this);
		this.settingsPanel.add(this.showToggleGrenzenBox);
		
		this.showNPanelBox = new JCheckBox("Geef N-paneel weer", true);
		this.showNPanelBox.setFont(this.font);
		this.showNPanelBox.addActionListener(this);
		this.settingsPanel.add(this.showNPanelBox);
		
		this.showKansBalkBox = new JCheckBox("Geef kansenbalk weer", true);
		this.showKansBalkBox.setFont(this.font);
		this.showKansBalkBox.addActionListener(this);
		this.settingsPanel.add(this.showKansBalkBox);
		
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
		if (h.containsKey("showGrensSlider")) {
			this.grensSliderWeergaveBox.setSelected(((Boolean)h.get("showGrensSlider")).booleanValue());
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
		h.put("showXAs", new Boolean(this.xAsWeergaveBox.isSelected()));
		h.put("showYAs", new Boolean(this.yAsWeergaveBox.isSelected()));
		h.put("showNSlider", new Boolean(this.nSliderWeergaveBox.isSelected()));
		h.put("showPSlider", new Boolean(this.pSliderWeergaveBox.isSelected()));
		h.put("showGrensSlider", new Boolean(this.grensSliderWeergaveBox.isSelected()));
		
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
	
	/**
	 * Implementatie voor ActionListener
	 * Verwerkt de user-interaction
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == this.nVeranderbaarBox) {
			this.interactiePanel.setNVeranderbaar(this.nVeranderbaarBox.isSelected()); //zet instelling in interactiePanel om de preview aan te passen
		}
		if(e.getSource() == this.pVeranderbaarBox) {
			this.interactiePanel.setPVeranderbaar(this.pVeranderbaarBox.isSelected()); //zet instelling in interactiePanel om de preview aan te passen
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
		if(e.getSource() == this.grensSliderWeergaveBox) {
			this.interactiePanel.setShowGrensSlider(this.grensSliderWeergaveBox.isSelected());
		}
		if(e.getSource() == this.showToggleGrenzenBox) {
			this.interactiePanel.setShowTweeGrenzenKeuze(this.showToggleGrenzenBox.isSelected());
		}
		if(e.getSource() == this.showNPanelBox) {
			this.interactiePanel.setShowNPanel(this.showNPanelBox.isSelected());
		}
		if(e.getSource() == this.showKansBalkBox) {
			this.interactiePanel.setShowKansBalk(this.showKansBalkBox.isSelected());
		}
	}
}
