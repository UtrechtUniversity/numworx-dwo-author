package fi.tekenveelvlakopdr;

import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import fi.beans.copyright.*;
import fi.beans.scorm.*;
import fi.beans.base64code.*;
import fi.beans.tekstobjects.*;
import fi.beans.wiskopdrbeans.*;
import fi.tekenveelvlakopdr.opdrnav.*;

//import grnuminput.*;


public class TekenVeelvlak extends TekenApplet3D implements  ActionListener,ItemListener
{	
	Slider zijdeSl;
	Matrix3D matrot, matres,mateenh;
	double k, xhoek,yhoek;

	Veelvlak v, tv;
	Punt[] trefpunten;
	boolean[] trefpuntRaak;
	int aantalHpNieuw, aantalGetekendeHoekpunten;
	Hoekpunt[] hoekpuntenNieuw;
	Punt[] trefpuntenNieuw;
	boolean begin, basisZichtbaar,maakLijn, maakVlak, vaktekening;
	JButton basisKnop, terugKnop, wisKnop, wisVKnop, lijnKnop, vlakKnop, vaktekKnop;
	JComboBox kiesV;
	JLabel l;
	
	int aantalPuntenRood, puntnr1, puntnr2;
	int[] puntnr;	
	
	Font font = new Font ("SansSerif",Font.PLAIN,12);
	Font fontBold = new Font ("SansSerif",Font.BOLD,12);
	
