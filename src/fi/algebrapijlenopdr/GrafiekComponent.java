package fi.algebrapijlenopdr;

import java.awt.*;
import java.awt.event.*;
import fi.algebrapijlenopdr.expressies_ap.*;


import java.text.*;
import java.util.Hashtable;


public class GrafiekComponent extends AlgebraSchuifComponent implements ActionListener, MouseListener, MouseMotionListener
{		
	private Image im;
  	private Graphics gIm;
  	
  	private int eenheid = 16;
		
	private PlusMinKnop pmKnopY,pmKnopX; 
	private ZoomKnop zoomInX, zoomUitX, zoomInY, zoomUitY, zoomIn, zoomUit, zoomStandaard;
	private Expressie[] expressies;
	private int aantalExpressies;
	private int maxAantalExpressies;
	private boolean gevuld;
	private int beginwaarde;
	private int selectnummer;
	private String varNaam;
	private String formuleNaam;
	private int xmin, xmax, ymin, ymax;
	private double beginx, beginy;
	private int veldx, veldy, veldb, veldh;
	private int eenheidx, eenheidy;
	private double eenheidxD, eenheidyD;
	private double schaalFactorY;
	private int factorRijNummerY;
	private double schaalFactorX;
	private int factorRijNummerX;
	private ZoomDraad zoomDraad;
	 
	int startxv = 0;
	int startyv = 0;
	 
	private GrafiekVeld gv;
	 
	private DecimalFormatSymbols dfs;
	private DecimalFormat df, dfTrace;
	private FontMetrics fm;
	
	Pijl[] pijlenIn;
	int aantalPijlenIn;
	
	private boolean[] isPuntGrafiek;
	private boolean[] isMeerPuntenGrafiek;
	private boolean[] isLijnGrafiek;
	private double[] puntXWaarde;
	
	private Font font = new Font("SansSerif", Font.PLAIN, 10);
	
	private boolean resize;
	private boolean trace=true;
	private boolean tracing=false;
	
	private Slider slider;
	private int tracex=-2;
	private double tracexD = tracex;
	
	public GrafiekComponent(AlgebraSchuifVeld sv,int x, int y, int b, int h)
	{	super(1,sv,x,y,b,h);
		super.setBounds(x,y,b,h);
		setLayout(null);
		setBackground(Color.white);
		
		maxAantalExpressies = 10;
		links = false;
		isStapel = false;
		expressies = new Expressie[maxAantalExpressies];
		aantalExpressies = 0;
		
		pijlenIn = new Pijl[maxAantalExpressies];
		aantalPijlenIn = 0;
		
		isPuntGrafiek = new boolean[10];
		isMeerPuntenGrafiek = new boolean[10];
		puntXWaarde = new double[10];
		isLijnGrafiek = new boolean[10];
		
		
		beginwaarde = 0;
		selectnummer = 999;
		xmin = 0; 
		ymin = 0;
		xmax = 10;
		ymax = 10;
		eenheidx = eenheid;
		eenheidy = eenheid;
		eenheidxD = eenheid;
		eenheidyD = eenheid;
		beginx = eenheidx;
		beginy = eenheidy;
		veldx = 40;
		veldy = 45;
		veldb = b-60;
		veldh = h-75;
		schaalFactorY = 1;
		factorRijNummerY = 99;
		schaalFactorX = 1;
		factorRijNummerX = 99;
		varNaam = "";
		formuleNaam = "";
		
		dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		df = new DecimalFormat("0.####", dfs);
		dfTrace = new DecimalFormat("0.##",dfs);
		
		gv = new GrafiekVeld(veldx,veldy,veldb,veldh);
		gv.addMouseListener(this);
		gv.addMouseMotionListener(this);
		
		add(gv);
		
		zoomStandaard	= new ZoomKnop("standaard");
		zoomStandaard.setBounds(32,2,25,25);
		zoomStandaard.addActionListener(this);
		add(zoomStandaard);
		
		zoomIn	= new ZoomKnop("zoomin");
		zoomIn.setBounds(57,2,25,25);
		zoomIn.addActionListener(this);
		add(zoomIn);
		
		zoomUit	= new ZoomKnop("zoomuit");
		zoomUit.setBounds(82,2,25,25);
		zoomUit.addActionListener(this);
		add(zoomUit);	
		
		zoomInX	= new ZoomKnop("zoominx");
		zoomInX.setBounds(107,2,25,25);
		zoomInX.addActionListener(this);
		add(zoomInX);
		
		zoomUitX= new ZoomKnop("zoomuitx");
		zoomUitX.setBounds(132,2,25,25);
		zoomUitX.addActionListener(this);
		add(zoomUitX);
		
		zoomInY	= new ZoomKnop("zoominy");
		zoomInY.setBounds(157,2,25,25);
		zoomInY.addActionListener(this);
		add(zoomInY);
		
		zoomUitY= new ZoomKnop("zoomuity");
		zoomUitY.setBounds(182,2,25,25);
		zoomUitY.addActionListener(this);
		add(zoomUitY);
		
		slider = new Slider(veldb,0);
		slider.setLocation(veldx-5,h-13);
		slider.addActionListener(this);
		slider.setBackground(new Color(210,210,210));
		add(slider);
			
	}
	
