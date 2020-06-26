package fi.wiskopdr.opdrnav;

import java.awt.AWTEventMulticaster;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
//import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import fi.beans.base64code.*;
import fi.wiskopdr.AntwoordEditPanel;
import fi.wiskopdr.DialogFacade;
import fi.wiskopdr.InteractiePanelContainerIF;
import fi.wiskopdr.TekstVakPanel;
import fi.wiskopdr.VariableCollection;
import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.WiskOpdrEditPanel;
import fi.wiskopdr.tekstobjects.*;
import fi.wiskopdr.formuleobjects.*;

public class MyOpdrEditContainer extends JPanel implements ActionListener, ItemListener, TextListener, MouseListener, MouseMotionListener {
	
    protected static final int OEFENEN = 0;
    protected static final int OEFENEN_STRAFPUNTEN = 1;
    protected static final int ZELFTOETS = 2;
    protected static final int EINDTOETS = 3;
    
    public static int defaultMarginX = 15;
    public static int defaultMarginY = 10;
    public static int defaultDocWidth = 1024;
    public static int defaultDocHeight = 450;
    
    protected int scoreMax;
    protected int mode;
    
	private TekstEditor tekstEditor, tekstEditor2;
	private JLabel tekstLabel;
	
	private JLabel randVarLabel;
	private TekstEditor randomVarEditor;
	private DialogFacade randVarPopupFrame;

	private JCheckBox titelCB;
	private TekstEditor titelEditor;
	private boolean hasTitle = true;
	
	private int scheidingX = 380;
	private int scheidingXOud = 380;
	private int eindX = 770;
	private boolean scheidingRaak = false;
	private boolean eindRaak = false;
	private int corrToolbar = 33;
	
	private JCheckBox nieuweVersieCB;
	private boolean hasAntwoordVak = true; // nieuwe editor betekent: hasAntwoordVak = false.
	private boolean hasTekstVakLayout = true; // icm. oude editor extra tekstruimte boven vast antwoordvak (nauwelijks meer gebruikt.
	private boolean plainEditor = false;
	
	private Font font = WiskOpdr.tekstFont; 
	
	//Voor oude editorversie
	private FormuleEditor antwoordvak, startEditor;
	private JLabel antwoordLabel, startLabel;
	private JCheckBox gelijkwaardigCB, herleidingCB, exactCB, stappenCB, vergelijkingCB, eindOplossingCB;
	private JCheckBox bewerkingKnoppenCB, abcKnopCB, subKnopCB;	
	private boolean vergelijking;
	private boolean eindOplossingNodig;
	private boolean abcKnop;
	private boolean subKnop;
	private boolean bewerkingKnoppen;
	private boolean herleiding;
	private boolean exact;
	private boolean stappen = true;
	private int soortHerleiding = 0;
	private int puntenGelijkwaardig = 0;
	private int puntenHerleiding = 0;
	private int puntenExact = 0;
	private int puntenEindOplossing = 0;
	private JTextField gelijkwaardigPV, herleidingPV, exactPV, eindOplossingPV;
	private JComboBox herleidingsKeuze;
	private String[] herleidingItems;
	private JLabel ScoringLabel, puntenLabel, checkTotaalLabel;
	private AntwoordEditPanel antwoordEditPanel; // bij oude editorversie: meer ruimte voor antwoordmodel (fullscreen).
	//Einde Voor oude editorversie

	public static void setDefaultDocSizes(int marginX, int marginY, int docWidth, int docHeight) {
	  defaultMarginX = marginX;
	  defaultMarginY = marginY;
	  defaultDocWidth = docWidth;
	  defaultDocHeight = docHeight;
	}
	
	public MyOpdrEditContainer() {
		this(790, 520);
	}

	public MyOpdrEditContainer(int b, int h) {
		setLayout(null);
		super.setSize(b, h); // voor dwo
		setOpaque(false);

		WiskOpdr.launchDataChanged = false;

		addMouseListener(this);
		addMouseMotionListener(this);

		makeGui();
	}
	
