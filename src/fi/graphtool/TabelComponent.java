package fi.graphtool;

import java.awt.AWTEventMulticaster;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;

import fi.wiskopdr.expressies.Expressie;
import fi.wiskopdr.formuleobjects.FormuleParser;


public class TabelComponent extends JPanel implements ActionListener, MouseListener{
	
	// gebruik in combinatie met functieEditor			 
	private int aantalExpressies;
	private int maxAantalExpressies;
	private Expressie[] functies;
	private String[] functieNamen;
	private Expressie func;
	private int funcNum;

	// voor zoomen (alleen i.c.m. functieEditor)
	private boolean zooming;
	private double beginX;
	private double schaalFactorX;
	private int factorRijNummerX;

	private boolean isTekenTool; 
	private GraphToolInteractiePanel interactiePanel;
	private boolean eenTabel;
	
	// alleen i.c.m. functieEditor
	private GraphToolKnop zoomInButton, zoomUitButton;
	private JComboBox functieKeuze;
	boolean updatingList = false;
	
	private JComboBox tabelKeuze;
	private GraphToolKnop resetButton;
	boolean tabelKeuzeEnabled = true;
	
	private GraphToolKnop pijlLinksButton, pijlRechtsButton;

	private String xAsNaam = "x";
	private String yAsNaam = "y";
	
	Font font, italicFont;
	FontMetrics fm, itFm;
	
	private int offSet;

	private int linkerBreedteTabel;
	private int linkerBreedteTool;	
	private int linkerBreedte;
	private int rechterBreedte;	
	 
	private int labelBreedte;
	private JLabel xAsNaamLabel, yAsNaamLabel;
	
	private int vakBreedte;	 
	private int vakHoogte;
	private JPanel xVakkenPanel, yVakkenPanel;
	 
	 // het aantal vakken bij constructie
	private int aantalVakken;
	private int[] firstIndexVisible;
	
	private Vector xVakken;
	private Vector yVakken;
	 
	private boolean xVakEditable;
	private boolean yVakEditable;
	
	private boolean frozen;
	private boolean docent;
	 
	private DecimalFormatSymbols dfs;
	private DecimalFormat df;
	
	private boolean randomAllowed;
	
