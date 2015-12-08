package fi.graphtool;

import java.awt.AWTEventMulticaster;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;


import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import fi.beans.stringutils.StringUtils;
import fi.graphtool.VeldEditorOptiesButton.FieldGraphType;
//import fi.wiskopdr.GrafiekComponent;
//import fi.wiskopdr.VergelijkingVak;
//import fi.wiskopdr.GrafiekComponent;
//import fi.wiskopdr.ImageComponent;
import fi.wiskopdr.expressies.*;
import fi.wiskopdr.formuleobjects.*;
import fi.wiskopdr.WiskOpdr;

public class VeldComponent extends FormuleEditor implements FocusListener, MouseListener {
	
	/* component defaults & contstants */
	public final static int cVCMaxAantalFormules = 1;
	public final static String	cVeldGrafiekTypeStrings[] = { "Quiver", "Streamline" };

	public final static int cDefault_VeldComponentHoogte = 80;
	public final static FieldGraphType cDefault_VeldGrafiekType = FieldGraphType.QUIVER;

	private VergelijkingVak[] formuleVakken; 
	private DomeinButton[] domeinButtons;
	private double[][] domeinen;
	private String[][] domeinStrings;
	private JCheckBox[] checkboxen;
	private GraphToolInteractiePanel grafiekComponent;
	
	private JButton[] enOfKnoppen;
	private boolean[] isEn;
	
	private int maxAantalFormules = cVCMaxAantalFormules;
	private int aantalRegels=1;
	//private static Image GOEDKRUL,FOUTKRUIS;
	private int actiefNummer;
	
	private String xAsNaam = "x";
	private String yAsNaam = "y";
	String[] namen = {"f","g","h","i","j","k","l","m","n"};
	private boolean functieBeginZichtbaar = true;
	private boolean formeleFuncties = true;
	private boolean domeinInstelbaar = false;
	public boolean docent;
	
	boolean grafiekKleurInstelbaar = true;
	boolean functieBeginAanpasbaar = true;
	
	private boolean functieToegestaan = true;
	private boolean ongelijkheidToegestaan = true;
	private boolean implicieteFunctieToegestaan = false;
	private boolean verticaleLijnToegestaan = true;
	private boolean parametrisatieToegestaan = false;
	
	private static double[] DEFAULTDOMEIN;
	
	private int formuleX = 30;
	
	protected static int FUNCTIE = 0;
	protected static int ONGELIJKHEID = 1;
	protected static int IMPLICIETEFUNCTIE = 2;
	protected static int VERTICALELIJN = 3;
	protected static int PARAMETRISATIEX = 4;
	protected static int PARAMETRISATIEY = 5;
	
	private int[] soortVak;
	//private boolean[] isOngelijkheid;
	
	static DecimalFormatSymbols dfs;
	public static DecimalFormat df;
	
	
	public VeldComponent(boolean b)
	{	super(b);
		docent = false;
	
		dfs = new DecimalFormatSymbols();
		if(WiskOpdr.language.toString().equals("nl")) dfs.setDecimalSeparator(',');
		else dfs.setDecimalSeparator('.');
		if(WiskOpdr.language.toString().equals("nl")) dfs.setGroupingSeparator(' ');
		else dfs.setGroupingSeparator(' ');
		df = new DecimalFormat("0.##########", dfs);
		
		zetGrafiekOfEdit(true);
		setFocusable(true);
		addFocusListener(this);
		remove(formuleVak);
		formuleVak.removeActionListener(this);
		formuleVak.removeFocusListener(this);
		setScrollHorizontal(false);
		
		ndewortelKnop = new FormuleButton("ndewortel");
		ndewortelKnop.setBounds(112,2,20,20);
		ndewortelKnop.addActionListener(this);
		
		ndelogKnop = new FormuleButton("ndelog");
		ndelogKnop.setBounds(156,2,25,20);
		ndelogKnop.addActionListener(this);
		
		absKnop = new FormuleButton("abs");
		absKnop.setBounds(134,2,20,20);
		absKnop.addActionListener(this);
		
		domeinStrings = new String[maxAantalFormules][2];
		for(int i = 0; i < maxAantalFormules; i++)
		{	domeinStrings[i][0] = "$f" + Double.NEGATIVE_INFINITY + "@";
			domeinStrings[i][1] = "$f" + Double.POSITIVE_INFINITY + "@";
		}
		
		DEFAULTDOMEIN = new double[2];
		DEFAULTDOMEIN[0] = Double.NEGATIVE_INFINITY;
		DEFAULTDOMEIN[1] = Double.POSITIVE_INFINITY;
		
		domeinen = new double[maxAantalFormules][2];
		for(int i = 0; i < maxAantalFormules; i++)
		{	domeinen[i][0] = DEFAULTDOMEIN[0];
			domeinen[i][1] = DEFAULTDOMEIN[1];
		}		
		
		soortVak = new int[maxAantalFormules];
		for(int i = 0; i < maxAantalFormules; i++)
			soortVak[i] = FUNCTIE;
		//isOngelijkheid = new boolean[maxAantalFormules];
		//for(int i = 0; i < maxAantalFormules; i++)
		//	isOngelijkheid[i] = false;
		
		isEn = new boolean[maxAantalFormules];
		for(int i = 0; i<isEn.length; i++)
			isEn[i] = true;
	}
	
	public void setEditable(boolean b)
	{	formuleVakken[0].formuleVak.setEditable(b);	
	}
	
	public void zetMaxAantalFormules(int num, boolean setState) {	
		maxAantalFormules = num;
		boolean knoppenNodig = maxAantalFormules > 0;
		boolean checkboxenNodig = docent || (maxAantalFormules > 0 && (grafiekComponent == null || grafiekComponent.typeOpdracht == GraphToolInteractiePanel.GEENOPDRACHT));
		
		nieuweRegelKnop.setVisible(knoppenNodig);
		verwijderRegelKnop.setVisible(knoppenNodig);
		checkboxen[0].setVisible(checkboxenNodig);
		formuleX = checkboxenNodig ? 30 : 10;
		if(aantalRegels > maxAantalFormules)
			aantalRegels = maxAantalFormules;
				
		zetFormuleRegels(maxAantalFormules, setState);
		if(maxAantalFormules > domeinen.length)
		{	double[][] oudDomeinen = new double[domeinen.length][2];
			for(int i = 0; i < oudDomeinen.length; i++)
			{	oudDomeinen[i][0] = domeinen[i][0];
				oudDomeinen[i][1] = domeinen[i][1];
			}
			domeinen = new double[maxAantalFormules][2];
			for(int i = 0; i < oudDomeinen.length; i++)
			{	domeinen[i][0] = oudDomeinen[i][0];
				domeinen[i][1] = oudDomeinen[i][1];
			}
			for(int i = oudDomeinen.length; i < maxAantalFormules; i++)
			{	domeinen[i][0] = DEFAULTDOMEIN[0];
				domeinen[i][1] = DEFAULTDOMEIN[1];
			}
			String[][] oudDomeinStrings = new String[domeinStrings.length][2];
			for(int i = 0; i < oudDomeinen.length; i++)
			{	oudDomeinStrings[i][0] = domeinStrings[i][0];
				oudDomeinStrings[i][1] = domeinStrings[i][1];
			}
			domeinStrings = new String[maxAantalFormules][2];
			for(int i = 0; i < oudDomeinStrings.length; i++)
			{	domeinStrings[i][0] = oudDomeinStrings[i][0];
				domeinStrings[i][1] = oudDomeinStrings[i][1];
			}
			for(int i = oudDomeinStrings.length; i < maxAantalFormules; i++)
			{	domeinStrings[i][0] = "$f" + Double.NEGATIVE_INFINITY + "@";
				domeinStrings[i][1] = "$f" + Double.POSITIVE_INFINITY + "@";
			}
		}
	}
	
