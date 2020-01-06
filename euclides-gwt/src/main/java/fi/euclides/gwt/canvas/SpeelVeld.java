package fi.euclides.gwt.canvas;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.Context2d.TextAlign;
import com.google.gwt.canvas.dom.client.Context2d.TextBaseline;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.HandlerRegistrations;
import com.vaadin.pointerevents.client.PointerCancelEvent;
import com.vaadin.pointerevents.client.PointerDownEvent;
import com.vaadin.pointerevents.client.PointerEventInitializer;
import com.vaadin.pointerevents.client.PointerEventsSupport;
import com.vaadin.pointerevents.client.PointerMoveEvent;
import com.vaadin.pointerevents.client.PointerUpEvent;

import fi.euclides.event.EventHandler;
import fi.euclides.event.HitTester;
import fi.euclides.event.NameMapper;
import fi.euclides.event.TrackerContext;
import fi.euclides.model.AbstractViewer;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Track;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.Adapter;
import fi.euclides.util.DefaultAdapter;
import fi.euclides.event.DescriptionBuilder;
import fi.euclides.gwt.DelayMouse;
import fi.euclides.gwt.GWTMouseHandler;
import fi.euclides.gwt.GWTMultiTouchHandler;
import fi.euclides.gwt.GWTPointerHandler;
import fi.euclides.gwt.MouseContext;
import fi.euclides.gwt.ViewerWidget;

public class SpeelVeld extends AbstractViewer implements ViewerWidget {
	  static final Logger LOG = Logger.getLogger("fi.euclides.gwt.canvas.SpeelVeld");

	class SpeelVeldContext implements TrackerContext {

	  final int id;
	  Track track;
	  
    SpeelVeldContext(int id) {
      this.id = id;
      adapter = Adapter.NULL;
    }

    @Override
    public void setTrack(Track track) {
      this.track = track;
    }

    @Override
    public Track getTrack() {
      return track;
    }

    @Override
    public HitTester getHitTester() {
      return SpeelVeld.this.getHitTester();
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

    private Adapter adapter;
    @Override
    public Adapter getAdapter() {
      return adapter;
    }

    @Override
    public void setAdapter(Adapter adapter) {
      this.adapter = adapter;
    }

    @Override
    public String toString() {
      // TODO Auto-generated method stub
      return "Finger" + id;
    }

  }

  private final CssColor black = CssColor.make(0,0,0);
	private final CssColor white = CssColor.make(255,255,255);
	private final static int POINTER_COLOR=5;
	private final static int WHITE=6;
	private final CssColor
		red = CssColor.make("red"),
		green = CssColor.make("green"),
		blue = CssColor.make("blue"),
		magenta = CssColor.make("magenta"),
		lightGray = CssColor.make(192,192,192),
		
		colors[] = {
			black,
			red,
			blue,
			magenta,
			lightGray, 
			CssColor.make(64,64,64),
			white,
		};
	
	@Override
	public boolean contains(double x, double y) {
		return 0 <= x && x <= width && 0 <= y && y <= height;
	}

	protected Canvas canvas;
	protected String fill;
	private Label status;
	protected Context2d context;
	private int height = 400;
	private int width = 400;

	public SpeelVeld(Canvas canvas, Label status) {
		this.canvas = canvas;
		this.context = canvas.getContext2d();
		this.width = canvas.getCoordinateSpaceWidth();
		this.height = canvas.getCoordinateSpaceHeight();
		
		ll.setClip(0, 0, width, height);
		rr.setClip(0, 0, width, height);
		this.status = status;
		setTrack(contexts.values());
		initHandlers(canvas);
	}

	public SpeelVeld(int width, int height) {
		this.width = width;
		this.height = height;
		canvas = Canvas.createIfSupported();
	    context = canvas.getContext2d();
	    canvas.setPixelSize(width,height);	    
		double ratio = nl.uu.fi.dwo.interaction.client.TekstComponent.getDeviceRatio(context); // retina screens
		if(ratio > 1.0) {
			canvas.setCoordinateSpaceHeight((int) (height*ratio));
			canvas.setCoordinateSpaceWidth((int) (width*ratio));
			context.setTransform(ratio, 0, 0, ratio, 0, 0);
		} else {
		//change the canvas dimensions
			this.canvas.setCoordinateSpaceHeight(height);
			this.canvas.setCoordinateSpaceWidth(width);
		}

	    
		status = new Label(" ");
		ll.setClip(0, 0, width, height);
		rr.setClip(0, 0, width, height);
		setTrack(contexts.values());
		initHandlers(canvas);
	}
	
