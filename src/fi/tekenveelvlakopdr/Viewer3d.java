package fi.tekenveelvlakopdr;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.JPanel;

import fi.beans.base64code.*;


public class Viewer3d extends JPanel
{
	private AnimatieBeheerder ab;
	private MuisBeheerder mb;
	private int breedte,hoogte;
	private Punt3D beginpunt,eindpunt,startpunt;
  	public Lichaam3D[] l;
  	private Image im ;
  	public Graphics gIm ;
	public Matrix3D mat;  
	private boolean pen, vul,leeg, schaduw;
	private int lnummer;
  	private Color penkleur,vulkleur,achtergrondkleur;
	public boolean bezigMetTekenen, muisAan;
	private double afstand;
	int aantalVeelvlakken;
	Veelvlak[] vvRij;
	private double k, xhoek,yhoek, beginx, beginy;
	
	public Viewer3d(int x, int y,int b, int h)
	{	this(new Veelvlak(), x, y, b, h);
	}
	
	public Viewer3d(Veelvlak v, int x, int y,int b, int h)
	{	setBounds(x,y,b,h);
		aantalVeelvlakken = 1;
		vvRij = new Veelvlak[5];
		vvRij[0] = v;
		//ab = new AnimatieBeheerder(this);
		mb = new MuisBeheerder(this);
		addMouseListener(mb);
		addMouseMotionListener(mb);
		achtergrondkleur = Color.white;
		leeg = false;
		schaduw = true;
		muisAan = true;
		l = new Lichaam3D[5];
		afstand = 1000;
		lnummer=0;
		for(int i=0 ; i<5 ; i++)
		{	l[i] = new Lichaam3D();
			l[i].zetAfstand(afstand);
		}
		mat = new Matrix3D();
		if(mb!=null && ab!=null)				
		{	mb.meldAnimatieBeheerder(ab);		
		}
		//vv = new Kubus(1);
		k=200;
		xhoek = 0;
		yhoek = 0;
		beginx = 30;
		beginy = -30;
	}
	
	public void setState(Hashtable h)
	{	
		double[] hoekpunten = null;
		int[] vlakken = null;
		int[] lijnen = null;
		
		hoekpunten = (double[])h.get("hoekpunten");
		vlakken = (int[])h.get("vlakken");
		lijnen = (int[])h.get("lijnen");
		
		Veelvlak v = new Veelvlak(hoekpunten, vlakken, lijnen);
		vvRij[0] = v;
	}
	
	public void setBackground(Color c)
	{	achtergrondkleur = c;
		super.setBackground(c);
	}
	
	public void zetAfstand(double afst)
	{	afstand = afst;
		for(int i=0 ; i<5 ; i++)
			{	l[i].zetAfstand(afst);
			}
	}
	public void zetSchaduw(boolean s)
	{	schaduw = s;
	}
	public void zetBeginHoeken(double hx, double hy)
	{	beginx = hx;
		beginy = hy;
		xhoek = 0;
		yhoek = 0;
	}
	public void zetMuisAan(boolean b)
	{	muisAan = b;
	}
	public void zetVeelvlak(Veelvlak v)
	{	vvRij[0] = v;
		tekenOpnieuw();
	}
	public void zetVeelvlak(Veelvlak v, int n)
	{	vvRij[n] = v;
		tekenOpnieuw();
	}
	public void voegVeelvlakToe(Veelvlak v)
	{	vvRij[aantalVeelvlakken] = v;
		aantalVeelvlakken++;
	}
	
	void tekenprogramma()
	{	mat.initialiseer();
		mat.xdraai(beginx+xhoek);mat.ydraai(beginy+yhoek);
		for(int i=0 ; i<aantalVeelvlakken ; i++)
		tekenVeelvlak(0,vvRij[i]);
	}
	void tekenVeelvlak(int n,Veelvlak vv)
	{	for(int i=0 ; i<vv.aantalVlakken ; i++)
		{	tekenVlak(n,vv.vlakken[i]);
		}
		for(int i=0 ; i<vv.aantalLijnen ; i++)
		{	tekenLijn(vv.lijnen[i]);
		}
	}
	void tekenLijn(Lijn l)
	{	penUit();
		stap(k*l.hpunt1.x, k*l.hpunt1.y, k*l.hpunt1.z);
		penAan(1,l.kleur);
		stap(k*l.hpunt2.x - k*l.hpunt1.x, k*l.hpunt2.y - k*l.hpunt1.y, k*l.hpunt2.z - k*l.hpunt1.z);
		penUit(1);
		stap(-k*l.hpunt2.x, -k*l.hpunt2.y, -k*l.hpunt2.z);
	}
	void tekenVlak(int n,Vlak v)
	{	penUit();
		stap(k*v.punten[0].x, k*v.punten[0].y, k*v.punten[0].z);
		if(!(v.lijnkleur=="transparant"))penAan(v.lijnkleur);
		vulAan(n,v.vulkleur);
		for(int i=0 ; i<v.aantalHoekpunten ; i++)
		{	int a=i ; int b=(i+1)%v.aantalHoekpunten;
			stap(-k*(v.punten[a].x-v.punten[b].x), -k*(v.punten[a].y-v.punten[b].y), -k*(v.punten[a].z-v.punten[b].z));
		}
		vulUit(n);
		penUit();
		stap(-k*v.punten[0].x, -k*v.punten[0].y, -k*v.punten[0].z);
		
	}
	public void paint(Graphics g)
  	{ 	bezigMetTekenen = true;
		if(im==null)
		{	breedte = getSize().width;
			hoogte = getSize().height;	
			double startschaal = Math.min((double)breedte/500,(double)hoogte/500);
			mat.initialiseer(0,0,0,startschaal);	
			startpunt = new Punt3D(breedte/2,hoogte/2,0);
			for(int i=0 ; i<5 ; i++)
			{	l[i].maakNulpunt(breedte/2,hoogte/2,0);
			}
			im = createImage(breedte,hoogte);
  			gIm = im.getGraphics();
			tekenOpImage(true);
		}
    	g.drawImage(im, 0, 0, null);
		bezigMetTekenen = false;
  	}
	
