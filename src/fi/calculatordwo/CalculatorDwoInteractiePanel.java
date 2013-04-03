package fi.calculatordwo;

import java.awt.event.*;
import java.awt.*;
import java.util.Hashtable;

import javax.swing.*;
import javax.swing.text.Caret;

import fi.beans.stringutils.*;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;

import fi.wiskopdr.expressies.Algebra;
import fi.wiskopdr.expressies.BasisExpressie;
import fi.wiskopdr.expressies.DecRound;
import fi.wiskopdr.expressies.Expressie;
import fi.wiskopdr.formuleobjects.FormuleVak;
import fi.wiskopdr.tekstobjects.*;

public class CalculatorDwoInteractiePanel  extends JPanel implements ActionListener, InteractiePanel
{
	int cdipBreedte = 500;
	int cdipHoogte = 250;
	
	Font theFont, theLargeFont, theSmallFont;
	FontMetrics theFM, theLargeFM, theSmallFM;
	
	JButton[] getalKnop;
	JButton plusKnop, minKnop, keerKnop, deelKnop, haakLinksKnop, haakRechtsKnop,
		machtKnop, kwadraatKnop, wortelKnop;
	JButton pijlLinksKnop, pijlRechtsKnop, insKnop, delKnop, cKnop, kommaKnop, negatiefKnop, 
		ansKnop, isKnop;
	JButton sinKnop, cosKnop, tanKnop, invKnop, piKnop;
	
	JTextField invoerVeld;
	JLabel uitvoerVeld;
	
	JPanel knoppenPanel, bovensteKnoppen, linkerKnoppen, rechterKnoppen, ondersteKnoppen;
	JPanel uitvoerPanel;
	
	String s;
	StringBuffer sb = new StringBuffer();
	StringBuffer sb2 = new StringBuffer();
	double rekenGetal;
	int lengteRekenGetal;
	double uitkomst;
	int lengteHaakjesUitdrukking;
	int linksTeller, rechtsTeller;
	
	boolean syntaxError;
	String subString;
	String bewaardeAns;
	int cp = 0;
	boolean nieuweInvoer = true;

	Color blauw, oranje, groen, geel, lichtgeel, grijs, donkergrijs;
	
	boolean wetenschappelijk = true;
	boolean invers = false;
	JLabel invLabel;
	
