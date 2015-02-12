package fi.tekenveelvlakopdr;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.JPanel;

import fi.beans.base64code.*;


public class Viewer3d extends JPanel
{
	private AnimatieBeheerder ab;
	private MuisBeheerder mb;
	private int breedte,hoogte;
	private Punt3D beginpunt,eindpunt,startpunt;
  	public Lichaam3D[] l;
  	private Image im ;
  	public Graphics gIm ;
	public Matrix3D mat;  
	private boolean pen, vul, leeg, schaduw;
	private int lnummer;
  	private Color penkleur,vulkleur,achtergrondkleur;
	public boolean bezigMetTekenen;
	public boolean muisAan;
	public boolean klikAan = false;
	private double afstand;
	int aantalVeelvlakken;
	Veelvlak[] vvRij;
	double k, xhoek,yhoek, beginx, beginy;
	double draaiX = 20, draaiY = -30;
	
	Polygon[] p;
//	private int[] sorteerRij;
//	private int pnr;
	
	int viewerPosition = TekenVeelvlakInteractiePanel.MOVEABLE;
	
	boolean restrictRotation = true;
	
	double k50;
	double kMinFac = 60e-2d;
	double kMaxFac = 140e-2d;
	double zoomFac = 5e-1d;
	
	boolean voorkantPijlZichtbaar = false;
	int voorkantPijlIndex = -1;
	
	boolean vlakkenKleurenOptie = false;
    boolean profielenKleurenOptie = true;
    //boolean viewerKleurenOptie = false;
    int aantalVlakkenRood = 0;
	
	public Viewer3d(int x, int y,int b, int h)
	{	this(new Veelvlak(), x, y, b, h);
	}
	
	public Viewer3d(Veelvlak v, int x, int y,int b, int h)
	{	setBounds(x,y,b,h);
		aantalVeelvlakken = 1;
		vvRij = new Veelvlak[5];
		vvRij[0] = v;
		p = new Polygon[v.aantalVlakken+4];
		//ab = new AnimatieBeheerder(this);
		mb = new MuisBeheerder(this);
		addMouseListener(mb);
		addMouseMotionListener(mb);
		achtergrondkleur = Color.white;
		leeg = false;
		schaduw = true;
		muisAan = true;
		l = new Lichaam3D[5];
		afstand = 1000;
		lnummer=0;
		for(int i=0 ; i<5 ; i++)
		{	l[i] = new Lichaam3D();
			l[i].zetAfstand(afstand);
		}
//		sorteerRij = new int[200];
		mat = new Matrix3D();
		if(mb!=null && ab!=null)				
		{	mb.meldAnimatieBeheerder(ab);		
		}
		//vv = new Kubus(1);
		k = 180; //k=200;
		xhoek = 0;
		yhoek = 0;
		beginx = 20;
		beginy = -30;
	}
	
	public void setBounds(int x, int y, int b, int h)
	{
		
//System.out.println("v3d b = " + b + " h = " + h);
		
        k50 = 180 - (350 - b) * 25e-1d / 18;
        double kMin = kMinFac * k50;
		double kMax = kMaxFac * k50;

//System.out.println("v3d kMin = " + UF.format(kMin, 1));
//System.out.println("v3d kMax = " + UF.format(kMax, 1));
		
		
        k = zoomFac * (kMax - kMin) + kMin;
        
		super.setBounds(x, y, b, h);
		
if (k > 0)		
{	//System.out.println("v3d b = " + b + " h = " + h);		
	//System.out.println("v3d k = " + UF.format(k, 1));
}
		
		
		im = null;
		repaint();
	}

    public void zetVlakkenKleurenOptie(boolean b)
    {	vlakkenKleurenOptie = b;
    }
    public void zetProfielenKleurenOptie(boolean b, boolean leerling)
    {	profielenKleurenOptie = b;
    
//System.out.println("v3d leerling = " + leerling);    
    
    	if (!leerling)
    		return;
    	if (vlakkenKleurenOptie && (afstand == 1000))
    		zetKlikAan(!b);
    	else if (vlakkenKleurenOptie && (afstand > 10000))
    		zetKlikAan(b);
    	
//System.out.println("v3d klikAan = " + klikAan);    	
    }
    //public void zetViewerKleurenOptie(boolean b)
    //{	viewerKleurenOptie = b;
    	
