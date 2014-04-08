package fi.graphtool;

import java.awt.AWTEventMulticaster;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;
import fi.beans.wnwidgets.NWButtonUI;
import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.ZoomKnop;
import fi.wiskopdr.expressies.Algebra;
import fi.wiskopdr.expressies.Expressie;
import fi.wiskopdr.formuleobjects.FormuleButton;
import fi.wiskopdr.formuleobjects.FormuleParser;
import fi.wiskopdr.tekstobjects.TekstArea;

public class GraphToolInteractiePanel extends JPanel implements InteractiePanel, ActionListener,
MouseListener, MouseMotionListener {

	int width = 280; // was 250
	int height = 280;
	int offset = 5;
	
	private GraphToolKnop zoomInX, zoomUitX, zoomInY, zoomUitY, 
		zoomIn, zoomUit, zoomStandaard;

	private int eenheid = 16;
	
	private static double[] DEFAULTDOMEIN;
	
	Expressie[] functies;
	Expressie[] ongelijkheden;
	Expressie[] verticaleLijnen;
	Expressie[][] parametrisaties;
	String[] parametrisatieVariabelen;
	boolean[] isY;
	boolean[] isGroterGelijk;
	boolean[] isEn;
	boolean[] inclusiefGelijkheid;
	double[][] domeinen;
	private int aantalFuncties;
	private int maxAantalExpressies;
	
	int beginwaarde;
	int selectnummer;
	String xAsNaam;
	String yAsNaam;
	String grafiekXAsNaam;
	String grafiekYAsNaam;
	
	private GrafiekVeld gv;
	private FormuleComponent formuleComponent;
	private int formuleComponentHoogte = 120;
	TekenComponent tekenComponent;
	private TabelComponent tabelComponent;
	private JPanel zoomBalk;
	
	//boolean parametrisaties = false;
	
	double beginx;
	double beginy;
	private double beginxDocent, beginyDocent;
	private int veldx, veldy;
	int veldb;
	int veldh;
	int eenheidx;
	int eenheidy;
	double eenheidxD;
	double eenheidyD;
	double schaalFactorX;
	double schaalFactorY;
	private double docentSchaalFactorX, docentSchaalFactorY;
	private int factorRijNummerX, factorRijNummerY;
	private ZoomDraad zoomDraad;
	
	int startxv = 0;
	int startyv = 0;
	int startSliderX = 0;
	int startSliderY = 0;
	boolean sliderSlepend = false;
	RealPoint dragPoint = null;
	RealPoint otherPoint = null;
	
	private DecimalFormatSymbols dfs;
	Font font = new Font("SansSerif", Font.PLAIN, 10);
	FontMetrics fm;
	DecimalFormat df;
	DecimalFormat dfTrace;
	
	boolean assenZichtbaar, schaalZichtbaar, schaalX, schaalY, roosterZichtbaar, roosterGrof, roosterX, roosterY, piLijnenZichtbaar, 
		xPositief, yPositief, xAsLog, yAsLog, xVarEditable, yVarEditable, snapToGridPoints, krommeZonderExtrapolatie, krommeMetExtrapolatie, zoomInTabel; //tekenGrafiekAan;
	boolean zoomOptie, traceOptie, dragOptie;
	boolean grafiekKleuren, kleurInstelbaar, functieBeginZichtbaar, functieBeginAanpasbaar, formeleFuncties, domeinInstelbaar;
	boolean functieToegestaan, ongelijkheidToegestaan, implicieteFunctieToegestaan, verticaleLijnToegestaan, parametrisatieToegestaan;
	
	boolean formuleComponentAan, tekenComponentAan, tabelComponentAan, tabelAlsTekenTool;
	Color piColor = Color.gray;
	private Color[] colors, gewoneKleuren;
	private Color[] opdrachtKleuren;
	//private static Color[] 
	static int PRAD = 2;
	
	boolean tracing = false;
	Slider slider;
	int tracex = -2;
	double tracexD = tracex;
	
	int tekenGrafiekNauwkeurigheid = 5;
	
	JTextField xAsNaamTF;
	Rectangle xAsNaamActivator;
	JTextField yAsNaamTF;
	Rectangle yAsNaamActivator;
	
	Color docentColor = Color.black; 
	
	protected static int GEENOPDRACHT = 0;
	protected static int VINDFORMULEBIJGRAFIEK = 1;
	protected static int VINDFORMULEBIJPUNTEN = 2;
	protected static int TEKENPUNTENBIJFORMULE = 3;
	protected static int TEKENTABELPUNTEN = 4;
	
	int typeOpdracht;
	private int scoreMax;
	private int[] maxScores;
	private int[] nauwkeurigheid;
	private int[] minimumPunten;
	private boolean leerlingZietTabel;
	private boolean domeinControleren;
	private Expressie[] docentFuncties;
	Expressie[] tekenDocentFuncties;
	double[][] docentDomeinen;
	String[][] docentDomeinStrings;
	String[] docentFunctieStrings;
	//private String[] docentFunctieStrings;
	Vector docentGraphPoints;
	
	int mode = 0;		
	private boolean correct = false;
	private boolean fout = false;
	int score;
	
	Vector listeners = new Vector();

	boolean nagekeken = false;
	private boolean ingevuld = false;

	JButton kijkNaButton;
	JLabel groenVinkjeLabel, oranjeVinkjeLabel, kruisjeLabel;
	JPanel kijkNaPanel;
	int kijkNaPanelHoogte;
	TekstArea feedbackTekst;
	private Image feedbackBallonImage;
	private JPanel mwFeedbackPanel;
	private JButton feedbackCloseButton;
	Icon goedkrulIcon, goedkrulHalfIcon, foutkruisIcon;
	
	boolean kijkNaButtonZichtbaar = false;
	URL imageURL;
	
	public static int maxGraphs = 3;
	private int numGraphs = maxGraphs;
	private int activeIndex = 1;
	Vector graphPoints = new Vector();
	SchuifParameter[] schuifParameters = new SchuifParameter[0];
	
	public GraphToolInteractiePanel() {
		setLayout(null);
		setBackground(Color.white);
		addActionListener(this);
		opdrachtKleuren = new Color[10];
		gewoneKleuren = new Color[10];
		colors = new Color[10];
	    
		opdrachtKleuren[0] = new Color(0,0,255);
		opdrachtKleuren[1] = new Color(0,220,220);
		opdrachtKleuren[2] = new Color(220,0,220);
		opdrachtKleuren[3] = new Color(200,200,0);
		opdrachtKleuren[4] = Color.black;
		opdrachtKleuren[5] = Color.black;
		opdrachtKleuren[6] = Color.black;
		opdrachtKleuren[7] = Color.black;
		opdrachtKleuren[8] = Color.black;
		opdrachtKleuren[9] = Color.black;
		
		gewoneKleuren[0] = new Color(0,0,255);
		gewoneKleuren[1] = new Color(0,200,0);
		gewoneKleuren[2] = new Color(255,50,50);
		gewoneKleuren[3] = new Color(0,220,220);
		gewoneKleuren[4] = new Color(220,0,220);
		gewoneKleuren[5] = new Color(200,200,0);
		gewoneKleuren[6] = Color.black;
		gewoneKleuren[7] = Color.black;
		gewoneKleuren[8] = Color.black;
		gewoneKleuren[9] = Color.black;
		
		for(int i = 0; i < colors.length; i++)
			colors[i] = gewoneKleuren[i];
		
		maxAantalExpressies = 9;
		aantalFuncties = 0;
		functies = new Expressie[maxAantalExpressies];
		
		DEFAULTDOMEIN = new double[2];
		DEFAULTDOMEIN[0] = Double.NEGATIVE_INFINITY;
		DEFAULTDOMEIN[1] = Double.POSITIVE_INFINITY;
		
		domeinen = new double[maxAantalExpressies][2];
		for(int i = 0; i < domeinen.length; i++)
		{	domeinen[i][0] = DEFAULTDOMEIN[0];
			domeinen[i][1] = DEFAULTDOMEIN[1];
		}
		
		ongelijkheden = new Expressie[maxAantalExpressies];
		isY = new boolean[maxAantalExpressies];
		isGroterGelijk = new boolean[maxAantalExpressies];
		isEn = new boolean[maxAantalExpressies];
		for(int i = 0; i < maxAantalExpressies; i++)
		{	isY[i] = true;
			isGroterGelijk[i] = true;
			if(i < maxAantalExpressies - 1)
				isEn[i] = true;
		}
		inclusiefGelijkheid = new boolean[maxAantalExpressies];
		
		verticaleLijnen = new Expressie[maxAantalExpressies];
		parametrisaties = new Expressie[maxAantalExpressies/2][2];
		parametrisatieVariabelen = new String[maxAantalExpressies/2];
		
		beginwaarde = 0;
		selectnummer = 999;
		
		eenheidx = eenheid;
		eenheidy = eenheid;
		eenheidxD = eenheid;
		eenheidyD = eenheid;
		veldx = offset;
		veldy = 30;
		veldb = width - 2 * offset;
		veldh = height - veldy - 2 * offset;
		docentSchaalFactorX = 1;
		docentSchaalFactorY = 1;
		schaalFactorX = 1;
		schaalFactorY = 1;
		factorRijNummerX = 99;
		factorRijNummerY = 99;
		
		xAsNaam = "x";
		yAsNaam = "y";
		grafiekXAsNaam = "x";
		grafiekYAsNaam = "y";
		
		assenZichtbaar = true;
		roosterZichtbaar = true;
		roosterGrof = false; 
		roosterX = true;
		roosterY = true;
		schaalZichtbaar = true; 
		schaalX = true;
		schaalY = true;
		piLijnenZichtbaar = false;
		xPositief = false;
		yPositief = false;
		xAsLog = false;
		yAsLog = false;
		xVarEditable = false;
		yVarEditable = false;
		snapToGridPoints = false;
		krommeZonderExtrapolatie = true;
		krommeMetExtrapolatie = true;
		zoomInTabel = true;
		
		zoomOptie = true; 
		traceOptie = true; 
		dragOptie = true; 
		
		formuleComponentAan = true;
		tekenComponentAan = false;
		tabelComponentAan = false;
		tabelAlsTekenTool = false;
		
		grafiekKleuren = true;
		kleurInstelbaar = true;
		functieBeginZichtbaar = true;
		functieBeginAanpasbaar = true;
		formeleFuncties = true;
		domeinInstelbaar = false;
		
		functieToegestaan = true;
		ongelijkheidToegestaan = true;
		implicieteFunctieToegestaan = false;
		verticaleLijnToegestaan = true;
		parametrisatieToegestaan = false;
		
		beginxDocent = veldb/2/eenheidx*eenheidx;
		beginyDocent = veldh/2/eenheidy*eenheidy;
		beginx = beginxDocent;
		beginy = beginyDocent;
		
		
		dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		df = new DecimalFormat("0.####", dfs);
		dfTrace = new DecimalFormat("0.##",dfs);
		
		gv = new GrafiekVeld(this, veldx,veldy,veldb,veldh);
		gv.addMouseListener(this);
		gv.addMouseMotionListener(this);
		add(gv);
		
		zoomBalk = new JPanel(){
			public void paintComponent(Graphics g)
			{
				//if("GR".equals(WiskOpdr.deployVariant)) ;
				//if("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant))super.paintComponent(g);
				//else					
				for(int i=0 ; i<10 ; i++)
				{	g.setColor(new Color(200+5*i,200+5*i,200+5*i));
					g.fillRect(0,getHeight() - (i+1)*getHeight()/10, getWidth(),getHeight()/10+1);
				}
				
			}
		};
		zoomBalk.setBounds(offset, offset, veldb, 23);
		zoomBalk.setLayout(null);
		zoomBalk.setBackground(new Color(210,210,210));
		//if(!"GR".equals(WiskOpdr.deployVariant))
		zoomBalk.setBorder(BorderFactory.createLineBorder(Color.lightGray));
		add(zoomBalk);
		
		zoomStandaard	= new GraphToolKnop("standaard", 0);
		zoomStandaard.setBounds(10,2,20,20);
		zoomStandaard.addActionListener(this);
		zoomBalk.add(zoomStandaard);
		
		zoomIn	= new GraphToolKnop("zoominknop.gif", 0);
		zoomIn.setBounds(35,2,20,20);
		zoomIn.addActionListener(this);
		zoomBalk.add(zoomIn);
		
		zoomUit	= new GraphToolKnop("zoomuitknop.gif", 0);
		zoomUit.setBounds(60,2,20,20);
		zoomUit.addActionListener(this);
		zoomBalk.add(zoomUit);	
		
		zoomInX	= new GraphToolKnop("zoominxknop.gif", 0);
		zoomInX.setBounds(85,2,20,20);
		zoomInX.addActionListener(this);
		zoomBalk.add(zoomInX);
		
		zoomUitX= new GraphToolKnop("zoomuitxknop.gif", 0);
		zoomUitX.setBounds(110,2,20,20);
		zoomUitX.addActionListener(this);
		zoomBalk.add(zoomUitX);
		
		zoomInY	= new GraphToolKnop("zoominyknop.gif", 0);
		zoomInY.setBounds(135,2,20,20);
		zoomInY.addActionListener(this);
		zoomBalk.add(zoomInY);
		
		zoomUitY= new GraphToolKnop("zoomuityknop.gif", 0);
		zoomUitY.setBounds(160,2,20,20);
		zoomUitY.addActionListener(this);
		zoomBalk.add(zoomUitY);
		
		formuleComponent = new FormuleComponent(true);
		formuleComponent.setSize(veldb, formuleComponentHoogte);
		formuleComponent.zetRandverhoging(false);
		add(formuleComponent);
		formuleComponent.zetFormuleRegels(maxAantalExpressies, false);
		formuleComponent.zetGrafiekComponent(this);
		formuleComponent.addActionListener(this);
		
		tabelComponent = new TabelComponent(veldb, false);
		tabelComponent.setLocation(offset, 270);
		add(tabelComponent);
		tabelComponent.zetGrafiekComponent(this);
		
		tekenComponent = new TekenComponent();
		tekenComponent.setSize(veldb, 24);
		add(tekenComponent);
		tekenComponent.zetGrafiekComponent(this);
		tekenComponent.addActionListener(this);
		
		slider = new Slider(veldb,0);
		slider.setLocation(veldx - offset, (int) Math.round(beginy));
		slider.addActionListener(this);
		slider.setBackground(getBackground());
		slider.setVisible(traceOptie);
		add(slider, 0);		
		
		xAsNaamTF = new JTextField();
		xAsNaamTF.addActionListener(this);
		xAsNaamTF.setFont(new Font ("SansSerif",Font.ITALIC,10 ));
		xAsNaamTF.setSize(80,15);
		xAsNaamTF.setVisible(false);
		gv.add(xAsNaamTF);
		xAsNaamTF.addKeyListener(new KeyAdapter()
		{	public void keyReleased(KeyEvent e)
			{	String[] forbiddenStrings = {"sin","cos","tan","ln","log"};
				String text = xAsNaamTF.getText().trim();
				
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
					if(FormuleParser.isWoordFormule())update(grafiekXAsNaam, false);
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
				if(text.length()==0) update(grafiekXAsNaam, false);
				
			}
			public void update(String s, boolean updateTF)
			{	String oldXAsNaam = grafiekXAsNaam;
				grafiekXAsNaam = s;
				if(updateTF) xAsNaamTF.setText(grafiekXAsNaam);
				if(grafiekXAsNaam.equals(""))grafiekXAsNaam = oldXAsNaam;
				repaint();
				xAsNaamTF.requestFocus();
			}
		});
		
		yAsNaamTF = new JTextField();
		yAsNaamTF.addActionListener(this);
		yAsNaamTF.setFont(new Font ("SansSerif",Font.ITALIC,10 ));
		yAsNaamTF.setSize(80,15);
		yAsNaamTF.setVisible(false);
		gv.add(yAsNaamTF);
		yAsNaamTF.addKeyListener(new KeyAdapter()
		{	public void keyReleased(KeyEvent e)
			{	String oldYAsNaam = grafiekYAsNaam;	
				grafiekYAsNaam = yAsNaamTF.getText().trim();
				yAsNaamTF.setText(grafiekYAsNaam);
				if(grafiekYAsNaam.equals(""))grafiekYAsNaam = oldYAsNaam;
				
				repaint();
				yAsNaamTF.requestFocus();
			}
		});
		
		xAsNaamActivator = new Rectangle();
		yAsNaamActivator = new Rectangle();
		
		goedkrulIcon = maakImageIcon("resources/goedkrul_en.gif");
		goedkrulHalfIcon = maakImageIcon("resources/goedkrulhalf.gif");
		foutkruisIcon = maakImageIcon("resources/foutkruis.gif");
		
		//beetje overbodig, nog weghalen?
		Font theFont = new Font("SansSerif", Font.PLAIN, 12);
		FontMetrics theFM = getFontMetrics(theFont);
		//tot hier.
		
		kijkNaButton = new JButton(GraphTool.rb.getString("kijkNaButton"));
		kijkNaButton.setFont(theFont);
		kijkNaButton.setBounds(0, 0, 75, 24);
		kijkNaButton.addActionListener(this);
		kijkNaButton.setEnabled(false);
		
	    groenVinkjeLabel = new JLabel(goedkrulIcon);
		groenVinkjeLabel.setBounds(76, 2, 20, 20);
		
		oranjeVinkjeLabel = new JLabel(goedkrulHalfIcon);
		oranjeVinkjeLabel.setBounds(76, 2, 20, 20);
		
	    kruisjeLabel = new JLabel(foutkruisIcon);
		kruisjeLabel.setBounds(76, 2, 20, 20);
		
		groenVinkjeLabel.setVisible(false);
		oranjeVinkjeLabel.setVisible(false);
		kruisjeLabel.setVisible(false);
		
		kijkNaPanel = new JPanel(null);
		kijkNaPanel.setOpaque(false);
		
		kijkNaPanelHoogte = 30;
		if("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant))
			kijkNaPanelHoogte = 50;
		kijkNaPanel.setSize(100, kijkNaPanelHoogte);
		
		kijkNaPanel.add(kijkNaButton);
		kijkNaPanel.add(groenVinkjeLabel);
		kijkNaPanel.add(oranjeVinkjeLabel);
		kijkNaPanel.add(kruisjeLabel);
		kijkNaPanel.setVisible(false);
		add(kijkNaPanel);
		
		feedbackTekst = new TekstArea();
		feedbackTekst.setSize(195,20);
		feedbackTekst.setBackground(new Color(255,255,200));
		if("MW".equals(WiskOpdr.deployVariant))feedbackTekst.setBackground(new Color(250,255,220));
		if("GR".equals(WiskOpdr.deployVariant))feedbackTekst.setBackground(new Color(255,255,255));
		feedbackTekst.setBorders(true);
		feedbackTekst.setCloseable(true);
		feedbackTekst.addActionListener(this);
		
		if("MW".equals(WiskOpdr.deployVariant))
		{
			feedbackBallonImage = NWButtonUI.loadImage("DWO-tekstballon.png", this);
			mwFeedbackPanel = new JPanel()
			{	public void paintComponent(Graphics g)
				{	int H = feedbackTekst.getHeight()+20;
					int w = getWidth();
					g.drawImage(feedbackBallonImage, 0, 0, w, 20, 0, 0, w, 20, this);
					g.drawImage(feedbackBallonImage, 0, 20, w, H-10, 0, 20, w, 25, this);
					g.drawImage(feedbackBallonImage, 0, H-10, w, H,0, 49, w, 59,  this);
				}
			};
			mwFeedbackPanel.setLayout(null);
			mwFeedbackPanel.setSize(283,59);
			feedbackCloseButton = new FormuleButton("maal",FormuleButton.MEERKNOP);
			feedbackCloseButton.addActionListener(this);
			feedbackCloseButton.setBackground(new Color(255,255,200));
			feedbackCloseButton.setBounds(mwFeedbackPanel.getSize().width-17, 5, 12,12);
			mwFeedbackPanel.add(feedbackCloseButton);
			feedbackTekst.setBorders(false);
		}
		if("GR".equals(WiskOpdr.deployVariant))
		{
			if("GR".equals(WiskOpdr.deployVariant))feedbackBallonImage = NWButtonUI.loadImage("DWO-tekstballon-gr.png", this);
			mwFeedbackPanel = new JPanel()
			{	public void paintComponent(Graphics g)
				{	int H = feedbackTekst.getHeight()+40;
					int w = getWidth();
					g.drawImage(feedbackBallonImage, 0, 0, w, 40, 0, 0, w, 40, this);
					g.drawImage(feedbackBallonImage, 0, 40, w, H-10, 0, 40, w, 44, this);
					g.drawImage(feedbackBallonImage, 0, H-10, w, H,0, 44, w, 54,  this);
				}
			};
			mwFeedbackPanel.setLayout(null);
			mwFeedbackPanel.setSize(206,54);
			feedbackCloseButton = new FormuleButton("maal",FormuleButton.MEERKNOP);
			feedbackCloseButton.addActionListener(this);
			feedbackCloseButton.setBackground(new Color(255,255,255));
			feedbackCloseButton.setBounds(mwFeedbackPanel.getSize().width-18, 18, 12,12);
			mwFeedbackPanel.add(feedbackCloseButton);
			feedbackTekst.setBorders(false);
		}
			
		
		resetDocentFunctie();
		resetDocentGraphPoints();
		maxScores = new int[9];
		maxScores[0] = 10;
		for (int i = 1; i < maxScores.length; i++)
			maxScores[i] = 0;
		scoreMax = 10;
		nauwkeurigheid = new int[3];
		for(int i = 0; i < nauwkeurigheid.length; i++)
			nauwkeurigheid[i] = 5;
		minimumPunten = new int[3]; 
		for(int i = 0; i < minimumPunten.length; i++)
			minimumPunten[i] = 5;
		leerlingZietTabel = true;
		domeinControleren = false;
		
		setBounds(0, 0, width, height); 
	}
	
	public void resetDocentFunctie()
	{	docentFuncties = new Expressie[maxAantalExpressies];
		docentFunctieStrings = new String[maxAantalExpressies]; 
		docentDomeinen = new double[maxAantalExpressies][2];
		docentDomeinStrings = new String[maxAantalExpressies][2];
		
		for(int i = 0; i < maxAantalExpressies; i++)
		{	docentFuncties[i] = null;
			docentFunctieStrings[i] = "$f@";
			docentDomeinen[i][0] = DEFAULTDOMEIN[0];
			docentDomeinen[i][1] = DEFAULTDOMEIN[1];
			docentDomeinStrings[i][0] = "$f" + Double.toString(DEFAULTDOMEIN[0]) + "@";
			docentDomeinStrings[i][1] = "$f" + Double.toString(DEFAULTDOMEIN[1]) + "@";
		}
	}
	
	public void resetDocentGraphPoints()
	{
		docentGraphPoints = new Vector();
	}
	
	public void paintComponent(Graphics g) {	
		super.paintComponent(g);
	}
	
	public ImageIcon maakImageIcon(String s)
	{
		URL imageURL = GraphTool.class.getResource(s);
		ImageIcon imageIcon = new ImageIcon();
		if (imageURL != null) 
			imageIcon = new ImageIcon(imageURL);
		else
			System.out.println("Error reading " + s);
		return imageIcon;
	}
	
	public void plaatsComponenten()
	{	int currentYTop = offset;
		int currentYBottom = this.getHeight() - offset;
		if(zoomOptie && zoomBalk != null)
		{	zoomBalk.setBounds(offset, currentYTop, veldb, zoomBalk.getHeight());
			zoomBalk.setVisible(true);
			currentYTop += zoomBalk.getHeight() + offset;
		}
		else if(zoomBalk != null)
			zoomBalk.setVisible(false);
		
		if(kijkNaButtonZichtbaar)
		{	//kijkNaPanel.setBounds(offset, currentYBottom - kijkNaPanel.getHeight(), Math.max(200, veldb), kijkNaPanel.getHeight());
			kijkNaPanel.setBounds(offset, currentYBottom - kijkNaPanel.getHeight(), 100, feedbackTekst.isVisible()?Math.max(feedbackTekst.getHeight(), kijkNaPanelHoogte):kijkNaPanelHoogte);
			kijkNaPanel.setVisible(true);
			kijkNaButton.setVisible(kijkNaButtonZichtbaar);
			currentYBottom -= kijkNaPanel.getHeight() + offset;
		}
		else if(kijkNaPanel != null)
			kijkNaPanel.setVisible(false);
		
		if(formuleComponentAan) 
		{	formuleComponent.setBounds(offset, currentYBottom - formuleComponent.getHeight(), veldb, formuleComponent.getHeight());
			formuleComponent.setVisible(true);
			currentYBottom -= formuleComponent.getHeight() + offset;
			
		}
		else 
			formuleComponent.setVisible(false);
		
		if(tabelComponentAan)
		{	tabelComponent.setBounds(offset, currentYBottom - tabelComponent.getHeight(), veldb, tabelComponent.getHeight());
			tabelComponent.setVisible(true);
			currentYBottom -= tabelComponent.getHeight() + offset;
			tabelComponent.zetAlsTekenTool(tabelAlsTekenTool, tekenComponentAan);
		}
		else 
		{	tabelComponent.setVisible(false);
		}
		if(tekenComponentAan)
		{	tekenComponent.setBounds(offset, currentYBottom - tekenComponent.getHeight(), veldb, tekenComponent.getHeight());
			tekenComponent.setVisible(true);
			currentYBottom -= tekenComponent.getHeight() + offset;
		}	
		else 
		{	tekenComponent.setVisible(false);
		}
		gv.setBounds(gv.getLocation().x, currentYTop, veldb, currentYBottom - currentYTop);
		veldh = gv.getHeight();
		
		if(schuifParameters != null)
		{	for(int i = 0; i < schuifParameters.length; i++)
			{	if(schuifParameters[i].getX() < 0 || schuifParameters[i].getX() > veldb - schuifParameters[i].geefLengte() || 
					schuifParameters[i].getY() < 0 || schuifParameters[i].getY() > currentYTop + veldh - 10)
					schuifParameters[i].zetLocatie(offset, currentYTop + veldh - 25*(i + 1));
			}
		}//p.zetLocatie(offset, gv.getY() + gv.getHeight() - offset - (schuifParameters.length - 1) * 15);
	}
	
	
	public void zetXAsNaam(String s, boolean setState)
	{	xAsNaam = s;
		grafiekXAsNaam = s;
		xAsNaamTF.setText(s);
		formuleComponent.zetXAsNaam(s, setState);
		tabelComponent.zetXAsNaam(s);
		repaint();
	}
	
	public void zetYAsNaam(String s, boolean setState)
	{	yAsNaam = s;
		grafiekYAsNaam = s;
		yAsNaamTF.setText(s);
		formuleComponent.zetYAsNaam(s, setState);
		tabelComponent.zetYAsNaam(s, true);
		repaint();
	}
	
	public void zetAssen(boolean b)
	{	assenZichtbaar = b;
		repaint();
	}
	
	public void zetRooster(boolean zichtbaar, boolean x, boolean y)
	{	roosterZichtbaar = zichtbaar;
		roosterX = x;
		roosterY = y;
		repaint();
	}
	
	public void zetRoosterX(boolean b)
	{	roosterX = b;
		repaint();
	}
	
	public void zetRoosterY(boolean b)
	{	roosterY = b;
		repaint();
	}
	
	public void zetRoosterGrof(boolean b)
	{	roosterGrof = b;
		repaint();
	}
	
	public void zetFormuleEditorOpties(Hashtable h, boolean setState)
	{
		if(h!=null)
		{
			if(h.containsKey("grafiekKleuren"))
				grafiekKleuren = ((Boolean)h.get("grafiekKleuren")).booleanValue();
			if(h.containsKey("kleurInstelbaar"))
				kleurInstelbaar = ((Boolean)h.get("kleurInstelbaar")).booleanValue();
			if(h.containsKey("functieBeginZichtbaar"))
				functieBeginZichtbaar = ((Boolean)h.get("functieBeginZichtbaar")).booleanValue();
			if(h.containsKey("functieBeginAanpasbaar"))
				functieBeginAanpasbaar = ((Boolean)h.get("functieBeginAanpasbaar")).booleanValue();
			if(h.containsKey("formeleFuncties"))
				formeleFuncties = ((Boolean)h.get("formeleFuncties")).booleanValue();
			if(h.containsKey("domeinInstelbaar"))
				domeinInstelbaar = ((Boolean)h.get("domeinInstelbaar")).booleanValue();
			if(h.containsKey("formuleComponentHoogte"))
				formuleComponentHoogte = ((Integer)h.get("formuleComponentHoogte")).intValue();
			
			if(h.containsKey("functieToegestaan"))
				functieToegestaan = ((Boolean)h.get("functieToegestaan")).booleanValue();
			if(h.containsKey("ongelijkheidToegestaan"))
				ongelijkheidToegestaan = ((Boolean)h.get("ongelijkheidToegestaan")).booleanValue();
			if(h.containsKey("implicieteFunctieToegestaan"))
				implicieteFunctieToegestaan = ((Boolean)h.get("implicieteFunctieToegestaan")).booleanValue();
			if(h.containsKey("verticaleLijnToegestaan"))
				verticaleLijnToegestaan = ((Boolean)h.get("verticaleLijnToegestaan")).booleanValue();
			if(h.containsKey("parametrisatieToegestaan"))
				parametrisatieToegestaan = ((Boolean)h.get("parametrisatieToegestaan")).booleanValue();
		}
		
		formuleComponent.setSize(formuleComponent.getWidth(), formuleComponentHoogte);
		plaatsComponenten();
		formuleComponent.zetGrafiekKleuren();
		formuleComponent.zetGrafiekKleurInstelbaar(kleurInstelbaar);
		formuleComponent.zetFormeleFuncties(formeleFuncties, setState);
		formuleComponent.zetFunctieBeginZichtbaar(functieBeginZichtbaar, setState);
		formuleComponent.zetFunctieBeginAanpasbaar(functieBeginAanpasbaar, setState);
		formuleComponent.zetDomeinInstelbaar(domeinInstelbaar, setState);
		
		formuleComponent.zetToegestaneFormules(functieToegestaan, ongelijkheidToegestaan, implicieteFunctieToegestaan, 
				verticaleLijnToegestaan, parametrisatieToegestaan, setState);
		
	}
	
	public void zetGrafiekKleuren(boolean b)
	{	grafiekKleuren = b;
		formuleComponent.zetGrafiekKleuren(); 
		repaint();
	}
	
	public void zetSnapToGridPoints(boolean b)
	{	snapToGridPoints = b;
	}
	
	public void zetKrommeKnoppen(boolean lijnen, boolean zonder, boolean met)
	{	krommeZonderExtrapolatie = zonder;
		krommeMetExtrapolatie = met;
		tekenComponent.zetLijnenKnoppen(lijnen, zonder, met);
	}
	
	public void zetTekenGrafiekNauwkeurigheid(int i)
	{	tekenGrafiekNauwkeurigheid = i;
		repaint();
	}
	
	public void zetDomeinControleren(boolean b, boolean setState)
	{	domeinControleren = b;
		if(domeinControleren)
			formuleComponent.resetDomeinen();
		formuleComponent.zetDomeinInstelbaar(b, setState);
	}
	
	public void zetLeerlingZietTabel(boolean b, boolean setState)
	{	leerlingZietTabel = b;
		zetTabelComponent(leerlingZietTabel, setState);
		tabelComponent.zetTabelPunten(docentGraphPoints, true);
	}

	public void zetSchaal(boolean zichtbaar, boolean x, boolean y)
	{	schaalZichtbaar = zichtbaar;
		schaalX = x;
		schaalY = y;
		repaint();
	}
	
	public void zetSchaalX(boolean b)
	{	schaalX = b;
		repaint();
	}
	
	public void zetSchaalY(boolean b)
	{	schaalY = b;
		repaint();
	}
	
	public void zetPiLijnen(boolean b)
	{	piLijnenZichtbaar = b;
		repaint();
	}
	
	public void zetXPositief(boolean b)
	{	xPositief = b;
		repaint();
	}
	
	public void zetYPositief(boolean b)
	{	yPositief = b;
		repaint();
	}
	
	public void zetXAsLog(boolean b)
	{	if(xAsLog != b)
		{	if(xAsLog)
			eenheidxD = eenheidxD/2;
		else
			eenheidxD = eenheidxD*2;
		eenheidx = (int) Math.round(eenheidxD);
		}
		xAsLog = b;
		repaint();
	}
	
	public void zetYAsLog(boolean b)
	{	if(yAsLog != b)
		{	if(yAsLog)
				eenheidyD = eenheidyD/2;
			else
				eenheidyD = eenheidyD*2;
			eenheidy = (int) Math.round(eenheidyD);
		}
		yAsLog = b;
		repaint();
	}
	
	public void zetXVarEditable(boolean b)
	{	xVarEditable = b;
		if(!xVarEditable)
			grafiekXAsNaam = xAsNaam;
		repaint();
	}
	
	public void zetYVarEditable(boolean b)
	{	yVarEditable = b;
		if(!yVarEditable)
			grafiekYAsNaam = yAsNaam;
		repaint();
	}
	
	public void zetKijkNaButton(boolean b)
	{	if(mode != 2 && mode != 3)
			kijkNaButtonZichtbaar = b;
		else
			kijkNaButtonZichtbaar = false;
		kijkNaPanel.setVisible(kijkNaButtonZichtbaar);
		kijkNaButton.setVisible(kijkNaButtonZichtbaar);
	}
	
	public void zetZoomOptie(boolean b)
	{	zoomOptie = b;
		zoomBalk.setVisible(b);
		plaatsComponenten();
	}
	
	public void zetDragOptie(boolean b)
	{	dragOptie = b;
	}
	
	public void zetTraceOptie(boolean b)
	{	traceOptie = b;
		slider.setVisible(traceOptie);
		repaint();
	}
	
	public void zetTekenComponent(boolean b)
	{	tekenComponentAan = b;
		plaatsComponenten();
	}
	
	public void zetTabelComponent(boolean b, boolean setState)
	{	tabelComponentAan = b;
		plaatsComponenten();
		if(tabelAlsTekenTool)
			tabelComponent.zetTabelPunten(getPoints(activeIndex, false), true);
		else 
			zetFunctie(activeIndex-1, functies[activeIndex-1], "$f@", formuleComponent.geefExpNaam(activeIndex-1), domeinen[activeIndex-1], true, setState, false);
	}
	
	public void zetFormuleComponent(boolean b, boolean setState)
	{	formuleComponentAan = b;
		plaatsComponenten();
		
		formuleComponent.parseFormule(activeIndex, setState);
		
	}
	
	public void zetTabelAlsTekenTool(boolean b, boolean setState)
	{
		tabelAlsTekenTool = b;
		if(!setState)
			tabelComponent.reset();
		plaatsComponenten();
		if(tabelAlsTekenTool)
			tabelComponent.zetTabelPunten(getPoints(activeIndex, false), true);
		else 
		{	zetFunctie(activeIndex-1, functies[activeIndex-1], "$f@", formuleComponent.geefExpNaam(activeIndex-1), domeinen[activeIndex-1], true, setState, false);
		}
	}
	
	public void zetZoomInTabel(boolean b)
	{	zoomInTabel = b;
		tabelComponent.zetZooming(b);
	}
	
	public void zetFormeleFuncties(boolean b, boolean setState)
	{	formeleFuncties = b;
		formuleComponent.zetFormeleFuncties(b, setState);
	}
	
	public void zetDomeinInstelbaar(boolean b, boolean setState)
	{	domeinInstelbaar = b;
		formuleComponent.zetDomeinInstelbaar(b, setState);
	}
	
	public FormuleComponent getFormuleComponent()
	{	return formuleComponent;
	}
	
	
	public TekenComponent getTekenComponent()
	{	return tekenComponent;
	}
	
	public TabelComponent getTabelComponent()
	{	return tabelComponent;
	}
	
	// zet hier de instellingen die specifiek zijn
		// per opdracht en die niet in de states bewaard worden	
	public void zetTypeOpdracht(int type, boolean setState)
	{	typeOpdracht = type;
		zetOpdrachtKleuren(type != GEENOPDRACHT);
		if (typeOpdracht == GEENOPDRACHT && !setState)
		{	zetTekenComponent(false);
			zetTabelComponent(false, setState);
			zetFormuleComponent(true, setState);
			formuleComponent.zetMaxAantalFormules(9, setState);
			formuleComponent.setEditable(true);
			zetDocentFuncties(null);
			zetZoomInTabel(true);
			zetFormeleFuncties(true, setState);
			zetTabelAlsTekenTool(false, setState);
			tabelComponent.setFrozen(false);
			tekenComponent.zetAantalGrafieken(3);
			tekenComponent.zetLijnenKnoppen(true, krommeZonderExtrapolatie, krommeMetExtrapolatie);
			tekenComponent.setFrozen(false);
			zetKijkNaButton(false);
		}
		else if(typeOpdracht == GEENOPDRACHT) // setState
		{	zetTekenComponent(tekenComponentAan);
			zetTabelComponent(tabelComponentAan, setState);
			zetFormuleComponent(formuleComponentAan, setState);
		}
		else if (typeOpdracht == VINDFORMULEBIJGRAFIEK)
		{	formuleComponent.setSize(getSize().width-5, 100);
			zetDocentFuncties(docentFuncties);
			//zetDocentDomeinen(docentDomeinen);
			zetDocentDomeinen(docentDomeinStrings);
			//if(docentDomeinen != null && docentDomeinen.length > 0)
				//iets??
			zetTekenComponent(false);
			zetTabelComponent(false, setState);
			zetFormuleComponent(true, setState);
			zetFormeleFuncties(formeleFuncties, setState);
			zetDomeinInstelbaar(domeinControleren, setState);

			formuleComponent.terugNaarEenRegel(setState);
			formuleComponent.zetMaxAantalFormules(Math.max(1, aantalFuncties), setState);
			
			zetKijkNaButton(false);
		}
		else if (typeOpdracht == VINDFORMULEBIJPUNTEN)
		{	formuleComponent.zetMaxAantalFormules(1, setState);
			formuleComponent.setSize(getSize().width-5, 100);
			
			zetDocentFuncties(Arrays.copyOfRange(docentFuncties, 0, 1));
			if(docentFunctieStrings != null)
				for(int i = 1; i < docentFunctieStrings.length; i++)
					docentFunctieStrings[i] = "$f@";
			zetTekenComponent(false);
			zetTabelComponent(false, setState);
			zetFormuleComponent(true, setState);
			zetFormeleFuncties(formeleFuncties, setState);
			zetDomeinInstelbaar(false, setState);

			formuleComponent.terugNaarEenRegel(setState);
			formuleComponent.zetMaxAantalFormules(1, setState); //dubbel
			zetKijkNaButton(false);
		}
		else if (typeOpdracht == TEKENPUNTENBIJFORMULE)
		{	zetTekenComponent(true);
			zetTabelComponent(false, setState);
			zetFormuleComponent(false, setState);
			zetDocentFuncties(docentFuncties);
			//zetDocentDomeinen(docentDomeinen);
			zetDocentDomeinen(docentDomeinStrings);
			tekenComponent.zetAantalGrafieken(aantalFuncties);
			tekenComponent.setConnectMode(tekenComponent.NONE);
			tekenComponent.zetLijnenKnoppen(false, krommeZonderExtrapolatie, krommeMetExtrapolatie); //niet meer nodig..
			zetKijkNaButton(true);
		}
		else if (typeOpdracht == TEKENTABELPUNTEN)
		{	
			zetZoomInTabel(false);
			zetTekenComponent(true);
			zetTabelComponent(leerlingZietTabel, setState);
			zetFormuleComponent(false, setState);
			zetFormeleFuncties(formeleFuncties, setState);
			zetDocentFuncties(Arrays.copyOfRange(docentFuncties, 0, 1));
			
			if(docentFunctieStrings != null && docentFuncties[0] != null)
				docentFunctieStrings[0] = "$f" + docentFuncties[0].toString() + "@";
			else if(docentFunctieStrings != null)
				docentFunctieStrings[0] = "$f@";
			if(docentFunctieStrings != null)
				for(int i = 1; i < docentFunctieStrings.length; i++)
				{
					docentFunctieStrings[i] = "$f@";
				}
			
			zetTabelAlsTekenTool(false, setState);
			tabelComponent.setXVakEditable(false);
			tabelComponent.setYVakEditable(false);
			
			tabelComponent.zetEenTabel(true);
			tabelComponent.zetYAsNaam(yAsNaam, false);
			
			tekenComponent.zetAantalGrafieken(1);
			tekenComponent.zetLijnenKnoppen(false, false, false);
			tabelComponent.zetEenTabel(true);
			tabelComponent.zetTabelPunten(docentGraphPoints, true);
			tabelComponent.zetReset(false);
				
			
			zetKijkNaButton(true);
			
		}
		
	}
	
	public void updateAantalFuncties()
	{	aantalFuncties = 0;
		if(docentFunctieStrings != null)
			for(int i = 0; i < docentFunctieStrings.length; i++)
				if(!docentFunctieStrings[i].equals("$f@"))
				{	aantalFuncties++;
				}
	}
	
	public void zetMaxScores(int[] ms)
	{	
		maxScores = ms;
		updateAantalFuncties();
		if(typeOpdracht == VINDFORMULEBIJGRAFIEK || typeOpdracht == TEKENPUNTENBIJFORMULE)
		{	scoreMax = 0;
			for(int i = 0; i < aantalFuncties; i++)
				scoreMax += maxScores[i];
		}
		else if(typeOpdracht == GEENOPDRACHT)
			scoreMax = 0;
		else
			scoreMax = maxScores[0];
	}

	public void zetNauwkeurigheid(int[] ms)
	{	nauwkeurigheid = ms;
	}

	public void zetMinimumPunten(int[] ms)
	{	minimumPunten = ms;
	}
		
		
	public void zetDocentFuncties(Expressie[] docentFunc)
	{	docentFuncties = docentFunc;
		tekenDocentFuncties = docentFunc;
		if(docentFunc == null)
			for(int i = 0; i < docentFunctieStrings.length; i++)
				docentFunctieStrings[i] = "$f@";
		repaint();
	}
	
	public void zetDocentDomeinen(String[][] docentDomString)
	{
		if(docentDomString == null) //moet eigenlijk niet gebeuren!
		{	docentDomeinStrings = null;
			docentDomeinen = null;
			return;
		}
		else
		{	docentDomeinStrings = new String[docentDomString.length][2];
			docentDomeinen = new double[docentDomString.length][2];
		}
		for(int i = 0; i < docentDomString.length; i++)
		{	docentDomeinStrings[i][0] = docentDomString[i][0];
			docentDomeinStrings[i][1] = docentDomString[i][1];
			if(docentDomeinStrings[i][0].equals("$f" + Double.toString(DEFAULTDOMEIN[0]) + "@"))
				docentDomeinen[i][0] = DEFAULTDOMEIN[0];
			else
				try{
				docentDomeinen[i][0] = FormuleParser.geefExpressie(docentDomString[i][0]).geefWaarde();
				}
				catch(Exception e){
					docentDomeinen[i][0] = DEFAULTDOMEIN[0];
				}
			if(docentDomeinStrings[i][1].equals("$f" + Double.toString(DEFAULTDOMEIN[1]) + "@"))
				docentDomeinen[i][1] = DEFAULTDOMEIN[1];
			else
				try{
				docentDomeinen[i][1] = FormuleParser.geefExpressie(docentDomString[i][1]).geefWaarde();
				}
				catch(Exception e){
					docentDomeinen[i][1] = DEFAULTDOMEIN[1];
				}
		}
		if(typeOpdracht == VINDFORMULEBIJGRAFIEK && !domeinControleren)
			formuleComponent.zetDomeinen(docentDomeinen);
		repaint();
	}
	
	/*
	public void zetDocentDomeinen(double[][] docentDom)
	{	if(docentDom == null)
		{	docentDomeinen = null;
			return;
		}
		else
			docentDomeinen = new double[docentDom.length][2];
		for(int i = 0; i < docentDomeinen.length; i++)
		{	docentDomeinen[i][0] = docentDom[i][0];
			docentDomeinen[i][1] = docentDom[i][1];
		}
		if(typeOpdracht == VINDFORMULEBIJGRAFIEK && !domeinControleren)
			formuleComponent.zetDomeinen(docentDom);
		repaint();
	}
	*/
		
		
	public Point realPointToPixels(RealPoint rp)
	{	if (Double.isNaN(rp.getX()) || Double.isNaN(rp.getY()))
			return null;
		
		Point pix = new Point();
		pix.x =	(int) Math.round(beginx + eenheidxD * (xAsLog?Math.log10(rp.getX()):rp.getX()) / schaalFactorX);
		pix.y = (int) Math.round(gv.getSize().height -
								(beginy + eenheidyD * (yAsLog?Math.log10(rp.getY()):rp.getY()) / schaalFactorY));
		return pix;
	}
	
	public void setBounds(int x, int y, int b, int h)
	{	super.setBounds(x,y,b,h);
		width = b;
		height = h; 
		veldb = b - 2*offset; 
		veldh = height - 2* offset;
		slider.zetLengte(veldb);
		plaatsComponenten();
	}
	
	public void setSize(int b, int h)
	{	super.setSize(b,h);
		veldb = b - 2*offset;
		slider.zetLengte(veldb);
		plaatsComponenten();
	}
	
	public void zetOpdrachtKleuren(boolean opdrachten)
	{
		if(opdrachten)
			for(int i = 0; i < colors.length; i++)
				colors[i] = opdrachtKleuren[i]; 
		else
			for(int i = 0; i < colors.length; i++)
				colors[i] = gewoneKleuren[i];
		formuleComponent.zetGrafiekKleuren();
		
	}
	
	public Color getFormuleColor(int index)
	{
		if(grafiekKleuren && typeOpdracht != GEENOPDRACHT)
			return opdrachtKleuren[index];
		else if(grafiekKleuren)
			return gewoneKleuren[index];
		else
			return gewoneKleuren[0];
	}
	
	//verschil tussen deze en die hierboven: bij nagekeken grafieken wordt grafiek groen, rood of oranje, 
	//maar de bijbehorende formule moet niet van kleur veranderen
	public Color getTekenColor(int index)
	{	if(grafiekKleuren)
			return colors[index];
		else
			return colors[0];
	}
	
	public void zetDocentColor(Color c)
	{
		docentColor = c;
		
		repaint();
	}
	
	public void setColor(int nr, Color c, boolean nakijken)
	{
		colors[nr] = c;
		if(typeOpdracht == GEENOPDRACHT)
			gewoneKleuren[nr] = c;
		else if (!nakijken)
			opdrachtKleuren[nr] = c;
		
		repaint();
	}
	
	public int getNumGraphs()
	{	return numGraphs;
	}
	
	public int getActiveIndex()
	{	return activeIndex;
	}
	
	public int getTypeOpdracht()
	{	return typeOpdracht;
	}
	
	
	public void setActiveIndex(int index, boolean setState)
	{	
		if ((index < 1) || (index > maxGraphs))
			activeIndex = 1;
		else
			activeIndex = index;
		
		tekenComponent.zetSelectedIndexGrKeuze(activeIndex - 1);
		if(tabelAlsTekenTool)
			tabelComponent.setActiveIndex(activeIndex, setState);
		repaint();			
	}
	
	public String[] getDocentFunctieStrings()	{
		return docentFunctieStrings;
	}
	
	public double[][] getDocentDomeinen()	{
		return docentDomeinen;
	}
	
	public Expressie[] getDocentFuncties()	{
		return docentFuncties;
	}
	
	public Vector getPoints(int index, boolean docent)
	{	Vector points = new Vector();
		for (int pCnt = 0; pCnt < (docent?docentGraphPoints:graphPoints).size(); pCnt++)
		{	RealPoint rp = (RealPoint) (docent?docentGraphPoints:graphPoints).elementAt(pCnt);
			if (rp.getIndex() == index)
				points.addElement(rp);
		}
		return points;
	}
	
	public void addInsert(RealPoint newRP, boolean docent)
	{	if((xPositief && newRP.getX() < 0) || (yPositief && newRP.getY() < 0))
			return;
				
		int pIndex = -1;
		boolean firstFound = false;
		for (int pCnt = 0; pCnt < (docent?docentGraphPoints:graphPoints).size(); pCnt++)
		{	RealPoint rp = (RealPoint) (docent?docentGraphPoints:graphPoints).elementAt(pCnt);
			if (!firstFound && rp.hasLargerXThen(newRP))
			{	pIndex = pCnt;
				firstFound = true;
			}
		}
		if (pIndex == -1)
			(docent?docentGraphPoints:graphPoints).addElement(newRP);
		else
			(docent?docentGraphPoints:graphPoints).insertElementAt(newRP, pIndex);
			
		if(tabelAlsTekenTool)
			tabelComponent.zetTabelPunt(newRP);
		repaint();
		produceAction("points changed");
	}
	
	
	public void removePoints(int index, boolean docent)
	{	for (int pCnt = (docent?docentGraphPoints:graphPoints).size() - 1; pCnt > - 1; pCnt--)
		{	RealPoint rp = (RealPoint) (docent?docentGraphPoints:graphPoints).elementAt(pCnt);
			if (rp.getIndex() == index)
				(docent?docentGraphPoints:graphPoints).removeElementAt(pCnt);
		}
		
		tabelComponent.reset();
		produceAction("points changed");
	}
	
	public void removePoint(int tabelindex, int index, boolean docent)
	{	for (int pCnt = (docent?docentGraphPoints:graphPoints).size() - 1; pCnt > - 1; pCnt--)
		{	RealPoint rp = (RealPoint) (docent?docentGraphPoints:graphPoints).elementAt(pCnt);
			if (rp.getIndex() == index && rp.getTabelIndex() == tabelindex)
			{	(docent?docentGraphPoints:graphPoints).removeElementAt(pCnt);
				produceAction("points changed");
				break;
			}
		}
	}
		
	public boolean hasPointWithSameXAs(RealPoint aRp, boolean docent)
	{	boolean found = false;
		Vector rPoints = getPoints(aRp.getIndex(), docent);
		for (int rCnt = 0; rCnt < rPoints.size(); rCnt++)
		{	RealPoint rp = (RealPoint) rPoints.elementAt(rCnt);
			if (aRp.hasSameXAs(rp))
				found = true;
		}
		return found;
	}
	
	public void voegSchuifParameterToe(SchuifParameter p)
	{	SchuifParameter[] parameters = new SchuifParameter[schuifParameters.length + 1];
		for(int i = 0; i < parameters.length - 1; i++)
			parameters[i] = schuifParameters[i];
		parameters[parameters.length - 1] = p;
		
		schuifParameters = parameters;
		p.zetLocatie(offset, gv.getY() + gv.getHeight() - (schuifParameters.length) * 25);
		p.geefSlider().setBackground(getBackground());
		p.geefSlider().addActionListener(this);
		p.geefSlider().addMouseListener(this);
		p.geefSlider().addMouseMotionListener(this);
		add(p.geefSlider(), 0);
		repaint();
	}
	
	public void wijzigSchuifParameter(int index, SchuifParameter p)
	{
		remove(schuifParameters[index].geefSlider());
		SchuifParameter[] parameters = new SchuifParameter[schuifParameters.length];
		for(int j = 0; j < parameters.length; j++)
		{	if(j != index)
				parameters[j] = schuifParameters[j];
		}
		parameters[index] = p;
		
		schuifParameters = parameters;
		p.zetLocatie(offset, gv.getY() + gv.getHeight() - offset - (index + 1) * 25);
		p.geefSlider().setBackground(getBackground());
		p.geefSlider().addActionListener(this);
		add(p.geefSlider(), 0);
		repaint();
	}
	
	
	
	
	public void setState(Hashtable h) 	
	{	
		//hier hoeven eigenlijk alleen maar dingen in die een leerling zou kunnen veranderen. 
		//Misschien is het dus goed om deze methode eens flink op te schonen.
		double beginxDocent = 1;
		double beginyDocent = 1;
		double beginx = 1;
		double beginy = 1;
		double docentSchaalFactorX = 1;
		double docentSchaalFactorY = 1;
		double schaalFactorX = 1;
		double schaalFactorY = 1;
		//graphPoints
		double[] graphPointsX = null;
		double[] graphPointsY = null;
		int[] graphPointsIndex = null;
		int[] graphPointsTabelIndex = null;
		String[] graphPointsXString = null;
		String[] graphPointsYString = null;
		//int[][] colorRGBs = new int[colors.length][3];
		int[][] colorRGBsOpdrachten = new int[opdrachtKleuren.length][3];
		for(int i = 0; i < opdrachtKleuren.length; i++)
		{	colorRGBsOpdrachten[i][0] = opdrachtKleuren[i].getRed();
			colorRGBsOpdrachten[i][1] = opdrachtKleuren[i].getGreen();
			colorRGBsOpdrachten[i][2] = opdrachtKleuren[i].getBlue();
		}
		int[][] colorRGBsGewoon = new int[gewoneKleuren.length][3];
		for(int i = 0; i < gewoneKleuren.length; i++)
		{	colorRGBsGewoon[i][0] = gewoneKleuren[i].getRed();
			colorRGBsGewoon[i][1] = gewoneKleuren[i].getGreen();
			colorRGBsGewoon[i][2] = gewoneKleuren[i].getBlue();
		}
		
		//schuifParameters
		double[] paramWaarden = null;
		
		int activeIndex = 1;
		String grafiekXAsNaam = "x";
		String grafiekYAsNaam = "y";
		String xAsNaam = "x";
		String yAsNaam = "y";
		boolean formuleComponentAan = true;
		boolean tekenComponentAan = false;
		boolean tabelComponentAan = false;
		boolean assenZichtbaar = true;
		boolean roosterZichtbaar = true;
		boolean roosterGrof = false; 
		boolean roosterX = true;
		boolean roosterY = true;
		boolean schaalZichtbaar = true;
		boolean schaalX = true;
		boolean schaalY = true;
		boolean piLijnenZichtbaar = false; 
		boolean zoomOptie = true; 
		boolean traceOptie = true; 
		boolean dragOptie = true; 
		boolean zoomInTabel = true;
		boolean tabelAlsTekenTool = false; 
		boolean xPositief = false; 
		boolean yPositief = false; 
		boolean xAsLog = false;
		boolean yAsLog = false;
		boolean xVarEditable = false;
		boolean yVarEditable = false;
		boolean snapToGridPoints = false;
		boolean krommeZonderExtrapolatie = true;
		boolean krommeMetExtrapolatie = true;
		int tekenGrafiekNauwkeurigheid = 5;
		int selectnummer = 999;
		int beginwaarde = 0;
		double tracexD = -2;
		
		boolean grafiekKleuren = true;
		boolean kleurInstelbaar = true;
		boolean functieBeginZichtbaar = true;
		boolean functieBeginAanpasbaar = true;
		boolean formeleFuncties = true;
		boolean domeinInstelbaar = true;
		int formuleComponentHoogte = 120;
		
		boolean functieToegestaan = true;
		boolean ongelijkheidToegestaan = true;
		boolean implicieteFunctieToegestaan = true;
		boolean verticaleLijnToegestaan = true;
		boolean parametrisatieToegestaan = true;
		
		if(h.containsKey("beginxDocent"))
	    	beginxDocent = ((Double)h.get("beginxDocent")).doubleValue();
    	if(h.containsKey("beginyDocent"))
    		beginyDocent = ((Double)h.get("beginyDocent")).doubleValue();
    	if(h.containsKey("beginx")) 
    		beginx = ((Double)h.get("beginx")).doubleValue();
    	if(h.containsKey("beginy")) 
    		beginy = ((Double)h.get("beginy")).doubleValue();
    	if(h.containsKey("docentSchaalFactorX"))
    		docentSchaalFactorX = ((Double)h.get("docentSchaalFactorX")).doubleValue();
    	if(h.containsKey("docentSchaalFactorY"))
    		docentSchaalFactorY = ((Double)h.get("docentSchaalFactorY")).doubleValue();
    	if(h.containsKey("schaalFactorX")) 
    		schaalFactorX = ((Double)h.get("schaalFactorX")).doubleValue();
    	if(h.containsKey("schaalFactorY")) 
    		schaalFactorY = ((Double)h.get("schaalFactorY")).doubleValue();
    	//if(h.containsKey("leerlingGrafiek"))
    	//	leerlingGrafiek = (boolean[])h.get("leerlingGrafiek");
    	//if(h.containsKey("graphPoints"))
    	//	graphPoints = (Vector)h.get("graphPoints");
    	if(h.containsKey("graphPointsX"))
    		graphPointsX = ((double[])h.get("graphPointsX"));
    	if(h.containsKey("graphPointsY"))
    		graphPointsY = ((double[])h.get("graphPointsY"));
    	if(h.containsKey("graphPointsIndex"))
    		graphPointsIndex = ((int[])h.get("graphPointsIndex"));
    	if(h.containsKey("graphPointsTabelIndex"))
    		graphPointsTabelIndex = ((int[])h.get("graphPointsTabelIndex"));
    	if(h.containsKey("graphPointsXString"))
    		graphPointsXString = ((String[])h.get("graphPointsXString"));
    	if(h.containsKey("graphPointsYString"))
    		graphPointsYString = ((String[])h.get("graphPointsYString"));
    	if(h.containsKey("colorRGBsOpdrachten"))
    		colorRGBsOpdrachten = ((int[][])h.get("colorRGBsOpdrachten"));
    	if(h.containsKey("colorRGBsGewoon"))
    		colorRGBsGewoon = ((int[][])h.get("colorRGBsGewoon"));
    	else if(h.containsKey("colorRGBs"))
    		colorRGBsGewoon = ((int[][])h.get("colorRGBs"));
    	if(h.containsKey("paramWaarden"))
    		paramWaarden = ((double[])h.get("paramWaarden"));
    	if(h.containsKey("activeIndex"))
    		activeIndex = ((Integer)h.get("activeIndex")).intValue();
    	if(h.containsKey("grafiekXAsNaam"))
    		grafiekXAsNaam = ((String)h.get("grafiekXAsNaam"));
    	if(h.containsKey("grafiekYAsNaam"))
    		grafiekYAsNaam = ((String)h.get("grafiekYAsNaam"));
		if (h.containsKey("xAsNaam")) 
			xAsNaam = (String) h.get("xAsNaam");
		if (h.containsKey("yAsNaam")) 
			yAsNaam = (String) h.get("yAsNaam");
		if (h.containsKey("formuleComponentAan")) 
			formuleComponentAan = ((Boolean) h.get("formuleComponentAan")).booleanValue();
		if (h.containsKey("tekenComponentAan")) 
			tekenComponentAan = ((Boolean) h.get("tekenComponentAan")).booleanValue();
		if (h.containsKey("tabelComponentAan")) 
			tabelComponentAan = ((Boolean) h.get("tabelComponentAan")).booleanValue();
		if (h.containsKey("assenZichtbaar")) 
			assenZichtbaar = ((Boolean) h.get("assenZichtbaar")).booleanValue();
		if (h.containsKey("roosterZichtbaar")) 
			roosterZichtbaar = ((Boolean) h.get("roosterZichtbaar")).booleanValue();
		if (h.containsKey("roosterGrof")) 
			roosterGrof = ((Boolean) h.get("roosterGrof")).booleanValue();
		if (h.containsKey("roosterX"))
			roosterX = ((Boolean) h.get("roosterX")).booleanValue();
		if (h.containsKey("roosterY"))
			roosterY = ((Boolean) h.get("roosterY")).booleanValue();
		if (h.containsKey("schaalZichtbaar")) 
			schaalZichtbaar = ((Boolean) h.get("schaalZichtbaar")).booleanValue();
		if (h.containsKey("schaalX"))
			schaalX = ((Boolean) h.get("schaalX")).booleanValue();
		if (h.containsKey("schaalY"))
			schaalY = ((Boolean) h.get("schaalY")).booleanValue();
		if (h.containsKey("piLijnenZichtbaar")) 
			piLijnenZichtbaar = ((Boolean) h.get("piLijnenZichtbaar")).booleanValue();
		if (h.containsKey("zoomOptie")) 
			zoomOptie = ((Boolean) h.get("zoomOptie")).booleanValue();
		if (h.containsKey("traceOptie")) 
			traceOptie = ((Boolean) h.get("traceOptie")).booleanValue();
		if (h.containsKey("dragOptie")) 
			dragOptie = ((Boolean) h.get("dragOptie")).booleanValue();
		if (h.containsKey("zoomInTabel")) 
			zoomInTabel = ((Boolean) h.get("zoomInTabel")).booleanValue();
		if (h.containsKey("tabelAlsTekenTool")) 
			tabelAlsTekenTool = ((Boolean) h.get("tabelAlsTekenTool")).booleanValue();
		if (h.containsKey("xPositief")) 
			xPositief = ((Boolean) h.get("xPositief")).booleanValue();
		if (h.containsKey("yPositief")) 
			yPositief = ((Boolean) h.get("yPositief")).booleanValue();
		if (h.containsKey("xAsLog")) 
			xAsLog = ((Boolean) h.get("xAsLog")).booleanValue();
		if (h.containsKey("yAsLog")) 
			yAsLog = ((Boolean) h.get("yAsLog")).booleanValue();
		if (h.containsKey("xVarEditable")) 
			xVarEditable = ((Boolean) h.get("xVarEditable")).booleanValue();
		if (h.containsKey("yVarEditable")) 
			yVarEditable = ((Boolean) h.get("yVarEditable")).booleanValue();
		if (h.containsKey("snapToGridPoints"))
			snapToGridPoints = ((Boolean) h.get("snapToGridPoints")).booleanValue();
		if (h.containsKey("krommeZonderExtrapolatie"))
			krommeZonderExtrapolatie = ((Boolean) h.get("krommeZonderExtrapolatie")).booleanValue();
		if (h.containsKey("krommeMetExtrapolatie"))
			krommeMetExtrapolatie = ((Boolean) h.get("krommeMetExtrapolatie")).booleanValue();
		if (h.containsKey("tekenGrafiekNauwkeurigheid"))
			tekenGrafiekNauwkeurigheid = ((Integer) h.get("tekenGrafiekNauwkeurigheid")).intValue();
		if (h.containsKey("selectnummer"))
			selectnummer = ((Integer) h.get("selectnummer")).intValue();
		if (h.containsKey("beginwaarde"))
			beginwaarde = ((Integer) h.get("beginwaarde")).intValue();
		if (h.containsKey("tracexD"))
			tracexD = ((Double) h.get("tracexD")).doubleValue();
		
		if(h.containsKey("grafiekKleuren"))
			grafiekKleuren = ((Boolean)h.get("grafiekKleuren")).booleanValue();
		if(h.containsKey("kleurInstelbaar"))
			kleurInstelbaar = ((Boolean)h.get("kleurInstelbaar")).booleanValue();
		if(h.containsKey("functieBeginZichtbaar"))
			functieBeginZichtbaar = ((Boolean)h.get("functieBeginZichtbaar")).booleanValue();
		if(h.containsKey("functieBeginAanpasbaar"))
			functieBeginAanpasbaar = ((Boolean)h.get("functieBeginAanpasbaar")).booleanValue();
		if(h.containsKey("formeleFuncties"))
			formeleFuncties = ((Boolean)h.get("formeleFuncties")).booleanValue();
		if(h.containsKey("domeinInstelbaar"))
			domeinInstelbaar = ((Boolean)h.get("domeinInstelbaar")).booleanValue();
		if(h.containsKey("formuleComponentHoogte"))
			formuleComponentHoogte = ((Integer)h.get("formuleComponentHoogte")).intValue();
		
		if(h.containsKey("functieToegestaan"))
			functieToegestaan = ((Boolean)h.get("functieToegestaan")).booleanValue();
		if(h.containsKey("ongelijkheidToegestaan"))
			ongelijkheidToegestaan = ((Boolean)h.get("ongelijkheidToegestaan")).booleanValue();
		if(h.containsKey("implicieteFunctieToegestaan"))
			implicieteFunctieToegestaan = ((Boolean)h.get("implicieteFunctieToegestaan")).booleanValue();
		if(h.containsKey("verticaleLijnToegestaan"))
			verticaleLijnToegestaan = ((Boolean)h.get("verticaleLijnToegestaan")).booleanValue();
		if(h.containsKey("parametrisatieToegestaan"))
			parametrisatieToegestaan = ((Boolean)h.get("parametrisatieToegestaan")).booleanValue();
		
		//hiervan doe je ook nog heel veel bij het zetten van de instellingen iets verderop..
		this.beginxDocent = beginxDocent;
		this.beginyDocent = beginyDocent;
    	this.beginx = beginx;
		this.beginy = beginy;
		this.docentSchaalFactorX = docentSchaalFactorX;
		this.docentSchaalFactorY = docentSchaalFactorY;
		this.schaalFactorX = schaalFactorX;
		this.schaalFactorY = schaalFactorY;
		//this.graphPoints = graphPoints;
		this.graphPoints = new Vector();
		for(int i = 0; i < graphPointsX.length; i++)
		{	RealPoint rp = new RealPoint(graphPointsX[i], graphPointsY[i]);
			rp.setIndex(graphPointsIndex[i]);
			rp.setTabelIndex(graphPointsTabelIndex[i]);
			rp.setxString(graphPointsXString[i]);
			rp.setyString(graphPointsYString[i]);
			graphPoints.add(rp);
		}
		for(int i = 0; i < colorRGBsGewoon.length; i++)
		{	//colors[i] = new Color(colorRGBs[i][0], colorRGBs[i][1], colorRGBs[i][2]);
			gewoneKleuren[i] = new Color(colorRGBsGewoon[i][0], colorRGBsGewoon[i][1], colorRGBsGewoon[i][2]);
		}
		for(int i = 0; i < colorRGBsOpdrachten.length; i++)
		{	//colors[i] = new Color(colorRGBs[i][0], colorRGBs[i][1], colorRGBs[i][2]);
			opdrachtKleuren[i] = new Color(colorRGBsOpdrachten[i][0], colorRGBsOpdrachten[i][1], colorRGBsOpdrachten[i][2]);
		}
		if(paramWaarden != null)
		{	//this.schuifParameters = new SchuifParameter[paramNamen.length];
			for(int i = 0; i < schuifParameters.length; i++)
			{	//schuifParameters[i] = new SchuifParameter(paramLengtes[i], paramNamen[i]);
				schuifParameters[i].zetWaarde(paramWaarden[i], false);
			}
		}
		this.activeIndex = activeIndex;
		this.xAsNaam = xAsNaam;
		this.yAsNaam = yAsNaam;
		this.formuleComponentAan = formuleComponentAan;
		this.tekenComponentAan = tekenComponentAan;
		this.tabelComponentAan = tabelComponentAan;
		this.assenZichtbaar = assenZichtbaar;
		this.roosterZichtbaar = roosterZichtbaar;
		this.roosterGrof = roosterGrof;
		this.roosterX = roosterX;
		this.roosterY = roosterY;
		this.schaalZichtbaar = schaalZichtbaar;
		this.schaalX = schaalX;
		this.schaalY = schaalY;
		this.piLijnenZichtbaar = piLijnenZichtbaar;
		this.zoomOptie = zoomOptie;
		this.traceOptie = traceOptie;
		this.dragOptie = dragOptie;
		this.zoomInTabel = zoomInTabel;
		this.tabelAlsTekenTool = tabelAlsTekenTool;
		this.xPositief = xPositief;
		this.yPositief = yPositief;
		this.xVarEditable = xVarEditable;
		this.yVarEditable = yVarEditable;
		this.snapToGridPoints = snapToGridPoints;
		this.krommeZonderExtrapolatie = krommeZonderExtrapolatie;
		this.krommeMetExtrapolatie = krommeMetExtrapolatie;
		this.tekenGrafiekNauwkeurigheid = tekenGrafiekNauwkeurigheid;
		this.selectnummer = selectnummer;
		this.beginwaarde = beginwaarde;
		this.tracexD = tracexD;
		tracex = (int) Math.round(tracexD);
		
		this.grafiekKleuren = grafiekKleuren;
		this.kleurInstelbaar = kleurInstelbaar;
		this.functieBeginZichtbaar = functieBeginZichtbaar;
		this.functieBeginAanpasbaar = functieBeginAanpasbaar;
		this.formeleFuncties = formeleFuncties;
		this.domeinInstelbaar = domeinInstelbaar;
		this.formuleComponentHoogte = formuleComponentHoogte;
		
		this.functieToegestaan = functieToegestaan;
		this.ongelijkheidToegestaan = ongelijkheidToegestaan;
		this.implicieteFunctieToegestaan = implicieteFunctieToegestaan;
		this.verticaleLijnToegestaan = verticaleLijnToegestaan;
		this.parametrisatieToegestaan = parametrisatieToegestaan;
		
		zetXAsNaam(xAsNaam, true);
		zetYAsNaam(yAsNaam, true);
		this.grafiekXAsNaam = grafiekXAsNaam;
		this.grafiekYAsNaam = grafiekYAsNaam;
		xAsNaamTF.setText(grafiekXAsNaam);
		yAsNaamTF.setText(grafiekYAsNaam);
		
			// opdrachten
		int typeOpdracht = GEENOPDRACHT;
		int[] maxScores = new int[9];
		int[] nauwkeurigheid = new int[3];
		int[] minimumPunten = new int[3];
		int scoreMax = 0;
		boolean domeinControleren = false;
		boolean leerlingZietTabel = true;
		//Expressie[] docentFuncties = new Expressie[maxAantalExpressies];
		String[] docentFunctieStrings = new String[maxAantalExpressies];
		//double[][] docentDomeinen = new double[maxAantalExpressies][2];
		String[][] docentDomeinStrings = new String[maxAantalExpressies][2];
		//Vector docentGraphPoints = new Vector();
		double[] docentGraphPointsX = null;
		double[] docentGraphPointsY = null;
		int[] docentGraphPointsIndex = null;
		int[] docentGraphPointsTabelIndex = null;
		String[] docentGraphPointsXString = null;
		String[] docentGraphPointsYString = null;
		boolean ingevuld = false;
		boolean nagekeken = false;
		
		
		if (h.containsKey("typeOpdracht")) 
			typeOpdracht = ((Integer) h.get("typeOpdracht")).intValue();		
		if (h.containsKey("maxScores")) 
			maxScores = (int[]) h.get("maxScores");		
		if (h.containsKey("docentFunctieStrings")) 
			docentFunctieStrings = (String[]) h.get("docentFunctieStrings");
		//if (h.containsKey("docentDomeinen"))
		//	docentDomeinen = (double[][])h.get("docentDomeinen");
		if (h.containsKey("docentDomeinStrings"))
			docentDomeinStrings = (String[][])h.get("docentDomeinStrings");
		//if (h.containsKey("docentGraphPoints")) 
			//docentGraphPoints = (Vector) h.get("docentGraphPoints");		
		if(h.containsKey("docentGraphPointsX"))
			docentGraphPointsX = ((double[])h.get("docentGraphPointsX"));
    	if(h.containsKey("docentGraphPointsY"))
    		docentGraphPointsY = ((double[])h.get("docentGraphPointsY"));
    	if(h.containsKey("docentGraphPointsIndex"))
    		docentGraphPointsIndex = ((int[])h.get("docentGraphPointsIndex"));
    	if(h.containsKey("docentGraphPointsTabelIndex"))
    		docentGraphPointsTabelIndex = ((int[])h.get("docentGraphPointsTabelIndex"));
    	if(h.containsKey("docentGraphPointsXString"))
    		docentGraphPointsXString = ((String[])h.get("docentGraphPointsXString"));
    	if(h.containsKey("docentGraphPointsYString"))
    		docentGraphPointsYString = ((String[])h.get("docentGraphPointsYString"));
    	if (h.containsKey("nauwkeurigheid")) 
			nauwkeurigheid = (int[]) h.get("nauwkeurigheid");		
		if (h.containsKey("minimumPunten")) 
			minimumPunten = (int[]) h.get("minimumPunten");	
		if (h.containsKey("scoreMax"))
			scoreMax = ((Integer) h.get("scoreMax")).intValue();
		if (h.containsKey("domeinControleren"))
			domeinControleren = ((Boolean) h.get("domeinControleren")).booleanValue();
		if (h.containsKey("leerlingZietTabel")) 
			leerlingZietTabel = ((Boolean) h.get("leerlingZietTabel")).booleanValue();		
		if (h.containsKey("ingevuld")) 
			ingevuld = ((Boolean)h.get("ingevuld")).booleanValue();
		if (h.containsKey("nagekeken")) 
			nagekeken = ((Boolean)h.get("nagekeken")).booleanValue();
		
		this.typeOpdracht = typeOpdracht;
		this.maxScores = maxScores;	
		this.nauwkeurigheid = nauwkeurigheid;
		this.minimumPunten = minimumPunten; 
		this.scoreMax = scoreMax;
		this.domeinControleren = domeinControleren;
		this.leerlingZietTabel = leerlingZietTabel;
		this.docentFunctieStrings = docentFunctieStrings;
		if(docentFunctieStrings != null)
		{	docentFuncties = new Expressie[docentFunctieStrings.length];
			for(int i = 0; i < docentFunctieStrings.length; i++)
				if (!docentFunctieStrings[i].equals("$f@"))
					docentFuncties[i] = FormuleParser.geefExpressie(docentFunctieStrings[i]);
		}
		// anders blijft docentFunctie null	
		//this.docentFuncties = docentFuncties;
		updateAantalFuncties();
		
		
		this.docentDomeinStrings = docentDomeinStrings;
		if(docentDomeinStrings != null)
		{	docentDomeinen = new double[docentDomeinStrings.length][2];
			for(int i = 0; i < docentDomeinStrings.length; i++)
			{	docentDomeinen[i][0] = FormuleParser.geefExpressie(docentDomeinStrings[i][0]).geefWaarde();
				docentDomeinen[i][1] = FormuleParser.geefExpressie(docentDomeinStrings[i][1]).geefWaarde();
				//Hier nog try/catch inbouwen? Geeft deze een exception bij randomvariabelen?
			}
			
		}
		/*
		if(docentDomeinen == null)
			this.docentDomeinen = null;
		else
		{	this.docentDomeinen = new double[docentDomeinen.length][2];
			for(int i = 0; i < docentDomeinen.length; i++)
			{	this.docentDomeinen[i][0] = docentDomeinen[i][0];
				this.docentDomeinen[i][1] = docentDomeinen[i][1];
			}
		}
		*/	
					
		//this.docentGraphPoints = docentGraphPoints;
		int kleinsteMinimum = minimumPunten[0];
		for(int i = 1; i < aantalFuncties; i++)
			if(minimumPunten[i] < kleinsteMinimum)
				kleinsteMinimum = minimumPunten[i];
		this.docentGraphPoints = new Vector();
		for(int i = 0; i < docentGraphPointsX.length; i++)
		{	RealPoint rp = new RealPoint(docentGraphPointsX[i], docentGraphPointsY[i]);
			rp.setIndex(docentGraphPointsIndex[i]);
			rp.setTabelIndex(docentGraphPointsTabelIndex[i]);
			rp.setxString(docentGraphPointsXString[i]);
			rp.setyString(docentGraphPointsYString[i]);
			docentGraphPoints.add(rp);
		}
		this.ingevuld = ingevuld;
		this.nagekeken = nagekeken;
		if((typeOpdracht == TEKENTABELPUNTEN && graphPoints.size() >= docentGraphPoints.size())
				|| (typeOpdracht == TEKENPUNTENBIJFORMULE && graphPoints.size() >= kleinsteMinimum))
			kijkNaButton.setEnabled(true);	
		
		int b = beginwaarde;
		beginwaarde = 1-(int)Math.round(beginx/eenheidx);
		selectnummer = selectnummer + b - beginwaarde;
		
		zetZoomOptie(zoomOptie);
		zetTraceOptie(traceOptie);
		if(traceOptie && tracex != -2) 
			slider.zetStand(tracex);
		tabelComponent.setState(h, false);
		
		zetZoomInTabel(zoomInTabel);
		zetTabelAlsTekenTool(tabelAlsTekenTool, true);
		zetXAsLog(xAsLog);
		zetYAsLog(yAsLog);
		zetXVarEditable(xVarEditable);
		zetYVarEditable(yVarEditable);
		zetKrommeKnoppen(true, krommeZonderExtrapolatie, krommeMetExtrapolatie);
		zetMaxScores(maxScores);
		zetDocentFuncties(docentFuncties);
		//zetDocentDomeinen(docentDomeinen);
		zetDocentDomeinen(docentDomeinStrings);
		zetFormuleEditorOpties(null, true);
		zetTypeOpdracht(typeOpdracht, true);
		
		tekenComponent.setState(h);
		
		formuleComponent.setState(h, null, null, false);
		
		setActiveIndex(activeIndex, true);
		//if(schuifParameters != null)
			//for (int i = 0; i < schuifParameters.length; i++)
			//{	//add(schuifParameters[i].geefSlider(), 0);
				//schuifParameters[i].geefSlider().addActionListener(this);
				//schuifParameters[i].zetWaarde, actie);
			//}
		
		if((mode != 2 && mode != 3) || nagekeken)	kijkNa();
		
	}
	
	public Hashtable getState() {	
			
		if(!("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant))) kijkNa(false);
		
		double beginxDocent = 1;
		double beginyDocent = 1;
		double beginx = 1;
		double beginy = 1;
		double docentSchaalFactorX = 1;
		double docentSchaalFactorY = 1;
		double schaalFactorX  = 1;
		double schaalFactorY  = 1;
		
		//graphPoints:
		double[] graphPointsX = new double[graphPoints.size()];
		double[] graphPointsY = new double[graphPoints.size()];
		int[] graphPointsIndex = new int[graphPoints.size()];
		int[] graphPointsTabelIndex = new int[graphPoints.size()];
		String[] graphPointsXString = new String[graphPoints.size()];
		String[] graphPointsYString = new String[graphPoints.size()];
		
		for(int i = 0; i < graphPoints.size(); i++)
		{	graphPointsX[i] = ((RealPoint) graphPoints.elementAt(i)).getX();
			graphPointsY[i] = ((RealPoint) graphPoints.elementAt(i)).getY();
			graphPointsIndex[i] = ((RealPoint) graphPoints.elementAt(i)).getIndex();
			graphPointsTabelIndex[i] = ((RealPoint) graphPoints.elementAt(i)).getTabelIndex();
			graphPointsXString[i] = ((RealPoint) graphPoints.elementAt(i)).getxString();
			graphPointsYString[i] = ((RealPoint) graphPoints.elementAt(i)).getyString();
		}
		
		int[][] colorRGBsOpdrachten = new int[opdrachtKleuren.length][3];
		for(int i = 0; i < opdrachtKleuren.length; i++)
		{	colorRGBsOpdrachten[i][0] = opdrachtKleuren[i].getRed();
			colorRGBsOpdrachten[i][1] = opdrachtKleuren[i].getGreen();
			colorRGBsOpdrachten[i][2] = opdrachtKleuren[i].getBlue();
		}
		int[][] colorRGBsGewoon = new int[gewoneKleuren.length][3];
		for(int i = 0; i < gewoneKleuren.length; i++)
		{	colorRGBsGewoon[i][0] = gewoneKleuren[i].getRed();
			colorRGBsGewoon[i][1] = gewoneKleuren[i].getGreen();
			colorRGBsGewoon[i][2] = gewoneKleuren[i].getBlue();
		}
		
		//schuifParameters: (gaat dit niet mis als schuifParameters null is?
		String[] paramNamen = new String[schuifParameters.length];
		double[] paramWaarden = new double[schuifParameters.length];
		double[] paramOnderGrensWaarden = new double[schuifParameters.length];
		double[] paramBovenGrensWaarden = new double[schuifParameters.length];
		double[] paramStapGroottes = new double[schuifParameters.length];
		int[] paramLengtes = new int[schuifParameters.length];
		int[] paramX = new int[schuifParameters.length];
		int[] paramY = new int[schuifParameters.length];
		
		for(int i = 0; i < schuifParameters.length; i++)
		{	paramNamen[i] = schuifParameters[i].geefNaam();
			paramWaarden[i] = schuifParameters[i].geefWaarde();
			paramOnderGrensWaarden[i] = schuifParameters[i].geefOnderGrens();
			paramBovenGrensWaarden[i] = schuifParameters[i].geefBovenGrens();
			paramStapGroottes[i] = schuifParameters[i].geefStapGrootte();
			paramLengtes[i] = schuifParameters[i].geefLengte();
			paramX[i] = schuifParameters[i].getX();
			paramY[i] = schuifParameters[i].getY();
		}
		
		//Vector graphPoints = new Vector();
		int activeIndex = 1;
		String grafiekXAsNaam = "x";
		String grafiekYAsNaam = "y";
		String xAsNaam = "x";
		String yAsNaam = "y";
		boolean formuleComponentAan = true;
		boolean tekenComponentAan = false;
		boolean tabelComponentAan = false;
		boolean assenZichtbaar = true;
		boolean roosterZichtbaar = true;
		boolean roosterGrof = false; 
		boolean roosterX = true;
		boolean roosterY = true;
		boolean schaalZichtbaar = true;
		boolean schaalX = true;
		boolean schaalY = true;
		boolean piLijnenZichtbaar = false; 
		boolean zoomOptie = true; 
		boolean traceOptie = true; 
		boolean dragOptie = true; 
		
		boolean zoomInTabel = true;
		boolean tabelAlsTekenTool = false; 
		boolean xPositief = false; 
		boolean yPositief = false; 
		boolean xAsLog = false;
		boolean yAsLog = false;
		boolean xVarEditable = false;
		boolean yVarEditable = false;
		boolean snapToGridPoints = false;
		boolean krommeZonderExtrapolatie = true;
		boolean krommeMetExtrapolatie = true;
		int tekenGrafiekNauwkeurigheid = 5;
		int selectnummer = 999;
		int beginwaarde = 0;
		double tracexD = -2;
		
		boolean grafiekKleuren = true;
		boolean kleurInstelbaar = true;
		boolean functieBeginZichtbaar = true;
		boolean functieBeginAanpasbaar = true;
		boolean formeleFuncties = true;
		boolean domeinInstelbaar = true;
		int formuleComponentHoogte = 120;
		
		boolean functieToegestaan = true;
		boolean ongelijkheidToegestaan = true;
		boolean implicieteFunctieToegestaan = true;
		boolean verticaleLijnToegestaan = true;
		boolean parametrisatieToegestaan = true;
		
		beginxDocent = this.beginxDocent;
		beginyDocent = this.beginyDocent;
		beginx = this.beginx;
		beginy = this.beginy;
		docentSchaalFactorX = this.docentSchaalFactorX;
		docentSchaalFactorY = this.docentSchaalFactorY;
		schaalFactorX = this.schaalFactorX;
		schaalFactorY = this.schaalFactorY;
		//graphPoints = this.graphPoints;
		activeIndex = this.activeIndex;
		grafiekXAsNaam = this.grafiekXAsNaam;
		grafiekYAsNaam = this.grafiekYAsNaam;
		xAsNaam = this.xAsNaam;
		yAsNaam = this.yAsNaam;
		formuleComponentAan = this.formuleComponentAan;
		tekenComponentAan = this.tekenComponentAan;
		tabelComponentAan = this.tabelComponentAan;
		assenZichtbaar = this.assenZichtbaar;
		roosterZichtbaar = this.roosterZichtbaar;
		roosterGrof = this.roosterGrof;
		roosterX = this.roosterX;
		roosterY = this.roosterY;
		schaalZichtbaar = this.schaalZichtbaar;
		schaalX = this.schaalX;
		schaalY = this.schaalY;
		piLijnenZichtbaar = this.piLijnenZichtbaar;
		zoomOptie = this.zoomOptie;
		traceOptie = this.traceOptie;
		dragOptie = this.dragOptie;
		formeleFuncties = this.formeleFuncties;
		domeinInstelbaar = this.domeinInstelbaar;
		zoomInTabel = this.zoomInTabel;
		tabelAlsTekenTool = this.tabelAlsTekenTool;
		xPositief = this.xPositief;
		yPositief = this.yPositief;
		xAsLog = this.xAsLog;
		yAsLog = this.yAsLog;
		xVarEditable = this.xVarEditable;
		yVarEditable = this.yVarEditable;
		grafiekKleuren = this.grafiekKleuren;
		snapToGridPoints = this.snapToGridPoints;
		krommeZonderExtrapolatie = this.krommeZonderExtrapolatie;
		krommeMetExtrapolatie = this.krommeMetExtrapolatie;
		tekenGrafiekNauwkeurigheid = this.tekenGrafiekNauwkeurigheid;
		selectnummer = this.selectnummer;
		beginwaarde = this.beginwaarde;
		tracexD = this.tracexD;
		
		grafiekKleuren = this.grafiekKleuren;
		kleurInstelbaar = this.kleurInstelbaar;
		functieBeginZichtbaar = this.functieBeginZichtbaar;
		functieBeginAanpasbaar = this.functieBeginAanpasbaar;
		formeleFuncties = this.formeleFuncties;
		domeinInstelbaar = this.domeinInstelbaar;
		formuleComponentHoogte = this.formuleComponentHoogte;
		
		functieToegestaan = this.functieToegestaan;
		ongelijkheidToegestaan = this.ongelijkheidToegestaan;
		implicieteFunctieToegestaan = this.implicieteFunctieToegestaan;
		verticaleLijnToegestaan = this.verticaleLijnToegestaan;
		parametrisatieToegestaan = this.parametrisatieToegestaan;
		
		Hashtable h = new Hashtable();
		h = tekenComponent.getState();
		Hashtable h1 = formuleComponent.getState();
		for (Enumeration e = h1.keys(); e.hasMoreElements();)
		{	Object aKey = e.nextElement();
			Object aValue = h1.get(aKey);
			h.put(aKey, aValue);
		}
		Hashtable h2 = tabelComponent.getState();
		for(Enumeration e = h2.keys(); e.hasMoreElements();)
		{	Object aKey = e.nextElement();
			Object aValue = h2.get(aKey);
			h.put(aKey, aValue);
		}
		h.put("beginxDocent", new Double(beginxDocent));
		h.put("beginyDocent", new Double(beginyDocent));
	    h.put("beginx", new Double(beginx));
	    h.put("beginy", new Double(beginy));
	    h.put("docentSchaalFactorX", new Double(docentSchaalFactorX));
	    h.put("docentSchaalFactorY", new Double(docentSchaalFactorY));
	    h.put("schaalFactorX", new Double(schaalFactorX));
	    h.put("schaalFactorY", new Double(schaalFactorY));
	    //h.put("graphPoints", new Vector(graphPoints));
	    h.put("graphPointsX", graphPointsX);
	    h.put("graphPointsY", graphPointsY);
	    h.put("graphPointsIndex", graphPointsIndex);
	    h.put("graphPointsTabelIndex", graphPointsTabelIndex);
	    h.put("graphPointsXString", graphPointsXString);
	    h.put("graphPointsYString", graphPointsYString);
	    h.put("colorRGBsOpdrachten", colorRGBsOpdrachten);
	    h.put("colorRGBsGewoon", colorRGBsGewoon);
	    h.put("paramNamen", paramNamen);
	    h.put("paramWaarden", paramWaarden);
	    h.put("paramOnderGrensWaarden", paramOnderGrensWaarden);
	    h.put("paramBovenGrensWaarden", paramBovenGrensWaarden);
	    h.put("paramStapGroottes", paramStapGroottes);
	    h.put("paramLengtes", paramLengtes);
	    h.put("paramX", paramX);
	    h.put("paramY", paramY);
	    h.put("activeIndex", new Integer(activeIndex));
	    h.put("grafiekXAsNaam", new String(grafiekXAsNaam));
	    h.put("grafiekYAsNaam", new String(grafiekYAsNaam));
		h.put("xAsNaam", xAsNaam);
		h.put("yAsNaam", yAsNaam);
		h.put("formuleComponentAan", new Boolean(formuleComponentAan));
		h.put("tekenComponentAan", new Boolean(tekenComponentAan));
		h.put("tabelComponentAan", new Boolean(tabelComponentAan));
		h.put("assenZichtbaar", new Boolean(assenZichtbaar));
		h.put("roosterZichtbaar", new Boolean(roosterZichtbaar));
		h.put("roosterGrof", new Boolean(roosterGrof));
		h.put("roosterX", new Boolean(roosterX));
		h.put("roosterY", new Boolean(roosterY));
		h.put("schaalZichtbaar", new Boolean(schaalZichtbaar));
		h.put("schaalX", new Boolean(schaalX));
		h.put("schaalY", new Boolean(schaalY));
		h.put("piLijnenZichtbaar", new Boolean(piLijnenZichtbaar));
		h.put("zoomOptie", new Boolean(zoomOptie));
		h.put("traceOptie", new Boolean(traceOptie));
		h.put("dragOptie", new Boolean(dragOptie));
		h.put("zoomInTabel", new Boolean(zoomInTabel));
		h.put("tabelAlsTekenTool", new Boolean(tabelAlsTekenTool));
		h.put("xPositief", new Boolean(xPositief));
		h.put("yPositief", new Boolean(yPositief));
		h.put("xAsLog", new Boolean(xAsLog));
		h.put("yAsLog", new Boolean(yAsLog));
		h.put("xVarEditable", new Boolean(xVarEditable));
		h.put("yVarEditable", new Boolean(yVarEditable));
		h.put("snapToGridPoints", new Boolean(snapToGridPoints));
		h.put("krommeZonderExtrapolatie", new Boolean(krommeZonderExtrapolatie));
		h.put("krommeMetExtrapolatie", new Boolean(krommeMetExtrapolatie));
		h.put("tekenGrafiekNauwkeurigheid", new Integer(tekenGrafiekNauwkeurigheid));
		h.put("selectnummer", new Integer(selectnummer));
		h.put("beginwaarde", new Integer(beginwaarde));
		h.put("tracexD", new Double(tracexD));
		
		h.put("grafiekKleuren", new Boolean(grafiekKleuren));
		h.put("kleurInstelbaar", new Boolean(kleurInstelbaar));
		h.put("functieBeginZichtbaar", new Boolean(functieBeginZichtbaar));
		h.put("functieBeginAanpasbaar", new Boolean(functieBeginAanpasbaar));
		h.put("formeleFuncties", new Boolean(formeleFuncties));
		h.put("domeinInstelbaar", new Boolean(domeinInstelbaar));
		h.put("formuleComponentHoogte", new Integer(formuleComponentHoogte));
		
		h.put("functieToegestaan", new Boolean(functieToegestaan));
		h.put("ongelijkheidToegestaan", new Boolean(ongelijkheidToegestaan));
		h.put("implicieteFunctieToegestaan", new Boolean(implicieteFunctieToegestaan));
		h.put("verticaleLijnToegestaan", new Boolean(verticaleLijnToegestaan));
		h.put("parametrisatieToegestaan", new Boolean(parametrisatieToegestaan));
		
				//Opdrachten
		int typeOpdracht = GEENOPDRACHT;
		int[] nauwkeurigheid = null;
		int[] minimumPunten = null; 
		int[] maxScores = new int[9];
		int scoreMax = 0;
		boolean domeinControleren = false;
		boolean leerlingZietTabel = true;	
		String[] docentFunctieStrings = new String[maxAantalExpressies];
		String[][] docentDomeinStrings = new String[maxAantalExpressies][2];
		//double[][] docentDomeinen = new double[maxAantalExpressies][2];
		//Vector docentGraphPoints = new Vector();
		double[] docentGraphPointsX = new double[docentGraphPoints.size()];
		double[] docentGraphPointsY = new double[docentGraphPoints.size()];
		int[] docentGraphPointsIndex = new int[docentGraphPoints.size()];
		int[] docentGraphPointsTabelIndex = new int[docentGraphPoints.size()];
		String[] docentGraphPointsXString = new String[docentGraphPoints.size()];
		String[] docentGraphPointsYString = new String[docentGraphPoints.size()];
		
		for(int i = 0; i < docentGraphPoints.size(); i++)
		{	docentGraphPointsX[i] = ((RealPoint) docentGraphPoints.elementAt(i)).getX();
			docentGraphPointsY[i] = ((RealPoint) docentGraphPoints.elementAt(i)).getY();
			docentGraphPointsIndex[i] = ((RealPoint) docentGraphPoints.elementAt(i)).getIndex();
			docentGraphPointsTabelIndex[i] = ((RealPoint) docentGraphPoints.elementAt(i)).getTabelIndex();
			docentGraphPointsXString[i] = ((RealPoint) docentGraphPoints.elementAt(i)).getxString();
			docentGraphPointsYString[i] = ((RealPoint) docentGraphPoints.elementAt(i)).getyString();
		}
		boolean ingevuld = true;
		boolean nagekeken = false;
		
		typeOpdracht = this.typeOpdracht;
		nauwkeurigheid = this.nauwkeurigheid;
		minimumPunten = this.minimumPunten;
		if(typeOpdracht == GEENOPDRACHT) 
		{	for(int i = 0; i < maxScores.length; i++) 
			maxScores[i] = 0;
		}
		else maxScores = this.maxScores;	
		zetMaxScores(maxScores);
		scoreMax = this.scoreMax;
		domeinControleren = this.domeinControleren;
		leerlingZietTabel = this.leerlingZietTabel;
		
		docentFunctieStrings = this.docentFunctieStrings;
		docentDomeinStrings = this.docentDomeinStrings;
		
		/*
		if(this.docentDomeinen == null)
			docentDomeinen = null;
		else
		{	docentDomeinen = new double[this.docentDomeinen.length][2];
			for(int i = 0; i < docentDomeinen.length; i++)
			{	docentDomeinen[i][0] = this.docentDomeinen[i][0];
				docentDomeinen[i][1] = this.docentDomeinen[i][1];
			}
		}
		*/
		//docentGraphPoints = this.docentGraphPoints;
		
		nagekeken = this.nagekeken;
		ingevuld = this.ingevuld;	
	    
		h.put("typeOpdracht", new Integer(typeOpdracht));
		h.put("nauwkeurigheid", nauwkeurigheid);
		h.put("minimumPunten", minimumPunten);
		h.put("maxScores", maxScores);
		h.put("scoreMax", new Integer(scoreMax));
		h.put("domeinControleren", new Boolean(domeinControleren));
		h.put("leerlingZietTabel", new Boolean(leerlingZietTabel));
		h.put("docentFunctieStrings", docentFunctieStrings);
		h.put("docentDomeinStrings", docentDomeinStrings);
		//h.put("docentDomeinen", docentDomeinen);
		//h.put("docentGraphPoints", docentGraphPoints);
		h.put("docentGraphPointsX", docentGraphPointsX);
	    h.put("docentGraphPointsY", docentGraphPointsY);
	    h.put("docentGraphPointsIndex", docentGraphPointsIndex);
	    h.put("docentGraphPointsTabelIndex", docentGraphPointsTabelIndex);
	    h.put("docentGraphPointsXString", docentGraphPointsXString);
	    h.put("docentGraphPointsYString", docentGraphPointsYString);
		h.put("ingevuld", new Boolean(ingevuld));
	    h.put("nagekeken", new Boolean(nagekeken));
	   
		return h;
	}
	
	
	public void zetOpdracht(Hashtable h, String[] randomVars, Hashtable randomValues) 
	{	double beginxDocent = 1;
		double beginyDocent = 1;
		double beginx = 1;
		double beginy = 1;
		double docentSchaalFactorX = 1;
		double docentSchaalFactorY = 1;
		double schaalFactorX = 1;
		double schaalFactorY = 1;
		//Vector graphPoints = new Vector();
		//graphPoints
		double[] graphPointsX = null;
		double[] graphPointsY = null;
		int[] graphPointsIndex = null;
		int[] graphPointsTabelIndex = null;
		String[] graphPointsXString = null;
		String[] graphPointsYString = null;
		int[][] colorRGBsOpdrachten = new int[opdrachtKleuren.length][3];
		for(int i = 0; i < opdrachtKleuren.length; i++)
		{	colorRGBsOpdrachten[i][0] = opdrachtKleuren[i].getRed();
			colorRGBsOpdrachten[i][1] = opdrachtKleuren[i].getGreen();
			colorRGBsOpdrachten[i][2] = opdrachtKleuren[i].getBlue();
		}
		int[][] colorRGBsGewoon = new int[gewoneKleuren.length][3];
		for(int i = 0; i < gewoneKleuren.length; i++)
		{	colorRGBsGewoon[i][0] = gewoneKleuren[i].getRed();
			colorRGBsGewoon[i][1] = gewoneKleuren[i].getGreen();
			colorRGBsGewoon[i][2] = gewoneKleuren[i].getBlue();
		}
		
		//schuifParameters
		String[] paramNamen = null;
		double[] paramWaarden = null;
		double[] paramOnderGrensWaarden = null;
		double[] paramBovenGrensWaarden = null;
		double[] paramStapGroottes = null;
		int[] paramLengtes = null;
		int[] paramX = null;
		int[] paramY = null;
		
		int activeIndex = 1;
		String grafiekXAsNaam = "x";
		String grafiekYAsNaam = "y";
		String xAsNaam = "x";
		String yAsNaam = "y";
		boolean formuleComponentAan = true;
		boolean tekenComponentAan = false;
		boolean tabelComponentAan = false;
		boolean assenZichtbaar = true;
		boolean roosterZichtbaar = true;
		boolean roosterGrof = false; 
		boolean roosterX = true;
		boolean roosterY = true;
		boolean schaalZichtbaar = true;
		boolean schaalX = true;
		boolean schaalY = true;
		boolean piLijnenZichtbaar = false; 
		boolean zoomOptie = true; 
		boolean traceOptie = true; 
		boolean dragOptie = true; 
		boolean zoomInTabel = true;
		boolean tabelAlsTekenTool = false; 
		boolean xPositief = false; 
		boolean yPositief = false; 
		boolean xAsLog = false;
		boolean yAsLog = false;
		boolean xVarEditable = false;
		boolean yVarEditable = false;
		boolean snapToGridPoints = false;
		boolean krommeZonderExtrapolatie = true;
		boolean krommeMetExtrapolatie = true;
		int tekenGrafiekNauwkeurigheid = 5;
		int selectnummer = 999;
		int beginwaarde = 0;
		double tracexD = -2;
		
		boolean grafiekKleuren = true;
		boolean kleurInstelbaar = true;
		boolean functieBeginZichtbaar = true;
		boolean functieBeginAanpasbaar = true;
		boolean formeleFuncties = true;
		boolean domeinInstelbaar = true;
		int formuleComponentHoogte = 120;
		
		boolean functieToegestaan = true;
		boolean ongelijkheidToegestaan = true;
		boolean implicieteFunctieToegestaan = true;
		boolean verticaleLijnToegestaan = true;
		boolean parametrisatieToegestaan = true;
		
		if(h.containsKey("beginxDocent"))
	    	beginxDocent = ((Double)h.get("beginxDocent")).doubleValue();
    	if(h.containsKey("beginyDocent"))
    		beginyDocent = ((Double)h.get("beginyDocent")).doubleValue();
    	if(h.containsKey("beginx")) 
    		beginx = ((Double)h.get("beginx")).doubleValue();
    	if(h.containsKey("beginy")) 
    		beginy = ((Double)h.get("beginy")).doubleValue();
    	if(h.containsKey("docentSchaalFactorX"))
    		docentSchaalFactorX = ((Double)h.get("docentSchaalFactorX")).doubleValue();
    	if(h.containsKey("docentSchaalFactorY"))
    		docentSchaalFactorY = ((Double)h.get("docentSchaalFactorY")).doubleValue();
    	if(h.containsKey("schaalFactorX")) 
    		schaalFactorX = ((Double)h.get("schaalFactorX")).doubleValue();
    	if(h.containsKey("schaalFactorY")) 
    		schaalFactorY = ((Double)h.get("schaalFactorY")).doubleValue();
    	//if(h.containsKey("graphPoints"))
    	//	graphPoints = (Vector)h.get("graphPoints");
    	if(h.containsKey("graphPointsX"))
    		graphPointsX = ((double[])h.get("graphPointsX"));
    	if(h.containsKey("graphPointsY"))
    		graphPointsY = ((double[])h.get("graphPointsY"));
    	if(h.containsKey("graphPointsIndex"))
    		graphPointsIndex = ((int[])h.get("graphPointsIndex"));
    	if(h.containsKey("graphPointsTabelIndex"))
    		graphPointsTabelIndex = ((int[])h.get("graphPointsTabelIndex"));
    	if(h.containsKey("graphPointsXString"))
    		graphPointsXString = ((String[])h.get("graphPointsXString"));
    	if(h.containsKey("graphPointsYString"))
    		graphPointsYString = ((String[])h.get("graphPointsYString"));
    	if(h.containsKey("colorRGBsOpdrachten"))
    		colorRGBsOpdrachten = ((int[][])h.get("colorRGBsOpdrachten"));
    	if(h.containsKey("colorRGBsGewoon"))
    		colorRGBsGewoon = ((int[][])h.get("colorRGBsGewoon"));
    	else if(h.containsKey("colorRGBs"))
    		colorRGBsGewoon = ((int[][])h.get("colorRGBs"));
    	if(h.containsKey("paramNamen"))
    		paramNamen = ((String[])h.get("paramNamen"));
    	if(h.containsKey("paramWaarden"))
    		paramWaarden = ((double[])h.get("paramWaarden"));
    	if(h.containsKey("paramOnderGrensWaarden"))
    		paramOnderGrensWaarden = ((double[])h.get("paramOnderGrensWaarden"));
    	if(h.containsKey("paramBovenGrensWaarden"))
    		paramBovenGrensWaarden = ((double[])h.get("paramBovenGrensWaarden"));
    	if(h.containsKey("paramStapGroottes"))
    		paramStapGroottes = ((double[])h.get("paramStapGroottes"));
    	if(h.containsKey("paramLengtes"))
    		paramLengtes = ((int[])h.get("paramLengtes"));
    	if(h.containsKey("paramX"))
    		paramX = ((int[])h.get("paramX"));
    	if(h.containsKey("paramY"))
    		paramY = ((int[])h.get("paramY"));
    	
    	if(h.containsKey("activeIndex"))
    		activeIndex = ((Integer)h.get("activeIndex")).intValue();
    	if(h.containsKey("grafiekXAsNaam"))
    		grafiekXAsNaam = ((String)h.get("grafiekXAsNaam"));
    	if(h.containsKey("grafiekYAsNaam"))
    		grafiekYAsNaam = ((String)h.get("grafiekYAsNaam"));
		if (h.containsKey("xAsNaam")) 
			xAsNaam = (String) h.get("xAsNaam");
		if (h.containsKey("yAsNaam")) 
			yAsNaam = (String) h.get("yAsNaam");
		if (h.containsKey("formuleComponentAan")) 
			formuleComponentAan = ((Boolean) h.get("formuleComponentAan")).booleanValue();
		if (h.containsKey("tekenComponentAan")) 
			tekenComponentAan = ((Boolean) h.get("tekenComponentAan")).booleanValue();
		if (h.containsKey("tabelComponentAan")) 
			tabelComponentAan = ((Boolean) h.get("tabelComponentAan")).booleanValue();
		if (h.containsKey("assenZichtbaar")) 
			assenZichtbaar = ((Boolean) h.get("assenZichtbaar")).booleanValue();
		if (h.containsKey("roosterZichtbaar")) 
			roosterZichtbaar = ((Boolean) h.get("roosterZichtbaar")).booleanValue();
		if (h.containsKey("roosterGrof")) 
			roosterGrof = ((Boolean) h.get("roosterGrof")).booleanValue();
		if (h.containsKey("roosterX"))
			roosterX = ((Boolean) h.get("roosterX")).booleanValue();
		if (h.containsKey("roosterY"))
			roosterY = ((Boolean) h.get("roosterY")).booleanValue();
		if (h.containsKey("schaalZichtbaar")) 
			schaalZichtbaar = ((Boolean) h.get("schaalZichtbaar")).booleanValue();
		if (h.containsKey("schaalX"))
			schaalX = ((Boolean) h.get("schaalX")).booleanValue();
		if (h.containsKey("schaalY"))
			schaalY = ((Boolean) h.get("schaalY")).booleanValue();
		if (h.containsKey("piLijnenZichtbaar")) 
			piLijnenZichtbaar = ((Boolean) h.get("piLijnenZichtbaar")).booleanValue();
		if (h.containsKey("zoomOptie")) 
			zoomOptie = ((Boolean) h.get("zoomOptie")).booleanValue();
		if (h.containsKey("traceOptie")) 
			traceOptie = ((Boolean) h.get("traceOptie")).booleanValue();
		if (h.containsKey("dragOptie")) 
			dragOptie = ((Boolean) h.get("dragOptie")).booleanValue();
		if (h.containsKey("zoomInTabel")) 
			zoomInTabel = ((Boolean) h.get("zoomInTabel")).booleanValue();
		if (h.containsKey("tabelAlsTekenTool")) 
			tabelAlsTekenTool = ((Boolean) h.get("tabelAlsTekenTool")).booleanValue();
		if (h.containsKey("xPositief")) 
			xPositief = ((Boolean) h.get("xPositief")).booleanValue();
		if (h.containsKey("yPositief")) 
			yPositief = ((Boolean) h.get("yPositief")).booleanValue();
		if (h.containsKey("xAsLog")) 
			xAsLog = ((Boolean) h.get("xAsLog")).booleanValue();
		if (h.containsKey("yAsLog")) 
			yAsLog = ((Boolean) h.get("yAsLog")).booleanValue();
		if (h.containsKey("xVarEditable")) 
			xVarEditable = ((Boolean) h.get("xVarEditable")).booleanValue();
		if (h.containsKey("yVarEditable")) 
			yVarEditable = ((Boolean) h.get("yVarEditable")).booleanValue();
		if (h.containsKey("snapToGridPoints"))
			snapToGridPoints = ((Boolean) h.get("snapToGridPoints")).booleanValue();
		if (h.containsKey("krommeZonderExtrapolatie"))
			krommeZonderExtrapolatie = ((Boolean) h.get("krommeZonderExtrapolatie")).booleanValue();
		if (h.containsKey("krommeMetExtrapolatie"))
			krommeMetExtrapolatie = ((Boolean) h.get("krommeMetExtrapolatie")).booleanValue();
		if (h.containsKey("tekenGrafiekNauwkeurigheid"))
			tekenGrafiekNauwkeurigheid = ((Integer) h.get("tekenGrafiekNauwkeurigheid")).intValue();
		if (h.containsKey("selectnummer"))
			selectnummer = ((Integer) h.get("selectnummer")).intValue();
		if (h.containsKey("beginwaarde"))
			beginwaarde = ((Integer) h.get("beginwaarde")).intValue();
		if (h.containsKey("tracexD"))
			tracexD = ((Double) h.get("tracexD")).doubleValue();
		
		if(h.containsKey("grafiekKleuren"))
			grafiekKleuren = ((Boolean)h.get("grafiekKleuren")).booleanValue();
		if(h.containsKey("kleurInstelbaar"))
			kleurInstelbaar = ((Boolean)h.get("kleurInstelbaar")).booleanValue();
		if(h.containsKey("functieBeginZichtbaar"))
			functieBeginZichtbaar = ((Boolean)h.get("functieBeginZichtbaar")).booleanValue();
		if(h.containsKey("functieBeginAanpasbaar"))
			functieBeginAanpasbaar = ((Boolean)h.get("functieBeginAanpasbaar")).booleanValue();
		if(h.containsKey("formeleFuncties"))
			formeleFuncties = ((Boolean)h.get("formeleFuncties")).booleanValue();
		if(h.containsKey("domeinInstelbaar"))
			domeinInstelbaar = ((Boolean)h.get("domeinInstelbaar")).booleanValue();
		if(h.containsKey("formuleComponentHoogte"))
			formuleComponentHoogte = ((Integer)h.get("formuleComponentHoogte")).intValue();
		
		if(h.containsKey("functieToegestaan"))
			functieToegestaan = ((Boolean)h.get("functieToegestaan")).booleanValue();
		if(h.containsKey("ongelijkheidToegestaan"))
			ongelijkheidToegestaan = ((Boolean)h.get("ongelijkheidToegestaan")).booleanValue();
		if(h.containsKey("implicieteFunctieToegestaan"))
			implicieteFunctieToegestaan = ((Boolean)h.get("implicieteFunctieToegestaan")).booleanValue();
		if(h.containsKey("verticaleLijnToegestaan"))
			verticaleLijnToegestaan = ((Boolean)h.get("verticaleLijnToegestaan")).booleanValue();
		if(h.containsKey("parametrisatieToegestaan"))
			parametrisatieToegestaan = ((Boolean)h.get("parametrisatieToegestaan")).booleanValue();
		
		this.beginxDocent = beginxDocent;
		this.beginyDocent = beginyDocent;
    	this.beginx = beginx;
		this.beginy = beginy;
		this.docentSchaalFactorX = docentSchaalFactorX;
		this.docentSchaalFactorY = docentSchaalFactorY;
		this.schaalFactorX = schaalFactorX;
		this.schaalFactorY = schaalFactorY;
		this.graphPoints = new Vector();
		for(int i = 0; i < graphPointsX.length; i++)
		{	RealPoint rp = new RealPoint(graphPointsX[i], graphPointsY[i]);
			rp.setIndex(graphPointsIndex[i]);
			rp.setTabelIndex(graphPointsTabelIndex[i]);
			rp.setxString(graphPointsXString[i]);
			rp.setyString(graphPointsYString[i]);
			graphPoints.add(rp);
		}
		for(int i = 0; i < colorRGBsGewoon.length; i++)
		{	//colors[i] = new Color(colorRGBs[i][0], colorRGBs[i][1], colorRGBs[i][2]);
			gewoneKleuren[i] = new Color(colorRGBsGewoon[i][0], colorRGBsGewoon[i][1], colorRGBsGewoon[i][2]);
		}
		for(int i = 0; i < colorRGBsOpdrachten.length; i++)
		{	//colors[i] = new Color(colorRGBs[i][0], colorRGBs[i][1], colorRGBs[i][2]);
			opdrachtKleuren[i] = new Color(colorRGBsOpdrachten[i][0], colorRGBsOpdrachten[i][1], colorRGBsOpdrachten[i][2]);
			//System.out.println("zijn opdrachtkleuren rood geworden?");
			//System.out.println("opdrachtKleuren[" + i + "]=" + colorRGBsOpdrachten[i][0] + ", " + colorRGBsOpdrachten[i][1] + ", " + colorRGBsOpdrachten[i][2]);
		}
		if(paramNamen != null)
		{	this.schuifParameters = new SchuifParameter[paramNamen.length];
			for(int i = 0; i < schuifParameters.length; i++)
			{	schuifParameters[i] = new SchuifParameter(paramLengtes[i], paramNamen[i]);
				schuifParameters[i].zetGrensWaarden(paramOnderGrensWaarden[i], paramBovenGrensWaarden[i]);
				schuifParameters[i].zetStapGrootte(paramStapGroottes[i]);
				schuifParameters[i].zetWaarde(paramWaarden[i], false);
				schuifParameters[i].zetLocatie(paramX[i], paramY[i]);
			}
		}
		
		this.activeIndex = activeIndex;
		this.xAsNaam = xAsNaam;
		this.yAsNaam = yAsNaam;
		this.formuleComponentAan = formuleComponentAan;
		this.tekenComponentAan = tekenComponentAan;
		this.tabelComponentAan = tabelComponentAan;
		this.assenZichtbaar = assenZichtbaar;
		this.roosterZichtbaar = roosterZichtbaar;
		this.roosterGrof = roosterGrof; 
		this.roosterX = roosterX;
		this.roosterY = roosterY;
		this.schaalZichtbaar = schaalZichtbaar;
		this.schaalX = schaalX;
		this.schaalY = schaalY;
		this.piLijnenZichtbaar = piLijnenZichtbaar; 
		this.zoomOptie = zoomOptie; 
		this.traceOptie = traceOptie; 
		this.dragOptie = dragOptie; 
		this.zoomInTabel = zoomInTabel;
		this.tabelAlsTekenTool = tabelAlsTekenTool; 
		this.xPositief = xPositief; 
		this.yPositief = yPositief; 
		this.xVarEditable = xVarEditable;
		this.yVarEditable = yVarEditable;
		this.snapToGridPoints = snapToGridPoints;
		this.krommeZonderExtrapolatie = krommeZonderExtrapolatie;
		this.krommeMetExtrapolatie = krommeMetExtrapolatie;
		this.tekenGrafiekNauwkeurigheid = tekenGrafiekNauwkeurigheid;
		this.selectnummer = selectnummer;
		this.beginwaarde = beginwaarde;
		this.tracexD = tracexD;
		tracex = (int) Math.round(tracexD);
		
		this.grafiekKleuren = grafiekKleuren;
		this.kleurInstelbaar = kleurInstelbaar;
		this.functieBeginZichtbaar = functieBeginZichtbaar;
		this.functieBeginAanpasbaar = functieBeginAanpasbaar;
		this.formeleFuncties = formeleFuncties;
		this.domeinInstelbaar = domeinInstelbaar;
		this.formuleComponentHoogte = formuleComponentHoogte;
		
		this.functieToegestaan = functieToegestaan;
		this.ongelijkheidToegestaan = ongelijkheidToegestaan;
		this.implicieteFunctieToegestaan = implicieteFunctieToegestaan;
		this.verticaleLijnToegestaan = verticaleLijnToegestaan;
		this.parametrisatieToegestaan = parametrisatieToegestaan;
		
		zetXAsNaam(xAsNaam, true);
		zetYAsNaam(yAsNaam, true);
		this.grafiekXAsNaam = grafiekXAsNaam;
		this.grafiekYAsNaam = grafiekYAsNaam;
		xAsNaamTF.setText(grafiekXAsNaam);
		yAsNaamTF.setText(grafiekYAsNaam);
		
		
		// opdrachten
		int typeOpdracht = GEENOPDRACHT;
		int[] maxScores = new int[9];
		int[] nauwkeurigheid = new int[3];
		int[] minimumPunten = new int[3];
		int scoreMax = 0;
		boolean domeinControleren = false;
		boolean leerlingZietTabel = true;
		//Expressie[] docentFuncties = null;
		String[] docentFunctieStrings = new String[maxAantalExpressies];
		//double[][] docentDomeinen = new double[maxAantalExpressies][2];
		String[][] docentDomeinStrings = new String[maxAantalExpressies][2];
		//Vector docentGraphPoints = new Vector();
		double[] docentGraphPointsX = null;
		double[] docentGraphPointsY = null;
		int[] docentGraphPointsIndex = null;
		int[] docentGraphPointsTabelIndex = null;
		String[] docentGraphPointsXString = null;
		String[] docentGraphPointsYString = null;
				
		if (h.containsKey("typeOpdracht")) 
			typeOpdracht = ((Integer) h.get("typeOpdracht")).intValue();		
		if (h.containsKey("maxScores")) 
			maxScores = (int[]) h.get("maxScores");		
		if (h.containsKey("docentFunctieStrings")) 
			docentFunctieStrings = (String[]) h.get("docentFunctieStrings");	
		//if (h.containsKey("docentDomeinen"))
		//	docentDomeinen = (double[][]) h.get("docentDomeinen");
		if(h.containsKey("docentDomeinStrings"))
			docentDomeinStrings = (String[][]) h.get("docentDomeinStrings");
		//if (h.containsKey("docentGraphPoints")) 
		//docentGraphPoints = (Vector) h.get("docentGraphPoints");		
		if(h.containsKey("docentGraphPointsX"))
			docentGraphPointsX = ((double[])h.get("docentGraphPointsX"));
		if(h.containsKey("docentGraphPointsY"))
			docentGraphPointsY = ((double[])h.get("docentGraphPointsY"));
		if(h.containsKey("docentGraphPointsIndex"))
			docentGraphPointsIndex = ((int[])h.get("docentGraphPointsIndex"));
		if(h.containsKey("docentGraphPointsTabelIndex"))
			docentGraphPointsTabelIndex = ((int[])h.get("docentGraphPointsTabelIndex"));
		if(h.containsKey("docentGraphPointsXString"))
			docentGraphPointsXString = ((String[])h.get("docentGraphPointsXString"));
		if(h.containsKey("docentGraphPointsYString"))
			docentGraphPointsYString = ((String[])h.get("docentGraphPointsYString"));
		if (h.containsKey("nauwkeurigheid")) 
			nauwkeurigheid = (int[]) h.get("nauwkeurigheid");		
		if (h.containsKey("minimumPunten")) 
			minimumPunten = (int[]) h.get("minimumPunten");	
		if (h.containsKey("scoreMax"))
			scoreMax = ((Integer) h.get("scoreMax")).intValue();
		if (h.containsKey("domeinControleren"))
			domeinControleren = ((Boolean) h.get("domeinControleren")).booleanValue();
		if (h.containsKey("leerlingZietTabel")) 
			leerlingZietTabel = ((Boolean) h.get("leerlingZietTabel")).booleanValue();		
		
		this.typeOpdracht = typeOpdracht;
		this.maxScores = maxScores;	
		this.nauwkeurigheid = nauwkeurigheid;
		this.minimumPunten = minimumPunten; 
		this.scoreMax = scoreMax;
		this.domeinControleren = domeinControleren;
		this.leerlingZietTabel = leerlingZietTabel;
		this.docentFunctieStrings = docentFunctieStrings;
		if(docentFunctieStrings != null)
		{	docentFuncties = new Expressie[docentFunctieStrings.length];
			for(int i = 0; i < docentFunctieStrings.length; i++)
			{	try         
	        	{   docentFunctieStrings[i] = FormuleParser.randomizeString(docentFunctieStrings[i],randomVars,randomValues);
	        	}
	        	catch(Exception e)
	        	{   docentFunctieStrings[i] = "$f???@";
	        	}
				if (!docentFunctieStrings[i].equals("$f@"))
				{	docentFuncties[i] = FormuleParser.geefExpressie(docentFunctieStrings[i]);
				}
			}
		}
		this.docentDomeinStrings = docentDomeinStrings;
		if(docentDomeinStrings != null)
		{	docentDomeinen = new double[docentDomeinStrings.length][2];
			for(int i = 0; i < docentDomeinStrings.length; i++)
			{	try
				{	docentDomeinStrings[i][0] = FormuleParser.randomizeString(docentDomeinStrings[i][0], randomVars, randomValues);
				}
				catch(Exception e)
				{	docentDomeinStrings[i][0] = "$f" + Double.toString(DEFAULTDOMEIN[0]) + "@";
				}
				try
				{	docentDomeinStrings[i][1] = FormuleParser.randomizeString(docentDomeinStrings[i][1], randomVars, randomValues);
				}
				catch(Exception e)
				{	docentDomeinStrings[i][1] = "$f" + Double.toString(DEFAULTDOMEIN[1]) + "@";
				}
				if(docentDomeinStrings[i][0].equals("$f" + Double.toString(DEFAULTDOMEIN[0]) + "@"))
				{	docentDomeinen[i][0] = DEFAULTDOMEIN[0];
					
				}
				else
					docentDomeinen[i][0] = FormuleParser.geefExpressie(docentDomeinStrings[i][0]).geefWaarde();
				if(docentDomeinStrings[i][1].equals("$f" + Double.toString(DEFAULTDOMEIN[1]) + "@"))
					docentDomeinen[i][1] = DEFAULTDOMEIN[1];
				else
					docentDomeinen[i][1] = FormuleParser.geefExpressie(docentDomeinStrings[i][1]).geefWaarde();
				
			}
		}
		// anders blijft docentFunctie null	
		//this.docentFuncties = docentFuncties;	
		updateAantalFuncties();
		/*
		if(docentDomeinen == null)
			this.docentDomeinen = null;
		else
		{	this.docentDomeinen = new double[docentDomeinen.length][2];
			for(int i = 0; i < docentDomeinen.length; i++)
			{	this.docentDomeinen[i][0] = docentDomeinen[i][0];
				this.docentDomeinen[i][1] = docentDomeinen[i][1];
			}
		}
		*/
		//this.docentGraphPoints = docentGraphPoints;
		this.docentGraphPoints = new Vector();
		for(int i = 0; i < docentGraphPointsX.length; i++)
		{	RealPoint rp = new RealPoint(docentGraphPointsX[i], docentGraphPointsY[i]);
			rp.setIndex(docentGraphPointsIndex[i]);
			rp.setTabelIndex(docentGraphPointsTabelIndex[i]);
			rp.setxString(docentGraphPointsXString[i]);
			rp.setyString(docentGraphPointsYString[i]);
			docentGraphPoints.add(rp);
		}
		
		if(randomVars != null)
		{	for (int pCnt = 0; pCnt < graphPoints.size(); pCnt++)
			{	RealPoint rp = (RealPoint) graphPoints.elementAt(pCnt);
				String xString = rp.getxString();
				String yString = rp.getyString();
										
				//vervangen door:
				try 
				{	xString = FormuleParser.randomizeString("$f" + xString + "@", randomVars, randomValues);
				}
				catch(Exception e)
				{	xString = "";
				}
				System.out.println("xString in randomParsen: " + xString);
				boolean xParam = false;
				if(schuifParameters != null)
				{	for(int i = 0; i < schuifParameters.length; i++)
					{	if(xString.equals("$f" + schuifParameters[i].geefNaam() + "@"))
						{	rp.setX(schuifParameters[i].geefWaarde());
							xParam = true;
						}
					
					}
					
				}
				if(!xParam)
				{	Expressie ex = FormuleParser.geefExpressie(xString);
					if (ex != null)
						rp.setX(ex.geefWaarde());
				}
				try 
				{	yString = FormuleParser.randomizeString("$f" + yString + "@", randomVars, randomValues);
				}
				catch(Exception e)
				{	yString = "";
				}
				boolean yParam = false;
				if(schuifParameters != null)
				{	for(int i = 0; i < schuifParameters.length; i++)
					{	if(yString.equals("$f" + schuifParameters[i].geefNaam() + "@"))
						{	rp.setY(schuifParameters[i].geefWaarde());
							yParam = true;
						}
					}
				}
				if(!yParam)
				{	Expressie ey = FormuleParser.geefExpressie(yString);
					if (ey != null)
						rp.setY(ey.geefWaarde());
				}
				// just in case
				if (Double.isNaN(rp.getX()))
					rp.setX(0);
				if (Double.isNaN(rp.getY()))
					rp.setY(0);
				
				rp.setIndex(rp.getIndex() % 100);
			}
		}
				
		if(randomVars != null)
    	{	for (int pCnt = 0; pCnt < docentGraphPoints.size(); pCnt++)
			{	RealPoint rp = (RealPoint) docentGraphPoints.elementAt(pCnt);
				String xString = rp.getxString();
				String yString = rp.getyString();
								
				//vervangen door:
				try 
				{	xString = FormuleParser.randomizeString("$f" + xString + "@", randomVars, randomValues);
				
				}
				catch(Exception e)
				{	xString = "";
				}
				Expressie ex = FormuleParser.geefExpressie(xString);
				if (ex != null)
					rp.setX(ex.geefWaarde());
				try 
				{	yString = FormuleParser.randomizeString("$f" + yString + "@", randomVars, randomValues);
				}
				catch(Exception e)
				{	yString = "";
				}
				Expressie ey = FormuleParser.geefExpressie(yString);
				if (ey != null)
					rp.setY(ey.geefWaarde());
				
				// just in case
				if (Double.isNaN(rp.getX()))
					rp.setX(0);
				if (Double.isNaN(rp.getY()))
					rp.setY(0);
				
				rp.setIndex(rp.getIndex() % 100);
			}
    	}
		
		zetZoomOptie(zoomOptie);
		zetTraceOptie(traceOptie);
		if(traceOptie && tracex != -2)
			slider.zetStand(tracex);
		
		tabelComponent.setState(h, false);
		
		zetZoomInTabel(zoomInTabel);
		zetTabelAlsTekenTool(tabelAlsTekenTool, true);
		zetXAsLog(xAsLog);
		zetYAsLog(yAsLog);
		zetXVarEditable(xVarEditable);
		zetYVarEditable(yVarEditable);
		zetKrommeKnoppen(true, krommeZonderExtrapolatie, krommeMetExtrapolatie);
		
		zetMaxScores(maxScores);
		zetDocentFuncties(docentFuncties);
		//zetDocentDomeinen(docentDomeinen)		
		zetDocentDomeinen(docentDomeinStrings);
		zetFormuleEditorOpties(null, true);
		zetTypeOpdracht(typeOpdracht, true);
		if(typeOpdracht == VINDFORMULEBIJPUNTEN && randomVars != null)
			for (int pCnt = 0; pCnt < docentGraphPoints.size(); pCnt++)
			{	RealPoint rp = (RealPoint) docentGraphPoints.elementAt(pCnt);
				removePoint(rp.getTabelIndex(), rp.getIndex(), true);
				rp.setY(docentFuncties[0].geefWaarde(rp.getX()));
				rp.setyString(Double.toString(rp.getY()));
				addInsert(rp, true);
			}
				
		tekenComponent.setState(h);
		formuleComponent.setState(h, randomVars, randomValues, false);
			
		setActiveIndex(activeIndex, true);
		if(schuifParameters != null)
			for (int i = 0; i < schuifParameters.length; i++)
			{	add(schuifParameters[i].geefSlider(), 0);
				schuifParameters[i].geefSlider().addActionListener(this);
			}
	}

	public void setEditState(Hashtable h) {
		double beginxDocent = 1;
		double beginyDocent = 1;
		double beginx = 1;
		double beginy = 1;
		double docentSchaalFactorX = 1;
		double docentSchaalFactorY = 1;
		double schaalFactorX = 1;
		double schaalFactorY = 1;
		//Vector graphPoints = new Vector();
		//graphPoints
		double[] graphPointsX = null;
		double[] graphPointsY = null;
		int[] graphPointsIndex = null;
		int[] graphPointsTabelIndex = null;
		String[] graphPointsXString = null;
		String[] graphPointsYString = null;
		int[][] colorRGBsOpdrachten = new int[opdrachtKleuren.length][3];
		for(int i = 0; i < opdrachtKleuren.length; i++)
		{	colorRGBsOpdrachten[i][0] = opdrachtKleuren[i].getRed();
			colorRGBsOpdrachten[i][1] = opdrachtKleuren[i].getGreen();
			colorRGBsOpdrachten[i][2] = opdrachtKleuren[i].getBlue();
		}
		int[][] colorRGBsGewoon = new int[gewoneKleuren.length][3];
		for(int i = 0; i < gewoneKleuren.length; i++)
		{	colorRGBsGewoon[i][0] = gewoneKleuren[i].getRed();
			colorRGBsGewoon[i][1] = gewoneKleuren[i].getGreen();
			colorRGBsGewoon[i][2] = gewoneKleuren[i].getBlue();
		}
		
		//schuifParameters
		String[] paramNamen = null;
		double[] paramWaarden = null;
		double[] paramOnderGrensWaarden = null;
		double[] paramBovenGrensWaarden = null;
		double[] paramStapGroottes = null;
		int[] paramLengtes = null;
		int[] paramX = null;
		int[] paramY = null;
		
		int activeIndex = 1;
		String grafiekXAsNaam = "x";
		String grafiekYAsNaam = "y";
		String xAsNaam = "x";
		String yAsNaam = "y";
		boolean formuleComponentAan = true;
		boolean tekenComponentAan = false;
		boolean tabelComponentAan = false;
		boolean assenZichtbaar = true;
		boolean roosterZichtbaar = true;
		boolean roosterGrof = false; 
		boolean roosterX = true;
		boolean roosterY = true;
		boolean schaalZichtbaar = true;
		boolean schaalX = true;
		boolean schaalY = true;
		boolean piLijnenZichtbaar = false; 
		boolean zoomOptie = true; 
		boolean traceOptie = true; 
		boolean dragOptie = true; 
		boolean zoomInTabel = true;
		boolean tabelAlsTekenTool = false; 
		boolean xPositief = false; 
		boolean yPositief = false; 
		boolean xAsLog = false;
		boolean yAsLog = false;
		boolean xVarEditable = false;
		boolean yVarEditable = false;
		boolean snapToGridPoints = false;
		boolean krommeZonderExtrapolatie = true;
		boolean krommeMetExtrapolatie = true;
		int tekenGrafiekNauwkeurigheid = 5;
		int selectnummer = 999;
		int beginwaarde = 0;
		double tracexD = -2;
		
		boolean grafiekKleuren = true;
		boolean kleurInstelbaar = true;
		boolean functieBeginZichtbaar = true;
		boolean functieBeginAanpasbaar = true;
		boolean formeleFuncties = true;
		boolean domeinInstelbaar = true;
		int formuleComponentHoogte = 120;
		
		boolean functieToegestaan = true;
		boolean ongelijkheidToegestaan = true;
		boolean implicieteFunctieToegestaan = true;
		boolean verticaleLijnToegestaan = true;
		boolean parametrisatieToegestaan = true;
		
		if(h.containsKey("beginxDocent"))
	    	beginxDocent = ((Double)h.get("beginxDocent")).doubleValue();
    	if(h.containsKey("beginyDocent"))
    		beginyDocent = ((Double)h.get("beginyDocent")).doubleValue();
    	if(h.containsKey("beginx")) 
    		beginx = ((Double)h.get("beginx")).doubleValue();
    	if(h.containsKey("beginy")) 
    		beginy = ((Double)h.get("beginy")).doubleValue();
    	if(h.containsKey("docentSchaalFactorX"))
    		docentSchaalFactorX = ((Double)h.get("docentSchaalFactorX")).doubleValue();
    	if(h.containsKey("docentSchaalFactorY"))
    		docentSchaalFactorY = ((Double)h.get("docentSchaalFactorY")).doubleValue();
    	if(h.containsKey("schaalFactorX")) 
    		schaalFactorX = ((Double)h.get("schaalFactorX")).doubleValue();
    	if(h.containsKey("schaalFactorY")) 
    		schaalFactorY = ((Double)h.get("schaalFactorY")).doubleValue();
    	//if(h.containsKey("graphPoints"))
    	//	graphPoints = (Vector)h.get("graphPoints");
    	if(h.containsKey("graphPointsX"))
    		graphPointsX = ((double[])h.get("graphPointsX"));
    	if(h.containsKey("graphPointsY"))
    		graphPointsY = ((double[])h.get("graphPointsY"));
    	if(h.containsKey("graphPointsIndex"))
    		graphPointsIndex = ((int[])h.get("graphPointsIndex"));
    	if(h.containsKey("graphPointsTabelIndex"))
    		graphPointsTabelIndex = ((int[])h.get("graphPointsTabelIndex"));
    	if(h.containsKey("graphPointsXString"))
    		graphPointsXString = ((String[])h.get("graphPointsXString"));
    	if(h.containsKey("graphPointsYString"))
    		graphPointsYString = ((String[])h.get("graphPointsYString"));
    	if(h.containsKey("colorRGBsOpdrachten"))
    		colorRGBsOpdrachten = ((int[][])h.get("colorRGBsOpdrachten"));
    	if(h.containsKey("colorRGBsGewoon"))
    		colorRGBsGewoon = ((int[][])h.get("colorRGBsGewoon"));
    	else if(h.containsKey("colorRGBs"))
    		colorRGBsGewoon = ((int[][])h.get("colorRGBs"));
    	if(h.containsKey("paramNamen"))
    		paramNamen = ((String[])h.get("paramNamen"));
    	if(h.containsKey("paramWaarden"))
    		paramWaarden = ((double[])h.get("paramWaarden"));
    	if(h.containsKey("paramOnderGrensWaarden"))
    		paramOnderGrensWaarden = ((double[])h.get("paramOnderGrensWaarden"));
    	if(h.containsKey("paramBovenGrensWaarden"))
    		paramBovenGrensWaarden = ((double[])h.get("paramBovenGrensWaarden"));
    	if(h.containsKey("paramStapGroottes"))
    		paramStapGroottes = ((double[])h.get("paramStapGroottes"));
    	if(h.containsKey("paramLengtes"))
    		paramLengtes = ((int[])h.get("paramLengtes"));
    	if(h.containsKey("paramX"))
    		paramX = ((int[])h.get("paramX"));
    	if(h.containsKey("paramY"))
    		paramY = ((int[])h.get("paramY"));
    	
    	if(h.containsKey("activeIndex"))
    		activeIndex = ((Integer)h.get("activeIndex")).intValue();
    	if(h.containsKey("grafiekXAsNaam"))
    		grafiekXAsNaam = ((String)h.get("grafiekXAsNaam"));
    	if(h.containsKey("grafiekYAsNaam"))
    		grafiekYAsNaam = ((String)h.get("grafiekYAsNaam"));
		if (h.containsKey("xAsNaam")) 
			xAsNaam = (String) h.get("xAsNaam");
		if (h.containsKey("yAsNaam")) 
			yAsNaam = (String) h.get("yAsNaam");
		if (h.containsKey("formuleComponentAan")) 
			formuleComponentAan = ((Boolean) h.get("formuleComponentAan")).booleanValue();
		if (h.containsKey("tekenComponentAan")) 
			tekenComponentAan = ((Boolean) h.get("tekenComponentAan")).booleanValue();
		if (h.containsKey("tabelComponentAan")) 
			tabelComponentAan = ((Boolean) h.get("tabelComponentAan")).booleanValue();
		if (h.containsKey("assenZichtbaar")) 
			assenZichtbaar = ((Boolean) h.get("assenZichtbaar")).booleanValue();
		if (h.containsKey("roosterZichtbaar")) 
			roosterZichtbaar = ((Boolean) h.get("roosterZichtbaar")).booleanValue();
		if (h.containsKey("roosterGrof")) 
			roosterGrof = ((Boolean) h.get("roosterGrof")).booleanValue();
		if (h.containsKey("roosterX"))
			roosterX = ((Boolean) h.get("roosterX")).booleanValue();
		if (h.containsKey("roosterY"))
			roosterY = ((Boolean) h.get("roosterY")).booleanValue();
		if (h.containsKey("schaalZichtbaar")) 
			schaalZichtbaar = ((Boolean) h.get("schaalZichtbaar")).booleanValue();
		if (h.containsKey("schaalX"))
			schaalX = ((Boolean) h.get("schaalX")).booleanValue();
		if (h.containsKey("schaalY"))
			schaalY = ((Boolean) h.get("schaalY")).booleanValue();
		if (h.containsKey("piLijnenZichtbaar")) 
			piLijnenZichtbaar = ((Boolean) h.get("piLijnenZichtbaar")).booleanValue();
		if (h.containsKey("zoomOptie")) 
			zoomOptie = ((Boolean) h.get("zoomOptie")).booleanValue();
		if (h.containsKey("traceOptie")) 
			traceOptie = ((Boolean) h.get("traceOptie")).booleanValue();
		if (h.containsKey("dragOptie")) 
			dragOptie = ((Boolean) h.get("dragOptie")).booleanValue();
		if (h.containsKey("zoomInTabel")) 
			zoomInTabel = ((Boolean) h.get("zoomInTabel")).booleanValue();
		if (h.containsKey("tabelAlsTekenTool")) 
			tabelAlsTekenTool = ((Boolean) h.get("tabelAlsTekenTool")).booleanValue();
		if (h.containsKey("xPositief")) 
			xPositief = ((Boolean) h.get("xPositief")).booleanValue();
		if (h.containsKey("yPositief")) 
			yPositief = ((Boolean) h.get("yPositief")).booleanValue();
		if (h.containsKey("xAsLog")) 
			xAsLog = ((Boolean) h.get("xAsLog")).booleanValue();
		if (h.containsKey("yAsLog")) 
			yAsLog = ((Boolean) h.get("yAsLog")).booleanValue();
		if (h.containsKey("xVarEditable")) 
			xVarEditable = ((Boolean) h.get("xVarEditable")).booleanValue();
		if (h.containsKey("yVarEditable")) 
			yVarEditable = ((Boolean) h.get("yVarEditable")).booleanValue();
		if (h.containsKey("snapToGridPoints"))
			snapToGridPoints = ((Boolean) h.get("snapToGridPoints")).booleanValue();
		if (h.containsKey("krommeZonderExtrapolatie"))
			krommeZonderExtrapolatie = ((Boolean) h.get("krommeZonderExtrapolatie")).booleanValue();
		if (h.containsKey("krommeMetExtrapolatie"))
			krommeMetExtrapolatie = ((Boolean) h.get("krommeMetExtrapolatie")).booleanValue();
		if (h.containsKey("tekenGrafiekNauwkeurigheid"))
			tekenGrafiekNauwkeurigheid = ((Integer) h.get("tekenGrafiekNauwkeurigheid")).intValue();
		if (h.containsKey("selectnummer"))
			selectnummer = ((Integer) h.get("selectnummer")).intValue();
		if (h.containsKey("beginwaarde"))
			beginwaarde = ((Integer) h.get("beginwaarde")).intValue();
		if (h.containsKey("tracexD"))
			tracexD = ((Double) h.get("tracexD")).doubleValue();
		
		if(h.containsKey("grafiekKleuren"))
			grafiekKleuren = ((Boolean)h.get("grafiekKleuren")).booleanValue();
		if(h.containsKey("kleurInstelbaar"))
			kleurInstelbaar = ((Boolean)h.get("kleurInstelbaar")).booleanValue();
		if(h.containsKey("functieBeginZichtbaar"))
			functieBeginZichtbaar = ((Boolean)h.get("functieBeginZichtbaar")).booleanValue();
		if(h.containsKey("functieBeginAanpasbaar"))
			functieBeginAanpasbaar = ((Boolean)h.get("functieBeginAanpasbaar")).booleanValue();
		if(h.containsKey("formeleFuncties"))
			formeleFuncties = ((Boolean)h.get("formeleFuncties")).booleanValue();
		if(h.containsKey("domeinInstelbaar"))
			domeinInstelbaar = ((Boolean)h.get("domeinInstelbaar")).booleanValue();
		if(h.containsKey("formuleComponentHoogte"))
			formuleComponentHoogte = ((Integer)h.get("formuleComponentHoogte")).intValue();
		
		if(h.containsKey("functieToegestaan"))
			functieToegestaan = ((Boolean)h.get("functieToegestaan")).booleanValue();
		if(h.containsKey("ongelijkheidToegestaan"))
			ongelijkheidToegestaan = ((Boolean)h.get("ongelijkheidToegestaan")).booleanValue();
		if(h.containsKey("implicieteFunctieToegestaan"))
			implicieteFunctieToegestaan = ((Boolean)h.get("implicieteFunctieToegestaan")).booleanValue();
		if(h.containsKey("verticaleLijnToegestaan"))
			verticaleLijnToegestaan = ((Boolean)h.get("verticaleLijnToegestaan")).booleanValue();
		if(h.containsKey("parametrisatieToegestaan"))
			parametrisatieToegestaan = ((Boolean)h.get("parametrisatieToegestaan")).booleanValue();
		
		
		this.beginxDocent = beginxDocent;
		this.beginyDocent = beginyDocent;
    	this.beginx = beginx;
		this.beginy = beginy;
		this.docentSchaalFactorX = docentSchaalFactorX;
		this.docentSchaalFactorY = docentSchaalFactorY;
		this.schaalFactorX = schaalFactorX;
		this.schaalFactorY = schaalFactorY;
		//this.graphPoints = graphPoints;
		this.graphPoints = new Vector();
		for(int i = 0; i < graphPointsX.length; i++)
		{	RealPoint rp = new RealPoint(graphPointsX[i], graphPointsY[i]);
			rp.setIndex(graphPointsIndex[i]);
			rp.setTabelIndex(graphPointsTabelIndex[i]);
			rp.setxString(graphPointsXString[i]);
			rp.setyString(graphPointsYString[i]);
			graphPoints.add(rp);
		}
		for(int i = 0; i < colorRGBsGewoon.length; i++)
		{	//colors[i] = new Color(colorRGBs[i][0], colorRGBs[i][1], colorRGBs[i][2]);
			gewoneKleuren[i] = new Color(colorRGBsGewoon[i][0], colorRGBsGewoon[i][1], colorRGBsGewoon[i][2]);
		}
		for(int i = 0; i < colorRGBsOpdrachten.length; i++)
		{	//colors[i] = new Color(colorRGBs[i][0], colorRGBs[i][1], colorRGBs[i][2]);
			opdrachtKleuren[i] = new Color(colorRGBsOpdrachten[i][0], colorRGBsOpdrachten[i][1], colorRGBsOpdrachten[i][2]);
		}
		if(paramNamen != null)
		{	this.schuifParameters = new SchuifParameter[paramNamen.length];
			for(int i = 0; i < schuifParameters.length; i++)
			{	schuifParameters[i] = new SchuifParameter(paramLengtes[i], paramNamen[i]);
				schuifParameters[i].zetGrensWaarden(paramOnderGrensWaarden[i], paramBovenGrensWaarden[i]);
				schuifParameters[i].zetStapGrootte(paramStapGroottes[i]);
				schuifParameters[i].zetWaarde(paramWaarden[i], false);
				schuifParameters[i].zetLocatie(paramX[i], paramY[i]);
			}
		}
		
		this.activeIndex = activeIndex;
		this.xAsNaam = xAsNaam;
		this.yAsNaam = yAsNaam;
		this.formuleComponentAan = formuleComponentAan;
		this.tekenComponentAan = tekenComponentAan;
		this.tabelComponentAan = tabelComponentAan;
		this.assenZichtbaar = assenZichtbaar;
		this.roosterZichtbaar = roosterZichtbaar;
		this.roosterGrof = roosterGrof; 
		this.roosterX = roosterX;
		this.roosterY = roosterY;
		this.schaalZichtbaar = schaalZichtbaar;
		this.schaalX = schaalX;
		this.schaalY = schaalY;
		this.piLijnenZichtbaar = piLijnenZichtbaar; 
		this.zoomOptie = zoomOptie; 
		this.traceOptie = traceOptie; 
		this.dragOptie = dragOptie; 
		this.zoomInTabel = zoomInTabel;
		this.tabelAlsTekenTool = tabelAlsTekenTool; 
		this.xPositief = xPositief; 
		this.yPositief = yPositief; 
		//niet: this.xAsLog = xAsLog etc. Dat gebeurt in zetXAsLog nog.
		this.xVarEditable = xVarEditable;
		this.yVarEditable = yVarEditable;
		this.snapToGridPoints = snapToGridPoints;
		this.krommeZonderExtrapolatie = krommeZonderExtrapolatie;
		this.krommeMetExtrapolatie = krommeMetExtrapolatie;
		this.tekenGrafiekNauwkeurigheid = tekenGrafiekNauwkeurigheid;
		this.selectnummer = selectnummer;
		this.beginwaarde = beginwaarde;
		this.tracexD = tracexD;
		tracex = (int) Math.round(tracexD);
		
		this.grafiekKleuren = grafiekKleuren;
		this.kleurInstelbaar = kleurInstelbaar;
		this.functieBeginZichtbaar = functieBeginZichtbaar;
		this.functieBeginAanpasbaar = functieBeginAanpasbaar;
		this.formeleFuncties = formeleFuncties;
		this.domeinInstelbaar = domeinInstelbaar;
		this.formuleComponentHoogte = formuleComponentHoogte;
		
		this.functieToegestaan = functieToegestaan;
		this.ongelijkheidToegestaan = ongelijkheidToegestaan;
		this.implicieteFunctieToegestaan = implicieteFunctieToegestaan;
		this.verticaleLijnToegestaan = verticaleLijnToegestaan;
		this.parametrisatieToegestaan = parametrisatieToegestaan;
		
		zetXAsNaam(xAsNaam, true);
		zetYAsNaam(yAsNaam, true);
		this.grafiekXAsNaam = grafiekXAsNaam;
		this.grafiekYAsNaam = grafiekYAsNaam;
		xAsNaamTF.setText(grafiekXAsNaam);
		yAsNaamTF.setText(grafiekYAsNaam);
		
		
		// opdrachten
		int typeOpdracht = GEENOPDRACHT;
		int[] maxScores = new int[9];
		int[] nauwkeurigheid = new int[3];
		int[] minimumPunten = new int[3];
		int scoreMax = 0;
		boolean domeinControleren = false;
		boolean leerlingZietTabel = true;
		//Expressie[] docentFuncties = null;
		String[] docentFunctieStrings = new String[maxAantalExpressies];
		//double[][] docentDomeinen = new double[maxAantalExpressies][2];
		String[][] docentDomeinStrings = new String[maxAantalExpressies][2];
		//Vector docentGraphPoints = new Vector();
		double[] docentGraphPointsX = null;
		double[] docentGraphPointsY = null;
		int[] docentGraphPointsIndex = null;
		int[] docentGraphPointsTabelIndex = null;
		String[] docentGraphPointsXString = null;
		String[] docentGraphPointsYString = null;
				
		if (h.containsKey("typeOpdracht")) 
			typeOpdracht = ((Integer) h.get("typeOpdracht")).intValue();		
		if (h.containsKey("maxScores")) 
			maxScores = (int[]) h.get("maxScores");		
		if (h.containsKey("docentFunctieStrings")) 
			docentFunctieStrings = (String[]) h.get("docentFunctieStrings");	
		//if (h.containsKey("docentDomeinen"))
		//	docentDomeinen = (double[][]) h.get("docentDomeinen");
		if (h.containsKey("docentDomeinStrings"))
			docentDomeinStrings = (String[][]) h.get("docentDomeinStrings");
		//if (h.containsKey("docentGraphPoints")) 
		//docentGraphPoints = (Vector) h.get("docentGraphPoints");		
		if(h.containsKey("docentGraphPointsX"))
			docentGraphPointsX = ((double[])h.get("docentGraphPointsX"));
		if(h.containsKey("docentGraphPointsY"))
			docentGraphPointsY = ((double[])h.get("docentGraphPointsY"));
		if(h.containsKey("docentGraphPointsIndex"))
			docentGraphPointsIndex = ((int[])h.get("docentGraphPointsIndex"));
		if(h.containsKey("docentGraphPointsTabelIndex"))
			docentGraphPointsTabelIndex = ((int[])h.get("docentGraphPointsTabelIndex"));
		if(h.containsKey("docentGraphPointsXString"))
			docentGraphPointsXString = ((String[])h.get("docentGraphPointsXString"));
		if(h.containsKey("docentGraphPointsYString"))
			docentGraphPointsYString = ((String[])h.get("docentGraphPointsYString"));
		if (h.containsKey("nauwkeurigheid")) 
			nauwkeurigheid = (int[]) h.get("nauwkeurigheid");		
		if (h.containsKey("minimumPunten")) 
			minimumPunten = (int[]) h.get("minimumPunten");	
		if (h.containsKey("scoreMax"))
			scoreMax = ((Integer) h.get("scoreMax")).intValue();
		if (h.containsKey("domeinControleren"))
			domeinControleren = ((Boolean) h.get("domeinControleren")).booleanValue();
		if (h.containsKey("leerlingZietTabel")) 
			leerlingZietTabel = ((Boolean) h.get("leerlingZietTabel")).booleanValue();		
		
		this.typeOpdracht = typeOpdracht;
		this.maxScores = maxScores;	
		this.nauwkeurigheid = nauwkeurigheid;
		this.minimumPunten = minimumPunten; 
		this.scoreMax = scoreMax;
		this.domeinControleren = domeinControleren;
		this.leerlingZietTabel = leerlingZietTabel;
		this.docentFunctieStrings = docentFunctieStrings;
		if(docentFunctieStrings != null)
		{	docentFuncties = new Expressie[docentFunctieStrings.length];
			for(int i = 0; i < docentFunctieStrings.length; i++)
			{	if (!docentFunctieStrings[i].equals("$f@"))
				{	docentFuncties[i] = FormuleParser.geefExpressie(docentFunctieStrings[i]);
				}
			}
		}	// anders blijft docentFunctie null
		//this.docentFuncties = docentFuncties;			
		this.docentDomeinStrings = docentDomeinStrings;
		if(docentDomeinStrings != null)
		{	docentDomeinen = new double[docentDomeinStrings.length][2];
			for(int i = 0; i < docentDomeinStrings.length; i++)
			{	try{
				docentDomeinen[i][0] = FormuleParser.geefExpressie(docentDomeinStrings[i][0]).geefWaarde();
				}
				catch(Exception e)
				{docentDomeinen[i][0] = DEFAULTDOMEIN[0];
				}
				try{
				docentDomeinen[i][1] = FormuleParser.geefExpressie(docentDomeinStrings[i][1]).geefWaarde();
				}
				catch(Exception e)
				{docentDomeinen[i][1] = DEFAULTDOMEIN[1];
				
				}
				//Hier nog try/catch inbouwen? Geeft deze een exception bij randomvariabelen?
			}
			
		}
		
		/*
		if(docentDomeinen == null)
			this.docentDomeinen = null;
		else
		{	this.docentDomeinen = new double[docentDomeinen.length][2];
			for(int i = 0; i < docentDomeinen.length; i++)
			{	this.docentDomeinen[i][0] = docentDomeinen[i][0];
				this.docentDomeinen[i][1] = docentDomeinen[i][1];
			}
		}
		*/
		//this.docentGraphPoints = docentGraphPoints;
		this.docentGraphPoints = new Vector();
		for(int i = 0; i < docentGraphPointsX.length; i++)
		{	RealPoint rp = new RealPoint(docentGraphPointsX[i], docentGraphPointsY[i]);
			rp.setIndex(docentGraphPointsIndex[i]);
			rp.setTabelIndex(docentGraphPointsTabelIndex[i]);
			rp.setxString(docentGraphPointsXString[i]);
			rp.setyString(docentGraphPointsYString[i]);
			docentGraphPoints.add(rp);
		}
		
		zetZoomOptie(zoomOptie);
		zetTraceOptie(traceOptie);
		if(traceOptie && tracex != -2)
			slider.zetStand(tracex);
		
		
		tabelComponent.setState(h, false);
		tabelComponent.setRandomAllowed(true);
		
		zetSnapToGridPoints(snapToGridPoints);
		zetZoomInTabel(zoomInTabel);
		zetTabelAlsTekenTool(tabelAlsTekenTool, true);
		zetXAsLog(xAsLog);
		zetYAsLog(yAsLog);
		zetXVarEditable(xVarEditable);
		zetYVarEditable(yVarEditable);
		zetKrommeKnoppen(true, krommeZonderExtrapolatie, krommeMetExtrapolatie);
		
		zetMaxScores(maxScores);
		zetDocentFuncties(docentFuncties);
		//zetDocentDomeinen(docentDomeinen);
		zetDocentDomeinen(docentDomeinStrings);
		zetFormuleEditorOpties(null, true);
		zetTypeOpdracht(typeOpdracht, true);
		
		tekenComponent.setState(h);
		formuleComponent.setState(h, null, null, false);
			
		setActiveIndex(activeIndex, true);
		if(schuifParameters != null)
			for (int i = 0; i < schuifParameters.length; i++)
			{	add(schuifParameters[i].geefSlider(), 0);
				schuifParameters[i].geefSlider().addActionListener(this);
				schuifParameters[i].geefSlider().addMouseListener(this);
				schuifParameters[i].geefSlider().addMouseMotionListener(this);
			}
	}

	public Hashtable getEditState() {
		return getState();
	}

	public InteractieEditPanel getEditPanel() {
		return new GraphToolInteractieEditPanel();
	}

	public void wis() {
		correct = false;
	    fout = false;
	    score = 0;
	    nagekeken = false;
	    ingevuld = false;
	    if(feedbackTekst!=null)
	    {	if("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant))	remove(mwFeedbackPanel);
	    	else remove(feedbackTekst);
	    }
	    graphPoints = null;
	    //nog meer? domeinen wissen? functies, ongelijkheden etc wissen?
	}

	public void zetMaat() {
		
	}

	public int geefAsHoogte() {
		return 0;
	}

	public int getIpId() {
		return 0;
	}

	public int getScore() {
		return score;
	}

	public int[][] getScoreObjectives() {
		return null;
	}

	public int getScoreMax() {
		return scoreMax;
		
	}

	public boolean isCorrect() {
		if(typeOpdracht == GEENOPDRACHT)
			return true;
		return correct;
	}

	public boolean isFout() {
		if(typeOpdracht == GEENOPDRACHT)
			return false;
		return fout;
	}

	public void zetMode(int mode) {
		this.mode = mode;
		zetKijkNaButton(kijkNaButtonZichtbaar);
	}

	public void zetNagekeken(boolean b) {
		if (ingevuld) 
			nagekeken = b;
	}

	public void stop() {
		if(mode != 1)
			kijkNa();
	}

	public void start() {
		
	}

	public void destroy() {
		
	}

	public void opnieuw() {
		
	}
	
	public void kijkNa(boolean show)
	{	ingevuld = false;
		remove(feedbackTekst);
		if(typeOpdracht == VINDFORMULEBIJGRAFIEK)
		{	if(functies != null)
			{	boolean[] functieCorrect = new boolean[aantalFuncties];
				Expressie[] vgldocentFuncties = new Expressie[aantalFuncties];
				for(int i = 0; i < aantalFuncties; i++)
					vgldocentFuncties[i] = docentFuncties[i];
				Color color = Color.red;
				score = 0;
				correct = false;
				fout = false;
				for(int i = 0; i < functies.length; i++)
				{	color = Color.red;
					if(functies[i] != null)
					{	ingevuld = true;
	    				for(int j = 0; j < vgldocentFuncties.length; j++)
							if(vgldocentFuncties[j] != null && Algebra.isGelijkwaardig(functies[i], vgldocentFuncties[j]))
							{	if(domeinControleren && (domeinen[i][0] != docentDomeinen[i][0] || domeinen[i][1] != docentDomeinen[i][1]))
								{	color = new Color(255, 193, 0);
									score += maxScores[j]/2;
								}
								else
								{	color = new Color(0, 200, 0);
									score += maxScores[j];
									functieCorrect[j] = true;
									domeinen[i][0] = docentDomeinen[j][0];
									domeinen[i][1] = docentDomeinen[j][1];
								}	
								vgldocentFuncties[j] = null;
								break;
							}
					}
					if(show)
						setColor(i, color, true);
					else
					;	//nog iets leegmaken, zodat niet wordt getekend?
						
				}
				for(int i = 0; i < functieCorrect.length; i++)
					if(!functieCorrect[i])
					{	fout = true;
						break;
					}
				if(!fout)
					correct = true;
			}
		}
		else if (typeOpdracht == VINDFORMULEBIJPUNTEN)
		{	Expressie leerlingExp = functies[0];
			if (leerlingExp != null)
			{	ingevuld = true;
				Color color = Color.red;
				score = 0;
				correct = false;
				fout = false;
				if (Algebra.isGelijkwaardig(leerlingExp, docentFuncties[0]))
				{	color = new Color(0, 200, 0);
					score = scoreMax;
					correct = true;
				}
				else
				{	fout = true;
				}
				if(show)
					setColor(0, color, true);
				else
					;	//iets leegmaken, zodat niet getekend?
			}
		}
		else if (typeOpdracht == TEKENPUNTENBIJFORMULE)
		{	int kleinsteMinimum = minimumPunten[0];
			for(int i = 1; i < aantalFuncties; i++)
				if(minimumPunten[i] < kleinsteMinimum)
					kleinsteMinimum = minimumPunten[i];
			if(docentFuncties != null && graphPoints.size() < kleinsteMinimum)
			{	score = 0;
				correct = false;
				produceAction("changed");
				if(graphPoints.size() > 0)	
					ingevuld = true;
				return;
			}
			if (docentFuncties != null)
			{	boolean[] functieCorrect = new boolean[aantalFuncties];
				boolean puntenCorrect = true;
				for(int i = 0; i < aantalFuncties; i++)
					functieCorrect[i] = false;
				Expressie[] vgldocentFuncties = new Expressie[aantalFuncties];
				for(int i = 0; i < aantalFuncties; i++)
					vgldocentFuncties[i] = docentFuncties[i];
				ingevuld = true;
				Color color = Color.red;
				score = 0; 
				correct = false;
				fout = false;
				int[][] hits = new int[aantalFuncties][aantalFuncties];
				
				Vector[] checkPoints = new Vector[aantalFuncties];
				for(int i = 0; i < aantalFuncties; i++)
				{	checkPoints[i] = getPoints(i + 1, false);
					for(int j = 0; j < hits.length; j++)
						hits[i][j] = 0;
					if(checkPoints[i].size() > 0)
						for(int pCnt = 0; pCnt < checkPoints[i].size(); pCnt++)
						{	RealPoint lPoint = (RealPoint) checkPoints[i].elementAt(pCnt);
							Point lPixel = realPointToPixels(lPoint);
							for(int j = 0; j < aantalFuncties; j++)
							{	double dWaarde = docentFuncties[j].geefWaarde(lPoint.getX());
								RealPoint dPoint = new RealPoint(lPoint.getX(), dWaarde);
								Point dPixel = realPointToPixels(dPoint);
								double dis = Math.sqrt((lPixel.x - dPixel.x) * (lPixel.x - dPixel.x) +
								     	   (lPixel.y - dPixel.y) * (lPixel.y - dPixel.y)); 
								if (dis < nauwkeurigheid[j])
									hits[i][j]++;
							}
						}
				}
				int[][] permutatie = new int[aantalFuncties][2];
				for(int i = 0; i < permutatie.length; i++)
				{	permutatie[i][0] = i;
					permutatie[i][1] = -1;
				}
				permutatie[0][1] = 0;
				int totaalHits = 0;
				int permutatieHits;
				int[] koppeling = new int[aantalFuncties];
				while(permutatie != null)
				{	permutatieHits = 0;
					for(int i = 0; i < permutatie.length; i++)
						permutatieHits += hits[i][permutatie[i][0]];
					if(permutatieHits > totaalHits)
					{	totaalHits = permutatieHits;
						for(int i = 0; i < permutatie.length; i++)
						{	koppeling[i] = permutatie[i][0];
						}
					}
					permutatie = vindVolgendePermutatie(permutatie);
				}
				int somMinimum = 0;
				for(int i = 0; i < aantalFuncties; i++)
					somMinimum += minimumPunten[i];
				int[] scorePerPunt = new int[aantalFuncties];
				for(int i = 0; i < aantalFuncties; i++)
					scorePerPunt[koppeling[i]] = maxScores[i] / Math.max(checkPoints[i].size(), minimumPunten[koppeling[i]]);
				if(totaalHits == 0)
				{	score = 0; 
					fout = true;
					groenVinkjeLabel.setVisible(false);
					oranjeVinkjeLabel.setVisible(false);
					kruisjeLabel.setVisible(show);
				}
				else if(totaalHits == Math.max(graphPoints.size(), somMinimum))
				{	for(int i = 0; i < aantalFuncties; i++)
						color = new Color(0, 200, 0);
					for(int i = 0; i < aantalFuncties; i++)
						functieCorrect[i] = true;
					score = scoreMax; 
					correct = true;
					groenVinkjeLabel.setVisible(show);
					oranjeVinkjeLabel.setVisible(false);
					kruisjeLabel.setVisible(false);
				}
				else 
				{	score = 0;
					for(int i = 0; i < aantalFuncties; i++)
						if(hits[i][koppeling[i]] == Math.max(checkPoints[i].size(), minimumPunten[koppeling[i]]))
						{	functieCorrect[koppeling[i]] = true;
							color = new Color(0, 200, 0);
							score += maxScores[koppeling[i]];
						}
						else
						{	score += hits[i][koppeling[i]] * scorePerPunt[koppeling[i]];
						}
					fout = true;
					groenVinkjeLabel.setVisible(false);
					oranjeVinkjeLabel.setVisible(show);
					kruisjeLabel.setVisible(false);
					boolean minstensEenFunctieCorrect = false;
					for(int i = 0; i < functieCorrect.length; i++)
						if(functieCorrect[i])
						{	minstensEenFunctieCorrect = true;
							break;
						}
					if(show && minstensEenFunctieCorrect)
						setFeedback(GraphTool.rb.getString("feedbackTekstGrafiekenDeels"),true);
					else if(show)
					{	setFeedback(GraphTool.rb.getString("feedbackTekstPuntenDeels"),true);
						puntenCorrect = false;
					}
				}
				if(!grafiekXAsNaam.equals(xAsNaam) || !grafiekYAsNaam.equals(yAsNaam))
				{	if(score > 0)
					color = new Color(255, 193, 0);
					score = Math.max(score - 2, 0);
					if(correct || oranjeVinkjeLabel.isVisible() && puntenCorrect)
					{	correct = false; 
						fout = true;
						//groenVinkjeLabel.setVisible(false);
						//oranjeVinkjeLabel.setVisible(show);
						if(show)setFeedback(GraphTool.rb.getString("feedbackTekstLabelsAssen"),true);
					}
				}
				if((krommeMetExtrapolatie || krommeZonderExtrapolatie) && tekenComponent.getConnectMode() != tekenComponent.CURVE_EXTRA && 
						tekenComponent.getConnectMode() != tekenComponent.CURVE)
				{	if(score > 0)
					color = new Color(255, 193, 0);
					score = Math.max(score - 2, 0);
					if(correct || oranjeVinkjeLabel.isVisible() && puntenCorrect)
					{	correct = false;
						fout = true;
						//groenVinkjeLabel.setVisible(false);
						//oranjeVinkjeLabel.setVisible(show);
						if(show)setFeedback(GraphTool.rb.getString("feedbackTekstTekenGrafiek"),true);
					} 
				}	
				
				if(show) 
				{	//leerlingcolor op juiste kleur zetten.
					for(int i = 0; i < aantalFuncties; i++)
					{	if(functieCorrect[koppeling[i]])
							colors[i] = color;
						else
							colors[i] = Color.red;
					}
					tekenDocentFuncties = new Expressie[aantalFuncties];
					if(!krommeMetExtrapolatie && !krommeZonderExtrapolatie)
					{	for(int i = 0; i < aantalFuncties; i++)
						{	if (functieCorrect[i])
							{	tekenDocentFuncties[i] = docentFuncties[i];
							}
						}
						docentColor = color;
					}
					repaint();
				}
				
			}
		}
		else if (typeOpdracht == TEKENTABELPUNTEN)
		{	
			if(graphPoints.size() < docentGraphPoints.size())
			{	score = 0;
				correct = false;
				produceAction("changed");
				return;
			}
			if (graphPoints.size() > 0)
			{	
				ingevuld = true;
				Color color = Color.red;
				score = 0;
				correct = false;
				fout = false;
	
				Vector llgPtsCopy = new Vector();
				for (int pCnt = 0; pCnt < graphPoints.size(); pCnt++)
				{	llgPtsCopy.addElement(graphPoints.elementAt(pCnt));
				}
				RealPoint[] llgPtsArray = new RealPoint[graphPoints.size()];
				
				for (int dCnt = 0; dCnt < docentGraphPoints.size(); dCnt++)
				{
					RealPoint dPt = (RealPoint) docentGraphPoints.elementAt(dCnt);
					RealPoint lPt = (RealPoint) llgPtsCopy.elementAt(0);
					int index = 0;
					double distance = Math.sqrt((dPt.getX() - lPt.getX())*(dPt.getX() - lPt.getX()) + (dPt.getY() - lPt.getY())*(dPt.getY() - lPt.getY()));
					for (int lCnt = 1; lCnt < llgPtsCopy.size(); lCnt++)
					{	RealPoint aLlgPt = (RealPoint) llgPtsCopy.elementAt(lCnt);
						double aDis = Math.sqrt((dPt.getX() - aLlgPt.getX()) * (dPt.getX() - aLlgPt.getX()) +
												(dPt.getY() - aLlgPt.getY()) * (dPt.getY() - aLlgPt.getY()));
						if (aDis < distance)
						{	distance = aDis;
							index = lCnt;
						}
					}
					
					llgPtsArray[dCnt] = (RealPoint) llgPtsCopy.elementAt(index);
					llgPtsCopy.removeElementAt(index);
				}
			
				int hits = 0;
				for (int dCnt = 0; dCnt < docentGraphPoints.size(); dCnt++)
				{	RealPoint dPoint = (RealPoint) docentGraphPoints.elementAt(dCnt);
					RealPoint lPoint = llgPtsArray[dCnt];
					
					Point lPixel = realPointToPixels(lPoint);
					Point dPixel = realPointToPixels(dPoint);
	
					double dis = Math.sqrt((lPixel.x - dPixel.x) * (lPixel.x - dPixel.x) +
								     	   (lPixel.y - dPixel.y) * (lPixel.y - dPixel.y)); 
					
					if (dis < nauwkeurigheid[0])
						hits++;
				}
				
				int scorePerPunt = scoreMax / Math.max(docentGraphPoints.size(), graphPoints.size());
				if (hits == 0)
					score = 0;
				else if (hits == graphPoints.size())
				{
					color = new Color(0, 200, 0);
					score = scoreMax;
					correct = true;
					groenVinkjeLabel.setVisible(show);
					oranjeVinkjeLabel.setVisible(false);
					kruisjeLabel.setVisible(false);
				}
				else
				{	score = hits * scorePerPunt;    			
					fout = true;
					groenVinkjeLabel.setVisible(false);
					oranjeVinkjeLabel.setVisible(false);
					kruisjeLabel.setVisible(show);
				}
				if(!grafiekXAsNaam.equals(xAsNaam) || !grafiekYAsNaam.equals(yAsNaam))
				{	score = Math.max(score - 2, 0);
					if(correct)
					{	correct = false; 
						groenVinkjeLabel.setVisible(false);
						oranjeVinkjeLabel.setVisible(show);
					}
				}
				if(show)
					setColor(0, color, true);
				
				repaint();
			}
		}
		
		if (show)
			if (ingevuld)
				produceAction("changed");
		//if (ingevuld)
			//produceAction("changed");
	
	}

	public void kijkNa()
    {	kijkNa(true);
	}
	
	public void setFeedback(String tekst, boolean closeable)
	{	feedbackTekst.setText("");
		feedbackTekst.setCloseable(closeable);
		feedbackTekst.setSize(veldb - kijkNaPanel.getWidth(), 20);
		
		//feedbackTekst.setSize(195,20);
		feedbackTekst.setLocation(kijkNaPanel.getLocation().x + kijkNaPanel.getWidth(), kijkNaPanel.getLocation().y);
				//getSize().width-250,formuleVakken[stapNr].getLocation().y + formuleVakken[stapNr].getSize().height + 10);
		feedbackTekst.setText(tekst);
		feedbackTekst.resize();
		if("MW".equals(WiskOpdr.deployVariant))
		{	
			int h = feedbackTekst.getHeight()+20;
			mwFeedbackPanel.setSize(mwFeedbackPanel.getWidth(),h);
			//mwFeedbackPanel.setLocation(getSize().width-310,formuleVakken[stapNr].getLocation().y + formuleVakken[stapNr].getSize().height + 5);
			mwFeedbackPanel.setLocation(kijkNaPanel.getLocation().x + kijkNaPanel.getWidth(), kijkNaPanel.getLocation().y);
			feedbackTekst.setLocation(50,10);
			mwFeedbackPanel.add(feedbackTekst,0);
			add(mwFeedbackPanel,0);
		}
		else if("GR".equals(WiskOpdr.deployVariant))
		{	
		    feedbackTekst.setOpaque(false);
            int h = feedbackTekst.getHeight()+40;
			mwFeedbackPanel.setSize(mwFeedbackPanel.getWidth(),h);
			//int fbx = Math.min(getSize().width-220, formuleVakken[stapNr].getLocation().x + formuleVakken[stapNr].getSize().width + 5);
			//int fby = formuleVakken[stapNr].getLocation().y + formuleVakken[stapNr].getSize().height + 5;
			//mwFeedbackPanel.setLocation(fbx, fby);
			mwFeedbackPanel.setLocation(kijkNaPanel.getLocation().x + kijkNaPanel.getWidth(), kijkNaPanel.getLocation().y);
			feedbackTekst.setLocation(5,25);
			mwFeedbackPanel.add(feedbackTekst,0);
			add(mwFeedbackPanel,0);
		}
		else add(feedbackTekst);
		produceAction("feedback");
	}
    
    	
    public int[][] vindVolgendePermutatie(int[][] permutatie)
    {	int verplaatsIndex = -1;
    	int verplaatsRichting = 0;
    	int verplaatsGetal = -1;
    	int[][] nieuwePerm = new int[permutatie.length][2];
    	
    	for(int i = 0; i < permutatie.length; i++)
    		if(permutatie[i][1] != 0 && permutatie[i][0] > verplaatsGetal)
    		{	verplaatsIndex = i;
    			verplaatsRichting = permutatie[i][1];
    			verplaatsGetal = permutatie[i][0];
    		}
    	if(verplaatsRichting == 1)
    	{	for(int i = 0; i < verplaatsIndex; i++)
    			nieuwePerm[i] = permutatie[i];
    		nieuwePerm[verplaatsIndex] = permutatie[verplaatsIndex + 1];
    		nieuwePerm[verplaatsIndex + 1] = permutatie[verplaatsIndex];
    		if(permutatie.length > verplaatsIndex + 2)
    			for(int i = verplaatsIndex + 2; i < permutatie.length; i++)
    				nieuwePerm[i] = permutatie[i];
    		if(verplaatsIndex == permutatie.length - 2 || nieuwePerm[verplaatsIndex + 2][0] > nieuwePerm[verplaatsIndex + 1][0])
    			nieuwePerm[verplaatsIndex + 1][1] = 0;
    		for(int i = 0; i < verplaatsIndex + 1; i++)
    			if(nieuwePerm[i][0] > nieuwePerm[verplaatsIndex + 1][0])
    				nieuwePerm[i][1] = 1;
    		if(permutatie.length > verplaatsIndex + 2)
    			for(int i = verplaatsIndex + 2; i < permutatie.length; i++)
    				if(nieuwePerm[i][0] > nieuwePerm[verplaatsIndex + 1][0])
    					nieuwePerm[i][1] = -1;
    	}
    	else if(verplaatsRichting == -1)
    	{	for(int i = 0; i < verplaatsIndex - 1; i++)
				nieuwePerm[i] = permutatie[i];
    		nieuwePerm[verplaatsIndex - 1] = permutatie[verplaatsIndex];
    		nieuwePerm[verplaatsIndex] = permutatie[verplaatsIndex - 1];
    		if(permutatie.length > verplaatsIndex + 1)
    			for(int i = verplaatsIndex + 1; i < permutatie.length; i++)
    				nieuwePerm[i] = permutatie[i];
    		if(verplaatsIndex == 1 || nieuwePerm[verplaatsIndex - 1][0] < nieuwePerm[verplaatsIndex - 2][0])
    			nieuwePerm[verplaatsIndex - 1][1] = 0;
    		for(int i = 0; i < verplaatsIndex - 1; i++)
    			if(nieuwePerm[i][0] > nieuwePerm[verplaatsIndex - 1][0])
    				nieuwePerm[i][1] = 1;
    		if(permutatie.length > verplaatsIndex + 1)
    			for(int i = verplaatsIndex; i < permutatie.length; i++)
    				if(nieuwePerm[i][0] > nieuwePerm[verplaatsIndex - 1][0])
    					nieuwePerm[i][1] = -1;
    	}
    	else
    		nieuwePerm = null;
    			
    	return nieuwePerm;
    }

	public void kijkNa(int stapNr) {
		kijkNa(true);
	}
	
	public void kijkNa(int stapNr, boolean show) {
		kijkNa(show);
	}


	public void updateTabelNames(String[] expNaam, boolean setState)
	{
		if(!tabelAlsTekenTool)
			tabelComponent.updateTabelNames(expNaam, formuleComponent.getMaxAantalFuncties(), setState);
	}
	
	public void zetOngelijkheid(int nr, Expressie e, boolean isYOngelijkheid, boolean isGroterGelijkOngelijkheid, boolean isEnOngelijkheid)//, boolean inclusiefGelijkheid)
	{
		ongelijkheden[nr] = e;
		isY[nr] = isYOngelijkheid;
		isGroterGelijk[nr] = isGroterGelijkOngelijkheid;
		isEn[nr] = isEnOngelijkheid;
		
		repaint();
	}
	
	public void zetFunctie(int nr, Expressie e, String expString, String expNaam, double[] domein, boolean update, boolean setState, boolean docent)
	{	if(docent && docentFunctieStrings != null && nr < docentFunctieStrings.length)
		{	if(docentFuncties != null && nr < docentFuncties.length)
				docentFuncties[nr] = e;
			if(tekenDocentFuncties != null && nr < tekenDocentFuncties.length)
				tekenDocentFuncties[nr] = e;
			if(docentDomeinen != null && nr < docentDomeinen.length)
			{	docentDomeinen[nr][0] = domein[0];
				docentDomeinen[nr][1] = domein[1];
			}
			docentFunctieStrings[nr] = expString;
		}
		else
		{	functies[nr] = e;
			domeinen[nr][0] = domein[0];
			domeinen[nr][1] = domein[1];
		}
		
		if(!tabelAlsTekenTool && !docent)//of misschien bij een bepaald type opdracht wel als docent waar is..
		{	
			tabelComponent.zetFunctie(nr, e, expNaam, update, setState);
		}
		
		repaint();
	}
	
	public void zetParametrisatie(int nr, Expressie e, String variabele, boolean isX)
	{	if(isX)
			parametrisaties[nr/2][0] = e;
		else
			parametrisaties[nr/2][1] = e;
		parametrisatieVariabelen[nr/2] = variabele;
		repaint();
	}
	
	public void zetVerticaleLijn(int nr, Expressie e)
	{
		verticaleLijnen[nr] = e;
		repaint();
	}
	
	public RealPoint realPointToRealPixels(RealPoint rp)
	{	RealPoint realPix = new RealPoint(
			beginx + eenheidxD * (xAsLog?Math.log10(rp.getX()):rp.getX()) / schaalFactorX,
			gv.getSize().height - (beginy + eenheidyD * (yAsLog?Math.log10(rp.getY()):rp.getY()) / schaalFactorY));
		return realPix;
	}
	
	public RealPoint pixelsToRealPoint(Point pix)
	{	RealPoint rp = new RealPoint(0, 0);
		rp.setX(schaalFactorX * (-beginx)/eenheidxD + schaalFactorX * pix.x / eenheidxD);
		if(xAsLog)
			rp.setX(Math.pow(10, rp.getX()));
		rp.setY((schaalFactorY * (-beginy) / eenheidyD +
				    schaalFactorY * (gv.getSize().height - pix.y) / eenheidyD));
		if(yAsLog)
			rp.setY(Math.pow(10, rp.getY()));
		rp.setxString(Double.toString(rp.getX()));
		rp.setyString(Double.toString(rp.getY()));
		return rp;
	}
	
	public int geefEersteVrijeVak(boolean docent)
	{	int vakIndex = 0;
		int laagsteVakIndex = 0;
		int hoogsteVakIndex = 1;
		Vector points = getPoints(activeIndex, docent);
		if(points.size() == 0)
			return vakIndex;
		for (int pCnt = 0; pCnt < points.size(); pCnt++)
		{	RealPoint rp = (RealPoint) points.elementAt(pCnt);
			if(rp.getTabelIndex() < laagsteVakIndex)
				laagsteVakIndex = rp.getTabelIndex();
			if(rp.getTabelIndex() > hoogsteVakIndex)
				hoogsteVakIndex = rp.getTabelIndex();
		}
		boolean[] kandidaat = new boolean[hoogsteVakIndex - laagsteVakIndex + 2];
		for(int i = 0; i < kandidaat.length; i++)
			kandidaat[i] = true;
		for (int pCnt = 0; pCnt < points.size(); pCnt++)
		{	RealPoint rp = (RealPoint) points.elementAt(pCnt);
			kandidaat[rp.getTabelIndex() - laagsteVakIndex] = false;
		}
		for(int i = 0; i < kandidaat.length; i++)
			if(kandidaat[i])
			{	vakIndex = laagsteVakIndex + i;
				break;
			}
		return vakIndex;	
	}
	
	public int closestFreePixX(int pressedX)
	{	
		Vector points = getPoints(getActiveIndex(), false);

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
	
	public int closestGridPix(int pressedX, boolean xAs)
	{	int bx = (int)Math.round(beginx);			
		int by = (int)Math.round(beginy);
		
		int imin = -(int)Math.round(beginx/eenheidx); 
		int imax = 1+gv.getWidth()/eenheidx-(int)Math.round(beginx/eenheidx);
		int jmin = -(int)Math.round(beginy/eenheidy); 
		int jmax = 1+gv.getHeight()/eenheidy-(int)Math.round(beginy/eenheidy);
		
		if(xAs)
		{	for(int i=imin+1 ; i<imax ; i++)
				if((!xPositief || i>0) && i%((roosterGrof && !xAsLog)?2:1)==0)  
					if(Math.abs(pressedX - (bx+i*eenheidxD)) <= eenheidxD/2)
					{	pressedX = (int)(bx+i*eenheidxD);
						break;
					}
		}
		else
		{	for(int j=jmin ; j<jmax ; j++)
				if((!yPositief || j>0) && j%((roosterGrof && !yAsLog)?2:1)==0) 
					if(Math.abs(pressedX - (gv.getHeight()-(by+j*eenheidyD))) <= eenheidyD/2)
					{	pressedX = (int)(gv.getHeight()-(by+j*eenheidyD));
						break;
					}
		}
		
		return pressedX;
	}
	
	public void mousePressed(MouseEvent e)
	{	requestFocus();
	
		if(schuifParameters != null)
		{	for(int i = 0; i < schuifParameters.length; i++)
			{	if(e.getSource() == schuifParameters[i].geefSlider() && !schuifParameters[i].geefSlider().isRaak())
				{	//if(schuifParameters[i].geefSlider().getRaak())
					//{
					//	System.out.println("raak");
					//	return;
					//}
				
				
					startxv = e.getXOnScreen(); 
							//e.getX();
					startyv = e.getYOnScreen();
					startSliderX = schuifParameters[i].getX();
					startSliderY = schuifParameters[i].getY();
					sliderSlepend = true;
					return;
				}
			}	
		}
		if (!tekenComponentAan && e.getSource() == gv) 
		{	startxv = e.getX();
			startyv = e.getY();
		}
		else if (tekenComponentAan && e.getSource() == gv)
		{	
			int pressedX = e.getX();
			int pressedY = e.getY();
			
			if (tekenComponent.getCursorMode() == tekenComponent.NOCUR)
			{	startxv = e.getX();
				startyv = e.getY();
			}
			else if (tekenComponent.getCursorMode() == tekenComponent.DRAW)
			{	
				if(typeOpdracht > GEENOPDRACHT)
				{	setColor(activeIndex - 1, opdrachtKleuren[activeIndex - 1], false);
				}
				
				
				// geklikt met rechter muisknop, dit is gummen
				if ((e.getModifiers() & e.BUTTON1_MASK) == 0)
            	{	// kijk of er op een punt van de actuele grafiek is geklikt
					RealPoint drp = null;
					Vector points = getPoints(getActiveIndex(), false);
					{	for (int pCnt = 0; pCnt < points.size(); pCnt++)
						{	RealPoint rp = (RealPoint) points.elementAt(pCnt);
							Point rpPix = realPointToPixels(rp);
							int dis = (int) Math.round(
								Math.sqrt((rpPix.x - pressedX) * (rpPix.x - pressedX) +
										  (rpPix.y - pressedY) * (rpPix.y - pressedY)));
							if (dis <= PRAD + 2)
							{	drp = rp;
							}
						}
					}
					if (drp != null)
					{	removePoint(drp.getTabelIndex(), drp.getIndex(), false);
					
					repaint();
					}

            	}
            	// geklikt met een andere muisknop
            	else	
            	{	// kijk of er op een point van de actuele grafiek is geklikt
					// dat gaan we dan slepen
					dragPoint = null;
					Vector points = getPoints(getActiveIndex(), false);
					{	for (int pCnt = 0; pCnt < points.size(); pCnt++)
						{	RealPoint rp = (RealPoint) points.elementAt(pCnt);
							Point rpPix = realPointToPixels(rp);
							int dis = (int) Math.round(
								Math.sqrt((rpPix.x - pressedX) * (rpPix.x - pressedX) +
										  (rpPix.y - pressedY) * (rpPix.y - pressedY)));
							if (dis <= PRAD + 2)
							{	dragPoint = rp;
							}
						}
					}
					// dragPoint slepen
					if (dragPoint != null)
					{	startxv = e.getX();
						startyv = e.getY();
					}	
           			else // tekenen
           			{	if(snapToGridPoints)
           				{	pressedX = closestGridPix(pressedX, true);
           					pressedY = closestGridPix(pressedY, false);
           				}
           				int freePixX = closestFreePixX(pressedX);						

						RealPoint newPoint = pixelsToRealPoint(
							new Point(freePixX, pressedY));
						newPoint.setIndex(getActiveIndex());
						newPoint.setTabelIndex(geefEersteVrijeVak(false));
						addInsert(newPoint, false);
						if(tabelAlsTekenTool)
							tabelComponent.vernieuwFirstIndexVisible(newPoint.getTabelIndex(), getActiveIndex());
						repaint();
           			} // tekenen
				} // niet rechts geklikt
			} // DRAW
			else if (tekenComponent.getCursorMode() == tekenComponent.DELETE)
			{	
			
				// kijk of er op een punt van de actuele grafiek is geklikt
				RealPoint drp = null;
				Vector points = getPoints(getActiveIndex(), false);
				{	for (int pCnt = 0; pCnt < points.size(); pCnt++)
					{	RealPoint rp = (RealPoint) points.elementAt(pCnt);
						Point rpPix = realPointToPixels(rp);
						int dis = (int) Math.round(
							Math.sqrt((rpPix.x - pressedX) * (rpPix.x - pressedX) +
									  (rpPix.y - pressedY) * (rpPix.y - pressedY)));
						if (dis <= PRAD + 2)
						{	drp = rp;
						}
						
					}
				}
				if (drp != null)
				{	if(typeOpdracht > GEENOPDRACHT)
					{	setColor(activeIndex - 1, opdrachtKleuren[activeIndex - 1], false);
					}
					removePoint(drp.getTabelIndex(), drp.getIndex(), false);
				
				repaint();
				}
			}
			else if (tekenComponent.getCursorMode() == tekenComponent.DRAG)
			{	// kijk of er op een punt van de actuele grafiek is geklikt
				dragPoint = null;
				otherPoint = null;
				Vector points = getPoints(getActiveIndex(), false);
				{	for (int pCnt = 0; pCnt < points.size(); pCnt++)
					{	RealPoint rp = (RealPoint) points.elementAt(pCnt);
						Point rpPix = realPointToPixels(rp);
						int dis = (int) Math.round(
							Math.sqrt((rpPix.x - pressedX) * (rpPix.x - pressedX) +
									  (rpPix.y - pressedY) * (rpPix.y - pressedY)));
						if (dis <= PRAD + 2)
						{	if(typeOpdracht > GEENOPDRACHT)
							{	setColor(activeIndex - 1, opdrachtKleuren[activeIndex - 1], false);
							}
							dragPoint = rp;
						}
						
					}
				}
				// als niet, kijk of er op een punt van een andere grafiek geklikt is
				if (dragPoint == null)
				{	for (int index = 1; index <= getNumGraphs(); index++)
					{	if (index != getActiveIndex())
						{	Vector indexPoints = getPoints(index, false);
							for (int pCnt = 0; pCnt < indexPoints.size(); pCnt++)
							{	RealPoint rp = (RealPoint) indexPoints.elementAt(pCnt);
								Point rpPix = realPointToPixels(rp);
								int dis = (int) Math.round(
									Math.sqrt((rpPix.x - pressedX) * (rpPix.x - pressedX) +
											  (rpPix.y - pressedY) * (rpPix.y - pressedY)));
								if (dis <= PRAD + 2)
								{	otherPoint = rp;
								}
						
							}
						}
					}
				}
				
				startxv = e.getX();
				startyv = e.getY();
			}
		}
	}
	
	public void mouseDragged(MouseEvent e)
	{	if(schuifParameters != null)
		{
			for(int i = 0; i < schuifParameters.length; i++)
			{
				if(e.getSource() == schuifParameters[i].geefSlider() && !schuifParameters[i].geefSlider().isRaak() && sliderSlepend)
				{	//System.out.println("goede schuifParameter gevonden: " + i);
					//if(schuifParameters[i].geefSlider().getRaak())
					//{
					//	System.out.println("raak");
					//	return;
					//}
					int dx = e.getXOnScreen() - startxv;
					int dy = e.getYOnScreen() - startyv;
					schuifParameters[i].zetLocatie(startSliderX + dx, startSliderY + dy);
					startxv = e.getXOnScreen();
					startyv = e.getYOnScreen();
					startSliderX = startSliderX + dx;
					startSliderY = startSliderY + dy;
					return;
				}
			}
		}
		
		if (tekenComponentAan && e.getSource() == gv)
		{	if (tekenComponent.getCursorMode() == tekenComponent.NOCUR)
			{	
				if (!dragOptie)
					return;
					
				int dx = e.getX() - startxv;
				int dy = e.getY() - startyv;
				beginx = beginx+dx;
				beginy = beginy-dy;
				if(traceOptie && tracex!=-2) 
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
			else if (tekenComponent.getCursorMode() == tekenComponent.DRAW)
			{	// punt slepen
				if (dragPoint != null)
				{	// schermpositie voor drag-event
					Point pix = realPointToPixels(dragPoint);
					int dx = e.getX() - startxv;
					int dy = e.getY() - startyv;	
					// schermpositie na drag-event
					Point dPix = new Point(pix.x + dx, pix.y + dy);
					RealPoint temp = pixelsToRealPoint(dPix);
					temp.setIndex(dragPoint.getIndex());
					if (hasPointWithSameXAs(temp, false))
					{	dragPoint.setX(temp.getX() + 2 * RealPoint.NZERO);					
					}
					else
					{	dragPoint.setX(temp.getX());										
					}	
					dragPoint.setY(temp.getY());
					dragPoint.setxString(Double.toString(dragPoint.getX()));
					dragPoint.setyString(Double.toString(dragPoint.getY()));
					removePoint(dragPoint.getTabelIndex(), dragPoint.getIndex(), false);
					addInsert(dragPoint, false);
					
					
					repaint();
					
					startxv = e.getX();
					startyv = e.getY();
				}	
			}
			else if (tekenComponent.getCursorMode() == tekenComponent.DRAG)
			{
				// punt slepen
				if (dragPoint != null)
				{	// schermpositie voor drag-event
					Point pix = realPointToPixels(dragPoint);
					int dx = e.getX() - startxv;
					int dy = e.getY() - startyv;	
					// schermpositie na drag-event
					Point dPix = new Point(pix.x + dx, pix.y + dy);
					RealPoint temp = pixelsToRealPoint(dPix);
					temp.setIndex(dragPoint.getIndex());
					if (hasPointWithSameXAs(temp, false))
					{	dragPoint.setX(temp.getX() + 2 * RealPoint.NZERO);															
					}
					else
					{	dragPoint.setX(temp.getX());										
					}	
					dragPoint.setY(temp.getY());
					dragPoint.setxString(Double.toString(dragPoint.getX()));
					dragPoint.setyString(Double.toString(dragPoint.getY()));
					removePoint(dragPoint.getTabelIndex(), dragPoint.getIndex(), false);
					addInsert(dragPoint, false);					
					
					repaint();
					
					startxv = e.getX();
					startyv = e.getY();
				}
				// grafiek slepen als drag==true
				else if ((dragPoint == null) && (otherPoint == null) && dragOptie)
				{	int dx = e.getX() - startxv;
					int dy = e.getY() - startyv;					
					beginx = beginx+dx;
					beginy = beginy-dy;
					
					if(traceOptie && tracex!=-2) 
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
		else if(!dragOptie)
			return;
		else if (e.getSource() == gv)//en niet de slider!
		{	int dx = e.getX() - startxv;
			int dy =  e.getY() - startyv;					
			beginx = beginx+dx;
			beginy = beginy-dy;
			
			if(traceOptie && tracex!=-2) 
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
		
	repaint();
	}
	
	
	public void mouseReleased(MouseEvent e)
	{	//resize = false;
		if(sliderSlepend)
			sliderSlepend = false;
		if (!tekenComponentAan && e.getSource() == gv) 
		{	double beginxR = beginx;
			beginx = eenheidx*Math.round(beginx/eenheidx);
			beginy = eenheidy*Math.round(beginy/eenheidy);
			
			if(traceOptie && tracex!=-2) 
			{	tracexD += beginx-beginxR;
				tracex += beginx-beginxR;
				slider.zetStand(tracex);
			}
			
			repaint();
		}
		else if (e.getSource() == gv)
		{	if (tekenComponent.getCursorMode() == tekenComponent.NOCUR)
			{	double beginxR = beginx;
				beginx = eenheidx*Math.round(beginx/eenheidx);
				beginy = eenheidy*Math.round(beginy/eenheidy);
				
				if(traceOptie && tracex!=-2) 
				{	tracexD += beginx-beginxR;
					tracex += beginx-beginxR;
					slider.zetStand(tracex);
				}
				
				
				repaint();
			}
			else if (tekenComponent.getCursorMode() == tekenComponent.DRAW)
			{	if (dragPoint != null)
				{	// corrigeer dragPoint				
					removePoint(dragPoint.getTabelIndex(), dragPoint.getIndex(), false);
					Point dragPix = realPointToPixels(dragPoint);
					if(snapToGridPoints)
					{	dragPix.x = closestGridPix(dragPix.x, true);
   						dragPix.y = closestGridPix(dragPix.y, false);
					}
					int freePixX = closestFreePixX(dragPix.x);
					RealPoint temp = pixelsToRealPoint(
						new Point(freePixX, dragPix.y));
					dragPoint.setX(temp.getX());
					dragPoint.setY(temp.getY());
					dragPoint.setxString(Double.toString(dragPoint.getX()));
					dragPoint.setyString(Double.toString(dragPoint.getY()));
					addInsert(dragPoint, false);
				
					dragPoint = null;
					
					repaint();
				}
			}
			else if (tekenComponent.getCursorMode() == tekenComponent.DRAG)
			{	if (dragPoint != null)
				{	// corrigeer dragPoint								
					
					removePoint(dragPoint.getTabelIndex(), dragPoint.getIndex(), false);
					Point dragPix = realPointToPixels(dragPoint);
					if(snapToGridPoints)
					{	dragPix.x = closestGridPix(dragPix.x, true);
   						dragPix.y = closestGridPix(dragPix.y, false);
					}
					int freePixX = closestFreePixX(dragPix.x);
					RealPoint temp = pixelsToRealPoint(
						new Point(freePixX, dragPix.y));
					dragPoint.setX(temp.getX());
					dragPoint.setY(temp.getY());	
					dragPoint.setxString(Double.toString(dragPoint.getX()));
					dragPoint.setyString(Double.toString(dragPoint.getY()));
					addInsert(dragPoint, false);							
					
					dragPoint = null;
					
					repaint();
				}
				// grafiek slepen
				else if ((dragPoint == null) && (otherPoint == null) && dragOptie)
				{	double beginxR = beginx;
					beginx = eenheidx*Math.round(beginx/eenheidx);
					beginy = eenheidy*Math.round(beginy/eenheidy);

					if(traceOptie && tracex!=-2) 
					{	tracexD += beginx-beginxR;
						tracex += beginx-beginxR;
						slider.zetStand(tracex);
					}
					
					repaint();
				}
			}
		}
		xAsNaamTF.setVisible(xVarEditable && gv.activateXAsNaam(e.getX(), e.getY()));
		if(xAsNaamTF.isVisible())xAsNaamTF.requestFocus();
		yAsNaamTF.setVisible(yVarEditable && gv.activateYAsNaam(e.getX(), e.getY()));
		if(yAsNaamTF.isVisible())yAsNaamTF.requestFocus();
		
	}
	
	public void mouseMoved(MouseEvent e)
	{	if (tekenComponentAan  && e.getSource() == gv)
		{	if (tekenComponent.getCursorMode() == tekenComponent.DRAW)
			{	// kijk of de cursor op een punt van de actuele grafiek staat
				int movedX = e.getX();
				int movedY = e.getY();				
				RealPoint drp = null;
				Vector points = getPoints(getActiveIndex(), false);
				{	for (int pCnt = 0; pCnt < points.size(); pCnt++)
					{	RealPoint rp = (RealPoint) points.elementAt(pCnt);
						Point rpPix = realPointToPixels(rp);
						int dis = (int) Math.round(
							Math.sqrt((rpPix.x - movedX) * (rpPix.x - movedX) +
									  (rpPix.y - movedY) * (rpPix.y - movedY)));
						if (dis <= PRAD + 2)
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
					imageURL = GraphTool.class.getResource("resources/tekencursor.gif");
					
					try
					{	BufferedImage bi = ImageIO.read(imageURL);
						drawCursor = Toolkit.getDefaultToolkit().
							createCustomCursor(bi, new Point(10, 23), "TEKEN_CURSOR");
					}
					catch (IndexOutOfBoundsException ioobe)
					{	error = true;
					} catch (IOException er) {
						er.printStackTrace();
						error = true;
					}
					if (!error)
					{	setCursor(drawCursor);
					}
				}
			}
		}
	}
	
	
	public void mouseExited(MouseEvent e)
	{	if (e.getSource() == gv)
		{	setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}
	public void mouseClicked(MouseEvent e){;}
	public void mouseEntered(MouseEvent e)
	{	if (tekenComponentAan && e.getSource() == gv)
		{	if (tekenComponent.getCursorMode() == tekenComponent.DRAW)
			{	boolean error = false;
				Cursor drawCursor = null;
				imageURL = GraphTool.class.getResource("resources/tekencursor.gif");
				
				try
				{	BufferedImage bi = ImageIO.read(imageURL);
					drawCursor = Toolkit.getDefaultToolkit().
						createCustomCursor(bi, new Point(10, 23), "TEKEN_CURSOR");
				}
				catch (IndexOutOfBoundsException ioobe)
				{	error = true;
				} 
				catch (IOException er) {
					er.printStackTrace();
					error = true;
				}
				if (!error)
				{	setCursor(drawCursor);
				}
			}
			else if (tekenComponent.getCursorMode() == tekenComponent.DELETE)
			{	boolean error = false;
				Cursor deleteCursor = null;
				imageURL = GraphTool.class.getResource("resources/gumcursor.gif");
				try
				{	BufferedImage bi = ImageIO.read(imageURL);
					deleteCursor = Toolkit.getDefaultToolkit().
						createCustomCursor(bi, new Point(10, 23), "GUM_CURSOR");
				}
				catch (IndexOutOfBoundsException ioobe)
				{	error = true;
				}
				catch (IOException er) {
					er.printStackTrace();
					error = true;
				}
				if (!error)
				{	setCursor(deleteCursor);
				}
			}
			else if (tekenComponent.getCursorMode() == tekenComponent.DRAG)
			{	setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
			
		}
	}	
	
	
	
	public void actionPerformed(ActionEvent e)
	{	if(zoomDraad!=null && zoomDraad.isAlive())return;
	
		if(e.getSource()==feedbackTekst)
		{	if(getParent()!=null)feedbackTekst.getParent().remove(feedbackTekst);
		}
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
			{	beginx = beginxDocent;
				beginy = beginyDocent;
				double beginxVorig = beginx;
				tracexD = beginx -(beginxVorig - tracexD)*schaalFactorX;
				factorRijNummerX = 99;
				factorRijNummerY = 99;
				schaalFactorX = docentSchaalFactorX;
				schaalFactorY = docentSchaalFactorY;
				beginwaarde = 0;
				selectnummer = 999;
				
				tracex = (int) Math.round(tracexD);
				slider.zetStand(tracex);
				
				repaint();
			}
		}
		if(e.getSource() == slider)
		{ 	if(e.getActionCommand().equals("start")) 
				tracing = true;
			tracex = slider.geefStand();
			tracexD = tracex;
			
			repaint();
			
		}
			
		if(e.getSource()== xAsNaamTF)
		{	xAsNaamTF.setVisible(false);
		}
		if(e.getSource()== yAsNaamTF)
		{	yAsNaamTF.setVisible(false);
		}
		
		if (typeOpdracht == VINDFORMULEBIJGRAFIEK || typeOpdracht == VINDFORMULEBIJPUNTEN)
		{	//if (e.getSource() == functieComponent)
			if (e.getSource() == formuleComponent)
			{	if(mode == 0 || mode ==1)
				{	kijkNa();
					if(ingevuld) produceAction("checked");
				}
			}		
		}
		else if (typeOpdracht == TEKENTABELPUNTEN)
		{	if (e.getActionCommand().equals("points changed")) 
			{	
				setColor(0, new Color(0,0,255), false);	
				tabelComponent.zetTabelPunten(docentGraphPoints, false);
				
				repaint();
				if(graphPoints.size() >= docentGraphPoints.size())
				{	kijkNaButton.setEnabled(true);
				}
				else
				{	kijkNaButton.setEnabled(false);
					groenVinkjeLabel.setVisible(false);
					oranjeVinkjeLabel.setVisible(false);
					kruisjeLabel.setVisible(false);
					
					repaint();
					score = 0;
					correct = false;
	    			produceAction("changed");
				}
			}	
			if (e.getSource() == kijkNaButton)
			{
				kijkNa();
				if((mode == 0 || mode ==1) && ingevuld)
					produceAction("checked");
			}
		}
		else if (typeOpdracht == TEKENPUNTENBIJFORMULE)
		{	if (e.getActionCommand().equals("points changed")) 
			{	int kleinsteMinimum = minimumPunten[0];
	    		for(int i = 1; i < aantalFuncties; i++)
	    			if(minimumPunten[i] < kleinsteMinimum)
	    				kleinsteMinimum = minimumPunten[i];
    		
				if(graphPoints.size() >= kleinsteMinimum)
				{	kijkNaButton.setEnabled(true);
				}
				else
				{	kijkNaButton.setEnabled(false);
					groenVinkjeLabel.setVisible(false);
					oranjeVinkjeLabel.setVisible(false);
					kruisjeLabel.setVisible(false);
					
					repaint();
					score = 0;
					correct = false;
	    			produceAction("changed");
				}
			}
			if (e.getSource() == kijkNaButton)
			{
				kijkNa();
				if((mode == 0 || mode ==1) && ingevuld)
					produceAction("checked");
			}
		}
		if(schuifParameters != null)
		{	for(int i = 0; i < schuifParameters.length; i++)
			{	if(e.getSource() == schuifParameters[i].geefSlider())
				{	schuifParameters[i].zetWaarde(schuifParameters[i].geefDoubleStand(), true);
					for(int j = 0; j < formuleComponent.getAantalRegels(); j++)
						formuleComponent.parseFormule(j, false);
					for(int j = 0; j < graphPoints.size(); j++)
					{	RealPoint rp = (RealPoint) graphPoints.get(j);
						if(rp.getxString().equals(schuifParameters[i].geefNaam()))
							rp.setX(schuifParameters[i].geefWaarde());
						if(rp.getyString().equals(schuifParameters[i].geefNaam()))
							rp.setY(schuifParameters[i].geefWaarde());
					}
					gv.repaint();
				}
			}
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
            eenheidxD = xAsLog?2*eenheid:eenheid;
			eenheidyD = yAsLog?2*eenheid:eenheid;
			eenheidx = xAsLog?2*eenheid:eenheid;
			eenheidy = yAsLog?2*eenheid:eenheid;
			double stapx, stapy;
			double factorx = 1;
			double factory = 1;
			
			double middenx = veldb/2/eenheidx*eenheidx;
			double middeny = veldh/2/eenheidy*eenheidy;
			
			if(in && x)
			{	if(factorRijNummerX%3==2)
					factorx=0.4;
				else 
					factorx=0.5;
			}
			else if(!in && x)
			{	if(factorRijNummerX%3==1)
					factorx=2.5;
				else 
					factorx=2;
			}
			
			if(in && y)
			{	if(factorRijNummerY%3==2)
					factory =0.4;
				else 
					factory=0.5;
			}
			
			else if(!in && y)
			{	if(factorRijNummerY%3==1)
					factory =2.5;
				else 
					factory=2;
			}
			
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
				beginx =  middenx -(middenx - beginx)/stapx;
				beginy =  middeny -(middeny - beginy)/stapy;
				
				tracexD = middenx -(middenx - tracexD)/stapx;
				
				beginwaarde = 1-(int)Math.round(beginx/eenheidx);
				tracex = (int) Math.round(tracexD);
				slider.zetStand(tracex);
				
				repaint();
			}
			
			schaalFactorX*=factorx;
			if(in && x)factorRijNummerX--;
			if(!in && x)factorRijNummerX++;
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
				beginx =  middenx -(middenx - beginx)/stapx;
				beginy =  middeny -(middeny - beginy)/stapy;
				
				tracexD = middenx -(middenx - tracexD)/stapx;
				
				beginwaarde = 1-(int)Math.round(beginx/eenheidx);
				tracex = (int) Math.round(tracexD);
				slider.zetStand(tracex);
				
				repaint();
			}
			beginwaarde = 1-(int)Math.round(beginx/eenheidx);
			double beginwaardeD = 1.0-(beginx/eenheidx);
			
			tracexD = tracexD + eenheid*(beginwaardeD - beginwaarde);
			tracex = (int) Math.round(tracexD);
			slider.zetStand(tracex);
			
			if(x)selectnummer = 999;
			
			repaint();
			
		}
		public void maakDood()
		{	dood = true;
		}
	}

}