	public CalculatorDwoInteractiePanel()
	{
		setLayout(new BorderLayout(5, 5));
		
		theFont = new Font("Sansserif", Font.BOLD, 16);
		theFM = getFontMetrics(theFont);
		theLargeFont = new Font("Sansserif", Font.PLAIN, 20);
		theLargeFM = getFontMetrics(theLargeFont);
		theSmallFont = new Font("Sansserif", Font.BOLD, 6);
		theSmallFM = getFontMetrics(theSmallFont);
		
		blauw = new Color(50, 120, 255);
		oranje = new Color(255, 170, 80);
		groen = new Color(0, 150, 0);
		geel = new Color(255, 255, 180);
		lichtgeel = new Color(255, 255, 220);
		grijs = Color.lightGray;
		donkergrijs = Color.gray;
		
		getalKnop = new JButton[10];
		for(int i = 0; i<getalKnop.length; i++)
			getalKnop[i] = maakButton(""+i, donkergrijs);
		
		plusKnop = maakButton("+", grijs);
		minKnop = maakButton("\u2212", grijs);
		keerKnop = maakButton("\u00D7", grijs);
		deelKnop = maakButton("\u00F7", grijs);
		machtKnop = maakButton("^", grijs);
		kwadraatKnop = maakButton("x\u00B2", grijs);
		wortelKnop = maakButton("\u221A", grijs);
		
		haakLinksKnop = maakButton("(", grijs);
		haakRechtsKnop = maakButton(")", grijs);
		
		pijlLinksKnop = maakButton("\u25C4", blauw);
		pijlRechtsKnop = maakButton("\u25BA", blauw);
		//als pijltjes groter moeten: gebruik resp 25C4 en 25BA.
		//als pijltjes kleiner moeten: gebruik resp 25C0 en 25B6
		insKnop = maakButton("INS", blauw);
		delKnop = maakButton("DEL", blauw);
		cKnop = maakButton("C", blauw);
		
		kommaKnop = maakButton(",", grijs);
		negatiefKnop = maakButton("(-)", grijs);
		ansKnop = maakButton("Ans", grijs);
		isKnop = maakButton("=", groen);
		
		sinKnop = maakButton("sin", Color.orange);
		cosKnop = maakButton("cos", Color.orange);
		tanKnop = maakButton("tan", Color.orange);
		invKnop = maakButton("INV", Color.orange);
		piKnop = maakButton("\u03C0", grijs);
		
		for(int i = 0; i < 10; i++)
			getalKnop[i].addActionListener(this);
		plusKnop.addActionListener(this);
		minKnop.addActionListener(this);
		keerKnop.addActionListener(this);
		deelKnop.addActionListener(this);
		machtKnop.addActionListener(this);
		kwadraatKnop.addActionListener(this);
		wortelKnop.addActionListener(this);
		haakLinksKnop.addActionListener(this);
		haakRechtsKnop.addActionListener(this);
		pijlLinksKnop.addActionListener(this);
		pijlRechtsKnop.addActionListener(this);
		insKnop.addActionListener(this);
		delKnop.addActionListener(this);
		cKnop.addActionListener(this);
		kommaKnop.addActionListener(this);
		negatiefKnop.addActionListener(this);
		ansKnop.addActionListener(this);
		isKnop.addActionListener(this);
		sinKnop.addActionListener(this);
		cosKnop.addActionListener(this);
		tanKnop.addActionListener(this);
		invKnop.addActionListener(this);
		piKnop.addActionListener(this);
				
		knoppenPanel = new JPanel();
		knoppenPanel.setLayout(new BorderLayout(5,5));
		add(knoppenPanel, BorderLayout.CENTER);
		
		bovensteKnoppen = new JPanel();
		bovensteKnoppen.setLayout(new GridLayout(1, 5, 3, 3));
		knoppenPanel.add(bovensteKnoppen, BorderLayout.NORTH);
		
		bovensteKnoppen.add(pijlLinksKnop);
		bovensteKnoppen.add(pijlRechtsKnop);
		bovensteKnoppen.add(insKnop);
		bovensteKnoppen.add(delKnop);
		bovensteKnoppen.add(cKnop);
		
		ondersteKnoppen = new JPanel();
		zetWetenschappelijk(wetenschappelijk);
		
		
		invoerVeld = new JTextField("");
		invoerVeld.setEditable(false);
		invoerVeld.getCaret().setVisible(true);
		invoerVeld.setBackground(lichtgeel);
		invoerVeld.setFont(theFont);
		invoerVeld.setMargin(new Insets(8,10,3,3));

		invLabel = new JLabel("I");
		invLabel.setFont(theSmallFont);
		invLabel.setOpaque(true);
		invLabel.setBackground(Color.BLACK);
		invLabel.setForeground(lichtgeel);
		invLabel.setHorizontalAlignment(SwingConstants.CENTER);
		invLabel.setBounds(2,2,5,6);
		invLabel.setVisible(invers);
		invoerVeld.add(invLabel, 0);
		
		uitvoerVeld = new JLabel("0");
		uitvoerVeld.setHorizontalAlignment(JLabel.RIGHT);
		uitvoerVeld.setFont(theLargeFont);
		
		uitvoerPanel = new JPanel();
		uitvoerPanel.setLayout(new BorderLayout());
		uitvoerPanel.setBackground(geel);
		add(uitvoerPanel, BorderLayout.NORTH);
		
		uitvoerPanel.add(invoerVeld, BorderLayout.NORTH);
		uitvoerPanel.add(uitvoerVeld, BorderLayout.SOUTH);
	}
	
	public JButton maakButton(String s, Color c)
	{
		JButton button = new JButton(s);
		button.setFont(theLargeFont);
		button.setBackground(c);
		button.setForeground(Color.WHITE);
		return button;
	}
	
