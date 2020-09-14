package fi.mathscratchgwt.client;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Logger;

import org.vectomatic.dom.svg.ui.SVGImage;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import nl.uu.fi.dwo.interaction.client.InteractionStub;
import nl.uu.fi.dwo.interaction.client.InteractionView;
import nl.uu.fi.dwo.interaction.client.JSONUtilities;
import nl.uu.fi.dwo.interaction.client.OpdrNavIF;
import nl.uu.fi.dwo.interaction.client.Stub;
import nl.uu.fi.dwo.interaction.client.event.CBookEvent;
import nl.uu.fi.dwo.interaction.client.event.CBookEventListener;
import nl.uu.fi.dwo.interaction.client.json.ObjectMap;

public class MathScratchGWT implements EntryPoint,InteractionView, InteractionStub, CBookEventListener {
	private static Logger logger = Logger.getLogger("MathScratchGWT");
	private static final String upgradeMessage = "Your browser does not support the HTML5 Canvas. Please upgrade your browser to view this demo.";
		
	public static MathScratchCssResource mathScratchCss;
	
	protected MathScratchGWTClientBundle mathScratchGWTClientBundle;
	
	OpdrNavIF comRoot;
	private int scoreMax = 0;
	
	protected int breedte = 700;
	protected int hoogte = 550;
	protected int asHoogte = 0;
	
	protected DockLayoutPanel dlp;
	
	MathScratchField mathScratchField;
	
	public MathScratchGWT() {
	}
	
	public MathScratchGWT(HashMap<String, Object> map, String[] randomVarNamen, HashMap randomVarWaarden) {
		ObjectMap h = JSONUtilities.wrapMap(map);
		Map<String,Object> launchData = new HashMap<String,Object>();
		
		if (h.containsKey("breedte"))
			breedte = h.getInt("breedte");
		if (h.containsKey("hoogte"))
			hoogte = h.getInt("hoogte");
		if (h.containsKey("interactiePanelLaunchState"))
			launchData = h.getMap("interactiePanelLaunchState");
		
		prepareResources();
		
		dlp = new DockLayoutPanel(Style.Unit.PX);
		dlp.addStyleName(mathScratchCss.dock());
		dlp.setPixelSize(breedte,hoogte);
		
		init(breedte, hoogte, launchData, randomVarWaarden);
	}
	
	@Override
	public void onModuleLoad() {
		prepareResources();
		
		dlp = new DockLayoutPanel(Style.Unit.PX);
		dlp.addStyleName(mathScratchCss.dock());
		dlp.setPixelSize(breedte , hoogte );
		
		RootLayoutPanel.get().add(dlp);
		RootLayoutPanel.get().addStyleName(mathScratchCss.root());
		
		Stub.publish(this);
	}
	
	protected void prepareResources() {
		mathScratchGWTClientBundle = GWT.create(MathScratchGWTClientBundle.class);
		mathScratchCss = mathScratchGWTClientBundle.getMathScratchGWTCSS();
		mathScratchCss.ensureInjected();
	}
	
	@Override
	public void init(int width, int height, Map<String, Object> launchData, Map<String, Number> values) {
		Element body = Document.get().getBody();
		body.setAttribute("oncontextmenu", "return false;");

		this.breedte = width;
		this.hoogte = height;
		
		dlp.setSize("" + breedte + "px", "" + hoogte + "px");
		
		mathScratchField = new MathScratchField(breedte,hoogte, this);

//		Canvas mathScratchCanvas = mathScratchField.getCanvas();
//		if (mathScratchCanvas == null) {
//	      RootLayoutPanel.get().add(new Label(upgradeMessage));
//	      return;
//		}
//		mathScratchCanvas.addStyleName(mathScratchCss.canvas());
//	    mathScratchField.initContext2d();
//		dlp.add(mathScratchCanvas);
		
		SVGImage svgImage = mathScratchField.getSVGImage();
		dlp.add(svgImage);
		
		ObjectMap launchState = JSONUtilities.wrapMap(launchData);
		
		if (launchState.containsKey("logOption")) {
			boolean logOption = ((Boolean) launchState.getBoolean("logOption"));
		}
		if (launchState.containsKey("logID")) {
			String logID = ((String) launchState.getString("logID"));
		}
		if (launchState.containsKey("checkDocent")) {
			boolean checkDocent = ((Boolean) launchState.getBoolean("checkDocent"));
		}
		if (launchState.containsKey("scoreMax")) {
			scoreMax = ((Integer) launchState.getInt("scoreMax"));
		}
		if (launchState.containsKey("drawOption")) {
			mathScratchField.setDrawOption((Boolean) launchState.getBoolean("drawOption"));
		}
		if (launchState.containsKey("formOption")) {
			mathScratchField.setFormOption((Boolean) launchState.getBoolean("formOption"));
		}
		if (launchState.containsKey("showWriting")) {
			mathScratchField.setShowWriting((Boolean) launchState.getBoolean("showWriting"));
		}
		if (launchState.containsKey("calculator")) {
			mathScratchField.setCalculator((Boolean) launchState.getBoolean("calculator"));
		}
		if (launchState.containsKey("inputOption")) {
			mathScratchField.setInputOption((Boolean) launchState.getBoolean("inputOption"));
		}
		if (launchState.containsKey("areaSettings")) {
			Map map = (Map) launchState.getMap("areaSettings");
			if(!map.isEmpty()) // Wim, kan leeg zijn.
				mathScratchField.setAreaSetting(map);
		}
		if (launchState.containsKey("grid")) {
			mathScratchField.setGrid((Boolean) launchState.getBoolean("grid"));
		}
		if (launchState.containsKey("scaleWriting")) {
			mathScratchField.setScaleWriting((Boolean) launchState.getBoolean("scaleWriting"));
		}
		if (launchState.containsKey("writingScale")) {
			mathScratchField.setWritingScale((Double) launchState.getDouble("writingScale"));
		}
		mathScratchField.paint();
	}
	
