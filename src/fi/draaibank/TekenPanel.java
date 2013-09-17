package fi.draaibank;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Polygon;
import java.util.Locale;
import java.util.ResourceBundle;

import java.util.Hashtable;
import java.util.ArrayList;

import javax.swing.*;

public class TekenPanel extends JPanel 
{	
	Matrix3D matrot;
	double k, xhoek,yhoek; 
	int hoogte, breedte;
	int aantal;
	Veelvlak v;
	boolean begin,raak;
	ControlPanel2 cp;
	Point posBasis;
	Punt[] nieuwHp;
	int aantalNieuwHp;
	Color bgcolor = Color.white;

	Tekenblad3D2 tb;
	private MuisBeheerder2 mb;
	
	boolean demoVersion = false;
		
	double zoomFactor = 1;
	//-----------------------------------------------------------------------------------------
	// initalisatie
	//-----------------------------------------------------------------------------------------
	public void init()
	{	tb = new Tekenblad3D2(this);
		setLayout(null);
		initialiseer();
		tb.setBounds(0, 0, getSize().width, getSize().height);
		add(tb);
	}	
	
	public void setSize(int b, int h)
	{
		if (tb != null)
			tb.setSize(b,h);
		super.setSize(b,h);
	}
	
	public void initialiseer()
	{	
		
		// lichtgeel
		bgcolor = new Color(255,255,200);
		achtergrondkleur(bgcolor);
		
		maakMuisActieMogelijk();
		
		breedte = getSize().width;
		hoogte = getSize().height;
		cp = new ControlPanel2(this);
		cp.setLayout(null);
		// kan wel een beetje kleiner
		//cp.setBounds(171,hoogte-60,breedte-171,60);
		cp.setBounds(171,hoogte-40,breedte-171,40);
		
		//tb.add(cp);
		
		add(cp);
		
		begin = true;
		k=2;
		matrot = new Matrix3D();
		nieuwHp = new Punt[50];
		aantalNieuwHp = 0;
		posBasis = new Point(85,hoogte-85);
		
		v = new Veelvlak();
	
		
	}

	public void initialiseer2()
	{	
		
		// lichtgeel
		bgcolor = new Color(255,255,200);
		achtergrondkleur(bgcolor);
		
		//maakMuisActieMogelijk();
		
		breedte = getSize().width;
		hoogte = getSize().height;
		
		//cp = new ControlPanel2(this);
		//cp.setLayout(null);
		// kan wel een beetje kleiner
		//cp.setBounds(171,hoogte-60,breedte-171,60);
		
		cp.setBounds(171,hoogte-40,breedte-171,40);
		
		//tb.add(cp);
		
		//add(cp);
		
/*		
		begin = true;
		k=2;
		matrot = new Matrix3D();
		nieuwHp = new Punt[50];
		aantalNieuwHp = 0;
		posBasis = new Point(85,hoogte-85);
		
		v = new Veelvlak();
*/
		
	}
	
	//-------------------------------------------------------------------------------------------
	//deze methoden kunnen alleen worden gebruikt in  "initialiseer()" van leerling-applet
	//-------------------------------------------------------------------------------------------
	public void maakMuisActieMogelijk()
	{	mb = new MuisBeheerder2(this);
		tb.addMouseListener(mb);
		tb.addMouseMotionListener(mb);
	}
	//-------------------------------------------------------------------------------------------
	//deze methoden worden gebruikt in de muishandler en  doorgegeven aan MuisBeheerder mb
	//-------------------------------------------------------------------------------------------
	public int geefSleepdx()
	{	return mb.geefSleepdx();
	}
	public int geefSleepdy()
	{	return mb.geefSleepdy();
	}
	public int geefDrukx()
	{	return mb.geefDrukx();
	}
	public int geefDruky()
	{	return mb.geefDruky();
	}
	public int geefX()
	{	return mb.geefX();
	}
	public int geefY()
	{	return mb.geefY();
	}

