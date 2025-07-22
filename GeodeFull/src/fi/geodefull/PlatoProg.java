package fi.geodefull;

import java.awt.*;
import java.awt.event.*;
import fi.beans.grnuminput.*;


public class PlatoProg extends TekenApplet3D
{	
	Matrix3D matrot;
	double k, xhoek,yhoek;
	Veelvlak v;
	Polygon[] p;
	boolean begin, raak;
	String veelvlakNaam;
	
	
	public void initialiseer()
	{	maakMuisActieMogelijk();
		veelvlakNaam = getParameter("veelvlak");
		matrot = new Matrix3D();
		k=245;
		begin=true;
		raak = false;
		if(veelvlakNaam.equals("tetraeder"))
		{	v = new Tetraeder(1);
			geefBasiskleur(v,"rood");
		}
		if(veelvlakNaam.equals("kubus"))
		{	v = new Kubus(1);
			geefBasiskleur(v,"oranje");
		}
		if(veelvlakNaam.equals("octaeder"))
		{	v = (new Kubus(1.7)).dualiseer();
			geefBasiskleur(v,"cyaan");
		}
		if(veelvlakNaam.equals("icosaeder"))
		{	v = new Icosaeder(1);
			geefBasiskleur(v,"groen");
		}
		if(veelvlakNaam.equals("dodecaeder"))
		{	v = (new Icosaeder(1.2)).dualiseer();
			geefBasiskleur(v,"geel");
		}
		p = new Polygon[v.aantalVlakken];
	}
	public void tekenprogramma()
	{	begindraai(20,30);
		tekenVeelvlak(0,v);
		
	}
	void begindraai(double xdr,double ydr)
	{	if(begin)
		{	matrot.initialiseer();
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
	{	if(raak)
		{	xhoek=-0.5*geefSleepdy();
			yhoek=0.5*geefSleepdx();
			matrot.initialiseer();
			matrot.ydraaiAbs(yhoek);
			matrot.xdraaiAbs(xhoek);
			tb.mat.mult(matrot);
			tekenOpnieuw();
		}
	}
}