	public void zetWetenschappelijk(boolean b)
	{
		wetenschappelijk = b;
		ondersteKnoppen.removeAll();
		knoppenPanel.remove(ondersteKnoppen);
		
		if(wetenschappelijk)
			ondersteKnoppen.setLayout(new GridLayout(4, 7, 3, 3));
		else
			ondersteKnoppen.setLayout(new GridLayout(4, 6, 3, 3));
		knoppenPanel.add(ondersteKnoppen, BorderLayout.CENTER);
		
		ondersteKnoppen.add(getalKnop[7]);
		ondersteKnoppen.add(getalKnop[8]);
		ondersteKnoppen.add(getalKnop[9]);
		ondersteKnoppen.add(keerKnop);
		ondersteKnoppen.add(deelKnop);
		ondersteKnoppen.add(wortelKnop);
		if(wetenschappelijk)
			ondersteKnoppen.add(sinKnop);
		
		ondersteKnoppen.add(getalKnop[4]);
		ondersteKnoppen.add(getalKnop[5]);
		ondersteKnoppen.add(getalKnop[6]);
		ondersteKnoppen.add(plusKnop);
		ondersteKnoppen.add(minKnop);
		ondersteKnoppen.add(kwadraatKnop);
		if(wetenschappelijk)
			ondersteKnoppen.add(cosKnop);
		
		ondersteKnoppen.add(getalKnop[1]);
		ondersteKnoppen.add(getalKnop[2]);
		ondersteKnoppen.add(getalKnop[3]);
		ondersteKnoppen.add(haakLinksKnop);
		ondersteKnoppen.add(haakRechtsKnop);
		ondersteKnoppen.add(machtKnop);
		if(wetenschappelijk)
			ondersteKnoppen.add(tanKnop);
		
		ondersteKnoppen.add(getalKnop[0]);
		ondersteKnoppen.add(kommaKnop);
		ondersteKnoppen.add(negatiefKnop);
		ondersteKnoppen.add(piKnop);
		ondersteKnoppen.add(ansKnop);
		ondersteKnoppen.add(isKnop);
		if(wetenschappelijk)
			ondersteKnoppen.add(invKnop);
		
		revalidate();
	}
	
	public void zetOpdracht(Hashtable h, String[] randomVars,
			Hashtable randomValues) {
		if (h.containsKey("wetenschappelijk")) 
			wetenschappelijk = ((Boolean) h.get("wetenschappelijk")).booleanValue();
		zetWetenschappelijk(wetenschappelijk);
	}

	public void setState(Hashtable h) {
		if (h.containsKey("wetenschappelijk")) 
			wetenschappelijk = ((Boolean) h.get("wetenschappelijk")).booleanValue();
		zetWetenschappelijk(wetenschappelijk);
		
	}

	public void setEditState(Hashtable h) {
		if (h.containsKey("wetenschappelijk")) 
			wetenschappelijk = ((Boolean) h.get("wetenschappelijk")).booleanValue();
		zetWetenschappelijk(wetenschappelijk);
	}

	public Hashtable getState() {
		return null;
	}

	public Hashtable getEditState() 
	{	
		Hashtable h = new Hashtable();
		h.put("wetenschappelijk", new Boolean(wetenschappelijk));
		
		return h;
	}

	public InteractieEditPanel getEditPanel() {
		return new CalculatorDwoInteractieEditPanel();
	}

	public void wis() {}

	public void zetMaat() {}

	public int geefAsHoogte() {
		return 0;
	}

	public int getIpId() {
		return 0;
	}

	public int getScore() {
		return 0;
	}

	public int getScoreMax() {
		return 0;
	}

	public boolean isCorrect() {
		return false;
	}

	public boolean isFout() {
		return false;
	}

	public void zetMode(int mode) {
	}

	public void zetNagekeken(boolean b) {
	}

	public void stop() {
	}

	public void start() {
	}

	public void destroy() {
	}

	public void opnieuw() {
	}

	public void kijkNa() {
	}

	public void kijkNa(int stapNr) {
	}

	public void addActionListener(ActionListener al) {
	}
	
	
	
	public void maakBerekenbaar(String s)
	{
		sb.delete(0, sb.length());
		sb.append(s);
		
		//Alle kwadraten veranderen in ^2
		for(int i = 0; i < sb.length(); i++)
			if(sb.charAt(i) == '\u00B2')
				sb.replace(i, i+1, "^2");
		
		//Zorgen dat voor en na elke komma getallen staan
		for(int i = 0; i < sb.length(); i++)
			if(sb.charAt(i) == ',')
			{	if(i == 0)
					sb.insert(0, '0');
				else if(!Character.isDigit(sb.charAt(i-1)))
					sb.insert(i-1, '0');
				if(i == sb.length()-1)
					sb.append('0');
				else if(!Character.isDigit(sb.charAt(i+1)))
					sb.insert(i+1, '0');
			}
		
		//Alle komma's veranderen in punten
		for(int i = 0; i< sb.length(); i++)
			if(sb.charAt(i) == ',')
				sb.setCharAt(i, '.');
		
		//Aantal linker- en rechterhaakjes kloppend maken
		berekenTellers(sb);
		if(rechtsTeller > linksTeller)
		{	syntaxError = true;
			System.out.println("ERROR te veel rechterhaakjes");
		}
		else if(linksTeller > rechtsTeller)
			for(int i = 0; i  <linksTeller - rechtsTeller; i++)
				sb.append(')');
		
		//Maaltekens invoegen waar nodig
		for(int i = 1; i < sb.length(); i++)
			if(sb.charAt(i) == '\u03C0' || sb.charAt(i) == '(' || sb.charAt(i)=='\u221A'||sb.charAt(i) == 'A')
				if(sb.charAt(i-1)==')' || sb.charAt(i-1) == '\u03C0' || Character.isDigit(sb.charAt(i-1))||sb.charAt(i-1) == 's')
					sb.insert(i, 'x');
		
		//SyntaxErrors voor getal na pi, Ans en haakje sluiten
		for(int i = 0; i < sb.length() - 1; i++)
			if(sb.charAt(i) == '\u03C0' || sb.charAt(i) == ')' || sb.charAt(i) == 's')
				if(Character.isDigit(sb.charAt(i+1)))
				{	syntaxError = true;
					System.out.println("Getal na pi, ans of haakje sluiten");
				}
		
		if(syntaxError)
		{	System.out.println("ERROR Return na maaltekens etc");
			return;
		}
		
		//pi's uitrekenen
		for(int i = 0; i < sb.length(); i++)
			if(sb.charAt(i) == '\u03C0')
				sb.replace(i, i+1, Double.toString(Math.PI));
				
		//Ans invullen
		for(int i = 0; i < sb.length(); i++)
			if(sb.charAt(i) == 'A')
				sb.replace(i, i+3, bewaardeAns);
	}
	
