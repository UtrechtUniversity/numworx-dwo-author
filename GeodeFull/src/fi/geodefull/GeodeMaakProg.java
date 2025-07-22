package fi.geodefull;

import java.awt.*;
import java.awt.event.*;
import fi.beans.grnuminput.*;

public class GeodeMaakProg extends TekenApplet3D implements ActionListener,NumberListener
{	
	InvoerVariabele xInv, opbolInv;
	NumberSlider opbolSl, zijdeSl;
	double xhoek,yhoek,hoek,k, opbol;
	Polygon[][] vg,vf;
	Geode g;
	Matrix3D matrot;
	boolean fullereen,geode;
	Button wisselKnop, lijnenKleurKnop,vlakkenKleurKnop;
	//KleurKiezer kk;

	public void initialiseer()
	{	//kk = new KleurKiezer(this);
		//add(kk,"South");
		matrot = new Matrix3D();
		maakMuisActieMogelijk();
		maakAnimatieMogelijk();
		opbolSl = new NumberSlider(0,100,0,0,"opbolling","");
		opbolSl.addNumberListener(this);
		opbolSl.setValue(0);
		rg.gridbag.setConstraints(opbolSl, rg.c);
		rg.add(opbolSl);
		rg.setBackground(Color.white);
		ab.setBackground(Color.white);
		
		//opbolInv = nieuweInvoerVariabele("opbolling",-100,100,0);
		xInv = nieuweInvoerVariabele("x",0,4,1);
		k = 200;
		opbol = 0;
		geode = true;
	
		
		g = new Geode(1,0,new BasisIco(k),true);
		vg = new Polygon[21][g.aantalDrPerFacet+1];
		vf = new Polygon[21][g.aantalZhPerFacet+1];
	}
	public void tekenprogramma()
	{	tekenGeode(g);
	}
	void tekenGeode(Geode g)
	{	for(int i=1 ; i<21 ; i++)
		{	for(int j=1 ; j<g.aantalDrPerFacet+1 ; j++)
			{	tekenDriehoek(g.dr[i][j]);
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
	/*void kleurAlleVlakken()
	{	if(geode)
		{	for(int i=1 ; i<21 ; i++)
			{	for(int j=1 ; j<g.aantalDrPerFacet+1 ; j++)
				{	g.dr[i][j].vulkleur = kk.geefVulkleur();
				}
			}
		}
		if(fullereen)
		{	for(int i=1 ; i<21 ; i++)
			{	for(int j=1 ; j<g.aantalZhPerFacet+1 ; j++)
				{	g.zh[i][j].vulkleur = kk.geefVulkleur();
				}
			}
			for(int i=1 ; i<13 ; i++)
			{	g.zh[i][0].vulkleur = kk.geefVulkleur();
				
			}
		}
		onthoudKleur();
	}
	void kleurAlleLijnen()
	{	if(geode)
		{	for(int i=1 ; i<21 ; i++)
			{	for(int j=1 ; j<g.aantalDrPerFacet+1 ; j++)
				{	g.dr[i][j].lijnkleur = kk.geefLijnkleur();
				}
			}
		}
		if(fullereen)
		{	for(int i=1 ; i<21 ; i++)
			{	for(int j=1 ; j<g.aantalZhPerFacet+1 ; j++)
				{	g.zh[i][j].lijnkleur = kk.geefLijnkleur();
				}
			}
			for(int i=1 ; i<13 ; i++)
			{	g.zh[i][0].lijnkleur = kk.geefLijnkleur();
				
			}
		}
		onthoudKleur();
	}
	void onthoudKleur()
	{	for(int i=1 ; i<21 ; i++)
		{	for(int j=1 ; j<g.aantalDrPerFacet+1 ; j++)
			{	g.dr[i][j].vorigeLijnkleur = g.dr[i][j].lijnkleur;
				g.dr[i][j].vorigeVulkleur = g.dr[i][j].vulkleur;
			}
		}
		for(int i=1 ; i<21 ; i++)
			{	for(int j=1 ; j<g.aantalZhPerFacet+1 ; j++)
				{	g.zh[i][j].vorigeLijnkleur = g.zh[i][j].lijnkleur;
					g.zh[i][j].vorigeVulkleur = g.zh[i][j].vulkleur;
				}
			}
		for(int i=1 ; i<13 ; i++)
			{	g.zh[i][0].vorigeLijnkleur = g.zh[i][0].lijnkleur;
				g.zh[i][0].vorigeVulkleur = g.zh[i][0].vulkleur;
			}
	}*/
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
		{	if(wisselKnop.getLabel()=="fullereen")
			{	fullereen=true;
				geode = false;
				wisselKnop.setLabel("geode");
				
			}
			else
			{	fullereen=false;
				geode = true;
				wisselKnop.setLabel("fullereen");
			}
		}
		
		if(animatieStatus())
		{	onderbreekAnimatie();
			animatieWasAan = true;
		}
		tekenOpnieuw();
		if(animatieWasAan)beginAnimatie();
	}/*
	public void muisDrukActie()
	{	for(int i=1 ; i<21 ; i++)
		{	if(geode)
			{
				for(int j=1 ; j<g.aantalDrPerFacet+1 ; j++)
				{	if(vg[i][j].contains(geefDrukx(),geefDruky()))
					{	//g.dr[i][j].vulkleur = kk.geefVulkleur();
						//g.dr[i][j].lijnkleur = kk.geefLijnkleur();
						if(g.dr[i][j].vulkleur == g.dr[i][j].vorigeVulkleur && g.dr[i][j].lijnkleur == g.dr[i][j].vorigeLijnkleur)
						{	g.dr[i][j].vulkleur=kk.geefVulkleur();
							g.dr[i][j].lijnkleur=kk.geefLijnkleur();
						}
						else 
						{	g.dr[i][j].vulkleur = g.dr[i][j].vorigeVulkleur;
							g.dr[i][j].lijnkleur = g.dr[i][j].vorigeLijnkleur;
						}
					}
				}
			}
			if(fullereen)
			{
				for(int j=1; j<g.aantalZhPerFacet+1 ; j++)
				{	if(vf[i][j].contains(geefDrukx(),geefDruky()))
					{	if(g.zh[i][j].vulkleur == g.zh[i][j].vorigeVulkleur && g.zh[i][j].lijnkleur == g.zh[i][j].vorigeLijnkleur)
						{	g.zh[i][j].vulkleur=kk.geefVulkleur();
							g.zh[i][j].lijnkleur=kk.geefLijnkleur();
						}
						else 
						{	g.zh[i][j].vulkleur = g.zh[i][j].vorigeVulkleur;
							g.zh[i][j].lijnkleur = g.zh[i][j].vorigeLijnkleur;
						}
						//if(g.zh[i][j].kleur == "transparant")g.zh[i][j].kleur = "oranje";
						//else if(g.zh[i][j].kleur == "oranje")g.zh[i][j].kleur = "transparant";
						//tekenOpnieuw();
					}
				}
			}
		}
		if(fullereen)
		{
			for(int i=1; i<13 ; i++)
				{	if(vf[i][0].contains(geefDrukx(),geefDruky()))
					{	if(g.zh[i][0].vulkleur == g.zh[i][0].vorigeVulkleur && g.zh[i][0].lijnkleur == g.zh[i][0].vorigeLijnkleur)
						{	g.zh[i][0].vulkleur=kk.geefVulkleur();
							g.zh[i][0].lijnkleur=kk.geefLijnkleur();
						}
						else 
						{	g.zh[i][0].vulkleur = g.zh[i][0].vorigeVulkleur;
							g.zh[i][0].lijnkleur = g.zh[i][0].vorigeLijnkleur;
						}
						//if(g.zh[j][0].kleur == "transparant")g.zh[j][0].kleur = "oranje";
						//else if(g.zh[j][0].kleur == "oranje")g.zh[j][0].kleur = "transparant";
						
					}
				}
		}
		tekenOpnieuw();
	}*/
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
	public void numberChanged(String name,double val)
	{	boolean animatieWasAan = false;
		if(animatieStatus())
		{	animatieWasAan = true;
			onderbreekAnimatie();
		}
		if(name=="opbolling")
		{	g.bolop(0.1*val);
			
		}
		if(name=="zijde")
		{	k = val;
		}
		if(!animatieStatus())tekenOpnieuw();
		if(animatieWasAan)beginAnimatie();
	}
	public void invoerVarActie(InvoerVariabele iv)
	{	
		
		if(iv==xInv)
		{	g = new Geode((int)xInv.geefWaarde(),0,new BasisIco(k),true);
			g.bolop(0);
			vg = new Polygon[21][g.aantalDrPerFacet+1];
			vf = new Polygon[21][g.aantalZhPerFacet+1];
		}
		if(!animatieStatus())tekenOpnieuw();
	}
}

