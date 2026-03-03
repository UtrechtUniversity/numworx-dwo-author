package fi.geodefull;

import java.awt.*;
import java.awt.event.*;

public class Vijfvlak1Prog extends TekenApplet3D
{	
	Matrix3D matrot;
	double k, xhoek,yhoek;
	Veelvlak v, tv;
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
		
		v = new Tetraeder(1);
			
		int n = 2;
		int aantalHp = 4;
		int aantalRib = 6;
		
		Hoekpunt[] hp = new Hoekpunt[v.aantalHoekpunten+n*aantalRib];
		for(int i=0 ; i<v.aantalHoekpunten ; i++)
		{	hp[i]=v.hoekpunten[i];
		}
		v.hoekpunten = hp;
		v.aantalHoekpunten+=n*aantalRib;
		
		int[] rib = {0,1,1,2,2,3,3,0,1,3,2,0};
		for(int i=0 ; i<aantalRib ; i++)
		{	for(int j=0 ; j<n ; j++)
			{	v.hoekpunten[aantalHp+n*i+j] = new Hoekpunt(((n-j)*v.hoekpunten[rib[2*i]].x + (j+1)*v.hoekpunten[rib[2*i+1]].x)/(n+1),  
														((n-j)*v.hoekpunten[rib[2*i]].y + (j+1)*v.hoekpunten[rib[2*i+1]].y)/(n+1),
														((n-j)*v.hoekpunten[rib[2*i]].z + (j+1)*v.hoekpunten[rib[2*i+1]].z)/(n+1));
			}
		}
											
		for(int i=0 ; i<v.aantalVlakken ; i++)
		{	v.vlakken[i].vulkleur = "transparant";
		}
		tv = new Veelvlak(v);
		
		Hoekpunt[] hp1 = {tv.hoekpunten[0],tv.hoekpunten[1],tv.hoekpunten[2]};
		tv.vlakken[0]=new Vlak(3,hp1);
		Hoekpunt[] hp2 = {tv.hoekpunten[1],tv.hoekpunten[13],tv.hoekpunten[9],tv.hoekpunten[2]};
		tv.vlakken[1]=new Vlak(4,hp2);
		Hoekpunt[] hp3 = {tv.hoekpunten[2],tv.hoekpunten[9],tv.hoekpunten[10],tv.hoekpunten[0]};
		tv.vlakken[2]=new Vlak(4,hp3);
		Hoekpunt[] hp4 = {tv.hoekpunten[0],tv.hoekpunten[10],tv.hoekpunten[13],tv.hoekpunten[1]};
		tv.vlakken[3]=new Vlak(4,hp4);
		Hoekpunt[] hp5 = {tv.hoekpunten[9],tv.hoekpunten[13],tv.hoekpunten[10]};
		tv.vlakken[4]=new Vlak(3,hp5);/**/
		tv.aantalVlakken=5;
		//p = new Polygon[v.aantalVlakken];
	}
	public void tekenprogramma()
	{	begindraai(-110,42);
		tekenVeelvlak(1,v);
		tekenVeelvlak(0,tv);
		
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
			//p[i] = geefVlak();
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
	
	/*public void muisDrukActie()
	{	for(int i=0 ; i<v.aantalVlakken ; i++)
		{	if(p[i].contains(geefDrukx(),geefDruky()))
			{	raak=true;
				return;
			}
		}
		raak = false;
	}*/
	public void muisSleepActie()
	{	//if(raak)
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