package fi.wiskopdr;

import java.awt.AWTEventMulticaster;
import java.awt.Font;
import java.awt.event.*;
import java.util.*;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.*;

import fi.beans.wiskopdrbeans.*;
import fi.wiskopdr.expressies.Expressie;
import fi.wiskopdr.formuleobjects.*;

public class GrafiekEditPanel extends JPanel 
							  implements InteractieEditPanel, 
										 ActionListener, FocusListener,
										 TabletOwner
{
	private GrafiekPanel grafiekPanel;
	
	// tools panel
	private JCheckBox formulesZichtbaarCB, grafiekTekenenCB, tabelCB,
					  assenCB, roosterCB, schaalCB, piLijnenCB,	 
					  zoomOptieCB, traceOptieCB, dragOptieCB, buttonCB, formFcCB,
					  zoomInTabelCB, tabelAlsTekenToolCB;

	private JLabel varNaamLabel, yAsLabel;
	private JTextField varNaamTF, yAsNaamTF;
	
	private String varNaam = "x";
	private String yAsNaam = "y";
	
	private boolean formulesZichtbaar, grafiekTekenen, tabelZichtbaar,
					assen, rooster, roosterGrof,schaal, piLijnen,
					zoomOptie, traceOptie, dragOptie, buttonOptie, formFc,
					zoomInTabel, tabelAlsTekenTool,
					newVersion, xPositief, yPositief,
					xVarEditable,yVarEditable,
					grafiekKleuren;
	
	// opdrachten panel
	protected static int GEENOPDRACHT = 0;
	protected static int FORMULEBIJGRAFIEK = 1;
	protected static int FORMULEBIJPUNTEN = 2;
	protected static int TEKENTABELPUNTEN = 4;
	protected static int PUNTENBIJFORMULE = 3;
	// etc.
	private int typeOpdracht;
	private JComboBox opdrachtKeuze;
	private boolean opdrachtKeuzeAllowed = true;
	private int scoreMax = 10;
	private JLabel scoreMaxLabel;
	private JTextField scoreMaxTF;	
	
	private JLabel explanLabel1, explanLabel2;
	
	private JLabel nauwkeurigheidLabel;
	private JTextField nauwkeurigheidTF;
	private JLabel minimumPuntenLabel;
	private JTextField minimumPuntenTF;
	int nauwkeurigheid = 5;
	int minimumPunten = 5;
	
	private JCheckBox leerlingZietTabelBox; 
	boolean leerlingZietTabel = true;
	
	private FunctieEditor docentFunctieEditor;
	private GrafiekTekenEditor docentGrafiekTekenEditor;
	private TabelComponent docentTabelComponent;
	
	// uit docentFormuleEditor
	private Expressie docentExpressie;	
	// zelfde, maar dan als String
	private String docentExpressieString;
	// uit docentGrafiekTekenEditor
	private Vector docentGrafiekPunten;
	// uit docentTabelComponent
	private Vector docentTabelPunten;
	private Vector docentTabelPuntVakIndex;
	private Vector docentTabelStringsX;
	private Vector docentTabelStringsY;
	
	private Tablet tablet;
	private boolean tabletAdded;
	private FormuleVakHouder tabletUser;
	
	private Font font = new Font("SansSerif",Font.PLAIN,12);
	
	private JComponent component;
	
	private JPanel toolsPanel, opdrachtenPanel; 
	private JTabbedPane tabbedPane;
	
	private JCheckBox newVersionCB;
	private JCheckBox xPositiefCB;
	private JCheckBox yPositiefCB;
	private JCheckBox xVarEditableCB;
	private JCheckBox yVarEditableCB;
	private JCheckBox roosterGrofCB;
	private JCheckBox grafiekKleurenCB;
	
	public GrafiekEditPanel()
	{	
		addMouseListener(new MouseAdapter()
			{	public void mousePressed(MouseEvent e)
				{	requestFocus();
				}
			});
		component = this;
		
		setLayout(null);
		setBackground(WiskOpdr.bgcolor);
		grafiekPanel = new GrafiekPanel();
		grafiekPanel.setBounds(10,20,300,440);
		add(grafiekPanel);
		
		grafiekPanel.getTabelComponent().setRandomAllowed(true);
		
		
		tabbedPane = new JTabbedPane();
		// dit NIET doen
		//tabbedPane.setLayout(null);
		// dit is de kleur van de niet aktieve tabs
		tabbedPane.setBackground(getBackground());				
		// hier OOK size zetten
		tabbedPane.setBounds(350, 10, 420, 510);		
		add(tabbedPane);

//boolean op = tabbedPane.isOpaque();
//System.out.println("op = " + op);
// tabbedPane is niet opaque
		tabbedPane.setOpaque(true);

		toolsPanel = new JPanel();
		toolsPanel.setLayout(null);
		toolsPanel.setBackground(getBackground());		
//toolsPanel.setBounds(350, 10, 420, 480);
//toolsPanel.setSize(420, 480);
		toolsPanel.setPreferredSize(new Dimension(420, 480));
		tabbedPane.add("tools", toolsPanel);
		
		opdrachtenPanel = new JPanel();
		opdrachtenPanel.setLayout(null);	
		opdrachtenPanel.setBackground(getBackground());		
//opdrachtenPanel.setBounds(350, 10, 420, 480);
//opdrachtenPanel.setSize(420, 480);
		toolsPanel.setPreferredSize(new Dimension(420, 480));
		tabbedPane.add(WiskOpdr.rb.getString("GEP_opdrachten"), opdrachtenPanel);
		
		// dit werkt niet
		// de active tab blijft grijs
		tabbedPane.setBackgroundAt(0, null);
		tabbedPane.setBackgroundAt(1, null);

		// toolsPanel defaults
		formulesZichtbaar = true;
		grafiekKleuren = true;
		grafiekTekenen = false;
		tabelZichtbaar = false;
		assen = true;
		rooster = true;
		roosterGrof = false;
		schaal = true;
		piLijnen = false;
		zoomOptie = true;
		traceOptie = true;
		dragOptie = true;
		formFc = true;
		zoomInTabel = true;
		tabelAlsTekenTool = false;
		
		JLabel variabelenLabel = new JLabel(WiskOpdr.rb.getString("GEP_varLabel"));
		variabelenLabel.setBounds(20,10,150,20);
		toolsPanel.add(variabelenLabel);
		
		JLabel assenstelselLabel = new JLabel(WiskOpdr.rb.getString("GEP_assenLabel"));;
		assenstelselLabel.setBounds(20,100,150,20);
		toolsPanel.add(assenstelselLabel);
		
		JLabel grafiekOptiesLabel = new JLabel(WiskOpdr.rb.getString("GEP_grafiekOptiesLabel"));
		// 150,250
		grafiekOptiesLabel.setBounds(20,190,150,20);
		toolsPanel.add(grafiekOptiesLabel);
		
		varNaamLabel = new JLabel(WiskOpdr.rb.getString("GEP_varXLabel"));
		varNaamLabel.setBounds(20,35,150,20);
		varNaamLabel.setFont(font);
		toolsPanel.add(varNaamLabel);
		
		varNaamTF = new JTextField(varNaam);
		varNaamTF.setBounds(160,35,50,20);
		varNaamTF.setFont(font);
		toolsPanel.add(varNaamTF);
		varNaamTF.addKeyListener(new KeyAdapter()
		{	public void keyReleased(KeyEvent e)
			{	String[] forbiddenStrings = {"sin","cos","tan","ln","log"};
				String text = varNaamTF.getText().trim();
				if(text.length()>0 && !Character.isLetter(text.charAt(0)))
				{	JOptionPane.showMessageDialog(WiskOpdr.applet, WiskOpdr.rb.getString("xVarMessage1"));
					update(text.substring(1),true);
				}
				else if(text.length()>1 && !FormuleParser.isWoordFormule())
				{	JOptionPane.showMessageDialog(WiskOpdr.applet, WiskOpdr.rb.getString("xVarMessage2"));
					update(text.substring(0,1),true);
				}
				else if(text.length()==1 && text.charAt(0)=='e')
				{	JOptionPane.showMessageDialog(WiskOpdr.applet, WiskOpdr.rb.getString("xVarMessage3"));
					if(FormuleParser.isWoordFormule())update("x", false);
					else update(text.substring(1),true);
				}
				else if(text.length()!=0)
				{	boolean forbidden = false;
					for(int i=0 ; i<forbiddenStrings.length ; i++)
					{	if(text.indexOf(forbiddenStrings[i])>-1)
						{	JOptionPane.showMessageDialog(WiskOpdr.applet, WiskOpdr.rb.getString("xVarMessage4a") + forbiddenStrings[i] + WiskOpdr.rb.getString("xVarMessage4b"));
							if(text.length()>0)update(text.substring(0,text.indexOf(forbiddenStrings[i])),true);
							forbidden = true;
						}
					}
					if(!forbidden)update(text,true);
				}
				if(text.length()==0) update("x", false);
			}
			public void update(String s, boolean updateTF)
			{	varNaam = s;
				if(updateTF) varNaamTF.setText(varNaam);
				if(varNaam.equals(""))varNaam = "x";
				grafiekPanel.zetVarNaam(varNaam);
				varNaamTF.requestFocus();
			}
		});
		
		
		yAsLabel = new JLabel(WiskOpdr.rb.getString("GEP_varYLabel"));
		yAsLabel.setBounds(20,65,150,20);
		yAsLabel.setFont(font);
		toolsPanel.add(yAsLabel);
		
		yAsNaamTF = new JTextField(yAsNaam);
		yAsNaamTF.setBounds(160,65,50,20);
		yAsNaamTF.setFont(font);
		toolsPanel.add(yAsNaamTF);
		yAsNaamTF.addKeyListener(new KeyAdapter()
		{	public void keyReleased(KeyEvent e)
			{	yAsNaam = yAsNaamTF.getText().trim();
				yAsNaamTF.setText(yAsNaam);
				if(yAsNaam.equals(""))yAsNaam = "y";
				grafiekPanel.zetYAsNaam(yAsNaam);
				yAsNaamTF.requestFocus();
			}
		});
		
		assenCB = // 150,125
			maakCheckBox(WiskOpdr.rb.getString("GEP_assenOptie"), 20,125,140,20, assen, toolsPanel);
		roosterCB = // 150,155
			maakCheckBox(WiskOpdr.rb.getString("GEP_roosterOptie"), 160,125,70,20, rooster, toolsPanel); 
		roosterGrofCB = // 150,155
			maakCheckBox(WiskOpdr.rb.getString("GEP_roosterGrofOptie"), 230,125,60,20, roosterGrof, toolsPanel); 
		schaalCB = // 150, 185
			maakCheckBox(WiskOpdr.rb.getString("GEP_schaalverdelingOptie"), 20,155,140,20, schaal, toolsPanel);
		piLijnenCB = // 150, 215
			maakCheckBox(WiskOpdr.rb.getString("GEP_piOptie"), 160,155,140,20, piLijnen, toolsPanel);
			
		zoomOptieCB = // 150,275
			maakCheckBox(WiskOpdr.rb.getString("GEP_zoomOptie"), 20,215,140,20, zoomOptie, toolsPanel);
		traceOptieCB = // 150, 305
			maakCheckBox(WiskOpdr.rb.getString("GEP_traceOptie"), 160,215,140,20, traceOptie, toolsPanel);
		dragOptieCB = // 150, 335
			maakCheckBox(WiskOpdr.rb.getString("GEP_dragOptie"), 20,245,140,20, dragOptie, toolsPanel);
			
		
		grafiekTekenenCB = 
			maakCheckBox(WiskOpdr.rb.getString("GEP_tekenOptie"), 20,275,160,20, grafiekTekenen, toolsPanel);
			
		tabelCB = 
			maakCheckBox(WiskOpdr.rb.getString("GEP_tabelOptie"), 20,305,160,20, tabelZichtbaar, toolsPanel);
			
		zoomInTabelCB =
			maakCheckBox(WiskOpdr.rb.getString("GEP_zoomInTabelOptie"), 40, 335, 160, 20, zoomInTabel, toolsPanel);
		zoomInTabelCB.setVisible(false);	
			
		tabelAlsTekenToolCB =
			maakCheckBox(WiskOpdr.rb.getString("GEP_tabelTekenToolOptie"), 40, 365, 160, 20, tabelAlsTekenTool, toolsPanel);
		tabelAlsTekenToolCB.setVisible(false);			
			
		formulesZichtbaarCB = 
			maakCheckBox(WiskOpdr.rb.getString("GEP_functieEditOptie"), 20,395,160,20, formulesZichtbaar, toolsPanel);
		grafiekKleurenCB = 
			maakCheckBox(WiskOpdr.rb.getString("GEP_grafiekKleurenOptie"), 180,395,160,20, grafiekKleuren, toolsPanel);

			
		buttonCB = 
			maakCheckBox(WiskOpdr.rb.getString("GEP_popupWeergave"), 150,350,160,20, buttonOptie, toolsPanel);
			toolsPanel.remove(buttonCB);
		formFcCB = 
			maakCheckBox(WiskOpdr.rb.getString("GEP_formeleFunctieNotatieOptie"), 
				40,425,240,20, formFc, toolsPanel);
		
		newVersionCB = maakCheckBox("new version", 300,5,160,20, newVersion, toolsPanel);
		
		xPositiefCB = maakCheckBox(WiskOpdr.rb.getString("GEP_xPositief"), 300,125,160,20, xPositief, toolsPanel);
		yPositiefCB = maakCheckBox(WiskOpdr.rb.getString("GEP_yPositief"), 300,155,160,20, yPositief, toolsPanel);
		xVarEditableCB = maakCheckBox(WiskOpdr.rb.getString("GEP_xEditable"), 300,35,100,20, xVarEditable, toolsPanel);
		yVarEditableCB = maakCheckBox(WiskOpdr.rb.getString("GEP_yEditable"), 300,65,100,20, yVarEditable, toolsPanel);
		
		xPositiefCB.setVisible(false);
		yPositiefCB.setVisible(false);
		xVarEditableCB.setVisible(false);
		yVarEditableCB.setVisible(false);
		
		
		
		// opdrachtPanel
		typeOpdracht = GEENOPDRACHT;
	
		opdrachtKeuze = new JComboBox();
		opdrachtKeuze.setBackground(getBackground());
		opdrachtKeuze.setFont(font);
		opdrachtKeuze.setBounds(20, 30, 300, 20);

		opdrachtKeuze.addItem(WiskOpdr.rb.getString("GEP_kiesOpdracht"));
		opdrachtKeuze.addItem(WiskOpdr.rb.getString("GEP_opdracht1"));
		opdrachtKeuze.addItem(WiskOpdr.rb.getString("GEP_opdracht2"));
		opdrachtKeuze.addItem(WiskOpdr.rb.getString("GEP_opdracht3"));
		opdrachtKeuze.addItem(WiskOpdr.rb.getString("GEP_opdracht4"));

		opdrachtenPanel.add(opdrachtKeuze);	
		opdrachtKeuze.addActionListener(new OpdrachtKeuzeAL());
		
		explanLabel1 = new JLabel();
		explanLabel1.setBackground(getBackground());
		explanLabel1.setFont(font);
		explanLabel1.setBounds(25, 30, 350, 20);		
		explanLabel1.setVisible(false);
		opdrachtenPanel.add(explanLabel1);	

		explanLabel2 = new JLabel();
		explanLabel2.setBackground(getBackground());
		explanLabel2.setFont(font);
		explanLabel2.setBounds(25, 30, 350, 20);		
		explanLabel2.setVisible(false);
		opdrachtenPanel.add(explanLabel2);	
		
		scoreMaxLabel = new JLabel(WiskOpdr.rb.getString("GEP_maximumScore"));
		scoreMaxLabel.setBackground(getBackground());
		scoreMaxLabel.setFont(font);
		scoreMaxLabel.setBounds(65, 70, 100, 20);
		scoreMaxLabel.setVisible(false);
		opdrachtenPanel.add(scoreMaxLabel);	
		
		scoreMaxTF = new JTextField("" + scoreMax);
		scoreMaxTF.setFont(font);
		scoreMaxTF.setBounds(215, 70, 70, 20);
		scoreMaxTF.setVisible(false);
		opdrachtenPanel.add(scoreMaxTF);			
		scoreMaxTF.addActionListener(this);
		scoreMaxTF.addFocusListener(this);

		nauwkeurigheidLabel = new JLabel(WiskOpdr.rb.getString("GEP_nauwkeurigheid"));
		nauwkeurigheidLabel.setBackground(getBackground());
		nauwkeurigheidLabel.setFont(font);
		nauwkeurigheidLabel.setBounds(65, 70, 150, 20);
		nauwkeurigheidLabel.setVisible(false);
		opdrachtenPanel.add(nauwkeurigheidLabel);	

		nauwkeurigheidTF = new JTextField("" + nauwkeurigheid);
		nauwkeurigheidTF.setFont(font);
		nauwkeurigheidTF.setBounds(215, 70, 70, 20);
		nauwkeurigheidTF.setVisible(false);
		opdrachtenPanel.add(nauwkeurigheidTF);	
		nauwkeurigheidTF.addActionListener(this);
		nauwkeurigheidTF.addFocusListener(this);

		
		minimumPuntenLabel = new JLabel(WiskOpdr.rb.getString("GEP_minimumPunten"));
		minimumPuntenLabel.setBackground(getBackground());
		minimumPuntenLabel.setFont(font);
		minimumPuntenLabel.setBounds(65, 70, 150, 20);
		minimumPuntenLabel.setVisible(false);
		opdrachtenPanel.add(minimumPuntenLabel);	

		minimumPuntenTF = new JTextField("" + minimumPunten);
		minimumPuntenTF.setFont(font);
		minimumPuntenTF.setBounds(215, 70, 70, 20);
		minimumPuntenTF.setVisible(false);
		opdrachtenPanel.add(minimumPuntenTF);	
		minimumPuntenTF.addActionListener(this);
		minimumPuntenTF.addFocusListener(this);
		
		leerlingZietTabelBox = new JCheckBox(WiskOpdr.rb.getString("GEP_leerlingZietTabel"));
		leerlingZietTabelBox.setBackground(getBackground());
		leerlingZietTabelBox.setFont(font);
		leerlingZietTabelBox.setBounds(65, 70, 150, 20);
		leerlingZietTabelBox.setSelected(true);
		leerlingZietTabelBox.setVisible(false);
		opdrachtenPanel.add(leerlingZietTabelBox);	
		leerlingZietTabelBox.addActionListener(this);
		
		
// nauwkeurigheid??

		docentExpressie = null;
		docentExpressieString = "$f@";
		docentGrafiekPunten = new Vector();
		docentTabelPunten = new Vector();
		docentTabelPuntVakIndex = new Vector();
		docentTabelStringsX = new Vector();
		docentTabelStringsY = new Vector();

	}
	
	public void resetDocentInput()
	{
		docentExpressie = null;
		docentExpressieString = "$f@";
		docentGrafiekPunten = new Vector();
		docentTabelPunten = new Vector();
		docentTabelPuntVakIndex = new Vector();
		docentTabelStringsX = new Vector();
		docentTabelStringsY = new Vector();
		
	}
	private JCheckBox maakCheckBox(
		String s, int x, int y, int b, int h, boolean selected, JPanel parent)
	{	JCheckBox checkbox = new JCheckBox(s);
		checkbox.setBounds(x,y,b,h);
		checkbox.setFont(font);
		checkbox.setBackground(getBackground());
		checkbox.setSelected(selected);
		checkbox.addActionListener(this);
		parent.add(checkbox);
		
		return checkbox;
	}
	
	
	private void zetOpdrachtKeuze(int keuze, boolean setState)
	{	
//System.out.println("zok = " + keuze + " " + setState);		
		int oudeKeuze = typeOpdracht;
		opdrachtKeuzeAllowed = false;
		opdrachtKeuze.setSelectedIndex(keuze);
		opdrachtKeuzeAllowed = true;
		typeOpdracht = keuze;
		
//System.out.println("gep zetOpdrKeuze begin tc = " + grafiekPanel.getTabelComponent().geefTabelpunten().size());		
//System.out.println("zok = " + keuze + " " + setState);		
		
		// geen opdracht terug naar defaults
		if (typeOpdracht == GEENOPDRACHT)
		{	explanLabel1.setVisible(false);
			explanLabel2.setVisible(false);
			scoreMaxLabel.setVisible(false);
			scoreMaxTF.setVisible(false);
			
			nauwkeurigheidLabel.setVisible(false);
			nauwkeurigheidTF.setVisible(false);
			minimumPuntenLabel.setVisible(false);
			minimumPuntenTF.setVisible(false);
			
			leerlingZietTabelBox.setVisible(false);

			// verwijder docentFunctieEditor
			// en koppel deze los van de grafiekComponent links 
			// en zet de docentExpressie in grafiekPanel op null
			// functieEditor links krijgt maxFuncties 9
			// docentExpressie hier bewaren?
			if (docentFunctieEditor != null)
			{	removeDocentFunctieEditor();
			}
			// verwijder docentGrafiekTekenEditor
			// en koppel deze los van grafiekComponent links
			// en maak de docentGrafiekPunten in grafiekPanel leeg
			// docentGrafiekPunten hier bewaren??
			if (docentGrafiekTekenEditor != null)
			{	removeDocentGrafiekTekenEditor();
			}
			// verwijder docentTabelComponent
			// en koppel deze los van de tabel links
			// en maak de docentTabelPunten in grafiekPanel leeg
			// zet de tabel links terug naar de defaults
			if (docentTabelComponent != null)
			{	removeDocentTabelComponent();
			}

			resetDocentInput();
			
			grafiekTekenenCB.setEnabled(true);
			tabelCB.setEnabled(true);
			formulesZichtbaarCB.setEnabled(true);
			formFcCB.setEnabled(true);
			tabelAlsTekenToolCB.setEnabled(true);
			
			// terug naar defaults
			boolean formulesZichtbaar = this.formulesZichtbaar;
			boolean grafiekTekenen = this.grafiekTekenen;
			boolean tabelZichtbaar = this.tabelZichtbaar;
			boolean formFc = this.formFc;
			boolean zoomInTabel = this.zoomInTabel;
			boolean tabelAlsTekenTool = this.tabelAlsTekenTool;
			
			if (!tabelAlsTekenTool)
				zoomInTabelCB.setEnabled(true);
			
			//grafiekTekenenCB.setSelected(grafiekTekenen);		
			//tabelCB.setSelected(tabelZichtbaar);		
			//formulesZichtbaarCB.setSelected(formulesZichtbaar);
			//formFcCB.setSelected(formFc);
			
			grafiekPanel.zetGrafiekTekenen(grafiekTekenen);
			grafiekPanel.zetTabelZichtbaar(tabelZichtbaar);
			grafiekPanel.zetFormulesZichtbaar(formulesZichtbaar);
			grafiekPanel.zetFormalFunction(formFc);
			grafiekPanel.zetTabelAlsTekenTool(tabelAlsTekenTool);
			grafiekPanel.zetZoomInTabel(zoomInTabel);
			
			grafiekPanel.getFunctieEditor().setEditable(true);
			
			grafiekPanel.getTabelComponent().setFrozen(false);
	
			if (oudeKeuze != GEENOPDRACHT)
				grafiekPanel.getTabelComponent().removeAllPoints();
			
			grafiekPanel.getGrafiekTekenEditor().zetEenGrafiek(false);
			grafiekPanel.getGrafiekTekenEditor().zetAlleenPunten(false);
			grafiekPanel.getGrafiekTekenEditor().setFrozen(false);
			
			grafiekPanel.zetKijkNaButton(false);
			
//System.out.println("gep zetOpdrKeuze geenopdr tc = " + grafiekPanel.getTabelComponent().geefTabelpunten().size());			
		}
		else if (typeOpdracht == FORMULEBIJGRAFIEK)
		{	
			// verwijder docentGrafiekTekenEditor
			// zie boven
			if (docentGrafiekTekenEditor != null)
			{	removeDocentGrafiekTekenEditor();
			}
			// verwijder docentTabelComponent
			// zie boven
			if (docentTabelComponent != null)
			{	removeDocentTabelComponent();
			}

			// toelichting			
			explanLabel1.setText(WiskOpdr.rb.getString("GEP_antwoordFormule"));		
			explanLabel1.setLocation(explanLabel1.getLocation().x, 70);
			explanLabel1.setVisible(true);
			
			explanLabel2.setVisible(false);
		
			// oude wordt vanzelf verwijderd
			maakDocentFunctieEditor(65, 110, 270, 120);			

			if (setState)
			{	
				if (docentExpressie != null)
				{	docentFunctieEditor.zetExpressie(docentExpressie);
					grafiekPanel.getGrafiekComponent().zetDocentExpressie(docentExpressie);
				}
				docentFunctieEditor.geefFormuleVak().vulVak(docentExpressieString);
			}
			else
			{	resetDocentInput();
			}
			
			nauwkeurigheidLabel.setVisible(false);
			nauwkeurigheidTF.setVisible(false);
			minimumPuntenLabel.setVisible(false);
			minimumPuntenTF.setVisible(false);

			leerlingZietTabelBox.setVisible(false);
			
			scoreMaxLabel.setLocation(scoreMaxLabel.getLocation().x, 260);
			scoreMaxTF.setLocation(scoreMaxTF.getLocation().x, 260);
			scoreMaxLabel.setVisible(true);
			scoreMaxTF.setVisible(true);

			// leerling heeft alleen functie-editor			
			boolean formulesZichtbaar = true;
			boolean formFc = formFcCB.isSelected();//true;
			boolean grafiekTekenen = false;
			boolean tabelZichtbaar = false;
			//grafiekTekenenCB.setSelected(grafiekTekenen);		
			//tabelCB.setSelected(tabelZichtbaar);		
			//formulesZichtbaarCB.setSelected(formulesZichtbaar);
			//formFcCB.setSelected(formFc);

			grafiekPanel.zetGrafiekTekenen(grafiekTekenen);
			grafiekPanel.zetTabelZichtbaar(tabelZichtbaar);
			grafiekPanel.zetFormulesZichtbaar(formulesZichtbaar);
			grafiekPanel.zetFormalFunction(formFc);

			grafiekTekenenCB.setEnabled(false);
			tabelCB.setEnabled(false);
			formulesZichtbaarCB.setEnabled(false);
			formFcCB.setEnabled(false);
			zoomInTabelCB.setEnabled(false);
			tabelAlsTekenToolCB.setEnabled(false);
			
			grafiekPanel.getFunctieEditor().terugNaarEenRegel();
			grafiekPanel.getFunctieEditor().zetMaxAantalFuncties(1);
			grafiekPanel.getFunctieEditor().setEditable(false);
			
			// disable die functieEditor??
			grafiekPanel.zetKijkNaButton(false);
			
			
		}
		else if (typeOpdracht == FORMULEBIJPUNTEN)
		{	
			// verwijder docentGrafiekTekenEditor
			// zie boven
			if (docentGrafiekTekenEditor != null)
			{	removeDocentGrafiekTekenEditor();
			}
			// verwijder docentTabelComponent
			// zie boven
			if (docentTabelComponent != null)
			{	removeDocentTabelComponent();
			}
			// toelichting			
			explanLabel1.setText(WiskOpdr.rb.getString("GEP_antwoordFormule"));		
			explanLabel1.setLocation(explanLabel1.getLocation().x, 70);
			explanLabel1.setVisible(true);
			
			// oude wordt verwijderd
			maakDocentFunctieEditor(65, 110, 270, 120);
			
			if (setState)
			{	
				if (docentExpressie != null)
				{	docentFunctieEditor.zetExpressie(docentExpressie);
// leerling ziet geen formule
// misschien wel laten zien aan docent i.v.m.
// makkelijker tekenen punten 			
//					grafiekPanel.getGrafiekComponent().zetDocentExpressie(docentExpressie);			
				}
				docentFunctieEditor.geefFormuleVak().vulVak(docentExpressieString);
//System.out.println("des = " + docentExpressieString);				
			}
			else
				resetDocentInput();
			
			explanLabel2.setText(WiskOpdr.rb.getString("GEP_tabelPuntenX"));		
			explanLabel2.setLocation(explanLabel1.getLocation().x, 250);
			explanLabel2.setVisible(true);
			
			docentTabelComponent = new TabelComponent(270);
			docentTabelComponent.setLocation(65, 290);
			opdrachtenPanel.add(docentTabelComponent);
			docentTabelComponent.zetAlsTekenTool(true);
			docentTabelComponent.zetEenTabel(true);
			docentTabelComponent.setYEditable(false);
			docentTabelComponent.setYVisible(false);
			
			docentTabelComponent.setRandomAllowed(true);			
			
			if (setState)
			{
				if ((docentTabelStringsX.size() == 0) && (docentTabelPunten.size() > 0))
				{	for (int sCnt = 0; sCnt < docentTabelPunten.size(); sCnt++)
					{	docentTabelStringsX.addElement(new String(""));
						docentTabelStringsY.addElement(new String(""));
					}
					
				}
				docentTabelComponent.zetTabelStringsX(docentTabelStringsX);
				docentTabelComponent.zetTabelStringsY(docentTabelStringsY);
				docentTabelComponent.zetDocentTabelPuntVakIndex(docentTabelPuntVakIndex);
				docentTabelComponent.zetDocentTabelPunten(docentTabelPunten, true);
			}
			
			docentTabelComponent.addActionListener(this);
// dit kan weg, er wordt toch niet getekend ??
// je kan ook de punten (x,0) tekenen			
			docentTabelComponent.zetGrafiekComponent(grafiekPanel.getGrafiekComponent());
			grafiekPanel.getGrafiekComponent().zetTabelComponent(docentTabelComponent);


			nauwkeurigheidLabel.setVisible(false);
			nauwkeurigheidTF.setVisible(false);
			minimumPuntenLabel.setVisible(false);
			minimumPuntenTF.setVisible(false);
			
			leerlingZietTabelBox.setVisible(false);
			
			scoreMaxLabel.setLocation(scoreMaxLabel.getLocation().x, 364);
			scoreMaxTF.setLocation(scoreMaxTF.getLocation().x, 364);
			scoreMaxLabel.setVisible(true);
			scoreMaxTF.setVisible(true);

			// leerling heeft alleen functie-editor
			boolean formulesZichtbaar = true;
			boolean formFc = formFcCB.isSelected();//true;
			boolean grafiekTekenen = false;
			boolean tabelZichtbaar = false;
			
			//grafiekTekenenCB.setSelected(grafiekTekenen);		
			//tabelCB.setSelected(tabelZichtbaar);		
			//formulesZichtbaarCB.setSelected(formulesZichtbaar);

			// DIT NIET, je gooit de docentGrafiekTekenEditor er weer uit
			grafiekPanel.zetGrafiekTekenen(grafiekTekenen);
			grafiekPanel.zetTabelZichtbaar(tabelZichtbaar);
			grafiekPanel.zetFormulesZichtbaar(formulesZichtbaar);
			grafiekPanel.zetFormalFunction(formFc);

			grafiekTekenenCB.setEnabled(false);
			tabelCB.setEnabled(false);
			formulesZichtbaarCB.setEnabled(false);
			formFcCB.setEnabled(false);
			zoomInTabelCB.setEnabled(false);
			tabelAlsTekenToolCB.setEnabled(false);
			
			grafiekPanel.getFunctieEditor().terugNaarEenRegel();
			grafiekPanel.getFunctieEditor().zetMaxAantalFuncties(1);
			grafiekPanel.getFunctieEditor().setEditable(false);
			
			// disable die functieEditor??
			grafiekPanel.zetKijkNaButton(false);


		}
		else if (typeOpdracht == TEKENTABELPUNTEN)
		{	
			// verwijder de docentFuncieEditor			
			// zie boven
			if (docentFunctieEditor != null)
			{	removeDocentFunctieEditor();
			}
			// verwijder docentGrafiekTekenEditor
			// zie boven
			if (docentGrafiekTekenEditor != null)
			{	removeDocentGrafiekTekenEditor();
			}
			// verwijder docentTabelComponent
			// zie boven
			if (docentTabelComponent != null)
			{	removeDocentTabelComponent();
			}
			
			// toelichting			
			explanLabel1.setText(WiskOpdr.rb.getString("GEP_tabelPunten"));		
			explanLabel1.setLocation(explanLabel1.getLocation().x, 70);
			explanLabel1.setVisible(true);		
		
			explanLabel2.setVisible(false);
			
			docentTabelComponent = new TabelComponent(270);
			docentTabelComponent.setXEditable(true);
			docentTabelComponent.setYEditable(true);
			docentTabelComponent.zetZooming(false);
			docentTabelComponent.zetReset(true);
			
			docentTabelComponent.addActionListener(this);

			docentTabelComponent.setRandomAllowed(true);
			
			//grafiekPanel.getTabelComponent().zetAlsTekenTool(true);
			grafiekPanel.getTabelComponent().zetEenTabel(true);
			grafiekPanel.getTabelComponent().setXEditable(false);
			grafiekPanel.getTabelComponent().setYEditable(false);
			grafiekPanel.getTabelComponent().setRandomAllowed(true);
			
			
			if (setState)
			{	
				docentTabelComponent.zetTabelStringsX(docentTabelStringsX);
				docentTabelComponent.zetTabelStringsY(docentTabelStringsY);
				docentTabelComponent.zetDocentTabelPuntVakIndex(docentTabelPuntVakIndex);
				docentTabelComponent.zetDocentTabelPunten(docentTabelPunten, true);
				
//System.out.println("gp tc zet");

				grafiekPanel.getTabelComponent().zetTabelStringsX(docentTabelStringsX);
				grafiekPanel.getTabelComponent().zetTabelStringsY(docentTabelStringsY);
				grafiekPanel.getTabelComponent().zetDocentTabelPuntVakIndex(docentTabelPuntVakIndex);
				grafiekPanel.getTabelComponent().zetDocentTabelPunten(docentTabelPunten, true);
				
				grafiekPanel.getTabelComponent().zetFirstIndexVisible(docentTabelComponent.getFirstIndexVisible());
				
				//docentTabelComponent.produceAction("points changed");
			}
			else
			{	resetDocentInput();
				leerlingZietTabel = true;
			}
			
			docentTabelComponent.setLocation(65, 110);
			opdrachtenPanel.add(docentTabelComponent);
			opdrachtenPanel.repaint();

			int maxY = 110 + docentTabelComponent.getSize().height + 40;

			leerlingZietTabelBox.setLocation(leerlingZietTabelBox.getLocation().x, maxY);
			leerlingZietTabelBox.setVisible(true);
			maxY += 40;
			
			nauwkeurigheidLabel.setLocation(nauwkeurigheidLabel.getLocation().x, maxY);
			nauwkeurigheidLabel.setVisible(true);
			nauwkeurigheidTF.setLocation(nauwkeurigheidTF.getLocation().x, maxY);
			nauwkeurigheidTF.setVisible(true);
			maxY += 40;

			scoreMaxLabel.setLocation(scoreMaxLabel.getLocation().x, maxY);
			scoreMaxTF.setLocation(scoreMaxTF.getLocation().x, maxY);
			scoreMaxLabel.setVisible(true);
			scoreMaxTF.setVisible(true);
			
			minimumPuntenLabel.setVisible(false);			
			// leerling heeft grafiekTekenEditor
			// en een tabelcomponent
			// dat is geen tekentool (dus hangt er een functie-editor
			// aan, maar die is niet zichtbaar!! dus loopt niet in de weg
			boolean formulesZichtbaar = false;
			boolean formFc = formFcCB.isSelected();//true;
			boolean grafiekTekenen = true;
			boolean tabelZichtbaar = leerlingZietTabel;
			leerlingZietTabelBox.setSelected(leerlingZietTabel);
			boolean zoomInTabel = false;
			boolean tabelAlsTekenTool = false;
			//grafiekTekenenCB.setSelected(grafiekTekenen);		
			//tabelCB.setSelected(tabelZichtbaar);		
			//formulesZichtbaarCB.setSelected(formulesZichtbaar);
			//formFcCB.setSelected(formFc);

			grafiekPanel.zetGrafiekTekenen(grafiekTekenen);
			grafiekPanel.zetTabelZichtbaar(tabelZichtbaar);
			grafiekPanel.zetFormulesZichtbaar(formulesZichtbaar);
			grafiekPanel.zetFormalFunction(formFc);
			grafiekPanel.zetZoomInTabel(zoomInTabel);
			//grafiekPanel.zetTabelAlsTekenTool(tabelAlsTekenTool);

			grafiekTekenenCB.setEnabled(false);
			tabelCB.setEnabled(false);
			formulesZichtbaarCB.setEnabled(false);
			formFcCB.setEnabled(false);
			zoomInTabelCB.setEnabled(false);
			tabelAlsTekenToolCB.setEnabled(false);
			
			grafiekPanel.getFunctieEditor().terugNaarEenRegel();
			grafiekPanel.getFunctieEditor().zetMaxAantalFuncties(1);
			//grafiekPanel.getFunctieEditor().setEditable(false);			
			
			grafiekPanel.zetTabelAlsTekenTool(tabelAlsTekenTool);
			grafiekPanel.getTabelComponent().setXEditable(false);
			grafiekPanel.getTabelComponent().setYEditable(false);
			
			grafiekPanel.getTabelComponent().setFrozen(true);
			grafiekPanel.getTabelComponent().zetEenTabel(true);
			grafiekPanel.getTabelComponent().zetYNaam("y");
			//grafiekPanel.getTabelComponent().setRandomAllowed(true);
			

			grafiekPanel.getGrafiekTekenEditor().zetEenGrafiek(true);
			grafiekPanel.getGrafiekTekenEditor().zetAlleenPunten(true);
			grafiekPanel.getGrafiekTekenEditor().setFrozen(true);
			grafiekPanel.getGrafiekTekenEditor().removeAllPoints();
			
			docentTabelComponent.produceAction("points changed");
			
			grafiekPanel.zetKijkNaButton(true);

		}
		else if (typeOpdracht == PUNTENBIJFORMULE)
		{	// verwijder docentGrafiekTekenEditor
			// zie boven
			if (docentGrafiekTekenEditor != null)
			{	removeDocentGrafiekTekenEditor();
			}
			// verwijder docentTabelComponent
			// zie boven
			if (docentTabelComponent != null)
			{	removeDocentTabelComponent();
			}

			// toelichting			
			explanLabel1.setText("voer in de funktie-editor hierbeneden de antwoordformule in");		
			explanLabel1.setLocation(explanLabel1.getLocation().x, 70);
			explanLabel1.setVisible(true);
			
			explanLabel2.setVisible(false);

			// oude wordt verwijderd
			maakDocentFunctieEditor(65, 110, 270, 120);
			
			if (setState)
			{	
				if (docentExpressie != null)
				{	docentFunctieEditor.zetExpressie(docentExpressie);
// leerling ziet geen formule			
//					grafiekPanel.getGrafiekComponent().zetDocentExpressie(docentExpressie);			
				}
				docentFunctieEditor.geefFormuleVak().vulVak(docentExpressieString);
//System.out.println("des = " + docentExpressieString);				
			}
			else
				resetDocentInput();
			
			leerlingZietTabelBox.setVisible(false);			
			
			int maxY = 260;
			nauwkeurigheidLabel.setLocation(nauwkeurigheidLabel.getLocation().x, maxY);
			nauwkeurigheidLabel.setVisible(true);
			nauwkeurigheidTF.setLocation(nauwkeurigheidTF.getLocation().x, maxY);
			nauwkeurigheidTF.setVisible(true);
			maxY += 40;
			minimumPuntenLabel.setLocation(minimumPuntenLabel.getLocation().x, maxY);
			minimumPuntenLabel.setVisible(true);
			minimumPuntenTF.setLocation(minimumPuntenTF.getLocation().x, maxY);
			minimumPuntenTF.setVisible(true);
			maxY += 40;

			scoreMaxLabel.setLocation(scoreMaxLabel.getLocation().x, maxY);
			scoreMaxTF.setLocation(scoreMaxTF.getLocation().x, maxY);
			scoreMaxLabel.setVisible(true);
			scoreMaxTF.setVisible(true);

			// leerling heeft alleen grafiekTekenTool
			boolean formulesZichtbaar = false;
			formFc = formFcCB.isSelected();//
			boolean grafiekTekenen = true;
			boolean tabelZichtbaar = false;
			
//			grafiekTekenenCB.setSelected(grafiekTekenen);		
//			tabelCB.setSelected(tabelZichtbaar);		
//			formulesZichtbaarCB.setSelected(formulesZichtbaar);

			grafiekPanel.zetGrafiekTekenen(grafiekTekenen);
			grafiekPanel.zetTabelZichtbaar(tabelZichtbaar);
			grafiekPanel.zetFormulesZichtbaar(formulesZichtbaar);

			grafiekTekenenCB.setEnabled(false);
			tabelCB.setEnabled(false);
			formulesZichtbaarCB.setEnabled(false);
			formFcCB.setEnabled(false);
			zoomInTabelCB.setEnabled(false);
			tabelAlsTekenToolCB.setEnabled(false);
			
			//grafiekPanel.getFunctieEditor().terugNaarEenRegel();
			//grafiekPanel.getFunctieEditor().zetMaxAantalFuncties(1);
			//grafiekPanel.getFunctieEditor().setEditable(false);
			
			// zet de grafiekTekenEditor op 1 grafiek			
			grafiekPanel.getGrafiekTekenEditor().zetEenGrafiek(true);
			grafiekPanel.getGrafiekTekenEditor().zetAlleenPunten(true);
			grafiekPanel.getGrafiekTekenEditor().setFrozen(true);
			grafiekPanel.getGrafiekTekenEditor().removeAllPoints();
			
			grafiekPanel.zetKijkNaButton(true);
		}
		
//System.out.println("gep zetOpdrKeuze einde tc = " + grafiekPanel.getTabelComponent().geefTabelpunten().size());
		
	}

	// maak een docentFunctieEditor
	// de oude wordt verwijderd, bewaar de docentExpressie!
	private void maakDocentFunctieEditor(int x, int y, int w, int h)
	{	if (docentFunctieEditor != null)
		{	removeDocentFunctieEditor();
		}
		docentFunctieEditor = new FunctieEditor(true);
		docentFunctieEditor.setBounds(x, y, w, h);
		docentFunctieEditor.zetRandverhoging(false);
		opdrachtenPanel.add(docentFunctieEditor,0);
		docentFunctieEditor.zetFuncties();
		docentFunctieEditor.zetMaxAantalFuncties(1);
		docentFunctieEditor.addActionListener(this);

		docentFunctieEditor.zetFormalFunction(formFcCB.isSelected());		
		// checkbocx[1] aan en disabled??
		
		opdrachtenPanel.repaint();
	}

	private void removeDocentFunctieEditor()
	{	opdrachtenPanel.remove(docentFunctieEditor);
		opdrachtenPanel.repaint();
		grafiekPanel.getGrafiekComponent().zetDocentExpressie(null);		
		grafiekPanel.zetDocentExpressie(null);
		grafiekPanel.getFunctieEditor().zetMaxAantalFuncties(9);	
		// reset opdracht type PUNTENBIJFORMULE
		grafiekPanel.getGrafiekTekenEditor().zetEenGrafiek(false);			
		// deze hier bewaren
		// docentExpressie = null;
	}

	private void removeDocentGrafiekTekenEditor()
	{	opdrachtenPanel.remove(docentGrafiekTekenEditor);
		opdrachtenPanel.repaint();
		grafiekPanel.getGrafiekComponent().removeGrafiekTekenEditor();
		grafiekPanel.zetDocentGrafiekPunten(new Vector());
		// deze bewaren
		// docentGrafiekPunten.removeAllElements();
	}
	
	private void removeDocentTabelComponent()
	{	opdrachtenPanel.remove(docentTabelComponent);
		opdrachtenPanel.repaint();
		grafiekPanel.zetDocentTabelPunten(new Vector());
		grafiekPanel.getGrafiekComponent().removeTabelComponent();
				
		// tabelComponent links terugzetten naar defaults
		zoomInTabel = true;
		grafiekPanel.zetZoomInTabel(zoomInTabel);
		grafiekPanel.getTabelComponent().reset();


		// deze bewaren
		// docentTabelPunten.removeAllElements();
				
	}
	
	public Hashtable getEditState()
	{	
//System.out.println("gep getEditState");			
		// vrije versie
		String varNaam = "x";
		String yAsNaam = "y";

		boolean assen = true;
		boolean rooster = true;
		boolean roosterGrof = false;
		boolean schaal = true;
		boolean piLijnen = false;
		
		boolean grafiekTekenen = false;
		boolean tabelZichtbaar = false;
		boolean formulesZichtbaar = true;
		boolean grafiekKleuren = true;
		
		boolean zoomOptie = true;
		boolean traceOptie = true;
		boolean dragOptie = true;
		boolean buttonOptie = false;
		boolean formFc = true;

		boolean zoomInTabel = true;
		boolean tabelAlsTekenTool = false;
		
		boolean newVersion = false;
		boolean xPositief = false;
		boolean yPositief = false;
		boolean xVarEditable = false;
		boolean yVarEditable = false;
		
		varNaam = this.varNaam;
		yAsNaam = this.yAsNaam;
		
		assen = this.assen;
		rooster = this.rooster;
		roosterGrof = this.roosterGrof;
		schaal = this.schaal;
		piLijnen = this.piLijnen;
		
		grafiekTekenen = this.grafiekTekenen;
		tabelZichtbaar = this.tabelZichtbaar;
		formulesZichtbaar = this.formulesZichtbaar;
		grafiekKleuren = this.grafiekKleuren;
		
		newVersion = this.newVersion;
		xPositief = this.xPositief;
		yPositief = this.yPositief;
		xVarEditable = this.xVarEditable;
		yVarEditable = this.yVarEditable;
		
		zoomOptie = this.zoomOptie;
		traceOptie = this.traceOptie;
		dragOptie = this.dragOptie;
		buttonOptie = this.buttonOptie;
		formFc = this.formFc;
	
		zoomInTabel = this.zoomInTabel;
		tabelAlsTekenTool = this.tabelAlsTekenTool;
		
		Hashtable h = grafiekPanel.getEditState();
		
		h.put("varNaam", varNaam);
		h.put("yAsNaam", yAsNaam);
		
		h.put("assen", new Boolean(assen));
		h.put("rooster", new Boolean(rooster));
		h.put("roosterGrof", new Boolean(roosterGrof));
		h.put("schaal", new Boolean(schaal));
		h.put("piLijnen", new Boolean(piLijnen));
		

		h.put("zoomOptie", new Boolean(zoomOptie));
		h.put("traceOptie", new Boolean(traceOptie));
		h.put("dragOptie", new Boolean(dragOptie));
		h.put("buttonOptie", new Boolean(buttonOptie));
		h.put("formFc", new Boolean(formFc));
		h.put("grafiekKleuren", new Boolean(grafiekKleuren));
		

		if (typeOpdracht == GEENOPDRACHT)
		{	
//System.out.println("to == 0");			
			h.put("grafiekTekenen", new Boolean(grafiekTekenen));
			h.put("tabelZichtbaar", new Boolean(tabelZichtbaar));
			h.put("formulesZichtbaar", new Boolean(formulesZichtbaar));
			h.put("zoomInTabel", new Boolean(zoomInTabel));
			h.put("tabelAlsTekenTool", new Boolean(tabelAlsTekenTool));
		}
		else if (typeOpdracht == FORMULEBIJGRAFIEK)
		{
			h.put("grafiekTekenen", new Boolean(false));
			h.put("tabelZichtbaar", new Boolean(false));
			h.put("formulesZichtbaar", new Boolean(true));
			//h.put("zoomInTabel", new Boolean(zoomInTabel));
			//h.put("tabelAlsTekenTool", new Boolean(tabelAlsTekenTool));
		}
		else if (typeOpdracht == FORMULEBIJPUNTEN)
		{
			h.put("grafiekTekenen", new Boolean(false));
			h.put("tabelZichtbaar", new Boolean(false));
			h.put("formulesZichtbaar", new Boolean(true));
			//h.put("zoomInTabel", new Boolean(zoomInTabel));
			//h.put("tabelAlsTekenTool", new Boolean(tabelAlsTekenTool));
		}
		else if (typeOpdracht == PUNTENBIJFORMULE)
		{
			h.put("grafiekTekenen", new Boolean(true));
			h.put("tabelZichtbaar", new Boolean(false));
			h.put("formulesZichtbaar", new Boolean(false));
			//h.put("zoomInTabel", new Boolean(zoomInTabel));
			//h.put("tabelAlsTekenTool", new Boolean(tabelAlsTekenTool));
		}
		
		else if (typeOpdracht == TEKENTABELPUNTEN)
		{
			h.put("grafiekTekenen", new Boolean(true));
			h.put("tabelZichtbaar", new Boolean(leerlingZietTabel));
			h.put("formulesZichtbaar", new Boolean(false));
			h.put("zoomInTabel", new Boolean(false));
			h.put("tabelAlsTekenTool", new Boolean(true));
		}
		
		h.put("newVersion", new Boolean(newVersion));
		h.put("xPositief", new Boolean(xPositief));
		h.put("yPositief", new Boolean(yPositief));
		h.put("xVarEditable", new Boolean(xVarEditable));
		h.put("yVarEditable", new Boolean(yVarEditable));
		
		// opdrachten
		int typeOpdracht = GEENOPDRACHT;
		int nauwkeurigheid = 5;
		int minimumPunten = 5; 
		int scoreMax = 10;
		boolean leerlingZietTabel = true;	
		String docentExpressieString = "$f@";
		Vector docentGrafiekPunten = new Vector();
		Vector docentTabelPunten = new Vector();
		Vector docentTabelPuntVakIndex = new Vector();
		Vector docentTabelStringsX = new Vector();
		Vector docentTabelStringsY = new Vector();
		
		typeOpdracht = this.typeOpdracht;
		nauwkeurigheid = this.nauwkeurigheid;
		minimumPunten = this.minimumPunten;
		if(typeOpdracht == GEENOPDRACHT) scoreMax=0;
		else scoreMax = this.scoreMax;
		leerlingZietTabel = this.leerlingZietTabel;
		
		// deze is al in orde, zie actionPerformed()
		if (docentFunctieEditor != null)
		{	docentExpressieString = docentFunctieEditor.geefFormuleVak().toString();
		}
		//System.out.println("gep des = " + this.docentExpressieString);		
		docentGrafiekPunten = this.docentGrafiekPunten;
		docentTabelPunten = this.docentTabelPunten;
		docentTabelPuntVakIndex = this.docentTabelPuntVakIndex;
		docentTabelStringsX = this.docentTabelStringsX;
		docentTabelStringsY = this.docentTabelStringsY;
//System.out.println("get " + docentTabelPunten.size());		

		h.put("typeOpdracht", new Integer(typeOpdracht));
		h.put("nauwkeurigheid", new Integer(nauwkeurigheid));
		h.put("minimumPunten", new Integer(minimumPunten));
		h.put("scoreMax", new Integer(scoreMax));
		h.put("leerlingZietTabel", new Boolean(leerlingZietTabel));
		h.put("docentExpressieString", docentExpressieString);
		h.put("docentGrafiekPunten", docentGrafiekPunten);
		h.put("docentTabelPunten", docentTabelPunten);
		h.put("docentTabelPuntVakIndex", docentTabelPuntVakIndex);		
		h.put("docentTabelStringsX", docentTabelStringsX);
		h.put("docentTabelStringsY", docentTabelStringsY);
		
		return h;
	}
	
	public void setEditState(Hashtable h)
	{	
		
//System.out.println("gep setEditState");		
		// vrije versie
		
		String varNaam = "x";
		String yAsNaam = "y";

		boolean assen = true;
		boolean rooster = true;
		boolean roosterGrof = false;
		boolean schaal = true;
		boolean piLijnen = false;
		
		boolean grafiekTekenen = false;
		boolean tabelZichtbaar = false;
		boolean formulesZichtbaar = true;
		boolean grafiekKleuren = true;

		boolean zoomOptie = true;
		boolean traceOptie = true;
		boolean dragOptie = true;
		boolean buttonOptie = false;
		boolean formFc = true;

		boolean zoomInTabel = true;
		boolean tabelAlsTekenTool = false;
		
		boolean newVersion = false;
		boolean xPositief = false;
		boolean yPositief = false;
		boolean xVarEditable = false;
		boolean yVarEditable = false;
				
		if (h.containsKey("varNaam")) 
			varNaam = (String) h.get("varNaam");
		if (h.containsKey("yAsNaam")) 
			yAsNaam = (String) h.get("yAsNaam");
			
		if (h.containsKey("assen")) 
			assen = ((Boolean) h.get("assen")).booleanValue();
		if (h.containsKey("rooster")) 
			rooster = ((Boolean) h.get("rooster")).booleanValue();
		if (h.containsKey("roosterGrof")) 
			roosterGrof = ((Boolean) h.get("roosterGrof")).booleanValue();
		if (h.containsKey("schaal")) 
			schaal = ((Boolean) h.get("schaal")).booleanValue();
		if (h.containsKey("piLijnen")) 
			piLijnen = ((Boolean) h.get("piLijnen")).booleanValue();

		if (h.containsKey("grafiekTekenen")) 
			grafiekTekenen = ((Boolean) h.get("grafiekTekenen")).booleanValue();
		if (h.containsKey("tabelZichtbaar")) 
			tabelZichtbaar = ((Boolean) h.get("tabelZichtbaar")).booleanValue();
		if (h.containsKey("formulesZichtbaar")) 
			formulesZichtbaar = ((Boolean) h.get("formulesZichtbaar")).booleanValue();
		if (h.containsKey("grafiekKleuren")) 
			grafiekKleuren = ((Boolean) h.get("grafiekKleuren")).booleanValue();

		if (h.containsKey("zoomOptie")) 
			zoomOptie = ((Boolean) h.get("zoomOptie")).booleanValue();
		if (h.containsKey("traceOptie")) 
			traceOptie = ((Boolean) h.get("traceOptie")).booleanValue();
		if (h.containsKey("dragOptie")) 
			dragOptie = ((Boolean) h.get("dragOptie")).booleanValue();
		if (h.containsKey("buttonOptie")) 
			buttonOptie = ((Boolean) h.get("buttonOptie")).booleanValue();
		if (h.containsKey("formFc")) 
			formFc = ((Boolean) h.get("formFc")).booleanValue();

		if (h.containsKey("zoomInTabel")) 
			zoomInTabel = ((Boolean) h.get("zoomInTabel")).booleanValue();
		if (h.containsKey("tabelAlsTekenTool")) 
			tabelAlsTekenTool = ((Boolean) h.get("tabelAlsTekenTool")).booleanValue();
		
		if (h.containsKey("newVersion")) newVersion = ((Boolean) h.get("newVersion")).booleanValue();
		if (h.containsKey("xPositief")) xPositief = ((Boolean) h.get("xPositief")).booleanValue();
		if (h.containsKey("yPositief")) yPositief = ((Boolean) h.get("yPositief")).booleanValue();
		if (h.containsKey("xVarEditable")) xVarEditable = ((Boolean) h.get("xVarEditable")).booleanValue();
		if (h.containsKey("yVarEditable")) yVarEditable = ((Boolean) h.get("yVarEditable")).booleanValue();
		
			
		
		this.varNaam = varNaam;
		this.yAsNaam = yAsNaam;

		this.assen = assen;
		this.rooster = rooster;
		this.roosterGrof = roosterGrof;
		this.schaal = schaal;
		this.piLijnen = piLijnen;

		this.grafiekTekenen = grafiekTekenen;
		this.tabelZichtbaar = tabelZichtbaar;
		this.formulesZichtbaar = formulesZichtbaar;
		this.grafiekKleuren = grafiekKleuren;

		this.zoomOptie = zoomOptie;
		this.traceOptie = traceOptie;
		this.dragOptie = dragOptie;
		this.buttonOptie = buttonOptie;
		this.formFc = formFc;
		
		this.zoomInTabel = zoomInTabel;
		this.tabelAlsTekenTool = tabelAlsTekenTool;
		
		this.newVersion = newVersion;
		this.xPositief = xPositief;
		this.yPositief = yPositief;
		this.xVarEditable = xVarEditable;
		this.yVarEditable = yVarEditable;
			
		varNaamTF.setText(varNaam);
		yAsNaamTF.setText(yAsNaam);
		
		assenCB.setSelected(assen);
		roosterCB.setSelected(rooster);
		roosterGrofCB.setVisible(rooster);
		roosterGrofCB.setSelected(roosterGrof);
		schaalCB.setSelected(schaal);
		piLijnenCB.setSelected(piLijnen);
		
		grafiekTekenenCB.setSelected(grafiekTekenen);		
		tabelCB.setSelected(tabelZichtbaar);	
		tabelAlsTekenToolCB.setVisible(tabelZichtbaar);
		zoomInTabelCB.setVisible(tabelZichtbaar);
		formulesZichtbaarCB.setSelected(formulesZichtbaar);
		formFcCB.setVisible(formulesZichtbaar);
		grafiekKleurenCB.setSelected(grafiekKleuren);
		
		zoomOptieCB.setSelected(zoomOptie);
		traceOptieCB.setSelected(traceOptie);
		dragOptieCB.setSelected(dragOptie);
		buttonCB.setSelected(buttonOptie);
		formFcCB.setSelected(formFc);
		
		zoomInTabelCB.setSelected(zoomInTabel);
		tabelAlsTekenToolCB.setSelected(tabelAlsTekenTool);
		
		newVersionCB.setSelected(newVersion);
		xPositiefCB.setSelected(xPositief);
		yPositiefCB.setSelected(yPositief);
		xVarEditableCB.setSelected(xVarEditable);
		yVarEditableCB.setSelected(yVarEditable);
		
		xPositiefCB.setVisible(newVersion);
		yPositiefCB.setVisible(newVersion);
		xVarEditableCB.setVisible(newVersion);
		yVarEditableCB.setVisible(newVersion);
		
		grafiekPanel.zetYAsNaam(yAsNaam);
		grafiekPanel.zetAssen(assen);
		grafiekPanel.zetRooster(rooster);
		grafiekPanel.zetRoosterGrof(roosterGrof);
		grafiekPanel.zetSchaal(schaal);
		grafiekPanel.zetPiLijnen(piLijnen);
		
		grafiekPanel.zetGrafiekTekenen(grafiekTekenen);
		grafiekPanel.zetTabelZichtbaar(tabelZichtbaar);
		grafiekPanel.zetFormulesZichtbaar(formulesZichtbaar);
		grafiekPanel.zetGrafiekKleuren(grafiekKleuren);
		
		grafiekPanel.zetTraceOptie(traceOptie);
		grafiekPanel.zetZoomOptie(zoomOptie);
		grafiekPanel.zetDragOptie(dragOptie);
		grafiekPanel.zetFormalFunction(formFc);
		
		grafiekPanel.zetZoomInTabel(zoomInTabel);
		grafiekPanel.zetTabelAlsTekenTool(tabelAlsTekenTool);
		
		// opdrachten
		int typeOpdracht = GEENOPDRACHT;
		int scoreMax = 10;
		int nauwkeurigheid = 5;
		int minimumPunten = 5;
		boolean leerlingZietTabel = true;
		Expressie docentExpressie = null;
		String docentExpressieString = "$f@";
		Vector docentGrafiekPunten = new Vector();
		Vector docentTabelPunten = new Vector();
		Vector docentTabelPuntVakIndex = new Vector();		
		Vector docentTabelStringsX = new Vector();
		Vector docentTabelStringsY = new Vector();
		
		if (h.containsKey("typeOpdracht")) 
			typeOpdracht = ((Integer) h.get("typeOpdracht")).intValue();		
		if (h.containsKey("scoreMax")) 
			scoreMax = ((Integer) h.get("scoreMax")).intValue();		
		if (h.containsKey("docentExpressieString")) 
			docentExpressieString = (String) h.get("docentExpressieString");		
		if (h.containsKey("docentGrafiekPunten")) 
			docentGrafiekPunten = (Vector) h.get("docentGrafiekPunten");		
		if (h.containsKey("docentTabelPunten")) 
			docentTabelPunten = (Vector) h.get("docentTabelPunten");		
		if (h.containsKey("docentTabelPuntVakIndex")) 
			docentTabelPuntVakIndex = (Vector) h.get("docentTabelPuntVakIndex");		
		if (h.containsKey("docentTabelStringsX")) 
			docentTabelStringsX = (Vector) h.get("docentTabelStringsX");		
		if (h.containsKey("docentTabelStringsY")) 
			docentTabelStringsY = (Vector) h.get("docentTabelStringsY");		

		if (h.containsKey("nauwkeurigheid")) 
			nauwkeurigheid = ((Integer) h.get("nauwkeurigheid")).intValue();		
		if (h.containsKey("minimumPunten")) 
			minimumPunten = ((Integer) h.get("minimumPunten")).intValue();		
		if (h.containsKey("leerlingZietTabel")) 
			leerlingZietTabel = ((Boolean) h.get("leerlingZietTabel")).booleanValue();		
		
		
		this.typeOpdracht = typeOpdracht;
		this.scoreMax = scoreMax;	
		this.nauwkeurigheid = nauwkeurigheid;
		this.minimumPunten = minimumPunten; 
		this.leerlingZietTabel = leerlingZietTabel;
		this.docentExpressieString = docentExpressieString;
		if (!docentExpressieString.equals("$f@"))
			docentExpressie = FormuleParser.geefExpressie(docentExpressieString);
		// anders blijft docentExpressie null	
		this.docentExpressie = docentExpressie;			
		this.docentGrafiekPunten = docentGrafiekPunten;
		this.docentTabelPunten = docentTabelPunten;
		this.docentTabelPuntVakIndex = docentTabelPuntVakIndex;
		this.docentTabelStringsX = docentTabelStringsX;
		this.docentTabelStringsY = docentTabelStringsY;
		
//System.out.println("set " + docentTabelPunten.size());		
		//zetOpdrachtKeuze(typeOpdracht);
		nauwkeurigheidTF.setText("" + nauwkeurigheid);
		minimumPuntenTF.setText("" + minimumPunten);
		scoreMaxTF.setText("" + scoreMax);
		leerlingZietTabelBox.setSelected(leerlingZietTabel);
		
// dit kan weg		
		//grafiekPanel.zetTypeOpdracht(typeOpdracht);
		grafiekPanel.zetMaxScore(scoreMax);
		grafiekPanel.zetNauwkeurigheid(nauwkeurigheid);
		grafiekPanel.zetMinimumPunten(minimumPunten);
		grafiekPanel.zetDocentExpressie(docentExpressie);
		
		grafiekPanel.setEditState(h);
		grafiekPanel.setButton(false);
		
		// dit hier i.v.m. opdracht 3
		zetOpdrachtKeuze(typeOpdracht, true);
		
		if (typeOpdracht > 0)
			tabbedPane.setSelectedComponent(opdrachtenPanel);
	}
	
	public void setBounds(int x, int y, int b, int h)
	{	tabbedPane.setBounds(b-430, 10, 420, 510);
		super.setBounds(x,y,b,h);
	}
	
	public void zetBreedte(int b)
	{	grafiekPanel.setSize(b,grafiekPanel.getSize().height);
	}
	public void zetHoogte(int h)
	{	grafiekPanel.setSize(grafiekPanel.getSize().width, h);
	}
	
	public void wis(){}
    
	public void zetMode(int mode){}
	
    public void stop(){}
    
    public void start(){}

    public void processNauwkeurigheid()
    {	String msString = nauwkeurigheidTF.getText();
    	boolean error = false;
		int ms = 0;
		try
		{	ms = Integer.parseInt(msString);
		}
		catch (NumberFormatException nfe)
		{	error = true;
		}
		if (!error && (ms > 0) && (ms < 50))
		{	nauwkeurigheid = ms;
		}
		nauwkeurigheidTF.setText("" + nauwkeurigheid);
    }

    public void processMinimumPunten()
    {	String msString = minimumPuntenTF.getText();
    	boolean error = false;
		int ms = 0;
		try
		{	ms = Integer.parseInt(msString);
		}
		catch (NumberFormatException nfe)
		{	error = true;
		}
		if (!error && (ms > 0) && (ms < 50))
		{	minimumPunten = ms;
		}
		minimumPuntenTF.setText("" + minimumPunten);
    }
    
    public void processMaxScore()
    {	String msString = scoreMaxTF.getText();
    	boolean error = false;
		int ms = 0;
		try
		{	ms = Integer.parseInt(msString);
		}
		catch (NumberFormatException nfe)
		{	error = true;
		}
		if (!error && (ms >= 0))
		{	scoreMax = ms;
		}
		scoreMaxTF.setText("" + scoreMax);
    }
	
	public void actionPerformed(ActionEvent e)
	{	
		if(e.getSource().equals(assenCB))
		{	assen = assenCB.isSelected();
			grafiekPanel.zetAssen(assen);
		}
		if(e.getSource().equals(roosterCB))
		{	rooster = roosterCB.isSelected();
			grafiekPanel.zetRooster(rooster);
			roosterGrofCB.setVisible(rooster);
		}
		if(e.getSource().equals(roosterGrofCB))
		{	roosterGrof = roosterGrofCB.isSelected();
			grafiekPanel.zetRoosterGrof(roosterGrof);
		}
		if(e.getSource().equals(schaalCB))
		{	schaal = schaalCB.isSelected();
			grafiekPanel.zetSchaal(schaal);
		}
		if(e.getSource().equals(piLijnenCB))
		{	piLijnen = piLijnenCB.isSelected();
			grafiekPanel.zetPiLijnen(piLijnen);
		}
		if(e.getSource().equals(grafiekTekenenCB))
		{	grafiekTekenen = grafiekTekenenCB.isSelected();
			grafiekPanel.zetGrafiekTekenen(grafiekTekenen);
		}
		if(e.getSource().equals(tabelCB))
		{	tabelZichtbaar = tabelCB.isSelected();
			grafiekPanel.zetTabelZichtbaar(tabelZichtbaar);
			zoomInTabelCB.setVisible(tabelZichtbaar);
			tabelAlsTekenToolCB.setVisible(tabelZichtbaar);
			
		}
		if(e.getSource().equals(zoomInTabelCB))	
		{	zoomInTabel = zoomInTabelCB.isSelected();
			grafiekPanel.zetZoomInTabel(zoomInTabel);
		}	
		
		if(e.getSource().equals(tabelAlsTekenToolCB))	
		{	tabelAlsTekenTool = tabelAlsTekenToolCB.isSelected();
			if (tabelAlsTekenTool)
			{	zoomInTabelCB.setSelected(false);
				zoomInTabelCB.setEnabled(false);
				zoomInTabel = false;
				grafiekPanel.zetZoomInTabel(zoomInTabel);
			}
			else
			{	zoomInTabelCB.setEnabled(true);
			}
			grafiekPanel.zetTabelAlsTekenTool(tabelAlsTekenTool);
		}	
		
		if(e.getSource().equals(grafiekKleurenCB))
		{	grafiekKleuren = grafiekKleurenCB.isSelected();
			grafiekPanel.zetGrafiekKleuren(grafiekKleuren);
		}
		if(e.getSource().equals(formulesZichtbaarCB))
		{	formulesZichtbaar = formulesZichtbaarCB.isSelected();
			formFcCB.setVisible(formulesZichtbaar);
			grafiekPanel.zetFormulesZichtbaar(formulesZichtbaar);
		}
		if(e.getSource().equals(zoomOptieCB))
		{	zoomOptie = zoomOptieCB.isSelected();
			grafiekPanel.zetZoomOptie(zoomOptie);
		}
		if(e.getSource().equals(traceOptieCB))
		{	traceOptie = traceOptieCB.isSelected();
			grafiekPanel.zetTraceOptie(traceOptie);
		}
		if(e.getSource().equals(dragOptieCB))
		{	dragOptie = dragOptieCB.isSelected();
			grafiekPanel.zetDragOptie(dragOptie);
		}
		if(e.getSource().equals(buttonCB))
		{	buttonOptie = buttonCB.isSelected();
			
		}
		if(e.getSource().equals(formFcCB))
		{	formFc = formFcCB.isSelected();
			grafiekPanel.zetFormalFunction(formFc);
			if (docentFunctieEditor != null)
				docentFunctieEditor.zetFormalFunction(formFc);
		}
		
		if(e.getSource().equals(newVersionCB))
		{	newVersion = newVersionCB.isSelected();
			grafiekPanel.zetNewVersion(newVersion);
			xPositiefCB.setVisible(newVersion);
			yPositiefCB.setVisible(newVersion);
			xVarEditableCB.setVisible(newVersion);
			yVarEditableCB.setVisible(newVersion);
		}
		if(e.getSource().equals(xPositiefCB))
		{	xPositief = xPositiefCB.isSelected();
			grafiekPanel.zetXPositief(xPositief);
		}
		if(e.getSource().equals(yPositiefCB))
		{	yPositief = yPositiefCB.isSelected();
			grafiekPanel.zetYPositief(yPositief);
		}
		if(e.getSource().equals(xVarEditableCB))
		{	xVarEditable = xVarEditableCB.isSelected();
			grafiekPanel.zetXVarEditable(xVarEditable);
		}
		if(e.getSource().equals(yVarEditableCB))
		{	yVarEditable = yVarEditableCB.isSelected();
			grafiekPanel.zetYVarEditable(yVarEditable);
		}
		
		if (e.getSource().equals(nauwkeurigheidTF))
		{	processNauwkeurigheid();
		}
		if (e.getSource().equals(minimumPuntenTF))
		{	processMinimumPunten();
		}
		
		// dit later nog netter met een KeyListener, zie class TabelVak
		if (e.getSource().equals(scoreMaxTF))
		{	processMaxScore();
		}
		
		if (e.getSource().equals(leerlingZietTabelBox))
		{	leerlingZietTabel = leerlingZietTabelBox.isSelected();
			grafiekPanel.zetTabelZichtbaar(leerlingZietTabel);
			
		}
		
		if (e.getSource().equals(docentFunctieEditor) //&&
			//e.getActionCommand().equals("ingevuld")
		   )
		{	if (e.getActionCommand().equals("ingevuld") ||
				e.getActionCommand().equals("focusLost"))
				//&&(docentFunctieEditor.geefLaatsteExp() != null))
			{	docentExpressieString = docentFunctieEditor.geefFormuleVak().toString();
				docentExpressie = docentFunctieEditor.geefLaatsteExp();
				//docentExpressieString = "$f" + docentExpressie.toString() + "@";			
			}
			else if (e.getActionCommand().equals("verwijderd"))	
			{	docentExpressie = null;
				docentExpressieString = "$f@";
			}
//System.out.println("des = " + docentExpressieString);			
			if (typeOpdracht == FORMULEBIJGRAFIEK)
				grafiekPanel.getGrafiekComponent().zetDocentExpressie(docentExpressie);
			grafiekPanel.zetDocentExpressie(docentExpressie);
			
			if (typeOpdracht == FORMULEBIJPUNTEN)
			{	if (docentExpressie == null)
				{
					for (int rCnt = 0; rCnt < docentTabelPunten.size(); rCnt++)
					{
						RealPoint rp = (RealPoint) docentTabelPunten.elementAt(rCnt);
						rp.y = 0;
					}
					
				}
				else
				{
					for (int rCnt = 0; rCnt < docentTabelPunten.size(); rCnt++)
					{
						RealPoint rp = (RealPoint) docentTabelPunten.elementAt(rCnt);
						rp.y = docentExpressie.geefWaarde(rp.x);
					}

				}
				grafiekPanel.getGrafiekComponent().repaint();
				
			}
		}	

		if (e.getSource().equals(docentGrafiekTekenEditor) &&
			e.getActionCommand().equals("points changed"))
		{	docentGrafiekPunten = docentGrafiekTekenEditor.getPoints(1);
			grafiekPanel.zetDocentGrafiekPunten(docentGrafiekPunten);
		}	
		
		if (e.getSource().equals(docentTabelComponent) //&&
			//e.getActionCommand().equals("points changed")
		   )
		{	if (e.getActionCommand().equals("points changed"))
			{
				docentTabelPunten = docentTabelComponent.geefTabelpunten();
				docentTabelPuntVakIndex = docentTabelComponent.geefTabelPuntVakIndex();
//System.out.println("dtpvi = " + docentTabelPuntVakIndex.size());				
				grafiekPanel.zetDocentTabelPuntVakIndex(docentTabelPuntVakIndex);
				grafiekPanel.zetDocentTabelPunten(docentTabelPunten);

				if ((typeOpdracht == TEKENTABELPUNTEN) || (typeOpdracht == FORMULEBIJPUNTEN))
				{
					docentTabelStringsX = docentTabelComponent.geefTabelStringsX();
					docentTabelStringsY = docentTabelComponent.geefTabelStringsY();
					grafiekPanel.zetDocentTabelStringsY(docentTabelStringsX);
					grafiekPanel.zetDocentTabelStringsY(docentTabelStringsY);
					grafiekPanel.getTabelComponent().zetTabelStringsX(docentTabelStringsX);
					grafiekPanel.getTabelComponent().zetTabelStringsY(docentTabelStringsY);
					grafiekPanel.getTabelComponent().zetDocentTabelPuntVakIndex(docentTabelPuntVakIndex);
System.out.println("links");					
					grafiekPanel.getTabelComponent().zetDocentTabelPunten(docentTabelPunten, true);
					grafiekPanel.getTabelComponent().zetFirstIndexVisible(docentTabelComponent.getFirstIndexVisible());
				}	
				
			}
			else if (e.getActionCommand().equals("pijl links"))
			{	if (typeOpdracht == TEKENTABELPUNTEN)
				{	grafiekPanel.getTabelComponent().pijlRechtsAction("leerling");
				//grafiekPanel.getTabelComponent().zetTabelStringsX(docentTabelStringsX);
				//grafiekPanel.getTabelComponent().zetTabelStringsY(docentTabelStringsY);
				grafiekPanel.getTabelComponent().zetDocentTabelPuntVakIndex(docentTabelPuntVakIndex);
				grafiekPanel.getTabelComponent().zetDocentTabelPunten(docentTabelPunten, true);
				grafiekPanel.getTabelComponent().zetFirstIndexVisible(docentTabelComponent.getFirstIndexVisible());
				}
			}	
			else if (e.getActionCommand().equals("pijl rechts"))
			{	if (typeOpdracht == TEKENTABELPUNTEN)
				{	grafiekPanel.getTabelComponent().pijlLinksAction();
				//grafiekPanel.getTabelComponent().zetTabelStringsX(docentTabelStringsX);
				//grafiekPanel.getTabelComponent().zetTabelStringsY(docentTabelStringsY);
				//grafiekPanel.getTabelComponent().zetDocentTabelPuntVakIndex(docentTabelPuntVakIndex);
				//grafiekPanel.getTabelComponent().zetDocentTabelPunten(docentTabelPunten, true);
				//grafiekPanel.getTabelComponent().zetFirstIndexVisible(docentTabelComponent.getFirstIndexVisible());
				}
			}	
			
			if (typeOpdracht == FORMULEBIJPUNTEN)
			{	
				docentExpressieString = docentFunctieEditor.geefFormuleVak().toString();
				if (!docentExpressieString.equals("$f@"))
					docentExpressie = FormuleParser.geefExpressie(docentExpressieString);
				if (docentExpressie == null)
				{
					for (int rCnt = 0; rCnt < docentTabelPunten.size(); rCnt++)
					{	RealPoint rp = (RealPoint) docentTabelPunten.elementAt(rCnt);
						rp.y = 0;
					}

				}
				else
				{
					for (int rCnt = 0; rCnt < docentTabelPunten.size(); rCnt++)
					{	RealPoint rp = (RealPoint) docentTabelPunten.elementAt(rCnt);
						rp.y = docentExpressie.geefWaarde(rp.x);
					}
				}
				
				grafiekPanel.getGrafiekComponent().repaint();
			}
			
			
		}	

		
		
	} // actionPerformed
	
	public void focusGained(FocusEvent e)
	{}
	
	public void focusLost(FocusEvent e)
	{	processNauwkeurigheid();
		processMinimumPunten();
		processMaxScore();
	}
	
	public void zetTabletUser(FormuleVakHouder formuleVakHouder)
	{	if(tablet==null) return;
		tablet.zetFormuleVakHouder(formuleVakHouder);
		tabletUser = formuleVakHouder;
		
	}
	
	public void zetTablet(FormuleVakHouder formuleVakHouder, int x, int y)
	{	if(tablet==null) 
		{	tablet = new Tablet(formuleVakHouder);
			tablet.setLocation(x,y);
			
		}
		tablet.zetFormuleVakHouder(formuleVakHouder);
		tabletUser = formuleVakHouder;
		
		
	}
	
	public void addTablet(FormuleVakHouder formuleVakHouder, int x, int y)
	{	if(tablet==null) 
		{	tablet = new Tablet(formuleVakHouder);
			
			
		}
		if(!tabletAdded)
		{	add(tablet,0);
			tablet.setLocation(x,y);
			tabletAdded = true;
			//resize();
            repaint();
		}
		tablet.zetFormuleVakHouder(formuleVakHouder);
	}
	
	public void removeTablet()
	{	if(tablet==null)return;
        remove(tablet);
        //resize();
        repaint();
		tabletAdded = false;
	}
	
	public Tablet getTablet()
	{	return tablet;
	}
	
	//ActionProducer
	private ActionListener actionListener = null;
	
	public void addActionListener(ActionListener l) 
 	{	actionListener = AWTEventMulticaster.add(actionListener,l);
 	}
 	
 	public void removeActionListener(ActionListener l)
 	{	actionListener = AWTEventMulticaster.remove(actionListener, l);
 	}	
 	
 	public void produceAction(String command)
 	{	if (actionListener != null)
 		{	actionListener.actionPerformed( new ActionEvent(this, 0, command) );
 		}
 	}
 	//end ActionProducer
	

	// inner class voor opdrachtKeuze
	class OpdrachtKeuzeAL implements ActionListener
	{	public void actionPerformed(ActionEvent e)
		{	
			if (opdrachtKeuzeAllowed)
			{	
				int index = opdrachtKeuze.getSelectedIndex();
				zetOpdrachtKeuze(index, false);
			}	
		}
	}
	
}
