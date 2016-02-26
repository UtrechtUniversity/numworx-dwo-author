package fi.wiskopdr.tekstobjects;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;

import org.cbook.cbookif.CBookContext;
import org.cbook.cbookif.CBookEvent;
import org.cbook.cbookif.CBookEventHandler;
import org.cbook.cbookif.CBookEventListener;
import org.cbook.cbookif.CBookWidgetEditIF;
import org.cbook.cbookif.CBookWidgetIF;
import org.cbook.cbookif.CBookWidgetInstanceIF;
import org.cbook.cbookif.rm.ResourceManager;
import org.json.simple.JSONArray;














//import fi.vangen.Vangen;
//import fi.mozarch.MozArch;
import fi.wiskopdr.cbook.CBookInteractiePanel;
import fi.wiskopdr.cbook.Service;
import fi.wiskopdr.cbook.WidgetBridge;
import fi.wiskopdr.formuleobjects.*;
import fi.wiskopdr.opdrnav.OpdrNavStruct;
import fi.wiskopdr.opdrnav.XWidgetManager;
import fi.wiskopdr.scheikundeobjects.ReactieVergelijkingVak;
import fi.wiskopdr.stelselsvergelijkingen.StelselAntwoordVak;
import fi.wiskopdr.symbolen.SymboolPanel;
import fi.wiskopdr.tekstobjects.TekstInteractiePanelVak.Connector;
//import fi.wiskopdr.tekstobjects.*;
import fi.wiskopdr.AntwoordVergelijkingVak;
import fi.beans.base64code.StringCodeObject;
import fi.beans.iconan.Iconan;
//import fi.beans.scorm.SCORM12APIInterface;
import fi.beans.wiskopdrbeans.CBookAware;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;
import fi.beans.wiskopdrbeans.ResourceManagerClient.ResourceManagerFactory;
import fi.beans.wiskopdrbeans.WiskOpdrApplet;
import fi.wiskopdr.CheckButtonPanel;
import fi.wiskopdr.CheckValueUnitPanel;
import fi.wiskopdr.DialogFacade;
import fi.wiskopdr.GrafiekPanel;
//import fi.wiskopdr.GrafiekTekenPanel;
import fi.wiskopdr.ImageComponent;
import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.AntwoordFormuleVak;
//import fi.wiskopdr.AntwoordVakEditPanel;
import fi.wiskopdr.InteractiePanelContainerIF;
import fi.wiskopdr.SimpelAntwoordFormuleVak;
import fi.wiskopdr.SimpelAntwoordVergelijkingVak;
import fi.wiskopdr.TekstVakPanel;
import fi.wiskopdr.GeogebraPanel;
import fi.wiskopdr.Geogebra3Panel;
import fi.wiskopdr.CheckUnitPanel;
import fi.wiskopdr.CheckSleepUnitPanel;
import fi.wiskopdr.AntwoordTekstVak;
import fi.wiskopdr.AntwoordKeuzeVak;
import fi.wiskopdr.GetallenlijnSprongPanel;
//import fi.nabouwenaanzichten.NabouwenAanzichten;
//import fi.algebrapijlenopdr.AlgebraPijlenOpdr;
//import fi.tekenveelvlakopdr.TekenVeelvlakOpdr;

public class TekstInteractiePanelVak extends TekstDeelVak implements ActionListener, InteractiePanelContainerIF, MouseListener, MouseMotionListener, KeyListener
{
	
	public static class Connector extends AbstractMap<String, String> implements Entry<String,String>, Serializable, Comparable<Connector> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String key;
		private String value;
		
		private TekstInteractiePanelVak source, dest;

		public String toString() {
			String tkey = source != null ? source.getLocalizedCmd(key): key;
			String tdest = dest != null ? dest.getLocalizedCmd(value): value;
			return tkey + "→" + tdest;
		}
		
		public Connector(String key, String value) {
			this.key = key;
			this.value = value;
		}

		public Connector(String s, String d,
				TekstInteractiePanelVak source,
				TekstInteractiePanelVak dest) {
			this(s,d);
			this.source = source;
			this.dest = dest;
			
		}

		@Override
		public Set<Entry<String, String>> entrySet() {
			Entry<String,String> set = this;
			return Collections.singleton(set);
		}

		public String getKey() {
			return key;
		}

		@Override
		public String getValue() {
			return value;
		}

		@Override
		public String setValue(String value) {
			return value;
		}

