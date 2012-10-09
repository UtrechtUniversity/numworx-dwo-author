package fi.kansbomen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.*;

import javax.swing.*;


public class KansbomenInteractieEditPanel extends JPanel implements ActionListener

{

	int editWidth = 190;
	int editHeight = 450; 
	int kbipBreedte = 500; // startbreedte ip
	int kbipHoogte = 450; // starthoogte ip
	
	Font theFont;
	FontMetrics theFM;
	Font theBoldFont;
	FontMetrics theBoldFM;
	
	int offset = 6;
	int currentX;
	int currentY;
	int width;
	int height;
	
	//boolean componentsCreated = false;
	//boolean noSetBounds = false;	

	protected KansbomenInteractiePanel kbip; //Ik weet niet waarom protected.
	JCheckBox kleurBox;
	JComboBox terugleggenBox, trekkingenBox, optiesBox;
	JLabel aantalTrekkingen, aantalOpties;
	JLabel[] naamOptie, aantalOptie;
	JTextField[] naamOptieBox, aantalOptieBox;
	String[] aantalString;
		
	//startwaarden editpanel
	boolean kleur = true;
	boolean terugleggen = true;
	int terugleggenKeuze = 0;
	int trekkingen = 2;
	int aantalOptiesKeuze = 2;
	String aantal1String = "4";
	String aantal2String = "4";
	String aantal3String = "4";
	String aantal4String = "4";
	int aantal1Int = 4;
	int aantal2Int = 4;
	int aantal3Int = 4;
	int aantal4Int = 4;
	
/*Wat heb ik nodig in mijn interactie-editpanel? 
- Aanvinkvakje volgorde weergeven (TO DO: volgorde-mogelijkheid bouwen!)
- Keuze tussen letters, kansen of niets bij takken	
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
	
	String[] teruglegKeuzes = { Kansbomen.rb.getString("metTerugleggenTekst"), Kansbomen.rb.getString("zonderTerugleggenTekst")};
	terugleggenBox = new JComboBox(teruglegKeuzes);
	terugleggenBox.setSelectedIndex(terugleggenKeuze);
	terugleggenBox.setFont(theFont);
	terugleggenBox.setBounds(currentX, currentY, width, height);
	add(terugleggenBox);
	terugleggenBox.addActionListener(this);
	
	currentY += height + 2 * offset;
	
	kleurBox = new JCheckBox(Kansbomen.rb.getString("kleurTekst"), kleur);
	kleurBox.setFont(theFont);
	kleurBox.setBackground(Color.white);
	kleurBox.setBounds(currentX, currentY, width, height);
	add(kleurBox);
	kleurBox.addActionListener(this);
	
	currentY += height + 2 * offset;
	
	aantalTrekkingen = new JLabel(Kansbomen.rb.getString("aantalTrekkingenTekst"));
	aantalTrekkingen.setFont(theFont);
	aantalTrekkingen.setBounds(currentX, currentY, 100, height);
	/* Eigenlijk zou ik die 100 hierboven wel weg willen hebben. Moet dat met een Layoutmanager?)
	 */
	add(aantalTrekkingen);
	
	currentX += aantalTrekkingen.getWidth()+ offset;
	
	String[] momentenKeuzes={"1","2","3","4","5","6"};
	trekkingenBox = new JComboBox(momentenKeuzes);
	trekkingenBox.setSelectedIndex(trekkingen);
	trekkingenBox.setFont(theFont);
	trekkingenBox.setBounds(currentX, currentY, width - aantalTrekkingen.getWidth() - offset, height);
	add(trekkingenBox);
	trekkingenBox.addActionListener(this);
	
	currentX -= aantalTrekkingen.getWidth()+ offset;
	currentY += height + 2 * offset;
	
	aantalOpties = new JLabel(Kansbomen.rb.getString("aantalOptiesTekst"));
	aantalOpties.setFont(theFont);
	aantalOpties.setBounds(currentX, currentY, 100, height);//hier ook liever geen 100
	add(aantalOpties);
	
	currentX += aantalOpties.getWidth()+ offset;
	
	String[] optiesKeuzes={"2","3","4"};
	optiesBox = new JComboBox(optiesKeuzes);
	optiesBox.setSelectedIndex(aantalOptiesKeuze);
	optiesBox.setFont(theFont);
	optiesBox.setBounds(currentX, currentY, width - aantalOpties.getWidth() - offset, height);
	add(optiesBox);
	optiesBox.addActionListener(this);
	
	currentX -= aantalOpties.getWidth()+ offset;
	currentY += height + 2 * offset;
	
	naamOptie = new JLabel[5];
	for(int i=1; i<5; i++)
	{	naamOptie[i] = new JLabel(Kansbomen.rb.getString("naamOptieTekst")+i+":"); 
		naamOptie[i].setFont(theFont);
		naamOptie[i].setBounds(currentX, currentY, 80, height);//liever geen 80
		currentY += 2 * height + 2 * offset;
	}
		
