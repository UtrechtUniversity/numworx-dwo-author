package fi.mathscratchgwt.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.vectomatic.dom.svg.OMSVGDocument;
import org.vectomatic.dom.svg.OMSVGElement;
import org.vectomatic.dom.svg.OMSVGLength;
import org.vectomatic.dom.svg.OMSVGLineElement;
import org.vectomatic.dom.svg.OMSVGPathElement;
import org.vectomatic.dom.svg.OMSVGPathSegList;
import org.vectomatic.dom.svg.OMSVGRect;
import org.vectomatic.dom.svg.OMSVGRectElement;
import org.vectomatic.dom.svg.OMSVGSVGElement;
import org.vectomatic.dom.svg.OMSVGTextElement;
import org.vectomatic.dom.svg.ui.SVGImage;
import org.vectomatic.dom.svg.utils.OMSVGParser;
import org.vectomatic.dom.svg.utils.SVGConstants;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.canvas.dom.client.Context2d.LineCap;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.dom.client.Style.FontStyle;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.dom.client.Style.WhiteSpace;
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
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.vaadin.pointerevents.client.PointerCancelEvent;
import com.vaadin.pointerevents.client.PointerCancelHandler;
import com.vaadin.pointerevents.client.PointerDownEvent;
import com.vaadin.pointerevents.client.PointerDownHandler;
import com.vaadin.pointerevents.client.PointerEvent;
import com.vaadin.pointerevents.client.PointerMoveEvent;
import com.vaadin.pointerevents.client.PointerMoveHandler;
import com.vaadin.pointerevents.client.PointerUpEvent;
import com.vaadin.pointerevents.client.PointerUpHandler;

import fi.writemathgwt.client.engine.Point;
import fi.writemathgwt.client.engine.Stroke;
import nl.uu.fi.dwo.interaction.client.JSONUtilities;
import nl.uu.fi.dwo.interaction.client.json.ObjectList;
import nl.uu.fi.dwo.interaction.client.json.ObjectMap;

public class MathScratchField {

	private static Logger logger = Logger.getLogger("MathScratchField");
	
	public static CssColor colorBlue1 = CssColor.make(49,71,112);
	public static CssColor colorBlue2 = CssColor.make(38,115,182);
	public static CssColor colorBlue3 = CssColor.make(120,150,202);
	public static CssColor colorBlue4 = CssColor.make(180,195,228);
	public static CssColor colorBlue5 = CssColor.make(211,229,244);
	public static CssColor colorBlue6 = CssColor.make(229,240,249);
	
	public static CssColor colorGray1 = CssColor.make(206,207,208);
	public static CssColor colorGray2 = CssColor.make(221,223,225);
	public static CssColor colorGray3 = CssColor.make(237,239,241);
	
	protected static CssColor defaultBorderColor = CssColor.make(211,229,244);
	protected static CssColor defaultBorderColorActive = CssColor.make(38,115,182);
	protected static CssColor defaultBgColor = CssColor.make(229, 240, 249);
	protected static CssColor defaultForegroundColor = CssColor.make(120, 150, 202);
	protected static CssColor defaultForegroundColorActive = CssColor.make(38,115,182);
	protected static CssColor defaultTextColor = CssColor.make(49,71,112);
	
	protected CssColor borderColor = defaultBorderColor;
	protected CssColor borderColorActive = defaultBorderColorActive;
	protected CssColor bgColor = defaultBgColor;
	protected CssColor foregroundColor = defaultForegroundColor;
	protected CssColor foregroundColorActive = defaultForegroundColorActive;
	protected CssColor textColor = defaultTextColor;
	
	protected String fontFamily = "sans-serif";
	protected FontStyle fontStyle = FontStyle.NORMAL;
	protected FontWeight fontWeight = FontWeight.BOLD;
	protected int fontSize = 13;
	
	private OMSVGSVGElement svg;
	private SVGImage svgImage;
	OMSVGDocument doc;
	SVGManager svgManager;
	
	private Canvas mathScratchCanvas, backgroundCanvas;
	private Context2d gIm, backgroundgIm, strokeContainergIm;

	int breedte, hoogte;

	private KStrokeContainer selectedStrokeContainer = null;

	public MathScratchGWT eigenaar;

	private Image binImage;
	private ImageElement binImageElement;
	private Image goedvinkImage, halfvinkImage, foutkruisImage;
	ImageElement goedvinkImageElement, halfvinkImageElement, foutkruisImageElement;

	protected double schrijfLeesFactor = 2.2;

	boolean lijnen = false;
	boolean ruitjes = true;
	int lineDistance = 10;
	int gridSize = 10;
	CssColor lijnenKleur = CssColor.make(150, 150, 255); 
	CssColor ruitjesKleur = CssColor.make(180,195,228);

	int maxHistories = 10;
	int numHistories = 0;
	HashMap<String, Object>[] histories = new HashMap[maxHistories + 1];

	private boolean correctEquationSend;
	
	private ArrayList<KStrokeContainer> kStrokeContainers = new ArrayList<KStrokeContainer>();
	private KStrokeContainer currentStrokeContainer, lastCurrentStrokeContainer;// = new KStrokeContainer();

	private ArrayList<KStrokeContainer> hiddenStrokeContainers = new ArrayList<KStrokeContainer>();
	private ArrayList<Rectangle> hiddenSCRectangles = new ArrayList<Rectangle>();
	private KStrokeContainer currentHiddenStrokeContainer;
	private int activeHSCNumber = 0;

	protected boolean calculator=true;

	//ArrayList<DoublePoint> draggDoublePoints = new ArrayList<DoublePoint>();
	ArrayList<DoublePoint> formulaStrokePointsDouble = new ArrayList<DoublePoint>();
	ArrayList<fi.writemathgwt.client.engine.Point> formulaStrokePoints = new ArrayList<fi.writemathgwt.client.engine.Point>();
	Stroke lastStroke;

	Point translation = new Point(0, 0);
	double scale = 1.0;
	
	 boolean drawOption = true;
	 boolean formOption = true;
	 boolean showWriting = true;
	private boolean inputOption = true;
	private Map areaSettings = null;
	private boolean scaleWriting = true;
	
	

