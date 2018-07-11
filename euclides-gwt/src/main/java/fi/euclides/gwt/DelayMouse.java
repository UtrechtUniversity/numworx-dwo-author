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

  static final Logger LOG = Logger.getLogger("DelayMouse");
  
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
      LOG.fine("moved " + moved + " " + (System.currentTimeMillis()-time) + " " + (x-x0) + " " + (y-y0));
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
    this(view, 500L, 5);
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
    ctx.remove(id);
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

}
