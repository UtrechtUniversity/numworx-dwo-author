package fi.ivmdrawgwt.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Touch;
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

import nl.uu.fi.dwo.interaction.client.JSONUtilities;
import nl.uu.fi.dwo.interaction.client.json.ObjectMap;

//import javax.sound.sampled.Line;


public class IVMdrawGWTField {
	Logger logger = Logger.getLogger("SiardDebugLogger");

	final int GAUSSIAN = 0;
	final int AVERAGE = 1;
	final int AVERAGE2 = 2;
	
	int smoothType = AVERAGE;
	
	public IVMdrawGWT owner;

	public Canvas ivmDrawGWTCanvas, backgroundCanvas;//, strokeContainerCanvas
	public Context2d gIm, backgroundgIm, strokeContainergIm;

	int breedte, hoogte;
	
	ArrayList<Point> formulaStrokePoints = new ArrayList<Point>();

	private boolean writing;
	private boolean moving;
	int startX, startY;
	boolean mouseDown;

	ArrayList<Point> allDrawnPoints = new ArrayList<>();
	private IVMStrokeContainer currentStrokeContainer;
	private IVMStrokeContainer jarStrokeContainer;
	private Stroke lastStroke;
	
	
	public IVMdrawGWTField(int w, int h, IVMdrawGWT owner) {
		this.owner = owner;
		
		ivmDrawGWTCanvas = Canvas.createIfSupported();
		backgroundCanvas = Canvas.createIfSupported();
		//strokeContainerCanvas = Canvas.createIfSupported();

		setSize(w, h);
		
		MouseHandler mouseHandler = new MouseHandler();
		ivmDrawGWTCanvas.addMouseDownHandler(mouseHandler);
		ivmDrawGWTCanvas.addMouseMoveHandler(mouseHandler);
		ivmDrawGWTCanvas.addMouseUpHandler(mouseHandler);
		
		MGWTTouchHandler touchHandler = new MGWTTouchHandler();
		ivmDrawGWTCanvas.addTouchStartHandler(touchHandler);
		ivmDrawGWTCanvas.addTouchMoveHandler(touchHandler);
		ivmDrawGWTCanvas.addTouchEndHandler(touchHandler);
		
		jarStrokeContainer = new IVMStrokeContainer();
		jarStrokeContainer.setIsJar(true);
		currentStrokeContainer = new IVMStrokeContainer();
	}
	
	public Canvas getCanvas() {
		return ivmDrawGWTCanvas;
	}
	
	public void initContext2d() {
		gIm = ivmDrawGWTCanvas.getContext2d();
		backgroundgIm = backgroundCanvas.getContext2d();
	}
	
	public void setState(Map<String, Object> map) {
		if(map == null || map.isEmpty())
			return;
		ObjectMap launchState = JSONUtilities.wrapMap(map);
		
		Map<String,Object> ivmStrokeContainer = new HashMap<String,Object>();
		
		if (launchState.containsKey("ivmStrokeContainer"))
			ivmStrokeContainer = launchState.getMap("ivmStrokeContainer");
		currentStrokeContainer = new IVMStrokeContainer();
		try {
			currentStrokeContainer.setState(ivmStrokeContainer);
		}
		catch(Exception e) {
		}
		lastStroke = currentStrokeContainer.getLastStroke();
		processIVM();
		paint();
	}
	
	public HashMap<String,Object> getState() {
		HashMap<String,Object> h = new HashMap<String,Object>();
		
		Map<String,Object> ivmStrokeContainer = new HashMap <String,Object>();
		ivmStrokeContainer = currentStrokeContainer.getState();
		h.put("ivmStrokeContainer", ivmStrokeContainer);
		
		return h;
	}
	
	public void paint()	{
		paint(gIm);
	}
	
	public void paint(Context2d g) {
		g.clearRect(0, 0, breedte, hoogte);
		g.setStrokeStyle(CssColor.make(80, 80, 80));

		jarStrokeContainer.draw(g);
		
		g.setLineWidth(1.0d);
		currentStrokeContainer.draw(g);
		if (formulaStrokePoints.size() == 1)
		{	Point p =  formulaStrokePoints.get(0);
			g.strokeRect(p.x, p.y, 1, 1);
		}
		if (formulaStrokePoints.size() > 1)
		{	
			Point p1 = formulaStrokePoints.get(0);
			g.beginPath();
			g.moveTo(p1.x, p1.y);
			for (int pCnt = 1; pCnt < formulaStrokePoints.size(); pCnt++)
			{	Point p2 = formulaStrokePoints.get(pCnt);
				g.lineTo(p2.x, p2.y);
				p1 = p2;
			}

			g.stroke();
		}
	}


