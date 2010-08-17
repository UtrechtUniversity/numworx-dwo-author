package fi.binomverdeling;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
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

import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;

/**
 * InteractiePanel van BinomVerdeling
 * Model, view en controller nu bij elkaar gevoegd.
 */
public class BVInteractiePanel extends JPanel implements InteractiePanel, ActionListener, ComponentListener {
	private double p; //succeskans
	private int n; //aantal herhalingen
	private int successen;
	private int staafbreedte;
	private JPanel centerpanel;
	private final int EDITHEIGHT = 30;
	private JTextField ntext;
	private JLabel nlabel;
	private JTextField ktext;
	private JLabel klabel;
	private JTextField ptext;
	private JLabel plabel;
	private JLabel totalekanslabel;
	
	private boolean nveranderbaar;
	private boolean pveranderbaar;
	private boolean successenveranderbaar;
	
	//De nog ongebruikte variabelen:
	private boolean showxas;
	private boolean showyas;
	
	/**
	 * Constructor
	 */
	public BVInteractiePanel() {
		super();
		//super.setPreferredSize(new Dimension(500,500));
		System.out.println("debug: height: " + this.getHeight() + "   width: " + this.getWidth());
		super.setLayout(new BorderLayout());
		
		this.n = 30;
		this.p = 0.5;
		this.successen = 10;
        
		this.showxas = true;
		this.showyas = true;
		this.nveranderbaar = true;
		this.pveranderbaar = true;
		this.successenveranderbaar = true;
		
        this.totalekanslabel = new JLabel("P(X <= " + this.successen + ") = " + this.berekenKansCumulatief());
		super.add(this.totalekanslabel, BorderLayout.SOUTH);
        this.ntext = new JTextField(5);
        this.ntext.setActionCommand("ntextupdate");
        this.ntext.setText(Integer.toString(this.n));
        
        this.ptext = new JTextField(5);
        this.ptext.setActionCommand("ptextupdate");
        this.ptext.setText(Double.toString(this.p));
        this.ktext = new JTextField(5);
        this.ktext.setActionCommand("ktextupdate");
        this.ktext.setText(Integer.toString(this.successen));
        
        this.nlabel = new JLabel("n_test");
        this.plabel = new JLabel("p");
        this.klabel = new JLabel("k");
        
        
        Container editbalk = new Container();
        LayoutManager layout = new GridLayout(0,6);
        editbalk.setLayout(layout);
        editbalk.add(this.nlabel);
        editbalk.add(this.ntext);
        editbalk.add(this.klabel);
        editbalk.add(this.ktext);
        editbalk.add(this.plabel);
        editbalk.add(this.ptext);
        super.add(editbalk, BorderLayout.NORTH);
        
        this.centerpanel = new JPanel();
        super.add(this.centerpanel, BorderLayout.CENTER);

        this.addComponentListener(this);
        super.setVisible(true);
        
	}
	
	/**
	 * Paint een enkel staafje
	 * @param k Het nummer van het te tekenen staafje
	 */
	public void paintStaafje(int k) {
		Graphics g = this.centerpanel.getGraphics();
		if(k<=this.successen) {
			g.setColor(Color.GRAY);
		}
		g.fillRect((2*k+1)*this.staafbreedte, this.centerpanel.getHeight() - (int)(this.berekenKansK(k)*(this.centerpanel.getHeight()-this.EDITHEIGHT)+this.EDITHEIGHT), this.staafbreedte, (int)(this.berekenKansK(k)*(this.centerpanel.getHeight()-this.EDITHEIGHT)));
	}
	/**
	 * Paint een enkel staafje, vergroot met een multiplier
	 * @param k Het nummer van het te tekenen staafje
	 * @param multiplier Het getal waarmee de lengte van het staafje vermenigvuldigd wordt
	 */
	public void paintStaafjeSchaal(int k,double multiplier) {
		Graphics g = super.getGraphics();
		if(k<=this.successen) {
			g.setColor(Color.GRAY);
		}
		g.fillRect((2*k+1)*this.staafbreedte, this.centerpanel.getHeight() - (int)(this.berekenKansK(k)*this.centerpanel.getHeight()*multiplier), this.staafbreedte, (int)(this.berekenKansK(k)*this.centerpanel.getHeight()*multiplier));
	}
	
	/**
	 * Paint alles
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);
        this.berekenStaafBreedte();
        g.clearRect(0, 0, this.centerpanel.getWidth(), this.centerpanel.getHeight());
		for(int k = 0; k <= this.n; k++) {
			this.paintStaafje(k);
		}
	}
	
	/**
	 * Bereken de breedte van de staafjes aan de hand van het aantal staafjes en de breedte van het panel
	 */
	private void berekenStaafBreedte() {
		this.staafbreedte = this.centerpanel.getWidth()/(2*this.n+3);
	}
	
