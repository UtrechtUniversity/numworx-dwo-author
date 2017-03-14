package fi.wiskopdr;

import java.awt.AWTEventMulticaster;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.Border;

import org.cbook.cbookif.CBookEvent;
import org.cbook.cbookif.CBookEventHandler;
import org.cbook.cbookif.CBookEventListener;

import fi.beans.base64code.StringCodeObject;
import fi.beans.iconan.Iconan;
import fi.beans.stringutils.StringUtils;
import fi.beans.wiskopdrbeans.CBookAware;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;
import fi.wiskopdr.expressies.Aftrekking;
import fi.wiskopdr.expressies.BasisExpressie;
import fi.wiskopdr.expressies.Expressie;
import fi.wiskopdr.expressies.Optelling;
import fi.wiskopdr.expressies.Vergelijking;
import fi.wiskopdr.expressies.VergelijkingMeerv;
import fi.wiskopdr.formuleobjects.FormuleParser;
import fi.wiskopdr.formuleobjects.FormuleVak;
import fi.wiskopdr.formuleobjects.FormuleVakHouder;
import fi.wiskopdr.formuleobjects.Tablet;
import fi.wiskopdr.formuleobjects.TabletOwner;
import fi.wiskopdr.opdrnav.MyOpdrContainer;
import fi.wiskopdr.opdrnav.OpdrNavStruct;
import fi.wiskopdr.opdrnav.XWidgetManager;
import fi.wiskopdr.tekstobjects.Link;
import fi.wiskopdr.tekstobjects.TekstDeelVak;
import fi.wiskopdr.tekstobjects.TekstImageVak;
import fi.wiskopdr.tekstobjects.TekstInteractiePanelVak;
import fi.wiskopdr.tekstobjects.TekstVak;

public class TekstVakPanel extends RoundedPanel implements TabletOwner, InteractiePanel, ActionListener, MouseListener, MouseMotionListener, CBookAware
{
	public static Map<String,Map> styles = new Hashtable<String,Map>();
	
	
	/**
	 * @param manager
	 */
	public TekstVakPanel(XWidgetManager manager) {
		this();
		this.manager = manager;
	}

	public static boolean fontOvererving = false;
	protected int score;
	protected int scoreMax;
	protected boolean correct = true;
	protected boolean fout = false;
	protected int mode;

	private TekstVak tekstVak;
	private Font font = WiskOpdr.tekstFont;//new Font("SansSerif",Font.PLAIN,12);

	private String styleString = null;
	private boolean randZichtbaar = false;
	private boolean bgColorZichtbaar = false;
	private Color bgColor = new Color(255, 255, 180);
	private Color oldbgColor = new Color(255, 255, 180);
	private Color fgColor = new Color(0, 0, 0);
	private Color randColor = Color.gray;
	//private Color selectieColor = Color.white;
	private boolean anderFont = false;
	private boolean zwevend = false;
	private boolean tableBorders = false;
	private int ronding = 0;
	 int cellMarge = 0;
	private int bovenMarge = 0;
	private int hoek;
	private int randDikte = 1;
	private boolean centerH;
	private boolean centerV;
	private boolean pasAanH;
	private boolean pasAanB;

	private int locationX;
	private int locationY;

	private int aantalRijen = 1;
	private int aantalKolommen = 1;
	private TekstVak[][] tekstVakken;
	private double[] breedtes;
	private double[] hoogtes;
	private int cellSpaceColumn = 2;
	private int cellSpaceRow = 2;
	private boolean boundsSet;

	private Rectangle[] dragColomsRects;
	private Rectangle[] dragRowsRects;
	private boolean dragModeColoms;
	private boolean dragModeRows;
	private int dragNumber;
	private int dragStart;

	private boolean tableMode;
	private boolean editable;
	private boolean selectable = false;
	private boolean colorSelection = false;
	private Color selectieColor = new Color(255, 128, 0, 128);
	private boolean sleepbaar = false;
	private boolean sleepdoel = false;
	private boolean sleepHandle = false;
	private int sleepdoelMarge = 10;
	private boolean sleepSnap = false;
	private boolean selected = false;

	private Point[] doelPosities;
	private TekstVakPanel[] sleepObjecten;

	private JPanel klikPanel;
	private Expressie antwoordExpressie;
	private String checkExpressieString = "$f1@";

	private Tablet tablet;
	private FormuleVakHouder tabletUser;
	private boolean tabletAdded;

	private int ipId;
	private int interlinie;
	int focusNr;

	private boolean relocate = false;
	private int startSleepX;
	private int startSleepY;

	private boolean zichtbaarNaNakijken;
	private boolean nagekeken;

	private boolean balansVergCom = false;

	private boolean aftrekPopup = false;
	private int puntenAftrekPopup;
	private boolean popupUsed;

	private boolean callOut = false;
	private boolean callOutDrag = false;
	
	private boolean visible = true;

	int callOutMargeX0 = 15;
	int callOutMargeY0 = 15;
	int callOutMargeX1 = 5;
	int callOutMargeY1 = 5;
	int callOutPointX = 0;
	int callOutPointY = 0;
	private int dragStartX = 0;
	private int dragStartY = 0;
	private int dragX = 0;
	private int dragY = 0;
	private int dragType = 0;

	String[][][] randomteksten = new String[1][][];
	Hashtable[][] randomIpLaunchdata = new Hashtable[1][];
	private boolean random = false;
	private int aantalRandom = 1;
	private String randomVar = "a";
	private boolean isLink;
	private boolean defaultBijNull;
	//private Link link = new Link("link", "http://", 400, 400);
	String[] httpString = new String[] {"http://", "http://", "http://", "http://",
			"http://", "http://", "http://", "http://", "http://", "http://"};
	
	private Link link = new Link("link", httpString, 400, 400, false, null);
	//private Link link = new Link("link", null, 400, 400, null);
	
	private boolean vulHoogte = false;
	private boolean inklapbaar = false;
	private boolean checkUitklapVak = false;
	private int inklapKnopPos = 1;
	private JToggleButton klapUitButton;
	//private int klapUitButtonWidth = 20;
	private boolean ingeklapt = true;
	private int[] uitklapHoogtes = new int[aantalRijen];
	private String knopImageString1 = "";
	private String knopImageString2 = "";
	
	private String[] randomVars;
	private Hashtable randomValues;
	
	private boolean logOption = false;
	private String logID = "";
	private String logIDLabel = "";
	
	private CBookEventHandler cbookEventHandler = new CBookEventHandler(this);
	
	private boolean isStappenVak = false;
	private int stapNr = 0;
	private String[] stappen = null;
	private boolean ideasStatistiek = false;
	
	public TekstVakPanel()
	{
		this(1, 1);
		setTableMode(false);

		/*
		setLayout(null);
		setBackground(WiskOpdr.bgcolor);
		tekstVak = new TekstVak();
		tekstVak.addActionListener(this);
		add(tekstVak);
		tekstVak.setLocation(0,0);
		tekstVak.setEditable(false);
		//tekstVak.setSelectable(false);
		tekstVak.setBackground(WiskOpdr.bgcolor);
		tekstVak.zetMarge(5);
		*/
	}

	public TekstVakPanel(int aantalRijen, int aantalKolommen)
	{
		setLayout(null);
		setBackground(WiskOpdr.bgcolor);
		setCurvature(0);
		
		
		setTableMode(true);

		addMouseListener(this);
		addMouseMotionListener(this);

		this.aantalRijen = aantalRijen;
		this.aantalKolommen = aantalKolommen;
		tekstVakken = new TekstVak[aantalRijen][aantalKolommen];
		for (int i = 0; i < aantalRijen; i++)
		{
			for (int j = 0; j < aantalKolommen; j++)
			{
				tekstVakken[i][j] = new TekstVak();
				tekstVakken[i][j].addActionListener(this);
				tekstVakken[i][j].addMouseListener(this);
				add(tekstVakken[i][j]);
				tekstVakken[i][j].setLocation(0, 0);
				tekstVakken[i][j].setEditable(false);
				//tekstVak.setSelectable(false);
				tekstVakken[i][j].setBackground(WiskOpdr.bgcolor);
				tekstVakken[i][j].zetMarge(5);
				tekstVakken[i][j].zetBovenMarge(0);
			}
		}

		tekstVak = tekstVakken[0][0];
	}
	
	public static void zetFontOvererving(boolean b)
	{	fontOvererving = b;
	}

	public void zetTeksten(String[][] teksten)
	{
		this.aantalRijen = teksten.length;
		this.aantalKolommen = teksten[0].length;
		for (int i = 0; i < aantalRijen; i++)
		{
			for (int j = 0; j < aantalKolommen; j++)
			{
				tekstVakken[i][j].zetTekst(teksten[i][j]);
			}
		}
	}

	public void zetTekst(String tekst)
	{
		tekstVakken[0][0].zetTekst(tekst);

	}

	public void zetBreedtes(double[] breedtes)
	{
		this.breedtes = breedtes;
	}

	public void zetHoogtes(double[] hoogtes)
	{
		this.hoogtes = hoogtes;
	}

	public void setTableMode(boolean b)
	{
		tableMode = b;
	}

	public void setEditable(boolean b)
	{

		//if(b)tekstVak.setBorder(new DashedBorder(Color.gray,2,2));
		for (int i = 0; i < aantalRijen; i++)
		{
			for (int j = 0; j < aantalKolommen; j++)
			{
				if (b)
					tekstVakken[i][j].setBorder(new DashedBorder(Color.gray, 2, 2));
				if (!b)
					tekstVakken[i][j].setBorder(BorderFactory.createEmptyBorder());
				tekstVakken[i][j].setEditable(b);
			}
		}
		editable = b;

	}

	public void zetKlikPanel(boolean b)
	{
		if (klikPanel == null)
		{
			klikPanel = new JPanel();
			klikPanel.setBounds(0, 0, getSize().width, getSize().height);
			klikPanel.setOpaque(false);
			klikPanel.addMouseListener(this);
			add(klikPanel, 0);
		}

		klikPanel.setBounds(0, 0, getSize().width, getSize().height);
	}

	public void setIpSelectable(boolean b)
	{
		selectable = b;
	}

	public boolean getRelocate()
	{
		return relocate;
	}

	public void setRelocate(boolean relocate)
	{
		this.relocate = relocate;
	}

	public boolean isIpSelectable()
	{
		return selectable;
	}

	public void setIpSleepbaar(boolean b)
	{
		sleepbaar = b;
	}

	public boolean isIpSleepbaar()
	{
		return sleepbaar;
	}

	public boolean hasSleepHandle()
	{
		return sleepHandle;
	}

	public void setIpSelected(boolean b)
	{
		selected = b;
		if (selected)
		{
			if (colorSelection)
				setBorder(selectieColor, 400);
			else
				setBorder(Color.gray, 5);
			if(cbookEventHandler.hasListeners("action.select"))
				cbookEventHandler.fire("action.select");
		}
		else
		{
			setBorder(randColor, randZichtbaar? randDikte : 0);
			if(cbookEventHandler.hasListeners("action.deselect"))
				cbookEventHandler.fire("action.deselect");
		}
		repaint();
	}

	public boolean isIpSelected()
	{
		return selected;
	}

	public void setBackground(Color color)
	{
		bgColor = new Color(color.getRed(), color.getGreen(), color.getBlue());
		super.setBackground(color);
		//if(tekstVak!=null) tekstVak.setBackground(color);
		for (int i = 0; i < aantalRijen && tekstVakken != null; i++)
		{
			for (int j = 0; j < aantalKolommen; j++)
			{
				tekstVakken[i][j].setBackground(color);
			}
		}
	}

	public void setForeground(Color color)
	{
		//fgColor = new Color(color.getRed(), color.getGreen(), color.getBlue());
		super.setForeground(color);
		//if(tekstVak!=null) tekstVak.setBackground(color);
		for (int i = 0; i < aantalRijen && tekstVakken != null; i++)
		{
			for (int j = 0; j < aantalKolommen; j++)
			{
				tekstVakken[i][j].setForeground(color);
			}
		}
	}

	public void setInterlinie(int interlinie)
	{
		for (int i = 0; i < aantalRijen && tekstVakken != null; i++)
		{
			for (int j = 0; j < aantalKolommen; j++)
			{
				tekstVakken[i][j].setInterlinie(interlinie);
			}
		}
	}

	public void increaseFont(int size)
	{
		Font oldFont = getFont();
		Font f = new Font(oldFont.getName(), oldFont.getStyle(), oldFont.getSize() + size);
		setFont(f);

		Vector v = geefInteractiePanels();
		for (int i = 0; i < v.size(); i++)
		{
			Object object = v.elementAt(i);
			if (object instanceof TekstInteractiePanelVak)
				((TekstInteractiePanelVak) object).increaseFont(size);
		}
	}

	public void setFont(Font font)
	{
		super.setFont(font);
		//if(tekstVak==null)return;
		//tekstVak.setFont(font);
		for (int i = 0; i < aantalRijen && tekstVakken != null; i++)
		{
			for (int j = 0; j < aantalKolommen; j++)
			{
				tekstVakken[i][j].setFont(font);
			}
		}
		//layoutTekst();
	}

	public TekstVak geefTekstVak(int rij, int kolom)
	{
		if(rij<aantalRijen && kolom<aantalKolommen)
			return tekstVakken[rij][kolom];
		else 
			return null;
	}
	
	
	public Vector geefInteractiePanels()
	{
		Vector v = new Vector();
		for (int i = 0; i < aantalRijen; i++)
		{
			for (int j = 0; j < aantalKolommen; j++)
			{
				tekstVakken[i][j].geefInteractiePanels(v);
			}
		}
		//Vector v = tekstVak.geefInteractiePanels();
		return v;
	}

	public InteractiePanel geefInteractiePanel(int x, int y)
	{
		Vector v = new Vector();
		if (tekstVakken.length < x || tekstVakken[x].length < y)
			return null;

		tekstVakken[x][y].geefInteractiePanels(v);
		if (v.size() > 0)
		{
			InteractiePanelContainerIF ipc = (InteractiePanelContainerIF) v.elementAt(0);
			if (ipc instanceof TekstInteractiePanelVak)
			{
				InteractiePanel ip = ((TekstInteractiePanelVak) ipc).getInteractiePanel();
				return ip;
			}
		}
		return null;
	}
	
	public TekstInteractiePanelVak getWidgetContainer(String crossWidgetId)
	{
		TekstInteractiePanelVak tipv = null;
		for (int i = 0; i < aantalRijen && tekstVakken != null; i++)
		{	for (int j = 0; j < aantalKolommen; j++)
			{	tipv = tekstVakken[i][j].getWidgetContainer(crossWidgetId);
				if(tipv!=null) return tipv;
			}
		}
		return tipv;
	}

	public InteractiePanel getInteractiePanel(int ID)
	{
		for (int i = 0; i < aantalRijen; i++)
		{
			for (int j = 0; j < aantalKolommen; j++)
			{
				InteractiePanel ip = tekstVakken[i][j].zoekInteractiePanel(ID);
				if (ip != null)
					return ip;
			}
		}
		return null;
	}

	public void zetGoedFout(boolean b)
	{
		/*zetTransparant(false);
		if (b)
			setBorder(new Color(50, 225, 50), 5);
		else
			setBorder(new Color(225, 50, 50), 5);
		repaint();*/
		
		Vector v = geefInteractiePanels();
		if (v.size() > 0)
		{	InteractiePanelContainerIF ipc = (InteractiePanelContainerIF) v.elementAt(0);
			if (ipc instanceof TekstInteractiePanelVak)
			{	InteractiePanel ip = ((TekstInteractiePanelVak) ipc).getInteractiePanel();
				if (ip instanceof SimpelAntwoordFormuleVak)
				{	((SimpelAntwoordFormuleVak) ip).zetGoedFout(b?1:0);
				}
			}
		}
	}

	public void wisGoedFout()
	{
		/*zetTransparant(!bgColorZichtbaar);
		if (randZichtbaar)
			setBorder(Color.gray);
		repaint();*/
		
		Vector v = geefInteractiePanels();
		if (v.size() > 0)
		{	InteractiePanelContainerIF ipc = (InteractiePanelContainerIF) v.elementAt(0);
			if (ipc instanceof TekstInteractiePanelVak)
			{	InteractiePanel ip = ((TekstInteractiePanelVak) ipc).getInteractiePanel();
				if (ip instanceof SimpelAntwoordFormuleVak)
				{	((SimpelAntwoordFormuleVak) ip).zetGoedFout(3);
				}
			}
		}
	}
	public void wisGoedFoutSleep()
	{
		zetTransparant(!bgColorZichtbaar);
		if (randZichtbaar)
			setBorder(Color.gray);
		repaint();
	}

	public void zetGoedFoutSleep(boolean b)
	{
		if (sleepObjecten != null && sleepdoel)
		{
			for (int i = 0; i < sleepObjecten.length; i++)
			{
				int dx = Math.abs(geefLocatie().x - sleepObjecten[i].geefLocatie().x);
				int dy = Math.abs(geefLocatie().y - sleepObjecten[i].geefLocatie().y);
				int marge = sleepObjecten[i].geefSleepdoelMarge();
				if (dx < Math.min(1, marge) && dy < Math.min(1, marge))
				{
					sleepObjecten[i].zetGoedFoutSleep(b);
					break;
				}
			}
		}
		if(!sleepdoel)
		{
			zetTransparant(false);
			if (b)
				setBorder(new Color(50, 225, 50), 5);
			else
				setBorder(new Color(225, 50, 50), 5);
			repaint();
		}
	}