	/**
	 * Build the jar by using three ArrayList. A drawn jar will always have a bottom and a left
	 * and right side. These strokes will be stored in their corresponding ArrayList.
	 */
	private void processIVM() {
		if(lastStroke==null)
			return;
		jarStrokeContainer.clear();
		ArrayList<DoublePoint> pointsLeft = new ArrayList<DoublePoint>();
		ArrayList<DoublePoint> pointsRight = new ArrayList<DoublePoint>();
		ArrayList<DoublePoint> pointsBottom = new ArrayList<DoublePoint>();
		double vaasX = 500;
		double volumeUnit = 20*lastStroke.getParsePointsbox().height;

		ArrayList<DoublePoint> points = new ArrayList<DoublePoint>();

		for(int j = 0 ; j < lastStroke.getParsePoints().size() ; j++) {
			points.add(new DoublePoint(lastStroke.getParsePoints().get(j).x, lastStroke.getParsePoints().get(j).y));
		}

		points = smooth(points, smoothType);
		double vaasY = lastStroke.getParsePoints().get(0).y;
		
		for(int j = 1 ; j < points.size() ; j++) {
			double dx = points.get(j).x - points.get(j-1).x ;
			double dy = points.get(j).y - points.get(j-1).y;
			
			double r = Math.sqrt(-volumeUnit*dx/dy);
			pointsLeft.add(new DoublePoint(vaasX-r, points.get(j).y));
			pointsRight.add(new DoublePoint(vaasX+r, points.get(j).y));
			pointsBottom.add(pointsLeft.get(0));
			pointsBottom.add(pointsRight.get(0));
		}

		ArrayList<DoublePoint> smoothedPointsLeft = smooth(pointsLeft, smoothType);
		ArrayList<DoublePoint> smoothedPointsRight = smooth(pointsRight, smoothType);
		
		Stroke streepLeft = new Stroke(smoothedPointsLeft,true);
		Stroke streepRight = new Stroke(smoothedPointsRight,true);
		Stroke streepBottom = new Stroke(pointsBottom,true);
		
		jarStrokeContainer.addStroke(streepLeft);
		jarStrokeContainer.addStroke(streepRight);
		jarStrokeContainer.addStroke(streepBottom);
		
		paint();
	}
	
	public ArrayList<DoublePoint> smooth(ArrayList<DoublePoint> doublePoints, int smoothType) {
		if (smoothType == GAUSSIAN)
			return gaussianSmooth(doublePoints);
		else if (smoothType == AVERAGE)
			return averageSmooth(doublePoints);
		else if (smoothType == AVERAGE2)
		{	//ArrayList<DoublePoint> oneSmooth = averageSmooth(doublePoints);
			return averageSmooth(doublePoints);			
		}
		else
			return doublePoints;
	}
	
	public ArrayList<DoublePoint> averageSmooth(ArrayList<DoublePoint> doublePoints) {
		if (doublePoints.size() < 5) 
			return doublePoints;
		ArrayList<DoublePoint> pointsNew = new ArrayList<DoublePoint>();
		pointsNew.add(doublePoints.get(0));		
		pointsNew.add(doublePoints.get(1));
		for (int i = 2; i < doublePoints.size() - 2; i++)
		{
			DoublePoint pOld0 = doublePoints.get(i-2);
			DoublePoint pOld1 = doublePoints.get(i-1);
			DoublePoint pOld2 = doublePoints.get(i);
			DoublePoint pOld3 = doublePoints.get(i+1);
			DoublePoint pOld4 = doublePoints.get(i+2);
			
			DoublePoint smoothedPoint = new DoublePoint(pOld0.x/5 + pOld1.x/5 + pOld2.x/5 + pOld3.x/5 + pOld4.x/5,
														pOld0.y/5 + pOld1.y/5 + pOld2.y/5 + pOld3.y/5 + pOld4.y/5);
			pointsNew.add(smoothedPoint);
		}
		pointsNew.add(doublePoints.get(doublePoints.size() - 1));
		
		return pointsNew;
	}
	
