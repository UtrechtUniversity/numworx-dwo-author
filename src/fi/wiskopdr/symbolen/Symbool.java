package fi.wiskopdr.symbolen;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JLabel;

public class Symbool extends JLabel {

	public static int GEEN = 0;
	public static int LIJN = 1;
	public static int PIJL = 2;
	public static int ACCOLADE = 3;
	public static int ELLIPS = 4;
	
	public static int RICHTING_LINKS = 0;
	public static int RICHTING_RECHTS = 1;
	public static int RICHTING_BOVEN = 2;
	public static int RICHTING_BENEDEN = 3;
	public static int RICHTING_RECHTSBOVEN = 4;
	public static int RICHTING_RECHTSONDER = 5;
	public static int RICHTING_LINKSONDER = 6;
	public static int RICHTING_LINKSBOVEN = 7;
	
	
	Color kleur = Color.BLACK;
	int dikte = 1;
	int richting;
	
	public Symbool(int richting)
	{	this.setOpaque(false);
		this.richting = richting;
	}
	
	public void zetRichting(int richting)
	{
		this.richting = richting;
	}
	
	
	public void zetDikte(int d)
	{
		dikte = d;
	}
	
	public void zetKleur(Color c)
	{
		kleur = c;
	}
	
	public int geefType()
	{
		return Symbool.GEEN;
	}
	
	
}
