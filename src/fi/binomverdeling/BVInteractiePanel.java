package fi.binomverdeling;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Observable;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;


public class BVInteractiePanel extends JPanel implements InteractiePanel {
	private double p; //succeskans
	private int n; //aantal herhalingen
	private int successen;
	
	private int staafbreedte;
	private final int EDITHEIGHT = 30;
	private JTextField ntext;
	private JLabel nlabel;
	private JTextField ktext;
	private JLabel klabel;
	private JTextField ptext;
	private JLabel plabel;
	private JLabel totalekanslabel;
	private JPanel centerpanel;
	
	/**
	 * Constructor
	 * @param p succeskans
	 * @param n aantal herhalingen
	 * @param successen de grens voor berekenKansCumulatief
	 */
	public BVInteractiePanel() {
		this.p = 0.5;
		this.n = 30;
		this.successen = 15;
		
		this.setLayout(null);
		
		//Test-textfield
		/*
		JTextField textField = new JTextField();
		textField.setBounds(50,100,200,25);
		add(textField);
		*/
		
		//view setup
		super.setLayout(new BorderLayout());
		this.centerpanel = new JPanel();
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
        
        super.add(this.centerpanel, BorderLayout.CENTER);
        
        this.setVisible(true);
	}
	
	/**
	 * Ongeordende k-greep uit n, zonder terugleggen.
	 * @param n grootte van verzameling
	 * @param k grootte van de greep
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
		return BVInteractiePanel.binom(n, k) * (double)Math.pow(p,k) * (double)Math.pow(1-p, n-k);
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
	
	/**
	 * Zet een nieuwe waarde voor p
	 * @param p de nieuwe succeskans
	 */
	public void setP(double p) {
		if(p >= 0 && p <= 1) {
			this.p = p;
		}
		//this.setChanged();
		//this.notifyObservers();
	}
	
	/**
	 * @return de succeskans
	 */
	public double getP() {
		return this.p;
	}
	
	/**
	 * Zet een nieuwe waarde voor n
	 * @param n het nieuwe aantal herhalingen
	 */
	public void setN(int n) {
		if(n >= 0) {
			this.n = n;
		}
		//this.setChanged();
		//this.notifyObservers();
	}
	
	/**
	 * @return het aantal herhalingen
	 */
	public int getN(){
		return this.n;
	}
	
	public void setSuccessen(int successen) {
		this.successen = successen;
		//this.setChanged();
		//this.notifyObservers();
	}
	
	public int getSuccessen() {
		return this.successen;
	}	
	
	//==================== Einde "model" gedeelte, begin van de view
	
	public void paintStaafje(int k) {
		Graphics g = this.centerpanel.getGraphics();
		if(k<=this.successen) {
			g.setColor(Color.GRAY);
		}
		g.fillRect((2*k+1)*this.staafbreedte, this.centerpanel.getHeight() - (int)(this.berekenKansK(k)*(this.centerpanel.getHeight()-this.EDITHEIGHT)+this.EDITHEIGHT), this.staafbreedte, (int)(this.berekenKansK(k)*(this.centerpanel.getHeight()-this.EDITHEIGHT)));
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
        this.berekenStaafBreedte();
        g.clearRect(0, 0, this.centerpanel.getWidth(), this.centerpanel.getHeight());
		for(int k = 0; k <= this.n; k++) {
			this.paintStaafje(k);
		}
	}
	
	private void berekenStaafBreedte() {
		this.staafbreedte = this.centerpanel.getWidth()/(2*this.n+3);
	}
	
	public void update(Observable o, Object arg) {
		this.ntext.setText(Integer.toString(this.n));
		this.ptext.setText(Double.toString(this.p));
		this.ktext.setText(Integer.toString(this.successen));
		this.totalekanslabel.setText("P(X <= " + this.successen + ") = " + this.berekenKansCumulatief());
		this.repaint(200);
	}
	
	public void addNListener(ActionListener a) {
		this.ntext.addActionListener(a);
	}
	public void addPListener(ActionListener a) {
		this.ptext.addActionListener(a);
	}
	public void addKListener(ActionListener a) {
		this.ktext.addActionListener(a);
	}
	public String getNText() {
		return this.ntext.getText();
	}
	public String getPText() {
		return this.ptext.getText();
	}
	public String getKText() {
		return this.ktext.getText();
	}
	
	//======================================== Interface implementatie
	public void zetOpdracht(Hashtable h, String[] randomVars, Hashtable randomValues)
	{
		
	}
	
	public void setState(Hashtable h)
	{
		
	}
	
	
	public void setEditState(Hashtable h)
	{
		
		
		
	}
	
	public Hashtable getState()
	{
		Hashtable h = new Hashtable();
		return h;
	}
	
	public Hashtable getEditState()
	{
		Hashtable h = new Hashtable();
		return h;
	}
	
	public InteractieEditPanel getEditPanel()
	{
		return new BVInteractieEditPanel();
	}
	public void setBounds(int x, int y, int b, int h)
	{	
		super.setBounds(x,y,b,h);
	}
	public void wis()
	{
		
	}
	public void zetMaat()
	{
	
	}
	public int geefAsHoogte()
	{
		return 0;
	}
	public int getIpId()
	{
		return 0;
	}
	
	public int getScore()
	{
		return 0;
	}
	public int getScoreMax()
	{
		return 0;
	}
	public boolean isCorrect()
	{
		return true;
	}
	public boolean isFout()
	{
		return false;
	}
	public void zetMode(int mode)
	{
	
	}
	public void zetNagekeken(boolean b)
	{
	
	}
    public void stop()
	{
	
	}
    public void start()
	{
    	
	}
   
    public void destroy()
	{
	
	}
    public void opnieuw()
	{
	
	}
    public void kijkNa()
	{
	
	}
    public void kijkNa(int stapNr)
	{
	
	}
    public void addActionListener(ActionListener al)
	{
	
	}
	
	
	
}