	private void makeGui() {
		titelEditor = new TekstEditor(false, false);
		titelEditor.setHeader(false);
		titelEditor.setBounds(10, 25, scheidingX - 15, 25);
		titelEditor.setFont(new Font(WiskOpdr.tekstFont.getName(), Font.BOLD, 16));
		add(titelEditor);

		tekstLabel = makeLabel(10, 95, scheidingX - 15, 20, WiskOpdr.rb.getString("opdrachtTekstLabel"), false);

		BasisTekstVak basisVak = new BasisTekstVak();
		tekstEditor = new TekstEditor(true, true, basisVak, true);
		tekstEditor.setBounds(10, 115, scheidingX - 15, 265);
		tekstEditor.addActionListener(this);
		tekstEditor.setFont(WiskOpdr.tekstFont);
		tekstEditor.setCrossWidgetOption(true);
		tekstEditor.setTemplateOption(true);
		tekstEditor.setStandardComponentOption(true);
		tekstEditor.setToolbarLeft(true);
		tekstEditor.setMainEditor(defaultMarginX, defaultMarginY, defaultDocWidth, defaultDocHeight);
		add(tekstEditor);
		tekstEditor.setResizable(true);

		tekstEditor2 = new TekstEditor(true, true, new BasisTekstVak(basisVak.getXWidgetManager()));
		tekstEditor2.setBounds(scheidingX + 5, 25, 765 - scheidingX, 260);
		tekstEditor2.addActionListener(this);
		tekstEditor2.setFont(WiskOpdr.tekstFont);
		add(tekstEditor2);
		tekstEditor2.setVisible(false);

		randomVarEditor = new TekstEditor(true, false, true, new TekstVak());
		randomVarEditor.setHeader(true);
		randomVarEditor.setBounds(scheidingX + 5, 25, 765 - scheidingX, 100);
		randomVarEditor.setResizable(true);
		randomVarEditor.addActionListener(this);
		add(randomVarEditor);
		
		randVarLabel = makeLabel(scheidingX + 15, 5, 765 - scheidingX, 20, WiskOpdr.rb.getString("randVarLabel"), true);
		randVarLabel.setForeground(new Color(51,74,112));

		titelCB = makeCheckBox(10, 8, 80, 15, WiskOpdr.rb.getString("opdrachtTitelLabel"), true, true);
		nieuweVersieCB = makeCheckBox(600, 5, 175, 20, WiskOpdr.rb.getString("editorVersieKnopLabel1"), false, true);

		// Voor oude Editorversie
		startLabel = makeLabel(scheidingX + 5, 100, 765 - scheidingX, 20, WiskOpdr.rb.getString("startExpLabel"), true);

		startEditor = new FormuleEditor(false);
		startEditor.setBounds(scheidingX + 5, 120, 765 - scheidingX, 105);
		add(startEditor);

		antwoordLabel = makeLabel(scheidingX + 5, 230, 100, 20, WiskOpdr.rb.getString("antwoordLabel"), true);

		antwoordvak = new FormuleEditor(true);
		antwoordvak.setScrollHorizontal(true);
		antwoordvak.setBounds(scheidingX + 5, 250, 765 - scheidingX, 130);
		antwoordvak.addActionListener(this);
		add(antwoordvak, 0);
		antwoordvak.setResizable(true);

		ScoringLabel = makeLabel(360, 385, 160, 20, WiskOpdr.rb.getString("scoringLabel"), true);
		puntenLabel = makeLabel(520, 385, 100, 20, WiskOpdr.rb.getString("puntenLabel"), true);
		checkTotaalLabel = makeLabel(620, 385, 160, 20, WiskOpdr.rb.getString("checkTotaalLabel"), false);
		checkTotaalLabel.setForeground(Color.red);

		gelijkwaardigCB = makeCheckBox(10, 8, 80, 15, WiskOpdr.rb.getString("gelijkwaardigCBLabel"), true, true);
		gelijkwaardigCB.setEnabled(false);
		herleidingCB = makeCheckBox(360, 435, 120, 20, WiskOpdr.rb.getString("herleidingCBLabel"), false, true);
		exactCB = makeCheckBox(360, 460, 120, 20, WiskOpdr.rb.getString("exactCBLabel"), false, true);
		stappenCB = makeCheckBox(630, 230, 200, 20, WiskOpdr.rb.getString("stappenCBLabel"), true, true);
		bewerkingKnoppenCB = makeCheckBox(500, 130, 130, 20, WiskOpdr.rb.getString("bewerkingKnoppenCBLabel"), false, false);
		abcKnopCB = makeCheckBox(630, 130, 60, 20, WiskOpdr.rb.getString("abcCBLabel"), false, false);
		subKnopCB = makeCheckBox(690, 130, 60, 20, WiskOpdr.rb.getString("subKnopCBLabel"), false, false);
		vergelijkingCB = makeCheckBox(520, 260, 110, 20, WiskOpdr.rb.getString("vergelijkingCBLabel"), false, true);
		eindOplossingCB = makeCheckBox(360, 435, 120, 20, WiskOpdr.rb.getString("eindOplossingCBLabel"), false, false);
		

		gelijkwaardigPV = makeTextField(520, 410, 30, 20, "10", true);
		herleidingPV = makeTextField(520, 435, 30, 20, "0", false);
		exactPV = makeTextField(520, 460, 30, 20, "0", false);
		eindOplossingPV = makeTextField(520, 435, 30, 20, "0", false);

		herleidingsKeuze = new JComboBox();
		herleidingsKeuze.setFont(font);
		herleidingsKeuze.setBounds(570, 435, 170, 20);
		herleidingsKeuze.addItemListener(this);
		herleidingsKeuze.setVisible(false);
		add(herleidingsKeuze);

		herleidingItems = new String[7];
		herleidingItems[0] = WiskOpdr.rb.getString("herleidingKeuze_0");
		herleidingItems[1] = WiskOpdr.rb.getString("herleidingKeuze_1");
		herleidingItems[2] = WiskOpdr.rb.getString("herleidingKeuze_2");
		herleidingItems[3] = WiskOpdr.rb.getString("herleidingKeuze_3");
		herleidingItems[4] = WiskOpdr.rb.getString("herleidingKeuze_4");
		herleidingItems[5] = WiskOpdr.rb.getString("herleidingKeuze_5");
		herleidingItems[6] = WiskOpdr.rb.getString("herleidingKeuze_6");
		for (int i = 0; i < herleidingItems.length; i++) {
			herleidingsKeuze.addItem(herleidingItems[i]);
		}

		antwoordEditPanel = new AntwoordEditPanel();
		antwoordEditPanel.setLayout(null);
		add(antwoordEditPanel, 0);
		antwoordEditPanel.setVisible(false);
		// Einde Voor oude editorversie
	}

	private JCheckBox makeCheckBox(int x, int y, int b, int h, String text, boolean selected, boolean visible) {
		JCheckBox checkbox = new JCheckBox(text);
		checkbox.setBounds(x, y, b, h);
		checkbox.setFont(font);
		checkbox.setOpaque(false);
		checkbox.addActionListener(this);
		checkbox.setSelected(selected);
		checkbox.setVisible(visible);
		add(checkbox, 0);
		return checkbox;
	}

	private JLabel makeLabel(int x, int y, int b, int h, String text, boolean visible) {
		JLabel label = new JLabel(text);
		label.setBounds(x, y, b, h);
		label.setFont(font);
		label.setVisible(visible);
		add(label, 0);
		return label;
	}

	private JTextField makeTextField(int x, int y, int b, int h, String text, boolean visible) {
		JTextField textField = new JTextField(text);
		textField.setBounds(x, y, b, h);
		textField.setFont(font);
		textField.addActionListener(this);
		textField.setVisible(visible);
		add(textField, 0);
		return textField;
	}

	public void refreshFonts() {
		tekstEditor.setFont(WiskOpdr.tekstFont);
		tekstEditor2.setFont(WiskOpdr.tekstFont);
	}

	public void setFont(Font f) {
		if (tekstEditor != null)
			tekstEditor.setFont(f);
		if (tekstEditor2 != null)
			tekstEditor2.setFont(f);
		if(tekstEditor!=null)
		  tekstEditor.setMainEditor(defaultMarginX, defaultMarginY, defaultDocWidth, defaultDocHeight);
	}

//	public void paintComponent(Graphics g) {
//	    int xnul = 25 + (tekstEditor.getWidth() - defaultDocWidth)/2;
//		g.setColor(new Color(51,74,112));
//	    g.drawLine(xnul+800, 0, xnul+800, 10);
//		g.drawString("800 px", xnul+805, 10);
//		g.drawLine(xnul+1024, 0, xnul+1024, 10);
//		g.drawString("1024 px", xnul+1029, 10);
//		g.drawLine(xnul+1280, 0, xnul+1280, 10);
//		g.drawString("1280 px", xnul+1288, 10);
//	}

	public void setControlPanelHeight(int h) {
		if (hasTekstVakLayout) {
			setSizesGui();
		}
	}

	private void setSizesGui() {
		int w = getSize().width;
		int h = getSize().height;
		titelCB.setBounds(hasTitle ? 10 : 65, 8, 80, 15);
		titelEditor.setBounds(10, 25, scheidingX - 15, 25);
		tekstLabel.setBounds(10, hasTitle ? 55 : 5, scheidingX - 15, 20);
		//tekstEditor.setBounds(10, hasTitle ? 75 : 15, scheidingX - 15 + corrToolbar, (hasTitle ? 305 : 355) + (hasTekstVakLayout ? h - 465 : 0));
		int corrMenu = OpdrNavStructEdit.hasMenuBar ? 5 : 0;
		tekstEditor.setBounds(10, hasTitle ? 75 : 5-corrMenu, w - 20, (hasTitle ? 305 : 355) + (hasTekstVakLayout ? h - 465-corrMenu : 0));
		tekstEditor.setMainEditor(defaultMarginX, defaultMarginY, defaultDocWidth, defaultDocHeight);
        
        
		tekstEditor2.setBounds(scheidingX + 5, 25, eindX - 5 - scheidingX, 260 + (hasAntwoordVak ? 0 : h - 450));
		if (hasTekstVakLayout) {
			randVarLabel.setBounds(w - 200, h - 96, 200, 20);
			randomVarEditor.setBounds(w - 210, h - 96, 200, 80);
		} else {
			randVarLabel.setBounds(scheidingX + 15, 5, eindX - 5 - scheidingX, 20);
			randomVarEditor.setBounds(scheidingX + 5, 25, eindX - 5 - scheidingX, 70);
		}
		if (randVarPopupFrame != null && randVarPopupFrame.isVisible()) {
			int xx = 0;
			int yy = 0;
			int bb = randVarPopupFrame.getSize().width - randVarPopupFrame.getInsets().left - randVarPopupFrame.getInsets().right;
			int hh = randVarPopupFrame.getSize().height - randVarPopupFrame.getInsets().top - randVarPopupFrame.getInsets().bottom;
			randomVarEditor.setBounds(xx, yy, bb, hh);
		}
		startLabel.setBounds(scheidingX + 5, startLabel.getLocation().y, eindX - 5 - scheidingX, 20);
		startEditor.setBounds(scheidingX + 5, startEditor.getLocation().y, eindX - 5 - scheidingX, 105);
		antwoordLabel.setBounds(scheidingX + 5, 230, eindX - 5 - scheidingX, 20);
		antwoordvak.setBounds(scheidingX + 5, 250, eindX - 5 - scheidingX, 130);
	}

