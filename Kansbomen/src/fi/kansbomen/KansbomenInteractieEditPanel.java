package fi.kansbomen;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import fi.beans.wiskopdrbeans.InteractieEditPanel;


public class KansbomenInteractieEditPanel extends JPanel implements ActionListener, FocusListener, InteractieEditPanel

{
	int editWidth = 190;
	int editHeight = 550; 
	int kbipBreedte = 500; // startbreedte ip
	int kbipHoogte = 450; // starthoogte ip
	
	Font theFont;
	FontMetrics theFM;
	Font theBoldFont;
	FontMetrics theBoldFM;
	
	int offset = 4;
	int offset2 = 1;
	int currentX;
	int currentY;
	int width;
	int height;
	
	boolean componentsCreated = false;
	//boolean noSetBounds = false;	

	protected KansbomenInteractiePanel kbip; //Ik weet niet waarom protected.
	JCheckBox kleurBox;
	JCheckBox teruglegZichtbaarBox, trekkingZichtbaarBox, optiesZichtbaarBox, 
				ballenZichtbaarBox, legendaZichtbaarBox, bovenbalkZichtbaarBox, aantallenZichtbaarBox;
	JCheckBox kijkNaBox, externControlerenBox;
	//NakijkModelButton nakijkModelButton;
	boolean teruglegZichtbaar = true;
	boolean trekkingZichtbaar = true;
	boolean optiesZichtbaar = true;
	boolean ballenZichtbaar = true;
	boolean legendaZichtbaar = true;
	boolean bovenbalkZichtbaar = true;
	boolean aantallenZichtbaar = true;
	boolean kijkNa = false;
	boolean checkExternal = false;
	JComboBox terugleggenBox, trekkingenBox, optiesBox, labelsBox, kansVolgordeBox;
	JLabel aantalTrekkingen, aantalOptiesLabel, naamLetterLabel, zichtbaarLabel, bovenbalkLabel, bovenbalkMvLabel;
	JTextField bovenbalkVeld, bovenbalkMvVeld;
	JLabel[] optieLabel, aantalOptie, letterOptie;
	JTextField[] naamOptieVeld, aantalOptieVeld, letterOptieVeld;
	String[] letterString = new String[6];
	String[] aantalString;
	String trekkingTekst = Kansbomen.rb.getString("trekkingBalkTekst");
	String trekkingMvTekst = Kansbomen.rb.getString("trekkingBalkTekstMv");
	
	//startwaarden editpanel
	boolean kleur = true;
	boolean terugleggen = true;
	boolean letter = false;
	int aantalOpties = 4;
	int kansVolgordeKeuze = 0;
	int terugleggenKeuze = 0;
	int trekkingen = 3;
	int labelsKeuze = 0;
	int[] aantalInt = new int[] {4,4,4,4,4,4};
	
	String[] naamOptieTekst;
	
	boolean nakijkModelIngesteld = false;
	int scoreMax;
	int[] nakijkModel = new int[] {10, 4, 4, 4, 4, 4, 4, 0, 3, 4};
	
	JTextField maxScoreVeld;
	JLabel maxScoreLabel;
	
	JTabbedPane tabbedPane;
	
