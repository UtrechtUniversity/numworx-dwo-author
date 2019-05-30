package fi.ivmdrawgwt.client;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.*;

import fi.ivmdrawgwt.client.text.Text;
import nl.uu.fi.dwo.interaction.client.InteractionStub;
import nl.uu.fi.dwo.interaction.client.JSONUtilities;
import nl.uu.fi.dwo.interaction.client.OpdrNavIF;
import nl.uu.fi.dwo.interaction.client.Stub;
import nl.uu.fi.dwo.interaction.client.json.ObjectMap;


public class IVMdrawGWT extends Composite implements EntryPoint, InteractionStub {
    public LayoutPanel mainPanel = new LayoutPanel();
    public Label label = new Label("Teken een grafiek!");
    public PopupPanel popUp = new PopupPanel();

	Logger logger = Logger.getLogger("feedbackDebug");

	static final String upgradeMessage = "Your browser does not support the HTML5 Canvas. Please upgrade your browser to view this demo.";
	static final Text rb = GWT.create(Text.class);
		
	DockLayoutPanel dlp;

	static int breedte = 700;
	static int hoogte = 550;
	
	int bottomHeight = 32;
	int topHeight = 52;
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
	int vaasNummer = -1;

	public void onModuleLoad() {
		ivmDrawGWTClientBundle = GWT.create(IVMdrawGWTClientBundle.class);
		ivmDrawCss = ivmDrawGWTClientBundle.getKladjeGWTCSS();
		ivmDrawCss.ensureInjected();

		dlp = new DockLayoutPanel(Style.Unit.PX);
		dlp.addStyleName(ivmDrawCss.dock());
		dlp.setPixelSize(breedte,hoogte);

		label.getElement().getStyle().setFontSize(2, Style.Unit.EM);

		mainPanel.add(dlp);
		mainPanel.add(label);

		mainPanel.setWidgetLeftWidth(dlp, 0, Style.Unit.EM, 50, Style.Unit.EM);
		mainPanel.setWidgetRightWidth(label, 68, Style.Unit.EM, 30, Style.Unit.EM);
		mainPanel.setWidgetTopHeight(label, 0, Style.Unit.EM, 42, Style.Unit.EM);

		label.getElement().getStyle().setBorderStyle(Style.BorderStyle.SOLID);
		label.getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);

		RootLayoutPanel.get().add(mainPanel);
		RootLayoutPanel.get().addStyleName(ivmDrawCss.root());

		ivmFeedbackGWTField = new IVMfeedbackGWTField(this);

		Stub.publish(this);

		Map<String, Object> launchdata = new HashMap<>();
		Map<String, Number> random = Collections.emptyMap();

		init(breedte, hoogte, launchdata, random );

	}
	
	public IVMdrawGWT(HashMap<String, Object> map, String[] randomVarNamen, HashMap<String, Number> randomVarWaarden) {
		ObjectMap h = JSONUtilities.wrapMap(map);

		//this.randomVarNamen = randomVarNamen;
		//this.randomVarWaarden = randomVarWaarden;
		Map<String,Object> launchState = new HashMap<String,Object>();
		
		if (h.containsKey("breedte"))
			breedte = h.getInt("breedte");
		if (h.containsKey("hoogte"))
			hoogte = h.getInt("hoogte");
		if (h.containsKey("interactiePanelLaunchState"))
			launchState = h.getMap("interactiePanelLaunchState");

		ivmDrawGWTClientBundle = GWT.create(IVMdrawGWTClientBundle.class);
		ivmDrawCss = ivmDrawGWTClientBundle.getKladjeGWTCSS();
		ivmDrawCss.ensureInjected();

		dlp = new DockLayoutPanel(Style.Unit.PX);
		dlp.addStyleName(ivmDrawCss.dock());
		dlp.setPixelSize(breedte-200,hoogte);
		
		mainPanel.add(dlp);
		mainPanel.add(label);

		

//		RootPanel.get().add(mainPanel);
		ivmFeedbackGWTField = new IVMfeedbackGWTField(this);

		init(breedte, hoogte, launchState, randomVarWaarden);
		}

	public IVMdrawGWT() {
		initWidget(new Label("IVMdraw"));
	}
	
	@Override
	public void init(int width, int height, Map<String, Object> launchData,	Map<String, Number> values) {
		//initWidget(new Label("IVMdraw"));
		
		this.breedte = width;
		this.hoogte = height;
		
		dlp.setSize("" + breedte + "px", "" + hoogte + "px");

		ObjectMap launchState = JSONUtilities.wrapMap(launchData);
		if(launchState.containsKey("vaasNummer")) {
			vaasNummer = launchState.getInt("vaasNummer");
			this.ivmDrawGWTField.correctVaasNummer = vaasNummer;
		}


		bottomPanel = new LayoutPanel();
		bottomPanel.addStyleName(ivmDrawCss.bottom());
		bottomPanel.add(new Label("Vaasnummer = "+vaasNummer));
		
		dlp.addSouth(bottomPanel, bottomHeight);
		
		topPanel = new LayoutPanel();
		topPanel.addStyleName(ivmDrawCss.top());
		//dlp.addNorth(topPanel, topHeight);

		int veldhoogte = hoogte - bottomHeight;
		
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

		//makeBottom();
		
		dlp.forceLayout();
		bottomPanel.forceLayout();
		
		ivmDrawGWTField.paint();

	}
	
	public Widget asWidget() {
		return mainPanel; 
	}

	@Override
	public HashMap<String, Object> getState() {
		return ivmDrawGWTField.getState();
	}

	@Override
	public void setState(HashMap<String, Object> h) {
		if(h == null||h.isEmpty()) return;
		ivmDrawGWTField.setState(h);
	}

	@Override
	public int getScore() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int[][] getScoreObjectives() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean isCorrect() {
		// TODO Auto-generated method stub
		return null;
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
	public void setCommunicationRoot(OpdrNavIF comRoot) {
		this.comRoot = comRoot;

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
	
}
