package fi.grafiek3dtest;

import java.awt.*;
import java.awt.event.*;
//import java.util.Hashtable;
import java.util.*;

import javax.swing.*;

import fi.grafiek3dtest.formuleobjects.*;
import fi.grafiek3dtest.expressies.*;


public class FunctieEditor extends FormuleEditor implements FocusListener
{
	private VergelijkingVak[] functieVakken;
	CheckComponent[] checks;
	
	Grafiek3DComponent grafiek3DComponent;
	
	private int maxAantalFuncties = 0;
	private int aantalRegels = 1;
	private int actiefNummer;
	
	int functieType = -1;
	
	String varNaamX = "x";
	String varNaamY = "y";
	String paramNaam = "t";
	String paramNaamU = "u";
	String paramNaamV = "v";
	String[] functieNamen = {"f","g","h","i","j","k","l","m","n","p"};
	String[] parametrisatieNamen = {"x","y","z"};
	
	JComboBox functieTypeBox;
	JLabel functieTypeLabel;
	
	JComboBox grafiekVoorbeeldenBox;
	JComboBox oppervlakVoorbeeldenBox;
	JComboBox krommeVoorbeeldenBox;
	
	boolean voorbeeldenEnabled = true;
	
	boolean functieTypeKeuze = true;
	
	String graphString = "$f@";
	String surfaceXString = "$f@";
	String surfaceYString = "$f@";
	String surfaceZString = "$f@";
	String uMinString = "$f@";
	String uMaxString = "$f@";
	String uPointsString = "$f@";
	String vMinString = "$f@";
	String vMaxString = "$f@";
	String vPointsString = "$f@";
	String curveXString = "$f@";
	String curveYString = "$f@";
	String curveZString = "$f@";
	String tMinString = "$f@";
	String tMaxString = "$f@";
	String tPointsString = "$f@";
	
	final double NZERO = 1e-5d;
	
	boolean functieTypeBoxEnabled = true;
	
	Vector grafiekVoorbeelden = new Vector();
	Vector oppervlakVoorbeelden = new Vector();
	Vector krommeVoorbeelden = new Vector();
	
	
	public FunctieEditor(boolean b)
	{	super(b);
		zetGrafiekOfEdit(true);
		addFocusListener(this);
		
		addActionListener(this);
		
		remove(formuleVak);
		formuleVak.removeActionListener(this);
		setScrollHorizontal(false);

		nieuweRegelKnop.setVisible(false);
		verwijderRegelKnop.setVisible(false);
		
		functieTypeBox = new JComboBox();
		functieTypeBox.setFont(Grafiek3DTest.tekstFont);
		FontMetrics tekstFM = getFontMetrics(Grafiek3DTest.tekstFont);
		functieTypeBox.addItem(Grafiek3DTest.rb.getString("grafiekTekst"));
		int width = tekstFM.stringWidth(Grafiek3DTest.rb.getString("grafiekTekst"));
		functieTypeBox.addItem(Grafiek3DTest.rb.getString("oppervlakTekst"));
		width = Math.max(width, tekstFM.stringWidth(Grafiek3DTest.rb.getString("oppervlakTekst")));
		functieTypeBox.addItem(Grafiek3DTest.rb.getString("krommeTekst"));
		width = Math.max(width, tekstFM.stringWidth(Grafiek3DTest.rb.getString("krommeTekst")));
		functieTypeBox.setBounds(160, 2, width + 35, 20);
		zetOpBalk(functieTypeBox);
		functieTypeBox.addActionListener(this);
		
		functieTypeLabel = new JLabel();
		functieTypeLabel.setBounds(160, 2, functieTypeBox.getSize().width, 20);
		functieTypeLabel.setVisible(false);
		zetOpBalk(functieTypeLabel);
		
		maakGrafiekVoorbeelden();
		
		grafiekVoorbeeldenBox = new JComboBox();
		grafiekVoorbeeldenBox.setFont(Grafiek3DTest.tekstFont);
		grafiekVoorbeeldenBox.addItem(Grafiek3DTest.rb.getString("voorbeeldenTekst"));
		width = tekstFM.stringWidth(Grafiek3DTest.rb.getString("voorbeeldenTekst"));
		for (int gCnt = 0; gCnt < grafiekVoorbeelden.size(); gCnt++)
		{	String name = "noName";
			if (Grafiek3DTest.langArg.equals("nl"))
				name = ((GrafiekVoorbeeld) grafiekVoorbeelden.elementAt(gCnt)).nlNaam;
			else
				name = ((GrafiekVoorbeeld) grafiekVoorbeelden.elementAt(gCnt)).enNaam;
			grafiekVoorbeeldenBox.addItem(name);
			width = Math.max(width, tekstFM.stringWidth(name));
		}
		grafiekVoorbeeldenBox.setBounds(160 + functieTypeBox.getSize().width + 30, 2, width + 35, 20);
		zetOpBalk(grafiekVoorbeeldenBox);
		grafiekVoorbeeldenBox.addActionListener(this);
		
		maakOppervlakVoorbeelden();
		
		oppervlakVoorbeeldenBox = new JComboBox();
		oppervlakVoorbeeldenBox.setFont(Grafiek3DTest.tekstFont);
		oppervlakVoorbeeldenBox.addItem(Grafiek3DTest.rb.getString("voorbeeldenTekst"));
		width = tekstFM.stringWidth(Grafiek3DTest.rb.getString("voorbeeldenTekst"));

//System.out.println("ov = " + oppervlakVoorbeelden.size());

		for (int oCnt = 0; oCnt < oppervlakVoorbeelden.size(); oCnt++)
		{	String name = "noName";
			if (Grafiek3DTest.langArg.equals("nl"))
				name = ((OppervlakVoorbeeld) oppervlakVoorbeelden.elementAt(oCnt)).nlNaam;
			else
				name = ((OppervlakVoorbeeld) oppervlakVoorbeelden.elementAt(oCnt)).enNaam;
//System.out.println("name = " + name);			
			oppervlakVoorbeeldenBox.addItem(name);
			width = Math.max(width, tekstFM.stringWidth(name));
		}
		
		oppervlakVoorbeeldenBox.setBounds(160 + functieTypeBox.getSize().width + 30, 2, width + 35, 20);
		oppervlakVoorbeeldenBox.setVisible(false); 
		zetOpBalk(oppervlakVoorbeeldenBox);
		oppervlakVoorbeeldenBox.addActionListener(this);

		maakKrommeVoorbeelden();
		
		krommeVoorbeeldenBox = new JComboBox();
		krommeVoorbeeldenBox.setFont(Grafiek3DTest.tekstFont);
		krommeVoorbeeldenBox.addItem(Grafiek3DTest.rb.getString("voorbeeldenTekst"));
		width = tekstFM.stringWidth(Grafiek3DTest.rb.getString("voorbeeldenTekst"));
		
		for (int kCnt = 0; kCnt < krommeVoorbeelden.size(); kCnt++)
		{	String name = "noName";
			if (Grafiek3DTest.langArg.equals("nl"))
				name = ((KrommeVoorbeeld) krommeVoorbeelden.elementAt(kCnt)).nlNaam;
			else
				name = ((KrommeVoorbeeld) krommeVoorbeelden.elementAt(kCnt)).enNaam;
			krommeVoorbeeldenBox.addItem(name);
			width = Math.max(width, tekstFM.stringWidth(name));
		}
		
		krommeVoorbeeldenBox.setBounds(160 + functieTypeBox.getSize().width + 30, 2, width + 35, 20);
		krommeVoorbeeldenBox.setVisible(false); 
		zetOpBalk(krommeVoorbeeldenBox);
		krommeVoorbeeldenBox.addActionListener(this);
		
	}
	
