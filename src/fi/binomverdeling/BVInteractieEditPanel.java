package fi.binomverdeling;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import fi.beans.wiskopdrbeans.InteractieEditPanel;

/**
 * InteractieEditPanel van de Binomiale Verdeling
 */
public class BVInteractieEditPanel extends JPanel implements InteractieEditPanel, ActionListener {
	private JPanel settingsPanel;
	
	private BVInteractiePanel interactiePanel;
	
	//aanpassingendeel
	private JPanel aanpassingenHelft;
	private JCheckBox nVeranderbaarBox;
	private JCheckBox pVeranderbaarBox;
	private JCheckBox MVeranderbaarBox;
	private JCheckBox populatieVeranderbaarBox;
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
	
	
	//nakijkdeel:
	private JPanel nakijkHelft;
	private JCheckBox nakijkenBox;
	
	private JCheckBox kijkMNa;
	private JTextField MField;
	private JPanel checkMPanel;
	
	private JCheckBox kijkNNa;
	private JTextField nField;
	private JPanel checkNPanel;
	
	private JCheckBox kijkPopulatieNa;
	private JTextField populatieField;
	private JPanel checkPopulatiePanel;
	
	private JCheckBox kijkPNa;
	private JTextField pField;
	private JPanel checkPPanel;
	
	private JCheckBox kijkVerdelingNa;
	private JComboBox verdelingComboBox;
	private JPanel checkVerdelingPanel;
	
	private JCheckBox kijkGrenzenNa;
	private JLabel grensVanLabel;
	private JTextField grensLinksField;
	private JLabel grensTotLabel;
	private JTextField grensRechtsField;
	private JPanel checkGrenzenPanel;
	private JPanel checkGrenzenHulpPanel;
	
