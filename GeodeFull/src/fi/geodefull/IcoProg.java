package fi.geodefull;

import java.awt.*;
import java.awt.event.*;
import fi.beans.grnuminput.*;


public class IcoProg extends TekenApplet3D implements NumberListener
{	
	private double xhoek,yhoek,hoek,hx,hy,z,y,maxHoek;
	private double[] h;
	private Polygon[] v;
	private boolean[] bv;
	NumberSlider oprolSl;
	private int aantalVlakken;
	private int aantalRandpunten;
	Punt3D[] randpunten;
	private boolean plat;

	public void initialiseer()
	{	maakMuisActieMogelijk();
		maxHoek = 41.81;
		oprolSl = new NumberSlider(0,maxHoek,0,3,"","");
		oprolSl.addNumberListener(this);
		oprolSl.setValue(0);
		rg.gridbag.setConstraints(oprolSl, rg.c);
		rg.add(oprolSl);
		oprolSl.setBackground(Color.white);
		rg.setBackground(Color.white);
		
		aantalRandpunten = 12;
		randpunten = new Punt3D[aantalRandpunten];
		randpunten[0] = new Punt3D(0,0,0);
		randpunten[1] = new Punt3D(5.0/6,0,Math.sqrt(3)/6);
		randpunten[2] = new Punt3D(1,0,0);
		randpunten[3] = new Punt3D(1.0/3,0,Math.sqrt(3)/3);
		randpunten[4] = new Punt3D(1.0/2,0,Math.sqrt(3)/2);
		randpunten[5] = new Punt3D(1.0/3,0,0);
		randpunten[6] = new Punt3D(1.0/2,0,0);
		randpunten[7] = new Punt3D(1.0/6,0,Math.sqrt(3)/6);
		randpunten[8] = new Punt3D(3.0/4,0,Math.sqrt(3)/4);
		randpunten[9] = new Punt3D(2.0/3,0,0);
		randpunten[10] = new Punt3D(1.0/4,0,Math.sqrt(3)/4);
		randpunten[11] = new Punt3D(2.0/3,0,Math.sqrt(3)/3);
		
		plat = true;
		aantalVlakken=20;
		v = new Polygon[aantalVlakken+1];
		bv = new boolean[aantalVlakken+1];
		h = new double[aantalVlakken+1];
		xhoek = 0;
		yhoek = 0;
		hx = -90;
		hy = 30;
		z = 180;
		y = 0;
		
		for(int i=0 ; i<aantalVlakken+1 ; i++) 
		{	h[i] = 0;
		}
	}
	
