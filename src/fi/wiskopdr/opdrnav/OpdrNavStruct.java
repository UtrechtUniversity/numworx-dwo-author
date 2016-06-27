package fi.wiskopdr.opdrnav;

import java.applet.Applet;
import java.awt.AWTEventMulticaster;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

import fi.beans.base64code.StringCodeObject;
import fi.beans.scorm.SCORM12APIInterface;
import fi.beans.wnwidgets.NWButtonUI;
import fi.beans.wnwidgets.OpnieuwPanel;
import fi.wiskopdr.AntwoordFormuleVak;
import fi.wiskopdr.AntwoordFormuleVakEditPanel;
import fi.wiskopdr.AntwoordVergelijkingVak;
import fi.wiskopdr.AntwoordVergelijkingVakEditPanel;
import fi.wiskopdr.DialogFacade;
import fi.wiskopdr.KlaarKnop;
import fi.wiskopdr.ScoresObjectivesPanel;
import fi.wiskopdr.SimpelAntwoordFormuleVak;
import fi.wiskopdr.SimpelAntwoordVergelijkingVak;
import fi.wiskopdr.TekstVakPanel;
import fi.wiskopdr.TimerPanel;
import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.expressies.Expressie;
import fi.wiskopdr.formuleobjects.FormuleParser;
import fi.wiskopdr.formuleobjects.FormuleTeken;
import fi.wiskopdr.formuleobjects.FormuleVakHouder;
import fi.wiskopdr.formuleobjects.Tablet;
import fi.wiskopdr.formuleobjects.TabletOwner;
import fi.wiskopdr.tekstobjects.Link;
import fi.wiskopdr.tekstobjects.ShareAction;
//import fi.wiskopdr.tekstobjects.LinkIF;
import fi.wiskopdr.tekstobjects.TekstImageVak;
import fi.wiskopdr.tekstobjects.TekstVak;

public class OpdrNavStruct extends JLayeredPane implements MouseListener, ActionListener, TabletOwner
{

	public static int OEFENEN = 0;
	public static int OEFENEN_STRAFPUNTEN = 1;
	public static int ZELFTOETS = 2;
	public static int EINDTOETS = 3;

	private MyOpdrContainer opdrContainer;

	private String[][] opdrachten;
	private Hashtable[][] states;
	private int[][] scores;
	private boolean[][] isCorrect;
	private int[][][][] scoresObjectives;
	private int[][][][] possibleMisconceptions;
	private int[][][][] measuredMisconceptions;
	private String[][] objectives;
	private String[] categorieString;
	private String[][] misconceptions;
	private String[] mccCategorieString;
	
	private String[][] times; // FIXME op 2 plekken krijgt times[i][j] een
								// nieuwe waarde
								// uitzoeken wanneer dat niet moet.

	private int aantalActiviteiten;
	private int activiteitNr;
	private String[] activiteitNamen;

	private int maxAantalOpdrachten = 51;
	private int aantalOpdrMax;
	private int[] aantalOpdrachten;
	private int opdrachtNr;

	private OpdrachtNrRij[] or;
	private ActKeuzePanel actKeuzePanel;

	private int orPosX, orPosY;
	private int actKeuzePanelX, actKeuzePanelY;

	private JButton opnieuwKnop, nakijkKnop, klaarKnop, itemOpnieuwKnop;
	private OpnieuwPanel opnieuwPanel;

	private JLabel aantalNakijkLabel;
	private JLabel scoreLabel;
	private JLabel opdrachtLabel;
	private JLabel[] activiteitScoreLabels;
	private int[] aantalNakijken;
	private MWScoreLabel mwScoreLab;

	private int mode;
	private int nakijkStraf = 5;
	private int foutStraf = 2;
	private int[][] strafpunten;
	private boolean opnieuwMogelijk;
	private boolean allesOpnieuwGR;
	private boolean itemOpnieuwMogelijk;
	private boolean checkPerOpdracht;

	private Hashtable launchData;
	private Applet applet;
	private SCORM12APIInterface api;
	private String lessonMode = "";

	private JComboBox modeChoice;
	private Font font = new Font("SansSerif", Font.PLAIN, 12);

	private Tablet tablet;
	private boolean tabletAdded;
	private FormuleVakHouder tabletUser;

	private boolean gekoppeldeOpdrachten = false;
	private boolean voorwaardelijkeOpdrachten = false;
	private JCheckBox gekoppeldeOpdrCB;
	private boolean[][] opdrachtenCorrect;

	private int[][] scoresMax;
	private int[][][][] scoresMaxObjectives;
	private int scoreMax;

	Hashtable instellingen = null;

	boolean objectivesAanwezig;
	private JButton scoresObjectivesKnop;
	private DialogFacade scoresObjectivesDialog;
	private ScoresObjectivesPanel scoresObjectivesPanel;
	
	private JButton viewMisconceptionsKnop;
	private DialogFacade viewMisconceptionsDialog;
	private ScoresObjectivesPanel viewMisconceptionsPanel;
	
	//private JButton condVolgendeKnop; // , condVorigeKnop;
	private String[] urls = null;
	private int[] grensScores = null;
	//private Link condLink;
	
	private JPanel afdekPanel;
	private TimerPanel timerPanel;
	private boolean timer;
	private int timeLimit;
	private boolean hoekGraden;

	private int orSize = "GR".equals(WiskOpdr.deployVariant) ? 21 : 25;

	private JButton volgendeKnop, vorigeKnop, eindeKnop;
	private boolean bolletjesZichtbaar = true;
	private boolean volgendeKnopZichtbaar = false;
	private boolean vorigeKnopZichtbaar = false;
	private boolean pagina = false;
	private boolean allesCorrectNodig = false;
	private Link eindeLink;

	private static int navigatieSize = 12;

	private int margeLinks = 17;
	private int margeRechts = 15;
	private int margeBoven = "GR".equals(WiskOpdr.deployVariant) ? 10 : 15;
	private int margeOnder = 15;

	private boolean globalParam = false;
	private boolean voortgang = false;
	private boolean condNav = false;
	private boolean condNavPerc = false;
	private boolean condNavVoorwaarden = false;
	private int[][][] navVoorwaarden = null;
	private boolean[][] bezocht = null;
	
	private boolean abcDeelOpdr = false;
	private boolean zelftoetsGeenCorr = false;
	private boolean eerderGeenCorr = false;
	private boolean zelftoetsNagekeken = false;
	private int condPerc = 100;
	private boolean scoresZichtbaar = true;
	
//	private JCheckBox lockToetsCB;
	private JLabel lockToetsLabel;
	private boolean toetsLocked;

	private JCheckBox overrideScoreCB;
	private JTextField overrideScoreTF;
	private JLabel overrideScoreLabel;
	private boolean overrideScore[];
	private int overridenScores[];

	private int reviewLocation = -1;
	private int aantalSessies = 0;
	private JLabel aantalSessiesLabel;
	
	private JPopupMenu orPopup;
	private JMenuItem newPageMenuItem, deletePageMenuItem;
	

	/**
	 * Maakt nieuw Opdrachtnavigatie component op basis van de aangeleverde
	 * launchData, dan wel een applet waaraan deze launchData kan worden
	 * opgevraagd mbv getParameter(String)
	 */
	public OpdrNavStruct(WiskOpdr applet, MyOpdrContainer opdrContainer, int x, int y, int b, int h, SCORM12APIInterface api, Hashtable launchData)
	{
		TekstVak.ID=0;
		TekstVak.TELLER=0;
		
		if(applet != null) applet.ons = this; // FIXME static reference mogelijk naar 'this' 
		setLayout(null);
		setBounds(x, y, b, h);
		setBackground(WiskOpdr.bgcolor);

		this.api = api;
		this.opdrContainer = opdrContainer;
		this.applet = applet;
		this.launchData = launchData;

		if (api != null)
			lessonMode = api.LMSGetValue("cmi.core.lesson_mode");
		ShareAction.init(getData(ShareAction.SHARE_MAP));
		TekstImageVak.setImageMapString(getData(TekstImageVak.IMAGE_MAP));

		String gekoppeldeOpdrachtenString = getData("gekoppeldeOpdrachten");
		if (gekoppeldeOpdrachtenString != null && gekoppeldeOpdrachtenString.equals("true"))
			gekoppeldeOpdrachten = true;
		
		String instellingenString = getData("instellingen");
		Object ob = StringCodeObject.decodeStringToObject(instellingenString);
		instellingen = (Hashtable) ob;
		
		// instelling "gekoppeldeOpdrachten" omgezet in een aantal nieuwe instellingen:
		if (gekoppeldeOpdrachten)
		{	gekoppeldeOpdrachten = false;
			instellingen.put("abcDeelOpdr", new Boolean(true));
			instellingen.put("condNav", new Boolean(true));
			instellingen.put("allesCorrectNodig", new Boolean(true));
			//instellingen.put("setCondNavPerc", new Integer(100));
			instellingen.put("globalParam", new Boolean(true));
		}
				
		zetInstellingen(instellingen);
		opdrContainer.refreshFonts();

		String aantalActiviteitenString = getData("aantalActiviteiten");
		aantalActiviteiten = Integer.parseInt(aantalActiviteitenString);
		aantalOpdrachten = new int[aantalActiviteiten];
		activiteitNamen = new String[aantalActiviteiten];
		for (int i = 0; i < aantalActiviteiten; i++)
		{
			activiteitNamen[i] = getData("activiteit_" + (i + 1));
			String aantalString = getData("aantalOpdrachten_" + (i + 1));
			aantalOpdrachten[i] = Integer.parseInt(aantalString);
		}

		opdrachten = new String[aantalActiviteiten][maxAantalOpdrachten];
		states = new Hashtable[aantalActiviteiten][maxAantalOpdrachten];
		scores = new int[aantalActiviteiten][maxAantalOpdrachten];
		times = new String[aantalActiviteiten][maxAantalOpdrachten];
		isCorrect = new boolean[aantalActiviteiten][maxAantalOpdrachten];
		scoresMax = new int[aantalActiviteiten][maxAantalOpdrachten];
		if (objectives != null)
		{
			scoresObjectives = new int[aantalActiviteiten][maxAantalOpdrachten][objectives.length][];
			scoresMaxObjectives = new int[aantalActiviteiten][maxAantalOpdrachten][objectives.length][];
			for (int k = 0; k < aantalActiviteiten; k++)
				for (int j = 0; j < maxAantalOpdrachten; j++)
					for (int i = 0; i < objectives.length; i++)
					{
						scoresObjectives[k][j][i] = new int[objectives[i].length];
						scoresMaxObjectives[k][j][i] = new int[objectives[i].length];
					}
		}
		if (misconceptions != null)
		{
			possibleMisconceptions = new int[aantalActiviteiten][maxAantalOpdrachten][misconceptions.length][];
			measuredMisconceptions = new int[aantalActiviteiten][maxAantalOpdrachten][misconceptions.length][];
			for (int k = 0; k < aantalActiviteiten; k++)
				for (int j = 0; j < maxAantalOpdrachten; j++)
					for (int i = 0; i < misconceptions.length; i++)
					{
						possibleMisconceptions[k][j][i] = new int[misconceptions[i].length];
						measuredMisconceptions[k][j][i] = new int[misconceptions[i].length];
					}
		}

		scoreMax = 0;
		for (int i = 0; i < aantalActiviteiten; i++)
		{
			for (int j = 0; j < aantalOpdrachten[i]; j++)
			{
				opdrachten[i][j] = getData("opdracht_" + (i + 1) + "_" + (j + 1));
				Object o = StringCodeObject.decodeStringToObject(opdrachten[i][j]);
				Hashtable ht = (Hashtable) o;

				
				if (ht != null && objectives != null && ht.containsKey("scoreMaxObjectives"))
					scoresMaxObjectives[i][j] = (int[][]) ht.get("scoreMaxObjectives"); //Gaat dit goed???
				
				if (ht != null && ht.containsKey("scoreMax"))
					scoresMax[i][j] = ((Integer) ht.get("scoreMax")).intValue();
				else
					scoresMax[i][j] = 10; // FIXME klopt dat wel?? Ja bij de oude editorversie was aanvankelijk geen scoreMax (standaard 10)
				scoreMax += scoresMax[i][j];
			}
		}
		
		//condLink = new Link("", urls, 400, 400, grensScores);
		bezocht = new boolean[aantalActiviteiten][maxAantalOpdrachten];
		for(int j = 0; j < aantalActiviteiten; j++)
		{	for(int i = 0; i < bezocht[j].length; i++)
				bezocht[j][i] = false;
		}	
		bezocht[0][0] = true;
		String modeString = getData("mode");
		mode = 0;
		if (modeString != null)
			mode = Integer.parseInt(modeString);

		String opnieuwMogelijkString = getData("opnieuwMogelijk");
		if (opnieuwMogelijkString != null && opnieuwMogelijkString.equals("true"))
			opnieuwMogelijk = true;

		opdrContainer.zetOpdracht(opdrachten[0][0]);
		opdrContainer.addActionListener(this);
		add(opdrContainer);

		//voorwaardelijkeOpdrachten = gekoppeldeOpdrachten && mode != 2 && mode != 3;

		aantalOpdrMax = aantalOpdrachten[0];
		for (int i = 1; i < aantalActiviteiten; i++)
		{
			aantalOpdrMax = Math.max(aantalOpdrMax, aantalOpdrachten[i]);
		}

		aantalNakijken = new int[aantalActiviteiten];
		strafpunten = new int[aantalActiviteiten][maxAantalOpdrachten];

		for (int i = 0; i < aantalActiviteiten; i++)
		{
			if (aantalOpdrachten[i] > maxAantalOpdrachten)
				maxAantalOpdrachten = aantalOpdrachten[i];
		}
		
		maakGui();

		opdrachtenCorrect = new boolean[aantalActiviteiten][maxAantalOpdrachten];

		int sumScoresMaxObjectives = 0;
		try
		{
			for (int i = 0; i < scoresMaxObjectives.length; i++)
				for (int j = 0; j < scoresMaxObjectives[i].length; j++)
					for (int k = 0; k < scoresMaxObjectives[i][j].length; k++)
						for (int l = 0; l < scoresMaxObjectives[i][j][k].length; l++)
							sumScoresMaxObjectives += scoresMaxObjectives[i][j][k][l];
		}
		catch (Exception e)
		{
		}

		if (sumScoresMaxObjectives > 0)
			objectivesAanwezig = true;
		else
			objectivesAanwezig = false;
		scoresObjectivesKnop.setVisible(objectivesAanwezig);
		viewMisconceptionsKnop.setVisible(misconceptions != null);
		
		
		zetMode(mode);

		layoutGui(b, h);

		if (checkPerOpdracht)
			opdrContainer.zetMode(2);
		zetGekoppeldeOpdrachten(gekoppeldeOpdrachten);
		
		or[activiteitNr].setScoresVisible(scoresZichtbaar);
		activiteitScoreLabels[0].setVisible(scoresZichtbaar);
		
		if(condNav && volgendeKnopZichtbaar)
			if((allesCorrectNodig || condNavPerc) && !or[activiteitNr].geefNoScore(opdrachtNr + 1))
				volgendeKnop.setEnabled(false);
		
		if(condNav && condNavVoorwaarden && !allesCorrectNodig)
			try{
			or[activiteitNr].setEnabled(true, bepaalVolgendeOpdracht(activiteitNr, opdrachtNr) + 1);
			}
			catch(Exception e)
			{	if("GR".equals(WiskOpdr.deployVariant) || "MW".equals(WiskOpdr.deployVariant))
				{	volgendeKnop.setEnabled(false);
					volgendeKnop.setVisible(volgendeKnopZichtbaar);
				}
				else
				{	volgendeKnop.setVisible(false);
					//eindeKnop.setVisible(volgendeKnopZichtbaar);
					eindeKnop.setEnabled(true);
				}
			}
		String[] eindeUrls = new String[] {"goto:0"};
		eindeLink = new Link("", eindeUrls, 400, 400, false, null);
			
		orPopup = new JPopupMenu();
		
		newPageMenuItem = new JMenuItem(WiskOpdr.rb.getString("newPageMenuItem"));
		newPageMenuItem.addActionListener(this);
		orPopup.add(newPageMenuItem);
		
		deletePageMenuItem = new JMenuItem(WiskOpdr.rb.getString("deletePageMenuItem"));
		deletePageMenuItem.addActionListener(this);
		orPopup.add(newPageMenuItem);
	}

	/**
	 * Hiermee worden de data opgevraagd, ofwel uit de hashtable launchData,
	 * ofwel via getParameter van het applet
	 */
	private String getData(String paramName)
	{
		if (launchData != null)
			return (String) launchData.get(paramName);
		if (applet != null)
			return (String) applet.getParameter(paramName);
		return null;
	}

