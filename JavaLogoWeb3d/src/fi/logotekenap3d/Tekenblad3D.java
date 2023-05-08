package fi.logotekenap3d;

import java.awt.*;
import java.awt.event.*;

import javax.swing.JPanel;
import fi.logotekenap3d.Punt3D;

public class Tekenblad3D extends JPanel //Canvas
{
	private int breedte,hoogte;
	private Punt3D beginpunt,eindpunt,startpunt;
  	public Lichaam3D l;
  	public Image im ;
  	private Graphics gIm ;
	public Matrix3D mat;  
	private TekenApplet3D eigenaar;
	private boolean pen, vul;
  	private Color penkleur,vulkleur,achtergrondkleur;
	public boolean bezigMetTekenen;
	
//	public static int consoleStartX = 10;
//	public static int consoleStartY = 16;
//	private int consoleX = consoleStartX;
//	private int consoleY = consoleStartY;
	
	Color achterkantKleur = new Color(192,192,192);
	
	double hoekX, hoekY,beginx,beginy; 
	
	boolean cursorAan = false;
	boolean transparant = false;
	int transparantAlpha = 125;
	boolean wireFrame = false; 
	
	double zoomFactor = 1;

	public Tekenblad3D(TekenApplet3D ap)
	{	
		//setLayout(null);
		achtergrondkleur = Color.white;
		l = new Lichaam3D();
		eigenaar = ap;
		mat = new Matrix3D();
		
	}
	
	public Graphics getgIm()
	{
		return gIm;
	}
	//-------------------------------------------------------------------------------------------
	//deze methoden worden gebruikt door het Tekenblad: om de image te initialiseren en
	//op het scherm te zetten. "paint()" wordt alleen bij de eerste keer tekenen gebruikt, daarna 
	//zorgt "tekenOpnieuw()" of "tekenErbij()" hiervoor. "TekenOpImage()" zorgt voor het vullen 
	//van de image, metbehulp van het door de leerlingen geimplementeerde "tekenprogramma()",
	//en wordt zowel door "paint()" als door "tekenOpImage()" gebruikt
	//-------------------------------------------------------------------------------------------  	
	public void paintComponent(Graphics g)
  	{ 	
		
System.out.println("tb paintComponent");

		gIm = g;
		
//if (gIm == null)
//System.out.println("paint gIm == null");	
		

		bezigMetTekenen = true;
//		if (im == null)
//		{
			
//System.out.println("paint im == null");

			breedte = getSize().width;
			hoogte = getSize().height;	
			double startschaal = Math.min((double)breedte/500,(double)hoogte/500);
			startschaal *= zoomFactor;
			mat.initialiseer(0,0,0,startschaal);	
			startpunt = new Punt3D(breedte/2,hoogte/2,0);
			l.maakNulpunt(breedte/2,hoogte/2,0);
//			im = createImage(breedte,hoogte);
//if (im == null)
//System.out.println("paint gIm == null");	
// 			gIm = im.getGraphics();
  			//initializeDrawing(eigenaar.eigenaar.trb.isTraceAan());
			tekenOpImage(eigenaar.eigenaar.trb.isTraceAan());
//		}
//    	g.drawImage(im, 0, 0, null);
		bezigMetTekenen = false;
  	}
	  
 	/**
 	 * Initializes a drawing. To be called before starting the execution of a 'tekenalgoritme'.
 	 */
 	public void initializeDrawing(boolean cursor)
  	{ 	
 		
System.out.println("tb initializeDrawing");

  		// wat is de reden van deze voorwaarde???
  		if (startpunt == null) 
  			return;
  		beginpunt = new Punt3D(startpunt);
    	eindpunt = new Punt3D(beginpunt);
    	mat.initialiseer();
    	achtergrondkleur = Color.WHITE;
	  	gIm.setColor(achtergrondkleur);
    	gIm.fillRect(0, 0, breedte, hoogte);
    	gIm.setColor(Color.gray);
    	gIm.drawRect(0, 0, breedte-1, hoogte-1);
//    	consoleX = consoleStartX;
//    	consoleY = consoleStartY;
    	//penAan(0,0,0);
    	//pen = true;
    	pen = false;
    	penkleur = Color.black;
    	//gIm.setColor(penkleur);
    	//penAan();
		vul = false;
    	vulkleur = Color.black;
    	if (cursor)
    	{	eigenaar.eigenaar.trb.traceProgram();
    		tekenCursor();
    	}
    	else
    		eigenaar.eigenaar.trb.executeProgram();
    	
    		
	}
  	
