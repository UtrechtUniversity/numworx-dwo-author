package fi.wiskopdr.opdrnav;

import java.awt.*;
import java.text.MessageFormat;
import java.util.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;

import javax.swing.*;

import fi.beans.base64code.StringCodeObject;
import fi.beans.iconan.Iconan;
import fi.wiskopdr.*;
import fi.wiskopdr.domainmodel.StudentModel;
import fi.wiskopdr.expressies.Expressie;
import fi.wiskopdr.formuleobjects.*;
import fi.wiskopdr.tekstobjects.TekstImageVak;

public class OpdrNavStructEdit extends JLayeredPane implements MouseListener, ActionListener, ItemListener, TabletOwner, ClipboardOwner {
	
	// Dit wordt vreselijke spaghetti :-(
	static private OpdrNavStructEdit instance;
	public static OpdrNavStructEdit getInstance() {
		return instance;
	}
	
	public void setSizeLabel(int size) {
		String t;
		if(size < 3000)
			t=MessageFormat.format("{0}B", (size));
		else 
			t=MessageFormat.format("{0}kB", (size+512)/1024);
		sizeLabel.setText(t);
		sizeLabel.setForeground(size < 3000000 ? Color.black : Color.red);
	}
	
	public void setSizeLabel(Map o) {
		setSizeLabel(WiskOpdr.getObjectSize(o));
	}
	private MyOpdrEditContainer opdrEditContainer;

	private String[][] opdrachten;

	private int maxAantalActiviteiten = 5;
	private int aantalActiviteiten;
	private int activiteitNr;

	private String[] activiteitNamen;

	private int maxAantalOpdrachten = 51;
	private int[] aantalOpdrachten;
	private int opdrachtNr;

	private OpdrachtNrRij[] or;
	private ActKeuzePanel actKeuzePanel;

	private int orPosX, orPosY;
	private int orSize = 25;
	private int margeLinks = 17;
	private int actKeuzePanelX, actKeuzePanelY;
	
	private int mode;
	private boolean opnieuwMogelijk;
	private boolean gekoppeldeOpdrachten = false;
	private boolean abcDeelOpdr = false;
	private boolean globalParam = false;
	private JCheckBox gekoppeldeOpdrCB;

	private PlusMinKnop aantalNivKnop;
	private PlusMinKnop aantalOpdrKnop;
	private PlusMinKnop opdrPositieKnop;
	private PlusMinKnop nivPositieKnop;

	private JComboBox modeChoice;
	private Font font = new Font("SansSerif", Font.PLAIN, 12);

	private Tablet tablet;
	private boolean tabletAdded;
	private FormuleVakHouder tabletUser;

	private JButton instellingenKnop;
	private DialogFacade instellingenDialog;
	private InstellingenPanel instellingenPanel;
	Hashtable instellingen = null;
	
	private JPopupMenu orPopup;
	private JMenuItem newPageMenuItem, insertCopyMenuItem, deletePageMenuItem, copyPageMenuItem, cutPageMenuItem, pastePageMenuItem;
	private Clipboard systemClipboard;

	public boolean useLocation;
	
	private JLabel sizeLabel;
	private JButton imagesButton;
	private DialogFacade imageDialog;
	private Iconan iconman;
	
	public static HelpBrowser helpBrowser;
	private static String HELP_URL1 = "https://app.dwo.nl/wisweb/?header=less&hash=#s:610861";
	