	public void zetXAsNaam(String s, boolean setState)
	{	String oudeXAsNaam = xAsNaam;
		xAsNaam = s;
		for(int i=0 ; i<maxAantalFormules ; i++)
		{	String vervangString = formuleVakken[i].formuleVak.toString();
			String vervangSubString = vervangString.substring(2, vervangString.length() - 1);
			vervangString = "$f" + vervangSubString.replaceAll(oudeXAsNaam, xAsNaam) + "@";
			formuleVakken[i].formuleVak.vulVak(vervangString);
			
			if(functieBeginZichtbaar)
			{	vervangString = formuleVakken[i].functieBeginVak.toString();
				vervangSubString = vervangString.substring(2, vervangString.length() - 1);
				vervangString = "$f" + vervangSubString.replaceAll(oudeXAsNaam, xAsNaam) + "@";
				formuleVakken[i].functieBeginVak.vulVak(vervangString);
			}
			parseFormule(i, setState);
		}
	}
	
	public void zetYAsNaam(String s, boolean setState)
	{	String oudeYAsNaam = yAsNaam;
		yAsNaam = s;
		
		for(int i=0 ; i<maxAantalFormules ; i++)
		{	String vervangString = formuleVakken[i].formuleVak.toString();
			String vervangSubString = vervangString.substring(2, vervangString.length() - 1);
			vervangString = "$f" + vervangSubString.replaceAll(oudeYAsNaam, yAsNaam) + "@";
			formuleVakken[i].formuleVak.vulVak(vervangString);
			parseFormule(i, setState);
		}
	}
	
	public void zetFormeleFuncties(boolean b, boolean setState)
	{	formeleFuncties = b;
		for(int i = 0; i < maxAantalFormules; i++)
			zetVoorvoegsel(i);
		grafiekComponent.updateTabelNames(geefExpNamen(), setState);	
	}
	
	public void zetDomeinInstelbaar(boolean b, boolean setState)
	{	domeinInstelbaar = b;
		if(grafiekComponent != null && grafiekComponent.typeOpdracht == 1)
			for(int i = 0; i < aantalRegels; i++)
				if(i < domeinButtons.length)
					domeinButtons[i].setVisible(domeinInstelbaar);
		for(int i = 0; i < aantalRegels + 1; i++)
			if(i < domeinButtons.length)
				parseFormule(i, setState);
	}
	
	public void zetVoorvoegsel(int regelnummer)
	{	String huidigeTekst = formuleVakken[regelnummer].formuleVak.toString();
		boolean vervangen = huidigeTekst.equals("$f@") || huidigeTekst.endsWith("=@");
		if(functieBeginZichtbaar && functieBeginAanpasbaar && vervangen)
		{	if (formeleFuncties) 
				formuleVakken[regelnummer].formuleVak.vulVak("$f"+namen[regelnummer]+"(" + xAsNaam + ")=@");
			else if (regelnummer > 1)
				formuleVakken[regelnummer].formuleVak.vulVak("$f"+yAsNaam+"$s"+(regelnummer+1)+"@=@");
			else 
				formuleVakken[regelnummer].formuleVak.vulVak("$f"+yAsNaam+"=@");
		}
		else if(functieBeginZichtbaar && !functieBeginAanpasbaar)
		{	if (formeleFuncties) 
				formuleVakken[regelnummer].functieBeginVak.vulVak("$f"+namen[regelnummer]+"(" + xAsNaam + ")=@");
			else if (aantalRegels > 1)
				formuleVakken[regelnummer].functieBeginVak.vulVak("$f"+yAsNaam+"$s"+(regelnummer+1)+"@=@");
			else 
				formuleVakken[regelnummer].functieBeginVak.vulVak("$f"+yAsNaam+"=@");
		}	
	}
	
	public void layoutVakken(boolean setState)
	{	int hoogte = 10;
		for(int i=0 ; i<maxAantalFormules ; i++)
		{	if(formuleVakken[i]!=null)
			{	formuleVakken[i].setLocation(formuleX,hoogte);
				int breedte = this.getWidth() - 25;
				if(getVerticalScrollBarVisible())	
					breedte = this.getWidth() - 40;
				if(soortVak[i] == PARAMETRISATIEX && checkboxen != null && checkboxen[i] != null)
				{	maakParametrisatieVak(i);
					
				}
				else if(checkboxen!=null && checkboxen[i]!=null)
					checkboxen[i].setLocation(4,hoogte+formuleVakken[i].ashoogte-5);
				
				if(domeinButtons!=null && domeinButtons[i]!=null)domeinButtons[i].setLocation(breedte, hoogte+formuleVakken[i].ashoogte-5);
				if(i>0 && enOfKnoppen != null && enOfKnoppen[i-1] != null)enOfKnoppen[i-1].setLocation(breedte, hoogte - 15);
				hoogte = hoogte + formuleVakken[i].getSize().height + 10;
			}
		}
		zetDomeinInstelbaar(domeinInstelbaar, setState);
		repaint();
	}
	
	public void zetGrafiekComponent(GraphToolInteractiePanel gc)
	{	grafiekComponent = gc;
		zetGrafiekKleuren();
	}
	
	public void zetGrafiekKleuren()
	{	if(formuleVakken != null && grafiekComponent != null)
			for(int i=0 ; i<formuleVakken.length ; i++)
				formuleVakken[i].setFGColor(grafiekComponent.getFormuleColor(i));
		repaint();
	}
	
	public void zetGrafiekKleurInstelbaar(boolean b)
	{	grafiekKleurInstelbaar = b;
		for(int i = 0; i < checkboxen.length; i++)
		{	if(b)
				checkboxen[i].addMouseListener(this);
			else
				checkboxen[i].removeMouseListener(this);
		}
	}
	
	public void zetFunctieBeginZichtbaar(boolean b, boolean setState)
	{	functieBeginZichtbaar = b;
		zetFormuleRegels(maxAantalFormules, setState);
	}
	
	public void zetFunctieBeginAanpasbaar(boolean b, boolean setState)
	{	functieBeginAanpasbaar = b;
		zetFormuleRegels(maxAantalFormules, setState);
	}
	
	public void zetToegestaneFormules(boolean functie, boolean ongelijkheid, boolean impliciet, boolean verticaal, boolean parametrisatie, boolean setState)
	{	functieToegestaan = functie;
		ongelijkheidToegestaan = ongelijkheid;
		implicieteFunctieToegestaan = impliciet;
		verticaleLijnToegestaan = verticaal;
		parametrisatieToegestaan = parametrisatie;
		for(int i = 0; i < aantalRegels; i++)
			parseFormule(i, setState);
	}
	
	
	public int getAantalRegels()
	{
		return aantalRegels;
	}
	
