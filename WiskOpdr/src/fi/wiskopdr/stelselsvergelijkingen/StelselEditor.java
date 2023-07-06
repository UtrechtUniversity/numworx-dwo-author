package fi.wiskopdr.stelselsvergelijkingen;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import fi.beans.stringutils.StringUtils;
import fi.wiskopdr.AntwoordVergelijkingVak;
import fi.wiskopdr.ImageComponent;
import fi.wiskopdr.SimpelAntwoordVergelijkingVak;
import fi.wiskopdr.TekstVakPanel;
import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.expressies.Algebra;
import fi.wiskopdr.expressies.BasisExpressie;
import fi.wiskopdr.expressies.Expressie;
import fi.wiskopdr.expressies.Vergelijking;
import fi.wiskopdr.expressies.VergelijkingMeerv;
import fi.wiskopdr.formuleobjects.FormuleButton;
import fi.wiskopdr.formuleobjects.FormuleEditor;
import fi.wiskopdr.formuleobjects.FormuleParser;
import fi.wiskopdr.opdrnav.OpdrNavStruct;

public class StelselEditor extends AntwoordVergelijkingVak {
	
	StelselRekenVak hoofdPanel;
	private StelselEditor[] kinderen;
	private StelselEditor parent = null;
	private boolean[] oplossingGevonden;
	private String[] varNamen; 
	private Expressie[][] oplossingen; 
	private int hoogte;
	
	private boolean[][] eindOplossingGevonden;
	private boolean[][] eindOplossingStelselGevonden;
	private boolean[][] eindOplossingExactGevonden;
	private boolean bevatVoldoetNiet = false;
	
	private boolean isEindOplossing = false;
	private boolean isEindOplossingStelsel = false;
	private boolean isEindOplossingExact = false;
	
	private boolean eindOplossingNodig = true;
	private boolean onafhankelijkNodig = false;
	private boolean exactNodig = true;
	
	private boolean ingevuld = false;
	private boolean nagekeken = false;
	private boolean isGelijkwaardig = false;
	private boolean isDeelOplossing = false;
	private boolean bevatFouteOplossing = false;
	
	private boolean hasFeedback = false;
	
	private boolean correct = false;
	private boolean fout = false;
	private int score = 0;
	private int scoreMax = 0; 
	
	private boolean heeftFocus = false;
	
	private FormuleButton feedbackButton;
	
	private StelselPijl[] pijlen = null;
	
	
	public StelselEditor(StelselRekenVak hoofdPanel)
	{
		super();
		setHeader(false);
		zetStandaardOpties();
		heeftFocus = true;
		this.hoofdPanel = hoofdPanel;
		stapH = 15;
		hoogte = hoofdPanel.getHeight();
	}
	
	public StelselEditor(StelselEditor parent)
	{
		super();
		this.parent = parent;
		this.varNamen = parent.geefVarNamen();
		setHeader(false);
		zetStandaardOpties();
		zetCheck(parent.getCheck());
		hoofdPanel = parent.geefHoofdPanel();
		stapH = 15;
		hoogte = 40;
		scoreMax = parent.scoreMax;
		//oplossingenGevonden en oplossingen instellen. 
	}
	
	public void zetStandaardOpties()
	{
		zetStappen(true);
		zetPijl(false);
		zetScrollOptie(false);
		zetMetRand(false);
		zetLinkerRand();
		
		feedbackButton = new FormuleButton("?");
		feedbackButton.addActionListener(this);
		feedbackButton.setBackground(new Color(215,215,215));
		feedbackButton.setSize(15, 15);
		feedbackButton.setVisible(false);
		add(feedbackButton);
	}
	
	public void zetScoreMax(int scoreMax)
	{
		this.scoreMax = scoreMax;
	}
	
	public void zetVarNamen(String[] varNamen)
	{
		this.varNamen = varNamen;
	}
	
	public void zetOplossingen(Expressie[][] oplossingen)
	{
		this.oplossingen = oplossingen;
		eindOplossingGevonden = new boolean[oplossingen.length][varNamen.length];
		eindOplossingStelselGevonden = new boolean[oplossingen.length][varNamen.length];
		eindOplossingExactGevonden = new boolean[oplossingen.length][varNamen.length];
		for(int i = 0; i < oplossingen.length; i++)
		{
			for(int j = 0; j < varNamen.length; j++)
			{	
				eindOplossingGevonden[i][j] = false;
				eindOplossingStelselGevonden[i][j] = false;
				eindOplossingExactGevonden[i][j] = false;
			}
		}
	}
	
	public void zetOplossingen(Expressie[][] oplossingen, boolean[][] eindOplossing, boolean[][] eindOplossingStelsel, boolean[][] eindOplossingExact)
	{
		this.oplossingen = oplossingen;
		eindOplossingGevonden = eindOplossing;
		eindOplossingStelselGevonden = eindOplossingStelsel;
		eindOplossingExactGevonden = eindOplossingExact;
	}
	
	public String[] geefVarNamen()
	{
		return varNamen;
	}
	
	public StelselRekenVak geefHoofdPanel()
	{
		return hoofdPanel;
	}
	
