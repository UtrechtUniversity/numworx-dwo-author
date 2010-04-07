package fi.normaleverdeling;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import fi.beans.wiskopdrbeans.WiskOpdrApplet;
// deze moet vanwege interface WiskOpdrApplet
import fi.beans.wiskopdrbeans.InteractiePanel;
// deze moet vanwege interface InteractiePanel
import fi.beans.wiskopdrbeans.InteractieEditPanel;

public class NormaalPanel extends JPanel implements 
													InteractiePanel,
													InteractieEditPanel,
													ActionListener
{	
	protected static int editBreedte = 150;	

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
	double minX = -75e-1d;
	double maxX = 75e-1d;
	double minY = 0;
	double maxY = 8e-1d;
	
	// grafiekgrenzen voor niet-standaardnormaal
	// wordt bij tekenen gezet op minX+mu en maxX+mu
	double minMuX = minX;
	double maxMuX = maxX;
	
	// parameters
	double mu = 0;
	int muDecimals = 2;
	double muMin = -100;
	double muMax = 100;
	double muSliderMin = mu - 1;
	double muSliderMax = mu + 1;
	
	double sigma = 1;
	int sigmaDecimals = 1;
	double sigmaMin = 1e-1d;
	double sigmaMax = 10;
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
	static Color pinkRed = new Color(203, 14, 113);	
    static Color lightRed = new Color(255, 99, 66);	
    
    Color areaColor = veryLightBlue;
    Color kansColor = pinkRed;
    Color muLineColor = Color.lightGray;
    
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
	
	

	public NormaalPanel(int w, int h)
	{	
		setBackground(Color.white);
		setLayout(null);
		
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
								      " = XXXXX", SwingConstants.CENTER);
		muMetWaardeLabel.setFont(theFont);
		width = theFM.stringWidth(muMetWaardeLabel.getText()) + 3;		
		muMetWaardeLabel.setSize(width, cHeight);
		add(muMetWaardeLabel);	

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

		int yPos = 0;

		// berekenGroup

		berekenLabel.setLocation(getSize().width - berekenLabel.getSize().width,
								 yPos);
		yPos += cHeight;						 	
		
		if (actualMuBerekenbaarOptie)
		{
			muButton.setLocation(getSize().width - muButton.getSize().width,
						     yPos);
			yPos += cHeight;						 							     
		}
		if (actualSigmaBerekenbaarOptie)
		{
			sigmaButton.setLocation(getSize().width - sigmaButton.getSize().width,
						        yPos);
			yPos += cHeight;
		}
		if (kansKeuze == TWEEGRENZEN)
		{	grensLinksButton.setLocation(getSize().width - grensLinksButton.getSize().width,
						                 yPos);
			yPos += cHeight;			                 
			grensRechtsButton.setLocation(getSize().width - grensRechtsButton.getSize().width,
						                  yPos);
			yPos += cHeight;			                 						                  
		}	
		else
		{
			grensButton.setLocation(getSize().width - grensButton.getSize().width,
						            yPos);
			yPos += cHeight;			                 						                  						            
		}	
	    kansButton.setLocation(getSize().width - kansButton.getSize().width,
						       yPos);


		// parameters
		yPos = 0;
		
		muLabel.setLocation(offSet, yPos);
		muTextField.setLocation(
			muLabel.getLocation().x + muLabel.getSize().width, 
			yPos);
		muWaardeLabel.setLocation(
			muLabel.getLocation().x + muLabel.getSize().width, 
			yPos);
		yPos += cHeight;			                 									

		if ((berekenKeuze != BEREKENMU) && !muVastOptie)
		{
			muSlider.setLocation(offSet, 
				yPos + (cHeight - muSlider.getSize().height) / 2); 
			yPos += cHeight1;			                 									
		}	

		sigmaLabel.setLocation(offSet, yPos);
		sigmaTextField.setLocation(
			sigmaLabel.getLocation().x + sigmaLabel.getSize().width, 
			yPos);
		sigmaWaardeLabel.setLocation(
			sigmaLabel.getLocation().x + sigmaLabel.getSize().width, 
			yPos);
		yPos += cHeight;			                 									

		if ((berekenKeuze != BEREKENSIGMA) && !sigmaVastOptie)
		{
			sigmaSlider.setLocation(offSet, 
				yPos + (cHeight - sigmaSlider.getSize().height) / 2); 
			yPos += cHeight1;
		}				                 									

		if (kansKeuze == TWEEGRENZEN)
		{	grensLinksLabel.setLocation(offSet, yPos);
			grensLinksTextField.setLocation(
				grensLinksLabel.getLocation().x + grensLinksLabel.getSize().width, 
				yPos);
			grensLinksWaardeLabel.setLocation(
				grensLinksLabel.getLocation().x + grensLinksLabel.getSize().width, 
				yPos);
			yPos += cHeight1;
			grensRechtsLabel.setLocation(offSet, yPos);
			grensRechtsTextField.setLocation(
				grensRechtsLabel.getLocation().x + grensRechtsLabel.getSize().width, 
				yPos);
			grensRechtsWaardeLabel.setLocation(
				grensRechtsLabel.getLocation().x + grensRechtsLabel.getSize().width, 
				yPos);
			yPos += cHeight1;			
		}
		else
		{
			grensLabel.setLocation(offSet, yPos);
			grensTextField.setLocation(
				grensLabel.getLocation().x + grensLabel.getSize().width, 
				yPos);
			grensWaardeLabel.setLocation(
				grensLabel.getLocation().x + grensLabel.getSize().width, 
				yPos);
			yPos += cHeight1;
		}	

		// kansLabel kan meerdere labels hebben
		int width = theFM.stringWidth(kansLabel.getText()) + 3;
		kansLabel.setBounds(offSet, yPos, width, cHeight); 
		kansTextField.setLocation(
			kansLabel.getLocation().x + kansLabel.getSize().width, 
			yPos);
		kansWaardeLabel.setLocation(
			kansLabel.getLocation().x + kansLabel.getSize().width, 
			yPos);
		yPos += cHeight1;	
			
			
		if (berekenKeuze != BEREKENKANS)
		{
			kansSlider.setLocation(offSet, 
				yPos + (cHeight - kansSlider.getSize().height) / 2); 
			yPos += cHeight1;
		}				                 									
			

		int hSpace = (getSize().width - muMetWaardeLabel.getSize().width) / 2;

		muMetWaardeLabel.setLocation(hSpace, getSize().height - cHeight);

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
				
		minMuX = minX + mu;
		maxMuX = maxX + mu;
	
		// muDecimals aanpassen
		if (Math.abs(mu) < 2)
			muDecimals = 3;
		else if (Math.abs(mu) < 20)
			muDecimals = 2;
		else
			muDecimals = 1;		
	
		String muString = UF.format(mu, muDecimals);
		
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
// dit gaat fout bij BEREKENMU??				
			}
			// nieuwe waarde kleiner, grenzen schuiven naar rechts
			else if (deltaMu < - NZERO)
			{	zetGrensRechts(grensRechts, false);
				zetGrensLinks(grensLinks, true);
// dit gaat fout bij BEREKENMU??								
			}			
		}

		// input via TextField
		if (resetSlider)
		{	if (Math.abs(mu) < (2 - NZERO))
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
			
			zetMuSlider();
		}
		// input via slider, afrondingsfouten
		//if (!bereken && !resetSlider)
		else
		{		
			if (mu < (muSliderMin + NZERO))
			{	mu = muSliderMin;
			}
			else if (mu > (muSliderMax - NZERO))
			{	mu = muSliderMax;
			}
			zetMuSlider();
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
	{	sigma = waarde;

				
		if (sigma > sigmaMax - NZERO)
		{	sigma = sigmaMax;

			if (berekenKeuze == BEREKENSIGMA)
			{
				berekenKeuze = BEREKENGRENS;
				bereken();
				berekenKeuze = BEREKENSIGMA;
			}	
			
		}
		if (sigma < sigmaMin + NZERO)
		{	sigma = sigmaMin;

			
			if (berekenKeuze == BEREKENSIGMA)
			{			
				berekenKeuze = BEREKENGRENS;
				bereken();
				berekenKeuze = BEREKENSIGMA;
			}	

		}
				
		// sigmaDecimals aanpassen
		if (sigma < 5 - NZERO)
			sigmaDecimals = 2;
		else
			sigmaDecimals = 1;	
		

		String sigmaString = UF.format(sigma, sigmaDecimals);
		
		if (NormaleVerdeling.langArg.equals("nl"))
			sigmaString = sigmaString.replace('.', ',');
			
		sigmaTextField.setText(sigmaString);
		sigmaWaardeLabel.setText(sigmaString);

// grenzen blijven op hun plaats, maar raken mogelijk buiten beeld!!

// dit hoeft niet want gebeurt al
//		if ((kansKeuze == KANSLINKS) || (kansKeuze == KANSRECHTS))
//		{	zetGrens(grens, false);
//		}


		if (resetSlider)
		{	if (sigma < (15e-1d - NZERO))
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

		if (bereken)
			bereken();

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

	
	public void zetGrens(double waarde, boolean bereken)
	{	grens = waarde;

		minMuX = minX + mu;
		maxMuX = maxX + mu;

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
	
	public void zetGrensSlider()
	{	minMuX = minX + mu;
		int sliderPos = (int) Math.round(
						    (grens - minMuX) / (maxX - minX) * (xMax - xMin));
		grensSlider.zetStand(sliderPos);				    
		fastPaint();
	}

	public void zetGrensLinks(double waarde, boolean bereken)
	{	
	
		// kontrole op waarden	
		grensLinks = waarde;

		minMuX = minX + mu;
		maxMuX = maxX + mu;

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
	{	minMuX = minX + mu;
		int sliderPos = (int) Math.round(
						    (grensLinks - minMuX) / (maxX - minX) * (xMax - xMin));
		tweeGrenzenSlider.zetStandLinks(sliderPos);				    
		
		fastPaint();
	}

	public void zetGrensRechts(double waarde, boolean bereken)
	{	
	
		// kontrole op waarden
		grensRechts = waarde;
		
		minMuX = minX + mu;
		maxMuX = maxX + mu;
		
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
	{	minMuX = minX + mu;
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

		minMuX = minX + mu;
		maxMuX = maxX + mu;

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
		muTextField.setVisible(true);
		muWaardeLabel.setVisible(false);
		muSlider.setVisible(true);
		muWaardeLabel.setForeground(Color.black);

		sigmaLabel.setForeground(Color.black);
		sigmaTextField.setVisible(true);
		sigmaWaardeLabel.setVisible(false);		
		sigmaSlider.setVisible(true);
		sigmaWaardeLabel.setForeground(Color.black);

		grensLabel.setForeground(Color.black);
		if (kansKeuze != TWEEGRENZEN)
		{	grensTextField.setVisible(true);		
			grensSlider.zetEnabled(true);
		}
		grensWaardeLabel.setVisible(false);		

		kansLabel.setForeground(Color.black);
		kansTextField.setVisible(true);		
		kansWaardeLabel.setVisible(false);
		kansSlider.setVisible(true);

		grensLinksLabel.setForeground(Color.black);
		if (kansKeuze == TWEEGRENZEN)
			grensLinksTextField.setVisible(true);		
		grensLinksWaardeLabel.setVisible(false);

		grensRechtsLabel.setForeground(Color.black);
		if (kansKeuze == TWEEGRENZEN)
		{	grensRechtsTextField.setVisible(true);		
			tweeGrenzenSlider.setVisible(true);
		}
		grensRechtsWaardeLabel.setVisible(false);

		grensSlider.zetEnabled(true);
		tweeGrenzenSlider.zetLinksEnabled(true);
		tweeGrenzenSlider.zetRechtsEnabled(true);

	}
	
	public void zetBerekenKeuze()
	{	
		resetParameters();
	
		if (berekenKeuze == BEREKENMU)
		{	muButton.setSelected(true);

			muLabel.setForeground(Color.blue);
			muSlider.setVisible(false);
			muTextField.setVisible(false);
			muWaardeLabel.setVisible(true);
			muWaardeLabel.setForeground(Color.blue);			
			
			grensSlider.zetEnabled(false);

			tweeGrenzenSlider.zetLinksEnabled(false);
			tweeGrenzenSlider.zetRechtsEnabled(false);
		
		}
		else if (berekenKeuze == BEREKENSIGMA)
		{	sigmaButton.setSelected(true);

			sigmaLabel.setForeground(Color.blue);
			sigmaSlider.setVisible(false);
			sigmaTextField.setVisible(false);
			sigmaWaardeLabel.setVisible(true);
			sigmaLabel.setForeground(Color.blue);			
		
		}
		else if (berekenKeuze == BEREKENGRENS)
		{	grensButton.setSelected(true);
			grensLabel.setForeground(Color.blue);
			grensTextField.setVisible(false);
			grensWaardeLabel.setVisible(true);
			grensSlider.zetEnabled(false);
		
		}
		else if (berekenKeuze == BEREKENKANS)
		{	kansButton.setSelected(true);
			kansLabel.setForeground(Color.blue);
			kansTextField.setVisible(false);
			kansWaardeLabel.setVisible(true);
		
			kansSlider.setVisible(false);	
		
		}
		else if (berekenKeuze == BEREKENGRENSLINKS)
		{	grensLinksButton.setSelected(true);
			grensLinksLabel.setForeground(Color.blue);
			grensLinksTextField.setVisible(false);
			grensLinksWaardeLabel.setVisible(true);
			tweeGrenzenSlider.zetLinksEnabled(false);
		
		}
		else if (berekenKeuze == BEREKENGRENSRECHTS)
		{	grensRechtsButton.setSelected(true);
			grensRechtsLabel.setForeground(Color.blue);
			grensRechtsTextField.setVisible(false);
			grensRechtsWaardeLabel.setVisible(true);
			tweeGrenzenSlider.zetRechtsEnabled(false);		
			
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


			if (grens > (mu + 1 - NZERO))
			{	zetGrensRechts(grens, false);
				zetGrensLinks(- grens, true);	
			}
			else
			{	zetGrensRechts(grens + 2, false);
				zetGrensLinks(grens, true);	
			}
			// waarden geven/overdragen
			//if (grensButton.isSelected())
			//	grensLinksButton.setSelected(true);	
			if (berekenKeuze == BEREKENGRENS)
			{	berekenKeuze = BEREKENGRENSLINKS;
				//zetBerekenKeuze();
			}
			
			zetBerekenKeuze();			
			
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
			muButton.setVisible(muBerekenbaarOptie);
			sigmaButton.setVisible(sigmaBerekenbaarOptie);
						
			zetBerekenKeuze();			
			plaatsComponenten();			
				
		}
		
//		plaatsComponenten();
	}


	public void zetKansOpties()
	{	// booleans zijn al gezet
	
		// override kansKeuze als nodig		
		if (!kansLinksOptie && (kansKeuze == KANSLINKS))
		{	if (kansRechtsOptie)
				kansKeuze = KANSRECHTS;
			else
				kansKeuze = TWEEGRENZEN;	
		}
		if (!kansRechtsOptie && (kansKeuze == KANSRECHTS))
		{	if (kansLinksOptie)
				kansKeuze = KANSLINKS;
			else
				kansKeuze = TWEEGRENZEN;	
		}
		if (!tweeGrenzenOptie && (kansKeuze == TWEEGRENZEN))
		{	if (kansLinksOptie)
				kansKeuze = KANSLINKS;
			else
				kansKeuze = KANSRECHTS;	
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
		
		zetKansKeuze();
		
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
		muSlider.setVisible(!b);

		
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
		sigmaSlider.setVisible(!b);

		plaatsComponenten();		
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
	{	minMuX = minX + mu;
		int stand = grensSlider.geefStand();
		double grensWaarde = minMuX + 
			((double) stand) / (xMax - xMin) * (maxX - minX);
		zetGrens(grensWaarde, true);	
	}

	public void processTweeGrenzenSlider(boolean links)
	{	minMuX = minX + mu;
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
	
	public void paint(Graphics g)
	{	if (ng == null)
			ng = getGraphics();

		if ((im == null) || (size.width != getSize().width) ||
			(size.height != getSize().height))
		{	im = createImage(getSize().width, getSize().height);
			og = im.getGraphics();
			size = new Dimension(getSize().width, getSize().height);
		}	
		
		
		og.setColor(Color.white);
		og.fillRect(0, 0, getSize().width, getSize().height);

		paintArea(og);
		paintMuLine(og);
		paintXAxis(og);
		paintDistribution(og);
		paintLabels(og);	
		paintComponents(og);
		
		g.drawImage(im, 0, 0, null);
	}
	
	
	public void paintXAxis(Graphics g)
	{	g.setColor(Color.black);
		g.drawLine(xMin, yMin, xMax, yMin);
	}
	
	public void paintMuLine(Graphics g)
	{	// centreren
		minMuX = minX + mu;
		
		int xPos = xMin + (xMax - xMin) / 2;
		double x = minMuX + ((double) (xPos - xMin)) / (xMax - xMin) * (maxX - minX);
		double fx = normalDF(x);
		int y = yMin - (int) Math.round(
								(fx - minY) / (maxY - minY) * (yMin - yMax));
		g.setColor(muLineColor);
		g.drawLine(xPos, y, xPos, yMin);		
	}

	public void paintDistribution(Graphics g)
	{	// centreren
		minMuX = minX + mu;
	
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
		minMuX = minX + mu;
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
		minMuX = minX + mu;
	
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
			g.drawString(grensWaarde, grensWaardePos,
						 yMin + vSpace);
			
			String gString = NormaleVerdeling.rb.getString("gTekst");			 
			width = theFM.stringWidth(gString);
			int gPos = grensPos + hOffset;
			if (gPos + width > getSize().width)
				gPos -= gPos + width - getSize().width;	
			g.drawString(gString, gPos, yMin - theFM.getDescent());			 					  	  
			
			String kansWaarde = UF.format(kans, kansDecimals);
			width = theFM.stringWidth(kansWaarde);
			int kansPos = grensPos - width - 2 * hOffset;
			if (kansPos < 0)
				kansPos = 0;
			
			g.setColor(kansColor);
			g.drawString(kansWaarde, kansPos,
					     yMin - theFM.getHeight());
			
			
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
			g.drawString(grensWaarde, 
						 grensWaardePos,
						 yMin + vSpace);
			
			String gString = NormaleVerdeling.rb.getString("gTekst");			 
			width = theFM.stringWidth(gString);
			int gPos = grensPos - hOffset - width;
			if (gPos < 0)
				gPos = 0;
			g.drawString(gString, gPos, yMin - theFM.getDescent());			 					  	  
			
			String kansWaarde = UF.format(kans, kansDecimals);
			width = theFM.stringWidth(kansWaarde);
			
			int kansPos = grensPos + 2 * hOffset;
			if (kansPos + width > getSize().width)
				kansPos -= kansPos + width - getSize().width;
			
			g.setColor(kansColor);
			g.drawString(kansWaarde, kansPos,
					     yMin - theFM.getHeight());
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
			g.drawString(grensLinksWaarde, 
						 grensLinksWaardePos,
						 yMin + vLinksSpace);
			g.drawString(grensRechtsWaarde, 
						 grensRechtsWaardePos,
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
			
			g.drawString(lString, lPos, yMin - theFM.getDescent());			 					  	  
			g.drawString(rString, rPos, yMin - theFM.getDescent());			 					  	  			
			
			String kansWaarde = UF.format(kans, kansDecimals);
			int width = theFM.stringWidth(kansWaarde);
			
			int kansPos = (grensLinksPos + grensRechtsPos) / 2 - width / 2;
			if (kansPos < 0)
				kansPos = 0;
			if (kansPos + width > getSize().width)
				kansPos -= kansPos + width - getSize().width;	
			
			g.setColor(kansColor);
			g.drawString(kansWaarde, kansPos,
					     yMin - theFM.getHeight());
		}
	}
	
	// rekenen

	public void bereken()
	{	
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
	// let op: de benadering met erf is alleen valide voor z>=0
	// voor negatieve z gebruik phi(z)=1-phi(-z)
	// merk op: erf(-z)=erf(z) 
	public double phi(double z)
	{	if (Math.abs(z) < NZERO)
			return 5e-1d;
		else if (z >= NZERO)	
			return (1 + erf(z / Math.sqrt(2))) / 2;
		else // z <= -NZERO)
			return 1 - (1 + erf(z / Math.sqrt(2))) / 2;	
	}

	// een benadering voor de error function
	public double erf(double x)
	{	double a = 8 * (Math.PI - 3) / (3 * Math.PI * (4 - Math.PI));
	
		double erfx = 
			Math.sqrt(1 - 
					  Math.exp(- x * x * ((4 / Math.PI) + a * x * x) /
					           (1 + a * x * x)));
					           
		return erfx;
	}
	
	// inverse distribution function voor standaard normale verdeling
	public double phiInv(double p)
	{	return Math.sqrt(2) * erfInv(2 * p - 1);
	}
	
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

//			if (langArg.equals("nl"))
//				txt = txt.replace(',', '.');		
				
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
//			if (NormaleVerdeling.langArg.equals("nl"))
//			{	
			
			// dubbele decimale komma
			// voldoende er twee te zoeken
			int pIndex1 = txt.indexOf(',');
			int pIndex2 = txt.lastIndexOf(',');
			if ((pIndex1 >= 0) && (pIndex2 >= 0) && (pIndex1 != pIndex2))
			{	// verwijderen
				txt = removeCharAt(txt, pIndex2);
				corrected = true;
			}

//			}
//			else
//			{

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
			
//			}	
			
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

//			if (langArg.equals("nl"))
//			{	txt = txt.replace('.', ',');		
//				corrected = true;
//			}
			
			if (corrected)
			{	
//System.out.println("corr " + txt);							
				inputTextField.setText(txt);
			
			}
			
		}
		
		public boolean isLegal(char c)
		{	//if (NormaleVerdeling.langArg.equals("nl"))
			//{	
			if (minusAllowed)
				return Character.isDigit(c) || (c == ',') || (c == '.') || (c == '-');
			else	
				return Character.isDigit(c) || (c == ',') || (c == '.');
			//}
			//else
			//{	if (minusAllowed)
			//		return Character.isDigit(c) || (c == '.') || (c == '-');
			//	else
			//		return Character.isDigit(c) || (c == '.');
			//}
		}
	}	

	
	// interface WiskOpdrApplet
	public InteractiePanel getInteractiePanel()
	{	return this;
	}
	
	// interface InteractiePanel
	public void zetOpdracht(Hashtable b, String[] randomVars, Hashtable randomValues)
	{	setEditState(b);
	}

	public void setState(Hashtable b)
	{	double mu = 0;
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
		zetGrensLinks(grensLinks, false);				
		zetGrensRechts(grensRechts, false);						
		zetKans(kans, false);
		
//System.out.println("k = " + kans);		
		
		zetKansKeuze();
		zetBerekenKeuze();
		
		
	}
	
	public void setEditState(Hashtable b)
	{	
		setState(b);
		
		boolean kansLinksOptie = true;
		boolean kansRechtsOptie = true;
		boolean tweeGrenzenOptie = true;
		
		boolean muBerekenbaarOptie = false;
		boolean sigmaBerekenbaarOptie = false;
		
		boolean muVastOptie = false;
		boolean sigmaVastOptie = false;
		
		if (b.containsKey("kanslinksoptie"))
			kansLinksOptie = ((Boolean) b.get("kanslinksoptie")).booleanValue();
		if (b.containsKey("kansrechtsoptie"))
			kansRechtsOptie = ((Boolean) b.get("kansrechtsoptie")).booleanValue();
		if (b.containsKey("tweegrenzenoptie"))
			tweeGrenzenOptie = ((Boolean) b.get("tweegrenzenoptie")).booleanValue();
		
		if (b.containsKey("muberekenbaaroptie"))
			muBerekenbaarOptie = 
				((Boolean) b.get("muberekenbaaroptie")).booleanValue();
		if (b.containsKey("sigmaberekenbaaroptie"))
			sigmaBerekenbaarOptie = 
				((Boolean) b.get("sigmaberekenbaaroptie")).booleanValue();
		
		if (b.containsKey("muvastoptie"))
			muVastOptie = 
				((Boolean) b.get("muvastoptie")).booleanValue();
		if (b.containsKey("sigmavastoptie"))
			sigmaVastOptie = 
				((Boolean) b.get("sigmavastoptie")).booleanValue();
		
		this.kansLinksOptie = kansLinksOptie;
		this.kansRechtsOptie = kansRechtsOptie;		
		this.tweeGrenzenOptie = tweeGrenzenOptie;
		this.muBerekenbaarOptie = muBerekenbaarOptie;
		this.sigmaBerekenbaarOptie = sigmaBerekenbaarOptie;
		this.muVastOptie = muVastOptie;
		this.sigmaVastOptie = sigmaVastOptie;
		
				
		zetKansOpties();
		
		zetMuBerekenbaarOptie(this.muBerekenbaarOptie);
		
//System.out.println("muBerekenbaarOptie = " + this.muBerekenbaarOptie);		
//System.out.println("muVastOptie = " + this.muVastOptie);				
		
		zetSigmaBerekenbaarOptie(this.sigmaBerekenbaarOptie);						
		
		zetMuVastOptie(this.muVastOptie);

//System.out.println("muBerekenbaarOptie = " + this.muBerekenbaarOptie);				
//System.out.println("muVastOptie = " + this.muVastOptie);				


		zetSigmaVastOptie(this.sigmaVastOptie);						
		


		bereken();
		
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

	    h.put("kanslinksoptie", new Boolean(kansLinksOptie));
		h.put("kansrechtsoptie", new Boolean(kansRechtsOptie));	    	    
		h.put("tweegrenzenoptie", new Boolean(tweeGrenzenOptie));	    	    	    
		
	    h.put("muberekenbaaroptie", new Boolean(muBerekenbaarOptie));		
	    h.put("sigmaberekenbaaroptie", new Boolean(sigmaBerekenbaarOptie));			    
	    
	    h.put("muvastoptie", new Boolean(muVastOptie));		
	    h.put("sigmavastoptie", new Boolean(sigmaVastOptie));			    
	    

	    return h;
	}

	public Hashtable getEditState()
	{	
	    Hashtable h = getState();
	    

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
	{	return 0;
	}
	
	public int getScoreMax()
	{	return 0;
	}
	
	public boolean isCorrect()
	{	return false;
	}
	
	public boolean isFout()
	{	return false;
	}
	
	public void zetMode(int mode)
	{}
	
	public void zetNagekeken(boolean b)
	{}

	public void stop()
	{}
	
	public void start()
	{}
	
	public int getIpId(){return 0;}
    
    public String getIpExpString(){return null;}

    public void destroy()
    {}
    
    public void opnieuw()
    {}
    
    public void kijkNa()
    {}
    
    public void kijkNa(int stapNr)
    {}
	
    public void addActionListener(ActionListener al)
    {}
    
	public void actionPerformed(ActionEvent e)
	{	
		if ((e.getSource() == muSlider) &&
			e.getActionCommand().equals("verschoven"))
		{	processMuSlider();
		}		
	
		if ((e.getSource() == sigmaSlider) &&
			e.getActionCommand().equals("verschoven"))
		{	processSigmaSlider();
		}		
	
		if ((e.getSource() == grensSlider) &&
			e.getActionCommand().equals("verschoven"))
		{	processGrensSlider();
		}		
		if ((e.getSource() == grensSlider) &&
			e.getActionCommand().equals("start"))
		{	lowerGrensLabels = true;
			fastPaint();
		}
		if ((e.getSource() == grensSlider) &&
			e.getActionCommand().equals("stop"))
		{	lowerGrensLabels = false;
			fastPaint();
		}
		
		if ((e.getSource() == tweeGrenzenSlider) &&
		    e.getActionCommand().equals("verschovenLinks"))
		{	processTweeGrenzenSlider(true);
		}    
		if ((e.getSource() == tweeGrenzenSlider) &&
		    e.getActionCommand().equals("startLinks"))
		{	lowerGrensLinksLabels = true;
			fastPaint();
		}    
		if ((e.getSource() == tweeGrenzenSlider) &&
		    e.getActionCommand().equals("verschovenRechts"))
		{	processTweeGrenzenSlider(false);
		}    
		if ((e.getSource() == tweeGrenzenSlider) &&
		    e.getActionCommand().equals("startRechts"))
		{	lowerGrensRechtsLabels = true;
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
		}		
		
		
	}

	public void zetBreedte(int b)
	{	setBounds(getLocation().x, getLocation().y, b, getSize().height);
	}
	
	public void zetHoogte(int h)
	{	setBounds(getLocation().x, getLocation().y, getSize().width, h);
	}
	
}