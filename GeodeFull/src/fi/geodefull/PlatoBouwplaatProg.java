package fi.geodefull;

import java.awt.*;
import java.awt.event.*;
import fi.beans.grnuminput.*;


public class PlatoBouwplaatProg extends TekenApplet3D implements NumberListener,ItemListener
{	
	private double xhoek,yhoek,hoek,hx,hy,z,y,maxHoek,coef;
	private boolean kubus,octaeder,tetraeder,icosaeder,dodecaeder;
	private double[] h;
	private Polygon[] v;
	private boolean[] bv;
	NumberSlider oprolSl;
	Choice kiesV;
	private int aantalVlakken;

	public void initialiseer()
	{	maakMuisActieMogelijk();
		maxHoek = 109.471;
		coef = 0.77;
			
		kubus = false;
		octaeder = false;
		tetraeder = true;
		icosaeder = false;
		dodecaeder = false;
		
		kiesV = new Choice();
		kiesV.addItemListener(this);
		kiesV.addItem("Tetraeder");
		kiesV.addItem("Kubus");
		kiesV.addItem("Octaeder");
		kiesV.addItem("Dodecaeder");
		kiesV.addItem("Icosaeder");
		rg.gridbag.setConstraints(kiesV, rg.c);
		rg.add(kiesV);
		
		oprolSl = new NumberSlider(0,100,0,0,"","");
		oprolSl.addNumberListener(this);
		oprolSl.setValue(0);
		oprolSl.setColumns(0);
		rg.gridbag.setConstraints(oprolSl, rg.c);
		rg.add(oprolSl);
		oprolSl.setBackground(Color.white);
		rg.setBackground(Color.white);
		
		
		
		aantalVlakken=20;
		v = new Polygon[aantalVlakken+1];
		bv = new boolean[aantalVlakken+1];
		h = new double[aantalVlakken+1];
		xhoek = 0;
		yhoek = 0;
		hx = 90;
		hy = 30;
		z = 200;
		y = 0;
		
		for(int i=0 ; i<aantalVlakken+1 ; i++) 
		{	h[i] = 0;
		}
	}
	
