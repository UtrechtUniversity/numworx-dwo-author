package fi.kansbomen;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

public class KansbomenInteractiePanel extends JPanel //implements ActionListener

{
Kansboom kansboom; 
/*Wat heb ik nodig in mijn interactiepanel? 
Eventueel:
- Aanvinkvakje met/zonder terugleggen
- Keuzevakje aantal keuzes
- Keuzevakje aantal keuzemomenten
	
*/
int marge=5;
int keuzeMomenten;
int aantalKeuzes;



	public KansbomenInteractiePanel()
	{
		//terugleggenBox = new JCheckBox();
		//ik wil mijn kansboom natuurlijk pas toevoegen als ik alle instellingen uit het edit-panel heb verwerkt.
		//en ik wil dat mijn kansboom zich aanpast als ik hier instellingen verander. 
		setLayout(null);
	
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
		kansboom.setSize(b-2*marge, h-2*marge);
		kansboom.setLocation(marge,marge);
		add(kansboom);
}
else
{
		kansboom.setSize(b-2*marge, h-2*marge);
}
	}

	public void zetKleur(boolean b)
	{
		kansboom.zetKleur(b);
	}

	public void zetTerugleggen(boolean b)
	{
		kansboom.zetTerugleggen(b);
	}

	public void zetKeuzeMomenten(int i)
	{
		kansboom.zetKeuzeMomenten(i);
	}
	
	public void zetAantalOpties(int i)
	{
		kansboom.zetAantalOpties(i);
	}
	
	public void zetAantalVanOptie(int i, int j)
	{
		kansboom.zetAantalVanOptie(i,j);
	}
}