	public void maakGrafiekVoorbeelden()
	{
		GrafiekVoorbeeld grafiekVoorbeeld1 = new Paraboloide();
		grafiekVoorbeelden.addElement(grafiekVoorbeeld1);
		GrafiekVoorbeeld grafiekVoorbeeld2 = new Zadel();
		grafiekVoorbeelden.addElement(grafiekVoorbeeld2);
		GrafiekVoorbeeld grafiekVoorbeeld3 = new ReciprokeTrumpet();
		grafiekVoorbeelden.addElement(grafiekVoorbeeld3);
		GrafiekVoorbeeld grafiekVoorbeeld4 = new LnTrumpet();
		grafiekVoorbeelden.addElement(grafiekVoorbeeld4);
		GrafiekVoorbeeld grafiekVoorbeeld5 = new SineHat();
		grafiekVoorbeelden.addElement(grafiekVoorbeeld5);
		GrafiekVoorbeeld grafiekVoorbeeld6 = new TangensChaos();
		grafiekVoorbeelden.addElement(grafiekVoorbeeld6);
		
		
	}
	
	public void maakOppervlakVoorbeelden()
	{
		OppervlakVoorbeeld oppervlakVoorbeeld1 = new Cylinder();
		oppervlakVoorbeelden.addElement(oppervlakVoorbeeld1);
		OppervlakVoorbeeld oppervlakVoorbeeld2 = new Cones();
		oppervlakVoorbeelden.addElement(oppervlakVoorbeeld2);
		OppervlakVoorbeeld oppervlakVoorbeeld3 = new Helicoide();
		oppervlakVoorbeelden.addElement(oppervlakVoorbeeld3);
		OppervlakVoorbeeld oppervlakVoorbeeld4 = new Sphere();
		oppervlakVoorbeelden.addElement(oppervlakVoorbeeld4);
		OppervlakVoorbeeld oppervlakVoorbeeld5 = new Ellipsoid();
		oppervlakVoorbeelden.addElement(oppervlakVoorbeeld5);
		OppervlakVoorbeeld oppervlakVoorbeeld6 = new Torus();
		oppervlakVoorbeelden.addElement(oppervlakVoorbeeld6);
		OppervlakVoorbeeld oppervlakVoorbeeld7 = new Trumpet();
		oppervlakVoorbeelden.addElement(oppervlakVoorbeeld7);
		OppervlakVoorbeeld oppervlakVoorbeeld8 = new EightSurface();
		oppervlakVoorbeelden.addElement(oppervlakVoorbeeld8);
		OppervlakVoorbeeld oppervlakVoorbeeld9 = new Shell();
		oppervlakVoorbeelden.addElement(oppervlakVoorbeeld9);
		
	}
	
	public void maakKrommeVoorbeelden()
	{
		KrommeVoorbeeld krommeVoorbeeld1 = new Helix();
		krommeVoorbeelden.addElement(krommeVoorbeeld1);
		KrommeVoorbeeld krommeVoorbeeld2 = new ConeHelix();
		krommeVoorbeelden.addElement(krommeVoorbeeld2);
		KrommeVoorbeeld krommeVoorbeeld3 = new TorusHelix();
		krommeVoorbeelden.addElement(krommeVoorbeeld3);
		KrommeVoorbeeld krommeVoorbeeld4 = new FlowerLeaves();
		krommeVoorbeelden.addElement(krommeVoorbeeld4);
		
	}
	
	public void zetFunctieTypeKeuze(boolean b)
	{
		functieTypeKeuze = b;
		
		if (functieType == Grafiek3DComponent.FUNCTION)
		{
			functieTypeLabel.setText(Grafiek3DTest.rb.getString("grafiekTekst"));
		}
		else if (functieType == Grafiek3DComponent.SURFACE)
		{
			functieTypeLabel.setText(Grafiek3DTest.rb.getString("oppervlakTekst"));
		}
		else // CURVE
			functieTypeLabel.setText(Grafiek3DTest.rb.getString("krommeTekst"));
			
		functieTypeBox.setVisible(b);
		functieTypeLabel.setVisible(!b);
	}
	
	public void zetVoorbeeldenEnabled(boolean b)
	{
		voorbeeldenEnabled = b;
		
		if (voorbeeldenEnabled)
			toonVoorbeeldenBox(functieType);
		else
		{
			grafiekVoorbeeldenBox.setVisible(false);
			oppervlakVoorbeeldenBox.setVisible(false);
			krommeVoorbeeldenBox.setVisible(false);
		}
	}
	
	public void layoutVakken()
	{	int hoogte = 3;
		for (int i = 0; i < maxAantalFuncties; i++)
		{	if (functieVakken[i] != null)
			{	functieVakken[i].setLocation(30, hoogte);
				int vSpace = (functieVakken[i].getSize().height + 8 - checks[i].getSize().height) / 2;
				if (vSpace < 0)
					vSpace = 0;
				checks[i].setLocation(3, hoogte + vSpace - 2);
				hoogte = hoogte + functieVakken[i].getSize().height + 8;
			}
		}
		repaint();
	}

	public void zetGrafiek3DComponent(Grafiek3DComponent g3dc)
	{	grafiek3DComponent = g3dc;
	}
	
