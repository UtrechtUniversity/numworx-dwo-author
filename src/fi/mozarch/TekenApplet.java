package fi.mozarch;


import java.awt.*;
import java.awt.event.*;
import java.applet.Applet;
//import grnuminput.*;


public class TekenApplet extends Applet 
{
	//private Regelaar rg;
	Tekenblad tb;
	private AnimatieBeheerder ab;
	private MuisBeheerder mb;
	//private TraceBeheerder trb;
	
	private boolean initializing;
	
	//-----------------------------------------------------------------------------------------
	// initalisatie
	//-----------------------------------------------------------------------------------------
	public void init()
	{	tb = new Tekenblad(this);
		//rg = new Regelaar(this);
		this.setLayout(new BorderLayout(0,0));
		initializing = true;
		initialiseer();							// wordt geimplementeerd in leerlingprogramma
		initializing = false;
		add(tb,"Center");
		//add(rg,"East");
		
											//
		if(mb!=null && ab!=null)				//
		{	mb.meldAnimatieBeheerder(ab);		// 
		}										//
												//
	}												
	//-------------------------------------------------------------------------------------------
	//deze methoden kunnen alleen worden gebruikt in  "initialiseer()" van leerling-applet
	//-------------------------------------------------------------------------------------------
	public void maakAnimatieMogelijk()					
	{	if(initializing)						 
		{	ab = new AnimatieBeheerder(this);	
			add(ab,"North");					
		}										
	}											
	public void maakMuisActieMogelijk()
	{	if(initializing) 
		{	mb = new MuisBeheerder(this);
			tb.addMouseListener(mb);
			tb.addMouseMotionListener(mb);
		}
	}
	//public void maakTraceMogelijk()
	//{	if(initializing) 
	//	{	trb = new TraceBeheerder(tb,rg);
	//		add(trb,"South");
	//	}
	//}
	//public InvoerVariabele nieuweInvoerVariabele(String n, double mn, double mx, double val)
	//{	return rg.nieuweInvoerVariabele( n,  mn,  mx,  val);
	//}
	//-------------------------------------------------------------------------------------------
	//deze methoden worden gebruikt in de muishandler en  doorgegeven aan MuisBeheerder mb
	//-------------------------------------------------------------------------------------------
	public int geefSleepdx(){return mb.geefSleepdx();}
	public int geefSleepdy(){return mb.geefSleepdy();}
	public int geefDrukx(){return mb.geefDrukx();}
	public int geefDruky(){return mb.geefDruky();}

	//-------------------------------------------------------------------------------------------
	//deze methoden worden gebruikt in de animatiehandler en doorgegeven aan AnimatieBeheerder ab
	//-------------------------------------------------------------------------------------------
	public void pauze(int millisec){ab.pauze(millisec);}
	public boolean animatieStatus(){if (ab!=null)return ab.animatieStatus();else return false;}

	//-------------------------------------------------------------------------------------------
	//deze methoden worden gebruikt in de animatiehandler en muishandlers en doogegeven aan 
	//Tekenblad
	//-------------------------------------------------------------------------------------------
	public void tekenOpnieuw(){tb.tekenOpnieuw();}
	public void tekenErbij(){tb.tekenErbij();}
	