	void tekenCursor()
	{	

		Punt3D[] cursorPunten = new Punt3D[4];
		Punt3D p = mat.geefVolgendPunt(beginpunt,25,0,0);
		cursorPunten[0] = p;
		p = mat.geefVolgendPunt(beginpunt,0,10,0);
		cursorPunten[1] = p;
		p = mat.geefVolgendPunt(beginpunt,-10,0,0);
		cursorPunten[2] = p;
		p = mat.geefVolgendPunt(beginpunt,0,-15,0);
		cursorPunten[3] = p;

		l.voegCursorToe(cursorPunten, Color.black, Color.yellow);
	}

	/**
	 * Outputs a finished drawing to the display, with or without the cursor (tracing)
	 * 
	 * @param cursor true when tracing, draw cursor on top of drawing.
	 */
	public void paintDrawing(boolean cursor)
	{	
//System.out.println("tb paintDrawing " + cursor);

		cursorAan = cursor;
		//initializeDrawing(cursor);
		//gIm = getGraphics();
		
//if (gIm == null)
//System.out.println("gIm == null");	
		
		//tekenOpImage(cursor);
		
		tekenOpnieuw();
		
//		Graphics g = getGraphics();
//		if(g!=null)
//			g.drawImage(im, 0, 0, null);
	}

  	public void tekenOpImage(boolean cursor)
  	{ 	
  		
System.out.println("tb tekenOpImage " + cursor);

		startpunt = new Punt3D(breedte/2,hoogte/2,0);

  		beginpunt = new Punt3D(startpunt);
    	eindpunt = new Punt3D(beginpunt);
		mat.initialiseer();

if (gIm == null)
{System.out.println("toi gIm == null");
return;
}
		
	  	gIm.setColor(achtergrondkleur);
		gIm.fillRect(0, 0, breedte, hoogte);
    	gIm.setColor(Color.gray);
    	gIm.drawRect(0, 0, breedte-1, hoogte-1);
    	//penAan(0,0,0);
    	pen = false;
		vul = false;
		mat.xdraai(beginx+hoekX); 
		mat.ydraai(beginy+hoekY);
    	if (cursor)
    	{	eigenaar.eigenaar.trb.traceProgram();
    		tekenCursor();
    	}
    	else
    		eigenaar.eigenaar.trb.executeProgram();
    	//eigenaar.tekenprogramma();
		l.sorteer();

//System.out.println("polyg = " + l.aantalPolygonen);

		for (int i = 0; i < l.aantalPolygonen; i++)
		{
			if (l.vlakken[i].normaal.z > 0)
			{	
//System.out.println("nz > 0");				
				
				double grijsfactor = 0.5*((-l.vlakken[i].normaal.x - l.vlakken[i].normaal.y + l.vlakken[i].normaal.z)/Math.sqrt(3)+1);
				if (grijsfactor < 0) 
					grijsfactor = 0;
				if (grijsfactor > 1)
					grijsfactor = 1;
				if (l.vlakken[i].vulkleur == null)
				{	l.vlakken[i].vulkleur = Color.magenta;
//System.out.println("polyg = " + i + " vk = null");				
				}
				int roodwaarde = 50 + (int) (l.vlakken[i].vulkleur.getRed() * grijsfactor * 0.75);
				int groenwaarde = 50 + (int) (l.vlakken[i].vulkleur.getGreen() * grijsfactor * 0.75);
				int blauwwaarde = 50 + (int) (l.vlakken[i].vulkleur.getBlue() * grijsfactor *0.75);
				int alpha = 255;
				if (transparant && !l.vlakken[i].naam.equals("cursor"))
					alpha = transparantAlpha;
				if (l.vlakken[i].naam.equals("cursor"))
					gIm.setColor(l.vlakken[i].vulkleur);
				else			
					gIm.setColor(new Color(roodwaarde,groenwaarde,blauwwaarde,alpha));
				if (!wireFrame || l.vlakken[i].naam.equals("cursor"))
					gIm.fillPolygon(l.vlakken[i].pol);
				
				gIm.setColor(l.vlakken[i].lijnkleur);
				if (!l.vlakken[i].isLijn && (l.vlakken[i].isOmlijnd || wireFrame))
				{	gIm.setColor(l.vlakken[i].lijnkleur);
					gIm.drawPolygon(l.vlakken[i].pol);
				}
				if (l.vlakken[i].isLijn)
				{	gIm.setColor(l.vlakken[i].lijnkleur);
					gIm.drawPolygon(l.vlakken[i].pol);
					penkleur = new Color(0,0,0);
				}
			}
// ook de achterkant vullen?			
			else // toegevoegd Huub, achterkant wel tekenen in achterkantKleur 
			{
				
//System.out.println("nz < 0");

				double grijsfactor = 0.5*((-l.vlakken[i].normaal.x - l.vlakken[i].normaal.y + l.vlakken[i].normaal.z)/Math.sqrt(3)+1);
				if (grijsfactor < 0) 
					grijsfactor = 0;
				if (grijsfactor > 1)
					grijsfactor = 1;
				int roodwaarde = 50 + (int) (achterkantKleur.getRed() * grijsfactor * 0.75);
				int groenwaarde = 50 + (int) (achterkantKleur.getGreen() * grijsfactor * 0.75);
				int blauwwaarde = 50 + (int) (achterkantKleur.getBlue() * grijsfactor *0.75);
				int alpha = 255;
				if (transparant && !l.vlakken[i].naam.equals("cursor"))
					alpha = transparantAlpha;
				if (l.vlakken[i].naam.equals("cursor"))
					gIm.setColor(l.vlakken[i].vulkleur);
				else			
					gIm.setColor(new Color(roodwaarde,groenwaarde,blauwwaarde,alpha));
				if (!wireFrame || l.vlakken[i].naam.equals("cursor"))
					gIm.fillPolygon(l.vlakken[i].pol);
				
				
				gIm.setColor(l.vlakken[i].lijnkleur);
				if (!l.vlakken[i].isLijn && (l.vlakken[i].isOmlijnd || wireFrame))
				{	gIm.setColor(l.vlakken[i].lijnkleur);
					gIm.drawPolygon(l.vlakken[i].pol);
				}
				
				if (l.vlakken[i].isLijn)
				{	gIm.setColor(l.vlakken[i].lijnkleur);
					gIm.drawPolygon(l.vlakken[i].pol);
					penkleur = new Color(0,0,0);
				}

				
			}
		}
		l = new Lichaam3D();			
		l.maakNulpunt(breedte/2,hoogte/2,0);
	}
	
