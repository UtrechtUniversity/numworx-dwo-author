package fi.binomverdeling;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;

/**
 * InteractiePanel van BinomVerdeling
 * Model, view en controller nu bij elkaar gevoegd.
 */
public class BVInteractiePanel extends JPanel implements InteractiePanel, ActionListener {
	private double p; //succeskans
	private int n; //aantal herhalingen
	private int successen;
	private BVStaafjesPanel staafjesPanel;
	public final int EDITHEIGHT = 0;
	private JTextField nText;
	private JLabel nLabel;
	private JTextField successenText;
	private JLabel successenLabel;
	private JTextField pText;
	private JLabel pLabel;
	private JLabel totaleKansLabel;
	private Slider nSlider;
	private Slider pSlider;
	private Slider successenSlider;
	
	public static final int N_MIN = 1;
	public static final int N_MAX = 100;
	
	private boolean nVeranderbaar;
	private boolean pVeranderbaar;
	private boolean successenVeranderbaar;
	
	private String nString;
	private String pString;
	private String successenString;
	
	//De nog ongebruikte variabelen:
	private boolean showXAs;
	private boolean showYAs;
	
	
	/**
	 * Constructor
	 */
	public BVInteractiePanel() {
		super();
		super.setLayout(new BorderLayout());
		
		this.n = 30;
		this.p = 0.5;
		this.successen = 10;
        
		this.showXAs = true;
		this.showYAs = true;
		this.nVeranderbaar = true;
		this.pVeranderbaar = true;
		this.successenVeranderbaar = true;
		
		this.nString = Integer.toString(this.n);
		this.pString = Double.toString(this.p);
		this.successenString = Integer.toString(this.successen);
		
        this.totaleKansLabel = new JLabel("P(X <= " + this.successen + ") = " + this.berekenKansCumulatief());
		super.add(this.totaleKansLabel, BorderLayout.SOUTH);
        this.nText = new JTextField(5);
        this.nText.setActionCommand("ntextupdate");
        this.nText.setText(Integer.toString(this.n));
        
        this.pText = new JTextField(5);
        this.pText.setActionCommand("ptextupdate");
        this.pText.setText(Double.toString(this.p));
        this.successenText = new JTextField(5);
        this.successenText.setActionCommand("successentextupdate");
        this.successenText.setText(Integer.toString(this.successen));
        
        this.nLabel = new JLabel("n: ");
        this.nLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        this.pLabel = new JLabel("p: ");
        this.pLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        this.successenLabel = new JLabel("k: ");
        this.successenLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        
        JPanel editBalk = new JPanel();
        editBalk.setBackground(Color.WHITE);
        editBalk.setLayout(new GridLayout(1,6));
        editBalk.add(this.nLabel);
        editBalk.add(this.nText);
        editBalk.add(this.successenLabel);
        editBalk.add(this.successenText);
        editBalk.add(this.pLabel);
        editBalk.add(this.pText); 
        
        
        JPanel noordBalk = new JPanel();
        noordBalk.setLayout(new GridLayout(2,1));
        super.add(noordBalk, BorderLayout.NORTH);
        
        noordBalk.add(editBalk);
        
        JPanel sliderBalk = new JPanel();
        sliderBalk.setLayout(new GridLayout(1,3));
        
        
        this.staafjesPanel = new BVStaafjesPanel(this);
        super.add(this.staafjesPanel, BorderLayout.CENTER);
        
        this.nText.addActionListener(this);
        this.pText.addActionListener(this);
        this.successenText.addActionListener(this);
        
        this.nSlider = new Slider(100,50);
        this.pSlider = new Slider(100,50);
        this.successenSlider = new Slider(100,50);
        
        this.nSlider.addActionListener(this);
        this.pSlider.addActionListener(this);
        this.successenSlider.addActionListener(this);
        
        sliderBalk.add(this.nSlider);
        sliderBalk.add(this.successenSlider);
        sliderBalk.add(this.pSlider);
        sliderBalk.setBackground(Color.WHITE);
        
        noordBalk.add(sliderBalk);
        
        this.add(noordBalk, BorderLayout.NORTH);
	}
	
	private double getPercentageFromSlider(Slider slider) {
		double d = (double)(slider.geefStand()-slider.getMinimum()) / (double)(slider.getMaximum()-slider.getMinimum());
		int i = (int)(d*10000);
		return i/10000.0;
	}
	
	private void setPSlider() {
		this.pSlider.zetStand((int)(this.p * (this.pSlider.getMaximum()-this.pSlider.getMinimum()) + this.pSlider.getMinimum()));
	}
	
	private int getNFromSlider() {
		return 0; //TODO
	}
	