	public void setChanged() {
//		if(formuleViewer!=null)
//			topPanel.remove(formuleViewer.getAsPanel());
//		formuleViewer = new FormuleViewer(kladjeGWTVeld.getFormula());
//		formuleViewer.setFont(FormuleFont.createFromFontSize(16));
//		topPanel.add(formuleViewer.getAsPanel());
		
		
		if(comRoot==null)
			return;
		Map<String,Object> map = mathScratchField.getState(false);
		comRoot.fireEvent(new CBookEvent(this,"drawing",map));
		comRoot.fireEvent(new CBookEvent(this,"equation",mathScratchField.getFormula()));
//		logger.info("in setChanged");
	}
	
	public void sendDrawing() {
		if(comRoot==null)
			return;
		Map<String,Object> map = mathScratchField.getState(false);
		comRoot.fireEvent(new CBookEvent(this,"drawing",map));
	}
	
	public void sendEquation() {
		if(comRoot!=null)
			comRoot.fireEvent(new CBookEvent(this,"equation",mathScratchField.getFormula()));
	}
	
	public void sendEquation(int nr) {
		if(comRoot!=null)
			comRoot.fireEvent(new CBookEvent(this,"equation."+nr,mathScratchField.getInputFormula()));
	}
	
	public void sendCorrectEquation() {
		if(comRoot!=null) {
			comRoot.fireEvent(new CBookEvent(this,"equation.correct",mathScratchField.getFormula()));
		}
	}
	
	public void fireCheck() {
		if(comRoot!=null)
			comRoot.fireEvent(new CBookEvent(this,"action.check"));
	}
	
	public void fireCheck_n() {
		if(comRoot!=null)
			comRoot.fireEvent(new CBookEvent(this,"action.check.n"));
	}
	
	public void fireClose() {
		if(comRoot!=null)
			comRoot.fireEvent(new CBookEvent(this,"action.closePopup"));
	}
	
	public void fireStrokeCodes() {
		if(comRoot==null)
			return;
		String text = mathScratchField.getStrokeCode();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("content", text);
		comRoot.fireEvent(new CBookEvent(this,"text.strokecode",map));
	}

	@Override
	public HashMap<String, Object> getState() {
		return mathScratchField.getState();
	}

	@Override
	public void setState(HashMap<String, Object> h) {
		if(h == null||h.isEmpty()) return;
		mathScratchField.setState(h, false);
	}

	@Override
	public int getScore() {
		return 0;
	}

	@Override
	public int[][] getScoreObjectives() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean isCorrect() {
		if(scoreMax==0)
			return Boolean.TRUE;
		return
				Boolean.FALSE;
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
//		premium = comRoot.getContext().getBoolean("premium", premium); // installeer hier premium feature.
//// doe er wat mee...		
//		if ( premium ) {
//			GWT.log("met een premium abonnement");
//		} else {
//			GWT.log("zonder een premium abonnement");
//		}
		
		comRoot.addCBookEventListener("drawing", this);
		comRoot.addCBookEventListener("action.setCorrect", this);
		comRoot.addCBookEventListener("action.setFalse", this);
		comRoot.addCBookEventListener("action.setHalf", this);
	}

	

	@Override
	public void zetVolledigeBreedte(int breedte) {
		this.breedte = breedte;

	}

	@Override
	public Widget asWidget() {
		return dlp; 
	}

	@Override
	public int getAsHoogte() {
		return asHoogte;
	}

	@Override
	public int getHeight() {
		return hoogte;
	}

	@Override
	public int getWidth() {
		return breedte;
	}

	@Override
	public void setAsHoogte(int ashoogte) {
		this.asHoogte = asHoogte;

	}

	@Override
	public void acceptCBookEvent(CBookEvent event) {
		String command = event.getCommand();
		if (command.startsWith("drawing")) {
			Map map = (Map)event.getParameters();
			if (map!=null) {
				mathScratchField.setState(map, false);
			}
		}
		
		if (command.startsWith("action.setFalse")) {
			mathScratchField.setFalse(true);
			mathScratchField.paintFormule(false);
			
		}
		
		if (command.startsWith("action.setCorrect"))
		{
			{	mathScratchField.setCorrect(true);
			mathScratchField.paintFormule(false);
			}
		}
		
		if (command.startsWith("action.setHalf"))
		{
			{	mathScratchField.setHalf(true);
			mathScratchField.paintFormule(false);
			}
		}
	}

	

}
