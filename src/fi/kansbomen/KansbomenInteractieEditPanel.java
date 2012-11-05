package fi.kansbomen;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.*;

import javax.swing.*;


public class KansbomenInteractieEditPanel extends JPanel implements ActionListener

{

	int editWidth = 190;
	int editHeight = 450; 
	int kbipBreedte = 600; // startbreedte ip
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
	
	//boolean componentsCreated = false;
	//boolean noSetBounds = false;	

	protected KansbomenInteractiePanel kbip; //Ik weet niet waarom protected.
	JCheckBox kleurBox, volgordeBox;
	JCheckBox teruglegZichtbaarBox, trekkingZichtbaarBox, optiesZichtbaarBox, ballenZichtbaarBox;
	boolean teruglegZichtbaar = true;
	boolean trekkingZichtbaar = true;
	boolean optiesZichtbaar = true;
	boolean ballenZichtbaar = true;
	JComboBox terugleggenBox, trekkingenBox, optiesBox, labelsBox;
	JLabel aantalTrekkingen, aantalOpties, zichtbaarLabel;
	JLabel[] naamOptie, aantalOptie, letterOptie;
	JTextField[] naamOptieVeld, aantalOptieVeld, letterOptieVeld;
	String[] namenString, letterString;
	String[] aantalString;
	
	//startwaarden editpanel
	boolean kleur = true;
	boolean volgorde = true;
	boolean terugleggen = true;
	boolean letter = false;
	int terugleggenKeuze = 0;
	int labelsKeuze = 0;
	int trekkingen = 2;
	String naam1String = Kansbomen.rb.getString("naam1StringTekst");
	String naam2String = Kansbomen.rb.getString("naam2StringTekst");
	String naam3String = Kansbomen.rb.getString("naam3StringTekst");
	String naam4String = Kansbomen.rb.getString("naam4StringTekst");	
	String aantal1String = "4";
	String aantal2String = "4";
	String aantal3String = "4";
	String aantal4String = "4";
	String letterOptie1, letterOptie2, letterOptie3, letterOptie4;
	int aantal1Int = 4;
	int aantal2Int = 4;
	int aantal3Int = 4;
	int aantal4Int = 4;
	
/*Wat heb ik nodig in mijn interactie-editpanel? 
- Aanvinkvakje keuze aan leerling voor met/zonder terugleggen, aantal keuzes, aantal keuzemomenten.
*/

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
	
	kleurBox = new JCheckBox(Kansbomen.rb.getString("kleurTekst"), kleur);
	kleurBox.setFont(theFont);
	kleurBox.setBackground(Color.white);
	kleurBox.setBounds(currentX, currentY, width, height);
	add(kleurBox);
	kleurBox.addActionListener(this);
	
	currentY += height + 2 * offset;
	
	volgordeBox = new JCheckBox(Kansbomen.rb.getString("volgordeTekst"), volgorde);
	volgordeBox.setFont(theFont);
	volgordeBox.setBackground(Color.white);
	volgordeBox.setBounds(currentX, currentY, width, height);
	add(volgordeBox);
	volgordeBox.addActionListener(this);
	
	currentY += height + 2 * offset;
	
	
	naamOptie = new JLabel[5];
	for(int i=1; i<5; i++)
	{	naamOptie[i] = new JLabel(Kansbomen.rb.getString("naamOptieTekst")+i+":"); 
		naamOptie[i].setFont(theFont);
		naamOptie[i].setBounds(currentX, currentY, 80, height);//liever geen 80
		add(naamOptie[i]);
		currentY += 2* height + 2* offset;
	}
		
	currentY -= 8 * height + 8 * offset;
	
	namenString = new String[5];
	namenString[1] = naam1String;
	namenString[2] = naam2String;
	namenString[3] = naam3String;
	namenString[4] = naam4String;
	
	naamOptieVeld = new JTextField[5];
	for(int i=1; i<5; i++)
	{ 	currentX += naamOptie[i].getWidth()+ offset;
		naamOptieVeld[i] = new JTextField(namenString[i]);
		naamOptieVeld[i].setFont(theFont);
		naamOptieVeld[i].setBounds(currentX, currentY, width - naamOptie[i].getWidth() - offset, height);
		add(naamOptieVeld[i]);
		naamOptieVeld[i].addActionListener(this);
		currentY += 2* height + 2* offset;
		currentX -= naamOptie[i].getWidth()+ offset;
	}
		
