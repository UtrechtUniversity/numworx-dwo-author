package fi.draaibank;				 

import java.awt.*;
import java.awt.event.*;

import javax.swing.JPanel;

import fi.beans.mainframe.JApplet;

public class TekenApplet3D extends JApplet 
{
	Tekenblad3D tb;
	private MuisBeheerder mb;
		  
	//-----------------------------------------------------------------------------------------
	// initalisatie
	//-----------------------------------------------------------------------------------------
	public void init()
	{	tb = new Tekenblad3D(this);
		getContentPane().setLayout(null);
		initialiseer();
		tb.setBounds(0, 0, getSize().width, getSize().height);
		getContentPane().add(tb);
	}	
	
	//-------------------------------------------------------------------------------------------
	//deze methoden kunnen alleen worden gebruikt in  "initialiseer()" van leerling-applet
	//-------------------------------------------------------------------------------------------
	public void maakMuisActieMogelijk()
	{	mb = new MuisBeheerder(this);
		tb.addMouseListener(mb);
		tb.addMouseMotionListener(mb);
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
	public int geefX()
	{	return mb.geefX();
	}
	public int geefY()
	{	return mb.geefY();
	}

	public void tekenOpnieuw()
	{	repaint();	
	}
	
/*	
	public void tekenOpnieuw()
	{	tb.tekenOpnieuw();	
	}
	public void tekenErbij()
	{	tb.tekenErbij();
	}
*/
  	//-------------------------------------------------------------------------------------------
	//deze methoden worden gebruikt in "initialiseer" en doorgegeven aan Tekenblad tb (of 
	//Matrix2d) 
	//-------------------------------------------------------------------------------------------
	public void schaal(double s)
	{ tb.mat.schaal(s);
	}
	public void achtergrondkleur(String kl)
	{	tb.achtergrondkleur(kl);
	}
	public void achtergrondkleur(int r, int g, int b)
	{	tb.achtergrondkleur(r, g, b);
	}
	public void achtergrondkleur(Color c)
	{	tb.achtergrondkleur(c);
	}
	
	//-------------------------------------------------------------------------------------------
	//deze methoden worden gebruikt in "tekenprogramma()" en doorgegeven aan Tekenblad tb (of 
	//Matrix2d) 
	//-------------------------------------------------------------------------------------------
	public void xdraai(double dh)
	{	tb.mat.xdraai(dh);
	}
	public void ydraai(double dh){tb.mat.ydraai(dh);}
	public void zdraai(double dh){tb.mat.zdraai(dh);}
	public void rechts(double dh){tb.mat.zdraai(-dh);}
	public void links(double dh){tb.mat.zdraai(dh);}
	public void stapy(double dy){tb.naarVolgendPunt(0,-dy,0);}
	public void vooruit(double dy){tb.naarVolgendPunt(0,-dy,0);}
	public void stapx(double dx){tb.naarVolgendPunt(dx,0,0);}
	public void stapz(double dz){tb.naarVolgendPunt(0,0,-dz);}
	public void stap(double dx,double dy,double dz){tb.naarVolgendPunt(dx,-dy,-dz);}
	public void stap(double dx,double dy){tb.naarVolgendPunt(dx,-dy,0);}
	public void penAan(){tb.penAan();}
	public void penAan(String kl){tb.penAan(kl);}
	public void penAan(int r, int g, int b){tb.penAan(r, g, b);}
	public void penAan(int n){tb.penAan(n);}
	public void penAan(int n,String kl){tb.penAan(n,kl);}
	public void penAan(int n,int r, int g, int b){tb.penAan(n,r, g, b);}
	public void penUit(){tb.penUit();}
	public void penUit(int n){tb.penUit(n);}
	public void vulAan(){tb.vulAan();}
	public void vulAan(String kl){tb.vulAan(kl);}
	public void vulAan(int r, int g, int b){tb.vulAan(r, g, b);}
	public void vulAan(int n){tb.vulAan(n);}
	public void vulAan(int n,String kl){tb.vulAan(n,kl);}
	public void vulAan(int n,int r, int g, int b){tb.vulAan(n,r, g, b);}
	public void vulAan(Color kl){tb.vulAan(kl);	}
	public void vulUit(){tb.vulUit();}
	public void vulUit(int n){tb.vulUit(n);}
	public void schrijf(String s){tb.schrijf(s);}
	public void schrijf(String s, Font f){tb.schrijf(s,f);}
	public Polygon geefVlak(){return tb.geefVlak();}
	public Punt geefPunt(){return tb.geefPunt();}
	public Punt geefPunt(int n){return tb.geefPunt(n);}

	
	//-------------------------------------------------------------------------------------------
	//deze methoden worden geimplemeteerd in het leerlingenprogramma
	//-------------------------------------------------------------------------------------------
	public void tekenprogramma(){}
	public void initialiseer(){}
	public void muisSleepActie(){}
	public void muisDrukActie(){}
	public void muisKlikActie(){}
	public void muisLosActie(){}
}		
	
/*    
class Punt3D

{	double x, y, z;
		
	Punt3D(double x, double y,double z)
	{	this.x = x;
		this.y = y;
		this.z = z;
	}
		
	Punt3D(Punt3D p)
	{	this.x = p.x;
		this.y = p.y;
		this.z = p.z;
	}
}
*/


class Tekenblad3D extends JPanel
{
	private int breedte,hoogte;
	private Punt3D beginpunt,eindpunt,startpunt;
  	public Lichaam3D[] l;
  	public Graphics gIm ;
	public Matrix3D mat;  
	private TekenApplet3D eigenaar;
	private boolean pen, vul,leeg;
	private int lnummer;
  	private Color penkleur,vulkleur,achtergrondkleur;
	public boolean bezigMetTekenen;
	
