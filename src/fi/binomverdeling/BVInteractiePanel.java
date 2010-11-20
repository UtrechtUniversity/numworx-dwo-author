package fi.binomverdeling;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Hashtable;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;

/**
 * InteractiePanel van BinomVerdeling
 * Model, view en controller nu bij elkaar gevoegd.
 */
public class BVInteractiePanel extends JPanel implements InteractiePanel, ActionListener, FocusListener {
	private double p; //succeskans
	private int n; //aantal herhalingen
	
	private int M; //voor hypergeometrisch
	private int greep; //voor hypergeometrisch
	
	//private int successen;
	//private int grensLinks; //gebruikt als voor tweegrenzen is gekozen
	//private int grensRechts; //geld als grens in geval van 1 grens, geld als linkergrens in geval van twee grenzen
	
	private GrenzenOptie grenzenOptie;
	private boolean tweeGrenzen; //true = 2 grenzen, false = 1 grens
	
	private boolean hypergeometrisch; //true: Hypergeometrisch, false: Binomiaal
	
	private BVStaafjesPanel staafjesPanel;
	private JTextField nText;
	private JLabel nLabel;
	private JTextField pText;
	private JLabel pLabel;
	private JRadioButton kansRadioLinks;
	private JRadioButton kansRadioMidden;
	private JRadioButton kansRadioRechts;
	private JLabel kansLabelLinks;
	private JLabel kansLabelMidden;
	private JLabel kansLabelRechts;
	
	private JPanel kansBalk;
	private JPanel zuidBalk;
	private JPanel noordBalk;
	
	private Slider nSlider;
	private Slider pSlider;
	private JCheckBox grenzenBox;
	private JCheckBox hypergeometrischBox;
	
	public static final int N_MIN = 0; //min en max van n voor de sliders
	public static final int N_MAX = 100;
	
	private boolean nVeranderbaar;
	private boolean pVeranderbaar;
	
	private boolean showNSlider;
	private boolean showPSlider;
	
	private BVInvoer nInvoer;
	private BVInvoer pInvoer;
	
	//private String nString;
	//private String pString;
	
	private boolean showNoordBalk;
	private boolean showTweeGrenzenKeuze;
	private boolean showKansBalk;
		
	private Font font;
	private FontMetrics fontMetrics;
	
	public final Color STAAFJE_TELT = new Color(110,5,165);
	public final Color STAAFJE_TELT_NIET = new Color(234,229,255);
	public final Color LABEL_BACKGROUND = new Color(240,247,255);	
	public final Color LABEL_ACTIEF = new Color(200,227,255);
	
	private JPanel noordLinks;
	private JPanel noordRechts;
	public final int NOORDBALKHEIGHT = 43;
	
	private Rectangle lastBounds; //om bij te houden wanneer de maat bounds veranderen
	