	public void splits()
	{
		VergelijkingMeerv vergelijkingen = formuleVak.geefVergelijking();
		hoogte = bepaalHoogte();
		Component huidigIC = getHuidigIC();
		remove(huidigIC);
		hoofdPanel.zetIC(huidigIC);//.contentPanel.add(huidigIC, 0);
		huidigIC.setLocation(this.getX() + 5, this.getY() + hoogte - 30); //TODO: stapH was '20', kijken hoe het uitkomt..
		//getHuidigIC().setVisible(false);
		
		//hoogte = 100;
		
		//VergelijkingMeerv vergelijkingen = geefVergelijking(); // deze bestaat uit k vergelijkingen. 
		int k = vergelijkingen.geefAantal();
		kinderen = new StelselEditor[k];
		pijlen = new StelselPijl[k];
		int breedteVergelijkingen = this.getX() + formuleVak.getX();
		
		for(int i = 0; i < k; i++)
		{
			StelselEditor editor = new StelselEditor(this);
			Vergelijking vergelijking = vergelijkingen.geefVergelijking(i);
			int teller = 0;
			for(int j = 0; j < oplossingen.length; j++)
			{
				if(vergelijking.isOplossing(oplossingen[j], varNamen))
					teller++;
			}
			Expressie[][] oplossingenKind = new Expressie[teller][varNamen.length];
			boolean[][] eindOplossingen = new boolean[teller][varNamen.length];
			boolean[][] eindOplossingenExact = new boolean[teller][varNamen.length];
			boolean[][] eindOplossingenStelsel = new boolean[teller][varNamen.length];
			teller = 0;
			for(int j = 0; j < oplossingen.length; j++)
			{	
				if(vergelijking.isOplossing(oplossingen[j], varNamen))
				{	
					oplossingenKind[teller] = oplossingen[j];
					for(int n = 0; n < varNamen.length; n++)
					{
						eindOplossingen[teller][n] = eindOplossingGevonden[j][n];
						eindOplossingenExact[teller][n] = eindOplossingExactGevonden[j][n];
						eindOplossingenStelsel[teller][n] = eindOplossingStelselGevonden[j][n];
					}
					teller++;
				}
			}
			editor.zetOplossingen(oplossingenKind, eindOplossingen, eindOplossingenStelsel, eindOplossingenExact);
			
			kinderen[i] = editor;
			hoofdPanel.contentPanel.add(editor);
			
			editor.vulVak("$f"+ vergelijking.toString() + "@");
			int xBegin = breedteVergelijkingen + editor.geefFormuleVak().getWidth()/2;
			breedteVergelijkingen += editor.geefFormuleVak().getWidth() + 20; //+20 voor breedte van woordje 'of', moet misschien nog wat preciezer ingesteld.
			editor.vulVak("$f@");
			pijlen[i] = new StelselPijl(xBegin, editor.geefFormuleVak().getWidth()/3); //wat hier op de tweede plek staat maakt niets uit, dat regel je nog in plaatsEditors.
			hoofdPanel.contentPanel.add(pijlen[i], 0);
		}
		kinderen[0].requestFocus();
		hoofdPanel.plaatsEditors();
		
	}
	
	public void requestFocus()
	{
		if(isHoofdEditor())
			this.formuleVak = geefLaatsteFormuleVak();
		else
			hoofdPanel.geefHoofdEditor().formuleVak = this.formuleVak;
		this.formuleVak.setEditable(true);
		hoofdPanel.geefHoofdEditor().zetFocusFalse();
		heeftFocus = true;
		this.formuleVak.requestFocus();
	}
	
	public Hashtable getState()
	{
		//Hashtable h = super.getState();
		
		//boolean[] takEindes = geefTakEindes();
		int[] aantalKinderen = geefAantalKinderen();
		Vector<Integer> stapNrsVector = geefStapNrsEditorEnKinderen();
		int[] stapNrs = new int[stapNrsVector.size()];
		for(int i = 0; i < stapNrs.length; i++)
			stapNrs[i] = stapNrsVector.get(i);
		Vector<String> formuleVakInhoudenVector = geefFormuleVakInhouden();
		//String[] formuleVakInhouden;
//		if(formuleVakInhoudenVector.size() > 0)
//		{	
			String [] formuleVakInhouden = new String[formuleVakInhoudenVector.size()];
			for(int i = 0; i < formuleVakInhouden.length; i++)
				formuleVakInhouden[i] = formuleVakInhoudenVector.get(i);
//		}
//		else
//		{	formuleVakInhouden = new String[1];
//				formuleVakInhouden[0] = "$f@";
//		}
		Vector<boolean[][]> eindOplExactVector = geefEindOplExactEditorEnKinderen();
		Vector<boolean[][]> eindOplGevondenVector = geefEindOplGevondenEditorEnKinderen();
		Vector<boolean[][]> eindOplStelselVector = geefEindOplStelselEditorEnKinderen();
		boolean[][][] eindOplossingExactGevondenArrays = new boolean[eindOplExactVector.size()][][];
		boolean[][][] eindOplossingGevondenArrays = new boolean[eindOplGevondenVector.size()][][];
		boolean[][][] eindOplossingStelselGevondenArrays = new boolean[eindOplStelselVector.size()][][];
		for(int i = 0; i < eindOplExactVector.size(); i++)
		{
			eindOplossingExactGevondenArrays[i] = eindOplExactVector.get(i);
			eindOplossingGevondenArrays[i] = eindOplGevondenVector.get(i);
			eindOplossingStelselGevondenArrays[i] = eindOplStelselVector.get(i);
		}
		
		
		Hashtable h = new Hashtable();
		h.put("aantalKinderen", aantalKinderen);
		h.put("stapNrs", stapNrs);
		h.put("formuleVakInhouden", formuleVakInhouden);
		h.put("eindOplossingExactGevondenArrays", eindOplossingExactGevondenArrays);
		h.put("eindOplossingGevondenArrays", eindOplossingGevondenArrays);
		h.put("eindOplossingStelselGevondenArrays", eindOplossingStelselGevondenArrays);
		h.put("ingevuld", new Boolean(ingevuld));
		h.put("nagekeken", new Boolean(nagekeken));
		
		
		return h;
	}
	
	public void setState(Hashtable h)
	{
		int[] aantalKinderen = null;
		int[] stapNrs = null;
		String[] formuleVakInhouden = null;
		boolean[][][] eindOplossingExactGevondenArrays = null;
		boolean[][][] eindOplossingGevondenArrays = null;
		boolean[][][] eindOplossingStelselGevondenArrays = null;
		boolean ingevuld = false;
		boolean nagekeken = false;
		if(h.containsKey("aantalKinderen"))
			aantalKinderen = OpdrNavStruct.toIntArray(h.get("aantalKinderen"));
		if(h.containsKey("stapNrs"))
			stapNrs = OpdrNavStruct.toIntArray(h.get("stapNrs"));
		if(h.containsKey("formuleVakInhouden"))
			formuleVakInhouden = OpdrNavStruct.toStringArray(h.get("formuleVakInhouden"));
		if(h.containsKey("eindOplossingExactGevondenArrays"))
			eindOplossingExactGevondenArrays = (boolean[][][]) h.get("eindOplossingExactGevondenArrays");
		if(h.containsKey("eindOplossingGevondenArrays"))
			eindOplossingGevondenArrays = (boolean[][][]) h.get("eindOplossingGevondenArrays");
		if(h.containsKey("eindOplossingStelselGevondenArrays"))
			eindOplossingStelselGevondenArrays = (boolean[][][]) h.get("eindOplossingStelselGevondenArrays");
		if(h.containsKey("ingevuld"))
			ingevuld = ((Boolean) h.get("ingevuld")).booleanValue();
		if(h.containsKey("nagekeken"))
			nagekeken = ((Boolean) h.get("nagekeken")).booleanValue();
		
		this.ingevuld = ingevuld;
		this.nagekeken = nagekeken;
		
		setStateEditorEnKinderen(aantalKinderen, stapNrs, formuleVakInhouden, eindOplossingExactGevondenArrays, 
				eindOplossingGevondenArrays, eindOplossingStelselGevondenArrays, 0, 0);
	}
	