	public Hashtable getDocentState()
	{	String[] docentExpressieStrings = null;
		boolean[] docentGeselecteerd = null;
		double[][] docentDomeinen = null;
		String[][] docentDomeinStrings = null;
		boolean[] docentIsEn = null;
		//hier moet nog bij: ingestelde kleuren (?) Of wordt dat ergens anders bewaard?
	
		docentExpressieStrings = new String[maxAantalFormules];
		docentGeselecteerd = new boolean[maxAantalFormules];
		docentIsEn = new boolean[maxAantalFormules];
		docentDomeinStrings = domeinStrings;
		if(domeinen == null)
			docentDomeinen = null;
		else
		{	docentDomeinen = new double[domeinen.length][2];
			for(int i = 0; i < domeinen.length; i++)
			{	docentDomeinen[i][0] = domeinen[i][0];
				docentDomeinen[i][1] = domeinen[i][1];
			}
		}
		for(int i=0 ; i<maxAantalFormules ; i++)
		{	if(functieBeginAanpasbaar)
				docentExpressieStrings[i] = formuleVakken[i].formuleVak.toString();
			else
			{	String s1 = formuleVakken[i].functieBeginVak.toString();
				String s2 = formuleVakken[i].formuleVak.toString();
				try{
					s1 = s1.substring(0, s1.length() - 1);
					s2 = s2.substring(2);
				}
				catch(Exception e){}
				docentExpressieStrings[i] = s1 + s2;
			}
			docentGeselecteerd[i] = checkboxen[i].isSelected();
			docentIsEn[i] = isEn[i];
		}		
	
		Hashtable h = new Hashtable();
		h.put("docentExpressieStrings", docentExpressieStrings);
		h.put("docentGeselecteerd", docentGeselecteerd);
		h.put("docentDomeinen", docentDomeinen);
		h.put("docentDomeinStrings", docentDomeinStrings);
		h.put("docentIsEn", docentIsEn);
		return h;
		
	}
	
	public Hashtable getState()
	{	String[] expressieStrings = null;
		boolean[] geselecteerd = null;
		String[][] domeinStrings = null;
		boolean[] isEn = null;
		expressieStrings = new String[maxAantalFormules];
		geselecteerd = new boolean[maxAantalFormules];
		isEn = new boolean[maxAantalFormules];
		domeinStrings = this.domeinStrings;
		int teller = 0;
		for(int i=0 ; i<maxAantalFormules ; i++)
		{	if(functieBeginAanpasbaar)
				expressieStrings[i] = formuleVakken[i].formuleVak.toString();
			else
			{	String s1 = formuleVakken[i].functieBeginVak.toString();
				String s2 = formuleVakken[i].formuleVak.toString();
				try{
					s1 = s1.substring(0, s1.length() - 1);
					s2 = s2.substring(2);
				}
				catch(Exception e){}
				expressieStrings[i] = s1 + s2;
			}
			if(expressieStrings[i].endsWith("=@"))
				expressieStrings[i] = "$f@";
			geselecteerd[i] = checkboxen[i].isSelected();
			isEn[i] = this.isEn[i];
		}		
		Hashtable h = new Hashtable();
	    h.put("expressieStrings", expressieStrings);
		h.put("geselecteerd", geselecteerd);
	    h.put("domeinStrings", domeinStrings);
	    h.put("isEn", isEn);
	    return h;
	}
	
	public void setState(Hashtable h, String[] randomVars, Hashtable randomValues, boolean docent)
    {	String[] expressieStrings = null;
		boolean[] geselecteerd = null;
    	String[][] domeinStrings = null;
    	boolean[] isEn = null;
    	
    	if(docent)
    	{	if(h.containsKey("docentExpressieStrings")) 
    			expressieStrings = GraphToolInteractiePanel.toStringArray(h.get("docentExpressieStrings"));
    		if(h.containsKey("docentGeselecteerd")) 
    			geselecteerd = (boolean[])h.get("docentGeselecteerd");
    		if(h.containsKey("docentDomeinStrings"))
    			domeinStrings = (String[][])h.get("docentDomeinStrings");
    		if(h.containsKey("docentIsEn")) 
        		isEn = (boolean[])h.get("docentIsEn");
        	
    	}
    	else
    	{  	if(h.containsKey("expressieStrings")) 
    			expressieStrings = GraphToolInteractiePanel.toStringArray(h.get("expressieStrings"));
    		if(h.containsKey("geselecteerd")) 
    			geselecteerd = GraphToolInteractiePanel.toBooleanArray(h.get("geselecteerd"));
    		if(h.containsKey("domeinStrings"))
    			domeinStrings = GraphToolInteractiePanel.toStringArrayArray(h.get("domeinStrings"));
    		if(h.containsKey("isEn")) 
        		isEn = GraphToolInteractiePanel.toBooleanArray(h.get("isEn"));
        }
    	
    	if(expressieStrings==null) 
    	{	return;
    	}
    	this.domeinStrings = domeinStrings;
    	if(domeinStrings != null)
    		domeinen = new double[domeinStrings.length][2];
     	for(int i=0 ; i<domeinStrings.length; i++)
		{	
     		if(domeinStrings != null && i < domeinStrings.length && i < domeinButtons.length)
     		{	if(!domeinStrings[i].equals("$f@"))
    			{	if(randomVars != null)
    				{	try
						{	domeinStrings[i][0] = FormuleParser.randomizeString(domeinStrings[i][0],randomVars,randomValues);
						}
						catch(Exception e)
						{	domeinStrings[i][0] = "$f???@";
							this.zetRandomFout(true);
						}
						try
						{	domeinStrings[i][1] = FormuleParser.randomizeString(domeinStrings[i][1],randomVars,randomValues);
						}
						catch(Exception e)
						{	domeinStrings[i][1] = "$f???@";
							this.zetRandomFout(true);
						}
    				}
    				zetDomein(domeinStrings[i], i);
    			}
     			domeinButtons[i].zetDomeinString(domeinStrings[i]);
     		}
		}
     	if(docent)
     		//grafiekComponent.zetDocentDomeinen(domeinen);
     		grafiekComponent.zetDocentDomeinen(domeinStrings);
     	for(int i = 0; i < expressieStrings.length; i++)	
     	{	if(!expressieStrings[i].equals("$f@") && !(i > 0 && expressieStrings[i].endsWith("=@") && docent))
			{	if(randomVars != null)
     			try			
    			{	expressieStrings[i] = FormuleParser.randomizeString(expressieStrings[i],randomVars,randomValues);
    			}
    			catch(Exception e)
    			{	expressieStrings[i] = "$f???@";
    				this.zetRandomFout(true);
    			}
				if(functieBeginAanpasbaar)
					formuleVakken[i].formuleVak.vulVak(expressieStrings[i]);
				parseFormule(expressieStrings[i], i, true);
			
     			if(i>0)
					add(formuleVakken[i],0);
				formuleVakken[i].setVisible(true);
				if(geselecteerd!=null)
					checkboxen[i].setSelected(geselecteerd[i]);
				if(docent || (maxAantalFormules > 0 && (grafiekComponent == null || grafiekComponent.typeOpdracht == GraphToolInteractiePanel.GEENOPDRACHT)))
				{	//System.out.println("hier visible gezet? " + i);
					add(checkboxen[i],0);
					//checkboxen[i].setVisible(true);
				
				}
				add(domeinButtons[i], 0);
				domeinButtons[i].setVisible(false);
				this.isEn[i] = isEn[i];
				if(geselecteerd[i]) 
     				parseFormule(i, true);
				if(i>0)
				{	add(enOfKnoppen[i-1],0);
					if(isEn[i-1])
						enOfKnoppen[i-1].setText(GraphTool.rb.getString("enOfButton_En"));
					else
						enOfKnoppen[i-1].setText(GraphTool.rb.getString("enOfButton_Of"));
				}
     			aantalRegels = i+1;
			}
			
		}
     	layoutVakken(true);
     	grafiekComponent.updateTabelNames(geefExpNamen(), true);
		
    }
	
