package fi.wiskopdr;

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.Hashtable;
import java.util.Vector;

import java.awt.geom.CubicCurve2D;
import java.awt.geom.GeneralPath;

import javax.swing.*;

import fi.wiskopdr.expressies.*;
import fi.wiskopdr.formuleobjects.*;


public class GrafiekComponent extends JPanel implements ActionListener, 
											 MouseListener, MouseMotionListener
{		
	private int eenheid = 16;
		
	private FormuleButton zoomInX, zoomUitX, zoomInY, zoomUitY, 
					 zoomIn, zoomUit, zoomStandaard;

	private Expressie[] expressies;
	private int aantalExpressies;
	private int maxAantalExpressies;
	
	private Expressie docentExpressie;
	private Vector docentPunten;
	
	private boolean gevuld;
	private int beginwaarde;
	private int selectnummer;
	private String varNaam;
	private String yAsLabel;
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
	RealPoint draggPoint = null;
	RealPoint otherPoint = null;	
	 
	private GrafiekVeld gv;
	private PuntenVeld pv;
	private GrafiekTekenEditor gte;
	private FunctieEditor fe;
	private TabelComponent tc;
	 
	private DecimalFormatSymbols dfs;
	private DecimalFormat df, dfTrace;
	private FontMetrics fm;
	
	private boolean[] isPuntGrafiek;
	private boolean[] isMeerPuntenGrafiek;
	private boolean[] isLijnGrafiek;
	private double[] puntXWaarde;
	
	private Font font = new Font("SansSerif", Font.PLAIN, 10);
	
	private boolean resize;
	
	private boolean assen = true;
	private boolean rooster = true;
	private boolean roosterGrof = false;
	private boolean schaal = true;
	
	private boolean piLijnen = false;
	
	private boolean traceOption = true;
	private boolean trace = true;
	private boolean tracing = false;
	
	private boolean drag = true;
	
	private boolean popupView = false;
		
	private Slider slider;
	private int tracex = -2;
	private double tracexD = tracex;
	private JCheckBox traceCheckbox;
	
	private int startx, starty;
	
	private JPanel headerPanel;
	private boolean newVersion = false;
	private boolean xPositief = false;
	private boolean yPositief = false;
	private boolean xVarEditable = false;
	private boolean yVarEditable = false;
	private boolean grafiekKleuren = true;
	
	Color piColor = Color.gray;//Color.magenta;
	
	private Color[] colors;
	
	private JTextField varNaamTF;
	private Rectangle varNaamActivator;
	private JTextField formuleNaamTF;
	private Rectangle formuleNaamActivator;
	
	private Color docentColor = Color.black;
	
	public GrafiekComponent(int x, int y, int b, int h)
	{	setBounds(x,y,b,h);
		setLayout(null);
		setBackground(Color.white);
		
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
		
		maxAantalExpressies = 10;
		expressies = new Expressie[maxAantalExpressies];
		aantalExpressies = 0;
	
		docentExpressie = null;
		
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
		
		if(newVersion)
		{	veldx = 0;
			veldy = 30;
			veldb = b;
			veldh = h-60;
		}
		else
		{	veldx = 40;
			veldy = 40;
			veldb = b-60;
			veldh = h-70;
		}
		
		schaalFactorY = 1;
		factorRijNummerY = 99;
		schaalFactorX = 1;
		factorRijNummerX = 99;
		
		varNaam = "x";
		yAsLabel = "y";
		formuleNaam = "";
		
		beginx = veldb/2/eenheidx*eenheidx;
		beginy = veldh/2/eenheidy*eenheidy;

		dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		df = new DecimalFormat("0.####", dfs);
		dfTrace = new DecimalFormat("0.##",dfs);
		
		gv = new GrafiekVeld(veldx,veldy,veldb,veldh);
		gv.addMouseListener(this);
		gv.addMouseMotionListener(this);
		add(gv);
		
		headerPanel = new JPanel(){
			public void paintComponent(Graphics g)
			{
				if("GR".equals(WiskOpdr.deployVariant)) ;
				//if("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant))super.paintComponent(g);
				else					
				for(int i=0 ; i<10 ; i++)
				{	g.setColor(new Color(200+5*i,200+5*i,200+5*i));
					g.fillRect(0,getHeight() - (i+1)*getHeight()/10, getWidth(),getHeight()/10+1);
				}
				
			}
		};
		if(newVersion)headerPanel.setBounds(0,0,b,23);
		else headerPanel.setBounds(veldx,0,veldb,23);
		headerPanel.setLayout(null);
		headerPanel.setBackground(new Color(210,210,210));
		if(!"GR".equals(WiskOpdr.deployVariant))headerPanel.setBorder(BorderFactory.createLineBorder(Color.lightGray));
		add(headerPanel);
		
		if("GR".equals(WiskOpdr.deployVariant))
		{	zoomStandaard	= new FormuleButton("gr_zoomstandaard",FormuleButton.NAVIGATIEKNOP);
			zoomStandaard.setBounds(0,0,23,23);
			zoomStandaard.addActionListener(this);
			headerPanel.add(zoomStandaard);
			
			zoomIn	= new FormuleButton("gr_zoomin",FormuleButton.NAVIGATIEKNOP);
			zoomIn.setBounds(26,0,23,23);
			zoomIn.addActionListener(this);
			headerPanel.add(zoomIn);
			
			zoomUit	= new FormuleButton("gr_zoomuit",FormuleButton.NAVIGATIEKNOP);
			zoomUit.setBounds(52,0,23,23);
			zoomUit.addActionListener(this);
			headerPanel.add(zoomUit);	
			
			zoomInX	= new FormuleButton("gr_zoominx",FormuleButton.NAVIGATIEKNOP);
			zoomInX.setBounds(78,0,23,23);
			zoomInX.addActionListener(this);
			headerPanel.add(zoomInX);
			
			zoomUitX= new FormuleButton("gr_zoomuitx",FormuleButton.NAVIGATIEKNOP);
			zoomUitX.setBounds(104,0,23,23);
			zoomUitX.addActionListener(this);
			headerPanel.add(zoomUitX);
			
			zoomInY	= new FormuleButton("gr_zoominy",FormuleButton.NAVIGATIEKNOP);
			zoomInY.setBounds(130,0,23,23);
			zoomInY.addActionListener(this);
			headerPanel.add(zoomInY);
			
			zoomUitY= new FormuleButton("gr_zoomuity",FormuleButton.NAVIGATIEKNOP);
			zoomUitY.setBounds(156,0,23,23);
			zoomUitY.addActionListener(this);
			headerPanel.add(zoomUitY);
		}
		else
		{	zoomStandaard	= new ZoomKnop("standaard");
			zoomStandaard.setBounds(10,2,20,20);
			zoomStandaard.addActionListener(this);
			headerPanel.add(zoomStandaard);
			
			zoomIn	= new ZoomKnop("zoomin");
			zoomIn.setBounds(35,2,20,20);
			zoomIn.addActionListener(this);
			headerPanel.add(zoomIn);
			
			zoomUit	= new ZoomKnop("zoomuit");
			zoomUit.setBounds(60,2,20,20);
			zoomUit.addActionListener(this);
			headerPanel.add(zoomUit);	
			
			zoomInX	= new ZoomKnop("zoominx");
			zoomInX.setBounds(85,2,20,20);
			zoomInX.addActionListener(this);
			headerPanel.add(zoomInX);
			
			zoomUitX= new ZoomKnop("zoomuitx");
			zoomUitX.setBounds(110,2,20,20);
			zoomUitX.addActionListener(this);
			headerPanel.add(zoomUitX);
			
			zoomInY	= new ZoomKnop("zoominy");
			zoomInY.setBounds(135,2,20,20);
			zoomInY.addActionListener(this);
			headerPanel.add(zoomInY);
			
			zoomUitY= new ZoomKnop("zoomuity");
			zoomUitY.setBounds(160,2,20,20);
			zoomUitY.addActionListener(this);
			headerPanel.add(zoomUitY);
		}
		
		slider = new Slider(veldb,0);
		slider.setLocation(veldx-5,h-(newVersion?18:13));
		slider.addActionListener(this);
		slider.setBackground(getBackground());
		slider.setVisible(trace);
		add(slider);
		
		traceCheckbox = new JCheckBox("trace");
		traceCheckbox.setFont(new Font("SansSerif",Font.PLAIN,10));
		traceCheckbox.setBounds(getWidth()-(newVersion?50:150),2,60,20);
		traceCheckbox.setBackground(Color.white);
		traceCheckbox.addActionListener(this);
		headerPanel.add(traceCheckbox,0);
		
		varNaamTF = new JTextField();
		varNaamTF.addActionListener(this);
		varNaamTF.setFont(new Font ("SansSerif",Font.ITALIC,10 ));
		varNaamTF.setSize(80,15);
		varNaamTF.setVisible(false);
		gv.add(varNaamTF);
		varNaamTF.addKeyListener(new KeyAdapter()
		{	public void keyReleased(KeyEvent e)
			{	String[] forbiddenStrings = {"sin","cos","tan","ln","log"};
				String text = varNaamTF.getText().trim();
				if(text.length()>0 && !Character.isLetter(text.charAt(0)))
				{	JOptionPane.showMessageDialog(WiskOpdr.applet, WiskOpdr.rb.getString("xVarMessage1"));
					update(text.substring(1),true);
				}
				else if(text.length()>1 && !FormuleParser.isWoordFormule())
				{	JOptionPane.showMessageDialog(WiskOpdr.applet, WiskOpdr.rb.getString("xVarMessage2"));
					update(text.substring(0,1),true);
				}
				else if(text.length()==1 && text.charAt(0)=='e')
				{	JOptionPane.showMessageDialog(WiskOpdr.applet, WiskOpdr.rb.getString("xVarMessage3"));
					if(FormuleParser.isWoordFormule())update("x", false);
					else update(text.substring(1),true);
				}
				else if(text.length()!=0)
				{	boolean forbidden = false;
					for(int i=0 ; i<forbiddenStrings.length ; i++)
					{	if(text.indexOf(forbiddenStrings[i])>-1)
						{	JOptionPane.showMessageDialog(WiskOpdr.applet, WiskOpdr.rb.getString("xVarMessage4a") + forbiddenStrings[i] + WiskOpdr.rb.getString("xVarMessage4b"));
							if(text.length()>0)update(text.substring(0,text.indexOf(forbiddenStrings[i])),true);
							forbidden = true;
						}
					}
					if(!forbidden)update(text,true);
				}
				if(text.length()==0) update("x", false);
			}
			public void update(String s, boolean updateTF)
			{	varNaam = s;
				if(updateTF) varNaamTF.setText(varNaam);
				if(varNaam.equals(""))varNaam = "x";
				zetVarNaam(varNaam);
				if(fe!=null)fe.zetVarNaam(varNaam);
				varNaamTF.requestFocus();
			}
		});
		
		formuleNaamTF = new JTextField();
		formuleNaamTF.addActionListener(this);
		formuleNaamTF.setFont(new Font ("SansSerif",Font.ITALIC,10 ));
		formuleNaamTF.setSize(80,15);
		formuleNaamTF.setVisible(false);
		gv.add(formuleNaamTF);
		formuleNaamTF.addKeyListener(new KeyAdapter()
		{	public void keyReleased(KeyEvent e)
			{	yAsLabel = formuleNaamTF.getText().trim();
				formuleNaamTF.setText(yAsLabel);
				if(yAsLabel.equals(""))yAsLabel = "y";
				zetYAsLabel(yAsLabel);
				if(fe!=null)fe.zetYAsLabel(yAsLabel);
				formuleNaamTF.requestFocus();
			}
		});
		
		varNaamActivator = new Rectangle();
		formuleNaamActivator = new Rectangle();
	}
	
	public double geefSchaalX()
	{	return 1.0*eenheid/schaalFactorX;
	}
	
	public double geefSchaalY()
	{	return 1.0*eenheid/schaalFactorY;
	}
	
	public void zetDocentColor(Color c)
	{
		docentColor = c;
		repaint();
	}
	
	public void setColor(int nr, Color c)
	{
		colors[nr] = c;
		repaint();
	}
	
	public GrafiekTekenEditor getGrafiekTekenEditor()
	{	return gte;
	}
	
	public void zetGrafiekTekenEditor(GrafiekTekenEditor gte)
	{	this.gte = gte;
		// er is nog geen puntenVeld, maak het
		if (pv == null)	
		{	pv = new PuntenVeld(veldx,veldy,veldb,veldh);
			add(pv,0);
			pv.addMouseListener(this);
			pv.addMouseMotionListener(this);
		}
		else // er is al een puntenVeld (van de tabelComponent of iets anders)
		{	pv.addMouseListener(this);
			pv.addMouseMotionListener(this);
	
		}
		repaint();
	}	
	
	public void removeGrafiekTekenEditor()
	{	gte = null;
		if ((tc == null) && (pv != null))
		{	remove(pv);	
			pv = null;
		}
		// als tc!=null dan moet er al een puntenveld zijn
		else if ((tc != null) && (pv != null))
		{	pv.removeMouseListener(this);
			
		}	
		docentPunten = null;	
		repaint();		
	}
	
	public void zetTabelComponent(TabelComponent tc)
	{	this.tc = tc;
		// er is nog geen puntenveld
		if (pv == null)	
		{	pv = new PuntenVeld(veldx,veldy,veldb,veldh);
			add(pv,0);
		}
		repaint();
	}	
	
	public void removeTabelComponent()
	{	tc = null;
		if ((gte == null) && (pv != null))
		{	remove(pv);	
			pv = null;
		}	
		else if ((gte != null) && (pv != null))
		{	// nothing to do
		}
		// tabelPunten = null
		repaint();	
		
	}
	
	public void zetAssen(boolean b)
	{	assen = b;
		repaint();
	}
	
	public void zetRooster(boolean b)
	{	rooster = b;
		repaint();
	}
	
	public void zetRoosterGrof(boolean b)
	{	roosterGrof = b;
		repaint();
	}
	
	public void zetGrafiekKleuren(boolean b)
	{	grafiekKleuren = b;
		repaint();
	}

	public void zetSchaal(boolean b)
	{	schaal = b;
		repaint();
	}
	
	public void zetPiLijnen(boolean b)
	{	piLijnen = b;
		repaint();
	}
	
	public void setTrace(boolean b)
	{
		traceOption = b;
		trace = b;
		if (!traceOption)
		{	trace = false;
			slider.setVisible(trace);
		}
		slider.setVisible(traceOption);		
		if(newVersion) veldh=getHeight()-45;
		traceCheckbox.setSelected(false);
		traceCheckbox.setVisible(traceOption);
		
		repaint();
	}
	
	public void setDrag(boolean b)
	{	drag = b;
	}
	
	public void setPopupView(boolean b)
	{	popupView = b;
	}

	
	public void zetVarNaam(String s)
	{	varNaam = s;
		repaint();
	}
	
	public void zetYAsLabel(String s)
	{	yAsLabel = s;
		repaint();
	}
	
	public void zetNewVersion(boolean b)
	{	newVersion = b;
		if(newVersion)
		{	veldx = 0;
			veldy = 30;
			veldb = getWidth();
			veldh = getHeight()-60;
		}
		else
		{	veldx = 40;
			veldy = 40;
			veldb = getWidth()-60;
			veldh = getHeight()-70;
		}
		setBounds(getBounds());
	}
	
	public void zetXPositief(boolean b)
	{	xPositief = b;
	}
	
	public void zetYPositief(boolean b)
	{	yPositief = b;
	}
	
	public void zetXVarEditable(boolean b)
	{	xVarEditable = b;
	}
	public void zetYVarEditable(boolean b)
	{	yVarEditable = b;
	}
	
	public void setBackground(Color c)
	{	if (slider!=null)
			slider.setBackground(c);
		super.setBackground(c);
	}
	
	public void setBounds(int x, int y, int b, int h)
	{	super.setBounds(x,y,b,h);
		if(newVersion)
		{	veldb = b;
			veldh = h-(traceOption?45:35);
		}
		else
		{	veldb = b-60;
			veldh = h-75;
		}
		if(gv!=null)gv.setBounds(veldx,veldy,veldb,veldh);
		if(pv!=null)pv.setBounds(veldx,veldy,veldb,veldh);
		if(headerPanel!=null)
		{	if(newVersion)headerPanel.setBounds(0,0,b,23);
			else headerPanel.setBounds(veldx,0,veldb,23);
		}
		if(slider!=null) 
		{	if(newVersion)
			{	slider.zetLengte(veldb-10);
				slider.setLocation(veldx,h-13);
			}
			else
			{	slider.zetLengte(veldb);
				slider.setLocation(veldx-5,h-13);
			}
		}
		if(traceCheckbox!=null) 
		{	traceCheckbox.setBounds(getWidth()-(newVersion?50:110),2,60,20);
			traceCheckbox.setOpaque(false);
		}
		resize=true;
	}
	public void setSize(int b, int h)
	{	
		super.setSize(b,h);
		if(newVersion)
		{	veldb = b;
			veldh = h-(traceOption?45:35);
		}
		else
		{	veldb = b-60;
			veldh = h-75;
		}
		
		gv.setSize(veldb,veldh);

		if(pv!=null) 
			pv.setSize(veldb,veldh);
		if(headerPanel!=null)
		{	if(newVersion)headerPanel.setBounds(0,0,b,23);
			else headerPanel.setBounds(veldx,0,veldb,23);
		}
		if(newVersion)
		{	slider.zetLengte(veldb-10);
			slider.setLocation(veldx,h-13);
		}
		else
		{	slider.zetLengte(veldb);
			slider.setLocation(veldx-5,h-13);
		}
		traceCheckbox.setBounds(getWidth()-(newVersion?50:110),2,60,10);
		traceCheckbox.setOpaque(false);
		resize=true;
	}
	
	public Hashtable getState()
	{	double beginx = veldb/2/eenheidx*eenheidx;
		double beginy = veldh/2/eenheidy*eenheidy;
		double schaalFactorX  = 0;
		double schaalFactorY  = 0;
		boolean trace = false;
						
		beginx = this.beginx;
		beginy = this.beginy;
		schaalFactorX = this.schaalFactorX;
		schaalFactorY = this.schaalFactorY;
		trace = this.trace;
		
		Hashtable h = new Hashtable();
	    h.put("beginx", new Double(beginx));
	    h.put("beginy", new Double(beginy));
	    h.put("schaalFactorX", new Double(schaalFactorX));
	    h.put("schaalFactorY", new Double(schaalFactorY));
	    h.put("trace", new Boolean(trace));

	    return h;
	}

    public void setState(Hashtable h)
    {	double beginx = veldb/2/eenheidx*eenheidx;
		double beginy = veldh/2/eenheidy*eenheidy;
		double schaalFactorX = 1;
	    double schaalFactorY = 1;
	    boolean trace = false;
	    
    	if(h.containsKey("beginx")) 
    		beginx = ((Double)h.get("beginx")).doubleValue();
    	if(h.containsKey("beginy")) 
    		beginy = ((Double)h.get("beginy")).doubleValue();
    	if(h.containsKey("schaalFactorX")) 
    		schaalFactorX = ((Double)h.get("schaalFactorX")).doubleValue();
    	if(h.containsKey("schaalFactorY")) 
    		schaalFactorY = ((Double)h.get("schaalFactorY")).doubleValue();
    	if(h.containsKey("trace")) 
    		trace = ((Boolean)h.get("trace")).booleanValue();
    				
		this.beginx = beginx;
		this.beginy = beginy;
		this.schaalFactorX = schaalFactorX;
		this.schaalFactorY = schaalFactorY;
		this.trace = trace;
		
		traceCheckbox.setSelected(trace);
		slider.setVisible(trace);
		
		int b = beginwaarde;
		beginwaarde = 1-(int)Math.round(beginx/eenheidx);
		selectnummer = selectnummer + b - beginwaarde;
		
    }
    
	public void paintComponent(Graphics g)
	{	g.setFont(font);
		
		int breedte = getSize().width;
		int hoogte = getSize().height;
		g.setColor(getBackground());
		g.fillRect(0,0,breedte,hoogte);

		if(drag)
		{	g.setColor(Color.white);
			g.fillRect(veldx-1,veldy-1,veldb+1,veldh+1);
			g.setColor(Color.gray);
			if(!newVersion)g.drawRect(veldx-1,veldy-1,veldb+1,veldh+1);
		}
		else
		{
			g.setColor(getBackground());
			g.fillRect(veldx-1,veldy-1,veldb+1,veldh+1);
		}
		
		FontMetrics fm = g.getFontMetrics();
		
		if(!newVersion)
		{	g.setColor(Color.black);
			g.setFont(new Font(font.getName(), Font.ITALIC, font.getSize()));
			int woordbreedte = fm.stringWidth(varNaam);
			g.drawString(varNaam, veldx+veldb+5-woordbreedte,veldy+veldh+20);

			g.drawString(yAsLabel, veldx, veldy - 5);
			g.drawString(formuleNaam,veldx-5,veldy+18);
		}
		
		g.setFont(font);
		
		if (!newVersion && schaal)
		{
		
			int imin = -(int)Math.round(beginx/eenheidx); 
			int imax = 1+veldb/eenheidx-(int)Math.round(beginx/eenheidx);
			int jmin = -(int)Math.round(beginy/eenheidy); 
			int jmax = 1+veldh/eenheidy-(int)Math.round(beginy/eenheidy);
			//int bx = (int)beginx;
			//int by = (int)beginy;
		
			for(int i=imin+1 ; i<imax; i++)
			{	new Expressie();
				String getal = df.format(schaalFactorX*(i));
				int woordbreedte = fm.stringWidth(getal);
				if(i%2==0)	g.drawString(getal,	(int)(veldx+beginx+i*eenheidxD-woordbreedte/2),	veldy+veldh+11);
			}
			for(int j=jmin+1 ; j<jmax; j++)
			{	String getal = df.format(schaalFactorY*(j));
				int woordbreedte = fm.stringWidth(getal);
				if(j%2==0)g.drawString(getal,	veldx-5-woordbreedte,(int)(veldy+veldh+5-(beginy+j*eenheidyD)));
			}
		
		} // if (schaal)
				
		//if(traceOption)
			//g.drawString("trace",28,hoogte-14);
		//super.paint(g);	
	}	
		
	public void zetBegin(int x, int y)
	{	beginx = eenheidx*x;
		beginy = eenheidy*y;
	}

	public void zetExpressie(int nr,Expressie e)
	{	/*Expressie exp = null;
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
		}*/
		expressies[nr] = e;
		repaint();
		
		produceAction("ingevuld");
		
	}
	
	public void zetDocentExpressie(Expressie docentExp)
	{	docentExpressie = docentExp;
		repaint();
	}	


//???	
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
			repaint();
		}
	}

	public Point realPointToPixels(RealPoint rp)
	{	if (Double.isNaN(rp.x) || Double.isNaN(rp.y))
			return null;
		
		Point pix = new Point();
		pix.x =	(int) Math.round(beginx + eenheidxD * rp.x / schaalFactorX);
		pix.y = (int) Math.round(pv.getSize().height - 
							     (beginy + eenheidyD * rp.y / schaalFactorY));
		return pix;
	}

	public RealPoint realPointToRealPixels(RealPoint rp)
	{	RealPoint realPix = new RealPoint(
			beginx + eenheidxD * rp.x / schaalFactorX,
			pv.getSize().height - (beginy + eenheidyD * rp.y / schaalFactorY));
		return realPix;
	}
	
	public RealPoint pixelsToRealPoint(Point pix)
	{	RealPoint rp = new RealPoint(0, 0);
		rp.x = schaalFactorX * (-beginx) / eenheidxD +
			   schaalFactorX * pix.x / eenheidxD;
		rp.y = (schaalFactorY * (-beginy) / eenheidyD +
			    schaalFactorY * (pv.getSize().height - pix.y) / eenheidyD); 	   
		return rp;
	}
	
	public int closestFreePixX(int pressedX)
	{	
		Vector points = gte.getPoints(gte.getActiveIndex());

		// check pressedX en zoek naar links	
		boolean found = false;	
		int firstFreeXLeft = - 1;				
		for (int lCnt = pressedX; lCnt >= 0; lCnt--)
		{	// nog geen gevonden
			if (!found)
			{	found = true;
				// ga door de punten heen	
				for (int pCnt = 0; pCnt < points.size(); pCnt++)
				{	RealPoint rp = (RealPoint) points.elementAt(pCnt);
					Point rpPix = realPointToPixels(rp);
					found = found && (rpPix.x != lCnt);
				}
				if (found)
					firstFreeXLeft = lCnt;
			}	
		}
		// klaar!
		if (firstFreeXLeft == pressedX)
			return firstFreeXLeft;
			
		// zoek nu rechts
		found = false;	
		int firstFreeXRight = - 1;				
		for (int rCnt = pressedX + 1; rCnt <= veldb; rCnt++)
		{	// nog geen gevonden
			if (!found)
			{	found = true;
				// ga door de punten heen	
				for (int pCnt = 0; pCnt < points.size(); pCnt++)
				{	RealPoint rp = (RealPoint) points.elementAt(pCnt);
					Point rpPix = realPointToPixels(rp);
					found = found && (rpPix.x != rCnt);
				}
				if (found)
					firstFreeXRight = rCnt;
			}	
		}
			
		if ((firstFreeXLeft == -1) && (firstFreeXRight == -1))
			return -1;
		else if ((firstFreeXLeft == -1) && (firstFreeXRight >= 0))		
			return firstFreeXRight;
		else if ((firstFreeXLeft >= 0) && (firstFreeXRight == -1))			
			return firstFreeXLeft;
		else if ((firstFreeXLeft >= 0) && (firstFreeXRight >= 0))
		{	if ((pressedX - firstFreeXLeft) < (firstFreeXRight - pressedX))
				return firstFreeXLeft;
			else
				return firstFreeXRight;	
		}				
		else 
			return -1;
	}

	
	public void mousePressed(MouseEvent e)
	{	requestFocus();

//if (gte == null)
//System.out.println("gte null");
//else
//System.out.println("gte not null");

		// slepen op grafiekVeld
		if ((e.getSource() == gv) //||
			// het puntenveld is er WEL maar maar er is geen
			// grafiekTekenEditor; dan moet er dus een tabelComponent
			// zijn, maar het puntenVeld heeft dan geen listeners
			// dus luistert het grafiekVeld
		    //((gte == null) && (pv != null) && (e.getSource() == pv))
		   )
		{	
//System.out.println("mp grafiekVeld");			
			startxv = e.getX();
			startyv = e.getY();
			varNaamTF.setVisible(xVarEditable && gv.activateVarNaam(e.getX(), e.getY()));
			formuleNaamTF.setVisible(yVarEditable && gv.activateFormuleNaam(e.getX(), e.getY()));
//System.out.println("gv press");			
		}
		// klikken met de grafiekTekenEditor aan
		else if ((gte != null) && (pv != null) && e.getSource() == pv)
		{	
//System.out.println("mp puntenVeld");			
			
			int pressedX = e.getX();
			int pressedY = e.getY();
			
			if (gte.getCursorMode() == gte.NOCUR)
			{	startxv = e.getX();
				startyv = e.getY();
			}
			else if (gte.getCursorMode() == gte.DRAW)
			{	
				// geklikt met rechter muisknop
				// dit is gummen
				if ((e.getModifiers() & e.BUTTON1_MASK) == 0)
            	{	// kijk of er op een punt van de aktuele grafiek 
					// is geklikt
					RealPoint drp = null;
					Vector points = gte.getPoints(gte.getActiveIndex());
					{	for (int pCnt = 0; pCnt < points.size(); pCnt++)
						{	RealPoint rp = (RealPoint) points.elementAt(pCnt);
							Point rpPix = realPointToPixels(rp);
							int dis = (int) Math.round(
								Math.sqrt((rpPix.x - pressedX) * (rpPix.x - pressedX) +
										  (rpPix.y - pressedY) * (rpPix.y - pressedY)));
							if (dis <= gte.PRAD+2)
							{	drp = rp;
							}
						
						}
					}
					if (drp != null)
					{	gte.removePoint(drp, true);
						repaint();
					}

            	}
            	// geklikt met een andere muisknop
            	else	
            	{
            		
					// kijk of er op een point van de aktuele grafiek is geklikt
					// dat gaan we dan slepen
					draggPoint = null;
					Vector points = gte.getPoints(gte.getActiveIndex());
					{	for (int pCnt = 0; pCnt < points.size(); pCnt++)
						{	RealPoint rp = (RealPoint) points.elementAt(pCnt);
							Point rpPix = realPointToPixels(rp);
							int dis = (int) Math.round(
								Math.sqrt((rpPix.x - pressedX) * (rpPix.x - pressedX) +
										  (rpPix.y - pressedY) * (rpPix.y - pressedY)));
							if (dis <= gte.PRAD+2)
							{	draggPoint = rp;
							}
							
						}
					}
					// draggPoint slepen
					if (draggPoint != null)
					{	startxv = e.getX();
						startyv = e.getY();
					}	
           			else // tekenen
           			{
//System.out.println("tekenen");
						int freePixX = closestFreePixX(pressedX);						
//System.out.println("pressed = " + pressedX);
//System.out.println("firstFree = " + freePixX);						

						RealPoint newPoint = pixelsToRealPoint(
							new Point(freePixX, pressedY));
						newPoint.index = gte.getActiveIndex();
						gte.addInsert(newPoint, true);
						repaint();

					} // tekenen
					
				} // niet rechts geklikt
			} // DRAW
			else if (gte.getCursorMode() == gte.DELETE)
			{	
				// kijk of er op een punt van de aktuele grafiek 
				// is geklikt
				RealPoint drp = null;
				Vector points = gte.getPoints(gte.getActiveIndex());
				{	for (int pCnt = 0; pCnt < points.size(); pCnt++)
					{	RealPoint rp = (RealPoint) points.elementAt(pCnt);
						Point rpPix = realPointToPixels(rp);
						int dis = (int) Math.round(
							Math.sqrt((rpPix.x - pressedX) * (rpPix.x - pressedX) +
									  (rpPix.y - pressedY) * (rpPix.y - pressedY)));
						if (dis <= gte.PRAD+2)
						{	drp = rp;
						}
						
					}
				}
				if (drp != null)
				{	gte.removePoint(drp, true);
					repaint();
				}
			}
			else if (gte.getCursorMode() == gte.DRAGG)
			{	// kijk of er op een punt van de aktuele grafiek 
				// is geklikt
				draggPoint = null;
				otherPoint = null;
				Vector points = gte.getPoints(gte.getActiveIndex());
				{	for (int pCnt = 0; pCnt < points.size(); pCnt++)
					{	RealPoint rp = (RealPoint) points.elementAt(pCnt);
						Point rpPix = realPointToPixels(rp);
						int dis = (int) Math.round(
							Math.sqrt((rpPix.x - pressedX) * (rpPix.x - pressedX) +
									  (rpPix.y - pressedY) * (rpPix.y - pressedY)));
						if (dis <= gte.PRAD+2)
						{	draggPoint = rp;
						}
						
					}
				}
				// als niet, kijk of er op een punt van een andere
				// grafiek geklikt is
				if (draggPoint == null)
				{	for (int index = 1; index <= gte.getNumGraphs(); index++)
					{	if (index != gte.getActiveIndex())
						{	Vector indexPoints = gte.getPoints(index);
							for (int pCnt = 0; pCnt < indexPoints.size(); pCnt++)
							{	RealPoint rp = (RealPoint) indexPoints.elementAt(pCnt);
								Point rpPix = realPointToPixels(rp);
								int dis = (int) Math.round(
									Math.sqrt((rpPix.x - pressedX) * (rpPix.x - pressedX) +
											  (rpPix.y - pressedY) * (rpPix.y - pressedY)));
								if (dis <= gte.PRAD+2)
								{	otherPoint = rp;
								}
						
							}
						}
					}
				}
				
				startxv = e.getX();
				startyv = e.getY();
			}
//System.out.println("pv press");						
		}
		else if(e.getX()>getSize().width-10 && e.getY()>getSize().height-10)
		{	resize = true;
			startx = e.getX();
			starty = e.getY();
		}
		
	}	
	
	public void mouseDragged(MouseEvent e)
	{	if ((gte != null) && (pv != null) && (e.getSource() == pv))
		{	if (gte.getCursorMode() == gte.NOCUR)
			{	
				if (!drag)
					return;
					
				int dx = e.getX() - startxv;
				int dy = e.getY() - startyv;
				beginx = beginx+dx;
				beginy = beginy-dy;
				if(trace && tracex!=-2) 
				{	tracexD = tracexD+dx;
					tracex = tracex+dx;
					slider.zetStand(tracex);
				}
		
				int b = beginwaarde;
				beginwaarde = 1-(int)Math.round(beginx/eenheidx);
				selectnummer = selectnummer + b - beginwaarde;
				repaint();
				startxv = e.getX();
				startyv = e.getY();
			}
			else if (gte.getCursorMode() == gte.DRAW)
			{	// punt slepen
				if (draggPoint != null)
				{	// schermpositie voor dragg-event
					Point pix = realPointToPixels(draggPoint);
					int dx = e.getX() - startxv;
					int dy = e.getY() - startyv;	
					// schermpositie na dragg-event
					Point dPix = new Point(pix.x + dx, pix.y + dy);
					RealPoint temp = pixelsToRealPoint(dPix);
					temp.index = draggPoint.index;
					if (gte.hasPointWithSameXAs(temp))
					{	draggPoint.x = temp.x + 2 * RealPoint.NZERO;										
//System.out.println("shifted");					
					}
					else
					{	draggPoint.x = temp.x;										
					}	
					draggPoint.y = temp.y;
					gte.removePoint(draggPoint, false);
					gte.addInsert(draggPoint, false);
					
					repaint();
					
					startxv = e.getX();
					startyv = e.getY();
				}	
			}
			else if (gte.getCursorMode() == gte.DRAGG)
			{
				// punt slepen
				if (draggPoint != null)
				{	// schermpositie voor dragg-event
					Point pix = realPointToPixels(draggPoint);
					int dx = e.getX() - startxv;
					int dy = e.getY() - startyv;	
					// schermpositie na dragg-event
					Point dPix = new Point(pix.x + dx, pix.y + dy);
					RealPoint temp = pixelsToRealPoint(dPix);
					temp.index = draggPoint.index;
					if (gte.hasPointWithSameXAs(temp))
					{	draggPoint.x = temp.x + 2 * RealPoint.NZERO;										
//System.out.println("shifted");					
					}
					else
					{	draggPoint.x = temp.x;										
					}	
					draggPoint.y = temp.y;
					gte.removePoint(draggPoint, false);
					gte.addInsert(draggPoint, false);					
					repaint();
					
					startxv = e.getX();
					startyv = e.getY();
					
				}
				// grafiek slepen als drag==true
				else if ((draggPoint == null) && (otherPoint == null) && drag)
				{	int dx = e.getX() - startxv;
					int dy = e.getY() - startyv;
//System.out.println("gv dragg");					
					beginx = beginx+dx;
					beginy = beginy-dy;
					if(trace && tracex!=-2) 
					{	tracexD = tracexD+dx;
						tracex = tracex+dx;
						slider.zetStand(tracex);
					}
			
					int b = beginwaarde;
					beginwaarde = 1-(int)Math.round(beginx/eenheidx);
					selectnummer = selectnummer + b - beginwaarde;
					repaint();
					startxv = e.getX();
					startyv = e.getY();
			
				}
			}
		}
		else if(!drag)
			return;
		else if (
				 (e.getSource() == gv) // ||
				 // het puntenveld is er WEL maar maar er is geen
				 // grafiekTekenEditor; dan moet er dus een tabelComponent
				 // zijn, maar dan luistert het puntenVeld niet
		    	 // ((gte == null) && (pv != null) && (e.getSource() == pv))
		   		)
		{	int dx = e.getX() - startxv;
			int dy =  e.getY() - startyv;
//System.out.println("gv dragg");					
			beginx = beginx+dx;
			beginy = beginy-dy;
			if(trace && tracex!=-2) 
			{	tracexD = tracexD+dx;
				tracex = tracex+dx;
				slider.zetStand(tracex);
			}
			
			int b = beginwaarde;
			beginwaarde = 1-(int)Math.round(beginx/eenheidx);
			selectnummer = selectnummer + b - beginwaarde;
			repaint();
			startxv = e.getX();
			startyv = e.getY();
			
		}
		else if(resize)
		{	int dx = e.getX() - startx;
			int dy = e.getY() - starty;
			//setSize(getSize().width + dx, getSize().height + dy);
			repaint();
			//schuifveld.tekenOpnieuw();
			startx = e.getX();
			starty = e.getY();
			
		}
		else
		{	int dx = e.getX() - startx;
			int dy = e.getY() - starty;
			
		}
	}
	
	public void mouseReleased(MouseEvent e)
	{	resize = false;
		//trace = false;
		if (
			(e.getSource() == gv) //||
			// het puntenveld is er WEL maar maar er is geen
			// grafiekTekenEditor; dan moet er dus een tabelComponent
			// zijn, maar dan luistert het puntenVeld niet
		    //((gte == null) && (pv != null) && (e.getSource() == pv))
		   ) 
		{	double beginxR = beginx;
			beginx = eenheidx*Math.round(beginx/eenheidx);
			beginy = eenheidy*Math.round(beginy/eenheidy);
			if(trace && tracex!=-2) 
			{	tracexD += beginx-beginxR;
				tracex += beginx-beginxR;
				slider.zetStand(tracex);
			}
			
			repaint();
		}
		else if ((gte != null) && (pv != null) && (e.getSource() == pv))
		{	if (gte.getCursorMode() == gte.NOCUR)
			{	double beginxR = beginx;
				beginx = eenheidx*Math.round(beginx/eenheidx);
				beginy = eenheidy*Math.round(beginy/eenheidy);
				if(trace && tracex!=-2) 
				{	tracexD += beginx-beginxR;
					tracex += beginx-beginxR;
					slider.zetStand(tracex);
				}
			
				repaint();
			}
			else if (gte.getCursorMode() == gte.DRAW)
			{	if (draggPoint != null)
				{	
				
					// corrigeer draggPoint				
					gte.removePoint(draggPoint, false);
					Point draggPix = realPointToPixels(draggPoint);
					int freePixX = closestFreePixX(draggPix.x);
					RealPoint temp = pixelsToRealPoint(
						new Point(freePixX, draggPix.y));
					draggPoint.x = temp.x;
					draggPoint.y = temp.y;								
					gte.addInsert(draggPoint, true);
				
					draggPoint = null;
					
					repaint();
				}
			}
			else if (gte.getCursorMode() == gte.DRAGG)
			{	if (draggPoint != null)
				{	
					// corrigeer draggPoint								
					
					gte.removePoint(draggPoint, false);
					Point draggPix = realPointToPixels(draggPoint);
					int freePixX = closestFreePixX(draggPix.x);
					RealPoint temp = pixelsToRealPoint(
						new Point(freePixX, draggPix.y));
					draggPoint.x = temp.x;
					draggPoint.y = temp.y;	
					gte.addInsert(draggPoint, true);							
					
					draggPoint = null;
					
					repaint();
				}
				// grafiek slepen
				else if ((draggPoint == null) && (otherPoint == null) && drag)
				{	double beginxR = beginx;
					beginx = eenheidx*Math.round(beginx/eenheidx);
					beginy = eenheidy*Math.round(beginy/eenheidy);
					if(trace && tracex!=-2) 
					{	tracexD += beginx-beginxR;
						tracex += beginx-beginxR;
						slider.zetStand(tracex);
					}
			
					repaint();
				}
			}
		}
		varNaamTF.setVisible(xVarEditable && gv.activateVarNaam(e.getX(), e.getY()));
		if(varNaamTF.isVisible())varNaamTF.requestFocus();
		formuleNaamTF.setVisible(yVarEditable && gv.activateFormuleNaam(e.getX(), e.getY()));
		if(formuleNaamTF.isVisible())formuleNaamTF.requestFocus();
		
	}
	
	public void mouseMoved(MouseEvent e)
	{	if ((gte != null) && (pv != null) && (e.getSource() == pv))
		{	if (gte.getCursorMode() == gte.DRAW)
			{	// kijk of de cursor op een punt van de aktuele grafiek 
				// staat
				int movedX = e.getX();
				int movedY = e.getY();				
				RealPoint drp = null;
				Vector points = gte.getPoints(gte.getActiveIndex());
				{	for (int pCnt = 0; pCnt < points.size(); pCnt++)
					{	RealPoint rp = (RealPoint) points.elementAt(pCnt);
						Point rpPix = realPointToPixels(rp);
						int dis = (int) Math.round(
							Math.sqrt((rpPix.x - movedX) * (rpPix.x - movedX) +
									  (rpPix.y - movedY) * (rpPix.y - movedY)));
						if (dis <= gte.PRAD+2)
						{	drp = rp;
						}
						
					}
				}
				if (drp != null)
				{	setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					repaint();
				}
				else
				{	boolean error = false;
					Cursor drawCursor = null;
					try
					{	
						drawCursor = Toolkit.getDefaultToolkit().
							createCustomCursor(gte.getImage("tekencursor.gif"),
								new Point(10, 10), "TEKEN_CURSOR");
					}
					catch (IndexOutOfBoundsException ioobe)
					//catch (HeadlessException he)
					{	error = true;
					}
					if (!error)
					{	setCursor(drawCursor);
					}
				}
			}
		}
	}
	
	public void mouseExited(MouseEvent e)
	{	if ((gte != null) && (pv != null) && (e.getSource() == pv))
		{	setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}
	public void mouseClicked(MouseEvent e){;}
	public void mouseEntered(MouseEvent e)
	{	if ((gte != null) && (pv != null) && (e.getSource() == pv))
		{	if (gte.getCursorMode() == gte.DRAW)
			{	boolean error = false;
				Cursor drawCursor = null;
				try
				{	
					drawCursor = Toolkit.getDefaultToolkit().
						createCustomCursor(gte.getImage("tekencursor.gif"),
							new Point(10, 10), "TEKEN_CURSOR");
				}
				catch (IndexOutOfBoundsException ioobe)
				//catch (HeadlessException he)
				{	error = true;
				}
				if (!error)
				{	setCursor(drawCursor);
				}
			}
			else if (gte.getCursorMode() == gte.DELETE)
			{	boolean error = false;
				Cursor deleteCursor = null;
				try
				{	
					deleteCursor = Toolkit.getDefaultToolkit().
						createCustomCursor(gte.getImage("gumcursor.gif"),
							new Point(10, 10), "GUM_CURSOR");
				}
				catch (IndexOutOfBoundsException ioobe)
				//catch (HeadlessException he)
				{	error = true;
				}
				if (!error)
				{	setCursor(deleteCursor);
				}
			}
			else if (gte.getCursorMode() == gte.DRAGG)
			{	setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
			
		}
	}	
	
	
	public void actionPerformed(ActionEvent e)
	{	if(zoomDraad!=null && zoomDraad.isAlive())return;
		
		if(e.getActionCommand().equals("focus")) ;
		else 
		{	if(e.getSource()==zoomUitY && factorRijNummerY<120)
			{	zoomDraad = new ZoomDraad(false,true,false);
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
			{	beginx = veldb/2/eenheidx*eenheidx;
				beginy = veldh/2/eenheidy*eenheidy;
				double beginxVorig = beginx;
				//beginx = eenheidx;
				//beginy = eenheidy;
				tracexD = beginx -(beginxVorig - tracexD)*schaalFactorX;
				factorRijNummerX = 99;
				factorRijNummerY = 99;
				schaalFactorX = 1;
				schaalFactorY = 1;
				beginwaarde = 0;
				selectnummer = 999;
				
				tracex = (int) Math.round(tracexD);
				//tracex=-2;
				slider.zetStand(tracex);
				repaint();
				
				
			}
			
		}
		if(e.getSource()==slider)
		{ 	if(e.getActionCommand().equals("start")) 
			{	tracing = true;
				//((AlgebraSchuifVeld)getParent()).zetTabellen(beginwaarde, 999, varNaam, schaalFactorX);
			}
			//else if(e.getActionCommand().equals("stop")) tracing = false;
			tracex = slider.geefStand();
			tracexD = tracex;
			repaint();
			//schuifveld.tekenOpnieuw();
			
		}
		if(e.getSource()==traceCheckbox)
		{	trace = traceCheckbox.isSelected();
			slider.setVisible(trace);
			repaint();
			//schuifveld.tekenOpnieuw();
		}
		if(e.getSource()==varNaamTF)
		{	varNaamTF.setVisible(false);
			
		}
		if(e.getSource()==formuleNaamTF)
		{	formuleNaamTF.setVisible(false);
		}
	}

	//ActionProducer
	private ActionListener actionListener = null;
	
	public void addActionListener(ActionListener l) 
 	{	actionListener = AWTEventMulticaster.add(actionListener,l);
 	}
 	
 	public void removeActionListener(ActionListener l)
 	{	actionListener = AWTEventMulticaster.remove(actionListener, l);
 	}	
 	
 	public void produceAction(String command)
 	{	if (actionListener != null)
 		{	actionListener.actionPerformed( new ActionEvent(this, 0, command) );
 		}
 	}
 	//end ActionProducer
 		
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
			
			double middenx = veldb/2/eenheidx*eenheidx;
			double middeny = veldh/2/eenheidy*eenheidy;
			
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
				
				repaint();
				
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
				repaint();
				//schuifveld.tekenOpnieuw();
			}
			
			
			
			beginwaarde = 1-(int)Math.round(beginx/eenheidx);
			double beginwaardeD = 1.0-(beginx/eenheidx);
			
			
			tracexD = tracexD + eenheid*(beginwaardeD - beginwaarde);
			tracex = (int) Math.round(tracexD);
			slider.zetStand(tracex);
			
			if(x)selectnummer = 999;
            
			
			repaint();
			//schuifveld.tekenOpnieuw();
			
		}
		public void maakDood()
		{	dood = true;
		}
	}
	
	
	class GrafiekVeld extends JComponent
	{
		private Image im;
  		private Graphics gIm;
		private boolean veranderd;
	
		public GrafiekVeld(int x, int y, int b, int h)
		{	
			super.setBounds(x,y,b,h);
			veranderd = true;
		}
		
		/*public void paint(Graphics g)
		{	tekenFunctie(g);
			
		}*/
		
		public boolean activateVarNaam(int x, int y)
		{	return varNaamActivator.contains(x,y);
		}
		
		public boolean activateFormuleNaam(int x, int y)
		{	return formuleNaamActivator.contains(x,y);
		}
		
		public void	paintComponent(Graphics gr)
		{	
			Graphics2D g = (Graphics2D) gr;
			
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_NORMALIZE);
			//g.setStroke(new BasicStroke(0.5f));
			//Graphics g;
	        //{     g = (Graphics2D)gr;
	        //      ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        //}
	        //((Graphics2D)g).setStroke(new BasicStroke(0.7f));
			
			int breedte = getSize().width;
			int hoogte = getSize().height;
			//g.setClip(0,0,breedte,hoogte);
			
			int bx = (int)Math.round(beginx);			
			int by = (int)Math.round(beginy);
			
			int maxWoordBreedteY = 0;
			int maxWoordHoogteX = 10;
			boolean witruimteX = false;
			boolean witruimteY = false;
			
			fm = g.getFontMetrics();
			
			if (rooster)
			{	int imin = -(int)Math.round(beginx/eenheidx); 
				int imax = 1+breedte/eenheidx-(int)Math.round(beginx/eenheidx);
				int jmin = -(int)Math.round(beginy/eenheidy); 
				int jmax = 1+hoogte/eenheidy-(int)Math.round(beginy/eenheidy);
				
				
				for(int j=jmin+1 ; j<jmax-1 ; j++)
				{	String getal = df.format(schaalFactorY*(j));
					int woordbreedte = fm.stringWidth(getal);
					if(j!=0 && j%2==0 && (!yPositief || j>0))
					{	maxWoordBreedteY = Math.max(maxWoordBreedteY, woordbreedte);
					}
				}
				for(int i=imin+1 ; i<imax ; i++)
				{	g.setColor(new Color(210,210,210));
					if((!xPositief || i>0) && i%(roosterGrof?2:1)==0)  g.drawLine((int)(bx+i*eenheidxD),0,(int)(bx+i*eenheidxD),hoogte - (yPositief?by:0));
				}
				
				//int by = (int)Math.round(beginy);
				for(int j=jmin ; j<jmax ; j++)
				{	g.setColor(new Color(210,210,210));
					if((!yPositief || j>0) && j%(roosterGrof?2:1)==0)g.drawLine(xPositief?bx:0,(int)(hoogte-(by+j*eenheidyD)),breedte,(int)(hoogte-(by+j*eenheidyD)));
				}	
			}
			
			if (schaal && newVersion)
			{
				
				
				g.setFont(font);
				
				int imin = -(int)Math.round(beginx/eenheidx); 
				int imax = 1+veldb/eenheidx-(int)Math.round(beginx/eenheidx);
				int jmin = -(int)Math.round(beginy/eenheidy); 
				int jmax = 1+veldh/eenheidy-(int)Math.round(beginy/eenheidy);
				//int bx = (int)beginx;
				//int by = (int)beginy;
			
				
				boolean gedaanX = false;
				for(int i=imin+1 ; i<imax-1 ; i++)
				{	new Expressie();
					String getal = df.format(schaalFactorX*(i));
					int woordbreedte = fm.stringWidth(getal);
					int xLabel = (int)(beginx+i*eenheidxD-woordbreedte/2);
					int yLabel = Math.min(hoogte-1, hoogte-by+11);
					witruimteX = yLabel==hoogte-1;
					if(i!=0 && i%2==0 && (!xPositief || i>0))
					{	g.setColor(Color.white);
						if(witruimteX && !gedaanX)g.fillRect(0, yLabel-9, breedte, 11);
						else g.fillRect(xLabel-1, yLabel-9, woordbreedte+2, 11);
						g.setColor(Color.black);
						g.drawString(getal,	xLabel,	yLabel);
						gedaanX = true;
					}
					
					
				}
				boolean gedaanY = false;
				for(int j=jmin+1 ; j<jmax-1 ; j++)
				{	String getal = df.format(schaalFactorY*(j));
					int woordbreedte = fm.stringWidth(getal);
					int xLabel = Math.max(maxWoordBreedteY-woordbreedte,bx-2-woordbreedte);
					int yLabel = (int)(veldh+5-(beginy+j*eenheidyD));
					witruimteY = xLabel==maxWoordBreedteY-woordbreedte;
					if(j!=0 && j%2==0 && (!yPositief || j>0))
					{	g.setColor(Color.white);
						if(witruimteY && !gedaanY)g.fillRect(xLabel-1+woordbreedte-maxWoordBreedteY, 0, maxWoordBreedteY+2, hoogte);
						else g.fillRect(xLabel-1, yLabel-9, woordbreedte+2, 11);
						g.setColor(Color.black);
						g.drawString(getal,	xLabel,yLabel);
						gedaanY = true;
					}
				}
				
				/*g.setColor(Color.black);
				if(bx>1 && bx<breedte)
				{	g.drawLine(bx-1,0,bx-1,hoogte);	
					g.drawLine(bx,0,bx,hoogte);
				}
				if(by>0 && by<hoogte)
				{	g.drawLine(0,hoogte-(by+1),breedte,hoogte-(by+1));
					g.drawLine(0,hoogte-(by),breedte,hoogte-(by));
				}
				g.drawString("O",bx-10,hoogte-by+12);*/
			
			}

			if (piLijnen)
			{	
			
				int dashStep = hoogte / 50;
				int dashes = 60;			
				
				g.setColor(piColor);
				// 0 is in beeld
				if ((bx > 0) && (bx < breedte))
				{	int maxLCnt = (int) Math.round(beginx / Math.PI);
					for (int lCnt = 0; lCnt < maxLCnt; lCnt++)
					{	int piX = (int) Math.round(beginx -
							lCnt * Math.PI * eenheidxD / schaalFactorX);
						if ((piX > 0) && (piX < breedte))
						{	for (int dCnt = 0; dCnt < dashes; dCnt++)
							{	if ((dCnt % 2) == 0)
									if(rooster)g.drawLine(piX, dCnt * dashStep, 
											   piX, dCnt * dashStep + dashStep);
							}	
							g.setColor(Color.black);
							if(lCnt==0);
							else if(lCnt==1) g.drawString("-"+"\u03C0",piX-3,hoogte-by+(newVersion?20:12));
							else g.drawString("-"+lCnt+"\u03C0",piX-3,hoogte-by+(newVersion?20:12));
							g.drawLine(piX,hoogte-by-2,piX,hoogte-by+2);
							g.setColor(piColor);
						}
					}
					int maxRCnt = (int) Math.round((breedte - beginx) / Math.PI);
					for (int rCnt = 0; rCnt < maxRCnt; rCnt++)
					{	int piX = (int) Math.round(beginx + 
							rCnt * Math.PI * eenheidxD / schaalFactorX);
						if ((piX > 0) && (piX < breedte))
						{	for (int dCnt = 0; dCnt < dashes; dCnt++)
							{	if ((dCnt % 2) == 0)
									if(rooster)g.drawLine(piX, dCnt * dashStep, 
											   piX, dCnt * dashStep + dashStep);
							}	
							g.setColor(Color.black);
							if(rCnt==0);
							else if(rCnt==1) g.drawString(""+"\u03C0",piX-3,hoogte-by+(newVersion?20:12));
							else g.drawString(""+rCnt+"\u03C0",piX-3,hoogte-by+(newVersion?20:12));
							g.drawLine(piX,hoogte-by-2,piX,hoogte-by+2);
							g.setColor(piColor);
						}
					}
					
				}	
				// 0 is links
				else if (bx <= 0)
				{	int maxRCnt = (int) Math.round((breedte - beginx) / Math.PI);
					for (int rCnt = 0; rCnt < maxRCnt; rCnt++)
					{	int piX = (int) Math.round(beginx +
							rCnt * Math.PI * eenheidxD / schaalFactorX);
						if ((piX > 0) && (piX < breedte))
						{	for (int dCnt = 0; dCnt < dashes; dCnt++)
							{	if ((dCnt % 2) == 0)
									if(rooster)g.drawLine(piX, dCnt * dashStep, 
											   piX, dCnt * dashStep + dashStep);
							}	
							g.setColor(Color.black);
							if(rCnt==0);
							else if(rCnt==1) g.drawString(""+"\u03C0",piX-3,hoogte-by+12);
							else g.drawString(""+rCnt+"\u03C0",piX-3,hoogte-by+12);
							g.drawLine(piX,hoogte-by-2,piX,hoogte-by+2);
							g.setColor(piColor);
						}
					}
				}		
				// 0 is rechts
				else if (bx >= breedte)
				{	int maxLCnt = (int) Math.round(beginx / Math.PI);
					for (int lCnt = 0; lCnt < maxLCnt; lCnt++)
					{	int piX = (int) Math.round(beginx - 
							lCnt * Math.PI * eenheidxD / schaalFactorX);
						if ((piX > 0) && (piX < breedte))
						{	for (int dCnt = 0; dCnt < dashes; dCnt++)
							{	if ((dCnt % 2) == 0)
									if(rooster)g.drawLine(piX, dCnt * dashStep, 
											   piX, dCnt * dashStep + dashStep);
							}
							g.setColor(Color.black);
							if(lCnt==0);
							else if(lCnt==1) g.drawString("-"+"\u03C0",piX-3,hoogte-by+12);
							else g.drawString("-"+lCnt+"\u03C0",piX-3,hoogte-by+12);
							g.drawLine(piX,hoogte-by-2,piX,hoogte-by+2);
							g.setColor(piColor);
						}
					}
				}		
				
			}
			
			if (assen)
			{
				g.setColor(Color.black);
				if(bx>1 && bx<breedte)
				{	g.drawLine(bx-1,0,bx-1,hoogte-Math.max(witruimteX?maxWoordHoogteX:0,(yPositief?by:0)));
					g.drawLine(bx,0,bx,hoogte-Math.max(witruimteX?maxWoordHoogteX:0,(yPositief?by:0)));
				}
				if(by>0 && by<hoogte)
				{	g.drawLine(Math.max(witruimteY?maxWoordBreedteY:0, xPositief?bx:0),hoogte-(by+1),breedte,hoogte-(by+1));
					g.drawLine(Math.max(witruimteY?maxWoordBreedteY:0, xPositief?bx:0),hoogte-(by),breedte,hoogte-(by));
				}
				if(newVersion)
				{	g.setFont(new Font ("SansSerif",Font.ITALIC,10 ));
					g.drawString("O",bx-11,hoogte-by+10);
					
					g.setColor(Color.black);
					g.setFont(new Font(font.getName(), Font.ITALIC, font.getSize()));
					FontMetrics fm = g.getFontMetrics();
					int woordbreedte = fm.stringWidth(varNaam);
					int formuleWoordbreedte = fm.stringWidth(yAsLabel);
					g.drawString(varNaam, breedte-woordbreedte-5,Math.min(hoogte-17, hoogte-(by)-5));
					
					g.drawString(yAsLabel, Math.max(18,bx+6), 9);
					g.drawString(formuleNaam, Math.max(18,bx+6), 9);
					
					varNaamActivator.setBounds(breedte-woordbreedte-5, Math.min(hoogte-17, hoogte-(by)-15), woordbreedte, 15);
					formuleNaamActivator.setBounds(Math.max(18,bx+6), 0, formuleWoordbreedte, 15);
					
					varNaamTF.setLocation(breedte-85,Math.min(hoogte-17, hoogte-(by)-15));
					formuleNaamTF.setLocation(Math.max(18,bx+6),0);
				}
			}	
			
			if (docentExpressie != null)
			{	/*g.setColor(Color.black);
				for(int i=0 ; i<breedte ; i++)
				{	double ii = i;
					double d0 = 
						(docentExpressie.substitueer(
							schaalFactorX*(-beginx)/eenheidxD + 
							schaalFactorX*ii/eenheidxD, varNaam)).geefWaarde();
					double d1 = 
						(docentExpressie.substitueer(
								schaalFactorX*(-beginx)/eenheidxD + 
								schaalFactorX*(ii+1)/eenheidxD, varNaam)).geefWaarde();
					if(!Double.isNaN(d0) && !Double.isNaN(d1))
					{	int x0 = i;
						int x1 = i+1;
						double dy0 = 
							Math.round(hoogte -(beginy+eenheidyD*d0/schaalFactorY));
						double dy1 = 
							Math.round(hoogte -(beginy+eenheidyD*d1/schaalFactorY));
						if(dy0>1000)dy0 = 1000;
						if(dy0<-1000)dy0 = -1000;
						if(dy1>1000)dy1 = 1000;
						if(dy1<-1000)dy1 = -1000;
						int y0 = (int)dy0;
						int y1 = (int)dy1;
						g.drawLine(x0,y0,x1,y1);
					}
				} //for(int i=0 ; i<breedte ; i++)*/
				g.setColor(docentColor);
				GeneralPath curve = new GeneralPath();
				for(int i=Math.max(witruimteY?maxWoordBreedteY:0, xPositief?bx:0) ; i<breedte ; i++)
				{	double ii = i;
					double d0 = (docentExpressie.substitueer(	schaalFactorX*(-beginx)/eenheidxD + schaalFactorX*ii/eenheidxD, varNaam)).geefWaarde();//dd0.doubleValue();
					double d1 = (docentExpressie.substitueer(	schaalFactorX*(-beginx)/eenheidxD + schaalFactorX*(ii+1)/eenheidxD, varNaam)).geefWaarde();//dd1.doubleValue();					if(b0 && b1)
					if(!Double.isNaN(d0) && !Double.isNaN(d1))
					{	int x0 = i;
						int x1 = i+1;
						double dy0 = hoogte -(beginy+eenheidyD*d0/schaalFactorY);
						double dy1 = hoogte -(beginy+eenheidyD*d1/schaalFactorY);
						if(dy0>1000)dy0 = 1000;
						if(dy0<-1000)dy0 = -1000;
						if(dy1>1000)dy1 = 1000;
						if(dy1<-1000)dy1 = -1000;
						int y0 = (int)dy0;
						int y1 = (int)dy1;
						
						if(curve.getCurrentPoint()==null)curve.moveTo((float)x0, (float)dy0);
						if(!yPositief || d1>0) curve.lineTo((float)x1, (float)dy1); 
					}
					else if(curve.getCurrentPoint()!=null)
					{	g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_PURE);
						g.setStroke(new BasicStroke(1.2f));
						g.draw(curve);
						curve = new GeneralPath();
					}
				}
				g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_PURE);
				g.setStroke(new BasicStroke(1.2f));
				g.draw(curve);
			}
			
			for(int j=0 ; j<expressies.length ; j++)
			{	if(expressies[j]!=null)
				{	g.setColor(Color.black);
					GeneralPath curve = new GeneralPath();
					for(int i=Math.max(witruimteY?maxWoordBreedteY:0, xPositief?bx:0) ; i<breedte ; i++)
					{	double ii = i;
						double d0 = (expressies[j].substitueer(	schaalFactorX*(-beginx)/eenheidxD + schaalFactorX*ii/eenheidxD, varNaam)).geefWaarde();//dd0.doubleValue();
						double d1 = (expressies[j].substitueer(	schaalFactorX*(-beginx)/eenheidxD + schaalFactorX*(ii+1)/eenheidxD, varNaam)).geefWaarde();//dd1.doubleValue();					if(b0 && b1)
						if(!Double.isNaN(d0) && !Double.isNaN(d1))
						{	int x0 = i;
							int x1 = i+1;
							double dy0 = hoogte -(beginy+eenheidyD*d0/schaalFactorY);
							double dy1 = hoogte -(beginy+eenheidyD*d1/schaalFactorY);
							if(dy0>1000)dy0 = 1000;
							if(dy0<-1000)dy0 = -1000;
							if(dy1>1000)dy1 = 1000;
							if(dy1<-1000)dy1 = -1000;
							int y0 = (int)dy0;
							int y1 = (int)dy1;
							
							if(curve.getCurrentPoint()==null)curve.moveTo((float)x0, (float)dy0);
							if(!yPositief || d1>0) curve.lineTo((float)x1, (float)dy1); 
						}
						if(Double.isNaN(d1) || yPositief && d1<0)
						{	g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_PURE);
							if(grafiekKleuren)g.setColor(colors[j]);
							else g.setColor(colors[0]);
							g.setStroke(new BasicStroke(1.2f));
							if(curve.getCurrentPoint()!=null)g.draw(curve);
							curve = new GeneralPath();
						}
					}
					g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_PURE);
					if(grafiekKleuren)g.setColor(colors[j]);
					else g.setColor(colors[0]);
					g.setStroke(new BasicStroke(1.2f));
					g.draw(curve);
					
					g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_NORMALIZE);
					g.setColor(new Color(100,100,100));
					double d = bx+1.0*((selectnummer+beginwaarde)*eenheidx);
					int x = (int)Math.round(d);
					double d0 = expressies[j].geefWaarde((selectnummer+beginwaarde)*schaalFactorX);
					
					if(!tracing && !Double.isNaN(d0) && selectnummer<8 && selectnummer>-1)
					{	int y = (int)Math.round(hoogte -(beginy+eenheidy*d0/schaalFactorY));
						g.fillOval(x-2,y-2,5,5);
						g.setStroke(new BasicStroke(1.0f));
						g.drawLine(x,y,x,Math.min(hoogte-by, hoogte));
						g.drawLine(x,y,Math.max(0,bx),y);
						g.setStroke(new BasicStroke(1.0f));
						tracexD = d;
						tracex = x;
						slider.zetStand(tracex);
						
						double dTraceX = schaalFactorX*(-beginx)/eenheidxD + schaalFactorX*tracexD/eenheidxD;
						double dTraceY = expressies[j].geefWaarde(dTraceX);
						int tracey = (int)Math.round(hoogte -(beginy+eenheidy*dTraceY/schaalFactorY));
						
						String xWaarde = dfTrace.format(dTraceX);
						String yWaarde = dfTrace.format(dTraceY);
						g.setFont(font);
						fm = g.getFontMetrics();
						int woordBreedteX = fm.stringWidth(xWaarde);
						int woordHoogteX = fm.getAscent();
						int woordBreedteY = fm.stringWidth(yWaarde);
						int woordHoogteY = fm.getAscent();
						g.setColor(new Color(255,255,200));
						g.fillRect(tracex-woordBreedteX/2-2, Math.min(hoogte-by, hoogte-woordHoogteX-2), woordBreedteX+4, woordHoogteX+2);
						g.fillRect(Math.max(0,bx-woordBreedteY), tracey-woordHoogteY/2-2, woordBreedteY+4, woordHoogteY+4);
						g.setColor(Color.black);
						g.drawRect(tracex-woordBreedteX/2-2, Math.min(hoogte-by, hoogte-woordHoogteX-2), woordBreedteX+4, woordHoogteX+2);
						g.drawRect(Math.max(0,bx-woordHoogteY), tracey-woordHoogteY/2-2, woordBreedteY+4, woordHoogteY+4);
						g.drawString(xWaarde, tracex-woordBreedteX/2, Math.min(hoogte-by+woordHoogteX, hoogte-2));
						g.drawString(yWaarde, Math.max(2,bx-woordBreedteY+2), tracey+woordHoogteY/2);
					}
					else if(trace)
					{	double dTraceX = schaalFactorX*(-beginx)/eenheidxD + schaalFactorX*tracexD/eenheidxD;
						//double dTraceY = expressies[j].geefWaarde(dTraceX);
						double dTraceY = (expressies[j].substitueer(dTraceX, varNaam)).geefWaarde();
						if(!Double.isNaN(dTraceY) && tracex<veldb && tracex>-1 && (!xPositief || tracex>bx))
						{	int tracey = (int)Math.round(hoogte -(beginy+eenheidy*dTraceY/schaalFactorY));
							g.fillOval(tracex-2,tracey-2,5,5);
							g.drawLine(tracex,tracey,tracex,Math.min(hoogte-by, hoogte));
							g.drawLine(tracex,tracey,Math.max(bx, 0),tracey);
						
							String xWaarde = dfTrace.format(dTraceX);
							String yWaarde = dfTrace.format(dTraceY);
							g.setFont(font);
							fm = g.getFontMetrics();
							int woordBreedteX = fm.stringWidth(xWaarde);
							int woordHoogteX = fm.getAscent();
							int woordBreedteY = fm.stringWidth(yWaarde);
							int woordHoogteY = fm.getAscent();
							g.setColor(new Color(255,255,200));
							g.fillRect(tracex-woordBreedteX/2-2, Math.min(hoogte-by, hoogte-woordHoogteX-2), woordBreedteX+4, woordHoogteX+2);
							g.fillRect(Math.max(0,bx-woordBreedteY-5), tracey-woordHoogteY/2-2, woordBreedteY+4, woordHoogteY+4);
							g.setColor(Color.black);
							g.drawRect(tracex-woordBreedteX/2-2, Math.min(hoogte-by, hoogte-woordHoogteX-2), woordBreedteX+4, woordHoogteX+2);
							g.drawRect(Math.max(0,bx-woordBreedteY-5), tracey-woordHoogteY/2-2, woordBreedteY+4, woordHoogteY+4);
							g.drawString(xWaarde, tracex-woordBreedteX/2, Math.min(hoogte-by+woordHoogteX, hoogte-2));
							g.drawString(yWaarde, Math.max(2,bx-woordBreedteY-3), tracey+woordHoogteY/2);
						}
					}
				}
				else if(isPuntGrafiek[j] && expressies[j]!=null && 
						expressies[j].geefVarNaam()==null)
				{	double d = bx+1.0*((puntXWaarde[j])*eenheidx/schaalFactorX);
					int x = (int)d;
					double d0 = expressies[j].geefWaarde((puntXWaarde[j])*schaalFactorX);
					int y = (int)Math.round(hoogte -(beginy+eenheidy*d0/schaalFactorY));
					g.setColor(Color.black);
					g.fillOval(x-2,y-2,5,5);
					
							
				}
				if(isMeerPuntenGrafiek[j] && expressies[j]!=null)
				{	g.setColor(Color.black);
					for (int k = 0; k<8; k++) 
					{	double d = bx+1.0*((k+beginwaarde)*eenheidx);
						int x = (int)d;
						if(expressies[j].isWaarde((k+beginwaarde)*schaalFactorX) && k<8 )
						{	double d0 = expressies[j].geefWaarde((k+beginwaarde)*schaalFactorX);
							int y = (int)Math.round(hoogte -(beginy+eenheidy*d0/schaalFactorY));
							g.fillOval(x-2,y-2,5,5);
						}
				    }
				    
					
					if(isPuntGrafiek[j])
					{	double d = bx+1.0*((selectnummer+beginwaarde)*eenheidx);
                        int x = (int)d;
                        d = bx+1.0*((puntXWaarde[j])*eenheidx/schaalFactorX);
						x = (int)d;
						double d0 = expressies[j].geefWaarde((puntXWaarde[j]));
						int y = (int)Math.round(hoogte -(beginy+eenheidy*d0/schaalFactorY));
						g.setColor(Color.black);
						g.fillOval(x-2,y-2,5,5);
					}
							
				}
				if(expressies[j]!=null && trace || selectnummer<8 && selectnummer>-1)
				{	double d = bx+1.0*((selectnummer+beginwaarde)*eenheidx);
                    int x = (int)Math.round(d);
                    if(!tracing && selectnummer<8 && selectnummer>-1)
                    {   tracexD = d;
                        tracex = x;
                        slider.zetStand(tracex);
                    }
                    if ((isLijnGrafiek[j]&& trace && !isPuntGrafiek[j] || isMeerPuntenGrafiek[j]))
                    {   slider.zetStand(tracex);
                        double dTraceX = 
                        	schaalFactorX*(-beginx)/eenheidxD + 
                        	schaalFactorX*tracexD/eenheidxD;
    					double dTraceY = expressies[j].geefWaarde(dTraceX);
    					if(!Double.isNaN(dTraceY) && tracex<veldb && tracex>-1)
    					{	int tracey = (int)Math.round(hoogte -(beginy+eenheidy*dTraceY/schaalFactorY));
                            g.setColor(new Color(255,0,0));
                            g.fillOval(tracex-2,tracey-2,5,5);
    						g.drawLine(tracex,tracey,tracex,hoogte);
    						g.drawLine(tracex,tracey,0,tracey);
    					
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
			} //for(int j=0 ; j<expressies.length ; j++)
			
				
		}// paintComponent
	} // class GrafiekVeld
	
	class PuntenVeld extends JComponent
	{	
	
		public PuntenVeld(int x, int y, int b, int h)
		{	
			super.setBounds(x,y,b,h);
		}

		public void paintComponent(Graphics g)
		{	// kijk of er een tabelComponent is
			if (tc != null)
			{	
				
//System.out.println("tc != null");				
				// teken eerst de punten van de niet aktieve tabellen	
				for (int index = 1; index <= tc.getNumTables(); index++)
				{	if (index != tc.getActiveIndex())
					{	Vector indexPoints = tc.getPoints(index);
//System.out.println("iPoints = " + indexPoints.size());					
						g.setColor(tc.getColor(index));
						for (int pCnt = 0; pCnt < indexPoints.size(); pCnt++)
						{	RealPoint rp = (RealPoint) indexPoints.elementAt(pCnt);
							Point pix = realPointToPixels(rp);
							if (pix != null)
								g.fillOval(pix.x - gte.PRAD, pix.y - gte.PRAD,
										   2 * tc.PRAD+1 , 2 * tc.PRAD+1);
						}
					}
				}
				// dan de punten aktieve tabel
				int aIndex = tc.getActiveIndex();
//System.out.println("aIndex = " + aIndex);				
				Vector aIndexPoints = tc.getPoints(aIndex);
//System.out.println("aPoints = " + aIndexPoints.size());				
				g.setColor(tc.getColor(aIndex));			
				for (int pCnt = 0; pCnt < aIndexPoints.size(); pCnt++)
				{	RealPoint rp = (RealPoint) aIndexPoints.elementAt(pCnt);
					Point pix = realPointToPixels(rp);
					if (pix != null)
						g.fillOval(pix.x - tc.PRAD - 2, pix.y - tc.PRAD - 2,
								   2 * tc.PRAD + 3, 2 * tc.PRAD + 3);
//System.out.println("" + pCnt + " = " + pix.x + "," + pix.y);						   
				}			
						
			}
		
		
			if (gte == null)
				return;
			// eerst de punten en verbindingen van de niet aktieve grafieken	
			for (int index = 1; index <= gte.getNumGraphs(); index++)
			{	if (index != gte.getActiveIndex())
				{	Vector indexPoints = gte.getPoints(index);
					g.setColor(gte.getColor(index));
					for (int pCnt = 0; pCnt < indexPoints.size(); pCnt++)
					{	RealPoint rp = (RealPoint) indexPoints.elementAt(pCnt);
						Point pix = realPointToPixels(rp);
						g.fillOval(pix.x - gte.PRAD, pix.y - gte.PRAD,
								   2 * gte.PRAD +1, 2 * gte.PRAD +1);
					}
					// verbinden met lijnen
					if ((gte.getConnectMode() == gte.LINES) &&
					    (indexPoints.size() > 1))
					{	RealPoint rp0 = (RealPoint) indexPoints.elementAt(0);
						Point pix0 = realPointToPixels(rp0);
						RealPoint rp1 = null;
						Point pix1 = null;
						for (int pCnt = 1; pCnt < indexPoints.size(); pCnt++)
						{	rp1 = (RealPoint) indexPoints.elementAt(pCnt);
							pix1 = realPointToPixels(rp1);
							g.drawLine(pix0.x, pix0.y, pix1.x, pix1.y);
							rp0 = rp1;
							pix0 = pix1;
						}
					}
					
					if ((gte.getConnectMode() == gte.CURVE) &&
					    (indexPoints.size() == 2))
					{	RealPoint rp0 = (RealPoint) indexPoints.elementAt(0);
						RealPoint rp1 = (RealPoint) indexPoints.elementAt(1);
						Point pix0 = realPointToPixels(rp0);
						Point pix1 = realPointToPixels(rp1);
						g.drawLine(pix0.x, pix0.y, pix1.x, pix1.y);
					}    
					
					if ((gte.getConnectMode() == gte.CURVE) &&
					    (indexPoints.size() > 2))
					{	Graphics2D g2D = (Graphics2D) g;
					
						g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
						// het punt voor het startpunt p0, if any
						RealPoint p00 = null;
						// startpunt p0
						RealPoint rp0 = (RealPoint) indexPoints.elementAt(0);
						RealPoint p0 = realPointToRealPixels(rp0);
						// eindpunt p1
						RealPoint rp1 = null;
						RealPoint p1 = null;
						// het punt na het eindpunt p1, if any
						RealPoint rp11 = null;
						RealPoint p11 = null;
				
						double intervalFrac = 3;				
				
						for (int pCnt = 1; pCnt < indexPoints.size(); pCnt++)
						{	// vindt eindpunt
							rp1 = (RealPoint) indexPoints.elementAt(pCnt);
							p1 = realPointToRealPixels(rp1);
							// kijk of p0-p1 in pixels vertikaal is, teken lijn
					
							if (Math.abs(p0.x - p1.x) < RealPoint.NZERO)
//							if (((int) Math.round(p0.x)) == ((int) Math.round(p1.x)))
							{	Point pix0 = realPointToPixels(rp0);
								Point pix1 = realPointToPixels(rp1);
								g.drawLine(pix0.x, pix0.y, pix1.x, pix1.y);
//System.out.println("vert");						

							}
							else
							{	// vindt het punt na p1, if any
								rp11 = null;
								p11 = null;
								if (pCnt < (indexPoints.size() - 1))
								{	rp11 = (RealPoint) indexPoints.elementAt(pCnt + 1);
									p11 = realPointToRealPixels(rp11);
								}
								// vindt nu de controle-punten
								// tussen p0 en p1
								// controlepunt 0
								RealPoint c0 = null;
								// p0 is het eerste punt, p1 is het tweede punt
								if (p00 == null)
								{	// vector p0 -> p1
									RealPoint slp0p1 = new RealPoint(
										p1.x - p0.x, p1.y - p0.y);
									RealPoint unitSlope0 =	slp0p1.standarize();
									double xLength = (p1.x - p0.x) / intervalFrac;
									double newLength = xLength / unitSlope0.x;
									RealPoint dir0 = new RealPoint(
										unitSlope0.x * newLength,
										unitSlope0.y * newLength);
									c0 = new RealPoint(p0.x + dir0.x, p0.y + dir0.y);		
								}
								else
								{	// vector p00 -> p0
									RealPoint slp00p0 = new RealPoint(
										p0.x - p00.x, p0.y - p00.y);
									// vector p0 -> p1
									RealPoint slp0p1 = new RealPoint(
										p1.x - p0.x, p1.y - p0.y);	
// eerst middelen, dan standariseren of omgekeerd?																
									RealPoint meanSlope0 = new RealPoint(
										(slp00p0.x + slp0p1.x) / 2,
										(slp00p0.y + slp0p1.y) / 2);	
									RealPoint unitSlope0 =	meanSlope0.standarize();	
									double xLength = //Math.max(
										//(p0.x - p00.x) / intervalFrac,
										(p1.x - p0.x) / intervalFrac
										//)
										;
									double newLength = xLength / unitSlope0.x;
									RealPoint dir0 = new RealPoint(
										unitSlope0.x * newLength,
										unitSlope0.y * newLength);
									c0 = new RealPoint(p0.x + dir0.x, p0.y + dir0.y);		
								}

// testing						
/*
						g.setColor(Color.orange);
						Point c0Pix = new Point(
							(int) Math.round(c0.x), (int) Math.round(c0.y));
						g.fillOval(c0Pix.x - gte.PRAD, c0Pix.y - gte.PRAD,
								   2 * gte.PRAD + 1, 2 * gte.PRAD + 1);						
//System.out.println("" + pCnt + " = c0 : " + c0Pix.x + "," + c0Pix.y); 												
*/
								// controlepunt 1
								RealPoint c1 = null;
								// p1 is het laatste punt
								if (p11 == null)
								{	// vector p0 -> p1
									RealPoint slp0p1 = new RealPoint(
										p1.x - p0.x, p1.y - p0.y);	
									RealPoint unitSlope1 =	slp0p1.standarize();	
									double xLength = (p1.x - p0.x) / intervalFrac;
									double newLength = xLength / unitSlope1.x;
									RealPoint dir1 = new RealPoint(
										- unitSlope1.x * newLength,
										- unitSlope1.y * newLength);
									c1 = new RealPoint(p1.x + dir1.x, p1.y + dir1.y);		
							
								}
								else
								{	// vector p0 -> p1
									RealPoint slp0p1 = new RealPoint(
										p1.x - p0.x, p1.y - p0.y);	
									// vector p1 -> p11	
									RealPoint slp1p11 = new RealPoint(
										p11.x - p1.x, p11.y - p1.y);	
// eerst middelen, dan standariseren of omgekeerd?																
									RealPoint meanSlope1 = new RealPoint(
										(slp0p1.x + slp1p11.x) / 2,
										(slp0p1.y + slp1p11.y) / 2);
									RealPoint unitSlope1 =	meanSlope1.standarize();	
									double xLength = //Math.max(
										(p1.x - p0.x) / intervalFrac//,
										//(p11.x - p1.x) / intervalFrac
										//)
										;
									double newLength = xLength / unitSlope1.x;
									RealPoint dir1 = new RealPoint(
										- unitSlope1.x * newLength,
										- unitSlope1.y * newLength);
									c1 = new RealPoint(p1.x + dir1.x, p1.y + dir1.y);		
							
								}
						
// testing						
/*
						g.setColor(Color.pink);
						Point c1Pix = new Point(
							(int) Math.round(c1.x), (int) Math.round(c1.y));
						g.fillOval(c1Pix.x - gte.PRAD, c1Pix.y - gte.PRAD,
								   2 * gte.PRAD + 1, 2 * gte.PRAD + 1);						
//System.out.println("" + pCnt + " = c1 : " + c1Pix.x + "," + c1Pix.y); 												
*/						
								//g.setColor(gte.getColor(aIndex));									
								CubicCurve2D bezier = new CubicCurve2D.Double();
								bezier.setCurve(p0.x, p0.y, c0.x, c0.y,
											    c1.x, c1.y, p1.x, p1.y);
								g2D.draw(bezier);			    
		
						
							} // else niet vertikaal
							p00 = p0;
							p0 = p1;
						} // for

					}    
					
				}
			}	
			// dan de punten en verbindingen van de aktieve grafiek
			int aIndex = gte.getActiveIndex();
			Vector aIndexPoints = gte.getPoints(aIndex);
			g.setColor(gte.getColor(aIndex));			
			for (int pCnt = 0; pCnt < aIndexPoints.size(); pCnt++)
			{	RealPoint rp = (RealPoint) aIndexPoints.elementAt(pCnt);
				Point pix = realPointToPixels(rp);
				if (gte.getCursorMode() == gte.NOCUR)
					g.fillOval(pix.x - gte.PRAD, pix.y - gte.PRAD,
						   2 * gte.PRAD+1, 2 * gte.PRAD+1);
				else
					g.fillOval(pix.x - gte.PRAD - 2, pix.y - gte.PRAD - 2,
						   2 * gte.PRAD + 3, 2 * gte.PRAD + 3);
//System.out.println("" + pCnt + " = " + pix.x + "," + pix.y);						   
			}			
			if ((gte.getConnectMode() == gte.LINES) &&
			    (aIndexPoints.size() > 1))
			{	RealPoint rp0 = (RealPoint) aIndexPoints.elementAt(0);
				Point pix0 = realPointToPixels(rp0);
				RealPoint rp1 = null;
				Point pix1 = null;
				for (int pCnt = 1; pCnt < aIndexPoints.size(); pCnt++)
				{	rp1 = (RealPoint) aIndexPoints.elementAt(pCnt);
					pix1 = realPointToPixels(rp1);
					g.drawLine(pix0.x, pix0.y, pix1.x, pix1.y);
					rp0 = rp1;
					pix0 = pix1;
				}
			}
			// kromme en slechts 2 punten
			if ((gte.getConnectMode() == gte.CURVE) &&
			    (aIndexPoints.size() == 2))
			{	RealPoint rp0 = (RealPoint) aIndexPoints.elementAt(0);
				RealPoint rp1 = (RealPoint) aIndexPoints.elementAt(1);
				Point pix0 = realPointToPixels(rp0);
				Point pix1 = realPointToPixels(rp1);
				g.drawLine(pix0.x, pix0.y, pix1.x, pix1.y);
			}    
			// kromme en >2 punten
			if ((gte.getConnectMode() == gte.CURVE) &&
			    (aIndexPoints.size() > 2))
			{	
				Graphics2D g2D = (Graphics2D) g;
				g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				// het punt voor het startpunt p0, if any
				//RealPoint rp00 = null;
				RealPoint p00 = null;
				// startpunt p0
				RealPoint rp0 = (RealPoint) aIndexPoints.elementAt(0);
				RealPoint p0 = realPointToRealPixels(rp0);
				// eindpunt p1
				RealPoint rp1 = null;
				RealPoint p1 = null;
				// het punt na het eindpunt p1, if any
				RealPoint rp11 = null;
				RealPoint p11 = null;
				
				double intervalFrac = 3;				
				
				for (int pCnt = 1; pCnt < aIndexPoints.size(); pCnt++)
				{	// vindt eindpunt
					rp1 = (RealPoint) aIndexPoints.elementAt(pCnt);
					p1 = realPointToRealPixels(rp1);
					// kijk of p0-p1 in pixels vertikaal is, teken lijn
					
					if (Math.abs(p0.x - p1.x) < RealPoint.NZERO)
//					if (((int) Math.round(p0.x)) == ((int) Math.round(p1.x)))
					{	Point pix0 = realPointToPixels(rp0);
						Point pix1 = realPointToPixels(rp1);
						g.drawLine(pix0.x, pix0.y, pix1.x, pix1.y);
//System.out.println("vert");						

					}
					else
					{	// vindt het punt na p1, if any
						rp11 = null;
						p11 = null;
						if (pCnt < (aIndexPoints.size() - 1))
						{	rp11 = (RealPoint) aIndexPoints.elementAt(pCnt + 1);
							p11 = realPointToRealPixels(rp11);
						}
						// vindt nu de controle-punten
						// tussen p0 en p1
						// controlepunt 0
						RealPoint c0 = null;
						// p0 is het eerste punt, p1 is het tweede punt
						if (p00 == null)
						{	// vector p0 -> p1
							RealPoint slp0p1 = new RealPoint(
								p1.x - p0.x, p1.y - p0.y);
							RealPoint unitSlope0 =	slp0p1.standarize();
							double xLength = (p1.x - p0.x) / intervalFrac;
							double newLength = xLength / unitSlope0.x;
							RealPoint dir0 = new RealPoint(
								unitSlope0.x * newLength,
								unitSlope0.y * newLength);
							c0 = new RealPoint(p0.x + dir0.x, p0.y + dir0.y);		
						}
						else
						{	// vector p00 -> p0
							RealPoint slp00p0 = new RealPoint(
								p0.x - p00.x, p0.y - p00.y);
							// vector p0 -> p1
							RealPoint slp0p1 = new RealPoint(
								p1.x - p0.x, p1.y - p0.y);	
// eerst middelen, dan standariseren of omgekeerd?																
							RealPoint meanSlope0 = new RealPoint(
								(slp00p0.x + slp0p1.x) / 2,
								(slp00p0.y + slp0p1.y) / 2);	
							RealPoint unitSlope0 =	meanSlope0.standarize();	
							double xLength = //Math.max(
								//(p0.x - p00.x) / intervalFrac,
								(p1.x - p0.x) / intervalFrac
								//)
								;
							double newLength = xLength / unitSlope0.x;
							RealPoint dir0 = new RealPoint(
								unitSlope0.x * newLength,
								unitSlope0.y * newLength);
							c0 = new RealPoint(p0.x + dir0.x, p0.y + dir0.y);		
						}

// testing						
/*
						g.setColor(Color.orange);
						Point c0Pix = new Point(
							(int) Math.round(c0.x), (int) Math.round(c0.y));
						g.fillOval(c0Pix.x - gte.PRAD, c0Pix.y - gte.PRAD,
								   2 * gte.PRAD + 1, 2 * gte.PRAD + 1);						
//System.out.println("" + pCnt + " = c0 : " + c0Pix.x + "," + c0Pix.y); 												
*/
						// controlepunt 1
						RealPoint c1 = null;
						// p1 is het laatste punt
						if (p11 == null)
						{	// vector p0 -> p1
							RealPoint slp0p1 = new RealPoint(
								p1.x - p0.x, p1.y - p0.y);	
							RealPoint unitSlope1 =	slp0p1.standarize();	
							double xLength = (p1.x - p0.x) / intervalFrac;
							double newLength = xLength / unitSlope1.x;
							RealPoint dir1 = new RealPoint(
								- unitSlope1.x * newLength,
								- unitSlope1.y * newLength);
							c1 = new RealPoint(p1.x + dir1.x, p1.y + dir1.y);		
							
						}
						else
						{	// vector p0 -> p1
							RealPoint slp0p1 = new RealPoint(
								p1.x - p0.x, p1.y - p0.y);	
							// vector p1 -> p11	
							RealPoint slp1p11 = new RealPoint(
								p11.x - p1.x, p11.y - p1.y);	
// eerst middelen, dan standariseren of omgekeerd?																
							RealPoint meanSlope1 = new RealPoint(
								(slp0p1.x + slp1p11.x) / 2,
								(slp0p1.y + slp1p11.y) / 2);
							RealPoint unitSlope1 =	meanSlope1.standarize();	
							double xLength = //Math.max(
								(p1.x - p0.x) / intervalFrac//,
								//(p11.x - p1.x) / intervalFrac
								//)
								;
							double newLength = xLength / unitSlope1.x;
							RealPoint dir1 = new RealPoint(
								- unitSlope1.x * newLength,
								- unitSlope1.y * newLength);
							c1 = new RealPoint(p1.x + dir1.x, p1.y + dir1.y);		
							
						}
						
// testing						
/*
						g.setColor(Color.pink);
						Point c1Pix = new Point(
							(int) Math.round(c1.x), (int) Math.round(c1.y));
						g.fillOval(c1Pix.x - gte.PRAD, c1Pix.y - gte.PRAD,
								   2 * gte.PRAD + 1, 2 * gte.PRAD + 1);						
//System.out.println("" + pCnt + " = c1 : " + c1Pix.x + "," + c1Pix.y); 												
*/						
						g.setColor(gte.getColor(aIndex));									
						CubicCurve2D bezier = new CubicCurve2D.Double();
						bezier.setCurve(p0.x, p0.y, c0.x, c0.y,
									    c1.x, c1.y, p1.x, p1.y);
						g2D.setStroke(new BasicStroke(0.7f));
						g2D.draw(bezier);			    
		
						
					} // else niet vertikaal
					p00 = p0;
					p0 = p1;
				} // for
			} // curve en minstens 3 punten   
			
			
		} // paintComponent
		
	} // inner class PuntenVeld	
} // class GrafiekComponent