	public MathScratchField(int w, int h, MathScratchGWT eigenaar) {
		this.eigenaar = eigenaar;
		
		doc = OMSVGParser.currentDocument();
		svg = doc.createSVGSVGElement();
		svgImage = new SVGImage(svg);
		svgImage.setPixelSize(w, h);

		svgManager = new SVGManager(this, doc, svg, w, h);
		
		mathScratchCanvas = Canvas.createIfSupported();
		backgroundCanvas = Canvas.createIfSupported();
		
		setSize(w, h);

		MouseHandler mouseHandler = new MouseHandler();
		svgImage.addMouseDownHandler(mouseHandler);
		svgImage.addMouseMoveHandler(mouseHandler);
		svgImage.addMouseUpHandler(mouseHandler);

		TouchHandler touchHandler = new TouchHandler();
		svgImage.addTouchStartHandler(touchHandler);
		svgImage.addTouchMoveHandler(touchHandler);
		svgImage.addTouchEndHandler(touchHandler);

		PointerHandler pointerHandler = new PointerHandler();
		(svgImage.asWidget()).addDomHandler((PointerMoveHandler) pointerHandler, PointerMoveEvent.getType());
		(svgImage.asWidget()).addDomHandler((PointerUpHandler) pointerHandler, PointerUpEvent.getType());
		(svgImage.asWidget()).addDomHandler((PointerDownHandler) pointerHandler, PointerDownEvent.getType());

		ImageResource binResource = eigenaar.mathScratchGWTClientBundle.binResource();
		binImage = new Image(binResource);
		binImageElement = ImageElement.as(binImage.getElement());

		ImageResource goedvinkResource = eigenaar.mathScratchGWTClientBundle.goedvinkResource();
		goedvinkImage = new Image(goedvinkResource);
		goedvinkImageElement = ImageElement.as(goedvinkImage.getElement());

		ImageResource halfvinkResource = eigenaar.mathScratchGWTClientBundle.halfvinkResource();
		halfvinkImage = new Image(halfvinkResource);
		halfvinkImageElement = ImageElement.as(halfvinkImage.getElement());

		ImageResource foutkruisResource = eigenaar.mathScratchGWTClientBundle.foutkruisResource();
		foutkruisImage = new Image(foutkruisResource);
		foutkruisImageElement = ImageElement.as(foutkruisImage.getElement());

		svgManager.appendBin();
		svgManager.enableBin(false);
		svgManager.appendUndo();
		svgManager.enableUndo(false);
	}

	public Canvas getCanvas() {
		return mathScratchCanvas;
	}
	
	public SVGImage getSVGImage() {
		return svgImage;
	}
	
	public KStrokeContainer getCurrentStrokeContainer() {
		return currentStrokeContainer;
	}
	
	public ArrayList<KStrokeContainer> getStrokeContainers() {
		return kStrokeContainers;
	}

	public void initContext2d() {
		gIm = mathScratchCanvas.getContext2d();
		backgroundgIm = backgroundCanvas.getContext2d();
	}

	public String getFormula() {
		if (currentStrokeContainer != null)
			return currentStrokeContainer.getFormulaString();
		return "";
	}

	public String getInputFormula() {
		if (currentHiddenStrokeContainer != null)
			return currentHiddenStrokeContainer.getFormulaString();
		return "";
	}

	public String getStrokeCode() {
		String code = "";
		for (int i = 0; i < kStrokeContainers.size(); i++) {
			code += "SCnr " + i + "\n";
			KStrokeContainer sc = kStrokeContainers.get(i);
			code += sc.getStrokeCode();
		}
		return code;
	}

	public void setState(Map<String, Object> map, boolean init) {
		if (map == null || map.isEmpty())
			return;

		ObjectMap launchState = JSONUtilities.wrapMap(map);

		kStrokeContainers.clear();

		List<Map<String, Object>> strokeContainerList = new ArrayList<Map<String, Object>>();
		if (launchState.containsKey("strokeContainerList"))
			strokeContainerList = launchState.getMapList("strokeContainerList");
		for (int sCnt = 0; sCnt < strokeContainerList.size(); sCnt++) {
			KStrokeContainer sc = new KStrokeContainer(this);
			sc.setFormuleModus(!showWriting);
			sc.setState(strokeContainerList.get(sCnt));
			kStrokeContainers.add(sc);
		}
		svgManager.appendStrokeContainers();
		if(init)
			addToHistory();

		List<Map<String, Object>> hiddenStrokeContainerList = new ArrayList<Map<String, Object>>();
		if (launchState.containsKey("hiddenStrokeContainerList"))
			hiddenStrokeContainerList = launchState.getMapList("hiddenStrokeContainerList");
		for (int sCnt = 0; sCnt < hiddenStrokeContainerList.size(); sCnt++) {
			KStrokeContainer sc = hiddenStrokeContainers.get(sCnt);
			sc.setState(hiddenStrokeContainerList.get(sCnt));
			Rectangle r = hiddenSCRectangles.get(sCnt);
			sc.setDefaultRectangle(new Rectangle(r.x, r.y, r.width, r.height));
		}

		paint();
	}

	public HashMap<String, Object> getState() {
		return getState(true);
	}

	public HashMap<String, Object> getState(boolean end) {
		HashMap<String, Object> h = new HashMap<String, Object>();

		if (end && currentStrokeContainer != null) {
			closeCurrentContainer();
		}

		if (end && currentHiddenStrokeContainer != null) {
			closeCurrentHiddenContainer();
		}

		List<Map<String, Object>> strokeContainerList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < kStrokeContainers.size(); i++) {
			KStrokeContainer sc = kStrokeContainers.get(i);
			if (sc != currentStrokeContainer)
				strokeContainerList.add(sc.getState());
		}
		h.put("strokeContainerList", strokeContainerList);

