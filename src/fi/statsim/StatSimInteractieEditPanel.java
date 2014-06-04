package fi.statsim;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.JCheckBox;
import javax.swing.ButtonGroup;

import fi.beans.wiskopdrbeans.InteractieEditPanel;

public class StatSimInteractieEditPanel extends JPanel implements InteractieEditPanel, ActionListener {

	private StatSimInteractiePanel interactiePanel;
	private JPanel optionsPanel;
	
	private int defaultWidth = 790;
	private int defaultHeight = 450;
	private int defaultIpWidth = 500;
	private int defaultOpWidth = 200;

	private JRadioButton dobbelstenenRadio;
	private JRadioButton muntenRadio;
	private JRadioButton binomTrekkingRadio;
	private JCheckBox muntenInstellingenCheckBox;
	private JCheckBox muntenResultatenCheckBox;
	private JCheckBox muntenGrafiekCheckBox;
	private JCheckBox muntenTabelCheckBox;
	private JCheckBox muntenFrequentieCheckBox;
	private JCheckBox dobbelstenenInstellingenCheckBox;
	private JCheckBox dobbelstenenResultatenCheckBox;
	private JCheckBox dobbelstenenGrafiekCheckBox;
	private JCheckBox dobbelstenenTabelCheckBox;
	private JCheckBox binomTrekkingInstellingenCheckBox;
	private JCheckBox binomTrekkingGrafiekCheckBox;
	private JCheckBox binomTrekkingTabelCheckBox;
	private JCheckBox binomTrekkingFrequentieCheckBox;
	private JCheckBox binomTrekkingRoosterCheckBox;
	