	public double[] getDomein()
	{
		return domeinen[0];
	}
	
	public double[][] getDomeinen()
	{
		return domeinen;
	}
	
	public void zetDomein(double[] domein)
	{
		domeinen[0][0] = domein[0];
		domeinen[0][1] = domein[1];
	}
	
	public String[][] getDomeinStrings()
	{
		return domeinStrings;
	}
	
	public void zetDomeinen(double[][] domein)
	{	if(domein == null)
			domeinen = null;
		else
		{	domeinen = new double[domein.length][2];
			for(int i = 0; i < domeinen.length; i++)
			{	domeinen[i][0] = domein[i][0];
				domeinen[i][1] = domein[i][1];
			}
		}
		for(int i = 0; i < domeinen.length; i++)
		{	domeinStrings[i][0] = "$f" + Double.toString(domeinen[i][0]) + "@";
			domeinStrings[i][1] = "$f" + Double.toString(domeinen[i][1]) + "@";
		}
		for(int i = 0; i < Math.min(domeinButtons.length, domeinen.length); i++)
			domeinButtons[i].zetDomeinString(domeinStrings[i]);
	}
	
	public void zetDomein(String[] domeinStrings, int i)
	{	
		if(domeinen.length > i)
		{	if(domeinStrings == null)
			{	domeinen[i][0] = DEFAULTDOMEIN[0];
				domeinen[i][1] = DEFAULTDOMEIN[1];
				return;
			}
			if(domeinStrings[0].equals("$f" + Double.NEGATIVE_INFINITY + "@"))
			{	domeinen[i][0] = Double.NEGATIVE_INFINITY;
			}
			else if(FormuleParser.geefExpressie(domeinStrings[0]) == null)
			{	domeinen[i][0] = Double.NEGATIVE_INFINITY;
			}
			else
			{	domeinen[i][0] = FormuleParser.geefExpressie(domeinStrings[0]).geefWaarde();
			}
			if(domeinStrings[1].equals("$f" + Double.POSITIVE_INFINITY + "@"))
				domeinen[i][1] = Double.POSITIVE_INFINITY;
			else if(FormuleParser.geefExpressie(domeinStrings[1]) == null)
				domeinen[i][1] = Double.POSITIVE_INFINITY;
			else
				domeinen[i][1] = FormuleParser.geefExpressie(domeinStrings[1]).geefWaarde();
		}
	}
	
	
	public void resetDomeinen()
	{	domeinStrings = new String[maxAantalFormules][2];
		for(int i = 0; i < maxAantalFormules; i++)
		{	domeinStrings[i][0] = "$f" + Double.toString(Double.NEGATIVE_INFINITY) + "@";
			domeinStrings[i][1] = "$f" + Double.toString(Double.POSITIVE_INFINITY) + "@";
		}
		
		domeinen = new double[maxAantalFormules][2];
		for(int i = 0; i < maxAantalFormules; i++)
		{	domeinen[i][0] = DEFAULTDOMEIN[0];
			domeinen[i][1] = DEFAULTDOMEIN[1];
		}
		
	}
	
	public void finish()
	{	for(int i=0 ; i<maxAantalFormules ; i++)
		{	formuleVakken[i].formuleVak.finish();
		}
	}
	
	public void zetFormuleRegels(int maxAantalFormules, boolean setState) {	
		String[] exps = new String[maxAantalFormules];
		for(int i = 0; i < maxAantalFormules; i++)
			exps[i] = "$f@";
		for(int i = 0; formuleVakken != null && i < formuleVakken.length; i++)
			if(formuleVakken[i] != null)
			{	if(i < maxAantalFormules)
				{	if(functieBeginAanpasbaar)
					{	exps[i] = formuleVakken[i].formuleVak.toString();
					}
					else
					{	
						String s1 = formuleVakken[i].functieBeginVak.toString();
						String s2 = formuleVakken[i].formuleVak.toString();
						try{
							s1 = s1.substring(0, s1.length() - 1);
							s2 = s2.substring(2);
						}
						catch(Exception e){}
						exps[i] = s1 + s2;
					}
				}
				remove(formuleVakken[i]);
			}
		boolean[] geselecteerd = new boolean[maxAantalFormules];
		for(int i = 0; checkboxen != null && i < checkboxen.length; i++)
			if(checkboxen[i] != null)
			{	if(i < geselecteerd.length)
					geselecteerd[i] = checkboxen[i].isSelected();
				remove(checkboxen[i]);
			}
		for(int i = 0; domeinButtons != null && i < domeinButtons.length; i++)
			if(domeinButtons[i] != null)
				remove(domeinButtons[i]);
		
		this.maxAantalFormules = maxAantalFormules; 
		
		formuleVakken = new VergelijkingVak[maxAantalFormules];
		
		for(int i=0 ; i<maxAantalFormules ; i++)
		{	formuleVakken[i] = new VergelijkingVak(functieBeginAanpasbaar);
			formuleVakken[i].setFont(WiskOpdr.formuleFont0);
			formuleVakken[i].setLocation(formuleX,10 + 35*i);
			formuleVakken[i].setOpaque(false);
			if(grafiekComponent != null)
				formuleVakken[i].setFGColor(grafiekComponent.getFormuleColor(i));
			if(functieBeginAanpasbaar)
				formuleVakken[i].formuleVak.vulVak(exps[i]);
			parseFormule(exps[i], i, setState);
			formuleVakken[i].formuleVak.addActionListener(this);
			formuleVakken[i].formuleVak.addFocusListener(this);
		}
		
		if(maxAantalFormules > 0)
		//if(docent || (maxAantalFormules > 1 && (grafiekComponent == null || grafiekComponent.typeOpdracht == GraphToolInteractiePanel.GEENOPDRACHT)))
		{	checkboxen = new JCheckBox[maxAantalFormules];
			for(int i=0 ; i<maxAantalFormules ; i++)
			{	checkboxen[i] = new JCheckBox();
				checkboxen[i].setBounds(4,12 + 35*i, 17, 17);
				checkboxen[i].setOpaque(false);
				checkboxen[i].addActionListener(this);
				if(i < geselecteerd.length)
					checkboxen[i].setSelected(geselecteerd[i]);
				if(grafiekKleurInstelbaar)
					checkboxen[i].addMouseListener(this);
			}
		}
		domeinButtons = new DomeinButton[maxAantalFormules];
		for(int i=0; i < maxAantalFormules; i++)
		{	domeinButtons[i] = new DomeinButton();
			if(i < domeinStrings.length)
				domeinButtons[i].zetDomeinString(domeinStrings[i]);
			domeinButtons[i].addActionListener(this);
		}
		domeinButtons[0].setLocation(this.getWidth() - 25, 5 + formuleVakken[0].ashoogte);
		
		for(int i = 0; i < aantalRegels; i++)
		{	add(formuleVakken[i],0);
			if(docent || (maxAantalFormules > 0 && (grafiekComponent == null || grafiekComponent.typeOpdracht == GraphToolInteractiePanel.GEENOPDRACHT)))
				add(checkboxen[i]);
			add(domeinButtons[i]);
		}
		domeinButtons[0].setVisible(false);
		isEn = new boolean[maxAantalFormules];
		for(int i = 0; i<isEn.length; i++)
			isEn[i] = true;
		
		enOfKnoppen = new JButton[maxAantalFormules];
		for(int i=0 ; i<maxAantalFormules ; i++)
		{	enOfKnoppen[i] = new JButton(GraphTool.rb.getString("enOfButton_En"));
			enOfKnoppen[i].setMargin(new Insets(0,0,0,0));
			enOfKnoppen[i].setSize(25, 20);
			enOfKnoppen[i].setOpaque(false);
			enOfKnoppen[i].addActionListener(this);
		}
		
		formuleVakken[0].setVisible(true);
		for(int i = 0; i < formuleVakken.length; i++) //aangepast 20-1-2014; leidt dit tot problemen? Dan terugzetten naar alleen doen voor 0 en niet voor alle i.
		{
			if(formuleVakken[i].formuleVak.toString().equals("$f@"))
				zetVoorvoegsel(i);
			else if(formuleVakken[i].formuleVak.toString().equals("$f"+namen[i]+"(" + xAsNaam + ")=@") ||
					formuleVakken[i].formuleVak.toString().equals("$f"+yAsNaam+"$s"+(i+1)+"@=@")||
					formuleVakken[i].formuleVak.toString().equals("$f"+yAsNaam+"=@"))
				if(!functieBeginZichtbaar)
					formuleVakken[i].formuleVak.vulVak("$f@");
		}
			
		formuleVak = formuleVakken[0].formuleVak;
		formuleVak.requestFocus();
		actiefNummer = 0;
		
		if(docent || (maxAantalFormules > 0 && (grafiekComponent == null || grafiekComponent.typeOpdracht == GraphToolInteractiePanel.GEENOPDRACHT)))
		{	checkboxen[0].setVisible(true);	
			checkboxen[0].setSelected(true);
		}
		
		layoutVakken(setState);
	}
	
