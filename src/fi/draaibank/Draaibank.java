package fi.draaibank;

import java.awt.event.*;
import java.awt.*;
import java.util.*;
//import fi.beans.grnuminput.*;
import fi.draaibank.text.*;

/**
 * @author Peter Boon
 */

public class Draaibank extends TekenApplet3D //implements  NumberListener
{	
	protected static ResourceBundle rb;
	private String langArg;
	
	Matrix3D matrot;
	//Polygon[] p;
	double k, xhoek,yhoek; 
	int hoogte, breedte;
	int aantal;
	Veelvlak v;
	boolean begin,raak;
	ControlPanel cp;
	//NumberSlider hoogteSl;
	//NumberArrow aantalInv;
	Point posBasis;
	Punt[] nieuwHp;
	int aantalNieuwHp;
	private Color bgcolor = Color.white;
	
	public static void main(String[] args)    
	{	int width = 800;
        int height = 600;
		MainFrame mf = new MainFrame(new Draaibank(),width, height);
		mf.setTitle("Draaibank");
		mf.pack();
		mf.show();
		mf.setSize(width, height);
	}
	
	public void initialiseer()
	{	
		String langArg = getParameter("language");
		if (langArg == null) langArg = "nl";
		Locale language = new Locale (langArg, "");
		rb = ResourceBundle.getBundle("fi.draaibank.text.Text",language);
		
		bgcolor = new Color(255,255,200);
		String kleurcode = getParameter("bgcolor");
		if(kleurcode!=null)bgcolor = new Color(Integer.parseInt(kleurcode.substring(1),16));
		achtergrondkleur(bgcolor);
		
		maakMuisActieMogelijk();
		breedte = getSize().width;
		hoogte = getSize().height;
		cp = new ControlPanel(this);
		cp.setLayout(null);
		cp.setBounds(171,hoogte-60,breedte-171,60);
		tb.add(cp);
		begin = true;
		k=2;
		matrot = new Matrix3D();
		nieuwHp = new Punt[50];
		aantalNieuwHp = 0;
		posBasis = new Point(85,hoogte-85);
		
		
		
		v = new Veelvlak();
		
		
		
	}
	public void tekenprogramma()
	{	
		tekenHok();
		tekenRoosterHok();
		tekenPunten();
		tekenLijnen();
		begindraai(20,-90);
		tekenVeelvlak(0,v);
		
	}
	void begindraai(double xdr,double ydr)
	{	if(begin)
		{	tb.mat.initialiseer();
			matrot.initialiseer();
			matrot.zdraaiAbs(ydr);
			matrot.ydraaiAbs(xdr);
			tb.mat.mult(matrot);
			begin=false;
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
	public void numberChanged(String name,double val)
	{	
	}
	public void muisDrukActie()
	{	if(geefDrukx()< 170 && hoogte-geefDruky() < 170 && hoogte-geefDruky() > 84)
		{	int x = geefDrukx()-posBasis.x+200;
			int y = geefDruky()-posBasis.y+200;
			int ex = (x+1)%2;
			int ey = (y+1)%2;
			//if(ex<2 && ey<2)
			{	voegNieuwPuntToe(x-200-ex+1 , y-200-ey+1);
			}
			//voegNieuwPuntToe(x,y);
			v = new DraaiObject(20,aantalNieuwHp,nieuwHp);
			tekenOpnieuw();
			return;
		}
	}
	
	public void muisSleepActie()
	{	if(geefX()> 170 || hoogte-geefY() > 170 )
		{	xhoek=-0.5*geefSleepdy();
			yhoek=0.5*geefSleepdx();
			matrot.initialiseer();
			matrot.ydraaiAbs(yhoek);
			matrot.xdraaiAbs(xhoek);
			tb.mat.mult(matrot);
			tekenOpnieuw();
		}
	}
}

