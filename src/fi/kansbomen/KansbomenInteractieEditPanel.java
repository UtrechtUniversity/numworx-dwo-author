package fi.kansbomen;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.*;
import java.util.Hashtable;

import javax.swing.*;

import fi.beans.wiskopdrbeans.InteractieEditPanel;


public class KansbomenInteractieEditPanel extends JPanel implements ActionListener, FocusListener, InteractieEditPanel

{

	int editWidth = 190;
	int editHeight = 500; 
	int kbipBreedte = 500; // startbreedte ip
	int kbipHoogte = 450; // starthoogte ip
	
	Font theFont;
	FontMetrics theFM;
	Font theBoldFont;
	FontMetrics theBoldFM;
	
	int offset = 4;
	int currentX;
	int currentY;
	int width;
	int height;
	
	boolean componentsCreated = false;
	//boolean noSetBounds = false;	

	protected KansbomenInteractiePanel kbip; //Ik weet niet waarom protected.
	JCheckBox kleurBox;
	JCheckBox teruglegZichtbaarBox, trekkingZichtbaarBox, optiesZichtbaarBox, 
				ballenZichtbaarBox, legendaZichtbaarBox, bovenbalkZichtbaarBox;
	JCheckBox kijkNaBox;
	JButton nakijkModelKnop;
	boolean teruglegZichtbaar = true;
	boolean trekkingZichtbaar = true;
	boolean optiesZichtbaar = true;
	boolean ballenZichtbaar = true;
	boolean legendaZichtbaar = true;
	boolean bovenbalkZichtbaar = true;
	boolean kijkNa = false;
	JComboBox terugleggenBox, trekkingenBox, optiesBox, labelsBox, kansVolgordeBox;
	JLabel aantalTrekkingen, aantalOptiesLabel, naamLetterLabel, zichtbaarLabel;
	JLabel[] /*naamOptie*/ optieLabel, aantalOptie, letterOptie;
	JTextField[] naamOptieVeld, aantalOptieVeld, letterOptieVeld;
	String[] letterString = new String[5];
	String[] aantalString;
	
	//startwaarden editpanel
	boolean kleur = true;
	boolean terugleggen = true;
	boolean letter = false;
	int aantalOpties = 2;
	int kansVolgordeKeuze = 0;
	int terugleggenKeuze = 0;
	int labelsKeuze = 0;
	
	String[] naamOptieTekst;
	