  	public void tekenOpImage(boolean wis)
  	{ 	beginpunt = new Punt3D(startpunt);
    	eindpunt = new Punt3D(beginpunt);
		//mat.initialiseer();
	  	gIm.setColor(achtergrondkleur);
    	if(wis)gIm.fillRect(0, 0, breedte, hoogte);
    	penAan(0,0,0);
		vul = false;
    	tekenprogramma();
		for(int i=0 ; i<5 ; i++)
		{	l[i].sorteer();
		}
			
		for(int j=0 ; j<5 ; j++)
		{
			for(int i=0 ; i<l[j].aantalPolygonen ; i++)
			{
				if(l[j].vlakken[i].normaal.z >0)
				{	if(schaduw)
					{	double grijsfactor = 0.5*((-l[j].vlakken[i].normaal.x - l[j].vlakken[i].normaal.y + l[j].vlakken[i].normaal.z)/Math.sqrt(3)+1);
						if(grijsfactor<0)grijsfactor=0;if(grijsfactor>1)grijsfactor=1;
						int roodwaarde = 50+(int)(l[j].vlakken[i].vulkleur.getRed()*grijsfactor*0.75);
						int groenwaarde = 50+(int)(l[j].vlakken[i].vulkleur.getGreen()*grijsfactor*0.75);
						int blauwwaarde = 50+(int)(l[j].vlakken[i].vulkleur.getBlue()*grijsfactor*0.75);
						gIm.setColor(new Color(roodwaarde,groenwaarde,blauwwaarde));
					}
					else
					{	gIm.setColor(l[j].vlakken[i].vulkleur);
					}
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
			l[j].zetAfstand(afstand);
			l[j].maakNulpunt(breedte/2,hoogte/2,0);
		}
	}
	
	//-------------------------------------------------------------------------------------------
	//deze methoden worden gebruikt door handlers van het leerlingprogramma
	//-------------------------------------------------------------------------------------------
	void tekenOpnieuw()
	{	if(im==null)return;
		bezigMetTekenen = true;
		tekenOpImage(true);
		Graphics g = getGraphics();
		g.drawImage(im, 0, 0, null); 
		bezigMetTekenen = false;
	}
  
  	void tekenErbij()
	{	bezigMetTekenen = true;
		tekenOpImage(false);
		Graphics g = getGraphics();
		g.drawImage(im, 0, 0, null);
		bezigMetTekenen = false;
	}

	void stap(double dx,double dy,double dz)
	{	naarVolgendPunt(dx,-dy,-dz);
	}
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
	void schrijf(String s)
	{	gIm.drawString(s, (int)beginpunt.x, (int)beginpunt.y);
	}
	void schrijf(String s, Font f)
	{	gIm.setFont(f);
		gIm.drawString(s, (int)beginpunt.x, (int)beginpunt.y);
	}
	Punt geefPunt()								// geeft de laatst getekende Punt
	{	double pf = (afstand-beginpunt.z)/afstand;
		double begx = l[0].nulpunt.x + (beginpunt.x-l[0].nulpunt.x)/pf;
		double begy = l[0].nulpunt.y + (beginpunt.y-l[0].nulpunt.y)/pf;
		return new Punt(begx,begy);
	}
	Punt geefPunt(int n)								// geeft de laatst getekende Punt
	{	double pf = (afstand-beginpunt.z)/afstand;
		double begx = l[n].nulpunt.x + (beginpunt.x-l[n].nulpunt.x)/pf;
		double begy = l[n].nulpunt.y + (beginpunt.y-l[n].nulpunt.y)/pf;
		return new Punt(begx,begy);
	}

	Polygon geefVlak()
	{	if(l[0].vlakken[l[0].aantalPolygonen-1].normaal.z > 0)
		return l[0].vlakken[l[0].aantalPolygonen-1].pol;
		else return new Polygon();
	}
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
	public void animatie(){}
	
	public void muisSleepActie()
	{	if(muisAan)
		{	xhoek -= 0.5*mb.geefSleepdy();
			if(xhoek>90-beginx)xhoek=90-beginx;
			if(xhoek<-90-beginx)xhoek=-90-beginx;
			yhoek += 0.5*mb.geefSleepdx();
			tekenOpnieuw();
		}
	}
	public void muisDrukActie(){}
	public void muisKlikActie(){}
	public void muisLosActie(){}
}

	
	
	
    









	

