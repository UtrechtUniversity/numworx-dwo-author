package fi.kansbomen;

import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;

public class KansbomenInteractiePanel extends JPanel implements ActionListener, FocusListener, InteractiePanel

{
Kansboom kansboom; 

int editWidth = 170;
int editHeight = 450;

int kbipBreedte = 500;
int kbipHoogte = 450;

int kansboomBreedte;
// Even kijken hoe ik bovenstaande handig combineer...


//boolean componentsCreated = false;

boolean teruglegZichtbaar = true;
boolean trekkingZichtbaar = true;
boolean optiesZichtbaar = true;
boolean ballenZichtbaar = true;
boolean legendaZichtbaar = true;
boolean bovenbalkZichtbaar = true;
boolean kijkNa = false;
boolean kleur = true;
int kansVolgordeKeuze = 0;
boolean terugleggen = true;
boolean letter = false;
int terugleggenKeuze = 0;
int labelsKeuze = 0;
int trekkingen = 2;


int offset=5;

int width;
int height;
int currentX;
int currentY;

Font theFont;
FontMetrics theFM;
Font theBoldFont;
FontMetrics theBoldFM;

JComboBox terugleggenBox;

JComboBox trekkingenBox, optiesBox;
JLabel aantalTrekkingen, aantalOptiesLabel, legendaKop;
JLabel[] aantalOptie, legendaOptie;
Color[] kleurRij = new Color[5];
LijntjeLabel[] legendaKleur;
JTextField[] aantalOptieVeld;	
String[] naamOptieTekst;
String[] letterString = new String[] {"d","d","d","d","d"};

int aantalOpties = 2;
int[] aantalInt = new int[]  {4,4,4,4,4};
int[] aantalIntOud = new int[] {4,4,4,4,4};
int breedteAantalVeld;


