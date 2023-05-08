package fi.euclides.gwt;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.dom.client.TouchCancelEvent;
import com.google.gwt.event.dom.client.TouchCancelHandler;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchEvent;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchMoveHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;

public class GWTTouchHandler 
implements TouchStartHandler, TouchMoveHandler, TouchEndHandler, TouchCancelHandler {

	private ViewerWidget viewer;
	/**
	 * 
	 */
	/**
	 * @param viewer
	 */
	public GWTTouchHandler(ViewerWidget viewer) {
		this.viewer = viewer;
	}
	private Touch current;
	private Touch getTouch(TouchEvent<?> event) {
		if(current != null)
		{
			int len = event.getTouches().length();
			for(int i = 0; i < len; i ++)
			{
				if(event.getTouches().get(i).getIdentifier() == current.getIdentifier())
					return event.getTouches().get(i);
			}
			return null;
		}
		Touch touch = event.getTouches().get(0);
		return touch;
	}
	int x,y;
	
	@Override
	public void onTouchStart(TouchStartEvent event) {
		if(current != null)
			return;
		Element e = event.getRelativeElement();
		Touch touch = getTouch(event);
		current = touch;
		x = touch.getRelativeX(e);
		y = touch.getRelativeY(e);
		int id = touch.getIdentifier();
		viewer.processMouseDown(x,y,id);
		event.getNativeEvent().preventDefault();
		event.getNativeEvent().stopPropagation();
	}
	@Override
	public void onTouchMove(TouchMoveEvent event) {
		Touch touch = getTouch(event);
		if(touch != null) 
		{
			Element element = event.getRelativeElement();
			x = touch.getRelativeX(element);
			y = touch.getRelativeY(element);
			int id = touch.getIdentifier();
			viewer.moveAway(x, y);
			viewer.processMouseDrag(x,y,id);
			event.getNativeEvent().preventDefault();
			event.getNativeEvent().stopPropagation();
		}
	}

	@Override
	public void onTouchEnd(TouchEndEvent event) {
		Touch touch = getTouch(event);
		if(current != null && touch == null)
		{	viewer.moveBack();
			viewer.processMouseUp(x, y,current.getIdentifier());
			current = null;
			event.getNativeEvent().preventDefault();
			event.getNativeEvent().stopPropagation();
		}
	}

	@Override
	public void onTouchCancel(TouchCancelEvent event) {
		Touch touch = getTouch(event);
		if(current != null && touch == null)
		{	viewer.moveBack();
			viewer.processMouseUp(x, y, current.getIdentifier());
			current = null;
			event.getNativeEvent().preventDefault();
			event.getNativeEvent().stopPropagation();
		}
	}
}