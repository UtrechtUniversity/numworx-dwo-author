package fi.mozarch;

import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class MozArch extends TekenApplet implements ActionListener
{
	protected static ResourceBundle rb;
	private Vlakdeel[] vlakdelen;
	private Vlakdeel actiefVlakdeel;//,vorigActiefVlakdeel;
	private int aantal,aantalVlakdelen, actiefVlakdeelNummer;
	double trek, trekx=0,treky=0, zijde;
	int[] volgorde;
	private LWButton wisKnop,inzendenKnop,bestandenKnop;
	private BestandenFrame bestandenFrame;
	
	private int beginFigAantalHp;
	private int beginFigAantalPz;
	
	private Color bgcolor = Color.white;
	
	public static void main(String[] args)    
	{	int width = 780;
        int height = 540;
		MozArchFrame mf = new MozArchFrame(new MozArch(),width, height);
		mf.setTitle("Halfregelmatige vlakvullingen");
		mf.pack();
		mf.show();
		mf.setSize(width, height);
	}
	
	public void initialiseer()
	{	Panel panel = new Panel();		panel.setBounds(1,471,600,28);
		panel.setLayout(null);
		panel.setBackground(new Color(220,220,160));		add(panel);
		
		InfoButton infoButton = new InfoButton("Mozaik",new String[]{"versie-info: 20021017",											"Copyright: Peter Boon"});
		infoButton.setBounds(10,4,30,20);
		infoButton.setBackground(new Color(190,190,130));
		panel.add(infoButton);
		
		String langArg = getParameter("language");
		if ( langArg == null || ! langArg.equals("en") ) langArg = "nl";
		Locale language = new Locale (langArg, "");
		rb = ResourceBundle.getBundle("fi.mozarch.text.Text",language);
		
		setBackground(bgcolor);
		String kleurcode = getParameter("bgcolor");
		if(kleurcode!=null)
		{	bgcolor = new Color(Integer.parseInt(kleurcode.substring(1),16));
			setBackground(bgcolor);
		}
		
		inzendenKnop = new LWButton(MozArch.rb.getString("opstuurKnopLabel"));
		inzendenKnop.addActionListener(this);
		inzendenKnop.setBounds(100,4,160,20);
		inzendenKnop.setBackground(new Color(190,190,130));
		panel.add(inzendenKnop);
		
		bestandenKnop = new LWButton(MozArch.rb.getString("bestandenKnopLabel"));
		bestandenKnop.addActionListener(this);
		bestandenKnop.setBounds(280,4,240,20);
		bestandenKnop.setBackground(new Color(190,190,130));
		panel.add(bestandenKnop);
		
		//inzendPanel = new InzendPanel();
		//inzendPanel.addActionListener(this);
		//inzendPanel.setBounds(10,10,760,481);
		//add(inzendPanel,0);
		//nzendPanel.setVisible(false);
		
		wisKnop = new LWButton(MozArch.rb.getString("wisKnopLabel"));
		wisKnop.setBounds(540,4,40,20);
		wisKnop.setBackground(new Color(190,190,130));
		wisKnop.addActionListener(this);
		panel.add(wisKnop);
		
		achtergrondkleur(bgcolor.getRed(),bgcolor.getGreen(),bgcolor.getBlue());
		maakMuisActieMogelijk();
		
		aantalVlakdelen = 0;
		aantal = 10;
		zijde = 40;
		vlakdelen = new Vlakdeel[200];
		volgorde = new int[200];
		
		
		boolean beginFig = true;
		beginFigAantalHp = 12;
		beginFigAantalPz = 2;
		String beginCode = getParameter("startFiguur");
		if(beginCode==null || beginCode.equals(""))beginFig = false;
		if(beginFig)
		{	int grens = beginCode.indexOf(",");
			beginFigAantalHp = Integer.parseInt(beginCode.substring(0,grens));
			beginFigAantalPz = Integer.parseInt(beginCode.substring(grens+1));
			
		}
		maakVeelhoek(beginFigAantalPz,beginFigAantalHp,-100.5,0.5,new Color(230,230,230));
		
		maakVeelhoek(1,3,300,220,new Color(255,0,0));
		maakVeelhoek(1,4,300,160,new Color(255,255,0));
		maakVeelhoek(1,6,300,80,new Color(0,255,0));
		maakVeelhoek(1,8,300,-20,new Color(0,255,255));
		maakVeelhoek(1,12,300,-160,new Color(0,0,255));
		vlakdelen[0].nieuw = false;
		
		String startFile = getParameter("stfile");
		if(startFile!=null && !startFile.equals(""))zetVlakdelen(startFile);
 	}
	
	public void tekenprogramma()
	{	
		
		
		for(int i=aantalVlakdelen-1 ; i>-1 ; i--)
		{	if(volgorde[i]!=-1 && !vlakdelen[volgorde[i]].nieuw)
				tekenVlakdeel(vlakdelen[volgorde[i]]);
		}
		tekenKader();
		for(int i=0 ; i<aantalVlakdelen ; i++)
		{	if(volgorde[i]!=-1 && vlakdelen[volgorde[i]].nieuw)
				tekenVlakdeel(vlakdelen[volgorde[i]]);
		}
		if(actiefVlakdeel!=null)tekenVlakdeel(actiefVlakdeel);
	}
	void tekenKader()
	{	penUit();stap(-390,250);penAan();
		vulAan(220,220,160);
		stap(779,0);stap(0,-499);stap(-779,0);stap(0,499);
		stap(10,-10);
		stap(0,-460);stap(590,0);stap(0,460);stap(-590,0);
		stap(-10,10);
		vulUit();
		penUit();
		stap(1,-1);
		vulAan(220,220,160);
		stap(9,0);stap(0,-9);stap(-9,0);stap(0,9);
		vulUit();
		stap(-1,1);
		stap(390,-250);penAan();
		
	}

	void maakVeelhoek(int aantalPerZijde, int n, double x, double y, Color k)
	{	double r = zijde*aantalPerZijde/(2*Math.sin(Math.PI/n));
		vlakdelen[aantalVlakdelen] = new Vlakdeel(this,aantalPerZijde*n+1,x,y,k);
		volgorde[aantalVlakdelen] = aantalVlakdelen;
		vlakdelen[aantalVlakdelen].hoekpunten[0] = new HoekpuntMoz(0,0);
		for(int i=0 ; i<n ; i++)
		{	double h = Math.PI/(n) - 2*i*Math.PI/n;
			double a = r*Math.cos(h);
			double b = r*Math.sin(h);
			double hv = Math.PI/(n) - 2*(i+1)*Math.PI/n;
			double av = r*Math.cos(hv);
			double bv = r*Math.sin(hv);
			for(int j=0 ; j<aantalPerZijde ; j++)
			{	double an = a+j*(av-a)/aantalPerZijde;
				double bn = b+j*(bv-b)/aantalPerZijde;
				vlakdelen[aantalVlakdelen].hoekpunten[1+aantalPerZijde*i+j] = new HoekpuntMoz(an,bn);
			}
		}
		vlakdelen[aantalVlakdelen].hoekpunten[aantalPerZijde*n+1] = vlakdelen[aantalVlakdelen].hoekpunten[1];
		aantalVlakdelen++;														
	}
	
	void maakAchtergrond()
	{
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
	
	public Vlakdeel[] geefVlakdelen()
	{	return vlakdelen;
	}
	
	public int geefAantalVlakdelen()
	{	return aantalVlakdelen;
	}
	
	public void zetVlakdelen(Vlakdeel[] vd, int[] vlg, int aantal)
	{	actiefVlakdeel = null;
		vlakdelen = vd;
		aantalVlakdelen = aantal;
		for(int i=0 ; i<aantalVlakdelen ; i++)
		{	volgorde[i] = vlg[i];
		}
		tekenOpnieuw();
	}
	
	public void zetVlakdelen(String s)
	{	actiefVlakdeel = null;
		Base64InputStream invoer;
		try
		{	ByteArrayInputStream bais = new ByteArrayInputStream(s.getBytes());
			invoer = new Base64InputStream(bais);
			aantalVlakdelen = invoer.readShort();
			//vlakdelen = new Vlakdeel[200];
			//volgorde = new int[200];
			for(int i=0 ; i<aantalVlakdelen ; i++)
			{	int aantalPunten = invoer.readShort();
				double posx = invoer.readDouble();
				double posy = invoer.readDouble();
				double orientatie = invoer.readDouble();
				boolean nieuw = invoer.readBoolean();
				int beginnummer = invoer.readShort();
				int rood = invoer.readByte()+128;
				int groen = invoer.readByte()+128;
				int blauw = invoer.readByte()+128;
				volgorde[i] = invoer.readShort();
				Color c = new Color(rood,groen,blauw);
				vlakdelen[i] = new Vlakdeel(this,aantalPunten,posx,posy,c);
				vlakdelen[i].orientatie = orientatie;
				vlakdelen[i].beginnummer = beginnummer;
				vlakdelen[i].nieuw = nieuw;
				for(int j=0 ; j<aantalPunten+1 ; j++)
				{	double x = invoer.readDouble();
					double y = invoer.readDouble();
					vlakdelen[i].hoekpunten[j] = new HoekpuntMoz(x,y);
				}
			}
		}
		catch(IOException io){}
	}
	
	public String maakString()
	{	Base64OutputStream uitvoer = null;
		ByteArrayOutputStream baos = null;
		try
		{	
			baos = new ByteArrayOutputStream();
			uitvoer = new Base64OutputStream(baos);
			uitvoer.writeShort((short)(aantalVlakdelen));
			for(int i=0 ; i<aantalVlakdelen ; i++)
			{	uitvoer.writeShort((short)(vlakdelen[i].aantalPunten));
				uitvoer.writeDouble(vlakdelen[i].draaipunt.x);
				uitvoer.writeDouble(vlakdelen[i].draaipunt.y);
				uitvoer.writeDouble(vlakdelen[i].orientatie);
				uitvoer.writeBoolean(vlakdelen[i].nieuw);
				uitvoer.writeShort((short)(vlakdelen[i].beginnummer));
				uitvoer.writeByte((byte)(vlakdelen[i].kleur.getRed()-128));
				uitvoer.writeByte((byte)(vlakdelen[i].kleur.getGreen()-128));
				uitvoer.writeByte((byte)(vlakdelen[i].kleur.getBlue()-128));
				uitvoer.writeShort((short)(volgorde[i]));
				for(int j=0 ; j<vlakdelen[i].aantalPunten+1 ; j++)
				{	uitvoer.writeDouble(vlakdelen[i].hoekpunten[j].x);
					uitvoer.writeDouble(vlakdelen[i].hoekpunten[j].y);
				}
			}	
			uitvoer.close();
		}
		catch(IOException io){}
		return baos.toString();
	}
	
	public void muisDrukActie()
	{	
		
		actiefVlakdeel=null;
		for(int i=0 ; i<aantalVlakdelen ; i++)
		{	int ii = volgorde[i];
			if(ii >0 &&vlakdelen[ii].tekenvlak.contains(geefDrukx(),geefDruky()))
			{	actiefVlakdeel = vlakdelen[ii];
				for(int j=i ; j>0 ; j--)
				{	volgorde[j] = volgorde[j-1];
				}
				volgorde[0] = ii;
				actiefVlakdeelNummer = ii;
				vlakdelen[ii].sleeppunt.x = geefDrukx();  vlakdelen[ii].sleeppunt.y = geefDruky();
				break;
			}
		}
		
	}
	
	public void muisSleepActie()
	{	if(actiefVlakdeel==null)return;
		int dx = geefSleepdx();  int dy = -geefSleepdy();
		double d = Math.sqrt(dx*dx + dy*dy);
		if(d<0.0001)return;
		double mpx = actiefVlakdeel.sleeppunt.x - actiefVlakdeel.draaipunt.x - 0.5*getSize().width ; 
		double mpy = actiefVlakdeel.sleeppunt.y + actiefVlakdeel.draaipunt.y - 0.5*getSize().height;
		double mp = Math.sqrt(mpx*mpx + mpy*mpy);
		
		double tx;
		double ty;
		double dhoek;
		if(mp<8)
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
		{	if(actiefVlakdeel.aantalPunten==4)maakVeelhoek(1,3,300,220,new Color(255,0,0));
			else if(actiefVlakdeel.aantalPunten==5)maakVeelhoek(1,4,300,160,new Color(255,255,0));
			else if(actiefVlakdeel.aantalPunten==7)maakVeelhoek(1,6,300,80,new Color(0,255,0));
			else if(actiefVlakdeel.aantalPunten==9)maakVeelhoek(1,8,300,-20,new Color(0,255,255));
			else if(actiefVlakdeel.aantalPunten==13)maakVeelhoek(1,12,300,-160,new Color(0,0,255));
			actiefVlakdeel.nieuw = false;
		}/**/
		
		
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
			if(actiefVlakdeel.aantalHoekpuntenVast>1 && trek>0 || actiefVlakdeel.aantalHoekpuntenVast==1 && trek>20)
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
	{	trekx =0 ; treky = 0;
		if(actiefVlakdeel==null)return;
		if(!actiefVlakdeel.nieuw && (actiefVlakdeel.draaipunt.x<-400 || actiefVlakdeel.draaipunt.x>200
		   || actiefVlakdeel.draaipunt.y<-250 || actiefVlakdeel.draaipunt.y>250))
		{	for(int j=actiefVlakdeelNummer ; j<aantalVlakdelen-1 ; j++)
			{	vlakdelen[j] = vlakdelen[j+1];
			}
			aantalVlakdelen--;
			for(int j=0 ; j<aantalVlakdelen ; j++)
			{	volgorde[j] = volgorde[j+1];
				if(volgorde[j]>actiefVlakdeelNummer)volgorde[j]--;
			}
			
			//volgorde[0] = -1;
			actiefVlakdeel=null;
			tekenOpnieuw();
			return;
		}
		for(int m=0 ; m<2 ; m++)	
		{
		double bx=0;
		double by=0;
		double dhoek=0;
		boolean maakAf = false;
		boolean raak= false;
		
		for(int i=1 ; i<actiefVlakdeel.aantalPunten ; i++)
		{	for(int j=aantalVlakdelen-1 ; j>-1 ; j--)
			{	int jj = volgorde[j];
				for(int k=1 ; jj!=-1 && k<vlakdelen[jj].aantalPunten; k++)
				{	double ax = vlakdelen[jj].hoekpunten[k].tekenpunt.x - actiefVlakdeel.hoekpunten[i].tekenpunt.x;
					double ay = vlakdelen[jj].hoekpunten[k].tekenpunt.y - actiefVlakdeel.hoekpunten[i].tekenpunt.y;
					if((ax<15 && ax>-15)&&(ay<15 && ay>-15)&& (actiefVlakdeelNummer != jj))
					{	if((actiefVlakdeel.aantalHoekpuntenVast<1 || maakAf) && !vlakdelen[jj].nieuw)
						{	maakAf = true;
							actiefVlakdeel.hoekpunten[i].tekenpunt.x += ax;
							actiefVlakdeel.hoekpunten[i].tekenpunt.y += ay;
							actiefVlakdeel.klikVast(i,jj,k);
							vlakdelen[jj].klikVast(k,actiefVlakdeelNummer,i);
							raak = true;
						}
						else
						{	if(!actiefVlakdeel.hoekpunten[i].zitVast(jj,k))
							{	double dax = actiefVlakdeel.hoekpunten[i].tekenpunt.x - actiefVlakdeel.draaipunt.x - 0.5*getSize().width ;
								double day = -actiefVlakdeel.hoekpunten[i].tekenpunt.y - actiefVlakdeel.draaipunt.y + 0.5*getSize().height;
								double dpx = vlakdelen[jj].hoekpunten[k].tekenpunt.x - actiefVlakdeel.draaipunt.x - 0.5*getSize().width ;
								double dpy = -vlakdelen[jj].hoekpunten[k].tekenpunt.y -  actiefVlakdeel.draaipunt.y + 0.5*getSize().height;
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
									actiefVlakdeel.klikVastDraai(i,jj,k);
									vlakdelen[jj].klikVastDraai(k,actiefVlakdeelNummer,i);
								}
							}
						}/**/
					}
				}
			}
			if(raak)
			{	raak = false;
				break;
			}
		}
		actiefVlakdeel.orientatie += dhoek ;
		if(m==0)tekenOpnieuw();
		}
		actiefVlakdeel = null;
		tekenOpnieuw();
	}
	
	public void wis()
	{	aantalVlakdelen = 0;
		actiefVlakdeel = null;
		maakVeelhoek(beginFigAantalPz,beginFigAantalHp,-100.5,0.5,new Color(230,230,230));
		maakVeelhoek(1,3,300,220,new Color(255,0,0));
		maakVeelhoek(1,4,300,160,new Color(255,255,0));
		maakVeelhoek(1,6,300,80,new Color(0,255,0));
		maakVeelhoek(1,8,300,-20,new Color(0,255,255));
		maakVeelhoek(1,12,300,-160,new Color(0,0,255));
		tekenOpnieuw();
		vlakdelen[0].nieuw = false;
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getSource()==wisKnop)
		{	wis();
		}
		else if(e.getSource()==inzendenKnop)
		{	if(bestandenFrame!=null)bestandenFrame.dispose();
			bestandenFrame = new BestandenFrame();
			bestandenFrame.show();
			bestandenFrame.slaOp(maakString());
		}
		else if(e.getSource()==bestandenKnop)
		{	if(bestandenFrame!=null)bestandenFrame.dispose();
			bestandenFrame = new BestandenFrame();
			bestandenFrame.addActionListener(this);
			bestandenFrame.show();
			bestandenFrame.bekijk();
		}
		else if(e.getSource()==bestandenFrame)
		{	String file = bestandenFrame.geefActieveFile();
			if(file!=null)zetVlakdelen(file);
			bestandenFrame.dispose();
			tekenOpnieuw();
		}
	}
}
