package fi.mathscratchgwt.client;

import java.util.ArrayList;

import org.vectomatic.dom.svg.OMNode;
import org.vectomatic.dom.svg.OMSVGCircleElement;
import org.vectomatic.dom.svg.OMSVGDocument;
import org.vectomatic.dom.svg.OMSVGLineElement;
import org.vectomatic.dom.svg.OMSVGPathElement;
import org.vectomatic.dom.svg.OMSVGPathSegList;
import org.vectomatic.dom.svg.OMSVGRectElement;
import org.vectomatic.dom.svg.OMSVGSVGElement;
import org.vectomatic.dom.svg.utils.SVGConstants;

import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.dom.client.Style.Unit;

import java.util.HashMap;

import fi.writemathgwt.client.engine.Point;
import fi.writemathgwt.client.engine.Stroke;
import nl.uu.fi.dwo.interaction.client.FormuleFont;



public class SVGManager {
	
	public static CssColor colorBlue1 = CssColor.make(49,71,112);
	public static CssColor colorBlue2 = CssColor.make(38,115,182);
	public static CssColor colorBlue3 = CssColor.make(120,150,202);
	public static CssColor colorBlue4 = CssColor.make(180,195,228);
	public static CssColor colorBlue5 = CssColor.make(211,229,244);
	public static CssColor colorBlue6 = CssColor.make(229,240,249);
	
	public static CssColor colorGray1 = CssColor.make(206,207,208);
	public static CssColor colorGray2 = CssColor.make(221,223,225);
	public static CssColor colorGray3 = CssColor.make(237,239,241);
	
	public static CssColor strokeColor = CssColor.make(80,80,80);
	public static CssColor toolbarColor = CssColor.make(255, 243, 180);
	
	
	private MathScratchField mathScratchField;
	private OMSVGDocument doc;
	private OMSVGSVGElement svg;
	private int width;
	private int height;
	
	private OMSVGPathElement bin;
	private OMSVGPathElement undo;
	
	private OMSVGPathElement formulaStroke;
	private OMSVGPathSegList segsFormulaStroke;
	
	private ArrayList<OMSVGSVGElement> svgStrokeContainers = new ArrayList<OMSVGSVGElement>();
	private OMSVGSVGElement svgSCShadow;
	
	private OMSVGSVGElement svgCurrentSC;
	private OMSVGSVGElement svgCurrentSCStrokes;
	private OMSVGSVGElement svgCurrentSCGrid;
	private OMSVGSVGElement svgCheckButton;
	private OMSVGSVGElement svgCloseButton;
	private OMSVGSVGElement svgCalculatorButton;
	private OMSVGSVGElement svgDrawButton;
	private OMSVGSVGElement svgFormButton;
	private OMSVGSVGElement svgHandleButton;
	private OMSVGSVGElement svgPenButton;
	private OMSVGSVGElement svgEraserButton;
	private OMSVGSVGElement svgUndoButton;
	private OMSVGSVGElement svgBinButton;
	
	
	private ArrayList<OMNode> currentSCNodes = new ArrayList<OMNode>(); 
	private ArrayList<OMNode> currentSCStrokeNodes = new ArrayList<OMNode>(); 
	private ArrayList<OMNode> currentSCGridNodes = new ArrayList<OMNode>(); 
	private ArrayList<ArrayList<OMNode>> scNodes = new ArrayList<ArrayList<OMNode>>();
	
	private HashMap<KStrokeContainer,OMSVGSVGElement> scMap = new HashMap<KStrokeContainer, OMSVGSVGElement>();
	private HashMap<Stroke,OMSVGPathElement> currentSCStrokeMap = new HashMap<Stroke, OMSVGPathElement>();

	
	public SVGManager(MathScratchField mathScratchField, OMSVGDocument doc, OMSVGSVGElement svg, int width, int height) {
		this.mathScratchField = mathScratchField;
		this.doc = doc;
		this.svg = svg;
		this.width = width;
		this.height = height;
		
		svgCurrentSC = doc.createSVGSVGElement();
		svgCurrentSCStrokes = doc.createSVGSVGElement();
		svgCurrentSCGrid = doc.createSVGSVGElement();
		
		initCloseButtonSVG();
		initCheckButtonSVG();
		initCalculatorButtonSVG();
		initDrawButtonSVG();
		initFormButtonSVG();
		initHandleButtonSVG();
		initPenButtonSVG();
		initEraserButtonSVG();
		initUndoButtonSVG();
		initBinButtonSVG();
	}
	
	public void drawGoedSVG(OMSVGSVGElement svgGoed) {
		OMSVGCircleElement goed = doc.createSVGCircleElement(9, 9, 9);
		goed.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, ""+CssColor.make(74,163,91));
		svgGoed.appendChild(goed);
		
