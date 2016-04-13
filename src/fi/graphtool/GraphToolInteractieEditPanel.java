package fi.graphtool;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.expressies.Expressie;
import fi.wiskopdr.formuleobjects.FormuleParser;



public class GraphToolInteractieEditPanel extends JPanel implements InteractieEditPanel, ActionListener, FocusListener {

	private GraphToolInteractiePanel interactiePanel;
	private JPanel optionsPanel;
	public static final boolean cDefault_TraceOption = false;
	
	private int defaultWidth = 800; 
	private int defaultIpHeight = 300;
	private int defaultIpWidth = 300; //hier stond 270
//	private int defaultOpWidth = 470;
	private int defaultOpWidth = 560;
	private int defaultOpHeight= 560;
//	private int defaultOpHeight= 490;
	
	private Font theFont;
	private FontMetrics theFM;
	private Font theBoldFont;
	private FontMetrics theBoldFM;
	
	double asDefXMin;
	double asDefXMax;
	double asDefXStap;
	double asDefYMin;
	double asDefYMax;
	double asDefYStap;

	private int offset = 4;
	private int currentX;
	private int currentY;
	private int width;
	private int height;
	
	//Tools-panel
	private JCheckBox formuleComponentCB, veldComponentCB, tekenComponentCB, tabelComponentCB,
	  assenZichtbaarCB, roosterZichtbaarCB, roosterXCB, roosterYCB, roosterGrofCB, schaalZichtbaarCB, schaalXCB, schaalYCB,
	  piLijnenZichtbaarCB, zoomOptieCB, traceOptieCB, dragOptieCB, 
	  zoomInTabelCB, tabelAlsTekenToolCB, xPositiefCB, 
	  yPositiefCB, xAsLogCB, yAsLogCB, xVarEditableCB, yVarEditableCB, 
	  snapToGridPointsCB, krommeZonderExtrapolatieCB, krommeMetExtrapolatieCB; 
	private FormuleEditorOptiesButton formuleEditorOptiesButton;
	private VeldEditorOptiesButton veldEditorOptiesButton;
	
	private JLabel xAsLabel, yAsLabel, tekenGrafiekNauwkeurigheidLabel;
	private JTextField xAsNaamTF, yAsNaamTF, tekenGrafiekNauwkeurigheidTF;

	private String xAsNaam = "x";
	private String yAsNaam = "y";
	private int tekenGrafiekNauwkeurigheid = 5;

	private boolean formuleComponentAan, veldComponentAan, tekenComponentAan, tabelComponentAan,
		assenZichtbaar, roosterZichtbaar, roosterX, roosterY, roosterGrof, schaalZichtbaar, schaalX, schaalY,
		piLijnenZichtbaar, zoomOptie, traceOptie, dragOptie, formeleFuncties, 
		zoomInTabel, tabelAlsTekenTool, xPositief, yPositief, xAsLog, yAsLog,
		xVarEditable, yVarEditable, 
		snapToGridPoints, rechteVerbindingen, krommeZonderExtrapolatie, krommeMetExtrapolatie,
		manualScaling,
		manualScalingX, manualScalingY;
	
	// Assen-definitie ui variabelen
	private JLabel assenDefLabel;
	private JLabel asDefXLabel, asDefXMinLabel, asDefXMaxLabel, asDefXStapLabel;
	private JTextField asDefXMinTF, asDefXMaxTF, asDefXStapTF;
	private JLabel asDefYLabel, asDefYMinLabel, asDefYMaxLabel, asDefYStapLabel;
	private JTextField asDefYMinTF, asDefYMaxTF, asDefYStapTF;
	
	private JCheckBox asManualDefCB;
	
	//Opdrachtenpanel 
	protected static int GEENOPDRACHT = 0;
	protected static int VINDFORMULEBIJGRAFIEK = 1;
	protected static int VINDFORMULEBIJPUNTEN = 2;
	protected static int TEKENPUNTENBIJFORMULE = 3;
	protected static int TEKENTABELPUNTEN = 4;
	
	private int typeOpdracht;
	private JComboBox opdrachtKeuze;
	private boolean opdrachtKeuzeAllowed = true;
	private int[] maxScores = new int[9];
	private JLabel[] scoreMaxLabel;
	private JTextField[] scoreMaxTF;	
	
	private JLabel explanLabel1, explanLabel2;
	
	private JLabel nauwkeurigheidLabel;
	private JTextField[] nauwkeurigheidTF;
	private JLabel minimumPuntenLabel;
	private JTextField[] minimumPuntenTF;
	int[] nauwkeurigheid = new int[3];
	int[] minimumPunten = new int[3];
	
	private JCheckBox domeinControlerenBox;
	boolean domeinControleren = false;
	private JCheckBox leerlingZietTabelBox; 
	boolean leerlingZietTabel = true;
	private boolean checkExternal = false;
	
	private JCheckBox tekenRechteCB, tekenMetExtrapolatieCB, tekenZonderExtrapolatieCB;
	private JCheckBox checkExternalCB;
	
	private FormuleComponent docentFormuleComponent;
	private TabelComponent docentTabelComponent;
	
	private int maxAantalExpressies;
	private Expressie[] docentFuncties;	
	private String[] docentFunctieStrings;
	
	private JPanel toolsPanel, opdrachtenPanel, schuifParameterPanel; 
	private JTabbedPane tabbedPane;
	
	private double beginxDocent, beginyDocent;
	private double docentSchaalFactorX, docentSchaalFactorY;
	
	private SchuifParameterInstellingenButton paramVoegToeButton, paramWijzigButton;
	private JButton paramVerwijderButton;
	private JList schuifParamList;
	private DefaultListModel schuifParamListElements;
	//private JTextField paramNaamTF, paramOnderGrensTF, paramBovenGrensTF, paramBeginStandTF, paramLengteTF;	
	
