package fi.draaibank;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Polygon;

import javax.swing.JPanel;

class Tekenblad3D2 extends JPanel
{
	private int breedte,hoogte;
	private Punt3D beginpunt,eindpunt,startpunt;
  	public Lichaam3D[] l;
  	public Graphics gIm ;
	public Matrix3D mat;  
	private TekenPanel eigenaar;
	private boolean pen, vul,leeg;
	private int lnummer;
  	private Color penkleur,vulkleur,achtergrondkleur;
	public boolean bezigMetTekenen;
	
	public Tekenblad3D2(TekenPanel ap)
	{	
		achtergrondkleur = Color.white;
		leeg = false;
		l = new Lichaam3D[5];
		lnummer=0;
		for(int i=0 ; i<5 ; i++)
		{	l[i] = new Lichaam3D();
		}
		eigenaar = ap;
		mat = new Matrix3D();
		
	}

	//public void paint(Graphics g)
	public void paintComponent(Graphics g)
  	{ 	
		
		bezigMetTekenen = true;
		//if (im == null)
		//{	
			boolean herschalen = false;
		
			int nbreedte = getSize().width;
			int nhoogte = getSize().height;
			if (breedte == 0)
				breedte = nbreedte;
			if (hoogte == 0)
				hoogte = nhoogte;
						
			if ((nbreedte != breedte)||(nhoogte != hoogte))
			{	breedte = nbreedte;
				hoogte = nhoogte;
				herschalen = true;
			}
			
			double startschaal = Math.min((double)breedte / 500,(double) hoogte / 500);
			 
			//mat.initialiseer(0,0,0,startschaal);
			if (herschalen)
				mat.herschaal(startschaal);
			
			startpunt = new Punt3D(breedte/2,hoogte/2,0);
			for(int i=0 ; i<5 ; i++)
			{	l[i].maakNulpunt(breedte/2,hoogte/2,0);
			}
			//im = createImage(breedte,hoogte);
  			//gIm = im.getGraphics();
			gIm = g;
			tekenOpImage(true);
		//}
    	//g.drawImage(im, 0, 0, null);
		bezigMetTekenen = false;
  	}
	  

  	public void tekenOpImage(boolean wis)
  	{ 	beginpunt = new Punt3D(startpunt);
    	eindpunt = new Punt3D(beginpunt);
		//Peter mat.initialiseer();
	  	gIm.setColor(achtergrondkleur);
    	if(wis)
    		gIm.fillRect(0, 0, breedte, hoogte);
    	penAan(0,0,0);
		vul = false;
    	eigenaar.tekenprogramma();
		for(int i=0 ; i<5 ; i++)
		{	l[i].sorteer();
		}
			
		for(int j=0 ; j<5 ; j++)
		{
			for(int i=0 ; i<l[j].aantalPolygonen ; i++)
			{
//System.out.println("l-" + j + " ap = " + l[j].aantalPolygonen);				
				
				if(l[j].vlakken[i].normaal.z >0)
				{	double grijsfactor = 0.5*((-l[j].vlakken[i].normaal.x - l[j].vlakken[i].normaal.y + l[j].vlakken[i].normaal.z)/Math.sqrt(3)+1);
					if(grijsfactor<0)grijsfactor=0;if(grijsfactor>1)grijsfactor=1;
					int roodwaarde = 50+(int)(l[j].vlakken[i].vulkleur.getRed()*grijsfactor*0.75);
					int groenwaarde = 50+(int)(l[j].vlakken[i].vulkleur.getGreen()*grijsfactor*0.75);
					int blauwwaarde = 50+(int)(l[j].vlakken[i].vulkleur.getBlue()*grijsfactor*0.75);
					gIm.setColor(new Color(roodwaarde,groenwaarde,blauwwaarde));
					if(!l[j].vlakken[i].isLeeg)gIm.fillPolygon(l[j].vlakken[i].pol);
					gIm.setColor(l[j].vlakken[i].lijnkleur);
					if(!l[j].vlakken[i].isLijn && l[j].vlakken[i].isOmlijnd )
					{	gIm.setColor(l[j].vlakken[i].lijnkleur);
						gIm.drawPolygon(l[j].vlakken[i].pol);
					}
					if (l[j].vlakken[i].isLijn)
					{	//int grw = (int)(125-0.7*l[j].vlakken[i].gemz);
						//gIm.setColor(new Color(grw,grw,grw));
						gIm.setColor(l[j].vlakken[i].lijnkleur);
						gIm.drawPolygon(l[j].vlakken[i].pol);
						penkleur = new Color(0,0,0);
					}
				}
			}
			l[j] = new Lichaam3D();			
			l[j].maakNulpunt(breedte/2,hoogte/2,0);
		}
	}
	