	public Hashtable getState()
	{	
System.out.println("fe getState");		
		
		Hashtable h = new Hashtable();
		
		updateExpStrings();
		
		// state
		h.put("functieType", new Integer(functieType));
		
		h.put("graphString", graphString);

		h.put("surfaceXString", surfaceXString);
		h.put("surfaceYString", surfaceYString);
		h.put("surfaceZString", surfaceZString);
		h.put("uMinString", uMinString);
		h.put("uMaxString", uMaxString);
		h.put("uPointsString", uPointsString);
		h.put("vMinString", vMinString);
		h.put("vMaxString", vMaxString);
		h.put("vPointsString", vPointsString);
		
		h.put("curveXString", curveXString);
		h.put("curveYString", curveYString);
		h.put("curveZString", curveZString);
		h.put("tMinString", tMinString);
		h.put("tMaxString", tMaxString);
		h.put("tPointsString", tPointsString);
		
		return h;
		
	}
	
	public Hashtable getEditState()
	{	

System.out.println("fe getEditState");

		Hashtable h = new Hashtable();
		
		updateExpStrings();
		
		// editstate
		h.put("functieTypeKeuze", new Boolean(functieTypeKeuze));
		h.put("voorbeeldenEnabled", new Boolean(voorbeeldenEnabled));
		
		// state
		h.put("functieType", new Integer(functieType));

		h.put("graphString", graphString);

		h.put("surfaceXString", surfaceXString);
		h.put("surfaceYString", surfaceYString);
		h.put("surfaceZString", surfaceZString);
		h.put("uMinString", uMinString);
		h.put("uMaxString", uMaxString);
		h.put("uPointsString", uPointsString);
		h.put("vMinString", vMinString);
		h.put("vMaxString", vMaxString);
		h.put("vPointsString", vPointsString);
		
		h.put("curveXString", curveXString);
		h.put("curveYString", curveYString);
		h.put("curveZString", curveZString);
		h.put("tMinString", tMinString);
		h.put("tMaxString", tMaxString);
		h.put("tPointsString", tPointsString);
		
		return h;
			
	}
	
	public void zetOpdracht(Hashtable h, String[] randomVars, Hashtable randomValues)
	{	
		int functieType = -1;
		
		// edit state
		if (h.containsKey("functieTypeKeuze")) 
			functieTypeKeuze = ((Boolean) h.get("functieTypeKeuze")).booleanValue();
		//zetFunctieTypeKeuze(functieTypeKeuze);

		if (h.containsKey("voorbeeldenEnabled")) 
			voorbeeldenEnabled = ((Boolean) h.get("voorbeeldenEnabled")).booleanValue();
		
		// state
		if (h.containsKey("functieType")) 
			functieType = ((Integer) h.get("functieType")).intValue();
		
		functieTypeBoxEnabled = false;
		functieTypeBox.setSelectedIndex(functieType);
		functieTypeBoxEnabled = true;
		
		// HIER		
//		zetFunctieTypeKeuze(functieTypeKeuze);
		
		if (h.containsKey("graphString")) 
			graphString = (String) h.get("graphString");

		if (h.containsKey("surfaceXString")) 
			surfaceXString = (String) h.get("surfaceXString");
		if (h.containsKey("surfaceYString")) 
			surfaceYString = (String) h.get("surfaceYString");
		if (h.containsKey("surfaceZString")) 
			surfaceZString = (String) h.get("surfaceZString");
		if (h.containsKey("uMinString")) 
			uMinString = (String) h.get("uMinString");
		if (h.containsKey("uMaxString")) 
			uMaxString = (String) h.get("uMaxString");
		if (h.containsKey("uPointsString")) 
			uPointsString = (String) h.get("uPointsString");
		if (h.containsKey("vMinString")) 
			vMinString = (String) h.get("vMinString");
		if (h.containsKey("vMaxString")) 
			vMaxString = (String) h.get("vMaxString");
		if (h.containsKey("vPointsString")) 
			vPointsString = (String) h.get("vPointsString");
		
		if (h.containsKey("curveXString")) 
			curveXString = (String) h.get("curveXString");
		if (h.containsKey("curveYString")) 
			curveYString = (String) h.get("curveYString");
		if (h.containsKey("curveZString")) 
			curveZString = (String) h.get("curveZString");
		if (h.containsKey("tMinString")) 
			tMinString = (String) h.get("tMinString");
		if (h.containsKey("tMaxString")) 
			tMaxString = (String) h.get("tMaxString");
		if (h.containsKey("tPointsString")) 
			tPointsString = (String) h.get("tPointsString");
		
		zetFuncties(functieType, false);
		toonVoorbeeldenBox(functieType);
	
		// HIER		
		zetFunctieTypeKeuze(functieTypeKeuze);
		
    }
	
	public void setEditState(Hashtable h)
	{	
		
System.out.println("fe setEditState");
		
		int functieType = -1;

		// edit state
		if (h.containsKey("functieTypeKeuze")) 
			functieTypeKeuze = ((Boolean) h.get("functieTypeKeuze")).booleanValue();
		//zetFunctieTypeKeuze(functieTypeKeuze);

		if (h.containsKey("voorbeeldenEnabled")) 
			voorbeeldenEnabled = ((Boolean) h.get("voorbeeldenEnabled")).booleanValue();
		
		
		// state
		if (h.containsKey("functieType")) 
			functieType = ((Integer) h.get("functieType")).intValue();

		functieTypeBoxEnabled = false;
		functieTypeBox.setSelectedIndex(functieType);
		functieTypeBoxEnabled = true;
		
		// HIER		
//		zetFunctieTypeKeuze(functieTypeKeuze);
		
		if (h.containsKey("graphString")) 
			graphString = (String) h.get("graphString");

		if (h.containsKey("surfaceXString")) 
			surfaceXString = (String) h.get("surfaceXString");
		if (h.containsKey("surfaceYString")) 
			surfaceYString = (String) h.get("surfaceYString");
		if (h.containsKey("surfaceZString")) 
			surfaceZString = (String) h.get("surfaceZString");
		if (h.containsKey("uMinString")) 
			uMinString = (String) h.get("uMinString");
		if (h.containsKey("uMaxString")) 
			uMaxString = (String) h.get("uMaxString");
		if (h.containsKey("uPointsString")) 
			uPointsString = (String) h.get("uPointsString");
		if (h.containsKey("vMinString")) 
			vMinString = (String) h.get("vMinString");
		if (h.containsKey("vMaxString")) 
			vMaxString = (String) h.get("vMaxString");
		if (h.containsKey("vPointsString")) 
			vPointsString = (String) h.get("vPointsString");
		
		if (h.containsKey("curveXString")) 
			curveXString = (String) h.get("curveXString");
		if (h.containsKey("curveYString")) 
			curveYString = (String) h.get("curveYString");
		if (h.containsKey("curveZString")) 
			curveZString = (String) h.get("curveZString");
		if (h.containsKey("tMinString")) 
			tMinString = (String) h.get("tMinString");
		if (h.containsKey("tMaxString")) 
			tMaxString = (String) h.get("tMaxString");
		if (h.containsKey("tPointsString")) 
			tPointsString = (String) h.get("tPointsString");
		
		zetFuncties(functieType, false);
		toonVoorbeeldenBox(functieType);

		// HIER		
		zetFunctieTypeKeuze(functieTypeKeuze);
		
    }
	
