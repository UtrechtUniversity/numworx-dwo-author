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
	double rekenGetal1, rekenGetal2;
	int lengteRekenGetal1;

	
	
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
		
		pijlLinksKnop = new JButton("\u25C0");
		pijlLinksKnop.addActionListener(this);
		pijlRechtsKnop = new JButton("\u25B6");
		pijlRechtsKnop.addActionListener(this);
		//als pijltjes groter moeten: gebruik resp 25C4 en 25BA.
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
		
		//uitvoerPanel2 = new TekstVak();
		//uitvoerFormuleVak = new TekstFormuleVak(uitvoerPanel2);
		//uitvoerPanel2.add(uitvoerFormuleVak);
		//uitvoerVeld = new TekstDeelVak(uitvoerPanel2);
		
		uitvoerVak = new FormuleVak();
		//uitvoerVak.setLocation(20,2);
		uitvoerVak.setBackground(new Color(225,225,225));
		//uitvoerVak.setFont(f);
		uitvoerVak.setEditable(false);
		//uitvoerFormuleVak.add(uitvoerVak);
	
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
	
	public void bereken()
	{
		int teller1, teller2;
		
		s = invoerVeld.getText();
		sb.delete(0, sb.length());
		sb.append(s);
		
		//Alle kwadraten veranderen in ^2
		for(int i = 0; i < sb.length(); i++)
			if(sb.charAt(i) == '\u00B2')
				sb.replace(i, i+1, "^2");
		
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
		{ 	uitvoerVeld.setText("Syntax ERROR");
			return;
		}
		else if(teller1 > teller2)
			for(int i = 0; i  <teller1 - teller2; i++)
				sb.append(')');
		
		//haakjes wegwerken (met een while statement, while er nog ) zijn.
		while(teller2 > -1)
		{
			try
			{	int eindpunt = sb.indexOf(")");
				int beginpunt = sb.substring(0,eindpunt).lastIndexOf("(");
				String substring1 = sb.substring(beginpunt,eindpunt);
			}
			catch(Exception e){}
		//hier een berekening, en dan in sb de substring (incl haakjes) vervangen door het berekende stuk.
		//Evt keerteken toevoegen...
			teller2--;
		}
		
		//op zoek naar wortels
		while(sb.indexOf("\u221A") != -1)
		{	vindGetalNaBewerking(sb.indexOf("\u221A"));
			double wortel = Math.sqrt(rekenGetal1);
			sb.replace(sb.indexOf("\u221A"), sb.indexOf("\u221A")+lengteRekenGetal1, Double.toString(wortel));
		}	
		
		//op zoek naar machten
		while(sb.indexOf("^") != -1)
		{	vindGetalNaBewerking(sb.indexOf("^"));
			double exponent = rekenGetal1;
			int lengte1 = lengteRekenGetal1;
			vindGetalVoorBewerking(sb.indexOf("^"));
			double grondtal = rekenGetal1;
			int lengte2 = lengteRekenGetal1;
			double macht = Math.pow(grondtal, exponent);
			sb.replace(sb.indexOf("^")-lengte2, sb.indexOf("^")+lengte1 , Double.toString(macht));
		}
			
		
		
		
		/*
		 * Berekenen:
		 * eerst wortels
		 * Vervolgens machten.
		 */
		
		s = sb.toString();
		uitvoerVeld.setText(s);
		
		
		
		/*Expressie exp = formuleVak.geefExpressie();
		if(exp!=null && !Double.isNaN(exp.geefWaarde()) && !(exp instanceof BasisExpressie))
		{	double d = exp.geefWaarde();
			Expressie expAfgerond = new DecRound(exp, new BasisExpressie(3));
			double dAfgerond = expAfgerond.geefWaarde();
			boolean isAfronding = !Algebra.isGelijkDouble(d, dAfgerond, 0.00000000000000001);
			String s1 = formuleVak.toString();
			s1 = s1.substring(2,s1.length()-1);
			String s2 = Expressie.df3.format(dAfgerond);
			if(afgerondOp3)
			{	if(isAfronding) s2 = "\u2248" + s2;
				else s2 = "=" + s2;
			}
			else
			{
				String s = Double.toString(d);
				String[] delen = StringUtils.split(s,"E");
				if(delen.length>1) s2 = delen[0] + "*10$m" + delen[1] + "@";
				else s2 = delen[0];
				if(isAfronding) s2 = "\u2248" + s2;
				else s2 = "=" + s2;
			}
			rmAntwoordVak.vulVak("$f" + s2 + "@");
			*/
	}
	
	public void vindGetalVoorBewerking(int pos)
	{
		if(sb.charAt(pos-1) == ',')
		{	sb.deleteCharAt(pos-1);
			pos--;
		}
		
		int getal1, getal2;
	
		if(Character.isDigit(sb.charAt(pos-1)))
		{	int beginPos = pos-1;
			while(Character.isDigit(sb.charAt(beginPos)))
				beginPos --;
			getal1 = Integer.parseInt(sb.substring(beginPos + 1, pos - 1));
			if(sb.charAt(beginPos)==',')
			{	rekenGetal1 = getal1/Math.pow(10, pos - beginPos -2);
				int pos2 = beginPos;
				int beginPos2 = pos2-1;
				while(Character.isDigit(sb.charAt(beginPos2)))
					beginPos2 --;
				try{
					getal2 = Integer.parseInt(sb.substring(beginPos2 + 1, pos2 - 1));
				}
				catch(Exception e){
					getal2 = 0;
				}
				rekenGetal2 = getal2;
				rekenGetal1 = rekenGetal1 + rekenGetal2;
				lengteRekenGetal1 = pos - beginPos2;
			}
			else
			{	rekenGetal1 = getal1;
				lengteRekenGetal1 = pos - beginPos;
			}
		}
		
		else if(sb.charAt(pos-1) == '\u03C0')
		{		rekenGetal1 = Math.PI;
				lengteRekenGetal1 = 1;
		}
		
		else
			{	uitvoerVeld.setText("Syntax ERROR");
				return;
			}
		
			
	}
	
	public void vindGetalNaBewerking(int pos)
	{
		if(sb.charAt(pos+1) == ',')
		{	sb.insert(pos+1,'0');
		}
				
		int getal1, getal2;
	
		
		if(Character.isDigit(sb.charAt(pos+1)))
		{	int eindPos = pos+1;
			while(Character.isDigit(sb.charAt(eindPos)))
				eindPos ++;
			getal1 = Integer.parseInt(sb.substring(pos+1, eindPos));
			if(sb.charAt(eindPos)==',')
			{	rekenGetal1 = getal1;
				int pos2 = eindPos;
				int eindPos2 = pos2+1;
				while(Character.isDigit(sb.charAt(eindPos2)))
					eindPos2 ++;
				try{
					getal2 = Integer.parseInt(sb.substring(pos2 + 1, eindPos2 - 1));
				}
				catch(Exception e){
					getal2 = 0;
				}
				rekenGetal2 = getal2/Math.pow(10, eindPos2 - pos2 -2);
				rekenGetal1 = rekenGetal1 + rekenGetal2;
				lengteRekenGetal1 = eindPos2 - pos;
			}
			else
			{	rekenGetal1 = getal1;
				lengteRekenGetal1 = eindPos - pos;
			}
		}
		
		else if(sb.charAt(pos+1) == '\u03C0')
			{	rekenGetal1 = Math.PI;
				lengteRekenGetal1 = 1;
			}
		else
			{	uitvoerVeld.setText("Syntax ERROR");
				return;
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
			//cursor uit invoervak; als je weer begint te typen, dan verdwijnt huidige invoer.
		}
		
		
	}

}
