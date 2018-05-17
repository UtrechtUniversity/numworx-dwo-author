package fi.euclides.gwt.svg;

import java.util.Vector;

import org.vectomatic.dom.svg.OMNode;
import org.vectomatic.dom.svg.OMNodeList;
import org.vectomatic.dom.svg.OMSVGCircleElement;
import org.vectomatic.dom.svg.OMSVGDefsElement;
import org.vectomatic.dom.svg.OMSVGDocument;
import org.vectomatic.dom.svg.OMSVGGElement;
import org.vectomatic.dom.svg.OMSVGLength;
import org.vectomatic.dom.svg.OMSVGLineElement;
import org.vectomatic.dom.svg.OMSVGPathElement;
import org.vectomatic.dom.svg.OMSVGPathSegList;
import org.vectomatic.dom.svg.OMSVGRect;
import org.vectomatic.dom.svg.OMSVGRectElement;
import org.vectomatic.dom.svg.OMSVGSVGElement;
import org.vectomatic.dom.svg.OMSVGStyle;
import org.vectomatic.dom.svg.OMSVGTextElement;
import org.vectomatic.dom.svg.ui.SVGImage;
import org.vectomatic.dom.svg.utils.OMSVGParser;
import org.vectomatic.dom.svg.utils.SVGConstants;

import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.user.client.ui.Widget;

import fi.euclides.event.EventHandler;
import fi.euclides.event.NameMapper;
import fi.euclides.event.TrackerContext;
import fi.euclides.gwt.GWTMouseHandler;
import fi.euclides.gwt.GWTTouchHandler;
import fi.euclides.gwt.ViewerWidget;
import fi.euclides.model.AbstractViewer;
import fi.euclides.model.Boog;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Punt;
import fi.euclides.model.Track;
import fi.euclides.model.math.Numbers;


public class SVGWidget extends AbstractViewer implements ViewerWidget, TrackerContext {

	private SVGImage image;
	protected OMSVGDocument doc;
	private int width;
	private int height;
	private int offX,offY;
	private OMSVGGElement g;
	private OMSVGCircleElement pointer;
	private Track track;

	public SVGWidget(int width, int height) {
		this();
		init(width, height);
	}

	public SVGWidget() {
        setTrack(this);
		doc = OMSVGParser.currentDocument();
		image = new SVGImage();
		image.setSvgElement(doc.createSVGSVGElement());		
		g = doc.createSVGGElement();
		defs = doc.createSVGDefsElement();
		pointer = doc.createSVGCircleElement(-2, -2, 1.5f);
		getSvgElement().appendChild(defs);
		getSvgElement().appendChild(pointer);
		getSvgElement().appendChild(g);
		boolean hastouch = TouchStartEvent.isSupported();
		if(hastouch) {
			GWTTouchHandler h = new GWTTouchHandler(this);
			image.addTouchCancelHandler(h);
			image.addTouchEndHandler(h);
			image.addTouchMoveHandler(h);
			image.addTouchStartHandler(h);
		} else {
			GWTMouseHandler h = new GWTMouseHandler(this);
			image.addMouseDownHandler(h);
			image.addMouseMoveHandler(h);
			image.addMouseUpHandler(h);
		}
	}

	public void init(int width, int height) {
		this.width = width;
		this.height = height;
		moveBack();
		image.setPixelSize(width, height);
	}
	
	
	
	private void setPointer(float x, float y) {
		pointer.getCx().getBaseVal().setValue(x);
		pointer.getCy().getBaseVal().setValue(y);
	}
	
	protected String color = "black";
	protected String fill = "none";

	protected void setCssColor(CssColor c) {
		color = c.toString();
	}
	
		@Override
		public Numbers clipTop() {
			return Numbers.ZERO;
		}

		@Override
		public Numbers clipBottom() {
			return Numbers.createInteger(height);
		}

		@Override
		public Numbers clipLeft() {
			return Numbers.ZERO;
		}

		@Override
		public Numbers clipRight() {
			return Numbers.createInteger(width);
		}