    public void setState(Hashtable h)
    {
		// state
		if (h.containsKey("functieType")) 
			functieType = ((Integer) h.get("functieType")).intValue();
		
		if (h.containsKey("graphString")) 
			graphString = (String) h.get("graphString");

		if (h.containsKey("surfaceXString")) 
			surfaceXString = (String) h.get("surfaceXString");
		if (h.containsKey("surfaceYString")) 
			surfaceYString = (String) h.get("surfaceYString");
		if (h.containsKey("surfaceZString")) 
			surfaceZString = (String) h.get("surfaceZString");
		if (h.containsKey("uMinString")) 
			uMinString = (String) h.get("uMinString");
		if (h.containsKey("uMaxString")) 
			uMaxString = (String) h.get("uMaxString");
		if (h.containsKey("uPointsString")) 
			uPointsString = (String) h.get("uPointsString");
		if (h.containsKey("vMinString")) 
			vMinString = (String) h.get("vMinString");
		if (h.containsKey("vMaxString")) 
			vMaxString = (String) h.get("vMaxString");
		if (h.containsKey("vPointsString")) 
			vPointsString = (String) h.get("vPointsString");
		
		if (h.containsKey("curveXString")) 
			curveXString = (String) h.get("curveXString");
		if (h.containsKey("curveYString")) 
			curveYString = (String) h.get("curveYString");
		if (h.containsKey("curveZString")) 
			curveZString = (String) h.get("curveZString");
		if (h.containsKey("tMinString")) 
			tMinString = (String) h.get("tMinString");
		if (h.containsKey("tMaxString")) 
			tMaxString = (String) h.get("tMaxString");
		if (h.containsKey("tPointsString")) 
			tPointsString = (String) h.get("tPointsString");
		
		zetFuncties(functieType, false);
		
		toonVoorbeeldenBox(functieType);
    	
    }
    
	public void updateExpStrings()
	{
		if (functieType == Grafiek3DComponent.FUNCTION)
		{	graphString = functieVakken[0].formuleVak2.toString();
		}
		else if (functieType == Grafiek3DComponent.SURFACE)
		{	surfaceXString = functieVakken[0].formuleVak2.toString();
			surfaceYString = functieVakken[1].formuleVak2.toString();
			surfaceZString = functieVakken[2].formuleVak2.toString();
			uMinString = functieVakken[3].formuleVak2.toString();
			uMaxString = functieVakken[4].formuleVak2.toString();
			uPointsString = functieVakken[5].formuleVak2.toString();	
			vMinString = functieVakken[6].formuleVak2.toString();
			vMaxString = functieVakken[7].formuleVak2.toString();
			vPointsString = functieVakken[8].formuleVak2.toString();	

		}
		else if (functieType == Grafiek3DComponent.CURVE)
		{	curveXString = functieVakken[0].formuleVak2.toString();
			curveYString = functieVakken[1].formuleVak2.toString();
			curveZString = functieVakken[2].formuleVak2.toString();
			tMinString = functieVakken[3].formuleVak2.toString();
			tMaxString = functieVakken[4].formuleVak2.toString();
			tPointsString = functieVakken[5].formuleVak2.toString();	

			
		}
		
	}
	
