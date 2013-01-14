package fi.kansbomen;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
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

//boolean componentsCreated = false;

boolean teruglegZichtbaar = true;
boolean trekkingZichtbaar = true;
boolean optiesZichtbaar = true;
boolean ballenZichtbaar = true;
boolean legendaZichtbaar = true;
boolean bovenbalkZichtbaar = true;
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
Color[] kleurRij = new Color[7];
Color[] gekleurdeRij, zwarteRij;
LijntjeLabel[] legendaKleur;
JTextField[] aantalOptieVeld;	
String[] naamOptieTekst;
String[] letterString = new String[7]; //{"d","d","d","d","d","d","d"}
String trekkingTekst = Kansbomen.rb.getString("trekkingBalkTekst");

int aantalOpties = 2;
int[] aantalInt = new int[] {4,4,4,4,4,4,4};
int[] aantalIntOud = new int[] {4,4,4,4,4,4,4};
int breedteAantalVeld;

boolean kijkNaActief;
JButton kijkNaButton;
JPanel kijkNaPanel;
JLabel groenVinkjeLabel;
JLabel geelVinkjeLabel;
JLabel kruisjeLabel;

int score;
int scoreMax = 10;

Vector listeners = new Vector();

int[] nakijkModel = new int[] {10, 4, 4, 4, 4, 4, 4, 0, 2, 2};
int[] leerlingAntwoorden = new int[] {0, 4, 4, 4, 4, 4, 4, 0, 2, 2};
int[] beginStatus = new int[] {0, 4, 4, 4, 4, 4, 4, 0, 2, 2};