	/*
	 * De berekenmethode; berekent wat er in de stringbuffer staat. Regelt met name
	 * gonioformules en haakjes zelf, besteedt de rest uit.
	 */
	
	public void bereken(StringBuffer sb)
	{
		//goniofuncties uitrekenen
		for(int i = 0; i < sb.length() - 1; i++)
			if(sb.charAt(i) == 's' && sb.charAt(i+1)== 'i')
			{	if(sb.charAt(i+3) == '(')
				{	vindHaakjesUitdrukking(sb, i + 3);
					sb.replace(i, i + lengteHaakjesUitdrukking + 4, 
							Double.toString((double)Math.round(1000000000*Math.sin(uitkomst))/1000000000));
				}
				else //arcsin
				{	vindHaakjesUitdrukking(sb, i + 5);
					sb.replace(i, i + lengteHaakjesUitdrukking + 6,
							Double.toString((double)Math.round(1000000000*Math.asin(uitkomst))/1000000000));
				}
			}
		for(int i = 0; i < sb.length()-1; i++)
			if(sb.charAt(i) == 'c' && sb.charAt(i+1) == 'o')
			{	if(sb.charAt(i+3) == '(')
				{	vindHaakjesUitdrukking(sb, i + 3);
					sb.replace(i, i + lengteHaakjesUitdrukking + 4,
							Double.toString((double)Math.round(1000000000*Math.cos(uitkomst))/1000000000));
				}
				else //arccos
				{	vindHaakjesUitdrukking(sb, i + 5);
					sb.replace(i, i + lengteHaakjesUitdrukking + 6,
							Double.toString((double)Math.round(1000000000*Math.acos(uitkomst))/1000000000));
				}
			}
		for(int i = 0; i < sb.length()-1; i++)
			if(sb.charAt(i) == 't' && sb.charAt(i+1) == 'a')
			{	if(sb.charAt(i+3) == '(')
				{	vindHaakjesUitdrukking(sb, i + 3);
					sb.replace(i, i + lengteHaakjesUitdrukking + 4,
							Double.toString((double)Math.round(1000000000*Math.tan(uitkomst))/1000000000));
				}
				else //arctan
				{	vindHaakjesUitdrukking(sb, i + 5);
					sb.replace(i, i + lengteHaakjesUitdrukking + 6,
							Double.toString((double)Math.round(1000000000*Math.atan(uitkomst))/1000000000));
				}
			}
		
System.out.println(sb.toString());		
		//haakjes wegwerken (met een while statement, zolang er nog ) zijn.
		berekenTellers(sb);
System.out.println("linksTeller = " + linksTeller + " en rechtsTeller = " + rechtsTeller);		
		String substring1;
		while(rechtsTeller > 0)
		{
			try
			{	int eindpunt = sb.indexOf(")");		
				int beginpunt = sb.substring(0,eindpunt).lastIndexOf("(");
				substring1 = sb.substring(beginpunt+1,eindpunt);				
				berekenWaarde(substring1);
				sb.replace(beginpunt, eindpunt+1, sb2.toString());
			}
			catch(Exception e){
				syntaxError = true;
				System.out.println("ERROR haakjes wegwerken");}
			rechtsTeller--;
		}
		
		try{
			berekenWaarde(sb.toString());				
			sb.replace(0, sb.length(), sb2.toString());
		}
		catch(Exception e)
		{ syntaxError = true;
		System.out.println("ERROR bereken waarde geheel");
		}
		
		if(sb.length()>1 && sb.substring(sb.length()-2).equals(".0"))
			sb.delete(sb.length()-2, sb.length());
		
	}
	