	public KansbomenInteractiePanel()
	{
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
		
		aantalOptiesLabel = new JLabel(Kansbomen.rb.getString("aantalOptiesTekst"));
		aantalOptiesLabel.setFont(theFont);
		aantalOptiesLabel.setBounds(currentX, currentY, 100, height);
		add(aantalOptiesLabel);
		
		currentX += aantalOptiesLabel.getWidth()+ offset;
		
		String[] optiesKeuzes={"2","3","4"};
		optiesBox = new JComboBox(optiesKeuzes);
		optiesBox.setSelectedIndex(aantalOpties);
		optiesBox.setFont(theFont);
		optiesBox.setBounds(currentX, currentY, width - aantalOptiesLabel.getWidth() - offset, height);
		add(optiesBox);
		optiesBox.addActionListener(this);
		
		currentX -= aantalOptiesLabel.getWidth()+ offset;
		currentY += height + 2 * offset;
		
		breedteAantalVeld = theFM.stringWidth("000")+ 2 * offset;
				                       
		aantalOptieVeld = new JTextField[5];
		for(int i=1; i<5; i++)
		{	aantalOptieVeld[i] = new JTextField(""+aantalInt[i]);
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
		       
		for(int i=1; i<5; i++)
			letterString[i] = naamOptieTekst[i].substring(0,1).toLowerCase();
		                            
		aantalOptie = new JLabel[5];
		for (int i=1; i<5; i++)
		{	aantalOptie[i] = new JLabel(Kansbomen.rb.getString("aantalTekst")+naamOptieTekst[i]+":");
			aantalOptie[i].setFont(theFont);
			aantalOptie[i].setBounds(currentX, currentY, width - breedteAantalVeld, height);
			currentY += height + offset;
		}
		plaatsOptieRegels(aantalOpties+2);
		
		currentY = editHeight - (aantalOpties + 3) * (height + offset);
		
		legendaKop = new JLabel(Kansbomen.rb.getString("legendaTekst"));
		legendaKop.setFont(theBoldFont);
		legendaKop.setBounds(currentX, currentY, width, height);
		add(legendaKop);
		currentY += height + offset;
		
		legendaOptie = new JLabel[5];
		legendaKleur = new LijntjeLabel[5];
		
		if(kleur) 
		{	kleurRij[1] = new Color(0,0,255);
			kleurRij[2] = new Color(0,200,0);
			kleurRij[3] = new Color(255,50,50);
			kleurRij[4] = new Color(0,220,220);
		}
		else
		{	kleurRij[1] = Color.BLACK;
			kleurRij[2] = Color.BLACK;
			kleurRij[3] = Color.BLACK;
			kleurRij[4] = Color.BLACK;
		}
		
		for (int i=1; i<5; i++)
		{
			legendaKleur[i] = new LijntjeLabel(kleurRij[i]);
			legendaKleur[i].setBounds(currentX, currentY, width / 5, height);
			add(legendaKleur[i]);
			currentX += width / 5 + offset;
			
			legendaOptie[i] = new JLabel(naamOptieTekst[i]+" ("+aantalInt[i]+")");
			legendaOptie[i].setFont(theFont);
			legendaOptie[i].setBounds(currentX, currentY, 4* width / 5, height);
			add(legendaOptie[i]);
			currentX -= width / 5 + offset;
			currentY += height + offset;
		}
			
		//currentX += width + offset;
		//componentsCreated = true;
				
	}

	/* Waarschijnlijk toch niet nodig?
	public void plaatsComponenten()
	{
		if (componentsCreated)
		{
			legendaKop.setLocation(offset, editHeight - (aantalOpties + 3) * (height + offset));
			for(int i=1; i<5; i++)
			{
				legendaKleur[i].setLocation(offset, legendaKleur[i].getLocation().y);
				legendaTekst[i].setLocation(offset, naamOptieVeld[i].getLocation().y);
				
			}
		}
	}
	*/
	
	public void plaatsOptieRegels(int j)
	{
		for(int i=1; i<5; i++)
		{	if(j>=i)
			{
				add(aantalOptie[i]);
				add(aantalOptieVeld[i]);
				aantalOptieVeld[i].addActionListener(this);
				aantalOptieVeld[i].addFocusListener(this);
			}
			
		}
	}	
	
	public void setBounds(int x, int y, int b, int h)
	{
		
super.setBounds(x,y,b,h);
	if(teruglegZichtbaar || trekkingZichtbaar || optiesZichtbaar || ballenZichtbaar
			|| legendaZichtbaar)
	{	currentX = width + 2 * offset;
		kansboomBreedte = b - width - 2 * offset;
	}
	else
	{	currentX = offset;
		kansboomBreedte = b - 2 * offset;
	}
if (kansboom == null)
{
		
		super.setBounds(x,y,b,h);
		kansboom = new Kansboom();
		kansboom.setSize(kansboomBreedte, h-2*offset);
		kansboom.setLocation(currentX, offset);
		add(kansboom);
}
else
{
		kansboom.setSize(kansboomBreedte, h-2*offset);
		kansboom.setLocation(currentX + offset, offset);
}

	}

	public void zetTeruglegZichtbaar(boolean b)
	{
		teruglegZichtbaar = b;
		terugleggenBox.setVisible(teruglegZichtbaar);
		setBounds(0, 0, kbipBreedte, kbipHoogte);
	}
	
	public void zetTrekkingZichtbaar(boolean b)
	{
		trekkingZichtbaar = b;
		trekkingenBox.setVisible(trekkingZichtbaar);
		aantalTrekkingen.setVisible(trekkingZichtbaar);
		setBounds(0, 0, kbipBreedte, kbipHoogte);
	}
	
	public void zetOptiesZichtbaar(boolean b)
	{
		optiesZichtbaar = b;
		optiesBox.setVisible(optiesZichtbaar);
		aantalOptiesLabel.setVisible(optiesZichtbaar);
		setBounds(0, 0, kbipBreedte, kbipHoogte);
	}
	
	public void zetBallenZichtbaar(boolean b)
	{
		ballenZichtbaar = b;
		aantalOptie[1].setVisible(b);
		aantalOptieVeld[1].setVisible(b);
		aantalOptie[2].setVisible(b);
		aantalOptieVeld[2].setVisible(b);
	
		if(aantalOpties+2>2)
		{	aantalOptie[3].setVisible(b);
			aantalOptieVeld[3].setVisible(b);
		}	
		else
		{	aantalOptie[3].setVisible(false);
			aantalOptieVeld[3].setVisible(false);
		}
		if(aantalOpties+2>3)
		{	aantalOptie[4].setVisible(b);
			aantalOptieVeld[4].setVisible(b);
		}	
		else
		{	aantalOptie[4].setVisible(false);
			aantalOptieVeld[4].setVisible(false);
		}
		setBounds(0, 0, kbipBreedte, kbipHoogte);
	}
	
	public void zetLegendaZichtbaar(boolean b)
	{
		legendaZichtbaar = b;
		
		legendaKop.setVisible(b);
		legendaKleur[1].setVisible(b);
		legendaOptie[1].setVisible(b);
		legendaKleur[2].setVisible(b);
		legendaOptie[2].setVisible(b);
	
		if(aantalOpties+2>2)
		{	legendaKleur[3].setVisible(b);
			legendaOptie[3].setVisible(b);
		}	
		else
		{	legendaKleur[3].setVisible(false);
			legendaOptie[3].setVisible(false);
		}
		if(aantalOpties+2>3)
		{	legendaKleur[4].setVisible(b);
			legendaOptie[4].setVisible(b);
		}	
		else
		{	legendaKleur[4].setVisible(false);
			legendaOptie[4].setVisible(false);
		}
		setBounds(0, 0, kbipBreedte, kbipHoogte);
	}
	
	public void zetBovenbalkZichtbaar(boolean b)
	{
		bovenbalkZichtbaar = b;
		kansboom.zetBovenbalkZichtbaar(b);
	}
	
	public void zetNaamOptie(int i, String s)
	{
		naamOptieTekst[i] = s;
		aantalOptie[i].setText(Kansbomen.rb.getString("aantalTekst")+s+":");
		zetLegendaTekst(i);
		letterString[i] = s.substring(0,1).toLowerCase();
		kansboom.letter[i] = letterString[i];
		kansboom.repaint();
	}
	
	public void zetLetterOptie(int i, String s)
	{
		letterString[i] = s;
		kansboom.letter[i] = letterString[i];
		kansboom.repaint();
	}
	
	public void zetKleur(boolean b)
	{
		kleur = b;
		kansboom.zetKleur(b);
		zetLegendaKleur(b);
	}
	
	public void zetKansVolgorde(int i)
	{
		kansVolgordeKeuze = i;
		kansboom.zetKansVolgorde(i);
	}

	public void zetTerugleggen(int i)
	{
		terugleggenKeuze = i;
		if(i==0)
			terugleggen = true;
		else if(i==1)
			terugleggen = false;
		kansboom.zetTerugleggen(terugleggen);
		terugleggenBox.setSelectedIndex(terugleggenKeuze);
	}

	public void zetTrekkingen(int i)
	{
		trekkingen = i-1;
		kansboom.zetTrekkingen(i);
		trekkingenBox.setSelectedIndex(trekkingen);
	}
	
	public void zetLabelsKeuze(int i)
	{
		labelsKeuze = i;
		kansboom.zetLabelsKeuze(i);
	}
	
	public void zetAantalOpties(int j, int w3, int w4)
	{
		aantalOpties=j-2;
		kansboom.zetAantalOpties(j, w3, w4);
		optiesBox.setSelectedIndex(j-2);
		
		if (aantalOpties + 2 == 3)
		{
			aantalOptie[4].setVisible(false);
			aantalOptieVeld[4].setVisible(false);
			legendaKleur[4].setVisible(false);
			legendaOptie[4].setVisible(false);
			
			// 3 zichtbaar zetten, kan verborgen zijn.
			// alleen zichtbaar als ze getoond moet worden.
			aantalOptie[3].setVisible(ballenZichtbaar);
			aantalOptieVeld[3].setVisible(ballenZichtbaar);
			legendaKleur[3].setVisible(legendaZichtbaar);
			legendaOptie[3].setVisible(legendaZichtbaar);
			
			currentY = kansboom.HOOGTE - (aantalOpties + 3)* (height + offset);
			currentX = offset;
			legendaKop.setBounds(currentX, currentY, width, height);
			currentY += height + offset;
			
			for (int i=1; i<4; i++)
			{
				legendaKleur[i].setBounds(currentX, currentY, width / 5, height);
				currentX += width / 5 + offset;
				legendaOptie[i].setBounds(currentX, currentY, 4 * width / 5, height);
				currentY += height + offset;
				currentX -= width / 5 + offset;
			}
			
		}
		else if (aantalOpties + 2 == 2)
		{
			aantalOptie[4].setVisible(false);
			aantalOptieVeld[4].setVisible(false);
			legendaKleur[4].setVisible(false);
			legendaOptie[4].setVisible(false);
			
			aantalOptie[3].setVisible(false);
			aantalOptieVeld[3].setVisible(false);
			legendaKleur[3].setVisible(false);
			legendaOptie[3].setVisible(false);
			
			currentY = kansboom.HOOGTE - (aantalOpties +1)* (height + offset);
			currentX = offset;
			legendaKop.setBounds(currentX, currentY, width, height);
			currentY += height + offset;
			
			for (int i=1; i<3; i++)
			{
				legendaKleur[i].setBounds(currentX, currentY, width / 5, height);
				currentX += width / 5 + offset;
				legendaOptie[i].setBounds(currentX, currentY, 4 * width / 5, height);
				currentY += height + offset;
				currentX -= width / 5 + offset;
			}
			
		}
		else
		{
			// 4 zichtbaar zetten, kan verborgen zijn		
			// vakjes voor aantal alleen zichtbaar als ze getoond moet worden.
			aantalOptie[4].setVisible(ballenZichtbaar);
			aantalOptieVeld[4].setVisible(ballenZichtbaar);
			legendaKleur[4].setVisible(legendaZichtbaar);
			legendaOptie[4].setVisible(legendaZichtbaar);
			
			// 3 zichtbaar zetten, kan verborgen zijn
			// vakjes voor aantal alleen zichtbaar als ze getoond moet worden.
			aantalOptie[3].setVisible(ballenZichtbaar);
			aantalOptieVeld[3].setVisible(ballenZichtbaar);
			legendaKleur[3].setVisible(legendaZichtbaar);
			legendaOptie[3].setVisible(legendaZichtbaar);
			
			currentY = kansboom.HOOGTE - (aantalOpties +1)* (height + offset);
			currentX = offset;
			legendaKop.setBounds(currentX, currentY, width, height);
			currentY += height + offset;
			
			for (int i=1; i<5; i++)
			{
				legendaKleur[i].setBounds(currentX, currentY, width / 5, height);
				currentX += width / 5 + offset;
				legendaOptie[i].setBounds(currentX, currentY, 4 * width / 5, height);
				currentY += height + offset;
				currentX -= width / 5 + offset;
			}
		}
	}
	
	public void zetAantalVanOptie(int i, int j)
	{
		aantalInt[i] = j;
		kansboom.zetAantalVanOptie(i,j);
		zetLegendaTekst(i);
		aantalOptieVeld[i].setText(""+aantalInt[i]);
		
	}
	
	public void zetLegendaTekst(int i)
	{
		legendaOptie[i].setText(naamOptieTekst[i]+" ("+aantalInt[i]+")");
	}
	
	public void zetLegendaKleur(boolean b)
	{
		if(b) 
			{	kleurRij[1] = new Color(0,0,255);
				kleurRij[2] = new Color(0,200,0);
				kleurRij[3] = new Color(255,50,50);
				kleurRij[4] = new Color(0,220,220);
			}
			else
			{	kleurRij[1] = Color.BLACK;
				kleurRij[2] = Color.BLACK;
				kleurRij[3] = Color.BLACK;
				kleurRij[4] = Color.BLACK;
			}
		for (int i=1; i<5; i++)
		{
			legendaKleur[i].setColor(kleurRij[i]);
		}
	}	
	
	public void zetKijkNa(boolean b)
	// mogelijk tijdelijke methode; ik moet de nakijkopties nog leren kennen.
	{
		kijkNa = b;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == terugleggenBox)
		{	zetTerugleggen(terugleggenBox.getSelectedIndex());
		}
		else if(e.getSource() == trekkingenBox)
		{	trekkingen = trekkingenBox.getSelectedIndex();
			zetTrekkingen(trekkingen+1);
			//hier set size?
		}
		else if(e.getSource() == optiesBox)
		{
			aantalOpties = optiesBox.getSelectedIndex();
			zetAantalOpties(aantalOpties+2, aantalInt[3], aantalInt[4]);
			
			
		
		}	
		else if(e.getSource() == aantalOptieVeld[1])
		{	 try
			{ 	aantalIntOud[1] = aantalInt[1];
				aantalInt[1] = Integer.parseInt( aantalOptieVeld[1].getText() );
				if(aantalInt[1] > 0)
				{	zetAantalVanOptie(1,aantalInt[1]);
					zetLegendaTekst(1);
				}	
				else 
				{	aantalInt[1] = aantalIntOud[1];
					aantalOptieVeld[1].setText(""+aantalInt[1]);
				}	
			}
			catch (Exception p)
			{aantalOptieVeld[1].setText(""+aantalInt[1]);}
		}
		else if(e.getSource() == aantalOptieVeld[2])
		{	 try
			{ 	aantalIntOud[2] = aantalInt[2];	
				aantalInt[2] = Integer.parseInt( aantalOptieVeld[2].getText() );
				if(aantalInt[2] > 0)
				{	zetAantalVanOptie(2,aantalInt[2]);
					zetLegendaTekst(2);
				}
				else 
				{	aantalInt[2] = aantalIntOud[2];
					aantalOptieVeld[2].setText(""+aantalInt[2]);
				}
		}
		catch (Exception p)
		{aantalOptieVeld[2].setText(""+aantalInt[2]);}
		}
		else if(e.getSource() == aantalOptieVeld[3])
		{	 try
			{ 	aantalIntOud[3] = aantalInt[3];
				aantalInt[3] = Integer.parseInt( aantalOptieVeld[3].getText() );
				if(aantalInt[3] > 0)
				{	zetAantalVanOptie(3,aantalInt[3]);
					zetLegendaTekst(3);
				}
				else 
				{	aantalInt[3] = aantalIntOud[3];
					aantalOptieVeld[3].setText(""+aantalInt[3]);
				}
			}
			catch (Exception p)
			{aantalOptieVeld[3].setText(""+aantalInt[3]);}
		}
		else if(e.getSource() == aantalOptieVeld[4])
		{	 try
			{ 	aantalIntOud[4] = aantalInt[4];	
				aantalInt[4] = Integer.parseInt( aantalOptieVeld[4].getText() );
				if(aantalInt[4] > 0)
				{	zetAantalVanOptie(4,aantalInt[4]);
					zetLegendaTekst(4);
				}
				else
				{	aantalInt[4] = aantalIntOud[4];	
					aantalOptieVeld[4].setText(""+aantalInt[4]);
				}
			}
			catch (Exception p)
			{aantalOptieVeld[4].setText(""+aantalInt[4]);}
		}
		
	}
	