	public void tekenOpnieuw()
	{	repaint();	
	}
	
/*	
	public void tekenOpnieuw()
	{	tb.tekenOpnieuw();	
	}
	public void tekenErbij()
	{	tb.tekenErbij();
	}
*/
  	//-------------------------------------------------------------------------------------------
	//deze methoden worden gebruikt in "initialiseer" en doorgegeven aan Tekenblad tb (of 
	//Matrix2d) 
	//-------------------------------------------------------------------------------------------
	public void schaal(double s)
	{   tb.mat.schaal(s);
	}
	public void achtergrondkleur(String kl)
	{	tb.achtergrondkleur(kl);
	}
	public void achtergrondkleur(int r, int g, int b)
	{	tb.achtergrondkleur(r, g, b);
	}
	public void achtergrondkleur(Color c)
	{	tb.achtergrondkleur(c);
	}
	
	//-------------------------------------------------------------------------------------------
	//deze methoden worden gebruikt in "tekenprogramma()" en doorgegeven aan Tekenblad tb (of 
	//Matrix2d) 
	//-------------------------------------------------------------------------------------------
	public void xdraai(double dh)
	{	tb.mat.xdraai(dh);
	}
	public void ydraai(double dh){tb.mat.ydraai(dh);}
	public void zdraai(double dh){tb.mat.zdraai(dh);}
	public void rechts(double dh){tb.mat.zdraai(-dh);}
	public void links(double dh){tb.mat.zdraai(dh);}
	public void stapy(double dy){tb.naarVolgendPunt(0,-dy,0);}
	public void vooruit(double dy){tb.naarVolgendPunt(0,-dy,0);}
	public void stapx(double dx){tb.naarVolgendPunt(dx,0,0);}
	public void stapz(double dz){tb.naarVolgendPunt(0,0,-dz);}
	public void stap(double dx,double dy,double dz){tb.naarVolgendPunt(dx,-dy,-dz);}
	public void stap(double dx,double dy){tb.naarVolgendPunt(dx,-dy,0);}
	public void penAan(){tb.penAan();}
	public void penAan(String kl){tb.penAan(kl);}
	public void penAan(int r, int g, int b){tb.penAan(r, g, b);}
	public void penAan(int n){tb.penAan(n);}
	public void penAan(int n,String kl){tb.penAan(n,kl);}
	public void penAan(int n,int r, int g, int b){tb.penAan(n,r, g, b);}
	public void penUit(){tb.penUit();}
	public void penUit(int n){tb.penUit(n);}
	public void vulAan(){tb.vulAan();}
	public void vulAan(String kl){tb.vulAan(kl);}
	public void vulAan(int r, int g, int b){tb.vulAan(r, g, b);}
	public void vulAan(int n){tb.vulAan(n);}
	public void vulAan(int n,String kl){tb.vulAan(n,kl);}
	public void vulAan(int n,int r, int g, int b){tb.vulAan(n,r, g, b);}
	public void vulAan(Color kl){tb.vulAan(kl);	}
	public void vulUit(){tb.vulUit();}
	public void vulUit(int n){tb.vulUit(n);}
	public void schrijf(String s){tb.schrijf(s);}
	public void schrijf(String s, Font f){tb.schrijf(s,f);}
	public Polygon geefVlak(){return tb.geefVlak();}
	public Punt geefPunt(){return tb.geefPunt();}
	public Punt geefPunt(int n){return tb.geefPunt(n);}

