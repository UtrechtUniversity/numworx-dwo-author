package fi.binomverdeling;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
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
	
	public static final int N_MIN = 0; //min en max van n voor de sliders
	public static final int N_MAX = 100;
	
	private boolean nVeranderbaar;
	private boolean pVeranderbaar;
	private boolean successenVeranderbaar;
	
	private boolean showNSlider;
	private boolean showPSlider;
	private boolean showSuccessenSlider;
	
	private String nString;
	private String pString;
	private String successenString;
	
	/**
	 * Constructor
	 */
	public BVInteractiePanel() {
		super();
		super.setLayout(new BorderLayout());
		
		this.n = 30;
		this.p = 0.5;
		this.successen = 10;
        
		this.nVeranderbaar = true;
		this.pVeranderbaar = true;
		this.successenVeranderbaar = true;
		
		this.showNSlider = true;
		this.showPSlider = true;
		this.showSuccessenSlider = true;
		
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
        this.successenLabel = new JLabel("successen: ");
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
        
        this.setSlider(this.nSlider, (double)(this.n-BVInteractiePanel.N_MIN)/(double)(BVInteractiePanel.N_MAX - BVInteractiePanel.N_MIN));
        this.setSlider(this.pSlider, this.p);
        this.setSlider(this.successenSlider, (double)this.successen / (double)BVInteractiePanel.N_MAX);
        
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
	
	/**
	 * Hulpfunctie om de stand van een slider te krijgen
	 * @param slider de Slider waarvan je de stand wil weten
	 * @return op welk deel de slider staat, dus in[0;1]
	 */
	private double getPercentageFromSlider(Slider slider) {
		double d = (double)(slider.geefStand()-slider.getMinimum()) / (double)(slider.getMaximum()-slider.getMinimum());
		int i = (int)(d*10000);
		return i/10000.0;
	}
	
	/**
	 * Hulpfunctie om de slider te zetten
	 * @param slider De slider die je gaat zetten
	 * @param deel Het deel van de gehele lengte van de slider waarop je hem zet, moet element zijn van [0;1];
	 */
	private void setSlider(Slider slider, double deel) {
		slider.zetStand((int)(deel * (slider.getMaximum()-slider.getMinimum()) + slider.getMinimum()));
	}
	
	/**
	 * Update de view
	 */
	public void vernieuw() {
		if (!(nString.length() >= 3 && nString.charAt(0) == '#' && nString.charAt(nString.length()-1) == '#')) {
			this.nString = Integer.toString(this.n);
		}
		if (!(pString.length() >= 3 && pString.charAt(0) == '#' && pString.charAt(pString.length()-1) == '#')) {
			this.pString = Double.toString(this.p);
		}
		if (!(successenString.length() >= 3 && successenString.charAt(0) == '#' && successenString.charAt(successenString.length()-1) == '#')) {
			this.successenString = Integer.toString(this.successen);
		}
		
		//this.nText.setText(Integer.toString(this.n));
		this.nText.setText(this.nString);
		this.nText.setEditable(this.nVeranderbaar);
		
		//this.pText.setText(Double.toString(this.p));
		this.pText.setText(this.pString);
		this.pText.setEditable(this.pVeranderbaar);
		
		//this.successenText.setText(Integer.toString(this.successen));
		this.successenText.setText(this.successenString);
		this.successenText.setEditable(this.successenVeranderbaar);
		
		this.nSlider.setVisible(this.showNSlider);
		this.pSlider.setVisible(this.showPSlider);
		this.successenSlider.setVisible(this.showSuccessenSlider);
		this.nSlider.setEditable(this.nVeranderbaar);
		this.pSlider.setEditable(this.pVeranderbaar);
		this.successenSlider.setEditable(this.successenVeranderbaar);
		
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
		this.vernieuw();
	}
	public int getN() {
		return this.n;
	}
	
	public void setN(int n) {
		if(n >= 0) {
			this.n = n;
		}
		this.vernieuw();
	}
	
	public int getSuccessen() {
		return this.successen;
	}
	
	public void setSuccessen(int successen) {
		this.successen = successen;
		this.vernieuw();
	}
	public void setNVeranderbaar(boolean b) {
		this.nVeranderbaar = b;
		this.vernieuw();
	}
	public void setPVeranderbaar(boolean b) {
		this.pVeranderbaar = b;
		this.vernieuw();
	}
	public void setSuccessenVeranderbaar(boolean b) {
		this.successenVeranderbaar = b;
		this.vernieuw();
	}
	public void setShowXAs(boolean b) {
		this.staafjesPanel.setShowXAs(b);
	}
	public void setShowYAs(boolean b) {
		this.staafjesPanel.setShowYAs(b);
	}
	public void setShowNSlider(boolean b) {
		this.showNSlider = b;
		this.vernieuw();
	}
	public void setShowPSlider(boolean b) {
		this.showPSlider = b;
		this.vernieuw();
	}
	public void setShowSuccessenSlider(boolean b) {
		this.showSuccessenSlider = b;
		this.vernieuw();
	}
	
	/**
	 * Verwerkt de user interaction
	 */
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getActionCommand().equals("ntextupdate")) {
			this.nString = this.nText.getText();
			try {
				this.n = Integer.parseInt(this.nString);
			}
			catch (NumberFormatException e) {
				//wordt nu gedaan in update
				//if (!(nString.length() >= 3 && nString.charAt(0) == '#' && nString.charAt(nString.length()-1) == '#')) {
				//	this.nString = Integer.toString(this.n);
				//}
			}
			this.setSlider(this.nSlider, (double)(this.n-BVInteractiePanel.N_MIN)/(double)(BVInteractiePanel.N_MAX - BVInteractiePanel.N_MIN));
		}
		if (arg0.getActionCommand().equals("ptextupdate")) {
			this.pString = this.pText.getText();
			try {
				this.p = Double.parseDouble(this.pString);
			}
			catch (NumberFormatException e) {
				//wordt nu gedaan in update
				//if (!(pString.length() >= 3 && pString.charAt(0) == '#' && pString.charAt(pString.length()-1) == '#')) {
				//	this.pString = Double.toString(this.p);
				//}
			}
			this.setSlider(this.pSlider, this.p);
		}
		if (arg0.getActionCommand().equals("successentextupdate")) {
			this.successenString = this.successenText.getText();
			try {
				this.successen = Integer.parseInt(this.successenString);
			}
			catch (NumberFormatException e) {
				//wordt nu gedaan in update
				//if (!(successenString.length() >= 3 && successenString.charAt(0) == '#' && successenString.charAt(successenString.length()-1) == '#')) {
				//	this.successenString = Integer.toString(this.successen);
				//}
			}
			this.setSlider(this.successenSlider, (double)this.successen / (double)BVInteractiePanel.N_MAX);
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
		
		this.vernieuw();
	}
	
	/**
	 * Gekopiëerd uit Normale Verdeling, kleine aanpassingen gemaakt
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
		if (delen.length > 1) {
			decFactor = Integer.parseInt(delen[1]);
			d = d / decFactor;
		}
		if (Double.isNaN(d)) {
			d = def;
		}
		return d;
	}
	
	//====================================================================================================
	//==================Interface methoden================================================================
	//====================================================================================================
	
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
		this.setState(b);
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
		h.put("showXAs", new Boolean(this.staafjesPanel.getShowXAs()));
		h.put("showYAs", new Boolean(this.staafjesPanel.getShowYAs()));
		
		h.put("nVeranderbaar", new Boolean(this.nVeranderbaar));
		h.put("pVeranderbaar", new Boolean(this.pVeranderbaar));
		h.put("successenVeranderbaar", new Boolean(this.successenVeranderbaar));
		
		h.put("nString", this.nString);
		h.put("pString", this.pString);
		h.put("successenString", this.successenString);
		
		h.put("showNSlider", new Boolean(this.showNSlider));
		h.put("showPSlider", new Boolean(this.showPSlider));
		h.put("showSuccessenSlider", new Boolean(this.showSuccessenSlider));
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
			this.staafjesPanel.setShowXAs(((Boolean)b.get("showXAs")).booleanValue());
		}
		if (b.containsKey("showYAs")) {
			this.staafjesPanel.setShowYAs(((Boolean)b.get("showYAs")).booleanValue());
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
		if (b.containsKey("showNSlider")) {
			this.showNSlider = ((Boolean)b.get("showNSlider")).booleanValue();
		}
		if (b.containsKey("showPSlider")) {
			this.showPSlider = ((Boolean)b.get("showPSlider")).booleanValue();
		}
		if (b.containsKey("showSuccessenSlider")) {
			this.showSuccessenSlider = ((Boolean)b.get("showSuccessenSlider")).booleanValue();
		}
		
		this.vernieuw();
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
	
	/**
	 * Override setBounds, zet naast het panel ook de sliders op de juiste grootte
	 */
	public void setBounds(int x, int y, int b, int h) {
		//resize de sliders
		this.nSlider.zetLengte(b/3 - 10);
		this.pSlider.zetLengte(b/3 - 10);
		this.successenSlider.zetLengte(b/3 - 10);
		
		//zet de sliders weer op de goede stand
		this.setSlider(this.nSlider, (double)(this.n-BVInteractiePanel.N_MIN)/(double)(BVInteractiePanel.N_MAX - BVInteractiePanel.N_MIN));
		this.setSlider(this.pSlider, this.p);
		this.setSlider(this.successenSlider, (double)this.successen / (double)BVInteractiePanel.N_MAX);
		
		//zet bounds van panel
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
		this.setState(b);
		
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
		
		//zet de ..String variabelen goed
		this.nString = Integer.toString(this.n);
		this.pString = Double.toString(this.p);
		this.successenString = Integer.toString(this.successen);
		
		//update
		this.vernieuw();
	}
}