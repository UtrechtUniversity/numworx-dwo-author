package fi.geodefull;

import java.awt.*;
import java.awt.event.*;
import fi.beans.grnuminput.*;

public class PlatoStulpProg extends TekenApplet3D implements ActionListener, NumberListener,ItemListener
{	
	Matrix3D matrot, matres,mateenh;
	double k, xhoek,yhoek,schaalDualIco;
	private boolean kubus,octaeder,tetraeder,icosaeder,dodecaeder;
	Veelvlak v1;
	Button draadKnop;
	NumberSlider afknotSl;
	Choice kiesV;
	int aantalKlikken = 0;
	boolean begin=true;
	boolean draad=false;
	String kleur;
	
	public void initialiseer()
	{	maakMuisActieMogelijk();
		maakAnimatieMogelijk();
		
		kiesV = new Choice();
		kiesV.addItemListener(this);
		kiesV.addItem("Tetraeder");
		kiesV.addItem("Kubus");
		kiesV.addItem("Octaeder");
		kiesV.addItem("Dodecaeder");
		kiesV.addItem("Icosaeder");
		rg.gridbag.setConstraints(kiesV, rg.c);
		rg.add(kiesV);
		rg.setBackground(Color.white);
		ab.setBackground(Color.white);
		afknotSl = new NumberSlider(0,100,0,0,"uitstulping","");
		afknotSl.addNumberListener(this);
		afknotSl.setValue(0);
		rg.gridbag.setConstraints(afknotSl, rg.c);
		rg.add(afknotSl);

		kubus = false;
		octaeder = false;
		tetraeder = true;
		icosaeder = false;
		dodecaeder = false;
		
		draadKnop = new Button("draadfiguur");
		draadKnop.addActionListener(this);
		rg.gridbag.setConstraints(draadKnop, rg.c);
		rg.add(draadKnop);
	
		k=245;
		matrot = new Matrix3D();
		v1 = new Tetraeder(0.85);
		geefBasiskleur(v1,"geel");
		
		
	}
	public void tekenprogramma()
	{	begindraai(15,-15);
		if(draad)
		{	kleurVeelvlak(v1,"transparant");
		}
		tekenVeelvlak(4,v1);
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
	void tekenVeelvlak(int n,Veelvlak vv)
	{	for(int i=0 ; i<vv.aantalVlakken ; i++)
		{	tekenVlak(n,vv.vlakken[i]);
		}
	}
	
	void kleurVeelvlak(Veelvlak v, String kl)
	{	for(int i=0 ; i<v.aantalVlakken ; i++)
		{	v.vlakken[i].vulkleur = kl;
		}
	}
	void geefBasiskleur(Veelvlak v, String kl)
	{	for(int i=0 ; i<v.aantalVlakken ; i++)
		{	v.vlakken[i].vulkleur = kl;
			v.vlakken[i].vorigeKleur = kl;
		}
	}
	
	void naarVorigeKleur()
	{	for(int i=0 ; i<v1.aantalVlakken ; i++)
		{	v1.vlakken[i].vulkleur = v1.vlakken[i].vorigeKleur;
		}
	}

	
	void tekenVlak(int n,Vlak v)
	{	penUit();
		stap(k*v.punten[0].x, k*v.punten[0].y, k*v.punten[0].z);
		if(!(v.lijnkleur=="transparant"))penAan("lichtgrijs");
		if(v.vulkleur=="transparant")vulAan(v.vulkleur);
		else vulAan("grijs");
		for(int i=v.aantalHoekpunten-1 ; i>-1 ; i--)
		{	int a=i ; int b=(i+1)%v.aantalHoekpunten;
			stap(k*(v.punten[a].x-v.punten[b].x), k*(v.punten[a].y-v.punten[b].y), k*(v.punten[a].z-v.punten[b].z));
		}
		vulUit();
		if(!(v.lijnkleur=="transparant"))penAan(v.lijnkleur);
		vulAan(n,v.vulkleur);
		for(int i=0 ; i<v.aantalHoekpunten ; i++)
		{	int a=i ; int b=(i+1)%v.aantalHoekpunten;
			stap(-k*(v.punten[a].x-v.punten[b].x), -k*(v.punten[a].y-v.punten[b].y), -k*(v.punten[a].z-v.punten[b].z));
		}
		vulUit(n);
		penUit();
		stap(-k*v.punten[0].x, -k*v.punten[0].y, -k*v.punten[0].z);
		
	}
	public void actionPerformed(ActionEvent e)
	{	boolean animatieWasAan=false;
		if(e.getSource()==draadKnop)
		{	if(draadKnop.getLabel()=="draadfiguur")
			{	draad=true;	
				draadKnop.setLabel("gevuld");
			}
			else 
			{	draad=false;
				naarVorigeKleur();
				draadKnop.setLabel("draadfiguur");
			}
		}
		
		if(animatieStatus())
		{	onderbreekAnimatie();
			animatieWasAan = true;
		}
		tekenOpnieuw();
		if(animatieWasAan)beginAnimatie();
	}
	public void numberChanged(String name,double val)
	{	boolean animatieWasAan = false;
		if(animatieStatus())
		{	animatieWasAan = true;
			onderbreekAnimatie();
		}
		if(name=="uitstulping")
		{	if(icosaeder)
			{	if(val==0)
				{	v1=new Icosaeder(0.85);
					geefBasiskleur(v1,"geel");
				}
				else if(val<100)
				{	
					v1=new DodecaederKnot(1.0,1-0.01*val);
					double x2 = v1.vlakken[0].x * v1.vlakken[0].x;
					double y2 = v1.vlakken[0].y * v1.vlakken[0].y;
					double z2 = v1.vlakken[0].z * v1.vlakken[0].z;
					double f = Math.sqrt(x2+y2+z2);
					v1=(new DodecaederKnot(1.17/f,1-0.01*val)).dualiseerb();
					geefBasiskleur(v1,"geel");
				}
				else if(val==100)
				{	v1=(new Icosidodecaeder(1.62)).dualiseerb();
					geefBasiskleur(v1,"geel");
				}
				
			}
			if(dodecaeder)
			{	if(val==0)
				{	v1 = (new Icosaeder(1.0)).dualiseer();
					geefBasiskleur(v1,"groen");
				}
				else if(val<100)
				{	v1=(new IcosaederKnot(1.58,0.01*val)).dualiseerb();
					geefBasiskleur(v1,"groen");
				}
				else if(val==100)
				{	v1=(new Icosidodecaeder(1.58)).dualiseerb();
					geefBasiskleur(v1,"groen");
				}
				
			}
			if(kubus)
			{	if(val==0)
				{	v1=new Kubus(0.85);
					geefBasiskleur(v1,"geel");
				}
				else if(val<100)
				{	v1=new OctaederKnot(1.0,1-0.01*val);
					double x2 = v1.vlakken[8].x * v1.vlakken[8].x;
					double y2 = v1.vlakken[8].y * v1.vlakken[8].y;
					double z2 = v1.vlakken[8].z * v1.vlakken[8].z;
					double f = Math.sqrt(x2+y2+z2);
					v1=(new OctaederKnot(1.175/f,1-0.01*val)).dualiseerb();
					geefBasiskleur(v1,"geel");
				}
				else if(val==100)
				{	v1=(new Kuboctaeder(1.76)).dualiseerb();
					geefBasiskleur(v1,"geel");
				}
			}
			if(octaeder)
			{	if(val==0)
				{	v1 = (new Kubus(1.5)).dualiseer();
					geefBasiskleur(v1,"groen");
				}
				else if(val<100)
				{	v1=(new KubusKnot(1.995,0.01*val)).dualiseerb();
					geefBasiskleur(v1,"groen");
				}
				else if(val==100)
				{	v1=(new Kuboctaeder(2.0)).dualiseerb();
					geefBasiskleur(v1,"groen");
				}
			}
			if(tetraeder)
			{	if(val==0)
				{	v1=new Tetraeder(0.85);
					geefBasiskleur(v1,"geel");
				}
				else if(val<100)
				{	v1=(new TetraederKnot2(3.53,0.01*val)).dualiseerb();
					geefBasiskleur(v1,"geel");
				}
				else if(val==100)
				{	v1=new Kubus(0.85);
					geefBasiskleur(v1,"geel");
				}
			}
		}
		if(name=="zijde")
		{	k = val;
		}
		if(!animatieStatus())tekenOpnieuw();
		if(animatieWasAan)beginAnimatie();
	}
	public void itemStateChanged(ItemEvent e)
	{	boolean animatieWasAan = false;
		if(animatieStatus())
		{	animatieWasAan = true;
			onderbreekAnimatie();
		}
		String soortV = kiesV.getSelectedItem();
		kubus = false;
		octaeder = false;
		tetraeder = false;	
		icosaeder = false;
		dodecaeder = false;	
		
		afknotSl.setInitValue();
		begin = true;
		//draad=false;
		//draadKnop.setLabel("draadfiguur");
		
		if (soortV=="Kubus")
		{	kubus=true ;
			v1 = new Kubus(0.85);
			geefBasiskleur(v1,"geel");
		}
		else if (soortV=="Octaeder")
		{	octaeder=true;
			v1 = (new Kubus(1.5)).dualiseer();
			geefBasiskleur(v1,"groen");
		}
		else if (soortV=="Icosaeder")
		{	icosaeder=true;
			v1=new Icosaeder(0.85);
			geefBasiskleur(v1,"geel");
		}
		else if (soortV=="Dodecaeder")
		{	dodecaeder=true;
			v1 = (new Icosaeder(1.0)).dualiseer();
			geefBasiskleur(v1,"groen");
		}
		else if (soortV=="Tetraeder")
		{	tetraeder=true;
			v1 = new Tetraeder(0.8);
			geefBasiskleur(v1,"geel");
		}
		if(!animatieStatus())tekenOpnieuw();
		if(animatieWasAan)beginAnimatie();
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
}

