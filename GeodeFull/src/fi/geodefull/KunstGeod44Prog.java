package fi.geodefull;

import java.awt.*;
import java.awt.event.*;
import fi.beans.grnuminput.*;

public class KunstGeod44Prog extends TekenApplet3D 
{	
	double xhoek,yhoek,hoek,k;
	Geode g;
	Polygon[][] vf;
	Matrix3D matrot;
	boolean begin,raak;
	Image plaatje;
	
	public void initialiseer()
	{	matrot = new Matrix3D();
		maakMuisActieMogelijk();
		maakAnimatieMogelijk();
		k = 220;
		begin = true;
		g = new Geode(4,4,new BasisIco(k),true);
		vf = new Polygon[21][g.aantalDrPerFacet+1];
		for(int i=1 ; i<21 ; i++)
		{	for(int j=1 ; j<g.aantalDrPerFacet+1 ; j++)
			{	if(j==1 || j==2 || j==3 || j==4 || j==5 || j==6 ||
				   j==13 || j==14 || j==15 || j==16 || j==17 || j==18 ||
					j==25 || j==26 || j==27 || j==28 || j==29 || j==30)g.dr[i][j].vulkleur = "rood";
			}
		}
		ab.setBackground(Color.white);
	}
	
	
	public void tekenprogramma()
	{	begindraai(0,-20);
		tekenGeode(g);
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
	void tekenGeode(Geode g)
	{	for(int i=1 ; i<21 ; i++)
		{	for(int j=1 ; j<g.aantalDrPerFacet+1 ; j++)
			{	tekenDriehoek(g.dr[i][j]);
				vf[i][j] = geefVlak();
			}
		}
	}

	void tekenDriehoek(Driehoek d)
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
		vulAan(d.vulkleur);
		
		{	stap(k*(d.punten[3].x-d.punten[1].x), k*(d.punten[3].y-d.punten[1].y), k*(d.punten[3].z-d.punten[1].z));
			stap(k*(d.punten[2].x-d.punten[3].x), k*(d.punten[2].y-d.punten[3].y), k*(d.punten[2].z-d.punten[3].z));
			stap(k*(d.punten[1].x-d.punten[2].x), k*(d.punten[1].y-d.punten[2].y), k*(d.punten[1].z-d.punten[2].z));
		}
		vulUit();
		penUit();
		stap(-k*d.punten[1].x, -k*d.punten[1].y, -k*d.punten[1].z);
		penAan();
	}

	
	public void muisDrukActie()
	{	for(int i=1 ; i<21 ; i++)
		{	for(int j=1; j<g.aantalDrPerFacet+1 ; j++)
			{	if(vf[i][j].contains(geefDrukx(),geefDruky()))
				{	raak = true;
					return;
				}
			}
		}
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