	public void setSize(int b, int h)
	{	
		super.setSize(b,h);
		veldb = b-60;
		veldh = h-75;
		gv.setSize(veldb,veldh);
		slider.zetLengte(veldb);
		slider.setLocation(veldx-5,h-13);
	}
	
	public Hashtable getState()
	{	double beginx  = 0;
		double beginy  = 0;
		double schaalFactorX  = 0;
		double schaalFactorY  = 0;
				
		beginx = this.beginx;
		beginy = this.beginy;
		schaalFactorX = this.schaalFactorX;
		schaalFactorY = this.schaalFactorY;
				
		Hashtable h = super.getState();
	    h.put("beginx", new Double(beginx));
	    h.put("beginy", new Double(beginy));
	    h.put("schaalFactorX", new Double(schaalFactorX));
	    h.put("schaalFactorY", new Double(schaalFactorY));
	    return h;
	}

    public void setState(Hashtable h)
    {	double beginx = ((Double)h.get("beginx")).doubleValue();
    	double beginy = ((Double)h.get("beginy")).doubleValue();
    	double schaalFactorX = ((Double)h.get("schaalFactorX")).doubleValue();
    	double schaalFactorY = ((Double)h.get("schaalFactorY")).doubleValue();
    				
		this.beginx = beginx;
		this.beginy = beginy;
		this.schaalFactorX = schaalFactorX;
		this.schaalFactorY = schaalFactorY;
		
		int b = beginwaarde;
		beginwaarde = 1-(int)Math.round(beginx/eenheidx);
		selectnummer = selectnummer + b - beginwaarde;
		((AlgebraSchuifVeld)getParent()).zetTabellen(beginwaarde,selectnummer, varNaam, schaalFactorX);
		
		super.setState(h);
    }
    /*
    public void paint(Graphics g)
	{	
		int breedte = getSize().width;
		int hoogte = getSize().height;
		g.setColor(new Color(220,220,220));
		g.fillRect(10,0,breedte-11,hoogte - 1);
		g.setColor(Color.black);
		g.drawRect(10,0,breedte-11,hoogte - 1);
		g.setColor(Color.white);
		g.fillRect(veldx-1,veldy-1,veldb+1,veldh+1);
		g.setColor(Color.black);
		g.drawRect(veldx-1,veldy-1,veldb+1,veldh+1);
		
		FontMetrics fm = g.getFontMetrics();
		int woordbreedte = fm.stringWidth(varNaam);
		g.drawString(varNaam, breedte-13-woordbreedte,hoogte-3);
		g.drawString(formuleNaam,20,20);
		
		if(aantalPijlenIn>0 && expressies[0]!=null && expressies[0].geefVarNaam()!=null && !expressies[0].geefVarNaam().equals("") && selectnummer>-1 && selectnummer<11)
		{	g.setColor(new Color(255,200,200));
			g.fillRect(veldx+(selectnummer+1)*eenheidx-eenheidx/2,veldy+veldh,eenheidy,eenheidy);
			g.setColor(Color.black);
			g.drawRect(veldx+(selectnummer+1)*eenheidx-eenheidx/2,veldy+veldh,eenheidy,eenheidy);
		}
		
		int imin = -(int)Math.round(beginx/eenheidx); 
		int imax = 1+veldb/eenheidx-(int)Math.round(beginx/eenheidx);
		int jmin = -(int)Math.round(beginy/eenheidx); 
		int jmax = 1+veldh/eenheidy-(int)Math.round(beginy/eenheidy);
		int bx = (int)beginx;
		int by = (int)beginy;
		
		new Expressie();
		for(int i=1 ; i<1+veldb/eenheidx ; i++)
		{	
			String getal = Expressie.df.format(schaalFactorX*(imin+i));
			woordbreedte = fm.stringWidth(getal);
			if(schaalFactorX>0.5 && schaalFactorX<5)g.drawString(getal,veldx+i*eenheidx-woordbreedte/2,veldy+veldh+13);
			else if((i-beginwaarde-1)%2==0)g.drawString(getal,veldx+i*eenheidx-woordbreedte/2,veldy+veldh+13);
		}
		for(int j=1 ; j<1+veldh/eenheidy ; j++)
		{	String getal = Expressie.df.format(schaalFactorY*(jmin+j));
			woordbreedte = fm.stringWidth(getal);
			g.drawString(getal,veldx-3-woordbreedte,veldy+veldh+5-(j*eenheidy));
		}
		super.paint(g);	
	}*/	
     
