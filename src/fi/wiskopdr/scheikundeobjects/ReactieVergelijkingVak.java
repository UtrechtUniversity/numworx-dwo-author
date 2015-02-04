package fi.wiskopdr.scheikundeobjects;

import java.awt.AWTEventMulticaster;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.MissingResourceException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.cbook.cbookif.CBookEvent;
import org.cbook.cbookif.CBookEventHandler;
import org.cbook.cbookif.CBookEventListener;

import fi.beans.ideas.RuleIF;
import fi.beans.stringutils.StringUtils;
import fi.beans.wiskopdrbeans.CBookAware;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;
import fi.beans.wnwidgets.NWButtonUI;
import fi.wiskopdr.AntwoordVak;
import fi.wiskopdr.DialogFacade;
import fi.wiskopdr.ImageComponent;
import fi.wiskopdr.TekstVakPanel;
import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.formuleobjects.FormuleButton;
import fi.wiskopdr.formuleobjects.FormuleEditor;
//import fi.wiskopdr.formuleobjects.FormuleParser;
import fi.wiskopdr.formuleobjects.FormuleVak;
import fi.wiskopdr.formuleobjects.TabletOwner;
import fi.wiskopdr.opdrnav.MyOpdrContainer;
import fi.wiskopdr.opdrnav.OpdrNavStruct;
import fi.wiskopdr.tekstobjects.TekstArea;
import fi.wiskopdr.tekstobjects.TekstInteractiePanelVak;

public class ReactieVergelijkingVak extends AntwoordVak implements InteractiePanel, CBookAware {
	static int GOED = 1;
	static int FOUT = 0;
	static int HALF = 2;
	static int GEEN = 3;

	//private FormuleButton plusKnop, minKnop, maalKnop, deelKnop, haakjesKnop, herleidKnop, abcKnop, subKnop;
	//private FormuleButton ontbindKnop, splitsKnop, wortelBewerkKnop;
	protected FormuleButton subscriptKnop, enkelePijlKnop, dubbelePijlKnop;
	
	
	private boolean ingevuld = false;
	boolean nagekeken = false;

	private boolean isGelijkwaardig = false;
	private boolean isGelijkwaardigMoleculen = false;
	private boolean isGelijkwaardigMoleculenLading = false;
	private boolean pijlKlopt = false;
	private boolean elementenBalansKlopt = false;
	private boolean ladingenBalansKlopt = false;
	private boolean kanVereenvoudigd = false;
	
	private int puntenCorrect = 0;
	private int puntenMoleculen = 0;
	private int puntenElementen = 0;
	private int puntenLadingen = 0;
	private int aftrekVereenvoudigbaar = 0;
	
	private int score;
	private int scoreMax;
	private boolean correct;
	private boolean fout;
	private int errorCount;
	private int attemptsCount;
	
	private ImageComponent goedIC, foutIC, halfIC, huidigIC;
	private ImageComponent feedbackIC;
	private ReactieVergelijking gewensteEindOplossing;
	
	//private VergelijkingMeerv[] juisteVormen;

	private ImageComponent[] imageComponenten;
	
	private int mode = 0;

	private int formuleVakX = 30;
	private int formuleVakY = 10;

	private int imageCompX = 5;
	private Font formuleVakFont = (!WiskOpdr.formTimes || WiskOpdr.mac) ? WiskOpdr.formuleFont0Mac : WiskOpdr.formuleFont0; 
	
	//private Expressie substitutie;
	private TekstArea feedbackTekst;
	
	private String formuleVakString;

	private Hashtable[] answerModels;
	private String[] randomVarNamen;
	private Hashtable randomVarWaarden;
	
	private String feedback;
	private boolean feedbackSize;
	private int feedbackWidth = 200;
	private int feedbackHeight = 20;
	private boolean hasFeedback;
	private int puntenFeedback;
	private int goedHalfFout;

	//private Vergelijking[] antwoordSubstituties;
	//private String[] antwoordStringSubstituties;
	//private Vergelijking[] gebruikersSubstituties;
	//private FormuleEditor gebruikersSubstitutiesVak;
	
	private Vector log;
	private JButton logKnop;
	private DialogFacade logDialog;
	private JTextArea logTextArea;

	private boolean check;
	private boolean teltMee;

	private boolean logOption;
	private String logID;
	
	private boolean[][] logObjectives;

	private double eqTestValueMin = 0;
	private double eqTestValueMax = 5;

	private Image feedbackBallonImage;
	private JButton feedbackCloseButton;
	
	private boolean uitw = false;
	private FormuleButton wisKnop;
	
	//private ReactieVergelijking huidigeVergelijking;
	//private VergelijkingMeerv huidigeVergelijking;
	
	private CBookEventHandler cbookEventHandler = new CBookEventHandler(this);
	
	static boolean fontOvererving;
	
	public static void zetFontOverervingForm(boolean b)
	{	fontOvererving = b;
	}