		OMSVGPathElement vink = doc.createSVGPathElement();
		OMSVGPathSegList segsVink = vink.getPathSegList();
		segsVink.appendItem(vink.createSVGPathSegMovetoAbs(4, 9));
		segsVink.appendItem(vink.createSVGPathSegLinetoAbs(8, 12));
		segsVink.appendItem(vink.createSVGPathSegLinetoAbs(13, 5));
		segsVink.appendItem(vink.createSVGPathSegMovetoAbs(4, 9));
		segsVink.appendItem(vink.createSVGPathSegClosePath());
		vink.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "white");
		vink.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "transparent");
		vink.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "" + 2.4);
		
		svgGoed.appendChild(vink);
	}
	
	public void drawFoutSVG(OMSVGSVGElement svgFout) {
		OMSVGCircleElement fout = doc.createSVGCircleElement(9, 9, 9);
		fout.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, ""+CssColor.make(217,56,49));
		svgFout.appendChild(fout);
		
		OMSVGPathElement kruis = doc.createSVGPathElement();
		OMSVGPathSegList segsKruis = kruis.getPathSegList();
		segsKruis.appendItem(kruis.createSVGPathSegMovetoAbs(5, 5));
		segsKruis.appendItem(kruis.createSVGPathSegLinetoAbs(13, 13));
		segsKruis.appendItem(kruis.createSVGPathSegMovetoAbs(5, 13));
		segsKruis.appendItem(kruis.createSVGPathSegLinetoAbs(13, 5));
		segsKruis.appendItem(kruis.createSVGPathSegClosePath());
		kruis.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "white");
		kruis.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "transparent");
		kruis.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "" + 2.0);
		
		svgFout.appendChild(kruis);
	}
	
	public void drawHalfSVG(OMSVGSVGElement svgHalf) {
		OMSVGCircleElement half = doc.createSVGCircleElement(9, 9, 8.4f);
		half.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, ""+CssColor.make(253,232,53));
		half.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, ""+CssColor.make(150,150,150));
		half.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "" + 1.2);
		svgHalf.appendChild(half);
		
		OMSVGPathElement vink = doc.createSVGPathElement();
		OMSVGPathSegList segsVink = vink.getPathSegList();
		segsVink.appendItem(vink.createSVGPathSegMovetoAbs(4, 9));
		segsVink.appendItem(vink.createSVGPathSegLinetoAbs(8, 12));
		segsVink.appendItem(vink.createSVGPathSegLinetoAbs(13, 5));
		segsVink.appendItem(vink.createSVGPathSegMovetoAbs(4, 9));
		segsVink.appendItem(vink.createSVGPathSegClosePath());
		vink.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, ""+CssColor.make(150,150,150));
		vink.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "transparent");
		vink.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "" + 2.4);
		
		svgHalf.appendChild(vink);
		
	}
	
	public void initCloseButtonSVG() {
		svgCloseButton = doc.createSVGSVGElement();
		Rectangle cba = new Rectangle(0,0,30,30);
		OMSVGPathElement closeButton = doc.createSVGPathElement();
		OMSVGPathSegList segsCloseButton = closeButton.getPathSegList();
		int m=10;
		segsCloseButton.appendItem(closeButton.createSVGPathSegMovetoAbs(cba.x+m, cba.y));
		segsCloseButton.appendItem(closeButton.createSVGPathSegLinetoAbs(cba.x+m, cba.y+m));
		segsCloseButton.appendItem(closeButton.createSVGPathSegLinetoAbs(cba.x, cba.y+m ));
		segsCloseButton.appendItem(closeButton.createSVGPathSegMovetoAbs(cba.x+cba.width-m, cba.y));
		segsCloseButton.appendItem(closeButton.createSVGPathSegLinetoAbs(cba.x+cba.width-m, cba.y+m));
		segsCloseButton.appendItem(closeButton.createSVGPathSegLinetoAbs(cba.x+cba.width, cba.y+m));
		segsCloseButton.appendItem(closeButton.createSVGPathSegMovetoAbs(cba.x, cba.y+cba.height-m));
		segsCloseButton.appendItem(closeButton.createSVGPathSegLinetoAbs(cba.x+m,  cba.y+cba.height-m));
		segsCloseButton.appendItem(closeButton.createSVGPathSegLinetoAbs(cba.x+m, cba.y+cba.height));
		segsCloseButton.appendItem(closeButton.createSVGPathSegMovetoAbs(cba.x+cba.width, cba.y+cba.height-m));
		segsCloseButton.appendItem(closeButton.createSVGPathSegLinetoAbs(cba.x+cba.width-m,  cba.y+cba.height-m));
		segsCloseButton.appendItem(closeButton.createSVGPathSegLinetoAbs(cba.x+cba.width-m, cba.y+cba.height));
		segsCloseButton.appendItem(closeButton.createSVGPathSegMovetoAbs(cba.x+m, cba.y));
		segsCloseButton.appendItem(closeButton.createSVGPathSegClosePath());
		closeButton.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, colorBlue2.toString());
		closeButton.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "transparent");
		closeButton.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "" + 4.0);
		svgCloseButton.appendChild(closeButton);
	}
	
	public void initCheckButtonSVG() {
		svgCheckButton = doc.createSVGSVGElement();
		Rectangle chba = new Rectangle(0,0,30,30);
		OMSVGPathElement checkButton = doc.createSVGPathElement();
		OMSVGPathSegList segsCheckButton = checkButton.getPathSegList();
		segsCheckButton.appendItem(checkButton.createSVGPathSegMovetoAbs(chba.x+chba.width/4 , chba.y+chba.height/2));
		segsCheckButton.appendItem(checkButton.createSVGPathSegLinetoAbs(chba.x+chba.width/2, chba.y+chba.height));
		segsCheckButton.appendItem(checkButton.createSVGPathSegLinetoAbs(chba.x+chba.width, chba.y));
		segsCheckButton.appendItem(checkButton.createSVGPathSegMovetoAbs(chba.x+chba.width/4 , chba.y+chba.height/2));
		segsCheckButton.appendItem(checkButton.createSVGPathSegClosePath());
		checkButton.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, colorBlue2.toString());
		checkButton.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "transparent");
		checkButton.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "" + 4.0);
		svgCheckButton.appendChild(checkButton);
	}
	
	public void initCalculatorButtonSVG() {
		svgCalculatorButton = doc.createSVGSVGElement();
		Rectangle ra = new Rectangle(-9,0,30,30);
		float e = (float)ra.width/16;
		OMSVGRectElement copyrect2 = doc.createSVGRectElement(ra.x+5 * e, ra.y+4.5f*e, 14 * e, 15 * e, e, e);
		copyrect2.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, colorBlue2.toString());
		copyrect2.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, colorBlue2.toString());
		copyrect2.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "" + 0.7f*e);
		svgCalculatorButton.appendChild(copyrect2);

		OMSVGRectElement copyrect1 = doc.createSVGRectElement(ra.x+7.25f*e, ra.y+7.0f*e, 9.5f*e, e, 0.5f*e, 0.5f*e);
		copyrect1.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, ""+toolbarColor);
		copyrect1.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, ""+toolbarColor);
		copyrect1.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "" + 1.5*e);
		svgCalculatorButton.appendChild(copyrect1);

		OMSVGCircleElement punt1 = doc.createSVGCircleElement(ra.x+8*e, ra.y+12.5f*e, 1.5f*e);
		OMSVGCircleElement punt2 = doc.createSVGCircleElement(ra.x+12*e, ra.y+12.5f*e, 1.5f*e);
		OMSVGCircleElement punt3 = doc.createSVGCircleElement(ra.x+16*e, ra.y+12.5f*e, 1.5f*e);
		OMSVGCircleElement punt4 = doc.createSVGCircleElement(ra.x+8*e, ra.y+16.5f*e, 1.5f*e);
		OMSVGCircleElement punt5 = doc.createSVGCircleElement(ra.x+12*e, ra.y+16.5f*e, 1.5f*e);
		OMSVGCircleElement punt6 = doc.createSVGCircleElement(ra.x+16*e, ra.y+16.5f*e, 1.5f*e);
		
		punt1.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, ""+toolbarColor);
		punt2.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, ""+toolbarColor);
		punt3.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, ""+toolbarColor);
		punt4.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, ""+toolbarColor);
		punt5.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, ""+toolbarColor);
		punt6.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, ""+toolbarColor);
		
		svgCalculatorButton.appendChild(punt1);
		svgCalculatorButton.appendChild(punt2);
		svgCalculatorButton.appendChild(punt3);
		svgCalculatorButton.appendChild(punt4);
		svgCalculatorButton.appendChild(punt5);
		svgCalculatorButton.appendChild(punt6);
	}
	
	private void initDrawButtonSVG() {
		svgDrawButton = doc.createSVGSVGElement();
		Rectangle r = new Rectangle(0,0,25,25);
		int x = r.x+r.width/8;
		int y = r.y+r.height/8;
		int w = 3*r.width/4;
		int h = 3*r.height/4;
		OMSVGRectElement rect = doc.createSVGRectElement(r.x, r.y, r.width, r.height, 0, 0);
		rect.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, colorBlue4.toString());
		svgDrawButton.appendChild(rect);
		
		OMSVGPathElement drawButton = doc.createSVGPathElement();
		OMSVGPathSegList segsDrawButton = drawButton.getPathSegList();
		segsDrawButton.appendItem(drawButton.createSVGPathSegMovetoAbs(x+w/3, y+2*h/3));
		segsDrawButton.appendItem(drawButton.createSVGPathSegLinetoAbs(x+2*w/3, y));
		segsDrawButton.appendItem(drawButton.createSVGPathSegLinetoAbs(x+w, y+2*h/3));
		segsDrawButton.appendItem(drawButton.createSVGPathSegLinetoAbs(x+w/3, y+2*h/3));
		segsDrawButton.appendItem(drawButton.createSVGPathSegLinetoAbs(x+2*w/3, y));
		
		segsDrawButton.appendItem(drawButton.createSVGPathSegMovetoAbs(x+w/3, y+h));
		segsDrawButton.appendItem(drawButton.createSVGPathSegArcAbs(x+w/3+0.001f, y+h, w/3, w/3, 360, true, true));
		segsDrawButton.appendItem(drawButton.createSVGPathSegMovetoAbs(x+w/3, y+2*h/3));
		segsDrawButton.appendItem(drawButton.createSVGPathSegClosePath());
		drawButton.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "white");
		drawButton.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "transparent");
		drawButton.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "" + 2.0);
		svgDrawButton.appendChild(drawButton);
	}
	
	public void enableDrawButtonSVG(boolean b) {
		if(b)
			((OMSVGRectElement)svgDrawButton.getFirstChild()).getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, colorBlue2.toString());
		else
			((OMSVGRectElement)svgDrawButton.getFirstChild()).getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, colorBlue4.toString());
	}
	
	private void initFormButtonSVG() {
		svgFormButton = doc.createSVGSVGElement();
		Rectangle r = new Rectangle(0,0,25,25);
		int x = r.x+r.width/8;
		int y = r.y+r.height/8;
		int w = 3*r.width/4;
		int h = 3*r.height/4;
		OMSVGRectElement rect = doc.createSVGRectElement(r.x, r.y, r.width, r.height, 0, 0);
		rect.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, colorBlue4.toString());
		svgFormButton.appendChild(rect);
		
		OMSVGPathElement formButton = doc.createSVGPathElement();
		OMSVGPathSegList segsFormButton = formButton.getPathSegList();
		segsFormButton.appendItem(formButton.createSVGPathSegMovetoAbs(x+w/6, y+h));
		segsFormButton.appendItem(formButton.createSVGPathSegLinetoAbs(x+w/6, y+h/6));
		segsFormButton.appendItem(formButton.createSVGPathSegArcAbs(x+w/2, y+h/6, w/6, w/6, 180, true ,true));
		segsFormButton.appendItem(formButton.createSVGPathSegMovetoAbs(x, y+h/2));
		segsFormButton.appendItem(formButton.createSVGPathSegLinetoAbs(x+w/3, y+h/2));
		
		segsFormButton.appendItem(formButton.createSVGPathSegMovetoAbs(x+w/2, y+h/3));
		segsFormButton.appendItem(formButton.createSVGPathSegLinetoAbs(x+w, y+h));
		segsFormButton.appendItem(formButton.createSVGPathSegMovetoAbs(x+w/2, y+h));
		segsFormButton.appendItem(formButton.createSVGPathSegLinetoAbs(x+w, y+h/3));
		segsFormButton.appendItem(formButton.createSVGPathSegClosePath());
		formButton.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "white");
		formButton.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "transparent");
		formButton.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "" + 2.0);
		svgFormButton.appendChild(formButton);
	}
	
	public void enableFormButtonSVG(boolean b) {
		if(b)
			((OMSVGRectElement)svgFormButton.getFirstChild()).getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, colorBlue2.toString());
		else
			((OMSVGRectElement)svgFormButton.getFirstChild()).getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, colorBlue4.toString());
	}
	
	public void initPenButtonSVG() {
		svgPenButton = doc.createSVGSVGElement();
		Rectangle r = new Rectangle(0,0,25,25);
		float x = (float)r.x+(float)r.width/8;
		float y = (float)r.y+(float)r.height/8;
		float w = 3*(float)r.width/4;
		float h = 3*(float)r.height/4;
		OMSVGRectElement rect = doc.createSVGRectElement(r.x, r.y, r.width, r.height, 0, 0);
		rect.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, colorBlue2.toString());
		svgPenButton.appendChild(rect);
		
		OMSVGPathElement penButton = doc.createSVGPathElement();
		OMSVGPathSegList segsPenButton = penButton.getPathSegList();
		segsPenButton.appendItem(penButton.createSVGPathSegMovetoAbs(x, y+h));
		segsPenButton.appendItem(penButton.createSVGPathSegLinetoAbs(x, y+3*h/4));
		segsPenButton.appendItem(penButton.createSVGPathSegLinetoAbs(x+3*w/4, y));
		segsPenButton.appendItem(penButton.createSVGPathSegLinetoAbs(x+w, y+h/4));
		segsPenButton.appendItem(penButton.createSVGPathSegLinetoAbs(x+w/4, y+h));
		segsPenButton.appendItem(penButton.createSVGPathSegLinetoAbs(x, y+h));
		segsPenButton.appendItem(penButton.createSVGPathSegLinetoAbs(x, y+3*h/4));
		
		segsPenButton.appendItem(penButton.createSVGPathSegMovetoAbs(x, y+3*h/4));
		segsPenButton.appendItem(penButton.createSVGPathSegLinetoAbs(x+w/4, y+h));
		
