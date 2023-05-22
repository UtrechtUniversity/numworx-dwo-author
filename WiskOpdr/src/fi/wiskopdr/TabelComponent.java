package fi.wiskopdr;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

import fi.beans.stringutils.StringUtils;
import fi.wiskopdr.expressies.*;
import fi.wiskopdr.formuleobjects.FormuleParser;

import java.util.Hashtable;
import java.util.Vector;
import java.text.*;

public class TabelComponent extends JPanel
							implements ActionListener, 
									   MouseListener, MouseMotionListener
{	

	// gebruik in combinatie met functieEditor			 
	private int aantalExpressies;
	private int maxAantalExpressies;
	private Expressie[] expressies;
	private String[] expressieNamen;
	// de actieve expressie	 	 
	private Expressie exp;
	// nummer van de actieve expressie in het array
	private int expNum;

	// voor zoomen (alleen i.c.m. functieEditor)
	private boolean zooming;
	private double beginX;
	private double schaalFactorX;
	private int factorRijNummerX;

	// gebruik als tekenTool	 
	private boolean isTekenTool; 
	 
	// gebruik als tekentool of standalone tabel 
	private Vector tabelPunten = new Vector();
	private Vector tabelPuntVakIndex = new Vector();
	private int maxTables = 3;	
	private int numTables = 3;
	private int activeIndex = 1;
	private Color[] colors = 
		{Color.blue, new Color(0,200,0), new Color(255,50,50)};
	public static int PRAD = 2;
	private GrafiekComponent grafiekComponent;			

	private boolean eenTabel;


	// alleen i.c.m. functieEditor
	private JButton zoomInButton;
	private JButton zoomUitButton;
	private JComboBox expressieKeuze;
	boolean updatingList = false;

	// gebruik als tekentool
	private JComboBox tabelKeuze;
	private JButton resetButton;
	boolean tabelKeuzeEnabled = true;

	// altijd aanwezig
	private JButton pijlLinksButton, pijlRechtsButton;

	private String varNaam = "x";
	private String yNaam = "y";
	private String yAsNaam = "y";
	 
	Font f, fIt;
	FontMetrics fm, fmIt;

	private int offSet;

	private int linkerBreedteTabel;
	private int linkerBreedteTool;	
	private int linkerBreedte;
	private int rechterBreedte;	
	 
	private int labelBreedte;
	private JLabel varNaamLabel, yNaamLabel;

	private int vakBreedte;	 
	private int vakHoogte;
	private JPanel xVakkenPanel, yVakkenPanel;
	 
	 // het aantal vakken bij constructie
	private int aantalVakken;
	private int firstIndexVisible;
	 
	private Vector xVakken;
	private Vector yVakken;
	 
	private boolean xEditable;
	private boolean yEditable;
	private boolean yVisible;
	
	private boolean frozen;
	 
	private DecimalFormatSymbols dfs;
	private DecimalFormat df;
	
	// randomisatie
	private boolean randomAllowed;
	//private boolean puntenNaN;
	private Vector tabelStringsX = new Vector();
	private Vector tabelStringsY = new Vector();
	 
	private static String[] imageNames = 
	{	
		"pijllinks.gif",
		// "pijllinks_rollover.gif",
		// "pijllinks_selected.gif",
		"pijlrechts.gif",
		// "pijlrechts_rollover.gif",
		// "pijlrechts_selected.gif",
		"zoominknop.gif",
		// "zoominknop_rollover.gif",
		// "zoominknop_selected.gif",
		"zoomuitknop.gif",
		// "zoomuitknop_rollover.gif",
		// "zoomuitknop_selected.gif",
		// reset		
		"teken_wisknop_default.gif",
		"teken_wisknop_rollover.gif",
		"teken_wisknop_selected.gif",
	};
	private static Hashtable images;
	 
	
	public TabelComponent(int breedte)
	{	
	
		setLayout(null);	

		if (images == null)
		{	images = new Hashtable();
			WiskOpdr.loadImages(images, imageNames);
		}

		maxAantalExpressies = 50;
		expressies = new Expressie[maxAantalExpressies];
		expressieNamen = new String[maxAantalExpressies];
		aantalExpressies = 0;
		exp = null;
		expNum = -1;
		
		
		beginX = -2;
		schaalFactorX = 1;
		factorRijNummerX = 99;

		zooming = true;
		isTekenTool = false;
		eenTabel = false;

		xEditable = false;
		yEditable = false;
		yVisible = true;
		frozen = false;
		randomAllowed = false;
		//puntenNaN = false;
	
		addMouseListener(this);
		addMouseMotionListener(this);
		
		f = new Font("SansSerrif",Font.PLAIN,12);
		fm = getFontMetrics(f);
		fIt = new Font("SansSerrif",Font.ITALIC,12);
		fmIt = getFontMetrics(fIt);
		
		dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		df = new DecimalFormat("0.###", dfs);

		// altijd

		offSet = 1;
		// minimale labelBreedte
		labelBreedte = 20;	
		// minimale vakBreedte
		vakBreedte = 33;
		// vakHoogte
		vakHoogte = 20;
		// ruimte links/rechte
		linkerBreedteTabel = 25;
		linkerBreedteTool = 50;
		// default tabel
		linkerBreedte = linkerBreedteTabel;
		rechterBreedte = 25;

		int hoogte = 2 * vakHoogte + 3 * offSet;
		setSize(breedte, hoogte);

		// geen tekentool, links
		zoomInButton = new JButton(new ImageIcon(getImage("zoominknop.gif")));
		zoomInButton.setRolloverIcon(
			new ImageIcon(getImage("zoominknop.gif")));
		zoomInButton.setPressedIcon(
			new ImageIcon(getImage("zoominknop.gif")));
// nog twee nieuwe gifjes			
//		zoomInButton.setBorder(null);
		zoomInButton.setBounds(3*offSet,2*offSet,20,20);
		add(zoomInButton);	
		zoomInButton.addActionListener(this);

		// geen tekentool, rechts
		zoomUitButton = new JButton(new ImageIcon(getImage("zoomuitknop.gif")));
		zoomUitButton.setRolloverIcon(
			new ImageIcon(getImage("zoomuitknop.gif")));
		zoomUitButton.setPressedIcon(
			new ImageIcon(getImage("zoomuitknop.gif")));
// nog twee nieuwe gifjes						
//		zoomUitButton.setBorder(null);
		zoomUitButton.setBounds(getSize().width - 2*offSet - 20, 2*offSet,20,20);
		add(zoomUitButton);	
		zoomUitButton.addActionListener(this);

		// tabel links
		expressieKeuze = new JComboBox();
		expressieKeuze.setFont(fIt);
		expressieKeuze.setBounds(linkerBreedte, 
			2 * offSet + vakHoogte, 49, vakHoogte);
		expressieKeuze.setBackground(new Color(210,210,210));
		expressieKeuze.setForeground(colors[0]);
		//expressieKeuze.setBackground(Color.white);
		expressieKeuze.setVisible(false);
		add(expressieKeuze);
		expressieKeuze.addActionListener(new ExpKeuzeAL());
		expressieKeuze.setRenderer(new TabelKeuzeRenderer());

		// beide varianten
		pijlLinksButton = new JButton(new ImageIcon(getImage("pijllinks.gif")));
		pijlLinksButton.setRolloverIcon(
			new ImageIcon(getImage("pijllinks.gif")));
		pijlLinksButton.setPressedIcon(
			new ImageIcon(getImage("pijllinks.gif")));
// nog twee nieuwe gifjes						
//		pijlLinksButton.setBorder(null);
		pijlLinksButton.setBounds(3*offSet,2*offSet + vakHoogte,20,20);
		add(pijlLinksButton);	
		pijlLinksButton.addActionListener(this);
		
		pijlRechtsButton = new JButton(new ImageIcon(getImage("pijlrechts.gif")));
		pijlRechtsButton.setRolloverIcon(
			new ImageIcon(getImage("pijlrechts.gif")));
		pijlRechtsButton.setPressedIcon(
			new ImageIcon(getImage("pijlrechts.gif")));
// nog twee nieuwe gifjes						
//		pijlRechtsButton.setBorder(null);
		pijlRechtsButton.setBounds(getSize().width - 2*offSet - 20,
								   2*offSet + vakHoogte,20,20);
		add(pijlRechtsButton);	
		pijlRechtsButton.addActionListener(this);

		// tekentool links
		tabelKeuze = new JComboBox();
		tabelKeuze.setBounds(3*offSet, 2*offSet, 45, 20);
		tabelKeuze.setBackground(new Color(210,210,210));
		//tabelKeuze.setBackground(Color.white);
		tabelKeuze.setForeground(colors[0]);
		tabelKeuze.setVisible(false);
		add(tabelKeuze);
		tabelKeuze.addItem("T1");
		tabelKeuze.addItem("T2");
		tabelKeuze.addItem("T3");

		tabelKeuze.setRenderer(new TabelKeuzeRenderer());
		tabelKeuze.addActionListener(new NumTabelAL());

		// tekentool rechts
		resetButton = new JButton(new ImageIcon(getImage("teken_wisknop_default.gif")));
		resetButton.setRolloverIcon(
			new ImageIcon(getImage("teken_wisknop_rollover.gif")));
		resetButton.setPressedIcon(
			new ImageIcon(getImage("teken_wisknop_selected.gif")));
		resetButton.setBorder(null);
		resetButton.setBounds(getSize().width - 2*offSet - 20,
								   2*offSet,20,20);
		resetButton.setVisible(false);
		add(resetButton);	
		resetButton.addActionListener(this);

		
		varNaamLabel = new JLabel(varNaam, SwingConstants.CENTER);
		varNaamLabel.setBackground(new Color(210,210,210));
		varNaamLabel.setOpaque(true);
		varNaamLabel.setFont(fIt);
		varNaamLabel.setBounds(
			linkerBreedte,
			offSet, 
			labelBreedte, vakHoogte);
		add(varNaamLabel);	
		yNaamLabel = new JLabel(yNaam, SwingConstants.CENTER);
		yNaamLabel.setBackground(new Color(210,210,210));		
		yNaamLabel.setOpaque(true);		
		yNaamLabel.setFont(fIt);
		yNaamLabel.setBounds(
			linkerBreedte,
			2 * offSet + vakHoogte,
			labelBreedte, vakHoogte);
		add(yNaamLabel);	
		
		xVakkenPanel = new JPanel();
		xVakkenPanel.setBackground(Color.white);
		xVakkenPanel.setLayout(null);
		xVakkenPanel.setLocation(
			varNaamLabel.getLocation().x + varNaamLabel.getSize().width + offSet,
			offSet);
		xVakkenPanel.setSize(
			getSize().width - xVakkenPanel.getLocation().x - rechterBreedte, 
			vakHoogte+1);
		add(xVakkenPanel);
		yVakkenPanel = new JPanel();
		yVakkenPanel.setBackground(Color.white);		
		yVakkenPanel.setLayout(null);
		yVakkenPanel.setLocation(
			yNaamLabel.getLocation().x + yNaamLabel.getSize().width + offSet,
			2 * offSet + vakHoogte);
		yVakkenPanel.setSize(
			getSize().width - yVakkenPanel.getLocation().x - rechterBreedte, 
			vakHoogte);
		add(yVakkenPanel);

		aantalVakken = 50;
		firstIndexVisible = 0;
		

		// als vector
		xVakken = new Vector(); 
		yVakken = new Vector(); 
		
		for (int vCnt = 0; vCnt < aantalVakken; vCnt++)
		{	TabelVak xVak = 
				new TabelVak(this, vCnt, 
							 vCnt * vakBreedte, 0, vakBreedte, vakHoogte+1, xEditable);
			// constructie, geen actie
			//xVak.zetText("x - " + vCnt);
			xVak.zetFont(f);
			xVakkenPanel.add(xVak);
			xVakken.addElement(xVak);
				
		}
		for (int vCnt = 0; vCnt < aantalVakken; vCnt++)
		{	TabelVak yVak = 
				new TabelVak(this, vCnt, 
							 vCnt * vakBreedte, 0, vakBreedte, vakHoogte, yEditable);
			// constructie geewn actie
			//yVak.zetText("y - " + vCnt);
			yVak.zetFont(f);
			yVakkenPanel.add(yVak);
			yVakken.addElement(yVak);
				
		}
		
		
	} // constructor

	public void setSize(int b, int h)
	{
		super.setSize(b,h);
		if (zoomUitButton != null)
			zoomUitButton.setBounds(getSize().width - 2*offSet - 20, 2*offSet,20,20);
		if(pijlRechtsButton!=null)pijlRechtsButton.setBounds(getSize().width - 2*offSet - 20, 2*offSet + vakHoogte,20,20);
		if(resetButton!=null)resetButton.setBounds(getSize().width - 2*offSet - 20, 2*offSet,20,20);
		if(xVakkenPanel!=null)xVakkenPanel.setSize(getSize().width - xVakkenPanel.getLocation().x - rechterBreedte, vakHoogte+1);
		if(yVakkenPanel!=null)yVakkenPanel.setSize(getSize().width - yVakkenPanel.getLocation().x - rechterBreedte, vakHoogte);
	}
	
	public void setBounds(int x, int y, int b, int h)
	{
		super.setBounds(x,y,b,h);
		if(zoomUitButton!=null)zoomUitButton.setBounds(getSize().width - 2*offSet - 20, 2*offSet,20,20);
		if(pijlRechtsButton!=null)pijlRechtsButton.setBounds(getSize().width - 2*offSet - 20, 2*offSet + vakHoogte,20,20);
		if(resetButton!=null)resetButton.setBounds(getSize().width - 2*offSet - 20, 2*offSet,20,20);
		if(xVakkenPanel!=null)xVakkenPanel.setSize(getSize().width - xVakkenPanel.getLocation().x - rechterBreedte, vakHoogte);
		if(yVakkenPanel!=null)yVakkenPanel.setSize(getSize().width - yVakkenPanel.getLocation().x - rechterBreedte, vakHoogte);
	}

	public void setColor(int nr, Color c)
	{
		colors[nr] = c;
		repaint();
	}
	
	
	public static Image getImage(String name)
	{	return (Image) images.get(name);
	}

	public void zetAlsTekenTool(boolean b)
	{	isTekenTool = b;
		if (isTekenTool)
		{	zoomInButton.setVisible(false);
			zoomUitButton.setVisible(false);
			expressieKeuze.setVisible(false);
			tabelKeuze.setVisible(true);
//System.out.println("yNaam = " + yNaam);			
			zetYNaam(yAsNaam);
			yNaamLabel.setVisible(true);
			resetButton.setVisible(true);
			linkerBreedte = linkerBreedteTool;
			setXEditable(true);
			setYEditable(true);
			
		}
		else
		{	zoomInButton.setVisible(zooming);
			zoomUitButton.setVisible(zooming);
			tabelKeuze.setVisible(false);
			resetButton.setVisible(false);
			linkerBreedte = linkerBreedteTabel;
			setXEditable(false);
			setYEditable(false);
			updateExpressieList();
			
			
		}
		varNaamLabel.setLocation(linkerBreedte, offSet);
		yNaamLabel.setLocation(linkerBreedte, 2 * offSet + vakHoogte);
		xVakkenPanel.setLocation(
			varNaamLabel.getLocation().x + varNaamLabel.getSize().width + offSet,
			offSet);
		xVakkenPanel.setSize(
			getSize().width - xVakkenPanel.getLocation().x - rechterBreedte, 
			vakHoogte);
		yVakkenPanel.setLocation(
			yNaamLabel.getLocation().x + yNaamLabel.getSize().width + offSet,
			2 * offSet + vakHoogte);
		yVakkenPanel.setSize(
			getSize().width - yVakkenPanel.getLocation().x - rechterBreedte, 
			vakHoogte);
	}
	
	public void zetZooming(boolean b)
	{	if (isTekenTool)
			return;
	
		zooming = b;
		zoomInButton.setVisible(zooming);
		zoomUitButton.setVisible(zooming);
	}

	
	// in tekenToolModus kan je maximaal 1 tabel tekenen
	public void zetEenTabel(boolean b)
	{	if (!isTekenTool)
			return;
	
		eenTabel = b;
		if (b)
		{	linkerBreedte = linkerBreedteTabel;
		}
		else
		{	linkerBreedte = linkerBreedteTool;
		}
		varNaamLabel.setLocation(linkerBreedte, offSet);
		yNaamLabel.setLocation(linkerBreedte, 2 * offSet + vakHoogte);
		xVakkenPanel.setLocation(
			varNaamLabel.getLocation().x + varNaamLabel.getSize().width + offSet,
			offSet);
		xVakkenPanel.setSize(
			getSize().width - xVakkenPanel.getLocation().x - rechterBreedte, 
			vakHoogte);
		yVakkenPanel.setLocation(
			yNaamLabel.getLocation().x + yNaamLabel.getSize().width + offSet,
			2 * offSet + vakHoogte);
		yVakkenPanel.setSize(
			getSize().width - yVakkenPanel.getLocation().x - rechterBreedte, 
			vakHoogte);
		
		tabelKeuze.setVisible(!eenTabel);		
	}
	
	public void zetReset(boolean b)
	{	resetButton.setVisible(b);
	}
	
	public int getVakBreedte()
	{	return vakBreedte;
	}

	public void setRandomAllowed(boolean b)
	{	randomAllowed = b;
		// zet alle xVakken
		for (int xCnt = 0; xCnt < xVakken.size(); xCnt++)
		{	TabelVak xVak = (TabelVak) xVakken.elementAt(xCnt);
			xVak.randomAllowed = b;
		}
		// zet alle yVakken
		for (int yCnt = 0; yCnt < yVakken.size(); yCnt++)
		{	TabelVak yVak = (TabelVak) yVakken.elementAt(yCnt);
			yVak.randomAllowed = b;
		}
		
	}
	
	public void setXEditable(boolean b)
	{	xEditable = b;
		// zet alle xVakken op xEditable
		for (int xCnt = 0; xCnt < xVakken.size(); xCnt++)
		{	TabelVak xVak = (TabelVak) xVakken.elementAt(xCnt);
			xVak.zetEditable(b);
		}
	}

	public boolean getXEditable()
	{
		return xEditable;
	}
	public void setYEditable(boolean b)
	{	yEditable = b;
		// zet alle yVakken op yEditable
		for (int yCnt = 0; yCnt < yVakken.size(); yCnt++)
		{	TabelVak yVak = (TabelVak) yVakken.elementAt(yCnt);
			yVak.zetEditable(b);
		}
		
	}

	
	public void setYVisible(boolean b)
	{	yVisible = b;
		
	}
	
	public void setFrozen(boolean b)
	{
		frozen = b;
	}
	
	public void zetFirstIndexVisible(int firstIndexVis)
	{	int dx = 0;
		for (int vCnt = 0; vCnt < firstIndexVis; vCnt++)
		{	TabelVak xVak = (TabelVak) xVakken.elementAt(vCnt);
			dx += xVak.getSize().width;
		}
		for (int xCnt = 0; xCnt < xVakken.size(); xCnt++)
		{	TabelVak xVak = (TabelVak) xVakken.elementAt(xCnt);
			xVak.translate(-dx);
		}
		for (int yCnt = 0; yCnt < yVakken.size(); yCnt++)
		{	TabelVak yVak = (TabelVak) yVakken.elementAt(yCnt);
			yVak.translate(-dx);
		}
		
		firstIndexVisible = firstIndexVis;
		
	}

	public int getFirstIndexVisible()
	{	return firstIndexVisible;
	}

	public Hashtable getState()
	{	
		// dit moet je wel meenemen(?) anders 
		// werkt setState niet goed	
		boolean isTekenTool = false;
		boolean eenTabel = false;
	
		double beginX = -2;
		double schaalFactorX = 1;
		boolean xEditable = false;
		boolean yEditable = false;
// nodig??		
//		int firstIndexVisible = 0;
		int activeIndex = 1;
		Vector tabelPunten = new Vector();
		//Vector tabelPuntIndices = new Vector();
		Vector tabelPuntVakIndex = new Vector();
		Vector tabelStringsX = new Vector();
		Vector tabelStringsY = new Vector();
		
		boolean puntenNaN = false;
	
		isTekenTool = this.isTekenTool;
		eenTabel = this.eenTabel;
	
		beginX = this.beginX;
		schaalFactorX = this.schaalFactorX;
		xEditable = this.xEditable;
		yEditable = this.yEditable;
//		firstIndexVisible = this.firstIndexVisible;
		activeIndex = this.activeIndex;
		tabelPunten = this.tabelPunten;
		tabelPuntVakIndex = this.tabelPuntVakIndex;
		tabelStringsX = this.tabelStringsX;
		tabelStringsY = this.tabelStringsY;
		//puntenNaN = this.puntenNaN; 

		Hashtable h = new Hashtable();
		if (grafiekComponent != null)
			h = grafiekComponent.getState();
	
		h.put("tabelAlsTekenTool", new Boolean(isTekenTool));
		h.put("eenTabel", new Boolean(eenTabel));				
		
		h.put("tabelBeginX", new Double(beginX));
		h.put("tabelSchaalFactorX", new Double(schaalFactorX));
		h.put("xEditable", new Boolean(xEditable));
		h.put("yEditable", new Boolean(yEditable));
//		h.put("firstIndexVisible", new Integer(firstIndexVisible));
		h.put("actieve tabel", new Integer(activeIndex));
		h.put("tabelPunten", tabelPunten);
		//h.put("tabelPuntIndices", tabelPuntIndices);
		h.put("tabelPuntVakIndex", tabelPuntVakIndex);
		h.put("tabelStringsX", tabelStringsX);
		h.put("tabelStringsY", tabelStringsY);
		//h.put("puntenNaN", new Boolean(puntenNaN));
		
		return h;
	}

	public Hashtable getEditState()
	{	String varNaam = "x";
		String yNaam = "y";
		boolean zooming = true;
		boolean isTekenTool = false;
		boolean eenTabel = false;
	
		double beginX = -2;
		double schaalFactorX = 1;
		boolean xEditable = false;
		boolean yEditable = false;
// nodig??		
//		int firstIndexVisible = 0;
		int activeIndex = 1;
		Vector tabelPunten = new Vector();
		//Vector tabelPuntIndices = new Vector();
		Vector tabelPuntVakIndex = new Vector();
		Vector tabelStringsX = new Vector();
		Vector tabelStringsY = new Vector();
		//boolean puntenNaN = false;

		varNaam = this.varNaam;
		yNaam = this.yNaam;
		zooming = this.zooming;
		isTekenTool = this.isTekenTool;
		eenTabel = this.eenTabel;
	
		beginX = this.beginX;
		schaalFactorX = this.schaalFactorX;
		xEditable = this.xEditable;
		yEditable = this.yEditable;
//		firstIndexVisible = this.firstIndexVisible;
		activeIndex = this.activeIndex;
		tabelPunten = this.tabelPunten;
		tabelPuntVakIndex = this.tabelPuntVakIndex;
		tabelStringsX = this.tabelStringsX;
		tabelStringsY = this.tabelStringsY;
		//puntenNaN = this.puntenNaN;

		for (int pCnt = 0; pCnt < tabelPunten.size(); pCnt++)
		{	String xString = "";
			if (tabelPunten.size() == tabelStringsX.size())
				xString = (String) tabelStringsX.elementAt(pCnt);
			String yString = "";
			if (tabelPunten.size() == tabelStringsY.size())
				yString = (String) tabelStringsY.elementAt(pCnt);
			RealPoint rp = (RealPoint) tabelPunten.elementAt(pCnt);
			if ((xString.length() > 2) && (xString.charAt(0) == '#') && 
				(xString.charAt(xString.length() - 1) == '#'))
			{	rp.x = Double.NaN;
				//xString = "";
			}
			if ((yString.length() > 2) && (yString.charAt(0) == '#') && 
				(yString.charAt(yString.length() - 1) == '#'))
			{	rp.y = Double.NaN;
				//yString = "";
			}
			
			setPuntIndex(rp, pCnt);
			
//System.out.println("rpindex = " + rp.index);			
			
		}
		
//System.out.println("getEditState tb = " + tabelPunten.size());		
		
		Hashtable h = new Hashtable();
		if (grafiekComponent != null)
			h = grafiekComponent.getState();
	
		h.put("varNaam", varNaam);
		h.put("yNaam", yNaam);
		h.put("zoomInTabel", new Boolean(zooming));
		h.put("tabelAlsTekenTool", new Boolean(isTekenTool));
		h.put("eenTabel", new Boolean(eenTabel));				
		h.put("tabelBeginX", new Double(beginX));
		h.put("tabelSchaalFactorX", new Double(schaalFactorX));
		h.put("xEditable", new Boolean(xEditable));
		h.put("yEditable", new Boolean(yEditable));
//		h.put("firstIndexVisible", new Integer(firstIndexVisible));
		h.put("actieve tabel", new Integer(activeIndex));
		h.put("tabelPunten", tabelPunten);
		//h.put("tabelPuntIndices", tabelPuntIndices);		
		h.put("tabelPuntVakIndex", tabelPuntVakIndex);
		h.put("tabelStringsX", tabelStringsX);
		h.put("tabelStringsY", tabelStringsY);
		//h.put("puntenNaN", new Boolean(puntenNaN));		
		
		return h;
	}
	
	public void setState(Hashtable h)
	{	
	
//System.out.println("tc setState");
	
		boolean isTekenTool = false;
		boolean eenTabel = false;	
	
		double beginX = -2;
		double schaalFactorX = 1;
		boolean xEditable = false;
		boolean yEditable = false;
// nodig??		
//		int firstIndexVisible = 0;
		int activeIndex = 1;
		Vector tabelPunten = new Vector();
		Vector tabelPuntIndices = new Vector();
		Vector tabelPuntVakIndex = new Vector();
		Vector tabelStringsX = new Vector();
		Vector tabelStringsY = new Vector();
		//boolean puntenNaN = false;

		if (h.containsKey("tabelAlsTekenTool"))
			isTekenTool = ((Boolean) h.get("tabelAlsTekenTool")).booleanValue();		
		if (h.containsKey("eenTabel"))
			eenTabel = ((Boolean) h.get("eenTabel")).booleanValue();		
		
		if (h.containsKey("tabelBeginX"))
			beginX = ((Double) h.get("tabelBeginX")).doubleValue();
    	if (h.containsKey("tabelSchaalFactorX")) 
    		schaalFactorX = ((Double) h.get("tabelSchaalFactorX")).doubleValue();
    	if (h.containsKey("xEditable")) 
    		xEditable = ((Boolean) h.get("xEditable")).booleanValue();
    	if (h.containsKey("yEditable")) 
    		yEditable = ((Boolean) h.get("yEditable")).booleanValue();
//    	if (h.containsKey("firstIndexVisible")) 
//    		firstIndexVisible = ((Integer) h.get("firstIndexVisible")).intValue();	

    	if (h.containsKey("actieve tabel")) 
    		activeIndex = ((Integer) h.get("actieve tabel")).intValue();	
		if (h.containsKey("tabelPunten")) 
    		tabelPunten = (Vector) h.get("tabelPunten");	
		if (h.containsKey("tabelPuntIndices")) 
			tabelPuntIndices = (Vector) h.get("tabelPuntIndices");
		if (h.containsKey("tabelPuntVakIndex")) 
			tabelPuntVakIndex = (Vector) h.get("tabelPuntVakIndex");
		if (h.containsKey("tabelStringsX")) 
			tabelStringsX = (Vector) h.get("tabelStringsX");
		if (h.containsKey("tabelStringsY")) 
			tabelStringsY = (Vector) h.get("tabelStringsY");
		//if (h.containsKey("puntenNaN")) 
		//	puntenNaN = ((Boolean) h.get("puntenNaN")).booleanValue();

		
		this.isTekenTool = isTekenTool;
		this.eenTabel = eenTabel;
    	
    	this.beginX = beginX;	
    	this.schaalFactorX = schaalFactorX;
    	this.xEditable = xEditable;
    	this.yEditable = yEditable;	
    	this.activeIndex = activeIndex;
    	this.tabelPunten = tabelPunten;
    	this.tabelPuntVakIndex = tabelPuntVakIndex;
    	this.tabelStringsX = tabelStringsX;
    	this.tabelStringsY = tabelStringsY;
    	//this.puntenNaN = puntenNaN;

    	setRandomAllowed(false);
    	
		setXEditable(xEditable);
		setYEditable(yEditable);	

		zetAlsTekenTool(isTekenTool);
		zetEenTabel(eenTabel);

		// dit zet de tabelPunten en de comboBox
		// er gebeurt niets als er geen tabelPunten zijn
		setActiveIndex(activeIndex);    	

		

	}
	
	public void setEditState(Hashtable h)
	{	
	
//System.out.println("tc setEditState");	
	
		String varNaam = "x";
		String yNaam = "y";
		String yAsNaam = "y";
		boolean zooming = true;
		boolean isTekenTool = false;
		boolean eenTabel = false;	
		
		double beginX = -2;
		double schaalFactorX = 1;
		boolean xEditable = false;
		boolean yEditable = false;
// nodig??		
//		int firstIndexVisible = 0;
		int activeIndex = 1;
		Vector tabelPunten = new Vector();
		Vector tabelPuntIndices = new Vector();
		Vector tabelPuntVakIndex = new Vector();
		Vector tabelStringsX = new Vector();
		Vector tabelStringsY = new Vector();
		//boolean puntenNaN = false;
		
		if (h.containsKey("varNaam"))
			varNaam = (String) h.get("varNaam");
		if (h.containsKey("yNaam"))
			yNaam = (String) h.get("yNaam");
		if (h.containsKey("yAsNaam"))
			yAsNaam = (String) h.get("yAsNaam");
		if (h.containsKey("zoomInTabel"))
			zooming = ((Boolean) h.get("zoomInTabel")).booleanValue();
		if (h.containsKey("tabelAlsTekenTool"))
			isTekenTool = ((Boolean) h.get("tabelAlsTekenTool")).booleanValue();		
		if (h.containsKey("eenTabel"))
			eenTabel = ((Boolean) h.get("eenTabel")).booleanValue();		
		
		if (h.containsKey("tabelBeginX"))
			beginX = ((Double) h.get("tabelBeginX")).doubleValue();
    	if (h.containsKey("tabelSchaalFactorX")) 
    		schaalFactorX = ((Double) h.get("tabelSchaalFactorX")).doubleValue();
    	if (h.containsKey("xEditable")) 
    		xEditable = ((Boolean) h.get("xEditable")).booleanValue();
    	if (h.containsKey("yEditable")) 
    		yEditable = ((Boolean) h.get("yEditable")).booleanValue();
//    	if (h.containsKey("firstIndexVisible")) 
//    		firstIndexVisible = ((Integer) h.get("firstIndexVisible")).intValue();	

    	if (h.containsKey("actieve tabel")) 
    		activeIndex = ((Integer) h.get("actieve tabel")).intValue();	
		if (h.containsKey("tabelPunten")) 
    		tabelPunten = (Vector) h.get("tabelPunten");
		if (h.containsKey("tabelPuntIndices")) 
			tabelPuntIndices = (Vector) h.get("tabelPuntIndices");
		if (h.containsKey("tabelPuntVakIndex")) 
			tabelPuntVakIndex = (Vector) h.get("tabelPuntVakIndex");
		if (h.containsKey("tabelStringsX")) 
			tabelStringsX = (Vector) h.get("tabelStringsX");
		if (h.containsKey("tabelStringsY")) 
			tabelStringsY = (Vector) h.get("tabelStringsY");
		//if (h.containsKey("puntenNaN")) 
		//	puntenNaN = ((Boolean) h.get("puntenNaN")).booleanValue();

		this.varNaam = varNaam;
		this.yNaam = yNaam;
		this.yAsNaam = yAsNaam;
		this.zooming = zooming;
		this.isTekenTool = isTekenTool;
		this.eenTabel = eenTabel;
    	
    	this.beginX = beginX;	
    	this.schaalFactorX = schaalFactorX;
    	this.xEditable = xEditable;
    	this.yEditable = yEditable;	
    	this.activeIndex = activeIndex;
    	this.tabelPunten = tabelPunten;
    	this.tabelPuntVakIndex = tabelPuntVakIndex;
    	this.tabelStringsX = tabelStringsX;
    	this.tabelStringsY = tabelStringsY;
    	//this.puntenNaN = puntenNaN;

    	setRandomAllowed(true);
    	
		setXEditable(xEditable);
		setYEditable(yEditable);	
    	
		zetVarNaam(varNaam);
		zetYNaam(yNaam);
		zetZooming(zooming);
		zetAlsTekenTool(isTekenTool);
		zetEenTabel(eenTabel);
		
//System.out.println("setEditState + tb1 = " + this.tabelPunten.size());		
		
		// dit zet de tabelPunten en de comboBox
		// er gebeurt niets als er geen tabelPunten zijn
		setActiveIndex(activeIndex);    	
    		
//System.out.println("setEditState + tb2 = " + this.tabelPunten.size());

		if (grafiekComponent != null)
		{	
//System.out.println("gc != null");

			grafiekComponent.zetVarNaam(varNaam);
			grafiekComponent.zetYAsLabel(yNaam);
			grafiekComponent.setState(h);
			grafiekComponent.repaint();
			
		}

//System.out.println("setEditState + tb3 = " + this.tabelPunten.size());		
	}

	public void zetOpdracht(Hashtable h, String[] randomVars, Hashtable randomValues)
	{
		
//System.out.println("tc zetOpdracht");
		
		String varNaam = "x";
		String yNaam = "y";
		boolean zooming = true;
		boolean isTekenTool = false;
		boolean eenTabel = false;	
		
		double beginX = -2;
		double schaalFactorX = 1;
		boolean xEditable = false;
		boolean yEditable = false;
// nodig??		
//		int firstIndexVisible = 0;
		int activeIndex = 1;
		Vector tabelPunten = new Vector();
		Vector tabelPuntIndices = new Vector();
		Vector tabelPuntVakIndex = new Vector();
		Vector tabelStringsX = new Vector();
		Vector tabelStringsY = new Vector();
		//boolean puntenNaN = false;
		
		if (h.containsKey("varNaam"))
			varNaam = (String) h.get("varNaam");
		if (h.containsKey("yNaam"))
			yNaam = (String) h.get("yNaam");
		if (h.containsKey("zoomInTabel"))
			zooming = ((Boolean) h.get("zoomInTabel")).booleanValue();
		if (h.containsKey("tabelAlsTekenTool"))
			isTekenTool = ((Boolean) h.get("tabelAlsTekenTool")).booleanValue();		
		if (h.containsKey("eenTabel"))
			eenTabel = ((Boolean) h.get("eenTabel")).booleanValue();		
		
		if (h.containsKey("tabelBeginX"))
			beginX = ((Double) h.get("tabelBeginX")).doubleValue();
    	if (h.containsKey("tabelSchaalFactorX")) 
    		schaalFactorX = ((Double) h.get("tabelSchaalFactorX")).doubleValue();
    	if (h.containsKey("xEditable")) 
    		xEditable = ((Boolean) h.get("xEditable")).booleanValue();
    	if (h.containsKey("yEditable")) 
    		yEditable = ((Boolean) h.get("yEditable")).booleanValue();
//    	if (h.containsKey("firstIndexVisible")) 
//    		firstIndexVisible = ((Integer) h.get("firstIndexVisible")).intValue();	

    	if (h.containsKey("actieve tabel")) 
    		activeIndex = ((Integer) h.get("actieve tabel")).intValue();	
		if (h.containsKey("tabelPunten")) 
    		tabelPunten = (Vector) h.get("tabelPunten");
		if (h.containsKey("tabelPuntIndices")) 
			tabelPuntIndices = (Vector) h.get("tabelPuntIndices");
		if (h.containsKey("tabelPuntVakIndex")) 
			tabelPuntVakIndex = (Vector) h.get("tabelPuntVakIndex");
		if (h.containsKey("tabelStringsX")) 
			tabelStringsX = (Vector) h.get("tabelStringsX");
		if (h.containsKey("tabelStringsY")) 
			tabelStringsY = (Vector) h.get("tabelStringsY");
		//if (h.containsKey("puntenNaN")) 
		//	puntenNaN = ((Boolean) h.get("puntenNaN")).booleanValue();
		
		this.varNaam = varNaam;
		this.yNaam = yNaam;
		this.zooming = zooming;
		this.isTekenTool = isTekenTool;
		this.eenTabel = eenTabel;
    	
    	this.beginX = beginX;	
    	this.schaalFactorX = schaalFactorX;
    	this.xEditable = xEditable;
    	this.yEditable = yEditable;	
    	this.activeIndex = activeIndex;
		this.tabelPunten = tabelPunten;
		this.tabelPuntVakIndex = tabelPuntVakIndex;
    	this.tabelStringsX = tabelStringsX;
    	this.tabelStringsY = tabelStringsY;
		//this.puntenNaN = puntenNaN;
		
//System.out.println("tc zetOpdr " + tabelPunten.size());

		for (int pCnt = 0; pCnt < tabelPunten.size(); pCnt++)
		{	String xString = "";
			if (tabelStringsX.size() == tabelPunten.size())
				xString = (String) tabelStringsX.elementAt(pCnt);
			String yString = "";
			if (tabelStringsY.size() == tabelPunten.size())
				yString = (String) tabelStringsY.elementAt(pCnt);
			RealPoint rp = (RealPoint) tabelPunten.elementAt(pCnt);
			
			//if ((xString.length() > 2) && (xString.charAt(0) == '#') && 
			//	(xString.charAt(xString.length() - 1) == '#'))
			//{	rp.x = substitueerRandom(rp.x, xString, randomVars, randomValues);					
			//}
			
			//vervangen door:
			try 
			{	xString = FormuleParser.randomizeString("$f" + xString + "@", randomVars, randomValues);
			}
			catch(Exception e)
			{	xString = "";
			}
//System.out.println("xString:  " + xString);
			Expressie ex = FormuleParser.geefExpressie(xString);
			if (ex != null)
				rp.x = ex.geefWaarde();
			
			//if ((yString.length() > 2) && (yString.charAt(0) == '#') && 
			//	(yString.charAt(yString.length() - 1) == '#'))
			//{	rp.y = substitueerRandom(rp.y, yString, randomVars, randomValues);
			//}
			
			try 
			{	yString = FormuleParser.randomizeString("$f" + yString + "@", randomVars, randomValues);
			}
			catch(Exception e)
			{	yString = "";
			}
//System.out.println("yString:  " + yString);
			Expressie ey = FormuleParser.geefExpressie(yString);
			if (ey != null)
				rp.y = ey.geefWaarde();
			
			// just in case
			if (Double.isNaN(rp.x))
				rp.x = 0;
			if (Double.isNaN(rp.y))
				rp.y = 0;
			
			rp.index = rp.index % 100;
			
		}

		setRandomAllowed(false);

    	setXEditable(xEditable);
    	setYEditable(yEditable);	

		zetVarNaam(varNaam);
		zetYNaam(yNaam);
		zetZooming(zooming);
		zetAlsTekenTool(isTekenTool);
		zetEenTabel(eenTabel);
		// dit zet de tabelPunten en de comboBox
		// er gebeurt niets als er geen tabelPunten zijn
		setActiveIndex(activeIndex);    	
    		
		if (grafiekComponent != null)
		{	
			grafiekComponent.zetVarNaam(varNaam);
			grafiekComponent.zetYAsLabel(yNaam);
			grafiekComponent.setState(h);
			grafiekComponent.repaint();
		}
		if (!isTekenTool)
			updateExpressieList();
		repaint();
	
	}
	
	public static double substitueerRandom(double def, String s, String[] randomVars, Hashtable randomValues) 
	{	double d = Double.NaN;
		s = s.substring(1, s.length() - 1);
		String[] delen = StringUtils.split(s, "/");
		int decFactor = 1;
		
		for (int j = 0 ; j < randomVars.length; j++)
		{	
//System.out.println("rava " + j + " " + randomVars[j]);			
			if (randomVars[j].equals(delen[0])) 
				d = ((Integer) randomValues.get(randomVars[j])).intValue();
		}
		if (delen.length > 1)
		{	decFactor = Integer.parseInt(delen[1]);
			d = d / decFactor;
		}
		if (Double.isNaN(d)) 
			d = def;
		return d;
	}
	
	public void zetGrafiekComponent(GrafiekComponent gc)
	{	grafiekComponent = gc;
	}


	
	public void zetVarNaam(String vn)
	{	varNaam = vn;
		varNaamLabel.setText(vn);
		int width = fmIt.stringWidth(" " + vn + " ");
		if (width <= labelBreedte)
		{	varNaamLabel.setSize(labelBreedte, vakHoogte);
			yNaamLabel.setSize(labelBreedte, vakHoogte);	
		} 
		else if ((width > labelBreedte) && (width <= 2 * labelBreedte))
		{	varNaamLabel.setSize(width, vakHoogte);
			yNaamLabel.setSize(width, vakHoogte);	
		}
		else
		{	varNaamLabel.setSize(2 * labelBreedte, vakHoogte);
			yNaamLabel.setSize(2 * labelBreedte, vakHoogte);	
		}
		xVakkenPanel.setLocation(
			varNaamLabel.getLocation().x + varNaamLabel.getSize().width + offSet,
			offSet);
		xVakkenPanel.setSize(
			getSize().width - xVakkenPanel.getLocation().x - rechterBreedte, 
			vakHoogte);
		yVakkenPanel.setLocation(
			yNaamLabel.getLocation().x + yNaamLabel.getSize().width + offSet,
			2 * offSet + vakHoogte);
		yVakkenPanel.setSize(
			getSize().width - yVakkenPanel.getLocation().x - rechterBreedte, 
			vakHoogte);
		if (grafiekComponent != null)
			grafiekComponent.zetVarNaam(vn);		
	}

	public void zetYNaam(String yn)
	{	yNaam = yn;
		yNaamLabel.setText(yn);
		int width = fmIt.stringWidth(" " + yn + " ");
		if (width <= labelBreedte)
		{	varNaamLabel.setSize(labelBreedte, vakHoogte);
			yNaamLabel.setSize(labelBreedte, vakHoogte);	
		} 
		else if ((width > labelBreedte) && (width <= 2 * labelBreedte))
		{	varNaamLabel.setSize(width, vakHoogte);
			yNaamLabel.setSize(width, vakHoogte);	
		}
		else
		{	varNaamLabel.setSize(2 * labelBreedte, vakHoogte);
			yNaamLabel.setSize(2 * labelBreedte, vakHoogte);	
		}
		xVakkenPanel.setLocation(
			varNaamLabel.getLocation().x + varNaamLabel.getSize().width + offSet,
			offSet);
		xVakkenPanel.setSize(
			getSize().width - xVakkenPanel.getLocation().x - rechterBreedte, 
			vakHoogte);
		yVakkenPanel.setLocation(
			yNaamLabel.getLocation().x + yNaamLabel.getSize().width + offSet,
			2 * offSet + vakHoogte);
		yVakkenPanel.setSize(
			getSize().width - yVakkenPanel.getLocation().x - rechterBreedte, 
			vakHoogte);
		
	}

	public void showExpressieKeuze(boolean b)
	{	expressieKeuze.setVisible(b);
		// als zichtbaar: verschuiven
		if (b)	
		{	yNaamLabel.setVisible(false);
			xVakkenPanel.setLocation(
				varNaamLabel.getLocation().x + expressieKeuze.getSize().width + offSet,
				offSet);
			xVakkenPanel.setSize(
				getSize().width - xVakkenPanel.getLocation().x - rechterBreedte, 
				vakHoogte);
			yVakkenPanel.setLocation(
				yNaamLabel.getLocation().x + expressieKeuze.getSize().getSize().width + offSet,
				2 * offSet + vakHoogte);
			yVakkenPanel.setSize(
				getSize().width - yVakkenPanel.getLocation().x - rechterBreedte, 
				vakHoogte);
		}
	}
	
	public void paintComponent(Graphics g)
	{	
		int breedte = getSize().width;
		int hoogte = getSize().height;

//g.setColor(Color.yellow);
		g.setColor(new Color(210,210,210));
		g.fillRect(0, 0, breedte, hoogte);					
	
		//g.setColor(Color.gray);
		//g.drawRect(0, 0, breedte - 1, hoogte - 1);				

		//g.setColor(new Color(210,210,210));
		
		// vakjes
		// varNaamLabel
		
		//g.drawRect(
		//	varNaamLabel.getLocation().x - 1,
		//	varNaamLabel.getLocation().y - 1,
		//	varNaamLabel.getSize().width,
		//	varNaamLabel.getSize().height);
			
		// yNaamLabel	
		if (yNaamLabel.isVisible())
		{
			g.drawRect(
				yNaamLabel.getLocation().x - 1,
				yNaamLabel.getLocation().y - 1,
				yNaamLabel.getSize().width,
				yNaamLabel.getSize().height);
		}		
		// xVakkenPanel
		//g.drawRect(
		//	xVakkenPanel.getLocation().x - 1,
		//	xVakkenPanel.getLocation().y - 1,
		//	xVakkenPanel.getSize().width + 1,
		//	xVakkenPanel.getSize().height);
		// yVakkenPanel
		g.drawRect(
			yVakkenPanel.getLocation().x - 1,
			yVakkenPanel.getLocation().y - 1,
			yVakkenPanel.getSize().width + 1,
			yVakkenPanel.getSize().height);
			
	}

	// tekentool

	public int getNumTables()
	{	return numTables;
	}

	public int getActiveIndex()
	{	return activeIndex;
	}

	public Color getColor(int index)
	{	return colors[index - 1];
	}

	public void setActiveIndex(int index)
	{	if ((index < 1) || (index > maxTables))
			activeIndex = 1;
		else
			activeIndex = index;
		tabelKeuzeEnabled = false;
		tabelKeuze.setSelectedIndex(index - 1);
		tabelKeuzeEnabled = true;
		zetTabelPunten(activeIndex);
		
		repaint();

		if (grafiekComponent != null)
			grafiekComponent.repaint();			
	}

	// dit maakt de fysieke tabel leeg
	// maar verwijdert niet de punten!!
	public void reset()
	{	// schuif de vakjes terug naar rechts als nodig
		if (firstIndexVisible > 0)
		{	TabelVak firstVak = (TabelVak) xVakken.elementAt(0);
			int dx = firstVak.getLocation().x; // negatief
			for (int xCnt = 0; xCnt < xVakken.size(); xCnt++)
			{	TabelVak xVak = (TabelVak) xVakken.elementAt(xCnt);
				xVak.translate(- dx);
			} 
			for (int yCnt = 0; yCnt < yVakken.size(); yCnt++)
			{	TabelVak yVak = (TabelVak) yVakken.elementAt(yCnt);
				yVak.translate(- dx);
			} 
			firstIndexVisible = 0;
		}
		for (int index = 0; index < xVakken.size(); index++)
		{	zetText(index, "", "");
		}
	}

	// voor expressies en tabelpunten
	public void zetText(int index, String xText, String yText)
	{	TabelVak indexXVak = (TabelVak) xVakken.elementAt(index);
		TabelVak indexYVak = (TabelVak) yVakken.elementAt(index);
		int xBreedte = indexXVak.zetText(xText);
		int yBreedte = indexYVak.zetText(yText);
		int breedte = Math.max(xBreedte, yBreedte);
		int oudeBreedte = indexXVak.getSize().width;
		if (breedte != oudeBreedte)
		{	indexXVak.zetBreedte(breedte);
			indexYVak.zetBreedte(breedte);
			int delta = breedte - oudeBreedte;
			for (int vCnt = index + 1; vCnt < xVakken.size(); vCnt++)
			{	TabelVak xVak = (TabelVak) xVakken.elementAt(vCnt);
				TabelVak yVak = (TabelVak) yVakken.elementAt(vCnt);
				xVak.translate(delta);
				yVak.translate(delta);
			}
		}
	}


	// i.c.m. functieEditor	
	public Expressie getExpressie(int num)
	{	return expressies[num];
	}

	// overloaded
	public int getExpressie(String name)
	{	int result = -1;
		for (int eCnt = 0; eCnt < maxAantalExpressies; eCnt++)
		{	if ((expressieNamen[eCnt] != null) &&
			     expressieNamen[eCnt].equals(name))
				result = eCnt;     
		}
		return result;
	}
	
	
	public int getEersteExpressie()
	{	int result = -1;
		boolean found = false;
		for (int eCnt = 0; eCnt < maxAantalExpressies; eCnt++)
		{	if ((expressies[eCnt] != null) && !found)
			{	result = eCnt;
				found = true;
			}
		}
		return result;
	}
	
	public void vindAantalExpressies()
	{	aantalExpressies = 0;
		for (int eCnt = 0; eCnt < maxAantalExpressies; eCnt++)
		{	if (expressies[eCnt] != null)
				aantalExpressies++;
		}
	}

	// aangeroepen uit functie-editor
	public void zetExpressieNaam(int num, String expNaam)
	{	expressieNamen[num] = expNaam;	
	}

	// aangeroepen uit functie-editor
	public void zetExpressie(int nr, Expressie e, String expNaam, boolean update)
	{	
		expressies[nr] = e;
		expressieNamen[nr] = expNaam;
		
		if (update)
		{	updateExpressieList();
		}
		
	}
	

	// overloaded	
	public void zetExpressie(Expressie e)
	{	// dit zou niet moeten gebeuren
		if (e == null)
		{	return;
		}
		// als oude expressie niet null, vervangen
		if (exp != null)
		{	reset();
		}
		// exp is nu niet null maar een nieuwe expressie
		exp = e;
//System.out.println("exp set");							
		for (int vCnt = 0; vCnt < xVakken.size(); vCnt++)
		{	double xWaarde = beginX + vCnt * schaalFactorX;
			zetExpressieWaarde(vCnt, xWaarde);
		}	
		repaint();
	}
	

	// zet xWaarde en de waarde van exp in xWaarde in de tabel
	public void zetExpressieWaarde(int index, double xWaarde)
	{	String xText = df.format(xWaarde);
		double yWaarde = exp.geefWaarde(xWaarde);
		String yText = "";
		if (Double.isNaN(yWaarde) || Double.isInfinite(yWaarde))
			yText = "-";
		else
			yText = df.format(yWaarde);	
		zetText(index, xText, yText);	
	
	}
	
	public void updateExpressieList()
	{	// kijk hoeveel expressies er zijn
		vindAantalExpressies();
		// geen expressies of alle verwijderd
		if (aantalExpressies == 0)
		{	// expressieKeuze onzichtbaar
			expressieKeuze.setVisible(false);
			yNaamLabel.setVisible(true);
			zetYNaam(yNaam);		 
			exp = null;
			expNum = -1;
			reset();
		} 
		else if (aantalExpressies == 1)
		{	// expressieKeuze onzichtbaar
			expressieKeuze.setVisible(false);
			int eIndex = getEersteExpressie();
			yNaamLabel.setVisible(true);
			zetYNaam(expressieNamen[eIndex]);
			// geen expressie actief
			//if (expNum == -1)
			//{	
				expNum = eIndex;
				zetExpressie(expressies[expNum]);
			//}
		}
		else 
		{	// vermijdt actionEvents op expressieKeuze
			updatingList = true;
		
			// verwijder oude namen
			expressieKeuze.removeAllItems();
		 	// nieuwe namen
		 	for (int nCnt = 0; nCnt < maxAantalExpressies; nCnt++)
		 	{	if (expressieNamen[nCnt] != null)
		 			expressieKeuze.addItem(expressieNamen[nCnt]);
		 	}
		 	// nog geen expressie gezet of oude is verwijderd
			if ((expNum == -1) || (expressies[expNum] == null))
			{	int eIndex = getEersteExpressie();
				expNum = eIndex;
				zetExpressie(expressies[expNum]);
				expressieKeuze.setSelectedItem(expressieNamen[eIndex]);
			}
			else // oude expressie blijft geselecteerd
			{	expressieKeuze.setSelectedItem(expressieNamen[expNum]);
			}
			showExpressieKeuze(true);
			// undo vermijdt
			updatingList = false;
		}
	}
	
	
	// vakjes in de tabel invullen: pas breedte aan aan inhoud
	// aan te roepen door KeyListener van x of y vak op index
	// in slechts een van de twee ben je aan het invullen
	public void adaptToText(int vakIndex)
	{	TabelVak indexXVak = (TabelVak) xVakken.elementAt(vakIndex);
		TabelVak indexYVak = (TabelVak) yVakken.elementAt(vakIndex);
		String xText = indexXVak.geefText();
		String yText = indexYVak.geefText();
		// bepaal gewenste breedte
		int xBreedte = indexXVak.geefBreedte(xText);
		int yBreedte = indexYVak.geefBreedte(yText);
		int breedte = Math.max(xBreedte, yBreedte);
		int oudeBreedte = indexXVak.getSize().width;
		if (breedte != oudeBreedte)
		{	indexXVak.zetBreedte(breedte);
			indexYVak.zetBreedte(breedte);
			int delta = breedte - oudeBreedte;
			for (int vCnt = vakIndex + 1; vCnt < xVakken.size(); vCnt++)
			{	TabelVak xVak = (TabelVak) xVakken.elementAt(vCnt);
				TabelVak yVak = (TabelVak) yVakken.elementAt(vCnt);
				xVak.translate(delta);
				yVak.translate(delta);
			}
		}
	}

	// alle punten	
	public Vector geefTabelpunten()
	{	return tabelPunten;
	}

	// alle vakIndices
	public Vector geefTabelPuntVakIndex()
	{	return tabelPuntVakIndex;
	}
	
	// alle x-strings	
	public Vector geefTabelStringsX()
	{	return tabelStringsX;
	}

	// alle y-strings	
	public Vector geefTabelStringsY()
	{	return tabelStringsY;
	}

	public void processRandomTabelPunt(int vakIndex, String xText, String yText)
	{	boolean xIsRandom = (xText.length() > 2) && (xText.charAt(0) == '#') && (xText.charAt(xText.length() - 1) == '#');
		boolean yIsRandom = (yText.length() > 2) && (yText.charAt(0) == '#') && (yText.charAt(yText.length() - 1) == '#');

//System.out.println("PRTB");		
		
		double xVal = 0;
		String xString = "";
		boolean xError = false;
		if (xIsRandom)
		{	xVal = Double.NaN;
			xString = xText;
		}
		else
		{	try
			{	if (xEditable)
					xVal = Double.parseDouble(xText);
			}
			catch (NumberFormatException nfe)
			{	xError = true;
			}
			
		}
		
		double yVal = 0;
		String yString = "";
		boolean yError = false;
		if (yIsRandom)
		{	yVal = Double.NaN;
			yString = yText;
		}
		else
		{	try
			{	if (yEditable)
					yVal = Double.parseDouble(yText);
			}
			catch (NumberFormatException nfe)
			{	yError = true;
			}
			
		}
		
		if (xError || yError)
		{	removeTabelPunt(vakIndex);
		}
		else // kijk of er al een punt met vakIndex is
		{	
//System.out.println("x = " + xVal);
//System.out.println("y = " + yVal);		
			RealPoint rp = new RealPoint(xVal, yVal);
			//rp.index = activeIndex;
			setTabelIndex(rp, activeIndex);
			//rp.vakIndex = vakIndex;
//System.out.println("prrtbp rptindex = " + rp.index);
//System.out.println("prrtbp vakindex = " + vakIndex);
			int tpIndex = getTabelPunt(vakIndex);
			if (tpIndex >= 0)
			{	removeTabelPunt(vakIndex);		
//System.out.println("prrtbp rtp = " + tabelPunten.size());			
			}
			int pIndex = addInsert(rp, vakIndex);
			if (pIndex == -1)
			{
				//rp.index = tabelPunten.size() - 1;
				setPuntIndex(rp, tabelPunten.size() - 1);
				tabelStringsX.addElement(xString);
				tabelStringsY.addElement(yString);
			}
			else
			{	
				//rp.index = pIndex;
				setPuntIndex(rp, pIndex);
				tabelStringsX.insertElementAt(xString, pIndex);
				tabelStringsY.insertElementAt(yString, pIndex);
				
			}
//System.out.println("prrtbp irp = " + getIndexOf(rp));
//System.out.println("prrtbp indrp = " + rp.index);
		}// else
//System.out.println("prrtbp ai = " + tabelPunten.size());
		// update grafiekComponent
		if (grafiekComponent != null)
		{	grafiekComponent.repaint();
		}
		produceAction("points changed");
			
	}
	// Enter of focusLost op een vakje	
	public void processTabelPunt(int vakIndex)
	{	TabelVak indexXVak = (TabelVak) xVakken.elementAt(vakIndex);
		TabelVak indexYVak = (TabelVak) yVakken.elementAt(vakIndex);
		String xText = indexXVak.geefText();
		String yText = indexYVak.geefText();
		// het nieuwe punt is geen volledig punt
		// er is b.v. een vakje uitgeveegd
		if ((xText.equals("") && xEditable) || (yText.equals("") && yEditable))
		{	removeTabelPunt(vakIndex);
//System.out.println("geen volledig punt");		
			produceAction("points changed");
			return;
		}
		
// HIER
		//boolean xIsRandom = (xText.length() > 2) && (xText.charAt(0) == '#') && (xText.charAt(xText.length() - 1) == '#');
		//boolean yIsRandom = (yText.length() > 2) && (yText.charAt(0) == '#') && (yText.charAt(yText.length() - 1) == '#');
		
		if (randomAllowed)
		{	processRandomTabelPunt(vakIndex, xText, yText);
			return;
		}
		
		double xVal = 0;
		double yVal = 0;
		boolean error = false;
		// dit zou niet nodig moeten zijn
		try
		{	if (xEditable)
				xVal = Double.parseDouble(xText);
			if (yEditable)
				yVal = Double.parseDouble(yText);
		}
		catch (NumberFormatException nfe)
		{	error = true;
//System.out.println("nfe");		
		}
		if (error)
		{	removeTabelPunt(vakIndex);
		}
		else // kijk of er al een punt met vakIndex is
		{	
//System.out.println("x = " + xVal);
//System.out.println("y = " + yVal);		
			RealPoint rp = new RealPoint(xVal, yVal);
			rp.index = activeIndex;
			//rp.vakIndex = vakIndex;
			
			int tpIndex = getTabelPunt(vakIndex);
			if (tpIndex >= 0)
			{	removeTabelPunt(vakIndex);
//System.out.println("prtbp rtp = " + tabelPunten.size());			
			}
			addInsert(rp, vakIndex);
		}
//System.out.println("prtbp ai = " + tabelPunten.size());		
		// update grafiekComponent
		if (grafiekComponent != null)
		{	grafiekComponent.repaint();
		}
		produceAction("points changed");
	}
	
	
	public int addInsert(RealPoint newRP, int vakIndex)
	{	int pIndex = -1;
	
		if (!randomAllowed)
		{	
			boolean firstFound = false;
			for (int pCnt = 0; pCnt < tabelPunten.size(); pCnt++)
			{	RealPoint rp = (RealPoint) tabelPunten.elementAt(pCnt);
				if (!firstFound && rp.hasLargerXThen(newRP))
				{	pIndex = pCnt;
					firstFound = true;
				}
			}
		}
		if (pIndex == -1)
		{
			tabelPunten.addElement(newRP);
			tabelPuntVakIndex.addElement(new Integer(vakIndex));
			
		}
		else
		{
			tabelPunten.insertElementAt(newRP, pIndex);	
			tabelPuntVakIndex.insertElementAt(new Integer(vakIndex), pIndex);
		}
		return pIndex;
	}	
	
	// verwijder punt met gegeven vakIndex van de actieve tabel
	public void removeTabelPunt(int vakIndex)
	{	// er is er hoogstens 1
		int tpIndex = -1;
		for (int tCnt = 0; tCnt < tabelPunten.size(); tCnt++)
		{	RealPoint rp = (RealPoint) tabelPunten.elementAt(tCnt);
			if ((getTabelIndex(rp) == activeIndex) && ((Integer)(tabelPuntVakIndex.elementAt(tCnt))).intValue() == vakIndex)
				tpIndex = tCnt;
		}
		if (tpIndex >= 0)
		{	
			if (randomAllowed)
			{	for (int pCnt = tpIndex + 1; pCnt < tabelPunten.size(); pCnt++)
				{	RealPoint rp = (RealPoint) tabelPunten.elementAt(pCnt);
					setPuntIndex(rp, pCnt - 1);
				}
			}	
			
			tabelPunten.removeElementAt(tpIndex);
			tabelPuntVakIndex.removeElementAt(tpIndex);

			if (randomAllowed)
			{	tabelStringsX.removeElementAt(tpIndex);
				tabelStringsY.removeElementAt(tpIndex);
			}
		}
	}
	
	// vindt punt met gegeven vakIndex van de actieve tabel (if any)
	public int getTabelPunt(int vakIndex)
	{	// er is er hoogstens 1
		int tpIndex = -1;
		for (int tCnt = 0; tCnt < tabelPunten.size(); tCnt++)
		{	RealPoint rp = (RealPoint) tabelPunten.elementAt(tCnt);
			//if ((rp.index == activeIndex) && (rp.vakIndex == vakIndex))
			//if ((rp.index == activeIndex) && ((Integer)(tabelPuntVakIndex.elementAt(tCnt))).intValue() == vakIndex)
			if ((getTabelIndex(rp) == activeIndex) && ((Integer)(tabelPuntVakIndex.elementAt(tCnt))).intValue() == vakIndex)
				tpIndex = tCnt;
		}
		return tpIndex;
	}

	// vindt alle punten met index aIndex
	public Vector getPoints(int aIndex)
	{	Vector points = new Vector();
		for (int pCnt = 0; pCnt < tabelPunten.size(); pCnt++)
		{	RealPoint rp = (RealPoint) tabelPunten.elementAt(pCnt);
			//if (rp.index == aIndex)
			if (getTabelIndex(rp) == aIndex)
				points.addElement(rp);
		}
		return points;
	}

	// verwijder alle punten met tabelIndex aIndex
	public void removePoints(int aIndex)
	{	Vector rPoints = getPoints(aIndex);
		for (int rCnt = 0; rCnt < rPoints.size(); rCnt++)
		{	RealPoint rp = (RealPoint) rPoints.elementAt(rCnt);
			int index = getIndexOf(rp);
			
			if (randomAllowed)
			{	for (int pCnt = index + 1; pCnt < tabelPunten.size(); pCnt++)
				{	RealPoint arp = (RealPoint) tabelPunten.elementAt(pCnt);
					setPuntIndex(arp, pCnt - 1);
				}
			}			
			
			tabelPunten.removeElementAt(index);
			tabelPuntVakIndex.removeElementAt(index);
			
			if (randomAllowed)
			{	tabelStringsX.removeElementAt(index);
				tabelStringsY.removeElementAt(index);
			}
		}
		produceAction("points changed");
	}

	// verwijder alle punten
	public void removeAllPoints()
	{
		tabelPunten.removeAllElements();
		tabelPuntVakIndex.removeAllElements();
		
		if (randomAllowed)
		{	tabelStringsX.removeAllElements();
			tabelStringsY.removeAllElements();
		}
		
	}
	public int getIndexOf(RealPoint rp)
	{	int index = -1;
		//if (!Double.isNaN(rp.x) && !Double.isNaN(rp.y))
		//if (!randomAllowed && !puntenNaN)
		if (!randomAllowed)
			index = tabelPunten.indexOf(rp);
		else
		{	//index = rp.index;
			index = getPuntIndex(rp);
		}
		
		return index;	
	}
	
	public int getTabelIndex(RealPoint rp)
	{	return rp.index % 100;
	}
	public int getPuntIndex(RealPoint rp)
	{ 	return rp.index / 100;
	}
	public void setTabelIndex(RealPoint rp, int tIndex)
	{	int pIndex = getPuntIndex(rp);
		rp.index = pIndex * 100 + tIndex;
	}
	public void setPuntIndex(RealPoint rp, int pIndex)
	{	int tIndex = getTabelIndex(rp);
		rp.index = pIndex * 100 + tIndex;
	}
	
	// vul de tabel met alle punten met index aIndex
	public void zetTabelPunten(int aIndex)
	{	// maak de tabel leeg
		reset();
		Vector aPoints = new Vector();
		if (aIndex == -10)
			aPoints = tabelPunten;
		else
			aPoints = getPoints(aIndex);
		
//System.out.println("zetTb " + aPoints.size());

		// vindt maximale vakIndex
		int maxIndex = 0;
		for (int tCnt = 0; tCnt < aPoints.size(); tCnt++)
		{	RealPoint rp = (RealPoint) aPoints.elementAt(tCnt);
//System.out.println("tpvi = "+ tabelPuntVakIndex.size());	
//System.out.println("tpiof = " + getIndexOf(rp));//indexOf(rp));
			//int vakIndex = ((Integer) tabelPuntVakIndex.elementAt(tabelPunten.indexOf(rp))).intValue();
			int vakIndex = ((Integer) tabelPuntVakIndex.elementAt(getIndexOf(rp))).intValue();
			//maxIndex = Math.max(rp.vakIndex, maxIndex);
			maxIndex = Math.max(vakIndex, maxIndex);
		}
		// kijk of er voldoende vakjes zijn
		if (maxIndex > xVakken.size())
		{	int nieuweVakken = maxIndex - xVakken.size();
			for (int vCnt = 0; vCnt < nieuweVakken; vCnt++)
			{
				TabelVak lastVak = (TabelVak) xVakken.lastElement();
			
				TabelVak xVak = 
						new TabelVak(this, xVakken.size() + 1, 
							lastVak.getLocation().x + lastVak.getSize().width, 
							0, vakBreedte, vakHoogte, xEditable);
				// constructie			
				//xVak.zetText("n 0");
				xVak.zetFont(f);
				xVak.randomAllowed = randomAllowed;
				xVakkenPanel.add(xVak);
				xVakken.addElement(xVak);
				
				TabelVak yVak = 
					new TabelVak(this, xVakken.size() + 1, 
						lastVak.getLocation().x + lastVak.getSize().width, 
						0, vakBreedte, vakHoogte, yEditable);
				// constructie		
				//yVak.zetText("n 0");
				yVak.zetFont(f);
				yVak.randomAllowed = randomAllowed;
				yVakkenPanel.add(yVak);
				yVakken.addElement(yVak);
			}	
	
		}
		// zet de punten
		for (int tCnt = 0; tCnt < aPoints.size(); tCnt++)
		{	RealPoint rp = (RealPoint) aPoints.elementAt(tCnt);	
			//int index = tabelPunten.indexOf(rp);
			int index = getIndexOf(rp);
//System.out.println("index = " + index);
//System.out.println("rp.index = " + rp.index);
//System.out.println("rp.x = " + rp.x);			
			String xText = "";
			if (!Double.isNaN(rp.x))
				xText = df.format(rp.x);
			else if (Double.isNaN(rp.x) && randomAllowed)
			{	xText = (String) tabelStringsX.elementAt(index);
			}
			String yText = "";
			if (!Double.isNaN(rp.y) && yVisible)
				yText = df.format(rp.y);
			else if (Double.isNaN(rp.y) && randomAllowed) 
			{	yText = (String) tabelStringsY.elementAt(index);
			}
			//int vakIndex = ((Integer)tabelPuntVakIndex.elementAt(tabelPunten.indexOf(rp))).intValue();
			int vakIndex = ((Integer)tabelPuntVakIndex.elementAt(getIndexOf(rp))).intValue();
			
//System.out.println("tpvi = "+ tabelPuntVakIndex.size());	
//System.out.println("tpiof = " + getIndexOf(rp));//indexOf(rp));
			
//System.out.println("x,y = " + xText + "," + yText);
//System.out.println("yText = " + yText);
//System.out.println("vakIndex = " + vakIndex);
			zetText(vakIndex, xText, yText);
		}
		
		repaint();
		// update grafiekComponent if any
		if (grafiekComponent != null)
		{	
//System.out.println("gc != null");			
			grafiekComponent.repaint();
		}
	}

	public void zetTabelStringsX(Vector docentTabelStringsX)
	{	tabelStringsX = docentTabelStringsX;
	}
	public void zetTabelStringsY(Vector docentTabelStringsY)
	{	tabelStringsY = docentTabelStringsY;
	}
	
	public void zetDocentTabelPunten(Vector docentTabelPunten, boolean rAllowed)
	{	Vector docentTabelPuntenCopy = new Vector();
		for (int pCnt = 0; pCnt < docentTabelPunten.size(); pCnt++)
		{	RealPoint rp = (RealPoint) docentTabelPunten.elementAt(pCnt);
			docentTabelPuntenCopy.addElement(new RealPoint(rp));
		}
	
		tabelPunten = docentTabelPuntenCopy;
		//puntenNaN = ptsNaN;
		//randomAllowed = rAllowed;
		setRandomAllowed(rAllowed);
		if (randomAllowed)
		//if (ptsNaN)
			zetTabelPunten(-10);
		else
			zetTabelPunten(1);
	}
	
	public void zetDocentTabelPuntVakIndex(Vector docentTabelPuntVakIndex)
	{	tabelPuntVakIndex = docentTabelPuntVakIndex;
	}
	
	public int geefBreedte()
	{	
		int b = vakBreedte;
		return b;	
	}
	
	public int geefHoogte()
	{	int h = 2 * vakHoogte + 3 * offSet;
		return h;
	}

	public void pijlLinksAction()
	{	// deze verdwijnt naar links
		TabelVak lxVak = (TabelVak) xVakken.elementAt(firstIndexVisible);
		firstIndexVisible++;
		// schuif alles naar links
		for (int xCnt = 0; xCnt < xVakken.size(); xCnt++)
		{	TabelVak tabelVak = (TabelVak) xVakken.elementAt(xCnt);
			tabelVak.translate(- lxVak.getSize().width);
		}
		for (int yCnt = 0; yCnt < yVakken.size(); yCnt++)
		{	TabelVak tabelVak = (TabelVak) yVakken.elementAt(yCnt);
			tabelVak.translate(- lxVak.getSize().width);
		}
		// maak voor het gemak altijd maar een nieuw vakje
		TabelVak lastVak = (TabelVak) xVakken.lastElement();
			
		TabelVak xVak = 
				new TabelVak(this, xVakken.size() + 1, 
					lastVak.getLocation().x + lastVak.getSize().width, 
					0, vakBreedte, vakHoogte, xEditable);
		// constructie			
		//xVak.zetText("n 0");
		xVak.zetFont(f);
		xVak.randomAllowed = randomAllowed;
		xVakkenPanel.add(xVak);
		xVakken.addElement(xVak);
				
		TabelVak yVak = 
			new TabelVak(this, xVakken.size() + 1, 
				lastVak.getLocation().x + lastVak.getSize().width, 
				0, vakBreedte, vakHoogte, yEditable);
		// constructie		
		//yVak.zetText("n 0");
		yVak.zetFont(f);
		yVak.randomAllowed = randomAllowed;
		yVakkenPanel.add(yVak);
		yVakken.addElement(yVak);
		
		if (exp != null)
		{	double lastXWaarde = lastVak.geefWaarde();
			if (!Double.isNaN(lastXWaarde))	
			{	zetExpressieWaarde(xVakken.size() - 1, 
					lastXWaarde + schaalFactorX);
			}
		}
		
		repaint();
	}

	public void pijlRechtsAction(String s)
	{
System.out.println(s);
		pijlRechtsAction(false);
	}
	
	public void pijlRechtsAction(boolean updateVakIndex)
	{	if (firstIndexVisible == 0)
		{	// maak nieuwe tabelVakken voor index 0
			TabelVak xVak = 
				new TabelVak(this, 0, 0, 0, vakBreedte, vakHoogte, xEditable);
			// constructie	
			//xVak.zetText("n 0");
			xVak.zetFont(f);
			xVak.randomAllowed = randomAllowed;
			xVakkenPanel.add(xVak);
			for (int xCnt = 0; xCnt < xVakken.size(); xCnt++)
			{	TabelVak tabelVak = (TabelVak) xVakken.elementAt(xCnt);
				tabelVak.incIndex();
				tabelVak.translate(vakBreedte);
			}
			xVakken.insertElementAt(xVak, 0);
				
			TabelVak yVak = 
				new TabelVak(this, 0, 0, 0, vakBreedte, vakHoogte, yEditable);
			// constructie	
			//yVak.zetText("n 0");
			yVak.zetFont(f);
			yVak.randomAllowed = randomAllowed;
			yVakkenPanel.add(yVak);
			for (int yCnt = 0; yCnt < yVakken.size(); yCnt++)
			{	TabelVak tabelVak = (TabelVak) yVakken.elementAt(yCnt);
				tabelVak.incIndex();
				tabelVak.translate(vakBreedte);
			}
			yVakken.insertElementAt(yVak, 0);
				
			// firstIndexVisible blijft 0

			for (int viCnt = 0; viCnt < tabelPuntVakIndex.size(); viCnt++)
			{	int tpvi = ((Integer) tabelPuntVakIndex.elementAt(viCnt)).intValue();
				if (updateVakIndex)
				{	tpvi += 1;
					tabelPuntVakIndex.setElementAt(new Integer(tpvi), viCnt);
				}
//System.out.println("tpvi+1= " + tpvi);				
			}
			
			
			if (exp != null)
			{	beginX -= schaalFactorX;
				zetExpressieWaarde(0, beginX);	
			}	
				
				
		}
		else // if (firstIndexVisible > 0)
		{	firstIndexVisible--;
			// dit vak wordt zichtbaar
			TabelVak xVak = (TabelVak) xVakken.elementAt(firstIndexVisible);
			// schuif alles naar rechts
			for (int xCnt = 0; xCnt < xVakken.size(); xCnt++)
			{	TabelVak tabelVak = (TabelVak) xVakken.elementAt(xCnt);
				tabelVak.translate(xVak.getSize().width);
			}
			for (int yCnt = 0; yCnt < yVakken.size(); yCnt++)
			{	TabelVak tabelVak = (TabelVak) yVakken.elementAt(yCnt);
				tabelVak.translate(xVak.getSize().width);
			}
				
		}

		repaint();

	}
	
	public void actionPerformed(ActionEvent e)
	{	
	
		if (e.getSource() == pijlLinksButton)
		{	if (!frozen)
			{
				pijlLinksAction();			
				produceAction("pijl rechts");
			}	
		}
		else if (e.getSource() == pijlRechtsButton)
		{	if (!frozen)
			{
				pijlRechtsAction(true);
				produceAction("pijl links");
			}	
		
		}
		else if (e.getSource() == resetButton)
		{	if (!frozen)
			{
				reset();
				removePoints(activeIndex);
				if (grafiekComponent != null)
				{	grafiekComponent.repaint();
				}
			}
		}
		else if (e.getSource() == zoomInButton)
		{	double factorX = 1;
			if (factorRijNummerX % 3 == 2)
			{	factorX = 0.4;
			}
			else if (factorRijNummerX % 3 == 0)
			{	factorX = 0.5;
			}
			else 
			{	factorX = 0.5;
			}
			schaalFactorX *= factorX;
			factorRijNummerX--;
			beginX *= factorX;
			int oldFirstIndexVisible = firstIndexVisible;
			// dit bevat een reset()
			zetExpressie(exp);
			zetFirstIndexVisible(oldFirstIndexVisible);
			
		}
		else if (e.getSource() == zoomUitButton)
		{	double factorX = 1;
			if (factorRijNummerX % 3 == 1)
			{	factorX = 2.5;
			}
			else if (factorRijNummerX % 3 == 2)
			{	factorX = 2;
			}
			else 
			{	factorX = 2;
			}
			schaalFactorX *= factorX;
			factorRijNummerX++;
			beginX *= factorX;
			int oldFirstIndexVisible = firstIndexVisible;
			// dit bevat een reset()
			zetExpressie(exp);
			zetFirstIndexVisible(oldFirstIndexVisible);
			
		}
		
	}	
	
	public void mousePressed(MouseEvent e){}
	public void mouseDragged(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
	public void mouseMoved(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mouseClicked(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}	

	class NumTabelAL implements ActionListener
	{	public void actionPerformed(ActionEvent e)
		{	if (!tabelKeuzeEnabled)
				return;
			int index = tabelKeuze.getSelectedIndex();
			tabelKeuze.setForeground(colors[index]);
			setActiveIndex(index + 1);
		}
	}

	class ExpKeuzeAL implements ActionListener
	{	public void actionPerformed(ActionEvent e)
		{	if (!updatingList)
			{
				String name = (String) expressieKeuze.getSelectedItem();
				expNum = getExpressie(name);
				zetExpressie(expressies[expNum]);
			}	
		}
	}
	
	class TabelKeuzeRenderer extends JLabel implements ListCellRenderer 
	{   public TabelKeuzeRenderer()
     	{  	setOpaque(true);
     	}
     	public Component getListCellRendererComponent(
         					JList list,
         					Object value,
         					int index,
         					boolean isSelected,
         					boolean cellHasFocus)
     	{
         	setText(value.toString());
         	if (isSelected)
         		setBackground(Color.white);
         	else	
         		setBackground(new Color(210, 210, 210));
         	if ((index >= 0) && (index < colors.length))	
         		setForeground(colors[index]);
         	return this;
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
	
}


class TabelVak extends JPanel
{
	// attributen
	TabelComponent owner;
	int vakIndex;

	JLabel tabelVakLabel;
	JTextField tabelVakTextField;
	
	String text = ""; // !!!
	
	boolean editable; 
	Color borderColor = new Color(200, 200, 200);
	
	FontMetrics fm;
	
	boolean randomAllowed = false;
	
	public TabelVak(TabelComponent o, int vIndex, 
				    int x, int y, int w, int h, boolean edit)
	{	
		owner = o;
		vakIndex = vIndex;
		
		setLayout(null);
		setBounds(x, y, w, h);
		editable = edit;
		tabelVakLabel = new JLabel("", SwingConstants.CENTER);
		tabelVakLabel.setBackground(new Color(245,245,245));
		tabelVakLabel.setBorder(BorderFactory.createLineBorder(borderColor));		
		tabelVakLabel.setOpaque(true);
		tabelVakLabel.setBounds(0, 0, w, h);
		add(tabelVakLabel);	
		tabelVakLabel.addMouseListener(new TextML());
		
		tabelVakTextField = new JTextField();
		tabelVakTextField.setBounds(1, 1, w, h - 1);
		tabelVakTextField.addFocusListener(new TextFL());
		tabelVakTextField.addActionListener(new TextAL());
		tabelVakTextField.addKeyListener(new InputKL());
		tabelVakTextField.setVisible(false);
		add(tabelVakTextField);
		
		
	}

	public void zetBreedte(int breedte)
	{	int x = getLocation().x;
		int y = getLocation().y;
		int h = getSize().height;
		setBounds(x, y, breedte, h);
		tabelVakLabel.setBounds(0, 0, breedte, h);
		tabelVakTextField.setBounds(1, 1, breedte, h - 1);
	}
	
	public void incIndex()
	{	vakIndex++;
	}
	
	public void translate(int dx)
	{	setLocation(getLocation().x + dx, getLocation().y);
	}
	
	public void zetEditable(boolean b)
	{	editable = b;
		// genoeg??
	}

	// aanpassen invulveld
	public int geefBreedte(String s)
	{	// bepaal de gewenste breedte
		int width = fm.stringWidth(" " + s + " ");
		int vakBreedte = owner.getVakBreedte();
		if (width <= vakBreedte)
			return vakBreedte;
		else if ((width > vakBreedte) && (width <= 2 * vakBreedte))
			return width;
		else
			return 2 * vakBreedte;		
	}

	// extern text zetten	
	public int zetText(String s)
	{	text = s;
		tabelVakLabel.setText(text);
		tabelVakTextField.setText(text);
		
		//tabelVakLabel.repaint();
		//tabelVakTextField.repaint();
		
		// bepaal de gewenste breedte
		int width = fm.stringWidth(" " + s + " ");
		int vakBreedte = owner.getVakBreedte();
		if (width <= vakBreedte)
			return vakBreedte;
		else if ((width > vakBreedte) && (width <= 2 * vakBreedte))
			return width;
		else
			return 2 * vakBreedte;		
	}
	
	public void zetFont(Font f)
	{	tabelVakLabel.setFont(f);
		tabelVakTextField.setFont(f);
		fm = getFontMetrics(f);
	}

	public double geefWaarde()
	{	double result = Double.NaN;
		try
		{	result = Double.parseDouble(text);
		}
		catch (NumberFormatException nfe) {}
		return result;
	}

	public String geefText()
	{	if (tabelVakTextField.isVisible())
			return tabelVakTextField.getText();
		else
			return text;	
	}
	
	class TextFL implements FocusListener
	{	public void focusGained(FocusEvent e)
		{
		}
		public void focusLost(FocusEvent e)
		{	
			
			text = tabelVakTextField.getText();

			String text1 = trimTrailingZeros(text);
			boolean changed1 = (text.length() != text1.length());
			String text2 = addLeadingZero(text1);
			boolean changed2 = (text1.length() != text2.length());
			if (changed1 || changed2)
			{	text = text2;
				tabelVakTextField.setText(text);
				owner.adaptToText(vakIndex);
			}

			tabelVakTextField.setVisible(false);
			tabelVakLabel.setText(text);
			tabelVakLabel.setVisible(true);
			// als valide tweetal aan punten toevoegen
			// of verwijderen			
			owner.processTabelPunt(vakIndex);
			
		}
	}
	
	class TextML extends MouseAdapter
	{	public void mousePressed(MouseEvent e)
		{	if (editable)
			{	tabelVakLabel.setVisible(false);
				tabelVakTextField.setVisible(true);
				tabelVakTextField.requestFocus();
			}
		}
	} 
	
	class TextAL implements ActionListener
	{	public void actionPerformed(ActionEvent e)
		{	
/*
			text = tabelVakTextField.getText();
			
			String text1 = trimTrailingZeros(text);
			boolean changed1 = (text.length() != text1.length());
			String text2 = addLeadingZero(text1);
			boolean changed2 = (text1.length() != text2.length());
			if (changed1 || changed2)
			{	text = text2;
				tabelVakTextField.setText(text);
				owner.adaptToText(vakIndex);
			}
*/			
			tabelVakTextField.setVisible(false);
			tabelVakLabel.setText(text);
			tabelVakLabel.setVisible(true);
			// als valide tweetal aan punten toevoegen
			// of verwijderen			
//			owner.processTabelPunt(vakIndex);
			
		}
	}

	public String trimTrailingZeros(String s)
	{	String txt = new String(s);
		if (txt.indexOf('.') < 0)
			return txt;
		char c = txt.charAt(txt.length() - 1);
		while (c == '0')
		{	txt = removeCharAt(txt, txt.length() - 1);
			c = txt.charAt(txt.length() - 1);
		}	
		c = txt.charAt(txt.length() - 1);
		if (c == '.')
			txt = removeCharAt(txt, txt.length() - 1);
		return txt;		
	}				
		
	public String addLeadingZero(String s)
	{	String txt = new String(s);
		// met minteken
		if ((txt.length() >= 2) && (txt.charAt(0) == '-') &&
			(txt.charAt(1) == '.'))
		{	txt = "-0" + txt.substring(1);
		}	
		// zonder minteken
		if ((txt.length() >= 1) && (txt.charAt(0) == '.'))
		{	txt = "0" + txt;
		}
		return txt;
	}

	public String removeCharAt(String s, int index)
	{	String txt = new String(s);
		// eerste
		if (index == 0)
			txt = txt.substring(1);
		// laatste	
		else if (index == (txt.length() - 1))
			txt = txt.substring(0, txt.length() - 1);
		// middenin	
		else
		{	String txt1 = txt.substring(0, index);
			String txt2 = txt.substring(index + 1);
			txt = txt1 + txt2;
		}
		return txt;
	}		
	
	class InputKL extends KeyAdapter
	{	public void keyReleased(KeyEvent e)
		{	
			String txt = tabelVakTextField.getText();
			
			//om randomvariabele in te kunnen vullen
			if (randomAllowed && isLegal(txt))
			{	
//System.out.println("ra && legal");
				owner.adaptToText(vakIndex);
				return;
			}
			
			boolean corrected = false;
			// kijk of txt illegale characters bevat
			// dit zou er maximaal 1 moeten zijn
			int index = -1;
			for (int cCnt = 0; cCnt < txt.length(); cCnt++)
			{	char c = txt.charAt(cCnt);
				if (!isLegal(c))
					index = cCnt;
			}
			// verwijder illegaal karakter
			if (index >= 0)
			{	txt = removeCharAt(txt, index);
				corrected = true;
			}
			// dubbele decimale punt
			// voldoende er twee te zoeken
			int pIndex1 = txt.indexOf('.');
			int pIndex2 = txt.lastIndexOf('.');
			if ((pIndex1 >= 0) && (pIndex2 >= 0) && (pIndex1 != pIndex2))
			{	// verwijderen
				txt = removeCharAt(txt, pIndex2);
				corrected = true;
			}
			// proberen een legaal karakter voor het
			// minteken (dit staat dan op plek 1) in te vullen
			if (txt.indexOf('-') == 1)
			{	txt = removeCharAt(txt, 0);
				corrected = true;
			}
			// minteken
			// alleen vooraan if any
			int minIndex = txt.lastIndexOf('-');
			if (minIndex > 0)
			{	txt = removeCharAt(txt, minIndex);
				corrected = true;
			}
			// leading zeros, leiden niet tot een NumberFormatException
			// geval met minteken
			if ((txt.indexOf('-') == 0) && (txt.length() >= 3) &&
				(txt.charAt(1) == '0') && Character.isDigit(txt.charAt(2)))
			{	txt = removeCharAt(txt, 1);
				corrected = true;
			}
			// geen minteken
			if ((txt.indexOf('-') < 0) && (txt.length() >= 2) &&
				(txt.charAt(0) == '0') && Character.isDigit(txt.charAt(1)))
			{	txt = removeCharAt(txt, 0);
				corrected = true;
			}
			
// trailing zeros na(!) decimale punt oplossen 
// bij actionPerformed of focusLost			

			
			if (corrected)
				tabelVakTextField.setText(txt);
			
			// pas de textbreedte aan
			owner.adaptToText(vakIndex);
		}
	
		public boolean isLegal(String s)
		{	
			if (s != null && s.length() > 0)
				return (s.charAt(0) == '#');
			else 
				return false;
	}
	
		public boolean isLegal(char c)
		{	return Character.isDigit(c) || (c == '-') || (c == '.');
		}
	}
}