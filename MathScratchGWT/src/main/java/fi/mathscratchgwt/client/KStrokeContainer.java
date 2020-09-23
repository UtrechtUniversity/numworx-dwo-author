package fi.mathscratchgwt.client;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.vectomatic.dom.svg.OMNode;
import org.vectomatic.dom.svg.OMSVGLineElement;
import org.vectomatic.dom.svg.OMSVGPathElement;
import org.vectomatic.dom.svg.OMSVGPathSegList;
import org.vectomatic.dom.svg.OMSVGRectElement;
import org.vectomatic.dom.svg.OMSVGSVGElement;
import org.vectomatic.dom.svg.utils.SVGConstants;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.Context2d.LineCap;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Image;

import fi.wiskopdr.FormuleParser;
import fi.wiskopdr.expressies.BasisExpressie;
import fi.wiskopdr.expressies.Expressie;
import fi.writemathgwt.client.engine.DoubleRectangle;
import fi.writemathgwt.client.engine.Point;
import fi.writemathgwt.client.engine.Stroke;
import fi.writemathgwt.client.engine.StrokeContainer;
import fi.writemathgwt.client.engine.WMObject;
import fi.writemathgwt.client.engine.WMObjectLine;
import nl.uu.fi.dwo.formule.client.formuleholder.FormuleViewer;
import nl.uu.fi.dwo.interaction.client.FormuleFont;
import nl.uu.fi.dwo.interaction.client.JSONUtilities;
import nl.uu.fi.dwo.interaction.client.json.ObjectMap;

public class KStrokeContainer {

	private static Logger logger = Logger.getLogger("KStrokeContainer");
	
	private MathScratchField parent;
	StrokeContainer strokeContainer;
	
	 boolean formuleModus;
	private boolean active = false;
	private boolean proActive = false;
	private boolean changedToActive = false;
	private boolean changedFromActive = false;
	private boolean popupMode = false;
	private double activeTranslationX;
	private double activeTranslationY;
	private double correctieX;
	
	private CssColor drawingColor = CssColor.make(80, 80, 80);
	private Rectangle defaultBox;
	private Rectangle box;
	private Rectangle writeBox;
	private boolean correct = false;
	private boolean isfalse = false;
	private boolean isHalf = false;
	
	 boolean isInputSC = false;
	
	 FormuleViewer formuleViewer;
	 boolean isGetalsExpressie = false;
	
	private Image eyeImage;
	private ImageElement eyeImageElement;
	
	private Image approxImage, formuleschrijfImage, formuleschrijfaanImage;
	private ImageElement approxImageElement, formuleschrijfImageElement, formuleschrijfaanImageElement;
	
	boolean recognizeOff;
	
	private boolean eraserActive = false;
	private boolean erasing = false;
	private int erasingX, erasingY;
	
	private int nr;
	
	private OMSVGSVGElement svg;
	private OMSVGSVGElement svgStrokes;
	private OMSVGSVGElement svgPopup;
	private OMSVGSVGElement svgGrid;
	private OMSVGRectElement rectToolbarForm;
	private OMSVGRectElement rectToolbarDraw;
	private ArrayList<OMNode> svgStrokesNodes = new ArrayList<OMNode>();
	private ArrayList<OMNode> svgPopupNodes = new ArrayList<OMNode>();
	private ArrayList<OMNode> svgGridNodes = new ArrayList<OMNode>();
	
	public KStrokeContainer (MathScratchField parent) {
		this.parent = parent;
		
		strokeContainer = new StrokeContainer();
		
		ImageResource eyeResource = parent.eigenaar.mathScratchGWTClientBundle.eyeResource();
		eyeImage = new Image(eyeResource);
		eyeImageElement = ImageElement.as(eyeImage.getElement());
		
		ImageResource approxResource = parent.eigenaar.mathScratchGWTClientBundle.approxResource();
		approxImage = new Image(approxResource);
		approxImageElement = ImageElement.as(approxImage.getElement());
		
		ImageResource formuleschrijfImageResource = parent.eigenaar.mathScratchGWTClientBundle.formuleschrijfResource(); 
		formuleschrijfImage = new Image(formuleschrijfImageResource);
		formuleschrijfImageElement = ImageElement.as(formuleschrijfImage.getElement());
		
		ImageResource formuleschrijfaanImageResource = parent.eigenaar.mathScratchGWTClientBundle.formuleschrijfaanResource(); 
		formuleschrijfaanImage = new Image(formuleschrijfaanImageResource);
		formuleschrijfaanImageElement = ImageElement.as(formuleschrijfaanImage.getElement());
		
//		initSvg();
//		svg.appendChild(svgPopup);
//		svg.appendChild(svgGrid);
//		svg.appendChild(svgStrokes);
	}
	
	public KStrokeContainer (MathScratchField parent, Rectangle defaultBox) {
		this.parent = parent;
		this.defaultBox = defaultBox;
		this.box = defaultBox;
		isInputSC = true;
		strokeContainer = new StrokeContainer();
		
		ImageResource eyeResource = parent.eigenaar.mathScratchGWTClientBundle.eyeResource();
		eyeImage = new Image(eyeResource);
		eyeImageElement = ImageElement.as(eyeImage.getElement());
		
		ImageResource approxResource = parent.eigenaar.mathScratchGWTClientBundle.approxResource();
		approxImage = new Image(approxResource);
		approxImageElement = ImageElement.as(approxImage.getElement());
		
		ImageResource formuleschrijfImageResource = parent.eigenaar.mathScratchGWTClientBundle.formuleschrijfResource(); 
		formuleschrijfImage = new Image(formuleschrijfImageResource);
		formuleschrijfImageElement = ImageElement.as(formuleschrijfImage.getElement());
		
		ImageResource formuleschrijfaanImageResource = parent.eigenaar.mathScratchGWTClientBundle.formuleschrijfaanResource(); 
		formuleschrijfaanImage = new Image(formuleschrijfaanImageResource);
		formuleschrijfaanImageElement = ImageElement.as(formuleschrijfaanImage.getElement());
		
		
//		initSvg();
//		svg.appendChild(svgPopup);
//		svg.appendChild(svgGrid);
//		svg.appendChild(svgStrokes);
	}
	
	public boolean addStroke(Stroke stroke) {
		box = null;
		writeBox = null;
		
		boolean b = false;
		if(recognizeOff) {
			b=strokeContainer.addStroke(stroke,false);
			addToHistory();
			parent.svgManager.enableBinButtonSVG(true);
		}
		else {
			strokeContainer.addStroke(stroke);
			//logger.info(strokeContainer.getFormulaString());
			//if(!"-".equals(stroke.getOneStrokeTeken()))
			String formuleString = "$f"+strokeContainer.getFormulaString()+"@";
//			Expressie formule = FormuleParser.geefExpressie(formuleString);
//			if(formule!=null) {
//				logger.info("na parsing: "+formule.toString());
//				logger.info("geefWaarde: "+formule.geefWaarde());
//			}
			//isGetalsExpressie = formule!=null && !Double.isNaN(formule.geefWaarde()) && !(formule instanceof BasisExpressie);
			checkBenaderbaar();
			formuleViewer = new FormuleViewer(strokeContainer.getFormulaString());
			formuleViewer.setColor(CssColor.make(38, 115, 182));
			//formuleViewer.setFont(FormuleFont.createFromFontSize(16));
		}
		corrigeerSCPositie();
		if(getStrokeCount()==0)
			box = defaultBox;
		
		return b;
	}
	
	public String checkBenaderbaar() {
		String rekenString = "$f"+strokeContainer.getFormulaString()+"@";
		if(rekenString.endsWith("=@"))
			rekenString = rekenString.substring(0, rekenString.length()-2)+"@";
		if(rekenString.indexOf("=")>-1)
			rekenString = "$f"+rekenString.substring(rekenString.lastIndexOf("=")+1);
		logger.info("na = teken "+rekenString);
		Expressie formule = FormuleParser.geefExpressie(rekenString);
		isGetalsExpressie = formule!=null && !Double.isNaN(formule.geefWaarde()) && !(formule instanceof BasisExpressie);
		return rekenString;
	}
	
	
	public void approximate() {
		String formuleString = "$f"+strokeContainer.getFormulaString()+"@";
		String rekenString = checkBenaderbaar();
		Expressie formule = FormuleParser.geefExpressie(rekenString);
		if(formule!=null) {
			double approxDouble = formule.geefWaarde();
			if(formuleString.endsWith("=@"))
				formuleString = formuleString.substring(0, formuleString.length()-2)+"@";
			String newFormuleString = formuleString.substring(0,formuleString.length()-1) + "=" + Double.toString(approxDouble) + "@";
			logger.info("na approx: "+newFormuleString);
			formuleViewer = new FormuleViewer(newFormuleString);
			formuleViewer.setColor(CssColor.make(38, 115, 182));
		}
	}
	