    //}
	
	public void zetZoomFac(double zFac)
	{
		zoomFac = zFac;
		k50 = 180 - (350 - getSize().width) * 25e-1d / 18;
        double kMin = kMinFac * k50;
		double kMax = kMaxFac * k50;
		k = zoomFac * (kMax - kMin) + kMin;
		
//System.out.println("v3d zetZoomFac " + UF.format(k, 1));		
	}
	
	public void zetViewerPosition(int vPos)
	{
		muisAan = false;
		viewerPosition = vPos;
		
		if (vPos == TekenVeelvlakInteractiePanel.MOVEABLE)
		{	muisAan = true;
			schaduw = true;
			zetBeginHoeken(draaiX,draaiY);
			zetAfstand(1000);
		}
		else if (vPos == TekenVeelvlakInteractiePanel.FRONTVIEW)
		{	zetBeginHoeken(0,0);
			zetAfstand(100000);
			schaduw = false;
		}
		else if (vPos == TekenVeelvlakInteractiePanel.BACKVIEW)
		{	zetBeginHoeken(0,180);
			zetAfstand(100000);
			schaduw = false;
		}
		else if (vPos == TekenVeelvlakInteractiePanel.TOPVIEW)
		{	zetBeginHoeken(90,0);
			zetAfstand(100000);
			schaduw = false;
		}
		else if (vPos == TekenVeelvlakInteractiePanel.BOTTOMVIEW)
		{	zetBeginHoeken(-90,0);
			zetAfstand(100000);
			schaduw = false;
		}
		else if (vPos == TekenVeelvlakInteractiePanel.LEFTVIEW)
		{	zetBeginHoeken(0,90);
			zetAfstand(100000);
			schaduw = false;
		}
		else if (vPos == TekenVeelvlakInteractiePanel.RIGHTVIEW)
		{	zetBeginHoeken(0,-90);
			zetAfstand(100000);
			schaduw = false;
		}
	
		tekenOpnieuw();
	}
/*	
	public String geefAanzichtTekst(int a)
	{	if(a==1) return ProfielenDraaien.rb.getString("vaLabel");
		else if(a==2) return ProfielenDraaien.rb.getString("raLabel");
		else if(a==3) return ProfielenDraaien.rb.getString("laLabel");
		else if(a==4) return ProfielenDraaien.rb.getString("baLabel");
		else if(a==5) return ProfielenDraaien.rb.getString("oaLabel");
		else if(a==6) return ProfielenDraaien.rb.getString("aaLabel");
		else return "";
	}
*/

	public String[] getViewerKleuren()
	{
		String[] viewerKleuren = new String[vvRij[0].aantalVlakken];
		for (int vCnt = 0; vCnt < viewerKleuren.length; vCnt++)
		{	viewerKleuren[vCnt] = vvRij[0].vlakken[vCnt].vulkleur;
		}
		return viewerKleuren;
	}
	
	public boolean evalueer()
	{
		String[] viewerKleuren = getViewerKleuren();
		int roodoranjeroodCnt = 0;
		int oranjeroodCnt = 0;
		for (int i = 0; i < viewerKleuren.length; i++)
		{	if (viewerKleuren[i].equals("roodoranjerood"))
				roodoranjeroodCnt++;
			if (viewerKleuren[i].equals("oranjerood"))
				oranjeroodCnt++;
		}

//		System.out.println("v3d eval ror = " + roodoranjeroodCnt + " or = " + oranjeroodCnt);
//		System.out.println("v3d eval avr = " + aantalVlakkenRood);
		
		
		return (roodoranjeroodCnt == aantalVlakkenRood) && (oranjeroodCnt == 0);
	}
	