	public void paint(Graphics g)
	{	g.setFont(font);
		
		int breedte = getSize().width;
		int hoogte = getSize().height;
		g.setColor(new Color(210,210,210));//getBackground());
		g.fillRect(10,0,breedte-11,hoogte - 1);
		g.setColor(Color.black);
		g.drawRect(10,0,breedte-11,hoogte - 1);
		
		/*g.setColor(Color.white);
		g.drawLine(11,1,breedte-1,1);
		g.drawLine(11,1,11,hoogte-1);
		g.setColor(Color.gray.darker());
		g.drawLine(11,hoogte-2,breedte-2,hoogte-2);
		g.drawLine(10,hoogte-1,breedte-1,hoogte-1);
		g.drawLine(breedte-1,0,breedte-1,hoogte-1);
		g.drawLine(breedte-2,1,breedte-2,hoogte-2);
		
		g.setColor(Color.white);
		g.drawLine(15,30,breedte-16,30);
		g.setColor(Color.gray.darker());
		g.drawLine(15,29,breedte-16,29);*/
		
		g.setColor(Color.white);
		g.fillRect(veldx-1,veldy-1,veldb+1,veldh+1);
		//g.setColor(Color.black);
		//g.drawRect(veldx-1,veldy-1,veldb+1,veldh+1);
		g.setColor(Color.gray.darker());
		g.drawLine(veldx-1,veldy-1,veldx+veldb,veldy-1);
		g.drawLine(veldx-1,veldy-1,veldx-1,veldy+veldh);
		g.drawLine(veldx-1,veldy-2,veldx+veldb,veldy-2);
		g.drawLine(veldx-2,veldy-1,veldx-2,veldy+veldh);
		g.setColor(new Color(180,180,180));
		g.drawLine(veldx+veldb,veldy-1,veldx+veldb,veldy+veldh);
		g.drawLine(veldx-1,veldy+veldh,veldx+veldb,veldy+veldh);
		g.setColor(Color.white);//new Color(230,230,230));
		g.drawLine(veldx+veldb+1,veldy-1,veldx+veldb+1,veldy+veldh);
		g.drawLine(veldx-1,veldy+veldh+1,veldx+veldb,veldy+veldh+1);
		//g.drawLine(veldx+veldb+2,veldy-1,veldx+veldb+2,veldy+veldh+1);
		//g.drawLine(veldx-1,veldy+veldh+2,veldx+veldb+1,veldy+veldh+2);
		
		g.setColor(Color.black);
		FontMetrics fm = g.getFontMetrics();
		int woordbreedte = fm.stringWidth(varNaam);
		g.drawString(varNaam, veldx+veldb+5,veldy+veldh+5);
		g.drawString(formuleNaam,veldx,veldy-8);
		
		//if(exp!=null && exp.geefVarNaam()!=null && !exp.geefVarNaam().equals("") && selectnummer>-1 && selectnummer<11)
		//{	g.setColor(new Color(255,200,200));
		//	g.fillRect(veldx+(selectnummer+1)*eenheidx-eenheidx/2,veldy+veldh,eenheidx,eenheidy);
		//	g.setColor(Color.black);
		//	g.drawRect(veldx+(selectnummer+1)*eenheidx-eenheidx/2,veldy+veldh,eenheidx,eenheidy);
		//}
		
		int imin = -(int)Math.round(beginx/eenheidx); 
		int imax = 1+veldb/eenheidx-(int)Math.round(beginx/eenheidx);
		int jmin = -(int)Math.round(beginy/eenheidy); 
		int jmax = 1+veldh/eenheidy-(int)Math.round(beginy/eenheidy);
		int bx = (int)beginx;
		int by = (int)beginy;
		
		for(int i=imin+1 ; i<imax ; i++)
		{	new Expressie();
			String getal = df.format(schaalFactorX*(i));
			woordbreedte = fm.stringWidth(getal);
			if(schaalFactorX>0.5 && schaalFactorX<5 && woordbreedte<eenheidx)g.drawString(getal,(int)(veldx+beginx+i*eenheidxD-woordbreedte/2),veldy+veldh+15);
			else if((i+imin -beginwaarde-1)%2==0)g.drawString(getal,(int)(veldx+beginx+i*eenheidxD-woordbreedte/2),veldy+veldh+15);
		}
		for(int j=jmin+1 ; j<jmax ; j++)
		{	String getal = df.format(schaalFactorY*(j));
			woordbreedte = fm.stringWidth(getal);
			g.drawString(getal,veldx-5-woordbreedte,(int)(veldy+veldh+5-(beginy+j*eenheidyD)));
		}
		super.paint(g);	
	}	
		
	public void zetBegin(int x, int y)
	{	beginx = eenheidx*x;
		beginy = eenheidy*y;
	}

	public void zetExpressie(int nr,Expressie e)
	{	Expressie exp = null;
		if(e!=null && e.geefVarNaam()!=null )
		{	exp = e;
			if(varNaam.equals("")|| aantalPijlenIn==1)varNaam = e.geefVarNaam();
			isPuntGrafiek[nr] = false;
		}
		else if(e!=null && e.geefWaarde()!=null && pijlenIn[nr]!=null)
		{	
			AlgebraSchuifComponent asc = pijlenIn[nr].zender;
			int teller = 20;
			puntXWaarde[nr] = asc.geefUitvoer(teller).geefWaarde().doubleValue();
			while(asc.pijlIn1 !=null && teller > 0)
			{	teller--;
				asc = asc.pijlIn1.zender;
				if(asc.geefUitvoer(teller).geefWaarde()!=null)puntXWaarde[nr] = asc.geefUitvoer(teller).geefWaarde().doubleValue();
				isPuntGrafiek[nr] = true;
			}
			exp = e;
		}
		else 
		{	exp = null;
			isPuntGrafiek[nr] = false;
		}
		expressies[nr] = exp;
		gv.tekenOpnieuw();
	}
	