	public ArrayList<DoublePoint> gaussianSmooth(ArrayList<DoublePoint> doublePoints) {
		if (doublePoints.size() < 3) 
			return doublePoints;
		ArrayList<DoublePoint> pointsNew = new ArrayList<DoublePoint>();
		pointsNew.add(doublePoints.get(0));		
		for (int i = 1; i < doublePoints.size() - 1; i++)
		{
			DoublePoint pOld0 = doublePoints.get(i-1);
			DoublePoint pOld1 = doublePoints.get(i);
			DoublePoint pOld2 = doublePoints.get(i+1);
			DoublePoint smoothedPoint = new DoublePoint(pOld0.x / 4 + pOld1.x / 2 + pOld2.x / 4,
														pOld0.y / 4 + pOld1.y / 2 + pOld2.y / 4);
			pointsNew.add(smoothedPoint);
		}
		pointsNew.add(doublePoints.get(doublePoints.size() - 1));
		
		return pointsNew;
		
	}
	
	void setSize(int w, int h) 
	{
		breedte = w;
		hoogte = h;
		ivmDrawGWTCanvas.setWidth(w + "px");
		ivmDrawGWTCanvas.setHeight(h + "px");
		ivmDrawGWTCanvas.setCoordinateSpaceWidth(w);
		ivmDrawGWTCanvas.setCoordinateSpaceHeight(h);
		backgroundCanvas.setWidth(w + "px");
		backgroundCanvas.setHeight(h + "px");
		backgroundCanvas.setCoordinateSpaceWidth(w);
		backgroundCanvas.setCoordinateSpaceHeight(h);
//		strokeContainerCanvas.setWidth(w + "px");
//		strokeContainerCanvas.setHeight(h + "px");
//		strokeContainerCanvas.setCoordinateSpaceWidth(w);
//		strokeContainerCanvas.setCoordinateSpaceHeight(h);
	}
	
	
	public void mouseDownTouchStartAction(int eventX, int eventY) {
		//if(currentStrokeContainer==null)
			currentStrokeContainer = new IVMStrokeContainer();
		
		formulaStrokePoints.clear();
		mouseDown = true;
		formulaStrokePoints.add(new Point(eventX, eventY));
		paint();
	}
	
	public void mouseMoveTouchMoveAction(int eventX, int eventY, boolean shiftPressed) {
//		eventX = (int)(eventX/scale -translation.x);
//		eventY = (int)(eventY/scale -translation.y);

		if (!mouseDown)
			return;

		this.allDrawnPoints.add(new Point(eventX, eventY));


		if(formulaStrokePoints.size()>0) {
				int dx = (int)(formulaStrokePoints.get(formulaStrokePoints.size()-1).x) - eventX;
				int dy = (int)(formulaStrokePoints.get(formulaStrokePoints.size()-1).y) - eventY;
				if(dx*dx+dy*dy>30)
					formulaStrokePoints.add(new Point(eventX, eventY));
		}
		paint();
		
	}


	public void addPopUpCanvas(LineData option, IVMdrawGWT owner, int widthMargin, int optionNr) {
		Canvas canvas = Canvas.createIfSupported();
		Context2d context = canvas.getContext2d();

		canvas.setWidth(200 + "px");
		canvas.setHeight(200 + "px");
		canvas.setCoordinateSpaceHeight(100);
		canvas.setCoordinateSpaceWidth(100);

		if (optionNr == 1) {
			// call function 1
			canvas.addMouseDownHandler((a) ->logger.log(Level.SEVERE, "drawn boi"));

		} else {
			// call function 2
			canvas.addMouseDownHandler((a) ->logger.log(Level.SEVERE, "drawn boi2"));
		}

		owner.mainPanel.add(canvas);
		owner.mainPanel.setWidgetLeftWidth(canvas, widthMargin, Style.Unit.EM, 30, Style.Unit.EM);
		owner.mainPanel.setWidgetTopHeight(canvas, 8, Style.Unit.EM, 30, Style.Unit.EM);

		option.drawnInContext(context, 100, 100);
	}


	public void createPopUpWindow(Classifier classifier) {
		IVMdrawGWT owner = this.owner;

		owner.mainPanel.add(owner.popUp);
		owner.mainPanel.setWidgetTopHeight(owner.popUp, 5, Style.Unit.EM, 20, Style.Unit.EM);
		owner.mainPanel.setWidgetLeftWidth(owner.popUp, 5, Style.Unit.EM, 45, Style.Unit.EM);
		owner.popUp.setVisible(true);

		addPopUpCanvas(new LineData(classifier.unclearPoints1), owner, 9, 1);
		addPopUpCanvas(new LineData(classifier.unclearPoints2), owner, 30, 2);
	}