	public void zetOpdracht(Hashtable h, String[] randomVars, Hashtable randomValues)
	{
		String styleString = null;
		String tekst = "";
		String[][] teksten = null;
		double[] breedtes = null;
		double[] hoogtes = null;
		boolean randZichtbaar = false;
		boolean bgColorZichtbaar = false;
		boolean zwevend = false;
		boolean anderFont = false;
		Hashtable[] interactiePanelLaunchData = null;
		int locationX = 0;
		int locationY = 0;
		boolean tableBorders = false;
		int cellMarge = 0;
		int bovenMarge = 0;
		int ronding = 0;
		Font font = getFont();
		Color bgColor = null;
		Color fgColor = Color.black;
		Color randColor = Color.gray;
		Color selectieColor = Color.white;
		int hoek = 0;
		boolean centerH = false;
		boolean centerV = false;
		boolean pasAanH = false;
		boolean pasAanB = false;
		boolean selectable = false;
		boolean selected = false;
		boolean colorSelection = false;
		boolean sleepbaar = false;
		boolean sleepdoel = false;
		boolean sleepHandle = false;
		String checkExpressieString = "$f@";
		int ipId = 0;
		int interlinie = 0;
		int cellSpaceColumn = 2;
		int cellSpaceRow = 2;
		int randDikte = 1;
		boolean zichtbaarNaNakijken = false;
		boolean balansVergCom = false;
		boolean aftrekPopup = false;
		int puntenAftrekPopup = 5;
		boolean callOut = false;
		int callOutMargeX0 = 15;
		int callOutMargeY0 = 15;
		int callOutMargeX1 = 5;
		int callOutMargeY1 = 5;
		int callOutPointX = 0;
		int callOutPointY = 0;
		boolean vulHoogte = false;
		boolean inklapbaar = false;
		boolean checkUitklapVak = false;
		int inklapKnopPos = 1;
		String knopImageString1 = "";
		String knopImageString2 = "";
		boolean ingeklapt = true;
		int[] uitklapHoogtes = new int[1];
		boolean random = false;
		int aantalRandom = 1;
		String randomVar = "a";
		String[][][] randomteksten = new String[1][][];
		randomIpLaunchdata = this.randomIpLaunchdata;
		boolean isLink = false;
		boolean defaultBijNull = false;
		String linkUrl = "";
		String[] linkUrls = null;
		int[] grensScores = null;
		int linkWidth = 400;
		int linkHeight = 400;
		boolean logOption = false;
		String logID = "";
		String logIDLabel = "";
		boolean visible = true;
		boolean ideasStatistiek = false;

		Hashtable style = null;
		if(h.containsKey("styleString")) 
			styleString = (String)h.get("styleString");
		if(TekstVakPanel.styles!=null && styleString!=null)
		{	if(TekstVakPanel.styles.containsKey(styleString)) 
				style = (Hashtable)TekstVakPanel.styles.get(styleString);
		}	
		if(style!=null)	
		{	if(style.containsKey("randZichtbaar")) randZichtbaar = ((Boolean)style.get("randZichtbaar")).booleanValue();
			if(style.containsKey("bgColorZichtbaar")) bgColorZichtbaar = ((Boolean)style.get("bgColorZichtbaar")).booleanValue();
				if(bgColorZichtbaar) bgColor = new Color(255,255,180);
			if(style.containsKey("bgColor")) bgColor = (Color)style.get("bgColor");
			if(style.containsKey("fgColor")) fgColor = (Color)style.get("fgColor");
			if(style.containsKey("randColor")) randColor = (Color)style.get("randColor");
			if(style.containsKey("tableBorders")) tableBorders = ((Boolean)style.get("tableBorders")).booleanValue();
			if(style.containsKey("cellMarge")) cellMarge = ((Integer)style.get("cellMarge")).intValue();
			if(style.containsKey("bovenMarge")) bovenMarge = ((Integer)style.get("bovenMarge")).intValue();
		    if(style.containsKey("ronding")) ronding = ((Integer)style.get("ronding")).intValue();
		    if (style.containsKey("anderFont"))
				anderFont = ((Boolean) style.get("anderFont")).booleanValue();
				
		    	if(anderFont) font = new Font("SansSerif", Font.BOLD, 14);
			if(style.containsKey("font")) font = (Font)style.get("font");
			if(style.containsKey("hoek")) hoek = ((Integer)style.get("hoek")).intValue();
			if(style.containsKey("centerH")) centerH = ((Boolean)style.get("centerH")).booleanValue();
			if(style.containsKey("centerV")) centerV = ((Boolean)style.get("centerV")).booleanValue();
			if(style.containsKey("pasAanH")) pasAanH = ((Boolean)style.get("pasAanH")).booleanValue();
			if(style.containsKey("pasAanB")) pasAanB = ((Boolean)style.get("pasAanB")).booleanValue();
			if(style.containsKey("interlinie")) interlinie = ((Integer)style.get("interlinie")).intValue();
			if(style.containsKey("cellSpaceColumn")) cellSpaceColumn = ((Integer)style.get("cellSpaceColumn")).intValue();
			if(style.containsKey("cellSpaceRow")) cellSpaceRow = ((Integer)style.get("cellSpaceRow")).intValue();
			if(style.containsKey("randDikte")) randDikte = ((Integer)style.get("randDikte")).intValue();
		}
		else
		{
			if(h.containsKey("randZichtbaar")) randZichtbaar = ((Boolean)h.get("randZichtbaar")).booleanValue();
			if(h.containsKey("bgColorZichtbaar")) bgColorZichtbaar = ((Boolean)h.get("bgColorZichtbaar")).booleanValue();
				if(bgColorZichtbaar) bgColor = new Color(255,255,180);
			if(h.containsKey("bgColor")) bgColor = (Color)h.get("bgColor");
			if(h.containsKey("fgColor")) fgColor = (Color)h.get("fgColor");
			if(h.containsKey("randColor")) randColor = (Color)h.get("randColor");
			if(h.containsKey("tableBorders")) tableBorders = ((Boolean)h.get("tableBorders")).booleanValue();
			if(h.containsKey("cellMarge")) cellMarge = ((Integer)h.get("cellMarge")).intValue();
			if(h.containsKey("bovenMarge")) bovenMarge = ((Integer)h.get("bovenMarge")).intValue();
	        if(h.containsKey("ronding")) ronding = ((Integer)h.get("ronding")).intValue();
	        if (h.containsKey("anderFont"))
				anderFont = ((Boolean) h.get("anderFont")).booleanValue();
				
	        	if(anderFont) font = new Font("SansSerif", Font.BOLD, 14);
			if(h.containsKey("font")) font = (Font)h.get("font");
			if(h.containsKey("hoek")) hoek = ((Integer)h.get("hoek")).intValue();
			if(h.containsKey("centerH")) centerH = ((Boolean)h.get("centerH")).booleanValue();
			if(h.containsKey("centerV")) centerV = ((Boolean)h.get("centerV")).booleanValue();
			if(h.containsKey("pasAanH")) pasAanH = ((Boolean)h.get("pasAanH")).booleanValue();
			if(h.containsKey("pasAanB")) pasAanB = ((Boolean)h.get("pasAanB")).booleanValue();
			if(h.containsKey("interlinie")) interlinie = ((Integer)h.get("interlinie")).intValue();
			if(h.containsKey("cellSpaceColumn")) cellSpaceColumn = ((Integer)h.get("cellSpaceColumn")).intValue();
			if(h.containsKey("cellSpaceRow")) cellSpaceRow = ((Integer)h.get("cellSpaceRow")).intValue();
			if(h.containsKey("randDikte")) randDikte = ((Integer)h.get("randDikte")).intValue();
		}
		
		
		if (h.containsKey("tekst"))
			tekst = (String) h.get("tekst");
		if (h.containsKey("teksten"))
			teksten = (String[][]) h.get("teksten");
		if (h.containsKey("breedtes"))
			breedtes = (double[]) h.get("breedtes");
		if (h.containsKey("hoogtes"))
			hoogtes = (double[]) h.get("hoogtes");
		if (h.containsKey("zwevend"))
			zwevend = ((Boolean) h.get("zwevend")).booleanValue();
		if (h.containsKey("locationX"))
			locationX = ((Integer) h.get("locationX")).intValue();
		if (h.containsKey("locationY"))
			locationY = ((Integer) h.get("locationY")).intValue();
		if (h.containsKey("interactiePanelLaunchData"))
			interactiePanelLaunchData = (Hashtable[]) h.get("interactiePanelLaunchData");
		if (h.containsKey("selectable"))
			selectable = ((Boolean) h.get("selectable")).booleanValue();
		if (h.containsKey("selected"))
			selected = ((Boolean) h.get("selected")).booleanValue();
		if (h.containsKey("colorSelection"))
			colorSelection = ((Boolean) h.get("colorSelection")).booleanValue();
		if (h.containsKey("selectieColor"))
			selectieColor = (Color)h.get("selectieColor");
		if (h.containsKey("sleepbaar"))
			sleepbaar = ((Boolean) h.get("sleepbaar")).booleanValue();
		if (h.containsKey("sleepdoel"))
			sleepdoel = ((Boolean) h.get("sleepdoel")).booleanValue();
		if (h.containsKey("sleepHandle"))
			sleepHandle = ((Boolean) h.get("sleepHandle")).booleanValue();
		if (h.containsKey("checkExpressieString"))
			checkExpressieString = (String) h.get("checkExpressieString");
		if (h.containsKey("ipId"))
			ipId = ((Integer) h.get("ipId")).intValue();
		/*if (h.containsKey("interlinie"))
			interlinie = ((Integer) h.get("interlinie")).intValue();
		if (h.containsKey("cellSpaceColumn"))
			cellSpaceColumn = ((Integer) h.get("cellSpaceColumn")).intValue();
		if (h.containsKey("cellSpaceRow"))
			cellSpaceRow = ((Integer) h.get("cellSpaceRow")).intValue();
		if (h.containsKey("randDikte"))
			randDikte = ((Integer) h.get("randDikte")).intValue();*/
		if (h.containsKey("zichtbaarNaNakijken"))
			zichtbaarNaNakijken = ((Boolean) h.get("zichtbaarNaNakijken")).booleanValue();
		if (h.containsKey("balansVergCom"))
			balansVergCom = ((Boolean) h.get("balansVergCom")).booleanValue();
		if (h.containsKey("aftrekPopup"))
			aftrekPopup = ((Boolean) h.get("aftrekPopup")).booleanValue();
		if (h.containsKey("puntenAftrekPopup"))
			puntenAftrekPopup = ((Integer) h.get("puntenAftrekPopup")).intValue();
		if (h.containsKey("vulHoogte"))
			vulHoogte = ((Boolean) h.get("vulHoogte")).booleanValue();
		if (h.containsKey("inklapbaar"))
			inklapbaar = ((Boolean) h.get("inklapbaar")).booleanValue();
		if (h.containsKey("inklapKnopPos"))
			inklapKnopPos = ((Integer) h.get("inklapKnopPos")).intValue();
		if(h.containsKey("knopImageString1")) 
			knopImageString1 = (String)h.get("knopImageString1");
		if(h.containsKey("knopImageString2")) 
			knopImageString2 = (String)h.get("knopImageString2");
		if (h.containsKey("ingeklapt"))
			ingeklapt = ((Boolean) h.get("ingeklapt")).booleanValue();
		if (h.containsKey("checkUitklapVak"))
			checkUitklapVak = ((Boolean) h.get("checkUitklapVak")).booleanValue();
		if (h.containsKey("uitklapHoogtes"))
			uitklapHoogtes = (int[])h.get("uitklapHoogtes");
		if (h.containsKey("callOut"))
			callOut = ((Boolean) h.get("callOut")).booleanValue();
		if (h.containsKey("callOutMargeX0"))
			callOutMargeX0 = ((Integer) h.get("callOutMargeX0")).intValue();
		if (h.containsKey("callOutMargeY0"))
			callOutMargeY0 = ((Integer) h.get("callOutMargeY0")).intValue();
		if (h.containsKey("callOutMargeX1"))
			callOutMargeX1 = ((Integer) h.get("callOutMargeX1")).intValue();
		if (h.containsKey("callOutMargeY1"))
			callOutMargeY1 = ((Integer) h.get("callOutMargeY1")).intValue();
		if (h.containsKey("callOutPointX"))
			callOutPointX = ((Integer) h.get("callOutPointX")).intValue();
		if (h.containsKey("callOutPointY"))
			callOutPointY = ((Integer) h.get("callOutPointY")).intValue();
		if (h.containsKey("random"))
			random = ((Boolean) h.get("random")).booleanValue();
		if (h.containsKey("aantalRandom"))
			aantalRandom = ((Integer) h.get("aantalRandom")).intValue();
		if (h.containsKey("randomteksten"))
			randomteksten = (String[][][]) h.get("randomteksten");
		if (h.containsKey("randomIpLaunchdata"))
			randomIpLaunchdata = (Hashtable[][]) h.get("randomIpLaunchdata");
		if (h.containsKey("randomVar"))
			randomVar = (String) h.get("randomVar");
		if (h.containsKey("isLink"))
			isLink = ((Boolean) h.get("isLink")).booleanValue();
		if (h.containsKey("defaultBijNull"))
			defaultBijNull = ((Boolean) h.get("defaultBijNull")).booleanValue();
				
		if (h.containsKey("linkUrl"))
			linkUrl = (String) h.get("linkUrl");
		if (h.containsKey("linkUrls"))
			linkUrls = (String[]) h.get("linkUrls");
		if (h.containsKey("grensScores"))
			grensScores = (int[]) h.get("grensScores");
		if (h.containsKey("linkWidth"))
			linkWidth = ((Integer) h.get("linkWidth")).intValue();
		if (h.containsKey("linkHeight"))
			linkHeight = ((Integer) h.get("linkHeight")).intValue();
		if(h.containsKey("logOption")) 
			logOption = ((Boolean)h.get("logOption")).booleanValue();
        if(h.containsKey("logID")) 
        	logID = (String)h.get("logID");
        if(h.containsKey("logIDLabel")) 
        	logIDLabel = (String)h.get("logIDLabel");
        if(h.containsKey("visible")) 
        	visible = ((Boolean)h.get("visible")).booleanValue();
        if(h.containsKey("ideasStatistiek"))
        	ideasStatistiek = ((Boolean)h.get("ideasStatistiek")).booleanValue();

		this.zichtbaarNaNakijken = zichtbaarNaNakijken;
		this.balansVergCom = balansVergCom;

		if (zichtbaarNaNakijken)
		{
			if (nagekeken)
				setVisible(true);
			else
				setVisible(false);
		}
		try
		{
			checkExpressieString = FormuleParser.randomizeString(checkExpressieString, randomVars, randomValues);
			antwoordExpressie = FormuleParser.geefExpressie(checkExpressieString);
		}
		catch (Exception e)
		{
			checkExpressieString = "$f???@";

		}

		this.randZichtbaar = randZichtbaar;
		this.bgColorZichtbaar = bgColorZichtbaar;
		this.anderFont = anderFont;
		this.zwevend = zwevend;
		this.locationX = locationX;
		this.locationY = locationY;
		this.tableBorders = tableBorders;
		this.cellMarge = cellMarge;
		this.bovenMarge = bovenMarge;
		this.ronding = ronding;
		this.bgColor = bgColor;
		this.fgColor = fgColor;
		this.randColor = randColor;
		this.selectieColor = selectieColor;
		this.hoek = hoek;
		this.centerV = centerV;
		this.centerH = centerH;
		this.pasAanH = pasAanH;
		this.pasAanB = pasAanB;
		this.selectable = selectable;
		this.selected = selected;
		this.colorSelection = colorSelection;
		this.sleepbaar = sleepbaar;
		this.sleepdoel = sleepdoel;
		this.sleepHandle = sleepHandle;
		this.checkExpressieString = checkExpressieString;
		this.ipId = ipId;
		this.interlinie = interlinie;
		this.cellSpaceColumn = cellSpaceColumn;
		this.cellSpaceRow = cellSpaceRow;
		this.randDikte = randDikte;
		this.aftrekPopup = aftrekPopup;
		this.puntenAftrekPopup = puntenAftrekPopup;
		this.vulHoogte = vulHoogte;
		this.inklapbaar = inklapbaar;
		this.checkUitklapVak = checkUitklapVak;
		this.uitklapHoogtes = uitklapHoogtes;
		this.inklapKnopPos = inklapKnopPos;
		this.knopImageString1 = knopImageString1;
		this.knopImageString2 = knopImageString2;
		this.ingeklapt = ingeklapt;
		this.callOutMargeX0 = callOutMargeX0;
		this.callOutMargeX1 = callOutMargeX1;
		this.callOutMargeY0 = callOutMargeY0;
		this.callOutMargeY1 = callOutMargeY1;
		this.callOutPointX = callOutPointX;
		this.callOutPointY = callOutPointY;
		this.isLink = isLink;
		this.defaultBijNull = defaultBijNull;
		
		this.logOption = logOption;
	    this.logID = logID;
	    this.logIDLabel = logIDLabel;
	    this.visible = visible;
	    this.ideasStatistiek = ideasStatistiek;
	    
		if (isLink) {
			if(!linkUrl.equals("")) {
				linkUrls = new String[1];
				linkUrls[0] = linkUrl;
			}
		
			//link = new Link("", linkUrl, linkWidth, linkHeight);
			link = new Link("", linkUrls, linkWidth, linkHeight, false, grensScores);
		}
		setCallOut(callOut);

		if (teksten == null)
		{
			teksten = new String[1][1];
			teksten[0][0] = tekst;
		}

		this.aantalRijen = teksten.length;
		this.aantalKolommen = teksten[0].length;
		
		tekstVakken = new TekstVak[aantalRijen][aantalKolommen];
		removeAll();
		
		for (int i = 0; i < aantalRijen; i++)
		{
			for (int j = 0; j < aantalKolommen; j++)
			{
				tekstVakken[i][j] = new TekstVak();
				tekstVakken[i][j].addActionListener(this);
				tekstVakken[i][j].addMouseListener(this);
				add(tekstVakken[i][j]);
				tekstVakken[i][j].setLocation(0, 0);
				tekstVakken[i][j].setEditable(false);
				//tekstVak.setSelectable(false);
				tekstVakken[i][j].setBackground(WiskOpdr.bgcolor);
				tekstVakken[i][j].zetMarge(cellMarge);
				tekstVakken[i][j].zetBovenMarge(bovenMarge);
				tekstVakken[i][j].setCenterH(centerH);
				tekstVakken[i][j].setCenterV(centerV);
				tekstVakken[i][j].setInterlinie(interlinie);
			}
		}

		if (breedtes == null || hoogtes == null)
		{
			if (breedtes == null)
				breedtes = new double[aantalKolommen];
			if (hoogtes == null)
				hoogtes = new double[aantalRijen];
			this.breedtes = breedtes;
			this.hoogtes = hoogtes;
			initializeTableBounds(getSize().width, getSize().height);
		}
		else
		{
			this.breedtes = breedtes;
			this.hoogtes = hoogtes;
			setTableBounds();
		}
		
		

		/*if(teksten==null) 
		{	teksten = new String[1][1];
			teksten[0][0] = tekst;
		}
		if(breedtes==null) 
		{	breedtes = new int[1];
		}
		if(hoogtes==null) 
		{	hoogtes = new int[1];
		}
		this.aantalRijen = teksten.length;
		this.aantalKolommen = teksten[0].length;
		this.breedtes = breedtes;
		this.hoogtes = hoogtes;
		
		setTableBounds();
		*/
		if (random)
		{
			for (int i = 0; i < randomVars.length; i++)
			{
				if (randomVar.equals(randomVars[i]))
				{
					int tabNummer = ((Number) randomValues.get(randomVar)).intValue() - 1;
					if (tabNummer < aantalRandom && tabNummer > -1)
					{
						teksten = randomteksten[tabNummer];
						interactiePanelLaunchData = randomIpLaunchdata[tabNummer];
					}
					break;
				}
			}
		}

		for (int i = 0; i < aantalRijen; i++)
		{
			for (int j = 0; j < aantalKolommen; j++)
			{
				try
				{
					teksten[i][j] = FormuleParser.randomizeTekstVakString(teksten[i][j], randomVars, randomValues);
				}
				catch (Exception e)
				{
					teksten[i][j] = "???";
					Logger.getLogger(getClass().getName()).log(Level.WARNING, "teksten "+ j + " " + j, e);
				}
			}
		}
		//if(anderFont) setFont(new Font("SansSerif", Font.BOLD, WiskOpdr.tekstFont.getSize()*7/6));
		

		for (int i = 0; i < aantalRijen; i++)
		{
			for (int j = 0; j < aantalKolommen; j++)
			{
				tekstVakken[i][j].zetTekst(teksten[i][j]);
				tekstVakken[i][j].setEditable(false);
			}
		}

		//tekstVak.zetTekst(tekst);
		//tekstVak.setEditable(false);

		setCurvature(ronding);

		if (randZichtbaar && "GR".equals(WiskOpdr.deployVariant) && randColor.getRed()==128 && randColor.getGreen()==128 && randColor.getBlue()==128)
			setBorder(new Color(70, 116, 183), 1);
		else if (randZichtbaar)
			setBorder(randColor, randDikte);
		else
			setBorder(randColor, 0);
		if (bgColorZichtbaar)
		{
			zetTransparant(false);
			setBackground(bgColor);

		}
		else
			zetTransparant(true);//setBackground(WiskOpdr.bgcolor);

		setForeground(fgColor);
		
		Font f = new Font(font.getName(), font.getStyle(), font.getSize());
		Font geerftFont;
		Color fgColorOvererving = fgColor;
		if (anderFont)
		{	setFont(f);
			setForeground(fgColor);
			//layoutTekst();
		}
		else if(fontOvererving && getParent() instanceof TekstInteractiePanelVak)
		{	geerftFont = ((TekstInteractiePanelVak)getParent()).getTekstVak().getFont();
			fgColorOvererving = ((TekstInteractiePanelVak)getParent()).getTekstVak().getForeground();
			setFont(geerftFont);
			setForeground(fgColorOvererving);
			//layoutTekst();
		}

		Vector v = geefInteractiePanels();
		for (int i = 0; i < v.size(); i++)
		{
			((InteractiePanelContainerIF) v.elementAt(i)).zetOpdracht(interactiePanelLaunchData[i], randomVars, randomValues);
			((InteractiePanelContainerIF) v.elementAt(i)).addActionListener(this);
		}
		layoutTekst();
		
		if (selectable || isLink)
			zetKlikPanel(true);
		
		if (selected)//setBorder(Color.gray,5);
		{
			if (colorSelection)
				setBorder(selectieColor, 400);
			else
				setBorder(Color.gray, 5);
		}

		if (balansVergCom)
			new BalansVergCom(this);
		
		
		if(inklapbaar)
		{	initieerKlapUitButton(ingeklapt);
			zetMaat();
		}
		
		if(getParent() instanceof TekstInteractiePanelVak)
		{	Map subscr = ((TekstInteractiePanelVak) getParent()).getSubscriptions();
			if(subscr!=null && subscr.containsKey("text.content"))
			{	stappen = new String[aantalRijen];
				zetMaat();
			}
		}
		
		this.randomVars = randomVars;
		this.randomValues = randomValues;
		
		//Verplaatst naar boven, ivm fontovererving. Nodig om font te zetten voordat de kinderen worden gezet.
		//Font f = new Font(font.getName(), font.getStyle(), font.getSize());
		
		if (anderFont)
		{	//setFont(f);
			setForeground(fgColor);
			layoutTekst();
		}
		else if(fontOvererving && getParent() instanceof TekstInteractiePanelVak)
		{	//Font geerftFont = ((TekstInteractiePanelVak)getParent()).getTekstVak().getFont();
			//Color fgColorOvererving = ((TekstInteractiePanelVak)getParent()).getTekstVak().getForeground();
			//setFont(geerftFont);
			setForeground(fgColorOvererving);
			layoutTekst();
		}
		setVisible(visible);
	}
	
