package fi.euclides.gwt;

import java.util.logging.Logger;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Touch;
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
  private ViewerWidget viewer;

  /**
   * 
   */
  /**
   * @param viewer
   */
  public GWTMultiTouchHandler(ViewerWidget viewer) {
    this.viewer = viewer;
  }

  @Override
  public void onTouchStart(TouchStartEvent event) {
    Element e = event.getRelativeElement();
    JsArray<Touch> touches = event.getChangedTouches();
    int len = touches.length();
    for (int i = 0; i < len; i++) {
      Touch touch = touches.get(i);
      int x = touch.getRelativeX(e);
      int y = touch.getRelativeY(e);
      int id = touch.getIdentifier();
      LOG.fine("touch start " + x + "," + y + "," + id);
      viewer.processMouseDown(x, y, id);
    }
    event.preventDefault();
    event.stopPropagation();
  }

  @Override
  public void onTouchMove(TouchMoveEvent event) {
    Element e = event.getRelativeElement();
    JsArray<Touch> touches = event.getChangedTouches();
    int len = touches.length();
    for (int i = 0; i < len; i++) {
      Touch touch = touches.get(i);
      int x = touch.getRelativeX(e);
      int y = touch.getRelativeY(e);
      int id = touch.getIdentifier();
      LOG.fine("touch move " + x + "," + y + "," + id);
      viewer.processMouseDrag(x, y, id);
    }
    event.preventDefault();
    event.stopPropagation();
  }

  @Override
  public void onTouchEnd(TouchEndEvent event) {
    Element e = event.getRelativeElement();
    JsArray<Touch> touches = event.getChangedTouches();
    int len = touches.length();
    for (int i = 0; i < len; i++) {
      Touch touch = touches.get(i);
      int x = touch.getRelativeX(e);
      int y = touch.getRelativeY(e);
      int id = touch.getIdentifier();
      LOG.fine("touch end " + x + "," + y + "," + id);
      viewer.processMouseUp(x, y, id);
    }
    event.preventDefault();
    event.stopPropagation();
  }

  @Override
  public void onTouchCancel(TouchCancelEvent event) {
    Element e = event.getRelativeElement();
    JsArray<Touch> touches = event.getChangedTouches();
    int len = touches.length();
    for (int i = 0; i < len; i++) {
      Touch touch = touches.get(i);
      int x = touch.getRelativeX(e);
      int y = touch.getRelativeY(e);
      int id = touch.getIdentifier();
      LOG.fine("touch cancel " + x + "," + y + "," + id);
      viewer.processMouseUp(x, y, id);
    }
    event.preventDefault();
    event.stopPropagation();
  }
}
