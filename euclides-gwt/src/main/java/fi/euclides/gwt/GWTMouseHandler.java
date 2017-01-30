package fi.euclides.gwt;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;

import fi.euclides.gwt.canvas.SpeelVeld;

public class GWTMouseHandler
implements MouseDownHandler, MouseUpHandler, MouseMoveHandler {
	/**
	 * 
	 */
	//private final GWTEuclides gwtEuclides;

	/**
	 * @param viewer Widget
	 */
	public GWTMouseHandler(ViewerWidget viewer) {
		this.viewer = viewer;
	}

	protected boolean mouseDown;
	private ViewerWidget viewer;

	public void onMouseDown(MouseDownEvent event) {
		int x = event.getX();
		int y = event.getY();
		if(event.getNativeButton() == NativeEvent.BUTTON_LEFT)
		{
			mouseDown = true;
			viewer.processMouseDown(x,y);
		}
	}
	public void onMouseUp(MouseUpEvent event) {
		int x = event.getX();
		int y = event.getY();
		if(event.getNativeButton() == NativeEvent.BUTTON_LEFT)
		{
			mouseDown = false;
			//viewer.moveBack();
			viewer.processMouseUp(x,y);
		}
	}
	public void onMouseMove(MouseMoveEvent event) {
		int x = event.getX();
		int y = event.getY();
		if(mouseDown && event.getNativeButton() == NativeEvent.BUTTON_LEFT)
		{	//viewer.moveAway(x, y);
			viewer.processMouseDrag(x,y);
		}
	}
	
}