	public void focusLost(FocusEvent e) 
	{
		if(e.getSource() == aantalOptieVeld[1])
		{	 try
			{ 	aantalIntOud[1] = aantalInt[1];
				aantalInt[1] = Integer.parseInt( aantalOptieVeld[1].getText() );
				if(aantalInt[1] > 0)
				{	zetAantalVanOptie(1,aantalInt[1]);
					zetLegendaTekst(1);
				}	
				else 
				{	aantalInt[1] = aantalIntOud[1];
					aantalOptieVeld[1].setText(""+aantalInt[1]);
				}	
			}
			catch (Exception p)
			{aantalOptieVeld[1].setText(""+aantalInt[1]);}
		}
		else if(e.getSource() == aantalOptieVeld[2])
		{	 try
			{ 	aantalIntOud[2] = aantalInt[2];	
				aantalInt[2] = Integer.parseInt( aantalOptieVeld[2].getText() );
				if(aantalInt[2] > 0)
				{	zetAantalVanOptie(2,aantalInt[2]);
					zetLegendaTekst(2);
				}
				else 
				{	aantalInt[2] = aantalIntOud[2];
					aantalOptieVeld[2].setText(""+aantalInt[2]);
				}
		}
		catch (Exception p)
		{aantalOptieVeld[2].setText(""+aantalInt[2]);}
		}
		else if(e.getSource() == aantalOptieVeld[3])
		{	 try
			{ 	aantalIntOud[3] = aantalInt[3];
				aantalInt[3] = Integer.parseInt( aantalOptieVeld[3].getText() );
				if(aantalInt[3] > 0)
				{	zetAantalVanOptie(3,aantalInt[3]);
					zetLegendaTekst(3);
				}
				else 
				{	aantalInt[3] = aantalIntOud[3];
					aantalOptieVeld[3].setText(""+aantalInt[3]);
				}
			}
			catch (Exception p)
			{aantalOptieVeld[3].setText(""+aantalInt[3]);}
		}
		else if(e.getSource() == aantalOptieVeld[4])
		{	 try
			{ 	aantalIntOud[4] = aantalInt[4];	
				aantalInt[4] = Integer.parseInt( aantalOptieVeld[4].getText() );
				if(aantalInt[4] > 0)
				{	zetAantalVanOptie(4,aantalInt[4]);
					zetLegendaTekst(4);
				}
				else
				{	aantalInt[4] = aantalIntOud[4];	
					aantalOptieVeld[4].setText(""+aantalInt[4]);
				}
			}
			catch (Exception p)
			{aantalOptieVeld[4].setText(""+aantalInt[4]);}
		}
		
	}		


