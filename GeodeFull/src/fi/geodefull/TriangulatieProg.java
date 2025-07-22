package fi.geodefull;

import java.awt.*;
import java.awt.event.*;
import fi.beans.grnuminput.*;

public class TriangulatieProg extends TekenApplet3D implements ActionListener 
{	
	double xhoek,yhoek,hoek,k, l, r, x, y, z;
	InvoerVariabele xInv,yInv;
	NumberSlider opbolSl, zijdeSl;
	Polygon[][] vg,vf;
	Geode g,ico;
	Matrix3D matrot;
	boolean fullereen,geode,raak;
	Button wisselKnop, lijnenKleurKnop,vlakkenKleurKnop;
	
	public void initialiseer()
	{	wisselKnop = new Button("   geode   ");
		wisselKnop.addActionListener(this);
		rg.gridbag.setConstraints(wisselKnop, rg.c);
		rg.add(wisselKnop);
		rg.setBackground(Color.white);
		xInv = nieuweInvoerVariabele("x",0,4,1);
		yInv = nieuweInvoerVariabele("y",0,4,0);
		matrot = new Matrix3D();
		maakMuisActieMogelijk();
		hoek = 0;
		k = 200;
		l = 50;
		x = 1;
		y = 0;
		z = 1;
		hoek = 0;
		geode = false;
		g = new Geode(1,0,new BasisIco(k),true);
		ico = new Geode(1,0,new BasisIco(k),true);
		for(int i=1 ; i<21 ; i++)
			{	for(int j=1 ; j<ico.aantalDrPerFacet+1 ; j++)
				{	ico.dr[i][j].lijnkleur = "rood";
					ico.dr[i][j].vulkleur = "transparant";
				}
			}
		
		vg = new Polygon[21][g.aantalDrPerFacet+1];
		vf = new Polygon[21][g.aantalZhPerFacet+1];
	}
	
