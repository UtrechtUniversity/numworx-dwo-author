package fi.wiskopdr;

import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;

import javax.swing.*;

import fi.wiskopdr.formuleobjects.*;
import fi.wiskopdr.expressies.*;

public class FunctieEditor extends FormuleEditor implements FocusListener
{
	private VergelijkingVak[] functieVakken;
	private JCheckBox[] checkboxen;
	private GrafiekComponent grafiekComponent;
	private TabelComponent tabelComponent;
	private int maxAantalFuncties=9;
	private int aantalRegels=1;
	private String voorbeeld;
	private static Image GOEDKRUL,FOUTKRUIS;
	private ImageComponent goedIC, foutIC;
	private int actiefNummer;
	
	//private FormuleButton nieuweRegelKnop;
	//private FormuleButton verwijderRegelKnop;
	
	private String varNaam = "x";
	private String yNaam = "y";
	String[] namen = {"f","g","h","i","j","k","l","m","n"};
	private boolean formalFunction = true;
	private Color[] colors;
	
	private Expressie laatsteExp;
	private int formuleX = 30;
	private boolean grafiekKleuren = true;
//	private String laatsteExpStr;
		
	public FunctieEditor(boolean b)
	{	super(b);
		zetGrafiekOfEdit(true);
		addFocusListener(this);
		remove(formuleVak);
		formuleVak.removeActionListener(this);
		setScrollHorizontal(false);
		
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
		goedIC = new ImageComponent(GOEDKRUL);
		foutIC = new ImageComponent(FOUTKRUIS);
		
		//headerPanel.remove(tabletButton);
		
		ndewortelKnop = new FormuleButton("ndewortel");
		ndewortelKnop.setBounds(112,2,20,20);
		ndewortelKnop.addActionListener(this);
		//zetOpBalk(ndewortelKnop);
		
		ndelogKnop = new FormuleButton("ndelog");
		ndelogKnop.setBounds(156,2,25,20);
		ndelogKnop.addActionListener(this);
		//zetOpBalk(ndelogKnop);
		
		absKnop = new FormuleButton("abs");
		absKnop.setBounds(134,2,20,20);
		absKnop.addActionListener(this);
		//zetOpBalk(absKnop);
		
		/*nieuweRegelKnop = new FormuleButton("gelijkwaardig");
		nieuweRegelKnop.setBounds(160,2,20,20);
		nieuweRegelKnop.addActionListener(this);
		zetOpBalk(nieuweRegelKnop);
		
		verwijderRegelKnop = new FormuleButton("terug");
		verwijderRegelKnop.setBounds(180,2,20,20);
		verwijderRegelKnop.addActionListener(this);
		zetOpBalk(verwijderRegelKnop);*/
	}

	public void setEditable(boolean b)
	{	functieVakken[0].formuleVak2.setEditable(b);	
	}
	
	public void zetMaxAantalFuncties(int num)
	{	maxAantalFuncties = num;
		boolean knoppenNodig = maxAantalFuncties>1;
		nieuweRegelKnop.setVisible(knoppenNodig);
		verwijderRegelKnop.setVisible(knoppenNodig);
		checkboxen[0].setVisible(knoppenNodig);
		formuleX = knoppenNodig ? 30 : 10;
		layoutVakken();
	}
	
	public void zetVarNaam(String s)
	{	varNaam = s;
		zetVoorvoegsel();
		if (grafiekComponent != null)
			grafiekComponent.zetVarNaam(varNaam);
	}
	
	public void zetYAsLabel(String s)
	{	yNaam = s;
		zetVoorvoegsel();
		if (grafiekComponent != null)
			grafiekComponent.zetYAsLabel(yNaam);
	}
	
	public void zetFormalFunction(boolean b)
	{	formalFunction = b;
		zetVoorvoegsel();
		updateTabelNames();	
	}
	
