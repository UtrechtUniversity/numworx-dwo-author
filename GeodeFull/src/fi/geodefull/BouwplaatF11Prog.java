package fi.geodefull;

import java.awt.*;
import java.awt.event.*;
import fi.beans.grnuminput.*;

public class BouwplaatF11Prog extends TekenApplet3D implements NumberListener
{	
	private double xhoek,yhoek,hoek,hd,hv,phi,rho,hx,hy,z,maxHoekd, maxHoekv, a,b,c,f,g,j,pi,afkn;
	private InvoerVariabele snelh,zijdeInv,phiInv;
	private int[] hoekvRij,hoekdRij;
	private double[] h, max;
	private Polygon[] v;
	private boolean[] bv;
	double k, l, r, x, y;
	Matrix3D matrot, matbegin, mateenh;
	NumberSlider oprolSl;

	
	public void initialiseer()
	{	oprolSl = new NumberSlider(0,37.375,0,3,"","");
		oprolSl.addNumberListener(this);
		oprolSl.setValue(0);
		rg.gridbag.setConstraints(oprolSl, rg.c);
		rg.add(oprolSl);
		oprolSl.setBackground(Color.white);
		rg.setBackground(Color.white);
		//add(oprolSl,"East");
		maakMuisActieMogelijk();
		hoek = 0;
		k = 30;
		y=0;
		
		maxHoekv = 63.435;
		maxHoekd = 37.375;
		hoekvRij = new int[11];
		hoekdRij = new int[20];
		v = new Polygon[32];
		bv = new boolean[32];
		h = new double[32];
		max = new double[32];
		int hoekvRij[] = {6,8,10,12,14,16,18,20,22,24,26};
		int hoekdRij[] = {1,2,3,4,5,7,9,11,13,15,17,19,21,23,25,27,28,29,30,31};
		max = new double[32];
		for(int i=1 ; i<32 ; i++) 
		{	h[i] = 0;
			max[i] = maxHoekd;
		}
		pi = Math.PI;
		rho = 30;
		xhoek = 0;
		yhoek = 0;
		hd = 10;
		hx = -90;
		hy = 100;
	}
	