//		segsPenButton.appendItem(penButton.createSVGPathSegLinetoAbs(x, y+3*h/4));
//		segsPenButton.appendItem(penButton.createSVGPathSegLinetoAbs(x+w/4, y+h));
//		
		
		segsPenButton.appendItem(penButton.createSVGPathSegMovetoAbs(x, y+h));
		segsPenButton.appendItem(penButton.createSVGPathSegClosePath());
		penButton.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "white");
		penButton.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "transparent");
		penButton.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "" + 2.0);
		svgPenButton.appendChild(penButton);
	}
	
	public void enablePenButtonSVG(boolean b) {
		if(b)
			((OMSVGRectElement)svgPenButton.getFirstChild()).getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, colorBlue2.toString());
		else
			((OMSVGRectElement)svgPenButton.getFirstChild()).getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, colorBlue4.toString());
	}
	
	public void initEraserButtonSVG() {
		svgEraserButton = doc.createSVGSVGElement();
		Rectangle r = new Rectangle(0,0,25,25);
		
		int x = r.x+r.width/8;
		int y = r.y+r.height/8;
		int w = 3*r.width/4;
		int h = 3*r.height/4;
		OMSVGRectElement rect = doc.createSVGRectElement(r.x, r.y, r.width, r.height, 0, 0);
		rect.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, colorBlue4.toString());
		svgEraserButton.appendChild(rect);
		
		OMSVGPathElement eraserButton = doc.createSVGPathElement();
		OMSVGPathSegList segsEraserButton = eraserButton.getPathSegList();
		segsEraserButton.appendItem(eraserButton.createSVGPathSegMovetoAbs(x+w/2, y));
		segsEraserButton.appendItem(eraserButton.createSVGPathSegLinetoAbs(x+w, y));
		segsEraserButton.appendItem(eraserButton.createSVGPathSegLinetoAbs(x+w/2, y+h));
		segsEraserButton.appendItem(eraserButton.createSVGPathSegLinetoAbs(x, y+h));
		segsEraserButton.appendItem(eraserButton.createSVGPathSegLinetoAbs(x+w/2, y));
		segsEraserButton.appendItem(eraserButton.createSVGPathSegLinetoAbs(x+w, y));
		segsEraserButton.appendItem(eraserButton.createSVGPathSegMovetoAbs(x+w/2, y));
		segsEraserButton.appendItem(eraserButton.createSVGPathSegClosePath());
		eraserButton.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "white");
		eraserButton.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "transparent");
		eraserButton.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "" + 2.0);
		svgEraserButton.appendChild(eraserButton);
		
		OMSVGPathElement eraserButtonPart = doc.createSVGPathElement();
		OMSVGPathSegList segsEraserButtonPart = eraserButtonPart.getPathSegList();
		segsEraserButtonPart.appendItem(eraserButtonPart.createSVGPathSegMovetoAbs(x+w/4, y+h/2));
		segsEraserButtonPart.appendItem(eraserButtonPart.createSVGPathSegLinetoAbs(x+3*w/4, y+h/2));
		segsEraserButtonPart.appendItem(eraserButtonPart.createSVGPathSegLinetoAbs(x+w/2, y+h));
		segsEraserButtonPart.appendItem(eraserButtonPart.createSVGPathSegLinetoAbs(x, y+h));
		segsEraserButtonPart.appendItem(eraserButtonPart.createSVGPathSegLinetoAbs(x+w/4, y+h/2));
		segsEraserButtonPart.appendItem(eraserButtonPart.createSVGPathSegLinetoAbs(x+3*w/4, y+h/2));
		segsEraserButtonPart.appendItem(eraserButtonPart.createSVGPathSegMovetoAbs(x+w/4, y+h/2));
		segsEraserButtonPart.appendItem(eraserButtonPart.createSVGPathSegClosePath());
		eraserButtonPart.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "white");
		svgEraserButton.appendChild(eraserButtonPart);
	}
	
	public void enableEraserButtonSVG(boolean b) {
		if(b)
			((OMSVGRectElement)svgEraserButton.getFirstChild()).getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, colorBlue2.toString());
		else
			((OMSVGRectElement)svgEraserButton.getFirstChild()).getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, colorBlue4.toString());
	}
	
	public void initUndoButtonSVG() {
		svgUndoButton = doc.createSVGSVGElement();
		Rectangle r = new Rectangle(0,0,25,25);
		float x = (float)r.x+(float)r.width/8;
		float y = (float)r.y+(float)r.height/8-5;
		float w = 3*(float)r.width/4;
		float h = 3*(float)r.height/4;
		
		OMSVGRectElement rect = doc.createSVGRectElement(r.x, r.y, r.width, r.height, 0, 0);
		rect.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, colorBlue4.toString());
		svgUndoButton.appendChild(rect);
		
		OMSVGPathElement undoButton = doc.createSVGPathElement();
		OMSVGPathSegList segsUndoButton = undoButton.getPathSegList();
		segsUndoButton.appendItem(undoButton.createSVGPathSegMovetoAbs(x, y + h));
		segsUndoButton.appendItem(undoButton.createSVGPathSegArcAbs(x + w , y + h, w / 2, w / 2, 180, true, true));
		segsUndoButton.appendItem(undoButton.createSVGPathSegMovetoAbs(x, y-1 + h));
		segsUndoButton.appendItem(undoButton.createSVGPathSegLinetoAbs(x , y-1 + h * 4 / 5));
		segsUndoButton.appendItem(undoButton.createSVGPathSegLinetoAbs(x + w / 5, y-1 + h ));
		segsUndoButton.appendItem(undoButton.createSVGPathSegLinetoAbs(x, y-1 + h));
		segsUndoButton.appendItem(undoButton.createSVGPathSegLinetoAbs(x , y-1 + h * 4 / 5));
		segsUndoButton.appendItem(undoButton.createSVGPathSegMovetoAbs(x + w / 6, y + h));
		segsUndoButton.appendItem(undoButton.createSVGPathSegClosePath());
		undoButton.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "white");
		undoButton.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "transparent");
		undoButton.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "" + 2.0);
		svgUndoButton.appendChild(undoButton);
	}
	
	public void enableUndoButtonSVG(boolean b) {
		if(b)
			((OMSVGRectElement)svgUndoButton.getFirstChild()).getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, colorBlue2.toString());
		else
			((OMSVGRectElement)svgUndoButton.getFirstChild()).getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, colorBlue4.toString());
	}
	
	public void initBinButtonSVG() {
		svgBinButton = doc.createSVGSVGElement();
		Rectangle r = new Rectangle(0,0,25,25);
		float x = (float)r.x+(float)r.width/8;
		float y = (float)r.y+(float)r.height/8;
		float w = 3*(float)r.width/4;
		float h = 3*(float)r.height/4;
		
		OMSVGRectElement rect = doc.createSVGRectElement(r.x, r.y, r.width, r.height, 0, 0);
		rect.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, colorBlue4.toString());
		svgBinButton.appendChild(rect);
		
		OMSVGPathElement binButton = doc.createSVGPathElement();
		OMSVGPathSegList segsBinButton = binButton.getPathSegList();
		segsBinButton.appendItem(binButton.createSVGPathSegMovetoAbs(x, y + h / 4));
		segsBinButton.appendItem(binButton.createSVGPathSegLinetoAbs(x + w, y + h / 4));
		segsBinButton.appendItem(binButton.createSVGPathSegMovetoAbs(x + w / 6, y + h / 4));
		segsBinButton.appendItem(binButton.createSVGPathSegLinetoAbs(x + w / 4, y + h));
		segsBinButton.appendItem(binButton.createSVGPathSegLinetoAbs(x + w * 3 / 4, y + h));
		segsBinButton.appendItem(binButton.createSVGPathSegLinetoAbs(x + w * 5 / 6, y + h / 4));
		segsBinButton.appendItem(binButton.createSVGPathSegMovetoAbs(x + w / 2, y + h / 4));
		segsBinButton.appendItem(binButton.createSVGPathSegLinetoAbs(x + w / 2, y + h));
		segsBinButton.appendItem(binButton.createSVGPathSegMovetoAbs(x + w * 3 / 8, y + h / 4));
		segsBinButton.appendItem(binButton.createSVGPathSegLinetoAbs(x + w * 3 / 8, y));
		segsBinButton.appendItem(binButton.createSVGPathSegLinetoAbs(x + w * 5 / 8, y));
		segsBinButton.appendItem(binButton.createSVGPathSegLinetoAbs(x + w * 5 / 8, y + h / 4));
		segsBinButton.appendItem(binButton.createSVGPathSegMovetoAbs(x, y + h / 4));
		segsBinButton.appendItem(binButton.createSVGPathSegClosePath());
		binButton.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, "white");
		binButton.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "transparent");
		binButton.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "" + 2.0);
		svgBinButton.appendChild(binButton);
	}
	
	public void enableBinButtonSVG(boolean b) {
		if(b)
			((OMSVGRectElement)svgBinButton.getFirstChild()).getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, colorBlue2.toString());
		else
			((OMSVGRectElement)svgBinButton.getFirstChild()).getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, colorBlue4.toString());
	}
	
	public void initHandleButtonSVG() {
		svgHandleButton = doc.createSVGSVGElement();
		Rectangle r = new Rectangle(0,0,30,30);
		Point pU = new Point(r.x+r.width/2,r.y+5);
		Point pD = new Point(r.x+r.width/2,r.y+r.height-5);
		Point pL = new Point(r.x+5,r.y+r.height/2);
		Point pR = new Point(r.x+r.width-5,r.y+r.height/2);
		int d = 3;
		OMSVGRectElement rect = doc.createSVGRectElement(r.x, r.y, r.width, r.height, 0, 0);
		rect.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "white");
		svgHandleButton.appendChild(rect);
		
		OMSVGPathElement handle = doc.createSVGPathElement();
		OMSVGPathSegList segsHandle = handle.getPathSegList();
		
		segsHandle.appendItem(handle.createSVGPathSegMovetoAbs(pU.x-d, pU.y+d));
		segsHandle.appendItem(handle.createSVGPathSegLinetoAbs(pU.x, pU.y));
		segsHandle.appendItem(handle.createSVGPathSegLinetoAbs(pU.x+d, pU.y+d));
		
		segsHandle.appendItem(handle.createSVGPathSegMovetoAbs(pD.x-d,pD.y-d));
		segsHandle.appendItem(handle.createSVGPathSegLinetoAbs(pD.x,pD.y));
		segsHandle.appendItem(handle.createSVGPathSegLinetoAbs(pD.x+d,pD.y-d));
		
		segsHandle.appendItem(handle.createSVGPathSegMovetoAbs(pL.x+d, pL.y+d));
		segsHandle.appendItem(handle.createSVGPathSegLinetoAbs(pL.x, pL.y));
		segsHandle.appendItem(handle.createSVGPathSegLinetoAbs(pL.x+d, pL.y-d));
		
		segsHandle.appendItem(handle.createSVGPathSegMovetoAbs(pR.x-d,pR.y-d));
		segsHandle.appendItem(handle.createSVGPathSegLinetoAbs(pR.x,pR.y));
		segsHandle.appendItem(handle.createSVGPathSegLinetoAbs(pR.x-d,pR.y+d));
		
		segsHandle.appendItem(handle.createSVGPathSegMovetoAbs(pU.x, pU.y));
		segsHandle.appendItem(handle.createSVGPathSegLinetoAbs(pD.x,pD.y));
		segsHandle.appendItem(handle.createSVGPathSegMovetoAbs(pL.x, pL.y));
		segsHandle.appendItem(handle.createSVGPathSegLinetoAbs(pR.x,pR.y));
		
		segsHandle.appendItem(handle.createSVGPathSegClosePath());
		handle.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, colorBlue3.toString());
		handle.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "transparent");
		handle.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "" + 1.5);
		svgHandleButton.appendChild(handle);
	}
	
	
	
	public void appendGrid() {
		int lineDistance = 10;
		CssColor gridColor = CssColor.make(180,195,228);
		int vSteps = height / lineDistance;
		for (int vCnt = 1; vCnt <= vSteps; vCnt++) {
			OMSVGLineElement stroke = doc.createSVGLineElement(0, vCnt * lineDistance, width - 1, vCnt * lineDistance);
			stroke.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, gridColor.toString());
			stroke.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "" + 0.3);
			svg.appendChild(stroke);
		}
		int hSteps = width / lineDistance;
		for (int hCnt = 1; hCnt <= hSteps; hCnt++) {
			OMSVGLineElement stroke = doc.createSVGLineElement(hCnt * lineDistance, 0, hCnt * lineDistance, width - 1);
			stroke.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, gridColor.toString());
			stroke.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "" + 0.4);
			svg.appendChild(stroke);
		}
	}
	
	public void appendBin() {
		CssColor strokeColor = colorBlue2;
//		if (kStrokeContainers.size() < 1)
//			strokeColor = colorBlue4;
		Rectangle r = mathScratchField.getBinArea();
		bin = doc.createSVGPathElement();
		OMSVGPathSegList segsBin = bin.getPathSegList();
		segsBin.appendItem(bin.createSVGPathSegMovetoAbs(r.x, r.y + r.height / 4));
		segsBin.appendItem(bin.createSVGPathSegLinetoAbs(r.x + r.width, r.y + r.height / 4));
		segsBin.appendItem(bin.createSVGPathSegMovetoAbs(r.x + r.width / 6, r.y + r.height / 4));
		segsBin.appendItem(bin.createSVGPathSegLinetoAbs(r.x + r.width / 4, r.y + r.height));
		segsBin.appendItem(bin.createSVGPathSegLinetoAbs(r.x + r.width * 3 / 4, r.y + r.height));
		segsBin.appendItem(bin.createSVGPathSegLinetoAbs(r.x + r.width * 5 / 6, r.y + r.height / 4));
		segsBin.appendItem(bin.createSVGPathSegMovetoAbs(r.x + r.width / 2, r.y + r.height / 4));
		segsBin.appendItem(bin.createSVGPathSegLinetoAbs(r.x + r.width / 2, r.y + r.height));
		segsBin.appendItem(bin.createSVGPathSegMovetoAbs(r.x + r.width * 3 / 8, r.y + r.height / 4));
		segsBin.appendItem(bin.createSVGPathSegLinetoAbs(r.x + r.width * 3 / 8, r.y));
		segsBin.appendItem(bin.createSVGPathSegLinetoAbs(r.x + r.width * 5 / 8, r.y));
		segsBin.appendItem(bin.createSVGPathSegLinetoAbs(r.x + r.width * 5 / 8, r.y + r.height / 4));
		segsBin.appendItem(bin.createSVGPathSegMovetoAbs(r.x, r.y + r.height / 6));
		segsBin.appendItem(bin.createSVGPathSegClosePath());
		bin.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, strokeColor.toString());
		bin.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "transparent");
		bin.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "" + 4.0);
		svg.appendChild(bin);
	}
	
	public void enableBin(boolean b) {
		if(b)
			bin.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, colorBlue2.toString());
		else
			bin.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, colorBlue4.toString());
	}
	
	public void updateBin() {
		boolean b = mathScratchField.numHistories>0;
		if(b)
			bin.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, colorBlue2.toString());
		else
			bin.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, colorBlue4.toString());
	}
	
	public void appendUndo() {
		CssColor strokeColor = colorBlue2;
//		if (mathScratchField.kStrokeContainers.size() < 1)
//			strokeColor = colorBlue4;
		Rectangle r = mathScratchField.getUndoArea();
		undo = doc.createSVGPathElement();
		OMSVGPathSegList segsUndo = undo.getPathSegList();
		segsUndo.appendItem(undo.createSVGPathSegMovetoAbs(r.x + r.width / 6, r.y + r.height));
		segsUndo.appendItem(undo.createSVGPathSegArcAbs((float)r.x + r.width + r.width / 6, (float)r.y + r.height, (float)r.width / 2, (float)r.width / 2, (float)180, true, true));
		segsUndo.appendItem(undo.createSVGPathSegMovetoAbs(r.x+2, r.y + r.height));
		segsUndo.appendItem(undo.createSVGPathSegLinetoAbs(r.x+2 , r.y + r.height * 4 / 5));
		segsUndo.appendItem(undo.createSVGPathSegLinetoAbs(r.x+2 + r.width / 5, r.y + r.height ));
		segsUndo.appendItem(undo.createSVGPathSegLinetoAbs(r.x, r.y + r.height));
		segsUndo.appendItem(undo.createSVGPathSegMovetoAbs(r.x+2, r.y + r.height));
		segsUndo.appendItem(undo.createSVGPathSegClosePath());
		undo.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, strokeColor.toString());
		undo.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "transparent");
		undo.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "" + 4.0);
		svg.appendChild(undo);
	}
	
	public void enableUndo(boolean b) {
		if(b)
			undo.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, colorBlue2.toString());
		else
			undo.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, colorBlue4.toString());
	}
	
	public void updateUndo() {
		boolean b = mathScratchField.numHistories>1;
		if(b)
			undo.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, colorBlue2.toString());
		else
			undo.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, colorBlue4.toString());
	}
	
	public void addStrokeContainer(KStrokeContainer sc) {
		OMSVGSVGElement svgSC = doc.createSVGSVGElement();
		ArrayList<OMNode> nodeList = new ArrayList<OMNode>();
		
		if(sc.formuleModus && !sc.recognizeOff) {
			OMSVGSVGElement svgForm = doc.createSVGSVGElement();
			if(!sc.recognizeOff && sc.formuleViewer!=null) {
				float x = sc.getBox().x;
				float y = sc.getBox().y;
				sc.formuleViewer.setFont(FormuleFont.createFromFontSize(16));
				sc.formuleViewer.setColor(CssColor.make(80, 80, 80));
				sc.formuleViewer.getMainRegel().draw(svgForm);
				svgForm.getX().getBaseVal().setValue(x);
				svgForm.getY().getBaseVal().setValue(y);
				//currentSCNodes.add(svgForm);
				//svgCurrentSC.appendChild(svgForm);
				nodeList.add(svgForm);
				svgSC.appendChild(svgForm);
			}
		}
		else {
			ArrayList<Stroke> strokes = sc.strokeContainer.getStrokes();
			for(int j = 0 ; j < strokes.size() ; j++) {
				Stroke stroke = strokes.get(j);
				OMSVGPathElement strokePath = doc.createSVGPathElement();
				OMSVGPathSegList segsStrokePath = strokePath.getPathSegList();
				float x0 = (float)stroke.getParsePoints().get(0).x;
				float y0 = (float)stroke.getParsePoints().get(0).y;
				//if(r.contains((int)x0,(int)0))
					segsStrokePath.appendItem(strokePath.createSVGPathSegMovetoAbs(x0,y0));
				if(stroke.getParsePointsbox().width>3 ||  stroke.getParsePointsbox().height>3) {
					for(int k = 1 ; k < stroke.getParsePoints().size() ; k++) {
						float x = (float)stroke.getParsePoints().get(k).x ;
						float y = (float)stroke.getParsePoints().get(k).y;
						//if(r.contains((int)x,(int)y))
							segsStrokePath.appendItem(strokePath.createSVGPathSegLinetoAbs(x,y));
					}
				}
				else {
					segsStrokePath.appendItem(strokePath.createSVGPathSegArcAbs(x0+0.01f, y0, 1, 1, 360, true, true));
				}
				segsStrokePath.appendItem(strokePath.createSVGPathSegMovetoAbs(x0,y0));
				segsStrokePath.appendItem(strokePath.createSVGPathSegClosePath());
				strokePath.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, ""+strokeColor);
				strokePath.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "transparent");
				strokePath.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "" + 1.5);
				
				nodeList.add(strokePath);
				svgSC.appendChild(strokePath);
			}
		}
		float corrx = sc.getBox().x-34;
		float corrY = sc. getBox().y + sc.getBox().height/2-7;
		if(sc.isCorrect()) {
			OMSVGSVGElement svgGoed = doc.createSVGSVGElement();
			drawGoedSVG(svgGoed);
			svgGoed.getX().getBaseVal().setValue(corrx);
			svgGoed.getY().getBaseVal().setValue(corrY);
			nodeList.add(svgGoed);
			svgSC.appendChild(svgGoed);
		}
		else if(sc.isHalf()) {
			OMSVGSVGElement svgHalf = doc.createSVGSVGElement();
			drawHalfSVG(svgHalf);
			svgHalf.getX().getBaseVal().setValue(corrx);
			svgHalf.getY().getBaseVal().setValue(corrY);
			nodeList.add(svgHalf);
			svgSC.appendChild(svgHalf);
		}
		else if(sc.isFalse()) {
			OMSVGSVGElement svgFout = doc.createSVGSVGElement();
			drawFoutSVG(svgFout);
			svgFout.getX().getBaseVal().setValue(corrx);
			svgFout.getY().getBaseVal().setValue(corrY);
			nodeList.add(svgFout);
			svgSC.appendChild(svgFout);
		}
		
		if(scMap.containsKey(sc)) {
			try {
				svg.replaceChild(svgSC, scMap.get(sc));
				svgStrokeContainers.remove(scMap.get(sc));
			} catch(Exception e) {}
		}
		else {
			svg.appendChild(svgSC);
		}
		scMap.put(sc, svgSC);
		svgStrokeContainers.add(svgSC);
		scNodes.add(nodeList);
	}
	
	public void removeStrokeContainer(KStrokeContainer sc) {
		try {
			svg.removeChild(scMap.get(sc));
			svgStrokeContainers.remove(scMap.get(sc));
		} catch(Exception e) {}
		
		scMap.remove(sc);
	}
	
	public void appendStrokeContainers () {
		removeStrokeContainers();
		scMap.clear();
		ArrayList<KStrokeContainer> strokeContainers = mathScratchField.getStrokeContainers();
		if(strokeContainers.size()>0) {
			enableBin(true);
			//enableUndo(true);
		}
		for(int i=0 ; i<strokeContainers.size() ; i++) {
			OMSVGSVGElement svgSC = doc.createSVGSVGElement();
			scMap.put(strokeContainers.get(i), svgSC);
			svgStrokeContainers.add(svgSC);
			scNodes.add(new ArrayList<OMNode>());
			
			KStrokeContainer sc = strokeContainers.get(i);
			if(sc.formuleModus && !sc.recognizeOff) {
				OMSVGSVGElement svgForm = doc.createSVGSVGElement();
				if(!sc.recognizeOff && sc.formuleViewer!=null) {
					float x = sc.getBox().x;
					float y = sc.getBox().y;
					sc.formuleViewer.setFont(FormuleFont.createFromFontSize(16));
					sc.formuleViewer.setColor(CssColor.make(80, 80, 80));
					sc.formuleViewer.getMainRegel().draw(svgForm);
					svgForm.getX().getBaseVal().setValue(x);
					svgForm.getY().getBaseVal().setValue(y);
					scNodes.get(i).add(svgForm);
					svgSC.appendChild(svgForm);
				}
			}
			else {
				ArrayList<Stroke> strokes = strokeContainers.get(i).strokeContainer.getStrokes();
				for(int j = 0 ; j < strokes.size() ; j++) {
					Stroke stroke = strokes.get(j);
					OMSVGPathElement strokePath = doc.createSVGPathElement();
					OMSVGPathSegList segsStrokePath = strokePath.getPathSegList();
					float x0 = (float)stroke.getParsePoints().get(0).x;
					float y0 = (float)stroke.getParsePoints().get(0).y;
					//if(r.contains((int)x0,(int)0))
						segsStrokePath.appendItem(strokePath.createSVGPathSegMovetoAbs(x0,y0));
					if(stroke.getParsePointsbox().width>3 ||  stroke.getParsePointsbox().height>3) {
						for(int k = 1 ; k < stroke.getParsePoints().size() ; k++) {
							float x = (float)stroke.getParsePoints().get(k).x ;
							float y = (float)stroke.getParsePoints().get(k).y;
							//if(r.contains((int)x,(int)y))
								segsStrokePath.appendItem(strokePath.createSVGPathSegLinetoAbs(x,y));
						}
					}
					else {
						segsStrokePath.appendItem(strokePath.createSVGPathSegArcAbs(x0+0.01f, y0, 1, 1, 360, true, true));
					}
					segsStrokePath.appendItem(strokePath.createSVGPathSegMovetoAbs(x0,y0));
					segsStrokePath.appendItem(strokePath.createSVGPathSegClosePath());
					strokePath.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, ""+strokeColor);
					strokePath.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "transparent");
					strokePath.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "" + 1.5);
					scNodes.get(i).add(strokePath);
					svgSC.appendChild(strokePath);
				}
			}
			
			float corrx = strokeContainers.get(i).getBox().x-34;
			float corrY = strokeContainers.get(i). getBox().y + strokeContainers.get(i).getBox().height/2-7;
			if(strokeContainers.get(i).isCorrect()) {
				OMSVGSVGElement svgGoed = doc.createSVGSVGElement();
				drawGoedSVG(svgGoed);
				svgGoed.getX().getBaseVal().setValue(corrx);
				svgGoed.getY().getBaseVal().setValue(corrY);
				scNodes.get(i).add(svgGoed);
				svgSC.appendChild(svgGoed);
			}
			else if(strokeContainers.get(i).isHalf()) {
				OMSVGSVGElement svgHalf = doc.createSVGSVGElement();
				drawHalfSVG(svgHalf);
				svgHalf.getX().getBaseVal().setValue(corrx);
				svgHalf.getY().getBaseVal().setValue(corrY);
				scNodes.get(i).add(svgHalf);
				svgSC.appendChild(svgHalf);
			}
			else if(strokeContainers.get(i).isFalse()) {
				OMSVGSVGElement svgFout = doc.createSVGSVGElement();
				drawFoutSVG(svgFout);
				svgFout.getX().getBaseVal().setValue(corrx);
				svgFout.getY().getBaseVal().setValue(corrY);
				scNodes.get(i).add(svgFout);
				svgSC.appendChild(svgFout);
			}
			svg.appendChild(svgSC);
		}
	}
	
	public void removeStrokeContainers() {
		for(int i=0 ; i<scNodes.size() ; i++) {
			ArrayList<OMNode> nodeList = scNodes.get(i);
			for(int j=0 ; j<nodeList.size() ; j++) {
				try {
					svgStrokeContainers.get(i).removeChild(nodeList.get(j));
				} catch(Exception e) {}
			}
			nodeList.clear();
			try {
				svg.removeChild(svgStrokeContainers.get(i));
			} catch(Exception e) {}
		}
		svgStrokeContainers.clear();
		scNodes.clear();
	}
	
	public void removeStrokeCurrentSC(Stroke stroke) {
		svgCurrentSCStrokes.removeChild(currentSCStrokeMap.get(stroke));
		currentSCStrokeMap.remove(stroke);
	}

	public void addStrokeCurrentSC(Stroke stroke) {
		OMSVGPathElement strokePath = doc.createSVGPathElement();
		OMSVGPathSegList segsStrokePath = strokePath.getPathSegList();
		float x0 = (float)stroke.getParsePoints().get(0).x;
		float y0 = (float)stroke.getParsePoints().get(0).y;
		//if(r.contains((int)x0,(int)0))
			segsStrokePath.appendItem(strokePath.createSVGPathSegMovetoAbs(x0,y0));
		if(stroke.getParsePointsbox().width>3 ||  stroke.getParsePointsbox().height>3) {
			for(int j = 1 ; j < stroke.getParsePoints().size() ; j++) {
				float x = (float)stroke.getParsePoints().get(j).x ;
				float y = (float)stroke.getParsePoints().get(j).y;
				//if(r.contains((int)x,(int)y))
					segsStrokePath.appendItem(strokePath.createSVGPathSegLinetoAbs(x,y));
			}
		}
		else {
			segsStrokePath.appendItem(strokePath.createSVGPathSegArcAbs(x0+0.01f, y0, 1, 1, 360, true, true));
		}
		segsStrokePath.appendItem(strokePath.createSVGPathSegMovetoAbs(x0,y0));
		segsStrokePath.appendItem(strokePath.createSVGPathSegClosePath());
		strokePath.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, ""+strokeColor);
		strokePath.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "transparent");
		strokePath.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "" + 3.0);
		currentSCStrokeNodes.add(strokePath);
		svgCurrentSCStrokes.appendChild(strokePath);
		currentSCStrokeMap.put(stroke, strokePath);
		
		
		
	}
	
	public void appendCurrentSC(KStrokeContainer csc) {
		appendCurrentSC(csc, null);
	}
	
	public void appendCurrentSC(KStrokeContainer csc, Stroke newStroke) {
		if(csc==null || !csc.isActive() || csc.getWriteBox()==null)
			return;
		removeCurrentSC();
		boolean drawMode = csc.recognizeOff;
		//popup
		float popupX = csc.getWriteBox().x-5;
		float popupY = csc.getWriteBox().y-5;
		float popupWidth = csc.getWriteBox().width+10;
		float popupHeight = csc.getWriteBox().height+10;
		Rectangle r = new Rectangle((int)popupX, (int)popupY, (int)popupWidth, (int)popupHeight);
		OMSVGRectElement popup = doc.createSVGRectElement(popupX, popupY, popupWidth, popupHeight, 0, 0);
		popup.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, SVGConstants.CSS_WHITE_VALUE);
		currentSCNodes.add(popup);
		svgCurrentSC.appendChild(popup);
		
		//shadow
		for(int i=0 ;i<10 ; i++) {
			CssColor c = CssColor.make("rgba("+(200+5*i)+","+(200+5*i)+","+(200+5*i)+","+(1-0.1*i)+")");
			OMSVGRectElement shadow = doc.createSVGRectElement(popupX-1-i, popupY-1-i, popupWidth+2+2*i, popupHeight+2+2*i, 0, 0);
			shadow.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "transparent");
			shadow.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY,""+c);
			shadow.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "2.0");
			currentSCNodes.add(shadow);
			svgCurrentSC.appendChild(shadow);
		}
		
		//grid
		CssColor gridColor = CssColor.make(180,195,228);
		Point c = mathScratchField.getActiveTranslation();
		int cx = (c.x-csc.getWriteBox().x)%20;
		int cy = (c.y-csc.getWriteBox().y)%20;
		int	lineDistance = (int)(10*mathScratchField.schrijfLeesFactor);
		int vSteps = (int)popupHeight / lineDistance;
		for (int vCnt = 1; vCnt <= vSteps+1; vCnt++) {
			OMSVGLineElement stroke = doc.createSVGLineElement(popupX, popupY + cy + vCnt * lineDistance, popupX + popupWidth, popupY + cy + vCnt * lineDistance);
			stroke.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, gridColor.toString());
			stroke.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "" + 0.4);
			currentSCGridNodes.add(stroke);
			svgCurrentSCGrid.appendChild(stroke);
		}
		int hSteps = (int)popupWidth / lineDistance;
		for (int hCnt = 1; hCnt <= hSteps; hCnt++) {
			OMSVGLineElement stroke = doc.createSVGLineElement(popupX + hCnt * lineDistance, popupY, popupX + hCnt * lineDistance , popupY + popupHeight);
			stroke.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, gridColor.toString());
			stroke.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "" + 0.4);
			currentSCGridNodes.add(stroke);
			svgCurrentSCGrid.appendChild(stroke);
		}
		currentSCNodes.add(svgCurrentSCGrid);
		svgCurrentSC.appendChild(svgCurrentSCGrid);
		//gele toolBar
		OMSVGRectElement toolbarForm = doc.createSVGRectElement(popupX+popupWidth-47, popupY, 47, popupHeight, 0, 0);
		toolbarForm.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, ""+toolbarColor);
		OMSVGRectElement toolbarDraw = doc.createSVGRectElement(popupX, popupY, popupWidth, 47, 0, 0);
		toolbarDraw.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, ""+toolbarColor);
		
		if(drawMode) {
			currentSCNodes.add(toolbarDraw);
			svgCurrentSC.appendChild(toolbarDraw);
		}
		else {
			currentSCNodes.add(toolbarForm);
			svgCurrentSC.appendChild(toolbarForm);
		}
				
		//closebutton
		Rectangle cba = csc.getCloseButtonArea();
		svgCloseButton.getX().getBaseVal().setValue(cba.x);
		svgCloseButton.getY().getBaseVal().setValue(cba.y);
		currentSCNodes.add(svgCloseButton);
		svgCurrentSC.appendChild(svgCloseButton);
		
		//checkbutton
		
		Rectangle chba = csc.getCheckButtonArea();
		svgCheckButton.getX().getBaseVal().setValue(chba.x);
		svgCheckButton.getY().getBaseVal().setValue(chba.y);
		if(mathScratchField.hasCheckConnection() && !csc.recognizeOff) {
			currentSCNodes.add(svgCheckButton);
			svgCurrentSC.appendChild(svgCheckButton);
		}
		
		//rekenmachine
		Rectangle aba = csc.getApproxButtonArea();
		svgCalculatorButton.getX().getBaseVal().setValue(aba.x);
		svgCalculatorButton.getY().getBaseVal().setValue(aba.y);
		if(!csc.recognizeOff && csc.isGetalsExpressie && mathScratchField.calculator) {
			currentSCNodes.add(svgCalculatorButton);
			svgCurrentSC.appendChild(svgCalculatorButton);
		}
		
		//goed/half/fout
		float corrx = csc.getWriteBox().x+6;
		float corrY = csc.getWriteBox().y+6;
		if(csc.isCorrect()) {
			OMSVGSVGElement svgGoed = doc.createSVGSVGElement();
			drawGoedSVG(svgGoed);
			svgGoed.getX().getBaseVal().setValue(corrx);
			svgGoed.getY().getBaseVal().setValue(corrY);
			currentSCNodes.add(svgGoed);
			svgCurrentSC.appendChild(svgGoed);
		}
		if(csc.isHalf()) {
			OMSVGSVGElement svgHalf = doc.createSVGSVGElement();
			drawHalfSVG(svgHalf);
			svgHalf.getX().getBaseVal().setValue(corrx);
			svgHalf.getY().getBaseVal().setValue(corrY);
			currentSCNodes.add(svgHalf);
			svgCurrentSC.appendChild(svgHalf);
		}
		if(csc.isFalse()) {
			OMSVGSVGElement svgFout = doc.createSVGSVGElement();
			drawFoutSVG(svgFout);
			svgFout.getX().getBaseVal().setValue(corrx);
			svgFout.getY().getBaseVal().setValue(corrY);
			currentSCNodes.add(svgFout);
			svgCurrentSC.appendChild(svgFout);
		}
		
		//drawButton
		if(!csc.isInputSC && !csc.isCorrect() && !csc.isHalf() && !csc.isFalse()) {
		Rectangle dba = csc.getNotRecognizeButtonArea();
		svgDrawButton.getX().getBaseVal().setValue(dba.x);
		svgDrawButton.getY().getBaseVal().setValue(dba.y);
		currentSCNodes.add(svgDrawButton);
		svgCurrentSC.appendChild(svgDrawButton);
		}
		
		//formButton
		if(drawMode) {
			Rectangle fba = csc.getRecognizeButtonArea();
			svgFormButton.getX().getBaseVal().setValue(fba.x);
			svgFormButton.getY().getBaseVal().setValue(fba.y);
			currentSCNodes.add(svgFormButton);
			svgCurrentSC.appendChild(svgFormButton);
			enableDrawButtonSVG(true);
			enableFormButtonSVG(false);
			
			Rectangle pba = csc.getPenButtonArea();
			svgPenButton.getX().getBaseVal().setValue(pba.x);
			svgPenButton.getY().getBaseVal().setValue(pba.y);
			currentSCNodes.add(svgPenButton);
			svgCurrentSC.appendChild(svgPenButton);
			
			Rectangle eba = csc.getEraserButtonArea();
			svgEraserButton.getX().getBaseVal().setValue(eba.x);
			svgEraserButton.getY().getBaseVal().setValue(eba.y);
			currentSCNodes.add(svgEraserButton);
			svgCurrentSC.appendChild(svgEraserButton);
			
			Rectangle uba = csc.getUndoButtonArea();
			svgUndoButton.getX().getBaseVal().setValue(uba.x);
			svgUndoButton.getY().getBaseVal().setValue(uba.y);
			currentSCNodes.add(svgUndoButton);
			svgCurrentSC.appendChild(svgUndoButton);
			
			Rectangle bba = csc.getBinButtonArea();
			svgBinButton.getX().getBaseVal().setValue(bba.x);
			svgBinButton.getY().getBaseVal().setValue(bba.y);
			currentSCNodes.add(svgBinButton);
			svgCurrentSC.appendChild(svgBinButton);
			
			//enableUndoButtonSVG(true);
			//enableBinButtonSVG(true);
		}
		else {
			enableDrawButtonSVG(false);
			enableFormButtonSVG(true);
		}
		
		//handleButton
		Rectangle hba = csc.getHandleArea();
		svgHandleButton.getX().getBaseVal().setValue(hba.x);
		svgHandleButton.getY().getBaseVal().setValue(hba.y);
