package fi.calculatordwo;

import java.awt.event.*;
import java.awt.*;
import java.util.Hashtable;
import java.util.Locale;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultCaret;

import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;
//import fi.wiskopdr.WiskOpdr;

public class CalculatorDwoInteractiePanel  extends JPanel implements ActionListener, KeyListener, MouseListener, InteractiePanel
{
	int cdipBreedte = 500;
	int cdipHoogte = 300;
	
	Font theFont, theLargeFont, theSmallFont;
	FontMetrics theFM, theLargeFM, theSmallFM;
	
	JButton[] getalKnop;
	JButton plusKnop, minKnop, keerKnop, deelKnop, haakLinksKnop, haakRechtsKnop,
		machtKnop, kwadraatKnop, wortelKnop, eenGedeeldDoorKnop, breukKnop;
	JButton pijlLinksKnop, pijlRechtsKnop, insKnop, delKnop, cKnop, kommaKnop, negatiefKnop, 
		ansKnop, isKnop;
	JButton sinKnop, cosKnop, tanKnop, invKnop, piKnop;
	JButton lnKnop, logKnop, eKnop, nWortelKnop, expKnop;
	
	//UIDefaults defaults; // om knoppen op Mac ook mooi te krijgen.
	
	JLabel[] leegLabel;
	JLabel sinInvLabel, cosInvLabel, tanInvLabel, decLabel;
	
	JTextField invoerVeld;
	JLabel uitvoerVeld;
	
	JPanel knoppenPanel, bovensteKnoppen, linkerKnoppen, rechterKnoppen, ondersteKnoppen;
	JPanel uitvoerPanel, instellingenPanel;
	
	String s;
	StringBuffer sb = new StringBuffer();
	StringBuffer sb2 = new StringBuffer();
	double rekenGetal;
	int lengteRekenGetal, lengte1, lengte2, lengteBreuk, lengteBreukB;
	double teller, noemer, tellerB, noemerB;
	double uitkomst, eindUitkomst;
	int lengteHaakjesUitdrukking;
	int linksTeller, rechtsTeller;
	
	JRadioButton gradenButton, radialenButton;
	ButtonGroup groep;
	
	boolean syntaxError;//, mathError;
	String subString;
	String bewaardeAns;
	int cp = 0;
	boolean nieuweInvoer = false;

	Color blauw, oranje, groen, geel, lichtgeel, grijs, donkergrijs, lichtblauw, witblauw;
	
	boolean breuk = false;
	int rmMode = 1;
	boolean gradenInstelbaar = true;
	boolean gonioKnoppen = true;
	boolean logaritmeKnoppen = true;
	
	boolean graden = false;
	boolean invers = false;
	boolean insert = true;
	JLabel invLabel;
	
	ReplaceCaret replaceCaret;
	DefaultCaret defaultCaret;
	
	int kc;
	
	public CalculatorDwoInteractiePanel()
	{
		setLayout(new BorderLayout(5, 5));
		
		theFont = new Font("Sansserif", Font.BOLD, 16);
		theFM = getFontMetrics(theFont);
		theLargeFont = new Font("Sansserif", Font.BOLD, 14);
		theLargeFM = getFontMetrics(theLargeFont);
		theSmallFont = new Font("Sansserif", Font.BOLD, 6);
		theSmallFM = getFontMetrics(theSmallFont);
		
		blauw = new Color(130, 180, 255);
		oranje = new Color(255, 170, 80);
		groen = new Color(0, 150, 0);
		lichtblauw = new Color(208, 228, 255);
		witblauw = new Color(245, 250, 255);
		geel = new Color(255, 255, 180);
		lichtgeel = new Color(255, 255, 220);
		grijs = Color.gray;
		donkergrijs = new Color(98, 98, 98);
		
		//defaults = UIManager.getDefaults();
		//defaults.put(CALCULATOR_BUTTON + "margin", new Insets(0,0,0,0));
		//defaults.put(CALCULATOR_BUTTON + "contentMargins", new Insets(0,0,0,0));
		
		getalKnop = new JButton[10];
		for(int i = 0; i<getalKnop.length; i++)
			getalKnop[i] = maakButton(""+i, donkergrijs, witblauw);
		
		plusKnop = maakButton("+", grijs, witblauw);
		minKnop = maakButton("\u2212", grijs, witblauw);
		keerKnop = maakButton("\u00D7", grijs, witblauw);
		deelKnop = maakButton("\u00F7", grijs, witblauw);
		machtKnop = maakButton("^", grijs, witblauw);
		kwadraatKnop = maakButton("x\u00B2", grijs, witblauw);
		wortelKnop = maakButton("\u221A", grijs, witblauw);
		eenGedeeldDoorKnop = maakButton("x\u207B\u00B9", grijs, witblauw);
		breukKnop = maakButton("a b/c", grijs, witblauw);
		expKnop = maakButton("<html>&times;10<sup><i>x</i></sup></html>", grijs, witblauw);
		
		haakLinksKnop = maakButton("(", grijs, witblauw);
		haakRechtsKnop = maakButton(")", grijs, witblauw);
		
		pijlLinksKnop = maakButton("\u25C4", blauw, witblauw);
		pijlRechtsKnop = maakButton("\u25BA", blauw, witblauw);
		insKnop = maakButton("INS", blauw, witblauw);
		delKnop = maakButton("DEL", blauw, witblauw);
		cKnop = maakButton("C", blauw, witblauw);
		
		if(CalculatorDwo.language.toString().equals("nl"))
			kommaKnop = maakButton(",", grijs, witblauw);
		else
			kommaKnop = maakButton(".", grijs, witblauw);
		negatiefKnop = maakButton("(-)", grijs, witblauw);
		ansKnop = maakButton("Ans", grijs, witblauw);
		isKnop = maakButton("=", groen, Color.WHITE);
		
		sinKnop = maakButton("sin", grijs, witblauw);
		cosKnop = maakButton("cos", grijs, witblauw);
		tanKnop = maakButton("tan", grijs, witblauw);
		invKnop = maakButton("INV", geel, donkergrijs);
		piKnop = maakButton("\u03C0", grijs, witblauw);
		
		logKnop = maakButton("log", grijs, witblauw);
		lnKnop = maakButton("ln", grijs, witblauw);
		eKnop = maakButton("e", grijs, witblauw);
		nWortelKnop = maakButton("\u207F\u221A", grijs, witblauw);
		
		for(int i = 0; i < 10; i++)
			getalKnop[i].addActionListener(this);
		plusKnop.addActionListener(this);
		minKnop.addActionListener(this);
		keerKnop.addActionListener(this);
		deelKnop.addActionListener(this);
		machtKnop.addActionListener(this);
		kwadraatKnop.addActionListener(this);
		wortelKnop.addActionListener(this);
		eenGedeeldDoorKnop.addActionListener(this);
		breukKnop.addActionListener(this);
		expKnop.addActionListener(this);
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
		nWortelKnop.addActionListener(this);
		leegLabel = new JLabel[15];
		for(int i = 0; i<15; i++)
			leegLabel[i] = new JLabel("");
		sinInvLabel = maakLabel("sin\u207B\u00B9", Color.orange);
		cosInvLabel = maakLabel("cos\u207B\u00B9", Color.orange);
		tanInvLabel = maakLabel("tan\u207B\u00B9", Color.orange);
		decLabel = maakLabel("\u2192dec", Color.orange);
		
		knoppenPanel = new JPanel();
		add(knoppenPanel, BorderLayout.CENTER);
		
		linkerKnoppen = new JPanel();
		linkerKnoppen.setLayout(new GridLayout(6, 5, 3, 3));
		rechterKnoppen = new JPanel();
		rechterKnoppen.setLayout(new GridLayout (5, 5, 3, 3));
		bovensteKnoppen = new JPanel();
		bovensteKnoppen.setLayout(new GridLayout(1, 5, 3, 3));
		ondersteKnoppen = new JPanel();
		
		instellingenPanel = new JPanel();
		instellingenPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		gradenButton = new JRadioButton(CalculatorDwo.rb.getString("gradenButton"));
		gradenButton.addActionListener(this);
		
		radialenButton = new JRadioButton(CalculatorDwo.rb.getString("radialenButton"));
		radialenButton.setSelected(true);
		radialenButton.addActionListener(this);
		
		groep = new ButtonGroup();
		groep.add(gradenButton);
		groep.add(radialenButton);
		
		zetRmMode(rmMode, gradenInstelbaar);
		
		replaceCaret = new ReplaceCaret();
		defaultCaret = new DefaultCaret();
		
		invoerVeld = new JTextField("");
		invoerVeld.setCaret(defaultCaret);
		invoerVeld.getCaret().setBlinkRate(500);
		invoerVeld.getCaret().setVisible(true);
		invoerVeld.setBackground(witblauw);
		invoerVeld.setFont(theFont);
		invoerVeld.setMargin(new Insets(8,10,3,3));
		invoerVeld.addKeyListener(this);
		invoerVeld.addMouseListener(this);

		invLabel = new JLabel("I");
		invLabel.setFont(theSmallFont);
		invLabel.setOpaque(true);
		invLabel.setBackground(Color.BLACK);
		invLabel.setForeground(witblauw);
		invLabel.setHorizontalAlignment(SwingConstants.CENTER);
		invLabel.setBounds(2,2,5,6);
		invLabel.setVisible(invers);
		invoerVeld.add(invLabel, 0);
		
		uitvoerVeld = new JLabel("0");
		uitvoerVeld.setHorizontalAlignment(JLabel.RIGHT);
		uitvoerVeld.setFont(theLargeFont);
		
		uitvoerPanel = new JPanel();
		uitvoerPanel.setLayout(new BorderLayout());
		uitvoerPanel.setBackground(lichtblauw);
		add(uitvoerPanel, BorderLayout.NORTH);
		
		uitvoerPanel.add(invoerVeld, BorderLayout.NORTH);
		uitvoerPanel.add(uitvoerVeld, BorderLayout.SOUTH);
	}
	