private boolean ingevuld;
private boolean nagekeken;
private int mode;

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
		//currentY = offset;
		
		gekleurdeRij = new Color[7];
		gekleurdeRij[1] = new Color(0,0,255);
		gekleurdeRij[2] = new Color(0,200,0);
		gekleurdeRij[3] = new Color(255,50,50);
		gekleurdeRij[4] = new Color(0,220,220);
		gekleurdeRij[5] = new Color(255,180,0);
		gekleurdeRij[6] = new Color(220,0,220);
		
		zwarteRij = new Color[7];
		for(int i = 1; i < 7; i++)
			zwarteRij[i] = new Color(0,0,0);		
	
		String[] teruglegKeuzes = { Kansbomen.rb.getString("metTerugleggenTekst"), Kansbomen.rb.getString("zonderTerugleggenTekst")};
		terugleggenBox = new JComboBox(teruglegKeuzes);
		terugleggenBox.setSelectedIndex(terugleggenKeuze);
		terugleggenBox.setFont(theFont);
		terugleggenBox.setBounds(currentX, currentY, width, height);
		add(terugleggenBox);
		terugleggenBox.addActionListener(this);
		
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
		
		aantalOptiesLabel = new JLabel(Kansbomen.rb.getString("aantalOptiesTekst"));
		aantalOptiesLabel.setFont(theFont);
		aantalOptiesLabel.setBounds(currentX, currentY, 100, height);
		add(aantalOptiesLabel);
		
		currentX += aantalOptiesLabel.getWidth()+ offset;
		
		String[] optiesKeuzes={"2","3","4","5","6"};
		optiesBox = new JComboBox(optiesKeuzes);
		optiesBox.setSelectedIndex(aantalOpties);
		optiesBox.setFont(theFont);
		optiesBox.setBounds(currentX, currentY, width - aantalOptiesLabel.getWidth() - offset, height);
		add(optiesBox);
		optiesBox.addActionListener(this);
		
		currentX -= aantalOptiesLabel.getWidth()+ offset;
		
		breedteAantalVeld = theFM.stringWidth("000")+ 2 * offset;
				                       
		aantalOptieVeld = new JTextField[7];
		for(int i=1; i<7; i++)
		{	aantalOptieVeld[i] = new JTextField(""+aantalInt[i]);
			aantalOptieVeld[i].setFont(theFont);
			aantalOptieVeld[i].setBounds(currentX + width - breedteAantalVeld, currentY, breedteAantalVeld, height);
		}
			
		naamOptieTekst = new String[7];
		naamOptieTekst[1] = Kansbomen.rb.getString("naam1StringTekst");
		naamOptieTekst[2] = Kansbomen.rb.getString("naam2StringTekst");
		naamOptieTekst[3] = Kansbomen.rb.getString("naam3StringTekst");
		naamOptieTekst[4] = Kansbomen.rb.getString("naam4StringTekst");
		naamOptieTekst[5] = Kansbomen.rb.getString("naam5StringTekst");
		naamOptieTekst[6] = Kansbomen.rb.getString("naam6StringTekst");
		       
		for(int i=1; i<7; i++)
			letterString[i] = naamOptieTekst[i].substring(0,1).toLowerCase();
		                            
		aantalOptie = new JLabel[7];
		for (int i=1; i<7; i++)
		{	aantalOptie[i] = new JLabel(Kansbomen.rb.getString("aantalTekst")+naamOptieTekst[i]+":");
			aantalOptie[i].setFont(theFont);
			aantalOptie[i].setBounds(currentX, currentY, width - breedteAantalVeld, height);
		}
		for(int i=1; i<7; i++)
	{		add(aantalOptie[i]);
			add(aantalOptieVeld[i]);
			aantalOptieVeld[i].addActionListener(this);
			aantalOptieVeld[i].addFocusListener(this);
		}
		
		legendaKop = new JLabel(Kansbomen.rb.getString("legendaTekst"));
		legendaKop.setFont(theBoldFont);
		legendaKop.setBounds(currentX, currentY, width, height);
		add(legendaKop);
		
		legendaOptie = new JLabel[7];
		legendaKleur = new LijntjeLabel[7];
		
		if(kleur) 
			kleurRij = gekleurdeRij;
		else
			kleurRij = zwarteRij;
		
		for (int i=1; i<7; i++)
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
		}
			
		kijkNaButton = new JButton(Kansbomen.rb.getString("kijkNaTekst"));
		kijkNaButton.setFont(new Font("SansSerif",Font.PLAIN, 12));
		kijkNaButton.setBounds(0, 0, 75, 24);
		kijkNaButton.addActionListener(this);
		
		java.net.URL imageURL = Kansbomen.class.getResource("resources/goedkrul_en.gif");
		if (imageURL != null)
		{
		    groenVinkjeLabel = new JLabel(new ImageIcon(imageURL));
		}
		else 
		{
			System.out.println("Error reading goedkrul_en.gif.");
			groenVinkjeLabel = new JLabel();
		}
		groenVinkjeLabel.setBounds(76, 2, 20, 20);
		
		imageURL = Kansbomen.class.getResource("resources/goedkrulhalf.gif");
		if (imageURL != null) 
		{
		    geelVinkjeLabel = new JLabel(new ImageIcon(imageURL));
		}
		else 
		{
			System.out.println("Error reading goedkrulhalf.gif.");
			geelVinkjeLabel = new JLabel();
		}
		geelVinkjeLabel.setBounds(76, 2, 20, 20);
		
		
		imageURL = Kansbomen.class.getResource("resources/foutkruis.gif");
		if (imageURL != null) 
		{
		    kruisjeLabel = new JLabel(new ImageIcon(imageURL));
		}
		else 
		{
			System.out.println("Error reading foutkruis.gif.");
			kruisjeLabel = new JLabel();
		}
		kruisjeLabel.setBounds(76, 2, 20, 20);
		
		groenVinkjeLabel.setVisible(false);
		geelVinkjeLabel.setVisible(false);
		kruisjeLabel.setVisible(false);
		
		kijkNaPanel = new JPanel(null);
		kijkNaPanel.setOpaque(false);
		kijkNaPanel.setBounds(editWidth - 95, editHeight-height, 95, 24);
		kijkNaPanel.add(kijkNaButton);
		kijkNaPanel.add(groenVinkjeLabel);
		kijkNaPanel.add(geelVinkjeLabel);
		kijkNaPanel.add(kruisjeLabel);
		kijkNaPanel.setVisible(false);
		this.add(kijkNaPanel, 0);
		
		zetLegendaZichtbaar(true);
	}
	
	public void vernieuwLayoutLinks()
	{
		currentY = offset;
		if (terugleggenBox.isVisible())
		{	
			terugleggenBox.setLocation(terugleggenBox.getLocation().x, currentY);
		    currentY += terugleggenBox.getSize().height + offset;
		}
		if (aantalTrekkingen.isVisible())
		{	
			aantalTrekkingen.setLocation(aantalTrekkingen.getLocation().x, currentY);
		    trekkingenBox.setLocation(trekkingenBox.getLocation().x, currentY);
		    currentY += aantalTrekkingen.getSize().height + offset;
		}
		if (aantalOptiesLabel.isVisible())
		{	
			aantalOptiesLabel.setLocation(aantalOptiesLabel.getLocation().x, currentY);
			optiesBox.setLocation(optiesBox.getLocation().x, currentY);
		    currentY += aantalOptiesLabel.getSize().height + offset;
		}
		for(int i = 1; i<7; i++)
		{	
			if (aantalOptieVeld[i].isVisible())
			{	
				aantalOptieVeld[i].setLocation(aantalOptieVeld[i].getLocation().x, currentY);
				aantalOptie[i].setLocation(aantalOptie[i].getLocation().x, currentY);
			    currentY += aantalOptieVeld[i].getSize().height + offset;
			}
		}
		if (legendaKop.isVisible())
		{	currentY += offset;
			legendaKop.setLocation(legendaKop.getLocation().x, currentY);
		    currentY += legendaKop.getSize().height + offset;
		}
		for(int i = 1; i<7; i++)
		{
			if (legendaOptie[i].isVisible())
			{	
				legendaOptie[i].setLocation(legendaOptie[i].getLocation().x, currentY);
				legendaKleur[i].setLocation(legendaKleur[i].getLocation().x, currentY);
			    currentY += legendaOptie[i].getSize().height;
			}
		}
		
		if (kijkNaPanel.isVisible())
			kijkNaPanel.setLocation(kijkNaPanel.getLocation().x, kbipHoogte-24);
		
	}
	
	public void setBounds(int x, int y, int b, int h)
	{
		super.setBounds(x,y,b,h);
		if(teruglegZichtbaar || trekkingZichtbaar || optiesZichtbaar || ballenZichtbaar
				|| legendaZichtbaar)
		{	currentX = width + 2 * offset;
			kansboomBreedte = b - width - 3 * offset;
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
		zetOpties(aantalOpties,aantalInt);
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
		for(int p = 1; p < 7; p++)
		{	if(aantalOpties+2 > p-1)
			{	aantalOptie[p].setVisible(b);
				aantalOptieVeld[p].setVisible(b);
			}
			else
			{	aantalOptie[p].setVisible(false);
				aantalOptieVeld[p].setVisible(false);
			}
		}
		setBounds(0, 0, kbipBreedte, kbipHoogte);
	}
	
	public void zetLegendaZichtbaar(boolean b)
	{
		legendaZichtbaar = b;
		legendaKop.setVisible(b);
		
		for(int p = 1; p < 7; p++)
		{	if(aantalOpties+2 > p-1)
			{	legendaKleur[p].setVisible(b);
				legendaOptie[p].setVisible(b);
			}	
			else
			{	legendaKleur[p].setVisible(false);
				legendaOptie[p].setVisible(false);
			}
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
	
	public void zetOpties(int k, int[] opties)
	{	aantalInt = opties;
		aantalOpties = k;
		kansboom.zetOpties(k, opties);
		for(int i = 1; i<aantalInt.length; i++)
		{	zetLegendaTekst(i);
			aantalOptieVeld[i].setText(""+aantalInt[i]);
		}
		optiesBox.setSelectedIndex(k);
		for(int p = 1; p < 7; p++)
		{	if(aantalOpties + 2 > p-1)
			{
				aantalOptie[p].setVisible(ballenZichtbaar);
				aantalOptieVeld[p].setVisible(ballenZichtbaar);
				legendaKleur[p].setVisible(legendaZichtbaar);
				legendaOptie[p].setVisible(legendaZichtbaar);
			}
			else
			{
				aantalOptie[p].setVisible(false);
				aantalOptieVeld[p].setVisible(false);
				legendaKleur[p].setVisible(false);
				legendaOptie[p].setVisible(false);
			}
		}
		vernieuwLayoutLinks();
	}
	
	public void zetLegendaTekst(int i)
	{
		legendaOptie[i].setText(naamOptieTekst[i]+" ("+aantalInt[i]+")");
	}
	
	public void zetLegendaKleur(boolean b)
	{
		if(b) 
			kleurRij = gekleurdeRij;	
		else
			kleurRij = zwarteRij;			
		
		for (int i = 1; i < 7; i++)
			legendaKleur[i].setColor(kleurRij[i]);
	}	
	
	public void zetTrekkingTekst(String s)
	{
		trekkingTekst = s;
		kansboom.trekkingTekst = s;
		kansboom.repaint();
	}
	
	public void zetKijkNa(boolean b)
	{
		kijkNaActief = b;
		kijkNaPanel.setVisible(kijkNaActief);
	}
	
	
	public void zetNakijkModel(int[] waardes)
	{
		scoreMax = waardes[0];
		nakijkModel = waardes;
	}
	
	public void updateLeerlingAntwoorden()
	{
		leerlingAntwoorden[0] = beginStatus[0];
		for(int i = 1; i < 7; i++)
			leerlingAntwoorden[i] = aantalInt[i];
		leerlingAntwoorden[7] = terugleggenKeuze;
		leerlingAntwoorden[8] = trekkingen;
		leerlingAntwoorden[9] = aantalOpties;	
	}
	
	public void zetBeginStatus()
	{
		for(int i = 1; i < 7; i++)
			beginStatus[i] = aantalInt[i];
		beginStatus[7] = terugleggenKeuze;
		beginStatus[8] = trekkingen;
		beginStatus[9] = aantalOpties;
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
			zetOpties(aantalOpties, aantalInt);
		
		}	
		else if(e.getSource() == aantalOptieVeld[1])
			actieAantalOptieVeld(1);
		else if(e.getSource() == aantalOptieVeld[2])
			actieAantalOptieVeld(2);
		else if(e.getSource() == aantalOptieVeld[3])
			actieAantalOptieVeld(3);
		else if(e.getSource() == aantalOptieVeld[4])
			actieAantalOptieVeld(4);
		else if(e.getSource() == aantalOptieVeld[5])
			actieAantalOptieVeld(5);
		else if(e.getSource() == aantalOptieVeld[6])
			actieAantalOptieVeld(6);
		else if(e.getSource() == kijkNaButton)
		{
			kijkNa();
		}	
	}
	
	public void focusLost(FocusEvent e) 
	{
		if(e.getSource() == aantalOptieVeld[1])
			actieAantalOptieVeld(1);
		else if(e.getSource() == aantalOptieVeld[2])
			actieAantalOptieVeld(2);
		else if(e.getSource() == aantalOptieVeld[3])
			actieAantalOptieVeld(3);
		else if(e.getSource() == aantalOptieVeld[4])
			actieAantalOptieVeld(4);
		else if(e.getSource() == aantalOptieVeld[5])
			actieAantalOptieVeld(5);
		else if(e.getSource() == aantalOptieVeld[6])
			actieAantalOptieVeld(6);
	}
	
	public void actieAantalOptieVeld(int i)
	{
		try
		{ 	aantalIntOud[i] = aantalInt[i];
			aantalInt[i] = Integer.parseInt( aantalOptieVeld[i].getText() );
			if(aantalInt[i] > 0)
			{	zetOpties(aantalOpties, aantalInt);
				zetLegendaTekst(i);
			}	
			else 
			{	aantalInt[i] = aantalIntOud[i];
				aantalOptieVeld[i].setText(""+aantalInt[i]);
			}	
		}
		catch (Exception p)
		{aantalOptieVeld[i].setText(""+aantalInt[i]);}
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
		if (h.containsKey("kijkNaActief"))
			kijkNaActief = ((Boolean) h.get("kijkNaActief")).booleanValue();
		if (h.containsKey("labelsKeuze"))
			labelsKeuze = ((Integer)h.get("labelsKeuze")).intValue();
		if(h.containsKey("kleur"))
			kleur = ((Boolean) h.get("kleur")).booleanValue();
		if (h.containsKey("kansVolgordeKeuze"))
			kansVolgordeKeuze = ((Integer)h.get("kansVolgordeKeuze")).intValue();
		if (h.containsKey("naamOptieTekst"))
			naamOptieTekst = ((String[]) h.get("naamOptieTekst"));
		for(int i = 1; i < 7; i++)
			zetNaamOptie(i, naamOptieTekst[i]);
		if (h.containsKey("letterString"))
			letterString = ((String[]) h.get("letterString"));
		for(int i = 1; i < 7; i++)
			zetLetterOptie(i, letterString[i]);
		if(h.containsKey("terugleggenKeuze"))
			terugleggenKeuze = ((Integer)h.get("terugleggenKeuze")).intValue();
		if(h.containsKey("trekkingen"))
			trekkingen = ((Integer)h.get("trekkingen")).intValue();
		if(h.containsKey("aantalOpties"))
			aantalOpties = ((Integer)h.get("aantalOpties")).intValue();
		if (h.containsKey("aantalInt"))
			aantalInt = ((int[]) h.get("aantalInt"));
		
		if(h.containsKey("scoreMax"))
			scoreMax = ((Integer)h.get("scoreMax")).intValue();
		if(h.containsKey("nakijkModel"))
			nakijkModel = (int[]) h.get("nakijkModel");
		if(h.containsKey("trekkingTekst"))
			trekkingTekst = ((String) h.get("trekkingTekst"));
		
		if(h.containsKey("kbipBreedte"))
			kbipBreedte = ((Integer)h.get("kbipBreedte")).intValue();
		if(h.containsKey("kbipHoogte"))
			kbipHoogte = ((Integer)h.get("kbipHoogte")).intValue();
		
		zetTeruglegZichtbaar(teruglegZichtbaar);
		zetTrekkingZichtbaar(trekkingZichtbaar);
		zetOptiesZichtbaar(optiesZichtbaar);
		zetBallenZichtbaar(ballenZichtbaar);
		zetLegendaZichtbaar(legendaZichtbaar);
		zetBovenbalkZichtbaar(bovenbalkZichtbaar);
		zetKijkNa(kijkNaActief);
		zetLabelsKeuze(labelsKeuze);
		zetKleur(kleur);
		zetKansVolgorde(kansVolgordeKeuze);
		zetTerugleggen(terugleggenKeuze);
		zetTrekkingen(trekkingen+1); 		
		zetOpties(aantalOpties, aantalInt);
		zetNakijkModel(nakijkModel);
		kansboom.trekkingTekst = trekkingTekst;
		setBounds(0, 0, kbipBreedte, kbipHoogte);
		zetBeginStatus();	
	}


	public void setState(Hashtable h) {
		if(h.containsKey("terugleggenKeuze"))
			terugleggenKeuze = ((Integer)h.get("terugleggenKeuze")).intValue();
		zetTerugleggen(terugleggenKeuze); 
		
		if(h.containsKey("trekkingen"))
			trekkingen = ((Integer)h.get("trekkingen")).intValue();
		zetTrekkingen(trekkingen+1); 
		
		if(h.containsKey("aantalOpties"))
			aantalOpties = ((Integer)h.get("aantalOpties")).intValue();
		
		if (h.containsKey("aantalInt"))
			aantalInt = ((int[]) h.get("aantalInt"));
		zetOpties(aantalOpties, aantalInt);
		
		if (h.containsKey("ingevuld")) 
			ingevuld = ((Boolean) h.get("ingevuld")).booleanValue();
	    if (h.containsKey("nagekeken")) 
	    	nagekeken = ((Boolean) h.get("nagekeken")).booleanValue();
		if (ingevuld && (mode == 0 || nagekeken)) 
	    	kijkNa();
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
			kijkNaActief = ((Boolean) h.get("kijkNaActief")).booleanValue();
		if (h.containsKey("labelsKeuze"))
			labelsKeuze = ((Integer)h.get("labelsKeuze")).intValue();
		if(h.containsKey("kleur"))
			kleur = ((Boolean) h.get("kleur")).booleanValue();
		if (h.containsKey("kansVolgordeKeuze"))
			kansVolgordeKeuze = ((Integer)h.get("kansVolgordeKeuze")).intValue();
		if (h.containsKey("naamOptieTekst"))
			naamOptieTekst = ((String[]) h.get("naamOptieTekst"));
		for(int i = 1; i < 7; i++)
			zetNaamOptie(i, naamOptieTekst[i]);
		if (h.containsKey("letterString"))
			letterString = ((String[]) h.get("letterString"));
		for(int i = 1; i < 7; i++)
			zetLetterOptie(i, letterString[i]);
		if(h.containsKey("terugleggenKeuze"))
			terugleggenKeuze = ((Integer)h.get("terugleggenKeuze")).intValue();
		if(h.containsKey("trekkingen"))
			trekkingen = ((Integer)h.get("trekkingen")).intValue();
		if(h.containsKey("aantalOpties"))
			aantalOpties = ((Integer)h.get("aantalOpties")).intValue();
		
		if (h.containsKey("aantalInt"))
			aantalInt = ((int[]) h.get("aantalInt"));
		
		if(h.containsKey("trekkingTekst"))
			trekkingTekst = ((String) h.get("trekkingTekst"));
	
		//nodig?
		if(h.containsKey("scoreMax"))
			scoreMax = ((Integer)h.get("scoreMax")).intValue();
		if(h.containsKey("nakijkModel"))
			nakijkModel = (int[]) h.get("nakijkModel");

		if(h.containsKey("kbipBreedte"))
			kbipBreedte = ((Integer)h.get("kbipBreedte")).intValue();
		if(h.containsKey("kbipHoogte"))
			kbipHoogte = ((Integer)h.get("kbipHoogte")).intValue();
		
		zetTeruglegZichtbaar(teruglegZichtbaar);
		zetTrekkingZichtbaar(trekkingZichtbaar);
		zetOptiesZichtbaar(optiesZichtbaar);
		zetBallenZichtbaar(ballenZichtbaar);
		zetLegendaZichtbaar(legendaZichtbaar);
		zetBovenbalkZichtbaar(bovenbalkZichtbaar);
		zetKijkNa(kijkNaActief);
		zetLabelsKeuze(labelsKeuze);
		zetKleur(kleur);
		zetKansVolgorde(kansVolgordeKeuze);
		zetTerugleggen(terugleggenKeuze);
		zetTrekkingen(trekkingen+1); 
		zetOpties(aantalOpties, aantalInt);
		zetNakijkModel(nakijkModel);
		kansboom.trekkingTekst = trekkingTekst;
		setBounds(0, 0, kbipBreedte, kbipHoogte);	
	}


	public Hashtable getState() {
		int terugleggenKeuze = 0;
		int trekkingen = 2;
		int aantalOpties = 2;
		int[] aantalInt = {4,4,4,4,4,4,4};
		
		terugleggenKeuze = this.terugleggenKeuze;
		trekkingen = this.trekkingen;
		aantalOpties = this.aantalOpties;
		aantalInt = this.aantalInt;
		
		Hashtable h = new Hashtable();
		
		h.put("terugleggenKeuze", terugleggenKeuze);
		h.put("trekkingen", trekkingen);
		h.put("aantalOpties", aantalOpties);
		h.put("aantalInt", aantalInt);
		h.put("ingevuld", new Boolean(ingevuld));
	    h.put("nagekeken", new Boolean(nagekeken));
		
		return h;
	}


	public Hashtable getEditState() {
		boolean teruglegZichtbaar = true;
		boolean trekkingZichtbaar = true;
		boolean optiesZichtbaar = true;
		boolean ballenZichtbaar = true;
		boolean legendaZichtbaar = true;
		boolean bovenbalkZichtbaar = true;
		boolean kijkNaActief = false;
		int labelsKeuze = 0;
		boolean kleur = true;
		int kansVolgordeKeuze = 0;
		String[] naamOptieTekst = null;
		String[] letterString = null;
		int terugleggenKeuze = 0;
		int trekkingen = 2;
		int aantalOpties = 2;
		int[] aantalInt = null;
		int scoreMax = 10;
		int[] nakijkModel = null;
		String trekkingTekst = "trekking";
				
		teruglegZichtbaar = this.teruglegZichtbaar;
		trekkingZichtbaar = this.trekkingZichtbaar;
		optiesZichtbaar = this.optiesZichtbaar;
		ballenZichtbaar = this.ballenZichtbaar;
		legendaZichtbaar = this.legendaZichtbaar;
		bovenbalkZichtbaar = this.bovenbalkZichtbaar;
		kijkNaActief = this.kijkNaActief;
		labelsKeuze = this.labelsKeuze;
		kleur = this.kleur;
		kansVolgordeKeuze = this.kansVolgordeKeuze;
		naamOptieTekst = this.naamOptieTekst;
		letterString = this.letterString;
		terugleggenKeuze = this.terugleggenKeuze;
		trekkingen = this.trekkingen;
		aantalOpties = this.aantalOpties;
		aantalInt = this.aantalInt;
		scoreMax = this.scoreMax;
		nakijkModel = this.nakijkModel;
		trekkingTekst = this.trekkingTekst;
				
		Hashtable h = new Hashtable();
		
		h.put("teruglegZichtbaar", teruglegZichtbaar);
		h.put("trekkingZichtbaar", trekkingZichtbaar);
		h.put("optiesZichtbaar", optiesZichtbaar);
		h.put("ballenZichtbaar", ballenZichtbaar);
		h.put("legendaZichtbaar", legendaZichtbaar);
		h.put("bovenbalkZichtbaar", bovenbalkZichtbaar);
		h.put("kijkNaActief", kijkNaActief);
		h.put("labelsKeuze", labelsKeuze);
		h.put("kleur", kleur);
		h.put("kansVolgordeKeuze", kansVolgordeKeuze);
		h.put("naamOptieTekst", naamOptieTekst);
		h.put("letterString", letterString);
		h.put("terugleggenKeuze", terugleggenKeuze);
		h.put("trekkingen", trekkingen);
		h.put("aantalOpties", aantalOpties);
		h.put("aantalInt", aantalInt);
		h.put("scoreMax", scoreMax);
		h.put("nakijkModel", nakijkModel);
		h.put("trekkingTekst", trekkingTekst);
		
		return h;
	}

	public InteractieEditPanel getEditPanel() {
		return new KansbomenInteractieEditPanel();
	}


	public void wis() {}


	public void zetMaat() {}


	public int geefAsHoogte() {
		return 0;
	}


	public int getIpId() {
		return 0;
	}


	public int getScore() 
	{
		return score;
	}


	public int getScoreMax() 
	{
		return scoreMax;
	}


	public boolean isCorrect() 
	{	if (!kijkNaActief)
			return true;
		return 
			score == scoreMax;
	}

	public boolean isFout() 
	{	if (!kijkNaActief)
			return false;
		return score == 0;
	}


	public void zetMode(int mode) {
		this.mode = mode;
		kijkNaButton.setVisible(mode == 0 || mode == 1);
		
	}


	public void zetNagekeken(boolean b) {
		if (ingevuld) 
			nagekeken = b;
		
	}


	public void stop() {
		kijkNa();
		
	}


	public void start() {
		
		}


	public void destroy() {
		
	}


	public void opnieuw() {
		
	}


	public void kijkNa() {
		// niet nakijken
    	if (!kijkNaActief)
    		return;
    	updateLeerlingAntwoorden();
    	if(Arrays.equals(leerlingAntwoorden,beginStatus))
    	{
    		ingevuld = false;
    		return;
    	}		                                   
    	
    	leerlingAntwoorden[0] = nakijkModel[0];
    	for(int i = nakijkModel[9] + 3; i < 7; i++)
    		leerlingAntwoorden[i] = nakijkModel[i];
    	if(Arrays.equals(leerlingAntwoorden,nakijkModel))
    		score = scoreMax;
    	else
    		score = 0;
    	
    	ingevuld = true;
    	 if (score == 0)
         {	kruisjeLabel.setVisible(true);
         	geelVinkjeLabel.setVisible(false);
         	groenVinkjeLabel.setVisible(false);
         }
         else if (score < scoreMax)
         {	kruisjeLabel.setVisible(false);
         	geelVinkjeLabel.setVisible(true);
         	groenVinkjeLabel.setVisible(false);
         }
    	 
         else // score==maxScore
         {	kruisjeLabel.setVisible(false);
         	geelVinkjeLabel.setVisible(false);
         	groenVinkjeLabel.setVisible(true);
         }
 //System.out.println("score = " + score);		
 		//fire actionEvent
 		ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "changed");
 		for (int lCnt = 0; lCnt < listeners.size(); lCnt++)
 		{
 			((ActionListener) listeners.elementAt(lCnt)).actionPerformed(event);
 		}
     }

	public void kijkNa(int stapNr) {
		kijkNa();
		
	}


	public void addActionListener(ActionListener al) {
		listeners.addElement(al);
	}


	public void focusGained(FocusEvent arg0) {
		
	}


}