	public void tekenprogramma()
	{	tb.mat.initialiseer();
		z = 70;
		afkn = 18;
		//phi = begrens(3.6*(10-afkn),0.1,35.9);
		//a = z*Math.sin((36-phi)*pi/180)/(Math.cos(phi*pi/180)*Math.tan(36*pi/180));
		//b = z*Math.tan(phi*pi/180)/Math.tan(36*pi/180);
		//c = 0.5*(z-b);
		rho = begrens(3.6*(afkn-10),0.1,53.9);
		f = z*Math.cos((36+rho)*pi/180)/Math.cos(rho*pi/180);
		g = z*0.5*(Math.sin((36+rho)*pi/180)/(Math.tan(36*pi/180)*Math.cos(rho*pi/180)) - Math.cos((36+rho)*pi/180)/Math.cos(rho*pi/180));
		j = z*Math.tan(rho*pi/180)/Math.tan(36*pi/180);
		//if(afkn>10)
		b=0.001;
		//if(afkn<10){f=a;g=0.001;j=-b;}
		xdraai(hx+xhoek);
		ydraai(hy+yhoek);
		penUit(); stap(-0.7*z,y,-0.5*z);
		
		//Gefacetteerde Dodecaeder
		stapz(0.5*(z-j));ydraai(-36);stapz(g);
		
						tienhoek();						ydraai(-144);stapz(b);ydraai(36);
		zdraai(h[1]);	zeshoek();v[1]=geefVlak();		ydraai(-120);stapz(g);ydraai(60);
		zdraai(h[2]);	tienhoek();v[2]=geefVlak();		ydraai(-144);stapz(b);ydraai(36);stapz(f);ydraai(36);stapz(b);ydraai(36);
		zdraai(h[3]);	zeshoek();v[3]=geefVlak();
		zdraai(-h[3]);									stapz(f);ydraai(36);stapz(b);ydraai(36);
		zdraai(h[4]);	zeshoek();v[4]=geefVlak();		ydraai(-120);stapz(g);ydraai(60);
		zdraai(h[5]);	tienhoek();v[5]=geefVlak();		stapz(f);ydraai(-36);stapz(b);ydraai(144);stapz(-f);			
		zdraai(h[6]);	zeshoek();v[6]=geefVlak();							
		zdraai(-h[6]);									stapz(f);ydraai(36);stapz(b);ydraai(-144);stapz(-f);
		zdraai(-h[5]);									ydraai(120);stapz(g);ydraai(-60);
		zdraai(-h[4]);									ydraai(144);stapz(b);ydraai(-36);stapz(f);ydraai(-36);stapz(b);ydraai(-36);stapz(f);ydraai(-36);stapz(b);ydraai(-36);
		zdraai(-h[2]);									ydraai(120);stapz(g);ydraai(-60);
		zdraai(-h[1]);									stapz(f);ydraai(36);stapz(b);ydraai(36);

		zdraai(h[7]);	zeshoek();v[7]=geefVlak();		ydraai(-120);stapz(g);ydraai(60);
		zdraai(h[8]);	tienhoek();v[8]=geefVlak();		ydraai(-144);stapz(b);ydraai(36);stapz(f);ydraai(36);stapz(b);ydraai(36);
		zdraai(h[9]);	zeshoek();v[9]=geefVlak();
		zdraai(-h[9]);									stapz(f);ydraai(36);stapz(b);ydraai(36);
		zdraai(h[10]);	zeshoek();v[10]=geefVlak();		ydraai(-120);stapz(g);ydraai(60);
		zdraai(h[11]);	tienhoek();v[11]=geefVlak();	stapz(f);ydraai(-36);stapz(b);ydraai(144);stapz(-f);				
		zdraai(h[12]);	zeshoek();v[12]=geefVlak();							
		zdraai(-h[12]);									stapz(f);ydraai(36);stapz(b);ydraai(-144);stapz(-f);
		zdraai(-h[11]);									ydraai(120);stapz(g);ydraai(-60);
		zdraai(-h[10]);									ydraai(144);stapz(b);ydraai(-36);stapz(f);ydraai(-36);stapz(b);ydraai(-36);stapz(f);ydraai(-36);stapz(b);ydraai(-36);
		zdraai(-h[8]);									ydraai(120);stapz(g);ydraai(-60);
		zdraai(-h[7]);									stapz(f);ydraai(36);stapz(b);ydraai(36);
		
		zdraai(h[13]);	zeshoek();v[13]=geefVlak();		ydraai(-120);stapz(g);ydraai(60);
		zdraai(h[14]);	tienhoek();v[14]=geefVlak();	ydraai(-144);stapz(b);ydraai(36);stapz(f);ydraai(36);stapz(b);ydraai(36);
		zdraai(h[15]);	zeshoek();v[15]=geefVlak();
		zdraai(-h[15]);									stapz(f);ydraai(36);stapz(b);ydraai(36);
		zdraai(h[16]);	zeshoek();v[16]=geefVlak();		ydraai(-120);stapz(g);ydraai(60);
		zdraai(h[17]);	tienhoek();v[17]=geefVlak();	stapz(f);ydraai(-36);stapz(b);ydraai(144);stapz(-f);				
		zdraai(h[18]);	zeshoek();v[18]=geefVlak();							
		zdraai(-h[18]);									stapz(f);ydraai(36);stapz(b);ydraai(-144);stapz(-f);
		zdraai(-h[17]);									ydraai(120);stapz(g);ydraai(-60);
		zdraai(-h[16]);									ydraai(144);stapz(b);ydraai(-36);stapz(f);ydraai(-36);stapz(b);ydraai(-36);stapz(f);ydraai(-36);stapz(b);ydraai(-36);
		zdraai(-h[14]);									ydraai(120);stapz(g);ydraai(-60);
		zdraai(-h[13]);									stapz(f);ydraai(36);stapz(b);ydraai(36);

		zdraai(h[19]);	zeshoek();v[19]=geefVlak();		ydraai(-120);stapz(g);ydraai(60);
		zdraai(h[20]);	tienhoek();v[20]=geefVlak();	ydraai(-144);stapz(b);ydraai(36);stapz(f);ydraai(36);stapz(b);ydraai(36);
		zdraai(h[21]);	zeshoek();v[21]=geefVlak();
		zdraai(-h[21]);									stapz(f);ydraai(36);stapz(b);ydraai(36);
		zdraai(h[22]);	zeshoek();v[22]=geefVlak();		ydraai(-120);stapz(g);ydraai(60);
		zdraai(h[23]);	tienhoek();v[23]=geefVlak();	stapz(f);ydraai(-36);stapz(b);ydraai(144);stapz(-f);					
		zdraai(h[24]);	zeshoek();v[24]=geefVlak();							
		zdraai(-h[24]);									stapz(f);ydraai(36);stapz(b);ydraai(-144);stapz(-f);
		zdraai(-h[23]);									ydraai(120);stapz(g);ydraai(-60);
		zdraai(-h[22]);									ydraai(144);stapz(b);ydraai(-36);stapz(f);ydraai(-36);stapz(b);ydraai(-36);stapz(f);ydraai(-36);stapz(b);ydraai(-36);
		zdraai(-h[20]);									ydraai(120);stapz(g);ydraai(-60);
		zdraai(-h[19]);									stapz(f);ydraai(36);stapz(b);ydraai(36);

		zdraai(h[25]);	zeshoek();v[25]=geefVlak();		ydraai(-120);stapz(g);ydraai(60);
		zdraai(h[26]);	tienhoek();v[26]=geefVlak();	ydraai(-144);stapz(b);ydraai(36);stapz(f);ydraai(36);stapz(b);ydraai(36);
		zdraai(h[27]);	zeshoek();v[27]=geefVlak();
		zdraai(-h[27]);									stapz(f);ydraai(36);stapz(b);ydraai(36);
		zdraai(h[28]);	zeshoek();v[28]=geefVlak();		ydraai(-120);stapz(g);ydraai(60);
		zdraai(h[29]);	tienhoek();v[29]=geefVlak();	stapz(f);ydraai(-36);stapz(b);ydraai(144);stapz(-f);						
		zdraai(h[30]);	zeshoek();v[30]=geefVlak();		ydraai(-120);stapz(g);ydraai(60);
		zdraai(h[31]);	tienhoek();v[31]=geefVlak();	
		

		
	}