	public void zetOpdracht(Hashtable h, String[] randomVars,
			Hashtable randomValues) {

		if (h.containsKey("teruglegZichtbaar"))
			teruglegZichtbaar = ((Boolean) h.get("teruglegZichtbaar")).booleanValue();
		if (h.containsKey("trekkingZichtbaar"))
			trekkingZichtbaar = ((Boolean) h.get("trekkingZichtbaar")).booleanValue();
		if (h.containsKey("optiesZichtbaar"))
			optiesZichtbaar = ((Boolean) h.get("optiesZichtbaar")).booleanValue();
		if (h.containsKey("ballenZichtbaar"))
			ballenZichtbaar = ((Boolean) h.get("ballenZichtbaar")).booleanValue();
		if (h.containsKey("legendaZichtbaar"))
			legendaZichtbaar = ((Boolean) h.get("legendaZichtbaar")).booleanValue();
		if (h.containsKey("bovenbalkZichtbaar"))
			bovenbalkZichtbaar = ((Boolean) h.get("bovenbalkZichtbaar")).booleanValue();
		if (h.containsKey("kijkNa"))
			kijkNa = ((Boolean) h.get("kijkNa")).booleanValue();
		if (h.containsKey("labelsKeuze"))
			labelsKeuze = ((Integer)h.get("labelsKeuze")).intValue();
		if(h.containsKey("kleur"))
			kleur = ((Boolean) h.get("kleur")).booleanValue();
		if (h.containsKey("kansVolgordeKeuze"))
			kansVolgordeKeuze = ((Integer)h.get("kansVolgordeKeuze")).intValue();
		if (h.containsKey("naamOptieTekst[1]"))
			naamOptieTekst[1] = ((String) h.get("naamOptieTekst[1]"));
		if (h.containsKey("naamOptieTekst[2]"))
			naamOptieTekst[2] = ((String) h.get("naamOptieTekst[2]"));
		if (h.containsKey("naamOptieTekst[3]"))
			naamOptieTekst[3] = ((String) h.get("naamOptieTekst[3]"));
		if (h.containsKey("naamOptieTekst[4]"))
			naamOptieTekst[4] = ((String) h.get("naamOptieTekst[4]"));
		if (h.containsKey("letterString[1]"))
			letterString[1] = ((String) h.get("letterString[1]"));
		if (h.containsKey("letterString[2]"))
			letterString[2] = ((String) h.get("letterString[2]"));
		if (h.containsKey("letterString[3]"))
			letterString[3] = ((String) h.get("letterString[3]"));
		if (h.containsKey("letterString[4]"))
			letterString[4] = ((String) h.get("letterString[4]"));
		if(h.containsKey("terugleggenKeuze"))
			terugleggenKeuze = ((Integer)h.get("terugleggenKeuze")).intValue();
		if(h.containsKey("trekkingen"))
			trekkingen = ((Integer)h.get("trekkingen")).intValue();
		if(h.containsKey("aantalOpties"))
			aantalOpties = ((Integer)h.get("aantalOpties")).intValue();
		if(h.containsKey("aantalInt[1]"))
			aantalInt[1] = ((Integer)h.get("aantalInt[1]")).intValue();
		if(h.containsKey("aantalInt[2]"))
			aantalInt[2] = ((Integer)h.get("aantalInt[2]")).intValue();
		if(h.containsKey("aantalInt[3]"))
			aantalInt[3] = ((Integer)h.get("aantalInt[3]")).intValue();
		if(h.containsKey("aantalInt[4]"))
			aantalInt[4] = ((Integer)h.get("aantalInt[4]")).intValue();
		
		zetTeruglegZichtbaar(teruglegZichtbaar);
		zetTrekkingZichtbaar(trekkingZichtbaar);
		zetOptiesZichtbaar(optiesZichtbaar);
		zetBallenZichtbaar(ballenZichtbaar);
		zetLegendaZichtbaar(legendaZichtbaar);
		zetBovenbalkZichtbaar(bovenbalkZichtbaar);
		zetKijkNa(kijkNa);
		zetLabelsKeuze(labelsKeuze);
		zetKleur(kleur);
		zetKansVolgorde(kansVolgordeKeuze);
		zetNaamOptie(1,naamOptieTekst[1]);
		zetNaamOptie(2,naamOptieTekst[2]);
		zetNaamOptie(3,naamOptieTekst[3]);
		zetNaamOptie(4,naamOptieTekst[4]);
		zetLetterOptie(1,letterString[1]);
		zetLetterOptie(2,letterString[2]);
		zetLetterOptie(3,letterString[3]);
		zetLetterOptie(4,letterString[4]);
		zetTerugleggen(terugleggenKeuze);
		zetTrekkingen(trekkingen+1); 		
		zetAantalVanOptie(1,aantalInt[1]);
		zetAantalVanOptie(2,aantalInt[2]);
		zetAantalVanOptie(3,aantalInt[3]);
		zetAantalVanOptie(4,aantalInt[4]);
		zetAantalOpties(aantalOpties+2, aantalInt[3], aantalInt[4] );
			
		
	}