	public void initialiseer()
	{	setOpaque(true);
	    setBackground(getBackground());
		maakMuisActieMogelijk();
		
		kiesV = new JComboBox();
		kiesV.addItem(TekenVeelvlakOpdr.rb.getString("dodecaederLabel"));
		kiesV.addItem(TekenVeelvlakOpdr.rb.getString("kubusLabel"));
		kiesV.addItem(TekenVeelvlakOpdr.rb.getString("octaederLabel"));
		kiesV.addItem(TekenVeelvlakOpdr.rb.getString("icosaederLabel"));
		kiesV.addItem(TekenVeelvlakOpdr.rb.getString("tetraederLabel"));
		kiesV.addItemListener(this);
        
		kiesV.setBounds(25,50,100,25);
		rg.add(kiesV);
		rg.setBackground(new Color(180,217,255));
		
		l = new JLabel(TekenVeelvlakOpdr.rb.getString("zijdeLabel"));
		l.setFont(font);
		l.setAlignmentX(JLabel.CENTER);
		l.setBounds(45,60,100,20);
		rg.add(l);
		
		zijdeSl = new Slider(100,40);
		zijdeSl.addActionListener(this);
		zijdeSl.setBounds(10,90,110,20);
		zijdeSl.setBackground(new Color(180,217,255));
		rg.add(zijdeSl);
		
		lijnKnop = new JButton(TekenVeelvlakOpdr.rb.getString("lijnKnopLabel"));
		lijnKnop.setFont(fontBold);
		lijnKnop.addActionListener(this);
		lijnKnop.setBounds(8,140,114,25);
		rg.add(lijnKnop);
		//lijnKnop.setBackground(Color.white);
		
		vlakKnop = new JButton(TekenVeelvlakOpdr.rb.getString("vlakKnopLabel"));
		vlakKnop.setFont(font);
        vlakKnop.addActionListener(this);
		vlakKnop.setBounds(8,170,114,25);
		rg.add(vlakKnop);
		
		basisKnop = new JButton(TekenVeelvlakOpdr.rb.getString("verbergBasisKnopLabel"));
		basisKnop.setFont(font);
        basisKnop.addActionListener(this);
		basisKnop.setBounds(8,200,114,25);
		rg.add(basisKnop);
		
		terugKnop = new JButton(TekenVeelvlakOpdr.rb.getString("terugKnopLabel"));
		terugKnop.setFont(font);
        terugKnop.addActionListener(this);
		terugKnop.setBounds(8,230,114,25);
		rg.add(terugKnop);
		
		wisKnop = new JButton(TekenVeelvlakOpdr.rb.getString("wisLijnKnopLabel"));
		wisKnop.setFont(font);
        wisKnop.addActionListener(this);
		wisKnop.setBounds(8,260,114,25);
		rg.add(wisKnop);
		
		wisVKnop = new JButton(TekenVeelvlakOpdr.rb.getString("wisVlakKnopLabel"));
		wisVKnop.setFont(font);
        wisVKnop.addActionListener(this);
		wisVKnop.setBounds(8,290,114,25);
		rg.add(wisVKnop);
		
		
		vaktekKnop = new JButton("vaktekening");
		vaktekKnop.addActionListener(this);
		//rg.add(vaktekKnop);
		
		matrot = new Matrix3D();
		matres = new Matrix3D();
		mateenh = new Matrix3D();
		tb.mat = matres;
		k=180;
		begin=true;
		basisZichtbaar=true;
		maakLijn=true;
		maakVlak=false;
		vaktekening = false;
		trefpunten = new Punt[500];
		trefpuntRaak = new boolean[500];
		wisTrefpunten();
		aantalHpNieuw = 0;
		hoekpuntenNieuw = new Hoekpunt[500];
		trefpuntenNieuw = new Punt[500];
		aantalPuntenRood = 0;
		puntnr = new int[20];
		
		v = (new Icosaeder(1.3)).dualiseer();
		//v = (new Kubus(1.3));
		
		int n = 0;
		int aantalHp = 8;
		int aantalRib = 12;
		aantalGetekendeHoekpunten = aantalHp + n*aantalRib;
		Hoekpunt[] hp = new Hoekpunt[v.aantalHoekpunten+n*aantalRib];
		for(int i=0 ; i<v.aantalHoekpunten ; i++)
		{	hp[i]=v.hoekpunten[i];
		}
		v.hoekpunten = hp;
		v.aantalHoekpunten+=n*aantalRib;
		
		int[] rib = {0,1,1,2,2,3,3,0,0,4,1,5,2,6,3,7,4,5,5,6,6,7,7,4};
		for(int i=0 ; i<aantalRib ; i++)
		{	for(int j=0 ; j<n ; j++)
			{	v.hoekpunten[aantalHp+n*i+j] = new Hoekpunt(((n-j)*v.hoekpunten[rib[2*i]].x + (j+1)*v.hoekpunten[rib[2*i+1]].x)/(n+1),  
														((n-j)*v.hoekpunten[rib[2*i]].y + (j+1)*v.hoekpunten[rib[2*i+1]].y)/(n+1),
														((n-j)*v.hoekpunten[rib[2*i]].z + (j+1)*v.hoekpunten[rib[2*i+1]].z)/(n+1));
			}
		}
											
		for(int i=0 ; i<v.aantalVlakken ; i++)
		{	v.vlakken[i].vulkleur = "transparant";
		}
		
		tv = new Veelvlak();
		
		for(int i=0 ; i<v.aantalHoekpunten ; i++)
		{	hoekpuntenNieuw[aantalHpNieuw] = v.hoekpunten[i];
			aantalHpNieuw++;
		}
		//tv.voegHoekpuntToe(hoekpuntenNieuw[0].x, hoekpuntenNieuw[0].y, hoekpuntenNieuw[0].z);//dubieuze toevoeging zou niet nodig moeten zijn.
	}

	public void setBounds(int x, int y, int b, int h)
	{	k=180.0/500*Math.min(b-90, h);
		super.setBounds(x,y,b,h);
	}
	
	public void setState(Hashtable h)
	{	
		double[] hoekpunten = null;
		int[] vlakken = null;
		int[] lijnen = null;
		boolean basisZichtbaar = true;
		
		hoekpunten = (double[])h.get("hoekpunten");
		vlakken = (int[])h.get("vlakken");
		lijnen = (int[])h.get("lijnen");
		basisZichtbaar = ((Boolean)h.get("basisZichtbaar")).booleanValue();
		
		aantalPuntenRood=0;
		wisTrefpunten();
		//tv.wisLijnen();
		aantalHpNieuw = 0;
		
		this.basisZichtbaar = basisZichtbaar;
		if(!basisZichtbaar) basisKnop.setLabel(TekenVeelvlakOpdr.rb.getString("toonBasisKnopLabel"));
		tv = new Veelvlak(hoekpunten, vlakken, lijnen);
		
		
		for(int i=0 ; i<v.aantalHoekpunten ; i++)
		{	hoekpuntenNieuw[aantalHpNieuw] = v.hoekpunten[i];
			aantalHpNieuw++;
		}
	
		maakAlleSnijpunten();
		//tekenOpnieuw();
		
	}
	
