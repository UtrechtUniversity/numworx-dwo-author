package fi.algebraexpressies;

import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Hashtable;

import fi.algebraexpressies.expressies_ap.*;
import fi.algebraexpressies.schuifobjects.*;

import javax.swing.*;

public class GrafiekComponent extends AlgebraSchuifComponent 
							  implements ActionListener, MouseListener, MouseMotionListener
{
  	private int eenheid = 16;
	 
  	private PlusMinKnop pmKnopY,pmKnopX; 
	private ZoomKnop zoomInX, zoomUitX, zoomInY, zoomUitY, zoomIn, zoomUit, zoomStandaard; 
	
	 private Expressie[] expressies;
	 private int maxAantalExpressies;
	 private int aantalExpressies;
	 
	 private boolean gevuld;
	 private int beginwaarde;
	 private int selectnummer;
	 private String varNaam = "qq";
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
	private boolean trace=false;
	private boolean tracing=false;
		
	private Slider slider;
	private int tracex=-2;
	private double tracexD = tracex;
	//private LWCheckbox traceCheckbox;
	private JCheckBox traceCheckbox;
	
	private JPopupMenu popup;
	private boolean kettingZichtbaar = true;
	private int movex, movey;
	 
	private Color[] colors;
	private Color traceKleur = Color.red;
		
	
    public GrafiekComponent(AlgebraSchuifVeld sv,int x, int y, int b, int h)
	{	
		super(1,sv,x,y,b,h);

		maxAantalExpressies = 10;
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
		veldx = 30;
		veldy = 50;
		veldb = b-50;//160;
		veldh = h-85;//160;
		schaalFactorY = 1;
		factorRijNummerY = 99;
		schaalFactorX = 1;
		factorRijNummerX = 99;
		varNaam = "qq";
		formuleNaam = "";
		
		dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		df = new DecimalFormat("0.####", dfs);
		dfTrace = new DecimalFormat("0.##",dfs);
		
		gv = new GrafiekVeld(veldx,veldy,veldb,veldh);
		gv.addMouseListener(this);
		gv.addMouseMotionListener(this);
		
		add(gv);
		
		zoomStandaard = new ZoomKnop("standaard");
		//zoomStandaard.setBounds(32,2,25,25);
		zoomStandaard.setBounds(22,12,25,25);
		zoomStandaard.addActionListener(this);
		zoomStandaard.setToolTip("Standaard weergave");
		add(zoomStandaard);
		
		zoomIn	= new ZoomKnop("zoomin");
		//zoomIn.setBounds(57,2,25,25);
		zoomIn.setBounds(47,12,25,25);
		zoomIn.addActionListener(this);
		zoomIn.setToolTip("Zoom in");
		add(zoomIn);
		
		zoomUit	= new ZoomKnop("zoomuit");
		//zoomUit.setBounds(82,2,25,25);
		zoomUit.setBounds(72,12,25,25);
		zoomUit.addActionListener(this);
		zoomUit.setToolTip("Zoom uit");
		add(zoomUit);	
		
		zoomInX	= new ZoomKnop("zoominx");
		//zoomInX.setBounds(107,2,25,25);
		zoomInX.setBounds(97,12,25,25);
		zoomInX.addActionListener(this);
		zoomInX.setToolTip("Zoom in horizontaal");
		add(zoomInX);
		
		zoomUitX= new ZoomKnop("zoomuitx");
		//zoomUitX.setBounds(132,2,25,25);
		zoomUitX.setBounds(122,12,25,25);
		zoomUitX.addActionListener(this);
		zoomUitX.setToolTip("Zoom uit horizontaal");
		add(zoomUitX);
		
		zoomInY	= new ZoomKnop("zoominy");
		//zoomInY.setBounds(157,2,25,25);
		zoomInY.setBounds(147,12,25,25);
		zoomInY.addActionListener(this);
		zoomInY.setToolTip("Zoom in vertikaal");
		add(zoomInY);
		
		zoomUitY= new ZoomKnop("zoomuity");
		//zoomUitY.setBounds(182,2,25,25);
		zoomUitY.setBounds(172,12,25,25);
		zoomUitY.addActionListener(this);
		zoomUitY.setToolTip("Zoom uit vertikaal");
		add(zoomUitY);
		
		slider = new Slider(veldb,0);
		slider.setLocation(veldx-5,h-13);
		slider.addActionListener(this);
		slider.setBackground(new Color(210,210,210));
		slider.setVisible(trace);
		add(slider);
		
		traceCheckbox = new JCheckBox();
		//traceCheckbox.setBounds(13,getSize().height-13,10,10);
		traceCheckbox.setBounds(0,getSize().height-13,17,10);
		traceCheckbox.setBackground(Color.white);
		traceCheckbox.addActionListener(this);
		add(traceCheckbox);
		traceCheckbox.setOpaque(false);
		//if(((AlgebraSchuifVeld)schuifveld).fixed)traceCheckbox.setVisible(false);
		
		
		popup = new JPopupMenu();
		
		JMenuItem mi = new JMenuItem(AlgebraExpressies.rb.getString("popup1Label5"));
		mi.addActionListener(this);
		popup.add(mi);
		
		mi = new JMenuItem(AlgebraExpressies.rb.getString("popup1Label6"));
		mi.addActionListener(this);
		popup.add(mi);
		
		add(popup);
		
		colors = new Color[10];
        
		colors[0] = new Color(0,0,255);
		colors[1] = new Color(0,200,0);
		colors[2] = new Color(255,50,50);
		colors[3] = new Color(00,220,220);
		colors[4] = new Color(220,0,220);
		colors[5] = new Color(200,200,0);
		colors[6] = Color.black;
		colors[7] = Color.black;
		colors[8] = Color.black;
		colors[9] = Color.black;
        
	}

	public void setSize(int b, int h)
	{	
		super.setSize(b,h);
		veldb = b-50;
		veldh = h-85;
		gv.setSize(veldb,veldh);
		slider.zetLengte(veldb);
		slider.setLocation(veldx-5,h-13);
		//traceCheckbox.setBounds(13,getSize().height-13,10,10);
		traceCheckbox.setBounds(0,getSize().height-13,17,10);
	}
    
	public Hashtable getState()
	{	int sizeB = 0;
		int sizeH = 0;
		boolean trace = false;
		double tracexD = 0;
		double beginy = 0;
		boolean kettingZichtbaar = true;
		double schaalFactorY  = 1;
		int factorRijNummerY = 99;
							
		sizeB = getSize().width;
		sizeH = getSize().height;
		trace = this.trace;
		tracexD = this.tracexD;
		beginy = this.beginy;
		kettingZichtbaar = this.kettingZichtbaar;
		schaalFactorY = this.schaalFactorY;
		factorRijNummerY = this.factorRijNummerY;
		
		Hashtable h = super.getState();
		
	    h.put("sizeB", new Integer(sizeB));
	    h.put("sizeH", new Integer(sizeH));
	    h.put("trace", new Boolean(trace));
	    h.put("tracexD", new Double(tracexD));
	    h.put("beginy", new Double(beginy));
	    h.put("kettingZichtbaar", new Boolean(kettingZichtbaar));
	    h.put("schaalFactorY", new Double(schaalFactorY));
	    h.put("factorRijNummerY", new Integer(factorRijNummerY));
	    return h;
	}
	
	public void setState(Hashtable h)
    {	int sizeB = 0;
		int sizeH = 0;
		boolean trace = false;
		double tracexD = 0;
		double beginy = 0;
		boolean kettingZichtbaar = true;
		double schaalFactorY  = 1;
		int factorRijNummerY = 99;
		
		if (h.containsKey("sizeB")) sizeB = ((Integer)h.get("sizeB")).intValue();
    	if (h.containsKey("sizeH")) sizeH = ((Integer)h.get("sizeH")).intValue();
    	if (h.containsKey("trace")) trace = ((Boolean)h.get("trace")).booleanValue();
    	if (h.containsKey("tracexD")) tracexD = ((Double)h.get("tracexD")).doubleValue();
    	if (h.containsKey("beginy")) beginy = ((Double)h.get("beginy")).doubleValue();
    	if (h.containsKey("kettingZichtbaar")) kettingZichtbaar = ((Boolean)h.get("kettingZichtbaar")).booleanValue();
    	if (h.containsKey("schaalFactorY")) schaalFactorY = ((Double)h.get("schaalFactorY")).doubleValue();
    	if (h.containsKey("factorRijNummerY")) factorRijNummerY = ((Integer)h.get("factorRijNummerY")).intValue();
    	
		
		setSize(sizeB,sizeH);
		this.trace = trace;
		this.tracexD = tracexD;
		this.beginy = beginy;
		tracex = (int)Math.round(tracexD);
		slider.zetStand(tracex);
		this.kettingZichtbaar = kettingZichtbaar;
		if(!kettingZichtbaar)zetBoomZichtbaarHier(kettingZichtbaar);
		this.schaalFactorY = schaalFactorY;
		this.factorRijNummerY = factorRijNummerY;
        
		traceCheckbox.setSelected(trace);
//		traceCheckbox.aan = trace;
		slider.setVisible(trace);
		
    }
	
	public void drawDottedLine(Graphics g, int x0, int y0, int x1, int y1)
	{
		int dx = 3;
		double length = Math.sqrt((double)((x1-x0)*(x1-x0)) + (double)((y1-y0)*(y1-y0)));
		System.out.println(""+length);
		int n = (int)Math.round(length/dx);
		System.out.println(""+n);
		for(int i=0 ; i<n ; i+=2)
		{
			int xn0 = x0 + (int)Math.round((double)(x1-x0)*i/n);
			int yn0 = y0 + (int)Math.round((double)(y1-y0)*i/n);
			int xn1 = x0 + (int)Math.round((double)(x1-x0)*(i+1)/n);
			int yn1 = y0 + (int)Math.round((double)(y1-y0)*(i+1)/n);
			g.drawLine(xn0,yn0,xn1,yn1);
			
		}
	}
	
	public void paint(Graphics g)
	{	g.setFont(font);
	
		int breedte = getSize().width;
		int hoogte = getSize().height;
		g.setColor(new Color(210,210,210));
		g.fillRect(0,10,breedte - 1,hoogte - 11);
		g.setColor(Color.black);
		g.drawRect(0,10,breedte-1,hoogte - 11);
		
		g.setColor(Color.white);
		g.drawLine(breedte-10, hoogte-2, breedte-2, hoogte-10);
		g.drawLine(breedte-7, hoogte-2, breedte-2, hoogte-7);
		
		g.setColor(Color.gray.darker());
		g.drawLine(breedte-9, hoogte-2, breedte-2, hoogte-9);
		g.drawLine(breedte-6, hoogte-2, breedte-2, hoogte-6);
		
		g.setColor(Color.white);
		g.fillRect(veldx-1,veldy-1,veldb+1,veldh+1);
		
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
		
		g.setColor(Color.black);
		FontMetrics fm = g.getFontMetrics();
		int woordbreedte = fm.stringWidth(varNaam);
		boolean b = varNaam.equals("qq") || varNaam.length()>2 && varNaam.substring(0,2).equals("qq");
		if(!b) 
			g.drawString(varNaam, veldx+veldb+5,veldy+veldh+5);
		g.drawString(formuleNaam,veldx,veldy-8);
		
/*
		if(aantalPijlenIn>0 && expressies[0]!=null && !expressies[0].geefVarNaam().equals("") && selectnummer>-1 && selectnummer<11)
		{	g.setColor(new Color(255,200,200));
			g.fillRect(veldx+(selectnummer+1)*eenheid-eenheid/2,veldy+veldh,eenheid,eenheid);
			g.setColor(Color.black);
			g.drawRect(veldx+(selectnummer+1)*eenheid-eenheid/2,veldy+veldh,eenheid,eenheid);
		}
*/		
		int imin = -(int)Math.round(beginx/eenheid); 
		int imax = 1+veldb/eenheid-(int)Math.round(beginx/eenheid);
		int jmin = -(int)Math.round(beginy/eenheid); 
		int jmax = 1+veldb/eenheid-(int)Math.round(beginy/eenheid);
		int bx = (int)beginx;
		int by = (int)beginy;
		
/*		
		for(int i=1 ; i<1+veldb/eenheid ; i++)
		{	new Expressie();
			String getal = Expressie.df.format(schaalFactorX*(imin+i));
			woordbreedte = fm.stringWidth(getal);
			if(schaalFactorX>0.5 && schaalFactorX<5)g.drawString(getal,veldx+i*eenheid-woordbreedte/2,veldy+veldh+13);
			else if((i-beginwaarde-1)%2==0)g.drawString(getal,veldx+i*eenheid-woordbreedte/2,veldy+veldh+13);
		}
		for(int j=1 ; j<1+veldh/eenheid ; j++)
		{	String getal = Expressie.df.format(schaalFactorY*(jmin+j));
			woordbreedte = fm.stringWidth(getal);
			g.drawString(getal,veldx-3-woordbreedte,veldy+veldh+5-(j*eenheid));
		}
*/		
		
		for(int i=imin+1 ; i<imax ; i++)
		{	new Expressie();
			String getal = df.format(schaalFactorX*(i));
			woordbreedte = fm.stringWidth(getal);
			if(schaalFactorX>0.5 && schaalFactorX<5 && woordbreedte<eenheidx)
				g.drawString(getal,(int)(veldx+beginx+i*eenheidxD-woordbreedte/2),veldy+veldh+15);
			else if(i%2==0)
				g.drawString(getal,(int)(veldx+beginx+i*eenheidxD-woordbreedte/2),veldy+veldh+15);
		}
		for(int j=jmin+1 ; j<jmax ; j++)
		{	String getal = df.format(schaalFactorY*(j));
			woordbreedte = fm.stringWidth(getal);
			g.drawString(getal,veldx-5-woordbreedte,(int)(veldy+veldh+5-(beginy+j*eenheidyD)));
		}
		
		g.drawString("trace",12,hoogte-15);		
		super.paint(g);	
	}	
		
	public void zetBegin(int x, int y)
	{	beginx = eenheidx*x;
		beginy = eenheidy*y;
	}
	

/*	
	public void zetExpressie(int nr,Expressie e)
	{	Expressie exp = null;
		if(e!=null && e.geefWaarde()==null )
		{	exp = e;
			if(varNaam.equals(""))varNaam = e.geefVarNaam();
		}
		else exp = null;
		expressies[nr] = exp;
		gv.tekenOpnieuw();
	}
*/
	public void zetExpressie(int nr,Expressie e)
	{	Expressie exp = null;
		if(e!=null && e.geefVarNaam()!=null )
		{	exp = e;
			if(varNaam.equals("qq")|| aantalPijlenIn==1)varNaam = e.geefVarNaam();
			isPuntGrafiek[nr] = false;
		}
		else if(e!=null && !Double.isNaN(e.geefWaarde().doubleValue()) && pijlenIn[nr]!=null)
		{	
			AlgebraSchuifComponent asc = pijlenIn[nr].zender;
			int teller = 20;
			puntXWaarde[nr] = asc.geefUitvoer(teller).geefWaarde().doubleValue();
			while(asc.pijlIn1 !=null && teller > 0)
			{	teller--;
				asc = asc.pijlIn1.zender;
				if(!Double.isNaN(asc.geefUitvoer(teller).geefWaarde().doubleValue()))
					puntXWaarde[nr] = asc.geefUitvoer(teller).geefWaarde().doubleValue();
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
	
	public void setZoomState(String varNaam, ZoomState zoomState)
	{	if(varNaam.equals(this.varNaam) && zoomState!=null)
		{	double beginxOud = beginx;
			double factorXOud = schaalFactorX;
			int factorRijNummerXOud = factorRijNummerX;
			System.out.println("beginx3 = "+beginx);
			//this.beginwaarde = zoomState.getBeginwaarde();
			//this.selectnummer = zoomState.getSelectnummer();
			//this.schaalFactorX = zoomState.getSchaalFactorX();
			//this.schaalFactorY = zoomState.getSchaalFactorY();
			this.factorRijNummerX = zoomState.getFactorRijNummerX();
			//this.factorRijNummerY = zoomState.getFactorRijNummerY();
			//this.beginx = ((double)zoomState.getBeginx()*eenheid)/14+eenheid;
			//this.beginy = (double)zoomState.getBeginy();
			//this.tracexD = (double)zoomState.getTracexD();
			
			/*if(factorRijNummerX-factorRijNummerXOud==1)
			{	factorRijNummerX--;
				zoomDraad = new ZoomDraad(true,false,false);
				zoomDraad.start();
			}
			else if(factorRijNummerX-factorRijNummerXOud==-1)
			{	factorRijNummerX++;
				zoomDraad = new ZoomDraad(true,false,true);
				zoomDraad.start();
			}
			else*/
			{	this.schaalFactorX = zoomState.getSchaalFactorX();
				this.beginwaarde = zoomState.getBeginwaarde();
				this.beginx = ((double)zoomState.getBeginx()*eenheid)/14+eenheid;
				this.selectnummer = zoomState.getSelectnummer();
				
			}
			
			System.out.println("dx = "+beginwaarde);
			System.out.println("dx = "+beginx);
			
			double dx  = beginx-beginxOud;
			double factor = schaalFactorX/factorXOud;
			
			if(trace && tracex!=-2) {
				tracexD = beginx+(tracexD-beginx)/factor+dx;
				tracex = (int)Math.round(tracexD);
				slider.zetStand(tracex);
			}
			
			
			
			
			
			//beginx = eenheidx-eenheidx*beginwaarde;
			if(selectnummer!=999)tracing = false;
			else
			{	//tracexD = beginx+1.0*((selectnummer+beginwaarde)*eenheidx);
				//tracex = (int)Math.round(tracexD);
				//slider.zetStand(tracex);
			}
			gv.tekenOpnieuw();
		}
	}
	
	public void zetBoomZichtbaarHier(boolean b)
    {   for(int i=0 ; i<aantalPijlenIn ; i++)
		{	if(pijlenIn[i]!=null)pijlenIn[i].zender.zetBoomZichtbaar(b);
	        kettingZichtbaar = b;
	        schuifveld.tekenOpnieuw();
	    }
    }
/*	
	public void zetVeranderd(int max)
	{	
		for(int i=0 ; i<aantalPijlenIn ; i++)
		{	Expressie e = pijlenIn[i].zender.geefUitvoer(20);
			zetExpressie(i,e);
			formuleNaam = ((UitvoerSchuifComponent)pijlenIn[i].zender).geefLabelTekst();
			
		}
		super.zetVeranderd(max);
	}
*/
	public void zetVeranderd(int max)
	{	for(int i=0 ; i<aantalPijlenIn ; i++)
		{	Expressie e = pijlenIn[i].zender.geefUitvoer(20);
			Expressie ev = pijlenIn[i].zender.geefVerborgenUitvoer(20);
			zetExpressie(i,e);
			formuleNaam = ((UitvoerSchuifComponent)pijlenIn[i].zender).geefLabelTekst();
			if ((e==null || !Double.isNaN(e.geefWaarde().doubleValue())) && 
				!(ev instanceof BasisExpressie) && ((UitvoerSchuifComponent)pijlenIn[i].zender).tabelZichtbaar)
			{	zetExpressie(i,ev);
				isMeerPuntenGrafiek[i] = true;
				isLijnGrafiek[i] = false;
				if (e!=null && !Double.isNaN(e.geefWaarde().doubleValue())) 
				{	AlgebraSchuifComponent asc = pijlenIn[i].zender;
					int teller = 20;
					puntXWaarde[i] = asc.geefUitvoer(teller).geefWaarde().doubleValue();
					while(asc.pijlIn1 !=null && teller > 0)
					{	teller--;
						asc = asc.pijlIn1.zender;
						Double d = asc.geefUitvoer(teller).geefWaarde();
						if(!Double.isNaN(d.doubleValue())) 
							puntXWaarde[i] = d.doubleValue();
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
		
		if(getParent()!=null)setZoomState(varNaam,((AlgebraSchuifVeld)getParent()).zoomStateHolder.getZoomState(varNaam));
        super.zetVeranderd(max);
	}
	
	public void verbind(Pijl p, int nr)
	{	pijlenIn[nr] = p;
		p.zetEind(getLocation().x + 10 + nr * 15, getLocation().y + 10);
		aantalPijlenIn++;
		pijlenIn[nr].setColor(colors[nr]);
	}
	
	public boolean meldAan(Pijl p, int x, int y)
	{	if (!(p.zender instanceof UitvoerSchuifComponent))
			return false;
	
		for (int i = 0; i < aantalPijlenIn; i++)
		{	if (pijlenIn[i].zender == p.zender) 
				return false;
		}
		AlgebraSchuifComponent asc = p.zender;
		int teller = 20;
		Expressie e = asc.geefUitvoer(teller);
		while (asc.pijlIn1 != null && teller > 0)
		{	teller--;
			asc = asc.pijlIn1.zender;
			e = asc.geefUitvoer(teller);
		}
	
		if (e != null && e.geefVarNaam() != null && varNaam != "qq" && !e.geefVarNaam().equals(varNaam)) 
			return false;
		
		//if(p.zender.geefUitvoer(20)!=null && !varNaam.equals("") && !varNaam.equals(p.zender.geefUitvoer(20).geefVarNaam()))return false;
		Rectangle ingang = new Rectangle(0, -10, getSize().width,  getSize().height + 10);
		if (aantalPijlenIn < 10 && ingang.contains(x - getLocation().x, y - getLocation().y))
		{	p.zetEind(getLocation().x + 10 + aantalPijlenIn * 15, getLocation().y + 10);
			pijlenIn[aantalPijlenIn] = p;
			aantalPijlenIn++;
			if (e != null && e.geefVarNaam() != null) 
				varNaam = e.geefVarNaam();
			
//			((UitvoerSchuifComponent)p.zender).zetGrafiek(true,this);
//			((UitvoerSchuifComponent)p.zender).zetTabel(beginwaarde,selectnummer, varNaam, schaalFactorX);
			zetVeranderd(20);
			if (p != null) 
				p.setColor(colors[aantalPijlenIn - 1]);
			schuifveld.tekenOpnieuw();
			return true;
		}
		return false;
	}
	
	public void maakLos(Pijl p)
	{	for(int i=0 ; i<aantalPijlenIn ; i++)
		{	p.setColor(Color.black);
			
			if(p==pijlenIn[i])
			{	
				Color colorRes = colors[i];
				for(int j=i ; j<aantalPijlenIn-1 ; j++)
				{	pijlenIn[j] = pijlenIn[j+1];
					colors[j] = colors[j+1];
					pijlenIn[j].zetEind(getLocation().x+10+j*15 , getLocation().y+10);
					expressies[j] = expressies[j+1];
				}
				colors[aantalPijlenIn-1] = colorRes;
				
				pijlenIn[aantalPijlenIn-1]=null;
				aantalPijlenIn--;
				break;
			}
		}
		//((UitvoerSchuifComponent)p.zender).zetGrafiek(false,this);
		selectnummer = 999;
		if(aantalPijlenIn==0)
		{	//((AlgebraSchuifVeld)getParent()).zetTabellen(0,selectnummer, varNaam, 1);
			//zetTabel(0,selectnummer, varNaam, 1);
			((AlgebraSchuifVeld)getParent()).zoomStateHolder.setBeginy(varNaam, eenheidy);
			varNaam = "qq";
			formuleNaam = "";
			beginy = eenheidy;
		}
	}
/*	
	public void mousePressed(MouseEvent e)
	{	if (e.getSource() == gv)
		{	startxv = e.getX();
			startyv = e.getY();
		}
		super.mousePressed(e);
	}	
*/	
	public void mousePressed(MouseEvent e)
	{	
		
		if (((AlgebraSchuifVeld) schuifveld).isDemo)
			return;
		if (((AlgebraSchuifVeld) schuifveld).frozen)
			return;
		
		requestFocus();
		
		if(e.getModifiers()== e.BUTTON3_MASK || e.isControlDown())
		{	
			//if (((AlgebraSchuifVeld) schuifveld).isDemo)
			//	return;
			if (((AlgebraSchuifVeld) schuifveld).alleenInvullen)
				return;
			
			popup.show(this,e.getX(),e.getY());
			return;
		}
		
		if (e.getSource() == gv)
		{	startxv = e.getX();
			startyv = e.getY();
			setCursor(new Cursor(Cursor.MOVE_CURSOR));
		}
		else if (e.getX() > getSize().width - 10 && e.getY()> getSize().height - 10)
		{	resize = true;
			startx = e.getX();
			starty = e.getY();
			setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));
		}
		//else if(e.getX()>40 && e.getX()<getSize().width-10 && e.getY()>getSize().height-10)
		//{	trace = true;
		//	startx = e.getX();
		//	starty = e.getY();
		//}
		else 
			super.mousePressed(e);
	}	
	
	public void mouseDragged(MouseEvent e)
	{	
		if (((AlgebraSchuifVeld) schuifveld).isDemo)
			return;
		if (((AlgebraSchuifVeld) schuifveld).frozen)
			return;
		
		
		if (e.getSource() == gv)
		{	int dx = e.getX() - startxv;
			int dy =  e.getY() - startyv;
		
			beginx = beginx + dx;
			beginy = beginy - dy;
			
			if(trace && tracex!=-2) 
			{
				tracexD = tracexD+dx;
				tracex = tracex+dx;
				slider.zetStand(tracex);
			}
			
			int b = beginwaarde;
			//beginwaarde = 1-(int) Math.round(beginx / eenheid);
			if (beginx > 0) 
				beginwaarde = 1 - (int) Math.round((beginx - eenheidx / 2) / eenheidx);
			else 
				beginwaarde = 1 - (int) Math.round((beginx + eenheidx / 2) / eenheidx);
			selectnummer = selectnummer + b - beginwaarde;

//			System.out.println("beginx1 = "+beginx);
			((AlgebraSchuifVeld)getParent()).zoomStateHolder.setBeginwaarde(varNaam, beginwaarde);
            ((AlgebraSchuifVeld)getParent()).zoomStateHolder.setSelectnummer(varNaam, selectnummer);
            ((AlgebraSchuifVeld)getParent()).zoomStateHolder.setBeginx(varNaam, ((beginx-eenheid)*14)/eenheid);
            ((AlgebraSchuifVeld)getParent()).zoomStateHolder.setBeginy(varNaam, beginy);
            ((AlgebraSchuifVeld)getParent()).zoomStateHolder.setTracexD(varNaam, tracexD);
			((AlgebraSchuifVeld)getParent()).zoomStateHolder.setZoomStates(varNaam);
            //System.out.println("beginx2 = "+beginx);			
			//gv.tekenOpnieuw();
		
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
			//schuifveld.tekenOpnieuw();
		}
	}
	
	/*public void mouseReleased(MouseEvent e)
	{	if(e.getSource()==gv)
		{	beginx = eenheid*Math.round(beginx/eenheid);
			beginy = eenheid*Math.round(beginy/eenheid);

			UitvoerSchuifComponent usc = null;
			if(pijlIn1!=null)
			{	usc = (UitvoerSchuifComponent)pijlIn1.zender;
				usc.zetTabel(beginwaarde,selectnummer, varNaam, schaalFactorX);
			}
			gv.tekenOpnieuw();
		}
		super.mouseReleased(e);
	}*/
	public void mouseReleased(MouseEvent e)
	{	
		if (((AlgebraSchuifVeld) schuifveld).isDemo)
			return;
		if (((AlgebraSchuifVeld) schuifveld).frozen)
			return;
		
		
		resize = false;
		
		if(e.getSource()==gv)
		{	setCursor(new Cursor(Cursor.HAND_CURSOR));
		
			double beginxR = beginx;
			beginx = eenheid*Math.round(beginx/eenheid);
			beginy = eenheid*Math.round(beginy/eenheid);

			if(trace && tracex!=-2) 
			{
				tracexD += beginx-beginxR;
				tracex += beginx-beginxR;
				slider.zetStand(tracex);
			}
			
			if(aantalPijlenIn>0)
			{	//((AlgebraSchuifVeld)getParent()).zetTabellen(beginwaarde,selectnummer, varNaam, schaalFactorX);
				
				
				((AlgebraSchuifVeld)getParent()).zoomStateHolder.setBeginwaarde(varNaam, beginwaarde);
	            ((AlgebraSchuifVeld)getParent()).zoomStateHolder.setSchaalFactorX(varNaam, schaalFactorX);
	            ((AlgebraSchuifVeld)getParent()).zoomStateHolder.setFactorRijNummerX(varNaam, factorRijNummerX);
	            ((AlgebraSchuifVeld)getParent()).zoomStateHolder.setSchaalFactorY(varNaam, schaalFactorY);
	            ((AlgebraSchuifVeld)getParent()).zoomStateHolder.setFactorRijNummerY(varNaam, factorRijNummerY);
	            ((AlgebraSchuifVeld)getParent()).zoomStateHolder.setSelectnummer(varNaam, selectnummer);
	            ((AlgebraSchuifVeld)getParent()).zoomStateHolder.setBeginx(varNaam, Math.round(beginx-eenheidx)*14/16);
	            ((AlgebraSchuifVeld)getParent()).zoomStateHolder.setBeginy(varNaam, Math.round(beginy));
	            ((AlgebraSchuifVeld)getParent()).zoomStateHolder.setTracexD(varNaam, tracexD);
				((AlgebraSchuifVeld)getParent()).zoomStateHolder.setZoomStates(varNaam);
	            
			}
/*			
			UitvoerSchuifComponent usc = null;
			for(int i=0 ; i<aantalPijlenIn ; i++)
			{	usc = (UitvoerSchuifComponent)pijlenIn[i].zender;
				usc.zetTabel(beginwaarde,selectnummer, varNaam, schaalFactorX);
			}
*/			
			gv.tekenOpnieuw();
			schuifveld.tekenOpnieuw();
		}
		else
			super.mouseReleased(e);
	}
	
	public void mouseMoved(MouseEvent e)
	{	
		if (((AlgebraSchuifVeld) schuifveld).isDemo)
			return;
		if (((AlgebraSchuifVeld) schuifveld).frozen)
			return;
		
		
		movex = e.getX();
		movey = e.getY();
		if(e.getX()>getSize().width-10 && e.getY()>getSize().height-10)
		{	setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));
		}
		else if(e.getX()>getSize().width-20 && e.getY()>getSize().height-20)
		{	setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
		gv.tekenOpnieuw();
		schuifveld.tekenOpnieuw();
	}
	
	public void mouseExited(MouseEvent e)
	{	//if(e.getSource()==gv)
		{	
			if (((AlgebraSchuifVeld) schuifveld).isDemo)
				return;
			if (((AlgebraSchuifVeld) schuifveld).frozen)
				return;
			
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR ));
			gv.tekenOpnieuw();
		}
	
	}
	public void mouseClicked(MouseEvent e){;}
	
	public void actionPerformed(ActionEvent e)
	{	
		
		if (((AlgebraSchuifVeld) schuifveld).isDemo)
			return;
		if (((AlgebraSchuifVeld) schuifveld).frozen)
			return;
		
		if (zoomDraad != null && zoomDraad.isAlive())
			return;
		
		if (e.getActionCommand().equals("focus")) 
			schuifveld.tekenOpnieuw();
		else 
		{	
		
			if (e.getSource() instanceof JMenuItem && 
					((JMenuItem)e.getSource()).getText().equals(AlgebraExpressies.rb.getString("popup1Label5")))
				{	zetBoomZichtbaarHier(true);
				}
				else if (e.getSource() instanceof JMenuItem && 
						((JMenuItem)e.getSource()).getText().equals(AlgebraExpressies.rb.getString("popup1Label6")))
				{	zetBoomZichtbaarHier(false);
				}
				else if (e.getSource() == zoomUitY && factorRijNummerY < 120)
				{	zoomDraad = new ZoomDraad(false, true, false);
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
					{	//((AlgebraSchuifVeld)getParent()).zetTabellen(beginwaarde,selectnummer, varNaam, schaalFactorX);
						//String varnaam = null;
			            //if(expressie!=null) varnaam = expressie.geefVarNaam();
			            //if(varnaam==null && verborgenExpressie!=null) varnaam = verborgenExpressie.geefVarNaam();
			            ((AlgebraSchuifVeld)getParent()).zoomStateHolder.setBeginwaarde(varNaam, beginwaarde);
			            ((AlgebraSchuifVeld)getParent()).zoomStateHolder.setSchaalFactorX(varNaam, schaalFactorX);
			            ((AlgebraSchuifVeld)getParent()).zoomStateHolder.setFactorRijNummerX(varNaam, factorRijNummerX);
			            ((AlgebraSchuifVeld)getParent()).zoomStateHolder.setSchaalFactorY(varNaam, schaalFactorY);
			            ((AlgebraSchuifVeld)getParent()).zoomStateHolder.setFactorRijNummerY(varNaam, factorRijNummerY);
			            ((AlgebraSchuifVeld)getParent()).zoomStateHolder.setSelectnummer(varNaam, selectnummer);
			            ((AlgebraSchuifVeld)getParent()).zoomStateHolder.setBeginx(varNaam, (beginx-eenheidx)*14/16);
			            ((AlgebraSchuifVeld)getParent()).zoomStateHolder.setBeginy(varNaam, Math.round(beginy));
			            ((AlgebraSchuifVeld)getParent()).zoomStateHolder.setTracexD(varNaam, tracexD);
						((AlgebraSchuifVeld)getParent()).zoomStateHolder.setZoomStates(varNaam);
			            
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
// ???					((AlgebraSchuifVeld)getParent()).zetTabellen(beginwaarde, 999, varNaam, schaalFactorX);
				}
				//else if(e.getActionCommand().equals("stop")) tracing = false;
				tracex = slider.geefStand();
				tracexD = tracex;
				gv.tekenOpnieuw();
				schuifveld.tekenOpnieuw();
				
			}
			if (e.getSource()==traceCheckbox)
			{	
				//trace = traceCheckbox.aan;
				trace = traceCheckbox.isSelected();
				slider.setVisible(trace);
				gv.tekenOpnieuw();
				schuifveld.tekenOpnieuw();
			}
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
		{	if(x) selectnummer = 999;
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
			
			//beginx = Math.round(beginx/eenheid)*eenheid;
			
			beginwaarde = 1-(int)Math.round(beginx/eenheidx);
			double beginwaardeD = 1.0-(beginx/eenheidx);
			System.out.println(""+beginx);
			System.out.println(""+eenheidx);
			System.out.println(""+beginwaardeD);
			
			tracexD = tracexD + eenheid*(beginwaardeD - beginwaarde);
			tracex = (int) Math.round(tracexD);
			slider.zetStand(tracex);
			
			if(x)selectnummer = 999;
			
			beginx = eenheidx-eenheidx*beginwaarde;
			
            if(aantalPijlenIn>0)
			{	//((AlgebraSchuifVeld)getParent()).zetTabellen(beginwaarde,selectnummer, varNaam, schaalFactorX);
            	((AlgebraSchuifVeld)getParent()).zoomStateHolder.setBeginwaarde(varNaam, beginwaarde);
	            ((AlgebraSchuifVeld)getParent()).zoomStateHolder.setSchaalFactorX(varNaam, schaalFactorX);
	            ((AlgebraSchuifVeld)getParent()).zoomStateHolder.setFactorRijNummerX(varNaam, factorRijNummerX);
	            ((AlgebraSchuifVeld)getParent()).zoomStateHolder.setSchaalFactorY(varNaam, schaalFactorY);
	            ((AlgebraSchuifVeld)getParent()).zoomStateHolder.setFactorRijNummerY(varNaam, factorRijNummerY);
	            ((AlgebraSchuifVeld)getParent()).zoomStateHolder.setSelectnummer(varNaam, selectnummer);
	            ((AlgebraSchuifVeld)getParent()).zoomStateHolder.setBeginx(varNaam, (beginx-eenheid)*14/eenheid);
	            ((AlgebraSchuifVeld)getParent()).zoomStateHolder.setBeginy(varNaam, Math.round(beginy));
	            ((AlgebraSchuifVeld)getParent()).zoomStateHolder.setTracexD(varNaam, tracexD);
				((AlgebraSchuifVeld)getParent()).zoomStateHolder.setZoomStates(varNaam);
	            
			}
            
			gv.tekenOpnieuw();
			schuifveld.tekenOpnieuw();
		}
		public void maakDood()
		{	dood = true;
		}
	}
	
	class GrafiekVeld extends JComponent //Component
	{
		private Image im;
  		private Graphics gIm;
		private boolean veranderd;
	
		public GrafiekVeld(int x, int y, int b, int h)
		{	
			setBounds(x,y,b,h);
			veranderd = true;
		}
		
		public void paint(Graphics g)
		{	int breedte = getSize().width;
			int hoogte = getSize().height;
			if (veranderd)			
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
		
		public void tekenOpnieuw()
		{	veranderd = true;
			repaint();
		}
		
		public void setSize(int b, int h)
		{	super.setSize(b,h);
			tekenOpnieuw();
		}
		
		public void	tekenFunctie(Graphics g)
		{	int breedte = getSize().width;
			int hoogte = getSize().height;
			g.setClip(0, 0, breedte, hoogte);
			int imin = - (int) Math.round(beginx / eenheidx); 
			int imax = 1 + breedte / eenheidx - (int) Math.round(beginx / eenheidx);
			int bx = (int)Math.round(beginx);
			for (int i = imin; i < imax; i++)
			{	g.setColor(Color.lightGray);
				g.drawLine((int) (bx + i * eenheidxD), 0, (int) (bx + i * eenheidxD), hoogte);
			}
			int jmin = -(int) Math.round(beginy / eenheidy); 
			int jmax = 1 + hoogte / eenheidy - (int) Math.round(beginy / eenheidy);
			int by = (int)Math.round(beginy);
			for(int j=jmin ; j<jmax ; j++)
			{	g.setColor(Color.lightGray);
				//g.drawLine(0,hoogte-(by+j*eenheid),breedte,hoogte-(by+j*eenheid));
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
			{	if(isLijnGrafiek[j] && expressies[j]!=null && expressies[j].geefVarNaam()!=null && 
					varNaam.equals(expressies[j].geefVarNaam())&& !expressies[j].geefVarNaam().equals("qq"))// && exp.geefVarNaam()!=null && !exp.geefVarNaam().equals(""))
				{	g.setColor(pijlenIn[j].getColor());
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
				}
				else if(isPuntGrafiek[j] && expressies[j]!=null && expressies[j].geefVarNaam()==null && !Double.isNaN(puntXWaarde[j]))
				{	double d = bx+1.0*((puntXWaarde[j])*eenheidx/schaalFactorX);
					int x = (int)d;
					double d0 = expressies[j].geefW((puntXWaarde[j])*schaalFactorX);
					int y = (int)Math.round(hoogte -(beginy+eenheidy*d0/schaalFactorY));
					g.setColor(pijlenIn[j].getColor());
					g.fillOval(x-2,y-2,5,5);
					
					g.setFont(font);
					fm = g.getFontMetrics();
					String xString = dfTrace.format(puntXWaarde[j]);
					String yString = dfTrace.format(d0);
					int woordBreedte = 40;
					if(fm!=null) woordBreedte = fm.stringWidth(xString+yString);
					g.setColor(new Color(255,255,225));
					g.fillRect(x+6,y-7,woordBreedte+20,15);
					g.setColor(Color.black);
					g.drawString("(" + xString + " , " + yString + ")", x+8,y+5);
					
				}
				if(isMeerPuntenGrafiek[j] && expressies[j]!=null)
				{	g.setColor(pijlenIn[j].getColor());
					for (int k = 0; k<8; k++) 
					{	double d = bx+1.0*((k+beginwaarde)*eenheidx);
						int x = (int)d;
						if(expressies[j].isWaarde((k+beginwaarde)*schaalFactorX) && k<8 )
						{	double d0 = expressies[j].geefW((k+beginwaarde)*schaalFactorX);
							int y = (int)Math.round(hoogte -(beginy+eenheidy*d0/schaalFactorY));
							g.fillOval(x-2,y-2,5,5);
						}
				    }
					for (int k = 0; k<8; k++) 
					{	double d = bx+1.0*((k+beginwaarde)*eenheidx);
						int x = (int)d;
						if(expressies[j].isWaarde((k+beginwaarde)*schaalFactorX) && k<8 )
						{	double d0 = expressies[j].geefW((k+beginwaarde)*schaalFactorX);
							int y = (int)Math.round(hoogte -(beginy+eenheidy*d0/schaalFactorY));
							
							if(new Rectangle(x-2,y-2,5,5).contains(movex, movey))
							{	g.setFont(font);
								fm = g.getFontMetrics();
								String xString = dfTrace.format((k+beginwaarde)*schaalFactorX);
								String yString = dfTrace.format(d0);
								int woordBreedte = 40;
								if(fm!=null) woordBreedte = fm.stringWidth(xString+yString);
								g.setColor(new Color(255,255,225));
								g.fillRect(x+6,y-7,woordBreedte+20,15);
								g.setColor(Color.black);
								g.drawString("(" + xString + " , " + yString + ")", x+8,y+5);	
							}
						}
				    }
					
					if(isPuntGrafiek[j] && !Double.isNaN(puntXWaarde[j]))
					{	double d = bx+1.0*((selectnummer+beginwaarde)*eenheidx);
                        int x = (int)d;
                        d = bx+1.0*((puntXWaarde[j])*eenheidx/schaalFactorX);
						x = (int)d;
						double d0 = expressies[j].geefW((puntXWaarde[j]));
						int y = (int)Math.round(hoogte -(beginy+eenheidy*d0/schaalFactorY));
						g.setColor(Color.black);
						g.fillOval(x-2,y-2,5,5);
                        
                        g.setFont(font);
                        fm = g.getFontMetrics();
                        String xString = dfTrace.format(puntXWaarde[j]);
                        String yString = dfTrace.format(d0);
                        int woordBreedte = 40;
                        if(fm!=null) woordBreedte = fm.stringWidth(xString+yString);
                        g.setColor(new Color(255,255,225));
                        g.fillRect(x+6,y-7,woordBreedte+20,15);
                        g.setColor(Color.black);
                        g.drawString("(" + xString + " , " + yString + ")", x+8,y+5);
					}
							
				}
				if(expressies[j]!=null && trace || expressies[j]!=null && selectnummer<8 && selectnummer>-1)
				{	double d = bx+1.0*((selectnummer+beginwaarde)*eenheidx);
                    int x = (int)Math.round(d);
                    if(!tracing && selectnummer<8 && selectnummer>-1)
                    {   tracexD = d;
                        tracex = x;
                        slider.zetStand(tracex);
                    }
                    boolean b = varNaam.equals("qq") || varNaam.length()>2 && varNaam.substring(0,2).equals("qq");
            		
                    if ((isLijnGrafiek[j])&& trace && !isPuntGrafiek[j] || isMeerPuntenGrafiek[j])
                    {   slider.zetStand(tracex);
                        double dTraceX = schaalFactorX*(-beginx)/eenheidxD + schaalFactorX*tracexD/eenheidxD;
    					double dTraceY = expressies[j].geefW(dTraceX);
    					if(!b && !Double.isNaN(dTraceY) && tracex<veldb && tracex>-1)
    					{	int tracey = (int)Math.round(hoogte -(beginy+eenheidy*dTraceY/schaalFactorY));
                            g.setColor(traceKleur);
                            g.fillOval(tracex-2,tracey-2,5,5);
    						drawDottedLine(g,tracex,hoogte,tracex,tracey);
    						drawDottedLine(g,0,tracey,tracex,tracey);
    					
                            if ((isLijnGrafiek[j] || isMeerPuntenGrafiek[j])&& trace && !isPuntGrafiek[j])
                            {    
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
				}
			} // for
			

/*			
			for(int j=0 ; j<aantalPijlenIn ; j++)
			{
				if(expressies[j]!=null && expressies[j].geefVarNaam()!=null && varNaam.equals(expressies[j].geefVarNaam())&& !expressies[j].geefVarNaam().equals(""))
				{	g.setColor(Color.black);
					for(int i=0 ; i<breedte ; i++)
					{	double ii = i;
						boolean b0 = expressies[j].isWaarde(schaalFactorX*(-beginx)/eenheid + schaalFactorX*ii/eenheid);
						boolean b1 = expressies[j].isWaarde(schaalFactorX*(-beginx)/eenheid + schaalFactorX*(ii+1)/eenheid);
						if(b0 && b1)
						{	double d0 = expressies[j].geefW(schaalFactorX*(-beginx)/eenheid + schaalFactorX*ii/eenheid);//dd0.doubleValue();
							double d1 = expressies[j].geefW(schaalFactorX*(-beginx)/eenheid + schaalFactorX*(ii+1)/eenheid);//dd1.doubleValue();
							int x0 = i;
							int x1 = i+1;
							double dy0 = Math.round(hoogte -(beginy+eenheid*d0/schaalFactorY));
							double dy1 = Math.round(hoogte -(beginy+eenheid*d1/schaalFactorY));
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
					double d = bx+1.0*((selectnummer+beginwaarde)*eenheid);
					int x = (int)d;
					if(expressies[j].isWaarde((selectnummer+beginwaarde)*schaalFactorX) && selectnummer<8 && selectnummer>-1)
					{	double d0 = expressies[j].geefW((selectnummer+beginwaarde)*schaalFactorX);
						int y = (int)Math.round(hoogte -(beginy+eenheid*d0/schaalFactorY));
						g.fillOval(x-2,y-2,5,5);
						g.drawLine(x,y,x,hoogte);
						g.drawLine(x,y,0,y);
					}
				}
			}
*/			
		}
	}
}