	/*public void zetVoorbeeld(Expressie e)
	{	if(e!=null)// && e.geefWaarde()==null )
		{	expVoorbeeld = e;
			varNaam = e.geefVarNaam();
		}
		else 
		{	expVoorbeeld = null;
			varNaam = "x";
			formuleNaam = "f(x)";
		}
		gv.tekenOpnieuw();
		repaint();
	}*/
	
	public void zetTabel(int beginwaarde, int selectnummer, String varN, double schaalFactorX)
	{	if(varNaam.equals(varN))
		{	this.beginwaarde = beginwaarde;
			beginx = eenheidx-eenheidx*beginwaarde;
			this.selectnummer = selectnummer;
			this.schaalFactorX = schaalFactorX;
			if(selectnummer!=999)tracing = false;
			else
			{	//tracexD = beginx+1.0*((selectnummer+beginwaarde)*eenheidx);
				//tracex = (int)Math.round(tracexD);
				//slider.zetStand(tracex);
			}
			gv.tekenOpnieuw();
		}
	}
	
	public void zetVeranderd(int max)
	{	
		for(int i=0 ; i<aantalPijlenIn ; i++)
		{	Expressie e = pijlenIn[i].zender.geefUitvoer(20);
			Expressie ev = pijlenIn[i].zender.geefVerborgenUitvoer(20);
			zetExpressie(i,e);
			formuleNaam = ((UitvoerSchuifComponent)pijlenIn[i].zender).geefLabelTekst();
			if((e==null || e.geefWaarde()!=null) && !(ev instanceof BasisExpressie) && ((UitvoerSchuifComponent)pijlenIn[i].zender).tabelZichtbaar)
			{	zetExpressie(i,ev);
				isMeerPuntenGrafiek[i] = true;
				isLijnGrafiek[i] = false;
				if(e!=null && e.geefWaarde()!=null) 
				{	AlgebraSchuifComponent asc = pijlenIn[i].zender;
					int teller = 20;
					puntXWaarde[i] = asc.geefUitvoer(teller).geefWaarde().doubleValue();
					while(asc.pijlIn1 !=null && teller > 0)
					{	teller--;
						asc = asc.pijlIn1.zender;
						puntXWaarde[i] = asc.geefUitvoer(teller).geefWaarde().doubleValue();
						isPuntGrafiek[i] = true;
					}
				}
			}
			else if(((UitvoerSchuifComponent)pijlenIn[i].zender).tabelZichtbaar)
			{	isMeerPuntenGrafiek[i] = true;
				isLijnGrafiek[i] = true;
			}
			else 
			{	isMeerPuntenGrafiek[i] = false;
				isLijnGrafiek[i] = true;
			}
			
		}
		super.zetVeranderd(max);
	}
	
	public void verbind(Pijl p, int nr)
	{	pijlenIn[nr] = p;
		p.zetEind(getLocation().x , getLocation().y+10+nr*15);
		aantalPijlenIn++;
	}

	public boolean meldAan(Pijl p, int x, int y)
	{	if(!(p.zender instanceof UitvoerSchuifComponent))return false;
	
		AlgebraSchuifComponent asc = p.zender;
		int teller = 20;
		Expressie e = asc.geefUitvoer(teller);
		while(asc.pijlIn1 !=null && teller > 0)
		{	teller--;
			asc = asc.pijlIn1.zender;
			e = asc.geefUitvoer(teller);
		}
		if(e!=null && e.geefVarNaam()!=null && varNaam!="" && !e.geefVarNaam().equals(varNaam)) return false;
					
		//if(p.zender.geefUitvoer(20)!=null && !varNaam.equals("") && !varNaam.equals(p.zender.geefUitvoer(20).geefVarNaam()))return false;
		Rectangle ingang = new Rectangle(-10,0,getSize().width+10, getSize().height);
		if(aantalPijlenIn <10 && ingang.contains(x-getLocation().x,y-getLocation().y))
		{	p.zetEind(getLocation().x , getLocation().y+10+aantalPijlenIn*15);
			pijlenIn[aantalPijlenIn] = p;
			aantalPijlenIn++;
			if(e!=null && e.geefVarNaam() != null) varNaam = e.geefVarNaam();
			
			zetVeranderd(20);
			schuifveld.tekenOpnieuw();
			return true;
		}
		return false;
	}
	
	public void maakLos(Pijl p)
	{	for(int i=0 ; i<aantalPijlenIn ; i++)
		{	if(p==pijlenIn[i])
			{	for(int j=i ; j<aantalPijlenIn-1 ; j++)
				{	pijlenIn[j] = pijlenIn[j+1];
					pijlenIn[j].zetEind(getLocation().x , getLocation().y+10+j*15);
					expressies[j] = expressies[j+1];
				}
				pijlenIn[aantalPijlenIn-1]=null;
				aantalPijlenIn--;
				break;
			}
		}
		//((UitvoerSchuifComponent)p.zender).zetGrafiek(false,this);
		selectnummer = 999;
		if(aantalPijlenIn==0)
		{	((AlgebraSchuifVeld)getParent()).zetTabellen(0,selectnummer, varNaam, 1);
			zetTabel(0,selectnummer, varNaam, 1);
			varNaam = "";
			formuleNaam = "";
			beginy = eenheidy;
		}
	}
	