	/**
	 * Constructor
	 */
	public BVInteractiePanel() {
		super();
		super.setLayout(new BorderLayout());
		
		this.font = new Font("Dialog", Font.PLAIN, 12);
		this.fontMetrics = getFontMetrics(this.font);
		
		this.n = 30;
		this.p = 0.5;
		
		this.nInvoer = new BVInvoer("30");
		this.pInvoer = new BVInvoer("0.5");
		
		this.hypergeometrisch = false;
		this.M = 0;
		
		this.grenzenOptie = GrenzenOptie.LINKS;
		
		this.nVeranderbaar = true;
		this.pVeranderbaar = true;
		
		this.showNSlider = true;
		this.showPSlider = true;
				
		this.showNoordBalk = true;
		this.showTweeGrenzenKeuze = true;
		this.showKansBalk = true;
		
		//maak het paneel wat in de center van de BorderLayout komt
        this.staafjesPanel = new BVStaafjesPanel(this, this.grenzenOptie);
        super.add(this.staafjesPanel, BorderLayout.CENTER);
		
        this.kansLabelLinks = new JLabel();
        this.kansLabelLinks.setBackground(this.LABEL_BACKGROUND);
        this.kansLabelLinks.setOpaque(true);
        this.kansLabelLinks.setFont(this.font);
        this.kansLabelLinks.setHorizontalAlignment(SwingConstants.CENTER);
        this.kansLabelMidden = new JLabel();
        this.kansLabelMidden.setBackground(this.LABEL_ACTIEF);
        this.kansLabelMidden.setOpaque(true);
        this.kansLabelMidden.setFont(this.font);
        this.kansLabelMidden.setHorizontalAlignment(SwingConstants.CENTER);
        this.kansLabelRechts = new JLabel();
        this.kansLabelRechts.setBackground(this.LABEL_BACKGROUND);
        this.kansLabelRechts.setOpaque(true);
        this.kansLabelRechts.setFont(this.font);
        this.kansLabelRechts.setHorizontalAlignment(SwingConstants.CENTER);
        
        this.zuidBalk = new JPanel();
        this.zuidBalk.setLayout(new GridLayout(2,1));
                
		this.kansBalk = new JPanel();
		GridLayout gl = new GridLayout(1,3);
		gl.setHgap(5);
		this.kansBalk.setLayout(gl);
		this.kansBalk.setBackground(Color.WHITE);
		this.kansRadioLinks = new JRadioButton(this.kansLabelLinksTekst(), true);
		this.kansRadioMidden = new JRadioButton(this.kansLabelMiddenTekst(), false);
		this.kansRadioRechts = new JRadioButton(this.kansLabelRechtsTekst(), false);

		this.kansRadioLinks.setFont(this.font);
		this.kansRadioMidden.setFont(this.font);
		this.kansRadioRechts.setFont(this.font);
		
		this.kansRadioLinks.setHorizontalAlignment(SwingConstants.CENTER);
		this.kansRadioMidden.setHorizontalAlignment(SwingConstants.CENTER);
		this.kansRadioRechts.setHorizontalAlignment(SwingConstants.CENTER);
		
		this.kansRadioLinks.addActionListener(this);
		this.kansRadioMidden.addActionListener(this);
		this.kansRadioRechts.addActionListener(this);
		
		this.kansRadioLinks.setBackground(this.LABEL_ACTIEF);
		this.kansRadioMidden.setBackground(this.LABEL_BACKGROUND);
		this.kansRadioRechts.setBackground(this.LABEL_BACKGROUND);
		
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(this.kansRadioLinks);
		buttonGroup.add(this.kansRadioMidden);
		buttonGroup.add(this.kansRadioRechts);
		
		this.kansBalk.add(this.kansRadioLinks);
		this.kansBalk.add(this.kansRadioMidden);
		this.kansBalk.add(this.kansRadioRechts);
		this.zuidBalk.add(this.kansBalk);
		
		this.grenzenBox = new JCheckBox("Twee grenswaarden", false);
		this.grenzenBox.setFont(this.font);
		this.grenzenBox.setBackground(Color.WHITE);
		this.grenzenBox.addActionListener(this);
		
		this.hypergeometrischBox = new JCheckBox("Hypergeometrisch", false);
		this.hypergeometrischBox.setFont(this.font);
		this.hypergeometrischBox.setBackground(Color.WHITE);
		this.hypergeometrischBox.addActionListener(this);
		//TODO Bezig met hyperBox, moet in zuidBalk er bij
		
		this.zuidBalk.add(this.grenzenBox);
		
		super.add(this.zuidBalk, BorderLayout.SOUTH);
		
        this.nText = new JTextField(4);
        this.nText.setActionCommand("ntextupdate");
        this.nText.setText(Integer.toString(this.n));
        
        this.pText = new JTextField(4);
        this.pText.setActionCommand("ptextupdate");
        this.pText.setText(Double.toString(this.p));
        
        this.nLabel = new JLabel("n = ");
        this.nLabel.setFont(this.font);
        this.nLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        this.pLabel = new JLabel("p = ");
        this.pLabel.setFont(this.font);
        this.pLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        this.nText.addActionListener(this);
        this.nText.addFocusListener(this);
        this.pText.addActionListener(this);
        this.pText.addFocusListener(this);
        
        this.nSlider = new Slider(100,50);
        this.pSlider = new Slider(100,50);
        
        this.nSlider.zetLengte(this.getWidth()/3);
        this.pSlider.zetLengte(this.getWidth()/3);
        this.nSlider.setLocation(0, 0);
        this.pSlider.setLocation(this.getWidth()/3 * 2, 0);
        
        this.setSlider(this.nSlider, (double)(this.n-BVInteractiePanel.N_MIN)/(double)(BVInteractiePanel.N_MAX - BVInteractiePanel.N_MIN));
        this.setSlider(this.pSlider, this.p);
        
        this.nSlider.addActionListener(this);
        this.pSlider.addActionListener(this);
                
        //maak het paneel wat in het NORTH gebied van de BorderLayout komt
        this.noordBalk = new JPanel();
        this.noordBalk.setLayout(null);
        this.noordBalk.setPreferredSize(new Dimension(this.getWidth(), this.NOORDBALKHEIGHT));
        this.noordBalk.setBackground(Color.WHITE);
        
        this.noordLinks = new JPanel();
        this.noordLinks.setBackground(this.LABEL_BACKGROUND);
        this.noordLinks.setLayout(null);
        this.noordLinks.setSize(this.getWidth()/3, this.NOORDBALKHEIGHT);
        this.noordLinks.setLocation(0, 0);
        this.noordLinks.add(this.nSlider);
        this.noordLinks.add(this.nLabel);
        this.noordLinks.add(this.nText);
        
        this.noordRechts = new JPanel();
        this.noordRechts.setBackground(this.LABEL_BACKGROUND);
        this.noordRechts.setLayout(null);
        this.noordRechts.setSize(this.getWidth()/3, this.NOORDBALKHEIGHT);
        this.noordRechts.setLocation(this.getWidth()/3, 0);
        this.noordRechts.add(this.pSlider);
        this.noordRechts.add(this.pLabel);
        this.noordRechts.add(this.pText);
        
        this.noordBalk.add(this.noordLinks);
        this.noordBalk.add(this.noordRechts);
                
        this.plaatsComponentenNoordBalk(this.getWidth());
        super.add(this.noordBalk, BorderLayout.NORTH);               
	}
	