	public void initConnections(XWidgetManager manager)
	{	Vector v = geefInteractiePanels();
		for (int i = 0; i < v.size(); i++)
		{	((InteractiePanelContainerIF) v.elementAt(i)).initConnections(manager);
		}
	}
	
	private void initieerKlapUitButton (boolean ingeklapt)
	{
		klapUitButton = new JToggleButton();
		klapUitButton.setIcon(new ImageIcon(WiskOpdr.class.getResource("resources/klapuit1.png")));
		klapUitButton.setSelectedIcon(new ImageIcon(WiskOpdr.class.getResource("resources/klapuit2.png")));
		klapUitButton.setSize(checkUitklapVak?30:15,15);
		
		if(knopImageString1!=null && !"".equals(knopImageString1))
       	{  	Iconan iconman = new Iconan(WiskOpdr.applet, (Component)this, (Hashtable)TekstImageVak.getImageMap());
			Image knopImage1 = iconman.getImage(knopImageString1);
	    	if(knopImage1!=null) 
	    		klapUitButton.setIcon(new ImageIcon(knopImage1));
		    int imWidth = iconman.getWidth(knopImageString1);
			int imHeight = iconman.getHeight(knopImageString1);
			if(imWidth == -1) imWidth = checkUitklapVak?30:15;
			if(imHeight == -1) imHeight = 15;
			klapUitButton.setSize(imWidth,imHeight);
		}
		
		if(knopImageString2!=null && !"".equals(knopImageString2))
       	{  	Iconan iconman = new Iconan(WiskOpdr.applet, (Component)this, (Hashtable)TekstImageVak.getImageMap());
			Image knopImage2 = iconman.getImage(knopImageString2);
			if(knopImage2!=null) 
	    		klapUitButton.setSelectedIcon(new ImageIcon(knopImage2));
		}
		
		klapUitButton.setSelected(!ingeklapt);	
		//int klapUitButtonWidth = 2*tekstVakken[0][0].getHeight()/3;
		klapUitButton.addActionListener(this);
		klapUitButton.setBackground(Color.white);
		klapUitButton.setBorder(BorderFactory.createEmptyBorder());
		klapUitButton.setMargin(new Insets(0,0,0,0));
		setKlapUitButtonLocation();
		//klapUitButton.setBounds(1,1,klapUitButtonWidth, tekstVakken[0][0].getHeight()-2);
		//klapUitButton.setFont(new Font("SansSerif",Font.PLAIN,klapUitButton.getHeight()-2));
		add(klapUitButton,0);

		
		
		if(inklapKnopPos==0)
			tekstVakken[0][0].setBounds(klapUitButton.getWidth(),0,tekstVakken[0][0].getWidth()-klapUitButton.getWidth(),tekstVakken[0][0].getHeight());
		if(ingeklapt && getParent() instanceof TekstInteractiePanelVak)
		{	TekstInteractiePanelVak tipv = ((TekstInteractiePanelVak) getParent());
			tipv.setSize(getWidth(),tekstVakken[0][0].getHeight());
			tipv.getTekstVak().zetMaat();
			//klapUitButton.setText("\u25b6");
		}
		//else
			//klapUitButton.setText("\u25be");
	}

	public void setTableBorders(boolean b)
	{
		tableBorders = b;
		repaint();
	}

	public void setNumberColums(int aantal)
	{
		removeAll();
		aantalKolommen = aantal;
		initializeTableBounds(getSize().width, getSize().height);
		setTableBounds();
		repaint();
	}

	public void setNumberRows(int aantal)
	{
		removeAll();
		aantalRijen = aantal;
		initializeTableBounds(getSize().width, getSize().height);
		setTableBounds();
		repaint();

	}

	public void zetBreedte(int b)
	{
		int bOud = getSize().width;
		setSize(b, getSize().height);
		//setTableBounds(b,getSize().height,bOud,getSize().height);
		layoutTekst();
		zetMaat();
		repaint();
	}

	public void zetHoogte(int h)
	{
		int hOud = getSize().height;
		setSize(getSize().width, h);
		//setTableBounds(getSize().width,h,getSize().width,hOud);
		layoutTekst();
		zetMaat();
		repaint();
	}

	public void setBounds(int x, int y, int b, int h)
	{ //tekstVak.setBounds(0,0,b,h);
		//int dBreedte = b - getSize().width;
		//int dHoogte = h - getSize().height;
		super.setBounds(x, y, b, h);
		if (breedtes == null)
			initializeTableBounds(b, h);
		//else 
		{ //breedtes[aantalKolommen-1] += dBreedte;
			//hoogtes[aantalRijen-1] += dHoogte;
		}
		setTableBounds();
		if (callOut)
			setCallOutBox(callOutMargeX0, callOutMargeY0, callOutMargeX1, callOutMargeY1);
	}

	public void setSize(int b, int h)
	{ //tekstVak.setBounds(0,0,b,h);
		//int bOud = getSize().width;
		//int hOud = getSize().height;
		//int dBreedte = b - bOud;
		//int dHoogte = h - hOud;
		super.setSize(b, h);
		//if(breedtes==null)initializeTableBounds(b, h);
		//else 
		//{	//breedtes[aantalKolommen-1] += dBreedte;
		//hoogtes[aantalRijen-1] += dHoogte;
		//setTableBounds(b, h, bOud, hOud);
		//}
		//setTableBounds();
		//zetMaat();
	}

	public void initializeTableBounds(int b, int h)
	{
		breedtes = new double[aantalKolommen];
		hoogtes = new double[aantalRijen];
		int cellB = b / aantalKolommen - cellSpaceColumn;
		int cellH = h / aantalRijen - cellSpaceRow;
		int restB = b - (aantalKolommen - 1) * (cellB + cellSpaceColumn);
		int restH = h - (aantalRijen - 1) * (cellH + cellSpaceRow);
		tekstVakken = new TekstVak[aantalRijen][aantalKolommen];
		removeAll();
		for (int i = 0; i < aantalRijen; i++)
		{
			for (int j = 0; j < aantalKolommen; j++)
			{
				if (i == 0)
					breedtes[j] = j == aantalKolommen - 1 ? restB : cellB;
				tekstVakken[i][j] = new TekstVak();
				tekstVakken[i][j].addActionListener(this);
				tekstVakken[i][j].addMouseListener(this);
				add(tekstVakken[i][j]);
				tekstVakken[i][j].setLocation(0, 0);
				tekstVakken[i][j].setEditable(true);
				//tekstVak.setSelectable(false);
				tekstVakken[i][j].setBackground(WiskOpdr.bgcolor);
				tekstVakken[i][j].zetMarge(0);
				tekstVakken[i][j].zetBovenMarge(0);
			}
			hoogtes[i] = i == aantalRijen - 1 ? restH : cellH;
		}
	}

	public void setTableBounds()
	{
		int b = getSize().width;
		int h = getSize().height;
		//initializeTableBounds(300,300);
		dragColomsRects = new Rectangle[aantalKolommen];
		dragRowsRects = new Rectangle[aantalRijen];
		double hoogteCum = 0;
		for (int i = 0; i < aantalRijen; i++)
		{
			double breedteCum = 0;
			if (i == aantalRijen - 1)
				hoogtes[i] = (double) h - hoogteCum;
			for (int j = 0; j < aantalKolommen; j++)
			{
				if (j == aantalKolommen - 1)
					breedtes[j] = (double) b - breedteCum;
				tekstVakken[i][j].setBounds((int) Math.round(breedteCum), (int) Math.round(hoogteCum), (int) Math.round(breedtes[j]), (int) Math.round(hoogtes[i]));
				breedteCum = breedteCum + breedtes[j] + cellSpaceColumn;
				if (i == 0)
					dragColomsRects[j] = new Rectangle((int) Math.round(breedteCum) - cellSpaceColumn, 0, cellSpaceColumn, getSize().height);

			}
			hoogteCum = hoogteCum + hoogtes[i] + cellSpaceRow;
			dragRowsRects[i] = new Rectangle(0, (int) Math.round(hoogteCum) - cellSpaceRow, getSize().width, cellSpaceRow);
		}
		if(inklapbaar && inklapKnopPos==0)
		{	int xt = klapUitButton!=null ? klapUitButton.getWidth() : 0;
			int yt = 0;
			int wt = tekstVakken[0][0].getWidth()-xt;
			int ht = tekstVakken[0][0].getHeight();
			tekstVakken[0][0].setBounds(xt,yt,wt,ht);
		}
	}

	public void setTableBounds(int b, int h, int bOud, int hOud)
	{
		//initializeTableBounds(300,300);
		dragColomsRects = new Rectangle[aantalKolommen];
		dragRowsRects = new Rectangle[aantalRijen];
		double hoogteCum = 0;
		for (int i = 0; i < aantalRijen; i++)
		{
			double breedteCum = 0;
			hoogtes[i] = hoogtes[i] * h / hOud;
			if (i == aantalRijen - 1)
				hoogtes[i] = h - hoogteCum;
			hoogteCum = hoogteCum + hoogtes[i] + cellSpaceRow;
			dragRowsRects[i] = new Rectangle(0, (int) Math.round(hoogteCum) - cellSpaceRow, getSize().width, cellSpaceRow);

			for (int j = 0; j < aantalKolommen; j++)
			{
				if (i == 0)
					breedtes[j] = breedtes[j] * b / bOud;
				if (i == 0)
					if (j == aantalKolommen - 1)
						breedtes[j] = b - breedteCum;
				tekstVakken[i][j].setBounds((int) Math.round(breedteCum), (int) Math.round(hoogteCum), (int) Math.round(breedtes[j]), (int) Math.round(hoogtes[i]));
				breedteCum = breedteCum + breedtes[j] + cellSpaceColumn;
				if (i == 0)
					dragColomsRects[j] = new Rectangle((int) Math.round(breedteCum) - cellSpaceColumn, 0, cellSpaceColumn, getSize().height);

			}
		}
		if (callOut)
		{
			if (callOutPointX > getWidth())
				callOutPointX = getWidth();
			if (callOutPointY > getHeight())
				callOutPointY = getHeight();
			if (callOutPointY != 0 && getWidth() - callOutPointX < getHeight() - callOutPointY && callOutPointX > getWidth() / 2)
				callOutPointX = getWidth();
			if (callOutPointX != 0 && getWidth() - callOutPointX > getHeight() - callOutPointY && callOutPointY > getHeight() / 2)
				callOutPointY = getHeight();
		}
		
		if(inklapbaar && inklapKnopPos==0)
		{	int xt = klapUitButton!=null ? klapUitButton.getWidth() : 0;
			int yt = 0;
			int wt = tekstVakken[0][0].getWidth()-xt;
			int ht = tekstVakken[0][0].getHeight();
			tekstVakken[0][0].setBounds(xt,yt,wt,ht);
		}
		
	}

	public void dragTableBoundsControl(int d)
	{
		if (dragModeColoms)
		{
			double cum = 0;
			for (int i = 0; i < dragNumber + 1; i++)
			{
				cum += breedtes[i];
			}
			breedtes[dragNumber] += d;
			double factor = ((double) getSize().width - cum - d) / ((double) getSize().width - cum);
			for (int i = dragNumber + 1; i < aantalKolommen; i++)
			{
				breedtes[i] *= factor;
			}
		}
		if (dragModeRows)
		{
			double cum = 0;
			for (int i = 0; i < dragNumber + 1; i++)
			{
				cum += hoogtes[i];
			}
			hoogtes[dragNumber] += d;
			double factor = ((double) getSize().height - cum - d) / ((double) getSize().height - cum);
			for (int i = dragNumber + 1; i < aantalRijen; i++)
			{
				hoogtes[i] *= factor;
			}
		}
		dragColomsRects = new Rectangle[aantalKolommen];
		dragRowsRects = new Rectangle[aantalRijen];
		double hoogteCum = 0;
		for (int i = 0; i < aantalRijen; i++)
		{
			hoogteCum = hoogteCum + hoogtes[i] + cellSpaceRow;
			dragRowsRects[i] = new Rectangle(0, (int) Math.round(hoogteCum) - cellSpaceRow, getSize().width, cellSpaceRow);
			double breedteCum = 0;
			for (int j = 0; j < aantalKolommen; j++)
			{
				tekstVakken[i][j].setBounds((int) Math.round(breedteCum), (int) Math.round(hoogteCum), (int) Math.round(breedtes[j]), (int) Math.round(hoogtes[i]));
				if (i == 0)
					breedteCum = breedteCum + breedtes[j] + cellSpaceColumn;
				if (i == 0)
					dragColomsRects[j] = new Rectangle((int) Math.round(breedteCum) - cellSpaceColumn, 0, cellSpaceColumn, getSize().height);

			}
		}
	}

