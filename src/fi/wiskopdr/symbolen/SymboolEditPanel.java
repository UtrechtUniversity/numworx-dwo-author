package fi.wiskopdr.symbolen;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Hashtable;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fi.beans.wiskopdrbeans.InteractieEditPanel;


public class SymboolEditPanel extends JPanel implements InteractieEditPanel, ActionListener, FocusListener{

	SymboolPanel symboolPanel;
	int symboolBreedte = 100;
	int symboolHoogte = 100;
	int editHeight = 400;
	
	Font theFont;
	FontMetrics theFM;
	
	int width = 200;
	int height = 20;
	
	int offset = 10;
	
	JComboBox symboolKeuzeBox;
	JComboBox richtingKeuzeBox;
	JLabel dikteLabel;
	JTextField dikteField;
	JCheckBox vulHoogteCB;
	
	public SymboolEditPanel()
	{
		setLayout(null);
		
		theFont = new Font("Dialog", Font.PLAIN, 12);
		theFM = getFontMetrics(theFont);
		
		symboolPanel = new SymboolPanel();
		symboolPanel.setBounds(offset, offset, symboolBreedte, symboolHoogte);
		add(symboolPanel);
		
		int currentX = symboolBreedte + 2 * offset;
		int currentY = offset;
		
		//TODO: alle teksten vervangen door rb.
		String[] symboolString = {"Kies symbool", "Lijn", "Pijl", "Accolade", "Ellips"};
				
		symboolKeuzeBox = new JComboBox(symboolString);
		symboolKeuzeBox.setBounds(currentX, currentY, width, height);
		symboolKeuzeBox.setFont(theFont);
		add(symboolKeuzeBox);
		symboolKeuzeBox.addActionListener(this);
		
		currentY += height + offset;
		
		String[] richtingString = {"Kies eerst symbool"};
		richtingKeuzeBox  = new JComboBox(richtingString);
		richtingKeuzeBox.setBounds(currentX, currentY, width, height);
		richtingKeuzeBox.setFont(theFont);
		add(richtingKeuzeBox);
		richtingKeuzeBox.addActionListener(this);
		
		currentY += height + offset;
		
		dikteLabel = new JLabel("Dikte");
		dikteLabel.setBounds(currentX, currentY, width / 2, height);
		dikteLabel.setFont(theFont);
		add(dikteLabel);
		currentX += width / 2 + offset;
		
		dikteField = new JTextField("1");//TODO: als dikte nog variabele wordt in deze class, dan hier dikte invullen.
		dikteField.setBounds(currentX, currentY, width / 2 - offset, height);
		add(dikteField);
		dikteField.addActionListener(this);
		dikteField.addFocusListener(this);
		
		currentX -= width / 2 - offset;
		currentY += height + offset;
		
		//TODO: kleurknop toevoegen
		
		currentY += height + offset;
		
		vulHoogteCB = new JCheckBox("Vul hoogte");
		vulHoogteCB.setBounds(currentX, currentY, width, height);
		vulHoogteCB.setOpaque(false);
		vulHoogteCB.setFont(theFont);
		add(vulHoogteCB);
		vulHoogteCB.addActionListener(this);
		
	}

	public void plaatsComponenten()
	{
		if(symboolKeuzeBox == null)
			return;
		int indent = symboolPanel.getWidth() + 2 * offset;
		symboolKeuzeBox.setLocation(indent, symboolKeuzeBox.getLocation().y);
		richtingKeuzeBox.setLocation(indent, richtingKeuzeBox.getLocation().y);
		dikteLabel.setLocation(indent, dikteLabel.getLocation().y);
		dikteField.setLocation(indent + width / 2 + offset, dikteField.getLocation().y);
		//locatie kleur zetten.
		vulHoogteCB.setLocation(indent, vulHoogteCB.getLocation().y);
	}
	