  	//-------------------------------------------------------------------------------------------
	//deze methoden worden gebruikt in "initialiseer" en doorgegeven aan Tekenblad tb (of 
	//Matrix2d) 
	//-------------------------------------------------------------------------------------------
	public void schaal(double s)
	{	tb.mat.schaal(s);
	}
	public void achtergrondkleur(String kl)
	{	tb.achtergrondkleur(kl);
	}
	public void achtergrondkleur(int r, int g, int b)
	{	tb.achtergrondkleur(r, g, b);
	}
	//-------------------------------------------------------------------------------------------
	//deze methoden worden gebruikt in "tekenprogramma()" en doorgegeven aan Tekenblad tb (of 
	//Matrix2d) 
	//-------------------------------------------------------------------------------------------
 	public void links(double dHoek)
	{	tb.mat.draai(dHoek);
		
	}
  	public void rechts(double dHoek)
	{	tb.mat.draai(-dHoek);		
		
	}
	public void vooruit(double dy)
	{	tb.naarVolgendPunt(0,-dy);	
		
	}
	public void stapy(double dy)
	{	tb.naarVolgendPunt(0,-dy);		
		
	}
	public void stapx(double dx)
	{	tb.naarVolgendPunt(dx,0);		
		
	}
	public void stap(double dx,double dy)
	{	tb.naarVolgendPunt(dx,-dy);
		
	}
	public void penAan()
	{	tb.penAan();							
		
	}
	public void penAan(String kl)
	{	tb.penAan(kl);				
		
	}
	public void penAan(Color kl)
	{	tb.penAan(kl);				
		
	}
	public void penAan(int r, int g, int b)
	{	tb.penAan(r, g, b);	
		
	}
	public void penUit()
	{	tb.penUit();							
		
	}
	public void vulAan()
	{	tb.vulAan();							
		
	}
	public void vulAan(String kl)
	{	tb.vulAan(kl);				
		
	}
	public void vulAan(Color kl)
	{	tb.vulAan(kl);				
		
	}
	public void vulAan(int r, int g, int b)
	{	tb.vulAan(r, g, b);	
		
	}
	public void vulUit()
	{	tb.vulUit();							
		
	}
	public Polygon geefVlak()
	{	return tb.geefVlak();
	}
	public Punt geefPunt()
	{	return tb.geefPunt();
	}
	//-------------------------------------------------------------------------------------------
	//deze methoden worden geimplemeteerd in het leerlingenprogramma
	//-------------------------------------------------------------------------------------------
	public void tekenprogramma(){}
	public void initialiseer(){}
	public void animatie(){}
	public void muisSleepActie(){}
	public void muisDrukActie(){}
	public void muisLosActie(){}
	//public void invoerVarActie(InvoerVariabele iv){}
}		
	
/*class Regelaar extends Panel
{	
	private TekenApplet eigenaar;
	private GridBagLayout gridbag;
	private GridBagConstraints c;
	private int aantalInvoerVars;
	private int maxAantalInvoerVars;
	private NumberArrow[] invoerComponenten;
	
	public Regelaar(TekenApplet ap)
	{	eigenaar = ap;
		maxAantalInvoerVars = 10;
		invoerComponenten = new NumberArrow[maxAantalInvoerVars];
		aantalInvoerVars = 0;
		gridbag = new GridBagLayout();
		c = new GridBagConstraints();
		setLayout(gridbag);
		c.insets = new Insets(5, 5, 5, 5); 			
		c.anchor = GridBagConstraints.NORTHWEST;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.weighty = 0.0;
		c.weightx = 0.0;
	}	
	//-----------------------------------------------------------------------------------------
	// nieuwe InvoerVariabelen worden hier gemaakt, en op het panel geplaatst 
	//-----------------------------------------------------------------------------------------
	public InvoerVariabele nieuweInvoerVariabele(String n, double mn, double mx, double val)
	{	InvoerVariabele iv = new InvoerVariabele(n,mn,mx,val);
		iv.ontvangEigenaar(eigenaar);
		NumberArrow invoercomp = new NumberArrow(iv.geefMin(), iv.geefMax(), iv.geefWaarde(), 1, 0, iv.geefNaam(), "");
		invoerComponenten[aantalInvoerVars] = invoercomp;
		aantalInvoerVars++;
		invoercomp.addNumberListener(iv);
		gridbag.setConstraints(invoercomp, c);
		add(invoercomp);
		return iv;
	}
	//-----------------------------------------------------------------------------------------
	// wordt gebruikt door de tracebeheerder om de InvoerVariabelen tijdens de trace uit te zetten
	//-----------------------------------------------------------------------------------------
	public void setEnableInvVars(boolean b)
	{	for(int i=0 ; i<aantalInvoerVars ; i++)
		{	invoerComponenten[i].setEnabled(b);
		}
	}
}*/