	public void corrigeerSCPositie() {
		if(active && getBox()!=null && !this.isInputSC) {
			int correctieX = Math.max(0, getBox().x+getBox().width+80+47 - parent.breedte-20);
			int correctieY = Math.min(0,getBox().y-70);
			activeTranslationX += correctieX;
			activeTranslationY += correctieY; 
			
			translate((int)-correctieX,(int)-correctieY);
		}
	}
	
	public void wis() {
		strokeContainer.wis();
		parent.svgManager.enableBinButtonSVG(false);
		addToHistory();
	}
	
	public void eraseStrokes(int x, int y) {
		strokeContainer.removeStrokes(x,y);
		addToHistory();
		if(strokeContainer.getStrokes().size()==0) {
			eraserActive = false;
			parent.svgManager.enableEraserButtonSVG(false);
			parent.svgManager.enablePenButtonSVG(true);
			parent.svgManager.enableBinButtonSVG(false);
		}
	}
	
//	public void eraseLastStroke() {
//		if(strokeContainer.getStrokes().size()>0)
//			strokeContainer.getStrokes().remove(strokeContainer.getStrokes().size()-1);
//		if(strokeContainer.getStrokes().size()==0)
//			eraserActive = false;
//	}
	
	private int numHistories;
	private int maxHistories = 10;
	private HashMap<String, Object>[] histories = new HashMap[maxHistories+1];
	
	
	public void addToHistory() {
		HashMap<String, Object> stateTable = getState();
		histories[numHistories] = stateTable;
		numHistories++;
		if (numHistories > 1)
			parent.svgManager.enableUndoButtonSVG(true);
		if (numHistories > maxHistories) {
			for (int i = 0; i < numHistories - 1; i++) {
				histories[i] = histories[i + 1];
			}
			numHistories--;
		}
	}
	
	public HashMap<String, Object> getFromHistory() {
		HashMap<String, Object> lastState = null;
		if (numHistories > 1) {
			lastState = histories[numHistories - 2];
			numHistories--;
			if (numHistories < 2)
				parent.svgManager.enableUndoButtonSVG(false);
		}
		else {
			numHistories = 1;
			parent.svgManager.enableUndoButtonSVG(false);
		}
		logger.info("getFromHistory "+numHistories);
		return lastState;
	}
	
	public void undo() {
		HashMap<String, Object> lastState = getFromHistory();
		if (lastState != null) {
			setState(lastState);
		}
		if(strokeContainer.getStrokes().size()==0) {
			eraserActive = false;
			parent.svgManager.enableEraserButtonSVG(false);
			parent.svgManager.enablePenButtonSVG(true);
			parent.svgManager.enableBinButtonSVG(false);
		}
		else {
			parent.svgManager.enableBinButtonSVG(true);
		}
	}
	
	public int getStrokeCount() {
		return strokeContainer.getStrokes().size();
	}
	
	public Rectangle getHandleArea() {
		if(writeBox==null)
			writeBox = new Rectangle(20,20,parent.breedte-40,parent.hoogte-40);
		Point c = parent.getActiveTranslation();
		int x = getWriteBox().x+2 ; 
		int y = getWriteBox().y + getWriteBox().height-30 ; 
		if(recognizeOff) {
			x = getWriteBox().x+getWriteBox().width/2 + c.x; 
			y = getWriteBox().y + getWriteBox().height/2+ c.y;
		}
		
//		x = Math.max(x, getBox().x-30); 
//		y = getBox().y+getBox().height; 
		return new Rectangle(x,y,30,30);
		
	}
	
	public OMSVGSVGElement getSvg() {
		return svg;
	}
	
	public Rectangle getHeaderArea() {
		if(writeBox==null)
			writeBox = new Rectangle(20,20,parent.breedte-40,parent.hoogte-40);
		if(recognizeOff) 
			return new Rectangle(getWriteBox().x, getWriteBox().y, getWriteBox().width, 47);
		else 
			return new Rectangle(getWriteBox().x + getWriteBox().width - 47 , getWriteBox().y, 47, getWriteBox().height);
	}
	
	public Rectangle getCloseButtonArea() {
		if(writeBox==null)
			writeBox = new Rectangle(20,20,parent.breedte-40,parent.hoogte-40);
		int x = getWriteBox().x + getWriteBox().width - 33; 
		int y = getWriteBox().y + 4; 
		return new Rectangle(x,y,30,30);
	}
	
	public Rectangle getCheckButtonArea() {
		if(writeBox==null)
			writeBox = new Rectangle(20,20,parent.breedte-40,parent.hoogte-40);
		int x = getWriteBox().x + getWriteBox().width - 33; 
		int y = getWriteBox().y + getWriteBox().height - 36; 
		return new Rectangle(x,y,30,30);
	}
	
	public Rectangle getPenButtonArea() {
		if(!recognizeOff)
			return new Rectangle(0,0,0,0);
		if(writeBox==null)
			writeBox = new Rectangle(20,20,parent.breedte-40,parent.hoogte-40);
		int x = getWriteBox().x + 80; 
		int y = getWriteBox().y + 5; 
		return new Rectangle(x,y,25,25);
	}
	
	public Rectangle getEraserButtonArea() {
		if(!recognizeOff)
			return new Rectangle(0,0,0,0);
		if(writeBox==null)
			writeBox = new Rectangle(20,20,parent.breedte-40,parent.hoogte-40);
		int x = getWriteBox().x + 108; 
		int y = getWriteBox().y + 5;  
		return new Rectangle(x,y,25,25);
	}
	
	public Rectangle getBinButtonArea() {
		if(!recognizeOff)
			return new Rectangle(0,0,0,0);
		if(writeBox==null)
			writeBox = new Rectangle(20,20,parent.breedte-40,parent.hoogte-40);
		int x = getWriteBox().x + 187; 
		int y = getWriteBox().y + 5;  
		return new Rectangle(x,y,25,25);
	}
	
	public Rectangle getUndoButtonArea() {
		if(!recognizeOff)
			return new Rectangle(0,0,0,0);
		if(writeBox==null)
			writeBox = new Rectangle(20,20,parent.breedte-40,parent.hoogte-40);
		int x = getWriteBox().x + 158; 
		int y = getWriteBox().y + 5;  
		return new Rectangle(x,y,25,25);
	}
	
	public Rectangle getRecognizeButtonArea() {
		if(!recognizeOff)
			return new Rectangle(0,0,0,0);
		if(writeBox==null)
			writeBox = new Rectangle(20,20,parent.breedte-40,parent.hoogte-40);
		int x = getWriteBox().x + 33; 
		int y = getWriteBox().y + 5; 
//		if(correct || isfalse || isHalf || recognizeOff)
//			return new Rectangle(x,y,0,0);
		return new Rectangle(x,y,25,25);
	}
	
	public Rectangle getNotRecognizeButtonArea() {
		if(writeBox==null)
			writeBox = new Rectangle(20,20,parent.breedte-40,parent.hoogte-40);
		int x = getWriteBox().x + 5; 
		int y = getWriteBox().y + 5; 
//		if(correct || isfalse || isHalf || recognizeOff)
//			return new Rectangle(x,y,0,0);
		return new Rectangle(x,y,25,25);
	}
	
	public Rectangle getApproxButtonArea() {
		if(writeBox==null)
			writeBox = new Rectangle(20,20,parent.breedte-40,parent.hoogte-40);
		int x = getWriteBox().x+getWriteBox().width-32; 
		int y = getWriteBox().y+getWriteBox().height/2-19; 
//		if(correct || isfalse || isHalf || recognizeOff)
//			return new Rectangle(x,y,0,0);
		if(!parent.calculator)
			return new Rectangle(x,y,0,0);
		return new Rectangle(x,y,27,38);
	}
	