	/**
	 * Update de kansbalk, zet JLabels neer als tweeGrenzen waar is, anders de JRadioButtons, en zet de teksten goed
	 */
	public void updateKansBalk() {
		if(this.tweeGrenzen) {	
			this.kansBalk.remove(this.kansRadioLinks);
			this.kansBalk.remove(this.kansRadioMidden);
			this.kansBalk.remove(this.kansRadioRechts);
			
			this.kansLabelLinks.setText(this.kansLabelLinksTekst());
			this.kansLabelMidden.setText(this.kansLabelMiddenTekst());
			this.kansLabelRechts.setText(this.kansLabelRechtsTekst());
			
			
			this.kansBalk.add(this.kansLabelLinks);
			this.kansBalk.add(this.kansLabelMidden);
			this.kansBalk.add(this.kansLabelRechts);
			
			this.kansLabelLinks.setBackground(this.LABEL_BACKGROUND);
			this.kansLabelMidden.setBackground(this.LABEL_ACTIEF);
			this.kansLabelRechts.setBackground(this.LABEL_BACKGROUND);
			
			this.kansBalk.validate();
		}
		else {
			this.kansBalk.remove(this.kansLabelLinks);
			this.kansBalk.remove(this.kansLabelMidden);
			this.kansBalk.remove(this.kansLabelRechts);
			
			this.kansRadioLinks.setText(this.kansLabelLinksTekst());			
			this.kansRadioMidden.setText(this.kansLabelMiddenTekst());
			this.kansRadioRechts.setText(this.kansLabelRechtsTekst());
						
			this.kansBalk.add(this.kansRadioLinks);
			this.kansBalk.add(this.kansRadioMidden);
			this.kansBalk.add(this.kansRadioRechts);
			
			this.kansBalk.validate();
		}
	}
	
	private String kansLabelLinksTekst() {
		if(this.tweeGrenzen) {
			int grens = this.staafjesPanel.getGrensLinks();
			double kans = this.berekenKansCumulatief(0, grens-1);
			int hulp = (int) Math.round(10000*kans);
			kans = (double)hulp/10000;
			
			return new String("P(X<" + grens + ") = " + kans);
		}
		else {
			int grens = this.staafjesPanel.getGrensRechts();
			double kans = this.berekenKansCumulatief(0, grens);
			int hulp = (int)Math.round(10000*kans);
			kans = (double)hulp/10000;
			
			return new String("P(X\u2264" + grens + ") = " + kans);
		}
	}
	
