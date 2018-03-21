package fi.wiskopdr.symbolen;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JLabel;

public class Symbool extends JLabel {

	public static int GEEN = 0;
	public static int LIJN = 1;
	public static int PIJL = 2;
	public static int ACCOLADE = 3;
	public static int ELLIPS = 4;
	public static int HAAK = 5;
	
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
		repaint();
	}
	
	public void zetKleur(Color c)
	{
		kleur = c;
		repaint();
	}
	
	public int geefType()
	{
		return Symbool.GEEN;
	}
	
	public Graphics2D createGraphics2D(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(kleur);
		g2.setStroke(new BasicStroke(dikte));
		return g2;
	}
	
}
