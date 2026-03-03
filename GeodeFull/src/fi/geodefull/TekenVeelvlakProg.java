package fi.geodefull;

import java.awt.*;
import java.awt.event.*;
import fi.beans.grnuminput.*;

public class TekenVeelvlakProg extends TekenApplet3D implements ActionListener,ItemListener
{	
	Matrix3D matrot, matres,mateenh;
	double k, xhoek,yhoek;
	
	
	NumberSlider zijdeSl;
	Veelvlak v, tv;
	Punt[] trefpunten;
	boolean[] trefpuntRaak;
	int aantalHpNieuw;
	Hoekpunt[] hoekpuntenNieuw;
	Punt[] trefpuntenNieuw;
	boolean begin, basisZichtbaar,maakLijn, maakVlak;
	Button basisKnop, terugKnop, wisKnop, lijnKnop, vlakKnop;
	Choice kiesV;
	
	int aantalPuntenRood, puntnr1, puntnr2;
	int[] puntnr;	
	
	public void initialiseer()
	{	maakMuisActieMogelijk();
		//maakAnimatieMogelijk();
		//tb.zetAfstand(10000);
		
		kiesV = new Choice();
		kiesV.addItemListener(this);
		kiesV.addItem("Kubus");
		kiesV.addItem("Octaeder");
		kiesV.addItem("Icosaeder");
		kiesV.addItem("Dodecaeder");
		kiesV.addItem("Tetraeder");
		rg.gridbag.setConstraints(kiesV, rg.c);
		rg.add(kiesV);
		rg.setBackground(Color.white);
		
		lijnKnop = new Button("maak lijn");
		lijnKnop.addActionListener(this);
		rg.gridbag.setConstraints(lijnKnop, rg.c);
		rg.add(lijnKnop);
		lijnKnop.setBackground(Color.white);
		
		vlakKnop = new Button("maak vlak");
		vlakKnop.addActionListener(this);
		rg.gridbag.setConstraints(vlakKnop, rg.c);
		rg.add(vlakKnop);
		
		basisKnop = new Button("verberg basisfiguur");
		basisKnop.addActionListener(this);
		rg.gridbag.setConstraints(basisKnop, rg.c);
		rg.add(basisKnop);
		
		terugKnop = new Button("maak ongedaan");
		terugKnop.addActionListener(this);
		rg.gridbag.setConstraints(terugKnop, rg.c);
		rg.add(terugKnop);
		
		wisKnop = new Button("wis lijnen");
		wisKnop.addActionListener(this);
		rg.gridbag.setConstraints(wisKnop, rg.c);
		rg.add(wisKnop);
		
		matrot = new Matrix3D();
		matres = new Matrix3D();
		mateenh = new Matrix3D();
		tb.mat = matres;
		k=240;
		begin=true;
		basisZichtbaar=true;
		maakLijn=true;
		maakVlak=false;
		trefpunten = new Punt[500];
		trefpuntRaak = new boolean[500];
		wisTrefpunten();
		aantalHpNieuw = 0;
		hoekpuntenNieuw = new Hoekpunt[500];
		trefpuntenNieuw = new Punt[500];
		aantalPuntenRood = 0;
		puntnr = new int[10];
		
		v = new Kubus(1);
		for(int i=0 ; i<v.aantalVlakken ; i++)
		{	v.vlakken[i].vulkleur = "transparant";
		}
		tv = new Veelvlak(v);
		
		/*Vlak vl = new Vlak(3);
		
		vl.punten[0] = tv.hoekpunten[0];
		vl.punten[1] = tv.hoekpunten[1];
		vl.punten[2] = tv.hoekpunten[2];
						
		tv.vlakken[0] = vl;
		tv.aantalVlakken++;
		*/
	}
	public void tekenprogramma()
	{	begindraai(20,30);
		if(basisZichtbaar)tekenVeelvlak(2,v);
		
		tekenVeelvlak(1,tv);
		maakTrefpunten(tv);
	}
	void begindraai(double xdr,double ydr)
	{	if(begin)
		{	lijnKnop.setSize(100,25);
			basisKnop.setSize(100,25);
			terugKnop.setSize(100,25);
			wisKnop.setSize(100,25);
			vlakKnop.setSize(100,25);
			//rg.doLayout();
			
			matrot.initialiseer();
			matrot.ydraaiAbs(ydr);
			matrot.xdraaiAbs(xdr);
			tb.mat.mult(matrot);
			begin=false;
		}
	}
	void maakTrefpunten(Veelvlak v)
	{	for(int i=0 ; i<v.aantalHoekpunten ; i++)
		{	penUit();stap(k*v.hoekpunten[i].x, k*v.hoekpunten[i].y, k*v.hoekpunten[i].z);
			trefpunten[i] = geefPunt(1);
			if(trefpuntRaak[i])
			{	tb.mat = mateenh;
				stap(5,0);vulAan(1,"groen");stap(-5,-5);stap(-5,5);stap(5,5);stap(5,-5);vulUit(1);stap(-5,0);
				tb.mat = matres;
			}
			stap(-k*v.hoekpunten[i].x, -k*v.hoekpunten[i].y, -k*v.hoekpunten[i].z);penAan();
		}
		for(int i=0 ; i<aantalHpNieuw ; i++)
		{	penUit();stap(k*hoekpuntenNieuw[i].x, k*hoekpuntenNieuw[i].y, k*hoekpuntenNieuw[i].z);
			trefpuntenNieuw[i] = geefPunt(1);
			//if(v.trefpuntRaak[i])
			//{	tb.mat = mateenh;
			//	stap(3,0);vulAan(1,"rood");stap(-3,-3);stap(-3,3);stap(3,3);stap(3,-3);vulUit(1);stap(-3,0);
			//	tb.mat = matres;
			//}
			stap(-k*hoekpuntenNieuw[i].x, -k*hoekpuntenNieuw[i].y, -k*hoekpuntenNieuw[i].z);penAan();
		}
	}
	public void wisTrefpunten()
	{	for(int i=0 ; i<100 ; i++)
		{	trefpuntRaak[i]=false;
		}
	}
	void tekenVeelvlak(int n,Veelvlak vv)
	{	for(int i=0 ; i<vv.aantalVlakken ; i++)
		{	tekenVlak(n,vv.vlakken[i]);
		}
		for(int i=0 ; i<vv.aantalLijnen ; i++)
		{	tekenLijn(vv.lijnen[i]);
		}
	}
	void tekenLijn(Lijn l)
	{	penUit();
		stap(k*l.hpunt1.x, k*l.hpunt1.y, k*l.hpunt1.z);
		penAan(1,l.kleur);
		stap(k*l.hpunt2.x - k*l.hpunt1.x, k*l.hpunt2.y - k*l.hpunt1.y, k*l.hpunt2.z - k*l.hpunt1.z);
		penUit(1);
		stap(-k*l.hpunt2.x, -k*l.hpunt2.y, -k*l.hpunt2.z);
	}
	void tekenVlak(int n,Vlak v)
	{	penUit();
		stap(k*v.punten[0].x, k*v.punten[0].y, k*v.punten[0].z);
		if(!(v.lijnkleur=="transparant"))penAan("lichtgrijs");
		if(v.vulkleur=="transparant")
		{	if(n==2)vulAan(v.vulkleur);
			else if(n==1)vulAan(1,v.vulkleur);
		}
		else 
		{	if(n==2)vulAan("grijs");
			else if(n==1)vulAan(1,"grijs");
		}
		for(int i=v.aantalHoekpunten-1 ; i>-1 ; i--)
		{	int a=i ; int b=(i+1)%v.aantalHoekpunten;
			stap(k*(v.punten[a].x-v.punten[b].x), k*(v.punten[a].y-v.punten[b].y), k*(v.punten[a].z-v.punten[b].z));
		}
		if(n==2)vulUit();
		else if(n==1)vulUit(1);
		
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
	void maakSnijpunt(Lijn l1, Lijn l2)
	{	double ax = l1.hpunt1.x;double ay = l1.hpunt1.y;double az = l1.hpunt1.z;
		double bx = l1.hpunt2.x;double by = l1.hpunt2.y;double bz = l1.hpunt2.z;
		double cx = l2.hpunt1.x;double cy = l2.hpunt1.y;double cz = l2.hpunt1.z;
		double dx = l2.hpunt2.x;double dy = l2.hpunt2.y;double dz = l2.hpunt2.z;
		
		double a1 = bx-ax;double a2 = by-ay;double a3 = bz-az;
		double b1 = cx-dx;double b2 = cy-dy;double b3 = cz-dz;
		double c1 = cx-ax;double c2 = cy-ay;double c3 = cz-az;
		
		double d = a1*b2 - a2*b1;
		double k=0; 
		double m;
		double afwijking ;
		if(Math.abs(d)>0.0000001)
		{	k = (c1*b2-c2*b1)/d;
			m = (a1*c2-a2*c1)/d;
			afwijking = c3-(a3*k+b3*m);
		}
		else
		{	d = a2*b3 - a3*b2;
			if(Math.abs(d)>0.0000001)
			{	k = (c2*b3-c3*b2)/d;
				m = (a2*c3-a3*c2)/d;
				afwijking = c1-(a1*k+b1*m);
			}
			else
			{	d = a1*b3 - a3*b1;
				if(Math.abs(d)>0.0000001)
				{	k = (c1*b3-c3*b1)/d;
					m = (a1*c3-a3*c1)/d;
					afwijking = c2-(a2*k+b2*m);
				}
				else return;
				
			}
		}
		
		
		
		if(afwijking<0.00001 && afwijking>-0.00001 && k<1 && k>0 && m<1 && m>0)
		{	hoekpuntenNieuw[aantalHpNieuw] = new Hoekpunt(ax + k*(bx-ax) , ay + k*(by-ay) , az + k*(bz-az));
			aantalHpNieuw++;
			
			/*Hoekpunt[] hp = new Hoekpunt[tv.aantalHoekpunten+1];
			for(int i=0 ; i<tv.aantalHoekpunten ; i++)
			{	hp[i]=tv.hoekpunten[i];
			}
			hp[tv.aantalHoekpunten]= new Hoekpunt(ax + k*(bx-ax) , ay + k*(by-ay) , az + k*(bz-az));
			tv.hoekpunten = hp;
			tv.aantalHoekpunten++;*/
			
		}
		
	}
	void zoekSnijpunten()
	{	for(int i=0 ; i<tv.aantalLijnen-1 ; i++)
		{	maakSnijpunt(tv.lijnen[tv.aantalLijnen-1],tv.lijnen[i]);
		}
		
	}
	public void actionPerformed(ActionEvent e)
	{	
		boolean animatieWasAan=false;
		
		if(e.getSource()==basisKnop)
		{	if(basisKnop.getLabel()=="verberg basisfiguur")
			{	
				basisZichtbaar = false;
				basisKnop.setLabel("laat basisfiguur zien");
				
			}
			else
			{	basisZichtbaar = true;
				basisKnop.setLabel("verberg basisfiguur");
			}
		}	
		if(e.getSource()==terugKnop)
		{	aantalPuntenRood=0;
			wisTrefpunten();
			if(maakLijn)
			{	if(tv.aantalLijnen>0)tv.aantalLijnen--;
			}
			if(maakVlak)
			{	if(tv.aantalVlakken>0)tv.aantalVlakken--;
			}
		}
		if(e.getSource()==lijnKnop)
		{	maakLijn = true;
			maakVlak = false;
			lijnKnop.setBackground(Color.white);
			vlakKnop.setBackground(Color.lightGray);
			aantalPuntenRood=0;
			wisTrefpunten();
		}
		if(e.getSource()==vlakKnop)
		{	maakLijn = false;
			maakVlak = true;
			lijnKnop.setBackground(Color.lightGray);
			vlakKnop.setBackground(Color.white);
			aantalPuntenRood=0;
			wisTrefpunten();
		}
		if(e.getSource()==wisKnop)
		{	aantalPuntenRood=0;
			wisTrefpunten();
			tv.aantalLijnen = 0;
			aantalHpNieuw = 0;
		}
		if(animatieStatus())
		{	onderbreekAnimatie();
			animatieWasAan = true;
		}
		tekenOpnieuw();
		if(animatieWasAan)beginAnimatie();
	}
	public void itemStateChanged(ItemEvent e)
	{	boolean animatieWasAan=false;
		
		String soortV = kiesV.getSelectedItem();
		
		
		if (soortV=="Kubus")v = new Kubus(1);
		else if (soortV=="Octaeder")v = (new Kubus(Math.sqrt(3))).dualiseer();
		else if (soortV=="Icosaeder")v = new Icosaeder(1);
		else if (soortV=="Dodecaeder")v = (new Icosaeder(1.3)).dualiseer();
		else if (soortV=="Tetraeder")v = new Tetraeder(1);
		tv = new Veelvlak(v);
		
		for(int i=0 ; i<v.aantalVlakken ; i++)
			{	v.vlakken[i].vulkleur = "transparant";
			}
		aantalPuntenRood=0;
		wisTrefpunten();
		tv.aantalLijnen = 0;
		aantalHpNieuw = 0;
		if(animatieStatus())
		{	onderbreekAnimatie();
			animatieWasAan = true;
		}
		tekenOpnieuw();
		if(animatieWasAan)beginAnimatie();
	}

	public void muisDrukActie()
	{	boolean raak = false;
		int max = tv.aantalHoekpunten;
		for(int i=0 ; i<max ; i++)
		{	double ax = trefpunten[i].x - geefDrukx();
			double ay =  trefpunten[i].y - geefDruky();
			if((ax<4 && ax>-4)&&(ay<4 && ay>-4))
			{	raak = true;
				if(maakLijn)
				{	if(aantalPuntenRood==0)
					{	puntnr1 = i;
						aantalPuntenRood++;
						trefpuntRaak[i]=true;
					}
					else 
					{	puntnr2 = i;
						if(puntnr1!=puntnr2)tv.maakLijn(puntnr1,puntnr2,"rood");
						aantalPuntenRood=0;
						wisTrefpunten();
						trefpuntRaak[puntnr1]=false;
						if(tv.aantalLijnen>1)zoekSnijpunten();
					}
				}
				else if(maakVlak)
				{	if(aantalPuntenRood>0 && i==puntnr[aantalPuntenRood-1])
					{	wisTrefpunten();
						aantalPuntenRood=0;
					}
					else if(aantalPuntenRood>1 && i==puntnr[aantalPuntenRood-2])
					{	wisTrefpunten();
						aantalPuntenRood=0;
					}
					else if(i!=puntnr[0] || aantalPuntenRood==0)
					{	puntnr[aantalPuntenRood] = i;
						aantalPuntenRood++;
						trefpuntRaak[i]=true;
					}
					else
					{	puntnr[aantalPuntenRood] = i; 
						Vlak vl = new Vlak(aantalPuntenRood);
						for(int j=0 ; j<aantalPuntenRood ; j++)
						{	vl.punten[j] = tv.hoekpunten[puntnr[j]];
						}
						tv.vlakken[tv.aantalVlakken] = vl;
						tv.aantalVlakken++;
						aantalPuntenRood=0;
						wisTrefpunten();
					}
				}
				break;
			}
		}
		if(raak)
		{	tekenOpnieuw();
			return;
		}
		else
		{	raak=false; 
			max = aantalHpNieuw;
			for(int i=0 ; i<max ; i++)
			{	double ax = trefpuntenNieuw[i].x - geefDrukx();
				double ay = trefpuntenNieuw[i].y - geefDruky();
				if((ax<4 && ax>-4)&&(ay<4 && ay>-4))
				{	raak = true;
					
					/*Hoekpunt[] hp = new Hoekpunt[tv.aantalHoekpunten+1];
					for(int i=0 ; i<tv.aantalHoekpunten ; i++)
					{	hp[i]=tv.hoekpunten[i];
					}
					hp[tv.aantalHoekpunten]= new Hoekpunt(ax + k*(bx-ax) , ay + k*(by-ay) , az + k*(bz-az));
					tv.hoekpunten = hp;
					tv.aantalHoekpunten++;*/
					tv.hoekpunten[tv.aantalHoekpunten]=hoekpuntenNieuw[i];
					trefpuntRaak[tv.aantalHoekpunten]= true;
					
					
					if(maakLijn)
					{	if(aantalPuntenRood==0)
						{	puntnr1 = tv.aantalHoekpunten;
							aantalPuntenRood++;
							trefpuntRaak[tv.aantalHoekpunten]=true;
						}
						else 
						{	puntnr2 = tv.aantalHoekpunten;
							if(puntnr1!=puntnr2)tv.maakLijn(puntnr1,puntnr2,"rood");
							aantalPuntenRood=0;
							wisTrefpunten();
							trefpuntRaak[puntnr1]=false;
							if(tv.aantalLijnen>1)zoekSnijpunten();
						}
					}
					else if(maakVlak)
					{	if(aantalPuntenRood>0 && tv.aantalHoekpunten==puntnr[aantalPuntenRood-1])
						{	wisTrefpunten();
							aantalPuntenRood=0;
						}
						else if(aantalPuntenRood>1 && tv.aantalHoekpunten==puntnr[aantalPuntenRood-2])
						{	wisTrefpunten();
							aantalPuntenRood=0;
						}
						else if(i!=puntnr[0] || aantalPuntenRood==0)
						{	puntnr[aantalPuntenRood] = tv.aantalHoekpunten;
							aantalPuntenRood++;
							trefpuntRaak[tv.aantalHoekpunten]=true;
						}
						else
						{	puntnr[aantalPuntenRood] = tv.aantalHoekpunten; 
							Vlak vl = new Vlak(aantalPuntenRood);
							for(int j=0 ; j<aantalPuntenRood ; j++)
							{	vl.punten[j] = tv.hoekpunten[puntnr[j]];
							}
							tv.vlakken[tv.aantalVlakken] = vl;
							tv.aantalVlakken++;
							aantalPuntenRood=0;
							wisTrefpunten();
						}
						
					}
					tv.aantalHoekpunten++;
					break;
				}
			}
			if(raak)
			{	tekenOpnieuw();
				return;
			}
		}
	}
	public void muisSleepActie()
	{	
		xhoek=-0.5*geefSleepdy();
		yhoek=0.5*geefSleepdx();
		matrot.initialiseer();
		matrot.ydraaiAbs(yhoek);
		matrot.xdraaiAbs(xhoek);
		matres.mult(matrot);
		tekenOpnieuw();
	}
	public void animatie()
	{	while(animatieStatus())
		{	matrot.initialiseer();
			matrot.ydraaiAbs(1);
			matrot.xdraaiAbs(0);
			matres.mult(matrot);
			tekenOpnieuw();
		}
	}
}