	public void zetVoorvoegsel()
	{	for(int i=0 ; i<maxAantalFuncties ; i++)
		{	if(formalFunction)functieVakken[i].formuleVak1.vulVak("$f"+namen[i]+"(" + varNaam + ")@");
			else if(aantalRegels>1)functieVakken[i].formuleVak1.vulVak("$f"+yNaam+"$s"+(i+1)+"@@");
			else functieVakken[i].formuleVak1.vulVak("$f"+yNaam+"@");
		}
	}
	
	public void layoutVakken()
	{	int hoogte = 10;
		for(int i=0 ; i<maxAantalFuncties ; i++)
		{	if(functieVakken[i]!=null)
			{	functieVakken[i].setLocation(formuleX,hoogte);
				if(checkboxen!=null && checkboxen[i]!=null)checkboxen[i].setLocation(4,hoogte+functieVakken[i].ashoogte-5);
				hoogte = hoogte + functieVakken[i].getSize().height + 10;
			}
		}
		repaint();
	}
	
	public void zetGrafiekComponent(GrafiekComponent gc)
	{	grafiekComponent = gc;
	}
	
	public void zetTabelComponent(TabelComponent tc)
	{	tabelComponent = tc;
	}
	
	// voor de docent-functie-editor
	// aanroepen NA zetFuncties()
	public void zetExpressie(Expressie exp)
	{	String expressieString = //exp.toString();
			"$f" + exp.toString() + "@";
//System.out.println("zet = " + expressieString);			
		functieVakken[0].formuleVak2.vulVak(expressieString);
		add(functieVakken[0],0);
		functieVakken[0].setVisible(true);
		checkboxen[0].setSelected(true);
		add(checkboxen[0],0);
		if(maxAantalFuncties>1)checkboxen[0].setVisible(true);
	}
	
	public Hashtable getState()
	{	String[] expressieStrings = null;
		boolean[] geselecteerd = null;
		String varNaam = "x";
		String yNaam = "y";		
		
		expressieStrings = new String[maxAantalFuncties];
		geselecteerd = new boolean[maxAantalFuncties];
		for(int i=0 ; i<maxAantalFuncties ; i++)
		{	expressieStrings[i] = functieVakken[i].formuleVak2.toString();
			geselecteerd[i] = checkboxen[i].isSelected();
		}		
		varNaam = this.varNaam;
		yNaam = this.yNaam;
		
		Hashtable h = grafiekComponent.getState();
	    h.put("expressieStrings", expressieStrings);
	    h.put("geselecteerd", geselecteerd);
	    h.put("varNaam", varNaam);
	    h.put("yNaam", yNaam);
	    return h;
	}
	
	public Hashtable getEditState()
	{	String[] expressieStrings = null;
		boolean[] geselecteerd = null;
		String varNaam = "x";
		String yNaam = "y";
		boolean formalFunction = true;
		
		expressieStrings = new String[maxAantalFuncties];
		geselecteerd = new boolean[maxAantalFuncties];
		for(int i=0 ; i<maxAantalFuncties ; i++)
		{	expressieStrings[i] = functieVakken[i].formuleVak2.toString();
			geselecteerd[i] = checkboxen[i].isSelected();
		}		
		
		varNaam = this.varNaam;
		yNaam = this.yNaam;
		formalFunction = this.formalFunction;
				
		Hashtable h = grafiekComponent.getState();
	    h.put("expressieStrings", expressieStrings);
	    h.put("geselecteerd", geselecteerd);
	    h.put("varNaam", varNaam);
	    h.put("yNaam", yNaam);
	    h.put("formalFunction", new Boolean(formalFunction));
	    return h;
	}
	
	