	/*
	public static void zetPlaatjes(Image gk, Image fk)
	{	GOEDKRUL = gk;
		FOUTKRUIS = fk;
	}
	*/
	
	public FormuleVak geefFormuleVak()
	{	return formuleVak;
	}
	
	public Expressie geefExpressie()
	{	FormuleParser p = new FormuleParser();
		return formuleVak.geefExpressie();
	}
	
	public String[] geefExpNamen()
	{	String[] expNaam = new String[maxAantalFormules];
		for (int i = 0; i < maxAantalFormules; i++)
		{	expNaam[i] = geefExpNaam(i);
		}
		return expNaam;
	}
	
	public String geefExpNaam(int i)
	{	String expNaam = "";
		if(formeleFuncties)
			expNaam = namen[i] + "(" + xAsNaam + ")";
		else if (aantalRegels > 1)
			expNaam = yAsNaam + (i + 1);
		else
			expNaam = yAsNaam;
	
		return expNaam;
	}
	
	public int getMaxAantalFuncties()
	{
		return maxAantalFormules;
	}
	
	public boolean geefIsEn(int i)
	{
		return isEn[i];
	}
	
	public void terugNaarEenRegel(boolean setState)
	{	for (int rCnt = aantalRegels; rCnt > 1; rCnt--)
		{	
			grafiekComponent.zetFunctie(aantalRegels - 1, null, "$f@", null, DEFAULTDOMEIN, true, setState, docent);
			
			formuleVakken[aantalRegels-1].formuleVak.vulVak("$f@");
			remove(formuleVakken[aantalRegels-1]);
			remove(checkboxen[aantalRegels-1]);
			remove(domeinButtons[aantalRegels-1]);
			layoutVakken(setState);
			aantalRegels--;
		}
		if(functieBeginZichtbaar)
		{	for(int i=0 ; i<maxAantalFormules ; i++)
			{	zetVoorvoegsel(i);
			}
		}
		else
			for(int i = 0; i < maxAantalFormules; i++)
				formuleVakken[i].formuleVak.vulVak("$f@");
		
		parseFormule("$f@", 0, setState);
	}
	
	public void parseFormule(int regelnummer, boolean setState)
	{	//System.out.println("parseFormule(" + regelnummer + ", " + Boolean.toString(setState));
		if(regelnummer >= formuleVakken.length)
			return;
		if(formuleVakken[regelnummer].functieBeginVak == null || formuleVakken[regelnummer].functieBeginVak.toString().length() == 0)
		{	String s = formuleVakken[regelnummer].formuleVak.toString();
			parseFormule(s, regelnummer, setState);
		}
		else
		{	String s1 = formuleVakken[regelnummer].functieBeginVak.toString();
			String s2 = formuleVakken[regelnummer].formuleVak.toString();
			try{
				s1 = s1.substring(0, s1.length() - 1);
				s2 = s2.substring(2);
				String s = s1 + s2;
				parseFormule(s, regelnummer, setState);
			}
			catch(Exception e){}
		}
	}
	