	public Rectangle getFormulaArea() {
		int x = Math.max(getWriteBox().x+50, getBox()!=null ? getBox().x : 0) ;// + getBox().width/2-parent.formuleViewer.getWidth()/2;
		int y = getWriteBox().y+5;
		if(formuleViewer!=null) {
			int w = formuleViewer.getWidth();
			int h = formuleViewer.getHeight();
			return new Rectangle(x,y,w,h);
		}
		return new Rectangle(x,y,0,0);
	}
	
	private void drawcloseButton(Context2d g, Rectangle r) {
		int m = 10;
		
		g.setStrokeStyle( CssColor.make(38, 115, 182));
		g.setLineWidth(4.0d);
		g.beginPath();
		g.moveTo(r.x+m, r.y);
		g.lineTo(r.x+m, r.y+m);
		g.lineTo(r.x, r.y+m);
		g.stroke();
		
		g.moveTo(r.x+r.width-m, r.y);
		g.lineTo(r.x+r.width-m, r.y+m);
		g.lineTo(r.x+r.width, r.y+m);
		g.stroke();
		
		g.moveTo(r.x, r.y+r.height-m);
		g.lineTo(r.x+m,  r.y+r.height-m);
		g.lineTo(r.x+m, r.y+r.height);
		g.stroke();
		
		g.moveTo(r.x+r.width, r.y+r.height-m);
		g.lineTo(r.x+r.width-m,  r.y+r.height-m);
		g.lineTo(r.x+r.width-m, r.y+r.height);
		g.stroke();
	}
	
	private void drawCheckButton(Context2d g, Rectangle r) {
		//int m = 10;
		
		//g.setFillStyle(CssColor.make(255, 255, 255));
		
		//g.fillRect(r.x, r.y, r.width, r.height);
		
		g.setStrokeStyle(CssColor.make(38, 115, 182));
		g.setLineWidth(4.0d);
		g.beginPath();
		g.moveTo(r.x+r.width/4 , r.y+r.height/2);
		g.lineTo(r.x+r.width/2, r.y+r.height);
		g.lineTo(r.x+r.width, r.y);
		g.moveTo(r.x+r.width/4 , r.y+r.height/2);
		g.closePath();
		g.stroke();
	}	
	
	private void drawNotRecognizeButton(Context2d g, Rectangle r) {
		if(recognizeOff)
			g.setFillStyle(CssColor.make(38, 115, 182));
		else
			g.setFillStyle(CssColor.make(180, 195, 228));
		g.fillRect(r.x, r.y, r.width, r.height);
		
		g.setStrokeStyle(CssColor.make(255,255,255));
		g.setLineWidth(2.0d);
		int x = r.x+r.width/8;
		int y = r.y+r.height/8;
		int w = 3*r.width/4;
		int h = 3*r.height/4;
		
		g.beginPath();
		g.moveTo(x+w/3, y+2*h/3);
		g.lineTo(x+2*w/3, y);
		g.lineTo(x+w, y+2*h/3);
		g.lineTo(x+w/3, y+2*h/3);
//		g.closePath();
//		g.stroke();
//		
//		g.beginPath();
		g.moveTo(x+w/3, y+2*h/3);
		g.arc(x+w/3, y+2*h/3, w/3, 0, 2*Math.PI);
		g.moveTo(x+w/3, y+2*h/3);
		g.closePath();
		g.stroke();
	}
	
	private void drawRecognizeButton(Context2d g, Rectangle r) {
		if(!recognizeOff)
			g.setFillStyle(CssColor.make(38, 115, 182));
		else
			g.setFillStyle(CssColor.make(180, 195, 228));
		g.fillRect(r.x, r.y, r.width, r.height);
		
		g.setStrokeStyle(CssColor.make(255,255,255));
		g.setLineWidth(2.0d);
		int x = r.x+r.width/8;
		int y = r.y+r.height/8;
		int w = 3*r.width/4;
		int h = 3*r.height/4;
		
		g.beginPath();
		g.moveTo(x+w/6, y+h);
		g.lineTo(x+w/6, y+h/6);
		g.arc(x+w/3, y+h/6, w/6, Math.PI, 0 ,false);
		g.moveTo(x, y+h/2);
		g.lineTo(x+w/3, y+h/2);
		g.moveTo(x+w/2, y+h/3);
		g.lineTo(x+w, y+h);
		g.moveTo(x+w/2, y+h);
		g.lineTo(x+w, y+h/3);
		g.closePath();
		g.stroke();
	}
	
	private void drawPenButton(Context2d g, Rectangle r) {
		if(!eraserActive)
			g.setFillStyle(CssColor.make(38, 115, 182));
		else
			g.setFillStyle(CssColor.make(180, 195, 228));
		g.fillRect(r.x, r.y, r.width, r.height);
		
		g.setStrokeStyle(CssColor.make(255,255,255));
		g.setLineWidth(2.0d);
		int x = r.x+r.width/8;
		int y = r.y+r.height/8;
		int w = 3*r.width/4;
		int h = 3*r.height/4;
		
		g.beginPath();
		g.moveTo(x, y+h);
		g.lineTo(x, y+3*h/4);
		g.lineTo(x+3*w/4, y);
		g.lineTo(x+w, y+h/4);
		g.lineTo(x+w/4, y+h);
		g.lineTo(x, y+3*h/4);
		g.lineTo(x+w/4, y+h);
		g.lineTo(x, y+h);
		g.closePath();
		g.stroke();
	}
	
	private void drawEraserButton(Context2d g, Rectangle r) {
		if(eraserActive)
			g.setFillStyle(CssColor.make(38, 115, 182));
		else
			g.setFillStyle(CssColor.make(180, 195, 228));
		g.fillRect(r.x, r.y, r.width, r.height);
		
		g.setStrokeStyle(CssColor.make(255,255,255));
		g.setLineWidth(2.0d);
		int x = r.x+r.width/8;
		int y = r.y+r.height/8;
		int w = 3*r.width/4;
		int h = 3*r.height/4;
		
		g.beginPath();
		g.moveTo(x+5, y+h-10);
		g.arcTo(x+w/2 , y , x+w , y , 2);
		g.arcTo(x+w, y , x+w/2 , y+h , 2);
		g.arcTo(x+w/2 , y+h , x , y+h , 2);
		g.arcTo(x , y+h, x+w/2, y , 2);
		g.closePath();
		g.stroke();
		
		g.setFillStyle(CssColor.make(255,255,255));
		g.beginPath();
		g.moveTo(x+w/4, y+h/2);
		g.lineTo(x+w/2, y);
		g.lineTo(x+w, y);
		g.lineTo(x+3*w/4 , y+h/2);
		g.lineTo(x+w/4, y+h/2);
		g.closePath();
		g.fill();
		
	}
	
	private void drawEraser(Context2d g, Rectangle r) {
		g.setStrokeStyle(CssColor.make(38, 115, 182));
		g.setLineWidth(2.0d);
		int x = r.x+r.width/8;
		int y = r.y+r.height/8;
		int w = 3*r.width/4;
		int h = 3*r.height/4;
		
		g.beginPath();
		g.moveTo(x+5, y+h-10);
		g.arcTo(x+w/2 , y , x+w , y , 2);
		g.arcTo(x+w, y , x+w/2 , y+h , 2);
		g.arcTo(x+w/2 , y+h , x , y+h , 2);
		g.arcTo(x , y+h, x+w/2, y , 2);
		g.closePath();
		g.stroke();
		
		g.setFillStyle(CssColor.make(38, 115, 182));
		g.beginPath();
		g.moveTo(x+w/4, y+h/2);
		g.lineTo(x+w/2, y);
		g.lineTo(x+w, y);
		g.lineTo(x+3*w/4 , y+h/2);
		g.lineTo(x+w/4, y+h/2);
		g.closePath();
		g.fill();
		
	}
	
	private void drawUndoButton(Context2d g, Rectangle r) {
		if(getStrokeCount()>0)
			g.setFillStyle(CssColor.make(38, 115, 182));
		else
			g.setFillStyle(CssColor.make(180, 195, 228));
		g.fillRect(r.x, r.y, r.width, r.height);
		
		r = new Rectangle(r.x+4, r.y, r.width-6, r.height-10);
		g.setStrokeStyle(CssColor.make(255,255,255));
		g.setLineWidth(2.0d);
		g.beginPath();
		g.moveTo(r.x+r.width/6, r.y+r.height);
		g.arc(r.x+r.width/2, r.y+r.height, r.width*5/12, Math.PI, 0);
		g.moveTo(r.x, r.y+r.height);
		g.lineTo(r.x, r.y+r.height*3/4);
		g.lineTo(r.x+r.width/4, r.y+r.height);
		g.lineTo(r.x-2, r.y+r.height);
		
		g.moveTo(r.x, r.y+r.height);
		g.closePath();
		g.stroke();
	}
	