	public void zetOpdracht(Hashtable h, String[] randomVars, Hashtable randomValues)
	{	String[] expressieStrings = null;
    	boolean[] geselecteerd = null;
    	String varNaam = "x";
    	String yNaam = "y";
    	boolean formalFunction = true;
    	boolean grafiekKleuren = true;
    
    	if(h.containsKey("expressieStrings")) 
    		expressieStrings = (String[])h.get("expressieStrings");
    	if(h.containsKey("geselecteerd")) 
    		geselecteerd = (boolean[])h.get("geselecteerd");
    	if(h.containsKey("varNaam")) 
    		varNaam = (String)h.get("varNaam");
    	if(h.containsKey("yNaam")) 
    		yNaam = (String)h.get("yNaam");
    	if(h.containsKey("formalFunction")) 
    		formalFunction = ((Boolean)h.get("formalFunction")).booleanValue();
    	if(h.containsKey("grafiekKleuren")) 
    		grafiekKleuren = ((Boolean)h.get("grafiekKleuren")).booleanValue();
    	
    	this.varNaam = varNaam;
    	this.yNaam = yNaam;
    	this.formalFunction = formalFunction;
    	this.grafiekKleuren = grafiekKleuren;
		grafiekComponent.zetVarNaam(varNaam);
    	
    	if(expressieStrings==null) 
    		return;
    	maxAantalFuncties = expressieStrings.length;
		//expressies = new Expressie[aantalExpressies];
		for(int i=0 ; i<maxAantalFuncties ; i++)
		{	
			if(grafiekKleuren)functieVakken[i].setFGColor(colors[i]);
			else functieVakken[i].setFGColor(Color.black);
			
			if(formalFunction) 
				functieVakken[i].formuleVak1.vulVak("$f"+namen[i]+"(" + varNaam + ")@");
			else if(aantalRegels>1)
				functieVakken[i].formuleVak1.vulVak("$f"+yNaam+"$s"+(i+1)+"@@");
			else 
				functieVakken[i].formuleVak1.vulVak("$f"+yNaam+"@");
			if(!expressieStrings[i].equals("$f@"))
			{	
    			try			
    			{	expressieStrings[i] = FormuleParser.randomizeString(expressieStrings[i],randomVars,randomValues);
    			}
    			catch(Exception e)
    			{	expressieStrings[i] = "$f???@";
    				this.zetRandomFout(true);
    			}
						
    			functieVakken[i].formuleVak2.vulVak(expressieStrings[i]);

    			if(geselecteerd[i]) 
    			{	grafiekComponent.zetExpressie(i,functieVakken[i].formuleVak2.geefExpressie());
    				if (tabelComponent != null)
						tabelComponent.zetExpressie(
							i,functieVakken[i].formuleVak2.geefExpressie(),null, false);
    			}


    			add(functieVakken[i],0);
    			functieVakken[i].setVisible(true);
				if(geselecteerd!=null)
					checkboxen[i].setSelected(geselecteerd[i]);
				checkboxen[i].setVisible(true);
				add(checkboxen[i],0);
				aantalRegels = i+1;
				
			}
			
		}
		layoutVakken();		
		
		if(maxAantalFuncties < 9)
			functieVakken[maxAantalFuncties].setVisible(true);
		grafiekComponent.setState(h);
		grafiekComponent.repaint();
		
		if (tabelComponent != null)
		{	updateTabelNames();
			tabelComponent.updateExpressieList();
		}
		
    }
	