	public void zetRichtingKeuzes(int symboolKeuze)
	{
		//symboolkeuze loopt waarschijnlijk nog niet gelijk met symboolkeuzes uit lijstje in Symbool. +1?
		String[] richtingString = {};
		//in plaats van teksten zouden plaatjes mooier zijn. 
		if(symboolKeuze == Symbool.GEEN)
		{	richtingString = new String[1];
			richtingString[0] = "Kies eerst symbool";
		}
		else if(symboolKeuze == Symbool.LIJN)
		{	richtingString = new String[4];
			richtingString[0] = "Horizontaal";
			richtingString[1] = "Verticaal";
			richtingString[2] = "Diagonaal omhoog";
			richtingString[3] = "Diagonaal omlaag";
		}
		else if(symboolKeuze == Symbool.PIJL)
		{
			richtingString = new String[8];
			richtingString[0] = "Links";
			richtingString[1] = "Rechts";
			richtingString[2] = "Boven";
			richtingString[3] = "Beneden";
			richtingString[4] = "Rechtsboven";
			richtingString[5] = "Rechtsonder";
			richtingString[6] = "Linksonder";
			richtingString[7] = "Linksboven";
		}
		else if(symboolKeuze == Symbool.ACCOLADE)
		{
			richtingString = new String[4];
			richtingString[0] = "Links";
			richtingString[1] = "Rechts";
			richtingString[2] = "Boven";
			richtingString[3] = "Beneden";
		}
		else if(symboolKeuze == Symbool.ELLIPS)
		{
			//nog kijken wat ik hiermee doe. Schuine ellipsen mogelijk maken? 
			//anders is richting kiezen hier vrij zinloos. 
		}
		//ook nog optie bouwen voor als geen symboolkeuze?
		//nog testen of onderstaande allemaal nodig. richtingkeuzebox opnieuw initialiseren.
		int x = richtingKeuzeBox.getLocation().x;
		int y = richtingKeuzeBox.getLocation().y;
		remove(richtingKeuzeBox);
		richtingKeuzeBox = new JComboBox(richtingString);
		richtingKeuzeBox.setBounds(x, y, width, height);
		richtingKeuzeBox.setFont(theFont);
		add(richtingKeuzeBox);
		richtingKeuzeBox.addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == symboolKeuzeBox)
		{
			int keuze = symboolKeuzeBox.getSelectedIndex();
			symboolPanel.zetSymboolKeuze(keuze);
			zetRichtingKeuzes(keuze);
		}
		else if(e.getSource() == richtingKeuzeBox)
		{
			symboolPanel.zetRichtingKeuze(richtingKeuzeBox.getSelectedIndex());
			//kijken of op deze manier slim is; richting is ook afhankelijk van symboolkeuze
		}
		else if(e.getSource() == dikteField)
		{
			symboolPanel.zetDikte(Integer.parseInt(dikteField.getText()));
		}
		else if(e.getSource() == vulHoogteCB)
		{
			symboolPanel.zetVulHoogte(vulHoogteCB.isSelected());
		}
	}

	@Override
	public void focusGained(FocusEvent arg0) {
		
	}

	@Override
	public void focusLost(FocusEvent e) {
		if(e.getSource() == dikteField)
		{
			symboolPanel.zetDikte(Integer.parseInt(dikteField.getText()));
		}
		
	}

	@Override
	public void setEditState(Hashtable b) {
		
	}

	@Override
	public Hashtable getEditState() {
		
		
		Hashtable h = symboolPanel.getEditState();
		
		return h;
	}

	@Override
	public void zetBreedte(int b) {
		symboolBreedte = b;
		
		setBounds(getLocation().x, getLocation().y, symboolBreedte + width + 3 * offset, Math.max(symboolHoogte, editHeight));		
		plaatsComponenten();
	}

	@Override
	public void zetHoogte(int h) {
		symboolHoogte = h;
		
		setBounds(getLocation().x, getLocation().y, symboolBreedte + width + 3 * offset, Math.max(symboolHoogte, editHeight));		

	}

	@Override
	public void stop() {
		
	}

	@Override
	public void start() {
		
	}
	
	
}