	public boolean evalueer(int gevraagdAanzicht)	
	{	int drx = (int) geefDraaiX();
		int dry = (int) geefDraaiY();
		int tol = 20;
		
		if (gevraagdAanzicht == TekenVeelvlakInteractiePanel.FRONTVIEW && 
			Math.abs(drx) < tol && Math.abs(dry) <tol)
			return true;
		else if (gevraagdAanzicht == TekenVeelvlakInteractiePanel.RIGHTVIEW && 
				Math.abs(drx)< tol && Math.abs(dry + 90) <tol)
			return true;
		else if (gevraagdAanzicht == TekenVeelvlakInteractiePanel.LEFTVIEW && 
				Math.abs(drx) < tol && Math.abs(dry - 90) < tol)
			return true;
		else if (gevraagdAanzicht == TekenVeelvlakInteractiePanel.TOPVIEW && 
				Math.abs(drx-90) < tol && Math.abs(dry) < tol)
			return true;
		else if (gevraagdAanzicht == TekenVeelvlakInteractiePanel.BOTTOMVIEW && 
				Math.abs(drx + 90)< tol && Math.abs(dry) < tol)
			return true;
		else if (gevraagdAanzicht == TekenVeelvlakInteractiePanel.BACKVIEW && 
				Math.abs(drx)<tol && (Math.abs(dry - 180) < tol || Math.abs(dry + 180)< tol))
			return true;
		
		return false;
	}
	public void setState(Hashtable h)
	{	
		
//System.out.println("viewer setState");

		double[] hoekpunten = null;
		int[] vlakken = null;
		int[] lijnen = null;
		String[] kleuren = null;
		
		hoekpunten = (double[]) h.get("hoekpunten");
		vlakken = (int[]) h.get("vlakken");
		lijnen = (int[]) h.get("lijnen");
		if (h.containsKey("kleuren"))
			kleuren = (String[]) h.get("kleuren");
		
		double zoomFac = 5e-1d;
		double draaiX = 20;
		double draaiY = -30;

		boolean muisAan = true;
		
		int viewerPosition = TekenVeelvlakInteractiePanel.MOVEABLE;
		
		int aantalVlakkenRood = 0; 
		
		if (h.containsKey("zoomFac"))
			zoomFac = ((Double) h.get("zoomFac")).doubleValue();
		if (h.containsKey("draaiX"))
			draaiX = ((Double) h.get("draaiX")).doubleValue();
		if (h.containsKey("draaiY"))
			draaiY = ((Double) h.get("draaiY")).doubleValue();

		if (h.containsKey("muisAan"))
			muisAan = ((Boolean) h.get("muisAan")).booleanValue();

		if (h.containsKey("viewerPosition"))
			viewerPosition = ((Integer) h.get("viewerPosition")).intValue();
		
//System.out.println("vPos = " + viewerPosition);
		if (h.containsKey("aantalVlakkenRood"))
			aantalVlakkenRood = ((Integer) h.get("aantalVlakkenRood")).intValue();

		
		//this.zoomFac = zoomFac;
		zetZoomFac(zoomFac);
		
		this.draaiX = draaiX;
		this.draaiY = draaiY;
		zetViewerPosition(viewerPosition);
		//zetBeginHoeken(draaiX, draaiY);
		
		this.muisAan = muisAan;
		
		this.aantalVlakkenRood = aantalVlakkenRood;

//System.out.println("vPos = " + viewerPosition);		
		
//System.out.println("hp = " + hoekpunten.length + " vl = " + vlakken.length + " ln = " + lijnen.length);

		Veelvlak v = new Veelvlak(hoekpunten, vlakken, lijnen);
		if (kleuren != null)
		{	for (int i = 0; i < v.aantalVlakken; i++)
			{	v.vlakken[i].vulkleur = kleuren[i];
			}
		}
		vvRij[0] = v;
		
		tekenOpnieuw();
	}
	
