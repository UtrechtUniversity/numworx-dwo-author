package fi.mozarch;


import java.awt.*;
import java.awt.event.*;

import javax.swing.JPanel;

import fi.beans.mainframe.JApplet;


public class TekenApplet extends JApplet 
{
	Tekenblad tb;
	private MuisBeheerder mb;
	private boolean initializing;
	
	//-----------------------------------------------------------------------------------------
	// initalisatie
	//-----------------------------------------------------------------------------------------
	public void init()
	{	
		tb = new Tekenblad(this);
	
		getContentPane().setLayout(null);
		
		initializing = true;
		initialiseer();							// wordt geimplementeerd in leerlingprogramma
		initializing = false;
		
		
		tb.setBounds(0, 0, getSize().width, getSize().height);
		
		getContentPane().add(tb);
		
	}												
	//-------------------------------------------------------------------------------------------
	//deze methoden kunnen alleen worden gebruikt in  "initialiseer()" van leerling-applet
	//-------------------------------------------------------------------------------------------
	public void maakMuisActieMogelijk()
	{	if (initializing) 
		{	mb = new MuisBeheerder(this);
			tb.addMouseListener(mb);
			tb.addMouseMotionListener(mb);
		}
	}
	//-------------------------------------------------------------------------------------------
	//deze methoden worden gebruikt in de muishandler en  doorgegeven aan MuisBeheerder mb
	//-------------------------------------------------------------------------------------------
	public int geefSleepdx()
	{	return mb.geefSleepdx();
	}
	public int geefSleepdy()
	{	return mb.geefSleepdy();
	}
	public int geefDrukx()
	{	return mb.geefDrukx();
	}
	public int geefDruky()
	{	return mb.geefDruky();
	}

	// het hele applet!!
	public void tekenOpnieuw()
	{	repaint();
	}

	//-------------------------------------------------------------------------------------------
	//deze methoden worden gebruikt in "initialiseer" en doorgegeven aan Tekenblad tb (of 
	//Matrix2d) 
	//-------------------------------------------------------------------------------------------
	public void schaal(double s)
	{	tb.mat.schaal(s);
	}
	public void achtergrondkleur(String kl)
	{	tb.achtergrondkleur(kl);
	}
	public void achtergrondkleur(int r, int g, int b)
	{	if (tb != null)
			tb.achtergrondkleur(r, g, b);
	}
	//-------------------------------------------------------------------------------------------
	//deze methoden worden gebruikt in "tekenprogramma()" en doorgegeven aan Tekenblad tb (of 
	//Matrix2d) 
	//-------------------------------------------------------------------------------------------
 	public void links(double dHoek)
	{	tb.mat.draai(dHoek);
	}
  	public void rechts(double dHoek)
	{	tb.mat.draai(-dHoek);		
	}
	public void vooruit(double dy)
	{	tb.naarVolgendPunt(0, -dy);	
	}
	public void stapy(double dy)
	{	tb.naarVolgendPunt(0, -dy);		
		//if (trb != null && trb.geefTraceStatus())
			//trb.volgendeMethode("stapy(" + Integer.toString((int)Math.rint(dy)) + ")");
	}
	public void stapx(double dx)
	{	tb.naarVolgendPunt(dx,0);		
		//if(trb!=null && trb.geefTraceStatus())
		//trb.volgendeMethode("stapx("+Integer.toString((int)Math.rint(dx))+")");
	}
	public void stap(double dx, double dy)
	{	tb.naarVolgendPunt(dx, -dy);
		//if (trb != null && trb.geefTraceStatus())
			//trb.volgendeMethode("stap(" + Integer.toString((int)Math.rint(dx)) + "," + Integer.toString((int)Math.rint(dy)) + ")");
	}
	public void penAan()
	{	tb.penAan();							
		//if(trb!=null && trb.geefTraceStatus())
		//trb.volgendeMethode("penAan()");
	}
	public void penAan(String kl)
	{	tb.penAan(kl);				
		//if(trb!=null && trb.geefTraceStatus())
		//trb.volgendeMethode("penAan("+kl+")");
	}
	public void penAan(Color kl)
	{	tb.penAan(kl);				
		//if(trb!=null && trb.geefTraceStatus())
		//trb.volgendeMethode("penAan()");
	}
	public void penAan(int r, int g, int b)
	{	tb.penAan(r, g, b);	
		//if(trb!=null && trb.geefTraceStatus())
		//trb.volgendeMethode("penAan("+Integer.toString(r)+Integer.toString(g)+Integer.toString(b)+")");
	}
	public void penUit()
	{	tb.penUit();							
		//if(trb!=null && trb.geefTraceStatus())
		//trb.volgendeMethode("penUit()");
	}
	public void vulAan()
	{	tb.vulAan();							
		//if(trb!=null && trb.geefTraceStatus())
		//trb.volgendeMethode("vulAan()");
	}
	public void vulAan(String kl)
	{	tb.vulAan(kl);				
		//if(trb!=null && trb.geefTraceStatus())
		//trb.volgendeMethode("vulAan("+kl+")");
	}
	public void vulAan(Color kl)
	{	tb.vulAan(kl);				
		//if(trb!=null && trb.geefTraceStatus())
		//trb.volgendeMethode("vulAan()");
	}
	public void vulAan(int r, int g, int b)
	{	tb.vulAan(r, g, b);	
		//if (trb != null && trb.geefTraceStatus())
		//trb.volgendeMethode("vulAan(" + Integer.toString(r) + Integer.toString(g) + Integer.toString(b) + ")");
	}
	public void vulUit()
	{	tb.vulUit();							
		//if (trb != null && trb.geefTraceStatus())
		//trb.volgendeMethode("vulUit()");
	}
	public Polygon geefVlak()
	{	return tb.geefVlak();
	}
	public Punt geefPunt()
	{	return tb.geefPunt();
	}
	//-------------------------------------------------------------------------------------------
	//deze methoden worden geimplemeteerd in het leerlingenprogramma
	//-------------------------------------------------------------------------------------------
	public void tekenprogramma()
	{}
	public void initialiseer()
	{}
	public void muisSleepActie()
	{}
	public void muisDrukActie()
	{}
	public void muisLosActie()
	{}
	//public void invoerVarActie(InvoerVariabele iv){}
}		