		@Override
		public void drawLine(double x1, double y1, double x2, double y2) {
			OMSVGLineElement line = doc.createSVGLineElement((float)x1, (float)y1, (float)x2, (float)y2);
			line.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, color);
			getBody().appendChild(line);
		}

		@Override
		protected void fillCircle(double x, double y, double w) {
			float r = (float)w / 2.0f;
			float cx = (float)x + r;
			float cy = (float)y + r;
 			OMSVGCircleElement circle = doc.createSVGCircleElement(cx, cy, r);
 			circle.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, color);
 			getBody().appendChild(circle);
		}

		@Override
		protected void drawCircle(double x, double y, double w) {
			float r = (float)w / 2.0f;
			float cx = (float)x + r;
			float cy = (float)y + r;
 			OMSVGCircleElement circle = doc.createSVGCircleElement(cx, cy, r);
 			OMSVGStyle style = circle.getStyle();
			style.setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, color);
 			style.setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, fill);
			getBody().appendChild(circle);		
		}

		@Override
		public void setColor(int c) {
			switch(c) {
			case magenta: color = "magenta"; break;
			default:
			case BLACK: color = "black"; break;
			case RED: color="red"; break;
			case blue: color="blue"; break;
			case LIGHT_GRAY: color="light_gray"; break;
			}
		}

		/* (non-Javadoc)
		 * @see fi.euclides.model.AbstractViewer#visitBoog(fi.euclides.model.Boog)
		 */
		@Override
		public void visitBoog(Boog b) {
			selectColor(b);
			Punt start = Boog.startOf(b);
			Punt end = Boog.endOf(b);
			float r = (float)b.getR();
			float angle = 0f;
			double length = b.length();
			boolean largeArcFlag = Math.abs(length) > Math.PI;
			boolean sweepFlag = length < 0;
			float x, y;
			OMSVGPathElement path;
			OMSVGPathSegList seg;
			OMSVGStyle style;
			if (fill != null && !"none".equals(fill)) {
				path = doc.createSVGPathElement();
				seg = path.getPathSegList();
				x = (float) b.getCenter().getXd();
				y = (float) b.getCenter().getYd();
				seg.appendItem(path.createSVGPathSegMovetoAbs(x, y));
				x = (float)start.getXd();
				y = (float)start.getYd();
				seg.appendItem(path.createSVGPathSegLinetoAbs(x, y));
				x = (float)end.getXd();
				y = (float)end.getYd();
				seg.appendItem(path.createSVGPathSegArcAbs(x, y, r, r, angle, largeArcFlag, sweepFlag));
				seg.appendItem(path.createSVGPathSegClosePath());
	 			style = path.getStyle();
				style.setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, fill);
				getBody().appendChild(path);
			}
			path = doc.createSVGPathElement();
			seg = path.getPathSegList();
			x = (float)start.getXd();
			y = (float)start.getYd();
			seg.appendItem(path.createSVGPathSegMovetoAbs(x, y));
			x = (float)end.getXd();
			y = (float)end.getYd();
			seg.appendItem(path.createSVGPathSegArcAbs(x, y, r, r, angle, largeArcFlag, sweepFlag));
 			style = path.getStyle();
			style.setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, color);
			style.setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "none");
			getBody().appendChild(path);	
		}

		@Override
		protected void drawPoint(double x, double y) {
 			OMSVGCircleElement circle = doc.createSVGCircleElement((float)x, (float)y, 0.5f);
 			circle.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, color);
 			getBody().appendChild(circle);

		}

		@Override
		protected void drawString(String string, double x, double y) {
			short unitType = OMSVGLength.SVG_LENGTHTYPE_NUMBER;
			OMSVGTextElement text = doc.createSVGTextElement((float)x, (float)y, unitType, string);
 			text.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, color);
			getBody().appendChild(text);
		}

		@Override
		public void paint() {
			OMNodeList<OMNode> list = getBody().getChildNodes();
			for(int i = 0; i < list.getLength(); ) {
				getBody().removeChild(list.getItem(0));
			}

			ll.setClip(clipLeft(), clipTop(), clipRight(), clipBottom());
			rr.setClip(clipLeft(), clipTop(), clipRight(), clipBottom());

			update();
		}
		
	
	protected OMSVGSVGElement getSvgElement() {
			return image.getSvgElement();
		}

	protected OMSVGGElement getBody() {
		return g;
	}
	
	protected OMSVGDefsElement getHead() {
		return defs;
	}
	
	public AbstractViewer getViewer() {
		return this;
	}

	@Override
	public Widget asWidget() {
		return image;
	}

	private EventHandler handler;
	private boolean moved;
	private OMSVGDefsElement defs;
	
	@Override
	public void setPointerHandler(EventHandler eventHandler) {
		handler = eventHandler;
	}

	@Override
	public void processMouseDown(int x, int y,int id) {
		handler.pointerPressed(x, y,this);
		moved = false;
		paint();
	}

	public void moveAway(int x, int y) {
		setPointer(x,y);
		if(offY != 0|| offX != 0) return;
		offY = 20; // patent Uli
		setViewBox();
		pointer.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "black"); // COLOR of POINTER
		 
	}
	
	public void moveBack() {
		offY = 0; offX = 0;
		setViewBox();
		pointer.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "none");
	}

	private void setViewBox() {
		getSvgElement().setViewBox(offX, offY, width, height);
	}

	@Override
	public void processMouseUp(int x, int y, int id) {
		if(!moved)
			handler.pointerClicked(x, y,this);
		handler.pointerReleased(x, y,this);
		paint();
	}

	@Override
	public void processMouseDrag(int x, int y, int id) {
		handler.pointerDragged(x, y,this);
		moved = true;
		paint();
	}

	@Override
	public void setMapper(NameMapper mapper) {
		// TODO Auto-generated method stub
		
	}

	public void drawString(String value, double x, double y,
			String h, String v, String bg) {
		drawString(value, x, y);
		OMSVGTextElement text = (OMSVGTextElement) getBody().getLastChild();
		OMSVGStyle style = text.getStyle();
		if(h != null) style.setSVGProperty(SVGConstants.CSS_TEXT_ANCHOR_PROPERTY, h);
		if(v != null) style.setSVGProperty(SVGConstants.CSS_DOMINANT_BASELINE_PROPERTY, v);
		if(bg != null) { // TODO randje?
			OMSVGRect bbox = text.getBBox();
			OMSVGRectElement rect = doc.createSVGRectElement(bbox);
			rect.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, bg);
			getBody().insertBefore(rect, text);
		}
	}

	@Override
	public EventHandler getPanHandler() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setBackground(String string) {
		// TODO Auto-generated method stub
		
	}

  @Override
  public Track getTrack() {
    return track;
  }

  @Override
  public void clearSelection() {
    getModel().clearSelection();
  }

  @Override
  public void toggle(Destroyable d) {
    getModel().toggle(d);
  }

  @Override
  public Vector<Destroyable> selection() {
    return getModel().getSelect();
  }

  @Override
  public void setTrack(Track track) {
    this.track = track;
  }

}