	private String kansLabelMiddenTekst() {
		if(this.tweeGrenzen) {
			double kans = this.berekenKansCumulatief(this.staafjesPanel.getGrensLinks(), this.staafjesPanel.getGrensRechts());
			kans = (double)Math.round(kans*10000)/10000.0;
			return "P(" + this.staafjesPanel.getGrensLinks() + "\u2264X\u2264" + this.staafjesPanel.getGrensRechts() + ") = " + kans;
		}
		else {
			double kans = this.berekenKansK(this.staafjesPanel.getGrensRechts());
			kans = (double)Math.round(kans*10000)/10000.0;
			return "P(X=" + this.staafjesPanel.getGrensRechts() + ") = " + kans;
		}
	}
	
	private String kansLabelRechtsTekst() {
		if(this.tweeGrenzen) {
			double kans = this.berekenKansCumulatief(this.staafjesPanel.getGrensRechts()+1, this.n);
			kans = (double)Math.round(kans*10000)/10000.0;
			return "P(X>" + this.staafjesPanel.getGrensRechts() + ") = " + kans;
		}
		else {
			double kans = this.berekenKansCumulatief(this.staafjesPanel.getGrensRechts(), this.n);
			kans = (double)Math.round(kans*10000)/10000.0;
			return "P(X\u2265" + this.staafjesPanel.getGrensRechts() + ") = " + kans;
		}
		
	}
	
	private void plaatsComponentenNoordBalk(int breedte) {
		this.noordLinks.setBounds(0,0,breedte/3,this.NOORDBALKHEIGHT);
		this.noordRechts.setBounds(2*breedte/3,0,breedte/3,this.NOORDBALKHEIGHT);
		
		//noordBalkLinks:
		this.nSlider.setLocation(0, 28);
		this.nLabel.setBounds((int)(breedte/6.0)-this.nLabel.getPreferredSize().width -10, 10, this.nLabel.getPreferredSize().width, 15);
		this.nText.setBounds((int)(breedte/6.0) -10, 7, this.nText.getPreferredSize().width, 20);
		
		//noordBalkRechts
		this.pSlider.setLocation(0, 28);
		this.pLabel.setBounds((int)(breedte/6.0)-this.pLabel.getPreferredSize().width -10, 10, this.pLabel.getPreferredSize().width, 15);
		this.pText.setBounds((int)(breedte/6.0) -10, 7, this.pText.getPreferredSize().width, 20);
				
		System.out.println(this.noordRechts.getComponentCount());
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
		if(!(this.nInvoer.isBreuk() || this.nInvoer.isRandomInput())) {
			this.nInvoer.setInput(Integer.toString(this.n));
		}
		if(!(this.pInvoer.isBreuk() || this.pInvoer.isRandomInput())) {
			this.pInvoer.setInput(Double.toString(this.p));
		}
				
		this.nText.setText(this.nInvoer.getInput());
		this.nText.setEditable(this.nVeranderbaar);
		
		this.pText.setText(this.pInvoer.getInput());
		this.pText.setEditable(this.pVeranderbaar);
		
		
		this.nSlider.setVisible(this.showNSlider);
		this.pSlider.setVisible(this.showPSlider);
		this.nSlider.setEditable(this.nVeranderbaar);
		this.pSlider.setEditable(this.pVeranderbaar);

		this.updateKansBalk();
		
		this.repaint();
	}
	