	/**
	 * Constructor
	 */
	public BVInteractieEditPanel() {		
		this.setLayout(new GridLayout(0,2));
		this.interactiePanel = new BVInteractiePanel();
		this.add(this.interactiePanel);
		
		this.font = new Font("Dialog", Font.PLAIN, 12);
		
		this.settingsPanel = new JPanel();
		this.settingsPanel.setLayout(new GridLayout(2,1));
		
		//aanpassingenhelft
		this.aanpassingenHelft = new JPanel();
		this.aanpassingenHelft.setLayout(new GridLayout(10,1));
		
		this.nVeranderbaarBox = new JCheckBox("n veranderbaar", true);
		this.nVeranderbaarBox.setFont(this.font);
		this.nVeranderbaarBox.addActionListener(this);
		this.aanpassingenHelft.add(this.nVeranderbaarBox);
		
		this.pVeranderbaarBox = new JCheckBox("p veranderbaar", true);
		this.pVeranderbaarBox.setFont(this.font);
		this.pVeranderbaarBox.addActionListener(this);
		this.aanpassingenHelft.add(this.pVeranderbaarBox);
		
		this.populatieVeranderbaarBox = new JCheckBox("populatie veranderbaar", true);
		this.populatieVeranderbaarBox.setFont(this.font);
		this.populatieVeranderbaarBox.addActionListener(this);
		this.aanpassingenHelft.add(this.populatieVeranderbaarBox);
		
		this.MVeranderbaarBox = new JCheckBox("M veranderbaar", true);
		this.MVeranderbaarBox.setFont(this.font);
		this.MVeranderbaarBox.addActionListener(this);
		this.aanpassingenHelft.add(this.MVeranderbaarBox);
		
		this.xAsWeergaveBox = new JCheckBox("geef X-as weer", true);
		this.xAsWeergaveBox.setFont(this.font);
		this.xAsWeergaveBox.addActionListener(this);
		this.aanpassingenHelft.add(this.xAsWeergaveBox);
		
		this.yAsWeergaveBox = new JCheckBox("Geef Y-as weer", true);
		this.yAsWeergaveBox.setFont(this.font);
		this.yAsWeergaveBox.addActionListener(this);
		this.aanpassingenHelft.add(this.yAsWeergaveBox);
		
		this.nSliderWeergaveBox = new JCheckBox("Geef slider voor n weer", true);
		this.nSliderWeergaveBox.setFont(this.font);
		this.nSliderWeergaveBox.addActionListener(this);
		this.aanpassingenHelft.add(this.nSliderWeergaveBox);
		
		this.pSliderWeergaveBox = new JCheckBox("Geef slider voor p weer", true);
		this.pSliderWeergaveBox.setFont(this.font);
		this.pSliderWeergaveBox.addActionListener(this);
		this.aanpassingenHelft.add(this.pSliderWeergaveBox);
		
		this.populatieSliderWeergaveBox = new JCheckBox("Geef slider voor populatie weer", true);
		this.populatieSliderWeergaveBox.setFont(this.font);
		this.populatieSliderWeergaveBox.addActionListener(this);
		this.aanpassingenHelft.add(this.populatieSliderWeergaveBox);
		
		this.MSliderWeergaveBox = new JCheckBox("Geef slider voor M weer", true);
		this.MSliderWeergaveBox.setFont(this.font);
		this.MSliderWeergaveBox.addActionListener(this);
		this.aanpassingenHelft.add(this.MSliderWeergaveBox);
		
		this.grensSliderWeergaveBox = new JCheckBox("Geef slider voor successen weer", true);
		this.grensSliderWeergaveBox.setFont(this.font);
		this.grensSliderWeergaveBox.addActionListener(this);
		this.aanpassingenHelft.add(this.grensSliderWeergaveBox);
		
		this.showTweeGrenzenKeuzeBox = new JCheckBox("Geef keuze tussen één en twee grenzen",true);
		this.showTweeGrenzenKeuzeBox.setFont(this.font);
		this.showTweeGrenzenKeuzeBox.addActionListener(this);
		this.aanpassingenHelft.add(this.showTweeGrenzenKeuzeBox);
		
		this.showNoordBalkBox = new JCheckBox("Geef noordbalk weer", true);
		this.showNoordBalkBox.setFont(this.font);
		this.showNoordBalkBox.addActionListener(this);
		this.aanpassingenHelft.add(this.showNoordBalkBox);
		
		this.showHyperKeuzeBox = new JCheckBox("Geef keuze tussen hypergeometrisch en binomiaal", true);
		this.showHyperKeuzeBox.setFont(this.font);
		this.showHyperKeuzeBox.addActionListener(this);
		this.aanpassingenHelft.add(this.showHyperKeuzeBox);
		
		this.showKansBalkBox = new JCheckBox("Geef kansenbalk weer", true);
		this.showKansBalkBox.setFont(this.font);
		this.showKansBalkBox.addActionListener(this);
		this.aanpassingenHelft.add(this.showKansBalkBox);
		
		this.settingsPanel.add(this.aanpassingenHelft);
		
		
		//nakijkhelft
		this.nakijkHelft = new JPanel();
		
		GridLayout layout = new GridLayout(7,1);
		layout.setVgap(10);
		this.nakijkHelft.setLayout(layout);
		
		this.nakijkenBox = new JCheckBox("Kijk deze opdracht na", false);
		this.nakijkenBox.setFont(this.font);
		this.nakijkenBox.addActionListener(this);
		this.nakijkHelft.add(this.nakijkenBox);
		
		
		this.kijkVerdelingNa = new JCheckBox("Kijk het soort verdeling na", false);
		this.kijkVerdelingNa.setFont(this.font);
		this.kijkVerdelingNa.addActionListener(this);
		String[] keuzes = {"Binomiaal", "Hypergeometrisch"};
		this.verdelingComboBox = new JComboBox(keuzes);
		this.verdelingComboBox.setFont(this.font);
		this.verdelingComboBox.addActionListener(this);
		this.verdelingComboBox.setVisible(false);
		this.checkVerdelingPanel = new JPanel(new GridLayout(1,2));
		this.checkVerdelingPanel.add(this.kijkVerdelingNa);
		this.checkVerdelingPanel.add(this.verdelingComboBox);
		this.checkVerdelingPanel.setVisible(false);
		this.nakijkHelft.add(this.checkVerdelingPanel);
		
		
		this.kijkGrenzenNa = new JCheckBox("Kijk grenzen na", false);
		this.kijkGrenzenNa.setFont(this.font);
		this.kijkGrenzenNa.addActionListener(this);
		this.grensVanLabel = new JLabel("van:");
		this.grensVanLabel.setFont(this.font);
		this.grensVanLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		this.grensLinksField = new JTextField(3);
		this.grensLinksField.setFont(this.font);
		this.grensLinksField.addActionListener(this);
		this.grensTotLabel = new JLabel("tot:");
		this.grensTotLabel.setFont(this.font);
		this.grensTotLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		this.grensRechtsField = new JTextField(3);
		this.grensRechtsField.setFont(this.font);
		this.grensRechtsField.addActionListener(this);
		this.checkGrenzenPanel = new JPanel(new GridLayout(1,2));
		this.checkGrenzenPanel.add(this.kijkGrenzenNa);
		this.checkGrenzenHulpPanel = new JPanel(new GridLayout(1,4));
		this.checkGrenzenHulpPanel.add(this.grensVanLabel);
		this.checkGrenzenHulpPanel.add(this.grensLinksField);
		this.checkGrenzenHulpPanel.add(this.grensTotLabel);
		this.checkGrenzenHulpPanel.add(this.grensRechtsField);
		this.checkGrenzenHulpPanel.setVisible(false);
		this.checkGrenzenPanel.add(this.checkGrenzenHulpPanel);
		this.checkGrenzenPanel.setVisible(false);
		this.nakijkHelft.add(this.checkGrenzenPanel);
		
		
		this.kijkNNa = new JCheckBox("Kijk n na", false);
		this.kijkNNa.setFont(this.font);
		this.kijkNNa.addActionListener(this);
		this.nField = new JTextField(4);
		this.nField.setText("30");
		this.nField.setFont(this.font);
		this.nField.setVisible(false);
		this.checkNPanel = new JPanel(new GridLayout(1,2));
		this.checkNPanel.add(this.kijkNNa);
		this.checkNPanel.add(this.nField);
		this.checkNPanel.setVisible(false);
		this.nakijkHelft.add(this.checkNPanel);
		
		
		this.kijkPNa = new JCheckBox("Kijk p na", false);
		this.kijkPNa.setFont(this.font);
		this.kijkPNa.addActionListener(this);
		this.pField = new JTextField(4);
		this.pField.setText("0.5");
		this.pField.setFont(this.font);
		this.pField.setVisible(false);
		this.checkPPanel = new JPanel(new GridLayout(1,2));
		this.checkPPanel.add(this.kijkPNa);
		this.checkPPanel.add(this.pField);
		this.checkPPanel.setVisible(false);
		this.nakijkHelft.add(this.checkPPanel);
		
		
		this.kijkPopulatieNa = new JCheckBox("Kijk populatie na", false);
		this.kijkPopulatieNa.setFont(this.font);
		this.kijkPopulatieNa.addActionListener(this);
		this.populatieField = new JTextField(4);
		this.populatieField.setText("100");
		this.populatieField.setFont(this.font);
		this.populatieField.setVisible(false);
		this.checkPopulatiePanel = new JPanel(new GridLayout(1,2));
		this.checkPopulatiePanel.add(this.kijkPopulatieNa);
		this.checkPopulatiePanel.add(this.populatieField);
		this.checkPopulatiePanel.setVisible(false);
		this.nakijkHelft.add(this.checkPopulatiePanel);
		
		
		this.kijkMNa = new JCheckBox("Kijk M na", false);
		this.kijkMNa.setFont(this.font);
		this.kijkMNa.addActionListener(this);
		this.MField = new JTextField(4);
		this.MField.setText("50");
		this.MField.setFont(this.font);
		this.MField.setVisible(false);
		this.checkMPanel = new JPanel(new GridLayout(1,2));
		this.checkMPanel.add(this.kijkMNa);
		this.checkMPanel.add(this.MField);
		this.checkMPanel.setVisible(false);
		this.nakijkHelft.add(this.checkMPanel);
		
		
		this.settingsPanel.add(this.nakijkHelft);
		this.add(this.settingsPanel);
	}
	