	public void setState(Hashtable h) {
		if(h.containsKey("terugleggenKeuze"))
			terugleggenKeuze = ((Integer)h.get("terugleggenKeuze")).intValue();
		zetTerugleggen(terugleggenKeuze); //gaat dit ook goed met de boolean terugleggen?
		
		if(h.containsKey("trekkingen"))
			trekkingen = ((Integer)h.get("trekkingen")).intValue();
		zetTrekkingen(trekkingen+1); 
		
		if(h.containsKey("aantalOpties"))
			aantalOpties = ((Integer)h.get("aantalOpties")).intValue();
		
		if(h.containsKey("aantalInt[1]"))
			aantalInt[1] = ((Integer)h.get("aantalInt[1]")).intValue();
		if(h.containsKey("aantalInt[2]"))
			aantalInt[2] = ((Integer)h.get("aantalInt[2]")).intValue();
		if(h.containsKey("aantalInt[3]"))
			aantalInt[3] = ((Integer)h.get("aantalInt[3]")).intValue();
		if(h.containsKey("aantalInt[4]"))
			aantalInt[4] = ((Integer)h.get("aantalInt[4]")).intValue();
		
		zetAantalVanOptie(1,aantalInt[1]);
		zetAantalVanOptie(2,aantalInt[2]);
		zetAantalVanOptie(3,aantalInt[3]);
		zetAantalVanOptie(4,aantalInt[4]);
		zetAantalOpties(aantalOpties+2, aantalInt[3], aantalInt[4] );
		
	}