	public void setViewerKleuren(String[] kleuren)
	{
		for (int i = 0; i < vvRij[0].aantalVlakken; i++)
		{	vvRij[0].vlakken[i].vulkleur = kleuren[i];
		}
	}
/*
	public Hashtable getState()
	{	
		if (vvRij[0] == null)
		{	
//System.out.println("vvRij[0] == null");			
			return null;
		}
		
System.out.println("viewer getState");

		double[] hoekpunten = null;
		int[] vlakken = null;
		int[] lijnen = null;
		String[] kleuren = null;
		
		hoekpunten = vvRij[0].hpRij;
		vlakken = vvRij[0].vlRij;
		lijnen = vvRij[0].lnRij;
		kleuren = new String[vvRij[0].aantalVlakken];
		for (int vCnt = 0; vCnt < kleuren.length; vCnt++)
		{	kleuren[vCnt] = vvRij[0].vlakken[vCnt].vulkleur;
		}
		
		
		double draaiX = this.draaiX;
		double draaiY = this.draaiY;
		
		if (viewerPosition == TekenVeelvlakInteractiePanel.MOVEABLE)
		{	draaiX = geefDraaiX();
			draaiY = geefDraaiY();
		}	
		
//System.out.println("hp = " + hoekpunten.length + " vl = " + vlakken.length + " ln = " + lijnen.length);		
		
		Hashtable h = new Hashtable();
		
		h.put("hoekpunten", hoekpunten);
		h.put("vlakken", vlakken);
		h.put("lijnen", lijnen);
		h.put("kleuren", kleuren);
		
		h.put("zoomFac", new Double(zoomFac));
		
		h.put("draaiX", new Double(draaiX));
		h.put("draaiY", new Double(draaiY));
//System.out.println("draaiX = " + UF.format(draaiX,1) + " draaiY = " + UF.format(draaiY,1));
		h.put("muisAan", new Boolean(muisAan));
		
		h.put("viewerPosition", new Integer(viewerPosition));
		
System.out.println("vPos = " + viewerPosition);		
		
		return h;
		
	}
*/	
	
	public void setBackground(Color c)
	{	achtergrondkleur = c;
		super.setBackground(c);
	}
	
	public void zetAfstand(double afst)
	{	afstand = afst;
		for(int i=0 ; i<5 ; i++)
			{	l[i].zetAfstand(afst);
			}
	}
	public void zetSchaduw(boolean s)
	{	schaduw = s;
	}
	public void zetBeginHoeken(double hx, double hy)
	{	beginx = hx;
		beginy = hy;
		if (restrictRotation)
		{	if (beginx > 90) 
				beginx = 90;
			if (beginx < -90)
				beginx = -90;
		}
		xhoek = 0;
		yhoek = 0;
	}
	public double geefDraaiX()
	{	return beginx+xhoek;
	}
	public double geefDraaiY()
	{	return beginy+yhoek;
	}
	public void zetKlikAan(boolean b)
	{	klikAan = b;
	}
	public void zetMuisAan(boolean b)
	{	muisAan = b;
	}
	public void zetVeelvlak(Veelvlak v)
	{	vvRij[0] = v;
		tekenOpnieuw();
//System.out.println("v3d zetVeelvlak vko = " + vlakkenKleurenOptie);
//System.out.println("v3d zetVeelvlak pko = " + profielenKleurenOptie);
	}
	public void zetVeelvlak(Veelvlak v, int n)
	{	vvRij[n] = v;
		tekenOpnieuw();
	}
	public void voegVeelvlakToe(Veelvlak v)
	{	vvRij[aantalVeelvlakken] = v;
		aantalVeelvlakken++;
	}

	public void voegVooraanzichtPijlToe(Veelvlak p)
	{	vvRij[aantalVeelvlakken] = p;
		voorkantPijlZichtbaar = true;
		voorkantPijlIndex = aantalVeelvlakken;
		aantalVeelvlakken++;
		tekenOpnieuw();
	}

	// neem aan pijl is de laatste
	public void verwijderVooraanzichtPijl()
	{	
		if (voorkantPijlZichtbaar)
		{	
			vvRij[aantalVeelvlakken] = null;
			voorkantPijlZichtbaar = false;
			voorkantPijlIndex = -1;
			aantalVeelvlakken--;
			tekenOpnieuw();
		}	
	}
	