	public void tekenprogramma()
	{	if(geode)
		{	tekenGeode(0,g);
		}
		else
		{
			tb.mat.initialiseer();

			penUit();stap(-360,260,0);penAan();
		
			rechts(150);vooruit(10*l);
			links(120+hoek);
			vulAan("oranje");
			vooruit(z*l);rechts(120);
			vooruit(z*l);rechts(120);
			vooruit(z*l);rechts(120);
			vulUit();
			rechts(120+hoek);
			vooruit(-10*l);links(150);
		
			links(90);
			for(int i=0 ; i<7 ; i++)
			{	rij(14);
			}
		}
		
	}
	void rij(int n)
	{	for(int i=0 ; i<n ; i++)
		{	patroon(l);
		}
		vooruit(n*l);links(60);
		vooruit(l);links(60);
		vooruit(l);rechts(120);
	}
	void patroon(double z)
	{	vooruit(z);rechts(120);
		vooruit(z);rechts(120);
		vooruit(2*z);rechts(120);
		vooruit(z);rechts(120);
		vooruit(z);rechts(60);
		vooruit(z);rechts(180);
	}
		void tekenGeode(int n,Geode g)
	{	for(int i=1 ; i<21 ; i++)
		{	for(int j=1 ; j<g.aantalDrPerFacet+1 ; j++)
			{	tekenDriehoek(n,g.dr[i][j]);
				vg[i][j] = geefVlak();
			}
		}
	}
	void tekenFullereen(Geode g)
	{	for(int i=1 ; i<21 ; i++)
		{	for(int j=1 ; j<g.aantalZhPerFacet+1 ; j++)
			{	tekenZeshoek(g.zh[i][j]);
				vf[i][j] = geefVlak();
			}
		}
		for(int i=1 ; i<13 ; i++)
		{	tekenZeshoek(g.zh[i][0]);
			vf[i][0] = geefVlak();
		}
	}
	void tekenDriehoek(int n,Driehoek d)
	{	penUit();
		stap(k*d.punten[1].x, k*d.punten[1].y, k*d.punten[1].z);
		if(!(d.lijnkleur=="transparant"))penAan("lichtgrijs");
		if(d.vulkleur=="transparant")vulAan(d.vulkleur);
		else vulAan("grijs");
			
			{	stap(-k*(d.punten[1].x-d.punten[2].x), -k*(d.punten[1].y-d.punten[2].y), -k*(d.punten[1].z-d.punten[2].z));
				stap(-k*(d.punten[2].x-d.punten[3].x), -k*(d.punten[2].y-d.punten[3].y), -k*(d.punten[2].z-d.punten[3].z));
				stap(-k*(d.punten[3].x-d.punten[1].x), -k*(d.punten[3].y-d.punten[1].y), -k*(d.punten[3].z-d.punten[1].z));
			}
		vulUit();
		if(!(d.lijnkleur=="transparant"))penAan(d.lijnkleur);
		vulAan(n,d.vulkleur);
		
		{	stap(k*(d.punten[3].x-d.punten[1].x), k*(d.punten[3].y-d.punten[1].y), k*(d.punten[3].z-d.punten[1].z));
			stap(k*(d.punten[2].x-d.punten[3].x), k*(d.punten[2].y-d.punten[3].y), k*(d.punten[2].z-d.punten[3].z));
			stap(k*(d.punten[1].x-d.punten[2].x), k*(d.punten[1].y-d.punten[2].y), k*(d.punten[1].z-d.punten[2].z));
		}
		vulUit(n);
		penUit();
		stap(-k*d.punten[1].x, -k*d.punten[1].y, -k*d.punten[1].z);
		penAan();
	}
	void tekenZeshoek(Zeshoek z)
	{	penUit();
		stap(k*z.punten[1].x, k*z.punten[1].y, k*z.punten[1].z);
		if(z.lijnkleur!="transparant")penAan("lichtgrijs");
		if(z.vulkleur=="transparant")vulAan(z.vulkleur);
		else vulAan("grijs");
			for(int i=6 ; i>0 ; i--)
			{	stap(-k*z.stappen[i].x, -k*z.stappen[i].y, -k*z.stappen[i].z);
			}
		vulUit();
		if(z.lijnkleur!="transparant")penAan(z.lijnkleur);
		vulAan(z.vulkleur);
		for(int i=1 ; i<7 ; i++)
		{	stap(k*z.stappen[i].x, k*z.stappen[i].y, k*z.stappen[i].z);
		}
		vulUit();
		penUit();
		stap(-k*z.punten[1].x, -k*z.punten[1].y, -k*z.punten[1].z);
		penAan();
	}
	public void actionPerformed(ActionEvent e)
	{	boolean animatieWasAan=false;
		if(e.getSource()==wisselKnop)
		{	if(wisselKnop.getLabel()=="triangulatie")
			{	fullereen=true;
				geode = false;
				wisselKnop.setLabel("   geode   ");
				
			}
			else
			{	g = new Geode((int)x,(int)y,new BasisIco(k),true);
				vg = new Polygon[21][g.aantalDrPerFacet+1];
				geode = true;
				wisselKnop.setLabel("triangulatie");
			}
		}
		
		if(animatieStatus())
		{	onderbreekAnimatie();
			animatieWasAan = true;
		}
		tekenOpnieuw();
		if(animatieWasAan)beginAnimatie();
	}
		public void muisKkActie()
	{	for(int i=1 ; i<21 ; i++)
		{	for(int j=1; j<g.aantalDrPerFacet+1 ; j++)
			{	if(vg[i][j].contains(geefDrukx(),geefDruky()))
				{	if(g.dr[i][j].vulkleur == "transparant")
					{	g.dr[i][j].vulkleur="oranje";
					}
					else 
					{	g.dr[i][j].vulkleur = "transparant";
					}
				}
			}
		}
		
		
		tekenOpnieuw();
	}
	public void muisDrukActie()
	{	for(int i=1 ; i<21 ; i++)
		{	for(int j=1; j<g.aantalDrPerFacet+1 ; j++)
			{	if(vg[i][j].contains(geefDrukx(),geefDruky()))
				{	raak = true;
					return;
				}
			}
		}
		
		
		raak = false;
	}
	public void muisLosActie()
	{	if((geefDrukx()-geefX())*(geefDrukx()-geefX()) + (geefDruky()-geefY())*(geefDruky()-geefY()) < 10 )
		{	muisKkActie();
		}
		tekenOpnieuw();
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
	public void invoerVarActie(InvoerVariabele iv)
	{			
		if(iv==xInv || iv==yInv)
		{	x = (int)xInv.geefWaarde();
			y = (int)yInv.geefWaarde();
			z = Math.sqrt(x*x + y*y + x*y);
			if(x==0 && y==0)hoek=0;
			else hoek = Math.asin(y*Math.sqrt(3)/(2*z))*180/Math.PI;
			if(geode)
			{	g = new Geode((int)xInv.geefWaarde(),(int)yInv.geefWaarde(),new BasisIco(k),true);
				vg = new Polygon[21][g.aantalDrPerFacet+1];
			}	
		}
		if(!animatieStatus())tekenOpnieuw();
	}
}