	public void zetFuncties(int funcType, boolean update)
	{	
		
		if (update && (functieType == funcType))
		{	return;
		}	
		
		if (update)
			updateExpStrings();

		if (update && (functieType >= 0))
			grafiek3DComponent.getHoeken();
		if (functieType == Grafiek3DComponent.FUNCTION)
		{	grafiek3DComponent.zetGrafiek3D(null);
		}
		else if (functieType == Grafiek3DComponent.SURFACE)
		{	grafiek3DComponent.zetSurface3D(null, null, null, 0, 0, 0, 0, 0, 0);
		}
		else if (functieType == Grafiek3DComponent.CURVE)
		{	grafiek3DComponent.zetCurve3D(null, null, null, 0, 0, 0);
		}

		removeFuncties();
		
		functieType = funcType;
		grafiek3DComponent.objectType = functieType;
		grafiek3DComponent.zetHoeken();

		if (grafiek3DComponent.knoppenPanel != null)
			grafiek3DComponent.layoutKnoppenPanel();
		
		if (functieType == Grafiek3DComponent.FUNCTION)
		{	
			maxAantalFuncties = 1; 
			functieVakken = new VergelijkingVak[maxAantalFuncties];
			checks = new CheckComponent[maxAantalFuncties];
		
			for (int i = 0; i < maxAantalFuncties; i++)
			{	functieVakken[i] = new VergelijkingVak();
				functieVakken[i].setFont(Grafiek3DTest.formuleFont0);
				functieVakken[i].setLocation(30, 3 + 32 * i);
				functieVakken[i].setOpaque(false);
				add(functieVakken[i], 0);
				checks[i] = new CheckComponent(3, 3 + 32 * i, 16, 16);
				add(checks[i], 0);
				functieVakken[i].formuleVak1.vulVak("$f" + functieNamen[i] + "(" + varNaamX + "," + varNaamY + ")@");
				functieVakken[i].formuleVak2.vulVak(graphString);
				functieVakken[i].formuleVak1.setEditable(false);
				functieVakken[i].formuleVak1.setSelectable(false);
				functieVakken[i].formuleVak2.addActionListener(this);
			}

			formuleVak = functieVakken[0].formuleVak2;
			formuleVak.requestFocus();
			actiefNummer = 0;
		
			
			layoutVakken();			

		}
		else if (functieType == Grafiek3DComponent.SURFACE)
		{
			maxAantalFuncties = 9; 
			functieVakken = new VergelijkingVak[maxAantalFuncties];
			checks = new CheckComponent[maxAantalFuncties];			
			
			for (int i = 0; i < maxAantalFuncties; i++)
			{	functieVakken[i] = new VergelijkingVak();
				functieVakken[i].setFont(Grafiek3DTest.formuleFont0);
				functieVakken[i].setLocation(30, 3 + 32 * i);
				functieVakken[i].setOpaque(false);
				add(functieVakken[i], 0);
				checks[i] = new CheckComponent(3, 3 + 32 * i, 16, 16);
				add(checks[i], 0);
				
				if (i < 3)
				{	functieVakken[i].formuleVak1.vulVak("$f" + parametrisatieNamen[i] + "(" + paramNaamU + "," + paramNaamV + ")@");
					if (i == 0)
						functieVakken[i].formuleVak2.vulVak(surfaceXString);
					else if (i == 1)
						functieVakken[i].formuleVak2.vulVak(surfaceYString);
					else
						functieVakken[i].formuleVak2.vulVak(surfaceZString);
						
				}
				else if (i == 3)
				{	functieVakken[i].formuleVak1.vulVak("$f" + "minimum " + paramNaamU + "@");
					functieVakken[i].formuleVak2.vulVak(uMinString);
				}
				else if (i == 4)
				{	functieVakken[i].formuleVak1.vulVak("$f" + "maximum " + paramNaamU + "@");
					functieVakken[i].formuleVak2.vulVak(uMaxString);
				}
				else if (i == 5)
				{	functieVakken[i].formuleVak1.vulVak("$f" + Grafiek3DTest.rb.getString("aantalPuntenTekst") + " " + paramNaamU + "@");
					functieVakken[i].formuleVak2.vulVak(uPointsString);
				}
				else if (i == 6)
				{	functieVakken[i].formuleVak1.vulVak("$f" + "minimum " + paramNaamV + "@");
					functieVakken[i].formuleVak2.vulVak(vMinString);
				}
				else if (i == 7)
				{	functieVakken[i].formuleVak1.vulVak("$f" + "maximum " + paramNaamV + "@");
					functieVakken[i].formuleVak2.vulVak(vMaxString);
				}
				else if (i == 8)
				{	functieVakken[i].formuleVak1.vulVak("$f" + Grafiek3DTest.rb.getString("aantalPuntenTekst") + " " + paramNaamV + "@");
					functieVakken[i].formuleVak2.vulVak(vPointsString);
				}
				
				functieVakken[i].formuleVak1.setEditable(false);
				functieVakken[i].formuleVak1.setSelectable(false);
				functieVakken[i].formuleVak2.addActionListener(this);
			}

			formuleVak = functieVakken[0].formuleVak2;
			formuleVak.requestFocus();
			actiefNummer = 0;
			
			layoutVakken();
		}
		else if (functieType == Grafiek3DComponent.CURVE)
		{
			maxAantalFuncties = 6; 
			functieVakken = new VergelijkingVak[maxAantalFuncties];
			checks = new CheckComponent[maxAantalFuncties];			
			
			for (int i = 0; i < maxAantalFuncties; i++)
			{	functieVakken[i] = new VergelijkingVak();
				functieVakken[i].setFont(Grafiek3DTest.formuleFont0);
				functieVakken[i].setLocation(30, 3 + 32 * i);
				functieVakken[i].setOpaque(false);
				add(functieVakken[i], 0);
				checks[i] = new CheckComponent(3, 3 + 32 * i, 16, 16);
				add(checks[i], 0);
				
				if (i < 3)
				{	functieVakken[i].formuleVak1.vulVak("$f" + parametrisatieNamen[i] + "(" + paramNaam + ")@");
					if (i == 0)
						functieVakken[i].formuleVak2.vulVak(curveXString);
					else if (i == 1)
						functieVakken[i].formuleVak2.vulVak(curveYString);
					else
						functieVakken[i].formuleVak2.vulVak(curveZString);
				
				}
				else if (i == 3)
				{	functieVakken[i].formuleVak1.vulVak("$f" + "minimum " + paramNaam + "@");
					functieVakken[i].formuleVak2.vulVak(tMinString);
				}
				else if (i == 4)
				{	functieVakken[i].formuleVak1.vulVak("$f" + "maximum " + paramNaam + "@");
					functieVakken[i].formuleVak2.vulVak(tMaxString);
				}
				else if (i == 5)
				{	functieVakken[i].formuleVak1.vulVak("$f" + Grafiek3DTest.rb.getString("aantalPuntenTekst") + " " + paramNaam + "@");
					functieVakken[i].formuleVak2.vulVak(tPointsString);
				}
				
				functieVakken[i].formuleVak1.setEditable(false);
				functieVakken[i].formuleVak1.setSelectable(false);
				functieVakken[i].formuleVak2.addActionListener(this);
			}

			formuleVak = functieVakken[0].formuleVak2;
			formuleVak.requestFocus();
			actiefNummer = 0;
			
			layoutVakken();			
		}
		
		procesInput();
	}
	
	public void removeFuncties()
	{	for (int i = 0; i < maxAantalFuncties; i++)
		{	remove(functieVakken[i]);
			functieVakken[i] = null;
			remove(checks[i]);
		}
		
	}
	