	public GraphToolInteractieEditPanel() {
		setLayout(null);
		interactiePanel = new GraphToolInteractiePanel();
		interactiePanel.setBounds(10,20,defaultIpWidth,defaultIpHeight);
		add(interactiePanel);
		
		// Set Scaling defaults
		asDefXMin = interactiePanel.asDefaultXMin;
		asDefXMax = interactiePanel.asDefaultXMax;
		asDefXStap = interactiePanel.asDefaultXStap;
		asDefYMin = interactiePanel.asDefaultYMin;
		asDefYMax = interactiePanel.asDefaultYMax;
		asDefYStap = interactiePanel.asDefaultYStap;
		
		optionsPanel = new JPanel();
		optionsPanel.setLayout(null);
		optionsPanel.setBounds(defaultIpWidth+10,20,defaultOpWidth,defaultOpHeight);
		add(optionsPanel);
		
		tabbedPane = new JTabbedPane();
		tabbedPane.setBackground(getBackground());				
		tabbedPane.setBounds(0, 0, defaultOpWidth, defaultOpHeight);	
		tabbedPane.setOpaque(false);
		optionsPanel.add(tabbedPane);
		
		toolsPanel = new JPanel();
		toolsPanel.setLayout(null);
		toolsPanel.setBackground(getBackground());		
		toolsPanel.setPreferredSize(new Dimension(defaultOpWidth, defaultOpHeight - 30));
		tabbedPane.add("Tools", toolsPanel);
		
		opdrachtenPanel = new JPanel();
		opdrachtenPanel.setLayout(null);	
		opdrachtenPanel.setBackground(getBackground());		
		opdrachtenPanel.setPreferredSize(new Dimension(defaultOpWidth, defaultOpHeight - 30));
		tabbedPane.add(GraphTool.rb.getString("GTIEP_opdrachten"), opdrachtenPanel);
		
		schuifParameterPanel = new JPanel();
		schuifParameterPanel.setLayout(null);
		schuifParameterPanel.setBackground(getBackground());		
		schuifParameterPanel.setPreferredSize(new Dimension(defaultOpWidth, defaultOpHeight - 30));
		tabbedPane.add(GraphTool.rb.getString("GTIEP_schuifparameters"), schuifParameterPanel);
		
		theFont = new Font("SansSerif",Font.PLAIN,12);
		theBoldFont = new Font("SansSerif", Font.BOLD, 12);
		theFM = getFontMetrics(theFont);
		theBoldFM = getFontMetrics(theBoldFont);
		
		width = defaultOpWidth/3 - 2 * offset;
		height = 3 * theFM.getHeight() / 2;
		currentX = 2 * offset;
		currentY = offset;
			
		//Defaults voor het tools-panel
		formuleComponentAan = true;
		veldComponentAan = false;
		tekenComponentAan = false;
		tabelComponentAan = false;
		assenZichtbaar = true;
		roosterZichtbaar = true;
		roosterX = true;
		roosterY = true;
		roosterGrof = false; 
		schaalZichtbaar = true; 
		schaalX = true;
		schaalY = true;
		piLijnenZichtbaar = false; 
		zoomOptie = true; 
		traceOptie = cDefault_TraceOption; 
		dragOptie = true; 
		formeleFuncties = true;
		zoomInTabel = true;
		tabelAlsTekenTool = false; 
		xPositief = false; 
		yPositief = false; 
		xAsLog = false;
		yAsLog = false;
		xVarEditable = false;
		yVarEditable = false;
		snapToGridPoints = false;
		rechteVerbindingen = false;
		krommeZonderExtrapolatie = true;
		krommeMetExtrapolatie = true;
		
		JLabel variabelenLabel = new JLabel(GraphTool.rb.getString("GTIEP_varLabel"));
		variabelenLabel.setBounds(currentX, currentY, width, height);
		toolsPanel.add(variabelenLabel);
		
		currentY += height + 2 * offset;
		
		xAsLabel = new JLabel(GraphTool.rb.getString("GTIEP_varXLabel"));
		xAsLabel.setBounds(currentX, currentY, 3*width/5, height);
		xAsLabel.setFont(theFont);
		toolsPanel.add(xAsLabel);
		
		currentX += xAsLabel.getWidth() + offset;
		
		xAsNaamTF = new JTextField(xAsNaam);
		xAsNaamTF.setBounds(currentX, currentY, 50, height);
		xAsNaamTF.setFont(theFont);
		toolsPanel.add(xAsNaamTF);
		xAsNaamTF.addKeyListener(new KeyAdapter()
		{	public void keyReleased(KeyEvent e)
			{	String[] forbiddenStrings = {"sin","cos","tan","ln","log"};
				String text = xAsNaamTF.getText().trim();
				
				
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
			{	xAsNaam = s;
				if(updateTF) xAsNaamTF.setText(xAsNaam);
				if(xAsNaam.equals(""))xAsNaam = "x";
				interactiePanel.zetXAsNaam(xAsNaam, false);
				docentFormuleComponent.zetXAsNaam(xAsNaam, false);
				docentTabelComponent.zetXAsNaam(xAsNaam);
				xAsNaamTF.requestFocus();
			}
		});
		
		currentX += xAsNaamTF.getWidth() + 10 * offset;
		
		xVarEditableCB = maakCheckBox(GraphTool.rb.getString("GTIEP_varEditable"), currentX, currentY, 3*width/2 , height, xVarEditable, toolsPanel);
		
		currentY += height + offset;
		currentX -= xAsLabel.getWidth() + xAsNaamTF.getWidth() + 11 * offset;
		
		yAsLabel = new JLabel(GraphTool.rb.getString("GTIEP_varYLabel"));
		yAsLabel.setBounds(currentX, currentY, 3*width/5, height);
		yAsLabel.setFont(theFont);
		toolsPanel.add(yAsLabel);
		
		currentX += yAsLabel.getWidth() + offset;
		
		yAsNaamTF = new JTextField(yAsNaam);
		yAsNaamTF.setBounds(currentX, currentY, 50, height);
		yAsNaamTF.setFont(theFont);
		toolsPanel.add(yAsNaamTF);
		yAsNaamTF.addKeyListener(new KeyAdapter() 
		{	public void keyReleased(KeyEvent e)
			{	yAsNaam = yAsNaamTF.getText().trim();
				yAsNaamTF.setText(yAsNaam);
				if(yAsNaam.equals(""))yAsNaam = "y";
				interactiePanel.zetYAsNaam(yAsNaam, false);
				docentFormuleComponent.zetYAsNaam(yAsNaam, false);
				docentTabelComponent.zetYAsNaam(yAsNaam, true);
				yAsNaamTF.requestFocus();
			}
		});
		
		currentX += yAsNaamTF.getWidth() + 10 * offset;
		
		yVarEditableCB = maakCheckBox(GraphTool.rb.getString("GTIEP_varEditable"), currentX, currentY, 3*width/2 , height, yVarEditable, toolsPanel);
		
		currentX -= yAsLabel.getWidth() + yAsNaamTF.getWidth() + 11 * offset;
		currentY += height + 2 * offset;
		
		// RPJ Schaal-definitie gedeelte
		asManualDefCB = maakCheckBox(GraphTool.rb.getString("GTIEP_schaalDefCB"), currentX, currentY, 2*width, height, manualScaling, toolsPanel);

		currentY += height + offset; 
		asDefXLabel = new JLabel(GraphTool.rb.getString("GTIEP_schaalXLabel"));
		asDefXLabel.setBounds(currentX, currentY, 3*width/5, height);
		asDefXLabel.setFont(theFont);
		toolsPanel.add(asDefXLabel);
		
		currentX += asDefXLabel.getWidth() + 1 * offset;
		asDefXMinLabel = new JLabel(GraphTool.rb.getString("GTIEP_schaalMinLabel"));
		asDefXMinLabel.setBounds(currentX, currentY, width /6, height);
		asDefXMinLabel.setFont(theFont);
		toolsPanel.add(asDefXMinLabel);

		currentX += asDefXMinLabel.getWidth() + 1 * offset;
		asDefXMinTF = new JTextField(String.valueOf(asDefXMin));
		asDefXMinTF.setEnabled(manualScalingX); // also enable/disable the input field according to its variable
		asDefXMinTF.setBounds(currentX, currentY, 50, height);
		asDefXMinTF.setFont(theFont);
		asDefXMinTF.setHorizontalAlignment(JTextField.RIGHT);
		asDefXMinTF.addActionListener(this);
		asDefXMinTF.addFocusListener(this);
		toolsPanel.add(asDefXMinTF);

		currentX += asDefXMinTF.getWidth() + 5 * offset;
		asDefXMaxLabel = new JLabel(GraphTool.rb.getString("GTIEP_schaalMaxLabel"));
		asDefXMaxLabel.setBounds(currentX, currentY, width /6, height);
		asDefXMaxLabel.setFont(theFont);
		toolsPanel.add(asDefXMaxLabel);

		currentX += asDefXMaxLabel.getWidth() + 1 * offset;
		asDefXMaxTF = new JTextField(String.valueOf(asDefXMax));
		asDefXMaxTF.setEnabled(manualScalingX); // also enable/disable the input field according to its variable
		asDefXMaxTF.setBounds(currentX, currentY, 50, height);
		asDefXMaxTF.setFont(theFont);
		asDefXMaxTF.setHorizontalAlignment(JTextField.RIGHT);
		asDefXMaxTF.addActionListener(this);
		asDefXMaxTF.addFocusListener(this);
		toolsPanel.add(asDefXMaxTF);

		currentX += asDefXMaxTF.getWidth() + 5 * offset;
		asDefXStapLabel = new JLabel(GraphTool.rb.getString("GTIEP_schaalStapLabel"));
		asDefXStapLabel.setBounds(currentX, currentY, width /6, height);
		asDefXStapLabel.setFont(theFont);
		toolsPanel.add(asDefXStapLabel);

		currentX += asDefXStapLabel.getWidth() + 1 * offset;
		asDefXStapTF = new JTextField(String.valueOf(asDefXStap));
		asDefXStapTF.setEnabled(manualScalingX); // also enable/disable the input field according to its variable
		asDefXStapTF.setBounds(currentX, currentY, 50, height);
		asDefXStapTF.setFont(theFont);
		asDefXStapTF.setHorizontalAlignment(JTextField.RIGHT);
		asDefXStapTF.addActionListener(this);
		asDefXStapTF.addFocusListener(this);
		toolsPanel.add(asDefXStapTF);

		currentX -= asDefXLabel.getWidth() + 
					asDefXMinLabel.getWidth() + asDefXMinTF.getWidth() + 
					asDefXMaxLabel.getWidth() + asDefXMaxTF.getWidth() +
					asDefXStapLabel.getWidth() +  
				    14 * offset;

		currentY += height + offset;
		asDefYLabel = new JLabel(GraphTool.rb.getString("GTIEP_schaalYLabel"));
		asDefYLabel.setBounds(currentX, currentY, 3*width/5, height);
		asDefYLabel.setFont(theFont);
		toolsPanel.add(asDefYLabel);
		
		currentX += asDefYLabel.getWidth() + 1 * offset;
		asDefYMinLabel = new JLabel(GraphTool.rb.getString("GTIEP_schaalMinLabel"));
		asDefYMinLabel.setBounds(currentX, currentY, width /6, height);
		asDefYMinLabel.setFont(theFont);
		toolsPanel.add(asDefYMinLabel);

		currentX += asDefYMinLabel.getWidth() + 1 * offset;
		asDefYMinTF = new JTextField(String.valueOf(asDefYMin));
		asDefYMinTF.setEnabled(manualScalingY); // also enable/disable the input field according to its variable
		asDefYMinTF.setBounds(currentX, currentY, 50, height);
		asDefYMinTF.setFont(theFont);
		asDefYMinTF.setHorizontalAlignment(JTextField.RIGHT);
		asDefYMinTF.addActionListener(this);
		asDefYMinTF.addFocusListener(this);
		toolsPanel.add(asDefYMinTF);

		currentX += asDefYMinTF.getWidth() + 5 * offset;
		asDefYMaxLabel = new JLabel(GraphTool.rb.getString("GTIEP_schaalMaxLabel"));
		asDefYMaxLabel.setBounds(currentX, currentY, width /6, height);
		asDefYMaxLabel.setFont(theFont);
		toolsPanel.add(asDefYMaxLabel);

		currentX += asDefYMaxLabel.getWidth() + 1 * offset;
		asDefYMaxTF = new JTextField(String.valueOf(asDefYMax));
		asDefYMaxTF.setEnabled(manualScalingY); // also enable/disable the input field according to its variable
		asDefYMaxTF.setBounds(currentX, currentY, 50, height);
		asDefYMaxTF.setFont(theFont);
		asDefYMaxTF.setHorizontalAlignment(JTextField.RIGHT);
		asDefYMaxTF.addActionListener(this);
		asDefYMaxTF.addFocusListener(this);
		toolsPanel.add(asDefYMaxTF);
		
		currentX += asDefYMaxTF.getWidth() + 5 * offset;
		asDefYStapLabel = new JLabel(GraphTool.rb.getString("GTIEP_schaalStapLabel"));
		asDefYStapLabel.setBounds(currentX, currentY, width /6, height);
		asDefYStapLabel.setFont(theFont);
		toolsPanel.add(asDefYStapLabel);

		currentX += asDefYStapLabel.getWidth() + 1 * offset;
		asDefYStapTF = new JTextField(String.valueOf(asDefYStap));
		asDefYStapTF.setEnabled(manualScalingY); // also enable/disable the input field according to its variable
		asDefYStapTF.setBounds(currentX, currentY, 50, height);
		asDefYStapTF.setFont(theFont);
		asDefYStapTF.setHorizontalAlignment(JTextField.RIGHT);
		asDefYStapTF.addActionListener(this);
		asDefYStapTF.addFocusListener(this);
		toolsPanel.add(asDefYStapTF);

		currentX -= asDefYLabel.getWidth() + 
					asDefYMinLabel.getWidth() + asDefYMinTF.getWidth() + 
					asDefYMaxLabel.getWidth() + asDefYMaxTF.getWidth() +
					asDefYStapLabel.getWidth() +  
				    14 * offset;
		
		currentY += height + 2 * offset;
		
		JLabel assenstelselLabel = new JLabel(GraphTool.rb.getString("GTIEP_assenLabel"));
		assenstelselLabel.setBounds(currentX, currentY, 2 * width, height);
		toolsPanel.add(assenstelselLabel);
		
		currentY += height + 2 * offset;
		
		assenZichtbaarCB = maakCheckBox(GraphTool.rb.getString("GTIEP_assenZichtbaar"), currentX, currentY, 2*width, height, assenZichtbaar, toolsPanel);
		currentX += assenZichtbaarCB.getWidth() + 2 * offset;
		xPositiefCB = maakCheckBox(GraphTool.rb.getString("GTIEP_xPositief"), currentX, currentY, width, height, xPositief, toolsPanel);
		currentX -= assenZichtbaarCB.getWidth() + 2 * offset;
		currentY += height + offset;
		schaalZichtbaarCB = maakCheckBox(GraphTool.rb.getString("GTIEP_schaalZichtbaar"), currentX, currentY, width, height, schaalZichtbaar, toolsPanel);
		currentX += schaalZichtbaarCB.getWidth() + 2 * offset;
		schaalXCB = maakCheckBox(GraphTool.rb.getString("GTIEP_xAs"), currentX, currentY, width/2-offset, height, schaalX, toolsPanel);
		currentX += schaalXCB.getWidth() + offset;
		schaalYCB = maakCheckBox(GraphTool.rb.getString("GTIEP_yAs"), currentX, currentY, width/2-offset, height, schaalY, toolsPanel);
		currentX += schaalYCB.getWidth() + offset;
		yPositiefCB = maakCheckBox(GraphTool.rb.getString("GTIEP_yPositief"), currentX, currentY, width, height, yPositief, toolsPanel);
		currentX -= schaalZichtbaarCB.getWidth() + schaalXCB.getWidth() + schaalYCB.getWidth() +  4 * offset;
		currentY += height + offset;
		roosterZichtbaarCB = maakCheckBox(GraphTool.rb.getString("GTIEP_roosterZichtbaar"), currentX, currentY, width/2, height, roosterZichtbaar, toolsPanel); 
		currentX += roosterZichtbaarCB.getWidth() + offset;
		roosterGrofCB = maakCheckBox(GraphTool.rb.getString("GTIEP_roosterGrof"), currentX, currentY, width/2, height, roosterGrof, toolsPanel); 
		currentX += roosterGrofCB.getWidth() + offset;
		roosterXCB = maakCheckBox(GraphTool.rb.getString("GTIEP_xAs"), currentX, currentY, width/2-offset, height, roosterX, toolsPanel);
		currentX += schaalXCB.getWidth() + offset;
		roosterYCB = maakCheckBox(GraphTool.rb.getString("GTIEP_yAs"), currentX, currentY, width/2-offset, height, roosterY, toolsPanel);
		currentX += schaalYCB.getWidth() +  offset;
		xAsLogCB = maakCheckBox(GraphTool.rb.getString("GTIEP_xAsLog"), currentX, currentY, width, height, xAsLog, toolsPanel);
		currentX -= roosterZichtbaarCB.getWidth() + roosterGrofCB.getWidth() + roosterXCB.getWidth() + roosterYCB.getWidth() + 4 * offset;
		currentY += height + offset;
		piLijnenZichtbaarCB = maakCheckBox(GraphTool.rb.getString("GTIEP_piLijnenZichtbaar"), currentX, currentY, 2*width, height, piLijnenZichtbaar, toolsPanel);
		currentX += piLijnenZichtbaarCB.getWidth() + 2 * offset;
		yAsLogCB = maakCheckBox(GraphTool.rb.getString("GTIEP_yAsLog"), currentX, currentY, width, height, yAsLog, toolsPanel);
		currentX -= piLijnenZichtbaarCB.getWidth() + 2 * offset;
		
		currentY += height + 2 * offset;
		
		JLabel grafiekOptiesLabel = new JLabel(GraphTool.rb.getString("GTIEP_grafiekOptiesLabel"));
		grafiekOptiesLabel.setBounds(currentX, currentY, width, height);
		toolsPanel.add(grafiekOptiesLabel);
		
		currentY += height + 2 * offset;
		
		zoomOptieCB = maakCheckBox(GraphTool.rb.getString("GTIEP_zoomOptie"), currentX, currentY, width, height, zoomOptie, toolsPanel);
		currentX += zoomOptieCB.getWidth() + offset;
		traceOptieCB = maakCheckBox(GraphTool.rb.getString("GTIEP_volgOptie"), currentX, currentY, width, height, traceOptie, toolsPanel);
		currentX += traceOptieCB.getWidth() + offset;
		dragOptieCB = maakCheckBox(GraphTool.rb.getString("GTIEP_sleepOptie"), currentX, currentY, width, height, dragOptie, toolsPanel);
		currentX -= zoomOptieCB.getWidth() + traceOptieCB.getWidth() + 2 * offset;
		currentY += height + offset;
		
		tekenComponentCB = maakCheckBox(GraphTool.rb.getString("GTIEP_tekenComponentAan"), currentX, currentY, width, height, tekenComponentAan, toolsPanel);
		currentY += height + offset;
		snapToGridPointsCB = maakCheckBox(GraphTool.rb.getString("GTIEP_snapToGridPoints"), currentX, currentY, width, height, snapToGridPoints, toolsPanel);
		snapToGridPointsCB.setVisible(false);
		currentY += height + offset;
		krommeZonderExtrapolatieCB = maakCheckBox(GraphTool.rb.getString("GTIEP_geenExtrapolatie"), currentX, currentY, width, height, krommeZonderExtrapolatie, toolsPanel);
		krommeZonderExtrapolatieCB.setVisible(false);
		currentY += height + offset;
		krommeMetExtrapolatieCB = maakCheckBox(GraphTool.rb.getString("GTIEP_welExtrapolatie"), currentX, currentY, width, height, krommeMetExtrapolatie, toolsPanel);
		krommeMetExtrapolatieCB.setVisible(false);
		currentY += height + offset;
		
		tekenGrafiekNauwkeurigheidLabel = new JLabel(GraphTool.rb.getString("GTIEP_tekenGrafiekNauwk"));
		tekenGrafiekNauwkeurigheidLabel.setBounds(currentX, currentY, 3*width/5, height);
		tekenGrafiekNauwkeurigheidLabel.setFont(theFont);
		toolsPanel.add(tekenGrafiekNauwkeurigheidLabel);
		tekenGrafiekNauwkeurigheidLabel.setVisible(false);
		
		currentX += tekenGrafiekNauwkeurigheidLabel.getWidth() + offset;
		
		tekenGrafiekNauwkeurigheidTF = new JTextField(tekenGrafiekNauwkeurigheid);
		tekenGrafiekNauwkeurigheidTF.setBounds(currentX, currentY, 30, height);
		tekenGrafiekNauwkeurigheidTF.setFont(theFont);
		toolsPanel.add(tekenGrafiekNauwkeurigheidTF);
		tekenGrafiekNauwkeurigheidTF.setText("" + tekenGrafiekNauwkeurigheid);
		tekenGrafiekNauwkeurigheidTF.setVisible(false);
		tekenGrafiekNauwkeurigheidTF.addActionListener(this);
		tekenGrafiekNauwkeurigheidTF.addFocusListener(this);
		
		currentY -= 4*height + 3*offset;
		currentX += tekenComponentCB.getWidth() - tekenGrafiekNauwkeurigheidLabel.getWidth();	
		tabelComponentCB = maakCheckBox(GraphTool.rb.getString("GTIEP_tabelComponentAan"), currentX, currentY, width, height, tabelComponentAan, toolsPanel);
		currentY += height + offset;
		zoomInTabelCB = maakCheckBox(GraphTool.rb.getString("GTIEP_zoomInTabelOptie"), currentX, currentY, width - 20, height, zoomInTabel, toolsPanel);
		zoomInTabelCB.setVisible(false);	
		currentY += height + offset;	
		tabelAlsTekenToolCB = maakCheckBox(GraphTool.rb.getString("GTIEP_tabelTekenToolOptie"), currentX, currentY, width - 20, height, tabelAlsTekenTool, toolsPanel);
		tabelAlsTekenToolCB.setVisible(false);			
		currentY -= 2 * height + 2 * offset;
		currentX += tabelComponentCB.getWidth() + offset; //- 20;
		formuleComponentCB = 
			maakCheckBox(GraphTool.rb.getString("GTIEP_formuleComponentAan"), currentX, currentY, width, height, formuleComponentAan, toolsPanel);
		currentY += height + offset;
		
		formuleEditorOptiesButton = new FormuleEditorOptiesButton(this);
		formuleEditorOptiesButton.setBounds(currentX, currentY, width, height);
		formuleEditorOptiesButton.setFont(theFont);
		formuleEditorOptiesButton.addActionListener(this);
		formuleEditorOptiesButton.setVisible(formuleComponentAan);
		toolsPanel.add(formuleEditorOptiesButton);
		currentY += height + offset;
		
		veldComponentCB = 
				maakCheckBox(GraphTool.rb.getString("GTIEP_veldComponentAan"), currentX, currentY, width, height, veldComponentAan, toolsPanel);
		currentY += height + offset;

		veldEditorOptiesButton = new VeldEditorOptiesButton(this);
		veldEditorOptiesButton.setBounds(currentX, currentY, width, height);
		veldEditorOptiesButton.setFont(theFont);
		veldEditorOptiesButton.addActionListener(this);
		veldEditorOptiesButton.setVisible(veldComponentAan);
		toolsPanel.add(veldEditorOptiesButton);
		
		typeOpdracht = GEENOPDRACHT;
		
		opdrachtKeuze = new JComboBox();
		opdrachtKeuze.setBackground(getBackground());
		opdrachtKeuze.setFont(theFont);
		opdrachtKeuze.setBounds(20, 30, 400, 20);

		opdrachtKeuze.addItem(GraphTool.rb.getString("Opdr_kiesOpdracht"));
		opdrachtKeuze.addItem(GraphTool.rb.getString("Opdr_Opdracht1"));
		opdrachtKeuze.addItem(GraphTool.rb.getString("Opdr_Opdracht2"));
		opdrachtKeuze.addItem(GraphTool.rb.getString("Opdr_Opdracht3"));
		opdrachtKeuze.addItem(GraphTool.rb.getString("Opdr_Opdracht4"));

		opdrachtenPanel.add(opdrachtKeuze);	
		opdrachtKeuze.addActionListener(new OpdrachtKeuzeAL());
		
		explanLabel1 = new JLabel();
		explanLabel1.setBackground(getBackground());
		explanLabel1.setFont(theFont);
		explanLabel1.setBounds(25, 30, 350, 20);		
		explanLabel1.setVisible(false);
		opdrachtenPanel.add(explanLabel1);	

		explanLabel2 = new JLabel();
		explanLabel2.setBackground(getBackground());
		explanLabel2.setFont(theFont);
		explanLabel2.setBounds(25, 30, 350, 20);		
		explanLabel2.setVisible(false);
		opdrachtenPanel.add(explanLabel2);	
		
		scoreMaxLabel = new JLabel[maxScores.length];
		for(int i = 0; i < scoreMaxLabel.length; i++)
		{	scoreMaxLabel[i] = new JLabel(GraphTool.rb.getString("Opdr_maximumScore") +" " + (i + 1));
			scoreMaxLabel[i].setBackground(getBackground());
			scoreMaxLabel[i].setFont(theFont);
			scoreMaxLabel[i].setBounds(65, 50 + i * (20 + offset), 100, 20);
			scoreMaxLabel[i].setVisible(false);
			opdrachtenPanel.add(scoreMaxLabel[i]);	
		}
		
		for(int i = 1; i < maxScores.length; i++)
			maxScores[i] = 0;
		maxScores[0] = 10;
		
		scoreMaxTF = new JTextField[maxScores.length];
		for(int i = 0; i < scoreMaxTF.length; i++)
		{	scoreMaxTF[i] = new JTextField("" + maxScores[i]);
			scoreMaxTF[i].setFont(theFont);
			scoreMaxTF[i].setBounds(215, 50 + i * (20 + offset), 50, 20);
			scoreMaxTF[i].setVisible(false);
			opdrachtenPanel.add(scoreMaxTF[i]);			
			scoreMaxTF[i].addActionListener(this);
			scoreMaxTF[i].addFocusListener(this);
		}
		
		nauwkeurigheidLabel = new JLabel(GraphTool.rb.getString("Opdr_nauwkeurigheid"));
		nauwkeurigheidLabel.setBackground(getBackground());
		nauwkeurigheidLabel.setFont(theFont);
		nauwkeurigheidLabel.setBounds(65, 70, 150, 20);
		nauwkeurigheidLabel.setVisible(false);
		opdrachtenPanel.add(nauwkeurigheidLabel);	

		for(int i = 0; i < nauwkeurigheid.length; i++)
			nauwkeurigheid[i] = 5;

		nauwkeurigheidTF = new JTextField[nauwkeurigheid.length];
		for (int i = 0; i < nauwkeurigheidTF.length; i++)
		{	nauwkeurigheidTF[i] = new JTextField("" + nauwkeurigheid[i]);
			nauwkeurigheidTF[i].setFont(theFont);
			nauwkeurigheidTF[i].setBounds(215 + i * (50 + 2 * offset), 70, 50, 20);//moet beter
			nauwkeurigheidTF[i].setVisible(false);
			opdrachtenPanel.add(nauwkeurigheidTF[i]);	
			nauwkeurigheidTF[i].addActionListener(this);
			nauwkeurigheidTF[i].addFocusListener(this);
		}

		minimumPuntenLabel = new JLabel(GraphTool.rb.getString("Opdr_minimumPunten"));
		minimumPuntenLabel.setBackground(getBackground());
		minimumPuntenLabel.setFont(theFont);
		minimumPuntenLabel.setBounds(65, 70, 150, 20);
		minimumPuntenLabel.setVisible(false);
		opdrachtenPanel.add(minimumPuntenLabel);	

		for(int i = 0; i < minimumPunten.length; i++)
			minimumPunten[i] = 5;
		
		minimumPuntenTF = new JTextField[minimumPunten.length];
		for (int i = 0; i < minimumPuntenTF.length; i++)
		{	minimumPuntenTF[i] = new JTextField("" + minimumPunten[i]);
			minimumPuntenTF[i].setFont(theFont);
			minimumPuntenTF[i].setBounds(215  + i * (50 + 2 * offset), 70, 50, 20);
			minimumPuntenTF[i].setVisible(false);
			opdrachtenPanel.add(minimumPuntenTF[i]);	
			minimumPuntenTF[i].addActionListener(this);
			minimumPuntenTF[i].addFocusListener(this);
		}
		
		domeinControlerenBox = maakCheckBox(GraphTool.rb.getString("Opdr_domeinControleren"), 65, 120, 150, 20, domeinControleren, opdrachtenPanel);
		domeinControlerenBox.setVisible(false);
				
		leerlingZietTabelBox = maakCheckBox(GraphTool.rb.getString("Opdr_leerlingZietTabel"), 65, 70, 150, 20, leerlingZietTabel, opdrachtenPanel);
		leerlingZietTabelBox.setVisible(false);
		
		tekenRechteCB = maakCheckBox(GraphTool.rb.getString("Opdr_tekenRechte"), 65, 170, 300, 20, rechteVerbindingen, opdrachtenPanel);
		tekenRechteCB.setVisible(false);
		
		tekenMetExtrapolatieCB = maakCheckBox(GraphTool.rb.getString("Opdr_tekenMetExtrapolatie"), 65, 195, 300, 20, krommeMetExtrapolatie, opdrachtenPanel);
		tekenMetExtrapolatieCB.setVisible(false);
		
		tekenZonderExtrapolatieCB = maakCheckBox(GraphTool.rb.getString("Opdr_tekenZonderExtrapolatie"), 65, 220, 300, 20, krommeZonderExtrapolatie, opdrachtenPanel);
		tekenZonderExtrapolatieCB.setVisible(false);
		
		checkExternalCB = maakCheckBox(GraphTool.rb.getString("Opdr_checkExternal"), 65, 245, 300, 20, checkExternal, opdrachtenPanel);
		checkExternalCB.setVisible(false);
		
		resetDocentFunctie();
		resetDocentGraphPoints();
		
		docentFormuleComponent = new FormuleComponent(true);
		docentFormuleComponent.setBounds(65, 110, 270, 120); 
		docentFormuleComponent.zetRandverhoging(false);
		opdrachtenPanel.add(docentFormuleComponent,0);
		docentFormuleComponent.docent = true;
		docentFormuleComponent.zetFormuleRegels(9, false);
		docentFormuleComponent.addActionListener(this);
		docentFormuleComponent.addFocusListener(this);
		docentFormuleComponent.setVisible(false);
		docentFormuleComponent.zetGrafiekComponent(interactiePanel);
		docentFormuleComponent.zetFormeleFuncties(formeleFuncties, true);	
		docentFormuleComponent.zetDomeinInstelbaar(true, false);
		docentFormuleComponent.resetDomeinen();
		docentFormuleComponent.zetXAsNaam(xAsNaam, false);
		docentFormuleComponent.zetFunctieBeginAanpasbaar(false, false);
		
		docentTabelComponent = new TabelComponent(270, true);
		docentTabelComponent.setLocation(65, 290);
		opdrachtenPanel.add(docentTabelComponent);
		docentTabelComponent.zetAlsTekenTool(true, false);
		docentTabelComponent.zetEenTabel(true);
		docentTabelComponent.setVisible(false);
		docentTabelComponent.zetGrafiekComponent(interactiePanel);
		docentTabelComponent.addActionListener(this);
		
		//Schuifparametertabblad:
		JLabel schuifParamLabel = new JLabel(GraphTool.rb.getString("GTIEP_schuifparameters"));
		schuifParamLabel.setBounds(2 * offset, 25, defaultOpWidth / 2, height);
		schuifParamLabel.setFont(theFont);
		schuifParameterPanel.add(schuifParamLabel);
		
		schuifParamListElements = new DefaultListModel();
		schuifParamList = new JList(schuifParamListElements);
		schuifParamList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		schuifParamList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		//list.setVisibleRowCount(-1);
		JScrollPane listScroller = new JScrollPane(schuifParamList);
		//listScroller.setPreferredSize(new Dimension(80, 80));
		listScroller.setBounds(2 * offset, 50, 100, 80);
		schuifParameterPanel.add(listScroller);
		
		paramVoegToeButton = new SchuifParameterInstellingenButton(GraphTool.rb.getString("GTIEP_voegtoe"), false, this);
		paramVoegToeButton.setBounds(130, 50, 100, height);
		paramVoegToeButton.addActionListener(this);
		paramVoegToeButton.setFont(theFont);
		schuifParameterPanel.add(paramVoegToeButton);
		
		paramWijzigButton = new SchuifParameterInstellingenButton(GraphTool.rb.getString("GTIEP_wijzig"), true, this);
		paramWijzigButton.setBounds(130, 80, 100, height);
		paramWijzigButton.addActionListener(this);
		paramWijzigButton.setFont(theFont);
		schuifParameterPanel.add(paramWijzigButton);
		
		paramVerwijderButton = new JButton(GraphTool.rb.getString("GTIEP_verwijder"));
		paramVerwijderButton.setBounds(130, 110, 100, height);
		paramVerwijderButton.addActionListener(this);
		paramVerwijderButton.setFont(theFont);
		schuifParameterPanel.add(paramVerwijderButton);
		
		interactiePanel.zetAssenDefinitie(asDefXMin, asDefXMax, asDefXStap, asDefYMin, asDefYMax, asDefYStap);
		
	}
	
	//methode wordt maar 1 keer gebruikt, kan ook weg.
	public void resetDocentFunctie()
	{	maxAantalExpressies = 9;
		docentFuncties = new Expressie[maxAantalExpressies];
		docentFunctieStrings = new String[maxAantalExpressies]; 
		
		for(int i = 0; i < maxAantalExpressies; i++)
		{	docentFuncties[i] = null;
			docentFunctieStrings[i] = "$f@";			
		}
		if(docentFormuleComponent != null)
			docentFormuleComponent.resetDomeinen();
		interactiePanel.resetDocentFunctie();
	}
	

	public void resetDocentGraphPoints()
	{
		interactiePanel.resetDocentGraphPoints();
	}
	
	private JCheckBox maakCheckBox(
			String s, int x, int y, int b, int h, boolean selected, JPanel parent)
	{	JCheckBox checkbox = new JCheckBox(s);
		checkbox.setBounds(x,y,b,h);
		checkbox.setFont(theFont);
		checkbox.setBackground(getBackground());
		checkbox.setSelected(selected);
		checkbox.addActionListener(this);
		parent.add(checkbox);
		
		return checkbox;
	}
	
	public int getSelectedSchuifParamIndex()
	{
		return schuifParamList.getSelectedIndex();
	}
	
	public void voegSchuifParameterToe(SchuifParameter p)
	{
		schuifParamListElements.addElement(p.geefNaam());
		interactiePanel.voegSchuifParameterToe(p);
		
	}
	
	
	public void wijzigSchuifParameter(int i, SchuifParameter p)
	{
		schuifParamListElements.remove(i);
		schuifParamListElements.add(i, p.geefNaam());
		interactiePanel.wijzigSchuifParameter(i, p);
	}
	
	public void zetFormuleEditorOpties(boolean setState)
	{
		Hashtable h = formuleEditorOptiesButton.getOptions();
		interactiePanel.zetFormuleEditorOpties(h, setState);
	}
	
	public void zetVeldEditorOpties(boolean setState)
	{
		Hashtable h = veldEditorOptiesButton.getOptions();
		interactiePanel.zetVeldEditorOpties(h, setState);
	}
	
	private void zetOpdrachtKeuze(int keuze, boolean setState)
	{	
		int oudeKeuze = typeOpdracht;
		opdrachtKeuzeAllowed = false;
		opdrachtKeuze.setSelectedIndex(keuze);
		opdrachtKeuzeAllowed = true;
		typeOpdracht = keuze;
		if(!setState)
		{	resetDocentTabelComponent();
			resetDocentFormuleComponent(setState);
			resetDocentGraphPoints();
		}
		
		// geen opdracht terug naar defaults
		if (typeOpdracht == GEENOPDRACHT && (oudeKeuze != GEENOPDRACHT || setState))
		{	explanLabel1.setVisible(false);
			explanLabel2.setVisible(false);
			for(int i = 0; i < scoreMaxTF.length; i++)
			{	scoreMaxLabel[i].setVisible(false);
				scoreMaxTF[i].setVisible(false);
			}
			nauwkeurigheidLabel.setVisible(false);
			for(int i = 0; i < nauwkeurigheidTF.length; i++)
				nauwkeurigheidTF[i].setVisible(false);
			minimumPuntenLabel.setVisible(false);
			for(int i = 0; i < minimumPuntenTF.length; i++)
				minimumPuntenTF[i].setVisible(false);
			domeinControlerenBox.setVisible(false);
			leerlingZietTabelBox.setVisible(false);
			tekenRechteCB.setVisible(false);
			tekenMetExtrapolatieCB.setVisible(false);
			tekenZonderExtrapolatieCB.setVisible(false);
			checkExternalCB.setVisible(false);
			
			
			tekenComponentCB.setEnabled(true);
			tabelComponentCB.setEnabled(true);
			formuleComponentCB.setEnabled(true);
			veldComponentCB.setEnabled(true);
			tabelAlsTekenToolCB.setEnabled(true);
			if(!tabelAlsTekenToolCB.isSelected())
				zoomInTabelCB.setEnabled(true);
			if(!setState) {	
				formuleComponentAan = true;
				veldComponentAan = false;
				tekenComponentAan = false;
				tabelComponentAan = false;
				zoomInTabel = true;
				tabelAlsTekenTool = false; 
			}

			formuleComponentCB.setSelected(formuleComponentAan);
			veldComponentCB.setSelected(veldComponentAan);
			tekenComponentCB.setSelected(tekenComponentAan);
			tabelComponentCB.setSelected(tabelComponentAan);
			zoomInTabelCB.setSelected(zoomInTabel);
			tabelAlsTekenToolCB.setSelected(tabelAlsTekenTool);
			
			snapToGridPointsCB.setVisible(tekenComponentAan);
			krommeZonderExtrapolatieCB.setVisible(tekenComponentAan);
			krommeMetExtrapolatieCB.setVisible(tekenComponentAan);
			if(!setState)
			{	rechteVerbindingen = true;
				krommeMetExtrapolatie = true;
				krommeZonderExtrapolatie = true;
			}
			krommeMetExtrapolatieCB.setSelected(krommeMetExtrapolatie);
			krommeZonderExtrapolatieCB.setSelected(krommeZonderExtrapolatie);
			tekenGrafiekNauwkeurigheidLabel.setVisible(tekenComponentAan); 
			tekenGrafiekNauwkeurigheidTF.setVisible(tekenComponentAan); 
			zoomInTabelCB.setVisible(tabelComponentAan);
			tabelAlsTekenToolCB.setVisible(tabelComponentAan);
			
			interactiePanel.zetTypeOpdracht(0, setState);
			docentFormuleComponent.zetGrafiekKleuren();
			return;
		}
		
		tekenComponentCB.setEnabled(false);
		tabelComponentCB.setEnabled(false);
		formuleComponentCB.setEnabled(false);
		veldComponentCB.setEnabled(false);
		zoomInTabelCB.setEnabled(false);
		tabelAlsTekenToolCB.setEnabled(false);
		
		if (typeOpdracht == VINDFORMULEBIJGRAFIEK)
		{	
			explanLabel1.setText(GraphTool.rb.getString("Opdr_antwoordFormule"));		
			explanLabel1.setLocation(explanLabel1.getLocation().x, 70);
			explanLabel1.setVisible(true);
			explanLabel2.setVisible(false);
		
			docentFormuleComponent.setVisible(true);
			zetMaxAantalExpressies(9, setState);
			
			for(int i = 0; i < docentFormuleComponent.getAantalRegels(); i++)
			{	scoreMaxLabel[i].setLocation(65, 240 + i * (20 + offset));
				scoreMaxLabel[i].setVisible(true);
				scoreMaxTF[i].setLocation(215, 240 + i * (20 + offset));
				scoreMaxTF[i].setVisible(true);
			}
			for(int i = docentFormuleComponent.getAantalRegels(); i < scoreMaxLabel.length; i++)
			{	scoreMaxLabel[i].setVisible(false);
				scoreMaxTF[i].setVisible(false);
			}
			
			nauwkeurigheidLabel.setVisible(false);
			for(int i = 0; i < nauwkeurigheidTF.length; i++)
				nauwkeurigheidTF[i].setVisible(false);
			minimumPuntenLabel.setVisible(false);
			for(int i = 0; i < minimumPuntenTF.length; i++)
				minimumPuntenTF[i].setVisible(false);
			
			domeinControlerenBox.setLocation(domeinControlerenBox.getLocation().x, 240 + docentFormuleComponent.getAantalRegels() *(20+offset));
			domeinControlerenBox.setVisible(true);
			leerlingZietTabelBox.setVisible(false);
			tekenRechteCB.setVisible(false);
			tekenMetExtrapolatieCB.setVisible(false);
			tekenZonderExtrapolatieCB.setVisible(false);
			checkExternalCB.setVisible(false);
			if(scoreMaxLabel[1].isVisible())
				scoreMaxLabel[0].setText(GraphTool.rb.getString("Opdr_maximumScore") + " " + 1);
			else
				scoreMaxLabel[0].setText(GraphTool.rb.getString("Opdr_maximumScore"));

		}
		else if (typeOpdracht == VINDFORMULEBIJPUNTEN)
		{	
			explanLabel1.setText(GraphTool.rb.getString("Opdr_antwoordFormule"));		
			explanLabel1.setLocation(explanLabel1.getLocation().x, 70);
			explanLabel1.setVisible(true);
			
			docentFormuleComponent.setVisible(true);
			zetMaxAantalExpressies(1, setState);
			if(docentFuncties.length > 1)
				for(int i = 1; i < docentFuncties.length; i++)
				{	docentFuncties[i] = null;
					if(docentFunctieStrings != null && i < docentFunctieStrings.length)
						docentFunctieStrings[i] = "$f@";
				}
			
			explanLabel2.setText(GraphTool.rb.getString("Opdr_tabelPuntenX"));		
			explanLabel2.setLocation(explanLabel1.getLocation().x, 250);
			explanLabel2.setVisible(true);
			
			docentTabelComponent.setLocation(65, 290);
			docentTabelComponent.setVisible(true);
			docentTabelComponent.setYVakEditable(false);
			
			if (setState)
			{	docentTabelComponent.zetTabelPunten(interactiePanel.getPoints(interactiePanel.getActiveIndex(), true), true);
			}
			
			nauwkeurigheidLabel.setVisible(false);
			for(int i = 0; i < nauwkeurigheidTF.length; i++)
				nauwkeurigheidTF[i].setVisible(false);
			minimumPuntenLabel.setVisible(false);
			for(int i = 0; i < minimumPuntenTF.length; i++)
				minimumPuntenTF[i].setVisible(false);
			
			domeinControlerenBox.setVisible(false);
			leerlingZietTabelBox.setVisible(false);
			tekenRechteCB.setVisible(false);
			tekenMetExtrapolatieCB.setVisible(false);
			tekenZonderExtrapolatieCB.setVisible(false);
			checkExternalCB.setVisible(false);
			
			scoreMaxLabel[0].setLocation(65, 364);
			scoreMaxLabel[0].setText(GraphTool.rb.getString("Opdr_maximumScore"));
			scoreMaxLabel[0].setVisible(true);
			scoreMaxTF[0].setLocation(scoreMaxTF[0].getLocation().x, 364);
			scoreMaxTF[0].setVisible(true);
			for(int i = 1; i < scoreMaxTF.length; i++)
			{	scoreMaxLabel[i].setVisible(false);
				scoreMaxTF[i].setVisible(false);
			}

		}
		
		else if (typeOpdracht == TEKENPUNTENBIJFORMULE)
		{	// toelichting			
			explanLabel1.setText(GraphTool.rb.getString("Opdr_antwoordFormule"));		
			explanLabel1.setLocation(explanLabel1.getLocation().x, 70);
			explanLabel1.setVisible(true);
			
			explanLabel2.setVisible(false);

			docentFormuleComponent.setVisible(true);
			zetMaxAantalExpressies(3, setState);
			
			if (docentFunctieStrings != null)
			{	for(int i = 0; i < docentFunctieStrings.length; i++)
					if(!docentFunctieStrings[i].equals("$f@"))
					{	nauwkeurigheidTF[i].setVisible(true);
						minimumPuntenTF[i].setVisible(true);
						scoreMaxTF[i].setVisible(true);
					}
					
					else
					{	nauwkeurigheidTF[i].setVisible(false);
						minimumPuntenTF[i].setVisible(false);
						scoreMaxTF[i].setVisible(false);
					}
			}
			
			for(int i = 0; i < docentFormuleComponent.getAantalRegels(); i++)
			{	scoreMaxTF[i].setLocation(215, 240 + i * (20 + offset));
				scoreMaxTF[i].setVisible(true);
				nauwkeurigheidTF[i].setVisible(true);
				minimumPuntenTF[i].setVisible(true);
				
			}
			domeinControlerenBox.setVisible(false);
			leerlingZietTabelBox.setVisible(false);	
			
			int maxY = 260;
			nauwkeurigheidLabel.setLocation(nauwkeurigheidLabel.getLocation().x, maxY);
			nauwkeurigheidLabel.setVisible(true);
			for(int i = 0; i < nauwkeurigheidTF.length; i++)
			{	nauwkeurigheidTF[i].setLocation(nauwkeurigheidTF[i].getLocation().x, maxY);
			}
			maxY += 30;
			minimumPuntenLabel.setLocation(minimumPuntenLabel.getLocation().x, maxY);
			minimumPuntenLabel.setVisible(true);
			for(int i = 0; i < minimumPuntenTF.length; i++)
			{	minimumPuntenTF[i].setLocation(minimumPuntenTF[i].getLocation().x, maxY);
			}
			maxY += 30;

			scoreMaxLabel[0].setLocation(65, maxY);
			scoreMaxLabel[0].setVisible(true);
			for(int i = 1; i < scoreMaxLabel.length; i++)
				scoreMaxLabel[i].setVisible(false);
			for(int i = 0; i < minimumPuntenTF.length; i++)
			{	scoreMaxTF[i].setLocation(215 + i * (50 + 2 * offset), maxY);
			}
			scoreMaxLabel[0].setText(GraphTool.rb.getString("Opdr_maximumScore"));
			maxY += 30;
			tekenRechteCB.setLocation(tekenRechteCB.getLocation().x, maxY);
			maxY += 25;
			tekenMetExtrapolatieCB.setLocation(tekenMetExtrapolatieCB.getLocation().x, maxY);
			maxY += 25;
			tekenZonderExtrapolatieCB.setLocation(tekenZonderExtrapolatieCB.getLocation().x, maxY);
			maxY += 25;
			checkExternalCB.setLocation(tekenZonderExtrapolatieCB.getLocation().x, maxY);
			tekenRechteCB.setVisible(true);
			tekenMetExtrapolatieCB.setVisible(true);
			tekenZonderExtrapolatieCB.setVisible(true);
			checkExternalCB.setVisible(true);
			if(!setState) 
			{	rechteVerbindingen = false;
				krommeMetExtrapolatie = true;
				krommeZonderExtrapolatie = false;
			}
			tekenRechteCB.setSelected(rechteVerbindingen);
			tekenMetExtrapolatieCB.setSelected(krommeMetExtrapolatie);
			tekenZonderExtrapolatieCB.setSelected(krommeZonderExtrapolatie);
			checkExternalCB.setSelected(checkExternal);
			interactiePanel.zetKrommeKnoppen(rechteVerbindingen, krommeZonderExtrapolatie, krommeMetExtrapolatie);
			

		}
		else if (typeOpdracht == TEKENTABELPUNTEN)
		{	// toelichting			
			explanLabel1.setText(GraphTool.rb.getString("Opdr_tabelPunten"));		
			explanLabel1.setLocation(explanLabel1.getLocation().x, 70);
			explanLabel1.setVisible(true);		
		
			explanLabel2.setVisible(false);
			
			docentTabelComponent.zetTabelPunten(interactiePanel.getPoints(interactiePanel.getActiveIndex(), true), true);
			
				
			if(!setState)
				leerlingZietTabel = true;
			
			docentTabelComponent.setYVakEditable(true);
			docentTabelComponent.setLocation(65, 110);
			docentTabelComponent.setVisible(true);
			opdrachtenPanel.repaint();

			int maxY = 110 + docentTabelComponent.getSize().height + 40;

			domeinControlerenBox.setVisible(false);
			tekenRechteCB.setVisible(true);
			tekenMetExtrapolatieCB.setVisible(true);
			tekenZonderExtrapolatieCB.setVisible(true);
			leerlingZietTabelBox.setLocation(leerlingZietTabelBox.getLocation().x, maxY);
			leerlingZietTabelBox.setVisible(true);
			maxY += 30;
			
			nauwkeurigheidLabel.setLocation(nauwkeurigheidLabel.getLocation().x, maxY);
			nauwkeurigheidLabel.setVisible(true);
			nauwkeurigheidTF[0].setLocation(nauwkeurigheidTF[0].getLocation().x, maxY);
			nauwkeurigheidTF[0].setVisible(true);
			for(int i = 1; i < nauwkeurigheidTF.length; i++)
				nauwkeurigheidTF[i].setVisible(false);
			maxY += 30;

			scoreMaxLabel[0].setLocation(65, maxY);
			scoreMaxLabel[0].setVisible(true);
			scoreMaxLabel[0].setText(GraphTool.rb.getString("Opdr_maximumScore"));
			scoreMaxTF[0].setLocation(scoreMaxTF[0].getLocation().x, maxY);
			scoreMaxTF[0].setVisible(true);
			for(int i = 1; i < nauwkeurigheidTF.length; i++)
			{	scoreMaxLabel[i].setVisible(false);
				scoreMaxTF[i].setVisible(false);
			}
			maxY += 30;
			tekenRechteCB.setLocation(tekenRechteCB.getLocation().x, maxY);
			maxY += 25;
			tekenMetExtrapolatieCB.setLocation(tekenMetExtrapolatieCB.getLocation().x, maxY);
			maxY += 25;
			tekenZonderExtrapolatieCB.setLocation(tekenZonderExtrapolatieCB.getLocation().x, maxY);
			maxY += 25;
			checkExternalCB.setLocation(65, maxY);
			checkExternalCB.setVisible(true);
			if(!setState) 
			{	rechteVerbindingen = false;
				krommeMetExtrapolatie = false;
				krommeZonderExtrapolatie = false;
			}
			tekenRechteCB.setSelected(rechteVerbindingen);
			tekenMetExtrapolatieCB.setSelected(krommeMetExtrapolatie);
			tekenZonderExtrapolatieCB.setSelected(krommeZonderExtrapolatie);
			checkExternalCB.setSelected(checkExternal);
			
			minimumPuntenLabel.setVisible(false);
			for(int i = 0; i < minimumPuntenTF.length; i++)
				minimumPuntenTF[i].setVisible(false);
			leerlingZietTabelBox.setSelected(leerlingZietTabel);
			docentTabelComponent.produceAction("points changed");
			interactiePanel.zetKrommeKnoppen(rechteVerbindingen, krommeZonderExtrapolatie, krommeMetExtrapolatie);
		}
		interactiePanel.zetTypeOpdracht(typeOpdracht, setState);
		processMaxScore(); // processMaxScore is dependent of typeOpdracht, needs to be executed after zetTypeOpdracht!
		processNauwkeurigheid();
		processMinimumPunten();
		docentFormuleComponent.zetGrafiekKleuren();
		interactiePanel.getFormuleComponent().setEditable(false);
		interactiePanel.getTabelComponent().setFrozen(true);
		//interactiePanel.getTekenComponent().setFrozen(true);
		
	}
	
	private void resetDocentFormuleComponent(boolean setState)
	{	docentFormuleComponent.setVisible(false);
	
		docentFormuleComponent.zetMaxAantalFormules(9, setState);
		docentFormuleComponent.resetDomeinen();
		interactiePanel.getFormuleComponent().zetMaxAantalFormules(9, setState);	
		interactiePanel.getTekenComponent().zetAantalGrafieken(3);			
	}
	
	private void resetDocentTabelComponent()//boolean setState)
	{	docentTabelComponent.setVisible(false);
		docentTabelComponent.reset();
		zoomInTabel = true;
		interactiePanel.zetZoomInTabel(zoomInTabel);
		interactiePanel.getTabelComponent().reset();
	}
	
	private void zetMaxAantalExpressies(int aantalExpressies, boolean setState)
	{	maxAantalExpressies = aantalExpressies;
		docentFormuleComponent.zetMaxAantalFormules(maxAantalExpressies, setState);
		Expressie[] newDocentFuncties = new Expressie[maxAantalExpressies];
		String[] newDocentFunctieStrings = new String[maxAantalExpressies]; 
		String[][] newDocentDomeinStrings = new String[maxAantalExpressies][2];
		
		for(int i = 0; i < maxAantalExpressies; i++)
		{	if(i < docentFuncties.length)
			{	newDocentFuncties[i] = docentFuncties[i];
				newDocentFunctieStrings[i] = docentFunctieStrings[i];
			}
			else
			{	newDocentFuncties[i] = null;
				newDocentFunctieStrings[i] = "$f@";
			}
			if(i < docentFormuleComponent.getDomeinStrings().length && docentFormuleComponent.getDomeinStrings()[i] != null)
			{	newDocentDomeinStrings[i][0] = docentFormuleComponent.getDomeinStrings()[i][0];
				newDocentDomeinStrings[i][1] = docentFormuleComponent.getDomeinStrings()[i][1];
			}
			else			
			{	newDocentDomeinStrings[i][0] = "$f" + Double.toString(Double.NEGATIVE_INFINITY) + "@";
				newDocentDomeinStrings[i][1] = "$f" + Double.toString(Double.POSITIVE_INFINITY) + "@";
			}
			docentFormuleComponent.zetDomein(newDocentDomeinStrings[i], i);
		}
		docentFuncties = newDocentFuncties;
		docentFunctieStrings = newDocentFunctieStrings;
		interactiePanel.zetDocentFuncties(docentFuncties);
		//interactiePanel.zetDocentDomeinen(docentFormuleComponent.getDomeinen());
		interactiePanel.zetDocentDomeinen(docentFormuleComponent.getDomeinStrings());
	}
	
	public GraphToolInteractiePanel getInteractiePanel()
	{
		return interactiePanel;
	}
	
	public Hashtable getEditState() {
		docentFormuleComponent.finish();
		processMaxScore();
		processMinimumPunten();
		processNauwkeurigheid();
		Hashtable h = interactiePanel.getEditState();
		Hashtable h1 = docentFormuleComponent.getDocentState();
		for (Enumeration e = h1.keys(); e.hasMoreElements();)
		{	Object aKey = e.nextElement();
			Object aValue = h1.get(aKey);
			h.put(aKey, aValue);
		}
		Hashtable h2 = docentTabelComponent.getDocentState();
		for (Enumeration e = h2.keys(); e.hasMoreElements();)
		{	Object aKey = e.nextElement();
			Object aValue = h2.get(aKey);
			h.put(aKey, aValue);
		}
		double beginxDocent = 0;
		double beginyDocent = 0;
		double docentSchaalFactorX = 1;
		double docentSchaalFactorY = 1;
		if(h.containsKey("beginx"))
			beginxDocent = ((Double) h.get("beginx")).doubleValue();
		if(h.containsKey("beginy"))
			beginyDocent = ((Double) h.get("beginy")).doubleValue();
		if(h.containsKey("schaalFactorX"))
			docentSchaalFactorX = ((Double) h.get("schaalFactorX")).doubleValue();
		if(h.containsKey("schaalFactorY"))
			docentSchaalFactorY = ((Double) h.get("schaalFactorY")).doubleValue();
		
		h.put("beginxDocent", new Double(beginxDocent));
		h.put("beginyDocent", new Double(beginyDocent));
		h.put("docentSchaalFactorX", new Double(docentSchaalFactorX));
		h.put("docentSchaalFactorY", new Double(docentSchaalFactorY));
		h.put("defaultIpWidth", new Integer(defaultIpWidth));
		h.put("defaultIpHeight", new Integer(defaultIpHeight));
		
		if (manualScaling) { // in case of manual scaling we need to reset the drag option for student use
			h.put("dragOptie", new Boolean(dragOptie)); // TODO
			h.put("zoomOptie", new Boolean(zoomOptie));
		}
		
		return h;
	}
	
	
	public void setEditState(Hashtable h) {

		if (h.containsKey("xAsNaam")) 
			xAsNaam = (String) h.get("xAsNaam");
		if (h.containsKey("yAsNaam")) 
			yAsNaam = (String) h.get("yAsNaam");
		if (h.containsKey("formuleComponentAan")) 
			formuleComponentAan = ((Boolean) h.get("formuleComponentAan")).booleanValue();
		if (h.containsKey("veldComponentAan")) 
			veldComponentAan = ((Boolean) h.get("veldComponentAan")).booleanValue();
		if (h.containsKey("tekenComponentAan")) 
			tekenComponentAan = ((Boolean) h.get("tekenComponentAan")).booleanValue();
		if (h.containsKey("tabelComponentAan")) 
			tabelComponentAan = ((Boolean) h.get("tabelComponentAan")).booleanValue();
		if (h.containsKey("assenZichtbaar")) 
			assenZichtbaar = ((Boolean) h.get("assenZichtbaar")).booleanValue();
		if (h.containsKey("roosterZichtbaar")) 
			roosterZichtbaar = ((Boolean) h.get("roosterZichtbaar")).booleanValue();
		if (h.containsKey("roosterGrof")) 
			roosterGrof = ((Boolean) h.get("roosterGrof")).booleanValue();
		if (h.containsKey("schaalZichtbaar")) 
			schaalZichtbaar = ((Boolean) h.get("schaalZichtbaar")).booleanValue();
		if (h.containsKey("piLijnenZichtbaar")) 
			piLijnenZichtbaar = ((Boolean) h.get("piLijnenZichtbaar")).booleanValue();
		if (h.containsKey("zoomOptie")) 
			zoomOptie = ((Boolean) h.get("zoomOptie")).booleanValue();
		if (h.containsKey("traceOptie")) 
			traceOptie = ((Boolean) h.get("traceOptie")).booleanValue();
		if (h.containsKey("dragOptie")) 
			dragOptie = ((Boolean) h.get("dragOptie")).booleanValue();
		if (h.containsKey("formeleFuncties")) 
			formeleFuncties = ((Boolean) h.get("formeleFuncties")).booleanValue();
		if (h.containsKey("zoomInTabel")) 
			zoomInTabel = ((Boolean) h.get("zoomInTabel")).booleanValue();
		if (h.containsKey("tabelAlsTekenTool")) 
			tabelAlsTekenTool = ((Boolean) h.get("tabelAlsTekenTool")).booleanValue();
		if (h.containsKey("xPositief")) 
			xPositief = ((Boolean) h.get("xPositief")).booleanValue();
		if (h.containsKey("yPositief")) 
			yPositief = ((Boolean) h.get("yPositief")).booleanValue();
		if (h.containsKey("xAsLog"))
			xAsLog = ((Boolean) h.get("xAsLog")).booleanValue();
		if (h.containsKey("yAsLog"))
			yAsLog = ((Boolean) h.get("yAsLog")).booleanValue();
		
		if (h.containsKey("manualScalingX")) 
			manualScalingX = ((Boolean) h.get("manualScalingX")).booleanValue();
		if (h.containsKey("manualScalingY")) 
			manualScalingY = ((Boolean) h.get("manualScalingY")).booleanValue();

		if (h.containsKey("xVarEditable")) 
			xVarEditable = ((Boolean) h.get("xVarEditable")).booleanValue();
		if (h.containsKey("yVarEditable")) 
			yVarEditable = ((Boolean) h.get("yVarEditable")).booleanValue();
		if (h.containsKey("snapToGridPoints"))
			snapToGridPoints = ((Boolean) h.get("snapToGridPoints")).booleanValue();
		if (h.containsKey("rechteVerbindingen"))
			rechteVerbindingen = ((Boolean) h.get("rechteVerbindingen")).booleanValue();
		if (h.containsKey("krommeZonderExtrapolatie"))
			krommeZonderExtrapolatie = ((Boolean) h.get("krommeZonderExtrapolatie")).booleanValue();
		if (h.containsKey("krommeMetExtrapolatie"))
			krommeMetExtrapolatie = ((Boolean) h.get("krommeMetExtrapolatie")).booleanValue();
		if (h.containsKey("tekenGrafiekNauwkeurigheid"))
			tekenGrafiekNauwkeurigheid = ((Integer)h.get("tekenGrafiekNauwkeurigheid")).intValue();
		if (h.containsKey("beginxDocent"))
			beginxDocent = ((Double) h.get("beginxDocent")).doubleValue();
		if (h.containsKey("beginyDocent"))
			beginyDocent = ((Double) h.get("beginyDocent")).doubleValue();
		if (h.containsKey("docentSchaalFactorX"))
			docentSchaalFactorX = ((Double) h.get("docentSchaalFactorX")).doubleValue();
		if (h.containsKey("docentSchaalFactorY"))
			docentSchaalFactorY = ((Double) h.get("docentSchaalFactorY")).doubleValue();
		
		if (h.containsKey("asDefXMin"))
			asDefXMin = ((Double) h.get("asDefXMin")).doubleValue();
		if (h.containsKey("asDefXMax"))
			asDefXMax = ((Double) h.get("asDefXMax")).doubleValue();
		if (h.containsKey("asDefXStap"))
			asDefXStap = ((Double) h.get("asDefXStap")).doubleValue();
		if (h.containsKey("asDefYMin"))
			asDefYMin = ((Double) h.get("asDefYMin")).doubleValue();
		if (h.containsKey("asDefYMax"))
			asDefYMax = ((Double) h.get("asDefYMax")).doubleValue();
		if (h.containsKey("asDefYStap"))
			asDefYStap = ((Double) h.get("asDefYStap")).doubleValue();
		
		if (h.containsKey("typeOpdracht")) 
			typeOpdracht = ((Integer) h.get("typeOpdracht")).intValue();		
		if (h.containsKey("maxScores")) 
			maxScores = (int[]) h.get("maxScores");		
		if (h.containsKey("docentFunctieStrings")) 
			docentFunctieStrings = (String[]) h.get("docentFunctieStrings");
		if(docentFunctieStrings != null)
			for(int i = 0; i < docentFunctieStrings.length; i++)
				if (!docentFunctieStrings[i].equals("$f@"))
					docentFuncties[i] = FormuleParser.geefExpressie(docentFunctieStrings[i]);
		if (h.containsKey("nauwkeurigheid")) 
			nauwkeurigheid = (int[]) h.get("nauwkeurigheid");		
		if (h.containsKey("minimumPunten")) 
			minimumPunten = (int[]) h.get("minimumPunten");		
		if (h.containsKey("leerlingZietTabel")) 
			leerlingZietTabel = ((Boolean) h.get("leerlingZietTabel")).booleanValue();	
		if (h.containsKey("domeinControleren"))
			domeinControleren = ((Boolean) h.get("domeinControleren")).booleanValue();
		if (h.containsKey("checkExternal"))
			checkExternal = ((Boolean) h.get("checkExternal")).booleanValue();
		
		for(int i = 0; i < nauwkeurigheidTF.length; i++)
			nauwkeurigheidTF[i].setText("" + nauwkeurigheid[i]);
		for(int i = 0; i < minimumPuntenTF.length; i++)
			minimumPuntenTF[i].setText("" + minimumPunten[i]);
		for(int i = 0; i < scoreMaxTF.length; i++)
			scoreMaxTF[i].setText("" + maxScores[i]);
		leerlingZietTabelBox.setSelected(leerlingZietTabel);
		domeinControlerenBox.setSelected(domeinControleren);
		tekenRechteCB.setSelected(rechteVerbindingen);
		tekenMetExtrapolatieCB.setSelected(krommeMetExtrapolatie);
		tekenZonderExtrapolatieCB.setSelected(krommeZonderExtrapolatie);
		checkExternalCB.setSelected(checkExternal);
		
		xAsNaamTF.setText(xAsNaam);
		yAsNaamTF.setText(yAsNaam);
		asDefXMinTF.setText(String.valueOf(asDefXMin));
		asDefXMaxTF.setText(String.valueOf(asDefXMax));
		asDefXStapTF.setText(String.valueOf(asDefXStap));
		asDefYMinTF.setText(String.valueOf(asDefYMin));
		asDefYMaxTF.setText(String.valueOf(asDefYMax));
		asDefYStapTF.setText(String.valueOf(asDefYStap));
		interactiePanel.zetAssenDefinitie(asDefXMin, asDefXMax, asDefXStap, asDefYMin, asDefYMax, asDefYStap);

		formuleComponentCB.setSelected(formuleComponentAan);
		
		veldComponentCB.setSelected(veldComponentAan);
		veldEditorOptiesButton.setVisible(veldComponentAan);
		tekenComponentCB.setSelected(tekenComponentAan);
		tabelComponentCB.setSelected(tabelComponentAan);
		assenZichtbaarCB.setSelected(assenZichtbaar);
		roosterZichtbaarCB.setSelected(roosterZichtbaar);
		roosterXCB.setSelected(roosterX);
		roosterYCB.setSelected(roosterY);
		roosterGrofCB.setSelected(roosterGrof);
		schaalZichtbaarCB.setSelected(schaalZichtbaar);
		schaalXCB.setSelected(schaalX);
		schaalYCB.setSelected(schaalY);
		piLijnenZichtbaarCB.setSelected(piLijnenZichtbaar);
		zoomOptieCB.setSelected(zoomOptie);
		traceOptieCB.setSelected(traceOptie && !tekenComponentAan);
		dragOptieCB.setSelected(dragOptie);
		zoomInTabelCB.setSelected(zoomInTabel);
		tabelAlsTekenToolCB.setSelected(tabelAlsTekenTool);
		xPositiefCB.setSelected(xPositief);
		yPositiefCB.setSelected(yPositief);
		xAsLogCB.setSelected(xAsLog);
		yAsLogCB.setSelected(yAsLog);
		
		asManualDefCB.setSelected(manualScalingX && manualScalingY);
		
		asDefXMinTF.setEnabled(manualScalingX); // also enable/disable the input field
		asDefXMaxTF.setEnabled(manualScalingX);
		asDefXStapTF.setEnabled(manualScalingX);
		asDefYMinTF.setEnabled(manualScalingY); // also enable/disable the input field
		asDefYMaxTF.setEnabled(manualScalingY);
		asDefYStapTF.setEnabled(manualScalingY);

		xVarEditableCB.setSelected(xVarEditable);
		yVarEditableCB.setSelected(yVarEditable);
		snapToGridPointsCB.setSelected(snapToGridPoints);
		krommeZonderExtrapolatieCB.setSelected(krommeZonderExtrapolatie);
		krommeMetExtrapolatieCB.setSelected(krommeMetExtrapolatie);
		tekenGrafiekNauwkeurigheidTF.setText("" + tekenGrafiekNauwkeurigheid);
		
		roosterGrofCB.setVisible(roosterZichtbaar);
		zoomInTabelCB.setVisible(tabelComponentAan);
		zoomInTabelCB.setEnabled(!tabelAlsTekenTool);
		tabelAlsTekenToolCB.setVisible(tabelComponentAan);
		snapToGridPointsCB.setVisible(tekenComponentAan);
		traceOptieCB.setVisible(!tekenComponentAan);

		krommeZonderExtrapolatieCB.setVisible(tekenComponentAan);
		krommeMetExtrapolatieCB.setVisible(tekenComponentAan);
		tekenGrafiekNauwkeurigheidLabel.setVisible(tekenComponentAan);// && tekenGrafiekAan);
		tekenGrafiekNauwkeurigheidTF.setVisible(tekenComponentAan);// && tekenGrafiekAan);
		
		if (h.containsKey("defaultIpWidth"))
			defaultIpWidth = ((Integer) h.get("defaultIpWidth")).intValue();
		if (h.containsKey("defaultIpHeight"))
			defaultIpHeight = ((Integer) h.get("defaultIpHeight")).intValue();
		
		setBounds(getLocation().x, getLocation().y, defaultIpWidth + defaultOpWidth + 10, Math.max(defaultIpHeight, defaultOpHeight));
		
		if (typeOpdracht > 0)
			tabbedPane.setSelectedComponent(opdrachtenPanel);
		
		Hashtable formuleOpties = new Hashtable();
		if(h.containsKey("grafiekKleuren"))
			formuleOpties.put("grafiekKleuren", h.get("grafiekKleuren"));
		if(h.containsKey("kleurInstelbaar"))
			formuleOpties.put("kleurInstelbaar", h.get("kleurInstelbaar"));
		if(h.containsKey("functieBeginZichtbaar"))
			formuleOpties.put("functieBeginZichtbaar", h.get("functieBeginZichtbaar"));
		if(h.containsKey("functieBeginAanpasbaar"))
			formuleOpties.put("functieBeginAanpasbaar", h.get("functieBeginAanpasbaar"));
		if(h.containsKey("formeleFuncties"))
			formuleOpties.put("formeleFuncties", h.get("formeleFuncties"));
		if(h.containsKey("domeinInstelbaar"))
			formuleOpties.put("domeinInstelbaar", h.get("domeinInstelbaar"));
		if(h.containsKey("formuleComponentHoogte"))
			formuleOpties.put("formuleComponentHoogte", h.get("formuleComponentHoogte"));
		
		if(h.containsKey("functieToegestaan"))
			formuleOpties.put("functieToegestaan", h.get("functieToegestaan"));
		if(h.containsKey("ongelijkheidToegestaan"))
			formuleOpties.put("ongelijkheidToegestaan", h.get("ongelijkheidToegestaan"));
		if(h.containsKey("implicieteFunctieToegestaan"))
			formuleOpties.put("implicieteFunctieToegestaan", h.get("implicieteFunctieToegestaan"));
		if(h.containsKey("verticaleLijnToegestaan"))
			formuleOpties.put("verticaleLijnToegestaan", h.get("verticaleLijnToegestaan"));
		if(h.containsKey("parametrisatieToegestaan"))
			formuleOpties.put("parametrisatieToegestaan", h.get("parametrisatieToegestaan"));
		
		formuleEditorOptiesButton.setOptions(formuleOpties);
		
		Hashtable veldOpties = new Hashtable();
		if(h.containsKey("veldGrafiekType"))
			veldOpties.put("veldGrafiekType", h.get("veldGrafiekType"));
		if(h.containsKey("veldPijlGrootteModus"))
			veldOpties.put("veldPijlGrootteModus", h.get("veldPijlGrootteModus"));
		if(h.containsKey("veldPijlGroottePixels"))
			veldOpties.put("veldPijlGroottePixels", h.get("veldPijlGroottePixels"));
		if(h.containsKey("veldPijlSchaalfactor"))
			veldOpties.put("veldPijlSchaalfactor", h.get("veldPijlSchaalfactor"));
		if(h.containsKey("veldLargerGridStartPoints"))
			veldOpties.put("veldLargerGridStartPoints", h.get("veldLargerGridStartPoints"));
		if(h.containsKey("veldComponentHoogte"))
			veldOpties.put("veldComponentHoogte", h.get("veldComponentHoogte"));
		veldEditorOptiesButton.setOptions(veldOpties);

		interactiePanel.setEditState(h);
		docentTabelComponent.zetXAsNaam(xAsNaam);
		docentTabelComponent.zetYAsNaam(yAsNaam, true);
		docentFormuleComponent.zetXAsNaam(xAsNaam, true);
		docentFormuleComponent.zetYAsNaam(yAsNaam, true);
		docentTabelComponent.setState(h, true);
		docentTabelComponent.setRandomAllowed(true);
		docentFormuleComponent.setState(h, null, null, true);
		docentFormuleComponent.zetFormeleFuncties(formeleFuncties, true);
		zetOpdrachtKeuze(typeOpdracht, true);
		if(interactiePanel.schuifParameters != null)
			for(int i = 0; i < interactiePanel.schuifParameters.length; i++)
			{
				schuifParamListElements.addElement(interactiePanel.schuifParameters[i].geefNaam());
			}
	}
	
	public void setBounds(int x, int y, int b, int h) {
		super.setBounds(x,y,b,h);
		optionsPanel.setBounds(defaultIpWidth + 10, 20, defaultOpWidth, defaultOpHeight);
		interactiePanel.setBounds(0, 0, defaultIpWidth, defaultIpHeight);
	}
	
	public void zetBreedte(int b) {
		defaultIpWidth = b;
		interactiePanel.setBounds(0, 0, b,interactiePanel.getHeight());	
		setBounds(getLocation().x, getLocation().y, b + defaultOpWidth + 10, Math.max(defaultIpHeight, defaultOpHeight));		
		optionsPanel.setBounds(defaultIpWidth + 10, 20, defaultOpWidth, defaultOpHeight);
		
		System.out.println("zetBreedte manualScaling=" + manualScaling);

		if (manualScaling) {
			interactiePanel.zetAssenDefinitie(asDefXMin, asDefXMax, asDefXStap, asDefYMin, asDefYMax, asDefYStap);
		}
	}
	
	public void zetHoogte(int h) {
		defaultIpHeight = h;
		interactiePanel.setBounds(0, 0, interactiePanel.getWidth(), h);	
		if (manualScaling) {
			interactiePanel.zetAssenDefinitie(asDefXMin, asDefXMax, asDefXStap, asDefYMin, asDefYMax, asDefYStap);
		}
	}
	
	public void wis() {
			
	}
	
	public void zetMode(int mode) {
			
	}
	
	public void stop() {
			
	}
	
	public void start() {
			
	}
	
	public void addActionListener(ActionListener al) {
			
	}
	
	public void processNauwkeurigheid() {	
		for(int i = 0; i < nauwkeurigheid.length; i++) {	
			String msString = nauwkeurigheidTF[i].getText();
		    boolean error = false;
			int ms = 0;
			try {	
				ms = Integer.parseInt(msString);
			}
			catch (NumberFormatException nfe) {	
				error = true;
			}
			if (!error && (ms > 0) && (ms < 50)) {	
				nauwkeurigheid[i] = ms;
			}
			nauwkeurigheidTF[i].setText("" + nauwkeurigheid[i]);
		}
	    interactiePanel.zetNauwkeurigheid(nauwkeurigheid);
	}

	public void processMinimumPunten()
	    {	for(int i = 0; i < minimumPunten.length; i++)
	    	{	String msString = minimumPuntenTF[i].getText();
		    	boolean error = false;
				int ms = 0;
				try
				{	ms = Integer.parseInt(msString);
				}
				catch (NumberFormatException nfe)
				{	error = true;
				}
				if (!error && (ms > 0) && (ms < 50))
				{	minimumPunten[i] = ms;
				}
				minimumPuntenTF[i].setText("" + minimumPunten[i]);
	    	}
			interactiePanel.zetMinimumPunten(minimumPunten);
	    }
	    
	public void processMaxScore()
	    {	for(int i = 0; i < maxScores.length; i++)
	    	{	String msString = scoreMaxTF[i].getText();
		    	boolean error = false;
				int ms = 0;
				try
				{	ms = Integer.parseInt(msString);
				}
				catch (NumberFormatException nfe)
				{	error = true;
				}
				if (!error && (ms >= 0))
				{	maxScores[i] = ms;
				}
				scoreMaxTF[i].setText("" + maxScores[i]);
	    	}
			interactiePanel.zetMaxScores(maxScores);
	    }
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(formuleComponentCB))
		{	formuleComponentAan = formuleComponentCB.isSelected();
			formuleEditorOptiesButton.setVisible(formuleComponentAan);
			interactiePanel.zetFormuleComponent(formuleComponentAan, false);
		}
		if(e.getSource().equals(veldComponentCB))
		{	veldComponentAan = veldComponentCB.isSelected();
			veldEditorOptiesButton.setVisible(veldComponentAan);
			interactiePanel.zetVeldComponent(veldComponentAan, false);
		}
		if(e.getSource().equals(tekenComponentCB)) {	
			tekenComponentAan = tekenComponentCB.isSelected();
			snapToGridPointsCB.setVisible(tekenComponentAan);
			krommeZonderExtrapolatieCB.setVisible(tekenComponentAan);
			krommeMetExtrapolatieCB.setVisible(tekenComponentAan);
			tekenGrafiekNauwkeurigheidLabel.setVisible(tekenComponentAan);// && tekenGrafiekAan);
			tekenGrafiekNauwkeurigheidTF.setVisible(tekenComponentAan);// && tekenGrafiekAan);
			interactiePanel.zetTekenComponent(tekenComponentAan);
			
			if (tekenComponentAan) {
				traceOptie = false;
				traceOptieCB.setSelected(false);
			}
			traceOptieCB.setVisible(!tekenComponentAan);

		}
		if(e.getSource().equals(tabelComponentCB))
		{	tabelComponentAan = tabelComponentCB.isSelected();
			zoomInTabelCB.setVisible(tabelComponentAan);
			tabelAlsTekenToolCB.setVisible(tabelComponentAan);
			interactiePanel.zetTabelComponent(tabelComponentAan, false);
		}
		if(e.getSource().equals(assenZichtbaarCB))
		{	assenZichtbaar = assenZichtbaarCB.isSelected();
			interactiePanel.zetAssen(assenZichtbaar);
		}
		if(e.getSource().equals(asManualDefCB)) {
			manualScaling = asManualDefCB.isSelected();
			manualScalingX = asManualDefCB.isSelected();
			manualScalingY = asManualDefCB.isSelected();

			interactiePanel.zetManualScalingX(manualScalingX);
			interactiePanel.zetManualScalingY(manualScalingY);

			asDefXMinTF.setEnabled(manualScalingY);
			asDefXMaxTF.setEnabled(manualScalingY);
			asDefXStapTF.setEnabled(manualScalingY);
			asDefYMinTF.setEnabled(manualScalingY);
			asDefYMaxTF.setEnabled(manualScalingY);
			asDefYStapTF.setEnabled(manualScalingY);

			if (manualScaling) {
				xAsLog = false;
				xAsLogCB.setSelected(xAsLog);
				interactiePanel.zetXAsLog(xAsLog);
				
				yAsLog = false;
				yAsLogCB.setSelected(yAsLog);
				interactiePanel.zetYAsLog(yAsLog);

				interactiePanel.zetDragOptie(!manualScaling);
				interactiePanel.zetZoomEnabled(!manualScaling);
				interactiePanel.zetAssenDefinitie(asDefXMin, asDefXMax, asDefXStap, asDefYMin, asDefYMax, asDefYStap);

			}
			else {
				dragOptie = dragOptieCB.isSelected();
				interactiePanel.zetDragOptie(dragOptie);
				interactiePanel.zetZoomEnabled(zoomOptie);
			}
			
			piLijnenZichtbaarCB.setVisible(!xAsLog);
		}
		