	currentY -= 7 * height + 7 * offset;
	
	letterOptie = new JLabel[5];
	for(int i=1; i<5; i++)
	{	letterOptie[i] = new JLabel(Kansbomen.rb.getString("letterOptieTekst")+i+":"); 
		letterOptie[i].setFont(theFont);
		letterOptie[i].setBounds(currentX, currentY, 80, height);//liever geen 80
		add(letterOptie[i]);
		currentY += 2* height + 2* offset;
	}
		
	currentY -= 8 * height + 8 * offset;
	
	letterString = new String[5];
	letterString[1] = naam1String.substring(0,1).toLowerCase();
	letterString[2] = naam2String.substring(0,1).toLowerCase();
	letterString[3] = naam3String.substring(0,1).toLowerCase();
	letterString[4] = naam4String.substring(0,1).toLowerCase();
		
	letterOptieVeld = new JTextField[5];
	for(int i=1; i<5; i++)
	{ 	currentX += letterOptie[i].getWidth()+ offset;
		letterOptieVeld[i] = new JTextField(letterString[i]);
		letterOptieVeld[i].setFont(theFont);
		letterOptieVeld[i].setBounds(currentX, currentY, theFM.charWidth('m') +2* offset, height);
		add(letterOptieVeld[i]);
		letterOptieVeld[i].addActionListener(this);
		currentY += 2* height + 2* offset;
		currentX -= naamOptie[i].getWidth()+ offset;
	}
		
	currentY -= height + offset;
	
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
	
	
	
	
	//componentsCreated=true;
	
}




public void actionPerformed(ActionEvent e) 