	/**
	 * Aantal mogelijkheden voor een k-greep uit n
	 * @return C(n,k)
	 */
	public static double binom(int n, int k)
	{
		if (k>n) {
			return 0.0;
		}
		else if(n == k) {
			return 1.0;
		}
		else {
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
	}
	
	/**
	 * komt neer op BinomPDF
	 * @param k het aantal te behalen successen
	 * @return P(X=k)
	 */
	public double berekenKansK(int k) {
		if (k > this.n) {
			return 0.0;
		}
		return BVInteractiePanelModel.binom(this.n, k) * (double)Math.pow(this.p,k) * (double)Math.pow(1-this.p, this.n-k);
	}
	
	/**
	 * Komt neer op BinomCDF
	 * @return P(X<=this.successen)
	 */
	public double berekenKansCumulatief(int van, int tot) {
		if(van <= 0 && tot >= this.n) { //als alles meetelt is de som dus 1
			return 1.0;
		}
		else {
			double som = 0.0;
			for (int count = van; count <= Math.min(tot, this.n); count++) {
				som += this.berekenKansK(count);
			}
			return som;
		}
	}
	
	public double berekenHyperKansK(int k) {
		return BVInteractiePanel.binom(this.M, k) * BVInteractiePanel.binom(this.n - this.greep, this.n - k) / BVInteractiePanel.binom(this.n, this.greep);
	}
	
	public double berekenHyperKansCumulatief(int van, int tot) {
		double som = 0.0;
		for(int count = van; count <= tot; count++) {
			som += this.berekenHyperKansK(count);
		}
		return som;
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
		this.staafjesPanel.berekenStaafBreedte();
		this.staafjesPanel.bepaalGrenzenMetSlider();
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
	public void setShowXAs(boolean b) {
		this.staafjesPanel.setShowXAs(b);
	}
	public void setShowYAs(boolean b) {
		this.staafjesPanel.setShowYAs(b);
	}
	public void setGrenzenOptie(GrenzenOptie grenzenOptie) {
		this.grenzenOptie = grenzenOptie;
		this.staafjesPanel.setGrenzenOptie(grenzenOptie);
		
		if (this.grenzenOptie == GrenzenOptie.LINKS) {
			this.kansRadioLinks.setSelected(true);
			this.kansRadioLinks.setBackground(this.LABEL_ACTIEF);
			this.kansRadioMidden.setBackground(this.LABEL_BACKGROUND);
			this.kansRadioRechts.setBackground(this.LABEL_BACKGROUND);
		}
		if (this.grenzenOptie == GrenzenOptie.GELIJK) {
			this.kansRadioMidden.setSelected(true);
			this.kansRadioLinks.setBackground(this.LABEL_BACKGROUND);
			this.kansRadioMidden.setBackground(this.LABEL_ACTIEF);
			this.kansRadioRechts.setBackground(this.LABEL_BACKGROUND);
		}
		if (this.grenzenOptie == GrenzenOptie.RECHTS) {
			this.kansRadioRechts.setSelected(true);
			this.kansRadioLinks.setBackground(this.LABEL_BACKGROUND);
			this.kansRadioMidden.setBackground(this.LABEL_BACKGROUND);
			this.kansRadioRechts.setBackground(this.LABEL_ACTIEF);
		}
	}
	
	public void setShowNSlider(boolean b) {
		this.showNSlider = b;
		this.vernieuw();
	}
	public void setShowPSlider(boolean b) {
		this.showPSlider = b;
		this.vernieuw();
	}
	public void setShowGrensSlider(boolean b) {
		this.staafjesPanel.setShowGrensSlider(b);
	}
	
	public void setTweeGrenzen(boolean tweeGrenzen) {
		this.tweeGrenzen = tweeGrenzen;
		this.grenzenBox.setSelected(tweeGrenzen);
		this.staafjesPanel.setTweeGrenzen(tweeGrenzen);
		this.updateKansBalk();
	}
	
	public void setShowNoordBalk (boolean show) {
		this.showNoordBalk = show;
		
		if(show) {
			this.remove(this.noordBalk);
			this.add(this.noordBalk, BorderLayout.NORTH);
		}
		else {
			this.remove(this.noordBalk);
		}
		this.revalidate();
		this.repaint();
	}

	/**
	 * Verander of de kansenbalk zichtbaar is of niet
	 * @param show true als kansenbalk zichtbaar moet zijn, anders false
	 */
	public void setShowKansBalk(boolean show) {
		this.showKansBalk = show;
		if(show) {
			if(this.showTweeGrenzenKeuze) {
				this.remove(this.grenzenBox);
				this.remove(this.zuidBalk);
				this.zuidBalk.removeAll();
				this.zuidBalk.add(this.kansBalk);
				this.zuidBalk.add(this.grenzenBox);
				this.add(this.zuidBalk, BorderLayout.SOUTH);
			}
			else {
				this.remove(this.kansBalk);
				this.add(this.kansBalk, BorderLayout.SOUTH);
			}
		}
		else {
			if(this.showTweeGrenzenKeuze) {
				this.remove(this.zuidBalk);
				this.remove(this.grenzenBox);
				this.add(this.grenzenBox, BorderLayout.SOUTH);
			}
			else {
				this.remove(this.kansBalk);
			}
		}
		this.revalidate();
		this.repaint();
	}
	
	/**
	 * Verander of de keuze tussen één en twee grenzen zichtbaar is of niet
	 * @param show true als keuze zichtbaar moet zijn, anders false
	 */
	public void setShowTweeGrenzenKeuze(boolean show) {
		this.showTweeGrenzenKeuze = show;
		if(!show) {
			if (this.showKansBalk) {
				this.remove(this.kansBalk);
				this.remove(this.zuidBalk);
				this.add(this.kansBalk, BorderLayout.SOUTH);
			}
			else {
				this.remove(this.grenzenBox);
			}
		}
		else {
			if(this.showKansBalk) {
				this.remove(this.kansBalk);
				this.remove(this.zuidBalk);
				this.zuidBalk.removeAll();
				this.zuidBalk.add(this.kansBalk);
				this.zuidBalk.add(this.grenzenBox);
				this.add(this.zuidBalk, BorderLayout.SOUTH);
			}
			else {
				this.remove(this.grenzenBox);
				this.add(this.grenzenBox, BorderLayout.SOUTH);
			}
		}
		this.revalidate();
		this.repaint();
	}
	
	/**
	 * Verwerk een verandering in het nTextField
	 */
	/*
	private void nTextUpdate() {
		this.nString = this.nText.getText();
		try {
			this.setN(Integer.parseInt(this.nString));
			this.staafjesPanel.bepaalGrenzenMetSlider();
		}
		catch (NumberFormatException e) {
			//wordt nu gedaan in update
			//if (!(nString.length() >= 3 && nString.charAt(0) == '#' && nString.charAt(nString.length()-1) == '#')) {
			//	this.nString = Integer.toString(this.n);
			//}
		}
		this.setSlider(this.nSlider, (double)(this.n-BVInteractiePanel.N_MIN)/(double)(BVInteractiePanel.N_MAX - BVInteractiePanel.N_MIN));
	}
	*/
	
	private void nTextUpdate() {
		BVInvoer invoer = new BVInvoer(this.nText.getText());
		if(invoer.isValidIntInput()) {
			this.nInvoer.setInput(this.nText.getText());
			if(!this.nInvoer.isRandomInput()) {
				try {
					this.setN((int)Math.round(Double.parseDouble(this.nInvoer.getInput())));
				}
				catch (NumberFormatException e){
					System.out.println("NumberFormatException in nTextUpdate! " + e.toString());
				}
			}
			this.staafjesPanel.bepaalGrenzenMetSlider();
		}
		else {
			this.nText.setText(this.nInvoer.getInput());
		}
		this.setSlider(this.nSlider, (double)(this.n-BVInteractiePanel.N_MIN)/(double)(BVInteractiePanel.N_MAX - BVInteractiePanel.N_MIN));
	}
	/**
	 * Verwerk een verandering in het pTextField
	 */
	/*
	private void pTextUpdate() {
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
	*/
	private void pTextUpdate() {
		BVInvoer invoer = new BVInvoer(this.pText.getText());
		if(invoer.isValidDoubleInput()) {
			this.pInvoer.setInput(this.pText.getText());
			if(!this.pInvoer.isRandomInput()) {
				if(!this.pInvoer.isBreuk()) {
					try {
						this.p = Double.parseDouble(this.pInvoer.getInput());
					}
					catch (NumberFormatException e){
						System.out.println("NumberFormatException in pTextUpdate! " + e.toString());
					}
				}
				else {
					try {
						this.p = Double.parseDouble(this.pInvoer.getTellerString()) / Double.parseDouble(this.pInvoer.getNoemerString());
					}
					catch (NumberFormatException e) {
						System.out.println("NumberFormatException in pTextUpdate! " + e.toString());
					}
				}
			}
		}
		else {
			this.pText.setText(this.pInvoer.getInput());
		}
		this.setSlider(this.pSlider, this.p);
	}
	
	public void focusGained(FocusEvent e) {
		//niet nodig, implementatie voor interface
	}
	
	public void focusLost(FocusEvent e) {
		if(e.getSource() == this.nText) {
			this.nTextUpdate();
			this.vernieuw();
		}
		if(e.getSource() == this.pText) {
			this.pTextUpdate();
			this.vernieuw();
		}
	}
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getActionCommand().equals("ntextupdate")) {
			this.nTextUpdate();
		}
		if (arg0.getActionCommand().equals("ptextupdate")) {
			this.pTextUpdate();
		}
		if (arg0.getSource() == this.nSlider) {
			this.setN((int)(this.getPercentageFromSlider(this.nSlider)*(BVInteractiePanel.N_MAX - BVInteractiePanel.N_MIN) + BVInteractiePanel.N_MIN));
			this.staafjesPanel.bepaalGrenzenMetSlider();
			if(!this.nInvoer.isRandomInput()) {
				this.nInvoer.setInput(Integer.toString(this.n));
			}
			if(arg0.getActionCommand().equals("stop")) {
				this.staafjesPanel.updateSuccessenSliderPosition();
			}
		}		
		if (arg0.getSource() == this.pSlider) {
			this.p = this.getPercentageFromSlider(this.pSlider);
			if(!this.pInvoer.isRandomInput()) {
				this.pInvoer.setInput(Double.toString(this.p));
			}
		}
		if (arg0.getSource() == this.grenzenBox) {
			this.tweeGrenzen = this.grenzenBox.isSelected();
			this.staafjesPanel.setTweeGrenzen(this.tweeGrenzen);
		}
		if (arg0.getSource() == this.kansRadioLinks) {
			this.setGrenzenOptie(GrenzenOptie.LINKS);
		}
		if (arg0.getSource() == this.kansRadioMidden) {
			this.setGrenzenOptie(GrenzenOptie.GELIJK);
		}
		if (arg0.getSource() == this.kansRadioRechts) {
			this.setGrenzenOptie(GrenzenOptie.RECHTS);
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
		/*
		Hashtable h = new Hashtable();
		h.put("n", new Integer(this.n));
		h.put("p", new Double(this.p));
		h.put("successen", new Integer(this.successen));
		
		h.put("nString", new String(this.nString));
		h.put("pString", new String(this.pString));
		h.put("successenString", new String(this.successenString));
		
		return h;
		*/
		return this.getState();
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
		h.put("showXAs", new Boolean(this.staafjesPanel.getShowXAs()));
		h.put("showYAs", new Boolean(this.staafjesPanel.getShowYAs()));
		
		h.put("nVeranderbaar", new Boolean(this.nVeranderbaar));
		h.put("pVeranderbaar", new Boolean(this.pVeranderbaar));
		
		h.put("nInvoer", this.nInvoer.getInput());
		h.put("pInvoer", this.pInvoer.getInput());
		
		h.put("showNSlider", new Boolean(this.showNSlider));
		h.put("showPSlider", new Boolean(this.showPSlider));
		
		h.put("grensLinks", new Integer(this.staafjesPanel.getGrensLinks()));
		h.put("grensRechts", new Integer(this.staafjesPanel.getGrensRechts()));
		
		if(this.grenzenOptie == GrenzenOptie.LINKS) {
			h.put("grenzenOptie", new Integer(0));
		}
		if(this.grenzenOptie == GrenzenOptie.GELIJK) {
			h.put("grenzenOptie", new Integer(1));
		}
		if(this.grenzenOptie == GrenzenOptie.RECHTS) {
			h.put("grenzenOptie", new Integer(2));
		}
		h.put("tweeGrenzen", new Boolean(this.tweeGrenzen));
		h.put("showGrensSlider", new Boolean(this.staafjesPanel.getShowGrensSlider()));
		h.put("showNoordBalk", new Boolean(this.showNoordBalk));
		h.put("showKansBalk", new Boolean(this.showKansBalk));
		h.put("showTweeGrenzenKeuze", new Boolean(this.showTweeGrenzenKeuze));
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
		if (b.containsKey("nInvoer")) {
			this.nInvoer = new BVInvoer((String)b.get("nInvoer"));
		}
		if (b.containsKey("pInvoer")) {
			this.pInvoer = new BVInvoer((String)b.get("pInvoer"));
		}
		if (b.containsKey("showNSlider")) {
			this.showNSlider = ((Boolean)b.get("showNSlider")).booleanValue();
		}
		if (b.containsKey("showPSlider")) {
			this.showPSlider = ((Boolean)b.get("showPSlider")).booleanValue();
		}
		if (b.containsKey("grensLinks")) {
			this.staafjesPanel.setGrensLinks(((Integer)b.get("grensLinks")).intValue());
		}
		if (b.containsKey("grensRechts")) {
			this.staafjesPanel.setGrensRechts(((Integer)b.get("grensRechts")).intValue());
		}
		
		if (b.containsKey("grenzenOptie")) {
			int optie = ((Integer)b.get("grenzenOptie")).intValue();
			if (optie == 0) {
				this.setGrenzenOptie(GrenzenOptie.LINKS);
			}
			if (optie == 1) {
				this.setGrenzenOptie(GrenzenOptie.GELIJK);
			}
			if (optie == 2) {
				this.setGrenzenOptie(GrenzenOptie.RECHTS);
			}
		}
		if (b.containsKey("tweeGrenzen")) {
			this.tweeGrenzen = ((Boolean)b.get("tweeGrenzen")).booleanValue();
			this.setTweeGrenzen(this.tweeGrenzen);
		}
		if (b.containsKey("showGrensSlider")) {
			this.staafjesPanel.setShowGrensSlider(((Boolean)b.get("showGrensSlider")).booleanValue());
		}
		if(b.containsKey("showKansBalk")) {
			this.setShowKansBalk(((Boolean)b.get("showKansBalk")).booleanValue());
		}
		if(b.containsKey("showNoordBalk")) {
			this.setShowNoordBalk(((Boolean)b.get("showNoordBalk")).booleanValue());
		}
		if(b.containsKey("showTweeGrenzenKeuze")) {
			this.setShowTweeGrenzenKeuze(((Boolean)b.get("showTweeGrenzenKeuze")).booleanValue());
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
		Rectangle r = new Rectangle(x,y,b,h);
		if(!r.equals(this.lastBounds)) {
			
			System.out.println("BVInteractiePanel.setBounds(" + x + "," + y + "," + b + "," + h);
			
			//resize de sliders
			this.nSlider.zetLengte(b/3 - 10);
			//this.nSlider.setLocation(0, 5);
			this.pSlider.zetLengte(b/3 - 10);
			//this.pSlider.setLocation(2*b/3, 5);
			
			//zet de sliders weer op de goede stand
			this.setSlider(this.nSlider, (double)(this.n-BVInteractiePanel.N_MIN)/(double)(BVInteractiePanel.N_MAX - BVInteractiePanel.N_MIN));
			this.setSlider(this.pSlider, this.p);
			
			this.plaatsComponentenNoordBalk(b);
			
			//zet bounds van panel
			super.setBounds(x, y, b, h);
		}
		this.lastBounds = r;
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
		/*
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
		*/
		
		if(this.nInvoer.isRandomInput()) {
			this.n = (int) BVInteractiePanel.substitueerRandom((double)this.n, this.nInvoer.getInput(), randomVars, randomValues);
			this.nInvoer.setInput(Integer.toString(this.n));
		}
		
		if(this.pInvoer.isRandomInput()) {
			if(!this.pInvoer.isBreuk()) {
				this.p = BVInteractiePanel.substitueerRandom(this.p, this.pInvoer.getInput(), randomVars, randomValues);
				this.pInvoer.setInput(Double.toString(this.p));
			}
			else {
				double teller;
				double noemer;
				if(BVInvoer.isRandomVar(this.pInvoer.getTellerString())) {
					teller = BVInteractiePanel.substitueerRandom(this.p, this.pInvoer.getTellerString(), randomVars, randomValues);
				}
				else{
					teller = Double.parseDouble(this.pInvoer.getTellerString());
				}
				
				if(BVInvoer.isRandomVar(this.pInvoer.getNoemerString())) {
					noemer = BVInteractiePanel.substitueerRandom(1.0, this.pInvoer.getNoemerString(), randomVars, randomValues);
				}
				else {
					noemer = Double.parseDouble(this.pInvoer.getNoemerString());
				}
				
				this.p = teller/noemer;
				if(this.p > 1.0) { //kansen groter dan 1.0 zijn onzin
					this.p = 1.0;
				}
				this.pInvoer.setInput(Double.toString(teller) + "/" + Double.toString(noemer));
			}
		}
				
		//update
		this.vernieuw();
	}
}