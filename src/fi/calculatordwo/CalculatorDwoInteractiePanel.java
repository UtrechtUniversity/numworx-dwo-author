package fi.calculatordwo;

import java.awt.event.*;
import java.awt.*;
import java.util.Hashtable;

import javax.swing.*;

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
	int cdipHoogte = 450;
	
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
	
	JButton dummyKnop;
	
	JTextField invoerVeld;
	JLabel uitvoerVeld;
	
	JPanel knoppenPanel;
	JPanel uitvoerPanel;
	
	//TekstDeelVak uitvoerVeld;
	TekstVak uitvoerPanel2;
	TekstFormuleVak uitvoerFormuleVak;
	
	FormuleVak uitvoerVak;
	
	String s;
	StringBuffer sb = new StringBuffer();
	StringBuffer sb2 = new StringBuffer();
	double rekenGetal1, rekenGetal2;
	int lengteRekenGetal1;
	StringBuffer kind1 = new StringBuffer();
	StringBuffer kind2 = new StringBuffer();
	int lengte1, lengte2;
	double uitkomst;
	Double eindUitkomst;
	
	double rekenKind1, rekenKind2;
	
	
	boolean syntaxError;
	String subString;

	
	
	public CalculatorDwoInteractiePanel()
	{
		setLayout(new BorderLayout());
		
		theFont = new Font("Dialog", Font.PLAIN, 14);
		theFM = getFontMetrics(theFont);
		theLargeFont = new Font("Dialog", Font.PLAIN, 16);
		theLargeFM = getFontMetrics(theLargeFont);
				
		getalKnop = new JButton[10];
		setFont(theLargeFont);
		for(int i = 0; i<getalKnop.length; i++)
		{	getalKnop[i] = new JButton(""+i);
			getalKnop[i].addActionListener(this);
		}
		
		plusKnop = new JButton("+");
		plusKnop.addActionListener(this);
		minKnop = new JButton("-");
		minKnop.addActionListener(this);
		keerKnop = new JButton("x");
		keerKnop.addActionListener(this);
		deelKnop = new JButton("/");
		deelKnop.addActionListener(this);
		machtKnop = new JButton("^");
		machtKnop.addActionListener(this);
		kwadraatKnop = new JButton("x\u00B2");
		kwadraatKnop.addActionListener(this);
		wortelKnop = new JButton("\u221A");
		wortelKnop.addActionListener(this);
		
		haakLinksKnop = new JButton("(");
		haakLinksKnop.addActionListener(this);
		haakRechtsKnop = new JButton(")");
		haakRechtsKnop.addActionListener(this);
		
		pijlLinksKnop = new JButton("\u25C4");
		pijlLinksKnop.addActionListener(this);
		pijlRechtsKnop = new JButton("\u25BA");
		pijlRechtsKnop.addActionListener(this);
		//als pijltjes groter moeten: gebruik resp 25C4 en 25BA.
		//als pijltjes kleiner moeten: gebruik resp 25C0 en 25B6
		insKnop = new JButton("INS");
		insKnop.addActionListener(this);
		delKnop = new JButton("DEL");
		delKnop.addActionListener(this);
		cKnop = new JButton("C");
		cKnop.addActionListener(this);
		
		kommaKnop = new JButton(",");
		kommaKnop.addActionListener(this);
		negatiefKnop = new JButton("(-)");
		negatiefKnop.addActionListener(this);
		ansKnop = new JButton("ANS");
		ansKnop.addActionListener(this);
		isKnop = new JButton("=");
		isKnop.addActionListener(this);
		
		sinKnop = new JButton("sin");
		sinKnop.addActionListener(this);
		cosKnop = new JButton("cos");
		cosKnop.addActionListener(this);
		tanKnop = new JButton("tan");
		tanKnop.addActionListener(this);
		invKnop = new JButton("INV");
		invKnop.addActionListener(this);
		piKnop = new JButton("\u03C0");
		piKnop.addActionListener(this);
		
		dummyKnop = new JButton("");
		dummyKnop.setVisible(false);
		
		knoppenPanel = new JPanel();
		knoppenPanel.setLayout(new GridLayout(5,6,2,2));
		add(knoppenPanel, BorderLayout.CENTER);
		
		knoppenPanel.add(pijlLinksKnop);
		knoppenPanel.add(pijlRechtsKnop);
		knoppenPanel.add(insKnop);
		knoppenPanel.add(delKnop);
		knoppenPanel.add(cKnop);
		knoppenPanel.add(dummyKnop);
		
		knoppenPanel.add(getalKnop[7]);
		knoppenPanel.add(getalKnop[8]);
		knoppenPanel.add(getalKnop[9]);
		knoppenPanel.add(keerKnop);
		knoppenPanel.add(deelKnop);
		knoppenPanel.add(wortelKnop);
		
		knoppenPanel.add(getalKnop[4]);
		knoppenPanel.add(getalKnop[5]);
		knoppenPanel.add(getalKnop[6]);
		knoppenPanel.add(plusKnop);
		knoppenPanel.add(minKnop);
		knoppenPanel.add(kwadraatKnop);
		
		knoppenPanel.add(getalKnop[1]);
		knoppenPanel.add(getalKnop[2]);
		knoppenPanel.add(getalKnop[3]);
		knoppenPanel.add(haakLinksKnop);
		knoppenPanel.add(haakRechtsKnop);
		knoppenPanel.add(machtKnop);
		
		knoppenPanel.add(getalKnop[0]);
		knoppenPanel.add(kommaKnop);
		knoppenPanel.add(negatiefKnop);
		knoppenPanel.add(ansKnop);
		knoppenPanel.add(isKnop);
		knoppenPanel.add(piKnop);
		
		invoerVeld = new JTextField("");
		invoerVeld.setEditable(false);
		
		
		uitvoerVeld = new JLabel("0");
		uitvoerVeld.setHorizontalAlignment(JLabel.RIGHT);
		
		uitvoerVak = new FormuleVak();
		uitvoerVak.setBackground(new Color(225,225,225));
		uitvoerVak.setEditable(false);
		
		uitvoerPanel = new JPanel();
		uitvoerPanel.setLayout(new BorderLayout());
		add(uitvoerPanel, BorderLayout.NORTH);
		
		uitvoerPanel.add(invoerVeld, BorderLayout.NORTH);
		uitvoerPanel.add(uitvoerVak, BorderLayout.SOUTH);
		uitvoerPanel.add(uitvoerVeld, BorderLayout.SOUTH);
	}
	
	public void zetOpdracht(Hashtable b, String[] randomVars,
			Hashtable randomValues) {
		// TODO Auto-generated method stub
		
	}

	public void setState(Hashtable b) {
		// TODO Auto-generated method stub
		
	}

	public void setEditState(Hashtable b) {
		// TODO Auto-generated method stub
		
	}

	public Hashtable getState() {
		// TODO Auto-generated method stub
		return null;
	}

	public Hashtable getEditState() {
		// TODO Auto-generated method stub
		return null;
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
		
		//++ veranderen in +
		while(sb2.indexOf("++") != -1)
		{	sb2.replace(sb2.indexOf("++"), sb2.indexOf("++")+2, "+");
		}
		
		//+- veranderen in -
		while(sb2.indexOf("+-") != -1)
		{	sb2.replace(sb2.indexOf("+-"), sb2.indexOf("+-")+2, "-");
		}
		
		//- veranderen in +
		while(sb2.indexOf("--") != -1)
		{	sb2.replace(sb2.indexOf("--"), sb2.indexOf("--")+2, "+");
		}
		
		//-+ veranderen in -
		while(sb2.indexOf("-+") != -1)
		{	sb2.replace(sb2.indexOf("-+"), sb2.indexOf("-+")+2, "-");
		}
		
		//op zoek naar wortels
		while(sb2.indexOf("\u221A") != -1)
		{	vindGetalNaBewerking(sb2.indexOf("\u221A"), sb2);		
			if(syntaxError)
				return;
			double wortel = Math.sqrt(rekenGetal1);
			sb2.replace(sb2.indexOf("\u221A"), sb2.indexOf("\u221A")+lengteRekenGetal1+1, Double.toString(wortel));
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
		while(sb2.indexOf("+") != -1 || sb2.indexOf("-") != -1)
		{	if(sb2.indexOf("-") == -1) 
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
		
		if(syntaxError)
			return;
		
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
		
		if(sb.substring(sb.length()-2).equals(".0"))
			sb.delete(sb.length()-2, sb.length());
		s = sb.toString();
		uitvoerVeld.setText(s);
	}
	
	public void vindUitkomst(String s, StringBuffer sb)
	{
		
		vindGetalVoorBewerking(sb.indexOf(s), sb);
		if(syntaxError)
			return;
		
		rekenKind1 = rekenGetal1;
		lengte1 = lengteRekenGetal1;
		vindGetalNaBewerking(sb.indexOf(s), sb);
		if(syntaxError)
			return;
		rekenKind2 = rekenGetal1;
		lengte2 = lengteRekenGetal1;
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
	
	/*
	public void vindUitdrVoorBewerking(int pos)
	{
		//int getal1, getal2;
		
		try{
			if(sb.charAt(pos-1) == '.')
			{	sb.deleteCharAt(pos-1);
				pos--;
			}
			
			if(Character.isDigit(sb.charAt(pos-1)))//geval dat er een getal voor de bewerking staat
			{	int beginPos = pos-1;
				while(beginPos >= 0 && Character.isDigit(sb.charAt(beginPos)))
					beginPos --;
				//doet het één keer te vaak:
				beginPos++;
				
				//getal1 = Integer.parseInt(sb.substring(beginPos, pos));
				if(beginPos != 0 && sb.charAt(beginPos-1)=='.')
				{	//rekenGetal1 = getal1/Math.pow(10, pos - beginPos);
					//int pos2 = beginPos-1;
					//int beginPos2 = pos2-1;
					beginPos = beginPos-2;
					while(beginPos >= 0 && Character.isDigit(sb.charAt(beginPos)))
						beginPos --;
					beginPos++;
				}		
				
				subString = sb.substring(beginPos, pos);
				
					//getal2 = Integer.parseInt(sb.substring(beginPos2, pos2));
					//rekenGetal2 = getal2;
					//rekenGetal1 = rekenGetal1 + rekenGetal2;
					//lengteRekenGetal1 = pos - beginPos2;
			}
			else if(sb.charAt(pos-1) == ')')//geval dat er iets tussen haakjes voor de bewerking staat.
			{	int teller = 1;
				int j = pos-1;
			while(teller > 0)
			{
				j--;
				if(sb.charAt(j)=='(')
					teller--;
				else if(sb.charAt(j)==')')
					teller++;
			}	

			subString = sb.substring(j, pos);
			}
			else
			{	syntaxError = true;
				System.out.println("Else voorBewerking");
			}
			
				//else
				//{	rekenGetal1 = getal1;
				//	lengteRekenGetal1 = pos - beginPos;
				//}
			
		}
		catch(Exception e){
			syntaxError = true;
			System.out.println("Exception voorBewerking");
		}
		
		
		
	}
	*/

	/*
	public void vindUitdrNaBewerking(int pos)
	{
		//try
		//{
			if(sb.charAt(pos+1) == '.')
			{	sb.insert(pos+1,'0');
			}
			
			if(Character.isDigit(sb.charAt(pos+1)))//geval dat er een getal na de bewerking staat
			{	int eindPos = pos+1;
				while(eindPos <= sb.length()-1 && Character.isDigit(sb.charAt(eindPos)))
					eindPos ++;
				//doet het één keer te vaak:
				eindPos--;
//System.out.println("eindPos = " + eindPos + "en length -1= " + (sb.length()-1));
								
				if(eindPos < sb.length()-1 && sb.charAt(eindPos+1)=='.')
				{	eindPos = eindPos+2;
					while(eindPos <= sb.length() - 1 && Character.isDigit(sb.charAt(eindPos)))
						eindPos ++;
					eindPos--;
				}	
//System.out.println("we komen na if");
				subString = sb.substring(pos+1, eindPos+1);
//System.out.println("we komen na substring maken");
//System.out.println(subString);
			}
			else if(sb.charAt(pos+1) == '(')//geval dat er iets tussen haakjes na de bewerking staat.
			{	int teller = 1;
				int j = pos+1;
				while(teller > 0)
				{
					j++;
					if(sb.charAt(j)==')')
						teller--;
					else if(sb.charAt(j)=='(')
						teller++;
				}	
				subString = sb.substring(pos+1, j);
			}
			else
			{	syntaxError = true;
				System.out.println("Else naBewerking");
			}
				
		//}
		//catch(Exception e){
		//	syntaxError = true;
		//	System.out.println("Exception naBewerking");
		//}
	}
*/
	
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
				rekenGetal1 = Double.parseDouble(subString);
				lengteRekenGetal1 = subString.length();
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
		try
		{	if(sb.charAt(pos+1) == '.')
			{	sb.insert(pos+1,'0');
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
				rekenGetal1 = Double.parseDouble(subString);
				lengteRekenGetal1 = subString.length();
			}
			else
				syntaxError = true;
		}
		catch(Exception e){
			syntaxError = true;
		}
		
	}


	public void actionPerformed(ActionEvent e) {
		String str = new String("");
		for(int i = 0; i < 10; i++)
			if(e.getSource() == getalKnop[i])
				invoerVeld.setText(invoerVeld.getText() + i);
			
		/*
		 * Rekenissues:
		 * Werken met een cursor. Tekst invoegen op cursor-positie... dus commando wordt
		 * anders, misschien methode voor schrijven.
		 * Zorgen voor een nette weergave van de string. Monospace lettertype?
		 */
		//kan ik een actionlistener maken die alle getallen afhandelt?
		
		if(e.getSource() == piKnop)
			invoerVeld.setText(invoerVeld.getText() + "\u03C0");
		else if(e.getSource() == plusKnop)
		 	invoerVeld.setText(invoerVeld.getText() + "+");
		else if(e.getSource() == minKnop)
		 	invoerVeld.setText(invoerVeld.getText() + "-");
		else if(e.getSource() == keerKnop)
		 	invoerVeld.setText(invoerVeld.getText() + "x");
		else if(e.getSource() == deelKnop)
		 	invoerVeld.setText(invoerVeld.getText() + "/");
		else if(e.getSource() == wortelKnop)
			invoerVeld.setText(invoerVeld.getText() + "\u221A");
		else if(e.getSource() == kwadraatKnop)
			invoerVeld.setText(invoerVeld.getText() + "\u00B2");
		else if(e.getSource() == machtKnop)
			invoerVeld.setText(invoerVeld.getText() + "^");
		else if(e.getSource() == haakLinksKnop)
		 	invoerVeld.setText(invoerVeld.getText() + "(");
		else if(e.getSource() == haakRechtsKnop)
		 	invoerVeld.setText(invoerVeld.getText() + ")");
		else if(e.getSource() == kommaKnop)
		 	invoerVeld.setText(invoerVeld.getText() + ",");
		else if(e.getSource() == negatiefKnop)
			invoerVeld.setText(invoerVeld.getText() + "-");
		else if(e.getSource() == ansKnop)
			invoerVeld.setText(invoerVeld.getText() + "Ans");
		
		
		else if(e.getSource() == cKnop)
			invoerVeld.setText("");
		else if(e.getSource() == delKnop)
		{	str = invoerVeld.getText();
			try
			{ invoerVeld.setText(str.substring(0, str.length()-1));
			}
			catch(Exception ex){}
			/* dit moet nog wel iets geraffineerder:
			 * Bij bewerking moeten ook de spaties weg.
			 * Bij dingen als Ans moet hele woord weg.
			 */
		}
		else if(e.getSource() == isKnop)
		{	bereken();
			if(syntaxError)
				uitvoerVeld.setText("Syntax ERROR");
			//cursor uit invoervak; als je weer begint te typen, dan verdwijnt huidige invoer.
		}
		
		
	}

}
