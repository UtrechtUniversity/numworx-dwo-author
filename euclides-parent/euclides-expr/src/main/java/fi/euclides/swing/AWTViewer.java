package fi.euclides.swing;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

import fi.euclides.event.EventHandler;
import fi.euclides.event.HumanContext;
import fi.euclides.event.TrackerContext;
import fi.euclides.model.AbstractViewer;
import fi.euclides.model.Destroyable;
import fi.euclides.model.ExtendedLijn;
import fi.euclides.model.Lijn;
import fi.euclides.model.Model;
import fi.euclides.model.Segment;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.Adapter;
import fi.euclides.util.DefaultAdapter;

public abstract class AWTViewer extends AbstractViewer implements MouseListener, MouseMotionListener, TrackerContext {
	protected Graphics2D g;
	protected EventHandler handler;
	public int offX, offY, width, height; 
	
	class Labeler2 extends Labeler {

		/* (non-Javadoc)
		 * @see fi.euclides.model.AbstractViewer.Labeler#visitLijn(fi.euclides.model.Lijn)
		 */
		public void visitLijn(Lijn l) {
			ExtendedLijn ll = AWTViewer.this.ll;
			//if(l instanceof Ray)
			//	ll = AWTViewer.this.rr;
			ll.setClip(-offX, -offY, width, height);
			ll.setLijn(l);
			if (Math.abs(ll.getDX())>Math.abs(ll.getDY()))
			{
				double x = ll.getX1(), y = ll.getY1();
				if(x < -offX)
				{
					y += (-offX-x)*ll.getDY()/ll.getDX();
					x  =  -offX;
				}
				if(y < -offY+15)
				{
					x += (-offY+15-y)/ll.getDY()*ll.getDX();
					y  =  -offY+15;
				}
				if(y > -offY+height)
				{
					x += (-offY+height-y)/ll.getDY()*ll.getDX();
					y  =  -offY+height;
				}
				drawString(AWTViewer.this.toString(l), (int)x+4, (int)(-4+y+4*ll.getDY()/ll.getDX()));
				return;
			}
			double x = ll.getX1(), y = ll.getY1();
			if(y < -offY)
			{
				x += (-offY-y)/ll.getDY()*ll.getDX();
				y  =  -offY;
			}
			if(x < -offX)
			{
				y += (-offX-x)*ll.getDY()/ll.getDX();
				x  =  -offX;
			}
			if(x > -offX+width-15)
			{
				y += (-offX+width-15-x)*ll.getDY()/ll.getDX();
				x  =  -offX+width-15;
			}

			drawString(AWTViewer.this.toString(l), (int)x+4, (int)(15+y));
			return;
			
			
//			super.visitLijn(l);
		}

		public void visitSegment(Segment s) {
			super.visitLijn(s);
		}
		
	}
	
	
	protected AWTViewer() {
	}

	protected AWTViewer(Model model) {
		super(model);
	}

	{
		LABELER = new Labeler2();
	}
	
	/* (non-Javadoc)
	 * @see viewer.AbstractViewer#drawString(java.lang.String, int, int)
	 */
	public void drawString(String string, double x, double y) {
		g.drawString(string, rint(x), rint(y));		
	}

	protected RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	public void paint(Graphics g2) {
		g = (Graphics2D) g2;
		g.translate(offX,offY);
		g.setRenderingHints(rh);
		ll.setClip(g.getClipBounds().x, g.getClipBounds().y, g.getClipBounds().width, g.getClipBounds().height);
		rr.setClip(g.getClipBounds().x, g.getClipBounds().y, g.getClipBounds().width, g.getClipBounds().height);
		update();
	}

	/* (non-Javadoc)
	 * @see euclides.event.Tracker#contains(double, double)
	 */
	public boolean contains(double x, double y) {
		return g.getClipBounds().contains(x, y);
	}

	/* (non-Javadoc)
	 * @see euclides.event.Tracker#setPointerHandler(euclides.event.EventHandler)
	 */
	public void setPointerHandler(EventHandler eventHandler) {
		this.handler = eventHandler;
	}