	/**
	 * Maakt nieuwe Opdrachtnavigatie-editor op basis van de aangeleverde launchData
	 */
	public OpdrNavStructEdit(MyOpdrEditContainer opdrEditContainer, int x, int y, int b, int h, Hashtable launchData) {
		setLayout(null);
		setBounds(x, y, b, h);
		setBackground(WiskOpdr.bgcolor);
		instance = this; // FIXME hoe kom ik hierachter?
		helpBrowser = new HelpBrowser(instance);
		
		this.opdrEditContainer = opdrEditContainer;

		if (launchData.containsKey("mode"))
          mode = Integer.parseInt(((String) launchData.get("mode")));
		if (launchData.containsKey("opnieuwMogelijk") && ((String) launchData.get("opnieuwMogelijk")).equals("true"))
          opnieuwMogelijk = true;
		
		String instellingenString = (String) launchData.get("instellingen");
		Object ob = StringCodeObject.decodeStringToObject(instellingenString);
		instellingen = (Hashtable) ob;
		
		// voor backward comp. na oefenen eindeloos: knop alles opnieuw toevoegen
		if(mode==0 && opnieuwMogelijk)
		  instellingen.put("opnieuw", new Boolean(true));
		
		
		zetInstellingen(instellingen);
		//System.out.println("templatepages"+ TekstVakPanel.templatePages);
		
		opdrEditContainer.refreshFonts();

		String aantalActiviteitenString = (String) launchData.get("aantalActiviteiten");
		if(aantalActiviteitenString == null) aantalActiviteitenString = "1";
		aantalActiviteiten = Integer.parseInt(aantalActiviteitenString);
		aantalOpdrachten = new int[aantalActiviteiten];
		activiteitNamen = new String[aantalActiviteiten];
		opdrachten = new String[aantalActiviteiten][maxAantalOpdrachten];

		for (int i = 0; i < aantalActiviteiten; i++) {
			activiteitNamen[i] = (String) launchData.get("activiteit_" + (i + 1));
			String aantalString = (String) launchData.get("aantalOpdrachten_" + (i + 1));
			if(aantalString == null) aantalString = "1";
			aantalOpdrachten[i] = Integer.parseInt(aantalString);
			for (int j = 0; j < aantalOpdrachten[i]; j++) {
				opdrachten[i][j] = (String) launchData.get("opdracht_" + (i + 1) + "_" + (j + 1));
			}
		}

		mode = 0;
		if (launchData.containsKey("mode"))
			mode = Integer.parseInt(((String) launchData.get("mode")));
		if (launchData.containsKey("opnieuwMogelijk") && ((String) launchData.get("opnieuwMogelijk")).equals("true"))
			opnieuwMogelijk = true;
		if (launchData.containsKey("gekoppeldeOpdrachten") && ((String) launchData.get("gekoppeldeOpdrachten")).equals("true"))
			gekoppeldeOpdrachten = true;

		opdrEditContainer.setEditState(opdrachten[0][0]);
		opdrEditContainer.zetMode(mode);

		add(opdrEditContainer);
		
//		HelpButton helpButton = new HelpButton(HELP_URL1);
//        helpButton.setBounds(360,27,20,20);
//        this.setLayer(helpButton, JLayeredPane.POPUP_LAYER.intValue());
//        this.add(helpButton);

		orPosX = 10;//orSize * 2 + 17 + margeLinks;
		orPosY = h - 80;//(2 * orSize);
		
		actKeuzePanelX = 490;//margeLinks;
		actKeuzePanelY = h-30-aantalActiviteiten * 20;//orPosY - aantalActiviteiten * 20 - 10;

		int aantalOpdrMax = aantalOpdrachten[0];
		for (int i = 1; i < aantalActiviteiten; i++) {
			aantalOpdrMax = Math.max(aantalOpdrMax, aantalOpdrachten[i]);
		}

		activiteitNr = 0;
		opdrachtNr = 0;

		actKeuzePanel = new ActKeuzePanel(activiteitNamen, actKeuzePanelX, actKeuzePanelY, 85, aantalActiviteiten * 20);
		actKeuzePanel.addActionListener(this);
		actKeuzePanel.setBackground(getBackground());
		if(aantalActiviteiten >1)
		  add(actKeuzePanel, 0);

		for (int i = 0; i < aantalActiviteiten; i++) {
			if (aantalOpdrachten[i] > maxAantalOpdrachten)
				maxAantalOpdrachten = aantalOpdrachten[i];
		}

		or = new OpdrachtNrRij[aantalActiviteiten];
		for (int i = 0; i < aantalActiviteiten; i++) {
			or[i] = new OpdrachtNrRij(aantalOpdrachten[i], orPosX, orPosY + 4, orSize);
			or[i].addActionListener(this);
			or[i].setRightClickPossible(true);
			or[i].setBackground(getBackground());
			or[i].setSelected(1);
			or[i].setSize(or[i].getSize().width, 25);
			or[i].setVisible(false);
			this.setLayer(or[i], JLayeredPane.PALETTE_LAYER.intValue());
			add(or[i]);

		}
		or[activiteitNr].setVisible(true);

		aantalNivKnop = new PlusMinKnop(actKeuzePanelX - 2, actKeuzePanelY + aantalActiviteiten * 20, 16, 20, PlusMinKnop.VERTIKAAL);
		aantalNivKnop.addActionListener(this);
        if(aantalActiviteiten>1)
            add(aantalNivKnop);

		aantalOpdrKnop = new PlusMinKnop(orPosX + 25 * aantalOpdrachten[activiteitNr] + 5, orPosY + 2, 20, 16, PlusMinKnop.HORIZONTAAL);
		aantalOpdrKnop.addActionListener(this);
		add(aantalOpdrKnop);

		nivPositieKnop = new PlusMinKnop(actKeuzePanelX - 20, actKeuzePanelY + aantalActiviteiten * 20 - 20, 12, 16, PlusMinKnop.VERTIKAAL);
		nivPositieKnop.addActionListener(this);
		if(aantalActiviteiten>1)
          add(nivPositieKnop);

		opdrPositieKnop = new PlusMinKnop(orPosX, orPosY + 25, 20, 16, PlusMinKnop.HORIZONTAAL);
		opdrPositieKnop.addActionListener(this);
		add(opdrPositieKnop);

		modeChoice = new JComboBox();
		modeChoice.setFont(font);
		//modeChoice.setBackground(new Color(180,195,228));
		//modeChoice.setForeground(new Color(51,74,112));
		//modeChoice.setBackground(new Color(255,255,255));
		modeChoice.setBorder(BorderFactory.createLineBorder(new Color(180,195,228)));
		modeChoice.addItem(WiskOpdr.rb.getString("choiceOefenen"));
		modeChoice.addItem(WiskOpdr.rb.getString("choiceOefenenStraf"));
		modeChoice.addItem(WiskOpdr.rb.getString("choiceZelfToets"));
		modeChoice.addItem(WiskOpdr.rb.getString("choiceEindToets"));
		//modeChoice.addItem(WiskOpdr.rb.getString("choiceOefenenEindloos"));
		modeChoice.setBounds(actKeuzePanelX + 193, actKeuzePanelY + aantalActiviteiten * 20 - 22, 140, 24);
		add(modeChoice, 0);
		if (opnieuwMogelijk && mode == 0)
			; //modeChoice.setSelectedIndex(4); deprecated. Nu via de alles opnieuwknop
		else
			modeChoice.setSelectedIndex(mode);
		modeChoice.addItemListener(this);

		gekoppeldeOpdrCB = new JCheckBox("abc..");
		gekoppeldeOpdrCB.setFont(font);
		gekoppeldeOpdrCB.setOpaque(false);
		gekoppeldeOpdrCB.setBounds(10, orPosY + 30, 50, 15);
		gekoppeldeOpdrCB.addItemListener(this);
		// add(gekoppeldeOpdrCB, 0);
		// deze checkbox wordt niet meer gebruikt. De bijbehorende combinatie van nieuwe instellingen wordt hier gezet
		if (gekoppeldeOpdrachten)
		{	//gekoppeldeOpdrCB.setSelected(true);
			gekoppeldeOpdrachten = false;
			instellingen.put("abcDeelOpdr", new Boolean(true));
			instellingen.put("condNav", new Boolean(true));
			instellingen.put("allesCorrectNodig", new Boolean(true));
			//instellingen.put("setCondNavPerc", new Integer(100));
			instellingen.put("globalParam", new Boolean(true));
		}
		//
		
		imagesButton = new JButton(WiskOpdr.rb.getString("imagesButtonLabel"));
		imagesButton.setFont(font);
		imagesButton.setMargin(new Insets(3, 5, 3, 5));
		//imagesButton.setBorder(BorderFactory.createEmptyBorder());
		imagesButton.addActionListener(this);
		//imagesButton.setBackground(new Color(180,195,228));
		//imagesButton.setForeground(new Color(51,74,112));
        add(imagesButton,0);
		
		sizeLabel = new JLabel("123,456Mb");
// styling
		sizeLabel.setFont(font);//sizeLabel.setBorder(BorderFactory.createLoweredSoftBevelBorder());
		sizeLabel.setForeground(new Color(51,74,112));
		sizeLabel.setBounds(actKeuzePanelX + 125, actKeuzePanelY + aantalActiviteiten * 20 - 42, 100, 22);
		add(sizeLabel,0);
		
		instellingenKnop = new JButton(WiskOpdr.rb.getString("optiesButtonLabel"));
		instellingenKnop.setFont(font);
		//instellingenKnop.setBorder(BorderFactory.createEmptyBorder());
		instellingenKnop.setMargin(new Insets(3, 5, 3, 5));
		instellingenKnop.setBounds(10, actKeuzePanelY + aantalActiviteiten * 20 - 20, 1600, 20);
		instellingenKnop.addActionListener(this);
		//instellingenKnop.setBackground(new Color(180,195,228));
		//instellingenKnop.setForeground(new Color(51,74,112));
		add(instellingenKnop,0);

		instellingenDialog = DialogFacade.newInstance(this, WiskOpdr.rb.getString("optiesButtonLabel"), true);

		instellingenPanel = new InstellingenPanel(instellingenDialog, this);
		instellingenPanel.addActionListener(this);
		instellingenPanel.setBounds(0, 0, 650, 760);
		instellingenPanel.zetInstellingen(instellingen);

		instellingenDialog.getContentPane().add(instellingenPanel);
		instellingenDialog.setSize(instellingenPanel.getSize());
		instellingenDialog.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				instellingenPanel.cancel();

			}
		});

		zetGekoppeldeOpdrachten(gekoppeldeOpdrachten);

		try
		{	systemClipboard = getToolkit().getSystemClipboard();
		}
		catch(Exception e)
		{	systemClipboard = null;
		}
		
		orPopup = new JPopupMenu();
		
		newPageMenuItem = new JMenuItem(WiskOpdr.rb.getString("newPageMenuItem"));
		newPageMenuItem.addActionListener(this);
		orPopup.add(newPageMenuItem);
		
		insertCopyMenuItem = new JMenuItem(WiskOpdr.rb.getString("insertCopyMenuItem"));
		insertCopyMenuItem.addActionListener(this);
		orPopup.add(insertCopyMenuItem);
		
		orPopup.addSeparator();
		
		copyPageMenuItem = new JMenuItem(WiskOpdr.rb.getString("copyPageMenuItem"));
		copyPageMenuItem.addActionListener(this);
		orPopup.add(copyPageMenuItem);
		
		cutPageMenuItem = new JMenuItem(WiskOpdr.rb.getString("cutPageMenuItem"));
		cutPageMenuItem.addActionListener(this);
		orPopup.add(cutPageMenuItem);
		
		pastePageMenuItem = new JMenuItem(WiskOpdr.rb.getString("pastePageMenuItem"));
		pastePageMenuItem.addActionListener(this);
		//pastePageMenuItem.setEnabled(false);
		orPopup.add(pastePageMenuItem);
		
		orPopup.addSeparator();
		
		deletePageMenuItem = new JMenuItem(WiskOpdr.rb.getString("deletePageMenuItem"));
		deletePageMenuItem.addActionListener(this);
		orPopup.add(deletePageMenuItem);
		