//		currentSCNodes.add(svgHandleButton);
//		svgCurrentSC.appendChild(svgHandleButton);
		currentSCStrokeNodes.add(svgHandleButton);
		svgCurrentSCStrokes.appendChild(svgHandleButton);
		
		// formuleViewer
		OMSVGSVGElement svgForm = doc.createSVGSVGElement();
		if(!csc.recognizeOff && csc.formuleViewer!=null) {
			float x = Math.max(csc.getWriteBox().x+50, csc.getBox()!=null ? csc.getBox().x : 0) ;// + getBox().width/2-parent.formuleViewer.getWidth()/2;
			float y = csc.getWriteBox().y+5;
			csc.formuleViewer.setFont(FormuleFont.createFromFontSize(16));
			csc.formuleViewer.setColor(CssColor.make(38, 115, 182));
			csc.formuleViewer.getMainRegel().draw(svgForm);
			svgForm.getX().getBaseVal().setValue(x);
			svgForm.getY().getBaseVal().setValue(y);
			currentSCNodes.add(svgForm);
			svgCurrentSC.appendChild(svgForm);
		}
		
		//strokes
		//if(newStroke==null) {
		ArrayList<Stroke> strokes = csc.strokeContainer.getStrokes();
		for(int i = 0 ; i < strokes.size() ; i++) {
			Stroke stroke = strokes.get(i);
			OMSVGPathElement strokePath = doc.createSVGPathElement();
			OMSVGPathSegList segsStrokePath = strokePath.getPathSegList();
			float x0 = (float)stroke.getParsePoints().get(0).x;
			float y0 = (float)stroke.getParsePoints().get(0).y;
			//if(r.contains((int)x0,(int)0))
				segsStrokePath.appendItem(strokePath.createSVGPathSegMovetoAbs(x0,y0));
			if(stroke.getParsePointsbox().width>3 ||  stroke.getParsePointsbox().height>3) {
				for(int j = 1 ; j < stroke.getParsePoints().size() ; j++) {
					float x = (float)stroke.getParsePoints().get(j).x ;
					float y = (float)stroke.getParsePoints().get(j).y;
					//if(r.contains((int)x,(int)y))
						segsStrokePath.appendItem(strokePath.createSVGPathSegLinetoAbs(x,y));
				}
			}
			else {
				segsStrokePath.appendItem(strokePath.createSVGPathSegArcAbs(x0+0.01f, y0, 1, 1, 360, true, true));
			}
			segsStrokePath.appendItem(strokePath.createSVGPathSegMovetoAbs(x0,y0));
			segsStrokePath.appendItem(strokePath.createSVGPathSegClosePath());
			strokePath.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, ""+strokeColor);
			strokePath.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "transparent");
			strokePath.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "" + 3.0);
			
			currentSCStrokeNodes.add(strokePath);
			svgCurrentSCStrokes.appendChild(strokePath);
			currentSCStrokeMap.put(stroke, strokePath);
			
			currentSCNodes.add(svgCurrentSCStrokes);
			svgCurrentSC.appendChild(svgCurrentSCStrokes);
			//currentSCStrokeMap.put(stroke, strokePath);
		}
		svg.appendChild(svgCurrentSC);