	public void handleClassification(LineData inputPoints) {
		String feedback;
		String color = "white";

		if (inputPoints.validInput()) {
			Matrix mPoints = new Matrix(inputPoints.getXs(), inputPoints.getYs());
			Classifier classifier = new Classifier(mPoints, 2, true);

			if (classifier.unclear()) {
//				this.createPopUpWindow(classifier);
			}

			feedback = classifier.getFeedback();

			if (classifier.classify()) {
				color = "#33cc33";
			} else {
				color = "white";
			}

		} else {
			feedback = Feedback.decreasingLine();
		}

		this.owner.ivmFeedbackGWTField.mouseUpEvent(feedback);
		this.owner.ivmFeedbackGWTField.owner.label.getElement().getStyle().setBackgroundColor(color);
	}


	public void mouseUpTouchEndAction(int eventX, int eventY) {
		LineData inputPoints = new LineData(this.allDrawnPoints);
		handleClassification(inputPoints);

		this.allDrawnPoints.clear();

		currentStrokeContainer.clear();
		lastStroke = new Stroke(formulaStrokePoints);
		currentStrokeContainer.addStroke(lastStroke);
		formulaStrokePoints.clear();

		if (!inputPoints.validInput()) {
			return;
		}

		processIVM();
		paint();
	}
	
	
	class MouseHandler implements MouseDownHandler, MouseMoveHandler, MouseUpHandler
	{
		
		public void onMouseDown(MouseDownEvent e)
		{
			e.preventDefault();
			// prevent scrolling 
			e.stopPropagation();
			
			int eventX = e.getX();
			int eventY = e.getY();
			
			
			mouseDownTouchStartAction(eventX, eventY);
		}


		public void onMouseMove(MouseMoveEvent e)	
		{
			e.preventDefault();
			// prevent scrolling
			e.stopPropagation();

			if (!mouseDown)
				return;

			int eventX = e.getX();
			int eventY = e.getY();
			boolean shiftPressed = e.isShiftKeyDown();
			
			mouseMoveTouchMoveAction(eventX, eventY, shiftPressed);
			
		} // onMouseMove
		
		public void onMouseUp(MouseUpEvent e)	
		{
			int eventX = e.getX();
			int eventY = e.getY();
			
			e.preventDefault();
			// prevent scrolling
			e.stopPropagation();
			mouseDown = false;
			if (e.getNativeButton() == NativeEvent.BUTTON_RIGHT) {
				return;
			}
			mouseUpTouchEndAction(eventX, eventY);
		}

	}


	
	class MGWTTouchHandler implements TouchStartHandler, TouchMoveHandler, TouchEndHandler
	{
		int lastTouchX = 0;
		int lastTouchY = 0;
		
		public void onTouchStart(TouchStartEvent e)
		{
			e.preventDefault();
			e.stopPropagation();
			
			if (e.getTouches().length() == 0)
				return;
			
			Touch touch = e.getTouches().get(0);
			
			int eventX = touch.getPageX() - ivmDrawGWTCanvas.getAbsoluteLeft();
			int eventY = touch.getPageY() - ivmDrawGWTCanvas.getAbsoluteTop();
			
			
				
			if (e.getTouches().length() == 1 && !moving ) {
				writing = true;
				mouseDownTouchStartAction(eventX, eventY);
			}
			if ( (e.getTouches().length() > 2) ) {
				moving = false;
				writing = false;
				
			}			

			e.preventDefault();
			e.stopPropagation();
		}
		public void onTouchMove(TouchMoveEvent e)
		{

			e.preventDefault();
			e.stopPropagation();
			
			if (e.getTouches().length() ==1)
			{
				Touch touch = e.getTouches().get(0);
				
			    boolean shiftPressed = false;
			    int eventX = touch.getPageX() - ivmDrawGWTCanvas.getAbsoluteLeft();
				int eventY = touch.getPageY() - ivmDrawGWTCanvas.getAbsoluteTop();	
				lastTouchX = eventX;
				lastTouchY = eventY;
			    
				mouseMoveTouchMoveAction(eventX, eventY, shiftPressed);
				
		    }
			
			e.preventDefault();
			e.stopPropagation();
			
		}
		public void onTouchEnd(TouchEndEvent e)
		{
//			Touch touch = e.getTouches().get(0);
//			
//		    int eventX = touch.getPageX() - kladjeHWTCanvas.getAbsoluteLeft();
//			int eventY = touch.getPageY() - kladjeHWTCanvas.getAbsoluteTop();
			
			moving = false;
			mouseUpTouchEndAction(lastTouchX, lastTouchY);
		}

	}

}
