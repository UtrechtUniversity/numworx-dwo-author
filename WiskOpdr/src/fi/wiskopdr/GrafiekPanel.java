package fi.wiskopdr;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.ScrollPane;
import java.awt.AWTEventMulticaster;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Vector;

import javax.swing.*;

import fi.wiskopdr.formuleobjects.EditorContentPanel;
import fi.wiskopdr.formuleobjects.FormuleVakHouder;
import fi.wiskopdr.formuleobjects.Tablet;
import fi.wiskopdr.formuleobjects.TabletOwner;
import fi.wiskopdr.formuleobjects.FormuleButton;
import fi.wiskopdr.formuleobjects.FormuleParser;
import fi.beans.mainframe.AppletStub;
import fi.beans.stringutils.StringUtils;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;
import fi.beans.wiskopdrbeans.WiskOpdrApplet;

import fi.wiskopdr.expressies.Algebra;
import fi.wiskopdr.expressies.Expressie;

public class GrafiekPanel extends JPanel implements  ActionListener, InteractiePanel, InteractieEditPanel, WiskOpdrApplet
{	
	final double NZero = 1e-2d;
	
	public boolean resized = true;
	

	private ScrollPane scrollPane;
	private EditorContentPanel contentPane; 
	
	private int startY;
	 
	private FunctieEditor functieEditor;
	private GrafiekComponent grafiekComponent;
	private GrafiekTekenEditor grafiekTekenEditor;
	private TabelComponent tabelComponent;
	
	//private JCheckBox formuleVisibleCheckbox;
	private Font font = WiskOpdr.tekstFont; //new Font("SansSerif", Font.PLAIN,12);
	private Tablet tablet;
	private boolean tabletAdded;
	private FormuleVakHouder tabletUser;
	
	//private JTextField varField;
	//private String varNaam  = "x";
	
	// gebruik dit voor verschil leerling/docent
	private boolean edit;
	//private boolean formulesZichtbaar = true;
	
	private String[] randomVars;
	private Hashtable randomValues;

	private String yAsNaam = "y";

	private boolean assen = true;
	private boolean rooster = true;
	private boolean roosterGrof = false;
	private boolean schaal = true;
	private boolean piLijnen = false;

	private boolean grafiekTekenen = false;	
	private boolean tabelZichtbaar = false;

	private boolean formulesZichtbaar = true;
	private boolean zoomOptie = true;
	private boolean traceOptie = true;
	private boolean dragOptie = true;
	private boolean isButton = false;
	
	private boolean zoomInTabel = true;
	private boolean tabelAlsTekenTool = false;
	
	private FormuleButton button;
	private JDialog frame;
	private JPanel basisPanel;
	
	private boolean newVersion = false;
	private boolean grafiekKleuren = true;

	// opdrachten
	private int typeOpdracht;
	private int scoreMax;
	private int nauwkeurigheid;
	private int minimumPunten;
	private Expressie docentExpressie;
	private String docentExpressieString;
	private Vector docentGrafiekPunten;
	private Vector docentTabelPunten;	
	private Vector docentTabelPuntVakIndex;
	private Vector docentTabelStringsX;
	private Vector docentTabelStringsY;
	private Vector docentTabelPuntenRandomized;
	
	private int mode = 0;		
	private boolean correct = true;
	private boolean fout = false;
	private int score;
	private boolean nagekeken = false;
	private boolean ingevuld = false;

	JButton kijkNaButton;
	JLabel groenVinkjeLabel, kruisjeLabel;
	JPanel kijkNaPanel;
	Icon goedkrulIcon, foutkruisIcon;
	
	
	public GrafiekPanel()
	{	
		super();
		setLayout(null);
		setBackground(WiskOpdr.bgcolor);
		
		basisPanel = new JPanel();
		basisPanel.setLayout(null);
		basisPanel.setOpaque(false);
		add(basisPanel);
		
		grafiekComponent = new GrafiekComponent(0,0,270,270);
		grafiekComponent.setBackground(WiskOpdr.bgcolor);
		basisPanel.add(grafiekComponent);

		grafiekTekenEditor = new GrafiekTekenEditor();
		if (newVersion)
			grafiekTekenEditor.setBounds(3,270,getSize().width-5, 24);
		else 
			grafiekTekenEditor.setBounds(50,270,getSize().width-50, 24);
		basisPanel.add(grafiekTekenEditor,0);
		grafiekTekenEditor.zetGrafiekComponent(grafiekComponent);
		grafiekTekenEditor.setVisible(grafiekTekenen);
		grafiekComponent.zetGrafiekTekenEditor(grafiekTekenEditor);		
	
	
		tabelComponent = new TabelComponent(270);
		tabelComponent.setLocation(0, 270);
		basisPanel.add(tabelComponent, 0);
		tabelComponent.setVisible(tabelZichtbaar);
		
		// toevoegen aan functieEditor
						
		functieEditor = new FunctieEditor(true);
//		functieEditor.setBounds(50,270,getSize().width-50, 120);
		if (newVersion)
			functieEditor.setBounds(3,294,getSize().width-5, 120);
		else 
			functieEditor.setBounds(50,294,getSize().width-50, 120);
		functieEditor.zetRandverhoging(false);
		basisPanel.add(functieEditor,0);
		functieEditor.zetFuncties();
		functieEditor.zetGrafiekComponent(grafiekComponent);
		
		if (!tabelAlsTekenTool)
		{	functieEditor.zetTabelComponent(tabelComponent);
			tabelComponent.zetGrafiekComponent(null);
			grafiekComponent.removeTabelComponent();		
		}
		else
		{	functieEditor.zetTabelComponent(null);
			tabelComponent.zetGrafiekComponent(grafiekComponent);
			grafiekComponent.zetTabelComponent(tabelComponent);		
		}
		functieEditor.requestFocus();
		functieEditor.addActionListener(this);

		button = new FormuleButton("uitleg");
		button.setBounds(0,0,20,20);
		button.addActionListener(this);
		
		createFrame();

		docentExpressie = null;
		docentGrafiekPunten = new Vector();
		docentTabelPunten = new Vector();	
		docentTabelPuntVakIndex = new Vector();
		docentTabelStringsX = new Vector();
		docentTabelStringsY = new Vector();
		docentTabelPuntenRandomized = new Vector();
		
		java.net.URL imageURL = WiskOpdr.class.getResource("resources/goedkrul_en.gif");
		if (imageURL != null) 
		{
		    goedkrulIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading goedkrul_en.gif.");
		}
		imageURL = WiskOpdr.class.getResource("resources/foutkruis.gif");
		if (imageURL != null) 
		{
			foutkruisIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading foutkruis.gif");
		}
		
		Font theFont = new Font("SansSerif", Font.PLAIN, 12);
		FontMetrics theFM = getFontMetrics(theFont);
		
		kijkNaButton = new JButton(WiskOpdr.rb.getString("GE_kijkNa"));
		kijkNaButton.setFont(theFont);
		kijkNaButton.setBounds(0, 0, 75, 24);
		kijkNaButton.addActionListener(this);
		kijkNaButton.setEnabled(false);
		
	    groenVinkjeLabel = new JLabel(goedkrulIcon);
		groenVinkjeLabel.setBounds(76, 2, 20, 20);
		
	    kruisjeLabel = new JLabel(foutkruisIcon);
		kruisjeLabel.setBounds(76, 2, 20, 20);
		
		groenVinkjeLabel.setVisible(false);
		kruisjeLabel.setVisible(false);
		
		kijkNaPanel = new JPanel(null);
		kijkNaPanel.setOpaque(false);
		
		int height = 3 * theFM.getHeight() / 2;

//System.out.println("bpw = " + basisPanel.getSize().width);		
		kijkNaPanel.setSize(95, height);
		
		kijkNaPanel.add(kijkNaButton);
		kijkNaPanel.add(groenVinkjeLabel);
		kijkNaPanel.add(kruisjeLabel);
		kijkNaPanel.setVisible(false);
		basisPanel.add(kijkNaPanel);
		
	}

