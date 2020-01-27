/*
 * File:		IVMdrawGWTField
 *
 * The main logic of the drawable field is implemented in this file.
 * It provides a drawable canvas, constructs a vase shape based on the input line and classifies the drawn line
 * as either correct or incorrect.
 *
 */

package fi.ivmdrawgwt.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
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
import com.google.gwt.user.client.ui.ListBox;
import com.vaadin.pointerevents.client.PointerDownEvent;
import com.vaadin.pointerevents.client.PointerDownHandler;
import com.vaadin.pointerevents.client.PointerMoveEvent;
import com.vaadin.pointerevents.client.PointerMoveHandler;
import com.vaadin.pointerevents.client.PointerUpEvent;
import com.vaadin.pointerevents.client.PointerUpHandler;

import fi.ivmdrawgwt.client.text.Text;
import nl.uu.fi.dwo.interaction.client.JSONUtilities;
import nl.uu.fi.dwo.interaction.client.json.ObjectMap;

//import javax.sound.sampled.Line;


public class IVMdrawGWTField {
	
	private static Logger logger = Logger.getLogger("IVMdrawGWT");
	final int GAUSSIAN = 0;
	final int AVERAGE = 1;
	final int AVERAGE2 = 2;
	
	private int smoothType = AVERAGE;
	
	private IVMdrawGWT owner;

	private Canvas ivmDrawGWTCanvas, backgroundCanvas;//, strokeContainerCanvas
	private Context2d gIm, backgroundgIm, strokeContainergIm;

	private int correctVaasNummer=1;


	private int breedte, hoogte;

	private ArrayList<Point> formulaStrokePoints = new ArrayList<Point>();

	private boolean writing;
	private boolean moving;
	private int startX, startY;
	private boolean mouseDown;
	private boolean hasPointerSupport = false;


	private ArrayList<Point> allDrawnPoints = new ArrayList<>();
	private IVMStrokeContainer currentStrokeContainer;
	private IVMStrokeContainer jarStrokeContainer;
	private Stroke lastStroke;
	
	private ArrayList<IVMStrokeContainer> strokeContainerHistory = new ArrayList<IVMStrokeContainer>();
	private ListBox historyList;
	

	public IVMdrawGWTField(int w, int h, IVMdrawGWT owner) {
		this.owner = owner;
		historyList = owner.getHistoryListBox();
		historyList.addChangeHandler(new ListHandler());
		historyList.getElement().getStyle().setBackgroundColor(""+CssColor.make(38,115,182));
		historyList.getElement().getStyle().setColor("white");
		historyList.getElement().getStyle().setBorderStyle(BorderStyle.SOLID);;
		
		ivmDrawGWTCanvas = Canvas.createIfSupported();
		//ivmDrawGWTCanvas.setStyleName(owner.ivmDrawCss.canvas());
		ivmDrawGWTCanvas.getElement().getStyle().setProperty("touchAction", "none");
		
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
		
		PointerHandler pointerHandler = new PointerHandler();
		ivmDrawGWTCanvas.addDomHandler((PointerMoveHandler)pointerHandler, PointerMoveEvent.getType()); 
		ivmDrawGWTCanvas.addDomHandler((PointerUpHandler)pointerHandler, PointerUpEvent.getType()); 
		ivmDrawGWTCanvas.addDomHandler((PointerDownHandler)pointerHandler, PointerDownEvent.getType()); 
		
		jarStrokeContainer = new IVMStrokeContainer();
		jarStrokeContainer.setIsJar(true);
		currentStrokeContainer = new IVMStrokeContainer();
	}

	/**
	 * Getter function for the canvas.
	 *
	 * @return Canvas object
	 */
	public Canvas getCanvas() {
		return ivmDrawGWTCanvas;
	}
	
	public void setCorrectVaasNummer (int correctVaasNummer) {
		this.correctVaasNummer = correctVaasNummer;
	}
	


	/**
	 * Initializes the context2d.
	 */
	public void initContext2d() {
		gIm = ivmDrawGWTCanvas.getContext2d();
		backgroundgIm = backgroundCanvas.getContext2d();
	}
	
	public void setState(Map<String, Object> map) {
		setState(map,true);
	}