	//public Vergelijking parseFormule(String s)
	public void parseFormule(String s, int regelnummer, boolean setState)
	{	//System.out.println("parseFormule(" + s + ", " + regelnummer + ", " + Boolean.toString(setState));
		//In alle lijstjes met expressies het huidige regelnummer verwijderen. 
		//Zo voorkom je dat expressies blijven staan als het type expressie verandert.
		//Hier moet ik nog even goed naar kijken in het geval van parametrisaties, omdat je dan twee regelnummers tegelijk nodig hebt.
		
		//voor parametrisaties is er een aantal opties:
		//er staat al een xparametrisatie, dan is de volgende regel ook een y-parametrisatie. Haal je die dan ook weg?
		//in principe wel, als je een nieuwe xparametrisatie typt, dan wordt de volgende regel automatisch weer gemarkeerd als yparam.

		if(grafiekComponent != null && grafiekComponent.typeOpdracht != 1 && regelnummer < domeinButtons.length)
			domeinButtons[regelnummer].setVisible(false);
		if(soortVak[regelnummer] == PARAMETRISATIEX)
		{	soortVak[regelnummer] = FUNCTIE;
			if(regelnummer < maxAantalFormules - 1)
				soortVak[regelnummer + 1] = FUNCTIE;
		}
		else if(soortVak[regelnummer] != PARAMETRISATIEY)
			soortVak[regelnummer] = FUNCTIE;
		
		//isOngelijkheid[regelnummer] = false;
		if(grafiekComponent != null)
		{	grafiekComponent.zetOngelijkheid(regelnummer, null, true, true, false);
			grafiekComponent.zetFunctie(regelnummer, null, "$f@", null, DEFAULTDOMEIN, true, setState, docent);
			grafiekComponent.zetVerticaleLijn(regelnummer, null);
		}
		
		//Altijd tekst ook in formuleregel zetten, zodat geparste formule 'gelijk loopt' met wat er in de regel staat.
		if(functieBeginAanpasbaar)
		{	formuleVakken[regelnummer].formuleVak.vulVak(s);
		}
		else
		{	try{
			String[] splitString = StringUtils.split(s, "=");
			formuleVakken[regelnummer].formuleVak.vulVak("$f" + splitString[1] + "@");
			}
			catch(Exception e)
			{ 
				formuleVakken[regelnummer].formuleVak.vulVak(s);
			}
		}
		
		try
		{	s = s.substring(2,s.length()-1);
			if(s.length()==0)
			{	return;
			}
			String[] vergTekens = {"=", ">", "<", "\u2264", "\u2265"};
			int tekenGetal = 0;
			String[] expressieStrings = null;
			Expressie e1 = null; //nu nog niet gebruikt, maar dat komt nog wel bij impliciete functies
			Expressie e2 = null;
			
			boolean split = false;
		    for(int j=0 ; j<vergTekens.length && !split; j++)
		    {	expressieStrings  = StringUtils.split(s,vergTekens[j]);
		        if(expressieStrings.length==2)
		    	{ 	
		        	e1 = FormuleParser.parse(FormuleParser.schoon(FormuleParser.formuleString("$f" + expressieStrings[0] + "@")));
	    			e2 = FormuleParser.parse(FormuleParser.schoon(FormuleParser.formuleString("$f" + expressieStrings[1] + "@")));
	    			
	    			if(expressieStrings[0] == null || expressieStrings[1] == null) 
			    	{	split = false;
			    	}
			    	else 
			    	{	split = true;
			    		tekenGetal = j;
			    	}
	    			break;
		    	}
			}
		    
		    if(!split)
		    {	return;
		    }
		    while(expressieStrings[0].endsWith(" "))
				expressieStrings[0] = expressieStrings[0].substring(0, expressieStrings[0].length() - 1);
		    //if(!functieBeginAanpasbaar && expressieStrings.length == 2)
		    	//vulFunctieRegel(expressieStrings[0], expressieStrings[1], vergTekens[tekenGetal], regelnummer);
		    if(!functieBeginAanpasbaar && expressieStrings.length == 2)
		    {	vulFunctieRegel(expressieStrings[0], expressieStrings[1], regelnummer);
		    }
		   		    
		    /* Volgens mij niet nodig: 
		    if(expressieStrings[0] == null || expressieStrings[1] == null)
		    {	return;
		    }
		    */
		    if(tekenGetal > 0 && !ongelijkheidToegestaan) // geval ongelijkheid
		    {	formuleVakken[regelnummer].formuleVak.vulVak("$f@");
		    	return;
		    }
		    else if(tekenGetal > 0)	
		    {	if(expressieStrings[0].equals(xAsNaam))
		    	{	boolean isGroterGelijk = true;
		    		if(tekenGetal == 2 || tekenGetal == 3)
		    			isGroterGelijk = false;
		    		if(checkboxen[regelnummer].isSelected())
		    			grafiekComponent.zetOngelijkheid(regelnummer, e2, false, isGroterGelijk, isEn[regelnummer]); 
		    		soortVak[regelnummer] = ONGELIJKHEID;
		    		//isOngelijkheid[regelnummer] = true;
		    	}
		    	else if(expressieStrings[0].equals(yAsNaam))
		    	{	boolean isGroterGelijk = true;
	    			if(tekenGetal == 2 || tekenGetal == 3)
	    				isGroterGelijk = false;
	    			if(checkboxen[regelnummer].isSelected())
	    				grafiekComponent.zetOngelijkheid(regelnummer, e2, true, isGroterGelijk, isEn[regelnummer]); 
	    			soortVak[regelnummer] = ONGELIJKHEID;
	    			//isOngelijkheid[regelnummer] = true;
		    	}
		    }//let op: neemt nu ook uitdrukkingen als sin(x) mee. Zorgen dat dat soort uitdrukkingen (impliciete functies) 
		    //er voor deze tijd al uitgefilterd zijn.
		    else if(expressieStrings[0].equals(yAsNaam) || expressieStrings[0].endsWith("(" + xAsNaam + ")"))
		    {	if(!functieToegestaan)
		    	{	formuleVakken[regelnummer].formuleVak.vulVak("$f@");
		    		return;
		    	}
		    	else
			    {	if(checkboxen[regelnummer].isSelected() || docent)
			    	{	grafiekComponent.zetFunctie(regelnummer, e2, "$f" + expressieStrings[1] +"@", expressieStrings[0], domeinen[regelnummer], true, setState, docent);
			    		domeinButtons[regelnummer].setVisible(domeinInstelbaar);
			    	}
			    } 
		    }
		    else if(expressieStrings[0].equals(xAsNaam))
		    {	if(!verticaleLijnToegestaan)
		    	{	formuleVakken[regelnummer].formuleVak.vulVak("$f@");
		    		return;
		    	}
		    	if(checkboxen[regelnummer].isSelected())
		    		grafiekComponent.zetVerticaleLijn(regelnummer, e2);
		    }
		    else if(expressieStrings[0].startsWith(xAsNaam + "("))
		    {	if(!parametrisatieToegestaan)
		    	{	formuleVakken[regelnummer].formuleVak.vulVak("$f@");
		    		return;
		    	}
		    	if(soortVak[regelnummer] != PARAMETRISATIEX && soortVak[regelnummer] != PARAMETRISATIEY)
		    	{	soortVak[regelnummer] = PARAMETRISATIEX;
		    		if(regelnummer < maxAantalFormules - 1)
		    		{	soortVak[regelnummer + 1] = PARAMETRISATIEY;
		    			maakParametrisatieVak(regelnummer);
		    		}
		    		//layoutVakken(setState);
		    	}
		    	if(checkboxen[regelnummer].isSelected())
		    	{	String variabele = "";
		    		try{
		    			variabele = expressieStrings[0].substring(expressieStrings[0].indexOf("(") + 1, expressieStrings[0].indexOf(")"));
		    		}
		    		catch(Exception e){}
		    		grafiekComponent.zetParametrisatie(regelnummer, e2, variabele, true);
		    	}
		    	//maar wat moet er gebeuren/hoe moet dat eruit zien met extra regel voor y?? Ik wil het liefst dat dit in dezelfde
		    	//formuleregel gebeurt. Andere optie is dat het wel in de volgende regel gebeurt; dan neem ik ze samen. 
		    	//Misschien is het een idee om meer dan 9 regels mogelijk te maken, die grens is vrij willekeurig. 
		    	//Wat gebeurt er bijvoorbeeld als ik die naar 20 leg?
		    	//Heb ik een PARAMETRISATIEX en PARAMETRISATIEY nodig?
		    	
		    }
		    else if(expressieStrings[0].startsWith(yAsNaam + "("))
		    {
		    	if(!parametrisatieToegestaan || soortVak[regelnummer] != PARAMETRISATIEY)
		    	{
		    		formuleVakken[regelnummer].formuleVak.vulVak("$F@");
		    		return;
		    	}
		    	if(checkboxen[regelnummer].isSelected())
		    	{	String variabele = "";
		    		try{
		    			variabele = expressieStrings[0].substring(expressieStrings[0].indexOf("(")+1, expressieStrings[0].indexOf(")"));
		    		}
		    		catch(Exception e){}
		    		grafiekComponent.zetParametrisatie(regelnummer, e2, variabele, false);
	    	
		    		
		    	}
		    }
		    else
		    	formuleVakken[regelnummer].formuleVak.vulVak("$f@");
		}
		catch(Exception e)
		{}
		zetEnOfKnoppen();
		
	}
	