	public void setEditState(Hashtable h)
	{	String[] expressieStrings = null;
    	boolean[] geselecteerd = null;
    	String varNaam = "x";
    	String yNaam = "y";
    	boolean formalFunction = true;
    	boolean grafiekKleuren = true;
    
    	if(h.containsKey("expressieStrings")) 
    		expressieStrings = (String[])h.get("expressieStrings");
    	if(h.containsKey("geselecteerd")) 
    		geselecteerd = (boolean[])h.get("geselecteerd");
    	if(h.containsKey("varNaam")) 
    		varNaam = (String)h.get("varNaam");
    	if(h.containsKey("yNaam")) 
    		yNaam = (String)h.get("yNaam");
    	if(h.containsKey("formalFunction")) 
    		formalFunction = ((Boolean)h.get("formalFunction")).booleanValue();
    	if(h.containsKey("grafiekKleuren")) 
    		grafiekKleuren = ((Boolean)h.get("grafiekKleuren")).booleanValue();
    	
    	this.varNaam = varNaam;
    	this.yNaam = yNaam;
    	this.formalFunction = formalFunction;
    	this.grafiekKleuren = grafiekKleuren;
		grafiekComponent.zetVarNaam(varNaam);
    	
    	if(expressieStrings==null) 
    		return;
    	maxAantalFuncties = expressieStrings.length;
		for(int i=0 ; i<maxAantalFuncties ; i++)
		{	if(grafiekKleuren)functieVakken[i].setFGColor(colors[i]);
			else functieVakken[i].setFGColor(Color.black);
			
			if(formalFunction) 
				functieVakken[i].formuleVak1.vulVak("$f"+namen[i]+"(" + varNaam + ")@");
			else if(aantalRegels>1)
				functieVakken[i].formuleVak1.vulVak("$f"+yNaam+"$s"+(i+1)+"@@");
			else 
				functieVakken[i].formuleVak1.vulVak("$f"+yNaam+"@");
			if(!expressieStrings[i].equals("$f@"))
			{	functieVakken[i].formuleVak2.vulVak(expressieStrings[i]);

				if(geselecteerd[i]) 
				{	grafiekComponent.zetExpressie(i,functieVakken[i].formuleVak2.geefExpressie());
					if (tabelComponent != null)
						tabelComponent.zetExpressie(
							i,functieVakken[i].formuleVak2.geefExpressie(),null, false);
				}	
				add(functieVakken[i],0);
				functieVakken[i].setVisible(true);
				if(geselecteerd!=null)
					checkboxen[i].setSelected(geselecteerd[i]);
				checkboxen[i].setVisible(true);
				add(checkboxen[i],0);
				aantalRegels = i+1;
			}
		}
		layoutVakken();	
		if(maxAantalFuncties < 9)
			functieVakken[maxAantalFuncties].setVisible(true);
		grafiekComponent.setState(h);
		grafiekComponent.repaint();
		
		if (tabelComponent != null)
		{	updateTabelNames();
			tabelComponent.updateExpressieList();
		}

		
    }
	
    public void setState(Hashtable h)
    {	String[] expressieStrings = null;
    	boolean[] geselecteerd = null;
    	String varNaam = this.varNaam;
		String yNaam = this.yNaam;
		
    	if(h.containsKey("expressieStrings")) 
    		expressieStrings = (String[])h.get("expressieStrings");
    	if(h.containsKey("geselecteerd")) 
    		geselecteerd = (boolean[])h.get("geselecteerd");
    	if(h.containsKey("varNaam")) varNaam = (String)h.get("varNaam");
    	if(h.containsKey("yNaam")) yNaam = (String)h.get("yNaam");
    	
    	this.varNaam = varNaam;
    	this.yNaam = yNaam;
    	
    	if(expressieStrings==null) 
    		return;
    	maxAantalFuncties = expressieStrings.length;
		//expressies = new Expressie[aantalExpressies];
		for(int i=0 ; i<maxAantalFuncties ; i++)
		{	if(!expressieStrings[i].equals("$f@"))
			{	functieVakken[i].formuleVak2.vulVak(expressieStrings[i]);

				if(geselecteerd[i]) 
				{	grafiekComponent.zetExpressie(i,functieVakken[i].formuleVak2.geefExpressie());
					if (tabelComponent != null)
						tabelComponent.zetExpressie(
							i,functieVakken[i].formuleVak2.geefExpressie(), null,false);
				}

				add(functieVakken[i],0);
				functieVakken[i].setVisible(true);
				if(geselecteerd!=null)
					checkboxen[i].setSelected(geselecteerd[i]);
				add(checkboxen[i],0);
				checkboxen[i].setVisible(true);
				
				aantalRegels = i+1;
			}
		}
		if(maxAantalFuncties < 9)functieVakken[maxAantalFuncties].setVisible(true);
		grafiekComponent.setState(h);
		grafiekComponent.repaint();
		
		if (tabelComponent != null)
		{	updateTabelNames();
			tabelComponent.updateExpressieList();
		}

		zetMaxAantalFuncties(maxAantalFuncties);
    }
    