	public void tekenprogramma()
	{	
		if (!demoVersion)
		{	
			tekenHok();
			tekenRoosterHok();
			tekenPunten();
			tekenLijnen();
		}
		begindraai(20,-90);
		tekenVeelvlak(0,v);
		
	}
	void begindraai(double xdr,double ydr)
	{	
		
//System.out.println("begindraai " + begin);		
		if (begin)
		{	tb.mat.initialiseer();
			matrot.initialiseer();
			matrot.zdraaiAbs(ydr);
			matrot.ydraaiAbs(xdr);
			tb.mat.mult(matrot);
			begin = false;
		}
	}
	void tekenHok()
	{	tb.gIm.setColor(Color.lightGray);
		tb.gIm.fillRect(0,hoogte-171,170,170);
		tb.gIm.setColor(Color.black);
		tb.gIm.drawRect(0,hoogte-171,170,170);
	}
	void tekenRoosterHok()
	{	tb.gIm.setColor(Color.white);
		tb.gIm.fillRect(5,hoogte-165,160,80);
		tb.gIm.setColor(Color.black);
		for(int j=0 ; j<9 ; j++)	tb.gIm.drawLine(5,hoogte-5-20*j,165,hoogte-5-20*j);
		for(int j=0 ; j<9 ; j++)	tb.gIm.drawLine(5+20*j,hoogte-5,5+20*j,hoogte-165);
	}
	void tekenStapTerug()
	{	if(aantalNieuwHp>2)
		{	aantalNieuwHp--;
			v = new DraaiObject(20,aantalNieuwHp,nieuwHp);
		}
		else 
		{	if(aantalNieuwHp!=0)aantalNieuwHp--;
			v = new Veelvlak();
		}
		tekenOpnieuw();
	}
	void wis()
	{	aantalNieuwHp=0;
		v = new Veelvlak();
		tekenOpnieuw();
	}
	void tekenPunten()
	{	for(int i=0 ; i<aantalNieuwHp ; i++)
		{	tb.gIm.fillOval(posBasis.x+(int)nieuwHp[i].x-3,posBasis.y+(int)nieuwHp[i].y-3,6,6 );
		}
	}
	void tekenLijnen()
	{	if(aantalNieuwHp>1)
		{	tb.gIm.setColor(Color.red);
			for(int i=0 ; i<aantalNieuwHp-1 ; i++)
			{	tb.gIm.drawLine(posBasis.x+(int)nieuwHp[i+1].x ,posBasis.y+(int)nieuwHp[i+1].y ,posBasis.x+(int)nieuwHp[i].x,  posBasis.y+(int)nieuwHp[i].y);
			}
			tb.gIm.setColor(Color.black);
		}
	}	
	

