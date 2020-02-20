/*
 * File:	IVMdrawGWT.java
 *
 * This file can be seen as a wrapper for the IVMdrawGWTField file.
 * It defines the entrypoint for the widget while also allow cross-widget communication.
 */

package fi.ivmdrawgwt.client;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchMoveHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.*;
import com.vaadin.pointerevents.client.PointerDownEvent;
import com.vaadin.pointerevents.client.PointerDownHandler;
import com.vaadin.pointerevents.client.PointerMoveEvent;
import com.vaadin.pointerevents.client.PointerMoveHandler;
import com.vaadin.pointerevents.client.PointerUpEvent;
import com.vaadin.pointerevents.client.PointerUpHandler;

import fi.ivmdrawgwt.client.IVMdrawGWTField.MGWTTouchHandler;
import fi.ivmdrawgwt.client.IVMdrawGWTField.MouseHandler;
import fi.ivmdrawgwt.client.IVMdrawGWTField.PointerHandler;
import fi.ivmdrawgwt.client.text.Text;
import nl.uu.fi.dwo.interaction.client.InteractionStub;
import nl.uu.fi.dwo.interaction.client.JSONUtilities;
import nl.uu.fi.dwo.interaction.client.OpdrNavIF;
import nl.uu.fi.dwo.interaction.client.Stub;
import nl.uu.fi.dwo.interaction.client.event.CBookEvent;
import nl.uu.fi.dwo.interaction.client.event.CBookEventListener;
import nl.uu.fi.dwo.interaction.client.json.ObjectMap;


public class IVMdrawGWT extends Composite implements EntryPoint, InteractionStub, CBookEventListener {
	private static Logger logger = Logger.getLogger("IVMdrawGWTField");
	
	private static final String upgradeMessage = "Your browser does not support the HTML5 Canvas. Please upgrade your browser to view this demo.";
	static final Text rb = GWT.create(Text.class);
		
	static int breedte = 700;
	static int hoogte = 500;

	private IVMdrawGWTClientBundle ivmDrawGWTClientBundle;
	private static IVMdrawCssResource ivmDrawCss;

	private DockLayoutPanel dlp;
	private LayoutPanel topPanel;

	private IVMdrawGWTField ivmDrawGWTField;
	
	private OpdrNavIF comRoot;
	private int vaasNummer = 0;
	private boolean jarFeedbackVisible = false;
	private boolean feedbackVisible = false;
	private boolean goedFoutVisible = true;
	private boolean historyVisible = false;
	private int scoreMax = 0;
	private int deelScore1 = 0;
	private int deelScore2 = 0;
	private int attemptsToCorrect = 999;
	
	static String feedbackCase1 = IVMdrawGWT.rb.defaultFeedbackCase1();
	static String feedbackCase2a = IVMdrawGWT.rb.defaultFeedbackCase2a();
	static String feedbackCase2b = IVMdrawGWT.rb.defaultFeedbackCase2b();
	static String feedbackCase3a = IVMdrawGWT.rb.defaultFeedbackCase3a();
	static String feedbackCase3b = IVMdrawGWT.rb.defaultFeedbackCase3b();
	static String feedbackCase4a = IVMdrawGWT.rb.defaultFeedbackCase4a();
	static String feedbackCase4b = IVMdrawGWT.rb.defaultFeedbackCase4b();
	static String feedbackCase5 = IVMdrawGWT.rb.defaultFeedbackCase5();
	static String feedbackCase6 = IVMdrawGWT.rb.defaultFeedbackCase6();
	static String feedbackCase7 = IVMdrawGWT.rb.defaultFeedbackCase7();
	
	private boolean correctGraph = false;
	private int mode = 0;
	private boolean check;
	
	private ListBox historyList;
	private PopupPanel feedbackPanel = new PopupPanel(true);
	private LayoutPanel feedbackTekst = new LayoutPanel();
	
	Image goedkrulImage;
	Image foutkruisImage;
	
	ImageElement binImageElement;
	ImageElement feedbackImageElement;
	ImageElement goedkrulImageElement;
	ImageElement foutkruisImageElement;

