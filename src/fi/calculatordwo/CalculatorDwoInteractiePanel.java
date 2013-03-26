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
	
	Font theFont;
	FontMetrics theFM;
	Font theLargeFont;
	FontMetrics theLargeFM;
	
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
	
	boolean syntaxError;
	String subString;
	String bewaardeAns;
	int cp = 0;
	boolean nieuweInvoer = true;

	Color blauw, oranje, groen, geel, lichtgeel, grijs, donkergrijs;
	
	boolean wetenschappelijk = true;
	
	public CalculatorDwoInteractiePanel()
	{
		setLayout(new BorderLayout(5, 5));
		
		theFont = new Font("Sansserif", Font.BOLD, 16);
		theFM = getFontMetrics(theFont);
		theLargeFont = new Font("Sansserif", Font.PLAIN, 20);
		theLargeFM = getFontMetrics(theLargeFont);
		
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
		
		/*
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
		*/
		
		invoerVeld = new JTextField("");
		invoerVeld.setEditable(false);
		invoerVeld.getCaret().setVisible(true);
		invoerVeld.setBackground(lichtgeel);
		invoerVeld.setFont(theFont);
		
		
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
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		
	}

	public void zetNagekeken(boolean b) {
		// TODO Auto-generated method stub
		
	}

	public void stop() {
		// TODO Auto-generated method stub
		
	}

	public void start() {
		// TODO Auto-generated method stub
		
	}

	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	public void opnieuw() {
		// TODO Auto-generated method stub
		
	}

	public void kijkNa() {
		// TODO Auto-generated method stub
		
	}

	public void kijkNa(int stapNr) {
		// TODO Auto-generated method stub
		
	}

	public void addActionListener(ActionListener al) {
		// TODO Auto-generated method stub
		
	}
	
	public void berekenWaarde(String str) // van een expressie zonder haakjes
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
				return;
			double wortel = Math.sqrt(rekenGetal);
			sb2.replace(sb2.indexOf("\u221A"), sb2.indexOf("\u221A")+lengteRekenGetal+1, Double.toString(wortel));
		}	
		
		//op zoek naar machten
		while(sb2.indexOf("^") != -1)
		{	vindUitkomst("^", sb2);
			if(syntaxError)
				return;
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
				return;
				
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
				return;
				
		}
	}
	
	
	
	public void bereken()
	{
		syntaxError = false;
		int teller1, teller2;
		
		s = invoerVeld.getText();
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
		teller1 = 0;
		teller2 = 0;
		for(int i = 0; i < sb.length(); i++)
			if(sb.charAt(i) == '(')
				teller1++;
		for(int i = 0; i < s.length(); i++)
			if(sb.charAt(i) == ')')
				teller2++;
		if(teller2 > teller1)
			syntaxError = true;
		else if(teller1 > teller2)
			for(int i = 0; i  <teller1 - teller2; i++)
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
					syntaxError = true;
		
		if(syntaxError)
			return;
		
		//pi's uitrekenen
		for(int i = 0; i < sb.length(); i++)
			if(sb.charAt(i) == '\u03C0')
				sb.replace(i, i+1, Double.toString(Math.PI));
				
		//Ans invullen
		for(int i = 0; i < sb.length(); i++)
			if(sb.charAt(i) == 'A')
				sb.replace(i, i+3, bewaardeAns);
		
		//haakjes wegwerken (met een while statement, zolang er nog ) zijn.
		String substring1;
		while(teller2 > 0)
		{
			try
			{	int eindpunt = sb.indexOf(")");		
				int beginpunt = sb.substring(0,eindpunt).lastIndexOf("(");
				substring1 = sb.substring(beginpunt+1,eindpunt);				
				berekenWaarde(substring1);
				sb.replace(beginpunt, eindpunt+1, sb2.toString());
			}
			catch(Exception e){
				syntaxError = true;}
			teller2--;
		}
		
		try{
			berekenWaarde(sb.toString());				
			sb.replace(0, sb.length(), sb2.toString());
		}
		catch(Exception e)
		{ syntaxError = true;
		}
		
		if(sb.length()>1 && sb.substring(sb.length()-2).equals(".0"))
			sb.delete(sb.length()-2, sb.length());
		s = sb.toString();
		uitvoerVeld.setText(s);
	}
	
	public void vindUitkomst(String s, StringBuffer sb)
	{
		
		vindGetalVoorBewerking(sb.indexOf(s), sb);
		if(syntaxError)
			return;
		
		double rekenKind1 = rekenGetal;
		int lengte1 = lengteRekenGetal;
		vindGetalNaBewerking(sb.indexOf(s), sb);
		if(syntaxError)
			return;
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
	
	public void replace(StringBuffer sb, String s1, String s2)
	{
		while(sb.indexOf(s1) != -1)
		{	sb.replace(sb.indexOf(s1), sb.indexOf(s1)+s1.length(), s2);
		}
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
					}	//wortel later als nodig nog toevoegen
				
			}
			else
				syntaxError = true;
		}
		catch(Exception e){
			syntaxError = true;
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
				syntaxError = true;
		}
		catch(Exception e){
			syntaxError = true;
		}
		
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
		
		else if(e.getSource() == cKnop)
		{	if(nieuweInvoer)
				nieuweInvoer = false;
			invoerVeld.setText("");
		}
		else if(e.getSource() == delKnop)
		{	str = invoerVeld.getText();
			if(invoerVeld.getCaretPosition() == 0)
				return;
			if(nieuweInvoer)
				nieuweInvoer = false; //kijken of dit geen gekke dingen oplevert...
			else if(str.charAt(invoerVeld.getCaretPosition() - 1)=='s')
			{	cp = invoerVeld.getCaretPosition();
				invoerVeld.setText(str.substring(0, invoerVeld.getCaretPosition() - 3) + str.substring(invoerVeld.getCaretPosition(), str.length()));
				invoerVeld.setCaretPosition(cp - 3);
			}
			else
			{ 	cp = invoerVeld.getCaretPosition();
				invoerVeld.setText(str.substring(0, invoerVeld.getCaretPosition() - 1) + str.substring(invoerVeld.getCaretPosition(), str.length()));
				invoerVeld.setCaretPosition(cp - 1);
			}
		}
		else if(e.getSource() == pijlLinksKnop)
		{	str = invoerVeld.getText();
			if(nieuweInvoer)
				nieuweInvoer = false;
			if(invoerVeld.getCaretPosition() == 0)
				return;
			else if(str.charAt(invoerVeld.getCaretPosition()-1)=='s')
				invoerVeld.setCaretPosition(invoerVeld.getCaretPosition() - 3);
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
			else invoerVeld.setCaretPosition(invoerVeld.getCaretPosition() + 1);
		}
		
		else if(e.getSource() == isKnop)
		{	if(!uitvoerVeld.getText().equals("Syntax ERROR"))
				bewaardeAns = uitvoerVeld.getText();
			bereken();
			if(syntaxError)
				uitvoerVeld.setText("Syntax ERROR");
			invoerVeld.getCaret().setVisible(false);
			nieuweInvoer = true;
		}
		
		
	}

}
/*
 * Issues:
 * Zorgen dat sin, cos, tan en inv werken. Ins weglaten?? Doet nu niets...
 */