class Tekenblad extends JPanel
{
	private int breedte,hoogte;
	private Punt beginpunt,eindpunt,startpunt;
  	private Polygon veelvlak;
  	private Graphics gIm ;
	public Matrix2D mat;  
	private TekenApplet eigenaar;
	private boolean pen, vul;
  	private Color penkleur, vulkleur, achtergrondkleur;
	  
	public Tekenblad(TekenApplet ap)
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


class MuisBeheerder implements MouseListener, MouseMotionListener
{
	private int eerstex, laatstex, eerstey, laatstey, dx, dy;
	private TekenApplet eigenaar;
	private boolean actief;
	
	public MuisBeheerder(TekenApplet ap)
	{	eigenaar = ap;
		actief = true;
		eerstex = 0;
		eerstey = 0;
		laatstex = 0;
		laatstey = 0;
		dx = 0;
		dy = 0;
	}
	//-------------------------------------------------------------------------------------------
	//afhandeling van de muis gebeurtenissen 
	//-------------------------------------------------------------------------------------------
	public void mousePressed(MouseEvent e)
	{	if (actief)
		{	
			eerstex = e.getX();
			eerstey = e.getY();
			laatstex = e.getX();
			laatstey = e.getY();
			eigenaar.muisDrukActie();
		}
	}
	
	public void mouseDragged(MouseEvent e)
	{	if (actief)
		{	int x = e.getX();
			int y = e.getY();
			dx = x - laatstex;
			dy = laatstey - y;
			eigenaar.muisSleepActie();
			laatstex = x;
			laatstey = y;	
		}
	}
	public void mouseReleased(MouseEvent e)
	{	
		eigenaar.muisLosActie();
	}
	public void mouseExited(MouseEvent e){;}
	public void mouseClicked(MouseEvent e){;}
	public void mouseEntered(MouseEvent e){;}
	public void mouseMoved(MouseEvent e){;}
	
	//-------------------------------------------------------------------------------------------
	//deze methode wordt gebruikt door de TraceBeheerder
	//-------------------------------------------------------------------------------------------
	public void setEnableMuisActie(boolean b)
	{	actief = b;
	}
	//-------------------------------------------------------------------------------------------
	//deze methoden worden gebruikt door de muishandlers in het leerlingenprogramma
	//-------------------------------------------------------------------------------------------
	public int geefSleepdx()
	{	return dx;
	}
	public int geefSleepdy()
	{	return dy;
	}
	public int geefDrukx()
	{	return eerstex;
	}
	public int geefDruky()
	{	return eerstey;
	}
}	

class Matrix2D
{
	//-------------------------------------------------------------------------------------------
	//deze klasse onthoudt, en berekent steeds opnieuw de tekenrichting, en berekent voor het 
	//Tekenblad aan de hand van een dx en dy het volgende eindpunt van de tekenlijn.
	//-------------------------------------------------------------------------------------------
	
	private double  starthoek, hoek, radHoek, cosHoek, sinHoek, startschaal, schaal;
	private double a11, a12, a21, a22;
 	private static double pi = Math.PI;

	public Matrix2D()
	{	starthoek = 0;
		hoek = 0;
		startschaal = 1;
		schaal = 1;
		radHoek = 0; 
		cosHoek = 1; 
		sinHoek = 0;
		a11 = 1; a12 = 0; a21 = 0; a22 = 1;
	}
	
	public Matrix2D(double hk, double schl)
	{	initialiseer(hk, schl);
	}
	
	public void initialiseer(double hk, double schl)
	{	starthoek = hk;
		startschaal = schl;
		initialiseer();
	}
	
	public void initialiseer()
	{	hoek = starthoek;
		schaal = startschaal;
		draai(0);
	}
	
	private void maakMatrix()
	{	a11 =  schaal * cosHoek;
		a12 = -schaal * sinHoek;
		a21 =  schaal * sinHoek;
		a22 =  schaal * cosHoek;
	}
	
	public void draai(double dHoek)
	{ 	hoek = hoek - dHoek;
		radHoek = hoek / 180 * pi;
		cosHoek = (double) Math.cos(radHoek);
		sinHoek = (double) Math.sin(radHoek);
		maakMatrix();
	}
	
	public void schaal(double s)
	{	schaal = startschaal * s;
		maakMatrix();
	}
	
	public Punt geefVolgendPunt(Punt beginp, double dx, double dy)
	{	Punt eindp = new Punt(beginp.x + a11 * dx + a12 * dy , beginp.y + a21 * dx + a22 * dy);
		return eindp;
	}	
}	