	/*
	 * berekenWaarde berekent de waarde van een expressie waarin geen haakjes, pi, ans en
	 * gonio-formules voorkomen.
	 */
	public void berekenWaarde(String str) 
	{
		sb2.delete(0, sb2.length());
		sb2.append(str);
		
		//alle minnen hetzelfde maken, en alle keertekens en gedeeld-doortekens snel leesbaar maken
		replace(sb2, "\u2212", "-");
		replace(sb2, "\u00F7", "/");
		replace(sb2, "\u00D7", "x");
		// in plaats van de regels hier voor / en x kan ik ook in het vervolg de / en x vervangen door 
		// de unicode-characters die ik erbij heb gezocht.
		
		//++ veranderen in +, etc
		replace(sb2, "++", "+");
		replace(sb2, "+-", "-");
		replace(sb2, "--", "+");
		replace(sb2, "-+", "-");
		replace(sb2, "x+", "x");
		replace(sb2, "/+", "/");
		
		//op zoek naar wortels
		while(sb2.indexOf("\u221A") != -1)
		{	vindGetalNaBewerking(sb2.indexOf("\u221A"), sb2);		
			if(syntaxError)
			{	
			System.out.println("ERROR wortels");
			return;
			}
			double wortel = Math.sqrt(rekenGetal);
			sb2.replace(sb2.indexOf("\u221A"), sb2.indexOf("\u221A")+lengteRekenGetal+1, Double.toString(wortel));
		}	
		
		//op zoek naar machten
		while(sb2.indexOf("^") != -1)
		{	vindUitkomst("^", sb2);
			if(syntaxError)
			{	System.out.println("ERROR machten");
				return;
			}
		}
			
		//op zoek naar producten en delingen
		while(sb2.indexOf("x") != -1 || sb2.indexOf("/") != -1)
		{
			if(sb2.indexOf("/") == -1) 
				vindUitkomst("x", sb2);
			else if(sb2.indexOf("x") == -1)
				vindUitkomst("/", sb2);
			else if(sb2.indexOf("x") < sb2.indexOf("/"))
				vindUitkomst("x", sb2);
			else 
				vindUitkomst("/", sb2);
			if(syntaxError)
			{	System.out.println("ERROR product/deling");
				return;
			}
		}
		
		//op zoek naar optellen en aftrekken
		while(sb2.indexOf("+") != -1 || sb2.indexOf("-") > 0)
		{	if(sb2.indexOf("-") <= 0) 
				vindUitkomst("+", sb2);
			else if(sb2.indexOf("+") == -1)
				vindUitkomst("-", sb2);
			else if(sb2.indexOf("+") < sb2.indexOf("-"))
				vindUitkomst("+", sb2);
			else 
				vindUitkomst("-", sb2);			
			if(syntaxError)
			{	System.out.println("ERROR optellen/aftrekken");
				return;
			}
		}
		try{	
			uitkomst = Double.parseDouble(sb2.toString());
		}
		catch(Exception e){
			System.out.println("uitkomstfout " + sb2.toString());			
		}
	}
	
	/*
	 * Haakjestellers berekenen; kijken of er evenveel haakjes links als rechts zijn.
	 */
	public void berekenTellers(StringBuffer sb)
	{
		linksTeller = 0;
		rechtsTeller = 0;
		for(int i = 0; i < sb.length(); i++)
			if(sb.charAt(i) == '(')
				linksTeller++;
		for(int i = 0; i < sb.length(); i++)
			if(sb.charAt(i) == ')')
				rechtsTeller++;
	}
	
	/*
	 * Voor het vervangen van symbolen, om de string beter te kunnen verwerken.
	 */
	public void replace(StringBuffer sb, String s1, String s2)
	{
		while(sb.indexOf(s1) != -1)
		{	sb.replace(sb.indexOf(s1), sb.indexOf(s1)+s1.length(), s2);
		}
	}
	