	public TabelComponent(int breedte, boolean docent)
	{	setLayout(null);	
		this.docent = docent;
		
		maxAantalExpressies = 50;
		functies = new Expressie[maxAantalExpressies];
		functieNamen = new String[maxAantalExpressies];
		aantalExpressies = 0;
		func = null;
		funcNum = -1;
		
		beginX = -2;
		schaalFactorX = 1;
		factorRijNummerX = 99;
	
		zooming = true;
		isTekenTool = false;
		eenTabel = false;
	
		xVakEditable = false;
		yVakEditable = false;
		frozen = false;
		randomAllowed = true;
		
		font = new Font("SansSerrif",Font.PLAIN,12);
		fm = getFontMetrics(font);
		italicFont = new Font("SansSerrif",Font.ITALIC,12);
		itFm = getFontMetrics(italicFont);
		
		dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		df = new DecimalFormat("0.###", dfs);
		
		offSet = 1;
		labelBreedte = 20;	
		vakBreedte = 33;
		vakHoogte = 20;
		linkerBreedteTabel = 25;//breedte met niets
		linkerBreedteTool = 55;//breedte met grafiekKeuze tekentool
		linkerBreedte = linkerBreedteTabel;
		rechterBreedte = 25;

		int hoogte = 2 * vakHoogte + 3 * offSet;
		setSize(breedte, hoogte);

		zoomInButton = new GraphToolKnop("zoominknop.gif", 2);
		zoomInButton.setBounds(3*offSet,2*offSet,20,20);
		add(zoomInButton);	
		zoomInButton.addActionListener(this);
		
		zoomUitButton = new GraphToolKnop("zoomuitknop.gif", 2);
		zoomUitButton.setBounds(getSize().width - 2*offSet - 20, 2*offSet,20,20);
		add(zoomUitButton);	
		zoomUitButton.addActionListener(this);

		functieKeuze = new JComboBox(){
			public void paintComponent(Graphics g)
			{	int red = getBackground().getRed();
				int green = getBackground().getGreen();
				int blue = getBackground().getBlue();
				
				for(int i = 0; i < 10; i++)
				{	g.setColor(new Color(red + (7*i+9)*(245-red)/180, green + (7*i+9)*(245-green)/180, blue + (7*i+9)*(245-blue)/180));
					g.fillRect(0,getHeight()-(i+1)*getHeight()/10, getWidth(),getHeight()/10+1);
				}
				g.setColor(getBackground().darker());
				g.drawLine(0,0,getSize().width-1,0);
				g.drawLine(0,0,0,getSize().height-1);
				g.drawLine(0,getSize().height-1,getSize().width-1,getSize().height-1);
				g.setColor(getForeground());
				g.drawString(getSelectedItem().toString(), 2, getHeight() - 5);
			}
		};
		functieKeuze.setFont(italicFont);
		functieKeuze.setBounds(linkerBreedte, 2 * offSet + vakHoogte, 49, vakHoogte);
		functieKeuze.setBackground(new Color(210, 210, 210));
		functieKeuze.setVisible(false);
		add(functieKeuze);
		functieKeuze.addActionListener(new FuncKeuzeAL());
		functieKeuze.setRenderer(new TabelKeuzeRenderer());
		
		
		pijlLinksButton = new GraphToolKnop("pijllinks.gif", 1);
		pijlLinksButton.setBounds(3*offSet,2*offSet + vakHoogte,20,20);
		add(pijlLinksButton);	
		pijlLinksButton.addActionListener(this);
		
		pijlRechtsButton = new GraphToolKnop("pijlrechts.gif", 1);
		pijlRechtsButton.setBounds(getSize().width - 2*offSet - 20,
				   2*offSet + vakHoogte,20,20);
		add(pijlRechtsButton);	
		pijlRechtsButton.addActionListener(this);
		
		tabelKeuze = new JComboBox(){
			public void paintComponent(Graphics g)
			{	int red = getBackground().getRed();
				int green = getBackground().getGreen();
				int blue = getBackground().getBlue();
				
				for(int i = 0; i < 10; i++)
				{	g.setColor(new Color((245+red)/2 + i*(245-red)/20, (245+green)/2 + i*(245-green)/20, (245+blue)/2 + i*(245-blue)/20));
					g.fillRect(0,getHeight()-(i+1)*getHeight()/10, getWidth(),getHeight()/10+1);
				}
				g.setColor(getBackground().darker());
				g.drawLine(0,0,getSize().width-1,0);
				g.drawLine(0,0,0,getSize().height-1);
				g.drawLine(0,getSize().height-1,getSize().width-1,getSize().height-1);
				g.setColor(getForeground());
				g.drawString(getSelectedItem().toString(), 2, getHeight() - 5);
			}
		};
		tabelKeuze.setBounds(3*offSet, 2*offSet, 50, 20);
		tabelKeuze.setBackground(new Color(210,210,210));
		tabelKeuze.setVisible(false);
		add(tabelKeuze);
		tabelKeuze.addItem("Gr 1");
		tabelKeuze.addItem("Gr 2");
		tabelKeuze.addItem("Gr 3");

		tabelKeuze.setRenderer(new TabelKeuzeRenderer());
		tabelKeuze.addActionListener(new NumTabelAL());

		// tekentool rechts
		resetButton = new GraphToolKnop("teken_wisknop.gif", 2);
		resetButton.setBounds(getSize().width - 2*offSet - 20,
								   2*offSet,20,20);
		resetButton.setVisible(false);
		add(resetButton);	
		resetButton.addActionListener(this);
		resetButton.addMouseListener(this);

		xAsNaamLabel = new JLabel(xAsNaam, SwingConstants.CENTER);
		xAsNaamLabel.setOpaque(false);
		xAsNaamLabel.setFont(italicFont);
		xAsNaamLabel.setBounds(linkerBreedte, offSet, labelBreedte, vakHoogte);
		add(xAsNaamLabel);	
		
		yAsNaamLabel = new JLabel(yAsNaam, SwingConstants.CENTER);
		yAsNaamLabel.setOpaque(false);		
		yAsNaamLabel.setFont(italicFont);
		yAsNaamLabel.setBounds(linkerBreedte, 2 * offSet + vakHoogte, labelBreedte, vakHoogte);
		add(yAsNaamLabel);	
		
		xVakkenPanel = new JPanel();
		xVakkenPanel.setOpaque(false);
		xVakkenPanel.setLayout(null);
		xVakkenPanel.setLocation(
			xAsNaamLabel.getLocation().x + xAsNaamLabel.getSize().width + offSet,
			offSet);
		xVakkenPanel.setSize(
			getSize().width - xVakkenPanel.getLocation().x - rechterBreedte, 
			vakHoogte+1);
		add(xVakkenPanel);
		
		yVakkenPanel = new JPanel();
		yVakkenPanel.setOpaque(false);
		yVakkenPanel.setLayout(null);
		yVakkenPanel.setLocation(
			yAsNaamLabel.getLocation().x + yAsNaamLabel.getSize().width + offSet,
			2 * offSet + vakHoogte);
		yVakkenPanel.setSize(
			getSize().width - yVakkenPanel.getLocation().x - rechterBreedte, 
			vakHoogte);
		add(yVakkenPanel);

		aantalVakken = 50;
		firstIndexVisible = new int[3];
		firstIndexVisible[0] = 0;
		firstIndexVisible[1] = 0;
		firstIndexVisible[2] = 0;
		
		xVakken = new Vector(); 
		yVakken = new Vector(); 
		
		for (int vCnt = 0; vCnt < aantalVakken; vCnt++)
		{	TabelVak xVak = 
				new TabelVak(this, vCnt, 
							 vCnt * vakBreedte, 0, vakBreedte, vakHoogte+1, xVakEditable);
			xVak.zetFont(font);
			xVakkenPanel.add(xVak);
			xVakken.addElement(xVak);
				
		}
		for (int vCnt = 0; vCnt < aantalVakken; vCnt++)
		{	TabelVak yVak = 
				new TabelVak(this, vCnt, 
							 vCnt * vakBreedte, 0, vakBreedte, vakHoogte, yVakEditable);
			yVak.zetFont(font);
			yVakkenPanel.add(yVak);
			yVakken.addElement(yVak);
		}
	}
	
	public ImageIcon maakImageIcon(String s)
	{
		URL imageURL = GraphTool.class.getResource(s);
		ImageIcon imageIcon = new ImageIcon();
		if (imageURL != null) 
		{
		imageIcon = new ImageIcon(imageURL);
		}
		else
		{
			System.out.println("Error reading " + s);
		}
		return imageIcon;
	}
	
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