class Tekenblad extends Canvas
{
	private int breedte,hoogte;
	private Punt beginpunt,eindpunt,startpunt;
  	private Polygon veelvlak;
  	private Image im ;
  	private Graphics gIm ;
	public Matrix2D mat;  
	private TekenApplet eigenaar;
	private boolean pen, vul;
  	private Color penkleur,vulkleur,achtergrondkleur;
	  
	public Tekenblad(TekenApplet ap)
	{	achtergrondkleur = Color.white;
		veelvlak = new Polygon();
		eigenaar = ap;
		mat = new Matrix2D();					// zorgt voor de tekenrichting
	}
	
	
	//-------------------------------------------------------------------------------------------
	//deze methoden worden gebruikt door het Tekenblad: om de image te initialiseren en
	//op het scherm te zetten. "paint()" wordt alleen bij de eerste keer tekenen gebruikt, daarna 
	//zorgt "tekenOpnieuw()" of "tekenErbij()" hiervoor. "TekenOpImage()" zorgt voor het vullen 
	//van de image, metbehulp van het door de leerlingen geimplementeerde "tekenprogramma()",
	//en wordt zowel door "paint()" als door "tekenOpImage()" gebruikt
	//-------------------------------------------------------------------------------------------
  	public void paint(Graphics g)
  	{ 	if(im==null)
		{	breedte = getSize().width;
			hoogte = getSize().height;	
			//double startschaal = Math.min((double)breedte/500,(double)hoogte/500);
			///mat.initialiseer(0,startschaal);	
			startpunt = new Punt(breedte/2,hoogte/2);
 			im = createImage(breedte,hoogte);
  			gIm = im.getGraphics();
			tekenOpImage(true);
		}
    	
  	}
	  
