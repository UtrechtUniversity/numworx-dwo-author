package fi.kansbomen;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class KansbomenInteractiePanel extends JPanel implements ActionListener

{
Kansboom kansboom; 

int editWidth = 170;
int editHeight = 450;

int offset=5;
int keuzeMomenten;
int aantalKeuzes;

int width;
int height;
int currentX;
int currentY;

Font theFont;
FontMetrics theFM;
Font theBoldFont;
FontMetrics theBoldFM;

JComboBox terugleggenBox;
int terugleggenKeuze = 0;
boolean terugleggen;

JComboBox trekkingenBox, optiesBox;
JLabel aantalTrekkingen, aantalOpties, legendaKop;
JLabel[] aantalOptie, legendaOptie;
JTextField[] aantalOptieVeld;	
String[] aantalString;
String[] naamOptieTekst;


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
int breedteAantalVeld;


	public KansbomenInteractiePanel()
	{
		//terugleggenBox = new JCheckBox();
		//ik wil mijn kansboom natuurlijk pas toevoegen als ik alle instellingen uit het edit-panel heb verwerkt.
		//en ik wil dat mijn kansboom zich aanpast als ik hier instellingen verander. 
		setLayout(null);
		
		theFont = new Font("Dialog", Font.PLAIN, 12);
		theFM = getFontMetrics(theFont);
		theBoldFont = new Font("Dialog", Font.BOLD, 12);
		theBoldFM = getFontMetrics(theBoldFont);
		
		width = editWidth - 2 * offset;
		height = 3 * theFM.getHeight() / 2;
		currentX = offset;
		currentY = offset;
		
		
		String[] teruglegKeuzes = { Kansbomen.rb.getString("metTerugleggenTekst"), Kansbomen.rb.getString("zonderTerugleggenTekst")};
		terugleggenBox = new JComboBox(teruglegKeuzes);
		terugleggenBox.setSelectedIndex(terugleggenKeuze);
		terugleggenBox.setFont(theFont);
		terugleggenBox.setBounds(currentX, currentY, width, height);
		add(terugleggenBox);
		terugleggenBox.addActionListener(this);
		
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
		
		breedteAantalVeld = theFM.stringWidth("000")+ 2 * offset;
		aantalString = new String[5];
		aantalString[1] = aantal1String;
		aantalString[2] = aantal2String;
		aantalString[3] = aantal3String;
		aantalString[4] = aantal4String;
		                       
		aantalOptieVeld = new JTextField[5];
		for(int i=1; i<5; i++)
		{	aantalOptieVeld[i] = new JTextField(aantalString[i]);
			aantalOptieVeld[i].setFont(theFont);
			aantalOptieVeld[i].setBounds(currentX + width - breedteAantalVeld, currentY, breedteAantalVeld, height);
			currentY += height + offset;
		}
		
		
		
		currentY -= 4 * height + 4 * offset;
		
		naamOptieTekst = new String[5];
		naamOptieTekst[1] = Kansbomen.rb.getString("naam1StringTekst");
		naamOptieTekst[2] = Kansbomen.rb.getString("naam2StringTekst");
		naamOptieTekst[3] = Kansbomen.rb.getString("naam3StringTekst");
		naamOptieTekst[4] = Kansbomen.rb.getString("naam4StringTekst");
		                            
		                            
		aantalOptie = new JLabel[5];
		for (int i=1; i<5; i++)
		{	aantalOptie[i] = new JLabel(Kansbomen.rb.getString("aantalTekst")+naamOptieTekst[i]+":");
			aantalOptie[i].setFont(theFont);
			aantalOptie[i].setBounds(currentX, currentY, width - breedteAantalVeld, height);
			currentY += height + offset;
		}
		plaatsOptieRegels(aantalOptiesKeuze+2);
		
		currentY = kansboom.HOOGTE - (aantalOptiesKeuze +1)* (height + offset);
		
		legendaKop = new JLabel(Kansbomen.rb.getString("legendaTekst"));
		legendaKop.setFont(theBoldFont);
		legendaKop.setBounds(currentX, currentY, width, height);
		add(legendaKop);
		currentY += height + offset;
		
		legendaOptie = new JLabel[5];
		for (int i=1; i<5; i++)
		{
			legendaOptie[i] = new JLabel(naamOptieTekst[i]+" ("+aantalString[i]+")");
			legendaOptie[i].setFont(theFont);
			legendaOptie[i].setBounds(currentX, currentY, width, height);
			add(legendaOptie[i]);
			currentY += height + offset;
		}
		
		
		
		
		currentX = width + offset;
				
		
	}

	
	public void plaatsOptieRegels(int j)
	{
		for(int i=1; i<5; i++)
		{	if(j>=i)
			{
				add(aantalOptie[i]);
				add(aantalOptieVeld[i]);
				aantalOptieVeld[i].addActionListener(this);
			}
			
		}
	}	
	
	public void setBounds(int x, int y, int b, int h)
	{
// Huub: deze methode wordt alleen in de DWO gebruikt en
// gaat mogelijk niet goed: kansboom is een zichtbaar JPanel,
// dat je plotsklaps opnieuw initialiseert; kijk dus even of
// de kansboom er al is, dan behoeft deze alleen een setSize
		
		
super.setBounds(x,y,b,h);
		
if (kansboom == null)
{
		//super.setBounds(x,y,b,h);
		kansboom = new Kansboom();
		kansboom.setSize(b-width-2*offset, h-2*offset);
		kansboom.setLocation(currentX+offset,offset);
		add(kansboom);
}
else
{
		kansboom.setSize(b-2*offset, h-2*offset);
}
	}

	// als de opbouw zo blijft als nu, dan kunnen al deze methodes weg en 
	// kan ik in de actionperformed steeds direct het commando neerzetten.
	
	public void zetKleur(boolean b)
	{
		kansboom.zetKleur(b);
	}
	
	public void zetVolgorde(boolean b)
	{
		kansboom.zetVolgorde(b);
	}

	public void zetTerugleggen(boolean b)
	{
		kansboom.zetTerugleggen(b);
	}

	public void zetKeuzeMomenten(int i)
	{
		kansboom.zetKeuzeMomenten(i);
	}
	
	public void zetLabelsKeuze(int i)
	{
		kansboom.zetLabelsKeuze(i);
	}
	
	public void zetAantalOpties(int i, int w3, int w4)
	{
		kansboom.zetAantalOpties(i, w3, w4);
	}
	
	public void zetAantalVanOptie(int i, int j)
	{
		kansboom.zetAantalVanOptie(i,j);
	}
	
	public void updateLegendaTekst(int i)
	{
		legendaOptie[i].setText(naamOptieTekst[i]+" ("+aantalString[i]+")");
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == terugleggenBox)
		{	if(terugleggenBox.getSelectedIndex()==0)
			{	terugleggenKeuze = 0;
				terugleggen=true;
			}
			else if(terugleggenBox.getSelectedIndex()==1)
			{	terugleggenKeuze = 1;
				terugleggen=false;
			}
			zetTerugleggen(terugleggen);
		}
		else if(e.getSource() == trekkingenBox)
		{	trekkingen = trekkingenBox.getSelectedIndex();
			zetKeuzeMomenten(trekkingen+1);
		}
		else if(e.getSource() == optiesBox)
		{
			aantalOptiesKeuze = optiesBox.getSelectedIndex();
			zetAantalOpties(aantalOptiesKeuze+2, aantal3Int, aantal4Int);
			
			
			if (aantalOptiesKeuze + 2 == 3)
			{
				aantalOptie[4].setVisible(false);
				aantalOptieVeld[4].setVisible(false);
				legendaOptie[4].setVisible(false);
				
				// 3 zichtbaar zetten, kan verborgen zijn
				aantalOptie[3].setVisible(true);
				aantalOptieVeld[3].setVisible(true);
				legendaOptie[3].setVisible(true);
				
				currentY = kansboom.HOOGTE - (aantalOptiesKeuze +1)* (height + offset);
				currentX = offset;
				legendaKop.setBounds(currentX, currentY, width, height);
				currentY += height + offset;
				
				for (int i=1; i<4; i++)
				{
					legendaOptie[i].setBounds(currentX, currentY, width, height);
					currentY += height + offset;
				}
				
				
			}
			else if (aantalOptiesKeuze + 2 == 2)
			{
				aantalOptie[4].setVisible(false);
				aantalOptieVeld[4].setVisible(false);
				legendaOptie[4].setVisible(false);
				
				aantalOptie[3].setVisible(false);
				aantalOptieVeld[3].setVisible(false);
				legendaOptie[3].setVisible(false);
				
				currentY = kansboom.HOOGTE - (aantalOptiesKeuze +1)* (height + offset);
				currentX = offset;
				legendaKop.setBounds(currentX, currentY, width, height);
				currentY += height + offset;
				
				for (int i=1; i<3; i++)
				{
					legendaOptie[i].setBounds(currentX, currentY, width, height);
					currentY += height + offset;
				}
				
			}
			else
			{
				// 4 zichtbaar zetten, kan verborgen zijn			
				aantalOptie[4].setVisible(true);
				aantalOptieVeld[4].setVisible(true);
				legendaOptie[4].setVisible(true);
				
				// 3 zichtbaar zetten, kan verborgen zijn			
				aantalOptie[3].setVisible(true);
				aantalOptieVeld[3].setVisible(true);
				legendaOptie[3].setVisible(true);
				
				currentY = kansboom.HOOGTE - (aantalOptiesKeuze +1)* (height + offset);
				currentX = offset;
				legendaKop.setBounds(currentX, currentY, width, height);
				currentY += height + offset;
				
				for (int i=1; i<5; i++)
				{
					legendaOptie[i].setBounds(currentX, currentY, width, height);
					currentY += height + offset;
				}
			}
		
		}	
		else if(e.getSource() == aantalOptieVeld[1])
		{	 try
			{ 		aantal1Int = Integer.parseInt( aantalOptieVeld[1].getText() );
					if(aantal1Int > 0)
					{	zetAantalVanOptie(1,aantal1Int);
						aantalString[1] = "" + aantal1Int;
						updateLegendaTekst(1);
					}	
					else 
						aantalOptieVeld[1].setText(aantalString[1]);
			}
			catch (Exception p)
			{aantalOptieVeld[1].setText(aantalString[1]);}
		}
		else if(e.getSource() == aantalOptieVeld[2])
		{	 try
			{ 		aantal2Int = Integer.parseInt( aantalOptieVeld[2].getText() );
				if(aantal2Int > 0)
				{	zetAantalVanOptie(2,aantal2Int);
					aantalString[2] = "" + aantal2Int;
					updateLegendaTekst(2);
				}
				else 
					aantalOptieVeld[2].setText(aantalString[2]);
		}
		catch (Exception p)
		{aantalOptieVeld[2].setText(aantalString[2]);}
		}
		else if(e.getSource() == aantalOptieVeld[3])
		{	 try
			{ 		aantal3Int = Integer.parseInt( aantalOptieVeld[3].getText() );
					if(aantal3Int > 0)
					{	zetAantalVanOptie(3,aantal3Int);
						aantalString[3] = "" + aantal3Int;
						updateLegendaTekst(3);
					}
					else 
						aantalOptieVeld[3].setText(aantalString[3]);
			}
			catch (Exception p)
			{aantalOptieVeld[3].setText(aantalString[3]);}
		}
		else if(e.getSource() == aantalOptieVeld[4])
		{	 try
			{ 		aantal4Int = Integer.parseInt( aantalOptieVeld[4].getText() );
					if(aantal4Int > 0)
					{	zetAantalVanOptie(4,aantal4Int);
						aantalString[4] = "" + aantal4Int;
						updateLegendaTekst(4);
					}
					else
						aantalOptieVeld[4].setText(aantalString[4]);
			}
			catch (Exception p)
			{aantalOptieVeld[4].setText(aantalString[4]);}
		}
		
	}
}