	/**
	 * Update de view
	 */
	public void update() {
		System.out.println("debug: height: " + this.getHeight() + "   width: " + this.getWidth());
		this.ntext.setText(Integer.toString(this.n));
		this.ntext.setEditable(this.nveranderbaar);
		
		this.ptext.setText(Double.toString(this.p));
		this.ptext.setEditable(this.pveranderbaar);
		
		this.ktext.setText(Integer.toString(this.successen));
		this.ktext.setEditable(this.successenveranderbaar);
		
		this.totalekanslabel.setText("P(X <= " + this.successen + ") = " + this.berekenKansCumulatief());
		this.repaint(200);
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
	
	public void setP(double p) {
		if(p >= 0 && p <= 1) {
			this.p = p;
		}
		this.update();
	}
	public void setN(int n) {
		if(n >= 0) {
			this.n = n;
		}
		this.update();
	}
	public void setSuccessen(int successen) {
		this.successen = successen;
		this.update();
	}
	
	/**
	 * Verwerkt de user interaction
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getActionCommand().equals("ntextupdate")) {
			int n;
			try {
				n = Integer.parseInt(this.ntext.getText());
			}
			catch (NumberFormatException e) {
				n = this.n;
			}
			this.setN(n);
		}
		if (arg0.getActionCommand().equals("ptextupdate")) {
			double p;
			try {
				p = Double.parseDouble(this.ptext.getText());
			}
			catch (NumberFormatException e) {
				p = this.p;
			}
			this.setP(p);
		}
		if (arg0.getActionCommand().equals("ktextupdate")) {
			int k;
			try {
				k = Integer.parseInt(this.ktext.getText());
			}
			catch (NumberFormatException e) {
				k = this.successen;
			}
			this.setSuccessen(k);
		}
	}
	
	//=========================================================================
	// Interface methoden
	//=========================================================================
	
	@Override
	public void addActionListener(ActionListener al) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int geefAsHoogte() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public InteractieEditPanel getEditPanel() {
		return new BVInteractieEditPanel();
	}

	@Override
	public Hashtable getEditState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getIpId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getScore() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getScoreMax() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Geeft de volledige toestand in de vorm van een Hashtable
	 */
	@Override
	public Hashtable getState() {
		Hashtable h = new Hashtable();
		
		h.put("n", new Integer(this.n));
		h.put("p", new Double(this.p));
		h.put("successen", new Integer(this.successen));
		h.put("showxas", new Boolean(this.showxas));
		h.put("showyas", new Boolean(this.showyas));
		
		h.put("nveranderbaar", new Boolean(this.nveranderbaar));
		h.put("pveranderbaar", new Boolean(this.pveranderbaar));
		h.put("successenveranderbaar", new Boolean(this.successenveranderbaar));
		
		return h;
	}

	/**
	 * Zet de toestand naar de inhoud van de hashtable b
	 */
	@Override
	public void setState(Hashtable b) {
		if (b.containsKey("n")) {
			this.n = ((Integer)b.get("n")).intValue();
		}
		else {
			this.n = 30;
		}
		if (b.containsKey("p")) {
			this.p = ((Double)b.get("p")).doubleValue();
		}
		else {
			this.p = 0.5;
		}
		if (b.containsKey("successen")) {
			this.successen = ((Integer)b.get("successen")).intValue();
		}
		else {
			this.successen = 10;
		}
		if (b.containsKey("showxas")) {
			this.showxas = ((Boolean)b.get("showxas")).booleanValue();
		}
		else {
			this.showxas = true;
		}
		if (b.containsKey("showyas")) {
			this.showyas = ((Boolean)b.get("showyas")).booleanValue();
		}
		else {
			this.showyas = true;
		}
		if (b.containsKey("nveranderbaar")) {
			this.nveranderbaar = ((Boolean)b.get("nveranderbaar")).booleanValue();
		}
		else {
			this.nveranderbaar = true;
		}
		if (b.containsKey("pveranderbaar")) {
			this.pveranderbaar = ((Boolean)b.get("pveranderbaar")).booleanValue();
		}
		else {
			this.pveranderbaar = true;
		}
		if (b.containsKey("successenveranderbaar")) {
			this.successenveranderbaar = ((Boolean)b.get("successenveranderbaar")).booleanValue();
		}
		else {
			this.successenveranderbaar = true;
		}
		
		this.update();
	}
	
	@Override
	public boolean isCorrect() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isFout() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void kijkNa() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void kijkNa(int stapNr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void opnieuw() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBounds(int x, int y, int b, int h) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEditState(Hashtable b) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void wis() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void zetMaat() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void zetMode(int mode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void zetNagekeken(boolean b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void zetOpdracht(Hashtable b, String[] randomVars,
			Hashtable randomValues) {
		// TODO Auto-generated method stub
		
	}

	//============================ ComponentListener implementatie
	@Override
	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentResized(ComponentEvent arg0) {
		this.update();
		
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