	public void mousePressed(MouseEvent e)
	{	requestFocus();
		if(e.getSource()==gv)
		{	startxv = e.getX();
			startyv = e.getY();
		}
		else if(e.getX()>getSize().width-10 && e.getY()>getSize().height-10)
		{	resize = true;
			startx = e.getX();
			starty = e.getY();
		}
		//else if(e.getX()>40 && e.getX()<getSize().width-10 && e.getY()>getSize().height-10)
		//{	trace = true;
		//	startx = e.getX();
		//	starty = e.getY();
		//}
		else super.mousePressed(e);
	}	
	
	public void mouseDragged(MouseEvent e)
	{	if(e.getSource()==gv)
		{	int dx = e.getX() - startxv;
			int dy =  e.getY() - startyv;
		
			beginx = beginx+dx;
			beginy = beginy-dy;
			if(trace && tracex!=-2) {
				tracexD = tracexD+dx;
				tracex = tracex+dx;
				slider.zetStand(tracex);
			}
			
			int b = beginwaarde;
			beginwaarde = 1-(int)Math.round(beginx/eenheidx);
			selectnummer = selectnummer + b - beginwaarde;
			gv.tekenOpnieuw();
			schuifveld.tekenOpnieuw();
			startxv = e.getX();
			startyv = e.getY();
			
		}
		else if(resize)
		{	int dx = e.getX() - startx;
			int dy = e.getY() - starty;
			setSize(getSize().width + dx, getSize().height + dy);
			schuifveld.tekenOpnieuw();
			startx = e.getX();
			starty = e.getY();
			
		}
		else
		{	super.mouseDragged(e);
			int dx = e.getX() - startx;
			int dy =  e.getY() - starty;
			for(int i=0 ; i<aantalPijlenIn ; i++)
			{	pijlenIn[i].verplaatsEind(dx,dy);
			}
		}
	}
	
	public void mouseReleased(MouseEvent e)
	{	resize = false;
		//trace = false;
		if(e.getSource()==gv)
		{	double beginxR = beginx;
			beginx = eenheidx*Math.round(beginx/eenheidx);
			beginy = eenheidy*Math.round(beginy/eenheidy);
			if(trace && tracex!=-2) {
				tracexD += beginx-beginxR;
				tracex += beginx-beginxR;
				slider.zetStand(tracex);
			}
			if(aantalPijlenIn>0)
			{	((AlgebraSchuifVeld)getParent()).zetTabellen(beginwaarde,selectnummer, varNaam, schaalFactorX);
				
			}
			gv.tekenOpnieuw();
			schuifveld.tekenOpnieuw();
		}
		else super.mouseReleased(e);
	}
	
	public void mouseMoved(MouseEvent e){;}
	public void mouseExited(MouseEvent e){;}
	public void mouseClicked(MouseEvent e){;}
	
	public void actionPerformed(ActionEvent e)
	{	if(zoomDraad!=null && zoomDraad.isAlive())return;
		//if(zoomDraad!=null)
		//{	zoomDraad.maakDood();
		//	zoomDraad=null;
		//}
		if(e.getActionCommand().equals("focus")) schuifveld.tekenOpnieuw();
		else 
		{	if(e.getSource()==zoomUitY && factorRijNummerY<120)
			{	
				zoomDraad = new ZoomDraad(false,true,false);
				zoomDraad.start();
			}
			else if(e.getSource()==zoomInY  && factorRijNummerY>87)
			{	zoomDraad = new ZoomDraad(false,true,true);
				zoomDraad.start();
			}
			else if(e.getSource()==zoomUitX && factorRijNummerX<120)
			{	zoomDraad = new ZoomDraad(true,false,false);
				zoomDraad.start();
			}
			else if(e.getSource()==zoomInX  && factorRijNummerX>87)
			{	zoomDraad = new ZoomDraad(true,false,true);
				zoomDraad.start();
			}
			else if(e.getSource()==zoomUit && factorRijNummerX<120 && factorRijNummerY<120)
			{	zoomDraad = new ZoomDraad(true,true,false);
				zoomDraad.start();
			}
			else if(e.getSource()==zoomIn && factorRijNummerX>87 && factorRijNummerY>87)
			{	zoomDraad = new ZoomDraad(true,true,true);
				zoomDraad.start();
			}
			else if(e.getSource()==zoomStandaard)
			{	//beginx = veldb/2/eenheidx*eenheidx;
				//beginy = veldh/2/eenheidy*eenheidy;
				double beginxVorig = beginx;
				beginx = eenheidx;
				beginy = eenheidy;
				tracexD = beginx -(beginxVorig - tracexD)*schaalFactorX;
				factorRijNummerX = 99;
				factorRijNummerY = 99;
				schaalFactorX = 1;
				schaalFactorY = 1;
				beginwaarde = 0;
				selectnummer = 999;
				if(aantalPijlenIn>0)
				{	((AlgebraSchuifVeld)getParent()).zetTabellen(beginwaarde,selectnummer, varNaam, schaalFactorX);
				}
				
				tracex = (int) Math.round(tracexD);
				//tracex=-2;
				slider.zetStand(tracex);
				
				gv.tekenOpnieuw();
				schuifveld.tekenOpnieuw();
				
				
			}
			
		}
		if(e.getSource()==slider)
		{ 	if(e.getActionCommand().equals("start")) 
			{	tracing = true;
				((AlgebraSchuifVeld)getParent()).zetTabellen(beginwaarde, 999, varNaam, schaalFactorX);
			}
			//else if(e.getActionCommand().equals("stop")) tracing = false;
			tracex = slider.geefStand();
			tracexD = tracex;
			gv.tekenOpnieuw();
			schuifveld.tekenOpnieuw();
			
		}
		//repaint();
		//gv.tekenOpnieuw();
	}
	