	void tienhoek()
	{	penUit();
		penAan("lichtgrijs");
		vulAan("grijs");
		for(int i=0 ; i<5 ; i++)
		{	stapz(f);
			ydraai(-36);
			stapz(b);
			ydraai(-36);
		}
		vulUit();
		ydraai(-144);
		penAan("zwart");
		vulAan("zwart");
		for(int i=0 ; i<5 ; i++)
		{	stapz(b);
			ydraai(36);
			stapz(f);
			ydraai(36);
		}
		vulUit();
		penUit();
		ydraai(144);
	}

	void zeshoek()
	{	penAan("lichtgrijs");
		vulAan("grijs");
		for(int i=0 ; i<3 ; i++)
		{	stapz(f);
			ydraai(-60);
			stapz(g);
			ydraai(-60);
		}
		vulUit();
		ydraai(-120);
		penAan("zwart");
		vulAan("wit");
		for(int i=0 ; i<3 ; i++)
		{	stapz(g);
			ydraai(60);
			stapz(f);
			ydraai(60);
		}
		vulUit();
		penUit();
		ydraai(120);
	}

	private double begrens(double d, double min, double max)
	{	if(d<min)d = min;if(d>max)d = max;
		return d;
	}	

	public void numberChanged(String name,double val)
	{	y=-2*val;
		for(int i=1 ; i<32 ; i++) 
		{	h[i] = val;
		}
		tekenOpnieuw();
	}
	
	public void muisDrukActie()
	{	for(int i=1 ; i<32 ; i++) bv[i] = v[i].contains(geefDrukx(),geefDruky());
	}
	
	public void muisSleepActie()
	{	for(int i=1 ; i<32 ; i++)
		{	if (bv[i])
			{	h[i] = begrens(h[i] - geefSleepdx(),-max[i], max[i]);
				tekenOpnieuw();
				return;
			}
		}
		xhoek-= geefSleepdy();
		yhoek += geefSleepdx();
		tekenOpnieuw();
	}
}