	private void drawBinButton(Context2d g, Rectangle r) {
		if(getStrokeCount()>0)
			g.setFillStyle(CssColor.make(38, 115, 182));
		else
			g.setFillStyle(CssColor.make(180, 195, 228));
		g.fillRect(r.x, r.y, r.width, r.height);
		
		g.setStrokeStyle(CssColor.make(255,255,255));
		g.setLineWidth(2.0d);
		int x = r.x+r.width/8;
		int y = r.y+r.height/8;
		int w = 3*r.width/4;
		int h = 3*r.height/4;
		
		g.beginPath();
		g.moveTo(x, y+h/4);
		g.lineTo(x+w, y+h/4);
		g.moveTo(x+w/6, y+h/4);
		g.lineTo(x+w/4, y+h);
		g.lineTo(x+w*3/4, y+h);
		g.lineTo(x+w*5/6, y+h/4);
		g.moveTo(x+w/2, y+h/4);
		g.lineTo(x+w/2, y+h);
		g.moveTo(x+w*3/8, y+h/4);
		g.lineTo(x+w*3/8, y);
		g.lineTo(x+w*5/8, y);
		g.lineTo(x+w*5/8, y+h/4);
		
		g.moveTo(x, y+h/6);
		g.closePath();
		g.stroke();
	}
	
	private void drawShadow(Context2d g, Rectangle r) {
		for(int i=0 ; i<10 ; i++) {
			g.setStrokeStyle( CssColor.make("rgba("+(200+5*i)+","+(200+5*i)+","+(200+5*i)+","+(1-0.1*i)+")"));
			g.setLineWidth(2.0d);
			g.beginPath();
			g.rect(r.x-2*i, r.y-2*i, r.width+4*i, r.height+4*i);
			g.closePath();
			g.stroke();
		}
	}
	
	private void drawHandle(Context2d g, Rectangle r) {
			g.setStrokeStyle( CssColor.make(38, 115, 182));
			g.setLineWidth(1.0d);
			g.beginPath();
			DoublePoint pU = new DoublePoint(r.x+r.width/2,r.y+5);
			DoublePoint pD = new DoublePoint(r.x+r.width/2,r.y+r.height-5);
			DoublePoint pL = new DoublePoint(r.x+5,r.y+r.height/2);
			DoublePoint pR = new DoublePoint(r.x+r.width-5,r.y+r.height/2);
			int d = 3;
			g.moveTo(pU.x, pU.y);
			g.lineTo(pU.x-d, pU.y+d);
			g.moveTo(pU.x, pU.y);
			g.lineTo(pU.x+d, pU.y+d);
			g.moveTo(pU.x, pU.y);
			g.lineTo(pD.x,pD.y);
			g.lineTo(pD.x-d,pD.y-d);
			g.moveTo(pD.x,pD.y);
			g.lineTo(pD.x+d,pD.y-d);
			g.moveTo(pD.x,pD.y);
			
			g.moveTo(pL.x, pL.y);
			g.lineTo(pL.x+d, pL.y+d);
			g.moveTo(pL.x, pL.y);
			g.lineTo(pL.x+d, pL.y-d);
			g.moveTo(pL.x, pL.y);
			g.lineTo(pR.x,pR.y);
			g.lineTo(pR.x-d,pR.y-d);
			g.moveTo(pR.x,pR.y);
			g.lineTo(pR.x-d,pR.y+d);
			g.moveTo(pR.x,pR.y);
			
			g.closePath();
			g.stroke();
		
	}
	
	private void drawProActiveAura(Context2d g, Rectangle r) {
		//g.setFillStyle(CssColor.make("rgba(200,200,200,0.5)"));
		g.setFillStyle(CssColor.make(255,255,255));
		r = new Rectangle(r.x-10, r.y-10, r.width+20, r.height+20);
		g.fillRect(r.x, r.y, r.width, r.height);
		
		for(int i=0 ; i<25 ; i++) {
			
			g.setStrokeStyle( CssColor.make("rgba("+(200+2*i)+","+(200+2*i)+","+(200+2*i)+","+0.5+")"));
			g.setLineWidth(1.0d);
			g.beginPath();
			g.rect(r.x-1*i, r.y-1*i, r.width+2*i, r.height+2*i);
			g.closePath();
			g.stroke();
		}
		g.setLineWidth(2.0d);
	}
	
	private void drawGrid (Context2d g, Rectangle r) {
		CssColor ruitjesKleur = CssColor.make(38, 115, 182);
				Point c = parent.getActiveTranslation();
				int cx = (c.x-getWriteBox().x)%20;
				int cy = (c.y-getWriteBox().y)%20;
					int	lineDistance = (int)(10*parent.schrijfLeesFactor);
					g.setStrokeStyle(ruitjesKleur);
					g.setLineWidth(0.2d);
					int vSteps = r.height / lineDistance;
					for (int vCnt = 1; vCnt <= vSteps; vCnt++)
					{
						g.beginPath();
						g.moveTo(r.x, r.y+cy + vCnt * lineDistance);
						g.lineTo(r.x + r.width - 1, r.y +cy+ vCnt * lineDistance);
						g.stroke();
					}
					int hSteps = r.width / lineDistance;
					for (int hCnt = 1; hCnt <= hSteps; hCnt++)
					{
						g.beginPath();
						g.moveTo(r.x+cx + hCnt * lineDistance, r.y);
						g.lineTo(r.x+cx + hCnt * lineDistance, r.y + r.height - 1);
						g.stroke();
					}
					
					
	}
	
	private void animateToActive (Context2d g) {
		g.setFillStyle(CssColor.make(200, 200, 200));
		int x = getWriteBox().x + getWriteBox().width/4;
		int y = getWriteBox().y + getWriteBox().height/4;
		int w = getWriteBox().width/2;
		int h = getWriteBox().height/2;
		g.fillRect(x,y,w,h);
		
		
	}
	