	public int[] setStateEditorEnKinderen(int[] aantalKinderen, int[] stapNrs, String[] formuleVakInhouden, 
			boolean[][][] exactArrays, boolean[][][] oplossingArrays, boolean[][][] stelselArrays, int formuleTeller, int editorTeller)
	{
		//eerst: setState van deze editor. Hashtable met geschikte info maken en super.setState aanroepen;
		Hashtable h = new Hashtable();
		int stapNr = stapNrs[editorTeller];
		String[] formuleVakInhoudenEditor = new String[stapNr + 1];
		for(int i = 0; i < stapNr + 1; i++)
			formuleVakInhoudenEditor[i] = formuleVakInhouden[formuleTeller + i];
		eindOplossingExactGevonden = exactArrays[editorTeller];
		eindOplossingGevonden = oplossingArrays[editorTeller];
		eindOplossingStelselGevonden = stelselArrays[editorTeller];
		
		//kijken of hier nog meer in moet, zoals ingevuld en nagekeken. Dan misschien toch beter h doorgeven.
		String antwoordString = formuleVakInhoudenEditor[formuleVakInhoudenEditor.length - 1];
		ingevuld = !antwoordString.equals("$f@");
		h.put("stapNr", new Integer(stapNr));
		h.put("formuleVakInhouden", formuleVakInhoudenEditor);
		h.put("ingevuld", new Boolean(ingevuld));
		h.put("nagekeken", new Boolean(nagekeken));
		h.put("antwoordString", antwoordString);
		super.setState(h);
		
		formuleTeller += stapNrs[editorTeller] + 1;
		editorTeller++;
		//dan: setStateEditorEnKinderen voor de kinderen aanroepen
		if(kinderen != null)
		{
			for(int i = 0; i < kinderen.length; i++)
			{
				int[] tellers = kinderen[i].setStateEditorEnKinderen(aantalKinderen, stapNrs, formuleVakInhouden, exactArrays, oplossingArrays, stelselArrays, formuleTeller, editorTeller);
				formuleTeller = tellers[0];
				editorTeller = tellers[1];
			}
		}
		int[] tellers = new int[2];
		tellers[0] = formuleTeller;
		tellers[1] = editorTeller;
		return tellers;
	}
	
	public int[] geefAantalKinderen()
	{
		if(kinderen == null)
			return new int[] {0};
		Vector<Integer> v = new Vector<Integer>();
		v.add(kinderen.length);
		for(int i = 0; i < kinderen.length; i++)
		{
			int[] k = kinderen[i].geefAantalKinderen();
			for(int j = 0; j < k.length; j++)
				v.add(k[j]);
		}
		int[] aantalKinderen = new int[v.size()];
		for(int i = 0; i < aantalKinderen.length; i++)
			aantalKinderen[i] = v.get(i);
		return aantalKinderen;
	}
	
	public Vector<String> geefFormuleVakInhouden()
	{
		Vector<String> v = new Vector<String>();
		for(int i = 0; i < getStapNr() + 1; i++)
		{
			if(formuleVakken[i] != null && (i == 0 || !formuleVakken[i].toString().equals("$f@"))) 
			//	v.add("$f@");
			//else 
				v.add(formuleVakken[i].toString());
		}
		if(kinderen != null)
		{
			for(int i = 0; i < kinderen.length; i++)
			{
				Vector<String> v2 = kinderen[i].geefFormuleVakInhouden();
				for(int j = 0; j < v2.size(); j++)
					v.add(v2.get(j));
			}
		}
		
		return v;
	}
	
	
	
	public Vector<Integer> geefStapNrsEditorEnKinderen()
	{
		Vector<Integer> v = new Vector<Integer>();
		
		if(kinderen != null)
		{	v.add(getStapNr());
			for(int i = 0; i < kinderen.length; i++)
			{
				Vector<Integer> v2 = kinderen[i].geefStapNrsEditorEnKinderen();
				for(int j = 0; j < v2.size(); j++)
					v.add(v2.get(j));
			}
		}
		else
		{
			int stapNr = getStapNr();
			if(stapNr > 0 && (formuleVakken[stapNr] == null || formuleVakken[stapNr].toString().equals("$f@")))
				stapNr--;
			v.add(stapNr);
		}
		return v;
	}
	
	public Vector<boolean[][]> geefEindOplExactEditorEnKinderen()
	{
		Vector<boolean[][]> v = new Vector<boolean[][]>();
		v.add(eindOplossingExactGevonden);
		if(kinderen != null)
		{
			for(int i = 0; i < kinderen.length; i++)
			{
				Vector<boolean[][]> v2 = kinderen[i].geefEindOplExactEditorEnKinderen();
				for(int j = 0; j < v2.size(); j++)
					v.add(v2.get(j));
			}
		}
		return v;
	}
	
	public Vector<boolean[][]> geefEindOplGevondenEditorEnKinderen()
	{
		Vector<boolean[][]> v = new Vector<boolean[][]>();
		v.add(eindOplossingGevonden);
		if(kinderen != null)
		{
			for(int i = 0; i < kinderen.length; i++)
			{
				Vector<boolean[][]> v2 = kinderen[i].geefEindOplGevondenEditorEnKinderen();
				for(int j = 0; j < v2.size(); j++)
					v.add(v2.get(j));
			}
		}
		return v;
	}
	
	public Vector<boolean[][]> geefEindOplStelselEditorEnKinderen()
	{
		Vector<boolean[][]> v = new Vector<boolean[][]>();
		v.add(eindOplossingStelselGevonden);
		if(kinderen != null)
		{
			for(int i = 0; i < kinderen.length; i++)
			{
				Vector<boolean[][]> v2 = kinderen[i].geefEindOplStelselEditorEnKinderen();
				for(int j = 0; j < v2.size(); j++)
					v.add(v2.get(j));
			}
		}
		return v;
	}
	
	public boolean heeftKinderen()
	{
		return kinderen != null;
	}
	
	public StelselEditor[] geefKinderen()
	{
		return kinderen;
	}
	
	public int geefHoogte()
	{
		return hoogte;
	}
	
