package fi.wiskopdr;

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
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
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
import fi.wiskopdr.expressies.Algebra;
import fi.wiskopdr.expressies.BasisExpressie;
import fi.wiskopdr.expressies.Expressie;
//import fi.wiskopdr.expressies.Functie;
//import fi.wiskopdr.expressies.FunctieDefSet;
import fi.wiskopdr.expressies.FunctieMV;
import fi.wiskopdr.expressies.FunctieMVDefSet;
import fi.wiskopdr.expressies.Vergelijking;
import fi.wiskopdr.expressies.VergelijkingMeerv;
import fi.wiskopdr.formuleobjects.FormuleButton;
import fi.wiskopdr.formuleobjects.FormuleEditor;
import fi.wiskopdr.formuleobjects.FormuleParser;
import fi.wiskopdr.formuleobjects.FormuleVak;
import fi.wiskopdr.formuleobjects.TabletOwner;
import fi.wiskopdr.opdrnav.MyOpdrContainer;
import fi.wiskopdr.opdrnav.OpdrNavStruct;
import fi.wiskopdr.stelselsvergelijkingen.StelselEditor;
import fi.wiskopdr.tekstobjects.FeedbackTekstArea;
import fi.wiskopdr.tekstobjects.TekstArea;
import fi.wiskopdr.tekstobjects.TekstInteractiePanelVak;

public class AntwoordVergelijkingVak extends AntwoordVak implements InteractiePanel, CBookAware {
	protected static int GOED = 1;
	protected static int FOUT = 0;
	protected static int HALF = 2;
	protected static int GEEN = 3;

	private FormuleButton plusKnop, minKnop, maalKnop, deelKnop, haakjesKnop, herleidKnop, abcKnop, subKnop;
	private FormuleButton ontbindKnop, splitsKnop, wortelBewerkKnop;
	private FormuleButton gelijkwaardigKnop, terugKnop, tip1Knop, tip2Knop, hulpKnop, solveKnop;

	private boolean ingevuld = false;
	boolean nagekeken = false;

	private boolean vorm;
	private boolean eindOplossingNodig;
	private boolean exactNodig;
	private boolean	significantNodig;
	private boolean isDeelOplossing = false;
	private boolean isGelijkwaardig = false;
	private boolean isEindOplossing = false;
	private boolean isEindOplossingExact = false;
	private boolean isEindOplossingSignificant = false;
	private boolean isJuisteVorm = false;

	private boolean bevatFouteOplossing = false;
	private boolean bevatVoldoetNiet = false;
	private boolean moetNogAfgerond = false;
	private boolean moetNogOngelijkheid = false;
	private boolean hasStartString = false;

	private int puntenGelijkwaardig = 0;
	private int puntenVorm = 0;
	private int puntenEindOplossing = 10;
	private int puntenExact = 0;
	private int puntenSignificant = 0;

	private int score;
	private int scoreMax;
	private boolean correct;
	private boolean fout;
	private int errorCount;
	private int attemptsCount;
	private boolean stapOk;

	private ImageComponent goedIC, foutIC, halfIC;
	private ImageComponent huidigIC;
	private ImageComponent feedbackIC;
	private VergelijkingMeerv gewensteEindOplossing;
	private VergelijkingMeerv gewensteTussenOplossing;

	private VergelijkingMeerv[] juisteVormen;

	private PijlVak[] pijlVakken;
	private PijlVak pijlVak;
	private int pijlX = "GR".equals(WiskOpdr.deployVariant) ? 105 : 130;
	protected FormuleVak[] formuleVakken;
	private int stapNr;

	private ImageComponent[] imageComponenten;
	private ImageComponent[] imageComponentenStap;

	private boolean stappen;
	protected int mode = 0;

	private int formuleVakX = 30;
	private int formuleVakY = 10;

	// even op protected ipv private zetten om te proberen:
	protected int stapH = 25;
	private int imageCompX = 5;
	private Font formuleVakFont = (!WiskOpdr.formTimes || WiskOpdr.mac) ? WiskOpdr.formuleFont0Mac : WiskOpdr.formuleFont0; 
	
	private Expressie substitutie;
	private TekstArea feedbackTekst;
	private boolean geenOplossing;
	
	//private String gekozenAntwoordString, gekozenStartString; 
	private String formuleVakString;

	private Hashtable[] answerModels;
	private String[] randomVarNamen;
	private Hashtable randomVarWaarden;
	
	private String feedback;
	private boolean feedbackSize;
	private int feedbackWidth = 200;
	private int feedbackHeight = 20;
	private boolean exactP;
	private boolean significantP;
	private boolean vormP;
	private boolean eindOplossingNodigP;
	private boolean gelijkwaardigP;
	private boolean hasFeedback;
	private int puntenFeedback;
	private int goedHalfFout;

	private boolean tips; // ideas aan
	private String strategieDomein;
	private int foutenTeller;
	//private int feedbackModus;
	//private boolean tipGebruikt;
	//private boolean hulpGebruikt;
	//private int aftrekTipHulp;

	//private FunctieDefSet functieDefSet = new FunctieDefSet();
	private FunctieMVDefSet functieMVDefSet = new FunctieMVDefSet();
	
	private Vergelijking[] antwoordSubstituties;
	private String[] antwoordStringSubstituties;
	private Vergelijking[] gebruikersSubstituties;
	private FormuleEditor gebruikersSubstitutiesVak;
	private boolean pijl = true;
	private boolean subKnopExtra;

	private Vector log;
	private JButton logKnop;
	private DialogFacade logDialog;
	private JTextArea logTextArea;

	private boolean linStrategieVersie = false;
	private boolean linOefenVersie = false;

	private boolean check;
	private boolean teltMee;

	private boolean logOption;
	private String logID;
	
	private boolean[][] logObjectives;

	private boolean tipOpBalk = true;
	private boolean hulpOpBalk = false;
	private boolean stapOpBalk = false;
	private boolean solveOpBalk = false;
	private boolean meerTips = false;
	private boolean tipBijFout = false;
	private boolean feedbackBijFout = false;
	private boolean hulpBijTip = false;
	
	private int aftrekTip = 0;
	private int aftrekHulp = 0;
	private int aftrekStap = 0;
	private int aftrekSolve = 0;
	
	private boolean diagnose = false;
	
	private int ideasPuntenAftrek;

	private Hashtable changedTexts = new Hashtable();

	private double eqTestValueMin = 0;
	private double eqTestValueMax = 5;

	private Image feedbackBallonImage;
	private JPanel mwFeedbackPanel;
	private JButton feedbackCloseButton;
	private boolean bordjesMethode = false;
	private boolean stepsForLinKwad = false;

	private FormuleVak formuleVakSimpel;
	private boolean uitw = false;
	private boolean casAntw = false;
	private boolean casCheck = false;
	private FormuleButton wisKnop;
	
	private VergelijkingMeerv huidigeVergelijking;
	private boolean balansKoppeling = false;
	
	private CBookEventHandler cbookEventHandler = new CBookEventHandler(this);
	
	static boolean fontOvererving;
	
	public static void zetFontOverervingForm(boolean b)
	{	fontOvererving = b;
	}

	
	// verwijder het "log" knopje tijdens het printen.
	@Override
	public void print(Graphics g) {
		boolean vis = false;
		if(logKnop != null && logKnop.isVisible()) {
			logKnop.setVisible(false);
			vis = true;
			basisPanel.doLayout();
		}
		super.print(g);
		if(logKnop != null && vis) {
			logKnop.setVisible(true);
			basisPanel.doLayout(); // restore layout
		}
	}



	public AntwoordVergelijkingVak() {
		super(true);
		remove(formuleVak);
		formuleVak.removeActionListener(this);

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

		gelijkwaardigKnop = new FormuleButton("gelijkwaardig", FormuleButton.NAVIGATIEKNOP);
		gelijkwaardigKnop.setBounds(knoppenStartX + 390, 2, 20, 20);
		gelijkwaardigKnop.addActionListener(this);
		zetOpBalk(gelijkwaardigKnop);

		terugKnop = new FormuleButton("terug", FormuleButton.NAVIGATIEKNOP);
		terugKnop.setBounds(knoppenStartX + 412, 2, 20, 20);
		terugKnop.addActionListener(this);
		zetOpBalk(terugKnop);

		if ("MW".equals(WiskOpdr.deployVariant)) {
			gelijkwaardigKnop.setBounds(knoppenStartX + 390, 2, 21, 23);
			terugKnop.setBounds(knoppenStartX + 412, 2, 21, 23);

			formuleVakX = 50;
			formuleVakY = 10;
			stapH = 25;

		}
		if ("GR".equals(WiskOpdr.deployVariant)) {
			gelijkwaardigKnop.setBounds(knoppenStartX + 390, 2, 16, 23);
			terugKnop.setBounds(knoppenStartX + 412, 2, 16, 23);

			formuleVakX = 30;
			formuleVakY = 10;
			stapH = 25;
			imageCompX = 4;

		}

		tip1Knop = new FormuleButton(WiskOpdr.rb.getString("ideasTip"));
		tip1Knop.setBounds(knoppenStartX + 365, 2, 30, 20);
		tip1Knop.addActionListener(this);
		tip1Knop.setVisible(false);
		zetOpBalk(tip1Knop);

		tip2Knop = new FormuleButton(WiskOpdr.rb.getString("ideasHelp"));
		tip2Knop.setBounds(knoppenStartX + 365, 2, 30, 20);
		tip2Knop.addActionListener(this);
		tip2Knop.setVisible(false);
		zetOpBalk(tip2Knop);

		hulpKnop = new FormuleButton(WiskOpdr.rb.getString("ideasStap"));
		hulpKnop.setBounds(knoppenStartX + 343, 2, 30, 20);
		hulpKnop.addActionListener(this);
		hulpKnop.setVisible(false);
		zetOpBalk(hulpKnop);

		solveKnop = new FormuleButton(WiskOpdr.rb.getString("ideasLosop"));
		solveKnop.setBounds(knoppenStartX + 343, 2, 40, 20);
		solveKnop.addActionListener(this);
		solveKnop.setVisible(false);
		zetOpBalk(solveKnop);
		
		wisKnop = new FormuleButton("wis");
		wisKnop.setBounds(knoppenStartX + 390, 2, 40, 20);
		wisKnop.addActionListener(this);
		wisKnop.setVisible(false);
		zetOpBalk(wisKnop);

		pijlVakken = new PijlVak[100];
		formuleVakken = new FormuleVak[100];
		imageComponenten = new ImageComponent[100];
		imageComponentenStap = new ImageComponent[100];

		formuleVakken[0] = new FormuleVak();
		formuleVakken[0].setFont(formuleVakFont);
		formuleVakken[0].addActionListener(this);
		formuleVakken[0].setLocation(formuleVakX, formuleVakY);
		add(formuleVakken[0]);

		formuleVak = formuleVakken[0];
		formuleVakSimpel = formuleVakken[0];

		plusKnop = new FormuleButton("plus", FormuleButton.BEWERKINGSKNOP);
		plusKnop.setBounds(knoppenStartX + 140, 2, 20, 20);
		plusKnop.addActionListener(this);
		zetOpBalk(plusKnop);
		plusKnop.setVisible(false);

		minKnop = new FormuleButton("min", FormuleButton.BEWERKINGSKNOP);
		minKnop.setBounds(knoppenStartX + 162, 2, 20, 20);
		minKnop.addActionListener(this);
		zetOpBalk(minKnop);
		minKnop.setVisible(false);

		maalKnop = new FormuleButton("maal", FormuleButton.BEWERKINGSKNOP);
		maalKnop.setBounds(knoppenStartX + 184, 2, 20, 20);
		maalKnop.addActionListener(this);
		zetOpBalk(maalKnop);
		maalKnop.setVisible(false);

		deelKnop = new FormuleButton("deel", FormuleButton.BEWERKINGSKNOP);
		deelKnop.setBounds(knoppenStartX + 206, 2, 20, 20);
		deelKnop.addActionListener(this);
		zetOpBalk(deelKnop);
		deelKnop.setVisible(false);

		haakjesKnop = new FormuleButton("haakjesweg", FormuleButton.BEWERKINGSKNOP);
		haakjesKnop.setBounds(knoppenStartX + 236, 2, 20, 20);
		haakjesKnop.addActionListener(this);
		zetOpBalk(haakjesKnop);
		haakjesKnop.setVisible(false);

		herleidKnop = new FormuleButton("herleid", FormuleButton.BEWERKINGSKNOP);
		herleidKnop.setBounds(knoppenStartX + 258, 2, 20, 20);
		herleidKnop.addActionListener(this);
		zetOpBalk(herleidKnop);
		herleidKnop.setVisible(false);

		ontbindKnop = new FormuleButton("ontbind", FormuleButton.BEWERKINGSKNOP);
		ontbindKnop.setBounds(knoppenStartX + 280, 2, 20, 20);
		ontbindKnop.setToolTipText("Ontbind");
		ontbindKnop.addActionListener(this);
		zetOpBalk(ontbindKnop);
		ontbindKnop.setVisible(false);

		splitsKnop = new FormuleButton("splits", FormuleButton.BEWERKINGSKNOP);
		splitsKnop.setBounds(knoppenStartX + 312, 2, 20, 20);
		splitsKnop.setToolTipText("Splits");
		splitsKnop.addActionListener(this);
		zetOpBalk(splitsKnop);
		splitsKnop.setVisible(false);

		wortelBewerkKnop = new FormuleButton("wortelbewerk", FormuleButton.BEWERKINGSKNOP);
		wortelBewerkKnop.setBounds(knoppenStartX + 334, 2, 20, 20);
		wortelBewerkKnop.setToolTipText("Wortels");
		wortelBewerkKnop.addActionListener(this);
		zetOpBalk(wortelBewerkKnop);
		wortelBewerkKnop.setVisible(false);

		abcKnop = new FormuleButton("abc", FormuleButton.BEWERKINGSKNOP);
		abcKnop.setBounds(knoppenStartX + 285, 2, 20, 20);
		abcKnop.addActionListener(this);
		zetOpBalk(abcKnop);
		abcKnop.setVisible(false);

		subKnop = new FormuleButton("sub", FormuleButton.BEWERKINGSKNOP);
		subKnop.setBounds(knoppenStartX + 309, 2, 20, 20);
		subKnop.addActionListener(this);
		zetOpBalk(subKnop);
		subKnop.setVisible(false);

		if ("MW".equals(WiskOpdr.deployVariant)) {
			plusKnop.setBounds(knoppenStartX + 220, 0, 22, 23);
			minKnop.setBounds(knoppenStartX + 244, 0, 22, 23);
			maalKnop.setBounds(knoppenStartX + 268, 0, 22, 23);
			deelKnop.setBounds(knoppenStartX + 292, 0, 22, 23);
			haakjesKnop.setBounds(knoppenStartX + 324, 0, 22, 23);
			herleidKnop.setBounds(knoppenStartX + 348, 0, 22, 23);
			abcKnop.setBounds(knoppenStartX + 377, 0, 22, 23);
			subKnop.setBounds(knoppenStartX + 403, 0, 22, 23);

			tip1Knop.setBounds(knoppenStartX + 377, 0, 22, 23);
			tip2Knop.setBounds(knoppenStartX + 403, 0, 22, 23);
			solveKnop.setBounds(knoppenStartX + 403, 0, 22, 23);
		}

		if ("GR".equals(WiskOpdr.deployVariant)) {
			plusKnop.setBounds(knoppenStartX + 150, 0, 22, 23);
			minKnop.setBounds(knoppenStartX + 174, 0, 22, 23);
			maalKnop.setBounds(knoppenStartX + 198, 0, 22, 23);
			deelKnop.setBounds(knoppenStartX + 222, 0, 22, 23);
			haakjesKnop.setBounds(knoppenStartX + 254, 0, 22, 23);
			herleidKnop.setBounds(knoppenStartX + 278, 0, 22, 23);
			abcKnop.setBounds(knoppenStartX + 377, 0, 22, 23);
			subKnop.setBounds(knoppenStartX + 403, 0, 22, 23);

			tip1Knop.setBounds(knoppenStartX + 377, 0, 22, 23);
			tip2Knop.setBounds(knoppenStartX + 403, 0, 22, 23);
			solveKnop.setBounds(knoppenStartX + 403, 0, 22, 23);
		}

		feedbackTekst = new FeedbackTekstArea();
		feedbackTekst.setSize(195, 20);
		feedbackTekst.setBackground(new Color(255, 255, 200));
		if ("MW".equals(WiskOpdr.deployVariant))
			feedbackTekst.setBackground(new Color(250, 255, 220));
		if ("GR".equals(WiskOpdr.deployVariant))
			feedbackTekst.setBackground(new Color(255, 255, 255));
		feedbackTekst.setBorders(true);
		feedbackTekst.setCloseable(true);
		feedbackTekst.addActionListener(this);

		if ("MW".equals(WiskOpdr.deployVariant)) {
			if ("MW".equals(WiskOpdr.deployVariant))
				feedbackBallonImage = NWButtonUI.loadImage("DWO-tekstballon.png", this);
			mwFeedbackPanel = new JPanel() {
				public void paintComponent(Graphics g) {
					int H = feedbackTekst.getHeight() + 20;
					int w = getWidth();
					g.drawImage(feedbackBallonImage, 0, 0, w, 20, 0, 0, w, 20, this);
					g.drawImage(feedbackBallonImage, 0, 20, w, H - 10, 0, 20, w, 25, this);
					g.drawImage(feedbackBallonImage, 0, H - 10, w, H, 0, 49, w, 59, this);
				}
			};
			mwFeedbackPanel.setLayout(null);
			mwFeedbackPanel.setSize(283, 59);
			feedbackCloseButton = new FormuleButton("maal", FormuleButton.MEERKNOP);
			feedbackCloseButton.addActionListener(this);
			feedbackCloseButton.setBackground(new Color(255, 255, 200));
			feedbackCloseButton.setBounds(mwFeedbackPanel.getSize().width - 17, 5, 12, 12);

			mwFeedbackPanel.add(feedbackCloseButton);
			feedbackTekst.setBorders(false);
		}
		if ("GR".equals(WiskOpdr.deployVariant)) {
			if ("GR".equals(WiskOpdr.deployVariant))
				feedbackBallonImage = NWButtonUI.loadImage("DWO-tekstballon-gr.png", this);
			mwFeedbackPanel = new JPanel() {
				public void paintComponent(Graphics g) {
					int H = feedbackTekst.getHeight() + 40;
					int w = getWidth();
					g.drawImage(feedbackBallonImage, 0, 0, w, 40, 0, 0, w, 40, this);
					g.drawImage(feedbackBallonImage, 0, 40, w, H - 10, 0, 40, w, 44, this);
					g.drawImage(feedbackBallonImage, 0, H - 10, w, H, 0, 44, w, 54, this);
				}
			};
			mwFeedbackPanel.setLayout(null);
			mwFeedbackPanel.setSize(206, 54);
			feedbackCloseButton = new FormuleButton("maal", FormuleButton.MEERKNOP);
			feedbackCloseButton.addActionListener(this);
			feedbackCloseButton.setBackground(new Color(255, 255, 255));
			feedbackCloseButton.setBounds(mwFeedbackPanel.getSize().width - 18, 18, 12, 12);

			mwFeedbackPanel.add(feedbackCloseButton);
			feedbackTekst.setBorders(false);
		}

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

		gebruikersSubstitutiesVak = new FormuleEditor(true);
		gebruikersSubstitutiesVak.setBounds(0, 30, 220, 150);
		gebruikersSubstitutiesVak.addActionListener(this);
		gebruikersSubstitutiesVak.setMultiLine(true);
		gebruikersSubstitutiesVak.setResizable(true);
		gebruikersSubstitutiesVak.setBackgroundContentPane(new Color(255, 255, 230));
		setLayer((Component) gebruikersSubstitutiesVak, JLayeredPane.POPUP_LAYER.intValue());
	}

	
	
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
		if (start)
			string = formuleVakken[0].toString();

