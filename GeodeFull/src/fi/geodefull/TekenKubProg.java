package fi.geodefull;

import java.awt.*;
import java.awt.event.*;
import fi.beans.grnuminput.*;

public class TekenKubProg extends TekenApplet3D
{	
	Matrix3D matrot, matres,mateenh;
	double k, xhoek,yhoek;
	
	
	
	Veelvlak v, tv;
	Punt[] trefpunten;
	boolean[] trefpuntRaak;
	
	
	boolean begin, basisZichtbaar,maakLijn, maakVlak;
	
	
	int aantalPuntenRood, puntnr1, puntnr2;
	int[] puntnr;	
	
	public void initialiseer()
	{	maakMuisActieMogelijk();
		
		
		matrot = new Matrix3D();
		matres = new Matrix3D();
		mateenh = new Matrix3D();
		tb.mat = matres;
		k=200;
		begin=true;
		basisZichtbaar=true;
		maakLijn=true;
		maakVlak=false;
		trefpunten = new Punt[500];
		trefpuntRaak = new boolean[500];
		wisTrefpunten();
		aantalPuntenRood = 0;
		puntnr = new int[10];
		
		v = new Kubus(1);
		
		/*int n = 2;
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
		*/									
		for(int i=0 ; i<v.aantalVlakken ; i++)
		{	v.vlakken[i].vulkleur = "transparant";
		}
		tv = new Veelvlak(v);
	}
	public void tekenprogramma()
	{	
		begindraai(20,30);
		if(basisZichtbaar)tekenVeelvlak(2,v);
		
		tekenVeelvlak(1,tv);
		maakTrefpunten(v);
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
	void maakTrefpunten(Veelvlak v)
	{	for(int i=0 ; i<v.aantalHoekpunten ; i++)
		{	penUit();stap(k*v.hoekpunten[i].x, k*v.hoekpunten[i].y, k*v.hoekpunten[i].z);
			trefpunten[i] = geefPunt(1);
			if(trefpuntRaak[i])
			{	tb.mat = mateenh;
				stap(5,0);vulAan(1,"groen");stap(-5,-5);stap(-5,5);stap(5,5);stap(5,-5);vulUit(1);stap(-5,0);
				tb.mat = matres;
			}
			else if(basisZichtbaar)
			{	tb.mat = mateenh;
				stap(2,0);vulAan(1,"zwart");stap(-2,-2);stap(-2,2);stap(2,2);stap(2,-2);vulUit(1);stap(-2,0);
				tb.mat = matres;
			}
			stap(-k*v.hoekpunten[i].x, -k*v.hoekpunten[i].y, -k*v.hoekpunten[i].z);penAan();
		}
	}
	public void wisTrefpunten()
	{	for(int i=0 ; i<100 ; i++)
		{	trefpuntRaak[i]=false;
		}
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
		if(!(v.lijnkleur=="transparant"))penAan("lichtgrijs");
		if(v.vulkleur=="transparant")
		{	if(n==2)vulAan(v.vulkleur);
			else if(n==1)vulAan(1,v.vulkleur);
		}
		else 
		{	if(n==2)vulAan("grijs");
			else if(n==1)vulAan(1,"grijs");
		}
		for(int i=v.aantalHoekpunten-1 ; i>-1 ; i--)
		{	int a=i ; int b=(i+1)%v.aantalHoekpunten;
			stap(k*(v.punten[a].x-v.punten[b].x), k*(v.punten[a].y-v.punten[b].y), k*(v.punten[a].z-v.punten[b].z));
		}
		if(n==2)vulUit();
		else if(n==1)vulUit(1);
		
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
	
	

	public void muisDrukActie()
	{	boolean raak = false;
		int max = tv.aantalHoekpunten;
		for(int i=0 ; i<max ; i++)
		{	double ax = trefpunten[i].x - geefDrukx();
			double ay =  trefpunten[i].y - geefDruky();
			if((ax<4 && ax>-4)&&(ay<4 && ay>-4))
			{	raak = true;
				if(maakLijn)
				{	if(aantalPuntenRood==0)
					{	puntnr1 = i;
						aantalPuntenRood++;
						trefpuntRaak[i]=true;
					}
					else 
					{	puntnr2 = i;
						if(puntnr1!=puntnr2)tv.maakLijn(puntnr1,puntnr2,"rood");
						aantalPuntenRood=0;
						wisTrefpunten();
						trefpuntRaak[puntnr1]=false;
						//if(tv.aantalLijnen>1)zoekSnijpunten();
					}
				}
				else if(maakVlak)
				{	if(aantalPuntenRood>0 && i==puntnr[aantalPuntenRood-1])
					{	wisTrefpunten();
						aantalPuntenRood=0;
					}
					else if(aantalPuntenRood>1 && i==puntnr[aantalPuntenRood-2])
					{	wisTrefpunten();
						aantalPuntenRood=0;
					}
					else if(i!=puntnr[0] || aantalPuntenRood==0)
					{	puntnr[aantalPuntenRood] = i;
						aantalPuntenRood++;
						trefpuntRaak[i]=true;
					}
					else
					{	puntnr[aantalPuntenRood] = i; 
						Vlak vl = new Vlak(aantalPuntenRood);
						for(int j=0 ; j<aantalPuntenRood ; j++)
						{	vl.punten[j] = tv.hoekpunten[puntnr[j]];
						}
						tv.vlakken[tv.aantalVlakken] = vl;
						tv.aantalVlakken++;
						aantalPuntenRood=0;
						wisTrefpunten();
					}
				}
				break;
			}
		}
		if(raak)
		{	tekenOpnieuw();
			return;
		}
		
	}
	public void muisSleepActie()
	{	
		xhoek=-0.5*geefSleepdy();
		yhoek=0.5*geefSleepdx();
		matrot.initialiseer();
		matrot.ydraaiAbs(yhoek);
		matrot.xdraaiAbs(xhoek);
		matres.mult(matrot);
		tekenOpnieuw();
	}
	public void animatie()
	{	while(animatieStatus())
		{	matrot.initialiseer();
			matrot.ydraaiAbs(1);
			matrot.xdraaiAbs(0);
			matres.mult(matrot); 
			tekenOpnieuw();
		}
	}
}