	public void setEditState(Hashtable h) {
		if (h.containsKey("teruglegZichtbaar"))
			teruglegZichtbaar = ((Boolean) h.get("teruglegZichtbaar")).booleanValue();
		if (h.containsKey("trekkingZichtbaar"))
			trekkingZichtbaar = ((Boolean) h.get("trekkingZichtbaar")).booleanValue();
		if (h.containsKey("optiesZichtbaar"))
			optiesZichtbaar = ((Boolean) h.get("optiesZichtbaar")).booleanValue();
		if (h.containsKey("ballenZichtbaar"))
			ballenZichtbaar = ((Boolean) h.get("ballenZichtbaar")).booleanValue();
		if (h.containsKey("legendaZichtbaar"))
			legendaZichtbaar = ((Boolean) h.get("legendaZichtbaar")).booleanValue();
		if (h.containsKey("bovenbalkZichtbaar"))
			bovenbalkZichtbaar = ((Boolean) h.get("bovenbalkZichtbaar")).booleanValue();
		if (h.containsKey("kijkNa"))
			kijkNa = ((Boolean) h.get("kijkNa")).booleanValue();
		if (h.containsKey("labelsKeuze"))
			labelsKeuze = ((Integer)h.get("labelsKeuze")).intValue();
		if(h.containsKey("kleur"))
			kleur = ((Boolean) h.get("kleur")).booleanValue();
		if (h.containsKey("kansVolgordeKeuze"))
			kansVolgordeKeuze = ((Integer)h.get("kansVolgordeKeuze")).intValue();
		if (h.containsKey("naamOptieTekst[1]"))
			naamOptieTekst[1] = ((String) h.get("naamOptieTekst[1]"));
		if (h.containsKey("naamOptieTekst[2]"))
			naamOptieTekst[2] = ((String) h.get("naamOptieTekst[2]"));
		if (h.containsKey("naamOptieTekst[3]"))
			naamOptieTekst[3] = ((String) h.get("naamOptieTekst[3]"));
		if (h.containsKey("naamOptieTekst[4]"))
			naamOptieTekst[4] = ((String) h.get("naamOptieTekst[4]"));
		if (h.containsKey("letterString[1]"))
			letterString[1] = ((String) h.get("letterString[1]"));
		if (h.containsKey("letterString[2]"))
			letterString[2] = ((String) h.get("letterString[2]"));
		if (h.containsKey("letterString[3]"))
			letterString[3] = ((String) h.get("letterString[3]"));
		if (h.containsKey("letterString[4]"))
			letterString[4] = ((String) h.get("letterString[4]"));
		if(h.containsKey("terugleggenKeuze"))
			terugleggenKeuze = ((Integer)h.get("terugleggenKeuze")).intValue();
		if(h.containsKey("trekkingen"))
			trekkingen = ((Integer)h.get("trekkingen")).intValue();
		if(h.containsKey("aantalOpties"))
			aantalOpties = ((Integer)h.get("aantalOpties")).intValue();
		if(h.containsKey("aantalInt[1]"))
			aantalInt[1] = ((Integer)h.get("aantalInt[1]")).intValue();
		if(h.containsKey("aantalInt[2]"))
			aantalInt[2] = ((Integer)h.get("aantalInt[2]")).intValue();
		if(h.containsKey("aantalInt[3]"))
			aantalInt[3] = ((Integer)h.get("aantalInt[3]")).intValue();
		if(h.containsKey("aantalInt[4]"))
			aantalInt[4] = ((Integer)h.get("aantalInt[4]")).intValue();
		
		zetTeruglegZichtbaar(teruglegZichtbaar);
		zetTrekkingZichtbaar(trekkingZichtbaar);
		zetOptiesZichtbaar(optiesZichtbaar);
		zetBallenZichtbaar(ballenZichtbaar);
		zetLegendaZichtbaar(legendaZichtbaar);
		zetBovenbalkZichtbaar(bovenbalkZichtbaar);
		zetKijkNa(kijkNa);
		zetLabelsKeuze(labelsKeuze);
		zetKleur(kleur);
		zetKansVolgorde(kansVolgordeKeuze);
		zetNaamOptie(1,naamOptieTekst[1]);
		zetNaamOptie(2,naamOptieTekst[2]);
		zetNaamOptie(3,naamOptieTekst[3]);
		zetNaamOptie(4,naamOptieTekst[4]);
		zetLetterOptie(1,letterString[1]);
		zetLetterOptie(2,letterString[2]);
		zetLetterOptie(3,letterString[3]);
		zetLetterOptie(4,letterString[4]);
		zetTerugleggen(terugleggenKeuze);
		zetTrekkingen(trekkingen+1); 		
		zetAantalVanOptie(1,aantalInt[1]);
		zetAantalVanOptie(2,aantalInt[2]);
		zetAantalVanOptie(3,aantalInt[3]);
		zetAantalVanOptie(4,aantalInt[4]);
		zetAantalOpties(aantalOpties+2, aantalInt[3], aantalInt[4] );		
		
	}