	public int geefHoogteEditorEnKinderen()
	{
		if(kinderen == null)
			return hoogte;
		else
		{
			int maxKindHoogte = 0;
			for(int i = 0; i < kinderen.length; i++)
				maxKindHoogte = Math.max(maxKindHoogte, kinderen[i].geefHoogteEditorEnKinderen());
			return hoogte + maxKindHoogte;
		}
	}
	
	public int geefBreedte(int kolomBreedte)
	{
		if(kinderen == null)
			return kolomBreedte;
		int breedte = 0;
		for(int i = 0; i < kinderen.length; i++)
			breedte += kinderen[i].geefBreedte(kolomBreedte);			
		return breedte;
	}
	
	public int geefEindAantalKinderen()
	{
		if(kinderen == null)
			return 1;
		int aantalKinderen = 0;
		for(int i = 0; i < kinderen.length; i++)
		{
			aantalKinderen += kinderen[i].geefEindAantalKinderen();
		}
		return aantalKinderen;
	}
	
	public void kijkNa()
	{
		if (mode == 0 || mode == 1)
		{
			kijkNa(-1);
			if (ingevuld)
				produceAction("changed");
		}
	}
	
	public void kijkNa(int stapNr)
	{
		kijkNa(stapNr, true);
	}
	
	public void kijkNa(int stapNr, boolean show)
	{
		kijkNa(stapNr, show, false);
	}
	