	public JButton maakButton(String s, Color backGround, Color foreGround)
	{
		final JButton button = new JButton(s);
		button.setFont(theLargeFont);
		button.setOpaque(true);
		//button.setBorderPainted(false);
		button.setBackground(backGround);
		button.setForeground(foreGround);
		button.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
		button.setMargin(new Insets(0, 0, 0, 0));
		//button.setUI(new MyButtonUI());
		button.getModel().addChangeListener(new ChangeListener() {
	        @Override
	        public void stateChanged(ChangeEvent e) {
	            ButtonModel model = (ButtonModel) e.getSource();
	            if (model.isRollover()) {
	                button.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.gray, 1), BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1)));
	            } else {
	                button.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
	            }
	        }
	    });
		return button;
	}
	
	public JLabel maakLabel(String s, Color c)
	{
		JLabel label = new JLabel(s);
		label.setFont(theLargeFont);
		label.setForeground(c);
		label.setVerticalAlignment(SwingConstants.BOTTOM);
		return label;
	}
	
	public void zetRmMode(int i, boolean b)
	{
		//rmMode = 0: Eenvoudig
		//rmMode = 1: Wetenschappelijk
		//rmMode = 2: Cito
		
		rmMode = i;
		gradenInstelbaar = (b && rmMode == 1);
		if(rmMode == 2)
			invKnop.setBackground(Color.ORANGE);
		else
			invKnop.setBackground(geel);
		if(rmMode == 2)
			graden = true;
		else
			graden = false;
		knoppenPanel.removeAll();
		
		if(rmMode < 2)
		{
			knoppenPanel.setLayout(new BorderLayout(5, 5));
			
			bovensteKnoppen.add(pijlLinksKnop);
			bovensteKnoppen.add(pijlRechtsKnop);
			bovensteKnoppen.add(insKnop);
			bovensteKnoppen.add(delKnop);
			bovensteKnoppen.add(cKnop);
			
			knoppenPanel.add(bovensteKnoppen, BorderLayout.NORTH);
			knoppenPanel.add(ondersteKnoppen, BorderLayout.CENTER);
		
			if(gradenInstelbaar)
			{	instellingenPanel.add(gradenButton);
				instellingenPanel.add(radialenButton);
				knoppenPanel.add(instellingenPanel, BorderLayout.SOUTH);
				
				
			}
			
			ondersteKnoppen.removeAll();
			if(rmMode == 1 && gonioKnoppen && logaritmeKnoppen)
				ondersteKnoppen.setLayout(new GridLayout(5, 8, 3, 3));
			else if(rmMode == 1)
				ondersteKnoppen.setLayout(new GridLayout(4, 8, 3, 3));
			else
				ondersteKnoppen.setLayout(new GridLayout(4, 6, 3, 3));
				
			ondersteKnoppen.add(getalKnop[7]);
			ondersteKnoppen.add(getalKnop[8]);
			ondersteKnoppen.add(getalKnop[9]);
			ondersteKnoppen.add(keerKnop);
			ondersteKnoppen.add(deelKnop);
			ondersteKnoppen.add(wortelKnop);
			if(rmMode == 1)
			{	ondersteKnoppen.add(nWortelKnop);
				if(gonioKnoppen)
					ondersteKnoppen.add(sinKnop);
				else if(logaritmeKnoppen)
					ondersteKnoppen.add(eenGedeeldDoorKnop);
				else
					ondersteKnoppen.add(leegLabel[1]);
			}
			
			ondersteKnoppen.add(getalKnop[4]);
			ondersteKnoppen.add(getalKnop[5]);
			ondersteKnoppen.add(getalKnop[6]);
			ondersteKnoppen.add(plusKnop);
			ondersteKnoppen.add(minKnop);
			ondersteKnoppen.add(kwadraatKnop);
			if(rmMode == 1)
			{	if(logaritmeKnoppen)
					ondersteKnoppen.add(logKnop);
				else
					ondersteKnoppen.add(eenGedeeldDoorKnop);
				if(gonioKnoppen)
					ondersteKnoppen.add(cosKnop);
				else if(logaritmeKnoppen)
					ondersteKnoppen.add(breukKnop);
				else
					ondersteKnoppen.add(leegLabel[2]);
			}
			
			ondersteKnoppen.add(getalKnop[1]);
			ondersteKnoppen.add(getalKnop[2]);
			ondersteKnoppen.add(getalKnop[3]);
			ondersteKnoppen.add(haakLinksKnop);
			ondersteKnoppen.add(haakRechtsKnop);
			ondersteKnoppen.add(machtKnop);
			if(rmMode == 1)
			{	if(logaritmeKnoppen)
					ondersteKnoppen.add(lnKnop);
				else
					ondersteKnoppen.add(breukKnop);
				if(gonioKnoppen)
					ondersteKnoppen.add(tanKnop);
				else if(logaritmeKnoppen)
					ondersteKnoppen.add(expKnop);
				else
					ondersteKnoppen.add(invKnop);
			}
			
			ondersteKnoppen.add(getalKnop[0]);
			ondersteKnoppen.add(kommaKnop);
			ondersteKnoppen.add(negatiefKnop);
			ondersteKnoppen.add(piKnop);
			if(rmMode == 1 && logaritmeKnoppen)
				ondersteKnoppen.add(eKnop);
			ondersteKnoppen.add(ansKnop);
			if(rmMode == 1 && !logaritmeKnoppen)
				ondersteKnoppen.add(expKnop);
			ondersteKnoppen.add(isKnop);
			if(rmMode == 1)
			{	if(gonioKnoppen || logaritmeKnoppen)
					ondersteKnoppen.add(invKnop);
				if(gonioKnoppen && logaritmeKnoppen)
				{
					ondersteKnoppen.add(eenGedeeldDoorKnop);
					ondersteKnoppen.add(breukKnop);
					ondersteKnoppen.add(expKnop);
					ondersteKnoppen.add(leegLabel[0]);
				}
			}
		}
		else
		{	knoppenPanel.setLayout(new GridLayout(1, 2, 10, 5));
			
			knoppenPanel.add(linkerKnoppen);
			knoppenPanel.add(rechterKnoppen);
			
			linkerKnoppen.add(invKnop);
			linkerKnoppen.add(leegLabel[0]);
			linkerKnoppen.add(leegLabel[1]);
			linkerKnoppen.add(leegLabel[2]);
			linkerKnoppen.add(leegLabel[3]);
			
			linkerKnoppen.add(sinInvLabel);
			linkerKnoppen.add(cosInvLabel);
			linkerKnoppen.add(tanInvLabel);
			linkerKnoppen.add(decLabel);
			linkerKnoppen.add(leegLabel[4]);
			
			linkerKnoppen.add(sinKnop);
			linkerKnoppen.add(cosKnop);
			linkerKnoppen.add(tanKnop);
			linkerKnoppen.add(breukKnop);
			linkerKnoppen.add(expKnop);
			
			linkerKnoppen.add(wortelKnop);
			linkerKnoppen.add(kwadraatKnop);
			linkerKnoppen.add(machtKnop);
			linkerKnoppen.add(eenGedeeldDoorKnop);
			linkerKnoppen.add(piKnop);
		
			for(int j = 5; j < 15; j++)
				linkerKnoppen.add(leegLabel[j]);
			
			rechterKnoppen.add(pijlLinksKnop);
			rechterKnoppen.add(pijlRechtsKnop);
			rechterKnoppen.add(insKnop);
			rechterKnoppen.add(delKnop);
			rechterKnoppen.add(cKnop);
			
			rechterKnoppen.add(getalKnop[7]);
			rechterKnoppen.add(getalKnop[8]);
			rechterKnoppen.add(getalKnop[9]);
			rechterKnoppen.add(keerKnop);
			rechterKnoppen.add(deelKnop);
			
			rechterKnoppen.add(getalKnop[4]);
			rechterKnoppen.add(getalKnop[5]);
			rechterKnoppen.add(getalKnop[6]);
			rechterKnoppen.add(plusKnop);
			rechterKnoppen.add(minKnop);
			
			rechterKnoppen.add(getalKnop[1]);
			rechterKnoppen.add(getalKnop[2]);
			rechterKnoppen.add(getalKnop[3]);
			rechterKnoppen.add(haakLinksKnop);
			rechterKnoppen.add(haakRechtsKnop);
			
			rechterKnoppen.add(getalKnop[0]);
			rechterKnoppen.add(kommaKnop);
			rechterKnoppen.add(negatiefKnop);
			rechterKnoppen.add(ansKnop);
			rechterKnoppen.add(isKnop);
		}
			
		revalidate();
		repaint();
	}
	
	public void zetOpdracht(Hashtable h, String[] randomVars,
			Hashtable randomValues) {
		if (h.containsKey("rmMode")) 
			rmMode = ((Integer) h.get("rmMode")).intValue();
		if (h.containsKey("gradenInstelbaar"))
			gradenInstelbaar = ((Boolean) h.get("gradenInstelbaar")).booleanValue();
		if (h.containsKey("gonioKnoppen"))
			gonioKnoppen = ((Boolean) h.get("gonioKnoppen")).booleanValue();
		if (h.containsKey("logaritmeKnoppen"))
			logaritmeKnoppen = ((Boolean) h.get("logaritmeKnoppen")).booleanValue();
		zetRmMode(rmMode, gradenInstelbaar);
		
	}

	public void setState(Hashtable h) {
		if (h.containsKey("rmMode")) 
			rmMode = ((Integer) h.get("rmMode")).intValue();
		if (h.containsKey("gradenInstelbaar"))
			gradenInstelbaar = ((Boolean) h.get("gradenInstelbaar")).booleanValue();
		if (h.containsKey("gonioKnoppen"))
			gonioKnoppen = ((Boolean) h.get("gonioKnoppen")).booleanValue();
		if (h.containsKey("logaritmeKnoppen"))
			logaritmeKnoppen = ((Boolean) h.get("logaritmeKnoppen")).booleanValue();
		zetRmMode(rmMode, gradenInstelbaar);
		
	}

	public void setEditState(Hashtable h) {
		if (h.containsKey("rmMode")) 
			rmMode = ((Integer) h.get("rmMode")).intValue();
		if (h.containsKey("gradenInstelbaar"))
			gradenInstelbaar = ((Boolean) h.get("gradenInstelbaar")).booleanValue();
		if (h.containsKey("gonioKnoppen"))
			gonioKnoppen = ((Boolean) h.get("gonioKnoppen")).booleanValue();
		if (h.containsKey("logaritmeKnoppen"))
			logaritmeKnoppen = ((Boolean) h.get("logaritmeKnoppen")).booleanValue();
		zetRmMode(rmMode, gradenInstelbaar);
	}

	public Hashtable getState() {
		return null;
	}

	public Hashtable getEditState() 
	{	
		Hashtable h = new Hashtable();
		h.put("rmMode", new Integer(rmMode));
		h.put("gradenInstelbaar", new Boolean(gradenInstelbaar));
		h.put("gonioKnoppen", new Boolean(gonioKnoppen));
		h.put("logaritmeKnoppen", new Boolean(logaritmeKnoppen));
		
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
		return true;
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
		
		//Alle ^(-1) goed schrijven
		for(int i = 0; i < sb.length()-1; i++)
			if(sb.charAt(i) == '\u207B')
				sb.replace(i, i+2, "^-1");
		
		//Alle *10^x goed schrijven
		for(int i = 0; i < sb.length()-1; i++)
			if(sb.charAt(i) == '\u2081')
			{	if(sb.length() < i + 3)
				{	syntaxError = true;
					System.out.println("ERROR niets na 10macht");
				}
				else if(sb.charAt(i+2) == '-' || sb.charAt(i+2) == '\u2212')
					sb.replace(i, i+3, "G");
				else
					sb.replace(i, i+2, "E");
			}
		
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
			for(int i = 0; i < linksTeller - rechtsTeller; i++)
			{	sb.append(')');
				invoerVeld.setText(invoerVeld.getText()+")");
			}
		
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
		//alle minnen hetzelfde maken, en alle keertekens en gedeeld-doortekens snel leesbaar maken
		replace(sb, "\u2212", "-");
		replace(sb, "\u00F7", "/");
		replace(sb, "\u00D7", "x");
		
		//++ veranderen in +, etc
		replace(sb, "++", "+");
		replace(sb, "+-", "-");
		replace(sb, "--", "+");
		replace(sb, "-+", "-");
		replace(sb, "x+", "x");
		replace(sb, "/+", "/");
		
		//checken of er breuktekentjes of B's in staan
		if(sb.indexOf("\u22A5") > -1 || sb.indexOf("B") > -1)
			breuk = true;	
				
		//goniofuncties uitrekenen
		for(int i = 0; i < sb.length() - 1; i++)
			if(sb.charAt(i) == 's' && sb.charAt(i+1)== 'i')
			{	if(sb.charAt(i+3) == '(')
				{	vindHaakjesUitdrukking(sb, i + 3);
					if(breuk)
					{	if(graden)
						{	teller = teller * Math.PI;
							noemer = noemer * 180;
						}	
						sb.replace(i, i + lengteHaakjesUitdrukking + 3, 
							Double.toString(Math.sin(teller/noemer)));
					}
					else	
					{	if(graden)
							uitkomst = uitkomst * Math.PI / 180;
						sb.replace(i, i + lengteHaakjesUitdrukking + 3,
							Double.toString(Math.sin(uitkomst)));
					}
				}
				else //arcsin
				{	vindHaakjesUitdrukking(sb, i + 7);
					if(breuk)
					{	if(graden)
						{	teller = teller * Math.PI;
							noemer = noemer * 180;
						}
						sb.replace(i, i + lengteHaakjesUitdrukking + 7,
							Double.toString(Math.asin(teller/noemer)));
					}
					else
					{	if(graden)
							uitkomst = uitkomst * Math.PI / 180;
						sb.replace(i, i + lengteHaakjesUitdrukking + 7,
							Double.toString(Math.asin(uitkomst)));
					}
				}
			}
		for(int i = 0; i < sb.length()-1; i++)
			if(sb.charAt(i) == 'c' && sb.charAt(i+1) == 'o')
			{	if(sb.charAt(i+3) == '(')
				{	vindHaakjesUitdrukking(sb, i + 3);
					if(breuk)
					{	if(graden)
						{	teller = teller * Math.PI;
							noemer = noemer * 180;
						}
						sb.replace(i, i + lengteHaakjesUitdrukking + 3,
							Double.toString(Math.cos(teller/noemer)));
					}
					else
					{	if(graden)
							uitkomst = uitkomst * Math.PI / 180;
						sb.replace(i, i + lengteHaakjesUitdrukking + 3,
							Double.toString(Math.cos(uitkomst)));
					}
				}
				else //arccos
				{	
					vindHaakjesUitdrukking(sb, i + 7);
					if(breuk)
					{	if(graden)
						{	teller = teller * Math.PI;
							noemer = noemer * 180;
						}
						sb.replace(i, i + lengteHaakjesUitdrukking + 7,
							Double.toString(Math.acos(teller/noemer)));
					}
					else
					{	if(graden)
							uitkomst = uitkomst * Math.PI / 180;
						sb.replace(i, i + lengteHaakjesUitdrukking + 7,
							Double.toString(Math.acos(uitkomst)));
					}
				}
			}
		for(int i = 0; i < sb.length()-1; i++)
			if(sb.charAt(i) == 't' && sb.charAt(i+1) == 'a')
			{	if(sb.charAt(i+3) == '(')
				{	vindHaakjesUitdrukking(sb, i + 3);
					if(breuk)
					{	if(graden)
						{	teller = teller * Math.PI;
							noemer = noemer * 180;
						}
						sb.replace(i, i + lengteHaakjesUitdrukking + 3,
							Double.toString(Math.tan(teller/noemer)));
					}
					else
					{	if(graden)
							uitkomst = uitkomst * Math.PI / 180;
						sb.replace(i, i + lengteHaakjesUitdrukking + 3,
								Double.toString(Math.tan(uitkomst)));
					}
					
				}
				else //arctan
				{	vindHaakjesUitdrukking(sb, i + 7);
					if(breuk)
					{	if(graden)
						{	teller = teller * Math.PI;
							noemer = noemer * 180;
						}
						sb.replace(i, i + lengteHaakjesUitdrukking + 7,
							Double.toString(Math.atan(teller/noemer)));
					}
					else	
					{	if(graden)
							uitkomst = uitkomst * Math.PI / 180;
						sb.replace(i, i + lengteHaakjesUitdrukking + 7,
							Double.toString(Math.atan(uitkomst)));
					}
				}
			}
		
		//logfuncties uitrekenen
		for(int i = 0; i < sb.length() - 1; i++)
			if(sb.charAt(i) == 'l' && sb.charAt(i+1)== 'o')//log
			{	vindHaakjesUitdrukking(sb, i + 3);
				if(breuk)
					sb.replace(i, i + lengteHaakjesUitdrukking + 3,
							Double.toString(Math.log10(teller/noemer)));
				else
					sb.replace(i, i + lengteHaakjesUitdrukking + 3,
							Double.toString(Math.log10(uitkomst)));
			}	
		for(int i = 0; i < sb.length() - 1; i++)
			if(sb.charAt(i) == 'l' && sb.charAt(i+1)== 'n')//ln
			{	vindHaakjesUitdrukking(sb, i + 2);
				if(breuk)
					sb.replace(i, i + lengteHaakjesUitdrukking + 2,
							Double.toString(Math.log(teller/noemer)));
				else
					sb.replace(i, i + lengteHaakjesUitdrukking + 2,
							Double.toString(Math.log(uitkomst)));
			}	
		
		//op zoek naar machten
		sb = vindMachten(sb);
			
		
		//haakjes wegwerken (met een while statement, zolang er nog ) zijn.
		berekenTellers(sb);
		String substring1;
		while(rechtsTeller > 0)
		{
			try
			{	int eindpunt = sb.indexOf(")");		
			System.out.println("try1: eindpunt = " + eindpunt);	
			int beginpunt = sb.substring(0,eindpunt).lastIndexOf("(");
			System.out.println("try2: beginpunt = " + beginpunt);	
				
			substring1 = sb.substring(beginpunt+1,eindpunt);				
			System.out.println("try3: substring1 = " + substring1);	
				
			berekenWaarde(substring1);
			System.out.println("try4");	
			System.out.println("sb2.toString(): " + sb2.toString());	
			sb.replace(beginpunt, eindpunt+1, sb2.toString());
			}
			catch(Exception e){
				syntaxError = true;
				System.out.println("ERROR haakjes wegwerken");}
			System.out.println("sb: " + sb);
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
	
	public StringBuffer vindMachten(StringBuffer sb)
	{ 
		if(breuk)
			while(sb.indexOf("^") != -1 && breuk)
			{	vindUitkomstMachtBreuk("^", sb);		
				sb.replace(vindIndex("^", sb) - lengte1, vindIndex("^", sb) + lengte2 + 1, teller + "B" + noemer);
				if(syntaxError)
				{	System.out.println("ERROR machten breuken");
					return sb;
				}
			}
		else
			while(sb.indexOf("^") != -1)
			{	double rekenKind1, rekenKind2;
				if(sb.indexOf("^") > 0 && sb.charAt(sb.indexOf("^") - 1) == ')')
				{	int index = sb.indexOf("^") - 1;
					int haakjesTeller = 1;
					while(index > 0 && haakjesTeller > 0)
					{	index--;
						if(sb.charAt(index) == '(')
							haakjesTeller--;
						else if(sb.charAt(index) == ')')
							haakjesTeller++;
					}
					vindHaakjesUitdrukking(sb, index);
					rekenKind1 = uitkomst;
					lengte1 = lengteHaakjesUitdrukking; //hier stond +1
				}
				else
				{	vindGetalVoorBewerking(vindIndex("^", sb), sb, false);
					rekenKind1 = rekenGetal;
					lengte1 = lengteRekenGetal;
				}
				if(syntaxError)
				{	System.out.println("Syntax Error komt uit vindGetalVoorBewerking");
					return sb;
				}
				if(sb.indexOf("^") < sb.length() - 1 && sb.charAt(sb.indexOf("^") + 1) == '(')
				{	vindHaakjesUitdrukking(sb, sb.indexOf("^") + 1);
					rekenKind2 = uitkomst;
					lengte2 = lengteHaakjesUitdrukking;
				}
				else
				{	vindGetalNaBewerking(vindIndex("^", sb), sb);
					rekenKind2 = rekenGetal;
					lengte2 = lengteRekenGetal;
				}
				if(syntaxError)
				{	System.out.println("Syntax Error komt uit vindGetalNaBewerking");
					return sb;
				}
				uitkomst = Math.pow(rekenKind1, rekenKind2);
				if(Double.isNaN(uitkomst))
				{	uitkomst = -Math.pow(-rekenKind1, rekenKind2);
					double test = Math.pow(uitkomst, 1/rekenKind2);
					if(Math.abs(test - rekenKind1) > 0.000000001)
						uitkomst = Double.NaN;
				}
				sb.replace(vindIndex("^", sb)-lengte1, vindIndex("^", sb)+lengte2+1, Double.toString(uitkomst));
				//vervangUitkomst("^", sb);
				if(syntaxError)
				{	System.out.println("ERROR machten");
					return sb;
				}
			}
		return sb;
	}
	
	/*
	 * berekenWaarde berekent de waarde van een expressie waarin geen haakjes, ans, logaritmes en
	 * gonio-formules voorkomen.
	 */
	public void berekenWaarde(String str) 
	{		
		sb2.delete(0, sb2.length());
		sb2.append(str);
	
		//op zoek naar wortels
		if(breuk)
		{	while(sb2.indexOf("\u221A") != -1)
			{	vindBBreukVanaf(sb2.indexOf("\u221A"), sb2);
				if(syntaxError)
				{	System.out.println("ERROR breuk-wortel");
					return;
				}
				if(sb2.indexOf("\u221A") == 0 || sb2.charAt(sb2.indexOf("\u221A")-1) != '\u207F')
				{	
					//testen of de teller en de noemer geheel zijn.
					//in dat geval breuk vereenvoudigen en dan pas wortels nemen, om te kunnen zien of ze geheel zijn.
					if(tellerB - (int) tellerB == 0 && noemerB - (int) noemerB == 0)
					{	teller = Math.sqrt(simplify((int) tellerB, (int) noemerB)[0]);
						noemer = Math.sqrt(simplify((int) tellerB, (int) noemerB)[1]);
					}
					else
					{	teller = Math.sqrt(tellerB);
						noemer = Math.sqrt(noemerB);
					}
					lengteRekenGetal = lengteBreukB;
					if(noemer == 1)
						sb2.replace(sb2.indexOf("\u221A"), sb2.indexOf("\u221A") + lengteBreukB + 1, "" + teller);
					else
						sb2.replace(sb2.indexOf("\u221A"), sb2.indexOf("\u221A") + lengteBreukB + 1, teller + "B" + noemer);
				}
				else
				{	teller = tellerB;
					noemer = noemerB;
					int lengte1 = lengteBreukB;
					vindBBreukTot(sb2.indexOf("\u221A") - 1, sb2, true);
					if(syntaxError)
					{	System.out.println("ERROR breuk-nwortel");
						return;
					}
					sb2.replace(sb2.indexOf("\u221A") - 1 - lengteBreukB, sb2.indexOf("\u221A") + lengte1 + 1, teller + "B" + noemer + "^"+noemerB + "B"+tellerB);
				}
			}
		}
		else
		{	while(sb2.indexOf("\u221A") != -1)
			{	vindGetalNaBewerking(sb2.indexOf("\u221A"), sb2);
				if(syntaxError)
				{	System.out.println("ERROR wortels zonder breuk");
					return;
				}
				if(sb2.indexOf("\u221A") == 0 || sb2.charAt(sb2.indexOf("\u221A")-1) != '\u207F')
				{	rekenGetal = Math.sqrt(rekenGetal);
					sb2.replace(sb2.indexOf("\u221A"), sb2.indexOf("\u221A") + lengteRekenGetal + 1, 
						Double.toString(rekenGetal));
				}
				else
				{	double rekenKind1 = rekenGetal;
					int lengte1 = lengteRekenGetal;
					vindGetalVoorBewerking(sb2.indexOf("\u207F"), sb2, true);
					if(syntaxError)
					{	System.out.println("ERROR nwortel zonder breuk");
						return;
					}
					rekenGetal = 1/rekenGetal;
					sb2.replace(sb2.indexOf("\u221A") - 1 - lengteRekenGetal, sb2.indexOf("\u221A") + lengte1 + 1, rekenKind1 + "^" + rekenGetal);
				}
			}
		}
		
		vindMachten(sb2);

		//op zoek naar producten en delingen
		if(breuk)
		{	while(sb2.indexOf("x") != -1 || sb2.indexOf("/") != -1)
			{
				if(sb2.indexOf("/") == -1) 
					vervangUitkomstBreuk("x", sb2);
				else if(sb2.indexOf("x") == -1)
					vervangUitkomstBreuk("/", sb2);
				else if(sb2.indexOf("x") < sb2.indexOf("/"))
					vervangUitkomstBreuk("x", sb2);
				else 
					vervangUitkomstBreuk("/", sb2);
				
				if(syntaxError)
				{	System.out.println("ERROR product/deling breuk");
					return;
				}
			}
		}
		else
		{	while(sb2.indexOf("x") != -1 || sb2.indexOf("/") != -1)
			{
				if(sb2.indexOf("/") == -1) 
					vervangUitkomst("x", sb2);
				else if(sb2.indexOf("x") == -1)
					vervangUitkomst("/", sb2);
				else if(sb2.indexOf("x") < sb2.indexOf("/"))
					vervangUitkomst("x", sb2);
				else 
					vervangUitkomst("/", sb2);
			
				if(syntaxError)
				{	System.out.println("ERROR product/deling");
					return;
				}
			}
		}
		
		//E- veranderen in G om problemen met mintekens te voorkomen
		replace(sb2, "E-", "G");
		
		//op zoek naar optellen en aftrekken
		if(breuk)
			while(vindIndex("+", sb2) != -1 || vindIndex("-", sb2) != -1)
			{	if(vindIndex("-", sb2) == -1) 				
					vervangUitkomstBreuk("+", sb2);
				else if(vindIndex("+", sb2) == -1)
					vervangUitkomstBreuk("-", sb2);
				else if(vindIndex("+", sb2) < vindIndex("-", sb2))
					vervangUitkomstBreuk("+", sb2);
				else 
					vervangUitkomstBreuk("-", sb2);			
				if(syntaxError)
				{	System.out.println("ERROR optellen/aftrekken breuk");
					return;
				}
			}
		else
			while(vindIndex("+", sb2) != -1 || vindIndex("-", sb2) != -1)
			{	if(vindIndex("-", sb2) == -1) 
					vervangUitkomst("+", sb2);
				else if(vindIndex("+", sb2) == -1)
					vervangUitkomst("-", sb2);
				else if(vindIndex("+", sb2) < vindIndex("-", sb2))
					vervangUitkomst("+", sb2);
				else 
					vervangUitkomst("-", sb2);			
				if(syntaxError)
				{	System.out.println("ERROR optellen/aftrekken");
					return;
				}
			}
		
		if(breuk)
		{	try
			{	vindBreukVanaf(-1, sb2);
				sb2.delete(0, sb2.length());
				sb2.append(teller + "B" + noemer);
			}
			catch(Exception e)
			{	syntaxError = true;
				System.out.println("ERROR in berekenWaardeOverig Breuk");
			}
		}
		else
			try{	
				uitkomst = Double.parseDouble(sb2.toString());
			}
			catch(Exception e)
			{	
				if(sb2.toString().equals("\u03C0"))
				{	uitkomst = Math.PI;
					sb2.delete(0, sb2.length());
					sb2.append(uitkomst);
				}
				else if(sb2.toString().equals("e"))
				{	uitkomst = Math.E;
					sb2.delete(0, sb2.length());
					sb2.append(uitkomst);
				}
				else if(sb2.indexOf("E") > -1)//een zeer groot getal
					try{
						//vindUitkomst("E", sb2);
						vervangUitkomst("E", sb2);
					}
					catch(Exception ex){
					syntaxError = true;
					System.out.println("ERROR vindUitkomst(E)");				
					}
				else if(sb2.indexOf("G") > -1)//een zeer klein getal
					try{
						//vindUitkomst("G", sb2);
						vervangUitkomst("G", sb2);
					}
					catch(Exception ex){
					syntaxError = true;
					System.out.println("ERROR vindUitkomst(G)");				
					}	
				else
				{	System.out.println("ERROR in berekenWaarde overig");
					syntaxError = true;
				}
			}
		System.out.println("sb2 einde berekenWaarde: " + sb2.toString());
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
	 * +, -, *, /
	 */
	public void vindUitkomst(String s, StringBuffer sb)
	{
		vindGetalVoorBewerking(vindIndex(s, sb), sb, true);
		if(syntaxError)
		{	System.out.println("Syntax Error komt uit vindGetalVoorBewerking");
			return;
		}
		double rekenKind1 = rekenGetal;
		
		lengte1 = lengteRekenGetal;
		vindGetalNaBewerking(vindIndex(s, sb), sb);
		if(syntaxError)
		{	System.out.println("Syntax Error komt uit vindGetalNaBewerking");
			return;
		}
		double rekenKind2 = rekenGetal;
		lengte2 = lengteRekenGetal;
		if(s.equals("+"))
			uitkomst = rekenKind1 + rekenKind2;
		else if(s.equals("-"))
			uitkomst = rekenKind1 - rekenKind2;
		else if(s.equals("x"))
			uitkomst = rekenKind1 * rekenKind2;
		else if(s.equals("/"))
			uitkomst = rekenKind1/rekenKind2;
		else if(s.equals("^"))
			uitkomst = Math.pow(rekenKind1,rekenKind2);
		else if(s.equals("E"))
			uitkomst = rekenKind1 * Math.pow(10, rekenKind2);
		else if(s.equals("G"))
			uitkomst = rekenKind1 * Math.pow(10, -rekenKind2);
	}
	
	public void vervangUitkomst(String s, StringBuffer sb)
	{
		vindUitkomst(s, sb);		
		sb.replace(vindIndex(s, sb)-lengte1, vindIndex(s, sb)+lengte2+1, Double.toString(uitkomst));
	}
	
	public void vindGetalVoorBewerking(int pos, StringBuffer sb, boolean minteken)
	{	
		int beginPos = pos-1;
		try{
			if(sb.charAt(pos-1) == '.')
			{	sb.deleteCharAt(pos-1);
				pos--;
			}
			
			if(Character.isDigit(sb.charAt(pos-1)))
			{	while(beginPos >= 0 && Character.isDigit(sb.charAt(beginPos)))
					beginPos --;
				//doet het ����n keer te vaak:
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
				if(beginPos != 0 && sb.charAt(beginPos-1) == '-' && minteken)
					if(beginPos == 1 || sb.charAt(beginPos - 2) == '^'
							|| sb.charAt(beginPos - 2) == 'x' || sb.charAt(beginPos - 2) == '/' 
								|| sb.charAt(beginPos - 2) == '(' || sb.charAt(beginPos - 2) == 'E')
					{	rekenGetal = -rekenGetal;
						lengteRekenGetal++;
						beginPos--;
					}	
			}
			else if(sb.charAt(pos - 1) == '\u03C0')//dit is pi
			{	rekenGetal = Math.PI;
				lengteRekenGetal = 1;
				if(pos > 0 && sb.charAt(pos - 1) == '-')
					if(pos == 1 || sb.charAt(pos - 2) == '^'
						|| sb.charAt(pos - 2) == 'x' || sb.charAt(pos - 2) == '/' 
							|| sb.charAt(pos - 2) == '(' || sb.charAt(beginPos - 2) == 'E')
					{	rekenGetal = -rekenGetal;
						lengteRekenGetal++;
						beginPos--;
					}		
			}
			else if(sb.charAt(pos - 1) == 'e') //dan moet er wel e staan
			{	rekenGetal = Math.E;
				lengteRekenGetal = 1;
				if(pos > 0 && sb.charAt(pos - 1) == '-')
					if(pos == 1 || sb.charAt(pos - 2) == '^'
						|| sb.charAt(pos - 2) == 'x' || sb.charAt(pos - 2) == '/' 
							|| sb.charAt(pos - 2) == '(' || sb.charAt(beginPos - 2) == 'E')
					{	rekenGetal = -rekenGetal;
						lengteRekenGetal++;
						beginPos--;
					}	
			}				
			else
			{	syntaxError = true;
				System.out.println("ERROR getalVoorBewerking else" );
			}
			
		}
		catch(Exception e){
			syntaxError = true;
			System.out.println("ERROR getalVoorBewerking Exception");
		}
		if(beginPos != 0 && sb.charAt(beginPos-1) == 'E') 
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
			
			subString = sb.substring(beginPos, beginPos2+1);
			double rekenGetal2 = Double.parseDouble(subString);
			rekenGetal = rekenGetal2*Math.pow(10, rekenGetal);
			lengteRekenGetal = lengteRekenGetal + subString.length() + 1;
		}
		if(beginPos != 0 && sb.charAt(beginPos-1) == 'G') 
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
			
			subString = sb.substring(beginPos, beginPos2+1);
			double rekenGetal2 = Double.parseDouble(subString);
			
			rekenGetal = rekenGetal2*Math.pow(10, -rekenGetal);
			lengteRekenGetal = lengteRekenGetal + subString.length() + 1;
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
			int eindPos = pos + 1;	
			if(Character.isDigit(sb.charAt(pos+1)))//geval dat er een getal na de bewerking staat
			{	
				
				//int eindPos = pos+1;
				while(eindPos <= sb.length()-1 && Character.isDigit(sb.charAt(eindPos)))
					eindPos ++;
				//doet het ����n keer te vaak:
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
			else if(sb.charAt(pos + 1) == '\u03C0')//dit is pi
			{	eindPos = pos + 1;//klopt dit??
				rekenGetal = Math.PI;
				lengteRekenGetal = 1;
			}
			else if(sb.charAt(pos + 1) == 'e') //nu moet er wel e staan
			{	eindPos = pos + 1;
				rekenGetal = Math.E;
				lengteRekenGetal = 1;
			}	
			else
			{	syntaxError = true;
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
				subString = sb.substring(eindPos2, eindPos + 1);
				double rekenGetal2 = Double.parseDouble(subString);
				if(negatief)
					rekenGetal2 = - rekenGetal2;
				rekenGetal = rekenGetal*Math.pow(10, rekenGetal2);
				lengteRekenGetal = lengteRekenGetal + subString.length() + 1;
				if(negatief)
					lengteRekenGetal++;			
			}
			if(eindPos < sb.length() - 1 && sb.charAt(eindPos + 1) == 'G')
			{
				int eindPos2 = eindPos + 2;
				eindPos = eindPos2;
				
				while(eindPos <= sb.length() - 1 && Character.isDigit(sb.charAt(eindPos)))
					eindPos ++;
				eindPos--;
				subString = sb.substring(eindPos2, eindPos + 1);
				double rekenGetal2 = Double.parseDouble(subString);
				rekenGetal = rekenGetal*Math.pow(10, - rekenGetal2);
				lengteRekenGetal = lengteRekenGetal + subString.length() + 1;			
			}
		}
		catch(Exception e){
			syntaxError = true;
			System.out.println("ERROR getalNaBewerking else");
			return;
		}
	}
	
	/*
	 * Uitkomst van 'simpele' bewerkingen berekenen:
	 * +, -, *, /, ^
	 */
	public void vindUitkomstBreuk(String s, StringBuffer sb)
	{
		double teller1, noemer1, teller2, noemer2;
		
		vindBreukTot(vindIndex(s, sb), sb);
		if(syntaxError)
		{	System.out.println("Syntax Error komt uit vindBreukTot");
			return;
		}
		teller1 = teller;
		noemer1 = noemer;
		lengte1 = lengteBreuk;
		
		vindBreukVanaf(vindIndex(s, sb), sb);
		if(syntaxError)
		{	System.out.println("Syntax Error komt uit vindBreukVanaf");
			return;
		}
		teller2 = teller;
		noemer2 = noemer;
		lengte2 = lengteBreuk;
		
		if(s.equals("+"))
		{	teller = teller1 * noemer2 + noemer1 * teller2;
			noemer = noemer1 * noemer2;
		}
		else if(s.equals("-"))
		{	teller = teller1 * noemer2 - noemer1 * teller2;
			noemer = noemer1 * noemer2;
		}
		else if(s.equals("x"))
		{	teller = teller1 * teller2;
			noemer = noemer1 * noemer2;
		}
		else if(s.equals("/"))
		{	teller = teller1 * noemer2;
			noemer = noemer1 * teller2;
		}
		//else if(s.equals("^"))
		//{	vindUitkomstMachtBreuk(s, sb);
		//}
		else if(s.equals("E"))
		{	teller = teller1 * Math.pow(10, teller2/noemer2);
			noemer = noemer1;		
		}
		else if(s.equals("G"))
		{	teller = teller1 * Math.pow(10, -teller2/noemer2);
			noemer = noemer1;		
		}
	}
	
	public void vindUitkomstMachtBreuk(String s, StringBuffer sb)
	{
		double teller1, noemer1, teller2, noemer2;
		
		if(sb.indexOf("^") > 0 && sb.charAt(sb.indexOf("^") - 1) == ')')
		{	int index = sb.indexOf("^") - 1;
			int haakjesTeller = 1;
			while(index > 0 && haakjesTeller > 0)
			{	index--;
				if(sb.charAt(index) == '(')
					haakjesTeller--;
				else if(sb.charAt(index) == ')')
					haakjesTeller++;
			}
			vindHaakjesUitdrukking(sb, index);
			teller1 = teller;
			noemer1 = noemer;
			lengte1 = lengteHaakjesUitdrukking;
		}
		else
		{	vindBBreukTot(vindIndex(s, sb), sb, false);
			teller1 = tellerB;
			noemer1 = noemerB;
			lengte1 = lengteBreukB;
		}
		if(syntaxError)
		{	System.out.println("Syntax Error komt uit vindBBreukTot");
			return;
		}
		
		if(sb.indexOf("^") < sb.length() - 1 && sb.charAt(sb.indexOf("^") + 1) == '(')
		{	vindHaakjesUitdrukking(sb, sb.indexOf("^") + 1);
			teller2 = teller;
			noemer2 = noemer;
			lengte2 = lengteHaakjesUitdrukking;
		}
		else
		{	vindBBreukVanaf(vindIndex(s, sb), sb);
			teller2 = tellerB;
			noemer2 = noemerB;
			lengte2 = lengteBreukB;
		}
		if(syntaxError)
		{	System.out.println("Syntax Error komt uit vindBBreukVanaf");
			return;
		}
		
		teller = Math.pow(teller1, teller2/noemer2);
		if(Double.isNaN(teller))
		{	teller = -Math.pow(-teller1, teller2/noemer2);
			double test = Math.pow(teller, noemer2/teller2);
			if(Math.abs(test - teller1) > 0.000000001)
				teller = Double.NaN;
		}
		noemer = Math.pow(noemer1, teller2/noemer2);
		if(Double.isNaN(noemer))
		{	noemer = -Math.pow(-noemer1, teller2/noemer2);
			double test = Math.pow(noemer, noemer2/teller2);
			if(Math.abs(test - noemer1) > 0.000000001)
				noemer = Double.NaN;
		}
	}
	
	public void vervangUitkomstBreuk(String s, StringBuffer sb)
	{
		vindUitkomstBreuk(s, sb);		
		sb.replace(vindIndex(s, sb) - lengte1, vindIndex(s, sb) + lengte2 + 1, teller + "B" + noemer);
	}
	
	public void vindBreukTot(int pos, StringBuffer sb)
	{
		vindBBreukTot(pos, sb, false);
		lengteBreuk = lengteBreukB;
		if(pos - lengteBreuk - 1 < 0 || sb.charAt(pos - lengteBreuk - 1) != '\u22A5' )
		{	teller = tellerB;
			noemer = noemerB;
		}
		else 
		{	teller = noemerB;
			noemer = tellerB;
			try{
				pos = pos - lengteBreukB - 1;
				vindBBreukTot(pos, sb, false);
				teller = teller * tellerB;
				noemer = noemer * noemerB; 
				lengteBreuk = lengteBreuk + lengteBreukB + 1;
			}
			catch(Exception e)
			{	System.out.println("ERROR breukTot1");
				syntaxError = true;
			}
			if(pos - lengteBreukB - 1 >= 0 && sb.charAt(pos - lengteBreukB - 1) == '\u22A5')
			{	try{
					pos = pos - lengteBreukB - 1;
					vindBBreukTot(pos, sb, false);
					teller = teller * noemerB + noemer * tellerB;
					noemer = noemer * noemerB;
					lengteBreuk = lengteBreuk + lengteBreukB + 1;
				}
				catch(Exception e)
				{	System.out.println("ERROR breukTot2");
					syntaxError = true;
				}
			}
		}
	}
	
	public void vindBreukVanaf(int pos, StringBuffer sb)
	{
		vindBBreukVanaf(pos, sb);
		teller = tellerB;
		noemer = noemerB;
		lengteBreuk = lengteBreukB;
		if(pos + lengteBreuk + 1 <= sb.length() - 1 && sb.charAt(pos + lengteBreukB + 1) == '\u22A5' )
		{	try{
				pos = pos + lengteBreukB + 1;
				vindBBreukVanaf(pos, sb);
				lengteBreuk = lengteBreuk + lengteBreukB + 1;
			}
			catch(Exception e)
			{	System.out.println("ERROR breukVanaf1");
				syntaxError = true;
				return;
			}
			if(pos + lengteBreukB + 1 >= sb.length() - 1 || sb.charAt(pos + lengteBreukB + 1) != '\u22A5')
			{	noemer = noemer * tellerB;
				teller = teller * noemerB;
			}
			else
			{
				double teller1 = teller * noemerB;
				double teller2 = noemer * tellerB;
				noemer = noemerB * noemer;
				try{
					pos = pos + lengteBreukB + 1;
					vindBBreukVanaf(pos, sb);
					teller = teller1 * tellerB + teller2 * noemerB;
					noemer = noemer * tellerB;
					lengteBreuk = lengteBreuk + lengteBreukB + 1;
				}
				catch(Exception e)
				{	System.out.println("ERROR breukVanaf2");
					syntaxError = true;
				}
			}
		}
	}
	
	public void vindBBreukTot(int pos, StringBuffer sb, boolean minteken)
	{
		vindGetalVoorBewerking(pos, sb, true);
		lengteBreukB = lengteRekenGetal;
		if(pos - lengteRekenGetal - 1 < 0 || sb.charAt(pos - lengteRekenGetal - 1) != 'B')
		{	tellerB = rekenGetal;
			noemerB = 1;
		}
		else
		{	noemerB = rekenGetal;
			vindGetalVoorBewerking(pos - lengteBreukB - 1, sb, minteken);
			tellerB = rekenGetal;
			lengteBreukB += lengteRekenGetal + 1;
		}
	}
	
	public void vindBBreukVanaf(int pos, StringBuffer sb)
	{
		vindGetalNaBewerking(pos, sb);
		tellerB = rekenGetal;
		lengteBreukB = lengteRekenGetal;
		if(pos + lengteRekenGetal + 1 > sb.length() -1 || sb.charAt(pos + lengteRekenGetal + 1) != 'B')
			noemerB = 1;
		else
		{	vindGetalNaBewerking(pos + lengteBreukB + 1, sb);
			noemerB = rekenGetal;
			lengteBreukB += lengteRekenGetal + 1;
		}
	}
	
	public int[] simplify(int nom, int denom)
    {   if (denom < 0)
        {   nom = - nom;
            denom = - denom;
        }
        if (nom == 0)
            denom = 1;
        else
        {   int g = gcd(nom, denom);
            nom = nom / g;
            denom = denom / g;
        }
        int[] breuk = {nom, denom}; 
        return breuk;
    }
  
	public int gcd(int a, int b)
	{   int m = Math.abs(a);
		int n = Math.abs(b);
		int temp = 0;
		while ( n != 0 )
		{   temp = m % n;
		    m = n;
		    n = temp;
		}
		return m;
	}
  
	public int vindIndex(String s, StringBuffer sb)
	{	int index;
		if(s.equals("-"))
		{	if(sb.substring(1).indexOf(s) > -1)
				index = sb.substring(1).indexOf(s) + 1;
			else
				index = sb.substring(1).indexOf(s);
		}
		else
			index = sb.indexOf(s);
		return index;
	  
	}
		
	/*
	 * Uitdrukking tussen haakjes vinden; haakje links staat op positie n.
	 * Wordt onder andere gebruikt voor gonioformules. 
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
		bereken(sb3);
		lengteHaakjesUitdrukking = j - n + 1; //dit is nu de lengte inclusief haakjes
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
	
	public void maakStapNaarRechts()
	{	String str = invoerVeld.getText();
		int cp = invoerVeld.getCaretPosition();
		
		if(nieuweInvoer)
		{	nieuweInvoer = false;
			invoerVeld.setCaretPosition(str.length());
		}
		if(cp == str.length())
			return;
		else if(str.charAt(cp)=='A')
			cp += 3;
		else if(str.charAt(cp) == 's' || str.charAt(cp) == 'c' || str.charAt(cp) == 't')
		{	if(str.charAt(cp + 3) == '(' )
				cp += 4;
			else
				cp += 6;
		}
		else if(str.charAt(cp) == 'l')
			if(str.charAt(cp + 1) == 'n')
				cp += 3;
			else
				cp += 4;
		else if(str.charAt(cp) == '\u207F' || str.charAt(cp) == '\u2081' || str.charAt(cp) == '\u207B')
			cp += 2;
		else 
			cp += 1;
		
		invoerVeld.setCaretPosition(cp);
		
	}
	
	public void maakStapNaarLinks()
	{	String str = invoerVeld.getText();
		int cp = invoerVeld.getCaretPosition();
		
		if(nieuweInvoer)
		{	nieuweInvoer = false;
			invoerVeld.setCaretPosition(str.length());
		}
		if(cp == 0)
			return;
		else if(str.charAt(cp - 1)=='s')
			cp -= 3;
		else if(str.charAt(cp - 1) == '(')
		{	if(cp < 3)
				cp -= 1;
			else if(str.charAt(cp - 2) == '\u00B9')
				cp -= 6;
			else if(str.charAt(cp - 2) == 'n' && str.charAt(cp - 3) == 'l')
				cp -= 3;
			else if(str.charAt(cp - 2) == 'n' || str.charAt(cp - 3) == 'o')
				cp -= 4;
			else 
				cp -= 1;
		}
		else if(cp >= 2 && (str.charAt(cp - 2) == '\u207F' || str.charAt(cp - 1) == '\u2080'
			|| str.charAt(cp - 1) == '\u00B9'))
			cp -= 2;
		else if(str.charAt(cp - 1) == '\u2070')
			cp -= 2;
		else 
			cp -= 1;
		
		invoerVeld.setCaretPosition(cp);
	}
	
	public void doeActieBackSpace()
	{	String str = invoerVeld.getText();
		int cp = invoerVeld.getCaretPosition();
		int ss = invoerVeld.getSelectionStart();
		int se = invoerVeld.getSelectionEnd();
		
		if(nieuweInvoer)
		{	nieuweInvoer = false; 
			invoerVeld.setCaretPosition(str.length());
		}
		if(cp == 0)
			return;
		else if(se - ss > 0)
			invoerVeld.setText(str.substring(0, ss) + str.substring(se));
		else if(str.charAt(cp - 1) == 's')
		{	invoerVeld.setText(str.substring(0, cp - 3) + str.substring(cp));
			invoerVeld.setCaretPosition(cp - 3);
		}
		else if(str.charAt(cp - 1) == '(' )
		{	if(cp < 3)
			{	invoerVeld.setText(str.substring(0, cp - 1) + str.substring(cp));
				invoerVeld.setCaretPosition(cp - 1);
			}
			else if(str.charAt(cp - 2) == '\u00B9')
			{	invoerVeld.setText(str.substring(0, cp - 6) + str.substring(cp));
				invoerVeld.setCaretPosition(cp - 6);
			}
			else if(str.charAt(cp - 2) == 'n' && str.charAt(cp - 3) == 'l')
			{	invoerVeld.setText(str.substring(0, cp - 3) + str.substring(cp));
				invoerVeld.setCaretPosition(cp - 3);
			}
			else if(str.charAt(cp - 2) == 'n' || str.charAt(cp - 3) == 'o')
			{	invoerVeld.setText(str.substring(0, cp - 4) + str.substring(cp));
				invoerVeld.setCaretPosition(cp - 4);
			}
			else
			{	invoerVeld.setText(str.substring(0, cp - 1) + str.substring(cp));
				invoerVeld.setCaretPosition(cp - 1);
			}
		}
		else if(cp >= 2 && str.charAt(cp - 2) == '\u207F')
		{	invoerVeld.setText(str.substring(0, cp - 2) + str.substring(cp));
			invoerVeld.setCaretPosition(cp - 2);
		}
		else if(str.charAt(cp - 1) == '\u2080' || str.charAt(cp - 1) == '\u00B9')
		{	invoerVeld.setText(str.substring(0, cp - 2) + str.substring(cp));
			invoerVeld.setCaretPosition(cp - 2);
		}
		else
		{ 	invoerVeld.setText(str.substring(0, cp - 1) + str.substring(cp));
			invoerVeld.setCaretPosition(cp - 1);
		}
		if(invoerVeld.getText().equals(""))
		{	breuk = false;
			invers = false;
			invLabel.setVisible(false);
		}
	}
	
	public void doeActieDelete()
	{	String str = invoerVeld.getText();
		int cp = invoerVeld.getCaretPosition();
		int ss = invoerVeld.getSelectionStart();
		int se = invoerVeld.getSelectionEnd();
		
		if(nieuweInvoer)
		{	nieuweInvoer = false;
			invoerVeld.setCaretPosition(str.length());
		}
		if(cp == str.length())
			return;
		else if(se - ss > 0)
		{	invoerVeld.setText(str.substring(0, ss) + str.substring(se));
			cp = ss;
		}
		else if(str.charAt(cp)=='A')
			invoerVeld.setText(str.substring(0, cp) + str.substring(cp + 3));
		else if(str.charAt(cp) == 's' || str.charAt(cp) == 'c' || str.charAt(cp) == 't')
		{	if(str.charAt(cp + 3) == '(' )
				invoerVeld.setText(str.substring(0, cp) + str.substring(cp + 4));	
			else
				invoerVeld.setText(str.substring(0, cp) + str.substring(cp + 6));
		}
		else if(str.charAt(cp) == 'l')
			if(str.charAt(cp + 1) == 'n')
				invoerVeld.setText(str.substring(0, cp) + str.substring(cp + 3));
			else
				invoerVeld.setText(str.substring(0, cp) + str.substring(cp + 4));
		else if(str.charAt(cp) == '\u207F' || str.charAt(cp) == '\u2081' || str.charAt(cp) == '\u207B')
			invoerVeld.setText(str.substring(0, cp) + str.substring(cp + 2));
		else 
		invoerVeld.setText(str.substring(0, cp) + str.substring(cp + 1));
		
		invoerVeld.setCaretPosition(cp);
		if(invoerVeld.getText().equals(""))
		{	breuk = false;
			invers = false;
			invLabel.setVisible(false);
		}
	}
	
	public void vindAntwoord(boolean dec)
	{	syntaxError = false;
		maakBerekenbaar(invoerVeld.getText());
		bereken(sb);
		if(!syntaxError)
		{	String uitvoerTekst;
			if(breuk)
			{	if(teller % 1 == 0  && noemer %1 == 0 && !dec)
				{	int tellerInt = (int) teller;	
					int noemerInt = (int) noemer;
					int[] breuk  = simplify(tellerInt, noemerInt);
				
					int gehelenInt = breuk[0]/breuk[1];
					if(gehelenInt > 0)
					{	breuk[0] = breuk[0] - gehelenInt * breuk[1];
						if(breuk[0] == 0)
							uitvoerTekst = "" + gehelenInt;
						else
							uitvoerTekst = gehelenInt + "\u22A5" + breuk[0] + "\u22A5" + breuk[1];
					}
					else
					{	if(breuk[0] == 0)
							uitvoerTekst = "" + 0;
						else
							uitvoerTekst = breuk[0] + "\u22a5" + breuk[1];
					}
					if(!syntaxError)
						bewaardeAns = uitvoerTekst;
				}
				else
				{	eindUitkomst = teller/noemer;
					if(!syntaxError)
						bewaardeAns = Double.toString(eindUitkomst);
					if(eindUitkomst < Math.pow(10, 9))
						eindUitkomst = (double) Math.round(1000000000 * eindUitkomst)/1000000000;
					uitvoerTekst = Double.toString(eindUitkomst);
				}
			}
			else
			{	try{
				eindUitkomst = Double.parseDouble(sb.toString());}
				catch(Exception ex) 
				{	if(sb2.indexOf("E") > -1)
						try
						{	vindUitkomst("E", sb2);
							eindUitkomst = uitkomst;
						}
						catch(Exception exc)
						{	syntaxError = true;
							System.out.println("ERROR vindUitkomst(E) bij eindUitkomst");				
						}
					else if(sb2.indexOf("G") > -1)
						try
						{	vindUitkomst("G", sb2);
							eindUitkomst = uitkomst;
						}
						catch(Exception exc)
						{	syntaxError = true;
							System.out.println("ERROR vindUitkomst(G) bij eindUitkomst");				
						}
					else if(sb2.indexOf("\u22a5") > -1 || sb2.indexOf("B") > -1)
					{
						eindUitkomst = teller/noemer;
					}
				}
				if(!syntaxError)
					bewaardeAns = Double.toString(eindUitkomst);
				if(eindUitkomst < Math.pow(10, 9))
					eindUitkomst = (double) Math.round(1000000000 * eindUitkomst)/1000000000;
				uitvoerTekst = Double.toString(eindUitkomst);
			}
			if(uitvoerTekst.length() > 1 && uitvoerTekst.endsWith(".0"))
				uitvoerTekst = uitvoerTekst.substring(0, uitvoerTekst.length()-2);
		
			int indexE;
			String tienMachtString;
			if(uitvoerTekst.contains("E"))
			{	indexE = uitvoerTekst.indexOf('E');
				tienMachtString = uitvoerTekst.substring(indexE + 1);
				tienMachtString = tienMachtString.replaceAll("0", "\u2070");
				tienMachtString = tienMachtString.replaceAll("1", "\u00B9");
				tienMachtString = tienMachtString.replaceAll("2", "\u00B2");
				tienMachtString = tienMachtString.replaceAll("3", "\u00B3");
				tienMachtString = tienMachtString.replaceAll("4", "\u2074");
				tienMachtString = tienMachtString.replaceAll("5", "\u2075");
				tienMachtString = tienMachtString.replaceAll("6", "\u2076");
				tienMachtString = tienMachtString.replaceAll("7", "\u2077");
				tienMachtString = tienMachtString.replaceAll("8", "\u2078");
				tienMachtString = tienMachtString.replaceAll("9", "\u2079");     
				tienMachtString = tienMachtString.replaceAll("-", "\u207B");
				
				if(uitvoerTekst.substring(0, indexE).length() > 1 && uitvoerTekst.substring(0, indexE).endsWith(".0"))
					uitvoerTekst = uitvoerTekst.substring(0, indexE - 2) + "\u00D710" + tienMachtString;
				else
					uitvoerTekst = uitvoerTekst.substring(0, indexE)+ "\u00D710"+ tienMachtString;
				
			}
			if(CalculatorDwo.language.toString().equals("nl"))
				uitvoerTekst = uitvoerTekst.replace(".", ",");
			uitvoerVeld.setText(uitvoerTekst);
		}
		if(sb2.toString().contains("NaN") || sb.toString().contains("NaN") || sb2.toString().contains("Infinity")
				|| sb.toString().contains("Infinity"))
			uitvoerVeld.setText("Math ERROR");
		else if(syntaxError)
			uitvoerVeld.setText("Syntax ERROR");
		nieuweInvoer = true;
		breuk = false;
		invers = false;
		invLabel.setVisible(false);
		if(!insert)
		{	insert = true;
			invoerVeld.getCaret().setVisible(false);
			invoerVeld.setCaret(defaultCaret);
		}
		invoerVeld.getCaret().setBlinkRate(500);
		invoerVeld.getCaret().setVisible(false);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == gradenButton)
			graden = true;
		else if(e.getSource() == radialenButton)
			graden = false;
		else
		{	invoerVeld.getCaret().setVisible(true);
			String str = new String("");
			for(int i = 0; i < 10; i++)
				if(e.getSource() == getalKnop[i])
					voegInOfVervang(""+i, false);
			
			if(e.getSource() == piKnop)
				voegInOfVervang("\u03C0", false);
			else if(e.getSource() == plusKnop)
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
			else if(e.getSource() == eenGedeeldDoorKnop)
				voegInOfVervang("\u207B\u00B9", true);
			else if(e.getSource() == expKnop)
				voegInOfVervang("\u2081\u2080", true);
			else if(e.getSource() == haakLinksKnop)
				voegInOfVervang("(", false);
			else if(e.getSource() == haakRechtsKnop)
				voegInOfVervang(")", false);
			else if(e.getSource() == kommaKnop)
			{	if(CalculatorDwo.language.toString().equals("nl"))
					voegInOfVervang(",", false);
				else
					voegInOfVervang(".", false);
			}
			else if(e.getSource() == negatiefKnop)
				voegInOfVervang("-", false);
			else if(e.getSource() == ansKnop)
				voegInOfVervang("Ans", false);
			else if(e.getSource() == breukKnop)
			{	if(!invers)
					voegInOfVervang("\u22A5", false);
				else
					vindAntwoord(true);
			}
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
			else if(e.getSource() == nWortelKnop)
				voegInOfVervang("\u207F\u221A", false);
			else if(e.getSource() == invKnop)
			{	invers = !invers;
				invLabel.setVisible(invers);
			}
			else if(e.getSource() == cKnop)
			{	nieuweInvoer = false;
				breuk = false;
				invers = false;
				invLabel.setVisible(false);
				invoerVeld.setText("");
				uitvoerVeld.setText("0");
			}
			else if(e.getSource() == delKnop)
				doeActieBackSpace();
			else if(e.getSource() == pijlLinksKnop)
				maakStapNaarLinks();
			else if(e.getSource() == pijlRechtsKnop)
				maakStapNaarRechts();
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
			{	vindAntwoord(false);
				
			}
			if(e.getSource() != isKnop && (e.getSource() != breukKnop || !invers))
				invoerVeld.requestFocus();
		}
	}

	public void keyPressed(KeyEvent e) 
	{	invoerVeld.getCaret().setVisible(true);
		kc =e.getKeyCode();
		
		if(kc == KeyEvent.VK_LEFT)
			maakStapNaarLinks();
		if(kc == KeyEvent.VK_RIGHT)
			maakStapNaarRechts();
		if(kc == KeyEvent.VK_DELETE)
			doeActieDelete();
		if(kc == KeyEvent.VK_BACK_SPACE)
			doeActieBackSpace();
		if(e.isShiftDown() && kc == KeyEvent.VK_6)
			voegInOfVervang("^", true);
		e.consume();
	}

	public void keyReleased(KeyEvent e){}

	public void keyTyped(KeyEvent e) 
	{	invoerVeld.getCaret().setVisible(true);
		int kch = e.getKeyChar();
		if(kch == KeyEvent.VK_ENTER || kch == '=')
			vindAntwoord(false);
		else if(kch == '*')
			voegInOfVervang("\u00D7", true);
		else if(kch == '/' || kch == ':')
			voegInOfVervang("\u00F7", true);
		else if(kch == ',' || kch == '.')
		{	if(CalculatorDwo.language.toString().equals("nl"))
				voegInOfVervang(",", false);
			else
				voegInOfVervang(".", false);
		}
		else if(kch == '-')
			voegInOfVervang("\u2212", true);
		else if(kch == '+')
			voegInOfVervang("+", true);
		else if(kch =='(' || kch == ')' || Character.isDigit(kch))	
			voegInOfVervang("" + (char)kch, false);
			
		e.consume();
	}

	public void mouseClicked(MouseEvent arg0) {	
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}

	public void mousePressed(MouseEvent e) 
	{	String str = invoerVeld.getText();
		int cp = invoerVeld.getCaretPosition();
		
		if(cp == 0 || cp == str.length())
			return;
		else if(str.charAt(cp) == 'i' || str.charAt(cp) == 'o' || str.charAt(cp - 1) == 'l' 
			|| str.charAt(cp - 1) == 't' || (str.charAt(cp) == 'n' && str.charAt(cp + 1) == 's')
			|| str.charAt(cp - 1) == '\u2081' || str.charAt(cp - 1) == '\u207F'
			|| (str.charAt(cp - 1) == '\u207B' && cp > 1 && str.charAt(cp-2) != 'n' && (str.charAt(cp - 2) != 's' || (cp > 2 && str.charAt(cp - 3) == 'n'))))
			cp--;
		else if(cp < str.length() - 1 && str.charAt(cp + 1) == '\u207B' && (str.charAt(cp) == 'n' || (str.charAt(cp) == 's' && str.charAt(cp - 1) != 'n')))
			cp -= 2;
		else if(str.charAt(cp - 1) == 'n' && (str.charAt(cp) == 's' || str.charAt(cp) == '(')
				|| (cp > 2 && str.charAt(cp - 2) == '\u207B' && (str.charAt(cp - 3) == 'n' || (str.charAt(cp - 3) == 's' && cp > 3 && str.charAt(cp - 4) != 'n')))
				|| (str.charAt(cp) == '(' && (str.charAt(cp - 1) == 's' || str.charAt(cp - 1) == 'g')))
			cp++;
		else if((cp < str.length() - 1 && str.charAt(cp + 1) == '(' && (str.charAt(cp - 1) == 'o' || str.charAt(cp) == 'n')) 
			|| (cp > 1 && str.charAt(cp - 1) == '\u207B' && (str.charAt(cp - 2) == 'n' || (str.charAt(cp - 2) == 's' && cp > 2 && str.charAt(cp - 3) != 'n'))))
			cp += 2;
		else if(str.charAt(cp) == '\u207B' && (str.charAt(cp - 1) == 'n' || (str.charAt(cp - 1) == 's' && cp > 1 && str.charAt(cp - 2) != 'n')))
			cp += 3;
				
		invoerVeld.setCaretPosition(cp);
	}

	public void mouseReleased(MouseEvent e) {
		int ss = invoerVeld.getSelectionStart();
		int se = invoerVeld.getSelectionEnd();
		String str = invoerVeld.getText();
		
		if(ss > 0 && se - ss > 0)
			if(str.charAt(ss) == 'i' || str.charAt(ss) == 'o' || str.charAt(ss - 1) == 'l' 
				|| str.charAt(ss - 1) == 't' || (str.charAt(ss) == 'n' && str.charAt(ss + 1) == 's')
				|| str.charAt(ss - 1) == '\u2081' || str.charAt(ss - 1) == '\u207F'
				|| (str.charAt(ss - 1) == '\u207B' && ss > 1 && str.charAt(ss - 2) != 'n' && (str.charAt(ss - 2) != 's' || (ss > 2 && str.charAt(ss - 3) == 'n'))))
				ss--;
			else if(ss > 1 && (str.charAt(ss - 1) == 'i' || str.charAt(ss - 1) == 'o' 
					|| (str.charAt(ss - 1) == 'a' && str.charAt(ss - 2) == 't') 
					|| (str.charAt(ss - 1) == 'n' && (str.charAt(ss - 2) == 'l' || str.charAt(ss) == 's'))))
				ss -= 2;
			else if(ss > 2 && (str.charAt(ss - 2) == 'i' || str.charAt(ss - 2) == 'o' 
				|| (str.charAt(ss - 2) == 'a' && str.charAt(ss - 3) == 't')))
				ss -= 3;
			else if(ss > 3 && str.charAt(ss - 1) == '\u207B' && (str.charAt(ss - 3) == 'i' 
				|| str.charAt(ss - 3) == 'o' || (str.charAt(ss - 3) == 'a' && str.charAt(ss - 4) == 't')))
				ss -= 4;
			else if(ss > 4 && str.charAt(ss - 2) == '\u207B' && (str.charAt(ss - 4) == 'i' 
				|| str.charAt(ss - 4) == 'o' || (str.charAt(ss - 4) == 'a' && str.charAt(ss - 5) == 't')))
				ss -= 5;
		
		if(se < str.length() && se - ss > 0)
			if(se > 4 && str.charAt(se - 2) == '\u207B' && (str.charAt(se - 4) == 'i' 
				|| str.charAt(se - 4) == 'o' || (str.charAt(se - 4) == 'a' && str.charAt(se - 5) == 't')))
				se++;
			else if(se > 3 && str.charAt(se - 1) == '\u207B' && (str.charAt(se - 3) == 'i' 
				|| str.charAt(se - 3) == 'o' || (str.charAt(se - 3) == 'a' && str.charAt(se - 4) == 't')))
				se += 2;
			else if(se > 2 && str.length() > 5 && str.charAt(se) == '\u207B' && (str.charAt(se - 2) == 'i' 
				|| str.charAt(se - 2) == 'o' || (str.charAt(se - 2) == 'a' && str.charAt(se - 3) == 't')))
				se += 3;
			else if(se > 1 && se < str.length() - 1 && str.charAt(se + 1) == '\u207B' && (str.charAt(se - 1) == 'i' 
				|| str.charAt(se - 1) == 'o' || (str.charAt(se - 1) == 'a' && str.charAt(se - 2) == 't')))
				se += 4;
			else if(se > 0 && se < str.length() - 2 && str.charAt(se + 2) == '\u207B' && (str.charAt(se) == 'i' 
				|| str.charAt(se) == 'o' || (str.charAt(se) == 'a' && str.charAt(se - 1) == 't')))
				se += 5;
			else if(se > 2 && (str.charAt(se - 2) == 'i' || str.charAt(se - 2) == 'o' 
				|| (str.charAt(se - 2) == 'a' && str.charAt(se - 3) == 't')))
				se++;
			else if((se > 1 && (str.charAt(se - 1) == 'n' || str.charAt(se - 1) == '\u207F' || str.charAt(se - 1) == '\u207B'
					|| str.charAt(se - 1) == '\u2081') || (se > 2 && str.charAt(se - 2) == 'o')))
				se++;
			else if(str.charAt(se) == 'n' || (se > 1 && str.charAt(se - 1) == 'o'))
				se += 2;
			else if(str.charAt(se) == 'o' || (se > 0 && str.charAt(se - 1) == 's' && str.charAt(se) == 'i')
					|| (se > 0 && str.charAt(se - 1) == 't' && str.charAt(se) == 'a'))
				se += 3;
		
		invoerVeld.setSelectionStart(ss);
		invoerVeld.setSelectionEnd(se);
			
		
	}
	
	

}
