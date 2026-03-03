package fi.geodefull;

import java.awt.*;
import java.awt.event.*;
import fi.beans.grnuminput.*;

public class Geode40Prog extends TekenApplet3D 
{	
	double xhoek,yhoek,hoek,k;
	Polygon[][] vg;
	Geode g;
	Matrix3D matrot;
	boolean begin,raak;
	
	public void initialiseer()
	{	matrot = new Matrix3D();
		maakMuisActieMogelijk();
		maakAnimatieMogelijk();
		k = 200;
		begin = true;
		g = new Geode(4,0,new BasisIco(k),true);
		vg = new Polygon[21][g.aantalDrPerFacet+1];
		/*for(int i=1 ; i<21 ; i++)
		{	for(int j=1 ; j<g.aantalZhPerFacet+1 ; j++)
			{	g.zh[i][j].vulkleur = "transparant";
			}
		}
		for(int i=1 ; i<13 ; i++)
		{	g.zh[i][0].vulkleur = "transparant";
		}*/
		ab.setBackground(Color.white);
	}
	
	public void tekenprogramma()
	{	tekenGeode(0,g);
		//if(begin)
		//{	beginAnimatie();
		//	begin = false;
		//}
	}
	void tekenGeode(int n,Geode g)
	{	for(int i=1 ; i<21 ; i++)
		{	for(int j=1 ; j<g.aantalDrPerFacet+1 ; j++)
			{	tekenDriehoek(n,g.dr[i][j]);
				vg[i][j] = geefVlak();
			}
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
	public void muisLosActie()
	{	if((geefDrukx()-geefX())*(geefDrukx()-geefX()) + (geefDruky()-geefY())*(geefDruky()-geefY()) < 10 )
		{	muisKkActie();
		}
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