	public void kijkNa(int stapNr, boolean show, boolean backStep)
	{
		checkAntwoord();
		
		if (!ingevuld)
		{
			if (show)
				zetGoedFout(GEEN, -1);
			if (formuleVak.toString().equals("$f@") && show)
			{
//				if ("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant))
//					remove(mwFeedbackPanel);
//				else
				remove(getFeedbackComponent());
				produceAction("feedbackWeg");
			}
			return;
		}
		
//		else if (hasFeedback)
//		{
//			if (goedHalfFout == 0)
//			{
//				if (show)
//					zetGoedFout(GOED, stapNr);
//				score = puntenFeedback;
//				correct = true;
//				fout = false;
//				if (show)
//					stapOk = false;
//				if (!pijl && show)
//					stapOk = true;
//			}
//			else if (goedHalfFout == 1)
//			{
//				if (show)
//					zetGoedFout(HALF, stapNr);
//				score = puntenFeedback;
//				correct = false;
//				fout = false;
//				if (show)
//					stapOk = true;
//			}
//			else if (goedHalfFout == 2)
//			{
//				if (show)
//					zetGoedFout(HALF, stapNr);
//				score = puntenFeedback;
//				correct = false;
//				fout = false;
//				if (show)
//					stapOk = false;
//			}
//			else if (goedHalfFout == 3)
//			{ // zetGoedFout(FOUT,stapNr);
//				//if (feedbackModus == 1 && show)
//				//	zetCorrectFoutStap(stapNr, false, true, false, "", show);
//				//else 
//				if (show)
//					zetGoedFout(FOUT, stapNr);
//				score = puntenFeedback;
//				correct = false;
//				fout = true;
//				if (show)
//					stapOk = false;
//			}
//		}
		else if (isGelijkwaardig)
		{
			
//			if (bevatFouteOplossing) // !isGelijkwaardig && isDeelOplossing &&
//										// bevatFouteOplossing
//			{
//				score = 0;
//				zetCorrectFoutStap(stapNr, false, true, false, "", show);// "
//				// foutenTeller++;
//			}
//			else if (vorm)
//			{
//				if (isJuisteVorm) // isGelijkwaardig && vorm && isJuisteVorm
//				{
//					score = puntenGelijkwaardig + puntenVorm;
//					zetCorrectFoutStap(stapNr, true, false, false, "feedbackTekst16", show);// "Dit is een correcte vergelijking"
//				}
//				else
//				// isGelijkwaardig && vorm && !isJuisteVorm
//				{
//					score = puntenGelijkwaardig;
//					zetCorrectFoutStap(stapNr, false, false, true, "feedbackTekst17", show); // "Deze vergelijking heeft (nog)niet de juiste vorm"
//				}
//			}
			if (eindOplossingNodig)
			{	if (bevatVoldoetNiet) // isGelijkwaardig && eindOplossingNodig
				{
					zetCorrectFoutStap(stapNr, false, false, false, "feedbackTekst02", show); // "Niet alle oplossingen voldoen aan de oorspronkelijke vergelijking. Verwijder de oplossingen die niet voldoen."
				}
				else if (isEindOplossing)
				{
					if (exactNodig)
					{	if (isEindOplossingExact) // isGelijkwaardig &&
													// eindOplossingNodig &&
													// isEindOplossing &&
													// exactNodig &&
													// isEindOplossingExact
						{	
							//score = puntenGelijkwaardig + puntenEindOplossing + puntenSignificant + puntenExact;
//							if (gewensteEindOplossing.isOngelijkheid())
//							{
//								zetCorrectFoutStap(stapNr, true, false, false, "feedbackTekst03", show);// "De ongelijkheid is correct opgelost"
//							}
//							else if (gewensteEindOplossing.isAfronding())
//							{
//								zetCorrectFoutStap(stapNr, true, false, false, "feedbackTekst11", show);// "De oplossing is correct afgerond"
//							}
//							else
//							{
							correct = true;
							if(hoofdPanel.geefHoofdEditor().zijnEditorOfKinderenCorrect())
							{	//TODO: feedback maken voor als oplossingenvak niet aanwezig is.
								if(hoofdPanel.geefAntwoordVak().oplossingenRegelZichtbaar)
									zetCorrectFoutStap(stapNr, true, false, false, "feedbackTekst21a", show);// "Je hebt alle oplossingen gevonden, vul ze onderaan in."
								else
								{	score = scoreMax;
									zetCorrectFoutStap(stapNr, true, false, false, "feedbackTekst21b", show);// "Je hebt alle oplossingen gevonden."
								}
								geefFocusDoor();
							}
							else
							{	zetCorrectFoutStap(stapNr, true, false, false, "feedbackTekst22", show);// "Je hebt de oplossingen in deze tak gevonden, ga verder met een andere tak."
								geefFocusDoor();
							}
						}
						else
						// isGelijkwaardig && eindOplossingNodig &&
						// isEindOplossing && exactNodig &&
						// isEindOplossingExact
						{
//							if (significantNodig)
//							{
//								if (isEindOplossingSignificant)
//								{
//									score = puntenGelijkwaardig + puntenEindOplossing + puntenSignificant;
//									zetCorrectFoutStap(stapNr, false, false, true, "feedbackTekst20", show);// "Oplossing is goed, significantie klopt maar heeft nog niet in de juiste vorm."
//								}
//								else
//								{
//									score = puntenGelijkwaardig + puntenEindOplossing;
//									zetCorrectFoutStap(stapNr, false, false, true, "feedbackTekst19", show);// "Oplossing is goed, maar nog niet in de juiste vorm en de significantie klopt niet."
//								}
//							}
//							else
//							{
								//score = puntenGelijkwaardig + puntenEindOplossing;
								zetCorrectFoutStap(stapNr, false, false, true, "feedbackTekst10", show);// "Oplossing is goed, maar nog niet in de juiste vorm."
//							}
						}
					}
					else
					// isGelijkwaardig && eindOplossingNodig &&
					// isEindOplossing && ! exactNodig
					{	
						//score = puntenGelijkwaardig + puntenEindOplossing;
//						if (gewensteEindOplossing.isOngelijkheid())
//						{
//							zetCorrectFoutStap(stapNr, true, false, false, "feedbackTekst03", show);// "De ongelijkheid is correct opgelost"
//						}
//						else if (gewensteEindOplossing.isAfronding())
//						{
//							zetCorrectFoutStap(stapNr, true, false, false, "feedbackTekst11", show);// "De oplossing is correct afgerond"
//						}
//						else
//						{
//							if (significantNodig)
//							{
//								if (isEindOplossingSignificant)
//								{
//									score = puntenGelijkwaardig + puntenEindOplossing + puntenSignificant;
//									zetCorrectFoutStap(stapNr, true, false, false, "feedbackTekst04", show);// "De vergelijking is correct opgelost"
//								}
//								else
//								{
//									score = puntenGelijkwaardig + puntenEindOplossing;
//									zetCorrectFoutStap(stapNr, false, false, true, "feedbackTekst18", show);// "De oplossing is goed, maar het aantal significante cijfers klopt niet."
//								}
//							}
//							else
//							{
								correct = true;
								score = scoreMax;
								if(hoofdPanel.geefHoofdEditor().zijnEditorOfKinderenCorrect())
								{	//TODO: feedback maken voor als oplossingenvak niet aanwezig is.
									if(hoofdPanel.geefAntwoordVak().oplossingenRegelZichtbaar)
										zetCorrectFoutStap(stapNr, true, false, false, "feedbackTekst21a", show);// "Je hebt alle oplossingen gevonden, vul ze onderaan in."
									else
										zetCorrectFoutStap(stapNr, true, false, false, "feedbackTekst21b", show);// "Je hebt alle oplossingen gevonden."
									geefFocusDoor();
								}
								else
								{	zetCorrectFoutStap(stapNr, true, false, false, "feedbackTekst22", show);// "Je hebt de oplossingen in deze tak gevonden, ga verder met een andere tak."
									geefFocusDoor();
								}
//							}
//						}
					}
				}
				else
				// isGelijkwaardig && eindOplossingNodig &&
				// !isEindOplossing
				{	
//					if (moetNogAfgerond) // isGelijkwaardig &&
//											// eindOplossingNodig &&
//											// !isEindOplossing
//					{
//						score = 0;
//						zetCorrectFoutStap(stapNr, false, false, true, "feedbackTekst05", show); // "Geef de gevraagde afronding"
//					}
//					else if (moetNogOngelijkheid) // isGelijkwaardig &&
//													// eindOplossingNodig &&
//													// !isEindOplossing
//					{
//						score = 0;
//						zetCorrectFoutStap(stapNr, false, false, true, "feedbackTekst06", show); // "Geef nu de oplossing(en) van de ongelijkheid"
//					}
//					else
//					{
						//score = puntenGelijkwaardig;
						zetCorrectFoutStap(stapNr, false, false, true, "", show);
//					}
				}
			}
			else
			// isGelijkwaardig && !vorm && !eindOplossingNodig
			{
				//score = puntenGelijkwaardig;
				// zetCorrectFoutStap(stapNr,true,false,false,"feedbackTekst16");//"Dit is een correcte vergelijking"

				// Nu kan het vak gebruikt worden als 'balans' voor het checken
				// van ware beweringen
				zetCorrectFoutStap(stapNr, false, false, true, "feedbackTekst16", show);// "Dit is een correcte vergelijking"
			}
		}
		else
		// niet isGelijkwaardig
		{	if (isDeelOplossing)
			{	if (bevatFouteOplossing) // !isGelijkwaardig && isDeelOplossing
											// && bevatFouteOplossing
				{
					//score = 0;
					zetCorrectFoutStap(stapNr, false, true, false, "feedbackTekst01", show);// "Deze stap bevat correcte en niet correcte onderdelen. Verwijder of vervang de delen die niet correct zijn"
				}
				else
				// !isGelijkwaardig && isDeelOplossing &&
				// !bevatFouteOplossing
				{
					//score = 0;
					zetCorrectFoutStap(stapNr, false, true, false, "feedbackTekst07", show);// "Er ontbreken oplossingen. Vul aan."
				}
			}
			else
			// niet isDeelOplossing
			{
			//	score = 0;
				zetCorrectFoutStap(stapNr, false, true, true, "", show);
			}
		}
		//System.out.println("this.getY = " + this.getY() + ", laatsteFormuleVak.getY = " + this.geefLaatsteFormuleVak().getY());
		feedbackButton.setBounds(30, this.geefLaatsteFormuleVak().getY() + this.geefLaatsteFormuleVak().getHeight() + 5, 15, 15);
		if(!(getFeedbackComponent().getText().equals("") || getFeedbackComponent().getText().equals("\n")))
			feedbackButton.setVisible(true);
		
		if(backStep)
			return;
		
		//TODO: opnemen dat je niet splitst als je alle oplossingen al hebt gevonden met deze laatste stap.
		if ((mode == 0 || mode == 1) && hasFeedback && !correct)
		{
			splitsOfMaakStap();
		}
		else if ((mode == 0 || mode == 1) && !hasFeedback && isGelijkwaardig && (onafhankelijkNodig && !isEindOplossingStelsel || eindOplossingNodig && !isEindOplossing || exactNodig && !isEindOplossingExact))
		{
			splitsOfMaakStap();
//			if (moetNogAfgerond)
//				formuleVakken[stapNr].vulVak("$f" + gewensteEindOplossing.geefVergelijkingVar() + "\u2248@");
		}
		else
		{	hoogte = bepaalHoogte();
			hoofdPanel.plaatsEditors();
		}
		if ((mode == 2 || mode == 3) && !formuleVak.toString().equals("$f@"))
		{
			VergelijkingMeerv antwoordIngevuld = formuleVak.geefVergelijking();
			if (antwoordIngevuld == null)
			{
				setFeedback(WiskOpdr.rb.getString("feedbackTekst09"), true);
			}
			else
			{
				splitsOfMaakStap();
				//remove(feedbackTekst);
			}
		}
		
	}
	
