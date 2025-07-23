package fi.geodefull;



import java.awt.Polygon;
import java.awt.Color;

public class SleepApplet2 extends TekenApplet3D
{
	private Vlakdeel[] vlakdelen;
	private Vlakdeel actiefVlakdeel,vorigActiefVlakdeel;
	private int aantal,aantalVlakdelen, actiefVlakdeelNummer;
	double trek, trekx=0,treky=0, zijde;
	boolean slepend,lossend,drukkend;
		
	public void initialiseer()
	{	
		achtergrondkleur("wit");
		maakMuisActieMogelijk();
		tb.zetSchaduw(false);
		tb.zetSchalend(false);
		aantalVlakdelen = 0;
		aantal = 10;
		zijde = 40;
		vlakdelen = new Vlakdeel[200];
		maakVeelhoek(3,300,220,new Color(255,0,0));
		maakVeelhoek(4,300,160,new Color(255,255,0));
		maakVeelhoek(6,300,80,new Color(0,255,0));
		maakVeelhoek(8,300,-20,new Color(0,255,255));
		maakVeelhoek(12,300,-160,new Color(0,0,255));
	
		actiefVlakdeel = vlakdelen[0];
		vorigActiefVlakdeel =actiefVlakdeel;
 	}
	
	public void tekenprogramma()
	{	for(int i=0 ; i<aantalVlakdelen ; i++)
		{	if(!vlakdelen[i].nieuw)
				tekenVlakdeel(vlakdelen[i]);
		}
		
		tekenKader();
		for(int i=0 ; i<aantalVlakdelen ; i++)
		{	if(vlakdelen[i].nieuw)
				tekenVlakdeel(vlakdelen[i]);
		}
		tekenVlakdeel(actiefVlakdeel);
	}
	void tekenKader()
	{	penUit();stap(-390,250);penAan();
		vulAan("lichtgrijs");
		stap(780,0);stap(0,-500);stap(-780,0);stap(0,500);
		stap(10,-10);
		stap(0,-480);stap(590,0);stap(0,480);stap(-590,0);
		stap(-10,10);
		vulUit();
		penUit();stap(390,-250);penAan();
		
	}

	void maakVeelhoek(int n, int x, int y, Color k)
	{	double r = zijde/(2*Math.sin(Math.PI/n));
		vlakdelen[aantalVlakdelen] = new Vlakdeel(this,n+1,x,y,k);
		vlakdelen[aantalVlakdelen].hoekpunten[0] = new HoekpuntMoz(0,0);
		for(int i=0 ; i<n ; i++)
		{	double h = Math.PI/(n) - 2*i*Math.PI/n;
			double a = r*Math.cos(h);
			double b = r*Math.sin(h);
			vlakdelen[aantalVlakdelen].hoekpunten[1+i] = new HoekpuntMoz(a,b);
		}
		vlakdelen[aantalVlakdelen].hoekpunten[n+1] = vlakdelen[aantalVlakdelen].hoekpunten[1];
		aantalVlakdelen++;														
	}
	
	void tekenVlakdeel(Vlakdeel vd)
	{	int len = vd.hoekpunten.length;
		int num = vd.beginnummer;
		
		penUit(); stap(vd.draaipunt.x, vd.draaipunt.y); 
		links(vd.orientatie);
		vulAan(vd.kleur);
		for(int i=0 ; i<len ; i++)
		{	stap(vd.hoekpunten[(i+1+num)%len].x - vd.hoekpunten[(i+num)%len].x , vd.hoekpunten[(i+1+num)%len].y - vd.hoekpunten[(i+num)%len].y);
			vd.hoekpunten[(i+1+num)%len].tekenpunt = new Punt(geefPunt());
		}
		vulUit();
		vd.tekenvlak = geefVlak();
		penAan();
		for(int i=0 ; i<len ; i++)
		{	if((i+1+num)%len==0 || (i+num)%len==0)penUit();
			stap(vd.hoekpunten[(i+1+num)%len].x - vd.hoekpunten[(i+num)%len].x , vd.hoekpunten[(i+1+num)%len].y - vd.hoekpunten[(i+num)%len].y);
			penAan();
		}
		rechts(vd.orientatie);
		penUit();
		stap(-vd.draaipunt.x, -vd.draaipunt.y);
		tb.zetStart();
	}	
	
