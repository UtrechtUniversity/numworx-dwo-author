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
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.*;

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
	public HorizontalPanel mainPanel = new HorizontalPanel();
    public Label label = new Label("Teken een grafiek!");
    public PopupPanel popUp = new PopupPanel();

	static final String upgradeMessage = "Your browser does not support the HTML5 Canvas. Please upgrade your browser to view this demo.";
	static final Text rb = GWT.create(Text.class);
		
	DockLayoutPanel dlp;

	static int breedte = 900;
	static int hoogte = 550;
	
	int bottomHeight = 0;
	int topHeight = 24;
	int leftOffset = 5;
	int topOffset = 5;
	int toggleSize = 22;
	int pushSize = 24;
	int buttonWidth = 40;
	int buttonHeight = 22;

	IVMdrawGWTClientBundle ivmDrawGWTClientBundle;
	static IVMdrawCssResource ivmDrawCss;

	LayoutPanel bottomPanel;
	LayoutPanel topPanel;
	
	IVMdrawGWTField ivmDrawGWTField;
	IVMfeedbackGWTField ivmFeedbackGWTField;
	
	OpdrNavIF comRoot;
	int vaasNummer = 0;
	boolean jarFeedbackVisible = false;
	boolean feedbackVisible = false;
	boolean goedFoutVisible = false;
	boolean historyVisible = false;
	int scoreMax = 0;
	int deelScore1 = 0;
	int deelScore2 = 0;
	int attemptsToCorrect = 999;
	
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
	
	boolean correctGraph = false;
	int mode = 0;
	boolean check;
	
	ListBox historyList;
	PopupPanel feedbackPanel = new PopupPanel(true);
	LayoutPanel feedbackTekst = new LayoutPanel();
	
	ImageResource goedkrulResource;
	ImageResource foutkruisResource;
	ImageResource binResource;
	ImageResource feedbackResource;
	
	Image goedkrulImage;
	Image foutkruisImage;
	Image binImage;
	Image feedbackImage;

	/**
	 * The GWT entry point method, called automatically by loading a module that declares an implementing class as an
	 * entry point. Source: http://www.gwtproject.org/javadoc/latest/com/google/gwt/core/client/EntryPoint.html
	 *
	 * For local testing and debugging, use IVMdrawGWTDebug's onModuleLoad.
	 */
	public void onModuleLoad() {
		ivmDrawGWTClientBundle = GWT.create(IVMdrawGWTClientBundle.class);
		ivmDrawCss = ivmDrawGWTClientBundle.getKladjeGWTCSS();
		ivmDrawCss.ensureInjected();
		
		
		
		dlp = new DockLayoutPanel(Style.Unit.PX);
		dlp.addStyleName(ivmDrawCss.dock());
		dlp.setPixelSize(breedte,hoogte);

		label.getElement().getStyle().setFontSize(2, Style.Unit.EM);
		label.getElement().getStyle().setHeight(21.5, Style.Unit.EM);

		mainPanel.add(dlp);
		//mainPanel.add(label);

		//mainPanel.setCellWidth(label, "250");
		//mainPanel.setBorderWidth(1);
		//mainPanel.setSpacing(5);

		//label.getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);

		RootLayoutPanel.get().add(mainPanel);
		RootLayoutPanel.get().addStyleName(ivmDrawCss.root());

		ivmFeedbackGWTField = new IVMfeedbackGWTField(this);

		Stub.publish(this);
	}


	/**
	 * Constructor for the IVMdrawGWT object.
	 * @param map Launchdata
	 * @param randomVarNamen TODO: not sure
	 * @param randomVarWaarden TODO: not sure
	 */
	public IVMdrawGWT(HashMap<String, Object> map, String[] randomVarNamen, HashMap<String, Number> randomVarWaarden) {
		ObjectMap h = JSONUtilities.wrapMap(map);

		//this.randomVarNamen = randomVarNamen;
		//this.randomVarWaarden = randomVarWaarden;
		Map<String,Object> launchData = new HashMap<String,Object>();
		
		if (h.containsKey("breedte"))
			breedte = h.getInt("breedte");
		if (h.containsKey("hoogte"))
			hoogte = h.getInt("hoogte");
		if (h.containsKey("interactiePanelLaunchState"))
			launchData = h.getMap("interactiePanelLaunchState");
		

		ivmDrawGWTClientBundle = GWT.create(IVMdrawGWTClientBundle.class);
		ivmDrawCss = ivmDrawGWTClientBundle.getKladjeGWTCSS();
		ivmDrawCss.ensureInjected();
		
		goedkrulResource = ivmDrawGWTClientBundle.goedvinkResource();
		foutkruisResource = ivmDrawGWTClientBundle.foutkruisResource();
		binResource = ivmDrawGWTClientBundle.binResource();
		feedbackResource = ivmDrawGWTClientBundle.feedbackResource();
		
		goedkrulImage = new Image(goedkrulResource);
		foutkruisImage = new Image(foutkruisResource);
		binImage = new Image(binResource);
		feedbackImage = new Image(feedbackResource);
		
		goedkrulImage.setVisible(false);
		foutkruisImage.setVisible(false);

		dlp = new DockLayoutPanel(Style.Unit.PX);
		dlp.addStyleName(ivmDrawCss.dock());
		dlp.setPixelSize(breedte,hoogte);
		
		mainPanel.add(dlp);
		//mainPanel.add(label);

		RootPanel.get().add(mainPanel);
		ivmFeedbackGWTField = new IVMfeedbackGWTField(this);

		init(breedte, hoogte, launchData, randomVarWaarden);
	}

	public IVMdrawGWT() {
		initWidget(new Label("IVMdraw"));
	}
	
	@Override
	public void init(int width, int height, Map<String, Object> launchData,	Map<String, Number> values) {
		//initWidget(new Label("IVMdraw"));
		
		goedkrulResource = ivmDrawGWTClientBundle.goedvinkResource();
		foutkruisResource = ivmDrawGWTClientBundle.foutkruisResource();
		binResource = ivmDrawGWTClientBundle.binResource();
		feedbackResource = ivmDrawGWTClientBundle.feedbackResource();
		
		goedkrulImage = new Image(goedkrulResource);
		foutkruisImage = new Image(foutkruisResource);
		binImage = new Image(binResource);
		feedbackImage = new Image(feedbackResource);
		
		goedkrulImage.setVisible(false);
		foutkruisImage.setVisible(false);
		
		breedte = width;
		hoogte = height;
		
		dlp.setSize("" + breedte + "px", "" + hoogte + "px");

		bottomPanel = new LayoutPanel();
		bottomPanel.addStyleName(ivmDrawCss.bottom());
		
		//dlp.addSouth(bottomPanel, bottomHeight);
		
		topPanel = new LayoutPanel();
		//topPanel.addStyleName(ivmDrawCss.top());
		dlp.addNorth(topPanel, 24);
		
		historyList = new ListBox();
		historyList.setVisible(false);
		topPanel.add(historyList);
		topPanel.setWidgetLeftWidth(historyList.asWidget(), 30, Style.Unit.PX, 160, Style.Unit.PX);
		
		
		

		int veldhoogte = hoogte - bottomHeight-24;
		
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
		if(launchState.containsKey("feedbackCase1")) feedbackCase1 = (String)launchState.get("feedbackCase1");
		if(launchState.containsKey("feedbackCase2a")) feedbackCase2a = (String)launchState.get("feedbackCase2a");
		if(launchState.containsKey("feedbackCase2b")) feedbackCase2b = (String)launchState.get("feedbackCase2b");
		if(launchState.containsKey("feedbackCase3a")) feedbackCase3a = (String)launchState.get("feedbackCase3a");
		if(launchState.containsKey("feedbackCase3b")) feedbackCase3b = (String)launchState.get("feedbackCase3b");
		if(launchState.containsKey("feedbackCase4a")) feedbackCase4a = (String)launchState.get("feedbackCase4a");
		if(launchState.containsKey("feedbackCase4b")) feedbackCase4b = (String)launchState.get("feedbackCase4b");
		if(launchState.containsKey("feedbackCase5")) feedbackCase5 = (String)launchState.get("feedbackCase6");
		if(launchState.containsKey("feedbackCase6")) feedbackCase6 = (String)launchState.get("feedbackCase6");
		if(launchState.containsKey("feedbackCase7")) feedbackCase7 = (String)launchState.get("feedbackCase7");
		
		check = check || feedbackVisible; // voor backwards comp
		
		boolean jarFeedbackSelected = true;
		if(launchState.containsKey("jarFeedbackVisible")) 
			jarFeedbackSelected = launchState.getBoolean("jarFeedbackVisible");
		jarFeedbackVisible = jarFeedbackSelected && vaasNummer!=0;
		
		
		this.ivmDrawGWTField.setCorrectVaasNummer(vaasNummer);
		label.setVisible(feedbackVisible);
		historyList.setVisible(historyVisible);
		
		//bottomPanel.add(new Label("Vaasnummer = "+vaasNummer));

		//makeBottom();
		
		
		//bottomPanel.forceLayout();
		
		ivmDrawGWTField.paint();
		
		feedbackTekst.getElement().setInnerText("Hier de feedback die ik wil laten zien");
		feedbackTekst.getElement().getStyle().setColor(""+CssColor.make(49,71,112));
		
		VerticalPanel vp = new VerticalPanel();
		vp.add(goedkrulImage);	
		vp.add(foutkruisImage);
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
		
		dlp.forceLayout();

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
			feedbackPanel.setAutoHideEnabled(true);
			
		}
	}
	
	public void closeFeedback() {
		logger.info("closeFeedback");
		feedbackPanel.hide();
		feedbackPanel.setAutoHideEnabled(false);
	}
	
	public Widget asWidget() {
		return mainPanel; 
	}
	
	public ListBox getHistoryListBox() {
		return historyList;
	}

	@Override
	public HashMap<String, Object> getState() {
		HashMap h = ivmDrawGWTField.getState();
		
		h.put("attemptsToCorrect", attemptsToCorrect);
		
		return h;
	}

	@Override
	public void setState(HashMap<String, Object> h) {
		if(h == null||h.isEmpty()) return;
		
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean isCorrect() {
		return correctGraph;
	}

	@Override
	public void kijkNa() {
		// TODO Auto-generated method stub

	}

	@Override
	public void zetNagekeken(boolean b) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setCommunicationRoot(OpdrNavIF comRoot)
	{
		this.comRoot = comRoot;
		zetMode(comRoot.getMode());
		
		comRoot.addCBookEventListener("graph", this);
	}
	
	public void zetMode(int mode)
	{
		this.mode = mode;
		
	}
	
	public void setChanged() {
		if(comRoot==null)
			return;
		
		if(correctGraph && attemptsToCorrect==999)
			attemptsToCorrect = ivmDrawGWTField.getAttemptCount();
		
		comRoot.setChanged(true);
				
		String feedback = label.getText(); //Hier laatste feedback opvragen
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void acceptCBookEvent(CBookEvent event) {
		String command = event.getCommand();
		if (command.startsWith("graph"))
		{
			Map map = (Map)event.getParameters();
			if (map!=null)
			{	ivmDrawGWTField.setState(map);
				
			}
		}
		
	}
	
}