{
	if(e.getSource() == kleurBox)
	{	kleur = kleurBox.isSelected();
		kbip.zetKleur(kleur);
	}
	else if(e.getSource() == volgordeBox)
	{	volgorde = volgordeBox.isSelected();
		kbip.zetVolgorde(volgorde);
	}
		
	else if(e.getSource() == labelsBox)
	{	labelsKeuze=labelsBox.getSelectedIndex();
		kbip.zetLabelsKeuze(labelsKeuze);	
	}
	
	else if(e.getSource() == trekkingenBox)
	{	trekkingen = trekkingenBox.getSelectedIndex();
		kbip.zetKeuzeMomenten(trekkingen+1);
	}
	else if(e.getSource() == naamOptieVeld[1])
	{
		naam1String = naamOptieVeld[1].getText();
		letterString[1] = naam1String.substring(0,1).toLowerCase();
		letterOptieVeld[1].setText(letterString[1]);
		kbip.naamOptieTekst[1] = naam1String;
		kbip.aantalOptie[1].setText(Kansbomen.rb.getString("aantalTekst")+naam1String+":");
		kbip.updateLegendaTekst(1);
		letterString[1]=naam1String.substring(0,1).toLowerCase();
		kbip.kansboom.letter1 = letterString[1];
		kbip.kansboom.repaint();
	}
	else if(e.getSource() == naamOptieVeld[2])
	{
		naam2String = naamOptieVeld[2].getText();
		letterString[2] = naam2String.substring(0,1).toLowerCase();
		letterOptieVeld[2].setText(letterString[2]);
		kbip.naamOptieTekst[2] = naam2String;
		kbip.aantalOptie[2].setText(Kansbomen.rb.getString("aantalTekst")+naam2String+":");
		kbip.updateLegendaTekst(2);
		letterString[2]=naam2String.substring(0,1).toLowerCase();
		kbip.kansboom.letter2 = letterString[2];
		kbip.kansboom.repaint();
	}
	else if(e.getSource() == naamOptieVeld[3])
	{
		naam3String = naamOptieVeld[3].getText();
		letterString[3] = naam3String.substring(0,1).toLowerCase();
		letterOptieVeld[3].setText(letterString[3]);
		kbip.naamOptieTekst[3] = naam3String;
		kbip.aantalOptie[3].setText(Kansbomen.rb.getString("aantalTekst")+naam3String+":");
		kbip.updateLegendaTekst(3);
		letterString[3]=naam3String.substring(0,1).toLowerCase();
		kbip.kansboom.letter3 = letterString[3];
		kbip.kansboom.repaint();
	}
	else if(e.getSource() == naamOptieVeld[4])
	{
		naam4String = naamOptieVeld[4].getText();
		letterString[4] = naam4String.substring(0,1).toLowerCase();
		letterOptieVeld[4].setText(letterString[4]);
		kbip.naamOptieTekst[4] = naam4String;
		kbip.aantalOptie[4].setText(Kansbomen.rb.getString("aantalTekst")+naam4String+":");
		kbip.updateLegendaTekst(4);
		letterString[4]=naam4String.substring(0,1).toLowerCase();
		kbip.kansboom.letter4 = letterString[4];
		kbip.kansboom.repaint();
	}
	else if(e.getSource() == letterOptieVeld[1])
	{
		if(letterOptieVeld[1].getText().length() < 3)
		{	letterString[1] = letterOptieVeld[1].getText();
			kbip.kansboom.letter1 = letterString[1];
			kbip.kansboom.repaint();
		}
		else
			letterOptieVeld[1].setText(letterString[1]);
	}
	else if(e.getSource() == letterOptieVeld[2])
	{
		if(letterOptieVeld[2].getText().length() < 3)
		{	letterString[2] = letterOptieVeld[2].getText();
			kbip.kansboom.letter2 = letterString[2];
			kbip.kansboom.repaint();
		}
		else
			letterOptieVeld[2].setText(letterString[2]);
	}
	else if(e.getSource() == letterOptieVeld[3])
	{
		if(letterOptieVeld[3].getText().length() < 3)
		{	letterString[3] = letterOptieVeld[3].getText();
			kbip.kansboom.letter3 = letterString[3];
			kbip.kansboom.repaint();
		}
		else
			letterOptieVeld[3].setText(letterString[3]);
	}
	else if(e.getSource() == letterOptieVeld[4])
	{
		if(letterOptieVeld[4].getText().length() < 3)
		{	letterString[4] = letterOptieVeld[4].getText();
			kbip.kansboom.letter4 = letterString[4];
			kbip.kansboom.repaint();
		}
		else
			letterOptieVeld[4].setText(letterString[4]);
	}
	else if(e.getSource() == teruglegZichtbaarBox)
	{	teruglegZichtbaar = teruglegZichtbaarBox.isSelected();
		kbip.terugleggenBox.setVisible(teruglegZichtbaar);
	}
	else if(e.getSource() == trekkingZichtbaarBox)
	{	trekkingZichtbaar = trekkingZichtbaarBox.isSelected();
		kbip.aantalTrekkingen.setVisible(trekkingZichtbaar);
		kbip.trekkingenBox.setVisible(trekkingZichtbaar);
	}
	else if(e.getSource() == optiesZichtbaarBox)
	{	optiesZichtbaar = optiesZichtbaarBox.isSelected();
		kbip.aantalOpties.setVisible(optiesZichtbaar);
		kbip.optiesBox.setVisible(optiesZichtbaar);
	}
	else if(e.getSource() == ballenZichtbaarBox)
	{	ballenZichtbaar = ballenZichtbaarBox.isSelected();
		kbip.aantalOptie[1].setVisible(ballenZichtbaar);
		kbip.aantalOptieVeld[1].setVisible(ballenZichtbaar);
		kbip.aantalOptie[2].setVisible(ballenZichtbaar);
		kbip.aantalOptieVeld[2].setVisible(ballenZichtbaar);
		if(kbip.aantalOptiesKeuze+2>2)
		{	kbip.aantalOptie[3].setVisible(ballenZichtbaar);
			kbip.aantalOptieVeld[3].setVisible(ballenZichtbaar);
		}	
		if(kbip.aantalOptiesKeuze+2>3)
		{	kbip.aantalOptie[4].setVisible(ballenZichtbaar);
			kbip.aantalOptieVeld[4].setVisible(ballenZichtbaar);
		}
	}	
}
	
	
}