	public StatSimInteractieEditPanel() {
		setLayout(null);
		
		interactiePanel = new StatSimInteractiePanel();
		interactiePanel.setBounds(10,20,defaultIpWidth,defaultHeight);
		add(interactiePanel);
		
		optionsPanel = new JPanel();
		optionsPanel.setLayout(null);
		optionsPanel.setBounds(defaultIpWidth+30,20,defaultOpWidth,defaultHeight);
		add(optionsPanel);
		
		muntenRadio=new JRadioButton("Munten");
		muntenRadio.setLocation(10,10);
		muntenRadio.setSize(200,20);
		muntenRadio.addActionListener(this);
		muntenRadio.setSelected(true);
		optionsPanel.add(muntenRadio);
		
		muntenInstellingenCheckBox = new JCheckBox("Instellingen");
		muntenInstellingenCheckBox.setLocation(30,30);
		muntenInstellingenCheckBox.setSize(200,20);
		muntenInstellingenCheckBox.addActionListener(this);
		muntenInstellingenCheckBox.setSelected(true);
		optionsPanel.add(muntenInstellingenCheckBox);
		
		muntenResultatenCheckBox = new JCheckBox("Resultaten");
		muntenResultatenCheckBox.setLocation(30,50);
		muntenResultatenCheckBox.setSize(200,20);
		muntenResultatenCheckBox.addActionListener(this);
		muntenResultatenCheckBox.setSelected(true);
		optionsPanel.add(muntenResultatenCheckBox);
		
		muntenGrafiekCheckBox = new JCheckBox("Grafiek");
		muntenGrafiekCheckBox.setLocation(30,70);
		muntenGrafiekCheckBox.setSize(200,20);
		muntenGrafiekCheckBox.addActionListener(this);
		muntenGrafiekCheckBox.setSelected(true);
		optionsPanel.add(muntenGrafiekCheckBox);
		
		muntenTabelCheckBox = new JCheckBox("Tabel");
		muntenTabelCheckBox.setLocation(30,90);
		muntenTabelCheckBox.setSize(200,20);
		muntenTabelCheckBox.addActionListener(this);
		muntenTabelCheckBox.setSelected(true);
		optionsPanel.add(muntenTabelCheckBox);
		
		muntenFrequentieCheckBox = new JCheckBox("Frequentie");
		muntenFrequentieCheckBox.setLocation(30,110);
		muntenFrequentieCheckBox.setSize(200,20);
		muntenFrequentieCheckBox.addActionListener(this);
		muntenFrequentieCheckBox.setSelected(true);
		optionsPanel.add(muntenFrequentieCheckBox);
		
		dobbelstenenRadio=new JRadioButton("Dobbelstenen");
		dobbelstenenRadio.setLocation(10,130);
		dobbelstenenRadio.setSize(200,20);
		dobbelstenenRadio.addActionListener(this);
		optionsPanel.add(dobbelstenenRadio);
		
		dobbelstenenInstellingenCheckBox = new JCheckBox("Instellingen");
		dobbelstenenInstellingenCheckBox.setLocation(30,150);
		dobbelstenenInstellingenCheckBox.setSize(200,20);
		dobbelstenenInstellingenCheckBox.addActionListener(this);
		dobbelstenenInstellingenCheckBox.setSelected(true);
		optionsPanel.add(dobbelstenenInstellingenCheckBox);
		
		dobbelstenenResultatenCheckBox = new JCheckBox("Resultaten");
		dobbelstenenResultatenCheckBox.setLocation(30,170);
		dobbelstenenResultatenCheckBox.setSize(200,20);
		dobbelstenenResultatenCheckBox.addActionListener(this);
		dobbelstenenResultatenCheckBox.setSelected(true);
		optionsPanel.add(dobbelstenenResultatenCheckBox);
		
		dobbelstenenGrafiekCheckBox = new JCheckBox("Grafiek");
		dobbelstenenGrafiekCheckBox.setLocation(30,190);
		dobbelstenenGrafiekCheckBox.setSize(200,20);
		dobbelstenenGrafiekCheckBox.addActionListener(this);
		dobbelstenenGrafiekCheckBox.setSelected(true);
		optionsPanel.add(dobbelstenenGrafiekCheckBox);
		
		dobbelstenenTabelCheckBox = new JCheckBox("Tabel");
		dobbelstenenTabelCheckBox.setLocation(30,210);
		dobbelstenenTabelCheckBox.setSize(200,20);
		dobbelstenenTabelCheckBox.addActionListener(this);
		dobbelstenenTabelCheckBox.setSelected(true);
		optionsPanel.add(dobbelstenenTabelCheckBox);
		
		binomTrekkingRadio=new JRadioButton("Binominale trekking");
		binomTrekkingRadio.setLocation(10,230);
		binomTrekkingRadio.setSize(200,20);
		binomTrekkingRadio.addActionListener(this);
		optionsPanel.add(binomTrekkingRadio);
	
		binomTrekkingInstellingenCheckBox = new JCheckBox("Instellingen");
		binomTrekkingInstellingenCheckBox.setLocation(30,250);
		binomTrekkingInstellingenCheckBox.setSize(200,20);
		binomTrekkingInstellingenCheckBox.addActionListener(this);
		binomTrekkingInstellingenCheckBox.setSelected(true);
		optionsPanel.add(binomTrekkingInstellingenCheckBox);
	
		binomTrekkingGrafiekCheckBox = new JCheckBox("Grafiek");
		binomTrekkingGrafiekCheckBox.setLocation(30,270);
		binomTrekkingGrafiekCheckBox.setSize(200,20);
		binomTrekkingGrafiekCheckBox.addActionListener(this);
		binomTrekkingGrafiekCheckBox.setSelected(true);
		optionsPanel.add(binomTrekkingGrafiekCheckBox);
		
		binomTrekkingTabelCheckBox = new JCheckBox("Tabel");
		binomTrekkingTabelCheckBox.setLocation(30,290);
		binomTrekkingTabelCheckBox.setSize(200,20);
		binomTrekkingTabelCheckBox.addActionListener(this);
		binomTrekkingTabelCheckBox.setSelected(true);
		optionsPanel.add(binomTrekkingTabelCheckBox);
		
		binomTrekkingFrequentieCheckBox = new JCheckBox("Frequentie");
		binomTrekkingFrequentieCheckBox.setLocation(30,310);
		binomTrekkingFrequentieCheckBox.setSize(200,20);
		binomTrekkingFrequentieCheckBox.addActionListener(this);
		binomTrekkingFrequentieCheckBox.setSelected(true);
		optionsPanel.add(binomTrekkingFrequentieCheckBox);
		
		binomTrekkingRoosterCheckBox = new JCheckBox("Rooster");
		binomTrekkingRoosterCheckBox.setLocation(30,330);
		binomTrekkingRoosterCheckBox.setSize(200,20);
		binomTrekkingRoosterCheckBox.addActionListener(this);
		binomTrekkingRoosterCheckBox.setSelected(true);
		optionsPanel.add(binomTrekkingRoosterCheckBox);
		
		ButtonGroup buttonGroup1=new ButtonGroup();
		buttonGroup1.add(muntenRadio);
		buttonGroup1.add(dobbelstenenRadio);
		buttonGroup1.add(binomTrekkingRadio);
	}
	