	private void animateFromActive (Context2d g) {
		g.setFillStyle(CssColor.make(200, 200, 200));
		int x = getWriteBox().x + getWriteBox().width/4;
		int y = getWriteBox().y + getWriteBox().height/4;
		int w = getWriteBox().width/2;
		int h = getWriteBox().height/2;
		g.fillRect(x,y,w,h);
		
	}
	
//	public void drawAn(Context2d g) {
//		if(strokeContainer.getStrokes().size()>0 || recognizeOff || isInputSC) {
//			if(active && changedToActive) {
//				animateToActive(g);
//				changedToActive = false;
//				Timer timer = new Timer()
//		        {
//		            @Override
//		            public void run()
//		            {
//		            		drawClean(g);
//		            }
//		        };
//
//		        timer.schedule(100);
//			}
//			else if(active && changedFromActive) {
//				animateFromActive(g);
//				changedFromActive = false;
//				Timer timer = new Timer()
//		        {
//		            @Override
//		            public void run()
//		            {
//		            		drawClean(g);
//		            }
//		        };
//
//		        timer.schedule(100);
//			}
//			else
//				drawClean(g);
//			
//		}
//		
//	}
	public void draw(Context2d g) {
		if(strokeContainer.getStrokes().size()>0 || recognizeOff || isInputSC) {
			
			
			if(active && !popupMode) {
				g.setFillStyle(CssColor.make(255, 255, 255));
				g.fillRect(getWriteBox().x-5, getWriteBox().y-5, getWriteBox().width+10, getWriteBox().height+10);
				
				drawShadow(g,new Rectangle(getWriteBox().x-5, getWriteBox().y-5, getWriteBox().width+10, getWriteBox().height+10));
				drawGrid(g,new Rectangle(getWriteBox().x-5, getWriteBox().y-5, getWriteBox().width+10, getWriteBox().height+10));
				
				//g.setFillStyle(CssColor.make(239, 241, 243));
				g.fillRect(getWriteBox().x + getWriteBox().width-40, getWriteBox().y, 40, 40);
				
//				g.setStrokeStyle(CssColor.make(255, 0, 0));
//				g.setLineWidth(1.0d);
//				
//				ArrayList<DoubleRectangle> boxes = strokeContainer.getMainLine().getBoxes();
//				for(int i=0 ; i<boxes.size() ; i++) {
//					int xx = (int)boxes.get(i).x;
//					int yy = (int)boxes.get(i).y;
//					int ww = (int)boxes.get(i).width;
//					int hh = (int)boxes.get(i).height;
//					g.beginPath();
//					//g.rect(getBox().x,(int)strokeContainer.averageBaseLine-strokeContainer.averageHeight, getBox().width, strokeContainer.averageHeight);
//					g.rect(xx,yy,ww,hh);
//					g.closePath();
//					g.stroke();
//					
//				}
//				g.setFillStyle(CssColor.make("rgba(200, 200, 200, 0.4)"));
//				ArrayList<WMObjectLine> lines = strokeContainer.getMainLine().getLines();
//				for(int i=0 ; i<lines.size() ; i++) {
//					int xx = (int)lines.get(i).getBox().x;
//					int avb = (int)lines.get(i).getAverageBaseLine();
//					int ww = (int)lines.get(i).getBox().width;
//					int avh = (int)lines.get(i).getAverageHeight();
//					g.fillRect(xx,avb-avh,ww,avh);
//				}
				
				if(!recognizeOff && formuleViewer!=null) {
					int x = Math.max(getWriteBox().x+50, getBox().x) ;// + getBox().width/2-parent.formuleViewer.getWidth()/2;
					int y = getWriteBox().y+5;//-20-formuleViewer.getHeight();
					g.setFillStyle(CssColor.make(255, 255, 255));
					g.fillRect(getFormulaArea().x, getFormulaArea().y, getFormulaArea().width,getFormulaArea().height);//formuleViewer.getMainRegel().width, formuleViewer.getMainRegel().height);
					
					g.translate(x, y);
					if(!"".equals(getFormulaString()) && getFormulaString()!=null) {
						formuleViewer.setColor(CssColor.make(38, 115, 182));
						formuleViewer.setFont(FormuleFont.createFromFontSize(16));
						formuleViewer.getMainRegel().paintAll(g);
					}
					g.translate(-x, -y);
				}
				
				g.setFillStyle(CssColor.make(255, 243, 180));
				if(!recognizeOff)
					g.fillRect(getWriteBox().x+getWriteBox().width-42, getWriteBox().y-4, 47, getWriteBox().height+8);
				if(recognizeOff) {
					g.fillRect(getWriteBox().x-4, getWriteBox().y-4, getWriteBox().width+8,47);
				}
				
				if(!recognizeOff) {
					g.setFillStyle(CssColor.make(255, 255, 255));
					g.fillRect(getHandleArea().x, getHandleArea().y, getHandleArea().width, getHandleArea().height);
					drawHandle(g,getHandleArea());
				}
				
				//g.setStrokeStyle(CssColor.make(80, 80, 80));

				drawcloseButton(g, getCloseButtonArea());
				
				
				if(recognizeOff && !correct && !isfalse && !isHalf)
					//g.drawImage(eyeImageElement, getWriteBox().x+5, getWriteBox().y+5);
					drawRecognizeButton(g,getRecognizeButtonArea());
				
				if(!isInputSC && !correct && !isfalse && !isHalf)
					//g.drawImage(eyeImageElement, getWriteBox().x+5, getWriteBox().y+5);
					drawNotRecognizeButton(g,getNotRecognizeButtonArea());
				
				if(!recognizeOff && isGetalsExpressie && parent.calculator)
					g.drawImage(approxImageElement, getWriteBox().x+getWriteBox().width-32, getWriteBox().y+getWriteBox().height/2-19);
				
//				if(!recognizeOff) {
//					g.drawImage(formuleschrijfImageElement, getWriteBox().x+getWriteBox().width-110, getWriteBox().y+5);
//					drawRecognizeButton(g,new Rectangle(getWriteBox().x+getWriteBox().width-80, getWriteBox().y+5, 25,25));
//				}
				
				if(recognizeOff) {
					g.setFont("16px arial");
					g.fillText("TEKENING", getWriteBox().x+250, getWriteBox().y+25);
					drawEraserButton(g,getEraserButtonArea());
					drawPenButton(g,getPenButtonArea());
					drawBinButton(g,getBinButtonArea());
					drawUndoButton(g,getUndoButtonArea());
				}
				if(erasing)
					drawEraser(g,new Rectangle(erasingX-25, erasingY-25, 25,25));
				
				if(parent.eigenaar.comRoot!=null && parent.eigenaar.comRoot.hasListeners("action.check") && !recognizeOff)
					drawCheckButton(g, getCheckButtonArea());
				
				
				
				
				
				
//				g.setFillStyle(CssColor.make(255, 255, 255));
//				
//				g.fillRect(getWriteBox().x + getWriteBox().width-77, getWriteBox().y+3, 34, 34);
//				g.setStrokeStyle(CssColor.make(0, 200, 0));
//				g.setLineWidth(5.0d);
//				g.beginPath();
//				g.moveTo(getWriteBox().x + getWriteBox().width-70, getWriteBox().y+10);
//				g.lineTo(getWriteBox().x + getWriteBox().width-60, getWriteBox().y+30);
//				g.lineTo(getWriteBox().x + getWriteBox().width-40, getWriteBox().y+0);
//				g.moveTo(getWriteBox().x + getWriteBox().width-70, getWriteBox().y+10);
//				g.closePath();
//				g.stroke();
				
				g.setStrokeStyle(drawingColor);
				g.setLineWidth(3.0d);
				
				if(correct||isfalse||isHalf) {
					//g.setFillStyle(CssColor.make(240, 255, 240));
					if(correct)
						g.drawImage(parent.goedvinkImageElement, getWriteBox().x + 20-14, getWriteBox().y + 20-14);
						//g.setFillStyle(CssColor.make(0, 200, 0));
					if(isfalse)
						g.drawImage(parent.foutkruisImageElement, getWriteBox().x + 20-14, getWriteBox().y + 20-14);
						//g.setFillStyle(CssColor.make(200, 0, 0));
					if(isHalf)
						g.drawImage(parent.halfvinkImageElement, getWriteBox().x + 20-14, getWriteBox().y + 20-14);
						//g.setFillStyle(CssColor.make(240, 240, 0));
//					g.beginPath();
//					g.arc(getWriteBox().x + 20, getWriteBox().y + 20 , 8, 0, 8* Math.PI);
//					g.closePath();
//					g.stroke();
//					g.fill();
				}
				
			}
			else {
				//g.setFillStyle(CssColor.make(243, 241, 239));
				g.setFillStyle(CssColor.make(255, 255, 255));
				g.setLineWidth(1.5d);
				if(correct||isfalse||isHalf) {
					//g.setFillStyle(CssColor.make(240, 255, 240));
					//g.fillRect(getBox().x-30, getBox().y-5, getBox().width+35, getBox().height+10);
					if(correct)
						g.drawImage(parent.goedvinkImageElement, getBox().x-20-14, getBox().y+getBox().height/2-7);
						//g.setFillStyle(CssColor.make(0, 200, 0));
					if(isfalse)
						g.drawImage(parent.foutkruisImageElement, getBox().x-20-14, getBox().y+getBox().height/2-7);
						//g.setFillStyle(CssColor.make(200, 0, 0));
					if(isHalf)
						g.drawImage(parent.halfvinkImageElement, getBox().x-20-14, getBox().y+getBox().height/2-7);
						//g.setFillStyle(CssColor.make(240, 240, 0));
//					g.beginPath();
//					g.arc(getBox().x-20, getBox().y+getBox().height/2 , 5, 0, 5* Math.PI);
//					g.closePath();
//					g.stroke();
//					g.fill();
				}
				//else
					//g.fillRect(getBox().x-5, getBox().y-5, getBox().width+10, getBox().height+10);
			}	
		}
		
		if(active && popupMode) {
			Rectangle wbox = new Rectangle(20,20,parent.breedte-40,parent.hoogte-40);
			g.setFillStyle(CssColor.make(255, 255, 255));
			g.fillRect(wbox.x, wbox.y, wbox.width, wbox.height);
			g.setFillStyle(CssColor.make(255, 243, 180));
			g.fillRect(wbox.x+wbox.width-42, wbox.y-5, 47, wbox.height+10);
			drawShadow(g,new Rectangle((int)wbox.x-5, (int)wbox.y-5, (int)wbox.width+10, (int)wbox.height+10));
			drawGrid(g,new Rectangle((int)wbox.x-5, (int)wbox.y-5, (int)wbox.width+10, (int)wbox.height+10));
			
			g.setStrokeStyle(CssColor.make(80, 80, 80));
			
			//g.setFillStyle(CssColor.make(239, 241, 243));
			//g.fillRect(wbox.x + wbox.width-40, wbox.y, 40, 40);

			drawcloseButton(g, getCloseButtonArea());
			if(!recognizeOff && formuleViewer!=null) {
				int x = Math.max(wbox.x+50, getBox()!=null ? getBox().x : 0) ;// + getBox().width/2-parent.formuleViewer.getWidth()/2;
				int y = wbox.y+5;//-20-formuleViewer.getHeight();
				g.translate(x, y);
				formuleViewer.setFont(FormuleFont.createFromFontSize(16));
				formuleViewer.setColor(CssColor.make(38, 115, 182));
				formuleViewer.getMainRegel().paintAll(g);
				g.translate(-x, -y);
			}
			
			if(parent.eigenaar.comRoot!=null && parent.eigenaar.comRoot.hasListeners("action.check"))
				drawCheckButton(g, getCheckButtonArea());
			
			if(correct||isfalse||isHalf) {
				//g.setFillStyle(CssColor.make(240, 255, 240));
				if(correct)
					g.setFillStyle(CssColor.make(0, 200, 0));
				if(isfalse)
					g.setFillStyle(CssColor.make(200, 0, 0));
				if(isHalf)
					g.setFillStyle(CssColor.make(240, 240, 0));
				g.beginPath();
				g.arc(wbox.x + 20, wbox.y + 20 , 8, 0, 8* Math.PI);
				g.closePath();
				g.stroke();
				g.fill();
			}
		}
		
		if(proActive) {
			g.setFillStyle(CssColor.make(240, 240, 240));
			drawProActiveAura(g, new Rectangle(getBox().x,getBox().y,getBox().width, getBox().height));
		}
		
		if(active || recognizeOff || !formuleModus) {
			if(!strokeContainer.isParseable())
				g.setStrokeStyle(CssColor.make(38, 115, 182));
			else
				g.setStrokeStyle(CssColor.make(80, 80, 80));
			ArrayList<Stroke> strokes = strokeContainer.getStrokes();
			Rectangle r = null;
			if(active && getWriteBox()!=null) {
				if(!recognizeOff)
					r = new Rectangle(getWriteBox().x, getWriteBox().y, getWriteBox().width-47, getWriteBox().height);
				else
					r = new Rectangle(getWriteBox().x, getWriteBox().y+47, getWriteBox().width, getWriteBox().height-47);
			}
				
			for(int i = 0 ; i < strokes.size() ; i++) {
				Stroke stroke = strokes.get(i);
				g.beginPath();
				double x0 = (int)stroke.getParsePoints().get(0).x;
				double y0 = (int)stroke.getParsePoints().get(0).y;
				if(!active||r==null)
					g.moveTo(x0, y0);
				else if(r.contains((int)x0,(int)0))
					g.moveTo(x0, y0);
				
				if(stroke.getParsePointsbox().width>3 ||  stroke.getParsePointsbox().height>3) {
					for(int j = 1 ; j < stroke.getParsePoints().size() ; j++) {
						double x = stroke.getParsePoints().get(j).x ;
						double y = stroke.getParsePoints().get(j).y;
						if(!active||r==null)
							g.lineTo(x, y);
						else if(r.contains((int)x,(int)y))
							g.lineTo(x, y);
					}
					g.moveTo(x0, y0);
					g.closePath();
					g.stroke();
				}
				else {
					g.arc(x0, y0, 1.5, 0, 1.5* Math.PI);
					g.closePath();
					g.stroke();
				}
			}
			if(active && recognizeOff) {
				g.setFillStyle(CssColor.make(255, 255, 255));
				g.fillRect(getHandleArea().x, getHandleArea().y, getHandleArea().width, getHandleArea().height);
				drawHandle(g,getHandleArea());
			}
		}
		else {
			formuleViewer.setFont(FormuleFont.createFromFontSize(18));
			formuleViewer.setColor(CssColor.make(80,80,80));
			g.translate(getBox().x, getBox().y);
			formuleViewer.getMainRegel().paintAll(g);
			g.translate(-getBox().x, -getBox().y);
			g.setLineWidth(3.0d);
		}
	}
	
