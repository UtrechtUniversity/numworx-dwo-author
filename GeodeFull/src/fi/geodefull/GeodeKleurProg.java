package fi.geodefull;

import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;

import javax.swing.JButton;

import fi.beans.grnuminput.*;

public class GeodeKleurProg extends TekenApplet3D implements ActionListener,NumberListener
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
	{	kk = new KleurKiezer(this);
		add(kk,"South");
		matrot = new Matrix3D();
		maakMuisActieMogelijk();
		maakAnimatieMogelijk();
		zijdeSl = new NumberSlider(0,300,200,0,"zijde","");
		zijdeSl.addNumberListener(this);
		zijdeSl.setValue(200);
		rg.gridbag.setConstraints(zijdeSl, rg.c);
		rg.add(zijdeSl);
		rg.setBackground(Color.white);
		ab.setBackground(Color.white);

		wisselKnop = new JButton("fullereen");
		wisselKnop.addActionListener(this);
		rg.gridbag.setConstraints(wisselKnop, rg.c);
		rg.add(wisselKnop);
		xInv = nieuweInvoerVariabele("x",0,4,1);
		yInv = nieuweInvoerVariabele("y",0,4,0);
		
		lijnenKleurKnop = new JButton("  Kleur alle lijnen  ");
		lijnenKleurKnop.addActionListener(this);
		rg.gridbag.setConstraints(lijnenKleurKnop, rg.c);
		rg.add(lijnenKleurKnop);

		vlakkenKleurKnop = new JButton("Kleur alle vlakken");
		vlakkenKleurKnop.addActionListener(this);
		rg.gridbag.setConstraints(vlakkenKleurKnop, rg.c);
		rg.add(vlakkenKleurKnop);

		k = 200;
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
	public void setState(Hashtable h)
	{	
		Hashtable geodeState = new Hashtable();
		int xWaarde = 1;
		int yWaarde = 0;
		boolean fullereen = false;
		
		if(h.containsKey("geodeState")) geodeState = (Hashtable)h.get("geodeState");
		if(h.containsKey("xWaarde")) xWaarde = ((Integer)h.get("xWaarde")).intValue();
		if(h.containsKey("yWaarde")) yWaarde = ((Integer)h.get("yWaarde")).intValue();
		if(h.containsKey("fullereen")) fullereen = ((Boolean)h.get("fullereen")).booleanValue();
		
		xInv.zetWaarde(xWaarde);
		yInv.zetWaarde(yWaarde);
		g = geod[xWaarde][yWaarde];
		vg = new Polygon[21][g.aantalDrPerFacet+1];
		vf = new Polygon[21][g.aantalZhPerFacet+1];
		this.fullereen = fullereen;
		this.geode = !fullereen;
		g.setState(geodeState);
	}
	
	public Hashtable getState()
	{	
		Hashtable geodeState = new Hashtable();
		int xWaarde = 1;
		int yWaarde = 0;
		boolean fullereen = false;
		
		geodeState = g.getState();
		xWaarde = (int)xInv.geefWaarde();
		yWaarde = (int)yInv.geefWaarde();
		fullereen = this.fullereen;
		
		Hashtable h = new Hashtable();
		h.put("xWaarde", new Integer(xWaarde));
		h.put("yWaarde", new Integer(yWaarde));
		h.put("geodeState", geodeState);
		h.put("fullereen", new Boolean(fullereen));
		
		return h;
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
		if(!(d.lijnkleur.equals("transparant")))penAan("lichtgrijs");
		if(d.vulkleur.equals("transparant"))vulAan(d.vulkleur);
		else vulAan("grijs");
			
			{	stap(-k*(d.punten[1].x-d.punten[2].x), -k*(d.punten[1].y-d.punten[2].y), -k*(d.punten[1].z-d.punten[2].z));
				stap(-k*(d.punten[2].x-d.punten[3].x), -k*(d.punten[2].y-d.punten[3].y), -k*(d.punten[2].z-d.punten[3].z));
				stap(-k*(d.punten[3].x-d.punten[1].x), -k*(d.punten[3].y-d.punten[1].y), -k*(d.punten[3].z-d.punten[1].z));
			}
		vulUit();
		if(!(d.lijnkleur.equals("transparant")))penAan(d.lijnkleur);
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
					{	if(g.dr[i][j].vulkleur == g.dr[i][j].vorigeVulkleur && g.dr[i][j].lijnkleur == g.dr[i][j].vorigeLijnkleur)
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

class Geode
{	int x, y;
	int aantalDrPerFacet;
	int aantalZhPerFacet;
	Driehoek[][] dr;
	Driehoek[] d;
	Zeshoek[][]zh;
	
	public Geode(int x, int y, BasisIco b, boolean fOpBol)
	{	this.x = x; this.y = y;

		d = b.d;
		
		if(x==1 && y==0 || x==0 && y==1)
		{	aantalDrPerFacet=1;
			aantalZhPerFacet=0;
			dr = new Driehoek[21][aantalDrPerFacet+1];
			zh = new Zeshoek[21][aantalZhPerFacet+1];
			for(int i=1 ; i<21 ; i++)
			{	d[i].maakPuntenG(x,y);
			}
			for(int i=1 ; i<21 ; i++)
			{	dr[i][1] = new Driehoek(d[i].punten[1], d[i].punten[2],d[i].punten[3]);dr[i][1].maakPuntenF(fOpBol);
			}	
			zh[1][0] = new Zeshoek(dr[1][1].punten[4],dr[2][1].punten[4],dr[3][1].punten[4],dr[3][1].punten[4],dr[4][1].punten[4],dr[5][1].punten[4]);
			zh[2][0] = new Zeshoek(dr[6][1].punten[4],dr[1][1].punten[4],dr[5][1].punten[4],dr[5][1].punten[4],dr[10][1].punten[4],dr[15][1].punten[4]);
			zh[3][0] = new Zeshoek(dr[7][1].punten[4],dr[2][1].punten[4],dr[1][1].punten[4],dr[1][1].punten[4],dr[6][1].punten[4],dr[11][1].punten[4]);
			zh[4][0] = new Zeshoek(dr[8][1].punten[4],dr[3][1].punten[4],dr[2][1].punten[4],dr[2][1].punten[4],dr[7][1].punten[4],dr[12][1].punten[4]);
			zh[5][0] = new Zeshoek(dr[9][1].punten[4],dr[4][1].punten[4],dr[3][1].punten[4],dr[3][1].punten[4],dr[8][1].punten[4],dr[13][1].punten[4]);
			zh[6][0] = new Zeshoek(dr[10][1].punten[4],dr[5][1].punten[4],dr[4][1].punten[4],dr[4][1].punten[4],dr[9][1].punten[4],dr[14][1].punten[4]);
			zh[7][0] = new Zeshoek(dr[16][1].punten[4],dr[11][1].punten[4],dr[6][1].punten[4],dr[6][1].punten[4],dr[15][1].punten[4],dr[20][1].punten[4]);
			zh[8][0] = new Zeshoek(dr[17][1].punten[4],dr[12][1].punten[4],dr[7][1].punten[4],dr[7][1].punten[4],dr[11][1].punten[4],dr[16][1].punten[4]);
			zh[9][0] = new Zeshoek(dr[18][1].punten[4],dr[13][1].punten[4],dr[8][1].punten[4],dr[8][1].punten[4],dr[12][1].punten[4],dr[17][1].punten[4]);
			zh[10][0] = new Zeshoek(dr[19][1].punten[4],dr[14][1].punten[4],dr[9][1].punten[4],dr[9][1].punten[4],dr[13][1].punten[4],dr[18][1].punten[4]);
			zh[11][0] = new Zeshoek(dr[20][1].punten[4],dr[15][1].punten[4],dr[10][1].punten[4],dr[10][1].punten[4],dr[14][1].punten[4],dr[19][1].punten[4]);
			zh[12][0] = new Zeshoek(dr[20][1].punten[4],dr[19][1].punten[4],dr[18][1].punten[4],dr[18][1].punten[4],dr[17][1].punten[4],dr[16][1].punten[4]);
		}
		else 
		{	int[] dat = new int[200];
			int[] zat = new int[200];
			if(x==2 && y==0 || x==0 && y==2)
			{	int[] drdata20 = {4,1,1,4,0,2,3,2,5,0,0,3,3,6,0,1,3,4,5,0,2,3};
				int[] zhdata20 = {2,1,4,3,1,1,1,3,1,4,0,1,1,1,0,3,2,4,1,2,1,1,3,2,4,0,1,2,1,0,3};
				dat = drdata20;zat = zhdata20;
			}
			else if(x==3 && y==0 || x==0 && y==3)
			{	int[] drdata30 = {9,2,1,4,0,5,6,2,6,0,1,6,3,8,0,3,6,4,10,0,5,6,6,10,0,1,6,8,10,0,3,6,9,10,0,4,6,5,10,0,0,6,7,10,0,2,6};
				int[] zhdata30 = {4,1,4,7,1,7,1,3,1,4,0,3,1,1,0,3,2,5,8,2,7,1,3,2,4,0,3,2,1,0,3,3,6,9,3,7,1,3,3,4,0,3,3,1,0,3,4,8,5,0,9,0,1,0,6,0,1,0,7,0,1};
				dat = drdata30;zat = zhdata30;
			}
			else if(x==4 && y==0 || x==0 && y==4)
			{	int[] drdata40 = {16,3,1,4,0,8,9,2,7,0,2,9,3,10,0,5,9,4,13,0,8,9,7,14,0,2,9,10,15,0,5,9,12,13,0,7,9,6,14,0,1,9,9,15,0,4,9 ,13,15,0,7,9,14,13,0,1,9,15,14,0,4,9,11,15,0,6,9,5,13,0,0,9,8,14,0,3,9,13,14,0,11,12};			
				int[] zhdata40 = {9,1,4,7,1,13,1,3,1,4,0,3,1,1,0,3,2,5,8,2,13,1,3,2,4,0,3,2,1,0,3,3,6,9,3,13,1,3,3,4,0,3,3,1,0,3,4,14,11,0,16,0,1,0,10,0,1,0,7,0,1,5,15,12,0,16,0,1,0,11,0,1,0,8,0,1,6,13,10,0,16,0,1,0,12,0,1,0,9,0,1,7,10,13,1,7,1,3,1,10,1,3,1,13,1,3,8,11,14,2,7,1,3,2,10,1,3,2,13,1,3,9,12,15,3,7,1,3,3,10,1,3,3,13,1,3};
				dat = drdata40;zat = zhdata40;
			}
			else if(x==1 && y==1)
			{	int[] drdata11 = {6,1,1,4,1,0,1,2,4,2,0,1,3,4,3,0,1,4,3,1,0,1,4,1,2,0,1,4,2,3,0,1};
				int[] zhdata11 = {1,1,5,2,0,4,2,3,0,1,2,3,0,4,0,3};
				dat = drdata11;zat = zhdata11;
			}
			else if(x==2 && y==2)
			{	int[] drdata22 = {12,2,1,4,1,0,6,2,6,2,0,6,3,8,3,0,6,4,9,1,0,6,6,5,2,0,6,8,7,3,0,6,4,10,0,5,6,6,10,0,1,6,8,10,0,3,6,10,8,0,5,6,10,4,0,1,6,10,6,0,3,6,};
				int[] zhdata22 = {7, 11,7,4,0,1,0,3,2,1,1,3,2,4,1,3, 12,8,5,0,1,1,3,3,1,1,3,3,4,1,3,10,9,6,0,1,2,3,1,1,1,3,1,4,1,3, 4,7,10,1,4,1,3,1,7,1,3,1,10,1,3, 5,8,11,2,4,1,3,2,7,1,3,2,10,1,3,6,9,12,3,4,1,3,3,7,1,3,3,10,1,3, 7,11,8,0,12,0,1,0,9,0,1,0,10,0,1};
				dat = drdata22;zat = zhdata22;
			}
			else if(x==3 && y==3)
			{	int[] drdata33 = {27,4,  1,4,1,0,12,  2,8,2,0,12,  3,12,3,0,12,  4,15,1,0,12,  8,7,2,0,12,	  12,11,3,0,12,	 4,16,0,11,12,	 8,17,0,3,12,	  12,18,0,7,12,	  16,14,0,11,12,	  17,6,0,3,12,	  18,10,0,7,12,	  15,14,1,2,12,	  7,6,2,2,12,	  11,10,3,2,12,	  14,18,0,9,12,	  6,16,0,1,12,	  10,17,0,5,12,	  18,12,0,9,12,	  16,4,0,1,12,	  17,8,0,5,12,	  16,19,0,10,12,	  17,19,0,2,12,  18,19,0,6,12,	  19,18,0,10,12,	 19,16,0,2,12,	  19,17,0,6,12};				
				int[] zhdata33 = {13,  20,7,4,	0,1,0,1,	2,1,1,3,	2,4,1,3,  21,8,5,	0,2,0,1,	3,1,1,3,	3,4,1,3,  19,9,6,	0,3,0,1,	1,1,1,3,	1,4,1,3,  4,7,10,	0,13,0,1,	1,16,1,3,	1,19,1,3,  5,8,11,	0,14,0,1,	2,16,1,3,	2,19,1,3,  6,9,12,	0,15,0,1,	3,16,1,3,	3,19,1,3,	  7,20,17,	0,26,0,1,	0,22,0,1,	0,10,0,1,	  8,21,18,	0,27,0,1,	0,23,0,1,	0,11,0,1,  9,19,16,	0,25,0,1,	0,24,0,1,	0,12,0,1,	  10,22,25,	0,16,0,1,	1,13,1,3,	0,13,0,1,	  11,23,26,	0,17,0,1,	2,13,1,3,	0,14,0,1,	  12,24,27,	0,18,0,1,	3,13,1,3,	0,15,0,1,  26,23,27,	0,24,0,1,	0,25,0,1,	0,22,0,1};
				dat = drdata33;zat = zhdata33;
			}
			else if(x==4 && y==4)
			{	int[] drdata44 = {48,6,	  1,4,1,0,18,  2,10,2,0,18,  3,16,3,0,18,  4,21,1,0,18,  10,9,2,0,18,  16,15,3,0,18,  4,22,0,17,18,  10,24,0,5,18,  16,26,0,11,18,  22,20,0,17,18,  24,8,0,5,18,  26,14,0,11,18,  21,20,1,2,18,  9,8,2,2,18,  15,14,3,2,18,	  20,19,1,2,18,  8,7,2,2,18,  14,13,3,2,18,  22,28,0,16,18,  24,29,0,4,18,  26,30,0,10,18,  28,27,0,16,18,  29,23,0,4,18,  30,25,0,10,18,  20,27,0,15,18,  8,23,0,3,18,  14,25,0,9,18,  27,18,0,15,18,  23,6,0,3,18,  25,12,0,9,18,  27,30,0,14,18,  23,28,0,2,18,  25,29,0,8,18,  30,26,0,14,18,  28,22,0,2,18,  29,24,0,8,18,  18,26,0,13,18,  6,22,0,1,18,  12,24,0,7,18,  26,16,0,13,18,  22,4,0,1,18,  24,10,0,7,18,  28,31,0,23,24,  29,31,0,19,24,  30,31,0,21,24,  31,30,0,23,24,  31,28,0,19,24,  31,29,0,21,24};				
				int[] zhdata44 = {25,  41,7,4,	0,1,0,1,	2,1,1,3,	2,4,1,3,  42,8,5,	0,2,0,1,	3,1,1,3,	3,4,1,3,  40,9,6,	0,3,0,1,	1,1,1,3,	1,4,1,3,    4,7,10,	0,13,0,1,	1,37,1,3,	1,40,1,3,  5,8,11,	0,14,0,1,	2,37,1,3,	2,40,1,3,  6,9,12,	0,15,0,1,	3,37,1,3,	3,40,1,3,    7,41,38,	0,35,0,1,	0,19,0,1,	0,10,0,1,  8,42,39,	0,36,0,1,	0,20,0,1,	0,11,0,1,  9,40,37,	0,34,0,1,	0,21,0,1,	0,12,0,1,    10,19,22,	0,25,0,1,	0,16,0,1,	0,13,0,1,  11,20,23,	0,26,0,1,	0,17,0,1,	0,14,0,1,  12,21,24,	0,27,2,1,	0,18,0,1,	0,15,0,1,   19,35,32,	0,47,0,1,	0,43,0,1,	0,22,0,1,  20,36,33,	0,48,0,1,	0,44,0,1,	0,23,0,1,  21,34,31,	0,46,0,1,	0,45,0,1,	0,24,0,1,    22,43,46,	0,31,0,1,	0,28,0,1,	0,25,0,1,  23,44,47,	0,32,0,1,	0,29,0,1,	0,26,0,1,  24,45,48,	0,33,0,1,	0,30,0,1,	0,27,0,1,    16,25,28,	1,16,1,3,	1,25,1,3,	1,28,1,3,  17,26,29,	2,16,1,3,	2,25,1,3,	2,28,1,3,  18,27,30,	3,16,1,3,	3,25,1,3,	3,28,1,3,   28,31,34,	0,37,0,1,	1,13,1,3,	1,16,1,3,  29,32,35,	0,38,0,1,	2,13,1,3,	2,16,1,3, 30,33,36,	0,39,0,1,	3,13,1,3,	3,16,1,3,    43,47,44,	0,48,0,1,	0,45,0,1,	0,46,0,1,};
				dat = drdata44;zat = zhdata44;
			}
			else if(x==2 && y==1 || x==1 && y==2)
			{	int[] drdata21 = {	7,1,	1,4,1,0,3,	2,5,2,0,3,	3,6,3,0,3,	4,6,1,1,3,	5,4,2,1,3,	6,5,3,1,3,	4,5,0,2,3,};
				int[] zhdata21 = {3,  5,7,4,	1,4,1,3,	0,1,0,1,	2,1,1,3,  6,7,5,	2,4,1,3,	0,2,0,1,	3,1,1,3,	 4,7,6,	3,4,1,3,	0,3,0,1,	1,1,1,3  };
				dat = drdata21;zat = zhdata21;
			}
			else if(x==3 && y==1 || x==1 && y==3)
			{	int[] drdata31 = {	13,2,	1,4,1,0,6,	2,6,2,0,6,	3,8,3,0,6,	4,9,1,1,6,	6,5,2,1,6,	8,7,3,1,6,	9,8,1,2,6,	5,4,2,2,6,	7,6,3,2,6,	4,5,0,5,6,	6,7,0,1,6,	8,9,0,3,6,	5,7,0,5,6};
				int[] zhdata31 = {6,  8,10,4,	1,7,1,3,	0,1,0,1,	2,1,1,3,  9,11,5,	2,7,1,3,	0,2,0,1,	3,1,1,3,	 7,12,6,	3,7,1,3,	0,3,0,1,	1,1,1,3,	 4,10,13,	0,12,0,1,	0,7,0,1,	1,4,1,3, 5,11,13,	0,10,0,1,	0,8,0,1,	2,4,1,3,	6,12,13,	0,11,0,1,	0,9,0,1,	3,4,1,3 };
				dat = drdata31;zat = zhdata31;
			}
			else if(x==4 && y==1 || x==1 && y==4)
			{	int[] drdata41 = {	21,3,	1,4,1,0,9,	2,7,2,0,9,	3,10,3,0,9,	4,12,1,1,9,	7,6,2,1,9,	10,9,3,1,9,	12,11,1,2,9,	6,5,2,2,9,	9,8,3,2,9,	11,10,1,3,9,	5,4,2,3,9,	8,7,3,3,9,	4,5,0,8,9,	7,8,0,2,9,	10,11,0,5,9,	5,13,0,8,9,	8,13,0,2,9,	11,13,0,5,9,	12,13,0,7,9,	6,13,0,1,9,	9,13,0,4,9};
				int[] zhdata41 = {10,  11,13,4,	1,10,1,3,	0,1,0,1,	2,1,1,3,  12,14,5,	2,10,1,3,	0,2,0,1,	3,1,1,3,  10,15,6,	3,10,1,3,	0,3,0,1,	1,1,1,3,	    4,13,16,	0,19,0,1,	0,7,0,1,	1,7,1,3,  5,14,17,	0,20,0,1,	0,8,0,1,	2,7,1,3,  6,15,18,	0,21,0,1,	0,9,0,1,	3,7,1,3,    7,19,18,	0,15,0,1,	0,10,0,1,	1,4,1,3,  8,20,16,	0,13,0,1,	0,11,0,1,	2,4,1,3,  9,21,17,	0,14,0,1,	0,12,0,1,	3,4,1,3,    16,20,17,	0,21,0,1,	0,18,0,1,	0,19,0,1  };
				dat = drdata41;zat = zhdata41;
			}
			else if(x==4 && y==3 || x==3 && y==4)
			{	int[] drdata43 = {37,5,  1,4,1,0,15,  2,9,2,0,15,  3,14,3,0,15,	  4,18,1,1,15,  9,8,2,1,15,  14,13,3,1,15,    18,17,1,1,15,  8,7,2,1,15,  13,12,3,1,15,    17,16,1,3,15,  7,6,2,3,15,  12,11,3,3,15,    16,15,1,3,15,  6,5,2,3,15,  11,10,3,3,15,    15,14,1,5,15,  5,4,2,5,15,  10,9,3,5,15,    18,19,0,13,15,  8,20,0,3,15,  13,21,0,8,15,    19,16,0,13,15,  20,6,0,3,15,  21,11,0,8,15,    16,13,0,11,15,  6,18,0,1,15,  11,8,0,6,15,    13,14,0,11,15,  18,4,0,1,15,  8,9,0,6,15,  	  19,21,0,12,15,  20,19,0,2,15,  21,20,0,7,15,    16,21,0,9,15,  6,19,0,14,15,  11,20,0,4,15,    19,20,0,17,20};
				int[] zhdata43 = {18,  17,29,4,	1,16,1,3,	0,1,0,1,	2,1,1,3,  18,30,5,	2,16,1,3,	0,2,0,1,	3,1,1,3,  16,28,6,	3,16,1,3,	0,3,0,1,	1,1,1,3,	   4,29,26,	0,35,0,1,	0,19,0,1,	0,7,0,1,  5,30,27,	0,36,0,1,	0,20,0,1,	0,8,0,1,  6,28,25,	0,34,0,1,	0,21,0,1,	0,9,0,1,    7,19,22,	0,10,0,1,	1,10,1,3,	1,13,1,3,  8,20,23,	0,11,0,1,	2,10,1,3,	2,13,1,3,  9,21,24,	0,12,0,1,	3,10,1,3,	3,13,1,3,    10,22,31,	0,34,0,1,	0,25,0,1,	0,13,0,1,	  11,23,32,	0,35,0,1,	0,26,0,1,	0,14,0,1,  12,24,33,	0,36,0,1,	0,27,0,1,	0,15,0,1,    13,25,28,	0,16,0,1,	1,4,1,3,	1,7,1,3,  14,26,29,	0,17,0,1,	2,4,1,3,	2,7,1,3,  15,27,30,	0,18,0,1,	3,4,1,3,	3,7,1,3,    19,35,32,	0,37,0,1,	0,31,0,1,	0,22,0,1,  20,36,33,	0,37,0,1,	0,32,0,1,	0,23,0,1,	  21,34,31,	0,37,0,1,	0,33,0,1,	0,24,0,1	};
				dat = drdata43;zat = zhdata43;
			}
			else if(x==3 && y==2 || x==2&& y==3)
			{	int[] drdata32 = {19,3,1,4,1,0,9,2,7,2,0,9,3,10,3,0,9,4,12,1,1,9,7,6,2,1,9,10,9,3,1,9,12,11,1,1,9,6,5,2,1,9,9,8,3,1,9,11,10,1,3,9, 5,4,2,3,9,8,7,3,3,9,4,5,0,8,9,7,8,0,2,9,10,11,0,5,9,5,6,0,8,9,8,9,0,2,9,11,12,0,5,9,6,9,0,8,9};
				int[] zhdata32 = {9,  11,13,4,	1,10,1,3,	0,1,0,1,	2,1,1,3,	 12,14,5,	2,10,1,3,	0,2,0,1,	3,1,1,3,	 10,15,6,	3,10,1,3,	0,3,0,1,	1,1,1,3,  4,13,16,	0,19,0,1,	0,18,0,1,	0,7,0,1,	 5,14,17,	0,19,0,1,	0,16,0,1,	0,8,0,1,	 6,15,18,	0,19,0,1,	0,17,0,1,	0,9,0,1,  7,18,15,	0,10,0,1,	1,4,1,3,	1,7,1,3, 8,16,13,	0,11,0,1,	2,4,1,3,	2,7,1,3, 9,17,14,	0,12,0,1,	3,4,1,3,	3,7,1,3 };
				dat = drdata32;zat = zhdata32;
			}
			else if(x==4 && y==2 || x==2 && y==4)
			{	int[] drdata42 = {28,4,  1,4,1,0,12,  2,8,2,0,12,  3,12,3,0,12,	  4,15,1,1,12,  8,7,2,1,12,	  12,11,3,1,12,    15,14,1,1,12,  7,6,2,1,12,  11,10,3,1,12,    15,16,0,10,12,  7,17,0,2,12,  11,18,0,6,12,    14,16,0,9,12,  6,17,0,1,12,  10,18,0,5,12,    16,11,0,9,12,  17,15,0,1,12,  18,7,0,5,12,    11,12,0,9,12,  15,4,0,1,12,  7,8,0,5,12,    13,12,1,4,12,  5,4,2,4,12,  9,8,3,4,12,    15,17,0,12,15,  7,18,0,13,15,  11,16,0,14,15,    16,17,0,14,15};
				int[] zhdata42 = {15,  23,20,4,	1,22,1,3,	0,1,0,1,	2,1,1,3,  24,21,5,	2,22,1,3,	0,2,0,1,	3,1,1,3,  22,19,6,	3,22,1,3,	0,3,0,1,	1,1,1,3,    4,20,17,	0,25,0,1,	0,10,0,1,	0,7,0,1,	  5,21,18,	0,26,0,1,	0,11,0,1,	0,8,0,1,  6,19,16,	0,27,0,1,	0,12,0,1,	0,9,0,1,    7,10,13,	1,7,1,3,	1,10,1,3,	1,13,1,3,	  8,11,14,	2,7,1,3,	2,10,1,3,	2,13,1,3,  9,12,15,	3,7,1,3,	3,10,1,3,	3,13,1,3,    13,16,19,	0,22,0,1,	1,4,1,3,	1,7,1,3,  14,17,20,	0,23,0,1,	2,4,1,3,	2,7,1,3,  15,18,21,	0,24,0,1,	3,4,1,3,	3,7,1,3,    10,25,28,	0,27,0,1,	0,16,0,1,	0,13,0,1,	  11,26,28,	0,25,0,1,	0,17,0,1,	0,14,0,1,  12,27,28,	0,26,0,1,	0,18,0,1,	0,15,0,1};
				dat = drdata42;zat = zhdata42;
			}
			aantalDrPerFacet=dat[0];
			aantalZhPerFacet=zat[0];
			
			dr = new Driehoek[21][aantalDrPerFacet+1];
			zh = new Zeshoek[21][aantalZhPerFacet+1];
			
			if(x<y)
			{	b.spiegel();
				for(int i=1 ; i<21 ; i++)
				{	d[i].maakPuntenG(x,y);
				}
				for(int i=1 ; i<21 ; i++)
				{	for(int j=0 ; j<dat[0] ; j++)
					{	dr[i][j+1] = new Driehoek(d[i].punten[dat[3+5*j]],d[i].punten[dat[2+5*j]], 	d[d[i].brnrs[dat[4+5*j]]].punten[4+(dat[1]*d[i].brsta[dat[4+5*j]]+dat[5+5*j])%dat[6+5*j]]);
						dr[i][j+1].maakPuntenF(fOpBol);
					}
				}
				for(int i=1 ; i<21 ; i++)
				{	for(int j=0 ; j<zat[0] ; j++)
					{	zh[i][j+1] = new Zeshoek(dr[i][zat[1+15*j]].punten[4],
												 dr[d[i].brnrs[zat[12+15*j]]][zat[13+15*j]+(d[i].brsta[zat[12+15*j]]+zat[14+15*j])%zat[15+15*j]].punten[4],
												 dr[d[i].brnrs[zat[8+15*j]]][zat[9+15*j]+(d[i].brsta[zat[8+15*j]]+zat[10+15*j])%zat[11+15*j]].punten[4],
												 dr[d[i].brnrs[zat[4+15*j]]][zat[5+15*j]+(d[i].brsta[zat[4+15*j]]+zat[6+15*j])%zat[7+15*j]].punten[4],
												 dr[i][zat[3+15*j]].punten[4],
												 dr[i][zat[2+15*j]].punten[4]);
					}
				}
				maakVijfhoeken();
			}
			else
			{	for(int i=1 ; i<21 ; i++)
				{	d[i].maakPuntenG(x,y);
				}
				for(int i=1 ; i<21 ; i++)
				{	for(int j=0 ; j<dat[0] ; j++)
					{	dr[i][j+1] = new Driehoek(d[i].punten[dat[2+5*j]], d[i].punten[dat[3+5*j]],	d[d[i].brnrs[dat[4+5*j]]].punten[4+(dat[1]*d[i].brsta[dat[4+5*j]]+dat[5+5*j])%dat[6+5*j]]);
						dr[i][j+1].maakPuntenF(fOpBol);
					}
				}
						
				for(int i=1 ; i<21 ; i++)
				{	for(int j=0 ; j<zat[0] ; j++)
					{	zh[i][j+1] = new Zeshoek(dr[i][zat[1+15*j]].punten[4],
												 dr[i][zat[2+15*j]].punten[4],
												 dr[i][zat[3+15*j]].punten[4],
												 dr[d[i].brnrs[zat[4+15*j]]][zat[5+15*j]+(d[i].brsta[zat[4+15*j]]+zat[6+15*j])%zat[7+15*j]].punten[4],
												 dr[d[i].brnrs[zat[8+15*j]]][zat[9+15*j]+(d[i].brsta[zat[8+15*j]]+zat[10+15*j])%zat[11+15*j]].punten[4],
												 dr[d[i].brnrs[zat[12+15*j]]][zat[13+15*j]+(d[i].brsta[zat[12+15*j]]+zat[14+15*j])%zat[15+15*j]].punten[4]);
					}
				}
				maakVijfhoeken();
			}
		}
	}
	public void setState(Hashtable h)
	{
		String[][] drKleuren = null;
		String[][] zhKleuren = null;
		
		if(h.containsKey("drKleuren")) drKleuren = (String[][])h.get("drKleuren");
		if(h.containsKey("zhKleuren")) zhKleuren = (String[][])h.get("zhKleuren");
		
		if(drKleuren==null) return;
		for(int i=1 ; i<21 ; i++)
		{	for(int j=1; j<aantalDrPerFacet+1 ; j++)
			{	dr[i][j].vulkleur = drKleuren[i][j];
				dr[i][j].vorigeVulkleur = drKleuren[i][j];
			}
			for(int j=1; j<aantalZhPerFacet+1 ; j++)
			{	zh[i][j].vulkleur = zhKleuren[i][j];
				zh[i][j].vorigeVulkleur = zhKleuren[i][j];
			}
		}
		for(int i=1 ; i<13 ; i++)
		{	zh[i][0].vulkleur = zhKleuren[i][0];
			zh[i][0].vorigeVulkleur = zhKleuren[i][0];
		}
	}
	
	public Hashtable getState()
	{
		String[][] drKleuren = null;
		String[][] zhKleuren = null;
		
		drKleuren = new String[21][aantalDrPerFacet+1];
		zhKleuren = new String[21][aantalZhPerFacet+1];
		for(int i=1 ; i<21 ; i++)
		{	for(int j=1; j<aantalDrPerFacet+1 ; j++)
			{	 drKleuren[i][j] = dr[i][j].vulkleur;
			}
			for(int j=1; j<aantalZhPerFacet+1 ; j++)
			{	 zhKleuren[i][j] = zh[i][j].vulkleur;
			}
		}
		for(int i=1 ; i<13 ; i++)
		{	zhKleuren[i][0] = zh[i][0].vulkleur;
		}
		
		Hashtable h = new Hashtable();
		h.put("drKleuren", drKleuren);
		h.put("zhKleuren", zhKleuren);
		
		return h;
	}
	void bolop(double factor)
	{	for(int i=1 ; i<21 ; i++)
		{		for(int k=4 ; k<d[i].aantalPunten + 1 ; k++)
				{	double rNieuw = Math.sqrt(d[i].punten[k].x*d[i].punten[k].x + d[i].punten[k].y*d[i].punten[k].y + d[i].punten[k].z*d[i].punten[k].z);
					double rOud = d[i].r[k];  
					double f = (rOud+factor*(1-rOud)/10)/rNieuw;			 
					d[i].punten[k].x = d[i].punten[k].x*f ; d[i].punten[k].y = d[i].punten[k].y*f ; d[i].punten[k].z = d[i].punten[k].z*f;
				}
		}
	}
	void maakVijfhoeken()
	{	if(x<y)
		{	zh[1][0] = new Zeshoek(dr[1][1].punten[4],dr[5][1].punten[4],dr[4][1].punten[4],dr[3][1].punten[4],dr[3][1].punten[4],dr[2][1].punten[4]);
			zh[2][0] = new Zeshoek(dr[6][1].punten[4],dr[15][1].punten[4],dr[10][3].punten[4],dr[5][3].punten[4],dr[5][3].punten[4],dr[1][2].punten[4]);
			zh[3][0] = new Zeshoek(dr[7][1].punten[4],dr[11][1].punten[4],dr[6][3].punten[4],dr[1][3].punten[4],dr[1][3].punten[4],dr[2][2].punten[4]);
			zh[4][0] = new Zeshoek(dr[8][1].punten[4],dr[12][1].punten[4],dr[7][3].punten[4],dr[2][3].punten[4],dr[2][3].punten[4],dr[3][2].punten[4]);
			zh[5][0] = new Zeshoek(dr[9][1].punten[4],dr[13][1].punten[4],dr[8][3].punten[4],dr[3][3].punten[4],dr[3][3].punten[4],dr[4][2].punten[4]);
			zh[6][0] = new Zeshoek(dr[10][1].punten[4],dr[14][1].punten[4],dr[9][3].punten[4],dr[4][3].punten[4],dr[4][3].punten[4],dr[5][2].punten[4]);
			zh[7][0] = new Zeshoek(dr[16][1].punten[4],dr[20][3].punten[4],dr[15][3].punten[4],dr[6][2].punten[4],dr[6][2].punten[4],dr[11][2].punten[4]);
			zh[8][0] = new Zeshoek(dr[17][1].punten[4],dr[16][3].punten[4],dr[11][3].punten[4],dr[7][2].punten[4],dr[7][2].punten[4],dr[12][2].punten[4]);
			zh[9][0] = new Zeshoek(dr[18][1].punten[4],dr[17][3].punten[4],dr[12][3].punten[4],dr[8][2].punten[4],dr[8][2].punten[4],dr[13][2].punten[4]);
			zh[10][0] = new Zeshoek(dr[19][1].punten[4],dr[18][3].punten[4],dr[13][3].punten[4],dr[9][2].punten[4],dr[9][2].punten[4],dr[14][2].punten[4]);
			zh[11][0] = new Zeshoek(dr[20][1].punten[4],dr[19][3].punten[4],dr[14][3].punten[4],dr[10][2].punten[4],dr[10][2].punten[4],dr[15][2].punten[4]);
			zh[12][0] = new Zeshoek(dr[20][2].punten[4],dr[16][2].punten[4],dr[17][2].punten[4],dr[18][2].punten[4],dr[18][2].punten[4],dr[19][2].punten[4]);
		}
		else
		{	zh[1][0] = new Zeshoek(dr[1][1].punten[4],dr[2][1].punten[4],dr[3][1].punten[4],dr[3][1].punten[4],dr[4][1].punten[4],dr[5][1].punten[4]);
			zh[2][0] = new Zeshoek(dr[6][1].punten[4],dr[1][2].punten[4],dr[5][3].punten[4],dr[5][3].punten[4],dr[10][3].punten[4],dr[15][1].punten[4]);
			zh[3][0] = new Zeshoek(dr[7][1].punten[4],dr[2][2].punten[4],dr[1][3].punten[4],dr[1][3].punten[4],dr[6][3].punten[4],dr[11][1].punten[4]);
			zh[4][0] = new Zeshoek(dr[8][1].punten[4],dr[3][2].punten[4],dr[2][3].punten[4],dr[2][3].punten[4],dr[7][3].punten[4],dr[12][1].punten[4]);
			zh[5][0] = new Zeshoek(dr[9][1].punten[4],dr[4][2].punten[4],dr[3][3].punten[4],dr[3][3].punten[4],dr[8][3].punten[4],dr[13][1].punten[4]);
			zh[6][0] = new Zeshoek(dr[10][1].punten[4],dr[5][2].punten[4],dr[4][3].punten[4],dr[4][3].punten[4],dr[9][3].punten[4],dr[14][1].punten[4]);
			zh[7][0] = new Zeshoek(dr[16][1].punten[4],dr[11][2].punten[4],dr[6][2].punten[4],dr[6][2].punten[4],dr[15][3].punten[4],dr[20][3].punten[4]);
			zh[8][0] = new Zeshoek(dr[17][1].punten[4],dr[12][2].punten[4],dr[7][2].punten[4],dr[7][2].punten[4],dr[11][3].punten[4],dr[16][3].punten[4]);
			zh[9][0] = new Zeshoek(dr[18][1].punten[4],dr[13][2].punten[4],dr[8][2].punten[4],dr[8][2].punten[4],dr[12][3].punten[4],dr[17][3].punten[4]);
			zh[10][0] = new Zeshoek(dr[19][1].punten[4],dr[14][2].punten[4],dr[9][2].punten[4],dr[9][2].punten[4],dr[13][3].punten[4],dr[18][3].punten[4]);
			zh[11][0] = new Zeshoek(dr[20][1].punten[4],dr[15][2].punten[4],dr[10][2].punten[4],dr[10][2].punten[4],dr[14][3].punten[4],dr[19][3].punten[4]);
			zh[12][0] = new Zeshoek(dr[20][2].punten[4],dr[19][2].punten[4],dr[18][2].punten[4],dr[18][2].punten[4],dr[17][2].punten[4],dr[16][2].punten[4]);
		}
	}
}

class Zeshoek
{
	Punt3D[] punten, stappen;
	public String vulkleur,lijnkleur,vorigeVulkleur,vorigeLijnkleur;
		
	public Zeshoek(Punt3D p1,Punt3D p2,Punt3D p3,Punt3D p4,Punt3D p5,Punt3D p6)
	{	vulkleur = "oranje";
		lijnkleur = "zwart";
		vorigeVulkleur = "oranje";
		vorigeLijnkleur = "zwart";
		punten = new Punt3D[7];
		punten[1] = p1;
		punten[2] = p2;
		punten[3] = p3;
		punten[4] = p4;
		punten[5] = p5;
		punten[6] = p6;
		
		stappen = new Punt3D[7];
		maakBasis();
	}
	public void maakBasis()
	{
		stappen[1] = new Punt3D(punten[6].x-punten[1].x, punten[6].y-punten[1].y, punten[6].z-punten[1].z);
		stappen[2] = new Punt3D(punten[5].x-punten[6].x, punten[5].y-punten[6].y, punten[5].z-punten[6].z);
		stappen[3] = new Punt3D(punten[4].x-punten[5].x, punten[4].y-punten[5].y, punten[4].z-punten[5].z);
		stappen[4] = new Punt3D(punten[3].x-punten[4].x, punten[3].y-punten[4].y, punten[3].z-punten[4].z);
		stappen[5] = new Punt3D(punten[2].x-punten[3].x, punten[2].y-punten[3].y, punten[2].z-punten[3].z);
		stappen[6] = new Punt3D(punten[1].x-punten[2].x, punten[1].y-punten[2].y, punten[1].z-punten[2].z);
	}
}


class Driehoek
{
	int aantalPunten;
	double[] r;
	Punt3D basisx, basisy;
	Punt3D[] punten, stappen;
	public String vulkleur,lijnkleur,vorigeVulkleur,vorigeLijnkleur;
	public int[] brnrs, brsta;
	
	public Driehoek(Punt3D p1,Punt3D p2,Punt3D p3)
	{	vulkleur = "oranje";
		lijnkleur = "zwart";
		vorigeVulkleur = "oranje";
		vorigeLijnkleur = "zwart";
		aantalPunten=3;
		punten = new Punt3D[32];
		r = new double[32];
		punten[1] = p1;
		punten[2] = p2;
		punten[3] = p3;
		//r[1]=1;
		//r[2]=1;
		//r[3]=1;
		
		stappen = new Punt3D[4];
		maakBasis();
	}
	public void maakBasis()
	{	stappen[1] = new Punt3D(punten[3].x-punten[1].x, punten[3].y-punten[1].y, punten[3].z-punten[1].z);
		stappen[2] = new Punt3D(punten[2].x-punten[3].x, punten[2].y-punten[3].y, punten[2].z-punten[3].z);
		stappen[3] = new Punt3D(punten[1].x-punten[2].x, punten[1].y-punten[2].y, punten[1].z-punten[2].z);
		
		basisx = new Punt3D(-stappen[3].x, -stappen[3].y, -stappen[3].z);
		basisy = new Punt3D(stappen[1]);
	}
	public void geefBuren(int buurnr0,int buurnr1, int buurnr2, int buurnr3, int buursta0,int buursta1, int buursta2, int buursta3)	
	{	brnrs = new int[4]; brsta = new int[4];
		brnrs[0] = buurnr0;
		brnrs[1] = buurnr1;
		brnrs[2] = buurnr2;
		brnrs[3] = buurnr3;
		brsta[0] = buursta0;
		brsta[1] = buursta1;
		brsta[2] = buursta2;
		brsta[3] = buursta3;
	}
	void maakPuntenF(boolean fOpBol)
	{	punten[4] = new Punt3D(punten[1].x + 1*basisx.x/3 + 1*basisy.x/3, punten[1].y + 1*basisx.y/3 + 1*basisy.y/3, punten[1].z + 1*basisx.z/3 + 1*basisy.z/3 );
		double r = Math.sqrt(punten[4].x*punten[4].x + punten[4].y*punten[4].y + punten[4].z*punten[4].z);
		if(fOpBol)
		{punten[4].x = punten[4].x/r ; punten[4].y = punten[4].y/r ; punten[4].z = punten[4].z/r;}

	}
	
	void maakPuntenG(int x, int y)
	{	int mx;int my;int nx;int ny;
		if(x>y)	{mx = x; my = y; nx = -y; ny = x+y;}
		else	{mx = y; my = x; nx = -x; ny = x+y;}
		int det = mx*ny - my*nx;
		int mmx = ny;	int mmy = -my;	int nnx = -nx;	int nny = mx;
			
		int[] pcoord = {0};
		if(x==2 && y==0 || x==0 && y==2){int[] pcoord21 = {3,1,0,1,1,0,1};					pcoord = pcoord21;}
		if(x==3 && y==0 || x==0 && y==3){int[] pcoord21 = {7,1,0,2,0,2,1,1,2,0,2,0,1,1,1};	pcoord = pcoord21;}
		if(x==4 && y==0 || x==0 && y==4){int[] pcoord21 = {12,1,0,2,0,3,0,3,1,2,2,1,3,0,3,0,2,0,1,1,1,2,1,1,2};	pcoord = pcoord21;}
		if(x==1 && y==1 || x==1 && y==1){int[] pcoord21 = {1,0,1};							pcoord = pcoord21;}
		if(x==2 && y==2 || x==2 && y==2){int[] pcoord21 = {7,0,1,1,1,1,2,0,3,-1,3,-1,2,0,2};pcoord = pcoord21;}
		if(x==3 && y==3 || x==3 && y==3){int[] pcoord21 = {16,0,1,1,1,1,2,2,2,2,3,1,4,0,4,-1,5,-2,5,-2,4,-1,3,-1,2,0,2,1,3,-1,4,0,3};pcoord = pcoord21;}
		if(x==4 && y==4 || x==4 && y==4){int[] pcoord21 = {28,0,1,1,1,1,2,2,2,2,3,3,3,3,4,2,5,1,5,0,6,-1,6,-2,7,-3,7,-3,6,-2,5,-2,4,-1,3,-1,2,0,2,1,3,2,4,0,5,-2,6,-1,4,0,3,1,4,-1,5,0,4};pcoord = pcoord21;}
		if(x==2 && y==1 || x==1 && y==2){int[] pcoord21 = {3,0,1,1,1,0,2};					pcoord = pcoord21;}
		if(x==3 && y==1 || x==1 && y==3){int[] pcoord21 = {6,0,1,1,1,2,1,1,2,0,3,0,2};		pcoord = pcoord21;}
		if(x==3 && y==2 || x==2 && y==3){int[] pcoord21 = {9,0,1,1,1,1,2,2,2,1,3,0,3,-1,4,-1,3,0,2};		pcoord = pcoord21;}
		if(x==4 && y==1 || x==1 && y==4){int[] pcoord21 = {10,0,1,1,1,2,1,3,1,2,2,1,3,0,4,0,3,0,2,1,2};		pcoord = pcoord21;}
		if(x==4 && y==3 || x==3 && y==4){int[] pcoord21 = {18,0,1,1,1,1,2,2,2,2,3,3,3,2,4,1,4,0,5,-1,5,-2,6,-2,5,-1,4,-1,3,0,2,0,3,1,3,0,4};		pcoord = pcoord21;}
		if(x==4 && y==2 || x==2 && y==4){int[] pcoord21 = {15,0,1,1,1,2,1,2,2,3,2,2,3,1,4,0,4,-1,5,-1,4,-1,3,0,2,0,3,1,2,1,3};		pcoord = pcoord21;}
		double a; double b;
		aantalPunten = pcoord[0]+3;
		for(int i=0 ; i<pcoord[0] ; i++)
		{	a = 1.0*(mmx*pcoord[1+2*i] + nnx*pcoord[2+2*i])/det;
			b = 1.0*(mmy*pcoord[1+2*i] + nny*pcoord[2+2*i])/det;
			punten[4+i] = new Punt3D(punten[1].x + a*basisx.x + b*basisy.x, punten[1].y + a*basisx.y + b*basisy.y, punten[1].z + a*basisx.z + b*basisy.z);
			r[4+i] = Math.sqrt(punten[4+i].x*punten[4+i].x + punten[4+i].y*punten[4+i].y + punten[4+i].z*punten[4+i].z);
			
			punten[4+i].x = punten[4+i].x/r[4+i] ; punten[4+i].y = punten[4+i].y/r[4+i] ; punten[4+i].z = punten[4+i].z/r[4+i];
		}
	}
}
class BasisIco
{		
	double pi, theta, ct, st,k;
	Punt3D[] icosaederPunten;
	Driehoek[] d;

	public BasisIco(double k)
	{	this.k = k;
		d = new Driehoek[21];
		pi = Math.PI;
		ct = (Math.cos(2*pi/5))/(1-Math.cos(2*pi/5));
		theta = Math.acos(ct);
		st = Math.sin(theta);
		
		icosaederPunten = new Punt3D[13];
		icosaederPunten[1] = new Punt3D(0,1,0);
		icosaederPunten[2] = new Punt3D(st,ct,0);
		icosaederPunten[3] = new Punt3D(st*Math.cos(2*pi/5), ct, st*Math.sin(2*pi/5));
		icosaederPunten[4] = new Punt3D(st*Math.cos(4*pi/5), ct, st*Math.sin(4*pi/5));
		icosaederPunten[5] = new Punt3D(st*Math.cos(6*pi/5), ct, st*Math.sin(6*pi/5));
		icosaederPunten[6] = new Punt3D(st*Math.cos(8*pi/5), ct, st*Math.sin(8*pi/5));
		icosaederPunten[7] = new Punt3D(st*Math.cos(1*pi/5), -ct,st*Math.sin(1*pi/5));
		icosaederPunten[8] = new Punt3D(st*Math.cos(3*pi/5), -ct, st*Math.sin(3*pi/5));
		icosaederPunten[9] = new Punt3D(st*Math.cos(5*pi/5), -ct, st*Math.sin(5*pi/5));
		icosaederPunten[10] = new Punt3D(st*Math.cos(7*pi/5), -ct, st*Math.sin(7*pi/5));
		icosaederPunten[11] = new Punt3D(st*Math.cos(9*pi/5), -ct, st*Math.sin(9*pi/5));
		icosaederPunten[12] = new Punt3D(0, -1, 0);
		
		d[1] = new Driehoek(icosaederPunten[1],icosaederPunten[2],icosaederPunten[3]);	
		d[2] = new Driehoek(icosaederPunten[1],icosaederPunten[3],icosaederPunten[4]);	
		d[3] = new Driehoek(icosaederPunten[1],icosaederPunten[4],icosaederPunten[5]);	
		d[4] = new Driehoek(icosaederPunten[1],icosaederPunten[5],icosaederPunten[6]);	
		d[5] = new Driehoek(icosaederPunten[1],icosaederPunten[6],icosaederPunten[2]);	
		d[6] = new Driehoek(icosaederPunten[2],icosaederPunten[7],icosaederPunten[3]);	
		d[7] = new Driehoek(icosaederPunten[3],icosaederPunten[8],icosaederPunten[4]);	
		d[8] = new Driehoek(icosaederPunten[4],icosaederPunten[9],icosaederPunten[5]);	
		d[9] = new Driehoek(icosaederPunten[5],icosaederPunten[10],icosaederPunten[6]);	
		d[10] = new Driehoek(icosaederPunten[6],icosaederPunten[11],icosaederPunten[2]);	
		d[11] = new Driehoek(icosaederPunten[3],icosaederPunten[7],icosaederPunten[8]);	
		d[12] = new Driehoek(icosaederPunten[4],icosaederPunten[8],icosaederPunten[9]);	
		d[13] = new Driehoek(icosaederPunten[5],icosaederPunten[9],icosaederPunten[10]);	
		d[14] = new Driehoek(icosaederPunten[6],icosaederPunten[10],icosaederPunten[11]);
		d[15] = new Driehoek(icosaederPunten[2],icosaederPunten[11],icosaederPunten[7]);	
		d[16] = new Driehoek(icosaederPunten[7],icosaederPunten[12],icosaederPunten[8]);	
		d[17] = new Driehoek(icosaederPunten[8],icosaederPunten[12],icosaederPunten[9]);	
		d[18] = new Driehoek(icosaederPunten[9],icosaederPunten[12],icosaederPunten[10]);
		d[19] = new Driehoek(icosaederPunten[10],icosaederPunten[12],icosaederPunten[11]);
		d[20] = new Driehoek(icosaederPunten[11],icosaederPunten[12],icosaederPunten[7]);
		
		d[1].geefBuren(1,2,5,6,0,0,2,2);
		d[2].geefBuren(2,3,1,7,0,0,2,2);
		d[3].geefBuren(3,4,2,8,0,0,2,2);
		d[4].geefBuren(4,5,3,9,0,0,2,2);
		d[5].geefBuren(5,1,4,10,0,0,2,2);
		
		d[6].geefBuren(6,1,15,11,0,1,2,0);
		d[7].geefBuren(7,2,11,12,0,1,2,0);
		d[8].geefBuren(8,3,12,13,0,1,2,0);
		d[9].geefBuren(9,4,13,14,0,1,2,0);
		d[10].geefBuren(10,5,14,15,0,1,2,0);
		
		d[11].geefBuren(11,7,6,16,0,0,1,2);
		d[12].geefBuren(12,8,7,17,0,0,1,2);
		d[13].geefBuren(13,9,8,18,0,0,1,2);
		d[14].geefBuren(14,10,9,19,0,0,1,2);
		d[15].geefBuren(15,6,10,20,0,0,1,2);
		
		d[16].geefBuren(16,11,20,17,0,1,1,0);
		d[17].geefBuren(17,12,16,18,0,1,1,0);
		d[18].geefBuren(18,13,17,19,0,1,1,0);
		d[19].geefBuren(19,14,18,20,0,1,1,0);
		d[20].geefBuren(20,15,19,16,0,1,1,0);
	}
	public void spiegel()
	{	d[1].punten[1].x=-d[1].punten[1].x;
		d[1].punten[2].x=-d[1].punten[2].x;
		d[2].punten[2].x=-d[2].punten[2].x;
		d[3].punten[2].x=-d[3].punten[2].x;
		d[4].punten[2].x=-d[4].punten[2].x;
		d[5].punten[2].x=-d[5].punten[2].x;
		d[6].punten[2].x=-d[6].punten[2].x;
		d[7].punten[2].x=-d[7].punten[2].x;
		d[8].punten[2].x=-d[8].punten[2].x; 
		d[9].punten[2].x=-d[9].punten[2].x;
		d[10].punten[2].x=-d[10].punten[2].x;
		d[16].punten[2].x=-d[16].punten[2].x;
		for(int i=1 ; i<21 ; i++)
		{	d[i].maakBasis();
		}	
	}
}
	