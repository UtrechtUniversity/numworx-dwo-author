package fi.binomverdeling;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
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
	
	private boolean nVeranderbaar;
	private boolean pVeranderbaar;
	private boolean successenVeranderbaar;
	
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
        LayoutManager layout = new GridLayout(0,6);
        editBalk.setLayout(layout);
        editBalk.add(this.nLabel);
        editBalk.add(this.nText);
        editBalk.add(this.successenLabel);
        editBalk.add(this.successenText);
        editBalk.add(this.pLabel);
        editBalk.add(this.pText);
        super.add(editBalk, BorderLayout.NORTH);
        
        this.staafjesPanel = new BVStaafjesPanel(this);
        super.add(this.staafjesPanel, BorderLayout.CENTER);
        
        this.nText.addActionListener(this);
        this.pText.addActionListener(this);
        this.successenText.addActionListener(this);
        super.setVisible(true);
        
	}
	
	/**
	 * Update de view
	 */
	public void update() {
		this.nText.setText(Integer.toString(this.n));
		this.nText.setEditable(this.nVeranderbaar);
		
		this.pText.setText(Double.toString(this.p));
		this.pText.setEditable(this.pVeranderbaar);
		
		this.successenText.setText(Integer.toString(this.successen));
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
	
	public void setN(int n) {
		if(n >= 0) {
			this.n = n;
		}
		this.update();
	}
	
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
			int n;
			try {
				n = Integer.parseInt(this.nText.getText());
				this.setN(n);
			}
			catch (NumberFormatException e) {
				//n = this.n;
				//this.setN(n);
			}
		}
		if (arg0.getActionCommand().equals("ptextupdate")) {
			double p;
			try {
				p = Double.parseDouble(this.pText.getText());
				this.setP(p);
			}
			catch (NumberFormatException e) {
				//p = this.p;
				//this.setP(p);
			}
		}
		if (arg0.getActionCommand().equals("successentextupdate")) {
			int k;
			try {
				k = Integer.parseInt(this.successenText.getText());
				this.setSuccessen(k);
			}
			catch (NumberFormatException e) {
				//k = this.successen;
				//this.setSuccessen(k);
			}
		}
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
		
		h.put("nString", new String(this.nText.getText()));
		h.put("pString", new String(this.pText.getText()));
		h.put("successenString", new String(this.successenText.getText()));
		
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
			this.nText.setText(new String((String)b.get("nString")));
		}
		if (b.containsKey("pString")) {
			this.pText.setText(new String((String)b.get("pString")));
		}
		if (b.containsKey("successenString")) {
			this.successenText.setText(new String((String)b.get("successenString")));
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
		
		/*
		if(b.containsKey("nString")) {
			String nString = new String((String)b.get("nString"));
			if ()
		}
		*/
		//TODO randomvars
		
		this.update();
	}
}