	public void geefFocusDoor()
	{
		if(hoofdPanel.geefHoofdEditor().zijnEditorOfKinderenCorrect())
		{
			if(hoofdPanel.geefAntwoordVak().oplossingenRegelZichtbaar)
				hoofdPanel.geefAntwoordVak().oplossingenVak.requestFocus();
		}
		else
			//volgende nog niet compleet afgeronde kind bepalen; als aan eind gekomen, dan aan begin verder, tot je weer bij dit kind komt.
			//Dit kind heeft in elk geval geen kinderen, dus eerste stap is naar parent.
		{	
			StelselEditor se = parent;
			StelselEditor se2 = this;
			while(se != null)
			{
				if(se.kinderen == null)
				{	se.requestFocus();
					return;
				}
				for(int i = 0; i < se.kinderen.length; i++)
				{	boolean kindGevonden = false;
					if(se2.equals(se.kinderen[i]))
					{
						kindGevonden = true;
					}
					else if(kindGevonden && !se.kinderen[i].zijnEditorOfKinderenCorrect())
					{
						//focus moet naar dit kind, of één van zijn kinderen/kleinkinderen etc
						se.kinderen[i].focusEersteVrijeKind();
						return;
					}
				}
				//alle volgende kinderen zijn nu kennelijk al klaar.
				//door naar parent.
				se2 = se;
				se = se.parent;
			}
			//als hier gekomen, dan is verderop geen vrije tak meer. Nu vanaf begin verder zoeken, tot aan this.
			for(int i = 0; i < hoofdPanel.geefHoofdEditor().kinderen.length; i++)
			{
				if(!hoofdPanel.geefHoofdEditor().kinderen[i].zijnEditorOfKinderenCorrect())
				{
					hoofdPanel.geefHoofdEditor().kinderen[i].focusEersteVrijeKind();
					return;
				}
					
			}
		}
	}
	
	public void focusEersteVrijeKind()
	{
		if(kinderen == null)
			requestFocus();
		else 
		{
			for(int i = 0; i < kinderen.length; i++)
			{
				if(!kinderen[i].zijnEditorOfKinderenCorrect())
				{	kinderen[i].focusEersteVrijeKind();
					return;
				}
			}
		}
	}
	
	public void zetCorrectFoutStap(int stapNr, boolean correct, boolean fout, boolean stapOk, String feedbackKey, boolean show)
	{
		this.correct = correct;
		this.fout = fout;
		super.zetCorrectFoutStap(stapNr, correct, fout, stapOk, feedbackKey, show);
		if(show)
			hoofdPanel.antwoordVak.produceAction("changed");
	}
	
	public void splitsOfMaakStap()
	{
		if(geefVergelijking() == null)
			return;
		if(isGelijkwaardig && geefVergelijking().geefAantal() > 1)
			splits();
		else
		{	super.maakStap();
			hoogte = bepaalHoogte();
			hoofdPanel.plaatsEditors();
		}
	}
	
	public void setSizes(int kolomBreedte)
	{
		int h = hoogte;
		if(kinderen == null && !isHoofdEditor()) //Kinderen (met lijnen etc) door laten lopen tot onderrand
			h = Math.max(h, hoofdPanel.getHeight() - 26 - this.getLocation().y); //corrigeren met 26 vanwege header. 
		
		this.setSize(geefBreedte(kolomBreedte), h);
		if(kinderen != null)
		{	for(int i = 0; i < kinderen.length; i++)
				kinderen[i].setSizes(kolomBreedte);
		}
	}
	
	public void setLocations()
	{
		int x = this.getLocation().x;
		int y = this.getLocation().y + hoogte;
		int breedteVergelijkingen = x + geefLaatsteFormuleVak().getX();
		StelselEditor hulpEditor = new StelselEditor(this);
		for(int i = 0; i < kinderen.length; i++)
		{	kinderen[i].setLocation(x, y);
			kinderen[i].scrollRectToVisible(new Rectangle(0, 0, 1, 1));
			if(kinderen[i].getHuidigIC() != null && kinderen[i].getHuidigIC().getParent() != null &&
					kinderen[i].getHuidigIC().getParent().equals(hoofdPanel.contentPanel))
				kinderen[i].getHuidigIC().setLocation(x + 5, y + hoogte - 20); //TODO: wat te doen met deze 20? stapH?
			hulpEditor.vulVak("$f" + geefLaatsteFormuleVak().geefVergelijking().geefVergelijking(i));
			pijlen[i].zetBeginX(breedteVergelijkingen + hulpEditor.geefFormuleVak().getWidth() / 2);
			breedteVergelijkingen += hulpEditor.geefFormuleVak().getWidth() + 20;//20 correctie voor woordje 'of'.
			pijlen[i].zetEindX(x + kinderen[i].getWidth()/3);
			pijlen[i].setLocation(Math.min(pijlen[i].xBegin, pijlen[i].xEind), y - 30);
			if(kinderen[i].heeftKinderen())
				kinderen[i].setLocations();
			x += kinderen[i].getWidth();
			
			
			
		}
	}
	
