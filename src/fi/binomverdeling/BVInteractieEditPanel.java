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
	private JCheckBox MVeranderbaarBox;
	private JCheckBox populatieVeranderbaarBox;
	private JLabel veranderbaarLabel;
	private JLabel weergaveLabel;
	private JCheckBox xAsWeergaveBox;
	private JCheckBox yAsWeergaveBox;
	private JCheckBox nSliderWeergaveBox;
	private JCheckBox pSliderWeergaveBox;
	private JCheckBox MSliderWeergaveBox;
	private JCheckBox populatieSliderWeergaveBox;
	
	private JCheckBox grensSliderWeergaveBox;
	private JCheckBox showTweeGrenzenKeuzeBox;
	private JCheckBox showKansBalkBox;
	private JCheckBox showNoordBalkBox;
	private JCheckBox showHyperKeuzeBox;
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
		
		this.populatieVeranderbaarBox = new JCheckBox("populatie veranderbaar", true);
		this.populatieVeranderbaarBox.setFont(this.font);
		this.populatieVeranderbaarBox.addActionListener(this);
		this.settingsPanel.add(this.populatieVeranderbaarBox);
		
		this.MVeranderbaarBox = new JCheckBox("M veranderbaar", true);
		this.MVeranderbaarBox.setFont(this.font);
		this.MVeranderbaarBox.addActionListener(this);
		this.settingsPanel.add(this.MVeranderbaarBox);
		
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
		
		this.populatieSliderWeergaveBox = new JCheckBox("Geef slider voor populatie weer", true);
		this.populatieSliderWeergaveBox.setFont(this.font);
		this.populatieSliderWeergaveBox.addActionListener(this);
		this.settingsPanel.add(this.populatieSliderWeergaveBox);
		
		this.MSliderWeergaveBox = new JCheckBox("Geef slider voor M weer", true);
		this.MSliderWeergaveBox.setFont(this.font);
		this.MSliderWeergaveBox.addActionListener(this);
		this.settingsPanel.add(this.MSliderWeergaveBox);
		
		this.grensSliderWeergaveBox = new JCheckBox("Geef slider voor successen weer", true);
		this.grensSliderWeergaveBox.setFont(this.font);
		this.grensSliderWeergaveBox.addActionListener(this);
		this.settingsPanel.add(this.grensSliderWeergaveBox);
		
		this.showTweeGrenzenKeuzeBox = new JCheckBox("Geef keuze tussen één en twee grenzen",true);
		this.showTweeGrenzenKeuzeBox.setFont(this.font);
		this.showTweeGrenzenKeuzeBox.addActionListener(this);
		this.settingsPanel.add(this.showTweeGrenzenKeuzeBox);
		
		this.showNoordBalkBox = new JCheckBox("Geef noordbalk weer", true);
		this.showNoordBalkBox.setFont(this.font);
		this.showNoordBalkBox.addActionListener(this);
		this.settingsPanel.add(this.showNoordBalkBox);
		
		this.showHyperKeuzeBox = new JCheckBox("Geef keuze tussen hypergeometrisch en binomiaal", true);
		this.showHyperKeuzeBox.setFont(this.font);
		this.showHyperKeuzeBox.addActionListener(this);
		this.settingsPanel.add(this.showHyperKeuzeBox);
		
		this.showKansBalkBox = new JCheckBox("Geef kansenbalk weer", true);
		this.showKansBalkBox.setFont(this.font);
		this.showKansBalkBox.addActionListener(this);
		this.settingsPanel.add(this.showKansBalkBox);
		
		this.add(this.settingsPanel);
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
		if (e.getSource() == this.populatieVeranderbaarBox) {
			this.interactiePanel.setPopulatieVeranderbaar(this.populatieVeranderbaarBox.isSelected());
		}
		if(e.getSource() == this.MVeranderbaarBox) {
			this.interactiePanel.setMVeranderbaar(this.MVeranderbaarBox.isSelected());
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
		if(e.getSource() == this.MSliderWeergaveBox) {
			this.interactiePanel.setShowMSlider(this.MSliderWeergaveBox.isSelected());
		}
		if(e.getSource() == this.populatieSliderWeergaveBox) {
			this.interactiePanel.setShowPopulatieSlider(this.populatieSliderWeergaveBox.isSelected());
		}
		if(e.getSource() == this.grensSliderWeergaveBox) {
			this.interactiePanel.setShowGrensSlider(this.grensSliderWeergaveBox.isSelected());
		}
		if(e.getSource() == this.showTweeGrenzenKeuzeBox) {
			this.interactiePanel.setShowTweeGrenzenKeuze(this.showTweeGrenzenKeuzeBox.isSelected());
		}
		if(e.getSource() == this.showNoordBalkBox) {
			this.interactiePanel.setShowNoordBalk(this.showNoordBalkBox.isSelected());
		}
		if(e.getSource() == this.showKansBalkBox) {
			this.interactiePanel.setShowKansBalk(this.showKansBalkBox.isSelected());
		}
		if(e.getSource() == this.showHyperKeuzeBox) {
			this.interactiePanel.setShowHyperKeuze(this.showHyperKeuzeBox.isSelected());
		}
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
		if (h.containsKey("MVeranderbaar")) {
			this.MVeranderbaarBox.setSelected(((Boolean)h.get("MVeranderbaar")).booleanValue());
		}
		if (h.containsKey("populatieVeranderbaar")) {
			this.populatieVeranderbaarBox.setSelected(((Boolean)h.get("populatieVeranderbaar")).booleanValue());
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
		if (h.containsKey("showMSlider")) {
			this.MSliderWeergaveBox.setSelected(((Boolean)h.get("showMSlider")).booleanValue());
		}
		if (h.containsKey("showPopulatieSlider")) {
			this.populatieSliderWeergaveBox.setSelected(((Boolean)h.get("showPopulatieSlider")).booleanValue());
		}
		if (h.containsKey("showGrensSlider")) {
			this.grensSliderWeergaveBox.setSelected(((Boolean)h.get("showGrensSlider")).booleanValue());
		}
		if(h.containsKey("showKansBalk")) {
			this.showKansBalkBox.setSelected(((Boolean)h.get("showKansBalk")).booleanValue());
		}
		if(h.containsKey("showNoordBalk")) {
			this.showNoordBalkBox.setSelected(((Boolean)h.get("showNoordBalk")).booleanValue());
		}
		if(h.containsKey("showTweeGrenzenKeuze")) {
			this.showTweeGrenzenKeuzeBox.setSelected(((Boolean)h.get("showTweeGrenzenKeuze")).booleanValue());
		}
		if(h.containsKey("showHyperKeuze")) {
			this.showHyperKeuzeBox.setSelected(((Boolean)h.get("showHyperKeuze")).booleanValue());
		}
	}

	/**
	 * Geef alle instellingen in de vorm van een hashtable
	 */
	public Hashtable getEditState() {
		
		//haal de edit gegevens uit het InteractiePanel
		Hashtable h = this.interactiePanel.getEditState();
		
		//en voeg de editgegevens uit het InteractieEditPanel er aan toe.
		/*
		h.put("nVeranderbaar", new Boolean(this.nVeranderbaarBox.isSelected()));
		h.put("pVeranderbaar", new Boolean(this.pVeranderbaarBox.isSelected()));
		h.put("showXAs", new Boolean(this.xAsWeergaveBox.isSelected()));
		h.put("showYAs", new Boolean(this.yAsWeergaveBox.isSelected()));
		h.put("showNSlider", new Boolean(this.nSliderWeergaveBox.isSelected()));
		h.put("showPSlider", new Boolean(this.pSliderWeergaveBox.isSelected()));
		h.put("showGrensSlider", new Boolean(this.grensSliderWeergaveBox.isSelected()));
		h.put("showKansBalk", new Boolean(this.showKansBalkBox.isSelected()));
		h.put("showNoordBalk", new Boolean(this.showNoordBalkBox.isSelected()));
		h.put("showTweeGrenzenKeuze", new Boolean(this.showTweeGrenzenKeuzeBox.isSelected()));
		*/
		
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
}