	private void initHandlers(final Canvas canvas) {
		PointerEventsSupport.init();
		boolean haspointer = PointerEventsSupport.isSupported();
		GWTPointerHandler ph;
		HandlerRegistration set = () -> {};
		LOG.info("haspointer = " + haspointer);
		{
			LOG.info("force pointer events support");
			GWTPointerHandler h = new GWTPointerHandler(new DelayMouse(this));
			// en nu?
			canvas.addDomHandler(h, PointerDownEvent.getType());
			canvas.addDomHandler(h, PointerUpEvent.getType());
			canvas.addDomHandler(h, PointerMoveEvent.getType());
			canvas.addDomHandler(h, PointerCancelEvent.getType());
			ph = h;
		}
		
		boolean hastouch = TouchStartEvent.isSupported();
		if(hastouch) {
			GWTMultiTouchHandler h = new GWTMultiTouchHandler(new DelayMouse(this));
			set = HandlerRegistrations.compose(
			canvas.addTouchCancelHandler(h),
			canvas.addTouchEndHandler(h),
			canvas.addTouchMoveHandler(h),
			canvas.addTouchStartHandler(h));
		} 
		{
			GWTMouseHandler h = new GWTMouseHandler(new DelayMouse(this, 200L, 4)); // testing...
			set = HandlerRegistrations.compose(set,
			canvas.addMouseDownHandler(h),
			canvas.addMouseUpHandler(h),
			canvas.addMouseMoveHandler(h));
			ph.setLegacy(set);
		}
		
		
	}

	
	@Override
	public void drawLine(double x1, double y1, double x2, double y2) {
		context.beginPath();
		context.moveTo(x1, y1);
		context.lineTo(x2, y2);
		context.stroke();
	}

	@Override
	protected void drawCircle(double i, double j, double k) {
		context.beginPath();
		k = k/2;
		context.arc(i+k, j+k, k, 0, 7);
		if(fill != null && !"none".equals(fill)) {
			context.setFillStyle(fill);
			context.fill();
		}
		context.stroke();
	}
	
	@Override
	protected void drawArc(double x, double y, double k, double start,
			double length) {
		k = k/2;
		if(fill != null && !"none".equals(fill)) {
			context.beginPath();
			context.moveTo(x+k, y+k);
			context.arc(x+k, y+k, k, -start, -(start+length), length>0);
			context.closePath();
			context.setFillStyle(fill);
			context.fill();
		}		
		context.beginPath();
		context.arc(x+k, y+k, k, -start, -(start+length), length>0);
		context.stroke();
	}

	@Override
	protected void drawPoint(double x, double y) {
		context.beginPath();
		context.arc(x,y,1,0,7);
		context.fill();
	}

	@Override
	protected void drawString(String string, double x, double y) {
		context.fillText(string, x, y);
	}

	protected void fillCircle(double i, double j, double k) {
		context.beginPath();
		k = k/2;
		context.arc(i+k, j+k, k, 0, 7);
		context.fill();
	}

	@Override
	public void paint() {
		context.save();
		context.clearRect(0, 0, width, height);
		context.translate(offX, offY);
		context.beginPath();
//		context.rect(0, 0, width, height);
//		context.closePath();
//		context.clip();
// FIXME alleen als offX,offY verandert
		ll.setClip(-offX,-offY, width, height);
		rr.setClip(-offX,-offY, width, height);
		context.setFillStyle(black);
		context.setStrokeStyle(black);
		this.update();
		if(mouse != null) mouse.paint();
		if(extra != null) extra.paint();
		context.restore();
	}

	@Override
	public void setColor(int n) {
		CssColor color = colors[n];
		context.setFillStyle(color);
		context.setStrokeStyle(color);
	}

	@Override
	public String describe(Destroyable d) {
		DescriptionBuilder builder = new DescriptionBuilder(getMapper());
		d.visit(builder);
		return builder.toString();
	}

	class Pointer {
		int x,y;
//		int w1 = 40, w2 = 3;
		double w1 = 1.5;
		Pointer(int x,int y) { drag(x,y); }
		void drag(int x, int y) {this.x = x; this.y = y; }
		void paint() { 
			
			context.save();
			setColor(POINTER_COLOR);
			drawCircle(x-w1, y-w1, w1*2);
//			setColor(WHITE);
//			fillCircle(0,0,w1*2);
//			setColor(POINTER_COLOR);
//			drawCircle(w1,w1,1);
//			drawLine(0, w1, w1*2, w1);
//			drawLine(w1,0,w1,w1*2);
//			context.beginPath();
//			context.arc(w1, w1, w1, 0, 7);
//			context.stroke();
//			context.clip();
//			context.translate(-x+w1, -y+w1);
//			update();
			context.restore();
		}
	}
	
	Pointer mouse;
	
	@Override
	public void setStatus(String string) {
		status.setText(string);
	}

	EventHandler handler;
	private boolean moved;
	private AbstractViewer extra;
	protected int offX;
	protected int offY;
	
	Map<Integer, TrackerContext> contexts = new HashMap<>();
	
	protected TrackerContext getCtx(int id) {
	  TrackerContext c = contexts.get(id);
	  if(c == null) {
	      c = new SpeelVeldContext(id);
	      contexts.put(id, c);
	  }
	  return c;
	}
	