	public Tekenblad3D(TekenApplet3D ap)
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
		
//System.out.println("tb paint");		
		
		bezigMetTekenen = true;
		//if (im == null)
		//{	
			breedte = getSize().width;
			hoogte = getSize().height;	
			double startschaal = Math.min((double)breedte / 500,(double) hoogte / 500);
			//DIT ERUIT mat.initialiseer(0,0,0,startschaal);	
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



class MuisBeheerder implements MouseListener, MouseMotionListener
{
	private int eerstex, laatstex, eerstey, laatstey, dx, dy;
	private TekenApplet3D eigenaar;
	
	public MuisBeheerder(TekenApplet3D ap)
	{	eigenaar = ap;
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
	{	
		eerstex = e.getX();
		eerstey = e.getY();
		laatstex = e.getX();
		laatstey = e.getY();
		eigenaar.muisDrukActie();
	}
	
	public void mouseDragged(MouseEvent e)
	{	int x = e.getX();
		int y = e.getY();
		dx = x - laatstex;
		dy = laatstey -y;
		eigenaar.muisSleepActie();
		laatstex = x;
		laatstey = y;	
	}
	
	public void mouseReleased(MouseEvent e)
	{	eigenaar.muisLosActie();
		
	}
	public void mouseClicked(MouseEvent e)
	{	
		eerstex = e.getX();
		eerstey = e.getY();
		laatstex = e.getX();
		laatstey = e.getY();
		eigenaar.muisKlikActie();
	
	}
	public void mouseExited(MouseEvent e){;}
	
	public void mouseEntered(MouseEvent e){;}
	public void mouseMoved(MouseEvent e){;}
	
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
	public int geefX()
	{	return laatstex;
	}
	public int geefY()
	{	return laatstey;
	}

}	

/*
class Rotatie3D
{
	public int as;
	public double rotatieHoek;

	public Rotatie3D(int as, double rotatieHoek)
	{	this.as = as;
		this.rotatieHoek = rotatieHoek;
	}
}
*/
/*
class Matrix3D
{	
	//-------------------------------------------------------------------------------------------
	//deze klasse onthoudt, en berekent steeds opnieuw de tekenrichting, en berekent voor het 
	//Tekenblad aan de hand van een dx,dy en dz het volgende eindpunt van de tekenlijn.
	//-------------------------------------------------------------------------------------------

	private Rotatie3D[] rotatieRij;
	private int aantalRotaties;
	private double starthoekx,starthoeky,starthoekz,startschaal;
	private double xx, xy, xz ;
	private double yx, yy, yz;
	private double zx, zy, zz;
 	private static double pi = Math.PI;

	public Matrix3D()
	{	rotatieRij = new Rotatie3D[150];
		aantalRotaties = 0;
		starthoekx = 0;
		starthoeky = 0;
		starthoekz = 0;
		startschaal = 1;
		xx = 1.0;
		yy = 1.0;
		zz = 1.0;
	}
	void initialiseer()
	{	rotatieRij = new Rotatie3D[150];
		aantalRotaties = 0;
		xx=1;xy=0;xz=0;
		yx=0;yy=1;yz=0;
		zx=0;zy=0;zz=1;
		schaal(startschaal);
		ydraaiAbs(starthoeky);
		xdraaiAbs(starthoekx);
		zdraaiAbs(starthoekz);
	}
	
	void initialiseer(double hx, double hy, double hz, double schl)
	{	starthoekx = hx;
		starthoeky = hy;
		starthoekz = hz;
		startschaal = schl;
		initialiseer();
	}	
			
	void schaal(double f) 
	{
		xx *= f;
		xy *= f;
		xz *= f;
		yx *= f;
		yy *= f;
		yz *= f;
		zx *= f;
		zy *= f;
		zz *= f;
   }

	void xdraai(double theta) 
	{	voegRotatieToe(1,theta);
	}
	void ydraai(double theta) 
	{	voegRotatieToe(2,theta);
	}
	void zdraai(double theta) 
	{	voegRotatieToe(3,theta);
	}
	public void voegRotatieToe(int as, double rotatieHoek)
	{	Rotatie3D r = new Rotatie3D(as,rotatieHoek);
		for(int i = 0; i<aantalRotaties ; i++)
		{	Rotatie3D rt = rotatieRij[i];
			if(rt.as == 1)xdraaiAbs(-rt.rotatieHoek);
			else if(rt.as == 2)ydraaiAbs(-rt.rotatieHoek);
			else if(rt.as == 3)zdraaiAbs(-rt.rotatieHoek);
		}
		if(r.as == 1)xdraaiAbs(rotatieHoek);
		else if(r.as == 2)ydraaiAbs(rotatieHoek);
		else if(r.as == 3)zdraaiAbs(rotatieHoek);
		for(int i = aantalRotaties ; i>0 ; i--)
		{	Rotatie3D rt = rotatieRij[i-1];
			if(rt.as == 1)xdraaiAbs(rt.rotatieHoek);
			else if(rt.as == 2)ydraaiAbs(rt.rotatieHoek);
			else if(rt.as == 3)zdraaiAbs(rt.rotatieHoek);
		}
		
		if(aantalRotaties>0 && (rotatieRij[aantalRotaties-1].as == as))
		{	rotatieRij[aantalRotaties-1].rotatieHoek += rotatieHoek;
			if(rotatieRij[aantalRotaties-1].rotatieHoek%360 == 0) aantalRotaties--;
		}
		else 
		{	rotatieRij[aantalRotaties] = r;
			aantalRotaties++;
		}
	}

	void ydraaiAbs(double theta) 
	{
		theta *= (pi / 180);
		double ct = Math.cos(theta);
		double st = Math.sin(theta);

		double Nxx =  (xx * ct + zx * st);
		double Nxy =  (xy * ct + zy * st);
		double Nxz =  (xz * ct + zz * st);

		double Nzx =  (zx * ct - xx * st);
		double Nzy =  (zy * ct - xy * st);
		double Nzz =  (zz * ct - xz * st);

		xx = Nxx;
		xy = Nxy;
		xz = Nxz;
		zx = Nzx;
		zy = Nzy;
		zz = Nzz;
    }

    void xdraaiAbs(double theta) 
    {
		theta *= (pi / 180);
		double ct = Math.cos(theta);
		double st = Math.sin(theta);

		double Nyx = (yx * ct + zx * st);
		double Nyy = (yy * ct + zy * st);
		double Nyz = (yz * ct + zz * st);

		double Nzx = (zx * ct - yx * st);
		double Nzy = (zy * ct - yy * st);
		double Nzz = (zz * ct - yz * st);

		yx = Nyx;
		yy = Nyy;
		yz = Nyz;
		zx = Nzx;
		zy = Nzy;
		zz = Nzz;
	}

	void zdraaiAbs(double theta) 
	{
		theta *= -(pi / 180);
		double ct = Math.cos(theta);
		double st = Math.sin(theta);

		double Nyx = (yx * ct + xx * st);
		double Nyy = (yy * ct + xy * st);
		double Nyz = (yz * ct + xz * st);

		double Nxx = (xx * ct - yx * st);
		double Nxy = (xy * ct - yy * st);
		double Nxz = (xz * ct - yz * st);

		yx = Nyx;
		yy = Nyy;
		yz = Nyz;
		xx = Nxx;
		xy = Nxy;
		xz = Nxz;
	}
	
	void mult(Matrix3D rhs) 	{			double lxx = xx * rhs.xx + yx * rhs.xy + zx * rhs.xz;
		double lxy = xy * rhs.xx + yy * rhs.xy + zy * rhs.xz;
		double lxz = xz * rhs.xx + yz * rhs.xy + zz * rhs.xz;

		double lyx = xx * rhs.yx + yx * rhs.yy + zx * rhs.yz;
		double lyy = xy * rhs.yx + yy * rhs.yy + zy * rhs.yz;
		double lyz = xz * rhs.yx + yz * rhs.yy + zz * rhs.yz;

		double lzx = xx * rhs.zx + yx * rhs.zy + zx * rhs.zz;
		double lzy = xy * rhs.zx + yy * rhs.zy + zy * rhs.zz;
		double lzz = xz * rhs.zx + yz * rhs.zy + zz * rhs.zz;

		xx = lxx;
		xy = lxy;
		xz = lxz;

		yx = lyx;
		yy = lyy;
		yz = lyz;

		zx = lzx;
		zy = lzy;
		zz = lzz;
    }

	Punt3D geefVolgendPunt(Punt3D bp, double dx, double dy, double dz)
	{
		Punt3D ep = new Punt3D(0,0,0);
		ep.x = bp.x + dx*xx + dy*xy + dz*xz;
		ep.y = bp.y + dx*yx + dy*yy + dz*yz;
		ep.z = bp.z + dx*zx + dy*zy + dz*zz;
		return ep;
	}
}
*/
/*
class Polygon3D
{
	public Polygon pol;
	public Punt3D normaal;
	public double gemz;
	public Color vulkleur,lijnkleur;
	public boolean isLijn,isOmlijnd,isLeeg;
}
*/
/*
class Lichaam3D
{
	public int[] xcoor;
	public int[] ycoor;
	public int[] zcoor;
	public double[] xcoord;
	public double[] ycoord;
	public double[] zcoord;

	public Polygon3D[] vlakken, vlakkenSort;
	public int aantalPunten, aantalPolygonen;
	private Polygon3D huidigePolygon;
	private double pf;
	Punt3D nulpunt;

	public Lichaam3D()
	{	xcoor = new int[20];
		ycoor = new int[20];
		zcoor = new int[20];
		xcoord = new double[20];
		ycoord = new double[20];
		zcoord = new double[20];

		vlakken = new Polygon3D[1000];
		aantalPunten = 0;
		aantalPolygonen = 0;
		nulpunt = new Punt3D(0,0,0);
	}
	public void maakNulpunt(double x,double y,double z)
	{	nulpunt.x = x;
		nulpunt.y = y;
		nulpunt.z = z;
	}
	
	public void voegPuntToe(Punt3D p)
	{	pf = (1000-p.z)/1000;
		xcoord[aantalPunten] = nulpunt.x + (p.x-nulpunt.x)/pf;
		ycoord[aantalPunten] = nulpunt.y + (p.y-nulpunt.y)/pf;
		zcoord[aantalPunten] = p.z;
		xcoor[aantalPunten] = (int)xcoord[aantalPunten];
		ycoor[aantalPunten] = (int)ycoord[aantalPunten];
		zcoor[aantalPunten] = (int)p.z;

		aantalPunten++;
	}
	
	public void voegPolygonToe(Color vulkl, Color lijnkl, boolean isOmlnd, boolean isLg )
	{	huidigePolygon = new Polygon3D();
		huidigePolygon.pol = new Polygon(xcoor,ycoor,aantalPunten);
		if(aantalPunten<3)
		{
			huidigePolygon.normaal = new Punt3D(0,0,1);
			huidigePolygon.isLijn = true;
		}
		else
		{	double ux = xcoord[1] - xcoord[0];
			double uy = ycoord[1] - ycoord[0];
			double uz = zcoord[1] - zcoord[0];
			double vx = xcoord[2] - xcoord[1];
			double vy = ycoord[2] - ycoord[1];
			double vz = zcoord[2] - zcoord[1];
			double nx = uy*vz - uz*vy;	
			double ny = uz*vx - ux*vz;
			double nz = ux*vy - uy*vx;
			double ln = Math.sqrt(nx*nx + ny*ny + nz*nz);
			double nex = nx/ln;
			double ney = ny/ln;
			double nez = nz/ln;
			huidigePolygon.normaal = new Punt3D(nex,ney,nez);
		}
		double gz = 0;
		for(int i=0 ; i<aantalPunten ; i++)
		{	gz = gz + zcoor[i];
		}
		huidigePolygon.gemz = gz/aantalPunten;
		huidigePolygon.vulkleur = vulkl;
		huidigePolygon.lijnkleur = lijnkl;
		huidigePolygon.isOmlijnd = isOmlnd;
		huidigePolygon.isLeeg = isLg;
		aantalPunten = 0;
		vlakken[aantalPolygonen] = huidigePolygon;
		aantalPolygonen++;
		
	}

	public void sorteer()
	{	for(int j=0 ; j<aantalPolygonen ;j++)
		{
			for(int i=j+1 ; i<aantalPolygonen ; i++)
			{
				if(vlakken[j].gemz > vlakken[i].gemz)
				{
					huidigePolygon = vlakken[j];
					vlakken[j] = vlakken[i];
					vlakken[i] = huidigePolygon;
				}
			}
		}
	}
}
*/