	public void muisDrukActie()
	{	drukkend = true;
		//while(tb.bezigMetTekenen||lossend||slepend)pauze(1);
		
		actiefVlakdeel=null;
		if(vorigActiefVlakdeel.tekenvlak.contains(geefDrukx(),geefDruky()))
		{	actiefVlakdeel = vorigActiefVlakdeel;
			actiefVlakdeel.sleeppunt.x = geefDrukx();  actiefVlakdeel.sleeppunt.y = geefDruky();
			drukkend = false;
			return;
		}
		for(int i=0 ; i<aantalVlakdelen ; i++)
		{	if(vlakdelen[i].tekenvlak.contains(geefDrukx(),geefDruky()))
			{	actiefVlakdeel = vlakdelen[i];
				//vlakdelen[i+1].tekenbaar = true;
				vorigActiefVlakdeel = actiefVlakdeel;
				actiefVlakdeelNummer = i;
				vlakdelen[i].sleeppunt.x = geefDrukx();  vlakdelen[i].sleeppunt.y = geefDruky();
				break;
			}
		}
		drukkend = false;
	}
	
	public void muisSleepActie()
	{	int dx = geefSleepdx();  int dy = -geefSleepdy();
		double d = Math.sqrt(dx*dx + dy*dy);
		if(d==0)d=0.0001;
		double mpx = actiefVlakdeel.sleeppunt.x - actiefVlakdeel.draaipunt.x - 0.5*getSize().width ; 
		double mpy = actiefVlakdeel.sleeppunt.y + actiefVlakdeel.draaipunt.y - 0.5*getSize().height;
		double mp = Math.sqrt(mpx*mpx + mpy*mpy);
		
		double tx;
		double ty;
		double dhoek;
		if(mp<10)
		{	tx = dx;
			ty = dy;
			dhoek = 0;
		}
		else
		{	double cosa = (mpx*dx + mpy*dy)/(mp*d);
			double mppx = actiefVlakdeel.sleeppunt.x + dx - actiefVlakdeel.draaipunt.x - 0.5*getSize().width; 
			double mppy = actiefVlakdeel.sleeppunt.y + dy + actiefVlakdeel.draaipunt.y - 0.5*getSize().height;
			double mpp = Math.sqrt(mppx*mppx + mppy*mppy);
			if(mpp<0.0001)mpp=0.0001;
			double t = mpp - mp;
			tx = (t/mpp)*mppx; 
			ty = (t/mpp)*mppy;
			double dh = (Math.acos((mp + d*cosa)/mpp))*180/Math.PI;
			if(mpx*dy - mpy*dx >0) dhoek = -dh;
			else  dhoek = dh;
		}
		
		if(actiefVlakdeel.nieuw)
		{	if(actiefVlakdeel.aantalPunten==4)maakVeelhoek(3,300,220,new Color(255,0,0));
			else if(actiefVlakdeel.aantalPunten==5)maakVeelhoek(4,300,160,new Color(255,255,0));
			else if(actiefVlakdeel.aantalPunten==7)maakVeelhoek(6,300,80,new Color(0,255,0));
			else if(actiefVlakdeel.aantalPunten==9)maakVeelhoek(8,300,-20,new Color(0,255,255));
			else if(actiefVlakdeel.aantalPunten==13)maakVeelhoek(12,300,-160,new Color(0,0,255));
			actiefVlakdeel.nieuw = false;
		}
		
		
		actiefVlakdeel.sleeppunt.x = actiefVlakdeel.sleeppunt.x + dx; 
		actiefVlakdeel.sleeppunt.y = actiefVlakdeel.sleeppunt.y + dy;
		if(actiefVlakdeel.aantalHoekpuntenVast<1)
		{	actiefVlakdeel.draaipunt.x = actiefVlakdeel.draaipunt.x + tx;
			actiefVlakdeel.draaipunt.y = actiefVlakdeel.draaipunt.y - ty;
			actiefVlakdeel.orientatie = actiefVlakdeel.orientatie + dhoek;
		}
		if(actiefVlakdeel.aantalHoekpuntenVast>0)
		{	if(actiefVlakdeel.aantalHoekpuntenVast==1)actiefVlakdeel.orientatie = actiefVlakdeel.orientatie + dhoek;
			trekx += tx;treky += ty;
			trek = Math.sqrt(trekx*trekx + treky*treky);
			if(trek>10)
			{	actiefVlakdeel.draaipunt.x = actiefVlakdeel.draaipunt.x + trekx;
				actiefVlakdeel.draaipunt.y = actiefVlakdeel.draaipunt.y - treky;
				for(int i=1 ; i<actiefVlakdeel.aantalPunten ; i++)
				{	for(int m=0 ; m<actiefVlakdeel.hoekpunten[i].aantalVastgeklikt ; m++)
					{	vlakdelen[actiefVlakdeel.hoekpunten[i].vlakdeelnummers[m]].klikLos(actiefVlakdeel.hoekpunten[i].hoeknummersVlakdeel[m],actiefVlakdeelNummer,i);
						
					}
				}
				actiefVlakdeel.klikAllesLos();
				trekx =0 ; treky = 0;
			}
		}
		tekenOpnieuw();
	}
	