	public void tekenprogramma()
	{	tb.mat.initialiseer();
		xdraai(hx+xhoek);
		ydraai(hy+yhoek);
		if(icosaeder)
		{	penUit(); stap(-0.29*z,y,-0.5*z);
			
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
		else if(octaeder)
		{	penUit(); stap(-0.29*z,y,-0.5*z);
		
							driehoek(z);					ydraai(-60);
			zdraai(h[2]);	driehoek(z); v[2] = geefVlak();	stapz(z);ydraai(60);stapz(-z);	
			zdraai(h[3]);	driehoek(z); v[3] = geefVlak();	stapz(z);ydraai(60);stapz(-z);
			zdraai(h[4]);	driehoek(z); v[4] = geefVlak();	
			zdraai(-h[4]);									stapz(z);ydraai(-60);stapz(-z);
			zdraai(-h[3]);									stapz(z);ydraai(-60);stapz(-z);
			zdraai(-h[2]);									ydraai(-120);stapz(-z);
			zdraai(h[5]);	driehoek(z); v[5] = geefVlak();	ydraai(-60);
			zdraai(h[6]);	driehoek(z); v[6] = geefVlak();	stapz(z);ydraai(60);stapz(-z);
			zdraai(h[7]);	driehoek(z); v[7] = geefVlak();	stapz(z);ydraai(60);stapz(-z);
			zdraai(h[8]);	driehoek(z); v[8] = geefVlak();	
		}
		else if(dodecaeder)
		{	penUit(); stap(-0.7*z,y,-0.5*z);
		
							vijfhoek(z);					ydraai(-108);
			zdraai(h[2]);	vijfhoek(z); v[2] = geefVlak();	ydraai(-108);stapz(z);ydraai(72);
			zdraai(h[7]);	vijfhoek(z); v[7] = geefVlak();
			zdraai(-h[7]);									ydraai(108);stapz(z);ydraai(-72);
			zdraai(-h[2]);									stapz(z);ydraai(72);
			zdraai(h[3]);	vijfhoek(z); v[3] = geefVlak();	ydraai(-108);stapz(z);ydraai(72);
			zdraai(h[8]);	vijfhoek(z); v[8] = geefVlak();
			zdraai(-h[8]);									ydraai(108);stapz(z);ydraai(-72);
			zdraai(-h[3]);									stapz(z);ydraai(72);
			zdraai(h[4]);	vijfhoek(z); v[4] = geefVlak();	ydraai(-108);stapz(z);ydraai(72);
			zdraai(h[9]);	vijfhoek(z); v[9] = geefVlak();
			zdraai(-h[9]);									ydraai(108);stapz(z);ydraai(-72);
			zdraai(-h[4]);									stapz(z);ydraai(72);
			zdraai(h[5]);	vijfhoek(z); v[5] = geefVlak();	ydraai(-108);stapz(z);ydraai(72);
			zdraai(h[10]);	vijfhoek(z); v[10] = geefVlak();
			zdraai(-h[10]);									ydraai(108);stapz(z);ydraai(-72);
			zdraai(-h[5]);									stapz(z);ydraai(72);
			zdraai(h[6]);	vijfhoek(z); v[6] = geefVlak();	ydraai(-108);stapz(z);ydraai(72);
			zdraai(h[11]);	vijfhoek(z); v[11] = geefVlak();ydraai(-108);stapz(z);ydraai(72);
															stapz(z);ydraai(72);
			zdraai(h[12]);	vijfhoek(z); v[12] = geefVlak();
		}
		else if(tetraeder)
		{	penUit(); stap(-0.29*z,y,-0.5*z);
			
							driehoek(z);					ydraai(-60);
			zdraai(h[2]);	driehoek(z); v[2] = geefVlak();	
			zdraai(-h[2]);									stapz(z);ydraai(120);
			zdraai(h[3]);	driehoek(z); v[3] = geefVlak();	
			zdraai(-h[3]);									stapz(z);ydraai(120);	
			zdraai(h[4]);	driehoek(z); v[4] = geefVlak();	
			zdraai(-h[4]);
		}
		else if(kubus)
		{	penUit(); stap(-0.5*z,y,-0.5*z);
			
							vierkant(z);					ydraai(-90);
			zdraai(h[2]);	vierkant(z); v[2] = geefVlak();
			zdraai(-h[2]);									ydraai(90);stapz(z);ydraai(180);
			zdraai(h[3]);	vierkant(z); v[3] = geefVlak();	
			zdraai(-h[3]);									ydraai(90);stapz(z);ydraai(180);
			zdraai(h[4]);	vierkant(z); v[4] = geefVlak();	
			zdraai(-h[4]);									ydraai(90);stapz(z);ydraai(180);
			zdraai(h[5]);	vierkant(z); v[5] = geefVlak();	ydraai(-90);stapz(z);ydraai(90);
			zdraai(h[6]);	vierkant(z); v[6] = geefVlak();									
		}
		
	}
	void driehoek(double zijde)
	{	penAan();
		vulAan("grijs");
		for(int i=0 ; i<3 ; i++)
		{	stapz(z);
			ydraai(-120);
		}
		vulUit();
		ydraai(-60);
		vulAan("geel");
		for(int i=0 ; i<3 ; i++)
		{	stapz(z);
			ydraai(120);
		}
		vulUit();
		ydraai(60);
	}
	void vierkant(double zijde)
	{	penAan();
		vulAan("grijs");
		for(int i=0 ; i<4 ; i++)
		{	stapz(z);
			ydraai(-90);
		}
		vulUit();
		ydraai(-90);
		vulAan("geel");
		for(int i=0 ; i<4 ; i++)
		{	stapz(z);
			ydraai(90);
		}
		vulUit();
		ydraai(90);
	}
	void vijfhoek(double zijde)
	{	penAan();
		vulAan("grijs");
		for(int i=0 ; i<5 ; i++)
		{	stapz(z);
			ydraai(-72);
		}
		vulUit();
		ydraai(-108);
		vulAan("geel");
		for(int i=0 ; i<5 ; i++)
		{	stapz(z);
			ydraai(72);
		}
		vulUit();
		ydraai(108);
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
	
	public void itemStateChanged(ItemEvent e)
	{	String soortV = kiesV.getSelectedItem();
		kubus = false;
		octaeder = false;
		tetraeder = false;	
		icosaeder = false;
		dodecaeder = false;	
		xhoek = 0;
		yhoek = 0;
		hx = 90;
		hy = 30;
		y = 0;
		oprolSl.setInitValue();
		
		for(int i=0 ; i<aantalVlakken+1 ; i++) 
		{	h[i] = 0;
		}
		if (soortV=="Kubus")
		{	kubus=true ;
			maxHoek = 90;
			hy = 0;
			coef = 0.55;
			aantalVlakken = 6;
			z = 110;
		}
		else if (soortV=="Octaeder")
		{	octaeder=true;
			maxHoek = 70.529;
			coef = 0.55;
			aantalVlakken = 8;
			z = 140;
		}
		else if (soortV=="Icosaeder")
		{	icosaeder=true;
			maxHoek = 41.81;
			coef = 0.7;
			aantalVlakken = 20;
			z = 90;
		}
		else if (soortV=="Dodecaeder")
		{	dodecaeder=true;
			maxHoek = 63.435;
			coef = 0.8;
			aantalVlakken = 12;
			z = 70;
			hy = 45;
		}
		else if (soortV=="Tetraeder")
		{	tetraeder=true;
			maxHoek = 109.471;
			coef = 0.77;
			aantalVlakken = 4;
			z = 200;
			hy = 30;
		}
		
		
		
		tekenOpnieuw();
		
	}
	public void numberChanged(String name,double val)
	{	y=-coef*val;
		for(int i=1 ; i<aantalVlakken+1 ; i++) 
		{	h[i] = val*maxHoek/100;
		}
		tekenOpnieuw();
	}
}