	/**
	 * The GWT entry point method, called automatically by loading a module that declares an implementing class as an
	 * entry point. Source: http://www.gwtproject.org/javadoc/latest/com/google/gwt/core/client/EntryPoint.html
	 *
	 * For local testing and debugging, use IVMdrawGWTDebug's onModuleLoad.
	 */
	public void onModuleLoad() {
		getImages();
		
		dlp = new DockLayoutPanel(Style.Unit.PX);
		dlp.addStyleName(ivmDrawCss.dock());
		dlp.setPixelSize(breedte,hoogte);

		RootLayoutPanel.get().add(dlp);
		RootLayoutPanel.get().addStyleName(ivmDrawCss.root());

		Stub.publish(this);
	}

	public void getImages() {
		ivmDrawGWTClientBundle = GWT.create(IVMdrawGWTClientBundle.class);
		ivmDrawCss = ivmDrawGWTClientBundle.getKladjeGWTCSS();
		ivmDrawCss.ensureInjected();
		
		ImageResource goedkrulResource = ivmDrawGWTClientBundle.goedvinkResource();
		ImageResource foutkruisResource = ivmDrawGWTClientBundle.foutkruisResource();
		ImageResource binResource = ivmDrawGWTClientBundle.binResource();
		ImageResource feedbackResource = ivmDrawGWTClientBundle.feedbackResource();
		
		goedkrulImage = new Image(goedkrulResource);
		foutkruisImage = new Image(foutkruisResource);
		
		Image binImage = new Image(binResource);
		Image feedbackImage = new Image(feedbackResource);
		
		goedkrulImageElement = ImageElement.as(goedkrulImage.getElement());
		foutkruisImageElement = ImageElement.as(foutkruisImage.getElement());
		binImageElement = ImageElement.as(binImage.getElement());
		feedbackImageElement = ImageElement.as(feedbackImage.getElement());
	}

	/**
	 * Constructor for the IVMdrawGWT object.
	 * @param map Launchdata
	 * @param randomVarNamen TODO: not sure
	 * @param randomVarWaarden TODO: not sure
	 */
	public IVMdrawGWT(HashMap<String, Object> map, String[] randomVarNamen, HashMap<String, Number> randomVarWaarden) {
		ObjectMap h = JSONUtilities.wrapMap(map);
		Map<String,Object> launchData = new HashMap<String,Object>();
		
		if (h.containsKey("breedte"))
			breedte = h.getInt("breedte");
		if (h.containsKey("hoogte"))
			hoogte = h.getInt("hoogte");
		if (h.containsKey("interactiePanelLaunchState"))
			launchData = h.getMap("interactiePanelLaunchState");
		
		getImages();

		dlp = new DockLayoutPanel(Style.Unit.PX);
		dlp.addStyleName(ivmDrawCss.dock());
		dlp.setPixelSize(breedte,hoogte);
		
		RootPanel.get().add(dlp);
		
		init(breedte, hoogte, launchData, randomVarWaarden);
	}

	public IVMdrawGWT() {
		initWidget(new Label("IVMdraw"));
	}
	