	public Hashtable getEditState() {
		Hashtable h = interactiePanel.getEditState();
		h.put("muntenRadio", new Boolean(muntenRadio.isSelected()));
		h.put("muntenInstellingen", new Boolean(muntenInstellingenCheckBox.isSelected()));
		h.put("muntenResultaten", new Boolean(muntenResultatenCheckBox.isSelected()));
		h.put("muntenGrafiek", new Boolean(muntenGrafiekCheckBox.isSelected()));
		h.put("muntenTabel", new Boolean(muntenTabelCheckBox.isSelected()));
		h.put("muntenFrequentie", new Boolean(muntenFrequentieCheckBox.isSelected()));
		h.put("dobbelstenenRadio", new Boolean(dobbelstenenRadio.isSelected()));
		h.put("dobbelstenenInstellingen", new Boolean(dobbelstenenInstellingenCheckBox.isSelected()));
		h.put("dobbelstenenResultaten", new Boolean(dobbelstenenResultatenCheckBox.isSelected()));
		h.put("dobbelstenenGrafiek", new Boolean(dobbelstenenGrafiekCheckBox.isSelected()));
		h.put("dobbelstenenTabel", new Boolean(dobbelstenenTabelCheckBox.isSelected()));
		h.put("binomTrekkingRadio", new Boolean(binomTrekkingRadio.isSelected()));
		h.put("binomTrekkingInstellingen", new Boolean(binomTrekkingInstellingenCheckBox.isSelected()));
		h.put("binomTrekkingGrafiek", new Boolean(binomTrekkingGrafiekCheckBox.isSelected()));
		h.put("binomTrekkingTabel", new Boolean(binomTrekkingTabelCheckBox.isSelected()));
		h.put("binomTrekkingFrequentie", new Boolean(binomTrekkingFrequentieCheckBox.isSelected()));
		h.put("binomTrekkingRooster", new Boolean(binomTrekkingRoosterCheckBox.isSelected()));
		return h;
	}
	
	
	public void setEditState(Hashtable h) {
		System.out.println("setEditState");
		Boolean muntenRadioBool=false;
		if(h.containsKey("muntenRadio")) muntenRadioBool= ((Boolean)h.get("muntenRadio")).booleanValue();
		muntenRadio.setSelected(muntenRadioBool);
		interactiePanel.munten.setVisible(muntenRadioBool);

			Boolean muntenInstellingen=true;
			if(h.containsKey("muntenInstellingen")) muntenInstellingen= ((Boolean)h.get("muntenInstellingen")).booleanValue();
			muntenInstellingenCheckBox.setSelected(muntenInstellingen);
			interactiePanel.munten.showInstellingen=muntenInstellingen;
			
			Boolean muntenResultaten=true;
			if(h.containsKey("muntenResultaten")) muntenResultaten= ((Boolean)h.get("muntenResultaten")).booleanValue();
			muntenResultatenCheckBox.setSelected(muntenResultaten);
			interactiePanel.munten.showResultaten=muntenResultaten;
			
			Boolean muntenGrafiek=true;
			if(h.containsKey("muntenGrafiek")) muntenGrafiek= ((Boolean)h.get("muntenGrafiek")).booleanValue();
			muntenGrafiekCheckBox.setSelected(muntenGrafiek);
			interactiePanel.munten.showGrafiek=muntenGrafiek;
			
			Boolean muntenTabel=true;
			if(h.containsKey("muntenTabel")) muntenTabel= ((Boolean)h.get("muntenTabel")).booleanValue();
			muntenTabelCheckBox.setSelected(muntenTabel);
			interactiePanel.munten.showTabel=muntenTabel;
	
			Boolean muntenFrequentie=true;
			if(h.containsKey("muntenFrequentie")) muntenFrequentie= ((Boolean)h.get("muntenFrequentie")).booleanValue();
			muntenFrequentieCheckBox.setSelected(muntenFrequentie);
			interactiePanel.munten.showFrequentie=muntenFrequentie;
			
			interactiePanel.munten.setZichtbaar();
			
		Boolean dobbelstenenRadioBool=false;
		if(h.containsKey("dobbelstenenRadio")) dobbelstenenRadioBool= ((Boolean)h.get("dobbelstenenRadio")).booleanValue();
		dobbelstenenRadio.setSelected(dobbelstenenRadioBool);
		interactiePanel.dobbelstenen.setVisible(dobbelstenenRadioBool);
		
	
			Boolean dobbelstenenInstellingen=true;
			if(h.containsKey("dobbelstenenInstellingen")) dobbelstenenInstellingen= ((Boolean)h.get("dobbelstenenInstellingen")).booleanValue();
			dobbelstenenInstellingenCheckBox.setSelected(dobbelstenenInstellingen);
			interactiePanel.dobbelstenen.showInstellingen=dobbelstenenInstellingen;
			
			Boolean dobbelstenenResultaten=true;
			if(h.containsKey("dobbelstenenResultaten")) dobbelstenenResultaten= ((Boolean)h.get("dobbelstenenResultaten")).booleanValue();
			dobbelstenenResultatenCheckBox.setSelected(dobbelstenenResultaten);
			interactiePanel.dobbelstenen.showResultaten=dobbelstenenResultaten;
			
			Boolean dobbelstenenGrafiek=true;
			if(h.containsKey("dobbelstenenGrafiek")) dobbelstenenGrafiek= ((Boolean)h.get("dobbelstenenGrafiek")).booleanValue();
			dobbelstenenGrafiekCheckBox.setSelected(dobbelstenenGrafiek);
			interactiePanel.dobbelstenen.showGrafiek=dobbelstenenGrafiek;
			
			Boolean dobbelstenenTabel=true;
			if(h.containsKey("dobbelstenenTabel")) dobbelstenenTabel= ((Boolean)h.get("dobbelstenenTabel")).booleanValue();
			dobbelstenenTabelCheckBox.setSelected(dobbelstenenTabel);
			interactiePanel.dobbelstenen.showTabel=dobbelstenenTabel;
			
			interactiePanel.dobbelstenen.setZichtbaar();		
		
		Boolean binomTrekkingRadioBool=false;
		if(h.containsKey("binomTrekkingRadio")) binomTrekkingRadioBool= ((Boolean)h.get("binomTrekkingRadio")).booleanValue();
		binomTrekkingRadio.setSelected(binomTrekkingRadioBool);
		interactiePanel.binomTrekking.setVisible(binomTrekkingRadioBool);

			Boolean binomTrekkingInstellingen=true;
			if(h.containsKey("binomTrekkingInstellingen")) binomTrekkingInstellingen= ((Boolean)h.get("binomTrekkingInstellingen")).booleanValue();
			binomTrekkingInstellingenCheckBox.setSelected(binomTrekkingInstellingen);
			interactiePanel.binomTrekking.showInstellingen=binomTrekkingInstellingen;
			
			Boolean binomTrekkingGrafiek=true;
			if(h.containsKey("binomTrekkingGrafiek")) binomTrekkingGrafiek= ((Boolean)h.get("binomTrekkingGrafiek")).booleanValue();
			binomTrekkingGrafiekCheckBox.setSelected(binomTrekkingGrafiek);
			interactiePanel.binomTrekking.showGrafiek=binomTrekkingGrafiek;
			
			Boolean binomTrekkingTabel=true;
			if(h.containsKey("binomTrekkingTabel")) binomTrekkingTabel= ((Boolean)h.get("binomTrekkingTabel")).booleanValue();
			binomTrekkingTabelCheckBox.setSelected(binomTrekkingTabel);
			interactiePanel.binomTrekking.showTabel=binomTrekkingTabel;
			
			Boolean binomTrekkingFrequentie=true;
			if(h.containsKey("binomTrekkingFrequentie")) binomTrekkingFrequentie= ((Boolean)h.get("binomTrekkingFrequentie")).booleanValue();
			binomTrekkingFrequentieCheckBox.setSelected(binomTrekkingFrequentie);
			interactiePanel.binomTrekking.showFrequentie=binomTrekkingFrequentie;

			Boolean binomTrekkingRooster=true;
			if(h.containsKey("binomTrekkingRooster")) binomTrekkingRooster= ((Boolean)h.get("binomTrekkingRooster")).booleanValue();
			binomTrekkingRoosterCheckBox.setSelected(binomTrekkingRooster);
			interactiePanel.binomTrekking.showRooster=binomTrekkingRooster;
			
			interactiePanel.binomTrekking.setZichtbaar();		
		
		interactiePanel.setEditState(h);
		
	}
	
