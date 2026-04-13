package fi.euclides.gwt;

import java.util.logging.Logger;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.dom.client.HumanInputEvent;
import com.google.gwt.event.dom.client.TouchCancelEvent;
import com.google.gwt.event.dom.client.TouchCancelHandler;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchMoveHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;

public class GWTMultiTouchHandler
    implements
      TouchStartHandler,
      TouchMoveHandler,
      TouchEndHandler,
      TouchCancelHandler {

  final private static Logger LOG = Logger.getLogger("GWTMultiTouchHandler");
  private MouseConsumer viewer;

  private static class CTX implements MouseContext {
    private final Element e;
    private final Touch   t;
    private final long stamp;
    private HumanInputEvent<?> ev;
    private CTX(Element e, Touch t, long stamp, HumanInputEvent<?> ev) {
      this.e = e;
      this.t = t;
      this.stamp = stamp;
    }
    @Override
    public int getID() {
      return t.getIdentifier();
    }
    @Override
    public int getX() {
      return t.getRelativeX(e);
    }
    @Override
    public int getY() {
      return t.getRelativeY(e);
    }
    @Override
    public int getScreenX() {
      return t.getScreenX();
    }
    @Override
    public int getScreenY() {
      return t.getScreenY();
    }
    @Override
    public int getClientX() {
      return t.getClientX();
    }
    @Override
    public int getClientY() {
      return t.getClientY();
    }
    @Override
    public long getTimestamp() {
      return stamp;
    }
	  @Override
	  public boolean isShiftDown() {
		return ev.isShiftKeyDown();
	  }
	  @Override
	  public boolean isControlDown() {
		return ev.isControlKeyDown();
	  }      

  }
  
  /**
   * 
   */
  /**
   * @param viewer
   */
  public GWTMultiTouchHandler(MouseConsumer viewer) {
    this.viewer = viewer;
  }

  @Override
  public void onTouchStart(TouchStartEvent event) {
    Element e = event.getRelativeElement();
    long stamp = System.currentTimeMillis();
    JsArray<Touch> touches = event.getChangedTouches();
    int len = touches.length();
    for (int i = 0; i < len; i++) {
      Touch touch = touches.get(i);
      int x = touch.getRelativeX(e);
      int y = touch.getRelativeY(e);
      int id = touch.getIdentifier();
      LOG.finest("touch start " + x + "," + y + "," + id);
      viewer.processMouseDown(new CTX(e, touch, stamp, event));
    }
    event.preventDefault();
    event.stopPropagation();
  }

  @Override
  public void onTouchMove(TouchMoveEvent event) {
    Element e = event.getRelativeElement();
    long stamp = System.currentTimeMillis();
    JsArray<Touch> touches = event.getChangedTouches();
    int len = touches.length();
    for (int i = 0; i < len; i++) {
      Touch touch = touches.get(i);
      int x = touch.getRelativeX(e);
      int y = touch.getRelativeY(e);
      int id = touch.getIdentifier();
      LOG.finest("touch move " + x + "," + y + "," + id);
      viewer.processMouseDrag(new CTX(e, touch, stamp, event));
    }
    event.preventDefault();
    event.stopPropagation();
  }

  @Override
  public void onTouchEnd(TouchEndEvent event) {
    Element e = event.getRelativeElement();
    long stamp = System.currentTimeMillis();
    JsArray<Touch> touches = event.getChangedTouches();
    int len = touches.length();
    for (int i = 0; i < len; i++) {
      Touch touch = touches.get(i);
      int x = touch.getRelativeX(e);
      int y = touch.getRelativeY(e);
      int id = touch.getIdentifier();
      LOG.finest("touch end " + x + "," + y + "," + id);
      viewer.processMouseUp(new CTX(e, touch, stamp, event));
    }
    event.preventDefault();
    event.stopPropagation();
  }

  @Override
  public void onTouchCancel(TouchCancelEvent event) {
    Element e = event.getRelativeElement();
    long stamp = System.currentTimeMillis();
    JsArray<Touch> touches = event.getChangedTouches();
    int len = touches.length();
    for (int i = 0; i < len; i++) {
      Touch touch = touches.get(i);
       viewer.processMouseUp(new CTX(e, touch, stamp, event));
    }
    event.preventDefault();
    event.stopPropagation();
  }
}
