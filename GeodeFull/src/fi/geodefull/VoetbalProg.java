package fi.geodefull;

import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;

import fi.beans.grnuminput.*;

public class VoetbalProg extends TekenApplet3D 
{	
	double xhoek,yhoek,hoek,k;
	Polygon[][] vf;
	Geode g;
	Matrix3D matrot;
	boolean begin,raak;
	
	public void initialiseer()
	{	matrot = new Matrix3D();
		maakMuisActieMogelijk();
		maakAnimatieMogelijk();
		k = 200;
		begin = true;
		g = new Geode(1,1,new BasisIco(k),true);
		vf = new Polygon[21][g.aantalZhPerFacet+1];
		for(int i=1 ; i<21 ; i++)
		{	for(int j=1 ; j<g.aantalZhPerFacet+1 ; j++)
			{	g.zh[i][j].vulkleur = "wit";
			}
		}
		for(int i=1 ; i<13 ; i++)
		{	g.zh[i][0].vulkleur = "zwart";
		}
		ab.setBackground(Color.white);
	}
	
	public void tekenprogramma()
	{	tekenFullereen(0,g);
		//if(begin)
		//{	beginAnimatie();
		//	begin = false;
		//}
	}
	void tekenFullereen(int n,Geode g)
	{	for(int i=1 ; i<21 ; i++)
		{	for(int j=1 ; j<g.aantalZhPerFacet+1 ; j++)
			{	tekenZeshoek(n,g.zh[i][j]);
				vf[i][j] = geefVlak();
			}
		}
		for(int i=1 ; i<13 ; i++)
		{	tekenZeshoek(n,g.zh[i][0]);
			vf[i][0] = geefVlak();
		}
	}
	void tekenZeshoek(int n,Zeshoek z)
	{	penUit();
		stap(k*z.punten[1].x, k*z.punten[1].y, k*z.punten[1].z);
		if(!z.lijnkleur.equals("transparant"))penAan("lichtgrijs");
		if(z.vulkleur.equals("transparant"))vulAan(z.vulkleur);
		else vulAan("grijs");
			for(int i=6 ; i>0 ; i--)
			{	stap(-k*z.stappen[i].x, -k*z.stappen[i].y, -k*z.stappen[i].z);
			}
		vulUit();
		if(!z.lijnkleur.equals("transparant"))penAan(z.lijnkleur);
		vulAan(z.vulkleur);
		for(int i=1 ; i<7 ; i++)
		{	stap(k*z.stappen[i].x, k*z.stappen[i].y, k*z.stappen[i].z);
		}
		vulUit();
		penUit();
		stap(-k*z.punten[1].x, -k*z.punten[1].y, -k*z.punten[1].z);
		penAan();
	}

	public void setState(Hashtable h)
	{	
		Hashtable geodeState = new Hashtable();
		
		if(h.containsKey("geodeState")) geodeState = (Hashtable)h.get("geodeState");
		
		g.setState(geodeState);
		
	}
	
	public Hashtable getState()
	{	
		Hashtable geodeState = new Hashtable();
		
		geodeState = g.getState();
		
		Hashtable h = new Hashtable();
		h.put("geodeState", geodeState);
		
		return h;
	}
	public void muisKkActie()
	{	for(int i=1 ; i<21 ; i++)
		{	for(int j=1; j<g.aantalZhPerFacet+1 ; j++)
			{	if(vf[i][j].contains(geefDrukx(),geefDruky()))
				{	if(g.zh[i][j].vulkleur == "transparant")
					{	g.zh[i][j].vulkleur="wit";
					}
					else 
					{	g.zh[i][j].vulkleur = "transparant";
					}
				}
			}
		}
		
		for(int i=1; i<13 ; i++)
		{	if(vf[i][0].contains(geefDrukx(),geefDruky()))
			{	if(g.zh[i][0].vulkleur == "transparant")
				{	g.zh[i][0].vulkleur="zwart";
				}
				else 
				{	g.zh[i][0].vulkleur = "transparant";
				}
			}
		}
		tekenOpnieuw();
	}
	public void muisDrukActie()
	{	for(int i=1 ; i<21 ; i++)
		{	for(int j=1; j<g.aantalZhPerFacet+1 ; j++)
			{	if(vf[i][j].contains(geefDrukx(),geefDruky()))
				{	raak = true;
					return;
				}
			}
		}
		
		for(int i=1; i<13 ; i++)
		{	if(vf[i][0].contains(geefDrukx(),geefDruky()))
			{	raak = true;
					return;
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