	public void setBounds(int x, int y, int b, int h) {
		super.setBounds(x,y,b,h);
		optionsPanel.setBounds(b-defaultOpWidth-10, 20, defaultOpWidth, defaultHeight);
	}
	
	
	public void zetBreedte(int b) {
		interactiePanel.setSize(b,interactiePanel.getHeight());	
	}
	
	public void zetHoogte(int h) {
		interactiePanel.setSize(interactiePanel.getWidth(), h);	
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
		   if (e.getSource()==muntenRadio) {
			   interactiePanel.munten.setVisible(true);
			   interactiePanel.dobbelstenen.setVisible(false);
			   interactiePanel.binomTrekking.setVisible(false);
		   }
		   if (e.getSource()==muntenInstellingenCheckBox) {
			   interactiePanel.munten.showInstellingen=muntenInstellingenCheckBox.isSelected();
			   interactiePanel.munten.setZichtbaar();
		   }
		   if (e.getSource()==muntenResultatenCheckBox) {
			   interactiePanel.munten.showResultaten=muntenResultatenCheckBox.isSelected();
			   interactiePanel.munten.setZichtbaar();
		   }
		   if (e.getSource()==muntenGrafiekCheckBox) {
			   interactiePanel.munten.showGrafiek=muntenGrafiekCheckBox.isSelected();
			   interactiePanel.munten.setZichtbaar();
		   }
		   if (e.getSource()==muntenTabelCheckBox) {
			   interactiePanel.munten.showTabel=muntenTabelCheckBox.isSelected();
			   interactiePanel.munten.setZichtbaar();
		   }
		   if (e.getSource()==muntenFrequentieCheckBox) {
			   interactiePanel.munten.showFrequentie=muntenFrequentieCheckBox.isSelected();
			   interactiePanel.munten.setZichtbaar();
		   }
		   if (e.getSource()==dobbelstenenRadio) {
			   interactiePanel.munten.setVisible(false);
			   interactiePanel.dobbelstenen.setVisible(true);
			   interactiePanel.binomTrekking.setVisible(false);
		   }
		   if (e.getSource()==dobbelstenenInstellingenCheckBox) {
			   interactiePanel.dobbelstenen.showInstellingen=dobbelstenenInstellingenCheckBox.isSelected();
			   interactiePanel.dobbelstenen.setZichtbaar();
		   }
		   if (e.getSource()==dobbelstenenResultatenCheckBox) {
			   interactiePanel.dobbelstenen.showResultaten=dobbelstenenResultatenCheckBox.isSelected();
			   interactiePanel.dobbelstenen.setZichtbaar();
		   }
		   if (e.getSource()==dobbelstenenGrafiekCheckBox) {
			   interactiePanel.dobbelstenen.showGrafiek=dobbelstenenGrafiekCheckBox.isSelected();
			   interactiePanel.dobbelstenen.setZichtbaar();
		   }
		   if (e.getSource()==dobbelstenenTabelCheckBox) {
			   interactiePanel.dobbelstenen.showTabel=dobbelstenenTabelCheckBox.isSelected();
			   interactiePanel.dobbelstenen.setZichtbaar();
		   }
		   if (e.getSource()==binomTrekkingRadio) {
			   interactiePanel.munten.setVisible(false);
			   interactiePanel.dobbelstenen.setVisible(false);
			   interactiePanel.binomTrekking.setVisible(true);
		   }
		   if (e.getSource()==binomTrekkingInstellingenCheckBox) {
			   interactiePanel.binomTrekking.showInstellingen=binomTrekkingInstellingenCheckBox.isSelected();
			   interactiePanel.binomTrekking.setZichtbaar();
		   }
		   if (e.getSource()==binomTrekkingGrafiekCheckBox) {
			   interactiePanel.binomTrekking.showGrafiek=binomTrekkingGrafiekCheckBox.isSelected();
			   interactiePanel.binomTrekking.setZichtbaar();
		   }
		   if (e.getSource()==binomTrekkingTabelCheckBox) {
			   interactiePanel.binomTrekking.showTabel=binomTrekkingTabelCheckBox.isSelected();
			   interactiePanel.binomTrekking.setZichtbaar();
		   }
		   if (e.getSource()==binomTrekkingFrequentieCheckBox) {
			   interactiePanel.binomTrekking.showFrequentie=binomTrekkingFrequentieCheckBox.isSelected();
			   interactiePanel.binomTrekking.setZichtbaar();
		   }
		   if (e.getSource()==binomTrekkingRoosterCheckBox) {
			   interactiePanel.binomTrekking.showRooster=binomTrekkingRoosterCheckBox.isSelected();
			   interactiePanel.binomTrekking.setZichtbaar();
		   }
	}

}