		//VergelijkingMeerv v = FormuleParser.parseVergelijking(string, functieDefSet);
		VergelijkingMeerv v = FormuleParser.parseVergelijking(string, functieMVDefSet);

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
		if (start)
			s = s + "start";
		else
			s = s + goedFout;
		s = s + "   ;   ";
		s = s + "score = " + score;
		s = s + "   ;   ";
		s = s + new Date().toString();
		s = s + "   ;   ";
		if (start)
			s = s + "Regelnummer = " + 0;
		else
			s = s + "Regelnummer = " + stapNr;
		s = s + "   ;   ";
		
		s = s + fbTekst;

		log.addElement(s);
	}

	public Component getComponentSimpel() {
		return formuleVakSimpel;
	}

	public void zetSimpelFormuleVak(FormuleVak f) {
		f.setLocation(formuleVakX, 10);
		f.setBorder(true);

		if (stapNr > 0)// (!startString && stapNr>0 || stapNr>1)
		{
			boolean nk = nagekeken;
			stapTerug();
			formuleVakSimpel = null;
			maakStap();
			nagekeken = nk;
		}
		int fsx = formuleVakSimpel.getX();
		int fsy = formuleVakSimpel.getY();

		remove(formuleVakSimpel);
		formuleVakSimpel = f;
		formuleVakSimpel.setLocation(fsx, fsy);

		formuleVakken[stapNr] = formuleVakSimpel;
		formuleVak = formuleVakSimpel;
		add(formuleVakSimpel);

		goedIC.setVisible(false);
		goedIC.zetKlein(false);
		add(goedIC);
		halfIC.setVisible(false);
		halfIC.zetKlein(false);
		add(halfIC);
		foutIC.setVisible(false);
		foutIC.zetKlein(false);
		add(foutIC);

		if ((mode == 0 || mode == 1 || nagekeken) && (stapNr > 0 || !hasStartString))
			kijkNa();
		else if (stapNr == 0 && hasStartString)
			maakStap();
	}

	public Component getGoedIC() {
		return goedIC;
	}

	public Component getFoutIC() {
		return foutIC;
	}

	public Component getHalfIC() {
		return halfIC;
	}
	
	public Component getHuidigIC() {
		return huidigIC;
	}

	public Component getFeedbackIC()
	{
		return feedbackIC;
	}
	
	public boolean hasFeedback() {
		return hasFeedback;
	}

	public InteractieEditPanel getEditPanel() {
		return new AntwoordVergelijkingVakEditPanel(1);
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
		if ("MW".equals(WiskOpdr.deployVariant)) {
			gelijkwaardigKnop.setLocation(b - 48, 0);
			terugKnop.setLocation(b - 25, 0);
			abcKnop.setLocation(b - 114, 0);
			subKnop.setLocation(b - 90, 0);
			tip1Knop.setLocation(b - 240, 0);
			tip2Knop.setLocation(b - 210, 0);
			hulpKnop.setLocation(b - 180, 0);
			solveKnop.setLocation(b - 150, 0);
		} else if ("GR".equals(WiskOpdr.deployVariant)) {
			gelijkwaardigKnop.setLocation(b - 36, 0);
			terugKnop.setLocation(b - 18, 0);
			subKnop.setLocation(b - 66, 0);
			if (subKnop.isVisible())
				abcKnop.setLocation(b - 90, 0);
			else
				abcKnop.setLocation(b - 66, 0);
			tip1Knop.setLocation(b - 200, 0);
			tip2Knop.setLocation(b - 175, 0);
			hulpKnop.setLocation(b - 180, 0);
			solveKnop.setLocation(b - 150, 0);

		} else {
			gelijkwaardigKnop.setLocation(b - 47, 2);
			terugKnop.setLocation(b - 25, 2);
			abcKnop.setLocation(b - 112, 2);
			subKnop.setLocation(b - 90, 2);
			tip1Knop.setLocation(b - 240, 2);
			tip2Knop.setLocation(b - 210, 2);
			hulpKnop.setLocation(b - 180, 2);
			solveKnop.setLocation(b - 150, 2);

		}

		logKnop.setBounds(b - 65, h - 55, 60, 21);

		super.setBounds(x, y, b, h);
	}

	public void wis() {
		for (int i = 0; i < stapNr + 1; i++) {
			if (i > 0)
				remove(formuleVakken[i]);
			if (imageComponenten[i] != null)
				remove(imageComponenten[i]);
			if (imageComponentenStap[i] != null)
				remove(imageComponentenStap[i]);
			if (i < stapNr)
				remove(pijlVakken[i]);
		}
		/*
		 * for(int i=1 ; i<stapNr+1; i++) { remove(formuleVakken[i]);
		 * if(imageComponenten[i]!=null) remove(imageComponenten[i]); } for(int
		 * i=0 ; i<stapNr; i++) { remove(pijlVakken[i]); }
		 */
		formuleVak = formuleVakken[0];
		formuleVakSimpel = formuleVakken[0];
		if (huidigIC != null) {
			huidigIC.setVisible(false);
			huidigIC.setLocation(0, 0);
		}
		stapNr = 0;
		formuleVakken[stapNr].setEditable(true);
		correct = false;
		fout = false;
		score = 0;
		errorCount = 0;
		if (feedbackTekst != null) {
			if ("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant))
				remove(mwFeedbackPanel);
			else
				remove(feedbackTekst);
		}
		nagekeken = false;
		ingevuld = false;
		zetGeenAntwoord(false);
		zetRandomFout(false);
		substitutie = null;
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
			formuleVakken[0].setFont(formuleVakFont);
			formuleVakSimpel.setFont(formuleVakFont);
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
		int soortHerleiding = 0;
		int puntenGelijkwaardig = 10;
		int puntenHerleiding = 0;
		int puntenExact = 0;
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
			if (h.containsKey("stappen"))
				stappen = ((Boolean) h.get("stappen")).booleanValue();
			if (h.containsKey("soortHerleiding"))
				soortHerleiding = ((Integer) h.get("soortHerleiding")).intValue();
			if (h.containsKey("puntenGelijkwaardig"))
				puntenGelijkwaardig = ((Integer) h.get("puntenGelijkwaardig")).intValue();
			if (h.containsKey("puntenHerleiding"))
				puntenHerleiding = ((Integer) h.get("puntenHerleiding")).intValue();
			if (h.containsKey("puntenExact"))
				puntenExact = ((Integer) h.get("puntenExact")).intValue();
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
		exactP = exact;
		significantP = significant;
		vormP = vorm;
		eindOplossingNodigP = eindOplossingNodig;
		gelijkwaardigP = gelijkwaardig;
		this.goedHalfFout = goedHalfFout;
		this.puntenFeedback = puntenFeedback;

		// System.out.println("antwoordztring :"+antwoordString);

		try {
			antwoordString = FormuleParser.randomizeString(antwoordString, randomVarNamen, randomVarWaarden);
		} catch (Exception e) {
			antwoordString = "$f???@";
			zetGeenAntwoord(true);
			// antwoordSyntaxFout = true;
		}

		try {
			vormString = FormuleParser.randomizeString(vormString, randomVarNamen, randomVarWaarden);
		} catch (Exception e) {
			vormString = "$f???@";
			zetGeenAntwoord(true);
			// antwoordSyntaxFout = true;
		}

		try {
			feedback = FormuleParser.randomizeTekstVakString(feedback, randomVarNamen, randomVarWaarden);
		} catch (Exception e) {
			feedback = "$f???@";
			// antwoordSyntaxFout = true;
		}
		zetJuisteAntwoord(antwoordString);
		zetJuisteVorm(vormString);
		// if(nr==0) basisAntwoord = juisteAntwoorden[0];

		//this.gekozenAntwoordString = antwoordString;
		this.feedback = feedback;
		if(feedbackSize)
        {	this.feedbackWidth = feedbackWidth;
        	this.feedbackHeight = feedbackHeight;
        }
	}
	
	public void zetBalansVergelijking(VergelijkingMeerv v){
		formuleVak.vulVak("$f" + v.toString() + "@");
		//if(formuleVak.geefVergelijking().isOplossing(gewensteEindOplossing.geefEindOplossingen("x"), "x", gewensteEindOplossing.geefVergTekens()))
		//{	stapOk = true;
		//	maakStap();
		//}

		
	}
	
	public Vergelijking geefBalansVergelijking(){	
		balansKoppeling = true;
		if(huidigeVergelijking!=null)return huidigeVergelijking.geefVergelijking(0);
		return null;
	}
	
	public Vergelijking geefInitBalansVergelijking(){	
		if(gewensteEindOplossing!=null)return gewensteEindOplossing.geefVergelijking(0);
		return null;
	}
	
	public void maakBalansStap(){
		if(formuleVak.geefVergelijking()!=null && !formuleVak.geefVergelijking().isEindOplossing("x"))		
			stapOk = true;
		maakStap();
	}

	public void zetOpdracht(Hashtable h, String[] randomVars, Hashtable randomValues) {
		if(fontOvererving && getParent() instanceof TekstInteractiePanelVak)
		{	Font geerftFont = ((TekstInteractiePanelVak)getParent()).getTekstVak().getFont();
			if (!geerftFont.getName().equals("TimesRoman") && WiskOpdr.formTimes && !WiskOpdr.mac) {
				geerftFont = new Font("TimesRoman", geerftFont.getStyle(), geerftFont.getSize() * 6 / 5);
			}
			formuleVakFont = geerftFont;
			formuleVakken[0].setFont(formuleVakFont);
			formuleVakSimpel.setFont(formuleVakFont);
		}
		
		randomVarNamen = randomVars;
		randomVarWaarden = randomValues;

		String antwoordString = "$f@";
		String startString = "$f@";
		boolean vorm = false;
		boolean exact = false;
		boolean significant = false;
		boolean stappen = true;
		int puntenGelijkwaardig = 10;
		int puntenExact = 0;
		int puntenSignificant = 0;
		boolean eindOplossingNodig = true;
		int puntenEindOplossing = 0;
		int puntenVorm = 0;
		boolean bewerkingKnoppen = false;
		boolean bewerkingKnoppenExtra = false;
		boolean abcKnop = false;
		boolean subKnop = false;
		boolean subKnopExtra = false;
		Hashtable[] answerModels = null;
		boolean hasFeedback = false;
		boolean feedbackSize = false;
		String vormString = "$f@";
		boolean tips = false;
		String strategieDomein = "";
		//int feedbackModus = 0;
		String[] antwoordSubStrings = null;
		String[] antwoordFuncStrings = null;
		boolean pijl = true;
		boolean linStrategieVersie = false;
		boolean linOefenVersie = false;
		boolean bordjesMethode = false;
		boolean check = true;
		boolean teltMee = true;
		boolean logOption = false;
		String logID = "";
		boolean tipOpBalk = true;
		boolean hulpOpBalk = false;
		boolean stapOpBalk = false;
		boolean solveOpBalk = false;
		boolean meerTips = false;
		boolean tipBijFout = false;
		boolean feedbackBijFout = false;
		boolean hulpBijTip = false;
		Hashtable changedTexts = new Hashtable();
		Hashtable ideasInstellingen = null;
		double eqTestValueMin = 0;
		double eqTestValueMax = 5;
		int scoreMax = 0;
		boolean uitw = false;
		boolean casAntw = false;
		boolean boxMetRand = true;
		boolean[][] logObjectives = null;
		
		if (h.containsKey("antwoordString"))
			antwoordString = (String) h.get("antwoordString");
		if (h.containsKey("startString"))
			startString = (String) h.get("startString");
		if (h.containsKey("vorm"))
			vorm = ((Boolean) h.get("vorm")).booleanValue();
		if (h.containsKey("exact"))
			exact = ((Boolean) h.get("exact")).booleanValue();
		if(h.containsKey("significant")) 
			significant = ((Boolean)h.get("significant")).booleanValue();
		if (h.containsKey("stappen"))
			stappen = ((Boolean) h.get("stappen")).booleanValue();
		if (h.containsKey("puntenGelijkwaardig"))
			puntenGelijkwaardig = ((Integer) h.get("puntenGelijkwaardig")).intValue();
		if (h.containsKey("puntenExact"))
			puntenExact = ((Integer) h.get("puntenExact")).intValue();
		if(h.containsKey("puntenSignificant")) 
			puntenSignificant = ((Integer)h.get("puntenSignificant")).intValue();
		if (h.containsKey("puntenVorm"))
			puntenVorm = ((Integer) h.get("puntenVorm")).intValue();
		if (h.containsKey("puntenEindOplossing"))
			puntenEindOplossing = ((Integer) h.get("puntenEindOplossing")).intValue();
		if (h.containsKey("eindOplossingNodig"))
			eindOplossingNodig = ((Boolean) h.get("eindOplossingNodig")).booleanValue();
		if (h.containsKey("bewerkingKnoppen"))
			bewerkingKnoppen = ((Boolean) h.get("bewerkingKnoppen")).booleanValue();
		if (h.containsKey("bewerkingKnoppenExtra"))
			bewerkingKnoppenExtra = ((Boolean) h.get("bewerkingKnoppenExtra")).booleanValue();
		if (h.containsKey("abcKnop"))
			abcKnop = ((Boolean) h.get("abcKnop")).booleanValue();
		if (h.containsKey("subKnop"))
			subKnop = ((Boolean) h.get("subKnop")).booleanValue();
		if (h.containsKey("subKnopExtra"))
			subKnopExtra = ((Boolean) h.get("subKnopExtra")).booleanValue();
		if (h.containsKey("answerModels"))
			answerModels = (Hashtable[]) h.get("answerModels");
		if (h.containsKey("hasFeedback"))
			hasFeedback = ((Boolean) h.get("hasFeedback")).booleanValue();
		if(h.containsKey("feedbackSize")) 
			feedbackSize = ((Boolean)h.get("feedbackSize")).booleanValue();
		
		if (h.containsKey("vormString"))
			vormString = (String) h.get("vormString");
		if (h.containsKey("tips"))
			tips = ((Boolean) h.get("tips")).booleanValue();
		if (tips) {
			if (h.containsKey("ideasInstellingen"))
				ideasInstellingen = (Hashtable) h.get("ideasInstellingen");
			else { // voor backward comp.
				if (h.containsKey("tipOpBalk"))
					tipOpBalk = ((Boolean) h.get("tipOpBalk")).booleanValue();
				if (h.containsKey("hulpOpBalk"))
					hulpOpBalk = ((Boolean) h.get("hulpOpBalk")).booleanValue();
				if (h.containsKey("stapOpBalk"))
					stapOpBalk = ((Boolean) h.get("stapOpBalk")).booleanValue();
				if (h.containsKey("solveOpBalk"))
					solveOpBalk = ((Boolean) h.get("solveOpBalk")).booleanValue();
				if (h.containsKey("meerTips"))
					meerTips = ((Boolean) h.get("meerTips")).booleanValue();
				if (h.containsKey("tipBijFout"))
					tipBijFout = ((Boolean) h.get("tipBijFout")).booleanValue();
				if (h.containsKey("feedbackBijFout"))
					feedbackBijFout = ((Boolean) h.get("feedbackBijFout")).booleanValue();
				if (h.containsKey("hulpBijTip"))
					hulpBijTip = ((Boolean) h.get("hulpBijTip")).booleanValue();
				if (h.containsKey("changedTexts"))
					changedTexts = (Hashtable) h.get("changedTexts");
				Hashtable sod = AntwoordVergelijkingVakEditPanel.strategieOudNieuw;
				if (sod.containsKey(strategieDomein))
					strategieDomein = (String) sod.get(strategieDomein);
				if (h.containsKey("strategieDomein"))
					strategieDomein = (String) h.get("strategieDomein");
			}	
		}

//		if (h.containsKey("feedbackModus"))
//			feedbackModus = ((Integer) h.get("feedbackModus")).intValue();
		if (h.containsKey("antwoordSubStrings"))
			antwoordSubStrings = (String[]) h.get("antwoordSubStrings");
		if (h.containsKey("antwoordFuncStrings"))
			antwoordFuncStrings = (String[]) h.get("antwoordFuncStrings");
		if (h.containsKey("pijl"))
			pijl = ((Boolean) h.get("pijl")).booleanValue();
		if (h.containsKey("linStrategieVersie"))
			linStrategieVersie = ((Boolean) h.get("linStrategieVersie")).booleanValue();
		if (h.containsKey("bordjesMethode"))
			bordjesMethode = ((Boolean) h.get("bordjesMethode")).booleanValue();
		if (h.containsKey("linOefenVersie"))
			linOefenVersie = ((Boolean) h.get("linOefenVersie")).booleanValue();
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
		if (h.containsKey("casAntw"))
			casAntw = ((Boolean) h.get("casAntw")).booleanValue();
		if (h.containsKey("boxMetRand"))
			boxMetRand = ((Boolean) h.get("boxMetRand")).booleanValue();
		if(h.containsKey("logObjectives")) 
			logObjectives = (boolean[][])h.get("logObjectives");
		
		this.pijl = pijl;
		this.subKnopExtra = subKnopExtra;
		this.linStrategieVersie = linStrategieVersie;
		this.linOefenVersie = linOefenVersie;
		this.bordjesMethode = bordjesMethode;
		this.check = check;
		this.teltMee = teltMee;
		this.logOption = logOption;
		this.logID = logID;
		this.eqTestValueMin = eqTestValueMin;
		this.eqTestValueMax = eqTestValueMax;

		//setScoreDataVergelijking(vorm, eindOplossingNodig, exact, puntenGelijkwaardig, puntenVorm, puntenEindOplossing, puntenExact);
		this.vorm = vorm;
		this.eindOplossingNodig = eindOplossingNodig;
		this.exactNodig = exact;
		this.significantNodig = significant;
		this.puntenGelijkwaardig = puntenGelijkwaardig;
		this.puntenVorm = puntenVorm;
		this.puntenEindOplossing = puntenEindOplossing;
		this.puntenExact = puntenExact;
		this.puntenSignificant = puntenSignificant;
		this.logObjectives = logObjectives;
		
		zetStappen(stappen);

		try {
			antwoordString = FormuleParser.randomizeString(antwoordString, randomVars, randomValues);
		} catch (Exception e) {
			antwoordString = "$f???@";
			zetGeenAntwoord(true);
		}
		zetJuisteAntwoord(antwoordString);

		if (antwoordSubStrings != null) {
			boolean subCorrect = true;
			antwoordSubstituties = new Vergelijking[antwoordSubStrings.length];
			for (int i = 0; i < antwoordSubStrings.length; i++)
			{
				try
				{
					antwoordSubStrings[i] = FormuleParser.randomizeString(antwoordSubStrings[i], randomVars, randomValues);
					//antwoordSubstituties[i] = (FormuleParser.parseVergelijking(antwoordSubStrings[i], functieDefSet)).geefVergelijking(0);
					antwoordSubstituties[i] = (FormuleParser.parseVergelijking(antwoordSubStrings[i], functieMVDefSet)).geefVergelijking(0);
					if (!antwoordSubstituties[i].geefExpLinks().isVar())
						subCorrect = false;
				} catch (Exception e) {
					subCorrect = false;
				}
			}
			if (!subCorrect)
				antwoordSubstituties = null;
			
			
			if(antwoordSubstituties==null)
			{	
				antwoordStringSubstituties = antwoordSubStrings;
				/*subCorrect = true;
				
				antwoordStringSubstituties = new Hashtable();
				for (int i = 0; i < antwoordSubStrings.length; i++)
				{	int index = antwoordSubStrings[i].indexOf('=');
					if(index==-1) 
					{	subCorrect = false;
						break;
					}
					String toSubstString = antwoordSubStrings[i].substring(2, index);
					String substString = antwoordSubStrings[i].substring(index+1,antwoordSubStrings[i].length()-1 );
					System.out.println("toSubstString" + toSubstString.trim());
					System.out.println("substString" + substString.trim());
					antwoordStringSubstituties.put(toSubstString.trim(), substString.trim());	
				}
				if (!subCorrect)
					antwoordStringSubstituties = null;
				*/
			}
		}
		
		if (antwoordFuncStrings != null) {
			
			for (int i = 0; i < antwoordFuncStrings.length; i++)
			{
				String[] functieDelen = antwoordFuncStrings[i].split("=");
				if(functieDelen.length<2) break;
				String functieExpressieString = "$f"+functieDelen[1];
				String functieNaam = functieDelen[0].substring(2, functieDelen[0].indexOf('('));
				String varString = functieDelen[0].substring(functieDelen[0].indexOf('(')+1, functieDelen[0].indexOf(')'));
				String[] functieMVVariabelen = varString.split(",");
				//String functieVariabele = functieDelen[0].substring(functieDelen[0].indexOf('(')+1, functieDelen[0].indexOf('(')+2);
				//System.out.println("varString:"+varString);
				//System.out.println("functieExpressieString:"+functieExpressieString);
				//System.out.println("functieMVVariabelen:"+functieMVVariabelen[0]);
				try
				{
					functieExpressieString = FormuleParser.randomizeString(functieExpressieString, randomVars, randomValues);
					
				} catch (Exception e) {
					
				}
				Expressie functieExpressie = FormuleParser.geefExpressie(functieExpressieString);
				//functieDefSet.addFunctieExpressie(functieNaam, functieVariabele, functieExpressie);
				functieMVDefSet.addFunctieMVExpressie(functieNaam, functieMVVariabelen, functieExpressie);
			}
		}

		try {
			vormString = FormuleParser.randomizeString(vormString, randomVars, randomValues);
		} catch (Exception e) {
			vormString = "$f???@";
			zetGeenAntwoord(true);
			// antwoordSyntaxFout = true;
		}
		zetJuisteVorm(vormString);

		try {
			startString = FormuleParser.randomizeString(startString, randomVars, randomValues);
		} catch (Exception e) {
			startString = "$f???@";
		}

		if (bordjesMethode && startString.length()>3) {
			startString = "$f" + (FormuleParser.parseVergelijking(startString, functieMVDefSet)).toStringStrikt() + "@";
			//startString = "$f" + (FormuleParser.parseVergelijking(startString, functieDefSet)).toStringStrikt() + "@";
			// System.out.println(startString);
		}
		zetStartString(startString);

		//this.gekozenAntwoordString = antwoordString;
		//this.gekozenStartString = startString;
		this.answerModels = answerModels;
		this.hasFeedback = hasFeedback;
		this.feedbackSize = feedbackSize;

		this.tips = tips;
		//this.feedbackModus = feedbackModus;
		this.scoreMax = scoreMax;
		this.uitw = uitw;
		this.casAntw = casAntw;
		
		zetBewerkingKnoppen(bewerkingKnoppen);
		zetBewerkingKnoppenExtra(bewerkingKnoppenExtra);
		zetAbcKnop(abcKnop);
		zetSubKnop(subKnop);
		gelijkwaardigKnop.setVisible(!linStrategieVersie && !bordjesMethode);

		if (tips && ideasInstellingen != null)
			setIdeas(ideasInstellingen);
		else if (tips) {
			// voor backwards comp
			this.strategieDomein = strategieDomein;
			this.tipOpBalk = tipOpBalk;
			this.hulpOpBalk = hulpOpBalk;
			this.stapOpBalk = stapOpBalk;
			this.solveOpBalk = solveOpBalk;
			this.meerTips = meerTips;
			this.tipBijFout = tipBijFout;
			this.feedbackBijFout = feedbackBijFout;
			this.hulpBijTip = hulpBijTip;
			this.changedTexts = changedTexts;
			
			tip1Knop.setVisible(tipOpBalk);
			tip2Knop.setVisible(hulpOpBalk);
			hulpKnop.setVisible(stapOpBalk);
			solveKnop.setVisible(solveOpBalk);
		}
		wisKnop.setVisible(casAntw  && !hasStartString || tips && !hasStartString && diagnose);
		zetMetRand(boxMetRand);
		feedbackIC.setVisible(false);
	}

	public void setIdeas(Hashtable h) {
		if (h.containsKey("strategieDomein"))
			strategieDomein = (String) h.get("strategieDomein");
		if (h.containsKey("tipOpBalk"))
			tipOpBalk = ((Boolean) h.get("tipOpBalk")).booleanValue();
		if (h.containsKey("hulpOpBalk"))
			hulpOpBalk = ((Boolean) h.get("hulpOpBalk")).booleanValue();
		if (h.containsKey("stapOpBalk"))
			stapOpBalk = ((Boolean) h.get("stapOpBalk")).booleanValue();
		if (h.containsKey("solveOpBalk"))
			solveOpBalk = ((Boolean) h.get("solveOpBalk")).booleanValue();
		if (h.containsKey("meerTips"))
			meerTips = ((Boolean) h.get("meerTips")).booleanValue();
		if (h.containsKey("tipBijFout"))
			tipBijFout = ((Boolean) h.get("tipBijFout")).booleanValue();
		if (h.containsKey("feedbackBijFout"))
			feedbackBijFout = ((Boolean) h.get("feedbackBijFout")).booleanValue();
		if (h.containsKey("hulpBijTip"))
			hulpBijTip = ((Boolean) h.get("hulpBijTip")).booleanValue();
		if (h.containsKey("changedTexts"))
			changedTexts = (Hashtable) h.get("changedTexts");
		if (h.containsKey("diagnose"))
			diagnose = ((Boolean) h.get("diagnose")).booleanValue();
		if (h.containsKey("aftrekTip"))
			aftrekTip = ((Integer) h.get("aftrekTip")).intValue();
		if (h.containsKey("aftrekHulp"))
			aftrekHulp = ((Integer) h.get("aftrekHulp")).intValue();
		if (h.containsKey("aftrekStap"))
			aftrekStap = ((Integer) h.get("aftrekStap")).intValue();
		if (h.containsKey("aftrekSolve"))
			aftrekSolve = ((Integer) h.get("aftrekSolve")).intValue();

		tip1Knop.setVisible(tipOpBalk);
		tip2Knop.setVisible(hulpOpBalk);
		hulpKnop.setVisible(stapOpBalk);
		solveKnop.setVisible(solveOpBalk);
		
	}

	public void setState(Hashtable h) {
		if (h == null)
			return;
		for (int i = 0; i < stapNr + 1; i++) {
			remove(formuleVakken[i]);
		}
		for (int i = 0; i < stapNr; i++) {
			remove(pijlVakken[i]);
		}

		int stapNr = 0;
		String[] formuleVakInhouden = null;
		//int[] vvY = null;
		String[] pijlVakInhouden = null;
		String[] pijlVakOperatoren = null;
		//int[] pvY = null;
		boolean ingevuld = false;
		boolean nagekeken = false;
		String substitutieString = "";
		//String gewensteAntwoordString = null;
		//String startString = "$f@";
		String antwoordString = null;
		//int aftrekTipHulp = 0;
		Vector log = new Vector();
		int attemptsCount = 0;
		int errorCount = 0;
		String[] gebruikersSubStrings = null;
		int ideasPuntenAftrek = 0;

		if (h.containsKey("stapNr"))
			stapNr = ((Number) h.get("stapNr")).intValue();
		if (h.containsKey("formuleVakInhouden"))
			formuleVakInhouden = OpdrNavStruct.toStringArray(h.get("formuleVakInhouden"));
		//if (h.containsKey("vvY"))
		//	vvY = OpdrNavStruct.toIntArray(h.get("vvY"));
		if (h.containsKey("pijlVakInhouden"))
			pijlVakInhouden = OpdrNavStruct.toStringArray(h.get("pijlVakInhouden"));
		if (h.containsKey("pijlVakOperatoren"))
			pijlVakOperatoren = OpdrNavStruct.toStringArray(h.get("pijlVakOperatoren"));
		//if (h.containsKey("pvY"))
		//	pvY = OpdrNavStruct.toIntArray(h.get("pvY"));
		if (h.containsKey("ingevuld"))
			ingevuld = ((Boolean) h.get("ingevuld")).booleanValue();
		if (h.containsKey("nagekeken"))
			nagekeken = ((Boolean) h.get("nagekeken")).booleanValue();
		if (h.containsKey("substitutieString"))
			substitutieString = (String) h.get("substitutieString");
		//if (h.containsKey("gewensteAntwoordString"))
		//	gewensteAntwoordString = (String) h.get("gewensteAntwoordString");
		if (h.containsKey("antwoordString"))
			antwoordString = (String) h.get("antwoordString");
		//if (h.containsKey("startString"))
		//	gekozenStartString = (String) h.get("startString");
//		if (h.containsKey("aftrekTipHulp"))
//			aftrekTipHulp = ((Integer) h.get("aftrekTipHulp")).intValue();
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

		 //patch voor reviewMode eindtoets html5
  		if(mode==3 && uitw==true && stapNr>0 && formuleVakInhouden[stapNr].equals("$f@")) {
  			stapNr--;
  			//formuleVakInhouden[stapNr] = formuleVakString;
  		}
  		
		this.stapNr = stapNr;
		formuleVakken = new FormuleVak[100];
		int y = formuleVakY;
		for (int i = 0; i < stapNr + 1; i++) {
			formuleVakken[i] = new FormuleVak();
			formuleVakken[i].setFont(formuleVakFont);
			if (i < stapNr && mode != 2)
				formuleVakken[i].setEditable(false);
			if (hasStartString && i == 0 && mode == 2)
				formuleVakken[i].setEditable(false);
			int x = formuleVakX;

			formuleVakken[i].setLocation(x, y);
			formuleVakken[i].addActionListener(this);
			add(formuleVakken[i]);
			formuleVakken[i].vulVak(formuleVakInhouden[i]);
			y = formuleVakken[i].getLocation().y + formuleVakken[i].getSize().height + stapH;
		}

		pijlVakken = new PijlVak[100];
		for (int i = 0; i < stapNr; i++) {
			if (pijlVakOperatoren != null && pijlVakOperatoren[i] != null)
				pijlVakken[i] = new PijlVak(pijlVakOperatoren[i]);
			else
				pijlVakken[i] = new PijlVak("implicatie");
			if (pijlVakInhouden != null && pijlVakInhouden[i] != null)
				pijlVakken[i].zetExpressie(pijlVakInhouden[i]);
			y = formuleVakken[i].getLocation().y + formuleVakken[i].getSize().height / 2;
			pijlVakken[i].setLocation(getSize().width - pijlX, y);
			if (pijlVakOperatoren != null && pijlVakOperatoren[i] != null && (pijlVakOperatoren[i].equals("sub") || pijlVakOperatoren[i].equals("abc")))
				pijlVakken[i].setLocation(getSize().width - pijlX - 30, y);
			if ("GR".equals(WiskOpdr.deployVariant) && pijlVakOperatoren != null && pijlVakOperatoren[i] != null && (pijlVakOperatoren[i].equals("sub") || pijlVakOperatoren[i].equals("abc")))
				pijlVakken[i].setLocation(getSize().width - pijlX - 60, y);
			
			add(pijlVakken[i]);
			pijlVakken[i].setPijlVisible(pijl);
		}

		this.ingevuld = ingevuld;
		this.nagekeken = nagekeken;

		//this.gekozenAntwoordString = gewensteAntwoordString;
		this.formuleVakString = antwoordString;
		//this.gekozenStartString = gekozenStartString;
		//this.aftrekTipHulp = aftrekTipHulp;
		this.log = log;
		this.attemptsCount = attemptsCount;
		this.errorCount = errorCount;
		this.ideasPuntenAftrek = ideasPuntenAftrek;

		gebruikersSubstitutiesVak.zetRegels(gebruikersSubStrings);

		if (ingevuld)
			vulVak(formuleVakString);
		//zetJuisteAntwoord(gekozenAntwoordString);

		if (!substitutieString.equals(""))
			substitutie = FormuleParser.parse(FormuleParser.schoon(FormuleParser.formuleString(substitutieString)));

		if (nagekeken && mode!=2)
			formuleVakken[stapNr].setEditable(false);
		else if ((stapNr > 0 || !hasStartString))
			formuleVakken[stapNr].setEditable(true);
		else {
			formuleVakken[stapNr].setEditable(false);
			stapOk = true;
		}
		formuleVak = formuleVakken[stapNr];
		formuleVakSimpel = formuleVakken[stapNr];
		
		if (casAntw && !hasStartString) {
			String vergString = formuleVakken[0].toString();
        	//VergelijkingMeerv vm = FormuleParser.parseVergelijking(vergString, functieDefSet);
        	VergelijkingMeerv vm = FormuleParser.parseVergelijking(vergString, functieMVDefSet);
        	String vergStringCas = "$f@";
        	
        	VergelijkingMeerv vmAntw = null;
        	if(vm!=null){
        		Vergelijking v = vm.geefVergelijking(0);
        		vergStringCas = v.geefExpLinks().toStringCAS() + "==" + v.geefExpRechts().toStringCAS();
        		String[] varNamen = v.geefVarNamen();
        		if(varNamen.length != 1)return;
        		vmAntw = //Expressie.solveWithCAS(vergStringCas, varNamen[0]);
        				 Expressie.solve(v, varNamen[0]);
        		String def = "";
        		if(vmAntw!=null) def = vmAntw.toString();
        		zetJuisteAntwoord("$f"+def+"@");
        		hasStartString = true;
			}
		}

		if ((mode == 0 || nagekeken) && (stapNr > 0 || !hasStartString)) {
			if (tips && diagnose)
				kijkNaIdeas();
			else
				kijkNa();
			if (linStrategieVersie) {
				if (!isEindOplossing)
					zetGoedFout(GEEN, -1);
				formuleVak.setEditable(false);
			}
		}
		
	}

	public Hashtable getState() {
		int stapNr = 0;
		String[] formuleVakInhouden = null;
		int[] vvY = null;
		String[] pijlVakInhouden = null;
		String[] pijlVakOperatoren = null;
		int[] pvY = null;
		boolean ingevuld = true;
		boolean nagekeken = false;
		String substitutieString = "";
		//String gewensteAntwoordString = null;
		//String startString = "$f@";
		String antwoordString = null;
		int aftrekTipHulp = 0;
		Vector log = new Vector();
		int attemptsCount = 0;
		int errorCount = 0;
		String[] gebruikersSubStrings;
		int ideasPuntenAftrek = 0;

		stapNr = this.stapNr;
		ingevuld = this.ingevuld;
		if (!ingevuld && mode != 1 && mode != 2 && mode != 3 && !(hasStartString && stapNr == 0)) {
			try {
				formuleVak.finish();
				if (uitw && formuleVak != formuleVakSimpel)
					formuleVakSimpel.finish();
			} catch (Exception e) {
			}
		}

		formuleVakInhouden = new String[stapNr + 1];
		vvY = new int[stapNr + 1];
		for (int i = 0; i < stapNr + 1; i++) {
			if(formuleVakken[i]==null) formuleVakInhouden[i] = "$f@";
			else formuleVakInhouden[i] = formuleVakken[i].toString();
			vvY[i] = formuleVakken[i].getLocation().y;
		}

		pijlVakInhouden = new String[stapNr];
		pijlVakOperatoren = new String[stapNr];
		pvY = new int[stapNr];
		for (int i = 0; i < stapNr; i++) {
			pijlVakInhouden[i] = pijlVakken[i].geefExpressieString();
			pijlVakOperatoren[i] = pijlVakken[i].geefOperator();
			pvY[i] = pijlVakken[i].getLocation().y;
		}

		ingevuld = this.ingevuld;
		nagekeken = this.nagekeken;
		attemptsCount = this.attemptsCount;
		errorCount = this.errorCount;
		ideasPuntenAftrek = this.ideasPuntenAftrek;

		if (substitutie != null)
			substitutieString = "$f" + substitutie.toString() + "@";

		gebruikersSubStrings = gebruikersSubstitutiesVak.geefRegels();

		//gewensteAntwoordString = this.gekozenAntwoordString;
		//startString = this.gekozenStartString;
		antwoordString = toString();

		//aftrekTipHulp = this.aftrekTipHulp;
		log = this.log;

		if (formuleVakken[stapNr] != null && formuleVakken[stapNr].toString().equals("$f@")) {
			if (stapNr > 1)
				stapTerug();
			else if (!hasStartString && stapNr > 0)
				stapTerug();
		}
		if(!("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant))) kijkNa(-1, false);
		if (logOption) {

			Hashtable logMap = new Hashtable();

			String formule = "";
			String string = formuleVakInhouden[stapNr];
			//VergelijkingMeerv v = FormuleParser.parseVergelijking(string, functieDefSet);
			VergelijkingMeerv v = FormuleParser.parseVergelijking(string, functieMVDefSet);

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

		Hashtable h = new Hashtable();
		h.put("stapNr", new Integer(stapNr));
		h.put("formuleVakInhouden", formuleVakInhouden);
		h.put("vvY", vvY);
		h.put("pijlVakInhouden", pijlVakInhouden);
		h.put("pijlVakOperatoren", pijlVakOperatoren);
		h.put("pvY", pvY);
		h.put("ingevuld", new Boolean(ingevuld));
		h.put("nagekeken", new Boolean(nagekeken));
		h.put("substitutieString", substitutieString);
		//h.put("gewensteAntwoordString", gewensteAntwoordString);
		//h.put("startString", startString);
		h.put("antwoordString", antwoordString);
		h.put("aftrekTipHulp", new Integer(aftrekTipHulp));
		h.put("log", log);
		h.put("attemptsCount", new Integer(attemptsCount));
		h.put("errorCount", new Integer(errorCount));
		h.put("gebruikersSubStrings", gebruikersSubStrings);
		h.put("ideasPuntenAftrek", new Integer(ideasPuntenAftrek));

		return h;
	}

	public void zetMaat()
	{

	}

	public int geefAsHoogte()
	{
		return 0;
	}

	public void zetStappen(boolean b)
	{
		stappen = b;
		gelijkwaardigKnop.setVisible(b);
		terugKnop.setVisible(b);
	}

	public void setScoreDataVergelijking(boolean vorm, boolean eindOplossingNodig, boolean exactNodig, int puntenGelijkwaardig, int puntenVorm, int puntenEindOplossing, int puntenExact)
	{
		this.vorm = vorm;
		this.eindOplossingNodig = eindOplossingNodig;
		this.exactNodig = exactNodig;
		this.puntenGelijkwaardig = puntenGelijkwaardig;
		this.puntenVorm = puntenVorm;
		this.puntenEindOplossing = puntenEindOplossing;
		this.puntenExact = puntenExact;
	}

	public VergelijkingMeerv geefVergelijking()
	{
		return formuleVak.geefVergelijking();
	}

	public VergelijkingMeerv geefStartVergelijking()
	{
		return formuleVakken[0].geefVergelijking();
	}

	public VergelijkingMeerv geefHuidigeVergelijking()
	{
		return formuleVakken[stapNr - 1].geefVergelijking();
	}
	
	public FormuleVak geefLaatsteFormuleVak()
	{
		return formuleVakken[stapNr];
	}

	public void zetStartString(String s)
	{
		formuleVak.vulVak(s);

		stepsForLinKwad = false;
		try
		{
			VergelijkingMeerv v = formuleVak.geefVergelijking();
			int graad = Algebra.geefCoefficienten(v.geefVergelijking(0)).length;
			stepsForLinKwad = graad < 4;
		}
		catch (Exception e)
		{
		}

		if (s.length() > 3)
		{
			formuleVak.setEditable(false);
			stapOk = true;
			if (!linStrategieVersie && !linOefenVersie && !bordjesMethode)
				maakStap();
			else
				formuleVak.setEditable(false);
			hasStartString = true;
			setLog(true);
		}
		else
		{
			hasStartString = false;
		}
	}

	public void zetBewerkingKnoppen(boolean b)
	{
		plusKnop.setVisible(b);
		minKnop.setVisible(b);
		maalKnop.setVisible(b);
		deelKnop.setVisible(b);
		haakjesKnop.setVisible(b);
		herleidKnop.setVisible(b);
	}

	public void zetBewerkingKnoppenExtra(boolean b)
	{
		ontbindKnop.setVisible(b);
		splitsKnop.setVisible(b);
		wortelBewerkKnop.setVisible(b);

	}

	public void zetAbcKnop(boolean b)
	{
		abcKnop.setVisible(b);
	}

	public void zetSubKnop(boolean b)
	{
		subKnop.setVisible(b);
	}

	public void vulVak(String s)
	{
		formuleVak.vulVak(s);
	}

	public String toString()
	{
		return formuleVak.toString();
	}

	protected void zetGoedFout(int uitslag, int formuleVakNr)
	{
		if (!check || balansKoppeling)
			return;
		if (formuleVakNr == -1)
		{
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
			int y = formuleVakken[stapNr].getLocation().y + formuleVakken[stapNr].getSize().height - 5;
			if ("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant))
				y = formuleVakken[stapNr].getLocation().y + formuleVakken[stapNr].getSize().height / 2 - 15;
			huidigIC.setLocation(imageCompX, y);
			huidigIC.setVisible(true);

		}
		else
		{
			if (imageComponenten[formuleVakNr] != null)
				remove(imageComponenten[formuleVakNr]);
			if (uitslag == GOED)
				imageComponenten[formuleVakNr] = new ImageComponent(WiskOpdr.GOEDKRUL);
			else if (uitslag == FOUT)
				imageComponenten[formuleVakNr] = new ImageComponent(WiskOpdr.FOUTKRUIS);
			else if (uitslag == HALF)
				imageComponenten[formuleVakNr] = new ImageComponent(WiskOpdr.HALFKRUL);
			else if (uitslag == GEEN)
				imageComponenten[formuleVakNr] = new ImageComponent(null);
			int y = formuleVakken[formuleVakNr].getLocation().y + formuleVakken[formuleVakNr].getSize().height - 5;
			if ("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant))
				y = formuleVakken[stapNr].getLocation().y + formuleVakken[stapNr].getSize().height / 2 - 15;
			imageComponenten[formuleVakNr].setLocation(imageCompX, y);
			add(imageComponenten[formuleVakNr]);
		}
	}

	private void zetGoedFoutStap(int uitslag, int pijlVakNr)
	{
		if (!check)
			return;

		if (imageComponentenStap[pijlVakNr] != null)
			remove(imageComponentenStap[pijlVakNr]);

		if (uitslag == GOED)
			imageComponentenStap[pijlVakNr] = new ImageComponent(WiskOpdr.GOEDKRUL);
		else if (uitslag == FOUT)
			imageComponentenStap[pijlVakNr] = new ImageComponent(WiskOpdr.FOUTKRUIS);
		else if (uitslag == HALF)
			imageComponentenStap[pijlVakNr] = new ImageComponent(WiskOpdr.HALFKRUL);
		else if (uitslag == GEEN)
			imageComponentenStap[pijlVakNr] = new ImageComponent(null);
		int x = pijlVakken[pijlVakNr].getLocation().x + 20;
		int y = pijlVakken[pijlVakNr].getLocation().y + pijlVakken[pijlVakNr].getSize().height / 2 - 15;
		imageComponentenStap[pijlVakNr].setLocation(x, y);
		add(imageComponentenStap[pijlVakNr]);
	}

	public void zetMode(int mode)
	{
		this.mode = mode;
	}

	public void zetJuisteAntwoord(String s)
	{
		if (s.length() > 3 && s.substring(0, 4).equals("CAS{"))
		{
			casCheck = true;
			return;
		}
		// System.out.println(s);
		if (WiskOpdr.language.getLanguage().equals("en"))
			s = StringUtils.replaceStr(s, "of", "or");
		FormuleParser p = new FormuleParser();
		int index = s.indexOf(";");
		if (index > -1)
		{
			String s1 = s.substring(0, index) + "@";
			//gewensteTussenOplossing = p.parseVergelijking(s1, functieDefSet);
			gewensteTussenOplossing = p.parseVergelijking(s1, functieMVDefSet);
			s = "$f" + s.substring(index + 1);
		}
		//gewensteEindOplossing = p.parseVergelijking(s, functieDefSet);
		gewensteEindOplossing = p.parseVergelijking(s, functieMVDefSet);
	}

	public void zetJuisteVorm(String s)
	{
		s = s.substring(2, s.length() - 1);
		String[] antwoordStrings = StringUtils.split(s, "::");

		juisteVormen = new VergelijkingMeerv[antwoordStrings.length];

		FormuleParser p = new FormuleParser();
		for (int i = 0; i < antwoordStrings.length; i++)
		{
			String antwoordStr = "$f" + antwoordStrings[i] + "@";
			//juisteVormen[i] = p.parseVergelijking(antwoordStr, functieDefSet);
			juisteVormen[i] = p.parseVergelijking(antwoordStr, functieMVDefSet);
		}

	}

	public void stop()
	{
		if (mode == 1)
			return;
		checkAntwoord();
		if (tips && diagnose)
			kijkNaIdeas();
		else
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
			if (formuleVakken[i] != null)
				remove(formuleVakken[i]);
			if (pijlVakken[i] != null)
				remove(pijlVakken[i]);
			if (imageComponenten[i] != null)
				remove(imageComponenten[i]);
		}
		stapNr = 0;
		formuleVakken[0] = new FormuleVak();
		formuleVakken[0].setFont(formuleVakFont);
		formuleVakken[0].addActionListener(this);
		formuleVakken[0].setLocation(formuleVakX, formuleVakY);
		add(formuleVakken[0]);
		formuleVak = formuleVakken[0];
		formuleVakSimpel = formuleVakken[0];
	}

	public void checkStap(int pijlVakNr, VergelijkingMeerv v1, VergelijkingMeerv v2)
	{
		boolean gelijkw = Algebra.zijnGelijkwaardigeVergelijkingen(v1, v2);
		zetGoedFoutStap(gelijkw ? GOED : FOUT, pijlVakNr);
	}
	
	public void checkStap(int pijlVakNr, FormuleVak fv1, FormuleVak fv2)
	{
		VergelijkingMeerv v1 = fv1.geefVergelijking();
		VergelijkingMeerv v2 = fv2.geefVergelijking();
		boolean gelijkw = Algebra.zijnGelijkwaardigeVergelijkingen(v1, v2);
		zetGoedFoutStap(gelijkw ? GOED : FOUT, pijlVakNr);

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
			int start = 0;
			if (hasStartString)
				start = 1;
			if (formuleVakken[stapNr] != null && formuleVakken[stapNr].toString().equals("$f@"))
			{
				if (stapNr > 1)
					stapTerug();
				else if (!hasStartString && stapNr > 0)
					stapTerug();
			}
			int voortgangsScore = 0;
			for (int i = start; i < stapNr + 1; i++)
			{
				formuleVak = formuleVakken[i];
				checkAntwoord();
				//if (stepsForLinKwad && pijl && start > 0)
				if (stepsForLinKwad && start > 0)
				{
					VergelijkingMeerv verg1 = formuleVakken[i - 1].geefVergelijking();
					VergelijkingMeerv verg2 = formuleVakken[i].geefVergelijking();
					
					// check of er een substitutie in het spel is
					if (substitutie != null) // er is een substitutie
					{
						verg1 = verg1.substitueer(substitutie, "p");
						verg2 = verg2.substitueer(substitutie, "p");
						
					}

					checkStap(i - 1, verg1, verg2);
					
					if (i == stapNr)
						kijkNa(i);
				}
				else
					kijkNa(i);
				
				if(hasFeedback)
					voortgangsScore = Math.max(voortgangsScore,score);
			}
			if(hasFeedback)
				score = voortgangsScore;
			if (ingevuld)
				produceAction("changed");
		}
	}

	public JPanel getMWFeedbackComponent()
	{
		return mwFeedbackPanel;
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
		feedbackTekst.setText(tekst);
		feedbackTekst.resize();
		
		if(this instanceof StelselEditor)
			feedbackTekst.setLocation(10, formuleVakken[stapNr].getLocation().y);
			
		feedbackTekst.setLocation(getSize().width - 250, formuleVakken[stapNr].getLocation().y + formuleVakken[stapNr].getSize().height + 10);
		
		if ("MW".equals(WiskOpdr.deployVariant))
		{
			int h = feedbackTekst.getHeight() + 20;
			mwFeedbackPanel.setSize(mwFeedbackPanel.getWidth(), h);
			mwFeedbackPanel.setLocation(getSize().width - 310, formuleVakken[stapNr].getLocation().y + formuleVakken[stapNr].getSize().height + 5);
			feedbackTekst.setLocation(50, 10);
			mwFeedbackPanel.add(feedbackTekst, 0);
			add(mwFeedbackPanel, 0);
		}
		else if ("GR".equals(WiskOpdr.deployVariant))
		{
			feedbackTekst.setOpaque(false);
			int h = feedbackTekst.getHeight() + 40;
			mwFeedbackPanel.setSize(mwFeedbackPanel.getWidth(), h);
			int fbx = Math.min(getSize().width - 220, formuleVakken[stapNr].getLocation().x + formuleVakken[stapNr].getSize().width + 5);
			int fby = formuleVakken[stapNr].getLocation().y + formuleVakken[stapNr].getSize().height + 5;
			mwFeedbackPanel.setLocation(fbx, fby);
			feedbackTekst.setLocation(5, 25);
			mwFeedbackPanel.add(feedbackTekst, 0);
			add(mwFeedbackPanel, 0);
		}
		else if (!(this instanceof StelselEditor))
			add(feedbackTekst, 0);
		produceAction("feedback");
	}

	public void setFeedback(String tekst, boolean closeable, int tipKnopOptie)
	{
		if (tipKnopOptie > 0)
		{
			feedbackTekst.setText("");
			feedbackTekst.setCloseable(closeable);
			feedbackTekst.setSize(feedbackWidth,feedbackHeight);
			feedbackTekst.setLocation(getSize().width - 250, formuleVakken[stapNr].getLocation().y + formuleVakken[stapNr].getSize().height + 10);
			if (tipKnopOptie == 1)
			{
				tip1Knop.setText("Tip");
				tip1Knop.setVisible(true);
				feedbackTekst.setButton("Deze stap is niet correct. "
				// + giveDiagnose()
				+ "Probeer nogmaals of gebruik de knop voor een tip", tip1Knop);
			}
			if (tipKnopOptie == 2)
			{
				tip2Knop.setText("Hulp");
				tip2Knop.setVisible(true);
				feedbackTekst.setButton("Deze stap is nog steeds niet correct. Probeer nogmaals of gebruik de knop voor hulp", tip2Knop);
			}
			if (tipKnopOptie == 3)
			{
				tip2Knop.setText("Hulp");
				tip2Knop.setVisible(true);
				feedbackTekst.setButton(tekst, tip2Knop);
			}

			feedbackTekst.resize();
			add(feedbackTekst, 0);
		}
		else
			setFeedback(tekst, closeable);
		produceAction("feedback");
	}

	public void zetCorrectFoutStap(int stapNr, boolean correct, boolean fout, boolean stapOk, String feedbackKey, boolean show)
	{
		this.correct = correct;
		this.fout = fout;
		if (!show)
			return;
		this.stapOk = stapOk;
		if (correct)
			zetGoedFout(GOED, stapNr);
		else if (!fout)
			zetGoedFout(HALF, stapNr);
		else
			zetGoedFout(FOUT, stapNr);
		if (feedbackKey != null && !feedbackKey.equals(""))
			setFeedback(WiskOpdr.rb.getString(feedbackKey), true);
		else if(this instanceof StelselEditor)
			feedbackTekst.setText("");
			
		//		else if (fout && feedbackModus == 1) {
		//			if (foutenTeller > 0)
		//				setFeedback("", true, foutenTeller);
		//			foutenTeller++;
		//		}
	}

	public void kijkNa(int stapNr)
	{
		kijkNa(stapNr, true);
		
		if(correct && cbookEventHandler.hasListeners("action.correct"))
			cbookEventHandler.fire("action.correct");
		if(fout && cbookEventHandler.hasListeners("action.false"))
			cbookEventHandler.fire("action.false");
		if(fout && getErrorCount()>1 && cbookEventHandler.hasListeners("action.false_2"))
			cbookEventHandler.fire("action.false_2");
	}

	public void kijkNa(int stapNr, boolean show)
	{
		if (hasFeedback)
			checkAntwoordFeedback(show);
		else
			checkAntwoord();
		if (!ingevuld)
		{
			if (show)
				zetGoedFout(GEEN, stapNr);
			if (formuleVak.toString().equals("$f@") && show)
			{
				if ("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant))
					remove(mwFeedbackPanel);
				else
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
					zetGoedFout(GOED, stapNr);
				score = puntenFeedback;
				correct = true;
				fout = false;
				if (show)
					stapOk = false;
				if (!pijl && show)
					stapOk = true;
			}
			else if (goedHalfFout == 1)
			{
				if (show)
					zetGoedFout(HALF, stapNr);
				score = puntenFeedback;
				correct = false;
				fout = false;
				if (show)
					stapOk = true;
			}
			else if (goedHalfFout == 2)
			{
				if (show)
					zetGoedFout(HALF, stapNr);
				score = puntenFeedback;
				correct = false;
				fout = false;
				if (show)
					stapOk = false;
			}
			else if (goedHalfFout == 3)
			{ // zetGoedFout(FOUT,stapNr);
				//if (feedbackModus == 1 && show)
				//	zetCorrectFoutStap(stapNr, false, true, false, "", show);
				//else 
				if (show)
					zetGoedFout(FOUT, stapNr);
				score = puntenFeedback;
				correct = false;
				fout = true;
				if (show)
					stapOk = false;
			}
		}
		else if (isGelijkwaardig)
		{
			if (bevatFouteOplossing) // !isGelijkwaardig && isDeelOplossing &&
										// bevatFouteOplossing
			{
				score = 0;
				zetCorrectFoutStap(stapNr, false, true, false, "", show);// "
				// foutenTeller++;
			}
			else if (vorm)
			{
				if (isJuisteVorm) // isGelijkwaardig && vorm && isJuisteVorm
				{
					score = puntenGelijkwaardig + puntenVorm;
					zetCorrectFoutStap(stapNr, true, false, false, "feedbackTekst16", show);// "Dit is een correcte vergelijking"
				}
				else
				// isGelijkwaardig && vorm && !isJuisteVorm
				{
					score = puntenGelijkwaardig;
					zetCorrectFoutStap(stapNr, false, false, true, "feedbackTekst17", show); // "Deze vergelijking heeft (nog)niet de juiste vorm"
				}
			}
			else if (eindOplossingNodig)
			{
				if (bevatVoldoetNiet) // isGelijkwaardig && eindOplossingNodig
				{
					score = 0;
					zetCorrectFoutStap(stapNr, false, false, false, "feedbackTekst02", show); // "Niet alle oplossingen voldoen aan de oorspronkelijke vergelijking. Verwijder de oplossingen die niet voldoen."
				}
				else if (isEindOplossing)
				{
					if (exactNodig)
					{
						if (isEindOplossingExact) // isGelijkwaardig &&
													// eindOplossingNodig &&
													// isEindOplossing &&
													// exactNodig &&
													// isEindOplossingExact
						{
							score = puntenGelijkwaardig + puntenEindOplossing + puntenSignificant + puntenExact;
							if (gewensteEindOplossing.isOngelijkheid())
							{
								zetCorrectFoutStap(stapNr, true, false, false, "feedbackTekst03", show);// "De ongelijkheid is correct opgelost"
							}
							else if (gewensteEindOplossing.isAfronding())
							{
								zetCorrectFoutStap(stapNr, true, false, false, "feedbackTekst11", show);// "De oplossing is correct afgerond"
							}
							else
							{
								zetCorrectFoutStap(stapNr, true, false, false, "feedbackTekst04", show);// "De vergelijking is correct opgelost"
							}
						}
						else
						// isGelijkwaardig && eindOplossingNodig &&
						// isEindOplossing && exactNodig &&
						// isEindOplossingExact
						{
							if (significantNodig)
							{
								if (isEindOplossingSignificant)
								{
									score = puntenGelijkwaardig + puntenEindOplossing + puntenSignificant;
									zetCorrectFoutStap(stapNr, false, false, true, "feedbackTekst20", show);// "Oplossing is goed, significantie klopt maar heeft nog niet in de juiste vorm."
								}
								else
								{
									score = puntenGelijkwaardig + puntenEindOplossing;
									zetCorrectFoutStap(stapNr, false, false, true, "feedbackTekst19", show);// "Oplossing is goed, maar nog niet in de juiste vorm en de significantie klopt niet."
								}
							}
							else
							{
								score = puntenGelijkwaardig + puntenEindOplossing;
								zetCorrectFoutStap(stapNr, false, false, true, "feedbackTekst10", show);// "Oplossing is goed, maar nog niet in de juiste vorm."
							}
						}
					}
					else
					// isGelijkwaardig && eindOplossingNodig &&
					// isEindOplossing && ! exactNodig
					{

						score = puntenGelijkwaardig + puntenEindOplossing;
						if (gewensteEindOplossing.isOngelijkheid())
						{
							zetCorrectFoutStap(stapNr, true, false, false, "feedbackTekst03", show);// "De ongelijkheid is correct opgelost"
						}
						else if (gewensteEindOplossing.isAfronding())
						{
							zetCorrectFoutStap(stapNr, true, false, false, "feedbackTekst11", show);// "De oplossing is correct afgerond"
						}
						else
						{

							if (significantNodig)
							{
								if (isEindOplossingSignificant)
								{
									score = puntenGelijkwaardig + puntenEindOplossing + puntenSignificant;
									zetCorrectFoutStap(stapNr, true, false, false, "feedbackTekst04", show);// "De vergelijking is correct opgelost"
								}
								else
								{
									score = puntenGelijkwaardig + puntenEindOplossing;
									zetCorrectFoutStap(stapNr, false, false, true, "feedbackTekst18", show);// "De oplossing is goed, maar het aantal significante cijfers klopt niet."
								}
							}
							else
							{
								zetCorrectFoutStap(stapNr, true, false, false, "feedbackTekst04", show);// "De vergelijking is correct opgelost"
							}
						}
					}

				}
				else
				// isGelijkwaardig && eindOplossingNodig &&
				// !isEindOplossing
				{
					if (moetNogAfgerond) // isGelijkwaardig &&
											// eindOplossingNodig &&
											// !isEindOplossing
					{
						score = 0;
						zetCorrectFoutStap(stapNr, false, false, true, "feedbackTekst05", show); // "Geef de gevraagde afronding"
					}
					else if (moetNogOngelijkheid) // isGelijkwaardig &&
													// eindOplossingNodig &&
													// !isEindOplossing
					{
						score = 0;
						zetCorrectFoutStap(stapNr, false, false, true, "feedbackTekst06", show); // "Geef nu de oplossing(en) van de ongelijkheid"
					}
					else
					{
						score = puntenGelijkwaardig;
						zetCorrectFoutStap(stapNr, false, false, true, "", show);
					}
				}
			}
			else
			// isGelijkwaardig && !vorm && !eindOplossingNodig
			{
				score = puntenGelijkwaardig;
				// zetCorrectFoutStap(stapNr,true,false,false,"feedbackTekst16");//"Dit is een correcte vergelijking"

				// Nu kan het vak gebruikt worden als 'balans' voor het checken
				// van ware beweringen
				zetCorrectFoutStap(stapNr, true, false, true, "feedbackTekst16", show);// "Dit is een correcte vergelijking"
			}
		}

		else
		// niet isGelijkwaardig
		{
			if (isDeelOplossing)
			{
				if (bevatFouteOplossing) // !isGelijkwaardig && isDeelOplossing
											// && bevatFouteOplossing
				{
					score = 0;
					zetCorrectFoutStap(stapNr, false, true, false, "feedbackTekst01", show);// "Deze stap bevat correcte en niet correcte onderdelen. Verwijder of vervang de delen die niet correct zijn"
				}
				else
				// !isGelijkwaardig && isDeelOplossing &&
				// !bevatFouteOplossing
				{
					score = 0;
					zetCorrectFoutStap(stapNr, false, true, false, "feedbackTekst07", show);// "Er ontbreken oplossingen. Vul aan."
				}
			}
			else
			// niet isDeelOplossing
			{
				score = 0;
				zetCorrectFoutStap(stapNr, false, true, false, "", show);
			}
		}

	}

	// alleen gebruikt bij linOefenVersie
	public boolean isJuistUitgevoerdeStap()
	{
		if (stapNr == 0)
			return isGelijkwaardig;
		String op = pijlVakken[stapNr - 1].geefOperator();
		pijlVakken[stapNr - 1].formuleVak.setEditable(false);
		Expressie en = pijlVakken[stapNr - 1].formuleVak.geefExpressie();
		if (op.equals("implicatie") ||op.equals("abc") || en == null)
			return isGelijkwaardig;
		VergelijkingMeerv verg = formuleVakken[stapNr - 1].geefVergelijking();

		VergelijkingMeerv vergNieuw = null;

		int aantalDelen = verg.geefAantal();
		for (int i = 0; i < aantalDelen && aantalDelen > 0; i++)
		{
			if (formuleVakken[stapNr - 1].partEquationSelected(i))
			{
				vergNieuw = verg.bewerkVergelijking(op, en, i);
				break;
			}
		}
		if (vergNieuw == null)
			vergNieuw = verg.bewerkVergelijking(op, en);

		VergelijkingMeerv vergAntwoord = formuleVakken[stapNr].geefVergelijking();
		if (op.equals("sub"))
			vergAntwoord = vergAntwoord.substitueer(substitutie, "p");
		return vergNieuw.isGelijkMet(vergAntwoord);
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

			boolean pastGelijkwaardig = isGelijkwaardig && !bevatFouteOplossing;
			boolean pastVorm = isJuisteVorm;
			boolean pastEindAntwoord = isEindOplossing;
			boolean pastExact = isEindOplossingExact;
			boolean pastSignificant = isEindOplossingSignificant;

			if (!gelijkwaardigP)
				pastGelijkwaardig = true;
			if (!vormP)
				pastVorm = true;
			if (!eindOplossingNodigP)
				pastEindAntwoord = true;
			if (!exactP)
				pastExact = true;
			if (!significantP)
				pastSignificant = true;

			boolean answerModelFits = pastGelijkwaardig && pastVorm && pastEindAntwoord && pastExact && pastSignificant;
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
			return;

		if (casCheck)
			;

		Algebra.setTestValues(eqTestValueMin, eqTestValueMax);
		ingevuld = false;
		if (!tips)
		{
			if ("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant))
				remove(mwFeedbackPanel);
			else
				remove(feedbackTekst);
		}
		VergelijkingMeerv antwoord = null;
		VergelijkingMeerv antwoordGeen = null;
		VergelijkingMeerv antwoordAlles = null;
		if (formuleVak.toString().length() > 6 && formuleVak.toString().substring(2, 6).equals("geen") || formuleVak.toString().length() > 4 && formuleVak.toString().substring(2, 4).equals("no"))
		{
			geenOplossing = true;
			// Vergelijking v = new Vergelijking(new BasisExpressie("x"), new
			// BasisExpressie(0.1234567));
			Vergelijking v = new Vergelijking(new BasisExpressie(gewensteEindOplossing.geefVergelijkingVar()), new BasisExpressie(0.1234567));
			Vergelijking[] vn = new Vergelijking[1];
			vn[0] = v;
			antwoordGeen = new VergelijkingMeerv(vn);
		}
		if (formuleVak.toString().length() > 7 && formuleVak.toString().substring(2, 7).equals("alles") || formuleVak.toString().length() > 5 && formuleVak.toString().substring(2, 5).equals("all"))
		{
			geenOplossing = true;
			// Vergelijking v = new Vergelijking(new BasisExpressie("x"), new
			// BasisExpressie(0.1234567));
			Vergelijking v = new Vergelijking(new BasisExpressie(gewensteEindOplossing.geefVergelijkingVar()), new BasisExpressie(0.7654321));
			Vergelijking[] vn = new Vergelijking[1];
			vn[0] = v;
			antwoordAlles = new VergelijkingMeerv(vn);
		}
		
		String formuleVakString = formuleVak.toString();
		
//		//laatste stap oplossen vergelijking soms van de vorm: x = expressie = eindexpressie
//		String[] antwoordVergStringParts = formuleVakString.split("=");
//		if(antwoordVergStringParts.length==3) {
//			VergelijkingMeerv vergMeerv = FormuleParser.parseVergelijking(formuleVakString, functieMVDefSet);
//			if(vergMeerv==null) {
//				String newString1 = antwoordVergStringParts[0]+"="+antwoordVergStringParts[2];
//				String newString2 = antwoordVergStringParts[0]+"="+antwoordVergStringParts[1]+"@";
//				VergelijkingMeerv newStringVerg1 = FormuleParser.parseVergelijking(newString1, functieMVDefSet);
//				VergelijkingMeerv newStringVerg2 = FormuleParser.parseVergelijking(newString2, functieMVDefSet);
//				if(newStringVerg1!=null && newStringVerg2!=null)
//					formuleVakString = newString1;
//			}
//		}
		
		//System.out.println("formuleVakStringVoor " + formuleVakString);
		if(antwoordStringSubstituties != null)
		{
			for (int i = 0; i < antwoordStringSubstituties.length; i++)
			{	int index = antwoordStringSubstituties[i].indexOf('=');
				if(index==-1) 
				{	break;
				}
				String toSubstString = antwoordStringSubstituties[i].substring(2, index);
				String substString = antwoordStringSubstituties[i].substring(index+1,antwoordStringSubstituties[i].length()-1 );
				toSubstString = toSubstString.trim();
				substString = substString.trim();
				
				//System.out.println("toSubstString" + toSubstString.trim());
				//System.out.println("substString" + substString.trim());
				formuleVakString = StringUtils.replaceStr(formuleVakString, toSubstString,substString);
			}
			
			/*for (Enumeration en = antwoordStringSubstituties.keys(); en.hasMoreElements();)
			{	String aKey = (String)en.nextElement();
				System.out.println("aKey " + aKey);
				String aValue = (String)antwoordStringSubstituties.get(aKey);
				System.out.println("aValue " + aValue);
				formuleVakString = StringUtils.replaceStr(formuleVakString,aKey,aValue);
			}*/
		}
		
		//VergelijkingMeerv antwoordIngevuld = formuleVak.geefVergelijking();
		//VergelijkingMeerv antwoordIngevuld = FormuleParser.parseVergelijking(formuleVakString, functieDefSet);
		VergelijkingMeerv antwoordIngevuld = FormuleParser.parseVergelijking(formuleVakString, functieMVDefSet);
		
		antwoord = antwoordIngevuld;
		if (antwoord == null)
		{
			antwoord = antwoordGeen;
			antwoordIngevuld = antwoordGeen;
		}
		if (antwoord == null)
		{
			antwoord = antwoordAlles;
			antwoordIngevuld = antwoordAlles;
		}

		if (substitutie != null && antwoordIngevuld != null)
			antwoord = antwoordIngevuld.substitueer(substitutie, "p");

		updateGebruikersSubstituties();
		if (gebruikersSubstituties != null && antwoord != null)
		{
			for (int i = 0; i < gebruikersSubstituties.length; i++)
			{
				antwoord = antwoord.substitueer(gebruikersSubstituties[i].geefExpRechts(), gebruikersSubstituties[i].geefExpLinks().geefVarNaam());
			}
		}
		
		String var = "x";
		if (gewensteEindOplossing != null)
			var = gewensteEindOplossing.geefVergelijkingVar();
		//System.out.println("checkAntwoord: var = " + var);

		if (antwoordSubstituties != null && antwoord != null)
		{
			for (int i = 0; i < antwoordSubstituties.length; i++)
			{
				//dit onderscheid blijven maken, voor het geval een variabele zowel gesubstitueerd zou kunnen worden als als eindvariabele gebruikt zou kunnen worden. 
				//bijvoorbeeld in werk van Harm Houwing voor oplossen van stelsels vergelijkingen.
				if(antwoord.isEindOplossing(var))
					antwoord = antwoord.substitueerEindOplossing(antwoordSubstituties[i].geefExpRechts(), antwoordSubstituties[i].geefExpLinks().geefVarNaam());
				else	
					antwoord = antwoord.substitueer(antwoordSubstituties[i].geefExpRechts(), antwoordSubstituties[i].geefExpLinks().geefVarNaam());
			}
		}

		if (antwoord != null)
		{ // if(tips)
			// { ingevuld = true;
			// return;
			// }
			if (!geenOplossing)
				if (!bordjesMethode)
				{
					String antwoordIngevuldString = antwoordIngevuld.toString();
					//System.out.println("antwoordIngevuldVoor " + antwoordIngevuldString);
					if(antwoordStringSubstituties != null)
					{
						for (int i = 0; i < antwoordStringSubstituties.length; i++)
						{	int index = antwoordStringSubstituties[i].indexOf('=');
							if(index==-1) 
							{	break;
							}
							String toSubstString = antwoordStringSubstituties[i].substring(2, index);
							String substString = antwoordStringSubstituties[i].substring(index+1,antwoordStringSubstituties[i].length()-1 );
							toSubstString = toSubstString.trim();
							substString = substString.trim();
							//System.out.println("toSubstString" + toSubstString.trim());
							//System.out.println("substString" + substString.trim());
							antwoordIngevuldString = StringUtils.replaceStr(antwoordIngevuldString,substString,toSubstString);
						}
						/*for (Enumeration en = antwoordStringSubstituties.keys(); en.hasMoreElements();)
						{	String aKey = (String)en.nextElement();
							String aValue = (String)antwoordStringSubstituties.get(aKey);
							antwoordIngevuldString = StringUtils.replaceStr(antwoordIngevuldString,aValue,aKey);
						}*/
					}
					//System.out.println("antwoordIngevuldNa " + antwoordIngevuldString);
					
					formuleVak.vulVak("$f" + antwoordIngevuldString + "@");
					//System.out.println("na vulvak");
					huidigeVergelijking = antwoord;
					sendCommand("balansvergelijking");
					sendCommand("equation");
					
				}
			//System.out.println("$f" + antwoordIngevuld.toString() + "@");
			ingevuld = true;
			
			//String var = "x";
			//if (gewensteEindOplossing != null)
			//	var = gewensteEindOplossing.geefVergelijkingVar();

			String diffVar = "x";
			for(int i = 0; i < antwoord.geefAantal(); i++)
			{	String diffVar2 = antwoord.geefVergelijking(i).geefVarNaam();
				if(diffVar2 != null && !diffVar2.equals(""))
				{	diffVar = diffVar2;
					break;
				}
			}
			if(FormuleParser.isDiffOperatoren())
			{	antwoord = antwoord.vervangDifferentialen(diffVar);
				antwoord = antwoord.vervangDiffs(gewensteEindOplossing.geefEindOplossingen(var), var);
			}
			//antwoord = antwoord.berekenDiffs(gewensteEindOplossing.geefEindOplossingen(var), var, diffVar);
			
			// TODO voor vector/matrix-vergelijking is dit altijd false als er meer dan 1 variabele zijn
			boolean isGelijkwaardigEind = antwoord.isOplossing(gewensteEindOplossing.geefEindOplossingen(var), var, gewensteEindOplossing.geefVergTekens());
			

			// Hiermee wordt, in geval er geen eindoplossing is, maar wel een
			// voorlopige tussenoplossing, aan het eind gevraagd de oplossing te
			// verwerpen
			if (gewensteEindOplossing.isOplossing(0.1234567))
				isGelijkwaardigEind = true;
			//

			isGelijkwaardig = isGelijkwaardigEind;
			if (gewensteTussenOplossing != null && !isGelijkwaardig)
				isGelijkwaardig = antwoord.isOplossing(gewensteTussenOplossing.geefEindOplossingen(var), var, gewensteTussenOplossing.geefVergTekens());

			if (antwoord.isVectorVergelijking())
				isEindOplossing = isGelijkwaardigEind && antwoord.isVectorEindOplossing(var);
			else if (antwoord.isMatrixVergelijking())
				isEindOplossing = isGelijkwaardigEind && antwoord.isMatrixEindOplossing(var);
			else
				isEindOplossing = isGelijkwaardigEind && antwoord.isEindOplossing(var);

			isEindOplossingSignificant = isGelijkwaardigEind && antwoord.isEindOplossingSignificant(gewensteEindOplossing.geefEindOplossingen(var), var, gewensteEindOplossing.geefVergTekens());

			isEindOplossingExact = isGelijkwaardigEind && antwoord.isEindOplossingExact(gewensteEindOplossing.geefEindOplossingen(var), var, gewensteEindOplossing.geefVergTekens());

			isDeelOplossing = antwoord.isDeelOplossing(gewensteEindOplossing.geefEindOplossingen(var), var, gewensteEindOplossing.geefVergTekens());
			if (gewensteTussenOplossing != null && !isDeelOplossing)
				isDeelOplossing = antwoord.isDeelOplossing(gewensteTussenOplossing.geefEindOplossingen(var), var, gewensteTussenOplossing.geefVergTekens());

			boolean bevatFouteOplossingEind = antwoord.bevatFouteOplossing(gewensteEindOplossing, var, gewensteEindOplossing.geefVergTekens());
			bevatFouteOplossing = bevatFouteOplossingEind;
			if (gewensteTussenOplossing != null && bevatFouteOplossing)
				bevatFouteOplossing = antwoord.bevatFouteOplossing(gewensteTussenOplossing, var, gewensteTussenOplossing.geefVergTekens());

			bevatVoldoetNiet = bevatFouteOplossingEind && !bevatFouteOplossing && isEindOplossing;
			// System.out.println(""+bevatVoldoetNiet);

			moetNogAfgerond = isGelijkwaardig && !isGelijkwaardigEind && antwoord.isEindOplossing(var) && gewensteEindOplossing.toString().indexOf("\u2248") > -1;

			moetNogOngelijkheid = isGelijkwaardig && !isGelijkwaardigEind && antwoord.isEindOplossing(var) && gewensteEindOplossing.isOngelijkheid();

			isJuisteVorm = false;
			for (int i = 0; i < juisteVormen.length; i++)
			{
				//isJuisteVorm = isJuisteVorm || Algebra.gelijkGevormd(antwoord, juisteVormen[i]); //in plaats hiervan antwoordIngevuld gebruiken, omdat met antwoord allerlei substituties kunnen zijn uitgevoerd.
				if (Algebra.isJuistFormaatVectorVoorstelling(juisteVormen[i])) // als de gewenste vorm een vectorvoorstelling is
				{
					isJuisteVorm = isJuisteVorm || Algebra.isJuisteVectorvoorstelling(antwoordIngevuld, gewensteEindOplossing, juisteVormen[i]);
					if (isJuisteVorm) // de juiste vectorvoorstelling is het goede antwoord
					{
						isGelijkwaardig = true;
						bevatFouteOplossing = false;
						isEindOplossing = true;
					}
				}
				else
					isJuisteVorm = isJuisteVorm || Algebra.gelijkGevormd(antwoordIngevuld, juisteVormen[i]);
				
				if (isJuisteVorm)
					break;
			}
			repaint();

			if (linOefenVersie)
			{
				isGelijkwaardig = isJuistUitgevoerdeStap();
				if (!isGelijkwaardig)
				{
					isEindOplossingExact = false;
					isEindOplossing = false;
					isDeelOplossing = false;
				}
			}
		}
		else
		{
			isGelijkwaardig = false;
			isEindOplossing = false;
			isEindOplossingExact = false;
			isDeelOplossing = false;
			bevatFouteOplossing = false;
			bevatVoldoetNiet = false;
			if (formuleVak.toString().indexOf("|") > -1)
			{ // setFeedback("Gebruik geen absoluut strepen ( bv: |x-3| )");
				setFeedback(WiskOpdr.rb.getString("feedbackTekst08"), false);
			}
			else if (formuleVak.toString().length() > 3)
			{ // setFeedback("De notatie van de vergelijking of oplossingen is niet juist");
				if (mode == 2 || mode == 3)
					ingevuld = true;
				setFeedback(WiskOpdr.rb.getString("feedbackTekst09"), false);
			}
		}
		Algebra.setDefaultTestValues();
	}

	public void updateGebruikersSubstituties()
	{
		String[] gebruikersSubstitutieStrings = gebruikersSubstitutiesVak.geefRegels();
		if (gebruikersSubstitutieStrings != null)
		{
			boolean subCorrect = true;
			gebruikersSubstituties = new Vergelijking[gebruikersSubstitutieStrings.length];
			for (int i = 0; i < gebruikersSubstitutieStrings.length; i++)
			{
				try
				{
					//gebruikersSubstituties[i] = (FormuleParser.parseVergelijking(gebruikersSubstitutieStrings[i], functieDefSet)).geefVergelijking(0);
					gebruikersSubstituties[i] = (FormuleParser.parseVergelijking(gebruikersSubstitutieStrings[i], functieMVDefSet)).geefVergelijking(0);
					if (!gebruikersSubstituties[i].geefExpLinks().isVar())
						subCorrect = false;
				}
				catch (Exception e)
				{
					subCorrect = false;
				}
			}
			if (!subCorrect)
				gebruikersSubstituties = null;
		}
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
		//if (feedbackModus == 1)
		//	return Math.max(0, score - aftrekTipHulp);
		if (tips)
			return Math.max(0, score - ideasPuntenAftrek);
		if(mode==1)
			return Math.max(0, score-errorCount*2);
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
				if(logObjectives[i][j] && mode==1)
					scoreObjectives[i][j] = score - errorCount*2;
				else if(logObjectives[i][j] && tips)
					scoreObjectives[i][j] = Math.max(0, score - ideasPuntenAftrek);
				else if(logObjectives[i][j])
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
	
	public int getErrorCount()
	{	return errorCount;
	}

	public void maakStap()
	{
		if (!stappen)
			return;

		foutenTeller = 0;
		//tipGebruikt = false;
		//hulpGebruikt = false;
		if (!stapOk && mode != 2 && mode != 3)
		{
			nagekeken = false;
			maakStap("implicatie");
		}
		else if (stapOk || mode == 2 || mode == 3)
		{
			nagekeken = false;
			stapOk = false;
			pijlVakken[stapNr] = new PijlVak("implicatie");
			int y = formuleVakken[stapNr].getLocation().y + formuleVakken[stapNr].getSize().height / 2;
			pijlVakken[stapNr].setLocation(getSize().width - pijlX, y);
			//if (pijl)
				add(pijlVakken[stapNr]);
				pijlVakken[stapNr].setPijlVisible(pijl);
			pijlVak = pijlVakken[stapNr];

			// formuleVakken[stapNr].setEditable(false);
			if ((mode != 2 && mode != 3) || (hasStartString && stapNr == 0))
				formuleVakken[stapNr].setEditable(false);

			formuleVakken[stapNr + 1] = new FormuleVak();
			formuleVakken[stapNr + 1].setFont(formuleVakFont);
			y = formuleVakken[stapNr].getLocation().y + formuleVakken[stapNr].getSize().height + stapH;
			int x = formuleVakX;
			formuleVakken[stapNr + 1].setLocation(x, y);
			formuleVakken[stapNr + 1].addActionListener(this);
			add(formuleVakken[stapNr + 1]);

			formuleVak = formuleVakken[stapNr + 1];
			formuleVakSimpel = formuleVakken[stapNr + 1];

			stapNr++;
			formuleVak.requestFocus();
		}
	}

	private void maakStap(String operator)
	{
		nagekeken = false;
		foutenTeller = 0;
		//tipGebruikt = false;
		//hulpGebruikt = false;
		if (stapNr == 0 && formuleVakken[stapNr] != null && formuleVakken[stapNr].toString().equals("$f@"))
			return;
		if (!stapOk && formuleVakken[stapNr] != null && !formuleVakken[stapNr].toString().equals("$f@"))
			return;
		if (operator.equals("implicatie"))
		{
			if (formuleVakken[stapNr] != null)
				remove(formuleVakken[stapNr]);
			if (pijlVakken[stapNr - 1] != null)
				remove(pijlVakken[stapNr - 1]);
			pijlVakken[stapNr - 1] = new PijlVak(operator);
			int y = formuleVakken[stapNr - 1].getLocation().y + formuleVakken[stapNr - 1].getSize().height / 2;
			pijlVakken[stapNr - 1].setLocation(getSize().width - pijlX, y);
			//if (pijl)
				add(pijlVakken[stapNr - 1]);
				pijlVakken[stapNr - 1].setPijlVisible(pijl);
			pijlVak = pijlVakken[stapNr - 1];

			formuleVakken[stapNr] = new FormuleVak();
			formuleVakken[stapNr].setFont(formuleVakFont);
			int x = formuleVakX;
			y = formuleVakken[stapNr - 1].getLocation().y + formuleVakken[stapNr - 1].getSize().height + stapH;
			formuleVakken[stapNr].setLocation(x, y);
			formuleVakken[stapNr].addActionListener(this);
			add(formuleVakken[stapNr]);

			formuleVak = formuleVakken[stapNr];
			formuleVak.requestFocus();
		}
		else
		{
			if (stapOk)
				stapNr++;
			stapOk = false;
			if (formuleVakken[stapNr] != null)
				remove(formuleVakken[stapNr]);
			if (pijlVakken[stapNr - 1] != null)
				remove(pijlVakken[stapNr - 1]);
			pijlVakken[stapNr - 1] = new PijlVak(operator);
			pijlVakken[stapNr - 1].addActionListener(this);
			int y = formuleVakken[stapNr - 1].getLocation().y + formuleVakken[stapNr - 1].getSize().height / 2;
			pijlVakken[stapNr - 1].setLocation(getSize().width - pijlX, y);
			if (operator.equals("abc") || operator.equals("sub"))
				pijlVakken[stapNr - 1].setLocation(getSize().width - pijlX - 30, y);
			if ("GR".equals(WiskOpdr.deployVariant) && (operator.equals("abc") || operator.equals("sub")))
				pijlVakken[stapNr - 1].setLocation(getSize().width - pijlX - 60, y);
			//if (pijl)
				add(pijlVakken[stapNr - 1]);
				pijlVakken[stapNr - 1].setPijlVisible(pijl);
			pijlVak = pijlVakken[stapNr - 1];
			formuleVak = pijlVak.formuleVak;
			pijlVak.requestFocus();

		}
		repaint();
	}

	private void maakBewerkingStap()
	{
		if (fout)
			return;
		String operator = pijlVak.geefOperator();
		pijlVak.formuleVak.setEditable(false);
		Expressie en = pijlVak.formuleVak.geefExpressie();
		VergelijkingMeerv verg = formuleVakken[stapNr - 1].geefVergelijking();

		// System.out.println("foute vergelijiking?"+formuleVakken[stapNr-1].toString());
		// System.out.println(verg.toString());
		VergelijkingMeerv vergNieuw = null;

		if (linOefenVersie || linStrategieVersie)
		{
			int aantalDelen = verg.geefAantal();
			for (int i = 0; i < aantalDelen && aantalDelen > 0; i++)
			{
				if (formuleVakken[stapNr - 1].partEquationSelected(i))
				{
					vergNieuw = verg.bewerkVergelijking(operator, en, i);
					break;
				}

			}
			if (vergNieuw == null)
				vergNieuw = verg.bewerkVergelijking(operator, en);
		}
		else
			vergNieuw = verg.bewerkVergelijking(operator, en);

		// System.out.println(vergNieuw.toString());

		int x = pijlVak.getLocation().x;
		int y = pijlVak.getLocation().y;
		/*
		 * if(!operator.equals("abc") && !operator.equals("sub") &&
		 * !linStrategieVersie && !linOefenVersie) { remove(pijlVak);
		 * pijlVakken[stapNr-1] = new PijlVak("implicatie"); pijlVak =
		 * pijlVakken[stapNr-1]; pijlVak.setLocation(x,y); if(pijl)add(pijlVak);
		 * }
		 */

		if ((mode != 2 && mode != 3) || (hasStartString && stapNr - 1 == 0))
			formuleVakken[stapNr - 1].setEditable(false);
		// formuleVakken[stapNr-1].setEditable(false);
		if (formuleVakken[stapNr] != null)
			remove(formuleVakken[stapNr]);
		formuleVakken[stapNr] = new FormuleVak();
		formuleVakken[stapNr].setFont(formuleVakFont);

		// y = formuleVakken[stapNr-1].getLocation().y +
		// formuleVakken[stapNr-1].getSize().height +
		// pijlVakken[stapNr-1].getHeight();
		y = formuleVakken[stapNr - 1].getLocation().y + formuleVakken[stapNr - 1].getSize().height + stapH;
		x = formuleVakX;
		formuleVakken[stapNr].setLocation(x, y);
		formuleVakken[stapNr].addActionListener(this);
		add(formuleVakken[stapNr]);

		formuleVak = formuleVakken[stapNr];
		if (!verg.toString().equals(vergNieuw.toString()) || linStrategieVersie)
		{
			if (!linOefenVersie)
			{
				formuleVak.vulVak("$f" + vergNieuw.toString() + "@");
				formuleVak.finish();
			}
		}

		formuleVak.requestFocus();

	}

	public void stapLeegTerug()
	{
		if (formuleVakken[stapNr] != null && formuleVakken[stapNr].toString().equals("$f@"))
		{
			if (stapNr > 1)
				stapTerug();
			else if (!hasStartString && stapNr > 0)
				stapTerug();
		}
	}

	public void stapTerug()
	{
		nagekeken = false;
		if (stapNr > 0)
		{
			remove(formuleVakken[stapNr]);
			remove(pijlVakken[stapNr - 1]);
			if (pijlVakken[stapNr - 1].geefOperator().equals("sub"))
				substitutie = null;
			if ("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant))
				remove(mwFeedbackPanel);
			else
				remove(feedbackTekst);
			if (stapNr > 1 || !hasStartString)
				formuleVakken[stapNr - 1].setEditable(true);
			if (bordjesMethode && stapNr > 0)
				formuleVakken[stapNr - 1].setSelectable(true);

			formuleVak = formuleVakken[stapNr - 1];
			formuleVakSimpel = formuleVakken[stapNr - 1];
			stapNr--;
			stapOk = false;
			if ((mode == 0 || mode == 1) && (stapNr > 0 || !hasStartString))
			{
				if (tips && diagnose)
					kijkNaIdeas();
				else
				{	if(this instanceof StelselEditor)
						((StelselEditor) this).kijkNa(-1, true, true);
					else
						kijkNa();
				}
			}
			else
			{
				stapOk = true;
				if (mode == 0 || mode == 1)
					zetGoedFout(GEEN, -1);
				else
				{
					zetGoedFout(GEEN, stapNr + 1);
					zetGoedFoutStap(GEEN, stapNr);
				}

			}
			if (linStrategieVersie)
			{
				zetGoedFout(GEEN, -1);
				formuleVak.setEditable(false);
				fout = false;
			}
			produceAction("changed");
		}
	}

	public void kijkNaIdeas()
	{
		if (WiskOpdr.ideas == null)
		{
			JOptionPane.showMessageDialog(this, "Feedbackservice not available");
			return;
		}
		score = 0;
		if (stapNr < 1)
			return;
		FormuleVak fvVorig = formuleVakken[stapNr - 1];
		FormuleVak fvHuidig = formuleVakken[stapNr];
		if (formuleVak.geefVergelijking() != null && isGelijkwaardig)
			fvHuidig = formuleVak;

		boolean geenOplossing = false;
		VergelijkingMeerv antwoordGeen = null;

		if (fvHuidig.toString().equals("$f@"))
			return;
		else if (fvHuidig.toString().length() > 6 && fvHuidig.toString().substring(2, 6).equals("geen") || fvHuidig.toString().length() > 4 && fvHuidig.toString().substring(2, 4).equals("no"))
		{
			geenOplossing = true;
			// Vergelijking v = new Vergelijking(new BasisExpressie("x"), new
			// BasisExpressie(0.1234567));
			Vergelijking v = new Vergelijking(new BasisExpressie(fvVorig.geefVergelijking().geefVergelijkingVar()), new BasisExpressie(0.1234567));
			Vergelijking[] vn = new Vergelijking[1];
			vn[0] = v;
			antwoordGeen = new VergelijkingMeerv(vn);
		}
		else if (fvHuidig.toString().length() > 7 && fvHuidig.toString().substring(2, 7).equals("alles") || fvHuidig.toString().length() > 5 && fvHuidig.toString().substring(2, 5).equals("all"))
		{
			geenOplossing = true;
			// Vergelijking v = new Vergelijking(new BasisExpressie("x"), new
			// BasisExpressie(0.1234567));
			Vergelijking v = new Vergelijking(new BasisExpressie(fvVorig.geefVergelijking().geefVergelijkingVar()), new BasisExpressie(0.7654321));
			Vergelijking[] vn = new Vergelijking[1];
			vn[0] = v;
			antwoordGeen = new VergelijkingMeerv(vn);
		}
		else if (fvHuidig.geefVergelijking() == null)
		{
			setFeedback(WiskOpdr.rb.getString("feedbackTekst09"), false);
			correct = false;
			fout = false;
			repaint();
			return;
		}

		ingevuld = true;
		// if(feedbackTekst!=null && getParent()==null &&
		// feedbackTekst.getParent()!=null)
		// { remove(feedbackTekst);
		// produceAction("feedbackWeg");
		// repaint();
		// }
		if (feedbackTekst != null)
		{
			if ("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant))
				removeSoft(mwFeedbackPanel);
			else
				removeSoft(feedbackTekst);
		}

		String vglStriktVorig = fvVorig.geefVergelijking().toStringStrikt();
		vglStriktVorig = vertaalNaarIdeasExpressie(vglStriktVorig);
		String vglStriktHuidig = null;
		if (antwoordGeen != null)
			vglStriktHuidig = antwoordGeen.toStringStrikt();
		else
			vglStriktHuidig = fvHuidig.geefVergelijking().toStringStrikt();
		// System.out.println(vglStriktHuidig);
		vglStriktHuidig = vertaalNaarIdeasExpressie(vglStriktHuidig);

		// System.out.println("diagnose("+vglStriktVorig+","+vglStriktHuidig+","+strategieDomein+")");

		RuleIF rule = WiskOpdr.ideas.diagnose(vglStriktVorig, vglStriktHuidig, strategieDomein);
		// System.out.println(rule.getName());

		String feedback = translateRule(rule.getName());
		if (rule.getName().equals("notequiv"))
		{
			feedback = translateRule(rule.getName());
			zetGoedFout(FOUT, -1);
			stapOk = false;
			correct = false;
			fout = true;
		}
		else if (rule.getName().equals("buggy"))
		{
			if (feedbackBijFout)
				feedback = formatRuleText(rule, translateRule(rule.getId()));
			zetGoedFout(FOUT, -1);
			stapOk = false;
			correct = false;
			fout = true;
		}
		else if (rule.isReady())
		{
			feedback = translateRule("ready");
			zetGoedFout(GOED, -1);
			stapOk = false;
			if (!hasFeedback)
				correct = true;
			fout = false;
			score = puntenExact + puntenEindOplossing + puntenGelijkwaardig;
		}
		else if (rule.getName().equals("similar"))
		{
			feedback = translateRule(rule.getName());
			zetGoedFout(GEEN, -1);
			stapOk = false;
			correct = false;
			fout = false;
		}

		else if (rule.getName().equals("expected"))
		{
			feedback = translateRule(rule.getName());
			zetGoedFout(HALF, -1);
			stapOk = true;
			correct = false;
			fout = false;
		}
		else if (rule.getName().equals("detour"))
		{
			feedback = translateRule(rule.getName());
			zetGoedFout(HALF, -1);
			stapOk = true;
			correct = false;
			fout = false;
		}
		else if (rule.getName().equals("correct"))
		{
			feedback = translateRule(rule.getName());
			zetGoedFout(HALF, -1);
			stapOk = true;
			correct = false;
			fout = false;
		}

		if (fout && tipBijFout)
		{
			RuleIF rules = WiskOpdr.ideas.getOneFirst(vglStriktVorig, strategieDomein);
			if (hulpBijTip)
				setFeedback(feedback + "\n\nTip: \n" + formatRuleText(rules, translateRule(rules.getId())) + "\n", true, 3);
			else
				setFeedback("Tip: \n" + formatRuleText(rules, translateRule(rules.getId())) + "\n", true);
			repaint();

		}
		else
		{
			setFeedback(feedback, true);
			repaint();
		}

		if (hasFeedback)
		{
			kijkNa();
			if (rule.getName().equals("similar"))
				stapOk = false;
		}
		else
		{
			if (ingevuld)
				produceAction("changed");
		}
	}

	public void actionPerformed(ActionEvent e)
	{
		super.actionPerformed(e);

		if (e.getSource() == formuleVak && bordjesMethode)
		{
			String s = "";
			s = e.getActionCommand();
			int index = s.indexOf("=");
			if (index > -1)
				s = s.substring(0, index) + "=@";
			if (stapOk && s.length() > 1 && s.substring(0, 2).equals("$f"))
			{
				maakStap();
				formuleVak.vulVak(s);
				return;
			}
			else if (!stapOk && stapNr > 0 && s.length() > 1 && s.substring(0, 2).equals("$f"))
			{
				stapTerug();
				maakStap();
				if (formuleVak != e.getSource())
					formuleVak.vulVak(s);
				return;
			}
		}

		if (e.getSource() == formuleVak && e.getActionCommand().equals("ingevuld"))
		{
			if (stapNr == 0 && tips && !hasStartString && diagnose)
			{
				if (formuleVak.geefVergelijking() == null)
				{
					if (formuleVak.toString().length() > 3)
					{ // setFeedback("De notatie van de vergelijking of oplossingen is niet juist");
						if (mode == 2 || mode == 3)
							ingevuld = true;
						setFeedback(WiskOpdr.rb.getString("feedbackTekst09"), true);
						return;
					}
					else
						return;
				}
				hasStartString = true;
				stapOk = true;
				setLog();
				maakStap();

			}
			else if (stapNr == 0 && casAntw && !hasStartString)
			{
				String vergString = formuleVak.toString();
				//VergelijkingMeerv vm = FormuleParser.parseVergelijking(vergString, functieDefSet);
				VergelijkingMeerv vm = FormuleParser.parseVergelijking(vergString, functieMVDefSet);
				String vergStringCas = "$f@";

				VergelijkingMeerv vmAntw = null;
				if (vm != null)
				{
					Vergelijking v = vm.geefVergelijking(0);
					String varNaam = v.geefVarNaam();
					vergStringCas = v.geefExpLinks().toStringCAS() + "==" + v.geefExpRechts().toStringCAS();
					String[] varNamen = v.geefVarNamen();
					if (varNamen.length != 1)
						return;
					double[] coeff = Algebra.geefCoefficienten(v);
					if (coeff != null && coeff.length == 2 && coeff[1] != 0)
					{
						huidigeVergelijking = vm;
						double antw = -coeff[0] / coeff[1];
						Vergelijking[] vAntw =
						{ new Vergelijking(new BasisExpressie(varNaam), new BasisExpressie(antw)) };
						vmAntw = new VergelijkingMeerv(vAntw);
						String def = "";
						if (vmAntw != null)
							def = vmAntw.toString();
						zetJuisteAntwoord("$f" + def + "@");
						sendCommand("equation");
						sendCommand("zetOplossing");
						sendCommand("double.solution");
						sendCommand("balansvergelijking");
						
					}
					else
					{	huidigeVergelijking = vm;
						vmAntw = //Expressie.solveWithCAS(vergStringCas, varNamen[0]);
								Expressie.solve(v, varNamen[0]);
						String def = "";
						if (vmAntw != null)
							def = vmAntw.toString();
						zetJuisteAntwoord("$f" + def + "@");
						sendCommand("equation");
					}
					hasStartString = true;
					stapOk = true;
					setLog();
					if (bordjesMethode)
					{
						try
						{
							VergelijkingMeerv antwoordIngevuld = formuleVak.geefVergelijking();
							if (antwoordIngevuld != null)
							{
								String vergelString = antwoordIngevuld.toStringStrikt();
								formuleVak.vulVak("$f" + vergelString + "@");
								requestFocus();
							}
						}
						catch (Exception exc)
						{
						}
					}
					else
						maakStap();
				}

			}
			else if (tips && diagnose)
			{
				kijkNaIdeas();
				setLog();
				maakStap();
				return;
			}
			else if (mode == 0 || mode == 1)
			{
				kijkNa();
				if (!formuleVak.toString().equals("$f@"))
					zetNagekeken(true);
				if (fout)
					errorCount++;
				attemptsCount++;
				if (linStrategieVersie)
				{
					if (!isEindOplossing)
						zetGoedFout(GEEN, -1);
					formuleVak.setEditable(false);
				}
				if (bordjesMethode && hasStartString)
				{
					if (isGelijkwaardig)
						formuleVakken[stapNr - 1].setSelectable(false);
					if (isGelijkwaardig)
						formuleVak.setEditable(false);
				}
				if (bordjesMethode && !hasStartString)
				{
					try
					{
						VergelijkingMeerv antwoordIngevuld = formuleVak.geefVergelijking();
						if (antwoordIngevuld != null)
						{
							String vergString = antwoordIngevuld.toStringStrikt();
							formuleVak.vulVak("$f" + vergString + "@");
							requestFocus();
						}
					}
					catch (Exception exc)
					{
					}
				}
			}
			else
			{
				try
				{
					VergelijkingMeerv antwoordIngevuld = formuleVak.geefVergelijking();
					if (antwoordIngevuld != null)
					{
						String vergString = antwoordIngevuld.toString();
						if (bordjesMethode && !hasStartString)
						{
							vergString = antwoordIngevuld.toStringStrikt();
						}
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
				setLog();
			}
			if ((mode == 0 || mode == 1) && ingevuld)
			{
				produceAction("checked");
			}

			if ((mode == 0 || mode == 1) && hasFeedback && !correct && !linStrategieVersie && !bordjesMethode)
			{
				maakStap();
			}
			else if ((mode == 0 || mode == 1) && !linStrategieVersie && !bordjesMethode && !linOefenVersie && !hasFeedback && isGelijkwaardig && (vorm && !isJuisteVorm || eindOplossingNodig && !isEindOplossing || exactNodig && !isEindOplossingExact) && !(formuleVakken[0].getParent() instanceof SimpelAntwoordVergelijkingVak))
			{
				maakStap();
				if (moetNogAfgerond)
					formuleVakken[stapNr].vulVak("$f" + gewensteEindOplossing.geefVergelijkingVar() + "\u2248@");
			}
			else if ((mode == 0 || mode == 1) && bordjesMethode && isGelijkwaardig && (isEindOplossing || moetNogAfgerond) && exactNodig && !isEindOplossingExact)
			{
				//if(bordjesMethode)stapOk = true;
				maakStap();
				if (moetNogAfgerond)
					formuleVakken[stapNr].vulVak("$f" + gewensteEindOplossing.geefVergelijkingVar() + "\u2248@");
				else
					formuleVakken[stapNr].vulVak("$f" + gewensteEindOplossing.geefVergelijkingVar() + "=");
			}/**/

			if ((mode == 2 || mode == 3) && !formuleVak.toString().equals("$f@"))
			{
				VergelijkingMeerv antwoordIngevuld = formuleVak.geefVergelijking();
				if (antwoordIngevuld == null)
				{
					setFeedback(WiskOpdr.rb.getString("feedbackTekst09"), true);
				}
				else
				{
					maakStap();
					remove(feedbackTekst);
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
			zetGoedFout(GEEN, -1);
			if (mode == 2 || mode == 3)
			{
				for (int i = 0; i < stapNr + 1; i++)
				{
					zetGoedFout(GEEN, i);
				}
				produceAction("feedbackWeg");
			}
		}
		else if (e.getSource() instanceof FormuleVak && e.getActionCommand().equals("focus"))
		{
			FormuleVak fv = (FormuleVak) e.getSource();
			if (fv != formuleVak)
				formuleVak = fv;
		}
		else if (e.getSource() == gelijkwaardigKnop)
		{
			maakStap();
			if (stapOk)
			{
				zetGoedFout(GEEN, -1);
			}
			else
				formuleVak.requestFocus();
		}
		else if (e.getSource() == terugKnop)
		{
			stapTerug();
			setLog();
			formuleVak.requestFocus();
		}
		else if (e.getSource() == plusKnop)
		{
			maakStap("+");
		}
		else if (e.getSource() == minKnop)
		{
			maakStap("-");
		}
		else if (e.getSource() == maalKnop)
		{
			maakStap("*");
		}
		else if (e.getSource() == deelKnop)
		{
			maakStap(":");
		}
		else if (e.getSource() == haakjesKnop)
		{
			maakStap("haakjes");
			maakBewerkingStap();
		}
		else if (e.getSource() == herleidKnop)
		{
			maakStap("herleid");
			maakBewerkingStap();

		}
		else if (e.getSource() == ontbindKnop)
		{
			maakStap("ontbind");
			maakBewerkingStap();

		}
		else if (e.getSource() == splitsKnop)
		{
			maakStap("splits");
			maakBewerkingStap();

		}
		else if (e.getSource() == wortelBewerkKnop)
		{
			maakStap("wortel");
			maakBewerkingStap();

		}
		else if (e.getSource() == abcKnop)
		{
			maakStap("abc");
		}
		else if (e.getSource() == subKnop)
		{
			if (subKnopExtra)
			{
				gebruikersSubstitutiesVak.setLocation(getWidth() - 221, 27);
				zetOpRoot(gebruikersSubstitutiesVak);
			}
			else if (substitutie == null)
				maakStap("sub");
		}
		else if (e.getSource() == gebruikersSubstitutiesVak)
		{
			removeFromRoot(gebruikersSubstitutiesVak);
			repaint();
		}
		else if (e.getSource() == pijlVak)
		{
			maakBewerkingStap();

			if (e.getActionCommand().equals("substitutie"))
			{
				substitutie = pijlVak.geefSubstitutie();
			}
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
			removeSoft(mwFeedbackPanel);
			if (uitw || getParent() == null && feedbackTekst.getParent() != null)
			{
				produceAction("closeFeedback");
			}
			formuleVak.requestFocus();
		}
		else if (e.getSource() == tip1Knop)
		{
			ideasPuntenAftrek += aftrekTip;
			FormuleVak fv = formuleVakken[this.stapNr < 1 ? 0 : this.stapNr - 1];
			if (formuleVak.geefVergelijking() != null && isGelijkwaardig)
				fv = formuleVak;
			String vglStrikt = fv.geefVergelijking().toStringStrikt();
			vglStrikt = vertaalNaarIdeasExpressie(vglStrikt);
			String vgl = fv.geefVergelijking().toString();
			if (meerTips)
			{
				RuleIF[] rules = WiskOpdr.ideas.getAllFirsts(vglStrikt, strategieDomein);
				if (rules == null)
				{
					JOptionPane.showMessageDialog(this, "Feedbackservice not available");
					return;
				}
				String feedback = "";
				for (int i = 0; i < rules.length; i++)
				{
					//System.out.println(rules[i].getId());
					if (i > 0)
						feedback = feedback + "\n";
					if (rules.length > 1)
						feedback = feedback + "Tip " + (i + 1) + ": \n";
					else
						feedback = feedback + "Tip: \n";

					String tipString = translateRule(rules[i].getId());
					tipString = formatRuleText(rules[i], tipString);
					feedback = feedback + tipString + "\n";
				}
				if (rules.length > 1)
					setFeedback(feedback, true);
				else if (hulpBijTip)
					setFeedback(feedback, true, 3);
				else
					setFeedback(feedback, true);

			}
			else
			{
				RuleIF rule = WiskOpdr.ideas.getOneFirst(vglStrikt, strategieDomein);
				if (rule == null)
				{
					JOptionPane.showMessageDialog(this, "Feedbackservice not available");
					return;
				}
				String feedback = "";
				feedback = feedback + "Tip: \n" + translateRule(rule.getId()) + "\n";
				feedback = formatRuleText(rule, feedback);
				//if (feedbackModus == 1 || hulpBijTip)
				//	setFeedback(feedback, true, 3);
				//else
				setFeedback(feedback, true);
			}
			//if (feedbackModus == 1 && !tipGebruikt)
			//	aftrekTipHulp = aftrekTipHulp + 1;
			//tipGebruikt = true;
			repaint();

		}
		else if (e.getSource() == tip2Knop)
		{
			ideasPuntenAftrek += aftrekHulp;
			FormuleVak fv = formuleVakken[this.stapNr < 1 ? 0 : this.stapNr - 1];
			if (formuleVak.geefVergelijking() != null && isGelijkwaardig)
				fv = formuleVak;
			String vglStrikt = fv.geefVergelijking().toStringStrikt();
			vglStrikt = vertaalNaarIdeasExpressie(vglStrikt);
			String vgl = fv.geefVergelijking().toString();
			RuleIF rule = WiskOpdr.ideas.getOneFirst(vglStrikt, strategieDomein);
			if (rule == null)
			{
				JOptionPane.showMessageDialog(this, "Feedbackservice not available");
				return;
			}
			String exprString = vertaalIdeasExpressie(rule.getExpr());
			//VergelijkingMeerv v = FormuleParser.parseVergelijking("$f" + exprString + "@", functieDefSet);
			VergelijkingMeerv v = FormuleParser.parseVergelijking("$f" + exprString + "@", functieMVDefSet);
			String feedback = formatRuleText(rule, translateRule(rule.getId()));
			setFeedback("Tip: \n" + feedback + "\n" + "\n" + "$f" + vgl + "@\n" + "      " + WiskOpdr.rb.getString("ideasWordtDan") + ":\n" + "$f" + v.toString() + "@\n", true);
			//			if (feedbackModus == 1 && !hulpGebruikt)
			//				aftrekTipHulp = aftrekTipHulp + 2;
			//			hulpGebruikt = true;
			repaint();

		}
		else if (e.getSource() == hulpKnop)
		{
			ideasPuntenAftrek += aftrekStap;
			FormuleVak fv = formuleVakken[this.stapNr < 1 ? 0 : this.stapNr - 1];
			if (formuleVak.geefVergelijking() != null && isGelijkwaardig)
				fv = formuleVak;
			String vglStrikt = fv.geefVergelijking().toStringStrikt();
			vglStrikt = vertaalNaarIdeasExpressie(vglStrikt);
			RuleIF rule = WiskOpdr.ideas.getOneFirst(vglStrikt, strategieDomein);
			if (rule == null)
			{
				JOptionPane.showMessageDialog(this, "Feedbackservice not available");
				return;
			}
			String exprString = vertaalIdeasExpressie(rule.getExpr());
			//VergelijkingMeerv v = FormuleParser.parseVergelijking("$f" + exprString + "@", functieDefSet);
			VergelijkingMeerv v = FormuleParser.parseVergelijking("$f" + exprString + "@", functieMVDefSet);
			if (stapNr > 0)
				pijlVakken[stapNr - 1].zetPijlTekst("", false);
			maakStap();
			pijlVakken[stapNr - 1].setLocation(getSize().width - pijlX - 150, pijlVakken[stapNr - 1].getLocation().y);
			pijlVakken[stapNr - 1].setSize(250, pijlVakken[stapNr - 1].getSize().height);

			String feedback = formatRuleText(rule, translateRule(rule.getId()));
			pijlVakken[stapNr - 1].zetPijlTekst(feedback, false);

			formuleVakken[stapNr].vulVak("$f" + v.toString() + "@");
			int y = formuleVakken[stapNr - 1].getLocation().y + formuleVakken[stapNr - 1].getSize().height + pijlVakken[stapNr - 1].getHeight() - 10;
			int x = formuleVakX;
			formuleVakken[stapNr].setLocation(x, y);
			if (diagnose)
				kijkNaIdeas();
			else
				kijkNa();
			maakStap();
			repaint();
		}
		else if (e.getSource() == solveKnop)
		{
			ideasPuntenAftrek += aftrekSolve;
			String vglStrikt = formuleVakken[0].geefVergelijking().toStringStrikt();
			System.out.println(vglStrikt.toString());
			RuleIF[] rules = WiskOpdr.ideas.getDerivation(vglStrikt, strategieDomein);
			if (rules == null)
			{
				JOptionPane.showMessageDialog(this, "Feedbackservice not available");
				return;
			}

			while (stapNr > 0)
				stapTerug();
			for (int i = 0; i < rules.length; i++)
			{
				stapOk = true;
				maakStap();
				// System.out.println(rules[i].getExpr());
				String exprString = vertaalIdeasExpressie(rules[i].getExpr());

				//VergelijkingMeerv v = FormuleParser.parseVergelijking("$f" + exprString + "@", functieDefSet);
				VergelijkingMeerv v = FormuleParser.parseVergelijking("$f" + exprString + "@", functieMVDefSet);

				String feedback = formatRuleText(rules[i], translateRule(rules[i].getId()));
				pijlVakken[stapNr - 1].zetPijlTekst(feedback, false);
				// pijlVakken[stapNr-1].zetPijlTekst(rules[i].getId(), false);
				pijlVakken[stapNr - 1].setLocation(getSize().width - pijlX - 150, pijlVakken[stapNr - 1].getLocation().y);
				pijlVakken[stapNr - 1].setSize(250, pijlVakken[stapNr - 1].getSize().height);
				formuleVakken[stapNr].vulVak("$f" + v.toString() + "@");
				int y = formuleVakken[stapNr - 1].getLocation().y + formuleVakken[stapNr - 1].getSize().height + pijlVakken[stapNr - 1].getHeight() - 10;
				int x = formuleVakX;
				formuleVakken[stapNr].setLocation(x, y);
			}
			if (diagnose)
				kijkNaIdeas();
			else
				kijkNa();

			repaint();
		}
		else if (e.getSource() == wisKnop)
		{
			hasStartString = false;
			wis();
			formuleVak.vulVak("$f@");
			stapOk = false;
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

	public String vertaalIdeasExpressie(String s)
	{
		s = StringUtils.replaceStr(s, "\u2228", WiskOpdr.rb.getString("ofLabel"));
		if (s.equals("false"))
			s = "x=geen";
		if (s.equals("true"))
			s = "x=alles";
		return s;
	}

	public String vertaalNaarIdeasExpressie(String s)
	{
		s = StringUtils.replaceStr(s, WiskOpdr.rb.getString("ofLabel"), "\u2228");
		s = StringUtils.replaceStr(s, " ", "");
		if (s.equals("geenoplossingen"))
			s = "false";
		if (s.equals("allesiseenoplossing"))
			s = "true";
		return s;
	}

	public String translateRule(String s)
	{
		if (changedTexts != null)
		{
			if (changedTexts.containsKey(s))
				return (String) changedTexts.get(s);
		}
		return translateRuleToStandard(s);
	}

	public static String translateRuleToStandard(String s)
	{
		if (s.equals("ready"))
			return "correct opgelost";
		else if (s.equals("similar"))
			return "geen verschil met de vorige stap";
		else if (s.equals("correct"))
			return "juiste stap";
		else if (s.equals("expected"))
			return "correct (standaard strategie)";
		else if (s.equals("detour"))
			return "dit lijkt een omweg";
		else if (s.equals("buggy"))
			return "bekende fout: ";
		else if (s.equals("notequiv"))
			return "onbekende fout";
		else
			try
			{
				s = WiskOpdr.rb.getString(s);
			}
			catch (MissingResourceException e)
			{
			}
		return s;
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
	
	private void sendCommand(String command)
	{
		if(command.equals("balansvergelijking"))
		{	
			Vergelijking balansVerg = null;
			if(huidigeVergelijking!=null  && cbookEventHandler.hasListeners("balansvergelijking"))
			{	balansVerg =  huidigeVergelijking.geefVergelijking(0);
				System.out.println("balancevergelijking: "+balansVerg.toString());
				Expressie exp1 = Algebra.herleid(balansVerg.geefExpLinks());
				Expressie exp2 = Algebra.herleid(balansVerg.geefExpRechts());
				String sx1 = exp1.toString().charAt(0)=='x' ? "1" : "";
				String sx2 = exp2.toString().charAt(0)=='x' ? "1" : "";
				String balansString = sx1+exp1.toString()+"="+sx2+exp2.toString();
				cbookEventHandler.fire(command,command,balansString);
			}
		
		}
		if(command.equals("equation") && cbookEventHandler.hasListeners("equation"))
		{	
			Vergelijking balansVerg = null;
			if(huidigeVergelijking!=null)
			{	balansVerg =  huidigeVergelijking.geefVergelijking(0);
				cbookEventHandler.fire(command,huidigeVergelijking.geefVergelijking(0).toString());
			}
		
		}
		if(command.equals("zetOplossing") && cbookEventHandler.hasListeners("zetOplossing"))
		{	
			Vergelijking balansVerg = geefInitBalansVergelijking();
    		if(balansVerg==null) return;
    		double[] coeff = Algebra.geefCoefficienten(balansVerg);
    		if(coeff.length==2 && coeff[1]!=0)	
    			cbookEventHandler.fire(command,command,new Double(-coeff[0]/coeff[1]));
		
		}
		if(command.equals("double.solution") && cbookEventHandler.hasListeners("double.solution"))
		{	
			Vergelijking balansVerg = geefInitBalansVergelijking();
    		if(balansVerg==null) return;
    		double[] coeff = Algebra.geefCoefficienten(balansVerg);
    		if(coeff.length==2 && coeff[1]!=0)	{
    			//cbookEventHandler.fire(command,command,new Double(-coeff[0]/coeff[1]));
    			
    			Map<String,Object> map = new HashMap<String,Object>();
    			map.put("name", "x");
    			map.put("value", new Double(-coeff[0]/coeff[1]));
    			cbookEventHandler.fire(command,map);
    			
    		}
		}
	}

	@Override
	public void acceptCBookEvent(CBookEvent event) {
		String command = event.getCommand();
		if(command.equals("balansvergelijking"))
		{	String vergelijkingString = (String)event.getParameter("balansvergelijking");
			//zetBalansVergelijking(FormuleParser.parseVergelijking("$f" + vergelijkingString + "@", functieDefSet));
			zetBalansVergelijking(FormuleParser.parseVergelijking("$f" + vergelijkingString + "@", functieMVDefSet));
		}
		if(command.equals("maakStap"))
		{	maakBalansStap();
		}
		if(command.startsWith("equation"))
		{
	 		String formuleString = (String)event.getMessage();
	 		if(formuleString.charAt(0)!='$') formuleString = "$f" + formuleString + "@";
			vulVak(formuleString);
			
		}
		if(command.startsWith("action.check"))
		{
	 		kijkNa();
		}
		
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
		String[] commands = {"balansvergelijking", 
				"double.solution",
				"equation",
				"action.correct",
				"action.false",
				"action.false_2"};
		return commands;
	}

	@Override
	public String[] getAcceptedCmds() {
		String[] commands = {"balansvergelijking", "maakStap", "equation", "action.setNotEditable", "action.check" };
		return commands;
	}

	@Override
	public String getLocalizedCmd(String cmd) {
		return WiskOpdr.rb.getString(CBA_PREFIX + cmd);
	}

	public int bepaalHoogte()
	{
		int hoogte = 0;
		for(int i = 0; i < stapNr + 1; i++)
		{
			hoogte += formuleVakken[i].getSize().height + stapH;
		}
		if (feedbackTekst != null && feedbackTekst.isShowing()) 
			hoogte += 30 + feedbackTekst.getHeight();
		return hoogte;
	}
	
	public void zetPijl(boolean p)
	{
		pijl = p;
	}
	
	public void zetCheck(boolean c)
	{
		check = c;
	}
	
	public boolean getCheck()
	{
		return check;
	}

	public int getStapNr()
	{
		return stapNr;
	}
	
//	public boolean getIngevuld()
//	{
//		return ingevuld;
//	}
//	
//	public void setIngevuld(boolean ingevuld)
//	{
//		this.ingevuld = ingevuld;
//	}
//	
//	public boolean getNagekeken()
//	{
//		return nagekeken;
//	}
//	
//	public void setNagekeken(boolean nagekeken)
//	{
//		this.nagekeken = nagekeken;
//	}
//
//	public boolean isGelijkwaardig() {
//		return isGelijkwaardig;
//	}
//
//	public void setGelijkwaardig(boolean isGelijkwaardig) {
//		this.isGelijkwaardig = isGelijkwaardig;
//	}
}