// paging:
		String loc = (String) launchData.get("cmi.location");
		this.useLocation = loc != null;
		try {
			int i = Integer.parseInt(loc);
			if(activiteitNr == 0 && i < aantalOpdrachten[0] && i >= 0)
			kiesOpdracht(activiteitNr, i);
		} catch(RuntimeException _) {}
		
		
	}

	/**
	 * Zet de instellingen die als opties voor de (gehele) activiteit zijn ingesteld
	 */
	public void zetInstellingen(Hashtable h) {
		int fontSize = 12;
		boolean maalTeken = false;
		boolean diffOperatoren = false;
		boolean woordFormule = false;
		boolean tweeHoofdletterVar = false;
		boolean hoekGraden = false;
		boolean formTimes = true;
		String fontName = "SansSerif";
		boolean fToets = true;
		boolean abcDeelOpdr = false;
		boolean globalParam = false;
		boolean significantie = false;
		boolean hasObjectives = false;
		String[][] objectives = null;
		String[] categorieString = null;
		StudentModel studentModel = null;
		boolean hasMisconceptions = false;
		String[][] misconceptions = null;
		String[] mccCategorieString = null;
		Hashtable styles = null;
		Hashtable templatePages = null;
		Hashtable templateComponents = null;
		ArrayList<String> templatePagesKeys = null;
		ArrayList<String> templateComponentsKeys = null;
		boolean templateEdit = false;
		boolean hasLayers = false;
		String[] layerNames = null;
		boolean[] layerVisible = null;

		if (h != null && h.containsKey("fontSize"))
			fontSize = ((Integer) h.get("fontSize")).intValue();
		if (h != null && h.containsKey("maalTeken"))
			maalTeken = ((Boolean) h.get("maalTeken")).booleanValue();
		if (h != null && h.containsKey("diffOperatoren"))
			diffOperatoren = ((Boolean) h.get("diffOperatoren")).booleanValue();
		if (h != null && h.containsKey("woordFormule"))
			woordFormule = ((Boolean) h.get("woordFormule")).booleanValue();
		if (h != null && h.containsKey("tweeHoofdletterVar"))
			tweeHoofdletterVar = ((Boolean) h.get("tweeHoofdletterVar")).booleanValue();
		if (h != null && h.containsKey("hoekGraden"))
			hoekGraden = ((Boolean) h.get("hoekGraden")).booleanValue();
		if (h != null && h.containsKey("formTimes"))
			formTimes = ((Boolean) h.get("formTimes")).booleanValue();
		if (h != null && h.containsKey("fontName"))
			fontName = (String) h.get("fontName");
		if (h != null && h.containsKey("fToets"))
			fToets = ((Boolean) h.get("fToets")).booleanValue();
		if (h != null && h.containsKey("globalParam"))
			globalParam = ((Boolean) h.get("globalParam")).booleanValue();
		if (h != null && h.containsKey("abcDeelOpdr"))
			abcDeelOpdr = ((Boolean) h.get("abcDeelOpdr")).booleanValue();
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
		if (h != null && h.containsKey("TekstVakPanelStyles"))
			styles = (Hashtable) h.get("TekstVakPanelStyles");
		if (h != null && h.containsKey("TekstVakPanelTemplatePages"))
			templatePages = (Hashtable) h.get("TekstVakPanelTemplatePages");
		if (h != null && h.containsKey("TekstVakPanelTemplateComponents"))
			templateComponents = (Hashtable) h.get("TekstVakPanelTemplateComponents");
		if (h != null && h.containsKey("TekstVakPanelTemplatePagesKeys"))
			templatePagesKeys = (ArrayList<String>) h.get("TekstVakPanelTemplatePagesKeys");
		if (h != null && h.containsKey("TekstVakPanelTemplateComponentsKeys"))
			templateComponentsKeys = (ArrayList<String>) h.get("TekstVakPanelTemplateComponentsKeys");
		if (h != null && h.containsKey("templateEdit"))
			templateEdit = ((Boolean) h.get("templateEdit")).booleanValue();
		if (h!=null && h.containsKey("hasLayers"))
			hasLayers = ((Boolean) h.get("hasLayers")).booleanValue();
		if (h != null && h.containsKey("layerNames"))
			layerNames = (String[]) h.get("layerNames");
		if (h != null && h.containsKey("layerVisible"))
			layerVisible = (boolean[]) h.get("layerVisible");
		
		WiskOpdr.zetFont(fontName, fontSize);
		WiskOpdr.setFormTimes(formTimes);
		setFont(WiskOpdr.tekstFont);

		WiskOpdr.setObjectives(objectives);
		WiskOpdr.setCategories(categorieString);
		
		WiskOpdr.setMisconceptions(misconceptions);
		WiskOpdr.setMccCategories(mccCategorieString);
		
		if(styles != null)
			TekstVakPanel.styles = styles;
		
		if(templatePages != null)
			TekstVakPanel.templatePages = templatePages;
		
		if(templatePagesKeys != null)
			TekstVakPanel.templatePagesKeys = templatePagesKeys;
		
		if(templateComponents != null)
			TekstVakPanel.templateComponents = templateComponents;
		
		if(templateComponentsKeys != null)
			TekstVakPanel.templateComponentsKeys = templateComponentsKeys;
		
		
		FormuleTeken.zetMaalTeken(maalTeken);
		FormuleTeken.zetDiffOperatoren(diffOperatoren);
		FormuleParser.zetDiffOperatoren(diffOperatoren);
		FormuleParser.zetWoordFormule(woordFormule);
		FormuleParser.zetTweeHoofdletterVariabele(tweeHoofdletterVar);
		FormuleParser.zetSignificantie(significantie);
		AntwoordFormuleVakEditPanel.zetSignificantieAan(significantie);
		AntwoordVergelijkingVakEditPanel.zetSignificantieAan(significantie);
		TekstVakPanel.setTemplateEditor(templateEdit);
		TekstVakPanel.layerNames = layerNames;
		TekstVakPanel.layerVisible = layerVisible;

		this.abcDeelOpdr = abcDeelOpdr;
		this.globalParam = globalParam;

		Expressie.zetHoekGraden(hoekGraden);
		WiskOpdr.setFToets(fToets);
		
		setAbcDeelOpdr(abcDeelOpdr);
	}

	/**
	 * Instellingen-optie voor de opnieuwknop
	 */
	public void zetOpnieuwMogelijk(boolean opnieuwMogelijk) {
		this.opnieuwMogelijk = opnieuwMogelijk;
	}

	/**
	 * zet achtergrondkleur en geeft die door aan de aanwezige componenten
	 */
	public void setBackground(Color c) {
		super.setBackground(c);
		Component[] components = getComponents();
		for (int i = 0; i < components.length; i++) {
			if (components[i] instanceof JPanel)
				components[i].setBackground(c);
		}
	}
	
	/**
	 * geeft font-instelling door aan de OpdrEditContainer
	 */
	public void setFont(Font f) {
		opdrEditContainer.setFont(f);
	}

	/**
	 * geeft alle componenten de juiste afmetingen/plaatsen bij een resize
	 */
	public void setSize(int b, int h) {
		super.setSize(b, h);
		opdrEditContainer.setSize(b, h);
		orPosY = h - 80;//(2 * orSize);
		
		opdrEditContainer.setControlPanelHeight(2 * orSize);
		actKeuzePanelY = h-30- aantalActiviteiten * 20;//orPosY - aantalActiviteiten * 20 - 15;

		int aantalOpdrMax = aantalOpdrachten[0];
		for (int i = 1; i < aantalActiviteiten; i++) {
			aantalOpdrMax = Math.max(aantalOpdrMax, aantalOpdrachten[i]);
		}

		for (int i = 0; i < aantalActiviteiten; i++) {
			if (!"GR".equals(WiskOpdr.deployVariant))
				or[i].setLocation(orPosX, orPosY );
		}

		actKeuzePanel.setLocation(actKeuzePanelX,actKeuzePanelY);
		aantalNivKnop.setBounds(actKeuzePanelX - 2, actKeuzePanelY + aantalActiviteiten * 20, 16, 20);
		aantalOpdrKnop.setBounds(orPosX + 25 * aantalOpdrachten[activiteitNr] + 5, orPosY + 2, 20, 16);
		nivPositieKnop.setBounds(actKeuzePanelX - 20, actKeuzePanelY + aantalActiviteiten * 20 - 20, 12, 16);
		opdrPositieKnop.setBounds(orPosX + 25 * opdrachtNr, orPosY + 25, 20, 16);
		modeChoice.setBounds(190, h-30, 140, 24);
		gekoppeldeOpdrCB.setBounds(10, orPosY + 30, 50, 15);
		gekoppeldeOpdrCB.addItemListener(this);
		instellingenKnop.setBounds(10, h - 30, 160, 24);
		sizeLabel.setBounds(480, h-30, 60, 24);
		imagesButton.setBounds(350,h-30,120,24);
	}
	
	/**
	 * Vraagt de launchData op (die de beginstatus van de activiteit bepalen)
	 */
	public Hashtable getEditState() {
		int aantalActiviteiten = 0;
		int mode = 0;
		boolean opnieuwMogelijk = false;
		boolean gekoppeldeOpdrachten = false;
		Hashtable instellingen = new Hashtable();
		boolean premium = false;
		
		aantalActiviteiten = this.aantalActiviteiten;
		mode = this.mode;
		opnieuwMogelijk = this.opnieuwMogelijk;
		gekoppeldeOpdrachten = this.gekoppeldeOpdrachten;
		instellingen = instellingenPanel.geefInstellingen();
		
		if(TekstVakPanel.styles != null)
			instellingen.put("TekstVakPanelStyles", TekstVakPanel.styles);
		
		if(TekstVakPanel.templatePages != null)
			instellingen.put("TekstVakPanelTemplatePages", TekstVakPanel.templatePages);
		
		if(TekstVakPanel.templatePagesKeys != null)
			instellingen.put("TekstVakPanelTemplatePagesKeys", TekstVakPanel.templatePagesKeys);
		
		if(TekstVakPanel.templateComponents != null)
			instellingen.put("TekstVakPanelTemplateComponents", TekstVakPanel.templateComponents);
		
		if(TekstVakPanel.templateComponentsKeys != null)
			instellingen.put("TekstVakPanelTemplateComponentsKeys", TekstVakPanel.templateComponentsKeys);

		Hashtable<String,String> h = new Hashtable<>();
		h.put("aantalActiviteiten", new String("" + aantalActiviteiten));

		opdrEditContainer.closeLinks();
		
		opdrachten[activiteitNr][opdrachtNr] = opdrEditContainer.getEditState();
		for (int i = 0; i < aantalActiviteiten; i++) {
			h.put("activiteit_" + (i + 1), s(activiteitNamen[i]));
			h.put("aantalOpdrachten_" + (i + 1), new String("" + aantalOpdrachten[i]));
			for (int j = 0; j < aantalOpdrachten[i]; j++) {
				h.put("opdracht_" + (i + 1) + "_" + (j + 1), opdrachten[i][j]);
// dit is vreselijk inefficient, maar "gets the job done!"
				if (!premium) {
				    Map opdr = (Map) StringCodeObject.decodeStringToObject(opdrachten[i][j]);
				    if(opdr != null) premium = Boolean.TRUE.equals(opdr.get("premium"));
				}
			}
		}
		h.put("mode", new String("" + mode));
		h.put("opnieuwMogelijk", new String("" + opnieuwMogelijk));
		h.put("gekoppeldeOpdrachten", new String("" + gekoppeldeOpdrachten));
		h.put("instellingen", StringCodeObject.encodeObjectToString(instellingen));
		if(premium) {
		  h.put("premium", Boolean.TRUE.toString());
		}
		if(this.useLocation)
		{
			h.put("cmi.location", String.valueOf(opdrachtNr));
		}
		
		return h;
	}
	
	private String s(String string) {
		return String.valueOf(string);
	}

	/**
	 * Instellingen-optie voor de gekoppelde opdrachten (abc checkbox linksonder) Deprecated.
	 * Combineert de (nieuwe) opties: deelopdrachten abc, globale parameters en voorwaardelijke navigatie
	 */
	public void zetGekoppeldeOpdrachten(boolean b) {
		gekoppeldeOpdrachten = b;
		for (int i = 0; i < aantalActiviteiten; i++) {
			or[i].zetLetters(b || abcDeelOpdr);
		}

	}

	/**
	 * Zet de modus (oefenen, zelftoets, enz.)
	 */
	public void zetMode(int mode) {
		this.mode = mode;
	}

	
	public int geefAantalActiviteiten() {
		return aantalActiviteiten;
	}

	public int geefAantalOpdrachten(int activiteit) {
		return aantalOpdrachten[activiteit];
	}

	public int geefOpdrachtNr() {
		return opdrachtNr;
	}

	public int geefActiviteitNr() {
		return activiteitNr;
	}

	public void stop() {
		//opdrEditContainer.stop();
		repaint();
	}

	/**
	 * Maakt geheugen-resources vrij
	 */
	public void destroy() {
		if (instellingenPanel != null) {
			instellingenDialog.dispose();
			instellingenDialog = null;
		}
	}

	/**
	 * Wordt aangeroepen bij overgang naar andere opdracht.
	 * Gegevens (launchData) worden weggeschreven naar String[][] opdrachten,  en gezet
	 */
	public synchronized void kiesOpdracht(int actNr, int opdrNr) {
		if (activiteitNr == actNr && (opdrachtNr == opdrNr))
			return;
		this.setCursor(new Cursor(Cursor.WAIT_CURSOR));

		opdrachten[activiteitNr][opdrachtNr] = opdrEditContainer.getEditState();

		if (activiteitNr != actNr) {
			or[activiteitNr].setVisible(false);
			activiteitNr = actNr;
			or[activiteitNr].setVisible(true);
			nivPositieKnop.setLocation(actKeuzePanelX - 20, actKeuzePanelY + activiteitNr * 20);
		}
		opdrachtNr = opdrNr;
		or[activiteitNr].setSelected(opdrachtNr + 1);

		opdrPositieKnop.setLocation(orPosX + 25 * opdrachtNr, orPosY + 25);
		aantalOpdrKnop.setLocation(orPosX + 25 * aantalOpdrachten[activiteitNr] + 5, orPosY + 2);

		if (globalParam)
			opdrEditContainer.viewRandVarEditor(opdrNr == 0);
		
		opdrEditContainer.setEditState(opdrachten[activiteitNr][opdrachtNr]);
		opdrEditContainer.repaint();
		
		this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

		produceAction("select");
	}

	public boolean copyToSystemClipboard(String s) {	
		if(s==null || systemClipboard==null)return false;
		StringSelection content = new StringSelection(s);
		systemClipboard.setContents(content, this);
		return true;
	}
	
	public void lostOwnership (Clipboard parClipboard, Transferable parTransferable) {
		//pastePageMenuItem.setEnabled(false);
	}
		 
	public boolean pasteFromSystemClipboard() {	
		if(systemClipboard==null)return false;
		Transferable clipboardContent = systemClipboard.getContents(this);
		 	
		 if ((clipboardContent != null) && (clipboardContent.isDataFlavorSupported (DataFlavor.stringFlavor))) 
		 {
		 	try 
		 	{ 	String tempString;
		 		tempString = (String) clipboardContent.getTransferData(DataFlavor.stringFlavor);
		 		opdrachten[activiteitNr][opdrachtNr] = tempString;
		 		opdrEditContainer.setEditState(opdrachten[activiteitNr][opdrachtNr]);
		 		opdrEditContainer.repaint();
		 		return true;
		    }
		    catch (Exception e) 
		    {  	 e.printStackTrace ();
		    		return false;
		    }
		 }
		 else return false;
    }
	
	public void deletePage() {
		for (int i = opdrachtNr; i < aantalOpdrachten[activiteitNr]-1; i++) {
			opdrachten[activiteitNr][i] = opdrachten[activiteitNr][i+1];
		}
		aantalOpdrachten[activiteitNr]--;
		remove(or[activiteitNr]);
		or[activiteitNr] = null;
		or[activiteitNr] = new OpdrachtNrRij(aantalOpdrachten[activiteitNr], orPosX, orPosY);
		or[activiteitNr].zetLetters(gekoppeldeOpdrachten || abcDeelOpdr);
		or[activiteitNr].setSize(or[activiteitNr].getSize().width, 25);
		or[activiteitNr].addActionListener(this);
		or[activiteitNr].setRightClickPossible(true);
		or[activiteitNr].setBackground(getBackground());
		or[activiteitNr].setSelected(opdrachtNr + 1);
		add(or[activiteitNr], 0);
		aantalOpdrKnop.setLocation(orPosX + 25 * aantalOpdrachten[activiteitNr] + 5, orPosY + 2);
		opdrEditContainer.setEditState(opdrachten[activiteitNr][opdrachtNr]);
		repaint();
		WiskOpdr.setLaunchDataChanged();
	}
	
	public void insertNewPage() {
		opdrachten[activiteitNr][opdrachtNr] = opdrEditContainer.getEditState();
		aantalOpdrachten[activiteitNr]++;
		for (int i = aantalOpdrachten[activiteitNr]-2 ; i > opdrachtNr-1; i--) {
			opdrachten[activiteitNr][i+1] = opdrachten[activiteitNr][i];
		}
		opdrachten[activiteitNr][opdrachtNr] = WiskOpdr.defaultEditModeState;
		remove(or[activiteitNr]);
		or[activiteitNr] = null;
		or[activiteitNr] = new OpdrachtNrRij(aantalOpdrachten[activiteitNr], orPosX, orPosY);
		or[activiteitNr].zetLetters(gekoppeldeOpdrachten || abcDeelOpdr);
		or[activiteitNr].setSize(or[activiteitNr].getSize().width, 25);
		or[activiteitNr].addActionListener(this);
		or[activiteitNr].setRightClickPossible(true);
		or[activiteitNr].setBackground(getBackground());
		or[activiteitNr].setSelected(opdrachtNr + 1);
		add(or[activiteitNr], 0);
		aantalOpdrKnop.setLocation(orPosX + 25 * aantalOpdrachten[activiteitNr] + 5, orPosY + 2);
		opdrEditContainer.setEditState(opdrachten[activiteitNr][opdrachtNr]);
		repaint();
		WiskOpdr.setLaunchDataChanged();
	}
	/**
	 * 1,2,3 - insertCopyPage(2) - 1,2,2,3
	 */
	public void insertCopyPage() {
		opdrachten[activiteitNr][opdrachtNr] = opdrEditContainer.getEditState();
		String copyState = opdrEditContainer.getEditState();
		aantalOpdrachten[activiteitNr]++;
		for (int i = aantalOpdrachten[activiteitNr]-2 ; i > opdrachtNr-1; i--) {
			opdrachten[activiteitNr][i+1] = opdrachten[activiteitNr][i];
		}
		opdrachten[activiteitNr][opdrachtNr] = copyState;
		remove(or[activiteitNr]);
		or[activiteitNr] = null;
		or[activiteitNr] = new OpdrachtNrRij(aantalOpdrachten[activiteitNr], orPosX, orPosY);
		or[activiteitNr].zetLetters(gekoppeldeOpdrachten || abcDeelOpdr);
		or[activiteitNr].setSize(or[activiteitNr].getSize().width, 25);
		or[activiteitNr].addActionListener(this);
		or[activiteitNr].setRightClickPossible(true);
		or[activiteitNr].setBackground(getBackground());
		or[activiteitNr].setSelected(opdrachtNr + 1);
		add(or[activiteitNr], 0);
		aantalOpdrKnop.setLocation(orPosX + 25 * aantalOpdrachten[activiteitNr] + 5, orPosY + 2);
		opdrEditContainer.setEditState(opdrachten[activiteitNr][opdrachtNr]);
		repaint();
		WiskOpdr.setLaunchDataChanged();
	}
	
	public void actionPerformed(ActionEvent e) {
	    if (e.getSource() == instellingenKnop) {
	    	instellingenDialog.setVisible(true);
		}
		if (e.getSource() == instellingenPanel) {
			setFont(WiskOpdr.tekstFont);
		}

		if (e.getSource() == or[activiteitNr]) {
			String command = e.getActionCommand();
			if(command.charAt(0)=='p'){
				command = command.substring(6);
				int nr = Integer.parseInt(command);
				orPopup.show(this, orPosX-10+25*nr, orPosY+10);
				kiesOpdracht(activiteitNr, nr - 1);
			}
			else {
				int nr = Integer.parseInt(command);
				kiesOpdracht(activiteitNr, nr - 1);
			}
		}
		
		if (e.getSource() == newPageMenuItem) {
			insertNewPage();
		}
		
		if (e.getSource() == insertCopyMenuItem) {
			insertCopyPage();
		}
		
		if (e.getSource() == deletePageMenuItem) {
			deletePage();
		}
		
		if (e.getSource() == copyPageMenuItem) {
			this.copyToSystemClipboard(opdrachten[activiteitNr][opdrachtNr]);
			pastePageMenuItem.setEnabled(true);
		}
		
		if (e.getSource() == cutPageMenuItem) {
			this.copyToSystemClipboard(opdrachten[activiteitNr][opdrachtNr]);
			pastePageMenuItem.setEnabled(true);
			deletePage();
		}
		
		if (e.getSource() == pastePageMenuItem) {
			insertNewPage();
			pasteFromSystemClipboard();
		}
		
		if (e.getSource() == actKeuzePanel) {
			if (e.getActionCommand().equals("editLabel")) {
				for (int i = 0; i < aantalActiviteiten; i++) {
					activiteitNamen[i] = actKeuzePanel.getlabel(i);
				}
				WiskOpdr.setLaunchDataChanged();
			} else {
				kiesOpdracht(actKeuzePanel.geefKeuze() - 1, 0);
			}
		}

		else if (e.getSource() == aantalOpdrKnop) {
			if (e.getActionCommand().equals("min") && aantalOpdrachten[activiteitNr] > 1) {
				WiskOpdr.setLaunchDataChanged();
// delete laatste opdracht.
				opdrachten[activiteitNr][opdrachtNr] = opdrEditContainer.getEditState();
				opdrachten[activiteitNr][aantalOpdrachten[activiteitNr] - 1] = null;

				aantalOpdrachten[activiteitNr]--;
				if (opdrachtNr > aantalOpdrachten[activiteitNr] - 1)
					opdrachtNr--;
				opdrPositieKnop.setLocation(orPosX + 25 * opdrachtNr, orPosY + 25);
				opdrEditContainer.setEditState(opdrachten[activiteitNr][opdrachtNr]);
				remove(or[activiteitNr]);
				or[activiteitNr] = null;
				or[activiteitNr] = new OpdrachtNrRij(aantalOpdrachten[activiteitNr], orPosX, orPosY);
				or[activiteitNr].zetLetters(gekoppeldeOpdrachten || abcDeelOpdr);
				or[activiteitNr].setSize(or[activiteitNr].getSize().width, 25);
				or[activiteitNr].addActionListener(this);
				or[activiteitNr].setRightClickPossible(true);
				or[activiteitNr].setBackground(getBackground());
				or[activiteitNr].setSelected(opdrachtNr + 1);
				add(or[activiteitNr], 0);
				aantalOpdrKnop.setLocation(orPosX + 25 * aantalOpdrachten[activiteitNr] + 5, orPosY + 2);
				repaint();
			}
			if (e.getActionCommand().equals("plus") && aantalOpdrachten[activiteitNr] < maxAantalOpdrachten) {
				WiskOpdr.setLaunchDataChanged();
// dupliceer laatste opdracht, of huidige opdracht???
				aantalOpdrachten[activiteitNr]++;
				opdrachten[activiteitNr][opdrachtNr] = opdrEditContainer.getEditState();
				opdrachten[activiteitNr][aantalOpdrachten[activiteitNr] - 1] = opdrachten[activiteitNr][aantalOpdrachten[activiteitNr] - 2];
				opdrachtNr = aantalOpdrachten[activiteitNr] - 1;
				opdrPositieKnop.setLocation(orPosX + 25 * opdrachtNr, orPosY + 25);

				remove(or[activiteitNr]);
				or[activiteitNr] = null;
				or[activiteitNr] = new OpdrachtNrRij(aantalOpdrachten[activiteitNr], orPosX, orPosY);
				or[activiteitNr].zetLetters(gekoppeldeOpdrachten || abcDeelOpdr);
				or[activiteitNr].setSize(or[activiteitNr].getSize().width, 25);
				or[activiteitNr].addActionListener(this);
				or[activiteitNr].setRightClickPossible(true);
				or[activiteitNr].setBackground(getBackground());
				or[activiteitNr].setSelected(opdrachtNr + 1);
				add(or[activiteitNr], 0);
				aantalOpdrKnop.setLocation(orPosX + 25 * aantalOpdrachten[activiteitNr] + 5, orPosY + 2);
				repaint();
			}
		} else if (e.getSource() == aantalNivKnop) {
			if (e.getActionCommand().equals("plus") && aantalActiviteiten > 1) {
				WiskOpdr.setLaunchDataChanged();
				for (int i = 0; i < aantalOpdrachten[aantalActiviteiten - 1]; i++) {
					opdrachten[aantalActiviteiten - 1][i] = null;
				}
				opdrachten[activiteitNr][opdrachtNr] = opdrEditContainer.getEditState();
				remove(or[aantalActiviteiten - 1]);
				or[aantalActiviteiten - 1] = null;
				activiteitNr = 0;
				opdrachtNr = 0;
				opdrPositieKnop.setLocation(orPosX + 25 * opdrachtNr, orPosY + 25);

				opdrEditContainer.setEditState(opdrachten[activiteitNr][opdrachtNr]);

				or[activiteitNr].setVisible(true);

				aantalActiviteiten--;
				remove(actKeuzePanel);
				//actKeuzePanelY = orPosY - aantalActiviteiten * 20 - 15;
				actKeuzePanelY = actKeuzePanelY+20;
				actKeuzePanel = new ActKeuzePanel(activiteitNamen, actKeuzePanelX, actKeuzePanelY, 85, aantalActiviteiten * 20, true);
				actKeuzePanel.addActionListener(this);
				actKeuzePanel.setBackground(getBackground());
				add(actKeuzePanel, 0);
				nivPositieKnop.setLocation(actKeuzePanelX - 20, actKeuzePanelY + activiteitNr * 20);
				repaint();

			}
			if (e.getActionCommand().equals("min") && aantalActiviteiten < maxAantalActiviteiten) {
				WiskOpdr.setLaunchDataChanged();
				aantalActiviteiten++;

				String[][] opdrachtNieuw = new String[aantalActiviteiten][maxAantalOpdrachten];
				for (int i = 0; i < aantalActiviteiten - 1; i++) {
					for (int j = 0; j < aantalOpdrachten[i]; j++) {
						opdrachtNieuw[i][j] = opdrachten[i][j];
					}
				}
				opdrachtNieuw[aantalActiviteiten - 1][0] = opdrachtNieuw[aantalActiviteiten - 2][0];
				opdrachten = opdrachtNieuw;
				int[] aantalOpdrachtenNieuw = new int[aantalActiviteiten];
				for (int i = 0; i < aantalActiviteiten - 1; i++) {
					aantalOpdrachtenNieuw[i] = aantalOpdrachten[i];
				}
				aantalOpdrachten = aantalOpdrachtenNieuw;

				String[] activiteitNamenNieuw = new String[aantalActiviteiten];
				for (int i = 0; i < aantalActiviteiten - 1; i++) {
					activiteitNamenNieuw[i] = activiteitNamen[i];
				}
				activiteitNamen = activiteitNamenNieuw;

				aantalOpdrachten[aantalActiviteiten - 1] = 1;
				if (gekoppeldeOpdrachten || abcDeelOpdr)
					activiteitNamen[aantalActiviteiten - 1] = WiskOpdr.rb.getString("standaardOpdrachtNaam") + " " + aantalActiviteiten;
				else
					activiteitNamen[aantalActiviteiten - 1] = WiskOpdr.rb.getString("standaardActiviteitNaam") + " " + aantalActiviteiten;

				OpdrachtNrRij[] orNieuw = new OpdrachtNrRij[aantalActiviteiten];
				for (int i = 0; i < aantalActiviteiten - 1; i++) {
					orNieuw[i] = or[i];
					orNieuw[i].setSize(or[i].getSize().width, 25);
				}
				or = orNieuw;
				or[aantalActiviteiten - 1] = new OpdrachtNrRij(aantalOpdrachten[aantalActiviteiten - 1], orPosX, orPosY);
				or[aantalActiviteiten - 1].zetLetters(gekoppeldeOpdrachten || abcDeelOpdr);
				or[aantalActiviteiten - 1].addActionListener(this);
				or[aantalActiviteiten - 1].setRightClickPossible(true);
				or[aantalActiviteiten - 1].setSize(or[aantalActiviteiten - 1].getSize().width, 25);
				or[aantalActiviteiten - 1].setBackground(getBackground());
				or[aantalActiviteiten - 1].setSelected(1);
				or[aantalActiviteiten - 1].setVisible(false);
				add(or[aantalActiviteiten - 1], 0);

				remove(actKeuzePanel);
				//actKeuzePanelY = orPosY - aantalActiviteiten * 20 - 15;
				actKeuzePanelY = actKeuzePanelY-20;
				actKeuzePanel = new ActKeuzePanel(activiteitNamen, actKeuzePanelX, actKeuzePanelY, 85, aantalActiviteiten * 20, true);
				actKeuzePanel.addActionListener(this);
				actKeuzePanel.setBackground(getBackground());
				add(actKeuzePanel, 0);
				nivPositieKnop.setLocation(actKeuzePanelX - 20, actKeuzePanelY + activiteitNr * 20);
				repaint();

			}
		} else if (e.getSource() == opdrPositieKnop) {
			if (e.getActionCommand().equals("min") && opdrachtNr > 0) {
				WiskOpdr.setLaunchDataChanged();
// swap opdrachtNr met opdrachtNr-1
				String s = opdrachten[activiteitNr][opdrachtNr];
				opdrachten[activiteitNr][opdrachtNr] = opdrachten[activiteitNr][opdrachtNr - 1];
				opdrachten[activiteitNr][opdrachtNr - 1] = s;
				opdrachtNr--;
				or[activiteitNr].setSelected(opdrachtNr + 1);
				opdrPositieKnop.setLocation(orPosX + 25 * opdrachtNr, orPosY + 25);
			}
			if (e.getActionCommand().equals("plus") && opdrachtNr < aantalOpdrachten[activiteitNr] - 1) {
				WiskOpdr.setLaunchDataChanged();
// swap opdrachtNr met opdrachtNr+1
				String s = opdrachten[activiteitNr][opdrachtNr];
				opdrachten[activiteitNr][opdrachtNr] = opdrachten[activiteitNr][opdrachtNr + 1];
				opdrachten[activiteitNr][opdrachtNr + 1] = s;
				opdrachtNr++;
				or[activiteitNr].setSelected(opdrachtNr + 1);
				opdrPositieKnop.setLocation(orPosX + 25 * opdrachtNr, orPosY + 25);
			}
		} else if (e.getSource() == nivPositieKnop) {
			if (e.getActionCommand().equals("min") && activiteitNr < aantalActiviteiten - 1) {
				WiskOpdr.setLaunchDataChanged();
				String[] s = new String[aantalOpdrachten[activiteitNr]];
				for (int i = 0; i < aantalOpdrachten[activiteitNr]; i++) {
					s[i] = opdrachten[activiteitNr][i];
				}
				for (int i = 0; i < aantalOpdrachten[activiteitNr + 1]; i++) {
					opdrachten[activiteitNr][i] = opdrachten[activiteitNr + 1][i];
				}
				for (int i = 0; i < aantalOpdrachten[activiteitNr]; i++) {
					opdrachten[activiteitNr + 1][i] = s[i];
				}
				int is = aantalOpdrachten[activiteitNr];
				aantalOpdrachten[activiteitNr] = aantalOpdrachten[activiteitNr + 1];
				aantalOpdrachten[activiteitNr + 1] = is;

				OpdrachtNrRij ors = or[activiteitNr];
				or[activiteitNr] = or[activiteitNr + 1];
				or[activiteitNr + 1] = ors;

				String an = activiteitNamen[activiteitNr];
				activiteitNamen[activiteitNr] = activiteitNamen[activiteitNr + 1];
				activiteitNamen[activiteitNr + 1] = an;
				actKeuzePanel.setNames(activiteitNamen);

				activiteitNr++;
				actKeuzePanel.setItem(activiteitNr);
				nivPositieKnop.setLocation(actKeuzePanelX - 20, actKeuzePanelY + activiteitNr * 20);
			}
			if (e.getActionCommand().equals("plus") && activiteitNr > 0) {
				WiskOpdr.setLaunchDataChanged();
				String[] s = new String[aantalOpdrachten[activiteitNr]];
				for (int i = 0; i < aantalOpdrachten[activiteitNr]; i++) {
					s[i] = opdrachten[activiteitNr][i];
				}
				for (int i = 0; i < aantalOpdrachten[activiteitNr - 1]; i++) {
					opdrachten[activiteitNr][i] = opdrachten[activiteitNr - 1][i];
				}
				for (int i = 0; i < aantalOpdrachten[activiteitNr]; i++) {
					opdrachten[activiteitNr - 1][i] = s[i];
				}
				int is = aantalOpdrachten[activiteitNr];
				aantalOpdrachten[activiteitNr] = aantalOpdrachten[activiteitNr - 1];
				aantalOpdrachten[activiteitNr - 1] = is;

				OpdrachtNrRij ors = or[activiteitNr];
				or[activiteitNr] = or[activiteitNr - 1];
				or[activiteitNr - 1] = ors;

				String an = activiteitNamen[activiteitNr];
				activiteitNamen[activiteitNr] = activiteitNamen[activiteitNr - 1];
				activiteitNamen[activiteitNr - 1] = an;
				actKeuzePanel.setNames(activiteitNamen);

				activiteitNr--;
				actKeuzePanel.setItem(activiteitNr);
				nivPositieKnop.setLocation(actKeuzePanelX - 20, actKeuzePanelY + activiteitNr * 20);
			}
		}
		if(e.getSource()==imagesButton)
		{
		  if(imageDialog == null)
	        {
	            //Frame f = JOptionPane.getFrameForComponent(this);
	            imageDialog = DialogFacade.newInstance(this,"title", true);
	           // imageDialog.setLayout(new BorderLayout());
	             iconman = new Iconan(WiskOpdr.applet, (Component)this, (Hashtable)TekstImageVak.getImageMap());
	            imageDialog.getContentPane().add(iconman);
	            imageDialog.pack();
	            iconman.addActionListener(this);
	        }
	       
	        imageDialog.setVisible(true);
		}

	}

	public void zetTabletUser(FormuleVakHouder formuleVakHouder) {
		if (tablet == null)
			return;
		tablet.zetFormuleVakHouder(formuleVakHouder);
		tabletUser = formuleVakHouder;
	}

	public void zetTablet(FormuleVakHouder formuleVakHouder, int x, int y) {
		if (tablet == null) {
			tablet = new Tablet(formuleVakHouder);
			tablet.setLocation(x, y);
		}
		tablet.zetFormuleVakHouder(formuleVakHouder);
		tabletUser = formuleVakHouder;
	}

	public void addTablet(FormuleVakHouder formuleVakHouder, int x, int y) {
		if (tablet == null) {
			tablet = new Tablet(formuleVakHouder);
		}
		if (!tabletAdded) {
			this.setLayer(tablet, JLayeredPane.PALETTE_LAYER.intValue());
			this.add(tablet, 0);
			tablet.setLocation(x, y);
			tabletAdded = true;
			repaint();
		}
		tablet.zetFormuleVakHouder(formuleVakHouder);
	}

	public void removeTablet() {
		if (tablet == null)
			return;
		remove(tablet);
		repaint();
		tabletAdded = false;
	}

	public Tablet getTablet() {
		return tablet;
	}

	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == modeChoice) {
			if (mode != modeChoice.getSelectedIndex())
				WiskOpdr.setLaunchDataChanged();
			mode = modeChoice.getSelectedIndex();
			if (modeChoice.getSelectedIndex() == 4) {
				opnieuwMogelijk = true;
				mode = 0;
			} else {
				opnieuwMogelijk = false;
			}
		} else if (e.getSource() == gekoppeldeOpdrCB) {
			boolean b = gekoppeldeOpdrCB.isSelected();
			for (int i = 0; i < aantalActiviteiten; i++) {
				if (b)
					activiteitNamen[i] = WiskOpdr.rb.getString("standaardOpdrachtNaam") + " " + (i + 1);
				else
					activiteitNamen[i] = WiskOpdr.rb.getString("standaardActiviteitNaam") + " " + (i + 1);
			}
			actKeuzePanel.setNames(activiteitNamen);
			zetGekoppeldeOpdrachten(b);
			repaint();
		}
	}
	
	public void setAbcDeelOpdr(boolean abcDeelOpdr) {
		this.abcDeelOpdr = abcDeelOpdr;
		for (int i = 0; i < aantalActiviteiten; i++) {
			or[i].zetLetters(abcDeelOpdr);
		}
	}


	public MyOpdrEditContainer getOpdrContainer() {
		return opdrEditContainer;
	}

	public MyOpdrEditContainer getOpdrEditContainer(String launchDataString) {

		return opdrEditContainer;
	}


	public MyOpdrEditContainer getOpdrContainer(String launchDataString) {
		return opdrEditContainer;
	}

	


	public MyOpdrEditContainer getOpdrEditContainer() {
		return opdrEditContainer;
	}


	public void mousePressed(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	// ActionProducer
	private ActionListener actionListener = null;

	public void addActionListener(ActionListener l) {
		actionListener = AWTEventMulticaster.add(actionListener, l);
	}

	public void removeActionListener(ActionListener l) {
		actionListener = AWTEventMulticaster.remove(actionListener, l);
	}

	public void produceAction(String command) {
		if (actionListener != null) {
			actionListener.actionPerformed(new ActionEvent(this, 0, command));
		}
	}
}
