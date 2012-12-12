package fi.kansbomen;

import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;

import javax.swing.*;

public class NakijkModel extends JDialog implements ActionListener, FocusListener
{
	int editWidth = 170;
	//int editHeight = 450;
	
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
	JLabel aantalTrekkingen, aantalOptiesLabel;
	JLabel[] aantalOptie;
	JTextField[] aantalOptieVeld;
	String[] naamOptieTekst;
	
	boolean terugleggen = true;
	int terugleggenKeuze = 0;
	int trekkingen = 2;
	int aantalOpties = 2;
	int[] aantalInt = new int[]  {4,4,4,4,4};
	int[] aantalIntOud = new int[] {4,4,4,4,4};
	int breedteAantalVeld;
	
	public NakijkModel()
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
	       
//	for(int i=1; i<5; i++)
//		letterString[i] = naamOptieTekst[i].substring(0,1).toLowerCase();
	                            
	aantalOptie = new JLabel[5];
	for (int i=1; i<5; i++)
	{	aantalOptie[i] = new JLabel(Kansbomen.rb.getString("aantalTekst")+naamOptieTekst[i]+":");
		aantalOptie[i].setFont(theFont);
		aantalOptie[i].setBounds(currentX, currentY, width - breedteAantalVeld, height);
		currentY += height + offset;
	}
	plaatsOptieRegels(aantalOpties+2);
	}

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
	
	public void zetNakijkModel(Hashtable h)
	{
		
	}
	
	public Hashtable getCheckState()
	{	int terugleggenKeuze = 0;
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
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == terugleggenBox)
		{	terugleggenKeuze = terugleggenBox.getSelectedIndex();
		}
		else if(e.getSource() == trekkingenBox)
		{	trekkingen = trekkingenBox.getSelectedIndex();
		}
		else if(e.getSource() == optiesBox)
		{
			aantalOpties = optiesBox.getSelectedIndex();	
		}	
	
		
		else if(e.getSource() == aantalOptieVeld[1])
		{	 try
			{ 	aantalIntOud[1] = aantalInt[1];
				aantalInt[1] = Integer.parseInt( aantalOptieVeld[1].getText() );
				if(aantalInt[1] <1)
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
				if(aantalInt[2] <1)
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
				if(aantalInt[3] <1)
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
				if(aantalInt[4] <1)
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
				if(aantalInt[1] <1)
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
				if(aantalInt[2] <1)
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
				if(aantalInt[3] <1)
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
				if(aantalInt[4] <1)
				{	aantalInt[4] = aantalIntOud[4];
					aantalOptieVeld[4].setText(""+aantalInt[4]);
				}
			}
			catch (Exception p)
			{aantalOptieVeld[4].setText(""+aantalInt[4]);}
		}
		
	}		
			

	public void focusGained(FocusEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	
}