	@Override
	public void init(int width, int height, Map<String, Object> launchData,	Map<String, Number> values) {
		
		goedkrulImage.setVisible(false);
		foutkruisImage.setVisible(false);
		
		breedte = width;
		hoogte = height;
		
		dlp.setSize("" + breedte + "px", "" + hoogte + "px");

		topPanel = new LayoutPanel();
		dlp.addNorth(topPanel, 24);
		
		historyList = new ListBox();
		historyList.setVisible(false);
		
		topPanel.add(historyList);
		topPanel.setWidgetLeftWidth(historyList.asWidget(), 30, Style.Unit.PX, 160, Style.Unit.PX);
		
		int veldhoogte = hoogte - 24;
		
		ivmDrawGWTField = new IVMdrawGWTField(breedte,veldhoogte, this);

		Canvas ivmDrawGWTCanvas = ivmDrawGWTField.getCanvas();
		if (ivmDrawGWTCanvas == null) {
	      RootLayoutPanel.get().add(new Label(upgradeMessage));
	      return;
		}
	    ivmDrawGWTCanvas.addStyleName(ivmDrawCss.canvas());
		ivmDrawGWTField.initContext2d();
		dlp.add(ivmDrawGWTCanvas);
		
		ivmDrawGWTField.setState(launchData);
		
		ObjectMap launchState = JSONUtilities.wrapMap(launchData);
		if(launchState.containsKey("vaasNummer")) 
			vaasNummer = launchState.getInt("vaasNummer");
		if(launchState.containsKey("check")) 
			check = launchState.getBoolean("check");
		if(launchState.containsKey("feedbackVisible")) 
			feedbackVisible = launchState.getBoolean("feedbackVisible");
		if(launchState.containsKey("goedFoutVisible")) 
			goedFoutVisible = launchState.getBoolean("goedFoutVisible");
		if(launchState.containsKey("historyVisible")) 
			historyVisible = launchState.getBoolean("historyVisible");
		if(launchState.containsKey("scoreMax")) 
			scoreMax = launchState.getInt("scoreMax");
		if(launchState.containsKey("deelScore1")) 
			deelScore1 = launchState.getInt("deelScore1");
		if(launchState.containsKey("deelScore2")) 
			deelScore2 = launchState.getInt("deelScore2");
		if(launchState.containsKey("feedbackCase1")) 
			feedbackCase1 = (String)launchState.get("feedbackCase1");
		if(launchState.containsKey("feedbackCase2a")) 
			feedbackCase2a = (String)launchState.get("feedbackCase2a");
		if(launchState.containsKey("feedbackCase2b")) 
			feedbackCase2b = (String)launchState.get("feedbackCase2b");
		if(launchState.containsKey("feedbackCase3a")) 
			feedbackCase3a = (String)launchState.get("feedbackCase3a");
		if(launchState.containsKey("feedbackCase3b")) 
			feedbackCase3b = (String)launchState.get("feedbackCase3b");
		if(launchState.containsKey("feedbackCase4a")) 
			feedbackCase4a = (String)launchState.get("feedbackCase4a");
		if(launchState.containsKey("feedbackCase4b")) 
			feedbackCase4b = (String)launchState.get("feedbackCase4b");
		if(launchState.containsKey("feedbackCase5")) 
			feedbackCase5 = (String)launchState.get("feedbackCase6");
		if(launchState.containsKey("feedbackCase6")) 
			feedbackCase6 = (String)launchState.get("feedbackCase6");
		if(launchState.containsKey("feedbackCase7")) 
			feedbackCase7 = (String)launchState.get("feedbackCase7");
		
		boolean jarFeedbackSelected = true;
		if(launchState.containsKey("jarFeedbackVisible")) 
			jarFeedbackSelected = launchState.getBoolean("jarFeedbackVisible");
		jarFeedbackVisible = jarFeedbackSelected && vaasNummer!=0;
		
		check = check || feedbackVisible; // voor backwards comp
		
		this.ivmDrawGWTField.setCorrectVaasNummer(vaasNummer);
		historyList.setVisible(historyVisible);
		//ivmDrawGWTField.paint();
		
		feedbackTekst.getElement().setInnerText("");
		feedbackTekst.getElement().getStyle().setColor(""+CssColor.make(49,71,112));
		
		Label closeButton = new Label("×");
		closeButton.getElement().getStyle().setColor(""+CssColor.make(49,71,112));
		closeButton.getElement().getStyle().setTextAlign(TextAlign.RIGHT);
		closeButton.getElement().getStyle().setFontSize(20, Unit.PX);
		closeButton.getElement().getStyle().setMarginTop(-6, Unit.PX);
		
		VerticalPanel vp = new VerticalPanel();
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(goedkrulImage);	
		hp.add(foutkruisImage);
		hp.add(closeButton);
		hp.setWidth("100%");
		vp.add(hp);
		vp.add(feedbackTekst);
		
		feedbackPanel.getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
		feedbackPanel.getElement().getStyle().setBorderColor(""+CssColor.make(38,115,182));
		feedbackPanel.getElement().getStyle().setBorderWidth(1, Style.Unit.PX);
		feedbackPanel.getElement().getStyle().setPadding(5, Style.Unit.PX);
		feedbackPanel.getElement().getStyle().setBackgroundColor(""+CssColor.make(239,241,243));
		feedbackPanel.getElement().getStyle().setProperty("boxShadow", "3px 3px 3px #96A1BD");
		feedbackPanel.add(vp);
		feedbackPanel.setAutoHideEnabled(false);
		feedbackPanel.setWidth("180px");
		
		MousePopupHandler mousePopupHandler = new MousePopupHandler();
		feedbackPanel.addDomHandler((MouseMoveHandler)mousePopupHandler, MouseMoveEvent.getType()); 
		feedbackPanel.addDomHandler((MouseDownHandler)mousePopupHandler, MouseDownEvent.getType()); 
		feedbackPanel.addDomHandler((MouseUpHandler)mousePopupHandler, MouseUpEvent.getType()); 
		
		TouchPopupHandler touchPopupHandler = new TouchPopupHandler();
		feedbackPanel.addDomHandler((TouchMoveHandler)touchPopupHandler, TouchMoveEvent.getType()); 
		feedbackPanel.addDomHandler((TouchStartHandler)touchPopupHandler,TouchStartEvent.getType()); 
		feedbackPanel.addDomHandler((TouchEndHandler)touchPopupHandler, TouchEndEvent.getType()); 
		
		PointerPopupHandler pointerPopupHandler = new PointerPopupHandler();
		feedbackPanel.addDomHandler((PointerMoveHandler)pointerPopupHandler, PointerMoveEvent.getType()); 
		feedbackPanel.addDomHandler((PointerUpHandler)pointerPopupHandler, PointerUpEvent.getType()); 
		feedbackPanel.addDomHandler((PointerDownHandler)pointerPopupHandler, PointerDownEvent.getType()); 
		
		dlp.addAttachHandler(new AttachEvent.Handler() {
			@Override
			public void onAttachOrDetach(AttachEvent event) {
				if(feedbackPanel!=null)
					feedbackPanel.hide();
			}
		});
	}
	