	public void checkAntwoord()
	{
		//Algebra.setTestValues(eqTestValueMin, eqTestValueMax);
		ingevuld = false;
		remove(getFeedbackComponent());
		
		//TODO: bevatVoldoetNiet bepalen.
		VergelijkingMeerv antwoord = null;
		
		String formuleVakString = formuleVak.toString();
		
		VergelijkingMeerv antwoordIngevuld = FormuleParser.parseVergelijking(formuleVakString);
		
		antwoord = antwoordIngevuld;
		
		if (antwoord != null)
		{ 	//if (!geenOplossing)
			//{
				//String antwoordIngevuldString = antwoordIngevuld.toString();
				//System.out.println("antwoordIngevuldVoor " + antwoordIngevuldString);
				
				//formuleVak.vulVak("$f" + antwoordIngevuldString + "@");
				//huidigeVergelijking = antwoord;
				
			//}
			ingevuld = true;
			
			String diffVar = "x";
			for(int i = 0; i < antwoord.geefAantal(); i++)
			{	String diffVar2 = antwoord.geefVergelijking(i).geefVarNaam();
				if(diffVar2 != null && !diffVar2.equals(""))
				{	diffVar = diffVar2;
					break;
				}
			}
//			if(FormuleParser.isDiffOperatoren())
//			{	antwoord = antwoord.vervangDifferentialen(diffVar);
//				antwoord = antwoord.vervangDiffs(gewensteEindOplossing.geefEindOplossingen(var), var);
//			}
			
			boolean isGelijkwaardigEind = antwoord.isStelselOplossing(oplossingen, varNamen);
			//boolean isGelijkwaardigEind = antwoord.isOplossing(gewensteEindOplossing.geefEindOplossingen(var), var, gewensteEindOplossing.geefVergTekens());
			

			// Hiermee wordt, in geval er geen eindoplossing is, maar wel een
			// voorlopige tussenoplossing, aan het eind gevraagd de oplossing te
			// verwerpen
//			if (gewensteEindOplossing.isOplossing(0.1234567))
//				isGelijkwaardigEind = true;
			//

			isGelijkwaardig = isGelijkwaardigEind;
//			if (gewensteTussenOplossing != null && !isGelijkwaardig)
//				isGelijkwaardig = antwoord.isOplossing(gewensteTussenOplossing.geefEindOplossingen(var), var, gewensteTussenOplossing.geefVergTekens());
//
			isEindOplossing = true;
			isEindOplossingExact = true;
			isEindOplossingStelsel = true;
			
			for(int i = 0; i < oplossingen.length; i++)
			{
				for(int j = 0; j < varNamen.length; j++)
				{	if(!eindOplossingGevonden[i][j])
					{	eindOplossingGevonden[i][j] = isGelijkwaardigEind && antwoord.isEindOplossing(oplossingen[i][j], varNamen[j], "=");
						if(!eindOplossingGevonden[i][j])
							isEindOplossing = false;
					}
					if(!eindOplossingStelselGevonden[i][j]) //TODO: kijken of hier ook nog als argument de oplossing moet worden meegegeven en zoja hoe.
					{	eindOplossingStelselGevonden[i][j] = isGelijkwaardigEind && antwoord.isStelselEindOplossing(varNamen[j], varNamen);
						if(!eindOplossingStelselGevonden[i][j])
							isEindOplossingStelsel = false;
					}
					if(!eindOplossingExactGevonden[i][j])
					{	eindOplossingExactGevonden[i][j] = isGelijkwaardigEind && antwoord.isEindOplossingExact(oplossingen[i], varNamen[j], "=");
						if(!eindOplossingExactGevonden[i][j])
							isEindOplossingExact = false;
					}
				}
			}
			
//
//			isEindOplossingSignificant = isGelijkwaardigEind && antwoord.isEindOplossingSignificant(gewensteEindOplossing.geefEindOplossingen(var), var, gewensteEindOplossing.geefVergTekens());
//
			//isEindOplossingExact = isGelijkwaardigEind && antwoord.isEindOplossingExact(gewensteEindOplossing.geefEindOplossingen(var), var, gewensteEindOplossing.geefVergTekens());
//
			isDeelOplossing = antwoord.isStelselDeelOplossing(oplossingen, varNamen);
//			if (gewensteTussenOplossing != null && !isDeelOplossing)
//				isDeelOplossing = antwoord.isDeelOplossing(gewensteTussenOplossing.geefEindOplossingen(var), var, gewensteTussenOplossing.geefVergTekens());
//
//			boolean bevatFouteOplossingEind = antwoord.bevatFouteOplossing(gewensteEindOplossing, var, gewensteEindOplossing.geefVergTekens());
			
			bevatFouteOplossing = antwoord.bevatFouteStelselOplossing(oplossingen, varNamen);
//			bevatFouteOplossing = bevatFouteOplossingEind;
//			if (gewensteTussenOplossing != null && bevatFouteOplossing)
//				bevatFouteOplossing = antwoord.bevatFouteOplossing(gewensteTussenOplossing, var, gewensteTussenOplossing.geefVergTekens());
//
//			bevatVoldoetNiet = bevatFouteOplossingEind && !bevatFouteOplossing && isEindOplossing;
//			// System.out.println(""+bevatVoldoetNiet);
//
//			moetNogAfgerond = isGelijkwaardig && !isGelijkwaardigEind && antwoord.isEindOplossing(var) && gewensteEindOplossing.toString().indexOf("\u2248") > -1;
//
//			moetNogOngelijkheid = isGelijkwaardig && !isGelijkwaardigEind && antwoord.isEindOplossing(var) && gewensteEindOplossing.isOngelijkheid();

//			isJuisteVorm = false;
//			for (int i = 0; i < juisteVormen.length; i++)
//			{
//				//isJuisteVorm = isJuisteVorm || Algebra.gelijkGevormd(antwoord, juisteVormen[i]); //in plaats hiervan antwoordIngevuld gebruiken, omdat met antwoord allerlei substituties kunnen zijn uitgevoerd.
//				isJuisteVorm = isJuisteVorm || Algebra.gelijkGevormd(antwoordIngevuld, juisteVormen[i]);
//				if (isJuisteVorm)
//					break;
//			}
			repaint();

		}
		else
		{
			isGelijkwaardig = false;
			isEindOplossing = false;
			isEindOplossingExact = false;
			isEindOplossingStelsel = false;
//			isEindOplossing = false;
//			isEindOplossingExact = false;
//			isDeelOplossing = false;
//			bevatFouteOplossing = false;
//			bevatVoldoetNiet = false;
			if (formuleVak.toString().indexOf("|") > -1)
			{ // setFeedback("Gebruik geen absoluut strepen ( bv: |x-3| )");
				setFeedback(WiskOpdr.rb.getString("feedbackTekst08"), false);
				addFeedbackComponent();
			}
			else if (formuleVak.toString().length() > 3)
			{ // setFeedback("De notatie van de vergelijking of oplossingen is niet juist");
				if (mode == 2 || mode == 3)
					ingevuld = true;
				setFeedback(WiskOpdr.rb.getString("feedbackTekst09"), false);
				addFeedbackComponent();
			}
		}
		Algebra.setDefaultTestValues();
	}
	
//	public void actionPerformed(ActionEvent e)
//	{
//		super.actionPerformed(e);
//		
//		if (e.getSource() != formuleVak || !e.getActionCommand().equals("ingevuld"))
//			return;
//		
//		
//	}
	
	public boolean isCorrect()
	{
		return correct;
	}
	