	private void showRightItems() {
		this.checkVerdelingPanel.setVisible(this.nakijkenBox.isSelected());
		this.verdelingComboBox.setVisible(this.nakijkenBox.isSelected() && this.kijkVerdelingNa.isSelected());
		
		this.checkGrenzenPanel.setVisible(this.nakijkenBox.isSelected());
		this.checkGrenzenHulpPanel.setVisible(this.nakijkenBox.isSelected() && this.kijkVerdelingNa.isSelected() && this.kijkGrenzenNa.isSelected());
		
		this.checkNPanel.setVisible(this.nakijkenBox.isSelected());
		this.nField.setVisible(this.nakijkenBox.isSelected() && this.kijkNNa.isSelected());
		
		this.checkPPanel.setVisible(this.nakijkenBox.isSelected() && this.kijkVerdelingNa.isSelected() && this.verdelingComboBox.getSelectedIndex() == 0);
		this.pField.setVisible(this.nakijkenBox.isSelected() && this.kijkVerdelingNa.isSelected() && this.verdelingComboBox.getSelectedIndex() == 0 && this.kijkPNa.isSelected());
		
		this.checkMPanel.setVisible(this.nakijkenBox.isSelected() && this.kijkVerdelingNa.isSelected() && this.verdelingComboBox.getSelectedIndex() == 1);
		this.MField.setVisible(this.nakijkenBox.isSelected() && this.kijkVerdelingNa.isSelected() && this.verdelingComboBox.getSelectedIndex() == 1 && this.kijkMNa.isSelected());
		
		this.checkPopulatiePanel.setVisible(this.nakijkenBox.isSelected() && this.kijkVerdelingNa.isSelected() && this.verdelingComboBox.getSelectedIndex() == 1);
		this.populatieField.setVisible(this.nakijkenBox.isSelected() && this.kijkVerdelingNa.isSelected() && this.verdelingComboBox.getSelectedIndex() == 1 && this.kijkPopulatieNa.isSelected());
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
		
		this.showRightItems();
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