	public void setFeedback(String feedback) {
		feedbackTekst.getElement().setInnerText(feedback);
		feedbackPanel.setPopupPosition(ivmDrawGWTField.getCanvas().getAbsoluteLeft()+50, ivmDrawGWTField.getCanvas().getAbsoluteTop()+30);
		if(feedbackVisible) {
			logger.info("setFeedback");
			if(goedFoutVisible) {
				goedkrulImage.setVisible(correctGraph);
				foutkruisImage.setVisible(!correctGraph);
			}
			else {
				goedkrulImage.setVisible(false);
				foutkruisImage.setVisible(false);
			}
			feedbackPanel.show();
			feedbackPanel.setVisible(true);
		}
		setChanged();
	}
	
	public void closeFeedback() {
		logger.info("closeFeedback");
		feedbackPanel.hide();
	}
	
	public Widget asWidget() {
		return dlp; 
	}
	
	public boolean getFeedbackVisible() {
		return feedbackVisible;
	}
	
	public boolean getJarFeedbackVisible() {
		return jarFeedbackVisible;
	}
	
	public boolean getGoedFoutVisible() {
		return goedFoutVisible;
	}
	
	public boolean getHistoryVisible() {
		return historyVisible;
	}
	
	public ListBox getHistoryListBox() {
		return historyList;
	}
	
	public boolean getCheck() {
		return check;
	}
	
	public boolean getCorrectGraph() {
		return correctGraph;
	}
	
	public boolean setCorrectGraph(boolean b) {
		return correctGraph = b;
	}

	@Override
	public HashMap<String, Object> getState() {
		HashMap h = ivmDrawGWTField.getState();
		
		h.put("attemptsToCorrect", attemptsToCorrect);
		
		return h;
	}