	public void procesInput()
	{	
		if (functieType == Grafiek3DComponent.FUNCTION)
		{	Expressie exp = functieVakken[0].formuleVak2.geefExpressie();

System.out.println("gs = " + functieVakken[0].formuleVak2.toString());
		
			if ((exp == null) || hasIllegalVarName(exp, varNaamX, varNaamY))
			{	grafiek3DComponent.zetGrafiek3D(null);	
				if (graphString.equals("$f@"))
					checks[0].setNeutral();
				else
					checks[0].setWrong();
			}
			else // legale situatie
			{	checks[0].setCorrect();
				grafiek3DComponent.zetGrafiek3D(exp);
			}
				
		}
		else if (functieType == Grafiek3DComponent.SURFACE)
		{	Expressie expX = functieVakken[0].formuleVak2.geefExpressie();
			boolean expXOK = false;
			Expressie expY = functieVakken[1].formuleVak2.geefExpressie();
			boolean expYOK = false;
			Expressie expZ = functieVakken[2].formuleVak2.geefExpressie();
			boolean expZOK = false;
			Expressie expUMin = functieVakken[3].formuleVak2.geefExpressie();
			boolean expUMinOK = false;
			Expressie expUMax = functieVakken[4].formuleVak2.geefExpressie();
			boolean expUMaxOK = false;
			Expressie expUPoints = functieVakken[5].formuleVak2.geefExpressie();
			boolean expUPointsOK = false;
			Expressie expVMin = functieVakken[6].formuleVak2.geefExpressie();
			boolean expVMinOK = false;
			Expressie expVMax = functieVakken[7].formuleVak2.geefExpressie();
			boolean expVMaxOK = false;
			Expressie expVPoints = functieVakken[8].formuleVak2.geefExpressie();
			boolean expVPointsOK = false;
			
			double uMin = 0;
			double uMax = 10;
			int uPoints = 10;
			double vMin = 0;
			double vMax = 10;
			int vPoints = 10;
			
			if ((expX == null) || hasIllegalVarName(expX, paramNaamU, paramNaamV))
			{	grafiek3DComponent.zetSurface3D(null, null, null, 0, 0, 0, 0, 0, 0);	
				if (surfaceXString.equals("$f@"))
					checks[0].setNeutral();
				else
					checks[0].setWrong();
			}
			else // legale situatie
			{	checks[0].setCorrect();
				expXOK = true;
			}
			if ((expY == null) || hasIllegalVarName(expY, paramNaamU, paramNaamV))
			{	grafiek3DComponent.zetSurface3D(null, null, null, 0, 0, 0, 0, 0, 0);	
				if (surfaceYString.equals("$f@"))
					checks[1].setNeutral();
				else
					checks[1].setWrong();
			}
			else // legale situatie
			{	checks[1].setCorrect();
				expYOK = true;
			}
			if ((expZ == null) || hasIllegalVarName(expZ, paramNaamU, paramNaamV))
			{	grafiek3DComponent.zetSurface3D(null, null, null, 0, 0, 0, 0, 0, 0);	
				if (surfaceZString.equals("$f@"))
					checks[2].setNeutral();
				else
					checks[2].setWrong();
			}
			else // legale situatie
			{	checks[2].setCorrect();
				expZOK = true;
			}
			if ((expUMin == null) || Double.isNaN(expUMin.geefWaarde()))
			{	grafiek3DComponent.zetSurface3D(null, null, null, 0, 0, 0, 0, 0, 0);
				if (uMinString.equals("$f@"))
					checks[3].setNeutral();
				else
					checks[3].setWrong();
			}
			else // legale situatie
			{	checks[3].setCorrect();
				expUMinOK = true;
				uMin = expUMin.geefWaarde();
			}
			if ((expUMax == null) || Double.isNaN(expUMax.geefWaarde()))
			{	grafiek3DComponent.zetSurface3D(null, null, null, 0, 0, 0, 0, 0, 0);
				if (uMaxString.equals("$f@"))
					checks[4].setNeutral();
				else
					checks[4].setWrong();
			}
			else // legale situatie
			{	checks[4].setCorrect();
				expUMaxOK = true;
				uMax = expUMax.geefWaarde();
			}
			if ((expUPoints == null) || Double.isNaN(expUPoints.geefWaarde()))
			{	grafiek3DComponent.zetSurface3D(null, null, null, 0, 0, 0, 0, 0, 0);
				if (uPointsString.equals("$f@"))
					checks[5].setNeutral();
				else
					checks[5].setWrong();
			}
			else // soms legale situatie
			{	double uPointsDouble = expUPoints.geefWaarde();
//System.out.println("uPD = " + uPointsDouble);			
				int uPointsInteger = (int) Math.round(uPointsDouble); 
//System.out.println("uPI = " + uPointsInteger);				
				if ((Math.abs(uPointsDouble - uPointsInteger) > NZERO) || (uPointsInteger < 3))
				{	grafiek3DComponent.zetSurface3D(null, null, null, 0, 0, 0, 0, 0, 0);
					if (uPointsString.equals("$f@"))
						checks[5].setNeutral();
					else
						checks[5].setWrong();
				}
				else
				{	checks[5].setCorrect();
					expUPointsOK = true;
					uPoints = uPointsInteger;
				}	
			}
			
			
			
			if ((expVMin == null) || Double.isNaN(expVMin.geefWaarde()))
			{	grafiek3DComponent.zetSurface3D(null, null, null, 0, 0, 0, 0, 0, 0);
				if (vMinString.equals("$f@"))
					checks[6].setNeutral();
				else
					checks[6].setWrong();
			}
			else // legale situatie
			{	checks[6].setCorrect();
				expVMinOK = true;
				vMin = expVMin.geefWaarde();
			}
			if ((expVMax == null) || Double.isNaN(expVMax.geefWaarde()))
			{	grafiek3DComponent.zetSurface3D(null, null, null, 0, 0, 0, 0, 0, 0);
				if (vMaxString.equals("$f@"))
					checks[7].setNeutral();
				else
					checks[7].setWrong();
			}
			else // legale situatie
			{	checks[7].setCorrect();
				expVMaxOK = true;
				vMax = expVMax.geefWaarde();
			}
			if ((expVPoints == null) || Double.isNaN(expVPoints.geefWaarde()))
			{	grafiek3DComponent.zetSurface3D(null, null, null, 0, 0, 0, 0, 0, 0);
				if (vPointsString.equals("$f@"))
					checks[8].setNeutral();
				else
					checks[8].setWrong();
			}
			else // soms legale situatie
			{	double vPointsDouble = expVPoints.geefWaarde();
				int vPointsInteger = (int) Math.round(vPointsDouble); 
				if ((Math.abs(vPointsDouble - vPointsInteger) > NZERO) || (vPointsInteger < 3))
				{	grafiek3DComponent.zetSurface3D(null, null, null, 0, 0, 0, 0, 0, 0);
					if (vPointsString.equals("$f@"))
						checks[8].setNeutral();
					else
						checks[8].setWrong();
				}
				else
				{	checks[8].setCorrect();
					expVPointsOK = true;
					vPoints = vPointsInteger;
				}	
			}
// check nog op uMin>=uMax, vMin>=vMax 		
			if (uMin > (uMax - NZERO))
			{	expUMinOK = false;
				expUMaxOK = false;
				checks[3].setWrong();
				checks[4].setWrong();
			}
			if (vMin > (vMax - NZERO))
			{	expVMinOK = false;
				expVMaxOK = false;
				checks[6].setWrong();
				checks[7].setWrong();
			}
			
			if (expXOK && expYOK && expZOK && expUMinOK && expUMaxOK && expUPointsOK &&
				expVMinOK && expVMaxOK && expVPointsOK)
			{
				grafiek3DComponent.zetSurface3D(expX, expY, expZ, uMin, uMax, uPoints, vMin, vMax, vPoints);
			}
			
			
		}
		else if (functieType == Grafiek3DComponent.CURVE)
		{	Expressie expX = functieVakken[0].formuleVak2.geefExpressie();
			boolean expXOK = false;
			Expressie expY = functieVakken[1].formuleVak2.geefExpressie();
			boolean expYOK = false;
			Expressie expZ = functieVakken[2].formuleVak2.geefExpressie();
			boolean expZOK = false;
			Expressie expTMin = functieVakken[3].formuleVak2.geefExpressie();
			boolean expTMinOK = false;
			Expressie expTMax = functieVakken[4].formuleVak2.geefExpressie();
			boolean expTMaxOK = false;
			Expressie expTPoints = functieVakken[5].formuleVak2.geefExpressie();
			boolean expTPointsOK = false;

			double tMin = 0;
			double tMax = 10;
			int tPoints = 10;

			if ((expX == null) || hasIllegalVarName(expX, paramNaam))
			{	grafiek3DComponent.zetCurve3D(null, null, null, 0, 0, 0);	
				if (curveXString.equals("$f@"))
					checks[0].setNeutral();
				else
					checks[0].setWrong();
			}
			else // legale situatie
			{	checks[0].setCorrect();
				expXOK = true;
			}
			if ((expY == null) || hasIllegalVarName(expY, paramNaam))
			{	grafiek3DComponent.zetCurve3D(null, null, null, 0, 0, 0);	
				if (curveYString.equals("$f@"))
					checks[1].setNeutral();
				else
					checks[1].setWrong();
			}
			else // legale situatie
			{	checks[1].setCorrect();
				expYOK = true;
			}
			if ((expZ == null) || hasIllegalVarName(expZ, paramNaam))
			{	grafiek3DComponent.zetCurve3D(null, null, null, 0, 0, 0);	
				if (curveZString.equals("$f@"))
					checks[2].setNeutral();
				else
					checks[2].setWrong();
			}
			else // legale situatie
			{	checks[2].setCorrect();
				expZOK = true;
			}
			if ((expTMin == null) || Double.isNaN(expTMin.geefWaarde()))
			{	grafiek3DComponent.zetCurve3D(null, null, null, 0, 0, 0);
				if (tMinString.equals("$f@"))
					checks[3].setNeutral();
				else
					checks[3].setWrong();
			}
			else // legale situatie
			{	checks[3].setCorrect();
				expTMinOK = true;
				tMin = expTMin.geefWaarde();
			}
			if ((expTMax == null) || Double.isNaN(expTMax.geefWaarde()))
			{	grafiek3DComponent.zetCurve3D(null, null, null, 0, 0, 0);
				if (tMaxString.equals("$f@"))
					checks[4].setNeutral();
				else
					checks[4].setWrong();
			}
			else // legale situatie
			{	checks[4].setCorrect();
				expTMaxOK = true;
				tMax = expTMax.geefWaarde();
			}
			if ((expTPoints == null) || Double.isNaN(expTPoints.geefWaarde()))
			{	grafiek3DComponent.zetCurve3D(null, null, null, 0, 0, 0);
				if (tPointsString.equals("$f@"))
					checks[5].setNeutral();
				else
					checks[5].setWrong();
			}
			else // soms legale situatie
			{	double tPointsDouble = expTPoints.geefWaarde();
//System.out.println("uPD = " + uPointsDouble);			
				int tPointsInteger = (int) Math.round(tPointsDouble); 
//System.out.println("uPI = " + uPointsInteger);				
				if ((Math.abs(tPointsDouble - tPointsInteger) > NZERO) || (tPointsInteger < 3))
				{	grafiek3DComponent.zetCurve3D(null, null, null, 0, 0, 0);
					if (tPointsString.equals("$f@"))
						checks[5].setNeutral();
					else
						checks[5].setWrong();
				}
				else
				{	checks[5].setCorrect();
					expTPointsOK = true;
					tPoints = tPointsInteger;
				}	
			}

			if (tMin > (tMax - NZERO))
			{	expTMinOK = false;
				expTMaxOK = false;
				checks[3].setWrong();
				checks[4].setWrong();
			}
			
			if (expXOK && expYOK && expZOK && expTMinOK && expTMaxOK && expTPointsOK)
			{
				grafiek3DComponent.zetCurve3D(expX, expY, expZ, tMin, tMax, tPoints);
			}
//if (expTMin != null)			
//System.out.println("etmin = " + expTMin.toString());			
		}
		
		
		
	}
	
	
	public boolean hasIllegalVarName(Expressie exp, String legalName)
	{	boolean illegalVarNaam = false;
		String[] varNamen = Algebra.geefVarNamen(exp);
		for (int v = 0; v < varNamen.length; v++)
		{	if (!varNamen[v].equals(legalName))
				illegalVarNaam = true;
		}
		return illegalVarNaam;
	}
	