	public void voegNieuwPuntToe(double x,double y)
	{	if((aantalNieuwHp==1 || aantalNieuwHp==2) && x==nieuwHp[0].x && y==nieuwHp[0].y)
		{	aantalNieuwHp=0;
			return;
		}
		for(int i=1 ; i<aantalNieuwHp ; i++)
		{	if(x==nieuwHp[i].x && y==nieuwHp[i].y)
			{	aantalNieuwHp=0;
				return;
			}
		}
		
		if(y==0)y=-0.01;
		nieuwHp[aantalNieuwHp] = new Punt(x,y);
		aantalNieuwHp++;
		/*if(aantalNieuwHp>2 && x==nieuwHp[0].x && y==nieuwHp[0].y)
		{	basisv = new SchuifStuk(aantalNieuwHp,nieuwHp,posBasis,Color.red);
			aantalNieuwHp=0;
			maakVorm = false;
			cp.controlLeggen();
			tb.tekenOpnieuw();
		}*/
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
	{	for(int i=0 ; i<v.aantalVlakken ; i++)
		{	v.vlakken[i].vulkleur = v.vlakken[i].vorigeKleur;
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
	
	
	
	// op het rooster
	public void muisDrukActie()
	{	
		
		
		if (geefDrukx() < 170 && hoogte - geefDruky() < 170 && hoogte- geefDruky() > 84)
		{	
			
//System.out.println("druk rooster");
			
			int x = geefDrukx() - posBasis.x + 200;
			int y = geefDruky() - posBasis.y + 200;
			int ex = (x+1)%2;
			int ey = (y+1)%2;
			//Peter if(ex<2 && ey<2)
			{	voegNieuwPuntToe(x-200-ex+1 , y-200-ey+1);
			}
			//Peter voegNieuwPuntToe(x,y);
			v = new DraaiObject(20,aantalNieuwHp,nieuwHp);
			tekenOpnieuw();
			return;
		}
		
		
	}
	
	public void muisSleepActie()
	{
		
		if (geefX() > 170 || hoogte - geefY() > 170 )
		{	

			xhoek =-0.5*geefSleepdy();
			yhoek = 0.5*geefSleepdx();
			
//System.out.println("sleep blad " + xhoek + " , " + yhoek);
			
			matrot.initialiseer();
			matrot.ydraaiAbs(yhoek);
			matrot.xdraaiAbs(xhoek);
			tb.mat.mult(matrot);
			tekenOpnieuw();
		}
		
		
	}
	public void muisKlikActie()
	{}
	public void muisLosActie()
	{}

	public void zetDemoVersion(boolean b)
	{
		demoVersion = b;
		cp.setVisible(!demoVersion);
		repaint();
	}

	public void zetZoomOption(boolean b)
	{
		cp.vergrootButton.setVisible(b);
		cp.verkleinButton.setVisible(b);
		
	}
	
	public void zoomIn()
	{
		tb.mat.schaal(11e-1d);
		zoomFactor *= 11e-1d;
		repaint();
	}
	
	public void zoomUit()
	{
		tb.mat.schaal(1/(11e-1d));
		zoomFactor /= 11e-1d;
		repaint();
	}
	
	public Hashtable getState()
	{	Hashtable h = new Hashtable();
		
		h.put("aantalNieuwHp", new Integer(aantalNieuwHp));
		
		ArrayList<Double> nieuwHpX = new ArrayList<Double>();
		ArrayList<Double> nieuwHpY = new ArrayList<Double>();
		
		for (int hpCnt = 0; hpCnt < aantalNieuwHp; hpCnt++)
		{
			nieuwHpX.add(new Double(nieuwHp[hpCnt].x));
			nieuwHpY.add(new Double(nieuwHp[hpCnt].y));
		}
		
		h.put("nieuwHpX", nieuwHpX);
		h.put("nieuwHpY", nieuwHpY);
		
		Hashtable matState = tb.mat.getState();
		
		h.put("matState", matState);

		return h;
	}
	
	public void setState(Hashtable h)
	{
		
		int aantalNieuwHp = 0;
		
		if (h.containsKey("aantalNieuwHp"))
			aantalNieuwHp = ((Integer) h.get("aantalNieuwHp")).intValue();
		
		this.aantalNieuwHp = aantalNieuwHp;
		
		ArrayList<Double> nieuwHpX = new ArrayList<Double>();
		ArrayList<Double> nieuwHpY = new ArrayList<Double>();
		nieuwHp = new Punt[50];

		if (h.containsKey("nieuwHpX"))
			nieuwHpX = (ArrayList<Double>) h.get("nieuwHpX");
		if (h.containsKey("nieuwHpY"))
			nieuwHpY = (ArrayList<Double>) h.get("nieuwHpY");
		
		for (int hpCnt = 0; hpCnt < aantalNieuwHp; hpCnt++)
		{
			nieuwHp[hpCnt] = new Punt( ((Double) nieuwHpX.get(hpCnt)).doubleValue(),
									   ((Double) nieuwHpY.get(hpCnt)).doubleValue());	
		}

		if (h.containsKey("matState"))
		{
			Hashtable matState = (Hashtable) h.get("matState");
			tb.mat.setState(matState);
		}
		

		//ACTIE
		if (aantalNieuwHp > 0)
			v = new DraaiObject(20,aantalNieuwHp,nieuwHp);
				
		begin = false;
		
		tekenOpnieuw();
				
	}
}