	public Hashtable getState()
	{	double[] hoekpunten = null;
		int[] vlakken = null;
		int[] lijnen = null;
		boolean basisZichtbaar = true;
		
		hoekpunten = tv.hpRij;
		vlakken = tv.vlRij;
		lijnen = tv.lnRij;
		basisZichtbaar = this.basisZichtbaar;
		
		Hashtable h = new Hashtable();
		
		h.put("hoekpunten", hoekpunten);
		h.put("vlakken", vlakken);
		h.put("lijnen", lijnen);
		h.put("basisZichtbaar", new Boolean(basisZichtbaar));
		
		return h;
	}
	
	public void tekenprogramma()
	{	if(vaktekening)
		{	begindraai(0,0);
			
			tb.gIm.drawRect(20,220,200,200);
			tb.gIm.drawRect(220,220,200,200);
			tb.gIm.drawRect(220,20,200,200);
			tb.gIm.drawRect(420,220,200,200);
			
			
			penUit(); stap(0,100,0); penAan();
			xdraai(90);
			if(basisZichtbaar)tekenVeelvlak(2,v);
			tekenVeelvlak(1,tv);
			xdraai(-90);
			penUit(); stap(0,-100,0); penAan();
			
			penUit(); stap(0,-100,0); penAan();
			xdraai(0);
			if(basisZichtbaar)tekenVeelvlak(2,v);
			tekenVeelvlak(1,tv);
			xdraai(0);
			penUit(); stap(0,100,0); penAan();
			
			penUit(); stap(-200,-100,0); penAan();
			ydraai(90);
			if(basisZichtbaar)tekenVeelvlak(2,v);
			tekenVeelvlak(1,tv);
			ydraai(-90);
			penUit(); stap(200,100,0); penAan();
			
			penUit(); stap(200,-100,0); penAan();
			ydraai(-90);
			if(basisZichtbaar)tekenVeelvlak(2,v);
			tekenVeelvlak(1,tv);
			ydraai(90);
			penUit(); stap(-200,100,0); penAan();
			
			k=92;
			penUit(); stap(220,130,0); penAan();
			xdraai(30);ydraai(-34);
			if(basisZichtbaar)tekenVeelvlak(2,v);
			tekenVeelvlak(1,tv);
			ydraai(34);xdraai(-30);
			penUit(); stap(-220,-130,0); penAan();
			
			
		}
		else
		{	begindraai(20,-30);
			if(basisZichtbaar)tekenVeelvlak(2,v);
			tekenVeelvlak(1,tv);
			maakTrefpunten(tv);
		}
	}
	void begindraai(double xdr,double ydr)
	{	if(begin)
		{	//lijnKnop.setSize(100,25);
			//basisKnop.setSize(100,25);
			//terugKnop.setSize(100,25);
			//wisKnop.setSize(100,25);
			//wisVKnop.setSize(100,25);
			//vlakKnop.setSize(100,25);
						
			tb.mat.initialiseer();
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
			if(basisZichtbaar)// && i<aantalGetekendeHoekpunten)
			{	tb.mat = mateenh;
				stap(2,0);vulAan(1,"zwart");stap(-2,-2);stap(-2,2);stap(2,2);stap(2,-2);vulUit(1);stap(-2,0);
				tb.mat = matres;
			}
			stap(-k*hoekpuntenNieuw[i].x, -k*hoekpuntenNieuw[i].y, -k*hoekpuntenNieuw[i].z);penAan();
		}
	}
	public void wisTrefpunten()
	{	for(int i=0 ; i<500 ; i++)
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
	void maakAlleSnijpunten()
	{	for(int i=0 ; i<tv.aantalLijnen ; i++)
		{	for(int j=0 ; j<i ; j++)
			{	maakSnijpunt(tv.lijnen[j],tv.lijnen[i]);
			}
		}
	}
	
	void zoekSnijpunten()
	{	for(int i=0 ; i<tv.aantalLijnen-1 ; i++)
		{	maakSnijpunt(tv.lijnen[tv.aantalLijnen-1],tv.lijnen[i]);
		}
		
	}
	boolean checkVlak(Hoekpunt hpt)
	{	double ux = tv.hoekpunten[puntnr[1]].x - tv.hoekpunten[puntnr[0]].x;
		double uy = tv.hoekpunten[puntnr[1]].y - tv.hoekpunten[puntnr[0]].y;
		double uz = tv.hoekpunten[puntnr[1]].z - tv.hoekpunten[puntnr[0]].z;
		double vx = tv.hoekpunten[puntnr[2]].x - tv.hoekpunten[puntnr[1]].x;
		double vy = tv.hoekpunten[puntnr[2]].y - tv.hoekpunten[puntnr[1]].y;
		double vz = tv.hoekpunten[puntnr[2]].z - tv.hoekpunten[puntnr[1]].z;
		double nx = uy*vz - uz*vy;	
		double ny = uz*vx - ux*vz;
		double nz = ux*vy - uy*vx;
		double d = tv.hoekpunten[puntnr[0]].x*nx + tv.hoekpunten[puntnr[0]].y*ny + tv.hoekpunten[puntnr[0]].z*nz;
		double dn = hpt.x*nx + hpt.y*ny + hpt.z*nz;
		if(d-dn < 0.0001 && d-dn > -0.0001)return true;
		else return false; 
	}
	public void actionPerformed(ActionEvent e)
	{		boolean animatieWasAan=false;
		
			if(e.getSource()==zijdeSl)
			{	k = 5*zijdeSl.geefStand();
			}
			
			else if(e.getSource()==basisKnop)
			{	if(basisKnop.getLabel()==TekenVeelvlakOpdr.rb.getString("verbergBasisKnopLabel"))
				{	
					basisZichtbaar = false;
					basisKnop.setLabel(TekenVeelvlakOpdr.rb.getString("toonBasisKnopLabel"));
					
				}
				else
				{	basisZichtbaar = true;
					basisKnop.setLabel(TekenVeelvlakOpdr.rb.getString("verbergBasisKnopLabel"));
				}
			}	
			else if(e.getSource()==terugKnop)
			{	aantalPuntenRood=0;
				wisTrefpunten();
				if(maakLijn)
				{	if(tv.aantalLijnen>0)tv.wisVorigeLijn();
				}
				if(maakVlak)
				{	if(tv.aantalVlakken>0)tv.wisVorigVlak();
				}
			}
			else if(e.getSource()==lijnKnop)
			{	maakLijn = true;
				maakVlak = false;
				//lijnKnop.setBackground(Color.white);
				//vlakKnop.setBackground(Color.lightGray);
				lijnKnop.setFont(fontBold);
                vlakKnop.setFont(font);
				aantalPuntenRood=0;
				wisTrefpunten();
			}
			else if(e.getSource()==vlakKnop)
			{	maakLijn = false;
				maakVlak = true;
				//lijnKnop.setBackground(Color.lightGray);
				//vlakKnop.setBackground(Color.white);
				lijnKnop.setFont(font);
                vlakKnop.setFont(fontBold);
				aantalPuntenRood=0;
				wisTrefpunten();
			}
			else if(e.getSource()==wisKnop)
			{	aantalPuntenRood=0;
				wisTrefpunten();
				tv.wisLijnen();
				aantalHpNieuw = 0;
				for(int i=0 ; i<v.aantalHoekpunten ; i++)
				{	hoekpuntenNieuw[aantalHpNieuw] = v.hoekpunten[i];
					aantalHpNieuw++;
				}
			}
			else if(e.getSource()==wisVKnop)
			{	aantalPuntenRood=0;
				wisTrefpunten();
				tv.wisVlakken();
				
			}
			
			else if(animatieStatus())
			{	onderbreekAnimatie();
				animatieWasAan = true;
			}
			tekenOpnieuw();
			if(animatieWasAan)beginAnimatie();
		
		
		
		
	}

	public void nieuw()
	{	String soortV = (String)kiesV.getSelectedItem();

		if (soortV==TekenVeelvlakOpdr.rb.getString("kubusLabel"))v = new Kubus(1);
		else if (soortV==TekenVeelvlakOpdr.rb.getString("octaederLabel"))v = (new Kubus(Math.sqrt(3))).dualiseer();
		else if (soortV==TekenVeelvlakOpdr.rb.getString("icosaederLabel"))v = new Icosaeder(1);
		else if (soortV==TekenVeelvlakOpdr.rb.getString("dodecaederLabel"))v = (new Icosaeder(1.3)).dualiseer();
		else if (soortV==TekenVeelvlakOpdr.rb.getString("tetraederLabel"))v = new Tetraeder(1);
		
		for(int i=0 ; i<v.aantalVlakken ; i++)
		{	v.vlakken[i].vulkleur = "transparant";
		}
		aantalPuntenRood=0;
		wisTrefpunten();
		tv.wisLijnen();
		aantalHpNieuw = 0;
		tv = new Veelvlak();
		
		for(int i=0 ; i<v.aantalHoekpunten ; i++)
		{	hoekpuntenNieuw[aantalHpNieuw] = v.hoekpunten[i];
			aantalHpNieuw++;
		}
		tekenOpnieuw();
	}
	
	public void zetBasis(int figNr, int aantalRibPunten)
	{	if (figNr==0)
		{	v = new Kubus(1);
			int n = aantalRibPunten;
			int aantalHp = 8;
			int aantalRib = 12;
			aantalGetekendeHoekpunten = aantalHp + n*aantalRib;
			Hoekpunt[] hp = new Hoekpunt[v.aantalHoekpunten+n*aantalRib];
			for(int i=0 ; i<v.aantalHoekpunten ; i++)
			{	hp[i]=v.hoekpunten[i];
			}
			v.hoekpunten = hp;
			v.aantalHoekpunten+=n*aantalRib;
			
			int[] rib = {0,1,1,2,2,3,3,0,0,4,1,5,2,6,3,7,4,5,5,6,6,7,7,4};
			for(int i=0 ; i<aantalRib ; i++)
			{	for(int j=0 ; j<n ; j++)
				{	v.hoekpunten[aantalHp+n*i+j] = new Hoekpunt(((n-j)*v.hoekpunten[rib[2*i]].x + (j+1)*v.hoekpunten[rib[2*i+1]].x)/(n+1),  
															((n-j)*v.hoekpunten[rib[2*i]].y + (j+1)*v.hoekpunten[rib[2*i+1]].y)/(n+1),
															((n-j)*v.hoekpunten[rib[2*i]].z + (j+1)*v.hoekpunten[rib[2*i+1]].z)/(n+1));
				}
			}
		}
		
		
		
		else if(figNr==1)v = (new Kubus(Math.sqrt(3))).dualiseer();
		else if(figNr==2)v = new Icosaeder(1);
		else if(figNr==3)v = (new Icosaeder(1.3)).dualiseer();
		else if(figNr==4)v = new Tetraeder(1);
		
		for(int i=0 ; i<v.aantalVlakken ; i++)
		{	v.vlakken[i].vulkleur = "transparant";
		}
	}
	
	public void itemStateChanged(ItemEvent e)
	{	boolean animatieWasAan=false;
		
		nieuw();
		
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
					{	if(aantalPuntenRood<3)
						{	puntnr[aantalPuntenRood] = i;
							aantalPuntenRood++;
							trefpuntRaak[i]=true;
						}
						else if(checkVlak(tv.hoekpunten[i]))
						{   puntnr[aantalPuntenRood] = i;
							aantalPuntenRood++;
							trefpuntRaak[i]=true;
						}
						
					}
					else 
					{	puntnr[aantalPuntenRood] = i; 
						//Vlak vl = new Vlak(aantalPuntenRood);
						//for(int j=0 ; j<aantalPuntenRood ; j++)
						//{	vl.punten[j] = tv.hoekpunten[puntnr[j]];
						//}
						tv.voegVlakToe(aantalPuntenRood,puntnr);
						//tv.vlakken[tv.aantalVlakken] = vl;
						//tv.aantalVlakken++;
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
				{	//if(aantalPuntenRood>2 && !checkVlak(hoekpuntenNieuw[i]))
					//{	return;
					//}
					raak = true;
					//tv.hoekpunten[tv.aantalHoekpunten]=hoekpuntenNieuw[i];
					tv.voegHoekpuntToe(hoekpuntenNieuw[i].x, hoekpuntenNieuw[i].y, hoekpuntenNieuw[i].z);
					tv.aantalHoekpunten--;
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
						else if(tv.aantalHoekpunten!=puntnr[0] || aantalPuntenRood==0)
						{	if(aantalPuntenRood<3)
							{	puntnr[aantalPuntenRood] = tv.aantalHoekpunten;
								aantalPuntenRood++;
								trefpuntRaak[tv.aantalHoekpunten]=true;
							}
							else if(checkVlak(tv.hoekpunten[tv.aantalHoekpunten]))
							{   puntnr[aantalPuntenRood] = tv.aantalHoekpunten;
								aantalPuntenRood++;
								trefpuntRaak[tv.aantalHoekpunten]=true;
							}
							else 
							{	trefpuntRaak[tv.aantalHoekpunten]= false;
								tv.aantalHoekpunten--;
								tv.hpRijAantal = tv.hpRijAantal - 3;
							}
						}
						else 
						{	puntnr[aantalPuntenRood] = tv.aantalHoekpunten; 
							//Vlak vl = new Vlak(aantalPuntenRood);
							//for(int j=0 ; j<aantalPuntenRood ; j++)
							//{	vl.punten[j] = tv.hoekpunten[puntnr[j]];
							//}
							//tv.vlakken[tv.aantalVlakken] = vl;
							//tv.aantalVlakken++;
							tv.voegVlakToe(aantalPuntenRood,puntnr);
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
	{	if(!vaktekening)
		{	xhoek=-0.5*geefSleepdy();
			yhoek=0.5*geefSleepdx();
			matrot.initialiseer();
			matrot.ydraaiAbs(yhoek);
			matrot.xdraaiAbs(xhoek);
			matres.mult(matrot);
			tekenOpnieuw();
		}
	}
	public void animatie()
	{	while(animatieStatus() && !vaktekening)
		{	matrot.initialiseer();
			matrot.ydraaiAbs(1);
			matrot.xdraaiAbs(0);
			matres.mult(matrot);
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


	public void addActionListener(ActionListener al) {
		// TODO Auto-generated method stub
		
	}

	

	public int geefAsHoogte() {
		// TODO Auto-generated method stub
		return 0;
	}


	//public InteractieEditPanel getEditPanel() {
	//	return this;
	//}


	public Hashtable getEditState() {
		return getState();
	}


	public int getIpId() {
		// TODO Auto-generated method stub
		return 0;
	}


	public int getScore() {
		// TODO Auto-generated method stub
		return 0;
	}


	public int getScoreMax() {
		// TODO Auto-generated method stub
		return 0;
	}


	public boolean isCorrect() {
		// TODO Auto-generated method stub
		return false;
	}


	public boolean isFout() {
		// TODO Auto-generated method stub
		return false;
	}


	public void kijkNa() {
		// TODO Auto-generated method stub
		
	}


	public void kijkNa(int stapNr) {
		// TODO Auto-generated method stub
		
	}


	public void opnieuw() {
		// TODO Auto-generated method stub
		
	}


	public void setEditState(Hashtable h) {
		setState(h);
		rg.add(kiesV);
		
	}


	public void wis() {
		// TODO Auto-generated method stub
		
	}


	public void zetMaat() {
		// TODO Auto-generated method stub
		
	}


	public void zetMode(int mode) {
		// TODO Auto-generated method stub
		
	}


	public void zetNagekeken(boolean b) {
		// TODO Auto-generated method stub
		
	}


	public void zetOpdracht(Hashtable h, String[] randomVars,
			Hashtable randomValues) {
		setState(h);
		rg.remove(kiesV);
		
	}


	public void zetBreedte(int b) {
		// TODO Auto-generated method stub
		
	}


	public void zetHoogte(int h) {
		// TODO Auto-generated method stub
		
	}
}