  	public void tekenOpImage(boolean wis)
  	{ 	if(im==null)return;
        beginpunt = new Punt(startpunt);
    	eindpunt = new Punt(beginpunt);
    	mat.initialiseer();
	  	gIm.setColor(achtergrondkleur);
    	if(wis)gIm.fillRect(0, 0, breedte, hoogte);
    	penAan(0,0,0);
		vul = false;
    	vulkleur = Color.black;
    	eigenaar.tekenprogramma();
	}
	public void zetStart()
	{	beginpunt = new Punt(startpunt);
    	eindpunt = new Punt(beginpunt);
	}
	//-------------------------------------------------------------------------------------------
	//deze methoden worden gebruikt door handlers van het leerlingprogramma
	//-------------------------------------------------------------------------------------------
 	void tekenOpnieuw()
	{	tekenOpImage(true);
		Graphics g = getGraphics();
		
	}
  	void tekenErbij()
	{	tekenOpImage(false);
		Graphics g = getGraphics();
		
	}
	//-------------------------------------------------------------------------------------------
	//deze methoden worden gebruikt door het Tekenblad om de lijnen en vlakken te tekenen
	//-------------------------------------------------------------------------------------------
	void naarVolgendPunt(double dx,double dy)
	{	eindpunt = mat.geefVolgendPunt(beginpunt,dx,dy);
		if(pen)gIm.drawLine((int)beginpunt.x,(int)beginpunt.y,(int)eindpunt.x,(int)eindpunt.y);
		if(vul) veelvlak.addPoint((int)beginpunt.x,(int)beginpunt.y);
		beginpunt.x = eindpunt.x;
		beginpunt.y = eindpunt.y;
	}
	void tekenPolygon()
	{	gIm.setColor(vulkleur);
		gIm.fillPolygon(veelvlak);
		gIm.setColor(penkleur);
		if(pen)gIm.drawPolygon(veelvlak);
	}
	//-------------------------------------------------------------------------------------------
	//deze methoden worden gebruikt door de TraceBeheerder
	//-------------------------------------------------------------------------------------------
	void tekenTraceImage()
	{	Graphics g = getGraphics();
		g.drawImage(im, 0, 0, null);
	}
	void tekenCursor()
	{	Polygon cursor = new Polygon();
		Punt p;
		p = mat.geefVolgendPunt(beginpunt,10,0);
		cursor.addPoint((int)p.x,(int)p.y);
		p = mat.geefVolgendPunt(p,-10,-10);
		cursor.addPoint((int)p.x,(int)p.y);
		p = mat.geefVolgendPunt(p,-10,10);
		cursor.addPoint((int)p.x,(int)p.y);
		gIm.setColor(new Color(255,255,0));
		gIm.fillPolygon(cursor);
		gIm.setColor(new Color(0,0,255));
		gIm.drawPolygon(cursor);
	}
 	//-------------------------------------------------------------------------------------------
	//deze methoden worden gebruikt in "tekenprogramma()" 
	//-------------------------------------------------------------------------------------------
	void penAan()
	{	pen = true;
	}
	void penAan(String kl)
	{	pen = true;
		penkleur = maakKleur(kl);
		gIm.setColor(penkleur);
	}
	void penAan(Color kl)
	{	pen = true;
		penkleur = kl;
		gIm.setColor(penkleur);
	}
	void penAan(int r, int g, int b)
	{	pen = true;
		penkleur = new Color(r,g,b);
		gIm.setColor(penkleur);
	}
	void penUit()
	{	pen = false;
	}
	void vulAan()
	{	vul = true;
		veelvlak = new Polygon();
	}
	void vulAan(String kl)
	{	vul = true;	
		vulkleur = maakKleur(kl);
		veelvlak = new Polygon();
	}
	void vulAan(Color kl)
	{	vul = true;	
		vulkleur = kl;
		veelvlak = new Polygon();
	}
	void vulAan(int r, int g, int b)
	{	vul = true;	
		vulkleur = new Color(r,g,b);
		veelvlak = new Polygon();
	}
	void vulUit()
	{	tekenPolygon();
		vul = false;
	}
	void achtergrondkleur(String kl)
	{	achtergrondkleur = maakKleur(kl);
	}
	void achtergrondkleur(int r, int g, int b)
	{	achtergrondkleur = new Color(r,g,b);
	}
	Polygon geefVlak()							// geeft de laatst getekende Polygon
	{	return veelvlak;
	}
	Punt geefPunt()								// geeft de laatst getekende Punt
	{	return beginpunt;
	}
	void schrijf(String s)
	{	gIm.drawString(s, (int)beginpunt.x, (int)beginpunt.y);
	}
 	//-------------------------------------------------------------------------------------------
	//deze methode wordt gebruikt een kleur in de vorm van een string om te zetten in een Color
	//-------------------------------------------------------------------------------------------
	private Color maakKleur(String kl)
	{	if(kl.equals("rood")) return Color.red;
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
}


class AnimatieBeheerder extends Panel implements ActionListener, Runnable
{
  	private Button animatieknop;
  	private Thread animatie;
  	boolean animatieAan;
	TekenApplet eigenaar;
	
	public AnimatieBeheerder(TekenApplet ap)
	{	eigenaar = ap;
		animatieAan = false;
		animatieknop = new Button("animatie");
		animatieknop.addActionListener(this);
		add(animatieknop);
	}
	//-------------------------------------------------------------------------------------------
	//de TraceBeheerder maakt zich met deze methode kenbaar aan de AnimatieBeheerder 
	//-------------------------------------------------------------------------------------------
	
	//-------------------------------------------------------------------------------------------
	//afhandeling van de animatieknop actie, en het starten van de animatiedraad 
	//-------------------------------------------------------------------------------------------
  	public void actionPerformed(ActionEvent e)
	{	if(animatie==null)
		{	beginAnimatie();
		}
		else
		{	onderbreekAnimatie();
			
			animatieknop.setLabel("animatie");
		}
	}
	
	public void run()
	{	eigenaar.animatie();
		onderbreekAnimatie();
		
		animatieknop.setLabel("animatie");
	}
		