	/*
	 * Uitkomst van 'simpele' bewerkingen berekenen:
	 * +, -, *, /, ^
	 */
	public void vindUitkomst(String s, StringBuffer sb)
	{
		
		vindGetalVoorBewerking(sb.indexOf(s), sb);
		if(syntaxError)
		{	System.out.println("Syntax Error komt uit vindGetalVoorBewerking");
			return;
		}
		double rekenKind1 = rekenGetal;
		int lengte1 = lengteRekenGetal;
		vindGetalNaBewerking(sb.indexOf(s), sb);
		if(syntaxError)
		{	System.out.println("Syntax Error komt uit vindGetalNaBewerking");
			return;
		}
		double rekenKind2 = rekenGetal;
		int lengte2 = lengteRekenGetal;
		if(s.equals("+"))
			uitkomst = rekenKind1 + rekenKind2;
		else if(s.equals("-"))
			uitkomst = rekenKind1 - rekenKind2;
		else if(s.equals("x"))
			uitkomst = rekenKind1 * rekenKind2;
		else if(s.equals("/"))
			uitkomst = rekenKind1/rekenKind2;
		else if(s.equals("^"))
			uitkomst = Math.pow(rekenKind1, rekenKind2);
		uitkomst = (double) Math.round(100000000 * uitkomst)/100000000;
		sb.replace(sb.indexOf(s)-lengte1, sb.indexOf(s)+lengte2+1, Double.toString(uitkomst));
		
	}
	
	public void vindGetalVoorBewerking(int pos, StringBuffer sb)
	{
		try{
			if(sb.charAt(pos-1) == '.')
			{	sb.deleteCharAt(pos-1);
				pos--;
			}
			
			if(Character.isDigit(sb.charAt(pos-1)))
			{	int beginPos = pos-1;
				while(beginPos >= 0 && Character.isDigit(sb.charAt(beginPos)))
					beginPos --;
				//doet het één keer te vaak:
				beginPos++;
				
				if(beginPos != 0 && sb.charAt(beginPos-1)=='.')
				{	beginPos = beginPos-2;
					while(beginPos >= 0 && Character.isDigit(sb.charAt(beginPos)))
						beginPos --;
					beginPos++;
				}	
				
				subString = sb.substring(beginPos, pos);
				rekenGetal = Double.parseDouble(subString);
				lengteRekenGetal = subString.length();
				if(beginPos != 0 && sb.charAt(beginPos-1) == '-')
					if(beginPos == 1 || sb.charAt(beginPos - 2) == '^'
							|| sb.charAt(beginPos - 2) == 'x' || sb.charAt(beginPos - 2) == '/' 
								|| sb.charAt(beginPos - 2) == '(')
					{	rekenGetal = -rekenGetal;
						lengteRekenGetal++;
					}	
				
			}
			else
			{	syntaxError = true;
				System.out.println("ERROR getalVoorBewerking else");
			}
		}
		catch(Exception e){
			syntaxError = true;
			System.out.println("ERROR getalVoorBewerking Exception");
		}
	}
	
	public void vindGetalNaBewerking(int pos, StringBuffer sb)
	{
		boolean negatief = false;
		try
		{	if(sb.charAt(pos+1) == '.')
			{	sb.insert(pos+1,'0');
			}
		
			if(sb.charAt(pos+1) == '-')
			{
				pos++;
				negatief = true;
			}
			
			if(Character.isDigit(sb.charAt(pos+1)))//geval dat er een getal na de bewerking staat
			{	int eindPos = pos+1;
				while(eindPos <= sb.length()-1 && Character.isDigit(sb.charAt(eindPos)))
					eindPos ++;
				//doet het één keer te vaak:
				eindPos--;
								
				if(eindPos < sb.length()-1 && sb.charAt(eindPos+1)=='.')
				{	eindPos = eindPos+2;
					while(eindPos <= sb.length() - 1 && Character.isDigit(sb.charAt(eindPos)))
						eindPos ++;
					eindPos--;
				}
				subString = sb.substring(pos + 1, eindPos + 1);
				rekenGetal = Double.parseDouble(subString);
				lengteRekenGetal = subString.length();
				if(negatief)
				{
					rekenGetal = - rekenGetal;
					lengteRekenGetal++;
				}
			}
			else
			{	syntaxError = true;
				System.out.println("ERROR getalNaBewerking else");
			}
		}
		catch(Exception e){
			syntaxError = true;
			System.out.println("ERROR getalNaBewerking else");
		}
	}
	
	/*
	 * Uitdrukking tussen haakjes vinden; wordt gebruikt voor gonioformules. 
	 */
	public void vindHaakjesUitdrukking(StringBuffer sb, int n)
	{	int teller = 1;
		int j = n;
		while(teller > 0)
		{	j++;
			if(sb.charAt(j) == '(')
				teller++;
			else if(sb.charAt(j) == ')')
				teller --;
		}
		StringBuffer sb3 = new StringBuffer();
		sb3.append(sb.substring(n + 1, j));
System.out.println("sb3 = " + sb3.toString());		
		bereken(sb3);
		lengteHaakjesUitdrukking = j - n;
	}