	/**
	 * Update de view
	 */
	public void update() {
		
		this.nText.setText(Integer.toString(this.n));
		//this.nText.setText(this.nString);
		this.nText.setEditable(this.nVeranderbaar);
		
		this.pText.setText(Double.toString(this.p));
		//this.pText.setText(this.pString);
		this.pText.setEditable(this.pVeranderbaar);
		
		this.successenText.setText(this.successenString);
		this.successenText.setEditable(this.successenVeranderbaar);
		
		this.totaleKansLabel.setText("P(X <= " + this.successen + ") = " + this.berekenKansCumulatief());
		this.repaint();
	}
	
	/**
	 * Aantal mogelijkheden voor een k-greep uit n
	 * @return C(n,k)
	 */
	public static double binom(int n, int k)
	{
		double[] b = new double[n+1];
		b[0] = 1;
		for(int i=1 ; i<n+1 ; i++)
		{	b[i] = 1;
			for(int j=i-1 ; j>0 ; j--)
			{	b[j] += b[j-1];
			}
		}
		return b[k];
	}
	
	/**
	 * komt neer op BinomPDF
	 * @param k het aantal te behalen successen
	 * @return P(X=k)
	 */
	public double berekenKansK(int k) {
		return BVInteractiePanelModel.binom(n, k) * (double)Math.pow(p,k) * (double)Math.pow(1-p, n-k);
	}
	
	/**
	 * Komt neer op BinomCDF
	 * @return P(X<=this.successen)
	 */
	public double berekenKansCumulatief() {
		if(this.successen >= this.n) { //als successen >= n, dan telt alles mee, dus is de som 1
			return 1.0;
		}
		else {
			double som = 0;
			for (int count = 0; count <= Math.min(this.successen, this.n); count++) {
				som += this.berekenKansK(count);
			}
			return som;
		}
	}
	public double getP() {
		return this.p;
	}
	
	public void setP(double p) {
		if(p >= 0 && p <= 1) {
			this.p = p;
		}
		this.update();
	}
	public int getN() {
		return this.n;
	}
	
	/*
	public void setN(int n) {
		if(n >= 0) {
			this.n = n;
		}
		this.update();
	}
	*/
	
	public int getSuccessen() {
		return this.successen;
	}
	
	public void setSuccessen(int successen) {
		this.successen = successen;
		this.update();
	}
	public void setNVeranderbaar(boolean b) {
		this.nVeranderbaar = b;
		this.update();
	}
	public void setPVeranderbaar(boolean b) {
		this.pVeranderbaar = b;
		this.update();
	}
	public void setSuccessenVeranderbaar(boolean b) {
		this.successenVeranderbaar = b;
		this.update();
	}
	public void setShowXAs(boolean b) {
		this.showXAs = b;
		//TODO implementeer
	}
	public void setShowYAs(boolean b) {
		this.showYAs = b;
		//TODO implementeer
	}
	
