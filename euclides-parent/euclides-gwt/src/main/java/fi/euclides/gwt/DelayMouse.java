/**
 * 
 */
package fi.euclides.gwt;

import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

/**
 * @author wim
 *
 */
public class DelayMouse implements MouseConsumer {

  //static final Logger LOG = Logger.getLogger("DelayMouse");
  
  MouseConsumer view;

  class Context {
    final long time;
    final int x0,y0;
    boolean moved;
    Context(int x0, int y0) {
      this.x0 = x0;
      this.y0 = y0;
      time = System.currentTimeMillis() + delay;
    }
    public boolean moved(int x, int y) {
      moved = 
          moved 
          || System.currentTimeMillis() > time
          || Math.abs(x-x0) > distance
          || Math.abs(y-y0) > distance;
      //LOG.fine("moved " + moved + " " + (System.currentTimeMillis()-time) + " " + (x-x0) + " " + (y-y0));
      return moved;
    }

  }
  
  final Map<Integer,Context> ctx;
  final long delay;
  final int distance;
  /**
   * 
   */
  public DelayMouse(MouseConsumer view) {
    this(view, 1000L, 10);
  }

  public DelayMouse(MouseConsumer view, long l, int i) {
    this.view = view;
    this.delay = l;
    this.distance = i;
    this.ctx = new TreeMap<>();
  }

  /* (non-Javadoc)
   * @see fi.euclides.gwt.MouseConsumer#processMouseDown(int, int, int)
   */
  @Override
  public void processMouseDown(int x, int y, int id) {
    Context c = new Context(x,y);
    ctx.put(id, c);
    view.processMouseDown(x, y, id);
  }

  /* (non-Javadoc)
   * @see fi.euclides.gwt.MouseConsumer#processMouseUp(int, int, int)
   */
  @Override
  public void processMouseUp(int x, int y, int id) {
    Context c = ctx.remove(id);
    //if(c != null) LOG.fine("up, time = " + (System.currentTimeMillis()-c.time));
    if(!c.moved(x, y))
    {	x = c.x0;
        y = c.y0;
    }
    view.processMouseUp(x, y, id);
  }

  /* (non-Javadoc)
   * @see fi.euclides.gwt.MouseConsumer#processMouseDrag(int, int, int)
   */
  @Override
  public void processMouseDrag(int x, int y, int id) {
    Context c = ctx.get(id);
    if (c.moved(x,y)) {
      view.processMouseDrag(x, y, id); 
    }

  }

  @Override
  public void processMouseDown(MouseContext ctx) {
    Context c = new Context(ctx.getX(),ctx.getY());
    this.ctx.put(ctx.getID(), c);
    view.processMouseDown(ctx);
  }

  static class MouseContextDelegate implements MouseContext {
    private final MouseContext delegate;
    private final Context ctx;
    private MouseContextDelegate(MouseContext delegate, Context ctx) {
      this.delegate = delegate;
      this.ctx = ctx;
    }
    public int getID() {
      return delegate.getID();
    }
    public int getScreenX() {
      return delegate.getScreenX();
    }
    public int getScreenY() {
      return delegate.getScreenY();
    }
    public int getClientX() {
      return delegate.getClientX();
    }
    public int getClientY() {
      return delegate.getClientY();
    }
    public long getTimestamp() {
      return delegate.getTimestamp();
    }
    @Override
    public int getX() {
      return ctx.x0;
    }
    @Override
    public int getY() {
      return ctx.y0;
    }
    
  }
  
  @Override
  public void processMouseUp(MouseContext ctx) {
    Context c = this.ctx.remove(ctx.getID());
    //if(c != null) LOG.fine("up, time = " + (System.currentTimeMillis()-c.time));
    if(!c.moved(ctx.getX(), ctx.getY()))
    {   
      ctx = new MouseContextDelegate(ctx, c);
    }
    view.processMouseUp(ctx);
  }

  @Override
  public void processMouseDrag(MouseContext ctx) {
    Context c = this.ctx.get(ctx.getID());
    if (c.moved(ctx.getX(),ctx.getY())) {
      view.processMouseDrag(ctx);
    }
  }

}