	public void zetFuncties()
	{	maxAantalFuncties = 9; 
		functieVakken = new VergelijkingVak[maxAantalFuncties];
		
		for(int i=0 ; i<maxAantalFuncties ; i++)
		{	functieVakken[i] = new VergelijkingVak();
			functieVakken[i].setFont(WiskOpdr.formuleFont0);
			functieVakken[i].setLocation(formuleX,10 + 35*i);
			functieVakken[i].setOpaque(false);
			if(grafiekKleuren)functieVakken[i].setFGColor(colors[i]);
			else functieVakken[i].setFGColor(Color.black);
			
			//add(functieVakken[i],0);
			//functieVakken[i].setVisible(false);
			if (formalFunction) 
				functieVakken[i].formuleVak1.vulVak("$f"+namen[i]+"(" + varNaam + ")@");
			else if (aantalRegels > 1)
				functieVakken[i].formuleVak1.vulVak("$f"+yNaam+"$s"+(i+1)+"@@");
			else 
				functieVakken[i].formuleVak1.vulVak("$f"+yNaam+"@");
			functieVakken[i].formuleVak1.setEditable(false);
			functieVakken[i].formuleVak1.setSelectable(false);
			functieVakken[i].formuleVak2.addActionListener(this);
		}
		add(functieVakken[0],0);
		
		checkboxen = new JCheckBox[maxAantalFuncties];
		for(int i=0 ; i<maxAantalFuncties ; i++)
		{	checkboxen[i] = new JCheckBox();
			checkboxen[i].setBounds(4,12 + 35*i, 17, 17);
			checkboxen[i].setOpaque(false);
			checkboxen[i].addActionListener(this);
			//add(checkboxen[i]);
			//checkboxen[i].setVisible(false);
		}
		add(checkboxen[0]);
		
		functieVakken[0].setVisible(true);
		formuleVak = functieVakken[0].formuleVak2;
		formuleVak.requestFocus();
		actiefNummer = 0;
		
		checkboxen[0].setVisible(true);	
		checkboxen[0].setSelected(true);
	}
	
	public static void zetPlaatjes(Image gk, Image fk)
	{	GOEDKRUL = gk;
		FOUTKRUIS = fk;
	}
	
	public FormuleVak geefFormuleVak()
	{	return formuleVak;
	}
	
	public Expressie geefVoorbeeld()
	{	FormuleParser p = new FormuleParser();
		return p.parse(p.schoon(voorbeeld));
	}
	
	public Expressie geefExpressie()
	{	FormuleParser p = new FormuleParser();
		return formuleVak.geefExpressie();
	}
	
	public Expressie geefLaatsteExp()
	{	return laatsteExp;
	}
	
	/*public boolean evalueer()
	{	
		FormuleParser p = new FormuleParser();
		if(formuleVak.geefExpressie()!=null && p.parse(p.schoon(voorbeeld)).isGelijkwaardig(formuleVak.geefExpressie()))
		{	remove(foutIC);
			goedIC.setLocation(getSize().width-40,10);
			add(goedIC);
			
			return true;
		}
		remove(goedIC);
		foutIC.setLocation(getSize().width-40,10);
		add(foutIC);
		return false;
	}*/
	