	public boolean zijnEditorOfKinderenCorrect()
	{
		if(kinderen == null && !correct)
			return false;
		else if(kinderen == null)
			return true;
		else
		{
			for(int i = 0; i < kinderen.length; i++)
			{
				if(!kinderen[i].zijnEditorOfKinderenCorrect())
					return false;
			}
		}
		return true;
	}
	
	public int getScoreEditorOfKinderen()
	{
		if(kinderen == null)
			return score;
		else
		{
			for (int i = 0; i < kinderen.length; i++)
			{
				if(kinderen[i].getScoreEditorOfKinderen() > 0)
					return kinderen[i].getScoreEditorOfKinderen();
			}
		}
		return 0;
	}
	
	
	
//	public void setNewScrollSize()
//	{
//		super.setNewScrollSize();
//		this.scrollRectToVisible(aRect);
//	}
	/*
	public void setNewScrollSize()
    {   
		//voorkomen dat al onderstaande gebeurt, of in elk geval scrollRectToVisible.
		//Waarom wilde je dit voorkomen??
    	//int maxX = 0; 
        //int maxY = 0; 
//        for(int i=0 ; i<contentPane.getComponentCount() ; i++)
//        {   Component c = contentPane.getComponent(i);
//            int b = c.getLocation().x + c.getSize().width;
//            if(b>maxX) maxX = b;
//            int h = c.getLocation().y + c.getSize().height + 20;
//            if(h>maxY) maxY = h;
//        }
//        if(scrollHorizontal)
//        {	contentPane.setPreferredSize(new Dimension(maxX,maxY));
//        }
//        else 
//        {	contentPane.setPreferredSize(new Dimension(contentPane.getSize().width-20, maxY));
//        }
//        contentPane.scrollRectToVisible(new Rectangle(0,maxY-10, contentPane.getSize().width, maxY));
//        contentPane.revalidate();
//        contentPane.doLayout();
        
    }*/
	
	public boolean isHoofdEditor()
	{
		return this.equals(hoofdPanel.geefHoofdEditor());
	}
	
	public void maakStap()
	{
		if(heeftFocus)
			splitsOfMaakStap();
		else
		{
				StelselEditor editorMetFocus = vindKindMetFocus();
				editorMetFocus.splitsOfMaakStap();
		}
	}
	
	public int bepaalHoogte()
	{
		int hoogte = super.bepaalHoogte();
		if(getFeedbackComponent() == null || !getFeedbackComponent().isShowing())
			hoogte += 20;
		return hoogte;
	}
	
	public void stapTerug()
	{
		if(heeftFocus)
		{
			if(this.getStapNr() > 0)
			{	super.stapTerug();
				return;
			}
			//nu: stapNr = 0, dus in eerste regel van de huidige editor. Deze regel leegmaken.
			if(getHuidigIC() != null)
				remove(getHuidigIC());
			formuleVak.vulVak("$f@");
			if(this.getStapNr() == 0 && !isHoofdEditor())
			{
				//focus in meest linker kolom
				if(parent.kinderen[0].heeftFocus)
				{
					for(int i = 0; i < parent.kinderen.length; i++)
					{	hoofdPanel.contentPanel.remove(parent.kinderen[i]); 
						hoofdPanel.contentPanel.remove(parent.pijlen[i]);
					}
					Component huidigIC = parent.getHuidigIC();
					hoofdPanel.contentPanel.remove(huidigIC);
					parent.kinderen = null;
					parent.pijlen = null;
					parent.hoogte = parent.bepaalHoogte();
					hoofdPanel.plaatsEditors();
					parent.requestFocus();
				}
				else
				{
					for(int i = 1; i < parent.kinderen.length; i++)
					{
						if(parent.kinderen[i].heeftFocus)
						{
							parent.kinderen[i-1].requestFocus();
							break;
						}
					}
				}
				
			}
		}
		else
		{
			StelselEditor editorMetFocus = vindKindMetFocus();
			editorMetFocus.stapTerug();
		}
	}
	
	public StelselEditor vindKindMetFocus()
	{
		if(heeftFocus)
			return this;
		else if(kinderen == null)
			return null;
		for(int i = 0; i < kinderen.length; i++)
		{
			StelselEditor kind = kinderen[i].vindKindMetFocus();
			if(kind != null)
				return kind;
		}
		return null;
	}
	
	public void zetFocusFalse()
	{
		heeftFocus = false;
		if(kinderen != null)
		{
			for(int i = 0; i < kinderen.length; i++)
				kinderen[i].zetFocusFalse();
		}
	}
	
	public void zetFocusOplossingenRegel()
	{
		zetFocusFalse();
		formuleVak = hoofdPanel.geefAntwoordVak().oplossingenVak.geefFormuleVak();
	}
	
	public void addFeedbackComponent()
	{
		Component c = getFeedbackComponent();
		hoofdPanel.contentPanel.add(c, 0);
		c.setLocation(Math.min(this.getX(), hoofdPanel.getWidth() - 200), 
				Math.min(this.getY() + this.geefLaatsteFormuleVak().getY() + this.geefLaatsteFormuleVak().getHeight() + 5, hoofdPanel.getHeight() - c.getHeight()));
	}

	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource()==feedbackButton)
		{	addFeedbackComponent();
			
			feedbackButton.setVisible(false);
		}
		else if(e.getActionCommand().equals("closeFeedback"))
		{	feedbackButton.setVisible(true);
			hoofdPanel.contentPanel.remove(getFeedbackComponent());
			hoofdPanel.repaint();
//			if(feedbackPanel.getParent()!=null)
//			{	Container c = feedbackPanel.getParent();
//				c.remove(feedbackPanel);
//				c.repaint();
//			}
//			return;
		}
		else
		{
			if(e.getActionCommand().equals("focus"))
			{
				if(isHoofdEditor())
					this.formuleVak = geefLaatsteFormuleVak();
				else
					hoofdPanel.geefHoofdEditor().formuleVak = this.formuleVak;
				this.formuleVak.setEditable(true);
				hoofdPanel.geefHoofdEditor().zetFocusFalse();
				heeftFocus = true;
			}
			if(e.getActionCommand().equals("formChanged"))
			{	feedbackButton.setVisible(false);
				hoofdPanel.contentPanel.remove(getFeedbackComponent());
				if(hoofdPanel.ic != null)
				{	hoofdPanel.contentPanel.remove(hoofdPanel.ic);
				}
				hoofdPanel.repaint();
			}
			super.actionPerformed(e);
		}
	}
	
	
	
}
