package fi.binomverdeling;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
public class BVInteractiePanel extends JPanel implements InteractiePanel, ActionListener {
	private double p; //succeskans
	private int n; //aantal herhalingen
	//private int successen;
	//private int grensLinks; //gebruikt als voor tweegrenzen is gekozen
	//private int grensRechts; //geld als grens in geval van 1 grens, geld als linkergrens in geval van twee grenzen
	
	private GrenzenOptie grenzenOptie;
	private boolean tweeGrenzen; //true = 2 grenzen, false = 1 grens
	
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
	private JPanel nSliderPanel;
	private JPanel pSliderPanel;
	
	private Slider nSlider;
	private Slider pSlider;
	private JCheckBox grenzenBox;
	
	public static final int N_MIN = 0; //min en max van n voor de sliders
	public static final int N_MAX = 100;
	
	private boolean nVeranderbaar;
	private boolean pVeranderbaar;
	
	private boolean showNSlider;
	private boolean showPSlider;
	
	private String nString;
	private String pString;
	
	private boolean showNPanel;
	private boolean showPPanel;
	private boolean showTweeGrenzenKeuze;
	private boolean showKansBalk;
		
	private Font font;
	private FontMetrics fontMetrics;
	
	public final Color STAAFJE_TELT = new Color(110,5,165);
	public final Color STAAFJE_TELT_NIET = new Color(234,229,255);
	public final Color LABEL_BACKGROUND = new Color(240,247,255);	
	public final Color LABEL_ACTIEF = new Color(200,227,255);
	
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
		
		
		this.grenzenOptie = GrenzenOptie.LINKS;
		
		this.nVeranderbaar = true;
		this.pVeranderbaar = true;
		
		this.showNSlider = true;
		this.showPSlider = true;
		
		this.nString = Integer.toString(this.n);
		this.pString = Double.toString(this.p);
		
		this.showNPanel = true;
		this.showPPanel = true;
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
		this.zuidBalk.add(this.grenzenBox);
		
		super.add(this.zuidBalk, BorderLayout.SOUTH);
		
        this.nText = new JTextField(5);
        this.nText.setActionCommand("ntextupdate");
        this.nText.setText(Integer.toString(this.n));
        
        this.pText = new JTextField(5);
        this.pText.setActionCommand("ptextupdate");
        this.pText.setText(Double.toString(this.p));
        
        this.nLabel = new JLabel("n = ");
        this.nLabel.setFont(this.font);
        this.nLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        this.pLabel = new JLabel("p = ");
        this.pLabel.setFont(this.font);
        this.pLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        JPanel editBalk = new JPanel();
        editBalk.setBackground(this.LABEL_BACKGROUND);
        
        editBalk.setLayout(new GridLayout(1,3));
        JPanel grid1 = new JPanel();
        grid1.setLayout(new GridLayout(1,3));
        grid1.setBackground(this.LABEL_BACKGROUND);
        grid1.add(this.nLabel);
        grid1.add(this.nText);
        JPanel p = new JPanel();
        p.setOpaque(false);
        grid1.add(p);
        
        editBalk.add(grid1);
        JPanel grid2 = new JPanel();
        grid2.setBackground(Color.WHITE);
        editBalk.add(grid2);
        
        JPanel grid3 = new JPanel();
        grid3.setBackground(this.LABEL_BACKGROUND);
        grid3.setLayout(new GridLayout(1,3));
        grid3.add(this.pLabel);
        grid3.add(this.pText);
        p = new JPanel();
        p.setOpaque(false);
        grid3.add(p);
        editBalk.add(grid3);
        
        //maak het paneel wat in het NORTH gebied van de BorderLayout komt
        this.noordBalk = new JPanel();
        this.noordBalk.setLayout(new GridLayout(2,1));
        super.add(this.noordBalk, BorderLayout.NORTH);
        
        this.noordBalk.add(editBalk);
        
        JPanel sliderBalk = new JPanel();
        sliderBalk.setBackground(this.LABEL_BACKGROUND);
        
        sliderBalk.setLayout(new GridLayout(1,3));
        
        this.nText.addActionListener(this);
        this.pText.addActionListener(this);
        
        this.nSliderPanel = new JPanel();
        this.nSliderPanel.setBackground(this.LABEL_BACKGROUND);
        this.pSliderPanel = new JPanel();
        this.pSliderPanel.setBackground(this.LABEL_BACKGROUND);
        
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
        