	public ReactieVergelijkingVak() {
		super(true);
		remove(formuleVak);
		formuleVak.removeActionListener(this);
	
		this.zetReactieVergelijkingMode();
		
		goedIC = new ImageComponent(WiskOpdr.GOEDKRUL);
		goedIC.setLocation(0, 0);
		goedIC.setVisible(false);
		add(goedIC);

		foutIC = new ImageComponent(WiskOpdr.FOUTKRUIS);
		foutIC.setLocation(0, 0);
		foutIC.setVisible(false);
		add(foutIC);

		halfIC = new ImageComponent(WiskOpdr.HALFKRUL);
		halfIC.setLocation(0, 0);
		halfIC.setVisible(false);
		add(halfIC);
		
		feedbackIC = new ImageComponent(WiskOpdr.loadImage("resources/feedback.gif"));
		feedbackIC.setLocation(0,0);
		feedbackIC.setVisible(false);
		add(feedbackIC);

		int knoppenStartX = 20;

		
		if ("MW".equals(WiskOpdr.deployVariant)) {
			
			formuleVakX = 50;
			formuleVakY = 10;

		}
		if ("GR".equals(WiskOpdr.deployVariant)) {
			
			formuleVakX = 30;
			formuleVakY = 10;
			imageCompX = 4;

		}

		wisKnop = new FormuleButton("wis");
		wisKnop.setBounds(knoppenStartX + 390, 2, 40, 20);
		wisKnop.addActionListener(this);
		wisKnop.setVisible(false);
		zetOpBalk(wisKnop);

		imageComponenten = new ImageComponent[100];
		
		formuleVak = new FormuleVak();
		formuleVak.setFont(formuleVakFont);
		formuleVak.addActionListener(this);
		formuleVak.setLocation(formuleVakX, formuleVakY);
		add(formuleVak);

//		plusKnop = new FormuleButton("plus", FormuleButton.BEWERKINGSKNOP);
//		plusKnop.setBounds(knoppenStartX + 140, 2, 20, 20);
//		plusKnop.addActionListener(this);
//		zetOpBalk(plusKnop);
//		plusKnop.setVisible(false);
//
//		minKnop = new FormuleButton("min", FormuleButton.BEWERKINGSKNOP);
//		minKnop.setBounds(knoppenStartX + 162, 2, 20, 20);
//		minKnop.addActionListener(this);
//		zetOpBalk(minKnop);
//		minKnop.setVisible(false);
//
//		maalKnop = new FormuleButton("maal", FormuleButton.BEWERKINGSKNOP);
//		maalKnop.setBounds(knoppenStartX + 184, 2, 20, 20);
//		maalKnop.addActionListener(this);
//		zetOpBalk(maalKnop);
//		maalKnop.setVisible(false);
//
//		deelKnop = new FormuleButton("deel", FormuleButton.BEWERKINGSKNOP);
//		deelKnop.setBounds(knoppenStartX + 206, 2, 20, 20);
//		deelKnop.addActionListener(this);
//		zetOpBalk(deelKnop);
//		deelKnop.setVisible(false);
//
//		haakjesKnop = new FormuleButton("haakjesweg", FormuleButton.BEWERKINGSKNOP);
//		haakjesKnop.setBounds(knoppenStartX + 236, 2, 20, 20);
//		haakjesKnop.addActionListener(this);
//		zetOpBalk(haakjesKnop);
//		haakjesKnop.setVisible(false);
//
//		herleidKnop = new FormuleButton("herleid", FormuleButton.BEWERKINGSKNOP);
//		herleidKnop.setBounds(knoppenStartX + 258, 2, 20, 20);
//		herleidKnop.addActionListener(this);
//		zetOpBalk(herleidKnop);
//		herleidKnop.setVisible(false);
//
//		ontbindKnop = new FormuleButton("ontbind", FormuleButton.BEWERKINGSKNOP);
//		ontbindKnop.setBounds(knoppenStartX + 280, 2, 20, 20);
//		ontbindKnop.setToolTipText("Ontbind");
//		ontbindKnop.addActionListener(this);
//		zetOpBalk(ontbindKnop);
//		ontbindKnop.setVisible(false);
//
//		splitsKnop = new FormuleButton("splits", FormuleButton.BEWERKINGSKNOP);
//		splitsKnop.setBounds(knoppenStartX + 312, 2, 20, 20);
//		splitsKnop.setToolTipText("Splits");
//		splitsKnop.addActionListener(this);
//		zetOpBalk(splitsKnop);
//		splitsKnop.setVisible(false);
//
//		wortelBewerkKnop = new FormuleButton("wortelbewerk", FormuleButton.BEWERKINGSKNOP);
//		wortelBewerkKnop.setBounds(knoppenStartX + 334, 2, 20, 20);
//		wortelBewerkKnop.setToolTipText("Wortels");
//		wortelBewerkKnop.addActionListener(this);
//		zetOpBalk(wortelBewerkKnop);
//		wortelBewerkKnop.setVisible(false);
//
//		abcKnop = new FormuleButton("abc", FormuleButton.BEWERKINGSKNOP);
//		abcKnop.setBounds(knoppenStartX + 285, 2, 20, 20);
//		abcKnop.addActionListener(this);
//		zetOpBalk(abcKnop);
//		abcKnop.setVisible(false);
//
//		subKnop = new FormuleButton("sub", FormuleButton.BEWERKINGSKNOP);
//		subKnop.setBounds(knoppenStartX + 309, 2, 20, 20);
//		subKnop.addActionListener(this);
//		zetOpBalk(subKnop);
//		subKnop.setVisible(false);
//
//		if ("MW".equals(WiskOpdr.deployVariant)) {
//			plusKnop.setBounds(knoppenStartX + 220, 0, 22, 23);
//			minKnop.setBounds(knoppenStartX + 244, 0, 22, 23);
//			maalKnop.setBounds(knoppenStartX + 268, 0, 22, 23);
//			deelKnop.setBounds(knoppenStartX + 292, 0, 22, 23);
//			haakjesKnop.setBounds(knoppenStartX + 324, 0, 22, 23);
//			herleidKnop.setBounds(knoppenStartX + 348, 0, 22, 23);
//			abcKnop.setBounds(knoppenStartX + 377, 0, 22, 23);
//			subKnop.setBounds(knoppenStartX + 403, 0, 22, 23);
//		}
//
//		if ("GR".equals(WiskOpdr.deployVariant)) {
//			plusKnop.setBounds(knoppenStartX + 150, 0, 22, 23);
//			minKnop.setBounds(knoppenStartX + 174, 0, 22, 23);
//			maalKnop.setBounds(knoppenStartX + 198, 0, 22, 23);
//			deelKnop.setBounds(knoppenStartX + 222, 0, 22, 23);
//			haakjesKnop.setBounds(knoppenStartX + 254, 0, 22, 23);
//			herleidKnop.setBounds(knoppenStartX + 278, 0, 22, 23);
//			abcKnop.setBounds(knoppenStartX + 377, 0, 22, 23);
//			subKnop.setBounds(knoppenStartX + 403, 0, 22, 23);
//
//		}

		feedbackTekst = new TekstArea();
		feedbackTekst.setSize(195, 20);
		feedbackTekst.setBackground(new Color(255, 255, 200));
		if ("MW".equals(WiskOpdr.deployVariant))
			feedbackTekst.setBackground(new Color(250, 255, 220));
		if ("GR".equals(WiskOpdr.deployVariant))
			feedbackTekst.setBackground(new Color(255, 255, 255));
		feedbackTekst.setBorders(true);
		feedbackTekst.setCloseable(true);
		feedbackTekst.addActionListener(this);

		log = new Vector();

		logTextArea = new JTextArea();
		logTextArea.setBounds(0, 0, 650, 320);

		logKnop = new JButton("log");
		logKnop.setBounds(getSize().width - 65, 3, 60, 20);
		logKnop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String text = "";
				for (int i = 0; i < log.size(); i++) {
					text = text + (String) log.elementAt(i) + '\n';
				}
				logTextArea.setText(text);
				logDialog.setVisible(true);
			}
		});

		if (((WiskOpdr) WiskOpdr.applet).reviewMode())
			basisPanel.add(logKnop, BorderLayout.SOUTH);

		logDialog = DialogFacade.newInstance(this, "Log", true);
		logDialog.getContentPane().add(logTextArea);
		logDialog.setSize(logTextArea.getSize());
		logDialog.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
			}
		});

	}