	/* (non-Javadoc)
	 * @see viewer.AbstractViewer#drawLine(int, int, int, int)
	 */
	protected void drawLine(double x1, double y1, double x2, double y2) {
		Shape l = new Line2D.Double(x1, y1, x2, y2);
		g.draw(l);
	}

	/* (non-Javadoc)
	 * @see viewer.AbstractViewer#drawOval(int, int, int, int)
	 */
	protected void drawCircle(double x, double y, double width) {
		Shape c = new Ellipse2D.Double(x,y, width, width);
		g.draw(c);
	}
	
	protected void drawArc(double x, double y, double w, double startAngle, double arcAngle) {
		startAngle *= R_TO_D;
		arcAngle *= R_TO_D;
		Shape s = new Arc2D.Double(x, y, w, w, startAngle, arcAngle, Arc2D.OPEN);
		g.draw(s);
	}
	
	
	
	/* (non-Javadoc)
	 * @see viewer.AbstractViewer#drawPoint(int, int)
	 */
	protected void drawPoint(double x, double y) {
		Shape r = new Rectangle2D.Double(x, y, 1.0,1.0);
		g.fill(r);
	}

	/* (non-Javadoc)
	 * @see viewer.AbstractViewer#fillOval(int, int, int, int)
	 */
	protected void fillCircle(double x, double y, double width) {
		Shape c = new Ellipse2D.Double(x,y, width, width);
		g.fill(c);
	}

	/* (non-Javadoc)
	 * @see viewer.AbstractViewer#setColor(java.awt.Color)
	 */
	Color colors[] = new Color[] { Color.BLACK, Color.RED, Color.BLUE, Color.MAGENTA, Color.LIGHT_GRAY } ;

	
	protected void setColor(int magenta) {
		g.setColor(colors[magenta]);
	}
	
	private void setHumanContext(MouseEvent event) {
		DefaultAdapter adapter = DefaultAdapter.getDefault(this);
		HumanContext context = new HumanContext() {

			@Override
			public boolean isShiftDown() {
				return event.isShiftDown();
			}

			@Override
			public boolean isControlDown() {
				return event.isControlDown();
			}

			@Override
			public long getTimestamp() {
				return event.getWhen();
			} };
		adapter.put(HumanContext.class, context);	
	}
	
	private void clrHumanContext() {
		DefaultAdapter adapter = DefaultAdapter.getDefault(this);
		adapter.put(HumanContext.class, null);
	}
	
	public void mouseClicked(MouseEvent e) {
		if(handler != null) {
			setHumanContext(e);
			handler.pointerClicked(e.getX()-offX, e.getY()-offY, this);
			clrHumanContext();
			paint();
		}
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		if(handler != null && e.getButton() == MouseEvent.BUTTON1) {
			setHumanContext(e);
			handler.pointerPressed(e.getX()-offX, e.getY()-offY,this);
			clrHumanContext();
			paint();
		}
	}

	public void mouseReleased(MouseEvent e) {
		if(handler != null && e.getButton() == MouseEvent.BUTTON1) {
			setHumanContext(e);
			handler.pointerReleased(e.getX()-offX, e.getY()-offY,this);
			clrHumanContext();
			paint();
		}
	}

	public void mouseDragged(MouseEvent e) {
		if(handler != null && (e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) == MouseEvent.BUTTON1_DOWN_MASK) {
			setHumanContext(e);
			handler.pointerDragged(e.getX()-offX, e.getY()-offY,this);
			clrHumanContext();
			paint();
		}
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void setCursor(Cursor cursor) {
	}
	
	public Numbers clipTop()
	{
		return Numbers.createInteger(-offY);
	}
	public Numbers clipBottom()
	{
		return Numbers.createInteger(height-offY);
	}
	public Numbers clipLeft()
	{
		return Numbers.createInteger(-offX);
	}
	
	public Numbers clipRight() {
		return Numbers.createInteger(width-offX);
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

  private Adapter adapter = Adapter.NULL;

  @Override
  public Adapter getAdapter() {
    return adapter;
  }

  @Override
  public void setAdapter(Adapter adapter) {
    this.adapter = adapter;
  }
  
	
}