	boolean nakijkModelIngesteld = false;

public KansbomenInteractieEditPanel ()
{
	setLayout(null);
	kbip = new KansbomenInteractiePanel();
	kbip.setBounds(0,0,kbipBreedte,kbipHoogte);
	add(kbip);
	
	theFont = new Font("Dialog", Font.PLAIN, 12);
	theFM = getFontMetrics(theFont);
	theBoldFont = new Font("Dialog", Font.BOLD, 12);
	theBoldFM = getFontMetrics(theBoldFont);
	
	width = editWidth - 2 * offset;
	height = 3 * theFM.getHeight() / 2;
	currentX = kbip.getSize().width + offset;
	int currentX2 = offset;
	currentY = offset;
	
	String[] labelsKeuzes = { Kansbomen.rb.getString("geenLabelTekst"), Kansbomen.rb.getString("letterLabelTekst"), Kansbomen.rb.getString("kansLabelTekst")};
	labelsBox = new JComboBox(labelsKeuzes);
	labelsBox.setSelectedIndex(labelsKeuze);
	labelsBox.setFont(theFont);
	labelsBox.setBounds(currentX, currentY, width, height);
	add(labelsBox);
	labelsBox.addActionListener(this);
	
	currentY += height + 2 * offset;
	
	String[] kansVolgordeKeuzes = { Kansbomen.rb.getString("geenKansVolgordeTekst"), Kansbomen.rb.getString("kansTekst"), Kansbomen.rb.getString("volgordeTekst")};
	kansVolgordeBox = new JComboBox(kansVolgordeKeuzes);
	kansVolgordeBox.setSelectedIndex(kansVolgordeKeuze);
	kansVolgordeBox.setFont(theFont);
	kansVolgordeBox.setBounds(currentX, currentY, width, height);
	add(kansVolgordeBox);
	kansVolgordeBox.addActionListener(this);
	
	currentY += height + 2 * offset;
	
	kleurBox = new JCheckBox(Kansbomen.rb.getString("kleurTekst"), kleur);
	kleurBox.setFont(theFont);
	kleurBox.setBackground(Color.white);
	kleurBox.setBounds(currentX, currentY, width, height);
	add(kleurBox);
	kleurBox.addActionListener(this);
	
	currentY += height + offset;
	
	legendaZichtbaarBox = new JCheckBox(Kansbomen.rb.getString("legendaZichtbaarTekst"), legendaZichtbaar);
	legendaZichtbaarBox.setFont(theFont);
	legendaZichtbaarBox.setBackground(Color.white);
	legendaZichtbaarBox.setBounds(currentX, currentY, width, height);
	add(legendaZichtbaarBox);
	legendaZichtbaarBox.addActionListener(this);
	
	currentY += height + offset;
	
	bovenbalkZichtbaarBox = new JCheckBox(Kansbomen.rb.getString("bovenbalkZichtbaarTekst"), legendaZichtbaar);
	bovenbalkZichtbaarBox.setFont(theFont);
	bovenbalkZichtbaarBox.setBackground(Color.white);
	bovenbalkZichtbaarBox.setBounds(currentX, currentY, width, height);
	add(bovenbalkZichtbaarBox);
	bovenbalkZichtbaarBox.addActionListener(this);
	
	currentY += height + 2 * offset;
	
	naamLetterLabel = new JLabel(Kansbomen.rb.getString("naamLetterTekst"));
	naamLetterLabel.setFont(theBoldFont);
	naamLetterLabel.setBounds(currentX, currentY, width, height);
	add(naamLetterLabel);
	
	currentY += height + offset;
	
	optieLabel = new JLabel[5];
	for(int i=1; i<5; i++)
	{	optieLabel[i] = new JLabel(Kansbomen.rb.getString("optieTekst")+i+":"); 
		optieLabel[i].setFont(theFont);
		optieLabel[i].setBounds(currentX, currentY, 
				theFM.stringWidth(Kansbomen.rb.getString("optieTekst")+i+":"), height);
		add(optieLabel[i]);
		currentY += height + offset;
	}
	
	currentY -= 4 * height + 4 * offset;
	currentX += optieLabel[1].getWidth() + offset;
	
	naamOptieTekst = new String[5];
	naamOptieTekst[1] = Kansbomen.rb.getString("naam1StringTekst");
	naamOptieTekst[2] = Kansbomen.rb.getString("naam2StringTekst");
	naamOptieTekst[3] = Kansbomen.rb.getString("naam3StringTekst");
	naamOptieTekst[4] = Kansbomen.rb.getString("naam4StringTekst");
	
	naamOptieVeld = new JTextField[5];
	for(int i=1; i<5; i++)
	{ 	naamOptieVeld[i] = new JTextField(naamOptieTekst[i]);
		naamOptieVeld[i].setFont(theFont);
		naamOptieVeld[i].setBounds(currentX, currentY, width - 
				optieLabel[i].getWidth() - theFM.charWidth('m') - 4 * offset, height);
		add(naamOptieVeld[i]);
		naamOptieVeld[i].addActionListener(this);
		naamOptieVeld[i].addFocusListener(this);
		currentY += height + offset;
	}
		
	currentY -= 4 * height + 4 * offset;
	
	for(int i=1; i<5; i++)
		letterString[i] = naamOptieTekst[i].substring(0,1).toLowerCase();
	
	currentX += naamOptieVeld[1].getWidth() + offset;
	
	letterOptieVeld = new JTextField[5];
	for(int i=1; i<5; i++)
	{ 	letterOptieVeld[i] = new JTextField(letterString[i]);
		letterOptieVeld[i].setFont(theFont);
		letterOptieVeld[i].setBounds(currentX, currentY, theFM.charWidth('m') + 2 * offset, height);
		add(letterOptieVeld[i]);
		letterOptieVeld[i].addActionListener(this);
		letterOptieVeld[i].addFocusListener(this);
		currentY += height + offset;
	}	
	
	currentX -= optieLabel[1].getWidth() + naamOptieVeld[1].getWidth() + 2 * offset;
	
	zichtbaarLabel = new JLabel(Kansbomen.rb.getString("zichtbaarTekst"));
	zichtbaarLabel.setFont(theBoldFont);
	zichtbaarLabel.setBounds(currentX, currentY, width, height);
	add(zichtbaarLabel);
	
	currentY += height + offset;
	
	teruglegZichtbaarBox = new JCheckBox(Kansbomen.rb.getString("teruglegZichtbaarTekst"), teruglegZichtbaar);
	teruglegZichtbaarBox.setFont(theFont);
	teruglegZichtbaarBox.setBackground(Color.white);
	teruglegZichtbaarBox.setBounds(currentX, currentY, width, height);
	add(teruglegZichtbaarBox);
	teruglegZichtbaarBox.addActionListener(this);
	
	currentY += height + offset;
	
	trekkingZichtbaarBox = new JCheckBox(Kansbomen.rb.getString("trekkingZichtbaarTekst"), trekkingZichtbaar);
	trekkingZichtbaarBox.setFont(theFont);
	trekkingZichtbaarBox.setBackground(Color.white);
	trekkingZichtbaarBox.setBounds(currentX, currentY, width, height);
	add(trekkingZichtbaarBox);
	trekkingZichtbaarBox.addActionListener(this);
	
	currentY += height + offset;
	
	optiesZichtbaarBox = new JCheckBox(Kansbomen.rb.getString("optiesZichtbaarTekst"), optiesZichtbaar);
	optiesZichtbaarBox.setFont(theFont);
	optiesZichtbaarBox.setBackground(Color.white);
	optiesZichtbaarBox.setBounds(currentX, currentY, width, height);
	add(optiesZichtbaarBox);
	optiesZichtbaarBox.addActionListener(this);
	
	currentY += height + offset;
	
	ballenZichtbaarBox = new JCheckBox(Kansbomen.rb.getString("ballenZichtbaarTekst"), ballenZichtbaar);
	ballenZichtbaarBox.setFont(theFont);
	ballenZichtbaarBox.setBackground(Color.white);
	ballenZichtbaarBox.setBounds(currentX, currentY, width, height);
	add(ballenZichtbaarBox);
	ballenZichtbaarBox.addActionListener(this);
	
	currentY += height + 2 * offset;
	
	kijkNaBox = new JCheckBox(Kansbomen.rb.getString("kijkNaTekst"), kijkNa);
	kijkNaBox.setFont(theFont);
	kijkNaBox.setBackground(Color.white);
	kijkNaBox.setBounds(currentX, currentY, 70, height);
	add(kijkNaBox);
	kijkNaBox.addActionListener(this);
	
	nakijkModelKnop = new JButton(Kansbomen.rb.getString("nakijkModelTekst"));
	nakijkModelKnop.setFont(theFont);
	nakijkModelKnop.setBounds(currentX + kijkNaBox.getWidth() + offset, currentY,
			width - kijkNaBox.getWidth() - offset, height);
	add(nakijkModelKnop);
	nakijkModelKnop.addActionListener(this);
	nakijkModelKnop.setVisible(kijkNa);
	
	componentsCreated = true;
	
}


public void plaatsComponenten()
{
	if (componentsCreated)
	{
		labelsBox.setLocation(kbip.getSize().width + offset, labelsBox.getLocation().y);
		kleurBox.setLocation(kbip.getSize().width + offset, kleurBox.getLocation().y);
		kansVolgordeBox.setLocation(kbip.getSize().width + offset, kansVolgordeBox.getLocation().y);
		naamLetterLabel.setLocation(kbip.getSize().width + offset, naamLetterLabel.getLocation().y);
		for(int i=1; i<5; i++)
		{
			optieLabel[i].setLocation(kbip.getSize().width + offset, optieLabel[i].getLocation().y);
			naamOptieVeld[i].setLocation(kbip.getSize().width + optieLabel[i].getSize().width + 2 * offset, naamOptieVeld[i].getLocation().y);
			letterOptieVeld[i].setLocation(kbip.getSize().width + optieLabel[i].getSize().width 
					+ naamOptieVeld[i].getSize().width + 3 * offset, letterOptieVeld[i].getLocation().y);
		}
		zichtbaarLabel.setLocation(kbip.getSize().width + offset, zichtbaarLabel.getLocation().y);
		teruglegZichtbaarBox.setLocation(kbip.getSize().width + offset, teruglegZichtbaarBox.getLocation().y);
		trekkingZichtbaarBox.setLocation(kbip.getSize().width + offset, trekkingZichtbaarBox.getLocation().y);
		optiesZichtbaarBox.setLocation(kbip.getSize().width + offset, optiesZichtbaarBox.getLocation().y);
		ballenZichtbaarBox.setLocation(kbip.getSize().width + offset, ballenZichtbaarBox.getLocation().y);
		legendaZichtbaarBox.setLocation(kbip.getSize().width + offset, legendaZichtbaarBox.getLocation().y);
		bovenbalkZichtbaarBox.setLocation(kbip.getSize().width + offset, bovenbalkZichtbaarBox.getLocation().y);
		kijkNaBox.setLocation(kbip.getSize().width + offset, kijkNaBox.getLocation().y);
		nakijkModelKnop.setLocation(kbip.getSize().width + kijkNaBox.getWidth() + 2 * offset, nakijkModelKnop.getLocation().y);
		
	}
}


public void actionPerformed(ActionEvent e) 

{
	if(e.getSource() == kleurBox)
	{	kleur = kleurBox.isSelected();
		kbip.zetKleur(kleur);
	}
	else if(e.getSource() == kansVolgordeBox)
	{	kansVolgordeKeuze = kansVolgordeBox.getSelectedIndex();
		kbip.zetKansVolgorde(kansVolgordeKeuze);
	}
		
	else if(e.getSource() == labelsBox)
	{	labelsKeuze=labelsBox.getSelectedIndex();
		kbip.zetLabelsKeuze(labelsKeuze);	
	}
	
	else if(e.getSource() == naamOptieVeld[1])
	{
		if(naamOptieVeld[1].getText().isEmpty())
			naamOptieVeld[1].setText(naamOptieTekst[1]);
		else
		{
		naamOptieTekst[1] = naamOptieVeld[1].getText();
		letterString[1] = naamOptieTekst[1].substring(0,1).toLowerCase();
		letterOptieVeld[1].setText(letterString[1]);
		kbip.zetNaamOptie(1,naamOptieTekst[1]);
		}
		
	}
	else if(e.getSource() == naamOptieVeld[2])
	{
		if(naamOptieVeld[2].getText().isEmpty())
			naamOptieVeld[2].setText(naamOptieTekst[2]);
		else
		{
		naamOptieTekst[2] = naamOptieVeld[2].getText();
		letterString[2] = naamOptieTekst[2].substring(0,1).toLowerCase();
		letterOptieVeld[2].setText(letterString[2]);
		kbip.zetNaamOptie(2,naamOptieTekst[2]);
		}
	}
	else if(e.getSource() == naamOptieVeld[3])
	{
		if(naamOptieVeld[3].getText().isEmpty())
			naamOptieVeld[3].setText(naamOptieTekst[3]);
		else
		{
		naamOptieTekst[3] = naamOptieVeld[3].getText();
		letterString[3] = naamOptieTekst[3].substring(0,1).toLowerCase();
		letterOptieVeld[3].setText(letterString[3]);
		kbip.zetNaamOptie(3,naamOptieTekst[3]);
		}
	}
	else if(e.getSource() == naamOptieVeld[4])
	{
		if(naamOptieVeld[4].getText().isEmpty())
			naamOptieVeld[4].setText(naamOptieTekst[4]);
		else
		{
		naamOptieTekst[4] = naamOptieVeld[4].getText();
		letterString[4] = naamOptieTekst[4].substring(0,1).toLowerCase();
		letterOptieVeld[4].setText(letterString[4]);
		kbip.zetNaamOptie(4,naamOptieTekst[4]);
		}
	}
	else if(e.getSource() == letterOptieVeld[1])
	{
		if(letterOptieVeld[1].getText().length() < 3)
		{	letterString[1] = letterOptieVeld[1].getText();
			kbip.zetLetterOptie(1,letterString[1]);
		}
		else
			letterOptieVeld[1].setText(letterString[1]);
	}
	else if(e.getSource() == letterOptieVeld[2])
	{
		if(letterOptieVeld[2].getText().length() < 3)
		{	letterString[2] = letterOptieVeld[2].getText();
			kbip.zetLetterOptie(2,letterString[2]);
		}
		else
			letterOptieVeld[2].setText(letterString[2]);
	}
	else if(e.getSource() == letterOptieVeld[3])
	{
		if(letterOptieVeld[3].getText().length() < 3)
		{	letterString[3] = letterOptieVeld[3].getText();
			kbip.zetLetterOptie(3,letterString[3]);
		}
		else
			letterOptieVeld[3].setText(letterString[3]);
	}
	else if(e.getSource() == letterOptieVeld[4])
	{
		if(letterOptieVeld[4].getText().length() < 3)
		{	letterString[4] = letterOptieVeld[4].getText();
			kbip.zetLetterOptie(4,letterString[4]);
		}
		else
			letterOptieVeld[4].setText(letterString[4]);
	}
	else if(e.getSource() == teruglegZichtbaarBox)
	{	teruglegZichtbaar = teruglegZichtbaarBox.isSelected();
		kbip.zetTeruglegZichtbaar(teruglegZichtbaar);
	}
	else if(e.getSource() == trekkingZichtbaarBox)
	{	trekkingZichtbaar = trekkingZichtbaarBox.isSelected();
		kbip.zetTrekkingZichtbaar(trekkingZichtbaar);
	}
	else if(e.getSource() == optiesZichtbaarBox)
	{	optiesZichtbaar = optiesZichtbaarBox.isSelected();
		kbip.zetOptiesZichtbaar(optiesZichtbaar);
	}
	else if(e.getSource() == ballenZichtbaarBox)
	{	ballenZichtbaar = ballenZichtbaarBox.isSelected();
		kbip.zetBallenZichtbaar(ballenZichtbaar);
	}	
	
	else if(e.getSource() == legendaZichtbaarBox)
	{	legendaZichtbaar = legendaZichtbaarBox.isSelected();
		kbip.zetLegendaZichtbaar(legendaZichtbaar);
	}	
	
	else if(e.getSource() == bovenbalkZichtbaarBox)
	{	bovenbalkZichtbaar = bovenbalkZichtbaarBox.isSelected();
		kbip.zetBovenbalkZichtbaar(bovenbalkZichtbaar);
	}	
	
	else if(e.getSource() == kijkNaBox)
	{	kijkNa = kijkNaBox.isSelected();
		nakijkModelKnop.setVisible(kijkNa);
		kbip.zetKijkNa(kijkNa);
	}
	
	else if(e.getSource() == nakijkModelKnop)
	{
		
	}
}

public void focusLost(FocusEvent e) 
{
	if(e.getSource() == naamOptieVeld[1])
	{
		if(naamOptieVeld[1].getText().isEmpty())
			naamOptieVeld[1].setText(naamOptieTekst[1]);
		else
		{
		naamOptieTekst[1] = naamOptieVeld[1].getText();
		letterString[1] = naamOptieTekst[1].substring(0,1).toLowerCase();
		letterOptieVeld[1].setText(letterString[1]);
		kbip.zetNaamOptie(1,naamOptieTekst[1]);
		}
		
	}
	else if(e.getSource() == naamOptieVeld[2])
	{
		if(naamOptieVeld[2].getText().isEmpty())
			naamOptieVeld[2].setText(naamOptieTekst[2]);
		else
		{
		naamOptieTekst[2] = naamOptieVeld[2].getText();
		letterString[2] = naamOptieTekst[2].substring(0,1).toLowerCase();
		letterOptieVeld[2].setText(letterString[2]);
		kbip.zetNaamOptie(2,naamOptieTekst[2]);
		}
	}
	else if(e.getSource() == naamOptieVeld[3])
	{
		if(naamOptieVeld[3].getText().isEmpty())
			naamOptieVeld[3].setText(naamOptieTekst[3]);
		else
		{
		naamOptieTekst[3] = naamOptieVeld[3].getText();
		letterString[3] = naamOptieTekst[3].substring(0,1).toLowerCase();
		letterOptieVeld[3].setText(letterString[3]);
		kbip.zetNaamOptie(3,naamOptieTekst[3]);
		}
	}
	else if(e.getSource() == naamOptieVeld[4])
	{
		if(naamOptieVeld[4].getText().isEmpty())
			naamOptieVeld[4].setText(naamOptieTekst[4]);
		else
		{
		naamOptieTekst[4] = naamOptieVeld[4].getText();
		letterString[4] = naamOptieTekst[4].substring(0,1).toLowerCase();
		letterOptieVeld[4].setText(letterString[4]);
		kbip.zetNaamOptie(4,naamOptieTekst[4]);
		}
	}
	else if(e.getSource() == letterOptieVeld[1])
	{
		if(letterOptieVeld[1].getText().length() < 3)
		{	letterString[1] = letterOptieVeld[1].getText();
			kbip.zetLetterOptie(1,letterString[1]);
		}
		else
			letterOptieVeld[1].setText(letterString[1]);
	}
	else if(e.getSource() == letterOptieVeld[2])
	{
		if(letterOptieVeld[2].getText().length() < 3)
		{	letterString[2] = letterOptieVeld[2].getText();
			kbip.zetLetterOptie(2,letterString[2]);
		}
		else
			letterOptieVeld[2].setText(letterString[2]);
	}
	else if(e.getSource() == letterOptieVeld[3])
	{
		if(letterOptieVeld[3].getText().length() < 3)
		{	letterString[3] = letterOptieVeld[3].getText();
			kbip.zetLetterOptie(3,letterString[3]);
		}
		else
			letterOptieVeld[3].setText(letterString[3]);
	}
	else if(e.getSource() == letterOptieVeld[4])
	{
		if(letterOptieVeld[4].getText().length() < 3)
		{	letterString[4] = letterOptieVeld[4].getText();
			kbip.zetLetterOptie(4,letterString[4]);
		}
		else
			letterOptieVeld[4].setText(letterString[4]);
	}
	
}




public void setEditState(Hashtable h) {

	if (h.containsKey("teruglegZichtbaar"))
		teruglegZichtbaar = ((Boolean) h.get("teruglegZichtbaar")).booleanValue();
	teruglegZichtbaarBox.setSelected(teruglegZichtbaar);
	
	if (h.containsKey("trekkingZichtbaar"))
		trekkingZichtbaar = ((Boolean) h.get("trekkingZichtbaar")).booleanValue();
	trekkingZichtbaarBox.setSelected(trekkingZichtbaar);
	
	if (h.containsKey("optiesZichtbaar"))
		optiesZichtbaar = ((Boolean) h.get("optiesZichtbaar")).booleanValue();
	optiesZichtbaarBox.setSelected(optiesZichtbaar);
	
	if (h.containsKey("ballenZichtbaar"))
		ballenZichtbaar = ((Boolean) h.get("ballenZichtbaar")).booleanValue();
	ballenZichtbaarBox.setSelected(ballenZichtbaar);
	
	if (h.containsKey("legendaZichtbaar"))
		legendaZichtbaar = ((Boolean) h.get("legendaZichtbaar")).booleanValue();
	legendaZichtbaarBox.setSelected(legendaZichtbaar);
	
	if (h.containsKey("bovenbalkZichtbaar"))
		bovenbalkZichtbaar = ((Boolean) h.get("bovenbalkZichtbaar")).booleanValue();
	bovenbalkZichtbaarBox.setSelected(bovenbalkZichtbaar);
	
	if (h.containsKey("kijkNa"))
		kijkNa = ((Boolean) h.get("kijkNa")).booleanValue();
	kijkNaBox.setSelected(kijkNa);
	nakijkModelKnop.setVisible(kijkNa);
	
	if (h.containsKey("labelsKeuze"))
		labelsKeuze = ((Integer) h.get("labelsKeuze")).intValue();
	labelsBox.setSelectedIndex(labelsKeuze);
	
	if (h.containsKey("kleur"))
		kleur = ((Boolean) h.get("kleur")).booleanValue();
	kleurBox.setSelected(kleur);
	
	if (h.containsKey("kansVolgordeKeuze"))
		kansVolgordeKeuze = ((Integer) h.get("kansVolgordeKeuze")).intValue();
	kansVolgordeBox.setSelectedIndex(kansVolgordeKeuze);
	
	if (h.containsKey("naamOptieTekst[1]"))
		naamOptieTekst[1] = ((String) h.get("naamOptieTekst[1]"));
	naamOptieVeld[1].setText(naamOptieTekst[1]);
		
	if (h.containsKey("naamOptieTekst[2]"))
		naamOptieTekst[2] = ((String) h.get("naamOptieTekst[2]"));
	naamOptieVeld[2].setText(naamOptieTekst[2]);
		
	if (h.containsKey("naamOptieTekst[3]"))
		naamOptieTekst[3] = ((String) h.get("naamOptieTekst[3]"));
	naamOptieVeld[3].setText(naamOptieTekst[3]);
		
	if (h.containsKey("naamOptieTekst[4]"))
		naamOptieTekst[4] = ((String) h.get("naamOptieTekst[4]"));
	naamOptieVeld[4].setText(naamOptieTekst[4]);
		
	if (h.containsKey("letterString[1]"))
		letterString[1] = ((String) h.get("letterString[1]"));
	letterOptieVeld[1].setText(letterString[1]);
	
	if (h.containsKey("letterString[2]"))
		letterString[2] = ((String) h.get("letterString[2]"));
	letterOptieVeld[2].setText(letterString[2]);
	
	if (h.containsKey("letterString[3]"))
		letterString[3] = ((String) h.get("letterString[3]"));
	letterOptieVeld[3].setText(letterString[3]);
	
	if (h.containsKey("letterString[4]"))
		letterString[4] = ((String) h.get("letterString[4]"));
	letterOptieVeld[4].setText(letterString[4]);
	
	
	
	
	if (h.containsKey("kbipBreedte"))
		kbipBreedte = ((Integer) h.get("kbipBreedte")).intValue();
	if (h.containsKey("kbipHoogte"))
		kbipHoogte = ((Integer) h.get("kbipHoogte")).intValue();
	
	setBounds(getLocation().x, getLocation().y, kbipBreedte + editWidth, Math.max(kbipHoogte, editHeight));
	
	// HIER !!
	kbip.setEditState(h);		

	
}




public Hashtable getEditState() {

	Hashtable h = kbip.getEditState();
	
	
	h.put("kbipBreedte", new Integer(kbipBreedte));
	h.put("kbipHoogte", new Integer(kbipHoogte));
	
		
	
	return h;
}


public void setBounds(int x, int y, int b, int h)
{
//	if (noSetBounds)
//	{	noSetBounds = false;
//		return;
//	}
//System.out.println("spiep setBounds raw " + x + " " + y + " " + b + " " + h);

	if ((h <= 1) || (x < 0) || (b <= 1))
		return;
	
	super.setBounds(x, y, kbipBreedte + editWidth, Math.max(kbipHoogte, editHeight));
	
//System.out.println("spiep setBounds " + x + " " + y + " " + (spipBreedte + editWidth) + " " + 
//				Math.max(spipHoogte, editHeight));

	if (kbip != null)
		kbip.setBounds(0, 0, kbipBreedte, kbipHoogte);
	
	plaatsComponenten();
	
//System.out.println("setBounds " + x + " " + y + " " + b + " " + h);		
	
}


public void zetBreedte(int b)
{	
	kbipBreedte = b;
	
	setBounds(getLocation().x, getLocation().y, kbipBreedte + editWidth, Math.max(kbipHoogte, editHeight));		
	plaatsComponenten();
}

public void zetHoogte(int h)
{	
	kbipHoogte = h;
	
	setBounds(getLocation().x, getLocation().y, kbipBreedte + editWidth, Math.max(kbipHoogte, editHeight));		
}

public Hashtable setInitialCheckState()
{
	int terugleggenKeuze = 0;
	int trekkingen = 2;
	int aantalOpties = 2;
	int[] aantalInt = {4,4,4,4,4};
	
	terugleggenKeuze = kbip.terugleggenKeuze;
	trekkingen = kbip.trekkingen;
	aantalOpties = kbip.aantalOpties;
	aantalInt[1] = kbip.aantalInt[1];
	aantalInt[2] = kbip.aantalInt[2];
	aantalInt[3] = kbip.aantalInt[3];
	aantalInt[4] = kbip.aantalInt[4];
	
	Hashtable h = new Hashtable();
	
	h.put("terugleggenKeuze", terugleggenKeuze);
	h.put("trekkingen", trekkingen);
	h.put("aantalOpties", aantalOpties);
	h.put("aantalInt[1]", aantalInt[1]);
	h.put("aantalInt[2]", aantalInt[2]);
	h.put("aantalInt[3]", aantalInt[3]);
	h.put("aantalInt[4]", aantalInt[4]);
	
	nakijkModelIngesteld = true;
	
	//en nu moet ergens, bij het indrukken van de Nakijkmodel-knop, iets als
	//if(!nakijkModelIngesteld)
	//zetNakijkModel(setInitialCheckState());
	//else
	//??? Hoe onthoud ik de staat van dat nakijkmodel? 
	
	return h;
}




public void wis() {
	// TODO Auto-generated method stub
	
}




public void zetMode(int mode) {
	// TODO Auto-generated method stub
	
}




public void stop() {
	// TODO Auto-generated method stub
	
}




public void start() {
	// TODO Auto-generated method stub
	
}




public void addActionListener(ActionListener al) {
	// TODO Auto-generated method stub
	
}


public void focusGained(FocusEvent arg0) {
		
}
	
	
}