	public void setDefaultRectangle(Rectangle r) {
		defaultBox = r;
		if(box==null)
			box = defaultBox;
	}
	
	public void setFormuleModus(boolean b) {
		formuleModus = b;
	}
	
	public void setCorrect(boolean correct) {
//		if(!correct && this.correct)
//			activeTranslation -=25;
//		if(correct && !this.correct)
//			activeTranslation -=25;
		this.correct = correct;
		if(correct) {
			isfalse = false;
			isHalf = false;
		}
	}
	
	public boolean isCorrect() {
		return correct;
	}
	
	public void setFalse(boolean isfalse) {
//		if(!isfalse && this.isfalse)
//			activeTranslation -=25;
//		if(isfalse && !this.isfalse)
//			activeTranslation -=25;
		this.isfalse = isfalse;
		if(isfalse) {
			correct = false;
			isHalf = false;
		}
	}
	
	public boolean isFalse() {
		return isfalse;
	}
	
	public void setHalf(boolean isHalf) {
//		if(!isfalse && this.isfalse)
//			activeTranslation -=25;
//		if(isfalse && !this.isfalse)
//			activeTranslation -=25;
		this.isHalf = isHalf;
		if(isHalf) {
			correct = false;
			isfalse = false;
		}
	}
	
	public boolean isHalf() {
		return isHalf;
	}
	
	public void parseAllStrokes() {
		ArrayList<Stroke> strokes = new ArrayList<Stroke>();
		for (int i = 0; i < strokeContainer.getStrokes().size(); i++) {	
			strokes.add(strokeContainer.getStrokes().get(i));
		}
		strokeContainer.removeWmObjects();
		strokeContainer.removeStrokes();
		boolean swapped = true;
		while (swapped)	{	
			swapped = false;
			for (int i = 1; i < strokes.size(); i++) {	
				Stroke stroke1 = strokes.get(i-1);
				Stroke stroke2 = strokes.get(i);
				if (stroke1.getTimeStamp() > stroke2.getTimeStamp()) {	
					strokes.set(i-1, stroke2);
					strokes.set(i, stroke1);
					swapped = true;
				}
			}
		}
		for (int i = 0; i < strokes.size(); i++) {	
			strokeContainer.addStroke(strokes.get(i));
		}
		
		
		formuleViewer = new FormuleViewer(strokeContainer.getFormulaString());
		formuleViewer.setColor(CssColor.make(38, 115, 182));
		
	}
	
	public void setNr(int nr) {
		this.nr = nr;
	}
	
	public void setRecognizeOff(boolean b) {
		recognizeOff=b;
		if(b) {
			strokeContainer.removeWmObjects();
			correct = false;
			isfalse = false;
			isHalf = false;
			numHistories = 0;
			addToHistory();
		}
		else {
			writeBox = null;
			parseAllStrokes();
			eraserActive = false;
		}
		
	}
	
	public boolean getRecognizeOff() {
		return recognizeOff;
	}
	
	public void setEraserActive(boolean b) {
		eraserActive = eraserActive ? false : b;
	}
	
	public void setErasing(boolean b, int x, int y) {
		erasing = b;
		erasingX = x;
		erasingY = y;
	}
	
	public boolean getEraserActive() {
		return eraserActive;
	}
	