/*	
	public void setLog() {
		setLog(false);
	}

	public void setLog(boolean start) {
		String goedFout = "";
		if (huidigIC == goedIC && huidigIC.isVisible())
			goedFout = "goed";
		if (huidigIC == halfIC && huidigIC.isVisible())
			goedFout = "half";
		if (huidigIC == foutIC && huidigIC.isVisible())
			goedFout = "fout";

		String formule = "";
		String string = formuleVak.toString();
		VergelijkingMeerv v = FormuleParser.parseVergelijking(string);

		int i = 0;
		while (v != null && v.geefVergelijking(i) != null) {
			if (i > 0)
				formule = formule + " of ";

			String formuleDeelLinksSchoon = FormuleParser.schoon(FormuleParser.formuleString("$f" + v.geefVergelijking(i).geefExpLinks().toString() + "@"));
			String formuleDeelRechtsSchoon = FormuleParser.schoon(FormuleParser.formuleString("$f" + v.geefVergelijking(i).geefExpRechts().toString() + "@"));

			formuleDeelLinksSchoon = StringUtils.replaceStr(formuleDeelLinksSchoon, "(0-", "(-");
			formuleDeelRechtsSchoon = StringUtils.replaceStr(formuleDeelRechtsSchoon, "(0-", "(-");

			String formuleDeelLinks = FormuleParser.pel(formuleDeelLinksSchoon);
			String formuleDeelRechts = FormuleParser.pel(formuleDeelRechtsSchoon);
			formule = formule + formuleDeelLinks + " = " + formuleDeelRechts;
			i++;
		}
		if (v == null
				&& (formuleVak.toString().length() > 6 && formuleVak.toString().substring(2, 6).equals("geen") 
				        || formuleVak.toString().length() > 4 && formuleVak.toString().substring(2, 4).equals("no"))) {
			formule = "geen oplossing";
		}
		if (v == null
	            && (formuleVak.toString().length() > 7 && formuleVak.toString().substring(2, 7).equals("alles") 
	                    || formuleVak.toString().length() > 5 && formuleVak.toString().substring(2, 5).equals("all"))) {
	        formule = "alles is een oplossing";
		}

		String fbTekst = "";
		if (feedbackTekst.isVisible() && feedbackTekst.getParent() != null)
			fbTekst = feedbackTekst.getText();
		fbTekst = fbTekst.replace('\n', ' ');
		String s = formule;
		s = s + "   ;   ";
		s = s + new Date().toString();
		s = s + "   ;   ";
		s = s + "Regelnummer = " + 0;
		s = s + "   ;   ";
		if (start)
			s = s + "start";
		else
			s = s + goedFout;
		s = s + "   ;   ";
		s = s + "score = " + score;
		s = s + "   ;   ";

		s = s + fbTekst;

		log.addElement(s);
	}
	*/

	public Component getGoedIC() {
		return goedIC;
	}

	public Component getFoutIC() {
		return foutIC;
	}

	public Component getHalfIC() {
		return halfIC;
	}

	public Component getFeedbackIC()
	{
		return feedbackIC;
	}
	
	public boolean hasFeedback() {
		return hasFeedback;
	}

	public InteractieEditPanel getEditPanel() {
		return new ReactieVergelijkingVakEditPanel(1);
	}

	public void setBounds(int x, int y, int b, int h) { // if(WiskOpdr.deployVariant!=null
														// &&
														// WiskOpdr.deployVariant.equals("GR"))
														// {
														// gelijkwaardigKnop.setBounds(290,2,90,20);
														// terugKnop.setBounds(390,2,80,20);
														// abcKnop.setBounds(230,2,20,20);
														// subKnop.setBounds(260,2,20,20);
														// }
														//
//		if ("MW".equals(WiskOpdr.deployVariant)) {
//			abcKnop.setLocation(b - 114, 0);
//			subKnop.setLocation(b - 90, 0);
//		} else if ("GR".equals(WiskOpdr.deployVariant)) {
//			subKnop.setLocation(b - 66, 0);
//			if (subKnop.isVisible())
//				abcKnop.setLocation(b - 90, 0);
//			else
//				abcKnop.setLocation(b - 66, 0);
//		} else {
//			abcKnop.setLocation(b - 112, 2);
//			subKnop.setLocation(b - 90, 2);
//		}

		logKnop.setBounds(b - 65, h - 55, 60, 21);

		super.setBounds(x, y, b, h);
	}

	public void wis() {
//		for (int i = 0; i < stapNr + 1; i++) {
//			if (i > 0)
//				remove(formuleVakken[i]);
//			if (imageComponenten[i] != null)
//				remove(imageComponenten[i]);
//			if (imageComponentenStap[i] != null)
//				remove(imageComponentenStap[i]);
//			if (i < stapNr)
//				remove(pijlVakken[i]);
//		}
		/*
		 * for(int i=1 ; i<stapNr+1; i++) { remove(formuleVakken[i]);
		 * if(imageComponenten[i]!=null) remove(imageComponenten[i]); } for(int
		 * i=0 ; i<stapNr; i++) { remove(pijlVakken[i]); }
		 */
		if (huidigIC != null) {
			huidigIC.setVisible(false);
			huidigIC.setLocation(0, 0);
		}
		//nodig? 
		formuleVak.setEditable(true);
		
		correct = false;
		fout = false;
		score = 0;
		errorCount = 0;
		if (feedbackTekst != null) {
			remove(feedbackTekst);
		}
		nagekeken = false;
		ingevuld = false;
		zetGeenAntwoord(false);
		zetRandomFout(false);
		log = new Vector();

	}

	public void zetNagekeken(boolean b) {
		if (ingevuld)
			nagekeken = b;
	}

	public Hashtable getEditState()
	{
		return null;
	}

	public void setEditState(Hashtable h) 
	{	
		if(fontOvererving && getParent() instanceof TekstInteractiePanelVak)
		{	Font geerftFont = ((TekstInteractiePanelVak)getParent()).getTekstVak().getFont();
			if (!geerftFont.getName().equals("TimesRoman") && WiskOpdr.formTimes && !WiskOpdr.mac) {
				geerftFont = new Font("TimesRoman", geerftFont.getStyle(), geerftFont.getSize() * 6 / 5);
			}
			formuleVakFont = geerftFont;
			formuleVak.setFont(formuleVakFont);
		}
		if(h.containsKey("hasFeedback"))
			hasFeedback = ((Boolean)h.get("hasFeedback")).booleanValue();
		feedbackIC.setVisible(hasFeedback);
	}

	public void setAnswerModel(int nr) {
		Hashtable h = answerModels[nr];
		if (h == null)
			return;

		String antwoordString = "$f@";
		boolean gelijkwaardig = true;
		boolean vorm = false;
		boolean eindOplossingNodig = false;
		boolean exact = false;
		boolean significant = false;
		int puntenFeedback = 0;
		String feedback = "";
		int feedbackWidth = 200;
		int feedbackHeight = 20;
		String vormString = "$f@";
		int goedHalfFout = 0;

		if (h != null) {
			if (h.containsKey("antwoordString"))
				antwoordString = (String) h.get("antwoordString");
			if (h.containsKey("gelijkwaardig"))
				gelijkwaardig = ((Boolean) h.get("gelijkwaardig")).booleanValue();
			if (h.containsKey("vorm"))
				vorm = ((Boolean) h.get("vorm")).booleanValue();
			if (h.containsKey("eindOplossingNodig"))
				eindOplossingNodig = ((Boolean) h.get("eindOplossingNodig")).booleanValue();
			if (h.containsKey("exact"))
				exact = ((Boolean) h.get("exact")).booleanValue();
			if(h.containsKey("significant")) 
				significant = ((Boolean)h.get("significant")).booleanValue();
			if (h.containsKey("puntenFeedback"))
				puntenFeedback = ((Integer) h.get("puntenFeedback")).intValue();
			if (h.containsKey("feedback"))
				feedback = (String) h.get("feedback");
			if(h.containsKey("feedbackWidth")) 
				feedbackWidth = ((Integer)h.get("feedbackWidth")).intValue();
			if(h.containsKey("feedbackHeight")) 
				feedbackHeight = ((Integer)h.get("feedbackHeight")).intValue();
			if (h.containsKey("vormString"))
				vormString = (String) h.get("vormString");
			if (h.containsKey("goedHalfFout"))
				goedHalfFout = ((Integer) h.get("goedHalfFout")).intValue();

		}
		this.goedHalfFout = goedHalfFout;
		this.puntenFeedback = puntenFeedback;


//		try {
//			antwoordString = FormuleParser.randomizeString(antwoordString, randomVarNamen, randomVarWaarden);
//		} catch (Exception e) {
//			antwoordString = "$f???@";
//			zetGeenAntwoord(true);
//			// antwoordSyntaxFout = true;
//		}

//		try {
//			vormString = FormuleParser.randomizeString(vormString, randomVarNamen, randomVarWaarden);
//		} catch (Exception e) {
//			vormString = "$f???@";
//			zetGeenAntwoord(true);
//			// antwoordSyntaxFout = true;
//		}

//		try {
//			feedback = FormuleParser.randomizeTekstVakString(feedback, randomVarNamen, randomVarWaarden);
//		} catch (Exception e) {
//			feedback = "$f???@";
//			// antwoordSyntaxFout = true;
		//}
		zetJuisteAntwoord(antwoordString);
		//zetJuisteVorm(vormString);
		// if(nr==0) basisAntwoord = juisteAntwoorden[0];

		//this.gekozenAntwoordString = antwoordString;
		this.feedback = feedback;
		if(feedbackSize)
        {	this.feedbackWidth = feedbackWidth;
        	this.feedbackHeight = feedbackHeight;
        }
	}
	
	
	public void zetOpdracht(Hashtable h, String[] randomVars, Hashtable randomValues) {
		if(fontOvererving && getParent() instanceof TekstInteractiePanelVak)
		{	Font geerftFont = ((TekstInteractiePanelVak)getParent()).getTekstVak().getFont();
			if (!geerftFont.getName().equals("TimesRoman") && WiskOpdr.formTimes && !WiskOpdr.mac) {
				geerftFont = new Font("TimesRoman", geerftFont.getStyle(), geerftFont.getSize() * 6 / 5);
			}
			formuleVakFont = geerftFont;
			formuleVak.setFont(formuleVakFont);
		}
		
		randomVarNamen = randomVars;
		randomVarWaarden = randomValues;

		String antwoordString = "$f@";
		int puntenCorrect = 10;
		int puntenMoleculen = 0;
		int puntenElementen = 0;
		int puntenLadingen = 0;
		int aftrekVereenvoudigbaar = 0;
		Hashtable[] answerModels = null;
		boolean hasFeedback = false;
		boolean feedbackSize = false;
		//String vormString = "$f@";
		//int feedbackModus = 0;
		boolean check = true;
		boolean teltMee = true;
		boolean logOption = false;
		String logID = "";
		double eqTestValueMin = 0;
		double eqTestValueMax = 5;
		int scoreMax = 0;
		boolean uitw = false;
		boolean boxMetRand = true;
		boolean[][] logObjectives = null;
		
		if (h.containsKey("antwoordString"))
			antwoordString = (String) h.get("antwoordString");
		if (h.containsKey("puntenCorrect"))
			puntenCorrect = ((Integer) h.get("puntenCorrect")).intValue();
		if (h.containsKey("puntenMoleculen"))
			puntenMoleculen = ((Integer) h.get("puntenMoleculen")).intValue();
		if (h.containsKey("puntenElementen"))
			puntenElementen = ((Integer) h.get("puntenElementen")).intValue();
		if (h.containsKey("puntenLadingen"))
			puntenLadingen = ((Integer) h.get("puntenLadingen")).intValue();
		if (h.containsKey("aftrekVereenvoudigbaar"))
			aftrekVereenvoudigbaar = ((Integer) h.get("aftrekVereenvoudigbaar")).intValue();
		if (h.containsKey("answerModels"))
			answerModels = (Hashtable[]) h.get("answerModels");
		if (h.containsKey("hasFeedback"))
			hasFeedback = ((Boolean) h.get("hasFeedback")).booleanValue();
		if(h.containsKey("feedbackSize")) 
			feedbackSize = ((Boolean)h.get("feedbackSize")).booleanValue();
		
//		if (h.containsKey("vormString"))
//			vormString = (String) h.get("vormString");
		if (h.containsKey("check"))
			check = ((Boolean) h.get("check")).booleanValue();
		if (h.containsKey("teltMee"))
			teltMee = ((Boolean) h.get("teltMee")).booleanValue();
		if (h.containsKey("logOption"))
			logOption = ((Boolean) h.get("logOption")).booleanValue();
		if (h.containsKey("logID"))
			logID = (String) h.get("logID");

		if (h.containsKey("eqTestValueMin"))
			eqTestValueMin = ((Double) h.get("eqTestValueMin")).doubleValue();
		if (h.containsKey("eqTestValueMax"))
			eqTestValueMax = ((Double) h.get("eqTestValueMax")).doubleValue();
		if (h.containsKey("scoreMax"))
			scoreMax = ((Integer) h.get("scoreMax")).intValue();
		if (h.containsKey("uitw"))
			uitw = ((Boolean) h.get("uitw")).booleanValue();
		if (h.containsKey("boxMetRand"))
			boxMetRand = ((Boolean) h.get("boxMetRand")).booleanValue();
		if(h.containsKey("logObjectives")) 
			logObjectives = (boolean[][])h.get("logObjectives");
		
		this.check = check;
		this.teltMee = teltMee;
		this.logOption = logOption;
		this.logID = logID;
		this.eqTestValueMin = eqTestValueMin;
		this.eqTestValueMax = eqTestValueMax;

		this.puntenCorrect = puntenCorrect;
		this.puntenMoleculen = puntenMoleculen;
		this.puntenElementen = puntenElementen;
		this.puntenLadingen = puntenLadingen;
		this.aftrekVereenvoudigbaar = aftrekVereenvoudigbaar;
		this.logObjectives = logObjectives;

//		try {
//			antwoordString = FormuleParser.randomizeString(antwoordString, randomVars, randomValues);
//		} catch (Exception e) {
//			antwoordString = "$f???@";
//			zetGeenAntwoord(true);
//		}
		zetJuisteAntwoord(antwoordString);


//		try {
//			vormString = FormuleParser.randomizeString(vormString, randomVars, randomValues);
//		} catch (Exception e) {
//			vormString = "$f???@";
//			zetGeenAntwoord(true);
//			// antwoordSyntaxFout = true;
//		}
//		zetJuisteVorm(vormString);

//		try {
//			startString = FormuleParser.randomizeString(startString, randomVars, randomValues);
//		} catch (Exception e) {
//			startString = "$f???@";
//		}

		this.answerModels = answerModels;
		this.hasFeedback = hasFeedback;
		this.feedbackSize = feedbackSize;

		this.scoreMax = scoreMax;
		this.uitw = uitw;
		
		zetMetRand(boxMetRand);
		feedbackIC.setVisible(false);
	}

	public void setState(Hashtable h) {
		if (h == null)
			return;
		int stapNr = 0;
		boolean ingevuld = false;
		boolean nagekeken = false;
		String substitutieString = "";
		String antwoordString = null;
		Vector log = new Vector();
		int attemptsCount = 0;
		int errorCount = 0;
		String[] gebruikersSubStrings = null;
		int ideasPuntenAftrek = 0;

		if (h.containsKey("stapNr"))
			stapNr = ((Number) h.get("stapNr")).intValue();
		if (h.containsKey("ingevuld"))
			ingevuld = ((Boolean) h.get("ingevuld")).booleanValue();
		if (h.containsKey("nagekeken"))
			nagekeken = ((Boolean) h.get("nagekeken")).booleanValue();
		if (h.containsKey("substitutieString"))
			substitutieString = (String) h.get("substitutieString");
		if (h.containsKey("antwoordString"))
			antwoordString = (String) h.get("antwoordString");
		if (h.containsKey("log"))
			log = OpdrNavStruct.toVector(h.get("log"));
		if (h.containsKey("attemptsCount"))
			attemptsCount = ((Number) h.get("attemptsCount")).intValue();
		if (h.containsKey("errorCount"))
			errorCount = ((Number) h.get("errorCount")).intValue();
		if (h.containsKey("gebruikersSubStrings"))
			gebruikersSubStrings = OpdrNavStruct.toStringArray(h.get("gebruikersSubStrings"));
		if (h.containsKey("ideasPuntenAftrek"))
			ideasPuntenAftrek = ((Number) h.get("ideasPuntenAftrek")).intValue();

		int y = formuleVakY;
		formuleVak.setFont(formuleVakFont);
		int x = formuleVakX;
		formuleVak.setLocation(x, y);
		formuleVak.addActionListener(this);//nodig?
		add(formuleVak);//nodig?
		formuleVak.vulVak(antwoordString);

		this.ingevuld = ingevuld;
		this.nagekeken = nagekeken;

		//this.gekozenAntwoordString = gewensteAntwoordString;
		this.formuleVakString = antwoordString;
		//this.gekozenStartString = gekozenStartString;
		//this.aftrekTipHulp = aftrekTipHulp;
		this.log = log;
		this.attemptsCount = attemptsCount;
		this.errorCount = errorCount;
		
		if (ingevuld)
			vulVak(formuleVakString);
		//zetJuisteAntwoord(gekozenAntwoordString);

		
		if ((mode == 0 || nagekeken)) {
			kijkNa();
			
		}
		
	}

	public Hashtable getState() {
		int stapNr = 0;
		boolean ingevuld = true;
		boolean nagekeken = false;
		String antwoordString = null;
		Vector log = new Vector();
		int attemptsCount = 0;
		int errorCount = 0;
		String[] gebruikersSubStrings;
		
		ingevuld = this.ingevuld;
		if (!ingevuld && mode != 1 && mode != 2 && mode != 3) {
			try {
				formuleVak.finish();
				
			} catch (Exception e) {
			}
		}

		ingevuld = this.ingevuld;
		nagekeken = this.nagekeken;
		attemptsCount = this.attemptsCount;
		errorCount = this.errorCount;
		
		//gewensteAntwoordString = this.gekozenAntwoordString;
		//startString = this.gekozenStartString;
		antwoordString = toString();

		//aftrekTipHulp = this.aftrekTipHulp;
		log = this.log;

		if(!("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant))) kijkNa(false);
		/* Nog beter naar kijken:
		if (logOption) {

			Hashtable logMap = new Hashtable();

			String formule = "";
			String string = formuleVakInhouden[stapNr];
			VergelijkingMeerv v = FormuleParser.parseVergelijking(string);

			int i = 0;
			while (v != null && v.geefVergelijking(i) != null)
			{
				if (i > 0)
					formule = formule + " of ";

				String formuleDeelLinksSchoon = FormuleParser.schoon(FormuleParser.formuleString("$f" + v.geefVergelijking(i).geefExpLinks().toString() + "@"));
				String formuleDeelRechtsSchoon = FormuleParser.schoon(FormuleParser
						.formuleString("$f" + v.geefVergelijking(i).geefExpRechts().toString() + "@"));

				formuleDeelLinksSchoon = StringUtils.replaceStr(formuleDeelLinksSchoon, "(0-", "(-");
				formuleDeelRechtsSchoon = StringUtils.replaceStr(formuleDeelRechtsSchoon, "(0-", "(-");

				String formuleDeelLinks = FormuleParser.pel(formuleDeelLinksSchoon);
				String formuleDeelRechts = FormuleParser.pel(formuleDeelRechtsSchoon);
				formule = formule + formuleDeelLinks + " = " + formuleDeelRechts;
				i++;
			}
			if (v == null
					&& (formuleVak.toString().length() > 6 && formuleVak.toString().substring(2, 6).equals("geen") || formuleVak.toString().length() > 4
							&& formuleVak.toString().substring(2, 4).equals("no"))) {
				formule = "geen oplossing";
			}
			if (v == null
	                && (formuleVak.toString().length() > 7 && formuleVak.toString().substring(2, 7).equals("alles") 
	                        || formuleVak.toString().length() > 5 && formuleVak.toString().substring(2, 5).equals("all"))) {
	            formule = "alles is een oplossing";
	        }
			logMap.put("logAnswer", formule);

			logMap.put("logScore", new Integer(score));
			logMap.put("logMaxScore", new Integer(scoreMax));
			logMap.put("logErrorCount", new Integer(errorCount));
			logMap.put("logAttemptsCount", new Integer(attemptsCount));
			logMap.put("logAttempts", log);

			WiskOpdr.setLog(logID, logMap);
		}
		*/

		Hashtable h = new Hashtable();
		h.put("stapNr", new Integer(stapNr));
		h.put("ingevuld", new Boolean(ingevuld));
		h.put("nagekeken", new Boolean(nagekeken));
		h.put("antwoordString", antwoordString);
		h.put("log", log);
		h.put("attemptsCount", new Integer(attemptsCount));
		h.put("errorCount", new Integer(errorCount));
		

		return h;
	}

	public void zetMaat()
	{

	}

	public int geefAsHoogte()
	{
		return 0;
	}

		
	public void vulVak(String s)
	{
		formuleVak.vulVak(s);
	}

	public String toString()
	{
		return formuleVak.toString();
	}

	private void zetGoedFout(int uitslag)
	{
		if (!check)
			return;
		if (huidigIC != null)
		{
			huidigIC.setVisible(false);
			huidigIC.setLocation(0, 0);
		}
		if (uitslag == GEEN)
			return;

		if (uitslag == GOED)
			huidigIC = goedIC;
		else if (uitslag == FOUT)
			huidigIC = foutIC;
		else if (uitslag == HALF)
			huidigIC = halfIC;
		int y = formuleVak.getLocation().y + formuleVak.getSize().height - 5;
		if ("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant))
			y = formuleVak.getLocation().y + formuleVak.getSize().height / 2 - 15;
		huidigIC.setLocation(imageCompX, y);
		huidigIC.setVisible(true);

	}


	public void zetMode(int mode)
	{
		this.mode = mode;
	}

	public void zetJuisteAntwoord(String s)
	{
		int index = s.indexOf(";");
		if (index > -1)
		{
			s = "$f" + s.substring(index + 1);
		}
		gewensteEindOplossing = ReactieParser.parseVergelijking(s);
	}

	public void stop()
	{
		if (mode == 1)
			return;
		checkAntwoord();
		kijkNa();
		if (ingevuld)
			produceAction("changed");

	}

	public void start()
	{
		formuleVak.requestFocus();
		Container parent = getParent();
		if (parent == null)
			return;
		int x = getLocation().x + parent.getLocation().x;
		int y = getLocation().y + parent.getLocation().y;
		int h = parent.getSize().height;
		for (int i = 0; parent != null && i < 40; i++)
		{
			if (parent instanceof TabletOwner)
			{
				((TabletOwner) parent).zetTablet(this, x + 20, y + 20);

				break;
			}
			else
			{
				parent = parent.getParent();
				if (parent == null)
					return;
				x += parent.getLocation().x;
				y += parent.getLocation().y;
			}
		}

	}

	public void opnieuw()
	{
		score = 0;
		correct = false;
		for (int i = 0; i < 100; i++)
		{
			if (imageComponenten[i] != null)
				remove(imageComponenten[i]);
		}
		formuleVak = new FormuleVak();
		formuleVak.setFont(formuleVakFont);
		formuleVak.addActionListener(this);
		formuleVak.setLocation(formuleVakX, formuleVakY);
		add(formuleVak);
	}

	public void kijkNa()
	{
		if (mode == 0 || mode == 1)
		{
			kijkNa(-1);
			if (ingevuld)
				produceAction("changed");
		}
		if (mode == 2 || mode == 3)
		{
			checkAntwoord();
			kijkNa(0); //nodig?
			
			if (ingevuld)
				produceAction("changed");
		}
	}

	public TekstArea getFeedbackComponent()
	{
		return feedbackTekst;
	}

	public void setFeedback(String tekst, boolean closeable)
	{
		if (tekst.trim().equals(""))
			return;
		feedbackTekst.setText("");
		feedbackTekst.setCloseable(closeable);
		feedbackTekst.setSize(195, 20);
		feedbackTekst.setLocation(getSize().width - 250, formuleVak.getLocation().y + formuleVak.getSize().height + 10);
		feedbackTekst.setText(tekst);
		feedbackTekst.resize();
		
		add(feedbackTekst, 0);
		produceAction("feedback");
	}

	public void kijkNa(int stapNr)
	{
		kijkNa(true);
	}

	public void kijkNa(boolean show)
	{
		if (hasFeedback)
			checkAntwoordFeedback(show);
		else
			checkAntwoord();
		if (!ingevuld)
		{
			if (show)
				zetGoedFout(GEEN);
			if (formuleVak.toString().equals("$f@") && show)
			{
//				if ("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant))
//					remove(mwFeedbackPanel);
//				else
					remove(feedbackTekst);
				produceAction("feedbackWeg");
			}
			return;
		}
		else if (hasFeedback)
		{
			if (goedHalfFout == 0)
			{
				if (show)
					zetGoedFout(GOED);
				score = puntenFeedback;
				correct = true;
				fout = false;
				
			}
			else if (goedHalfFout == 1)
			{
				if (show)
					zetGoedFout(HALF);
				score = puntenFeedback;
				correct = false;
				fout = false;
			}
			else if (goedHalfFout == 2)
			{
				if (show)
					zetGoedFout(HALF);
				score = puntenFeedback;
				correct = false;
				fout = false;
			}
			else if (goedHalfFout == 3)
			{ 	if (show)
					zetGoedFout(FOUT);
				score = puntenFeedback;
				correct = false;
				fout = true;
			}
		}
		else if (isGelijkwaardig)
		{
		
			score = puntenCorrect;
			correct = true;
			fout = false;
			if(show)
				zetGoedFout(GOED);
			// zetCorrectFoutStap(stapNr,true,false,false,"feedbackTekst16");//"Dit is een correcte vergelijking"

			
		}
		else if(!isGelijkwaardigMoleculen)
		{
			score = 0;
			correct = false;
			fout = true;
			if(show)
			{	setFeedback(WiskOpdr.rb.getString("feedbackReactieVerg01"), true);
				zetGoedFout(FOUT);
			}
		}
		else if(!elementenBalansKlopt)
		{
			score = puntenMoleculen;
			correct = false;
			fout = false;
			if(show)
			{
				setFeedback(WiskOpdr.rb.getString("feedbackReactieVerg02"), true);
				zetGoedFout(HALF);
			}
		}
		else if(!isGelijkwaardigMoleculenLading)
		{
			score = puntenElementen;
			correct = false;
			fout = false;
			if(show)
			{
				setFeedback(WiskOpdr.rb.getString("feedbackReactieVerg03"), true);
				zetGoedFout(HALF);
			}
		}
		else if(!ladingenBalansKlopt)
		{
			score = puntenElementen;
			correct = false;
			fout = false;
			if(show)
			{
				setFeedback(WiskOpdr.rb.getString("feedbackReactieVerg04"), true);
				zetGoedFout(HALF);
			}
		}
		else if(!pijlKlopt)
		{
			score = puntenLadingen;
			correct = false;
			fout = false;
			if(show)
			{
				setFeedback(WiskOpdr.rb.getString("feedbackReactieVerg05"), true);
				zetGoedFout(HALF);
			}
		}
		else if(kanVereenvoudigd)
		{
			score = puntenCorrect - aftrekVereenvoudigbaar;
			correct = false;
			fout = false;
			if(show)
			{
				setFeedback(WiskOpdr.rb.getString("feedbackReactieVerg06"), true);
				zetGoedFout(HALF);
			}
		}
		else
		{
			score = 0;
			correct = false;
			fout = true;
			if(show)
			{
				setFeedback(WiskOpdr.rb.getString("feedbackReactieVerg07"), true);
				zetGoedFout(FOUT);
			}
		}
		
//		else
//		{
//			score = 0;
//			correct = false;
//			fout = true;
//			if(show)
//				zetGoedFout(FOUT);
//		}

		

	}

	public void checkAntwoordFeedback()
	{
		checkAntwoordFeedback(true);
	}

	public void checkAntwoordFeedback(boolean show)
	{
		int aantalAnswerModels = answerModels.length;
		for (int h = 0; h < aantalAnswerModels; h++)
		{
			setAnswerModel(h);

			checkAntwoord();

//			boolean pastGelijkwaardig = isGelijkwaardig && !bevatFouteOplossing;
//			boolean pastVorm = isJuisteVorm;
//			boolean pastEindAntwoord = isEindOplossing;
//			boolean pastExact = isEindOplossingExact;
//			boolean pastSignificant = isEindOplossingSignificant;

//			if (!gelijkwaardigP)
//				pastGelijkwaardig = true;
//			if (!vormP)
//				pastVorm = true;
//			if (!eindOplossingNodigP)
//				pastEindAntwoord = true;
//			if (!exactP)
//				pastExact = true;
//			if (!significantP)
//				pastSignificant = true;

			boolean answerModelFits = isGelijkwaardig;
			if (answerModelFits)
			{
				if (!feedback.trim().equals("") && show)
					setFeedback(feedback, true);
				else if (getParent() == null && feedbackTekst.getParent() != null)
				{
					produceAction("feedbackWeg");
				}
				break;
			}
		}
	}

	public void checkAntwoord()
	{
		
		if (gewensteEindOplossing == null)
		{	System.out.println("gewensteEindoplossing null");
			return;
		}

		//Algebra.setTestValues(eqTestValueMin, eqTestValueMax);
		ingevuld = false;
		
//		if ("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant))
//			remove(mwFeedbackPanel);
//		else
		remove(feedbackTekst);
		String formuleVakString = formuleVak.toString();
		if(!formuleVakString.equals("$f@"))
			ingevuld = true;
		
		ReactieVergelijking antwoordIngevuld = ReactieParser.parseVergelijking(formuleVakString);
		//VergelijkingMeerv antwoordIngevuld = FormuleParser.parseVergelijking(formuleVakString);
		
		
//		if (antwoord == null)
//		{
//			antwoord = antwoordGeen;
//			antwoordIngevuld = antwoordGeen;
//		}
//		if (antwoord == null)
//		{
//			antwoord = antwoordAlles;
//			antwoordIngevuld = antwoordAlles;
//		}
		isGelijkwaardig = false;
		isGelijkwaardigMoleculen = false;
		isGelijkwaardigMoleculenLading = false;
		elementenBalansKlopt = false;
		ladingenBalansKlopt = false;
		pijlKlopt = false;
		kanVereenvoudigd = false;
		if(antwoordIngevuld != null)
		{
			isGelijkwaardig = antwoordIngevuld.isGelijkwaardig(gewensteEindOplossing);
			if(isGelijkwaardig)
			{
				isGelijkwaardigMoleculen = true;
				isGelijkwaardigMoleculenLading = true;
			}
			else
			{
				isGelijkwaardigMoleculen = antwoordIngevuld.isGelijkwaardigMoleculen(gewensteEindOplossing);
				isGelijkwaardigMoleculenLading = antwoordIngevuld.isGelijkwaardigMoleculenLading(gewensteEindOplossing);
	
			}
			elementenBalansKlopt = antwoordIngevuld.elementenBalansKlopt();
			ladingenBalansKlopt = antwoordIngevuld.ladingenBalansKlopt();
			pijlKlopt = antwoordIngevuld.isGelijkwaardigPijl(gewensteEindOplossing);
			if(pijlKlopt && ladingenBalansKlopt && elementenBalansKlopt && isGelijkwaardigMoleculenLading)
				kanVereenvoudigd = antwoordIngevuld.kanVereenvoudigd(gewensteEindOplossing);
		}
		
		repaint();

	
		
		
	}

	public int getIpId()
	{
		return 0;
	}

	public String getIpExpString()
	{
		return null;
	}

	public int getScore()
	{
		if (!teltMee)
			return 0;
		return score;
	}

	public int[][] getScoreObjectives()
	{
		if (logObjectives == null)
			return null;
		int[][] scoreObjectives = new int[logObjectives.length][];
		for (int i = 0; i < logObjectives.length; i++)
			scoreObjectives[i] = new int[logObjectives[i].length];
		for (int i = 0; i < logObjectives.length; i++)
			for (int j = 0; j < logObjectives[i].length; j++)
			{
				if (logObjectives[i][j])
					scoreObjectives[i][j] = score;
			}
		return scoreObjectives;
	}

	public int getScoreMax()
	{
		if (!teltMee)
			return 0;
		return scoreMax;
	}

	public boolean isCorrect()
	{
		if (!teltMee)
			return true;
		return correct;
	}

	public boolean isFout()
	{
		if (!teltMee)
			return false;
		return fout;
	}

	public void actionPerformed(ActionEvent e)
	{
		super.actionPerformed(e);

		if (e.getSource() == formuleVak && e.getActionCommand().equals("ingevuld"))
		{
			if (mode == 0 || mode == 1)
			{
				kijkNa();
				if (!formuleVak.toString().equals("$f@"))
					zetNagekeken(true);
				if (fout)
					errorCount++;
				attemptsCount++;
			}
			else
			{
				try
				{
					ReactieVergelijking antwoordIngevuld = formuleVak.geefReactieVergelijking();
					if (antwoordIngevuld != null)
					{
						String vergString = antwoordIngevuld.toString();
						formuleVak.vulVak("$f" + vergString + "@");
						requestFocus();
					}
				}
				catch (Exception exc)
				{
				}
			}

			if (!formuleVak.toString().equals("$f@"))
			{
				//setLog();
			}
			if ((mode == 0 || mode == 1) && ingevuld)
			{
				produceAction("checked");
			}

			if ((mode == 2 || mode == 3) && !formuleVak.toString().equals("$f@"))
			{
				ReactieVergelijking antwoordIngevuld = formuleVak.geefReactieVergelijking();
				if (antwoordIngevuld == null)
				{
					setFeedback(WiskOpdr.rb.getString("feedbackTekst09"), true);
				}
				
			}
		}

		else if (e.getSource() == formuleVak && e.getActionCommand().equals("formChanged"))
		{ // if(feedbackTekst!=null)
			// remove(feedbackTekst);
			if (feedbackTekst != null && getParent() == null && feedbackTekst.getParent() != null)
			{
				remove(feedbackTekst);
				produceAction("feedbackWeg");
			}
			zetGoedFout(GEEN);
			if (mode == 2 || mode == 3)
			{
				zetGoedFout(GEEN);
				produceAction("feedbackWeg");
			}
		}
		else if (e.getSource() instanceof FormuleVak && e.getActionCommand().equals("focus"))
		{
			FormuleVak fv = (FormuleVak) e.getSource();
			if (fv != formuleVak)
				formuleVak = fv;
		}
		else if (e.getSource() == feedbackTekst)
		{
			removeSoft(feedbackTekst);
			if (uitw || getParent() == null && feedbackTekst.getParent() != null)
			{
				produceAction("closeFeedback");
			}
			formuleVak.requestFocus();
		}
		else if (e.getSource() == feedbackCloseButton)
		{
			if (uitw || getParent() == null && feedbackTekst.getParent() != null)
			{
				produceAction("closeFeedback");
			}
			formuleVak.requestFocus();
		}
	}

	public String formatRuleText(RuleIF rule, String text)
	{
		String argument = rule.getArgument();
		String argument1 = null;
		String argument2 = null;

		if (argument != null)
		{
			int indexAnd = argument.indexOf("\u2227");
			if (indexAnd > -1)
			{
				argument1 = argument.substring(0, indexAnd);
				argument2 = argument.substring(indexAnd);

				int index = argument1.indexOf("=");
				if (index > -1)
					argument1 = argument1.substring(index + 1);
				argument1 = "$f" + argument1 + "@";

				index = argument2.indexOf("=");
				if (index > -1)
					argument2 = argument2.substring(index + 1);
				argument2 = "$f" + argument2 + "@";
			}

			else
			{
				int index = argument.indexOf("=");
				if (index > -1)
					argument = argument.substring(index + 1);
				argument = "$f" + argument + "@";
			}
		}
		if (text.indexOf("{1}") > -1)
			text = StringUtils.replaceStr(text, "{1}", argument1);
		if (text.indexOf("{2}") > -1)
			text = StringUtils.replaceStr(text, "{2}", argument2);
		if (text.indexOf("{?}") > -1 && argument1 != null)
			text = StringUtils.replaceStr(text, "{?}", argument1);
		else if (text.indexOf("{?}") > -1)
			text = StringUtils.replaceStr(text, "{?}", argument);
		return text;
	}

	
	public void verplaatsFocus()
	{
		Container parent = getParent();
		for (int i = 0; parent != null && i < 40; i++)
		{
			if (parent instanceof TekstVakPanel)
			{
				((TekstVakPanel) parent).verplaatsFocus();
				break;
			}
			else if (parent instanceof MyOpdrContainer)
			{
				((MyOpdrContainer) parent).verplaatsFocus();
				break;
			}
			else
			{
				parent = parent.getParent();
			}
		}
	}

	// ActionProducer
	private ActionListener actionListener = null;

	public void addActionListener(ActionListener l)
	{
		actionListener = AWTEventMulticaster.add(actionListener, l);
	}

	public void removeActionListener(ActionListener l)
	{
		actionListener = AWTEventMulticaster.remove(actionListener, l);
	}

	public void produceAction(String command)
	{
		if (actionListener != null)
		{
			actionListener.actionPerformed(new ActionEvent(this, 0, command));
		}
	}
	//
	
	

	@Override
	public void acceptCBookEvent(CBookEvent event) {
		//String command = event.getCommand();
//		if(command.equals("balansvergelijking"))
//		{	String vergelijkingString = (String)event.getParameter("balansvergelijking");
//			zetBalansVergelijking(FormuleParser.parseVergelijking("$f" + vergelijkingString + "@"));
//		}
		
		
	}

	@Override
	public void addCBookEventListener(CBookEventListener listener,
			String command) {
		System.out.println("addCBookEventListener: "+listener.toString() +"+"+command);
		cbookEventHandler.addCBookEventListener(listener, command);
		
	}

	@Override
	public void removeCBookEventListener(CBookEventListener listener,
			String command) {
		cbookEventHandler.removeCBookEventListener(listener, command);
		
	}

	@Override
	public String[] getSendCmds() {
		String[] commands = {"balansvergelijking", "double.solution", "equation"};
		return commands;
	}

	@Override
	public String[] getAcceptedCmds() {
		String[] commands = {"balansvergelijking", "maakStap", "equation"};
		return commands;
	}

	@Override
	public String getLocalizedCmd(String cmd) {
		return WiskOpdr.rb.getString(CBA_PREFIX + cmd);
	}

}
