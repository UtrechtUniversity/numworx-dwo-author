package fi.euclides.gwt;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import com.google.gwt.dom.client.NativeEvent;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.vaadin.pointerevents.client.PointerCancelEvent;
import com.vaadin.pointerevents.client.PointerCancelHandler;
import com.vaadin.pointerevents.client.PointerDownEvent;
import com.vaadin.pointerevents.client.PointerDownHandler;
import com.vaadin.pointerevents.client.PointerEvent;
import com.vaadin.pointerevents.client.PointerMoveEvent;
import com.vaadin.pointerevents.client.PointerMoveHandler;
import com.vaadin.pointerevents.client.PointerUpEvent;
import com.vaadin.pointerevents.client.PointerUpHandler;

public class GWTPointerHandler
		implements PointerCancelHandler, PointerDownHandler, PointerMoveHandler, PointerUpHandler {

	final private static Logger LOG = Logger.getLogger("GWTPointerHandler");

	final MouseConsumer viewer;
	final Set<Integer> bitset = new HashSet<>();
	
	private HandlerRegistration mouse;
	
	public void setLegacy(HandlerRegistration legacy) {
		mouse = legacy;
	}
	private void noLegacy() {
		if (mouse != null) {
			mouse.removeHandler();
			mouse = null;
		}
	}
	
	
	private static class CTX implements MouseContext {

		final PointerEvent<?> ev;
		final long stamp;
		
		private CTX(PointerEvent<?> ev) {
			this.ev = ev;
	        stamp = System.currentTimeMillis();
		}

		@Override
		public int getID() {
			return ev.getPointerId();
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
	
	public GWTPointerHandler(MouseConsumer viewer) {
		this.viewer = viewer;
	}

	@Override
	public void onPointerUp(PointerUpEvent event) {
		event.preventDefault();
		if(event.getNativeButton() == NativeEvent.BUTTON_LEFT)
		{
			LOG.fine("up " + event+ " " + bitset);
			CTX ctx = new CTX(event);
			viewer.processMouseUp(ctx);
			bitset.remove(event.getPointerId());
		}
	}

	@Override
	public void onPointerMove(PointerMoveEvent event) {
		event.preventDefault();
		if (bitset.contains(event.getPointerId())) {
			CTX ctx = new CTX(event);
			viewer.processMouseDrag(ctx);			
		}
	}

	@Override
	public void onPointerDown(PointerDownEvent event) {
		event.preventDefault();
		noLegacy();
		if(event.getNativeButton() == NativeEvent.BUTTON_LEFT)
		{
			LOG.fine("down " + event + " " + bitset);
			CTX ctx = new CTX(event);
			bitset.add(event.getPointerId());
			viewer.processMouseDown(ctx);
		}
	}

	@Override
	public void onPointerCancel(PointerCancelEvent event) {
		event.preventDefault();
		LOG.info("cancel " + event + " " + bitset);
		bitset.remove(event.getPointerId());
	}

}
