package fi.geodefull;

import java.awt.*;
import java.awt.event.*;
import fi.beans.grnuminput.*;

public class PyramideProg extends TekenApplet3D implements  NumberListener
{	
	Matrix3D matrot;
	Polygon[] p;
	double k, xhoek,yhoek,hoogte; 
	int aantal;
	Veelvlak v;
	boolean begin,raak;
	NumberSlider hoogteSl;
	NumberArrow aantalInv;
	
	public void initialiseer()
	{	maakMuisActieMogelijk();
		hoogteSl = new NumberSlider(0,100,100,0,"hoogte","");
		hoogteSl.addNumberListener(this);
		hoogteSl.setValue(100);
		rg.gridbag.setConstraints(hoogteSl, rg.c);
		rg.add(hoogteSl);
		hoogteSl.setBackground(Color.white);
		rg.setBackground(Color.white);
		
		aantalInv = new NumberArrow(3,40,4,1,0,"aantal zijden","");
		aantalInv.addNumberListener(this);
		aantalInv.setValue(4);
		rg.gridbag.setConstraints(aantalInv, rg.c);
		rg.add(aantalInv);
		aantalInv.setBackground(Color.white);
		aantal = 4;
		hoogte = 100;
		begin = true;
		k=150;
		matrot = new Matrix3D();
		v = new Pyramide(1.0,4,1);
		p = new Polygon[v.aantalVlakken];
		
		
	}
	public void tekenprogramma()
	{	begindraai(20,-20);
		tekenVeelvlak(0,v);
		
	}
	void begindraai(double xdr,double ydr)
	{	if(begin)
		{	tb.mat.initialiseer();
			matrot.initialiseer();
			matrot.ydraaiAbs(ydr);
			matrot.xdraaiAbs(xdr);
			tb.mat.mult(matrot);
			begin=false;
		}
	}
	void tekenVeelvlak(int n,Veelvlak vv)
	{	for(int i=0 ; i<vv.aantalVlakken ; i++)
		{	tekenVlak(n,vv.vlakken[i]);
			p[i] = geefVlak();
		}
	}
	
	void kleurVeelvlak(Veelvlak v, String kl)
	{	for(int i=0 ; i<v.aantalVlakken ; i++)
		{	v.vlakken[i].vulkleur = kl;
		}
	}
	void geefBasiskleur(Veelvlak v, String kl)
	{	for(int i=0 ; i<v.aantalVlakken ; i++)
		{	v.vlakken[i].vulkleur = kl;
			v.vlakken[i].vorigeKleur = kl;
		}
	}
	void naarVorigeKleur()
	{	for(int i=0 ; i<v.aantalVlakken ; i++)
		{	v.vlakken[i].vulkleur = v.vlakken[i].vorigeKleur;
		}
	}
	void tekenVlak(int n,Vlak v)
	{	penUit();
		stap(k*v.punten[0].x, k*v.punten[0].y, k*v.punten[0].z);
		if(!(v.lijnkleur=="transparant"))penAan("lichtgrijs");
		if(v.vulkleur=="transparant")vulAan(v.vulkleur);
		else vulAan("grijs");
		for(int i=v.aantalHoekpunten-1 ; i>-1 ; i--)
		{	int a=i ; int b=(i+1)%v.aantalHoekpunten;
			stap(k*(v.punten[a].x-v.punten[b].x), k*(v.punten[a].y-v.punten[b].y), k*(v.punten[a].z-v.punten[b].z));
		}
		vulUit();
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
	public void numberChanged(String name,double val)
	{	if(name.equals("hoogte"))
		{	hoogte = val;
			v = new Pyramide(1.0,aantal,hoogte/100);
		}
		else
		{	aantal = (int)val;
			v = new Pyramide(1.0,aantal,hoogte/100);
			p = new Polygon[v.aantalVlakken];
		}
		tekenOpnieuw();
	}
		public void muisKkActie()
	{	for(int i=0 ; i<v.aantalVlakken ; i++)
		{	if(p[i].contains(geefDrukx(),geefDruky()))
			{	if(v.vlakken[i].vulkleur == "transparant")v.vlakken[i].vulkleur = v.vlakken[i].vorigeKleur;
				else v.vlakken[i].vulkleur = "transparant";
				tekenOpnieuw();
			}
		}
	}
	public void muisLosActie()
	{	if((geefDrukx()-geefX())*(geefDrukx()-geefX()) + (geefDruky()-geefY())*(geefDruky()-geefY()) < 10 )
		{	muisKkActie();
		}
		tekenOpnieuw();
	}
	public void muisDrukActie()
	{	for(int i=0 ; i<v.aantalVlakken ; i++)
		{	if(p[i].contains(geefDrukx(),geefDruky()))
			{	raak=true;
				return;
			}
		}
		raak = false;
	}
	public void muisSleepActie()
	{	
		xhoek=-0.5*geefSleepdy();
		yhoek=0.5*geefSleepdx();
		matrot.initialiseer();
		matrot.ydraaiAbs(yhoek);
		matrot.xdraaiAbs(xhoek);
		tb.mat.mult(matrot);
		tekenOpnieuw();
	}
}