	// functienotatie of niet
	// regel erbij (y -> y1) of regel eraf (y1 -> y)	 
	// ook bij set(Edit)State en zetOdracht
	public void updateTabelNames()
	{	if (tabelComponent == null)
			return;
		for (int i = 0; i < maxAantalFuncties; i++)
		{	String expNaam = "";
			if (formalFunction)
				expNaam = namen[i] + "(" + varNaam + ")";
			else if (aantalRegels > 1)
				expNaam = yNaam + (i + 1);
			else
				expNaam = yNaam;	
			// alleen expressies != null
			// i.e. geselecteerd en ingevuld	
			if (tabelComponent.getExpressie(i) != null)
				tabelComponent.zetExpressieNaam(i, expNaam);	
		}
		if (tabelComponent != null)
		{	tabelComponent.updateExpressieList();
		}
	}
	
	public void terugNaarEenRegel()
	{	for (int rCnt = aantalRegels; rCnt > 1; rCnt--)
		{
			grafiekComponent.zetExpressie(aantalRegels - 1, null);

			if (tabelComponent != null)
			{	tabelComponent.zetExpressie(aantalRegels - 1, null, null, true);
			}
//System.out.println("gc vrknop");							
			functieVakken[aantalRegels-1].formuleVak2.vulVak("$f@");
			remove(functieVakken[aantalRegels-1]);
			remove(checkboxen[aantalRegels-1]);
			layoutVakken();
			aantalRegels--;
			for(int i=0 ; i<maxAantalFuncties ; i++)
			{	if(formalFunction)
					functieVakken[i].formuleVak1.vulVak("$f"+namen[i]+"(" + varNaam + ")@");
				else if(aantalRegels>1)
					functieVakken[i].formuleVak1.vulVak("$f"+yNaam+"$s"+(i+1)+"@@");
				else 
					functieVakken[i].formuleVak1.vulVak("$f"+yNaam+"@");
			}
		
		}
		functieVakken[0].formuleVak2.vulVak("$f@");
		grafiekComponent.zetExpressie(0, null);
		if (tabelComponent != null)
			tabelComponent.zetExpressie(0, null, null, true);
		
	}
	public void actionPerformed(ActionEvent e)
	{	
		if (e.getSource() == nieuweRegelKnop && 
			aantalRegels < maxAantalFuncties)
		{	if(checkboxen[aantalRegels - 1].isSelected())
			{	grafiekComponent.zetExpressie(aantalRegels - 1,	functieVakken[aantalRegels-1].formuleVak2.geefExpressie());
				// kijk of gelinkt aan een TabelComponent	
				if (tabelComponent != null)
				{	String expNaam = "";
					if (formalFunction)	expNaam = namen[aantalRegels - 1] + "(" + varNaam + ")";
					else if (aantalRegels > 1)expNaam = yNaam + aantalRegels;
					else expNaam = yNaam;	
					tabelComponent.zetExpressie(aantalRegels - 1,functieVakken[aantalRegels-1].formuleVak2.geefExpressie(),	expNaam, true);		
				}	
//System.out.println("gc nrknop");					
			}	
			add(functieVakken[aantalRegels],0);
			add(checkboxen[aantalRegels],0);
			layoutVakken();
			functieVakken[aantalRegels].formuleVak2.requestFocus();
			aantalRegels++;
			for(int i= 0; i < maxAantalFuncties; i++)
			{	if(formalFunction)
					functieVakken[i].formuleVak1.vulVak("$f"+namen[i]+"(" + varNaam + ")@");
				else if(aantalRegels > 1)
					functieVakken[i].formuleVak1.vulVak("$f"+yNaam+"$s"+(i+1)+"@@");
				else 
					functieVakken[i].formuleVak1.vulVak("$f"+yNaam+"@");
			}
			return;
		}
		else if (e.getSource() == verwijderRegelKnop && aantalRegels > 1)
		{	grafiekComponent.zetExpressie(aantalRegels - 1, null);

			if (tabelComponent != null)
			{	tabelComponent.zetExpressie(aantalRegels - 1, null, null, true);
			}
//System.out.println("gc vrknop");							
			functieVakken[aantalRegels-1].formuleVak2.vulVak("$f@");
			remove(functieVakken[aantalRegels-1]);
			remove(checkboxen[aantalRegels-1]);
			layoutVakken();
			aantalRegels--;
			for(int i=0 ; i<maxAantalFuncties ; i++)
			{	if(formalFunction)
					functieVakken[i].formuleVak1.vulVak("$f"+namen[i]+"(" + varNaam + ")@");
				else if(aantalRegels>1)
					functieVakken[i].formuleVak1.vulVak("$f"+yNaam+"$s"+(i+1)+"@@");
				else 
					functieVakken[i].formuleVak1.vulVak("$f"+yNaam+"@");
			}
			
		}
		
		for(int i=0 ; i<maxAantalFuncties ; i++)
		{	if(e.getSource()==functieVakken[i].formuleVak2 && 
			  (e.getActionCommand().equals("ingevuld") || 
			   e.getActionCommand().equals("focuslost")))
			{	laatsteExp = functieVakken[i].formuleVak2.geefExpressie();
//				laatsteExpStr = functieVakken[i].formuleVak2.toString();
				if(checkboxen[i].isSelected())
				{	
					// bij docent-functie-editor niet aanwezig
					if (grafiekComponent != null)				
						grafiekComponent.zetExpressie(i, laatsteExp);
//System.out.println("fvak i check");						
					layoutVakken();
					if (tabelComponent != null)
					{	String expNaam = "";
						if (formalFunction)
							expNaam = namen[i] + "(" + varNaam + ")";
						else if (aantalRegels > 1)
							expNaam = yNaam + (i + 1);
						else
							expNaam = yNaam;	
						tabelComponent.zetExpressie(i, laatsteExp, expNaam, true);
					}
					produceAction("ingevuld");
				}
				/*if(exp!=null)
				{	functieVakken[i].formuleVak2.vulVak("$f" + exp.toString() + "@");
					if(i<maxAantalFuncties-1)// && !checkboxen[i+1].isVisible())
					{	add(checkboxen[i+1],0);
						add(functieVakken[i+1],0);
						layoutVakken();	
						//checkboxen[i+1].setVisible(true);
						//functieVakken[i+1].setVisible(true);
					}
				}*/
				
				break;
				
			}
			
			
		}
		for(int i=0 ; i<maxAantalFuncties ; i++)
		{	if(e.getSource()==functieVakken[i].formuleVak2 && 
			   e.getActionCommand().equals("focus"))
			{	if(formuleVak != functieVakken[i].formuleVak2)
				{	formuleVak.deSelect();
					Expressie exp = formuleVak.geefExpressie();
					//if(checkboxen[actiefNummer].isAan())grafiekComponent.zetExpressie(exp,actiefNummer);
					//if(exp!=null)
					//{	formuleVak.vulVak("$f" + exp.toString() + "@");
					//	if(actiefNummer<aantalFuncties-1 && !checkboxen[actiefNummer+1].isVisible())
					//	{	checkboxen[actiefNummer+1].setVisible(true);
					//		functieVakken[actiefNummer+1].setVisible(true);
					//	}
					//}
					actiefNummer = i;
					formuleVak = functieVakken[i].formuleVak2;
					
				}
				break;
			}
			
		}
		for(int i=0 ; i<maxAantalFuncties ; i++)
		{	if(e.getSource()==checkboxen[i])
			{	if(checkboxen[i].isSelected())
				{	laatsteExp = functieVakken[i].formuleVak2.geefExpressie();
//					laatsteExpStr = functieVakken[i].formuleVak2.toString();
					// bij docent-functie-editor niet aanwezig
					if (grafiekComponent != null)
						grafiekComponent.zetExpressie(i,laatsteExp);
					if (tabelComponent != null)
					{	String expNaam = "";
						if (formalFunction)
							expNaam = namen[i] + "(" + varNaam + ")";
						else if (aantalRegels > 1)
							expNaam = yNaam + (i+1);
						else
							expNaam = yNaam;	
						tabelComponent.zetExpressie(i, laatsteExp, expNaam, true);
					}
					produceAction("ingevuld");
				}
				else 
				{	if (grafiekComponent != null)	
						grafiekComponent.zetExpressie(i, null);
					if (tabelComponent != null)	
						tabelComponent.zetExpressie(i, null, null, true);
					produceAction("verwijderd");
				}
				
				laatsteExp = formuleVak.geefExpressie();
//				laatsteExpStr = formuleVak.toString();
				if(checkboxen[actiefNummer].isSelected())
				{	// bij docent-functie-editor niet aanwezig
					if (grafiekComponent != null)
						grafiekComponent.zetExpressie(actiefNummer,laatsteExp);
					if (tabelComponent != null)
					{	String expNaam = "";
						if (formalFunction)
							expNaam = namen[actiefNummer] + "(" + varNaam + ")";
						else if (aantalRegels > 1)
							expNaam = yNaam + (actiefNummer + 1);
						else
							expNaam = yNaam;
						tabelComponent.zetExpressie(actiefNummer, laatsteExp, expNaam, true);
					
					}
						
					produceAction("ingevuld");	
				}	
				if(laatsteExp!=null)
				{	//formuleVak.vulVak("$f" + exp.toString() + "@");
						
				}
				
				break;
			}
			
		}
		super.actionPerformed(e);
	}
	