	//-------------------------------------------------------------------------------------------
	//deze methoden worden gebruikt door handlers van het leerlingprogramma
	//-------------------------------------------------------------------------------------------
	void tekenOpnieuw()
	{	
		repaint();
		
//if (im == null)
//{	
//System.out.println("tekenOpnieuw im == null");	
//	return;
//}
//System.out.println("tekenOpnieuw " + cursorAan);
/*		
		bezigMetTekenen = true;
		tekenOpImage(cursorAan);
		Graphics g = getGraphics();
		g.drawImage(im, 0, 0, null); 
		bezigMetTekenen = false;
*/		
	}
  
  	void tekenErbij()
	{
  		
  		repaint();
//if (im == null)
//	return;

//System.out.println("tekenErbij " + cursorAan);
/*  		
  		bezigMetTekenen = true;
		tekenOpImage(cursorAan);
		Graphics g = getGraphics();
		g.drawImage(im, 0, 0, null);
		bezigMetTekenen = false;
*/		
	}
  	
	public Punt3D geefBeginpunt()
	{	return new Punt3D(beginpunt);
	}

	public void zetCursorAan(boolean b)
	{	cursorAan = b;
		tekenOpnieuw();
	}

	public void zetTransparant(boolean b)
	{	transparant = b;
		tekenOpnieuw();
	}

	public void zetWireFrame(boolean b)
	{	wireFrame = b;
		tekenOpnieuw();
	}

	public void zoomIn()
	{
//System.out.println("tb zoomIn");		
		//mat.zetStartschaal(mat.geefStartschaal()*(11e-1d));
		zoomFactor *= 11e-1d;
		tekenOpnieuw();
	}
	