	public void mouseEntered(MouseEvent e){;}	
	
	class ZoomDraad extends Thread 
	{	boolean dood = false;
		boolean x,y,in;
		
		ZoomDraad(boolean x, boolean y, boolean in)
		{	this.x = x;
			this.y = y;
			this.in = in;
		}
		
		public void run()
		{	selectnummer = 999;
            eenheidxD = eenheid;
			eenheidyD = eenheid;
			eenheidx = eenheid;
			eenheidy = eenheid;
			double stapx, stapy;
			double factorx = 1;
			double factory = 1;
			//int middenx = veldb/2/eenheidx*eenheidx;
			//int middeny = veldh/2/eenheidy*eenheidy;
			
			double middenx = eenheidx;
			double middeny = eenheidy;
			
			double beginxOud = beginx;
			
			if(in && x)
			{	if(factorRijNummerX%3==2)
				{	factorx=0.4;
				}
				else if(factorRijNummerX%3==0)
				{	factorx=0.5;
				}
				else 
				{	factorx=0.5;
				}
				
			}
			
			else if(!in && x)
			{	if(factorRijNummerX%3==1)
				{	factorx=2.5;
				}
				else if(factorRijNummerX%3==2)
				{	factorx=2;
				}
				else 
				{	factorx=2;
				}
			}
			
			if(in && y)
			{	if(factorRijNummerY%3==2)
				{	factory =0.4;
				}
				else if(factorRijNummerY%3==0)
				{	factory=0.5;
				}
				else 
				{	factory=0.5;
				}
			}
			
			else if(!in && y)
			{	if(factorRijNummerY%3==1)
				{	factory =2.5;
				}
				else if(factorRijNummerY%3==2)
				{	factory=2;
				}
				else 
				{	factory=2;
				}
			}
			
			//if(factorx!=1)
			//{	tracex=-2;
			//	slider.zetStand(tracex);
			//}
			stapx= Math.pow(factorx,0.1);
			stapy= Math.pow(factory,0.1);
			
			
			for(int i=0 ; i<5 ; i++)
			{	int delay = 20;
				long t = System.currentTimeMillis();
				try
				{	t = t+delay;
					sleep(Math.max(1, t-System.currentTimeMillis()));
				}
    			catch(InterruptedException e)    // geen ;
				{   };
				eenheidxD = eenheidxD/stapx;
				eenheidyD = eenheidyD/stapy;
				eenheidx = (int) Math.round(eenheidxD);
				eenheidy = (int) Math.round(eenheidyD);
				double beginxVorig = beginx;
				beginx =  middenx -(middenx - beginx)/stapx;
				beginy =  middeny -(middeny - beginy)/stapy;
				
				tracexD = middenx -(middenx - tracexD)/stapx;
				
				beginwaarde = 1-(int)Math.round(beginx/eenheidx);
				//double beginwaardeD = 1.0-(beginx/eenheidx);
				//tracexD = tracexD + eenheid*(beginwaardeD - beginwaarde);
				tracex = (int) Math.round(tracexD);
				slider.zetStand(tracex);
				
				gv.tekenOpnieuw();
				schuifveld.tekenOpnieuw();
				
			}
			
			
			
			schaalFactorX*=factorx;
			if(in && x)factorRijNummerX--;
			else if(!in && x)factorRijNummerX++;
			schaalFactorY*=factory;
			if(in && y)factorRijNummerY--;
			if(!in && y)factorRijNummerY++;
			
			eenheidxD = eenheidxD*factorx;
			eenheidyD = eenheidyD*factory;
			
			
			for(int i=0 ; i<5 ; i++)
			{	int delay = 20;
				long t = System.currentTimeMillis();
				try
				{	t = t+delay;
					sleep(Math.max(1, t-System.currentTimeMillis()));
				}
    			catch(InterruptedException e)    // geen ;
				{   };
				eenheidxD = eenheidxD/stapx;
				eenheidyD = eenheidyD/stapy;
				eenheidx = (int) Math.round(eenheidxD);
				eenheidy = (int) Math.round(eenheidyD);
				double beginxVorig = beginx;
				beginx =  middenx -(middenx - beginx)/stapx;
				beginy =  middeny -(middeny - beginy)/stapy;
				
				tracexD = middenx -(middenx - tracexD)/stapx;
				
				beginwaarde = 1-(int)Math.round(beginx/eenheidx);
				//double beginwaardeD = 1.0-(beginx/eenheidx);
				//tracexD = tracexD + eenheid*(beginwaardeD - beginwaarde);
				tracex = (int) Math.round(tracexD);
				slider.zetStand(tracex);
				gv.tekenOpnieuw();
				schuifveld.tekenOpnieuw();
			}
			
			
			
			beginwaarde = 1-(int)Math.round(beginx/eenheidx);
			double beginwaardeD = 1.0-(beginx/eenheidx);
			System.out.println(""+beginx);
			System.out.println(""+eenheidx);
			System.out.println(""+beginwaardeD);
			
			tracexD = tracexD + eenheid*(beginwaardeD - beginwaarde);
			tracex = (int) Math.round(tracexD);
			slider.zetStand(tracex);
			
			selectnummer = 999;
			if(aantalPijlenIn>0)
			{	((AlgebraSchuifVeld)getParent()).zetTabellen(beginwaarde,selectnummer, varNaam, schaalFactorX);
			}
			
			gv.tekenOpnieuw();
			schuifveld.tekenOpnieuw();
			
		}
		public void maakDood()
		{	dood = true;
		}
	}
	
	
	class GrafiekVeld extends Component
	{
		private Image im;
  		private Graphics gIm;
		private boolean veranderd;
	