	public void voegTekstIn(String s, boolean b)
	{
		if(nieuweInvoer && b)
		{	invoerVeld.setText("");
			nieuweInvoer = false;
		}
		if(nieuweInvoer && !b)
		{	invoerVeld.setText("Ans");
			nieuweInvoer = false;
		}
		String str2 = invoerVeld.getText();
		if(invoerVeld.getCaretPosition() == 0)
		{	invoerVeld.setText(s + str2);
			invoerVeld.setCaretPosition(s.length());
		}
		else if(invoerVeld.getCaretPosition() == str2.length())
			invoerVeld.setText(str2 + s);
		else
		{	cp = invoerVeld.getCaretPosition();
			invoerVeld.setText(str2.substring(0,invoerVeld.getCaretPosition())+ s + str2.substring(invoerVeld.getCaretPosition(), str2.length()));
			invoerVeld.setCaretPosition(cp+s.length());
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		invoerVeld.getCaret().setVisible(true);
		String str = new String("");
		for(int i = 0; i < 10; i++)
			if(e.getSource() == getalKnop[i])
				voegTekstIn(""+i, true);
		
		
		if(e.getSource() == piKnop)
			voegTekstIn("\u03C0", true);
		else if(e.getSource() == plusKnop)
			voegTekstIn("+", false);
		else if(e.getSource() == minKnop)
			voegTekstIn("\u2212", false);
		else if(e.getSource() == keerKnop)
			voegTekstIn("\u00D7", false);
		else if(e.getSource() == deelKnop)
			voegTekstIn("\u00F7", false);
		else if(e.getSource() == wortelKnop)
			voegTekstIn("\u221A", true);
		else if(e.getSource() == kwadraatKnop)
			voegTekstIn("\u00B2", false);
		else if(e.getSource() == machtKnop)
			voegTekstIn("^", false);
		else if(e.getSource() == haakLinksKnop)
			voegTekstIn("(", true);
		else if(e.getSource() == haakRechtsKnop)
			voegTekstIn(")", false);
		else if(e.getSource() == kommaKnop)
			voegTekstIn(",", true);
		else if(e.getSource() == negatiefKnop)
			voegTekstIn("-", true);
		else if(e.getSource() == ansKnop)
			voegTekstIn("Ans", true);
		else if(e.getSource() == sinKnop)
		{	if(!invers)
				voegTekstIn("sin(", true);
			else
			{	voegTekstIn("sin\u207B\u00B9(", true);
				invers = false;
				invLabel.setVisible(false);
			}
		}
		else if(e.getSource() == cosKnop)
		{	if(!invers)
				voegTekstIn("cos(", true);
			else
			{	voegTekstIn("cos\u207B\u00B9(", true);
				invers = false;
				invLabel.setVisible(false);
			}
		}
		else if(e.getSource() == tanKnop)
		{	if(!invers)
				voegTekstIn("tan(", true);
			else
			{	voegTekstIn("tan\u207B\u00B9(", true);
				invers = false;
				invLabel.setVisible(false);
			}
		}
		else if(e.getSource() == invKnop)
		{	invers = !invers;
			invLabel.setVisible(invers);
		}
		else if(e.getSource() == cKnop)
		{	if(nieuweInvoer)
				nieuweInvoer = false;
			invoerVeld.setText("");
		}
		else if(e.getSource() == delKnop)
		{	str = invoerVeld.getText();
			cp = invoerVeld.getCaretPosition();
			if(cp == 0)
				return;
			if(nieuweInvoer)
				nieuweInvoer = false; //kijken of dit geen gekke dingen oplevert...
			else if(str.charAt(cp - 1) == 's')
			{	invoerVeld.setText(str.substring(0, cp - 3) + str.substring(cp, str.length()));
				invoerVeld.setCaretPosition(cp - 3);
			}
			else if(str.charAt(cp - 1) == '(' )
			{
				if(cp < 3)
				{	invoerVeld.setText(str.substring(0, cp - 1) + str.substring(cp, str.length()));
					invoerVeld.setCaretPosition(cp - 1);
				}
				else if(str.charAt(cp - 2) == '\u00B9')
				{	invoerVeld.setText(str.substring(0, cp - 6) + str.substring(cp, str.length()));
					invoerVeld.setCaretPosition(cp - 6);
				}
				else if(str.charAt(cp - 2) == 'n' || str.charAt(cp - 3) == 'o')
				{	invoerVeld.setText(str.substring(0, cp - 4) + str.substring(cp, str.length()));
					invoerVeld.setCaretPosition(cp - 4);
				}
				else
				{	invoerVeld.setText(str.substring(0, cp - 1) + str.substring(cp, str.length()));
					invoerVeld.setCaretPosition(cp - 1);
				}
			}
			else
			{ 	invoerVeld.setText(str.substring(0, cp - 1) + str.substring(cp, str.length()));
				invoerVeld.setCaretPosition(cp - 1);
			}
		}
		else if(e.getSource() == pijlLinksKnop)
		{	str = invoerVeld.getText();
			if(nieuweInvoer)
				nieuweInvoer = false;
			if(invoerVeld.getCaretPosition() == 0)
				return;
			else if(str.charAt(invoerVeld.getCaretPosition() - 1)=='s')
				invoerVeld.setCaretPosition(invoerVeld.getCaretPosition() - 3);
			else if(str.charAt(invoerVeld.getCaretPosition() - 1) == '(')
			{	if(invoerVeld.getCaretPosition() == 1 || invoerVeld.getCaretPosition() == 2)
					invoerVeld.setCaretPosition(invoerVeld.getCaretPosition() - 1);
				else if(str.charAt(invoerVeld.getCaretPosition() - 2) == 'n'||
						str.charAt(invoerVeld.getCaretPosition() - 3) == 'o')
					invoerVeld.setCaretPosition(invoerVeld.getCaretPosition() - 4);
				else if(str.charAt(invoerVeld.getCaretPosition() - 2) == '\u00B9')
					invoerVeld.setCaretPosition(invoerVeld.getCaretPosition() - 6);
				else 
					invoerVeld.setCaretPosition(invoerVeld.getCaretPosition() - 1);
			}
			else invoerVeld.setCaretPosition(invoerVeld.getCaretPosition() - 1);
		}
		else if(e.getSource() == pijlRechtsKnop)
		{	str = invoerVeld.getText();
			if(nieuweInvoer)
				nieuweInvoer = false;
			if(invoerVeld.getCaretPosition()==str.length())
				return;
			else if(str.charAt(invoerVeld.getCaretPosition())=='A')
				invoerVeld.setCaretPosition(invoerVeld.getCaretPosition() + 3);
			else if(str.charAt(invoerVeld.getCaretPosition()) == 's' ||
					str.charAt(invoerVeld.getCaretPosition()) == 'c' ||
					str.charAt(invoerVeld.getCaretPosition()) == 't')
			{	if(str.charAt(invoerVeld.getCaretPosition() + 3) == '(' )
					invoerVeld.setCaretPosition(invoerVeld.getCaretPosition() + 4);
				else
					invoerVeld.setCaretPosition(invoerVeld.getCaretPosition() + 6);
			}
			else invoerVeld.setCaretPosition(invoerVeld.getCaretPosition() + 1);
		}
		
		else if(e.getSource() == isKnop)
		{	if(!uitvoerVeld.getText().equals("Syntax ERROR"))
				bewaardeAns = uitvoerVeld.getText();
			maakBerekenbaar(invoerVeld.getText());
			syntaxError = false;
			bereken(sb);
			if(syntaxError)
				uitvoerVeld.setText("Syntax ERROR");
			else
				uitvoerVeld.setText(sb.toString());
			invoerVeld.getCaret().setVisible(false);
			nieuweInvoer = true;
		}
		
		
	}

}
/*
 * TO DO:
 *  
 * INS implementeren of weglaten. (Op mijn Casio is INS voor invoegen; als je de
 * cursor ergens neerzet en INS is niet aan, dan vervang je tekst. Met INS zet je 
 * de tekst tussen de al bestaande tekst.)
 * 
 * Zorgen dat de rekenmachine overweg kan met wetenschappelijke notatie (E-5 oid). 
 * Samenhangend hiermee: EXP-knop toevoegen?
 * 
 * Werken aan afrondingsfouten (bijv bij sin(3*pi)). 
 * 
 * log en ln toevoegen? Ook daarvoor heb ik de gebouwde 'bereken'-constructie nodig,
 * net als bij gonio.
 * 
 * xe-machts wortels toevoegen?
 * 
 * Nadenken over implementatie in de DWO.
 * 
 * In later stadium: alle print-statements weer verwijderen.
 * 
 * Nog eens kritisch (laten) kijken naar layout.
 */