	currentY -= 8 * height + 8 * offset;
	
	naamOptieBox = new JTextField[5];
	for(int i=1; i<5; i++)
	{ 	currentX += naamOptie[i].getWidth()+ offset;
		naamOptieBox[i] = new JTextField(Kansbomen.rb.getString("optieTekst")+i);
		naamOptieBox[i].setFont(theFont);
		naamOptieBox[i].setBounds(currentX, currentY, width - naamOptie[i].getWidth() - offset, height);
		currentY += 2 * height + 2 * offset;
		currentX -= naamOptie[i].getWidth()+ offset;
	}
		
	currentY -= 7 * height + 7 * offset;
	
	aantalOptie = new JLabel[5];
	for (int i=1; i<5; i++)
	{	aantalOptie[i] = new JLabel(Kansbomen.rb.getString("aantalOptieTekst")+i+":");
		aantalOptie[i].setFont(theFont);
		aantalOptie[i].setBounds(currentX, currentY, 80, height);//liever geen 80
		currentY += 2 * height + 2 * offset;
	}
	
	currentY -= 8 * height + 8 * offset;
	
	aantalString = new String[5];
	aantalString[1] = aantal1String;
	aantalString[2] = aantal2String;
	aantalString[3] = aantal3String;
	aantalString[4] = aantal4String;
	                       
	aantalOptieBox = new JTextField[5];
	for(int i=1; i<5; i++)
	{	currentX += aantalOptie[i].getWidth() + offset;
		aantalOptieBox[i] = new JTextField(aantalString[i]);
		aantalOptieBox[i].setFont(theFont);
		aantalOptieBox[i].setBounds(currentX, currentY, width - aantalOptie[1].getWidth() - offset, height);
		currentX -= aantalOptie[i].getWidth() + offset;
		currentY += 2* height + 2 * offset;
	}
		
	plaatsOptieRegels(aantalOptiesKeuze+2);
	
	
	
	//componentsCreated=true;
	
}

public void plaatsOptieRegels(int j)
{
	for(int i=1; i<5; i++)
	{	if(j>=i)
		{
			add(naamOptie[i]);
			add(naamOptieBox[i]);
			naamOptieBox[i].addActionListener(this);
			add(aantalOptie[i]);
			add(aantalOptieBox[i]);
			aantalOptieBox[i].addActionListener(this);
		}
		
	}
}

public void actionPerformed(ActionEvent e) 

{
	if(e.getSource() == kleurBox)
	{	kleur = kleurBox.isSelected();
		kbip.zetKleur(kleur);
	}
	else if(e.getSource() == terugleggenBox)
	{	if(terugleggenBox.getSelectedIndex()==0)
		{	terugleggenKeuze = 0;
			terugleggen=true;
		}
		else if(terugleggenBox.getSelectedIndex()==1)
		{	terugleggenKeuze = 1;
			terugleggen=false;
		}
		kbip.zetTerugleggen(terugleggen);
	}
	else if(e.getSource() == trekkingenBox)
	{	trekkingen = trekkingenBox.getSelectedIndex();
		kbip.zetKeuzeMomenten(trekkingen+1);
	}
	else if(e.getSource() == optiesBox)
	{
		aantalOptiesKeuze = optiesBox.getSelectedIndex();
		kbip.zetAantalOpties(aantalOptiesKeuze+2);
		//Wat moet hier om te zorgen dat alleen de goede regels worden getekend?
	}
		
	else if(e.getSource() == aantalOptieBox[1])
		{	 try
			{ 		aantal1Int = Integer.parseInt( aantalOptieBox[1].getText() );
					if(aantal1Int > 0)
						kbip.zetAantalVanOptie(1,aantal1Int);
			}
			catch (Exception p)
			{}
		}
	else if(e.getSource() == aantalOptieBox[2])
	{	 try
		{ 		aantal2Int = Integer.parseInt( aantalOptieBox[2].getText() );
				if(aantal2Int > 0)
					kbip.zetAantalVanOptie(2,aantal2Int);
		}
		catch (Exception p)
		{}
	}
	else if(e.getSource() == aantalOptieBox[3])
	{	 try
		{ 		aantal3Int = Integer.parseInt( aantalOptieBox[3].getText() );
				if(aantal3Int > 0)
					kbip.zetAantalVanOptie(3,aantal3Int);
		}
		catch (Exception p)
		{}
	}
	else if(e.getSource() == aantalOptieBox[4])
	{	 try
		{ 		aantal4Int = Integer.parseInt( aantalOptieBox[4].getText() );
				if(aantal4Int > 0)
					kbip.zetAantalVanOptie(4,aantal4Int);
		}
		catch (Exception p)
		{}
	}
		
}
	
	
}