		List<Map<String, Object>> hiddenStrokeContainerList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < hiddenStrokeContainers.size(); i++) {
			KStrokeContainer sc = hiddenStrokeContainers.get(i);
			hiddenStrokeContainerList.add(sc.getState());
		}
		h.put("hiddenStrokeContainerList", hiddenStrokeContainerList);

		return h;
	}

	void setSize(int w, int h) {
		breedte = w;
		hoogte = h;
		mathScratchCanvas.setWidth(w + "px");
		mathScratchCanvas.setHeight(h + "px");
		mathScratchCanvas.setCoordinateSpaceWidth(w);
		mathScratchCanvas.setCoordinateSpaceHeight(h);
		backgroundCanvas.setWidth(w + "px");
		backgroundCanvas.setHeight(h + "px");
		backgroundCanvas.setCoordinateSpaceWidth(w);
		backgroundCanvas.setCoordinateSpaceHeight(h);
		
		svgImage.setPixelSize(w, h);
	}

	private KStrokeContainer proActiveStrokeContainer;
	private int proActiveX;
	private int proActiveY;
	private boolean writing;
	private boolean moving;
	private boolean movingStrokes;
	private Point activeTranslation = new Point(0, 0);

	public Point getActiveTranslation() {
		return activeTranslation;
	}

	

	public void paintFormule(boolean refresh) {
		if(true)return;
		if (refresh) {
			gIm.clearRect(0, 0, breedte, hoogte);
			gIm.drawImage(backgroundCanvas.getCanvasElement(), 0.0, 0.0);
		}
		if (currentStrokeContainer != null) {
			gIm.setStrokeStyle(CssColor.make(80, 80, 80));
			currentStrokeContainer.draw(gIm);

			if (formulaStrokePoints.size() == 1) {
				fi.writemathgwt.client.engine.Point p = formulaStrokePoints.get(0);
				gIm.strokeRect(p.x, p.y, 1, 1);
			}
			if (formulaStrokePoints.size() > 1) {
				fi.writemathgwt.client.engine.Point p1 = formulaStrokePoints.get(0);
				gIm.beginPath();
				gIm.moveTo(p1.x, p1.y);
				for (int pCnt = 1; pCnt < formulaStrokePoints.size(); pCnt++) {
					fi.writemathgwt.client.engine.Point p2 = formulaStrokePoints.get(pCnt);
					gIm.lineTo(p2.x, p2.y);
					p1 = p2;
				}
				gIm.stroke();
			}
		}

		if (currentHiddenStrokeContainer != null) {
			gIm.setStrokeStyle(CssColor.make(80, 80, 80));
			currentHiddenStrokeContainer.draw(gIm);

			if (formulaStrokePoints.size() == 1) {
				fi.writemathgwt.client.engine.Point p = formulaStrokePoints.get(0);
				gIm.strokeRect(p.x, p.y, 1, 1);
			}
			if (formulaStrokePoints.size() > 1) {
				fi.writemathgwt.client.engine.Point p1 = formulaStrokePoints.get(0);
				gIm.beginPath();
				gIm.moveTo(p1.x, p1.y);
				for (int pCnt = 1; pCnt < formulaStrokePoints.size(); pCnt++) {
					fi.writemathgwt.client.engine.Point p2 = formulaStrokePoints.get(pCnt);
					gIm.lineTo(p2.x, p2.y);
					p1 = p2;
				}
				gIm.stroke();
			}
		}

		if (proActiveStrokeContainer != null) {
			proActiveStrokeContainer.draw(gIm);
		}
	}
	
	private DoublePoint lastPoint;

	public void paintLastSegment() {if(true)return;
		gIm.setLineWidth(2.0d);
		gIm.setStrokeStyle(CssColor.make(80, 80, 80));
		gIm.setLineCap(LineCap.ROUND);
		int size = formulaStrokePointsDouble.size();
		if (size == 2)
			lastPoint = formulaStrokePointsDouble.get(formulaStrokePointsDouble.size() - 2);
		DoublePoint p = formulaStrokePointsDouble.get(formulaStrokePointsDouble.size() - 1);
		
		gIm.beginPath();
		gIm.moveTo(lastPoint.x, lastPoint.y);
		gIm.lineTo(p.x, p.y);
		gIm.stroke();
		lastPoint = p;
	}
	
	
	public void paint() {
		//paint(backgroundgIm);
		//gIm.clearRect(0, 0, breedte, hoogte);
		//gIm.drawImage(backgroundCanvas.getCanvasElement(), 0.0, 0.0);
	}

	public void paint(Context2d g) {
		//g.clearRect(0, 0, breedte, hoogte);

		//drawBin(g);
		//drawUndo(g);

		if (currentStrokeContainer != null && !currentStrokeContainer.isNotRelevant()) {
			g.setFillStyle(CssColor.make("rgba(200,200,200,0.5)"));
		}

		g.setLineWidth(0.3d);

		// achtergrond horizontale lijnen
		if (lijnen) {
			g.setStrokeStyle(lijnenKleur);
			int steps = hoogte / lineDistance;
			for (int lCnt = 1; lCnt <= steps; lCnt++) {
				g.beginPath();
				g.moveTo(0, lCnt * lineDistance);
				g.lineTo(breedte - 1, lCnt * lineDistance);
				g.stroke();
			}
		}
		// achtergrond ruitjes
		if (ruitjes) {
			lineDistance = 10;
			g.setStrokeStyle(ruitjesKleur);
			int vSteps = hoogte / lineDistance;
			for (int vCnt = 1; vCnt <= vSteps; vCnt++) {
				g.beginPath();
				g.moveTo(0, vCnt * lineDistance);
				g.lineTo(breedte - 1, vCnt * lineDistance);
				g.stroke();
			}
			int hSteps = breedte / lineDistance;
			for (int hCnt = 1; hCnt <= hSteps; hCnt++) {
				g.beginPath();
				g.moveTo(hCnt * lineDistance, 0);
				g.lineTo(hCnt * lineDistance, hoogte - 1);
				g.stroke();
			}
		}
		g.setLineWidth(1.2d);
		tekenProgramma(g);
	}
	
	

	void tekenProgramma(Context2d g) {
		g.scale(scale, scale);
		g.translate(translation.x, translation.y);

		g.setLineWidth(3.0d);

		for (int k = 0; k < kStrokeContainers.size(); k++) {
			if (kStrokeContainers.get(k) != currentStrokeContainer
					&& kStrokeContainers.get(k) != proActiveStrokeContainer) {
				if (currentStrokeContainer != null && !currentStrokeContainer.isNotRelevant())
					g.setStrokeStyle(CssColor.make(150, 150, 150));
				else
					g.setStrokeStyle(CssColor.make(80, 80, 80));
				kStrokeContainers.get(k).draw(g);
			}
		}
		g.translate(-translation.x, -translation.y);
		g.scale(1 / scale, 1 / scale);
	}

	public Rectangle getBinArea() {
		return new Rectangle(breedte - 40, 6, 34, 34);
	}

	public Rectangle getUndoArea() {
		return new Rectangle(breedte - 90, 6, 34, 34);
	}

	private void drawBin(Context2d g) {
		Rectangle r = getBinArea();
		g.setStrokeStyle(CssColor.make(38, 115, 182));
		if (kStrokeContainers.size() < 1)
			g.setStrokeStyle(CssColor.make(180, 195, 228));
		g.setLineWidth(4.0d);
		g.beginPath();
		g.moveTo(r.x, r.y + r.height / 4);
		g.lineTo(r.x + r.width, r.y + r.height / 4);
		g.moveTo(r.x + r.width / 6, r.y + r.height / 4);
		g.lineTo(r.x + r.width / 4, r.y + r.height);
		g.lineTo(r.x + r.width * 3 / 4, r.y + r.height);
		g.lineTo(r.x + r.width * 5 / 6, r.y + r.height / 4);
		g.moveTo(r.x + r.width / 2, r.y + r.height / 4);
		g.lineTo(r.x + r.width / 2, r.y + r.height);
		g.moveTo(r.x + r.width * 3 / 8, r.y + r.height / 4);
		g.lineTo(r.x + r.width * 3 / 8, r.y);
		g.lineTo(r.x + r.width * 5 / 8, r.y);
		g.lineTo(r.x + r.width * 5 / 8, r.y + r.height / 4);

		g.moveTo(r.x, r.y + r.height / 6);
		g.closePath();
		g.stroke();
	}
	
	private void drawUndo(Context2d g) {
		Rectangle r = getUndoArea();
		g.setStrokeStyle(CssColor.make(38, 115, 182));
		if (numHistories < 1)
			g.setStrokeStyle(CssColor.make(180, 195, 228));
		g.setLineWidth(4.0d);
		g.beginPath();
		g.moveTo(r.x + r.width / 6, r.y + r.height);
		g.arc(r.x + r.width / 2, r.y + r.height, r.width * 5 / 12, Math.PI, 0);
		g.moveTo(r.x, r.y + r.height);
		g.lineTo(r.x, r.y + r.height * 3 / 4);
		g.lineTo(r.x + r.width / 4, r.y + r.height);
		g.lineTo(r.x - 2, r.y + r.height);

		g.moveTo(r.x, r.y + r.height);
		g.closePath();
		g.stroke();
	}
	
	private void closeCurrentContainer() {

		if (!currentStrokeContainer.recognizeOff && currentStrokeContainer != null && currentStrokeContainer.isNotRelevantWhenReady()) {
			kStrokeContainers.remove(currentStrokeContainer);
			svgManager.removeStrokeContainer(currentStrokeContainer);
			currentStrokeContainer = null;
			return;
		}
		if (currentStrokeContainer == null)
			return;
		currentStrokeContainer.setFormuleModus(!showWriting);
		currentStrokeContainer.setActive(false);
		currentStrokeContainer.scale(1.0 / schrijfLeesFactor);
		currentStrokeContainer.setEraserActive(false);
		
		currentStrokeContainer.translate(-activeTranslation.x, -activeTranslation.y);
		setInSCRow(currentStrokeContainer);
		activeTranslation.x = 0;
		activeTranslation.y = 0;
		lastCurrentStrokeContainer = currentStrokeContainer;
		svgManager.addStrokeContainer(currentStrokeContainer);
		currentStrokeContainer = null;
		activeHSCNumber = 0;
		eigenaar.fireClose();
		svgManager.removeCurrentSC();
	}

	private void closeCurrentHiddenContainer() {

		if (currentHiddenStrokeContainer != null && currentHiddenStrokeContainer.isNotRelevantWhenReady()) {
			currentHiddenStrokeContainer.setActive(false);
			currentHiddenStrokeContainer.getDefaultBox().translate(-activeTranslation.x, -activeTranslation.y);
			activeTranslation.x = 0;
			activeTranslation.y = 0;
			currentHiddenStrokeContainer = null;
			return;
		}
		if (currentHiddenStrokeContainer == null)
			return;
		
		currentHiddenStrokeContainer.setActive(false);
		currentHiddenStrokeContainer.translate(-activeTranslation.x, -activeTranslation.y);
		currentHiddenStrokeContainer.getDefaultBox().translate(-activeTranslation.x, -activeTranslation.y);
		activeTranslation.x = 0;
		activeTranslation.y = 0;
		currentHiddenStrokeContainer = null;
		activeHSCNumber = 0;
		svgManager.removeCurrentSC();

	}

	private KStrokeContainer findHiddenStrokeContainer(int x, int y) {
		for (int k = 0; k < hiddenStrokeContainers.size(); k++) {
			if (hiddenSCRectangles.get(k).contains(x, y) && !hiddenStrokeContainers.get(k).isActive()) {
				activeHSCNumber = k + 1;
				return hiddenStrokeContainers.get(k);
			}
		}
		return null;
	}

	private KStrokeContainer findInactiveStrokeContainer(int x, int y) {
		for (int k = 0; k < kStrokeContainers.size(); k++) {
			if (kStrokeContainers.get(k).contains(x, y, 0) && !kStrokeContainers.get(k).isActive()) {
				activeHSCNumber = 0;
				return kStrokeContainers.get(k);
			}
		}
		for (int k = 0; k < kStrokeContainers.size(); k++) {
			if (kStrokeContainers.get(k).contains(x, y, 30) && !kStrokeContainers.get(k).isActive()) {
				activeHSCNumber = 0;
				return kStrokeContainers.get(k);
			}
		}
		return null;
	}

	void undo() {
		//wis(false);
		HashMap<String, Object> lastState = getFromHistory();
		if (lastState != null) {
			setState(lastState, false);
		}
		paint();
	}
	
	void addToHistory() {
		HashMap<String, Object> stateTable = getState(false);
		histories[numHistories] = stateTable;
		numHistories++;
		if (numHistories > 1)
			svgManager.enableUndo(true);
		if (numHistories > maxHistories) {
			for (int i = 0; i < numHistories - 1; i++) {
				histories[i] = histories[i + 1];
			}
			numHistories--;
		}
		logger.info("addToHistory "+numHistories);
	}

	public HashMap<String, Object> getFromHistory() {
		HashMap<String, Object> lastState = null;
		if (numHistories > 1) {
			lastState = histories[numHistories - 2];
			numHistories--;
			if (numHistories < 2)
				svgManager.enableUndo(false);
		}
		else {
			numHistories = 1;
			svgManager.enableUndo(false);
		}
		logger.info("getFromHistory "+numHistories);
		return lastState;
		
//		if (numHistories > 0)
//			numHistories--;
//		
//
//		if (numHistories > 0) {
//			return histories[numHistories - 1];
//		} else {
//			numHistories = 0;
//			return null;
//		}
	}

	void wis(boolean complete) {
		kStrokeContainers.clear();
		if (complete)
			numHistories = 0;
		paint();
		svgManager.enableBin(false);
	}
	
	public boolean hasCheckConnection() {
		return (eigenaar.comRoot!=null && eigenaar.comRoot.hasListeners("action.check"));
	}
	
	public void setCorrect(boolean correct) {
		if(currentStrokeContainer!=null) {
			currentStrokeContainer.setCorrect(correct);
			if(correct && !correctEquationSend && eigenaar.comRoot.hasListeners("equation.correct")) {
				eigenaar.sendCorrectEquation();
				correctEquationSend = true;
				eigenaar.fireCheck();
			}
		}
		else if(lastCurrentStrokeContainer!=null) {
			lastCurrentStrokeContainer.setCorrect(correct);
			lastCurrentStrokeContainer.scale(schrijfLeesFactor/1.0);
			lastCurrentStrokeContainer.setActive(true);
			currentStrokeContainer = lastCurrentStrokeContainer;
			if(correct && !correctEquationSend && eigenaar.comRoot.hasListeners("equation.correct")) {
				eigenaar.sendCorrectEquation();
				correctEquationSend = true;
				eigenaar.fireCheck();
			}
			lastCurrentStrokeContainer = null;
		}
		closeCurrentContainer();
		addToHistory();
		paint();
	}
	
	public void setFalse(boolean isfalse) {
		if(currentStrokeContainer!=null)
			currentStrokeContainer.setFalse(isfalse);
		else if(lastCurrentStrokeContainer!=null) {
			lastCurrentStrokeContainer.setFalse(isfalse);
			currentStrokeContainer = lastCurrentStrokeContainer;
			currentStrokeContainer.scale(schrijfLeesFactor/1.0);
			currentStrokeContainer.setActive(true);
			lastCurrentStrokeContainer = null;
		}
		svgManager.appendCurrentSC(currentStrokeContainer);
		//paint();
	}
	
	public void setHalf(boolean half) {
		if(currentStrokeContainer!=null)
			currentStrokeContainer.setHalf(half);
		else if(lastCurrentStrokeContainer!=null) {
			lastCurrentStrokeContainer.setHalf(half);
			currentStrokeContainer = lastCurrentStrokeContainer;
			currentStrokeContainer.scale(schrijfLeesFactor/1.0);
			currentStrokeContainer.setActive(true);
			lastCurrentStrokeContainer = null;
		}
		closeCurrentContainer();
		addToHistory();
		paint();
	}

	private void setInSCRow(KStrokeContainer sc) {
		
		KStrokeContainer referenceSC = null;
		for (int i = 0; i < kStrokeContainers.size(); i++) {
			KStrokeContainer ksc = kStrokeContainers.get(i);
			if (ksc != sc && ksc.getBox()!=null && sc.getBox()!=null && ksc.getBox().y < sc.getBox().y && Math.abs(ksc.getBox().x - sc.getBox().x) < 60
					&& Math.abs(sc.getBox().y - (ksc.getBox().y + ksc.getBox().height)) < 100
					&& (referenceSC == null || ksc.getBox().y > referenceSC.getBox().y)) {
				referenceSC = ksc;
			}
		}
		if (referenceSC != null) {
			int dx = referenceSC.getBox().x - sc.getBox().x;
			int dy = referenceSC.getBox().y + referenceSC.getBox().height + 15 - sc.getBox().y;
			sc.translate(dx, dy);
		}
	}
	
	
	public void setDrawOption(boolean drawOption) {
		this.drawOption = drawOption;
	}
	
	public void setFormOption(boolean formOption) {
		this.formOption = formOption;
		if(!formOption)
			KStrokeContainer.setOnlyDrawOption(true);
		else
			KStrokeContainer.setOnlyDrawOption(false);
			
	}
	
	public void setShowWriting(boolean showWriting) {
		this.showWriting = showWriting;
	}
	
	public void setCalculator(boolean calculator) {
		this.calculator = calculator;
	}
	
	public void setInputOption(boolean inputOption) {
		this.inputOption = inputOption;
	}
	
	public void setAreaSetting(Map areaSettings) {
		this.areaSettings = areaSettings;
		ObjectMap launchState = JSONUtilities.wrapMap(areaSettings);
		ObjectList rectangles = launchState.getObjectList("rectangleData");
		int[][] data = new int[rectangles.size()][4];
		for(int i=0 ; i<rectangles.size() ; i++) {
			int[] rectAttr = rectangles.getIntArray(i);
			for(int j=0 ; j<4 ; j++) {
				data[i][j]=rectAttr[j];
			}
		}
		for(int i=0 ; i<data.length ; i++) {
			hiddenSCRectangles.add(new Rectangle(data[i][0],data[i][1],data[i][2],data[i][3]));
			hiddenStrokeContainers.add(new KStrokeContainer(this,new Rectangle(data[i][0],data[i][1],data[i][2],data[i][3])));
		}
	}
	
	public void setGrid(boolean grid) {
		this.ruitjes = grid;
		if(ruitjes)
			svgManager.appendGrid();
	}
	
	public void setScaleWriting(boolean scaleWriting) {
		this.scaleWriting = scaleWriting;
		if(!scaleWriting)
			this.schrijfLeesFactor = 1;
			
	}
	
	public void setWritingScale(double writingScale) {
		this.schrijfLeesFactor = writingScale;
		if(!scaleWriting)
			this.schrijfLeesFactor = 1;
	}
	
	private void addFormulaStrokePoint(Point point) {
		formulaStrokePoints.add(point);
	}
	
	private void clearFormulaStrokePoints() {
		
	}

	public void mouseDownTouch2StartAction(int eventX, int eventY) {
		mouseDownTouchStartAction(eventX, eventY);
		paintFormule(true);
	}

	public void mouseDownTouchStartAction(int eventX, int eventY) {
		eventX = (int) (eventX / scale - translation.x);
		eventY = (int) (eventY / scale - translation.y);

		mouseDown = true;
		startX = eventX;
		startY = eventY;

		if (currentHiddenStrokeContainer == null && currentStrokeContainer == null) {
			currentHiddenStrokeContainer = findHiddenStrokeContainer(eventX, eventY);
			if (currentHiddenStrokeContainer != null) {
				currentHiddenStrokeContainer.setActive(true);
				svgManager.appendCurrentSC(currentHiddenStrokeContainer);
				if (currentHiddenStrokeContainer.getStrokeCount() > 0)
					return;
				currentHiddenStrokeContainer.getWriteBox();
			}
		}
		if (currentHiddenStrokeContainer != null) {
			if (currentHiddenStrokeContainer.getHandleArea().contains(eventX, eventY)) {
				moving = true;
				return;
			}
			if (currentHiddenStrokeContainer.getCloseButtonArea().contains(eventX, eventY)
					&& currentHiddenStrokeContainer.isActive()) {
				eigenaar.sendEquation(activeHSCNumber);
				closeCurrentHiddenContainer();
				svgManager.removeCurrentSC();
				addToHistory();
				paint();
				return;
			}
			if (currentHiddenStrokeContainer.getCheckButtonArea().contains(eventX, eventY)
					&& currentHiddenStrokeContainer.isActive()) {
				eigenaar.sendEquation(activeHSCNumber);
				closeCurrentHiddenContainer();
				svgManager.removeCurrentSC();
				paint();
				eigenaar.fireCheck_n();
				return;
			}
			if (currentHiddenStrokeContainer.getRecognizeButtonArea().contains(eventX, eventY)
					&& currentHiddenStrokeContainer.isActive()) {
				return;
			}
			if (currentHiddenStrokeContainer.getApproxButtonArea().contains(eventX, eventY)
					&& currentHiddenStrokeContainer.isActive()) {
				currentHiddenStrokeContainer.approximate();
				svgManager.appendCurrentSC(currentHiddenStrokeContainer);
				paintFormule(false);
				return;
			}

			if (!currentHiddenStrokeContainer.writeBoxContains(eventX, eventY)) {
				closeCurrentHiddenContainer();
				svgManager.removeCurrentSC();
				// addToHistory();
				paint();
				// eigenaar.setChanged();
			}
			formulaStrokePoints.clear();
			svgManager.removeFormulaStroke();
			formulaStrokePointsDouble.clear();
			mouseDown = true;
			formulaStrokePoints.add(new fi.writemathgwt.client.engine.Point(eventX, eventY));
			svgManager.addFormulaStrokePoint(new DoublePoint(eventX, eventY));
			formulaStrokePointsDouble.add(new DoublePoint(eventX, eventY));
			// paintFormule(true);
			return;
		}

		if (currentStrokeContainer != null && currentStrokeContainer.getHandleArea().contains(eventX, eventY)) {
			if(currentStrokeContainer.recognizeOff)
				movingStrokes = true;
			moving = true;
			return;
		}

		if (currentStrokeContainer != null && currentStrokeContainer.getCloseButtonArea().contains(eventX, eventY)) {
			if (activeHSCNumber > 0)
				eigenaar.sendEquation(activeHSCNumber);
			
			closeCurrentContainer();
			//svgManager.appendStrokeContainers();
			addToHistory();
			paint();
			eigenaar.fireStrokeCodes();
			return;
		}

		if (currentStrokeContainer != null && currentStrokeContainer.getEraserButtonArea().contains(eventX, eventY)) {
			currentStrokeContainer.setEraserActive(true);
			svgManager.enablePenButtonSVG(false);
			svgManager.enableEraserButtonSVG(true);
			paintFormule(false);
			return;
		}

		if (currentStrokeContainer != null && currentStrokeContainer.getPenButtonArea().contains(eventX, eventY)) {
			currentStrokeContainer.setEraserActive(false);
			svgManager.enablePenButtonSVG(true);
			svgManager.enableEraserButtonSVG(false);
			paintFormule(false);
			return;
		}

		if (currentStrokeContainer != null && currentStrokeContainer.getBinButtonArea().contains(eventX, eventY)) {
			currentStrokeContainer.wis();
			svgManager.appendCurrentSC(currentStrokeContainer);
			paintFormule(false);
			return;
		}

		if (currentStrokeContainer != null && currentStrokeContainer.getUndoButtonArea().contains(eventX, eventY)) {
			currentStrokeContainer.undo();
			svgManager.appendCurrentSC(currentStrokeContainer);
			paintFormule(false);
			return;
		}

		if (currentStrokeContainer != null && currentStrokeContainer.getFormulaArea().contains(eventX, eventY)) {
			currentStrokeContainer.setFormuleModus(true);
			proActiveStrokeContainer = null;
			if (activeHSCNumber > 0)
				eigenaar.sendEquation(activeHSCNumber);
			closeCurrentContainer();
			//svgManager.appendStrokeContainers();
			addToHistory();
			paint();
			eigenaar.fireStrokeCodes();
			return;

		}

		if (currentStrokeContainer != null && currentStrokeContainer.getRecognizeButtonArea().contains(eventX, eventY)
				&& !(currentStrokeContainer.isCorrect() || currentStrokeContainer.isFalse()
						|| currentStrokeContainer.isHalf())) {
			currentStrokeContainer.setRecognizeOff(false);
			svgManager.appendCurrentSC(currentStrokeContainer);
			paint();
			return;
		}

		if (drawOption && currentStrokeContainer != null
				&& currentStrokeContainer.getNotRecognizeButtonArea().contains(eventX, eventY)
				&& !(currentStrokeContainer.isCorrect() || currentStrokeContainer.isFalse()
						|| currentStrokeContainer.isHalf())) {
			currentStrokeContainer.setRecognizeOff(true);
			svgManager.appendCurrentSC(currentStrokeContainer);
			paintFormule(false);
			return;
		}

		if (currentStrokeContainer != null && currentStrokeContainer.getEraserActive()
				&& currentStrokeContainer.writeBoxContains(eventX, eventY)) {
			currentStrokeContainer.eraseStrokes(eventX, eventY);
			currentStrokeContainer.setErasing(true, eventX, eventY);
			svgManager.appendCurrentSC(currentStrokeContainer);
			paintFormule(false);
			return;
		}

		if (currentStrokeContainer != null && currentStrokeContainer.getCheckButtonArea().contains(eventX, eventY)) {
			correctEquationSend = false;
			if (activeHSCNumber > 0) {
				eigenaar.sendEquation(activeHSCNumber);
				closeCurrentContainer();
				paint();
				eigenaar.fireCheck_n();
			} else
				eigenaar.fireCheck();
			return;
		}

		if (currentStrokeContainer != null && currentStrokeContainer.getApproxButtonArea().contains(eventX, eventY)) {
			currentStrokeContainer.approximate();
			svgManager.appendCurrentSC(currentStrokeContainer);
			paintFormule(false);
			return;
		}

		if (currentStrokeContainer != null && currentStrokeContainer.getHeaderArea().contains(eventX, eventY)) {
			return;
		}

		if (currentStrokeContainer != null && !currentStrokeContainer.writeBoxContains(eventX, eventY)) {
			closeCurrentContainer();
			addToHistory();
			paint();
			eigenaar.setChanged();
		}

		if (currentStrokeContainer == null && getBinArea().contains(eventX, eventY)) {
			kStrokeContainers.clear();
			svgManager.removeStrokeContainers();
			addToHistory();
			svgManager.updateBin();
			svgManager.updateUndo();
			eigenaar.setChanged();
		}

		if (currentStrokeContainer == null && getUndoArea().contains(eventX, eventY)) {
			undo();
			eigenaar.setChanged();
		}
		if (currentHiddenStrokeContainer == null)
			proActiveStrokeContainer = findInactiveStrokeContainer(eventX, eventY);

		if (currentStrokeContainer == null && proActiveStrokeContainer != null) {

			proActiveX = proActiveStrokeContainer.getBox().x;
			proActiveY = proActiveStrokeContainer.getBox().y;
			proActiveStrokeContainer.setProActive(true);
			svgManager.startTranslateSC(proActiveStrokeContainer);
			paint();
			paintFormule(true);
			return;
		}

		if ((currentStrokeContainer == null && currentHiddenStrokeContainer == null
				|| !currentStrokeContainer.writeBoxContains(eventX, eventY)) && proActiveStrokeContainer == null) {
			currentStrokeContainer = new KStrokeContainer(this);
			currentStrokeContainer.setActive(true);
			kStrokeContainers.add(currentStrokeContainer);
			svgManager.enableBin(true);
			//svgManager.enableUndo(true);
		}

		proActiveStrokeContainer = null;
		formulaStrokePoints.clear();
		formulaStrokePointsDouble.clear();
		mouseDown = true;
		formulaStrokePoints.add(new fi.writemathgwt.client.engine.Point(eventX, eventY));
		formulaStrokePointsDouble.add(new DoublePoint(eventX, eventY));
		paintFormule(true);

	}

	public void mouseMoveTouch2MoveAction(int eventX, int eventY) {
		if (currentStrokeContainer != null) {
			formulaStrokePoints.clear();
			formulaStrokePointsDouble.clear();
			int dx = eventX - startX;
			int dy = eventY - startY;
			currentStrokeContainer.translate(dx, dy);
			activeTranslation.x += dx;
			activeTranslation.y += dy;
			paintFormule(true);
			startX = eventX;
			startY = eventY;
		} 
		else if (currentHiddenStrokeContainer != null) {
			formulaStrokePoints.clear();
			formulaStrokePointsDouble.clear();
			int dx = eventX - startX;
			int dy = eventY - startY;
			currentHiddenStrokeContainer.translate(dx, dy);
			currentHiddenStrokeContainer.getDefaultBox().translate(dx, dy);
			activeTranslation.x += dx;
			activeTranslation.y += dy;
			paintFormule(true);
			startX = eventX;
			startY = eventY;
		}
	}

	public void mouseMoveTouchMoveAction(int eventX, int eventY, boolean shiftPressed) {
		eventX = (int) (eventX / scale - translation.x);
		eventY = (int) (eventY / scale - translation.y);

		if (!mouseDown)
			return;

		if (proActiveStrokeContainer != null) {
			int dx = eventX - startX;
			int dy = eventY - startY;
			proActiveStrokeContainer.translate(dx, dy);
			//int nr = kStrokeContainers.indexOf(proActiveStrokeContainer);
			//svgManager.translateSC(nr, dx, dy);
			svgManager.translateSC(proActiveStrokeContainer, dx, dy);
			startX = eventX;
			startY = eventY;
			paintFormule(true);
		} 
		else if (moving) {
			if (currentStrokeContainer != null) {
				formulaStrokePoints.clear();
				formulaStrokePointsDouble.clear();
				int dx = eventX - startX;
				int dy = eventY - startY;
				
				if(movingStrokes)
					svgManager.translateCurrentSCStrokes(dx,dy);
				else {
					svgManager.translateCurrentSC(dx,dy);
				}
				currentStrokeContainer.translate(dx, dy);
				activeTranslation.x += dx;
				activeTranslation.y += dy;
				paintFormule(true);
				
				//svgManager.appendCurrentSC(currentStrokeContainer);
				startX = eventX;
				startY = eventY;
			} else if (currentHiddenStrokeContainer != null) {
				formulaStrokePoints.clear();
				formulaStrokePointsDouble.clear();
				int dx = eventX - startX;
				int dy = eventY - startY;
				currentHiddenStrokeContainer.translate(dx, dy);
				currentHiddenStrokeContainer.getDefaultBox().translate(dx, dy);
				svgManager.translateCurrentSC(dx,dy);
				activeTranslation.x += dx;
				activeTranslation.y += dy;
				paintFormule(true);
				startX = eventX;
				startY = eventY;
			}
		} 
		else if (formulaStrokePoints.size() > 0) {
			DoublePoint lastPoint = formulaStrokePointsDouble.get(formulaStrokePointsDouble.size() - 1);
			double xD = 0.5 * (lastPoint.x + eventX);
			double yD = 0.5 * (lastPoint.y + eventY);
			
			double dx = xD - lastPoint.x;
			double dy = yD - lastPoint.y;
			double dd = dx*dx+dy*dy;
			
			//if(dd>10 || formulaStrokePointsDouble.size()==1) {
				formulaStrokePointsDouble.add(new DoublePoint(xD, yD));
				svgManager.addFormulaStrokePoint(new DoublePoint(xD, yD));
				paintLastSegment();
//			}
//			else {
//				//formulaStrokePointsDouble.add(new DoublePoint(xD, yD));
//				svgManager.replaceFormulaStrokePoint(new DoublePoint(xD, yD));
//				//paintLastSegment();
//			}
			
			
			
			
			
			
			
		}
	}

	public void mouseUpTouchEndAction(int eventX, int eventY) {
		//movingStrokes = false;
		if (currentHiddenStrokeContainer != null) {
			if (formulaStrokePointsDouble.size() > 0) {
				double[] x = new double[formulaStrokePointsDouble.size()];
				double[] y = new double[formulaStrokePointsDouble.size()];
				for (int i = 0; i < x.length; i++) {
					x[i] = formulaStrokePointsDouble.get(i).x;
					y[i] = formulaStrokePointsDouble.get(i).y;
				}
				logger.info("hidden make stroke");
				Stroke strokeNew = new Stroke(x, y);
				if (strokeNew.getParsePointsbox().getDiagonal() > 15
						|| currentHiddenStrokeContainer.getStrokeCount() > 0) {
					currentHiddenStrokeContainer.addStroke(strokeNew);
					currentHiddenStrokeContainer.setCorrect(false);
					currentHiddenStrokeContainer.setFalse(false);
					currentHiddenStrokeContainer.setHalf(false);
					svgManager.appendCurrentSC(currentHiddenStrokeContainer);
					if (currentHiddenStrokeContainer.getStrokeCount() == 1)
						paint();
				}
			}

			if (currentHiddenStrokeContainer.isNotRelevant()) { // currentStrokeContainer.getStrokeCount()==1 &&
				currentHiddenStrokeContainer.wis();
				formulaStrokePoints.clear();
				svgManager.removeFormulaStroke();
				formulaStrokePointsDouble.clear();
				eigenaar.sendEquation(activeHSCNumber);
				paint();
				paintFormule(true);
				return;
			}

			formulaStrokePoints.clear();
			svgManager.removeFormulaStroke();
			formulaStrokePointsDouble.clear();

			if(moving) {
				svgManager.appendCurrentSC(currentHiddenStrokeContainer);
				moving = false;
			}
			
			paintFormule(true);
			if (currentHiddenStrokeContainer == null)
				eigenaar.sendDrawing();
			else 
				eigenaar.sendEquation(activeHSCNumber);
			return;
		}

		if (proActiveStrokeContainer != null) {
			if (eventX > breedte - 60 && eventY < 60 || proActiveStrokeContainer.getBox().x > breedte
					|| proActiveStrokeContainer.getBox().y > hoogte) {
				kStrokeContainers.remove(proActiveStrokeContainer);
				svgManager.removeStrokeContainer(proActiveStrokeContainer);
				//svgManager.appendStrokeContainers();
				proActiveStrokeContainer = null;
				addToHistory();
				paint();
				return;
			}
			int pX = proActiveStrokeContainer.getBox().x - proActiveX;
			int pY = proActiveStrokeContainer.getBox().y - proActiveY;

			boolean nietVerschoven = pX * pX + pY * pY < 16;
			if (nietVerschoven) {
				currentStrokeContainer = proActiveStrokeContainer;
				currentStrokeContainer.scale(schrijfLeesFactor / 1.0);
				currentStrokeContainer.setActive(true);
				svgManager.appendCurrentSC(currentStrokeContainer);
				currentStrokeContainer.setNr(kStrokeContainers.indexOf(currentStrokeContainer));

			}
			proActiveStrokeContainer.setProActive(false);
			svgManager.stopTranslateSC(proActiveStrokeContainer);
			proActiveStrokeContainer = null;
			addToHistory();
			paint();
		}

		if (currentStrokeContainer != null && formulaStrokePoints.size() > 0) {
			double[] x = new double[formulaStrokePointsDouble.size()];
			double[] y = new double[formulaStrokePointsDouble.size()];
			for (int i = 0; i < x.length; i++) {
				x[i] = formulaStrokePointsDouble.get(i).x;
				y[i] = formulaStrokePointsDouble.get(i).y;
			}
			Stroke stroke = new Stroke(x, y);
			
			if (drawOption && (stroke.getParsePointsbox().height > 200
					|| currentStrokeContainer.getStrokeCount() == 0 && stroke.getParsePointsbox().width > 200))
				currentStrokeContainer.setRecognizeOff(true);

			currentStrokeContainer.addStroke(stroke);
			currentStrokeContainer.setCorrect(false);
			currentStrokeContainer.setFalse(false);
			currentStrokeContainer.setHalf(false);
			//svgManager.appendCurrentSC(currentStrokeContainer);
			if(currentStrokeContainer.recognizeOff && currentStrokeContainer.getStrokeCount() > 1)
				svgManager.addStrokeCurrentSC(stroke);
			else
				svgManager.appendCurrentSC(currentStrokeContainer);
			if (currentStrokeContainer.getStrokeCount() == 1) {
				//svgManager.appendCurrentSC(currentStrokeContainer);
				paint();
			}
		}

		if (currentStrokeContainer != null && currentStrokeContainer.isNotRelevant()
				&& !currentStrokeContainer.getRecognizeOff()) {
			kStrokeContainers.remove(currentStrokeContainer);
			svgManager.removeStrokeContainer(currentStrokeContainer);
			//svgManager.appendStrokeContainers();
			svgManager.removeCurrentSC();
			currentStrokeContainer = null;
			eigenaar.fireStrokeCodes();
			formulaStrokePoints.clear();
			svgManager.removeFormulaStroke();
			formulaStrokePointsDouble.clear();
			paint();
			paintFormule(true);
			return;
		}

		if (currentStrokeContainer != null)
			currentStrokeContainer.setErasing(false, eventX, eventY);

		formulaStrokePoints.clear();
		svgManager.removeFormulaStroke();
		formulaStrokePointsDouble.clear();
		
		if(movingStrokes) {
			svgManager.stopTranslateSCStrokes();
			movingStrokes = false;
		}
		if(moving) {
			svgManager.appendCurrentSC(currentStrokeContainer);
			moving = false;
		}
		//
		paintFormule(true);
		if (currentStrokeContainer == null)
			eigenaar.sendDrawing();
		else
			eigenaar.sendEquation();
	}

	private boolean mouseOnRight = false;
	private boolean hasPointerEventSupport;
	private int startX, startY;
	private boolean mouseDown;

	class MouseHandler implements MouseDownHandler, MouseMoveHandler, MouseUpHandler {

		public void onMouseDown(MouseDownEvent e) {
			e.preventDefault();
			e.stopPropagation();

			if (hasPointerEventSupport)
				return;

			int eventX = e.getX();
			int eventY = e.getY();

			if (e.getNativeButton() == NativeEvent.BUTTON_RIGHT) {
				mouseOnRight = true;
				startX = eventX;
				startY = eventY;
				mouseDownTouch2StartAction(eventX, eventY);
				return;
			}
			mouseDownTouchStartAction(eventX, eventY);
		}

		public void onMouseMove(MouseMoveEvent e) {
			e.preventDefault();
			e.stopPropagation();

			if (hasPointerEventSupport)
				return;

			if (!mouseDown)
				return;

			int eventX = e.getX();
			int eventY = e.getY();
			boolean shiftPressed = e.isShiftKeyDown();

			if (mouseOnRight)
				mouseMoveTouch2MoveAction(eventX, eventY);
			else
				mouseMoveTouchMoveAction(eventX, eventY, shiftPressed);
		}

		public void onMouseUp(MouseUpEvent e) {
			e.preventDefault();
			e.stopPropagation();

			if (hasPointerEventSupport)
				return;

			int eventX = e.getX();
			int eventY = e.getY();

			e.preventDefault();
			e.stopPropagation();
			mouseDown = false;
			mouseOnRight = false;
			if (e.getNativeButton() == NativeEvent.BUTTON_RIGHT) {
				return;
			}
			mouseUpTouchEndAction(eventX, eventY);
		}
	}

	class TouchHandler implements TouchStartHandler, TouchMoveHandler, TouchEndHandler {
		int lastTouchX = 0;
		int lastTouchY = 0;

		public void onTouchStart(TouchStartEvent e) {
			e.stopPropagation();
			e.preventDefault();

			if (hasPointerEventSupport)
				return;

			if (e.getTouches().length() == 0)
				return;

			Touch touch = e.getTouches().get(0);

			int eventX = touch.getPageX() - mathScratchCanvas.getAbsoluteLeft();
			int eventY = touch.getPageY() - mathScratchCanvas.getAbsoluteTop();

			if ((e.getTouches().length() == 2)) {
				moving = true;
				writing = false;
				startX = eventX;
				startY = eventY;
				mouseDownTouch2StartAction(eventX, eventY);
				return;
			}
			if (e.getTouches().length() == 1 && !moving) {
				writing = true;
				mouseDownTouchStartAction(eventX, eventY);
			}
			if ((e.getTouches().length() > 2)) {
				moving = false;
				writing = false;
			}

			e.preventDefault();
			e.stopPropagation();
		}

		public void onTouchMove(TouchMoveEvent e) {
			e.stopPropagation();
			e.preventDefault();

			if (hasPointerEventSupport)
				return;

			if (e.getTouches().length() == 1) {
				Touch touch = e.getTouches().get(0);

				boolean shiftPressed = false;
				int eventX = touch.getPageX() - mathScratchCanvas.getAbsoluteLeft();
				int eventY = touch.getPageY() - mathScratchCanvas.getAbsoluteTop();
				lastTouchX = eventX;
				lastTouchY = eventY;

				mouseMoveTouchMoveAction(eventX, eventY, shiftPressed);
			}
			if (moving && e.getTouches().length() == 2) {
				Touch touch = e.getTouches().get(0);

				boolean shiftPressed = false;
				int eventX = touch.getPageX() - mathScratchCanvas.getAbsoluteLeft();
				int eventY = touch.getPageY() - mathScratchCanvas.getAbsoluteTop();

				mouseMoveTouch2MoveAction(eventX, eventY);
			}
			e.preventDefault();
			e.stopPropagation();
		}

		public void onTouchEnd(TouchEndEvent e) {
			e.preventDefault();
			e.stopPropagation();

			if (hasPointerEventSupport)
				return;

			//moving = false;
			mouseUpTouchEndAction(lastTouchX, lastTouchY);
		}
	}

	class PointerHandler implements PointerUpHandler, PointerDownHandler, PointerMoveHandler, PointerCancelHandler,
			com.google.gwt.event.shared.EventHandler {
		int lastTouchX = 0;
		int lastTouchY = 0;

		int touchCount = 0;

		ArrayList<PointerEvent> events;

		@Override
		public void onPointerCancel(PointerCancelEvent e) {
			e.stopPropagation();
			e.preventDefault();

			touchCount--;
		}

		@Override
		public void onPointerMove(PointerMoveEvent e) {
			e.stopPropagation();
			e.preventDefault();
			boolean shiftPressed = false;
			int eventX = e.getRelativeX(mathScratchCanvas.getElement());
			int eventY = e.getRelativeY(mathScratchCanvas.getElement());
			lastTouchX = eventX;
			lastTouchY = eventY;

			mouseMoveTouchMoveAction(eventX, eventY, shiftPressed);

			e.preventDefault();
			e.stopPropagation();
		}

		@Override
		public void onPointerDown(PointerDownEvent e) {
			e.stopPropagation();
			e.preventDefault();

			hasPointerEventSupport = true;

			int eventX = e.getRelativeX(mathScratchCanvas.getElement());
			int eventY = e.getRelativeY(mathScratchCanvas.getElement());

			if (!moving) {
				writing = true;
				mouseDownTouchStartAction(eventX, eventY);
				lastTouchX = eventX;
				lastTouchY = eventY;
			}

			e.preventDefault();
			e.stopPropagation();
		}

		@Override
		public void onPointerUp(PointerUpEvent e) {
			e.stopPropagation();
			e.preventDefault();

			//moving = false;
			mouseUpTouchEndAction(lastTouchX, lastTouchY);

			e.preventDefault();
			e.stopPropagation();
		}
	}
	
	private static SVGImage invisible; 
	static {
		OMSVGDocument document = OMSVGParser.currentDocument();
		OMSVGSVGElement svg = document.createSVGSVGElement();
		invisible = new SVGImage(svg);
		invisible.setPixelSize(1, 1);
		svg.setHeight(Unit.PX, 1);
		svg.setWidth(Unit.PX, 1);
		svg.setViewBox(0, 0, 1, 1);
		invisible.getStyle().setVisibility(Visibility.HIDDEN);
		final RootLayoutPanel root = RootLayoutPanel.get();
		root.add(invisible);
		root.setWidgetBottomHeight(invisible, 0, Unit.PX, 1, Unit.PX);
		root.setWidgetRightWidth(invisible, 0, Unit.PX, 1, Unit.PX);
	}
	
	public class FontMetrics {
		private float width;
		private float height;
		
		
		
		public FontMetrics(String text) {
			OMSVGTextElement t = new OMSVGTextElement(0, 0, OMSVGLength.SVG_LENGTHTYPE_NUMBER, text);
			t.getStyle().setFontSize(fontSize , Unit.PX);
			t.getStyle().setFontStyle(fontStyle);
			t.getStyle().setFontWeight(fontWeight);
			t.getStyle().setSVGProperty(SVGConstants.CSS_FONT_FAMILY_PROPERTY, fontFamily);
			t.getStyle().setWhiteSpace(WhiteSpace.PRE);
			t.setXmlspace(SVGConstants.SVG_PRESERVE_VALUE);
			
			OMSVGElement svg = invisible.getSvgElement();
			svg.appendChild(t);
			OMSVGRect r = t.getBBox();
			this.width = r.getWidth();
			this.height = r.getHeight();
		}
		public float getWidth() { return width; }
		public float getHeight() { return height; }
	}
}