	void tekenprogramma()
	{	
		mat.initialiseer();
		mat.xdraai(beginx+xhoek);
		mat.ydraai(beginy+yhoek);
		for (int i = 0; i < aantalVeelvlakken; i++)
			tekenVeelvlak(0,vvRij[i]);
	}
	void tekenVeelvlak(int n,Veelvlak vv)
	{	
		if (vv == vvRij[0])
		{	p = new Polygon[vv.aantalVlakken];
		}
		for (int i = 0; i < vv.aantalVlakken; i++)
		{	tekenVlak(n,vv.vlakken[i]);
			if (vv == vvRij[0])
			{	p[i] = geefVlak();
			}
		}
		//for (int i = 0; i < vv.aantalLijnen; i++)
		//{	tekenLijn(vv.lijnen[i]);
		//}
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
	{	
		
//System.out.println("v3d tekenVlak " + v.vulkleur);

		penUit();
		stap(k*v.punten[0].x, k*v.punten[0].y, k*v.punten[0].z);
		
//System.out.println("vvk = " + v.vulkleur);		
		
		if (v.vulkleur=="transparant")
		{	if (n==2)
				vulAan(v.vulkleur);
			else if (n==1)
				vulAan(1,v.vulkleur);
		}
		else if (v.vulkleur != "zwart") 
		{	if (n==2)
			{	vulAan("grijs");
			}
			else if (n==1)
			{	vulAan(1,"grijs");
			}
			else
				vulAan(n,"grijs");
		}
		
		if (v.vulkleur != "zwart")
		{
			for(int i=v.aantalHoekpunten-1 ; i>-1 ; i--)
			{	int a=i ; 
				int b=(i+1)%v.aantalHoekpunten;
				stap(k*(v.punten[a].x-v.punten[b].x), k*(v.punten[a].y-v.punten[b].y), k*(v.punten[a].z-v.punten[b].z));
			}
			if (n==2)
				vulUit();
			else if (n==1)
				vulUit(1);
			else
				vulUit(n);
		}
		
		
		
		if (!(v.lijnkleur == "transparant"))
			penAan(v.lijnkleur);
		vulAan(n,v.vulkleur);
		for (int i = 0; i < v.aantalHoekpunten; i++)
		{	int a=i ; 
			int b=(i+1)%v.aantalHoekpunten;
			stap(-k*(v.punten[a].x-v.punten[b].x), -k*(v.punten[a].y-v.punten[b].y), -k*(v.punten[a].z-v.punten[b].z));
		}
		vulUit(n);
		penUit();
		stap(-k*v.punten[0].x, -k*v.punten[0].y, -k*v.punten[0].z);
		
	}
	public void paint(Graphics g)
  	{ 	bezigMetTekenen = true;
		if (im == null)
		{	breedte = getSize().width;
			hoogte = getSize().height;	
			double startschaal = Math.min((double)breedte/500,(double)hoogte/500);
			mat.initialiseer(0,0,0,startschaal);	
			startpunt = new Punt3D(breedte/2,hoogte/2,0);
			for(int i=0 ; i<5 ; i++)
			{	l[i].maakNulpunt(breedte/2,hoogte/2,0);
			}
			im = createImage(breedte,hoogte);
  			gIm = im.getGraphics();
			tekenOpImage(true);
		}
    	g.drawImage(im, 0, 0, null);
		bezigMetTekenen = false;
  	}
	
  	public void tekenOpImage(boolean wis)
  	{ 	beginpunt = new Punt3D(startpunt);
    	eindpunt = new Punt3D(beginpunt);
		//mat.initialiseer();
	  	gIm.setColor(achtergrondkleur);
    	if(wis)
    		gIm.fillRect(0, 0, breedte, hoogte);
    	penAan(0,0,0);
		vul = false;
    	tekenprogramma();
		for(int i=0 ; i<5 ; i++)
		{	l[i].sorteer();
		}
//		for(int i = 0; i < 200; i++)
//		{	sorteerRij[i] = l[0].sorteerRij[i];
//		}	
		for(int j=0 ; j<5 ; j++)
		{
//System.out.println("v3d l = " + j + " ap = " + l[j].aantalPolygonen);

			for(int i=0 ; i<l[j].aantalPolygonen ; i++)
			{
				
				if (l[j].vlakken[i].normaal.z > 0)
				{	if (schaduw)
					{	double grijsfactor = 0.5*((-l[j].vlakken[i].normaal.x - l[j].vlakken[i].normaal.y + l[j].vlakken[i].normaal.z)/Math.sqrt(3)+1);
						if (grijsfactor < 0)
							grijsfactor = 0;
						if (grijsfactor > 1)
							grijsfactor = 1;
						int roodwaarde = 50+(int)(l[j].vlakken[i].vulkleur.getRed()*grijsfactor*0.75);
						int groenwaarde = 50+(int)(l[j].vlakken[i].vulkleur.getGreen()*grijsfactor*0.75);
						int blauwwaarde = 50+(int)(l[j].vlakken[i].vulkleur.getBlue()*grijsfactor*0.75);
						gIm.setColor(new Color(roodwaarde,groenwaarde,blauwwaarde));
//System.out.println("v3d schaduw");						
					}
					else
					{	gIm.setColor(l[j].vlakken[i].vulkleur);
					}
					if (!l[j].vlakken[i].isLeeg)
					{	gIm.fillPolygon(l[j].vlakken[i].pol);
//System.out.println("vlak niet leeg");					
					}
					gIm.setColor(l[j].vlakken[i].lijnkleur);
					if(!l[j].vlakken[i].isLijn && l[j].vlakken[i].isOmlijnd )
					{	gIm.setColor(l[j].vlakken[i].lijnkleur);
						gIm.drawPolygon(l[j].vlakken[i].pol);
					}
					if (l[j].vlakken[i].isLijn)
					{	//int grw = (int)(125-0.7*l[j].vlakken[i].gemz);
						//gIm.setColor(new Color(grw,grw,grw));
						gIm.setColor(l[j].vlakken[i].lijnkleur);
						gIm.drawPolygon(l[j].vlakken[i].pol);
						penkleur = new Color(0,0,0);
					}
				}
			}
			l[j] = new Lichaam3D();	
			l[j].zetAfstand(afstand);
			l[j].maakNulpunt(breedte/2,hoogte/2,0);
		}
	}
	
	//-------------------------------------------------------------------------------------------
	//deze methoden worden gebruikt door handlers van het leerlingprogramma
	//-------------------------------------------------------------------------------------------
	void tekenOpnieuw()
	{	if (im == null)
			return;
	
//System.out.println("tekenOpnieuw");

		bezigMetTekenen = true;
		tekenOpImage(true);
		Graphics g = getGraphics();
		g.drawImage(im, 0, 0, null); 
		bezigMetTekenen = false;
	}
  
  	void tekenErbij()
	{	bezigMetTekenen = true;
		tekenOpImage(false);
		Graphics g = getGraphics();
		g.drawImage(im, 0, 0, null);
		bezigMetTekenen = false;
	}

	void stap(double dx,double dy,double dz)
	{	naarVolgendPunt(dx,-dy,-dz);
	}
	void naarVolgendPunt(double dx,double dy, double dz)
	{	eindpunt = mat.geefVolgendPunt(beginpunt,dx,dy,dz);
		if(pen && !vul)
		{	l[lnummer].voegPuntToe(beginpunt);
			l[lnummer].voegPuntToe(eindpunt);
			l[lnummer].voegPolygonToe(penkleur,penkleur,true, false);
		}
		if(vul) 	 
		{	l[lnummer].voegPuntToe(beginpunt);
		}
		beginpunt.x = eindpunt.x;
		beginpunt.y = eindpunt.y;
		beginpunt.z = eindpunt.z;
	}
	
	void tekenPolygon()
	{	l[0].voegPolygonToe(vulkleur, penkleur, pen, leeg);
	}
	void tekenPolygon(int n)
	{	l[n].voegPolygonToe(vulkleur, penkleur, pen, leeg);
	}
	void penAan()
	{	pen = true;
	}
	void penAan(String kl)
	{	pen = true;
		penkleur = maakKleur(kl);
		gIm.setColor(penkleur);
	}
	void penAan(int r, int g, int b)
	{	pen = true;
		penkleur = new Color(r,g,b);
		gIm.setColor(penkleur);
	}
	void penAan(int n)
	{	pen = true;
		lnummer=n;
	}
	void penAan(int n,String kl)
	{	pen = true;
		penkleur = maakKleur(kl);
		gIm.setColor(penkleur);
		lnummer=n;
	}
	void penAan(int n,int r, int g, int b)
	{	pen = true;
		penkleur = new Color(r,g,b);
		gIm.setColor(penkleur);
		lnummer=n;
	}
	void penUit()
	{	pen = false;
	}
	void penUit(int n)
	{	pen = false;
		lnummer=0;
	}
	void vulAan()
	{	vul = true;
	}
	void vulAan(String kl)
	{	vul = true;
		if (kl.equals("transparant"))
			leeg = true;
		vulkleur = maakKleur(kl);
	}
	void vulAan(int r, int g, int b)
	{	vul = true;	
		vulkleur = new Color(r,g,b);
	}
	void vulAan(int n)
	{	vul = true;
		lnummer=n;
	}
	void vulAan(int n,String kl)
	{	vul = true;
		lnummer=n;
		if (kl.equals("transparant"))
			leeg = true;
		vulkleur = maakKleur(kl);
	}
	void vulAan(int n,int r, int g, int b)
	{	vul = true;
		lnummer=n;
		vulkleur = new Color(r,g,b);
	}
	void vulAan(Color kl)
	{	vul = true;	
		vulkleur = kl;
	}

	void vulUit()
	{	tekenPolygon();
		vul = false;
		lnummer=0;
		leeg = false;
	}
	void vulUit(int n)
	{	tekenPolygon(n);
		vul = false;
		lnummer=0;
		leeg = false;
	}
	void achtergrondkleur(String kl)
	{	achtergrondkleur = maakKleur(kl);
	}
	void achtergrondkleur(int r, int g, int b)
	{	achtergrondkleur = new Color(r,g,b);
	}
	void zetAchtergrond(Color c)
	{	achtergrondkleur = c;
	}
	void schrijf(String s)
	{	gIm.drawString(s, (int)beginpunt.x, (int)beginpunt.y);
	}
	void schrijf(String s, Font f)
	{	gIm.setFont(f);
		gIm.drawString(s, (int)beginpunt.x, (int)beginpunt.y);
	}
	Punt geefPunt()								// geeft de laatst getekende Punt
	{	double pf = (afstand-beginpunt.z)/afstand;
		double begx = l[0].nulpunt.x + (beginpunt.x-l[0].nulpunt.x)/pf;
		double begy = l[0].nulpunt.y + (beginpunt.y-l[0].nulpunt.y)/pf;
		return new Punt(begx,begy);
	}
	Punt geefPunt(int n)								// geeft de laatst getekende Punt
	{	double pf = (afstand-beginpunt.z)/afstand;
		double begx = l[n].nulpunt.x + (beginpunt.x-l[n].nulpunt.x)/pf;
		double begy = l[n].nulpunt.y + (beginpunt.y-l[n].nulpunt.y)/pf;
		return new Punt(begx,begy);
	}

	Polygon geefVlak()
	{	if(l[0].vlakken[l[0].aantalPolygonen-1].normaal.z > 0)
		return l[0].vlakken[l[0].aantalPolygonen-1].pol;
		else return new Polygon();
	}
	private Color maakKleur(String kl)
	{	if (kl.equals("rood")) 
			return Color.red;
		else if (kl.equals("roodoranje")) 
		{	if (vlakkenKleurenOptie && profielenKleurenOptie && (afstand > 10000))
				return Color.orange;
			else if (vlakkenKleurenOptie && profielenKleurenOptie && (afstand == 1000))
				return Color.red;
			if (vlakkenKleurenOptie && !profielenKleurenOptie && (afstand > 10000))
				return Color.red;
			if (vlakkenKleurenOptie && !profielenKleurenOptie && (afstand == 1000))
				return Color.orange;
			else
				return Color.red;
		}
		else if (kl.equals("oranje"))
			return Color.orange;
		else if (kl.equals("oranjerood"))
			return Color.red;
		else if (kl.equals("roodoranjerood"))
			return Color.red;
		else if (kl.equals("groen")) 
			return Color.green;
		else if (kl.equals("blauw")) 
			return Color.blue;
		else if (kl.equals("geel")) 
			return Color.yellow;
		else if (kl.equals("cyaan")) 
			return Color.cyan;
		else if (kl.equals("roze")) 
			return Color.pink;
		else if (kl.equals("zwart")) 
			return Color.black;
		else if (kl.equals("grijs")) 
			return Color.gray;
		else if (kl.equals("lichtgrijs")) 
			return Color.lightGray;
		else if (kl.equals("magenta")) 
			return Color.magenta;
		else if (kl.equals("wit")) 
			return Color.white;
		else return Color.black;		
	}	
	public void animatie(){}

	public void muisKkActie()
	{	//int extra = 0 ;
		//if (vvRij[1] != null)
		//	extra = vvRij[1].aantalVlakken;						
		//for (int j = vvRij[0].aantalVlakken + extra - 1; j > -1; j--)

//System.out.println("v3d muisKkActie vkoptie = " + vlakkenKleurenOptie);		
		
		for (int j = vvRij[0].aantalVlakken - 1; j > -1; j--)
		{	//int i = sorteerRij[j];
			if (p[j].contains(mb.geefDrukx(),mb.geefDruky()))
			{	
				if (vlakkenKleurenOptie)
				{
					
//System.out.println("v3d muisKkActie in");
//System.out.println("vk " + j + " " + vvRij[0].vlakken[j].vulkleur);

					// een oranje vlak dat rood gekleurd wordt
					if (vvRij[0].vlakken[j].vulkleur.equals("oranje"))
					{	vvRij[0].vlakken[j].vulkleur = "oranjerood";
//System.out.println("muisKkActie oranje wordt oranjerood");					
					}
					// een rood vlak dat weer oranje gekleurd wordt
					else if (vvRij[0].vlakken[j].vulkleur.equals("oranjerood"))
					{	vvRij[0].vlakken[j].vulkleur = "oranje";
//System.out.println("muisKkActie oranjerood wordt oranje");					
					}
					// dit vlak is elders rood gemaakt maar ziet er oranje uit, het wordt nu rood
					else if (vvRij[0].vlakken[j].vulkleur.equals("roodoranje"))
					{	vvRij[0].vlakken[j].vulkleur = "roodoranjerood";
//System.out.println("muisKkActie roodoranje wordt roodoranjerood");					
					}
					// dit is een elders rood gemaakt vlak dat rood gekleurd is, het wordt nu oranje
					else if (vvRij[0].vlakken[j].vulkleur.equals("roodoranjerood"))
					{	vvRij[0].vlakken[j].vulkleur = "roodoranje";
//System.out.println("muisKkActie roodoranjerood wordt roodoranje");					
					}
				
				
				}
				tekenOpnieuw();
				return;
			}
		}
	}
	
	public void muisSleepActie()
	{	if (muisAan)
		{	xhoek -= 0.5*mb.geefSleepdy();
			if (restrictRotation)
			{	if (xhoek > 90 - beginx)
					xhoek = 90 - beginx;
				if (xhoek < -90 - beginx)
					xhoek = -90 - beginx;
				
//System.out.println("beginx = " + beginx);
//System.out.println("xhoek = " + xhoek);
			}	
			yhoek += 0.5*mb.geefSleepdx();
			tekenOpnieuw();
		}
	}
	public void muisDrukActie(){}
	public void muisKlikActie(){}
	public void muisLosActie()
	{	if ((klikAan && (mb.geefDrukx()-mb.geefX())*(mb.geefDrukx()-mb.geefX()) + 
			            (mb.geefDruky()-mb.geefY())*(mb.geefDruky()-mb.geefY()) < 10))
		{	muisKkActie();
		}
		tekenOpnieuw();
	}
}

	
	
	
    









	