		@Override
		public int compareTo(Connector other) {
			return toString().compareTo(other.toString());
		}

	}

	static class EventDecorator extends CBookEvent {
		public EventDecorator(String command) {
			super(command, command);
		}
		CBookEvent event;
		/**
		 * @return
		 * @see org.cbook.cbookif.CBookEvent#getParameters()
		 */
		public Map<String, ?> getParameters() {
			return event.getParameters();
		}
		/**
		 * @param key
		 * @return
		 * @see org.cbook.cbookif.CBookEvent#getParameter(java.lang.String)
		 */
		public Object getParameter(String key) {
			return event.getParameter(key);
		}
		/**
		 * @return
		 * @see org.cbook.cbookif.CBookEvent#getMessage()
		 */
		public String getMessage() {
			return event.getMessage();
		}
		/**
		 * @return
		 * @see java.util.EventObject#getSource()
		 */
		public Object getSource() {
			return event.getSource();
		}
		
	}

	public static class CBEDecorator implements CBookEventListener {

		private EventDecorator event;
		private CBookEventListener listener;
		public CBEDecorator(InteractiePanel interactiePanel, String command) {
			event = new EventDecorator(command);
			listener = (CBookEventListener) interactiePanel;
		}

		@Override
		public void acceptCBookEvent(CBookEvent event) {
			this.event.event = event;
			listener.acceptCBookEvent(this.event);
		}

	}

	class PopupContainer extends Container implements XWidgetManager.HasWidgetManager {

		@Override
		public XWidgetManager getXWidgetManager() {
			return tekstVak.getXWidgetManager();
		}

	}

	static final int CBOOKWIDGET = -2;
	static final String CBOOKWIDGET_NAME = "soortInteractiePanelClass";
	
	
	
	private InteractiePanel interactiePanel;
	private InteractieEditPanel interactieEditPanel;
	private boolean editMode;
	private Hashtable launchData;
	private boolean selected, connected;
	private EditInteractiePanelDialog editInteractiePanelDialog;
	private JPanel afdekPanel;
	private JPanel vervangingsPanel;
	private JPanel resizePanel;
	private JPanel sleepPanel;
	private JPanel callOutPosPanel;
	private boolean popup;
	private int soortInteractiePanel;
	
	private boolean sleepModus;
	private boolean resizeModus;
	
	private boolean eersteKeer = true;
	private boolean draggingToSelect = false;
		
	private TekstTeken anchor;
	
	private int sleepX, sleepY, startX, startY;
	
	private boolean studentEditor = false;
	
	public static TekstInteractiePanelVak potentialSource, potentialDest;
	//private CBookEventHandler cbookEventHandler = new CBookEventHandler(this);
	private String crossWidgetId = null;
	@Deprecated
	private List<Map<String, String>> connections = new ArrayList<Map<String, String>>();
	private Map<String,Set<Connector>> subscriptions;
	
	public static String[][] wiskOpdrInteractiePanels = 
	{
		//{ "fi.wiskopdr.AntwoordFormuleVak" , "Formulevak met stappen" },
		//{ "fi.wiskopdr.AntwoordVergelijkingVak" , "Vergelijkingvak met stappen" },
		//{ "fi.wiskopdr.SimpelAntwoordFormuleVak" , "Simpel formulevak" },
		//{ "fi.wiskopdr.SimpelAntwoordVergelijkingVak" , "Simpel vergelijkingvak" },
		//{ "fi.wiskopdr.tekstobjectsTekstEditor" , "Tekst-antwoordvak" },
		{ "fi.algebrapijlenopdr.AlgebraPijlenOpdr" , "AlgebraPijlen" },
		{ "fi.nabouwenaanzichten.NabouwenAanzichten" , "Blokkenbouwen" },
		{ "fi.flowdiagrams.FlowDiagrams" , "Stroomdiagrammen" },
		{ "fi.balansfruit.BalansFruitApplet" , "Fruitbalans" },
		{ "fi.doorziendwo.DoorzienDWO" , "Doorzien" },
		{ "fi.vergroten.Vergroten" , "Vergroten" },
		{ "fi.vangen.Vangen" , "Vangen" },
		{ "fi.verknippen.Verknippen" , "Verknippen" },
		{ "fi.geomalgebra.GeomAlgebra" , "Geom.algebra 2d" },
		{ "fi.geomalgebra1d.GeomAlgebra1d" , "Geom.algebra 1d" },
		{ "fi.normaleverdeling.NormaleVerdeling" , "Normale verdeling" },
		{ "fi.tinyplayerapplet.TinyPlayerApplet" , "mp3-player" },
		{ "fi.binomverdeling.BinomVerdeling" , "Binominale verdeling" },
		{ "fi.tekenveelvlakopdr.TekenVeelvlakOpdr" , "Tekenveelvlak" },
		{ "fi.mozarch.MozArch" , "Mozaik" },
		{ "fi.geodefull.GeodeFull" , "Veelvlakken" },
		{ "fi.figuursnijden.Snijden" , "Eerlijk verdelen" },
		{ "fi.statistiek.Statistiek" , "Statistische representaties" },
		{ "fi.stroomdiagrammen.Stroomdiagrammen" , "Stroomdiagrammen Nieuw" },
		{ "fi.tegels.Tegels" , "Tegels" },
		{ "fi.omtrekapplet.OmtrekApplet" , "Omtrek applet" },
		{ "fi.algebraexpressies.AlgebraExpressies" , "Algebra Expressies" },
		{ "fi.oppervlaktealgebra.OppervlakteAlgebra" , "Oppervlakte-algebra" },
		{ "fi.blokkenprogramma.BlokkenProgramma" , "Blokkenprogramma" },
		{ "fi.grafiek3dtest.Grafiek3DTest" , "Grafieken 3D" },
		{ "fi.spot_problems_dwo.Spot_Problems_dwo" , "Stippelalgebra" },
		{ "fi.kladje.Kladje" , "Kladje" },
		{ "fi.kansbomen.Kansbomen" , "Kansbomen" },
		{ "fi.calculatordwo.CalculatorDwo" , "Rekenmachine" },
		{ "fi.formstruct.FormStruct" , "[test] FormStruct" },
		{ "fi.graphtool.GraphTool" , "[test] GraphTool" },
		{ "fi.draaibank.Draaibank" , "[test] Draaibank" },
		{ "fi.waarmakersdwo.WaarmakersDwo" , "[test] Waarmaker" },
		{ "fi.statsim.StatSim" , "[test] Statistiek Simulaties" },
		{ "fi.sliderwidget.SliderWidget" , "[test] Slider" },
		{ "fi.dataplot.DataPlot" , "[test] DataPlot" },
		{ "fi.javalogoweb.JavaLogoWeb" , "[test] JavaLogoWeb" }
	};
	
	
	
	//public static String[] interactiePanelClassNames =
	//{	 "fi.nabouwenaanzichten.NabouwenAanzichten" , 
	//	 "fi.flowdiagrams.FlowDiagrams" , 
	//	 "fi.wiskopdr.GrafiekPanel",
	//	 "fi.wiskopdr.GrafiekTekenPanel"
	//};
	public static String[] interactiePanelDescriptions =
	{	WiskOpdr.rb.getString("formuleAntwVakLabel") ,
		WiskOpdr.rb.getString("vergelijkingAntwVakLabel") ,
		WiskOpdr.rb.getString("simpelFormuleAntwVakLabel") ,
		WiskOpdr.rb.getString("simpelVergelijkingAntwVakLabel") ,
		WiskOpdr.rb.getString("tekstAntwVakLabel") ,
		WiskOpdr.rb.getString("algebraPijlenLabel") ,
		WiskOpdr.rb.getString("blokkenbouwenLabel") ,
		WiskOpdr.rb.getString("stroomdiagrammenLabel") ,
		WiskOpdr.rb.getString("grafiekenToolLabel") ,
		WiskOpdr.rb.getString("tekstVakLabelH"),
		WiskOpdr.rb.getString("geogebra3IpLabel"),//"Geogebra",
		WiskOpdr.rb.getString("fruitBalansIpLabel"),//"Fruitbalans",
		WiskOpdr.rb.getString("checkSelectieUnitLabel"),//"CheckUnit",
		WiskOpdr.rb.getString("checkTekstAntwoordVakLabel"),//"Check-tekstantwoordvak",
		WiskOpdr.rb.getString("keuzeAntwoordVakLabel"),//"Keuzeantwoordvak",
		WiskOpdr.rb.getString("doorzienIpLabel"),//"Doorzien-component",
		WiskOpdr.rb.getString("checkSleepUnitLabel"),//"CheckSleepUnit",
		WiskOpdr.rb.getString("vergrotenIpLabel"),//"PO Vergroten",
		WiskOpdr.rb.getString("vangenIpLabel"),//"PO Vangen",
		WiskOpdr.rb.getString("verknippenIpLabel"),//"PO Verknippen",
		WiskOpdr.rb.getString("geomAlgebra2dIpLabel"),//"Geom.algebra 2d",
		WiskOpdr.rb.getString("geomAlgebra1dIpLabel"),//"Geom.algebra 1d",
		WiskOpdr.rb.getString("normaleVerdelingIpLabel"),//"Normale verdeling",
		WiskOpdr.rb.getString("mp3PlayerIpLabel"),//"Mp3-player",
		WiskOpdr.rb.getString("binominaleVerdelingIpLabel"),//"Binominale verdeling",
		WiskOpdr.rb.getString("getallenlijnIpLabel"),//"Getallenlijn (sprong)",
		WiskOpdr.rb.getString("tekenVeelvlakIpLabel"),//"Tekenveelvlak",
		WiskOpdr.rb.getString("mozaikIpLabel"),//"Mozaik",
		WiskOpdr.rb.getString("veelvlakkenIpLabel"),//"Veelvlakken",
		WiskOpdr.rb.getString("eerlijkVerdelenIpLabel"),//"PO Eerlijk verdelen",
		WiskOpdr.rb.getString("statistiekIpLabel"),//"[test] Statistische representaties",
		WiskOpdr.rb.getString("stroomdiagrammenIpLabel"),//"[test] Stroomdiagrammen Nieuw",
		WiskOpdr.rb.getString("tegelsIpLabel"),//"Tegels",
		WiskOpdr.rb.getString("checkWaardeUnitLabel"),//"[test] CheckWaardeUnit",
		WiskOpdr.rb.getString("omtrekAppletIpLabel"),//"PO Omtrek applet",
		WiskOpdr.rb.getString("algebraExpressiesIpLabel"),//"[test] Algebra expressies",
		WiskOpdr.rb.getString("oppervlakteAlgebraIpLabel"),//"[test] Oppervlakte-algebra"
		WiskOpdr.rb.getString("blokkenProgrammaIpLabel"),//"[test] Blokkenprogramma"
		WiskOpdr.rb.getString("grafieken3DIpLabel"),//"[test] Blokkenprogramma"
		WiskOpdr.rb.getString("geogebraIpLabel"),//"Geogebra",
		WiskOpdr.rb.getString("spotProblemsIpLabel"),//"[test] SpotProblem"
		WiskOpdr.rb.getString("kladjeIpLabel"),//"[test] Kladje"
		WiskOpdr.rb.getString("kansbomenIpLabel"),//"Kansbomen"
		WiskOpdr.rb.getString("rekenmachineIpLabel"),//"Rekenmachine"
		WiskOpdr.rb.getString("formStructIpLabel"),//"[test] FormStruct"
		WiskOpdr.rb.getString("graphToolIpLabel"),//"[test] GraphTool"
		WiskOpdr.rb.getString("draaibankIpLabel"),//"[test] Draaibank"
		WiskOpdr.rb.getString("waarmakersIpLabel"),//"[test] Waarmakers"
		WiskOpdr.rb.getString("statSimIpLabel"),//"[test] StatSim"
		"CheckButton",//WiskOpdr.rb.getString("checkButtonIpLabel"),//"[test] CheckButton"
		"Slider",
		"DataPlot",
		"ReactieVergelijking",
		WiskOpdr.rb.getString("stelselVakLabel"),//[test] Stelsel-antwoordvak
		WiskOpdr.rb.getString("javaLogoIpLabel"),//[test] JavaLogo
		WiskOpdr.rb.getString("symboolIpLabel"),
	};
	
	
	public static int[][] interactiePanelSets =
	{
		{0,1,2,3,4,13,14,12,16,25,33,49,52,53},
		{5,6,7,11,15,17,18,19,20,21,22,23,24,26,27,28,29,30,31,32,34,35,36,37,38,40,41,42,43,44,45,46,47,48,50,51,54},
		{45},
		{9, 55}, 
		{10,39},
		{-2},
		{-2},
		{-2},
		{-2},
	};
	
	public static String[] interactiePanelSetNames =
	{
		WiskOpdr.rb.getString("antwoordVakLabel") ,
		WiskOpdr.rb.getString("interactieVakLabel") ,
		WiskOpdr.rb.getString("grafiekComponentLabel") ,
		WiskOpdr.rb.getString("tekstVakLabel"),
		"geogebra",
		"c-book widget",
		"cindy widget",
		"eslate widget",
		"epsilon widget"
	};
	
	public static int AntwoordvakkenSetNr = 0;
	public static int AppletsSetNr = 1;
	public static int GrafiekenSetNr = 2;
	public static int TekstvakkenSetNr = 3;
	public static int GeogebraSetNr = 4;
	public final static int CBookSetNr = 5;
	public final static int CindySetNr = 6;
	public final static int ESlateSetNr = 7;
	public final static int EpsilonSetNr = 8;
	public int currentSetNr = 0;
	
	private DialogFacade popupFrame;
	//private FormuleButton popupButton; // deprecated, gebruik popupJButton en deze alleen bij constructie
	private JButton popupJButton;
	private Image popupImage;
	private String popupImageString;
	private Iconan iconman;
		
	
	public TekstInteractiePanelVak(TekstVak tv, int setNr)
	{	this(tv);
		currentSetNr = setNr;
		//tv.getXWidgetManager().newCrossWidgetId(this);
// TODO omzetten naar DialogFacade				
		Component window = WiskOpdr.getWindowForComponent(tv);
		if(window instanceof Frame)editInteractiePanelDialog = new EditInteractiePanelDialog((Frame)window, "", false, currentSetNr, launchData, tv.getXWidgetManager());
		if(window instanceof Dialog)editInteractiePanelDialog = new EditInteractiePanelDialog((Dialog)window, "", false, currentSetNr, launchData, tv.getXWidgetManager());
		
		editInteractiePanelDialog.setBackground(WiskOpdr.bgcolor);
		editInteractiePanelDialog.addActionListener(this);
		editInteractiePanelDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		showDialog(false);
	}
	
	public String getLocalizedCmd(String key) {
		if(interactiePanel instanceof CBookAware)
		{
			try {
				key = ((CBookAware) interactiePanel).getLocalizedCmd(key);
			} catch (Throwable e) {
				System.err.println(e);
			}
		}
		return key;
	}

	public TekstInteractiePanelVak(TekstVak tv, InteractiePanel ip)
	{	this(tv);
		interactiePanel = ip;	
	
	}
	
	public TekstInteractiePanelVak(TekstVak tv, Hashtable launchData)
	{	this(tv);
		this.launchData = launchData;
		setEditState(launchData);
	}
	
	public TekstInteractiePanelVak(TekstVak tv)
	{	super(tv);
		setLayout(null);
		//setBackground(Color.white);
		ashoogte = 10;
		
		addMouseListener(this);
		addMouseMotionListener(this);
		
		
		//launchData = EditInteractiePanelDialog.editInteractiePanel(getEditPanel());
		//setEditState(launchData);
		setSize(30,30);
		setOpaque(false);
		
	}
	
	/**
	 * @return null or crosswidgetID
	 */
	public String getCrossWidgetId0()
	{
		return crossWidgetId;
	}
	
	public String getCrossWidgetId()
	{	
		if(crossWidgetId == null)
		{
			XWidgetManager manager = tekstVak.getXWidgetManager();
			if(manager != null) manager.newCrossWidgetId(this);
		}
		return crossWidgetId;
	}

	public String getCrossWidgetId(XWidgetManager manager)
	{	
		if(crossWidgetId == null && manager != null)
		{
			manager.newCrossWidgetId(this);
		}
		return crossWidgetId;
	}

	public void setCrossWidgetId(String crossWidgetId)
	{	this.crossWidgetId = crossWidgetId;
	}
	
	@Deprecated
	public List getConnections()
	{	return connections;
	}
	public Map<String,Set<Connector>> getSubscriptions() {
		return subscriptions;
	}
	
	public void initConnections(XWidgetManager manager)
	{
		if(interactiePanel instanceof TekstVakPanel)
			((TekstVakPanel)interactiePanel).initConnections(manager);
		//else
		{	//System.out.println("initConnections");
			//System.out.println("connections: "+connections.toString());
			if( subscriptions != null ) 
			{
				for( Entry<String, Set<Connector>> entry: subscriptions.entrySet())
				{
					String command = entry.getKey();
					Iterator<Connector> iter = entry.getValue().iterator();
					while (iter.hasNext()) {
						Connector c = iter.next();
						String sender = c.getKey();
						TekstInteractiePanelVak tipv = manager.getWidgetContainer(sender);
						if (tipv != null && tipv.interactiePanel instanceof CBookAware) 
						{
							CBookAware senderPanel = (CBookAware) tipv.interactiePanel;
							String commandSender = c.get(sender);
							if(commandSender .equals( command ))
								senderPanel.addCBookEventListener((CBookEventListener) interactiePanel, command);
							else
								senderPanel.addCBookEventListener(new CBEDecorator(interactiePanel, command), commandSender);
						} else
							iter.remove();
					}
				}
			} 
			{
			
			
			
			
			Iterator<Map<String, String>> iter = connections.iterator();
			while(iter.hasNext()){ 
				Map<String, String> type = iter.next();
				Map.Entry<String,String> entry = type.entrySet().iterator().next();
				//System.out.println("zoek in tekstvak: "+entry.getValue());
				
				TekstInteractiePanelVak tipv = manager.getWidgetContainer(entry.getValue());
								
				if(tipv != null && tipv.interactiePanel instanceof CBookAware)
				{	((CBookAware)interactiePanel).addCBookEventListener((CBookAware)tipv.interactiePanel, entry.getKey());
					//System.out.println("addCBookEventListener: "+ entry.getKey());
					tipv.addSubscription(entry.getKey(), getCrossWidgetId(), entry.getKey());
				}
				else
					iter.remove();
				
			}
			}
			manager.setCrossWidgetView(this);
		}
	}
	
	public BasisTekstVak getBasisTekstVak()
	{
// definitie is nog niet helemaal duidelijk, want er zijn meerdere basisvakken en
// slechts één manager.
		return tekstVak.getXWidgetManager().getBasisVak();
		
//		while(basisTekstVak.getParent()!=null && basisTekstVak.getParent() instanceof TekstVakPanel)
//		{
//			TekstVakPanel tvp = (TekstVakPanel)basisTekstVak.getParent();
//			TekstInteractiePanelVak tipvParent = (TekstInteractiePanelVak)tvp.getParent();
//			if(tipvParent==null) 
//				break;
//			else 
//				basisTekstVak = tipvParent.tekstVak;
//		}
//		
//		return (BasisTekstVak)basisTekstVak;
	}
	
	public TekstInteractiePanelVak getWidgetContainer(String crossWidgetId)
	{
		if(crossWidgetId.equals(this.crossWidgetId))
			return this;
		TekstInteractiePanelVak tipv = null;
		if(interactiePanel instanceof TekstVakPanel)
		{	TekstVakPanel tvp = (TekstVakPanel)interactiePanel;
			tipv = tvp.getWidgetContainer(crossWidgetId);
		}
		return tipv;
	}
	
	public void connect(TekstInteractiePanelVak dest, Set<Connector> set)
	{
		if(dest == this || !(dest.interactiePanel instanceof CBookAware)) return;
		
		CBookAware listener = (CBookAware)(dest.interactiePanel);
		Object[] possibleValues = set.toArray();
		Object selectedValue = null;
		if(possibleValues.length>0)
			selectedValue = JOptionPane.showInputDialog(this, "Choose one", "Command", JOptionPane.INFORMATION_MESSAGE, null, possibleValues, possibleValues[0]);
		Connector commands = (Connector)selectedValue;
		if(commands == null) return;
		String commandOut = commands.getKey();
		String commandIn = commands.getValue();
		//Map connection = Collections.singletonMap(command, dest.getCrossWidgetId());
		if(
				//command!=null && !checkConnectionExists(connection)
			 !dest.checkSubscriptionExists(commandIn, getCrossWidgetId(), commandOut)
		)
		{	((CBookAware)interactiePanel).addCBookEventListener(new CBEDecorator(dest.interactiePanel, commandIn), commandOut);
			//connections.add(connection);
			dest.addSubscription(commandIn, getCrossWidgetId(), commandOut);
			//System.out.println("connected:"+command +" "+dest.getCrossWidgetId());
		}
		getCrossWidgetId(); // zender OOK CrossWidgetId!
		tekstVak.getXWidgetManager().setCrossWidgetView(this);
		tekstVak.getXWidgetManager().setCrossWidgetView(dest); // dest is eindvak
		getBasisTekstVak().updateCrossWidgetView();
		WiskOpdr.setLaunchDataChanged();
	}
	
	private void addSubscription(String commandIn, String xwid,
			String commandOut) {
		if(subscriptions == null) {
			subscriptions = new TreeMap<String, Set<Connector>>();
		}
		Set<Connector> map = subscriptions.get(commandIn);
		if (map == null) {
			map = new TreeSet<Connector>();
			subscriptions.put(commandIn, map);
		}
		map.add(new Connector(xwid, commandOut));
	}

	
	private boolean checkSubscriptionExists(String commandIn, String xWid, String commandOut) {
		if(subscriptions == null) return false;
		Set<Connector> set = subscriptions.get(commandIn);
		return set != null && set.contains(new Connector(xWid, commandOut));
	}
	
	@Deprecated
	private boolean checkConnectionExists(Map connection)
	{	boolean exists = false;
		Map<String, String> type0 = connection;
		Map.Entry<String,String> entry0 = type0.entrySet().iterator().next();
		String commandString0 = entry0.getKey();
		String idString0 = entry0.getValue();
		
		Iterator iter = connections.iterator();
		Map<String, String> mapToBeRemoved = null;
		while(iter.hasNext())
		{	Map<String, String> type = (Map)iter.next();
			Map.Entry<String,String> entry = type.entrySet().iterator().next();
			String commandString = entry.getKey();
			String idString = entry.getValue();
			exists = exists || commandString.equals(commandString0) && idString.equals(idString0);
		}
		return exists;
	}
	@Deprecated
	public void removeConnection(TekstInteractiePanelVak dest, String command)
	{
		dest.removeSubscription(command, getCrossWidgetId(), command);

		Iterator<Map<String, String>> iter = connections.iterator();
		final String destWidgetId = dest.getCrossWidgetId0();
		while(iter.hasNext())
		{	Map<String, String> type = iter.next();
			Map.Entry<String,String> entry = type.entrySet().iterator().next();
			String commandString = entry.getKey();
			String idString = entry.getValue();
			if(commandString.equals(command) && idString.equals(destWidgetId))
			{	iter.remove();
			}
		}
	}
	
	public void removeSubscription(String commandIn, String xWid, String commandOut){
		if(subscriptions == null) return;
		Set<?> set = subscriptions.get(commandIn);
		if(set == null) return;
		Connector c = new Connector(xWid,commandOut);
		set.remove(c);
	}
	
	
	public InteractieEditPanel getInteractieEditPanel()
	{
		if(interactieEditPanel==null)
			if(interactiePanel!=null)
				interactieEditPanel = interactiePanel.getEditPanel();
		return interactieEditPanel;
	}
	
	public String[] getSendCmds()
	{	String[] sendCmds = null;
		if(interactiePanel instanceof CBookAware)
		{
			return ((CBookAware)interactiePanel).getSendCmds();
		}
		return sendCmds;
	}
	
	public String[] getAcceptedCmds()
	{	String[] acceptedCmds = null;
		if(interactiePanel instanceof CBookAware)
		{
			return ((CBookAware) interactiePanel).getAcceptedCmds();
		}	
		return acceptedCmds;
	}
	
	public void setPopupImage(String imageName)
	{   popupImageString = imageName;
	}
	
	public void maakPopupFrame()
	{	
		String title = "";//interactiePanelDescriptions[soortInteractiePanel];
		popupFrame = DialogFacade.newInstance(this, title);
		popupFrame.setContentPane(new PopupContainer());
		popupFrame.getContentPane().setBackground(Color.white);
		boolean scroll = false;
		if(((Component)interactiePanel).getSize().height<600 || !(interactiePanel instanceof TekstVakPanel))
		{
			popupFrame.getContentPane().setLayout(null);
			if(interactiePanel!=null)popupFrame.getContentPane().add((Component)interactiePanel,0);
		}
		else
		{	scroll = true;
			popupFrame.getContentPane().setLayout(new BorderLayout()); // no default borderlayout.
// FIXME Component.setPreferredSize(Dimension) since 1.5
// JComponent.setPreferredSize is wel 1.4
			if(interactiePanel instanceof JComponent)
				((JComponent)interactiePanel).setPreferredSize(((Component)interactiePanel).getSize());
			JScrollPane jScrollPane = new JScrollPane((Component)interactiePanel);
			jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		    jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		    popupFrame.getContentPane().add(jScrollPane, BorderLayout.CENTER);
		}
	    
	    popupFrame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e)
			{   popupFrame.setVisible(false);
				popupFrame.dispose();
				popupFrame = null;
			}
		});
	    popupFrame.addComponentListener(new ComponentAdapter(){
			public void componentResized(ComponentEvent e)
			{   int x = 0;//frame.getInsets().left;
				int y = 0;//frame.getInsets().top;
				int b = popupFrame.getSize().width - popupFrame.getInsets().left - popupFrame.getInsets().right;
				int h = popupFrame.getSize().height - popupFrame.getInsets().top - popupFrame.getInsets().bottom;
				if(((Component)interactiePanel).getSize().height<600 || !(interactiePanel instanceof TekstVakPanel))interactiePanel.setBounds(x,y,b,h);
				//resize();
			}
		});
	  
	    
	}
	
	public void setAnchor(TekstTeken anchor)
	{	this.anchor = anchor;
	}
	
	public void increaseFont(int size)
	{	
		if(interactiePanel instanceof TekstVakPanel) ((TekstVakPanel)interactiePanel).increaseFont(size);
		
	}
	
	public void setBackground(Color color)
	{	super.setBackground(color);
		if(interactiePanel!=null && !(interactiePanel instanceof TekstVakPanel)) ((Component)interactiePanel).setBackground(color);
		if(interactiePanel instanceof TekstVakPanel && ((TekstVakPanel)interactiePanel).isTransparant()) ((Component)interactiePanel).setBackground(color);
	}
	
	public void setSize(int b,int h)
	{
		super.setSize(b,h);
		if(!(interactiePanel instanceof TekstVakPanel) && editMode && !popup)
        {	if(afdekPanel!=null)afdekPanel.setSize(b,h);
        }
		if(resizePanel!=null)resizePanel.setBounds(b-6,h-6,6,6);
		if(interactiePanel!=null && !popup)((Component)interactiePanel).setSize(b,h);
	}
	
	public void setEditMode(boolean b)
	{	editMode = b;
		if(!editMode && afdekPanel!=null) remove(afdekPanel);
        //if(b)setBackground(Color.lightGray);
		
	}
	
	public void setStudentEditor(boolean b)
	{	studentEditor = b;
		if(interactiePanel instanceof GrafiekPanel)((GrafiekPanel)interactiePanel).setStudentEditor(b);
	}
	
	public boolean getStudentEditor()
	{	return studentEditor;
	}
	
	//public InteractieEditPanel getEditPanel()
	//{	//AntwoordVakEditPanel aep = new AntwoordVakEditPanel();
		//if(launchData!=null) aep.setEditState(launchData);
	//	return null;
		
	//}
	
	public InteractiePanel getInteractiePanel()
	{	return interactiePanel;
		
	}
	
	public InteractiePanel getInteractiePanel(int ID)
	{	if(interactiePanel==null)return null;
		if(interactiePanel.getIpId()==ID) return interactiePanel;
		if(interactiePanel instanceof TekstVakPanel) return ((TekstVakPanel)interactiePanel).getInteractiePanel(ID);
		return null;
	}
	
	public InteractiePanel zoekInteractiePanel(int ID)
	{	// indien klaarknop niet in hetzelfde tekstvak:
		if(tekstVak.getParent() instanceof TekstVakPanel && tekstVak.hasOneDeelVak())// 
		{	TekstVakPanel  tvp = (TekstVakPanel)tekstVak.getParent();
			//indien in een eigen zwevend tekstvak
			if(tvp.getParent()instanceof TekstInteractiePanelVak && tvp.isZwevend()) 
			{	InteractiePanel ip = (((TekstDeelVak)tvp.getParent()).tekstVak).zoekInteractiePanel(ID);
				if(ip!=null) return ip;
			}
			//indien in een andere cel (kolom/rij) van hetzelfde tekstvakpanel
			if(tvp.getParent() instanceof TekstInteractiePanelVak) //tvp.isZwevend() && 
			{	InteractiePanel ip = ((TekstInteractiePanelVak)tvp.getParent()).getInteractiePanel(ID);
				if(ip!=null) return ip;
			}
			 
		}
		// indien klaarknop wel in hetzelfde tekstvak
		return tekstVak.zoekInteractiePanel(ID);
	}
	
	public Vector zoekInteractiePanels()
	{	
		
		// indien klaarknop niet in hetzelfde tekstvak:
		if(tekstVak.getParent() instanceof TekstVakPanel && tekstVak.hasOneDeelVak())// 
		{	Vector v1 = null;
			TekstVakPanel  tvp = (TekstVakPanel)tekstVak.getParent();
			//indien in een eigen zwevend tekstvak
			if(tvp.getParent()instanceof TekstInteractiePanelVak && tvp.isZwevend()) 
			{	v1 = (((TekstDeelVak)tvp.getParent()).tekstVak).geefInteractiePanels();
			}
			//indien in een andere cel (kolom/rij) van hetzelfde tekstvakpanel
			else if(tvp.getParent() instanceof TekstInteractiePanelVak) //tvp.isZwevend() && 
			{	v1 = ((TekstInteractiePanelVak)tvp.getParent()).geefInteractiePanels();
			}
			return v1; 
		}
		// indien klaarknop wel in hetzelfde tekstvak
		else return tekstVak.geefInteractiePanels();
	}
		
	public void setEditState(Hashtable h)
	{
		if(h==null)return;
		h = ShareAction.unwrapLaunchData(h); // here intervention for shared launchdata
		launchData = h;
		int soortInteractiePanel = 5;
		Hashtable interactiePanelLaunchState = null;
        int breedte = 0;
        int hoogte = 0;
        int locationX = 0;
        int locationY = 0;
        boolean volledigeBreedte = false;
        boolean popup = false;
        int setNr = 0;
        boolean studentEditor = false;
        String popupImageString = null;
        List connections = new JSONArray();
        Map  subscriptions = null;
        String crossWidgetId = null;
        
		if(h.containsKey("soortInteractiePanel")) soortInteractiePanel = ((Integer)h.get("soortInteractiePanel")).intValue();
		if(h.containsKey("interactiePanelLaunchState")) interactiePanelLaunchState = (Hashtable)h.get("interactiePanelLaunchState");
        if(h.containsKey("breedte")) breedte = ((Integer)h.get("breedte")).intValue();
        if(h.containsKey("hoogte")) hoogte = ((Integer)h.get("hoogte")).intValue();
        if(h.containsKey("locationX")) locationX = ((Integer)h.get("locationX")).intValue();
        if(h.containsKey("locationY")) locationY = ((Integer)h.get("locationY")).intValue();
        if(h.containsKey("volledigeBreedte")) volledigeBreedte = ((Boolean)h.get("volledigeBreedte")).booleanValue();
        if(h.containsKey("popup")) popup = ((Boolean)h.get("popup")).booleanValue();
        if(h.containsKey("setNr")) setNr = ((Integer)h.get("setNr")).intValue();
        if(h.containsKey("studentEditor")) studentEditor = ((Boolean)h.get("studentEditor")).booleanValue();
        if(h.containsKey("popupImageString")) popupImageString = (String)h.get("popupImageString");
        if(h.containsKey("crossWidgetId")) crossWidgetId = (String)h.get("crossWidgetId");
        if(h.containsKey("connections")) connections = (List)h.get("connections");
        if(h.containsKey("subscriptions")) subscriptions = (Map) h.get("subscriptions");
        
        this.currentSetNr = setNr;
        this.popup = popup;
        this.soortInteractiePanel = soortInteractiePanel;
        this.studentEditor = studentEditor;
        this.popupImageString = popupImageString;

        if(crossWidgetId != null)
        {
        	XWidgetManager manager = tekstVak.getXWidgetManager();
        	setCrossWidgetId(crossWidgetId);
			manager.updateCrossWidgetId(this);
        }
        this.connections = connections;
        this.subscriptions = toConnector(subscriptions);
        
        if(soortInteractiePanel == 0)
		{	if(interactiePanel==null || !(interactiePanel instanceof AntwoordFormuleVak))
			{	interactiePanel = new AntwoordFormuleVak();
				//interactiePanel.setBounds(0,0,getSize().width, getSize().height);
				interactiePanel.addActionListener(this);
				//add((Component)interactiePanel,0);
				
			}
			interactiePanel.setEditState(interactiePanelLaunchState);
		}
		else if(soortInteractiePanel == 1)
		{
			if(interactiePanel==null || !(interactiePanel instanceof AntwoordVergelijkingVak))
			{	interactiePanel = new AntwoordVergelijkingVak();
				//interactiePanel.setBounds(0,0,getSize().width, getSize().height);
				interactiePanel.addActionListener(this);
				//add((Component)interactiePanel,0);
				
			}
			interactiePanel.setEditState(interactiePanelLaunchState);
		}
		else if(soortInteractiePanel == 2)
		{	if(interactiePanel==null || !(interactiePanel instanceof SimpelAntwoordFormuleVak))
			{	interactiePanel = new SimpelAntwoordFormuleVak();
				interactiePanel.addActionListener(this);
			}
				((SimpelAntwoordFormuleVak)interactiePanel).zetMinBreedte(breedte);
				//interactiePanel.setBounds(0,0,getSize().width, getSize().height);
				setBackground(Color.white);
				
				//add((Component)interactiePanel,0);
				
			
			//interactiePanel.setEditState(interactiePanelLaunchState);
		}
		else if(soortInteractiePanel == 3)
		{	if(interactiePanel==null || !(interactiePanel instanceof SimpelAntwoordVergelijkingVak))
			{	interactiePanel = new SimpelAntwoordVergelijkingVak();
				interactiePanel.addActionListener(this);
			}
				((SimpelAntwoordVergelijkingVak)interactiePanel).zetMinBreedte(breedte);
				//interactiePanel.setBounds(0,0,getSize().width, getSize().height);
				setBackground(Color.white);
				
				//add((Component)interactiePanel,0);
				
			
			//interactiePanel.setEditState(interactiePanelLaunchState);
		}
		else if(soortInteractiePanel == 4)
		{	if(interactiePanel==null || !(interactiePanel instanceof TekstEditor))
			{	interactiePanel = new TekstEditor(true,true,true);
				//((TekstEditor)interactiePanel).zetRekenVak(true);
				//interactiePanel.setBounds(0,0,getSize().width, getSize().height);
				interactiePanel.addActionListener(this);
				//add((Component)interactiePanel,0);
				
			}
			//interactiePanel.setEditState(interactiePanelLaunchState);
		}
		else if(soortInteractiePanel == 8)
		{	if(interactiePanel==null || !(interactiePanel instanceof GrafiekPanel))
			{	interactiePanel = new GrafiekPanel();
				//((TekstEditor)interactiePanel).zetRekenVak(true);
				//interactiePanel.setBounds(0,0,getSize().width, getSize().height);
			((Component)interactiePanel).setBackground(getBackground());
				interactiePanel.addActionListener(this);
				//add((Component)interactiePanel,0);
				
			}
			//interactiePanel.setEditState(interactiePanelLaunchState);
		}
		else if(soortInteractiePanel == 9)
		{	if(interactiePanel==null || !(interactiePanel instanceof TekstVakPanel))
			{	interactiePanel = new TekstVakPanel();
				((TekstVakPanel)interactiePanel).setEditable(true);
				//interactiePanel.setBounds(0,0,getSize().width, getSize().height);
				((Component)interactiePanel).setBackground(getBackground());
				interactiePanel.addActionListener(this);
				
			//add((Component)interactiePanel,0);
			
			}
			//interactiePanel.setEditState(interactiePanelLaunchState);
		}
		else if(soortInteractiePanel == 10)
		{	if(interactiePanel==null || !(interactiePanel instanceof Geogebra3Panel))
			{	Geogebra3Panel geogebra3Panel = new Geogebra3Panel();
				interactiePanel = geogebra3Panel;
				geogebra3Panel.setInstanceId(getCrossWidgetId());
				geogebra3Panel.setFactory(WidgetBridge.getFactory(geogebra3Panel));
				geogebra3Panel.setBackground(getBackground());
				interactiePanel.addActionListener(this);
			}
		}
		else if(soortInteractiePanel == 39)
		{	if(interactiePanel==null || !(interactiePanel instanceof GeogebraPanel))
			{	GeogebraPanel geogebraPanel = new GeogebraPanel();
				interactiePanel = geogebraPanel;
				geogebraPanel.setInstanceId(getCrossWidgetId());
				geogebraPanel.setFactory(WidgetBridge.getFactory(geogebraPanel));
				geogebraPanel.setBackground(getBackground());
				interactiePanel.addActionListener(this);
			}
		}
		else if(soortInteractiePanel == 12)
		{	if(interactiePanel==null || !(interactiePanel instanceof CheckUnitPanel))
			{	interactiePanel = new CheckUnitPanel();
				//((TekstVakPanel)interactiePanel).setEditable(true);
				//interactiePanel.setBounds(0,0,getSize().width, getSize().height);
				((Component)interactiePanel).setBackground(getBackground());
				interactiePanel.addActionListener(this);
				
			//add((Component)interactiePanel,0);
			
			}
			//interactiePanel.setEditState(interactiePanelLaunchState);
		}
		else if(soortInteractiePanel == 13)
		{	if(interactiePanel==null || !(interactiePanel instanceof AntwoordTekstVak))
			{	interactiePanel = new AntwoordTekstVak();
				//((TekstVakPanel)interactiePanel).setEditable(true);
				//interactiePanel.setBounds(0,0,getSize().width, getSize().height);
				((AntwoordTekstVak)interactiePanel).zetMinBreedte(breedte);
				((Component)interactiePanel).setBackground(getBackground());
				interactiePanel.addActionListener(this);
				
			//add((Component)interactiePanel,0);
			
			}
			//interactiePanel.setEditState(interactiePanelLaunchState);
		
		}
		else if(soortInteractiePanel == 14)
		{	if(interactiePanel==null || !(interactiePanel instanceof AntwoordKeuzeVak))
			{	interactiePanel = new AntwoordKeuzeVak();
				//((TekstVakPanel)interactiePanel).setEditable(true);
				//interactiePanel.setBounds(0,0,getSize().width, getSize().height);
				((Component)interactiePanel).setBackground(getBackground());
				interactiePanel.addActionListener(this);
				
			//add((Component)interactiePanel,0);
			
			}
			//interactiePanel.setEditState(interactiePanelLaunchState);
		}
		else if(soortInteractiePanel == 16)
		{	if(interactiePanel==null || !(interactiePanel instanceof CheckSleepUnitPanel))
			{	interactiePanel = new CheckSleepUnitPanel();
				//((TekstVakPanel)interactiePanel).setEditable(true);
				//interactiePanel.setBounds(0,0,getSize().width, getSize().height);
				((Component)interactiePanel).setBackground(getBackground());
				interactiePanel.addActionListener(this);
				
			//add((Component)interactiePanel,0);
			
			}
			//interactiePanel.setEditState(interactiePanelLaunchState);
		}
		else if(soortInteractiePanel == 25)
		{	if(interactiePanel==null || !(interactiePanel instanceof GetallenlijnSprongPanel))
			{	interactiePanel = new GetallenlijnSprongPanel();
				//((TekstVakPanel)interactiePanel).setEditable(true);
				//interactiePanel.setBounds(0,0,getSize().width, getSize().height);
				((Component)interactiePanel).setBackground(getBackground());
				interactiePanel.addActionListener(this);
				
			//add((Component)interactiePanel,0);
			
			}
			//interactiePanel.setEditState(interactiePanelLaunchState);
		}
		else if(soortInteractiePanel == 33)
        {   if(interactiePanel==null || !(interactiePanel instanceof CheckValueUnitPanel))
            {   interactiePanel = new CheckValueUnitPanel();
                //((TekstVakPanel)interactiePanel).setEditable(true);
                //interactiePanel.setBounds(0,0,getSize().width, getSize().height);
                ((Component)interactiePanel).setBackground(getBackground());
                interactiePanel.addActionListener(this);
                
            //add((Component)interactiePanel,0);
            
            }
            //interactiePanel.setEditState(interactiePanelLaunchState);
        }
		else if(soortInteractiePanel == 49)
        {   if(interactiePanel==null || !(interactiePanel instanceof CheckButtonPanel))
            {   interactiePanel = new CheckButtonPanel();
                //((TekstVakPanel)interactiePanel).setEditable(true);
                //interactiePanel.setBounds(0,0,getSize().width, getSize().height);
                ((Component)interactiePanel).setBackground(getBackground());
                interactiePanel.addActionListener(this);
                
            //add((Component)interactiePanel,0);
            
            }
            //interactiePanel.setEditState(interactiePanelLaunchState);
        }
		else if(soortInteractiePanel == 52)
        {   if(interactiePanel==null || !(interactiePanel instanceof ReactieVergelijkingVak))
            {   interactiePanel = new ReactieVergelijkingVak();
                //((TekstVakPanel)interactiePanel).setEditable(true);
                //interactiePanel.setBounds(0,0,getSize().width, getSize().height);
                ((Component)interactiePanel).setBackground(getBackground());
                interactiePanel.addActionListener(this);
                
            //add((Component)interactiePanel,0);
            
            }
            //interactiePanel.setEditState(interactiePanelLaunchState);
        }
        
		else if(soortInteractiePanel == 53)
        {   if(interactiePanel==null || !(interactiePanel instanceof StelselAntwoordVak))
            {   interactiePanel = new StelselAntwoordVak();
                //((TekstVakPanel)interactiePanel).setEditable(true);
                //interactiePanel.setBounds(0,0,getSize().width, getSize().height);
                ((Component)interactiePanel).setBackground(getBackground());
                interactiePanel.addActionListener(this);
                
            //add((Component)interactiePanel,0);
            
            }
            //interactiePanel.setEditState(interactiePanelLaunchState);
        }
		else if(soortInteractiePanel == 55)
		{
			if(interactiePanel == null || !(interactiePanel instanceof SymboolPanel))
			{
				interactiePanel = new SymboolPanel();
				((Component)interactiePanel).setBackground(getBackground());
				interactiePanel.addActionListener(this);
			}
		}
		else if(soortInteractiePanel > 4)
		{  	for(int i=0 ; i<TekstInteractiePanelVak.wiskOpdrInteractiePanels.length ; i++)
			{	if(soortInteractiePanel == interactiePanelSets[1][i])
				{	interactiePanel = maakInteractiePanel(TekstInteractiePanelVak.wiskOpdrInteractiePanels[i][0], WiskOpdr.language);
					if(interactiePanel==null) return;
					//interactiePanel.setBounds(0,0,getSize().width, getSize().height);
					if(!(interactiePanel instanceof JButton))((Component)interactiePanel).setBackground(getBackground());
					interactiePanel.addActionListener(this);
					//add((Component)interactiePanel,0);
					break;
				}
			}
			//interactiePanel.setEditState(interactiePanelLaunchState);
			//interactiePanel.start();
		}
		else if(soortInteractiePanel == CBOOKWIDGET)
		{
			String clazz = String.valueOf(h.get(CBOOKWIDGET_NAME));
			final CBookWidgetIF widget = Service.widgetForName(clazz);
			if(widget != null){
				ResourceManagerFactory factory = WidgetBridge.getFactory(widget, getCrossWidgetId());
				interactiePanel = new CBookInteractiePanel(widget, getCrossWidgetId(), factory);
				interactiePanel.addActionListener(this);
			}
			else
				interactiePanel = null;
		}
		else
		{	interactiePanel = null;
			
		}
        
        //pas hier setSize om te zorgen dat maat vh interactiePanel ook meteen goed wordt gezet 
        //(bijv belangrijk voor volledige breedte tekstvakpanel). 
        if(volledigeBreedte)setSize(tekstVak.getSize().width-2*tekstVak.geefMarge(),hoogte);        
        else setSize(breedte, hoogte);
        
        setLocation(locationX,locationY);
        
        
        
        boolean ipAdded = false;
        if(interactiePanel!=null) 
        {	interactiePanel.setBounds(0,0,getSize().width, getSize().height);
// FIXME Wat heeft deze lijst te betekenen. geogebrapanel is nu een JROOTPANE geworden, was JLayeredPane
        	if(interactiePanel instanceof JPanel || interactiePanel instanceof JLayeredPane  || interactiePanel instanceof JRootPane || popup)
        	{ 	ipAdded = true;
        		if(popup)
        		{
        			if(popupJButton==null) 
        	        {
        				if(soortInteractiePanel == CBOOKWIDGET && interactiePanel instanceof CBookInteractiePanel && popupImageString != null)
        				{
        					Icon icon = ((CBookInteractiePanel) interactiePanel).getIcon();
							popupJButton = new JButton(icon);
        					popupJButton.addActionListener(this);
               				popupJButton.setBorder(BorderFactory.createLineBorder(Color.gray));
               				popupJButton.setSize(icon.getIconWidth(), icon.getIconHeight());
               			        					
        				} else {
        					FormuleButton popupButton;
        				popupJButton = popupButton = new FormuleButton(interactiePanelSetNames[setNr]);
        				popupButton.setBounds(0,0,20,20);
        				popupButton.setBorder(BorderFactory.createLineBorder(Color.gray));
        				popupButton.addActionListener(this);
        				
        				if(popupImageString!=null && !"".equals(popupImageString))
        				{   iconman = new Iconan(WiskOpdr.applet, (Component)this, (Hashtable)TekstImageVak.getImageMap());
	        		    	if(popupImageString!=null && !"".equals(popupImageString))popupImage = iconman.getImage(popupImageString);
	        		    	popupButton.setPopupButtonImage(popupImage);
	        		    	int imWidth = iconman.getWidth(popupImageString);
	        				int imHeight = iconman.getHeight(popupImageString);
	        				if(imWidth == -1) imWidth = 20;
	        				if(imHeight == -1) imHeight = 20;
	        				popupButton.setSize(imWidth,imHeight);
	        		    	//popupButton.setSize(popupImage.getWidth(null), popupImage.getHeight(null));
        				}}
        	        }
        			//if(popupFrame==null) maakPopupFrame();
        			//popupFrame.getContentPane().add((Component)interactiePanel,0);
        			new PopupContainer().add((Component)interactiePanel);
        			interactiePanel.setEditState(interactiePanelLaunchState);
        			add(popupJButton,0);
        			setSize(popupJButton.getSize());
        		}
        		else
        		{	
        			//if(volledigeBreedte)interactiePanel.setBounds(0,0,tekstVak.getSize().width-2*tekstVak.geefMarge(), hoogte);
    	        	//else interactiePanel.setBounds(0,0,breedte,hoogte);
        			
        			add((Component)interactiePanel,0);
        			if(volledigeBreedte)
        				setSize(tekstVak.getSize().width-2*tekstVak.geefMarge(),hoogte);
        			else
        				setSize(breedte, hoogte);
        			interactiePanel.setEditState(interactiePanelLaunchState);
        			
        			
        		}	
        	}
			//interactiePanel.start();
        }
        setStudentEditor(studentEditor);
        
        if(afdekPanel==null) 
        {	afdekPanel = new JPanel()
        		/*{	public void paintComponent(Graphics g)
		        	{	if(selected) 
		        		{	g.setColor(new Color(0,0,0,128));
		        			g.fillRect(0,0,getWidth(), getHeight());
		        		
		        		}
		        		else super.paintComponent(g);
		        	}
        		}*/
        	;
        	afdekPanel.setLayout(new BorderLayout());
			afdekPanel.setOpaque(false);
	        afdekPanel.addMouseListener(this);
	        afdekPanel.addMouseMotionListener(this);
	        
	        vervangingsPanel = new JPanel(){
				
	        	String tekst = "Interactief component";
	        	
				public void paintComponent(Graphics g)
				{	int x = getBounds().x;
					int y = getBounds().y;
					int b = getBounds().width;
					int h = getBounds().height;
					g.setColor(new Color(230,230,230));
					g.fillRect(x,y,b,h);
					g.setColor(Color.gray);
					g.drawRect(x,y,b-1,h-1);
					g.setFont(new Font("SansSerif",Font.BOLD,20));
					g.drawString(tekst,20,40);
					
				}
				public void setName(String s)
				{	super.setName(getName());
					tekst = s;
					repaint();
				}
			};
		}
        if(!ipAdded) 
        {	afdekPanel.add(vervangingsPanel);
        	if(soortInteractiePanel>-1)
        		vervangingsPanel.setName(interactiePanelDescriptions[soortInteractiePanel]);
        }
        else afdekPanel.remove(vervangingsPanel);
        
        if(	resizePanel==null) 
        {	resizePanel = new JPanel();
        	resizePanel.setOpaque(false);
        	resizePanel.addMouseListener(this);
        	resizePanel.addMouseMotionListener(this);
	        
        }
        afdekPanel.setBounds(0,0,breedte, hoogte);
        if(interactiePanel instanceof TekstVakPanel)
        {	((TekstVakPanel)interactiePanel).setEditable(true);
    		if(!popup)
    		{	afdekPanel.setBounds(0,0,6,6);
    			afdekPanel.setOpaque(true);
    			afdekPanel.setBackground(Color.black);
    			
    			resizePanel.setBounds(getSize().width-6,getSize().height-6,6,6);
    			resizePanel.setOpaque(true);
    			resizePanel.setBackground(Color.black);
    			add(resizePanel,0);
    		}
    		/*
    		if(	callOutPosPanel==null) 
            {	callOutPosPanel = new JPanel();
	            callOutPosPanel.setOpaque(false);
	            callOutPosPanel.addMouseListener(this);
	            callOutPosPanel.addMouseMotionListener(this);
    	    }
    		*/
        }
        add(afdekPanel,0);
        setEditMode(true);
        
        zetMaat();
		
        
	}
	
	
	
	private Map<String, Set<Connector>> toConnector(Map s) {
		if (s == null)
			return null;
		TreeMap<String,Set<Connector>> result = new TreeMap();
		Set<Entry<?,?>> entrySet = s.entrySet();
		for (Entry<?, ?> entry : entrySet) {
			String key = (String) entry.getKey();
			TreeSet<Connector> newValue  = new TreeSet<Connector>();
			Set<Map<String,String>> value = (Set) entry.getValue();
			for (Map<String, String> map : value) {
				for(Entry<String,String> connector : map.entrySet())
				{
					newValue.add(new Connector(connector.getKey(), connector.getValue()));
				}
			}
			result.put(key, newValue);
		}
		return result;
	}

	public Hashtable getEditState()
	{	
		int locationX = 0;
		int locationY = 0;
		int breedte = 0;
		int hoogte = 0;
		boolean studentEditor = false;
		String crossWidgetId;
		List connections;
		Map  subscriptions;
		
		studentEditor = this.studentEditor;
		crossWidgetId = this.crossWidgetId;
		connections = this.connections;
		subscriptions = replaceConnector(this.subscriptions);
	       		
		if((interactiePanel instanceof TekstVakPanel && !popup)){
			Hashtable interactiePanelLaunchState = interactiePanel.getEditState();
			locationX = getLocation().x;
			locationY = getLocation().y;
			
			interactiePanelLaunchState.put("locationX", new Integer(locationX));
			interactiePanelLaunchState.put("locationY", new Integer(locationY));
			
			breedte = getSize().width;
			hoogte = getSize().height;
			
			launchData.put("locationX", new Integer(locationX));
			launchData.put("locationY", new Integer(locationY));
			launchData.put("breedte", new Integer(breedte));
			launchData.put("hoogte", new Integer(hoogte));
			launchData.put("interactiePanelLaunchState", interactiePanelLaunchState);
			
			
			
		}
		else if(interactiePanel!=null && interactiePanel instanceof TekstVakPanel)
		{	Hashtable interactiePanelLaunchState = interactiePanel.getEditState();
			launchData.put("interactiePanelLaunchState", interactiePanelLaunchState);
		}
		
		//else if(interactiePanel!=null && interactiePanel instanceof GeogebraPanel)
		//{	Hashtable interactiePanelLaunchState = interactiePanel.getEditState();
		//	launchData.put("interactiePanelLaunchState", interactiePanelLaunchState);
		//}

		else if(interactiePanel!=null)//Ook nodig bij andere interactiePanels?????
		{	launchData.put("studentEditor", new Boolean(studentEditor));
			
			Hashtable interactiePanelLaunchState = interactiePanel.getEditState();
			if(studentEditor && interactiePanelLaunchState!=null)launchData.put("interactiePanelLaunchState", interactiePanelLaunchState);
		}
		if(crossWidgetId != null)
			launchData.put("crossWidgetId", crossWidgetId);
		if(!connections.isEmpty())
			launchData.put("connections", connections);
		else 
			launchData.remove("connections");
		if(subscriptions != null && !subscriptions.isEmpty())
			launchData.put("subscriptions", subscriptions);
		else 
			launchData.remove("subscriptions");

		return launchData;
	}
	
	private Map<String,Set<Map<?,?>>> replaceConnector(Map<String, Set<Connector>> s) {
		if(s == null) return null;
		Map<String,Set<Map<?,?>>> result = new Hashtable<String, Set<Map<?, ?>>>();
		Set<Entry<String, Set<Connector>>> entrySet = s.entrySet();
		for (Entry<String, Set<Connector>> entry : entrySet) {
			Set<Connector> value = entry.getValue();
			if(value.isEmpty()) continue;
			String key = entry.getKey();
			Set newValue = new HashSet();
			for (Connector connector : value) {
				newValue.add(new Hashtable(connector));
			}
			result.put(key, newValue);
		}
		return result;
	}

	public void zetOpdracht(Hashtable h, String[] randomVars, Hashtable randomValues)
	{	if(h==null)return;
	
		h = ShareAction.unwrapLaunchData(h); // intervention for launchdata (might be not nessessary)
		launchData = h;
		
		int soortInteractiePanel = 0;
		Hashtable interactiePanelLaunchState = null;
        int breedte = 0;
        int hoogte = 0;
        int locationX = 0;
        int locationY = 0;
        boolean volledigeBreedte = false;
        boolean popup = false;
        int setNr = 0;
        boolean studentEditor = false;
        String popupImageString = null;
        //String crossWidgetId = null;
		List connections = new JSONArray();
		Map  subscriptions = null;
        
		
		if(h.containsKey("soortInteractiePanel")) soortInteractiePanel = ((Integer)h.get("soortInteractiePanel")).intValue();
		if(h.containsKey("interactiePanelLaunchState")) interactiePanelLaunchState = (Hashtable)h.get("interactiePanelLaunchState");
        if(h.containsKey("breedte")) breedte = ((Integer)h.get("breedte")).intValue();
        if(h.containsKey("hoogte")) hoogte = ((Integer)h.get("hoogte")).intValue();
        if(h.containsKey("locationX")) locationX = ((Integer)h.get("locationX")).intValue();
        if(h.containsKey("locationY")) locationY = ((Integer)h.get("locationY")).intValue();
        if(h.containsKey("volledigeBreedte")) volledigeBreedte = ((Boolean)h.get("volledigeBreedte")).booleanValue();
        if(h.containsKey("popup")) popup = ((Boolean)h.get("popup")).booleanValue();
        if(h.containsKey("setNr")) setNr = ((Integer)h.get("setNr")).intValue();
        if(h.containsKey("studentEditor")) studentEditor = ((Boolean)h.get("studentEditor")).booleanValue();
        if(h.containsKey("popupImageString")) popupImageString = (String)h.get("popupImageString");
        if(h.containsKey("crossWidgetId")) crossWidgetId = (String)h.get("crossWidgetId");
        if(h.containsKey("connections")) connections = (List)h.get("connections");
        if(h.containsKey("subscriptions")) subscriptions = (Map) h.get("subscriptions");
        //System.out.println("in zetopdracht: crossWidgetId: "+ crossWidgetId);
        if(crossWidgetId != null)
        	tekstVak.getXWidgetManager().updateCrossWidgetId(this);
        if(volledigeBreedte)setSize(tekstVak.getSize().width-2*tekstVak.geefMarge(),hoogte);        
        else setSize(breedte, hoogte);
        
        setLocation(locationX,locationY);
        
        this.popup = popup;
        this.soortInteractiePanel = soortInteractiePanel;
        this.studentEditor = studentEditor;
        this.connections = connections;
        this.subscriptions = toConnector(subscriptions);
        //this.crossWidgetId = crossWidgetId;

        
         
        if(soortInteractiePanel == 0)
		{	if(interactiePanel==null || !(interactiePanel instanceof AntwoordFormuleVak))
			{	interactiePanel = new AntwoordFormuleVak();
				interactiePanel.setBounds(0,0,getSize().width, getSize().height);
				interactiePanel.addActionListener(this);
				//add((Component)interactiePanel,0);
				
			}
			//interactiePanel.zetOpdracht(interactiePanelLaunchState,randomVars,randomValues);
		}
		else if(soortInteractiePanel == 1)
		{
			if(interactiePanel==null || !(interactiePanel instanceof AntwoordVergelijkingVak))
			{	interactiePanel = new AntwoordVergelijkingVak();
				interactiePanel.setBounds(0,0,getSize().width, getSize().height);
				interactiePanel.addActionListener(this);
				//add((Component)interactiePanel,0);
				
			}
			//interactiePanel.zetOpdracht(interactiePanelLaunchState,randomVars,randomValues);
		}
		else if(soortInteractiePanel == 2)
		{	if(interactiePanel==null || !(interactiePanel instanceof SimpelAntwoordFormuleVak))
			{	interactiePanel = new SimpelAntwoordFormuleVak();
				((SimpelAntwoordFormuleVak)interactiePanel).zetMinBreedte(breedte);
				interactiePanel.setBounds(0,0,getSize().width, getSize().height);
				setBackground(Color.white);
				interactiePanel.addActionListener(this);
				//add((Component)interactiePanel,0);
			}
			//interactiePanel.zetOpdracht(interactiePanelLaunchState,randomVars,randomValues);
		}
		else if(soortInteractiePanel == 3)
		{	if(interactiePanel==null || !(interactiePanel instanceof SimpelAntwoordVergelijkingVak))
			{	interactiePanel = new SimpelAntwoordVergelijkingVak();
				((SimpelAntwoordVergelijkingVak)interactiePanel).zetMinBreedte(breedte);
				interactiePanel.setBounds(0,0,getSize().width, getSize().height);
				setBackground(Color.white);
				interactiePanel.addActionListener(this);
				//add((Component)interactiePanel,0);
				
			}
			//interactiePanel.zetOpdracht(interactiePanelLaunchState,randomVars,randomValues);
		}
		else if(soortInteractiePanel == 4)
		{	if(interactiePanel==null || !(interactiePanel instanceof TekstEditor))
			{	interactiePanel = new TekstEditor(true,true,true);
				//((TekstEditor)interactiePanel).zetRekenVak(true);
				interactiePanel.setBounds(0,0,getSize().width, getSize().height);
				interactiePanel.addActionListener(this);
				//add((Component)interactiePanel,0);
				
			}
			//interactiePanel.zetOpdracht(interactiePanelLaunchState,randomVars,randomValues);
		}
		else if(soortInteractiePanel == 8)
		{	if(interactiePanel==null || !(interactiePanel instanceof GrafiekPanel))
			{	interactiePanel = new GrafiekPanel();
				interactiePanel.setBounds(0,0,breedte,hoogte);
				((Component)interactiePanel).setBackground(getBackground());
				interactiePanel.addActionListener(this);
				//add((Component)interactiePanel,0);
			}
			//interactiePanel.zetOpdracht(interactiePanelLaunchState,randomVars,randomValues);
		}
		else if(soortInteractiePanel == 9)
		{	if(interactiePanel==null || !(interactiePanel instanceof TekstVakPanel))
			{	interactiePanel = new TekstVakPanel();
				//((TekstEditor)interactiePanel).zetRekenVak(true);
				interactiePanel.setBounds(0,0,getSize().width, getSize().height);
				((Component)interactiePanel).setBackground(getBackground());
				interactiePanel.addActionListener(this);
				//add((Component)interactiePanel,0);
			}
			
		}
		else if(soortInteractiePanel == 10)
		{	if(interactiePanel==null || !(interactiePanel instanceof Geogebra3Panel))
			{	Geogebra3Panel geogebra3Panel = new Geogebra3Panel();
				interactiePanel = geogebra3Panel;
				geogebra3Panel.setBackground(getBackground());
				geogebra3Panel.setInstanceId(getCrossWidgetId());
				geogebra3Panel.setFactory(WidgetBridge.getFactory(geogebra3Panel));
				interactiePanel.addActionListener(this);
			}
		}
		else if(soortInteractiePanel == 39)
		{	if(interactiePanel==null || !(interactiePanel instanceof GeogebraPanel))
			{	GeogebraPanel geogebraPanel = new GeogebraPanel();
				interactiePanel = geogebraPanel;
				geogebraPanel.setBackground(getBackground());
				geogebraPanel.setInstanceId(getCrossWidgetId());
				geogebraPanel.setFactory(WidgetBridge.getFactory(geogebraPanel));
				interactiePanel.addActionListener(this);
			}
		}
		else if(soortInteractiePanel == 12)
		{	if(interactiePanel==null || !(interactiePanel instanceof CheckUnitPanel))
			{	interactiePanel = new CheckUnitPanel();
				//((TekstVakPanel)interactiePanel).setEditable(true);
				//interactiePanel.setBounds(0,0,getSize().width, getSize().height);
				((Component)interactiePanel).setBackground(getBackground());
				interactiePanel.addActionListener(this);
				
			//add((Component)interactiePanel,0);
			
			}
		}
		else if(soortInteractiePanel == 13)
		{	if(interactiePanel==null || !(interactiePanel instanceof AntwoordTekstVak))
			{	interactiePanel = new AntwoordTekstVak();
				//((TekstVakPanel)interactiePanel).setEditable(true);
				//interactiePanel.setBounds(0,0,getSize().width, getSize().height);
				((AntwoordTekstVak)interactiePanel).zetMinBreedte(breedte);
				((Component)interactiePanel).setBackground(getBackground());
				interactiePanel.addActionListener(this);
				
			//add((Component)interactiePanel,0);
			
			}
		}
		else if(soortInteractiePanel == 14)
		{	if(interactiePanel==null || !(interactiePanel instanceof AntwoordKeuzeVak))
			{	interactiePanel = new AntwoordKeuzeVak();
				//((TekstVakPanel)interactiePanel).setEditable(true);
				//interactiePanel.setBounds(0,0,getSize().width, getSize().height);
				((Component)interactiePanel).setBackground(getBackground());
				interactiePanel.addActionListener(this);
				
			//add((Component)interactiePanel,0);
			
			}
		}
		else if(soortInteractiePanel == 16)
		{	if(interactiePanel==null || !(interactiePanel instanceof CheckSleepUnitPanel))
			{	interactiePanel = new CheckSleepUnitPanel();
				//((TekstVakPanel)interactiePanel).setEditable(true);
				//interactiePanel.setBounds(0,0,getSize().width, getSize().height);
				((Component)interactiePanel).setBackground(getBackground());
				interactiePanel.addActionListener(this);
				
			//add((Component)interactiePanel,0);
			
			}
		}
		else if(soortInteractiePanel == 25)
		{	if(interactiePanel==null || !(interactiePanel instanceof GetallenlijnSprongPanel))
			{	interactiePanel = new GetallenlijnSprongPanel();
				//((TekstVakPanel)interactiePanel).setEditable(true);
				//interactiePanel.setBounds(0,0,getSize().width, getSize().height);
				((Component)interactiePanel).setBackground(getBackground());
				interactiePanel.addActionListener(this);
				
			//add((Component)interactiePanel,0);
			
			}
			//interactiePanel.setEditState(interactiePanelLaunchState);
		}
		else if(soortInteractiePanel == 33)
        {   if(interactiePanel==null || !(interactiePanel instanceof CheckValueUnitPanel))
            {   interactiePanel = new CheckValueUnitPanel();
                //((TekstVakPanel)interactiePanel).setEditable(true);
                //interactiePanel.setBounds(0,0,getSize().width, getSize().height);
                ((Component)interactiePanel).setBackground(getBackground());
                interactiePanel.addActionListener(this);
                
            //add((Component)interactiePanel,0);
            
            }
            //interactiePanel.setEditState(interactiePanelLaunchState);
        }
		else if(soortInteractiePanel == 49)
        {   if(interactiePanel==null || !(interactiePanel instanceof CheckButtonPanel))
            {   interactiePanel = new CheckButtonPanel();
                //((TekstVakPanel)interactiePanel).setEditable(true);
                //interactiePanel.setBounds(0,0,getSize().width, getSize().height);
                ((Component)interactiePanel).setBackground(getBackground());
                interactiePanel.addActionListener(this);
                
            //add((Component)interactiePanel,0);
            
            }
            //interactiePanel.setEditState(interactiePanelLaunchState);
        }
		else if(soortInteractiePanel == 52)
        {   if(interactiePanel==null || !(interactiePanel instanceof ReactieVergelijkingVak))
            {   interactiePanel = new ReactieVergelijkingVak();
                //((TekstVakPanel)interactiePanel).setEditable(true);
                //interactiePanel.setBounds(0,0,getSize().width, getSize().height);
                ((Component)interactiePanel).setBackground(getBackground());
                interactiePanel.addActionListener(this);
                
            //add((Component)interactiePanel,0);
            
            }
            //interactiePanel.setEditState(interactiePanelLaunchState);
        }
        
		else if(soortInteractiePanel == 53)
        {   if(interactiePanel==null || !(interactiePanel instanceof StelselAntwoordVak))
            {   interactiePanel = new StelselAntwoordVak();
                //((TekstVakPanel)interactiePanel).setEditable(true);
                //interactiePanel.setBounds(0,0,getSize().width, getSize().height);
                ((Component)interactiePanel).setBackground(getBackground());
                interactiePanel.addActionListener(this);
                
            //add((Component)interactiePanel,0);
            
            }
            //interactiePanel.setEditState(interactiePanelLaunchState);
        }
		else if(soortInteractiePanel == 55)
		{
			if(interactiePanel == null || !(interactiePanel instanceof SymboolPanel))
			{
				interactiePanel = new SymboolPanel();
				((Component)interactiePanel).setBackground(getBackground());
				interactiePanel.addActionListener(this);
			}
		}
		else if(soortInteractiePanel > 4)
		{  	for(int i=0 ; i<TekstInteractiePanelVak.wiskOpdrInteractiePanels.length ; i++)
			{	if(soortInteractiePanel == interactiePanelSets[1][i])
				{	interactiePanel = maakInteractiePanel(TekstInteractiePanelVak.wiskOpdrInteractiePanels[i][0], WiskOpdr.language);
					if(interactiePanel==null) return;
					interactiePanel.setBounds(0,0,getSize().width, getSize().height);
					if(!(interactiePanel instanceof JButton))((Component)interactiePanel).setBackground(getBackground());
					interactiePanel.addActionListener(this);
					//add((Component)interactiePanel,0);
					break;
				}
			}
			//interactiePanel.zetOpdracht(interactiePanelLaunchState,randomVars,randomValues);
			//interactiePanel.start();
		}
		else if(soortInteractiePanel == CBOOKWIDGET)
		{	
			String widgetname = (String) launchData.get(CBOOKWIDGET_NAME);
			final CBookWidgetIF w = Service.widgetForName(widgetname);
			if(w != null){
				ResourceManagerFactory factory = WidgetBridge.getFactory(w, getCrossWidgetId());
				interactiePanel = new CBookInteractiePanel(w, getCrossWidgetId(), factory);
				interactiePanel.addActionListener(this);
			}
		}
		else
		{	interactiePanel = null;
			
		}
        
        if(interactiePanel!=null) 
        {	
        	if(popup)
        	{
        		if(popupJButton==null) 
    	        {
    				if(soortInteractiePanel == CBOOKWIDGET && interactiePanel instanceof CBookInteractiePanel && popupImageString != null)
    				{
    					Icon icon;
    					// if (popupImageString) icon = new ImageIcon(...); 
    					icon = ((CBookInteractiePanel) interactiePanel).getIcon();
						popupJButton = new JButton(icon);
    					popupJButton.addActionListener(this);
           				popupJButton.setBorder(BorderFactory.createLineBorder(Color.gray));
           				popupJButton.setSize(icon.getIconWidth(), icon.getIconHeight());
           			    popupJButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));   					
    				} 
    				else 
    				{
	        			FormuleButton popupButton;
	        			popupJButton = popupButton = new FormuleButton(interactiePanelSetNames[setNr],FormuleButton.MEERKNOP);
	    				popupButton.setBounds(0,0,20,20);
	    				if("MW".equals(WiskOpdr.deployVariant) && (soortInteractiePanel==8 || soortInteractiePanel==9 || soortInteractiePanel==10))popupButton.setBounds(0,0,50,50);
	    				popupButton.addActionListener(this);
	    				
	    				if(popupImageString!=null && !"".equals(popupImageString))
	    				{   iconman = new Iconan(WiskOpdr.applet, (Component)this, (Hashtable)TekstImageVak.getImageMap());
	        		    	if(popupImageString!=null && !"".equals(popupImageString))popupImage = iconman.getImage(popupImageString);
	        		    	popupButton.setPopupButtonImage(popupImage);
	        		    	int imWidth = iconman.getWidth(popupImageString);
	        				int imHeight = iconman.getHeight(popupImageString);
	        				if(imWidth == -1) imWidth = 20;
	        				if(imHeight == -1) imHeight = 20;
	        				popupButton.setSize(imWidth,imHeight);
	    				}
	    			}
    	        }
        		interactiePanel.setBounds(0,0,breedte,hoogte);
        		//if(popupFrame==null) maakPopupFrame();
    	        //popupFrame.getContentPane().add((Component)interactiePanel,0);
        		new PopupContainer().add((Component) interactiePanel); // vul getParent() van interactiepanel
        		interactiePanel.zetOpdracht(interactiePanelLaunchState,randomVars,randomValues);
    	        if(interactiePanel instanceof GrafiekPanel)((GrafiekPanel)interactiePanel).setPopupView(true);
    			//todo setPopupview opnemen in de interface InteractiePanel
    			add(popupJButton,0);
    			interactiePanel.start();
    			setSize(popupJButton.getSize());
    			if("MW".equals(WiskOpdr.deployVariant) && (soortInteractiePanel==8 || soortInteractiePanel==9 || soortInteractiePanel==10)) setSize(50,50);
    			else setSize(popupJButton.getSize());
        	}
        	else
        	{	if(volledigeBreedte)interactiePanel.setBounds(0,0,tekstVak.getSize().width-2*tekstVak.geefMarge(), hoogte);
	        	else interactiePanel.setBounds(0,0,breedte,hoogte);
		        add((Component)interactiePanel,0);
		        try {
					interactiePanel.zetOpdracht(interactiePanelLaunchState,randomVars,randomValues);
					interactiePanel.start();
				} catch (Exception e) {
					Logger.getLogger(getClass().getName()).log(Level.WARNING, "zetOpdracht " + interactiePanel, e);
				}
        	}
        }
        if(afdekPanel!=null) remove(afdekPanel);
        setEditMode(false);
        
        setStudentEditor(studentEditor);
        
        
        if(interactiePanel instanceof TekstVakPanel && ((TekstVakPanel)interactiePanel).isIpSleepbaar())
        {
	        if(sleepPanel==null) 
	        {	sleepPanel = new JPanel();
		        sleepPanel.setOpaque(false);
		        sleepPanel.addMouseListener(this);
		        sleepPanel.addMouseMotionListener(this);
		        
	        }
	        
	        sleepPanel.setBounds(0,0,breedte, hoogte);
	        if(((TekstVakPanel)interactiePanel).hasSleepHandle())
	        {	sleepPanel.setLayout(null);
	        	sleepPanel.setBounds(0,0,20, 20);
	        	ImageComponent ic = new ImageComponent(WiskOpdr.loadImage("resources/crosshair.gif"));
	        	ic.setBounds(1,1,20,20);
	        	sleepPanel.add(ic);
	        	
	        }
	        add(sleepPanel,0);
        }
        
		zetMaat();
		
		
	}
	
	public Vector geefInteractiePanels()
	{	Vector v = null;
		if(interactiePanel instanceof TekstVakPanel)
		{
			v = ((TekstVakPanel)interactiePanel).geefInteractiePanels();
		}
		return v;
	}
	
	private InteractiePanel maakInteractiePanel(String name, Locale language)
	{
		try
		{	Class c = Class.forName(name);
	    	Constructor cc = c.getDeclaredConstructor(new Class[] { Locale.class } );
	    	Object o = cc.newInstance(new Object[] { language } );
	    	return ((WiskOpdrApplet)o).getInteractiePanel();
		}
		catch(Exception e)
		{	//System.out.println("kijk"+e.toString());
			return null;
		}
	}
	
	public void setState(Hashtable h)
	{
		h = ShareAction.unwrapState(launchData, h);
		// potentieel probleem dat widgets niet tegen lege state kunnen
		if(h != null && !h.isEmpty()) // skip? hoe?
		try {
			if(interactiePanel!=null)interactiePanel.setState(h);
		} catch (Exception e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
		}
		if(interactiePanel instanceof TekstVakPanel && ((TekstVakPanel)interactiePanel).isIpSleepbaar()) 
		{	
			int x = ((TekstVakPanel)interactiePanel).geefLocatie().x;
			int y = ((TekstVakPanel)interactiePanel).geefLocatie().y;
			setLocation(x, y);
		
		}
		
	}
	
	
	public Hashtable getState()
	{
		return ShareAction.wrapState(launchData, getState_int());
	}
	
	private Hashtable getState_int()
	{		
		if(interactiePanel!=null)return interactiePanel.getState();
		else return null;
	}
	
	public void paint(Graphics g)
	{	
		
		super.paint(g);
		if(selected && editMode) 
		{	g.setColor(new Color(0,0,0,128));
			g.fillRect(0,0,getWidth(), getHeight());
		
		}
		
		
	}
	
	
	public void zetMode(int mode)
	{	if(interactiePanel!=null)interactiePanel.zetMode(mode);
	}
	
	public void zetNagekeken(boolean b)
	{	if(interactiePanel!=null)interactiePanel.zetNagekeken(b);
	}
	
	
	public void wis()
    {   if(interactiePanel!=null)interactiePanel.wis();
    }
	
	public int getScore()
	{	if(interactiePanel!=null) return interactiePanel.getScore();
		else return 0;
	}
	
	public int[][] getScoreObjectives()
	{	if(interactiePanel!=null && (interactiePanel instanceof SimpelAntwoordFormuleVak 
			|| interactiePanel instanceof TekstVakPanel 
			|| interactiePanel instanceof AntwoordFormuleVak 
			|| interactiePanel instanceof SimpelAntwoordVergelijkingVak
			|| interactiePanel instanceof AntwoordVergelijkingVak
			|| interactiePanel instanceof CheckUnitPanel
			|| interactiePanel instanceof CheckSleepUnitPanel
			|| interactiePanel instanceof AntwoordTekstVak
			|| interactiePanel instanceof AntwoordKeuzeVak
			|| interactiePanel instanceof CheckValueUnitPanel
			|| interactiePanel instanceof CBookInteractiePanel
			))
			return interactiePanel.getScoreObjectives();
		else return null;
	}
	
	//dit liever met een extra interface methode van InteractiePanel
	public int[][] getMeasuredMisconceptions()
	{	if(interactiePanel!=null && interactiePanel instanceof AntwoordFormuleVak)
			return ((AntwoordFormuleVak)interactiePanel).getMeasuredMisconceptions();
		if(interactiePanel!=null && interactiePanel instanceof SimpelAntwoordFormuleVak)
			return ((SimpelAntwoordFormuleVak)interactiePanel).getMeasuredMisconceptions();
		if(interactiePanel!=null && interactiePanel instanceof CheckUnitPanel)
			return ((CheckUnitPanel)interactiePanel).getMeasuredMisconceptions();
		if(interactiePanel!=null && interactiePanel instanceof TekstVakPanel)
			return ((TekstVakPanel)interactiePanel).getMeasuredMisconceptions();
		return null;
	}
	
	//dit liever met een extra interface methode van InteractiePanel
	public int[][] getPossibleMisconceptions()
	{	if(interactiePanel!=null && interactiePanel instanceof AntwoordFormuleVak)
			return ((AntwoordFormuleVak)interactiePanel).getPossibleMisconceptions();
		if(interactiePanel!=null && interactiePanel instanceof SimpelAntwoordFormuleVak)
			return ((SimpelAntwoordFormuleVak)interactiePanel).getPossibleMisconceptions();
		if(interactiePanel!=null && interactiePanel instanceof CheckUnitPanel)
			return ((CheckUnitPanel)interactiePanel).getPossibleMisconceptions();
		if(interactiePanel!=null && interactiePanel instanceof TekstVakPanel)
			return ((TekstVakPanel)interactiePanel).getPossibleMisconceptions();
		return null;
	}
	
	
	public int getScoreMax()
	{	if(launchData==null)return 0;
		int scoreMax = 0;
		Hashtable interactiePanelLaunchState = null;
	    if(launchData.containsKey("interactiePanelLaunchState")) interactiePanelLaunchState = (Hashtable)launchData.get("interactiePanelLaunchState");
	    if(interactiePanelLaunchState.containsKey("scoreMax")) scoreMax = ((Integer)interactiePanelLaunchState.get("scoreMax")).intValue();
	    return scoreMax;
	}
	
	public int[][] getScoreMaxObjectives()
	{	if(launchData==null || WiskOpdr.objectives==null)return null;
		int[][] scoreMaxObjectives = null;
		Hashtable interactiePanelLaunchState = null;
	    if(launchData.containsKey("interactiePanelLaunchState")) interactiePanelLaunchState = (Hashtable)launchData.get("interactiePanelLaunchState");
	    if(interactiePanelLaunchState.containsKey("scoreMaxObjectives")) scoreMaxObjectives = (int[][])interactiePanelLaunchState.get("scoreMaxObjectives");
// default implementation of "scoreMaxObjectives"
	    else if(interactiePanelLaunchState.containsKey("logObjectives")) {
	    	boolean[][] logObjectives = OpdrNavStruct.toBooleanArrayArray(interactiePanelLaunchState.get("logObjectives"));
			if(logObjectives!=null)
			{	
				int scoreMax = getScoreMax();
				scoreMaxObjectives = new int[logObjectives.length][];
				for(int j=0 ; j<scoreMaxObjectives.length; j++)
				{	scoreMaxObjectives[j] = new int[logObjectives[j].length];
					for(int i=0 ; i<scoreMaxObjectives[j].length ; i++)
					{	if(logObjectives[j][i]) scoreMaxObjectives[j][i] = scoreMax;
					}
				}
			}
 
	    }
	    
	    
	    
	    
	    return scoreMaxObjectives;
	}

	public boolean isCorrect()
	{	if(interactiePanel!=null) return interactiePanel.isCorrect();
		return true;
	}
	
	public boolean isFout()
	{	if(interactiePanel!=null) return interactiePanel.isFout();
		return false;
	}
    
    public void stop()
    {   if(interactiePanel!=null)interactiePanel.stop();
    	if(editInteractiePanelDialog != null)
    	{
    		editInteractiePanelDialog.dispose();
    		editInteractiePanelDialog = null;
    	}
    	closePopup();
    }
    
    public void closePopup()
    {   if(interactiePanel!=null && interactiePanel instanceof TekstVakPanel) 
		{	((TekstVakPanel)interactiePanel).closePopup();
		}
    	if(popupFrame != null)
    	{   popupFrame.setVisible(false);
    	    popupFrame.dispose();
    	    popupFrame = null;
    	}
    
    }
    
    public void start()
    {   if(interactiePanel!=null)interactiePanel.start();
    }
    
    public void destroy()
    {   if(interactiePanel!=null)interactiePanel.destroy();
    	try {
			XWidgetManager manager = tekstVak.getXWidgetManager();
			manager.remove(this);
		} catch (IllegalArgumentException e) {
			// without manager nothing to remove.
		}
        
    }
    
    public void opnieuw()
    {   if(interactiePanel!=null)interactiePanel.opnieuw();
    }
    
    public void kijkNa()
    {   if(interactiePanel!=null) interactiePanel.kijkNa();
   
    }
    
    public void kijkNa(int stapNr)
    {    if(interactiePanel!=null) interactiePanel.kijkNa(stapNr);
   		//System.out.println("cbookevent");
    }
	
	public void vulVak(String s)
	{	//currentSetNr = Integer.parseInt(s);
		if(s!=null && !s.equals(""))
		{	Object o = StringCodeObject.decodeStringToObject(s);
			if(o==null)return;
			launchData = (Hashtable)o;
			setEditState(launchData);
			if(!editMode || studentEditor)remove(afdekPanel);
		}
		
	}
	
	
	public void setEditable(boolean b)
	{	
	}
	
	public void setSelected(boolean b)
	{	selected = b;
	}
	
	public boolean isSelected()
	{	return selected;
	}
	
	public void zetMaat()
	{	if(interactiePanel!=null)
		{	if(!popup)setSize(((Component)interactiePanel).getSize().width, ((Component)interactiePanel).getSize().height);
			else if("MW".equals(WiskOpdr.deployVariant) 
					&& (interactiePanel instanceof GrafiekPanel || interactiePanel instanceof GeogebraPanel))setSize(50,50);
			else setSize(popupJButton.getSize());
			((Component)interactiePanel).setLocation(0,0);
			if(interactiePanel instanceof SimpelAntwoordFormuleVak)ashoogte = ((SimpelAntwoordFormuleVak)interactiePanel).geefAsHoogte()+2;
			else if(!popup && interactiePanel instanceof SimpelAntwoordVergelijkingVak)ashoogte = ((SimpelAntwoordVergelijkingVak)interactiePanel).geefAsHoogte()+2;
            else if(!popup && interactiePanel instanceof AntwoordTekstVak)ashoogte = ((AntwoordTekstVak)interactiePanel).geefAsHoogte();
            else if(!popup && interactiePanel instanceof TekstVakPanel)ashoogte = ((TekstVakPanel)interactiePanel).geefAsHoogte();
            else if(!popup && interactiePanel instanceof AntwoordFormuleVak)ashoogte = ((AntwoordFormuleVak)interactiePanel).geefAsHoogte();
            else if(!popup && interactiePanel instanceof CheckValueUnitPanel)ashoogte = ((CheckValueUnitPanel)interactiePanel).geefAsHoogte();
            else if(!popup && interactiePanel instanceof CheckUnitPanel)ashoogte = ((CheckUnitPanel)interactiePanel).geefAsHoogte();
            else if(!popup && interactiePanel instanceof CheckSleepUnitPanel)ashoogte = ((CheckSleepUnitPanel)interactiePanel).geefAsHoogte();
            else if(!popup && interactiePanel instanceof CheckButtonPanel)ashoogte = ((CheckButtonPanel)interactiePanel).geefAsHoogte();
            
            else ashoogte = 15;
		}
		
		if(getParent()instanceof FormuleElement)((FormuleElement)getParent()).zetMaat();
		if(getParent()instanceof TekstElement)((TekstElement)getParent()).zetMaat();
		//if(getParent()!=null && getParent().getParent()instanceof TekstVak)((TekstVak)getParent().getParent()).layoutTekst();
	}
	
	public String toString()
	{	return "$V@";// + antwoordVak.getText() + "@";
	}
	
	public String toCompleteString()
	{	String s = "";
		if(launchData!=null) 
		{	launchData = getEditState();
			s = StringCodeObject.encodeObjectToString(launchData);
		}
		return "$V" + s + "@";
		
	}
	
	public boolean zetFocus()
	{
		if(interactiePanel instanceof SimpelAntwoordFormuleVak) 
		{	((SimpelAntwoordFormuleVak)interactiePanel).geefFormuleVak().requestFocus();
			return true;
		}
		else if(interactiePanel instanceof SimpelAntwoordVergelijkingVak) 
		{	((SimpelAntwoordVergelijkingVak)interactiePanel).geefFormuleVak().requestFocus();
			return true;
		}
		else if(interactiePanel instanceof AntwoordFormuleVak) 
		{	((AntwoordFormuleVak)interactiePanel).geefFormuleVak().requestFocus();
			return true;
		}
		else if(interactiePanel instanceof AntwoordVergelijkingVak) 
		{	((AntwoordVergelijkingVak)interactiePanel).geefFormuleVak().requestFocus();
			return true;
		}
		else if(interactiePanel instanceof TekstVakPanel) 
		{	return ((TekstVakPanel)interactiePanel).zetFocus();
		}
		else return false;
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getActionCommand().equals("tekst"))
		{	produceThisAction(e);
			return;
		}
		if(e.getActionCommand().equals("formule"))
		{	produceThisAction(e);
			return;
		}
		if(e.getActionCommand().equals("balansvergelijking"))
		{	return;
		}
		if(e.getActionCommand().equals("balansvergelijkinginit"))
		{	return;
		}
		if(e.getSource()==popupJButton)
		{	//if(editMode)return;
			if(popupFrame==null) maakPopupFrame();
			else
			{	popupFrame.setVisible(true);
				return;
			}
			popupFrame.setVisible(true);
			int width = ((Component)interactiePanel).getSize().width + popupFrame.getInsets().left + popupFrame.getInsets().right;
			int height = ((Component)interactiePanel).getSize().height + popupFrame.getInsets().top + popupFrame.getInsets().bottom;
			
			if(interactiePanel instanceof TekstVakPanel)
			{	if(height>600)width += 20;
				height = Math.min(600, height);
				((TekstVakPanel)interactiePanel).setPopupUsed(true);
			}
			popupFrame.setSize(width,height);
			
			Dimension screenSize = WiskOpdr.applet.getToolkit().getScreenSize();
			int x = getLocationOnScreen().x + Math.min(0,screenSize.width - (getLocationOnScreen().x + width));
			int y = getLocationOnScreen().y + Math.min(0,screenSize.height - (getLocationOnScreen().y + height));
			popupFrame.setLocation(x,y);
			
			interactiePanel.start();
			return;
		}
		if(e.getActionCommand().equals("ok"))
		{	editInteractiePanelDialog.stop();
			launchData = editInteractiePanelDialog.getEditState();
			editInteractiePanelDialog.removeActionListener(this);
			
			if(launchData !=null)
			{	if(interactiePanel!=null)remove((Component)interactiePanel);
				setEditState(launchData);
			}
			editInteractiePanelDialog.dispose();
			editInteractiePanelDialog = null;
			if(launchData ==null || interactiePanel==null)
			{	setSelected(true);
				((TekstVak)(getParent().getParent())).deleteSelection();
			}
			
			return;
		}
		if(e.getActionCommand().equals("cancel"))
		{
			editInteractiePanelDialog.stop();
			editInteractiePanelDialog.removeActionListener(this);
			editInteractiePanelDialog.dispose();
			editInteractiePanelDialog = null;
			if(launchData ==null || interactiePanel==null)
			{	setSelected(true);
				((TekstVak)(getParent().getParent())).deleteSelection();
			}
			
			return;
		}
		if(e.getActionCommand().equals("focus"))requestFocus();//tekstVak.zetAntwoordVak(antwoordVak);
		produceAction(e.getActionCommand());
	}
	
	public void mouseMoved(MouseEvent e)
	{
	}
	
	public void mouseDragged(MouseEvent e)
	{	if(sleepModus)
		{	int dx = e.getX() - startX;
			int dy = e.getY() - startY;
			int xNieuw = getLocation().x + dx;
			int yNieuw = getLocation().y + dy;
			if(xNieuw + getSize().width > tekstVak.getSize().width) xNieuw = tekstVak.getSize().width - getSize().width;
			if(yNieuw + getSize().height > tekstVak.getSize().height) yNieuw = tekstVak.getSize().height - getSize().height;
			if(xNieuw < 0) xNieuw = 0;
			if(yNieuw < 0) yNieuw = 0;
			setLocation(xNieuw, yNieuw);
			((TekstVakPanel)interactiePanel).zetLocatie(getLocation().x, getLocation().y);
			repaint();
			
			WiskOpdr.setLaunchDataChanged();
		}
		else if(resizeModus)
		{	int dx = e.getX() - startX;
			int dy = e.getY() - startY;
			if(((TekstVakPanel)interactiePanel).isHeightResizable()) dy = 0;
			if(((TekstVakPanel)interactiePanel).isWidthResizable()) dx = 0;
			setSize(getSize().width + dx, getSize().height + dy);
			((TekstVakPanel)interactiePanel).setTableBounds(getSize().width, getSize().height, getSize().width-dx, getSize().height-dy);
			((TekstVakPanel)interactiePanel).layoutTekst();
			zetMaat();
			repaint();
			WiskOpdr.setLaunchDataChanged();
		}
		else if(selectable && e.getSource()==afdekPanel && e.isShiftDown())
		{
			//System.out.println("Start cross W C");
			
		}
		else if(selectable && e.getSource()==afdekPanel || selectable && e.getSource()==resizePanel && e.isShiftDown())
		{	if(Math.abs(startX-e.getX())>=3 || Math.abs(startY-e.getY())>=3)
			{	//waiting = false;
				draggingToSelect = true;
			}
			if(Math.abs(startX-e.getX())<3 && Math.abs(startY-e.getY())<3)
			{	
				return;
			}
			if(getParent()instanceof TekstRegel)
			{	MouseEvent en = new MouseEvent((TekstRegel)getParent(),e.getID(),e.getWhen(),e.getModifiers(), getLocation().x,e.getY()+getLocation().y,1,false);
				MouseEvent ed = new MouseEvent((TekstRegel)getParent(),e.getID(),e.getWhen(),e.getModifiers(), e.getX()+getLocation().x,e.getY()+getLocation().y,1,false);
				
				if(eersteKeer)
				{	((TekstRegel)getParent()).mousePressed(en);
					eersteKeer=false;
				}
			}
			//waiting = false;
			if(e.getX()<0 || e.getX()>getSize().width || e.getY()<0 || e.getY()>getSize().height)
			{	//terug = false;
				
				
				if(getParent()instanceof TekstRegel)
				{	MouseEvent en = new MouseEvent((TekstRegel)getParent(),e.getID(),e.getWhen(),e.getModifiers(), getLocation().x,e.getY()+getLocation().y,1,false);
					MouseEvent ed = new MouseEvent((TekstRegel)getParent(),e.getID(),e.getWhen(),e.getModifiers(), e.getX()+getLocation().x,e.getY()+getLocation().y,1,false);
				
					if(e.getSource()==resizePanel)
					{
						en = new MouseEvent((TekstRegel)getParent(),e.getID(),e.getWhen(),e.getModifiers(), getLocation().x+getWidth(),e.getY()+getLocation().y+getHeight(),1,false);
						ed = new MouseEvent((TekstRegel)getParent(),e.getID(),e.getWhen(),e.getModifiers(), e.getX()+getLocation().x+getWidth(),e.getY()+getLocation().y+getHeight(),1,false);
					
					}
					if(eersteKeer)
					{	((TekstRegel)getParent()).mousePressed(en);
						eersteKeer=false;
					}
					((TekstRegel)getParent()).mouseDragged(ed);
					//((FormuleElement)getParent().getParent()).requestFocus();
					//formuleVak.zetActieveRegel((FormuleRegel)getParent().getParent());
					//((FormuleRegel)getParent().getParent()).setSelection(((FormuleElement)getParent()).getLocation().x, ((FormuleElement)getParent()).getLocation().x+1);
				}
				
			}
			else
			{	setSelected(true);
				
				
			}
		}
		
	}
	
	/*public void paintComponent(Graphics g)
	{
		
		if(selected) 
		{	g.setColor(new Color(0,0,0,128));
			g.fillRect(0,0,getWidth(), getHeight());
		
		}
		else super.paintComponent(g);
	}*/
	
	public void mousePressed(MouseEvent e)
	{	
		connected = false;
		if(interactiePanel instanceof TekstVakPanel)
		{	if(editInteractiePanelDialog==null && (e.getSource()==afdekPanel || e.getSource()==resizePanel) && (e.getModifiers()== InputEvent.BUTTON3_MASK || e.isControlDown()))
			{
				launchData = getEditState();
// TODO omzetten naar DialogFacade				
				Component window = WiskOpdr.getWindowForComponent(this);
				if(window instanceof Frame)editInteractiePanelDialog = new EditInteractiePanelDialog((Frame)window, "", false, currentSetNr, launchData, getXWidgetManager());
				if(window instanceof Dialog)editInteractiePanelDialog = new EditInteractiePanelDialog((Dialog)window, "", false, currentSetNr, launchData , getXWidgetManager());
				
				//editInteractiePanelDialog = new EditInteractiePanelDialog(WiskOpdr.getFrame(), "", false, currentSetNr, launchData);
				editInteractiePanelDialog.setBackground(WiskOpdr.bgcolor);
				editInteractiePanelDialog.addActionListener(this);
				editInteractiePanelDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				interactiePanel.start();
				showDialog(true);
			}
			else  if((e.getSource()==afdekPanel || e.getSource()==resizePanel && e.isShiftDown()) &&((TekstVakPanel)interactiePanel).isZwevend() &&  !getBasisTekstVak().crossWidgetViewActief())
			{
				sleepModus = true;
				if(anchor!=null)
				{	anchor.setChar('\u25cf');
					anchor.repaint();
				}
				startX = e.getX();
				startY = e.getY();
			}
			else if(e.getSource()==resizePanel  && !e.isShiftDown())
			{	resizeModus = true;
				startX = e.getX();
				startY = e.getY();
			}
			else if(e.getSource()==resizePanel  && e.isShiftDown())
			{	
				if(selectable)
				{	tekstVak.zetTekstFocus();
					startX = e.getX();
					startY = e.getY();
					
				}
			}
			else  if(e.getSource()==sleepPanel &&((TekstVakPanel)interactiePanel).isIpSleepbaar() &&((TekstVakPanel)interactiePanel).isIpSleepbaar()&&  !getBasisTekstVak().crossWidgetViewActief())
			{	//if(getParent()instanceof TekstVak)
				//	((TekstVak)getParent()).add(this,0);
				if(getParent()instanceof TekstVak)
					getParent().setComponentZOrder(this, 0);
				
				((TekstVakPanel)interactiePanel).startDrag();
				sleepModus = true;
				startX = e.getX();
				startY = e.getY();
				((TekstVakPanel)interactiePanel).requestFocus();
			}
			else  if(e.getSource()==afdekPanel)
			{
				if(e.isShiftDown() && getBasisTekstVak().crossWidgetViewActief())
				{	if(selectable && interactiePanel instanceof CBookAware)
					{	TekstInteractiePanelVak.potentialSource = this;
						//System.out.println("potentialSource = this;");
						setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
					}
				}
				
				if(selectable)
				{	tekstVak.zetTekstFocus();
					startX = e.getX();
					startY = e.getY();
					
				}
			}
			
		}
		else  if(e.getSource()==afdekPanel && e.isShiftDown() && getBasisTekstVak().crossWidgetViewActief())
		{	if(selectable && interactiePanel instanceof CBookAware)
			{	TekstInteractiePanelVak.potentialSource = this;
				//System.out.println("potentialSource = this;");
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
		}
		else  if(e.getSource()==afdekPanel)
		{	if(selectable)
			{	tekstVak.zetTekstFocus();
				startX = e.getX();
				startY = e.getY();
				
			}
		}
	}
/**
 * Laat het editInteractionPanel zien 
 * @param modal true: de methode blokkeert.
 */
	public void showDialog(boolean modal) {
		if(editInteractiePanelDialog != null) {
			editInteractiePanelDialog.setVisible(false);
			editInteractiePanelDialog.setModal(modal);
			editInteractiePanelDialog.toFront(); 		// FIXME orderening (met Events)
			editInteractiePanelDialog.requestFocus(); 
			editInteractiePanelDialog.setVisible(true); // BLOKKEERT als 'modal' true
		}
	}
	
	public void mouseClicked(MouseEvent e){;}
	
	public void mouseReleased(MouseEvent e)
	{	
		setCursor(Cursor.getDefaultCursor());
		
		eersteKeer = true;
		if(e.getSource()==afdekPanel && draggingToSelect || e.getSource()==resizePanel && draggingToSelect)
		{	draggingToSelect = false;
			repaint();
			return;
		}
		if(interactiePanel instanceof TekstVakPanel && ((TekstVakPanel)interactiePanel).isIpSleepbaar())
		{	Point[] doelPosities = ((TekstVakPanel)interactiePanel).geefSleepDoelPosities();
			int marge = ((TekstVakPanel)interactiePanel).geefSleepdoelMarge();
			boolean snap = ((TekstVakPanel)interactiePanel).geefSleepSnap();
			if(doelPosities != null) 
			{	boolean snapped = false;
				for(int i=0 ; i<doelPosities.length ; i++)
				{	
					if(doelPosities[i]==null) {
			    		JOptionPane.showMessageDialog(this, "Sleep-unit fout.\nNiet alle doelobjecten zijn aanwezig.\nDoelobject met ID="+(-(i+1))+" kan niet gevonden worden.");
			    		break;
			    	}
					int dx = Math.abs(getLocation().x - doelPosities[i].x);
					int dy = Math.abs(getLocation().y - doelPosities[i].y);
					//if(snap && dx*dx+dy*dy < marge*marge)
					if(snap && dx < marge && dy < marge) 
					{	setLocation(doelPosities[i].x, doelPosities[i].y);
						((TekstVakPanel)interactiePanel).zetLocatie(getLocation().x, getLocation().y);
						repaint();
						snapped = true;
						break;
					}
				}
				if(!snapped && ((TekstVakPanel)interactiePanel).getRelocate())
				{	Point p = ((TekstVakPanel)interactiePanel).getStartSleep();
					setLocation(p.x, p.y);
					((TekstVakPanel)interactiePanel).zetLocatie(getLocation().x, getLocation().y);
					repaint();
				}
			}
		}
		// inhoud die op 'volle breedte' is ingesteld wordt aangepast
		if(editMode && interactiePanel instanceof TekstVakPanel)setEditState(getEditState());
		
		sleepModus = false;
		if(anchor!=null)
		{	anchor.setChar('\u25cb');
			anchor.repaint();
		}
		resizeModus = false;
		
		
		if(!editMode || studentEditor)return;
	
		if(interactiePanel instanceof TekstVakPanel)
		{	
			if(editInteractiePanelDialog==null && connected && potentialSource != null && potentialDest != null)
			{
				doConnect();
			} 
			else
			{ 	((TekstVakPanel)interactiePanel).zetLocatie(getLocation().x, getLocation().y);
				if(getParent()!=null && getParent().getParent()instanceof TekstVak)((TekstVak)getParent().getParent()).layoutTekst();
				
			}
			return;
		}
		
		launchData = getEditState();
// delay doConnect to mouseReleased
		if(editInteractiePanelDialog==null && connected && potentialSource != null && potentialDest != null)
		{
			doConnect();
		} else
		
		if(editInteractiePanelDialog==null && !connected){
// TODO omzetten naar DialogFacade	
			
			
			Component window = WiskOpdr.getWindowForComponent(this);
			if(soortInteractiePanel==25)currentSetNr = 0;
			if(window instanceof Frame)editInteractiePanelDialog = new EditInteractiePanelDialog((Frame)window, "", false, currentSetNr, launchData, getXWidgetManager());
			if(window instanceof Dialog)editInteractiePanelDialog = new EditInteractiePanelDialog((Dialog)window, "", false, currentSetNr, launchData, getXWidgetManager());
			
			//editInteractiePanelDialog = new EditInteractiePanelDialog(WiskOpdr.getFrame(), "", false, currentSetNr, launchData);
			editInteractiePanelDialog.setBackground(WiskOpdr.bgcolor);
			editInteractiePanelDialog.addActionListener(this);
			editInteractiePanelDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			showDialog(true);

		}
	}
	public void mouseEntered(MouseEvent e){
		if(selectable && e.getSource()==afdekPanel && e.isShiftDown()  && getBasisTekstVak().crossWidgetViewActief())
		{	//System.out.println("potentialSource != null "+(potentialSource!=null));
			//System.out.println("potentialSource != this "+(potentialSource != this));
			if(potentialSource != null && potentialSource != this  && interactiePanel instanceof CBookAware)
			{
				potentialDest = this;
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				potentialDest.connected = true;
				potentialSource.connected = true;
				//System.out.println("connect enter");
				return;
			}
		}
	}

	private void doConnect() {
		String[] sendCmds = potentialSource.getSendCmds();
		String[] acceptedCmds = potentialDest.getAcceptedCmds();
		if(sendCmds != null && acceptedCmds != null)
		{	Set<String> srcSet = new TreeSet<String>(Arrays.asList(sendCmds));
			Set<String> destSet = new TreeSet<String>(Arrays.asList(acceptedCmds));
			TreeSet<Connector> result = new TreeSet<Connector>();
			//destSet.retainAll(srcSet);
			for( String s: srcSet) for(String d: destSet) {
				int dot;
				dot = s.indexOf('.');
				String ps = dot >=0 ? s.substring(0,dot) : s; // prefix match "input.XXX" matches "input" or "input.YYY"
				dot = d.indexOf('.');
				String pd = dot >=0 ? d.substring(0,dot) : d;
				if(pd.equals(ps))
					result.add(new Connector(s,d, potentialSource, potentialDest));
			}
			if(!destSet.isEmpty())
			{	potentialSource.connect(potentialDest, result);
			} else
				noConnectionPossible();
		} else {
			noConnectionPossible();
		}
		//System.out.println("connect");
		potentialSource = null;
		connected = false;
	}
	
	/**
	 * waarschuwing als geen verbinding mogelijk is.
	 * TODO internationalisatie.
	 */
	private void noConnectionPossible() {
		JOptionPane.showMessageDialog(this, "Not possible", "Command", JOptionPane.WARNING_MESSAGE);
	}

	public void mouseExited(MouseEvent e){
		if(connected && selectable && e.getSource()==afdekPanel && e.isShiftDown()  && getBasisTekstVak().crossWidgetViewActief())
		{	//System.out.println("potentialSource != null "+(potentialSource!=null));
			//System.out.println("potentialSource != this "+(potentialSource != this));
			if(potentialDest == this && potentialSource != null && potentialSource != this  && interactiePanel instanceof CBookAware)
			{
				connected = false;
				setCursor(Cursor.getDefaultCursor());
				potentialDest = null;
				potentialSource.connected = false;
				//System.out.println("connect exit");
				return;
			}
		}
		
		
		
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
 	
 	public void produceThisAction(ActionEvent e)
 	{	if (actionListener != null)
 		{	actionListener.actionPerformed(e);
 		}
 	}
 	//end ActionProducer

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyChar()==KeyEvent.VK_SHIFT)
		{
			if (potentialSource!=null && potentialDest!=null)
			{	//TekstInteractiePanelVak.potentialSource.connect(this, new TreeSet<String>());
				//potentialSource = null;
			}
		}
		
	}
}

