package fi.geodefull;

import java.awt.*;
import java.awt.event.*;
import fi.beans.grnuminput.*;

public class KubKnotProg extends TekenApplet3D implements ActionListener, NumberListener
{	
	Matrix3D matrot, matres,mateenh;
	double k, xhoek,yhoek;
	KubusKnot kubk;
	OctaederKnot octk;
	Kubus kub;
	Kuboctaeder kubo;
	Veelvlak v1,v2;
	Button draadKnop;
	NumberSlider afknotSl;
	int aantalKlikken = 0;
	boolean begin;
	boolean draad=false;
	boolean kleurveranderd=false;
	String kleur;
	
	public void initialiseer()
	{	maakMuisActieMogelijk();
		maakAnimatieMogelijk();
		
		afknotSl = new NumberSlider(0,100,0,0,"afknotting","");
		afknotSl.addNumberListener(this);
		afknotSl.setValue(0);
		rg.gridbag.setConstraints(afknotSl, rg.c);
		rg.add(afknotSl);
		rg.setBackground(Color.white);
		ab.setBackground(Color.white);
		
		draadKnop = new Button("draadfiguur");
		draadKnop.addActionListener(this);
		rg.gridbag.setConstraints(draadKnop, rg.c);
		rg.add(draadKnop);
		
		matrot = new Matrix3D();
		matres = new Matrix3D();
		mateenh = new Matrix3D();
		k=130;
		//kleur = "oranje";
		begin=true;
		kub = new Kubus(0.8);
		kubk = new KubusKnot(0.8, 0.001);
		kubo = new Kuboctaeder(0.8);
		octk = new OctaederKnot(0.8,0.5);
		v1=kub;
		v2=kub.dualiseerb();
		kleurVeelvlak(v2,"groen");
		v2.schaal(0.22);
		
	}
	public void tekenprogramma()
	{	begindraai(20,30);
		tb.mat = mateenh;
		if(draad)
		{	kleurVeelvlak(v1,"transparant");kleurVeelvlak(v2,"transparant");
		}
		penUit();
		//vulAan("wit");
		//stapx(-5);stapy(250);stapx(10);stapy(-500);stapx(-10);stapy(250);stapx(5);
		//vulUit();
		stapx(-100);
		tb.mat = matres;
		tekenVeelvlak(4,v1);
		tb.mat = mateenh;
		stapx(200);
		tb.mat = matres;
		tekenVeelvlak(4,v2);
		tb.mat = mateenh;
	}
	void begindraai(double xdr,double ydr)
	{	if(begin)
		{	matrot.initialiseer();
			matrot.ydraaiAbs(ydr);
			matrot.xdraaiAbs(xdr);
			matres.mult(matrot);
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
		for(int i=0 ; i<v2.aantalVlakken ; i++)
		{	v2.vlakken[i].vulkleur = v2.vlakken[i].vorigeKleur;
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
	public void actionPerformed(ActionEvent e)
	{	boolean animatieWasAan=false;
		if(e.getSource()==draadKnop)
		{	if(draadKnop.getLabel()=="draadfiguur")
			{	draad=true;	
				//kleurveranderd=true;
				//kleur = "transparant";
				draadKnop.setLabel("gevuld");
			}
			else 
			{	draad=false;
				naarVorigeKleur();
				//kleurveranderd=true;
				//kleur = "oranje";
				draadKnop.setLabel("draadfiguur");
			}
		}
		
		if(animatieStatus())
		{	onderbreekAnimatie();
			animatieWasAan = true;
		}
		tekenOpnieuw();
		if(animatieWasAan)beginAnimatie();
	}
	public void numberChanged(String name,double val)
	{	boolean animatieWasAan = false;
		if(animatieStatus())
		{	animatieWasAan = true;
			onderbreekAnimatie();
		}
		if(name=="afknotting")
		{	if(val==0)
			{	v1=new Kubus(0.8);
				v2=v1.dualiseerb();
				geefBasiskleur(v2,"groen");
				v2.schaal(0.22);
				
			}
			else if(val<47)
			{	v1=new KubusKnot(0.8,0.02*val);
				v2 = v1.dualiseerb();
				kleurVeelvlak(v2,"groen");
				v2.schaal(0.22);
			}
			else if(val<53)
			{	v1=new Kuboctaeder(0.8);
				v2=v1.dualiseerb();
				geefBasiskleur(v2,"cyaan");
				v2.schaal(0.22);
			}
			else if(val<100)
			{	v1=new OctaederKnot(0.8,0.02*(val-50));
				v2 = v1.dualiseerb();
				geefBasiskleur(v2,"geel");
				v2.schaal(0.22);
			}	
			else if(val==100)
			{	v1=kub.dualiseerb();
				v2 = v1.dualiseerb();
				kleurVeelvlak(v1,"groen");
				geefBasiskleur(v2,"geel");
				v2.schaal(0.22);
			}	
			
		}
		if(name=="zijde")
		{	k = val;
		}
		if(!animatieStatus())tekenOpnieuw();
		if(animatieWasAan)beginAnimatie();
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