	public void maakParametrisatieVak(int regelnummer)
	{
		int yPositie = formuleVakken[regelnummer].getY();
		checkboxen[regelnummer].setLocation(4, yPositie + formuleVakken[regelnummer].getSize().height);
		if(regelnummer < maxAantalFormules - 1)
		{	AccoladeLabel accoladeLabel = new AccoladeLabel(formuleVakken[regelnummer].getSize().height + 10  + formuleVakken[regelnummer + 1].getSize().height);
			accoladeLabel.setLocation(15, yPositie);
			add(accoladeLabel);
		}
		
		//checken of alles al in parametrisatiestand staat. Anders daarvoor zorgen. Maar in principe moet ook alles uit parametrisatiestand
				//aan begin van parsen... Dus moet dit altijd gebeuren. Bij het uit parametrisatiestand halen goed opletten dat je de formule wel netjes bewaart.
				//laat de regel ook maar zichtbaar.
				//accolade neerzetten
				//volgende regel zichtbaar maken (als die niet al zichtbaar is).
				//checkboxen weghalen of onzichtbaar maken (waarschijnlijk is dat laatste handiger)
				//nieuwe checkbox terugzetten midden voor de accolade.
				//yasnaam(variabele) klaarzetten op volgende regel, als die niet al een parametrisatie bevatte.
				//Uberhaubt: volgende regel ook meteen zichtbaar maken.
	}
	
	
	/*
	public void maakParametrisatieVak(int regelnummer)
	{
		soortVak[regelnummer] = PARAMETRISATIEX;
		if(regelnummer == maxAantalFormules - 1)
			return;
		
		soortVak[regelnummer + 1] = PARAMETRISATIEY;
		
		JLabel accoladeLabel = new JLabel("{");
		accoladeLabel.setFont(new Font("SansSerif", Font.PLAIN, 32));
		accoladeLabel.setSize(20, 40);
		accoladeLabel.setLocation(20, 10);
		add(accoladeLabel);
	}
	*/
	
	public void zetEnOfKnoppen()
	{	for(int i = 0; i < maxAantalFormules - 1; i++)
		{	if(i < enOfKnoppen.length)
			{
				if(soortVak[i] == ONGELIJKHEID && soortVak[i+1] == ONGELIJKHEID)
					enOfKnoppen[i].setVisible(true);
				else
					enOfKnoppen[i].setVisible(false);
			}
		}
		
	}
	
	public void vulFunctieRegel(String deel1, String deel2, int regelnummer)
	{	//if(functieBeginAanpasbaar)
		//	formuleVakken[regelnummer].functieBeginVak.vulVak("$f" + deel1 + vergelijkingsTeken + deel2 + "@");
		formuleVakken[regelnummer].functieBeginVak.vulVak("$f" + deel1 + "=@");
		formuleVakken[regelnummer].formuleVak.vulVak("$f" + deel2 + "@");
	}
	
	public void maakNieuweRegel()
	{
		parseFormule(aantalRegels - 1, false);
		add(formuleVakken[aantalRegels],0);
		zetVoorvoegsel(aantalRegels);	
		if(docent || (grafiekComponent == null || grafiekComponent.typeOpdracht == GraphToolInteractiePanel.GEENOPDRACHT))
			add(checkboxen[aantalRegels],0);
		else
			checkboxen[aantalRegels].setSelected(true);
		add(domeinButtons[aantalRegels],0);
		domeinButtons[aantalRegels].setVisible(false);
		add(enOfKnoppen[aantalRegels - 1], 0);
		enOfKnoppen[aantalRegels - 1].setVisible(false);
		layoutVakken(false);
		formuleVakken[aantalRegels].formuleVak.requestFocus();
		aantalRegels++;
		produceAction("regel meer");
	}
	
	public void zetVergelijking(int regelNr, String vergelijkingString)
	{
		//zetFunctieBeginAanpasbaar(false, false);
		//zetFormeleFuncties(false, false);
		VergelijkingMeerv v = FormuleParser.parseVergelijking(vergelijkingString);
		String functieString0 = "$f"+v.geefVergelijking(0).geefExpLinks().toString()+"@";
		if(functieBeginAanpasbaar)
			functieString0 = "$fy=" + functieString0.substring(2);
		formuleVakken[0].formuleVak.vulVak(functieString0);
		if(aantalRegels<2) maakNieuweRegel();
		String functieString1 = "$f"+v.geefVergelijking(0).geefExpRechts().toString()+"@";
		if(functieBeginAanpasbaar)
			functieString1 = "$fy=" + functieString1.substring(2);
		formuleVakken[1].formuleVak.vulVak(functieString1);
		checkboxen[0].setSelected(true);
		checkboxen[1].setSelected(true);
		parseFormule(0, false);
		parseFormule(1, false);
	}
	
	public void zetFunctie(int regelNr, String functieString)
	{
		if(functieBeginAanpasbaar)
			functieString = "$fy=" + functieString.substring(2);
		if(regelNr==0) 
			formuleVakken[0].formuleVak.vulVak(functieString);
		while(aantalRegels-1<regelNr) 
			maakNieuweRegel();
		formuleVakken[regelNr].formuleVak.vulVak(functieString);
		checkboxen[regelNr].setSelected(true);
		parseFormule(regelNr, false);
	}
	
	public void zetFuncties(Map map)
	{
		String numberString = (String)map.get("number");
		int number = 0;
		try	{	
			number = Integer.parseInt(numberString);
		}
		catch (NumberFormatException nfe) {
			System.out.println(nfe.toString());
		}
		String clear = (String)map.get("clear");
		String abscissa_name = (String)map.get("abscissa_name");
		String abscissa_min = (String)map.get("abscissa_min");
		String abscissa_max = (String)map.get("abscissa_max");
		String ordinate_name = (String)map.get("ordinate_name");
		String ordinate_min = (String)map.get("ordinate_min");
		String ordinate_max = (String)map.get("ordinate_max");
		
		zetXAsNaam(abscissa_name,false);
		zetYAsNaam(ordinate_name,false);
		
		//Expressie[] functions = new Expressie[number];
		//Color[] colors = null;
		//double[] thicknesses = null;
		
		for(int i=0 ; i<number ; i++)
		{
			String functionString = (String)map.get("function_"+i);
			functionString = functionString.replaceAll("root", "sqrt");
			functionString = functionString.replaceAll("$", "");

			functionString = "$"+yAsNaam+"=" + functionString.substring(2);
			formuleVakken[i].formuleVak.vulVak(functionString);
			checkboxen[i].setSelected(true);
			parseFormule(i, false);
			if(i<number-1)
				maakNieuweRegel();
			//functions[i] = popcornParse(functionString);
			//String colorString = (String)map.get("color_"+i);
			//colors[i] = colorParse(colorString);
			//String thicknessString = (String)map.get("thickness_"+i);
			//try	{	
			//	thicknesses[i] = Double.parseDouble(thicknessString);
			//}
			//catch (NumberFormatException nfe) {
			//	System.out.println(nfe.toString());
			//}
			
		}
	}
	
