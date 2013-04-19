package fi.calculatordwo;

import java.awt.event.*;
import java.awt.*;
import java.util.Hashtable;

import javax.swing.*;
import javax.swing.text.Caret;
import javax.swing.text.DefaultCaret;

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
	int cdipBreedte = 540;
	int cdipHoogte = 250;
	
	Font theFont, theLargeFont, theSmallFont;
	FontMetrics theFM, theLargeFM, theSmallFM;
	
	JButton[] getalKnop;
	JButton plusKnop, minKnop, keerKnop, deelKnop, haakLinksKnop, haakRechtsKnop,
		machtKnop, kwadraatKnop, wortelKnop;
	JButton pijlLinksKnop, pijlRechtsKnop, insKnop, delKnop, cKnop, kommaKnop, negatiefKnop, 
		ansKnop, isKnop;
	JButton sinKnop, cosKnop, tanKnop, invKnop, piKnop;
	JButton lnKnop, logKnop, eKnop, xWortelKnop;
	
	JTextField invoerVeld;
	JLabel uitvoerVeld;
	
	JPanel knoppenPanel, bovensteKnoppen, linkerKnoppen, rechterKnoppen, ondersteKnoppen;
	JPanel uitvoerPanel;
	
	String s;
	StringBuffer sb = new StringBuffer();
	StringBuffer sb2 = new StringBuffer();
	double rekenGetal;
	int lengteRekenGetal;
	double uitkomst, eindUitkomst;
	int lengteHaakjesUitdrukking;
	int linksTeller, rechtsTeller;
	
	boolean syntaxError;
	String subString;
	String bewaardeAns;
	int cp = 0;
	boolean nieuweInvoer = false;

	Color blauw, oranje, groen, geel, lichtgeel, grijs, donkergrijs;
	
	boolean wetenschappelijk = true;
	boolean invers = false;
	boolean insert = true;
	JLabel invLabel;
	
	ReplaceCaret replaceCaret;
	DefaultCaret defaultCaret;
	
	public CalculatorDwoInteractiePanel()
	{
		setLayout(new BorderLayout(5, 5));
		
		theFont = new Font("Sansserif", Font.BOLD, 16);
		theFM = getFontMetrics(theFont);
		theLargeFont = new Font("Sansserif", Font.PLAIN, 18);
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
		
		logKnop = maakButton("log", Color.orange);
		lnKnop = maakButton("ln", Color.orange);
		eKnop = maakButton("e", grijs);
		xWortelKnop = maakButton("x\u221A", Color.orange);
		
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
		logKnop.addActionListener(this);
		lnKnop.addActionListener(this);
		eKnop.addActionListener(this);
		xWortelKnop.addActionListener(this);
				
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
		
		replaceCaret = new ReplaceCaret();
		defaultCaret = new DefaultCaret();
		
		invoerVeld = new JTextField("");
		invoerVeld.setEditable(false);
		invoerVeld.setCaret(defaultCaret);
		invoerVeld.getCaret().setBlinkRate(500);
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
			ondersteKnoppen.setLayout(new GridLayout(4, 8, 3, 3));
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
		{	ondersteKnoppen.add(xWortelKnop);
			ondersteKnoppen.add(sinKnop);
		}
		
		ondersteKnoppen.add(getalKnop[4]);
		ondersteKnoppen.add(getalKnop[5]);
		ondersteKnoppen.add(getalKnop[6]);
		ondersteKnoppen.add(plusKnop);
		ondersteKnoppen.add(minKnop);
		ondersteKnoppen.add(kwadraatKnop);
		if(wetenschappelijk)
		{	ondersteKnoppen.add(logKnop);
			ondersteKnoppen.add(cosKnop);
		}
		
		ondersteKnoppen.add(getalKnop[1]);
		ondersteKnoppen.add(getalKnop[2]);
		ondersteKnoppen.add(getalKnop[3]);
		ondersteKnoppen.add(haakLinksKnop);
		ondersteKnoppen.add(haakRechtsKnop);
		ondersteKnoppen.add(machtKnop);
		if(wetenschappelijk)
		{	ondersteKnoppen.add(lnKnop);
			ondersteKnoppen.add(tanKnop);
		}
		
		ondersteKnoppen.add(getalKnop[0]);
		ondersteKnoppen.add(kommaKnop);
		ondersteKnoppen.add(negatiefKnop);
		ondersteKnoppen.add(piKnop);
		if(wetenschappelijk)
			ondersteKnoppen.add(eKnop);
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
					sb.insert(i, '0');
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
			if(sb.charAt(i) == '\u03C0' || sb.charAt(i) == 'e' || sb.charAt(i) == '(' || sb.charAt(i)=='\u221A'||sb.charAt(i) == 'A')
			{	if(sb.charAt(i-1)==')' || sb.charAt(i-1) == '\u03C0' || sb.charAt(i-1) == 'e' || Character.isDigit(sb.charAt(i-1)))
					sb.insert(i, 'x');
				if(sb.charAt(i-1) == 's' && sb.charAt(i-2) == 'n')
					sb.insert(i, 'x');
			}	
		
		//SyntaxErrors voor getal na pi, Ans en haakje sluiten
		for(int i = 0; i < sb.length() - 1; i++)
			if(sb.charAt(i) == '\u03C0' || sb.charAt(i) == 'e' || sb.charAt(i) == ')' || sb.charAt(i) == 's')
				if(Character.isDigit(sb.charAt(i+1)))
				{	syntaxError = true;
					System.out.println("Getal na pi, e, ans of haakje sluiten");
				}
		
		if(syntaxError)
		{	System.out.println("ERROR Return na maaltekens etc");
			return;
		}
		
		//pi's uitrekenen --> misschien beter nog even Math.Pi neerzetten, en pas later echt uitrekenen?
		for(int i = 0; i < sb.length(); i++)
			if(sb.charAt(i) == '\u03C0')
				sb.replace(i, i+1, "Math.PI");

		//e's uitrekenen
		for(int i = 0; i < sb.length(); i++)
			if(sb.charAt(i) == 'e')
				sb.replace(i, i+1, "Math.F"); // Niet E, want die bestaat al...
				
		//Ans invullen
		for(int i = 0; i < sb.length(); i++)
			if(sb.charAt(i) == 'A')
				sb.replace(i, i+3, bewaardeAns);
	}
	
	/*
	 * De berekenmethode; berekent wat er in de stringbuffer staat. Regelt
	 * gonioformules, logaritmes en haakjes zelf, besteedt de rest uit.
	 */
	
	public void bereken(StringBuffer sb)
	{
		//goniofuncties uitrekenen
		for(int i = 0; i < sb.length() - 1; i++)
			if(sb.charAt(i) == 's' && sb.charAt(i+1)== 'i')
			{	if(sb.charAt(i+3) == '(')
				{	vindHaakjesUitdrukking(sb, i + 3);
System.out.println("uitkomst = " + uitkomst);				
					sb.replace(i, i + lengteHaakjesUitdrukking + 4,
							Double.toString(Math.sin(uitkomst)));
				}
				else //arcsin
				{	vindHaakjesUitdrukking(sb, i + 5);
					sb.replace(i, i + lengteHaakjesUitdrukking + 6,
							Double.toString(Math.asin(uitkomst)));
				}
			}
		for(int i = 0; i < sb.length()-1; i++)
			if(sb.charAt(i) == 'c' && sb.charAt(i+1) == 'o')
			{	if(sb.charAt(i+3) == '(')
				{	vindHaakjesUitdrukking(sb, i + 3);
					sb.replace(i, i + lengteHaakjesUitdrukking + 4,
							Double.toString(Math.cos(uitkomst)));
				}
				else //arccos
				{	
					System.out.println("sb.charAt(i+3) = " + sb.charAt(i+3));
					vindHaakjesUitdrukking(sb, i + 5);
					sb.replace(i, i + lengteHaakjesUitdrukking + 6,
							Double.toString(Math.acos(uitkomst)));
				}
			}
		for(int i = 0; i < sb.length()-1; i++)
			if(sb.charAt(i) == 't' && sb.charAt(i+1) == 'a')
			{	if(sb.charAt(i+3) == '(')
				{	vindHaakjesUitdrukking(sb, i + 3);
					sb.replace(i, i + lengteHaakjesUitdrukking + 4,
							Double.toString(Math.tan(uitkomst)));
				}
				else //arctan
				{	vindHaakjesUitdrukking(sb, i + 5);
					sb.replace(i, i + lengteHaakjesUitdrukking + 6,
							Double.toString(Math.atan(uitkomst)));
				}
			}
		
		//logfuncties uitrekenen
		for(int i = 0; i < sb.length() - 1; i++)
			if(sb.charAt(i) == 'l' && sb.charAt(i+1)== 'o')//logs
			{	vindHaakjesUitdrukking(sb, i + 3);
				sb.replace(i, i + lengteHaakjesUitdrukking + 4,
							Double.toString(Math.log10(uitkomst)));
			}	
		for(int i = 0; i < sb.length() - 1; i++)
			if(sb.charAt(i) == 'l' && sb.charAt(i+1)== 'n')//lns
			{	vindHaakjesUitdrukking(sb, i + 2);
				sb.replace(i, i + lengteHaakjesUitdrukking + 3,
							Double.toString(Math.log(uitkomst)));
			}	
		
		//haakjes wegwerken (met een while statement, zolang er nog ) zijn.
		berekenTellers(sb);
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
		
	}
	
	/*
	 * berekenWaarde berekent de waarde van een expressie waarin geen haakjes, pi, e, ans, logaritmes en
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
		
		//kijken of er een E in staat (bij hele grote of hele kleine getallen)
		//werkt niet zo, want die E komt net zo hard weer terug bij het vervangen! Dus blijf je in de loop.
		//Misschien betere oplossing: zorgen dat het -teken niet schrikt als ie een E tegenkomt vlak voor zich
		// en weet wat ie dan moet doen. Eigenlijk wil je E- gewoon negeren.
		//
		//Of misschien is er eigenlijk wel meer nodig, namelijk dat ie echt kan rekenen met E.
		//Misschien moet ik dus met het rekenwerk ook echt een double bijhouden.  
		/*
		while(sb2.indexOf("E") != -1)
		{	vindGetalNaBewerking(sb2.indexOf("E"), sb2);
System.out.println(rekenGetal);		
			if(syntaxError)
			{
				System.out.println("ERROR E");
				return;
			}
			double tienmacht = Math.pow(10, rekenGetal);
			sb2.replace(sb2.indexOf("E"), sb2.indexOf("E") + lengteRekenGetal + 1, 
					"x" + Double.toString(tienmacht));
System.out.println(sb2.toString());			
		}
		*/
		
		//op zoek naar wortels
		while(sb2.indexOf("\u221A") != -1)
		{	vindGetalNaBewerking(sb2.indexOf("\u221A"), sb2);		
			if(syntaxError)
			{	
			System.out.println("ERROR wortels");
			return;
			}
			double wortel = Math.sqrt(rekenGetal);
			sb2.replace(sb2.indexOf("\u221A"), sb2.indexOf("\u221A") + lengteRekenGetal + 1, 
					Double.toString(wortel));
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
		//hier misschien een andere manier van tellen gebruiken, omdat je niet alle - weggewerkt krijgt misschien. 
		//maar het is goed als - even vaak voorkomt als E-, dus tellen hoe vaak beide voorkomen en alleen als
		//- vaker voorkomt nog op zoek gaan naar die minnen.
		while(sb2.indexOf("+") != -1 || sb2.indexOf("-") > 0)
		{	//hier regelen dat elke E- wordt overgeslagen. 
			
				//getal na E- vinden, pas daarna bewerking zoeken;
				//maar wat als dat een + is, dus een bewerking op het getal met de E
				//waar je middenin zit? Dat gaat gewoon goed als vind getal voor/na bewerking E's aankunnen.
				//Dus: in vind getal voor/na bewerking meenemen dat een getal een E kan bevatten
			
			
			
			if(sb2.indexOf("-") <= 0) 
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
			
			if(sb2.toString().equals("Math.PI"))
				uitkomst = Math.PI;
			else if(sb2.toString().equals("Math.F"))
				uitkomst = Math.E;
			else
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
		sb.replace(sb.indexOf(s)-lengte1, sb.indexOf(s)+lengte2+1, Double.toString(uitkomst));
		
	}
	
	public void vindGetalVoorBewerking(int pos, StringBuffer sb)
	{
		try{
			if(sb.charAt(pos-1) == '.')
			{	sb.deleteCharAt(pos-1);
				pos--;
			}
			
			/*
			if(sb.charAt(pos-1) == 'E')
			{	vindGetalNaBewerking(pos-1, sb);
				
			}
			*/
			
			if(Character.isDigit(sb.charAt(pos-1)))
			{	int beginPos = pos-1;
				while(beginPos >= 0 && Character.isDigit(sb.charAt(beginPos)))
					beginPos --;
				//doet het één keer te vaak:
				beginPos++;
				
				if(beginPos != 0 && sb.charAt(beginPos-1)=='.')
				{	beginPos = beginPos-2;
					while(beginPos >= 0 && Character.isDigit(sb.charAt(beginPos)))
						beginPos--;
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
				if(beginPos != 0 && sb.charAt(beginPos-1) == 'E') // dit pas als mogelijkheden e en pi ook verwerkt? Kan denk ik wel..
				{
					int beginPos2 = beginPos - 2;
					beginPos = beginPos2;
					while(beginPos >= 0 && Character.isDigit(sb.charAt(beginPos)))
						beginPos--;
					beginPos++;
					
					if(beginPos != 0 && sb.charAt(beginPos-1)=='.')
					{	beginPos = beginPos-2;
						while(beginPos >= 0 && Character.isDigit(sb.charAt(beginPos)))
							beginPos--;
						beginPos++;
					}	
					
					subString = sb.substring(beginPos, beginPos2);
					double rekenGetal2 = Double.parseDouble(subString);
					rekenGetal = rekenGetal2*Math.pow(10, rekenGetal);
					lengteRekenGetal = lengteRekenGetal + subString.length() + 1;
				}
				
			}
			else if(sb.charAt(pos - 1) == 'I')//nu moet er wel Math.PI staan
			{	rekenGetal = Math.PI;
				lengteRekenGetal = 7;//de lengte van de string Math.PI
				if(pos > 6 && sb.charAt(pos - 7) == '-')
					if(pos == 7 || sb.charAt(pos - 8) == '^'
						|| sb.charAt(pos - 8) == 'x' || sb.charAt(pos - 8) == '/' 
							|| sb.charAt(pos - 8) == '(')
					{	rekenGetal = -rekenGetal;
						lengteRekenGetal++;
					}	
			}
			else if(sb.charAt(pos - 1) == 'F') //nu moet er wel Math.F staan
			{	rekenGetal = Math.E;
				lengteRekenGetal = 6;//de lengte van de string Math.F
				if(pos > 5 && sb.charAt(pos - 6) == '-')
					if(pos == 6 || sb.charAt(pos - 7) == '^'
						|| sb.charAt(pos - 7) == 'x' || sb.charAt(pos - 7) == '/' 
							|| sb.charAt(pos - 7) == '(')
					{	rekenGetal = -rekenGetal;
						lengteRekenGetal++;
					}	
			}				
			else
			{	syntaxError = true;
				System.out.println("ERROR getalVoorBewerking else" );
				System.out.println(Math.cos(Math.PI/2));
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
		int eindPos = pos + 1;
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
			{	//int eindPos = pos+1;
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
					
			}
			else if(sb.charAt(pos + 1) == 'M' && sb.charAt(pos + 6) == 'P')//nu moet er wel Math.PI staan
			{	eindPos = pos + 7;
				rekenGetal = Math.PI;
				lengteRekenGetal = 7;//de lengte van de string Math.PI
			}
			else if(sb.charAt(pos + 1) == 'M' && sb.charAt(pos + 6) == 'F') //nu moet er wel Math.F staan
			{	eindPos = pos + 6;
				rekenGetal = Math.E;
				lengteRekenGetal = 6;//de lengte van de string Math.F
			}	
			else
			{	syntaxError = true;
				System.out.println("ERROR getalNaBewerking else");
				return;
			}
		}
		catch(Exception e){
			syntaxError = true;
			System.out.println("ERROR getalNaBewerking else");
			return;
		}
		if(negatief)
		{
			rekenGetal = - rekenGetal;
			lengteRekenGetal++;
		}
		
		if(eindPos < sb.length() - 1 && sb.charAt(eindPos + 1) == 'E')
		{
			negatief = false;
			int eindPos2 = eindPos + 2;
			eindPos = eindPos2;
			if(sb.charAt(eindPos)=='-')
			{	negatief = true;
				eindPos++;
			}
			
			while(eindPos <= sb.length() - 1 && Character.isDigit(sb.charAt(eindPos)))
				eindPos ++;
			eindPos--;
			subString = sb.substring(eindPos2, eindPos);//nog even controleren of ik het goede pak..
			double rekenGetal2 = Double.parseDouble(subString);
			if(negatief)
				rekenGetal2 = - rekenGetal2;
			rekenGetal = rekenGetal*Math.pow(10, rekenGetal2);
			lengteRekenGetal = lengteRekenGetal + subString.length() + 1;
			if(negatief)
				lengteRekenGetal++;			
		}
System.out.println("rekenGetal na bewerking is " + rekenGetal);		
	}
	
	/*
	 * Uitdrukking tussen haakjes vinden; haakje links staat op positie n.
	 * Wordt gebruikt voor gonioformules. 
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

	/*
	 * de boolean is om aan te geven of Ans moet worden toegevoegd als 
	 * een nieuwe berekening wordt gestart.
	 */
	
	public void voegTekstIn(String s, boolean ans)
	{
		if(nieuweInvoer && ans)
		{	invoerVeld.setText("Ans");
			nieuweInvoer = false;
		}
		if(nieuweInvoer && !ans)
		{	invoerVeld.setText("");
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
	
	public void vervangTekst(String s)
	{
		String str2 = invoerVeld.getText();
		cp = invoerVeld.getCaretPosition();
		
		if(cp == str2.length())
		{	invoerVeld.setText(str2 + s);
			invoerVeld.setCaretPosition(cp + s.length());
			return;
		}
		
		char testChar = str2.charAt(cp);
		// eerste stuk vast terugzetten:
		invoerVeld.setText(str2.substring(0,invoerVeld.getCaretPosition()) + s);
		
		//uitrekenen wat er verder nog terugmoet (meestal alles behalve het eerstevolgende karakter
		if(testChar=='s' || testChar == 'c' || testChar == 't' || testChar == 'A')
		{	char testChar2 = str2.charAt(cp + 3);
			if(testChar2 == '(')
			{	invoerVeld.setText(invoerVeld.getText() + str2.substring(cp + 4));
			}
			else
				invoerVeld.setText(invoerVeld.getText() + str2.substring(cp + 6));
		}
		else
			invoerVeld.setText(invoerVeld.getText() + str2.substring(cp + 1));
		
		invoerVeld.setCaretPosition(cp + s.length());
	}
	
	public void voegInOfVervang(String s, boolean ans)
	{
		if(insert)
			voegTekstIn(s, ans);
		else
			vervangTekst(s);
	}
	
	
	public void actionPerformed(ActionEvent e) {
		invoerVeld.getCaret().setVisible(true);
		String str = new String("");
		for(int i = 0; i < 10; i++)
			if(e.getSource() == getalKnop[i])
				voegInOfVervang(""+i, false);
		
		
		if(e.getSource() == piKnop)
			//voegTekstIn("\u03C0", true);
			voegInOfVervang("\u03C0", false);
		else if(e.getSource() == plusKnop)
			//voegTekstIn("+", false);
			voegInOfVervang("+", true);
		else if(e.getSource() == minKnop)
			voegInOfVervang("\u2212", true);
		else if(e.getSource() == keerKnop)
			voegInOfVervang("\u00D7", true);
		else if(e.getSource() == deelKnop)
			voegInOfVervang("\u00F7", true);
		else if(e.getSource() == wortelKnop)
			voegInOfVervang("\u221A", false);
		else if(e.getSource() == kwadraatKnop)
			voegInOfVervang("\u00B2", true);
		else if(e.getSource() == machtKnop)
			voegInOfVervang("^", true);
		else if(e.getSource() == haakLinksKnop)
			voegInOfVervang("(", false);
		else if(e.getSource() == haakRechtsKnop)
			voegInOfVervang(")", true);
		else if(e.getSource() == kommaKnop)
			voegInOfVervang(",", false);
		else if(e.getSource() == negatiefKnop)
			voegInOfVervang("-", false);
		else if(e.getSource() == ansKnop)
			voegInOfVervang("Ans", false);
		else if(e.getSource() == sinKnop)
		{	if(!invers)
				voegInOfVervang("sin(", false);
			else
			{	voegInOfVervang("sin\u207B\u00B9(", false);
				invers = false;
				invLabel.setVisible(false);
			}
		}
		else if(e.getSource() == cosKnop)
		{	if(!invers)
				voegInOfVervang("cos(", false);
			else
			{	voegInOfVervang("cos\u207B\u00B9(", false);
				invers = false;
				invLabel.setVisible(false);
			}
		}
		else if(e.getSource() == tanKnop)
		{	if(!invers)
				voegInOfVervang("tan(", false);
			else
			{	voegInOfVervang("tan\u207B\u00B9(", false);
				invers = false;
				invLabel.setVisible(false);
			}
		}
		else if(e.getSource() == logKnop)
			voegInOfVervang("log(", false);
		else if(e.getSource() == lnKnop)
			voegInOfVervang("ln(", false);
		else if(e.getSource() == eKnop)
			voegInOfVervang("e", false);
		else if(e.getSource() == xWortelKnop)
			voegInOfVervang("x\u221A", false);//dit moet nog anders, misschien getal ervoor al omhoog zetten?
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
			if(nieuweInvoer)
			{	nieuweInvoer = false; 
				invoerVeld.setCaretPosition(str.length());
			}
			cp = invoerVeld.getCaretPosition();
			if(cp == 0)
				return;
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
			{	nieuweInvoer = false;
				invoerVeld.setCaretPosition(str.length());
			}
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
			{	nieuweInvoer = false;
				invoerVeld.setCaretPosition(str.length());
			}
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
		
		else if(e.getSource() == insKnop)
		{	str = invoerVeld.getText();
			invoerVeld.getCaret().setVisible(false);
			insert = !insert;
			if(nieuweInvoer)
			{	nieuweInvoer = false;
				invoerVeld.setCaretPosition(str.length());
			}
			cp = invoerVeld.getCaretPosition();
			if(insert)
				invoerVeld.setCaret(defaultCaret);
			else
				invoerVeld.setCaret(replaceCaret);
			invoerVeld.setCaretPosition(cp);
			invoerVeld.getCaret().setBlinkRate(500);
			invoerVeld.getCaret().setVisible(true);
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
			{	try{
				eindUitkomst = Double.parseDouble(sb.toString());
				eindUitkomst = (double) Math.round(1000000000 * eindUitkomst)/1000000000;
				
				uitvoerVeld.setText(Double.toString(eindUitkomst));
				if(uitvoerVeld.getText().length() > 1 && uitvoerVeld.getText().endsWith(".0"))
					uitvoerVeld.setText(uitvoerVeld.getText().substring(0, uitvoerVeld.getText().length()-2));
				}
				catch(Exception ex) {};
			}
			nieuweInvoer = true;
			if(!insert)
			{	insert = true;
				invoerVeld.getCaret().setVisible(false);
				invoerVeld.setCaret(defaultCaret);
			}
			invoerVeld.getCaret().setBlinkRate(500);
			invoerVeld.getCaret().setVisible(false);
			
			
			
		}
		
		
	}

}
/*
 * TO DO:
 *  
 * Zorgen dat de rekenmachine overweg kan met wetenschappelijke notatie (E-5 oid)?
 * Samenhangend hiermee: EXP-knop toevoegen? 
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
