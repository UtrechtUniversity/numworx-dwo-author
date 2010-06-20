package logotekenap;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Polygon;
import fi.javalogoweb.*;

public class Tekenblad extends JPanel
{
	private int breedte,hoogte;
	private Punt beginpunt,eindpunt,startpunt;
  	private Polygon veelvlak;
  	private Image im ;
  	private Graphics gIm ;
	public Matrix2D mat;  
	private JavaLogoWeb eigenaar;
	private TraceBeheerder trb;
	private boolean pen, vul;
  	private Color penkleur,vulkleur,achtergrondkleur;
	public boolean bezigMetTekenen;
	  
	public Tekenblad(JavaLogoWeb ap)
	{	achtergrondkleur = Color.white;
		veelvlak = new Polygon();
		eigenaar = ap;
		mat = new Matrix2D();					// zorgt voor de tekenrichting
	}
	
	
	
	public void meldTraceBeheerder(TraceBeheerder trb)
	{	this.trb = trb;
	}
	//-------------------------------------------------------------------------------------------
	//deze methoden worden gebruikt door het Tekenblad: om de image te initialiseren en
	//op het scherm te zetten. "paint()" wordt alleen bij de eerste keer tekenen gebruikt, daarna 
	//zorgt "tekenOpnieuw()" of "tekenErbij()" hiervoor. "TekenOpImage()" zorgt voor het vullen 
	//van de image, metbehulp van het door de leerlingen geimplementeerde "tekenprogramma()",
	//en wordt zowel door "paint()" als door "tekenOpImage()" gebruikt
	//-------------------------------------------------------------------------------------------
  	public void paintComponent(Graphics g)
  	{ 	bezigMetTekenen = true;
		if(im==null)
		{	breedte = getSize().width;
			hoogte = getSize().height;	
			double startschaal = Math.min((double)breedte/500,(double)hoogte/500);
			mat.initialiseer(0,startschaal);	
			startpunt = new Punt(breedte/2,hoogte/2);
 			im = createImage(breedte,hoogte);
  			gIm = im.getGraphics();
			tekenOpImage(true);
		}
    	if(trb == null || !trb.geefTraceStatus())g.drawImage(im, 0, 0, null);
		bezigMetTekenen = false;
  	}
	  