	/**
	 * Function used for the 'auteursomgeving'.
	 * @param map launchdata
	 */
	public void setState(Map<String, Object> map, boolean start) {
		if(map == null || map.isEmpty())
			return;
		ObjectMap launchState = JSONUtilities.wrapMap(map);
		
		Map<String,Object> ivmStrokeContainer = new HashMap<String,Object>();
		
		if (launchState.containsKey("ivmStrokeContainer"))
			ivmStrokeContainer = launchState.getMap("ivmStrokeContainer");
		IVMStrokeContainer currentContainer = new IVMStrokeContainer();
		try {
			currentContainer.setState(ivmStrokeContainer);
		}
		catch(Exception e) {
		}
		
		historyList.clear();
		
		List<Map<String,Object>> strokeContainerList = new ArrayList<Map<String,Object>>();
		if (launchState.containsKey("strokeContainerList"))
			strokeContainerList = launchState.getMapList("strokeContainerList");
		
		//historyList.setVisible(owner.historyVisible && strokeContainerList.size()>0);
		//historyList.setSelectedIndex(historyList.getItemCount()-1);
		
		strokeContainerHistory.clear();
		for (int sCnt = 0; sCnt < strokeContainerList.size(); sCnt++)
		{	
			IVMStrokeContainer sc = new IVMStrokeContainer();
			sc.setState(strokeContainerList.get(sCnt));
			strokeContainerHistory.add(sc);
			if((owner.check||owner.feedbackVisible) && correctVaasNummer!=0) // "owner.feedbackVisible" voor backward compatibiliteit (check werd pas later ingevoerd)
				handleClassification(new LineData(sc.getLastStroke().getIntParsePoints()), false);
			historyList.addItem(IVMdrawGWT.rb.pogingTekst() + " " + (sCnt+1) + " " + (owner.correctGraph ? "(" + IVMdrawGWT.rb.correctTekst() + ")" : ""));
		}
		
		int historySelection = -1;
		if(launchState.containsKey("historySelection"))
			historySelection = launchState.getInt("historySelection");
		if(historySelection>-1) {
			logger.info("histaryList>-1");
			historyList.setSelectedIndex(historySelection);
			currentStrokeContainer = strokeContainerHistory.get(historySelection);
			lastStroke = currentStrokeContainer.getLastStroke();
			if(owner.check && correctVaasNummer!=0)
				handleClassification(new LineData(lastStroke.getIntParsePoints()), false);
			processIVM();
		}
		else {
			currentStrokeContainer = currentContainer;
			lastStroke = currentStrokeContainer.getLastStroke();
			if(lastStroke!=null && owner.check && correctVaasNummer!=0) {
				handleClassification(new LineData(lastStroke.getIntParsePoints()), false);
				processIVM();
			}
		}
		historyList.setVisible(owner.historyVisible && strokeContainerList.size()>0);
		paint();
	}


	/**
	 * Function used for the 'auteursomgeving'.
	 */
	public HashMap<String,Object> getState() {
		HashMap<String,Object> h = new HashMap<String,Object>();
		
		Map<String,Object> ivmStrokeContainer = new HashMap <String,Object>();
		ivmStrokeContainer = currentStrokeContainer.getState();
		h.put("ivmStrokeContainer", ivmStrokeContainer);
		
		List<Map<String,Object>> strokeContainerList = new ArrayList<Map<String,Object>>();
		for (int i = 0; i < strokeContainerHistory.size(); i++)
		{	IVMStrokeContainer sc = strokeContainerHistory.get(i);
			//if(sc != currentStrokeContainer)
				strokeContainerList.add(sc.getState());
		}
		h.put("strokeContainerList", strokeContainerList);
		
		int historySelection = historyList.getSelectedIndex();
		h.put("historySelection", new Integer(historySelection));
		
		return h;
	}
	