	public Expressie popcornParse(String s)
	{	Expressie e = null;
		s = s.replaceAll("root", "sqrt");
		s = s.replaceAll("$", "");
		e = FormuleParser.parse("$f"+s+"@");
		return e;
	}
	
	public Color colorParse(String s)
	{
		Color c = null;
		
		return c;
	}
	
	public void actionPerformed(ActionEvent e)
	{	
		if (e.getSource() == nieuweRegelKnop && aantalRegels < maxAantalFormules)
		{	maakNieuweRegel();
			return;
		}
		else if (e.getSource() == nieuweRegelKnop)
			return;
		else if (e.getSource() == verwijderRegelKnop && aantalRegels > 1)
		{	
			parseFormule("$f@", aantalRegels - 1, false);
			//formuleVakken[aantalRegels - 1].formuleVak.vulVak("$f@");
			if(functieBeginZichtbaar)
			{	zetVoorvoegsel(aantalRegels - 1);
			}
			//else
			//	formuleVakken[aantalRegels - 1].formuleVak.vulVak("$f@");
			remove(formuleVakken[aantalRegels-1]);
			remove(checkboxen[aantalRegels-1]);
			if(!docent && grafiekComponent != null && grafiekComponent.typeOpdracht != GraphToolInteractiePanel.GEENOPDRACHT)
				checkboxen[aantalRegels-1].setSelected(false);
			remove(domeinButtons[aantalRegels-1]);
			remove(enOfKnoppen[aantalRegels-2]);
			isEn[aantalRegels - 2] = true;
			enOfKnoppen[aantalRegels-2].setText(GraphTool.rb.getString("enOfButton_En"));
			layoutVakken(false);
			aantalRegels--;
			produceAction("regel minder");
			return;
		}
		
		for(int i=0; i<maxAantalFormules; i++)
		{	if(e.getSource()==formuleVakken[i].formuleVak &&  (e.getActionCommand().equals("ingevuld") || 
					  e.getActionCommand().equals("focuslost")))
			{				
				parseFormule(i, false);
				if(checkboxen[i].isSelected())
				{	layoutVakken(false);
					produceAction("ingevuld");
				}
				break;
			}
			
			
		}
		for(int i=0 ; i<maxAantalFormules ; i++)
		{	if(e.getSource()==formuleVakken[i].formuleVak && 
			   e.getActionCommand().equals("focus"))
			{	if(formuleVak != formuleVakken[i].formuleVak)
				{	formuleVak.deSelect();
					Expressie exp = formuleVak.geefExpressie();
					actiefNummer = i;
					formuleVak = formuleVakken[i].formuleVak;
				}
				break;
			}
			
		}
		for(int i = 0; i<maxAantalFormules; i++)
		{	if(e.getSource()==enOfKnoppen[i])
			{	isEn[i] = !isEn[i];
				if(isEn[i])
					enOfKnoppen[i].setText(GraphTool.rb.getString("enOfButton_En"));
				else			
					enOfKnoppen[i].setText(GraphTool.rb.getString("enOfButton_Of"));
				parseFormule(i, false);
				break;
			}
			
		}
		if (checkboxen != null ) { 
			for(int i=0 ; i<maxAantalFormules ; i++) {	

				if(e.getSource()==checkboxen[i])
				{	parseFormule(i, false);
					if(checkboxen[i].isSelected())
					{	produceAction("ingevuld");
					}
					else 
					{	produceAction("verwijderd");
					}
					if(checkboxen[actiefNummer].isSelected())
					{	parseFormule(actiefNummer, false);
						produceAction("ingevuld");	
					}	
					break;
				}
			}
		}
	for(int i=0 ; i<maxAantalFormules ; i++)
		{	
		
		if(e.getSource()==domeinButtons[i] && e.getActionCommand().equals("maak Domein"))
			{	domeinStrings[i][0] = domeinButtons[i].getDomeinString()[0];
				domeinStrings[i][1] = domeinButtons[i].getDomeinString()[1];
				zetDomein(domeinStrings[i], i);
				parseFormule(i, false);
				
				produceAction("ingevuld");
			}
		}
		super.actionPerformed(e);
	}
	
	public void mousePressed(MouseEvent e) {	
		
		if (checkboxen != null) {
			for(int i = 0; i < aantalRegels; i++) {
				if(e.getSource().equals(checkboxen[i]) && (e.getModifiers() & e.BUTTON1_MASK) == 0) {
					Color kleur = JColorChooser.showDialog(this, GraphTool.rb.getString("kleurKiezer"), grafiekComponent.getFormuleColor(i));//new Color(255,255,180));
					grafiekComponent.setColor(i,  kleur, false);
					zetGrafiekKleuren();
				}
			}
		}
		
		for(int i = 0; i < aantalRegels; i++)
		{	int yMin = formuleVakken[i].getLocation().y;
			int yMax = formuleVakken[i].getLocation().y + 
					   formuleVakken[i].getSize().height+10;
			if(e.getY() > yMin && e.getY() < yMax)
			{	formuleVak.deSelect();
				parseFormule(actiefNummer, false);
				if(checkboxen[actiefNummer].isSelected())
				{	produceAction("ingevuld");		
				}	
				actiefNummer = i;
				formuleVak = formuleVakken[i].formuleVak;
				formuleVak.requestFocus();
				formuleVak.zetOpEind();
				break;
			}
		}
	}
	
	public void focusGained(FocusEvent e)
    {   
	}
	public void focusLost(FocusEvent e)
	{   parseFormule(actiefNummer, false);
		
		if(checkboxen[actiefNummer].isSelected())
		{	produceAction("ingevuld");		
		}
		produceAction("focusLost");
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
	
 	class AccoladeLabel extends JLabel
 	{
 		int hoogte;
 		
 		public AccoladeLabel(int height)
 		{
 			super();
 			hoogte = height;
 			setSize(7, hoogte);
 		}
 		
 		public void paintComponent(Graphics g)
 		{
 			g.setColor(Color.DARK_GRAY);
 			g.drawLine(4, 1, 5, 1);
 			g.drawLine(3, 2, 3, (hoogte - 1)/2 - 1);
 			g.drawLine(3, (hoogte - 1)/2 - 1, 1, (hoogte - 1)/2 + 1);
 			g.drawLine(1, (hoogte - 1)/2 + 1, 3, (hoogte - 1)/2 + 3);
 			g.drawLine(3, (hoogte - 1)/2 + 3, 3, hoogte - 2);
 			g.drawLine(4, hoogte - 1, 5, hoogte - 1);
 		}
 	}
}