        this.nSliderPanel.add(this.nSlider);
        this.pSliderPanel.add(this.pSlider);
        
        sliderBalk.add(this.nSliderPanel);
        JPanel leeg3 = new JPanel();
        leeg3.setBackground(Color.WHITE);
        sliderBalk.add(leeg3);
        sliderBalk.add(this.pSliderPanel);
        
        this.noordBalk.add(sliderBalk);
        
        this.add(this.noordBalk, BorderLayout.NORTH);
        
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
			int hulp = (int)(10000*kans);
			kans = (double)hulp/10000;
			
			return new String("P(X<" + grens + ") = " + kans);
		}
		else {
			int grens = this.staafjesPanel.getGrensRechts();
			double kans = this.berekenKansCumulatief(0, grens);
			int hulp = (int)(10000*kans);
			kans = (double)hulp/10000;
			
			return new String("P(X\u2264" + grens + ") = " + kans);
		}
	}
	
	private String kansLabelMiddenTekst() {
		if(this.tweeGrenzen) {
			double kans = this.berekenKansCumulatief(this.staafjesPanel.getGrensLinks(), this.staafjesPanel.getGrensRechts());
			kans = (double)(int)(kans*10000)/10000.0;
			return "P(" + this.staafjesPanel.getGrensLinks() + "\u2264X\u2264" + this.staafjesPanel.getGrensRechts() + ") = " + kans;
		}
		else {
			double kans = this.berekenKansK(this.staafjesPanel.getGrensRechts());
			kans = (double)(int)(kans*10000)/10000.0;
			return "P(X=" + this.staafjesPanel.getGrensRechts() + ") = " + kans;
		}
	}
	
	private String kansLabelRechtsTekst() {
		if(this.tweeGrenzen) {
			double kans = this.berekenKansCumulatief(this.staafjesPanel.getGrensRechts()+1, this.n);
			kans = (double)(int)(kans*10000)/10000.0;
			return "P(X>" + this.staafjesPanel.getGrensRechts() + ") = " + kans;
		}
		else {
			double kans = this.berekenKansCumulatief(this.staafjesPanel.getGrensRechts(), this.n);
			kans = (double)(int)(kans*10000)/10000.0;
			return "P(X\u2265" + this.staafjesPanel.getGrensRechts() + ") = " + kans;
		}
		
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
	
	/*
	public void updateLabels() {
		this.kansRadioLinks.setText(this.kansLabelLinksTekst());
		this.kansRadioMidden.setText(this.kansLabelMiddenTekst());
		this.kansRadioRechts.setText(this.kansLabelRechtsTekst());
	}
	*/
	
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
				
		this.nText.setText(this.nString);
		this.nText.setEditable(this.nVeranderbaar);
		
		this.pText.setText(this.pString);
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
			double som = 0;
			for (int count = van; count <= Math.min(tot, this.n); count++) {
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
	
	public void setShowNPanel(boolean show) {
		if (!show) {
			if(this.showPPanel) {
				this.nSlider.setVisible(false);
				this.nSlider.setBackground(Color.WHITE);
			}
			else {
				this.remove(this.noordBalk);
			}
		}
		
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
	 * Verwerkt de user interaction
	 */
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getActionCommand().equals("ntextupdate")) {
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
		if (arg0.getSource() == this.nSlider) {
			this.setN((int)(this.getPercentageFromSlider(this.nSlider)*(BVInteractiePanel.N_MAX - BVInteractiePanel.N_MIN) + BVInteractiePanel.N_MIN));
			this.staafjesPanel.bepaalGrenzenMetSlider();
		}
		
		if (arg0.getSource() == this.pSlider) {
			this.p = this.getPercentageFromSlider(this.pSlider);
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
		
		h.put("nString", this.nString);
		h.put("pString", this.pString);
		
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
		if (b.containsKey("nString")) {
			this.nString = (String)b.get("nString");
		}
		if (b.containsKey("pString")) {
			this.pString = (String)b.get("pString");
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
		
		//zet de ..String variabelen goed
		this.nString = Integer.toString(this.n);
		this.pString = Double.toString(this.p);
		
		//update
		this.vernieuw();
	}
}