	public void paint()	{
		paint(gIm);
	}
	
	
	public void paint(Context2d g) {
		g.clearRect(0, 0, breedte, hoogte);

		/* Draws the labels for the coordinate system */
		int xAsLengte = owner.jarFeedbackVisible ? (breedte-60)/2 : breedte-60;
		int yAsLengte = hoogte-60;
		g.setStrokeStyle(CssColor.make(49,71,112));
		g.setLineWidth(1.5d);
		g.beginPath();
		g.moveTo(30, 30);
		g.lineTo(25, 35);
		g.moveTo(30, 30);
		g.lineTo(35, 35);
		g.moveTo(30, 30);
		g.lineTo(30, 30 + yAsLengte);
		g.lineTo(30 + xAsLengte, 30 + yAsLengte);
		g.lineTo(25 + xAsLengte, 25 + yAsLengte);
		g.lineTo(30 + xAsLengte, 30 + yAsLengte);
		g.lineTo(25 + xAsLengte, 35 + yAsLengte);
		if(correctVaasNummer!=0) {
			g.setFillStyle(CssColor.make(49,71,112));
			g.setFont("15px arial");
			g.fillText(IVMdrawGWT.rb.vaasHoogteTekst(), 7, 17);
			g.fillText(IVMdrawGWT.rb.vaasVolumeTekst(), xAsLengte/2, 50+yAsLengte);
		}
		g.stroke();
		g.closePath();
		
		g.drawImage(ImageElement.as(owner.binImage.getElement()),xAsLengte, 30);
		if(currentStrokeContainer.getStrokeCount()>0) {
			if(owner.feedbackVisible)
					g.drawImage(ImageElement.as(owner.feedbackImage.getElement()),xAsLengte-30, 30);
			if(owner.feedbackVisible && owner.correctGraph)
				g.drawImage(ImageElement.as(owner.goedkrulImage.getElement()),55, 35);
			else if(owner.feedbackVisible)
				g.drawImage(ImageElement.as(owner.foutkruisImage.getElement()),55, 35);
		}
		
		if(owner.jarFeedbackVisible) {
			g.setFillStyle(CssColor.make(239, 241, 243));
			g.fillRect(xAsLengte+50, 30, xAsLengte-20, yAsLengte);
			
			g.setFillStyle(CssColor.make(49,71,112));
			g.setFont("15px arial");
			g.fillText(IVMdrawGWT.rb.vaasLabel(), 3*xAsLengte/2+30, 50+yAsLengte);
			
			jarStrokeContainer.draw(g);
		}
		
		g.setLineWidth(1.5d);
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
		double vaasX = 3*breedte/4;
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


	/**
	 * Call functions when mouseDown event occurs.
	 * @param eventX
	 * @param eventY
	 */
	public void mouseDownTouchStartAction(int eventX, int eventY) {
		//if(currentStrokeContainer==null)
		//	currentStrokeContainer = new IVMStrokeContainer();
		
		owner.closeFeedback();
		int xAsLengte = owner.jarFeedbackVisible ? (breedte-60)/2 : breedte-60;
		
		if(owner.feedbackVisible && eventX>xAsLengte-30 && eventX<xAsLengte && eventY>30 && eventY<60) {
			logger.info("In mouseDownAction");
			handleClassification(new LineData(currentStrokeContainer.getLastStroke().getIntParsePoints()), true);
			return;
		}
		if(eventX>xAsLengte && eventX<xAsLengte+30 && eventY>30 && eventY<60) {
			currentStrokeContainer = new IVMStrokeContainer();
			jarStrokeContainer.clear();
			paint();
			return;
		}
			
			
		
		formulaStrokePoints.clear();
		//mouseDown = true;
		formulaStrokePoints.add(new Point(eventX, eventY));
		paint();
	}


	/**
	 * Call functions when mouseMove event occurs.
	 * @param eventX
	 * @param eventY
	 * @param shiftPressed
	 */
	public void mouseMoveTouchMoveAction(int eventX, int eventY) {

		this.allDrawnPoints.add(new Point(eventX, eventY));
		if(formulaStrokePoints.size()>0) {
				int dx = (int)(formulaStrokePoints.get(formulaStrokePoints.size()-1).x) - eventX;
				int dy = (int)(formulaStrokePoints.get(formulaStrokePoints.size()-1).y) - eventY;
				if(dx*dx+dy*dy>3)
					formulaStrokePoints.add(new Point(eventX, eventY));
		}
		paint();
		
	}

	private void handleClassification(LineData inputPoints) {
		handleClassification(inputPoints,true);
	}

	/**
	 * Handle the classification by:
	 * - adjusting the background colour of the feedbackfield.
	 * - set the correctGraph Boolean
	 * - communicating the feedback to the user.
	 * @param inputPoints
	 */
	private void handleClassification(LineData inputPoints, boolean geefFeedback) {
		String feedback;
		String color = "white";

		if (inputPoints.validInput()) {
			Matrix mPoints = new Matrix(inputPoints.getXs(), inputPoints.getYs());
			Classifier classifier = new Classifier(mPoints, this.correctVaasNummer);

			feedback = classifier.getFeedback();


			if (classifier.classify()) {
				color = "#33cc33";
				this.owner.correctGraph = true;
			} else {
				color = "white";
				this.owner.correctGraph = false;
			}

		} else {
			if (LineData.maxDecreasingSequence(inputPoints.getYs()) > 20) {
				feedback = Feedback.decreasingLine();
			} else {
				feedback = Feedback.decreasingXs();
			}

		}
		if(geefFeedback) {
			logger.info("handleClassifcation");
			this.owner.setFeedback(feedback);
		}
			
		//this.owner.ivmFeedbackGWTField.mouseUpEvent(feedback);
		//this.owner.label.getElement().getStyle().setBackgroundColor(color);
	}


	/**
	 * Call functions when mouseUp event occurs (i.e. when line is drawn).
	 * @param eventX
	 * @param eventY
	 */
	public void mouseUpTouchEndAction(int eventX, int eventY) {
		if(formulaStrokePoints.size()==0) {
			paint();
			return;
		}
			
		int x0 = formulaStrokePoints.get(0).x;
		int y0 = formulaStrokePoints.get(0).y;
		int x1 = formulaStrokePoints.get(formulaStrokePoints.size()-1).x;
		int y1 = formulaStrokePoints.get(formulaStrokePoints.size()-1).y;
		if((x0-x1)*(x0-x1)+(y0-y1)*(y0-y1)<1600) {
			this.allDrawnPoints.clear();
			formulaStrokePoints.clear();
			paint();
			return;
		}
		
//		LineData inputPoints = new LineData(this.allDrawnPoints);
//		
//		if(owner.check && correctVaasNummer!=0)
//			handleClassification(inputPoints);
//
//		this.allDrawnPoints.clear();
		
		

		currentStrokeContainer = new IVMStrokeContainer(); //.clear();
		lastStroke = new Stroke(formulaStrokePoints);
		currentStrokeContainer.addStroke(lastStroke);
		formulaStrokePoints.clear();
		strokeContainerHistory.add(currentStrokeContainer);
		
		LineData inputPoints = new LineData(lastStroke.getIntParsePoints());
		
		if(owner.check && correctVaasNummer!=0)
			handleClassification(inputPoints);

		this.allDrawnPoints.clear();
		

		if (owner.check && correctVaasNummer!=0 && !inputPoints.validInput()) {
			return;
		}

		processIVM();
		paint();
		historyList.addItem(IVMdrawGWT.rb.pogingTekst() + " " + (historyList.getItemCount()+1) + " " + (owner.correctGraph ? "(" + IVMdrawGWT.rb.correctTekst() + ")" : "") );
		historyList.setVisible(owner.historyVisible && strokeContainerHistory.size()>0);
		historyList.setSelectedIndex(historyList.getItemCount()-1);
		
		owner.setChanged();
	}
	
	public int getAttemptCount() {
		return strokeContainerHistory.size();
	}
	
	class ListHandler implements ChangeHandler {

		@Override
		public void onChange(ChangeEvent event) {
			int selectedIndex = historyList.getSelectedIndex();
			if(selectedIndex>-1) {
				currentStrokeContainer = strokeContainerHistory.get(selectedIndex);
				lastStroke = currentStrokeContainer.getLastStroke();
				if(owner.check && correctVaasNummer!=0)
					handleClassification(new LineData(lastStroke.getIntParsePoints()));
				processIVM();
				owner.setChanged();
			}
			
		}
		
	}
	
	class MouseHandler implements MouseDownHandler, MouseMoveHandler, MouseUpHandler
	{
		
		public void onMouseDown(MouseDownEvent e)
		{
			e.preventDefault();
			e.stopPropagation();
			
			if(hasPointerSupport)
				return;
			
			mouseDown = true;
			
			int eventX = e.getX();
			int eventY = e.getY();
			
			mouseDownTouchStartAction(eventX, eventY);
		}


		public void onMouseMove(MouseMoveEvent e)	
		{
			e.preventDefault();
			e.stopPropagation();
			
			if(hasPointerSupport)
				return;

			if (!mouseDown)
				return;

			int eventX = e.getX();
			int eventY = e.getY();
			
			mouseMoveTouchMoveAction(eventX, eventY);
			
		} // onMouseMove

		/**
		 * MouseUp event functionality should be added to mouseUpTouchEndAction()
		 * @param e
		 */
		public void onMouseUp(MouseUpEvent e)	
		{
			e.preventDefault();
			e.stopPropagation();
			
			if(hasPointerSupport)
				return;
			
			int eventX = e.getX();
			int eventY = e.getY();
			
			mouseDown = false;
			if (e.getNativeButton() == NativeEvent.BUTTON_RIGHT) {
				return;
			}
			mouseUpTouchEndAction(eventX, eventY);
		}

	}

	class PointerHandler implements PointerDownHandler, PointerMoveHandler, PointerUpHandler
	{
		int lastTouchX = 0;
		int lastTouchY = 0;
		
		@Override
		public void onPointerDown(PointerDownEvent e) {
			e.preventDefault();
			e.stopPropagation();
			logger.info("PointerDown");
			hasPointerSupport = true;
			mouseDown = true;
			
			int eventX = e.getRelativeX(ivmDrawGWTCanvas.getElement());
			int eventY = e.getRelativeY(ivmDrawGWTCanvas.getElement());
			
			mouseDownTouchStartAction(eventX, eventY);
			e.preventDefault();
			e.stopPropagation();
		}
		
		@Override
		public void onPointerMove(PointerMoveEvent e) {
			e.preventDefault();
			e.stopPropagation();
			
			if (!mouseDown)
				return;
			
		    int eventX = e.getRelativeX(ivmDrawGWTCanvas.getElement());
			int eventY = e.getRelativeY(ivmDrawGWTCanvas.getElement());	
			lastTouchX = eventX;
			lastTouchY = eventY;
		    
			mouseMoveTouchMoveAction(eventX, eventY);
		}
		
		@Override
		public void onPointerUp(PointerUpEvent e) {
			e.preventDefault();
			e.stopPropagation();
			logger.info("PointerUp");
			mouseDown = false;
			
			mouseUpTouchEndAction(lastTouchX, lastTouchY);
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
			
			if(hasPointerSupport)
				return;
			
			mouseDown = true;
			
			if (e.getTouches().length() == 0)
				return;
			
			Touch touch = e.getTouches().get(0);
			
			int eventX = touch.getPageX() - ivmDrawGWTCanvas.getAbsoluteLeft();
			int eventY = touch.getPageY() - ivmDrawGWTCanvas.getAbsoluteTop();
			
				
			if (e.getTouches().length() == 1 ) {
				mouseDownTouchStartAction(eventX, eventY);
			}
			
			e.preventDefault();
			e.stopPropagation();
		}
		public void onTouchMove(TouchMoveEvent e)
		{
			e.preventDefault();
			e.stopPropagation();
			
			if(hasPointerSupport)
				return;
			
			if (e.getTouches().length() ==1)
			{
				Touch touch = e.getTouches().get(0);
				
			    int eventX = touch.getPageX() - ivmDrawGWTCanvas.getAbsoluteLeft();
				int eventY = touch.getPageY() - ivmDrawGWTCanvas.getAbsoluteTop();	
				lastTouchX = eventX;
				lastTouchY = eventY;
			    
				mouseMoveTouchMoveAction(eventX, eventY);
			}
			
			e.preventDefault();
			e.stopPropagation();
			
		}
		public void onTouchEnd(TouchEndEvent e)
		{
			e.preventDefault();
			e.stopPropagation();
			
			if(hasPointerSupport)
				return;
			
			mouseUpTouchEndAction(lastTouchX, lastTouchY);
		}

	}

}