	public Hashtable getState() {
		int terugleggenKeuze = 0;
		int trekkingen = 2;
		int aantalOpties = 2;
		int[] aantalInt = {4,4,4,4,4};
		
		terugleggenKeuze = this.terugleggenKeuze;
		trekkingen = this.trekkingen;
		aantalOpties = this.aantalOpties;
		aantalInt[1] = this.aantalInt[1];
		aantalInt[2] = this.aantalInt[2];
		aantalInt[3] = this.aantalInt[3];
		aantalInt[4] = this.aantalInt[4];
		
		Hashtable h = new Hashtable();
		
		h.put("terugleggenKeuze", terugleggenKeuze);
		h.put("trekkingen", trekkingen);
		h.put("aantalOpties", aantalOpties);
		h.put("aantalInt[1]", aantalInt[1]);
		h.put("aantalInt[2]", aantalInt[2]);
		h.put("aantalInt[3]", aantalInt[3]);
		h.put("aantalInt[4]", aantalInt[4]);
		
		return h;
	}


	public Hashtable getEditState() {
		boolean teruglegZichtbaar = true;
		boolean trekkingZichtbaar = true;
		boolean optiesZichtbaar = true;
		boolean ballenZichtbaar = true;
		boolean legendaZichtbaar = true;
		boolean bovenbalkZichtbaar = true;
		boolean kijkNa = false;
		int labelsKeuze = 0;
		boolean kleur = true;
		int kansVolgordeKeuze = 0;
		String[] naamOptieTekst = {"dummy", "dummy", "dummy","dummy", "dummy"};
		String[] letterString = {"d", "d", "d", "d", "d"};	
		int terugleggenKeuze = 0;
		int trekkingen = 2;
		int aantalOpties = 2;
		int[] aantalInt = {4,4,4,4,4};
		
		//boolean letter = false;
		
		
		teruglegZichtbaar = this.teruglegZichtbaar;
		trekkingZichtbaar = this.trekkingZichtbaar;
		optiesZichtbaar = this.optiesZichtbaar;
		ballenZichtbaar = this.ballenZichtbaar;
		legendaZichtbaar = this.legendaZichtbaar;
		bovenbalkZichtbaar = this.bovenbalkZichtbaar;
		kijkNa = this.kijkNa;
		labelsKeuze = this.labelsKeuze;
		kleur = this.kleur;
		kansVolgordeKeuze = this.kansVolgordeKeuze;
		naamOptieTekst[1] = this.naamOptieTekst[1];
		naamOptieTekst[2] = this.naamOptieTekst[2];
		naamOptieTekst[3] = this.naamOptieTekst[3];
		naamOptieTekst[4] = this.naamOptieTekst[4];
		letterString[1] = this.letterString[1];
		letterString[2] = this.letterString[2];
		letterString[3] = this.letterString[3];
		letterString[4] = this.letterString[4];
		terugleggenKeuze = this.terugleggenKeuze;
		trekkingen = this.trekkingen;
		aantalOpties = this.aantalOpties;
		aantalInt[1] = this.aantalInt[1];
		aantalInt[2] = this.aantalInt[2];
		aantalInt[3] = this.aantalInt[3];
		aantalInt[4] = this.aantalInt[4];
		
		
		//letter = this.letter;
				
		Hashtable h = new Hashtable();
		
		h.put("teruglegZichtbaar", teruglegZichtbaar);
		h.put("trekkingZichtbaar", trekkingZichtbaar);
		h.put("optiesZichtbaar", optiesZichtbaar);
		h.put("ballenZichtbaar", ballenZichtbaar);
		h.put("legendaZichtbaar", legendaZichtbaar);
		h.put("bovenbalkZichtbaar", bovenbalkZichtbaar);
		h.put("kijkNa", kijkNa);
		h.put("labelsKeuze", labelsKeuze);
		h.put("kleur", kleur);
		h.put("kansVolgordeKeuze", kansVolgordeKeuze);
		h.put("naamOptieTekst[1]", naamOptieTekst[1]);
		h.put("naamOptieTekst[2]", naamOptieTekst[2]);
		h.put("naamOptieTekst[3]", naamOptieTekst[3]);
		h.put("naamOptieTekst[4]", naamOptieTekst[4]);
		h.put("letterString[1]", letterString[1]);
		h.put("letterString[2]", letterString[2]);
		h.put("letterString[3]", letterString[3]);
		h.put("letterString[4]", letterString[4]);
		h.put("terugleggenKeuze", terugleggenKeuze);
		h.put("trekkingen", trekkingen);
		h.put("aantalOpties", aantalOpties);
		h.put("aantalInt[1]", aantalInt[1]);
		h.put("aantalInt[2]", aantalInt[2]);
		h.put("aantalInt[3]", aantalInt[3]);
		h.put("aantalInt[4]", aantalInt[4]);
		h.put("kbipBreedte", kbipBreedte);
		h.put("kbipHoogte", kbipHoogte);
		
		// kbip breedte en hoogte toevoegen?

		return h;
	}