	public boolean hasIllegalVarName(Expressie exp, String legalName1, String legalName2)
	{	boolean illegalVarNaam = false;
		String[] varNamen = Algebra.geefVarNamen(exp);
		for (int v = 0; v < varNamen.length; v++)
		{	if (!varNamen[v].equals(legalName1) && !varNamen[v].equals(legalName2))
				illegalVarNaam = true;
		}
		return illegalVarNaam;
	}
	
	public FormuleVak geefFormuleVak()
	{	return formuleVak;
	}
	
	public Expressie geefExpressie()
	{	FormuleParser p = new FormuleParser();
		return formuleVak.geefExpressie();
	}
	
	
	public void paint(Graphics g)
	{
		super.paint(g);
		
		for (int i = 0; i < maxAantalFuncties; i++)
		{	if (functieVakken[i] != null)
			{
				g.setColor(Color.gray);
				g.drawRect(0, headerPanel.getLocation().y + headerPanel.getSize().height + 2 +  
						   functieVakken[i].getLocation().y - 2, 
						   getSize().width - 1, 
						   functieVakken[i].getSize().height + 8);
			}
		}
	}
	
	public void zetGrafiekVoorbeeld(GrafiekVoorbeeld gv)
	{
		functieVakken[0].formuleVak2.vulVak(gv.graphString);
		grafiek3DComponent.zetGrafiekVoorbeeld(gv);
		procesInput();
		
	}

	public void zetOppervlakVoorbeeld(OppervlakVoorbeeld ov)
	{
		functieVakken[0].formuleVak2.vulVak(ov.surfaceXString);
		functieVakken[1].formuleVak2.vulVak(ov.surfaceYString);
		functieVakken[2].formuleVak2.vulVak(ov.surfaceZString);
		functieVakken[3].formuleVak2.vulVak(ov.uMinString);
		functieVakken[4].formuleVak2.vulVak(ov.uMaxString);
		functieVakken[5].formuleVak2.vulVak(ov.uPointsString);
		functieVakken[6].formuleVak2.vulVak(ov.vMinString);
		functieVakken[7].formuleVak2.vulVak(ov.vMaxString);
		functieVakken[8].formuleVak2.vulVak(ov.vPointsString);
		
		grafiek3DComponent.zetOppervlakVoorbeeld(ov);
		procesInput();
		
	}