	/**
	 * Verwerkt de user interaction
	 */
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getActionCommand().equals("ntextupdate")) {
			//TODO this.setNSlider();
			
			this.nString = this.nText.getText();
			try {
				this.n = Integer.parseInt(this.nString);
			}
			catch (NumberFormatException e) {
				//if (!(nString.length() >= 3 && nString.charAt(0) == '#' && nString.charAt(nString.length()-1) == '#')) {
				//	this.nString = Integer.toString(this.n);
				//}
			}
		}
		if (arg0.getActionCommand().equals("ptextupdate")) {
			this.setPSlider();
			this.pString = this.pText.getText();
			try {
				this.p = Double.parseDouble(this.pString);
			}
			catch (NumberFormatException e) {
				//if (!(pString.length() >= 3 && pString.charAt(0) == '#' && pString.charAt(pString.length()-1) == '#')) {
				//	this.pString = Double.toString(this.p);
				//}
			}
		}
		if (arg0.getActionCommand().equals("successentextupdate")) {
			//TODO this.setSuccessenSlider();
			this.successenString = this.successenText.getText();
			try {
				this.successen = Integer.parseInt(this.successenString);
			}
			catch (NumberFormatException e) {
				//if (!(successenString.length() >= 3 && successenString.charAt(0) == '#' && successenString.charAt(successenString.length()-1) == '#')) {
				//	this.successenString = Integer.toString(this.successen);
				//}
			}
		}
		if (arg0.getSource() == this.nSlider) {
			this.n = (int)(this.getPercentageFromSlider(this.nSlider)*(BVInteractiePanel.N_MAX - BVInteractiePanel.N_MIN) + BVInteractiePanel.N_MIN);
		}
		
		if (arg0.getSource() == this.pSlider) {
			this.p = this.getPercentageFromSlider(this.pSlider);
		}
		
		if (arg0.getSource() == this.successenSlider) {
			this.successen = (int)(this.getPercentageFromSlider(this.successenSlider)*BVInteractiePanel.N_MAX);
		}
		
		this.update();
	}
	
	/**
	 * Gekopiëerd uit Normale Verdeling
	 */
	public static double substitueerRandom(double def, String s, String[] randomVars, Hashtable randomValues) {
		double d = Double.NaN;
		s = s.substring(1, s.length() - 1);
		//String[] delen = StringUtils.split(s, "/");
		String[] delen = s.split("/");
		int decFactor = 1;
		for (int j = 0 ; j < randomVars.length; j++) {
			if (randomVars[j].equals(delen[0])) {
				d = ((Integer) randomValues.get(randomVars[j])).intValue();
			}
		}
		System.out.println("Debug:");
		for (String a: delen) {
			System.out.println(a);
		}
		System.out.println(delen.length);
		if (delen.length > 1) {
			decFactor = Integer.parseInt(delen[1]);
			d = d / decFactor;
		}
		if (Double.isNaN(d)) {
			d = def;
		}
		return d;
	}
	
	//=========================================================================
	//==================Interface methoden=====================================
	//=========================================================================
	
	public void addActionListener(ActionListener al) {
		
	}

	
	public void destroy() {
		
	}

	
	public int geefAsHoogte() {
		return 0;
	}

	
	public InteractieEditPanel getEditPanel() {
		return new BVInteractieEditPanel();
	}

	
	/**
	 * Wordt aangeroepen door getEditState van BVInteractieEditPanel, omdat een deel
	 * van de editgegevens in BVInteractiePanel staan.
	 */
	public Hashtable getEditState() {
		Hashtable h = new Hashtable();
		h.put("n", new Integer(this.n));
		h.put("p", new Double(this.p));
		h.put("successen", new Integer(this.successen));
		
		h.put("nString", new String(this.nString));
		h.put("pString", new String(this.pString));
		h.put("successenString", new String(this.successenString));
		
		return h;
	}
	
	/**
	 * Zet de oude Editgegevens voor BVInteractiePanel als je met BVInteractieEditPanel 
	 * nieuwe editgegevens gaat maken.
	 */
	public void setEditState(Hashtable b) {
		if (b.containsKey("n")) {
			this.n = ((Integer)b.get("n")).intValue();
		}
		if (b.containsKey("p")) {
			this.p = ((Double)b.get("p")).doubleValue();
		}
		if (b.containsKey("successen")) {
			this.successen = ((Integer)b.get("successen")).intValue();
		}
		if (b.containsKey("showXAs")) {
			this.showXAs = ((Boolean)b.get("showXAs")).booleanValue();
		}
		if (b.containsKey("showYAs")) {
			this.showYAs = ((Boolean)b.get("showYAs")).booleanValue();
		}
		if (b.containsKey("nVeranderbaar")) {
			this.nVeranderbaar = ((Boolean)b.get("nVeranderbaar")).booleanValue();
		}
		if (b.containsKey("pVeranderbaar")) {
			this.pVeranderbaar = ((Boolean)b.get("pVeranderbaar")).booleanValue();
		}
		if (b.containsKey("successenVeranderbaar")) {
			this.successenVeranderbaar = ((Boolean)b.get("successenVeranderbaar")).booleanValue();
		}
		if (b.containsKey("nString")) {
			this.nString = (String)b.get("nString");
		}
		if (b.containsKey("pString")) {
			this.pString = (String)b.get("pString");
		}
		if (b.containsKey("successenString")) {
			this.successenString = (String)b.get("successenString");
		}
		
		this.update();
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

	/**
	 * Geeft de volledige toestand in de vorm van een Hashtable
	 */
	
	public Hashtable getState() {
		Hashtable h = new Hashtable();
		
		h.put("n", new Integer(this.n));
		h.put("p", new Double(this.p));
		h.put("successen", new Integer(this.successen));
		h.put("showXAs", new Boolean(this.showXAs));
		h.put("showYAs", new Boolean(this.showYAs));
		
		h.put("nVeranderbaar", new Boolean(this.nVeranderbaar));
		h.put("pVeranderbaar", new Boolean(this.pVeranderbaar));
		h.put("successenVeranderbaar", new Boolean(this.successenVeranderbaar));
		
		h.put("nString", this.nString);
		h.put("pString", this.pString);
		h.put("successenString", this.successenString);
		return h;
	}

	/**
	 * Zet de toestand naar de inhoud van de hashtable b
	 */
	
	public void setState(Hashtable b) {
		if (b.containsKey("n")) {
			this.n = ((Integer)b.get("n")).intValue();
		}
		if (b.containsKey("p")) {
			this.p = ((Double)b.get("p")).doubleValue();
		}
		if (b.containsKey("successen")) {
			this.successen = ((Integer)b.get("successen")).intValue();
		}
		if (b.containsKey("showXAs")) {
			this.showXAs = ((Boolean)b.get("showXAs")).booleanValue();
		}
		if (b.containsKey("showYAs")) {
			this.showYAs = ((Boolean)b.get("showYAs")).booleanValue();
		}
		if (b.containsKey("nVeranderbaar")) {
			this.nVeranderbaar = ((Boolean)b.get("nVeranderbaar")).booleanValue();
		}
		if (b.containsKey("pVeranderbaar")) {
			this.pVeranderbaar = ((Boolean)b.get("pVeranderbaar")).booleanValue();
		}
		if (b.containsKey("successenVeranderbaar")) {
			this.successenVeranderbaar = ((Boolean)b.get("successenVeranderbaar")).booleanValue();
		}
		if (b.contains("nString")) {
			this.nString = (String)b.get("nString");
		}
		if (b.contains("pString")) {
			this.pString = (String)b.get("pString");
		}
		if (b.contains("successenString")) {
			this.successenString = (String)b.get("successenString");
		}
		
		this.update();
	}
	
	public boolean isCorrect() {
		return false;
	}
	
	public boolean isFout() {
		
		return false;
	}
	
	public void kijkNa() {
	}
	
	public void kijkNa(int stapNr) {
	}
	
	public void opnieuw() {
	}
	
	public void setBounds(int x, int y, int b, int h) {
		super.setBounds(x, y, b, h);
	}

	public void start() {
	}

	public void stop() {
	}

	public void wis() {
	}

	public void zetMaat() {
	}

	public void zetMode(int mode) {
	}

	public void zetNagekeken(boolean b) {
	}

	/**
	 * Initialiseer zoals in de BVInteractieEditPanel is ingesteld, met evt. randomvars.
	 * @param b De hashtable die uit getEditState() komt
	 * @param randomVars De namen van de random variabelen
	 * @param randomValues de waarden van alle random variabelen
	 */
	public void zetOpdracht(Hashtable b, String[] randomVars, Hashtable randomValues) {
		//zet gegevens uit getEditState hashtable
		if (b.containsKey("n")) {
			this.n = ((Integer)b.get("n")).intValue();
		}
		if (b.containsKey("p")) {
			this.p = ((Double)b.get("p")).doubleValue();
		}
		if (b.containsKey("successen")) {
			this.successen = ((Integer)b.get("successen")).intValue();
		}
		if (b.containsKey("showXAs")) {
			this.showXAs = ((Boolean)b.get("showXAs")).booleanValue();
		}
		if (b.containsKey("showYAs")) {
			this.showYAs = ((Boolean)b.get("showYAs")).booleanValue();
		}
		if (b.containsKey("nVeranderbaar")) {
			this.nVeranderbaar = ((Boolean)b.get("nVeranderbaar")).booleanValue();
		}
		if (b.containsKey("pVeranderbaar")) {
			this.pVeranderbaar = ((Boolean)b.get("pVeranderbaar")).booleanValue();
		}
		if (b.containsKey("successenVeranderbaar")) {
			this.successenVeranderbaar = ((Boolean)b.get("successenVeranderbaar")).booleanValue();
		}
		
		//vul randomvars in en zet textboxes
		if(b.containsKey("nString")) {
			String nString = new String((String)b.get("nString"));
			this.nText.setText(nString);
			if (nString.length() >= 3 && nString.charAt(0) == '#' && nString.charAt(nString.length()-1) == '#') {
				this.n = (int) BVInteractiePanel.substitueerRandom((double)this.n, nString, randomVars, randomValues);
			}
		}
		if(b.containsKey("pString")) {
			String pString = new String((String)b.get("pString"));
			this.pText.setText(pString);
			if (pString.length() >= 3 && pString.charAt(0) == '#' && pString.charAt(pString.length()-1) == '#') {
				this.p = BVInteractiePanel.substitueerRandom(this.p, pString, randomVars, randomValues);
			}
		}
		if(b.containsKey("successenString")) {
			String successenString = new String((String)b.get("successenString"));
			this.successenText.setText(successenString);
			if (successenString.length() >= 3 && successenString.charAt(0) == '#' && successenString.charAt(successenString.length()-1) == '#') {
				this.successen = (int)BVInteractiePanel.substitueerRandom((double)this.successen, successenString, randomVars, randomValues);
			}
		}
		
		this.nString = Integer.toString(this.n);
		this.pString = Double.toString(this.p);
		this.successenString = Integer.toString(this.successen);
		
		//update
		this.update();
	}
}