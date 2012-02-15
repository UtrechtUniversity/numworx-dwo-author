package fi.grafiek3dtest;

import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;

import javax.swing.*;

import fi.grafiek3dtest.formuleobjects.*;
import fi.grafiek3dtest.expressies.*;


public class FunctieEditor extends FormuleEditor implements FocusListener
{
	private VergelijkingVak[] functieVakken;
	private JCheckBox[] checkboxen;
	private JPanel[] cbPanels;
	private GrafiekComponent grafiekComponent;
	Grafiek3DComponent grafiek3DComponent;
	private int maxAantalFuncties=0;
	private int aantalRegels=1;
	private String voorbeeld;
	private static Image GOEDKRUL,FOUTKRUIS;
	private ImageComponent goedIC, foutIC;
	private int actiefNummer;
	
	//private FormuleButton nieuweRegelKnop;
	//private FormuleButton verwijderRegelKnop;
	
	private String varNaam = "x";
	private String yNaam = "y";
	String[] namen = {"f","g","h","i","j","k","l","m","n","p"};
	private boolean formalFunction = true;
	private Color[] colors;
	
	int functieType = 0;
	String varNaamX = "x";
	String varNaamY = "y";
	String paramNaam = "t";
	String paramNaamU = "u";
	String paramNaamV = "v";
	String[] functieNamen = {"f","g","h","i","j","k","l","m","n","p"};
	String[] parametrisatieNamen = {"x","y","z"};
	
	
	
		
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
		
	}
	
	public void zetVoorvoegsel()
	{	for (int i = 0; i < maxAantalFuncties; i++)
		{	if (formalFunction)
				functieVakken[i].formuleVak1.vulVak("$f" + namen[i] + "(" + varNaam + ")@");
			else 
				if (aantalRegels > 1)
					functieVakken[i].formuleVak1.vulVak("$f" + yNaam + "$s" + (i + 1) + "@@");
			else 
				functieVakken[i].formuleVak1.vulVak("$f" + yNaam + "@");
		}
	}
	
	public void layoutVakken()
	{	int hoogte = 10;
		for(int i=0 ; i<maxAantalFuncties ; i++)
		{	if(functieVakken[i]!=null)
			{	functieVakken[i].setLocation(30,hoogte);
				if(cbPanels!=null && cbPanels[i]!=null)cbPanels[i].setLocation(4,hoogte+functieVakken[i].ashoogte-5);
				hoogte = hoogte + functieVakken[i].getSize().height + 10;
			}
		}
		repaint();
	}
	
	public void zetGrafiekComponent(GrafiekComponent gc)
	{	grafiekComponent = gc;
	}
	
	public void zetGrafiek3DComponent(Grafiek3DComponent g3dc)
	{	grafiek3DComponent = g3dc;
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
    
    	if(h.containsKey("expressieStrings")) expressieStrings = (String[])h.get("expressieStrings");
    	if(h.containsKey("geselecteerd")) geselecteerd = (boolean[])h.get("geselecteerd");
    	if(h.containsKey("varNaam")) varNaam = (String)h.get("varNaam");
    	if(h.containsKey("yNaam")) yNaam = (String)h.get("yNaam");
    	if(h.containsKey("formalFunction")) formalFunction = ((Boolean)h.get("formalFunction")).booleanValue();
    	
    	this.varNaam = varNaam;
    	this.yNaam = yNaam;
    	this.formalFunction = formalFunction;
		grafiekComponent.zetVarNaam(varNaam);
    	
    	if(expressieStrings==null) return;
    	maxAantalFuncties = expressieStrings.length;
		//expressies = new Expressie[aantalExpressies];
		for(int i=0 ; i<maxAantalFuncties ; i++)
		{	if(formalFunction) functieVakken[i].formuleVak1.vulVak("$f"+namen[i]+"(" + varNaam + ")@");
			else if(aantalRegels>1)functieVakken[i].formuleVak1.vulVak("$f"+yNaam+"$s"+(i+1)+"@@");
			else functieVakken[i].formuleVak1.vulVak("$f"+yNaam+"@");
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
    			if(geselecteerd[i]) grafiekComponent.zetExpressie(i,functieVakken[i].formuleVak2.geefExpressie());
    			add(functieVakken[i],0);
    			functieVakken[i].setVisible(true);
				if(geselecteerd!=null)checkboxen[i].setSelected(geselecteerd[i]);
				checkboxen[i].setVisible(true);
				add(cbPanels[i],0);
				aantalRegels = i+1;
				
			}
			
		}
		layoutVakken();		
		
		if(maxAantalFuncties < 9)functieVakken[maxAantalFuncties].setVisible(true);
		grafiekComponent.setState(h);
		grafiekComponent.repaint();
    }
	
	public void setEditState(Hashtable h)
	{	String[] expressieStrings = null;
    	boolean[] geselecteerd = null;
    	String varNaam = "x";
    	String yNaam = "y";
    	boolean formalFunction = true;
    
    	if(h.containsKey("expressieStrings")) expressieStrings = (String[])h.get("expressieStrings");
    	if(h.containsKey("geselecteerd")) geselecteerd = (boolean[])h.get("geselecteerd");
    	if(h.containsKey("varNaam")) varNaam = (String)h.get("varNaam");
    	if(h.containsKey("yNaam")) yNaam = (String)h.get("yNaam");
    	if(h.containsKey("formalFunction")) formalFunction = ((Boolean)h.get("formalFunction")).booleanValue();
    	
    	this.varNaam = varNaam;
    	this.yNaam = yNaam;
    	this.formalFunction = formalFunction;
		grafiekComponent.zetVarNaam(varNaam);
    	
    	if(expressieStrings==null) return;
    	maxAantalFuncties = expressieStrings.length;
		for(int i=0 ; i<maxAantalFuncties ; i++)
		{	if(formalFunction) functieVakken[i].formuleVak1.vulVak("$f"+namen[i]+"(" + varNaam + ")@");
			else if(aantalRegels>1)functieVakken[i].formuleVak1.vulVak("$f"+yNaam+"$s"+(i+1)+"@@");
			else functieVakken[i].formuleVak1.vulVak("$f"+yNaam+"@");
			if(!expressieStrings[i].equals("$f@"))
			{	functieVakken[i].formuleVak2.vulVak(expressieStrings[i]);
				if(geselecteerd[i]) grafiekComponent.zetExpressie(i,functieVakken[i].formuleVak2.geefExpressie());
				add(functieVakken[i],0);
				functieVakken[i].setVisible(true);
				if(geselecteerd!=null)checkboxen[i].setSelected(geselecteerd[i]);
				checkboxen[i].setVisible(true);
				add(cbPanels[i],0);
				aantalRegels = i+1;
			}
		}
		layoutVakken();	
		if(maxAantalFuncties < 9)functieVakken[maxAantalFuncties].setVisible(true);
		grafiekComponent.setState(h);
		grafiekComponent.repaint();
    }
	
    public void setState(Hashtable h)
    {	String[] expressieStrings = null;
    	boolean[] geselecteerd = null;
    	String varNaam = this.varNaam;
		String yNaam = this.yNaam;
		
		if(h.containsKey("expressieStrings")) expressieStrings = (String[])h.get("expressieStrings");
    	if(h.containsKey("geselecteerd")) geselecteerd = (boolean[])h.get("geselecteerd");
    	if(h.containsKey("varNaam")) varNaam = (String)h.get("varNaam");
    	if(h.containsKey("yNaam")) yNaam = (String)h.get("yNaam");
    	
    	this.varNaam = varNaam;
    	this.yNaam = yNaam;
    	
    	if(expressieStrings==null) return;
    	maxAantalFuncties = expressieStrings.length;
		//expressies = new Expressie[aantalExpressies];
		for(int i=0 ; i<maxAantalFuncties ; i++)
		{	if(formalFunction) functieVakken[i].formuleVak1.vulVak("$f"+namen[i]+"(" + varNaam + ")@");
			else if(aantalRegels>1)functieVakken[i].formuleVak1.vulVak("$f"+yNaam+"$s"+(i+1)+"@@");
			else functieVakken[i].formuleVak1.vulVak("$f"+yNaam+"@");
			if(!expressieStrings[i].equals("$f@"))
			{	functieVakken[i].formuleVak2.vulVak(expressieStrings[i]);
				if(geselecteerd[i]) grafiekComponent.zetExpressie(i,functieVakken[i].formuleVak2.geefExpressie());
				add(functieVakken[i],0);
				functieVakken[i].setVisible(true);
				if(geselecteerd!=null)checkboxen[i].setSelected(geselecteerd[i]);
				add(cbPanels[i],0);
				checkboxen[i].setVisible(true);
				
				aantalRegels = i+1;
			}
		}
		
		
		if(maxAantalFuncties < 9)functieVakken[maxAantalFuncties].setVisible(true);
		if (grafiekComponent != null)
		{	
			grafiekComponent.setState(h);
			grafiekComponent.zetVarNaam(varNaam);
			grafiekComponent.zetYAsLabel(yNaam);
			grafiekComponent.repaint();
		}
    }
    
	public void zetFuncties()
	{	maxAantalFuncties = 9; 
		functieVakken = new VergelijkingVak[maxAantalFuncties];
		
		for (int i = 0; i < maxAantalFuncties; i++)
		{	functieVakken[i] = new VergelijkingVak();
			functieVakken[i].setFont(Grafiek3DTest.formuleFont0);
			functieVakken[i].setLocation(30, 10 + 35 * i);
			functieVakken[i].setOpaque(false);
			//add(functieVakken[i],0);
			//functieVakken[i].setVisible(false);
			if (formalFunction) 
				functieVakken[i].formuleVak1.vulVak("$f" + namen[i] + "(" + varNaam + ")@");
			else if(aantalRegels > 1)
				functieVakken[i].formuleVak1.vulVak("$f" + yNaam + "$s" + (i + 1) + "@@");
			else 
				functieVakken[i].formuleVak1.vulVak("$f" + yNaam + "@");
			functieVakken[i].formuleVak1.setEditable(false);
			functieVakken[i].formuleVak1.setSelectable(false);
			functieVakken[i].formuleVak2.addActionListener(this);
		}
		add(functieVakken[0],0);
		
		checkboxen = new JCheckBox[maxAantalFuncties];
		cbPanels  = new JPanel[maxAantalFuncties];
		for(int i = 0; i < maxAantalFuncties; i++)
		{	checkboxen[i] = new JCheckBox();
			cbPanels[i] = new JPanel();
			cbPanels[i].setLayout(null);
			cbPanels[i].setBackground(colors[i]);
			cbPanels[i].setBounds(2, 10 + 35 * i, 19, 19);
			checkboxen[i].setBackground(colors[i]);
			checkboxen[i].setBounds(-1, 1, 17, 17);
			checkboxen[i].setOpaque(false);
			checkboxen[i].addActionListener(this);
			cbPanels[i].add(checkboxen[i]);
			//checkboxen[i].setVisible(false);
		}
		add(cbPanels[0]);
		
		functieVakken[0].setVisible(true);
		formuleVak = functieVakken[0].formuleVak2;
		formuleVak.requestFocus();
		actiefNummer = 0;
		
		checkboxen[0].setVisible(true);	
		checkboxen[0].setSelected(true);
	}
	
	public void zetFuncties(int funcType)
	{	functieType = funcType;
		if (functieType == Grafiek3DComponent.FUNCTION)
		{	
			maxAantalFuncties = 1; 
			functieVakken = new VergelijkingVak[maxAantalFuncties];
		
			for (int i = 0; i < maxAantalFuncties; i++)
			{	functieVakken[i] = new VergelijkingVak();
				functieVakken[i].setFont(Grafiek3DTest.formuleFont0);
				functieVakken[i].setLocation(30, 10 + 35 * i);
				functieVakken[i].setOpaque(false);
				//add(functieVakken[i],0);
				//functieVakken[i].setVisible(false);
				//	if (formalFunction) 
					functieVakken[i].formuleVak1.vulVak("$f" + functieNamen[i] + "(" + varNaamX + "," + varNaamY + ")@");
				//	else if(aantalRegels > 1)
				//		functieVakken[i].formuleVak1.vulVak("$f" + yNaam + "$s" + (i + 1) + "@@");
				//	else 
				//		functieVakken[i].formuleVak1.vulVak("$f" + yNaam + "@");
					functieVakken[i].formuleVak1.setEditable(false);
					functieVakken[i].formuleVak1.setSelectable(false);
					functieVakken[i].formuleVak2.addActionListener(this);
			}
			add(functieVakken[0], 0);

//			functieVakken[0].setVisible(true);
			formuleVak = functieVakken[0].formuleVak2;
			formuleVak.requestFocus();
			actiefNummer = 0;
		
			nieuweRegelKnop.setVisible(false);
			verwijderRegelKnop.setVisible(false);

		}
		else if (functieType == Grafiek3DComponent.SURFACE)
		{
			maxAantalFuncties = 3; 
			functieVakken = new VergelijkingVak[maxAantalFuncties];
			
			for (int i = 0; i < maxAantalFuncties; i++)
			{	functieVakken[i] = new VergelijkingVak();
				functieVakken[i].setFont(Grafiek3DTest.formuleFont0);
				functieVakken[i].setLocation(30, 10 + 35 * i);
				functieVakken[i].setOpaque(false);
				//add(functieVakken[i],0);
				//functieVakken[i].setVisible(false);
//				if (formalFunction) 
					functieVakken[i].formuleVak1.vulVak("$f" + parametrisatieNamen[i] + "(" + paramNaamU + "," + paramNaamV + ")@");
//				else if(aantalRegels > 1)
//					functieVakken[i].formuleVak1.vulVak("$f" + yNaam + "$s" + (i + 1) + "@@");
//				else 
//					functieVakken[i].formuleVak1.vulVak("$f" + yNaam + "@");
				functieVakken[i].formuleVak1.setEditable(false);
				functieVakken[i].formuleVak1.setSelectable(false);
				functieVakken[i].formuleVak2.addActionListener(this);
			}
			add(functieVakken[0], 0);
			add(functieVakken[1], 0);
			add(functieVakken[2], 0);

//			functieVakken[0].setVisible(true);
			formuleVak = functieVakken[0].formuleVak2;
			formuleVak.requestFocus();
			actiefNummer = 0;
			
			nieuweRegelKnop.setVisible(false);
			verwijderRegelKnop.setVisible(false);
		}
		else if (functieType == Grafiek3DComponent.CURVE)
		{
			maxAantalFuncties = 3; 
			functieVakken = new VergelijkingVak[maxAantalFuncties];
			
			for (int i = 0; i < maxAantalFuncties; i++)
			{	functieVakken[i] = new VergelijkingVak();
				functieVakken[i].setFont(Grafiek3DTest.formuleFont0);
				functieVakken[i].setLocation(30, 10 + 35 * i);
				functieVakken[i].setOpaque(false);
				//add(functieVakken[i],0);
				//functieVakken[i].setVisible(false);
//				if (formalFunction) 
					functieVakken[i].formuleVak1.vulVak("$f" + parametrisatieNamen[i] + "(" + paramNaam + ")@");
//				else if(aantalRegels > 1)
//					functieVakken[i].formuleVak1.vulVak("$f" + yNaam + "$s" + (i + 1) + "@@");
//				else 
//					functieVakken[i].formuleVak1.vulVak("$f" + yNaam + "@");
				functieVakken[i].formuleVak1.setEditable(false);
				functieVakken[i].formuleVak1.setSelectable(false);
				functieVakken[i].formuleVak2.addActionListener(this);
			}
			add(functieVakken[0], 0);
			add(functieVakken[1], 0);
			add(functieVakken[2], 0);
			
//			functieVakken[0].setVisible(true);
			formuleVak = functieVakken[0].formuleVak2;
			formuleVak.requestFocus();
			actiefNummer = 0;
			
			nieuweRegelKnop.setVisible(false);
			verwijderRegelKnop.setVisible(false);
			
		}
	}
	
	public void procesInput(Expressie exp, int functieVakNr)
	{	
		
//System.out.println("nr = " + functieVakNr);

		if (functieType == Grafiek3DComponent.FUNCTION)
		{	
			if (exp == null)
			{	
				grafiek3DComponent.zetGrafiek3D(null);				
//System.out.println("exp = null");
			}
			else // exp != null
			{
				String[] varNamen = Algebra.geefVarNamen(exp);
				boolean illegalVarNaam = false;
				for (int v = 0; v < varNamen.length; v++)
				{	if (!varNamen[v].equals(varNaamX) && !varNamen[v].equals(varNaamY))
						illegalVarNaam = true;
//System.out.println("vn" + v + " = " + varNamen[v]);
				}
				if (!illegalVarNaam)
				{	// groen rondje
					grafiek3DComponent.zetGrafiek3D(exp);
				}
				else
				{
					grafiek3DComponent.zetGrafiek3D(null);					
//System.out.println("illegalVarNaam " + illegalVarNaam);
				}
			}
		}
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
	
	public void actionPerformed(ActionEvent e)
	{	if (e.getSource() == nieuweRegelKnop && aantalRegels < maxAantalFuncties)
		{	if (checkboxen[aantalRegels-1].isSelected() && grafiekComponent != null)
				grafiekComponent.zetExpressie(aantalRegels - 1, functieVakken[aantalRegels - 1].formuleVak2.geefExpressie());
			add(functieVakken[aantalRegels], 0);
			add(cbPanels[aantalRegels], 0);
			layoutVakken();
			functieVakken[aantalRegels].formuleVak2.requestFocus();
			aantalRegels++;
			for(int i = 0; i < maxAantalFuncties; i++)
			{	if (formalFunction)
					functieVakken[i].formuleVak1.vulVak("$f" + namen[i] + "(" + varNaam + ")@");
				else if (aantalRegels > 1)
					functieVakken[i].formuleVak1.vulVak("$f" + yNaam + "$s" + (i + 1) + "@@");
				else 
					functieVakken[i].formuleVak1.vulVak("$f" + yNaam + "@");
			}
			return;
		}
		else if (e.getSource() == verwijderRegelKnop && aantalRegels > 1)
		{	if (grafiekComponent != null)
				grafiekComponent.zetExpressie(aantalRegels - 1, null);
			functieVakken[aantalRegels-1].formuleVak2.vulVak("$f@");
			remove(functieVakken[aantalRegels-1]);
			remove(cbPanels[aantalRegels-1]);
			layoutVakken();
			aantalRegels--;
			for (int i = 0; i < maxAantalFuncties; i++)
			{	if (formalFunction)
					functieVakken[i].formuleVak1.vulVak("$f" + namen[i] + "(" + varNaam + ")@");
				else if(aantalRegels > 1)
					functieVakken[i].formuleVak1.vulVak("$f" + yNaam + "$s" + (i + 1) + "@@");
				else 
					functieVakken[i].formuleVak1.vulVak("$f" + yNaam + "@");
			}
			
		}
		
		for (int i = 0; i < maxAantalFuncties; i++)
		{	if (e.getSource() == functieVakken[i].formuleVak2 && 
				(e.getActionCommand().equals("ingevuld") || e.getActionCommand().equals("focuslost")))
			{	Expressie exp = functieVakken[i].formuleVak2.geefExpressie();
				if (checkboxen != null && checkboxen[i] != null && checkboxen[i].isSelected() && grafiekComponent != null)
				{	grafiekComponent.zetExpressie(i,exp);
					layoutVakken();
				}
				if (grafiek3DComponent != null)
				{	
					
					procesInput(exp, i);
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
		
		for (int i = 0; i < maxAantalFuncties; i++)
		{	if (e.getSource() == functieVakken[i].formuleVak2 && e.getActionCommand().equals("focus"))
			{	if (formuleVak != functieVakken[i].formuleVak2)
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
		
		for (int i = 0; i < maxAantalFuncties; i++)
		{	if (checkboxen != null && checkboxen[i] != null && e.getSource() == checkboxen[i])
			{	if (checkboxen[i].isSelected() && grafiekComponent != null)
				{	grafiekComponent.zetExpressie(i,functieVakken[i].formuleVak2.geefExpressie());
				}
				else if (grafiekComponent != null)
				{	grafiekComponent.zetExpressie(i, null);
				}
				
				Expressie exp = formuleVak.geefExpressie();
				if (checkboxen[actiefNummer].isSelected()  && grafiekComponent != null)
					grafiekComponent.zetExpressie(actiefNummer, exp);
					if (exp != null)
					{	//formuleVak.vulVak("$f" + exp.toString() + "@");
						
					}
				
				break;
			}
			
		}
		super.actionPerformed(e);
	}
	
	public void mousePressed(MouseEvent e)
	{	//boolean focusRequest = false;
		for (int i = 0; i < aantalRegels; i++)
		{	int yMin = functieVakken[i].getLocation().y;
			int yMax = functieVakken[i].getLocation().y + functieVakken[i].getSize().height + 10;
			if (e.getY() > yMin && e.getY() < yMax)
			{	//if(formuleVak != functieVakken[i].formuleVak2)
				{	formuleVak.deSelect();
					Expressie exp = formuleVak.geefExpressie();
					if (checkboxen != null && checkboxen[actiefNummer] != null && checkboxen[actiefNummer].isSelected() && 
						grafiekComponent != null)
						grafiekComponent.zetExpressie(actiefNummer, exp);
					actiefNummer = i;
					formuleVak = functieVakken[i].formuleVak2;
					formuleVak.requestFocus();
					formuleVak.zetOpEind();
					//focusRequest = true;
				}
				break;
			}
			
		}
		
		//if (!focusRequest)
		//	requestFocus();
	}
	
	public void focusGained(FocusEvent e)
    {   
	}
	public void focusLost(FocusEvent e)
	{   
		
//System.out.println("fe focus lost");

		Expressie exp = formuleVak.geefExpressie();
		if (checkboxen != null && checkboxen != null && checkboxen[actiefNummer].isSelected() && grafiekComponent != null)
		{	grafiekComponent.zetExpressie(actiefNummer,exp);

		}
		if (grafiek3DComponent != null)
		{
			procesInput(exp, actiefNummer);
		}
		if (exp != null)
		{	formuleVak.vulVak("$f" + exp.toString() + "@");
		}
	}
}