		if(e.getSource().equals(roosterZichtbaarCB))
		{	roosterZichtbaar = roosterZichtbaarCB.isSelected();
			roosterX = roosterZichtbaar;
			roosterY = roosterZichtbaar;
			roosterGrofCB.setVisible(roosterZichtbaar);
			roosterXCB.setVisible(roosterZichtbaar);
			roosterYCB.setVisible(roosterZichtbaar);
			roosterXCB.setSelected(roosterX);
			roosterYCB.setSelected(roosterY);
			interactiePanel.zetRooster(roosterZichtbaar, roosterX, roosterY);
		}
		if(e.getSource().equals(roosterXCB))
		{	roosterX = roosterXCB.isSelected();
			interactiePanel.zetRoosterX(roosterX);
		}
		if(e.getSource().equals(roosterYCB))
		{	roosterY = roosterYCB.isSelected();
			interactiePanel.zetRoosterY(roosterY);
		}
		if(e.getSource().equals(roosterGrofCB))
		{	roosterGrof = roosterGrofCB.isSelected();
			interactiePanel.zetRoosterGrof(roosterGrof);
		}
		if(e.getSource().equals(schaalZichtbaarCB))
		{	schaalZichtbaar = schaalZichtbaarCB.isSelected();
			schaalX = schaalZichtbaar;
			schaalY = schaalZichtbaar;
			schaalXCB.setVisible(schaalZichtbaar);
			schaalYCB.setVisible(schaalZichtbaar);
			schaalXCB.setSelected(schaalX);
			schaalYCB.setSelected(schaalY);
			interactiePanel.zetSchaal(schaalZichtbaar, schaalX, schaalY);
		}
		if(e.getSource().equals(schaalXCB))
		{	schaalX = schaalXCB.isSelected();
			interactiePanel.zetSchaalX(schaalX);
		}
		if(e.getSource().equals(schaalYCB))
		{	schaalY = schaalYCB.isSelected();
			interactiePanel.zetSchaalY(schaalY);
		}
		if(e.getSource().equals(piLijnenZichtbaarCB))
		{	piLijnenZichtbaar = piLijnenZichtbaarCB.isSelected();
			interactiePanel.zetPiLijnen(piLijnenZichtbaar);
		}
		if(e.getSource().equals(zoomOptieCB))
		{	zoomOptie = zoomOptieCB.isSelected();
			interactiePanel.zetZoomOptie(zoomOptie);
		}
		if(e.getSource().equals(traceOptieCB))
		{	traceOptie = traceOptieCB.isSelected();
			interactiePanel.zetTraceOptie(traceOptie);
		}
		if(e.getSource().equals(dragOptieCB)) {	
			if ( (!manualScalingX) && (!manualScalingY) ) {
				dragOptie = dragOptieCB.isSelected();
				interactiePanel.zetDragOptie(dragOptie);
			}
		}
		if(e.getSource().equals(zoomInTabelCB))
		{	zoomInTabel = zoomInTabelCB.isSelected();
			interactiePanel.zetZoomInTabel(zoomInTabel);
		}
		if(e.getSource().equals(tabelAlsTekenToolCB))
		{	tabelAlsTekenTool = tabelAlsTekenToolCB.isSelected();
			zoomInTabelCB.setEnabled(!tabelAlsTekenTool);
			if(tabelAlsTekenTool)
			{	zoomInTabel = false;
				zoomInTabelCB.setSelected(false);
				interactiePanel.zetZoomInTabel(false);
			}
			interactiePanel.zetTabelAlsTekenTool(tabelAlsTekenTool, false);
		}
		if(e.getSource().equals(xPositiefCB))
		{	xPositief = xPositiefCB.isSelected();
			interactiePanel.zetXPositief(xPositief);
		}
		if(e.getSource().equals(yPositiefCB))
		{	yPositief = yPositiefCB.isSelected();
			interactiePanel.zetYPositief(yPositief);
		}
		if(e.getSource().equals(xAsLogCB)) {
			xAsLog = xAsLogCB.isSelected();
			interactiePanel.zetXAsLog(xAsLog);
			if (xAsLog) {
				piLijnenZichtbaarCB.setSelected(false);
				piLijnenZichtbaar = false;
				interactiePanel.zetPiLijnen(piLijnenZichtbaar);

				manualScaling = false;
				asManualDefCB.setSelected(manualScaling);
				
				manualScalingX = false;
				interactiePanel.zetManualScalingX(manualScalingX);
				asDefXMinTF.setEnabled(manualScalingX);
				asDefXMaxTF.setEnabled(manualScalingX);
				asDefXStapTF.setEnabled(manualScalingX);

				manualScalingY = false;
				interactiePanel.zetManualScalingX(manualScalingY);
				asDefYMinTF.setEnabled(manualScalingY);
				asDefYMaxTF.setEnabled(manualScalingY);
				asDefYStapTF.setEnabled(manualScalingY);
				
				interactiePanel.zetDragOptie(dragOptie);
				interactiePanel.zetZoomEnabled(zoomOptie);
			}
			
			piLijnenZichtbaarCB.setVisible(!xAsLog);
		}
		