	public void setProActive (boolean b) {
		proActive = b;
	}
	public void setActive (boolean b) {
		if(active && !b)
			changedFromActive = true;
		if(!active && b)
			changedToActive = true;
		if(b && parent.showWriting)
			formuleModus=false;
		active = b;
		if(active && getBox()!=null) {
			int extraRuimteRechts = recognizeOff ? 40 : 80;
			//activeTranslationX = Math.max(0, getBox().x+getBox().width+extraLinks+47 - parent.breedte-20);
			
			activeTranslationX = 0; 
			if(getBox().x<70)
				activeTranslationX = getBox().x-70;
			if(getBox().x+getBox().width > parent.breedte-(70+extraRuimteRechts)) 
				activeTranslationX = getBox().x+getBox().width - (parent.breedte-(70+extraRuimteRechts));
			
			activeTranslationY = 0; 
			if(getBox().y<70)
				activeTranslationY = getBox().y-70;
			if(getBox().y+getBox().height > parent.hoogte-70) 
				activeTranslationY = Math.min(getBox().y-70  ,  getBox().y+getBox().height - (parent.hoogte-70));
			
			translate((int)-activeTranslationX,(int)-activeTranslationY);
			//defaultBox.translate((int)-activeTranslationX,(int)-activeTranslationY);
		}
		else if(getBox()!=null) {
			translate((int)activeTranslationX, (int)activeTranslationY);
			//defaultBox.translate((int)activeTranslationX, (int)activeTranslationY);
		}
		
		
		
//		if(b) {
//			updatePopupSvg();
//			updateGridSvg();
//			updateStrokesSvg();
//			
//		}
//		else {
//			svg.removeChild(svgPopup);
//			svg.removeChild(svgGrid);
//			//svg.removeChild(svgStrokes);
//			updateStrokesSvg();
//		}
			
	}
	
