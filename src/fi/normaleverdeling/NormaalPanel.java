package fi.normaleverdeling;

import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

import javax.swing.*;

import fi.beans.wiskopdrbeans.WiskOpdrApplet;
import fi.beans.stringutils.StringUtils;
// deze moet vanwege interface WiskOpdrApplet
import fi.beans.wiskopdrbeans.InteractiePanel;
// deze moet vanwege interface InteractiePanel
import fi.beans.wiskopdrbeans.InteractieEditPanel;

public class NormaalPanel extends JPanel implements 
													InteractiePanel,
													InteractieEditPanel,
													ActionListener
{	
	protected static int editBreedte = 290;	

	// fonts
	Font theFont;
	FontMetrics theFM;
	
	// layout
	int offSet = 10;
	int cHeight, cHeight1;

	// de kanskeuzes
	ButtonGroup kansGroup;
	JRadioButton linksButton;
	JRadioButton rechtsButton;
	JRadioButton tweeGrenzenButton;
	
	JLabel berekenLabel;
	
	// de berekenkeuzes
	ButtonGroup berekenGroup;
	JRadioButton muButton;
	JRadioButton sigmaButton;
	JRadioButton grensButton;
	JRadioButton grensLinksButton;
	JRadioButton grensRechtsButton;
	JRadioButton kansButton;

	// de parameters
	JLabel muLabel;
	Slider muSlider;
	JTextField muTextField;
	JLabel muWaardeLabel;

	JLabel sigmaLabel;
	Slider sigmaSlider;
	JTextField sigmaTextField;
	JLabel sigmaWaardeLabel;

	JLabel grensLabel;
	Slider grensSlider;
	JTextField grensTextField;
	JLabel grensWaardeLabel;

	JLabel grensLinksLabel;
	JTextField grensLinksTextField;
	JLabel grensLinksWaardeLabel;

	JLabel grensRechtsLabel;
	JTextField grensRechtsTextField;
	JLabel grensRechtsWaardeLabel;
	
	DoubleSlider tweeGrenzenSlider;

	JLabel kansLabel;
	Slider kansSlider;	
	JTextField kansTextField;
	JLabel kansWaardeLabel;

	JLabel muMetWaardeLabel;

	// kansKeuzes	
	static final int KANSLINKS = 0;
	static final int KANSRECHTS = 1;
	static final int TWEEGRENZEN = 2;
	int kansKeuze = KANSLINKS;
	//int kansKeuze = KANSRECHTS;
	//int kansKeuze = TWEEGRENZEN;
	
	// berekenKeuzes
	static final int BEREKENMU = 0;
	static final int BEREKENSIGMA = 1;
	static final int BEREKENGRENS = 2;
	static final int BEREKENKANS = 3;
	static final int BEREKENGRENSLINKS = 4;
	static final int BEREKENGRENSRECHTS = 5;
	int berekenKeuze = BEREKENKANS;
	
	int oldBerekenKeuze;

	// grafiek-grenzen in pixels
	// zet in plaatsComponenten()
	int xMin, xMax, yMin, yMax;

	// grafiek-grenzen als reeele waarden
	// voor de standaardnormale verdeling
	// worden nooit veranderd
	double minX = -45e-1d;
	double maxX = 45e-1d;
	double minY = 0;
	double maxY = 8e-1d;
	
	// grafiekgrenzen voor niet-standaardnormaal
	// wordt bij tekenen gezet op minX+mu en maxX+mu
	double minMuX = minX;
	double maxMuX = maxX;
	
	// parameters
	double mu = 0;
	int muDecimals = 2;
	String muString = "";
	double muMin = -10000;
	double muMax = 10000;
	double muSliderMin = mu - 1;
	double muSliderMax = mu + 1;
	
	double sigma = 1;
	int sigmaDecimals = 1;
	double sigmaMin = 1e-2d;
	double sigmaMax = 500;
	double sigmaSliderMin = sigmaMin;
	double sigmaSliderMax = sigma + 5e-1d;
	
	double grens = mu + 1;//sigma;
	double grensLinks = mu - 1;//sigma;
	double grensRechts = mu + 1;//sigma;
	int grensDecimals = 2;
	
	double kans = 841e-3d;
	int kansDecimals = 3;

	static final double NZERO = 1e-5d;

	static Color lightBlue = new Color(20, 194, 214);
	static Color veryLightBlue = new Color(198, 239, 247);
	static Color veryLightPurperBlue = new Color(234, 229, 255);
	static Color pinkRed = new Color(203, 14, 113);	
    static Color lightRed = new Color(255, 99, 66);	
    
    Color areaColor = veryLightPurperBlue;
    Color kansColor = pinkRed;
    Color muLineColor = Color.lightGray;
    Color sigmaLineColor = Color.gray;    
    
    Graphics ng;
    Graphics og;
    Image im;
    Dimension size;

	boolean lowerGrensLabels = false;
	boolean lowerGrensLinksLabels = false;
	boolean lowerGrensRechtsLabels = false;

	// edit state variabelen
	
	// kans opties
	boolean kansLinksOptie = true;
	boolean kansRechtsOptie = true;
	boolean tweeGrenzenOptie = true;
	
	// bereken opties
	boolean muBerekenbaarOptie = false;
	boolean actualMuBerekenbaarOptie = false;
	
	boolean sigmaBerekenbaarOptie = false;
	boolean actualSigmaBerekenbaarOptie = false;
	
	// vaste waarde opties
	boolean muVastOptie = false;
	boolean sigmaVastOptie = false;
	
	// slider opties
	boolean muSliderOptie = false;
	boolean sigmaSliderOptie = false;
	boolean grensSliderOptie = true;
	boolean kansSliderOptie = false;
	
	boolean muZichtbaarOptie = true;
	boolean sigmaZichtbaarOptie = true;
	boolean grensZichtbaarOptie = true;
	boolean kansZichtbaarOptie = true;
	
	boolean muZichtbaarFigOptie = true;
	boolean sigmaZichtbaarFigOptie = true;
	boolean grensZichtbaarFigOptie = true;
	boolean kansZichtbaarFigOptie = true;

	boolean berekenbaarZichtbaar = true;
	
	RoundedPanel bgPanel1, bgPanel2, bgPanel3, bgPanel4, bgPanel5, bgPanel6;
	
	static DecimalFormatSymbols dfs;
	public static DecimalFormat df,df1,df2,df3;

	//nakijken:
	boolean kijkOpdrachtNa = false;

	boolean kijkMuNa;
	double antwoordMu;
	boolean kijkSigmaNa;
	double antwoordSigma;
	boolean kijkGrensNa;
	double antwoordGrens;
	boolean kijkGrensLinksNa;
	double antwoordGrensLinks;
	boolean kijkGrensRechtsNa;
	double antwoordGrensRechts;
	boolean kijkKansNa;
	double antwoordKans;
	
	int maxScore;	
	int score;
		
	JButton kijkNaButton;
	JPanel kijkNaPanel;
	JLabel vinkjeLabel;
	JLabel kruisjeLabel;
	
	boolean nagekeken = false;
	int mode;
	boolean correct = false;
	boolean fout = false;
	
	Vector listeners = new Vector();
	
	public NormaalPanel(int w, int h)
	{	
		setBackground(Color.white);
		setLayout(null);
		
		dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		df = new DecimalFormat("0", dfs);
		df1 = new DecimalFormat("0.#", dfs);
		df2 = new DecimalFormat("0.##", dfs);
		df3 = new DecimalFormat("0.###", dfs);
		
		theFont = new Font("Dialog", Font.PLAIN, 12);
		theFM = getFontMetrics(theFont);

		cHeight = 3 * theFM.getHeight() / 2;
		cHeight1 = cHeight + offSet;
		
		int width = 0;

		// kansGroup
				
		rechtsButton = new JRadioButton(NormaleVerdeling.rb.getString("kansRechtsTekst"));
		rechtsButton.setFont(theFont);
		rechtsButton.setOpaque(false);
		width = theFM.stringWidth(rechtsButton.getText()) + 30;
		rechtsButton.setSize(width, cHeight);					 
		
		linksButton = new JRadioButton(NormaleVerdeling.rb.getString("kansLinksTekst"));
		linksButton.setFont(theFont);
		linksButton.setOpaque(false);		
		width = theFM.stringWidth(linksButton.getText()) + 30;
		linksButton.setSize(width, cHeight);					 
		
		tweeGrenzenButton = new JRadioButton(NormaleVerdeling.rb.getString("tweeGrenzenTekst"));
		tweeGrenzenButton.setFont(theFont);
		tweeGrenzenButton.setOpaque(false);		
		width = theFM.stringWidth(tweeGrenzenButton.getText()) + 30;		
		tweeGrenzenButton.setSize(width, cHeight);					 
		
		kansGroup = new ButtonGroup();
		kansGroup.add(rechtsButton);
		kansGroup.add(linksButton);
		kansGroup.add(tweeGrenzenButton);		
		
		add(linksButton);
		linksButton.addActionListener(new KansKeuzeAL());
		add(rechtsButton);
		rechtsButton.addActionListener(new KansKeuzeAL());
		add(tweeGrenzenButton);
		tweeGrenzenButton.addActionListener(new KansKeuzeAL());

		// berekenGroup

		width = theFM.stringWidth(NormaleVerdeling.rb.getString("grensRechtsTekst")) + 30;		
		
		berekenLabel = new JLabel(NormaleVerdeling.rb.getString("berekenTekst"));
		berekenLabel.setFont(theFont);
		berekenLabel.setSize(width, cHeight);
		add(berekenLabel);		
		
		muButton = new JRadioButton(NormaleVerdeling.rb.getString("muTekst"));
		muButton.setFont(theFont);
		muButton.setOpaque(false);
		muButton.setSize(width, cHeight);					 
		
		sigmaButton = new JRadioButton(NormaleVerdeling.rb.getString("sigmaTekst"));
		sigmaButton.setFont(theFont);
		sigmaButton.setOpaque(false);		
		sigmaButton.setSize(width, cHeight);					 
		
		grensButton = new JRadioButton(NormaleVerdeling.rb.getString("grensTekst"));
		grensButton.setFont(theFont);
		grensButton.setOpaque(false);
		grensButton.setSize(width, cHeight);					 

		kansButton = new JRadioButton(NormaleVerdeling.rb.getString("kansTekst"));
		kansButton.setFont(theFont);
		kansButton.setOpaque(false);
		kansButton.setSize(width, cHeight);					 
		
		grensLinksButton = new JRadioButton(NormaleVerdeling.rb.getString("grensLinksTekst"));
		grensLinksButton.setFont(theFont);
		grensLinksButton.setOpaque(false);
		grensLinksButton.setSize(width, cHeight);					 

		grensRechtsButton = new JRadioButton(NormaleVerdeling.rb.getString("grensRechtsTekst"));
		grensRechtsButton.setFont(theFont);
		grensRechtsButton.setOpaque(false);
		grensRechtsButton.setSize(width, cHeight);					 
							   
		berekenGroup = new ButtonGroup();
		berekenGroup.add(muButton);
		berekenGroup.add(sigmaButton);
		berekenGroup.add(grensButton);
		berekenGroup.add(kansButton);
		berekenGroup.add(grensLinksButton);		
		berekenGroup.add(grensRechtsButton);				

		add(muButton);
		muButton.addActionListener(new BerekenKeuzeAL());		
		add(sigmaButton);
		sigmaButton.addActionListener(new BerekenKeuzeAL());		
		add(grensButton);
		grensButton.addActionListener(new BerekenKeuzeAL());		
		add(kansButton);
		kansButton.addActionListener(new BerekenKeuzeAL());		
		add(grensLinksButton);		
		grensLinksButton.addActionListener(new BerekenKeuzeAL());		
		add(grensRechtsButton);
		grensRechtsButton.addActionListener(new BerekenKeuzeAL());		
		
		int height = 3 * theFM.getHeight() / 2;		

		muLabel = new JLabel(NormaleVerdeling.rb.getString("muTekst") + " = ");
		muLabel.setFont(theFont);
		width = theFM.stringWidth(muLabel.getText()) + 3;
		muLabel.setSize(width, cHeight);
		add(muLabel);
		
		muTextField = new JTextField("");
		muTextField.setFont(theFont);
		width = theFM.stringWidth("XXXXX") + 10;
		muTextField.setSize(width, cHeight);
		add(muTextField);	
		// listeners
		muTextField.addKeyListener(new InputKL(muTextField, true));
		muTextField.addActionListener(new TextAL(muTextField));		
		muTextField.addFocusListener(new TextFL(muTextField));		

		muWaardeLabel = new JLabel("");
		muWaardeLabel.setFont(theFont);
		muWaardeLabel.setForeground(Color.blue);
		muWaardeLabel.setSize(width, cHeight);
		add(muWaardeLabel);	
			
		muSlider = new Slider(150, 75);
		muSlider.setVisible(muSliderOptie);
		add(muSlider);	
		muSlider.addActionListener(this);
		
		sigmaLabel = new JLabel(NormaleVerdeling.rb.getString("sigmaTekst") + " = ");
		sigmaLabel.setFont(theFont);
		width = theFM.stringWidth(sigmaLabel.getText()) + 3;
		sigmaLabel.setSize(width, cHeight);
		add(sigmaLabel);

		sigmaTextField = new JTextField("");
		sigmaTextField.setFont(theFont);
		width = theFM.stringWidth("XXXXX") + 10;
		sigmaTextField.setSize(width, cHeight);
		add(sigmaTextField);	
		// listeners
		sigmaTextField.addKeyListener(new InputKL(sigmaTextField, false));
		sigmaTextField.addActionListener(new TextAL(sigmaTextField));		
		sigmaTextField.addFocusListener(new TextFL(sigmaTextField));		

		sigmaWaardeLabel = new JLabel("");
		sigmaWaardeLabel.setFont(theFont);
		sigmaWaardeLabel.setForeground(Color.blue);
		sigmaWaardeLabel.setSize(width, cHeight);
		add(sigmaWaardeLabel);	

		sigmaSlider = new Slider(150, 75);
		add(sigmaSlider);	
		sigmaSlider.addActionListener(this);		
		
		grensLabel = new JLabel(NormaleVerdeling.rb.getString("grensGTekst") + " = ");
		grensLabel.setFont(theFont);
		width = theFM.stringWidth(grensLabel.getText()) + 3;
		grensLabel.setSize(width, cHeight);
		add(grensLabel);
		
		grensTextField = new JTextField("");
		grensTextField.setFont(theFont);
		width = theFM.stringWidth("XXXXXX") + 10;
		grensTextField.setSize(width, cHeight);
		add(grensTextField);	
		// listeners
		grensTextField.addKeyListener(new InputKL(grensTextField, true));
		grensTextField.addActionListener(new TextAL(grensTextField));		
		grensTextField.addFocusListener(new TextFL(grensTextField));		

		grensWaardeLabel = new JLabel("");
		grensWaardeLabel.setFont(theFont);
		grensWaardeLabel.setForeground(Color.blue);
		grensWaardeLabel.setSize(width, cHeight);
		add(grensWaardeLabel);	

		// kies maar wat
		grensSlider = new Slider(100, 50);
		grensSlider.zetShowLine(false);
		add(grensSlider);
		grensSlider.addActionListener(this);

		grensLinksLabel = new JLabel(NormaleVerdeling.rb.getString("grensLinksLTekst") + " = ");
		grensLinksLabel.setFont(theFont);
		width = theFM.stringWidth(grensLinksLabel.getText()) + 3;
		grensLinksLabel.setSize(width, cHeight);
		add(grensLinksLabel);
		
		grensLinksTextField = new JTextField("");
		grensLinksTextField.setFont(theFont);
		width = theFM.stringWidth("XXXXXX") + 10;
		grensLinksTextField.setSize(width, cHeight);
		add(grensLinksTextField);	
		// listeners
		grensLinksTextField.addKeyListener(new InputKL(grensLinksTextField, true));
		grensLinksTextField.addActionListener(new TextAL(grensLinksTextField));		
		grensLinksTextField.addFocusListener(new TextFL(grensLinksTextField));		

		grensLinksWaardeLabel = new JLabel("");
		grensLinksWaardeLabel.setFont(theFont);
		grensLinksWaardeLabel.setForeground(Color.blue);
		grensLinksWaardeLabel.setSize(width, cHeight);
		add(grensLinksWaardeLabel);	


		grensRechtsLabel = new JLabel(NormaleVerdeling.rb.getString("grensRechtsRTekst") + " = ");
		grensRechtsLabel.setFont(theFont);
		width = theFM.stringWidth(grensRechtsLabel.getText()) + 3;
		grensRechtsLabel.setSize(width, cHeight);
		add(grensRechtsLabel);
		
		grensRechtsTextField = new JTextField("");
		grensRechtsTextField.setFont(theFont);
		width = theFM.stringWidth("XXXXXX") + 10;
		grensRechtsTextField.setSize(width, cHeight);
		add(grensRechtsTextField);	
		// listeners
		grensRechtsTextField.addKeyListener(new InputKL(grensRechtsTextField, true));
		grensRechtsTextField.addActionListener(new TextAL(grensRechtsTextField));		
		grensRechtsTextField.addFocusListener(new TextFL(grensRechtsTextField));		


		grensRechtsWaardeLabel = new JLabel("");
		grensRechtsWaardeLabel.setFont(theFont);
		grensRechtsWaardeLabel.setForeground(Color.blue);
		grensRechtsWaardeLabel.setSize(width, cHeight);
		add(grensRechtsWaardeLabel);	

		tweeGrenzenSlider = new DoubleSlider(100, 30, 70);
		tweeGrenzenSlider.zetShowLine(false);
		add(tweeGrenzenSlider);
		tweeGrenzenSlider.addActionListener(this);
		
		kansLabel = new JLabel("");
		kansLabel.setFont(theFont);	
		width = theFM.stringWidth(kansLabel.getText()) + 3;
		kansLabel.setSize(width, cHeight);
		add(kansLabel);	
			
		kansTextField = new JTextField("");
		kansTextField.setFont(theFont);
		width = theFM.stringWidth("XXXXXX") + 10;
		kansTextField.setSize(width, cHeight);
		add(kansTextField);	
		// listeners
		kansTextField.addKeyListener(new InputKL(kansTextField, false));
		kansTextField.addActionListener(new TextAL(kansTextField));		
		kansTextField.addFocusListener(new TextFL(kansTextField));		

		kansWaardeLabel = new JLabel("");
		kansWaardeLabel.setFont(theFont);
		kansWaardeLabel.setForeground(Color.blue);
		kansWaardeLabel.setSize(width, cHeight);
		add(kansWaardeLabel);	

		kansSlider = new Slider(150, 75);
		add(kansSlider);	
		kansSlider.addActionListener(this);
		
		muMetWaardeLabel = new JLabel(NormaleVerdeling.rb.getString("muTekst") + 
								      " = XXXX", SwingConstants.CENTER);
		muMetWaardeLabel.setFont(theFont);
		width = theFM.stringWidth(muMetWaardeLabel.getText()) + 3;		
		muMetWaardeLabel.setSize(width, cHeight);
		
//		if (muZichtbaarFigOptie) 
//			add(muMetWaardeLabel);	

		bgPanel1 = new RoundedPanel(10);
		add(bgPanel1);
		bgPanel2 = new RoundedPanel(10);
		add(bgPanel2);
		bgPanel3 = new RoundedPanel(10);
		add(bgPanel3);
		bgPanel4 = new RoundedPanel(10);
		add(bgPanel4);
		bgPanel5 = new RoundedPanel(10);
		add(bgPanel5);
		bgPanel6 = new RoundedPanel(10);
		add(bgPanel6);
		
		// HIER!!
		setSize(w, h);
		size = new Dimension(w, h);
		
//		plaatsComponenten();
		
		zetMu(mu, false, false);
		zetSigma(sigma, false, false);
		zetGrens(grens, false);		
		zetGrensLinks(grensLinks, false);				
		zetGrensRechts(grensRechts, false);						
		zetKans(kans, false);
		
		zetKansKeuze();
		zetBerekenKeuze();
		
//		plaatsComponenten();			

		bereken();
		
		
		kijkNaButton = new JButton(NormaleVerdeling.rb.getString("kijkNaTekst"));
		kijkNaButton.setFont(theFont);
		kijkNaButton.setBounds(0, 0, 100, 20);
		kijkNaButton.addActionListener(new KijkNaAL());
		
		java.net.URL imageURL = NormaleVerdeling.class.getResource("resources/goedkrul_en_klein.gif");
		if (imageURL != null) {
		    vinkjeLabel = new JLabel(new ImageIcon(imageURL));
		}
		else {
			System.out.println("Error reading goedkrul_en_klein.gif.");
			vinkjeLabel = new JLabel();
		}
		vinkjeLabel.setBounds(100, 0, 20, 20);
		imageURL = NormaleVerdeling.class.getResource("resources/foutkruis_klein.gif");
		if (imageURL != null) {
		    kruisjeLabel = new JLabel(new ImageIcon(imageURL));
		}
		else {
			System.out.println("Error reading foutkruis_klein.gif.");
			kruisjeLabel = new JLabel();
		}
		kruisjeLabel.setBounds(100, 0, 20, 20);
		
		vinkjeLabel.setVisible(false);
		kruisjeLabel.setVisible(false);
		
		kijkNaPanel = new JPanel(null);
		kijkNaPanel.setBackground(Color.WHITE);
		kijkNaPanel.setSize(120, 20);
		kijkNaPanel.add(kijkNaButton);
		kijkNaPanel.add(vinkjeLabel);
		kijkNaPanel.add(kruisjeLabel);
		kijkNaPanel.setVisible(kijkOpdrachtNa);
		
		add(kijkNaPanel);
		
		plaatsComponenten();

	}
	
	public void zetKijkOpdrachtNa(boolean b)
	{
		kijkOpdrachtNa = b;
		kijkNaPanel.setVisible(kijkOpdrachtNa);
		
		plaatsComponenten();
	}
	
	public void plaatsComponenten()
	{
//		im = null;
		
		// kansGroup

		if (kansRechtsOptie)
		{
			rechtsButton.setLocation(getSize().width - rechtsButton.getSize().width,
								     getSize().height - cHeight);
			linksButton.setLocation(getSize().width - 2 * rechtsButton.getSize().width,
							  	getSize().height - cHeight);
		}
		else
		{
			rechtsButton.setLocation(getSize().width - rechtsButton.getSize().width,
								     getSize().height - cHeight);
			linksButton.setLocation(getSize().width - linksButton.getSize().width,
							  	getSize().height - cHeight);
		}

		tweeGrenzenButton.setLocation(offSet,
							      	  getSize().height - cHeight);

		bgPanel5.setBounds(0, tweeGrenzenButton.getY(),
						   getWidth(),
						   getHeight() - tweeGrenzenButton.getY() + 6);
		
		if (!rechtsButton.isVisible() && !tweeGrenzenButton.isVisible()) 
			bgPanel5.setVisible(false);
		
		int yPos = 3;

		// berekenGroup
		
		if (berekenbaarZichtbaar)
		{	berekenLabel.setLocation(getSize().width - berekenLabel.getSize().width, yPos);
			yPos += cHeight;						 	
			
			if (actualMuBerekenbaarOptie)
			{	muButton.setLocation(getSize().width - muButton.getSize().width, yPos);
				yPos += cHeight;						 							     
			}
			if (actualSigmaBerekenbaarOptie)
			{	sigmaButton.setLocation(getSize().width - sigmaButton.getSize().width,  yPos);
				yPos += cHeight;
			}
			if (kansKeuze == TWEEGRENZEN)
			{	grensLinksButton.setLocation(getSize().width - grensLinksButton.getSize().width, yPos);
				yPos += cHeight;			                 
				grensRechtsButton.setLocation(getSize().width - grensRechtsButton.getSize().width, yPos);
				yPos += cHeight;			                 						                  
			}	
			else
			{
				grensButton.setLocation(getSize().width - grensButton.getSize().width, yPos);
				yPos += cHeight;			                 						                  						            
			}	
		    kansButton.setLocation(getSize().width - kansButton.getSize().width, yPos);
		    yPos += cHeight;	
	
		    bgPanel6.setBounds(getSize().width - berekenLabel.getSize().width - 10,
		    				   berekenLabel.getY() - 3,
		    				   berekenLabel.getSize().width + 10,
		    				   yPos - berekenLabel.getY() + 11);
		}
		else
		{
			berekenLabel.setVisible(false);
			grensLinksButton.setVisible(false);
			grensRechtsButton.setVisible(false);
			grensButton.setVisible(false);
			kansButton.setVisible(false);
			bgPanel6.setVisible(false);
		}
		
		if (kijkOpdrachtNa && (kijkNaPanel != null))
		{
			int y = 50;
			if (berekenbaarZichtbaar)
				y = bgPanel6.getLocation().y + bgPanel6.getSize().height + 50;
			kijkNaPanel.setLocation(getSize().width - kijkNaPanel.getSize().width - 20, y);
		}
		
		// parameters
		yPos = 3;
		
		if (muZichtbaarOptie)
		{	muLabel.setLocation(offSet, yPos);
			muTextField.setLocation(muLabel.getLocation().x + muLabel.getSize().width, yPos);
			muWaardeLabel.setLocation(muLabel.getLocation().x + muLabel.getSize().width, yPos);
			yPos += cHeight;			                 									
			if ((berekenKeuze != BEREKENMU) && !muVastOptie &&  muSliderOptie)
			{	muSlider.setLocation(offSet, yPos);// + (cHeight - muSlider.getSize().height) / 2); 
				yPos += muSlider.getSize().height-5;			                 									
			}	
			yPos += 10;
			bgPanel1.setBounds(0,muLabel.getY()-3,180,yPos-muLabel.getY()-4);
		}
		else
		{	muLabel.setVisible(false);
			muTextField.setVisible(false);
			muWaardeLabel.setVisible(false);
			muSlider.setVisible(false);
			bgPanel1.setVisible(false);
		}
		
		if (sigmaZichtbaarOptie)
		{	sigmaLabel.setLocation(offSet, yPos);
			sigmaTextField.setLocation(sigmaLabel.getLocation().x + sigmaLabel.getSize().width,	yPos);
			sigmaWaardeLabel.setLocation(sigmaLabel.getLocation().x + sigmaLabel.getSize().width, yPos);
			yPos += cHeight;			                 									
			if ((berekenKeuze != BEREKENSIGMA) && !sigmaVastOptie && sigmaSliderOptie)
			{	sigmaSlider.setLocation(offSet, yPos);// + (cHeight - sigmaSlider.getSize().height) / 2); 
				yPos += sigmaSlider.getSize().height-5;
			}
			yPos += 10;
			bgPanel2.setBounds(0,sigmaLabel.getY()-3,180,yPos-sigmaLabel.getY()-4);
		}
		else
		{	sigmaLabel.setVisible(false);
			sigmaTextField.setVisible(false);
			sigmaWaardeLabel.setVisible(false);
			sigmaSlider.setVisible(false);
			bgPanel2.setVisible(false);
		}
		
		if (grensZichtbaarOptie)
		{	if (kansKeuze == TWEEGRENZEN)
			{	grensLinksLabel.setLocation(offSet, yPos);
				grensLinksTextField.setLocation(grensLinksLabel.getLocation().x + grensLinksLabel.getSize().width, yPos);
				grensLinksWaardeLabel.setLocation(grensLinksLabel.getLocation().x + grensLinksLabel.getSize().width, yPos);
				yPos += cHeight1;
				grensRechtsLabel.setLocation(offSet, yPos);
				grensRechtsTextField.setLocation(grensRechtsLabel.getLocation().x + grensRechtsLabel.getSize().width, yPos);
				grensRechtsWaardeLabel.setLocation(	grensRechtsLabel.getLocation().x + grensRechtsLabel.getSize().width, yPos);
				yPos += cHeight;			
			}
			else
			{
				grensLabel.setLocation(offSet, yPos);
				grensTextField.setLocation(	grensLabel.getLocation().x + grensLabel.getSize().width, yPos);
				grensWaardeLabel.setLocation(grensLabel.getLocation().x + grensLabel.getSize().width, yPos);
				yPos += cHeight;
			}
			yPos += 10;
			bgPanel3.setBounds(0,grensLabel.getY()-3,180,yPos-grensLabel.getY()-4);
			if (kansKeuze == TWEEGRENZEN)
				bgPanel3.setBounds(0,grensLinksLabel.getY()-3,180,yPos-grensLinksLabel.getY()-4);
		}
		else
		{	grensLabel.setVisible(false);
			grensTextField.setVisible(false);
			grensWaardeLabel.setVisible(false);
			grensLinksLabel.setVisible(false);
			grensLinksTextField.setVisible(false);
			grensLinksWaardeLabel.setVisible(false);
			grensRechtsLabel.setVisible(false);
			grensRechtsTextField.setVisible(false);
			grensRechtsWaardeLabel.setVisible(false);
			bgPanel3.setVisible(false);
		}
		if (kansZichtbaarOptie)
		{	// kansLabel kan meerdere labels hebben
			int width = theFM.stringWidth(kansLabel.getText()) + 3;
			kansLabel.setBounds(offSet, yPos, width, cHeight); 
			kansTextField.setLocation(kansLabel.getLocation().x + kansLabel.getSize().width,yPos);
			kansWaardeLabel.setLocation(kansLabel.getLocation().x + kansLabel.getSize().width, yPos);
			yPos += cHeight;	
			if (berekenKeuze != BEREKENKANS &&  kansSliderOptie)
			{	kansSlider.setLocation(offSet,yPos);// + (cHeight - kansSlider.getSize().height) / 2); 
				yPos += kansSlider.getSize().height-5;
			}	
			yPos += 10;
			bgPanel4.setBounds(0,kansLabel.getY()-3,180,yPos-kansLabel.getY()-4);
		}
		else
		{	kansLabel.setVisible(false);
			kansTextField.setVisible(false);
			kansWaardeLabel.setVisible(false);
			kansSlider.setVisible(false);
			bgPanel4.setVisible(false);
		}
			

		int hSpace = (getSize().width - muMetWaardeLabel.getSize().width) / 2;

		muMetWaardeLabel.setLocation(hSpace, getSize().height - 2*cHeight+4);

		// grafiekParameters

		xMin = offSet;
		xMax = getSize().width - offSet;
		yMin = getSize().height - 5 * cHeight / 2;
		yMax = offSet;	   	

		// een grens
		grensSlider.zetLengte(xMax - xMin);
		grensSlider.setLocation(xMin - 5, yMin - grensSlider.getSize().height / 2 + 1);
		zetGrensSlider();

		// twee grenzen
		tweeGrenzenSlider.zetLengte(xMax - xMin);
		tweeGrenzenSlider.setLocation(xMin - 5, yMin - grensSlider.getSize().height / 2 + 1);
		
		zetGrensLinksSlider();
		zetGrensRechtsSlider();
		
		fastPaint();
	}

	public double round(double d, int decs)
	{	double factor = Math.pow(10, decs);
		return Math.round(d * factor) / factor;
	}	
	
	public void zetMu(double waarde, boolean bereken, boolean resetSlider)
	{	
		//mu = waarde;
		double muWaarde = waarde;

		if (muWaarde > muMax - NZERO)
			muWaarde = muMax;
		if (muWaarde < muMin + NZERO)
			muWaarde = muMin;
				
		double deltaMu = muWaarde - mu;
		
		mu = muWaarde;		
				
		//minMuX = minX + mu;
		//maxMuX = maxX + mu;

		// input via TextField
		if (resetSlider)
		{	
		
			minMuX = minX + mu;
			maxMuX = maxX + mu;

			// zet nieuwe grenzen voor de muSlider
			muSliderMin = minMuX;
			muSliderMax = maxMuX;

/*		
			// zet nieuwe grenzen voor de muSlider
			if (Math.abs(mu) < (2 - NZERO))
			{	muSliderMin = mu - 1;
				muSliderMax = mu + 1;
			}
			else if (Math.abs(mu) < (20 - NZERO))
			{	muSliderMin = mu - 5;
				muSliderMax = mu + 5;
			}
			else
			{	if ((mu + 10) > (muMax - NZERO))
				{	muSliderMax = muMax;
					muSliderMin = muMax - 20;
				}
				else if ((mu - 10) < (muMin + NZERO))
				{	muSliderMin = muMin;
					muSliderMax = muMin + 20;
				}
				else
				{	muSliderMin = mu - 10;
					muSliderMax = mu + 10;	
				}
			}
*/			
			zetMuSlider();
		}
		// input via slider, afrondingsfouten
		//if (!bereken && !resetSlider)
		else
		{		
			//minMuX = minX;// + mu;
			//maxMuX = maxX;// + mu;
		
			if (mu < (muSliderMin + NZERO))
			{	mu = muSliderMin;
			}
			else if (mu > (muSliderMax - NZERO))
			{	mu = muSliderMax;
			}
			zetMuSlider();
		}

		// muDecimals aanpassen
		
		muString = df3.format(mu);
		
		if (Math.abs(mu) < 2) 
		{	muString = df3.format(mu);
			muDecimals = 3;
		}	
		else if (Math.abs(mu) < 20)
		{	muString = df2.format(mu);
			muDecimals = 2;
		}
		else if (Math.abs(mu) < 200)
		{	muString = df1.format(mu);
			muDecimals = 1;
		}	
		else 
		{	muString = df.format(mu);
			muDecimals = 0;	
		}	
		
		
		mu = round(mu, muDecimals);	
		//String muString = UF.format(mu, muDecimals);
		//String muString = df.format(mu);
		
		if (NormaleVerdeling.langArg.equals("nl"))
			muString = muString.replace('.', ',');
		
		muTextField.setText(muString);
		muWaardeLabel.setText(muString);

		muMetWaardeLabel.setText(NormaleVerdeling.rb.getString("muTekst") + 
								   " = " + muString);

		// grenzen blijven op hun plaats, maar raken mogelijk buiten beeld!!

		if (((kansKeuze == KANSLINKS) || (kansKeuze == KANSRECHTS)) &&
			(berekenKeuze != BEREKENGRENS)) 
		{	zetGrens(grens, false);
			//zetGrens(grens, !bereken);
		}
		if (kansKeuze == TWEEGRENZEN)
		{	// nieuwe waarde groter, grenzen schuiven naar links
			if (deltaMu > NZERO)
			{	zetGrensLinks(grensLinks, false);
				zetGrensRechts(grensRechts, true);

			}
			// nieuwe waarde kleiner, grenzen schuiven naar rechts
			else if (deltaMu < - NZERO)
			{	zetGrensRechts(grensRechts, false);
				zetGrensLinks(grensLinks, true);

			}			
			else
			{	zetGrensLinks(grensLinks, false);
				zetGrensRechts(grensRechts, true);
			}
		}

		if (bereken)
			bereken();

		fastPaint();
	
	}

	public void zetMuSlider()
	{	int sliderPos = (int) Math.round(
						    (mu - muSliderMin) / (muSliderMax - muSliderMin) * 
						    (muSlider.getMaximum() - muSlider.getMinimum()));
						    
		muSlider.zetStand(sliderPos);				    
		
		fastPaint();
	}	


	public void zetSigma(double waarde, boolean bereken, boolean resetSlider)
	{	
		sigma = waarde;
				
		if (sigma > sigmaMax - NZERO)
		{	sigma = sigmaMax;
			if (berekenKeuze == BEREKENSIGMA)
			{	berekenKeuze = BEREKENGRENS;
				bereken();
				berekenKeuze = BEREKENSIGMA;
			}	
		}
		if (sigma < sigmaMin + NZERO)
		{	sigma = sigmaMin;
			if (berekenKeuze == BEREKENSIGMA)
			{	berekenKeuze = BEREKENGRENS;
				bereken();
				berekenKeuze = BEREKENSIGMA;
			}	
		}

		if (resetSlider)
		{	
		
			minX = -45e-1d * sigma;
			maxX = 45e-1d * sigma;
			minY = 0;
			maxY = 6e-1d / sigma;
		
			minMuX = minX + mu;
			maxMuX = maxX + mu;		

			// zet nieuwe grenzen voor de muSlider
			muSliderMin = minMuX;
			muSliderMax = maxMuX;

			zetMuSlider();		

			// zet de grenzen voor de sigmaslider
			sigmaSliderMin = sigma - sigma / 2;
			sigmaSliderMax = sigma + sigma / 2;
			if (sigmaSliderMin < (sigmaMin + NZERO))
			{	sigmaSliderMin = sigmaMin;
			}
			if (sigmaSliderMax > (sigmaMax - NZERO))
			{	sigmaSliderMax = sigmaMax;
			}
			
/*		
			if (sigma < (15e-1d - NZERO))
			{	if ((sigma - 5e-1d) < (sigmaMin + NZERO))
				{	sigmaSliderMin = sigmaMin;
					sigmaSliderMax = sigmaMin + 1;
				}
				else
				{	sigmaSliderMin = sigma - 5e-1d;
					sigmaSliderMax = sigma + 5e-1d;
				}
			}
			else if (sigma < (5 - NZERO))
			{	sigmaSliderMin = sigma - 1;
				sigmaSliderMax = sigma + 1;
			}
			else
			{	if ((sigma + 2) > (sigmaMax - NZERO))
				{	sigmaSliderMin = sigmaMax - 4;
					sigmaSliderMax = sigmaMax;
				}
				else
				{	sigmaSliderMin = sigma - 2;
					sigmaSliderMax = sigma + 2;
				}
			}
*/			
			zetSigmaSlider();
			
		}

		else
		{		
			if (sigma < (sigmaSliderMin + NZERO))
			{	sigma = sigmaSliderMin;
			}
			else if (sigma > (sigmaSliderMax - NZERO))
			{	sigma = sigmaSliderMax;
			}
			zetSigmaSlider();
		}
				
		// sigmaDecimals aanpassen
		if (sigma < 10 - NZERO)
			sigmaDecimals = 2;
		else if (sigma < 100 - NZERO)
			sigmaDecimals = 1;	
		else
			sigmaDecimals = 0;		
		

		sigma = round(sigma, sigmaDecimals);	

		String sigmaString = UF.format(sigma, sigmaDecimals);
		
		if (NormaleVerdeling.langArg.equals("nl"))
			sigmaString = sigmaString.replace('.', ',');
			
		sigmaTextField.setText(sigmaString);
		sigmaWaardeLabel.setText(sigmaString);

		// grenzen blijven niet noodzakelijk op hun plaats, 
		// en raken mogelijk buiten beeld!!

		if ((kansKeuze == KANSLINKS) || (kansKeuze == KANSRECHTS))
		{	zetGrens(grens, false);
		}

		if (kansKeuze == TWEEGRENZEN)
		{		
			zetGrensLinks(grensLinks, false);
			zetGrensRechts(grensRechts, true);

		}

		if (bereken)
			bereken();
		//	bereken(resetSlider);

		fastPaint();	
	}

	public void zetSigmaSlider()
	{	int sliderPos = (int) Math.round(
						    (sigma - sigmaSliderMin) / 
						    (sigmaSliderMax - sigmaSliderMin) * 
						    (sigmaSlider.getMaximum() - sigmaSlider.getMinimum()));
						    
		sigmaSlider.zetStand(sliderPos);				    
		
		fastPaint();
	}	
/*
	public void zetGrens(double waarde, boolean bereken)
	{
		zetGrens(waarde, bereken, false);
	}
*/	
//	public void zetGrens(double waarde, boolean bereken, boolean start)
	public void zetGrens(double waarde, boolean bereken)
	{	
		grens = waarde;
/*	
		if (start)
		{	minX = -45e-1d * sigma;
			maxX = 45e-1d * sigma;
			minY = 0;
			maxY = 6e-1d / sigma;
		
			minMuX = minX + mu;
			maxMuX = maxX + mu;
		}
*/		
		double grensMax = maxMuX;
		double grensMin = minMuX;
		
// hier if (berekenKeuze == BEREKENMU)
// etc;		
		if (berekenKeuze == BEREKENMU)
		{	
		
			grensMax = muMax + minX;// + maxX;
			grensMin = muMin + maxX;// + minX;
		}


//System.out.println("mimu = " + minMuX);
//System.out.println("mamu = " + maxMuX);
		
		if (grens > grensMax - NZERO)
		{	grens = grensMax;
		
			if (berekenKeuze == BEREKENGRENS)
			{
				berekenKeuze = BEREKENKANS;
				bereken();
				berekenKeuze = BEREKENGRENS;
			}
		}
		if (grens < grensMin + NZERO)
		{	grens = grensMin;
		
			if (berekenKeuze == BEREKENGRENS)
			{
				berekenKeuze = BEREKENKANS;
				bereken();
				berekenKeuze = BEREKENGRENS;
			}
		
		}

		
grensDecimals = findGrensDecimals();		
		
		
		grens = round(grens, grensDecimals);
					
		String grensString = UF.format(grens, grensDecimals);
		
		if (NormaleVerdeling.langArg.equals("nl"))
			grensString = grensString.replace('.', ',');
	
		grensTextField.setText(grensString);
		grensWaardeLabel.setText(grensString);

		zetGrensSlider();

		if (bereken)
			bereken();

		fastPaint();		
	}
	
	public int findGrensDecimals()
	{	int result = 2;
		
		// aantal eenheden x-as per pixel
		double xUnitsPerPixel = (maxX - minX) / (xMax - xMin);
		
//System.out.println("xup = " + xUnitsPerPixel);		
		
		if (xUnitsPerPixel < 1e-4d + NZERO)
			result = 5;
		else if (xUnitsPerPixel < 1e-3d + NZERO)
			result = 4;
		else if (xUnitsPerPixel < 1e-2d + NZERO)
			result = 3;
		else if (xUnitsPerPixel < 1e-1d + NZERO)
			result = 2;
		else if (xUnitsPerPixel < 1 + NZERO)
			result = 1;
		else 
			result = 0;
		
	
		return result;
	}
	
	
	public void zetGrensSlider()
	{	//minMuX = minX + mu;
		int sliderPos = (int) Math.round(
						    (grens - minMuX) / (maxX - minX) * (xMax - xMin));
		grensSlider.zetStand(sliderPos);				    
		fastPaint();
	}

	public void zetGrensLinks(double waarde, boolean bereken)
	{	
	
		// kontrole op waarden	
		grensLinks = waarde;

//		minMuX = minX + mu;
//		maxMuX = maxX + mu;

		double minDis = 
			((double) tweeGrenzenSlider.pixDis) / (xMax - xMin) * (maxX - minX);

//System.out.println("minDis = " + minDis);
		
		if (grensLinks > grensRechts - minDis - NZERO)
		{	grensLinks = grensRechts - minDis - NZERO;
			
//System.out.println("dicht links gevonden");
							
			if (berekenKeuze == BEREKENGRENSLINKS)
			{	int oldBerekenKeuze = berekenKeuze;
				berekenKeuze = BEREKENKANS;
				bereken();
				berekenKeuze = oldBerekenKeuze;
				zetKansSlider();
			}
		}


		// grenslinks loopt links vast
		// fixeer grens rechts (kans gegeven)		
		if (grensLinks < minMuX + NZERO)
		{	grensLinks = minMuX;

//System.out.println("vast links gevonden");														

			if (grensRechts < (maxMuX - NZERO))
			{
			
			
						if (berekenKeuze == BEREKENGRENSLINKS)
						{	oldBerekenKeuze = berekenKeuze;
							berekenKeuze = BEREKENGRENSRECHTS;
							bereken();
							berekenKeuze = oldBerekenKeuze;
			
						}
						
			}			
		}

grensDecimals = findGrensDecimals();

		grensLinks = round(grensLinks, grensDecimals);
		
		String grensLinksString = UF.format(grensLinks, grensDecimals);
		
		if (NormaleVerdeling.langArg.equals("nl"))
			grensLinksString = grensLinksString.replace('.', ',');
	
		grensLinksTextField.setText(grensLinksString);
		grensLinksWaardeLabel.setText(grensLinksString);

		zetGrensLinksSlider();

		if (bereken)
			bereken();
		
		fastPaint();			
		
	}

	public void zetGrensLinksSlider()
	{	//minMuX = minX + mu;
		int sliderPos = (int) Math.round(
						    (grensLinks - minMuX) / (maxX - minX) * (xMax - xMin));
		tweeGrenzenSlider.zetStandLinks(sliderPos);				    
		
		fastPaint();
	}

	public void zetGrensRechts(double waarde, boolean bereken)
	{	
	
		// kontrole op waarden
		grensRechts = waarde;
		
//		minMuX = minX + mu;
//		maxMuX = maxX + mu;
		
		if (grensRechts > maxMuX - NZERO)
		{	grensRechts = maxMuX;

//System.out.println("vast rechts gevonden");														
if (grensLinks > minMuX + NZERO)
{

			if (berekenKeuze == BEREKENGRENSRECHTS)
			{	oldBerekenKeuze = berekenKeuze;
				berekenKeuze = BEREKENGRENSLINKS;
				bereken();
				berekenKeuze = oldBerekenKeuze;
			}
}			
		}
	
		double minDis = 
			((double) tweeGrenzenSlider.pixDis) / (xMax - xMin) * (maxX - minX);
			
		if (grensRechts < (grensLinks + minDis + NZERO))
		{	grensRechts = grensLinks + minDis + NZERO;
		
//System.out.println("dicht rechts gevonden");																			

			if (berekenKeuze == BEREKENGRENSRECHTS)
			{	
//System.out.println("dicht rechts bij berekengrensrechts");																	
				int oldBerekenKeuze = berekenKeuze;
				berekenKeuze = BEREKENKANS;
				bereken();
				berekenKeuze = oldBerekenKeuze;
				zetKansSlider();
			}

		}
		
grensDecimals = findGrensDecimals();
		
		grensRechts = round(grensRechts, grensDecimals);				
		
		String grensRechtsString = UF.format(grensRechts, grensDecimals);
		
		if (NormaleVerdeling.langArg.equals("nl"))
			grensRechtsString = grensRechtsString.replace('.', ',');
	
		grensRechtsTextField.setText(grensRechtsString);
		grensRechtsWaardeLabel.setText(grensRechtsString);
		
		zetGrensRechtsSlider();
		
		if (bereken)
			bereken();
		
		fastPaint();		
	}

	public void zetGrensRechtsSlider()
	{	//minMuX = minX + mu;
		int sliderPos = (int) Math.round(
						    (grensRechts - minMuX) / (maxX - minX) * (xMax - xMin));
		tweeGrenzenSlider.zetStandRechts(sliderPos);				    
		
		fastPaint();
	}

	public void zetKans(double waarde, boolean bereken)
	{	
	
		double kansWaarde = waarde;
		
		if (kansWaarde > 1 - NZERO)
			kansWaarde = 1;
		if (kansWaarde < NZERO)
			kansWaarde = 0;	

		//minMuX = minX + mu;
		//maxMuX = maxX + mu;

		// kans wordt veranderd, mu wordt berekend
		// en mu komt over maximum heen:
		// fixeer kans
		if ((berekenKeuze == BEREKENMU) &&
			(mu > (muMax - NZERO)))
		{	int oldBerekenKeuze = berekenKeuze;
			berekenKeuze = BEREKENKANS;
			bereken();
			berekenKeuze = oldBerekenKeuze;
			if (kansWaarde < kans)
			{	zetKansSlider();
			}
			else
			{	kans = kansWaarde;
			}		
		}	
		// kans wordt veranderd, mu wordt berekend
		// en mu komt beneden minimum:
		// fixeer kans
		else if ((berekenKeuze == BEREKENMU) &&
			(mu < (muMin + NZERO)))
		{	int oldBerekenKeuze = berekenKeuze;
			berekenKeuze = BEREKENKANS;
			bereken();
			berekenKeuze = oldBerekenKeuze;
			if (kansWaarde > kans)
			{	zetKansSlider();
			}
			else
			{	kans = kansWaarde;
			}		
		}	

		// kans wordt veranderd, grensLinks wordt berekend
		// en grensLinks komt links buiten beeld, i.e.
		// wordt kleiner dan minMuX
		// fixeer kans
		else if ((berekenKeuze == BEREKENGRENSLINKS) &&
			(grensLinks < (minMuX + NZERO)) 
		   )
		{	
//System.out.println("links vast bij kans");
//System.out.println("kansWaarde = " + kansWaarde);
				// herbereken		
		
				int oldBerekenKeuze = berekenKeuze;
				berekenKeuze = BEREKENKANS;
				bereken();
				berekenKeuze = oldBerekenKeuze;

				if (kansWaarde > kans)
				{	zetKansSlider();
				}
				else
				{	kans = kansWaarde;
				}
			//}
		}
		// kans wordt veranderd, grensRechts wordt berekend
		// en grensRechts komt rechts buiten beeld, i.e.
		// wordt groter dan maxMuX
		// fixeer kans
		else if ((berekenKeuze == BEREKENGRENSRECHTS) &&
			(grensRechts > (maxMuX - NZERO)) //&&
		   )
		{	
//System.out.println("rechts vast bij kans");
//System.out.println("kansWaarde = " + kansWaarde);

			// herbereken		
		
			int oldBerekenKeuze = berekenKeuze;
			berekenKeuze = BEREKENKANS;
			bereken();
			berekenKeuze = oldBerekenKeuze;

			if (kansWaarde > kans)
			{	zetKansSlider();
			}
			else
			{	kans = kansWaarde;
			}

		}
		else	
			kans = kansWaarde;

		kans = round(kans, kansDecimals);
			
		String kansString = UF.format(kans, kansDecimals);
		
		if (NormaleVerdeling.langArg.equals("nl"))
			kansString = kansString.replace('.', ',');
	
		kansTextField.setText(kansString);
		kansWaardeLabel.setText(kansString);

		zetKansSlider();	

		if (bereken)
			bereken();

		fastPaint();		
	}

	public void zetKansSlider()
	{	int sliderPos = (int) Math.round(
						    (kans - 0) / (1 - 0) * 
						    (kansSlider.getMaximum() - kansSlider.getMinimum()));
						    
		kansSlider.zetStand(sliderPos);				    
		
		fastPaint();
	}	
	
	public void zetKansKeuze()
	{	if (kansKeuze == KANSLINKS)
		{	linksButton.setSelected(true);
		}
		else if (kansKeuze == KANSRECHTS)
		{	rechtsButton.setSelected(true);
		}
		else // kansKeuze == TWEEGRENZEN
		{	tweeGrenzenButton.setSelected(true);
		}
		zetLijsten();

//		bereken();
		
		repaint();
		
	}
	
	public void resetParameters()
	{	muLabel.setForeground(Color.black);
		muLabel.setOpaque(false);
		muLabel.setVisible(true);
		muTextField.setVisible(true);
		muWaardeLabel.setVisible(false);
		muSlider.setVisible(muSliderOptie);
		muWaardeLabel.setForeground(Color.black);
		muWaardeLabel.setOpaque(false);

		sigmaLabel.setForeground(Color.black);
		sigmaLabel.setOpaque(false);
		sigmaLabel.setVisible(true);
		sigmaTextField.setVisible(true);
		sigmaWaardeLabel.setVisible(false);		
		sigmaSlider.setVisible(sigmaSliderOptie);
		sigmaWaardeLabel.setForeground(Color.black);
		sigmaWaardeLabel.setOpaque(false);

		grensLabel.setForeground(Color.black);
		grensLabel.setOpaque(false);
		//grensLabel.setVisible(true);
		if (kansKeuze != TWEEGRENZEN)
		{	grensLabel.setVisible(true);
			grensTextField.setVisible(true);		
			grensSlider.zetEnabled(true);
			grensSlider.setVisible(grensSliderOptie);
		}
		grensWaardeLabel.setVisible(false);
		grensWaardeLabel.setOpaque(false);

		kansLabel.setForeground(Color.black);
		kansLabel.setOpaque(false);
		kansLabel.setVisible(true);
		kansTextField.setVisible(true);		
		kansWaardeLabel.setVisible(false);
		kansWaardeLabel.setOpaque(false);
		kansSlider.setVisible(true && kansSliderOptie);

		grensLinksLabel.setForeground(Color.black);
		grensLinksLabel.setOpaque(false);
		//grensLinksLabel.setVisible(true);
		if (kansKeuze == TWEEGRENZEN)
		{	grensLinksLabel.setVisible(true);
			grensLinksTextField.setVisible(true);		
		}
		grensLinksWaardeLabel.setVisible(false);
		grensLinksWaardeLabel.setOpaque(false);

		grensRechtsLabel.setForeground(Color.black);
		grensRechtsLabel.setOpaque(false);
		//grensRechtsLabel.setVisible(true);		
		if (kansKeuze == TWEEGRENZEN)
		{	grensRechtsLabel.setVisible(true);		
			grensRechtsTextField.setVisible(true);		
			tweeGrenzenSlider.setVisible(grensSliderOptie);
			grensLabel.setVisible(false);
			grensTextField.setVisible(false);
		}
		grensRechtsWaardeLabel.setVisible(false);
		grensRechtsWaardeLabel.setOpaque(false);

		grensSlider.zetEnabled(true);
		tweeGrenzenSlider.zetLinksEnabled(true);
		tweeGrenzenSlider.zetRechtsEnabled(true);
		
		bgPanel1.setBackground(new Color(240,247,255));
		bgPanel2.setBackground(new Color(240,247,255));
		bgPanel3.setBackground(new Color(240,247,255));
		bgPanel4.setBackground(new Color(240,247,255));
		bgPanel5.setBackground(new Color(240,247,255));
		bgPanel6.setBackground(new Color(240,247,255));
		
		bgPanel1.setVisible(true);
		bgPanel2.setVisible(true);
		bgPanel3.setVisible(true);
		bgPanel4.setVisible(true);
		bgPanel5.setVisible(true);
		bgPanel6.setVisible(true);
		
		muMetWaardeLabel.setVisible(muZichtbaarFigOptie);

	}
	
	public void zetBerekenKeuze()
	{	
		resetParameters();
	
		if (berekenKeuze == BEREKENMU)
		{	muButton.setSelected(true);

			muLabel.setForeground(Color.blue);
			muLabel.setOpaque(true);
			muLabel.setBackground(new Color(200,227,255));
			
			muSlider.setVisible(false);
			muTextField.setVisible(false);
			muWaardeLabel.setVisible(true);
			muWaardeLabel.setForeground(Color.blue);
			muWaardeLabel.setOpaque(true);
			muWaardeLabel.setBackground(new Color(200,227,255));
			bgPanel1.setBackground(new Color(200,227,255));
			
			grensSlider.zetEnabled(false);

			tweeGrenzenSlider.zetLinksEnabled(false);
			tweeGrenzenSlider.zetRechtsEnabled(false);
		
		}
		else if (berekenKeuze == BEREKENSIGMA)
		{	sigmaButton.setSelected(true);

			sigmaLabel.setForeground(Color.blue);
			sigmaLabel.setOpaque(true);
			sigmaLabel.setBackground(new Color(200,227,255));
			sigmaSlider.setVisible(false);
			sigmaTextField.setVisible(false);
			sigmaWaardeLabel.setVisible(true);
			sigmaWaardeLabel.setForeground(Color.blue);
			sigmaWaardeLabel.setOpaque(true);
			sigmaWaardeLabel.setBackground(new Color(200,227,255));
			bgPanel2.setBackground(new Color(200,227,255));
		
		}
		else if (berekenKeuze == BEREKENGRENS)
		{	grensButton.setSelected(true);
			grensLabel.setForeground(Color.blue);
			grensLabel.setOpaque(true);
			grensLabel.setBackground(new Color(200,227,255));
			grensTextField.setVisible(false);
			grensWaardeLabel.setVisible(true);
			grensWaardeLabel.setForeground(Color.blue);
			grensWaardeLabel.setOpaque(true);
			grensWaardeLabel.setBackground(new Color(200,227,255));
			bgPanel3.setBackground(new Color(200,227,255));
			grensSlider.zetEnabled(false);
			
			muTextField.setVisible(!muVastOptie);
			muSlider.setVisible(!muVastOptie && muSliderOptie);
			muWaardeLabel.setVisible(muVastOptie);
			
			sigmaSlider.setVisible(!sigmaVastOptie && sigmaSliderOptie);
			sigmaTextField.setVisible(!sigmaVastOptie);
			sigmaWaardeLabel.setVisible(sigmaVastOptie);
		
		}
		else if (berekenKeuze == BEREKENKANS)
		{	kansButton.setSelected(true);
			kansLabel.setForeground(Color.blue);
			kansLabel.setOpaque(true);
			kansLabel.setBackground(new Color(200,227,255));
			kansTextField.setVisible(false);
			kansWaardeLabel.setVisible(true);
			kansWaardeLabel.setForeground(Color.blue);
			kansWaardeLabel.setOpaque(true);
			kansWaardeLabel.setBackground(new Color(200,227,255));
			bgPanel4.setBackground(new Color(200,227,255));
		
			kansSlider.setVisible(false);	
			
			muTextField.setVisible(!muVastOptie);
			muSlider.setVisible(!muVastOptie && muSliderOptie);
			muWaardeLabel.setVisible(muVastOptie);
			
			sigmaSlider.setVisible(!sigmaVastOptie && sigmaSliderOptie);
			sigmaTextField.setVisible(!sigmaVastOptie);
			sigmaWaardeLabel.setVisible(sigmaVastOptie);
		
		}
		else if (berekenKeuze == BEREKENGRENSLINKS)
		{	grensLinksButton.setSelected(true);
			grensLinksLabel.setForeground(Color.blue);
			grensLinksLabel.setOpaque(true);
			grensLinksLabel.setBackground(new Color(200,227,255));
			grensLinksTextField.setVisible(false);
			grensLinksWaardeLabel.setVisible(true);
			grensLinksWaardeLabel.setForeground(Color.blue);
			grensLinksWaardeLabel.setOpaque(true);
			grensLinksWaardeLabel.setBackground(new Color(200,227,255));
			tweeGrenzenSlider.zetLinksEnabled(false);
			
			muTextField.setVisible(!muVastOptie);
			muSlider.setVisible(!muVastOptie && muSliderOptie);
			muWaardeLabel.setVisible(muVastOptie);
			
			sigmaSlider.setVisible(!sigmaVastOptie && sigmaSliderOptie);
			sigmaTextField.setVisible(!sigmaVastOptie);
			sigmaWaardeLabel.setVisible(sigmaVastOptie);
		
		}
		else if (berekenKeuze == BEREKENGRENSRECHTS)
		{	grensRechtsButton.setSelected(true);
			grensRechtsLabel.setForeground(Color.blue);
			grensRechtsLabel.setOpaque(true);
			grensRechtsLabel.setBackground(new Color(200,227,255));
			grensRechtsTextField.setVisible(false);
			grensRechtsWaardeLabel.setVisible(true);
			grensRechtsWaardeLabel.setForeground(Color.blue);
			grensRechtsWaardeLabel.setOpaque(true);
			grensRechtsWaardeLabel.setBackground(new Color(200,227,255));
			tweeGrenzenSlider.zetRechtsEnabled(false);	
			
			muTextField.setVisible(!muVastOptie);
			muSlider.setVisible(!muVastOptie && muSliderOptie);
			muWaardeLabel.setVisible(muVastOptie);
			
			sigmaSlider.setVisible(!sigmaVastOptie && sigmaSliderOptie);
			sigmaTextField.setVisible(!sigmaVastOptie);
			sigmaWaardeLabel.setVisible(sigmaVastOptie);
			
		}
		
		plaatsComponenten();	
		
	}
	
	public void zetLijsten()
	{	
		if (kansKeuze == TWEEGRENZEN)
		{	// berekenlijst
		
			//oldBerekenMuOptie = actualBerekenMuOptie;
			actualMuBerekenbaarOptie = false;
			muButton.setVisible(false);
			//oldBerekenSigmaOptie = berekenSigmaOptie;
			actualSigmaBerekenbaarOptie = false;
			sigmaButton.setVisible(false);
		
			if ((berekenKeuze == BEREKENMU)	||
				(berekenKeuze == BEREKENSIGMA))
			{	berekenKeuze = BEREKENKANS;
				bereken();
			}	
		
			grensButton.setVisible(false);
			grensLinksButton.setVisible(true);
			grensRechtsButton.setVisible(true);
			
			// parameterlijst
			grensLabel.setVisible(false);	
			grensTextField.setVisible(false);	
			grensWaardeLabel.setVisible(false);	
			grensSlider.setVisible(false);
			
			kansLabel.setText(NormaleVerdeling.rb.getString("kansTekst") + " = ");
						
			plaatsComponenten();			
			
			grensLinksLabel.setVisible(true);
			grensLinksTextField.setVisible(true);
			
			grensRechtsLabel.setVisible(true);
			grensRechtsTextField.setVisible(true);

			tweeGrenzenSlider.setVisible(true);			
			
//			repaint();
			
			//kansLabel.setText(NormaleVerdeling.rb.getString("kansTekst") + " = ");

/*
			if (grens > (mu + 1 - NZERO))
			{	zetGrensRechts(grens, false);
				zetGrensLinks(- grens, true);	
			}
			else
			{	zetGrensRechts(grens + 2, false);
				zetGrensLinks(grens, true);	
			}
*/			
			
			// waarden geven/overdragen
			//if (grensButton.isSelected())
			//	grensLinksButton.setSelected(true);	
			if (berekenKeuze == BEREKENGRENS)
			{	berekenKeuze = BEREKENGRENSLINKS;
				//zetBerekenKeuze();
			}
			
			zetBerekenKeuze();			


			if (grens > (mu + 1 - NZERO))
			{	zetGrensRechts(grens, false);
				zetGrensLinks(- grens, true);	
			}
			else
			{	zetGrensRechts(grens + 2, false);
				zetGrensLinks(grens, true);	
			}
			

			
		} 
		else // KANSLINKS of KANSRECHTS
		{	// berekenlijst
			grensButton.setVisible(true);
			grensLinksButton.setVisible(false);
			grensRechtsButton.setVisible(false);
				
			// parameterlijst
			grensLabel.setVisible(true);	
			grensTextField.setVisible(true);	
			grensSlider.setVisible(true);

			boolean wasTweeGrenzen = grensLinksLabel.isVisible();
			
			grensLinksLabel.setVisible(false);
			grensLinksTextField.setVisible(false);
			grensLinksWaardeLabel.setVisible(false);

			grensRechtsLabel.setVisible(false);
			grensRechtsTextField.setVisible(false);
			grensRechtsWaardeLabel.setVisible(false);	

			tweeGrenzenSlider.setVisible(false);					
			
			repaint();
			
			if (kansKeuze == KANSLINKS)
			{	kansLabel.setText(NormaleVerdeling.rb.getString("kansLinksTekst") + " = ");
				if (wasTweeGrenzen)
					zetGrens(grensRechts, true);
			}
			else // kansKeuze == KANSRECHTS
			{	kansLabel.setText(NormaleVerdeling.rb.getString("kansRechtsTekst") + " = ");
				if (wasTweeGrenzen)
					zetGrens(grensLinks, true);
			}
			
			// waarden geven/ overdragen
			if ((berekenKeuze == BEREKENGRENSLINKS) || 
				(berekenKeuze == BEREKENGRENSRECHTS)) 
			{	berekenKeuze = BEREKENGRENS;
			//	zetBerekenKeuze();
			}
			
			actualMuBerekenbaarOptie = muBerekenbaarOptie;
			actualSigmaBerekenbaarOptie = sigmaBerekenbaarOptie;
			muButton.setVisible(muBerekenbaarOptie && berekenbaarZichtbaar);
			sigmaButton.setVisible(sigmaBerekenbaarOptie && berekenbaarZichtbaar);
						
			zetBerekenKeuze();			
			plaatsComponenten();			
				
		}
		
//		plaatsComponenten();
	}


	public void zetKansOpties()
	{	// booleans zijn al gezet

		boolean override = false;
			
		// override kansKeuze als nodig		
		if (!kansLinksOptie && (kansKeuze == KANSLINKS))
		{	if (kansRechtsOptie)
				kansKeuze = KANSRECHTS;
			else
				kansKeuze = TWEEGRENZEN;	
				
			override = true;	
		}
		if (!kansRechtsOptie && (kansKeuze == KANSRECHTS))
		{	if (kansLinksOptie)
				kansKeuze = KANSLINKS;
			else
				kansKeuze = TWEEGRENZEN;
				
			override = true;			
		}
		if (!tweeGrenzenOptie && (kansKeuze == TWEEGRENZEN))
		{	if (kansLinksOptie)
				kansKeuze = KANSLINKS;
			else
				kansKeuze = KANSRECHTS;	
				
			override = true;		
		}
		
		// als maar 1 optie geselecteerd geen box
		if (
			(kansLinksOptie && !kansRechtsOptie && !tweeGrenzenOptie) ||
			(!kansLinksOptie && kansRechtsOptie && !tweeGrenzenOptie) ||
			(!kansLinksOptie && !kansRechtsOptie && tweeGrenzenOptie)
		   )	
		{	linksButton.setVisible(false);
			rechtsButton.setVisible(false);
			tweeGrenzenButton.setVisible(false);
		}		
		else
		{	linksButton.setVisible(kansLinksOptie);
			rechtsButton.setVisible(kansRechtsOptie);
			tweeGrenzenButton.setVisible(tweeGrenzenOptie);
		}
		
		if (override)
			zetKansKeuze();
		
		plaatsComponenten();
		
	}
	
	public void zetberekenbaarZichtbaar(boolean b)
	{	berekenbaarZichtbaar = b;
		berekenLabel.setVisible(b);
		
		muButton.setVisible(b && muBerekenbaarOptie && !muVastOptie);
		sigmaButton.setVisible(b && sigmaBerekenbaarOptie && !sigmaVastOptie);
		
		grensLinksButton.setVisible(b && kansKeuze == TWEEGRENZEN);
		grensRechtsButton.setVisible(b && kansKeuze == TWEEGRENZEN);
		grensButton.setVisible(b && kansKeuze != TWEEGRENZEN);
		kansButton.setVisible(b);
		bgPanel6.setVisible(b);
		
		plaatsComponenten();
	}
	
	public void zetMuBerekenbaarOptie(boolean b)
	{	muBerekenbaarOptie = b;
		if (!b)
			actualMuBerekenbaarOptie = false;
		else		
			actualMuBerekenbaarOptie = (kansKeuze != TWEEGRENZEN);
			
		//if (actualMuBerekenbaarOptie)	
		muButton.setVisible(actualMuBerekenbaarOptie);

		// uitschakelen met berekenKeuze==BEREKENMU
		if (!b && (berekenKeuze == BEREKENMU))
		{	berekenKeuze = BEREKENKANS;
			zetBerekenKeuze();
		}
		
		// inschakelen met muVastOptie==true 
		if (b && muVastOptie)
		{	zetMuVastOptie(!b);
		}
		
		//zetBerekenKeuze();
		plaatsComponenten();
	}

	public void zetSigmaBerekenbaarOptie(boolean b)
	{	sigmaBerekenbaarOptie = b;
		if (!b)
			actualSigmaBerekenbaarOptie = false;
		else
			actualSigmaBerekenbaarOptie = (kansKeuze != TWEEGRENZEN);
			
		//if (actualSigmaBerekenbaarOptie)	
		sigmaButton.setVisible(actualSigmaBerekenbaarOptie);
			
		// uitschakelen met berekenKeuze==BEREKENSIGMA
		if (!b && (berekenKeuze == BEREKENSIGMA))
		{	berekenKeuze = BEREKENKANS;
			zetBerekenKeuze();
			
		}

		// inschakelen met sigamVastOptie==true 
		if (b && sigmaVastOptie)
		{	zetSigmaVastOptie(!b);
		}
		
		//zetBerekenKeuze();
		plaatsComponenten();		
	}
	
	
	public void zetMuVastOptie(boolean b)
	{	muVastOptie = b;
//		muWaardeLabel.setVisible(b);
//		muTextField.setVisible(!b);
//		muSlider.setVisible(!b);

		// inschakelen met muBerekenbaarOptie==true
		if (b && muBerekenbaarOptie)
		{	zetMuBerekenbaarOptie(!b);
		}

		muWaardeLabel.setForeground(Color.black);
		muWaardeLabel.setVisible(b);
		muTextField.setVisible(!b);
		muSlider.setVisible(!b && muSliderOptie);

		
		plaatsComponenten();
	}

	public void zetSigmaVastOptie(boolean b)
	{	sigmaVastOptie = b;
//		sigmaWaardeLabel.setVisible(b);
//		sigmaTextField.setVisible(!b);
//		sigmaSlider.setVisible(!b);
		
		// inschakelen met sigmaBerekenbaarOptie==true		
		if (b && sigmaBerekenbaarOptie)
		{	zetSigmaBerekenbaarOptie(!b);
		}
		
		sigmaWaardeLabel.setForeground(Color.black);		
		sigmaWaardeLabel.setVisible(b);
		sigmaTextField.setVisible(!b);
		sigmaSlider.setVisible(!b && sigmaSliderOptie);

		plaatsComponenten();		
	}

	
	public void zetMuSliderOptie(boolean b)
	{	muSliderOptie = b;
		muSlider.setVisible(b && !muVastOptie && (berekenKeuze != BEREKENMU));
		plaatsComponenten();
	}
	
	public void zetSigmaSliderOptie(boolean b)
	{	sigmaSliderOptie = b;
		sigmaSlider.setVisible(b && !sigmaVastOptie && (berekenKeuze != BEREKENSIGMA));
		plaatsComponenten();
	}
	
	public void zetGrensSliderOptie(boolean b)
	{	grensSliderOptie = b;
	
		grensSlider.setVisible(b && (berekenKeuze != BEREKENGRENS) &&
			(kansKeuze != TWEEGRENZEN));
		
		tweeGrenzenSlider.setVisible(b && (berekenKeuze != BEREKENGRENSLINKS) &&
			(berekenKeuze != BEREKENGRENSRECHTS) && (kansKeuze == TWEEGRENZEN));
		plaatsComponenten();
	}
	
	public void zetKansSliderOptie(boolean b)
	{	kansSliderOptie = b;
		kansSlider.setVisible(b && (berekenKeuze != BEREKENKANS));
		plaatsComponenten();
	}
	
	public void zetMuZichtbaarOptie(boolean b)
	{	muZichtbaarOptie = b;
		//resetParameters();
		zetBerekenKeuze();
		plaatsComponenten();
	}
	
	public void zetSigmaZichtbaarOptie(boolean b)
	{	sigmaZichtbaarOptie = b;
		//resetParameters();
		zetBerekenKeuze();
		plaatsComponenten();
	}
	
	public void zetGrensZichtbaarOptie(boolean b)
	{	grensZichtbaarOptie = b;
		//resetParameters();
		zetBerekenKeuze();
		plaatsComponenten();
	}
	
	public void zetKansZichtbaarOptie(boolean b)
	{	kansZichtbaarOptie = b;
//		resetParameters();
		zetBerekenKeuze();
		plaatsComponenten();
	}
	
	public void zetMuZichtbaarFigOptie(boolean b)
	{	muZichtbaarFigOptie = b;
		muMetWaardeLabel.setVisible(b);
		//plaatsComponenten();
		fastPaint();
	}
	
	public void zetSigmaZichtbaarFigOptie(boolean b)
	{	sigmaZichtbaarFigOptie = b;
		//plaatsComponenten();
		fastPaint();
	}
	
	public void zetGrensZichtbaarFigOptie(boolean b)
	{	grensZichtbaarFigOptie = b;
		//plaatsComponenten();
		fastPaint();
	}
	
	public void zetKansZichtbaarFigOptie(boolean b)
	{	kansZichtbaarFigOptie = b;
		//plaatsComponenten();
		fastPaint();
	}

	public void processMuSlider()
	{	
		int stand = muSlider.geefStand();
		double muWaarde = muSliderMin + 
			((double) stand) / 
				(muSlider.getMaximum() - muSlider.getMinimum()) * 
				(muSliderMax - muSliderMin);
		zetMu(muWaarde, true, false);	
	}

	public void processSigmaSlider()
	{	
	
		int stand = sigmaSlider.geefStand();
		double sigmaWaarde = sigmaSliderMin + 
			((double) stand) / 
				(sigmaSlider.getMaximum() - sigmaSlider.getMinimum()) * 
				(sigmaSliderMax - sigmaSliderMin);
		zetSigma(sigmaWaarde, true, false);	
		
	}

	public void processGrensSlider()
	{	//minMuX = minX + mu;
		int stand = grensSlider.geefStand();
		double grensWaarde = minMuX + 
			((double) stand) / (xMax - xMin) * (maxX - minX);
		zetGrens(grensWaarde, true);	
	}

	public void processTweeGrenzenSlider(boolean links)
	{	//minMuX = minX + mu;
		if (links)
		{	int standLinks = tweeGrenzenSlider.geefStandLinks();
			double grensLinksWaarde = minMuX + 
				((double) standLinks) / (xMax - xMin) * (maxX - minX);
			zetGrensLinks(grensLinksWaarde, true);	
		
		}
		else
		{	int standRechts = tweeGrenzenSlider.geefStandRechts();
			double grensRechtsWaarde = minMuX + 
				((double) standRechts) / (xMax - xMin) * (maxX - minX);
			zetGrensRechts(grensRechtsWaarde, true);	
		}
	}

	public void processKansSlider()
	{	
		int stand = kansSlider.geefStand();
		double kansWaarde = 0 + 
			((double) stand) / 
				(kansSlider.getMaximum() - kansSlider.getMinimum()) * (1 - 0);
		zetKans(kansWaarde, true);	
	}

	
	public void fastPaint()
	{	if (ng == null)
			repaint();
		else
			paint(ng);	
	}
	
	public void paintComponent(Graphics g)
	{	/*if (ng == null)
			ng = getGraphics();

		if ((im == null) || (size.width != getSize().width) ||
			(size.height != getSize().height))
		{	im = createImage(getSize().width, getSize().height);
			og = im.getGraphics();
			size = new Dimension(getSize().width, getSize().height);
		}	
		*/
		
		Graphics2D og = (Graphics2D) g;
		og.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		og.setColor(Color.white);
		og.fillRect(0, 0, getSize().width, getSize().height);

		paintArea(og);
		paintMuLine(og);
		
		paintSigmaLines(og);
		
		paintXAxis(og);
		paintDistribution(og);
		paintLabels(og);	
		//paintComponents(og);
		
		//g.drawImage(im, 0, 0, null);
	}
	
	
	public void paintXAxis(Graphics g)
	{	g.setColor(Color.black);
		g.drawLine(xMin, yMin, xMax, yMin);
	}
	
	public void paintMuLine(Graphics g)
	{	// centreren
//		minMuX = minX + mu;

		// middenin				
		//int xPos = xMin + (xMax - xMin) / 2;

		int xPos = xMin + (int) Math.round(
								(mu - minMuX) / (maxX - minX) * (xMax - xMin));

		//double x = minMuX + ((double) (xPos - xMin)) / (xMax - xMin) * (maxX - minX);

		//double fx = normalDF(x);
		
		double fx = normalDF(mu);		

		int y = yMin - (int) Math.round(
								(fx - minY) / (maxY - minY) * (yMin - yMax));
		g.setColor(muLineColor);
		g.drawLine(xPos, y, xPos, yMin);		
		
		String muWaarde = NormaleVerdeling.rb.getString("muTekst") + 
								                        " = " + muString;
		
		//String muWaarde = UF.format(mu, muDecimals);
		int width = theFM.stringWidth(muWaarde);
		int bx = xPos - width / 2;
		if (bx < 2)
			bx = 2;
			
		if ((bx + width) > (getSize().width - 2))
			bx = getSize().width - width - 2;	
		
		int by = yMin + 2 * theFM.getHeight();
		int by2 = yMin + theFM.getHeight();
		
		boolean lower = lowerGrensLabels || lowerGrensLinksLabels ||
						lowerGrensRechtsLabels;
		
		g.setColor(Color.black);
		if (muZichtbaarFigOptie && lower)
			g.drawString(muWaarde, bx, by2);	
		else if (muZichtbaarFigOptie && !lower)	
			g.drawString(muWaarde, bx, by);	
		
	}

	public void paintSigmaLines(Graphics g)
	{	// left
	
		int xStartLeft = xMin + (int) Math.round(
							(mu - sigma - minMuX) / (maxX - minX) * (xMax - xMin));
		int xEndLeft = xMin + (int) Math.round(
						(mu - minMuX) / (maxX - minX) * (xMax - xMin)) - 1;
						
		int xStartRight = xEndLeft + 2;
		
		int xEndRight = xMin + (int) Math.round(
							(mu + sigma - minMuX) / (maxX - minX) * (xMax - xMin));
						
						
		double fx = normalDF(mu - sigma);				
		
		int y = yMin - (int) Math.round(
								(fx - minY) / (maxY - minY) * (yMin - yMax));

		String sigmaWaarde = NormaleVerdeling.rb.getString("sigmaTekst") + 
							 	" = " + UF.format(sigma, sigmaDecimals);
		
		int width = theFM.stringWidth(sigmaWaarde);
		
		int bxLeft = (xStartLeft + xEndLeft) / 2 - width / 2;
		int bxRight = (xStartRight + xEndRight) / 2 - width / 2;
		int by = y + theFM.getHeight();

		if (sigmaZichtbaarFigOptie)
		{	
			g.setColor(sigmaLineColor);
			g.drawLine(xStartLeft, y, xEndLeft, y);
			g.drawLine(xStartLeft, y, xStartLeft + 5, y - 5);	
			g.drawLine(xStartLeft, y, xStartLeft + 5, y + 5);		
			g.drawLine(xEndLeft, y, xEndLeft - 5, y - 5);	
			g.drawLine(xEndLeft, y, xEndLeft - 5, y + 5);		
			
			g.drawLine(xStartRight, y, xEndRight, y);
			g.drawLine(xStartRight, y, xStartRight + 5, y - 5);	
			g.drawLine(xStartRight, y, xStartRight + 5, y + 5);		
			g.drawLine(xEndRight, y, xEndRight - 5, y - 5);	
			g.drawLine(xEndRight, y, xEndRight - 5, y + 5);					

			g.setColor(Color.black);	
			if ((bxRight + width) < getSize().width)		
				g.drawString(sigmaWaarde, bxRight, by);
			else
				g.drawString(sigmaWaarde, bxLeft, by);
		}

		
	}

	public void paintDistribution(Graphics g)
	{	// centreren
		//minMuX = minX + mu;
	
// stroke zetten
	
		g.setColor(Color.black);
		for (int xCnt = xMin; xCnt < xMax; xCnt++)
		{	// maak van xCnt en xCnt+1 de corresponderende double
			double x1 = minMuX + ((double) (xCnt - xMin)) / (xMax - xMin) * (maxX - minX);
			double x2 = minMuX + ((double) (xCnt + 1 - xMin)) / (xMax - xMin) * (maxX - minX);
			// bereken funktiewaarden
			double fx1 = normalDF(x1);
			double fx2 = normalDF(x2);
			// maak van fx1 en fx2 de correnponderende ints
			int y1 = yMin - (int) Math.round(
									(fx1 - minY) / (maxY - minY) * (yMin - yMax));
			int y2 = yMin - (int) Math.round(
									(fx2 - minY) / (maxY - minY) * (yMin - yMax));
			g.drawLine(xCnt, y1, xCnt + 1, y2);
		}
	
	}

	public void paintArea(Graphics g)
	{	// centreren
//		minMuX = minX + mu;
//		maxMuX = maxX + mu;
		
		int xStart = 0;
		int xStop = 0;
		
		if (kansKeuze == KANSLINKS)
		{	xStart = xMin;
			xStop = xMin + (int) Math.round(
								  (grens - minMuX) / (maxX - minX) * (xMax - xMin));
		}
		else if (kansKeuze == KANSRECHTS)
		{	xStart = xMin + (int) Math.round(
								  (grens - minMuX) / (maxX - minX) * (xMax - xMin));
			xStop = xMax;					  
		}
		else // kansKeuze == TWEEGRENZEN
		{	xStart = xMin + (int) Math.round(
								  (grensLinks - minMuX) / (maxX - minX) * (xMax - xMin));
			xStop = xMin + (int) Math.round(
								  (grensRechts - minMuX) / (maxX - minX) * (xMax - xMin));					  
		}

		//g.setColor(lightBlue);
		for (int xCnt = xStart; xCnt <= xStop; xCnt++)
		{	// maak van xCnt een double
			double x = minMuX + ((double) (xCnt - xMin)) / (xMax - xMin) * (maxX - minX);
			// bereken funktiewaarde
			double fx = normalDF(x);
			// maak van fx de correnponderende int
			int y = yMin - (int) Math.round(
									(fx - minY) / (maxY - minY) * (yMin - yMax));
			
			if ((kansKeuze == KANSLINKS) && (xCnt == xStop))
				g.setColor(Color.black);
			else if ((kansKeuze == KANSRECHTS) && (xCnt == xStart))
				g.setColor(Color.black);		
			else if ((kansKeuze == TWEEGRENZEN) && ((xCnt == xStart) || (xCnt == xStop)))
				g.setColor(Color.black);		
			else
				g.setColor(areaColor);									
			
			g.drawLine(xCnt, y, xCnt, yMin);
		}

		
	}

	public void paintLabels(Graphics g)
	{	// centreren
//		minMuX = minX + mu;
	
		int hOffset = 5;		
	
		if (kansKeuze == KANSLINKS)
		{	String grensWaarde = UF.format(grens, grensDecimals);
			int width = theFM.stringWidth(grensWaarde);
			// in pixels
			int grensPos = xMin + (int) Math.round(
								  	  (grens - minMuX) / (maxX - minX) * (xMax - xMin));
								  	  
			int grensWaardePos = grensPos - width / 2;					  	  
			if (grensWaardePos < 0)
				grensWaardePos = 0;
			if (grensWaardePos + width > getSize().width)
				grensWaardePos -= grensWaardePos + width - getSize().width;	

			int vSpace = 0;
			if (lowerGrensLabels)
				vSpace = 2 * theFM.getHeight();
			else
				vSpace = theFM.getHeight();	
				
			g.setFont(theFont);
			g.setColor(Color.black);
			if (grensZichtbaarFigOptie)
				g.drawString(grensWaarde, grensWaardePos, yMin + vSpace);
			
			String gString = NormaleVerdeling.rb.getString("gTekst");			 
			width = theFM.stringWidth(gString);
			int gPos = grensPos + hOffset;
			if (gPos + width > getSize().width)
				gPos -= gPos + width - getSize().width;	
			if (grensZichtbaarFigOptie)
				g.drawString(gString, gPos, yMin - theFM.getDescent());			 					  	  
			
			String kansWaarde = UF.format(kans, kansDecimals);
			width = theFM.stringWidth(kansWaarde);
			int kansPos = grensPos - width - 2 * hOffset;
			if (kansPos < 0)
				kansPos = 0;
			
			g.setColor(kansColor);
			if (kansZichtbaarFigOptie)
				g.drawString(kansWaarde, kansPos, yMin - theFM.getHeight());
			
			
		}
		else if (kansKeuze == KANSRECHTS)
		{	String grensWaarde = UF.format(grens, grensDecimals);
			int width = theFM.stringWidth(grensWaarde);
			// in pixels
			int grensPos = xMin + (int) Math.round(
								  	  (grens - minMuX) / (maxX - minX) * (xMax - xMin));

			int grensWaardePos = grensPos - width / 2;					  	  
			if (grensWaardePos < 0)
				grensWaardePos = 0;
			if (grensWaardePos + width > getSize().width)
				grensWaardePos -= grensWaardePos + width - getSize().width;	

			int vSpace = 0;
			if (lowerGrensLabels)
				vSpace = 2 * theFM.getHeight();
			else
				vSpace = theFM.getHeight();	

			g.setFont(theFont);
			g.setColor(Color.black);
			if (grensZichtbaarFigOptie)
				g.drawString(grensWaarde, grensWaardePos, yMin + vSpace);
			
			String gString = NormaleVerdeling.rb.getString("gTekst");			 
			width = theFM.stringWidth(gString);
			int gPos = grensPos - hOffset - width;
			if (gPos < 0)
				gPos = 0;
			if (grensZichtbaarFigOptie)
				g.drawString(gString, gPos, yMin - theFM.getDescent());			 					  	  
			
			String kansWaarde = UF.format(kans, kansDecimals);
			width = theFM.stringWidth(kansWaarde);
			
			int kansPos = grensPos + 2 * hOffset;
			if (kansPos + width > getSize().width)
				kansPos -= kansPos + width - getSize().width;
			
			g.setColor(kansColor);
			if (kansZichtbaarFigOptie)
				g.drawString(kansWaarde, kansPos, yMin - theFM.getHeight());
		}
		else // kansKeuze == TWEEGRENZEN
		{	String grensLinksWaarde = UF.format(grensLinks, grensDecimals);
			String grensRechtsWaarde = UF.format(grensRechts, grensDecimals);
			int widthLinks = theFM.stringWidth(grensLinksWaarde);
			int widthRechts = theFM.stringWidth(grensRechtsWaarde);
			// in pixels
			int grensLinksPos = xMin + 
				(int) Math.round((grensLinks - minMuX) / (maxX - minX) * (xMax - xMin));
			int grensRechtsPos = xMin + 
				(int) Math.round((grensRechts - minMuX) / (maxX - minX) * (xMax - xMin));

			int grensLinksWaardePos = grensLinksPos - widthLinks / 2;					  	  
			if (grensLinksWaardePos < 0)
				grensLinksWaardePos = 0;
			if (grensLinksWaardePos + widthLinks > getSize().width)
				grensLinksWaardePos -= grensLinksWaardePos + widthLinks - getSize().width;	
			
			int grensRechtsWaardePos = grensRechtsPos - widthRechts / 2;					  	  
			if (grensRechtsWaardePos < 0)
				grensRechtsWaardePos = 0;
			if (grensRechtsWaardePos + widthRechts > getSize().width)
				grensRechtsWaardePos -= grensRechtsWaardePos + widthRechts - getSize().width;	
			
			if ((grensLinksWaardePos + widthLinks > grensRechtsWaardePos) &&
				!lowerGrensLinksLabels && !lowerGrensRechtsLabels)
			{	
				grensLinksWaardePos = grensLinksPos - widthLinks;	
				grensRechtsWaardePos = grensRechtsPos;
				
				if (grensLinksWaardePos < 0)
				{	
					grensLinksWaardePos = 0;
					grensRechtsWaardePos = grensLinksWaardePos + widthLinks;
										
				}
				
				if (grensRechtsWaardePos + widthRechts > getSize().width)
				{	
					grensRechtsWaardePos -= grensRechtsWaardePos + widthRechts - getSize().width;	 	
					grensLinksWaardePos = grensRechtsWaardePos - widthRechts;
					
				}
			}	
				

			int vLinksSpace = 0;
			if (lowerGrensLinksLabels)
				vLinksSpace = 2 * theFM.getHeight();
			else
				vLinksSpace = theFM.getHeight();	
				
			int vRechtsSpace = 0;
			if (lowerGrensRechtsLabels)
				vRechtsSpace = 2 * theFM.getHeight();
			else
				vRechtsSpace = theFM.getHeight();	
				
			g.setFont(theFont);
			g.setColor(Color.black);
			if (grensZichtbaarFigOptie)
				g.drawString(grensLinksWaarde, grensLinksWaardePos,
						 yMin + vLinksSpace);
			if (grensZichtbaarFigOptie)
				g.drawString(grensRechtsWaarde, grensRechtsWaardePos,
						 yMin + vRechtsSpace);
			
			String lString = NormaleVerdeling.rb.getString("lTekst");			 
			String rString = NormaleVerdeling.rb.getString("rTekst");			 
			
			int lWidth = theFM.stringWidth(lString);
			int rWidth = theFM.stringWidth(rString);
			
			int lPos = grensLinksPos - hOffset - lWidth;
			if (lPos < 0)
				lPos = 0;
				
			int rPos = grensRechtsPos + hOffset;	
			if (rPos + rWidth > getSize().width)
				rPos -= rPos + rWidth - getSize().width;
			
			if (grensZichtbaarFigOptie)
				g.drawString(lString, lPos, yMin - theFM.getDescent());			 					  	  
			if (grensZichtbaarFigOptie)
				g.drawString(rString, rPos, yMin - theFM.getDescent());			 					  	  			
			
			String kansWaarde = UF.format(kans, kansDecimals);
			int width = theFM.stringWidth(kansWaarde);
			
			int kansPos = (grensLinksPos + grensRechtsPos) / 2 - width / 2;
			if (kansPos < 0)
				kansPos = 0;
			if (kansPos + width > getSize().width)
				kansPos -= kansPos + width - getSize().width;	
			
			g.setColor(kansColor);
			if (kansZichtbaarFigOptie)
				g.drawString(kansWaarde, kansPos, yMin - theFM.getHeight());
		}
	}
	
	// rekenen
/*	
	public void bereken()
	{
		bereken(false);
	}
*/
//	public void bereken(boolean sigmaInput)
	public void bereken()
	{	
/*	
		if(sigmaInput)
		{	minX = -45e-1d * sigma;
			maxX = 45e-1d * sigma;
			minY = 0;
			maxY = 6e-1d/sigma;
			
			minMuX = minX + mu;
			maxMuX = maxX + mu;
			
			zetGrensSlider();
		}
*/		
		if (berekenKeuze == BEREKENKANS)
		{	if (kansKeuze == KANSLINKS)
			{	zetKans(phi((grens - mu) / sigma), false);
			}
			else if (kansKeuze == KANSRECHTS)
			{	zetKans(1 - phi((grens - mu) / sigma), false);
			}
			else // kansKeuze == TWEEGRENZEN
			{	zetKans(phi((grensRechts - mu) / sigma) -
						phi((grensLinks - mu) / sigma), false);
			}
			
		}
		
		if (berekenKeuze == BEREKENGRENS)
		{	if (kansKeuze == KANSLINKS)
			{	// P[X<grens]=kans heeft als oplossing
				// grens=mu+sigma*phiInv(kans)
				zetGrens(mu + sigma * phiInv(kans), false);
			}
			else if (kansKeuze == KANSRECHTS)
			{	// P[X>grens]=kans heeft als oplossing
				// grens=mu+sigma*phiInv(1-kans)
				zetGrens(mu + sigma * phiInv(1 - kans), false);
			}
		}
		
		if (berekenKeuze == BEREKENGRENSLINKS)
		{	// kansKeuze == TWEEGRENZEN
			// P[grensLinks<X<grensRechts]=kans heeft als oplossing
			// voor grensLinks:
			// schrijf kans=P[grensLinks<X<grensRechts]=
			// 1-P[grensLinks<X]-P[X>grensRechts]	
			// dan is P[grensLinks<X]=1-P[X>grensRechts]-kans
			// met oplossing grensLinks=mu+sigma*phiInv(1-P[X>grensRechts]-kans)
			// waar P[X>grensRechts]=1-phi((grensRechts-mu)/sigma) 
			double kansWaarde = 1 - (1 - phi((grensRechts - mu) / sigma)) - kans; 
			if (kansWaarde < NZERO)
				kansWaarde = 0;
			
			zetGrensLinks(mu + sigma * phiInv(kansWaarde), false);
		}
		
		if (berekenKeuze == BEREKENGRENSRECHTS)
		{	// kansKeuze == TWEEGRENZEN
			// P[grensLinks<X<grensRechts]=kans heeft als oplossing
			// voor grensRechts:
			// schrijf kans=P[grensLinks<X<grensRechts]=
			// 1-P[grensLinks<X]-P[X>grensRechts]	
			// dan is P[grensRechst>X]=1-P[grensLinks<X]-kans
			// met oplossing grensRechts=mu+sigma*phiInv(1-(1-P[grensLinks<X]-kans))
			// waar P[grensLinks<X]=phi((grensLinks-mu)/sigma) 
			double kansWaarde = 1 - (1 - phi((grensLinks - mu) / sigma) - kans);
			if (kansWaarde > 1 - NZERO)
				kansWaarde = 1;
			
			zetGrensRechts(mu + sigma * phiInv(kansWaarde),	false);
		
		}
		
		if (berekenKeuze == BEREKENSIGMA)
		{	if (kansKeuze == KANSLINKS)
			{	// P[X<grens]=kans heeft als oplossing
				// grens=mu+sigma*phiInv(kans)
				// dus sigma=(grens-mu)/phiInv(kans)
				zetSigma((grens - mu) / phiInv(kans), false, true);
			}
			else if (kansKeuze == KANSRECHTS)
			{	// P[X>grens]=kans heeft als oplossing
				// grens=mu+sigma*phiInv(1-kans)
				// dus sigma=(grens-mu)/phiInv(1-kans)
				zetSigma((grens - mu) / phiInv(1-kans), false, true);
			}
			else // kansKeuze == TWEEGRENZEN
			{	// kans=P[grensLinks<X<grensRechts]=
				// phi((grensRechts - mu) / sigma) -
				// phi((grensLinks - mu) / sigma)

// dit werkt niet goed !!
				
				double sigmaStart = sigmaMin;
				double sigmaStep = 1e-1d;
				int steps = (int) Math.round((sigmaMax - sigmaMin) / sigmaStep);
				double kansWaarde = 
					phi((grensRechts - mu) / sigmaStart) -
					phi((grensLinks - mu) / sigmaStart);
				double sigmaSought = sigmaStart;	
					
				for (int sCnt = 1; sCnt <= steps; sCnt++)
				{	sigmaStart += sigmaStep;
					double waarde =
						phi((grensRechts - mu) / sigmaStart) -
						phi((grensLinks - mu) / sigmaStart);
					if (Math.abs(waarde - kans) < Math.abs(kansWaarde - kans))
					{	kansWaarde = waarde;
						sigmaSought = sigmaStart;
					}
				}	

				// hier nog een keer verfijnen	
				sigmaStart = sigmaSought - 2e-1d;
				sigmaStep = 2e-1d;
				double sigmaStop = sigmaSought + 2e-1d;
				steps = (int) Math.round((sigmaStop - sigmaStart) / sigmaStep);
				
				kansWaarde = 
					phi((grensRechts - mu) / sigmaStart) -
					phi((grensLinks - mu) / sigmaStart);
				sigmaSought = sigmaStart;	
				
				for (int sCnt = 1; sCnt <= steps; sCnt++)
				{	sigmaStart += sigmaStep;
					double waarde =
						phi((grensRechts - mu) / sigmaStart) -
						phi((grensLinks - mu) / sigmaStart);
					if (Math.abs(waarde - kans) < Math.abs(kansWaarde - kans))
					{	kansWaarde = waarde;
						sigmaSought = sigmaStart;
					}
				}	
				
				if (sigmaSought < sigmaMin + NZERO)
					sigmaSought = sigmaMin;
					
				if (sigmaSought > sigmaMax - NZERO)
					sigmaSought = sigmaMax;
								
//System.out.println("ss = " + sigmaSought);			
			
				zetSigma(sigmaSought, false, true);
			
			}
		}
		
		if (berekenKeuze == BEREKENMU)
		{	if (kansKeuze == KANSLINKS)
			{	// P[X<grens]=kans heeft als oplossing
				// grens=mu+sigma*phiInv(kans)
				// dus mu=grens-sigma*phiInv(kans)
				zetMu(grens - sigma * phiInv(kans), false, true);
			}
			else if (kansKeuze == KANSRECHTS)
			{	// P[X>grens]=kans heeft als oplossing
				// grens=mu+sigma*phiInv(1-kans)
				// dus mu=grens-sigma*phiInv(1-kans)
				zetMu(grens - sigma * phiInv(1-kans), false, true);
			}
			else // kansKeuze == TWEEGRENZEN
			{	// kans=P[grensLinks<X<grensRechts]=
				// phi((grensRechts - mu) / sigma) -
				// phi((grensLinks - mu) / sigma)
				
// dit werkt niet goed !!				
				
				double muStart = mu - 4 * sigma;
				double muEnd = mu + 4 * sigma;
				double muStep = 1e-1d;
				int steps = (int) Math.round((muEnd - muStart) / muStep);
				double kansWaarde = 
					phi((grensRechts - muStart) / sigma) -
					phi((grensLinks - muStart) / sigma);
				double muSought = muStart;	
				
				for (int sCnt = 1; sCnt <= steps; sCnt++)
				{	muStart += muStep;
					double waarde =
						phi((grensRechts - muStart) / sigma) -
						phi((grensLinks - muStart) / sigma);
					if (Math.abs(waarde - kans) < Math.abs(kansWaarde - kans))
					{	kansWaarde = waarde;
						muSought = muStart;
					}
				}	
				
				
				zetMu(muSought, false, true);				
			}
		}
		
		fastPaint();
	}

	// density function normale verdeling
	public double normalDF(double x)
	{	double fx = Math.pow(Math.E, 
							 - (x - mu) * (x - mu) / (sigma * sigma * 2)) /
					(sigma * Math.sqrt(2 * Math.PI));		 
		return fx;			
	}

	// distribution function voor standaard normale verdeling
	public double phi(double z)
	{	if (Math.abs(z) < NZERO)
			return 5e-1d;
		else 
			return (1 + erf(z / Math.sqrt(2))) / 2;
	}

	// een benadering voor de error function
	public double erf(double x)
	{	
		double erfx = StatUtil.erf(x);

		return erfx;
	}
	
	// inverse distribution function voor standaard normale verdeling
	public double phiInv(double p)
	{	
		double phiInvp = StatUtil.getInvCDF(p, true);
	
		return phiInvp;
	
	}
/*	
	// een benadering voor de inverse van de error function
	public double erfInv(double x)
	{	double a = 8 * (Math.PI - 3) / (3 * Math.PI * (4 - Math.PI));
	
		double signx = 1;
		if (x < -NZERO)
			signx = -1;
		
		double temp = 2 / (Math.PI * a) + Math.log(1 - x * x) / 2;	
			
		double erfInvx = signx *
			Math.sqrt(
				Math.sqrt(temp * temp - Math.log(1 - x * x) / a) - temp);
		
		return erfInvx;			  
					
	}
*/	
	// inner classes
	class KansKeuzeAL implements ActionListener
	{	public void actionPerformed(ActionEvent e)
		{	if (linksButton.isSelected())
				kansKeuze = KANSLINKS;
			else if (rechtsButton.isSelected())
				kansKeuze = KANSRECHTS;
			else // tweeGrenzenButton	
				kansKeuze = TWEEGRENZEN;

			zetKansKeuze();	
		
			bereken();	
			
			repaint();
			
			changed();
		}
	}

	class BerekenKeuzeAL implements ActionListener
	{	public void actionPerformed(ActionEvent e)
		{	if (muButton.isSelected())
				berekenKeuze = BEREKENMU;
			else if (sigmaButton.isSelected())
				berekenKeuze = BEREKENSIGMA;
			else if (grensButton.isSelected())
				berekenKeuze = BEREKENGRENS;
			else if (kansButton.isSelected())
				berekenKeuze = BEREKENKANS;
			else if (grensLinksButton.isSelected())
				berekenKeuze = BEREKENGRENSLINKS;
			else // grensRechtsButton
				berekenKeuze = BEREKENGRENSRECHTS;

			zetBerekenKeuze();
			
			changed();
			
		}
	}

	class KijkNaAL implements ActionListener
	{	public void actionPerformed(ActionEvent e)
		{
			kijkNa();
		}
		
	}
	
	class TextFL implements FocusListener
	{		
		JTextField inputTextField;
		
		public TextFL(JTextField input)
		{	inputTextField = input;
		}

	
		public void focusGained(FocusEvent e)
		{
		}
		public void focusLost(FocusEvent e)
		{	// invoer user
			String text = inputTextField.getText();
			// komma gebruikt
			if (text.indexOf(',') >= 0)
			{	String text1 = trimTrailingZeros(text, ',');
				boolean changed1 = (text.length() != text1.length());
				String text2 = addLeadingZero(text1, ',');
				boolean changed2 = (text1.length() != text2.length());
				if (changed1 || changed2)
				{	text = text2;
				}
			}
			// punt gebruikt
			if (text.indexOf('.') >= 0)
			{	String text1 = trimTrailingZeros(text, '.');
				boolean changed1 = (text.length() != text1.length());
				String text2 = addLeadingZero(text1, '.');
				boolean changed2 = (text1.length() != text2.length());
				if (changed1 || changed2)
				{	text = text2;
				}
			}	
			inputTextField.setText(text);				

			String format = new String(text);		
			format = format.replace(',', '.');		

			double userInput = 0;
			boolean error = false;
			try
			{	userInput = Double.parseDouble(format);
			}
			catch (NumberFormatException nfe)
			{	error = true;
			}
			// dit zou niet moeten gebeuren
			// Peter: nu wel bij de definitie van een random variabele ipv een double			
			if (error)
				return;

			if (inputTextField == muTextField)
			{	zetMu(userInput, true, true);
			}
			else if (inputTextField == sigmaTextField)
			{	zetSigma(userInput, true, true);
			}
			else if (inputTextField == grensTextField)
			{	zetGrens(userInput, true);
			}
			else if (inputTextField == grensLinksTextField)
			{	zetGrensLinks(userInput, true);
			}
			else if (inputTextField == grensRechtsTextField)
			{	zetGrensRechts(userInput, true);
			}
			else if (inputTextField == kansTextField)
			{	zetKans(userInput, true);
			}
			
			changed();
		} // focusLost
	}


	class TextAL implements ActionListener
	{	
		JTextField inputTextField;
		
		public TextAL(JTextField input)
		{	inputTextField = input;
		}
		
		public void actionPerformed(ActionEvent e)
		{	
			String text = inputTextField.getText();
			
			// komma gebruikt
			if (text.indexOf(',') >= 0)
			{	String text1 = trimTrailingZeros(text, ',');
				boolean changed1 = (text.length() != text1.length());
				String text2 = addLeadingZero(text1, ',');
				boolean changed2 = (text1.length() != text2.length());
				if (changed1 || changed2)
				{	text = text2;
				}
			}
			// punt gebruikt
			if (text.indexOf('.') >= 0)
			{	String text1 = trimTrailingZeros(text, '.');
				boolean changed1 = (text.length() != text1.length());
				String text2 = addLeadingZero(text1, '.');
				boolean changed2 = (text1.length() != text2.length());
				if (changed1 || changed2)
				{	text = text2;
				}
			}	
			inputTextField.setText(text);				

			String format = new String(text);		
			format = format.replace(',', '.');		

			double userInput = 0;
			boolean error = false;
			try
			{	userInput = Double.parseDouble(format);
			}
			catch (NumberFormatException nfe)
			{	error = true;
			}
			// dit zou niet moeten gebeuren  
			// Peter: nu wel bij de definitie van een random variabele ipv een double
			if (error)
			{	return;
			}
			
			if (inputTextField == muTextField)
			{	zetMu(userInput, true, true);
			}
			else if (inputTextField == sigmaTextField)
			{	zetSigma(userInput, true, true);
			}
			else if (inputTextField == grensTextField)
			{	zetGrens(userInput, true);
			}
			else if (inputTextField == grensLinksTextField)
			{	zetGrensLinks(userInput, true);
			}
			else if (inputTextField == grensRechtsTextField)
			{	zetGrensRechts(userInput, true);
			}
			else if (inputTextField == kansTextField)
			{	zetKans(userInput, true);
			}
			
			changed();
			
		} // actionPerformed
	}

	public String trimTrailingZeros(String s, char decSep)
	{	String txt = new String(s);
		if (txt.indexOf(decSep) < 0)
			return txt;
		char c = txt.charAt(txt.length() - 1);
		while (c == '0')
		{	txt = removeCharAt(txt, txt.length() - 1);
			c = txt.charAt(txt.length() - 1);
		}	
		c = txt.charAt(txt.length() - 1);
		if (c == decSep)
			txt = removeCharAt(txt, txt.length() - 1);
		return txt;		
	}				
		
	public String addLeadingZero(String s, char decSep)
	{	String txt = new String(s);
		// met minteken
		if ((txt.length() >= 2) && (txt.charAt(0) == '-') &&
			(txt.charAt(1) == decSep))
		{	txt = "-0" + txt.substring(1);
		}	
		// zonder minteken
		if ((txt.length() >= 1) && (txt.charAt(0) == decSep))
		{	txt = "0" + txt;
		}
		return txt;
	}


	public String removeCharAt(String s, int index)
	{	String txt = new String(s);
		// eerste
		if (index == 0)
			txt = txt.substring(1);
		// laatste	
		else if (index == (txt.length() - 1))
			txt = txt.substring(0, txt.length() - 1);
		// middenin	
		else
		{	String txt1 = txt.substring(0, index);
			String txt2 = txt.substring(index + 1);
			txt = txt1 + txt2;
		}
		return txt;
	}		
	
	class InputKL extends KeyAdapter
	{	
		JTextField inputTextField;
		boolean minusAllowed;
		
		public InputKL(JTextField input, boolean minAllowed)
		{	inputTextField = input;
			minusAllowed = minAllowed;
		}
		public void keyReleased(KeyEvent e)
		{	
			inputTextField.setForeground(Color.black);
		
			String txt = inputTextField.getText();
			
			//om randomvariabele in te kunnen vullen
			if (isLegal(txt))
				return;

//System.out.println(txt);
				
			boolean corrected = false;

			// kijk of txt illegale characters bevat
			// dit zou er maximaal 1 moeten zijn
			int index = -1;
			for (int cCnt = 0; cCnt < txt.length(); cCnt++)
			{	char c = txt.charAt(cCnt);
				if (!isLegal(c))
				{	index = cCnt;
//System.out.println("illegal " + index);				
				}
			}	
			// verwijder illegaal karakter
			if (index >= 0)
			{	txt = removeCharAt(txt, index);
				corrected = true;
//System.out.println("corr " + txt);							
			}
			
//System.out.println(txt);			
			
			// dubbele decimale komma
			// voldoende er twee te zoeken
			int pIndex1 = txt.indexOf(',');
			int pIndex2 = txt.lastIndexOf(',');
			if ((pIndex1 >= 0) && (pIndex2 >= 0) && (pIndex1 != pIndex2))
			{	// verwijderen
				txt = removeCharAt(txt, pIndex2);
				corrected = true;
			}

			// dubbele decimale punt
			// voldoende er twee te zoeken
			pIndex1 = txt.indexOf('.');
			pIndex2 = txt.lastIndexOf('.');
			if ((pIndex1 >= 0) && (pIndex2 >= 0) && (pIndex1 != pIndex2))
			{	// verwijderen
				txt = removeCharAt(txt, pIndex2);
				corrected = true;
			}
			
			// komma na decimale punt
			pIndex1 = txt.indexOf('.');
			pIndex2 = txt.lastIndexOf(',');
			if ((pIndex1 >= 0) && (pIndex2 >= 0) && (pIndex1 < pIndex2))
			{	// verwijderen
				txt = removeCharAt(txt, pIndex2);
				corrected = true;
			}
			
			// punt na decimale komma
			pIndex1 = txt.indexOf(',');
			pIndex2 = txt.lastIndexOf('.');
			if ((pIndex1 >= 0) && (pIndex2 >= 0) && (pIndex1 < pIndex2))
			{	// verwijderen
				txt = removeCharAt(txt, pIndex2);
				corrected = true;
			}
			
			// proberen een legaal karakter voor het
			// minteken (dit staat dan op plek 1) in te vullen
			if (txt.indexOf('-') == 1)
			{	txt = removeCharAt(txt, 0);
				corrected = true;
			}
			
			// minteken
			// alleen vooraan if any
			int minIndex = txt.lastIndexOf('-');
			if (minIndex > 0)
			{	txt = removeCharAt(txt, minIndex);
				corrected = true;
			}
			
			
			// leading zeros, leiden niet tot een NumberFormatException
			// geval met minteken
			if ((txt.indexOf('-') == 0) && (txt.length() >= 3) &&
				(txt.charAt(1) == '0') && Character.isDigit(txt.charAt(2)))
			{	txt = removeCharAt(txt, 1);
				corrected = true;
			}
			
			// leading zeros, leiden niet tot een NumberFormatException	
			// geen minteken
			if ((txt.indexOf('-') < 0) && (txt.length() >= 2) &&
				(txt.charAt(0) == '0') && Character.isDigit(txt.charAt(1)))
			{	txt = removeCharAt(txt, 0);
				corrected = true;
			}
			
			// trailing zeros na(!) decimale punt oplossen 
			// bij actionPerformed of focusLost			

			if (corrected)
			{	
//System.out.println("corr " + txt);							
				inputTextField.setText(txt);
			
			}
			
		}
		
		public boolean isLegal(String s)
		{	
			if (s != null && s.length() > 0)
				return s.charAt(0) == '#';
			else 
				return false;
		}
		
		public boolean isLegal(char c)
		{	
			if (minusAllowed)
				return Character.isDigit(c) || (c == ',') || (c == '.') || (c == '-');
			else	
				return Character.isDigit(c) || (c == ',') || (c == '.');
		}
	}	

	
	// interface WiskOpdrApplet
	public InteractiePanel getInteractiePanel()
	{	return this;
	}
	
	// interface InteractiePanel
	public void zetOpdracht(Hashtable b, String[] randomVars, Hashtable randomValues)
	{	
		
//System.out.println("raval " + randomVars.length);		
		double mu = 0;
		double sigma = 1;
		double grens = mu + 1;
		double grensLinks = mu - 1;
		double grensRechts = mu + 1;
		double kans = 6e-1d;
		int kansKeuze = KANSLINKS;
		int berekenKeuze = BEREKENKANS;
		
		if (b.containsKey("mu"))
			mu = ((Double) b.get("mu")).doubleValue();
		if (b.containsKey("sigma"))
			sigma = ((Double) b.get("sigma")).doubleValue();	
		if (b.containsKey("grens"))
			grens = ((Double) b.get("grens")).doubleValue();	
		if (b.containsKey("grenslinks"))
			grensLinks = ((Double) b.get("grenslinks")).doubleValue();	
		if (b.containsKey("grensrechts"))
			grensRechts = ((Double) b.get("grensrechts")).doubleValue();	
		if (b.containsKey("kans"))
			kans = ((Double) b.get("kans")).doubleValue();	
		
		if (b.containsKey("kanskeuze"))
			kansKeuze = ((Integer) b.get("kanskeuze")).intValue();	
		if (b.containsKey("berekenkeuze"))
			berekenKeuze = ((Integer) b.get("berekenkeuze")).intValue();	

		this.kansKeuze = kansKeuze;
		this.berekenKeuze = berekenKeuze;	

		boolean kansLinksOptie = true;
		boolean kansRechtsOptie = true;
		boolean tweeGrenzenOptie = true;
		
		boolean berekenbaarZichtbaar = true;
		boolean muBerekenbaarOptie = false;
		boolean sigmaBerekenbaarOptie = false;
		
		boolean muVastOptie = false;
		boolean sigmaVastOptie = false;
		
		boolean muSliderOptie = false;
		boolean sigmaSliderOptie = false;
		boolean grensSliderOptie = true;
		boolean kansSliderOptie = false;
		
		boolean muZichtbaarOptie = true;
		boolean sigmaZichtbaarOptie = true;
		boolean grensZichtbaarOptie = true;
		boolean kansZichtbaarOptie = true;
		
		boolean muZichtbaarFigOptie = true;
		boolean sigmaZichtbaarFigOptie = true;
		boolean grensZichtbaarFigOptie = true;
		boolean kansZichtbaarFigOptie = true;
		
		String muString = "";
		String sigmaString = "";
		String grensString = "";
		String grensLinksString = "";
		String grensRechtsString = "";
		String kansString = "";
		
		if (b.containsKey("kanslinksoptie"))
			kansLinksOptie = ((Boolean) b.get("kanslinksoptie")).booleanValue();
		if (b.containsKey("kansrechtsoptie"))
			kansRechtsOptie = ((Boolean) b.get("kansrechtsoptie")).booleanValue();
		if (b.containsKey("tweegrenzenoptie"))
			tweeGrenzenOptie = ((Boolean) b.get("tweegrenzenoptie")).booleanValue();
		
		if (b.containsKey("berekenbaarZichtbaar"))
			berekenbaarZichtbaar = ((Boolean) b.get("berekenbaarZichtbaar")).booleanValue();
		if (b.containsKey("muberekenbaaroptie"))
			muBerekenbaarOptie = ((Boolean) b.get("muberekenbaaroptie")).booleanValue();
		if (b.containsKey("sigmaberekenbaaroptie"))	
			sigmaBerekenbaarOptie = ((Boolean) b.get("sigmaberekenbaaroptie")).booleanValue();
		
		if (b.containsKey("muvastoptie"))
			muVastOptie = ((Boolean) b.get("muvastoptie")).booleanValue();
		if (b.containsKey("sigmavastoptie"))
			sigmaVastOptie = ((Boolean) b.get("sigmavastoptie")).booleanValue();
		
		if (b.containsKey("muSliderOptie")) 
			muSliderOptie = ((Boolean) b.get("muSliderOptie")).booleanValue();
		if (b.containsKey("sigmaSliderOptie")) 
			sigmaSliderOptie = ((Boolean) b.get("sigmaSliderOptie")).booleanValue();
		if (b.containsKey("kansSliderOptie")) 
			kansSliderOptie = ((Boolean) b.get("kansSliderOptie")).booleanValue();
		if (b.containsKey("grensSliderOptie")) 
			grensSliderOptie = ((Boolean) b.get("grensSliderOptie")).booleanValue();

		if (b.containsKey("muZichtbaarFigOptie")) 
			muZichtbaarFigOptie = ((Boolean) b.get("muZichtbaarFigOptie")).booleanValue();
		if (b.containsKey("sigmaZichtbaarFigOptie")) 
			sigmaZichtbaarFigOptie = ((Boolean) b.get("sigmaZichtbaarFigOptie")).booleanValue();
		if (b.containsKey("grensZichtbaarFigOptie")) 
			grensZichtbaarFigOptie = ((Boolean) b.get("grensZichtbaarFigOptie")).booleanValue();
		if (b.containsKey("kansZichtbaarFigOptie")) 
			kansZichtbaarFigOptie = ((Boolean) b.get("kansZichtbaarFigOptie")).booleanValue();

		if (b.containsKey("muZichtbaarOptie")) 
			muZichtbaarOptie = ((Boolean) b.get("muZichtbaarOptie")).booleanValue();
		if (b.containsKey("sigmaZichtbaarOptie")) 
			sigmaZichtbaarOptie = ((Boolean) b.get("sigmaZichtbaarOptie")).booleanValue();
		if (b.containsKey("grensZichtbaarOptie")) 
			grensZichtbaarOptie = ((Boolean) b.get("grensZichtbaarOptie")).booleanValue();
		if (b.containsKey("kansZichtbaarOptie")) 
			kansZichtbaarOptie = ((Boolean) b.get("kansZichtbaarOptie")).booleanValue();
		
		if (b.containsKey("muString"))
			muString = (String) b.get("muString");
		if (b.containsKey("sigmaString"))
			sigmaString = (String) b.get("sigmaString");
		if (b.containsKey("grensString"))
			grensString = (String) b.get("grensString");
		if (b.containsKey("grensLinksString"))
			grensLinksString = (String) b.get("grensLinksString");
		if (b.containsKey("grensRechtsString"))
			grensRechtsString = (String) b.get("grensRechtsString");
		if (b.containsKey("kansString"))
			kansString = (String) b.get("kansString");
		
		if (muString.length() > 0 && muString.charAt(0) == '#' && 
			muString.charAt(muString.length() - 1) == '#') 
			mu = substitueerRandom(mu, muString, randomVars, randomValues);
		if (sigmaString.length() > 0 && sigmaString.charAt(0) == '#' && 
			sigmaString.charAt(sigmaString.length() - 1) == '#') 
			sigma = substitueerRandom(sigma, sigmaString, randomVars, randomValues);
		if (grensString.length() > 0 && grensString.charAt(0) == '#' && 
			grensString.charAt(grensString.length() - 1) == '#') 
			grens = substitueerRandom(grens, grensString, randomVars, randomValues);
		if (grensLinksString.length() > 0 && grensLinksString.charAt(0) == '#' && 
			grensLinksString.charAt(grensLinksString.length() - 1) == '#') 
			grensLinks = substitueerRandom(grensLinks, grensLinksString, randomVars, randomValues);
		if (grensRechtsString.length() > 0 && grensRechtsString.charAt(0) == '#' && 
			grensRechtsString.charAt(grensRechtsString.length() - 1) == '#') 
			grensRechts = substitueerRandom(grensRechts, grensRechtsString, randomVars, randomValues);
		if (kansString.length() > 0 && kansString.charAt(0) == '#' && 
			kansString.charAt(kansString.length() - 1) == '#') 
			kans = substitueerRandom(kans,kansString, randomVars, randomValues);
		
		zetMu(mu, false, true);		
		zetSigma(sigma, false, true);
		zetGrens(grens, false);
		zetGrensLinks(grensLinks, false);				
		zetGrensRechts(grensRechts, false);						
		zetKans(kans, false);

		zetKansKeuze();
		
		if (this.kansKeuze == TWEEGRENZEN)
		{	zetGrensLinks(grensLinks, false);				
			zetGrensRechts(grensRechts, false);						
		}
		
		zetBerekenKeuze();
		
		/*
		muTextField.setText(muString);
		sigmaTextField.setText(sigmaString);
		grensTextField.setText(grensString);
		grensLinksTextField.setText(grensLinksString);
		grensRechtsTextField.setText(grensRechtsString);
		kansTextField.setText(kansString);
		*/
		
		this.kansLinksOptie = kansLinksOptie;
		this.kansRechtsOptie = kansRechtsOptie;		
		this.tweeGrenzenOptie = tweeGrenzenOptie;
		
		this.muBerekenbaarOptie = muBerekenbaarOptie;
		this.berekenbaarZichtbaar = berekenbaarZichtbaar;
		this.sigmaBerekenbaarOptie = sigmaBerekenbaarOptie;
		
		this.muVastOptie = muVastOptie;
		this.sigmaVastOptie = sigmaVastOptie;
		
		this.muSliderOptie = muSliderOptie;
		this.sigmaSliderOptie = sigmaSliderOptie;
		this.kansSliderOptie = kansSliderOptie;
		this.grensSliderOptie = grensSliderOptie;
		
		this.muZichtbaarOptie = muZichtbaarOptie;
		this.sigmaZichtbaarOptie = sigmaZichtbaarOptie;
		this.grensZichtbaarOptie = grensZichtbaarOptie;
		this.kansZichtbaarOptie = kansZichtbaarOptie;
		
		this.muZichtbaarFigOptie = muZichtbaarFigOptie;
		this.sigmaZichtbaarFigOptie = sigmaZichtbaarFigOptie;
		this.grensZichtbaarFigOptie = grensZichtbaarFigOptie;
		this.kansZichtbaarFigOptie = kansZichtbaarFigOptie;
		
		zetKansOpties();
		
		zetMuBerekenbaarOptie(this.muBerekenbaarOptie);
		
		zetSigmaBerekenbaarOptie(this.sigmaBerekenbaarOptie);						
		
		zetMuVastOptie(this.muVastOptie);

		zetSigmaVastOptie(this.sigmaVastOptie);						
		
		zetMuSliderOptie(this.muSliderOptie);
		zetSigmaSliderOptie(this.sigmaSliderOptie);
		zetGrensSliderOptie(this.grensSliderOptie);		
		zetKansSliderOptie(this.kansSliderOptie);

		zetMuZichtbaarOptie(this.muZichtbaarOptie);
		zetSigmaZichtbaarOptie(this.sigmaZichtbaarOptie);
		zetGrensZichtbaarOptie(this.grensZichtbaarOptie);
		zetKansZichtbaarOptie(this.kansZichtbaarOptie);
		
		zetMuZichtbaarFigOptie(this.muZichtbaarFigOptie);
		zetSigmaZichtbaarFigOptie(this.sigmaZichtbaarFigOptie);
		zetGrensZichtbaarFigOptie(this.grensZichtbaarFigOptie);
		zetKansZichtbaarFigOptie(this.kansZichtbaarFigOptie);
		

		zetBerekenKeuze();

		bereken();

		if (b.containsKey("kijkNa"))
			kijkOpdrachtNa = ((Boolean) b.get("kijkNa")).booleanValue();

		zetKijkOpdrachtNa(kijkOpdrachtNa);
		
		if (kijkOpdrachtNa)
		{
		
			antwoordMu = 0;
			if (b.containsKey("kijkMuNa"))
			{	kijkMuNa = ((Boolean) b.get("kijkMuNa")).booleanValue();
				if (b.containsKey("checkMu"))
				{	String checkMu = (String) b.get("checkMu");
					if (checkMu.length() > 0 && checkMu.charAt(0) == '#' && 
						checkMu.charAt(checkMu.length() - 1) == '#') 
						antwoordMu = substitueerRandom(antwoordMu, checkMu, randomVars, randomValues);
					else if (!checkMu.equals(""))
					{	checkMu = checkMu.replace(',', '.');	
						antwoordMu = Double.parseDouble(checkMu);
					}
					
//System.out.println("am = " + antwoordMu);					
				}
			}
			antwoordSigma = 1;
			if (b.containsKey("kijkSigmaNa"))
			{	kijkSigmaNa = ((Boolean) b.get("kijkSigmaNa")).booleanValue();
				if (b.containsKey("checkSigma"))
				{	String checkSigma = (String) b.get("checkSigma");
					if (checkSigma.length() > 0 && checkSigma.charAt(0) == '#' && 
						checkSigma.charAt(checkSigma.length() - 1) == '#') 
						antwoordSigma = substitueerRandom(antwoordSigma, checkSigma, randomVars, randomValues);
					else if (!checkSigma.equals(""))
					{	checkSigma = checkSigma.replace(',', '.');
						antwoordSigma = Double.parseDouble(checkSigma);
					}
				}
			}
			antwoordGrens = antwoordMu - 1;
			if (b.containsKey("kijkGrensNa"))
			{	kijkGrensNa = ((Boolean) b.get("kijkGrensNa")).booleanValue();
				if (b.containsKey("checkGrens"))
				{	String checkGrens = (String) b.get("checkGrens");
					if (checkGrens.length() > 0 && checkGrens.charAt(0) == '#' && 
						checkGrens.charAt(checkGrens.length() - 1) == '#') 
						antwoordGrens = substitueerRandom(antwoordGrens, checkGrens, randomVars, randomValues);
					else if (!checkGrens.equals(""))
					{	checkGrens = checkGrens.replace(',', '.');	
					 	antwoordGrens = Double.parseDouble(checkGrens);
					} 	
				}
			}
			antwoordGrensLinks = antwoordMu - 1;
			if (b.containsKey("kijkGrensLinksNa"))
			{	kijkGrensLinksNa = ((Boolean) b.get("kijkGrensLinksNa")).booleanValue();
				if (b.containsKey("checkGrensLinks"))
				{	String checkGrensLinks = (String) b.get("checkGrensLinks");
					if (checkGrensLinks.length() > 0 && checkGrensLinks.charAt(0) == '#' && 
						checkGrensLinks.charAt(checkGrensLinks.length() - 1) == '#') 
						antwoordGrensLinks = substitueerRandom(antwoordGrensLinks, checkGrensLinks, randomVars, randomValues);
					else if (!checkGrensLinks.equals(""))
					{	checkGrensLinks = checkGrensLinks.replace(',', '.');	
						antwoordGrensLinks = Double.parseDouble(checkGrensLinks);
					}
				}
			}
			antwoordGrensRechts = antwoordMu + 1;
			if (b.containsKey("kijkGrensRechtsNa"))
			{	kijkGrensRechtsNa = ((Boolean) b.get("kijkGrensRechtsNa")).booleanValue();
				if (b.containsKey("checkGrensRechts"))
				{	String checkGrensRechts = (String) b.get("checkGrensRechts");
					if (checkGrensRechts.length() > 0 && checkGrensRechts.charAt(0) == '#' && 
						checkGrensRechts.charAt(checkGrensRechts.length() - 1) == '#') 
						antwoordGrensRechts = substitueerRandom(antwoordGrensRechts, checkGrensRechts, randomVars, randomValues);
					else if (!checkGrensRechts.equals(""))
					{	checkGrensRechts = checkGrensRechts.replace(',', '.');
						antwoordGrensRechts = Double.parseDouble(checkGrensRechts);
					}
				}
			}
			antwoordKans = 25e-2d;
			if (b.containsKey("kijkKansNa"))
			{	kijkKansNa = ((Boolean) b.get("kijkKansNa")).booleanValue();
				if (b.containsKey("checkKans"))
				{	String checkKans = (String) b.get("checkKans");
					if (checkKans.length() > 0 && checkKans.charAt(0) == '#' && 
						checkKans.charAt(checkKans.length() - 1) == '#') 
						antwoordKans = substitueerRandom(antwoordKans, checkKans, randomVars, randomValues);
					else if (!checkKans.equals(""))
					{	
//System.out.println("" + checkKans);						
						checkKans  = checkKans.replace(',', '.');
//System.out.println("" + checkKans);						
						antwoordKans = Double.parseDouble(checkKans);
					}
				}
			}
			maxScore = 0;
			if (b.containsKey("maxScore"))
			{	String maxScoreStr = (String) b.get("maxScore");
				if (!maxScoreStr.equals(""))
					maxScore = Integer.parseInt(maxScoreStr);
			}
			
			
			
			
		}
		
/*		

		boolean kijkGrensNa;
		double antwoordGrens;
		boolean kijkGrensLinksNa;
		double antwoordGrensLinks;
		boolean kijkGrensRechtsNa;
		double antwoordGrensRechts;
		boolean kijkKansNa;
		double antwoordKans;
		
		int maxScore;	
		int score;
*/		
	}
	
	public static double substitueerRandom(double def, String s, String[] randomVars, Hashtable randomValues) 
	{	double d = Double.NaN;
		s = s.substring(1, s.length() - 1);
		String[] delen = StringUtils.split(s, "/");
		int decFactor = 1;
		
		for (int j = 0 ; j < randomVars.length; j++)
		{	
//System.out.println("rava " + j + " " + randomVars[j]);			
			if (randomVars[j].equals(delen[0])) 
				d = ((Integer) randomValues.get(randomVars[j])).intValue();
		}
		if (delen.length > 1)
		{	decFactor = Integer.parseInt(delen[1]);
			d = d / decFactor;
		}
		if (Double.isNaN(d)) 
			d = def;
		return d;
	}

	public void setState(Hashtable b)
	{	
		double mu = 0;
		double sigma = 1;
		double grens = mu + 1;
		double grensLinks = mu - 1;
		double grensRechts = mu + 1;
		double kans = 6e-1d;
		int kansKeuze = KANSLINKS;
		int berekenKeuze = BEREKENKANS;
		
		if (b.containsKey("mu"))
			mu = ((Double) b.get("mu")).doubleValue();
		if (b.containsKey("sigma"))
			sigma = ((Double) b.get("sigma")).doubleValue();	
		if (b.containsKey("grens"))
			grens = ((Double) b.get("grens")).doubleValue();	
		if (b.containsKey("grenslinks"))
			grensLinks = ((Double) b.get("grenslinks")).doubleValue();	
		if (b.containsKey("grensrechts"))
			grensRechts = ((Double) b.get("grensrechts")).doubleValue();	
		if (b.containsKey("kans"))
			kans = ((Double) b.get("kans")).doubleValue();	
		
		if (b.containsKey("kanskeuze"))
			kansKeuze = ((Integer) b.get("kanskeuze")).intValue();	
		if (b.containsKey("berekenkeuze"))
			berekenKeuze = ((Integer) b.get("berekenkeuze")).intValue();	

		this.kansKeuze = kansKeuze;
		this.berekenKeuze = berekenKeuze;	

		zetMu(mu, false, true);
		zetSigma(sigma, false, true);
		zetGrens(grens, false);
		
//		this.grensRechts = grensRechts;
		
		zetGrensLinks(grensLinks, false);				
		zetGrensRechts(grensRechts, false);						
		zetKans(kans, false);
		
//System.out.println("k = " + kans);		
		
		zetKansKeuze();
		
		if (this.kansKeuze == TWEEGRENZEN)
		{	zetGrensLinks(grensLinks, false);				
			zetGrensRechts(grensRechts, false);						
		}
		
		zetBerekenKeuze();
		
		bereken();
		
		if (b.containsKey("nagekeken"))
			nagekeken = ((Boolean) b.get("nagekeken")).booleanValue();
		
		if ((mode == 0 || nagekeken))
		//if (nagekeken)
			kijkNa();

	}
	
	public void setEditState(Hashtable b)
	{	
		setState(b);
		
		boolean kansLinksOptie = true;
		boolean kansRechtsOptie = true;
		boolean tweeGrenzenOptie = true;
		
		boolean berekenbaarZichtbaar = true;
		boolean muBerekenbaarOptie = false;
		boolean sigmaBerekenbaarOptie = false;
		
		boolean muVastOptie = false;
		boolean sigmaVastOptie = false;
		
		boolean muSliderOptie = false;
		boolean sigmaSliderOptie = false;
		boolean grensSliderOptie = true;
		boolean kansSliderOptie = false;
		
		boolean muZichtbaarOptie = true;
		boolean sigmaZichtbaarOptie = true;
		boolean grensZichtbaarOptie = true;
		boolean kansZichtbaarOptie = true;
		
		boolean muZichtbaarFigOptie = true;
		boolean sigmaZichtbaarFigOptie = true;
		boolean grensZichtbaarFigOptie = true;
		boolean kansZichtbaarFigOptie = true;
		
		String muString = "";
		String sigmaString = "";
		String grensString = "";
		String grensLinksString = "";
		String grensRechtsString = "";
		String kansString = "";
		
		if (b.containsKey("kanslinksoptie"))
			kansLinksOptie = ((Boolean) b.get("kanslinksoptie")).booleanValue();
		if (b.containsKey("kansrechtsoptie"))
			kansRechtsOptie = ((Boolean) b.get("kansrechtsoptie")).booleanValue();
		if (b.containsKey("tweegrenzenoptie"))
			tweeGrenzenOptie = ((Boolean) b.get("tweegrenzenoptie")).booleanValue();
		
		if (b.containsKey("berekenbaarZichtbaar"))
			berekenbaarZichtbaar = ((Boolean) b.get("berekenbaarZichtbaar")).booleanValue();
		if (b.containsKey("muberekenbaaroptie"))
			muBerekenbaarOptie = ((Boolean) b.get("muberekenbaaroptie")).booleanValue();
		if (b.containsKey("sigmaberekenbaaroptie"))	
			sigmaBerekenbaarOptie = ((Boolean) b.get("sigmaberekenbaaroptie")).booleanValue();
		
		if (b.containsKey("muvastoptie"))
			muVastOptie = ((Boolean) b.get("muvastoptie")).booleanValue();
		if (b.containsKey("sigmavastoptie"))
			sigmaVastOptie = ((Boolean) b.get("sigmavastoptie")).booleanValue();
		
		if (b.containsKey("muSliderOptie")) 
			muSliderOptie = ((Boolean) b.get("muSliderOptie")).booleanValue();
		if (b.containsKey("sigmaSliderOptie")) 
			sigmaSliderOptie = ((Boolean) b.get("sigmaSliderOptie")).booleanValue();
		if (b.containsKey("grensSliderOptie")) 
			grensSliderOptie = ((Boolean) b.get("grensSliderOptie")).booleanValue();			
		if (b.containsKey("kansSliderOptie")) 
			kansSliderOptie = ((Boolean) b.get("kansSliderOptie")).booleanValue();

		if (b.containsKey("muZichtbaarFigOptie")) 
			muZichtbaarFigOptie = ((Boolean) b.get("muZichtbaarFigOptie")).booleanValue();
		if (b.containsKey("sigmaZichtbaarFigOptie")) 
			sigmaZichtbaarFigOptie = ((Boolean) b.get("sigmaZichtbaarFigOptie")).booleanValue();
		if (b.containsKey("grensZichtbaarFigOptie")) 
			grensZichtbaarFigOptie = ((Boolean) b.get("grensZichtbaarFigOptie")).booleanValue();
		if (b.containsKey("kansZichtbaarFigOptie")) 
			kansZichtbaarFigOptie = ((Boolean) b.get("kansZichtbaarFigOptie")).booleanValue();

		if (b.containsKey("muZichtbaarOptie")) 
			muZichtbaarOptie = ((Boolean) b.get("muZichtbaarOptie")).booleanValue();
		if (b.containsKey("sigmaZichtbaarOptie")) 
			sigmaZichtbaarOptie = ((Boolean) b.get("sigmaZichtbaarOptie")).booleanValue();
		if (b.containsKey("grensZichtbaarOptie")) 
			grensZichtbaarOptie = ((Boolean) b.get("grensZichtbaarOptie")).booleanValue();
		if (b.containsKey("kansZichtbaarOptie")) 
			kansZichtbaarOptie = ((Boolean) b.get("kansZichtbaarOptie")).booleanValue();
		
		if (b.containsKey("muString"))
			muString = (String) b.get("muString");
		if (b.containsKey("sigmaString"))
			sigmaString = (String) b.get("sigmaString");
		if (b.containsKey("grensString"))
			grensString = (String) b.get("grensString");
		if (b.containsKey("grensLinksString"))
			grensLinksString = (String) b.get("grensLinksString");
		if (b.containsKey("grensRechtsString"))
			grensRechtsString = (String) b.get("grensRechtsString");
		if (b.containsKey("kansString"))
			kansString = (String) b.get("kansString");
		
		if (!muString.equals(""))
			muTextField.setText(muString);
		if (!muString.equals(""))
			sigmaTextField.setText(sigmaString);
		if (!muString.equals(""))
			grensTextField.setText(grensString);
		if (!muString.equals(""))
			grensLinksTextField.setText(grensLinksString);
		if (!muString.equals(""))
			grensRechtsTextField.setText(grensRechtsString);
		if (!muString.equals(""))
			kansTextField.setText(kansString);
		
		this.kansLinksOptie = kansLinksOptie;
		this.kansRechtsOptie = kansRechtsOptie;		
		this.tweeGrenzenOptie = tweeGrenzenOptie;
		
		this.muBerekenbaarOptie = muBerekenbaarOptie;
		this.berekenbaarZichtbaar = berekenbaarZichtbaar;
		this.sigmaBerekenbaarOptie = sigmaBerekenbaarOptie;
		this.muVastOptie = muVastOptie;
		this.sigmaVastOptie = sigmaVastOptie;
		
		this.muSliderOptie = muSliderOptie;
		this.sigmaSliderOptie = sigmaSliderOptie;
		this.kansSliderOptie = kansSliderOptie;
		this.grensSliderOptie = grensSliderOptie;
		
		this.muZichtbaarOptie = muZichtbaarOptie;
		this.sigmaZichtbaarOptie = sigmaZichtbaarOptie;
		this.grensZichtbaarOptie = grensZichtbaarOptie;
		this.kansZichtbaarOptie = kansZichtbaarOptie;
		
		this.muZichtbaarFigOptie = muZichtbaarFigOptie;
		this.sigmaZichtbaarFigOptie = sigmaZichtbaarFigOptie;
		this.grensZichtbaarFigOptie = grensZichtbaarFigOptie;
		this.kansZichtbaarFigOptie = kansZichtbaarFigOptie;
		
				
		zetKansOpties();
		
		zetMuBerekenbaarOptie(this.muBerekenbaarOptie);
		
//System.out.println("muBerekenbaarOptie = " + this.muBerekenbaarOptie);		
//System.out.println("muVastOptie = " + this.muVastOptie);				
		
		zetSigmaBerekenbaarOptie(this.sigmaBerekenbaarOptie);						
		
		zetMuVastOptie(this.muVastOptie);

//System.out.println("muBerekenbaarOptie = " + this.muBerekenbaarOptie);				
//System.out.println("muVastOptie = " + this.muVastOptie);				


		zetSigmaVastOptie(this.sigmaVastOptie);						
		
		zetMuSliderOptie(this.muSliderOptie);
		zetSigmaSliderOptie(this.sigmaSliderOptie);
		zetGrensSliderOptie(this.grensSliderOptie);		
		zetKansSliderOptie(this.kansSliderOptie);

		zetMuZichtbaarOptie(this.muZichtbaarOptie);
		zetSigmaZichtbaarOptie(this.sigmaZichtbaarOptie);
		zetGrensZichtbaarOptie(this.grensZichtbaarOptie);
		zetKansZichtbaarOptie(this.kansZichtbaarOptie);
		
		zetMuZichtbaarFigOptie(this.muZichtbaarFigOptie);
		zetSigmaZichtbaarFigOptie(this.sigmaZichtbaarFigOptie);
		zetGrensZichtbaarFigOptie(this.grensZichtbaarFigOptie);
		zetKansZichtbaarFigOptie(this.kansZichtbaarFigOptie);



		zetBerekenKeuze();


		bereken();
		
// is dit allemaal nodig??
// zie zetOpdracht		
		
//		if (b.containsKey("kijkNa"))
//			kijkOpdrachtNa = ((Boolean) b.get("kijkNa")).booleanValue();

/*	
		

		boolean kijkMuNa;
		double antwoordMu;
		boolean kijkSigmaNa;
		double antwoordSigma;
		boolean kijkGrensNa;
		double antwoordGrens;
		boolean kijkGrensLinksNa;
		double antwoordGrensLinks;
		boolean kijkGrensRechtsNa;
		double antwoordGrensRechts;
		boolean kijkKansNa;
		double antwoordKans;
		
		int maxScore;	
		int score;
*/		
	}

	public Hashtable getState()
	{	
	    Hashtable h = new Hashtable();
	    
	    h.put("mu", new Double(mu));
	    h.put("sigma", new Double(sigma));
	    h.put("grens", new Double(grens));
	    h.put("grenslinks", new Double(grensLinks));
	    h.put("grensrechts", new Double(grensRechts));
	    h.put("kans", new Double(kans));
	    
	    h.put("kanskeuze", new Integer(kansKeuze));
	    h.put("berekenkeuze", new Integer(berekenKeuze));
	    
	    h.put("nagekeken",new Boolean(nagekeken));
/*
	    h.put("kanslinksoptie", new Boolean(kansLinksOptie));
		h.put("kansrechtsoptie", new Boolean(kansRechtsOptie));	    	    
		h.put("tweegrenzenoptie", new Boolean(tweeGrenzenOptie));	    	    	    
		
		h.put("berekenbaarZichtbaar", new Boolean(berekenbaarZichtbaar));		
	    h.put("muberekenbaaroptie", new Boolean(muBerekenbaarOptie));		
	    h.put("sigmaberekenbaaroptie", new Boolean(sigmaBerekenbaarOptie));			    
	    
	    h.put("muvastoptie", new Boolean(muVastOptie));		
	    h.put("sigmavastoptie", new Boolean(sigmaVastOptie));
	    
	    h.put("muSliderOptie", new Boolean(muSliderOptie));
		h.put("sigmaSliderOptie", new Boolean(sigmaSliderOptie));
		h.put("grensSliderOptie", new Boolean(grensSliderOptie));	
		h.put("kansSliderOptie", new Boolean(kansSliderOptie));	
		
		h.put("muZichtbaarOptie", new Boolean(muZichtbaarOptie));
		h.put("sigmaZichtbaarOptie", new Boolean(sigmaZichtbaarOptie));
		h.put("grensZichtbaarOptie", new Boolean(grensZichtbaarOptie));	
		h.put("kansZichtbaarOptie", new Boolean(kansZichtbaarOptie));	
		
		h.put("muZichtbaarFigOptie", new Boolean(muZichtbaarFigOptie));
		h.put("sigmaZichtbaarFigOptie", new Boolean(sigmaZichtbaarFigOptie));
		h.put("grensZichtbaarFigOptie", new Boolean(grensZichtbaarFigOptie));	
		h.put("kansZichtbaarFigOptie", new Boolean(kansZichtbaarFigOptie));	
*/	    
		return h;
	}

	public Hashtable getEditState()
	{	
		String muString = "";
		String sigmaString = "";
		String grensString = "";
		String grensLinksString = "";
		String grensRechtsString = "";
		String kansString = "";
		
		muString = muTextField.getText();
		sigmaString = sigmaTextField.getText();
		grensString = grensTextField.getText();
		grensLinksString = grensLinksTextField.getText();
		grensRechtsString = grensRechtsTextField.getText();
		kansString = kansTextField.getText();
		
	    Hashtable h = getState();

	    h.put("kanslinksoptie", new Boolean(kansLinksOptie));
		h.put("kansrechtsoptie", new Boolean(kansRechtsOptie));	    	    
		h.put("tweegrenzenoptie", new Boolean(tweeGrenzenOptie));	    	    	    
		
		h.put("berekenbaarZichtbaar", new Boolean(berekenbaarZichtbaar));		
	    h.put("muberekenbaaroptie", new Boolean(muBerekenbaarOptie));		
	    h.put("sigmaberekenbaaroptie", new Boolean(sigmaBerekenbaarOptie));			    
	    
	    h.put("muvastoptie", new Boolean(muVastOptie));		
	    h.put("sigmavastoptie", new Boolean(sigmaVastOptie));
	    
	    h.put("muSliderOptie", new Boolean(muSliderOptie));
		h.put("sigmaSliderOptie", new Boolean(sigmaSliderOptie));
		h.put("grensSliderOptie", new Boolean(grensSliderOptie));	
		h.put("kansSliderOptie", new Boolean(kansSliderOptie));	
		
		h.put("muZichtbaarOptie", new Boolean(muZichtbaarOptie));
		h.put("sigmaZichtbaarOptie", new Boolean(sigmaZichtbaarOptie));
		h.put("grensZichtbaarOptie", new Boolean(grensZichtbaarOptie));	
		h.put("kansZichtbaarOptie", new Boolean(kansZichtbaarOptie));	
		
		h.put("muZichtbaarFigOptie", new Boolean(muZichtbaarFigOptie));
		h.put("sigmaZichtbaarFigOptie", new Boolean(sigmaZichtbaarFigOptie));
		h.put("grensZichtbaarFigOptie", new Boolean(grensZichtbaarFigOptie));	
		h.put("kansZichtbaarFigOptie", new Boolean(kansZichtbaarFigOptie));	

	    h.put("muString", muString);
	    h.put("sigmaString", sigmaString);
	    h.put("grensString", grensString);
	    h.put("grensLinksString", grensLinksString);
	    h.put("grensRechtsString", grensRechtsString);
	    h.put("kansString", kansString);
	    

	    h.put("nagekeken",new Boolean(nagekeken));
	    
	    return h;
	}

	public InteractieEditPanel getEditPanel()
	{	
		return new NormaalEditPanel(getSize().width + editBreedte, getSize().height);
	}

	// dit moet natuurlijk met een super !!		
	public void setBounds(int x, int y, int b, int h)
	{	super.setBounds(x, y, b, h);
	
		plaatsComponenten();
	}

	public void wis()
	{}
	
	public void zetMaat()
	{}
	
	public int geefAsHoogte()
	{	return 0;
	}

	public int getScore()
	{	return score;
	}
	
	public int getScoreMax()
	{	return maxScore;
	}
	
	public boolean isCorrect()
	{	
		if (kijkOpdrachtNa)
			return correct;
		else
			return true;
	}
	
	public boolean isFout()
	{	if (kijkOpdrachtNa)
			return fout;
		else
			return false;
	}
	
	public void zetMode(int mode)
	{
		this.mode = mode;
    	if (kijkOpdrachtNa)    
    		kijkOpdrachtNa = (mode == 0 || mode == 1);
	}
	
	public void zetNagekeken(boolean b)
	{
		nagekeken = b;
	}

	public void stop()
	{}
	
	public void start()
	{}
	
	public int getIpId()
	{	return 0;
	}
    
    public String getIpExpString()
    {	return null;
    }

    public void destroy()
    {}
    
    public void opnieuw()
    {}
    
    public void changed()
    {
    	if (kijkOpdrachtNa) 
		{	correct = false;
			fout = false;
    		vinkjeLabel.setVisible(false);
    		kruisjeLabel.setVisible(false);
    		fireChangeEvent();
		}
    }

    public void kijkNa()
    {
		boolean correct = true;
		if (kijkOpdrachtNa) 
		{
			if (kijkMuNa)
			{	double muIn = round(mu, muDecimals);
				double muAn = round(antwoordMu, muDecimals);
				correct = correct && (Math.abs(muIn - muAn) < NZERO);
			}
			
			if (kijkSigmaNa)
			{	double sigmaIn = round(sigma, sigmaDecimals);
				double sigmaAn = round(antwoordSigma, sigmaDecimals);
				correct = correct && (Math.abs(sigmaIn - sigmaAn) < NZERO);
			}

			if (kijkGrensNa)
			{	double grensIn = round(grens, grensDecimals);
				double grensAn = round(antwoordGrens, grensDecimals);
				correct = correct && (Math.abs(grensIn - grensAn) < NZERO);
			}

			if (kijkGrensLinksNa)
			{	double grensLinksIn = round(grensLinks, grensDecimals);
				double grensLinksAn = round(antwoordGrensLinks, grensDecimals);
				correct = correct && (Math.abs(grensLinksIn - grensLinksAn) < NZERO);
			}

			if (kijkGrensRechtsNa)
			{	double grensRechtsIn = round(grensRechts, grensDecimals);
				double grensRechtsAn = round(antwoordGrensRechts, grensDecimals);
				correct = correct && (Math.abs(grensRechtsIn - grensRechtsAn) < NZERO);
			}
			
			if (kijkKansNa)
			{	double kansIn = round(kans, kansDecimals);
				double kansAn = round(antwoordKans, kansDecimals);
				correct = correct && (Math.abs(kansIn - kansAn) < NZERO);
			}
			
			
		}
		this.correct = correct;
		fout = !correct;
		if (correct) 
		{	score = maxScore;
		}
		else 
		{	score = 0;
		}
		vinkjeLabel.setVisible(correct);
		kruisjeLabel.setVisible(!correct);
		
		zetNagekeken(true);
		
		fireChangeEvent();
/*		
		ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "changed");
		for (int lCnt = 0; lCnt < listeners.size(); lCnt++)
		{
			((ActionListener) listeners.elementAt(lCnt)).actionPerformed(event);
		}
*/		
		/*		
		Iterator<ActionListener> iterator = this.listeners.iterator();
		while(iterator.hasNext()) 
		{
			iterator.next().actionPerformed(event);
		}
*/		
	}
    	
    public void fireChangeEvent()
    {
		ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "changed");
		for (int lCnt = 0; lCnt < listeners.size(); lCnt++)
		{
			((ActionListener) listeners.elementAt(lCnt)).actionPerformed(event);
		}

    }
    
    public void kijkNa(int stapNr)
    {}
	
    public void addActionListener(ActionListener al)
    {
    	listeners.addElement(al);
    }
    
	public void actionPerformed(ActionEvent e)
	{	
		if ((e.getSource() == muSlider) &&
			e.getActionCommand().equals("verschoven"))
		{	processMuSlider();
			changed();
		}		
	
		if ((e.getSource() == sigmaSlider) &&
			e.getActionCommand().equals("verschoven"))
		{	processSigmaSlider();
			changed();
		}		
	
		if ((e.getSource() == grensSlider) &&
			e.getActionCommand().equals("verschoven"))
		{	processGrensSlider();
			changed();
		}		
		
		if ((e.getSource() == grensSlider) &&
			e.getActionCommand().equals("start"))
		{	lowerGrensLabels = true;
			//muMetWaardeLabel.setVisible(false);
			
			fastPaint();
		}
		if ((e.getSource() == grensSlider) &&
			e.getActionCommand().equals("stop"))
		{	lowerGrensLabels = false;
			//muMetWaardeLabel.setVisible(muZichtbaarFigOptie);

			fastPaint();
		}
		
		if ((e.getSource() == tweeGrenzenSlider) &&
		    e.getActionCommand().equals("verschovenLinks"))
		{	processTweeGrenzenSlider(true);
			changed();
		}    
		if ((e.getSource() == tweeGrenzenSlider) &&
		    e.getActionCommand().equals("startLinks"))
		{	lowerGrensLinksLabels = true;
			lowerGrensRechtsLabels = true;
			fastPaint();
		}    
		if ((e.getSource() == tweeGrenzenSlider) &&
		    e.getActionCommand().equals("verschovenRechts"))
		{	processTweeGrenzenSlider(false);
			changed();
		}    
		if ((e.getSource() == tweeGrenzenSlider) &&
		    e.getActionCommand().equals("startRechts"))
		{	lowerGrensLinksLabels = true;
			lowerGrensRechtsLabels = true;
	
			fastPaint();
		}    
		if ((e.getSource() == tweeGrenzenSlider) &&
		    e.getActionCommand().equals("stop"))
		{	lowerGrensLinksLabels = false;
			lowerGrensRechtsLabels = false;
			
//System.out.println("l = " + tweeGrenzenSlider.geefStandLinks());
//System.out.println("r = " + tweeGrenzenSlider.geefStandRechts());			
			fastPaint();
		}    
		if ((e.getSource() == kansSlider) &&
			e.getActionCommand().equals("verschoven"))
		{	processKansSlider();
			changed();
		}		
		
		
	}

	public void zetBreedte(int b)
	{	setBounds(getLocation().x, getLocation().y, b, getSize().height);
	}
	
	public void zetHoogte(int h)
	{	setBounds(getLocation().x, getLocation().y, getSize().width, h);
	}
	
}