	public void dragTableBoundsShift(int d)
	{
		if (dragModeColoms)
		{
			super.setSize(getSize().width + d, getSize().height);
			breedtes[dragNumber] += d;

		}
		if (dragModeRows)
		{
			super.setSize(getSize().width, getSize().height + d);
			hoogtes[dragNumber] += d;
		}
		dragColomsRects = new Rectangle[aantalKolommen];
		dragRowsRects = new Rectangle[aantalRijen];
		double hoogteCum = 0;
		for (int i = 0; i < aantalRijen; i++)
		{
			hoogteCum = hoogteCum + hoogtes[i] + cellSpaceRow;
			dragRowsRects[i] = new Rectangle(0, (int) Math.round(hoogteCum) - cellSpaceRow, getSize().width, cellSpaceRow);
			double breedteCum = 0;
			for (int j = 0; j < aantalKolommen; j++)
			{
				tekstVakken[i][j].setBounds((int) Math.round(breedteCum), (int) Math.round(hoogteCum), (int) Math.round(breedtes[j]), (int) Math.round(hoogtes[i]));
				if (i == 0)
					breedteCum = breedteCum + breedtes[j] + cellSpaceColumn;
				if (i == 0)
					dragColomsRects[j] = new Rectangle((int) Math.round(breedteCum) - cellSpaceColumn, 0, cellSpaceColumn, getSize().height);

			}
		}
		zetMaat();
	}

	public void wis()
	{
	}

	public void zetMode(int mode)
	{
		Vector v = geefInteractiePanels();
		for (int i = 0; i < v.size(); i++)
		{
			((InteractiePanelContainerIF) v.elementAt(i)).zetMode(mode);
		}
	}

	public void stop()
	{
		Vector v = geefInteractiePanels();
		for (int i = 0; i < v.size(); i++)
		{
			((InteractiePanelContainerIF) v.elementAt(i)).stop();
		}
	}
	
	public void closePopup()
	{
		Vector v = geefInteractiePanels();
		for (int i = 0; i < v.size(); i++)
		{
			((InteractiePanelContainerIF) v.elementAt(i)).closePopup();
		}
	}

	public void start()
	{
		Vector v = geefInteractiePanels();
		for (int i = 0; i < v.size(); i++)
		{
			((InteractiePanelContainerIF) v.elementAt(i)).start();
		}
	}

	public boolean zetFocus()
	{
		Vector v = geefInteractiePanels();
		focusNr = 0;
		boolean focusGezet = false;
		for (int i = 0; i < v.size() && !focusGezet; i++)
		{
			if (((InteractiePanelContainerIF) v.elementAt(focusNr)) != null)
				focusGezet = ((InteractiePanelContainerIF) v.elementAt(focusNr)).zetFocus();
			if (focusGezet)
			{
				break;

			}
			focusNr = (focusNr + 1) % v.size();

		}
		return focusGezet;
	}