  	public void tekenOpImage(boolean wis)
  	{ 	beginpunt = new Punt(startpunt);
    	eindpunt = new Punt(beginpunt);
    	mat.initialiseer();
	  	gIm.setColor(achtergrondkleur);
    	if(wis)gIm.fillRect(0, 0, breedte, hoogte);
    	gIm.setColor(Color.black);
    	gIm.drawRect(0, 0, breedte-1, hoogte-1);
    	//penAan(0,0,0);
    	pen = true;
    	penkleur = Color.black;
    	gIm.setColor(penkleur);
    	penAan();
		vul = false;
    	vulkleur = Color.black;
    	eigenaar.tekenprogramma();
	}
	//-------------------------------------------------------------------------------------------
	//deze methoden worden gebruikt door handlers van het leerlingprogramma
	//-------------------------------------------------------------------------------------------
 	public void tekenOpnieuw()
	{	bezigMetTekenen = true;
		tekenOpImage(true);
		Graphics g = getGraphics();
		if(trb == null || !trb.geefTraceStatus())g.drawImage(im, 0, 0, null);
		bezigMetTekenen = false;
	}
  	void tekenErbij()
	{	bezigMetTekenen = true;
		tekenOpImage(false);
		Graphics g = getGraphics();
		if(trb == null || !trb.geefTraceStatus())g.drawImage(im, 0, 0, null);
		bezigMetTekenen = false;
	}
	//-------------------------------------------------------------------------------------------
	//deze methoden worden gebruikt door het Tekenblad om de lijnen en vlakken te tekenen
	//-------------------------------------------------------------------------------------------
	public void naarVolgendPunt(double dx,double dy)
	{	eindpunt = mat.geefVolgendPunt(beginpunt,dx,dy);
		if(pen)gIm.drawLine((int)Math.rint(beginpunt.x),(int)Math.rint(beginpunt.y),(int)Math.rint(eindpunt.x),(int)Math.rint(eindpunt.y));
		if(vul) veelvlak.addPoint((int)Math.rint(beginpunt.x),(int)Math.rint(beginpunt.y));
		beginpunt.x = eindpunt.x;
		beginpunt.y = eindpunt.y;
	}
	void tekenPolygon()
	{	gIm.setColor(vulkleur);
		gIm.fillPolygon(veelvlak);
		gIm.setColor(penkleur);
		if(pen)gIm.drawPolygon(veelvlak);
	}
	//-------------------------------------------------------------------------------------------
	//deze methoden worden gebruikt door de TraceBeheerder
	//-------------------------------------------------------------------------------------------
	void tekenTraceImage()
	{	Graphics g = getGraphics();
		g.drawImage(im, 0, 0, null);
	}
	void tekenCursor()
	{	Polygon cursor = new Polygon();
		Punt p;
		p = mat.geefVolgendPunt(beginpunt,10,0);
		cursor.addPoint((int)p.x,(int)p.y);
		p = mat.geefVolgendPunt(p,-10,-10);
		cursor.addPoint((int)p.x,(int)p.y);
		p = mat.geefVolgendPunt(p,-10,10);
		cursor.addPoint((int)p.x,(int)p.y);
		gIm.setColor(new Color(255,255,0));
		gIm.fillPolygon(cursor);
		gIm.setColor(new Color(0,0,255));
		gIm.drawPolygon(cursor);
	}
 	//-------------------------------------------------------------------------------------------
	//deze methoden worden gebruikt in "tekenprogramma()" 
	//-------------------------------------------------------------------------------------------
	public boolean links(double dHoek)
	{	mat.draai(dHoek);
		if(trb!=null && trb.geefTraceStatus())
			return trb.volgendeMethode("links("+Integer.toString((int)Math.rint(dHoek))+")");
		else return false;
	}
  	public boolean rechts(double dHoek)
	{	mat.draai(-dHoek);		
		if(trb!=null && trb.geefTraceStatus())
			return trb.volgendeMethode("rechts("+Integer.toString((int)Math.rint(dHoek))+")");
		else return false;
	}
	public boolean vooruit(double dy)
	{	naarVolgendPunt(0,-dy);	
		if(trb!=null)
			return trb.volgendeMethode("vooruit("+Integer.toString((int)Math.rint(dy))+")");
		else return false;
	}
	public boolean stapy(double dy)
	{	naarVolgendPunt(0,-dy);		
		if(trb!=null && trb.geefTraceStatus())
			return trb.volgendeMethode("stapy("+Integer.toString((int)Math.rint(dy))+")");
		else return false;
	}
	public boolean stapx(double dx)
	{	naarVolgendPunt(dx,0);		
		if(trb!=null && trb.geefTraceStatus())
			return trb.volgendeMethode("stapx("+Integer.toString((int)Math.rint(dx))+")");
		else return false;
	}
	public boolean stap(double dx,double dy)
	{	naarVolgendPunt(dx,-dy);
		if(trb!=null && trb.geefTraceStatus())
			return trb.volgendeMethode("stap("+Integer.toString((int)Math.rint(dx))+","+Integer.toString((int)Math.rint(dy))+")");
		else return false;
	}
	public boolean penAan()
	{	pen = true;							
		if(trb!=null && trb.geefTraceStatus())
			return trb.volgendeMethode("penAan()");
		else return false;
	}
	public boolean penAan(String kl)
	{	pen = true;
		penkleur = maakKleur(kl);
		gIm.setColor(penkleur);			
		if(trb!=null && trb.geefTraceStatus())
			return trb.volgendeMethode("penAan("+kl+")");
		else return false;
	}
	public boolean penAan(int r, int g, int b)
	{	pen = true;
		penkleur = new Color(r,g,b);
		gIm.setColor(penkleur);
		if(trb!=null && trb.geefTraceStatus())
			return trb.volgendeMethode("penAan("+Integer.toString(r)+Integer.toString(g)+Integer.toString(b)+")");
		else return false;
	}
	public boolean penUit()
	{	pen = false;							
		if(trb!=null && trb.geefTraceStatus())
			return trb.volgendeMethode("penUit()");
		else return false;
	}
	public boolean vulAan()
	{	vul = true;
		veelvlak = new Polygon();							
		if(trb!=null && trb.geefTraceStatus())
			return trb.volgendeMethode("vulAan()");
		else return false;
	}
	public boolean vulAan(String kl)
	{	vul = true;	
		vulkleur = maakKleur(kl);
		veelvlak = new Polygon();				
		if(trb!=null && trb.geefTraceStatus())
			return trb.volgendeMethode("vulAan("+kl+")");
		else return false;
	}
	public boolean vulAan(int r, int g, int b)
	{	vul = true;	
		vulkleur = new Color(r,g,b);
		veelvlak = new Polygon();	
		if(trb!=null && trb.geefTraceStatus())
			return trb.volgendeMethode("vulAan("+Integer.toString(r)+Integer.toString(g)+Integer.toString(b)+")");
		else return false;
	}
	public boolean vulUit()
	{	tekenPolygon();
		vul = false;							
		if(trb!=null && trb.geefTraceStatus())
			return trb.volgendeMethode("vulUit()");
		else return false;
	}
	
	public boolean varAanpassing(String varNaam, String varValue)
	{	if(trb!=null && trb.geefTraceStatus())
			return trb.volgendeMethode(varNaam + " = " + varValue);
		else return false;
	}
	
	/*public void penAan()
	{	pen = true;
	}
	public void penAan(String kl)
	{	pen = true;
		penkleur = maakKleur(kl);
		gIm.setColor(penkleur);
	}
	public void penAan(int r, int g, int b)
	{	pen = true;
		penkleur = new Color(r,g,b);
		gIm.setColor(penkleur);
	}
	public void penUit()
	{	pen = false;
	}
	public void vulAan()
	{	vul = true;
		veelvlak = new Polygon();
	}
	public void vulAan(String kl)
	{	vul = true;	
		vulkleur = maakKleur(kl);
		veelvlak = new Polygon();
	}
	public void vulAan(int r, int g, int b)
	{	vul = true;	
		vulkleur = new Color(r,g,b);
		veelvlak = new Polygon();
	}
	public void vulUit()
	{	tekenPolygon();
		vul = false;
	}*/
	void achtergrondkleur(String kl)
	{	achtergrondkleur = maakKleur(kl);
	}
	void achtergrondkleur(int r, int g, int b)
	{	achtergrondkleur = new Color(r,g,b);
	}
	void schrijf(String s)
	{	gIm.drawString(s, (int)beginpunt.x, (int)beginpunt.y);
	}
	void schrijf(String s, Font f)
	{	gIm.setFont(f);
		gIm.drawString(s, (int)beginpunt.x, (int)beginpunt.y);
	}
	
	
	Polygon geefVlak()							// geeft de laatst getekende Polygon
	{	return veelvlak;
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