	public void tekenprogramma()
	{	tb.mat.initialiseer();
		penUit();stap(-45,-20,0);
		xdraai(hx+xhoek);
		ydraai(hy+yhoek);
		penUit(); stap(-0.29*z,y,-0.5*z);
			
							driehoek(z);					ydraai(-60);
			zdraai(h[2]);	driehoek(z); v[2] = geefVlak();	ydraai(-60);
			zdraai(h[3]);	driehoek(z); v[3] = geefVlak();	
			zdraai(-h[3]);									stapz(z);ydraai(120);
			zdraai(h[4]);	driehoek(z); v[4] = geefVlak();	stapz(z);ydraai(60);stapz(-z);	
			zdraai(h[5]);	driehoek(z); v[5] = geefVlak();	
			zdraai(-h[5]);									ydraai(-120);stapz(-z);
			zdraai(h[6]);	driehoek(z); v[6] = geefVlak();	ydraai(-60);
		
			zdraai(h[7]);	driehoek(z); v[7] = geefVlak();
			zdraai(-h[7]);									stapz(z);ydraai(120);
			zdraai(h[8]);	driehoek(z); v[8] = geefVlak();	stapz(z);ydraai(60);stapz(-z);
			zdraai(h[9]);	driehoek(z); v[9] = geefVlak();
			zdraai(-h[9]);									ydraai(-120);stapz(-z);
			zdraai(h[10]);	driehoek(z); v[10] = geefVlak();ydraai(-60);
			zdraai(h[11]);	driehoek(z); v[11] = geefVlak();
			zdraai(-h[11]);									ydraai(60);
			zdraai(-h[10]);									ydraai(60);
			zdraai(-h[8]);									ydraai(60);stapz(z);ydraai(-120);
			zdraai(-h[6]);									ydraai(60);
			zdraai(-h[4]);									ydraai(60);stapz(z);ydraai(-120);
			zdraai(-h[2]);									stapz(z);ydraai(120);
			zdraai(h[12]);	driehoek(z); v[12] = geefVlak();	
			zdraai(-h[12]);									stapz(z);ydraai(120);
			zdraai(h[13]);	driehoek(z); v[13] = geefVlak();stapz(z);ydraai(60);stapz(-z);	
			zdraai(h[14]);	driehoek(z); v[14] = geefVlak();	
			zdraai(-h[14]);									ydraai(-120);stapz(-z);
			zdraai(h[15]);	driehoek(z); v[15] = geefVlak();	ydraai(-60);
		
			zdraai(h[16]);	driehoek(z); v[16] = geefVlak();
			zdraai(-h[16]);									stapz(z);ydraai(120);
			zdraai(h[17]);	driehoek(z); v[17] = geefVlak();	stapz(z);ydraai(60);stapz(-z);
			zdraai(h[18]);	driehoek(z); v[18] = geefVlak();
			zdraai(-h[18]);									ydraai(-120);stapz(-z);
			zdraai(h[19]);	driehoek(z); v[19] = geefVlak();ydraai(-60);
			zdraai(h[20]);	driehoek(z); v[20] = geefVlak();
		
	}
	
	void tekenRandlijnen()
	{	for(int i=0 ; i<aantalRandpunten ; i=i+2)
		{	penAan(1,"zwart");vulAan(1,"zwart");
			stap(z*(randpunten[i+1].x-randpunten[i].x),0,z*(randpunten[i+1].z-randpunten[i].z));
			if(i!=4 &&i!=8)stap(-0.0001,0,0);else stap(0.0001,0,0);
			stap(z*(randpunten[i].x-randpunten[i+1].x),0,z*(randpunten[i].z-randpunten[i+1].z));
			if(i!=4 &&i!=8)stap(0.0001,0,0);else stap(-0.0001,0,0);
			vulUit(1);penUit(1);
			
			stap(z*(randpunten[(i+2)%aantalRandpunten].x-randpunten[i].x),0,z*(randpunten[(i+2)%aantalRandpunten].z-randpunten[i].z));
		}
	}
		
	
	void driehoek(double zijde)
	{	if(plat)penAan(128,64,0); else penUit();
		vulAan("grijs");
		for(int i=0 ; i<3 ; i++)
		{	stapz(z);
			ydraai(-120);
		}
		vulUit();
		ydraai(-60);
		vulAan("oranje");
		for(int i=0 ; i<3 ; i++)
		{	stapz(z);
			ydraai(120);
		}
		vulUit();
		ydraai(60);
		
		ydraai(30);
		tekenRandlijnen();
		ydraai(-30);
	}
	
	public void muisDrukActie()
	{	for(int i=2 ; i<aantalVlakken+1 ; i++) bv[i] = v[i].contains(geefDrukx(),geefDruky());
	}

	private double begrens(double d, double max)
	{	if(d<-max)d = -max;if(d>max)d = max;
		return d;
	}	
	
	public void muisSleepActie()
	{	for(int i=2 ; i<aantalVlakken+1 ; i++)
		{	if (bv[i])
			{	h[i] = begrens(h[i] - geefSleepdx(),maxHoek);
				tekenOpnieuw();
				return;
			}
		}
		xhoek-= geefSleepdy();
		yhoek += geefSleepdx();
		tekenOpnieuw();
	}
	
	public void numberChanged(String name,double val)
	{	y=-3.2*val;
		if(val==0)plat=true;else plat = false;
		for(int i=1 ; i<aantalVlakken+1 ; i++) 
		{	h[i] = val;
		}
		tekenOpnieuw();
	}
}