	public void verplaatsFocus()
	{
		Vector v = geefInteractiePanels();
		if (focusNr == v.size() - 1)
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
			return;
		}
		boolean focusGezet = false;
		for (int i = 0; i < v.size() && !focusGezet; i++)
		{
			focusNr = (focusNr + 1) % v.size();
			if (((InteractiePanelContainerIF) v.elementAt(focusNr)) != null)
				focusGezet = ((InteractiePanelContainerIF) v.elementAt(focusNr)).zetFocus();
			if (focusGezet)
				break;
		}
		//System.out.println("tab");
	}

	public void destroy()
	{
		Vector v = geefInteractiePanels();
		for (int i = 0; i < v.size(); i++)
		{
			((InteractiePanelContainerIF) v.elementAt(i)).destroy();
		}
	}

	//public void layoutTekst()
	//{
	//	tekstVak.layoutTekst();
	//}

	public int geefAsHoogte()
	{
		if (tekstVakken[0][0] != null)
			return tekstVakken[0][0].getAsHoogte();
		return 0;
	}

	XWidgetManager manager;
	public InteractieEditPanel getEditPanel()
	{
		if(manager == null)
		{
			manager = tekstVak.getXWidgetManager();
		}
		return new TekstVakEditPanel(manager);
	}

	public Hashtable getEditState()
	{
		String styleString = null;
		//String tekst = "";
		String[][] teksten = null;
		double[] breedtes = null;
		double[] hoogtes = null;
		boolean randZichtbaar = false;
		boolean bgColorZichtbaar = false;
		boolean zwevend = false;
		int locationX = 0;
		int locationY = 0;
		boolean anderFont = false;
		Hashtable[] interactiePanelLaunchData = null;
		int scoreMax = 0;
		int[][] scoreMaxObjectives = null;
		boolean tableBorders = false;
		int cellMarge = 0;
		int bovenMarge = 0;
		int ronding = 0;
		Font f = getFont();
		Font font = WiskOpdr.tekstFont;
		if (f != null)
			font = new Font(f.getFontName(), f.getStyle(), f.getSize());
		Color bgColor = null;
		Color fgColor = Color.black;
		Color randColor = Color.gray;
		Color selectieColor = new Color(255, 128, 0, 128);;
		int hoek = 0;
		boolean centerH = false;
		boolean centerV = false;
		boolean pasAanH = false;
		boolean pasAanB = false;
		boolean selectable = false;
		boolean selected = false;
		boolean colorSelection = false;
		boolean sleepbaar = false;
		boolean sleepdoel = false;
		boolean sleepHandle = false;
		String checkExpressieString = "$f@";
		int ipId = 0;
		int interlinie = 0;
		int cellSpaceColumn = 2;
		int cellSpaceRow = 2;
		int randDikte = 1;
		boolean zichtbaarNaNakijken = false;
		boolean balansVergCom = false;
		boolean aftrekPopup = false;
		int puntenAftrekPopup = 5;
		boolean vulHoogte = false;
		boolean inklapbaar = false;
		boolean checkUitklapVak = false;
		int inklapKnopPos = 1;
		String knopImageString1 = "";
		String knopImageString2 = "";
		boolean ingeklapt = true;
		int[] uitklapHoogtes = new int[aantalRijen];
		boolean callOut = false;
		int callOutMargeX0 = 15;
		int callOutMargeY0 = 15;
		int callOutMargeX1 = 5;
		int callOutMargeY1 = 5;
		int callOutPointX = 0;
		int callOutPointY = 0;
		String[][][] randomteksten = new String[1][][];
		Hashtable[][] randomIpLaunchdata = new Hashtable[1][];
		int aantalRandom = 1;
		boolean random = false;
		String randomVar = "a";
		boolean isLink = false;
		boolean defaultBijNull = false;
		//String linkUrl = "";
		String[] linkUrls = null;
		int[] grensScores = null;
		int linkWidth = 400;
		int linkHeight = 400;
		boolean logOption = false;
		String logID = "";
		String logIDLabel = "";
		boolean visible = true;
		boolean ideasStatistiek = false;

		styleString = this.styleString;
		randZichtbaar = this.randZichtbaar;
		bgColorZichtbaar = this.bgColorZichtbaar;
		zwevend = this.zwevend;
		anderFont = this.anderFont;
		tableBorders = this.tableBorders;
		cellMarge = this.cellMarge;
		bovenMarge = this.bovenMarge;
		ronding = this.ronding;
		bgColor = this.bgColor;
		fgColor = this.fgColor;
		randColor = this.randColor;
		selectieColor = this.selectieColor;
		hoek = this.hoek;
		centerH = this.centerH;
		centerV = this.centerV;
		pasAanH = this.pasAanH;
		pasAanB = this.pasAanB;
		selectable = this.selectable;
		selected = this.selected;
		colorSelection = this.colorSelection;
		sleepbaar = this.sleepbaar;
		sleepdoel = this.sleepdoel;
		sleepHandle = this.sleepHandle;
		checkExpressieString = this.checkExpressieString;
		ipId = this.ipId;
		locationX = this.locationX;
		locationY = this.locationY;
		interlinie = this.interlinie;
		cellSpaceColumn = this.cellSpaceColumn;
		cellSpaceRow = this.cellSpaceRow;
		randDikte = this.randDikte;
		zichtbaarNaNakijken = this.zichtbaarNaNakijken;
		balansVergCom = this.balansVergCom;
		aftrekPopup = this.aftrekPopup;
		puntenAftrekPopup = this.puntenAftrekPopup;
		vulHoogte = this.vulHoogte;
		inklapbaar = this.inklapbaar;
		checkUitklapVak = this.checkUitklapVak;
		inklapKnopPos = this.inklapKnopPos;
		knopImageString1 = this.knopImageString1;
		knopImageString2 = this.knopImageString2;
		ingeklapt = this.ingeklapt;
		uitklapHoogtes = this.uitklapHoogtes;
		callOut = this.callOut;
		callOutMargeX0 = this.callOutMargeX0;
		callOutMargeX1 = this.callOutMargeX1;
		callOutMargeY0 = this.callOutMargeY0;
		callOutMargeY1 = this.callOutMargeY1;
		callOutPointX = this.callOutPointX;
		callOutPointY = this.callOutPointY;
		randomteksten = this.randomteksten;
		randomIpLaunchdata = this.randomIpLaunchdata;
		aantalRandom = this.aantalRandom;
		random = this.random;
		randomVar = this.randomVar;
		isLink = this.isLink;
		defaultBijNull = this.defaultBijNull;
		//linkUrl = link.getUrl();
		linkUrls = link.getUrlString();
		grensScores = link.getGrensScores();
		linkWidth = link.getWidth();
		linkHeight = link.getHeight();
		logOption = this.logOption;
		logID = this.logID;
		logIDLabel = this.logIDLabel;
		visible = this.visible;
		ideasStatistiek = this.ideasStatistiek;

		if (WiskOpdr.objectives != null)
		{
			scoreMaxObjectives = new int[WiskOpdr.objectives.length][];
			for (int i = 0; i < WiskOpdr.objectives.length; i++)
				scoreMaxObjectives[i] = new int[WiskOpdr.objectives[i].length];
		}

		Vector v = geefInteractiePanels();
		interactiePanelLaunchData = new Hashtable[v.size()];
		for (int i = 0; i < v.size(); i++)
		{
			interactiePanelLaunchData[i] = ((InteractiePanelContainerIF) v.elementAt(i)).getEditState();
			scoreMax += ((InteractiePanelContainerIF) v.elementAt(i)).getScoreMax();
			int[][] ob = ((InteractiePanelContainerIF) v.elementAt(i)).getScoreMaxObjectives();

			for (int j = 0; scoreMaxObjectives != null && ob != null && j < scoreMaxObjectives.length && j < ob.length; j++)
				for (int k = 0; scoreMaxObjectives[j] != null && k < scoreMaxObjectives[j].length && k < ob[j].length; k++)
				{
					scoreMaxObjectives[j][k] += ob[j][k];
				}
		}

		Hashtable h = new Hashtable();

		//tekst = tekstVak.toString();

		teksten = new String[aantalRijen][aantalKolommen];
		for (int i = 0; i < aantalRijen; i++)
		{
			for (int j = 0; j < aantalKolommen; j++)
			{
				teksten[i][j] = tekstVakken[i][j].toString();
			}
		}
		breedtes = this.breedtes;
		hoogtes = this.hoogtes;

		if(styleString!=null)
			h.put("styleString", styleString);
		
		{
			h.put("randZichtbaar", new Boolean(randZichtbaar));
			h.put("bgColorZichtbaar", new Boolean(bgColorZichtbaar));
			if(bgColor!=null)h.put("bgColor", bgColor);
			h.put("fgColor", fgColor);
			h.put("randColor", randColor);
			h.put("anderFont", new Boolean(anderFont));
			h.put("font", font);
			h.put("tableBorders", new Boolean(tableBorders));
			h.put("cellMarge",new Integer(cellMarge));
			h.put("bovenMarge",new Integer(bovenMarge));
			h.put("ronding",new Integer(ronding));
			h.put("hoek",new Integer(hoek));
			h.put("centerH", new Boolean(centerH));
			h.put("centerV", new Boolean(centerV));
			h.put("pasAanH", new Boolean(pasAanH));
			h.put("pasAanB", new Boolean(pasAanB));
			h.put("interlinie",new Integer(interlinie));
			h.put("cellSpaceColumn",new Integer(cellSpaceColumn));
			h.put("cellSpaceRow",new Integer(cellSpaceRow));
			h.put("randDikte",new Integer(randDikte));
		}
		
		h.put("teksten", teksten);
		h.put("breedtes", breedtes);
		h.put("hoogtes", hoogtes);
		h.put("zwevend", new Boolean(zwevend));
		h.put("locationX", new Integer(locationX));
		h.put("locationY", new Integer(locationY));
		h.put("interactiePanelLaunchData", interactiePanelLaunchData);
		h.put("scoreMax", new Integer(scoreMax));
		if (scoreMaxObjectives != null)
			h.put("scoreMaxObjectives", scoreMaxObjectives);
		h.put("selectable", new Boolean(selectable));
		h.put("selected", new Boolean(selected));
		h.put("colorSelection", new Boolean(colorSelection));
		h.put("selectieColor", selectieColor);
		h.put("sleepbaar", new Boolean(sleepbaar));
		h.put("sleepdoel", new Boolean(sleepdoel));
		h.put("sleepHandle", new Boolean(sleepHandle));
		h.put("checkExpressieString", checkExpressieString);
		h.put("ipId", new Integer(ipId));
		h.put("interlinie", new Integer(interlinie));
		h.put("cellSpaceColumn", new Integer(cellSpaceColumn));
		h.put("cellSpaceRow", new Integer(cellSpaceRow));
		h.put("randDikte", new Integer(randDikte));
		h.put("zichtbaarNaNakijken", new Boolean(zichtbaarNaNakijken));
		h.put("balansVergCom", new Boolean(balansVergCom));
		h.put("aftrekPopup", new Boolean(aftrekPopup));
		h.put("puntenAftrekPopup", new Integer(puntenAftrekPopup));
		h.put("vulHoogte", new Boolean(vulHoogte));
		h.put("inklapbaar", new Boolean(inklapbaar));
		if(inklapbaar)
		{
			h.put("knopImageString1", knopImageString1);
			h.put("knopImageString2", knopImageString2);
			h.put("ingeklapt", new Boolean(ingeklapt));
			h.put("uitklapHoogtes", uitklapHoogtes);
			h.put("inklapKnopPos", inklapKnopPos);
			h.put("checkUitklapVak", new Boolean(checkUitklapVak));
		}
		h.put("callOut", new Boolean(callOut));
		h.put("callOutMargeX0", new Integer(callOutMargeX0));
		h.put("callOutMargeX1", new Integer(callOutMargeX1));
		h.put("callOutMargeY0", new Integer(callOutMargeY0));
		h.put("callOutMargeY1", new Integer(callOutMargeY1));
		h.put("callOutPointX", new Integer(callOutPointX));
		h.put("callOutPointY", new Integer(callOutPointY));
		h.put("isLink", new Boolean(isLink));
		h.put("defaultBijNull", new Boolean(defaultBijNull));
		h.put("random", new Boolean(random));
		if (random)
		{
			h.put("randomteksten", randomteksten);
			h.put("randomIpLaunchdata", randomIpLaunchdata);
			h.put("aantalRandom", new Integer(aantalRandom));
			h.put("randomVar", randomVar);
		}
		if (isLink)
		{
			//h.put("linkUrl", linkUrl);
			h.put("linkUrls", linkUrls);
			//h.put("grensScores", grensScores);
			h.put("linkWidth", new Integer(linkWidth));
			h.put("linkHeight", new Integer(linkHeight));
		}
		h.put("logOption",new Boolean(logOption));
		h.put("logID",logID);
		h.put("logIDLabel",logIDLabel);
		h.put("visible", new Boolean(visible));
		h.put("ideasStatistiek", new Boolean(ideasStatistiek));
		
		for (int i = 0; i < aantalRijen && inklapbaar; i++)
		{	if(uitklapHoogtes.length>i)System.out.println("uitklapH: rij "+i +"="+uitklapHoogtes[i]);
			//System.out.println("Hoogtes: rij "+i +"="+(int)hoogtes[i]);
		}
		
		return h;
	}

	public int getIpId()
	{
		return ipId;
	}

	public String getIpExpString()
	{
		Vector v = geefInteractiePanels();
		if (v.size() > 0)
		{
			InteractiePanelContainerIF ipc = (InteractiePanelContainerIF) v.elementAt(0);
			if (ipc instanceof TekstInteractiePanelVak)
			{
				InteractiePanel ip = ((TekstInteractiePanelVak) ipc).getInteractiePanel();
				if (ip instanceof SimpelAntwoordFormuleVak)
				{
					FormuleVak fv = ((SimpelAntwoordFormuleVak) ip).geefFormuleVak();
					if (fv != null)
						return fv.toString();
				}
				if (ip instanceof SimpelAntwoordVergelijkingVak)
				{
					String string = null;
					FormuleVak fv = ((SimpelAntwoordVergelijkingVak) ip).geefFormuleVak();
					if (fv != null)
					{
						string = fv.toString();
						VergelijkingMeerv vgm = FormuleParser.parseVergelijking(string);
						Vergelijking vg = null;
						if (vgm != null)
							vg = vgm.geefVergelijking(0);
						Expressie e = null;
						if (vg != null)
							e = new Aftrekking(vg.geefExpLinks(), vg.geefExpRechts());
						if (e != null)
							return "$f" + e.toString() + "@";
					}
				}
			}
		}
		return checkExpressieString;
	}

	public void requestFocus()
	{
		Vector v = geefInteractiePanels();
		if (v.size() > 0)
		{
			InteractiePanelContainerIF ipc = (InteractiePanelContainerIF) v.elementAt(0);
			if (ipc instanceof TekstInteractiePanelVak)
			{
				InteractiePanel ip = ((TekstInteractiePanelVak) ipc).getInteractiePanel();
				if (ip instanceof SimpelAntwoordFormuleVak)
				{
					FormuleVak fv = ((SimpelAntwoordFormuleVak) ip).geefFormuleVak();
					if (fv != null)
						fv.requestFocus();
				}
			}
		}
	}

	public int getScore()
	{
		//if(selectable)return score;
		score = 0;
		Vector v = geefInteractiePanels();
		for (int i = 0; i < v.size(); i++)
		{
			InteractiePanelContainerIF ipc = (InteractiePanelContainerIF) v.elementAt(i);
			score += ipc.getScore();
		}
		if (aftrekPopup && popupUsed)
			score = score - puntenAftrekPopup;
		return score;
	}

	public int[][] getScoreObjectives()
	{
		if (WiskOpdr.objectives == null)
			return null;
		int[][] scoreObjectives = new int[WiskOpdr.objectives.length][];
		for (int i = 0; i < WiskOpdr.objectives.length; i++)
			scoreObjectives[i] = new int[WiskOpdr.objectives[i].length];
		Vector v = geefInteractiePanels();
		for (int i = 0; i < v.size(); i++)
		{
			InteractiePanelContainerIF ipc = (InteractiePanelContainerIF) v.elementAt(i);
			int[][] scoreObj = ipc.getScoreObjectives();
			for (int j = 0; scoreObj != null && j < WiskOpdr.objectives.length && j < scoreObj.length; j++)
				for (int k = 0; scoreObj[j] != null && k < WiskOpdr.objectives[j].length && k < scoreObj[j].length; k++)
				{
					scoreObjectives[j][k] += scoreObj[j][k];
					if (aftrekPopup && popupUsed)
						scoreObjectives[j][k] -= puntenAftrekPopup;
				}
		}
		return scoreObjectives;
	}
	
	public int[][] getMeasuredMisconceptions()
	{	
		if (WiskOpdr.misconceptions == null)
			return null;
		int[][] logMisconceptions = new int[WiskOpdr.misconceptions.length][];
		for (int i = 0; i < WiskOpdr.misconceptions.length; i++)
			logMisconceptions[i] = new int[WiskOpdr.misconceptions[i].length];
		Vector v = geefInteractiePanels();
		for (int i = 0; i < v.size(); i++)
		{
			InteractiePanelContainerIF ipc = (InteractiePanelContainerIF) v.elementAt(i);
			int[][] logMisc = ipc.getMeasuredMisconceptions();
			for (int j = 0; logMisc != null && j < WiskOpdr.misconceptions.length && j < logMisc.length; j++)
				for (int k = 0; logMisc[j] != null && k < WiskOpdr.misconceptions[j].length && k < logMisc[j].length; k++)
				{
					logMisconceptions[j][k] += logMisc[j][k];
				}
		}
		return logMisconceptions;
	}
	
	public int[][] getPossibleMisconceptions()
	{	
		if (WiskOpdr.misconceptions == null)
			return null;
		int[][] logMisconceptions = new int[WiskOpdr.misconceptions.length][];
		for (int i = 0; i < WiskOpdr.misconceptions.length; i++)
			logMisconceptions[i] = new int[WiskOpdr.misconceptions[i].length];
		Vector v = geefInteractiePanels();
		for (int i = 0; i < v.size(); i++)
		{
			InteractiePanelContainerIF ipc = (InteractiePanelContainerIF) v.elementAt(i);
			int[][] logMisc = ipc.getPossibleMisconceptions();
			for (int j = 0; logMisc != null && j < WiskOpdr.misconceptions.length && j < logMisc.length; j++)
				for (int k = 0; logMisc[j] != null && k < WiskOpdr.misconceptions[j].length && k < logMisc[j].length; k++)
				{
					logMisconceptions[j][k] += logMisc[j][k];
				}
		}
		return logMisconceptions;
	}

	public int getScoreMax()
	{
		// TODO Auto-generated method stub
		return scoreMax;
	}

	public void setState(Hashtable h)
	{
		if (h == null)
		{
// Wim: always initialize subcomponents, with 'no state';
			Vector v = geefInteractiePanels();
			for (int i = 0; i < v.size(); i++)
			{
					((InteractiePanelContainerIF) v.elementAt(i)).setState(null);
			}
			return;
		}

		Hashtable[] interactiePanelStates = null;
		boolean selected = false;
		int locationX = 0;
		int locationY = 0;
		boolean nagekeken = false;
		boolean popupUsed = false;
		boolean ingeklapt = true;
		boolean visible = true;
		String[] stappen = null;
		stapNr = 0;

		if (h.containsKey("interactiePanelStates"))
			interactiePanelStates = OpdrNavStruct.toHashtableArray(h.get("interactiePanelStates"));
		if (h.containsKey("selected"))
			selected = ((Boolean) h.get("selected")).booleanValue();
		if (h.containsKey("locationX"))
			locationX = ((Number) h.get("locationX")).intValue();
		if (h.containsKey("locationY"))
			locationY = ((Number) h.get("locationY")).intValue();
		if (h.containsKey("nagekeken"))
			nagekeken = ((Boolean) h.get("nagekeken")).booleanValue();
		if (h.containsKey("popupUsed"))
			popupUsed = ((Boolean) h.get("popupUsed")).booleanValue();
		if (h.containsKey("ingeklapt"))
			ingeklapt = ((Boolean) h.get("ingeklapt")).booleanValue();
		if (h.containsKey("visible"))
			visible = ((Boolean) h.get("visible")).booleanValue();
		if (h.containsKey("stappen"))
			stappen = (String[])h.get("stappen");
		if (h.containsKey("stapNr"))
			stapNr = ((Number) h.get("stapNr")).intValue();
		
		if(stappen!=null)
		{
			for(int i=0 ; i<stapNr ; i++)
			{	tekstVakken[i][aantalKolommen-1].insert(stappen[i]);
			}
			
			Vector v = geefInteractiePanels();
			for(int i=0 ; i<v.size() ; i++)
			{
				InteractiePanelContainerIF ipc = (InteractiePanelContainerIF) v.elementAt(i);
				if (ipc instanceof TekstInteractiePanelVak)
				{	((TekstInteractiePanelVak) ipc).setEditModeAll(false);
				}
			}
			zetOpdracht(getEditState(),null,null);
		}
		
		Vector v = geefInteractiePanels();
		for (int i = 0; i < v.size(); i++)
		{
			if (interactiePanelStates.length > i && interactiePanelStates[i] != null)
				((InteractiePanelContainerIF) v.elementAt(i)).setState(interactiePanelStates[i]);
			else
				((InteractiePanelContainerIF) v.elementAt(i)).setState(null);
				
		}
		this.selected = selected;
		this.nagekeken = nagekeken;
		this.locationX = locationX;
		this.locationY = locationY;
		this.selected = selected;
		this.popupUsed = popupUsed;
		this.stappen = stappen;
		this.stapNr = stapNr;
		this.visible = visible;
		

		if (aftrekPopup && popupUsed)
			produceAction("changed");

		if (selected)//setBorder(Color.gray,5);
		{
			if (colorSelection)
				setBorder(selectieColor, 400);
			else
				setBorder(Color.gray, 5);
		}

		if (zichtbaarNaNakijken)
			setVisible(nagekeken);
		
		if(inklapbaar)
		{	if(this.ingeklapt != ingeklapt)
			{ 	klapUitAction();
			}
		}
	}
	

	public Hashtable getState()
	{
		Hashtable[] interactiePanelStates = null;
		boolean selected = false;
		int locationX = 0;
		int locationY = 0;
		boolean nagekeken = false;
		boolean popupUsed = false;
		boolean ingeklapt = true;
		boolean visible = true;

		Vector v = geefInteractiePanels();
		interactiePanelStates = new Hashtable[v.size()];
		for (int i = 0; i < v.size(); i++)
		{
			interactiePanelStates[i] = ((InteractiePanelContainerIF) v.elementAt(i)).getState();
		}
		selected = this.selected;
		locationX = this.locationX;
		locationY = this.locationY;
		nagekeken = this.nagekeken;
		popupUsed = this.popupUsed;
		ingeklapt = this.ingeklapt;
		visible = this.visible;

		if (logOption) {

			Hashtable logMap = new Hashtable();

			logMap.put("logIDLabel", logIDLabel);
			logMap.put("logAnswer", "" + (aftrekPopup ? (popupUsed ? "Ja" : "") : "|"));

			logMap.put("logScore", new Integer(popupUsed ? -puntenAftrekPopup : 0));
			logMap.put("logMaxScore", new Integer(0));
			logMap.put("logErrorCount", new Integer(0));
			logMap.put("logAttemptsCount", new Integer(popupUsed ? 1 : 0));
			logMap.put("logAttempts", new Vector());

			WiskOpdr.setLog(logID, logMap);
		}
		
		Hashtable h = new Hashtable();
		h.put("interactiePanelStates", interactiePanelStates);
		h.put("selected", new Boolean(selected));
		h.put("locationX", new Integer(locationX));
		h.put("locationY", new Integer(locationY));
		h.put("nagekeken", new Boolean(nagekeken));
		h.put("popupUsed", new Boolean(popupUsed));
		h.put("ingeklapt", new Boolean(ingeklapt));
		h.put("visible", new Boolean(visible));
		if(stappen!=null)
		{	h.put("stappen", stappen);
			h.put("stapNr", new Integer(stapNr));
		}

		return h;
	}

	public boolean isCorrect()
	{
		//if(selectable)return correct;
		correct = true;
		Vector v = geefInteractiePanels();
		for (int i = 0; i < v.size(); i++)
		{
			InteractiePanelContainerIF ipc = (InteractiePanelContainerIF) v.elementAt(i);
			correct = correct && ipc.isCorrect();
		}
		return correct;
	}

	public boolean isFout()
	{
		// TODO Auto-generated method stub
		return fout;
	}

	public void kijkNa()
	{
		Vector v = geefInteractiePanels();
		for (int i = 0; i < v.size(); i++)
		{
			((InteractiePanelContainerIF) v.elementAt(i)).kijkNa();
		}
	}

	public void kijkNaKlaarKnop()
	{
		/*Expressie basis = new BasisExpressie(0);
		correct = (selected && Algebra.isGelijkwaardig(antwoordExpressie,basis) || !selected && !Algebra.isGelijkwaardig(antwoordExpressie,basis));
		if(correct)
		{	score = scoreMax;
			fout = false;
		}
		else 
		{	score = 0;
			fout = true;
		}*/
		//produceAction("checked");
	}

	public void kijkNa(int stapNr)
	{
		/*if(selectable && stapNr==-2)
		{
			kijkNaKlaarKnop();
			return;
		}*/
		Vector v = geefInteractiePanels();
		for (int i = 0; i < v.size(); i++)
		{
			((InteractiePanelContainerIF) v.elementAt(i)).kijkNa(stapNr);
		}

	}

	public void zetCorrect()
	{

	}

	public void opnieuw()
	{
		Vector v = geefInteractiePanels();
		for (int i = 0; i < v.size(); i++)
		{
			((InteractiePanelContainerIF) v.elementAt(i)).opnieuw();
		}

	}

	public boolean isInklapbaar()
	{
		return inklapbaar;
	}
	
	public boolean isZwevend()
	{
		return zwevend;
	}

	public boolean isWidthResizable()
	{
		return pasAanB;
	}

	public boolean isHeightResizable()
	{
		return pasAanH;
	}

	public void setPopupUsed(boolean b)
	{
		popupUsed = b;
		produceAction("changed");
	}

	public Point geefLocatie()
	{
		return new Point(locationX, locationY);
	}

	public void zetLocatie(int x, int y)
	{
		locationX = x;
		locationY = y;
		
		if(cbookEventHandler.hasListeners("double.xcoordinate"))
		{	Map<String,Object> mapx = new HashMap<String,Object>();
			mapx.put("name", "xcoordinate");
			mapx.put("value", locationX);
			cbookEventHandler.fire("double.xcoordinate",mapx);
		}
		
		if(cbookEventHandler.hasListeners("double.ycoordinate"))
		{	Map<String,Object> mapy = new HashMap<String,Object>();
			mapy.put("name", "(double)xcoordinate");
			mapy.put("value", locationY);
			cbookEventHandler.fire("double.ycoordinate",mapy);
		}
	}

	public void setStartSleep(int x, int y)
	{
		startSleepX = x;
		startSleepY = y;
	}

	public void setStartSleep()
	{
		startSleepX = locationX;
		startSleepY = locationY;
	}

	public Point getStartSleep()
	{
		return new Point(startSleepX, startSleepY);
	}

	public void zetSleepDoelPosities(Point[] doelPosities)
	{
		this.doelPosities = doelPosities;
	}

	public void zetSleepObjecten(TekstVakPanel[] sleepObjecten)
	{
		this.sleepObjecten = sleepObjecten;
	}

	public Point[] geefSleepDoelPosities()
	{
		return doelPosities;
	}

	public void zetSleepSnap(boolean sleepSnap)
	{
		this.sleepSnap = sleepSnap;
	}

	public boolean geefSleepSnap()
	{
		return sleepSnap;
	}

	public void zetSleepdoelMarge(int sleepdoelMarge)
	{
		this.sleepdoelMarge = sleepdoelMarge;
	}

	public int geefSleepdoelMarge()
	{
		return sleepdoelMarge;
	}

	public void setEditState(Hashtable h)
	{
		String styleString = null;
		
		String tekst = "";
		String[][] teksten = null;
		double[] breedtes = null;
		double[] hoogtes = null;
		boolean randZichtbaar = false;
		boolean bgColorZichtbaar = false;
		boolean anderFont = false;
		boolean zwevend = false;
		Hashtable[] interactiePanelLaunchData = null;
		int locationX = 0;
		int locationY = 0;
		boolean tableBorders = false;
		int cellMarge = 0;
		int bovenMarge = 0;
		int ronding = 0;
		Font f = getFont();
		if (f == null)
			f = WiskOpdr.tekstFont;
		Font font = new Font(f.getFontName(), f.getStyle(), f.getSize());

		Color bgColor = null;
		Color fgColor = Color.black;
		Color randColor = Color.gray;
		Color selectieColor = Color.white;
		int hoek = 0;
		boolean centerH = false;
		boolean centerV = false;
		boolean pasAanH = false;
		boolean pasAanB = false;
		boolean selectable = false;
		boolean selected = false;
		boolean colorSelection = false;
		boolean sleepbaar = false;
		boolean sleepdoel = false;
		boolean sleepHandle = false;
		String checkExpressieString = "$f@";
		int ipId = 0;
		int interlinie = 0;
		int cellSpaceColumn = 2;
		int cellSpaceRow = 2;
		int randDikte = 1;
		boolean zichtbaarNaNakijken = false;
		boolean balansVergCom = false;
		boolean aftrekPopup = false;
		int puntenAftrekPopup = 5;
		boolean callOut = false;
		boolean vulHoogte = false;
		boolean inklapbaar = false;
		boolean checkUitklapVak = false;
		int inklapKnopPos = 1;
		String knopImageString1 = "";
		String knopImageString2 = "";
		int[] uitklapHoogtes = new int[aantalRijen];
		boolean ingeklapt = true;
		int callOutMargeX0 = 15;
		int callOutMargeY0 = 15;
		int callOutMargeX1 = 5;
		int callOutMargeY1 = 5;
		int callOutPointX = 0;
		int callOutPointY = 0;
		boolean random = false;
		int aantalRandom = 1;
		String randomVar = "a";
		String[][][] randomteksten = new String[1][][];
		Hashtable[][] randomIpLaunchdata = new Hashtable[1][];
		boolean isLink = false;
		boolean defaultBijNull = false;
		String linkUrl = "";
		String[] linkUrls = null;
		int[] grensScores = null;
		int linkWidth = 400;
		int linkHeight = 400;
		boolean logOption = false;
		String logID = "";
		String logIDLabel = "";
		boolean visible = true;
		boolean ideasStatistiek = false;

		Hashtable style = null;
		if(h.containsKey("styleString")) styleString = (String)h.get("styleString");
		if(TekstVakPanel.styles!=null && styleString!=null)
		{	if(TekstVakPanel.styles.containsKey(styleString)) 
				style = (Hashtable)TekstVakPanel.styles.get(styleString);
		}	
		if(style!=null)	
		{	if(style.containsKey("randZichtbaar")) randZichtbaar = ((Boolean)style.get("randZichtbaar")).booleanValue();
			if(style.containsKey("bgColorZichtbaar")) bgColorZichtbaar = ((Boolean)style.get("bgColorZichtbaar")).booleanValue();
				if(bgColorZichtbaar) bgColor = new Color(255,255,180);
			if(style.containsKey("bgColor")) bgColor = (Color)style.get("bgColor");
			if(style.containsKey("fgColor")) fgColor = (Color)style.get("fgColor");
			if(style.containsKey("randColor")) randColor = (Color)style.get("randColor");
			if(style.containsKey("tableBorders")) tableBorders = ((Boolean)style.get("tableBorders")).booleanValue();
			if(style.containsKey("cellMarge")) cellMarge = ((Integer)style.get("cellMarge")).intValue();
			if(style.containsKey("bovenMarge")) bovenMarge = ((Integer)style.get("bovenMarge")).intValue();
		    if(style.containsKey("ronding")) ronding = ((Integer)style.get("ronding")).intValue();
		    if (style.containsKey("anderFont"))
				anderFont = ((Boolean) style.get("anderFont")).booleanValue();
				if(anderFont) font = new Font("SansSerif", Font.BOLD, 14);
			if(style.containsKey("font")) font = (Font)style.get("font");
			if(style.containsKey("hoek")) hoek = ((Integer)style.get("hoek")).intValue();
			if(style.containsKey("centerH")) centerH = ((Boolean)style.get("centerH")).booleanValue();
			if(style.containsKey("centerV")) centerV = ((Boolean)style.get("centerV")).booleanValue();
			if(style.containsKey("pasAanH")) pasAanH = ((Boolean)style.get("pasAanH")).booleanValue();
			if(style.containsKey("pasAanB")) pasAanB = ((Boolean)style.get("pasAanB")).booleanValue();
			if(style.containsKey("interlinie")) interlinie = ((Integer)style.get("interlinie")).intValue();
			if(style.containsKey("cellSpaceColumn")) cellSpaceColumn = ((Integer)style.get("cellSpaceColumn")).intValue();
			if(style.containsKey("cellSpaceRow")) cellSpaceRow = ((Integer)style.get("cellSpaceRow")).intValue();
			if(style.containsKey("randDikte")) randDikte = ((Integer)style.get("randDikte")).intValue();
		}
		else
		{
			if(h.containsKey("randZichtbaar")) randZichtbaar = ((Boolean)h.get("randZichtbaar")).booleanValue();
			if(h.containsKey("bgColorZichtbaar")) bgColorZichtbaar = ((Boolean)h.get("bgColorZichtbaar")).booleanValue();
				if(bgColorZichtbaar) bgColor = new Color(255,255,180);
			if(h.containsKey("bgColor")) bgColor = (Color)h.get("bgColor");
			if(h.containsKey("fgColor")) fgColor = (Color)h.get("fgColor");
			if(h.containsKey("randColor")) randColor = (Color)h.get("randColor");
			if(h.containsKey("tableBorders")) tableBorders = ((Boolean)h.get("tableBorders")).booleanValue();
			if(h.containsKey("cellMarge")) cellMarge = ((Integer)h.get("cellMarge")).intValue();
			if(h.containsKey("bovenMarge")) bovenMarge = ((Integer)h.get("bovenMarge")).intValue();
	        if(h.containsKey("ronding")) ronding = ((Integer)h.get("ronding")).intValue();
	        if (h.containsKey("anderFont"))
				anderFont = ((Boolean) h.get("anderFont")).booleanValue();
				if(anderFont) font = new Font("SansSerif", Font.BOLD, 14);
			if(h.containsKey("font")) font = (Font)h.get("font");
			if(h.containsKey("hoek")) hoek = ((Integer)h.get("hoek")).intValue();
			if(h.containsKey("centerH")) centerH = ((Boolean)h.get("centerH")).booleanValue();
			if(h.containsKey("centerV")) centerV = ((Boolean)h.get("centerV")).booleanValue();
			if(h.containsKey("pasAanH")) pasAanH = ((Boolean)h.get("pasAanH")).booleanValue();
			if(h.containsKey("pasAanB")) pasAanB = ((Boolean)h.get("pasAanB")).booleanValue();
			if(h.containsKey("interlinie")) interlinie = ((Integer)h.get("interlinie")).intValue();
			if(h.containsKey("cellSpaceColumn")) cellSpaceColumn = ((Integer)h.get("cellSpaceColumn")).intValue();
			if(h.containsKey("cellSpaceRow")) cellSpaceRow = ((Integer)h.get("cellSpaceRow")).intValue();
			if(h.containsKey("randDikte")) randDikte = ((Integer)h.get("randDikte")).intValue();
		}
		
		if (h.containsKey("tekst"))
			tekst = (String) h.get("tekst");
		if (h.containsKey("teksten"))
			teksten = (String[][]) h.get("teksten");
		if (h.containsKey("breedtes"))
			breedtes = (double[]) h.get("breedtes");
		if (h.containsKey("hoogtes"))
			hoogtes = (double[]) h.get("hoogtes");
		if (h.containsKey("zwevend"))
			zwevend = ((Boolean) h.get("zwevend")).booleanValue();
		if (h.containsKey("locationX"))
			locationX = ((Integer) h.get("locationX")).intValue();
		if (h.containsKey("locationY"))
			locationY = ((Integer) h.get("locationY")).intValue();
		if (h.containsKey("interactiePanelLaunchData"))
			interactiePanelLaunchData = (Hashtable[]) h.get("interactiePanelLaunchData");
		if (h.containsKey("selectable"))
			selectable = ((Boolean) h.get("selectable")).booleanValue();
		if (h.containsKey("selected"))
			selected = ((Boolean) h.get("selected")).booleanValue();
		if (h.containsKey("colorSelection"))
			colorSelection = ((Boolean) h.get("colorSelection")).booleanValue();
		if (h.containsKey("selectieColor"))
			selectieColor = (Color)h.get("selectieColor");
		if (h.containsKey("sleepbaar"))
			sleepbaar = ((Boolean) h.get("sleepbaar")).booleanValue();
		if (h.containsKey("sleepdoel"))
			sleepdoel = ((Boolean) h.get("sleepdoel")).booleanValue();
		if (h.containsKey("sleepHandle"))
			sleepHandle = ((Boolean) h.get("sleepHandle")).booleanValue();
		if (h.containsKey("checkExpressieString"))
			checkExpressieString = (String) h.get("checkExpressieString");
		if (h.containsKey("ipId"))
			ipId = ((Integer) h.get("ipId")).intValue();
		if (h.containsKey("zichtbaarNaNakijken"))
			zichtbaarNaNakijken = ((Boolean) h.get("zichtbaarNaNakijken")).booleanValue();
		if (h.containsKey("balansVergCom"))
			balansVergCom = ((Boolean) h.get("balansVergCom")).booleanValue();
		if (h.containsKey("aftrekPopup"))
			aftrekPopup = ((Boolean) h.get("aftrekPopup")).booleanValue();
		if (h.containsKey("puntenAftrekPopup"))
			puntenAftrekPopup = ((Integer) h.get("puntenAftrekPopup")).intValue();
		if (h.containsKey("vulHoogte"))
			vulHoogte = ((Boolean) h.get("vulHoogte")).booleanValue();
		if (h.containsKey("inklapbaar"))
			inklapbaar = ((Boolean) h.get("inklapbaar")).booleanValue();
		if (h.containsKey("checkUitklapVak"))
			checkUitklapVak = ((Boolean) h.get("checkUitklapVak")).booleanValue();
		if(h.containsKey("inklapKnopPos")) 
			inklapKnopPos = ((Integer)h.get("inklapKnopPos")).intValue();
		if(h.containsKey("knopImageString1"))
			knopImageString1 = (String)h.get("knopImageString1");
		if(h.containsKey("knopImageString2")) 
			knopImageString2 = (String)h.get("knopImageString2");
		if (h.containsKey("ingeklapt"))
			ingeklapt = ((Boolean) h.get("ingeklapt")).booleanValue();
		if(h.containsKey("uitklapHoogtes")) 
			uitklapHoogtes = (int[])h.get("uitklapHoogtes");
		if (h.containsKey("callOut"))
			callOut = ((Boolean) h.get("callOut")).booleanValue();
		if (h.containsKey("callOutMargeX0"))
			callOutMargeX0 = ((Integer) h.get("callOutMargeX0")).intValue();
		if (h.containsKey("callOutMargeY0"))
			callOutMargeY0 = ((Integer) h.get("callOutMargeY0")).intValue();
		if (h.containsKey("callOutMargeX1"))
			callOutMargeX1 = ((Integer) h.get("callOutMargeX1")).intValue();
		if (h.containsKey("callOutMargeY1"))
			callOutMargeY1 = ((Integer) h.get("callOutMargeY1")).intValue();
		if (h.containsKey("callOutPointX"))
			callOutPointX = ((Integer) h.get("callOutPointX")).intValue();
		if (h.containsKey("callOutPointY"))
			callOutPointY = ((Integer) h.get("callOutPointY")).intValue();
		if (h.containsKey("random"))
			random = ((Boolean) h.get("random")).booleanValue();
		if (h.containsKey("aantalRandom"))
			aantalRandom = ((Integer) h.get("aantalRandom")).intValue();
		if (h.containsKey("randomteksten"))
			randomteksten = (String[][][]) h.get("randomteksten");
		if (h.containsKey("randomIpLaunchdata"))
			randomIpLaunchdata = (Hashtable[][]) h.get("randomIpLaunchdata");
		if (h.containsKey("randomVar"))
			randomVar = (String) h.get("randomVar");
		if (h.containsKey("isLink"))
			isLink = ((Boolean) h.get("isLink")).booleanValue();
		if (h.containsKey("defaultBijNull"))
			defaultBijNull = ((Boolean) h.get("defaultBijNull")).booleanValue();
		if (h.containsKey("linkUrl"))
			linkUrl = (String) h.get("linkUrl");
		if (h.containsKey("linkUrls"))
			linkUrls = (String[]) h.get("linkUrls");
		if (h.containsKey("grensScores"))
			grensScores = (int[]) h.get("grensScores");
		if (h.containsKey("linkWidth"))
			linkWidth = ((Integer) h.get("linkWidth")).intValue();
		if (h.containsKey("linkHeight"))
			linkHeight = ((Integer) h.get("linkHeight")).intValue();
		if(h.containsKey("logOption")) 
			logOption = ((Boolean)h.get("logOption")).booleanValue();
        if(h.containsKey("logID")) 
        	logID = (String)h.get("logID");
        if(h.containsKey("logIDLabel")) 
        	logIDLabel = (String)h.get("logIDLabel");
        if (h.containsKey("visible"))
        	visible = ((Boolean) h.get("visible")).booleanValue();
        if (h.containsKey("ideasStatistiek"))
        	ideasStatistiek = ((Boolean) h.get("ideasStatistiek")).booleanValue();
        
       	this.styleString = styleString;
        this.randZichtbaar = randZichtbaar;
        this.selectieColor = selectieColor;
		this.bgColorZichtbaar = bgColorZichtbaar;
		this.anderFont = anderFont;
		if (!this.zwevend && zwevend && locationX == 0 && locationY == 0)
		{ //locationX=10;
			//locationY=10;
		}

		
		this.locationX = locationX;
		this.locationY = locationY;

		this.tableBorders = tableBorders;
		this.cellMarge = cellMarge;
		this.bovenMarge = bovenMarge;
		this.ronding = ronding;
		this.hoek = hoek;
		this.centerH = centerH;
		this.centerV = centerV;
		this.pasAanH = pasAanH;
		this.pasAanB = pasAanB;
		this.selectable = selectable;
		this.selected = selected;
		this.colorSelection = colorSelection;
		this.sleepbaar = sleepbaar;
		this.sleepdoel = sleepdoel;
		this.sleepHandle = sleepHandle;
		this.checkExpressieString = checkExpressieString;
		this.ipId = ipId;
		this.interlinie = interlinie;
		this.cellSpaceColumn = cellSpaceColumn;
		this.cellSpaceRow = cellSpaceRow;
		//setFont(font);
		//setBackground(bgColor);

		this.fgColor = fgColor;
		this.randColor = randColor;
		this.randDikte = randDikte;
		this.zichtbaarNaNakijken = zichtbaarNaNakijken;
		this.balansVergCom = balansVergCom;
		this.aftrekPopup = aftrekPopup;
		this.puntenAftrekPopup = puntenAftrekPopup;
		this.vulHoogte = vulHoogte;
		this.inklapbaar = inklapbaar;
		this.checkUitklapVak = checkUitklapVak;
		this.inklapKnopPos = inklapKnopPos;
		this.knopImageString1 = knopImageString1;
		this.knopImageString2 = knopImageString2;
		this.ingeklapt = ingeklapt;
		this.uitklapHoogtes = uitklapHoogtes;
		this.callOut = callOut;
		this.callOutMargeX0 = callOutMargeX0;
		this.callOutMargeX1 = callOutMargeX1;
		this.callOutMargeY0 = callOutMargeY0;
		this.callOutMargeY1 = callOutMargeY1;
		this.callOutPointX = callOutPointX;
		this.callOutPointY = callOutPointY;
		this.random = random;
		this.aantalRandom = aantalRandom;
		this.randomteksten = randomteksten;
		this.randomIpLaunchdata = randomIpLaunchdata;
		this.randomVar = randomVar;
		this.isLink = isLink;
		this.defaultBijNull = defaultBijNull;
		if (isLink) {
			if(!linkUrl.equals("")) {
				linkUrls = new String[1];
				linkUrls[0] = linkUrl;
			}
		
			//link = new Link("", linkUrl, linkWidth, linkHeight);
			link = new Link("", linkUrls, linkWidth, linkHeight, false, grensScores);
		}
		

		/**/if (teksten == null)
		{
			teksten = new String[1][1];
			teksten[0][0] = tekst;
		}

		this.aantalRijen = teksten.length;
		this.aantalKolommen = teksten[0].length;
		this.logOption = logOption;
	    this.logID = logID;
	    this.logIDLabel = logIDLabel;
	    this.visible = visible;
	    this.ideasStatistiek = ideasStatistiek;

		tekstVakken = new TekstVak[aantalRijen][aantalKolommen];
		removeAll();
		
		
		for (int i = 0; i < aantalRijen; i++)
		{
			for (int j = 0; j < aantalKolommen; j++)
			{
				tekstVakken[i][j] = new TekstVak();
				tekstVakken[i][j].addActionListener(this);
				tekstVakken[i][j].addMouseListener(this);
				add(tekstVakken[i][j]);
				tekstVakken[i][j].setLocation(1, 1);
				//tekstVak.setSelectable(false);
				tekstVakken[i][j].setBackground(WiskOpdr.bgcolor);
				tekstVakken[i][j].zetMarge(cellMarge);
				tekstVakken[i][j].zetBovenMarge(bovenMarge);
				tekstVakken[i][j].setCenterH(centerH);
				tekstVakken[i][j].setCenterV(centerV);
				tekstVakken[i][j].setInterlinie(interlinie);
			}
		}
		setEditable(true);

		if (breedtes == null || hoogtes == null)
		{
			if (breedtes == null)
				breedtes = new double[aantalKolommen];
			if (hoogtes == null)
				hoogtes = new double[aantalRijen];
			this.breedtes = breedtes;
			this.hoogtes = hoogtes;
			initializeTableBounds(getSize().width, getSize().height);
		}
		else
		{
			this.breedtes = breedtes;
			this.hoogtes = hoogtes;

		}
		setTableBounds();
		
		

		//if(anderFont) setFont(new Font("SansSerif", Font.BOLD, WiskOpdr.tekstFont.getSize()*7/6));

		for (int i = 0; i < aantalRijen; i++)
		{
			for (int j = 0; j < aantalKolommen; j++)
			{
				tekstVakken[i][j].zetTekst(teksten[i][j]);

			}
		}

		//tekstVak.zetTekst(tekst);

		//tekstVak.setEditable(false);

		setCurvature(ronding);

		//setOpaque(true);

		if (randZichtbaar)
			setBorder(randColor, randDikte);
		else
			setBorder(randColor, 0);
		if (bgColorZichtbaar)
		{
			zetTransparant(false);
			setBackground(bgColor);
		}
		else
			zetTransparant(true);//setBackground(WiskOpdr.bgcolor);

		setForeground(fgColor);

		
		//if(anderFont) setFont(font);
		Font ff = new Font(font.getName(), font.getStyle(), font.getSize());
		Font geerftFont;
		Color fgColorOvererving = fgColor;
		
		if (anderFont)
		{	setFont(ff);
			setForeground(fgColor);
			//layoutTekst();
		}
		else if(fontOvererving && getParent() instanceof TekstInteractiePanelVak)
		{	geerftFont = ((TekstInteractiePanelVak)getParent()).getTekstVak().getFont();
			fgColorOvererving = getParent() instanceof TekstInteractiePanelVak ? ((TekstInteractiePanelVak)getParent()).getTekstVak().getForeground() : fgColor;
			setFont(geerftFont);
			setForeground(fgColorOvererving);
			//layoutTekst();
		}
		

		Vector v = geefInteractiePanels();
		for (int i = 0; i < v.size(); i++)
		{
			((InteractiePanelContainerIF) v.elementAt(i)).setEditState(interactiePanelLaunchData[i]);
			((InteractiePanelContainerIF) v.elementAt(i)).addActionListener(this);
		}

		layoutTekst();
		
		if(this.zwevend != zwevend)
		{	this.zwevend = zwevend;
			try
			{
				((TekstInteractiePanelVak) getParent()).getTekstVak().layoutTekst();
			}
			catch (Exception e)
			{
			}
		}
		
		
		//if(inklapbaar)
		//{	initieerKlapUitButton(ingeklapt);
		//}
		
		if (selected)//setBorder(Color.gray,5);
		{
			if (colorSelection)
				setBorder(selectieColor, 400);
			else
				setBorder(Color.gray, 5);
		}
		
		if(inklapbaar)
		{	initieerKlapUitButton(ingeklapt);
			zetMaat();
		}
		
		if(getParent() instanceof TekstInteractiePanelVak)
		{	Map subscr = ((TekstInteractiePanelVak) getParent()).getSubscriptions();
			if(subscr!=null && subscr.containsKey("text.content"))
			{	stappen = new String[aantalRijen];
				zetMaat();
			}
		}
		
		//Font ff = new Font(font.getName(), font.getStyle(), font.getSize());
		if (anderFont)
		{	//setFont(ff);
			setForeground(fgColor);
			layoutTekst();
		}
		else if(fontOvererving && getParent() instanceof TekstInteractiePanelVak)
		{	//geerftFont = ((TekstInteractiePanelVak)getParent()).getTekstVak().getFont();
			//fgColorOvererving = getParent() instanceof TekstInteractiePanelVak ? ((TekstInteractiePanelVak)getParent()).getTekstVak().getForeground() : fgColor;
			//setFont(geerftFont);
			setForeground(fgColorOvererving);
			layoutTekst();
		}
		
	}

	//public boolean contains(int x, int y)
	//{
	//if(callOut && tekstVakken[0][0]!=null) return new Rectangle(callOutMargeX0, callOutMargeY0, callOutMargeX1-callOutMargeX0, callOutMargeY1-callOutMargeY0).contains(x-geefLocatie().x,y-geefLocatie().y);
	//return super.contains(x,y);
	//}

	public void layoutTekst()
	{
		//System.out.println("layoutTekst() ");
		
		for (int i = 0; i < aantalRijen; i++)
		{
			for (int j = 0; j < aantalKolommen; j++)
			{
				tekstVakken[i][j].layoutTekst();
			}
		}
	}

	public void setCallOutDrag(int dragX, int dragY)
	{
		if (dragType == 0)
		{
			callOutMargeX0 = dragX;
			callOutMargeY0 = dragY;
		}
		else if (dragType == 1)
		{
			callOutMargeY0 = dragY;
		}
		else if (dragType == 2)
		{
			callOutMargeX1 = getWidth() - dragX;
			callOutMargeY0 = dragY;
		}
		else if (dragType == 3)
		{
			callOutMargeX1 = getWidth() - dragX;
		}
		else if (dragType == 4)
		{
			callOutMargeX1 = getWidth() - dragX;
			callOutMargeY1 = getHeight() - dragY;
		}
		else if (dragType == 5)
		{
			callOutMargeY1 = getHeight() - dragY;
		}
		else if (dragType == 6)
		{
			callOutMargeX0 = dragX;
			callOutMargeY1 = getHeight() - dragY;
		}
		else if (dragType == 7)
		{
			callOutMargeX0 = dragX;
		}
		else if (dragType == 8)
		{
			if (dragX < dragY && dragX < getHeight() - dragY && dragX < getWidth() / 2)
			{
				callOutPointX = 0;
				callOutPointY = dragY;
			}
			else if (dragX > dragY && getWidth() - dragX > dragY && dragY < getHeight() / 2)
			{
				callOutPointX = dragX;
				callOutPointY = 0;
			}
			else if (getWidth() - dragX < dragY && getWidth() - dragX < getHeight() - dragY && dragX > getWidth() / 2)
			{
				callOutPointX = getWidth();
				callOutPointY = dragY;
			}
			else if (getWidth() - dragX > getHeight() - dragY && dragX > getHeight() - dragY && dragY > getHeight() / 2)
			{
				callOutPointX = dragX;
				callOutPointY = getHeight();
			}
		}

		if (callOutMargeX0 < 5)
			callOutMargeX0 = 5;
		if (callOutMargeY0 < 5)
			callOutMargeY0 = 5;
		if (callOutMargeX1 < 5)
			callOutMargeX1 = 5;
		if (callOutMargeY1 < 5)
			callOutMargeY1 = 5;
		if (callOutMargeX0 > getWidth() - 5)
			callOutMargeX0 = getWidth() - 5;
		if (callOutMargeY0 > getHeight() - 5)
			callOutMargeY0 = getHeight() - 5;
		if (callOutMargeX1 > getWidth() - 5)
			callOutMargeX1 = getWidth() - 5;
		if (callOutMargeY1 > getHeight() - 5)
			callOutMargeY1 = getHeight() - 5;

		if (callOutPointX < 0)
			callOutPointX = 0;
		if (callOutPointY < 0)
			callOutPointY = 0;
		if (callOutPointX > getWidth())
			callOutPointX = getWidth();
		if (callOutPointY > getHeight())
			callOutPointY = getHeight();

		setCallOutBox();
	}

	public void setCallOutBox()
	{
		if (callOut && tekstVakken.length == 1 && tekstVakken[0].length == 1)
		{
			tekstVakken[0][0].setLocation(callOutMargeX0, callOutMargeY0);
			tekstVakken[0][0].setSize(getWidth() - callOutMargeX0 - callOutMargeX1, getHeight() - callOutMargeY0 - callOutMargeY1);
			super.setCallOutBox(callOutMargeX0, callOutMargeY0, callOutMargeX1, callOutMargeY1);

		}
	}

	public void setCallOutBox(int callOutMargeX0, int callOutMargeY0, int callOutMargeX1, int callOutMargeY1)
	{

		this.callOutMargeX0 = callOutMargeX0;
		this.callOutMargeY0 = callOutMargeY0;
		this.callOutMargeX1 = callOutMargeX1;
		this.callOutMargeY1 = callOutMargeY1;
		setCallOutBox();

		//callOutPosY = y;

	}

	public void setCallOut(boolean b)
	{
		if (b && tekstVakken.length == 1 && tekstVakken[0].length == 1)
			callOut = true;
		else
			callOut = false;
	}

	public int getCallOutDragType(int x, int y)
	{
		Point[] stippen = getCallOutDots();
		for (int i = 0; i < 9; i++)
		{
			if (new Rectangle(stippen[i].x - 3, stippen[i].y - 3, 5, 5).contains(x, y))
			{
				return i;
			}
		}
		return -1;
	}

	public Point[] getCallOutDots()
	{
		int x = tekstVakken[0][0].getX();
		int y = tekstVakken[0][0].getY();
		int w = tekstVakken[0][0].getWidth();
		int h = tekstVakken[0][0].getHeight();
		Point[] stippen = new Point[9];
		stippen[0] = new Point(x - 2, y - 2);
		stippen[1] = new Point(x + w / 2, y - 2);
		stippen[2] = new Point(x + w + 3, y - 2);
		stippen[3] = new Point(x + w + 3, y + h / 2);
		stippen[4] = new Point(x + w + 3, y + h + 3);
		stippen[5] = new Point(x + w / 2, y + h + 3);
		stippen[6] = new Point(x - 2, y + h + 3);
		stippen[7] = new Point(x - 2, y + h / 2);

		int xp = callOutPointX;
		int yp = callOutPointY;
		if (callOutPointX == 0)
			xp += 3;
		if (callOutPointX == getWidth())
			xp -= 3;
		if (callOutPointY == 0)
			yp += 3;
		if (callOutPointY == getHeight())
			yp -= 3;
		stippen[8] = new Point(xp, yp);

		return stippen;

	}

	public void paintComponent(Graphics gr)
	{
		//if(!visible) 
		//	return;
		Graphics2D g = (Graphics2D) gr;

		//if(hoek!=0)
		{
			((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			AffineTransform rotation = AffineTransform.getRotateInstance(hoek * Math.PI / 180, getSize().width / 2, getSize().height / 2);
			g.transform(rotation);
		}

		if (random)
		{
			int x = getWidth();
			for (int i = 0; i < 4; i++)
			{
				g.drawRect(x - 6 - 5 * i, 0, 3, 3);
			}
		}
		if (callOut && tekstVakken.length == 1 && tekstVakken[0].length == 1)
		{
			g.setColor(randColor);
			int w = tekstVakken[0][0].getWidth();
			int h = tekstVakken[0][0].getHeight();
			int xm = tekstVakken[0][0].getLocation().x + w / 2;
			int ym = tekstVakken[0][0].getLocation().y + h / 2;

			double x = xm - callOutPointX;
			double y = ym - callOutPointY;
			if (x == 0)
				x += 0.000001;

			double rx = 1.0 / Math.sqrt(1.0 + y * y / (x * x));
			double ry = 1.0 * y / x / Math.sqrt(1.0 + y * y / (x * x));

			Polygon p = new Polygon();
			p.addPoint(callOutPointX, callOutPointY);
			p.addPoint((int) (xm + 5 * ry), (int) (ym - 5 * rx));
			p.addPoint((int) (xm - 5 * ry), (int) (ym + 5 * rx));
			g.fillPolygon(p);
			g.drawPolygon(p);
		}

		super.paintComponent(g);
		if (editable && callOut)
		{
			Point[] stippen = getCallOutDots();
			g.setColor(Color.green);
			for (int i = 0; i < 8; i++)
			{
				g.fillOval(stippen[i].x - 3, stippen[i].y - 3, 5, 5);
			}
			int x = callOutPointX;
			int y = callOutPointY;
			if (callOutPointX == 0)
				x += 3;
			if (callOutPointX == getWidth())
				x -= 3;
			if (callOutPointY == 0)
				y += 3;
			if (callOutPointY == getHeight())
				y -= 3;
			g.setColor(Color.red);
			g.fillOval(x - 3, y - 3, 6, 6);
		}
		if (!tableBorders)
			return;

		double hoogteCum = -0.5 - cellSpaceRow / 2;
		double breedteCum = -0.5 - cellSpaceColumn / 2;
		g.setColor(randColor);
		if ("GR".equals(WiskOpdr.deployVariant)  && randColor.getRed()==128 && randColor.getGreen()==128 && randColor.getBlue()==128 )
			g.setColor(new Color(70, 116, 183));
		for (int i = 0; i < aantalRijen - 1; i++)
		{
			hoogteCum += hoogtes[i] + cellSpaceRow;
			g.drawLine(0, (int) Math.round(hoogteCum), getSize().width, (int) Math.round(hoogteCum));
		}
		for (int i = 0; i < aantalKolommen - 1; i++)
		{
			breedteCum += breedtes[i] + cellSpaceColumn;
			g.drawLine((int) Math.round(breedteCum), 0, (int) Math.round(breedteCum), getSize().height);
		}
	}

	public void zetMaat()
	{
		int[] ashoogte = new int[aantalRijen];
		for (int i = 0; i < aantalRijen; i++)
		{
			for (int j = 0; j < aantalKolommen; j++)
			{
				tekstVakken[i][j].resize();
				if(!tekstVakken[i][j].bevatVulHoogteSymbool())
					ashoogte[i] = Math.max(tekstVakken[i][j].getAsHoogte(), ashoogte[i]);
			}
		}
		if (pasAanH || pasAanB)
		{	int[] breedte = new int[aantalKolommen];
			for (int i = 0; i < aantalRijen; i++)
			{	tekstVakken[i][0].resize();

				int hoogte = Math.max(0, tekstVakken[i][0].getSize().height - tekstVakken[i][0].geefOpgevuldeHoogte());
				for (int j = 0; j < aantalKolommen; j++)
				{	tekstVakken[i][j].resize();
					breedte[j] = Math.max(breedte[j], tekstVakken[i][j].getSize().width);
					if(!tekstVakken[i][j].bevatVulHoogteSymbool())
						ashoogte[i] = Math.max(tekstVakken[i][j].getAsHoogte(), ashoogte[i]);
				}

				for (int j = 0; j < aantalKolommen; j++)
				{	if (pasAanB)
						breedtes[j] = Math.max(10, breedte[j]);
					if (pasAanH)
					{	if(!tekstVakken[i][j].bevatVulHoogteSymbool())
							hoogte = Math.max(hoogte, tekstVakken[i][j].getSize().height - tekstVakken[i][j].geefOpgevuldeHoogte() - tekstVakken[i][j].getAsHoogte() + ashoogte[i]);// - tekstVakken[i][j].geefOpgevuldeHoogte()
					}
				}
				if (pasAanH)
					hoogtes[i] = hoogte;
			}

			double hoogteCum = 0;
			double breedteCum = 0;
			for (int i = 0; i < aantalRijen; i++)
			{	
				if(stappen!=null && (i>stapNr-1 || stapNr==0))
				{	hoogteCum = (hoogteCum==0 && editable) ? 10 : hoogteCum;
					break;
				}
				if(!visible && !editable)
				{	break;
				}
				
				if(i==0 || !(inklapbaar && ingeklapt))
					hoogteCum = hoogteCum + hoogtes[i] + cellSpaceRow;
			}
			hoogteCum -= cellSpaceRow;
			if(callOut)
				hoogteCum += this.callOutMargeY0 + this.callOutMargeY1;
			for (int j = 0; j < aantalKolommen; j++)
			{
				breedteCum = breedteCum + breedtes[j] + cellSpaceColumn;
			}
			breedteCum -= cellSpaceColumn;
			if(callOut)
				breedteCum += this.callOutMargeX0 + this.callOutMargeX1;
			if (pasAanB && pasAanH)
			{	setSize((int) Math.round(breedteCum) - 1, (int) Math.round(hoogteCum));
			}
			else if (pasAanB)
			{	setSize((int) Math.round(breedteCum) - 1, getSize().height);
			}
			else if (pasAanH)
			{	setSize(getSize().width, (int) Math.round(hoogteCum));

			}
			
			for (int i = 0; i < aantalRijen; i++)
			{
				for (int j = 0; j < aantalKolommen; j++)
				{
					tekstVakken[i][j].setAsHoogte(ashoogte[i]);
					tekstVakken[i][j].layoutResize((int) Math.round(breedtes[j]), (int) Math.round(hoogtes[i]));
				}
			}
		}
		else
		{
			for (int i = 0; i < aantalRijen; i++)
			{
				for (int j = 0; j < aantalKolommen; j++)
				{
					tekstVakken[i][j].setAsHoogte(ashoogte[i]);
					tekstVakken[i][j].layoutResize((int) Math.round(breedtes[j]), (int) Math.round(hoogtes[i]));
				}
			}
		}
		//if(widthResizable && heightResizable)setSize(targetWidth, tekstVakken[0][0].getSize().height);
		//else if(heightResizable)setSize(getSize().width,tekstVakken[0][0].getSize().height);

		//if(!ingeklapt)
			updateUitklapHoogtes();
		if (getParent() != null && getParent() instanceof TekstDeelVak)
		{	((TekstDeelVak) getParent()).zetMaat();

		}
		else
			setSize(getSize().width, getSize().height);

	}
	
	public boolean vulHoogteMogelijk()
	{	return vulHoogte;
	}
	
	public int geefOpgevuldeHoogte()
	{	int opgevuldeHoogte = 0;
		return tekstVakken[0][0].geefRestHoogte();
	}
	
	public int geefUitklapHoogte(int nr)
	{	if(nr<uitklapHoogtes.length)
			return uitklapHoogtes[nr];
		else return 0;
	}
	
	public void corrigeerRestHoogte()
	{	if(vulHoogte && getParent() instanceof TekstDeelVak)
		{	//int restHoogte =((TekstDeelVak) getParent()).getTekstVak().geefRestHoogte() - ((TekstDeelVak) getParent()).getTekstVak().geefOpgevuldeHoogte();// Math.max(((TekstDeelVak) getParent()).getTekstVak().geefRestHoogte(), -geefRestHoogte());
			int restHoogte =((TekstDeelVak) getParent()).getTekstVak().geefRestHoogte();
			setSize(getSize().width, Math.max(0, getSize().height + restHoogte));
			((TekstInteractiePanelVak) getParent()).setSize(getSize().width, getSize().height);
			tekstVakken[0][0].centerContent();
		}
	}
	
	//niet gebruikt en dubbel met vulHoogteMogelijk().
//	public boolean vulRestHoogteActief()
//	{
//		return vulHoogte;
//	}

	public boolean isPopup()
	{
		if (getParent() != null && getParent().getParent() != null && getParent().getParent().getParent() != null && getParent().getParent().getParent().getParent() instanceof JDialog)
			return true;
		return false;
	}

	// methoden TabletOwner

	public void zetTabletUser(FormuleVakHouder formuleVakHouder)
	{
		if (!isPopup())
		{
			Container parent = getParent();
			for (int i = 0; parent != null && i < 40; i++)
			{
				if (parent instanceof TabletOwner)
				{
					((TabletOwner) parent).zetTabletUser(formuleVakHouder);
					break;
				}
				else
				{
					parent = parent.getParent();
				}
			}
		}
		if (tablet == null)
			return;
		tablet.zetFormuleVakHouder(formuleVakHouder);
		tabletUser = formuleVakHouder;
	}

	public void zetTablet(FormuleVakHouder formuleVakHouder, int x, int y)
	{
		if (!isPopup())
		{
			Container parent = getParent();
			for (int i = 0; parent != null && i < 40; i++)
			{
				if (parent instanceof TabletOwner)
				{
					((TabletOwner) parent).zetTablet(formuleVakHouder, x, y);
					break;
				}
				else
				{
					parent = parent.getParent();
				}
			}
			return;
		}
		if (tablet == null)
		{
			tablet = new Tablet(formuleVakHouder);
			tablet.setLocation(x, y);
		}
		tablet.zetFormuleVakHouder(formuleVakHouder);
		tabletUser = formuleVakHouder;
	}

	public void addTablet(FormuleVakHouder formuleVakHouder, int xx, int yy)
	{
		Container parent = getParent();
		int x = xx + parent.getLocation().x;
		int y = yy + parent.getLocation().y;
		int h = parent.getSize().height;
		for (int i = 0; parent != null && i < 40; i++)
		{
			if (parent instanceof TabletOwner)
			{
				((TabletOwner) parent).addTablet(formuleVakHouder, x, y);
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

	// einde methode TabletOwner

	public boolean bevatSleepObject()
	{
		boolean bevat = false;
		if (sleepObjecten != null && sleepdoel)
		{
			for (int i = 0; i < sleepObjecten.length; i++)
			{
				int dx = Math.abs(geefLocatie().x - sleepObjecten[i].geefLocatie().x);
				int dy = Math.abs(geefLocatie().y - sleepObjecten[i].geefLocatie().y);
				int marge = sleepObjecten[i].geefSleepdoelMarge();
				//if(dx*dx+dy*dy < Math.min(1, marge*marge));
				if (dx < Math.min(1, marge) && dy < Math.min(1, marge))
				{
					bevat = true;
					break;
				}
			}
		}
		return bevat;
	}

	public Expressie geefSleepObjectWaarde()
	{
		Expressie waarde = null;
		if (sleepObjecten != null && sleepdoel)
		{
			for (int i = 0; i < sleepObjecten.length; i++)
			{
				int dx = Math.abs(geefLocatie().x - sleepObjecten[i].geefLocatie().x);
				int dy = Math.abs(geefLocatie().y - sleepObjecten[i].geefLocatie().y);
				int marge = sleepObjecten[i].geefSleepdoelMarge();
				//if(dx*dx+dy*dy < Math.min(1, marge*marge))
				if (dx < Math.min(1, marge) && dy < Math.min(1, marge))
				{
					waarde = FormuleParser.geefExpressie(sleepObjecten[i].getIpExpString());
					break;
				}
			}
		}
		return waarde;
	}

	public Expressie geefSleepObjectVerzamelWaarde()
	{
		Expressie waarde = new BasisExpressie(0);
		if (sleepObjecten != null && sleepdoel)
		{
			for (int i = 0; i < sleepObjecten.length; i++)
			{
				int x = sleepObjecten[i].geefLocatie().x - geefLocatie().x;
				int y = sleepObjecten[i].geefLocatie().y - geefLocatie().y;
				int b = sleepObjecten[i].getWidth();
				int h = sleepObjecten[i].getHeight();
				boolean binnen = this.contains(x, y) && this.contains(x + b, y + h);
				if (binnen)
				{
					waarde = new Optelling(waarde, FormuleParser.geefExpressie(sleepObjecten[i].getIpExpString()));
				}
			}
		}
		return waarde;
	}

	public boolean ipObjectIsCorrect()
	{
		boolean juist = false;
		Vector v = geefInteractiePanels();
		if (v.size() > 0)
		{
			InteractiePanelContainerIF ipc = (InteractiePanelContainerIF) v.elementAt(0);
			if (ipc instanceof TekstInteractiePanelVak)
			{
				InteractiePanel ip = ((TekstInteractiePanelVak) ipc).getInteractiePanel();
				if (ip instanceof SimpelAntwoordFormuleVak)
				{
					ip.kijkNa();
					juist = ((SimpelAntwoordFormuleVak) ip).isCorrectStrikt();
				}
			}
		}
		return juist;
	}

	public Expressie geefObjectWaarde()
	{
		Expressie waarde = FormuleParser.geefExpressie(getIpExpString());
		if("$f@".equals(getIpExpString()) && defaultBijNull)
			waarde = FormuleParser.geefExpressie(checkExpressieString);
		return waarde;
	}

	public boolean objectNullWaarde()
	{
		return "".equals(getIpExpString()) || "$f@".equals(getIpExpString());
	}
	
	public int getFirstRowMinHeight(TekstVak tv)
	{
		if(tv!=tekstVakken[0][0] || !pasAanH) 
			return 0;
		int minHeight = 2*bovenMarge;
		if(inklapbaar && klapUitButton!=null) 
			minHeight = minHeight + klapUitButton.getHeight();
		return minHeight;
	}
	
	public void setKlapUitButtonLocation()
	{
		if(klapUitButton==null)
			return;
		if(inklapKnopPos==0)
			klapUitButton.setLocation(1, (tekstVakken[0][0].getHeight()-klapUitButton.getHeight())/2);
		else if(inklapKnopPos==1) 
			klapUitButton.setLocation(getWidth()-klapUitButton.getWidth()-cellMarge-1, (tekstVakken[0][0].getHeight()-klapUitButton.getHeight())/2);
		else if(inklapKnopPos==2)
			klapUitButton.setLocation(tekstVakken[0][0].getContentBreedte(), (tekstVakken[0][0].getHeight()-klapUitButton.getHeight())/2);
		else 
			klapUitButton.setVisible(false);
	}
	
	private void updateUitklapHoogtes()
	{
		if(!ingeklapt)
		{
			uitklapHoogtes = new int[aantalRijen];
			for (int i = 0; i < aantalRijen; i++)
			{	uitklapHoogtes[i] = (int)hoogtes[i];
			}
		}
		else 
			uitklapHoogtes[0] = (int)hoogtes[0];
		
		produceAction("tvpUitklapResize");
	}

	public void klapUitAction()
	{ System.out.println("uitklapaction");
		if(!(getParent() instanceof TekstInteractiePanelVak))
		{
			if(ingeklapt)
			{
				ingeklapt = false;
				zetMaat();
				updateUitklapHoogtes();
				
				klapUitButton.setSelected(true);
			}
		}
		else if(ingeklapt)
		{
			ingeklapt = false;
			TekstInteractiePanelVak c = ((TekstInteractiePanelVak) getParent());
			//c.setSize(uitklapMaat.width, uitklapMaat.height);
			
			zetMaat();
			updateUitklapHoogtes();
			c.getTekstVak().layoutTekst();
			c.getTekstVak().produceAction("resize");
			//klapUitButton.setText("\u25be");
			
			klapUitButton.setSelected(true);
			produceAction("tvpKlapUit");
			if(cbookEventHandler.hasListeners("action.unfold"))
				cbookEventHandler.fire("action.unfold");
			//klapUitButton.setBackground(Color.white);
		}
		else
		{
			ingeklapt = true;
			TekstInteractiePanelVak c = ((TekstInteractiePanelVak) getParent());
			//uitklapMaat = new Dimension(c.getSize());
			//c.setSize(getWidth(),tekstVakken[0][0].getHeight());
			//c.getTekstVak().
			
			updateUitklapHoogtes();
			zetMaat();
			c.getTekstVak().layoutTekst();
			c.getTekstVak().produceAction("resize");
			//klapUitButton.setText("\u25b8");
			klapUitButton.setBackground(Color.white);
			produceAction("tvpKlapIn");
			if(cbookEventHandler.hasListeners("action.fold"))
				cbookEventHandler.fire("action.fold");
			//klapUitButton.setSelected(false);
			
			
			if(checkUitklapVak) 
			{	boolean vakinhoudCorrect = true;
				Vector v = ((TekstInteractiePanelVak)getParent()).zoekInteractiePanels();
				for (int i = 0; i < v.size(); i++)
				{	vakinhoudCorrect = vakinhoudCorrect && ((InteractiePanelContainerIF) v.elementAt(i)).isCorrect();
					
				}
				if(correct)klapUitButton.setIcon(new ImageIcon(WiskOpdr.class.getResource("resources/klapuit1goed.png")));
				else klapUitButton.setIcon(new ImageIcon(WiskOpdr.class.getResource("resources/klapuit1.png")));
			}
		}
		if(editable)
			WiskOpdr.setLaunchDataChanged();
	}
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource()==klapUitButton)
		{	
			klapUitAction();
			setPopupUsed(true);
			produceAction("resize");
		}
		if (e.getActionCommand().equals("tekst"))
		{
			produceThisAction(e);
			//System.out.println("raak"+e.getSource().toString());
			return;
		}
		if (e.getActionCommand().equals("formule"))
		{
			produceThisAction(e);
			//System.out.println("raak"+e.getSource().toString());
			return;
		}
		if (e.getActionCommand().equals("resize"))
		{
			produceAction(e.getActionCommand());
			return;
		}
		score = 0;
		scoreMax = 0;
		correct = true;
		fout = false;
		Vector v = geefInteractiePanels();
		for (int i = 0; i < v.size(); i++)
		{
			InteractiePanelContainerIF ipc = (InteractiePanelContainerIF) v.elementAt(i);
			score += ipc.getScore();
			scoreMax += ipc.getScoreMax();
			correct = correct && ipc.isCorrect();
			if (e.getSource() == ipc)
				fout = ipc.isFout();
		}

		produceAction(e.getActionCommand());
	}

	public void zetNagekeken(boolean b)
	{
		nagekeken = b;
		if (zichtbaarNaNakijken)
			setVisible(nagekeken);

		Vector v = geefInteractiePanels();
		for (int i = 0; i < v.size(); i++)
		{
			((InteractiePanelContainerIF) v.elementAt(i)).zetNagekeken(b);
		}

	}

	public void startDrag()
	{
		produceAction("pick");
	}

	public void mouseMoved(MouseEvent e)
	{
		if ((selectable || isLink) && !editable)
		{
			setCursor(new Cursor(Cursor.HAND_CURSOR));
			return;
		}
		if (!editable)
			return;
		for (int i = 0; i < aantalKolommen; i++)
		{
			if (dragColomsRects[i].contains(e.getX(), e.getY()))
			{
				if (!pasAanB)
					setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
				return;
			}
		}
		for (int i = 0; i < aantalRijen; i++)
		{
			if (dragRowsRects[i].contains(e.getX(), e.getY()))
			{
				if (!pasAanH)
					setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
				return;
			}
		}
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	public void mouseDragged(MouseEvent e)
	{
		int dx = e.getX() - dragStart;
		int dy = e.getY() - dragStart;
		if (!pasAanB && dragModeColoms && breedtes[dragNumber] + dx > 0 && breedtes[dragNumber + 1] - dx > 0)
		{
			if (e.isControlDown())
				dragTableBoundsControl(dx);
			else if (e.isShiftDown())
				dragTableBoundsShift(dx);
			else
			{
				breedtes[dragNumber] += dx;
				breedtes[dragNumber + 1] -= dx;
				setTableBounds();
			}
			dragStart = e.getX();
			layoutTekst();
			repaint();
		}
		if (!pasAanH && dragModeRows && hoogtes[dragNumber] + dy > 0 && hoogtes[dragNumber + 1] - dy > 0)
		{
			if (e.isControlDown())
				dragTableBoundsControl(dy);
			else if (e.isShiftDown())
				dragTableBoundsShift(dy);
			else
			{
				hoogtes[dragNumber] += dy;
				hoogtes[dragNumber + 1] -= dy;
				setTableBounds();
			}
			dragStart = e.getY();
			layoutTekst();
			repaint();
		}
		if (callOut && callOutDrag)
		{
			int dxCallOut = e.getX() - dragStartX;
			int dyCallOut = e.getY() - dragStartY;
			dragX += dxCallOut;
			dragY += dyCallOut;
			this.setCallOutDrag(dragX, dragY);
			dragStartX = e.getX();
			dragStartY = e.getY();
			repaint();
		}
	}

	public void mousePressed(MouseEvent e)
	{
		if (isLink && !editable)
		{
			link.activate(0); // hier moet de score van de hele activiteit worden opgevraagd..
		}
		if (callOut && editable)
		{
			dragType = getCallOutDragType(e.getX(), e.getY());
			if (dragType > -1)
			{
				callOutDrag = true;
				dragX = e.getX();
				dragY = e.getY();
				dragStartX = e.getX();
				dragStartY = e.getY();
			}
		}
		if (!editable && selectable)
		{
			selected = !selected;
			if (selected)
			{
				if (colorSelection)
					setBorder(selectieColor, 400);
				else
					setBorder(Color.gray, 5);
				produceAction("select");
				if(cbookEventHandler.hasListeners("action.select"))
					cbookEventHandler.fire("action.select");
			}
			else
			{ //setBorder(Color.gray,randZichtbaar?1:0);
				setBorder(randColor, randZichtbaar ? randDikte : 0);
				produceAction("deselect");
				if(cbookEventHandler.hasListeners("action.deselect"))
					cbookEventHandler.fire("action.deselect");
			}
			repaint();

			return;
		}
		if (!editable)
			return;
		for (int i = 0; i < aantalKolommen; i++)
		{
			if (dragColomsRects[i].contains(e.getX(), e.getY()))
			{
				dragModeColoms = true;
				dragNumber = i;
				dragStart = e.getX();
				return;
			}
		}
		for (int i = 0; i < aantalRijen; i++)
		{
			if (dragRowsRects[i].contains(e.getX(), e.getY()))
			{
				dragModeRows = true;
				dragNumber = i;
				dragStart = e.getY();
				return;
			}
		}
		dragModeColoms = false;
		dragModeRows = false;
	}

	public void mouseClicked(MouseEvent e)
	{
		;
	}

	public void mouseReleased(MouseEvent e)
	{
		if (dragModeColoms || dragModeRows)
			WiskOpdr.setLaunchDataChanged();
		dragModeColoms = false;
		dragModeRows = false;
		callOutDrag = false;

	}

	public void mouseEntered(MouseEvent e)
	{
		if ((selectable || isLink) && !editable)
		{
			setCursor(new Cursor(Cursor.HAND_CURSOR));
			return;
		}
	}

	public void mouseExited(MouseEvent e)
	{
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	//ActionProducer
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

	public void produceThisAction(ActionEvent e)
	{
		if (actionListener != null)
		{
			actionListener.actionPerformed(e);
		}
	}

	//end ActionProducer

	class DashedBorder implements Border
	{
		int THICKNESS = 1;
		Color color;
		int dashWidth;
		int dashHeight;

		public DashedBorder()
		{
			this(Color.black, 2, 2);
		}

		public DashedBorder(Color c, int width, int height)
		{
			if (width < 1)
			{
				throw new IllegalArgumentException("Invalid width: " + width);
			}
			if (height < 1)
			{
				throw new IllegalArgumentException("Invalid height: " + height);
			}
			color = c;
			dashWidth = width;
			dashHeight = height;
		}

		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height)
		{
			Insets insets = getBorderInsets(c);
			g.setColor(color);
			int numWide = (int) Math.round(width / dashWidth);
			int numHigh = (int) Math.round(height / dashHeight);
			int startPoint;
			for (int i = 0; i <= numWide; i += 2)
			{
				startPoint = x + dashWidth * i;
				g.fillRect(startPoint, y, dashWidth, THICKNESS);
				g.fillRect(startPoint, y + height - insets.bottom, dashWidth, THICKNESS);
			}
			for (int i = 0; i <= numHigh; i += 2)
			{
				startPoint = x + dashHeight * i;
				g.fillRect(x, startPoint, THICKNESS, dashHeight);
				g.fillRect(x + width - insets.right, startPoint, THICKNESS, dashHeight);
			}
		}

		public Insets getBorderInsets(Component c)
		{
			return new Insets(THICKNESS, THICKNESS, THICKNESS, THICKNESS);
		}

		public boolean isBorderOpaque()
		{
			return false;
		}
	}

	@Override
	public void acceptCBookEvent(CBookEvent event) {
		String command = event.getCommand();
		if(command.startsWith("action.setVisible"))
		{	visible = true;
			setVisible(visible);
			zetMaat();
		}
		else if(command.startsWith("action.setNotVisible"))
		{	visible = false;
			setVisible(visible);
			zetMaat();
		}
		else if(command.startsWith("action.select"))
		{	setIpSelected(true);
		}
		else if(command.startsWith("action.deselect"))
		{	setIpSelected(false);
		}
		else if(command.startsWith("action.fold") && !ingeklapt)
		{	klapUitAction();
			setPopupUsed(true);
			produceAction("resize");
			klapUitButton.setSelected(false);
		}
		else if(command.startsWith("action.unfold") && ingeklapt)
		{	klapUitAction();
			setPopupUsed(true);
			produceAction("resize");
			//klapUitButton.setSelected(false);
		}
		
		
		else if(command.startsWith("int.index"))
		{	int index = 0;
			index = (Integer)event.getParameter(command);
			if (random && index < aantalRandom);
			{	String[][] teksten = null;
				Hashtable[] interactiePanelLaunchData = null;
				int tabNummer = index - 1;
				if (tabNummer < aantalRandom && tabNummer > -1)
				{
					teksten = randomteksten[tabNummer];
					interactiePanelLaunchData = randomIpLaunchdata[tabNummer];
				}
				for (int i = 0; i < aantalRijen; i++)
				{
					for (int j = 0; j < aantalKolommen; j++)
					{
						tekstVakken[i][j].zetTekst(teksten[i][j]);
						tekstVakken[i][j].setEditable(false);
					}
				}
				Vector v = geefInteractiePanels();
				for (int i = 0; i < v.size(); i++)
				{
					((InteractiePanelContainerIF) v.elementAt(i)).zetOpdracht(interactiePanelLaunchData[i], randomVars, randomValues);
					((InteractiePanelContainerIF) v.elementAt(i)).addActionListener(this);
				}
			}
		}
		else if(command.startsWith("text.content"))
		{ 
			isStappenVak = true;
			if(stappen ==null)
				stappen = new String[aantalRijen];
			Map map = (Map)event.getParameters();
			if(map!=null)
			{	String contentString = ((String)map.get("content"));
				if(contentString.startsWith("H4sIAAAAAAAAA"))
				{	contentString = "$V"+contentString+"@";
				}
				else if(contentString.startsWith("VH4sIAAAAAAAAA"))
				{	contentString = "$"+contentString+"@";
				}
				//if(contentString.startsWith("{"))
				//{	//JSON
				//	Hashtable h = WiskOpdr.toHashtable(contentString);
				//	System.out.println("contentString: "+contentString);
				//	contentString = StringCodeObject.encodeObjectToString(h);
				//	contentString = "$V"+contentString+"@";
				//}
				else if(contentString.startsWith("back"))
				{	tekstVakken[stapNr-1][aantalKolommen-1].zetTekst("");
					stapNr--;
					zetMaat();
					return;
				}
				stappen[stapNr] = contentString;
				stapNr++;
				
				/*Vector w = geefInteractiePanels();
				Hashtable[] interactiePanelStates = new Hashtable[w.size()];
				for (int i = 0; i < w.size(); i++)
				{	interactiePanelStates[i] = ((InteractiePanelContainerIF) w.elementAt(i)).getState();
				}*/
				
				tekstVakken[stapNr-1][aantalKolommen-1].insert(contentString);
				
				Vector vStapNr = tekstVakken[stapNr-1][aantalKolommen-1].geefInteractiePanels();
				if(vStapNr.size()>0)
				{	InteractiePanelContainerIF ipcNew = (InteractiePanelContainerIF)vStapNr.elementAt(0);
					ipcNew.zetOpdracht(ipcNew.getEditState(),null,null);
					ipcNew.addActionListener(this);
				}
				
				
				Vector v = geefInteractiePanels();
				for(int i=stapNr-2 ; i>-1 && i<v.size() ; i++)
				{
					InteractiePanelContainerIF ipc = (InteractiePanelContainerIF) v.elementAt(i);
					if (ipc instanceof TekstInteractiePanelVak)
					{	((TekstInteractiePanelVak) ipc).setEditModeAll(false);
					}
				}
				
				
				//zetOpdracht(getEditState(),null,null);
				
				/*for (int i = 0; i < w.size(); i++)
				{	 ((InteractiePanelContainerIF) w.elementAt(i)).setState(interactiePanelStates[i]);
				}
				*/
				
			}
		}
	}
	
	

	@Override
	public void addCBookEventListener(CBookEventListener listener, String command) {
		cbookEventHandler.addCBookEventListener(listener, command);
		
	}

	@Override
	public void removeCBookEventListener(CBookEventListener listener,String command) {
		cbookEventHandler.removeCBookEventListener(listener, command);
		
	}

	@Override
	public String[] getSendCmds() {
		String[] sendCommands = {
				"double.xcoordinate", 
				"double.ycoordinate",
				"action.unfold",
				"action.fold",
				"action.select",
				"action.deselect"};
		return sendCommands;
	}

	@Override
	public String[] getAcceptedCmds() {
		String[] sendCommands = {"int.index",
				"action.setVisible",
				"action.setNotVisible",
				"action.unfold",
				"action.fold",
				"action.select",
				"action.deselect",
				"text.content"};
		return sendCommands;
	}

	@Override
	public String getLocalizedCmd(String cmd) {
		String locString = WiskOpdr.rb.getString(CBA_PREFIX + cmd);
		if(locString==null)
			locString = cmd;
		return locString;
	}

	public void prepareForPrint() {
		// TODO iets met zwevend
		visible = true;
		setVisible(visible);
		if(isInklapbaar() && ingeklapt)
			klapUitAction();
		zetMaat();	
	}

}