	JPanel viewerOptiesPanel, nakijkOptiesPanel;
	
	

public KansbomenInteractieEditPanel ()
{
	setLayout(null);
	kbip = new KansbomenInteractiePanel();
	kbip.setBounds(0,0,kbipBreedte,kbipHoogte);
	add(kbip);
	
	tabbedPane = new JTabbedPane();
	
	viewerOptiesPanel = new JPanel();
	viewerOptiesPanel.setLayout(null);
	tabbedPane.addTab(Kansbomen.rb.getString("viewerOpties"), viewerOptiesPanel);
	
	nakijkOptiesPanel = new JPanel();
	nakijkOptiesPanel.setLayout(null);
	tabbedPane.addTab(Kansbomen.rb.getString("nakijkOpties"), nakijkOptiesPanel);
	
	tabbedPane.setBounds(getSize().width - editWidth, 0, editWidth, editHeight);
	add(tabbedPane);
	
	//tabbedPane.addChangeListener(new TabbedPaneCL());
	
	theFont = new Font("Dialog", Font.PLAIN, 12);
	theFM = getFontMetrics(theFont);
	theBoldFont = new Font("Dialog", Font.BOLD, 12);
	theBoldFM = getFontMetrics(theBoldFont);
	
	width = editWidth - 2 * offset;
	height = 3 * theFM.getHeight() / 2;
	currentX = offset;
	currentY = offset;
	
	String[] labelsKeuzes = { Kansbomen.rb.getString("geenLabelTekst"), Kansbomen.rb.getString("letterLabelTekst"), 
			Kansbomen.rb.getString("kansNaastLabelTekst"), Kansbomen.rb.getString("kansOnderLabelTekst")};
	labelsBox = new JComboBox(labelsKeuzes);
	labelsBox.setOpaque(false);
	labelsBox.setSelectedIndex(labelsKeuze);
	labelsBox.setFont(theFont);
	labelsBox.setBounds(currentX, currentY, width, height);
	viewerOptiesPanel.add(labelsBox);
	labelsBox.addActionListener(this);
	
	currentY += height + offset2;
	
	String[] kansVolgordeKeuzes = { Kansbomen.rb.getString("geenKansVolgordeTekst"), Kansbomen.rb.getString("volgordeTekst"), 
			Kansbomen.rb.getString("kansNaastTekst"), Kansbomen.rb.getString("kansOnderTekst")};
	kansVolgordeBox = new JComboBox(kansVolgordeKeuzes);
	kansVolgordeBox.setSelectedIndex(kansVolgordeKeuze);
	kansVolgordeBox.setFont(theFont);
	kansVolgordeBox.setBounds(currentX, currentY, width, height);
	viewerOptiesPanel.add(kansVolgordeBox);
	kansVolgordeBox.addActionListener(this);
	
	currentY += height + offset2;
	
	kleurBox = new JCheckBox(Kansbomen.rb.getString("kleurTekst"), kleur);
	kleurBox.setFont(theFont);
	kleurBox.setBounds(currentX, currentY, width, height);
	viewerOptiesPanel.add(kleurBox);
	kleurBox.addActionListener(this);
	
	currentY += height;
	
	bovenbalkZichtbaarBox = new JCheckBox(Kansbomen.rb.getString("bovenbalkZichtbaarTekst"), legendaZichtbaar);
	bovenbalkZichtbaarBox.setFont(theFont);
	bovenbalkZichtbaarBox.setBounds(currentX, currentY, width, height);
	viewerOptiesPanel.add(bovenbalkZichtbaarBox);
	bovenbalkZichtbaarBox.addActionListener(this);
	
	currentY += height;
	
	legendaZichtbaarBox = new JCheckBox(Kansbomen.rb.getString("legendaZichtbaarTekst"), legendaZichtbaar);
	legendaZichtbaarBox.setFont(theFont);
	legendaZichtbaarBox.setBounds(currentX, currentY, width, height);
	viewerOptiesPanel.add(legendaZichtbaarBox);
	legendaZichtbaarBox.addActionListener(this);
	
	currentY += height;
	
	aantallenZichtbaarBox = new JCheckBox(Kansbomen.rb.getString("aantallenZichtbaarTekst"), aantallenZichtbaar);
	aantallenZichtbaarBox.setFont(theFont);
	aantallenZichtbaarBox.setBounds(currentX, currentY, width, height);
	viewerOptiesPanel.add(aantallenZichtbaarBox);
	aantallenZichtbaarBox.addActionListener(this);
	
	currentY += height;
	
	
	naamLetterLabel = new JLabel(Kansbomen.rb.getString("naamLetterTekst"));
	naamLetterLabel.setFont(theBoldFont);
	naamLetterLabel.setBounds(currentX, currentY, width, height);
	viewerOptiesPanel.add(naamLetterLabel);
	
	currentY += height + offset2;
	
	optieLabel = new JLabel[6];
	for(int i=0; i<6; i++)
	{	optieLabel[i] = new JLabel(Kansbomen.rb.getString("optieTekst")+(i + 1)+":"); 
		optieLabel[i].setFont(theFont);
		optieLabel[i].setBounds(currentX, currentY, 
				theFM.stringWidth(Kansbomen.rb.getString("optieTekst")+(i+1)+":"), height);
		viewerOptiesPanel.add(optieLabel[i]);
		currentY += height + offset2;
	}
	
	currentY -= 6 * height + 6 * offset2;
	currentX += optieLabel[1].getWidth() + offset;
	
	naamOptieTekst = new String[6];
	naamOptieTekst[0] = Kansbomen.rb.getString("naam1StringTekst");
	naamOptieTekst[1] = Kansbomen.rb.getString("naam2StringTekst");
	naamOptieTekst[2] = Kansbomen.rb.getString("naam3StringTekst");
	naamOptieTekst[3] = Kansbomen.rb.getString("naam4StringTekst");
	naamOptieTekst[4] = Kansbomen.rb.getString("naam5StringTekst");
	naamOptieTekst[5] = Kansbomen.rb.getString("naam6StringTekst");
	
	naamOptieVeld = new JTextField[6];
	for(int i=0; i<6; i++)
	{ 	naamOptieVeld[i] = new JTextField(naamOptieTekst[i]);
		naamOptieVeld[i].setFont(theFont);
		naamOptieVeld[i].setBounds(currentX, currentY, width - 
				optieLabel[i].getWidth() - 2 * theFM.charWidth('m') - 4 * offset, height);
		viewerOptiesPanel.add(naamOptieVeld[i]);
		naamOptieVeld[i].addActionListener(this);
		naamOptieVeld[i].addFocusListener(this);
		currentY += height + offset2;
	}
		
	currentY -= 6 * height + 6 * offset2;
	
	for(int i=0; i<6; i++)
		letterString[i] = naamOptieTekst[i].substring(0,1).toLowerCase();
	
	currentX += naamOptieVeld[1].getWidth() + offset;
	
	letterOptieVeld = new JTextField[6];
	for(int i=0; i<6; i++)
	{ 	letterOptieVeld[i] = new JTextField(letterString[i]);
		letterOptieVeld[i].setFont(theFont);
		letterOptieVeld[i].setBounds(currentX, currentY, 2 * theFM.charWidth('m') + 2 * offset, height);
		viewerOptiesPanel.add(letterOptieVeld[i]);
		letterOptieVeld[i].addActionListener(this);
		letterOptieVeld[i].addFocusListener(this);
		currentY += height + offset2;
	}	
	
	currentY += offset - offset2;
	
	currentX -= optieLabel[0].getWidth() + naamOptieVeld[0].getWidth() + 2 * offset;
	
	bovenbalkLabel = new JLabel(Kansbomen.rb.getString("bovenbalkTekst")+":");
	bovenbalkLabel.setFont(theFont);
	bovenbalkLabel.setBounds(currentX, currentY, 
			Math.max(theFM.stringWidth(Kansbomen.rb.getString("bovenbalkTekst")+":"),
					theFM.stringWidth(Kansbomen.rb.getString("bovenbalkMvTekst")+":")), height);
	viewerOptiesPanel.add(bovenbalkLabel);
	
	currentX += bovenbalkLabel.getWidth() + offset;
	
	bovenbalkVeld = new JTextField(trekkingTekst);
	bovenbalkVeld.setFont(theFont);
	bovenbalkVeld.setBounds(currentX, currentY, width - bovenbalkLabel.getWidth() - offset, height);
	viewerOptiesPanel.add(bovenbalkVeld);
	bovenbalkVeld.addActionListener(this);
	bovenbalkVeld.addFocusListener(this);
	
	currentX -= bovenbalkLabel.getWidth() + offset;
	currentY += height + offset2;
	
	bovenbalkMvLabel = new JLabel(Kansbomen.rb.getString("bovenbalkMvTekst")+":");
	bovenbalkMvLabel.setFont(theFont);
	bovenbalkMvLabel.setBounds(currentX, currentY, bovenbalkLabel.getWidth(), height);
	viewerOptiesPanel.add(bovenbalkMvLabel);
	
	currentX += bovenbalkMvLabel.getWidth() + offset;
	
	bovenbalkMvVeld = new JTextField(trekkingMvTekst);
	bovenbalkMvVeld.setFont(theFont);
	bovenbalkMvVeld.setBounds(currentX, currentY, width - bovenbalkMvLabel.getWidth() - offset, height);
	viewerOptiesPanel.add(bovenbalkMvVeld);
	bovenbalkMvVeld.addActionListener(this);
	bovenbalkMvVeld.addFocusListener(this);
	
	currentX -= bovenbalkMvLabel.getWidth() + offset;
	currentY += height + offset;
	
	zichtbaarLabel = new JLabel(Kansbomen.rb.getString("zichtbaarTekst"));
	zichtbaarLabel.setFont(theBoldFont);
	zichtbaarLabel.setBounds(currentX, currentY, width, height);
	viewerOptiesPanel.add(zichtbaarLabel);
	
	currentY += height;
	
	teruglegZichtbaarBox = new JCheckBox(Kansbomen.rb.getString("teruglegZichtbaarTekst"), teruglegZichtbaar);
	teruglegZichtbaarBox.setFont(theFont);
	teruglegZichtbaarBox.setBounds(currentX, currentY, width, height);
	viewerOptiesPanel.add(teruglegZichtbaarBox);
	teruglegZichtbaarBox.addActionListener(this);
	
	currentY += height;
	
	trekkingZichtbaarBox = new JCheckBox(Kansbomen.rb.getString("trekkingZichtbaarTekst"), trekkingZichtbaar);
	trekkingZichtbaarBox.setFont(theFont);
	trekkingZichtbaarBox.setBounds(currentX, currentY, width, height);
	viewerOptiesPanel.add(trekkingZichtbaarBox);
	trekkingZichtbaarBox.addActionListener(this);
	
	currentY += height;
	
	optiesZichtbaarBox = new JCheckBox(Kansbomen.rb.getString("optiesZichtbaarTekst"), optiesZichtbaar);
	optiesZichtbaarBox.setFont(theFont);
	optiesZichtbaarBox.setBounds(currentX, currentY, width, height);
	viewerOptiesPanel.add(optiesZichtbaarBox);
	optiesZichtbaarBox.addActionListener(this);
	
	currentY += height;
	
	ballenZichtbaarBox = new JCheckBox(Kansbomen.rb.getString("ballenZichtbaarTekst"), ballenZichtbaar);
	ballenZichtbaarBox.setFont(theFont);
	ballenZichtbaarBox.setBounds(currentX, currentY, width, height);
	viewerOptiesPanel.add(ballenZichtbaarBox);
	ballenZichtbaarBox.addActionListener(this);
	
	currentY = height + offset2;
	
	kijkNaBox = new JCheckBox(Kansbomen.rb.getString("kijkNaTekst"), kijkNa);
	kijkNaBox.setFont(theFont);
	kijkNaBox.setBounds(currentX, currentY, 70, height);
	nakijkOptiesPanel.add(kijkNaBox);
	kijkNaBox.addActionListener(this);
	
	currentY += height + offset2;
	
	String[] teruglegKeuzes = { Kansbomen.rb.getString("metTerugleggenTekst"), Kansbomen.rb.getString("zonderTerugleggenTekst")};
	terugleggenBox = new JComboBox(teruglegKeuzes);
	terugleggenBox.setSelectedIndex(terugleggenKeuze);
	terugleggenBox.setFont(theFont);
	terugleggenBox.setBounds(currentX, currentY, width, height);
	nakijkOptiesPanel.add(terugleggenBox);
	
	currentY += height + 2 * offset;
	aantalTrekkingen = new JLabel(Kansbomen.rb.getString("aantalTekst") + trekkingMvTekst+":");
	aantalOptie = new JLabel[6];
	for(int i = 0; i<6; i++)
	{
		aantalOptie[i] = new JLabel(Kansbomen.rb.getString("aantalTekst")+naamOptieTekst[i]+":");
	}
	
	aantalTrekkingen.setFont(theFont);
	aantalTrekkingen.setBounds(currentX, currentY, 100, height);
	nakijkOptiesPanel.add(aantalTrekkingen);
	
	currentX += aantalTrekkingen.getWidth()+ offset;
	
	String[] momentenKeuzes={"1","2","3","4","5","6"};
	trekkingenBox = new JComboBox(momentenKeuzes);
	trekkingenBox.setSelectedIndex(trekkingen - 1);
	trekkingenBox.setFont(theFont);
	trekkingenBox.setBounds(currentX, currentY, width - aantalTrekkingen.getWidth() - offset, height);
	nakijkOptiesPanel.add(trekkingenBox);
	
	currentX -= aantalTrekkingen.getWidth()+ offset;
	currentY += height + 2 * offset;
	
	aantalOptiesLabel = new JLabel(Kansbomen.rb.getString("aantalOptiesTekst"));
	aantalOptiesLabel.setFont(theFont);
	aantalOptiesLabel.setBounds(currentX, currentY, 100, height);
	nakijkOptiesPanel.add(aantalOptiesLabel);
	
	currentX += aantalOptiesLabel.getWidth()+ offset;
	
	String[] optiesKeuzes={"2","3","4","5","6"};
	optiesBox = new JComboBox(optiesKeuzes);
	optiesBox.setSelectedIndex(aantalOpties - 2);
	optiesBox.setFont(theFont);
	optiesBox.setBounds(currentX, currentY, width - aantalOptiesLabel.getWidth() - offset, height);
	nakijkOptiesPanel.add(optiesBox);
	optiesBox.addActionListener(this);
	
	currentX -= aantalOptiesLabel.getWidth()+ offset;
	currentY += height + offset;
	
	int breedteAantalVeld = theFM.stringWidth("000")+ 2 * offset;
			                       
	aantalOptieVeld = new JTextField[6];
	for(int i=0; i<6; i++)
	{	aantalOptieVeld[i] = new JTextField(""+aantalInt[i]);
		aantalOptieVeld[i].setFont(theFont);
		aantalOptieVeld[i].setBounds(currentX + width - breedteAantalVeld, currentY, breedteAantalVeld, height);
		currentY += height + offset;
	}
		
	currentY -= 6 * height + 6 * offset;
	
	for (int i=0; i<6; i++)
	{	aantalOptie[i].setFont(theFont);
		aantalOptie[i].setBounds(currentX, currentY, width - breedteAantalVeld, height);
		currentY += height + offset;
	}
	for(int i=0; i<6; i++)
	{
		nakijkOptiesPanel.add(aantalOptie[i]);
		nakijkOptiesPanel.add(aantalOptieVeld[i]);
	}
	zetAantalOpties(aantalOpties);
			    	
	currentY += height + 2 * offset;
	
	maxScoreLabel = new JLabel(Kansbomen.rb.getString("maxScoreTekst"));
	maxScoreLabel.setFont(theFont);
	maxScoreLabel.setBounds(currentX, currentY, 100, height);
	nakijkOptiesPanel.add(maxScoreLabel);
	
	currentX += maxScoreLabel.getWidth()+ offset;
			
	maxScoreVeld = new JTextField("10");
	maxScoreVeld.setFont(theFont);
	maxScoreVeld.setBounds(currentX, currentY, width - 100 - offset, height);
	nakijkOptiesPanel.add(maxScoreVeld);
	
	currentY += height + 2 * offset;
	currentX -= maxScoreLabel.getWidth() + offset;
	
	externControlerenBox = new JCheckBox(Kansbomen.rb.getString("externControlerenTekst"), checkExternal);
	externControlerenBox.setFont(theFont);
	externControlerenBox.setBounds(currentX, currentY, width, height);
	nakijkOptiesPanel.add(externControlerenBox);
	//externControlerenBox.setVisible(kijkNa);
	externControlerenBox.addActionListener(this);
	
	componentsCreated = true;
	
	zetNakijkComponentenEnabled(false);
}


public void plaatsComponenten()
{
	if (componentsCreated)
	{
//		int indent = kbip.getSize().width + offset;
//		labelsBox.setLocation(indent, labelsBox.getLocation().y);
//		kleurBox.setLocation(indent, kleurBox.getLocation().y);
//		kansVolgordeBox.setLocation(indent, kansVolgordeBox.getLocation().y);
//		naamLetterLabel.setLocation(indent, naamLetterLabel.getLocation().y);
//		for(int i=0; i<6; i++)
//		{
//			optieLabel[i].setLocation(indent, optieLabel[i].getLocation().y);
//			naamOptieVeld[i].setLocation(indent + optieLabel[i].getSize().width + offset, naamOptieVeld[i].getLocation().y);
//			letterOptieVeld[i].setLocation(indent + optieLabel[i].getSize().width 
//					+ naamOptieVeld[i].getSize().width + 2 * offset, letterOptieVeld[i].getLocation().y);
//		}
//		bovenbalkLabel.setLocation(indent, bovenbalkLabel.getLocation().y);
//		bovenbalkVeld.setLocation(indent + bovenbalkLabel.getWidth() + offset, bovenbalkVeld.getLocation().y);
//		bovenbalkMvLabel.setLocation(indent, bovenbalkMvLabel.getLocation().y);
//		bovenbalkMvVeld.setLocation(indent + bovenbalkMvLabel.getWidth() + offset, bovenbalkMvVeld.getLocation().y);
//		zichtbaarLabel.setLocation(indent, zichtbaarLabel.getLocation().y);
//		teruglegZichtbaarBox.setLocation(indent, teruglegZichtbaarBox.getLocation().y);
//		trekkingZichtbaarBox.setLocation(indent, trekkingZichtbaarBox.getLocation().y);
//		optiesZichtbaarBox.setLocation(indent, optiesZichtbaarBox.getLocation().y);
//		ballenZichtbaarBox.setLocation(indent, ballenZichtbaarBox.getLocation().y);
//		legendaZichtbaarBox.setLocation(indent, legendaZichtbaarBox.getLocation().y);
//		bovenbalkZichtbaarBox.setLocation(indent, bovenbalkZichtbaarBox.getLocation().y);
//		kijkNaBox.setLocation(indent, kijkNaBox.getLocation().y);
//		nakijkModelButton.setLocation(indent + kijkNaBox.getWidth() + offset, nakijkModelButton.getLocation().y);
//		externControlerenBox.setLocation(indent, externControlerenBox.getLocation().y);
		tabbedPane.setBounds(kbipBreedte, 0, editWidth, editHeight);
		
		repaint();
	}
}

public void zetNakijkComponentenEnabled(boolean enabled)
{
	terugleggenBox.setEnabled(enabled && teruglegZichtbaar);
	aantalTrekkingen.setEnabled(enabled && trekkingZichtbaar);
	trekkingenBox.setEnabled(enabled && trekkingZichtbaar);
	aantalOptiesLabel.setEnabled(enabled && optiesZichtbaar);
	optiesBox.setEnabled(enabled && optiesZichtbaar);
	for(int i = 0; i < aantalOptieVeld.length; i++)
	{	aantalOptie[i].setEnabled(enabled && ballenZichtbaar);
		aantalOptieVeld[i].setEnabled(enabled && ballenZichtbaar);
	}
	maxScoreLabel.setEnabled(enabled);
	maxScoreVeld.setEnabled(enabled);
	externControlerenBox.setEnabled(enabled);
	//TODO: verder aanvullen. 
	
}

public void zetAantalOpties(int j)
{
	for(int i = 0; i < j; i++)
	{
		aantalOptie[i].setVisible(true);
		aantalOptieVeld[i].setVisible(true);
	}
	for(int i = j; i < 6; i++)
	{
		aantalOptie[i].setVisible(false);
		aantalOptieVeld[i].setVisible(false);
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
	else if(e.getSource() == naamOptieVeld[0])
		actieNaamOptieVeld(0);
	else if(e.getSource() == naamOptieVeld[1])
		actieNaamOptieVeld(1);
	else if(e.getSource() == naamOptieVeld[2])
		actieNaamOptieVeld(2);
	else if(e.getSource() == naamOptieVeld[3])
		actieNaamOptieVeld(3);
	else if(e.getSource() == naamOptieVeld[4])
		actieNaamOptieVeld(4);
	else if(e.getSource() == naamOptieVeld[5])
		actieNaamOptieVeld(5);
	else if(e.getSource() == letterOptieVeld[0])
		actieLetterOptieVeld(0);
	else if(e.getSource() == letterOptieVeld[1])
		actieLetterOptieVeld(1);
	else if(e.getSource() == letterOptieVeld[2])
		actieLetterOptieVeld(2);
	else if(e.getSource() == letterOptieVeld[3])
		actieLetterOptieVeld(3);
	else if(e.getSource() == letterOptieVeld[4])
		actieLetterOptieVeld(4);
	else if(e.getSource() == letterOptieVeld[5])
		actieLetterOptieVeld(5);
	else if(e.getSource() == bovenbalkVeld)
	{
		trekkingTekst = bovenbalkVeld.getText();
		kbip.zetTrekkingTekst(trekkingTekst);
	}	
	else if(e.getSource() == bovenbalkMvVeld)
	{	trekkingMvTekst = bovenbalkMvVeld.getText();
		kbip.zetTrekkingMvTekst(trekkingMvTekst);
	}
	else if(e.getSource() == teruglegZichtbaarBox)
	{	teruglegZichtbaar = teruglegZichtbaarBox.isSelected();
		kbip.zetTeruglegZichtbaar(teruglegZichtbaar);
		nakijkModel[7] = kbip.terugleggenKeuze;
		terugleggenBox.setEnabled(teruglegZichtbaar);
		kijkNaAlleenAlsMogelijk();
		
	}
	else if(e.getSource() == trekkingZichtbaarBox)
	{	trekkingZichtbaar = trekkingZichtbaarBox.isSelected();
		kbip.zetTrekkingZichtbaar(trekkingZichtbaar);
		nakijkModel[8] = kbip.trekkingen;
		trekkingenBox.setEnabled(trekkingZichtbaar);
		kijkNaAlleenAlsMogelijk();
	}
	else if(e.getSource() == optiesZichtbaarBox)
	{	optiesZichtbaar = optiesZichtbaarBox.isSelected();
		kbip.zetOptiesZichtbaar(optiesZichtbaar);
		nakijkModel[9] = kbip.aantalOpties;
		optiesBox.setEnabled(optiesZichtbaar);
		aantalOptiesLabel.setEnabled(optiesZichtbaar);
		kijkNaAlleenAlsMogelijk();
	}
	else if(e.getSource() == ballenZichtbaarBox)
	{	ballenZichtbaar = ballenZichtbaarBox.isSelected();
		kbip.zetBallenZichtbaar(ballenZichtbaar);
		for(int i = 0; i < 6; i++)
		{	nakijkModel[i + 1] = kbip.aantalInt[i];
			aantalOptieVeld[i].setEnabled(ballenZichtbaar && kijkNa);
			aantalOptie[i].setEnabled(ballenZichtbaar && kijkNa);
		}
		kijkNaAlleenAlsMogelijk();
	}	
	
	else if(e.getSource() == legendaZichtbaarBox)
	{	legendaZichtbaar = legendaZichtbaarBox.isSelected();
		aantallenZichtbaarBox.setVisible(legendaZichtbaar);
		kbip.zetLegendaZichtbaar(legendaZichtbaar);
	}	
	else if(e.getSource() == aantallenZichtbaarBox)
	{	aantallenZichtbaar = aantallenZichtbaarBox.isSelected();
		kbip.zetAantallenZichtbaar(aantallenZichtbaar);
	}
	
	else if(e.getSource() == bovenbalkZichtbaarBox)
	{	bovenbalkZichtbaar = bovenbalkZichtbaarBox.isSelected();
		kbip.zetBovenbalkZichtbaar(bovenbalkZichtbaar);
	}	
	
	else if(e.getSource() == kijkNaBox)
	{	kijkNa = kijkNaBox.isSelected();
		//nakijkModelButton.setVisible(kijkNa);
		zetNakijkComponentenEnabled(kijkNa);
		externControlerenBox.setSelected(checkExternal);
		kbip.zetKijkNa(kijkNa);
	}
	
	else if(e.getSource() == externControlerenBox)
	{
		checkExternal = externControlerenBox.isSelected();
		kbip.zetCheckExternal(checkExternal);
	}
	else if(e.getSource().equals(optiesBox))
		zetAantalOpties(optiesBox.getSelectedIndex()+2);
	
}

public void kijkNaAlleenAlsMogelijk()
{
	if(!teruglegZichtbaar && !trekkingZichtbaar && !optiesZichtbaar && !ballenZichtbaar)
	{	kijkNa = false;
		kijkNaBox.setSelected(kijkNa);
		kijkNaBox.setEnabled(kijkNa);
		zetNakijkComponentenEnabled(false);
		kbip.zetKijkNa(kijkNa);
	}	
	else
		kijkNaBox.setEnabled(true);
}

public void focusLost(FocusEvent e)  
{
	if(e.getSource() == naamOptieVeld[0])
		actieNaamOptieVeld(0);
	else if(e.getSource() == naamOptieVeld[1])
		actieNaamOptieVeld(1);
	else if(e.getSource() == naamOptieVeld[2])
		actieNaamOptieVeld(2);
	else if(e.getSource() == naamOptieVeld[3])
		actieNaamOptieVeld(3);
	else if(e.getSource() == naamOptieVeld[4])
		actieNaamOptieVeld(4);
	else if(e.getSource() == naamOptieVeld[5])
		actieNaamOptieVeld(5);
	else if(e.getSource() == letterOptieVeld[0])
		actieLetterOptieVeld(0);
	else if(e.getSource() == letterOptieVeld[1])
		actieLetterOptieVeld(1);
	else if(e.getSource() == letterOptieVeld[2])
		actieLetterOptieVeld(2);
	else if(e.getSource() == letterOptieVeld[3])
		actieLetterOptieVeld(3);
	else if(e.getSource() == letterOptieVeld[4])
		actieLetterOptieVeld(4);
	else if(e.getSource() == letterOptieVeld[5])
		actieLetterOptieVeld(5);
	else if(e.getSource() == bovenbalkVeld)
	{
		trekkingTekst = bovenbalkVeld.getText();
		kbip.zetTrekkingTekst(trekkingTekst);
	}	
	else if(e.getSource() == bovenbalkMvVeld)
	{
		trekkingMvTekst = bovenbalkMvVeld.getText();
		kbip.zetTrekkingMvTekst(trekkingMvTekst);
	}
}

public void actieNaamOptieVeld(int i)
{
	if(naamOptieVeld[i].getText().isEmpty())
		naamOptieVeld[i].setText(naamOptieTekst[i]);
	else
	{
	naamOptieTekst[i] = naamOptieVeld[i].getText();
	letterString[i] = naamOptieTekst[i].substring(0,1).toLowerCase();
	letterOptieVeld[i].setText(letterString[i]);
	aantalOptie[i].setText(Kansbomen.rb.getString("aantalTekst")+naamOptieTekst[i]+":");
	kbip.zetNaamOptie(i,naamOptieTekst[i]);
	}
}

public void actieLetterOptieVeld(int i)
{
	if(letterOptieVeld[i].getText().length() < 3)
	{	letterString[i] = letterOptieVeld[i].getText();
		kbip.zetLetterOptie(i,letterString[i]);
	}
	else
		letterOptieVeld[i].setText(letterString[i]);
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
	
	if (h.containsKey("bovenbalkZichtbaar"))
		bovenbalkZichtbaar = ((Boolean) h.get("bovenbalkZichtbaar")).booleanValue();
	bovenbalkZichtbaarBox.setSelected(bovenbalkZichtbaar);
	
	if (h.containsKey("legendaZichtbaar"))
		legendaZichtbaar = ((Boolean) h.get("legendaZichtbaar")).booleanValue();
	legendaZichtbaarBox.setSelected(legendaZichtbaar);
	aantallenZichtbaarBox.setVisible(legendaZichtbaar);
	
	if (h.containsKey("aantallenZichtbaar"))
		aantallenZichtbaar = ((Boolean) h.get("aantallenZichtbaar")).booleanValue();
	aantallenZichtbaarBox.setSelected(aantallenZichtbaar);
	
	if (h.containsKey("kijkNa"))
		kijkNa = ((Boolean) h.get("kijkNa")).booleanValue();
	kijkNaBox.setSelected(kijkNa);
		
	if (h.containsKey("checkExternal"))
		checkExternal = ((Boolean) h.get("checkExternal")).booleanValue();
	externControlerenBox.setSelected(checkExternal);
	
	if (h.containsKey("labelsKeuze"))
		labelsKeuze = ((Integer) h.get("labelsKeuze")).intValue();
	labelsBox.setSelectedIndex(labelsKeuze);
	
	if (h.containsKey("kleur"))
		kleur = ((Boolean) h.get("kleur")).booleanValue();
	kleurBox.setSelected(kleur);
	
	if (h.containsKey("kansVolgordeKeuze"))
		kansVolgordeKeuze = ((Integer) h.get("kansVolgordeKeuze")).intValue();
	kansVolgordeBox.setSelectedIndex(kansVolgordeKeuze);
	if (h.containsKey("nakijkModel"))
		nakijkModel = ((int[]) h.get("nakijkModel"));
	
	if (h.containsKey("naamOptieTekst"))
	{	String[] naamOptieTekst = ((String[]) h.get("naamOptieTekst"));
		if(naamOptieTekst.length == 6)
			this.naamOptieTekst = naamOptieTekst;
		else if(naamOptieTekst.length == 7)
		{
			this.naamOptieTekst = new String[6];
			for(int i = 0; i < this.naamOptieTekst.length; i++)
				this.naamOptieTekst[i] = naamOptieTekst[i+1];
		}
	}
	for(int i = 0; i < 6; i++)
	{	naamOptieVeld[i].setText(naamOptieTekst[i]);
		aantalOptie[i].setText(Kansbomen.rb.getString("aantalTekst")+naamOptieTekst[i]+":");
	}
	if (h.containsKey("letterString"))
	{	String[] letterString = ((String[]) h.get("letterString"));
		if(letterString.length == 6)
			this.letterString = letterString;
		else if(letterString.length == 7)
		{
			this.letterString = new String[6];
			for(int i = 0; i < this.letterString.length; i++)
				this.letterString[i] = letterString[i+1];
			//this.nakijkModel[8] += 1;
			//this.nakijkModel[9] += 2;
		}
	}
	for(int i = 0; i < 6; i++)
		letterOptieVeld[i].setText(letterString[i]);
	if (h.containsKey("trekkingTekst"))
		trekkingTekst = ((String) h.get("trekkingTekst"));
	bovenbalkVeld.setText(trekkingTekst);
	if (h.containsKey("trekkingMvTekst"))
		trekkingMvTekst = ((String) h.get("trekkingMvTekst"));
	bovenbalkMvVeld.setText(trekkingMvTekst);
	if (h.containsKey("scoreMax"))
		scoreMax = ((Integer) h.get("scoreMax")).intValue();
	
	updateBeginwaarden(nakijkModel);
	terugleggenBox.setSelectedIndex(terugleggenKeuze);
	trekkingenBox.setSelectedIndex(trekkingen - 1);
	optiesBox.setSelectedIndex(aantalOpties - 2);
	for(int i = 0; i < aantalOptieVeld.length; i++)
	{	aantalOptieVeld[i].setText("" + aantalInt[i]);
	}
	maxScoreVeld.setText("" + scoreMax);
	
	if (h.containsKey("nakijkModelIngesteld"))
		nakijkModelIngesteld = ((Boolean) h.get("nakijkModelIngesteld")).booleanValue();
	
	if (h.containsKey("kbipBreedte"))
		kbipBreedte = ((Integer) h.get("kbipBreedte")).intValue();
	if (h.containsKey("kbipHoogte"))
		kbipHoogte = ((Integer) h.get("kbipHoogte")).intValue();
	
	setBounds(getLocation().x, getLocation().y, kbipBreedte + editWidth, Math.max(kbipHoogte, editHeight));
	zetAantalOpties(aantalOpties);
	zetNakijkComponentenEnabled(kijkNa);
	kbip.setEditState(h);		
}

public Hashtable getEditState() {

	boolean kijkNa = false;
	//int scoreMax = 0;
	//int[] nakijkModel = null;
	boolean nakijkModelIngesteld = false;
	String[] naamOptieTekst = null;
	String[] letterString = null;
	
	//boolean[] itemsEnabled = null;

	if(this.naamOptieTekst.length == 7)
	{
		naamOptieTekst = new String[6];
		for(int i = 0; i < naamOptieTekst.length; i++)
		{
			naamOptieTekst[i] = this.naamOptieTekst[i+1];
		}
	}
	else
		naamOptieTekst = this.naamOptieTekst;
	if(this.letterString.length == 7)
	{
		letterString = new String[6];
		for(int i = 0; i < letterString.length; i++)
		{
			letterString[i] = this.letterString[i+1];
		}
	}
	else
		letterString = this.letterString;
	kijkNa = this.kijkNa;
	nakijkModelIngesteld = this.nakijkModelIngesteld;
	if(kijkNa)
	{	maakNakijkModel();
		//nakijkModel = nakijkModelButton.getNakijkModel();
		//itemsEnabled = nakijkModelButton.itemsEnabled;
	}
	//nakijkModel = this.nakijkModel;
	
	Hashtable h = kbip.getEditState();
	h.put("kijkNa", kijkNa);
	h.put("nakijkModelIngesteld", nakijkModelIngesteld);
	if(kijkNa){
		h.put("scoreMax", new Integer(scoreMax));
		h.put("nakijkModel", nakijkModel);
		//h.put("itemsEnabled", itemsEnabled);
	}	
	h.put("naamOptieTekst", naamOptieTekst);
	h.put("letterString", letterString);
	
	h.put("kbipBreedte", new Integer(kbipBreedte));
	h.put("kbipHoogte", new Integer(kbipHoogte));
	
	return h;
}

public void maakNakijkModel()
{ //haal huidige waarden uit invoervelden.
	try{
		scoreMax = Integer.parseInt(maxScoreVeld.getText());
	}
	catch(Exception e){
		scoreMax = 0;}
	terugleggenKeuze = terugleggenBox.getSelectedIndex();
	trekkingen = trekkingenBox.getSelectedIndex() + 1;
	aantalOpties = optiesBox.getSelectedIndex() + 2;
	for(int i = 0; i < 6; i++)
	{	
		try{
			aantalInt[i] = Integer.parseInt(aantalOptieVeld[i].getText());
		}
		catch(Exception e){};
	}
	updateNakijkModel();
}

public void updateNakijkModel()
{
   	nakijkModel[0] = scoreMax;
	for(int i = 1; i < 7; i++)
		nakijkModel[i] = aantalInt[i - 1];
	nakijkModel[7] = terugleggenKeuze;
	nakijkModel[8] = trekkingen;
	nakijkModel[9] = aantalOpties;
}

public void updateBeginwaarden(int[] nakijkmodel)
{
	scoreMax = nakijkmodel[0];
	for(int i = 0; i < 6; i++)
		aantalInt[i] = nakijkModel[i + 1];
	
	terugleggenKeuze = nakijkmodel[7];
	trekkingen = nakijkmodel[8];
	aantalOpties = nakijkmodel[9];
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


public void wis() {}

public void zetMode(int mode) {}

public void stop() {}

public void start() {}

public void addActionListener(ActionListener al) {}

public void focusGained(FocusEvent arg0) {}

class TabbedPaneCL implements ChangeListener
{
	
	public void stateChanged(ChangeEvent e)
	{
		//noSetBounds = true;
		//int index = tabbedPane.getSelectedIndex();
//		// terug naar viewerOptionsPanel
//		if (index == 0)
//		{	naip.toonDocentViewer(false);
//			
//		}
//		else // naar nakijkOptiesPanel
//		{	if (nakijkBox.isSelected())
//				naip.toonDocentViewer(true);
//		}
	}
}	
	
}