	public void mousePressed(MouseEvent e)
	{	
		for(int i=0 ; i<aantalRegels ; i++)
		{	int yMin = functieVakken[i].getLocation().y;
			int yMax = functieVakken[i].getLocation().y + 
					   functieVakken[i].getSize().height+10;
			if(e.getY() > yMin && e.getY() < yMax)
			{	//if(formuleVak != functieVakken[i].formuleVak2)
				{	formuleVak.deSelect();
					laatsteExp = formuleVak.geefExpressie();
//					laatsteExpStr = formuleVak.toString();
					if(checkboxen[actiefNummer].isSelected())
					{	if (grafiekComponent != null)				
							grafiekComponent.zetExpressie(actiefNummer,laatsteExp);
//System.out.println("gc mousepressed");							
						if (tabelComponent != null)
						{	String expNaam = "";
							if (formalFunction)
								expNaam = namen[actiefNummer] + "(" + varNaam + ")";
							else if (aantalRegels > 1)
								expNaam = yNaam + (actiefNummer + 1);
							else
								expNaam = yNaam;
							tabelComponent.zetExpressie(actiefNummer, laatsteExp, expNaam, true);	
						}
			
						produceAction("ingevuld");		
					}	
					actiefNummer = i;
					formuleVak = functieVakken[i].formuleVak2;
					formuleVak.requestFocus();
					formuleVak.zetOpEind();
				}
				break;
			}
			
		}
		
	}
	
	public void focusGained(FocusEvent e)
    {   
	}
	public void focusLost(FocusEvent e)
	{   laatsteExp = formuleVak.geefExpressie();
//		laatsteExpStr = formuleVak.toString();
	
//System.out.println("focus lost");

		if(checkboxen[actiefNummer].isSelected())
		{	
			if (grafiekComponent != null)
				grafiekComponent.zetExpressie(actiefNummer,laatsteExp);
			
			if (tabelComponent != null)
			{	String expNaam = "";
				if (formalFunction)
					expNaam = namen[actiefNummer] + "(" + varNaam + ")";
				else if (aantalRegels > 1)
					expNaam = yNaam + (actiefNummer + 1); 
				else
					expNaam = yNaam;
				tabelComponent.zetExpressie(actiefNummer, laatsteExp, expNaam, true);	
			}
			produceAction("ingevuld");		
		}
		if (laatsteExp!=null)
		{	formuleVak.vulVak("$f" + laatsteExp.toString() + "@");
			produceAction("focusLost");
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
