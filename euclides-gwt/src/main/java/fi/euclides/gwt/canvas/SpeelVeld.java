package fi.euclides.gwt.canvas;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import fi.euclides.event.EventHandler;
import fi.euclides.event.NameMapper;
import fi.euclides.model.AbstractViewer;
import fi.euclides.model.Destroyable;
import fi.euclides.model.math.Numbers;
import fi.euclides.event.DescriptionBuilder;
import fi.euclides.gwt.GWTMouseHandler;
import fi.euclides.gwt.GWTTouchHandler;
import fi.euclides.gwt.ViewerWidget;

public class SpeelVeld extends AbstractViewer implements ViewerWidget {

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

	private Canvas canvas;
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
		initHandlers(canvas);
	}

	public SpeelVeld(int width, int height) {
		canvas = Canvas.createIfSupported();
		canvas.setPixelSize(width,height);
	    canvas.setCoordinateSpaceWidth(width);
	    canvas.setCoordinateSpaceHeight(height);
	    context = canvas.getContext2d();
		status = new Label(" ");
		ll.setClip(0, 0, width, height);
		rr.setClip(0, 0, width, height);
		initHandlers(canvas);
	}
	
	private void initHandlers(final Canvas canvas) {
		boolean hastouch = TouchStartEvent.isSupported();
		if(hastouch) {
			GWTTouchHandler h = new GWTTouchHandler(this);
			canvas.addTouchCancelHandler(h);
			canvas.addTouchEndHandler(h);
			canvas.addTouchMoveHandler(h);
			canvas.addTouchStartHandler(h);
		} else {
			GWTMouseHandler h = new GWTMouseHandler(this);
			canvas.addMouseDownHandler(h);
			canvas.addMouseUpHandler(h);
			canvas.addMouseMoveHandler(h);
		}
		
		
	}

	
	@Override
	protected void drawLine(double x1, double y1, double x2, double y2) {
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
		context.stroke();
	}
	
	@Override
	protected void drawArc(double x, double y, double k, double start,
			double length) {
		context.beginPath();
		k = k/2;
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
		context.beginPath();
		context.rect(0, 0, width, height);
		context.closePath();
		context.clip();
		context.setFillStyle(white);
		context.fillRect(0, 0, width, height);
		context.setFillStyle(black);
		context.setStrokeStyle(black);
		this.update();
		if(mouse != null) mouse.paint();
		if(extra != null) extra.paint();
	}

	@Override
	protected void setColor(int n) {
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
		int w1 = 40, w2 = 3;
		Pointer(int x,int y) { drag(x,y); }
		void drag(int x, int y) {this.x = x; this.y = y; }
		void paint() { 
			context.save();
			setColor(POINTER_COLOR);
			drawCircle(x-w1, y-w1, w1*2);
			setColor(WHITE);
			fillCircle(0,0,w1*2);
			setColor(POINTER_COLOR);
			drawCircle(w1,w1,1);
			drawLine(0, w1, w1*2, w1);
			drawLine(w1,0,w1,w1*2);
			context.beginPath();
			context.arc(w1, w1, w1, 0, 7);
			context.stroke();
			context.clip();
			context.translate(-x+w1, -y+w1);
			update();
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
	
	public void processMouseDown(int x, int y) {
		handler.pointerPressed(x, y);
		moved = false;
		mouse = new Pointer(x,y);
		paint();
	}

	@Override
	public void setPointerHandler(EventHandler eventHandler) {
		handler = eventHandler;
	}

	public void processMouseDrag(int x, int y) {
		moved=true;
		handler.pointerDragged(x, y);
		mouse.drag(x, y);
		paint();
	}

	public void processMouseUp(int x, int y) {
		if (!moved)
			handler.pointerClicked(x, y);
		handler.pointerReleased(x, y);
		mouse = null;
		paint();
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
		canvas.setPixelSize(width,height);
	    canvas.setCoordinateSpaceWidth(width);
	    canvas.setCoordinateSpaceHeight(height);
		ll.setClip(0, 0, width, height);
		rr.setClip(0, 0, width, height);
	}

	@Override
	public void moveAway(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveBack() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMapper(NameMapper mapper) {
		// TODO Auto-generated method stub
		
	}

}