	private void createFrame() {
		frame = new JDialog(WiskOpdr.getFrame(), "Graphs", false);
		frame.getContentPane().setLayout(null);
		frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e)
			{   frame.setVisible(false);
			}
		});
		frame.addComponentListener(new ComponentAdapter(){
			public void componentResized(ComponentEvent e)
			{   int x = 0;//frame.getInsets().left;
				int y = 0;//frame.getInsets().top;
				int b = frame.getSize().width - frame.getInsets().left - frame.getInsets().right;
				int h = frame.getSize().height - frame.getInsets().top - frame.getInsets().bottom;
				basisPanel.setBounds(x,y,b,h);
				resize();
			}
		});
	}	
	
	public void setPopupView(boolean b)
	{	grafiekComponent.setPopupView(b);
	}
	
	public void setButton(boolean b)
	{	isButton = b;
		grafiekComponent.setPopupView(b);
		if(b)
		{	add(button);
			int breedte = basisPanel.getSize().width + frame.getInsets().left + frame.getInsets().right;
			int hoogte = basisPanel.getSize().height + frame.getInsets().top + frame.getInsets().bottom;
			frame.setSize(breedte,hoogte);
			frame.getContentPane().add(basisPanel);
			
		}
		else
		{	remove(button);
			add(basisPanel);
			frame.setVisible(false);
		}
	}

	public GrafiekComponent getGrafiekComponent()
	{	return grafiekComponent;
	}	
	
	public FunctieEditor getFunctieEditor()
	{	return functieEditor;	
	}

	public TabelComponent getTabelComponent()
	{	return tabelComponent;
	}

	public GrafiekTekenEditor getGrafiekTekenEditor()
	{	return grafiekTekenEditor;
	}

	public void zetEdit(boolean b)
	{	edit = b;
	}
	
	public void zetVarNaam(String varNaam)
	{	// dit zet ook de varNaam van de GrafiekComponent
		functieEditor.zetVarNaam(varNaam);
		tabelComponent.zetVarNaam(varNaam);
	}

	public void zetYAsNaam(String yAsN)
	{	yAsNaam = yAsN;
		// dit zet ook de yAsNaam van de GrafiekComponent	
		functieEditor.zetYAsLabel(yAsNaam);
		tabelComponent.zetYNaam(yAsNaam);
	}
	
	public void zetFormalFunction(boolean b)
	{	functieEditor.zetFormalFunction(b);
	}
	
	public void zetAssen(boolean b)
	{	assen = b;
		grafiekComponent.zetAssen(b);
	}

	public void zetRooster(boolean b)
	{	rooster = b;
		grafiekComponent.zetRooster(b);
	}
	
	public void zetRoosterGrof(boolean b)
	{	roosterGrof = b;
		grafiekComponent.zetRoosterGrof(b);
	}

	public void zetSchaal(boolean b)
	{	schaal = b;
		grafiekComponent.zetSchaal(b);
	}

	public void zetPiLijnen(boolean b)
	{	piLijnen = b;
		grafiekComponent.zetPiLijnen(b);
	}
	
	public void zetGrafiekTekenen(boolean b)
	{	grafiekTekenen = b;
		grafiekTekenEditor.setVisible(b);
		if (grafiekTekenen)
			grafiekComponent.zetGrafiekTekenEditor(grafiekTekenEditor);
		else
			grafiekComponent.removeGrafiekTekenEditor();
		resize();
	}

	public void zetTabelZichtbaar(boolean b)
	{	tabelZichtbaar = b;
		zetTabelAlsTekenTool(tabelAlsTekenTool);
		tabelComponent.setVisible(b);
		resize();
	}

	public void zetZoomInTabel(boolean b)
	{	zoomInTabel = b;	
		tabelComponent.zetZooming(b);
	}
	
	public void zetTabelAlsTekenTool(boolean b)
	{	tabelAlsTekenTool = b;
		if (b)
		{	functieEditor.zetTabelComponent(null);
			tabelComponent.zetGrafiekComponent(grafiekComponent);
			grafiekComponent.zetTabelComponent(tabelComponent);
//System.out.println("ttool = true");			
		}	
		else
		{	functieEditor.zetTabelComponent(tabelComponent);
			tabelComponent.zetGrafiekComponent(null);
			grafiekComponent.removeTabelComponent();
		}
		tabelComponent.zetAlsTekenTool(b);
	}

	public void zetFormulesZichtbaar(boolean b)
	{	
//System.out.println("fz = " + b);		
		formulesZichtbaar = b;
		functieEditor.setVisible(b);
		resize();
	}
	
	public void zetGrafiekKleuren(boolean b)
	{	grafiekKleuren = b;
		grafiekComponent.zetGrafiekKleuren(b);
	}

	public void zetKijkNaButton(boolean b)
	{	kijkNaPanel.setVisible(b);
		resize();
	}
	
	public void zetZoomOptie(boolean b)
	{	zoomOptie = b;
		resize();
	}
	
	public void zetTraceOptie(boolean b)
	{	traceOptie = b;
		grafiekComponent.setTrace(b);
		resize();
	}
	
	public void zetDragOptie(boolean b)
	{	dragOptie = b;
		grafiekComponent.setDrag(b);
		repaint();
	}
	
	public void zetNewVersion(boolean b)
	{	newVersion = b;
		grafiekComponent.zetNewVersion(b);
		setBounds(getBounds());
		repaint();
	}
	
	public void zetXPositief(boolean b)
	{	grafiekComponent.zetXPositief(b);
		repaint();
	}
	public void zetYPositief(boolean b)
	{	grafiekComponent.zetYPositief(b);
		repaint();
	}
	public void zetXVarEditable(boolean b)
	{	grafiekComponent.zetXVarEditable(b);
	}
	public void zetYVarEditable(boolean b)
	{	grafiekComponent.zetYVarEditable(b);
	}

	// zet hier de instellingen die specifiek zijn
	// per opdracht en die niet in de states bewaard worden	
	public void zetTypeOpdracht(int type, boolean zetOpdracht)
	{	typeOpdracht = type;
		if (typeOpdracht == GrafiekEditPanel.GEENOPDRACHT)
		{	functieEditor.zetMaxAantalFuncties(9);
			grafiekComponent.zetDocentExpressie(null);
			//grafiekComponent.removeGrafiekTekenEditor();
			
			zetZoomInTabel(true);
			//tabelComponent.reset();
			
			grafiekTekenEditor.zetEenGrafiek(false);			
		}
		else if (typeOpdracht == GrafiekEditPanel.FORMULEBIJGRAFIEK)
		{	functieEditor.zetMaxAantalFuncties(1);
			if (newVersion)
				functieEditor.setSize(getSize().width-5, 100);
			else 
				functieEditor.setSize(getSize().width-50, 100);
			grafiekComponent.zetDocentExpressie(docentExpressie);
//if (docentExpressie == null)
//System.out.println("de null");			
		}
		else if (typeOpdracht == GrafiekEditPanel.FORMULEBIJPUNTEN)
		{	functieEditor.zetMaxAantalFuncties(1);
			if (newVersion)
				functieEditor.setSize(getSize().width-5, 100);
			else 
				functieEditor.setSize(getSize().width-50, 100);
			
			if (zetOpdracht && 
				(docentExpressie != null))
			{
				for (int rCnt = 0; rCnt < docentTabelPunten.size(); rCnt++)
				{
					RealPoint rp = (RealPoint) docentTabelPuntenRandomized.elementAt(rCnt);
					rp.y = docentExpressie.geefWaarde(rp.x);
				}
				
			}
			
			
			// deze is onzichtbaar
			tabelComponent.zetTabelStringsX(docentTabelStringsX);
			tabelComponent.zetTabelStringsY(docentTabelStringsY);
			tabelComponent.zetDocentTabelPuntVakIndex(docentTabelPuntVakIndex);
			if (zetOpdracht)
				tabelComponent.zetDocentTabelPunten(docentTabelPuntenRandomized, false);
			else
				tabelComponent.zetDocentTabelPunten(docentTabelPunten, true);
			// maar je kan de punten wel tekenen!
			tabelComponent.zetGrafiekComponent(grafiekComponent);
			grafiekComponent.zetTabelComponent(tabelComponent);
			
			
		}
		else if (typeOpdracht == GrafiekEditPanel.TEKENTABELPUNTEN)
		{	
			zetZoomInTabel(false);
			if (!zetOpdracht)
				tabelComponent.setRandomAllowed(true);
			
			tabelComponent.zetEenTabel(true);
			tabelComponent.zetTabelStringsX(docentTabelStringsX);
			tabelComponent.zetTabelStringsY(docentTabelStringsY);
			tabelComponent.zetDocentTabelPuntVakIndex(docentTabelPuntVakIndex);
			
			tabelComponent.zetReset(false);
//System.out.println("dtpvi = " + docentTabelPuntVakIndex.size());			
			
// Hier ontrandomiseren
			if (zetOpdracht)
			{	tabelComponent.zetDocentTabelPunten(docentTabelPuntenRandomized, false);
			}
			else
				tabelComponent.zetDocentTabelPunten(docentTabelPunten, true);		
			
//System.out.println("dtp = " + docentTabelPunten.size());

			grafiekTekenEditor.zetEenGrafiek(true);			
			grafiekTekenEditor.zetAlleenPunten(true);			
			//grafiekTekenEditor.zetGrafiekComponent(grafiekComponent);
			//grafiekComponent.zetGrafiekTekenEditor(grafiekTekenEditor);
			grafiekTekenEditor.addActionListener(this);
			
			grafiekComponent.removeTabelComponent();
			
			zetKijkNaButton(true);
			
		}
		else if (typeOpdracht == GrafiekEditPanel.PUNTENBIJFORMULE)
		{	grafiekTekenEditor.zetEenGrafiek(true);			
			grafiekTekenEditor.zetAlleenPunten(true);
			//grafiekTekenEditor.zetGrafiekComponent(grafiekComponent);
			//grafiekComponent.zetGrafiekTekenEditor(grafiekTekenEditor);
			grafiekTekenEditor.addActionListener(this);
			
			zetKijkNaButton(true);
			
		}

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
	
	public void zetMaxScore(int ms)
	{	scoreMax = ms;
	}

	public void zetNauwkeurigheid(int ms)
	{	nauwkeurigheid = ms;
	}

	public void zetMinimumPunten(int ms)
	{	minimumPunten = ms;
	}
	
	public void zetDocentExpressie(Expressie docentExp)
	{	docentExpressie = docentExp;
		if (docentExpressie != null)
			docentExpressieString = "$f" + docentExpressie.toString() + "@";
		else
			docentExpressieString = "$f@";	
	}
	
	public void zetDocentGrafiekPunten(Vector docentGrafiekPts)
	{	docentGrafiekPunten = docentGrafiekPts;
	}

	public void zetDocentTabelPunten(Vector docentTabelPts)
	{	docentTabelPunten = docentTabelPts;
	}

	public void zetDocentTabelStringsX(Vector docentTabelStrgsX)
	{	docentTabelStringsX = docentTabelStrgsX;
	}

	public void zetDocentTabelStringsY(Vector docentTabelStrgsY)
	{	docentTabelStringsY = docentTabelStrgsY;
	}
	
	public void zetDocentTabelPuntVakIndex(Vector docentTabelPuntVakInd)
	{	docentTabelPuntVakIndex = docentTabelPuntVakInd;
	}
	
	/*public void paint(Graphics g)
	{	{ 	if(im==null)
			{	//im = createImage(image.getWidth(null),image.getHeight(null));
				im = createImage(getSize().width,getSize().height);
  				gIm = im.getGraphics();
  				resized = false;
			}
			gIm.setColor(getBackground());
			gIm.fillRect(0,0,getSize().width,getSize().height);
			super.paint(gIm);
			g.drawImage(im, 0, 0, null);
  		}
	}
	
	public void update(Graphics g)
	{	paint(g);
	}*/
	
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
	
	// hier de feedback
	public void actionPerformed(ActionEvent e)
	{	
		if (typeOpdracht == GrafiekEditPanel.FORMULEBIJGRAFIEK)
		{	if ((e.getSource() == functieEditor) &&
				e.getActionCommand().equals("ingevuld"))
			{	kijkNa();
			}		
		}		
		else if (typeOpdracht == GrafiekEditPanel.FORMULEBIJPUNTEN)
		{	if ((e.getSource() == functieEditor) &&
				e.getActionCommand().equals("ingevuld"))
			{	kijkNa();
			}
		}
		else if (typeOpdracht == GrafiekEditPanel.TEKENTABELPUNTEN)
		{	if ((e.getSource() == grafiekTekenEditor) &&
				e.getActionCommand().equals("points changed"))
			{	grafiekTekenEditor.setColor(0, Color.blue);	
				grafiekComponent.repaint();
				Vector leerlingPts = grafiekTekenEditor.getPoints(1);
				Vector docentPts = tabelComponent.geefTabelpunten();
				if (leerlingPts.size() >= docentPts.size())
				{	kijkNaButton.setEnabled(true);
				}
				else
				{	kijkNaButton.setEnabled(false);
					groenVinkjeLabel.setVisible(false);
					kruisjeLabel.setVisible(false);
					grafiekComponent.zetTabelComponent(null);
					grafiekComponent.repaint();
					score = 0;
					correct = false;
	    			produceAction("changed");
				}
			}	
			if (e.getSource() == kijkNaButton)
			{
				kijkNa();
			}
		}
		else if (typeOpdracht == GrafiekEditPanel.PUNTENBIJFORMULE)
		{	if ((e.getSource() == grafiekTekenEditor) &&
				e.getActionCommand().equals("points changed"))
			{	Vector leerlingPts = grafiekTekenEditor.getPoints(1);
				if (leerlingPts.size() >= minimumPunten)
				{	kijkNaButton.setEnabled(true);
				}
				else
				{	kijkNaButton.setEnabled(false);
					groenVinkjeLabel.setVisible(false);
					kruisjeLabel.setVisible(false);
					grafiekComponent.zetDocentExpressie(null);
					grafiekComponent.repaint();
					score = 0;
					correct = false;
	    			produceAction("changed");
					
				}
			}
			if (e.getSource() == kijkNaButton)
			{
				kijkNa();
			}
		
		}

	}
	
	
	 
	public void removeTablet()
	{	if(tablet==null)return;
        remove(tablet);
        //resize();
        repaint();
		tabletAdded = false;
	}
	
	public void zetOpdracht(Hashtable h, String[] randomVars, Hashtable randomValues)
	{
		//edit = false;
//System.out.println("gp zetOpdracht");
		
		String varNaam = "x";
		String yAsNaam = "y";

		boolean assen = true;
		boolean rooster = true;
		boolean roosterGrof = false;
		boolean schaal = true;
		boolean piLijnen = false;
		
		boolean grafiekTekenen = true;
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
				
		if(h.containsKey("varNaam")) 
			varNaam = (String)h.get("varNaam");
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
			tabelZichtbaar = ((Boolean)h.get("tabelZichtbaar")).booleanValue();
		if (h.containsKey("formulesZichtbaar")) 
			formulesZichtbaar = ((Boolean)h.get("formulesZichtbaar")).booleanValue();
		if (h.containsKey("grafiekKleuren")) 
			grafiekKleuren = ((Boolean)h.get("grafiekKleuren")).booleanValue();

		if(h.containsKey("zoomOptie")) 
			zoomOptie = ((Boolean)h.get("zoomOptie")).booleanValue();
		if(h.containsKey("traceOptie")) 
			traceOptie = ((Boolean)h.get("traceOptie")).booleanValue();
		if(h.containsKey("dragOptie")) 
			dragOptie = ((Boolean)h.get("dragOptie")).booleanValue();
		if(h.containsKey("buttonOptie")) 
			buttonOptie = ((Boolean)h.get("buttonOptie")).booleanValue();
		if(h.containsKey("formFc")) 
			formFc = ((Boolean)h.get("formFc")).booleanValue();

		if (h.containsKey("zoomInTabel"))
			zoomInTabel = ((Boolean) h.get("zoomInTabel")).booleanValue();
		if (h.containsKey("tabelAlsTekenTool"))
			tabelAlsTekenTool = ((Boolean) h.get("tabelAlsTekenTool")).booleanValue();	
		
		if (h.containsKey("newVersion")) newVersion = ((Boolean)h.get("newVersion")).booleanValue();
		if (h.containsKey("xPositief")) xPositief = ((Boolean)h.get("xPositief")).booleanValue();
		if (h.containsKey("yPositief")) yPositief = ((Boolean)h.get("yPositief")).booleanValue();
		if (h.containsKey("xVarEditable")) xVarEditable = ((Boolean)h.get("xVarEditable")).booleanValue();
		if (h.containsKey("yVarEditable")) yVarEditable = ((Boolean)h.get("yVarEditable")).booleanValue();
		
		
		functieEditor.zetVarNaam(varNaam);
		zetYAsNaam(yAsNaam);

		zetAssen(assen);
		zetRooster(rooster);
		zetRoosterGrof(roosterGrof);
		zetSchaal(schaal);
		zetPiLijnen(piLijnen);
		
		zetGrafiekTekenen(grafiekTekenen);
		zetTabelZichtbaar(tabelZichtbaar);
		zetFormulesZichtbaar(formulesZichtbaar);
		zetGrafiekKleuren(grafiekKleuren);

//System.out.println("gp zetOpdracht gt " + grafiekTekenen);

		zetTraceOptie(traceOptie);
		zetZoomOptie(zoomOptie);
		zetDragOptie(dragOptie);
		setButton(buttonOptie);
		zetFormalFunction(formFc);

		zetZoomInTabel(zoomInTabel);
		zetTabelAlsTekenTool(tabelAlsTekenTool);
		
		zetNewVersion(newVersion);
		zetXPositief(newVersion && xPositief);
		zetYPositief(newVersion && yPositief);
		zetXVarEditable(newVersion && xVarEditable);
		zetYVarEditable(newVersion && yVarEditable);

		
		int breedte = basisPanel.getSize().width + frame.getInsets().left + frame.getInsets().right;
		int hoogte = basisPanel.getSize().height + frame.getInsets().top + frame.getInsets().bottom;
		if(buttonOptie)frame.setSize(breedte,hoogte);
		
		
		this.randomVars = randomVars;
		this.randomValues = randomValues;
		
		functieEditor.zetOpdracht(h, randomVars, randomValues);
		grafiekTekenEditor.zetOpdracht(h, randomVars, randomValues);		
		tabelComponent.zetOpdracht(h, randomVars, randomValues);		
		
		// opdrachten
		int typeOpdracht = 0;
		int scoreMax = 10;
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
		

		this.typeOpdracht = typeOpdracht;
		this.scoreMax = scoreMax;	
		this.docentExpressieString = docentExpressieString;
		try         
        {   docentExpressieString = FormuleParser.randomizeString(docentExpressieString,randomVars,randomValues);
        }
        catch(Exception e)
        {   docentExpressieString = "$f???@";
        }

//System.out.println("docExpStr = " + this.docentExpressieString);

		if (!docentExpressieString.equals("$f@"))
			docentExpressie = FormuleParser.geefExpressie(docentExpressieString);
		// anders blijft docentExpressie null	
		this.docentExpressie = docentExpressie;			
		this.docentGrafiekPunten = docentGrafiekPunten;
		this.docentTabelPunten = docentTabelPunten;
		this.docentTabelPuntVakIndex = docentTabelPuntVakIndex;		
		this.docentTabelStringsX = docentTabelStringsX;
		this.docentTabelStringsY = docentTabelStringsY;
		
		int nauwkeurigheid = 5;
		if (h.containsKey("nauwkeurigheid")) 
			nauwkeurigheid = ((Integer) h.get("nauwkeurigheid")).intValue();
		this.nauwkeurigheid = nauwkeurigheid;

		int minimumPunten = 5;
		if (h.containsKey("minimumPunten")) 
			minimumPunten = ((Integer) h.get("minimumPunten")).intValue();
		this.minimumPunten = minimumPunten;
//System.out.println("this mp = " + this.minimumPunten);		
		
		if (typeOpdracht == GrafiekEditPanel.TEKENTABELPUNTEN)
		{	
			docentTabelPuntenRandomized = new Vector();
			for (int pCnt = 0; pCnt < docentTabelPunten.size(); pCnt++)
			{	String xString = "";
				if(docentTabelPunten.size()==docentTabelStringsX.size()) xString = (String) docentTabelStringsX.elementAt(pCnt);
				String yString = "";
				if(docentTabelPunten.size()==docentTabelStringsY.size()) yString = (String) docentTabelStringsY.elementAt(pCnt);
				RealPoint rp = (RealPoint) docentTabelPunten.elementAt(pCnt);
				RealPoint rpc = new RealPoint(rp);
				
//				if ((xString.length() > 2) && (xString.charAt(0) == '#') && 
//					(xString.charAt(xString.length() - 1) == '#'))
//				{	rpc.x = substitueerRandom(rpc.x, xString, randomVars, randomValues);
					//xString = "";
//System.out.println("x = " + rpc.x);					
//				}
				
//System.out.println("xString:  " + xString);				
				//vervangen door:
				try 
				{	xString = FormuleParser.randomizeString("$f" + xString + "@", randomVars, randomValues);
				}
				catch(Exception e)
				{	xString = "";
				}
//System.out.println("xString:  " + xString);
				Expressie ex = FormuleParser.geefExpressie(xString);
				if (ex != null)
					rpc.x = ex.geefWaarde();
//System.out.println("rpc.x:  " + rpc.x);
				
//				if ((yString.length() > 2) && (yString.charAt(0) == '#') && 
//					(yString.charAt(yString.length() - 1) == '#'))
//				{	rpc.y = substitueerRandom(rpc.y, yString, randomVars, randomValues);
					//yString = "";
//				}
				
				try 
				{	yString = FormuleParser.randomizeString("$f" + yString + "@", randomVars, randomValues);
				}
				catch(Exception e)
				{	yString = "";
				}
//System.out.println("yString:  " + yString);
				Expressie ey = FormuleParser.geefExpressie(yString);
				if (ey != null)
					rpc.y = ey.geefWaarde();
				
				// just in case
				if (Double.isNaN(rpc.x))
					rpc.x = 0;
				if (Double.isNaN(rpc.y))
					rpc.y = 0;
				
				rpc.index = rpc.index % 100;
				
				docentTabelPuntenRandomized.addElement(rpc);
			}
			
		}

		if (typeOpdracht == GrafiekEditPanel.FORMULEBIJPUNTEN)
		{	
			docentTabelPuntenRandomized = new Vector();
			for (int pCnt = 0; pCnt < docentTabelPunten.size(); pCnt++)
			{	String xString = "";
				if(docentTabelPunten.size()==docentTabelStringsX.size()) xString = (String) docentTabelStringsX.elementAt(pCnt);
				//String yString = (String) docentTabelStringsY.elementAt(pCnt);
				RealPoint rp = (RealPoint) docentTabelPunten.elementAt(pCnt);
				RealPoint rpc = new RealPoint(rp);
				
//				if ((xString.length() > 2) && (xString.charAt(0) == '#') && 
//					(xString.charAt(xString.length() - 1) == '#'))
//				{	rpc.x = substitueerRandom(rpc.x, xString, randomVars, randomValues);
					//xString = "";
//System.out.println("x = " + rpc.x);					
//				}
				
//System.out.println("xString:  " + xString);				
				//vervangen door:
				try 
				{	xString = FormuleParser.randomizeString("$f" + xString + "@", randomVars, randomValues);
				}
				catch(Exception e)
				{	xString = "";
				}
//System.out.println("xString:  " + xString);
				Expressie ex = FormuleParser.geefExpressie(xString);
				if (ex != null)
					rpc.x = ex.geefWaarde();
//System.out.println("rpc.x:  " + rpc.x);
				
//				if ((yString.length() > 2) && (yString.charAt(0) == '#') && 
//					(yString.charAt(yString.length() - 1) == '#'))
//				{	rpc.y = substitueerRandom(rpc.y, yString, randomVars, randomValues);
					//yString = "";
//				}
				
/*				
				try 
				{	yString = FormuleParser.randomizeString("$f" + yString + "@", randomVars, randomValues);
				}
				catch(Exception e)
				{	yString = "";
				}
//System.out.println("yString:  " + yString);
				Expressie ey = FormuleParser.geefExpressie(yString);
				if (ey != null)
					rpc.y = ey.geefWaarde();
*/				
				// just in case
				if (Double.isNaN(rpc.x))
					rpc.x = 0;
//				if (Double.isNaN(rpc.y))
//					rpc.y = 0;
				
				rpc.index = rpc.index % 100;
				
				docentTabelPuntenRandomized.addElement(rpc);
			}
			
		}
		
		zetTypeOpdracht(typeOpdracht, true);
	}
	
	public void setState(Hashtable h)
	{
		//edit = false;

//System.out.println("gp setState");
		
		functieEditor.setState(h);
		grafiekTekenEditor.setState(h);
		tabelComponent.setState(h);
/*
		int nauwkeurigheid = 5;
		if (h.containsKey("nauwkeurigheid")) 
			nauwkeurigheid = ((Integer) h.get("nauwkeurigheid")).intValue();
		this.nauwkeurigheid = nauwkeurigheid;

		
		int minimumPunten = 5;
		if (h.containsKey("minimumPunten")) 
			minimumPunten = ((Integer) h.get("minimumPunten")).intValue();
		this.minimumPunten = minimumPunten;
	
		int scoreMax = 10;
		if (h.containsKey("scoreMax")) 
			scoreMax = ((Integer) h.get("scoreMax")).intValue();
		this.scoreMax = scoreMax;
*/			
		Vector leerlingPts = grafiekTekenEditor.getPoints(1);
		
		if (leerlingPts.size() >= minimumPunten)
			kijkNaButton.setEnabled(true);
		
		if (mode == 0 || nagekeken) 
			kijkNa();
		
		
// is dit nodig?? Nee!!
/*		
		// opdrachten
		int typeOpdracht = 0;
		int maxScore = 0;
		Expressie docentExpressie = null;
		
		if (h.containsKey("typeOpdracht")) 
			typeOpdracht = ((Integer) h.get("typeOpdracht")).intValue();		
		if (h.containsKey("maxScore")) 
			maxScore = ((Integer) h.get("maxScore")).intValue();		
//		if (h.containsKey("docentExpressie")) 
//			docentExpressie = (Expressie) h.get("docentExpressie");		

		this.typeOpdracht = typeOpdracht;
		this.maxScore = maxScore;	
		this.docentExpressie = docentExpressie;
*/		
	}
	
	public void setEditState(Hashtable h)
	{
		//edit = true;
		
System.out.println("gp setEditState");		
		
		String varNaam = "x";
		String yAsNaam = "y";

		boolean assen = true;
		boolean rooster = true;
		boolean roosterGrof = false;
		boolean schaal = true;
		boolean piLijnen = false;
		
		boolean grafiekTekenen = true;
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
		
				
		if(h.containsKey("varNaam")) 
			varNaam = (String)h.get("varNaam");
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
		if(h.containsKey("tabelZichtbaar")) 
			tabelZichtbaar = ((Boolean)h.get("tabelZichtbaar")).booleanValue();
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
		
		
		
		functieEditor.zetVarNaam(varNaam);
		zetYAsNaam(yAsNaam);
		
		zetAssen(assen);
		zetRooster(rooster);
		zetRoosterGrof(roosterGrof);
		zetSchaal(schaal);
		zetPiLijnen(piLijnen);
		
		zetGrafiekTekenen(grafiekTekenen);
		zetTabelZichtbaar(tabelZichtbaar);		
		zetFormulesZichtbaar(formulesZichtbaar);
		zetGrafiekKleuren(grafiekKleuren);
		
//System.out.println("gp setEditState ft " + formulesZichtbaar);
//System.out.println("gp setEditState tz " + tabelZichtbaar);

		zetTraceOptie(traceOptie);
		zetZoomOptie(zoomOptie);
		zetDragOptie(dragOptie);
		setButton(buttonOptie);
		zetFormalFunction(formFc);
	
		zetZoomInTabel(zoomInTabel);
		zetTabelAlsTekenTool(tabelAlsTekenTool);
		
		zetNewVersion(newVersion);
		zetXPositief(newVersion && xPositief);
		zetYPositief(newVersion && yPositief);
		
		functieEditor.setEditState(h);
		grafiekTekenEditor.setEditState(h);
		tabelComponent.setEditState(h);
		
		// opdrachten
		int typeOpdracht = 0;
		int scoreMax = 10;
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

		this.typeOpdracht = typeOpdracht;
		this.scoreMax = scoreMax;	
		this.docentExpressieString = docentExpressieString;
		if (!docentExpressieString.equals("$f@"))
			docentExpressie = FormuleParser.geefExpressie(docentExpressieString);
		this.docentExpressie = docentExpressie;						
		this.docentGrafiekPunten = docentGrafiekPunten;
		this.docentTabelPunten = docentTabelPunten;
		this.docentTabelPuntVakIndex = docentTabelPuntVakIndex;
		this.docentTabelStringsX = docentTabelStringsX;
		this.docentTabelStringsY = docentTabelStringsY;

//System.out.println("tpvi = " + docentTabelPuntVakIndex);

		int nauwkeurigheid = 5;
		if (h.containsKey("nauwkeurigheid")) 
			nauwkeurigheid = ((Integer) h.get("nauwkeurigheid")).intValue();
		this.nauwkeurigheid = nauwkeurigheid;

		int minimumPunten = 5;
		if (h.containsKey("minimumPunten")) 
			minimumPunten = ((Integer) h.get("minimumPunten")).intValue();
		this.minimumPunten = minimumPunten;
		
		
//System.out.println("des = " + this.docentExpressieString);
		zetTypeOpdracht(typeOpdracht, false);
		

	}
	
	public Hashtable getState()
	{
//System.out.println("gp getState");		
		
		// begin met deze
		Hashtable h = grafiekTekenEditor.getState();		
		// toevoegen getState() van functieEditor
		Hashtable h1 = functieEditor.getState();
		for (Enumeration e = h1.keys(); e.hasMoreElements();)
		{	Object aKey = e.nextElement();
			Object aValue = h1.get(aKey);
			h.put(aKey, aValue);
		}
		// toevoegen getState() van tabelComponent		
		Hashtable h2 = tabelComponent.getState();
		for (Enumeration e = h2.keys(); e.hasMoreElements();)
		{	Object aKey = e.nextElement();
			Object aValue = h2.get(aKey);
			h.put(aKey, aValue);
		}
		
		return h;
	}
	
	public JPanel getViewer()
	{
		return grafiekComponent;
	}
	
	public Hashtable getEditState()
	{
		
//System.out.println("gp setEditState");		

		// begin met deze		
		Hashtable h = grafiekTekenEditor.getEditState();		
		
		// toevoegen getEditState() van functieEditor
		Hashtable h1 = functieEditor.getEditState();
		for (Enumeration e = h1.keys(); e.hasMoreElements();)
		{	Object aKey = e.nextElement();
			Object aValue = h1.get(aKey);
			h.put(aKey, aValue);
		}
		// toevoegen getEditState() van tabelComponent		
		Hashtable h2 = tabelComponent.getEditState();
		for (Enumeration e = h2.keys(); e.hasMoreElements();)
		{	Object aKey = e.nextElement();
			Object aValue = h2.get(aKey);
			h.put(aKey, aValue);
		}
		
		return h;
	}
	
	public InteractieEditPanel getEditPanel()
	{	return new GrafiekEditPanel();
	}
	
	public void zetBreedte(int b)
	{	//grafiekPanel.setSize(b,grafiekPanel.getSize().height);
	}
	public void zetHoogte(int h)
	{	//grafiekPanel.setSize(grafiekPanel.getSize().width, h);
	}
	
	public void setBounds(int x, int y, int b, int h)
	{	if(isButton)
		{	super.setBounds(x,y,20,20);
			basisPanel.setBounds(0,0,b,h);
		}
		else 
		{	super.setBounds(x,y,b,h);
			basisPanel.setBounds(0,0,b,h);
		}
		resize();
	}
	
	public void setSize(int b, int h)
	{	if(isButton)
		{	super.setSize(20,20);
			basisPanel.setSize(b,h);
		}
		else 
		{	super.setSize(b,h);
			basisPanel.setSize(b,h);
		}
		resize();
	}
	
	public void resize()
	{
//System.out.println("resize");		
		
		int space = 5;
		int gcX = 0;
		int gcY = zoomOptie?0:(-28);
		int gcB = basisPanel.getSize().width;
		int gcH = basisPanel.getSize().height - 
			(zoomOptie?0:(-28)) - 
			(grafiekTekenen?24+space:0) -
			(tabelZichtbaar?(tabelComponent.getSize().height+space):0) -
			(formulesZichtbaar?(functieEditor.getSize().height):0) -
			(kijkNaPanel.isVisible()?(kijkNaPanel.getSize().height+3):0);
		grafiekComponent.setBounds(gcX,gcY,gcB,gcH);
		
		if(newVersion)
		{	int gtX = isButton?0:0;
			int gtY = gcY + gcH;
			int gtB = basisPanel.getSize().width - (isButton?0:0);
			int gtH = (grafiekTekenen?(24+space):0);
			grafiekTekenEditor.setBounds(gtX,gtY,gtB,24);
			
			int tcX = isButton?0:0;
			//int tcX = (basisPanel.getSize().width - tabelComponent.getSize().width) / 2;
			if (tcX < 0) tcX = 0;	
			int tcY = gcY + gcH + gtH;
			int tcB = basisPanel.getSize().width; // - (isButton?0:60);
			int tcH = (tabelZichtbaar?(tabelComponent.getSize().height+space):0);
			tabelComponent.setBounds(tcX, tcY, tcB, tabelComponent.getSize().height);
		
			int feX = isButton?0:0;
			int feY = gcY + gcH + gtH + tcH;
			int feB = basisPanel.getSize().width - (isButton?0:0);
			//int feH = basisPanel.getSize().height - gcY - gcH - gtH - tcH;
			if (formulesZichtbaar)
				functieEditor.setBounds(feX,feY,feB,functieEditor.getSize().height);
		}
		else
		{
			int gtX = isButton?0:40;
			int gtY = gcY + gcH;
			int gtB = basisPanel.getSize().width - (isButton?0:60);
			//int space = 5;
			int gtH = (grafiekTekenen?(24+space):0);
			grafiekTekenEditor.setBounds(gtX,gtY,gtB,24);
		
			int tcX = isButton?0:40;
			//int tcX = (basisPanel.getSize().width - tabelComponent.getSize().width) / 2;
			if (tcX < 0) tcX = 0;	
			int tcY = gcY + gcH + gtH;
			int tcB = basisPanel.getSize().width-60; // - (isButton?0:60)
			int tcH = (tabelZichtbaar?(tabelComponent.getSize().height+space):0);
			tabelComponent.setBounds(tcX, tcY, tcB, tabelComponent.getSize().height);
	//System.out.println("tcX = " + tcX);		
			
			int feX = isButton?0:40;
			int feY = gcY + gcH + gtH + tcH;
			int feB = basisPanel.getSize().width - (isButton?0:60);
			//int feH = basisPanel.getSize().height - gcY - gcH - gtH - tcH;
			if( formulesZichtbaar)
				functieEditor.setBounds(feX,feY,feB,functieEditor.getSize().height);
		}
		if (kijkNaPanel.isVisible())
		{
			kijkNaPanel.setLocation((basisPanel.getSize().width - 75) / 2, 
		                             basisPanel.getSize().height - kijkNaPanel.getSize().height);
		}
		
		
	}
	
	public void setStudentEditor(boolean b)
	{	if(b)zetNewVersion(true);
	}
	
	public void wis(){}
	
	public void zetMaat(){}
	
	public int geefAsHoogte(){return 10;}
	
	public int getIpId(){return 0;}
	
	public int getScore()
	{	
//System.out.println("getScore " + score);		
		return score;
	}
	
	public int[][] getScoreObjectives()
	{	return null;
	}
	
	public int getScoreMax()
	{	if(typeOpdracht == GrafiekEditPanel.GEENOPDRACHT)return 0;
		else return scoreMax;
	}
	
	public boolean isCorrect()
	{	return correct;
	}
	
	public boolean isFout()
	{	return fout;
	}
	
	public void zetMode(int mode)
	{	this.mode = mode;
	}
	
	public void zetNagekeken(boolean b)
	{	if (ingevuld)
		nagekeken = b;
	}
	
    public void stop()
    {	if (frame != null) 
    		frame.setVisible(false);
    }
    
    public void start(){}
    
    public void destroy(){}
    
    public void opnieuw(){}
    
    public void kijkNa()
    {
    	if (typeOpdracht == GrafiekEditPanel.FORMULEBIJGRAFIEK)
		{	Expressie leerlingExp = functieEditor.geefExpressie();
			if (leerlingExp != null)
			{	ingevuld = true;
				Color color = Color.red;
				score = 0;
				correct = false;
				fout = false;
				if (Algebra.isGelijkwaardig(leerlingExp, docentExpressie))
				{
					color = new Color(0, 200, 0);
					score = scoreMax;
					correct = true;
				}
				else
				{	fout = true;
				}
				grafiekComponent.setColor(0, color);
			}
		}
    	else if (typeOpdracht == GrafiekEditPanel.FORMULEBIJPUNTEN)
		{	Expressie leerlingExp = functieEditor.geefExpressie();
			if (leerlingExp != null)
			{	ingevuld = true;
				Color color = Color.red;
				score = 0;
				correct = false;
				fout = false;
				if (Algebra.isGelijkwaardig(leerlingExp, docentExpressie))
				{
					color = new Color(0, 200, 0);
					score = scoreMax;
					correct = true;
					
				}
				else
				{	fout = true;
				}
				grafiekComponent.setColor(0, color);
			}
		}
    	else if (typeOpdracht == GrafiekEditPanel.PUNTENBIJFORMULE)
    	{	// size is >= 5
    		Vector leerlingPts = grafiekTekenEditor.getPoints(1);
    		
//System.out.println("lp = " + leerlingPts.size());
//System.out.println("mp = " + minimumPunten);
    		if (leerlingPts.size() < minimumPunten)
    		{	score = 0;
    			correct = false;
    			produceAction("changed");
    			if (leerlingPts.size() > 0)
    				ingevuld = true;
    			return;
    		
    		}
    		
    		if (docentExpressie != null)
    		{	
    			ingevuld = true;
    			Color color = Color.red;
    			score = 0;
    			correct = false;
    			fout = false;
/*			
    			double maxDWaarde = 0;
    			for (int pCnt = 0; pCnt < leerlingPts.size(); pCnt++)
    			{	RealPoint lPoint = (RealPoint) leerlingPts.elementAt(pCnt);
    				double dWaarde = Math.abs(docentExpressie.geefWaarde(lPoint.x));
    				maxDWaarde = Math.max(maxDWaarde, dWaarde);
    			}
    			double minAfw = ((double) nauwkeurigheid) * Math.abs(maxDWaarde) / 100;
    			for (int pCnt = 0; pCnt < leerlingPts.size(); pCnt++)
    			{	RealPoint lPoint = (RealPoint) leerlingPts.elementAt(pCnt);
    				double dWaarde = Math.abs(docentExpressie.geefWaarde(lPoint.x));
    				double afwijking = ((double) nauwkeurigheid) * Math.abs(dWaarde) / 100;
    				
    				afwijking = nauwkeurigheid;
    				if (Math.abs(dWaarde) > NZero)
    					minAfw = Math.min(minAfw, afwijking);
    			}
*/    			
    			
    			int hits = 0;
    			for (int pCnt = 0; pCnt < leerlingPts.size(); pCnt++)
    			{	RealPoint lPoint = (RealPoint) leerlingPts.elementAt(pCnt);
    				//double lWaarde = lPoint.y;
    				double dWaarde = docentExpressie.geefWaarde(lPoint.x);
    				RealPoint dPoint = new RealPoint(lPoint.x, dWaarde);
    				Point lPixel = grafiekComponent.realPointToPixels(lPoint);
    				Point dPixel = grafiekComponent.realPointToPixels(dPoint);

    				double dis = Math.sqrt((lPixel.x - dPixel.x) * (lPixel.x - dPixel.x) +
    							     	   (lPixel.y - dPixel.y) * (lPixel.y - dPixel.y)); 
/*    				
  
    				double afwijking = minAfw;
    				if (Math.abs(dWaarde) > NZero)
    					afwijking = ((double) nauwkeurigheid) * Math.abs(dWaarde) / 100;
    				
    				afwijking = nauwkeurigheid;
    				double sy = grafiekComponent.geefSchaalY();
    				if (Math.abs(lWaarde - dWaarde)*sy < afwijking)
*/    				
    				if (dis < nauwkeurigheid)
    					hits++;
    			}
//System.out.println("hits = " + hits);    			
    			int scorePerPunt = scoreMax / leerlingPts.size();
    			if (hits == 0)
    			{	score = 0;
    				fout = true;
    				groenVinkjeLabel.setVisible(false);
    				kruisjeLabel.setVisible(true);
    			}
    			else if (hits == leerlingPts.size())
    			{
    				color = new Color(0, 200, 0);
					score = scoreMax;
					correct = true;
					groenVinkjeLabel.setVisible(true);
					kruisjeLabel.setVisible(false);
    			}
    			else
    			{	score = hits * scorePerPunt;    			
    				fout = true;
    				groenVinkjeLabel.setVisible(false);
					kruisjeLabel.setVisible(true);
    			}
    			grafiekComponent.zetDocentColor(color);
    			if (correct)
    				grafiekComponent.zetDocentExpressie(docentExpressie);
    			
    		}
    		
    	}
    	else if (typeOpdracht == GrafiekEditPanel.TEKENTABELPUNTEN)
    	{	
    		Vector leerlingPts = grafiekTekenEditor.getPoints(1);
    		Vector docentPts = tabelComponent.geefTabelpunten();
    		
    		if (leerlingPts.size() < docentPts.size())
    		{	score = 0;
    			correct = false;
				produceAction("changed");
    			return;
    		}
    			
    		if (docentPts.size() > 0)
    		{	
    			ingevuld = true;
    			Color color = Color.red;
    			score = 0;
    			correct = false;
    			fout = false;

    			Vector llgPtsCopy = new Vector();
    			for (int pCnt = 0; pCnt < leerlingPts.size(); pCnt++)
    			{	llgPtsCopy.addElement(leerlingPts.elementAt(pCnt));
    			}
    			RealPoint[] llgPtsArray = new RealPoint[leerlingPts.size()];
    			
    			for (int dCnt = 0; dCnt < docentPts.size(); dCnt++)
    			{
    				RealPoint dPt = (RealPoint) docentPts.elementAt(dCnt);
    				RealPoint lPt = (RealPoint) llgPtsCopy.elementAt(0);
    				int index = 0;
    				double distance = Math.sqrt((dPt.x - lPt.x)*(dPt.x - lPt.x) + (dPt.y - lPt.y)*(dPt.y - lPt.y));
    				for (int lCnt = 1; lCnt < llgPtsCopy.size(); lCnt++)
    				{	RealPoint aLlgPt = (RealPoint) llgPtsCopy.elementAt(lCnt);
    					double aDis = Math.sqrt((dPt.x - aLlgPt.x) * (dPt.x - aLlgPt.x) +
    											(dPt.y - aLlgPt.y) * (dPt.y - aLlgPt.y));
    					if (aDis < distance)
    					{	distance = aDis;
    						index = lCnt;
    					}
    				}
    				
    				llgPtsArray[dCnt] = (RealPoint) llgPtsCopy.elementAt(index);
    				llgPtsCopy.removeElementAt(index);
    				
    			}
/*    			
    			double maxDWaarde = 0;
    			for (int dCnt = 0; dCnt < Math.min(docentPts.size(), leerlingPts.size()); dCnt++)
    			{	RealPoint dPoint = (RealPoint) docentPts.elementAt(dCnt);
    				double dWaarde = Math.max(Math.abs(dPoint.x), Math.abs(dPoint.y));
    				maxDWaarde = Math.max(maxDWaarde, dWaarde);
    			}
    			double minAfw = ((double) nauwkeurigheid) * Math.abs(maxDWaarde) / 100;
    			for (int dCnt = 0; dCnt < docentPts.size(); dCnt++)
    			{	RealPoint dPoint = (RealPoint) docentPts.elementAt(dCnt);
    				double dWaarde = Math.max(Math.abs(dPoint.x), Math.abs(dPoint.y));
    				double afwijking = ((double) nauwkeurigheid) * Math.abs(dWaarde) / 100;
    				if (Math.abs(dWaarde) > NZero)
    					minAfw = Math.min(minAfw, afwijking);
    			}
*/    			
    			
    			int hits = 0;
    			for (int dCnt = 0; dCnt < docentPts.size(); dCnt++)
    			{	RealPoint dPoint = (RealPoint) docentPts.elementAt(dCnt);
    				RealPoint lPoint = llgPtsArray[dCnt];
    				
    				Point lPixel = grafiekComponent.realPointToPixels(lPoint);
    				Point dPixel = grafiekComponent.realPointToPixels(dPoint);

    				double dis = Math.sqrt((lPixel.x - dPixel.x) * (lPixel.x - dPixel.x) +
    							     	   (lPixel.y - dPixel.y) * (lPixel.y - dPixel.y)); 
    				
/*    				
    				double dWaarde = Math.max(Math.abs(dPoint.x), Math.abs(dPoint.y));
    				double afwijking = minAfw;
    				
    				
    				if (Math.abs(dWaarde) > NZero)
    					afwijking = ((double) nauwkeurigheid) * Math.abs(dWaarde) / 100;
    				
    				afwijking = nauwkeurigheid;
    				double sx = grafiekComponent.geefSchaalX();
    				double sy = grafiekComponent.geefSchaalY();
    				double distance = Math.sqrt((dPoint.x - lPoint.x)*(dPoint.x - lPoint.x)*(sx*sx) + (dPoint.y - lPoint.y)*(dPoint.y - lPoint.y)*(sy*sy));
    				
    				if (Math.abs(distance) < afwijking)
*/
    				if (dis < nauwkeurigheid)
    					hits++;
    			}
    			
    			int scorePerPunt = scoreMax / docentPts.size();
    			if (hits == 0)
    				score = 0;
    			else if (hits == leerlingPts.size())
    			{
    				color = new Color(0, 200, 0);
					score = scoreMax;
					correct = true;
					grafiekTekenEditor.setColor(0, color);
					groenVinkjeLabel.setVisible(true);
					kruisjeLabel.setVisible(false);
    			}
    			else
    			{	score = hits * scorePerPunt;    			
    				fout = true;
    				groenVinkjeLabel.setVisible(false);
					kruisjeLabel.setVisible(true);
    			}

    			tabelComponent.setColor(0, color);
    			grafiekComponent.repaint();
    			//grafiekComponent.zetTabelComponent(tabelComponent);
    		}
    	}
    	
    	if (ingevuld)
    		produceAction("changed");
    }
    
    public void kijkNa(int stapNr)
    {	kijkNa();
    }
    
    //public void addActionListener(ActionListener al){}
    
    public InteractiePanel getInteractiePanel()
	{
		return this;
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

  @Override
  public void setStub(AppletStub stub) {
  }
    
} // GrafiekPanel