	public void zoomUit()
	{
		//mat.zetStartschaal(mat.geefStartschaal()*(9e-1d));
		zoomFactor *= 91e-2d;
		tekenOpnieuw();
		
	}

	public void zoom(double fac)
	{
//System.out.println("tb zoomIn");		
		//mat.zetStartschaal(mat.geefStartschaal()*fac);
		zoomFactor *= fac;
		tekenOpnieuw();
	}
	

	//-------------------------------------------------------------------------------------------
	//deze methoden worden gebruikt door het Tekenblad om de lijnen en vlakken te tekenen
	//-------------------------------------------------------------------------------------------

	void naarVolgendPunt(double dx,double dy, double dz)
	{	eindpunt = mat.geefVolgendPunt(beginpunt, dx, dy, dz);
		
		if (pen && !vul)
		{	l.voegPuntToe(beginpunt);
			l.voegPuntToe(eindpunt);
			l.voegPolygonToe(penkleur,penkleur,true);
		}
		if (pen && vul)
		{
//System.out.println("voegLijnToe");
			l.voegLijnToe(beginpunt, eindpunt, penkleur, vulkleur);
		}
		if (vul)		 
		{	l.voegPuntToe(beginpunt);
		}
		beginpunt.x = eindpunt.x;
		beginpunt.y = eindpunt.y;
		beginpunt.z = eindpunt.z;
		
	}
	
	void tekenPolygon()
	{	l.voegPolygonToe(vulkleur, penkleur, pen);
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
	void penAan(int r, int g, int b)
	{	pen = true;
		penkleur = new Color(r,g,b);
		gIm.setColor(penkleur);
	}
	void penUit()
	{	pen = false;
	}
	void vulAan()
	{	vul = true;
	}
	void vulAan(String kl)
	{	vul = true;	
		vulkleur = maakKleur(kl);
	}
	void vulAan(int r, int g, int b)
	{	vul = true;	
		vulkleur = new Color(r,g,b);
	}
	void vulUit()
	{	tekenPolygon();
		vul = false;
	}
	void achtergrondkleur(String kl)
	{	achtergrondkleur = maakKleur(kl);
	}
	void achtergrondkleur(int r, int g, int b)
	{	achtergrondkleur = new Color(r,g,b);
	}
	Polygon geefVlak()
	{	if (l.vlakken[l.aantalPolygonen-1].normaal.z > 0)
			return l.vlakken[l.aantalPolygonen-1].pol;
		else 
			return new Polygon();
	}
	
 	//-------------------------------------------------------------------------------------------
	//deze methode wordt gebruikt een kleur in de vorm van een string om te zetten in een Color
	//-------------------------------------------------------------------------------------------
// hoe alle bestaande kleuren tranparant te maken en terug?	
	private Color maakKleur(String kl)
	{	if (kl.equals("rood")) 
			return Color.red;
		else if (kl.equals("transrood")) 
			return new Color(255,0,0,150);
		else if (kl.equals("groen")) 
			return Color.green;
		else if (kl.equals("blauw")) 
			return Color.blue;
		else if (kl.equals("geel")) 
			return Color.yellow;
		else if (kl.equals("cyaan")) 
			return Color.cyan;
		else if (kl.equals("roze")) 
			return Color.pink;
		else if (kl.equals("zwart")) 
			return Color.black;
		else if (kl.equals("grijs")) 
			return Color.gray;
		else if (kl.equals("lichtgrijs")) 
			return Color.lightGray;
		else if (kl.equals("magenta")) 
			return Color.magenta;
		else if (kl.equals("wit")) 
			return Color.white;
		else if (kl.equals("oranje")) 
			return Color.orange;
		else 
			return Color.black;		
	}	
	
	public double geefDraaiX()
	{
		return beginx+hoekX;
	}

	public void zetBeginHoeken(double hx, double hy)
	{
		beginx = hx;
		beginy = hy;
		hoekX = 0;
		hoekY = 0;
	}

	public double geefDraaiY()
	{
		return beginy+hoekY;
	}


	public void muisSleepActie()
	{	if (eigenaar.geefMuisBeheerder() != null)
		{	hoekX = hoekX - eigenaar.geefSleepdy();
			hoekY = hoekY + eigenaar.geefSleepdx();
		}	
		tekenOpnieuw();
	}
}
