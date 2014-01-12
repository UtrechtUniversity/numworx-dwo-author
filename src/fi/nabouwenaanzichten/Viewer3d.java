package fi.nabouwenaanzichten;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class Viewer3d extends JComponent
{
    private NabouwenAanzichtenIF eigenaar;
    private GetalRooster gr;
    //private AnimatieBeheerder ab;
    private MuisBeheerder mb;
    private int breedte,hoogte;
    private Punt3D beginpunt,eindpunt,startpunt;
    public Lichaam3D[] l;
    private Image im ;
    public Graphics gIm ;
    public Matrix3D mat;  
    private boolean pen, vul,leeg, schaduw, grZichtbaar;
    private int lnummer;
    private Color penkleur,
    			  vulkleur,
    			  achtergrondkleur;
    public boolean bezigMetTekenen, muisAan, klikAan,pijlAan,balkAan, maakAanzicht;
    private double afstand;
    int aantalVeelvlakken;
    Veelvlak[] vvRij;
    Polygon[][] p;
    Polygon[][][][] pp;
    Klikvlak[] kv;
    int aantalKv;
    KubusRooster kr;
    private double k, xhoek,yhoek, beginx, beginy;
    private int[] sorteerRij;
    private CubeRemoveThread cubeRemoveThread;
    private boolean removed = false;
    private boolean removing = false;
    
    
    public Viewer3d(KubusRooster kr, int x, int y,int b, int h, NabouwenAanzichtenIF hb)
    {   setBounds(x,y,b,h);
        breedte = getSize().width;
        hoogte = getSize().height;  
        aantalVeelvlakken = 0;
        eigenaar = hb;
        
        this.kr = kr;
        int n = kr.maxAantal;
        p = new Polygon[n][n];
        pp = new Polygon[n][n][n][6];
        aantalKv = 0;
        kv = new Klikvlak[n*n*n*7];
        for(int i=0 ; i<kr.maxAantal ; i++)
        {   for(int j=0 ; j<kr.maxAantal ; j++)
            {   for(int k=0 ; k<kr.maxAantal ; k++)
                {   for(int m=0 ; m<6 ; m++)
                    {   pp[i][j][k][m] = new Polygon();
                    }
                }
            }
        }
        
        mb = new MuisBeheerder(this);
        addMouseListener(mb);
        addMouseMotionListener(mb);
        achtergrondkleur = Color.white;
        leeg = false;
        schaduw = true;
        muisAan = true;
        klikAan = true;
        pijlAan = true;
        balkAan = false;
        maakAanzicht = false;
        grZichtbaar = false;
        l = new Lichaam3D[5];
        afstand = 1000;
        lnummer=0;
        for(int i=0 ; i<5 ; i++)
        {   l[i] = new Lichaam3D();
            l[i].zetAfstand(afstand);
        }
        sorteerRij = new int[2000];
        mat = new Matrix3D();
        //if(mb!=null && ab!=null)              
        //{ mb.meldAnimatieBeheerder(ab);       
        //}
        k=230;
        xhoek = 0;
        yhoek = 0;
        beginx = 30;
        beginy = -30;
    }
    
    public void zetAfstand(double afst)
    {   afstand = afst;
        for(int i=0 ; i<5 ; i++)
            {   l[i].zetAfstand(afst);
            }
    }
    public void zetSchaduw(boolean s)
    {   schaduw = s;
    }
    public void zetBeginHoeken(double hx, double hy)
    {   beginx = hx;
        beginy = hy;
        xhoek = 0;
        yhoek = 0;
    }
    
    public double getXHoek()
    {   return beginx + xhoek;
    }
    public double getYHoek()
    {   return beginy + yhoek;
    }
    public void zetMuisAan(boolean b)
    {   muisAan = b;
    }
    public void zetKlikAan(boolean b)
    {   klikAan = b;
    }
    public void zetPijlAan(boolean b)
    {   pijlAan = b;
        if (pijlAan)balkAan = false;
    }
    public void zetMaakAanzicht(boolean b)
    {   maakAanzicht = b;
	    if (maakAanzicht)
	    {	zetBeginHoeken(90,0);
	    	zetAfstand(1000000000);
	    	zetMuisAan(false);
	    	zetSchaduw(false);
	    }
	    else
	    {	zetBeginHoeken(30,-30);
	    	zetAfstand(1000);
	    	zetMuisAan(true);
	    	zetSchaduw(true);
	    }
    }
    public void zetBalkAan(boolean b)
    {   balkAan = b;
        if (balkAan)
            pijlAan = false;
    }
    public void zetKubusRooster(KubusRooster kur)
    {   kr = kur;
        if(grZichtbaar)
        {   if(gr!=null)remove(gr);
            zetGetalRooster(true);
            for(int i=0 ; i<kr.maxAantal ; i++)
            {   for(int j=0 ; j<kr.maxAantal ; j++)
                {   for(int k=0 ; k<kr.maxAantal ; k++)
                    {   if(kr.kubussen[i][j][k]!=null && gr.geefHoogte(i,j)<k+1)
                        {   gr.zetHoogte(i,j,k);
                        }
                    }
                }
            }
        }
        int n = kr.maxAantal;
        p = new Polygon[n][n];
        pp = new Polygon[n][n][n][6];
        for(int i=0 ; i<kr.maxAantal ; i++)
        {   for(int j=0 ; j<kr.maxAantal ; j++)
            {   for(int k=0 ; k<kr.maxAantal ; k++)
                {   for(int m=0 ; m<6 ; m++)
                    {   pp[i][j][k][m] = new Polygon();
                    }
                }
            }
        }
        aantalKv = 0;
        kv = new Klikvlak[n*n*n*7];
        if(im!=null)tekenOpnieuw();
    }
    
    public void zetHoogtes()
    {   if (gr != null)
        {   for (int i = 0; i < kr.maxAantal; i++)
            {   for (int j = 0; j < kr.maxAantal; j++)
                {   for (int k = 0; k < kr.maxAantal; k++)
                    {   if (kr.kubussen[i][j][k] != null && gr.geefHoogte(i,j) < k + 1)
                        {   gr.zetHoogte(i, j, k);
                        }
                    }
                }
            }
        
        }
        
    }
    
    public void wisHoogtes()
    {
    	if (gr != null)
    		gr.wis();
    }
    
    public void zetVeelvlak(Veelvlak v)
    {   vvRij[0] = v;
        tekenOpnieuw();
    }
    public void zetVeelvlak(Veelvlak v, int n)
    {   vvRij[n] = v;
        tekenOpnieuw();
    }
    public void zetGetalRooster(boolean bool)
    {   grZichtbaar = bool;
        if(bool)
        {   int n = kr.maxAantal;
            int x = breedte*80/300;
            int b = breedte*140/300;
            gr = new GetalRooster(n,x,x,b);
            add(gr);
        }
        else
        {   if(gr!=null)
        		remove(gr);
        }
        zetBeginHoeken(90,0);
        zetAfstand(1000000000);
        zetMuisAan(false);
        zetSchaduw(false);
    }
    
    public void zetGetalRooster2(boolean bool)
    {   grZichtbaar = bool;
        if (bool)
        {   
        	int n = kr.maxAantal;

            int x = breedte * 80 / 300;
            int y = hoogte * 80 / 300;
            int b = Math.min(hoogte,breedte) * 140 / 300;

            if (hoogte < breedte)
            {   x += (breedte - hoogte) * 40 / 300;
            
            }
            else // hoogte > breedte
            {   y += (hoogte - breedte) * 40 / 300;
            }
            
            gr = new GetalRooster(n, x, y, b);
            //add(gr);
            
//System.out.println("breedte = " + breedte);
//System.out.println("hoogte = " + hoogte);
//System.out.println("x = " + x);
//System.out.println("y = " + y);
//System.out.println("b = " + b);
        }
        else
        {   if (gr != null)
            {   //remove(gr);
                gr = null;
            }
        }
        if (bool)
        {	zetBeginHoeken(90,0);
        	zetAfstand(1000000000);
        	zetMuisAan(false);
        	zetSchaduw(false);
        }
    }
    
    public void voegVeelvlakToe(Veelvlak v)
    {   vvRij[aantalVeelvlakken] = v;
        aantalVeelvlakken++;
    }
    
    void tekenprogramma()
    {   mat.initialiseer();
        mat.xdraai(beginx+xhoek);mat.ydraai(beginy+yhoek);
        tekenKubusRooster();        
    }
    
    void tekenKubusRooster()
    {   aantalKv = 0;
        if(!maakAanzicht)tekenVeelvlak(0, kr.grondvlak);
        if(pijlAan)
        {
            for(int i=0 ; i<kr.pijl.aantalVlakken ; i++)
            {   tekenVlak(1,kr.pijl.vlakken[i]);
                kv[aantalKv] = new Klikvlak(i,0,1,6);
                aantalKv++;
            }
        }
        if (balkAan)
        {
            for (int i = 0; i < kr.balk.aantalVlakken; i++)
            {   tekenVlak(1, kr.balk.vlakken[i]);
                kv[aantalKv] = new Klikvlak(i, 0, 1, 6);
                aantalKv++;
            }
        }
        for(int i=0 ; i<kr.maxAantal ; i++)
        {   for(int j=0 ; j<kr.maxAantal ; j++)
            {   if(maakAanzicht) 
            	{	kr.vierkanten[i][j].vlakken[0].vulkleur = "wit";
            		kr.vierkanten[i][j].vlakken[0].vorigeKleur = "wit";
            	}
	            else
	            {  	kr.vierkanten[i][j].vlakken[0].vorigeKleur = "lichtgrijs";
	            	kr.vierkanten[i][j].vlakken[0].vulkleur = "lichtgrijs";
	            }
        		tekenVlak(1, kr.vierkanten[i][j].vlakken[0]);
                p[i][j] = geefVlak(1);
                kv[aantalKv] = new Klikvlak(i,j,0,6);
                aantalKv++;
                for(int k=0 ; k<kr.maxAantal ; k++)
                {   if(kr.kubussen[i][j][k] !=null)
                    {   for(int m=0 ; m<6 ; m++)
                        {   if(kr.kubussen[i][j][k].isOnbedekt[m])
                            {   tekenVlak(1, kr.kubussen[i][j][k].vlakken[m]);
                                pp[i][j][k][m] = geefVlak(1);
                                kv[aantalKv] = new Klikvlak(i,j,k,m);
                                aantalKv++;
                            }
                        }
                    }
                }
            }
        }
    }
    void tekenVeelvlak(int n,Veelvlak vv)
    {   for(int i=0 ; i<vv.aantalVlakken ; i++)
        {   tekenVlak(n,vv.vlakken[i]);
        }
    }
    void tekenVlak(int n,Vlak v)
    {   penUit();
        stap(k*v.punten[0].x, k*v.punten[0].y, k*v.punten[0].z);
        if(!(v.lijnkleur=="transparant"))penAan(v.lijnkleur);
        vulAan(n,v.vulkleur);
        for(int i=0 ; i<v.aantalHoekpunten ; i++)
        {   int a=i ; int b=(i+1)%v.aantalHoekpunten;
            stap(-k*(v.punten[a].x-v.punten[b].x), -k*(v.punten[a].y-v.punten[b].y), -k*(v.punten[a].z-v.punten[b].z));
        }
        vulUit(n);
        penUit();
        stap(-k*v.punten[0].x, -k*v.punten[0].y, -k*v.punten[0].z);
        
    }
    
    public void setSize(int width, int height)
    {
    	im = null;
    	super.setSize(width, height);
    	breedte = getSize().width;
    	hoogte = getSize().height;
    }
    
    public void setBounds(int x, int y, int w, int h)
    {
    	im = null;
    	super.setBounds(x, y, w, h);
    	breedte = getSize().width;
    	hoogte = getSize().height;
    	
    }
    
    public void paint(Graphics g)
    {   bezigMetTekenen = true;
        if (im == null)
        {   breedte = getSize().width;
            hoogte = getSize().height;  
            // breedte/400 ipv breedte/500, dan past de langwerpige kr beter
            double startschaal = Math.min((double)breedte/400,(double)hoogte/500);
            mat.initialiseer(0,0,0,startschaal);    
            startpunt = new Punt3D(breedte/2,hoogte/2,0);
            for(int i=0 ; i<5 ; i++)
            {   l[i].maakNulpunt(breedte/2,hoogte/2,0);
            }
            im = createImage(breedte,hoogte);
            gIm = im.getGraphics();
            tekenOpImage(true);
        }
        g.drawImage(im, 0, 0, null);
        bezigMetTekenen = false;
    }
    
    public void tekenOpImage(boolean wis)
    {   if (gIm == null)
    		return;
        beginpunt = new Punt3D(startpunt);
        eindpunt = new Punt3D(beginpunt);
        //mat.initialiseer();
        gIm.setColor(achtergrondkleur);
        if (wis) 
        	gIm.fillRect(0, 0, breedte, hoogte);
        penAan(0,0,0);
        
//gIm.drawRect(0, 0, breedte-1, hoogte-1);

        vul = false;
        tekenprogramma();
        for(int i=0 ; i<5 ; i++)
        {   l[i].sorteer();
        }
        for(int i=0 ; i<2000 ; i++)
        {   sorteerRij[i] = l[1].sorteerRij[i];
        }   
        for(int j=0 ; j<5 ; j++)
        {
            for(int i=0 ; i<l[j].aantalPolygonen ; i++)
            {
                if(l[j].vlakken[i].normaal.z >0)
                {   if(schaduw)
                    {   double grijsfactor = 0.5*((-l[j].vlakken[i].normaal.x - l[j].vlakken[i].normaal.y + l[j].vlakken[i].normaal.z)/Math.sqrt(3)+1);
                        if(grijsfactor<0)grijsfactor=0;if(grijsfactor>1)grijsfactor=1;
                        int roodwaarde = 50+(int)(l[j].vlakken[i].vulkleur.getRed()*grijsfactor*0.75);
                        int groenwaarde = 50+(int)(l[j].vlakken[i].vulkleur.getGreen()*grijsfactor*0.75);
                        int blauwwaarde = 50+(int)(l[j].vlakken[i].vulkleur.getBlue()*grijsfactor*0.75);
                        gIm.setColor(new Color(roodwaarde,groenwaarde,blauwwaarde));
                    }
                    else
                    {   gIm.setColor(l[j].vlakken[i].vulkleur);
                    }
                    if(!l[j].vlakken[i].isLeeg)gIm.fillPolygon(l[j].vlakken[i].pol);
                    gIm.setColor(l[j].vlakken[i].lijnkleur);
                    if(!l[j].vlakken[i].isLijn && l[j].vlakken[i].isOmlijnd )
                    {   gIm.setColor(l[j].vlakken[i].lijnkleur);
                        gIm.drawPolygon(l[j].vlakken[i].pol);
                    }
                    if (l[j].vlakken[i].isLijn)
                    {   //int grw = (int)(125-0.7*l[j].vlakken[i].gemz);
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
        //super.paint(gIm);
        if (gr != null)
            gr.paintComponent(gIm);
    }
    
    //-------------------------------------------------------------------------------------------
    //deze methoden worden gebruikt door handlers van het leerlingprogramma
    //-------------------------------------------------------------------------------------------
    void tekenOpnieuw()
    {   bezigMetTekenen = true;
        tekenOpImage(true);
        Graphics g = getGraphics();
        if (g != null)
        	g.drawImage(im, 0, 0, null); 
        bezigMetTekenen = false;
    }
  
    void tekenErbij()
    {   bezigMetTekenen = true;
        tekenOpImage(false);
        Graphics g = getGraphics();
        g.drawImage(im, 0, 0, null);
        bezigMetTekenen = false;
    }

    void stap(double dx,double dy,double dz)
    {   naarVolgendPunt(dx,-dy,-dz);
    }
    void naarVolgendPunt(double dx,double dy, double dz)
    {   eindpunt = mat.geefVolgendPunt(beginpunt,dx,dy,dz);
        if(pen && !vul)
        {   l[lnummer].voegPuntToe(beginpunt);
            l[lnummer].voegPuntToe(eindpunt);
            l[lnummer].voegPolygonToe(penkleur,penkleur,true, false);
        }
        if(vul)      
        {   l[lnummer].voegPuntToe(beginpunt);
        }
        beginpunt.x = eindpunt.x;
        beginpunt.y = eindpunt.y;
        beginpunt.z = eindpunt.z;
    }
    
    void tekenPolygon()
    {   l[0].voegPolygonToe(vulkleur, penkleur, pen, leeg);
    }
    void tekenPolygon(int n)
    {   l[n].voegPolygonToe(vulkleur, penkleur, pen, leeg);
    }
    void penAan()
    {   pen = true;
    }
    void penAan(String kl)
    {   pen = true;
        penkleur = maakKleur(kl);
        gIm.setColor(penkleur);
    }
    void penAan(int r, int g, int b)
    {   pen = true;
        penkleur = new Color(r,g,b);
        gIm.setColor(penkleur);
    }
    void penAan(int n)
    {   pen = true;
        lnummer=n;
    }
    void penAan(int n,String kl)
    {   pen = true;
        penkleur = maakKleur(kl);
        gIm.setColor(penkleur);
        lnummer=n;
    }
    void penAan(int n,int r, int g, int b)
    {   pen = true;
        penkleur = new Color(r,g,b);
        gIm.setColor(penkleur);
        lnummer=n;
    }
    void penUit()
    {   pen = false;
    }
    void penUit(int n)
    {   pen = false;
        lnummer=0;
    }
    void vulAan()
    {   vul = true;
    }
    void vulAan(String kl)
    {   vul = true;
        if(kl.equals("transparant"))leeg = true;
        vulkleur = maakKleur(kl);
    }
    void vulAan(int r, int g, int b)
    {   vul = true; 
        vulkleur = new Color(r,g,b);
    }
    void vulAan(int n)
    {   vul = true;
        lnummer=n;
    }
    void vulAan(int n,String kl)
    {   vul = true;
        lnummer=n;
        if(kl.equals("transparant"))leeg = true;
        vulkleur = maakKleur(kl);
    }
    void vulAan(int n,int r, int g, int b)
    {   vul = true;
        lnummer=n;
        vulkleur = new Color(r,g,b);
    }
    void vulAan(Color kl)
    {   vul = true; 
        vulkleur = kl;
    }

    void vulUit()
    {   tekenPolygon();
        vul = false;
        lnummer=0;
        leeg = false;
    }
    void vulUit(int n)
    {   tekenPolygon(n);
        vul = false;
        lnummer=0;
        leeg = false;
    }
    void achtergrondkleur(String kl)
    {   achtergrondkleur = maakKleur(kl);
    }
    void achtergrondkleur(int r, int g, int b)
    {   achtergrondkleur = new Color(r,g,b);
    }
    void zetAchtergrond(Color c)
    {   achtergrondkleur = c;
    }
    void schrijf(String s)
    {   gIm.drawString(s, (int)beginpunt.x, (int)beginpunt.y);
    }
    void schrijf(String s, Font f)
    {   gIm.setFont(f);
        gIm.drawString(s, (int)beginpunt.x, (int)beginpunt.y);
    }
    Punt geefPunt()                             // geeft de laatst getekende Punt
    {   double pf = (afstand-beginpunt.z)/afstand;
        double begx = l[0].nulpunt.x + (beginpunt.x-l[0].nulpunt.x)/pf;
        double begy = l[0].nulpunt.y + (beginpunt.y-l[0].nulpunt.y)/pf;
        return new Punt(begx,begy);
    }
    Punt geefPunt(int n)                                // geeft de laatst getekende Punt
    {   double pf = (afstand-beginpunt.z)/afstand;
        double begx = l[n].nulpunt.x + (beginpunt.x-l[n].nulpunt.x)/pf;
        double begy = l[n].nulpunt.y + (beginpunt.y-l[n].nulpunt.y)/pf;
        return new Punt(begx,begy);
    }

    Polygon geefVlak()
    {   if(l[0].vlakken[l[0].aantalPolygonen-1].normaal.z > 0)
        return l[0].vlakken[l[0].aantalPolygonen-1].pol;
        else return new Polygon();
    }
    Polygon geefVlak(int n)
    {   if(l[n].vlakken[l[n].aantalPolygonen-1].normaal.z > 0)
        return l[n].vlakken[l[n].aantalPolygonen-1].pol;
        else return new Polygon();
    }
    private Color maakKleur(String kl)
    {   if(kl.equals("rood")) return Color.red;
        else if(kl.equals("groen")) return Color.green;
        else if(kl.equals("blauw")) return Color.blue;
        else if(kl.equals("geel")) return Color.yellow;
        else if(kl.equals("cyaan")) return Color.cyan;
        else if(kl.equals("roze")) return Color.pink;
        else if(kl.equals("zwart")) return Color.black;
        else if(kl.equals("grijs")) return Color.gray;
        else if(kl.equals("lichtgrijs")) return Color.lightGray;
        else if(kl.equals("magenta")) return Color.magenta;
        else if(kl.equals("wit")) return Color.white;
        else if(kl.equals("oranje")) return Color.orange;
        else return Color.black;        
    }   
    public void animatie(){}
    
    public void muisSleepActie()
    {   if(removed) return;
        if(muisAan)
        {   xhoek -= 0.5*mb.geefSleepdy();
            if(xhoek>90-beginx)xhoek=90-beginx;
            if(xhoek<0-beginx)xhoek=0-beginx;
            yhoek += 0.5*mb.geefSleepdx();
            tekenOpnieuw();
        }
    }
    public void muisKkActie(MouseEvent e, boolean remove)
    {   
        for(int q=aantalKv-1 ; q>-1 ; q--)
        {   int n = sorteerRij[q];
            if(kv[n].m == 6 && kv[n].k == 0 && p[kv[n].i][kv[n].j].contains(mb.geefDrukx(),mb.geefDruky()))
            {   if(eigenaar.isBouwen() && !(e.getModifiers()== e.BUTTON3_MASK || e.isControlDown() || remove))
                {   kr.voegKubusToe(kv[n].i,kv[n].j,0);
                    if(gr!=null)gr.verhoog(kv[n].i,kv[n].j);
                }
                else
                {   kr.verwijderKubus(kv[n].i,kv[n].j,0);
                    if(gr!=null)gr.verlaag(kv[n].i,kv[n].j);
                }
                return;
            }
            else if(kv[n].m != 6 && pp[kv[n].i][kv[n].j][kv[n].k][kv[n].m].contains(mb.geefDrukx(),mb.geefDruky()))
            {   if(eigenaar.isBouwen() && !(e.getModifiers()== e.BUTTON3_MASK || e.isControlDown() ||remove) && !maakAanzicht)
                {   if(kv[n].m==0)
                    {   kr.voegKubusToe(kv[n].i,kv[n].j,kv[n].k+1);
                        if(gr!=null)gr.verhoog(kv[n].i,kv[n].j);
                    }
                	else if(kv[n].m==1)
                    {   kr.voegKubusToe(kv[n].i,kv[n].j-1,kv[n].k);
                    }
                	else if(kv[n].m==2)
                    {   kr.voegKubusToe(kv[n].i+1,kv[n].j,kv[n].k);
                    }
                	else if(kv[n].m==3)
                    {   kr.voegKubusToe(kv[n].i,kv[n].j+1,kv[n].k);
                    }
                	else if(kv[n].m==4)
                    {   kr.voegKubusToe(kv[n].i-1,kv[n].j,kv[n].k);
                    }
                	else if(kv[n].m==5)
                    {   kr.voegKubusToe(kv[n].i,kv[n].j,kv[n].k-1);
                    }
                }
                else
                {   kr.verwijderKubus(kv[n].i,kv[n].j,kv[n].k);
                    if(gr!=null)gr.verlaag(kv[n].i,kv[n].j);
                }
                return;
            }
        }
    }
    public void muisDrukActie(MouseEvent e){
        if(removing) return;
        removing = true;
        if(cubeRemoveThread!=null)
        {   cubeRemoveThread.maakDood();
            cubeRemoveThread=null;
        }
        cubeRemoveThread = new CubeRemoveThread(e);
        cubeRemoveThread.start();
        
        
    }
    public void muisKlikActie(){}
    public void muisLosActie(MouseEvent e)
    {   removing = false;
        if(!removed && (klikAan && (mb.geefDrukx()-mb.geefX())*(mb.geefDrukx()-mb.geefX()) + (mb.geefDruky()-mb.geefY())*(mb.geefDruky()-mb.geefY()) < 16) )
        {   muisKkActie(e, false);
            eigenaar.zetVeranderd();
        }
        removed = false;
    }
    
    class CubeRemoveThread extends Thread 
    {   
        final MouseEvent ee;
        boolean dood = false;
        
        public CubeRemoveThread(MouseEvent e)
        {   ee = e;
        }
        
        public void run()
        {   try
            {   sleep(500);
            }
            catch(InterruptedException e)    
            { }
            if((removing && !dood && klikAan && (mb.geefDrukx()-mb.geefX())*(mb.geefDrukx()-mb.geefX()) + (mb.geefDruky()-mb.geefY())*(mb.geefDruky()-mb.geefY()) < 16) )
            {   removed = true;
                muisKkActie(ee, true);
                eigenaar.zetVeranderd();    
            }
            removing = false;
        }
        public void maakDood()
        {   dood = true;
        }
    }
}

    
    
    
    