	public void processMouseDown(int x, int y,int id) {
		handler.pointerPressed(x-offX, y-offY,getCtx(id));
		moved = false;
		paint();
	}
		
  @Override
  public void processMouseDown(MouseContext ctx) {
	  DefaultAdapter.getDefault(getCtx(ctx.getID())).put(MouseContext.class, ctx);
	  ViewerWidget.super.processMouseDown(ctx);
      DefaultAdapter.getDefault(getCtx(ctx.getID())).put(MouseContext.class, null);
  }

  @Override
  public void processMouseUp(MouseContext ctx) {
    DefaultAdapter.getDefault(getCtx(ctx.getID())).put(MouseContext.class, ctx);
    ViewerWidget.super.processMouseUp(ctx);
    DefaultAdapter.getDefault(getCtx(ctx.getID())).put(MouseContext.class, null);
  }

  @Override
  public void processMouseDrag(MouseContext ctx) {
    DefaultAdapter.getDefault(getCtx(ctx.getID())).put(MouseContext.class, ctx);
    ViewerWidget.super.processMouseDrag(ctx);
    DefaultAdapter.getDefault(getCtx(ctx.getID())).put(MouseContext.class, null);

  }

  @Override
	public void setPointerHandler(EventHandler eventHandler) {
		handler = eventHandler;
	}

	public void processMouseDrag(int x, int y,int id ) {
		moved=true;
		handler.pointerDragged(x-offX, y-offY,getCtx(id));
		paint();
	}

	public void processMouseUp(int x, int y,int id) {
		x -= offX; y -= offY;
		if (!moved)
		{
			LOG.fine("clicked " + handler);
			handler.pointerClicked(x, y,getCtx(id));
		}
		handler.pointerReleased(x, y,getCtx(id));
		paint();
	}

	@Override
	public Numbers clipTop() {
		return Numbers.createInteger(-offY);
	}

	@Override
	public Numbers clipBottom() {
		return Numbers.createInteger(height-offY);
	}

	@Override
	public Numbers clipLeft() {
		return Numbers.createInteger(-offX);
	}

	@Override
	public Numbers clipRight() {
		return Numbers.createInteger(width-offX);
	}

	public void add(AbstractViewer extra) {
		this.extra = extra;
		extra.setModel(getModel());
	}

	@Override
	public Widget asWidget() {
		return canvas;
	}

	@Override
	public AbstractViewer getViewer() {
		return this;
	}

	protected void setCssColor(CssColor c) {
		context.setFillStyle(c);
		context.setStrokeStyle(c);
	}

	@Override
	public void init(int w, int h) {
		width = w;
		height = h;
		offX=0;
		offY=0;
		canvas.setPixelSize(width,height);
	    canvas.setCoordinateSpaceWidth(width);
	    canvas.setCoordinateSpaceHeight(height);
		ll.setClip(0, 0, width, height);
		rr.setClip(0, 0, width, height);
	}

	@Override
	public void moveAway(int x, int y) {
		if(mouse == null) {
			//mouse = new Pointer(x,y);
			//offY = 20; // Ulli patent
		} else {
			mouse.drag(x,y);
		}
	}

	@Override
	public void moveBack() {
		mouse = null;
		//offX = 0; offY = 0;
	}

	@Override
	public void setMapper(NameMapper mapper) {
	}

	public void drawString(String value, double x, double y,
			String h, String v, String bg) {
		TextAlign halign = TextAlign.START;
		if (ViewerWidget.TEXT_MIDDLE.equals(h)) halign = TextAlign.CENTER;
		else if (ViewerWidget.TEXT_END.equals(h)) halign = TextAlign.END;
		context.setTextAlign(halign);
		TextBaseline valign = TextBaseline.ALPHABETIC;
		if (ViewerWidget.TEXT_TOP.equals(v)) valign = TextBaseline.TOP;
		else if(ViewerWidget.TEXT_BOTTOM.equals(v)) valign = TextBaseline.BOTTOM;
		else if(ViewerWidget.TEXT_CENTRAL.equals(v)) valign = TextBaseline.MIDDLE;
		context.setTextBaseline(valign);
		if(bg != null && !"none".equals(bg))
		{	context.save();
			context.setFillStyle(bg);
			double w = context.measureText(value).getWidth();
			double s = 12;
			double rx = x;
			double ry = y;
			switch(halign) {
			case END: rx -= w; break;
			case CENTER: rx -= w/2; break;
			default:
			}
			switch(valign) {
			case ALPHABETIC: ry -= s* 0.75;  break; // schatting
			case BOTTOM: ry -= s; break;
			case MIDDLE: ry -= s/2;break;
			default:
			}
			context.fillRect(rx, ry, w, s);
			context.restore();
		}
		drawString(value, x, y);
		
	}

//	@Override
//	public EventHandler getPanHandler() {
//		return new PanHandler("Pan", this);
//	}

	@Override
	public void setBackground(String string) {		
	}

}