	public InteractieEditPanel getEditPanel() {
		// TODO Auto-generated method stub
		return new KansbomenInteractieEditPanel();
	}


	public void wis() {
		// TODO Auto-generated method stub
		
	}


	public void zetMaat() {
		// TODO Auto-generated method stub
		
	}


	public int geefAsHoogte() {
		// TODO Auto-generated method stub
		return 0;
	}


	public int getIpId() {
		// TODO Auto-generated method stub
		return 0;
	}


	public int getScore() {
		// TODO Auto-generated method stub
		return 0;
	}


	public int getScoreMax() {
		// TODO Auto-generated method stub
		return 0;
	}


	public boolean isCorrect() {
		// TODO Auto-generated method stub
		return false;
	}


	public boolean isFout() {
		// TODO Auto-generated method stub
		return false;
	}


	public void zetMode(int mode) {
		// TODO Auto-generated method stub
		
	}


	public void zetNagekeken(boolean b) {
		// TODO Auto-generated method stub
		
	}


	public void stop() {
		// TODO Auto-generated method stub
		
	}


	public void start() {
		// TODO Auto-generated method stub
		
	}


	public void destroy() {
		// TODO Auto-generated method stub
		
	}


	public void opnieuw() {
		// TODO Auto-generated method stub
		
	}


	public void kijkNa() {
		// TODO Auto-generated method stub
		
	}


	public void kijkNa(int stapNr) {
		// TODO Auto-generated method stub
		
	}


	public void addActionListener(ActionListener al) {
		// TODO Auto-generated method stub
		
	}


	public void focusGained(FocusEvent arg0) {
		
	}


}