	public boolean contains(int x, int y) {
		if (this.plainEditor)
			return true;
		if (y < getSize().height - 90 || x > randomVarEditor.getX() && y < getSize().height - 10)
			return true;
		return false;
	}

	public void setTitle(boolean b) {
		hasTitle = b;
		titelEditor.setVisible(b);
		setSizesGui();
	}
	
	public void zetTekst(String s) {
		tekstEditor.zetTekst("");
		tekstEditor.insert(s);
		tekstEditor.layoutTekst();
	}
	

	private void zetAntwoordVakUsed(boolean b) {
		hasAntwoordVak = b;
		if (!b) { 
			showTekstVakLayout();
			antwoordEditPanel.setVisible(false);
			antwoordvak.setVisible(false);
			bewerkingKnoppenCB.setVisible(false);
			abcKnopCB.setVisible(false);
			subKnopCB.setVisible(false);
			tekstEditor.setResizable(false);
		} else {
			hideTekstVakLayout();
			antwoordvak.setVisible(true);
			if (vergelijkingCB.isSelected()) {
				bewerkingKnoppenCB.setVisible(true);
				abcKnopCB.setVisible(true);
				subKnopCB.setVisible(true);
			}
			tekstEditor.setResizable(true);
		}
		startEditor.setVisible(b);
		startLabel.setVisible(b);
		setSizesGui();
	}