	//-------------------------------------------------------------------------------------------
	//deze methoden worden behalve door de Animatiebeheerder zelf, ook gebruikt door de 
	//TraceBeheerder en MuisBeheerder 
	//-------------------------------------------------------------------------------------------
	public void onderbreekAnimatie()
	{	animatieAan=false;
		pauze(200);
		animatie = null;
	}
		public void beginAnimatie()
	{	animatieAan=true;
		pauze(200);
		animatie = new Thread(this);
		animatie.start();
		
			animatieknop.setLabel("stoppen");
	}	
	void setEnableAnimatieKnop(boolean b)
	{	animatieknop.setEnabled(b);
	}
	//-------------------------------------------------------------------------------------------
	//deze methoden worden gebruikt in de animatie- en muishandlers van het leerlingenprogramma.
	//"animatieStatus()" wordt ook gebruikt door de TraceBeheerder en MuisBeheerder.
	//-------------------------------------------------------------------------------------------
	public boolean animatieStatus()
	{	return animatieAan;
	}
	public void pauze(int millisec)
	{  try
    	{   Thread.sleep(millisec);
		}
    	catch(InterruptedException e)    // geen ;
		{   }
	}
}

class MuisBeheerder implements MouseListener, MouseMotionListener
{
	private int eerstex, laatstex, eerstey, laatstey, dx, dy;
	private TekenApplet eigenaar;
	private AnimatieBeheerder ab;
	private boolean animatieWasAan;
	private boolean actief;
	
	public MuisBeheerder(TekenApplet ap)
	{	eigenaar = ap;
		actief = true;
		eerstex = 0;
		eerstey = 0;
		laatstex = 0;
		laatstey = 0;
		dx = 0;
		dy = 0;
	}
	//-------------------------------------------------------------------------------------------
	//de AnimatieBeheerder maakt zich met deze methode kenbaar aan de Muisbeheerder  
	//-------------------------------------------------------------------------------------------
	public void meldAnimatieBeheerder(AnimatieBeheerder ab)
	{	this.ab = ab;
	}
	//-------------------------------------------------------------------------------------------
	//afhandeling van de muis gebeurtenissen 
	//-------------------------------------------------------------------------------------------
	public void mousePressed(MouseEvent e)
	{	if(actief)
		{	if(ab!=null && ab.animatieStatus())
			{	animatieWasAan = true;
				ab.onderbreekAnimatie();
			}
			eerstex = e.getX();
			eerstey = e.getY();
			laatstex = e.getX();
			laatstey = e.getY();
			eigenaar.muisDrukActie();
		}
	}
	
	public void mouseDragged(MouseEvent e)
	{	if(actief)
		{	int x = e.getX();
			int y = e.getY();
			dx = x - laatstex;
			dy = laatstey -y;
			eigenaar.muisSleepActie();
			laatstex = x;
			laatstey = y;	
		}
	}
	public void mouseReleased(MouseEvent e)
	{	if(animatieWasAan)
		{	animatieWasAan = false;
			ab.beginAnimatie();
			
		}
		eigenaar.muisLosActie();
	}
	public void mouseExited(MouseEvent e){;}
	public void mouseClicked(MouseEvent e){;}
	public void mouseEntered(MouseEvent e){;}
	public void mouseMoved(MouseEvent e){;}
	
	//-------------------------------------------------------------------------------------------
	//deze methode wordt gebruikt door de TraceBeheerder
	//-------------------------------------------------------------------------------------------
	public void setEnableMuisActie(boolean b)
	{	actief = b;
	}
	//-------------------------------------------------------------------------------------------
	//deze methoden worden gebruikt door de muishandlers in het leerlingenprogramma
	//-------------------------------------------------------------------------------------------
	public int geefSleepdx()
	{	return dx;
	}
	public int geefSleepdy()
	{	return dy;
	}
	public int geefDrukx()
	{	return eerstex;
	}
	public int geefDruky()
	{	return eerstey;
	}
}	

class Matrix2D
{
	//-------------------------------------------------------------------------------------------
	//deze klasse onthoudt, en berekent steeds opnieuw de tekenrichting, en berekent voor het 
	//Tekenblad aan de hand van een dx en dy het volgende eindpunt van de tekenlijn.
	//-------------------------------------------------------------------------------------------
	
