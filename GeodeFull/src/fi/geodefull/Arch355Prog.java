package fi.geodefull;

import java.awt.event.*;
import java.awt.*;
import fi.beans.grnuminput.*;

public class Arch355Prog extends TekenApplet3D 
{	
	Matrix3D matrot;
	double k, xhoek,yhoek;
	Veelvlak v1,v2;
	boolean begin;
	
	public void initialiseer()
	{	maakMuisActieMogelijk();
				
		begin = true;
		k=250;
		matrot = new Matrix3D();
		v1 = new DodecaederKnot(1.0,0.44);
		
		
	}
	public void tekenprogramma()
	{	begindraai(20,0);
		tekenVeelvlak(3,v1);
		
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
	{	for(int i=0 ; i<v1.aantalVlakken ; i++)
		{	v1.vlakken[i].vulkleur = v1.vlakken[i].vorigeKleur;
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