	public void zetKrommeVoorbeeld(KrommeVoorbeeld kv)
	{
			functieVakken[0].formuleVak2.vulVak(kv.curveXString);
			functieVakken[1].formuleVak2.vulVak(kv.curveYString);
			functieVakken[2].formuleVak2.vulVak(kv.curveZString);
			functieVakken[3].formuleVak2.vulVak(kv.tMinString);
			functieVakken[4].formuleVak2.vulVak(kv.tMaxString);
			functieVakken[5].formuleVak2.vulVak(kv.tPointsString);
		
		grafiek3DComponent.zetKrommeVoorbeeld(kv);
		procesInput();
		
	}
	
	public void toonVoorbeeldenBox(int type)
	{
		if (!voorbeeldenEnabled)
		{	grafiekVoorbeeldenBox.setVisible(false);
			oppervlakVoorbeeldenBox.setVisible(false);
			krommeVoorbeeldenBox.setVisible(false);
			
			return;
		
		}
		
		if (type == Grafiek3DComponent.FUNCTION)
		{	grafiekVoorbeeldenBox.setVisible(true);
			oppervlakVoorbeeldenBox.setVisible(false);
			krommeVoorbeeldenBox.setVisible(false);
			
		}
		else if (type == Grafiek3DComponent.SURFACE)
		{	grafiekVoorbeeldenBox.setVisible(false);
			oppervlakVoorbeeldenBox.setVisible(true);
			krommeVoorbeeldenBox.setVisible(false);
			
		}
		else if (type == Grafiek3DComponent.CURVE)
		{	grafiekVoorbeeldenBox.setVisible(false);
			oppervlakVoorbeeldenBox.setVisible(false);
			krommeVoorbeeldenBox.setVisible(true);
			
		}
	}
	
	public void actionPerformed(ActionEvent e)
	{	
		if (e.getSource() == functieTypeBox)
		{
			if (!functieTypeBoxEnabled)
				return;
			
			String selString = (String) functieTypeBox.getSelectedItem();
			//removeFuncties();
			if (selString.equals(Grafiek3DTest.rb.getString("grafiekTekst")))
			{	
				zetFuncties(Grafiek3DComponent.FUNCTION, true);
				toonVoorbeeldenBox(Grafiek3DComponent.FUNCTION);
			}
			else if (selString.equals(Grafiek3DTest.rb.getString("oppervlakTekst")))
			{	
				zetFuncties(Grafiek3DComponent.SURFACE, true);
				toonVoorbeeldenBox(Grafiek3DComponent.SURFACE);
			}
			else if (selString.equals(Grafiek3DTest.rb.getString("krommeTekst")))
			{	
				zetFuncties(Grafiek3DComponent.CURVE, true);
				toonVoorbeeldenBox(Grafiek3DComponent.CURVE);
			}
			
			
			removeTablet2();
		}
		
		if (e.getSource() == grafiekVoorbeeldenBox)
		{
			int grIndex = grafiekVoorbeeldenBox.getSelectedIndex();
//System.out.println("gvbi = " + grIndex);			
			if (grIndex >= 1)
			{
				GrafiekVoorbeeld gv = (GrafiekVoorbeeld) grafiekVoorbeelden.elementAt(grIndex - 1);
				zetGrafiekVoorbeeld(gv);
			}
		}

		if (e.getSource() == oppervlakVoorbeeldenBox)
		{
			int opIndex = oppervlakVoorbeeldenBox.getSelectedIndex();
//System.out.println("gvbi = " + grIndex);			
			if (opIndex >= 1)
			{
				OppervlakVoorbeeld ov = (OppervlakVoorbeeld) oppervlakVoorbeelden.elementAt(opIndex - 1);
				zetOppervlakVoorbeeld(ov);
			}
		}

		if (e.getSource() == krommeVoorbeeldenBox)
		{
			int krIndex = krommeVoorbeeldenBox.getSelectedIndex();
//System.out.println("gvbi = " + grIndex);			
			if (krIndex >= 1)
			{
				KrommeVoorbeeld kv = (KrommeVoorbeeld) krommeVoorbeelden.elementAt(krIndex - 1);
				zetKrommeVoorbeeld(kv);
			}
		}
		
		for (int i = 0; i < maxAantalFuncties; i++)
		{	if (e.getSource() == functieVakken[i].formuleVak2 && 
				(e.getActionCommand().equals("ingevuld") || e.getActionCommand().equals("focuslost")))
			{	Expressie exp = functieVakken[i].formuleVak2.geefExpressie();
				
				if (grafiek3DComponent != null)
				{	layoutVakken();
					updateExpStrings();
					procesInput();
				}
				
				break;
				
			}
			
			
		}
		
		for (int i = 0; i < maxAantalFuncties; i++)
		{	if (e.getSource() == functieVakken[i].formuleVak2 && e.getActionCommand().equals("focus"))
			{	
//System.out.println("focus " + i);			
				if (formuleVak != functieVakken[i].formuleVak2)
				{	formuleVak.deSelect();

					if (grafiek3DComponent != null)
					{
						layoutVakken();
						updateExpStrings();
						procesInput();
					}	
					
					actiefNummer = i;
					formuleVak = functieVakken[i].formuleVak2;
					
				}
				break;
			}
			
		}
		
		if (e.getActionCommand().equals("knop"))
		{	layoutVakken();
//System.out.println("knop");			
		}

		super.actionPerformed(e);
	}
	
	public void mousePressed(MouseEvent e)
	{	//boolean focusRequest = false;
		for (int i = 0; i < aantalRegels; i++)
		{	int yMin = functieVakken[i].getLocation().y;
			int yMax = functieVakken[i].getLocation().y + functieVakken[i].getSize().height;// + 10;
			if (e.getY() > yMin && e.getY() < yMax)
			{	//if(formuleVak != functieVakken[i].formuleVak2)
				{	formuleVak.deSelect();

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

		if (grafiek3DComponent != null)
		{
			layoutVakken();
			updateExpStrings();			
			procesInput();
		}
		if (exp != null)
		{	formuleVak.vulVak("$f" + exp.toString() + "@");
		}
	}
}