	private double  starthoek, hoek, radHoek, cosHoek, sinHoek, startschaal, schaal;
	private double a11,a12,a21,a22;
 	private static double pi = Math.PI;

	public Matrix2D()
	{	starthoek = 0;
		hoek = 0;
		startschaal = 1;
		schaal = 1;
		radHoek = 0; 
		cosHoek = 1; 
		sinHoek = 0;
		a11 = 1; a12 = 0; a21 = 0; a22 = 1;
	}
	
	public Matrix2D(double hk, double schl)
	{	initialiseer(hk, schl);
	}
	
	public void initialiseer(double hk,double schl)
	{	starthoek = hk;
		startschaal = schl;
		initialiseer();
	}
	
	public void initialiseer()
	{	hoek = starthoek;
		schaal = startschaal;
		draai(0);
	}
	
	private void maakMatrix()
	{	a11 = schaal*cosHoek;
		a12 = -schaal*sinHoek;
		a21 = schaal*sinHoek;
		a22 = schaal*cosHoek;
	}
	
	public void draai(double dHoek)
	{ 	hoek = hoek - dHoek;
		radHoek = hoek/180*pi;
		cosHoek = (double)Math.cos(radHoek);
		sinHoek = (double)Math.sin(radHoek);
		maakMatrix();
	}
	
	public void schaal(double s)
	{	schaal = startschaal*s;
		maakMatrix();
	}
	