	public void zetAlsTekenTool(boolean b, boolean grafiekTekenToolAan)
	{	isTekenTool = b;
		if (isTekenTool)
		{	zoomInButton.setVisible(false);
			zoomUitButton.setVisible(false);
			functieKeuze.setVisible(false);
			tabelKeuze.setVisible(!grafiekTekenToolAan);			
			zetYAsNaam(yAsNaam, false);
			yAsNaamLabel.setVisible(true);
			resetButton.setVisible(!grafiekTekenToolAan);
			if(grafiekTekenToolAan)
				linkerBreedte = linkerBreedteTabel;
			else
				linkerBreedte = linkerBreedteTool;
			setXVakEditable(true);
			setYVakEditable(true);
		}
		else
		{	zoomInButton.setVisible(zooming);
			zoomUitButton.setVisible(zooming);
			tabelKeuze.setVisible(false);
			resetButton.setVisible(false);
			linkerBreedte = linkerBreedteTabel;
			setXVakEditable(false);
			setYVakEditable(false);
			if(interactiePanel.getTypeOpdracht() != interactiePanel.TEKENTABELPUNTEN)
				updateFunctieList(false);
		}
		xAsNaamLabel.setLocation(linkerBreedte, offSet);
		yAsNaamLabel.setLocation(linkerBreedte, 2 * offSet + vakHoogte);
		if(functieKeuze.isVisible())
		{	xVakkenPanel.setLocation(yAsNaamLabel.getLocation().x + 
				functieKeuze.getSize().width + offSet, offSet);
			yVakkenPanel.setLocation(yAsNaamLabel.getLocation().x + 
				functieKeuze.getSize().width + offSet, 2 * offSet + vakHoogte);
		}
		else
		{	xVakkenPanel.setLocation(yAsNaamLabel.getLocation().x + 
				yAsNaamLabel.getSize().width + offSet, offSet);
			yVakkenPanel.setLocation(yAsNaamLabel.getLocation().x + 
				yAsNaamLabel.getSize().width + offSet, 2 * offSet + vakHoogte);
		}
		xVakkenPanel.setSize(
			getSize().width - xVakkenPanel.getLocation().x - rechterBreedte, 
			vakHoogte);
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
	
	public void zetEenTabel(boolean b)
	{	
		eenTabel = b;
		if (b)
		{	linkerBreedte = linkerBreedteTabel;
		}
		else
		{	linkerBreedte = linkerBreedteTool;
		}
		xAsNaamLabel.setLocation(linkerBreedte, offSet);
		yAsNaamLabel.setLocation(linkerBreedte, 2 * offSet + vakHoogte);
		xVakkenPanel.setLocation(
			xAsNaamLabel.getLocation().x + xAsNaamLabel.getSize().width + offSet,
			offSet);
		xVakkenPanel.setSize(
			getSize().width - xVakkenPanel.getLocation().x - rechterBreedte, 
			vakHoogte);
		yVakkenPanel.setLocation(
			yAsNaamLabel.getLocation().x + xAsNaamLabel.getSize().width + offSet,
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
	
	
	
	public void setXVakEditable(boolean b)
	{	xVakEditable = b;
		// zet alle xVakken op xVakEditable
		for (int xCnt = 0; xCnt < xVakken.size(); xCnt++)
		{	TabelVak xVak = (TabelVak) xVakken.elementAt(xCnt);
			xVak.zetEditable(b);
		}
	}

	public boolean getXVakEditable()
	{
		return xVakEditable;
	}
	
	public void setYVakEditable(boolean b)
	{	yVakEditable = b;
		// zet alle yVakken op yVakEditable
		for (int yCnt = 0; yCnt < yVakken.size(); yCnt++)
		{	TabelVak yVak = (TabelVak) yVakken.elementAt(yCnt);
			yVak.zetEditable(b);
		}
		
	}
	
	public void setFrozen(boolean b)
	{
		frozen = b;
	}
	
	public void zetFirstIndexVisible(int firstIndexVis)
	{	schuifVakjesNaarRechts();
		int dx = 0;
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
		
	}
	
	public void vernieuwFirstIndexVisible(int index, int activeIndex)
	{	TabelVak netGevuldVak = (TabelVak) xVakken.elementAt(index);
		int vakjesBreedte = netGevuldVak.getSize().width;
		if(index > firstIndexVisible[activeIndex - 1])
		{	do{
				index--;
				TabelVak xVak = (TabelVak) xVakken.elementAt(index);
				vakjesBreedte += xVak.getSize().width;
			}
			while(vakjesBreedte < xVakkenPanel.getWidth() && index > firstIndexVisible[activeIndex - 1]);
		if(vakjesBreedte > xVakkenPanel.getWidth())
				index++;
		}
		
		zetFirstIndexVisible(index);
		firstIndexVisible[activeIndex - 1] = index;
	}
		
	public Hashtable getDocentState()
	{
		double docentBeginX = -2;
		double docentSchaalFactorX = 1;
		int[] docentFirstIndexVisible = new int[3];
		
		docentBeginX = beginX;
		docentSchaalFactorX = schaalFactorX;
		docentFirstIndexVisible = firstIndexVisible;
		
		Hashtable h = new Hashtable();
			
		h.put("docentTabelBeginX", new Double(docentBeginX));
		h.put("docentTabelSchaalFactorX", new Double(docentSchaalFactorX));
		h.put("docentTabelFirstIndexVisible", docentFirstIndexVisible);
		
		return h;
	}
	
	public Hashtable getState()
	{	
		double beginX = -2;
		double schaalFactorX = 1;
		int[] firstIndexVisible = new int[3];
		
		beginX = this.beginX;
		schaalFactorX = this.schaalFactorX;
		firstIndexVisible = this.firstIndexVisible;
		Hashtable h = new Hashtable();
			
		h.put("tabelBeginX", new Double(beginX));
		h.put("tabelSchaalFactorX", new Double(schaalFactorX));
		h.put("tabelFirstIndexVisible", firstIndexVisible);
		
		return h;
	}
	
	public void setState(Hashtable h, boolean docent)
	{	double beginX = -2;
		double schaalFactorX = 1;
		int[] firstIndexVisible = new int[3];
		
		if(docent)
		{	if (h.containsKey("docentTabelBeginX"))
			beginX = ((Number) h.get("docentTabelBeginX")).doubleValue();
    		if (h.containsKey("docentTabelSchaalFactorX")) 
				schaalFactorX = ((Number) h.get("docentTabelSchaalFactorX")).doubleValue();
			if (h.containsKey("docentTabelFirstIndexVisible"))
				firstIndexVisible = GraphToolInteractiePanel.toIntArray(h.get("docentTabelFirstIndexVisible"));
    	}
		else
		{	if (h.containsKey("tabelBeginX"))
				beginX = ((Number) h.get("tabelBeginX")).doubleValue();
    		if (h.containsKey("tabelSchaalFactorX")) 
				schaalFactorX = ((Number) h.get("tabelSchaalFactorX")).doubleValue();
			if (h.containsKey("tabelFirstIndexVisible"))
				firstIndexVisible = GraphToolInteractiePanel.toIntArray(h.get("tabelFirstIndexVisible"));
		}
		
    	this.beginX = beginX;	
    	this.schaalFactorX = schaalFactorX;
    	this.firstIndexVisible = firstIndexVisible;
    	
    	if(!isTekenTool)
		{	for(int i = 0; i < firstIndexVisible.length; i++)
    			firstIndexVisible[i] = 0;//int[] oldFirstIndexVisible = new int[3]; 
			zetFunctie(func, true);// dit bevat een reset()
		}
    	setRandomAllowed(false);
    }
	
	public void zetGrafiekComponent(GraphToolInteractiePanel gc)
	{	interactiePanel = gc;
		functieKeuze.setForeground(interactiePanel.getFormuleColor(0));
		tabelKeuze.setForeground(interactiePanel.getFormuleColor(0));
	}
	
	public GraphToolInteractiePanel getGrafiekComponent()
	{
		return interactiePanel;
	}
	
	public void zetXAsNaam(String xasnaam)
	{	xAsNaam = xasnaam;
		xAsNaamLabel.setText(xasnaam);
		int width = itFm.stringWidth(" " + xasnaam + " ");
		if (width <= labelBreedte)
		{	xAsNaamLabel.setSize(labelBreedte, vakHoogte);
			yAsNaamLabel.setSize(labelBreedte, vakHoogte);	
		} 
		else if ((width > labelBreedte) && (width <= 2 * labelBreedte))
		{	xAsNaamLabel.setSize(width, vakHoogte);
			yAsNaamLabel.setSize(width, vakHoogte);	
		}
		else
		{	xAsNaamLabel.setSize(2 * labelBreedte, vakHoogte);
			yAsNaamLabel.setSize(2 * labelBreedte, vakHoogte);	
		}
		xVakkenPanel.setLocation(
			xAsNaamLabel.getLocation().x + xAsNaamLabel.getSize().width + offSet,
			offSet);
		xVakkenPanel.setSize(
			getSize().width - xVakkenPanel.getLocation().x - rechterBreedte, 
			vakHoogte);
		yVakkenPanel.setLocation(
			yAsNaamLabel.getLocation().x + yAsNaamLabel.getSize().width + offSet,
			2 * offSet + vakHoogte);
		yVakkenPanel.setSize(
			getSize().width - yVakkenPanel.getLocation().x - rechterBreedte, 
			vakHoogte);
	}

	public void zetYAsNaam(String yn, boolean b)
	{	//boolean bepaalt of alleen de tekst moet worden veranderd, of ook de variabele.
		if(b)
			yAsNaam = yn;
		yAsNaamLabel.setText(yn);
		int width = itFm.stringWidth(" " + yn + " ");
		if (width <= labelBreedte)
		{	xAsNaamLabel.setSize(labelBreedte, vakHoogte);
			yAsNaamLabel.setSize(labelBreedte, vakHoogte);	
		} 
		else if ((width > labelBreedte) && (width <= 2 * labelBreedte))
		{	xAsNaamLabel.setSize(width, vakHoogte);
			yAsNaamLabel.setSize(width, vakHoogte);	
		}
		else
		{	xAsNaamLabel.setSize(2 * labelBreedte, vakHoogte);
			yAsNaamLabel.setSize(2 * labelBreedte, vakHoogte);	
		}
		xVakkenPanel.setLocation(
			xAsNaamLabel.getLocation().x + xAsNaamLabel.getSize().width + offSet,
			offSet);
		xVakkenPanel.setSize(
			getSize().width - xVakkenPanel.getLocation().x - rechterBreedte, 
			vakHoogte);
		yVakkenPanel.setLocation(
			yAsNaamLabel.getLocation().x + yAsNaamLabel.getSize().width + offSet,
			2 * offSet + vakHoogte);
		yVakkenPanel.setSize(
			getSize().width - yVakkenPanel.getLocation().x - rechterBreedte, 
			vakHoogte);
	}
	
	public void showFunctieKeuze(boolean b)
	{	functieKeuze.setVisible(b);
		// als zichtbaar: verschuiven
		if (b)	
		{	yAsNaamLabel.setVisible(false);
			xVakkenPanel.setLocation(
				xAsNaamLabel.getLocation().x +  functieKeuze.getSize().width + offSet,
				offSet);
			xVakkenPanel.setSize(
				getSize().width - xVakkenPanel.getLocation().x - rechterBreedte, 
				vakHoogte);
			yVakkenPanel.setLocation(
				yAsNaamLabel.getLocation().x + functieKeuze.getSize().width + offSet,
				2 * offSet + vakHoogte);
			yVakkenPanel.setSize(
				getSize().width - yVakkenPanel.getLocation().x - rechterBreedte, 
				vakHoogte);
		}
		else
		{	//linkerBreedte = linkerBreedteTabel;
			xVakkenPanel.setLocation(xAsNaamLabel.getLocation().x + yAsNaamLabel.getWidth() + offSet,
				offSet);
			xVakkenPanel.setSize(getSize().width - xVakkenPanel.getLocation().x - rechterBreedte, 
				vakHoogte);
			yVakkenPanel.setLocation(yAsNaamLabel.getLocation().x + yAsNaamLabel.getWidth() + offSet,
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

		for(int i=0 ; i<20 ; i++)
		{	if(i%2 == 0)
				g.setColor(new Color(200+5*(i/2),200+5*(i/2),200+5*(i/2)));
			if(i%2 == 1)
				g.setColor(new Color(202+5*(i/2),202+5*(i/2),202+5*(i/2)));
			g.fillRect(0,getHeight() - (i+1)*getHeight()/20, getWidth(),getHeight()/20+1);
		}
		setBorder(BorderFactory.createLineBorder(Color.lightGray));
		
	}
	
	public void setActiveIndex(int index, boolean setState)
	{	tabelKeuzeEnabled = false;
		tabelKeuze.setSelectedIndex(index - 1);
		tabelKeuze.setForeground(interactiePanel.getFormuleColor(index - 1));
		tabelKeuzeEnabled = true;
		int[] oldFirstIndexVisible = new int[3]; 
		for(int i = 0; i < oldFirstIndexVisible.length; i++)
			oldFirstIndexVisible[i] = firstIndexVisible[i];
		Vector points = interactiePanel.getPoints(index, docent);
		if(!setState) //deze voorwaarde is nodig voor random grafiekpunten
			reset();
		zetTabelPunten(points, true);
		for(int i = 0; i < firstIndexVisible.length; i++)
			firstIndexVisible[i] = oldFirstIndexVisible[i];

		zetFirstIndexVisible(firstIndexVisible[index - 1]);
		
	}
	
	//Deze methode stelt bewust niet firstIndexVisible ook op 0, want wordt
	//ook gebruikt in zetFirstIndexVisible.
	public void schuifVakjesNaarRechts()
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
	}
	
	
	public void reset()
	{	
		schuifVakjesNaarRechts();
		firstIndexVisible[interactiePanel.getActiveIndex()-1] = 0;
		for (int index = 0; index < xVakken.size(); index++)
		{	zetText(index, "", "");
		}
	}
	
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
	
	public Expressie getFunctie(int num)
	{	return functies[num];
	}
	
	public int getFunctie(String name)
	{	int result = -1;
		for (int eCnt = 0; eCnt < maxAantalExpressies; eCnt++)
		{	if ((functieNamen[eCnt] != null) &&
			     functieNamen[eCnt].equals(name))
				result = eCnt;     
		}
		return result;
	}
	
	public int getEersteFunctie()
	{	int result = -1;
		boolean found = false;
		for (int eCnt = 0; eCnt < maxAantalExpressies; eCnt++)
		{	if ((functies[eCnt] != null) && !found)
			{	result = eCnt;
				found = true;
			}
		}
		return result;
	}
	
	public void vindAantalExpressies()
	{	aantalExpressies = 0;
		for (int eCnt = 0; eCnt < maxAantalExpressies; eCnt++)
		{	if (functies[eCnt] != null)
				aantalExpressies++;
		}
	}
	
	public void updateTabelNames(String[] expNaam, int maxAantalFuncties, boolean setState)
	{
		for(int i = 0; i < maxAantalFuncties; i++)
			if(getFunctie(i) != null)
				zetFunctieNaam(i, expNaam[i]);
		updateFunctieList(setState);
	}
	
// aangeroepen uit functie-editor
	public void zetFunctieNaam(int num, String expNaam)
	{	functieNamen[num] = expNaam;	
	}

	public void zetFunctie(int nr, Expressie e, String expNaam, boolean update, boolean setState)
	{	
		functies[nr] = e;		
		functieNamen[nr] = expNaam;
		
		if (update)
			updateFunctieList(setState);
	}
	
	public void zetFunctie(Expressie e, boolean setState)
	{	
				
		// dit zou niet moeten gebeuren
		if (e == null)
		{	return;
		}
		// als oude expressie niet null, vervangen
		if (func != null && !setState)
			reset();
		
		// exp is nu niet null maar een nieuwe expressie
		func = e;
		for (int vCnt = 0; vCnt < xVakken.size(); vCnt++)
		{	double xWaarde = beginX + vCnt * schaalFactorX;
			zetFunctieWaarde(vCnt, xWaarde);
		}
		
		repaint();
	}
	
	public void zetFunctieWaarde(int index, double xWaarde)
	{	String xText = df.format(xWaarde);
		double yWaarde = func.geefWaarde(xWaarde);
		String yText = "";
		if (Double.isNaN(yWaarde) || Double.isInfinite(yWaarde))
			yText = "-";
		else
			yText = df.format(yWaarde);	
		zetText(index, xText, yText);	

	}
	
	public void updateFunctieList(boolean setState)
	{	
		vindAantalExpressies();
		// geen expressies of alle verwijderd
		if (aantalExpressies == 0)
		{	
			// expressieKeuze onzichtbaar
			functieKeuze.setVisible(false);
			yAsNaamLabel.setVisible(true);
			zetYAsNaam(yAsNaam, false);		 
			func = null;
			funcNum = -1;
			if(!setState)
			{	reset();
			}
		} 
		else if (aantalExpressies == 1)
		{	
			// expressieKeuze onzichtbaar
			functieKeuze.setVisible(false);
			int eIndex = getEersteFunctie();
			yAsNaamLabel.setVisible(true);
			zetYAsNaam(functieNamen[eIndex], false);
			// geen expressie actief
			funcNum = eIndex;
			zetFunctie(functies[funcNum], setState);
		}
		else 
		{	// vermijd actionEvents op expressieKeuze
			updatingList = true;
			// verwijder oude namen
			functieKeuze.removeAllItems();
		 	// nieuwe namen
		 	for (int nCnt = 0; nCnt < maxAantalExpressies; nCnt++)
		 	{	if (functieNamen[nCnt] != null)
		 			functieKeuze.addItem(functieNamen[nCnt]);
		 	}
		 	// nog geen expressie gezet of oude is verwijderd
			if ((funcNum == -1) || (functies[funcNum] == null))
			{	int eIndex = getEersteFunctie();
				funcNum = eIndex;
				zetFunctie(functies[funcNum], setState);
				functieKeuze.setSelectedItem(functieNamen[eIndex]);
			}
			else // oude expressie blijft geselecteerd
			{	functieKeuze.setSelectedItem(functieNamen[funcNum]);
			}
			showFunctieKeuze(true);
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
		zetText(vakIndex, xText, yText);
	}
	
	public void processRandomTabelPunt(int vakIndex, String xText, String yText)
	{	
		boolean xIsRandom = (xText.length() > 2) && (xText.charAt(0) == '#') && (xText.charAt(xText.length() - 1) == '#');
		boolean yIsRandom = (yText.length() > 2) && (yText.charAt(0) == '#') && (yText.charAt(yText.length() - 1) == '#');

		boolean xParam = false;
		double xVal = 0;
		String xString = "";
		boolean xError = false;
		if (xIsRandom)
		{	xVal = Double.NaN;
			xString = xText;
		}
		else
		{	for(int i = 0; i < interactiePanel.schuifParameters.length; i++)
			{	
				if(xText.equals(interactiePanel.schuifParameters[i].geefNaam()))
				{	xVal = interactiePanel.schuifParameters[i].geefWaarde();
					xParam = true;
					break;
				}
			}
			
			
			try
			{	if (xVakEditable && !xParam)
					xVal = Double.parseDouble(xText);
			}
			catch (NumberFormatException nfe)
			{	xError = true;
			}
		}
		
		boolean yParam = false;
		double yVal = 0;
		String yString = "";
		boolean yError = false;
		if (yIsRandom)
		{	yVal = Double.NaN;
			yString = yText;
		}
		else
		{	for(int i = 0; i < interactiePanel.schuifParameters.length; i++)
			{	
				if(yText.equals(interactiePanel.schuifParameters[i].geefNaam()))
				{	yVal = interactiePanel.schuifParameters[i].geefWaarde();
					yParam = true;
					break;
				}
			}
		
			try
			{	if (yVakEditable && !yParam)
				{	yVal = Double.parseDouble(yText);
				}
			}
			catch (NumberFormatException nfe)
			{	yError = true;
			}
		}
		
		if (xError || yError)
		{	interactiePanel.removePoint(vakIndex, interactiePanel.getActiveIndex(), docent);
		}
		else // kijk of er al een punt met vakIndex is
		{	
			RealPoint rp = new RealPoint(xVal, yVal);
			rp.setIndex(interactiePanel.getActiveIndex());
			rp.setTabelIndex(vakIndex);
			rp.setxString(xText);
			rp.setyString(yText);
			int tpIndex = getTabelPunt(vakIndex);
			if (tpIndex >= 0)
			{	interactiePanel.removePoint(vakIndex, interactiePanel.getActiveIndex(), docent);
			}
			interactiePanel.addInsert(rp, docent);
		}// update interactiePanel
		
		produceAction("points changed");
		
			
	}
	
	public void processTabelPunt(int vakIndex)
	{	
		TabelVak indexXVak = (TabelVak) xVakken.elementAt(vakIndex);
		TabelVak indexYVak = (TabelVak) yVakken.elementAt(vakIndex);
		String xText = indexXVak.geefText();
		String yText = indexYVak.geefText();
		// het nieuwe punt is geen volledig punt
		// er is b.v. een vakje uitgeveegd
		if ((xText.equals("") && xVakEditable) || (yText.equals("") && yVakEditable))
		{	interactiePanel.removePoint(vakIndex, interactiePanel.getActiveIndex(), docent);
			produceAction("points changed");
			return;
		}
		
		if (randomAllowed)
		{	processRandomTabelPunt(vakIndex, xText, yText);
			return;
		}
		
		boolean xParam = false;
		boolean yParam = false;
		double xVal = 0;
		double yVal = 0;
		boolean error = false;
		
		for(int i = 0; i < interactiePanel.schuifParameters.length; i++)
		{	
			if(xText.equals(interactiePanel.schuifParameters[i].geefNaam()))
			{	xVal = interactiePanel.schuifParameters[i].geefWaarde();
				xParam = true;
				break;
			}
			if(yText.equals(interactiePanel.schuifParameters[i].geefNaam()))
			{	yVal = interactiePanel.schuifParameters[i].geefWaarde();
				yParam = true;
				break;
			}
		}
		
		// dit zou niet nodig moeten zijn
		try
		{	if (xVakEditable && !xParam)
				xVal = Double.parseDouble(xText);
			if (yVakEditable && !yParam)
				yVal = Double.parseDouble(yText);
		}
		catch (NumberFormatException nfe)
		{	error = true;
		}
		if (error)
		{	interactiePanel.removePoint(vakIndex, interactiePanel.getActiveIndex(), docent);
		}
		else // kijk of er al een punt met vakIndex is
		{	
			RealPoint rp = new RealPoint(xVal, yVal);
			rp.setIndex(interactiePanel.getActiveIndex());			
			rp.setTabelIndex(vakIndex);
			if(xParam)
				rp.setxString(xText);
			if(yParam)
				rp.setyString(yText);
			int tpIndex = getTabelPunt(vakIndex);
			if (tpIndex >= 0)
			{	interactiePanel.removePoint(vakIndex, interactiePanel.getActiveIndex(), docent);
			}
			interactiePanel.addInsert(rp, docent);
		}
		produceAction("points changed");
		
		
	}
	
	// vind punt met gegeven vakIndex van de actieve tabel (if any)
	public int getTabelPunt(int vakIndex)
	{	// er is er hoogstens 1
		Vector points = interactiePanel.getPoints(interactiePanel.getActiveIndex(), docent);
		int tpIndex = -1;
		for (int tCnt = 0; tCnt < points.size(); tCnt++)
		{	RealPoint rp = (RealPoint) points.elementAt(tCnt);
			if (rp.getTabelIndex() == vakIndex)
				tpIndex = tCnt;
		}
		return tpIndex;
	}
	
	public void zetTabelPunten(Vector points, boolean maakLeeg)
	{ 	if(maakLeeg)
			for (int index = 0; index < xVakken.size(); index++)
			{	zetText(index, "", "");
			}
		
		int maxIndex = 0;
		for (int tCnt = 0; tCnt < points.size(); tCnt++)
		{	RealPoint rp = (RealPoint) points.elementAt(tCnt);
			maxIndex = Math.max(rp.getTabelIndex(), maxIndex);
		}
		// kijk of er voldoende vakjes zijn
		if (maxIndex > xVakken.size())
		{	int nieuweVakken = maxIndex - xVakken.size();
			for (int vCnt = 0; vCnt < nieuweVakken; vCnt++)
			{
				TabelVak lastVak = (TabelVak) xVakken.lastElement();
			
				TabelVak xVak = 
						new TabelVak(this, xVakken.size(), 
							lastVak.getLocation().x + lastVak.getSize().width, 
							0, vakBreedte, vakHoogte, xVakEditable);
				// constructie			
				xVak.zetFont(font);
				xVak.randomAllowed = randomAllowed;
				xVakkenPanel.add(xVak);
				xVakken.addElement(xVak);
				
				TabelVak yVak = 
					new TabelVak(this, xVakken.size(), 
						lastVak.getLocation().x + lastVak.getSize().width, 
						0, vakBreedte, vakHoogte, yVakEditable);
				// constructie		
				yVak.zetFont(font);
				yVak.randomAllowed = randomAllowed;
				yVakkenPanel.add(yVak);
				yVakken.addElement(yVak);
			}	
		}
		// zet de punten
		for (int tCnt = 0; tCnt < points.size(); tCnt++)
		{	RealPoint rp = (RealPoint) points.elementAt(tCnt);	
			zetTabelPunt(rp);
		}
		repaint();
	}
	
	public void zetTabelPunt(RealPoint rp)
	{	String xText = "";
		String yText = "";
		//eerst checken of het om een schuifparameter gaat. Dus is de naam (xString) gelijk aan een van de namen van de schuifparameters?
		boolean xParam = false;
		boolean yParam = false;
		for(int i = 0; i < interactiePanel.schuifParameters.length; i++)
		{	String naam = interactiePanel.schuifParameters[i].geefNaam();
			if(rp.getxString().equals(naam))
			{	xText = naam;
				xParam = true;
			}
			if(rp.getyString().equals(naam))
			{	yText = naam;
				yParam = true;
			}
		}
	
		if (!xParam && !Double.isNaN(rp.getX()))
			xText = df.format(rp.getX());		
		else if (!xParam && Double.isNaN(rp.getX()) && randomAllowed)
		{	xText = rp.getxString();
		}
		//String yText = "";
		if (!yParam && !Double.isNaN(rp.getY()))
			yText = df.format(rp.getY());
		else if (!yParam && Double.isNaN(rp.getY()) && randomAllowed) 
		{	yText = rp.getyString();
		}
		zetText(rp.getTabelIndex(), xText, yText);
		
	}
	
	public void pijlRechtsAction()
	{	// deze verdwijnt naar links
		TabelVak lxVak = (TabelVak) xVakken.elementAt(firstIndexVisible[interactiePanel.getActiveIndex()-1]);
		firstIndexVisible[interactiePanel.getActiveIndex()-1]++;
		beginX += schaalFactorX;
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
				new TabelVak(this, xVakken.size()+1, 
					lastVak.getLocation().x + lastVak.getSize().width, 
					0, vakBreedte, vakHoogte, xVakEditable);
		// constructie			
		xVak.zetFont(font);
		xVak.randomAllowed = randomAllowed;
		xVakkenPanel.add(xVak);
		xVakken.addElement(xVak);

		TabelVak yVak = 
			new TabelVak(this, yVakken.size()+1,//xVakken veranderd in yVakken; xVakken is momenteel ��n groter.
				lastVak.getLocation().x + lastVak.getSize().width, 
				0, vakBreedte, vakHoogte, yVakEditable);
		// constructie		
		yVak.zetFont(font);
		yVak.randomAllowed = randomAllowed;
		yVakkenPanel.add(yVak);
		yVakken.addElement(yVak);
		if (func != null)
		{	
			double lastXWaarde = lastVak.geefWaarde();
			if (!Double.isNaN(lastXWaarde))	
			{	
				zetFunctieWaarde(xVakken.size() - 1, 
					lastXWaarde + schaalFactorX);
			}
		}
		repaint();
	}

	public void pijlLinksAction(String s)
	{	pijlLinksAction(false);
	}
	
	public void pijlLinksAction(boolean updateVakIndex)
	{	
		if (firstIndexVisible[interactiePanel.getActiveIndex()-1] == 0)
		{	// maak nieuwe tabelVakken voor index 0 
			TabelVak xVak = 
				new TabelVak(this, 0, 0, 0, vakBreedte, vakHoogte + 1, xVakEditable);
			// constructie	
			xVak.zetFont(font);
			xVak.randomAllowed = randomAllowed;
			xVakkenPanel.add(xVak);
			for (int xCnt = 0; xCnt < xVakken.size(); xCnt++)
			{	TabelVak tabelVak = (TabelVak) xVakken.elementAt(xCnt);
				tabelVak.verhoogIndex();
				tabelVak.translate(vakBreedte);
			}
			xVakken.insertElementAt(xVak, 0);
				
			TabelVak yVak = 
				new TabelVak(this, 0, 0, 0, vakBreedte, vakHoogte, yVakEditable);
			// constructie	
			yVak.zetFont(font);
			yVak.randomAllowed = randomAllowed;
			yVakkenPanel.add(yVak);
			for (int yCnt = 0; yCnt < yVakken.size(); yCnt++)
			{	TabelVak tabelVak = (TabelVak) yVakken.elementAt(yCnt);
				tabelVak.verhoogIndex();
				tabelVak.translate(vakBreedte);
			}
			yVakken.insertElementAt(yVak, 0);
				
			// firstIndexVisible blijft 0
			for (int viCnt = 0; viCnt < interactiePanel.getPoints(interactiePanel.getActiveIndex(), docent).size(); viCnt++)
			{	RealPoint rp = (RealPoint) interactiePanel.getPoints(interactiePanel.getActiveIndex(), docent).elementAt(viCnt);
				int tpvi = rp.getTabelIndex();
				
				if (updateVakIndex)
				{	tpvi += 1;
					rp.setTabelIndex(tpvi);
				}
				interactiePanel.getPoints(interactiePanel.getActiveIndex(), docent).removeElementAt(viCnt);
				interactiePanel.getPoints(interactiePanel.getActiveIndex(), docent).insertElementAt(rp, viCnt);
			}
			
		}
		else // if (firstIndexVisible > 0)
		{	firstIndexVisible[interactiePanel.getActiveIndex()-1]--;
		
			// dit vak wordt zichtbaar
			TabelVak xVak = (TabelVak) xVakken.elementAt(firstIndexVisible[interactiePanel.getActiveIndex()-1]);
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
		if (func != null)
		{	beginX -= schaalFactorX;
			zetFunctieWaarde(0, beginX);	
		}	
	
	repaint();
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getActionCommand().equals("focus"))
			return;
	
		else if(e.getActionCommand().equals("knopje"))
		{
			if (e.getSource() == pijlLinksButton)
			{	if (!frozen)
				{
					pijlLinksAction(true);	
				}	
			}
			else if (e.getSource() == pijlRechtsButton)
			{	if (!frozen)
				{
					pijlRechtsAction();
				}	
			
			}
			else if (e.getSource() == resetButton)
			{	if (!frozen)
				{
					if(docent)
						reset();//(Reset gewone tabelComponent gebeurt nog in removePoints)
					interactiePanel.removePoints(interactiePanel.getActiveIndex(), docent);
					interactiePanel.repaint();
				
				}
			}
			else if (e.getSource() == zoomInButton)
			{	
						
				double factorX = 1;
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
				int[] oldFirstIndexVisible = new int[3]; 
				for(int i = 0; i < oldFirstIndexVisible.length; i++)
					oldFirstIndexVisible[i] = firstIndexVisible[i];
				zetFunctie(func, false);// dit bevat een reset()
				
				for(int i = 0; i < firstIndexVisible.length; i++)
					firstIndexVisible[i] = oldFirstIndexVisible[i];
				zetFirstIndexVisible(firstIndexVisible[interactiePanel.getActiveIndex() - 1]);
								
			}
			else if (e.getSource() == zoomUitButton)
			{	
						
				double factorX = 1;
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
				int[] oldFirstIndexVisible = new int[3]; 
				for(int i = 0; i < oldFirstIndexVisible.length; i++)
					oldFirstIndexVisible[i] = firstIndexVisible[i];// dit bevat een reset()
				zetFunctie(func, false);
				for(int i = 0; i < firstIndexVisible.length; i++)
					firstIndexVisible[i] = oldFirstIndexVisible[i];
	
				zetFirstIndexVisible(firstIndexVisible[interactiePanel.getActiveIndex() - 1]);
						
			}		
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
			if(index >= 0)
			{	tabelKeuze.setForeground(interactiePanel.getFormuleColor(index));
				interactiePanel.setActiveIndex(index + 1, false);
			}
		}
	}
	
	class FuncKeuzeAL implements ActionListener
	{	public void actionPerformed(ActionEvent e)
		{	if (!updatingList)
			{
				String name = (String) functieKeuze.getSelectedItem();
				funcNum = getFunctie(name);
				zetFunctie(functies[funcNum], false);
			}	
			functieKeuze.setForeground(interactiePanel.getFormuleColor(funcNum));
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
         	if ((index >= 0))	
         		setForeground(interactiePanel.getFormuleColor(index));
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
	
	String text = ""; 
	
	boolean editable; 
	
	FontMetrics fm;
	
	boolean randomAllowed = true;
	
	public TabelVak(TabelComponent o, int vIndex, 
				    int x, int y, int w, int h, boolean edit)
	{	
		owner = o;
		vakIndex = vIndex;
		setOpaque(false);
		setLayout(null);
		setBounds(x, y, w, h);
		editable = edit;
		tabelVakLabel = new JLabel("", SwingConstants.CENTER);
		tabelVakLabel.setBackground(Color.WHITE);
		tabelVakLabel.setOpaque(true);
		tabelVakLabel.setBounds(2, 1, w - 2, h - 1);
		add(tabelVakLabel);	
		tabelVakLabel.addMouseListener(new TextML());
		
		tabelVakTextField = new JTextField();
		tabelVakTextField.setBounds(3, 2, w-2, h - 2);
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
		tabelVakLabel.setBounds(2, 1, breedte - 2, h - 1);
		tabelVakTextField.setBounds(3, 2, breedte - 2, h - 2);
	}
	
	public void verhoogIndex()
	{	vakIndex++;
	}
	
	public void translate(int dx)
	{	setLocation(getLocation().x + dx, getLocation().y);
	}
	
	public void zetEditable(boolean b)
	{	editable = b;
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
			tabelVakTextField.setVisible(false);
			tabelVakLabel.setText(text);
			tabelVakLabel.setVisible(true);
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
			if ((randomAllowed && isLegal(txt, true)) || isLegal(txt, false))
			{	owner.adaptToText(vakIndex);
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
			
			if (corrected)
				tabelVakTextField.setText(txt);
			
			// pas de textbreedte aan
			owner.adaptToText(vakIndex);
		}
	
		public boolean isLegal(String s, boolean randomAllowed)
		{	
			if (s != null && s.length() > 0)
			{	if(randomAllowed && s.charAt(0) == '#')
					return true;
				else
				{	boolean isParameterNaam = false;
					for(int i = 0; i < owner.getGrafiekComponent().schuifParameters.length; i++)
					{
						if(owner.getGrafiekComponent().schuifParameters[i].geefNaam().contains(s))
							isParameterNaam = true;
					}
					return isParameterNaam;
				}
			}
			else 
				return false;
	}
	
		public boolean isLegal(char c)
		{	return Character.isDigit(c) || (c == '-') || (c == '.');
		}
	}
}