	//-------------------------------------------------------------------------------------------
	//deze methoden worden gebruikt door handlers van het leerlingprogramma
	//-------------------------------------------------------------------------------------------
	void tekenOpnieuw()
	{	
/*		
		bezigMetTekenen = true;
		tekenOpImage(true);
		//Graphics g = getGraphics();
		//g.drawImage(im, 0, 0, null); 
		bezigMetTekenen = false;
*/		
		repaint();
		
	}
  
/*	
  	void tekenErbij()
	{	bezigMetTekenen = true;
		tekenOpImage(false);
		//Graphics g = getGraphics();
		//g.drawImage(im, 0, 0, null);
		bezigMetTekenen = false;
	}
*/
	//-------------------------------------------------------------------------------------------
	//deze methoden worden gebruikt door het Tekenblad om de lijnen en vlakken te tekenen
	//-------------------------------------------------------------------------------------------

	void naarVolgendPunt(double dx,double dy, double dz)
	{	eindpunt = mat.geefVolgendPunt(beginpunt,dx,dy,dz);
		
		if(pen && !vul)
		{	l[lnummer].voegPuntToe(beginpunt);
			l[lnummer].voegPuntToe(eindpunt);
			l[lnummer].voegPolygonToe(penkleur,penkleur,true, false);
		}
		
		if(vul)  		 
		{	l[lnummer].voegPuntToe(beginpunt);
		}
		beginpunt.x = eindpunt.x;
		beginpunt.y = eindpunt.y;
		beginpunt.z = eindpunt.z;
	}
	
	void tekenPolygon()
	{	l[0].voegPolygonToe(vulkleur, penkleur, pen, leeg);
	}
	void tekenPolygon(int n)
	{	l[n].voegPolygonToe(vulkleur, penkleur, pen, leeg);
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
	void penAan(int n)
	{	pen = true;
		lnummer=n;
	}
	void penAan(int n,String kl)
	{	pen = true;
		penkleur = maakKleur(kl);
		gIm.setColor(penkleur);
		lnummer=n;
	}
	void penAan(int n,int r, int g, int b)
	{	pen = true;
		penkleur = new Color(r,g,b);
		gIm.setColor(penkleur);
		lnummer=n;
	}
	void penUit()
	{	pen = false;
	}
	void penUit(int n)
	{	pen = false;
		lnummer=0;
	}
	void vulAan()
	{	vul = true;
	}
	void vulAan(String kl)
	{	vul = true;
		if(kl.equals("transparant"))leeg = true;
		vulkleur = maakKleur(kl);
	}
	void vulAan(int r, int g, int b)
	{	vul = true;	
		vulkleur = new Color(r,g,b);
	}
	void vulAan(int n)
	{	vul = true;
		lnummer=n;
	}
	void vulAan(int n,String kl)
	{	vul = true;
		lnummer=n;
		if(kl.equals("transparant"))leeg = true;
		vulkleur = maakKleur(kl);
	}
	void vulAan(int n,int r, int g, int b)
	{	vul = true;
		lnummer=n;
		vulkleur = new Color(r,g,b);
	}
	void vulAan(Color kl)
	{	vul = true;	
		vulkleur = kl;
	}

	void vulUit()
	{	tekenPolygon();
		vul = false;
		lnummer=0;
		leeg = false;
	}
	void vulUit(int n)
	{	tekenPolygon(n);
		vul = false;
		lnummer=0;
		leeg = false;
	}
	void achtergrondkleur(String kl)
	{	achtergrondkleur = maakKleur(kl);
	}
	void achtergrondkleur(int r, int g, int b)
	{	achtergrondkleur = new Color(r,g,b);
	}
	void achtergrondkleur(Color c)
	{	achtergrondkleur = c;
	}
	void schrijf(String s)
	{	gIm.drawString(s, (int)beginpunt.x, (int)beginpunt.y);
	}
	void schrijf(String s, Font f)
	{	gIm.setFont(f);
		gIm.drawString(s, (int)beginpunt.x, (int)beginpunt.y);
	}
	Punt geefPunt()								// geeft de laatst getekende Punt
	{	double pf = (1000-beginpunt.z)/1000;
		double begx = l[0].nulpunt.x + (beginpunt.x-l[0].nulpunt.x)/pf;
		double begy = l[0].nulpunt.y + (beginpunt.y-l[0].nulpunt.y)/pf;
		return new Punt(begx,begy);
	}
	Punt geefPunt(int n)								// geeft de laatst getekende Punt
	{	double pf = (1000-beginpunt.z)/1000;
		double begx = l[n].nulpunt.x + (beginpunt.x-l[n].nulpunt.x)/pf;
		double begy = l[n].nulpunt.y + (beginpunt.y-l[n].nulpunt.y)/pf;
		return new Punt(begx,begy);
	}

	Polygon geefVlak()
 	{	if(l[0].vlakken[l[0].aantalPolygonen-1].normaal.z > 0)
		return l[0].vlakken[l[0].aantalPolygonen-1].pol;
		else return new Polygon();
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