	public void muisLosActie()
	{	for(int m=0 ; m<2 ; m++)	
		{
		double bx=0;
		double by=0;
		double dhoek=0;
		boolean maakAf = false;
		boolean raak= false;
		
		for(int i=1 ; i<actiefVlakdeel.aantalPunten ; i++)
		{	for(int j=0 ; j<aantalVlakdelen ; j++)
			{	for(int k=1 ; k<vlakdelen[j].aantalPunten ; k++)
				{	double ax = vlakdelen[j].hoekpunten[k].tekenpunt.x - actiefVlakdeel.hoekpunten[i].tekenpunt.x;
					double ay = vlakdelen[j].hoekpunten[k].tekenpunt.y - actiefVlakdeel.hoekpunten[i].tekenpunt.y;
					if((ax<15 && ax>-15)&&(ay<15 && ay>-15)&& (actiefVlakdeelNummer != j))
					{	if((actiefVlakdeel.aantalHoekpuntenVast<1 || maakAf) && !vlakdelen[j].nieuw)
						{	maakAf = true;
							actiefVlakdeel.hoekpunten[i].tekenpunt.x += ax;
							actiefVlakdeel.hoekpunten[i].tekenpunt.y += ay;
							actiefVlakdeel.klikVast(i,j,k);
							vlakdelen[j].klikVast(k,actiefVlakdeelNummer,i);
							raak = true;
						}
						else
						{	if(!actiefVlakdeel.hoekpunten[i].zitVast(j,k))
							{	double dax = actiefVlakdeel.hoekpunten[i].tekenpunt.x - actiefVlakdeel.draaipunt.x - 0.5*getSize().width ;
								double day = -actiefVlakdeel.hoekpunten[i].tekenpunt.y - actiefVlakdeel.draaipunt.y + 0.5*getSize().height;
								double dpx = vlakdelen[j].hoekpunten[k].tekenpunt.x - actiefVlakdeel.draaipunt.x - 0.5*getSize().width ;
								double dpy = -vlakdelen[j].hoekpunten[k].tekenpunt.y -  actiefVlakdeel.draaipunt.y + 0.5*getSize().height;
								double da = Math.sqrt(dax*dax + day*day);
								double dp = Math.sqrt(dpx*dpx + dpy*dpy);
								if((da-dp<0.01) && (da-dp>-0.01))
								{	if(Math.abs(dax-dpx)<0.0001 && Math.abs(day-dpy)<0.0001)
									{	dhoek = 0;
									}
									else
									{	double dh = (Math.acos((dax*dpx+day*dpy)/(da*dp))*180/Math.PI);
										if(dax*dpy - day*dpx >0) dhoek = dh;
										else  dhoek = -dh;
									}
									actiefVlakdeel.klikVastDraai(i,j,k);
									vlakdelen[j].klikVastDraai(k,actiefVlakdeelNummer,i);
								}
							}
						}
					}
				}
			}
			if(raak)
			{	raak = false;
				break;
			}
		}
		actiefVlakdeel.orientatie += dhoek ;
		tekenOpnieuw();
		}
		lossend = false;
	}
}