	private void maakPopupFrame() {
		randVarPopupFrame = DialogFacade.newInstance(this, WiskOpdr.rb.getString("randVarLabel"));
		randVarPopupFrame.getContentPane().setLayout(null);
		randVarPopupFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				randVarPopupFrame.setVisible(false);
				randVarPopupFrame.dispose();
				randVarPopupFrame = null;
				setSizesGui();
				add(randomVarEditor);
				randomVarEditor.setEnlarged(false);
				randVarLabel.setVisible(true);
				repaint();
			}
		});
		randVarPopupFrame.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				int x = 0;
				int y = 0;
				int b = randVarPopupFrame.getSize().width - randVarPopupFrame.getInsets().left - randVarPopupFrame.getInsets().right;
				int h = randVarPopupFrame.getSize().height - randVarPopupFrame.getInsets().top - randVarPopupFrame.getInsets().bottom;
				randomVarEditor.setBounds(x, y, b, h);
			}
		});
	}
	
	private Vector geefInteractiePanels() {
		Vector v = tekstEditor.geefInteractiePanels();
		Vector v2 = tekstEditor2.geefInteractiePanels();
		for (int i = 0; i < v2.size(); i++) {
			v.addElement(v2.elementAt(i));
		}
		return v;
	}

	public void setEditState(String s) {
		if (s == null || s.equals(""))
			return;
		Object o = StringCodeObject.decodeStringToObject(s);
		Hashtable h = (Hashtable) o;
		if (h == null)
			return;
		tekstEditor.geefTekstVak().getXWidgetManager().clear();
		String titel = "titel";
		String tekst = "tekst";
		String tekst2 = "";
		String randVarString = "";

		int scheidingX = 365;
		int eindX = 770;
		boolean hasTitle = true;
		boolean hasAntwoordVak = true;
		Hashtable[] interactiePanelLaunchData = null;

		if (h.containsKey("titel"))
			titel = (String) h.get("titel");
		if (h.containsKey("tekst"))
			tekst = (String) h.get("tekst");
		if (h.containsKey("tekst2"))
			tekst2 = (String) h.get("tekst2");
		if (h.containsKey("randVarString"))
			randVarString = (String) h.get("randVarString");
		if (h.containsKey("scheidingX"))
			scheidingX = ((Integer) h.get("scheidingX")).intValue();
		if (h.containsKey("eindX"))
			eindX = ((Integer) h.get("eindX")).intValue();
		if (h.containsKey("hasTitle"))
			hasTitle = ((Boolean) h.get("hasTitle")).booleanValue();
		if (h.containsKey("hasAntwoordVak"))
			hasAntwoordVak = ((Boolean) h.get("hasAntwoordVak")).booleanValue();
		if (h.containsKey("interactiePanelLaunchData"))
			interactiePanelLaunchData = (Hashtable[]) h.get("interactiePanelLaunchData");

		this.scheidingX = defaultDocWidth;
		this.scheidingXOud = scheidingX;
		this.eindX = eindX;
		this.hasTitle = hasTitle;
		this.hasAntwoordVak = hasAntwoordVak;

		titelEditor.zetTekst(titel);
		tekstEditor.zetTekst(tekst);
		tekstEditor2.zetTekst(tekst2);
		if(tekst2==null || "".equals(tekst2.trim()))
		  remove(tekstEditor2);
		randomVarEditor.zetTekst(randVarString);
		randomVarEditor.layoutTekst();

		titelCB.setSelected(hasTitle);
		if(!hasTitle)
		  titelCB.setVisible(false);

		tekstEditor.setBounds(10, hasTitle ? 75 : 25, scheidingX - 15 + corrToolbar, (hasTitle ? 305 : 355) + (hasTekstVakLayout ? 35 : 0));
		tekstEditor2.setBounds(scheidingX + 5, 25, eindX - 5 - scheidingX, 260 + (hasAntwoordVak ? 0 : 130));

		// voor de oude editorversie
		String antwoordString = "$f@";
		String startString = "$f@";
		boolean herleiding = false;
		boolean exact = false;
		boolean stappen = true;
		int soortHerleiding = 0;
		int puntenGelijkwaardig = 0;
		int puntenHerleiding = 0;
		int puntenExact = 0;
		boolean vergelijking = false;
		boolean eindOplossingNodig = true;
		int puntenEindOplossing = 10;
		boolean bewerkingKnoppen = false;
		boolean abcKnop = false;
		boolean subKnop = false;

		if (interactiePanelLaunchData == null) // voor de backward compatibiliteit:
		{
			if (h.containsKey("antwoordString"))
				antwoordString = (String) h.get("antwoordString");
			if (h.containsKey("startString"))
				startString = (String) h.get("startString");
			if (h.containsKey("herleiding"))
				herleiding = ((Boolean) h.get("herleiding")).booleanValue();
			if (h.containsKey("exact"))
				exact = ((Boolean) h.get("exact")).booleanValue();
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
			if (h.containsKey("vergelijking"))
				vergelijking = ((Boolean) h.get("vergelijking")).booleanValue();
			if (h.containsKey("eindOplossingNodig"))
				eindOplossingNodig = ((Boolean) h.get("eindOplossingNodig")).booleanValue();
			if (h.containsKey("puntenEindOplossing"))
				puntenEindOplossing = ((Integer) h.get("puntenEindOplossing")).intValue();
			if (h.containsKey("bewerkingKnoppen"))
				bewerkingKnoppen = ((Boolean) h.get("bewerkingKnoppen")).booleanValue();
			if (h.containsKey("abcKnop"))
				abcKnop = ((Boolean) h.get("abcKnop")).booleanValue();
			if (h.containsKey("subKnop"))
				subKnop = ((Boolean) h.get("subKnop")).booleanValue();
			
		} else {
			if(hasAntwoordVak) {
				Hashtable interactiePanelLaunchState = null;
				int soortInteractiePanel = 0;
	
				if (interactiePanelLaunchData[0].containsKey("interactiePanelLaunchState"))
					interactiePanelLaunchState = (Hashtable) interactiePanelLaunchData[0].get("interactiePanelLaunchState");
				if (interactiePanelLaunchData[0].containsKey("soortInteractiePanel"))
					soortInteractiePanel = ((Integer) interactiePanelLaunchData[0].get("soortInteractiePanel")).intValue();
	
				if (interactiePanelLaunchState.containsKey("antwoordString"))
					antwoordString = (String) interactiePanelLaunchState.get("antwoordString");
				if (interactiePanelLaunchState.containsKey("startString"))
					startString = (String) interactiePanelLaunchState.get("startString");
				if (interactiePanelLaunchState.containsKey("herleiding"))
					herleiding = ((Boolean) interactiePanelLaunchState.get("herleiding")).booleanValue();
				if (interactiePanelLaunchState.containsKey("exact"))
					exact = ((Boolean) interactiePanelLaunchState.get("exact")).booleanValue();
				if (interactiePanelLaunchState.containsKey("stappen"))
					stappen = ((Boolean) interactiePanelLaunchState.get("stappen")).booleanValue();
				if (interactiePanelLaunchState.containsKey("soortHerleiding"))
					soortHerleiding = ((Integer) interactiePanelLaunchState.get("soortHerleiding")).intValue();
				if (interactiePanelLaunchState.containsKey("puntenGelijkwaardig"))
					puntenGelijkwaardig = ((Integer) interactiePanelLaunchState.get("puntenGelijkwaardig")).intValue();
				if (interactiePanelLaunchState.containsKey("puntenHerleiding"))
					puntenHerleiding = ((Integer) interactiePanelLaunchState.get("puntenHerleiding")).intValue();
				if (interactiePanelLaunchState.containsKey("puntenExact"))
					puntenExact = ((Integer) interactiePanelLaunchState.get("puntenExact")).intValue();
				if (interactiePanelLaunchState.containsKey("vergelijking"))
					vergelijking = ((Boolean) interactiePanelLaunchState.get("vergelijking")).booleanValue();
				if (interactiePanelLaunchState.containsKey("eindOplossingNodig"))
					eindOplossingNodig = ((Boolean) interactiePanelLaunchState.get("eindOplossingNodig")).booleanValue();
				if (interactiePanelLaunchState.containsKey("puntenEindOplossing"))
					puntenEindOplossing = ((Integer) interactiePanelLaunchState.get("puntenEindOplossing")).intValue();
				if (interactiePanelLaunchState.containsKey("bewerkingKnoppen"))
					bewerkingKnoppen = ((Boolean) interactiePanelLaunchState.get("bewerkingKnoppen")).booleanValue();
				if (interactiePanelLaunchState.containsKey("abcKnop"))
					abcKnop = ((Boolean) interactiePanelLaunchState.get("abcKnop")).booleanValue();
				if (interactiePanelLaunchState.containsKey("subKnop"))
					subKnop = ((Boolean) interactiePanelLaunchState.get("subKnop")).booleanValue();
	
				if (soortInteractiePanel == 1)
					vergelijking = true;
			}
			
			if(tekstEditor.geefTekstVak().crossWidgetViewActief())
				tekstEditor.geefTekstVak().newCrossWidgetView();
			
			Vector v = geefInteractiePanels();
			int aantalInteractiePanels = 5 + v.size();
			for (int i = 5; i < aantalInteractiePanels; i++) {
				InteractiePanelContainerIF ip = (InteractiePanelContainerIF) v.elementAt(i - 5);
				if (interactiePanelLaunchData != null && interactiePanelLaunchData[i] != null) {
					ip.setEditState(interactiePanelLaunchData[i]);
				}
			}
			if(tekstEditor.geefTekstVak().crossWidgetViewActief())
			{	for (int i = 5; i < aantalInteractiePanels; i++) {
					InteractiePanelContainerIF ip = (InteractiePanelContainerIF) v.elementAt(i - 5);
					if (interactiePanelLaunchData != null && interactiePanelLaunchData[i] != null) {
						ip.initConnections(tekstEditor.geefTekstVak().getXWidgetManager());
					}
				}
				tekstEditor.geefTekstVak().updateCrossWidgetView();
			}
			
		}
		this.herleiding = herleiding;
		this.exact = exact;
		this.soortHerleiding = soortHerleiding;
		this.puntenGelijkwaardig = puntenGelijkwaardig;
		this.puntenHerleiding = puntenHerleiding;
		this.puntenExact = puntenExact;
		this.vergelijking = vergelijking;
		this.stappen = stappen;
		this.eindOplossingNodig = eindOplossingNodig;
		this.puntenEindOplossing = puntenEindOplossing;
		this.bewerkingKnoppen = bewerkingKnoppen;
		this.abcKnop = abcKnop;
		this.subKnop = subKnop;

		antwoordvak.geefFormuleVak().vulVak(antwoordString);
		startEditor.geefFormuleVak().vulVak(startString);

		stappenCB.setSelected(stappen);
		vergelijkingCB.setSelected(vergelijking);
		abcKnopCB.setSelected(abcKnop);
		subKnopCB.setSelected(subKnop);
		bewerkingKnoppenCB.setSelected(bewerkingKnoppen);

		if (vergelijking) {
			bewerkingKnoppenCB.setVisible(true);
			abcKnopCB.setVisible(true);
			subKnopCB.setVisible(true);
			herleidingCB.setVisible(false);
			exactCB.setVisible(true);
			herleidingPV.setVisible(false);
			exactPV.setVisible(true);
			herleidingsKeuze.setVisible(false);

			eindOplossingCB.setVisible(true);
			eindOplossingCB.setSelected(eindOplossingNodig);
			eindOplossingPV.setVisible(eindOplossingNodig);
			eindOplossingPV.setText("" + puntenEindOplossing);

			gelijkwaardigPV.setText("" + puntenGelijkwaardig);

			exactCB.setSelected(exact);
			exactPV.setVisible(exact);
			exactPV.setText("" + puntenExact);
		} else {
			bewerkingKnoppenCB.setVisible(false);
			abcKnopCB.setVisible(false);
			subKnopCB.setVisible(false);
			herleidingCB.setVisible(true);
			exactCB.setVisible(true);
			herleidingPV.setVisible(true);
			exactPV.setVisible(true);
			herleidingsKeuze.setVisible(true);

			eindOplossingCB.setVisible(false);
			eindOplossingPV.setVisible(false);

			gelijkwaardigPV.setText("" + puntenGelijkwaardig);

			herleidingCB.setSelected(herleiding);
			herleidingPV.setVisible(herleiding);
			herleidingPV.setText("" + puntenHerleiding);

			herleidingsKeuze.setVisible(herleiding);
			herleidingsKeuze.setSelectedIndex(soortHerleiding);

			exactCB.setSelected(exact);
			exactPV.setVisible(exact);
			exactPV.setText("" + puntenExact);
		}
		// EIND voor oude editorversie

		setTitle(hasTitle); // daarmee ook setSizesGui() aangeroepen
		if (antwoordEditPanel.isVisible())
			hideAntwoordVak();
		if (hasTekstVakLayout)
			hideTekstVakLayout();
		tekstEditor.setResizable(true);
		antwoordvak.setResizable(true);
		zetAntwoordVakUsed(hasAntwoordVak);
		nieuweVersieCB.setSelected(!hasAntwoordVak);
		nieuweVersieCB.setVisible(hasAntwoordVak);
		if (hasAntwoordVak) {
			hideAntwoordVak();
			
		}
		titelEditor.deleteStates();
		tekstEditor.deleteStates();
		tekstEditor2.deleteStates();
		randomVarEditor.deleteStates();
	}

	public void viewRandVarEditor(boolean b) {
		randomVarEditor.setVisible(b);
		randomVarEditor.setEnabled(b);
		randVarLabel.setVisible(b);
	}

	public String getRandVarString() {
		return randomVarEditor.getText();
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
  public String getEditState() {
		String titel = null;
		String tekst = null;
		String tekst2 = null;
		String randVarString = null;
		int scheidingX = 365;
		int eindX = 770;
		boolean hasTitle = true;
		boolean hasAntwoordVak = true;
		Hashtable[] interactiePanelLaunchData = null;
		scoreMax = 0;
		int[][] scoreMaxObjectives = null;

		// Voor de oude editorversie:
		Hashtable interactiePanelLaunchDataOudeVersie = null;

		hasAntwoordVak = this.hasAntwoordVak;
		
		if (hasAntwoordVak) {
			String antwoordString = null;
			String startString = null;
			boolean herleiding = false;
			boolean exact = false;
			boolean stappen = false;
			int soortHerleiding = 0;
			int puntenGelijkwaardig = 0;
			int puntenHerleiding = 0;
			int puntenExact = 0;
			boolean vergelijking = false;
			boolean eindOplossingNodig = true;
			int puntenEindOplossing = 0;
			boolean bewerkingKnoppen = false;
			boolean abcKnop = false;
			boolean subKnop = false;

			antwoordString = antwoordvak.geefFormuleVak().toString();
			startString = startEditor.geefFormuleVak().toString();
			herleiding = this.herleiding;
			exact = this.exact;
			stappen = this.stappen;
			soortHerleiding = this.soortHerleiding;
			try {
				puntenGelijkwaardig = Integer.parseInt(gelijkwaardigPV.getText());
				this.puntenGelijkwaardig = puntenGelijkwaardig;
			} catch (Exception ex) {
			}
			try {
				puntenHerleiding = Integer.parseInt(herleidingPV.getText());
				this.puntenHerleiding = puntenHerleiding;
			} catch (Exception ex) {
			}
			try {
				puntenExact = Integer.parseInt(exactPV.getText());
				this.puntenExact = puntenExact;
			} catch (Exception ex) {
			}
			try {
				puntenEindOplossing = Integer.parseInt(eindOplossingPV.getText());
				this.puntenEindOplossing = puntenEindOplossing;
			} catch (Exception ex) {
			}
			puntenHerleiding = this.puntenHerleiding;
			puntenExact = this.puntenExact;
			vergelijking = this.vergelijking;
			eindOplossingNodig = this.eindOplossingNodig;
			puntenEindOplossing = this.puntenEindOplossing;
			bewerkingKnoppen = this.bewerkingKnoppen;
			abcKnop = this.abcKnop;
			subKnop = this.subKnop;
			
			Hashtable interactiePanelLaunchState = new Hashtable();
			interactiePanelLaunchState.put("antwoordString", antwoordString);
			interactiePanelLaunchState.put("startString", startString);
			interactiePanelLaunchState.put("herleiding", new Boolean(herleiding));
			interactiePanelLaunchState.put("exact", new Boolean(exact));
			interactiePanelLaunchState.put("stappen", new Boolean(stappen));
			interactiePanelLaunchState.put("soortHerleiding", new Integer(soortHerleiding));
			interactiePanelLaunchState.put("puntenGelijkwaardig", new Integer(puntenGelijkwaardig));
			interactiePanelLaunchState.put("puntenHerleiding", new Integer(puntenHerleiding));
			interactiePanelLaunchState.put("puntenExact", new Integer(puntenExact));
			interactiePanelLaunchState.put("vergelijking", new Boolean(vergelijking));
			interactiePanelLaunchState.put("eindOplossingNodig", new Boolean(eindOplossingNodig));
			interactiePanelLaunchState.put("puntenEindOplossing", new Integer(puntenEindOplossing));
			interactiePanelLaunchState.put("bewerkingKnoppen", new Boolean(bewerkingKnoppen));
			interactiePanelLaunchState.put("abcKnop", new Boolean(abcKnop));
			interactiePanelLaunchState.put("subKnop", new Boolean(subKnop));

			int soortInteractiePanel = 0;
			if (vergelijking)
				soortInteractiePanel = 1;

			interactiePanelLaunchDataOudeVersie = new Hashtable();
			interactiePanelLaunchDataOudeVersie.put("interactiePanelLaunchState", interactiePanelLaunchState);
			interactiePanelLaunchDataOudeVersie.put("soortInteractiePanel", new Integer(soortInteractiePanel));

			if (hasAntwoordVak) {
				if (vergelijking)
					scoreMax = scoreMax + puntenGelijkwaardig + puntenEindOplossing + puntenExact;
				else
					scoreMax = scoreMax + puntenGelijkwaardig + puntenHerleiding + puntenExact;
			}
		}
		else
			scoreMax = 0;

		titel = titelEditor.getText();
		tekst = tekstEditor.getText();
		tekst2 = tekstEditor2.getText();
		premium = FormuleVak.detectPremium(tekst)||FormuleVak.detectPremium(titel)||FormuleVak.detectPremium(tekst2);
		
		randVarString = randomVarEditor.getText();
		
		//VariableCollection vc = new VariableCollection();
		//boolean wellSet = vc.setVariables(randVarString);
		//if(!wellSet || !vc.checkBorders())
		//{
		//	JOptionPane.showMessageDialog(this, "fout in definitie randomvariabelen");
		//}
		
		scheidingX = this.scheidingX;
		eindX = this.eindX;
		hasTitle = this.hasTitle;
		
		if(!hasAntwoordVak)
			scoreMax = 0;
		
		Vector v = geefInteractiePanels();
		//System.out.println("Vectir met ip Panels: "+v.toString());
		int aantalInteractiePanel = 5 + v.size();
		interactiePanelLaunchData = new Hashtable[aantalInteractiePanel];
		if (interactiePanelLaunchDataOudeVersie != null)
			interactiePanelLaunchData[0] = interactiePanelLaunchDataOudeVersie;

		if (WiskOpdr.objectives != null)
		{    scoreMaxObjectives = new int[WiskOpdr.objectives.length][];
			for(int i = 0; i < WiskOpdr.objectives.length; i++)
				scoreMaxObjectives[i] = new int[WiskOpdr.objectives[i].length];
		}
        for (int i = 0; i < v.size(); i++) {
            InteractiePanelContainerIF ip = (InteractiePanelContainerIF) v.elementAt(i);
            interactiePanelLaunchData[i + 5] = ip.getEditState();
            Hashtable editState = interactiePanelLaunchData[i + 5];
            scoreMax += ip.getScoreMax();
            //System.out.println("LaunchData ip Panels: "+ interactiePanelLaunchData[i + 5].toString());
            
            Hashtable launchState = (Hashtable)editState.get("interactiePanelLaunchState");
            if ( Boolean.TRUE.equals(launchState.get("premium")))
                premium = true;
            if(launchState.containsKey("logID")) {
            	String logID = (String)launchState.get("logID");
            	if(logID.startsWith("DWOTEMPNAME_") && ip instanceof TekstInteractiePanelVak) {
            		launchState.put("logID", "");
            		launchState.put("logOption", false);
            		TekstVakPanel.setTemplateName(logID.substring(12));
            		launchState.put("logID", logID);
            		launchState.put("logOption", true);
            	}
            	if(logID.startsWith("DWOTEMP_") && ip instanceof TekstInteractiePanelVak) {
            		launchState.put("logID", "");
            		launchState.put("logOption", false);
            		TekstInteractiePanelVak ipp = new TekstInteractiePanelVak(((TekstInteractiePanelVak)ip).getTekstVak(),editState);
            		TekstVakPanel.addTemplatePage(logID,ipp.toCompleteString());
            		launchState.put("logID", logID);
            		launchState.put("logOption", true);
            	}
            	if(logID.startsWith("DWOCOMP_") && ip instanceof TekstInteractiePanelVak) {
            		launchState.put("logID", "");
            		launchState.put("logOption", false);
            		TekstInteractiePanelVak ipp = new TekstInteractiePanelVak(((TekstInteractiePanelVak)ip).getTekstVak(),editState);
            		TekstVakPanel.addTemplateComponent(logID,ipp.toCompleteString());
            		launchState.put("logID", logID);
            		launchState.put("logOption", true);
            	}
            }
            
            int[][] ob = ((InteractiePanelContainerIF) v.elementAt(i)).getScoreMaxObjectives();
            for (int j = 0; scoreMaxObjectives != null && ob != null && j < scoreMaxObjectives.length; j++) 
                for(int k = 0; scoreMaxObjectives[j] != null && k < scoreMaxObjectives[j].length; k++){
                try{
                	scoreMaxObjectives[j][k] += ob[j][k];
                }
                catch(Exception e){}
            }
        }

		Hashtable h = new Hashtable();
		h.put("titel", titel);
		h.put("tekst", tekst);
		h.put("tekst2", tekst2);
		h.put("randVarString", randVarString);
		h.put("scheidingX", new Integer(scheidingX));
		h.put("eindX", new Integer(eindX));
		h.put("hasTitle", new Boolean(hasTitle));
		h.put("hasAntwoordVak", new Boolean(hasAntwoordVak));
		h.put("interactiePanelLaunchData", interactiePanelLaunchData);
		h.put("scoreMax", new Integer(scoreMax));
		if (scoreMaxObjectives != null)
			h.put("scoreMaxObjectives", scoreMaxObjectives);
		if (premium)
		    h.put("premium", Boolean.TRUE);
		//System.out.println("tekst: "+tekst);
		//System.out.println("interactiePanelLaunchData: "+interactiePanelLaunchData[5].toString());

		String s = StringCodeObject.encodeObjectToString(h);
		return s;
	}

	public void closeLinks() {
	}

	public void destroy() {
		if (antwoordvak != null) {
			remove(antwoordvak);
			antwoordvak.destroy();
			antwoordvak = null;
		}

		if (tekstEditor != null) {
			remove(tekstEditor);
			tekstEditor.destroy();
			tekstEditor = null;
		}
	}

	public void textValueChanged(TextEvent e) {
		int puntenGelijkwaardig = 0;
		int puntenHerleiding = 0;
		int puntenExact = 0;
		int puntenEindOplossing = 0;

		if (e.getSource() == gelijkwaardigPV) {
			try {
				puntenGelijkwaardig = Integer.parseInt(gelijkwaardigPV.getText());
				this.puntenGelijkwaardig = puntenGelijkwaardig;
			} catch (Exception ex) {
			}
		}
		if (e.getSource() == herleidingPV) {
			try {
				puntenHerleiding = Integer.parseInt(herleidingPV.getText());
				this.puntenHerleiding = puntenHerleiding;
			} catch (Exception ex) {
			}
		}
		if (e.getSource() == exactPV) {
			try {
				puntenExact = Integer.parseInt(exactPV.getText());
				this.puntenExact = puntenExact;
			} catch (Exception ex) {
			}
		}
		if (e.getSource() == eindOplossingPV) {
			try {
				puntenEindOplossing = Integer.parseInt(eindOplossingPV.getText());
				this.puntenEindOplossing = puntenEindOplossing;
			} catch (Exception ex) {
			}
		}
		boolean b = false;
		if (vergelijking)
			b = this.puntenGelijkwaardig + this.puntenEindOplossing + this.puntenExact == 10;
		else
			b = this.puntenGelijkwaardig + this.puntenHerleiding + this.puntenExact == 10;

		checkTotaalLabel.setVisible(!b);
	}

	private void showAntwoordVak() {
		antwoordEditPanel.setBounds(10, 0, getSize().width, getSize().height - 100);
		antwoordEditPanel.setBackground(getBackground());
		antwoordvak.setBounds(0, 20, getSize().width - 10, getSize().height - 280);
		antwoordLabel.setBounds(0, 0, getSize().width - 10, 20);

		vergelijkingCB.setBounds(520, 0, 110, 20);
		stappenCB.setBounds(630, 0, 200, 20);
		antwoordEditPanel.add(antwoordLabel, 0);
		antwoordEditPanel.add(vergelijkingCB, 0);
		antwoordEditPanel.add(stappenCB, 0);

		JPanel scorePanel = new JPanel();
		scorePanel.setLayout(null);
		scorePanel.setBounds(0, 250, getSize().width, getSize().height - 370);
		scorePanel.setBackground(getBackground());

		ScoringLabel.setBounds(360, 15, 160, 20);
		scorePanel.add(ScoringLabel);
		puntenLabel.setBounds(520, 15, 100, 20);
		scorePanel.add(puntenLabel);
		checkTotaalLabel.setBounds(620, 15, 160, 20);
		scorePanel.add(checkTotaalLabel);
		gelijkwaardigCB.setBounds(360, 40, 120, 20);
		scorePanel.add(gelijkwaardigCB);
		herleidingCB.setBounds(360, 65, 120, 20);
		scorePanel.add(herleidingCB);
		exactCB.setBounds(360, 90, 120, 20);
		scorePanel.add(exactCB, 0);
		eindOplossingCB.setBounds(360, 65, 120, 20);
		scorePanel.add(eindOplossingCB);
		gelijkwaardigPV.setBounds(520, 40, 30, 20);
		scorePanel.add(gelijkwaardigPV);
		herleidingPV.setBounds(520, 65, 30, 20);
		scorePanel.add(herleidingPV);
		exactPV.setBounds(520, 90, 30, 20);
		scorePanel.add(exactPV);
		eindOplossingPV.setBounds(520, 65, 30, 20);
		scorePanel.add(eindOplossingPV);
		herleidingsKeuze.setBounds(570, 65, 170, 20);
		scorePanel.add(herleidingsKeuze);

		antwoordEditPanel.add(antwoordvak);
		antwoordEditPanel.add(scorePanel, 0);
		antwoordEditPanel.setVisible(true);
		antwoordEditPanel.repaint();
	}

	private void hideAntwoordVak() {
		int w = getSize().width;
		int h = getSize().height;

		antwoordvak.setBounds(scheidingX + 5, 250, 765 - scheidingX, 130);
		antwoordLabel.setBounds(scheidingX + 5, 230, 765 - scheidingX, 20);
		vergelijkingCB.setBounds(520, 230, 110, 20);
		stappenCB.setBounds(630, 230, 200, 20);
		add(antwoordvak, 0);
		add(antwoordLabel);
		add(vergelijkingCB, 0);
		add(stappenCB, 0);
		antwoordEditPanel.setVisible(false);

		ScoringLabel.setBounds(380, 385, 160, 20);
		add(ScoringLabel);
		puntenLabel.setBounds(540, 385, 100, 20);
		add(puntenLabel);
		checkTotaalLabel.setBounds(640, 385, 160, 20);
		add(checkTotaalLabel);
		gelijkwaardigCB.setBounds(380, 410, 120, 20);
		add(gelijkwaardigCB);
		herleidingCB.setBounds(380, 435, 120, 20);
		add(herleidingCB);
		exactCB.setBounds(380, 460, 120, 20);
		add(exactCB, 0);
		stappenCB.setBounds(650, 230, 200, 20);
		eindOplossingCB.setBounds(380, 435, 120, 20);
		add(eindOplossingCB);
		gelijkwaardigPV.setBounds(540, 410, 30, 20);
		add(gelijkwaardigPV);
		herleidingPV.setBounds(540, 435, 30, 20);
		add(herleidingPV);
		exactPV.setBounds(540, 460, 30, 20);
		add(exactPV);
		eindOplossingPV.setBounds(540, 435, 30, 20);
		add(eindOplossingPV);
		herleidingsKeuze.setBounds(590, 435, 170, 20);
		add(herleidingsKeuze);
	}

	public void showTekstVakLayout() {
		showAntwoordVak();
		hasTekstVakLayout = true;
		tekstEditor.setBounds(10, hasTitle ? 75 : 25, scheidingX - 15, (hasTitle ? 340 : 390));
		antwoordEditPanel.setVisible(false);
		startEditor.setBounds(scheidingX + 5, 310, 765 - scheidingX, 105);

		bewerkingKnoppenCB.setBounds(500, 290, 130, 20);
		abcKnopCB.setBounds(630, 290, 60, 20);
		subKnopCB.setBounds(690, 290, 60, 20);
		startLabel.setBounds(scheidingX + 5, 290, 765 - scheidingX, 20);
		
		randVarLabel.setBounds(370, 420, 400, 20);
		randomVarEditor.setBounds(370, 440, 400, 70);

		tekstEditor2.setVisible(true);
	}

	public void hideTekstVakLayout() {
		if (hasAntwoordVak)
			hideAntwoordVak();
		hasTekstVakLayout = false;
		tekstEditor.setBounds(10, hasTitle ? 75 : 25, scheidingX - 15, hasTitle ? 305 : 355);
		startEditor.setBounds(scheidingX + 5, 120, 765 - scheidingX, 105);

		bewerkingKnoppenCB.setBounds(500, 100, 130, 20);
		abcKnopCB.setBounds(630, 100, 60, 20);
		subKnopCB.setBounds(690, 100, 60, 20);
		startLabel.setBounds(scheidingX + 5, 100, 765 - scheidingX, 20);

		randVarLabel.setBounds(scheidingX + 5, 5, 765 - scheidingX, 20);
		randomVarEditor.setBounds(scheidingX + 5, 25, 765 - scheidingX, 70);

		tekstEditor2.setVisible(false);
	}

	public void setPlainEditor(WiskOpdrEditPanel woep) {
		plainEditor = true;
		nieuweVersieCB.setVisible(false);
		titelCB.setVisible(false);
		tekstLabel.setVisible(false);
		randomVarEditor.setVisible(false);
		randVarLabel.setVisible(false);
		tekstEditor.setBounds(0, 0, getWidth(), getHeight());
		removeMouseListener(this);
		removeMouseMotionListener(this);
	}

	public void zetMode(int mode)
    {   this.mode = mode;
    }
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == antwoordvak) {
			if (e.getActionCommand().equals("vergroot")) {
				showAntwoordVak();
				nieuweVersieCB.setVisible(false);
			}
			if (e.getActionCommand().equals("verklein")) {
				hideAntwoordVak();
				nieuweVersieCB.setVisible(true);
			}
			revalidate();
			repaint();
		}
		if (e.getSource() == tekstEditor) {
			if (e.getActionCommand().equals("vergroot")) {
				showTekstVakLayout();
				revalidate();
				repaint();
			}
			if (e.getActionCommand().equals("verklein")) {
				hideTekstVakLayout();
				revalidate();
				repaint();
			}
			if (e.getActionCommand().equals("instellingen")) {
				produceAction("instellingen");
			}
		}
		if (e.getSource() == randomVarEditor) {
			if (e.getActionCommand().equals("vergroot")) {
				if (randVarPopupFrame == null)
					maakPopupFrame();
				Dimension screenSize = WiskOpdr.applet.getToolkit().getScreenSize();
				int x = randomVarEditor.getLocationOnScreen().x + Math.min(0, screenSize.width - (getLocationOnScreen().x + 500));
				int y = randomVarEditor.getLocationOnScreen().y + Math.min(0, screenSize.height - (getLocationOnScreen().y + 400));
				randVarPopupFrame.setVisible(true);
				randVarLabel.setVisible(false);
				randVarPopupFrame.getContentPane().add(randomVarEditor);
				randVarPopupFrame.pack();
				randVarPopupFrame.setSize(500, 400);
				randVarPopupFrame.setLocation(x, y);

			}
			if (e.getActionCommand().equals("verklein")) {
				randVarPopupFrame.setVisible(false);
				randVarPopupFrame.dispose();
				randVarPopupFrame = null;
				randVarLabel.setVisible(true);
				setSizesGui();
				add(randomVarEditor);
			}
			revalidate();
			repaint();
		}
		if (e.getSource() == vergelijkingCB) {
			WiskOpdr.setLaunchDataChanged();
			boolean b = vergelijkingCB.isSelected();
			vergelijking = b;
			herleidingCB.setVisible(!b);
			herleidingPV.setVisible(!b);
			herleidingsKeuze.setVisible(!b);

			eindOplossingCB.setVisible(b);
			eindOplossingCB.setSelected(b);
			eindOplossingPV.setVisible(b);
			bewerkingKnoppenCB.setVisible(b);
			abcKnopCB.setVisible(b);
			subKnopCB.setVisible(b);
			if (b) {
				eindOplossingPV.setText("10");
				gelijkwaardigPV.setText("0");
				puntenEindOplossing = 10;
				puntenExact = 0;
				puntenGelijkwaardig = 0;
				exactCB.setSelected(false);
				exactPV.setText("0");

			} else {
				eindOplossingPV.setText("0");
				gelijkwaardigPV.setText("10");
				puntenEindOplossing = 0;
				puntenExact = 0;
				puntenGelijkwaardig = 10;
				puntenHerleiding = 0;
				herleidingCB.setSelected(false);
				herleidingPV.setText("0");
				herleidingsKeuze.setVisible(b);
				exactCB.setSelected(false);
				exactPV.setText("0");
			}

		}

		if (e.getSource() == gelijkwaardigCB) {
			WiskOpdr.setLaunchDataChanged();
			boolean b = gelijkwaardigCB.isSelected();
			gelijkwaardigPV.setVisible(b);
			herleidingCB.setEnabled(b);
			exactCB.setEnabled(b);
			if (!b) {
				herleidingPV.setVisible(b);
				herleidingPV.setText("0");
				puntenHerleiding = 0;
				herleidingCB.setSelected(b);
				herleidingsKeuze.setVisible(b);
				exactPV.setVisible(b);
				exactPV.setText("0");
				puntenExact = 0;
				exactCB.setSelected(b);
				exactCB.setEnabled(b);

			}
			herleidingCB.setEnabled(b);
		}
		if (e.getSource() == herleidingCB) {
			WiskOpdr.setLaunchDataChanged();
			boolean b = herleidingCB.isSelected();
			herleiding = b;
			herleidingPV.setVisible(b);
			herleidingsKeuze.setVisible(b);
			if (!b) {
				herleidingPV.setText("0");
				puntenHerleiding = 0;
			}

		}
		if (e.getSource() == exactCB) {
			WiskOpdr.setLaunchDataChanged();
			boolean b = exactCB.isSelected();
			exact = b;
			exactPV.setVisible(b);
			if (!b) {
				exactPV.setText("0");
				puntenExact = 0;
			}
		}
		if (e.getSource() == eindOplossingCB) {
			WiskOpdr.setLaunchDataChanged();
			boolean b = eindOplossingCB.isSelected();
			eindOplossingNodig = b;
			eindOplossingPV.setVisible(b);
			if (b) {
				eindOplossingPV.setText("10");
				gelijkwaardigPV.setText("0");
				puntenEindOplossing = 10;
				puntenGelijkwaardig = 0;
			} else {
				exactCB.setSelected(false);
				eindOplossingPV.setText("0");
				gelijkwaardigPV.setText("10");
				puntenEindOplossing = 0;
				puntenGelijkwaardig = 10;
			}
		}
		if (e.getSource() == stappenCB) {
			WiskOpdr.setLaunchDataChanged();
			boolean b = stappenCB.isSelected();
			stappen = b;
		}
		if (e.getSource() == bewerkingKnoppenCB) {
			WiskOpdr.setLaunchDataChanged();
			boolean b = bewerkingKnoppenCB.isSelected();
			bewerkingKnoppen = b;
		}
		if (e.getSource() == abcKnopCB) {
			WiskOpdr.setLaunchDataChanged();
			boolean b = abcKnopCB.isSelected();
			abcKnop = b;
		}
		if (e.getSource() == subKnopCB) {
			WiskOpdr.setLaunchDataChanged();
			boolean b = subKnopCB.isSelected();
			subKnop = b;
		}
		if (e.getSource() == herleidingsKeuze) {
			WiskOpdr.setLaunchDataChanged();
			soortHerleiding = herleidingsKeuze.getSelectedIndex();
		}
		if (e.getSource() == titelCB) {
			WiskOpdr.setLaunchDataChanged();
			setTitle(titelCB.isSelected());
		}
		if (e.getSource() == nieuweVersieCB) {
			WiskOpdr.setLaunchDataChanged();
			boolean b = nieuweVersieCB.isSelected();
			if (!b) {
				scheidingXOud = scheidingX;
				scheidingX = eindX / 2;
			} else
				scheidingX = scheidingXOud;
			zetAntwoordVakUsed(!b);
		}
	}

	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == herleidingsKeuze) {
			if (soortHerleiding != herleidingsKeuze.getSelectedIndex())
				WiskOpdr.setLaunchDataChanged();
			soortHerleiding = herleidingsKeuze.getSelectedIndex();
		}
	}

	public void mousePressed(MouseEvent e) {
		if (e.getX() > scheidingX + corrToolbar - 5 && e.getX() < scheidingX + corrToolbar + 5 && e.getY() < getSize().height - 85)
			scheidingRaak = true;
		else
			scheidingRaak = false;
		
		if (e.getX() > eindX + corrToolbar - 5 && e.getX() < eindX + corrToolbar + 5 && e.getY() < getSize().height - 120)
			eindRaak = true;
		else
			eindRaak = false;
	}

	public void mouseClicked(MouseEvent e) {
		;
	}

	public void mouseReleased(MouseEvent e) {
		scheidingRaak = false;
		eindRaak = false;
	}

	public void mouseEntered(MouseEvent e) {
		kiesCursor(e);
	}

	public void mouseExited(MouseEvent e) {
		kiesCursor(e);
	}

	public void mouseDragged(MouseEvent e) {
		if (scheidingRaak) {
			scheidingX = e.getX() - corrToolbar;
			setSizesGui();
			WiskOpdr.setLaunchDataChanged();
		} else if (eindRaak) {
			eindX = e.getX() - corrToolbar;
			setSizesGui();
			WiskOpdr.setLaunchDataChanged();
		}
	}

	public void mouseMoved(MouseEvent e) {
		kiesCursor(e);
	}

	public void kiesCursor(MouseEvent e) {
		if (e.getX() > scheidingX + corrToolbar - 5 && e.getX() < scheidingX + corrToolbar + 5 && e.getY() < getSize().height - 120 || e.getX() > eindX + corrToolbar - 5 && e.getX() < eindX + corrToolbar + 5
				&& e.getY() < getSize().height - 120) {
			setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
		} else {
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}

	// ActionProducer
	private ActionListener actionListener = null;
  boolean premium;

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
	// end ActionProducer

  public void setEditPanelSize() {
    int w = getWidth();
    int h = getHeight();
    tekstEditor.setBounds(0, 0, w, h);

    
  }
}