	public void setpopupMode (boolean b) {
		popupMode = b;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public String getFormulaString() {
		if(recognizeOff)
			return "";
		if(strokeContainer.isParseable())
			return strokeContainer.getFormulaString();
		return "";
	}
	
	
	public DoubleRectangle getBoundingBox() {
		return strokeContainer.getBoundingBox();
	}
	
	public Rectangle getBox() {
		if(box == null && strokeContainer != null && strokeContainer.getBoundingBox()!=null) {
			
			int x = (int)strokeContainer.getBoundingBox().x;
			int y = (int)strokeContainer.getBoundingBox().y;
			int width = (int)strokeContainer.getBoundingBox().width;
			int height = (int)strokeContainer.getBoundingBox().height;
			if(formuleModus && !active && !recognizeOff) {
				width = formuleViewer.getWidth();
				height = formuleViewer.getHeight();
			}
				
			box = new Rectangle(x, y, width, height);
			if(width<0 || height<0)
				box=null;
				
		}
		if(box==null)
			box = defaultBox;
		
		
		return box;
	}
	
	public Rectangle getDefaultBox() {
		return defaultBox;
	}
	
	public Rectangle getDragBox() {
		if(box == null && strokeContainer != null && strokeContainer.getBoundingBox()!=null) {
			int x = (int)strokeContainer.getBoundingBox().x;
			int y = (int)strokeContainer.getBoundingBox().y;
			int width = (int)strokeContainer.getBoundingBox().width;
			int height = (int)strokeContainer.getBoundingBox().height;
			box = new Rectangle(x, y, width, height);
		}
		return box;
	}
	
	public Rectangle getWriteBox() {
		int margin = 60;
		
		if(writeBox==null && strokeContainer != null && getBox()!=null) { // strokeContainer.getBoundingBox()!=null
			
			Rectangle b = getBox();
			int xSC = b.x;
			int ySC = b.y;
			int widthSC =  Math.max(60, b.width);
			int heightSC = Math.max(30, b.height);
			
			int x = Math.max(20,xSC - margin);
			int y = (int)Math.max(20, ySC - margin-10);
			
			int width = Math.min(parent.breedte-40, widthSC+3*margin+47); //47 is breedte gele strook met knoppen)
			int height = Math.min(parent.hoogte-40, heightSC + 2*margin);
			width = Math.max(width, height);
			
			x = (int)Math.min(x, parent.breedte - width -20);
			y = (int)Math.min(y, parent.hoogte - height -20);
			
			
			writeBox = new Rectangle(x, y, width, height);
		}
		if(recognizeOff) {
			int height = parent.hoogte-40;
			int width = parent.breedte-40;//Math.max(writeBox.width,height);
			int x = writeBox.x;
			int y = writeBox.y;
			x = (int)Math.min(x, parent.breedte - width -20);
			y = (int)Math.min(y, parent.hoogte - height -20);
			writeBox = new Rectangle(x, y, width, height);
		}
		return writeBox;
	}
	
	public String getStrokeCode() {
		String code = "";
		for(int i=0 ; i<strokeContainer.getStrokes().size() ; i++) {
			code += strokeContainer.getStrokes().get(i).getTestCode();
			code += "\n";
		}
		return code;
	}
	
	public boolean contains(int x, int y) {
		if(getBox()==null)
			return false;
		Rectangle r = new Rectangle(getBox().x ,getBox().y ,getBox().width ,getBox().height);
		return r.contains(x, y);
	}
	
	public boolean writeBoxContains(int x, int y) {
		if(getWriteBox()==null)
			return false;
		return getWriteBox().contains(x, y);
	}
	
	public boolean isNotRelevant() {
		if(strokeContainer.getStrokes().size()==0 || strokeContainer.getDiagonal()<15)
			return true;
		return false;
	}
	
	public boolean isNotRelevantWhenReady() {
		if(strokeContainer.getStrokes().size()==0) {
			return true;

		}
		if(strokeContainer.getStrokes().size()==1) {
			double length = strokeContainer.getStrokes().get(0).getLength();
			if(length<35)
				return true;

		}
		if(strokeContainer.getStrokes().size()==2) {
			double length1 = strokeContainer.getStrokes().get(0).getLength() ;
			double length2 = strokeContainer.getStrokes().get(1).getLength();
			if(length1<35 && length2<35)
				return true;

		}
		if(strokeContainer.getStrokes().size()==3) {
			double length1 = strokeContainer.getStrokes().get(0).getLength() ;
			double length2 = strokeContainer.getStrokes().get(1).getLength();
			double length3 = strokeContainer.getStrokes().get(2).getLength();
			if(length1<35 && length2<35 && length3<35)
				return true;

		}
		return false;
		
	}
	
	public boolean contains(int x, int y, int margin) {
		if(strokeContainer != null && strokeContainer.getBoundingBox()!=null) {
			int xb = (int)strokeContainer.getBoundingBox().x;
			int yb = (int)strokeContainer.getBoundingBox().y;
			int width = (int)strokeContainer.getBoundingBox().width;
			int height = (int)strokeContainer.getBoundingBox().height;
			int leftMargin = 0;
			if(correct||isHalf||isfalse)
				leftMargin = 40;
			Rectangle box = new Rectangle(xb-margin-leftMargin, yb-margin, width+2*margin+leftMargin, height+2*margin);
			if(box.contains(x,y))
				return true;
		}
		return false;
	}
	
	public void translate(int dx, int dy) {
		strokeContainer.translate(dx, dy);
		box = null;
		writeBox = null;
	}
	
	public void scale(double factor) {
		strokeContainer.scale(factor);
		box = null;
		writeBox = null;
	}
	
	public void scale(double cx, double cy,double factor) {
		strokeContainer.scale(cx, cy, factor);
		box = null;
		writeBox = null;
	}
	
	public double getDiagonal() {
		return strokeContainer.getDiagonal();
	}
	
	public HashMap<String,Object> getState() {
		HashMap<String,Object> map = strokeContainer.getState();
		map.put("correct", new Boolean(correct));
		map.put("isfalse", new Boolean(isfalse));
		map.put("isHalf", new Boolean(isHalf));
		map.put("recognizeOff", new Boolean(recognizeOff));
		return map;
	}
	
	public void setState (Map<String,Object> map) {
		strokeContainer.setState(map);
		ObjectMap launchState = JSONUtilities.wrapMap(map);
		if(launchState.containsKey("correct"))
			correct = launchState.getBoolean("correct");
		if(launchState.containsKey("isfalse"))
			isfalse = launchState.getBoolean("isfalse");
		if(launchState.containsKey("isHalf"))
			isHalf = launchState.getBoolean("isHalf");
		if(launchState.containsKey("recognizeOff"))
			recognizeOff = launchState.getBoolean("recognizeOff");
		
		formuleViewer = new FormuleViewer(strokeContainer.getFormulaString());
		formuleViewer.setColor(CssColor.make(38, 115, 182));
	}
	
//	private void initSvg() {
//		svg = parent.doc.createSVGSVGElement();
//		svgPopup = parent.doc.createSVGSVGElement();
//		svgGrid = parent.doc.createSVGSVGElement();
//		svgStrokes = parent.doc.createSVGSVGElement();
//	}
//	
//	public void updatePopupSvg() {
//		for(int i=0 ; i<svgPopupNodes.size() ; i++) {
//			try {
//				svgPopup.removeChild(svgPopupNodes.get(i));
//			} catch(Exception e) {}
//		}
//		svgPopup.getX().getBaseVal().setValue(0);
//		svgPopup.getY().getBaseVal().setValue(0);
//		if(getWriteBox()==null)
//			return;
//		float popupX = getWriteBox().x-5;
//		float popupY = getWriteBox().y-5;
//		float popupWidth = getWriteBox().width+10;
//		float popupHeight = getWriteBox().height+10;
//		OMSVGRectElement popup = parent.doc.createSVGRectElement(popupX, popupY, popupWidth, popupHeight, 0, 0);
//		popup.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, SVGConstants.CSS_WHITE_VALUE);
//		svgPopupNodes.add(popup);
//		svgPopup.appendChild(popup);
//		
//		//shadow
//		for(int i=0 ;i<10 ; i++) {
//			CssColor c = CssColor.make("rgba("+(200+5*i)+","+(200+5*i)+","+(200+5*i)+","+(1-0.1*i)+")");
//			OMSVGRectElement shadow = parent.doc.createSVGRectElement(popupX-1-i, popupY-1-i, popupWidth+2+2*i, popupHeight+2+2*i, 0, 0);
//			shadow.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "transparent");
//			shadow.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY,""+c);
//			shadow.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "2.0");
//			svgPopupNodes.add(shadow);
//			svgPopup.appendChild(shadow);
//		}
//		
//		CssColor toolbarColor = CssColor.make(255, 243, 180);
//		rectToolbarForm = parent.doc.createSVGRectElement(popupX+popupWidth-47, popupY, 47, popupHeight, 0, 0);
//		rectToolbarForm.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, ""+toolbarColor);
//		rectToolbarDraw = parent.doc.createSVGRectElement(popupX, popupY, popupWidth, 47, 0, 0);
//		rectToolbarDraw.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, ""+toolbarColor);
//		
//		if(recognizeOff) {
//			svgPopupNodes.add(rectToolbarDraw);
//			svgPopup.appendChild(rectToolbarDraw);
//		}
//		else {
//			svgPopupNodes.add(rectToolbarForm);
//			svgPopup.appendChild(rectToolbarForm);
//		}
//	}
//	
//	public void updateGridSvg() {
//		for(int i=0 ; i<svgGridNodes.size() ; i++) {
//			try {
//				svgGrid.removeChild(svgGridNodes.get(i));
//			} catch(Exception e) {}
//		}
//		svgGrid.getX().getBaseVal().setValue(0);
//		svgGrid.getY().getBaseVal().setValue(0);
//		CssColor gridColor = CssColor.make(180,195,228);
//		if(getWriteBox()==null)
//			return;
//		float popupX = getWriteBox().x-5;
//		float popupY = getWriteBox().y-5;
//		float popupWidth = getWriteBox().width+10-47;
//		float popupHeight = getWriteBox().height+10;
//		Point c = parent.getActiveTranslation();
//		int cx = (c.x-getWriteBox().x)%20;
//		int cy = (c.y-getWriteBox().y)%20;
//		int	lineDistance = (int)(10*parent.schrijfLeesFactor);
//		int vSteps = (int)popupHeight / lineDistance;
//		for (int vCnt = 1; vCnt <= vSteps+1; vCnt++) {
//			OMSVGLineElement stroke = parent.doc.createSVGLineElement(popupX, popupY + cy + vCnt * lineDistance, popupX + popupWidth, popupY + cy + vCnt * lineDistance);
//			stroke.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, gridColor.toString());
//			stroke.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "" + 0.4);
//			svgGridNodes.add(stroke);
//			svgGrid.appendChild(stroke);
//		}
//		int hSteps = (int)popupWidth / lineDistance;
//		for (int hCnt = 1; hCnt <= hSteps; hCnt++) {
//			OMSVGLineElement stroke = parent.doc.createSVGLineElement(popupX + hCnt * lineDistance, popupY, popupX + hCnt * lineDistance , popupY + popupHeight);
//			stroke.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, gridColor.toString());
//			stroke.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "" + 0.4);
//			svgGridNodes.add(stroke);
//			svgGrid.appendChild(stroke);
//		}
//	}
//	
//	public void updateStrokesSvg() {
//		for(int i=0 ; i<svgStrokesNodes.size() ; i++) {
//			try {
//				svgStrokes.removeChild(svgStrokesNodes.get(i));
//			} catch(Exception e) {}
//		}
//		svgStrokes.getX().getBaseVal().setValue(0);
//		svgStrokes.getY().getBaseVal().setValue(0);
//		CssColor strokeColor = CssColor.make(80,80,80);
//		ArrayList<Stroke> strokes = strokeContainer.getStrokes();
//		for(int i = 0 ; i < strokes.size() ; i++) {
//			Stroke stroke = strokes.get(i);
//			OMSVGPathElement strokePath = parent.doc.createSVGPathElement();
//			OMSVGPathSegList segsStrokePath = strokePath.getPathSegList();
//			float x0 = (float)stroke.getParsePoints().get(0).x;
//			float y0 = (float)stroke.getParsePoints().get(0).y;
//			//if(r.contains((int)x0,(int)0))
//				segsStrokePath.appendItem(strokePath.createSVGPathSegMovetoAbs(x0,y0));
//			if(stroke.getParsePointsbox().width>3 ||  stroke.getParsePointsbox().height>3) {
//				for(int j = 1 ; j < stroke.getParsePoints().size() ; j++) {
//					float x = (float)stroke.getParsePoints().get(j).x ;
//					float y = (float)stroke.getParsePoints().get(j).y;
//					//if(r.contains((int)x,(int)y))
//						segsStrokePath.appendItem(strokePath.createSVGPathSegLinetoAbs(x,y));
//				}
//			}
//			else {
//				segsStrokePath.appendItem(strokePath.createSVGPathSegArcAbs(x0+0.01f, y0, 1, 1, 360, true, true));
//			}
//			segsStrokePath.appendItem(strokePath.createSVGPathSegMovetoAbs(x0,y0));
//			segsStrokePath.appendItem(strokePath.createSVGPathSegClosePath());
//			strokePath.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, ""+strokeColor);
//			strokePath.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "transparent");
//			strokePath.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "" + 3.0);
//			svgStrokesNodes.add(strokePath);
//			svgStrokes.appendChild(strokePath);
//		}		
//	}
//	
//	public void addStrokeSvg(Stroke stroke) {
//		CssColor strokeColor = CssColor.make(80,80,80);
//		OMSVGPathElement strokePath = parent.doc.createSVGPathElement();
//		OMSVGPathSegList segsStrokePath = strokePath.getPathSegList();
//		float x0 = (float)stroke.getParsePoints().get(0).x;
//		float y0 = (float)stroke.getParsePoints().get(0).y;
//		//if(r.contains((int)x0,(int)0))
//			segsStrokePath.appendItem(strokePath.createSVGPathSegMovetoAbs(x0,y0));
//		if(stroke.getParsePointsbox().width>3 ||  stroke.getParsePointsbox().height>3) {
//			for(int j = 1 ; j < stroke.getParsePoints().size() ; j++) {
//				float x = (float)stroke.getParsePoints().get(j).x ;
//				float y = (float)stroke.getParsePoints().get(j).y;
//				//if(r.contains((int)x,(int)y))
//					segsStrokePath.appendItem(strokePath.createSVGPathSegLinetoAbs(x,y));
//			}
//		}
//		else {
//			segsStrokePath.appendItem(strokePath.createSVGPathSegArcAbs(x0+0.01f, y0, 1, 1, 360, true, true));
//		}
//		segsStrokePath.appendItem(strokePath.createSVGPathSegMovetoAbs(x0,y0));
//		segsStrokePath.appendItem(strokePath.createSVGPathSegClosePath());
//		strokePath.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, ""+strokeColor);
//		strokePath.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "transparent");
//		strokePath.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "" + 3.0);
//		svgStrokesNodes.add(strokePath);
//		svgStrokes.appendChild(strokePath);
//	}
	
}

