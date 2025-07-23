package fi.geodefull;

import java.awt.*;
import java.awt.event.*;
import fi.beans.grnuminput.*;

public class PlatoDualProg extends TekenApplet3D implements ActionListener,ItemListener
{	
	Matrix3D matrot;
	double k, xhoek,yhoek;
	Icosaeder ico;
	Veelvlak[] v, vKub, vOct, vIco,vDod,vTet;
	boolean begin;//,octZichtbaar,kubZichtbaar ;
	Button dualiseerKnop, terugKnop;
	Choice kiesV;
	int aantalKlikken = 0;
	
	public void initialiseer()
	{	maakMuisActieMogelijk();
		maakAnimatieMogelijk();
		
		kiesV = new Choice();
		kiesV.addItemListener(this);
		kiesV.addItem("Kubus");
		kiesV.addItem("Octaeder");
		kiesV.addItem("Icosaeder");
		kiesV.addItem("Dodecaeder");
		kiesV.addItem("Tetraeder");
		rg.gridbag.setConstraints(kiesV, rg.c);
		rg.add(kiesV);
		rg.setBackground(Color.white);
		ab.setBackground(Color.white);
		
		dualiseerKnop = new Button("dualiseer");
		dualiseerKnop.addActionListener(this);
		rg.gridbag.setConstraints(dualiseerKnop, rg.c);
		rg.add(dualiseerKnop);
		
		terugKnop = new Button("terug");
		terugKnop.addActionListener(this);
		rg.gridbag.setConstraints(terugKnop, rg.c);
		rg.add(terugKnop);

		matrot = new Matrix3D();
		k=200;
		begin=true;
		v = new Veelvlak[5];
		vKub = new Veelvlak[5];
		vOct = new Veelvlak[5];
		vIco = new Veelvlak[5];
		vDod = new Veelvlak[5];
		vTet = new Veelvlak[5];
		vKub[0] = new Kubus(1);
		vOct[0] = (new Kubus(Math.sqrt(3))).dualiseer();
		vIco[0] = new Icosaeder(1);
		vDod[0] = (new Icosaeder(1.3)).dualiseer();
		vTet[0] = new Tetraeder(1);
		for(int i=1 ; i<5 ; i++)
		{	vKub[i]=vKub[i-1].dualiseer();
			vOct[i]=vOct[i-1].dualiseer();
			vIco[i]=vIco[i-1].dualiseer();
			vDod[i]=vDod[i-1].dualiseer();
			vTet[i]=vTet[i-1].dualiseer();
		}
		v = vKub;
		for(int i=0 ; i<v[0].aantalVlakken ; i++)
			{	v[0].vlakken[i].vulkleur = "oranje";
			}
	}
	public void tekenprogramma()
	{	begindraai(20,30);
		for(int i=0 ; i<aantalKlikken+1 ; i++)
		{tekenVeelvlak(4-i , v[i]);
		}
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
	public void itemStateChanged(ItemEvent e)
	{	boolean animatieWasAan=false;
		
		String soortV = kiesV.getSelectedItem();
		for(int j=aantalKlikken ; j>0 ; j--)
		{	for(int i=0 ; i<v[j-1].aantalVlakken ; i++)
			{	v[j-1].vlakken[i].vulkleur = "oranje";
			}
			aantalKlikken--;
		}
		if (soortV=="Kubus")v = vKub;
		else if (soortV=="Octaeder")v = vOct;
		else if (soortV=="Icosaeder")v = vIco;
		else if (soortV=="Dodecaeder")v = vDod;
		else if (soortV=="Tetraeder")v = vTet;
		
		if(animatieStatus())
		{	onderbreekAnimatie();
			animatieWasAan = true;
		}
		tekenOpnieuw();
		if(animatieWasAan)beginAnimatie();
	}
	public void actionPerformed(ActionEvent e)
	{	
		boolean animatieWasAan=false;
		
		if(e.getSource()==dualiseerKnop)
		{	for(int j=0 ; j<4 ; j++)
			{	if(aantalKlikken==j)
				{	for(int i=0 ; i<v[j].aantalVlakken ; i++)
					{	v[j].vlakken[i].vulkleur = "transparant";
					}
				}
			}
			if(aantalKlikken<4)aantalKlikken++;
		}	
		if(e.getSource()==terugKnop)
		{	for(int j=1 ; j<5 ; j++)
			{	if(aantalKlikken==j)
				{	for(int i=0 ; i<v[j-1].aantalVlakken ; i++)
					{	v[j-1].vlakken[i].vulkleur = "oranje";
					}
				}
			}
			if(aantalKlikken>0)aantalKlikken--;
		}
		if(animatieStatus())
		{	onderbreekAnimatie();
			animatieWasAan = true;
		}
		tekenOpnieuw();
		if(animatieWasAan)beginAnimatie();
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
	public void animatie()
	{	while(animatieStatus())
		{	matrot.initialiseer();
			matrot.ydraaiAbs(1);
			matrot.xdraaiAbs(0);
			tb.mat.mult(matrot);
			tekenOpnieuw();
		}
	}
}
