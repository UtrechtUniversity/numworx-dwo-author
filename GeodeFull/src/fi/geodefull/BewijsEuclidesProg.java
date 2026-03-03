package fi.geodefull;

import java.awt.event.*;
import java.awt.*;
import fi.beans.grnuminput.*;

public class BewijsEuclidesProg extends TekenApplet3D implements NumberListener
{	
	Matrix3D matrot,matres,mateenh;
	double k,z, xhoek,yhoek;
	Veelvlak u,v, tv;
	Veelvlak w;
	Polygon[] p;
	boolean begin, raak;
	String veelvlakNaam;
	Hoekpunt[] hp;
	Hoekpunt hpActief;
	NumberArrow hoogteInv;
	Label label1,label2,label3,label1a,label2a,label3a;
	
	public void initialiseer()
	{	maakMuisActieMogelijk();
		
		label1 = new Label("aantal hoekpunten:");
		label2 = new Label("aantal ribben:");
		label3 = new Label("aantal zijden:");
		label1a = new Label("0");
		label2a = new Label("0");
		label3a = new Label("0");
		
		rg.gridbag.setConstraints(label1, rg.c);
		rg.add(label1);
		rg.gridbag.setConstraints(label1a, rg.c);
		rg.add(label1a);
		rg.gridbag.setConstraints(label2, rg.c);
		rg.add(label2);
		rg.gridbag.setConstraints(label2a, rg.c);
		rg.add(label2a);
		rg.gridbag.setConstraints(label3, rg.c);
		rg.add(label3);
		rg.gridbag.setConstraints(label3a, rg.c);
		rg.add(label3a);
		
		
		hoogteInv = new NumberArrow(0,45,0,0.5,1,"hoogte","");
		hoogteInv.addNumberListener(this);
		hoogteInv.setValue(0);
		rg.gridbag.setConstraints(hoogteInv, rg.c);
		rg.add(hoogteInv);
		hoogteInv.setBackground(Color.white);
		rg.setBackground(Color.white);
		
		tb.zetAfstand(3000);
		matrot = new Matrix3D();
		matres = new Matrix3D();
		mateenh = new Matrix3D();
		tb.mat = matres;
		k=8;
		z=16;
		begin=true;
		raak = false;
		tv = new Tetraeder(1);
		hp = new Hoekpunt[12];
		hp[0] = new Hoekpunt(0,0,-22);
		hp[1] = new Hoekpunt(9,0,-13);
		hp[2] = new Hoekpunt(0,0,-6);
		hp[3] = new Hoekpunt(0,10,-4);
		hp[4] = new Hoekpunt(18,0,2);
		hp[5] = new Hoekpunt(0,15,8);
		hp[6] = new Hoekpunt(0,14,10);
		hp[7] = new Hoekpunt(0,6,12);
		hp[8] = new Hoekpunt(6,0,18);
		hp[9] = new Hoekpunt(9,0,20);
		hp[10] = new Hoekpunt(4,4,22);
		hp[11] = new Hoekpunt(6.5,1,23);
		
		int[] vl = {8,
					3,	0,3,1,
					4,	1,3,5,4,
					6,	4,5,6,10,11,9,
					6,	0,2,7,6,5,3,
					6,	0,1,4,9,8,2,
					5,	2,8,11,10,7,
					3,	6,7,10,
					3,	8,9,11};
		v = new Veelvlak(hp,vl);
		for(int i=0 ; i<v.aantalVlakken ; i++)
		{	v.vlakken[i].vulkleur = "transparant";
		}
		
		double[] hpu = {-20,-20,-22,-20,20,-22,20,20,-22,20,-20,-22};
		int[] vlu = {1,		
					 4,	0,3,2,1};
		u = new Veelvlak(hpu,vlu);
		for(int i=0 ; i<u.aantalVlakken ; i++)
		{	u.vlakken[i].vulkleur = "geel";
		}
		
		hpActief = hp[0];
		
		//w = maakWaterVv(17);
		//for(int i=0 ; i<w.aantalVlakken ; i++)
		//{	w.vlakken[i].vulkleur = "cyaan";
		//	w.vlakken[i].lijnkleur = "blauw";
		//}
		w=v;
	}
	public Veelvlak maakWaterVv(double z)
	{	Hoekpunt[] hpw = new Hoekpunt[30];
		for(int i=0 ; i<12 ; i++)
		{	hpw[i] = hp[i];
		}
		hpw[12] = new Hoekpunt(z				,0				,z-22);
		hpw[13] = new Hoekpunt(0				,5*z/9			,z-22);
		hpw[14] = new Hoekpunt(0				,0				,z-22);
		hpw[15] = new Hoekpunt(9-(z-9)			,10*(z-9)/9		,z-22);
		hpw[16] = new Hoekpunt(9+9*(z-9)/15		,0				,z-22);
		hpw[17] = new Hoekpunt(6*(z-16)/24		,0				,z-22);
		hpw[18] = new Hoekpunt(0				,6*(z-16)/18	,z-22);
		hpw[19] = new Hoekpunt(0				,10+5*(z-18)/12	,z-22);
		hpw[20] = new Hoekpunt(18-18*(z-24)/6	,15*(z-24)/6	,z-22);
		hpw[21] = new Hoekpunt(18-9*(z-24)/18	,0				,z-22);
		hpw[22] = new Hoekpunt(0				,15-(z-30)/2	,z-22);
		hpw[23] = new Hoekpunt(0				,14-8*(z-32)/2	,z-22);
		hpw[24] = new Hoekpunt(4*(z-32)/12		,14-10*(z-32)/12,z-22);
		hpw[25] = new Hoekpunt(4*(z-34)/10		,6-2*(z-34)/10	,z-22);
		hpw[27] = new Hoekpunt(6+3*(z-40)/2		,0				,z-22);
		hpw[26] = new Hoekpunt(6+(z-40)/10		,(z-40)/5		,z-22);
		hpw[28] = new Hoekpunt(9-5*(z-42)/6		,(z-42)/3		,z-22);
		hpw[29] = new Hoekpunt(4+2.5*(z-44)		,4-3*(z-44)		,z-22);
		
		if(z<9)
		{	int[] vlw = {4,
						 3,	12,13,14,
						 3,	0,13,12,
						 3,	0,12,14,
						 3,	0,14,13};
			if(z==0) 
			{	hpActief = hp[0];
				label1a.setText("0");
				label2a.setText("0");
				label3a.setText("0");
			}
			else 
			{	hpActief = null;
				label1a.setText("4");
				label2a.setText("6");
				label3a.setText("4");
			}
			return new Veelvlak(hpw,vlw);
		}
		else if(z<16)
		{	int[] vlw = {5,
						 4,	13,14,16,15,
						 4,	0,13,15,1,
						 3,	1,15,16,
						 4,	14,0,1,16,
						 3,	0,14,13	};
			if(z==9) 
			{	hpActief = hp[1];
				label1a.setText("4");
				label2a.setText("6");
				label3a.setText("4");
			}
			else 
			{	hpActief = null;
				label1a.setText("6");
				label2a.setText("9");
				label3a.setText("5");
			}
			return new Veelvlak(hpw,vlw);		
		}
		else if(z<18)
		{	int[] vlw = {6,
						 5,	17,16,15,13,18,
						 4,	0,13,15,1,
						 3,	1,15,16,
						 5,	0,1,16,17,2,
						 4,	13,0,2,18,
						 3,	2,17,18	};
			if(z==16) 
			{	hpActief = hp[2];
				label1a.setText("6");
				label2a.setText("9");
				label3a.setText("5");
			}
			else 
			{	hpActief = null;
				label1a.setText("8");
				label2a.setText("12");
				label3a.setText("6");
			}
			return new Veelvlak(hpw,vlw);		
		}
		else if(z<24)
		{	int[] vlw = {6,
						 4,	19,18,17,16,
						 3,	0,3,1,
						 4,	16,1,3,19,
						 5,	0,1,16,17,2,
						 5,	0,2,18,19,3,
						 3,	2,17,18	};
			if(z==18) 
			{	hpActief = hp[3];
				label1a.setText("7");
				label2a.setText("11");
				label3a.setText("6");
			}
			else 
			{	hpActief = null;
				label1a.setText("8");
				label2a.setText("12");
				label3a.setText("6");
			}
			return new Veelvlak(hpw,vlw);		
		}
		else if(z<30)
		{	int[] vlw = {7,
						 5,	19,18,17,21,20,
						 3,	0,3,1,
						 5,	4,1,3,19,20,
						 6,	0,1,4,21,17,2,
						 5,	0,2,18,19,3,
						 3,	2,17,18,
						 3,	4,20,21	};
			if(z==24) 
			{	hpActief = hp[4];
				label1a.setText("8");
				label2a.setText("12");
				label3a.setText("6");
			}
			else 
			{	hpActief = null;
				label1a.setText("10");
				label2a.setText("15");
				label3a.setText("7");
			}
			return new Veelvlak(hpw,vlw);
		}
		else if(z<32)
		{	int[] vlw = {7,
						 4,	22,18,17,21,
						 3,	0,3,1,
						 4,	4,1,3,5,
						 6,	0,1,4,21,17,2,
						 6,	0,2,18,22,5,3,
						 3,	2,17,18,
						 4,	21,4,5,22};
			if(z==30) 
			{	hpActief = hp[5];
				label1a.setText("9");
				label2a.setText("14");
				label3a.setText("7");
			}
			else 
			{	hpActief = null;
				label1a.setText("10");
				label2a.setText("15");
				label3a.setText("7");
			}
			return new Veelvlak(hpw,vlw);		
		}
		else if(z<34)
		{	int[] vlw = {8,
						 5,	17,21,24,23,18,
						 3,	0,3,1,
						 4,	4,1,3,5,
						 6,	0,1,4,21,17,2,
						 7,	0,2,18,23,6,5,3,
						 3,	2,17,18,
						 5,	21,4,5,6,24,
						 3,	6,23,24	};
			if(z==32) 
			{	hpActief = hp[6];
				label1a.setText("10");
				label2a.setText("15");
				label3a.setText("7");
			}
			else 
			{	hpActief = null;
				label1a.setText("12");
				label2a.setText("18");
				label3a.setText("8");
			}
			return new Veelvlak(hpw,vlw);		
		}
		else if(z<40)
		{	int[] vlw = {8,
						 4,	17,21,24,25,
						 3,	0,3,1,
						 4,	4,1,3,5,
						 6,	0,1,4,21,17,2,
						 6,	0,2,7,6,5,3,
						 4,	2,17,25,7,
						 5,	21,4,5,6,24,
						 4,	24,6,7,25};
			if(z==34) 
			{	hpActief = hp[7];
				label1a.setText("11");
				label2a.setText("17");
				label3a.setText("8");
			}
			else 
			{	hpActief = null;
				label1a.setText("12");
				label2a.setText("18");
				label3a.setText("8");
			}
			return new Veelvlak(hpw,vlw);		
		}
		else if(z<42)
		{	int[] vlw = {9,
						 5,	21,24,25,26,27,
						 3,	0,3,1,
						 4,	4,1,3,5,
						 7,	0,1,4,21,27,8,2,
						 6,	0,2,7,6,5,3,
						 5,	7,2,8,26,25,
						 5,	21,4,5,6,24,
						 4,	24,6,7,25,
						 3,	8,27,26	};
			if(z==40) 
			{	hpActief = hp[8];
				label1a.setText("12");
				label2a.setText("18");
				label3a.setText("8");
			}
			else 
			{	hpActief = null;
				label1a.setText("14");
				label2a.setText("21");
				label3a.setText("9");
			}
			return new Veelvlak(hpw,vlw);		
		}
		else if(z<44)
		{	int[] vlw = {9,
						 4,	26,28,24,25,
						 3,	0,3,1,
						 4,	4,1,3,5,
						 6,	0,1,4,9,8,2,
						 6,	0,2,7,6,5,3,
						 5,	7,2,8,26,25,
						 6,	9,4,5,6,24,28,
						 4,	24,6,7,25,
						 4,	26,8,9,28	};
			if(z==42) 
			{	hpActief = hp[9];
				label1a.setText("13");
				label2a.setText("20");
				label3a.setText("9");
			}
			else 
			{	hpActief = null;
				label1a.setText("14");
				label2a.setText("21");
				label3a.setText("9");
			}
			return new Veelvlak(hpw,vlw);		
		}
		else 
		{	int[] vlw = {9,
						 3,	26,28,29,
						 3,	0,3,1,
						 4,	4,1,3,5,
						 6,	0,1,4,9,8,2,
						 6,	0,2,7,6,5,3,
						 6,	7,2,8,26,29,10,
						 7,	9,4,5,6,10,29,28,
						 3,	6,7,10,
						 4,	26,8,9,28	};
			if(z==44) 
			{	hpActief = hp[10];
				label1a.setText("13");
				label2a.setText("20");
				label3a.setText("9");
			}
			else 
			{	hpActief = null;
				label1a.setText("14");
				label2a.setText("21");
				label3a.setText("9");
			}
			if(z==45) 
			{	hpActief = hp[11];
				label1a.setText("12");
				label2a.setText("18");
				label3a.setText("8");
			}
			//else hpActief = null;
			return new Veelvlak(hpw,vlw);		
		}
	}
	public void tekenprogramma()
	{	tb.mat.initialiseer();
		penUit();stap(0,35,0);
		xdraai(105+xhoek);	zdraai(45+yhoek);
		
		tekenVeelvlak(2,w);
		if(hpActief!=null)tekenHoekpunt(4,hpActief);
		tekenVeelvlak(4,v);
		tekenVeelvlak(1,u);
		
	}
	void begindraai(double xdr,double ydr)
	{	if(begin)
		{	matrot.initialiseer();
			matrot.xdraaiAbs(90);
			matrot.ydraaiAbs(-52);
			matrot.xdraaiAbs(10);
			tb.mat.mult(matrot);
			begin=false;
		}
	}
	void tekenVeelvlak(int n,Veelvlak vv)
	{	for(int i=0 ; i<vv.aantalVlakken ; i++)
		{	tekenVlak(n,vv.vlakken[i]);
			//p[i] = geefVlak();
		}
	}
	void tekenVlak(int n,Vlak v)
	{	penUit();
		stap(k*v.punten[0].x, k*v.punten[0].y, k*v.punten[0].z);
		if(!(v.lijnkleur=="transparant"))penAan("lichtgrijs");
		if(v.vulkleur=="transparant")vulAan(n-1,v.vulkleur);
		else vulAan(n-1,"grijs");
		for(int i=v.aantalHoekpunten-1 ; i>-1 ; i--)
		{	int a=i ; int b=(i+1)%v.aantalHoekpunten;
			stap(k*(v.punten[a].x-v.punten[b].x), k*(v.punten[a].y-v.punten[b].y), k*(v.punten[a].z-v.punten[b].z));
		}
		vulUit(n-1);
		if(!(v.lijnkleur=="transparant"))penAan(n,v.lijnkleur);
		vulAan(n,v.vulkleur);
		for(int i=0 ; i<v.aantalHoekpunten ; i++)
		{	int a=i ; int b=(i+1)%v.aantalHoekpunten;
			stap(-k*(v.punten[a].x-v.punten[b].x), -k*(v.punten[a].y-v.punten[b].y), -k*(v.punten[a].z-v.punten[b].z));
		}
		vulUit(n);
		penUit();
		stap(-k*v.punten[0].x, -k*v.punten[0].y, -k*v.punten[0].z);
	}
	void tekenHoekpunt(int n,Hoekpunt hp)
	{	penUit();stap(k*hp.x,k*hp.y,k*hp.z);
		tb.mat = mateenh;
		vulAan(n,"rood"); stap(-4,0,0);stap(4,4,0);stap(4,-4,0);stap(-4,-4,0);stap(-4,4,0);stap(4,0,0);vulUit(n);
		tb.mat = matres;
		stap(-k*hp.x,-k*hp.y,-k*hp.z);
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
	
	/*public void muisDrukActie()
	{	for(int i=0 ; i<v.aantalVlakken ; i++)
		{	if(p[i].contains(geefDrukx(),geefDruky()))
			{	raak=true;
				return;
			}
		}
		raak = false;
	}*/
	public void muisSleepActie()
	{	//if(raak)
		{	xhoek=xhoek-geefSleepdy();
			yhoek=yhoek-geefSleepdx();
			
			tekenOpnieuw();
		}
	}
	public void numberChanged(String name,double val)
	{	z = val;
		w = maakWaterVv(z);
		for(int i=0 ; i<w.aantalVlakken ; i++)
		{	w.vlakken[i].vulkleur = "cyaan";
			w.vlakken[i].lijnkleur = "blauw";
		}
		tekenOpnieuw();
	}
}