		if(e.getSource().equals(yAsLogCB)) 
		{	yAsLog = yAsLogCB.isSelected();
			interactiePanel.zetYAsLog(yAsLog);
			if (yAsLog) {
				manualScaling = false;
				asManualDefCB.setSelected(manualScaling);

				manualScaling = false;
				asManualDefCB.setSelected(manualScaling);
				
				manualScalingX = false;
				interactiePanel.zetManualScalingX(manualScalingX);
				asDefXMinTF.setEnabled(manualScalingX);
				asDefXMaxTF.setEnabled(manualScalingX);
				asDefXStapTF.setEnabled(manualScalingX);

				manualScalingY = false;
				interactiePanel.zetManualScalingX(manualScalingY);
				asDefYMinTF.setEnabled(manualScalingY);
				asDefYMaxTF.setEnabled(manualScalingY);
				asDefYStapTF.setEnabled(manualScalingY);
				
				interactiePanel.zetDragOptie(dragOptie);
				interactiePanel.zetZoomEnabled(zoomOptie);
			}
		}
		if(e.getSource().equals(xVarEditableCB))
		{	xVarEditable = xVarEditableCB.isSelected();
			interactiePanel.zetXVarEditable(xVarEditable);
		}
		if(e.getSource().equals(yVarEditableCB))
		{	yVarEditable = yVarEditableCB.isSelected();
			interactiePanel.zetYVarEditable(yVarEditable);
		}
		if(e.getSource().equals(snapToGridPointsCB))
		{	snapToGridPoints = snapToGridPointsCB.isSelected();
			interactiePanel.zetSnapToGridPoints(snapToGridPoints);
		}
		if(e.getSource().equals(krommeZonderExtrapolatieCB))
		{	krommeZonderExtrapolatie = krommeZonderExtrapolatieCB.isSelected();
			interactiePanel.zetKrommeKnoppen(rechteVerbindingen, krommeZonderExtrapolatie, krommeMetExtrapolatie);
		}
		if(e.getSource().equals(krommeMetExtrapolatieCB))
		{	krommeMetExtrapolatie = krommeMetExtrapolatieCB.isSelected();
			interactiePanel.zetKrommeKnoppen(rechteVerbindingen, krommeZonderExtrapolatie, krommeMetExtrapolatie);
		}
		if(e.getSource().equals(tekenGrafiekNauwkeurigheidTF))
		{
			try{
				int tgn = Integer.parseInt(tekenGrafiekNauwkeurigheidTF.getText());
				if(tgn > -1)
					tekenGrafiekNauwkeurigheid = tgn;
				else
					tekenGrafiekNauwkeurigheidTF.setText("" + tekenGrafiekNauwkeurigheid);
			}
			catch(Exception ex){
				tekenGrafiekNauwkeurigheidTF.setText("" + tekenGrafiekNauwkeurigheid);
			}
			interactiePanel.zetTekenGrafiekNauwkeurigheid(tekenGrafiekNauwkeurigheid);
		}
		if(e.getSource().equals(domeinControlerenBox))
		{	domeinControleren = domeinControlerenBox.isSelected();
			interactiePanel.zetDomeinControleren(domeinControleren, false);
		}
		if(e.getSource().equals(leerlingZietTabelBox))
		{	leerlingZietTabel = leerlingZietTabelBox.isSelected();
			interactiePanel.zetLeerlingZietTabel(leerlingZietTabel, false);
		}
		if(e.getSource().equals(tekenRechteCB))
		{
			rechteVerbindingen = tekenRechteCB.isSelected();
			interactiePanel.zetKrommeKnoppen(rechteVerbindingen, krommeZonderExtrapolatie, krommeMetExtrapolatie);
		}
		if(e.getSource().equals(tekenMetExtrapolatieCB))
		{	krommeMetExtrapolatie = tekenMetExtrapolatieCB.isSelected();
			interactiePanel.zetKrommeKnoppen(rechteVerbindingen, krommeZonderExtrapolatie, krommeMetExtrapolatie);
		}
		if(e.getSource().equals(tekenZonderExtrapolatieCB))
		{	krommeZonderExtrapolatie = tekenZonderExtrapolatieCB.isSelected();
			interactiePanel.zetKrommeKnoppen(rechteVerbindingen, krommeZonderExtrapolatie, krommeMetExtrapolatie);
		}
		if(e.getSource().equals(checkExternalCB))
		{	checkExternal = checkExternalCB.isSelected();
			interactiePanel.zetCheckExternal(checkExternal);
		}
		for(int i = 0; i < nauwkeurigheidTF.length; i++)
		if (e.getSource().equals(nauwkeurigheidTF[i]))
		{	processNauwkeurigheid();
		}
		for(int i = 0; i < minimumPuntenTF.length; i++)
		if (e.getSource().equals(minimumPuntenTF[i]))
		{	processMinimumPunten();
		}
		if (e.getSource().equals(scoreMaxTF))
		{	processMaxScore();
		}
		if (e.getSource().equals(docentFormuleComponent))
			{	
			if (e.getActionCommand().equals("ingevuld") ||
					e.getActionCommand().equals("focusLost") || e.getActionCommand().equals("verwijderd"))
					
			{	docentFuncties = interactiePanel.getDocentFuncties();
				docentFunctieStrings = interactiePanel.getDocentFunctieStrings();
				//hier ook ongelijkheden opvragen?
				interactiePanel.repaint();
			}
			if (typeOpdracht == VINDFORMULEBIJPUNTEN)
			{	if (docentFuncties == null || docentFuncties[0] == null)
				{
					for (int rCnt = 0; rCnt < interactiePanel.getPoints(interactiePanel.getActiveIndex(), true).size(); rCnt++)
					{
						RealPoint rp = (RealPoint) interactiePanel.getPoints(interactiePanel.getActiveIndex(), true).elementAt(rCnt);
						interactiePanel.removePoint(rp.getTabelIndex(), rp.getIndex(), true);
						rp.setY(0);
						rp.setyString("0");
						interactiePanel.addInsert(rp, true, false);
					}
				}
				else
				{
					for (int rCnt = 0; rCnt < interactiePanel.getPoints(interactiePanel.getActiveIndex(), true).size(); rCnt++)
					{
						RealPoint rp = (RealPoint) interactiePanel.getPoints(interactiePanel.getActiveIndex(), true).elementAt(rCnt);
						interactiePanel.removePoint(rp.getTabelIndex(), rp.getIndex(), true);
						rp.setY(docentFuncties[0].geefWaarde(rp.getX()));
						rp.setyString(Double.toString(rp.getY()));
						interactiePanel.addInsert(rp, true, false);
					}

				}
				docentTabelComponent.zetTabelPunten(interactiePanel.getPoints(interactiePanel.getActiveIndex(), true), true);
				interactiePanel.repaint();
			}
			if(e.getActionCommand().equals("regel meer"))
			{	if(typeOpdracht == VINDFORMULEBIJGRAFIEK)
				{	int aantalRegels = docentFormuleComponent.getAantalRegels() - 1;
					scoreMaxLabel[aantalRegels].setLocation(65, 240 + aantalRegels * (20 + offset));
					scoreMaxLabel[aantalRegels].setVisible(true);
					scoreMaxLabel[0].setText(GraphTool.rb.getString("Opdr_maximumScore") +" " + 1);
					scoreMaxTF[aantalRegels].setLocation(215, 240 + aantalRegels * (20 + offset));
					scoreMaxTF[aantalRegels].setVisible(true);
					scoreMaxTF[aantalRegels].setText("" + 10);
					domeinControlerenBox.setLocation(domeinControlerenBox.getLocation().x, 240 + docentFormuleComponent.getAantalRegels() *(20+offset));
					
				}
				else if(typeOpdracht == TEKENPUNTENBIJFORMULE)
				{	int maxY = 260;
					int aantalRegels = docentFormuleComponent.getAantalRegels() - 1;
					nauwkeurigheidTF[aantalRegels].setLocation(nauwkeurigheidTF[aantalRegels].getLocation().x, maxY);
					nauwkeurigheidTF[aantalRegels].setVisible(true);
					nauwkeurigheidTF[aantalRegels].setText("" + 5);
					maxY += 30;
					minimumPuntenTF[aantalRegels].setLocation(minimumPuntenTF[aantalRegels].getLocation().x, maxY);
					minimumPuntenTF[aantalRegels].setVisible(true);
					minimumPuntenTF[aantalRegels].setText("" + 5);
					maxY += 30;
					scoreMaxTF[aantalRegels].setLocation(215 + aantalRegels * (50 + 2 * offset), maxY);
					scoreMaxTF[aantalRegels].setVisible(true);
					scoreMaxTF[aantalRegels].setText("" + 10);
				}
			}
			else if(e.getActionCommand().equals("regel minder"))
				{	int aantalRegels = docentFormuleComponent.getAantalRegels();
					scoreMaxLabel[aantalRegels].setVisible(false);
					scoreMaxTF[aantalRegels].setVisible(false);
					if(aantalRegels < nauwkeurigheidTF.length)
					{	nauwkeurigheidTF[aantalRegels].setVisible(false);
						minimumPuntenTF[aantalRegels].setVisible(false);
					}
					if(aantalRegels == 1)
						scoreMaxLabel[0].setText(GraphTool.rb.getString("Opdr_maximumScore"));		
					domeinControlerenBox.setLocation(domeinControlerenBox.getLocation().x, 240 + docentFormuleComponent.getAantalRegels() *(20+offset));
					
				}
		}
		if (e.getSource().equals(docentTabelComponent))
		{	if(typeOpdracht == TEKENTABELPUNTEN)
			{	if (e.getActionCommand().equals("points changed"))
				{	interactiePanel.getTabelComponent().zetTabelPunten(interactiePanel.getPoints(interactiePanel.getActiveIndex(), true), false);
					docentTabelComponent.zetTabelPunten(interactiePanel.getPoints(interactiePanel.getActiveIndex(), true), false);
				}	
				else if (e.getActionCommand().equals("pijl links"))
				{	interactiePanel.getTabelComponent().pijlLinksAction("leerling");
				}
				else if (e.getActionCommand().equals("pijl rechts"))
				{	interactiePanel.getTabelComponent().pijlRechtsAction();
				}	
			}
			else if (typeOpdracht == VINDFORMULEBIJPUNTEN && e.getActionCommand().equals("points changed"))
			{	docentFunctieStrings[0] = interactiePanel.getDocentFunctieStrings()[0];
				if (!docentFunctieStrings[0].equals("$f@"))
				{	docentFuncties[0] = FormuleParser.geefExpressie(docentFunctieStrings[0]);
				}
				if (docentFuncties[0] == null) {	
					for (int rCnt = 0; rCnt < interactiePanel.getPoints(interactiePanel.getActiveIndex(), true).size(); rCnt++)
					{
						RealPoint rp = (RealPoint) interactiePanel.getPoints(interactiePanel.getActiveIndex(), true).elementAt(rCnt);
						interactiePanel.removePoint(rp.getTabelIndex(), rp.getIndex(), true);
						rp.setY(0);
						rp.setyString("0");
						interactiePanel.addInsert(rp, true, false);
					}
				}
				else {
					for (int rCnt = 0; rCnt < interactiePanel.getPoints(interactiePanel.getActiveIndex(), true).size(); rCnt++) {	
						RealPoint rp = (RealPoint) interactiePanel.getPoints(interactiePanel.getActiveIndex(), true).elementAt(rCnt);

						interactiePanel.removePoint(rp.getTabelIndex(), rp.getIndex(), true);
						rp.setY(docentFuncties[0].geefWaarde(rp.getX()));
						rp.setyString(Double.toString(rp.getY()));
						interactiePanel.addInsert(rp, true, false);
					}
				}
				docentTabelComponent.zetTabelPunten(interactiePanel.getPoints(interactiePanel.getActiveIndex(), true), false);
				
				interactiePanel.repaint();
			}
		}
		if(e.getSource().equals(paramVerwijderButton))
		{
			int selectedIndex = schuifParamList.getSelectedIndex();
			if(selectedIndex < 0)
				return;
			schuifParamListElements.remove(selectedIndex);
			interactiePanel.remove(interactiePanel.schuifParameters[selectedIndex].geefSlider());
			SchuifParameter[] parameters = new SchuifParameter[interactiePanel.schuifParameters.length - 1];
			for(int i = 0; i < selectedIndex; i++)
				parameters[i] = interactiePanel.schuifParameters[i];
			for(int i = selectedIndex; i < parameters.length; i++)
				parameters[i] = interactiePanel.schuifParameters[i + 1];
			interactiePanel.schuifParameters = parameters;
			interactiePanel.repaint();
		}
		
//		if ( (e.getSource().equals(schaalXMinTF)) || (e.getSource().equals(schaalXMaxTF)) || (e.getSource().equals(schaalXStapTF)) ||
//			 (e.getSource().equals(schaalYMinTF)) || (e.getSource().equals(schaalYMaxTF)) || (e.getSource().equals(schaalYStapTF))
//		   ) {
		if ( e.getSource().equals(asDefXMinTF) ) {
			try {  
				asDefXMin = Double.parseDouble(asDefXMinTF.getText());
			}  
			catch(NumberFormatException nfe) {  
				asDefXMin = asDefXMax-10*asDefXStap;
				asDefXMinTF.setText(String.valueOf(asDefXMin));
				JOptionPane.showMessageDialog(WiskOpdr.applet, GraphTool.rb.getString("GTIEP_fout_geenGetal"));
			}  
			
			if (asDefXMin>=asDefXMax) {
				asDefXMin = asDefXMax-10*asDefXStap;
				asDefXMinTF.setText(String.valueOf(asDefXMin));
				JOptionPane.showMessageDialog(WiskOpdr.applet, GraphTool.rb.getString("GTIEP_fout_minGroterGelijkMax"));
			}
			interactiePanel.zetAssenDefinitie(asDefXMin, asDefXMax, asDefXStap, asDefYMin, asDefYMax, asDefYStap);
        }
		if ( e.getSource().equals(asDefXMaxTF) ) {
			try {  
				asDefXMax = Double.parseDouble(asDefXMaxTF.getText());
			}  
			catch(NumberFormatException nfe) {  
				asDefXMax = asDefXMin+10*asDefXStap;
				asDefXMaxTF.setText(String.valueOf(asDefXMax));
				JOptionPane.showMessageDialog(WiskOpdr.applet, GraphTool.rb.getString("GTIEP_fout_geenGetal"));
			}  
			if (asDefXMax<=asDefXMin) {
				asDefXMax = asDefXMin+10*asDefXStap;
				asDefXMaxTF.setText(String.valueOf(asDefXMax));
				JOptionPane.showMessageDialog(WiskOpdr.applet, GraphTool.rb.getString("GTIEP_fout_maxKleinerGelijkMin"));
			}
			interactiePanel.zetAssenDefinitie(asDefXMin, asDefXMax, asDefXStap, asDefYMin, asDefYMax, asDefYStap);
        }
		if ( e.getSource().equals(asDefXStapTF) ) {
			try {  
				asDefXStap = Double.parseDouble(asDefXStapTF.getText());
			}  
			catch(NumberFormatException nfe) {  
				asDefXStapTF.setText(String.valueOf((asDefXMax-asDefXMin)/10));
				asDefXStap = Double.parseDouble(asDefXStapTF.getText());
				JOptionPane.showMessageDialog(WiskOpdr.applet, GraphTool.rb.getString("GTIEP_fout_geenGetal"));
			}  
			if (asDefXStap<=0) {
				asDefXStapTF.setText(String.valueOf((asDefXMax-asDefXMin)/10));
				asDefXStap = Double.parseDouble(asDefXStapTF.getText());
				JOptionPane.showMessageDialog(WiskOpdr.applet, GraphTool.rb.getString("GTIEP_fout_stapKleinerGelijk0"));
			}
			interactiePanel.zetAssenDefinitie(asDefXMin, asDefXMax, asDefXStap, asDefYMin, asDefYMax, asDefYStap);
        }
		if ( e.getSource().equals(asDefYMinTF) ) {
			try {  
				asDefYMin = Double.parseDouble(asDefYMinTF.getText());
			}  
			catch(NumberFormatException nfe) {  
				asDefYMin = asDefYMax-5*asDefYStap;
				asDefYMinTF.setText(String.valueOf(asDefYMin));
				JOptionPane.showMessageDialog(WiskOpdr.applet, GraphTool.rb.getString("GTIEP_fout_geenGetal"));
			}  
			if (asDefYMin>=asDefYMax) {
				asDefYMin = asDefYMax-5*asDefYStap;
				asDefYMinTF.setText(String.valueOf(asDefYMin));
				JOptionPane.showMessageDialog(WiskOpdr.applet, GraphTool.rb.getString("GTIEP_fout_minGroterGelijkMax"));
			}
			interactiePanel.zetAssenDefinitie(asDefXMin, asDefXMax, asDefXStap, asDefYMin, asDefYMax, asDefYStap);
        }
		if ( e.getSource().equals(asDefYMaxTF) ) {
			try {  
				asDefYMax = Double.parseDouble(asDefYMaxTF.getText());
			}  
			catch(NumberFormatException nfe) {  
				asDefYMax = asDefYMin+5*asDefYStap;
				asDefYMaxTF.setText(String.valueOf(asDefYMax));
				JOptionPane.showMessageDialog(WiskOpdr.applet, GraphTool.rb.getString("GTIEP_fout_geenGetal"));
			}  
			if (asDefYMax<=asDefYMin) {
				asDefYMax = asDefYMin+5*asDefYStap;
				asDefYMaxTF.setText(String.valueOf(asDefYMax));
				JOptionPane.showMessageDialog(WiskOpdr.applet, GraphTool.rb.getString("GTIEP_fout_maxKleinerGelijkMin"));
			}
			interactiePanel.zetAssenDefinitie(asDefXMin, asDefXMax, asDefXStap, asDefYMin, asDefYMax, asDefYStap);
        }
		if ( e.getSource().equals(asDefYStapTF) ) {
			try {  
				asDefYStap = Double.parseDouble(asDefYStapTF.getText());
			}  
			catch(NumberFormatException nfe) {  
				asDefYStapTF.setText(String.valueOf((asDefYMax-asDefYMin)/5));
				asDefYStap = Double.parseDouble(asDefYStapTF.getText());
				JOptionPane.showMessageDialog(WiskOpdr.applet, GraphTool.rb.getString("GTIEP_fout_geenGetal"));
			}  
			if (asDefYStap<=0) {
				asDefYStapTF.setText(String.valueOf((asDefYMax-asDefYMin)/5));
				asDefYStap = Double.parseDouble(asDefYStapTF.getText());
				JOptionPane.showMessageDialog(WiskOpdr.applet, GraphTool.rb.getString("GTIEP_fout_stapKleinerGelijk0"));
			}
			interactiePanel.zetAssenDefinitie(asDefXMin, asDefXMax, asDefXStap, asDefYMin, asDefYMax, asDefYStap);
        }

	}
	
	public void focusGained(FocusEvent e)
	{}
	
	public void focusLost(FocusEvent e) {	
		boolean defaultBehaviour = true;
		if(e.getSource().equals(tekenGrafiekNauwkeurigheidTF)) {
			defaultBehaviour = false;
			try{
				int tgn = Integer.parseInt(tekenGrafiekNauwkeurigheidTF.getText());
				if(tgn > -1)
					tekenGrafiekNauwkeurigheid = tgn;
				else
					tekenGrafiekNauwkeurigheidTF.setText("" + tekenGrafiekNauwkeurigheid);
			}
			catch(Exception ex){
				tekenGrafiekNauwkeurigheidTF.setText("" + tekenGrafiekNauwkeurigheid);
			}
			interactiePanel.zetTekenGrafiekNauwkeurigheid(tekenGrafiekNauwkeurigheid);
		}
		if ( e.getSource().equals(asDefXMinTF) ) {
			defaultBehaviour = false;
			try {  
				asDefXMin = Double.parseDouble(asDefXMinTF.getText());
			}  
			catch(NumberFormatException nfe) {  
				asDefXMin = asDefXMax-10*asDefXStap;
				asDefXMinTF.setText(String.valueOf(asDefXMin));
				JOptionPane.showMessageDialog(WiskOpdr.applet, GraphTool.rb.getString("GTIEP_fout_geenGetal"));
			}  
		
			if (asDefXMin>=asDefXMax) {
				asDefXMin = asDefXMax-10*asDefXStap;
				asDefXMinTF.setText(String.valueOf(asDefXMin));
				JOptionPane.showMessageDialog(WiskOpdr.applet, GraphTool.rb.getString("GTIEP_fout_minGroterGelijkMax"));
			}
			interactiePanel.zetAssenDefinitie(asDefXMin, asDefXMax, asDefXStap, asDefYMin, asDefYMax, asDefYStap);
        }
		if ( e.getSource().equals(asDefXMaxTF) ) {
			defaultBehaviour = false;
			try {  
				asDefXMax = Double.parseDouble(asDefXMaxTF.getText());
			}  
			catch(NumberFormatException nfe) {  
				asDefXMax = asDefXMin+10*asDefXStap;
				asDefXMaxTF.setText(String.valueOf(asDefXMax));
				JOptionPane.showMessageDialog(WiskOpdr.applet, GraphTool.rb.getString("GTIEP_fout_geenGetal"));
			}  
			if (asDefXMax<=asDefXMin) {
				asDefXMax = asDefXMin+10*asDefXStap;
				asDefXMaxTF.setText(String.valueOf(asDefXMax));
				JOptionPane.showMessageDialog(WiskOpdr.applet, GraphTool.rb.getString("GTIEP_fout_maxKleinerGelijkMin"));
			}
			interactiePanel.zetAssenDefinitie(asDefXMin, asDefXMax, asDefXStap, asDefYMin, asDefYMax, asDefYStap);
        }
		if ( e.getSource().equals(asDefXStapTF) ) {
			defaultBehaviour = false;
			try {  
				asDefXStap = Double.parseDouble(asDefXStapTF.getText());
			}  
			catch(NumberFormatException nfe) {  
				asDefXStapTF.setText(String.valueOf((asDefXMax-asDefXMin)/10));
				asDefXStap = Double.parseDouble(asDefXStapTF.getText());
				JOptionPane.showMessageDialog(WiskOpdr.applet, GraphTool.rb.getString("GTIEP_fout_geenGetal"));
			}  
			if (asDefXStap<=0) {
				asDefXStapTF.setText(String.valueOf((asDefXMax-asDefXMin)/10));
				asDefXStap = Double.parseDouble(asDefXStapTF.getText());
				JOptionPane.showMessageDialog(WiskOpdr.applet, GraphTool.rb.getString("GTIEP_fout_stapKleinerGelijk0"));
			}
			interactiePanel.zetAssenDefinitie(asDefXMin, asDefXMax, asDefXStap, asDefYMin, asDefYMax, asDefYStap);
        }
		if ( e.getSource().equals(asDefYMinTF) ) {
			defaultBehaviour = false;
			try {  
				asDefYMin = Double.parseDouble(asDefYMinTF.getText());
			}  
			catch(NumberFormatException nfe) {  
				asDefYMin = asDefYMax-5*asDefYStap;
				asDefYMinTF.setText(String.valueOf(asDefYMin));
				JOptionPane.showMessageDialog(WiskOpdr.applet, GraphTool.rb.getString("GTIEP_fout_geenGetal"));
			}  
			if (asDefYMin>=asDefYMax) {
				asDefYMin = asDefYMax-5*asDefYStap;
				asDefYMinTF.setText(String.valueOf(asDefYMin));
				JOptionPane.showMessageDialog(WiskOpdr.applet, GraphTool.rb.getString("GTIEP_fout_minGroterGelijkMax"));
			}
			interactiePanel.zetAssenDefinitie(asDefXMin, asDefXMax, asDefXStap, asDefYMin, asDefYMax, asDefYStap);
        }
		if ( e.getSource().equals(asDefYMaxTF) ) {
			defaultBehaviour = false;
			try {  
				asDefYMax = Double.parseDouble(asDefYMaxTF.getText());
			}  
			catch(NumberFormatException nfe) {  
				asDefYMax = asDefYMin+5*asDefYStap;
				asDefYMaxTF.setText(String.valueOf(asDefYMax));
				JOptionPane.showMessageDialog(WiskOpdr.applet, GraphTool.rb.getString("GTIEP_fout_geenGetal"));
			}  
			if (asDefYMax<=asDefYMin) {
				asDefYMax = asDefYMin+5*asDefYStap;
				asDefYMaxTF.setText(String.valueOf(asDefYMax));
				JOptionPane.showMessageDialog(WiskOpdr.applet, GraphTool.rb.getString("GTIEP_fout_maxKleinerGelijkMin"));
			}
			interactiePanel.zetAssenDefinitie(asDefXMin, asDefXMax, asDefXStap, asDefYMin, asDefYMax, asDefYStap);
        }
		if ( e.getSource().equals(asDefYStapTF) ) {
			defaultBehaviour = false;
			try {  
				asDefYStap = Double.parseDouble(asDefYStapTF.getText());
			}  
			catch(NumberFormatException nfe) {  
				asDefYStapTF.setText(String.valueOf((asDefYMax-asDefYMin)/5));
				asDefYStap = Double.parseDouble(asDefYStapTF.getText());
				JOptionPane.showMessageDialog(WiskOpdr.applet, GraphTool.rb.getString("GTIEP_fout_geenGetal"));
			}  
			if (asDefYStap<=0) {
				asDefYStapTF.setText(String.valueOf((asDefYMax-asDefYMin)/5));
				asDefYStap = Double.parseDouble(asDefYStapTF.getText());
				JOptionPane.showMessageDialog(WiskOpdr.applet, GraphTool.rb.getString("GTIEP_fout_stapKleinerGelijk0"));
			}
			interactiePanel.zetAssenDefinitie(asDefXMin, asDefXMax, asDefXStap, asDefYMin, asDefYMax, asDefYStap);
        }
		if (defaultBehaviour) {
			processNauwkeurigheid();
			processMinimumPunten();
			processMaxScore();
		}
	}
	
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