	public Punt geefVolgendPunt(Punt beginp, double dx, double dy)
	{	Punt eindp = new Punt(beginp.x + a11*dx + a12*dy , beginp.y + a21*dx + a22*dy);
		return eindp;
	}	
}	

/*class TraceBeheerder extends Panel implements ActionListener,Runnable
{
	private Button stapKnop,terugKnop,loopKnop,beginKnop,traceKnop;
	private TextField methodeVeld;
	private int maxAantalStappen,aantalStappen,aantalStappenTekening;
	//private Regelaar rg;
	private Tekenblad tb;
	private AnimatieBeheerder ab;
	private MuisBeheerder mb;
	private boolean loopAan,traceAan;
	private Thread loop;
	
	public TraceBeheerder(Tekenblad tb, Regelaar rg)
	{	beginKnop = new Button("begin");
		beginKnop.addActionListener(this);
		add(beginKnop);
		stapKnop = new Button("stap");
		stapKnop.addActionListener(this);
		add(stapKnop);
		terugKnop = new Button("terug");
		terugKnop.addActionListener(this);
		add(terugKnop);
		methodeVeld = new TextField("",15);
		add(methodeVeld);
		loopKnop = new Button("loop");
		loopKnop.addActionListener(this);
		add(loopKnop);
		traceKnop = new Button("trace aanschakelen");
		traceKnop.addActionListener(this);
		add(traceKnop);
		
		aantalStappen = 0;
		maxAantalStappen = 0;
		aantalStappenTekening = 1;
		this.tb = tb;
		this.rg = rg;
		loopAan = false;
		traceAan = false;
	}
	void naarBegin()
	{	methodeVeld.setVisible(false);
		beginKnop.setVisible(false);
		stapKnop.setVisible(false);
		loopKnop.setVisible(false);
		terugKnop.setVisible(false);
	}
	//-------------------------------------------------------------------------------------------
	//de AnimatieBeheerder en Muisbeheerder maken zich met deze methoden bekend 
	//-------------------------------------------------------------------------------------------
	public void meldAnimatieBeheerder(AnimatieBeheerder ab)
	{	this.ab = ab;
	}
	public void meldMuisBeheerder(MuisBeheerder mb)
	{	this.mb = mb;
	}
	//-------------------------------------------------------------------------------------------
	// het Tekenblad vraagt hiermee op of de Tracefunctie aanstaat 
	//-------------------------------------------------------------------------------------------
	public boolean geefTraceStatus()
	{	return traceAan;
	}
	//-------------------------------------------------------------------------------------------
	//de AnimatieBeheerder kan de TraceKnop hiermee disabelen  
	//-------------------------------------------------------------------------------------------
	public void setEnableTraceKnop(boolean b)
	{	traceKnop.setEnabled(b);
	}
	//-------------------------------------------------------------------------------------------
	//het TekenApplet geeft bij het doorlopen van tekenprogramma() de namen van de uitgevoerde
	//stappen (tekenopdrachten) door aan TraceBeheerder.
	//Wanneer het aantal stappen gelijk is aan maxAantalStappen, dan wordt het tot dan toe 
	//voltooide deel van de tekening op het image via de methode tekenTraceImage()op Tekenblad 
	//gezet . De variabele maxAantalStappen wordt met de stapKnop (of met de loopKnop
	//in een Thread) steeds met een verhoogd, waardoor de tekening stap voor stap wordt opgebouwd.
	//met de terugKnop wordt maxAantalStappen telkens een verlaagd, waardoor de tekening stap voor
	//stap terugloopt
	//-------------------------------------------------------------------------------------------
	public void volgendeMethode(String naam)
	{	aantalStappen++;
		if(aantalStappen == maxAantalStappen && traceAan)
		{	tb.tekenCursor();
			tb.tekenTraceImage();
			if(!loopAan)methodeVeld.setText(naam);
			else methodeVeld.setText("");
		}
		aantalStappenTekening = aantalStappen;
		if(!traceAan)naarBegin();
	}
	//-------------------------------------------------------------------------------------------
	//afhandeling van de knopacties, en het starten van de loopdraad 
	//-------------------------------------------------------------------------------------------
	public void actionPerformed(ActionEvent e)
	{	if(e.getSource() == stapKnop)
		{	loopAan=false;
			maxAantalStappen++;
			aantalStappen = 0;
			tb.tekenOpnieuw();
		}
		if(e.getSource() == terugKnop)
		{	loopAan=false;
			maxAantalStappen--;
			if(maxAantalStappen<0)maxAantalStappen=0;
			aantalStappen = 0;
			tb.tekenOpnieuw();
		}
		if(e.getSource() == loopKnop)
		{	if(loop==null)
			{	loopAan=true;
				loop = new Thread(this);
				loop.start();
				loopKnop.setLabel("stop");
			}
		
			else
			{	loopAan=false;
				loop = null;
				loopKnop.setLabel("loop");
			}
		}
		if(e.getSource() == beginKnop)
		{	loopAan=false;
			maxAantalStappen = 1;
			aantalStappen = 0;
			tb.tekenOpnieuw();
		}
		if(e.getSource() == traceKnop)
		{	if(!traceAan)
			{	traceAan = true;
				//rg.setEnableInvVars(false);
				if(ab!=null)ab.setEnableAnimatieKnop(false);
				if(mb!=null)mb.setEnableMuisActie(false);
				traceKnop.setLabel("trace uitschakelen");
				methodeVeld.setVisible(true);
				beginKnop.setVisible(true);
				stapKnop.setVisible(true);
				terugKnop.setVisible(true);
				loopKnop.setVisible(true);
				
			}
			else
			{	traceAan = false;
				loopAan = false;
				//rg.setEnableInvVars(true);
				if(ab!=null)ab.setEnableAnimatieKnop(true);
				if(mb!=null)mb.setEnableMuisActie(true);
				tb.tekenOpnieuw();
				methodeVeld.setVisible(false);
				beginKnop.setVisible(false);
				stapKnop.setVisible(false);
				loopKnop.setVisible(false);
				terugKnop.setVisible(false);
				traceKnop.setLabel("trace aanschakelen");
			}
				
			maxAantalStappen = 1;
			aantalStappen = 0;
			tb.tekenOpnieuw();
		}
	}
	public void run()
	{	while(loopAan && aantalStappenTekening>maxAantalStappen)
		{	maxAantalStappen++;
			aantalStappen = 0;
			tb.tekenOpnieuw();
			try	
			{   loop.sleep(100);
			}
    		catch(InterruptedException e) {   }
		}
		loopAan=false;
		loop = null;
		loopKnop.setLabel("loop");
		if(aantalStappenTekening<maxAantalStappen)maxAantalStappen = 0; 
	}
}*/