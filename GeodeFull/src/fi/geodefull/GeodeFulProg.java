package fi.geodefull;

import java.awt.*;
import java.awt.event.*;

import javax.swing.JButton;

import fi.beans.grnuminput.*;

public class GeodeFulProg extends TekenApplet3D implements ActionListener,NumberListener
{	
	InvoerVariabele xInv, yInv, zijdeInv, opbolInv;
	NumberSlider opbolSl, zijdeSl;
	double xhoek,yhoek,hoek,k, opbol;
	Polygon[][] vg,vf;
	Geode g;
	Geode[][] geod;
	Matrix3D matrot;
	boolean fullereen,geode;
	JButton wisselKnop, lijnenKleurKnop,vlakkenKleurKnop;
	KleurKiezer kk;

	public void initialiseer()
	{	//kk = new KleurKiezer(this);
		//add(kk,"South");
		matrot = new Matrix3D();
		maakMuisActieMogelijk();
		maakAnimatieMogelijk();
		zijdeSl = new NumberSlider(0,300,200,0,"zijde","");
		zijdeSl.addNumberListener(this);
		zijdeSl.setValue(200);
		rg.gridbag.setConstraints(zijdeSl, rg.c);
		rg.setBackground(Color.white);
		ab.setBackground(Color.white);
		//rg.add(zijdeSl);

		wisselKnop = new JButton("fullereen");
		wisselKnop.addActionListener(this);
		rg.gridbag.setConstraints(wisselKnop, rg.c);
		rg.add(wisselKnop);
		xInv = nieuweInvoerVariabele("x",0,4,1);
		yInv = nieuweInvoerVariabele("y",0,4,0);
		
		lijnenKleurKnop = new JButton("  Kleur alle lijnen  ");
		lijnenKleurKnop.addActionListener(this);
		rg.gridbag.setConstraints(lijnenKleurKnop, rg.c);
		//rg.add(lijnenKleurKnop);

		vlakkenKleurKnop = new JButton("Kleur alle vlakken");
		vlakkenKleurKnop.addActionListener(this);
		rg.gridbag.setConstraints(vlakkenKleurKnop, rg.c);
		//rg.add(vlakkenKleurKnop);

		k = 220;
		opbol = 0;
		geode = true;
		
		geod = new Geode[5][5];
		for(int i=0 ; i<5 ; i++)
		{	for(int j=0 ; j<5 ; j++)
			{	if(i==0 && j==0)geod[i][j] = new Geode(1,0,new BasisIco(k), true);
				else geod[i][j] = new Geode(i,j,new BasisIco(k),true);
			}
		}
		g = geod[1][0];
		vg = new Polygon[21][g.aantalDrPerFacet+1];
		vf = new Polygon[21][g.aantalZhPerFacet+1];
	}
	public void tekenprogramma()
	{	//xdraai(xhoek); ydraai(yhoek);
		if(geode)
			tekenGeode(g);
		if(fullereen)
			tekenFullereen(g);
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
	void kleurAlleVlakken()
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
		if(e.getSource()==vlakkenKleurKnop)
		{	kleurAlleVlakken();
		}
		if(e.getSource()==lijnenKleurKnop)
		{	kleurAlleLijnen();
		}
		if(animatieStatus())
		{	onderbreekAnimatie();
			animatieWasAan = true;
		}
		tekenOpnieuw();
		if(animatieWasAan)beginAnimatie();
	}
	public void muisLosActie()
	{	if((geefDrukx()-geefX())*(geefDrukx()-geefX()) + (geefDruky()-geefY())*(geefDruky()-geefY()) < 10 )
		{	muisKkActie();
		}
		tekenOpnieuw();
	}

	public void muisKkActie()
	{	for(int i=1 ; i<21 ; i++)
		{	if(geode)
			{
				for(int j=1 ; j<g.aantalDrPerFacet+1 ; j++)
				{	if(vg[i][j].contains(geefDrukx(),geefDruky()))
					{	if(g.dr[i][j].vulkleur == "oranje")
						{	g.dr[i][j].vulkleur="transparant";
						}
						else 
						{	g.dr[i][j].vulkleur = "oranje";
						}
					}
				}
			}
			if(fullereen)
			{
				for(int j=1; j<g.aantalZhPerFacet+1 ; j++)
				{	if(vf[i][j].contains(geefDrukx(),geefDruky()))
					{	if(g.zh[i][j].vulkleur == "oranje")
						{	g.zh[i][j].vulkleur="transparant";
						}
						else 
						{	g.zh[i][j].vulkleur = "oranje";
						}
					}
				}
			}
		}
		if(fullereen)
		{
			for(int i=1; i<13 ; i++)
				{	if(vf[i][0].contains(geefDrukx(),geefDruky()))
					{	if(g.zh[i][0].vulkleur == "oranje")
						{	g.zh[i][0].vulkleur="transparant";
						}
						else 
						{	g.zh[i][0].vulkleur = "oranje";
						}
					}
				}
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
		
		if(name=="zijde")
		{	k = val;
		}
		if(!animatieStatus())tekenOpnieuw();
		if(animatieWasAan)beginAnimatie();
	}
	public void invoerVarActie(InvoerVariabele iv)
	{			
		if(iv==xInv || iv==yInv)
		{	g = geod[(int)xInv.geefWaarde()][(int)yInv.geefWaarde()];
			vg = new Polygon[21][g.aantalDrPerFacet+1];
			vf = new Polygon[21][g.aantalZhPerFacet+1];
		}
		if(!animatieStatus())tekenOpnieuw();
	}
}

