package fi.mozarch;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

import javax.swing.JPanel;

class Tekenblad2 extends JPanel
{
	private int breedte,hoogte;
	private Punt beginpunt,eindpunt,startpunt;
  	private Polygon veelvlak;
  	private Graphics gIm ;
	public Matrix2D mat;  
	private TekenPanel eigenaar;
	private boolean pen, vul;
  	private Color penkleur, vulkleur, achtergrondkleur;
	  
	public Tekenblad2(TekenPanel ap)
	{	
		//setLayout(null);
		
		achtergrondkleur = Color.white;
		veelvlak = new Polygon();
		eigenaar = ap;
		mat = new Matrix2D();					// zorgt voor de tekenrichting
	}
	
	public void paintComponent(Graphics g)
	{	breedte = getSize().width;
		hoogte = getSize().height;
		startpunt = new Punt(breedte / 2, hoogte / 2);

		gIm = g;
		tekenOpImage(g);
	}
  	
	public void tekenOpImage(Graphics g)
  	{ 	beginpunt = new Punt(startpunt);
    	eindpunt = new Punt(beginpunt);
    	mat.initialiseer();
	  	
    	if (achtergrondkleur != null)
    		g.setColor(achtergrondkleur);
    	g.fillRect(0, 0, breedte, hoogte);

	  	penAan(0, 0, 0);
		vul = false;
    	vulkleur = Color.black;
    	eigenaar.tekenprogramma();
    	
	}
	public void zetStart()
	{	beginpunt = new Punt(startpunt);
    	eindpunt = new Punt(beginpunt);
	}
	public void zetStartPunt(int px, int py)
	{	startpunt = new Punt(px, py);
	}

	//-------------------------------------------------------------------------------------------
	//deze methoden worden gebruikt door handlers van het leerlingprogramma
	//-------------------------------------------------------------------------------------------
	
 	void tekenOpnieuw()
	{	
 		repaint();
	}

 	//-------------------------------------------------------------------------------------------
	//deze methoden worden gebruikt door het Tekenblad om de lijnen en vlakken te tekenen
	//-------------------------------------------------------------------------------------------
	void naarVolgendPunt(double dx, double dy)
	{	eindpunt = mat.geefVolgendPunt(beginpunt, dx, dy);
		if (pen)
			gIm.drawLine((int) beginpunt.x,(int) beginpunt.y,(int) eindpunt.x,(int) eindpunt.y);
		if (vul)
			veelvlak.addPoint((int) beginpunt.x,(int) beginpunt.y);
		beginpunt.x = eindpunt.x;
		beginpunt.y = eindpunt.y;
	}
	
	void tekenPolygon()
	{	gIm.setColor(vulkleur);
		gIm.fillPolygon(veelvlak);
		gIm.setColor(penkleur);
		if (pen)
			gIm.drawPolygon(veelvlak);
	}
	
 	//-------------------------------------------------------------------------------------------
	//deze methoden worden gebruikt in "tekenprogramma()" 
	//-------------------------------------------------------------------------------------------
	void penAan()
	{	pen = true;
	}
	void penAan(String kl)
	{	pen = true;
		penkleur = maakKleur(kl);
		gIm.setColor(penkleur);
	}
	void penAan(Color kl)
	{	pen = true;
		penkleur = kl;
		gIm.setColor(penkleur);
	}
	void penAan(int r, int g, int b)
	{	pen = true;
		penkleur = new Color(r, g, b);
		gIm.setColor(penkleur);
	}
	void penUit()
	{	pen = false;
	}
	
	void zetVul(boolean b)
	{	vul = b;
	}
	void vulAan()
	{	vul = true;
		veelvlak = new Polygon();
	}
	void vulAan(String kl)
	{	vul = true;	
		vulkleur = maakKleur(kl);
		veelvlak = new Polygon();
	}
	void vulAan(Color kl)
	{	vul = true;	
		vulkleur = kl;
		veelvlak = new Polygon();
	}
	void vulAan(int r, int g, int b)
	{	vul = true;	
		vulkleur = new Color(r, g, b);
		veelvlak = new Polygon();
	}
	void vulUit()
	{	tekenPolygon();
		vul = false;
	}
	void achtergrondkleur(String kl)
	{	achtergrondkleur = maakKleur(kl);
	}
	void achtergrondkleur(int r, int g, int b)
	{	achtergrondkleur = new Color(r, g, b);
	}
	Polygon geefVlak()							// geeft de laatst getekende Polygon
	{	return veelvlak;
	}
	Punt geefPunt()								// geeft de laatst getekende Punt
	{	return beginpunt;
	}
	void schrijf(String s)
	{	gIm.drawString(s, (int) beginpunt.x, (int) beginpunt.y);
	}
 	//-------------------------------------------------------------------------------------------
	//deze methode wordt gebruikt een kleur in de vorm van een string om te zetten in een Color
	//-------------------------------------------------------------------------------------------
	private Color maakKleur(String kl)
	{	if(kl.equals("rood")) return Color.red;
		else if(kl.equals("groen")) return Color.green;
		else if(kl.equals("blauw")) return Color.blue;
		else if(kl.equals("geel")) return Color.yellow;
		else if(kl.equals("cyaan")) return Color.cyan;
		else if(kl.equals("roze")) return Color.pink;
		else if(kl.equals("zwart")) return Color.black;
		else if(kl.equals("grijs")) return Color.gray;
		else if(kl.equals("lichtgrijs")) return Color.lightGray;
		else if(kl.equals("magenta")) return Color.magenta;
		else if(kl.equals("wit")) return Color.white;
		else if(kl.equals("oranje")) return Color.orange;
		else return Color.black;		
	}	
}
