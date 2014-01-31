package fi.geomalgebra;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Vector;
import java.awt.Color;

import fi.geomalgebra.expressies.*;

import javax.swing.*;

import fi.beans.wiskopdrbeans.WiskOpdrApplet;
// deze moet vanwege interface WiskOpdrApplet
import fi.beans.wiskopdrbeans.InteractiePanel;
// deze moet vanwege interface InteractiePanel
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.geomalgebra.formuleobjects.FormuleParser;


public class GAInteractiePanel extends JPanel implements InteractiePanel, InteractieEditPanel,
							         ActionListener
									
{
	AlgebraVeld av = null;	
	ControlPanel cp;
	LineaalHor lh;
	LineaalVer lv;
	
	boolean varWaardeZichtbaar;
	boolean oppWaardeZichtbaar = false;
	boolean formuleZichtbaar = true;
	boolean constructieTools = true;
	boolean alleenOppervlaktes;
	boolean werkblad;
	boolean oppervlaktesZichtbaar = true;
	boolean lengtesBreedtesZichtbaar = true;
	boolean negatieveWaarden = true;
	boolean puzzelen = false;

	boolean kijkNaActief;
	boolean equivalent = true;
	String antwoordFormuleStringCorrect = "";
	
	int score = 0;
	int scoreMax = 10;
	
    boolean ingevuld = false;
	private boolean nagekeken = false;
	private int mode;
	
	
	ImageIcon goedkrulIcon, foutkruisIcon, halfkrulIcon, resetIcon;
	JButton kijkNaButton;
	JPanel kijkNaPanel;
	JLabel groenVinkjeLabel;
	JLabel geelVinkjeLabel;
	JLabel kruisjeLabel;
	int kijkNaHeight;
	
    Vector listeners = new Vector();
    
	boolean kijkNaChanged = false;	
	
	GAInteractieEditPanel gaiep = null;
		
	public GAInteractiePanel()
	{
		setLayout(null);
		// echte initiatie vind pas plaats na setBounds
		java.net.URL imageURL = GeomAlgebra.class.getResource("resources/goedkrul_en.gif");
		if (imageURL != null) 
		{
		    goedkrulIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading goedkrul_en.gif.");
		}
		imageURL = GeomAlgebra.class.getResource("resources/foutkruis.gif");
		if (imageURL != null) 
		{
			foutkruisIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading foutkruis.gif");
		}
		imageURL = GeomAlgebra.class.getResource("resources/goedkrulhalf.gif");
		if (imageURL != null) 
		{
			halfkrulIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading goedkrulhalf.");
		}
		imageURL = GeomAlgebra.class.getResource("resources/reseticon.gif");
		if (imageURL != null) 
		{
			resetIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading reseticon.gif");
		}
		
		
		Font theFont = new Font("SansSerif", Font.PLAIN, 12);
		FontMetrics theFM = getFontMetrics(theFont);
		
		
		kijkNaButton = new JButton(GeomAlgebra.rb.getString("kijkNaTekst"));
		kijkNaButton.setFont(theFont);
		kijkNaButton.setBounds(0, 0, 75, 24);
		kijkNaButton.addActionListener(this);
		
	    groenVinkjeLabel = new JLabel(goedkrulIcon);
		groenVinkjeLabel.setBounds(76, 2, 20, 20);

	    geelVinkjeLabel = new JLabel(halfkrulIcon);
		geelVinkjeLabel.setBounds(76, 2, 20, 20);
		
	    kruisjeLabel = new JLabel(foutkruisIcon);
		kruisjeLabel.setBounds(76, 2, 20, 20);
		
		groenVinkjeLabel.setVisible(false);
		geelVinkjeLabel.setVisible(false);
		kruisjeLabel.setVisible(false);
		
		kijkNaPanel = new JPanel(null);
		kijkNaPanel.setOpaque(false);
		
		kijkNaHeight = 3 * theFM.getHeight() / 2;
		//kijkNaPanel.setBackground(Color.cyan);
		kijkNaPanel.setSize(95, kijkNaHeight);
		kijkNaPanel.add(kijkNaButton);
		kijkNaPanel.add(groenVinkjeLabel);
		kijkNaPanel.add(geelVinkjeLabel);
		kijkNaPanel.add(kruisjeLabel);
		kijkNaPanel.setVisible(false);
		add(kijkNaPanel);
		
	}

	
	public void zetOpdracht(Hashtable b, String[] randomVars, Hashtable randomValues)
	{
//System.out.println("gaip zetOpdracht");
		
		boolean varWaardeZichtbaar = false;
		boolean oppWaardeZichtbaar = false;
		boolean formuleZichtbaar = true;
		boolean constructieTools = true;
		boolean alleenOppervlaktes = false;
		boolean werkblad = false;
		boolean oppervlaktesZichtbaar = true;
		boolean lengtesBreedtesZichtbaar = true;
		boolean negatieveWaarden = true;
		boolean puzzelen = false;

		boolean kijkNaActief = false;
		boolean equivalent = true;
		String antwoordFormuleStringCorrect = "";
		int scoreMax = 10;
	

		if (b.containsKey("appletLaunchData"))
		{
//System.out.println("aLD found");

			Hashtable appletLaunchData = (Hashtable) b.get("appletLaunchData");
			
			String varWaardeZichtbaarString = "false";
			String oppWaardeZichtbaarString = "false";
			String formuleZichtbaarString = "true";
			String constructieToolsString = "true";
			String alleenOppervlaktesString = "false";
			
			if (appletLaunchData.containsKey("varWaarde"))
				varWaardeZichtbaarString = (String) appletLaunchData.get("varWaarde");
			if (varWaardeZichtbaarString.equals("true") || varWaardeZichtbaarString.equals("yes"))
				varWaardeZichtbaar = true;
			if (appletLaunchData.containsKey("oppWaarde"))
				oppWaardeZichtbaarString = (String) appletLaunchData.get("oppWaarde");
			if (oppWaardeZichtbaarString.equals("true") || oppWaardeZichtbaarString.equals("yes"))
				oppWaardeZichtbaar = true;
			if (appletLaunchData.containsKey("formule"))
				formuleZichtbaarString = (String) appletLaunchData.get("formule");
			if (formuleZichtbaarString.equals("false") || formuleZichtbaarString.equals("no"))
				formuleZichtbaar = false;
			if (appletLaunchData.containsKey("constructieTools"))
				constructieToolsString = (String) appletLaunchData.get("constructieTools");
			if (constructieToolsString.equals("false") || constructieToolsString.equals("no"))
				constructieTools = false;
			if (appletLaunchData.containsKey("alleenOppervlaktes"))
				alleenOppervlaktesString = (String) appletLaunchData.get("alleenOppervlaktes");
			if (alleenOppervlaktesString.equals("true") || alleenOppervlaktesString.equals("yes"))
				alleenOppervlaktes = true;
						
			

		}
		else
		{
			if (b.containsKey("varWaardeZichtbaar"))
				varWaardeZichtbaar = ((Boolean) b.get("varWaardeZichtbaar")).booleanValue();
			if (b.containsKey("oppWaardeZichtbaar"))
				oppWaardeZichtbaar = ((Boolean) b.get("oppWaardeZichtbaar")).booleanValue();
			if (b.containsKey("formuleZichtbaar"))
				formuleZichtbaar = ((Boolean) b.get("formuleZichtbaar")).booleanValue();
			if (b.containsKey("constructieTools"))
				constructieTools = ((Boolean) b.get("constructieTools")).booleanValue();
			if (b.containsKey("alleenOppervlaktes"))
				alleenOppervlaktes = ((Boolean) b.get("alleenOppervlaktes")).booleanValue();
			if (b.containsKey("werkblad"))
				werkblad = ((Boolean) b.get("werkblad")).booleanValue();
			if (b.containsKey("oppervlaktesZichtbaar"))
				oppervlaktesZichtbaar = ((Boolean) b.get("oppervlaktesZichtbaar")).booleanValue();
			if (b.containsKey("lengtesBreedtesZichtbaar"))
				lengtesBreedtesZichtbaar = ((Boolean) b.get("lengtesBreedtesZichtbaar")).booleanValue();
			if (b.containsKey("negatieveWaarden"))
				negatieveWaarden = ((Boolean) b.get("negatieveWaarden")).booleanValue();
			if (b.containsKey("puzzelen"))
				puzzelen = ((Boolean) b.get("puzzelen")).booleanValue();

			
			if (b.containsKey("kijkNaActief"))
				kijkNaActief = ((Boolean) b.get("kijkNaActief")).booleanValue();
			if (b.containsKey("equivalent"))
				equivalent = ((Boolean) b.get("equivalent")).booleanValue();
			if (b.containsKey("antwoordFormuleStringCorrect"))
				antwoordFormuleStringCorrect = (String) b.get("antwoordFormuleStringCorrect");

			if (b.containsKey("scoreMax"))
				scoreMax = ((Integer) b.get("scoreMax")).intValue();
			
			
		}
		
		zetVarWaardeZichtbaar(varWaardeZichtbaar);
		zetOppWaardeZichtbaar(oppWaardeZichtbaar);
		zetFormuleZichtbaar(formuleZichtbaar);
		zetConstructieTools(constructieTools);
		zetAlleenOppervlaktes(alleenOppervlaktes);
		zetWerkblad(werkblad);
		zetOppervlaktesZichtbaar(oppervlaktesZichtbaar);
		zetLengtesBreedtesZichtbaar(lengtesBreedtesZichtbaar);
		zetNegatieveWaarden(negatieveWaarden);
		zetPuzzelen(puzzelen);
		
		zetKijkNaActief(kijkNaActief);
		this.equivalent = equivalent;
		this.antwoordFormuleStringCorrect = antwoordFormuleStringCorrect;
//System.out.println("zet o afs = " + antwoordFormuleStringCorrect);		
		this.scoreMax = scoreMax;
		
		
		if (b.containsKey("appletEditState"))
		{	String appletEditState = (String) b.get("appletEditState");
			av.setState(appletEditState);
			
		}
		else if (b.containsKey("state"))
		{	
			
			State state = (State) b.get("state");
			av.setState(state);
			
		}
		else if (b.containsKey("stateHM"))
		{
			HashMap stateHM = (HashMap) b.get("stateHM");
			State state = NoSer.setStateState(stateHM);
			av.setState(state);
			
//System.out.println("zetOpdr stateHM");			
		}
		
		av.docentState = new State(av.aantalFg, av.fg, av.var);
		
	
//System.out.println("af = " + av.aantalFg);

		//if (kijkNaActief)
		//	kijkNa();
	}
	
	public void setState(Hashtable b)
	{
		if (b.containsKey("appletState"))
		{	String appletState = (String) b.get("appletState");
			av.setState(appletState);
		}
		else if (b.containsKey("state"))
		{	State state = (State) b.get("state");
			av.setState(state);
		}
		else if (b.containsKey("stateHM"))
		{
			HashMap stateHM = (HashMap) b.get("stateHM");
			State state = NoSer.setStateState(stateHM);
			av.setState(state);
//System.out.println("setState stateHM");			
		}
		if (kijkNaActief)
			kijkNa();
		
	}
	
	public void setEditState(Hashtable b)
	{
//System.out.println("gaip setEditState");

		boolean varWaardeZichtbaar = false;
		boolean oppWaardeZichtbaar = false;
		boolean formuleZichtbaar = true;
		boolean constructieTools = true;
		boolean alleenOppervlaktes = false;
		boolean werkblad = false;
		boolean oppervlaktesZichtbaar = true;
		boolean lengtesBreedtesZichtbaar = true;
		boolean negatieveWaarden = true;
		boolean puzzelen = false;

		boolean kijkNaActief = false;
		boolean equivalent = true;
		String antwoordFormuleStringCorrect = "";
		int scoreMax = 10;
		
	

		if (b.containsKey("appletLaunchData"))
		{
//System.out.println("aLD found");

			Hashtable appletLaunchData = (Hashtable) b.get("appletLaunchData");
			
			String varWaardeZichtbaarString = "false";
			String oppWaardeZichtbaarString = "false";
			String formuleZichtbaarString = "true";
			String constructieToolsString = "true";
			String alleenOppervlaktesString = "false";
			
			if (appletLaunchData.containsKey("varWaarde"))
				varWaardeZichtbaarString = (String) appletLaunchData.get("varWaarde");
			if (varWaardeZichtbaarString.equals("true") || varWaardeZichtbaarString.equals("yes"))
				varWaardeZichtbaar = true;
			if (appletLaunchData.containsKey("oppWaarde"))
				oppWaardeZichtbaarString = (String) appletLaunchData.get("oppWaarde");
			if (oppWaardeZichtbaarString.equals("true") || oppWaardeZichtbaarString.equals("yes"))
				oppWaardeZichtbaar = true;
			if (appletLaunchData.containsKey("formule"))
				formuleZichtbaarString = (String) appletLaunchData.get("formule");
			if (formuleZichtbaarString.equals("false") || formuleZichtbaarString.equals("no"))
				formuleZichtbaar = false;
			if (appletLaunchData.containsKey("constructieTools"))
				constructieToolsString = (String) appletLaunchData.get("constructieTools");
			if (constructieToolsString.equals("false") || constructieToolsString.equals("no"))
				constructieTools = false;
			if (appletLaunchData.containsKey("alleenOppervlaktes"))
				alleenOppervlaktesString = (String) appletLaunchData.get("alleenOppervlaktes");
			if (alleenOppervlaktesString.equals("true") || alleenOppervlaktesString.equals("yes"))
				alleenOppervlaktes = true;
						
			

		}
		else
		{
			if (b.containsKey("varWaardeZichtbaar"))
				varWaardeZichtbaar = ((Boolean) b.get("varWaardeZichtbaar")).booleanValue();
			if (b.containsKey("oppWaardeZichtbaar"))
				oppWaardeZichtbaar = ((Boolean) b.get("oppWaardeZichtbaar")).booleanValue();
			if (b.containsKey("formuleZichtbaar"))
				formuleZichtbaar = ((Boolean) b.get("formuleZichtbaar")).booleanValue();
			if (b.containsKey("constructieTools"))
				constructieTools = ((Boolean) b.get("constructieTools")).booleanValue();
			if (b.containsKey("alleenOppervlaktes"))
				alleenOppervlaktes = ((Boolean) b.get("alleenOppervlaktes")).booleanValue();
			
			if (b.containsKey("werkblad"))
				werkblad = ((Boolean) b.get("werkblad")).booleanValue();
			if (b.containsKey("oppervlaktesZichtbaar"))
				oppervlaktesZichtbaar = ((Boolean) b.get("oppervlaktesZichtbaar")).booleanValue();
			if (b.containsKey("lengtesBreedtesZichtbaar"))
				lengtesBreedtesZichtbaar = ((Boolean) b.get("lengtesBreedtesZichtbaar")).booleanValue();
			if (b.containsKey("negatieveWaarden"))
				negatieveWaarden = ((Boolean) b.get("negatieveWaarden")).booleanValue();
			if (b.containsKey("puzzelen"))
				puzzelen = ((Boolean) b.get("puzzelen")).booleanValue();
			
			if (b.containsKey("kijkNaActief"))
				kijkNaActief = ((Boolean) b.get("kijkNaActief")).booleanValue();
			if (b.containsKey("equivalent"))
				equivalent = ((Boolean) b.get("equivalent")).booleanValue();
			if (b.containsKey("antwoordFormuleStringCorrect"))
				antwoordFormuleStringCorrect = (String) b.get("antwoordFormuleStringCorrect");

			if (b.containsKey("scoreMax"))
				scoreMax = ((Integer) b.get("scoreMax")).intValue();
			
			
		}

		zetVarWaardeZichtbaar(varWaardeZichtbaar);
		zetOppWaardeZichtbaar(oppWaardeZichtbaar);
		zetFormuleZichtbaar(formuleZichtbaar);
		zetConstructieTools(constructieTools);
		zetAlleenOppervlaktes(alleenOppervlaktes);
		zetWerkblad(werkblad);
		zetOppervlaktesZichtbaar(oppervlaktesZichtbaar);
		zetLengtesBreedtesZichtbaar(lengtesBreedtesZichtbaar);
		zetNegatieveWaarden(negatieveWaarden);
		zetPuzzelen(puzzelen);
		
		zetKijkNaActief(kijkNaActief);
		this.equivalent = equivalent;
		this.antwoordFormuleStringCorrect = antwoordFormuleStringCorrect;
		this.scoreMax = scoreMax;
		
		if (b.containsKey("appletEditState"))
		{	String appletEditState = (String) b.get("appletEditState");
			av.setState(appletEditState);
			av.docentState = new State(av.aantalFg, av.fg, av.var);
		}
		else if (b.containsKey("state"))
		{	State state = (State) b.get("state");
			av.setState(state);
			av.docentState = new State(av.aantalFg, av.fg, av.var);
		}
		else if (b.containsKey("stateHM"))
		{
			HashMap stateHM = (HashMap) b.get("stateHM");
			State state = NoSer.setStateState(stateHM);
			av.setState(state);
			av.docentState = new State(av.aantalFg, av.fg, av.var);
//System.out.println("setEditState stateHM");			
		}
		
		//av.docentState = new State(av.aantalFg, av.fg, av.var);
		
	}
	
	public Hashtable getState()
	{
		Hashtable h = new Hashtable();
		
		State state = av.getStateState();
		if (state != null)
		{	h.put("state", state);
			HashMap stateHM = NoSer.getStateState(state);
			h.put("stateHM", stateHM);
		}
		else
		{
			HashMap stateHM = new HashMap();
			h.put("stateHM", stateHM);
		}

		
		return h;
		
	}
	
	public Hashtable getEditState()
	{
		Hashtable h = new Hashtable();
		
		h.put("varWaardeZichtbaar", new Boolean(varWaardeZichtbaar));
		h.put("oppWaardeZichtbaar", new Boolean(oppWaardeZichtbaar));
		h.put("formuleZichtbaar", new Boolean(formuleZichtbaar));
		h.put("constructieTools", new Boolean(constructieTools));
		h.put("alleenOppervlaktes", new Boolean(alleenOppervlaktes));
		h.put("werkblad", new Boolean(werkblad));
		h.put("oppervlaktesZichtbaar", new Boolean(oppervlaktesZichtbaar));
		h.put("lengtesBreedtesZichtbaar", new Boolean(lengtesBreedtesZichtbaar));
		h.put("negatieveWaarden", new Boolean(negatieveWaarden));
		h.put("puzzelen", new Boolean(puzzelen));
		
		h.put("kijkNaActief", new Boolean(kijkNaActief));
		h.put("equivalent", new Boolean(equivalent));
		
		State state = av.getStateState();
		if (state != null)
		{	h.put("state", state);
			HashMap stateHM = NoSer.getStateState(state);
			h.put("stateHM", stateHM);
		}
		else
		{
			HashMap stateHM = new HashMap();
			h.put("stateHM", stateHM);
		}
		
		return h;
	}
	
	
	
	public InteractieEditPanel getEditPanel()
	{
		return new GAInteractieEditPanel();
	}
	
	public void zetVarWaardeZichtbaar(boolean b)
	{
		varWaardeZichtbaar = b;
		av.zetVarWaardeZichtbaar(varWaardeZichtbaar);
	}

	public void zetOppWaardeZichtbaar(boolean b)
	{
		oppWaardeZichtbaar = b;
		av.zetOppWaardeZichtbaar(oppWaardeZichtbaar);
	}
	public void zetFormuleZichtbaar(boolean b)
	{
		formuleZichtbaar = b;
		av.zetFormuleZichtbaar(formuleZichtbaar);
	}
	public void zetConstructieTools(boolean b)
	{
		constructieTools = b;
		cp.setVisible(constructieTools);
		lh.setVisible(constructieTools);
		lv.setVisible(constructieTools);
		av.zetConstructieTools(constructieTools);
		Figuur.zetGeslotenVeld(!constructieTools || alleenOppervlaktes);
	}
	public void zetAlleenOppervlaktes(boolean b)
	{	
		alleenOppervlaktes = b;
		av.zetAlleenOppervlaktes(alleenOppervlaktes);
		Figuur.zetGeslotenVeld(!constructieTools || alleenOppervlaktes);
	}
	
	public void zetWerkblad(boolean b)
	{
		werkblad = b;
		av.zetWerkBlad(werkblad);
	}
	
	public void zetOppervlaktesZichtbaar(boolean b)
	{
		oppervlaktesZichtbaar = b;
		av.zetOppervlaktesZichtbaar(oppervlaktesZichtbaar);
	}

	public void zetLengtesBreedtesZichtbaar(boolean b)
	{
		lengtesBreedtesZichtbaar = b;
		av.zetLengtesBreedtesZichtbaar(lengtesBreedtesZichtbaar);
	}
	
	public void zetNegatieveWaarden(boolean b)
	{
		negatieveWaarden = b;
		lh.zetNegatieveWaarden(negatieveWaarden);
		lv.zetNegatieveWaarden(negatieveWaarden);
		av.zetNegatieveWaarden(negatieveWaarden);
	}
	
	public void zetPuzzelen(boolean b)
	{
		puzzelen = b;
		av.zetPuzzelen(puzzelen);
	}
	
	public void zetKijkNaActief(boolean b)
	{
		kijkNaActief = b;
		kijkNaPanel.setVisible(kijkNaActief);
		kijkNaChanged = true;
		setBounds(getLocation().x, getLocation().y, getSize().width, getSize().height);
		kijkNaChanged = false;
		//av.zetKijkNaActief(kijkNaActief);
	}

	// !equivalent is gelijk
	public void zetEquivalent(boolean b)
	{
		equivalent = b;
	}
	
	public void setBounds(int x, int y, int b, int h)
	{
	
		if ((getLocation().x == x) && (getLocation().y == y) &&
			(getSize().width == b) && (getSize().height == h) && !kijkNaChanged)
				return;
		
//		System.out.println("spip set bounds " + b + " " + h);
		
		if (h == 1)
			return;
		
		super.setBounds(x, y, b, h);
		
		int breedte = b;
		int hoogte = h;
		
		if (av == null) 
		{	av = new AlgebraVeld(breedte, hoogte);
			av.setLocation(0,0);
			av.zetVarWaardeZichtbaar(varWaardeZichtbaar);
			av.zetOppWaardeZichtbaar(oppWaardeZichtbaar);
			av.zetFormuleZichtbaar(formuleZichtbaar);
			av.zetConstructieTools(constructieTools);
			av.zetAlleenOppervlaktes(alleenOppervlaktes);
			
			av.makeResetButton(resetIcon);
			
			add(av);
			
			av.addActionListener(this);
		
			Figuur.zetGeslotenVeld(!constructieTools || alleenOppervlaktes);
			Figuur.zetVeldSizes(breedte, hoogte);
		
			cp = new ControlPanel(av);
			cp.setLayout(null);
			cp.setBounds(1, hoogte - 41, breedte - 2, 40);
			if (constructieTools) 
				av.add(cp, 0);
		
			lh = new LineaalHor(breedte, hoogte);
			lh.zetNegatieveWaarden(negatieveWaarden);
			lh.addActionListener(this);
			if (constructieTools) 
				av.add(lh, 0);
		
			lv = new LineaalVer(breedte, hoogte);
			lv.zetNegatieveWaarden(negatieveWaarden);
			lv.addActionListener(this);
			if (constructieTools) 
				av.add(lv, 0);

//System.out.println("av created");			
		}
		else
		{	
			if (kijkNaActief)
			{	hoogte -= kijkNaHeight + 5; 
			}
			av.setSize(breedte, hoogte);	

			Figuur.zetGeslotenVeld(!constructieTools || alleenOppervlaktes);
			Figuur.zetVeldSizes(breedte, hoogte);
		
			cp.setBounds(1, hoogte - 41, breedte - 2, 40);
			
			av.remove(lh);
			lh = new LineaalHor(breedte, hoogte);
			lh.zetNegatieveWaarden(negatieveWaarden);
			lh.addActionListener(this);
			av.add(lh, 0);
			lh.setVisible(constructieTools);
			//if (constructieTools) 
			//	av.add(lh, 0);
			
			av.remove(lv);
			lv = new LineaalVer(breedte, hoogte);
			lv.zetNegatieveWaarden(negatieveWaarden);
			lv.addActionListener(this);
			av.add(lv, 0);
			lv.setVisible(constructieTools);			
			//if (constructieTools)
			//	av.add(lv, 0);
			
			if (gaiep == null)
			{	kijkNaPanel.setLocation((breedte - kijkNaPanel.getSize().width) / 2, 
					                     getSize().height - kijkNaHeight);
			}
			else
			{	kijkNaPanel.setLocation(breedte / 4, getSize().height - kijkNaHeight);
				
			}
			
//System.out.println("g3dc sized");		
		}
		
	}
	
	
	public void wis()
	{}
	
	public void zetMaat()
	{}
	
	public int geefAsHoogte()
	{	return 0;
	}
	
	public int getIpId()
	{	return 0;
	}
	
	public String getIpExpString()
	{	return null;
	}
	
	public int getScore()
	{	return score;
	}
	
	public int getScoreMax()
	{
		if (kijkNaActief)
			return scoreMax;
		else 
			return 0;
	}
	
	public boolean isCorrect()
	{	if (!kijkNaActief)
			return true;
		return 
			score == scoreMax;
	}
	
	public boolean isFout()
	{	if (!kijkNaActief)
			return false;
		return score == 0;
	}
	
	public void zetMode(int mode)
    {   this.mode = mode;
    	if (kijkNaActief)    
    		zetKijkNaActief(mode == 0 || mode == 1);
    }

	
	public void zetNagekeken(boolean b)
	{	if (ingevuld) 
			nagekeken = b;
	}
	
    public void stop()
    {}
    
    public void start()
    {}
    
    public void destroy()
    {}
    
    public void opnieuw()
    {}
    
    public void kijkNa()
    {
    	
//System.out.println("kijkNa");

    	if (!kijkNaActief)
    	{	
//System.out.println("!kijkNaActief");    		
    		return;
    	
    	}
    	
    	ingevuld = av.aantalFg > 0;
    	
    	if (!ingevuld)
    	{	
//System.out.println("!ingevuld");    		
    		return;
    	
    	}
    	
    	if (antwoordFormuleStringCorrect.equals(""))
    	{
//System.out.println("afs = ");    		
    		return;
    	}
    	

    	String formule = av.formule;
		if ((formule.length() > 1) && (formule.charAt(0) == '(') && (formule.charAt(formule.length() - 1) == ')'))
		{
			formule = formule.substring(1);
			formule = formule.substring(0, formule.length() - 1);
		}
				
    	
    	String antwoordFormuleStringCorrected = "$f" + antwoordFormuleStringCorrect + "@";
    	String leerlingExpressieString = "$f" + formule + "@";
    	if (leerlingExpressieString.equals(""))
    	{
//System.out.println("les = ");    		
    		return;
    	}
    	else
    	{
//System.out.println("lesstr = " + leerlingExpressieString);    		
    	}
    	
    	Expressie antwoordExpressie = FormuleParser.geefExpressie(antwoordFormuleStringCorrected);
    	Expressie leerlingExpressie = FormuleParser.geefExpressie(leerlingExpressieString);
    	
    	
    	
    	if (antwoordExpressie == null || leerlingExpressie == null)
    	{	
    		if (leerlingExpressie == null)
    		{
//System.out.println("les = null");
    		}
    		if (antwoordExpressie == null)
    		{
//System.out.println("aes = null");
    		}
    		
    		return;
    	
    	}
    	
    	boolean correct = false;
    	boolean halfCorrect = false;
    	if (equivalent && Algebra.isGelijkwaardig(antwoordExpressie, leerlingExpressie))
    	{	correct = true;
    	}
    	if (!equivalent && Algebra.zijnGelijk(antwoordExpressie, leerlingExpressie))
    	{	correct = true;
    	}
    	if (!equivalent && Algebra.isGelijkwaardig(antwoordExpressie, leerlingExpressie))
    	{	halfCorrect = true;
    	}
    	
    	if (correct)
    	{
//System.out.println("correct");

    		score = scoreMax;
    		groenVinkjeLabel.setVisible(true);
    		geelVinkjeLabel.setVisible(false);
    		kruisjeLabel.setVisible(false);
    	}
    	else if (halfCorrect)
    	{
    		score = scoreMax / 2;
    		groenVinkjeLabel.setVisible(false);
    		geelVinkjeLabel.setVisible(true);
    		kruisjeLabel.setVisible(false);
    		
    	}
    	else 
    	{
//System.out.println("not correct");    		
    		score = 0;
    		groenVinkjeLabel.setVisible(false);
    		geelVinkjeLabel.setVisible(false);
    		kruisjeLabel.setVisible(true);
    		
    	}
    	
    	
    	fireChangeEvent();
    	
    }
    
    public void fireChangeEvent()
    {	ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "changed");
		for (int lCnt = 0; lCnt < listeners.size(); lCnt++)
		{
			((ActionListener) listeners.elementAt(lCnt)).actionPerformed(event);
		}
    	
    }
    
    public void kijkNa(int stapNr)
    {
    	kijkNa();
    }
    
    public void addActionListener(ActionListener al)
    {
    	listeners.addElement(al);
    }

	public void zetBreedte(int b)
	{}
	
	public void zetHoogte(int h)
	{}
    
	public void actionPerformed(ActionEvent e)
	{
		String command = e.getActionCommand();
		int modifier = e.getModifiers();
		if (command.equals("maakBasis"))
		{	av.zetBasis(modifier);
		}
		if (command.equals("changed"))
		{
			
			if (kijkNaActief && ingevuld)
			{	
				groenVinkjeLabel.setVisible(false);
				geelVinkjeLabel.setVisible(false);
				kruisjeLabel.setVisible(false);
			
				score = 0;
			
			
				fireChangeEvent();
			}

		}
		
		if (e.getSource() == kijkNaButton)
		{
			kijkNa();
		}
		
	}
}
