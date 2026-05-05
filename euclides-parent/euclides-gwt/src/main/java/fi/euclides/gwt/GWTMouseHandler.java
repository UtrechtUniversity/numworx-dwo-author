package fi.euclides.gwt;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;

public class GWTMouseHandler
implements MouseDownHandler, MouseUpHandler, MouseMoveHandler {

    private static class CTX implements MouseContext {
      private final MouseEvent<?> ev;
      private final long stamp;
      private CTX(MouseEvent<?> ev) {
        this.ev = ev;
        stamp = System.currentTimeMillis();
      }
      @Override
      public int getID() {
        return 0;
      }
      @Override
      public int getX() {
        return ev.getX();
      }
      @Override
      public int getY() {
        return ev.getY();
      }
      @Override
      public int getScreenX() {
        return ev.getScreenX();
      }
      @Override
      public int getScreenY() {
        return ev.getScreenY();
      }
      @Override
      public int getClientX() {
        return ev.getClientX();
      }
      @Override
      public int getClientY() {
        return ev.getClientY();
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
	 * @param viewer Widget
	 */
	public GWTMouseHandler(MouseConsumer viewer) {
		this.viewer = viewer;
	}

	protected boolean mouseDown;
	private MouseConsumer viewer;

	public void onMouseDown(MouseDownEvent event) {
		if(event.getNativeButton() == NativeEvent.BUTTON_LEFT)
		{
			mouseDown = true;
			viewer.processMouseDown(new CTX(event));
		}
	}
	public void onMouseUp(MouseUpEvent event) {
		if(event.getNativeButton() == NativeEvent.BUTTON_LEFT)
		{
			mouseDown = false;
			//viewer.moveBack();
			viewer.processMouseUp(new CTX(event));
		}
	}
	public void onMouseMove(MouseMoveEvent event) {
		if(mouseDown && event.getNativeButton() == NativeEvent.BUTTON_LEFT)
		{	//viewer.moveAway(x, y);
			viewer.processMouseDrag(new CTX(event));
		}
	}
	
}