//		}
//		else {
//			OMSVGPathElement strokePath = doc.createSVGPathElement();
//			OMSVGPathSegList segsStrokePath = strokePath.getPathSegList();
//			float x0 = (float)newStroke.getParsePoints().get(0).x;
//			float y0 = (float)newStroke.getParsePoints().get(0).y;
//			//if(r.contains((int)x0,(int)0))
//				segsStrokePath.appendItem(strokePath.createSVGPathSegMovetoAbs(x0,y0));
//			if(newStroke.getParsePointsbox().width>3 ||  newStroke.getParsePointsbox().height>3) {
//				for(int j = 1 ; j < newStroke.getParsePoints().size() ; j++) {
//					float x = (float)newStroke.getParsePoints().get(j).x ;
//					float y = (float)newStroke.getParsePoints().get(j).y;
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
//			currentSCNodes.add(strokePath);
//			svgCurrentSC.appendChild(strokePath);
//			currentSCStrokeMap.put(newStroke, strokePath);
//		}
			
	}
	
	public void removeCurrentSC() {
		for(int i=0 ; i<currentSCStrokeNodes.size() ; i++) {
			try {
				svgCurrentSCStrokes.removeChild(currentSCStrokeNodes.get(i));
			} catch(Exception e) {}
		}
		try {
			svgCurrentSC.removeChild(svgCurrentSCStrokes);
		} catch(Exception e) {}
		
		for(int i=0 ; i<currentSCGridNodes.size() ; i++) {
			try {
				svgCurrentSCGrid.removeChild(currentSCGridNodes.get(i));
			} catch(Exception e) {}
		}
		try {
			svgCurrentSC.removeChild(svgCurrentSCGrid);
		} catch(Exception e) {}
		
		for(int i=0 ; i<currentSCNodes.size() ; i++) {
			try {
				svgCurrentSC.removeChild(currentSCNodes.get(i));
			} catch(Exception e) {}
		}
		try {
			svg.removeChild(svgCurrentSC);
		} catch(Exception e) {}
		
		svgCurrentSC.getX().getBaseVal().setValue(0);
		svgCurrentSC.getY().getBaseVal().setValue(0);
	}
	
	public void translateCurrentSC(int dx, int dy) {
		svgCurrentSC.getX().getBaseVal().setValue(svgCurrentSC.getX().getBaseVal().getValue() + dx);
		svgCurrentSC.getY().getBaseVal().setValue(svgCurrentSC.getY().getBaseVal().getValue() + dy);
	}
	
	public void translateCurrentSCStrokes(int dx, int dy) {
		svgCurrentSCStrokes.getX().getBaseVal().setValue(svgCurrentSCStrokes.getX().getBaseVal().getValue() + dx);
		svgCurrentSCStrokes.getY().getBaseVal().setValue(svgCurrentSCStrokes.getY().getBaseVal().getValue() + dy);
		int	lineDistance = (int)(10*mathScratchField.schrijfLeesFactor);
		svgCurrentSCGrid.getX().getBaseVal().setValue((svgCurrentSCGrid.getX().getBaseVal().getValue() + dx - 0.5f*lineDistance)%lineDistance + 0.5f*lineDistance);
		svgCurrentSCGrid.getY().getBaseVal().setValue((svgCurrentSCGrid.getY().getBaseVal().getValue() + dy - 0.5f*lineDistance)%lineDistance + 0.5f*lineDistance);
	}
	
	public void stopTranslateSCStrokes() {
		svgCurrentSCStrokes.getX().getBaseVal().setValue(0);
		svgCurrentSCStrokes.getY().getBaseVal().setValue(0);
		svgCurrentSCGrid.getX().getBaseVal().setValue(0);
		svgCurrentSCGrid.getY().getBaseVal().setValue(0);
	}
	
	public void startTranslateSC(KStrokeContainer sc) {
		svg.removeChild(scMap.get(sc));
		svg.appendChild(scMap.get(sc));
		int x = sc.getBox().x-10;
		int y = sc.getBox().y-10;
		int w = sc.getBox().width+20;
		int h = sc.getBox().height+20;
		svgSCShadow = doc.createSVGSVGElement();
		OMSVGRectElement popup = doc.createSVGRectElement(x, y, w, h, 0, 0);
		popup.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, SVGConstants.CSS_WHITE_VALUE);
		svgSCShadow.appendChild(popup);
		
		for(int i=0 ;i<10 ; i++) {
			CssColor c = CssColor.make("rgba("+(200+5*i)+","+(200+5*i)+","+(200+5*i)+","+(1-0.1*i)+")");
			OMSVGRectElement shadow = doc.createSVGRectElement(x-1-2*i,y-1-2*i,w+2+4*i,h+2+4*i, 0, 0);
			shadow.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "transparent");
			shadow.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY,""+c);
			shadow.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "2.0");
			currentSCNodes.add(shadow);
			svgSCShadow.appendChild(shadow);
		}
		svgSCShadow.getX().getBaseVal().setValue(svgSCShadow.getX().getBaseVal().getValue() - scMap.get(sc).getX().getBaseVal().getValue());
		svgSCShadow.getY().getBaseVal().setValue(svgSCShadow.getY().getBaseVal().getValue() - scMap.get(sc).getY().getBaseVal().getValue());
		scMap.get(sc).insertBefore(svgSCShadow, scMap.get(sc).getFirstChild());
	}
	
	public void stopTranslateSC(KStrokeContainer sc) {
		try {
			scMap.get(sc).removeChild(svgSCShadow);
		} catch(Exception e) {}
		svgSCShadow = null;
	}
	
	public void translateSC(KStrokeContainer sc, int dx, int dy) {
		scMap.get(sc).getX().getBaseVal().setValue(scMap.get(sc).getX().getBaseVal().getValue() + dx);
		scMap.get(sc).getY().getBaseVal().setValue(scMap.get(sc).getY().getBaseVal().getValue() + dy);
	}
	
	private OMSVGPathElement initFormulaStroke() {
		OMSVGPathElement formulaStroke = doc.createSVGPathElement();
		formulaStroke.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, ""+strokeColor);
		formulaStroke.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "transparent");
		formulaStroke.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "" + 3.0);
		svg.appendChild(formulaStroke);
		return formulaStroke;
	}
	
	public void addFormulaStrokePoint(DoublePoint point) {
		if(formulaStroke==null) {
			formulaStroke = initFormulaStroke();
			segsFormulaStroke = formulaStroke.getPathSegList();
		}
		if(segsFormulaStroke.getNumberOfItems()==0) {
			segsFormulaStroke.appendItem(formulaStroke.createSVGPathSegMovetoAbs((float)point.x, (float)point.y));
			segsFormulaStroke.appendItem(formulaStroke.createSVGPathSegMovetoAbs((float)point.x, (float)point.y));
			segsFormulaStroke.appendItem(formulaStroke.createSVGPathSegClosePath());
		}
		else {
			segsFormulaStroke.insertItemBefore(formulaStroke.createSVGPathSegLinetoAbs((float)point.x, (float)point.y), segsFormulaStroke.getNumberOfItems()-2);
		}
	}
	
	public void replaceFormulaStrokePoint(DoublePoint point) {
		if(segsFormulaStroke.getNumberOfItems()>3) {
			segsFormulaStroke.replaceItem(formulaStroke.createSVGPathSegLinetoAbs((float)point.x, (float)point.y), segsFormulaStroke.getNumberOfItems()-3);
		}
		else {
			segsFormulaStroke.insertItemBefore(formulaStroke.createSVGPathSegLinetoAbs((float)point.x, (float)point.y), segsFormulaStroke.getNumberOfItems()-2);
		}
	}
	
	public void removeFormulaStroke() {
		if(segsFormulaStroke==null)
			return;
		segsFormulaStroke.clear();
		try {
			svg.removeChild(formulaStroke);
		} catch(Exception e) {}
		formulaStroke = null;
	}
}