		public GrafiekVeld(int x, int y, int b, int h)
		{	
			super.setBounds(x,y,b,h);
			veranderd = true;
		}
		
		public void paint(Graphics g)
		{	int breedte = getSize().width;
			int hoogte = getSize().height;
			if(veranderd)			
			{	if (im == null || resize)
				{	im = createImage(breedte, hoogte);
					gIm = im.getGraphics();
				}
				gIm.setColor(Color.white);
				gIm.fillRect(0,0,breedte,hoogte);
				tekenFunctie(gIm);
				veranderd = false;
			}
			g.drawImage(im, 0, 0, null);
		}
		
		public void setSize(int b, int h)
		{	super.setSize(b,h);
			tekenOpnieuw();
		}
		
		public void tekenOpnieuw()
		{	veranderd = true;
			repaint();
		}
		
		public void	tekenFunctie(Graphics g)
		{	int breedte = getSize().width;
			int hoogte = getSize().height;
			g.setClip(0,0,breedte,hoogte);
			int imin = -(int)Math.round(beginx/eenheidx); 
			int imax = 1+breedte/eenheidx-(int)Math.round(beginx/eenheidx);
			int bx = (int)Math.round(beginx);
			for(int i=imin ; i<imax ; i++)
			{	g.setColor(Color.lightGray);
				g.drawLine((int)(bx+i*eenheidxD),0,(int)(bx+i*eenheidxD),hoogte);
			}
			int jmin = -(int)Math.round(beginy/eenheidy); 
			int jmax = 1+hoogte/eenheidy-(int)Math.round(beginy/eenheidy);
			int by = (int)Math.round(beginy);
			for(int j=jmin ; j<jmax ; j++)
			{	g.setColor(Color.lightGray);
				g.drawLine(0,(int)(hoogte-(by+j*eenheidyD)),breedte,(int)(hoogte-(by+j*eenheidyD)));
			}	
			g.setColor(Color.black);
			if(bx>1 && bx<breedte)
			{	g.drawLine(bx-1,0,bx-1,hoogte);	
				g.drawLine(bx,0,bx,hoogte);
			}
			if(by>0 && by<hoogte)
			{	g.drawLine(0,hoogte-(by+1),breedte,hoogte-(by+1));
				g.drawLine(0,hoogte-(by),breedte,hoogte-(by));
			}
			g.drawString("O",bx-10,hoogte-by+12);
			
			for(int j=0 ; j<aantalPijlenIn ; j++)
			{	if(isLijnGrafiek[j] && expressies[j]!=null && expressies[j].geefVarNaam()!=null && varNaam.equals(expressies[j].geefVarNaam())&& !expressies[j].geefVarNaam().equals(""))// && exp.geefVarNaam()!=null && !exp.geefVarNaam().equals(""))
				{	g.setColor(Color.black);
					for(int i=0 ; i<breedte ; i++)
					{	double ii = i;
						double d0 = expressies[j].geefW(schaalFactorX*(-beginx)/eenheidxD + schaalFactorX*ii/eenheidxD);//dd0.doubleValue();
						double d1 = expressies[j].geefW(schaalFactorX*(-beginx)/eenheidxD + schaalFactorX*(ii+1)/eenheidxD);//dd1.doubleValue();					if(b0 && b1)
						if(!Double.isNaN(d0) && !Double.isNaN(d1))
						{	int x0 = i;
							int x1 = i+1;
							double dy0 = Math.round(hoogte -(beginy+eenheidyD*d0/schaalFactorY));
							double dy1 = Math.round(hoogte -(beginy+eenheidyD*d1/schaalFactorY));
							if(dy0>1000)dy0 = 1000;
							if(dy0<-1000)dy0 = -1000;
							if(dy1>1000)dy1 = 1000;
							if(dy1<-1000)dy1 = -1000;
							int y0 = (int)dy0;
							int y1 = (int)dy1;
							g.drawLine(x0,y0,x1,y1);
						}
					}
					g.setColor(new Color(255,0,0));
					double d = bx+1.0*((selectnummer+beginwaarde)*eenheidx);
					int x = (int)Math.round(d);
					double d0 = expressies[j].geefW((selectnummer+beginwaarde)*schaalFactorX);
					if(!tracing && !Double.isNaN(d0) && selectnummer<8 && selectnummer>-1)
					{	int y = (int)Math.round(hoogte -(beginy+eenheidy*d0/schaalFactorY));
						g.fillOval(x-2,y-2,5,5);
						g.drawLine(x,y,x,hoogte);
						g.drawLine(x,y,0,y);
						tracexD = d;
						tracex = x;
						slider.zetStand(tracex);
					}
					else if(trace)
					{	double dTraceX = schaalFactorX*(-beginx)/eenheidxD + schaalFactorX*tracexD/eenheidxD;
						double dTraceY = expressies[j].geefW(dTraceX);
						if(!Double.isNaN(dTraceY) && tracex<veldb && tracex>-1)
						{	int tracey = (int)Math.round(hoogte -(beginy+eenheidy*dTraceY/schaalFactorY));
							g.fillOval(tracex-2,tracey-2,5,5);
							g.drawLine(tracex,tracey,tracex,hoogte);
							g.drawLine(tracex,tracey,0,tracey);
						
							String xWaarde = dfTrace.format(dTraceX);
							String yWaarde = dfTrace.format(dTraceY);
							g.setFont(font);
							fm = g.getFontMetrics();
							int woordBreedteX = fm.stringWidth(xWaarde);
							int woordHoogteX = fm.getAscent();
							int woordBreedteY = fm.stringWidth(yWaarde);
							int woordHoogteY = fm.getAscent();
							g.setColor(new Color(255,255,200));
							g.fillRect(tracex-woordBreedteX/2-2, hoogte-woordHoogteX-2, woordBreedteX+4, woordHoogteX+2);
							g.fillRect(0, tracey-woordHoogteY/2-2, woordBreedteY+4, woordHoogteY+4);
							g.setColor(Color.black);
							g.drawRect(tracex-woordBreedteX/2-2, hoogte-woordHoogteX-2, woordBreedteX+4, woordHoogteX+2);
							g.drawRect(0, tracey-woordHoogteY/2-2, woordBreedteY+4, woordHoogteY+4);
							g.drawString(xWaarde, tracex-woordBreedteX/2, hoogte-2);
							g.drawString(yWaarde, 2, tracey+woordHoogteY/2);
						}
					}
				}
				else if(isPuntGrafiek[j] && expressies[j]!=null && expressies[j].geefVarNaam()==null)
				{	double d = bx+1.0*((puntXWaarde[j])*eenheidx/schaalFactorX);
					int x = (int)d;
					double d0 = expressies[j].geefW((puntXWaarde[j])*schaalFactorX);
					int y = (int)Math.round(hoogte -(beginy+eenheidy*d0/schaalFactorY));
					g.setColor(Color.black);
					g.fillOval(x-2,y-2,5,5);
					
							
				}
				if(isMeerPuntenGrafiek[j] && expressies[j]!=null)
				{	g.setColor(Color.black);
					for (int k = 0; k<8; k++) 
					{	double d = bx+1.0*((k+beginwaarde)*eenheidx);
						int x = (int)d;
						if(expressies[j].isWaarde((k+beginwaarde)*schaalFactorX) && k<8 && selectnummer>-1)
						{	double d0 = expressies[j].geefW((k+beginwaarde)*schaalFactorX);
							int y = (int)Math.round(hoogte -(beginy+eenheidy*d0/schaalFactorY));
							g.fillOval(x-2,y-2,5,5);
						}
				    }
				    g.setColor(new Color(255,0,0));
					double d = bx+1.0*((selectnummer+beginwaarde)*eenheidx);
					int x = (int)d;
					if(!tracing && expressies[j].isWaarde((selectnummer+beginwaarde)*schaalFactorX) && selectnummer<8 && selectnummer>-1)
					{	double d0 = expressies[j].geefW((selectnummer+beginwaarde)*schaalFactorX);
						int y = (int)Math.round(hoogte -(beginy+eenheidy*d0/schaalFactorY));
						g.fillOval(x-2,y-2,5,5);
						g.drawLine(x,y,x,hoogte);
						g.drawLine(x,y,0,y);
					}
					
					if(isPuntGrafiek[j])
					{	d = bx+1.0*((puntXWaarde[j])*eenheidx/schaalFactorX);
						x = (int)d;
						double d0 = expressies[j].geefW((puntXWaarde[j]));
						int y = (int)Math.round(hoogte -(beginy+eenheidy*d0/schaalFactorY));
						g.setColor(Color.black);
						g.fillOval(x-2,y-2,5,5);
					}
							
				}
			}
			
		}
	}
}