	/**
	 * Gebruikersinterface wordt hier gemaakt
	 */
	private void maakGui()
	{
		volgendeKnop = new VVButton("volgende");
		if ("MW".equals(WiskOpdr.deployVariant))
			volgendeKnop = new JButton("volgende");
		if ("GR".equals(WiskOpdr.deployVariant))
			volgendeKnop = new JButton(">");
		volgendeKnop.setBorder(BorderFactory.createEmptyBorder());
		volgendeKnop.setFont(new Font("SansSerif", Font.PLAIN, navigatieSize));
		volgendeKnop.addActionListener(this);
		volgendeKnop.setVisible(volgendeKnopZichtbaar);
		add(volgendeKnop, 0);
		
		vorigeKnop = new VVButton("vorige");
		if ("MW".equals(WiskOpdr.deployVariant))
			vorigeKnop = new JButton("vorige");
		if ("GR".equals(WiskOpdr.deployVariant))
			vorigeKnop = new JButton("<");
		vorigeKnop.setBorder(BorderFactory.createEmptyBorder());
		vorigeKnop.setFont(new Font("SansSerif", Font.PLAIN, navigatieSize));
		vorigeKnop.addActionListener(this);
		vorigeKnop.setVisible(vorigeKnopZichtbaar);
		
		add(vorigeKnop, 0);
		
		eindeKnop = new JButton(WiskOpdr.rb.getString("eindeKnopLabel"));
		eindeKnop.setFont(new Font("SansSerif", Font.PLAIN, navigatieSize));
		eindeKnop.addActionListener(this);
		eindeKnop.setVisible(false);
		add(eindeKnop, 0);

		nakijkKnop = new JButton(WiskOpdr.rb.getString("nakijkKnopLabel"));
		nakijkKnop.setFont(new Font("SansSerif", Font.PLAIN, navigatieSize));
		nakijkKnop.addActionListener(this);
		nakijkKnop.setVisible(false);
		add(nakijkKnop, 0);

		if (WiskOpdr.zoefi)
			klaarKnop = new KlaarKnop(WiskOpdr.rb.getString("klaarKnopLabel"));
		else
			klaarKnop = new JButton(WiskOpdr.rb.getString("klaarKnopLabel"));
		klaarKnop.setFont(WiskOpdr.tekstFont);
		klaarKnop.addActionListener(this);
		klaarKnop.setVisible(checkPerOpdracht);
		add(klaarKnop, 0);

		aantalNakijkLabel = new JLabel(WiskOpdr.rb.getString("nakijkLabel1") + " 0 " + WiskOpdr.rb.getString("nakijkLabel2"));
		aantalNakijkLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		aantalNakijkLabel.setVisible(false);
		add(aantalNakijkLabel, 0);

		lockToetsLabel = new JLabel(WiskOpdr.rb.getString("lockToetsLabel"));
		lockToetsLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
		lockToetsLabel.setVisible(false);
		add(lockToetsLabel, 0);

		itemOpnieuwKnop = new JButton(WiskOpdr.rb.getString("opnieuwKnopLabel"));
		itemOpnieuwKnop.setFont(new Font("SansSerif", Font.PLAIN, navigatieSize));
		itemOpnieuwKnop.setMargin(new Insets(4, 10, 4, 10));
		itemOpnieuwKnop.addActionListener(this);
		if ("GR".equals(WiskOpdr.deployVariant))
			itemOpnieuwKnop.setVisible(opnieuwMogelijk);
		else
			itemOpnieuwKnop.setVisible(itemOpnieuwMogelijk);
		add(itemOpnieuwKnop, 0);

		opnieuwKnop = new JButton(WiskOpdr.rb.getString("allesOpnieuwKnopLabel"));
		opnieuwKnop.setFont(new Font("SansSerif", Font.PLAIN, navigatieSize));
		opnieuwKnop.setMargin(new Insets(4, 0, 4, 0));
		opnieuwKnop.addActionListener(this);
		if (!"GR".equals(WiskOpdr.deployVariant) || mode == 2 || allesOpnieuwGR)
			opnieuwKnop.setVisible(opnieuwMogelijk);
		else
			opnieuwKnop.setVisible(false);
		add(opnieuwKnop, 0);

		String imageName = "/fi/beans/wnwidgets/resources/popup-gray.png";
		if ("MW".equals(WiskOpdr.deployVariant))
		{
			imageName = "/fi/beans/wnwidgets/resources/popup.png";
			opnieuwPanel = new OpnieuwPanel(WiskOpdr.loadImage(imageName), getWidth() / 2 - 180, getHeight() / 2 - 105, WiskOpdr.rb.getString("opnieuwPanelTekstMW"), "", WiskOpdr.language);
		}
		else
		{
			opnieuwPanel = new OpnieuwPanel(WiskOpdr.loadImage(imageName), getWidth() / 2 - 180, getHeight() / 2 - 105, WiskOpdr.rb.getString("opnieuwPanelTekst"), WiskOpdr.rb.getString("opnieuwPanelTitel"), WiskOpdr.language);
		}
		opnieuwPanel.setVisible(false);
		opnieuwPanel.addActionListener(this);
		this.setLayer(opnieuwPanel, JLayeredPane.POPUP_LAYER.intValue());
		add(opnieuwPanel, 0);

		opdrachtLabel = new JLabel(WiskOpdr.rb.getString("opdrachtLabel"));
		if (pagina)
			opdrachtLabel.setText(WiskOpdr.rb.getString("paginaLabel"));
		else
			opdrachtLabel.setText(WiskOpdr.rb.getString("opdrachtLabel"));
		opdrachtLabel.setFont(new Font("SansSerif", Font.PLAIN, navigatieSize));
		opdrachtLabel.setAlignmentX(JLabel.RIGHT_ALIGNMENT);
		if (!"GR".equals(WiskOpdr.deployVariant) && !WiskOpdr.zoefi)
			add(opdrachtLabel, 0);
		opdrachtLabel.setVisible(bolletjesZichtbaar || lessonMode.equals("review"));
		if (aantalActiviteiten == 1 && aantalOpdrachten[0] == 1)
			opdrachtLabel.setVisible(false);

		scoreLabel = new JLabel(WiskOpdr.rb.getString("scoreLabel"));
		scoreLabel.setFont(new Font("SansSerif", Font.PLAIN, navigatieSize));
		if (WiskOpdr.deployVariant != null && WiskOpdr.deployVariant.equals("GR"))
			scoreLabel.setFont(font);
		scoreLabel.setAlignmentX(JLabel.RIGHT_ALIGNMENT);
		boolean gr = WiskOpdr.deployVariant != null && WiskOpdr.deployVariant.equals("GR");
		if (!"GR".equals(WiskOpdr.deployVariant) && !WiskOpdr.zoefi && !gr)
			add(scoreLabel, 0);
		scoreLabel.setVisible(bolletjesZichtbaar || lessonMode.equals("review"));
		if (aantalActiviteiten == 1 && aantalOpdrachten[0] == 1)
			scoreLabel.setVisible(false);

		mwScoreLab = new MWScoreLabel();
		if ("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant))
			add(mwScoreLab, 0);

		actKeuzePanel = new ActKeuzePanel(activiteitNamen, actKeuzePanelX, actKeuzePanelY, 160, aantalActiviteiten * 20);
		actKeuzePanel.addActionListener(this);
		actKeuzePanel.setBackground(getBackground());
		if (aantalActiviteiten > 1)
			add(actKeuzePanel, 0);

		activiteitScoreLabels = new JLabel[aantalActiviteiten];

		for (int i = 0; i < aantalActiviteiten; i++)
		{
			activiteitScoreLabels[i] = new JLabel(WiskOpdr.rb.getString("score") + 0);
			activiteitScoreLabels[i].setFont(new Font("SansSerif", Font.PLAIN, 12));
			if (WiskOpdr.deployVariant != null && WiskOpdr.deployVariant.equals("GR"))
				activiteitScoreLabels[i].setFont(new Font("SansSerif", Font.PLAIN, 12));
			if (!gr)
				add(activiteitScoreLabels[i], 0);
		}
		if (aantalActiviteiten == 1)
		{
			activiteitScoreLabels[0].setFont(new Font("SansSerif", Font.BOLD, navigatieSize + 2));
			activiteitScoreLabels[0].setText(WiskOpdr.rb.getString("totaal"));
			if(voortgang)
				activiteitScoreLabels[0].setText(WiskOpdr.rb.getString("voortgang") + "0%");
		}

		or = new OpdrachtNrRij[aantalActiviteiten];
		for (int i = 0; i < aantalActiviteiten; i++)
		{
			or[i] = new OpdrachtNrRij(aantalOpdrachten[i], orPosX, orPosY + 4, orSize);
			or[i].addActionListener(this);
			or[i].setBackground(getBackground());
			or[i].setSelected(1);
			or[i].setVisible(false);
			this.setLayer(or[i], JLayeredPane.PALETTE_LAYER.intValue());
			add(or[i]);
			for (int j = 0; j < aantalOpdrachten[i]; j++)
			{
				if (scoresMax[i][j] == 0)
					or[i].zetNoScore(j + 1, true);
			}
		}
		or[activiteitNr].setVisible(bolletjesZichtbaar || lessonMode.equals("review"));
		if (aantalActiviteiten == 1 && aantalOpdrachten[0] == 1)
			remove(or[0]);

//		lockToetsCB = new JCheckBox(WiskOpdr.rb.getString("lockToetsCBLabel"));
//		lockToetsCB.setFont(font);
//		lockToetsCB.setOpaque(false);
//		lockToetsCB.addActionListener(this);
//		lockToetsCB.setVisible(mode == 3 && lessonMode.equals("review"));
//		add(lockToetsCB, 0);

		volgendeKnop.setEnabled(opdrachtNr < aantalOpdrachten[activiteitNr] - 1);
		vorigeKnop.setEnabled(opdrachtNr > 0);
		eindeKnop.setEnabled(opdrachtNr == aantalOpdrachten[activiteitNr] - 1);

		if ("MW".equals(WiskOpdr.deployVariant))
		{
			if(!WiskOpdr.deployDwoGrading) opdrContainer.setControlPanelHeight(orSize);
			klaarKnop.setUI(NWButtonUI.getInstance("mw_formbutton_skin_orange.png"));
			klaarKnop.setBounds(10, orPosY - 30, 2 * orSize + 30, orSize - 2);
			klaarKnop.setText("nakijken");
			opnieuwKnop.setUI(NWButtonUI.getInstance("mw_formbutton_skin_orange.png"));
			opnieuwKnop.setText("opnieuw");
			nakijkKnop.setUI(NWButtonUI.getInstance("mw_formbutton_skin_orange.png"));
			volgendeKnop.setUI(NWButtonUI.getInstance("mw_formbutton_skin_orange.png"));
			vorigeKnop.setUI(NWButtonUI.getInstance("mw_formbutton_skin_orange.png"));
			//eindeKnop.setUI(NWButtonUI.getInstance("mw_formbutton_skin_orange.png"));

			setMWScoreLabel();
			activiteitScoreLabels[0].setVisible(false);
		}
		if ("GR".equals(WiskOpdr.deployVariant))
		{
			if(!WiskOpdr.deployDwoGrading) opdrContainer.setControlPanelHeight(orSize);
			if(mode==2)opnieuwKnop.setText("Toets opnieuw");
			if(mode==2)nakijkKnop.setText("Toets nakijken");
			klaarKnop.setText(" Klaar");
			klaarKnop.setUI(NWButtonUI.getInstance("gr_formbutton_skin_blue_full.png"));
			klaarKnop.setForeground(Color.white);
			klaarKnop.setFont(new Font("SansSerif", Font.BOLD, 12));
			itemOpnieuwKnop.setUI(NWButtonUI.getInstance("gr_formbutton_skin_blue.png"));
			itemOpnieuwKnop.setFont(new Font("SansSerif", Font.BOLD, 12));
			itemOpnieuwKnop.setForeground(new Color(70, 117, 186));
			itemOpnieuwKnop.setMargin(new Insets(0, 0, 0, 0));
			opnieuwKnop.setUI(NWButtonUI.getInstance("gr_formbutton_skin_blue.png"));
			opnieuwKnop.setFont(new Font("SansSerif", Font.BOLD, 12));
			opnieuwKnop.setForeground(new Color(70, 117, 186));
			opnieuwKnop.setMargin(new Insets(0, 0, 0, 0));

			nakijkKnop.setUI(NWButtonUI.getInstance("gr_formbutton_skin_blue.png"));
			nakijkKnop.setFont(new Font("SansSerif", Font.BOLD, 12));
			nakijkKnop.setForeground(new Color(70, 117, 186));
			nakijkKnop.setMargin(new Insets(0, 0, 0, 0));

			volgendeKnop.setUI(NWButtonUI.getInstance("gr_formbutton_skin_blue.png"));
			volgendeKnop.setFont(new Font("SansSerif", Font.BOLD, 12));
			volgendeKnop.setForeground(new Color(70, 117, 186));

			vorigeKnop.setUI(NWButtonUI.getInstance("gr_formbutton_skin_blue.png"));
			vorigeKnop.setFont(new Font("SansSerif", Font.BOLD, 12));
			vorigeKnop.setForeground(new Color(70, 117, 186));
			
			//eindeKnop.setUI(NWButtonUI.getInstance("gr_formbutton_skin_blue.png"));
			//eindeKnop.setFont(new Font("SansSerif", Font.BOLD, 12));
			//eindeKnop.setForeground(new Color(70, 117, 186));

			setMWScoreLabel();
			activiteitScoreLabels[0].setVisible(false);
		}

		aantalSessiesLabel = new JLabel("Attemps: 0");
		aantalSessiesLabel.setVisible(false);
		add(aantalSessiesLabel);

		scoresObjectivesKnop = new JButton(WiskOpdr.rb.getString("deelscoresKnopLabel"));
		scoresObjectivesKnop.setFont(new Font("SansSerif", Font.PLAIN, 12));
		scoresObjectivesKnop.setVisible(false);
		add(scoresObjectivesKnop, 0);
		scoresObjectivesKnop.addActionListener(this);
		
		viewMisconceptionsKnop = new JButton(WiskOpdr.rb.getString("misconceptionsKnopLabel"));
		viewMisconceptionsKnop.setFont(new Font("SansSerif", Font.PLAIN, 12));
		viewMisconceptionsKnop.setVisible(false);
		add(viewMisconceptionsKnop, 0);
		viewMisconceptionsKnop.addActionListener(this);

		if (timer)
		{
			timerPanel = new TimerPanel(orSize + 25, orSize + 40);
			timerPanel.addActionListener(this);
			add(timerPanel, 0);
		}
	}

	/**
	 * Gebruikersinterface wordt hier opnieuw van maten en locaties voorzien
	 */
	private void layoutGui(int b, int h)
	{
		orPosX = orSize * 2 + 17 + margeLinks;
		orPosY = h - (2 * orSize);
		if ("MW".equals(WiskOpdr.deployVariant))
			orPosY = h - orSize;
		if ("GR".equals(WiskOpdr.deployVariant))
			orPosY = h - orSize - 11;
		actKeuzePanelX = margeLinks;
		actKeuzePanelY = orPosY - aantalActiviteiten * 20 - 15;

		if (("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant)) && !WiskOpdr.deployDwoGrading)
			opdrContainer.setControlPanelHeight(orSize);
		else
			opdrContainer.setControlPanelHeight(2 * orSize);

		for (int i = 0; i < aantalActiviteiten; i++)
		{
			activiteitScoreLabels[i].setBounds(actKeuzePanelX + 170, actKeuzePanelY + i * 20, 70, 20);
		}
		if (aantalActiviteiten == 1)
		{
			activiteitScoreLabels[0].setBounds(orPosX + aantalOpdrMax * orSize + orSize + 00, orPosY + orSize + 2, 2 * orSize + 90, orSize - 5);
		}
		for (int i = 0; i < aantalActiviteiten; i++)
		{
			if (!"GR".equals(WiskOpdr.deployVariant))
				or[i].setLocation(orPosX, orPosY + 4);
		}
		actKeuzePanel.setLocation(actKeuzePanelX, actKeuzePanelY);
		aantalNakijkLabel.setBounds(orPosX + aantalOpdrachten[0] * 25 + 130, orPosY + 25, 110, orSize);
		opdrachtLabel.setBounds(orPosX - (orSize * 2 + 15), orPosY + 2, orSize * 2 + 15, orSize);
		scoreLabel.setBounds(orPosX - (orSize * 2 + 15), orPosY + orSize + 2, orSize * 2 + 15, orSize);
		scoresObjectivesKnop.setBounds(orPosX + aantalOpdrMax * orSize + 2 * orSize + 110 + activiteitScoreLabels[0].getWidth(), orPosY + orSize + 4, 2 * orSize + 50, orSize - 5);
		viewMisconceptionsKnop.setBounds(orPosX + aantalOpdrMax * orSize + 4 * orSize + 170 + activiteitScoreLabels[0].getWidth(), orPosY + orSize + 4, 2 * orSize + 50, orSize - 5);
		
		
		if (afdekPanel != null)
			afdekPanel.setBounds(0, 0, getSize().width, getSize().height - 50);

		if ("MW".equals(WiskOpdr.deployVariant))
		{
			klaarKnop.setBounds(10, orPosY - 30, 2 * orSize + 30, orSize - 2);
			vorigeKnop.setBounds(b - 7 * orSize - 40 - 45, orPosY + 2, 2 * orSize + 30, orSize - 2);
			nakijkKnop.setBounds(b - 10 * orSize - 40 - 55, orPosY + 2, 2 * orSize + 30, orSize - 2);
			volgendeKnop.setBounds(b - 4 * orSize - 40 - 35, orPosY + 2, 2 * orSize + 30, orSize - 2);
			//eindeKnop.setBounds(b - 4 * orSize - 40 - 35, orPosY + 2, 2 * orSize + 30, orSize - 2);
			opnieuwKnop.setBounds(b - 1 * orSize - 40 - 25, orPosY + 2, 2 * orSize + 30, orSize - 2);
			mwScoreLab.setBounds(5, orPosY + 2, 200, 20);
		}
		else if ("GR".equals(WiskOpdr.deployVariant))
		{
			klaarKnop.setBounds(b - 10 * orSize - 40 - 60, orPosY + 2, 2 * orSize + 15, orSize - 5);
			itemOpnieuwKnop.setBounds(b - 1 * orSize - 40 - 30, orPosY + 2, 2 * orSize + 20, orSize - 5);
			opnieuwKnop.setBounds(b - 1 * orSize - 40 - 30, orPosY + 2, 4 * orSize + 15, orSize - 5);
			nakijkKnop.setBounds(b - 10 * orSize - 40 - 60, orPosY + 2, 4 * orSize + 15, orSize - 5);
			volgendeKnop.setBounds(b - 4 * orSize - 40 - 40, orPosY + 2, orSize - 5, orSize - 5);
			//eindeKnop.setBounds(b - 4 * orSize - 40 - 40, orPosY + 2, orSize - 5, orSize - 5);
			vorigeKnop.setBounds(b - 7 * orSize - 40 - 50, orPosY + 2, orSize - 5, orSize - 5);

			margeRechts = 15;
			margeLinks = 17;

			volgendeKnop.setBounds(b - volgendeKnop.getWidth() - margeRechts, orPosY + 2, orSize - 5, orSize - 5);
			//eindeKnop.setBounds(b - eindeKnop.getWidth() - margeRechts, orPosY + 2, orSize - 5, orSize - 5);
			for(int i=0 ; i<this.aantalActiviteiten ; i++)
			{	or[i].setLocation(volgendeKnop.getX() - or[i].getWidth(), orPosY + 2);
			}
			//or[activiteitNr].setLocation(volgendeKnop.getX() - or[activiteitNr].getWidth(), orPosY + 2);
			vorigeKnop.setBounds(or[activiteitNr].getX() - orSize, orPosY + 2, orSize - 5, orSize - 5);
			opnieuwKnop.setBounds(vorigeKnop.getX() - opnieuwKnop.getWidth() - 5, orPosY + 2, 4 * orSize + 15, orSize - 5);
			nakijkKnop.setBounds(opnieuwKnop.getX() - nakijkKnop.getWidth() - 5, orPosY + 2, 4 * orSize + 15, orSize - 5);
			itemOpnieuwKnop.setBounds(vorigeKnop.getX() - itemOpnieuwKnop.getWidth() - 5, orPosY + 2, 2 * orSize + 20, orSize - 5);
			klaarKnop.setBounds(itemOpnieuwKnop.getX() - klaarKnop.getWidth() - 5, orPosY + 2, 2 * orSize + 15, orSize - 5);
			mwScoreLab.setBounds(margeLinks + 18, orPosY - 3, 200, 20);
		}
		else
		{
			vorigeKnop.setBounds(b - 4 * orSize - 30 - 40, orPosY + 4, 2 * orSize, orSize - 5);
			volgendeKnop.setBounds(b - 2 * orSize - 30 - 30, orPosY + 4, 2 * orSize, orSize - 5);
			eindeKnop.setBounds(b - 2 * orSize - 30 - 30, orPosY + 4, 3 * orSize, orSize - 5);
			if (WiskOpdr.zoefi)
				klaarKnop.setBounds(getSize().width - 90 - (3 * orSize + 30), orPosY - 2 * orSize, 4 * orSize + 30, 3 * orSize / 2 - 5);
			else
				klaarKnop.setBounds(getSize().width - 90 - (2 * orSize + 30), orPosY - 30, 2 * orSize + 30, orSize - 5);
			nakijkKnop.setBounds(orPosX + aantalOpdrMax * orSize + 2 * orSize + 90, orPosY + 4, 2 * orSize + 30, orSize - 5);
			itemOpnieuwKnop.setBounds(orPosX + aantalOpdrMax * orSize + 20, orPosY + 4, 2 * orSize + 30, orSize - 5);
			if (!itemOpnieuwKnop.isVisible())
				opnieuwKnop.setBounds(orPosX + aantalOpdrMax * orSize + 20, orPosY + 4, 2 * orSize + 50, orSize - 5);
			else
				opnieuwKnop.setBounds(orPosX + aantalOpdrMax * orSize + 2 * orSize + 70, orPosY + 4, 2 * orSize + 50, orSize - 5);
//			lockToetsCB.setBounds(getWidth() - 220, getHeight() - 23, 120, 20);
			aantalSessiesLabel.setBounds(getWidth() - 320, getHeight() - 23, 120, 20);
			lockToetsLabel.setBounds(getWidth() - 120, getHeight() - 23, 100, 20);

		}

		if (timer)
			timerPanel.setLocation(getSize().width - 50, orPosY - 65);
	}

	/**
	 * Het score-label van MW en GR versies wordt hier geupdate.
	 */
	public void setMWScoreLabel()
	{
		if (!"MW".equals(WiskOpdr.deployVariant) && !"GR".equals(WiskOpdr.deployVariant))
			return;
		String tekst = "" + (opdrachtNr + 1) + " van " + aantalOpdrachten[activiteitNr] + "  |  score:";
		int score = or[activiteitNr].geefScore(opdrachtNr + 1);
		String tussenTekst = " totaal:";
		int totaal = or[activiteitNr].geefScore();
		mwScoreLab.setContent(tekst, score, tussenTekst, totaal);
	}

	/**
	 * Transparant afdekpanel fixeert de activiteit. Muisacties zijn onmogelijk
	 * en de focus wordt weggenomen
	 */
	public void zetAfdekPanelLeeg(boolean b)
	{
		if (afdekPanel != null)
			remove(afdekPanel);
		if (b)
		{
			afdekPanel = new JPanel();
			afdekPanel.setOpaque(false);
			afdekPanel.addMouseListener(this);
			afdekPanel.setBounds(0, 0, getSize().width, getSize().height - 50);
			add(afdekPanel, 0);
			Thread startDraad = new Thread()
			{
				public void run()
				{
					try
					{
						sleep(500);
					}
					catch (InterruptedException e)
					{
					}
					afdekPanel.requestFocus();
				}
			};
			startDraad.start();
		}
	}

	/**
	 * Transparant afdekpanel fixeert de activiteit na aflopen timer en geeft
	 * mededeling. Muisacties zijn onmogelijk en de focus wordt weggenomen
	 */
	public void zetAfdekPanel(boolean b, int soort)
	{
		final int srt = soort;
		if (b)
		{
			if (afdekPanel != null)
				remove(afdekPanel);
			afdekPanel = new JPanel()
			{
				public void paintComponent(Graphics g)
				{
					super.paintComponent(g);
					g.setFont(new Font("SansSerif", Font.BOLD, 22));
					if (srt == 0)
						g.setColor(new Color(255, 180, 180));
					else
						g.setColor(new Color(180, 255, 180));
					g.fillRoundRect(570, 330, 150, 60, 30, 30);
					g.setColor(Color.black);
					if (srt == 0)
						g.drawString(WiskOpdr.rb.getString("ONS_timeisup"), 580, 365);
					else
						g.drawString(WiskOpdr.rb.getString("ONS_timeready"), 580, 365);
				}
			};
			afdekPanel.setOpaque(false);
			afdekPanel.addMouseListener(this);
			afdekPanel.setBounds(0, 0, getSize().width, getSize().height - 50);
		}
		if (b)
		{
			add(afdekPanel, 0);
			add(opnieuwKnop, 0);
			repaint();
			Thread startDraad = new Thread()
			{
				public void run()
				{
					try
					{
						sleep(500);
					}
					catch (InterruptedException e)
					{
					}
					afdekPanel.requestFocus();
					repaint();
				}
			};
			startDraad.start();
		}
		if (!b && afdekPanel != null)
			remove(afdekPanel);
	}

	/**
	 * Maakt panel met deelscores zichtbaar mbv een popup-venster
	 */
	public void zetScoresObjectivesPanel() {
        int aantalDiagrammen = 0;
        for(int k = 0; k < objectives.length; k++)
        {	int somObjective = 0;
        	for(int i = 0; i < scoresMaxObjectives.length; i++)
        		for(int j = 0; j < scoresMaxObjectives[i].length; j++)
        		{	try{
        			for(int l = 0; l < scoresMaxObjectives[i][j][k].length; l++)
        				somObjective += scoresMaxObjectives[i][j][k][l];
        			}
        			catch(Exception e){somObjective = 0;
        			}
        		}
        	if(somObjective > 0) aantalDiagrammen++;
        }
		
		scoresObjectivesDialog = DialogFacade.newInstance(this,WiskOpdr.rb.getString("deelscores"), true);
        scoresObjectivesPanel = new ScoresObjectivesPanel(getScoresObjectivesForDiagram());
        if(aantalDiagrammen < 4)
        	scoresObjectivesPanel.setBounds(0, 0, 400 * aantalDiagrammen, 350);
        else 
        	scoresObjectivesPanel.setBounds(0, 0, 1200, 700);
        scoresObjectivesDialog.getContentPane().add(scoresObjectivesPanel);
        scoresObjectivesDialog.setSize(scoresObjectivesPanel.getSize());
    }
	
	/**
	 * Maakt panel met misconcepties zichtbaar mbv een popup-venster
	 */
	public void zetViewMisconceptionsPanel() {
        int aantalDiagrammen = misconceptions.length;
        	
        /*for(int k = 0; k < misconceptions.length; k++)
        {	int somObjective = 0;
        	for(int i = 0; i < scoresMaxObjectives.length; i++)
        		for(int j = 0; j < scoresMaxObjectives[i].length; j++)
        		{	try{
        			for(int l = 0; l < scoresMaxObjectives[i][j][k].length; l++)
        				somObjective += scoresMaxObjectives[i][j][k][l];
        			}
        			catch(Exception e){somObjective = 0;
        			}
        		}
        	if(somObjective > 0) aantalDiagrammen++;
        }*/
		
		viewMisconceptionsDialog = DialogFacade.newInstance(this,WiskOpdr.rb.getString("misconceptions"), true);
		viewMisconceptionsPanel = new ScoresObjectivesPanel(getMisconceptionsForDiagram());
		viewMisconceptionsPanel.zetKleurNeutraal();
        if(aantalDiagrammen < 4)
        	viewMisconceptionsPanel.setBounds(0, 0, 400 * aantalDiagrammen, 350);
        else 
        	viewMisconceptionsPanel.setBounds(0, 0, 1200, 700);
        viewMisconceptionsDialog.getContentPane().add(viewMisconceptionsPanel);
        viewMisconceptionsDialog.setSize(viewMisconceptionsPanel.getSize());
    }

	/**
	 * Instellingen onder de knop "Opties" worden hier gezet
	 */
	public void zetInstellingen(Hashtable h)
	{
		int fontSize = 12;
		boolean maalTeken = false;
		boolean diffOperatoren = false;
		int keyBoardNr = 0;
		int writeMathSetNr = 0;
		boolean woordFormule = false;
		boolean tweeHoofdletterVar = false;
		boolean timer = false;
		int timeLimit = 60;
		boolean opnieuw = false;
		boolean itemOpnieuw = false;
		boolean checkPerOpdracht = false;
		boolean hoekGraden = false;
		boolean bolletjesZichtbaar = true;
		boolean volgendeKnopZichtbaar = false;
		boolean vorigeKnopZichtbaar = false;
		boolean pagina = false;
		boolean formTimes = true;
		int navigatieSize = 12;
		String fontName = "SansSerif";
		boolean fontOvererving = false;
		boolean fontOverervingForm = false;
		int margeLinks = 18;
		int margeRechts = 15;
		int margeBoven = 15;
		int margeOnder = 15;
		boolean fToets = true;
		boolean globalParam = false;
		boolean voortgang = false;
		boolean condNav = false;
		int condPerc = 100;
		boolean condNavPerc = true;
		boolean condNavVoorwaarden = false;
		int[][][] navVoorwaarden = null;
		boolean allesCorrectNodig = false;
		String[] urls = null;
		int[] grensScores = null;
		boolean abcDeelOpdr = false;
		boolean zelftoetsGeenCorr = false;
		boolean eerderGeenCorr = false;
		boolean significantie = false;
		boolean hasObjectives = false;
		String[][] objectives = null;
		String[] categorieString = null;
		boolean hasMisconceptions = false;
		String[][] misconceptions = null;
		String[] mccCategorieString = null;
		boolean scoresZichtbaar = true;
		
		if (h != null && h.containsKey("fontSize"))
			fontSize = ((Integer) h.get("fontSize")).intValue();
		if (h != null && h.containsKey("maalTeken"))
			maalTeken = ((Boolean) h.get("maalTeken")).booleanValue();
		if (h != null && h.containsKey("diffOperatoren"))
			diffOperatoren = ((Boolean) h.get("diffOperatoren")).booleanValue();
		if (h != null && h.containsKey("keyBoardNr"))
			keyBoardNr = ((Integer) h.get("keyBoardNr")).intValue();
		if (h != null && h.containsKey("writeMathSetNr"))
			writeMathSetNr = ((Integer) h.get("writeMathSetNr")).intValue();
		if (h != null && h.containsKey("woordFormule"))
			woordFormule = ((Boolean) h.get("woordFormule")).booleanValue();
		if (h != null && h.containsKey("tweeHoofdletterVar"))
			tweeHoofdletterVar = ((Boolean) h.get("tweeHoofdletterVar")).booleanValue();
		if (h != null && h.containsKey("timer"))
			timer = ((Boolean) h.get("timer")).booleanValue();
		if (h != null && h.containsKey("timeLimit"))
			timeLimit = ((Integer) h.get("timeLimit")).intValue();
		if (h != null && h.containsKey("opnieuw"))
			opnieuw = ((Boolean) h.get("opnieuw")).booleanValue();
		if (h != null && h.containsKey("itemOpnieuw"))
			itemOpnieuw = ((Boolean) h.get("itemOpnieuw")).booleanValue();
		if (h != null && h.containsKey("checkPerOpdracht"))
			checkPerOpdracht = ((Boolean) h.get("checkPerOpdracht")).booleanValue();
		if (h != null && h.containsKey("hoekGraden"))
			hoekGraden = ((Boolean) h.get("hoekGraden")).booleanValue();
		if (h != null && h.containsKey("bolletjesZichtbaar"))
			bolletjesZichtbaar = ((Boolean) h.get("bolletjesZichtbaar")).booleanValue();
		if (h != null && h.containsKey("volgendeKnopZichtbaar"))
			volgendeKnopZichtbaar = ((Boolean) h.get("volgendeKnopZichtbaar")).booleanValue();
		if (h != null && h.containsKey("vorigeKnopZichtbaar"))
			vorigeKnopZichtbaar = ((Boolean) h.get("vorigeKnopZichtbaar")).booleanValue();
		if (h != null && h.containsKey("pagina"))
			pagina = ((Boolean) h.get("pagina")).booleanValue();
		if (h != null && h.containsKey("formTimes"))
			formTimes = ((Boolean) h.get("formTimes")).booleanValue();
		if (h != null && h.containsKey("navigatieSize"))
			navigatieSize = ((Integer) h.get("navigatieSize")).intValue();
		if (h != null && h.containsKey("fontName"))
			fontName = (String) h.get("fontName");
		if (h != null && h.containsKey("fontOvererving"))
			fontOvererving = ((Boolean) h.get("fontOvererving")).booleanValue();
		if (h != null && h.containsKey("fontOverervingForm"))
		fontOverervingForm = ((Boolean) h.get("fontOverervingForm")).booleanValue();
		if (h != null && h.containsKey("margeLinks"))
			margeLinks = ((Integer) h.get("margeLinks")).intValue();
		if (h != null && h.containsKey("margeRechts"))
			margeRechts = ((Integer) h.get("margeRechts")).intValue();
		if (h != null && h.containsKey("margeBoven"))
			margeBoven = ((Integer) h.get("margeBoven")).intValue();
		if (h != null && h.containsKey("margeOnder"))
			margeOnder = ((Integer) h.get("margeOnder")).intValue();
		if (h != null && h.containsKey("fToets"))
			fToets = ((Boolean) h.get("fToets")).booleanValue();
		if (h != null && h.containsKey("globalParam"))
			globalParam = ((Boolean) h.get("globalParam")).booleanValue();
		if (h != null && h.containsKey("voortgang"))
			voortgang = ((Boolean) h.get("voortgang")).booleanValue();
		if (h != null && h.containsKey("condNav"))
			condNav = ((Boolean) h.get("condNav")).booleanValue();
		if (h != null && h.containsKey("abcDeelOpdr"))
			abcDeelOpdr = ((Boolean) h.get("abcDeelOpdr")).booleanValue();
		if (h != null && h.containsKey("condNavPerc"))
			condNavPerc = ((Boolean) h.get("condNavPerc")).booleanValue();
		if (h != null && h.containsKey("condNavVoorwaarden"))
			condNavVoorwaarden = ((Boolean) h.get("condNavVoorwaarden")).booleanValue();
		if (h != null && h.containsKey("condPerc"))
			condPerc = ((Integer) h.get("condPerc")).intValue();
		if (h != null && h.containsKey("urls"))
			urls = (String[]) h.get("urls");
		if (h != null && h.containsKey("navVoorwaarden"))
			navVoorwaarden = (int[][][]) h.get("navVoorwaarden");
		if (h != null && h.containsKey("allesCorrectNodig"))
			allesCorrectNodig = ((Boolean) h.get("allesCorrectNodig")).booleanValue();
		if (h != null && h.containsKey("grensScores"))
			grensScores = (int[]) h.get("grensScores");
		if (h != null && h.containsKey("zelftoetsGeenCorr"))
			zelftoetsGeenCorr = ((Boolean) h.get("zelftoetsGeenCorr")).booleanValue();
		if (h != null && h.containsKey("eerderGeenCorr"))
			eerderGeenCorr = ((Boolean) h.get("eerderGeenCorr")).booleanValue();
		if (h != null && h.containsKey("significantie"))
			significantie = ((Boolean) h.get("significantie")).booleanValue();
		if (h != null && h.containsKey("hasObjectives"))
			hasObjectives = ((Boolean) h.get("hasObjectives")).booleanValue();
		if (h != null && h.containsKey("objectives"))
			try	{	
				objectives = (String[][]) h.get("objectives");
			} catch(Exception ex){
				
			}
		if (h != null && h.containsKey("categorieString"))
			categorieString = (String[]) h.get("categorieString");
		
		if (h != null && h.containsKey("hasMisconceptions"))
			hasObjectives = ((Boolean) h.get("hasMisconceptions")).booleanValue();
		if (h != null && h.containsKey("misconceptions"))
			try	{	
				misconceptions = (String[][]) h.get("misconceptions");
			} catch(Exception ex){
				
			}
		if (h != null && h.containsKey("mccCategorieString"))
			mccCategorieString = (String[]) h.get("mccCategorieString");
		if (h != null && h.containsKey("scoresZichtbaar"))
			scoresZichtbaar = ((Boolean) h.get("scoresZichtbaar")).booleanValue();

		WiskOpdr.zetFont(fontName, fontSize);
		WiskOpdr.setFormTimes(formTimes);
		
		TekstVakPanel.zetFontOvererving(fontOvererving);
		AntwoordFormuleVak.zetFontOverervingForm(fontOverervingForm);
		SimpelAntwoordFormuleVak.zetFontOverervingForm(fontOverervingForm);
		AntwoordVergelijkingVak.zetFontOverervingForm(fontOverervingForm);
		SimpelAntwoordVergelijkingVak.zetFontOverervingForm(fontOverervingForm);
		
		setFont(WiskOpdr.tekstFont);

		setNavigatieSize(navigatieSize);
		if ("GR".equals(WiskOpdr.deployVariant))
			orSize = 21;
		orPosY = getSize().height - (2 * orSize);

		// if(hasObjectives)
		WiskOpdr.setObjectives(objectives);
		WiskOpdr.setCategories(categorieString);
		
		// if(hasMisconceptions)
		WiskOpdr.setMisconceptions(misconceptions);
		WiskOpdr.setCategories(mccCategorieString);

		FormuleTeken.zetMaalTeken(maalTeken);
		FormuleTeken.zetDiffOperatoren(diffOperatoren);
		FormuleParser.zetDiffOperatoren(diffOperatoren);
		FormuleParser.zetWoordFormule(woordFormule);
		FormuleParser.zetTweeHoofdletterVariabele(tweeHoofdletterVar);
		FormuleParser.zetSignificantie(significantie);
		AntwoordFormuleVakEditPanel.zetSignificantieAan(significantie);
		AntwoordVergelijkingVakEditPanel.zetSignificantieAan(significantie);

		//setTimer(timer, timeLimit);
		this.timer = timer;
		this.timeLimit = timeLimit;
		this.opnieuwMogelijk = opnieuw;
		this.allesOpnieuwGR = opnieuw;
		this.itemOpnieuwMogelijk = itemOpnieuw;
		this.checkPerOpdracht = checkPerOpdracht;
		this.hoekGraden = hoekGraden;
		this.bolletjesZichtbaar = bolletjesZichtbaar;
		this.volgendeKnopZichtbaar = volgendeKnopZichtbaar;
		this.vorigeKnopZichtbaar = vorigeKnopZichtbaar;
		this.pagina = pagina;
		this.globalParam = globalParam;
		this.voortgang = voortgang;
		this.condNav = condNav;
		this.condNavPerc = condNavPerc;
		this.condNavVoorwaarden = condNavVoorwaarden;
		this.condPerc = condPerc;
		this.navVoorwaarden = navVoorwaarden;
		this.allesCorrectNodig = allesCorrectNodig;
		this.urls = urls;
		this.grensScores = grensScores;
		this.abcDeelOpdr = abcDeelOpdr;
		this.zelftoetsGeenCorr = zelftoetsGeenCorr;
		this.eerderGeenCorr = eerderGeenCorr;
		this.objectives = objectives;
		this.categorieString = categorieString;
		this.misconceptions = misconceptions;
		this.mccCategorieString = mccCategorieString;
		this.scoresZichtbaar = scoresZichtbaar;

		Expressie.zetHoekGraden(hoekGraden);
		zetMarges(margeLinks, margeRechts, margeBoven, margeOnder);
		WiskOpdr.setFToets(fToets);

		if(condNav)
		{	setCondNavPerc(condNavPerc, condPerc);
			setCondNavVoorwaarden(condNavVoorwaarden, navVoorwaarden);
		}
		setAbcDeelOpdr(abcDeelOpdr);
		zetScoresZichtbaar(scoresZichtbaar);
		 

	}

	/**
	 * Marges worden gezet. Hebben geen effect op de versies voor de uitgevers
	 */
	public void zetMarges(int margeLinks, int margeRechts, int margeBoven, int margeOnder)
	{
		if ("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant))
			return;
		this.margeLinks = margeLinks;
		this.margeRechts = margeRechts;
		this.margeBoven = margeBoven;
		this.margeOnder = margeOnder;
		opdrContainer.zetMarges(margeLinks, margeBoven);
	}

	/**
	 * Grootte van de navigatiebolletjes wordt ingesteld
	 */
	public void setNavigatieSize(int navigatieSize)
	{
		orSize = navigatieSize * 2 + 1;
		OpdrNavStruct.navigatieSize = navigatieSize;
	}

	/**
	 * Timer wordt neergezet en gestart (aangeroepen door zetInstellingen)
	 */
	public void setTimer(boolean b, int timeLimit)
	{
		if (timerPanel == null)
		{
			timerPanel = new TimerPanel(orSize + 25, orSize + 40);
			timerPanel.addActionListener(this);
			add(timerPanel, 0);
		}
		timerPanel.setVisible(b);
		if (b)
		{
			timerPanel.zetTijdMax(timeLimit);
			timerPanel.zetInstelbaar(false);
			timerPanel.start();
			timer = true;
		}
	}

	/**
	 * Geeft achtergrondkleur door aan componenten
	 */
	public void setBackground(Color c)
	{
		super.setBackground(c);
		Component[] components = getComponents();
		for (int i = 0; i < components.length; i++)
		{
			if (components[i] instanceof JPanel)
				components[i].setBackground(c);
		}
	}

	/**
	 * Geeft setFont door aan Opdrachtcontainer
	 */
	public void setFont(Font f)
	{
		opdrContainer.setFont(f);
	}

	public void setSize(int b, int h)
	{
		super.setSize(b, h);
		opdrContainer.setSize(b, h);
		layoutGui(b, h);
	}

	/**
	 * Aangeroepen door de DWO in review mode (hiermee kan dezelfde opgave bij
	 * verschillende leerlingen worden doorgebladerd
	 */
	public void setReviewLocation(int loc)
	{
		reviewLocation = loc;
	}

	/**
	 * Aangeroepen door de DWO in review mode (hiermee verschijnt een lege
	 * opdracht in het geval er geen suspenddata zijn)
	 */
	public void setEmptyState()
	{
		zetToetsLocked("completed".equals(api.LMSGetValue("cmi.completion_status"))); // Wim: toetslocked blijft op 'true' staan.
		for (int i = 0; i < aantalActiviteiten; i++)
		{
			int totaal = 0;
			int voortgangPerc = 0;
			for (int j = 0; j < aantalOpdrachten[i]; j++)
			{
				this.opdrachtNr = j;
				states[i][j] = null;
				or[i].zetGemaakt(j + 1, false);
				or[i].zetScore(j + 1, 0);
			}
			activiteitScoreLabels[i].setText(WiskOpdr.rb.getString("score") + totaal);
			if (aantalActiviteiten == 1)
			{	activiteitScoreLabels[0].setText(WiskOpdr.rb.getString("totaal") + totaal);
				if(voortgang)
					activiteitScoreLabels[0].setText(WiskOpdr.rb.getString("voortgang") + voortgangPerc + "%");
			}
		}
		if (activiteitNr == 0 && reviewLocation != -1)
			opdrachtNr = reviewLocation;
		opdrContainer.zetOpdracht(opdrachten[activiteitNr][opdrachtNr]);
		stelNavigatieIn(activiteitNr, opdrachtNr);
		or[activiteitNr].setSelected(opdrachtNr+1);
	}

	/**
	 * De status van het applet wordt gezet met behulp de suspenddata.
	 * (aangeroepen door setState van WiskOpdr)
	 * @param patch 
	 */
	public void setState(Hashtable h) { setState(h,false); }
	
	private void setState(Hashtable h, boolean patch)
	{
		//int aantalActiviteiten = this.aantalActiviteiten;
		int activiteitNr = 0;
		//int maxAantalOpdrachten = 20;
		int opdrachtNr = 0;
		//int[] aantalOpdrachten = this.aantalOpdrachten;
		Hashtable[][] opdrContStates = null;
		boolean[][] orGoedFout = opdrachtenCorrect; // = (boolean[][]) h.get("orGoedFout");
		int[][] orScores = null;
		int[][] scores = null;
		String[][] orTimes = null;
		int[] aantalNakijken = this.aantalNakijken;
		int[][] strafpunten = null;
		boolean locked = false;
		boolean zelftoetsNagekeken = false;
		int aantalSessies = 0;
		int[][][][] scoresObjectives = null;
		int[][][][] scoresMaxObjectives = null;
		int[][][][] possibleMisconceptions = null;
		int[][][][] measuredMisconceptions = null;
		boolean[][] bezocht = null;
		
		Object shareState = h.get(ShareAction.SHARE_MAP);
		if(shareState instanceof Hashtable) 
			ShareAction.setSharedState((Hashtable) shareState);
		else
			ShareAction.setSharedState(null);
		
		//if (h.containsKey("aantalActiviteiten"))
		//	aantalActiviteiten = ((Number) h.get("aantalActiviteiten")).intValue();
		if (h.containsKey("activiteitNr"))
			activiteitNr = ((Number) h.get("activiteitNr")).intValue();
		//if (h.containsKey("maxAantalOpdrachten"))
		//	maxAantalOpdrachten = ((Number) h.get("maxAantalOpdrachten")).intValue();
		if (h.containsKey("opdrachtNr"))
			opdrachtNr = ((Number) h.get("opdrachtNr")).intValue();
		//if (h.containsKey("aantalOpdrachten"))
		//	aantalOpdrachten = toIntArray(h.get("aantalOpdrachten"));
		if (h.containsKey("opdrContStates"))
			opdrContStates = toHashtableArrayArray(h.get("opdrContStates"));
		if (h.containsKey("orGoedFout"))
			orGoedFout = toBooleanArrayArray(h.get("orGoedFout"));
		if (h.containsKey("orScores"))
			orScores = toIntArrayArray(h.get("orScores"));
		else
			orScores = new int[aantalActiviteiten][maxAantalOpdrachten];
		if(h.containsKey("scores"))
			scores = toIntArrayArray(h.get("scores"));
		else
			scores = new int[aantalActiviteiten][maxAantalOpdrachten];
		orTimes = toStringArrayArray(h.get("orTimes"));
		if (h.containsKey("aantalNakijken"))
			aantalNakijken = toIntArray(h.get("aantalNakijken"));
		if (h.containsKey("strafpunten"))
			strafpunten = toIntArrayArray(h.get("strafpunten"));
		if (h.containsKey("locked"))
			locked = ((Boolean) h.get("locked")).booleanValue();
		if (h.containsKey("zelftoetsNagekeken"))
			zelftoetsNagekeken = ((Boolean) h.get("zelftoetsNagekeken")).booleanValue();
		if (h.containsKey("aantalSessies"))
			aantalSessies = ((Number) h.get("aantalSessies")).intValue();
		if (h.containsKey("scoresObjectives"))
			scoresObjectives = (int[][][][]) h.get("scoresObjectives");
		if (h.containsKey("scoresMaxObjectives"))
			scoresMaxObjectives = (int[][][][]) h.get("scoresMaxObjectives");
		if (h.containsKey("possibleMisconceptions"))
			possibleMisconceptions = (int[][][][]) h.get("possibleMisconceptions");
		if (h.containsKey("measuredMisconceptions"))
			measuredMisconceptions = (int[][][][]) h.get("measuredMisconceptions");
		if (h.containsKey("bezocht"))
			try{	
				bezocht = toBooleanArrayArray( h.get("bezocht") );
			}
			catch(Exception e)
			{
				bezocht = new boolean[aantalActiviteiten][];
				bezocht[0] = toBooleanArray( h.get("bezocht") );
				if(aantalActiviteiten > 1)
					for(int j = 1; j < aantalActiviteiten; j++)
					{	bezocht[j] = new boolean[aantalOpdrachten[j]];
						for(int i = 0; i < aantalOpdrachten[j]; i++)
							bezocht[j][i] = false;
					}
			}
		if (activiteitNr == 0 && reviewLocation >=0 && reviewLocation < aantalOpdrachten[0])
			opdrachtNr = reviewLocation;

		//this.aantalActiviteiten = aantalActiviteiten;
		if (strafpunten != null)
			this.strafpunten = strafpunten;
		if(orGoedFout[0].length < aantalOpdrachten[0]) {
			boolean x[] = orGoedFout[0];
			orGoedFout[0] = new boolean[aantalOpdrachten[0]];
			System.arraycopy(x, 0, orGoedFout[0], 0, x.length);
		}
		this.opdrachtenCorrect = orGoedFout;

		if(bezocht == null)
		{	bezocht = new boolean[aantalActiviteiten][];
			for(int j = 0; j < aantalActiviteiten; j++)
			{	bezocht[j] = new boolean[aantalOpdrachten[j]];
				for(int i = 0; i < aantalOpdrachten[j]; i++)
					bezocht[j][i] = false;
			}
		//	for(int i = 0; i < opdrachtNr; i++)
		//		bezocht[activiteitNr][i] = true;
			bezocht[activiteitNr][opdrachtNr] = true;
			
			//for(int i = opdrachtNr + 1; i < aantalOpdrachten[activiteitNr]; i++)
			//	bezocht[activiteitNr][i] = false;
		}
		
		this.bezocht = bezocht;
		this.scores = scores;
		
		if (scoresObjectives != null)
		{
			this.scoresObjectives = scoresObjectives;
			this.scoresMaxObjectives = scoresMaxObjectives;
		}
		if (possibleMisconceptions != null)
		{
			this.possibleMisconceptions = possibleMisconceptions;
			this.measuredMisconceptions = measuredMisconceptions;
		}
		int sumScoresMaxObjectives = 0;
		try
		{
			for (int i = 0; i < scoresMaxObjectives.length; i++)
				for (int j = 0; j < scoresMaxObjectives[i].length; j++)
					for (int k = 0; k < scoresMaxObjectives[i][j].length; k++)
						for (int l = 0; l < scoresMaxObjectives[i][j][k].length; l++)
							sumScoresMaxObjectives += scoresMaxObjectives[i][j][k][l];
		}
		catch (Exception e)
		{
		}

		if (sumScoresMaxObjectives > 0)
			objectivesAanwezig = true;
		else
			objectivesAanwezig = false;
		scoresObjectivesKnop.setVisible(objectivesAanwezig);
		
		viewMisconceptionsKnop.setVisible(possibleMisconceptions!=null);

		boolean allCorrect = true;
		
		for (int i = 0; i < aantalActiviteiten; i++)
		{
			this.activiteitNr = i;
			this.aantalOpdrachten[i] = aantalOpdrachten[i];
			this.aantalNakijken[i] = aantalNakijken[i];
			int totaal = 0;			
			for (int j = 0; j < aantalOpdrachten[i]; j++)
			{
				this.opdrachtNr = j;
				Hashtable[] opdrachtStates = opdrContStates[i];
				states[i][j] = j < opdrachtStates.length ? opdrachtStates[j] : null; // NPE

// XXX LET OP de html5 suspend_data heeft een kunstmatige tekstvak, de applet variant niet.
// dat betekent dat de interactiePanelStates één nivo dieper zit.
				
				if(patch && states[i][j] != null)
				{
					Object object = states[i][j].get("interactiePanelStates");
					List l = (List) object;
					if(l.size() > 5)
					{ 	
						object = l.get(5);
						Map m = (Map) object;
						object = m.get("interactiePanelStates");
						l = (List) object;
						for(int x=0; x<5; x++) l.add(0, null);
						states[i][j].put("interactiePanelStates", object);
					} else {
						//states[i][j].remove("interactiePanelStates");
						//states[i][j] = null;
					}
				}

				if (orTimes != null)
					times[i][j] = orTimes[i][j];
				else
					times[i][j] = null;
				
				if (mode != 3 || api != null && api.LMSGetValue("USER_GROUP").equals("UG_TEACHER") || lessonMode.equals("review"))
				{
					or[i].zetGemaakt(j + 1, getBoolean(orGoedFout, i, j));
					allCorrect = allCorrect && getBoolean(orGoedFout, i, j);
					or[i].zetScore(j + 1, getInt(orScores,i,j));
				}
				totaal += getInt(orScores,i,j);
			}
			if (mode == 2 && totaal > 0)
				totaal = Math.max(0, totaal - (aantalNakijken[i] - 1) * nakijkStraf);
			if (mode != 3 || api != null && api.LMSGetValue("USER_GROUP").equals("UG_TEACHER") || lessonMode.equals("review"))
			{
				activiteitScoreLabels[i].setText(WiskOpdr.rb.getString("score") + totaal);
				if (aantalActiviteiten == 1)
				{	activiteitScoreLabels[0].setText(WiskOpdr.rb.getString("totaal") + totaal);
				if(voortgang)
					activiteitScoreLabels[0].setText(WiskOpdr.rb.getString("voortgang") + bepaalVoortgangPercentage(activiteitNr, opdrachtNr) + "%");
				}
			}
		}
		this.activiteitNr = activiteitNr;
		this.opdrachtNr = opdrachtNr;
		this.aantalSessies = aantalSessies;

		aantalSessiesLabel.setText("Attemps: " + aantalSessies);

		aantalNakijkLabel.setText(keerNagekeken(aantalNakijken[activiteitNr]));
		if (mode == 2 && aantalNakijken[activiteitNr] > 0 && !zelftoetsGeenCorr)
			aantalNakijkLabel.setVisible(true);
		
		if (mode == 3)
		{	scoresObjectivesKnop.setVisible(false);
			viewMisconceptionsKnop.setVisible(false);
			for (int i = 0; i < aantalActiviteiten; i++)
			{
				for (int j = 0; j < aantalOpdrachten[i]; j++)
				{
					this.scores[i][j] = getInt( orScores, i, j);
					isCorrect[i][j] = getBoolean(orGoedFout, i, j);
				}
			}
		}
		
		
		
		if (mode == 3 && api != null && (api.LMSGetValue("USER_GROUP").equals("UG_TEACHER") || lessonMode.equals("review") || toetsLocked))
		{	scoresObjectivesKnop.setVisible(objectivesAanwezig);
			viewMisconceptionsKnop.setVisible(possibleMisconceptions!=null);
		
			for (int i = 0; i < aantalActiviteiten; i++)
			{
				for (int j = 0; j < aantalOpdrachten[i]; j++)
				{
					or[i].zetGemaakt(j + 1, getBoolean(orGoedFout, i, j));
					or[i].zetScore(j + 1, getInt(orScores,i,j));
				}
			}
		}

		if (states[activiteitNr][opdrachtNr] != null)
		{
			opdrContainer.zetOpdrachtPlusState(opdrachten[activiteitNr][opdrachtNr], !(gekoppeldeOpdrachten || globalParam), states[activiteitNr][opdrachtNr]);
			if (mode == EINDTOETS && (lessonMode.equals("review") || toetsLocked))
			{
				opdrContainer.kijkNa();
				opdrContainer.zetNagekeken(true);
			}
			opdrContainer.setInitialTime(times[activiteitNr][opdrachtNr]);
		}
		else
		{
			opdrContainer.zetOpdracht(opdrachten[activiteitNr][opdrachtNr]);
			opdrContainer.setInitialTime(null);
		}

		or[0].setVisible(false);
		or[activiteitNr].setVisible(bolletjesZichtbaar || lessonMode.equals("review"));
		or[activiteitNr].setSelected(opdrachtNr + 1);
		actKeuzePanel.setItem(activiteitNr);

		if ("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant))
			setMWScoreLabel();

		vorigeKnop.setEnabled(opdrachtNr > 0);

		produceAction("select");

		if (locked)
			requestFocus();
		if (timerPanel != null && locked)
			timerPanel.stop();
		if (zelftoetsGeenCorr)
			zetAfdekPanelLeeg(locked);
		else if (allCorrect)
			zetAfdekPanel(locked, 1);
		else
			zetAfdekPanel(locked, 0);

		this.zelftoetsNagekeken = zelftoetsNagekeken;
		
		nakijkKnop.setEnabled(lessonMode.equals("review") || !zelftoetsNagekeken && suspendDataCompleted(activiteitNr, opdrachtNr));
		scoresObjectivesKnop.setEnabled((mode != 2 && mode !=3) || lessonMode.equals("review") || !zelftoetsNagekeken && suspendDataCompleted(activiteitNr, opdrachtNr) 
				|| (mode == 3 && api != null && (api.LMSGetValue("USER_GROUP").equals("UG_TEACHER") || lessonMode.equals("review") || toetsLocked)));
		viewMisconceptionsKnop.setEnabled((mode != 2 && mode !=3) || lessonMode.equals("review") || !zelftoetsNagekeken && suspendDataCompleted(activiteitNr, opdrachtNr) 
				|| (mode == 3 && api != null && (api.LMSGetValue("USER_GROUP").equals("UG_TEACHER") || lessonMode.equals("review") || toetsLocked)));
		
		vorigeKnop.setVisible(vorigeKnopZichtbaar || !bolletjesZichtbaar && (zelftoetsNagekeken||toetsLocked));
	
		if(eerderGeenCorr)
		{	boolean alBezocht = false;
			for(int i = opdrachtNr + 1; i < aantalOpdrachten[activiteitNr]; i++)
				if(getBoolean(bezocht, activiteitNr, i))
					alBezocht = true;
			zetAfdekPanelLeeg(alBezocht);
		}
		if(zelftoetsNagekeken && zelftoetsGeenCorr)
			zetAfdekPanelLeeg(true);
		
		for (int i = 0; i < aantalActiviteiten; i++)
			for (int j = 0; j < aantalOpdrachten[i]; j++)
				if (mode != 3 || api != null && api.LMSGetValue("USER_GROUP").equals("UG_TEACHER") || lessonMode.equals("review"))
					stelNavigatieIn(i, j);
		
		stelNavigatieIn(activiteitNr, opdrachtNr);

	}

	/**
	 * Save access to boolean array[][] that is too short.
	 * @param array
	 * @param i
	 * @param j
	 * @return
	 */
	private boolean getBoolean(boolean[][] array, int i, int j) {
		if(i >= array.length) return false;
		boolean[] a = array[i];
		if(j >= a.length) return false;
		return a[j];
	}

	private int getInt(int[][] array, int i, int j) {
		if(i >= array.length) return 0;
		int[] a = array[i];
		if(j >= a.length) return 0;
		return a[j];
	}

	public static String[][] toStringArrayArray(Object object)
	{
		if (object == null || object instanceof String[][])
			return (String[][]) object;
		if (object instanceof List)
		{
			List list = (List) object;
			String[][] result = new String[list.size()][];
			for (int i = 0; i < result.length; i++)
			{
				result[i] = toStringArray(list.get(i));
			}
			return result;
		}
		return null;
	}

	public static String[] toStringArray(Object object)
	{
		if (object == null || object instanceof String[])
			return (String[]) object;
		if (object instanceof List)
		{
			List list = (List) object;
			return (String[]) list.toArray(new String[list.size()]);
		}
		return null;
	}

	public static int[][] toIntArrayArray(Object object)
	{
		if (object == null || object instanceof int[][])
			return (int[][]) object;
		if (object instanceof List)
		{
			List list = (List) object;
			int[][] result = new int[list.size()][];
			for (int i = 0; i < result.length; i++)
			{
				result[i] = toIntArray(list.get(i));
			}
			return result;
		}
		return null;
	}

	static public boolean[][] toBooleanArrayArray(Object object)
	{
		if (object == null || object instanceof boolean[][])
			return (boolean[][]) object;
		if (object instanceof List)
		{
			List list = (List) object;
			boolean[][] result = new boolean[list.size()][];
			for (int i = 0; i < result.length; i++)
			{
				result[i] = toBooleanArray(list.get(i));
			}
			return result;
		}
		return null;
	}

	public static boolean[] toBooleanArray(Object object)
	{
		if (object == null || object instanceof boolean[])
			return (boolean[]) object;
		if (object instanceof List)
		{
			List list = (List) object;
			boolean[] result = new boolean[list.size()];
			for (int i = 0; i < result.length; i++)
			{
				result[i] = Boolean.TRUE.equals(list.get(i));
			}
			return result;
		}
		return null;
	}

	public static int[] toIntArray(Object object)
	{
		if (object == null || object instanceof int[])
			return (int[]) object;
		if (object instanceof List)
		{
			List c = (List) object;
			int[] result = new int[c.size()];
			for (int i = 0; i < result.length; i++)
			{
				final Object o = c.get(i);
				if(o instanceof Number)
					result[i] = ((Number) o).intValue();
			}
			return result;
		}
		return null;
	}

	private static Hashtable[][] toHashtableArrayArray(Object object)
	{
		if (object == null || object instanceof Hashtable[][])
			return (Hashtable[][]) object;
		if (object instanceof List)
		{
			List list = (List) object;
			Hashtable[][] result = new Hashtable[list.size()][];
			for (int i = 0; i < result.length; i++)
			{
				result[i] = toHashtableArray(list.get(i));
			}
			return result;
		}
		return null;
	}

	public static Hashtable[] toHashtableArray(Object object)
	{
		if (object == null || object instanceof Hashtable[])
			return (Hashtable[]) object;
		if (object instanceof List)
		{
			List list = (List) object;
			Hashtable[] result = new Hashtable[list.size()];
			for (int i = 0; i < result.length; i++)
			{
				result[i] = toHashtable(list.get(i));
			}
			return result;
		}
		return null;
	}

	static Hashtable toHashtable(Object object)
	{
		if (object == null || object instanceof Hashtable)
			return (Hashtable) object;
		if (object instanceof Map)
		{	Map map = (Map)object;
			Hashtable hash = new Hashtable();
			Iterator keys = map.keySet().iterator();
			while (keys.hasNext()) {
				Object key = (Object) keys.next();
				Object value = map.get(key);
				if(value != null) hash.put(key, value);
			}
			return hash;
		}
		return null;
	}

	/**
	 * De status van het applet wordt opgevraagd. (aangeroepen door getState van
	 * WiskOpdr)
	 */
	public Hashtable getState()
	{
		//int aantalActiviteiten = 0;
		int activiteitNr = 0;
		int opdrachtNr = 0;
		//int maxAantalOpdrachten = 20;
		//int[] aantalOpdrachten = null;
		Hashtable[][] opdrContStates = null;
		boolean[][] orGoedFout = null;
		int[][] orScores = null;
		int[][] scores = null;
		String[][] orTimes = null;
		int[] aantalNakijken = null;
		int[][] strafpunten = null;
		boolean locked = false;
		boolean zelftoetsNagekeken = false;
		int aantalSessies = 0;
		int[][][][] scoresObjectives = null;
		int[][][][] scoresMaxObjectives = null;
		Hashtable scoresPerObjective = null;
		int[][][][] possibleMisconceptions = null;
		int[][][][] measuredMisconceptions = null;
		boolean[][] bezocht = null;

		//aantalActiviteiten = this.aantalActiviteiten;
		activiteitNr = this.activiteitNr;
		opdrachtNr = this.opdrachtNr;
		//maxAantalOpdrachten = this.maxAantalOpdrachten;
		strafpunten = this.strafpunten;
		//scores = this.scores;
		//aantalOpdrachten = new int[aantalActiviteiten];
		aantalNakijken = new int[aantalActiviteiten];
		orGoedFout = new boolean[aantalActiviteiten][];
		orScores = new int[aantalActiviteiten][];
		scores = this.scores;
		orTimes = new String[aantalActiviteiten][];
		opdrContStates = new Hashtable[aantalActiviteiten][];
		locked = afdekPanel != null && afdekPanel.isVisible();
		zelftoetsNagekeken = this.zelftoetsNagekeken;
		aantalSessies = this.aantalSessies;
		scoresMaxObjectives = this.scoresMaxObjectives;
		scoresObjectives = this.scoresObjectives;
		possibleMisconceptions = this.possibleMisconceptions;
		measuredMisconceptions = this.measuredMisconceptions;
		bezocht = this.bezocht;

		double[][] scorePercObjectives = getScoresObjectives();
		scoresPerObjective = new Hashtable();
		for (int i = 0; objectives != null && i < objectives.length; i++)
		{
			scoresPerObjective.put(objectives[i], scorePercObjectives[i]);
		}

		if(!"GR".equals(WiskOpdr.deployVariant) && !"MW".equals(WiskOpdr.deployVariant))
			opdrContainer.closePopups();
		opdrContainer.sessionStop();
		// toegevoegd 20100820 voor de zekerheid alleen voor MW
		if (mode == 3 && ("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant)))
			opdrContainer.kijkNa();

		states[activiteitNr][opdrachtNr] = opdrContainer.getState();
		scores[activiteitNr][opdrachtNr] = opdrContainer.getScore();
		this.scores[activiteitNr][opdrachtNr] = opdrContainer.getScore();
		if (objectives != null)
			scoresObjectives[activiteitNr][opdrachtNr] = opdrContainer.getScoreObjectives();
		if (misconceptions != null)
		{	possibleMisconceptions[activiteitNr][opdrachtNr] = opdrContainer.getPossibleMisconceptions();
			measuredMisconceptions[activiteitNr][opdrachtNr] = opdrContainer.getMeasuredMisconceptions();
		}

		// FIXME verzoek sylvia: geen update als ..... HIERO
		if (!zelftoetsNagekeken && !locked) // oftewel geen correctie meer
											// mogelijk
			times[activiteitNr][opdrachtNr] = opdrContainer.getSessionTime();
		isCorrect[activiteitNr][opdrachtNr] = opdrContainer.isCorrect();

		for (int i = 0; i < aantalActiviteiten; i++)
		{
			//aantalOpdrachten[i] = this.aantalOpdrachten[i];
			aantalNakijken[i] = this.aantalNakijken[i];
			orScores[i] = new int[aantalOpdrachten[i]];
			orTimes[i] = new String[aantalOpdrachten[i]];
			orGoedFout[i] = new boolean[aantalOpdrachten[i]];
			opdrContStates[i] = new Hashtable[aantalOpdrachten[i]];
			for (int j = 0; j < aantalOpdrachten[i]; j++)
			{
				opdrContStates[i][j] = states[i][j];
				orGoedFout[i][j] = or[i].geefGoedFout(j + 1);
				orScores[i][j] = or[i].geefScore(j + 1);
				if (mode == 3) 
				{
					orScores[i][j] = scores[i][j];
					orGoedFout[i][j] = isCorrect[i][j];
				}
				orTimes[i][j] = times[i][j]; // mode??

			}
		}

		Hashtable h = new Hashtable();
		//h.put("aantalActiviteiten", new Integer(aantalActiviteiten));
		h.put("activiteitNr", new Integer(activiteitNr));
		h.put("opdrachtNr", new Integer(opdrachtNr));
		//h.put("maxAantalOpdrachten", new Integer(maxAantalOpdrachten));
		//h.put("aantalOpdrachten", aantalOpdrachten);
		h.put("opdrContStates", opdrContStates);
		h.put("orGoedFout", orGoedFout);
		h.put("orScores", orScores);
		h.put("scores", scores);
		h.put("orTimes", orTimes);
		h.put("aantalNakijken", aantalNakijken);
		h.put("strafpunten", strafpunten);
		h.put("locked", new Boolean(locked));
		h.put("zelftoetsNagekeken", new Boolean(zelftoetsNagekeken));
		h.put("aantalSessies", new Integer(aantalSessies));
		if (objectives != null)
		{
			h.put("scoresObjectives", scoresObjectives);
			h.put("scoresMaxObjectives", scoresMaxObjectives);
			h.put("scoresPerObjective", scoresPerObjective);
		}
		if (misconceptions != null)
		{
			h.put("possibleMisconceptions", possibleMisconceptions);
			h.put("measuredMisconceptions", measuredMisconceptions);
		}
		if (bezocht != null)
			h.put("bezocht", bezocht);
		Hashtable shareState = ShareAction.getSharedState();
		if (shareState != null)
			h.put(ShareAction.SHARE_MAP, shareState);
		return h;
	}

	/**
	 * Hiermee wordt gevraagd of er supenddata zijn van alle opdrachten van
	 * deze activiteit, behalve die met het meegegeven opdrachtnummer (huidige
	 * opdracht).
	 */
	public boolean suspendDataCompleted(int actNr, int opdrNr)
	{
		boolean completed = true;
		if(condNav && condNavVoorwaarden)
		{	if(bepaalVolgendeOpdracht(actNr, opdrNr) > -1 && opdrNr < aantalOpdrachten[actNr] - 1)
			{
			completed = false;
			}
		}
		else
			for (int j = 0; j < aantalOpdrachten[actNr]; j++)
			{
				if (states[actNr] != null && opdrNr != j)
				{
					completed = states[actNr][j] != null;
					if (!completed)
						break;
				}
			}
		return completed;
	}

	/**
	 * Berekent de totale score van het applet, en geeft deze terug, geschaald
	 * naar 100%.
	 */
	public double getScore()
	{
		int totaalScore = 0;
		int totaalMax = 0;
		int hoogstBezocht = 0;
		int hoogstActiviteit = 0;
		for (int i = 0; i < aantalActiviteiten; i++)
		{
			if (mode == 3)
			{
				for (int j = 0; j < aantalOpdrachten[i]; j++)
				{
					totaalScore += scores[i][j];
				}
			}
			else
			{
				totaalScore += or[i].geefScore() - ((mode == 2) ? (nakijkStraf * (Math.max(0, aantalNakijken[i] - 1))) : 0);
			}
			if(condNav && condNavVoorwaarden)
			{	int opdrNr = 0;
				while(bepaalVolgendeOpdracht(i, opdrNr) > -1 && opdrNr < aantalOpdrachten[i] - 1)
				{	totaalMax += scoresMax[i][opdrNr];
					opdrNr = bepaalVolgendeOpdracht(i, opdrNr);
				}
				totaalMax += scoresMax[i][opdrNr];
			}
			else
				for (int j = 0; j < aantalOpdrachten[i]; j++)
				{
					totaalMax += scoresMax[i][j];
				}
			
			System.out.println("TotaalMax " + totaalMax);
			System.out.println("TotaalScore " + totaalScore);
			if (totaalMax == 0)
				return 0;
		}
		double doubleScore;
		doubleScore = Math.round(100.0 * totaalScore / totaalMax);
		if (Double.isInfinite(doubleScore) || Double.isNaN(doubleScore))
			doubleScore = 0;
		return doubleScore;
	}

	/**
	 * Verzamelt de maximale scores per leerdoel, de gerealiseerde scores per
	 * leerdoel en de leerdoelen zelf en geeft deze terug tbv het diagram.
	 */
	public Hashtable getScoresObjectivesForDiagram()
	{
		Hashtable h = new Hashtable();
		if (objectives == null)
			return h;
		
		int[][] totaalScoreObjectives = null;
		int[][] totaalMaxObjectives = null;
		double[][] scoresPercObjectives = null;

		totaalScoreObjectives = new int[objectives.length][];
		totaalMaxObjectives = new int[objectives.length][];
		scoresPercObjectives = new double[objectives.length][];

		for (int i = 0; i < objectives.length; i++)
		{
			totaalScoreObjectives[i] = new int[objectives[i].length];
			totaalMaxObjectives[i] = new int[objectives[i].length];
			scoresPercObjectives[i] = new double[objectives[i].length];
		}

		for (int i = 0; i < aantalActiviteiten; i++)
		{ //String scoreString = scores[i].getText();
			//int score = Integer.parseInt(scoreString.substring(7));
			//totaalScore += score;
			for (int j = 0; j < aantalOpdrachten[i]; j++)
			{	if(scoresObjectives[i][j] != null)
					for (int k = 0; k < objectives.length && k < scoresObjectives[i][j].length; k++)
					{	if (scoresObjectives[i][j][k] != null)
							for (int l = 0; l < objectives[k].length && l < scoresObjectives[i][j][k].length; l++)
								totaalScoreObjectives[k][l] += scoresObjectives[i][j][k][l];
					}	
			}

			for (int j = 0; j < aantalOpdrachten[i]; j++)
			{
				for (int k = 0; k < objectives.length && k < scoresMaxObjectives[i][j].length; k++)
				{
					for (int l = 0; l < objectives[k].length && l < scoresMaxObjectives[i][j][k].length; l++)
					{
						if (scoresMaxObjectives[i][j][k] != null)
							totaalMaxObjectives[k][l] += scoresMaxObjectives[i][j][k][l];
					}
				}
			}
		}

		h.put("objectives", objectives);
		h.put("totaalScoreObjectives", totaalScoreObjectives);
		h.put("totaalMaxObjectives", totaalMaxObjectives);
		h.put("categorieString", categorieString);

		return h;
	}
	
	/**
	 * Verzamelt de maximale scores per leerdoel, de gerealiseerde scores per
	 * leerdoel en de leerdoelen zelf en geeft deze terug tbv het diagram.
	 */
	public Hashtable getMisconceptionsForDiagram()
	{
		Hashtable h = new Hashtable();
		if (misconceptions == null)
			return h;
		
		int[][] totaalMeasuredMisconceptions = null;
		int[][] totaalPossibleMisconceptions = null;
		//double[][] scoresPercObjectives = null;

		totaalMeasuredMisconceptions = new int[misconceptions.length][];
		totaalPossibleMisconceptions = new int[misconceptions.length][];
		//scoresPercObjectives = new double[objectives.length][];

		for (int i = 0; i < misconceptions.length; i++)
		{
			totaalMeasuredMisconceptions[i] = new int[misconceptions[i].length];
			totaalPossibleMisconceptions[i] = new int[misconceptions[i].length];
			//scoresPercObjectives[i] = new double[objectives[i].length];
		}

		for (int i = 0; i < aantalActiviteiten; i++)
		{ //String scoreString = scores[i].getText();
			//int score = Integer.parseInt(scoreString.substring(7));
			//totaalScore += score;
			for (int j = 0; j < aantalOpdrachten[i]; j++)
			{	if(measuredMisconceptions[i][j] != null)
					for (int k = 0; k < misconceptions.length && k < measuredMisconceptions[i][j].length; k++)
					{	if (measuredMisconceptions[i][j][k] != null)
							for (int l = 0; l < misconceptions[k].length && l < measuredMisconceptions[i][j][k].length; l++)
								totaalMeasuredMisconceptions[k][l] += measuredMisconceptions[i][j][k][l];
					}	
			}

			for (int j = 0; j < aantalOpdrachten[i]; j++)
			{	if(possibleMisconceptions[i][j] != null)
					for (int k = 0; k < misconceptions.length && k < possibleMisconceptions[i][j].length; k++)
					{	if (possibleMisconceptions[i][j][k] != null)
							for (int l = 0; l < misconceptions[k].length && l < possibleMisconceptions[i][j][k].length; l++)
								totaalPossibleMisconceptions[k][l] += possibleMisconceptions[i][j][k][l];
					}	
			}
		}

		h.put("objectives", misconceptions);
		h.put("totaalScoreObjectives", totaalMeasuredMisconceptions);
		h.put("totaalMaxObjectives", totaalPossibleMisconceptions);
		h.put("categorieString", mccCategorieString);

		return h;
	}

	/**
	 * Geeft per leerdoel de score in procenten
	 */
	public double[][] getScoresObjectives()
	{
		int[][] totaalScoreObjectives = null;
		int[][] totaalMaxObjectives = null;
		double[][] scoresPercObjectives = null;

		if (objectives == null)
			return new double[1][1];
		totaalScoreObjectives = new int[objectives.length][];
		totaalMaxObjectives = new int[objectives.length][];
		scoresPercObjectives = new double[objectives.length][];
		for (int i = 0; i < objectives.length; i++)
		{
			totaalScoreObjectives[i] = new int[objectives[i].length];
			totaalMaxObjectives[i] = new int[objectives[i].length];
			scoresPercObjectives[i] = new double[objectives[i].length];
		}

		for (int i = 0; i < aantalActiviteiten; i++)
		{
			/*
			for (int j = 0; j < aantalOpdrachten[i]; j++)
			{
				for (int k = 0; k < objectives.length && k < scoresObjectives[i][j].length; k++)
				{
					for (int l = 0; l < objectives[k].length && l < scoresObjectives[i][j][k].length; l++)
					{
						if (scoresObjectives[i][j][k] != null)
							totaalScoreObjectives[k][l] += scoresObjectives[i][j][k][l];
					}
				}
			}
			*/
			
			for (int j = 0; j < aantalOpdrachten[i]; j++)
			{	if(scoresObjectives[i][j] != null)
					for (int k = 0; k < objectives.length && k < scoresObjectives[i][j].length; k++)
					{	if (scoresObjectives[i][j][k] != null)
							for (int l = 0; l < objectives[k].length && l < scoresObjectives[i][j][k].length; l++)
								totaalScoreObjectives[k][l] += scoresObjectives[i][j][k][l];
					}	
			}

			for (int j = 0; j < aantalOpdrachten[i]; j++)
			{
				for (int k = 0; k < objectives.length && k < scoresMaxObjectives[i][j].length ; k++)
				{
					for (int l = 0; l < objectives[k].length && l < scoresMaxObjectives[i][j][k].length; l++)
					{
						if (scoresMaxObjectives[i][j][k] != null)
							totaalMaxObjectives[k][l] += scoresMaxObjectives[i][j][k][l];
					}
				}
			}

			/*
			for (int j = 0; j < aantalOpdrachten[i]; j++) {
				for (int k = 0; k < objectives.length; k++) {
					if(scoresObjectives[i][j]!=null) totaalScoreObjectives[k] += scoresObjectives[i][j][k];
				}
			}

			for (int j = 0; j < aantalOpdrachten[i]; j++) {
				for (int k = 0; k < objectives.length; k++) {
					if(scoresMaxObjectives[i][j]!=null)totaalMaxObjectives[k] += scoresMaxObjectives[i][j][k];
				}
			}
			*/
		}

		for (int k = 0; k < objectives.length; k++)
			for (int l = 0; l < objectives[k].length; l++)
			{
				if (totaalMaxObjectives[k][l] == 0)
					scoresPercObjectives[k][l] = 0;
				else
					scoresPercObjectives[k][l] = Math.round(100.0 * totaalScoreObjectives[k][l] / totaalMaxObjectives[k][l]);
			}
		return scoresPercObjectives;
	}

	/**
	 * Geeft de deelscores van de opdrachten, in tekst
	 */
	public String[] getScores()
	{
		String[] log = new String[aantalOpdrachten[0]];
		for (int i = 0; i < aantalOpdrachten[0]; i++)
		{
			log[i] = "" + or[0].geefScore(i + 1);
		}
		return log;
	}

	/**
	 * Instellingen-optie voor de gekoppelde opdrachten (abc checkbox
	 * linksonder) Deprecated. Combineert de (nieuwe) opties: deelopdrachten
	 * abc, globale parameters en voorwaardelijke navigatie
	 */
	public void zetGekoppeldeOpdrachten(boolean b)
	{
		gekoppeldeOpdrachten = b;
		voorwaardelijkeOpdrachten = gekoppeldeOpdrachten && mode != 2 && mode != 3;
		if (pagina)
			opdrachtLabel.setText(WiskOpdr.rb.getString("paginaLabel"));
		else if (b || abcDeelOpdr)
			opdrachtLabel.setText(WiskOpdr.rb.getString("onderdeelLabel"));
		else
			opdrachtLabel.setText(WiskOpdr.rb.getString("opdrachtLabel"));
		for (int i = 0; i < aantalActiviteiten; i++)
		{
			or[i].setEnabled(!(voorwaardelijkeOpdrachten || (condNav && condNavPerc)));
			//or[i].setEnabled(!(voorwaardelijkeOpdrachten || condNav));
			or[i].setEnabled(true, 1);
			or[i].zetLetters(b || abcDeelOpdr);
		}
	}

	/**
	 * Verzegelt de toets.
	 */
	public void zetToetsLocked(boolean b)
	{
		toetsLocked = b;
		if (lessonMode.equals("normal"))
			lockToetsLabel.setVisible(b);
//		if (lessonMode.equals("review"))
//			lockToetsCB.setSelected(b);
	}

	/**
	 * Maakt aantal sessies zichtbaar. Belangrijk in afgeschermde toetsmodus
	 * (bij meer dan 1 sessie is er fraude)
	 */
	public void toonAantalSessies()
	{
		if (lessonMode.equals("review"))
			aantalSessiesLabel.setVisible(true);
	}

	/**
	 * Zet de modus (oefenen, zelftoets, enz.)
	 */
	public void zetMode(int mode)
	{
		this.mode = mode;
		opdrContainer.zetMode(mode);

		if (mode == ZELFTOETS)
		{
			nakijkKnop.setVisible(true);
			scoresObjectivesKnop.setVisible(objectivesAanwezig);
			viewMisconceptionsKnop.setVisible(possibleMisconceptions!=null);
			if (aantalOpdrachten[activiteitNr] == 1)
			{	nakijkKnop.setEnabled(true);
				scoresObjectivesKnop.setEnabled(true);
				viewMisconceptionsKnop.setEnabled(true);
			}
			else
			{	nakijkKnop.setEnabled(lessonMode.equals("review"));
				scoresObjectivesKnop.setEnabled(lessonMode.equals("review"));
				viewMisconceptionsKnop.setEnabled(lessonMode.equals("review"));
			}
			klaarKnop.setVisible(false);
			itemOpnieuwKnop.setVisible(false);

			Point locationNakijkKnop = nakijkKnop.getLocation();
			Point locationOpnieuwKnop = opnieuwKnop.getLocation();
			nakijkKnop.setLocation(locationOpnieuwKnop);
			opnieuwKnop.setLocation(locationNakijkKnop);
		}
		if (mode == EINDTOETS)
		{
			scoresObjectivesKnop.setVisible(false);
			viewMisconceptionsKnop.setVisible(false);
			String s = "UG_STUDENT";
			if (api != null)
				s = api.LMSGetValue("USER_GROUP");
			if (s != null && s.equals("UG_TEACHER") || lessonMode.equals("review"))
			{
				nakijkKnop.setVisible(true);
				scoresObjectivesKnop.setVisible(objectivesAanwezig);
				viewMisconceptionsKnop.setVisible(possibleMisconceptions!=null);
			}
			klaarKnop.setVisible(false);
//			if ("review".equals(lessonMode) && lockToetsCB != null)
//				lockToetsCB.setVisible(true);
		}
	}

	public int getMode()
	{
		return mode;
	}
	
	public int geefAantalActiviteiten()
	{
		return aantalActiviteiten;
	}

	public int geefAantalOpdrachten(int activiteit)
	{
		return aantalOpdrachten[activiteit];
	}

	public int geefOpdrachtNr()
	{
		return opdrachtNr;
	}

	public int geefActiviteitNr()
	{
		return activiteitNr;
	}

	public void setTeacher(boolean b)
	{
		if (b && mode == EINDTOETS)
		{	nakijkKnop.setVisible(true);
			scoresObjectivesKnop.setVisible(objectivesAanwezig);
			viewMisconceptionsKnop.setVisible(possibleMisconceptions!=null);
		}
	}

	public void start()
	{
		TekstVak.ID=0;
		TekstVak.TELLER=0;
		
		if (timer)
		{
			timerPanel.zetTijdMax(timeLimit);
			timerPanel.zetInstelbaar(false);
			timerPanel.start();
		}
		opdrContainer.start();
		aantalSessies++;
		repaint();
	}

	public void stop()
	{
		opdrContainer.stop();
		repaint();
	}

	public void destroy()
	{
		opdrContainer.destroy();
	}

	/**
	 * Wordt aangeroepen bij overgang naar andere opdracht. Gegevens (launchData
	 * + leerlingresultaten) worden weggeschreven en gezet.
	 */
	public synchronized void kiesOpdracht(int actNr, int opdrNr)
	{
		TekstVak.ID=0;
		TekstVak.TELLER=0;
		try{
		if(bezocht!=null && opdrNr > 0 && opdrNr < bezocht[actNr].length)
			bezocht[actNr][opdrNr] = true;
		}
		catch(Exception e){}
		if (activiteitNr == actNr && !or[activiteitNr].getEnabled(opdrNr + 1))
		//if (activiteitNr == actNr && (opdrachtNr == opdrNr || !or[activiteitNr].getEnabled(opdrNr + 1)))
		{	return;
		
		}
		this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		opdrContainer.closePopups();
		if (mode == 0 || mode == OEFENEN_STRAFPUNTEN)
			opdrContainer.stop();
		else
			opdrContainer.sessionStop();
		states[activiteitNr][opdrachtNr] = opdrContainer.getState();
		scores[activiteitNr][opdrachtNr] = opdrContainer.getScore();
		
		if (objectives != null)
			scoresObjectives[activiteitNr][opdrachtNr] = opdrContainer.getScoreObjectives();
		if (misconceptions != null)
		{	possibleMisconceptions[activiteitNr][opdrachtNr] = opdrContainer.getPossibleMisconceptions();
			measuredMisconceptions[activiteitNr][opdrachtNr] = opdrContainer.getMeasuredMisconceptions();
		}
		// FIXME op verzoek sylvia HIERO
		// if (correctie nog mogelijk)
		boolean locked = afdekPanel != null && afdekPanel.isVisible();
		if (!zelftoetsNagekeken && !locked)
			times[activiteitNr][opdrachtNr] = opdrContainer.getSessionTime();
		isCorrect[activiteitNr][opdrachtNr] = opdrContainer.isCorrect();

		//if (!WiskOpdr.language.getLanguage().equals(new Locale("fa").getLanguage()))
			((WiskOpdr) applet).updateResults();

		if ("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant))
		{
			WiskOpdr.setLMSScore();
			int res = opdrachtNr;
			WiskOpdr.setLMSState();
			WiskOpdr.setCompleted(suspendDataCompleted(actNr, opdrNr));
		}

		boolean actChange = false;
		if (activiteitNr != actNr)
		{
			actChange = true;
			or[activiteitNr].setVisible(false);
			activiteitNr = actNr;
			or[activiteitNr].setVisible(bolletjesZichtbaar || lessonMode.equals("review"));
		}
		opdrachtNr = opdrNr;
		or[activiteitNr].setSelected(opdrachtNr + 1);

		if (states[activiteitNr][opdrachtNr] != null)
		{
			opdrContainer.zetOpdrachtPlusState(opdrachten[activiteitNr][opdrachtNr], !(gekoppeldeOpdrachten || globalParam), states[activiteitNr][opdrachtNr]);
			opdrContainer.setInitialTime(times[activiteitNr][opdrachtNr]);
			if (mode == EINDTOETS && (lessonMode.equals("review") || toetsLocked))
			{
				opdrContainer.kijkNa();
				opdrContainer.zetNagekeken(true);
			}
		}
		else
		{
			opdrContainer.setInitialTime(null); // in de else tak, RESET
												// ACCUMULATOR
			if (actChange)
				opdrContainer.zetOpdracht(opdrachten[activiteitNr][opdrachtNr]);
			else
				opdrContainer.zetOpdracht(opdrachten[activiteitNr][opdrachtNr], !(gekoppeldeOpdrachten || globalParam));
		}

		if (mode == ZELFTOETS && suspendDataCompleted(actNr, opdrNr))
		{
			if (opnieuwMogelijk)
				opnieuwKnop.setVisible(true);
			nakijkKnop.setEnabled(true);
			scoresObjectivesKnop.setEnabled(true);
			viewMisconceptionsKnop.setEnabled(true);
			if (zelftoetsNagekeken)
				nakijkKnop.setEnabled(lessonMode.equals("review") || !zelftoetsGeenCorr);
		}
		else
			System.out.println(suspendDataCompleted(actNr, opdrNr));
		setMWScoreLabel();

		opdrContainer.start();
		opdrContainer.repaint();
		this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

		produceAction("select");
		if (afdekPanel != null && afdekPanel.isVisible())
			requestFocus();
		
		if(eerderGeenCorr)
		{	boolean alBezocht = false;
			for(int i = opdrachtNr + 1; i < aantalOpdrachten[activiteitNr]; i++)
				if(bezocht[activiteitNr][i])
					alBezocht = true;
			zetAfdekPanelLeeg(alBezocht);
		}
		
		if(aantalActiviteiten == 1 && activiteitScoreLabels[0].isVisible() && voortgang)
			activiteitScoreLabels[0].setText(WiskOpdr.rb.getString("voortgang") + bepaalVoortgangPercentage(activiteitNr, opdrachtNr) + "%");
			
		
	}	
	
	public void gaNaarVolgendeOpdracht(int actNr, int opdrNr)
	{
		int volgendeOpdracht = bepaalVolgendeOpdracht(actNr, opdrNr);
		if(volgendeOpdracht == -1)
		{	kiesOpdracht(actNr, opdrNr);
		}			
		else
			kiesOpdracht(actNr, volgendeOpdracht);
		if(volgendeOpdracht - opdrNr > 1)
			for(int i = opdrNr+1; i < volgendeOpdracht; i++)
				bezocht[actNr][i] = false;
	}
	
	
	/* Deze methode berekent wat de volgende pagina moet zijn bij conditionele
	 * navigatie met voorwaarden. 
	 */
	public int bepaalVolgendeOpdracht(int actNr, int opdrNr)
	{
		int scoreSelectie = 0;
		int scoreMaxSelectie = 0;
		int scorePercTotHier;
		int volgendeOpdracht = 0;
		
		try
		{	int[] naarPaginas = navVoorwaarden[0][opdrNr];//kan fout gaan als navVoorwaarden leeg (of niet gevuld voor opdrNr)
			int[] scorePaginas = navVoorwaarden[1][opdrNr];
			int[] grensScores = navVoorwaarden[2][opdrNr];
			
			for(int i = 0; i < scorePaginas.length; i++)//kan fout gaat als scorePaginas leeg
			{	if(bezocht[actNr][scorePaginas[i]-1])
				{	scoreSelectie = scoreSelectie + scores[actNr][scorePaginas[i]-1];//-strafpunten[actNr][scorePaginas[i]-1];
					scoreMaxSelectie += scoresMax[actNr][scorePaginas[i]-1];
				}
			}
			scorePercTotHier = 100 * scoreSelectie / scoreMaxSelectie;//kan fout gaan bij delen door 0
			
			if(scorePercTotHier <= grensScores[0])
				volgendeOpdracht = naarPaginas[0] - 1;
			else
			{	for(int i = 1; i < grensScores.length; i++)//kan fout gaat als grensscores leeg
					if(scorePercTotHier > grensScores[i-1] && scorePercTotHier <= grensScores[i])
						volgendeOpdracht = naarPaginas[i] - 1;
			}
			if(scorePercTotHier > grensScores[grensScores.length - 1])
				volgendeOpdracht = naarPaginas[grensScores.length - 1] - 1;
		}
		catch(Exception e)//als bovenstaande niet lukt, ga je gewoon naar de volgende pagina.
		{	if(opdrNr < aantalOpdrachten[actNr] - 1)
				volgendeOpdracht = opdrNr + 1;	
			else
				volgendeOpdracht = -1;
		}
		if(volgendeOpdracht >= aantalOpdrachten[actNr])
			volgendeOpdracht = -1;
		return volgendeOpdracht;
		
	}
	
	public void bepaalVorigeOpdracht(int actNr, int opdrNr)
	{
		int i = Math.max(opdrNr - 1, 0);
		while(!bezocht[actNr][i] && i > 0)
			i--;
		kiesOpdracht(actNr, i);
		
	}
		
	public void stelNavigatieIn(int actNr, int opdrNr)
	{	//bolletje zelf moet altijd enabled zijn, als het al een keer is bezocht.
		try{	
			if(bezocht[actNr][opdrNr])
				or[actNr].setEnabled(true, opdrNr + 1);
		}
		catch(Exception e){}
			
		//Als op laatste pagina: geen bolletjes in te stellen, einde-knop neerzetten
		if(opdrNr == aantalOpdrachten[actNr] - 1)
		{
			volgendeKnop.setEnabled(false);
			if(!"GR".equals(WiskOpdr.deployVariant) && !"MW".equals(WiskOpdr.deployVariant))
			{	volgendeKnop.setVisible(false);
				//eindeKnop.setVisible(volgendeKnopZichtbaar);
				eindeKnop.setEnabled(true);
			}
		}
		//Als conditionele navigatie met voorwaarden, en van huidige pagina word je naar
		//menu gestuurd: einde-knop neerzetten, alle volgende bolletjes disabled.
		else if(condNav && condNavVoorwaarden && bepaalVolgendeOpdracht(actNr, opdrNr) == -1)
		{
			if("GR".equals(WiskOpdr.deployVariant) || "MW".equals(WiskOpdr.deployVariant))
				volgendeKnop.setEnabled(false);
			else
			{	volgendeKnop.setVisible(false);
				//eindeKnop.setVisible(volgendeKnopZichtbaar);
			}
			for(int i = opdrNr + 1; i < aantalOpdrachten[actNr]; i++)
				or[actNr].setEnabled(false, i + 1);
			if(allesCorrectNodig && !opdrachtenCorrect[actNr][opdrNr] && !or[actNr].geefNoScore(opdrNr + 1))
				eindeKnop.setEnabled(false);
			else
				eindeKnop.setEnabled(true);
		}
		else
		{	eindeKnop.setVisible(false);
			eindeKnop.setEnabled(false);
			volgendeKnop.setVisible(volgendeKnopZichtbaar);
		
			//Als leerling pas door mag als alles op pagina correct: volgende bolletjes en volgende/einde-knop disablen.
			if(allesCorrectNodig && !opdrachtenCorrect[actNr][opdrNr] && !or[actNr].geefNoScore(opdrNr + 1))
			{	for(int i = opdrNr + 1; i < aantalOpdrachten[actNr]; i++)
					or[actNr].setEnabled(false, i + 1);
				volgendeKnop.setEnabled(false);
				eindeKnop.setEnabled(false);
				return;
			}
			
			if(condNav && condNavPerc)
			{	boolean conditie = or[actNr].geefNoScore(opdrNr + 1) || 
						100.0 * (Math.max(0, scores[actNr][opdrNr] /*- strafpunten[actNr][opdrNr]*/)) / scoresMax[actNr][opdrNr] >= condPerc;
				//boolean conditie = or[actNr].geefNoScore(opdrNr + 1) ||
					//	100.0 * or[actNr].geefScore(opdrNr + 1) / scoresMax[actNr][opdrNr] >= condPerc;
				for(int i = opdrNr + 2; i < aantalOpdrachten[actNr]; i++)
					or[actNr].setEnabled(bezocht[actNr][i], i + 1);
				or[actNr].setEnabled(conditie, opdrNr + 2);
				volgendeKnop.setEnabled(conditie);
				eindeKnop.setEnabled(conditie);
			}
			else
				volgendeKnop.setEnabled(true);
			if(condNav && condNavVoorwaarden)
			{	for(int i = opdrNr + 1; i < aantalOpdrachten[actNr]; i++)
					or[actNr].setEnabled(bezocht[actNr][i], i + 1);
				if(bepaalVolgendeOpdracht(actNr, opdrNr) > -1)
				{	or[actNr].setEnabled(!allesCorrectNodig || or[actNr].geefNoScore(opdrNr+1) || opdrachtenCorrect[actNr][opdrNr], bepaalVolgendeOpdracht(actNr, opdrNr) + 1);
					if(!allesCorrectNodig)
					{	int volgende = bepaalVolgendeOpdracht(actNr, opdrNr);
						while(bepaalVolgendeOpdracht(actNr, volgende) > -1)
						{	if(bepaalVolgendeOpdracht(actNr, volgende) > volgende + 1)
								for(int i = volgende + 1; i < bepaalVolgendeOpdracht(actNr, volgende); i++)
									or[actNr].setEnabled(false, i + 1);
							or[actNr].setEnabled(true, bepaalVolgendeOpdracht(actNr, volgende) + 1);
							volgende = bepaalVolgendeOpdracht(actNr, volgende);
						}
						if(volgende + 1 < aantalOpdrachten[actNr])
							for(int i = volgende + 1; i < aantalOpdrachten[actNr]; i++)
								or[actNr].setEnabled(false, i + 1);
								
					}
				
				}
			}
		
		}
		
	}
	
	public int bepaalVoortgangPercentage(int actNr, int opdrNr)
	{	int volgende = 0;
		int aantalPaginas = 1;
		int huidigePagina = 0;
		while(bepaalVolgendeOpdracht(actNr, volgende) > -1)
		{	if(volgende == opdrNr)
			huidigePagina = aantalPaginas - 1;
			volgende = bepaalVolgendeOpdracht(actNr, volgende);
			aantalPaginas++;
		}
		if(volgende == opdrNr)
			huidigePagina = aantalPaginas - 1;
		int voortgangPerc = 100*huidigePagina/aantalPaginas;
		return voortgangPerc;
		
	}
		
	
	public void actionPerformed(ActionEvent e)
	{
//		if (e.getSource() == lockToetsCB)
//		{
//			WiskOpdr.setReviewData("toetsLocked", new Boolean(lockToetsCB.isSelected()));
//		}
		if (e.getSource() == timerPanel)
		{
			if (e.getActionCommand().equals("telaat"))
				zetAfdekPanel(true, 0);
			else
				zetAfdekPanel(false, 0);

			return;
		}
		if (e.getSource() == scoresObjectivesKnop)
		{
			zetScoresObjectivesPanel();
			scoresObjectivesDialog.setVisible(true);
		}
		if (e.getSource() == viewMisconceptionsKnop)
		{
			zetViewMisconceptionsPanel();
			viewMisconceptionsDialog.setVisible(true);
		}
		if (e.getSource() == nakijkKnop)
		{
			if (!lessonMode.equals("review"))
				aantalNakijken[activiteitNr]++;
			int totaal = 0;
			states[activiteitNr][opdrachtNr] = opdrContainer.getState();
			for (int j = 0; j < aantalOpdrachten[activiteitNr]; j++)
			{
				//if (states[activiteitNr][opdrachtNr] != null)
				if (states[activiteitNr][j] != null)
					opdrContainer.zetOpdrachtPlusState(opdrachten[activiteitNr][j], !(gekoppeldeOpdrachten || globalParam), states[activiteitNr][j]);
				else
					opdrContainer.zetOpdracht(opdrachten[activiteitNr][j]);
				opdrContainer.kijkNa();
				opdrContainer.zetNagekeken(true);
				states[activiteitNr][j] = opdrContainer.getState();
				int score = opdrContainer.getScore();
				scores[activiteitNr][j] = score;
				if (objectives != null)
					scoresObjectives[activiteitNr][j] = opdrContainer.getScoreObjectives();
				if (misconceptions != null)
				{	possibleMisconceptions[activiteitNr][opdrachtNr] = opdrContainer.getPossibleMisconceptions();
					measuredMisconceptions[activiteitNr][opdrachtNr] = opdrContainer.getMeasuredMisconceptions();
				}
				boolean correct = opdrContainer.isCorrect();
				isCorrect[activiteitNr][j] = correct;

				or[activiteitNr].zetGemaakt(j + 1, correct);
				or[activiteitNr].zetScore(j + 1, score);
				totaal += score;
			}
			opdrContainer.zetOpdrachtPlusState(opdrachten[activiteitNr][opdrachtNr], !(gekoppeldeOpdrachten || globalParam), states[activiteitNr][opdrachtNr]);
			if (mode == 2 || mode == 3)
			{
				if (mode == 2 && zelftoetsGeenCorr)
				{
					// laatste kans op update sessiontime
					if (!zelftoetsNagekeken)
					{
						opdrContainer.sessionStop();
						times[activiteitNr][opdrachtNr] = opdrContainer.getSessionTime();
					}
					zetAfdekPanelLeeg(true);
				}
				zelftoetsNagekeken = true;
				nakijkKnop.setEnabled(lessonMode.equals("review") || !zelftoetsGeenCorr);
				scoresObjectivesKnop.setEnabled(true);//goed? nodig?
				vorigeKnop.setVisible(vorigeKnopZichtbaar || !bolletjesZichtbaar && zelftoetsNagekeken);

				totaal = Math.max(0, totaal - (Math.max(0, aantalNakijken[activiteitNr] - 1)) * nakijkStraf);
				aantalNakijkLabel.setText(keerNagekeken(aantalNakijken[activiteitNr]));
				if (aantalNakijken[activiteitNr] > 0 && !zelftoetsGeenCorr)
					aantalNakijkLabel.setVisible(true);
			}
			activiteitScoreLabels[activiteitNr].setText(WiskOpdr.rb.getString("score") + totaal);
			if (aantalActiviteiten == 1)
			{	activiteitScoreLabels[activiteitNr].setText(WiskOpdr.rb.getString("totaal") + totaal);
				if(voortgang)
					activiteitScoreLabels[0].setText(WiskOpdr.rb.getString("voortgang") + bepaalVoortgangPercentage(activiteitNr, opdrachtNr) + "%");
			}

			if (mode == 0 || mode == OEFENEN_STRAFPUNTEN)
			{
				WiskOpdr.setLMSScore();
				WiskOpdr.setLMSState();
				setMWScoreLabel();
			}

		}
		if (e.getSource() == klaarKnop)
		{
			states[activiteitNr][opdrachtNr] = opdrContainer.getState();
			opdrContainer.kijkNa(-2);
			opdrContainer.zetNagekeken(true); // ??
		}
		if (e.getSource() == opnieuwKnop)
		{
			opnieuwPanel.setVisible(true);
		}
		else if (e.getSource() == opnieuwPanel && opnieuwPanel.isOk())
		{
			this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
// alles opnieuw
			ShareAction.clearSharedState();
			opnieuwPanel.setVisible(false);
			zetAfdekPanel(false, 0);
			if (timer)
				timerPanel.start();
			aantalNakijken[activiteitNr] = 0;
			activiteitScoreLabels[activiteitNr].setText(WiskOpdr.rb.getString("score") + 0);
			if (aantalActiviteiten == 1)
			{	activiteitScoreLabels[activiteitNr].setText(WiskOpdr.rb.getString("totaal") + 0);
				if(voortgang)
					activiteitScoreLabels[0].setText(WiskOpdr.rb.getString("voortgang") + 0 + "%");
			}
			
			if (mode == ZELFTOETS)
			{
				if (aantalOpdrachten[activiteitNr] == 1)
				{	nakijkKnop.setEnabled(true);
					scoresObjectivesKnop.setEnabled(true);
					viewMisconceptionsKnop.setEnabled(true);
				}
				else
				{	nakijkKnop.setEnabled(lessonMode.equals("review"));
					scoresObjectivesKnop.setEnabled(lessonMode.equals("review"));
					viewMisconceptionsKnop.setEnabled(lessonMode.equals("review"));
				}
			}

			for (int j = 0; j < aantalOpdrachten[activiteitNr]; j++)
			{	// nergens voor nodig lijkt me:
				//opdrContainer.zetOpdracht(opdrachten[activiteitNr][j]);
				states[activiteitNr][j] = null;
				scores[activiteitNr][j] = 0;
				if (objectives != null)
				{
					for (int k = 0; k < objectives.length; k++)
						for (int l = 0; l < objectives[k].length; l++)
						{
							scoresObjectives[activiteitNr][j][k][l] = 0;
						}
				}
				if (misconceptions != null)
				{
					for (int k = 0; k < misconceptions.length; k++)
						for (int l = 0; l < misconceptions[k].length; l++)
						{
							possibleMisconceptions[activiteitNr][j][k][l] = 0;
							measuredMisconceptions[activiteitNr][j][k][l] = 0;
						}
				}
				isCorrect[activiteitNr][j] = false;
				// nergens voor nodig lijkt me:
				//opdrContainer.opnieuw();
				or[activiteitNr].zetGemaakt(j + 1, false);
				or[activiteitNr].zetScore(j + 1, 0);
				strafpunten[activiteitNr][j] = 0;
			}
			or[activiteitNr].setEnabled(!(voorwaardelijkeOpdrachten || (condNav && condNavPerc)));
			//or[activiteitNr].setEnabled(!(voorwaardelijkeOpdrachten || condNav));
			or[activiteitNr].setEnabled(true, 1);

			opdrachtNr = 0;
			opdrContainer.zetOpdracht(opdrachten[activiteitNr][opdrachtNr]);
			or[activiteitNr].setSelected(1);
			if (mode == 2 || mode == 3)
			{
				aantalNakijkLabel.setText(WiskOpdr.rb.getString("nakijkLabel1") + aantalNakijken[activiteitNr] + WiskOpdr.rb.getString("nakijkLabel2"));
				aantalNakijkLabel.setVisible(false);
			}
			this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			zelftoetsNagekeken = false;
			repaint();

			if ("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant))
				WiskOpdr.setCompleted(false);
			WiskOpdr.setLMSScore();
			WiskOpdr.setLMSState();
			setMWScoreLabel();
		}
		else if (e.getSource() == itemOpnieuwKnop)
		{
			this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			opnieuwPanel.setVisible(false);
			zetAfdekPanel(false, 0);
			if (timer)
				timerPanel.start();

			opdrContainer.zetOpdracht(opdrachten[activiteitNr][opdrachtNr]);
			states[activiteitNr][opdrachtNr] = null;
			scores[activiteitNr][opdrachtNr] = 0;
			if (objectives != null)
			{
				for (int k = 0; k < objectives.length; k++)
					for (int l = 0; l < objectives[k].length; l++)
					{
						scoresObjectives[activiteitNr][opdrachtNr][k][l] = 0;
					}
			}
			if (misconceptions != null)
			{
				for (int k = 0; k < misconceptions.length; k++)
					for (int l = 0; l < misconceptions[k].length; l++)
					{
						possibleMisconceptions[activiteitNr][opdrachtNr][k][l] = 0;
						measuredMisconceptions[activiteitNr][opdrachtNr][k][l] = 0;
					}
			}
			isCorrect[activiteitNr][opdrachtNr] = false;
			opdrContainer.opnieuw();
			or[activiteitNr].zetGemaakt(opdrachtNr + 1, false);
			or[activiteitNr].zetScore(opdrachtNr + 1, 0);
			strafpunten[activiteitNr][opdrachtNr] = 0;

			this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			repaint();
			WiskOpdr.setLMSScore();
			WiskOpdr.setLMSState();
			setMWScoreLabel();

		}
		else if (e.getSource() == or[activiteitNr])
		{
			kiesOpdracht(activiteitNr, Integer.parseInt(e.getActionCommand()) - 1);

		}
		else if (e.getSource() == volgendeKnop && opdrachtNr < aantalOpdrachten[activiteitNr] - 1)
		{
			if(condNav && condNavVoorwaarden)
			{	states[activiteitNr][opdrachtNr] = opdrContainer.getState();
				scores[activiteitNr][opdrachtNr] = opdrContainer.getScore();
				if (objectives != null)
					scoresObjectives[activiteitNr][opdrachtNr] = opdrContainer.getScoreObjectives();
				if (misconceptions != null)
				{	possibleMisconceptions[activiteitNr][opdrachtNr] = opdrContainer.getPossibleMisconceptions();
					measuredMisconceptions[activiteitNr][opdrachtNr] = opdrContainer.getMeasuredMisconceptions();
				}
				isCorrect[activiteitNr][opdrachtNr] = opdrContainer.isCorrect();
				stelNavigatieIn(activiteitNr, opdrachtNr);
				gaNaarVolgendeOpdracht(activiteitNr, opdrachtNr);
			}
			else
				kiesOpdracht(activiteitNr, opdrachtNr + 1);
		}
		else if (e.getSource() == vorigeKnop && opdrachtNr > 0)
		{
			if(condNav && condNavVoorwaarden)
			{	states[activiteitNr][opdrachtNr] = opdrContainer.getState();
				scores[activiteitNr][opdrachtNr] = opdrContainer.getScore();
				if (objectives != null)
					scoresObjectives[activiteitNr][opdrachtNr] = opdrContainer.getScoreObjectives();
				if (misconceptions != null)
				{	possibleMisconceptions[activiteitNr][opdrachtNr] = opdrContainer.getPossibleMisconceptions();
					measuredMisconceptions[activiteitNr][opdrachtNr] = opdrContainer.getMeasuredMisconceptions();
				}
				isCorrect[activiteitNr][opdrachtNr] = opdrContainer.isCorrect();
				bepaalVorigeOpdracht(activiteitNr, opdrachtNr);
			}
			else
				kiesOpdracht(activiteitNr, opdrachtNr - 1);
		}
		else if (e.getSource() == actKeuzePanel)
		{
			if (e.getActionCommand().equals("editLabel"))
			{
				for (int i = 0; i < aantalActiviteiten; i++)
				{
					activiteitNamen[i] = actKeuzePanel.getlabel(i);
				}
				WiskOpdr.setLaunchDataChanged();
			}
			else
			{
				kiesOpdracht(actKeuzePanel.geefKeuze() - 1, 0);
			}

		}
		else if (e.getSource() == opdrContainer)
		{
			if (!(e.getActionCommand().equals("checked") || e.getActionCommand().equals("changed")))
				return;
			
			int score = opdrContainer.getScore();
			boolean correct = opdrContainer.isCorrect();
			boolean fout = opdrContainer.isFout();

			scores[activiteitNr][opdrachtNr] = score;
			if (objectives != null)
				scoresObjectives[activiteitNr][opdrachtNr] = opdrContainer.getScoreObjectives();
			if (misconceptions != null)
			{	possibleMisconceptions[activiteitNr][opdrachtNr] = opdrContainer.getPossibleMisconceptions();
				measuredMisconceptions[activiteitNr][opdrachtNr] = opdrContainer.getMeasuredMisconceptions();
			}
			isCorrect[activiteitNr][opdrachtNr] = correct;

			if (e.getActionCommand().equals("checked") && fout && mode == OEFENEN_STRAFPUNTEN)
			{
				//strafpunten niet hier aftrekken maar in het antwoordvak
				//strafpunten[activiteitNr][opdrachtNr] += foutStraf;
			}
			
			// niet meer nodig bij nieuwe opzet strafpunten
			//score = Math.max(0, score - getInt(strafpunten,activiteitNr,opdrachtNr));
			
//!! Dubbel met wat hieronder staat... Alleen niet voor zelftoets, maar daarvoor wil je op dit moment geen score zetten.
//			if (mode != 3)
//			{
//				or[activiteitNr].zetGemaakt(opdrachtNr + 1, correct);
//				opdrachtenCorrect[activiteitNr][opdrachtNr] = correct;
//				or[activiteitNr].zetScore(opdrachtNr + 1, score);
//			}
			if (mode == 0 || mode == OEFENEN_STRAFPUNTEN)
			{
				or[activiteitNr].zetGemaakt(opdrachtNr + 1, correct);
				opdrachtenCorrect[activiteitNr][opdrachtNr] = correct;
				or[activiteitNr].zetScore(opdrachtNr + 1, score);

				if (e.getActionCommand().equals("checked"))
				{
					WiskOpdr.setLMSScore();
					WiskOpdr.setLMSState();
				}
				setMWScoreLabel();
			}

			int totaal = 0;
			boolean allCorrect = true;
			for (int i = 0; i < aantalOpdrachten[activiteitNr]; i++)
			{
				totaal += or[activiteitNr].geefScore(i + 1);
				allCorrect = allCorrect && or[activiteitNr].geefGoedFout(i + 1);
			}
			if (mode == 2)
			{
				totaal = Math.max(0, totaal - (Math.max(0, aantalNakijken[activiteitNr] - 1)) * nakijkStraf);
			}
			if (mode == 0 || mode == OEFENEN_STRAFPUNTEN)
			{
				activiteitScoreLabels[activiteitNr].setText(WiskOpdr.rb.getString("score") + totaal);
				if (aantalActiviteiten == 1)
				{	activiteitScoreLabels[activiteitNr].setText(WiskOpdr.rb.getString("totaal") + totaal);
					if(voortgang)
						activiteitScoreLabels[0].setText(WiskOpdr.rb.getString("voortgang") + bepaalVoortgangPercentage(activiteitNr, opdrachtNr) + "%");
				}
			}
			if (timer && allCorrect)
			{
				zetAfdekPanel(true, 1);
				timerPanel.stop();
			}
			if (correct)
				produceAction("correct");
		}
		
		stelNavigatieIn(activiteitNr, opdrachtNr);
		vorigeKnop.setEnabled(opdrachtNr > 0);
		
		if(e.getSource() == eindeKnop && !"GR".equals(WiskOpdr.deployVariant) && !"MW".equals(WiskOpdr.deployVariant))
		{	eindeLink.activate(0);
			return;
		}
		
	}

	/**
	 * @param aantal
	 * @return localized string: n keer nagekeken.
	 */
	private String keerNagekeken(final int aantal) {
		if(aantal == 1)
			return WiskOpdr.rb.getString("ONS_1timeChecked");
		String format = WiskOpdr.rb.getString("ONS_timesChecked");
		return MessageFormat.format(format, Integer.valueOf(aantal));
		//return "" + aantal + " keer nagekeken";
	}

	/**
	 * Voor toetsenbordje
	 */
	public void zetTabletUser(FormuleVakHouder formuleVakHouder)
	{
		if (tablet == null)
			return;
		tablet.zetFormuleVakHouder(formuleVakHouder);
		tabletUser = formuleVakHouder;
	}

	/**
	 * Voor toetsenbordje
	 */
	public void zetTablet(FormuleVakHouder formuleVakHouder, int x, int y)
	{
		if (tablet == null)
		{
			tablet = new Tablet(formuleVakHouder);
			tablet.setLocation(x, y);
		}
		tablet.zetFormuleVakHouder(formuleVakHouder);
		tabletUser = formuleVakHouder;
	}

	/**
	 * Voor toetsenbordje
	 */
	public void addTablet(FormuleVakHouder formuleVakHouder, int x, int y)
	{
		if (tablet == null)
		{
			tablet = new Tablet(formuleVakHouder);
		}
		if (!tabletAdded)
		{
			this.setLayer(tablet, JLayeredPane.PALETTE_LAYER.intValue());
			this.add(tablet, 0);

			tablet.setLocation(x, y);
			tabletAdded = true;
			repaint();
		}
		tablet.zetFormuleVakHouder(formuleVakHouder);
	}

	/**
	 * Voor toetsenbordje
	 */
	public void removeTablet()
	{
		if (tablet == null)
			return;
		remove(tablet);
		repaint();
		tabletAdded = false;
	}

	public Tablet getTablet()
	{
		return tablet;
	}

	public void mousePressed(MouseEvent e)
	{
	}

	public void mouseClicked(MouseEvent e)
	{
		;
	}

	public void mouseReleased(MouseEvent e)
	{
		;
	}

	public void mouseEntered(MouseEvent e)
	{
		;
	}

	public void mouseExited(MouseEvent e)
	{
		;
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

	/**
	 * Zet instelling: conditionele navigatie: Je mag pas verder bij ... % goed
	 */
	
	public void setCondNavPerc(boolean condNavPerc, int condPerc)
	{
		this.condNavPerc = condNavPerc;
		this.condPerc = condPerc;
		for (int i = 0; i < aantalActiviteiten; i++)
		{
			or[i].setEnabled(!condNavPerc);
			or[i].setEnabled(true, 1);

		}
	}
	
	/**
	 * Zet instelling: conditionele navigatie 2: de activiteit waar je hierna naartoe gaat hangt af van je totaalscore
	 */
	
	
	public void setCondNavVoorwaarden(boolean condNavVoorwaarden, int[][][] navVoorwaarden)
	{
		this.condNavVoorwaarden = condNavVoorwaarden;
		this.navVoorwaarden = navVoorwaarden;
		/*
		for(int i = 0; i < aantalActiviteiten; i++)
		{
			or[i].setEnabled(!condNavVoorwaarden);
			or[i].setEnabled(true, 1);
		}
		*/
		
		
		
		
		/* Eerste versie (met argumenten boolean condNav2, String[] urls, int[] grensScores)
		this.urls = urls;
		this.grensScores = grensScores;
		
		//if(!condNav2 && condVolgendeKnop != null)
			//condVolgendeKnop.setVisible(false);
		if(condNav2)
		{	condLink = new Link("", urls, 400, 400, grensScores);
			//condVolgendeKnop.setVisible(opdrachtNr == aantalOpdrachten[activiteitNr] - 1);
		}
		*/
	}
	
	
	
	/**
	 * Zet instelling: a,b,c als label voor bolletjes ipv 1,2,3
	 */
	public void setAbcDeelOpdr(boolean abcDeelOpdr)
	{
		this.abcDeelOpdr = abcDeelOpdr;
		if (opdrachtLabel != null)
		{
			if (pagina)
				opdrachtLabel.setText(WiskOpdr.rb.getString("paginaLabel"));
			else if (abcDeelOpdr)
				opdrachtLabel.setText(WiskOpdr.rb.getString("onderdeelLabel"));
			else
				opdrachtLabel.setText(WiskOpdr.rb.getString("opdrachtLabel"));
		}
		for (int i = 0; i < aantalActiviteiten; i++)
		{
			or[i].zetLetters(abcDeelOpdr);
		}
	}
	
	/**
	 * Zet instelling: a,b,c als label voor bolletjes ipv 1,2,3
	 */
	public void zetScoresZichtbaar(boolean scoresZichtbaar)
	{
		this.scoresZichtbaar = scoresZichtbaar;
		//for (int i = 0; i < aantalActiviteiten; i++)
		//{
		//	or[i].setScoresVisible(scoresZichtbaar);
		//}
		//activiteitScoreLabels[0].setVisible(scoresZichtbaar);
	}

	/**
	 * Zet instelling: geen correctiemogelijkheid bij zelftoets
	 */
	public void setZelftoetsGeenCorr(boolean zelftoetsGeenCorr)
	{
		this.zelftoetsGeenCorr = zelftoetsGeenCorr;

	}

	public MyOpdrContainer getOpdrContainer()
	{
		return opdrContainer;
	}

	public MyOpdrContainer getOpdrContainer(String launchDataString)
	{
		return opdrContainer;
	}

	public MyOpdrContainer getOpdrEditContainer()
	{
		return opdrContainer;
	}

	public static Vector toVector(Object object)
	{
		if (object == null || object instanceof Vector)
			return (Vector) object;
		if (object instanceof Collection)
		{
			return new Vector((Collection) object);
		}
		return null;
	}

	public void setJSONState(Hashtable onsState) {
		setState(onsState, true);
		
		
	}

}