	@Override
	public void setState(HashMap<String, Object> h) {
		if(h == null||h.isEmpty()) {
			ivmDrawGWTField.paint();
			return;
		}
		
		ivmDrawGWTField.setState(h);
		
		ObjectMap launchState = JSONUtilities.wrapMap(h);
		
		if(launchState.containsKey("attemptsToCorrect"))
			attemptsToCorrect = launchState.getInt("attemptsToCorrect");
	}

	@Override
	public int getScore() {
		int score = 0;
		if(attemptsToCorrect<3)
			score = scoreMax;
		else if(attemptsToCorrect<999 && attemptsToCorrect>2)
			score = deelScore1;
		else if(ivmDrawGWTField.getAttemptCount()>2)
			score = deelScore2;
		return score;
	}

	@Override
	public int[][] getScoreObjectives() {
		return null;
	}

	@Override
	public Boolean isCorrect() {
		return correctGraph;
	}

	@Override
	public void kijkNa() {
	}

	@Override
	public void zetNagekeken(boolean b) {
	}

	@Override
	public void setCommunicationRoot(OpdrNavIF comRoot) {
		this.comRoot = comRoot;
		zetMode(comRoot.getMode());
		
		comRoot.addCBookEventListener("graph", this);
	}
	
	public void zetMode(int mode) {
		this.mode = mode;
	}
	
	public void setChanged() {
		if(comRoot==null)
			return;
		
		if(correctGraph && attemptsToCorrect==999)
			attemptsToCorrect = ivmDrawGWTField.getAttemptCount();
		
		comRoot.setChanged(true);
				
		String feedback = feedbackTekst.getElement().getInnerText(); //Hier laatste feedback opvragen
		Map map = new HashMap<String,Object>();
		map.put("content", feedback);
		comRoot.fireEvent(new CBookEvent(this,"text.feedback",map));
		
		Map<String,Object> mapGraph = ivmDrawGWTField.getState();
		comRoot.fireEvent(new CBookEvent(this,"graph",mapGraph));
		
		if(correctGraph)
			comRoot.fireEvent(new CBookEvent(this,"action.correct"));
		else
			comRoot.fireEvent(new CBookEvent(this,"action.false"));
	}

	@Override
	public void zetVolledigeBreedte(int breedte) {
		this.breedte = breedte;
	}

	@Override
	public int getAsHoogte() {
		return 0;
	}

	public int getHeight() 
	{
		return hoogte;
	}

	public int getWidth() 
	{
		return breedte;
	}

	@Override
	public void setAsHoogte(int ashoogte) {
	}

	@Override
	public void acceptCBookEvent(CBookEvent event) {
		String command = event.getCommand();
		if (command.startsWith("graph"))	{
			Map map = (Map)event.getParameters();
			if (map!=null) {	
				ivmDrawGWTField.setState(map);
			}
			setChanged();
		}
	}
	
	class MousePopupHandler implements MouseDownHandler, MouseMoveHandler, MouseUpHandler {
		@Override
		public void onMouseUp(MouseUpEvent event) {
		}

		@Override
		public void onMouseMove(MouseMoveEvent event) {
		}

		@Override
		public void onMouseDown(MouseDownEvent event) {
			closeFeedback();
		}
	}
	class PointerPopupHandler implements PointerDownHandler, PointerMoveHandler, PointerUpHandler	{
		@Override
		public void onPointerUp(PointerUpEvent event) {
		}

		@Override
		public void onPointerMove(PointerMoveEvent event) {
		}

		@Override
		public void onPointerDown(PointerDownEvent event) {
			closeFeedback();
		}
	}
	class TouchPopupHandler implements TouchStartHandler, TouchMoveHandler, TouchEndHandler {
		@Override
		public void onTouchEnd(TouchEndEvent event) {
		}

		@Override
		public void onTouchMove(TouchMoveEvent event) {
		}

		@Override
		public void onTouchStart(TouchStartEvent event) {
			closeFeedback();
		}
	}
}
